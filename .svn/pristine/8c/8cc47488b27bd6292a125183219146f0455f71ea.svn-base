package com.hnjk.edu.learning.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
/**
 * 网上学习时间设置服务接口实现.
 * <code>LearningTimeSettingServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-1-6 下午03:31:08
 * @see 
 * @version 1.0
 */
@Transactional
@Service("learningTimeSettingService")
public class LearningTimeSettingServiceImpl extends BaseServiceImpl<LearningTimeSetting> implements ILearningTimeSettingService {

	@Override
	@Transactional(readOnly=true)
	public Page findLearningTimeSettingByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+LearningTimeSetting.class.getSimpleName()+" where isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("yearInfoId")){//年度
			hql.append(" and yearInfo.resourceid = :yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}	
		if(condition.containsKey("term")){//学期
			hql.append(" and term = :term ");
			values.put("term", condition.get("term"));
		}
		if(objPage.isOrderBySetted()){
			hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		}
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<LearningTimeSetting> findLearningTimeSettingListByCondition(String yearInfoId, String term) throws ServiceException {
		List<LearningTimeSetting> sets  = (List<LearningTimeSetting>)exGeneralHibernateDao.findByHql(" from "+LearningTimeSetting.class.getSimpleName()+" lt where lt.isDeleted = ? and lt.yearInfo.firstYear = ? and lt.term = ? ", 0,Long.parseLong(yearInfoId),term);
		return sets;
	}

	@Override
	public LearningTimeSetting findLearningTimeSetting(String yearInfoId, String term) throws ServiceException {
		String hql = " from "+LearningTimeSetting.class.getSimpleName()+" where isDeleted = ? and yearInfo.resourceid = ? and term = ? ";
		List<LearningTimeSetting> list = findByHql(hql, 0,yearInfoId,term);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public LearningTimeSetting findLearningTimeSetting(Integer isdeleted,String firstYear, String term) throws ServiceException {
		String hql = " from "+LearningTimeSetting.class.getSimpleName()+" where isDeleted = ? and yearInfo.firstYear = ? and term = ? ";
		List<LearningTimeSetting> list = findByHql(hql, isdeleted,Long.parseLong(firstYear),term);
		if(list != null && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
}
