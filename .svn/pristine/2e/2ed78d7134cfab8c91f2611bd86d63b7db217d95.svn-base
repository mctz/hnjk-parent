package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.StudentMakeupList;

/**
 * 
 * <code>Service接口</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2014-5-14 下午01:37:50
 * @see 
 * @version 1.0
 */
public interface IStudentMakeupListService extends IBaseService<StudentMakeupList> {

	public StudentMakeupList findStudentMakeupListByStuIdAndCourseId(String stuId,String courseId)
			throws ServiceException;
	
	/**
	 * 根据传入的ID使用IN语句查询StudentMakeupList列表
	 * @param ids couesid
	 * @return
	 * @throws ServiceException
	 */
//	public List<StudentMakeupList> findExamResultListByIds(String ids)throws ServiceException;
	
	/**
	 * 根据学籍ID和课程ID并按考试批次类型的升序排列获取补考名单
	 * 
	 * @param studentId
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> findByStudentIdAndCourseId(String studentId,String courseId) 
			throws ServiceException;
	
	/**
	 * 删除某个学生某门课程的补考名单
	 * @param studentInfoId
	 * @param courseId
	 * @param planCourseId
	 */
	public void deleteByStuIdAndCourseId(String studentInfoId, String courseId, String planCourseId);
	
	/**
	 * 获取某个学生某门课程某个批次的补考名单
	 * @param studentInfoId
	 * @param planCourseId
	 * @param examSubId
	 * @return
	 */
	public StudentMakeupList getByCondition(String studentInfoId, String planCourseId,String examSubId);

	public List<Map<String, Object>> findCourseByCondition(Map<String, Object> condition);
}
