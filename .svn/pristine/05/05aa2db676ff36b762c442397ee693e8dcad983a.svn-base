package com.hnjk.edu.portal.service.impl;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.portal.model.Article;
import com.hnjk.edu.portal.model.HelpArticle;
import com.hnjk.edu.portal.service.IHelperArticleService;
import com.hnjk.platform.system.service.IAttachsService;
@Service("helpArticleService")
@Transactional
public class HelpActicleServiceImpl extends BaseServiceImpl<HelpArticle> implements
		IHelperArticleService {
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	

	@Override
	public void addHelpArticle(HelpArticle helpArticle, String[] attachIds)
			throws ServiceException {
		save(helpArticle);//保存
		if(null != attachIds){
			attachsService.updateAttachByFormId(attachIds, helpArticle.getResourceid(), "HELPARTICLE", helpArticle.getFillinManId(), helpArticle.getFillinMan());
		}

	}

	@Override
	public void updateHelpArticle(HelpArticle helpArticle, String[] attachIds)
			throws ServiceException {
		update(helpArticle);
		if(null != attachIds){
			attachsService.updateAttachByFormId(attachIds, helpArticle.getResourceid(), "HELPARTICLE", helpArticle.getFillinManId(), helpArticle.getFillinMan());
		}

	}

	@Override
	@Transactional(readOnly=true)
	public Page findHelpArticleByCondition(Map<String, Object> condition,
			Page page) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("title")){
			objCriterion.add(Restrictions.eq("title",condition.get("title")));
		}
		if(condition.containsKey("channelId")){
			objCriterion.add(Restrictions.eq("channel.resourceid",condition.get("channelId")));
		}
		if(condition.containsKey("isPublish")){
			objCriterion.add(Restrictions.eq("isPublish",condition.get("isPublish")));
		}
		if (condition.containsKey("tags")) {
			objCriterion.add(Restrictions.like("tags", "%"+condition.get("tags")+"%"));
		}
		if (condition.containsKey("titlelike")) {
			objCriterion.add(Restrictions.like("title", "%"+condition.get("titlelike")+"%"));
		}
		if (condition.containsKey("content")) {
			objCriterion.add(Restrictions.like("content", "%"+condition.get("content")+"%"));
		}
		if (condition.containsKey("ChannalIds")){
			List<String> channelIds = (List<String>)condition.get("ChannelIds");
			Criterion criterion = null;
			for (String channelId : channelIds) {
				if(null!=criterion){
					criterion = Restrictions.or(criterion, Restrictions.eq("channel.resourceid", channelId));
				}else{
					criterion = Restrictions.eq("channel.resourceid", channelId);
				}
			}
			objCriterion.add(criterion);
			
		}
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		
		return exGeneralHibernateDao.findByCriteria(HelpArticle.class, page, objCriterion.toArray(new Criterion[objCriterion.size()]));
	}

	
	@Override
	@Transactional(readOnly=true)
	public Page findHelpArticleByFullText(Page page, String[] filed, String keys)
			throws ServiceException {
		return luceneTextQuery.findByFullText(page, filed, keys, HelpArticle.class);
	}

}
