package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentExercise;

/**
 * 学生作业回答情况服务接口
 * <code>IStudentExerciseService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-31 上午10:07:45
 * @see 
 * @version 1.0
 */
public interface IStudentExerciseService extends IBaseService<StudentExercise> {
	/**
	 * 分页查找学生作业回答情况
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findStudentExerciseByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 学生是否完成作业
	 * @param exerciseBatchId
	 * @param studentInfoId
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistStudentExercise(String exerciseBatchId, String studentInfoId) throws ServiceException;
	/**
	 * 保存离线作用
	 */
	void saveOrUpdate(StudentExercise studentExercise,String[] uploadfileids);
	/**
	 * 根据条件查找列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<StudentExercise> findStudentExerciseByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 已提交学生人数
	 * @param exerciseBacthId
	 * @return
	 * @throws ServiceException
	 */
	Integer countStudentFinished(String exerciseBatchId)throws ServiceException;
	/**
	 * 作业平均分
	 * @param courseId
	 * @param studentInfoId
	 * @return
	 * @throws ServiceException
	 */
	Double avgStudentExerciseResult(String courseId,String studentInfoId,String yearId,String term) throws ServiceException;
	/**
	 * 已提交学生人数及平均分
	 * @param exerciseBatchId
	 * @return
	 * @throws ServiceException
	 */
	Map avgStudentFinished(String exerciseBatchId)throws ServiceException;
	
	void batchSaveOrUpdateStudentExercise(List<StudentExercise> studentExercises) throws ServiceException;
	/**
	 * 撤销作业提交
	 * @param exerciseBatchId
	 * @param studentIds
	 * @throws ServiceException
	 */
	void cancelStudentExercise(String exerciseBatchId, String[] studentIds) throws ServiceException;
}
