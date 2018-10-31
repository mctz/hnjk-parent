package com.hnjk.edu.learning.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseOverview;

/**
 * 课程概况服务接口
 * <code>ICourseOverviewService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 上午11:25:14
 * @see 
 * @version 1.0
 */
public interface ICourseOverviewService extends IBaseService<CourseOverview> {
	/**
	 * 分页查询课程概况
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findCourseOverviewByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 一类只能一项
	 * @param type
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsType(String type,String courseId) throws ServiceException;	
}
