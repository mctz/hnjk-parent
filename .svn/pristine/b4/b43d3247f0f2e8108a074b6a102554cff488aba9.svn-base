package com.hnjk.core.support.base.service;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JasperPrint;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;

/**
 * 基础服务接口定义. <p>
 * 这个接口定义了对DAO的数据交互.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-13下午03:44:04
 * @see 
 * @version 1.0
 */
public interface IBaseService<T> {

	/**
	 * 获取对象
	 * @param id 序列化之后的ID
	 * @return
	 * @throws ServiceException
	 */
	public abstract T get(Serializable id) throws ServiceException;
	
	public abstract Object get(Class<?> clazz,Serializable id) throws ServiceException;

	/**
	 * 载入对象
	 * @param id 序列化之后的ID
	 * @return
	 * @throws ServiceException
	 */
	public abstract T load(final Serializable id) throws ServiceException;

	public abstract Object load(Class clazz,final Serializable id) throws ServiceException;
	/**
	 * 根据hql查找唯一对象
	 * @param hql hql语句
	 * @param values hql属性
	 * @return 对象
	 * @ServiceException 
	 */
	public abstract T findUnique(String hql, Object... values) throws ServiceException;

	/**
	 * 根据属性查找唯一对象.
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return 对象.
	 * @throws ServiceException
	 */
	public abstract T findUniqueByProperty(String propertyName, Object value)
			throws ServiceException;

	/**
	 * 获取实体全部列表(loadAll方式).
	 * @return 返回实体列表.
	 */
	public abstract List<T> getAll() throws ServiceException;

	/**
	 * 获取实体全部列表(criteria方式)，推荐使用这种方式.
	 * @see IBaseService#findByCriteria
	 * @return 返回实体列表.
	 */
	public abstract List<T> getAllBycriteria() throws ServiceException;

	/**
	 * 获取实体全部分页列表.
	 * 使用criteria方式查找.	 * 
	 * @param page
	 * @see #findByCriteria
	 * @return 分页查询结果
	 */
	public abstract Page getAllBycriteria(Page page) throws ServiceException;

	/**
	 * SaveOrUpdate
	 * @param entity
	 */
	public abstract void saveOrUpdate(T entity) throws ServiceException;

	/**
	 * save
	 * @param entity
	 * @return  实体对象.
	 */	
	public abstract T save(T entity) throws ServiceException;

	/**
	 * update
	 * @param entity
	 */
	public abstract void update(T entity) throws ServiceException;

	/**
	 * merge
	 * @param entity
	 * @return 实体
	 */
	public abstract T merge(T entity) throws ServiceException;

	/**
	 * persist方式保存
	 * @param entity
	 */
	public abstract void persist(T entity) throws ServiceException;

	/**
	 * 根据对象删除
	 * @param entity
	 */
	public abstract void delete(T entity) throws ServiceException;

	/**
	 * 根据对象ID删除(逻辑)
	 * @param id
	 */
	public abstract void delete(Serializable id) throws ServiceException;

	/**
	 * 批量删除（逻辑）
	 * @param ids
	 * @throws Exception
	 */
	public abstract void batchDelete(final String[] ids) throws ServiceException;
	/**
	 * 批量删除（逻辑）
	 * @param list
	 * @throws ServiceException
	 */
	public abstract void batchDelete(final  List<T> list) throws ServiceException;
	
	/**
	 * 删除实体(物理)
	 * @param entity
	 * @throws ServiceException
	 */
	public abstract void truncate(Object entity) throws ServiceException;
	
	/**
	 * 根据对象属性物理删除
	 * @param clazz
	 * @param propertyName
	 * @param value
	 * @throws ServiceException
	 */
	public void truncateByProperty(Class<?> clazz, String propertyName, String value) throws ServiceException ;
	
	/**
	 * 批量删除实体（物理）
	 * @param clazz
	 * @param ids
	 * @throws ServiceException
	 */
	public abstract void batchTruncate(Class<?> clazz,final String[] ids) throws ServiceException;
	
	/**
	 * 批量更新保存
	 * @param list
	 * @throws ServiceException
	 */
	public abstract void batchSaveOrUpdate(final  List<T> list) throws ServiceException;
	
	/**
	 * flush
	 *
	 */
	public abstract void flush() throws ServiceException;

	/**
	 * 清空指定对象缓存
	 * @param entity
	 */
	public abstract void clearCache(T entity) throws ServiceException;

	/**
	 * 清空所有缓存
	 *
	 */
	public abstract void clearAllCache() throws ServiceException;

	/**
	 * 重新加载
	 * @param proxy
	 */
	public abstract void reloadLazy(Object proxy) throws ServiceException;

