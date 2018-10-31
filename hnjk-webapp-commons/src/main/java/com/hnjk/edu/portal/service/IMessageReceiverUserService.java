package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.MessageReceiverUser;
/**
 * 消息接受者明细管理服务接口
 * <code>IMessageReceiverService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-16 下午03:52:08
 * @see 
 * @version 1.0
 */
public interface IMessageReceiverUserService extends IBaseService<MessageReceiverUser> {
	
	/**
	 * 分页查询消息
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	
	
	/**
	 * 根据条件获取用户接收消息列表
	 * 
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	List<MessageReceiverUser> findByCondition(Map<String,Object> condition) throws ServiceException;
}
