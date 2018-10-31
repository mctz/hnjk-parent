package com.hnjk.edu.learning.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.BbsUserInfo;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsUserInfoService;
//import com.hnjk.edu.teaching.service.ITeachTaskService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 论坛帖子回复服务接口实现 <code>BbsReplyServiceImpl</code>
 * <p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-8 下午02:25:22
 * @see
 * @version 1.0
 */
@Transactional
@Service("bbsReplyService")
public class BbsReplyServiceImpl extends BaseServiceImpl<BbsReply> implements IBbsReplyService {
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("bbsUserInfoService")
	private IBbsUserInfoService bbsUserInfoService;
	
//	@Autowired
//	@Qualifier("teachtaskservice")
//	private ITeachTaskService teachTaskService;
	
	@Override
	@Transactional(readOnly = true)
	public Page findBbsReplyByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Criteria objCriterion = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createCriteria(BbsReply.class);
		
		if (condition.containsKey("isDeleted")) {
			objCriterion.add(Restrictions.eq("isDeleted", condition.get("isDeleted")));
		}
		if (condition.containsKey("replyMan")) {
			objCriterion.add(Restrictions.ilike("replyMan", "%" + condition.get("replyMan") + "%"));
		}
//		if (condition.containsKey("replyManId")) 
//			objCriterion.add(Restrictions.eq("replyManId", condition.get("replyManId")));
		if (condition.containsKey("bbsTopicId")) {
			objCriterion.add(Restrictions.eq("bbsTopic.resourceid", condition.get("bbsTopicId")));
		}
		if (condition.containsKey("replyDateStart")) {
			objCriterion.add(Restrictions.ge("replyDate", new Date(Long.parseLong(condition.get("replyDateStart").toString()))));
		}
		if (condition.containsKey("replyDateEnd")) {
			objCriterion.add(Restrictions.le("replyDate", new Date(Long.parseLong(condition.get("replyDateEnd").toString()))));
		}
		
		// 排序
		if (ExStringUtils.isNotEmpty(objPage.getOrderBy()) && ExStringUtils.isNotEmpty(objPage.getOrder())) {
			if (Page.ASC.equals(objPage.getOrder())) {
				objCriterion.addOrder(Order.asc(objPage.getOrderBy()));
			} else {
				objCriterion.addOrder(Order.desc(objPage.getOrderBy()));
			}
		}
		return exGeneralHibernateDao.findByCriteriaSession(BbsReply.class, objPage, objCriterion);
	}

	@Override
	public void deleteBbsReply(String[] ids) throws ServiceException {
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				BbsReply reply = get(id);
				BbsTopic bbsTopic = reply.getBbsTopic();
				bbsTopic.setReplyCount(bbsTopic.getReplyCount() - 1);// 帖子回复数-1
				bbsTopic.getBbsSection().setTopicAndReplyCount(bbsTopic.getBbsSection().getTopicAndReplyCount()-1);
				if(ExDateUtils.isSameDay(bbsTopic.getBbsSection().getTodayTopicDate(), bbsTopic.getFillinDate())){
					bbsTopic.getBbsSection().setTodayTopicCount(bbsTopic.getBbsSection().getTodayTopicCount()-1);
				}
				reply.getBbsUserInfo().setTopicCount(reply.getBbsUserInfo().getTopicCount()-1);//用户帖子总数-1
				delete(id);
				logger.info("批量删除论坛帖子回复=" + id);
			}
		}
	}

	@Override
	public Integer getNextShowOrder(String bbsTopicId) throws ServiceException {
		List bbsReplys = exGeneralHibernateDao.findByHql(" from " + BbsReply.class.getSimpleName() + " where bbsTopic.resourceid=? ", bbsTopicId);
		Integer showOrder = 0;
		if (bbsReplys != null) {
			showOrder = bbsReplys.size();
		}
		return ++showOrder;
	}
	/**
	 * 此方法增加了后置通知发送温馨提示 com.hnjk.edu.learning.aspect.LearningAspect.sendMsgAfterBbsReply()
	 */
	@Override
	public void saveOrUpdateBbsReply(BbsReply bbsReply, String[] attachIds) throws ServiceException {
		User user = SpringSecurityHelper.getCurrentUser();
		user = (User) exGeneralHibernateDao.get(User.class, user.getResourceid());
		
		String secCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();				
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
		if(ExStringUtils.isEmpty(bbsReply.getResourceid())){
			BbsSection section = bbsReply.getBbsTopic().getBbsSection();
			section.setTopicAndReplyCount(section.getTopicAndReplyCount()+1);//总贴＋1
			if(section.getTodayTopicDate()==null || !ExDateUtils.isSameDay(section.getTodayTopicDate(), new Date())){
				section.setTodayTopicDate(new Date());//设置为当天日期
				section.setTodayTopicCount(0);//重置今日贴为0
			}
			section.setTodayTopicCount(section.getTodayTopicCount()+1);//今日贴＋1
		}
		if(ExStringUtils.isEmpty(bbsReply.getResourceid())&&bbsReply.getBbsUserInfo()==null){			
			BbsUserInfo bbsUserInfo = bbsUserInfoService.findUniqueByProperty("sysUser.resourceid",user.getResourceid());
			if(bbsUserInfo==null){//创建论坛用户信息
				bbsUserInfo = new BbsUserInfo();
				bbsUserInfo.setSysUser(user);
				bbsUserInfo.setUserName(user.getUsername());
			}
			bbsReply.setBbsUserInfo(bbsUserInfo);
			bbsUserInfo.setTopicCount(bbsUserInfo.getTopicCount()+1);
			bbsUserInfoService.saveOrUpdate(bbsReply.getBbsUserInfo());
			bbsReply.getBbsTopic().setLastReplyManId(bbsUserInfo.getResourceid());
		}
		bbsReply.setIsAttachs((null != attachIds && attachIds.length >0)?Constants.BOOLEAN_YES:Constants.BOOLEAN_NO);
		
		if(bbsReply.getBbsTopic().getIsAnswered()==Constants.BOOLEAN_FALSE&&bbsReply.getBbsTopic().getBbsSection().getSectionCode().equals(secCode)&&SpringSecurityHelper.isUserInRole(roleCode)
				//&&teachTaskService.isCourseTeacher(bbsReply.getBbsTopic().getCourse().getResourceid(), user.getResourceid(), 0)
				) {
			bbsReply.getBbsTopic().setIsAnswered(Constants.BOOLEAN_TRUE);//如果是课程老师，是指应答状态
		}
		
		bbsReply.getBbsTopic().getBbsReplys().add(bbsReply);
		saveOrUpdate(bbsReply);
		if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsReply.getIsAttachs())) {
			attachsService.updateAttachByFormId(attachIds, bbsReply.getResourceid(), BbsReply.class.getSimpleName(), user.getResourceid(), user.getCnName());
		}
		
	}

	/**
	 * 获取某个人对某个随堂问答的最新回复
	 * @param userId
	 * @param topicId
	 * @return
	 */
	@Override
	public BbsReply getSomeOneLastReply(String userId, String topicId) {
		BbsReply lastReply = null;
		List<BbsReply> replyList = findByHql("from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsUserInfo.sysUser.resourceid=? and bbsTopic.resourceid=? order by replyDate desc",userId,topicId);
		if(ExCollectionUtils.isNotEmpty(replyList)){
			lastReply = replyList.get(0);
		}
		return lastReply;
	}

}
