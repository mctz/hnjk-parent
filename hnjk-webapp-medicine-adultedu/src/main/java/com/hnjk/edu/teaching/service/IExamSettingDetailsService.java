package com.hnjk.edu.teaching.service;

import java.util.List;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
/**
 * 
 * <code>考试计划设置明细表IExamSettingDetailsService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午05:17:55
 * @see 
 * @version 1.0
 */
public interface IExamSettingDetailsService extends IBaseService<ExamSettingDetails> {
	
	/**
	 * 获取所有考试计划设置明细
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamSettingDetails> findAllExamSettingDetails()throws ServiceException;
}
