package com.hnjk.edu.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
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
import com.hnjk.edu.portal.service.IArticleService;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;

/**
 *  <code>ArticleServiceImpl</code>文章服务实现。<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-20 下午07:11:35
 * @see 
 * @version 1.0
 */
@Service("articleService")
@Transactional
public class ArticleServiceImpl  extends BaseServiceImpl<Article> implements IArticleService {	

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findArticleByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("title")){
			objCriterion.add(Restrictions.like("title","%"+condition.get("title")+"%"));
		}
		if(condition.containsKey("fillinMan")){
			objCriterion.add(Restrictions.like("fillinMan","%"+condition.get("fillinMan")+"%"));
		}
		if(condition.containsKey("channelId")){
			objCriterion.add(Restrictions.eq("channel.resourceid",condition.get("channelId")));
		}
		if(condition.containsKey("channelIdList")){
			objCriterion.add(Restrictions.in("channel.resourceid", (List)condition.get("channelIdList")));
		}
		
		if(condition.containsKey("auditStatus")){
			objCriterion.add(Restrictions.eq("auditStatus",condition.get("auditStatus")));
		}
		
		if(condition.containsKey("isPhotoNews")){			
			objCriterion.add(Restrictions.eq("isPhotoNews",condition.get("isPhotoNews")));
		}
		if(condition.containsKey("isDraft")){
			objCriterion.add(Restrictions.eq("isDraft",condition.get("isDraft")));
		}
		if(condition.containsKey("auditStatus")){
			objCriterion.add(Restrictions.eq("auditStatus",condition.get("auditStatus")));
		}
		
		if(condition.containsKey("unitId")){
			objCriterion.add(Restrictions.eq("orgUnit.resourceid",condition.get("unitId")));
		}
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		
		return exGeneralHibernateDao.findByCriteria(Article.class, page, objCriterion.toArray(new Criterion[objCriterion.size()]));
	}

	@Override
	@Transactional(readOnly=true)
	public List<Article> findArticleByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> map = new HashMap<String,Object>();
		String hql = "from " +Article.class.getSimpleName() +" t where t.isDeleted=:isDeleted ";
		map.put("isDeleted", 0);
		if(condition.containsKey("title")){
			hql += " and t.title like :title";
			map.put("title", "%"+condition.get("title")+"%");
		}
		if(condition.containsKey("channelId")){
			hql += " and t.channel.resourceid=:channelId ";
			map.put("channelId", condition.get("channelId"));
		}
		if(condition.containsKey("channelIdList")){
			hql += " and t.channel.resourceid in(:channelIdList)";
			map.put("channelIdList", (List)condition.get("channelIdList"));
		}
		
		if(condition.containsKey("auditStatus")){
			hql += " and t.auditStatus =:auditStatus ";
			map.put("auditStatus", condition.get("auditStatus"));
		}
		
		if(condition.containsKey("isPhotoNews")){			
			hql += " and t.isPhotoNews =:isPhotoNews ";
			map.put("isPhotoNews", condition.get("isPhotoNews"));
		}
		if(condition.containsKey("isDraft")){
			hql += " and t.isDraft =:isDraft ";
			map.put("isDraft", condition.get("isDraft"));
		}
		
		if(condition.containsKey("unitId")){
			hql += " and t.orgUnit.resourceid =:unitId ";
			map.put("unitId", condition.get("unitId"));
		}
		hql +=" order by t.auditDate desc ";
		
		
		return (List<Article>) exGeneralHibernateDao.findByHql(hql, map);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<Article> findArticleByType(String unitId,String channelId, String articleType,String isPhotoNews, int limitnum) throws ServiceException {		
		String hql = " from "+Article.class.getName()+" where isDeleted = 0 and isDraft = :isDraft ";
		if(!ExStringUtils.isEmpty(unitId)){//如果传入单位ID，则表示为校外学习中心的文章，否则，默认为本部文章
			hql += " and orgUnit.resourceid = :unitId ";
		}else{
			hql += " and orgUnit.unitType = 'localDepart' ";
		}
		
		if(!ExStringUtils.isEmpty(channelId)){
			hql += " and channel.resourceid = :channelId ";
		}
		
		if(!ExStringUtils.isEmpty(articleType)){
			hql += " and artitype = :articleType ";
		}
		if(!ExStringUtils.isEmpty(isPhotoNews)){
			hql += " and isPhotoNews = :isPhotoNews ";
		}
		hql += " and auditStatus = :auditStatus  order by topLevel desc , fillinDate desc";//排序
		
		Query query = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createQuery(hql);
		query.setParameter("isDraft", Constants.BOOLEAN_NO);
		if(!ExStringUtils.isEmpty(unitId)){
			query.setParameter("unitId", unitId);
		}
		
		if(!ExStringUtils.isEmpty(channelId)){
			query.setParameter("channelId", channelId);
		}	
		if(!ExStringUtils.isEmpty(articleType)){
			query.setParameter("articleType", articleType);
		}
		if(!ExStringUtils.isEmpty(isPhotoNews)){
			query.setParameter("isPhotoNews", Constants.BOOLEAN_YES);
		}
		query.setParameter("auditStatus", Article.AUDIT_STATUS_PASS);//审核通过的
		
		if(limitnum>0){//添加条数限制
			query.setFirstResult(0);
			query.setMaxResults(limitnum);
		}
		List<Article> list = query.list();
		return list;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findArticleByFullText(Page page, String[] filed, String keys) throws ServiceException {		
		return luceneTextQuery.findByFullText(page, filed, keys, Article.class);
	}


	@Override
	public void addArticle(Article article, String[] attachIds) throws ServiceException {
		save(article);//保存
		if(null != attachIds){
			String postfix = attachsService.getPostfixAttr(attachIds);
			long size = attachsService.updateAttachByFormId(attachIds, article.getResourceid(), Article.ARTICLE_ATTACHS_TYPE, article.getFillinManId(), article.getFillinMan());
			if(size!=0 || ExStringUtils.isNotEmpty(postfix)){
				article.setFileSize(size);
				article.setArtitype(postfix.toString());
				save(article);
			}
		}
		
		
	}

	@Override
	public void updateArticle(Article article, String[] attachIds) throws ServiceException {
		if(null !=attachIds ){
			String postfix = attachsService.getPostfixAttr(attachIds);
			long size = attachsService.updateAttachByFormId(attachIds, article.getResourceid(), Article.ARTICLE_ATTACHS_TYPE, article.getFillinManId(), article.getFillinMan());
			if(size!=0 || ExStringUtils.isNotEmpty(postfix)){
				article.setFileSize(size);
				article.setArtitype(postfix.toString());
			}
		}
		update(article);
	}
		
	
}
