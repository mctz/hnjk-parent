package com.hnjk.edu.teaching.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.learning.model.StuActiveCourseExamCount;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.model.UsualResultsHistory;
import com.hnjk.edu.teaching.model.UsualResultsRule;
import com.hnjk.edu.teaching.service.IUsualResultsHistoryService;
import com.hnjk.edu.teaching.service.IUsualResultsRuleService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.edu.teaching.vo.UsualResultsVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;
/**
 * 学生平时成绩服务接口实现
 * <code>UsualResultsServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-12-21 下午02:19:07
 * @see 
 * @version 1.0
 */
@Transactional
@Service("usualResultsService")
public class UsualResultsServiceImpl extends BaseServiceImpl<UsualResults> implements IUsualResultsService {
	
	@Autowired
	@Qualifier("usualResultsRuleService") 
	private IUsualResultsRuleService usualResultsRuleService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;                  
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;                      
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("usualResultsHistoryService")
	private IUsualResultsHistoryService usualResultsHistoryService;

	@Override
	public void batchSaveOrUpdateUsualResults(String[] planId, String[] isDealed, String[] bbsResults, String[] exerciseResults, String[] selftestResults, String[] otherResults, String[] faceResults, String[] askQuestionResults, String[] courseExamResults) throws ServiceException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(planId!=null && planId.length>0){
			List<UsualResults> list1 = new ArrayList<UsualResults>(0);
			List<StudentLearnPlan> list2 = new ArrayList<StudentLearnPlan>(0);
			Map<String,UsualResultsRule> map = new HashMap<String,UsualResultsRule>();
			String defaultRule = CacheAppManager.getSysConfigurationByCode("usualResultsDefaultRule").getParamValue().trim();
			UsualResultsRule currentRule = null;
			if(ExStringUtils.isNotBlank(defaultRule) && defaultRule.split(":").length==3){
				currentRule = new UsualResultsRule(defaultRule.split(":"));
			}
			for (int i = 0; i < planId.length; i++) {				
				if("Y".equalsIgnoreCase(isDealed[i])){
					StudentLearnPlan plan = studentLearnPlanService.get(planId[i]);						
					UsualResults result = new UsualResults();
					if(plan.getUsualResults()!=null){
						result = plan.getUsualResults();
					}
					if(plan.getTeachingPlanCourse()!=null){
						result.setCourse(plan.getTeachingPlanCourse().getCourse());
						result.setMajorCourseId(plan.getTeachingPlanCourse().getResourceid());
					} else {
						result.setCourse(plan.getPlanoutCourse());
					}
//					result.setYearInfo(plan.getOrderExamYear());
//					result.setTerm(plan.getOrderExamTerm());
					result.setYearInfo(plan.getYearInfo());
					result.setTerm(plan.getTerm());
					result.setStudentInfo(plan.getStudentInfo());					
					result.setFillinManId(user.getResourceid());
					result.setFillinMan(user.getCnName());
					result.setFillinDate(new Date());
					if(bbsResults!=null && bbsResults[i]!=null) {
						result.setBbsResults(bbsResults[i]);
					}
					if(selftestResults!=null && selftestResults[i]!=null) {
						result.setSelftestResults(selftestResults[i]);
					}
					if(otherResults!=null && otherResults[i]!=null) {
						result.setOtherResults(otherResults[i]);
					}
					if(faceResults!=null && faceResults[i]!=null) {
						result.setFaceResults(faceResults[i]);
					}
					result.setAskQuestionResults(askQuestionResults[i]);
					result.setCourseExamResults(courseExamResults[i]);
					result.setExerciseResults(exerciseResults[i]);
					result.setStatus("0");					
					
					UsualResultsRule rule = null;
					if(currentRule!=null){
						rule = currentRule;
					}else {
						if(map.containsKey(plan.getTeachingPlanCourse().getCourse().getResourceid())){
							rule = map.get(plan.getTeachingPlanCourse().getCourse().getResourceid());
						} else {
//							rule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(plan.getExamInfo().getCourse().getResourceid(),plan.getOrderExamYear().getResourceid(),plan.getOrderExamTerm());
							rule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(plan.getTeachingPlanCourse().getCourse().getResourceid(),plan.getYearInfo().getResourceid(),plan.getTerm());
							map.put(plan.getTeachingPlanCourse().getCourse().getResourceid(), rule);
						}
					}
					if(rule!=null) {//计算总分
						result.setUsualResults(result.getUsualTotalResults(rule));
						if(currentRule==null){//无默认积分规则
							result.setResultsRuleId(rule.getResourceid());
							if(Constants.BOOLEAN_NO.equalsIgnoreCase(rule.getIsUsed())) {
								rule.setIsUsed(Constants.BOOLEAN_YES);
							}
						}
					} else {
						throw new ServiceException("课程"+plan.getTeachingPlanCourse().getCourse().getCourseName()+plan.getYearInfo().getYearName()+JstlCustomFunction.dictionaryCode2Value("CodeTerm", plan.getTerm())+"的平时分积分规则不存在");
					}
					plan.setUsualResults(result);
					list1.add(result);
					list2.add(plan);							
				}
			}
			batchSaveOrUpdate(list1);
			studentLearnPlanService.batchSaveOrUpdate(list2);
		}	
		
	}
	
	@Override
	public void submitUsualResults(String[] ids) throws ServiceException {
		if(ids!=null && ids.length>0){
			for (String id : ids) {
				StudentLearnPlan plan = studentLearnPlanService.get(id);
				if(plan.getUsualResults()!=null && "1".equals(plan.getUsualResults().getStatus())){//已提交
					throw new ServiceException("学生"+plan.getStudentInfo().getStudentName()+"的平时成绩已经提交,请不要重复操作");
				}
				if(plan.getUsualResults()==null || ExStringUtils.isBlank(plan.getUsualResults().getUsualResults())){
					throw new ServiceException("学生"+plan.getStudentInfo().getStudentName()+"的平时成绩还没有录入保存");
				}
//				if(plan.getExamResults()!=null && (ExStringUtils.isNotBlank(plan.getExamResults().getUsuallyScore())||"4".equals(plan.getExamResults().getCheckStatus()))){
//					throw new ServiceException("学生"+plan.getStudentInfo().getStudentName()+"已经存在平时成绩");
//				}
//				if(plan.getExamResults()!=null){//写入平时成绩分数
////					plan.getExamResults().setUsuallyScore(plan.getUsualResults().getUsualResults());
//					plan.getExamResults().setOnlineScore(plan.getUsualResults().getUsualResults());

				if(plan.getExamResults()!=null && plan.getExamResults().getIsDeleted() == 0){//写入平时成绩分数
					YearInfo info = null;
					String term = null;
					try {
						info = plan.getExamResults().getExamInfo().getExamSub().getYearInfo();
						term = plan.getExamResults().getExamInfo().getExamSub().getTerm();
					} catch (Exception e) {
						
					}
					if(null != info && info.getResourceid().equals(plan.getUsualResults().getYearInfo().getResourceid()) 
							&& null != term && term.equals(plan.getUsualResults().getTerm())){
						if("4".equals(plan.getExamResults().getCheckStatus())){
							throw new ServiceException("学生"+plan.getStudentInfo().getStudentName()+"成绩已发布不能改变");
						}else{
							plan.getExamResults().setUsuallyScore(plan.getUsualResults().getUsualResults());
							plan.getExamResults().setOnlineScore(plan.getUsualResults().getUsualResults());
							plan.getUsualResults().setStatus("1");
						}
						
					}else{
						plan.getExamResults().setUsuallyScore(plan.getUsualResults().getUsualResults());
						plan.getExamResults().setOnlineScore(plan.getUsualResults().getUsualResults());
						plan.getUsualResults().setStatus("1");
					}
				}else{
					plan.getUsualResults().setStatus("1");
				}
			}
		}
	}

	@Override
	public void saveOrUpdateUsualResultsAndLearnPlan(List<UsualResults> usualResultsList, List<StudentLearnPlan> planList) throws ServiceException {
		batchSaveOrUpdate(usualResultsList);
		studentLearnPlanService.batchSaveOrUpdate(planList);
	}
	
	
	/**
	 * 计算课程发布的随堂练习数
	 * @param courseId
	 * @return
	 */
	public long countCourseExamsByCourse(String courseId){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		long courseExamCount = 0;
		try {
			StringBuilder countSql = new StringBuilder(500);
			countSql.append("select count(t.resourceid) from edu_lear_courseexam t join edu_lear_exams s on t.examid=s.resourceid ")
			.append("join edu_teach_syllabustree st on st.isdeleted=0 and st.resourceid=t.syllabustreeid and s.courseid=st.courseid ")
			.append("where t.isdeleted=0 and t.ispublished='Y' and s.examtype<=5 and s.courseid=:courseId ");
			courseExamCount = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(countSql.toString(), params);
		} catch (Exception e) {
			throw new ServiceException("计算课程发布的随堂练习题目数失败:"+e);
		}
		return courseExamCount;
	}	
	/**
	 * 计算课程的随堂练习分
	 * @param courseId
	 * @param stuIds
	 * @param courseExamCount
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, BigDecimal> calculateCourseExamResults(String courseId,String stuIds,long courseExamCount)throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("courseId", courseId);
		params.put("isDeleted",0);
		params.put("yesStr",Constants.BOOLEAN_YES);
		
		StringBuffer sql = new StringBuffer();
		sql.append(" select rs.studentid,round((newCount+oldCount)*100/(oldCount+"+courseExamCount+"),1) score ");
		sql.append(" from (       ");
		sql.append("     select se.studentid, ");
		sql.append("     nvl(count( distinct case when ce.ispublished='Y' and ce.isdeleted=0  then se.courseexamid else null end),0) newCount, ");
		sql.append("     nvl(count( distinct case when not (ce.ispublished='Y' and ce.isdeleted=0 ) then se.courseexamid else null end),0) oldCount ");
		sql.append("     from edu_lear_studentcourseexam se ");
		sql.append("     join edu_roll_studentinfo s on se.studentid=s.resourceid and s.studentstatus in('11','12','18','21')");
		if (ExStringUtils.isNotEmpty(stuIds)) {
			sql.append(" and s.resourceid in  ('"+stuIds+"')");
		}
		sql.append("     join edu_lear_courseexam ce on se.courseexamid=ce.resourceid ");
		sql.append("     join edu_lear_exams es on ce.examid=es.resourceid and es.examtype<=5 and es.courseid =:courseId");
		sql.append("     where se.isdeleted=:isDeleted and se.iscorrect=:yesStr and se.result is not null  ");
		sql.append("     group by se.studentid  ");
		sql.append(" ) rs ");
		Map<String, BigDecimal> scoreMap = new HashMap<String, BigDecimal>();
		try {
			List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), params);	
			if(null != resultList && resultList.size() > 0){
				for (Map<String, Object> map : resultList) {
					scoreMap.put(map.get("studentid").toString(), (BigDecimal)(map.get("score")));
				}
			}
		} catch (Exception e) {
			throw new ServiceException("获取课程所有学生的随堂练习分失败:"+e);
		}
		return scoreMap;
	} 
	/**
	 * 计算整门课程所有学生的作业分
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, BigDecimal> calculateExerciseResults(String stuid, String yearInfoId, String term, String courseId)throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("yearInfoId", yearInfoId);
		params.put("term", term);
		StringBuffer sql = new StringBuffer();
		sql.append(" select rs.studentid,nvl(sum(rs.score),0) score ");
		sql.append(" from ( ");
		sql.append("     select p.studentid,nvl(se.result,0) score, ");
		sql.append("            row_number() over(partition by p.studentid order by se.result desc nulls last) seq ");//按分数排序 
		sql.append("     from edu_learn_stuplan p     ");
		sql.append(" join edu_teach_plancourse pe on pe.resourceid = p.PLANSOURCEID ");
		//sql.append("     join edu_teach_examinfo ei on p.examinfoid=ei.resourceid ");
		sql.append("     join edu_lear_exercisebatch b on pe.courseid=b.courseid and b.isdeleted=0 and b.status>=1 ");//and b.yearid=p.orderexamyear and b.term=p.orderexamterm
		sql.append("     left join edu_lear_studentexercise se on se.studentid=p.studentid and se.exercisebatchid=b.resourceid and se.isdeleted=0 and se.status=2 and se.exerciseid is null ");
		sql.append("     where p.isdeleted=0 and b.yearid=:yearInfoId and b.term=:term ");
		sql.append("     and pe.courseid=:courseId ");
		sql.append(" ) rs ");
		sql.append(" where rs.seq =1 ");//取分数最高的一次
		if(ExStringUtils.isNotBlank(stuid)){
			sql.append(" and rs.studentid = :stuid");
			params.put("stuid", stuid);
		}
		sql.append(" group by rs.studentid ");
		Map<String, BigDecimal> scoreMap = new HashMap<String, BigDecimal>();
		try {
			List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), params);			
			for (Map<String, Object> map : resultList) {
				scoreMap.put(map.get("studentid").toString(), (BigDecimal)(map.get("score")));
			}
		} catch (Exception e) {
			throw new ServiceException("获取课程所有学生的作业练习分失败:"+e);
		}
		return scoreMap;
	}
	/**
	 * 计算网络辅导分 
	 * @param courseId
	 * @param type 0=网络辅导,1=随堂问答
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, BigDecimal> calculateBbsResults(String userid,String courseId, int type)throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		
		String askSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();
		params.put("sectionCode", askSectionCode);
		
		StringBuffer sql = new StringBuffer();
		if(type==0){
			sql.append(" select rs.userid studentid,count(rs.resourceid) score from ( ");
		} else {
			sql.append(" select rs.userid studentid,sum(rs.scoretype) score from ( ");
		}
		sql.append("   select t.resourceid,u.userid,t.scoretype from edu_bbs_topic t ");
		sql.append(" join edu_bbs_section s1 on t.sectionid=s1.resourceid ");
		sql.append("   join edu_bbs_userinfo u on t.fillinmanid=u.resourceid ");
		sql.append("   where t.isdeleted=0 and t.courseid=:courseId ");		
		sql.append("  and s1.sectioncode"+(type==0?"<>":"=")+":sectionCode ");
		sql.append("   "+(type==0?"and t.isbest='Y'":"") +" ");
		sql.append("   union all  ");
		sql.append("   select r.resourceid,ru.userid,r.scoretype from edu_bbs_reply r join edu_bbs_topic p on r.topicid=p.resourceid ");
		sql.append(" join edu_bbs_section s2 on p.sectionid=s2.resourceid ");
		sql.append("   join edu_bbs_userinfo ru on ru.resourceid=r.replymanid ");
		sql.append("   where r.isdeleted=0 and p.courseid=:courseId ");	
		sql.append("  and s2.sectioncode"+(type==0?"<>":"=")+":sectionCode ");
		sql.append("   "+(type==0?"and p.isbest='Y'":"") +" ");
		sql.append(" ) rs ");
		if(ExStringUtils.isNotBlank(userid)){
			sql.append(" where rs.userid = :userid");
			params.put("userid", userid);
		}
		sql.append(" group by rs.userid ");
		Map<String, BigDecimal> scoreMap = new HashMap<String, BigDecimal>();
		try {
			List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), params);			
			if(null != resultList && resultList.size() > 0){
				for (Map<String, Object> map : resultList) {
					scoreMap.put(map.get("studentid").toString(), (BigDecimal)(map.get("score")));
				}
			}
		} catch (Exception e) {
			throw new ServiceException("获取课程所有学生的网络辅导分失败:"+e);
		}
		return scoreMap;
	}
	/**
	 * 保存课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveAllUsualResults(String yearInfoId, String term, String courseId) throws ServiceException {
		String defaultRule = CacheAppManager.getSysConfigurationByCode("usualResultsDefaultRule").getParamValue().trim();
		Course course 				    = (Course) exGeneralHibernateDao.get(Course.class, courseId);
		UsualResultsRule rule = null;
		if(ExStringUtils.isNotBlank(defaultRule) && defaultRule.split(":").length==3){
			rule = new UsualResultsRule(defaultRule.split(":"));
		}else {
			rule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearInfoId,term);
		}
		YearInfo yearInfo 			    = (YearInfo) exGeneralHibernateDao.get(YearInfo.class, yearInfoId);
		Map<String, Object> condition   = new HashMap<String, Object>();
		condition.put("courseId", courseId);//改为查询考试课程里的课程id,这样不会漏查计划外课程
		condition.put("yearInfo", yearInfoId);
		condition.put("term", term);
//		condition.put("status", 2);//预约考试
//		condition.put("netstudy", Constants.BOOLEAN_YES);//纯网络，过滤掉面授
		List<StudentLearnPlan> planList = studentLearnPlanService.findStudentLearnPlanByCondition(condition);
		
		if(ExCollectionUtils.isNotEmpty(planList)){
			
			User currentUser 			= null;
			try {
				currentUser  		    = SpringSecurityHelper.getCurrentUser();
			} catch (Exception e) {	}
			if(currentUser == null){//定时调度使用
				try {
					List<User> adminUsers   = userService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.like("cnName","系统管理员"));
					currentUser  		    = adminUsers.get(0);
				} catch (Exception e) {	}
			}	

			String fillinMan   			= currentUser.getCnName();
			String fillinManId 			= currentUser.getResourceid();
			Date fillinDate    			= new Date();
			
			Map<String, BigDecimal> courseExamScoreMap  = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> exerciseScoreMap 	= new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> bbsScoreMap 		= new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> askScoreMap 		= new HashMap<String, BigDecimal>();
			long courseExamCount 						= 0;
			if(rule.getCourseExamResultPer()>0){//随堂练习分
				courseExamCount 						= countCourseExamsByCourse(courseId);
				if(courseExamCount>0){
					courseExamScoreMap 					= calculateCourseExamResults(courseId,"",courseExamCount);
				}
			}
			if(rule.getExerciseResultPer()>0){//作业练习分
				exerciseScoreMap 						= calculateExerciseResults("",yearInfoId, term, courseId);
			}			
			if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){//网络辅导
				bbsScoreMap 							= calculateBbsResults("",courseId,0);
			}
			if(rule.getAskQuestionResultPer()>0  && rule.getBbsBestTopicNum()>0){//随堂问答
				askScoreMap 							= calculateBbsResults("",courseId,1);
			}
			StudentInfo studentInfo 					= null;
			String studentId 							= null;
			DecimalFormat decFormat 					= new DecimalFormat("###.#");
			List<UsualResults> usualResultsList 		= new ArrayList<UsualResults>();
			
			for (StudentLearnPlan plan : planList) {	
				
				studentInfo 							= plan.getStudentInfo();
				studentId 								= studentInfo.getResourceid();
				UsualResults usualResults 				= new UsualResults();
				if(plan.getUsualResults
						()!=null) {
					usualResults = plan.getUsualResults();
				}
			
				if("1".equals(usualResults.getStatus()) || //跳过已提交的,以及沿用平时分
					Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam()) && "0".equals(usualResults.getStatus())){
					continue;
				}

				usualResults.setCourse(course);
				usualResults.setYearInfo(yearInfo);
				usualResults.setTerm(term);
				usualResults.setStudentInfo(studentInfo);
				usualResults.setMajorCourseId(null==plan.getTeachingPlanCourse()?"":plan.getTeachingPlanCourse().getResourceid());
				usualResults.setFillinManId(fillinManId);
				usualResults.setFillinMan(fillinMan);
				usualResults.setFillinDate(fillinDate);
				usualResults.setResultsRuleId(rule.getResourceid());
				usualResults.setStatus("0");	
				
				//随堂练习分
				BigDecimal tempCourseExamScore 			= BigDecimal.valueOf(0);
				if(rule.getCourseExamResultPer()>0 && ExStringUtils.isBlank(usualResults.getCourseExamResults())){//未录入分数
					tempCourseExamScore 			    = courseExamScoreMap.get(studentId);
					tempCourseExamScore 				= (tempCourseExamScore!=null)?tempCourseExamScore:BigDecimal.valueOf(0);
					if(rule.getCourseExamCorrectPer()>0 && tempCourseExamScore.doubleValue()>=rule.getCourseExamCorrectPer()){//大于满分正确率
						tempCourseExamScore = BigDecimal.valueOf(100);
					} 					
				} else if(ExStringUtils.isNotBlank(usualResults.getCourseExamResults())){//已录入分数
					tempCourseExamScore = BigDecimal.valueOf(Double.valueOf(usualResults.getCourseExamResults()));
				}
				usualResults.setCourseExamResults(decFormat.format(tempCourseExamScore.doubleValue()));
				//作业练习分
				BigDecimal tempExerciseScore 			= BigDecimal.valueOf(0);
				if(rule.getExerciseResultPer()>0 && ExStringUtils.isBlank(usualResults.getExerciseResults())){
					tempExerciseScore 					= exerciseScoreMap.get(studentId);
					tempExerciseScore 					= (tempExerciseScore!=null)?tempExerciseScore:BigDecimal.valueOf(0);
				} else if(ExStringUtils.isNotBlank(usualResults.getExerciseResults())){
					tempExerciseScore = BigDecimal.valueOf(Double.valueOf(usualResults.getExerciseResults()));
				}
				usualResults.setExerciseResults(decFormat.format(tempExerciseScore.doubleValue()));	
				//网络辅导分
				BigDecimal tempBbsScore 				= BigDecimal.valueOf(0);
				if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0 && ExStringUtils.isBlank(usualResults.getBbsResults())){
					tempBbsScore 						= bbsScoreMap.get(studentInfo.getSysUser().getResourceid());
					tempBbsScore 						= (tempBbsScore!=null)?tempBbsScore:BigDecimal.valueOf(0);
					if(tempBbsScore.longValue()>=rule.getBbsBestTopicNum()){//大于满分发贴数
						tempBbsScore 					= BigDecimal.valueOf(100);
					} else {
						tempBbsScore 					= BigDecimal.valueOf(rule.getBbsBestTopicResult()*tempBbsScore.longValue());
					}
				} else if(ExStringUtils.isNotBlank(usualResults.getBbsResults())){
					tempBbsScore = BigDecimal.valueOf(Double.valueOf(usualResults.getBbsResults()));
				}
				usualResults.setBbsResults(decFormat.format(tempBbsScore.doubleValue()));
				//随堂问答分
				BigDecimal tempAskQuestionScore 		= BigDecimal.valueOf(0);
				if(rule.getAskQuestionResultPer()>0&& rule.getBbsBestTopicNum()>0 && ExStringUtils.isBlank(usualResults.getAskQuestionResults())){
					tempAskQuestionScore 				= askScoreMap.get(studentInfo.getSysUser().getResourceid());
					tempAskQuestionScore 				= (tempAskQuestionScore!=null)?tempAskQuestionScore:BigDecimal.valueOf(0);
					if(tempAskQuestionScore.longValue()>=rule.getBbsBestTopicNum()){//大于满分发贴数
						tempAskQuestionScore 			= BigDecimal.valueOf(100);
					} else {
						tempAskQuestionScore 			= BigDecimal.valueOf(rule.getBbsBestTopicResult()*tempAskQuestionScore.longValue());
					}
				} else if(ExStringUtils.isNotBlank(usualResults.getAskQuestionResults())){
					tempAskQuestionScore = BigDecimal.valueOf(Double.valueOf(usualResults.getAskQuestionResults()));
				}
				usualResults.setAskQuestionResults(decFormat.format(tempAskQuestionScore.doubleValue()));
				
				usualResults.setSelftestResults("0");
				usualResults.setOtherResults("0");
				usualResults.setFaceResults("0");
				//计算总分
				usualResults.setUsualResults(usualResults.getUsualTotalResults(rule));
				if(Constants.BOOLEAN_NO.equals(rule.getIsUsed())){
					rule.setIsUsed(Constants.BOOLEAN_YES);//更新为已使用状态
				}

				usualResultsList.add(usualResults);
				plan.setUsualResults(usualResults);
			}
			batchSaveOrUpdate(usualResultsList);
			studentLearnPlanService.batchSaveOrUpdate(planList);
		}		
	}
	
	/**
	 * 保存课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int saveAllUsualResultsInt(String yearInfoId, String term, String courseId) throws ServiceException {
		
		Course course 				    = (Course) exGeneralHibernateDao.get(Course.class, courseId);
		UsualResultsRule rule 		    = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId, yearInfoId, term);
		if(rule==null){
			throw new ServiceException("课程 "+course.getCourseName()+" 没有设置平时分积分规则!");
		}
		YearInfo yearInfo 			    = (YearInfo) exGeneralHibernateDao.get(YearInfo.class, yearInfoId);
		Map<String, Object> condition   = new HashMap<String, Object>();
		condition.put("examCourseId", courseId);//改为查询考试课程里的课程id,这样不会漏查计划外课程
		condition.put("examOrderYearInfo", yearInfoId);
		condition.put("examOrderTerm", term);
		//condition.put("status", 2);//预约考试 --医学院不走预约流程
		condition.put("netstudy", Constants.BOOLEAN_YES);//纯网络，过滤掉面授
		List<StudentLearnPlan> planList = studentLearnPlanService.findStudentLearnPlanByCondition(condition);
		int returnNum = 0;
		//if(ExCollectionUtils.isNotEmpty(planList)){
			
			User currentUser 			= null;
			try {
				currentUser  		    = SpringSecurityHelper.getCurrentUser();
			} catch (Exception e) {	}
			if(currentUser == null){//定时调度使用
				try {
					List<User> adminUsers   = userService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.like("cnName","系统管理员"));
					currentUser  		    = adminUsers.get(0);
				} catch (Exception e) {	}
			}	

			String fillinMan   			= currentUser.getCnName();
			String fillinManId 			= currentUser.getResourceid();
			Date fillinDate    			= new Date();
			
//			Map<String, BigDecimal> courseExamScoreMap  = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> exerciseScoreMap 	= new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> bbsScoreMap 		= new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> askScoreMap 		= new HashMap<String, BigDecimal>();
//			long courseExamCount 						= 0;
			//随堂练习分不需要重新计算，直接通过随堂联系累计得分情况表获取
//			if(rule.getCourseExamResultPer()>0){//随堂练习分
//				
//				courseExamCount 						= countCourseExamsByCourse(courseId);
//				if(courseExamCount>0){
//					courseExamScoreMap 					= calculateCourseExamResults(courseId,"",courseExamCount);
//				}
//			}
			if(rule.getExerciseResultPer()>0){//作业练习分
				exerciseScoreMap 						= calculateExerciseResults(yearInfoId, term, courseId, rule.getIsMaxScore());
			}			
			if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){//网络辅导
				bbsScoreMap 							= calculateBbsResults("",courseId,0);
			}
			if(rule.getAskQuestionResultPer()>0  && rule.getBbsBestTopicNum()>0){//随堂问答
				askScoreMap 							= calculateBbsResults("",courseId,1);
			}
			StudentInfo studentInfo 					= null;
			String studentId 							= null;
			DecimalFormat decFormat 					= new DecimalFormat("###.#");
			List<UsualResults> usualResultsList 		= new ArrayList<UsualResults>();
			
			
			for (StudentLearnPlan plan : planList) {
//				
//				Session session = exGeneralHibernateDao.getSessionFactory().openSession();
//				Transaction tx = session.beginTransaction();
				returnNum ++ ;
				studentInfo 							= plan.getStudentInfo();
				studentId 								= studentInfo.getResourceid();
				String userId                           = studentInfo.getSysUser() == null?"":studentInfo.getSysUser().getResourceid();
				UsualResults usualResults 				= new UsualResults();
				if(plan.getUsualResults()!=null) {
					usualResults = plan.getUsualResults();
				}
			
				if("1".equals(usualResults.getStatus()) || //跳过已提交的,以及沿用平时分
					Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam()) && "0".equals(usualResults.getStatus())){
					continue;
				}
//				if(
//					Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam()) && "0".equals(usualResults.getStatus())){
//					continue;
//				}

				usualResults.setCourse(course);
				usualResults.setYearInfo(yearInfo);
				usualResults.setTerm(term);
				usualResults.setStudentInfo(studentInfo);
				usualResults.setMajorCourseId(null==plan.getTeachingPlanCourse()?"":plan.getTeachingPlanCourse().getResourceid());
				usualResults.setFillinManId(fillinManId);
				usualResults.setFillinMan(fillinMan);
				usualResults.setFillinDate(fillinDate);
				usualResults.setResultsRuleId(rule.getResourceid());
				usualResults.setStatus("0");	
				//随堂练习分
				BigDecimal tempCourseExamScore 			= BigDecimal.valueOf(0);
				
				//直接通过随堂练习得分累计情况表获取随堂练习分数
				StuActiveCourseExamCount stuActiveCourseExamCount = plan.getStuActiveCourseExamCount();
				tempCourseExamScore 			    = null == stuActiveCourseExamCount?BigDecimal.valueOf(0):BigDecimal.valueOf(stuActiveCourseExamCount.getCorrectper());
				tempCourseExamScore 				= (tempCourseExamScore!=null)?tempCourseExamScore:BigDecimal.valueOf(0);
				if(rule.getCourseExamCorrectPer()>0 && tempCourseExamScore.doubleValue()>=rule.getCourseExamCorrectPer()){//大于满分正确率
					tempCourseExamScore = BigDecimal.valueOf(100);
				}
				usualResults.setCourseExamResults(decFormat.format(tempCourseExamScore.doubleValue()));
				//作业练习分
				BigDecimal tempExerciseScore 			= BigDecimal.valueOf(0);
				if(rule.getExerciseResultPer()>0 && (ExStringUtils.isBlank(usualResults.getExerciseResults())||Double.valueOf(usualResults.getExerciseResults())==0.0)){
					tempExerciseScore 					= exerciseScoreMap.get(studentId);
					tempExerciseScore 					= (tempExerciseScore!=null)?tempExerciseScore:BigDecimal.valueOf(0);
				}else if(ExStringUtils.isNotBlank(usualResults.getExerciseResults())){
					tempExerciseScore = BigDecimal.valueOf(Double.valueOf(usualResults.getExerciseResults()));
				}
//				if(rule.getExerciseResultPer()>0){
//					tempExerciseScore 					= exerciseScoreMap.get(studentId);
//					tempExerciseScore 					= (tempExerciseScore!=null)?tempExerciseScore:BigDecimal.valueOf(0);
//				}
				usualResults.setExerciseResults(decFormat.format(tempExerciseScore.doubleValue()));	
				//网络辅导分
				BigDecimal tempBbsScore 				= BigDecimal.valueOf(0);
				if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0 && (ExStringUtils.isBlank(usualResults.getBbsResults())||Double.valueOf(usualResults.getBbsResults())==0.0)){
//				if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){
					tempBbsScore 						= bbsScoreMap.get(userId);
					tempBbsScore 						= (tempBbsScore!=null)?tempBbsScore:BigDecimal.valueOf(0);
					if(tempBbsScore.longValue()>=rule.getBbsBestTopicNum()){//大于满分发贴数
						tempBbsScore 					= BigDecimal.valueOf(100);
					} else {
						tempBbsScore 					= BigDecimal.valueOf(rule.getBbsBestTopicResult()*tempBbsScore.longValue());
					}
				} else if(ExStringUtils.isNotBlank(usualResults.getBbsResults())){
					tempBbsScore = BigDecimal.valueOf(Double.valueOf(usualResults.getBbsResults()));
				}
				usualResults.setBbsResults(decFormat.format(tempBbsScore.doubleValue()));
				//随堂问答分
				BigDecimal tempAskQuestionScore 		= BigDecimal.valueOf(0);
				if(rule.getAskQuestionResultPer()>0&& rule.getBbsBestTopicNum()>0 && (ExStringUtils.isBlank(usualResults.getAskQuestionResults())||Double.valueOf(usualResults.getAskQuestionResults())==0.0)){
//				if(rule.getAskQuestionResultPer()>0&& rule.getBbsBestTopicNum()>0 ){
					
					tempAskQuestionScore 				= askScoreMap.get(userId);
					tempAskQuestionScore 				= (tempAskQuestionScore!=null)?tempAskQuestionScore:BigDecimal.valueOf(0);
					tempAskQuestionScore                = (tempAskQuestionScore.compareTo(BigDecimal.valueOf(0))==-1)?BigDecimal.valueOf(0):tempAskQuestionScore;
					if(tempAskQuestionScore.longValue()>=rule.getBbsBestTopicNum()){//大于满分发贴数
						tempAskQuestionScore 			= BigDecimal.valueOf(100);
					} else {
						tempAskQuestionScore 			= BigDecimal.valueOf(rule.getBbsBestTopicResult()*tempAskQuestionScore.longValue());
					}
				} else if(ExStringUtils.isNotBlank(usualResults.getAskQuestionResults())){
					tempAskQuestionScore = BigDecimal.valueOf(Double.valueOf(usualResults.getAskQuestionResults()));
				}
				usualResults.setAskQuestionResults(decFormat.format(tempAskQuestionScore.doubleValue()));
				
				usualResults.setSelftestResults("0");
				usualResults.setOtherResults("0");
				usualResults.setFaceResults("0");
				//计算总分
				usualResults.setUsualResults(usualResults.getUsualTotalResults(rule));
				if(Constants.BOOLEAN_NO.equals(rule.getIsUsed())){
					rule.setIsUsed(Constants.BOOLEAN_YES);//更新为已使用状态
				}
				
//				usualResultsList.add(usualResults);
				
				plan.setUsualResults(usualResults);	
				saveOrUpdate(usualResults);
				studentLearnPlanService.saveOrUpdate(plan);
				
				//记入日志表
				UsualResultsHistory history = new UsualResultsHistory();
				history.setUsualResultsHistory(usualResults,plan.getResourceid());
				usualResultsHistoryService.save(history);
//				tx.commit();
//				session.close();
			}
//			batchSaveOrUpdate(usualResultsList);
//			studentLearnPlanService.batchSaveOrUpdate(planList);
		//}		
//		Session session1 = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
//		session1.close();
		return returnNum;
	}
	
	/**
	 * 计算整门课程所有学生的作业分
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	public Map<String, BigDecimal> calculateExerciseResults(String stuid,String yearInfoId, String term,String courseId,String isMaxScore)throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseId", courseId);
		params.put("yearInfoId", yearInfoId);
		params.put("term", term);
		StringBuffer sql = new StringBuffer();
		sql.append(" select rs.studentid,nvl(rs.score,0) score ");
		sql.append(" from ( ");
		sql.append("     select p.studentid,nvl(se.result,0) score, ");
		sql.append("            row_number() over(partition by p.studentid order by se.result desc nulls last) seq ");//按分数排序 
		sql.append("     from edu_learn_stuplan p     ");
		sql.append("     join edu_teach_examinfo ei on p.examinfoid=ei.resourceid ");
		sql.append("     join edu_lear_exercisebatch b on ei.courseid=b.courseid and b.isdeleted=0 and b.status>=1 and b.yearid=p.orderexamyear and b.term=p.orderexamterm  ");
		sql.append("     left join edu_lear_studentexercise se on se.studentid=p.studentid and se.exercisebatchid=b.resourceid and se.isdeleted=0 and se.status=2 and se.exerciseid is null ");
		sql.append("     where p.isdeleted=0 and p.orderexamyear=:yearInfoId and p.orderexamterm=:term ");
		sql.append("     and ei.courseid=:courseId ");
		sql.append(" ) rs ");
		sql.append(" where rs.seq=1 ");//取分数最高的
		//sql.append(" group by rs.studentid ");
		Map<String, BigDecimal> scoreMap = new HashMap<String, BigDecimal>();
		try {
			List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), params);			
			for (Map<String, Object> map : resultList) { 
				/*
				scoreMap.put(map.get("studentid").toString(), (BigDecimal) map.get("SCORE"));
				*/
				
				/*20141011 添加作业平时分取分规则*/
				Double originalExerciseResults = avgStudentExerciseResult(courseId, map.get("studentid").toString(),yearInfoId,term,isMaxScore);
				BigDecimal studentExerciseResult = new BigDecimal(originalExerciseResults);//double转bigDecimal
				scoreMap.put(map.get("studentid").toString(), studentExerciseResult);
			}
		} catch (Exception e) {
			throw new ServiceException("获取课程所有学生的作业练习分失败:"+e);
		}
		return scoreMap;
	}
	
	
	/*
	 * 1.如果本学期尚未布置作业，则进度框显示0%。
	 * 2.如果本学期只布置了一次作业，则取作业成绩的50%为作业进度，例如作业得分为90，则作业进度为45%；如果某次作业尚未评分，则此次作业得分视为0分。
	 * 3.如果本学期布置了两次作业，则取两次作业得分的平均分作为作业进度；如果某次作业尚未评分，则此次作业得分视为0分。
	 * 4.如果本学期布置了三次（或以上）的作业，则取作业得分中最高的两个，计算这两次的平均分作为作业进度；如果某次作业尚未评分，则此次作业得分视为0分。
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.IStudentExerciseService#avgStudentExerciseResult(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public Double avgStudentExerciseResult(String courseId, String studentInfoId,String yearId, String term, String isMaxScore) throws ServiceException {
		/*
		String hql1 = " select count(*) from "+ExerciseBatch.class.getName()+" s where s.isDeleted=0 and s.course.resourceid=? and s.yearInfo.resourceid=? and s.term=? ";
		Long total = exGeneralHibernateDao.findUnique(hql1, courseId, yearId, term);
		total = total!=null?total:0;
		if(total.intValue()==0)
			return 0.0;
		String hql2 = " select sum(s.result) from "+StudentExercise.class.getName()+" s where s.isDeleted=0 and s.exerciseBatch.course.resourceid=? and s.studentInfo.resourceid=? and s.exerciseBatch.yearInfo.resourceid=? and s.exerciseBatch.term=? and s.exercise is null ";
		Double totalSum = exGeneralHibernateDao.findUnique(hql2, courseId,studentInfoId, yearId, term);
		totalSum = totalSum!=null?totalSum:0.0;
		return totalSum/total;*/
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("courseId", courseId);
		param.put("studentId", studentInfoId);
		param.put("yearId", yearId);
		param.put("term", term);
		//查询时，把未做或未批改的作业视为0分
		String sql = " select nvl(decode(nvl(st.status,0),0,0,st.result),0) score,nvl(st.status,0) status from edu_lear_exercisebatch t left join edu_lear_studentexercise st on st.exercisebatchid=t.resourceid and st.studentid=:studentId and st.isdeleted=0 and st.exerciseid is null where t.isdeleted=0 and t.status>=1 and t.courseid=:courseId and t.yearid=:yearId and t.term=:term order by status desc, score desc ";
		Double result = 0.0;
		try {
			List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, param);
			if(ExCollectionUtils.isNotEmpty(list)){
				
				/*20141011 根据作业平时分取分规则 返回得分：isMaxScore-Y(默认)取作业最高分  isMaxScore-N取作业平均分  */
				//20141011 取作业平均分
				if("N".equals(isMaxScore)){
					BigDecimal avg = new BigDecimal(0.0);
					if(list.size()>=2){
						for (int i = 0; i < list.size(); i++) {
							if(i<2){ // 取前两次,适合于一次的情况
								BigDecimal score = (BigDecimal)list.get(i).get("SCORE");
								avg = avg.add(score);
							}						
						}
						result = avg.divide(new BigDecimal(2.0), 1, BigDecimal.ROUND_HALF_UP).doubleValue();//取平均分
					}else{
						result = ((BigDecimal)list.get(0).get("SCORE")).doubleValue();
					}
				}
				//20141011 取作业最高分
				else{
					BigDecimal max = new BigDecimal(0.0);
					for (int i = 0; i < list.size(); i++) {
						BigDecimal score = (BigDecimal)list.get(i).get("SCORE");
						if(score.compareTo(max)==1){
							max = score;
						}			
					}
					result = max.doubleValue();
				}
				
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * 提交课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @param teacherId
	 * @param unitId
	 * @param type
	 * @throws ServiceException
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public int submitAllUsualResults(String yearInfoId, String term,String courseId,String teacherId,String unitId, String type) throws Exception {
		int submitNum  = 0;		
		do{
			Course course 				  = (Course) exGeneralHibernateDao.get(Course.class, courseId);
			UsualResultsRule rule 		  = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId, yearInfoId, term);
			if(rule==null){
				throw new ServiceException("课程 "+course.getCourseName()+" 没有设置平时分积分规则!");
			}
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("courseId", courseId);
			condition.put("yearInfoId", yearInfoId);
			condition.put("term", term);
			
			// 检查是否有要保存的记录
			StringBuffer hasRecordSql= new StringBuffer("select ur.resourceid from (select so.resourceid stuid,substr(tt.term,7,1) term,y.resourceid yearInfoId,tt.courseid courseid ");
			hasRecordSql.append("  from edu_roll_studentinfo so, edu_teach_timetable tt, edu_base_year y");
			hasRecordSql.append("  where so.isdeleted = 0 and tt.isdeleted = 0 and so.studentstatus in ('11', '16', '25') and tt.classesid=so.classesid and tt.courseid=:courseId ");
			hasRecordSql.append("  and substr(tt.term,7,1)=:term and substr(tt.term,0,4)=y.firstyear and y.isdeleted=0 and y.resourceid=:yearInfoId ");
			 if(ExStringUtils.isNotEmpty(teacherId)){
				 hasRecordSql.append(" and tt.teacherid=:teacherId ");
				 condition.put("teacherId", teacherId);
			 }
			
			 if(ExStringUtils.isNotEmpty(unitId)){
				 hasRecordSql.append(" and so.branchschoolid=:unitId ");
				 condition.put("unitId", unitId);
			}
		   hasRecordSql.append(")  sut  ");
			hasRecordSql.append("  left join edu_teach_usualresults ur on ur.isdeleted=0 and ur.term=sut.term and ur.courseid=sut.courseid and ur.yearid=sut.yearInfoId  and ur.studentid=sut.stuid ");
			hasRecordSql.append("  where ur.status is null ");
			
			/*StringBuffer hasRecordSql= new StringBuffer("select ur.resourceid from edu_teach_usualresults ur join edu_roll_studentinfo si on si.resourceid=ur.studentid");
			hasRecordSql.append("  where ur.isdeleted=0 and si.studentstatus in ('11', '16', '25') and si.isdeleted=0");
			hasRecordSql.append(" and ur.yearid=:yearInfoId  and ur.term=:term  and ur.courseid=:courseId ");
				//班级上课时间（年度+学期）
			hasRecordSql.append(" and ur.studentid in(select so.resourceid from edu_roll_studentinfo so,edu_teach_timetable tt,edu_base_year y where so.isdeleted=0 and tt.isdeleted=0 and so.studentstatus in('11','16','25') ");
			hasRecordSql.append(" and tt.classesid=so.classesid and tt.courseid=:courseId and substr(tt.term,7,1)=:term and substr(tt.term,0,4)=y.firstyear and y.isdeleted=0 and y.resourceid=:yearInfoId ");
			
			 if(ExStringUtils.isNotEmpty(teacherId)){
				 hasRecordSql.append(" and tt.teacherid=:teacherId ");
				 condition.put("teacherId", teacherId);
			 }
			
			 if(ExStringUtils.isNotEmpty(unitId)){
				 hasRecordSql.append(" and so.branchschoolid=:unitId ");
				 condition.put("unitId", unitId);
			}
		   hasRecordSql.append(")  ");*/
			List<Map<String, Object>> hasResultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hasRecordSql.toString(), condition);			
			if(hasResultList!=null && hasResultList.size()>0){
				throw new ServiceException("请先保存成绩再进行提交！");
			}
			
			StringBuffer sql  = new StringBuffer();
			sql.append(" select t.studentid,t.usualresultsid,t.examresultsid,u.usualresults from edu_learn_stuplan t ");
			sql.append(" join edu_teach_examinfo ei on t.examinfoid=ei.resourceid ");
			sql.append(" join edu_teach_usualresults u on t.usualresultsid=u.resourceid ");
			sql.append(" join edu_teach_examresults r on t.examresultsid=r.resourceid ");
			sql.append(" where t.isdeleted=0 and ei.examCourseType in (0,1,3) ");
			sql.append(" and u.status='0'   ");//平时成绩未提交	,纯网络	
			sql.append(" and t.yearid=:yearInfoId and t.term=:term ");
			sql.append(" and ei.courseid=:courseId ");
			sql.append(" and t.studentid in ");
			sql.append(" (select so.resourceid from edu_roll_studentinfo so,edu_teach_timetable tt,edu_base_year y where so.isdeleted=0 and tt.isdeleted=0 and so.studentstatus in('11','16','25')");
			sql.append(" and tt.classesid=so.classesid and tt.courseid=:courseId and substr(tt.term,7,1)=:term and substr(tt.term,0,4)=y.firstyear and y.isdeleted=0 and y.resourceid=:yearInfoId ");
			if(ExStringUtils.isNotEmpty(teacherId)){
				sql.append(" and tt.teacherid=:teacherId ");
				condition.put("teacherId", teacherId);
			}
			
			if(ExStringUtils.isNotEmpty(unitId)){
				sql.append(" and so.branchschoolid=:unitId ");
				condition.put("unitId", unitId);
			}
			
			sql.append(")  ");
			
			String uSql = sql.toString();
			sql.append(" and nvl(r.checkstatus,-1)<3 ");//课程成绩未审核发布
			if("submit".equals(type)){
				// 更新成绩
				List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);			
				if(ExCollectionUtils.isNotEmpty(resultList)){
					List<Object[]> params = new ArrayList<Object[]>();
					for (Map<String, Object> map : resultList) {
						params.add(new Object[]{map.get("usualresults"),map.get("examresultsid")});					
					}
					//baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(" update edu_teach_usualresults us set us.status='1' where exists ("+uSql+" and us.resourceid=t.usualresultsid)", condition);
					baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("update edu_teach_examresults r set r.usuallyscore=? where r.resourceid=?", params);
					/*int[] counts  = baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("update edu_teach_examresults r set r.usuallyscore=? where r.resourceid=?", params);
				if(counts!=null){
					submitNum = counts.length;
				}		*/		
				}
				// 更新平时成绩
				StringBuffer update_sql = new StringBuffer("update edu_teach_usualresults us set us.status='1' where us.yearid=:yearInfoId and us.term=:term and us.courseid=:courseId and us.studentid in "); 
				update_sql.append(" (select so.resourceid from edu_roll_studentinfo so,edu_teach_timetable tt,edu_base_year y where so.isdeleted=0 and tt.isdeleted=0  and so.studentstatus in('11','16','25')");
				update_sql.append(" and tt.classesid=so.classesid and tt.courseid=:courseId and substr(tt.term,7,1)=:term and substr(tt.term,0,4)=y.firstyear and y.isdeleted=0 and y.resourceid=:yearInfoId ");
				if(ExStringUtils.isNotEmpty(teacherId)){
					update_sql.append(" and tt.teacherid=:teacherId ");
					condition.put("teacherId", teacherId);
				}
				
				if(ExStringUtils.isNotEmpty(unitId)){
					update_sql.append(" and so.branchschoolid=:unitId ");
					condition.put("unitId", unitId);
				}
				
				update_sql.append(")  ");
				submitNum = baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(update_sql.toString(), condition);
			}else {
				List<Map<String, Object>> resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);			
				if(ExCollectionUtils.isNotEmpty(resultList)){
					List<Object[]> params = new ArrayList<Object[]>();
					for (Map<String, Object> map : resultList) {
						params.add(new Object[]{map.get("usualresults"),map.get("examresultsid")});		
					}
					baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(" update edu_teach_usualresults us set us.status='1' where exists ("+uSql+" and us.resourceid=t.usualresultsid)", condition);
					int[] counts  = baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("update edu_teach_examresults r set r.usuallyscore=? where r.resourceid=?", params);
					if(counts!=null){
						submitNum = counts.length;
					}				
				}
			}
			
		} while(false);

		return submitNum;
	}
	
	@Override
	public Page findUsualResultsVoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = getUsualResultsVoSqlByCondition(condition, params);	
		if(objPage.isOrderBySetted()){
			sql.append(" order by "+objPage.getOrderBy()+" "+objPage.getOrder());
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(), params.toArray(), UsualResultsVo.class);
	}

	private StringBuffer getUsualResultsVoSqlByCondition(Map<String, Object> condition, List<Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.resourceid studentLearnPlanId,t.studentid,y.yearname,t.term term,c.coursename, ");
		sql.append(" s.studyno,s.studentname,t.usualresultsid,u.bbsresults,u.askquestionresults,u.courseexamresults,u.exerciseresults, ");
		sql.append(" u.selftestresults,u.otherresults,u.faceresults,u.usualresults,u.status,s.sysuserid,t.orderexamyear yearInfoId,pe.courseid ");
		sql.append(" from edu_learn_stuplan t ");
		sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid and s.isdeleted=0");
//		sql.append(" left join edu_base_year y on t.orderexamyear=y.resourceid ");
//		sql.append(" join edu_teach_examinfo ei on t.examinfoid=ei.resourceid ");
		sql.append(" join EDU_TEACH_PLANCOURSE pe on pe.RESOURCEID = t.PLANSOURCEID and pe.isdeleted=0");
		sql.append(" join edu_base_course c on pe.courseid=c.resourceid and c.isdeleted=0");
		sql.append(" join edu_base_year y on t.yearid=y.resourceid and y.isdeleted=0");
		sql.append(" join edu_teach_guiplan gp on gp.planid=s.teachplanid and gp.gradeid=s.gradeid and gp.isdeleted=0 ");
		sql.append(" join edu_teach_coursestatus cs on cs.guiplanid=gp.resourceid and cs.plancourseid=pe.resourceid and cs.schoolids=s.BRANCHSCHOOLID and cs.isdeleted=0 and cs.teachtype='networkTeach' ");
		sql.append(" left join edu_teach_usualresults u on t.usualresultsid=u.resourceid ");
		if(condition.containsKey("teacherId")){
			sql.append(" join (select tt.classesid,tt.plancourseid,tt.courseid,tt.teacherid,tt.term from  EDU_TEACH_TIMETABLE tt where tt.isdeleted=0 group by tt.classesid,tt.plancourseid,tt.courseid,tt.teacherid,tt.term)te");
		    sql.append(" on s.classesid = te.classesid and te.courseid = c.resourceid and t.plansourceid = te.plancourseid and te.term like '%'||y.firstyear||'_0'||t.term||'%' ");
		    sql.append(" and te.teacherid = ? ");
		    params.add(condition.get("teacherId"));
		}
		sql.append(" where t.isdeleted=0   ");
		
//		if(condition.containsKey("examOrderYearInfo")){
//			sql.append(" and t.orderexamyear=? ");
//			params.add(condition.get("examOrderYearInfo"));
//		}
//		if(condition.containsKey("examOrderTerm")){
//			sql.append(" and t.orderexamterm=? ");
//			params.add(condition.get("examOrderTerm"));
//		}
		
		/*屏蔽上面2条条件，现针对每个学生没门课程每个学期会有一条该学期的分数*/
		if(condition.containsKey("examOrderYearInfo")){
			sql.append(" and t.YEARID=? ");
			params.add(condition.get("examOrderYearInfo"));
		}
		if(condition.containsKey("examOrderTerm")){
			sql.append(" and t.term=? ");
			params.add(condition.get("examOrderTerm"));
		}
		
		if(condition.containsKey("courseId")){
			sql.append(" and c.resourceid=? ");
			params.add(condition.get("courseId"));
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and s.studyno=? ");
			params.add(condition.get("studyNo"));
		}
		if(condition.containsKey("studentStatus")){
			sql.append(" and s.studentStatus in("+condition.get("studentStatus")+") ");
		}
		if(condition.containsKey("studentId")){
			sql.append(" and s.resourceid=? ");
			params.add(condition.get("studentId"));
		}
		if(condition.containsKey("name")){
			sql.append(" and s.studentName like ? ");
			params.add("%"+condition.get("name")+"%");
		}
		if(condition.containsKey("usualStatus")){
			if("-1".equals(condition.get("usualStatus"))){
				sql.append(" and u.status is null ");
			} else {
				sql.append(" and u.status=? ");
				params.add(condition.get("usualStatus"));
			}
		}
		if(condition.containsKey("brSchoolid")){
			sql.append(" and s.branchschoolid=? ");
			params.add(condition.get("brSchoolid"));
		}else if (condition.containsKey("unitId")){
			sql.append(" and s.branchschoolid=? ");
			params.add(condition.get("unitId"));
		}
		if(condition.containsKey("classesIds")){
			sql.append(" and s.classesid in ?");
			params.add("'"+condition.get("classesIds")+"'");
		}
		/*if(condition.containsKey("teacherId")){ //老师
			sql.append("   and exists (select * from EDU_TEACH_TIMETABLE te where te.isdeleted = 0 and s.classesid = te.classesid and te.teacherid = ? and te.courseid = c.resourceid and te.term like  '%'||y.firstyear||'_0'||t.term||'%') ");
			params.add(condition.get("teacherId"));
		}*/
		/*if(condition.containsKey("teacherId")){ //老师 注释代码：保证和学习情况统计界面人数统一
			sql.append(" and te.resourceid is not null ");
		}*/
		sql.append(" group by t.resourceid,t.studentid,y.yearname,y.firstyear,t.term,c.coursename,c.coursecode,s.studyno,s.studentname, ")
		.append(" t.usualresultsid,u.bbsresults,u.askquestionresults,u.courseexamresults,u.exerciseresults,u.selftestresults, ")
		.append(" u.otherresults,u.faceresults,u.usualresults,u.status,s.sysuserid,t.orderexamyear,t.orderexamterm,pe.courseid ");
		return sql;
	}
	
	@Override
	public List<UsualResultsVo> findUsualResultsVoByCondition(Map<String, Object> condition) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = getUsualResultsVoSqlByCondition(condition, params);
		List<UsualResultsVo> list = new ArrayList<UsualResultsVo>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), params.toArray(), UsualResultsVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 定时计算并提交平时成绩（在学习时间结束后）
	 * 注：计算网络教育类课程的成绩
	 * @return
	 */
	@Override
	public String timingCalculateAndSubmitUsualResults()throws ServiceException {
		
		LearningTimeSetting  setting 		    = null;
		Date curTime                            = ExDateUtils.getCurrentDateTime();
		Date cachTime                           = null;
		StringBuffer returnStr       		    = new StringBuffer("");
		String yearInfoId              			= "";
		String term      						= "";
		int counts                     	    	= 0 ;
		try {
			cachTime                            = ExDateUtils.parseDate(CacheAppManager.getSysConfigurationByCode("usualResultsCalculateTime").getParamValue(),ExDateUtils.PATTREN_DATE);
		} catch (Exception e1) {
			cachTime                            = curTime;
		}
		try {
			//saveAllUsualResults("ff8080811ec87d11011ec9f5ed9c1216","1","ff8080811ec87d11011ed2ef2f85444d");
			//submitAllUsualResults("ff8080811ec87d11011ec9f5ed9c1216","1","ff8080811ec87d11011ed2ef2f85444d");
		
			curTime                             = ExDateUtils.formatDate(curTime,ExDateUtils.PATTREN_DATE);//当前系统日期
			
			List<Grade> grades 			 		= (List<Grade>)exGeneralHibernateDao.findByCriteria(Grade.class, Restrictions.eq("isDeleted",0),Restrictions.eq("isDefaultGrade",Constants.BOOLEAN_YES));

			if (null!=grades&& !grades.isEmpty()) {
				yearInfoId                      = grades.get(0).getYearInfo().getResourceid();
				term                            = grades.get(0).getTerm();
				
				List<LearningTimeSetting> sets  = (List<LearningTimeSetting>)exGeneralHibernateDao.findByCriteria(LearningTimeSetting.class, Restrictions.eq("isDeleted",0),Restrictions.eq("yearInfo.resourceid",yearInfoId),Restrictions.eq("term",term));
				if(null!=sets&&!sets.isEmpty()) {
					setting = sets.get(0);
				}
			}	
			//学习时间结束第二天凌晨计算平时分,或者是全局参数中设置的时间
			if ((null!=setting&&setting.getEndTime()!=null&&
				 curTime.getTime()  == ExDateUtils.formatDate(ExDateUtils.addDays(setting.getEndTime(),1),ExDateUtils.PATTREN_DATE).getTime())||
				 cachTime.getTime() == curTime.getTime() ) {
				
				List<Map<String,Object>> cids   = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select distinct i.courseid courseid,count(stp.resourceid) counts from edu_learn_stuplan stp inner join edu_teach_examinfo i on stp.examinfoid = i.resourceid where stp.isdeleted = ? and stp.orderexamyear = ? and stp.orderexamterm = ? group by i.courseid order by counts ", new Object []{0,yearInfoId,term});
				//List<Map<String,Object>> list   = cids.subList(0,302);
				
				for (Map<String,Object> map : cids) {

					saveAllUsualResults(yearInfoId,term,map.get("courseid").toString());
					counts += submitAllUsualResults(yearInfoId,term,map.get("courseid").toString(),null,null,"timing");
				}
				returnStr.append("计算并提交平时成绩成功，共"+counts+"条记录！");
			}else {
				returnStr.append("未设置学习时间,或者当前时间跟学习结束时间不是同一天！");
			}
		} catch (Exception e) {
			String msg 					  		 = "定时计算并提交平时成绩出错：{}"+e.fillInStackTrace();
			List<User> adminUsers         		 = userService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.like("cnName","系统管理员"));
			sysMsgService.sendMsg(adminUsers.get(0).getResourceid(),adminUsers.get(0).getCnName(),adminUsers.get(0).getOrgUnit(),
								  "tips","<font color='red'>定时计算并提交平时成绩出错</font>",msg,"ROLE_ADMINISTRATOR","role","","timingCalculateAndSubmitUsualResults");
			logger.debug(msg);
			returnStr.append(msg);
			e.printStackTrace();
			throw new ServiceException(msg);
		
		}
	
		return returnStr.toString();
	}
	
	/**
	 * 网络面授类课程学生平时成绩的计算-成绩录入审核过程中使用
	 * 注:2012.12.12 AB类课程成绩录入   中指出AB类课程的平时分只包括随堂练习，且不按照平时分计分规则比例计算，既随堂练习=平时分总分
	 * @param yearInfoId   年度ID
	 * @param term         学期
	 * @param courseId     课程ID
	 * @param stuIds       学生ID
	 * @return
	 */
	@Override
	public Map<String,BigDecimal> calculateNetsidestudyUsualResults( String courseId,String stuIds) {

		Map<String,BigDecimal> scoreMap = new HashMap<String,BigDecimal>();

		long courseExamCount 			= 0;
		courseExamCount 	 			= countCourseExamsByCourse(courseId);
		if(courseExamCount>0){
			scoreMap 		 			= calculateCourseExamResults(courseId,stuIds, courseExamCount);
		}

		return scoreMap;
	}
	
	/**
	 * 计算单个学生单门课程平时成绩
	 * @param yearInfoId
	 * @param term
	 * @param courseId
	 * @throws ServiceException
	 */
	@Override
	public void saveSpecificUsualResults(StudentInfo stu,String yearInfoId, String term, String courseId) throws ServiceException {
		if(null == stu){
			throw new ServiceException("无法识别的学生!");
		}
		String studentId 							= stu.getResourceid();
		
		User user = stu.getSysUser();
		if(null == user){
			throw new ServiceException("学生"+stu.getStudentName()+"("+stu.getStudyNo()+")未绑定用户信息!");
		}
		
		Course course 				    = (Course) exGeneralHibernateDao.get(Course.class, courseId);
		if(null == course){
			throw new ServiceException("无法识别的课程!");
		}
		
		YearInfo yearInfo 			    = (YearInfo) exGeneralHibernateDao.get(YearInfo.class, yearInfoId);
		if(null == yearInfo){
			throw new ServiceException("无法识别的年度!");
		}
		
		UsualResultsRule rule 		    = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId, yearInfoId, term);
		if(null == rule){
			throw new ServiceException("课程 "+course.getCourseName()+" 没有设置平时分积分规则，请联系教务员!");
		}
		
		
		Map<String, Object> condition   = new HashMap<String, Object>();
		condition.put("courseId", courseId);//改为查询考试课程里的课程id,这样不会漏查计划外课程
		condition.put("studentId", studentId);
		List<StudentLearnPlan> planList = studentLearnPlanService.findStudentLearnPlanByCondition(condition);
		
		if(ExCollectionUtils.isNotEmpty(planList)){
			User currentUser 			= null;
			try {
				currentUser  		    = SpringSecurityHelper.getCurrentUser();
			} catch (Exception e) {	}
			if(currentUser == null){
				try {
					List<User> adminUsers   = userService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.like("cnName","系统管理员"));
					currentUser  		    = adminUsers.get(0);
				} catch (Exception e) {	}
			}	

			String fillinMan   			= currentUser.getCnName();
			String fillinManId 			= currentUser.getResourceid();
			Date fillinDate    			= new Date();
			
			Map<String, BigDecimal> courseExamScoreMap  = new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> exerciseScoreMap 	= new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> bbsScoreMap 		= new HashMap<String, BigDecimal>();
			Map<String, BigDecimal> askScoreMap 		= new HashMap<String, BigDecimal>();
			long courseExamCount 						= 0;
			if(rule.getCourseExamResultPer()>0){//随堂练习分
				courseExamCount 						= countCourseExamsByCourse(courseId);
				if(courseExamCount>0){
					courseExamScoreMap 					= calculateCourseExamResults(courseId,studentId,courseExamCount);
				}
			}
			if(rule.getExerciseResultPer()>0){//作业练习分
				exerciseScoreMap 						= calculateExerciseResults(studentId,yearInfoId, term, courseId);
			}			
			if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){//网络辅导
				bbsScoreMap 							= calculateBbsResults(user.getResourceid(),courseId,0);
			}
			if(rule.getAskQuestionResultPer()>0  && rule.getBbsBestTopicNum()>0){//随堂问答
				askScoreMap 							= calculateBbsResults(user.getResourceid(),courseId,1);
			}
			DecimalFormat decFormat 					= new DecimalFormat("###.#");
			List<UsualResults> usualResultsList 		= new ArrayList<UsualResults>();
			if(null != planList && planList.size() > 0){
				for (StudentLearnPlan plan : planList) {	
					UsualResults usualResults 				= new UsualResults();
					if(plan.getUsualResults()!=null) //学习计划里绑定了平时成绩直接获取
					{
						usualResults = plan.getUsualResults();
					}
				
					if(Constants.BOOLEAN_NO.equals(plan.getIsRedoCourseExam()) || "1".equals(usualResults.getStatus())){//跳过沿用平时分 
						continue;
					}

					usualResults.setCourse(course);
					usualResults.setYearInfo(yearInfo);
					usualResults.setTerm(term);
					usualResults.setStudentInfo(stu);
					usualResults.setMajorCourseId(null==plan.getTeachingPlanCourse()?"":plan.getTeachingPlanCourse().getResourceid());
					usualResults.setFillinManId(fillinManId);
					usualResults.setFillinMan(fillinMan);
					usualResults.setFillinDate(fillinDate);
					usualResults.setResultsRuleId(rule.getResourceid());
					//usualResults.setStatus("0");	
					
					//随堂练习分
					BigDecimal tempCourseExamScore 			= BigDecimal.valueOf(0);
					if(rule.getCourseExamResultPer()>0){//未录入分数 && ExStringUtils.isBlank(usualResults.getCourseExamResults())
						tempCourseExamScore 			    = courseExamScoreMap.get(studentId);
						tempCourseExamScore 				= (tempCourseExamScore!=null)?tempCourseExamScore:BigDecimal.valueOf(0);
						if(rule.getCourseExamCorrectPer()>0 && tempCourseExamScore.doubleValue()>=rule.getCourseExamCorrectPer()){//大于满分正确率
							tempCourseExamScore = BigDecimal.valueOf(100);
						} 					
					} 
//					else if(ExStringUtils.isNotBlank(usualResults.getCourseExamResults())){//已录入分数
//						tempCourseExamScore = BigDecimal.valueOf(Double.valueOf(usualResults.getCourseExamResults()));
//					}
					usualResults.setCourseExamResults(decFormat.format(tempCourseExamScore.doubleValue()));
					//作业练习分
					BigDecimal tempExerciseScore 			= BigDecimal.valueOf(0);
					if(rule.getExerciseResultPer()>0){//&& ExStringUtils.isBlank(usualResults.getExerciseResults())
						tempExerciseScore 					= exerciseScoreMap.get(studentId);
						tempExerciseScore 					= (tempExerciseScore!=null)?tempExerciseScore:BigDecimal.valueOf(0);
					} 
//					else if(ExStringUtils.isNotBlank(usualResults.getExerciseResults())){
//						tempExerciseScore = BigDecimal.valueOf(Double.valueOf(usualResults.getExerciseResults()));
//					}
					usualResults.setExerciseResults(decFormat.format(tempExerciseScore.doubleValue()));	
					//网络辅导分
					BigDecimal tempBbsScore 				= BigDecimal.valueOf(0);
					if(rule.getBbsResultPer()>0 && rule.getBbsBestTopicNum()>0){ //&& ExStringUtils.isBlank(usualResults.getBbsResults())
						tempBbsScore 						= bbsScoreMap.get(user.getResourceid());
						tempBbsScore 						= (tempBbsScore!=null)?tempBbsScore:BigDecimal.valueOf(0);
						if(tempBbsScore.longValue()>=rule.getBbsBestTopicNum()){//大于满分发贴数
							tempBbsScore 					= BigDecimal.valueOf(100);
						} else {
							tempBbsScore 					= BigDecimal.valueOf(rule.getBbsBestTopicResult()*tempBbsScore.longValue());
						}
					} 
//					else if(ExStringUtils.isNotBlank(usualResults.getBbsResults())){
//						tempBbsScore = BigDecimal.valueOf(Double.valueOf(usualResults.getBbsResults()));
//					}
					usualResults.setBbsResults(decFormat.format(tempBbsScore.doubleValue()));
					//随堂问答分
					BigDecimal tempAskQuestionScore 		= BigDecimal.valueOf(0);
					if(rule.getAskQuestionResultPer()>0&& rule.getBbsBestTopicNum()>0){//&& ExStringUtils.isBlank(usualResults.getAskQuestionResults())
						tempAskQuestionScore 				= askScoreMap.get(user.getResourceid());
						tempAskQuestionScore 				= (tempAskQuestionScore!=null)?tempAskQuestionScore:BigDecimal.valueOf(0);
						if(tempAskQuestionScore.longValue()>=rule.getBbsBestTopicNum()){//大于满分发贴数
							tempAskQuestionScore 			= BigDecimal.valueOf(100);
						} else {
							tempAskQuestionScore 			= BigDecimal.valueOf(rule.getBbsBestTopicResult()*tempAskQuestionScore.longValue());
						}
					} 
//					else if(ExStringUtils.isNotBlank(usualResults.getAskQuestionResults())){
//						tempAskQuestionScore = BigDecimal.valueOf(Double.valueOf(usualResults.getAskQuestionResults()));
//					}
					usualResults.setAskQuestionResults(decFormat.format(tempAskQuestionScore.doubleValue()));
					
					usualResults.setSelftestResults("0");
					usualResults.setOtherResults("0");
					usualResults.setFaceResults("0");
					//计算总分
					usualResults.setUsualResults(usualResults.getUsualTotalResults(rule));
					if(Constants.BOOLEAN_NO.equals(rule.getIsUsed())){
						rule.setIsUsed(Constants.BOOLEAN_YES);//更新为已使用状态
					}

					usualResultsList.add(usualResults);
					plan.setUsualResults(usualResults);	
				}
				batchSaveOrUpdate(usualResultsList);
				studentLearnPlanService.batchSaveOrUpdate(planList);
			}
		} else {
			throw new ServiceException("学生"+stu.getStudentName()+"课程 "+course.getCourseName()+" 没有学习计划!");
		}	
	}

	/**
	 * 根据条件获取平时成绩
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<UsualResults> findByCondition(Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer("from "+UsualResults.class.getSimpleName()+" ur where ur.isDeleted=0 ");
		Map<String, Object> params = new HashMap<String, Object>();
		
		if(condition.containsKey("studentId")){
			hql.append(" and ur.studentInfo.resourceid=:studentId ");
			params.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("courseId")){
			hql.append(" and ur.course.resourceid=:courseId ");
			params.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("yearInfoId")){
			hql.append(" and ur.yearInfo.resourceid=:yearInfoId ");
			params.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and ur.term=:term ");
			params.put("term", condition.get("term"));
		}
		if(condition.containsKey("status")){
			hql.append(" and ur.status=:status ");
			params.put("status", condition.get("status"));
		}
		
		return findByHql(hql.toString(), params);
	}

	/**
	 * 获取学生网上学习情况-分页
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findLearningInfoByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = getLearningInfoByConditionSql(condition, params);	
		if(page.isOrderBySetted()){
			sql.append(" order by "+page.getOrderBy()+" "+page.getOrder());
		}
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), params.toArray(), UsualResultsVo.class);
	}
	
	@Override
	public List<UsualResultsVo> findLearningInfoListByCondition(Map<String, Object> condition) throws Exception {
		List<Object> params = new ArrayList<Object>();
		StringBuffer sql = getLearningInfoByConditionSql(condition, params);
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), params.toArray(),UsualResultsVo.class);
	}
	
	/**
	 * 获取学生网上学习情况SQL
	 * @param condition
	 * @param params
	 * @return
	 * @throws ServiceException
	 */
	private StringBuffer getLearningInfoByConditionSql(Map<String, Object> condition, List<Object> params) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.resourceid studentLearnPlanId,t.studentid,y.yearname,t.term term,c.coursename,s.studyno,s.studentname, ");
		sql.append(" us.askquestionresults,us.courseexamresults,us.exerciseresults,us.usualresults,us.status,pe.courseid, ");
		sql.append(" u.unitname,majorname,gradename,classesname,ur.askquestionresultper,ur.courseexamresultper,ur.exerciseresultper ");
		sql.append(" from edu_learn_stuplan t ");
		sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid ");
		sql.append(" join EDU_TEACH_PLANCOURSE pe on pe.RESOURCEID = t.PLANSOURCEID");
		sql.append(" join edu_base_course c on pe.courseid=c.resourceid and c.isdeleted=0 and c.hasresource='Y' ");
		sql.append(" join hnjk_sys_unit u on u.isdeleted=0 and u.resourceid=s.branchschoolid ");
		sql.append(" join edu_base_major m on m.isdeleted=0 and m.resourceid=s.majorid ");
		sql.append(" join edu_base_grade g on g.isdeleted=0 and g.resourceid=s.gradeid ");
		sql.append(" join edu_roll_classes cl on cl.isdeleted=0 and cl.resourceid=s.classesid ");
		sql.append(" join edu_base_year y on t.yearid=y.resourceid ");
		//开课记录
		sql.append(" join edu_teach_guiplan gp on gp.planid=s.teachplanid and gp.gradeid=s.gradeid and gp.isdeleted=0 ");
		sql.append(" join edu_teach_coursestatus cs on cs.guiplanid=gp.resourceid and cs.plancourseid=pe.resourceid and cs.schoolids=s.BRANCHSCHOOLID and cs.isdeleted=0 and cs.teachtype='networkTeach' ");
		sql.append(" left join edu_teach_usualresults us on t.usualresultsid=us.resourceid ");
		sql.append(" left join edu_teach_uresultsrule ur on ur.yearid=t.yearid and ur.term=t.term and ur.isdeleted=0 and ur.isused='Y' and ur.courseid=pe.courseid ");
		if(condition.containsKey("teacherId")){
			sql.append(" join (select tt.classesid,tt.plancourseid,tt.courseid,tt.teacherid,tt.term from  EDU_TEACH_TIMETABLE tt where tt.isdeleted=0 group by tt.classesid,tt.plancourseid,tt.courseid,tt.teacherid,tt.term)te");
		    sql.append(" on s.classesid = te.classesid and te.courseid = c.resourceid and t.plansourceid = te.plancourseid and te.term like '%'||y.firstyear||'_0'||t.term||'%' ");
		    sql.append(" and te.teacherid = ? ");
		    params.add(condition.get("teacherId"));
		}
		sql.append(" where t.isdeleted=0   ");
		
		if(condition.containsKey("examOrderYearInfo")){
			sql.append(" and t.YEARID=? ");
			params.add(condition.get("examOrderYearInfo"));
		}
		if(condition.containsKey("examOrderTerm")){
			sql.append(" and t.term=? ");
			params.add(condition.get("examOrderTerm"));
		}
		if(condition.containsKey("courseId")){
			sql.append(" and c.resourceid=? ");
			params.add(condition.get("courseId"));
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and s.studyno=? ");
			params.add(condition.get("studyNo"));
		}
		if(condition.containsKey("studentId")){
			sql.append(" and s.resourceid=? ");
			params.add(condition.get("studentId"));
		}
		if(condition.containsKey("name")){
			sql.append(" and s.studentName like ? ");
			params.add("%"+condition.get("name")+"%");
		}
		if(condition.containsKey("usualStatus")){
			if("-1".equals(condition.get("usualStatus"))){
				sql.append(" and us.status is null ");
			}else{
				sql.append(" and us.status=? ");
				params.add(condition.get("usualStatus"));
			}
		}
		if(condition.containsKey("branchSchool")){
			sql.append(" and s.branchschoolid=? ");
			params.add(condition.get("branchSchool"));
		}else if(condition.containsKey("unitId")){
			sql.append(" and s.branchschoolid=? ");
			params.add(condition.get("unitId"));
		}
		if(condition.containsKey("majorId")){
			sql.append(" and s.majorid=? ");
			params.add(condition.get("majorId"));
		}
		if(condition.containsKey("classicId")){
			sql.append(" and s.classicid=? ");
			params.add(condition.get("classicId"));
		}
		if(condition.containsKey("schoolType")){
			sql.append(" and s.teachingtype=? ");
			params.add(condition.get("schoolType"));
		}
		if(condition.containsKey("gradeId")){
			sql.append(" and s.gradeid=? ");
			params.add(condition.get("gradeId"));
		}
		if(condition.containsKey("classesId")){
			sql.append(" and s.classesid=? ");
			params.add(condition.get("classesId"));
		}
		if(condition.containsKey("studentStatus")){
			sql.append(" and s.studentStatus in("+condition.get("studentStatus")+") ");
		}
		if(condition.containsKey("classesIds")){
			sql.append(" and s.classesid in ('"+condition.get("classesIds")+"') ");
		}
		
		return sql;
	}
}
