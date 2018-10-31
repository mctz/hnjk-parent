package com.hnjk.edu.portal.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageReceiverUser;
import com.hnjk.edu.portal.model.MessageSender;
import com.hnjk.edu.portal.service.IMessageReceiverService;
import com.hnjk.edu.portal.service.IMessageReceiverUserService;
import com.hnjk.edu.portal.service.IMessageSenderService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * 通知管理
 * <code>SysMsgServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-13 上午11:59:32
 * @see 
 * @version 1.0
 */
@Transactional
@Service("sysMsgService")
public class SysMsgServiceImpl extends BaseServiceImpl<Message> implements ISysMsgService {

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("messageSenderService")
	private IMessageSenderService messageSenderService;
	
	@Autowired
	@Qualifier("messageReceiverService")
	private IMessageReceiverService messageReceiverService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("messageReceiverUserService")
	private IMessageReceiverUserService messageReceiverUserService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Override
	public void saveMessage_old(MessageSender messageSender, MessageReceiver messageReceiver, String[] attachIds) throws ServiceException {
		Message message = messageSender.getMessage();
		save(message);
		exGeneralHibernateDao.save(messageSender);	
		attachsService.updateAttachByFormId(attachIds, message.getResourceid(), Message.class.getSimpleName(), messageSender.getSenderId(), messageSender.getSender());
	
		//if(Constants.BOOLEAN_NO.equalsIgnoreCase(message.getIsDraft())){//非草稿
			exGeneralHibernateDao.save(messageReceiver);				
		//} 
		Set<MessageReceiverUser> messageReceiverUsers = messageReceiver.getMessageReceiverUsers();
		List<MessageReceiverUser> delMessageReceiverUsers = new ArrayList<MessageReceiverUser>();
		List<String> usernameList = new ArrayList<String>();
		String usernames = messageReceiver.getUserNames();
		if(ExStringUtils.isNotEmpty(usernames)){
			for(MessageReceiverUser mu:messageReceiverUsers){
				if(usernames.contains(mu.getUserName())){
					usernameList.add(mu.getUserName());
					continue;
				}else{
					delMessageReceiverUsers.add(mu);
				}
			}
			String[] unames = usernames.split(",");
			for(String uname:unames){
				if(usernameList.contains(uname)){
					continue;
				}else{
					MessageReceiverUser mru = new MessageReceiverUser();
					mru.setMessageReceiver(messageReceiver);
					mru.setUserName(uname);
					messageReceiverUserService.save(mru);
				}
			}
			if(delMessageReceiverUsers.size()>0){
				messageReceiverUserService.batchDelete(delMessageReceiverUsers);
			}
		}
	}

	@Override
	public void updateMessage_old(MessageSender messageSender, MessageReceiver messageReceiver, String[] attachIds) throws ServiceException {
		Message message = messageSender.getMessage();
		exGeneralHibernateDao.update(messageSender);
		attachsService.updateAttachByFormId(attachIds, message.getResourceid(), Message.class.getSimpleName(), messageSender.getSenderId(), messageSender.getSender());
		exGeneralHibernateDao.saveOrUpdate(messageReceiver);	
		
		Set<MessageReceiverUser> messageReceiverUsers = messageReceiver.getMessageReceiverUsers();
		List<MessageReceiverUser> delMessageReceiverUsers = new ArrayList<MessageReceiverUser>();
		List<String> usernameList = new ArrayList<String>();
		String usernames = messageReceiver.getUserNames();
		if (ExStringUtils.isNotEmpty(usernames)) {
			for (MessageReceiverUser mu : messageReceiverUsers) {
				if (usernames.contains(mu.getUserName())) {
					usernameList.add(mu.getUserName());
					continue;
				} else {
					delMessageReceiverUsers.add(mu);
				}
			}
			String[] unames = usernames.split(",");
			for (String uname : unames) {
				if (usernameList.contains(uname)) {
					continue;
				} else {
					MessageReceiverUser mru = new MessageReceiverUser();
					mru.setMessageReceiver(messageReceiver);
					mru.setUserName(uname);
					messageReceiverUserService.save(mru);
				}
			}
			if (delMessageReceiverUsers.size() > 0) {
				messageReceiverUserService.batchDelete(delMessageReceiverUsers);
			}
		}
	}

