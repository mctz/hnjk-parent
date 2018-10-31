package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.Exercise;

/**
 * 课后作业管理服务接口
 * <code>IExerciseService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午05:09:09
 * @see 
 * @version 1.0
 */
public interface IExerciseService extends IBaseService<Exercise> {
	/**
	 * 课后作业分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findExerciseByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 删除作业
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteExercise(String[] ids) throws ServiceException;
	/**
	 * 获取作业习题下一个排序号
	 * @param exerciseBatchId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextShowOrder(String exerciseBatchId) throws ServiceException;
	/**
	 * 查询作业列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Exercise> findExerciseByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 习题是否已经存在
	 * @param batchId
	 * @param courseExamId
	 * @return
	 * @throws ServiceException
	 */
	boolean isExsitExercise(String batchId,String courseExamId) throws ServiceException;
}
