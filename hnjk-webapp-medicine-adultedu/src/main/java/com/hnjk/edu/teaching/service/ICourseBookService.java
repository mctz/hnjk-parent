package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.CourseBook;

/**
 * 教学管理-教材管理服务接口.
 * <code>ICourseBookService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-7 下午02:46:16
 * @see 
 * @version 1.0
 */
public interface ICourseBookService extends IBaseService<CourseBook>{
	/**
	 * 分页查询教材
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseBookByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids);
	/**
	 * 删除教材
	 * @param id
	 */
	void deleteCourseBook(String id);
	
	/**
	 * 獲取出版社名稱
	 * @return
	 */
	List<String> getPublishCompanies();
}
