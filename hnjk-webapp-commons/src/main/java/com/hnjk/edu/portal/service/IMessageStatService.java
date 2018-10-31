package com.hnjk.edu.portal.service;

import java.util.List;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.MessageStat;
/**
 * 消息阅读情况服务接口
 * <code>IMessageStatService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-22 下午02:56:34
 * @see 
 * @version 1.0
 */
public interface IMessageStatService extends IBaseService<MessageStat> {
	/**
	 * 用户是否已阅读
	 * @param readUserId
	 * @return
	 * @throws ServiceException
	 */
	boolean isMessageReaded(String msgId,String readUserId) throws ServiceException;
	
	/**
	 * 獲取收件人的消息ID
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	List<String> getIsReadMsgIds(String userId) throws ServiceException;
}
