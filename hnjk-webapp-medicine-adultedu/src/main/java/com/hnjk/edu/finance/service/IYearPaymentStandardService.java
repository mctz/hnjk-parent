package com.hnjk.edu.finance.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.YearPaymentStandard;
import com.hnjk.edu.finance.vo.StudentPaymentInfoVo;
/**
 * 年度缴费标准服务接口.
 * <code>IYearPaymentStandardService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-9 下午03:26:39
 * @see 
 * @version 1.0
 */
public interface IYearPaymentStandardService extends IBaseService<YearPaymentStandard> {
	/**
	 * 分页查询年缴费标准
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findYearPaymentStandardByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 根据年级和缴费类别获取年缴费标准
	 * @param gradeId
	 * @param paymentType
	 * @return
	 * @throws ServiceException
	 */
	YearPaymentStandard getYearPaymentStandard(String gradeId, String paymentType) throws ServiceException;
	
	/**
	 * 根据开始缴费日期获取缴费的有关信息
	 * @param payBeginDate
	 * @return
	 * @throws ServiceException
	 */
	List<StudentPaymentInfoVo> findPaymentInfoByBeginDate(Date payBeginDate) throws ServiceException;
	
	/**
	 * 根据年级、专业和缴费期数获取缴费标准明细
	 * @param gradeId
	 * @param majorId
	 * @param feeTerm
	 * @return
	 * @throws ServiceException
	 */
	StudentPaymentInfoVo findByCondition(String gradeId,String majorId,Integer feeTerm) throws ServiceException;
	
	/**
	 * 扫描学生缴费标准
	 * @return 
	 */
	public int scanPaymentStandar(String resourceid) throws ServiceException;
	
}