	@Override
	public void deleteMessage_old(String type, String[] ids) throws ServiceException {
		if("inbox".equalsIgnoreCase(type)){//收件箱
			if(ids!=null && ids.length>0){
				for (String id : ids) {
					MessageReceiver messageReceiver = messageReceiverService.findUniqueByProperty("message.resourceid", id);	
					messageReceiverService.delete(messageReceiver);
				}
			}
		} else if("draftbox".equalsIgnoreCase(type) || "sendbox".equalsIgnoreCase(type)){//草稿箱或发件箱
			if(ids!=null && ids.length>0){
				for (String id : ids) {
					MessageSender messageSender = messageSenderService.findUniqueByProperty("message.resourceid", id);	
					messageSenderService.delete(messageSender);
				}
			}
		} 
	}	
	
	@Override
	public Integer unreadMessageCount(User user) throws ServiceException {
		String hql = "select count(r) from MessageReceiver r where r.isDeleted = 0  and r.message.sendTime <= current_date() ";
		
		hql += " and ( 1=0 or r.receiveType = 'user' and r.userNames like '%"+user.getUsername()+"%' ";
		if(user.getOrgUnit()!=null) {
			hql += " or r.receiveType = 'org' and r.orgUnitCodes like '%"+user.getOrgUnit().getUnitCode()+"%' ";
		}
		hql += "or r.receiveType = 'role' and (1=0 ";
		for (Role r : user.getRoles()) {
			hql += " or r.roleCodes like '%"+r.getRoleCode()+"%' ";
		}
		hql += " ))";
		hql += " and r.message.resourceid not in (select s.message.resourceid from MessageStat s where s.isDeleted=0 and s.receiverId='"+user.getResourceid()+"' ) ";
		Long count = exGeneralHibernateDao.findUnique(hql);
		return count==null?0:count.intValue();
	}
	
	/**
	 *  发送消息
	 * @param sendUserId        消息发送者用户ID
	 * @param sendUserCnName    消息发送者用户中文名
	 * @param sendUsersOrgUnit  消息发送者所属的组织单位
	 * @param msgType   		消息类型
	 * @param title     		消息标题
	 * @param msg       		消息内容
	 * @param receiver          消息接收者 userId/roleCode/orgCode
	 * @param receiveType       消息的接收类型:按用户、组织...
	 * @param formId  			消息发送者所属的formId
	 * @param formType  		消息发送者所属的formType
	 */
	@Override
	public void sendMsg(String sendUserId, String sendUserCnName, OrgUnit sendUsersOrgUnit, String msgType, String title,
						String msg, String receiver, String receiveType, String formId, String formType){

		Message message 				= new Message();
//		MessageSender messageSender 	= new MessageSender();
		MessageReceiver messageReceiver = new MessageReceiver();
		
		message.setContent(msg);
		message.setMsgTitle(title);
		message.setMsgType(msgType);
		message.setSendTime(ExDateUtils.getCurrentDateTime());
		message.setFormId(formId);
		message.setFormType(formType);
		message.setIsDraft(Constants.BOOLEAN_NO);
		message.setSenderName(sendUserCnName);
		message.setSender(userService.get(sendUserId));
		message.setOrgUnit(sendUsersOrgUnit);
		/*messageSender.setMessage(message);
		messageSender.setOrgUnit(sendUsersOrgUnit);
		messageSender.setSender(sendUserCnName);
		messageSender.setSenderId(sendUserId);*/

		messageReceiver.setMessage(message);
		messageReceiver.setReceiveType(receiveType);
		
		if (Constants.msgReceiveType.role.toString().equals(receiveType)) {
			messageReceiver.setRoleCodes(receiver);
		} else if (Constants.msgReceiveType.org.toString().equals(receiveType)) {
			messageReceiver.setOrgUnitCodes(receiver);
		} else if (Constants.msgReceiveType.user.toString().equals(receiveType)) {
			messageReceiver.setUserNames(receiver);
		}
		
		this.saveMessage(message, messageReceiver, null);
		/*this.save(message);
		messageSenderService.save(messageSender);
		messageReceiverService.save(messageReceiver);	*/
		
	}
	
