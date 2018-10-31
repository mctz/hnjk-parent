package com.hnjk.edu.portal.service.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.service.IChannelService;

/**
 *  <code>ChannelServiceImpl</code>门户栏目服务实现.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-20 下午03:06:08
 * @see 
 * @version 1.0
 */
@Service("channelService")
@Transactional
public class ChannelServiceImpl extends BaseServiceImpl<Channel> implements IChannelService {
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持
	
	@Override
	public void delete(Channel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_PORTAL_CHANNEL).remove(entity.getResourceid());//从内存中删除
		exGeneralHibernateDao.delete(entity);
	}

	@Override
	public void delete(Serializable id) throws ServiceException {
		Channel channel = (Channel)exGeneralHibernateDao.get(Channel.class, id);
		delete(channel);
	}

	@Override
	public Channel save(Channel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_PORTAL_CHANNEL).put(entity.getResourceid(), entity);
		return super.save(entity);
	}

	@Override
	public void saveOrUpdate(Channel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_PORTAL_CHANNEL).remove(entity.getResourceid());
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_PORTAL_CHANNEL).put(entity.getResourceid(), entity);
		super.saveOrUpdate(entity);
	}

	@Override
	public void update(Channel entity) throws ServiceException {
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_PORTAL_CHANNEL).remove(entity.getResourceid());
		//MemeryCacheManager.getCache(CacheAppManager.CACHE_PORTAL_CHANNEL).put(entity.getResourceid(), entity);
		super.update(entity);
	}

	/*
	 * 根据条件查找栏目列表
	 * (non-Javadoc)
	 * @see com.hnjk.edu.portal.service.IChannelService#findChannelByCondition(java.util.Map, com.hnjk.core.rao.dao.helper.Page)
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findChannelByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("channelName")){
			objCriterion.add(Restrictions.like("channelName","%"+condition.get("channelName")+"%"));
		}
		if(condition.containsKey("channelPosition")){
			objCriterion.add(Restrictions.eq("channelPosition",condition.get("channelPosition")));
		}
		
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		
		return exGeneralHibernateDao.findByCriteria(Channel.class,page,objCriterion.toArray(new Criterion[objCriterion.size()]));
	}


	@Override
	@Transactional(readOnly=true)
	public List<Channel> findChannelByParentAndchild() throws ServiceException {			
		return findChannelByParentAndchild(null);
	}

	@Override
	@Transactional(readOnly=true)
	public List<Channel> findChannelByParentAndchild(Map<String, Object> condition) throws ServiceException {
		String hql = "select *  from edu_portal_channel t  where t.channelstatus =0 and t.isdeleted=0 ";
		if(null != condition &&condition.containsKey("channelName")){
			hql+=" and t.channelName like '%"+condition.get("channelName")+"%' ";
		}
		if(null != condition && condition.containsKey("channelPosition")){
			hql += " and t.channelPosition ='"+condition.get("channelPosition")+"' ";
		}
		hql += "start with t.resourceid = ?  connect by prior t.resourceid = t.parentid";
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();		
		SQLQuery query = session.createSQLQuery(hql).addEntity(Channel.class);
		if(null != condition && condition.containsKey("parentId")){
			query.setParameter(0, condition.get("parentId"));
		}else {
			query.setParameter(0, Channel.CHANNEL_ROOT);	
		}				
		return query.list();
	}

	/* (non-Javadoc)
	 * @see com.hnjk.edu.portal.service.IChannelService#findChannelTree(java.lang.String)
	 */
	@Override
	public List<Channel> findChannelTree(String parentCode) throws Exception {
		String sql = "select * from edu_portal_channel t where t.isdeleted=0 and t.channelstatus =0  ";
		//目前写死代码，开始
//		sql +=" and (t.resourceid in ('0', '402881a044aaf4a40144ab06a0dc0001') or t.channelname like '%资料库%')";
		//--结束
		sql +=" start with ";
		if(ExStringUtils.isNotEmpty(parentCode)){
			sql += " t.resourceid = '"+parentCode+"' ";
		}else{
			sql += " t.parentid is null ";
		}
		
		sql += " connect by prior t.resourceid=t.parentid ";
		List<Channel> list = jdbcDao.getBaseJdbcTemplate().findList(sql, Channel.class, null);
		return list;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Channel> findCourseChannelByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder("select *  from edu_portal_channel t ");
		hql.append(" where t.isdeleted=0 ");
		if(condition.containsKey("channelName")){
			hql.append(" and t.channelName like :channelName ");
			params.put("channelName", "%"+condition.get("channelName")+"%");
		}
		if(condition.containsKey("courseId")){
			hql.append(" and t.courseid =:courseId ");
			params.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("channelStatus")){
			hql.append(" and t.channelstatus =:channelStatus ");
			params.put("channelStatus", condition.get("channelStatus"));
		}
		if(condition.containsKey("channelLevel")){
			hql.append(" and t.channelLevel =:channelLevel ");
			params.put("channelLevel", condition.get("channelLevel"));
		}
		if(condition.containsKey("channelPosition")){
			hql.append(" and t.channelPosition =:channelPosition ");
			params.put("channelPosition", condition.get("channelPosition"));
		}
		if(condition.containsKey("channelType")){
			hql.append(" and t.channelType =:channelType ");
			params.put("channelType", condition.get("channelType"));
		}
		if(condition.containsKey("chanelIds")){
			hql.append(" and t.resourceid in ("+condition.get("chanelIds")+")");
		}
		hql.append(" start with ");
		if(condition.containsKey("parentId")){
			hql.append(" t.resourceid=:parentId ");
			params.put("parentId", condition.get("parentId"));
		} else {
			hql.append(" t.parentid is null ");
		}
		hql.append(" connect by prior t.resourceid = t.parentid ");
		hql.append(" order siblings by t.channelLevel,t.showOrder,t.resourceid ");
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();		
		SQLQuery query = session.createSQLQuery(hql.toString()).addEntity(Channel.class);
		query.setProperties(params);	
		return query.list();
	}
	
}
