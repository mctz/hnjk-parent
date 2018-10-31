package com.hnjk.edu.finance.service;


import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.finance.model.ReturnPremium;
import com.hnjk.edu.finance.vo.MajorFeeInfoVO;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * 退费记录标准服务接口.
 * <code>IReturnPremiumService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @see 
 * @version 1.0
 */
public interface IReturnPremiumService extends IBaseService<ReturnPremium> {


	List<ReturnPremium> findReturnPremiumByCondition(Map<String, Object> condition);

	Page findReturnPremiumByCondition(Map<String, Object> condition, Page page);

	List<Map<String, Object>> findReturnPremiumForMap(Map<String, Object> condition) throws ServerException;
	
	/**
	 * 创建退缴费记录
	 * 
	 * @param money
	 * @param facepayFee
	 * @param paymentMethod
	 * @param recpayFee
	 * @param _stuInfo
	 * @param yearInfo
	 * @param showOrder
	 * @param processType
	 * @param orderNo
	 * @param chargingItems
	 * @return
	 */
	public ReturnPremium createReturnPremium(Double money, Double facepayFee,String paymentMethod, Double recpayFee, 
			StudentInfo _stuInfo,YearInfo yearInfo, Integer showOrder,String processType,String orderNo,String chargingItems);
	
	/**
	 * 学籍异动审批成功后处理学费退费补交逻辑
	 * 
	 * @param changeInfo
	 */
	public Map<String, Object> handleTuitionForStuChange(StuChangeInfo changeInfo);
	
	/**
	 * 获取某个年某个专业第几年的缴费标准信息
	 * 
	 * @param gradeId
	 * @param majorIds
	 * @param feeTerm
	 * @return
	 */
	public List<MajorFeeInfoVO> findMajorFeeInfo(String gradeId,String[] majorIds,int feeTerm ) throws Exception;

}
