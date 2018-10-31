package com.hnjk.platform.system.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.CustomFormDefine;

/**
 * 自定义表单业务接口.
 * @author hzg
 *
 */
public interface ICustomFormService extends IBaseService<CustomFormDefine> {

	/**
	 * 根据条件查找分页.
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findCustomerFormByCondition(Map<String, Object> condition,Page page) throws ServiceException;
	/**
	 * 批量删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
}
