package com.hnjk.edu.portal.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageReceiverUser;
import com.hnjk.edu.portal.model.MessageSender;
import com.hnjk.edu.portal.service.IMessageReceiverService;
import com.hnjk.edu.portal.service.IMessageReceiverUserService;
import com.hnjk.edu.portal.service.IMessageStatService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

import edu.emory.mathcs.backport.java.util.Arrays;
/**
 * 消息接受者明细管理服务接口实现
 * <code>MessageReceiverServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-16 下午03:53:54
 * @see 
 * @version 1.0
 */
@Transactional
@Service("messageReceiverService")
public class MessageReceiverServiceImpl extends BaseServiceImpl<MessageReceiver> implements IMessageReceiverService {
	
	@Autowired
	@Qualifier("messageStatService")
	private IMessageStatService messageStatService;
	

	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("messageReceiverUserService")
	private IMessageReceiverUserService messageReceiverUserService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Transactional(readOnly=true)
	@Deprecated
	public Page findMessageByCondition_old(Map<String, Object> condition, Page objPage) throws ServiceException {
		logger.info("数据库CPU占用追踪：MessageReceiverServiceImpl.findMessageByCondition,Date:"+new Date());
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = "select s from "+MessageReceiver.class.getSimpleName()+" r,"+MessageSender.class.getSimpleName()+" s ";
		hql += " where r.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
				
		if(condition.containsKey("msgTitle")){//标题
			hql += " and lower(r.message.msgTitle) like lower(:msgTitle) ";
			values.put("msgTitle", "%"+condition.get("msgTitle")+"%");
		}
		if(condition.containsKey("msgType")){//类型
			hql += " and r.message.msgType =:msgType ";
			values.put("msgType", condition.get("msgType"));
		}		
		if(condition.containsKey("startDate")){//开始时间
			hql += " and r.message.sendTime >= to_date('"+condition.get("startDate").toString()+"','yyyy-MM-dd') ";
		}
		if(condition.containsKey("endDate")){//结束时间
			hql += " and r.message.sendTime <= to_date('"+condition.get("endDate").toString()+"','yyyy-MM-dd')+1 ";
		}
		//发送时间小于当前
		hql += " and r.message.sendTime <=:today ";
		values.put("today", new Date());
		
		hql += " and ( 1=0 or r.receiveType = 'user' and EXISTS (from "+ MessageReceiverUser.class.getSimpleName() +" eu where eu.isDeleted=0 ";
		hql += " and eu.username = '"+(condition.containsKey("userName")?condition.get("userName"):"")+"' and eu.messageReceiver.resourceid = r.resourceid ) ";
		hql += " or r.receiveType = 'org' and r.orgUnitCodes like '%"+(condition.containsKey("unitCode")?condition.get("unitCode"):"")+"%' ";
		hql += "or r.receiveType = 'role' and (1=0 ";
		if(condition.containsKey("unitCode")){
			String roles = null!=condition.get("roleCode")?condition.get("roleCode").toString():"";
			for (String r : roles.split(",")) {
				hql += " or r.roleCodes like '%"+r+"%' ";
			}
		}		
		hql += " )";
		hql += " or r.receiveType like '%org%' and r.orgUnitCodes like '%"+(condition.containsKey("unitCode")?condition.get("unitCode"):"")+"%' ";
		hql += "and r.receiveType like '%role%' and (1=0 ";
		if(condition.containsKey("unitCode")){
			String roles = null!=condition.get("roleCode")?condition.get("roleCode").toString():"";
			for (String r : roles.split(",")) {
				hql += " or r.roleCodes like '%"+r+"%' ";
			}
		}		
		hql += " ))";
		hql += " and s.message.resourceid=r.message.resourceid ";
//		if(condition.containsKey("unread"))//未读
//			hql += " and exists(from MessageStat stat where stat.message.resourceid=r.message and stat.receiverId='"+user.getResourceid()+"') ";
		if(condition.containsKey("senderIds")){//发送人
			hql += " and s.senderId in ("+condition.get("senderIds")+") ";
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())) {
			hql += " order by r."+objPage.getOrderBy() +" "+ objPage.getOrder();
		}
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Transactional(readOnly=true)
	@Override
	public Page findMessageByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		logger.info("数据库CPU占用追踪：MessageReceiverServiceImpl.findMessageByCondition,Date:"+new Date());
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = "select r from "+MessageReceiver.class.getSimpleName()+" r ";
		hql += " where r.isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("msgTitle")){//标题
			hql += " and lower(r.message.msgTitle) like lower(:msgTitle) ";
			values.put("msgTitle", "%"+condition.get("msgTitle")+"%");
		}
		if(condition.containsKey("msgType")){//类型
			hql += " and r.message.msgType =:msgType ";
			values.put("msgType", condition.get("msgType"));
		}		
		if(condition.containsKey("startDate")){//开始时间
			hql += " and r.message.sendTime >= to_date('"+condition.get("startDate").toString()+"','yyyy-MM-dd') ";
		}
		if(condition.containsKey("endDate")){//结束时间
			hql += " and r.message.sendTime <= to_date('"+condition.get("endDate").toString()+"','yyyy-MM-dd')+1 ";
		}
		//发送时间小于当前
		hql += " and r.message.sendTime <=:today ";
		values.put("today", new Date());
		
		hql += " and ( 1=0 or (r.receiveType = 'user' and EXISTS (from "+ MessageReceiverUser.class.getSimpleName() +" eu where eu.isDeleted=0 ";
		hql += " and eu.userName = '"+(condition.containsKey("userName")?condition.get("userName"):"")+"' and eu.messageReceiver.resourceid = r.resourceid )) ";
		hql += " or (r.receiveType = 'org' and r.orgUnitCodes like '%"+(condition.containsKey("unitCode")?condition.get("unitCode"):"")+"%')";
		hql += "or (r.receiveType = 'role' and (1=0 ";
		if(condition.containsKey("unitCode")){
			String roles = null!=condition.get("roleCode")?condition.get("roleCode").toString():"";
			for (String r : roles.split(",")){
				hql += " or r.roleCodes like '%"+r+"%' ";
			}
		}		
		hql += " ))";
		hql += " or (r.receiveType = 'grade' and r.grades like '%"+(condition.containsKey("grade")?condition.get("grade"):"")+"%') ";
		hql += " or (r.receiveType = 'classes' and r.classes like '%"+(condition.containsKey("classes")?condition.get("classes"):"")+"%') ";
		hql += " )";
		if(condition.containsKey("senderIds")){//发送人IDs
			hql += " and r.message.sender.resourceid in ("+condition.get("senderIds")+") ";
		}
		if(condition.containsKey("senderId")){//发送人
			hql += " and r.message.sender.resourceid=:senderId ";
			values.put("senderId", condition.get("senderId"));
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())) {
			hql += " order by r."+objPage.getOrderBy() +" "+ objPage.getOrder();
		}
			return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public List<Map<String, Object>> getUserMessageList(Map<String, Object> condition) throws ServiceException {
		//构造page对象
		Page objPage = new Page();
		objPage.setPageSize(15);
		if(condition.containsKey("fetchSize")){//获取的消息条数
			objPage.setPageSize(Integer.parseInt(condition.get("fetchSize").toString()));
		}
		
		objPage.setOrderBy("messageReceiver.message.sendTime"); //按发送时间排序
		objPage.setOrder(Page.DESC);
		
//		Page messagePage = findMessageByCondition(condition, objPage);
		Page messagePage = messageReceiverUserService.findByCondition(condition, objPage);
		//获取阅读人列表
//		List<String> statlist = messageStatService.getIsReadMsgIds(condition.get("userId").toString());
		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();			
		for (Object o : messagePage.getResult()) {
			MessageReceiverUser receiverUser = (MessageReceiverUser)o;
			Message message = receiverUser.getMessageReceiver().getMessage();
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("resourceid", message.getResourceid());
			map.put("msgType", JstlCustomFunction.dictionaryCode2Value("CodeMsgType", message.getMsgType()));
			map.put("msgTitle", message.getMsgTitle());
			try {
				map.put("sendTime", ExDateUtils.formatDateStr(message.getSendTime(), "MM/dd HH:mm"));
			} catch (ParseException e) {
				//noting
			}
			map.put("sender", message.getSenderName());
//			map.put("isRead", statlist.contains(receiver.getMessage().getResourceid()));
			map.put("isRead", ("readed".equals(receiverUser.getReadStatus())));
			list.add(map);
		}
		
		return list;
	}

	/**
	 * 根据消息接收人信息获取所有接收人
	 * 
	 * @param messageReceiver
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> findByMessageReceiver(MessageReceiver messageReceiver) throws ServiceException {
		List<User> userList = new ArrayList<User>();
		try {
			if(messageReceiver != null){
				 String params = "";
				StringBuffer hql = new StringBuffer(" from " + User.class.getSimpleName() + " u where u.isDeleted=0 ");
				if("org".equals(messageReceiver.getReceiveType()) || "role".equals(messageReceiver.getReceiveType())){
					if(ExStringUtils.isNotEmpty(messageReceiver.getRoleCodes())){
						String temp = messageReceiver.getRoleCodes();
						temp = "'"+temp.replaceAll(",", "','")+"'";
						hql.setLength(0);
						hql.append("select distinct u from " + User.class.getSimpleName() + " u join u.roles r where "
								+ "u.isDeleted=0 and r.roleCode in ("+temp+") ");
					}
					if(ExStringUtils.isNotEmpty(messageReceiver.getOrgUnitCodes())){
						params = messageReceiver.getOrgUnitCodes();
						params = "'"+params.replaceAll(",", "','")+"'";
						hql.append(" and u.orgUnit.unitCode in ("+params+") ");
					}
					
				} else if("grade".equals(messageReceiver.getReceiveType())){
					params = messageReceiver.getGrades();
					params = "'"+params.replaceAll(",", "','")+"'";
					hql.setLength(0);
					hql.append(" select distinct u from StudentInfo si inner join si.sysUser u inner join si.grade g ");
					hql.append("where u.isDeleted=0 and si.isDeleted=0 and g.isDeleted=0 and g.resourceid in ("+params+") ");
				} else if("classes".equals(messageReceiver.getReceiveType())){
					params = messageReceiver.getClasses();
					params = "'"+params.replaceAll(",", "','")+"'";
					hql.setLength(0);
					hql.append(" select distinct u from StudentInfo si inner join si.sysUser u inner join si.classes cl ");
					hql.append("where u.isDeleted=0 and si.isDeleted=0 and cl.isDeleted=0 and cl.resourceid in ("+params+") ");
				}
				userList = (List<User>)exGeneralHibernateDao.findByHql(hql.toString());
			}
		} catch (Exception e) {
			logger.error("根据消息接收人信息获取所有接收人出错", e);
		}
		return userList;
	}
	
}
