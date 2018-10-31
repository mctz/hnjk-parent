package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.TeacherCourseFiles;

/**
 * 教师课程文件服务接口.
 * @author hzg
 *
 */
public interface ITeacherCourseFilesService extends IBaseService<TeacherCourseFiles>{

	/**
	 * 根据课程ID查找课程文件树.
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	List<TeacherCourseFiles> findTeacherFilesTree(String courseId) throws ServiceException;
	List<TeacherCourseFiles> findTeacherFilesTreeByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 分页查找.
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findTeacherFilesByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	List<TeacherCourseFiles> findTeacherFilesByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 保存
	 * @param teacherFiles
	 * @param uploadfileid
	 * @throws ServiceException
	 */
	void saveOrUpdateTeacherFiles(TeacherCourseFiles teacherFiles,String[] uploadfileid) throws ServiceException;
	/**
	 * 移动文件
	 * @param ids
	 * @param fromId
	 * @param moveToId
	 * @throws ServiceException
	 */
	void moveTeacherFiles(String[] ids, String moveToId) throws ServiceException;
	/**
	 * 发布/取消发布
	 * @param ids
	 * @param isPublished
	 * @throws ServiceException
	 */
	void publishTeacherFiles(String[] ids, String isPublished) throws ServiceException;
	
}
