package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseExam;

/**
 * 题库试题管理服务接口.
 * <code>ICourseExamService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-29 上午11:10:58
 * @see 
 * @version 1.0
 */
public interface ICourseExamService extends IBaseService<CourseExam> {
	/**
	 * 分页查询题库试题
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseExamByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;	
	/**
	 * 查询试题列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<CourseExam> findCourseExamByCondition(Map<String, Object> condition) throws ServiceException;	
	/**
	 * 保存或更新
	 * @param courseExam
	 * @param activeCourseExam
	 * @throws ServiceException
	 */
	@Override
	void saveOrUpdate(CourseExam courseExam) throws ServiceException;
	/**
	 * 批量删除试题
	 * @param ids
	 * @param isEnrolExam
	 * @throws ServiceException
	 */
	void batchDeleteCourseExam(String[] ids,String isEnrolExam) throws ServiceException;
	/**
	 * 解析文本导入试题
	 * @param isEnrolExam
	 * @param course
	 * @param courseName
	 * @param text
	 * @return
	 * @throws ServiceException
	 */
	List<CourseExam> parseAndImportCourseExam(String isEnrolExam, String examform, Course course, String courseName, String text) throws ServiceException;
	/**
	 * 导入随堂练习
	 * @param text
	 * @return
	 * @throws ServiceException
	 */
	int parseAndImportActiveCourseExam(String text) throws ServiceException;
	/**
	 * 获取对应试题数量
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> getCourseExamTypeAndCount(Map<String, Object> condition) throws ServiceException;
}
