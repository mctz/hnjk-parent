package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
/**
 * 教学计划课程排课服务接口.
 * <code>ITeachingPlanCourseTimetableService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-11 上午09:33:41
 * @see 
 * @version 1.0
 */
public interface ITeachingPlanCourseTimetableService extends IBaseService<TeachingPlanCourseTimetable> {

	List<TeachingPlanCourseTimetable> findTeachingPlanCourseTimetableByCondition(Map<String, Object> condition) throws ServiceException;
	void updateCourseTimetable(TeachingPlanCourseTimetable timetable)  throws ServiceException;
	Page findCourseTimetableHistoryByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	List<Map<String, Object>> findCourseTimetableHistoryByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 是否有权限
	 * @param userId
	 * @param courseId
	 * @param classesId
	 * @return
	 * @throws ServiceException
	 */
	boolean hasAuthority(String userId, String courseId, String classesId) throws ServiceException;
	
	/**
	 * 是否已经排课
	 * @param courseId
	 * @param term
	 * @return
	 */
	public boolean isArrangeCourse(String courseId, String term);
	
	/**
	 * 根据条件获取已经排课的课程ID列表
	 * @param condition
	 * @return
	 */
	public List<String> findCourseIdByCondition(Map<String, Object> condition);
	
	/**
	 * 获取某个老师授课或教某门课程的所有教学点
	 * @param teacherId
	 * @param courseId  这个可以为空
	 * @return
	 */
	public List<String> findSchoolIdByTeacherId(String teacherId, String courseId);
	
}