	/**
	 * 使用HQL方式查找对象列表.
	 * @param hql
	 * @param values
	 * @return
	 */
	public abstract List<T> findByHql(String hql, Object... values)	throws ServiceException;
	
	/**
	 * 使用HQL方式查找列表，
	 * @param clazz 目標CLASS
	 * @param hql
	 * @param values
	 * @return
	 * @throws ServiceException
	 */
	public List findByHql(Class clazz,String hql,Object... values) throws ServiceException;
	
	/**
	 * 使用HQL方式查找对象列表.
	 * @param hql
	 * @param values
	 * @return
	 */
	public abstract List<T> findByHql(String hql, Map<String, Object> values)		throws ServiceException;
		
	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数.
	 * @param hql hql语句.
	 * @param values 命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询时的参数.
	 */
	public Page findByHql(Page page, String hql, Map<String, Object> values) throws ServiceException;
	
	/**
	 * 使用Criteria方式查找. 
	 * @param criterion
	 * @return
	 */
	public abstract List<T> findByCriteria(Criterion... criterion)	throws ServiceException;

	/**
	 * 使用Criteria方式查找,可增加排序条件. 
	 * @param criterion
	 * @return
	 */
	public abstract List findByCriteria(Class<?> clazz,Criterion[] criterion,Order...order2)
			throws ServiceException;
	
	/**
	 * 使用Criteria方式查找,可增加排序条件和最大结果集. 
	 * @param criterion
	 * @return
	 */
	public abstract List findByCriteria(Class<?> clazz,Criterion[] criterion, Integer firstResult, Integer maxResults, Order...order2)
			throws ServiceException;
	
	/**
	 * 使用Criteria方式查找. 
	 * @param criterion
	 * @return
	 */
	public abstract List<T> findByCriteria(Criterion[] criterion, Integer firstResult, Integer maxResults, Order...order2)
			throws ServiceException;
	
	/**
	 * 根据实体ID集合查找对象实体对象集合.
	 * @param values 实体对象ID集合.
	 * @return
	 * @throws ServiceException
	 */
	//public abstract List<T> findEntitys(List values) throws ServiceException;

	/**
	 * 根据实体ID查找实体对象集合.
	 * @param clazz 实体类.
	 * @param values ID集合
	 * @return 实体对象集合.
	 * @throws ServiceException
	 */
	public abstract List findEntitysByIds(Class clazz,Collection<String> values) throws ServiceException;

	/**
	 * 按Criterion分页查询.
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 *             
	 * @param criterion 数量可变的Criterion.
	 * @param see {@link #countQueryResult(Page, Criteria)}
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public abstract Page findByCriteria(Page page, Criterion... criterion)
			throws ServiceException;
	
	/***
	 * @主要功能：按Criteria分页查询.
	 * @author: ctf 陈廷峰
	 * @since： Jun 2, 2009 
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 * @param c Criteria类型的查询条件
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 * @see {@link #countQueryResult}
	 * @throws ServiceException
	 */
	public abstract Page findByCriteriaSession(Page page, Criteria c) throws ServiceException;
	
	/**
	 * 按Criteria 别名分页查询，用于多表关联查询
	 * @param page 分页对象
	 * @param alias 别名
	 * @param criterion 
	 * @return 封装好的分页对象
	 * @throws ServiceException
	 */
	public abstract Page findByCriteriaWithAlias(Page page, String[] alias, Criterion... criterion) throws ServiceException;
	
	/**
	 * 获取数据源
	 * @return
	 */
	public abstract Connection getConn() throws SQLException;
	
	/**
	 * 使用jsperreport打印报表
	 * @param reportTemplateFile
	 * @param parameter
	 * @param conn
	 * @return jasperPrint
	 * @throws ServiceException
	 */
	public abstract JasperPrint printReport(String reportTemplateFile,Map<String, Object> parameter,Connection conn) throws ServiceException;
	
	/**
	 * 使用jsperreport打印报表
	 * @param reportTemplateFile
	 * @param parameter
	 * @param conn
	 * @param closed 手动调用关闭
	 * @return
	 * @throws ServiceException
	 */
	public abstract JasperPrint printReport(String reportTemplateFile,Map<String, Object> parameter,Connection conn,boolean closed ) throws ServiceException;

	/**
	 * 用来判断指定的实体是否存在.
	 * @param entityName 实体名,ex: com.hnjk.test.model.Test
	 * @return
	 */
	public boolean isExistEntity(String entityName);

	/**
	 * 根据查询条件获取HQL，并返回查询结果集
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<T> findByCondition(Map<String, Object> condition) throws ServiceException;

	/**
	 * 根据查询条件获取HQL，并返回查询结果集
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page findByCondition(Page page,Map<String, Object> condition) throws ServiceException;
}