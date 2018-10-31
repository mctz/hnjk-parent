package com.hnjk.core.rao.dao.hibernate;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.impl.CriteriaImpl.OrderEntry;
import org.hibernate.transform.ResultTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;

import com.hnjk.core.annotation.DeleteChild;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.rao.dao.helper.GenericsUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.helper.QueryParameter;
import com.hnjk.core.support.base.model.BaseModel;
import com.hnjk.core.support.base.model.IBaseModel;

/**
 * 提供基础的hibernate DAO模板. <p>
 * 该模板继承并封装了spring2.5 <code>HibernateDaoSupport</code>.<p>
 * 具体使用方法请参见单元测试用例.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-2-26上午09:49:06
 * @see {@link #org.springframework.orm.hibernate3.support.HibernateDaoSupport()}
 * @version 1.0
 */
public class BaseHibernateTemplate extends HibernateDaoSupport {
	protected Logger logger = LoggerFactory.getLogger(getClass());

	public BaseHibernateTemplate(){}
	
	/**构造方法，赋值sessionFactory*/
	public BaseHibernateTemplate(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}
	
	

	/**
	 * 根据实体ID获取实体对象.
	 * @param clazz 需要获取的对象类型
	 * @param id 序列化后的ID
	 * @return	实体对象.
	 * @throws ServiceException
	 */
	public Object get(Class<?> clazz, Serializable id) throws ServiceException {		
		return getHibernateTemplate().get(clazz, id);
	}

	/**
	 * load对象
	 * @param clazz 对象类型
	 * @param id ID
	 * @return 实体对象
	 * @throws ServiceException
	 */
	public Object load(Class<?> clazz, final Serializable id) throws ServiceException {
		return getHibernateTemplate().load(clazz, id);
	}

	/**
	 * 根据hql语句查找唯一对象.
	 * @param hql hql语句
	 * @param values 参数可变，按顺序绑定
	 * @return 对象
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUnique(String hql, Object... values) throws ServiceException {
		Assert.hasText(hql, "hql不能为空");
		return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 根据hql语句查找唯一对象.
	 * @param <X>
	 * @param hql
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <X> X findUnique(final String hql, final Map<String, Object> values) {
		List<X> list = createQuery(hql, values).list();
		if(list.size()>0) {
			return list.get(0);
		}else {
			return null;
		}
		//return (X) createQuery(hql, values).uniqueResult();
	}

	/**
	 * 根据属性查找唯一对象.
	 * @param clazz 对象类型
	 * @param propertyName 属性名
	 * @param value 属性值
	 * @return 对象
	 * @throws ServiceException
	 */
	public Object findUniqueByProperty(Class<?> clazz, String propertyName, Object value) throws ServiceException {
		Assert.hasText(propertyName, "propertyName不能为空");
		return createCriteria(clazz, Restrictions.eq(propertyName, value),Restrictions.eq("isDeleted", 0)).uniqueResult();
	}

	/**
	 * 获取实体全部列表(loadAll方式).
	 * @param clazz 实体类型
	 * @return 返回实体列表.
	 * @throws ServiceException
	 */
	public List<?> getAll(Class<?> clazz) throws ServiceException {
		return getHibernateTemplate().loadAll(clazz);
	}

	/**
	 * 获取实体全部列表(criteria方式)，推荐使用这种方式.
	 * @param clazz 实体类型
	 * @see #findByCriteria(Class, Criterion[])
	 * @return 返回实体列表.
	 * @throws ServiceException
	 */
	public List<?> getAllBycriteria(Class<?> clazz) throws ServiceException {
		return findByCriteria(clazz);
	}

	/**
	 * 获取实体全部分页列表.
	 * 使用criteria方式查找.
	 * @param clazz 实体类型	
	 * @param page
	 * @see #findByCriteria(Class clazz,Page page, Criterion... criterion)
	 * @return 分页查询结果
	 * @throws ServiceException
	 */
	public Page getAllBycriteria(Class<?> clazz, Page page) throws ServiceException {
		return findByCriteria(clazz, page);
	}

