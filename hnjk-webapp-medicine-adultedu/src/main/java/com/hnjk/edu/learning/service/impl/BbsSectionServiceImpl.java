package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.service.IBbsSectionService;
/**
 * 课程论坛版块管理服务接口实现
 * <code>BbsSectionServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-6 下午04:34:58
 * @see 
 * @version 1.0
 */
@Transactional
@Service("bbsSectionService")
public class BbsSectionServiceImpl extends BaseServiceImpl<BbsSection> implements IBbsSectionService {
	
	@Override
	@Transactional(readOnly=true)
	public Page findBbsSectionByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+BbsSection.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("sectionName")){//论坛版块名称
			hql += " and sectionName like :sectionName ";
			values.put("sectionName", "%"+condition.get("sectionName")+"%");
		}	
		if(condition.containsKey("masterName")){//版主名
			hql += " and masterName like :masterName ";
			values.put("masterName", "%"+condition.get("masterName")+"%");
		}
		if(condition.containsKey("bbsSectionId")){
			hql += " and resourceid = :bbsSectionId ";
			values.put("bbsSectionId", condition.get("bbsSectionId"));
		}
		if(condition.containsKey("isCourseSection")){
			hql += " and isCourseSection = :isCourseSection ";
			values.put("isCourseSection", condition.get("isCourseSection"));
		}
		
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void deleteBbsSection(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				try {
					Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
					String sql = "select * from edu_bbs_section t where t.isdeleted=0 start with t.resourceid=:resourceid connect by prior t.resourceid=t.parentid ";
					SQLQuery query = session.createSQLQuery(sql).addEntity(BbsSection.class);
					query.setParameter("resourceid",id);
					List<BbsSection> bbsSections = query.list();					
					for(BbsSection b : bbsSections){
						exGeneralHibernateDao.delete(BbsSection.class, b.getResourceid());
						logger.info("删除课程论坛="+id);
					}
				} catch (Exception e) {
					throw new ServiceException("删除课程论坛版块:"+e.getMessage());
				}
			}
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public boolean isExistsSectionCode(String sectionCode) throws ServiceException {
		BbsSection bbsSection = findUnique(" from "+ BbsSection.class.getSimpleName()+" where isDeleted=0 and sectionCode=? ", sectionCode);
		return (bbsSection != null ? true : false);
	}

	@Override
	@Transactional(readOnly=true)
	public Integer statTodayTopicCount(String bbsSectionId,String courseId) throws ServiceException {
		Long topics = null;
		Long replys = null;
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = "select count(b) from "+BbsTopic.class.getSimpleName()+" b where b.isDeleted=0 and b.bbsSection.resourceid=:bbsSectionId and b.fillinDate >=:today and b.parenTopic is null ";
		String hql2 = "select count(r) from "+BbsReply.class.getSimpleName()+" r where r.isDeleted=0 and r.bbsTopic.bbsSection.resourceid=:bbsSectionId and r.replyDate >=:today ";
		values.put("bbsSectionId", bbsSectionId);
		values.put("today", ExDateUtils.getToday());
		if(ExStringUtils.isNotEmpty(courseId)){
			hql += " and b.course.resourceid=:courseId ";
			hql2 += " and r.bbsTopic.course.resourceid=:courseId ";
			values.put("courseId", courseId);
		} 
		topics = exGeneralHibernateDao.findUnique(hql, values);
		replys = exGeneralHibernateDao.findUnique(hql2, values);
		topics = (topics==null)?0:topics;
		replys = (replys==null)?0:replys;	
		return (topics.intValue() + replys.intValue());
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer statInvitationCount(String bbsSectionId,String courseId) throws ServiceException {
		Long topics = null;
		Long replys = null;
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = "select count(b) from "+BbsTopic.class.getSimpleName()+" b where b.isDeleted=0 and b.bbsSection.resourceid=:bbsSectionId and b.parenTopic is null ";
		String hql2 = "select count(r) from "+BbsReply.class.getSimpleName()+" r where r.isDeleted=0 and r.bbsTopic.bbsSection.resourceid=:bbsSectionId ";
		values.put("bbsSectionId", bbsSectionId);
		if(ExStringUtils.isNotEmpty(courseId)){
			hql += " and b.course.resourceid=:courseId ";
			hql2 += " and r.bbsTopic.course.resourceid=:courseId ";
			values.put("courseId", courseId);
		} 
		topics = exGeneralHibernateDao.findUnique(hql, values);
		replys = exGeneralHibernateDao.findUnique(hql2, values);
		topics = (topics==null)?0:topics;
		replys = (replys==null)?0:replys;	
		return (topics.intValue() + replys.intValue());
	}
	
	@Override
	@Transactional(readOnly=true)
	public Integer statTopicCount(String bbsSectionId, String courseId) throws ServiceException {
		Long topics = null;
		if(ExStringUtils.isNotEmpty(courseId)){
			topics = exGeneralHibernateDao.findUnique("select count(b) from "+BbsTopic.class.getSimpleName()+" b where b.isDeleted=0 and b.bbsSection.resourceid=? and b.course.resourceid=?  and b.parenTopic is null ", new Object[]{bbsSectionId,courseId});
		} else {
			topics = exGeneralHibernateDao.findUnique("select count(b) from "+BbsTopic.class.getSimpleName()+" b where b.isDeleted=0 and b.bbsSection.resourceid=? ", bbsSectionId);
		}		
		topics = (topics==null)?0:topics;
		return topics.intValue();
	}

	@Override
	public boolean isModerator(String id) throws ServiceException {
		Long count = exGeneralHibernateDao.findUnique("select count(b) from "+BbsSection.class.getSimpleName()+" b where b.isDeleted=0 and b.masterId like ? ", "%"+id+"%");
		return (count==null||count.intValue()==0)?false:true;
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<BbsSection> findBbsSectionBy(String parentId) throws ServiceException {
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		String sql = "select * from EDU_BBS_SECTION s where s.isdeleted=0  start with s.resourceid=:resourceid connect by prior s.resourceid=s.parentid";
		SQLQuery query = session.createSQLQuery(sql).addEntity(BbsSection.class);
		query.setParameter("resourceid",parentId);
		return query.list();
	}	
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public Map<String, Object> getSectionList(String hql) {		
		List<BbsSection> parentBbsSections = findByHql(hql);
		Map<String,Object> map = new HashMap<String, Object>();
		for (BbsSection section : parentBbsSections) {
			List<BbsSection> bbsSection = findBbsSectionBy(section.getResourceid());
			map.put(section.getResourceid(), bbsSection);
		}
		return map;
	}
	
	@Override
	public Integer getNextShowOrder(String parentId) throws ServiceException {
		Integer showOrder = null;
		if(ExStringUtils.isNotEmpty(parentId)){
			showOrder = exGeneralHibernateDao.findUnique("select max(c.showOrder) from "+BbsSection.class.getSimpleName()+" c where c.isDeleted=0 and c.parent.resourceid=? ", parentId);
		} else {
			showOrder = exGeneralHibernateDao.findUnique("select max(c.showOrder) from "+BbsSection.class.getSimpleName()+" c where c.isDeleted=0 and c.parent is null ");
		}		
		if(showOrder==null) {
			showOrder = 0;
		}
		return ++showOrder;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BbsSection> findCourseBbsSection() throws ServiceException {
		List<BbsSection> courseSections = findByHql(" from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and isCourseSection='Y' and parent is not null order by sectionLevel,parent,showOrder ");
		return courseSections;
	}

	/**
	 * 根据编码获取版块
	 * @param sectionCode
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public BbsSection getBySectionCode(String sectionCode) throws ServiceException {
		String hql = "from "+BbsSection.class.getSimpleName()+" bs where bs.isDeleted=0 and bs.sectionCode=? ";
		return findUnique(hql, sectionCode);
	}	
}
