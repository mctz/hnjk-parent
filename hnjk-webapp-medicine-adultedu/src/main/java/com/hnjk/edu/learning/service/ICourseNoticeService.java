package com.hnjk.edu.learning.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseNotice;

/**
 * 课程公告服务接口
 * <code>ICourseNoticeService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 上午09:40:07
 * @see 
 * @version 1.0
 */
public interface ICourseNoticeService extends IBaseService<CourseNotice> {
	/**
	 * 分页查询课程公告
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findCourseNoticeByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 保存或更新课程公告
	 * @param courseNotice
	 * @param attachs
	 */
	void saveOrUpdateCourseNotice(CourseNotice courseNotice,String[] attachIds)throws ServiceException;
}
