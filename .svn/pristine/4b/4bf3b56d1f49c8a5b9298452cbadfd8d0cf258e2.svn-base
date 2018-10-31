package com.hnjk.edu.learning.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseExamRules;
/**
 * 试卷成卷规则服务接口.
 * <code>ICourseExamRulesService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-26 下午04:35:42
 * @see 
 * @version 1.0
 */
public interface ICourseExamRulesService extends IBaseService<CourseExamRules> {
	/**
	 * 分页查询试卷成卷规则
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseExamRulesByCondition(Map<String, Object> condition, Page objPage) throws ServiceException; 
	/**
	 * 获取下一个版本号
	 * @param isEnrolExam
	 * @param courseName
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextVersionNum(String isEnrolExam,String courseName,String courseId) throws ServiceException; 
	/**
	 * 可用的试题数
	 * @param isEnrolExam
	 * @param courseName
	 * @param courseId
	 * @param examNodeType
	 * @param examType
	 * @return
	 * @throws ServiceException
	 */
	Long getAvailableCourseExamsCount(CourseExamRules courseExamRules,String examNodeType,String examType) throws ServiceException; 
}
