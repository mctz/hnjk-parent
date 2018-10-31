package com.hnjk.edu.recruit.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.edu.learning.model.CourseExamRulesDetails;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ICourseExamPapersService;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
import com.hnjk.edu.recruit.service.IRecruitExamLogsService;
import com.hnjk.edu.recruit.service.IRecruitExamStudentAnswerService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 入学考试日志记录服务接口实现.
 * <code>RecruitExamLogsServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-19 下午05:15:34
 * @see 
 * @version 1.0
 */
@Transactional
@Service("recruitExamLogsService")
public class RecruitExamLogsServiceImpl extends BaseServiceImpl<RecruitExamLogs> implements IRecruitExamLogsService {

	@Autowired
	@Qualifier("recruitExamStudentAnswerService")
	private IRecruitExamStudentAnswerService recruitExamStudentAnswerService;
	
	@Autowired
	@Qualifier("courseExamPapersService")
	private ICourseExamPapersService courseExamPapersService;
	
//	@Autowired
//	@Qualifier("studentLearnPlanService")
//	private IStudentLearnPlanService studentLearnPlanService;
	
//	@Autowired
//	private IStudentActiveCourseExamService studentActiveCourseExamService;
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;
		
	@Override
	@Transactional(readOnly=true)
	public Page findRecruitExamLogsByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = getRecruitExamLogsHql(condition, values);
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return findByHql(objPage, hql.toString(), values);
	}

	private StringBuffer getRecruitExamLogsHql(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer(" from "+RecruitExamLogs.class.getSimpleName()+" where isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		
		if(condition.containsKey("courseName")){//考试科目
			hql.append(" and courseName =:courseName ");
			values.put("courseName", condition.get("courseName"));
		}	
		if(condition.containsKey("brSchool")){//报名学习中心
			hql.append(" and brSchool.resourceid =:brSchool ");
			values.put("brSchool", condition.get("brSchool"));
		}
		if(condition.containsKey("branchSchool")){//学生学习中心
			hql.append(" and studentInfo.branchSchool.resourceid =:branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("recruitExamPlanId")){//考试场次
			hql.append(" and recruitExamPlan.resourceid =:recruitExamPlanId ");
			values.put("recruitExamPlanId", condition.get("recruitExamPlanId"));
		}
		if(condition.containsKey("examCertificateNo")){//准考证号
			hql.append(" and enrolleeInfo.examCertificateNo like :examCertificateNo ");
			values.put("examCertificateNo", "%"+condition.get("examCertificateNo")+"%");
		}
		if(condition.containsKey("name")){//姓名
			hql.append(" and enrolleeInfo.studentBaseInfo.name like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("notstatus")){
			hql.append(" and status <>:notstatus ");
			values.put("notstatus", condition.get("notstatus"));
		}
		if(condition.containsKey("status")){
			if("3".equals(condition.get("status").toString())){//查询断线的数据
				Date curTime = ExDateUtils.addMinutes(new Date(), -2);				
				hql.append(" and (status = 0 or status = 1) and lastUpdateTime <:curTime ");
				values.put("curTime", curTime);
			} else {
				hql.append(" and status =:status ");
				values.put("status", condition.get("status"));
			}			
		}
		if(condition.containsKey("courseId")){
			hql.append(" and examInfo.course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("examSubId")){
			hql.append(" and examInfo.examSub.resourceid =:examSubId ");
			values.put("examSubId", condition.get("examSubId"));
		}
		if(condition.containsKey("examInfoId")){
			hql.append(" and examInfo.resourceid =:examInfoId ");
			values.put("examInfoId", condition.get("examInfoId"));
		}
		if(condition.containsKey("isMachineExam")){
			if(Constants.BOOLEAN_NO.equals(condition.get("isMachineExam"))){
				hql.append(" and (examInfo.isMachineExam is null or examInfo.isMachineExam ='N') ");				
			} else {
				hql.append(" and examInfo.isMachineExam =:isMachineExam ");
				values.put("isMachineExam", condition.get("isMachineExam"));
			}			
		}
		if(condition.containsKey("studyNo")){
			hql.append(" and studentInfo.studyNo like :studyNo ");
			values.put("studyNo", "%"+condition.get("studyNo")+"%");
		}
		if(condition.containsKey("studentName")){
			hql.append(" and studentInfo.studentName like :studentName ");
			values.put("studentName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("isEntranceExam")){
			if(Constants.BOOLEAN_YES.equals(condition.get("isEntranceExam"))){//入学考查询已安排的学生
				hql.append(" and studentInfo is null and examRoomPlan.isDeleted=:isDeleted  ");
				hql.append(" and recruitExamPlan.recruitPlan.isPublished='Y' ");//默认查询开放批次的考试场次
			} else {
				hql.append(" and studentInfo is not null ");
			}
		}
		if(condition.containsKey("examStartScore")){
			hql.append(" and examScore >= :examStartScore ");
			values.put("examStartScore", condition.get("examStartScore"));
		}
		if(condition.containsKey("examEndScore")){
			hql.append(" and examScore <= :examEndScore ");
			values.put("examEndScore", condition.get("examEndScore"));
		}
		if(condition.containsKey("startTime")){
			hql.append(" and examInfo.examStartTime >= to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
			//values.put("examStartTime",ExDateUtils.convertToDateTime(condition.get("startTime").toString()));
		}
		if(condition.containsKey("endTime")){
			hql.append(" and examInfo.examEndTime <= to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
			//values.put("examEndTime",ExDateUtils.convertToDateTime(condition.get("endTime").toString()));
		}
		if(condition.containsKey("machineExamName")){
			hql.append(" and examInfo.resourceid = :machineExamName ");
			values.put("machineExamName", condition.get("machineExamName"));
		}
		return hql;
	}
	
	@Override
	public List<RecruitExamLogs> findRecruitExamLogsByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql 		  = getRecruitExamLogsHql(condition, values);
		if(condition.containsKey("orderby")){
			hql.append(" order by "+condition.get("orderby"));			
		}
		return findByHql(hql.toString(), values);
	}
	
	/*
	@Override
	public void handPapers(String[] ids,String from) throws ServiceException {
		if(ids != null && ids.length > 0){
			for (String id : ids) {
				RecruitExamLogs log = get(id);
				if(log.getStatus()==2){//已交卷
					continue;
				}
				if(ExStringUtils.isNotBlank(from) && "online".equals(from)){//网考
					Double result = recruitExamStudentAnswerService.getOnlineExamStudentAnswerResult(log.getStudentInfo().getResourceid(), log.getCourseExamPapers().getResourceid());
					List<StudentLearnPlan> plans = studentLearnPlanService.findByHql(" from " + StudentLearnPlan.class.getSimpleName() + " where isDeleted=0 and status=2 and studentInfo.resourceid=? and examInfo.resourceid=? ",log.getStudentInfo().getResourceid(),log.getExamInfo().getResourceid());
					if(plans != null && !plans.isEmpty()){
						StudentLearnPlan plan = plans.get(0);						
						log.setExamScore(result);
						
						Double activeCourseExamScore = 100.0*studentActiveCourseExamService.avgStudentActiveCourseExamResult(log.getExamInfo().getCourse().getResourceid(), log.getStudentInfo().getResourceid());
						plan.getExamResults().setWrittenScore(Double.toString(result));
						plan.getExamResults().setUsuallyScore(Double.toString(activeCourseExamScore));
						if(result<activeCourseExamScore){// 网考成绩 ＜ 随堂练习成绩，终评成绩=50%网考成绩+50%随堂练习
							plan.getExamResults().setIntegratedScore(Double.toString(BigDecimalUtil.div(result+activeCourseExamScore, 2.0, 1)));
						} else {	//网考成绩 ≧ 随堂练习成绩，终评成绩取网考成绩
							plan.getExamResults().setIntegratedScore(Double.toString(result));
						}				
					}
				} else {
					for (ExamScore score : log.getEnrolleeInfo().getExamScore()) {
						if(log.getCourseName().equals(score.getExamSubject().getCourseName())){
							Double result = recruitExamStudentAnswerService.getRecruitExamStudentAnswerResult(log.getEnrolleeInfo().getResourceid(), log.getRecruitExamPlan().getResourceid(), log.getCourseExamPapers().getResourceid());
							score.setOriginalPoint((result!=null)?result:0.0);
							break;
						}
					}
					Double totalOriginalPoint = 0.0;//自动计算总分
					for (ExamScore score : log.getEnrolleeInfo().getExamScore()) {
						if(score.getOriginalPoint() != null){
							totalOriginalPoint += score.getOriginalPoint();
						} else {
							totalOriginalPoint = null;//还未考完，不设置总分
							break;
						}
					}
					log.getEnrolleeInfo().setOriginalPoint(totalOriginalPoint!=null?Math.round(totalOriginalPoint):null);
				}				
				log.setStatus(2);//改为交卷状态
				if(log.getLogoutTime()==null){
					log.setLogoutTime(log.getEndTime());
				}
				update(log);
			}
		}
	}
	
	*/
	
	@Override
	public void continueExam(String[] ids,String from) throws ServiceException {
		if(ids != null && ids.length > 0){
			for (String id : ids) {
				RecruitExamLogs log = get(id);
				if(log.getStatus()==0){//在考学生不用处理
					continue;
				}
				if(ExStringUtils.isNotBlank(from) && "online".equals(from)){//网考
					log.setStartTime(null);
					log.setEndTime(null);
					log.setLogoutTime(null);
					log.setLoginIp(null);
					log.setLoginTime(null);
					log.setExamScore(null);
					log.setStatus(-1);
					log.setExamScore(null);
					
					List<StudentLearnPlan> plans = (List<StudentLearnPlan>) exGeneralHibernateDao.findByHql(" from " + StudentLearnPlan.class.getSimpleName() + " where isDeleted=0 and status=2 and studentInfo.resourceid=? and (teachingPlanCourse.course.resourceid=? or planoutCourse.resourceid=?) ",log.getStudentInfo().getResourceid(), log.getExamInfo().getCourse().getResourceid(),log.getExamInfo().getCourse().getResourceid());
					if (plans != null && !plans.isEmpty()) {
						StudentLearnPlan plan = plans.get(0);
						if (plan.getExamResults()!=null) {
							plan.getExamResults().setWrittenScore(null);
							plan.getExamResults().setUsuallyScore(null);
							plan.getExamResults().setIntegratedScore(null);
						}
					}
				} else {
					/*
					for (ExamScore score : log.getEnrolleeInfo().getExamScore()) {
						if(log.getCourseName().equals(score.getExamSubject().getCourseName())){
							score.setOriginalPoint(null);//清除成绩
							break;
						}
					}
					*/
					log.setStatus(1);//改为退出状态
					log.getEnrolleeInfo().setOriginalPoint(null);
				}
				update(log);
			}
		}
	}
	
	@Override
	public void reExam(String[] ids) throws ServiceException {
		/*
		if(ids != null && ids.length > 0){
			for (String id : ids) {
				RecruitExamLogs log = get(id);
				for (ExamScore score : log.getEnrolleeInfo().getExamScore()) {
					if(log.getCourseName().equals(score.getExamSubject().getCourseName())){
						score.setOriginalPoint(null);
						break;
					}
				}
				log.setStatus(-1);
				log.setStartTime(null);
				log.setEndTime(null);
				log.setLoginIp(null);
				log.setLoginTime(null);
				log.setLogoutTime(null);
				
				CourseExamRules rule = null;
				for (RecruitExamPlanDetails d : log.getRecruitExamPlan().getRecruitExamPlanDetails()) {
					if(log.getCourseName().equals(d.getCourseName())){
						rule = d.getCourseExamRules();
					}
				}
				CourseExamPapers paper = courseExamPapersService.fetchCourseExamPapersByRandom(rule, "entrance_exam", JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",log.getCourseName()));
				courseExamPapersService.save(paper);
				
				log.setCourseExamPapers(paper);//重新生成试卷
				log.getEnrolleeInfo().setOriginalPoint(null);
				update(log);
			}
		}
		*/
	}
	
	@Override
	public void reOnlineExam(String[] ids) throws ServiceException {
		if(ids != null && ids.length > 0){
			for (String id : ids) {
				RecruitExamLogs log = get(id);
				
				List<StudentLearnPlan> plans = (List<StudentLearnPlan>) exGeneralHibernateDao.findByHql(" from " + StudentLearnPlan.class.getSimpleName() + " where isDeleted=0 and status=2 and studentInfo.resourceid=? and (teachingPlanCourse.course.resourceid=? or planoutCourse.resourceid=?) ",log.getStudentInfo().getResourceid(), log.getExamInfo().getCourse().getResourceid(),log.getExamInfo().getCourse().getResourceid());
				if (plans != null && !plans.isEmpty()) {
					StudentLearnPlan plan = plans.get(0);
					ExamResults oldExamResults = plan.getExamResults();
					
					ExamResults examResults = new ExamResults();
					examResults.setCourse(oldExamResults.getCourse());
					examResults.setMajorCourseId(oldExamResults.getMajorCourseId());
					examResults.setStudentInfo(oldExamResults.getStudentInfo());
					examResults.setExamInfo(oldExamResults.getExamInfo());
					examResults.setCheckStatus("-1");
					examResults.setExamAbnormity("0");
					examResults.setExamStartTime(oldExamResults.getExamStartTime());
					examResults.setExamEndTime(oldExamResults.getExamEndTime());
					examResults.setExamsubId(oldExamResults.getExamsubId());
					examResults.setExamType(oldExamResults.getExamType());
					examResults.setExamCount(oldExamResults.getExamCount()!=null?oldExamResults.getExamCount()+1:1);
					examResults.setCreditHour(oldExamResults.getCreditHour());
					examResults.setStydyHour(oldExamResults.getStydyHour());
					examResults.setCourseScoreType(oldExamResults.getCourseScoreType());
					
					exGeneralHibernateDao.save(examResults);
					
					plan.setExamResults(examResults);//关联最新空白成绩
					exGeneralHibernateDao.update(plan);
					
					delete(log);//删除旧记录
					
					if(log.getStudentInfo().getSysUser()!=null){
						sendTipsMessage(log.getStudentInfo(),log.getExamInfo());//发送温馨提示
					}
				}									
			}			
		}
	}

	@Override
	public void reloginExam(String[] ids) throws ServiceException {
		if(ids != null && ids.length > 0){
			for (String id : ids) {
				RecruitExamLogs log = get(id);
				if(log.getStatus()!=null && (log.getStatus()==0 || log.getStatus()==1)){
					log.setStatus(-1);//设置为未登录状态
					log.setLoginIp(null);
					log.setLoginTime(null);
					log.setLogoutTime(null);
				}
			}
		}		
	}
	public void sendTipsMessage(StudentInfo studentInfo,ExamInfo examInfo) throws ServiceException {
		Message message = new Message();
//		MessageSender messageSender = new MessageSender();
		MessageReceiver messageReceiver = new MessageReceiver();		
		
		User user = SpringSecurityHelper.getCurrentUser();
		String content = studentInfo.getSysUser().getCnName()+"同学:<br/>你的"+examInfo.getCourse().getCourseName()+"网上考试已经可以重考了。";
		try {
			content += "<br/>请在 <span style='color:green;font-weight: bolder;'>"+ExDateUtils.formatDateStr(examInfo.getExamStartTime(),ExDateUtils.PATTREN_DATE_TIME) + " - "
							+ ExDateUtils.formatDateStr(examInfo.getExamEndTime(),ExDateUtils.PATTREN_DATE_TIME)+" </span> 考试时段内登录考试。";
		} catch (ParseException e) {			
		}
		message.setContent(content);
		message.setMsgTitle(studentInfo.getSysUser().getCnName()+"同学:"+examInfo.getCourse().getCourseName()+"网上考试重考提示");
		message.setMsgType("tips");
		message.setSendTime(new Date());
		message.setSender(user);
		message.setSenderName(user.getCnName());
		message.setOrgUnit(user.getOrgUnit());
		
	/*	messageSender.setMessage(message);
		messageSender.setSender(user.getCnName());
		messageSender.setSenderId(user.getResourceid());
		messageSender.setOrgUnit(user.getOrgUnit());*/
			
		messageReceiver.setMessage(message);
		messageReceiver.setReceiveType("user");
		messageReceiver.setUserNames(studentInfo.getSysUser().getUsername()+",");
		
		sysMsgService.saveMessage(message, messageReceiver,null);	
//		sysMsgService.saveMessage_old(messageSender, messageReceiver, null);
		
//		exGeneralHibernateDao.save(message);
//		exGeneralHibernateDao.save(messageSender);	
//		exGeneralHibernateDao.save(messageReceiver);
	}
	
	@Override
	public int joinCourseExamPaperDetails(String examSubId, String examInfoId) throws ServiceException{
		int size = 0;
		if(ExStringUtils.isNotBlank(examSubId) && ExStringUtils.isNotBlank(examInfoId)){			
			ExamInfo examInfo = (ExamInfo) exGeneralHibernateDao.get(ExamInfo.class, examInfoId);
			CourseExamRules rule = examInfo.getCourseExamRules();
			if(rule!=null && !"fixed".equals(examInfo.getExamPaperType())){//随机成卷
				if(ExCollectionUtils.isEmpty(rule.getCourseExamRulesDetails())){
					throw new ServiceException("课程："+examInfo.getCourse().getCourseName()+"成卷规则不合法");
				}
				for (CourseExamRulesDetails d : rule.getCourseExamRulesDetails()) {
					if(d.getExamNum()==null || d.getExamNum()<=0 || d.getExamValue()==null || d.getExamValue()<=0.0){
						throw new ServiceException("课程："+examInfo.getCourse().getCourseName()+"成卷规则不合法");
					}
				}
				
				Map<String, Object> condition = new HashMap<String, Object>();
				Map<String,Object> values =  new HashMap<String, Object>();
				condition.put("examSubId", examSubId);
				condition.put("examInfoId", ExStringUtils.trimToEmpty(examInfoId));		
				condition.put("isMachineExam", "Y");
				StringBuffer hql = getRecruitExamLogsHql(condition, values);
				hql.append(" and courseExamPapers.courseExamPaperDetails.size=1 ");
				List<RecruitExamLogs> list = findByHql(hql.toString(), values);
				
				if(ExCollectionUtils.isNotEmpty(list)){
					condition.clear();
					if(Constants.BOOLEAN_YES.equals(rule.getIsEnrolExam())){//入学考试
						condition.put("isEnrolExam", Constants.BOOLEAN_YES);
						condition.put("courseName", rule.getCourseName());
						condition.put("notInExamType", "('4','5')");
					} else {
						condition.put("isEnrolExam", Constants.BOOLEAN_NO);
						condition.put("courseId", rule.getCourse().getResourceid());
						if(ExStringUtils.isNotBlank(rule.getPaperSourse())){
							condition.put("examforms", Arrays.asList(rule.getPaperSourse().split("\\,")));
						}			
					}
					Map<String, List<CourseExam>> courseExamMap = new HashMap<String, List<CourseExam>>();
					for (CourseExamRulesDetails r : rule.getCourseExamRulesDetails()) {
						if(ExStringUtils.isNotBlank(r.getExamNodeType())){
							condition.put("examNodeType", r.getExamNodeType());
						}
						condition.put("examType",r.getExamType());
						List<CourseExam> courseExamList = courseExamService.findCourseExamByCondition(condition);
						courseExamMap.put(r.getResourceid(), courseExamList);
					}
					
					for (RecruitExamLogs recruitExamLogs : list) {			
						CourseExamPapers paper = recruitExamLogs.getCourseExamPapers();
						if(paper.getCourseExamPaperDetails().size()<2){						
							CourseExamPapers newPaper = courseExamPapersService.fetchCourseExamPapersByRandom(rule, paper.getPaperType(), paper.getPaperName(),courseExamMap);
							courseExamPapersService.save(newPaper);	
							recruitExamLogs.setCourseExamPapers(newPaper);	
							paper.setIsDeleted(1);							
						}			
					}	
					batchSaveOrUpdate(list);
					size = list.size();
				}				
			}			
		}
		return size;
	}	
	
	@Override
	public List<Map<String, Object>> findRecruitExamLogsByConditionForExport(Map<String, Object> condition) throws ServiceException{
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select u.unitname,p.examplanname,t.coursename,en.examcertificateno,b.certnum,b.name,b.mobile,en.coursegroupname ");
		sql.append(" from edu_recruit_stustates t ");
		sql.append(" join edu_recruit_enrolleeinfo en on t.enrolleeinfoid=en.resourceid ");
		sql.append(" join edu_base_student b on en.studentbaseinfoid=b.resourceid ");
		sql.append(" join edu_recruit_examplan p on t.examplanid=p.resourceid ");
		sql.append(" join edu_recruit_examroomplan r on t.examroomplanid=r.resourceid ");
		sql.append(" join hnjk_sys_unit u on en.branchschoolid=u.resourceid ");
		sql.append(" where t.isdeleted=0 and r.isdeleted=0 ");
		if(condition.containsKey("recruitExamPlanId")){
			sql.append(" and t.examplanid=? ");
			params.add(condition.get("recruitExamPlanId"));
		}
		if(condition.containsKey("brSchool")){
			sql.append(" and en.branchschoolid=? ");
			params.add(condition.get("brSchool"));
		}
		if(condition.containsKey("courseName")){
			sql.append(" and t.courseName=? ");
			params.add(condition.get("courseName"));
		}
		if(condition.containsKey("orderby")){
			sql.append(" order by  "+condition.get("orderby"));			
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), params.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 期末机考强制交卷
	 * @param ids
	 * @throws ServiceException
	 */
	@Override
	public void handFinalExamPapers(String[] ids) throws ServiceException {	
		for (String id : ids) {
			RecruitExamLogs log = get(id);
			//System.out.println(log.getStatus());
			if(log.getStatus()==0){
				Map<String, String> answerMap = getAnswerMap(log);
				System.out.println(answerMap);
				recruitExamStudentAnswerService.saveOrUpdateOnlineExamResult(answerMap, log);
			}			
		}		
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, String> getAnswerMap(RecruitExamLogs log){
		Map<String, String> answerMap = new HashMap<String, String>();
		String sql = "select p.resourceid from edu_learn_stuplan p where p.isdeleted=0 and p.studentid=:studentId and p.examinfoid=:examInfoId ";
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("studentId", log.getStudentInfo().getResourceid());
			param.put("examInfoId", log.getExamInfo().getResourceid());
			Map<String, Object> planIdMap = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, param);
			if(planIdMap!=null && !planIdMap.isEmpty() && planIdMap.get("resourceid")!=null){
				String answerStr = memcachedManager.get("_answer_"+planIdMap.get("resourceid").toString()); 	
				if(ExStringUtils.isNotBlank(answerStr)){
					List<Object> list = (List<Object>) JsonUtils.jsonToBean(answerStr);
					for (Object object : list) {
						Map<String, Object> val = (Map<String, Object>)object;
						if(answerMap.containsKey(val.get("name").toString())){
							answerMap.put(val.get("name").toString(), answerMap.get(val.get("name").toString())+(val.get("value")!=null?val.get("value").toString():""));
						} else {
							answerMap.put(val.get("name").toString(), (val.get("value")!=null?val.get("value").toString():""));
						}								
					}					
				}
			}
		} catch (Exception e) {
			logger.error("获取答案出错:{}",e.fillInStackTrace());
		}	
		return answerMap;
	}

}
