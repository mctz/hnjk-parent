package com.hnjk.edu.basedata.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.NationMajor;
/**
 * 国家专业代码库管理服务接口.
 * <code>INationMajorService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-15 下午02:45:48
 * @see 
 * @version 1.0
 */
public interface INationMajorService extends IBaseService<NationMajor> {
	/**
	 * 分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findNationMajorByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 根据条件查询-返回LIST
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<NationMajor> findNationMajorByCondition(Map<String,Object > condition)throws ServiceException;
	/**
	 * 是否存在国家专业代码
	 * @param majorCode
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsNationMajorCode(String majorCode) throws ServiceException;
}
