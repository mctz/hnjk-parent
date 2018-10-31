package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
/**
 * 网上学习时间设置服务接口
 * <code>ILearningTimeSettingService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-1-6 下午02:55:32
 * @see 
 * @version 1.0
 */
public interface ILearningTimeSettingService extends IBaseService<LearningTimeSetting> {

	/**
	 * 分页查询网上学习时间
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findLearningTimeSettingByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 获取某年度的网上学习时间
	 * @param yearInfoId
	 * @param term
	 * @return
	 * @throws ServiceException
	 */
	LearningTimeSetting findLearningTimeSetting(String yearInfoId, String term) throws ServiceException;
	
	LearningTimeSetting findLearningTimeSetting(Integer isdeleted,String yearInfoId, String term) throws ServiceException;
	
	List<LearningTimeSetting> findLearningTimeSettingListByCondition(String yearInfoId, String term) throws ServiceException;
}
