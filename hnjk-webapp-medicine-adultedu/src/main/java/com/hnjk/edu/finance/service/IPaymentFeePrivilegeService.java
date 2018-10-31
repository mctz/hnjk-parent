package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.PaymentFeePrivilege;
import com.hnjk.edu.finance.vo.PaymentFeePrivilegeVo;
/**
 * 学习中心优惠设置服务接口.
 * <code>IPaymentFeePrivilegeService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-9 上午10:07:09
 * @see 
 * @version 1.0
 */
public interface IPaymentFeePrivilegeService extends IBaseService<PaymentFeePrivilege> {
	/**
	 * 分页查询学习中心优惠
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findPaymentFeePrivilegeVoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查询学习中心优惠
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<PaymentFeePrivilegeVo> findPaymentFeePrivilegeVoByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 获取学习中心优惠(recruitMajorId为空时获取招生专业学习中心优惠)
	 * @param brSchool
	 * @param recruitMajorId
	 * @return
	 * @throws ServiceException
	 */
	PaymentFeePrivilege getPaymentFeePrivilege(String brSchool, String recruitMajorId) throws ServiceException;
}
