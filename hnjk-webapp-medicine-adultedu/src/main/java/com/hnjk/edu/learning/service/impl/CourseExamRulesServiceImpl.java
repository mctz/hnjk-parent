package com.hnjk.edu.learning.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.edu.learning.service.ICourseExamRulesService;
/**
 * 试卷成卷规则服务接口实现
 * <code>CourseExamRulesServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-26 下午04:40:25
 * @see 
 * @version 1.0
 */
@Transactional
@Service("courseExamRulesService")
public class CourseExamRulesServiceImpl extends BaseServiceImpl<CourseExamRules> implements ICourseExamRulesService {
	
	@Override
	@Transactional(readOnly=true)
	public Page findCourseExamRulesByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+CourseExamRules.class.getSimpleName()+" r where 1=1 ";
		hql += " and r.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("courseName")){//入学考试课程
			hql += " and r.courseName=:courseName ";
			values.put("courseName", condition.get("courseName"));
		}
		if(condition.containsKey("courseId")){//关联课程
			hql += " and r.course.resourceid =:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("isEnrolExam")){//是否入学考试
			hql += " and r.isEnrolExam =:isEnrolExam ";
			values.put("isEnrolExam", condition.get("isEnrolExam"));
		}
		if(condition.containsKey("paperSourse")){
			if(!"entrance_exam".equals(condition.get("paperSourse"))){
				hql += " and r.paperSourse like :paperSourse ";
				values.put("paperSourse", "%"+condition.get("paperSourse")+"%");
			}			
		}
		//if(condition.containsKey("teacherId")){//课程老师
		//	hql += " and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=r.course.resourceid and t.teacherId =:teacherId ) ";
		//	values.put("teacherId", condition.get("teacherId"));//老师
		//}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return findByHql(objPage, hql, values);
	}
	
	@Override
	public Integer getNextVersionNum(String isEnrolExam, String courseName, String courseId) throws ServiceException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select coalesce(max(versionNum),0)+1 from "+CourseExamRules.class.getSimpleName()+" where isDeleted=0 and isEnrolExam=:isEnrolExam ");
		parameters.put("isEnrolExam", isEnrolExam);		
		if(Constants.BOOLEAN_YES.equals(isEnrolExam)){//入学考
			hql.append(" and courseName=:courseName ");
			parameters.put("courseName", courseName);
		} else {
			hql.append(" and course.resourceid=:courseId ");
			parameters.put("courseId", courseId);
		}
		return exGeneralHibernateDao.findUnique(hql.toString(), parameters);
	}
	
	@Override
	public Long getAvailableCourseExamsCount(CourseExamRules courseExamRules,String examNodeType,String examType) throws ServiceException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("select count(resourceid) from "+CourseExam.class.getSimpleName()+" where isDeleted=0 and isEnrolExam=:isEnrolExam ");
		parameters.put("isEnrolExam", courseExamRules.getIsEnrolExam());	
		if(ExStringUtils.isNotEmpty(examNodeType)){
			hql.append(" and examNodeType=:examNodeType ");
			parameters.put("examNodeType", examNodeType);
		}
		if(ExStringUtils.isNotEmpty(examType)){
			hql.append(" and examType=:examType ");
			parameters.put("examType", examType);
		}	
		if(ExStringUtils.isNotBlank(courseExamRules.getPaperSourse())){
			if(courseExamRules.getPaperSourse().contains("unit_exam")){
				hql.append(" and (examform in (:examforms) or examform is null) ");
			} else {
				hql.append(" and examform in (:examforms) ");
			}
			parameters.put("examforms", Arrays.asList(courseExamRules.getPaperSourse().split("\\,")));
		}
		if(Constants.BOOLEAN_YES.equals(courseExamRules.getIsEnrolExam())){//入学考
			hql.append(" and courseName=:courseName ");
			parameters.put("courseName", courseExamRules.getCourseName());
		} else {
			hql.append(" and course.resourceid=:courseId ");
			parameters.put("courseId", courseExamRules.getCourse().getResourceid());
		}
		return exGeneralHibernateDao.findUnique(hql.toString(), parameters);
	}
}
