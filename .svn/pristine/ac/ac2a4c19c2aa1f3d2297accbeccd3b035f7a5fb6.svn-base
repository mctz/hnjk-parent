package com.hnjk.edu.basedata.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Grade;

/**
 * <code>IGradeService</code>基础数据-年级-服务接口.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午01:57:13
 * @see 
 * @version 1.0
 */
public interface IGradeService extends IBaseService<Grade>{
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findGradeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;

	/**
	 * 获取默认年级
	 * @return
	 * @throws ServiceException
	 */
	Grade getDefaultGrade() throws ServiceException;
	
	/**
	 * 根据年级ID集合获取名称集合
	 * 
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	String findNamesByIds(String ids) throws ServiceException;
}
