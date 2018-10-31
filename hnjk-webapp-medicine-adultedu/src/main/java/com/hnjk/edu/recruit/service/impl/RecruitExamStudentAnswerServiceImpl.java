package com.hnjk.edu.recruit.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
import com.hnjk.edu.recruit.model.RecruitExamStudentAnswer;
import com.hnjk.edu.recruit.service.IRecruitExamStudentAnswerService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.ExamResults;
/**
 * 入学考试学生作答情况服务接口实现.
 * <code>RecruitExamStudentAnswerServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-28 上午10:58:56
 * @see 
 * @version 1.0
 */
@Transactional
@Service("recruitExamStudentAnswerService")
public class RecruitExamStudentAnswerServiceImpl extends BaseServiceImpl<RecruitExamStudentAnswer> implements IRecruitExamStudentAnswerService {
	
	@Override
	public Double getRecruitExamStudentAnswerResult(String enrolleeInfoId, String recruitExamPlanId, String courseExamPaperId) throws ServiceException {
		String hql = " select coalesce(sum(coalesce(result,0)),0) from "+RecruitExamStudentAnswer.class.getSimpleName()+" where isDeleted=0 and enrolleeInfo.resourceid=? and recruitExamPlan.resourceid=? and courseExamPapers.resourceid=? ";
		return exGeneralHibernateDao.findUnique(hql, enrolleeInfoId,recruitExamPlanId,courseExamPaperId);
	}
	
	@Override
	public Double getOnlineExamStudentAnswerResult(String studentInfoId, String courseExamPaperId) throws ServiceException {
		String hql = " select coalesce(sum(coalesce(result,0)),0) from "+RecruitExamStudentAnswer.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and courseExamPapers.resourceid=? ";
		return exGeneralHibernateDao.findUnique(hql, studentInfoId,courseExamPaperId);
	}
	
	@Override
	public void saveOrUpdateOnlineExamResult(Map<String, String> answerMap,RecruitExamLogs recruitExamLogs) throws ServiceException {
		String planId = ExStringUtils.trimToEmpty(answerMap.get("planId"));//考场安排id
		String recruitExamLogsId = ExStringUtils.trimToEmpty(answerMap.get("recruitExamLogsId"));
		double total = 0.0;			
		if(ExStringUtils.isNotEmpty(planId) && ExStringUtils.isNotBlank(recruitExamLogsId) && recruitExamLogsId.equals(recruitExamLogs.getResourceid())){		
			StudentLearnPlan plan = (StudentLearnPlan) exGeneralHibernateDao.get(StudentLearnPlan.class, planId);
			CourseExamPapers paper = recruitExamLogs.getCourseExamPapers();//试卷
			
			Date curDate = new Date();
			List<RecruitExamStudentAnswer> answers = findByHql(" from "+RecruitExamStudentAnswer.class.getSimpleName()+" where isDeleted=0 and courseExamPapers.resourceid=? and studentInfo.resourceid=? ", paper.getResourceid(),plan.getStudentInfo().getResourceid());
			total = saveOrUpdateStudentAnswers(answerMap, paper, answers,plan.getStudentInfo());	//保存答题情况并计算总分			
			total = BigDecimal.valueOf(total).divide(BigDecimal.ONE,BigDecimal.ROUND_HALF_UP,0).doubleValue();//四舍五入取整			
			
			if(recruitExamLogs.getLoginTime()==null){
				recruitExamLogs.setLoginTime(recruitExamLogs.getStartTime());
			}
			recruitExamLogs.setExamScore(total);
			recruitExamLogs.setStatus(2);//设置为交卷状态
			if(recruitExamLogs.getEndTime()!=null && curDate.after(recruitExamLogs.getEndTime())){
				recruitExamLogs.setLogoutTime(recruitExamLogs.getEndTime());//设置结束时间
			} else {
				recruitExamLogs.setLogoutTime(curDate);//设置结束时间
			}	
			
			String score = BigDecimal.valueOf(total).divide(BigDecimal.ONE,BigDecimal.ROUND_DOWN,0).toString();
			ExamResults rs = plan.getExamResults();
			rs.setWrittenScore(score);
			rs.setWrittenMachineScore(score);									
			rs.setCheckStatus("0");//改为保存	
		}		
	}
	
