package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.finance.model.Refundback;
import com.hnjk.edu.finance.vo.RefundbackVO;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午4:37:51 
 * 
 */
public interface IRefundbackService extends IBaseService<Refundback> {

	
	 /**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	 Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除(只能删除未处理状态)
	 * @param resourceids
	 * @throws ServiceException
	 */
	 void batchCascadeDelete(String resourceids) throws ServiceException;
	
	/**
	 * 根据条件获取预退费补交订单信息列表
	 * @param condition
	 * @return
	 */
	 List<Refundback> findRefundbackByCondition(Map<String, Object> condition);
	
	/**
	 * 批量处理或回退预退费补交订单
	 * @param type
	 * @param resourceids
	 * @return
	 */
	Map<String, Object> handleOrRollbackRefundback(String type,String resourceids);
	
	/**
	 * 根据条件获取预退费补交订单信息
	 * 
	 * @param condition
	 * @return
	 */
	List<RefundbackVO> findRefundbackInfoByCondition(Map<String, Object> condition) throws Exception;
	
	/**
	 * 退补教材费
	 * 
	 * @param stuChangeId
	 * @param processType
	 * @param money
	 * @return
	 */
	Map<String,Object> refundSuppleTextbookFee(String stuChangeId,String processType,Double money);
	
	/**
	 * 编辑退补订单信息
	 * 
	 * @param resourceid
	 * @param money
	 * @return
	 */
	Map<String, Object> editRefundback(String resourceid,Double money);
	
	void createRefundback(StuChangeInfo changeInfo,String chargingItems,
			StudentInfo studentInfo,String processType,YearInfo year,Double money);
}
