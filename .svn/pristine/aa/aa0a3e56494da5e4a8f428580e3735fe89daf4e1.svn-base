package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.AnnualFees;

/**
 * 学生年度缴费服务接口.
 * <code>IAnnualFeesService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @see 
 * @version 1.0
 */
public interface IAnnualFeesService extends IBaseService<AnnualFees> {

	/**
	 * 根据条件获取年度缴费信息
	 */
	List<AnnualFees> findAnnualFeesByCondition(Map<String, Object> condition);
	
	/**
	 * 根据条件获取年度缴费信息,分页
	 */
	Page findAnnualFeesByCondition(Map<String, Object> condition, Page page);
	
	/**
	 * 根据条件获取年度缴费信息
	 * @param studentInfoId
	 * @param yearInfoId
	 * @param chargingItems
	 * @return
	 */
	AnnualFees findUniqueByCondition(String studentInfoId, String yearInfoId, String chargingItems);
}
