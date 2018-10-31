package com.hnjk.core.rao.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import com.hnjk.core.foundation.utils.SqlServerParse;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import org.hibernate.SQLQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.util.Assert;

import com.hnjk.core.rao.dao.helper.Page;

/**
 * 提供一个Jdbc方式操作数据的模板，基于Spring2.5 <code>SimpleJdbcTemplate</code>封装实现. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26上午11:48:03
 * @see #org.springframework.jdbc.core.simple.SimpleJdbcTemplate()
 * @version 1.0
 */

public class BaseJdbcTemplate{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SimpleJdbcTemplate jdbcTemplate = null;//定义jdbctemplate
	
	protected JdbcTemplate originalJdbcTemplate = null;//原生jdbcTemp，用于一些遗留系统的SQL。

	protected ExGeneralHibernateDao exGeneralHibernateDao;
		
	/**构造方法，将数据源赋值给jdbcTemplate对象*/	
	public  BaseJdbcTemplate(DataSource dataSource){		
		jdbcTemplate = new SimpleJdbcTemplate(dataSource);//创建一个simpleJdbcTemplate实例
		originalJdbcTemplate = new JdbcTemplate(dataSource);//创建一个原生的jdbcTemplate
		logger.debug("创建SimpleJdbcTemplate实例...");
	}
	
	public SimpleJdbcTemplate getJdbcTemplate(){
		return jdbcTemplate;
	}	
		
	/**
	 * @return the originalJdbcTemplate
	 */
	public JdbcTemplate getOriginalJdbcTemplate() {
		return originalJdbcTemplate;
	}

	/**
	 * 执行增删改等操作.
	 * 使用：如<per>insert into users (name,login_name,password) values(:name,:loginName,:password)</per>
	 * 使用‘：’作为参数，参数为bean的属性
	 * @param sql sql语句
	 * @param bean 
	 * @return
	 */
	public int executeForObject(final String sql,Object bean) throws Exception{
		Assert.hasText(sql,"sql语句不正确!");
		if(bean!=null){			
			return jdbcTemplate.update(sql, paramBeanMapper(bean));
		}else{
			return jdbcTemplate.update(sql);
		}
	}
	
	/**
	 * 根据指定条件进行增删改等操作.
	 * 使用：<pre>insert into users (name,login_name,password) values(:name,:login_name,:password)</pre>
	 * 使用‘：’作为参数，参数为map的key
	 * @param sql
	 * @param parameters 参数集合(ke = 参数名,value = 参数值)
	 * @return
	 */
	public int executeForMap(final String sql,Map parameters) throws Exception{
		Assert.hasText(sql,"sql语句不正确!");
		if(parameters!=null){
			return jdbcTemplate.update(sql, parameters);
		}else{
			return jdbcTemplate.update(sql);
		}
	}
	
