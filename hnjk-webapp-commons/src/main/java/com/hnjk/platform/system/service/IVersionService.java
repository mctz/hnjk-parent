package com.hnjk.platform.system.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.Version;

/**
 * IVersionService
 * @author zik, 广东学苑教育发展有限公司
 *
 */
public interface IVersionService extends IBaseService<Version> {
	/**
	 * 获取最新的Android版本
	 * @return
	 */
	Version getLastVersion();
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	
	/**
	 * 查询版本列表--不分页
	 * @param condition
	 * @return
	 */
	@Override
	List<Version> findByCondition(Map<String, Object> condition) throws ServiceException;
}
