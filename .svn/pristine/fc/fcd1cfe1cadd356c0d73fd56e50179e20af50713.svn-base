package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.TextbookFee;
import com.hnjk.edu.finance.vo.TextbookFeeVo;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年7月31日 下午3:56:38 
 * 
 */
public interface ITextbookFeeService extends IBaseService<TextbookFee> {

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;
	
	/**
	 * 根据年度和专业获取教材费信息
	 * 
	 * @param gradeId
	 * @param yearId
	 * @return
	 */
	TextbookFee findByYearAndMajor(String yearId, String majorId);
	
	/**
	 * 处理导入年教材费标准逻辑
	 * @param textbookFeeList
	 * @param yearId
	 * @return
	 */
	public Map<String, Object> handleTextbookFee(List<TextbookFeeVo> textbookFeeList, String yearId);
}
