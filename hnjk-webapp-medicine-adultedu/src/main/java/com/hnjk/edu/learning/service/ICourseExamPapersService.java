package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.CourseExamRules;
/**
 * 试卷管理服务接口.
 * <code>ICourseExamPapersService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-13 上午11:10:31
 * @see 
 * @version 1.0
 */
public interface ICourseExamPapersService extends IBaseService<CourseExamPapers> {
	/**
	 * 分页查询试卷
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseExamPapersByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 根据成卷规则生成试卷
	 * @param rule
	 * @return
	 * @throws ServiceException
	 */
	CourseExamPapers fetchCourseExamPapersByRandom(CourseExamRules rule,String paperType,String paperName) throws ServiceException;
	/**
	 * 
	 * @param rule
	 * @param paperType
	 * @param paperName
	 * @param courseExamMap 试题来源集合，key：ruledetailsid,value:对应试题集合
	 * @return
	 * @throws ServiceException
	 */
	CourseExamPapers fetchCourseExamPapersByRandom(CourseExamRules rule,String paperType,String paperName,Map<String, List<CourseExam>> courseExamMap) throws ServiceException;
}
