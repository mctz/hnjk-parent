package com.hnjk.edu.portal.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.model.MessageReceiverUser;
import com.hnjk.edu.portal.service.IMessageReceiverUserService;


/**
 * 消息发送者明细管理服务接口实现
 * <code>MessageSenderServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-16 下午03:48:03
 * @see 
 * @version 1.0
 */
@Transactional
@Service("messageReceiverUserService")
public class MessageReceiverUserServiceImpl extends BaseServiceImpl<MessageReceiverUser> implements IMessageReceiverUserService {

	/**
	 * 分页查询消息
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		if(ExStringUtils.isNotEmpty(page.getOrderBy())) {
			hql.append(" order by mru."+page.getOrderBy() +" "+ page.getOrder());
		}
		return exGeneralHibernateDao.findByHql(page, hql.toString(), values);
	}

	/**
	 * 根据条件获取用户接收消息HQL
	 * 
	 * @param condition
	 * @param page
	 * @param values
	 * @return
	 */
	private StringBuffer findByConditionHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer(" from "+MessageReceiverUser.class.getSimpleName()+" mru ");
		/*hql.append(" where mru.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);*/
		hql.append(" where 1=1 ");
		
		if(condition.containsKey("isDeleted")){
			hql.append(" and mru.isDeleted = :isDeleted ");
			values.put("isDeleted", condition.get("isDeleted"));
		}
		if(condition.containsKey("senderId")){
			hql.append(" and mru.messageReceiver.message.sender.resourceid=:senderId ");
			values.put("senderId", condition.get("senderId"));
		}
		if(condition.containsKey("messageId")){
			hql.append(" and mru.messageReceiver.message.resourceid=:messageId ");
			values.put("messageId", condition.get("messageId"));
		}
		if(condition.containsKey("userId")){
			hql.append(" and mru.receiver.resourceid=:userId ");
			values.put("userId", condition.get("userId"));
		}
		if(condition.containsKey("userName")){
			hql.append(" and mru.userName=:userName ");
			values.put("userName", condition.get("userName"));
		}
		if(condition.containsKey("readStatus")){
			hql.append(" and mru.readStatus=:readStatus ");
			values.put("readStatus", condition.get("readStatus"));
		}
		if(condition.containsKey("msgStatus")){
			hql.append(" and mru.msgStatus=:msgStatus ");
			values.put("msgStatus", condition.get("msgStatus"));
		}
		if(condition.containsKey("msgTitle")){//标题
			hql.append(" and lower(mru.messageReceiver.message.msgTitle) like lower(:msgTitle) ");
			values.put("msgTitle", "%"+condition.get("msgTitle")+"%");
		}
		if(condition.containsKey("isDraft")){// 是否草稿
			hql.append(" and mru.messageReceiver.message.isDraft=:isDraft ");
			values.put("isDraft", condition.get("isDraft"));
		}
		if(condition.containsKey("msgType")){//类型
			hql.append(" and mru.messageReceiver.message.msgType =:msgType ");
			values.put("msgType", condition.get("msgType"));
		}		
		if(condition.containsKey("startDate")){//开始时间
			hql.append(" and mru.messageReceiver.message.sendTime >= to_date('"+condition.get("startDate").toString()+"','yyyy-MM-dd') ");
		}
		if(condition.containsKey("endDate")){//结束时间
			hql.append(" and mru.messageReceiver.message.sendTime <= to_date('"+condition.get("endDate").toString()+"','yyyy-MM-dd')+1 ");
		}
		if(condition.containsKey("sendTime")){// 发送时间
			hql.append(" and mru.messageReceiver.message.sendTime <= :sendTime ");
			values.put("sendTime", condition.get("sendTime"));
		}
		return hql;
	}

	/**
	 * 根据条件获取用户接收消息列表
	 * 
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<MessageReceiverUser> findByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = findByConditionHql(condition, values);
		hql.append(" order by mru.messageReceiver.message.sendTime desc ");
		return (List<MessageReceiverUser>) exGeneralHibernateDao.findByHql(hql.toString(), values);
	}
	
}
