package com.hnjk.edu.teaching.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.service.ICourseService;

/**
 * 课程库管理服务接口.
 * <code>CourseServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-6 下午05:11:30
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseService")
public class CourseServiceImpl extends BaseServiceImpl<Course> implements ICourseService{
		
	/*
	 * 根据条件查询
	 * (non-Javadoc)
	 * @see com.hnjk.edu.teaching.service.ICourseService#findCourseByCondition(java.util.Map, com.hnjk.core.rao.dao.helper.Page)
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findCourseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("courseName")){
			objCriterion.add(Restrictions.ilike("courseName","%"+condition.get("courseName")+"%"));
		}
		if(condition.containsKey("courseCode")){
			objCriterion.add(Restrictions.eq("courseCode",condition.get("courseCode")));
		}

		if(condition.containsKey("status")){
			objCriterion.add(Restrictions.eq("status",Long.parseLong(condition.get("status").toString())));
		}
		
		if(condition.containsKey("isUniteExam")){
			objCriterion.add(Restrictions.eq("isUniteExam",condition.get("isUniteExam")));
		}
		if(condition.containsKey("isDegreeUnitExam")){
			objCriterion.add(Restrictions.eq("isDegreeUnitExam",condition.get("isDegreeUnitExam")));
		}
		if(condition.containsKey("courseType")){
			objCriterion.add(Restrictions.eq("courseType",condition.get("courseType")));
		}
		if(condition.containsKey("isPractice")){
			objCriterion.add(Restrictions.eq("isPractice",condition.get("isPractice")));
		}
		if (condition.containsKey("hasResource")) {
			if ("Y".equals(condition.get("hasResource"))) {
				objCriterion.add(Restrictions.eq("hasResource",condition.get("hasResource")));
			}else {
				objCriterion.add(Restrictions.or(Restrictions.eq("hasResource","N"), Restrictions.isNull("hasResource")));
			}
		}
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		
		return exGeneralHibernateDao.findByCriteria(Course.class, objPage, objCriterion.toArray(new Criterion[objCriterion.size()]));
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseByHql(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " select c from "+Course.class.getSimpleName()+" c where 1=1 ";
		hql += " and c.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);

		if(condition.containsKey("courseCode")){
			hql += " and c.courseCode =:courseCode ";
			values.put("courseCode", condition.get("courseCode"));
		}	
		if(condition.containsKey("status")){
			hql += " and c.status =:status ";
			values.put("status", Long.parseLong(condition.get("status").toString()));
		}
		if(condition.containsKey("hasResource")){
			hql += " and c.hasResource =:hasResource ";
			values.put("hasResource", condition.get("hasResource"));
		}
		if(condition.containsKey("courseName")){
			hql += " and lower(c.courseName) like:courseName ";
			values.put("courseName", "%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if(condition.containsKey("coursename")){
			hql += " and lower(c.courseName) = :coursename ";
			values.put("coursename", condition.get("coursename").toString().toLowerCase());
		}
		if(condition.containsKey("courseId")){
			hql += " and c.resourceid = :courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("teacherId")){//课程老师
			hql +=" and exists( from "+TeachingPlanCourseTimetable.class.getSimpleName() + " te where te.isDeleted = 0 and c.resourceid = te.course.resourceid and te.teacherId = :teacherId ) ";
			values.put("teacherId", condition.get("teacherId"));//老师
		}			
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql += " order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder();
		}
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}
	}

	@Override
	public void deleteCourse(String c_id) throws ServiceException {
		exGeneralHibernateDao.delete(Course.class, c_id);
	}
	
	@Override
	public void enableCourse(List<String> ids, boolean isEnabled) throws ServiceException {
		Course course = null;
		if (null != ids && ids.size() >0) {
			for(String id :ids){				
				course = get(id);				
				if(isEnabled){
					course.setStatus(1L);
					course.setStopTime(null);
				}else{
					course.setStatus(2L);
					course.setStopTime(new Date());//停用时间
				}
				update(course);
			}			
		}		
	}

	@Override
	@Transactional(readOnly=true)
	public boolean isExistsCourseCode(String courseCode) throws ServiceException {
		Course course = findUnique(" from "+ Course.class.getSimpleName()+" where isDeleted=0 and courseCode=? ", courseCode);
		return (course != null ? true : false);
	}


	@Override
	public Page findCourseForStudentOutPlanCourse(Map<String, Object> condition, Page objPage)throws ServiceException {
		
		condition.put("practice", "N");
		condition.put("status", 1L);
		condition.put("isDeleted",0);
		
		StringBuffer courseHQL = new StringBuffer("from "+Course.class.getSimpleName()+" c where c.isDeleted=:isDeleted and c.status=:status ");
		courseHQL.append(" and  c.isPractice=:practice   ");
		courseHQL.append(" and  c.planoutCreditHour is not null   ");
		if (condition.containsKey("coursesIds")&&ExStringUtils.isNotEmpty(condition.get("coursesIds").toString())&&condition.get("coursesIds").toString().length()>1) {
			courseHQL.append(" and c.resourceid not in( "+condition.get("coursesIds").toString()+")");
		}
		if (condition.containsKey("courseName")) {
			courseHQL.append(" and  c.courseName like '%"+condition.get("courseName")+"%' ");
		}
		if (condition.containsKey("creditHour")) {
			courseHQL.append(" and  c.planoutCreditHour >="+condition.get("creditHour"));
		}
		if (condition.containsKey("examType")) {
			courseHQL.append(" and  c.examType ="+Long.parseLong(condition.get("examType").toString()));
		}else {
			courseHQL.append(" and ( c.examType = '0' or c.examType = '1' or c.examType = '2' or c.examType is null )");
		}
		
		courseHQL.append("  and c.courseName not like '毕业%' and c.courseName not like '%设计'") ;
		
		courseHQL.append(" order by c.planoutCreditHour DESC ");
		
		return exGeneralHibernateDao.findByHql(objPage, courseHQL.toString(),condition);
	}
	
	@Override
	public String constructOptions(Map<String, Object> condition, String ...settingList) {
		StringBuffer courseOption = new StringBuffer();
		Map<String, Object> values = new HashMap<String, Object>();
		String hql = " from "+Course.class.getSimpleName()+" where isDeleted=:isDeleted";
		values.put("isDeleted", 0);
		if(condition.containsKey("status")){
			hql += " and status=:status";
			values.put("status", condition.get("status"));
		}
		if(condition.containsKey("courseType")){
			hql += " and courseType=:courseType";
			values.put("courseType", condition.get("courseType"));
		}
		if(condition.containsKey("isUniteExam")){
			hql += " and isUniteExam=:isUniteExam";
			values.put("isUniteExam", condition.get("isUniteExam"));
		}
		if(condition.containsKey("isDegreeUnitExam")){
			hql += " and isDegreeUnitExam=:isDegreeUnitExam";
			values.put("isDegreeUnitExam", condition.get("isDegreeUnitExam"));
		}
		if(condition.containsKey("isPractice")){
			hql += " and isPractice=:isPractice";
			values.put("isPractice", condition.get("isPractice"));
		}
		if(condition.containsKey("examType")){
			hql += " and examType=:examType";
			values.put("examType", condition.get("examType"));
		}
		List<Course> courseList = findByHql(hql, values);
		for (Course course : courseList) {
			courseOption.append("<option");
			if(settingList!=null && settingList.length>0){
				for (String resid : settingList) {
					if(course.getResourceid().equals(resid)){
						courseOption.append(" selected='selected'");
					}
				}
			}
			courseOption.append(" value='"+course.getResourceid()+"'>"+course.getCourseCode()+" - "+course.getCourseName()+"</option>");
		}
		return courseOption.toString();
	}
	
}
