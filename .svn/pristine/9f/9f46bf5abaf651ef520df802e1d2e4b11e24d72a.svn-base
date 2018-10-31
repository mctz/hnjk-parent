package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.edu.finance.vo.TransferSchoolFeeVo;

/** 
 * 学费提成接口
 * 
 * @author Zik, 广东学苑教育发展有限公司
 * @since Aug 22, 2016 4:27:13 PM 
 * 
 */
public interface IFeeCommissionInfoService {
	/**
	 * 查询学费提成明细列表-广东医版本
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> findFeeCommissionInfoByJDBC(Map<String, Object> condition);

	Page findNotPayStudentFee(Map<String, Object> condition,Page objPage);

	List<Map<String, Object>> findNotPayStudentFee(Map<String, Object> condition);
	
	/**
	 * 查询学费提成明细列表-广外版本
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> findFeeCommissionExt(Map<String, Object> condition);
	
	/**
	 * 查询转教学点学费分成信息
	 * 
	 * @param beginDate
	 * @param endDate
	 * @param separateDate 格式如：07-01
	 * @return
	 */
	List<TransferSchoolFeeVo> findChangeUnitFeeInfo(String beginDate, String endDate,String separateDate) throws Exception;

}
