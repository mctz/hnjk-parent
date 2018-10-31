package com.hnjk.edu.finance.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.service.IAnnualFeesService;

/**
 * 学生年度缴费标准服务.
 * <code>AnnualFeesServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @version 1.0
 */
@Transactional
@Service("annualFeesService")
public class AnnualFeesServiceImpl extends BaseServiceImpl<AnnualFees> implements IAnnualFeesService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 根据条件查询退费记录
	 */
	@Override
	@Transactional(readOnly=true)
	public List<AnnualFees> findAnnualFeesByCondition(Map<String, Object> condition) {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findAnnualFeesByConditionHql(condition, values);
		return findByHql(hql.toString(), values);
	}
	
	/**
	 * 根据条件查询退费记录HQL
	 * @param condition
	 * @return
	 */
	private StringBuffer findAnnualFeesByConditionHql(Map<String, Object> condition,Map<String, Object> param) {
		StringBuffer hql = new StringBuffer();
		hql.append("from "+AnnualFees.class.getSimpleName() +" af where af.isDeleted=:isDeleted ");
		param.put("isDeleted", 0);
		
		if(condition.containsKey("resourceid")){//勾选id
			hql.append(" and af.resourceid=:resourceid");
			param.put("resourceid", condition.get("resourceid"));
		}else{
			if(condition.containsKey("studentInfoId")){//勾选id
				hql.append(" and af.studentInfo.resourceid=:studentInfoId");
				param.put("studentInfoId", condition.get("studentInfoId"));
			}
			if(condition.containsKey("year")){//学年名
				hql.append(" and af.yearInfo.firstYear=:year");
				param.put("year", condition.get("year"));
			}	
			if(condition.containsKey("brSchool")){
				hql.append(" and af.studentInfo.branchSchool.resourceid=:brSchool");
				param.put("brSchool", condition.get("brSchool"));
			}
			if(condition.containsKey("yearId")){
				hql.append(" and af.yearInfo.resourceid=:yearId");
				param.put("yearId", condition.get("yearId"));
			}
			if(condition.containsKey("gradeid")){
				hql.append(" and af.studentInfo.grade.resourceid=:gradeid");
				param.put("gradeid", condition.get("gradeid"));
			}
			if(condition.containsKey("classicid")){
				hql.append(" and af.studentInfo.classic.resourceid=:classicid");
				param.put("classicid", condition.get("classicid"));
			}if(condition.containsKey("majorid")){
				hql.append(" and af.studentInfo.major.resourceid=:majorid");
				param.put("majorid", condition.get("majorid"));
			}if(condition.containsKey("classesId")){
				hql.append(" and af.studentInfo.Classes.resourceid=:classesId");
				param.put("classesId", condition.get("classesId"));
			}
			if(condition.containsKey("name")){
				hql.append(" and af.studentInfo.studentName=:name");
				param.put("name", condition.get("name"));
			}
			if(condition.containsKey("studyNo")){
				hql.append(" and af.studentInfo.studyNo=:studyNo");
				param.put("studyNo", condition.get("studyNo"));
			}
			if(condition.containsKey("chargeStatus")){//付款状态
				hql.append(" and af.chargeStatus=:chargeStatus");
				param.put("chargeStatus",Integer.parseInt((String)condition.get("chargeStatus")));
			}
			if(condition.containsKey("chargingItems")){
				hql.append(" and af.chargingItems=:chargingItems");
				param.put("chargingItems", condition.get("chargingItems"));
			}
		}
		if(condition.containsKey("studentStatus")){//学籍状态
			hql.append(" and af.studentInfo.studentStatus=:studentStatus");
			param.put("studentStatus",condition.get("studentStatus"));
		}
		hql.append(" order by af.studyNo,af.yearInfo.firstYear asc");
		return hql;
	}
	
	/*
	 * 根据条件获取学生年度缴费分页记录
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findAnnualFeesByCondition(Map<String, Object> condition, Page page) {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = findAnnualFeesByConditionHql(condition, values);

		return findByHql(page,hql.toString(), values);
	}

	/**
	 * 根据条件获取年度缴费信息
	 * @param studentInfoId
	 * @param yearInfoId
	 * @param chargingItems
	 * @return
	 */
	@Override
	public AnnualFees findUniqueByCondition(String studentInfoId, String yearInfoId, String chargingItems) {
		String annualFeeHql = "from AnnualFees where isDeleted=0 and studentInfo.resourceid=? and yearInfo.resourceid=? and chargingItems=?";
		return findUnique(annualFeeHql, studentInfoId,yearInfoId,chargingItems);
	}

}
