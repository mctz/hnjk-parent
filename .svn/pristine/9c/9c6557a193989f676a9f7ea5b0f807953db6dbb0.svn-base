package com.hnjk.edu.portal.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.MessageSender;
/**
 * 消息发送者明细管理服务接口
 * <code>IMessageSenderService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-16 下午03:46:22
 * @see 
 * @version 1.0
 */
public interface IMessageSenderService extends IBaseService<MessageSender> {
	/**
	 * 分页查询消息
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findMessageByCondition(Map<String,Object> condition,Page page) throws ServiceException;
}
