package com.hnjk.edu.roll.service.impl;


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.roll.model.GraduateNograduateSetting;
import com.hnjk.edu.roll.service.IGraduateNoSettingService;
@Transactional
@Service("graduateNoSettingService")
public class GraduateNoSettingServiceImpl extends BaseServiceImpl<GraduateNograduateSetting> implements IGraduateNoSettingService {

	@Override
	public Page findGraduateByCondition(Map<String, Object> condition,
			Page objPage) {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+GraduateNograduateSetting.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("yearInfo")){
			hql += " and yearInfo.yearName = :yearInfo ";
			values.put("yearInfo", condition.get("yearInfo"));
		}
		if(condition.containsKey("term")){
			hql += " and term = :term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("startDate")){
			hql += " and startDate = to_date(:startDate,'yyyy-mm-dd') ";
			values.put("startDate", condition.get("startDate"));
		}
		if(condition.containsKey("endDate")){
			hql += " and endDate = to_date(:endDate,'yyyy-mm-dd') ";
			values.put("endDate", condition.get("endDate"));
		}
		if(condition.containsKey("revokeDate")){
			hql += " and revokeDate = to_date(:revokeDate,'yyyy-mm-dd') ";
			values.put("revokeDate", condition.get("revokeDate"));
		}
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}

	@Override
	public void batchCascadeDelete(String[] ids) {
		if(ids!=null && ids.length>0){
			for(String id : ids){
				delete(id);	
				logger.info("批量删除="+id);
			}
		}

	}

}
