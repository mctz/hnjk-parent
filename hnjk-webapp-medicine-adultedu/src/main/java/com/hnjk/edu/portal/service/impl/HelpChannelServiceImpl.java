package com.hnjk.edu.portal.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.portal.model.HelpChannel;
import com.hnjk.edu.portal.service.IHelperChannelService;
/**
 *  <code>ChannelServiceImpl</code>门户栏目服务实现.<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-9-14 
 * @see 
 * @version 1.0
 */
@Service("helperChannelService")
@Transactional
public class HelpChannelServiceImpl extends BaseServiceImpl<HelpChannel> implements
		IHelperChannelService {
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持
	/**
	 * 添加栏目节点
	 */
	@Override
	public HelpChannel save(HelpChannel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_HELP_CHANNEL).put(entity.getResourceid(), entity);
		return super.save(entity);
	}
	/**
	 * 删除栏目节点
	 */
	@Override
	public void delete(Serializable id) throws ServiceException {
		HelpChannel helpchannel = (HelpChannel)exGeneralHibernateDao.get(HelpChannel.class, id);
		delete(helpchannel);
	}
	/**
	 * 添加或更新栏目节点
	 */
	@Override
	public void saveOrUpdate(HelpChannel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_HELP_CHANNEL).remove(entity.getResourceid());
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_HELP_CHANNEL).put(entity.getResourceid(), entity);
		super.saveOrUpdate(entity);
	}
	/**
	 * 更新栏目节点
	 */
	@Override
	public void update(HelpChannel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_HELP_CHANNEL).remove(entity.getResourceid());
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_HELP_CHANNEL).put(entity.getResourceid(), entity);
		super.saveOrUpdate(entity);

	}
	/**
	 * 按条件查找栏目列表
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findHelpChannelByCondition(Map<String, Object> condition,
			Page page) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("helpchannelname")){
			objCriterion.add(Restrictions.like("channelName", "%"+condition.get("channelName")+"%"));
		}
		objCriterion.add(Restrictions.eq("isDeleted", 0));
		return exGeneralHibernateDao.findByCriteria(HelpChannel.class, page, objCriterion.toArray(new Criterion[objCriterion.size()]));
	}
	
	/**
	 * 返回栏目的树形列表
	 */
	@Override
	@Transactional(readOnly=true)
	public List<HelpChannel> findHelpChannelByParentAndchild() throws ServiceException{
		return findHelpChannelByParentAndchild(null);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<HelpChannel> findHelpChannelByParentAndchild(Map<String, Object> condition) throws ServiceException{
		String hql = "select * from EDU_HELP_CHANNEL t where t.isdeleted = 0 ";
		if(null != condition && condition.containsKey("helpChannelName")){
			hql += " and t.channelName like '%"+condition.get("helpChannelName")+"%' ";
		}
		if(null != condition && condition.containsKey("helpChannelNameN")){
			hql += " and t.channelName != '"+condition.get("helpChannelNameN")+"' ";
		}
		if(null != condition && condition.containsKey("isChild") ){
			hql += " and t.isChild ='"+condition.get("isChild")+"' ";
		}
		if(null != condition && condition.containsKey("parentCode")){
			hql += " start with t.resourceid = '"+condition.get("parentCode")+"' connect by prior t.resourceid = t.parentid";
		}
		Session session = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(hql).addEntity(HelpChannel.class);
		return query.list();
	}
	/**
	 * 全部查询
	 */
	@Override
	public List<HelpChannel> findHelpChannelTree(String parentCode) throws Exception{
		String sql = "select * from EDU_HELP_CHANNEL t where t.isdeleted=0 start with ";
		if(ExStringUtils.isNotEmpty(parentCode)){
			sql += " t.resourceid = '"+parentCode+"' ";
		}else{
			sql += " t.parentid is null ";
		}
		sql += " connect by prior t.resourceid = t.parentid ";
		List<HelpChannel> list =jdbcDao.getBaseJdbcTemplate().findList(sql, HelpChannel.class, null);
		return list;
	}
	
	
}
