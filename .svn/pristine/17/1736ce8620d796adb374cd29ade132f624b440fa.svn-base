package com.hnjk.edu.portal.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.portal.model.MessageSender;
import com.hnjk.edu.portal.service.IMessageSenderService;
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
@Service("messageSenderService")
public class MessageSenderServiceImpl extends BaseServiceImpl<MessageSender> implements IMessageSenderService {

	@Override
	public Page findMessageByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+MessageSender.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
				
		if(condition.containsKey("msgTitle")){//标题
			hql += " and lower(message.msgTitle) like lower(:msgTitle) ";
			values.put("msgTitle", "%"+condition.get("msgTitle")+"%");
		}	
		if(condition.containsKey("msgType")){//类型
			hql += " and message.msgType =:msgType ";
			values.put("msgType", condition.get("msgType"));
		}	
		if(condition.containsKey("isDraft")){//是否草稿
			hql += " and message.isDraft =:isDraft ";
			values.put("isDraft", condition.get("isDraft"));
		}
		if(condition.containsKey("senderId")){//发送人
			hql += " and senderId =:senderId ";
			values.put("senderId", condition.get("senderId"));
		}		
		if(condition.containsKey("startDate")){//开始时间
			hql += " and message.sendTime >= to_date('"+condition.get("startDate").toString()+"','yyyy-MM-dd') ";
		}
		if(condition.containsKey("endDate")){//结束时间
			hql += " and message.sendTime <= to_date('"+condition.get("endDate").toString()+"','yyyy-MM-dd')+1 ";
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())) {
			hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		}
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
}