	/**
	 * 发送消息 
	 * @param receivers     接收者     : 按组织(组织编码)/按角色(角色编码)/按用户(用户名)
	 * @param receiveType   接收类型     按组织/ 按角色/按用户
	 * @param sendUser      发送者
	 * @param msgTitle      消息标题
	 * @param msgContent    消息 key是接收者，value是消息内容
	 * @param msgType       消息类型:  tips - 温馨提示 / sysmsg - 系统消息/ usermsg - 用户消息
	 * @param isdraft       是否草稿  Y/N
	 * @param isReply       消息是否允许回复  Y/N
	 * @param formType      表单类型
	 * @param formId        表单ID
	 * @throws ServiceException
	 */
	@Override
	public void sendMsgByIds(List<String> receivers,String receiveType,User sendUser,Map<String, String> msgTitle,Map<String, String> msgContent,String msgType,String isdraft ,String isReply,String formType,String formId)throws ServiceException {
		
		Date curTime                 = new Date();
		List<Object[]> msgParam      = new ArrayList<Object[]>();
		List<Object[]> senderParam   = new ArrayList<Object[]>();
		List<Object[]> receiverParam = new ArrayList<Object[]>();
		
		String msgSql      			 = "insert into edu_portal_msg (resourceid,msgtitle,msgtype,content,sendtime,isdraft,isreply,formid,formtype,version,isdeleted,senderid,sendername,orgunitid) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		String senderSql   			 = "insert into edu_portal_sender (resourceid,msgid,senderid,sender,orgunitid,version,isdeleted) values (?,?,?,?,?,?,?)";
		String receiverSql			 = "";
		if (Constants.msgReceiveType.org.toString().equals(receiveType)) {
			receiverSql              = "insert into edu_portal_receiver (resourceid,receivetype,msgid,orgunitcodes,version,isdeleted) values (?,?,?,?,?,?)";  
		}else if (Constants.msgReceiveType.role.toString().equals(receiveType)) {
			receiverSql              = "insert into edu_portal_receiver (resourceid,receivetype,msgid,rolecodes,version,isdeleted) values (?,?,?,?,?,?)";  
		}else if (Constants.msgReceiveType.user.toString().equals(receiveType)) {
			receiverSql              = "insert into edu_portal_receiver (resourceid,receivetype,msgid,usernames,version,isdeleted) values (?,?,?,?,?,?)";  
		}else {
			throw new ServiceException("请传入正确的消息接收者类型！");
		}
		
		GUIDUtils.init();
		for (String receive:receivers) {
			String msgid 			 = GUIDUtils.buildMd5GUID(false);
			//消息id,消息标题,消息类型,消息内容,发送时间,是否草稿,是否可回复,版本,是否删除				
			msgParam.add(new Object[]{msgid,msgTitle.get(receive),msgType,msgContent.get(receive),curTime,ExStringUtils.trimToEmpty(isdraft),
					ExStringUtils.trimToEmpty(isReply),ExStringUtils.trimToEmpty(formId),ExStringUtils.trimToEmpty(formType),0L,0,sendUser.getResourceid(),
					sendUser.getCnName(),sendUser.getOrgUnit().getResourceid()});
			//发送id,消息id,发送人id,发送人姓名,发送人组织单位,版本,是否删除
//			senderParam.add(new Object[]{GUIDUtils.buildMd5GUID(false),msgid,sendUser.getResourceid(),sendUser.getCnName(),sendUser.getOrgUnit().getResourceid(),0L,0});
			//接收id,接收类型,消息id,接受者登录名,版本,是否删除
			receiverParam.add(new Object[]{GUIDUtils.buildMd5GUID(false),receiveType,msgid,receive,0L,0});
		}
		
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(msgSql, msgParam);
//		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(senderSql,senderParam);
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(receiverSql, receiverParam);
		
		logger.info("消息发送成功,共{}条,{}",new Object[]{receivers.size(),curTime});
	}

