package com.hnjk.core.support.base.service;

import com.hnjk.core.beans.ColumnInfo;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.GenericsUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.hibernate.BaseSupportHibernateDao;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.metadata.ClassMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * 提供一个基本的业务（领域模型对象管理）实现模板抽象. <p>
 * 其他业务类继承此类，可以免去大部分代码.
 * eg.
 * public class LeaveServiceImpl extends BaseServiceImpl<Leave> implements ILeaveService
 * <p>  
 * 建议业务层都集成这个基类而不要直接继承<code>BaseSupportHibernateDao</code>，可以规范代码。
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-3-2上午11:00:31
 * @see {@link # BaseSupportHibernateDao()}
 * @version 1.0
 */
@SuppressWarnings("unchecked")
@Transactional
public class BaseServiceImpl<T> extends BaseSupportHibernateDao implements IBaseService<T> {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	//使用泛型获取实体类型
	protected Class<T> entityClass;

	protected Class<?> getEntityClass() {
		return entityClass;
	}

	@SuppressWarnings("unchecked")
	public BaseServiceImpl() {
		entityClass = GenericsUtils.getGenericClass(getClass());
		logger.debug("获取泛型实例类型：" + entityClass);
	}

	/* 
	 * 获取实体对象
	 */	
	@Override
	public T get(Serializable id) throws ServiceException {
		return (T) exGeneralHibernateDao.get(getEntityClass(), id);
	}
	
	@Override
	public Object get(Class clazz, Serializable id) throws ServiceException {
		return exGeneralHibernateDao.get(clazz, id);
	}
	
	/* 
	 * 加载实体对象
	 */
	@Override
	public T load(final Serializable id) throws ServiceException {
		return (T) exGeneralHibernateDao.load(getEntityClass(), id);
	}
	
	@Override
	public Object load(Class clazz, Serializable id) throws ServiceException {
		return exGeneralHibernateDao.load(clazz, id);
	}

	/* 
	 * 根据hql语句查找唯一对象
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findUnique(java.lang.String, java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public T findUnique(String hql, Object... values) throws ServiceException {
		return (T) exGeneralHibernateDao.findUnique(hql, values);
	}

	/*
	 * 根据对象属性查找唯一对象
	 *  (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findUniqueByProperty(java.lang.String, java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public T findUniqueByProperty(String propertyName, Object value) throws ServiceException {
		return (T) exGeneralHibernateDao.findUniqueByProperty(getEntityClass(), propertyName, value);
	}

	/* 
	 * 加载对象列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#getAll()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> getAll() throws ServiceException {
		return (List<T>) exGeneralHibernateDao.getAll(getEntityClass());
	}

	/*
	 * criteria方式获取列表
	 *  (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#getAllBycriteria()
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> getAllBycriteria() throws ServiceException {
		return (List<T>) exGeneralHibernateDao.getAllBycriteria(getEntityClass());
	}

	/* 
	 * 以Criteria方式获取全部分页列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#getAllBycriteria(com.gdcn.core.dao.support.Page)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page getAllBycriteria(Page page) throws ServiceException {
		return exGeneralHibernateDao.getAllBycriteria(getEntityClass(), page);
	}

	/*
	 * 保持或更新
	 *  (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#saveOrUpdate(T)
	 */
	@Override
	public void saveOrUpdate(T entity) throws ServiceException {
		exGeneralHibernateDao.saveOrUpdate(entity);
	}

	/*
	 * 保存
	 *  (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#save(T)
	 */
	@Override
	public T save(T entity) throws ServiceException {
		return (T) exGeneralHibernateDao.save(entity);
	}

	/* 
	 * 更新
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#update(T)
	 */
	@Override
	public void update(T entity) throws ServiceException {
		exGeneralHibernateDao.update(entity);
	}

	/* 
	 * merge方式更新
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#merge(T)
	 */
	@Override
	public T merge(T entity) throws ServiceException {
		return (T) exGeneralHibernateDao.merge(entity);
	}

	/* 
	 * persist方式更新
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#persist(T)
	 */
	@Override
	public void persist(T entity) throws ServiceException {
		exGeneralHibernateDao.persist(entity);
	}

	/* 
	 * 实体对象方式删除
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#delete(T)
	 */
	@Override
	public void delete(T entity) throws ServiceException {
		exGeneralHibernateDao.delete(entity);
	}

	/* 
	 * 实体ID方式删除
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#delete(java.io.Serializable)
	 */
	@Override
	public void delete(Serializable id) throws ServiceException {
		exGeneralHibernateDao.delete(getEntityClass(), id);
	}

	/*
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.IBaseService#batchDelete(java.lang.Class, java.lang.String[])
	 */
	@Override
	public void batchDelete(final String[] ids) throws ServiceException {
		exGeneralHibernateDao.batchDelete(getEntityClass(), ids);
	}
	/*
	 * (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.IBaseService#batchDelete(java.util.List)
	 */
	@Override
	public void batchDelete(List<T> list) throws ServiceException {
		exGeneralHibernateDao.batchDelete(list);
	}
	/*
	 * 批量删除（物理）
	 * (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.IBaseService#batchTruncate(java.lang.Class, java.lang.String[])
	 */
	@Override
	public void batchTruncate(Class<?> clazz, String[] ids) throws ServiceException {
		exGeneralHibernateDao.batchTruncate(clazz, ids);
	}

	/*
	 * 删除（物理）
	 * (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.IBaseService#truncate(java.lang.Object)
	 */
	@Override
	public void truncate(Object entity) throws ServiceException {
		exGeneralHibernateDao.truncate(entity);
	}

	
	@Override
	public void truncateByProperty(Class clazz, String propertyName, String value) throws ServiceException {
		exGeneralHibernateDao.truncateByProperty(clazz, propertyName, value);		
	}

	/*
	 *批量更新或插入 
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.IBaseService#batchSaveOrUpdate(java.util.List)
	 */
	@Override
	public void batchSaveOrUpdate(final List<T> list) throws ServiceException {
		exGeneralHibernateDao.batchSaveOrUpdate(list);
	}

	/*
	 * 刷新缓存
	 *  (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#flush()
	 */
	@Override
	public void flush() throws ServiceException {
		exGeneralHibernateDao.flush();
	}

	/*
	 * 清空制定实体对象缓存
	 *  (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#clearCache(T)
	 */
	@Override
	public void clearCache(T entity) throws ServiceException {
		exGeneralHibernateDao.clearCache(entity);
	}

	/* 
	 * 清空全部缓存
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#clearAllCache()
	 */
	@Override
	public void clearAllCache() throws ServiceException {
		exGeneralHibernateDao.clearAllCache();
	}

	/* 
	 * 重新加载
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#reloadLazy(java.lang.Object)
	 */
	@Override
	public void reloadLazy(Object proxy) throws ServiceException {
		exGeneralHibernateDao.reloadLazy(proxy);
	}

	/* 
	 * 根据HQL方式查找对象列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findByHql(java.lang.String, java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findByHql(String hql, Object... values) throws ServiceException {
		return (List<T>)exGeneralHibernateDao.findByHql(hql, values);
	}
	
		
	/* (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.IBaseService#findByHql(java.lang.Class, java.lang.String, java.lang.Object[])
	 */
	@Override
	@Transactional(readOnly = true)
	public List findByHql(Class clazz, String hql, Object... values)	throws ServiceException {				
		return exGeneralHibernateDao.findByHql(hql, values);
	}

	/* 
	 * 根据HQL方式查找对象列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findByHql(java.lang.String, java.lang.Object)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findByHql(String hql, Map<String, Object> values) throws ServiceException {
		return (List<T>)exGeneralHibernateDao.findByHql(hql, values);
	}

	/**
	 * 根据查询条件获取HQL，并返回查询结果集
	 * <p>condition中参数名称的格式必须为：grade.yearInfo.resourceid
	 * <p>范围查询：number[-start/begin,-end]，start<=number<=end
	 * <p>模糊查询：studentName[-like/likeLR,-likeL,-likeR]
	 * <p>[-inList/idList、-notInList、-null、-notNull、-not/notEqual
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values = new HashMap<String, Object>();
		//因为condition只是作为查询条件使用，并没有修改对象的值，所以使用假深复制
		values.putAll(condition);
		String hql = getHqlByCondition(getEntityClass(),values);
		logger.info("查询集合HQL："+hql);
		return (List<T>)exGeneralHibernateDao.findByHql(hql, values);
	}

	/*
	 * 根据HQL查找分页列表
	 * (non-Javadoc)
	 * @see com.hnjk.core.support.base.service.IBaseService#findByHql(com.hnjk.core.rao.dao.helper.Page, java.lang.String, java.util.Map)
	 */
	@Override
	public Page findByHql(Page page, String hql, Map<String, Object> values) throws ServiceException {
		return exGeneralHibernateDao.findByHql(page, hql, values);
	}

	/**
	 * 根据查询条件获取HQL，并返回查询结果集
	 * <p>condition中参数名称的格式必须为：grade.yearInfo.resourceid
	 * <p>范围查询：number[-start/begin,-end]，start<=number<=end
	 * <p>模糊查询：studentName[-like/likeLR,-likeL,-likeR]
	 * <p>[-inList/idList、-notInList、-null、-notNull、-not/notEqual
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly = true)
	public Page findByCondition(Page page, Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values = new HashMap<String, Object>();;
		//因为condition只是作为查询条件使用，并没有修改对象的值，所以使用假深复制
		values.putAll(condition);
		String hql = getHqlByCondition(getEntityClass(),values);
		logger.info("查询分页HQL："+hql);
		return exGeneralHibernateDao.findByHql(page,hql, values);
	}

	/**
	 * 获取Hql查询语句，并校验condition查询条件
	 * @param clazz
	 * @param condition
	 * @return
	 */
	private String getHqlByCondition(Class clazz, Map<String, Object> condition) {
		StringBuilder hqlBuilder = new StringBuilder(" from "+clazz.getSimpleName());
		//基本类型查询 - 如果查询条件的值是以下类型，则不进行类型匹配
		List<Class> baseType = new ArrayList<Class>();
		baseType.add(int.class);
		baseType.add(String.class);
		baseType.add(Integer.class);
		baseType.add(Long.class);
		baseType.add(long.class);
		baseType.add(Number.class);
		baseType.add(Double.class);
		baseType.add(Date.class);

		//实体类的所有属性，包括private、static、final等
		List<Field> fieldList = ExBeanUtils.getDeclaredFieldList(clazz);

		//数据库中的字段，即实体类含有Column、JoinColumn、Version等注解，由fieldList过滤所得
		List<ColumnInfo> columnInfoList = new ArrayList<ColumnInfo>();

		//默认添加未删除查询条件
		if (!condition.containsKey("isDeleted")) {
			condition.put("isDeleted", 0);
		}

		//获取属性名和属性值，其中key为参数名(过滤非数据库字段 及 非查询字段)
		for (Map.Entry entry : condition.entrySet()) {
			//属性名称
			String inputName = entry.getKey().toString();
			//删除横杠（范围查询，-start,-begin,-end,-like,-idList）
			String propertyNameForHql = ExStringUtils.removeEndString(inputName,"-");

			//判断查询条件key是否存在,不存在则跳过
			Field currentField = ExBeanUtils.hasDeclaredField(clazz,propertyNameForHql);
			if(currentField == null){
				continue;
			}
			//去掉.属性
			String propertyName = ExStringUtils.substringByRegex(propertyNameForHql, ".", true);

			for (Field field : fieldList) {
				List<Annotation> annotationList = Arrays.asList(field.getAnnotations());
				//必须要有字段类型的注解(即非事务字段)，属性名称相同，属性类型必须相匹配（都为基本类型）或特殊情况（<resourceid-inList,List<String>>）
				if (!ExBeanUtils.isContainsTransient(annotationList)) {

					if (field.getName().equals(propertyName) &&
							(currentField.getType().equals(entry.getValue().getClass()) || (ExBeanUtils.isContainsObj(baseType,currentField.getType()) && ExBeanUtils.isContainsObj(baseType,entry.getValue().getClass()))
									|| ExStringUtils.endsWithIgnoreCase(entry.getKey().toString(),"List") && entry.getKey().toString().contains("-") && "ArrayList".equals(entry.getValue().getClass().getSimpleName()))) {
						ColumnInfo columnInfo = new ColumnInfo();
						columnInfo.setInputName(inputName);
						columnInfo.setPropertyName(field.getName());
						columnInfo.setPropertyType(currentField.getType());
						columnInfo.setPropertyNameForHql(propertyNameForHql);
						columnInfo.setParamKey(propertyNameForHql.replace(".","_"));
						columnInfo.setParamValue(entry.getValue());
						columnInfo.setColumnName(ExBeanUtils.getValueByAnnotationName(field));
						columnInfo.setAnnotationList(annotationList);
						columnInfoList.add(columnInfo);
						break;
					}
				}
			}
		}

		//获取HQL查询语句[hqlBuilder] 及 Map查询条件[condition]
		boolean hasWhere = false;
		Map<String,Object> values = new HashMap<String, Object>();
		try {
			for (ColumnInfo columnInfo:columnInfoList ) {
				if (hasWhere) {
					hqlBuilder.append(" and ");
				} else {
					hasWhere = true;
					hqlBuilder.append(" where ");
				}
				String inputName = columnInfo.getInputName();
				String operate = " = :";
				String operateEnd = "";
				String keyParam = columnInfo.getParamKey();
				Object valueParam = columnInfo.getParamValue();
				//根据字段类型对参数值的类型进行转换
				if (ExStringUtils.endsWithIgnoreCase(inputName,"List") && "ArrayList".equals(valueParam.getClass().getSimpleName())) {
					//List类型不做处理
				} else if (Date.class.equals(columnInfo.getPropertyType())) {
					//Date类型使用String拼接
					if (valueParam.getClass().equals(Date.class)) {
						valueParam = "to_date('" + ExDateUtils.formatDateStr((Date) valueParam, 6) + "','yyyy-MM-dd HH24:mi:ss')";
					} else {
						valueParam = "to_date('" + valueParam + "','yyyy-MM-dd HH24:mi:ss')";
					}
				} else if (Double.class.equals(columnInfo.getPropertyType())) {
					valueParam = ExNumberUtils.toDouble(valueParam.toString());
				} else if (Long.class.equals(columnInfo.getPropertyType())){
					valueParam = Long.parseLong(valueParam.toString());
				} else if(Integer.class.equals(columnInfo.getPropertyType()) || int.class.equals(columnInfo.getPropertyType())) {
					valueParam = Integer.parseInt(valueParam.toString());
				} else if (String.class.equals(columnInfo.getPropertyType())) {
					valueParam = valueParam.toString();
				}
				//处理范围查询条件
				if (ExStringUtils.endsWithIgnoreCase(inputName, "-begin") || ExStringUtils.endsWithIgnoreCase(inputName, "-start")) {
					operate = operate.replace("=", ">=");
					if (Date.class.equals(columnInfo.getPropertyType())) {
						operate = " >= ";
						hqlBuilder.append(columnInfo.getPropertyNameForHql()).append(operate).append(valueParam);
						continue;
					}
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-end")) {
					operate = operate.replace("=", "<=");
					if (Date.class.equals(columnInfo.getPropertyType())) {
						operate = " <= ";
						hqlBuilder.append(columnInfo.getPropertyNameForHql()).append(operate).append(valueParam);
						continue;
					}
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-likeL")) {
					operate = operate.replace("=", "like");
					valueParam = "%" + valueParam;
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-likeR")) {
					operate = operate.replace("=", "like");
					valueParam = valueParam + "%";
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-like") || ExStringUtils.endsWithIgnoreCase(inputName, "-likeLR")) {
					operate = operate.replace("=", "like");
					valueParam = "%" + valueParam + "%";
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-idList") || ExStringUtils.endsWithIgnoreCase(inputName, "-inList")) {
					operate = operate.replace("=", "in (");
					keyParam += "_inList";
					operateEnd = ")";
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-notInList")) {
					operate = operate.replace("=", "not in (");
					keyParam += "_notInList";
					operateEnd = ")";
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-notEqual") || ExStringUtils.endsWithIgnoreCase(inputName, "-not")) {
					operate = operate.replace("=", "!=");
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-notNull")) {
					hqlBuilder.append(columnInfo.getPropertyNameForHql()).append(" is not null");
					continue;
				} else if (ExStringUtils.endsWithIgnoreCase(inputName, "-null")) {
					hqlBuilder.append(columnInfo.getPropertyNameForHql()).append(" is null");
					continue;
				}
				values.put(keyParam, valueParam);
				hqlBuilder.append(columnInfo.getPropertyNameForHql()).append(operate).append(keyParam).append(operateEnd);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (condition.containsKey("orderBy") || condition.containsKey("orderby") || condition.containsKey("ORDERBY")) {
			hqlBuilder.append(" order by ").append(condition.get("orderBy").toString());
		}
		condition.clear();
		condition.putAll(values);
		return hqlBuilder.toString();
	}

	/**
	 * 根据查询条件获取SQL，并返回查询结果集
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<T> findByConditionForSQL(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values = new HashMap<String, Object>();
		//因为condition只是作为查询条件使用，并没有修改对象的值，所以使用假深复制
		values.putAll(condition);
		//List<Class> entityList = new ArrayList<Class>();
		List<ColumnInfo> columnInfoList = new ArrayList<ColumnInfo>();
		String sql = getSqlByCondition(getEntityClass(),values,columnInfoList);
		logger.info("查询集合SQL："+sql);
		SQLQuery query = exGeneralHibernateDao.getSessionFactory().getCurrentSession().createSQLQuery(sql);
		for (ColumnInfo columnInfo : columnInfoList) {
			query.addEntity(columnInfo.getPropertyName(),columnInfo.getPropertyType().getClass());
		}
		//填充参数
		for(String key : condition.keySet()){
			query.setParameter(key, condition.get(key));
		}
		return query.list();
	}

	//String sql = "select {c.*}, {s.*} from Clazz c , student s where s.class_id = c.id   ";
	//List<Clazz> clazzs = (List<Clazz>) session().createSQLQuery(sql).addEntity("c", Clazz.class).addEntity("s", Student.class).list();
	private String getSqlByCondition(Class clazz, Map<String, Object> condition,List<ColumnInfo> columnInfoList) {
		StringBuilder sqlBuilder = new StringBuilder();
		Map<String,Object> values = new HashMap<String, Object>();

		//实体类的所有属性，包括private、static、final等
		List<Field> fieldList = ExBeanUtils.getDeclaredFieldList(clazz);
		Table annotation = (Table) clazz.getAnnotation(Table.class);

		//基本类型查询 - 如果查询条件的值是以下类型，则不进行类型匹配
		List<Class> baseType = new ArrayList<Class>();
		baseType.add(int.class);
		baseType.add(String.class);
		baseType.add(Integer.class);
		baseType.add(Long.class);
		baseType.add(long.class);
		baseType.add(Number.class);
		baseType.add(Double.class);
		baseType.add(Date.class);

		StringBuilder selectBuilder = new StringBuilder("select {").append(clazz.getSimpleName().toUpperCase()).append(".*}");
		StringBuilder tableBuilder = new StringBuilder(" from ").append(annotation.name()+" ").append(clazz.getSimpleName().toUpperCase());
		StringBuilder whereBuilder = new StringBuilder(" where ").append(clazz.getSimpleName().toUpperCase()).append(".isdeleted=0");

		ColumnInfo columnInfo = new ColumnInfo();
		columnInfo.setPropertyType(clazz);
		columnInfo.setPropertyName(clazz.getSimpleName().toUpperCase());
		columnInfoList.add(columnInfo);
		//分两类处理1、@JoinColumn   2、其它
		List<Annotation> annotationList;
		for (Field field : fieldList) {
			if (!ExBeanUtils.isContainsTransient(Arrays.asList(field.getAnnotations())) && !baseType.contains(field.getClass())) {
				annotationList = Arrays.asList(field.getDeclaredAnnotations());
				String propertyName = field.getName();
				columnInfo = new ColumnInfo();
				columnInfo.setPropertyName(propertyName);
				columnInfo.setPropertyType(field.getType());
				String columnName = "";
				if (ExBeanUtils.isContainsAnnotation(annotationList,JoinColumn.class)) {
					String columnTableName = field.getType().getAnnotation(Table.class).name();
					columnInfo.setPropertyTableName(columnTableName);
					columnName = field.getAnnotation(JoinColumn.class).name();
				} else {
					continue;
				}
				columnInfo.setColumnName(columnName);
				columnInfoList.add(columnInfo);
			}
		}

		for (ColumnInfo columnInfo_temp : columnInfoList) {
			selectBuilder.append(",{").append(columnInfo_temp.getPropertyName()).append(".*}");
			tableBuilder.append(","+columnInfo_temp.getPropertyTableName()+" ").append(columnInfo_temp.getPropertyName().toUpperCase());
			whereBuilder.append(" and ").append(clazz.getSimpleName().toUpperCase()+"."+columnInfo_temp.getColumnName()+"=").append(columnInfo_temp.getPropertyName().toUpperCase()+".RESOURCEID");
		}
		condition.clear();
		condition.putAll(values);
		sqlBuilder.append(selectBuilder.toString()).append(tableBuilder.toString()).append(whereBuilder.toString());

		return sqlBuilder.toString();
	}

	/* 
	 * 根据Criteria方式查找对象列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findByCriteria(org.hibernate.criterion.Criterion)
	 */
	@Override
	@Transactional(readOnly = true)
	public List<T> findByCriteria(Criterion... criterion) throws ServiceException {
		return (List<T>)exGeneralHibernateDao.findByCriteria(getEntityClass(), criterion);
	}

	/* 
	 * 根据Criteria方式查找对象列表,可增加排序条件
	 * (non-Javadoc)
	 */
	@Override
	@Transactional(readOnly = true)
	public List findByCriteria(Class<?> clazz, Criterion[] criterion, Order... order2) throws ServiceException {
		return exGeneralHibernateDao.findByCriteria(clazz, criterion, null, null, order2);
	}

	/* 
	 * 根据Criteria方式查找对象列表,可增加排序条件和最大结果集
	 * (non-Javadoc)
	 */
	@Override
	@Transactional(readOnly = true)
	public List findByCriteria(Class<?> clazz, Criterion[] criterion, Integer firstResult, Integer maxResults,
			Order... order2) throws ServiceException {
		return exGeneralHibernateDao.findByCriteria(clazz, criterion, firstResult, maxResults, order2);
	}

	@Override
	public List<T> findByCriteria(Criterion[] criterion, Integer firstResult, Integer maxResults, Order... order2)
			throws ServiceException {
		return (List<T>)exGeneralHibernateDao.findByCriteria(getEntityClass(), criterion, firstResult, maxResults, order2);
	}

	/* 
	 * 根据对象属性列表查找对象列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findEntitys(java.util.List)
	 */
	/*
	@Transactional(readOnly = true)
	public List<T> findEntitys(List values) throws ServiceException {
		return (List<T>)exGeneralHibernateDao.findEntitys(values);
	}
	*/

	/* 
	 * 根据对象属性集合查找对象列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findEntitysByIds(java.lang.Class, java.util.Collection)
	 */
	@Override
	@Transactional(readOnly = true)
	public List findEntitysByIds(Class clazz, Collection<String> values) throws ServiceException {
		return exGeneralHibernateDao.findEntitysByIds(clazz, values);
	}

	/* 
	 * 根据Criteria方式查找对象分页列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.impl.IBaseService#findByCriteria(com.gdcn.core.dao.support.Page, org.hibernate.criterion.Criterion)
	 */
	@Override
	@Transactional(readOnly = true)
	public Page findByCriteria(Page page, Criterion... criterion) throws ServiceException {
		return exGeneralHibernateDao.findByCriteria(getEntityClass(), page, criterion);
	}

	/*
	 * 根据Criteria别名查找对象分页列表
	 * (non-Javadoc)
	 * @see com.gdcn.core.service.IBaseService#findByCriteriaWithAlias(com.gdcn.core.dao.support.Page, java.lang.String[], org.hibernate.criterion.Criterion[])
	 */
	@Override
	@Transactional(readOnly = true)
	public Page findByCriteriaWithAlias(Page page, String[] alias, Criterion... criterion) throws ServiceException {
		return exGeneralHibernateDao.findByCriteriaWithAlias(getEntityClass(), page, alias, criterion);
	}

	/***
	 * @主要功能：按Criteria分页查询.
	 * @author: ctf 陈廷峰
	 * @since： Jun 2, 2009 
	 * @param page 分页参数.包括pageSize、firstResult、orderBy、asc、autoCount.
	 *             其中firstResult可直接指定,也可以指定pageNo.
	 *             autoCount指定是否动态获取总结果数.
	 * @param c Criteria类型的查询条件
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 * @see IBaseService#findByCriteriaSession
	 * @throws ServiceException
	 */
	@Override
	@Transactional(readOnly = true)
	public Page findByCriteriaSession(Page page, Criteria c) throws ServiceException {
		return exGeneralHibernateDao.findByCriteriaSession(getEntityClass(), page, c);
	}

	/***
	 * 
	 * @主要功能：按Criteria查询列表
	 * @author: Snoopy Chen (ctfzh@yahoo.com.cn)
	 * @since： Jun 9, 2009 
	 * @param c Criteria类型的查询条件
	 * @return
	 * @throws ServiceException
	 */
	@Transactional(readOnly = true)
	public List<T> findByCriteriaSession(Criteria c) throws ServiceException {
		return (List<T>)exGeneralHibernateDao.findByCriteriaSession(c);
	}

	@Override
	public Connection getConn() throws SQLException {		
		return SessionFactoryUtils.getDataSource(exGeneralHibernateDao.getSessionFactory()).getConnection();	
	}

	@Override
	public JasperPrint printReport(String reportTemplateFile,	Map<String, Object> parameter, Connection conn)	throws ServiceException {
		return printReport(reportTemplateFile, parameter, conn, true);		
	}

	@Override
	public JasperPrint printReport(String reportTemplateFile,Map<String, Object> parameter, Connection conn, final boolean closed)throws ServiceException {
		try {
			return JasperFillManager.fillReport(reportTemplateFile, parameter, conn);
		} catch (JRException e) {
			throw new ServiceException("打印报表出错："+e.fillInStackTrace());
		}finally{
			if(null != conn) {
				try {
					if(closed) {
						conn.close();
					}
				} catch (SQLException e) {
					//忽略
				}
			}
		} 
		
		
	}
	
	@Override
	public boolean isExistEntity(String entityName){
		ClassMetadata classMetadata = exGeneralHibernateDao.getSessionFactory().getClassMetadata(entityName);		
		return null != classMetadata;
	}
	
}
