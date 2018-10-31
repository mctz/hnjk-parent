package com.hnjk.edu.portal.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.portal.model.MessageStat;
import com.hnjk.edu.portal.service.IMessageStatService;
/**
 * 消息阅读情况服务接口实现
 * <code>MessageStatServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-22 下午02:57:53
 * @see 
 * @version 1.0
 */
@Transactional
@Service("messageStatService")
public class MessageStatServiceImpl extends BaseServiceImpl<MessageStat> implements IMessageStatService {

	@Override
	public boolean isMessageReaded(String msgId, String readUserId) throws ServiceException {
		Long stat = exGeneralHibernateDao.findUnique("select count(s) from "+MessageStat.class.getSimpleName()+" s where s.isDeleted=0 and s.message.resourceid=? and s.receiverId =?", new Object[]{msgId,readUserId});
		return (stat!=null&&stat.intValue()>0)?true:false;
	}

	/* (non-Javadoc)
	 * @see com.hnjk.edu.portal.service.IMessageStatService#getIsReadMsgIds(java.lang.String)
	 */
	@Override
	public List<String> getIsReadMsgIds(String userId) throws ServiceException {
		return (List<String>)exGeneralHibernateDao.findByHql("select message.resourceid from "+MessageStat.class.getSimpleName()+" where isDeleted=0 and receiverId = ? order by message.sendTime desc ", userId);
	}

	
}
