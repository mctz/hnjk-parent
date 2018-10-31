package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageSender;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
/**
 * 通知管理
 * <code>ISysMsgService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-13 上午11:59:28
 * @see 
 * @version 1.0
 */
public interface ISysMsgService extends IBaseService<Message>{
	/**
	 * 保存消息
	 * @param messageSender
	 * @param messageReceiver
	 * @param attachIds
	 * @throws ServiceException
	 */
	void saveMessage_old(MessageSender messageSender,MessageReceiver messageReceiver,String[] attachIds) throws ServiceException;
	/**
	 * 更新消息
	 * @param messageSender
	 * @param messageReceiver
	 * @param attachIds
	 * @throws ServiceException
	 */
	void updateMessage_old(MessageSender messageSender,MessageReceiver messageReceiver,String[] attachIds) throws ServiceException;
	/**
	 * 删除消息
	 * @param type
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteMessage_old(String type,String[] ids) throws ServiceException;
	/**
	 * 当前用户未读消息数目
	 * @return
	 * @throws ServiceException
	 */
	Integer unreadMessageCount(User user) throws ServiceException;
	/**
	 * 发送消息 
	 * @param receivers     接收者     : 按组织(组织编码)/按角色(角色编码)/按用户(用户名)
	 * @param receiveType   接收类型     按组织/ 按角色/按用户
	 * @param sendUser      发送者
	 * @param msgTitle      消息标题
	 * @param msgContent    消息 key是接收者，value是消息内容
	 * @param msgType       消息类型:  tips - 温馨提示 / sysmsg - 系统消息/ usermsg - 用户消息
	 * @param isdraft       是否草稿   Y/N
	 * @param isReply       消息是否允许回复 Y/N
	 * @param formType      表单类型
	 * @param formId        表单ID
	 * @throws ServiceException
	 */
	void sendMsgByIds(List<String> receivers,String receiveType,User sendUser,Map<String, String> msgTitle,Map<String, String> msgContent,String msgType,String isdraft ,String isReply,String formType,String formId)throws ServiceException;
	
	/**
	 *  发送消息
	 * @param sendUserId        消息发送者用户ID
	 * @param sendUserCnName    消息发送者用户中文名
	 * @param sendUsersOrgUnit  消息发送者所属的组织单位
	 * @param msgType   		消息类型
	 * @param title     		消息标题
	 * @param msg       		消息内容
	 * @param receiver          消息接收者userId/roleCode/orgCode
	 * @param receiveType       消息的接收类型:按用户、组织...
	 * @param formId  			消息发送者所属的formId
	 * @param formType  		消息发送者所属的formType
	 */
	void sendMsg(String sendUserId,String sendUserCnName,OrgUnit sendUsersOrgUnit,String msgType,String title,
				 String msg,String receiverUserName,String receiveType,String formId,String formType)throws ServiceException;
	
	/**
	 * 分页查询消息
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findMessageByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	
	/**
	 * 更新消息
	 * @param message
	 * @param messageReceiver
	 * @throws ServiceException
	 */
	void updateMessage(Message message,MessageReceiver messageReceiver,String[] attachIds) throws ServiceException;
	
	/**
	 * 保存消息
	 * @param message
	 * @param messageReceiver
	 * @throws ServiceException
	 */
	void saveMessage(Message message,MessageReceiver messageReceiver,String[] attachIds ) throws ServiceException;
	
	/**
	 * 删除消息
	 * @param type
	 * @param ids
	 * @param user
	 * @throws ServiceException
	 */
	void deleteMessage(String type,String[] ids,User user) throws ServiceException;
	
	/**
	 *  撤销消息
	 * @param ids
	 * @throws ServiceException
	 */
	void revokeMessage(String[] ids)throws ServiceException;
	/**
	 * 保存消息及消息接收人
	 * @param msg
	 * @param receiver
	 */
	void saveMsgAndMsgReceiver(Message msg, MessageReceiver receiver);
}