	/**
	 * 根据SQL语句，返回对象集合.<p>
	 * 使用‘：’作为参数，参数名为map的key,用法：
	 * <pre>select * from table where createdate = :cdate </pre>
	 * @param sql sql语句
	 * @param clazz 
	 * @param parameters 参数集合(ke = 参数名,value = 参数值)
	 * @return 不符合要求时将返回<code>null</code>
	 */	
	@SuppressWarnings("unchecked")
	public List findList(final String sql,Class clazz,Map parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			Assert.notNull(clazz,"集合中对象类型不能为空!");
			if(parameters!=null){
				return jdbcTemplate.query(sql, resultBeanMapper(clazz),parameters);
			}else{
				return jdbcTemplate.query(sql, resultBeanMapper(clazz));
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 根据SQL语句，返回对象集合.<p>
	 * 使用‘：’作为参数，参数名为map的key
	 * @param sql
	 * @param clazz
	 * @param parameters
	 * @return
	 * @throws Exception
	 */
	public List findListWithEntity(final String sql,Class clazz,Map<String,Object> parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			Assert.notNull(clazz,"集合中对象类型不能为空!");
			SQLQuery query = exGeneralHibernateDao.getSessionFactory().getCurrentSession()
					.createSQLQuery(sql.toString()).addEntity(clazz);
			if(parameters!=null){
				//填充参数
				for(String key : parameters.keySet()){
					query.setParameter(key, parameters.get(key));
				}
			}
			return  query.list();
		}catch (Exception e) {
			logger.error("不可抛出的异常:"+e.getMessage());
			return null;
		}
	}
	/**
	 * 根据SQL语句，返回对象集合.<p>
	 * 使用‘?’作为参数，用法：
	 * <pre>select * from table where createdate = ? </pre>
	 * @param sql sql语句
	 * @param clazz 
	 * @param parameters 参数值集合
	 * @return 不符合要求时将返回<code>null</code>
	 */	
	@SuppressWarnings("unchecked")
	public List findList(final String sql,Object[] parameters, Class clazz)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			Assert.notNull(clazz,"集合中对象类型不能为空!");
			if(parameters!=null){
				return jdbcTemplate.query(sql, resultBeanMapper(clazz),parameters);
			}else{
				return jdbcTemplate.query(sql, resultBeanMapper(clazz));
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 根据SQL语句，返回对象集合.<p>
	 * 使用‘?’作为参数
	 * @param sql
	 * @param parameters
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public List findListWithEntity(final String sql,Object[] parameters,Class clazz)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			Assert.notNull(clazz,"集合中对象类型不能为空!");
			SQLQuery query = exGeneralHibernateDao.getSessionFactory().getCurrentSession()
					.createSQLQuery(sql.toString()).addEntity(clazz);
			if(parameters!=null){
				//填充参数
				int i = 0;
				for(Object value : parameters){
					query.setParameter(i++, value);
				}
			}
			return  query.list();
		}catch (Exception e) {
			logger.error("不可抛出的异常:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 根据SQL查找对象.
	 * 用法：
	 * <pre>select * from table where resourceid = :id </pre> 
	 * 使用‘：’作为参数，参数名为map key.
	 * @param sql
	 * @param clazz
	 * @param parameters 参数集合(ke = 参数名,value = 参数值)
	 * @return 不符合要求时将返回<code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public Object findForObject(final String sql,Class clazz, Map parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			Assert.notNull(clazz,"集合中对象类型不能为空!");
			if(parameters!=null){
				return jdbcTemplate.queryForObject(sql, resultBeanMapper(clazz), parameters);
			}else{
				return jdbcTemplate.queryForLong(sql, resultBeanMapper(clazz));
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常:"+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 根据SQL，返回数值性结果.
	 * 多用于count,sum等统计情况，支持参数别名.
	 * 使用:<pre>select count(*) from tb where name=:name</pre>
	 * @param sql
	 * @param parameters 参数集合(ke = 参数名,value = 参数值)
	 * @return 
	 */
	public long findForLong(final String sql,Map parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			if(parameters!=null){
				return jdbcTemplate.queryForLong(SqlServerParse.removeOrderBy(sql), parameters);
			}else{
				return jdbcTemplate.queryForLong(SqlServerParse.removeOrderBy(sql));
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常："+e.getMessage());
			return 0;
		}
	}
	
	/**
	 * 根据SQL语句，得到一个对象的MAP集合.
	 * 对于某些查询，如多表联合查询中，我们需要取不同表中的字段，这时可以使用map进行封装，key=字段名,value=字段值.
	 * 支持参数别名.
	 * @param sql
	 * @param parameters 参数集合(ke = 参数名,value = 参数值)
	 * @return 不符合要求时将返回<code>null</code>
	 */
	public Map findForMap(final String sql,Map parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			if(parameters!=null){
				return jdbcTemplate.queryForMap(sql, parameters);
			}else{
				return jdbcTemplate.queryForMap(sql);
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常："+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 使用SQL语句，得到一个封装为Map后的List集合.
	 * 可以作为对{@link #findForMap(String, Map)}的补充.
	 * @param sql
	 * @param parameters 参数集合(ke = 参数名,value = 参数值)
	 * @return 不符合要求时将返回<code>null</code>
	 */
	public List<Map<String,Object>> findForListMap(final String sql,Map parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			if(parameters!=null){
				return jdbcTemplate.queryForList(sql, parameters);
			}else{
				return jdbcTemplate.queryForList(sql);
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常："+e.getMessage());
			return null;
		}
	}
	/**
	 * 使用SQL语句，得到一个封装为Map后的List集合.
	 * @param sql
	 * @param parameters 参数集合列表
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findForList(final String sql,Object[] parameters)  throws Exception{
		try{
			Assert.hasText(sql,"sql语句不正确!");
			if(parameters!=null){
				return jdbcTemplate.queryForList(sql, parameters);
			}else{
				return jdbcTemplate.queryForList(sql);
			}
		}catch (Exception e) {
			logger.error("不可抛出的异常："+e.getMessage());
			return null;
		}
	}
	
	/**
	 * 分页查询
	 * @param page
	 * @param sql
	 * @param parameters
	 * @param clazz
	 * @return
	 */
	public Page findList(final Page page, final String sql, final Object[] parameters,final Class clazz) {

		 final long rowCount = originalJdbcTemplate.queryForLong("select count(*) from("+ SqlServerParse.removeOrderBy(sql)+")", parameters);
		 if(page.isAutoCount()){
			 page.setTotalCount(new Long(rowCount).intValue());
		 }
		 //查出单页的记录数
		 final int startRow = (page.getPageNum() - 1) * page.getPageSize();
		 final ParameterizedRowMapper rowMapper = resultBeanMapper(clazz);
		 originalJdbcTemplate.query(sql, parameters, new ResultSetExtractor() {
			 @Override
			 public Object extractData(ResultSet rs) throws SQLException,  DataAccessException {
				 final List pageItems = page.getResult();
				 int currentRow = 0;
				 while (rs.next() && currentRow < startRow + page.getPageSize()) {
					 if (currentRow >= startRow) {
						 pageItems.add(rowMapper.mapRow(rs, currentRow));
					 }
					 currentRow++;
				 }
				 return page;
			 }
		 });
		 return page;
	 }

	/**
	 * 分页查询
	 * @param page
	 * @param sql
	 * @param parameters
	 * @return
	 */
	public Page findListMap(final Page page,final String sql, final Object[] parameters) {
		if(page.isAutoCount()){//自动计数总数
			final long rowCount = originalJdbcTemplate.queryForLong("select count(*) from ("+SqlServerParse.removeOrderBy(sql)+")", parameters);
			page.setTotalCount(new Long(rowCount).intValue());
		} 
		//查出单页的记录数
		final int startRow = (page.getPageNum() - 1) * page.getPageSize();	
		final RowMapper rowMapper = new ColumnMapRowMapper();
		originalJdbcTemplate.query(sql, parameters, new ResultSetExtractor() {  
			@Override
			public Object extractData(ResultSet rs) throws SQLException,  DataAccessException {
				final List pageItems = page.getResult();  
				int currentRow = 0;  
				while (rs.next() && currentRow < startRow + page.getPageSize()) { 
					if (currentRow >= startRow) {  
						pageItems.add(rowMapper.mapRow(rs, currentRow)); 
					}
					currentRow++;  
				}
				return page;
			}
		}); 		
		return page;
	}

	/**
	 * 查询结果映射器，将查询结果自动拼装为一个javaBean
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("unchecked")
	protected ParameterizedBeanPropertyRowMapper resultBeanMapper(Class clazz) {
		return ParameterizedBeanPropertyRowMapper.newInstance(clazz);
	}
	
	/**
	 * 指定一个查询参数的实现类
	 * @param object
	 * @return
	 */
	protected BeanPropertySqlParameterSource paramBeanMapper(Object object) {
		return new BeanPropertySqlParameterSource(object);
	}
}
