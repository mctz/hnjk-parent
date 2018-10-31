package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamSetting;
import com.hnjk.edu.teaching.model.ExamSettingDetails;

/**
 * 考试计划设置表
 * <code>IExamSettingService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午03:58:46
 * @see 
 * @version 1.0
 */

public interface IExamSettingService  extends IBaseService<ExamSetting>{

	Page findExamSettingByCondition(Map<String, Object> condition, Page objPage);

	@Override
	void batchDelete(String[] ids);

	void deleteExamCourse(String c_id);

	ExamSettingDetails getExamCourse(String string);
	
	/**
	 * 更新考试计划设置
	 * @param settId
	 * @throws ServiceException
	 */
	public void updateExamSetting(String[] courseIds,ExamSetting examSetting)throws ServiceException;
	
	/**
	 * 关联子表查询考试计划设置
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findExamSettingJoinSubTable(Map<String, Object> condition, Page objPage)throws ServiceException;
}
