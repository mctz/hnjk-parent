package com.hnjk.security.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.IField;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 组织单位服务实现. <code>OrgUnitServiceImpl</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-4 上午09:58:47
 * @see 
 * @version 1.0
 */
@Service("orgUnitService")
@Transactional
public class OrgUnitServiceImpl extends BaseServiceImpl<OrgUnit> implements IOrgUnitService{
	
	@Autowired 
	@Qualifier("baseSupportJdbcDao")	
	private IBaseSupportJdbcDao jdbcDao;//注入jdbc template 支持

	//新增
	@Override
	public void addOrgUnit(OrgUnit orgUnit) throws ServiceException {
		exGeneralHibernateDao.save(orgUnit);
		//插入缓存
		addOrgUnitToCache(orgUnit);
	}

	//更新
	@Override
	public void updateOrgUnit(OrgUnit orgUnit) throws ServiceException {
		exGeneralHibernateDao.update(orgUnit);
		updateOrgUnitToCache(orgUnit);
	}

	/*
	@SuppressWarnings("unchecked")
	public void deleteAndUpdate(String resourceid,int size) throws ServiceException{
		OrgUnit orgunit = load(resourceid);
		
		Criterion[] criterions = new Criterion[1];
		criterions[0] = Restrictions.eq("parent.resourceid", orgunit.getParentId());
		List orgList = exGeneralHibernateDao.findByCriteria(OrgUnit.class, criterions);
		OrgUnit parentOrg = null;
		if(orgList.size() == size){
			parentOrg = load(orgunit.getParentId());
			parentOrg.setIsChild(Constants.BOOLEAN_NO);
			exGeneralHibernateDao.update(parentOrg);
		}
		
		String sql = "select * from hnjk_sys_unit t where t.isdeleted=0  start with t.resourceid=:id connect by prior t.resourceid=t.parentid";		
		try {
			Map<String,String> map = new HashMap<String, String>();
			map.put("id", resourceid);
			List<OrgUnit> list = jdbcDao.getBaseJdbcTemplate().findList(sql, OrgUnit.class, map);
			for(OrgUnit org : list){
				exGeneralHibernateDao.delete(OrgUnit.class, org.getResourceid());
				removeOrgUnitFromCache(org.getResourceid());
			}
		} catch (Exception e) {
			throw new ServiceException("删除组织出错:"+e.getMessage());
		}
	}
	*/
	
