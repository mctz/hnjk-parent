package com.hnjk.edu.finance.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.finance.model.FeeMajor;
import com.hnjk.edu.finance.service.IFeeMajorService;
/**
 * 缴费类别服务.
 * <code>FeeMajorServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-8 下午03:32:03
 * @see 
 * @version 1.0
 */
@Transactional
@Service("feeMajorService")
public class FeeMajorServiceImpl extends BaseServiceImpl<FeeMajor> implements IFeeMajorService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly=true)
	public Page findFeeMajorByCondition(Map<String, Object> condition,	Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+FeeMajor.class.getSimpleName()+" where isDeleted = :isDeleted ";		
		values.put("isDeleted", 0);
		
		if(condition.containsKey("majorName")){
			hql += " and major.majorName like :majorName ";
			values.put("majorName", "%"+condition.get("majorName")+"%");
		}
		if(condition.containsKey("majorCode")){
			hql +=" and major.majorCode = :majorCode ";
			values.put("majorCode",condition.get("majorCode"));
		}
		if(objPage.isOrderBySetted()){
			hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		}		
		return findByHql(objPage, hql, values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<String> findFeeMajor() throws ServiceException {
		List<String>  majorIds = new ArrayList<String>();
		String sql = "select t.BASEMAJORID from EDU_FEE_MAJOR t where t.BASEMAJORID is null";
		Session session = exGeneralHibernateDao.getSessionFactory().openSession();
		
		 Query query=session.createSQLQuery(sql);
		
		   
		 majorIds=query.list();
		
		session.close();
		return majorIds;
	}

	/**
	 * 根据属性获取唯一实体
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public FeeMajor findByProperty(String propertyName, String propertyValue) throws ServiceException {
		return (FeeMajor)exGeneralHibernateDao.findUniqueByProperty(FeeMajor.class, propertyName, propertyValue);
	}

	/**
	 * 根据条件获取唯一的缴费专业实体
	 * @param majorId
	 * @param teachingType
	 * @return
	 */
	@Override
	public FeeMajor findUniqueByCondition(String majorId,String teachingType) {
		StringBuffer hql = new StringBuffer(500);
		hql.append("from "+FeeMajor.class.getSimpleName()+" where isDeleted = 0 ");
		hql.append(" and major.resourceid=? ");
		hql.append(" and teachingType=? ");
		
		return findUnique(hql.toString(), majorId,teachingType);
	}
	
	/**
	 * 批量物理删除
	 */
	@Override
	public void batchCascadeDelete(String[] ids) throws Exception{
		if(ids!=null && ids.length>0){
			List<String> idsList = Arrays.asList(ids);
			Map<String, Object> params = new HashMap<String, Object>(5);
			params.put("ids", idsList);
			String deleteSql = "delete from edu_fee_major where resourceid in (:ids)";
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(deleteSql, params);
		}
	}
	
}