	/**
	 * 保存或更新.
	 * @param entity
	 * @throws ServiceException
	 */
	public void saveOrUpdate(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().saveOrUpdate(entity);
		logger.debug("saveOrUpdate entity: {}", entity);
	}
	
	/**
	 * 批量保存或更新.
	 * @param entity
	 * @throws ServiceException
	 */
	
	public void saveOrUpdateCollection(Collection<? extends BaseModel> entities) throws ServiceException {
		Assert.notNull(entities, "entities不能为空");
		getHibernateTemplate().saveOrUpdateAll(entities);
		logger.debug("saveOrUpdate entitys: {}", entities);
	}

	/**
	 * 保存
	 * @param entity
	 * @return  实体对象.
	 * @throws ServiceException
	 */
	public Object save(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().save(entity);
		logger.debug("save entity: {}", entity);
		return entity;
	}

	/**
	 * 更新
	 * @param entity
	 * @throws ServiceException
	 */
	public void update(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().update(entity);
		logger.debug("update entity: {}", entity);
	}

	/**
	 * merge方式更新
	 * @param entity
	 * @return 实体
	 * @throws ServiceException
	 */
	public Object merge(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().merge(entity);
		logger.debug("merge entity: {}", entity);
		return entity;
	}

	/**
	 * persist方式保存
	 * @param entity
	 * @throws ServiceException
	 */
	public void persist(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().persist(entity);
		logger.debug("persist entity: {}", entity);
	}

	/**
	 * 物理删除实体
	 * @param entity
	 * @throws ServiceException
	 */
	public void truncate(Object entity) throws ServiceException{
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().delete(entity);
		logger.debug("delete entity:{}",entity);
	}
	
