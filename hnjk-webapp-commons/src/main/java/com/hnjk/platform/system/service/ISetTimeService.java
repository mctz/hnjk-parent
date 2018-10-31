package com.hnjk.platform.system.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.SetTime;

/**
 * 设置时间接口
 * @author Zik
 *
 */
public interface ISetTimeService extends IBaseService<SetTime>{

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findSetTimeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	
	/**
	 * 根据业务类型获取时间设置
	 * @param businessType
	 * @return
	 * @throws ServiceException
	 */
	SetTime getSetTimeByBusinessType(String businessType) throws ServiceException;
}
