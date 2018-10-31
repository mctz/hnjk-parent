package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
/**
 *教学活动环节中的时间设置服务接口.
 * <code>ITeachingActivityTimeSettingService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2012-12-25 下午02:40:48
 * @see 
 * @version 1.0
 */
public interface ITeachingActivityTimeSettingService extends IBaseService<TeachingActivityTimeSetting> {
	
	/**
	 * 根据条件查询教学活动环节中的时间设置-返回Page
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findTeachingActivityTimeSettingByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	
	/**
	 * 根据条件查询教学活动环节中的时间设置-返回List
	 * @param condition
	 * @return
	 */
	List<TeachingActivityTimeSetting> findTeachingActivityTimeSettingByCondition(Map<String,Object> condition) throws ServiceException;
}
