package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseReference;
/**
 * 课程参考资料服务接口
 * <code>ICourseReferenceService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-19 上午10:18:52
 * @see 
 * @version 1.0
 */
public interface ICourseReferenceService extends IBaseService<CourseReference> {
	/**
	 * 分页查询课程参考资料
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseReferenceByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 根据条件查询列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<CourseReference> findCourseReferenceByCondition(Map<String, Object> condition) throws ServiceException;
}
