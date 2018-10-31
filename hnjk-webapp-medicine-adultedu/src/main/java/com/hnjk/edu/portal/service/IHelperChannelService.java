package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.Channel;
import com.hnjk.edu.portal.model.HelpChannel;

public interface IHelperChannelService extends IBaseService<HelpChannel> {
	/**
	 * 条件查询帮助栏目
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findHelpChannelByCondition(Map<String,Object> condition,Page page) throws ServiceException;	
	
	
	/**
	 * 返回栏目的树形列表，包含父子关系
	 * @return
	 * @throws ServiceException
	 */
	List<HelpChannel> findHelpChannelByParentAndchild() throws ServiceException;
	
	List<HelpChannel> findHelpChannelByParentAndchild(Map<String, Object> condition) throws ServiceException;
	
	List<HelpChannel> findHelpChannelTree(String parentCode) throws Exception;
}