	/**
	 * 分页查询消息
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findMessageByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Message.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
				
		if(condition.containsKey("msgTitle")){//标题
			hql += " and lower(msgTitle) like lower(:msgTitle) ";
			values.put("msgTitle", "%"+condition.get("msgTitle")+"%");
		}
		if(condition.containsKey("senderName")){//发送人姓名
			hql += " and senderName like :senderName ";
			values.put("senderName", "%"+condition.get("senderName")+"%");
		}
		if(condition.containsKey("msgType")){//类型
			hql += " and msgType =:msgType ";
			values.put("msgType", condition.get("msgType"));
		}
		if(condition.containsKey("formId")){//表单ID
			hql += " and formId =:formId ";
			values.put("formId", condition.get("formId"));
		}
		if(condition.containsKey("isDraft")){//是否草稿
			hql += " and isDraft =:isDraft ";
			values.put("isDraft", condition.get("isDraft"));
		}
		if(condition.containsKey("senderId")){//发送人
			hql += " and sender.resourceid =:senderId ";
			values.put("senderId", condition.get("senderId"));
		}	
		if(condition.containsKey("status")){//状态
			hql += " and status =:status ";
			values.put("status", condition.get("status"));
		}
		if(condition.containsKey("startDate")){//开始时间
			hql += " and sendTime >= to_date('"+condition.get("startDate").toString()+"','yyyy-MM-dd') ";
		}
		if(condition.containsKey("endDate")){//结束时间
			hql += " and sendTime <= to_date('"+condition.get("endDate").toString()+"','yyyy-MM-dd')+1 ";
		}
		if(ExStringUtils.isNotEmpty(page.getOrderBy())) {
			hql += " order by "+page.getOrderBy() +" "+ page.getOrder();
		}
		return exGeneralHibernateDao.findByHql(page, hql, values);
	}

	/**
	 * 更新消息
	 * @param message
	 * @param messageReceiver
	 * @throws ServiceException
	 */
	@Override
	public void updateMessage(Message message, MessageReceiver messageReceiver,String[] attachIds) throws ServiceException {
		// 保存消息
//		saveOrUpdate(message);
		if(attachIds != null && attachIds.length > 0){
			message.setHasAttachs(Constants.BOOLEAN_YES);
		}
		// 保存接收人
		exGeneralHibernateDao.saveOrUpdate(messageReceiver);	
		attachsService.updateAttachByFormId(attachIds, message.getResourceid(), Message.class.getSimpleName(), message.getSender().getResourceid(), message.getSenderName());
		
		String msgStatus = "unSend";
		if(Constants.BOOLEAN_NO.equals(message.getIsDraft())){
			msgStatus = "sended";
		}
		// 只有草稿才能编辑
		if(ExStringUtils.isNotEmpty( messageReceiver.getReceiveType())  && "user".equals( messageReceiver.getReceiveType())) {
			Set<MessageReceiverUser> messageReceiverUsers = messageReceiver.getMessageReceiverUsers();
			// 要删除的用户
			List<String> delMessageReceiverUsers = new ArrayList<String>();
			// 已有的用户
			List<String> usernameList = new ArrayList<String>();
			List<MessageReceiverUser> saveList = new ArrayList<MessageReceiverUser>();
			// 编辑后要发送的用户
			String usernames = messageReceiver.getUserNames();
			if (ExStringUtils.isNotEmpty(usernames)) {
				for (MessageReceiverUser mu : messageReceiverUsers) {
					if (usernames.contains(mu.getUserName())) {
						usernameList.add(mu.getUserName());
						if(Constants.BOOLEAN_NO.equals(message.getIsDraft())){
							mu.setMsgStatus(msgStatus);
							saveList.add(mu);
						}
					} else {
						delMessageReceiverUsers.add(mu.getResourceid());
					}
				}
				String[] unames = usernames.split(",");
				for (String uname : unames) {
					// 新增要发送的用户
					if (!usernameList.contains(uname)) {
						User _user = userService.findUnique("from "+User.class.getSimpleName()+" u where u.isDeleted=0 and u.username=? ", uname);
						if(_user != null){
							MessageReceiverUser mru = new MessageReceiverUser();
							mru.setMessageReceiver(messageReceiver);
							mru.setReceiver(_user);
							mru.setUserCNName(_user.getCnName());
							mru.setUserName(uname);
							mru.setMsgStatus(msgStatus);
							saveList.add(mru);
						}
					}
				}
				if(ExCollectionUtils.isNotEmpty(saveList)){
					messageReceiverUserService.batchSaveOrUpdate(saveList);
				}
				// 批量物理删除
				if (delMessageReceiverUsers.size() > 0) {
					messageReceiverUserService.batchTruncate(MessageReceiverUser.class, delMessageReceiverUsers.toArray(new String[0]));
				}
			}
		} else if (ExStringUtils.isNotEmpty(message.getIsDraft())  && Constants.BOOLEAN_NO.equals(message.getIsDraft())){// 从草稿箱中发送
			List<MessageReceiverUser> _saveList = new ArrayList<MessageReceiverUser>();
			createReceiverUser(messageReceiver, _saveList, msgStatus);
			if(ExCollectionUtils.isNotEmpty(_saveList)){
				messageReceiverUserService.batchSaveOrUpdate(_saveList);
			}
		}
	}

