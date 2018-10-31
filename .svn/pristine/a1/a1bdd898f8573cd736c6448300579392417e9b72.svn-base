package com.hnjk.edu.learning.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseMockTest;
/**
 * 课程模拟试题管理服务接口
 * <code>ICourseMockTestService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-9 下午04:52:30
 * @see 
 * @version 1.0
 */
public interface ICourseMockTestService extends IBaseService<CourseMockTest> {
	/**
	 * 分页查询课程模拟试题
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseMockTestByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
}
