package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.security.model.User;


/**
 * 课程-教师-班级关联表 接口.
 * <code>ICourseStatusClService</code><p>

 */
public interface ICourseStatusClService extends IBaseService<CourseTeacherCl>{

	List<Map<String, Object>> findTeacherByCondition(Map<String, Object> param);
	/**
	 * 根据年份、学期查找任课老师表
	 * @param param-->firstYear(Long)  term(String)
	 * @return list<Map>
	 * @throws ServiceException
	 */
	List<Map<String, Object>> findCourseAndClasses(Map<String, Object> param)
			throws ServiceException;
	/**
	 * 根据条件查找任课老师表
	 * @param param
	 * @return list<Map>
	 * @throws ServiceException
	 * @throws Exception
	 */
	List<Map<String, Object>> findCourseTeacherCl(Map<String, Object> param)
			throws ServiceException, Exception;
	/**
	 * 根据条件查找任课老师表
	 * @param param
	 * @return list
	 * @throws ServiceException
	 */
	List<CourseTeacherCl> findCourseTeacherCl_new(Map<String, Object> param)
			throws ServiceException;
	
}
