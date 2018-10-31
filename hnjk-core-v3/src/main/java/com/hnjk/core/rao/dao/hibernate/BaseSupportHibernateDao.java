package com.hnjk.core.rao.dao.hibernate;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 提供一个基础的hibernate DAO支持类. <p>
 * 业务层可以继承以这个支持类与数据交互.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-13上午09:39:44
 * @see 
 * @version 1.0
 */
public class BaseSupportHibernateDao{
	
	public ExGeneralHibernateDao exGeneralHibernateDao;
	
	public BaseLuceneTemplate luceneTextQuery;
	
	/**
	 * 将session factory 传给<code>ExGeneralHibernateDao</code>并创建dao实例
	 * @param sessionFactory
	 */
	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		exGeneralHibernateDao = new ExGeneralHibernateDao(sessionFactory);
		luceneTextQuery = new BaseLuceneTemplate(sessionFactory);
	}
		
}
