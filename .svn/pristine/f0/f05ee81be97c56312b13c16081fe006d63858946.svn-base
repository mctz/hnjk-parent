
/**
 * <code>IStuPerpayfeeService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:16:19
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.StuPerpayfee;
import com.hnjk.edu.roll.model.StudentFactFee;

/**
 * <code>IStuPerpayfeeService</code><p>
 * 学生预交费用表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:16:19
 * @see 
 * @version 1.0
 */
public interface IStuPerpayfeeService extends IBaseService<StuPerpayfee> {
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findFeeByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 查询学生实际缴费、应缴明细
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findFactFeeByCondition(Map<String, Object> condition ,Page objPage) throws ServiceException;
	
	void saveCollection(List<StuPerpayfee> stuFeeList) throws ServiceException;
	/**
	 * 获取学员的应缴费用和实缴费用
	 * @param values
	 * @return
	 */
	List findByHql( Map<String,Object> values) throws ServiceException;
	
	/**
	 * 同步銀校通數據
	 * @throws ServiceException
	 */
	void synchronStuFee() throws ServiceException;
	/**
	 * 查询缴费信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<StudentFactFee> findStudentFactFeeByCondition(Map<String, Object> condition) throws ServiceException;

}
