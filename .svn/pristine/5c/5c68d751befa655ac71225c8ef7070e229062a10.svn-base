package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.StudentActiveCourseExam;
import com.hnjk.edu.roll.model.StudentInfo;
/**
 * 随堂练习学生回答情况服务接口
 * <code>IStudentActiveCourseExamService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-23 上午09:46:51
 * @see 
 * @version 1.0
 */
public interface IStudentActiveCourseExamService extends IBaseService<StudentActiveCourseExam> {
	/**
	 * 已提交随堂练习数目
	 * @param syllabusId
	 * @param studentInfoId
	 * @param type all-已保存，finished-已提交题目数，correct-正确数
	 * @return
	 * @throws ServiceException
	 */
	Long finishedActiveCourseExam(String syllabusId,String studentInfoId,String type) throws ServiceException;
	/**
	 * 学生随堂练习正确率
	 * @param courseId
	 * @param studentInfoId
	 * @return
	 * @throws ServiceException
	 */
	Double avgStudentActiveCourseExamResult(String courseId,String studentInfoId) throws ServiceException;
	/**
	 * 分页查询学生答题情况
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findStudentActiveCourseExamByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 重做随堂练习
	 * @param ids
	 * @throws ServiceException
	 */
	void redoActiveCourseExam(String[] ids) throws ServiceException;	
	/**
	 * 对随堂练习进行调分
	 * @param ids
	 * @throws ServiceException
	 */
	void scoreActiveCourseExam(String[] ids) throws ServiceException;	
	/**
	 * 
	 * @param currentlist
	 * @param studentInfo
	 * @param syllabusId
	 * @param type
	 * @throws ServiceException
	 */
	void saveAllStudentActiveCourseExam(List<StudentActiveCourseExam> currentlist,StudentInfo studentInfo,String syllabusId,String type) throws ServiceException;
}
