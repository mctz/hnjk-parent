package com.hnjk.edu.arrange.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.ArrangeTemplate;

public interface IArrangeTemplateService extends IBaseService<ArrangeTemplate>{
	
	/**
	 * 通过查询条件返回page
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findArrangeTemplateByHql(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 根据查询条件返回list
	 * @param map
	 * @return
	 * @throws ServiceException
	 */
	List<ArrangeTemplate> findTemplateListByCondition(Map<String, Object> map) throws ServiceException;

	/**
	 * 根据条件构造成select标签中的option(只供select标签用)
	 * @param condition
	 * @param classroomid
	 * @return
	 */
	String constructOptions(Map<String, Object> condition, String defaultValue) throws ServiceException;

}
