package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.BrschMajorSetting;
/**
 * 学习中心允许招生的专业限制Service
 * <code>IBrschMajorSettingService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-1-11 下午03:10:50
 * @see 
 * @version 1.0
 */
public interface IBrschMajorSettingService extends IBaseService<BrschMajorSetting>{

	/**
	 * 获取学习中心允许招生的专业列表
	 * @param brschoolId
	 * @return
	 * @throws ServiceException
	 */
	public List<BrschMajorSetting> findBranchSchoolLimitMajorList(Map<String,Object> condition)throws ServiceException;
}
