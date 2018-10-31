package com.hnjk.edu.recruit.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
import com.hnjk.edu.recruit.model.RecruitExamStudentAnswer;
/**
 * 入学考试学生作答情况服务接口.
 * <code>IRecruitExamStudentAnswerService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-28 上午10:57:30
 * @see 
 * @version 1.0
 */
public interface IRecruitExamStudentAnswerService extends IBaseService<RecruitExamStudentAnswer> {
	/**
	 * 获取考试结果
	 * @param enrolleeInfoId
	 * @param recruitExamPlanId
	 * @param courseExamPaperId
	 * @return
	 * @throws ServiceException
	 */
	Double getRecruitExamStudentAnswerResult(String enrolleeInfoId,String recruitExamPlanId, String courseExamPaperId) throws ServiceException;
	/**
	 * 获取网考结果
	 * @param studentInfoId
	 * @param courseExamPaperId
	 * @return
	 * @throws ServiceException
	 */
	Double getOnlineExamStudentAnswerResult(String studentInfoId, String courseExamPaperId) throws ServiceException;
	
	void saveOrUpdateOnlineExamResult(Map<String, String> answerMap,RecruitExamLogs recruitExamLogs) throws ServiceException;
}
