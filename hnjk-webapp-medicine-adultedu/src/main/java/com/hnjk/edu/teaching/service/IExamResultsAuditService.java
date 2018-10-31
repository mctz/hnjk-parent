package com.hnjk.edu.teaching.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamResultsAudit;
/**
 * 
 * <code>成绩复审Service接口</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-12-22 下午15:21:02
 * @see 
 * @version 1.0
 */
public interface IExamResultsAuditService extends IBaseService<ExamResultsAudit> {

	/**
	 * 根据条件查询成绩复审-返回分页对象
	 * @param condition
	 * @param page
	 * @return
	 */
	public Page findExamResultsAuditByCondition(Map<String,Object> condition,Page page)throws ServiceException;
	
	/**
	 * 成绩复审
	 * @param ids
	 * @param flag
	 * @throws ServiceException
	 */
	public void examResultsReview(String[]ids,String flag,HttpServletRequest request)throws Exception;
}
