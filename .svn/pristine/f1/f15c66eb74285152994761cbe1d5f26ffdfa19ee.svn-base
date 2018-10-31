package com.hnjk.edu.basedata.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Classic;

/**
 *  <code>IClassic</code>基础数据-层次类别-服务接口.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午01:58:12
 * @see 
 * @version 1.0
 */
public interface IClassicService extends IBaseService<Classic>{
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findClassicByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;

	/**
	 * 获取班级人数
	 * @param resourceid
	 * @return
	 * @throws Exception 
	 */
	long getStudentNumbers(String resourceid) throws Exception;

}
