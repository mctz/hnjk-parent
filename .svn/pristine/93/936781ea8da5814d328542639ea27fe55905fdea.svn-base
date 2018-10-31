package com.hnjk.edu.teaching.service.impl;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.QueryParameter;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.CourseBook;
import com.hnjk.edu.teaching.service.ICourseBookService;

/**
 * 课程教材管理实现接口.
 * <code>CourseBookServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-8 上午11:48:57
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseBookService")
public class CourseBookServiceImpl extends BaseServiceImpl<CourseBook> implements ICourseBookService{

	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseBookByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
	
		Criteria objCriterion = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createCriteria(CourseBook.class, "coursebook")
												.createAlias("coursebook.course", "course");
		if(condition.containsKey("courseName")){
			objCriterion.add(Restrictions.ilike("course.courseName","%"+condition.get("courseName")+"%"));
		}
		if(condition.containsKey("courseCode")){
			objCriterion.add(Restrictions.eq("course.courseCode",condition.get("courseCode")));
		}
		if(condition.containsKey("bookName")){
			objCriterion.add(Restrictions.ilike("coursebook.bookName","%"+condition.get("bookName")+"%"));
		}
		if(condition.containsKey("status")){
			objCriterion.add(Restrictions.eq("coursebook.status",Long.parseLong(condition.get("status").toString())));
		}
		
		if(condition.containsKey("schoolType")){
			objCriterion.add(Restrictions.eq("coursebook.schoolType",condition.get("schoolType").toString()));
		}
		
		if(condition.containsKey("branchSchool")){
			objCriterion.add(Restrictions.eq("coursebook.brSchool.resourceid",condition.get("branchSchool").toString()));
		}

		if(condition.containsKey("hasUsedCoursebookId")){//排除掉教学计划中已使用的课程
			objCriterion.add(Restrictions.not(Restrictions.in("resourceid", (List<String>)condition.get("hasUsedCoursebookId"))));
		}
		
		objCriterion.add(Restrictions.eq("coursebook.isDeleted", 0));//是否删除 =0
		
		//排序
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy()) && ExStringUtils.isNotEmpty(objPage.getOrder())){
			if(QueryParameter.ASC.equals(objPage.getOrder())){
				objCriterion.addOrder(Order.asc(objPage.getOrderBy()));
			}else {
				objCriterion.addOrder(Order.desc(objPage.getOrderBy()));
			}
		}
		
		return exGeneralHibernateDao.findByCriteriaSession(CourseBook.class, objPage, objCriterion);		
	}

	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}

	@Override
	public void deleteCourseBook(String id) {
		exGeneralHibernateDao.delete(CourseBook.class, id);
	}

	/* (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ICourseBookService#getPublishCompanies()
	 */
	@Override
	@Transactional(readOnly=true)
	public List<String> getPublishCompanies() {		
		return (List<String>)exGeneralHibernateDao.findByHql("select distinct publishCompany from "+CourseBook.class.getSimpleName()+ " where isDeleted = ?", 0);
	}
	
	
}