	/**
	 * 根据属性删除（物理）
	 * @param clazz
	 * @param propertyName
	 * @param value
	 * @throws ServiceException
	 */
	public void truncateByProperty(Class clazz, String propertyName, String value) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		hql.append("delete ").append(clazz.getName()).append(" where ").append(propertyName).append(" = ").append("'").append(value).append("'");
		this.createQuery(hql.toString()).executeUpdate();
	}
	
	/**
	 * 物理批量删除
	 * @param clazz
	 * @param ids
	 * @throws ServiceException
	 */
	public void batchTruncate(Class<?> clazz,final String[] ids) throws ServiceException{
		Assert.notNull(ids);
		final String queryStr = "delete "+clazz.getSimpleName()+" where resourceid in (:ids)";
		getHibernateTemplate().execute(new HibernateCallback() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(queryStr);
                query.setParameterList("ids", ids);
                return query.executeUpdate();
            }
        });
	}
	
	/**
	 * 删除（软删除）对象.
	 * @param entity
	 * @throws ServiceException
	 */
	public void delete(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");		
		try {
			final String resourceid =  (String)ExBeanUtils.getFieldValue(entity, "resourceid");
			final String queryStr = "update " + entity.getClass().getSimpleName() + " set isDeleted = 1 where resourceid = :id";
								
			getHibernateTemplate().execute(new HibernateCallback() {
				@Override
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					Query query = session.createQuery(queryStr);	
					query.setString("id",resourceid );
					//TODO 如果声明了关联检查，则执行检查回调
					
					return query.executeUpdate();
				}
			});
			//针对一对多的实体，如果有子表，则删除子表，
			//多对多的情况不做处理		
						
			List<Collection<IBaseModel>> childEntryList = getChildEntityName(entity);
			if(null != childEntryList && childEntryList.size()>0){
				for(Collection<IBaseModel> collection : childEntryList){//遍历多个子
					if(null != collection && collection.size()>0){//遍历子
						for(IBaseModel childEntry : collection){
							delete(childEntry.getClass(), childEntry.getResourceid());
						}
					}					
				}
			}
					
		} catch (Exception e) {		
			 logger.error("删除实体出错:"+e.fillInStackTrace());
			 throw new ServiceException("删除实体"+entity.getClass().getSimpleName()+"出错!");
		}	
		
		logger.debug("delete entity: {}", entity);
	}
	
	protected boolean doCheckEntityRelation(Object entity){
		return false;
	}
	
	/**
	 * 获取实体子类型
	 * @param collection
	 * @return map<String,Object> key -子实体类型,子数据
	 */
	@SuppressWarnings("unchecked")
	protected List<Collection<IBaseModel>> getChildEntityName(Object entity){
		List<Collection<IBaseModel>> childEntiryList = new ArrayList<Collection<IBaseModel>>();
		PropertyDescriptor[] oldPds = PropertyUtils .getPropertyDescriptors(entity); 
		try {
			 for (int i = 0; i < oldPds.length; i++) {  
				 PropertyDescriptor oldPd = oldPds[i]; 	
				 if(null == oldPd.getWriteMethod()) {
					 continue;
				 }
				if(!"class".equals(oldPd.getDisplayName())){//排除掉class类型的
					Object indate = PropertyUtils.getNestedProperty(entity,oldPd.getDisplayName());
					Field field = ExBeanUtils.getDeclaredField(entity.getClass(), oldPd.getDisplayName());
					if(null != indate && indate instanceof Collection<?>){
						//如果注解为不删除子表的忽略
						DeleteChild deleteChild = field.getAnnotation(DeleteChild.class);//获取不删除的注解
						if(null != deleteChild && !deleteChild.deleteable()){
							continue;
						}
						childEntiryList.add((Collection<IBaseModel>)indate);
						//childEntiryMap.put(GenericsUtils.getFieldGenericType(field).getName(), indate);						
						logger.debug("==>获取实体"+entity.getClass().getSimpleName()+"子类型："+GenericsUtils.getFieldGenericType(field).getName());
					}		
				
				}
			
			 }	
		} catch (Exception e) {
			throw new ServiceException("删除实体："+entity.getClass().getName()+" 子对象出错："+e.getMessage());
		}
		 
		return childEntiryList;
	}

	/**
	 * 批量删除对象(软删除)
	 * @param clazz	实例类型
	 * @param ids	对象ID集合
	 * @throws ServiceException 
	 */
	public void batchDelete(Class<?> clazz, final String[] ids) throws ServiceException {
		Assert.notNull(ids, "ids不能为空");
		for (int i = 0; i < ids.length; i++) {
			delete(clazz, ids[i]);
		}
		logger.debug("delete from entites:{}", ids);
	}
	/**
	 * 批量删除对象(软删除)
	 * @param list
	 * @throws ServiceException
	 */
	public void batchDelete(final List<?> list) throws ServiceException {
		Assert.notNull(list, "list不能为空");
		for (int i = 0; i < list.size(); i++) {
			delete(list.get(i));
		}
	}
	

	/**
	 * 批量保存或更新	
	 * @param list 需要更新或保存的实体列表.
	 * @throws ServiceException
	 */
	public void batchSaveOrUpdate(final List<? > list) throws ServiceException {
		Assert.notNull(list, "list不能为空");
		getHibernateTemplate().execute(new HibernateCallback() {
			@Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				int rows = 0;
				for (int i = 0; i < list.size(); i++) {
					session.saveOrUpdate(list.get(i));
					rows++;
					if (rows % 10000 == 0) {
						//将本批插入的对象立即写入数据库并释放内存
						session.flush();
						session.clear();
					}
				}
				logger.debug("save or update entites row:" + rows);
				return rows;
			}
		});
	}
	/**
	 * 执行传入的删除、修改HQL语句
	 * @param hql  HQL语句 
	 * @param map  hql语句中的参数
	 * @return
	 * @throws ServiceException
	 */
	public int executeHQL(final String hql, final Map<String,Object> param) throws ServiceException {		
		//TODO 更改为execute(callback)模式
		int rows = 0;
		/*		
		if(!param.isEmpty()){
			Session session 			  = getSession();
			Query query     			  = session.createQuery(hql);	
			Set<Entry<String,Object>> set = param.entrySet();
			for(Entry<String,Object> ent : set){
				if(ent.getValue() instanceof Collection){
					query.setParameterList(ent.getKey().toString(), (Collection)ent.getValue());				
				}else {
					query.setParameter(ent.getKey().toString(), ent.getValue());
				}
			}
			rows  =  query.executeUpdate();
		}
		*/
		Assert.notNull(hql,"hql不能为空");		
		Object object = getHibernateTemplate().execute(new HibernateCallback() {
            @Override
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery(hql);
                query.setProperties(param);
                return query.executeUpdate();
            }
        });
		rows = (Integer)object;
		return rows;
	}
	/**
	 * 根据对象ID删除
	 * @param clazz 实体类型
	 * @param id 实体ID
	 * @throws ServiceException
	 */
	public void delete(Class<?> clazz, Serializable id) throws ServiceException {
		Assert.notNull(id, "id不能为空");		
		delete(get(clazz, id));
		logger.debug("delete entity by id: {}", id);
	}

	/**
	 * flush 
	 * @throws ServiceException
	 */
	public void flush() throws ServiceException {
		getHibernateTemplate().flush();
	}

	/**
	 * 清空指定对象缓存
	 * @param entity
	 * @throws ServiceException
	 */
	public void clearCache(Object entity) throws ServiceException {
		Assert.notNull(entity, "entity不能为空");
		getHibernateTemplate().evict(entity);
		logger.debug("clear entity from cache:{}" + entity);
	}

	/**
	 * 清空所有缓存
	 * @throws ServiceException
	 */
	public void clearAllCache() throws ServiceException {
		getHibernateTemplate().clear();
		logger.debug("clear all cache...");
	}

	/**
	 * 重新初始化proxy实体.
	 * @param proxy
	 * @throws ServiceException
	 */
	public void reloadLazy(Object proxy) throws ServiceException {
		Assert.notNull(proxy, "proxy不能为空");
		getHibernateTemplate().initialize(proxy);
		logger.debug("initialize proxy: {}", proxy);
	}

	/**
	 * 使用HQL方式查找对象列表.
	 * @param hql HQL语句  
	 * @param values hql语句中的参数
	 * @return 实体列表.
	 * @throws ServiceException	 
	 */
	public List<?> findByHql(String hql, Object... values) throws ServiceException {
		return createQuery(hql, values).list();
	}
	
	/**
	 * 使用HQL方式查找对象列表.
	 * @param hql  HQL语句 
	 * @param map  hql语句中的参数
	 * @return
	 * @throws ServiceException
	 */	
	public List<?> findByHql(final String hql,final Map<String,Object> map) throws ServiceException {
		
		/*if(!map.isEmpty()){			
			Query queryObject = getSession().createQuery(hql);
			Set<Entry<String,Object>> set = map.entrySet();
			for(Entry<String,Object> ent : set){
				if(ent.getValue() instanceof Collection){
					queryObject.setParameterList(ent.getKey().toString(), (Collection)ent.getValue());				
				}else {
					queryObject.setParameter(ent.getKey().toString(), ent.getValue());
				}
			}
			return queryObject.list();			
		}
		*/
		//更改为execute(callback)模式
		if(!map.isEmpty()){
			return getHibernateTemplate().executeFind(new HibernateCallback() {				
				@Override
				public Object doInHibernate(Session session) throws HibernateException,	SQLException {
					Query queryObject = session.createQuery(hql);
					Set<Entry<String,Object>> set = map.entrySet();
					for(Entry<String,Object> ent : set){
						if(ent.getValue() instanceof Collection){
							queryObject.setParameterList(ent.getKey().toString(), (Collection)ent.getValue());				
						}else {
							queryObject.setParameter(ent.getKey().toString(), ent.getValue());
						}
					}
					return queryObject.list();
					
				}
			});
		}
		
		return Collections.EMPTY_LIST;
	}

	/**
	 * 按HQL分页查询.
	 * 
	 * @param page 分页参数.
	 * @param hql hql语句.
	 * @param values 命名参数,按名称绑定.
	 * 
	 * @return 分页查询结果, 附带结果列表及所有查询时的参数.
	 */
	public Page findByHql(Page page, String hql, Map<String, Object> values) throws ServiceException {
		Assert.notNull(page, "page不能为空");

		Query q = createQuery(hql, values);

		if (page.isAutoCount()) {
			long totalCount = countHqlResult(hql, values);
			page.setTotalCount(Long.valueOf(totalCount).intValue());
		}
		//设置分页参数
		setPageParameter(q, page);
		List result = q.list();
		if(page.getTotalPages()==1){
			if(page.getTotalCount()!=result.size()){
				page.setTotalCount(result.size());
			}
		}
		page.setResult(result);
		return page;
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数. 
	 * @param clazz 实体类型
	 * @param criterion Criterion条件.
	 * @return 实体列表 
	 * @throws ServiceException
	 */
	public List<?> findByCriteria(Class<?> clazz, Criterion... criterion) throws ServiceException {
		return createCriteria(clazz, criterion).list();
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数. 
	 * @notice 增加一个可选参数Order[]
	 * @param clazz 实体类型
	 * @param criterion Criteriont条件数组.
	 * @param order2 排序条件，可以为多个，自动绑定
	 * @return 实体列表
	 * @throws ServiceException
	 */
	public List<?> findByCriteria(Class<?> clazz, Criterion[] criterion, Integer firstResult, Integer maxResults,
			Order... order2) throws ServiceException {
		Criteria criteria = createCriteria(clazz, criterion);
		for (Order order : order2) {
			criteria.addOrder(order);
		}
		if (firstResult != null) {
			criteria.setMaxResults(firstResult);
		}
		if (maxResults != null) {
			criteria.setMaxResults(maxResults);
		}
		return criteria.list();
	}

	/***
	 * 按Criteria查询列表	，这个Criteria为自己封装好的Criterion条件.
	 * @author: Snoopy Chen (ctfzh@yahoo.com.cn)
	 * @since： Jun 9, 2009 
	 * @param c 查询条件
	 * @return 实体列表
	 * @throws ServiceException
	 */
	public List<?> findByCriteriaSession(Criteria c) throws ServiceException {
		return c.list();
	}

	/**
	 * 按Criterion分页查询.
	 * @param clazz 实体类型
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 *             
	 * @param criterion 数量可变的Criterion.
	 * @param see {@link #countQueryResult(Page, Criteria)}
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	public Page findByCriteria(Class<?> clazz, Page page, Criterion... criterion) throws ServiceException {
		Assert.notNull(page, "page不能为空");

		Criteria c = createCriteria(clazz, criterion);
		this.findByCriteriaCommon(page, c);
		return page;
	}

	/***
	 * 按Criteria分页查询.
	 * @author: ctf 陈廷峰
	 * @since： Jun 2, 2009 
	 * @param clazz 实体类型
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 * @param c 自己封装好的Criterion条件
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 * @see {@link #countQueryResult(Page, Criteria)}
	 * @throws ServiceException
	 */
	public Page findByCriteriaSession(Class<?> clazz, Page page, Criteria c) throws ServiceException {
		Assert.notNull(page, "page不能为空");
		if (page.isAutoCount()) {//如果允许自动统计,统计totalCount
			page.setTotalCount(countQueryResult(page, c));
		}
		if (page.isFirstSetted()) {
			c.setFirstResult(page.getFirst());//设置查询起始数
		}
		if (page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());//设置查询结束数
		}
		if (page.isOrderBySetted()) {//排序
			if (page.getOrder().equalsIgnoreCase(QueryParameter.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		}

		if (page.getTotalCount() > 0) {
			page.setResult(c.list());
		}
		return page;
	}

	/**
	 * 按Criteria 别名分页查询，用于多表关联查询
	 * @param clazz 实体类型
	 * @param page 分页对象
	 * @param alias 实体别名
	 * @param criterion 查询条件
	 * @return 实体分页列表
	 * @throws ServiceException
	 */
	public Page findByCriteriaWithAlias(Class<?> clazz, Page page, String[] alias, Criterion... criterion)
			throws ServiceException {
		Assert.notNull(page, "page不能为空");

		Criteria c = getSession().createCriteria(clazz);
		if (alias != null) {
			for (int i = 0; i < alias.length; i++) {
				String[] as = alias[i].split(":");
				if (as != null && as.length == 2) {
					c.createAlias(as[1], as[0]);
					logger.debug("creating alias:{},{}", as[1], as[0]);
				}
			}
		}
		for (Criterion cx : criterion) {
			c.add(cx);
		}

		if (page.isAutoCount()) {//如果允许自动统计,统计totalCount
			page.setTotalCount(countQueryResult(page, c));
		}
		if (page.isFirstSetted()) {
			c.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());
		}
		if (page.isOrderBySetted()) {
			if (page.getOrder().equalsIgnoreCase(QueryParameter.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		}

		if (page.getTotalCount() > 0) {
			page.setResult(c.list());
		}
		return page;
	}

	/**
	 * 根据ID集合查找实体列表
	 * @param clazz 实体类型
	 * @param values ID集合
	 * @return
	 * @throws ServiceException
	 */
	public List<?> findEntitysByIds(Class<?> clazz, Collection<String> values) throws ServiceException {
		Assert.notNull(values);
		//StringBuffer strbf = new StringBuffer();
		//for (Serializable pk : values) {
		//	if (pk instanceof String || pk instanceof Character) {
	//			strbf.append("'" + pk + "',");
		//	} else {
		//		strbf.append(pk + ",");
		//	}
		//}
		//String parms = strbf.substring(0, strbf.length() - 1).toString();
		//return findByHql("from " + clazz.getSimpleName() + " as entity where entity.resourceid in(" + parms
		//		+ ") and entity.isDeleted = 0");	
		//FIXED by hzg 改为传参方式.
		String hql = "from " + clazz.getSimpleName() + " as entity where entity.resourceid in(:ids) and entity.isDeleted = :isdeleted";
		Query queryObject = getSession().createQuery(hql);
		queryObject.setParameterList("ids", values);
		queryObject.setParameter("isdeleted", 0);
		return queryObject.list();
	}

	/*
	 * 根据实体对象ID集合，查找实体列表
	 * @param values
	 * @return
	 * @throws ServiceException
	 */
	/* MODIFY by hzg 禁用此方法.
	public List<?> findEntitys(Collection<? extends BaseModel> values) throws ServiceException {
		Assert.notNull(values);
		StringBuffer strbf = new StringBuffer();
		for (BaseModel entity : values) {
			strbf.append("'" + entity.getResourceid() + "',");
		}
		String parms = strbf.substring(0, strbf.length() - 1).toString();
		return findByHql("from " + getType(values.iterator().next()) + " as entity where entity.resourceid in(" + parms
				+ ") and entity.isDeleted = 0 ");
	}

	private static String getType(BaseModel be) {
		return be.getClass().getSimpleName();
	}
	*/

	/**
	 * 按Criterion分页查询,级联查询与非级联查询的公用部分.
	 * @param clazz 实体类型
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 *             
	 * @param criterion 数量可变的Criterion.
	 * @param see {@link #countQueryResult(Page, Criteria)}
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	private void findByCriteriaCommon(Page page, Criteria c) {
		if (page.isAutoCount()) {//如果允许自动统计,统计totalCount
			page.setTotalCount(countQueryResult(page, c));
		}
		if (page.isFirstSetted()) {
			c.setFirstResult(page.getFirst());
		}
		if (page.isPageSizeSetted()) {
			c.setMaxResults(page.getPageSize());
		}
		if (page.isOrderBySetted()) {
			if (page.getOrder().equalsIgnoreCase(QueryParameter.ASC)) {
				c.addOrder(Order.asc(page.getOrderBy()));
			} else {
				c.addOrder(Order.desc(page.getOrderBy()));
			}
		}

		if (page.getTotalCount() > 0) {
			page.setResult(c.list());
		}
	}

	/**
	 * 执行count查询获得本次Hql查询所能获得的对象总数.
	 * 
	 * 本函数只能自动处理简单的hql语句,复杂的hql查询请另行编写count语句查询.
	 */
	protected long countHqlResult(final String hql, final Map<String, Object> values) {
		String fromHql = hql;
			
		//select子句与order by子句会影响count查询,进行简单的排除.
		fromHql = "from " + StringUtils.substringAfter(fromHql, "from");
		fromHql = StringUtils.substringBefore(fromHql, "order by");
//		if(fromHql.indexOf("group by")>-1) fromHql = StringUtils.substringBefore(fromHql, "group by");
		if(fromHql.lastIndexOf("group by")>-1){
			String afterGroupBy = StringUtils.substringAfterLast(fromHql, "group by");
			/*if(afterGroupBy.indexOf(")") > -1){//<=-1 
				fromHql = StringUtils.substringBeforeLast(fromHql, "group by");
			}*/
		}

		String countHql = "select distinct count(*) " + fromHql ;

		try {
			Long count = findUnique(countHql, values);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("hql can't be auto count, hql is:" + countHql, e);
		}
	}

	/**
	 * 设置分页参数到Query对象,辅助函数.
	 */
	protected Query setPageParameter(final Query q, final Page page) {
		//hibernate的firstResult的序号从0开始		
		q.setFirstResult(page.getFirst());
		q.setMaxResults(page.getPageSize());
		return q;
	}

	/**
	 *  根据查询函数与参数列表创建Query对象,后续可进行更多处理,辅助函数.
	 * @param queryString
	 * @param values
	 * @return
	 */
	protected Query createQuery(String queryString, Object... values) throws ServiceException {
		Assert.hasText(queryString);
		Query queryObject = getSession().createQuery(queryString);
		if (null != values) {
			for (int i = 0; i < values.length; i++) {
				queryObject.setParameter(i, values[i]);
			}
		}
		return queryObject;
	}

	/**
	 * 根据查询HQL与参数列表创建Query对象.
	 * 
	 * @param values 命名参数,按名称绑定.
	 */
	protected Query createQuery(final String queryString, final Map<String, Object> values) {
		Assert.hasText(queryString, "queryString不能为空");
		Query query = getSession().createQuery(queryString);
		if (values != null) {
			query.setProperties(values);
		}	
		
		return query;
	}

	/**
	 * 根据Criterion条件创建Criteria,后续可进行更多处理,辅助函数.
	 * HQL运算符 QBC运算符 含义
		= Restrictions.eq() 等于
		<> Restrictions.not(Exprission.eq()) 不等于
		> Restrictions.gt() 大于
		>= Restrictions.ge() 大于等于
		< Restrictions.lt() 小于
		<= Restrictions.le() 小于等于
		is null Restrictions.isnull() 等于空值
		is not null Restrictions.isNotNull() 非空值
		like Restrictions.like() 字符串模式匹配
		and Restrictions.and() 逻辑与
		and Restrictions.conjunction() 逻辑与
		or Restrictions.or() 逻辑或
		or Restrictions.disjunction() 逻辑或
		not Restrictions.not() 逻辑非
		in(列表) Restrictions.in() 等于列表中的某一个值
		ont in(列表) Restrictions.not(Restrictions.in())不等于列表中任意一个值
		between x and y Restrictions.between() 闭区间xy中的任意值
		not between x and y Restrictions.not(Restrictions..between()) 小于值X或者大于值y
	 * @param clazz 对象类型
	 * @param criteria
	 */
	protected Criteria createCriteria(Class<?> clazz, Criterion... criterions) throws ServiceException {
		Criteria criteria = getSession().createCriteria(clazz);
		if (null != criterions && criterions.length > 0) {
			for (Criterion c : criterions) {
				criteria.add(c);
			}
		}
		return criteria;
	}

	/**
	 * 通过count查询获得本次查询所能获得的对象总数.
	 * @return page对象中的totalCount属性将赋值.
	 */
	@SuppressWarnings("unchecked")
	protected int countQueryResult(Page page, Criteria c) throws ServiceException {
		CriteriaImpl impl = (CriteriaImpl) c;

		// 先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List<OrderEntry>) ExBeanUtils.getFieldValue(impl, "orderEntries");
			ExBeanUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		// 执行Count查询
		int totalCount = (Integer) c.setProjection(Projections.rowCount()).uniqueResult();
		if (totalCount < 1) {
			return -1;
		}

		// 将之前的Projection和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}

		try {
			ExBeanUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}", e.getMessage());
		}

		return totalCount;
	}
}
