package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.Channel;

/**
 * 门户栏目服务接口.
 * <code>IChannelService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-20 下午03:06:29
 * @see 
 * @version 1.0
 */
public interface IChannelService  extends IBaseService<Channel> {
	
	/**
	 * 根据条件查找栏目列表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findChannelByCondition(Map<String,Object> condition,Page page) throws ServiceException;	
	
	/**
	 * 返回栏目的树形列表，包含父子关系
	 * @return
	 * @throws ServiceException
	 */
	List<Channel> findChannelByParentAndchild() throws ServiceException;
	
	List<Channel> findChannelByParentAndchild(Map<String, Object> conddition) throws ServiceException;
	
	List<Channel> findChannelTree(String parentCode) throws Exception;
	/**
	 * 精品课程栏目
	 * @param conddition
	 * @return
	 * @throws Exception
	 */
	List<Channel> findCourseChannelByCondition(Map<String, Object> condition) throws ServiceException;
}
