package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseLearningGuid;

/**
 * 课程学习指南服务接口
 * <code>ICourseLearningGuidService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 下午01:25:32
 * @see 
 * @version 1.0
 */
public interface ICourseLearningGuidService extends IBaseService<CourseLearningGuid> {
	/**
	 * 分页查询课程学习指南
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseLearningGuidByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 根据条件查找列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<CourseLearningGuid> findCourseLearningGuidByCondition(Map<String, Object> condition) throws ServiceException;
}
