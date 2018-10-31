package com.hnjk.edu.portal.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.portal.model.Article;

/**
 *  <code>IArticleService</code>门户网站文章服务接口.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-20 下午07:10:04
 * @see 
 * @version 1.0
 */
public interface IArticleService  extends IBaseService<Article> {
	
	/**
	 * 保存文章
	 * @param article
	 * @param attachIds
	 * @throws ServiceException
	 */
	void addArticle(Article article,String[] attachIds) throws ServiceException; 
	
	/**
	 * 更新附件
	 * @param article
	 * @param attachIds
	 * @throws ServiceException
	 */
	void updateArticle(Article article,String[] attachIds) throws ServiceException;
	
	/**
	 * 根据查询条件查找
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findArticleByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	/**
	 * 根据查询条件查找
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Article> findArticleByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据栏目ID或文字类型查找列表
	 * @param unit单位ID
	 * @param channelId
	 * @param articleType
	 * @param isPhotoNews 是否图片新闻
	 * @param limitnum 返回条数
	 * @return
	 * @throws ServiceException
	 */
	List<Article> findArticleByType(String unitId,String channelId,String articleType,String isPhotoNews,int limitnum) throws ServiceException;
	
	/**
	 * 全文检索
	 * @param page
	 * @param filed
	 * @param keys
	 * @return
	 * @throws ServiceException
	 */
	Page findArticleByFullText(Page page,String[] filed,String keys) throws ServiceException;
}