	/**
	 * 保存消息
	 * @param message
	 * @param messageReceiver
	 * @throws ServiceException
	 */
	@Override
	public void saveMessage(Message message, MessageReceiver messageReceiver,String[] attachIds) throws ServiceException {
		try {
			// 保存消息
			if(attachIds != null && attachIds.length > 0){
				message.setHasAttachs(Constants.BOOLEAN_YES);
			}
			saveMsgAndMsgReceiver(message, messageReceiver);	
			attachsService.updateAttachByFormId(attachIds, message.getResourceid(), Message.class.getSimpleName(), message.getSender().getResourceid(), message.getSenderName());
			
			List<MessageReceiverUser> saveList = new ArrayList<MessageReceiverUser>();
			String receiveType = messageReceiver.getReceiveType();
			String msgStatus = "unSend";
			if(Constants.BOOLEAN_NO.equals(message.getIsDraft())){
				msgStatus = "sended";
			}
			// 接收类型为用户时
			if("user".equals(receiveType)){// 发送或保存为草稿
				if(ExStringUtils.isNotEmpty(messageReceiver.getUserNames())){
					for(String userName : messageReceiver.getUserNames().split(",")){
						User _user = userService.findUnique("from "+User.class.getSimpleName()+" u where u.isDeleted=0 and u.username=? ", userName);
						if(_user != null){
							MessageReceiverUser mru = new MessageReceiverUser();
							mru.setMessageReceiver(messageReceiver);
							mru.setReceiver(_user);
							mru.setUserCNName(_user.getCnName());
							mru.setUserName(userName);
							mru.setMsgStatus(msgStatus);
							saveList.add(mru);
						}
					}
				}
			} else if(ExStringUtils.isNotEmpty(message.getIsDraft()) && Constants.BOOLEAN_NO.equals(message.getIsDraft())){// 发送信息时
				createReceiverUser(messageReceiver, saveList, msgStatus);
			}
			if(ExCollectionUtils.isNotEmpty(saveList)){
				messageReceiverUserService.batchSaveOrUpdate(saveList);
			}
		} catch (Exception e) {
			logger.error("保存消息出错", e);
		}
	}

	/**
	 * @param message
	 * @param messageReceiver
	 * @throws ServiceException
	 */
	@Override
	public void saveMsgAndMsgReceiver(Message message,
									  MessageReceiver messageReceiver) throws ServiceException {
		saveOrUpdate(message);
		// 保存接收人
		exGeneralHibernateDao.save(messageReceiver);
		if(!"user".equalsIgnoreCase(messageReceiver.getReceiveType())){		
		
			List<MessageReceiverUser> saveList = new ArrayList<MessageReceiverUser>();
			
			String msgStatus = "unSend";
			if(Constants.BOOLEAN_NO.equals(message.getIsDraft())){
				msgStatus = "sended";
			}
			createReceiverUser(messageReceiver, saveList, msgStatus);
			if(ExCollectionUtils.isNotEmpty(saveList)){
				messageReceiverUserService.batchSaveOrUpdate(saveList);
			}
		}
	}

