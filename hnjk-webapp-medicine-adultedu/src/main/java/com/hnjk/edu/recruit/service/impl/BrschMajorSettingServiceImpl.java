package com.hnjk.edu.recruit.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
import com.hnjk.edu.recruit.service.IBrschMajorSettingService;
/**
 *  学习中心允许招生的专业限制Service
 * <code>BrschMajorSettingServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-1-11 下午03:10:43
 * @see 
 * @version 1.0
 */

@Service("brschMajorSettingService")
@Transactional
public class BrschMajorSettingServiceImpl extends BaseServiceImpl<BrschMajorSetting> 
										  implements IBrschMajorSettingService  {

	@Override
	public List<BrschMajorSetting> findBranchSchoolLimitMajorList(Map<String,Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		
		hql.append(" from "+BrschMajorSetting.class.getSimpleName()+" setting where setting.isDeleted=0 ");
		
		if (condition.containsKey("isOpened")) {
			hql.append(" and isOpened=:isOpened");
		}
		if (condition.containsKey("brschoolId")) {
			hql.append(" and setting.brSchool.resourceid =:brschoolId");
		}
		
		return (List<BrschMajorSetting>) this.exGeneralHibernateDao.findByHql(hql.toString(),condition);
	}

}
