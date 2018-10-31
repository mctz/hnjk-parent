package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.security.model.User;
/**
 * 消息接受者明细管理服务接口
 * <code>IMessageReceiverService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-16 下午03:52:08
 * @see 
 * @version 1.0
 */
public interface IMessageReceiverService extends IBaseService<MessageReceiver> {
	/**
	 * 分页查询消息
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findMessageByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	
	/**
	 * 获取用户消息列表.
	 * @param condition userName , unitCode,roleCode,fetchSize,userId
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> getUserMessageList(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据消息接收人信息获取所有接收人
	 * 
	 * @param messageReceiver
	 * @return
	 * @throws ServiceException
	 */
	List<User> findByMessageReceiver(MessageReceiver messageReceiver) throws ServiceException;
}
