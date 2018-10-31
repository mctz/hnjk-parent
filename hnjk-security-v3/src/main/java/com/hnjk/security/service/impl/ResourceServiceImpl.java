package com.hnjk.security.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.service.IResourceService;

/**
 * 系统安全权限-资源授权管理服务. <code>ResourceServiceImpl</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-11 下午05:49:11
 * @see 
 * @version 1.0
 */
@Transactional
@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl<Resource> implements IResourceService{

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao jdbcDao;
	/*
	 * 删除授权资源，并跟新缓存
	 * 不能直接进行删除，只需要维护父子关系。
	 */
	@Override
	public void deleteResource(String resourceid) throws ServiceException {
		//判断改权限是否被分配，如果分配了则不能删除
		String sql = "select t.authorityid from  hnjk_sys_roleauthority t where t.authorityid=:id";
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql);
		query.setParameter("id",resourceid);		
		List list = query.list();
				
		if(null != list && list.contains(resourceid)){
			throw new ServiceException("权限资源:"+get(resourceid).getResourceName()+" 已被分配，不能删除！");
		}
		
		Resource resource = get(resourceid);
		removeCacheResource(resource);//同步缓存
		Resource parent = resource.getParent();
		parent.getChildren().remove(resource);
		update(parent);		
	}
	
	/*
	 * 更新资源
	 */	
	@Override
	public void updateResource(Resource resource) throws ServiceException{
		exGeneralHibernateDao.update(resource);
		updateCacheResource(resource);
	}
	
	/*
	 * 获取授权资源
	 */
	@Override
	@Transactional(readOnly=true)
	public GrantedAuthority[] getAuthoritys(String resourcePath) throws ServiceException {
		Resource resource = (Resource) exGeneralHibernateDao.findUniqueByProperty(Resource.class,"resourcePath", resourcePath);
		GrantedAuthority[] gas = new GrantedAuthority[1];
		gas[0] = new GrantedAuthorityImpl(resource.getResourceCode());
		return gas;
	}
	
	/*
	 * 保存资源授权
	 */
	@Override
	public void saveResource(Resource resource) throws ServiceException {
		exGeneralHibernateDao.save(resource);		
		updateCacheResource(resource);
	}
	
	//根据条件查找资源
	@Override
	@Transactional(readOnly = true)
	public Page findResourceByCondition(Map<String, Object> condition,  Page page) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Resource.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("resourceCode")){
			hql += " and resourceCode = :resourceCode ";
			values.put("resourceCode", condition.get("resourceCode"));
		}
		if(condition.containsKey("resourceName")){
			hql += " and resourceName like :resourceName ";
			values.put("resourceName", "%"+condition.get("resourceName")+"%");
		}
		hql += " and parent.resourceid is not null ";
		if(condition.containsKey("resId")){
			hql += " and parent.resourceid =:resid0 ";
			values.put("resid0", condition.get("resId"));
		}
		hql += " order by "+page.getOrderBy() +" "+ page.getOrder();

		return exGeneralHibernateDao.findByHql(page, hql, values);		
	
	}

	//根据条件查找资源
	@Transactional(readOnly = true)
	@Override
	public List<Resource> findBySql (Map<String,Object> condition) throws ServiceException {
		StringBuilder sqlBuilder = new StringBuilder("select distinct r.* from HNJK_SYS_RESOURCES r join HNJK_SYS_ROLEAUTHORITY ra on ra.AUTHORITYID=r.RESOURCEID where r.ISDELETED=0 ");
		Map<String,Object> valueMap = new HashMap<String, Object>();
		if (condition.containsKey("roleid")) {
			sqlBuilder.append(" and ra.ROLEID = :roleid");
			valueMap.put("roleid",condition.get("roleid"));
		}
		try {
			return jdbcDao.getBaseJdbcTemplate().findList(sqlBuilder.toString(),Resource.class,valueMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Resource>();

	}

	/*
	 * 批量删除
	 * (non-Javadoc)
	 * @see com.hnjk.security.service.IResourceService#deleteCascadeArray(java.lang.String)
	 */
	@Override
	public void deleteCascadeArray(String ids) {
		String[] idArr = ids.split(",");
		if(idArr.length>0){
			for(int index=0;index<idArr.length;index++){
				deleteResource(idArr[index]);
			}
		}
	}

	@Override
	@Transactional(readOnly = true)
	public List<Resource> findResourceTree(Map<String, Object> condition) {
		StringBuffer sql  = new StringBuffer("select * from hnjk_sys_resources t where t.isdeleted=0 ");
		if(condition.containsKey("type")){//资源类型，如menu,func等
			sql.append(" and t.resourcetype=:type ");
		}
		sql.append( " start with ");
		if(condition.containsKey("code")){//编码，根据指定编码作为根
			sql.append( "  t.resourcecode=:code  ");
		 }else if(condition.containsKey("id")){//根据指定resourceid作为根
			 sql.append( "  t.parentid=:id  ");
		 }else{
			 sql.append(" t.parentid is null ");
		 }
		sql.append(" connect by prior t.resourceid=t.parentid order siblings by t.showorder,t.resourcecode");

		SQLQuery query = exGeneralHibernateDao.getSessionFactory().getCurrentSession()
						.createSQLQuery(sql.toString()).addEntity(Resource.class);
		
		//填充参数
		for(String key : condition.keySet()){
			query.setParameter(key, condition.get(key));
		}

		return  query.list();
	}
	
	//更新缓存
	private  void updateCacheResource(Resource res){	
		//List<Resource>	resList = findResourceCatalog(null,ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));	
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("code", ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
		List<Resource> resList = findResourceTree(condition);
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).removeall();
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).put(CacheSecManager.CACHE_SEC_RESOURCE, resList);
	}
	
	//删除缓存
	private void removeCacheResource(Resource res){
		@SuppressWarnings("unchecked")
		List<Resource> resList = (List<Resource>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).get(CacheSecManager.CACHE_SEC_RESOURCE);
		for (Resource re : resList) {
			if(res.getResourceid().equals(re.getResourceid())){
				resList.remove(re);	
				break;
			}
		}
	}
	
	
}
