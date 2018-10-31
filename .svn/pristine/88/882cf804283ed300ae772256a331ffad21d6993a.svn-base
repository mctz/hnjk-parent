package com.hnjk.edu.basedata.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.NationMajor;
import com.hnjk.edu.basedata.service.INationMajorService;
/**
 * 国家专业代码库管理服务接口实现.
 * <code>NationMajorServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-15 下午02:49:32
 * @see 
 * @version 1.0
 */
@Service("nationMajorService")
@Transactional
public class NationMajorServiceImpl extends BaseServiceImpl<NationMajor> implements INationMajorService {
	
	/**
	 * 分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	public Page findNationMajorByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+NationMajor.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("nationMajorCode")){//专业编码
			hql += " and nationMajorCode=:nationMajorCode" ;
			values.put("nationMajorCode",condition.get("nationMajorCode"));
		}
		if (condition.containsKey("nationMajorType")) {//科类
			hql += " and nationMajorType=:nationMajorType" ;
			values.put("nationMajorType",condition.get("nationMajorType"));
		}
		if (condition.containsKey("classicid")) {//层次
			hql += " and classic.resourceid=:classicid" ;
			values.put("classicid",condition.get("classicid"));
		}
		if(condition.containsKey("nationMajorName")){//专业名称
			hql += " and lower(nationMajorName) like :nationMajorName ";
			values.put("nationMajorName", "%"+condition.get("nationMajorName").toString().toLowerCase()+"%");
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	/**
	 * 根据条件查询-返回LIST
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<NationMajor> findNationMajorByCondition(Map<String, Object> condition) throws ServiceException {
		
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+NationMajor.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		
		if(condition.containsKey("nationMajorCode")){//专业编码
			hql += " and nationMajorCode=:nationMajorCode" ;
			values.put("nationMajorCode",condition.get("nationMajorCode"));
		}
		if (condition.containsKey("nationMajorType")) {//科类
			hql += " and nationMajorType=:nationMajorType" ;
			values.put("nationMajorType",condition.get("nationMajorType"));
		}
		if (condition.containsKey("classicid")) {//层次
			hql += " and classic.resourceid=:classicid" ;
			values.put("classicid",condition.get("classicid"));
		}
		if(condition.containsKey("nationMajorName")){//专业名称
			hql += " and lower(nationMajorName) like :nationMajorName ";
			values.put("nationMajorName", "%"+condition.get("nationMajorName").toString().toLowerCase()+"%");
		}
		if (condition.containsKey("exceptid")) {//排除的ID
			hql += " and resourceid not in("+condition.get("exceptid")+")";
		}
		if (condition.containsKey("nationmajorParentCatolog")) {
			hql += " and parentCatalog=:nationmajorParentCatolog";
			values.put("nationmajorParentCatolog", condition.get("nationmajorParentCatolog"));
		}
		if (condition.containsKey("nationmajorChildCatolog")) {
			hql += " and childCatalog=:nationmajorChildCatolog";
			values.put("nationmajorChildCatolog", condition.get("nationmajorChildCatolog"));
		}
		return (List<NationMajor>) exGeneralHibernateDao.findByHql(hql.toString(),values);
	}

	@Override
	@Transactional(readOnly=true)
	public boolean isExistsNationMajorCode(String majorCode) throws ServiceException {
		List<NationMajor> list = findByHql(" from "+ NationMajor.class.getSimpleName()+" where isDeleted=0 and majorCode=? ", majorCode);
		return ((list != null && !list.isEmpty()) ? true : false);
	}
}
