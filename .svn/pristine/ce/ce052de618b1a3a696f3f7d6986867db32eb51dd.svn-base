package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
/**
 * 时间设置服务接口.
 * <code>ITeachingPlanCourseTimePeriodService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-13 下午04:23:51
 * @see 
 * @version 1.0
 */
public interface ITeachingPlanCourseTimePeriodService extends IBaseService<TeachingPlanCourseTimePeriod> {
	
	Page findTeachingPlanCourseTimePeriodByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	String constructOptions(Map<String, Object> condition, String[] settingList);

	/**
	 * 删除没有使用的重复记录
	 */
	void distinctTimePeriod();
}
