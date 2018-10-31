package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.UniversalExam;
import com.hnjk.edu.teaching.vo.UniversalExamCountVO;
import com.hnjk.edu.teaching.vo.UniversalExamDetailsVO;
import com.hnjk.edu.teaching.vo.UniversalExamVO;
import com.hnjk.security.model.User;

/**
 * 统考成绩表-教学计划内课程
 * <code>IUniversalExamService</code>
 * 
 * @author Zik，广东学苑教育发展有限公司
 * @since 2015-7-9 上午 10:52:24
 * @see
 * @version 1.0
 */
public interface IUniversalExamService extends IBaseService<UniversalExam> {

	/**
	 * 根据查询条件获取统考成绩列表
	 * 
	 * @param condition
	 * @param UEpage
	 * @return
	 * @throws ServiceException
	 */
	Page findUniversalExamList(Map<String, Object> condition, Page UEpage) throws ServiceException;
	
	/**
	 * 根据查询条件获取统考成绩列表--HQL
	 * 
	 * @param condition
	 * @param UEpage
	 * @return
	 * @throws ServiceException
	 */
	public Page findUniversalExamListByHQL(Map<String, Object> condition, Page UEpage)
			throws ServiceException;
	
	/**
	 * 解析统考成绩文件
	 * 
	 * @param filePath
	 * @param course
	 * @param user
	 * @param examType
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> analysisUniversalExamFile(String filePath, Course course, User user, String examType) 
			throws ServiceException;
	
	/**
	 * 根据条件查询统考成绩记录
	 * 
	 * @param condition
	 * @param orderBy
	 * @return
	 * @throws ServiceException
	 */
	List<UniversalExam> findByCondition(Map<String, Object> condition, String orderBy) throws ServiceException;
	/**
	 * 根据查询条件返回下载的记录
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<UniversalExamVO>  findUniversalExamVO(Map<String,Object> condition) throws ServiceException;
	
	/**
	 * 更新统考成绩的当前成绩（正考）
	 * @param examResultId
	 * @param studentInfoId
	 * @param courseId
	 * @param planCourseId
	 * @return
	 */
	int updateExamResult(String examResultId, String studentInfoId, String courseId, String planCourseId);

	

	Page getUniversalExamList(Map<String, Object> condition, Page objPage)
			throws ServiceException;

	Page getUniversalExamDetails(Map<String, Object> condition, Page objPage)
			throws ServiceException;

	List<UniversalExamDetailsVO> getUniversalExamDetails(
			Map<String, Object> condition) throws ServiceException, Exception;

	List<UniversalExamDetailsVO> handleUniversalExamDetails(
			Map<String, Object> condition, List<UniversalExamDetailsVO> voList)
			throws Exception;

	List<UniversalExamCountVO> getUniversalExamCount(
			Map<String, Object> condition)
			throws ServiceException;
	
}
