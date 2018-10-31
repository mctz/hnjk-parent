package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
/**
 * 在线学习模块公共JDBCService
 * <code>ILearningJDBCService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-17 上午10:44:14
 * @see 
 * @version 1.0
 */
public interface ILearningJDBCService {

	/**
	 * 统计学生帖子总数和回复数
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page statStudentBbsTopicAndReply(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 学生网络辅导成绩登记
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page statStudentBbsScore(Map<String, Object> condition, Page objPage) throws ServiceException;
	List<Map<String, Object>> statStudentBbsScore(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 统计教师帖子总数和回复数
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page statEdumanagerBbsTopicAndReply(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 统计随堂练习回答情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> statStudentActiveCourseExam(Map<String, Object> condition) throws ServiceException;
	/**
	 * 查看学生答题情况
	 * @param condition
	 * @return
	 */
	List<Map<String,Object>> stateStudentActiveCourseExam(Map<String, Object> condition)throws ServiceException;
	/**
	 * 查看学生随堂练习得分累积情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page scoreStudentActiveCourseExam(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 查看随堂练习分布情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> distributeStudentActiveCourseExam(Map<String, Object> condition)throws ServiceException;
	/**
	 * 根据学籍ID查询学生的统考课程成绩
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> findStatCouseExamResults(String studentId);
	
	/**
	 * 根据学籍ID查询学生的免修免考成绩
	 * @param studentId
	 * @param courseIds
	 * @return
	 */
	public List<Map<String, Object>> findNoExamCourseResults(String studentId);
	public List<Map<String, Object>> findToGraduateStatCouseExamResults();
	public List<Map<String, Object>> findToGraduateNoExamCourseResults();
	/**
	 * 查看随堂练习分布以及完成情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String,Object>> distributeStudentActiveCourseExamFinished(Map<String, Object> condition)throws ServiceException;
	
	/**
	 * 计算随堂练习分数
	 * @throws Exception 
	 */
	public List<Map<String,Object>> toCalculate(String studyno,String courseid) throws Exception;
	
	/**
	 * 获取某个学生某门课程的总帖子数和有效帖子数
	 * @param bbsUserInfoId
	 * @param courseId
	 * @return
	 */
	public Map<String, Object> getStuCourseTopicNum(String bbsUserInfoId, String courseId) throws Exception ;
	
	/**
	 * 获取学生随堂练习得分累积情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> findStuExamScoreInfo(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 网上学习情况统计 - 分页
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findLearningInfoStatistics(Map<String, Object> condition, Page objPage) throws Exception;
	
	/**
	 * 网上学习情况统计 - 列表
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> findLearningInfoStatistics(Map<String, Object> condition) throws Exception;
}
