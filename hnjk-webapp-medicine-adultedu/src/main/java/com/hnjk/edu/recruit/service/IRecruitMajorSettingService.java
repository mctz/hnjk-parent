package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.RecruitMajorSetting;

/**
 * 招生专业设置Service接口
 * @author luof
 * @since： 2011-4-7 下午15:21:30
 * @see 
 * @version 1.0
 */
public interface IRecruitMajorSettingService extends IBaseService<RecruitMajorSetting> {
	
	/**
	 * 根据条件查询招生专业设置表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<RecruitMajorSetting> findRecruitMajorSettingByCondition(Map<String,Object> condition) throws ServiceException;
	/**
	 * 特定招生专业时查询招生专业设置表，过滤同一层次、办学模式下已制定的专业
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<RecruitMajorSetting> findRecruitMajorSettingListForAddMajor(Map<String,Object> condition)throws ServiceException;
	
	public ExGeneralHibernateDao getGeneralHibernateDao();
}
