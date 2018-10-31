package com.hnjk.edu.learning.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentSelfExercise;
/**
 * 学生自荐作业服务接口.
 * <code>IStudentSelfExerciseService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-12-14 下午04:09:37
 * @see 
 * @version 1.0
 */
public interface IStudentSelfExerciseService extends IBaseService<StudentSelfExercise> {
	/**
	 * 保存学生自荐作业
	 * @param exercise
	 * @param attachids
	 * @throws ServiceException
	 */
	void saveOrUpdateStudentSelfExercise(StudentSelfExercise exercise, String[] attachids) throws ServiceException;
	/**
	 * 分页查询学生自荐作业
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findStudentSelfExerciseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 审核发布/取消发布
	 * @param ids
	 * @param isPublished
	 * @throws ServiceException
	 */
	void publishStudentSelfExercise(String[] ids, String isPublished) throws ServiceException;
}
