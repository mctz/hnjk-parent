package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.model.ExerciseBatch;

/**
 * 课后作业批次管理服务接口
 * <code>IExerciseBatchService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 上午11:28:50
 * @see 
 * @version 1.0
 */
public interface IExerciseBatchService extends IBaseService<ExerciseBatch> {
	/**
	 * 课后作业批次分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findExerciseBatchByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	List<ExerciseBatch> findExerciseBatchByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 删除作业批次
	 * @param ids
	 * @throws ServiceException
	 */
	public void deleteExerciseBatch(String[] ids) throws ServiceException;
	/**
	 * 保存更新作业批次
	 * @param exerciseBatch
	 * @param attachIds
	 * @throws ServiceException
	 */
	void saveOrUpdateExerciseBatch(ExerciseBatch exerciseBatch,String[] attachIds) throws ServiceException;
	
	List<ExerciseBatch> statusExerciseBatch(String[] ids,Integer status) throws ServiceException;
	/**
	 * 删除作业题目
	 * @param colsId
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteExercise(String colsId,String[] ids) throws ServiceException;
	/**
	 * 保存主观题
	 * @param exerciseBatch
	 * @param exercise
	 * @param attachIds
	 * @throws ServiceException
	 */
	void saveSubjectiveExercise(ExerciseBatch exerciseBatch,Exercise exercise,String[] attachIds) throws ServiceException;
	
	/**
	 * 查找需要批改的业(已提交人数大于已批改人数)
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> findNeedsCorrectExercise(Map<String,Object> param)throws Exception;
	/**
	 * 统计作业提交和批改状态人数
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> findExerciseStatusCount(Map<String,Object> param)throws ServiceException;
	
	
	public Page findonlineClasses(Map<String, Object> condition,Page objPage) throws ServiceException;
	
}