	@Override
	public void delete(Serializable id) throws ServiceException {		
		super.delete(id);		
		removeOrgUnitFromCache(id);
	}

	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if(ids.length>0){
			for(int index=0;index<ids.length;index++){
				delete(ids[index]);				
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)
	public List<OrgUnit> findOrgTree(String parentCode) throws Exception {
		String sql = "select * from hnjk_sys_unit t where t.isdeleted=0  start with ";
		if(ExStringUtils.isNotEmpty(parentCode)){
			sql += " t.unitCode = '"+parentCode+"' ";
		}else{
			sql += " t.parentid is null ";
		}
		 
		 sql += " connect by prior t.resourceid=t.parentid order siblings by t.unitLevel,t.showOrder,t.unitName";
		List<OrgUnit> list = jdbcDao.getBaseJdbcTemplate().findList(sql, OrgUnit.class, null);
		return list;
	}

	@Override
	@Transactional(readOnly=true)
	public Page findOrgByCondition(Map<String, Object> condition, Page page) throws ServiceException{
		List<Criterion> objCriterion = new ArrayList<Criterion>();
		if(condition.containsKey("unitName")){//组织单位名称
			objCriterion.add(Restrictions.like("unitName","%"+condition.get("unitName")+"%"));
		}
		if (condition.containsKey("status")) {//状态
			objCriterion.add(Restrictions.eq("status", condition.get("status")));
		}		
		if (condition.containsKey("unitId")) {//ID
			if(condition.containsKey("isChild") && "y".equalsIgnoreCase(condition.get("isChild").toString())){
				objCriterion.add(Restrictions.eq("resourceid", condition.get("unitId")));
			}else {
				objCriterion.add(Restrictions.eq("parent.resourceid", condition.get("unitId")));
			}			
		}
		if(condition.containsKey("unitType")){//组织类型
			objCriterion.add(Restrictions.eq("unitType", condition.get("unitType")));
		}
		if (condition.containsKey("isAllowNewMajor")) {//是否允许开新专业
			objCriterion.add(Restrictions.eq("isAllowNewMajor",condition.get("isAllowNewMajor")));
		}	
		if (condition.containsKey("unitCode")) {//学习中心编号
			objCriterion.add(Restrictions.eq("unitCode",condition.get("unitCode")));
		}
		if (condition.containsKey("schoolType")){//学习中心办学模式
			Criterion condition_tmp = Restrictions.or(
											Restrictions.or(
													Restrictions.like("schoolType", "%,"+condition.get("schoolType")+",%")
													,Restrictions.like("schoolType", condition.get("schoolType")+",%") )
											,Restrictions.or(
													Restrictions.like("schoolType", "%,"+condition.get("schoolType"))
													, Restrictions.eq("schoolType",condition.get("schoolType"))));
			objCriterion.add(condition_tmp);
		}
		
		objCriterion.add(Restrictions.eq("isDeleted", 0));//是否删除 =0
		return exGeneralHibernateDao.findByCriteria(OrgUnit.class,page,objCriterion.toArray(new Criterion[objCriterion.size()]));
	}
	//暂时用一种新的方法替代上面的方法，因为OrgUnit中有orgUnitExtend EAGER 导致加载unit时同时加载extend，又因为one2many，会有多条记录出现。
	@Override
	@Transactional(readOnly=true)
	public Page findOrgByConditionByHql(Map<String, Object> condition, Page page) throws ServiceException{
		StringBuffer hql = new StringBuffer();
		Map<String,Object> values =  new HashMap<String, Object>();
		hql.append(" from " + OrgUnit.class.getSimpleName() + " where 1=1 and isDeleted = :isDeleted " );
		values.put("isDeleted", 0);
		
		if(condition.containsKey("unitName")){//组织单位名称
			hql.append(" and unitName like :unitName ");
			values.put("unitName", "%"+condition.get("unitName")+"%");
		}
		if(condition.containsKey("unitname")){//组织单位名称
			hql.append(" and unitName = :unitname ");
			values.put("unitname", condition.get("unitname"));
		}
		if (condition.containsKey("status")) {//状态
			hql.append(" and status = :status ");
			values.put("status", condition.get("status"));
		}		
		if (condition.containsKey("unitId")) {//ID
			if(condition.containsKey("isChild") && "y".equalsIgnoreCase(condition.get("isChild").toString())){
				hql.append(" and resourceid = :unitId ");
				values.put("unitId", condition.get("unitId"));
			}else {
				hql.append(" and parent.resourceid = :unitId ");
				values.put("unitId", condition.get("unitId"));
			}			
		}
		if(condition.containsKey("isChild")){
			hql.append(" and isChild = :isChild ");
			values.put("isChild", condition.get("isChild"));
		}
		if(condition.containsKey("unitType")){//组织类型
			hql.append(" and unitType = :unitType ");
			values.put("unitType", condition.get("unitType"));
		}
		if (condition.containsKey("isAllowNewMajor")) {//是否允许开新专业
			hql.append(" and isAllowNewMajor = :isAllowNewMajor ");
			values.put("isAllowNewMajor", condition.get("isAllowNewMajor"));
		}	
		if (condition.containsKey("unitCode")) {//学习中心编号
			hql.append(" and unitCode = :unitCode ");
			values.put("unitCode", condition.get("unitCode"));
		}
		if (condition.containsKey("schoolType")){//学习中心办学模式
			String tmp = " and ((schoolType like :schoolType1 or schoolType like :schoolType2 ) or (schoolType like :schoolType3 or schoolType=:schoolType4 )) ";
			values.put("schoolType1", "%,"+condition.get("schoolType")+",%");
			values.put("schoolType2", condition.get("schoolType")+",%");
			values.put("schoolType3", "%,"+condition.get("schoolType"));
			values.put("schoolType4", condition.get("schoolType"));
		}
		if (condition.containsKey("payForm")) {// 收费形式
			hql.append(" and payForm = :payForm ");
			values.put("payForm", condition.get("payForm"));
		}
		if(condition.containsKey("isRecruit")){
			hql.append(" and (isRecruit = :isRecruit or isRecruit is null) ");
			values.put("isRecruit", condition.get("isRecruit"));
		}
		hql.append(" and unitName not in('未分配','转出学生')");
		if(ExStringUtils.isNotEmpty(page.getOrderBy())){
			hql.append(" order by "+page.getOrderBy()+" " +page.getOrder());
		}
		return exGeneralHibernateDao.findByHql(page, hql.toString(), values);  
	}

	/** 
	 * 通过 parentId 获取组织架构数据
	 * @param parentId			父节点id
	 * @param containSelf		数据中是否包含本身?
	 * @param containChildrenOrg数据中是否要进行递归查询?
	 * @return
	 * @throws ServiceException
	 */	
	@Override
	@Transactional(readOnly=true)
	public List<OrgUnit> findOrgByParentId(String parentId, boolean containSelf, boolean containChildrenOrg) throws ServiceException{		
		@SuppressWarnings("unchecked")
		List<OrgUnit> all = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		List<OrgUnit> result = getChildren(all, parentId, containChildrenOrg);
		if(containSelf) {
			result.add(getOrgUnitFromCache(parentId));
		}
		return result;
	}
	
	/**
	 * 从缓存中获取单位
	 * @param unitId
	 * @return
	 */
	public static OrgUnit getOrgUnitFromCache(String unitId) {		
		@SuppressWarnings("unchecked")
		List<OrgUnit> list = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		if(null != list){
			for(OrgUnit orgUnit :list){
				if(unitId.equals(orgUnit.getResourceid())){
					return orgUnit;
				}
			}
		}
		return null;
	}
	
	public static void addOrgUnitToCache(OrgUnit orgUnit){
		@SuppressWarnings("unchecked")
		List<OrgUnit> list = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		list.add(orgUnit);	
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).put(CacheSecManager.CACHE_SEC_ORGS, list);
	}
	
