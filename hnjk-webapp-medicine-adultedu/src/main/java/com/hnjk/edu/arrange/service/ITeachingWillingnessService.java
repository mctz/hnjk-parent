package com.hnjk.edu.arrange.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.TeachingWillingness;

public interface ITeachingWillingnessService extends IBaseService<TeachingWillingness> {

	/**
	 * 查询意愿申请表
	 * @param condition
	 * @return Page
	 */
	Page findWillingnessByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;

}
