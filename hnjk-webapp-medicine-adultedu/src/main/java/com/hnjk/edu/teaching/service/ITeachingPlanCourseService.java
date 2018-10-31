package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;

/**
 * 
 * <code>教学计划课程Service</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 上午09:36:16
 * @see 
 * @version 1.0
 */
public interface ITeachingPlanCourseService  extends IBaseService<TeachingPlanCourse>{
	/**
	 * 教学计划课程列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findTeachingPlanCourseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 教学计划课程列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachingPlanCourse> findTeachingPlanCourse(Map<String, Object> condition) throws ServiceException;

	String constructOptions(Map<String,Object> condition, String teachingPlanCourseid) throws Exception;

	List<StudentExamResultsVo> printOutOrInExam(List<StudentExamResultsVo> list, List<String> tempList, StudentInfo stu, String degreeUnitExam);
}