	public static void removeOrgUnitFromCache(Serializable orgUnitId){
		@SuppressWarnings("unchecked")
		List<OrgUnit> list = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		if(null != list){
			for(OrgUnit org : list){
				if(orgUnitId.equals(org.getResourceid())){
					list.remove(org);					
					break;
				}
			}
		}
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).put(CacheSecManager.CACHE_SEC_ORGS, list);
	}
	
	public static void updateOrgUnitToCache(OrgUnit orgUnit){
		@SuppressWarnings("unchecked")
		List<OrgUnit> list = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		if(null != list){
			for(OrgUnit org : list){
				if(orgUnit.getResourceid().equals(org.getResourceid())){
					list.remove(org);					
					break;
				}
			}
			list.add(orgUnit);
		}
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).put(CacheSecManager.CACHE_SEC_ORGS, list);
	}
	
	/**
	 * 通过传入 所有的组织对象集合 以及  parentId 获取其子节点
	 * @param all		所有组织对象集合
	 * @param parentId	父节点id
	 * @param lists		可选参数,装结果的集合
	 * @return
	 */
	@Transactional(readOnly=true)
	public static List<OrgUnit> getChildren(List<OrgUnit> all, String parentId, boolean containChildrenOrg, List<OrgUnit>...lists) throws ServiceException {
		List<OrgUnit> result = lists.length > 0 ? lists[0] : new ArrayList<OrgUnit>();
		if(!StringUtils.isEmpty(parentId)) {
			for(OrgUnit o : all) {
				if(o.getParentId() != null && parentId.equalsIgnoreCase(o.getParentId())) {
					result.add(o);
					if(containChildrenOrg && hasChildren(all, parentId)) {
						result.addAll(getChildren(all, parentId, containChildrenOrg, lists));
					}
				}
			}
		}
		return result;
	}
	
	@Transactional(readOnly=true)
	public static boolean hasChildren(List<OrgUnit> all, String parentId) throws ServiceException{
		if(!StringUtils.isEmpty(parentId)) {
			for(OrgUnit o : all) {
				if(o.getParentId() != null && parentId.equalsIgnoreCase(o.getParentId())) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<OrgUnit> findOrgUnitListByType(String unitType) throws ServiceException {
		Assert.hasText(unitType, "组织类型不能为空");		
		return findByHql("from "+OrgUnit.class.getSimpleName()+" where isDeleted = 0 and unitType = ? order by unitName ", unitType);
	}

	/**
	 * 根据教学点编码集合获取名称集合
	 * 
	 * @param codes
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String findNamesByCodes(String codes) throws ServiceException {
		String unitNames = "";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("codes", Arrays.asList(codes.split(",")));
			String sql = "select max(r) unitNames from (select wm_concat(u.unitname) over (order by u.unitcode) r  from hnjk_sys_unit u where u.unitcode in (:codes)) ";
			Map<String, Object> unitNamesMap = jdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(unitNamesMap!=null && unitNamesMap.size()>0){
				unitNames = (String)unitNamesMap.get("unitNames");
			}
		} catch (Exception e) {
			logger.error("根据教学点编码集合获取名称集合出错", e);
		}
		return unitNames;
	}
	
	/**
	 * 设置教学点收费形式
	 * @param payForm
	 * @param resourceids
	 * @throws Exception
	 */
	@Override
	public void setUnitPayForm(String payForm, String resourceids) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payForm", payForm);
		params.put("resourceids", Arrays.asList(resourceids.split(",")));
		StringBuffer sql = new StringBuffer("update hnjk_sys_unit u set u.payform=:payForm where u.resourceid in (:resourceids) ");
		jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql.toString(), params);
		// 更新缓存
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).removeall();
		List<OrgUnit> orgs = findByHql("from "+OrgUnit.class.getSimpleName()+" where isDeleted = ? order by unitName", 0);	
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).put(CacheSecManager.CACHE_SEC_ORGS, orgs);
	}

	/**
	 * 设置分成比例
	 * @param royaltyRate
	 * @param resourceids
	 * @throws Exception
	 */
	@Override
	public void setRoyaltyRate(BigDecimal royaltyRate,BigDecimal royaltyRate2,BigDecimal reserveRatio, String resourceids) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("royaltyRate", royaltyRate);
		params.put("royaltyRate2", royaltyRate2);
		params.put("reserveRatio", reserveRatio);
		params.put("resourceids", Arrays.asList(resourceids.split(",")));
		StringBuffer sql = new StringBuffer("update hnjk_sys_unit u set u.royaltyrate=:royaltyRate, u.royaltyrate2=:royaltyRate2, u.reserveRatio=:reserveRatio where u.resourceid in (:resourceids)");
		jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql.toString(), params);
		// 更新缓存
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).removeall();
		List<OrgUnit> orgs = findByHql("from "+OrgUnit.class.getSimpleName()+" where isDeleted = ? order by unitName", 0);	
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).put(CacheSecManager.CACHE_SEC_ORGS, orgs);
	}

	@Override
	public String constructOptions(Map<String, Object> condition,String defaultValue, int unique) throws ServiceException {
		StringBuffer unitOption = new StringBuffer();
		if(unique==1){
			OrgUnit orgUnit = get(defaultValue);
			unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");				
			unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
		}else {
			List<OrgUnit> orgList = findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitCode asc  ");
			if(null != orgList && orgList.size()>0){
				for(OrgUnit orgUnit : orgList){
					if(ExStringUtils.isNotEmpty(defaultValue)&&defaultValue.equals(orgUnit.getResourceid())){
						unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");				
						unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
					}else if(!"未分配".equals(orgUnit.getUnitName())){
						unitOption.append("<option value='"+orgUnit.getResourceid()+"'");				
						unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
					}
				}
			}
		}
		return unitOption.toString();
	}

	@Override
	public OrgUnit findOrgByUnitName(String unitNameStr) {
		OrgUnit unit;
		unit = findUniqueByProperty("unitName",unitNameStr.trim());
		if (unit == null) {
			unit = findUniqueByProperty("unitShortName",unitNameStr.trim());
		}
		return unit;
	}
}
