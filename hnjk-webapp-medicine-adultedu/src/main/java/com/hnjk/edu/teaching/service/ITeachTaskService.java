package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachTask;

/**
 * 教学计划 - 教学任务书.
 * <code>ITeachTaskService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-8 下午03:42:06
 * @see 
 * @version 1.0
 */
public interface ITeachTaskService extends IBaseService<TeachTask>{

	Page findTeachTaskByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 发送任务书
	 * @param type 0-生成/1-发送给老师/2-老师发回/3-审核发布
	 * @param ids
	 */
	void batchSend(int type ,String[] ids)throws ServiceException;
	
	void deleteDetail(String c_id)throws ServiceException;
	
	void batchCascadeDelete(String[] split)throws ServiceException;
	/**
	 * 是否课程的老师
	 * @param courseId
	 * @param teacherId
	 * @param type 0-不区分 1-负责老师 2-主讲老师 3-辅导老师
	 * @return
	 * @throws ServiceException
	 */
	boolean isCourseTeacher(String courseId,String teacherId,int type) throws ServiceException;
	/**
	 * 查找课程最近一份任务书
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	TeachTask findLastTeachTask(String courseId) throws ServiceException;
	
	/**
	 * 查找老师最近一份任务书
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	TeachTask findLastTeachTaskByTeacherId(String teacherId) throws ServiceException;
	/**
	 * 查找改学期的教学任务书模板
	 * @param yearInfoId
	 * @param term
	 * @return
	 * @throws ServiceException
	 */
	TeachTask findTeachTaskTemplate(String yearInfoId,String term) throws ServiceException;
	/**
	 * 根据条件查询列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachTask> findTeachTaskByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 学生课程对应的任务书
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachTask> findStudentTeacherByCondition(Map<String, Object> condition) throws ServiceException;
}