	/**
	 * 生成接收者与用户的关联记录（除用户类型外）
	 * 
	 * @param messageReceiver
	 * @param saveList
	 * @param msgStatus
	 */
	private void createReceiverUser(MessageReceiver messageReceiver, List<MessageReceiverUser> saveList, String msgStatus) {
		List<User> receiverList = messageReceiverService.findByMessageReceiver(messageReceiver);
		if(ExCollectionUtils.isNotEmpty(receiverList)){
			for(User user : receiverList){
				MessageReceiverUser mru = new MessageReceiverUser();
				mru.setMessageReceiver(messageReceiver);
				mru.setReceiver(user);
				mru.setUserCNName(user.getCnName());
				mru.setUserName(user.getUsername());
				mru.setMsgStatus(msgStatus);
				saveList.add(mru);
			}
		}
	}
	
	@Override
	public void deleteMessage(String type, String[] ids,User user) throws ServiceException {
		Map<String,Object> condition = new HashMap<String, Object>();
		if("inbox".equalsIgnoreCase(type)){//收件箱
			if(ids!=null && ids.length>0){
				List<MessageReceiverUser> deleteList = new ArrayList<MessageReceiverUser>();
				for (String id : ids) {
					condition.clear();
					condition.put("userId", user.getResourceid());
					condition.put("userName", user.getUsername());
					condition.put("messageId", id);
					List<MessageReceiverUser> messageReceiverUserList = messageReceiverUserService.findByCondition(condition);
					deleteList.addAll(messageReceiverUserList);
				}
				if(ExCollectionUtils.isNotEmpty(deleteList)){
					messageReceiverUserService.batchDelete(deleteList);
				}
			}
		} else if("draftbox".equalsIgnoreCase(type)){//草稿箱(物理删除)
			if(ids!=null && ids.length>0){
				cascadeTruncate(ids, condition);
			}
		} else if ("sendbox".equalsIgnoreCase(type)){// 发件箱
			if(ids!=null && ids.length>0){
				List<Message> deleteMessageList = new ArrayList<Message>();
				for (String id : ids) {
					Message message = get(id);
					if(message!=null){
						deleteMessageList.add(message);
					}
				}
				if(ExCollectionUtils.isNotEmpty(deleteMessageList)){
					batchDelete(deleteMessageList);
				}
			}
		}
	}

	/**
	 * 级联删除消息（物理删除）
	 * @param ids
	 * @param condition
	 */
	private void cascadeTruncate(String[] ids, Map<String, Object> condition) {
		List<String> deleteMruList = new ArrayList<String>();
		List<String> deleteMrList = new ArrayList<String>();
		List<String> deleteMessageList = new ArrayList<String>();
		for (String id : ids) {
			condition.clear();
			condition.put("messageId", id);
			List<MessageReceiverUser> messageReceiverUserList = messageReceiverUserService.findByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(messageReceiverUserList)){
				for(MessageReceiverUser mru : messageReceiverUserList){
					deleteMruList.add(mru.getResourceid());
				}
			}
			MessageReceiver messageReceiver = messageReceiverService.findUniqueByProperty("message.resourceid", id);	
			if(messageReceiver!=null){
				deleteMrList.add(messageReceiver.getResourceid());
			}
			deleteMessageList.add(id);
		}
		if(ExCollectionUtils.isNotEmpty(deleteMruList)){
			messageReceiverUserService.batchTruncate(MessageReceiverUser.class, deleteMruList.toArray(new String[deleteMruList.size()]));
		}
		if(ExCollectionUtils.isNotEmpty(deleteMrList)){
			messageReceiverService.batchTruncate(MessageReceiver.class, deleteMrList.toArray(new String[deleteMrList.size()]));
		}
		if(ExCollectionUtils.isNotEmpty(deleteMessageList)){
			batchTruncate(Message.class, deleteMessageList.toArray(new String[deleteMessageList.size()]));
		}
	}

	/**
	 *  撤销消息
	 * @param ids
	 * @throws ServiceException
	 */
	@Override
	public void revokeMessage(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			Map<String, Object> condition = new HashMap<String, Object>();
			cascadeTruncate(ids, condition);
		}
	}	

}
