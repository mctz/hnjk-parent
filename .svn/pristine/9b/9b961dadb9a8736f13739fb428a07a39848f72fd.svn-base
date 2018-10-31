package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
/**
 * 学生缴费明细接口.
 * <code>IStudentPaymentDetailsService</code><p>
 * 
 * @author：  zik, 广东学苑教育发展有限公司
 * @since： 2015-11-16 16:35
 * @see 
 * @version 1.0
 */
public interface IStudentPaymentDetailsService extends IBaseService<StudentPaymentDetails> {
	
	/**
	 * 根据条件获取学生缴费明细列表-page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findStudentPaymentDetailsByCondition(Page objPage,Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据条件获取学生缴费明细列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentPaymentDetails> findStudentPaymentDetailsByCondition(Map<String, Object> condition) throws ServiceException;

	/**
	 * 根据条件获取学费汇总表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> findChargeSummayForMap(Map<String, Object> condition) throws ServiceException;
}
