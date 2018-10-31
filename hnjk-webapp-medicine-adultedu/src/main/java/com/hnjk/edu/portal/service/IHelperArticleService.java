package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.Article;
import com.hnjk.edu.portal.model.HelpArticle;

public interface IHelperArticleService extends IBaseService<HelpArticle> {
	/**
	 * 保存文章
	 */
	void addHelpArticle(HelpArticle helpArticle,String[] attachIds) throws ServiceException; 
	
	/**
	 * 更新附件
	 */
	void updateHelpArticle(HelpArticle helpArticle,String[] attachIds) throws ServiceException;
	
	/**
	 * 根据查询条件查找
	 */
	Page findHelpArticleByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	

	/**
	 * 全文检索
	 */
	Page findHelpArticleByFullText(Page page,String[] filed,String keys) throws ServiceException;
}
