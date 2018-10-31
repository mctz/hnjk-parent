package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.UsualResultsRule;
/**
 * 学生平时分积分规则服务接口
 * <code>IUsualResultsRuleService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-20 下午05:02:17
 * @see 
 * @version 1.0
 */
public interface IUsualResultsRuleService extends IBaseService<UsualResultsRule> {
	/**
	 * 分页查询
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findUsualResultsRuleByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 删除
	 * @param ids
	 * @throws ServiceException
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 获取规则版本
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextVersionNum(String courseId) throws ServiceException;
	/**
	 * 获取课程平时分积分规则
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	UsualResultsRule getUsualResultsRuleByCourse(String courseId) throws ServiceException;
	/**
	 * 按年度学期获取课程平时分积分规则
	 * @param courseId
	 * @param yearInfoId
	 * @param term
	 * @return
	 * @throws ServiceException
	 */
	UsualResultsRule getUsualResultsRuleByCourseAndYear(String courseId,String yearInfoId,String term) throws ServiceException;
}
