package com.hnjk.edu.recruit.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitMajorSetting;
import com.hnjk.edu.recruit.service.IRecruitMajorSettingService;

/**
 * 招生专业设置Service实现
 * @author luof
 * @since： 2011-4-7 下午15:21:30
 * @see 
 * @version 1.0
 */
@Service("recruitMajorSettingService")
@Transactional
public class RecruitMajorSettingServiceImpl extends BaseServiceImpl<RecruitMajorSetting> implements IRecruitMajorSettingService{

	/**
	 * 根据条件查询招生专业设置表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<RecruitMajorSetting> findRecruitMajorSettingByCondition(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+RecruitMajorSetting.class.getSimpleName()+" setting where setting.isDeleted=:isDeleted");
		condition.put("isDeleted", 0);
		if (condition.containsKey("major")) {
			hql.append(" and setting.major.resourceid=:major");	
		}
		if (condition.containsKey("classic")) {
			hql.append(" and setting.classic.resourceid=:classic");	
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and setting.teachingType=:teachingType");	
		}
		
		return findByHql(hql.toString(),condition);
	}
	
	@Override
	public ExGeneralHibernateDao getGeneralHibernateDao(){
		return exGeneralHibernateDao;
	}

	@Override
	public List<RecruitMajorSetting> findRecruitMajorSettingListForAddMajor(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+RecruitMajorSetting.class.getSimpleName()+" setting where setting.isDeleted=:isDeleted");
		condition.put("isDeleted", 0);
		if (condition.containsKey("major")) {
			hql.append(" and setting.major.resourceid=:major");	
		}
		if (condition.containsKey("classic")) {
			hql.append(" and setting.classic.resourceid=:classic");	
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and setting.teachingType=:teachingType");	
		}
		
//		hql.append(" and setting.major.resourceid not in( select rm.major.resourceid from "+RecruitMajor.class.getSimpleName()+" rm where rm.isDeleted=:isDeleted ");
//		hql.append(" and rm.teachingType=:teachingType and  rm.classic.resourceid=:classic and rm.recruitPlan.resourceid=:recruitPlanId )");
		
		return findByHql(hql.toString(),condition);
	}
}
