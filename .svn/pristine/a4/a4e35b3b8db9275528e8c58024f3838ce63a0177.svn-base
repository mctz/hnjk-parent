package com.hnjk.core.rao.dao.hibernate;

import java.util.List;

import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.analysis.cn.ChineseAnalyzer;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.SessionFactory;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.hnjk.core.rao.dao.helper.Page;

/**
 * 提供一个基本的搜索引擎模板. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-13上午11:38:24
 * @see 
 * @version 1.0
 */
public class BaseLuceneTemplate  extends HibernateDaoSupport{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**构造方法：注入session factory*/
	public BaseLuceneTemplate(SessionFactory sessionFactory){
		super.setSessionFactory(sessionFactory);
	}
	
	/**
	 * 创建索引.
	 * @param clazz
	 * @return
	 */
	public int createIndex(Class<?> clazz){
		FullTextSession session = Search.getFullTextSession(getSession());
		session.setFlushMode(FlushMode.MANUAL);
		session.setCacheMode(CacheMode.IGNORE);
		
		ScrollableResults results =  session.createQuery("from "+clazz.getSimpleName()+" where isDeleted = 0")
									.setFetchSize(1000)
									.scroll(ScrollMode.FORWARD_ONLY);
		int index = 0;
		while(results.next()){
			session.index(results.get(0));
			index++;
			if(index % 1000 == 0){
				session.clear();
			}
		}
		session.clear();
		return index;
	}
	
	/**
	 * 单个字段全文检索
	 * @param field 字段名
	 * @param text 字段值
	 * @param clazz 搜索的对象
	 * @return
	 */
	public List<?> findByFullText(String field,String text,Class<?> clazz){
		try{
			Assert.notNull(field,"搜索字段为空");
			Assert.hasText(field,"搜索字段不是文本");
			Assert.hasText(text,"搜索内容不是文本");
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			//创建lucene解析器
			org.apache.lucene.queryParser.QueryParser parser = new QueryParser(field, new StopAnalyzer());
			//parser.setAllowLeadingWildcard(true);
			org.apache.lucene.search.Query luceneQuery = parser.parse(text);
			org.hibernate.Query fullTextQuery = null;
			if(clazz != null){
				fullTextQuery=fullTextSession.createFullTextQuery(luceneQuery,clazz);
			}else{
				fullTextQuery=fullTextSession.createFullTextQuery(luceneQuery);
			}
			logger.info("find '"+text+"' on field:"+field);
			return fullTextQuery.list();
		}catch (ParseException e) {
			logger.error("not find '"+text+"' on field:"+field,e.fillInStackTrace());
			return null;
		}catch (Exception e) {
			logger.error("find '"+text+"' on field:"+field+" having error!",e.fillInStackTrace());
			return null;
		}
	}
	
	/**
	 * 根据多个字段全文检索.
	 * @param field 字段集
	 * @param text	文本值
	 * @param clazz 对象
	 * @return
	 */
	public Page findByFullText(Page page,String[] field,String text,Class<?> clazz){
		try{
			Assert.notNull(field,"搜索字段为空");
			Assert.notEmpty(field,"搜索字段数目为0");
			Assert.hasText(text,"搜索内容不是文本");
			FullTextSession fullTextSession = Search.getFullTextSession(getSession());
			//BooleanClause.Occur[] flags = new BooleanClause.Occur[field.length];
			//for(int i=0;i<field.length;i++){
			//	flags[i]=BooleanClause.Occur.SHOULD;
			//}
			//org.apache.lucene.search.Query luceneQuery = MultiFieldQueryParser.parse(text, field,flags, new StandardAnalyzer());
			ChineseAnalyzer analyzer = new ChineseAnalyzer();
			MultiFieldQueryParser parser = new MultiFieldQueryParser(field,analyzer);
			parser.setDefaultOperator(QueryParser.OR_OPERATOR);//使用OR
			BooleanQuery booleanQuery = new BooleanQuery();//多条件查询
			booleanQuery.add(parser.parse(text), BooleanClause.Occur.MUST);
			
			//添加查询条件
//			QueryParser queryParser = null;
//			if(null != queryParaMap){
//				for(String key : queryParaMap.keySet()){
//					queryParser = new QueryParser(key,analyzer);
//					booleanQuery.add(queryParser.parse(queryParaMap.get(key).toString()), BooleanClause.Occur.MUST); 
//				}
//			}
			org.hibernate.Query fullTextQuery = null;
			if(clazz!=null){
				fullTextQuery=fullTextSession.createFullTextQuery(booleanQuery,clazz);
			}else{
				fullTextQuery=fullTextSession.createFullTextQuery(booleanQuery);
			}
			logger.info("find '"+text+"' on field:"+field.toString());
			
			if(page.isAutoCount()){
				page.setTotalCount(fullTextQuery.list().size());
			}
			fullTextQuery.setFirstResult(page.getFirst());
			fullTextQuery.setMaxResults(page.getPageSize());
			page.setResult(fullTextQuery.list());
			return page;
		}catch (ParseException e) {
			logger.error("not find '"+text+"' on field:"+field,e.fillInStackTrace());
			return null;
		}catch (Exception e) {
			logger.error("find '"+text+"' on field:"+field+" having error!",e.fillInStackTrace());
			return null;
		}
	}
			
}
