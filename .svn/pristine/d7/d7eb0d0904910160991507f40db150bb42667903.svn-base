package com.hnjk.edu.teaching.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.recruit.model.RecruitExamStudentAnswer;
import com.hnjk.edu.recruit.service.IRecruitExamStudentAnswerService;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.IExamPaperCorrectService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
/**
 * 阅卷管理.
 * <code>ExamPaperCorrectController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-15 下午04:11:28
 * @see 
 * @version 1.0
 */
@Controller
public class ExamPaperCorrectController extends BaseSupportController{

	private static final long serialVersionUID = 9034765866280929955L;
	
	@Autowired
	@Qualifier("examPaperCorrectService")
	private IExamPaperCorrectService examPaperCorrectService;
	
	@Autowired
	@Qualifier("recruitExamStudentAnswerService")
	private IRecruitExamStudentAnswerService recruitExamStudentAnswerService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;

	/**
	 * 考试课程
	 * @param examSubId
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/course/list.html")
	public String listExamPaperCourse(String examSubId, Page objPage, ModelMap model) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();		
		if(ExStringUtils.isBlank(examSubId)){//查询默认批次
			List<ExamSub> examSubList = examSubService.findByHql("from ExamSub examSub where isDeleted=? and batchType=? and isSpecial=? order by yearInfo.firstYear desc,term desc,resourceid", 0,"exam",Constants.BOOLEAN_NO);
			if(ExCollectionUtils.isNotEmpty(examSubList)){//查询默认批次
				examSubId = examSubList.get(0).getResourceid();
			}
		}
		if(ExStringUtils.isNotBlank(examSubId)) {
			condition.put("examSubId", examSubId);//考试批次
			Page page = examPaperCorrectService.findExamCourseForCorrectByCondition(condition, objPage);
			model.addAttribute("examCourseList", page);
		}
		model.addAttribute("condition", condition);
		return "/edu3/teaching/exampapercorrect/exampaper-course-list";
	}	
	
	/**
	 * 查询考试试题列表
	 * @param examSubId
	 * @param courseId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/list.html")
	public String listExamPaperCorrect(String examSubId, String courseId, HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();		
		if(ExStringUtils.isBlank(examSubId)){//查询默认批次
			List<ExamSub> examSubList = examSubService.findByHql("from ExamSub examSub where isDeleted=? and batchType=? and isSpecial=? order by yearInfo.firstYear desc,term desc,resourceid", 0,"exam",Constants.BOOLEAN_NO);
			if(ExCollectionUtils.isNotEmpty(examSubList)){//查询默认批次
				examSubId = examSubList.get(0).getResourceid();
			}
		}
		if(ExStringUtils.isNotBlank(examSubId)) {
			condition.put("examSubId", examSubId);//考试批次
			
			String sql = "select distinct c.coursename,c.coursecode,c.resourceid from edu_teach_examinfo t join edu_base_course c on c.resourceid=t.courseid where t.isdeleted=0 and t.ismachineexam='Y' and t.examsubid=? and exists ( select d.resourceid from edu_lear_expaperdetails d join edu_lear_exams s on s.resourceid=d.examid and s.isdeleted=0 join edu_recruit_stustates a on a.exampaperid=d.paperid and a.isdeleted=0 where d.isdeleted=0 and s.examtype in ('4','5') and a.examinfoid=t.resourceid) order by c.courseCode";
			try {
				List<Map<String, Object>> courseList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql, new Object[]{examSubId});
				model.addAttribute("courseList", courseList);
			} catch (Exception e) {
				logger.error("查询批阅的课程列表出错",e.fillInStackTrace());
			}
		}
		if(ExStringUtils.isNotBlank(courseId)) {
			condition.put("courseId", courseId);//课程
		}
		if(ExStringUtils.isNotBlank(examSubId) && ExStringUtils.isNotBlank(courseId)){		
			Page page = examPaperCorrectService.findCourseExamForCorrectByCondition(condition, objPage);
			model.addAttribute("examList", page);
		}
		model.addAttribute("condition", condition);
		return "/edu3/teaching/exampapercorrect/exampapercorrect-list";
	}
	/**
	 * 学生答题情况
	 * @param examSubId
	 * @param examId
	 * @param studyNo
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/correct.html")
	public String correctStudentExamAnswer(String examSubId, String examId, String answerid, HttpServletRequest request, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(examSubId) && ExStringUtils.isNotBlank(examId)){	
			String isFinishCorrect = ExStringUtils.trimToEmpty(request.getParameter("isFinishCorrect"));
			Map<String, Object> studentExamAnswer = examPaperCorrectService.getStudentExamAnswer(examSubId, examId, answerid,isFinishCorrect);
			if(!"Y".equals(isFinishCorrect) && (studentExamAnswer == null || studentExamAnswer.isEmpty())){//无isFinishCorrect参数
				isFinishCorrect = "Y";//批阅完毕，设置批阅结束标志
				studentExamAnswer = examPaperCorrectService.getStudentExamAnswer(examSubId, examId, answerid,isFinishCorrect);
			}
			model.addAttribute("studentExamAnswer", studentExamAnswer);
			
			model.addAttribute("scoreMode", ExStringUtils.trimToEmpty(request.getParameter("scoreMode")));//评分模式
			model.addAttribute("showOrHide1", ExStringUtils.trimToEmpty(request.getParameter("showOrHide1")));//题目显示状态
			model.addAttribute("showOrHide2", ExStringUtils.trimToEmpty(request.getParameter("showOrHide2")));//答案显示状态
			model.addAttribute("isFinishCorrect", isFinishCorrect);
			model.addAttribute("lineChar", "\n");
		}		
		return "/edu3/teaching/exampapercorrect/exampapercorrect-form";
	}
	/**
	 * 保存批阅结果
	 * @param examSubId
	 * @param answerid
	 * @param score
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/save.html")
	public void saveStudentExamAnswerScore(String examSubId, String answerid, Double score,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(answerid)){
				RecruitExamStudentAnswer answer = recruitExamStudentAnswerService.get(answerid);	
				if(answer.getResult()==null || !answer.getResult().equals(score)){//还未批改 或 分数改动
					examPaperCorrectService.saveExamPaperCorrectScore(examSubId, answer, score);
				}//分数不变时不更新				
				
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_TEACHING_EXAMPAPERCORRECT_ANSWER");
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/exampapercorrect/correct.html?examSubId="+ExStringUtils.trimToEmpty(examSubId)+"&examId="+answer.getCourseExamPaperDetails().getCourseExam().getResourceid()+"&scoreMode="+ExStringUtils.trimToEmpty(request.getParameter("scoreMode"))+"&isFinishCorrect="+ExStringUtils.trimToEmpty(request.getParameter("isFinishCorrect")));
			} else {
				map.put("statusCode", 300);
				map.put("message", "批改失败");
			}
		}catch (Exception e) {
			logger.error("保存批阅结果出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存批阅结果失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 批量置零分
	 * @param examSubId
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/zero.html")
	public void zeroExamPapers(String examSubId, String resourceid, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(examSubId) && ExStringUtils.isNotBlank(resourceid)){			
				examPaperCorrectService.batchZeroExamPapers(examSubId, resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
			}	
		} catch (Exception e) {
			logger.error("批量置零分出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "批量置零分失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	/**
	 * 成绩列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/examresults/list.html")
	public String listExamResults(HttpServletRequest request,ModelMap model,Page page){
		Map<String,Object> condition = new HashMap<String, Object>();		
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId")); 
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo")); 
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName")); 
		
		if(ExStringUtils.isNotBlank(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(studentName)) {
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotBlank(examSubId)){
			Page examResultsList = examPaperCorrectService.findOnlineExamResulstsVoByCondition(condition, page);
			model.addAttribute("examResultsList", examResultsList);
		}		
		model.addAttribute("condition",condition);
		return "/edu3/teaching/exampapercorrect/examresults-list";
	}
	/**
	 * 提交成绩
	 * @param examSubId
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/examresults/submit.html")
	public void submitOnlineExamResults(String examSubId, String resourceid, HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(examSubId) && ExStringUtils.isNotBlank(resourceid)){			
				examPaperCorrectService.submitOnlineExamResults(examSubId, resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "提交成绩成功！");	
			}	
		} catch (Exception e) {
			logger.error("提交机考成绩出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交成绩失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));		
	}
	/**
	 * 重新批改
	 * @param examResultId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/recorrect.html")
	public String recorrectStudentExamAnswer(String examResultId, HttpServletRequest request, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(examResultId)){	
			ExamResults examResult = examResultsService.get(examResultId);
			
			StringBuffer hql = new StringBuffer();
			hql.append("from "+RecruitExamStudentAnswer.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? ")
			.append(" and courseExamPaperDetails.courseExam.examType>=4 and courseExamPaperDetails.courseExam.examType<=5 ")
			.append(" and courseExamPaperDetails.courseExam.isOnlineAnswer='Y' ")
			.append(" and courseExamPapers.course.resourceid=? order by showOrder,resourceid");
			List<RecruitExamStudentAnswer> list = recruitExamStudentAnswerService.findByHql(hql.toString(), examResult.getStudentInfo().getResourceid(),examResult.getCourse().getResourceid());
			
			model.addAttribute("examResult", examResult);
			model.addAttribute("examList", list);
			model.addAttribute("scoreMode", ExStringUtils.trimToEmpty(request.getParameter("scoreMode")));//评分模式
			model.addAttribute("lineChar", "\n");
		}		
		return "/edu3/teaching/exampapercorrect/exampapercorrect-recorrect-form";
	}
	/**
	 * 保存重新批改结果
	 * @param examResultId
	 * @param scoreMode
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/recorrect/save.html")
	public void recorrectStudentExamAnswerScore(String examResultId, String scoreMode,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String[] answerid = request.getParameterValues("answerid");
			String[] score = request.getParameterValues("score");			
			examPaperCorrectService.saveRecorreExamScore(examResultId, answerid, score);
			map.put("navTabId", "RES_TEACHING_EXAMPAPERCORRECT_RECORRECT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/exampapercorrect/recorrect.html?examResultId="+ExStringUtils.trimToEmpty(examResultId)+"&scoreMode="+ExStringUtils.trimToEmpty(scoreMode));
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
		}catch (Exception e) {
			logger.error("保存批阅结果出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存批阅分数失败:<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 复查试卷
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/review/list.html")
	public String listReViewExamPaper(HttpServletRequest request,ModelMap model,Page page){
		Map<String,Object> condition = new HashMap<String, Object>();		
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId")); 
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo")); 
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName")); 
		
		if(ExStringUtils.isBlank(examSubId)){//查询默认批次
			List<ExamSub> examSubList = examSubService.findByHql("from ExamSub examSub where isDeleted=? and batchType=? and isSpecial=? order by yearInfo.firstYear desc,term desc,resourceid", 0,"exam",Constants.BOOLEAN_NO);
			if(ExCollectionUtils.isNotEmpty(examSubList)){//查询默认批次
				examSubId = examSubList.get(0).getResourceid();
			}
		}
		if(ExStringUtils.isNotBlank(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(studentName)) {
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotBlank(examSubId) && ExStringUtils.isNotBlank(courseId)){
			condition.put("checkStatus", "4");//发布
			Page examResultsList = examPaperCorrectService.findOnlineExamResulstsVoByCondition(condition, page);
			model.addAttribute("examResultsList", examResultsList);
		}		
		model.addAttribute("condition",condition);
		return "/edu3/teaching/exampapercorrect/exampaper-review-list";
	}
	/**
	 * 复查试卷
	 * @param examResultId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exampapercorrect/review.html")
	public String reviewStudentExamAnswer(String examResultId, HttpServletRequest request, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(examResultId)){	
			ExamResults examResult = examResultsService.get(examResultId);
			
			StringBuffer hql = new StringBuffer();
			hql.append("from "+RecruitExamStudentAnswer.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? ")
			.append(" and courseExamPapers.course.resourceid=? order by showOrder,resourceid");
			List<RecruitExamStudentAnswer> list = recruitExamStudentAnswerService.findByHql(hql.toString(), examResult.getStudentInfo().getResourceid(),examResult.getCourse().getResourceid());
			
			if(ExCollectionUtils.isNotEmpty(list)){
				CourseExamPapers paper = list.get(0).getCourseExamPapers();
				List<CourseExamPaperDetails> examList = new ArrayList<CourseExamPaperDetails>(paper.getCourseExamPaperDetails());
				model.addAttribute("courseExamPaperDetails", examList);
				
				Map<String,Integer> showOrders = new HashMap<String, Integer>();//试题序号列表
				int index = 0;
				double totalScore = 0.0;//试卷总分
				for (CourseExamPaperDetails detail : examList) {
					if(null == detail.getCourseExam().getParent()){ //非材料题的小题
						totalScore += detail.getScore();
					}
					if(!CourseExam.COMPREHENSION.equals(detail.getCourseExam().getExamType())){
						showOrders.put(detail.getResourceid(), ++index);					
					}
				}
				model.addAttribute("totalExams", index);
				model.addAttribute("totalScore", totalScore);
				model.addAttribute("showOrders", showOrders);	
				
				Map<String,RecruitExamStudentAnswer> studentAnswers = new HashMap<String, RecruitExamStudentAnswer>();
				for (RecruitExamStudentAnswer answer : list) {
					studentAnswers.put(answer.getCourseExamPaperDetails().getResourceid(), answer);
				}
				model.addAttribute("studentAnswers", studentAnswers);
			}
			
			model.addAttribute("examResult", examResult);
			model.addAttribute("examList", list);
			model.addAttribute("scoreMode", ExStringUtils.trimToEmpty(request.getParameter("scoreMode")));//评分模式
			model.addAttribute("lineChar", "\n");
		}		
		return "/edu3/teaching/exampapercorrect/exampaper-review";
	}
}