	public double saveOrUpdateStudentAnswers(Map<String, String> answerMap, CourseExamPapers paper, List<RecruitExamStudentAnswer> answers,StudentInfo studentInfo) {
		List<RecruitExamStudentAnswer> list = new ArrayList<RecruitExamStudentAnswer>();
		double total = 0.0;//总分
		for (CourseExamPaperDetails exam : paper.getCourseExamPaperDetails()) {	
			RecruitExamStudentAnswer studentAnswer = findStudentAnswerByExamDetails(answers,exam);//答案项是否以创建
			if(studentAnswer==null){
				studentAnswer = new RecruitExamStudentAnswer();
				studentAnswer.setCourseExamPaperDetails(exam);
				studentAnswer.setShowOrder(exam.getShowOrder());
				studentAnswer.setCourseExamPapers(paper);
				studentAnswer.setStudentInfo(studentInfo);
			} 	
			String answer = ExStringUtils.trimToEmpty(answerMap.get("a"+exam.getShowOrder()));
			if(CourseExam.SINGLESELECTION.equals(exam.getCourseExam().getExamType())
					|| CourseExam.MUTILPCHIOCE.equals(exam.getCourseExam().getExamType())
					|| CourseExam.CHECKING.equals(exam.getCourseExam().getExamType())){ //非材料题的文章部分,单选，多选，判断保存答案												
				studentAnswer.setAnswer(ExStringUtils.trimToEmpty(answer));//保存答案
				String correctAnswer = exam.getCourseExam().getAnswer();
				if(CourseExam.CHECKING.equals(exam.getCourseExam().getExamType())){//判断题
					correctAnswer = covertToCorrectAnswer(correctAnswer);//把不同格式的答案转成T和F
				}
				if(answer.equalsIgnoreCase(correctAnswer)){//答案正确
					studentAnswer.setIsCorrect(Constants.BOOLEAN_YES);
					studentAnswer.setResult(exam.getScore());
					total += studentAnswer.getResult();
				} else {
					studentAnswer.setIsCorrect(Constants.BOOLEAN_NO);
					studentAnswer.setResult(0.0);
				}
			} else if(Constants.BOOLEAN_YES.equals(exam.getCourseExam().getIsOnlineAnswer()) 
					&& (CourseExam.COMPLETION.equals(exam.getCourseExam().getExamType()) 
							|| CourseExam.ESSAYS.equals(exam.getCourseExam().getExamType()))){
				//在线作答的非客观题，保存答案
				studentAnswer.setAnswer(ExStringUtils.trimToEmpty(answer));//保存答案
			}
			list.add(studentAnswer);
		}
		batchSaveOrUpdate(list);
		return total;
	}
	
	/**
	 * 查找已答结果
	 * @param answers
	 * @param exam
	 * @return
	 */
	private RecruitExamStudentAnswer findStudentAnswerByExamDetails(List<RecruitExamStudentAnswer> answers,CourseExamPaperDetails exam){
		if(answers!=null && !answers.isEmpty()){
			for (RecruitExamStudentAnswer answer : answers) {
				if(answer.getCourseExamPaperDetails().getResourceid().equals(exam.getResourceid())){
					return answer;
				}
			}
		}
		return null;
	}
	
	private String covertToCorrectAnswer(String answer){
		if("T".equalsIgnoreCase(answer) || "对".equalsIgnoreCase(answer) || "√".equalsIgnoreCase(answer)){
			return "T";
		} else if("F".equalsIgnoreCase(answer) || "错".equalsIgnoreCase(answer) || "×".equalsIgnoreCase(answer)){
			return "F";
		}
		return answer;
	}
}
