package com.hnjk.edu.teaching.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.edu.recruit.model.RecruitExamStudentAnswer;

/**
 * 阅卷管理服务接口.
 * <code>IExamPaperCorrectService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-15 下午04:13:40
 * @see 
 * @version 1.0
 */
public interface IExamPaperCorrectService {
	/**
	 * 查找待批阅的试题(包括批阅统计)
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findCourseExamForCorrectByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查找批次下某试题的下一份试卷
	 * @param examSubId
	 * @param examId
	 * @param studyNo 取学号大于studyNo的第一份试卷
	 * @param isFinishCorrect 是否完成批改
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> getStudentExamAnswer(String examSubId, String examId, String answerid, String isFinishCorrect) throws ServiceException;
	/**
	 * 答卷批量置零分
	 * @param examSubId
	 * @param ids
	 * @throws ServiceException
	 */
	void batchZeroExamPapers(String examSubId, String[] ids) throws ServiceException;
	/**
	 * 在线作答成绩列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findOnlineExamResulstsVoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 查询课程列表
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findExamCourseForCorrectByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 保存批改分数
	 * @param examSubId
	 * @param answer
	 * @param score
	 * @throws ServiceException
	 */
	void saveExamPaperCorrectScore(String examSubId,RecruitExamStudentAnswer answer, Double score) throws ServiceException;
	/**
	 * 批量提交成绩
	 * @param examSubId
	 * @param courseIds
	 * @throws ServiceException
	 */
	void submitOnlineExamResults(String examSubId, String[] courseIds)throws ServiceException;
	/**
	 * 重新批阅
	 * @param examResultId
	 * @param answerid
	 * @param score
	 * @throws ServiceException
	 */
	void saveRecorreExamScore(String examResultId, String[] answerid, String[] score) throws ServiceException;
}
