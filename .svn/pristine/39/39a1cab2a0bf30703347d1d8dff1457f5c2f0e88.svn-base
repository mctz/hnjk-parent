package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.PublishedExamResultsAudit;
/**
 * 成绩复审接口.
 * <code>IPublishedExamResultsAuditService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-1-16 下午03:25:50
 * @see 
 * @version 1.0
 */
public interface IPublishedExamResultsAuditService extends IBaseService<PublishedExamResultsAudit> {

	/**
	 * 根据条件查询成绩复审-返回分页对象
	 * @param condition
	 * @param page
	 * @return
	 */
	public Page findExamResultsAuditByCondition(Map<String,Object> condition,Page page)throws ServiceException;

	/**
	 * 已发布的成绩更正复审
	 * @param ids
	 * @throws ServiceException
	 */
	void publishAuditExamResults(String[] ids) throws Exception;
	/**
	 * 已发布的毕业论文成绩更正复审
	 * @param ids
	 * @throws ServiceException
	 */
	void publishAuditThesisExamResulsts(String[] ids) throws ServiceException;
}
