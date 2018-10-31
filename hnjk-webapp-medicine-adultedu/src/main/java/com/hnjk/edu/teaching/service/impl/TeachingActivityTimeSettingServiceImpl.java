package com.hnjk.edu.teaching.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.service.ITeachingActivityTimeSettingService;

@Service("teachingActivityTimeSettingService")
@Transactional
public class TeachingActivityTimeSettingServiceImpl extends BaseServiceImpl<TeachingActivityTimeSetting> implements ITeachingActivityTimeSettingService {

	/**
	 * 根据条件查询教学活动环节中的时间设置-返回Page
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findTeachingActivityTimeSettingByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted", 0);
		
		StringBuilder hql 		 = new StringBuilder(" from "+TeachingActivityTimeSetting.class.getSimpleName()+" setting where setting.isDeleted = :isDeleted ");
		if (condition.containsKey("yearInfoId")) {
			hql.append(" and setting.yearInfo.resourceid=:yearInfoId");
			param.put("yearInfoId", condition.get("yearInfoId"));
		}
		if (condition.containsKey("term")){
			hql.append(" and setting.term=:term");
			param.put("term", condition.get("term"));
		}          
		if (condition.containsKey("mainProcessType")){
			hql.append(" and setting.mainProcessType=:mainProcessType");
			param.put("mainProcessType", condition.get("mainProcessType"));
		} 
		if (condition.containsKey("startTime")){
			hql.append(" and setting.startTime >=:startTime");
			param.put("startTime", condition.get("startTime"));
		} 				           
		if (condition.containsKey("endTime")) {
			hql.append(" and setting.endTime >=:endTime");
			param.put("endTime", condition.get("endTime"));
		}					       
		if (ExStringUtils.isNotBlank(page.getOrderBy())) {
			hql.append(" order by "+page.getOrderBy());
		}
		if (ExStringUtils.isNotBlank(page.getOrder())) {
			hql.append(" "+page.getOrder() );
		}
		return exGeneralHibernateDao.findByHql(page, hql.toString(), param);
	}

	/**
	 * 根据条件查询教学活动环节中的时间设置-返回List
	 * @param condition
	 * @return
	 */
	@Override
	public List<TeachingActivityTimeSetting> findTeachingActivityTimeSettingByCondition(Map<String, Object> condition)throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted", 0);
		StringBuilder hql 		 = new StringBuilder(" from "+TeachingActivityTimeSetting.class.getSimpleName()+" setting where setting.isDeleted = :isDeleted ");
		if(condition.containsKey("subProcessType")) {
			hql.append(" and setting.subProcessType = :subProcessType ");
			param.put("subProcessType", condition.get("subProcessType"));
		}
		if(condition.containsKey("mainProcessType")) {
			hql.append(" and setting.mainProcessType = :mainProcessType ");
			param.put("mainProcessType", condition.get("mainProcessType"));
		}
		if(condition.containsKey("yearInfo")) {
			hql.append(" and setting.yearInfo.isDeleted=0 and setting.yearInfo.resourceid = :yearInfo ");
			param.put("yearInfo", condition.get("yearInfo"));
		}
		if(condition.containsKey("fistYear")) {
			hql.append(" and setting.yearInfo.isDeleted=0 and setting.yearInfo.firstYear = :fistYear ");
			param.put("fistYear", condition.get("fistYear"));
		}
		if(condition.containsKey("term")) {
			hql.append(" and setting.term = :term ");
			param.put("term", condition.get("term"));
		}
		if(condition.containsKey("orderBy")) {
			hql.append(" order by ").append(condition.get("orderBy")).append(" ").append(ExStringUtils.isBlank(condition.get("order"))?"ASC":condition.get("order"));
		}
		return (List<TeachingActivityTimeSetting>) exGeneralHibernateDao.findByHql(hql.toString(), param);
	}
	
}
