package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Course;


/**
 * 课程库管理服务接口.
 * <code>ICourseService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-6 下午05:08:15
 * @see 
 * @version 1.0
 */
public interface ICourseService extends IBaseService<Course>{
	/**
	 * 分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	
	Page findCourseByHql(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	void batchCascadeDelete(String[] ids) throws ServiceException;
	
	void deleteCourse(String c_id) throws ServiceException;
	
	/**
	 * 设置课程状态
	 * @param ids
	 * @param isEnabled
	 * @throws ServiceException
	 */
	void enableCourse(List<String> ids,boolean isEnabled) throws ServiceException;
	/**
	 * 课程编码是否存在
	 * @param courseCode
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsCourseCode(String courseCode) throws ServiceException;
	
	Page findCourseForStudentOutPlanCourse(Map<String, Object> condition,Page objPage) throws ServiceException;

	/**
	 * 课程选择框-供 select 标签使用
	 * @param condition
	 * @param settingList
	 * @return
	 */
	String constructOptions(Map<String, Object> condition, String[] settingList);
}
