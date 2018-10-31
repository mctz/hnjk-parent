package com.hnjk.security.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.security.service.IResourceService;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.service.IRoleService;

/**
 * 系统安全权限-角色管理服务. <code>RoleServiceImpl</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-11 下午05:49:49
 * @see 
 * @version 1.0
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl extends BaseServiceImpl<Role> implements IRoleService{
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持

	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;

	/*
	 *  根据所选资源ID更新保存角色	
	 */
	@Override
	public void saveOrUpdateRole(Role role, List<String> ids)  throws ServiceException{
		if(ExStringUtils.isBlank(role.getResourceid())){
			//校验重复性
			Role r = findUniqueByProperty("roleCode", role.getRoleCode());
			if(null != r){
				throw new ServiceException("已存在相同编号的角色，请重新输入！");
			}
		}
		if(null != ids && ids.size()>0){
			List<Resource> newList = new ArrayList<Resource>();
			//FIXED 查询in  超过1000的query error
			//List list = exGeneralHibernateDao.findEntitysByIds(Resource.class, ids);//查找资源列表
			List<Resource> list = (List<Resource>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).getList(CacheSecManager.CACHE_SEC_RESOURCE, Resource.class);
			if(null == list){
				list = (List<Resource>)exGeneralHibernateDao.findByHql("from "+Resource.class.getSimpleName()+" where isDeleted = ?", 0);
			}
			for(Resource resource : list){
				for(String id : ids){
					if(id.equals(resource.getResourceid())){
						newList.add(resource);
						break;
					}
				}
			}

			List<Resource> delList = new ArrayList<Resource>();

			for(Resource oldRes : role.getAuthoritys()){//剔除删除的
				if(!ids.contains(oldRes.getResourceid())){
					delList.add(oldRes);
				}
			}
			role.getAuthoritys().removeAll(delList);
			if(ExCollectionUtils.isNotEmpty(newList)){
				role.getAuthoritys().addAll(newList);
			}

		}
		saveOrUpdate(role);
		reloadResourceRoleRef(role);//重新加载角色授权资源

	}	

	/*
	 * 根据角色编码获取角色对象
	 */
	@Override
	@Transactional(readOnly=true)
	public Role getRoleByRolecode(String rolecode) throws ServiceException {	
		return (Role) exGeneralHibernateDao.findUniqueByProperty(Role.class,"roleCode",rolecode);
	}

	//根据条件查找
	@Override
	@Transactional(readOnly=true)
	public Page findRoleByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("roleCode")){//根据资源编码
			objCriterion.add(Restrictions.eq("roleCode",condition.get("roleCode")));
		}
		if(condition.containsKey("roleName")){//资源名称
			objCriterion.add(Restrictions.like("roleName","%"+condition.get("roleName")+"%"));
		}
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		
		return exGeneralHibernateDao.findByCriteria(Role.class,page,objCriterion.toArray(new Criterion[objCriterion.size()]));
	}

	/*
	 * 批量删除
	 * (non-Javadoc)
	 * @see com.hnjk.security.service.IRoleService#deleteArray(java.lang.String[])
	 */
	@Override
	public void deleteArray(String[] ids) throws ServiceException{
		if(ids.length>0){
			for(int index=0;index<ids.length;index++){
				deleteRole(ids[index]);
			}
		}
	}

	/*
	 * 单个删除
	 */
	@Override
	public void deleteRole(String roleid) throws ServiceException {
		Role role = get(roleid);
		Role parentRole = role.getParent();
		if(null != parentRole){
			parentRole.getChildren().remove(role);
			update(parentRole);
		}else{
			delete(role);
		}
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLES).remove(roleid);		
	}
	
	
	/*
	 *重新加载角色授权资源
	 */
	private void reloadResourceRoleRef(Role role) {
		Map<String, Set<String>> resRoleMap = (Map<String, Set<String>>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE)
				.get(CacheSecManager.CACHE_SEC_ROLE_RESOURCE);

		String value = role.getRoleCode();
		for(Resource resource : role.getAuthoritys()) {//更新缓存该角色缓存 key - url : value - rolecode
			String key = resource.getResourcePath();
			if(key == null || "".equalsIgnoreCase(key)) {
				continue;
			}

			Set<String> roleSet = resRoleMap.get(key);//获取该资源的角色列表
			if(null == roleSet){
				roleSet = new HashSet<String>();
				roleSet.add(value);
				resRoleMap.put(key, roleSet);
				continue;
			}
			if(!roleSet.contains(value)){
				roleSet.add(value);
			}
		}

		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE).remove(CacheSecManager.CACHE_SEC_ROLE_RESOURCE);
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLES).remove(role.getResourceid());
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE).put(CacheSecManager.CACHE_SEC_ROLE_RESOURCE, resRoleMap);//更新
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLES).put(role.getResourceid(),role);	// 将角色放入缓存
			
		logger.info("加载系统角色："+role.getRoleName()+",资源-角色映射："+resRoleMap.size()+"条...");
		
	}

	private List<Resource> getAuthorityListByRole(Role role) {
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("roleid",role.getResourceid());
		return resourceService.findBySql(condition);
	}

	//查找角色树
	@Override
	@Transactional(readOnly=true)
	public List<Role> findRoleTreeList(String parentCode) throws ServiceException {
		String sql = "select * from hnjk_sys_roles t where t.isdeleted=0  start with ";
		if(ExStringUtils.isNotEmpty(parentCode)){
			sql += " t.roleCode = '"+parentCode+"' ";
		}else{
			sql += " t.parentid is null ";
		}
		 
		 sql += " connect by prior t.resourceid=t.parentid order siblings by t.showOrder";
		try {
			return jdbcDao.getBaseJdbcTemplate().findList(sql, Role.class, null);
		} catch (Exception e) {
			throw new ServiceException(e);
		}		
	}

	/**
	 * 根据角色编码集合获取名称集合
	 * 
	 * @param codes
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNamesByCodes(String codes) throws ServiceException {
		String roleNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("codes", Arrays.asList(codes.split(",")));
			String sql = "select max(re) roleNames from (select wm_concat(r.rolename) over (order by r.rolecode) re  from hnjk_sys_roles r where r.rolecode in (:codes)) ";
			Map<String, Object> roleNamesMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(roleNamesMap!=null && roleNamesMap.size()>0){
				roleNames = (String)roleNamesMap.get("roleNames");
			}
		} catch (Exception e) {
			logger.error("根据角色编码集合获取名称集合出错", e);
		}
		return roleNames;
	}

	@Override
	public String getAuthoritysByRole(Role role) throws ServiceException {
		if (role == null) {
			return "";
		}
		//生成XML文档，并使用clob类型输出
		StringBuilder sql = new StringBuilder("select XMLAGG(XMLELEMENT(E, ra.AUTHORITYID || ',')).EXTRACT('//text()').getclobval() aids from hnjk_sys_roleauthority ra where ra.roleid = '"+role.getResourceid()+"'");
		String authorityStr = "";
		try {
			Map<String,Object> resultMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql.toString(),null);
			//for (Map<String, Object> map : resultMap) {
				authorityStr = resultMap.get("aids").toString();
			//}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return authorityStr.toString();
	}

}
