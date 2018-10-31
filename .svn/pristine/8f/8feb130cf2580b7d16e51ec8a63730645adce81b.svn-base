package com.hnjk.edu.finance.service;

import java.math.BigDecimal;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.StudentFeeRule;

/**
 * 年级预交费用设置表.
 * <code>IStudentFeeRuleService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-8-13 下午02:05:11
 * @see 
 * @version 1.0
 */
public interface IStudentFeeRuleService extends IBaseService<StudentFeeRule>{

	Page findFeeRuleByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	BigDecimal AjaxSumPoint(String grade, String major, String classic) throws ServiceException;

	void batchCascadeDelete(String[] split) throws ServiceException;

	void deleteDetail(String c_id) throws ServiceException;

	void exeGeneratorFee(String resourceids) throws ServiceException;

	void update(StudentFeeRule fee, HttpServletRequest request);

}
