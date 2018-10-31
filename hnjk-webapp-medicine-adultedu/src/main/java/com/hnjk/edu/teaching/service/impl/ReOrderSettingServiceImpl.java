package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.ReOrderSetting;
import com.hnjk.edu.teaching.service.IReOrderSettingService;


/**
 * 
 * <code>补预约管理Service实现</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-11-23 上午10:26:45
 * @see 
 * @version 1.0
 */
@Service("reOrderSettingService")
@Transactional
public class ReOrderSettingServiceImpl extends BaseServiceImpl<ReOrderSetting> implements IReOrderSettingService{


	/**
	 * 根据条件查询补预约管理记录，返回LIST
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ReOrderSetting> findReOrderSettingByCondition(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql  		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		hql.append(" from "+ReOrderSetting.class.getSimpleName()+" setting where setting.isDeleted=:isDeleted");
		if (condition.containsKey("reOrderType")) {
			hql.append(" and setting.reOrderType=:reOrderType");
			param.put("reOrderType", condition.get("reOrderType"));
		}
		if (condition.containsKey("orderCourseSetting")) {
			hql.append(" and setting.orderCourseSetting.resourceid=:orderCourseSetting");
			param.put("orderCourseSetting", condition.get("orderCourseSetting"));
		}
		if (condition.containsKey("examSubId")) {
			hql.append(" and setting.examSub.resourceid=:examSubId");
			param.put("examSubId", condition.get("examSubId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and setting.brSchool.resourceid=:branchSchool");
			param.put("branchSchool", condition.get("branchSchool"));
		}

		hql.append(" order by setting.endTime desc");
		param.put("isDeleted", 0);
		
		return (List<ReOrderSetting>) exGeneralHibernateDao.findByHql(hql.toString(), param);
	}
	/**
	 * 根据条件查询补预约管理记录,返回PAGE
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findReOrderSettingByCondition(Map<String, Object> condition,Page page) throws ServiceException {
		StringBuffer hql  		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		hql.append(" from "+ReOrderSetting.class.getSimpleName()+" setting where setting.isDeleted=:isDeleted");
		if (condition.containsKey("reOrderType")) {
			hql.append(" and setting.reOrderType=:reOrderType");
			param.put("reOrderType", condition.get("reOrderType"));
		}
		if (condition.containsKey("orderCourseSetting")) {
			hql.append(" and setting.orderCourseSetting.resourceid=:orderCourseSetting");
			param.put("orderCourseSetting", condition.get("orderCourseSetting"));
		}
		if (condition.containsKey("examSub")) {
			hql.append(" and setting.examSub.resourceid=:examSub");
			param.put("examSub", condition.get("examSub"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and setting.brSchool.resourceid=:branchSchool");
			param.put("branchSchool", condition.get("branchSchool"));
		}
		hql.append(" order by setting.endTime desc");
		param.put("isDeleted", 0);
		return exGeneralHibernateDao.findByHql(page, hql.toString(), param);
	}

}
