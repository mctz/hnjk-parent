package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ElectiveExamResults;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.IElectiveExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.INoExamApplyService;
import com.hnjk.edu.teaching.service.IStateExamResultsService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.taglib.JstlCustomFunction;

/**
 * 
 * <code>学生成绩统计、列表Service实现</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-5-18 上午10:54:50
 * @see 
 * @version 1.0
 */
@Service("studentExamResultsService")
@Transactional(readOnly=true)
public class StudentExamResultsServiceImpl extends BaseServiceImpl<ExamResults> implements IStudentExamResultsService{
	
	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;
	
//	@Autowired
//	@Qualifier("equcourseservice")
//	private IEqucourseService equcourseservice;
	
//	@Autowired
//	@Qualifier("stateExamCourseService")
//	private IStateExamCourseService stateExamCourseService;

	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
//	@Autowired
//	@Qualifier("learningJDBCService")
//	private ILearningJDBCService learningJDBCService;
//	
	@Autowired
	@Qualifier("noexamapplyservice")
	INoExamApplyService noexamapplyservice;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;
	
	@Autowired
	@Qualifier("electiveExamResultsService")
	private IElectiveExamResultsService electiveExamResultsService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService ;
	
	/**
	 * 获取学生的所有成绩--后台管理员使用，查出学生所有成绩，除了刚预约生成的成绩类型
	 * @param studentInfo
	 * @param examCount
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentExamResultsVo> studentExamResultsList(StudentInfo studentInfo,Integer examCount) throws  Exception {
		
		List<StudentExamResultsVo> returnList   = new LinkedList<StudentExamResultsVo>();
		List <StudentExamResultsVo> resultsList = new ArrayList<StudentExamResultsVo>();
		
		if (null!=studentInfo.getTeachingPlan() && 
			null!=studentInfo.getTeachingPlan().getTeachingPlanCourses() && 
			!studentInfo.getTeachingPlan().getTeachingPlanCourses().isEmpty()) {
			
			Map<String,TeachingPlanCourse> tpc  = getStudentTeachingPlanCourse(studentInfo);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("flag", "");
			paramMap.put("examCount", examCount);
			//学生成绩--计划内\外课程成绩
			List<StudentExamResultsVo> erList   = getStudentAllExamResultsVo(studentInfo,tpc,paramMap); 
			//统考、学位课成绩VO
			List <StudentExamResultsVo> serList = getStatExamResultsVo(studentInfo);
			//免修、免考成绩
			List<StudentExamResultsVo> nel      = getNoExamResultsVo(studentInfo,tpc);
			//加入选修课成绩
			//List<ElectiveExamResults> electiveList = electiveExamResultsService.studentExamResultsList(studentInfo);
			//List<StudentExamResultsVo> tempElectives = new ArrayList<StudentExamResultsVo>();
			//for (ElectiveExamResults results : electiveList) {
			//	tempElectives.add(getVoByElectiveExamResults(results));
			//}
			//处理等效课程
			//resultsList						= getEqucourseList(serList,erList,studentInfo,resultsList);

			if (null!=serList && serList.size()>0) {
				resultsList.addAll(serList);
			}
			if (null!=erList  && erList.size()>0) {
				resultsList.addAll(erList);
			}
			if (null!=erList  && nel.size()>0) {
				resultsList.addAll(nel);
			}
			//if (null!=tempElectives  && tempElectives.size()>0)		resultsList.addAll(tempElectives);
			
			returnList = setInCreditHour(resultsList,tpc,"");
			
		}

		return returnList;
	}
	
	private StudentExamResultsVo getVoByElectiveExamResults(ElectiveExamResults results) {
		StudentExamResultsVo resultsVo = new StudentExamResultsVo();
		resultsVo.setExamSubId(results.getExamSub().getResourceid());
		resultsVo.setBatchName(results.getExamSub().getBatchName());
		resultsVo.setCourseId(results.getCourse().getResourceid());
		resultsVo.setCourseName(results.getCourse().getCourseName());
		resultsVo.setStydyHour(results.getCourse().getPlanoutStudyHour().toString());
		resultsVo.setCourseTypeCode(results.getCourse().getCourseType());
		resultsVo.setCourseNature("2010");//选修课
		resultsVo.setInCreditHour(results.getCourse().getPlanoutCreditHour());
		resultsVo.setIntegratedScore(results.getIntegratedScore());
		resultsVo.setIntegratedScoreL(Long.valueOf(results.getIntegratedScore()));
		resultsVo.setCheckStatusCode("4");
		resultsVo.setIsPass("Y");
		resultsVo.setIsmakeupexam("N");
		return resultsVo;
	}
	
	/**
	 * （旧）获取学生的所有成绩:
	 W0519A2：【查询与打印成绩单】打印成绩单修改
	 * @param studentInfo
	 * @param flag
	 * @return
	 * @throws ServiceException
	 */
	public List<StudentExamResultsVo> studentExamResultsList(StudentInfo studentInfo,String flag)throws  Exception {
		
		List<StudentExamResultsVo> returnList   = new LinkedList<StudentExamResultsVo>();
		List <StudentExamResultsVo> resultsList = new ArrayList<StudentExamResultsVo>();
		
		if (null!=studentInfo.getTeachingPlan() && 
			null!=studentInfo.getTeachingPlan().getTeachingPlanCourses() && 
			!studentInfo.getTeachingPlan().getTeachingPlanCourses().isEmpty()) {
			
			Map<String,TeachingPlanCourse> tpc  = getStudentTeachingPlanCourse(studentInfo);
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("flag", flag);
			//学生成绩--计划内\外课程成绩
			List<StudentExamResultsVo> erList   = getStudentAllExamResultsVo(studentInfo,tpc,paramMap); 
			//统考、学位课成绩VO
			List <StudentExamResultsVo> serList = getStatExamResultsVo(studentInfo);
			//免修、免考成绩
			List<StudentExamResultsVo> nel      = getNoExamResultsVo(studentInfo,tpc);
			//处理等效课程
			//resultsList						= getEqucourseList(serList,erList,studentInfo,resultsList);

			if (null!=serList && serList.size()>0) {
				resultsList.addAll(serList);
			}
			if (null!=erList  && erList.size()>0) {
				resultsList.addAll(erList);
			}
			if (null!=erList  && nel.size()>0) {
				resultsList.addAll(nel);
			}
			
			returnList = setInCreditHour(resultsList,tpc,flag);
			
		}

		return returnList;
	}
	
	/**
	 * （新）获取学生的所有成绩:
	 W0519A2：【查询与打印成绩单】打印成绩单修改
	 * @param studentInfo
	 * @param flag
	 * @param examCount 选修课成绩
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentExamResultsVo> studentExamResultsList(StudentInfo studentInfo, String flag, Integer examCount)throws  Exception {
		
		List<StudentExamResultsVo> returnList   = new LinkedList<StudentExamResultsVo>();
		List <StudentExamResultsVo> resultsList = new ArrayList<StudentExamResultsVo>();
		
		if (null!=studentInfo.getTeachingPlan() && 
			null!=studentInfo.getTeachingPlan().getTeachingPlanCourses() && 
			!studentInfo.getTeachingPlan().getTeachingPlanCourses().isEmpty()) {
			
			Map<String,TeachingPlanCourse> tpc  = getStudentTeachingPlanCourse(studentInfo);
			Map<String, Object> paraMap =  new HashMap<String, Object>();
			paraMap.put("flag", flag);
			paraMap.put("examCount", examCount);
			//学生成绩--计划内\外课程成绩
			List<StudentExamResultsVo> erList   = getStudentAllExamResultsVo(studentInfo,tpc,paraMap); 
			//统考、学位课成绩VO
			List <StudentExamResultsVo> serList = getStatExamResultsVo(studentInfo);
			//免修、免考成绩
			List<StudentExamResultsVo> nel      = getNoExamResultsVo(studentInfo,tpc);
			//处理等效课程
			//resultsList						= getEqucourseList(serList,erList,studentInfo,resultsList);

			if (null!=serList && serList.size()>0) {
				resultsList.addAll(serList);
			}
			if (null!=erList  && erList.size()>0) {
				resultsList.addAll(erList);
			}
			if (null!=erList  && nel.size()>0) {
				resultsList.addAll(nel);
			}
			
			returnList = setInCreditHour(resultsList,tpc,flag);
			
		}

		return returnList;
	}
	
	
	/**
	 * 获取学生所有发布的有效成绩
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> studentPublishedExamResultsList(Map<String, Object> condition) {
		List<Map<String, Object>> results =  new ArrayList<Map<String,Object>>();		
		StringBuffer sql 				  = new StringBuffer();
		//					成绩ID			学生ID		课程ID		课程名			是否等级成绩			专业ID			是否计划外		课程类型				综合成绩			平时成绩			卷面成绩			学分			学时
		sql.append("  select t.resourceid,t.studentid,t.courseid, c.coursename,t.majorcourseid,t.isoutplancourse ,t.coursescoretype,t.integratedscore,t.usuallyscore,t.writtenscore ,t.credithour,t.stydyhour,t.examtype")
		    .append("    from edu_teach_examresults t")
		    .append(" inner join edu_base_course c on t.courseid = c.resourceid ")
		    .append("   where t.isdeleted    = 0")
		    .append("     and t.checkstatus  = '4'")
		    .append("     and t.examabnormity= '0' ");
		if(!condition.containsKey("printStatus")){
			sql.append("     and t.integratedscore is not null ");
		}
		if (condition.containsKey("studentId")) {
			sql.append(" and t.studentid    =:studentId  ");
		}
		if (condition.containsKey("courseIds")) {
			sql.append(" and t.courseid in(:courseIds)  ");
		}
		//-------------学籍信息-------------
		sql.append(" and  exists (")
		   .append("	 select info.resourceid from edu_roll_studentinfo info,edu_base_student stu ")
		   .append("      where info.isdeleted = 0 and info.resourceid = t.studentid")
		   .append("        and info.studentbaseinfoid = stu.resourceid");
		if (condition.containsKey("gradeId")) {
			sql.append("    and info.GRADEID=:gradeId");
		}
		if (condition.containsKey("majorId")) {
			sql.append("    and info.MAJORID=:majorId");
		}
		if (condition.containsKey("brSchoolId")) {
			sql.append("    and info.BRANCHSCHOOLID=:brSchoolId");
		}
		if (condition.containsKey("classic")) {
			sql.append("    and info.CLASSICID=:classic");
		}
		if (condition.containsKey("studyNo")) {
			sql.append("    and info.STUDYNO=:studyNo");
		}
		if (condition.containsKey("stuName")) {
			sql.append("    and stu.NAME=:stuName");
		}
		if (condition.containsKey("studentId")) {
			sql.append("    and info.resourceid=:studentId  ");
		}
		sql.append(" )");
		//-------------学籍信息-------------
		sql.append(" union select '',eer.studentid,eer.courseid,c.coursename,'','Y',eer.courseScoreType,eer.integratedscore,'','',c.planoutcredithour,c.planoutstudyhour,c.examtype");
		sql.append(" from edu_teach_electiveexamresults eer join edu_base_course c on c.resourceid=eer.courseid where eer.studentid=:studentId");
		sql.append(" order by studentid,courseid");

		try {
			results                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
			ScoreEncryptionDecryptionUtil util 	   = ScoreEncryptionDecryptionUtil.getInstance();
			for (Map<String, Object> map:results) {
				//String integratedscore             = (String)map.get("INTEGRATEDSCORE");
				String studentId                   = (String)map.get("STUDENTID");				
				BigDecimal credithour              = null==map.get("CREDITHOUR")?new BigDecimal("0"):(BigDecimal)map.get("CREDITHOUR");
				String credithourStr               = credithour.toString();
				boolean isDouble                   = ExStringUtils.containsAny(credithourStr,".".toCharArray());
				if ((isDouble&&ExStringUtils.endsWith(credithourStr, "0"))||isDouble==false) {//学分为.0的，或没有小数位的显示为整数
					map.put("credithour", credithour.intValue());
				}else {
					map.put("credithour", credithour.doubleValue());
				}
				if(ExStringUtils.isBlank(map.get("resourceid")==null?"":map.get("resourceid").toString())){//选修课
					map.put("INTEGRATEDSCORE",(String)map.get("INTEGRATEDSCORE"));
					map.put("WRITTENSCORE", (String)map.get("WRITTENSCORE"));
					map.put("USUALLYSCORE", (String)map.get("USUALLYSCORE"));
				}else{
					map.put("INTEGRATEDSCORE", util.decrypt(studentId, (String)map.get("INTEGRATEDSCORE")));
					map.put("WRITTENSCORE", util.decrypt(studentId, (String)map.get("WRITTENSCORE")));
					map.put("USUALLYSCORE", util.decrypt(studentId, (String)map.get("USUALLYSCORE")));
				}
			}

		} catch (Exception e) {
			logger.error("查询学生所有有效的发布的成绩出错:{}"+e.fillInStackTrace());
		}
		return results;
	}
	
		
	/**
	 * 获取学生已获取的学分
	 * @param stu
	 * @return
	 * @throws Exception
	 */
	@Override
	public StudentExamResultsVo getStudentFinishedCreditHour(StudentInfo stu) throws Exception {
		Map<String,Object> paraMap            = new HashMap<String, Object>();
		paraMap.put("studentId", stu.getResourceid());
		//学生的所有有效成绩列表
		List<Map<String, Object>>  resultsMap = studentPublishedExamResultsList(paraMap);
		//List<Map<String,Object>> scl          = learningJDBCService.findStatCouseExamResults(stu.getResourceid());
		//学生的所有免修免考课程成绩
		//List<Map<String,Object>> ncl  		  = learningJDBCService.findNoExamCourseResults(stu.getResourceid());
		//教学计划课程
		TeachingPlan teachingPlan             = stu.getTeachingPlan();
		List<TeachingPlanCourse> planCourseList = new ArrayList<TeachingPlanCourse>(teachingPlan.getTeachingPlanCourses());
		
		return getStudentFinishedCreditHour(stu, resultsMap, Collections.EMPTY_LIST, Collections.EMPTY_LIST, planCourseList);
	}
	
	/*
	 * 获取学生已取得学分，by hzg
	 * 用户登录后获取学分
	 */
	@Override
	public StudentExamResultsVo getStudentFinishedCreditHour(StudentInfo stu, List<Map<String, Object>>  resultsMap,
															 List<Map<String,Object>> scl, List<Map<String,Object>> ncl,
															 List<TeachingPlanCourse> planCourseList) throws Exception {
		StudentExamResultsVo vo = new StudentExamResultsVo();
		if (null!=stu) {
			
			Map<String,Object>  statCourseId      = new HashMap<String, Object>(); //统考课程ID
			Map<String,Object>  statCourseRs  	  = new HashMap<String, Object>(); //统考课程成绩
			Map<String,Object> noExamCourseRs 	  = new HashMap<String, Object>(); //免修免考成绩 
			//学籍对象
			//StudentInfo stu                       = (StudentInfo)condition.get("studentInfo");
			//paraMap.put("studentId", stu.getResourceid());
			//学籍对应的教学计划
			//TeachingPlan teachingPlan             = stu.getTeachingPlan();                   
			//学生的所有有效成绩列表
			//List<Map<String, Object>>  resultsMap = studentPublishedExamResultsList(paraMap);
			//学生的所有统考课程成绩
			//List<Map<String,Object>> scl          = learningJDBCService.findStatCouseExamResults(stu.getResourceid());
			//学生的所有免修免考课程成绩
			//List<Map<String,Object>> ncl  		  = learningJDBCService.findNoExamCourseResults(stu.getResourceid());
			//教学计划课程
			//Set<TeachingPlanCourse> planCourseList = teachingPlan.getTeachingPlanCourses();
			
			//通过的成绩
			List<Map<String, Object>> maxScore    = sortExamResultList(resultsMap);
			//计划内课程成绩
			List<Map<String, Object>> planScore   = new ArrayList<Map<String,Object>>();
			//统考课程成绩
			if (null!=scl && !scl.isEmpty()) {
				for (Map<String,Object> rs1:scl) {
					statCourseRs.put(rs1.get("PLANCOURSEID").toString(),rs1);
					statCourseId.put(rs1.get("courseid").toString(),rs1);
				}
			}
			//免修免考成绩 
			if (null!=ncl && !ncl.isEmpty()) {
				for (Map<String,Object> rs2:ncl) {
					noExamCourseRs.put(rs2.get("COURSEID").toString(), rs2);
				}
			}
			
			// 取得学分总和
			Double total_credithour   	          	= 0D;
			// 取得必修学分总和
			Double require_total_credithour 		= 0D;
			// 取得选修学分总和
			//Double minor_total_credithour           = 0D;
			// 计划内学分总和
			Double inplan_total_credihour           = 0D;
			// 总分数
			//Double total_score                      = 0D;
			//已修必修课数
			int compulsoryed                      = 0;
			
			for(TeachingPlanCourse planCourse:planCourseList){//遍历教学计划课程
				//测试学分计算用途代码
    			/*if ("多媒体技术".equals(planCourse.getCourseName())) {
					System.out.println("========="+planCourse.getResourceid());
				}*/
				//System.out.println(planCourse.getCourseName()+"-------当前学分:"+total_credithour);
				//如果学生统考课程成绩不为空则显示统考课程成绩
				if (null!=statCourseRs.get(planCourse.getResourceid())) {
					
					total_credithour             += planCourse.getCreditHour();
					inplan_total_credihour       += planCourse.getCreditHour();
					if ("11".equals(planCourse.getCourseType())||"thesis".equals(planCourse.getCourseType())) {
						require_total_credithour += planCourse.getCreditHour();
						compulsoryed+=1;
					}
					continue;
					
				//如果学生免修免考成绩不为空则显示免修免考成绩
				}else if(null!=noExamCourseRs.get(ExStringUtils.isNotEmpty(planCourse.getCourseId())?planCourse.getCourseId():planCourse.getCourse().getResourceid())) {
					
					total_credithour             += planCourse.getCreditHour();
					inplan_total_credihour       += planCourse.getCreditHour();
					if ("11".equals(planCourse.getCourseType())||"thesis".equals(planCourse.getCourseType())) {
						require_total_credithour += planCourse.getCreditHour();
						compulsoryed+=1;
					}
					continue;
				//非统考、免修成绩 	
				}else{
					
					for (Map<String, Object> map :maxScore) {
						String examResultsCourseId = (String) map.get("COURSEID");
			
						/*String courseName          = (String)map.get("COURSENAME");
						if ("多媒体技术".equals(courseName)) {
							System.out.println("========="+planCourse.getResourceid());
						}*/
						String integratedscore     = (String)map.get("INTEGRATEDSCORE");
						String coursescoretype     = (String)map.get("COURSESCORETYPE");
						String score100            = convertScore(coursescoretype,integratedscore);
						Double score100D           = 0.0;
						if (ExStringUtils.isNotEmpty(score100)) {
							score100D              = Double.parseDouble(score100);
						}	
						String isPass              = isPassScore(coursescoretype, score100D);
							
						if ("Y".equals(isPass)&&examResultsCourseId.equals(ExStringUtils.isNotEmpty(planCourse.getCourseId())?planCourse.getCourseId():planCourse.getCourse().getResourceid())) {
							if (planCourse.getCreditHour() != null) {
								total_credithour      += planCourse.getCreditHour();
								inplan_total_credihour+= planCourse.getCreditHour();
								if ("11".equals(planCourse.getCourseType())||"thesis".equals(planCourse.getCourseType())) {
									require_total_credithour += planCourse.getCreditHour();
									compulsoryed             +=1;
								}
							}
							planScore.add(map);
						}
					}
				}
				//测试学分计算用途代码
				//System.out.println(planCourse.getCourseName()+"-------获得学分:"+total_credithour);
			}
			
			//--------------------------------------------所有成绩中去除教学计划课程之后，如果还有成绩就当计划外课程成绩处理--------------------------------------------
			maxScore.removeAll(planScore);
			StringBuffer outPlanCourseId = new StringBuffer();
			if (maxScore.size()>0) {
				for (Map<String, Object> map :maxScore) {
					
					String integratedscore     = (String)map.get("INTEGRATEDSCORE");
					String coursescoretype     = (String)map.get("COURSESCORETYPE");
					String score100            = convertScore(coursescoretype,integratedscore);
					Double score100D           = 0.0;
					if (ExStringUtils.isNotEmpty(score100)) {
						score100D              = Double.parseDouble(score100);
					}	
					String isPass              = isPassScore(coursescoretype, score100D);
					if ("Y".equals(isPass)){
						String examResultsCourseId        = (String) map.get("COURSEID");
						if (statCourseId.containsKey(examResultsCourseId)||noExamCourseRs.containsKey(examResultsCourseId)) {
							continue;
						}
						outPlanCourseId.append(",'"+examResultsCourseId+"'");
					}
				}
			}
			if (ExStringUtils.isNotEmpty(outPlanCourseId.toString())) {
				//FIXED by hzg 优化写法
				//String sql   				   = " select c.planoutcredithour from edu_base_course c where c.isdeleted = 0 and c.resourceid in  ("+outPlanCourseId.toString().substring(1)+")";
				//List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, new HashMap());				
				//for (Map<String, Object> map :list) {
				//	BigDecimal credithour      = (BigDecimal)map.get("PLANOUTCREDITHOUR");
					//total_credithour          += credithour.longValue();
				//}
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("isdeleted", 0);
				total_credithour +=  baseSupportJdbcDao.getBaseJdbcTemplate().findForLong("select sum(c.planoutcredithour) from edu_base_course c where c.isdeleted = :isdeleted and c.resourceid in ("+outPlanCourseId.toString().substring(1)+")", parameters);
			}
			//------所有成绩中去除教学计划课程之后，如果还有成绩就当计划外课程成绩处理
			vo.setTotalCredit(Double.valueOf(String.format("%.1f",total_credithour)));
			vo.setRequiredCredit(Double.valueOf(String.format("%.1f",require_total_credithour)));
			vo.setCompulsoryed(compulsoryed);
		}
		return vo;
	}
	
	/**
	 * 对查询出来的成绩列表进行过滤并取出同一科目最大成绩(通过的)
	 * @return
	 */
	@Override
	public List<Map<String, Object>> sortExamResultList(List<Map<String, Object>> resultsMap){
		
		Map<String,Object> courseMaxResultsMap  = new HashMap<String, Object>();
		List<Map<String, Object>>  resultsList  = new ArrayList<Map<String,Object>>();
		
		if (null!=resultsMap&&!resultsMap.isEmpty()) {
			//遍历所有成绩列表
			for (Map<String, Object> map:resultsMap){
				
				String integratedscore              = (String)map.get("INTEGRATEDSCORE");
				String coursescoretype              = (String)map.get("COURSESCORETYPE");
				String studentId                    = (String)map.get("STUDENTID");
				String courseId                     = (String)map.get("COURSEID");
				
			
				//根据学籍ID+课程ID获取同一科目的成绩MAP
				Map<String, Object> maxResult       = (Map<String, Object>)courseMaxResultsMap.get(studentId+"_"+courseId);
				//如果没取到说明该课程还没放入成绩,否则比较当前遍历的成绩如果大于集合中的成绩则把当前成绩放入
				if (null==maxResult) {
					courseMaxResultsMap.put(studentId+"_"+courseId, map);
				}else {
				
					String score100                 = convertScore(coursescoretype,integratedscore);
					Double score100D                = 0.0;
					if (ExStringUtils.isNotEmpty(score100)) {
						score100D                   = Double.parseDouble(score100);
					}

					String maxResultIntegratedscore = (String)maxResult.get("INTEGRATEDSCORE");
					String maxResultCoursescoretype = (String)maxResult.get("COURSESCORETYPE");
					
					String maxResultScore100        = convertScore(maxResultCoursescoretype,maxResultIntegratedscore);
					Double maxResultScore100D       = 0.0;
					if (ExStringUtils.isNotEmpty(maxResultScore100)) {
						maxResultScore100D          = Double.parseDouble(maxResultScore100);
					}
					
					if (score100D>maxResultScore100D) {
						map.put("INTEGRATEDSCORE", integratedscore);
						courseMaxResultsMap.put(studentId+"_"+courseId, map);
					}
				}
				
			}
		}

		if (null!=courseMaxResultsMap.values()) {
			
			for (Object o :courseMaxResultsMap.values()) {
			
				Map<String, Object> m  = (Map<String, Object>)o;
				
				/*  2011.8.31发现如果取通过的成绩，当一学生的同一科成绩最高的为不通过，而且这些成绩都是发布了的，在学生界面会报错
			    String courseId        = (String)m.get("COURSEID");
				String integratedscore = (String)m.get("INTEGRATEDSCORE");
				String coursescoretype = (String)m.get("COURSESCORETYPE");
				String score100        = convertScore(coursescoretype,integratedscore);
				Double score100D       = 0.0;
				if (ExStringUtils.isNotEmpty(score100)) {
					 score100D         = Double.parseDouble(score100);
				}
				
				String isPass          = isPassScore(coursescoretype, score100D);
				
				if ("Y".equals(isPass)) {
					resultsList.add(m);
				}*/
				m.put("INTEGRATEDSCORE", convertScoreStr((String)m.get("courseScoreType"),(String)m.get("INTEGRATEDSCORE")));
				m.put("WRITTENSCORE", convertScoreStr((String)m.get("courseScoreType"),(String)m.get("WRITTENSCORE")));
				m.put("USUALLYSCORE", convertScoreStr((String)m.get("courseScoreType"),(String)m.get("USUALLYSCORE")));
				resultsList.add(m);
			}
		}
		return resultsList;
	}
	
	/**
	 * 等效课程(未使用)
	 * @param serList            统考、学位课成绩VO
	 * @param erList			  学生成绩--计划内\外课程成绩
	 * @param studentInfo        学籍
	 * @param resultsList		 返回结果集
	 * @return
	 */
	private List <StudentExamResultsVo>  getEqucourseList(List <StudentExamResultsVo> serList,List<StudentExamResultsVo> erList,
														  StudentInfo studentInfo,List<StudentExamResultsVo> resultsList ){
		
//		String equcoursesHQL	   		   = " from "+Equcourse.class.getSimpleName()+" where isDeleted = 0  ";
//		List<Equcourse> equcourses 		   = equcourseservice.findByHql(equcoursesHQL);
//
//		if (null!=equcourses && equcourses.size()>0) {
//			Set<TeachingPlanCourse> tpcSet = studentInfo.getTeachingPlan().getTeachingPlanCourses();
//			for (Equcourse c:equcourses) {
//				String  oldCourseId		   = c.getOldCourseId();
//				String  newCourseId		   = c.getNewCourseId();
//			}
//		}

		return resultsList;
	}
	/**
	 * 根据传入的学籍对象获取对应的教学计划课程
	 * @param studentInfo
	 * @return
	 */
	private Map<String,TeachingPlanCourse> getStudentTeachingPlanCourse(StudentInfo studentInfo){
		Map<String,TeachingPlanCourse> planCourseMap = new HashMap<String, TeachingPlanCourse>();
		Set<TeachingPlanCourse> planCourseSet        = studentInfo.getTeachingPlan().getTeachingPlanCourses();
		for (TeachingPlanCourse planCourse:planCourseSet) {
			planCourseMap.put(planCourse.getCourse().getResourceid(), planCourse);
		}
		return planCourseMap;
	}
	/**
	 * 设置课程取得的学分
	 * @param resultsList
	 * @return
	 */
	private List<StudentExamResultsVo> setInCreditHour(List<StudentExamResultsVo> resultsList,Map<String,TeachingPlanCourse> planCourseMap,String printExam){
		//返回结果集
		List<StudentExamResultsVo> returnList        = new LinkedList<StudentExamResultsVo>();
		//用于存放同一门课程最大成绩
		Map<String,StudentExamResultsVo> scoreMap    = new HashMap<String, StudentExamResultsVo>();
		//用于存放同一门课程最大成绩在集合中的下标
		Map<String,Integer> indexMap                 = new HashMap<String, Integer>();
		
		for (int i = 0; i < resultsList.size(); i++) {
			String resourceid = resultsList.get(i).getResourceid();
			String index =  (resourceid.startsWith("elective_") || resourceid.startsWith("state_"))?resourceid:resultsList.get(i).getCourseId();
			StudentExamResultsVo vo                  = scoreMap.get(index);
			if (Constants.BOOLEAN_YES.equals(resultsList.get(i).getIsPass()) && 
				"4".equals(ExStringUtils.defaultIfEmpty(resultsList.get(i).getCheckStatusCode(), "-1"))) {//已发布并且通过
				if (null==vo) {
					//选修课resourceid = elective_courseid
					//统考课resourceid = state_courseid
					scoreMap.put(index, resultsList.get(i));
					indexMap.put(index, i);
				} else{
					Long l1 = resultsList.get(i).getIntegratedScoreL();
					Long l2 = vo.getIntegratedScoreL();
					if (l1>l2) {
						scoreMap.put(index, resultsList.get(i));
						indexMap.put(index, i);
					}	
				}
			}
		}
		
		// 取得必修学分总和
		Double require_total_credithour = 0D;
		// 取得学分总和
		Double total_credithour   	    = 0D;
		// 取得选修学分总和
		Double minor_total_credithour   = 0D;
		// 计划内学分总和
		Double inplan_total_credihour   = 0D;
		// 总分数
		Double total_score              = 0D;
		
		//主干课成绩
		Double main_total_Score         = 0D;
		//专业基础课成绩
		Double base_total_score         = 0D;
		//主干课学分
		Double main_total_credithour    = 0D;
		//专业基础课学分
		Double base_total_credithour    = 0D;
		
		// 总科目
		int total_count 			  = 0;
		//已修必修课数
		int require_count             = 0;
		//限选课门数
		int limited_count             = 0;
		//主干课门数
		int main_count                = 0;
		//专业基础课门数
		int base_count                = 0;
		//学位英语是否通过
		//boolean degreeEnglish         = false;
		//毕业论文成绩
		String thesisScore            = "";
		Map<String, Object> electiveCourse = new HashMap<String, Object>();
		int noTermNum = 0;
		for (int i = 0; i < resultsList.size(); i++) {
			
			String courseScoreType                   = resultsList.get(i).getExamResultsType();
			String courseId                          = resultsList.get(i).getCourseId();
			TeachingPlanCourse tpc                   = planCourseMap.get(courseId);
			Integer maxExamResultsIndex				 = indexMap.get(courseId);
			StudentExamResultsVo maxExamResultsScore = scoreMap.get(courseId);
			StudentExamResultsVo vo                  = resultsList.get(i);
			String isPass                            = ExStringUtils.defaultIfEmpty(vo.getIsPass(), Constants.BOOLEAN_NO);
			String checkStuts                        = ExStringUtils.defaultIfEmpty(vo.getCheckStatusCode(), "-1");
			vo.setIndex(String.valueOf(i+1));
			
			if (vo.getResourceid().startsWith("elective_")) {
				
			}else if (null==maxExamResultsScore) {
				vo.setInCreditHour(0D);
			}
			if (null!=maxExamResultsScore && resultsList.get(i).getCourseId().equals(maxExamResultsScore.getCourseId())&& maxExamResultsIndex!=i) {
				vo.setInCreditHour(0D);
			}
			
			//System.out.println(vo.getCourseName()+"---->\t\t\t\t获取学分:"+vo.getInCreditHour()+"----当前总学分："+total_credithour+"----当前成绩:"+vo.getIntegratedScore()+":"+vo.getIntegratedScoreL());

			if ("4".equals(checkStuts)) {//Constants.BOOLEAN_YES.equals(isPass) 计算所有课程平均分 20181019
				//考虑到汕大选修课的特殊性，此处相同课程通过只计算一次学分
				if(electiveCourse.containsKey(vo.getResourceid())){
					returnList.add(vo);
					continue;
				}else {
					electiveCourse.put(vo.getResourceid(), vo.getResourceid());
				}
				//主干课程学分
				if (null!=tpc&&Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(tpc.getIsMainCourse()))) {
					main_count                          += 1;
					main_total_credithour               += vo.getInCreditHour();
					main_total_Score                    += vo.getIntegratedScoreL();
				}
				//专业基础课
				if (null!=tpc&&"1500".equals(ExStringUtils.trimToEmpty(tpc.getCourseNature()))) {
					base_count                          += 1;
					base_total_credithour               += vo.getInCreditHour();
					base_total_score                    += vo.getIntegratedScoreL();
				}
				if ("11".equals(vo.getCourseTypeCode())) {	//必修课学分
					require_total_credithour            += vo.getInCreditHour();
					require_count                       += 1;
				}
				if ("thesis".equals(ExStringUtils.trimToEmpty(vo.getCourseTypeCode()))) {
					require_total_credithour            += vo.getInCreditHour();
					require_count                       += 1;
					thesisScore                          = 2514>=vo.getIntegratedScoreL()?convertScoreStr(courseScoreType, vo.getIntegratedScore()):"";
				}
				if ("33".equals(ExStringUtils.trimToEmpty(vo.getCourseTypeCode()))) {
					limited_count                       += 1;
				}
				if (null!=vo.getInCreditHour() && 0 !=vo.getInCreditHour()) {		//取得的总学分
					total_credithour                    += vo.getInCreditHour();
				}else if (vo.getCreditHour()!=null && 0!=vo.getCreditHour()) {
					total_credithour                    += vo.getCreditHour();
				}
				if ("N".equals(ExStringUtils.trimToEmpty(vo.getIsOutplancourse()))) {//计划内学分
					inplan_total_credihour              += null==vo.getInCreditHour()?0:vo.getInCreditHour();
				}														   	

				total_score                             += vo.getIntegratedScoreL();//总分数
				
				total_count                             += 1;
				
				// 判断是否有学位课程，将学分设置为空
				if(ExStringUtils.isBlank(vo.getCourseTerm())){
					noTermNum +=1;
					vo.setInCreditHourStr("");
					vo.setStydyHour("");
				}
			}

			returnList.add(vo);
		}
		
		if (total_credithour>0) {
			minor_total_credithour  = total_credithour-require_total_credithour;//选修学分
		}
		
		/**
		 * 打印成绩
		 */
		if("true".equals(printExam)){
			returnList = getList(resultsList);
		}
		
		StudentExamResultsVo lastVo = new StudentExamResultsVo();
		lastVo.setTotalScore(total_score);
		lastVo.setTotalCredit(ExNumberUtils.toDouble(total_credithour));
		lastVo.setTotalMajorCredit(ExNumberUtils.toDouble(inplan_total_credihour));
		lastVo.setRequiredCredit(ExNumberUtils.toDouble(require_total_credithour));
		lastVo.setElectiveCredit(ExNumberUtils.toDouble(minor_total_credithour));
		lastVo.setRecordCount(total_count);
		lastVo.setCompulsoryed(require_count);
		lastVo.setLimitedCount(limited_count);
		
		lastVo.setMainCourse(main_count);
		lastVo.setBaseCourse(base_count);
		
		if (main_total_Score>0) {
			lastVo.setMainCourseScoreAvg(BigDecimal.valueOf(main_total_Score/main_count).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		}
		if (base_total_score>0) {
			lastVo.setBaseCourseScoreAvg(base_total_score/base_count);
		}

		if (total_score>0) {
			lastVo.setAvgScore(BigDecimal.valueOf(total_score/(electiveCourse.size()-noTermNum)).setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue());
		}else {
			lastVo.setAvgScore(0D);
		}
		lastVo.setThesisScore(thesisScore);
		
		returnList.add(lastVo);

		return returnList;
	}
	
	private List<StudentExamResultsVo> getList(List<StudentExamResultsVo> resultsList){
		
		List<StudentExamResultsVo> returnList = new ArrayList<StudentExamResultsVo>();
		
		//由于正考，一补，二补考，清考等成绩的加入对这个方法进行改造如下
		Map<String,List<StudentExamResultsVo>> courseMapList = new HashMap<String, List<StudentExamResultsVo>>();
		//过滤重复的课程
		for(StudentExamResultsVo vo : resultsList){
			List<StudentExamResultsVo> list = null;
			list = courseMapList.get(vo.getCourseId());
			if(null == list){
				list = new ArrayList<StudentExamResultsVo>();
			}
			list.add(vo);
			courseMapList.put(vo.getCourseId(), list);
		}
		
		//由于成绩的显示规则是  正考/一补/二补/...
		for (String key : courseMapList.keySet()) {
			StudentExamResultsVo rtvo = null;
			List<StudentExamResultsVo> list = courseMapList.get(key);
			List<StudentExamResultsVo> orderList = new ArrayList<StudentExamResultsVo>();
			List<StudentExamResultsVo> rmList = new ArrayList<StudentExamResultsVo>();
			for(int i = 0 ; i < list.size(); i++){
				StudentExamResultsVo v = list.get(i);
				if("5".equals(v.getExamAbnormityCode())){//处理缓考
					SysConfiguration guration = CacheAppManager.getSysConfigurationByCode("ExamResultCorrosion");//缓考替代全局参数
					String code = null == guration ? "" : guration.getParamValue();
					for(StudentExamResultsVo vv : list){
						if(ExStringUtils.isNotBlank(code) && code.equals(vv.getExamSubType())){
							v.setIntegratedScore("缓考"+vv.getIntegratedScore());
							Double _inCreditHour = 0D;
							try {
								Double _integratedScore = Double.valueOf(vv.getIntegratedScore());
								if(_integratedScore >= 60){
									_inCreditHour = vv.getInCreditHour();
								}
							} catch (NumberFormatException e) {
								_inCreditHour = vv.getInCreditHour();
							}
							v.setCreditHour(_inCreditHour);
							v.setInCreditHour(_inCreditHour);
							rmList.add(vv);
						}
					}
				}
				List<Dictionary> children  = CacheAppManager.getChildren("ExamResult");//从缓存中获取字典对象
				Map<String,Dictionary> mapdict = new HashMap<String, Dictionary>();
				for(Dictionary dict : children){ //按字典里的顺序放值
					mapdict.put(dict.getDictValue(), dict);
				}
				
				if(mapdict.containsKey(v.getExamSubType())){
					Dictionary d = mapdict.get(v.getExamSubType());
					v.setExamOrder(d.getShowOrder());
				}else{
					v.setExamOrder(-1); //放在最前面
				}
				orderList.add(v);
			}
			list.removeAll(rmList);
			for(int i = 0 ; i < list.size(); i++){
				for(int ii = i+1 ; ii < list.size(); ii++){
					StudentExamResultsVo v1 = list.get(i);
					StudentExamResultsVo v2 = list.get(ii);
					if(v1.getExamOrder() > v2.getExamOrder()){
						list.set(i, v2);
						list.set(ii,v1);
					}
				}
			}
			
			String str = "";
			List<String> noExamList = new ArrayList<String>();
			for(int i = 0 ; i < list.size(); i++){
				StudentExamResultsVo v = list.get(i);
				if(!noExamList.contains(v.getCourseId())){
					str = ExStringUtils.isNotBlank(str) ? str +"/"+v.getIntegratedScore():str+v.getIntegratedScore();
					rtvo = v;
				}
				// 有免考的，不管是否有补考，都只显示免考
				// TODO:如果有其他的情况另论
				if(ExStringUtils.isNotEmpty(v.getIsNoExam()) && "Y".equals(v.getIsNoExam())){
					noExamList.add(v.getCourseId());
					str = "免" + v.getIntegratedScore();
					rtvo = v;
					break;
				}
			}
			rtvo.setIntegratedScore(str);
			returnList.add(rtvo);
		}
		
//		//用于存放同一门课程最大正考成绩
//		Map<String,StudentExamResultsVo> scoreMap_zk    = new HashMap<String, StudentExamResultsVo>();
//		List<StudentExamResultsVo> 		 list_zk		= new ArrayList<StudentExamResultsVo>();
//		for(StudentExamResultsVo vo :resultsList){
//			if ("4".equals(ExStringUtils.defaultIfEmpty(vo.getCheckStatusCode(), "-1"))) {//已发布并
//				if(!Constants.BOOLEAN_YES.equals(vo.getIsNoExam())&&!Constants.BOOLEAN_YES.equals(vo.getIsMakeupExam())){ //非补考，免考
//				//	System.out.println("IsNoExam="+vo.getIsNoExam()+"||IsMakeupExam"+vo.getIsMakeupExam());
//					list_zk.add(vo);
//				}
//			}
//		}
//		
//		//正考成绩最大值
//		for(int i = 0 ; i < list_zk.size(); i ++){
//			StudentExamResultsVo vo                  = scoreMap_zk.get(list_zk.get(i).getCourseId());
//			if (null==vo) {
//				scoreMap_zk.put(list_zk.get(i).getCourseId(), list_zk.get(i));
//			} else{
//				Long l1 = list_zk.get(i).getIntegratedScoreL();
//				Long l2 = vo.getIntegratedScoreL();
//				if (l1>l2) {
//					scoreMap_zk.put(list_zk.get(i).getCourseId(), list_zk.get(i));
//				}	
//			}
//		}
//		
//		//用于存放同一门课程最大补考成绩
//		Map<String,StudentExamResultsVo> scoreMap_bk    = new HashMap<String, StudentExamResultsVo>();
//		List<StudentExamResultsVo> 		 list_bk		= new ArrayList<StudentExamResultsVo>();
//		for(StudentExamResultsVo vo :resultsList){
//			if ("4".equals(ExStringUtils.defaultIfEmpty(vo.getCheckStatusCode(), "-1"))) {//已发布
//				if(Constants.BOOLEAN_YES.equals(vo.getIsMakeupExam())){ //补考
//					list_bk.add(vo);
//				}
//			}
//		}
//		
//		//补考成绩最大值
//		for(int i = 0 ; i < list_bk.size(); i ++){
//			StudentExamResultsVo vo                  = scoreMap_bk.get(list_bk.get(i).getCourseId());
//			if (null==vo) {
//				scoreMap_bk.put(list_bk.get(i).getCourseId(), list_bk.get(i));
//			} else{
//				Long l1 = list_bk.get(i).getIntegratedScoreL();
//				Long l2 = vo.getIntegratedScoreL();
//				if (l1>l2) {
//					scoreMap_bk.put(list_bk.get(i).getCourseId(), list_bk.get(i));
//				}	
//			}
//		}
//		
//		//免考
//		List<StudentExamResultsVo> 		 list_mk		= new ArrayList<StudentExamResultsVo>();
//		for(StudentExamResultsVo vo :resultsList){
//			if ("4".equals(ExStringUtils.defaultIfEmpty(vo.getCheckStatusCode(), "-1"))) {//已发布并且通过
//				if(Constants.BOOLEAN_YES.equals(vo.getIsNoExam())){ //免考
//					//vo.setIntegratedScore("70"); //免考 默认70分
//					list_mk.add(vo);
//				}
//			}
//		}
//		
//		//打印成绩list 
//		if(null != scoreMap_zk){
//			for (String key_zk : scoreMap_zk.keySet()) {  
//	            //System.out.println("key = " + key + " and value = " + map.get(key));  
//				if(Constants.BOOLEAN_YES.equals(scoreMap_zk.get(key_zk).getIsPass())){//1.如果正考就通过，则只显示正考的最高值，如80分，则显示80
//					returnList.add(scoreMap_zk.get(key_zk));
//				}else{//2.正考不通过，补考了，则显示 正考高值/补考高值，如正考40分，后面的补考最高位65分，成绩这里显示 40/65
//					if(null != scoreMap_bk){
//						if(scoreMap_bk.size() == 0){
//							returnList.add(scoreMap_zk.get(key_zk));
//						}else{
//							for(String key_bk : scoreMap_bk.keySet()){
//								if(scoreMap_bk.get(key_bk).getCourseId().equals(scoreMap_zk.get(key_zk).getCourseId())){
//									scoreMap_zk.get(key_zk).setIntegratedScore(scoreMap_zk.get(key_zk).getIntegratedScore()+"/"+scoreMap_bk.get(key_bk).getIntegratedScore());
//									returnList.add(scoreMap_zk.get(key_zk));
//								}else{
//									returnList.add(scoreMap_zk.get(key_zk));
//								}
//							}
//						}
//					}
//				}
//	        } 
//		}
//
//		//免考
//		if(null != list_mk){
//			for(StudentExamResultsVo vomk :list_mk){
//				returnList.add(vomk);
//			}
//		}	
//
//	  HashSet h = new HashSet(returnList); //去除重复数据
//	  returnList.clear();
//	  returnList.addAll(h);
////	  int index = 1; //排序
////	  for(StudentExamResultsVo v :returnList){
////		  v.setIndex(index+"");
////		  ++index;
////	  }
	  return returnList;
	}
	
	/**
	 * 学生成绩(所有成绩)--计划内\外课程成绩
	 * @param studentInfo
	 * @param tpc
	 * @param param
	 * @return
	 */
	private List<StudentExamResultsVo> getStudentAllExamResultsVo(StudentInfo studentInfo,Map<String,TeachingPlanCourse> tpc,Map<String, Object> param)throws Exception{
		List <StudentExamResultsVo> resultsList = new ArrayList<StudentExamResultsVo>();
		List<Map<String, Object>> electiveExams = new ArrayList<Map<String,Object>>();
		String flag = (String) param.get("flag");
		Integer examCount = (Integer) param.get("examCount");
		StringBuffer hql = new StringBuffer();
		Map<String, Object> para =new HashMap<String, Object>();
		para.put("studentid", studentInfo.getResourceid());
		if(examCount==null){
			hql.append("select count(*) from edu_teach_electiveexamresults eer where eer.isdeleted=0 and eer.studentid=:studentid and eer.checkstatus <> '-1'");
			examCount = (int) baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(hql.toString(), para);
		}
		//先判断是否有选修课成绩，如果有则联合查询选修成绩表（每次查询大约节省0.3秒）
		if(examCount>0 && "audit".equals(flag)){
			//成绩审核
			hql.setLength(0);
			Integer optionalCourseNum = (Integer) ExStringUtils.nvl4Obj(studentInfo.getTeachingPlan().getOptionalCourseNum(), 4);
			para.put("optionalCourseNum", optionalCourseNum);
			hql.append("select * from (select exam.courseid,exam.integratedscore from(");
			hql.append("select eer.courseid,eer.examabnormity,eer.integratedscore,decode(eer.examabnormity,'8',65,nvl(d.showorder,eer.integratedscore))score");
			hql.append(" ,row_number() over(partition by eer.courseid order by decode(eer.examabnormity,'8',65,nvl(d.showorder,eer.integratedscore)) desc) r");
			hql.append(" from edu_teach_electiveexamresults eer");
			hql.append(" left join hnjk_sys_dict d on d.dictvalue=eer.integratedscore and d.parentid='829C90102DEA4CEBE040007F010022CC'");
			hql.append(" where eer.studentid=:studentid and eer.checkstatus <> '-1' and eer.isdeleted=0 and eer.examabnormity in('0','6','8')");
			hql.append("  group by eer.courseid,eer.examabnormity,d.showorder,eer.integratedscore) exam");
			hql.append("  where R=1 order by exam.score desc) where rownum<=:optionalCourseNum");
			electiveExams = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), para);
		}
		hql.setLength(0);
		hql.append("select * from (select er.resourceid,er.courseid,c.courseCode,c.planoutCreditHour,er.examAbnormity,er.courseScoreType,er.checkStatus,er.isMarkDelete,c.courseEnName,c.courseName,c.examType courseExamType,er.stydyHour,er.examsubId,ei.examStartTime,ei.examEndTime,er.isMakeupExam,er.isDelayExam");
		hql.append(",f_decrypt_score(er.writtenScore,er.studentid) writtenScore,f_decrypt_score(er.integratedscore,er.studentid) integratedscore,f_decrypt_score(er.usuallyScore,er.studentid) usuallyScore,es.examType, es.starttime,es.endtime,null optionalcoursenum ");
		hql.append(" from EDU_TEACH_EXAMRESULTS er left join edu_teach_examsub es on es.resourceid=er.examsubid ");
		hql.append(" left join edu_teach_examinfo ei on ei.examsubid=es.resourceid and ei.examtype=es.examtype and ei.courseid=er.courseid and ei.isDeleted=0");
		hql.append(" left join edu_base_course c on c.resourceid=er.courseid and c.isDeleted=0 where er.isDeleted=0 ");
		hql.append(" and er.checkStatus <> '-1' and er.studentid=:studentid ");
		//加入选修课成绩
		if(examCount>0){
			hql.append(" union select * from(select 'elective_'||eer.courseid,eer.courseid,c.courseCode,nvl(c.planoutcredithour,3.0) planoutcredithour,eer.examAbnormity,eer.courseScoreType,eer.checkStatus,'N' isMarkDelete,c.courseenname,c.coursename,c.examtype courseExamType,nvl(c.planoutstudyhour,3.0),eer.examsubid,null examStartTime,null examEndTime,'N' isMakeupExam,'N' isDelayExam");
			hql.append(" ,eer.writtenScore,eer.integratedscore,eer.usuallyScore,es.examtype,es.starttime,es.endtime,nvl(p.optionalcoursenum,4) optionalcoursenum ");
			hql.append(" from edu_teach_electiveexamresults eer ");
			hql.append(" join edu_base_course c on c.resourceid=eer.courseid ");
			hql.append(" join edu_teach_examsub es on es.resourceid=eer.examsubid");
			hql.append(" join edu_roll_studentinfo si on si.resourceid=eer.studentid");
			hql.append(" join edu_teach_plan p on p.resourceid=si.teachplanid");
			hql.append(" where eer.checkStatus <> '-1' and eer.studentid=:studentid and eer.isdeleted=0");
			if("audit".equals(flag)){//成绩审核，选成绩最高4门课程
				hql.append(" and (eer.courseid,eer.integratedscore) in ("+ExStringUtils.getValueByKeyFromMapList(electiveExams,"courseid","integratedscore")+")");
			}
			hql.append(")");
		}
		hql.append(") order by courseid,decode(examtype, 'N', 1, 'Y', 2, 'T', 3, 'Q', 4, 'R', 5, 'G', 6),starttime,endtime");
	    List<Map<String, Object>> list  = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), para);
	    for (Map<String, Object> rs : list) {
			TeachingPlanCourse planCourse       = tpc.get(rs.get("courseid"));
			Double creditHour 				    = 0D;
			String courseType                   = "";
			String isPlanOutCourse              = Constants.BOOLEAN_NO;		
			String examClassType = "";
			//是否计划外课程
			if (null!=planCourse) {
				creditHour						= null==planCourse.getCreditHour()?0D:planCourse.getCreditHour();	
				courseType                      = null==planCourse.getCourseType()?"":planCourse.getCourseType();
				isPlanOutCourse                 = Constants.BOOLEAN_NO;
				examClassType = planCourse.getExamClassType() != null ? planCourse.getExamClassType() : "";
			} else {
				creditHour                      =  null==rs.get("planoutCreditHour")?0D:Double.valueOf(rs.get("planoutCreditHour").toString());
				courseType 						= "22";
				isPlanOutCourse                 = Constants.BOOLEAN_YES;
			}
			String examAbnormity                = (String) (null==rs.get("examAbnormity")?"4":rs.get("examAbnormity"));
			String integratedScore              = (String) rs.get("integratedscore");//当成绩类型不为百分制时转成字符型成绩
			String courseScoreType              = ExStringUtils.trimToEmpty((String) rs.get("courseScoreType"));
			Double integratedScoreD             = null==rs.get("integratedscore")?0d:Double.valueOf((String) rs.get("integratedscore"));
			
			//如果成绩异常或者正常成绩但综合成绩为空，设置一个默认值
			if ((!"0".equals(rs.get("examAbnormity")))||("0".equals(rs.get("examAbnormity"))&&null==rs.get("integratedscore"))) {
				if (Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(courseScoreType)) {
					integratedScoreD            = 0D ;
					integratedScore             = "0";

				}else if (Constants.COURSE_SCORE_TYPE_TWO.equals(courseScoreType)) {
					integratedScoreD            = 2201D;
					integratedScore             = "2201";

				}else if (Constants.COURSE_SCORE_TYPE_THREE.equals(courseScoreType)) {
					integratedScoreD        	= 2301D;	
					integratedScore         	= "2301";

				}else if (Constants.COURSE_SCORE_TYPE_FOUR.equals(courseScoreType)) {
					integratedScoreD            = 2401D;		
					integratedScore             = "2401";

				}else  if (Constants.COURSE_SCORE_TYPE_FIVE.equals(courseScoreType)) {
					integratedScoreD            = 2501D;
					integratedScore             = "2501";
				}
			}
			
			String[] score_d = integratedScoreD.toString().split("\\.");
			String _score = "0";
			if(null != score_d && score_d.length > 0) {
				_score = score_d[0];
			}
			integratedScoreD                	= Double.valueOf(convertScore(courseScoreType,_score));
			
			
			StudentExamResultsVo vo 			= new StudentExamResultsVo();
			
			/*StudentMakeupList studentMakeupList = graduateDataService.findByHql1(studentInfo);
			if(studentMakeupList != null) {
				vo.setBkScore(studentMakeupList.getExamResults().getIntegratedScore());//补考成绩
			}else {
				vo.setBkScore("");
			}*/
			vo.setResourceid((String) rs.get("resourceid"));
			vo.setCheckStatus(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", (String) (null != rs.get("checkStatus") ? rs.get("checkStatus") : "")));
			vo.setCheckStatusCode((String) rs.get("checkStatus"));
			vo.setCourseId((String) rs.get("courseid"));
			vo.setCourseCode(ExStringUtils.toString(rs.get("courseCode")));
			vo.setCourseEnName(ExStringUtils.trimToEmpty((String) rs.get("courseEnName")));
			vo.setCourseName((String) rs.get("courseName"));
			vo.setExamClassType(JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", null != examClassType ? examClassType : ""));
			String CourseNature = null != planCourse ?  (null != planCourse.getCourseNature() ? planCourse.getCourseNature() : "") : "2010";
			vo.setCourseNature(JstlCustomFunction.dictionaryCode2Value("courseNature",CourseNature));
			vo.setStudyNo(studentInfo.getStudyNo());
			vo.setStudentName(studentInfo.getStudentName());
			vo.setClassesid(studentInfo.getClassesid());
			if(null != rs.get("stydyHour")){
				vo.setStydyHour(rs.get("stydyHour").toString());
			}else{
				TeachingPlan tp = studentInfo.getTeachingPlan();
				Set<TeachingPlanCourse> tpcSet = null != tp ?  tp.getTeachingPlanCourses() : null;
				
				if(null != tpcSet && tpcSet.size() > 0){
					for(TeachingPlanCourse v :tpcSet){
						if(v.getCourse().getResourceid().equals(rs.get("courseid"))){
							vo.setStydyHour(null==v.getStydyHour()?"":v.getStydyHour().toString());
						}
					}
				}
			}
			
			if("thesis".equals(courseType)&&ExStringUtils.isNotBlank((String) rs.get("examsubId"))){//毕业论文
				ExamSub sub = examSubService.get(rs.get("examsubId").toString());
				vo.setPassTime(sub.getGraduateAuditDate());
			} else {
				vo.setExamStartTime((Date) (null==rs.get("examStartTime")?null:rs.get("examStartTime")));
				vo.setExamEndTime((Date) (null==rs.get("examEndTime")?null:rs.get("examEndTime")));
			}			
			vo.setIsmakeupexam((String) rs.get("isMakeupExam")); //补考成绩
			vo.setIsdelayexam((String) rs.get("isDelayExam"));//延考成绩
			vo.setExamResultsType(courseScoreType);
			vo.setExamAbnormityCode((String) rs.get("examAbnormity"));
			vo.setIsOutplancourse(isPlanOutCourse);
			vo.setCourseType(JstlCustomFunction.dictionaryCode2Value("CodeCourseType", courseType));
			vo.setCourseTypeCode(courseType);
			vo.setExamType(JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",rs.get("courseExamType")!=null?rs.get("courseExamType").toString():""));
			ExamSub sub = examSubService.get((Serializable) rs.get("examsubId"));
			vo.setExamSubType(null != sub ? sub.getExamType() : ""); //对应字典 ExamResult
			String term = "";
			String year_term = "";
			if(null != planCourse && ("print".equals(flag) || "export".equals(flag))){
				term = planCourse.getTerm();
				long year = studentInfo.getGrade().getYearInfo().getFirstYear() + (Integer.parseInt(term)-1)/2;
				year_term = year + "_" + (Integer.parseInt(term)%2==0?"2":"1");
			}else {//选修课学期
				long year = sub.getYearInfo().getFirstYear() - studentInfo.getGrade().getYearInfo().getFirstYear();
				long electiveTerm = year*2;
				electiveTerm = electiveTerm + Integer.parseInt(sub.getTerm());
				term = Long.toString(electiveTerm);
				year_term = sub.getYearInfo().getFirstYear() + "_" + sub.getTerm();
			}

			vo.setCourseTerm(term);//课程学期
			vo.setCourseYearTerm(year_term);
			if (!Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(courseScoreType)) {
				vo.setExamResultsChs(convertScoreStr(courseScoreType,integratedScore));
			}
			if (!"0".equals(examAbnormity)) {
				vo.setIsPass(Constants.BOOLEAN_NO);
				creditHour = 0D;

				vo.setUsuallyScore((String) (null==rs.get("usuallyScore")?"0":rs.get("usuallyScore")));
				if(ExStringUtils.isNotEmpty(flag) && ("print".equals(flag) || "export".equals("flag")) 
						&&"1".equals(examAbnormity)){
					vo.setExamResultsChs(ExamResults.CHEAT_SCORE);
					vo.setWrittenScore(ExamResults.CHEAT_SCORE);
					vo.setIntegratedScore(ExamResults.CHEAT_SCORE);
				} else {
					vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
					vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
					vo.setIntegratedScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
				}
				vo.setIntegratedScoreL(0L);
			} else {
				vo.setIsPass(isPassScore(courseScoreType,integratedScoreD));
				vo.setUsuallyScore(convertScore(courseScoreType,(String) rs.get("usuallyScore")));
				vo.setWrittenScore(convertScore(courseScoreType,(String) rs.get("writtenScore")));
				vo.setIntegratedScore(convertScore(courseScoreType,(String) rs.get("integratedscore")));
				vo.setIntegratedScoreL(Double.valueOf(convertScore(courseScoreType,(String) rs.get("integratedscore"))).longValue());
			}
			vo.setCreditHour(creditHour);
			vo.setInCreditHour(creditHour);
			vo.setBatchName(null == sub ? "" : sub.getBatchName());//考试批次名称
			vo.setExamSubId(null == sub ? "" : sub.getResourceid());//考试批次ID
//			vo.setIsMakeupExam(rs.getIsMakeupExam());//是否补考
			String isBK = null == rs.get("examType") ? "" : rs.get("examType").toString();
			vo.setIsMakeupExam(isBK);//是否补考
			//增加考试结果id和标记删除,20161017
			vo.setExamResultId((String) rs.get("resourceid"));
			vo.setIsMarkDelete((String) rs.get("isMarkDelete"));
			
			//如果该门课程成绩为等级制，则将分数转为等级
			String scoreStyle = courseScoreType==null?"":courseScoreType;
			if(planCourse !=null && planCourse.getScoreStyle()!=null){
				scoreStyle = planCourse.getScoreStyle();
			}
			vo.setExamResultsType(scoreStyle);
			if("30".equals(scoreStyle)){//等级成绩，分数转字符
				List<Dictionary> list_dict = CacheAppManager.getChildren("CodeScoreLevel");
				for (int i=0;i<list_dict.size();i++) {
					Dictionary dictionary = list_dict.get(i);
					int score = Integer.parseInt(dictionary.getDictValue());
					if(Integer.parseInt((String) rs.get("integratedscore"))>=score){
						vo.setExamResultsChs(dictionary.getDictName());
						vo.setIntegratedScore(dictionary.getDictName());
						i=list_dict.size();
					}
				}
			}
			if ("25".equals(courseScoreType)) {//字符成绩
				String scoreChs = convertScoreStr(courseScoreType, (String) rs.get("integratedscore"));
				vo.setExamResultsChs(scoreChs);
				vo.setIntegratedScore(scoreChs);
			}
			//补考和结补成绩
			if("Y".equals(sub.getExamType()) || "T".equals(sub.getExamType())){//补考成绩
				vo.setBkScore(vo.getIntegratedScore());
			}else if ("Q".equals(sub.getExamType())) {//结补成绩，暂时未加入返校补（R,G）
				vo.setJbScore(vo.getIntegratedScore());
			}
			resultsList.add(vo);
		}
	    /*
	    String hql                              = " from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 ";
		   hql                             += " and results.checkStatus <> '-1' and results.studentInfo.resourceid=? order by results.course.courseName";
	    List<ExamResults> list 					= (List<ExamResults>) exGeneralHibernateDao.findByCriteriaSession(c)(hql,studentInfo.getResourceid());
		
		for (ExamResults rs : list) {
			TeachingPlanCourse planCourse       = tpc.get(rs.getCourse().getResourceid());
			Double creditHour 				    = 0D;
			String courseType                   = "";
			String isPlanOutCourse              = Constants.BOOLEAN_NO;		
			String examClassType = "";
			//是否计划外课程
			if (null!=planCourse) {
				creditHour						= null==planCourse.getCreditHour()?0D:planCourse.getCreditHour();	
				courseType                      = null==planCourse.getCourseType()?"":planCourse.getCourseType();
				isPlanOutCourse                 = Constants.BOOLEAN_NO;
				examClassType = planCourse.getExamClassType() != null ? planCourse.getExamClassType() : "";
			} else {
				creditHour                      = null==rs.getCourse().getPlanoutCreditHour()?0D:rs.getCourse().getPlanoutCreditHour();
				courseType 						= "22";
				isPlanOutCourse                 = Constants.BOOLEAN_YES;
			}
			String examAbnormity                = null==rs.getExamAbnormity()?"4":rs.getExamAbnormity();
			String integratedScore              = rs.getIntegratedScore();//当成绩类型不为百分制时转成字符型成绩
			String courseScoreType              = ExStringUtils.trimToEmpty(rs.getCourseScoreType());
			Double integratedScoreD             = null==rs.getIntegratedScore()?0d:Double.valueOf(rs.getIntegratedScore());
			
			//如果成绩异常或者正常成绩但综合成绩为空，设置一个默认值
			if ((!"0".equals(rs.getExamAbnormity()))||("0".equals(rs.getExamAbnormity())&&null==rs.getIntegratedScore())) {
				if (Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(courseScoreType)) {
					integratedScoreD            = 0D ;
					integratedScore             = "0";

				}else if (Constants.COURSE_SCORE_TYPE_TWO.equals(courseScoreType)) {
					integratedScoreD            = 2201D;
					integratedScore             = "2201";

				}else if (Constants.COURSE_SCORE_TYPE_THREE.equals(courseScoreType)) {
					integratedScoreD        	= 2301D;	
					integratedScore         	= "2301";

				}else if (Constants.COURSE_SCORE_TYPE_FOUR.equals(courseScoreType)) {
					integratedScoreD            = 2401D;		
					integratedScore             = "2401";

				}else  if (Constants.COURSE_SCORE_TYPE_FIVE.equals(courseScoreType)) {
					integratedScoreD            = 2501D;
					integratedScore             = "2501";
				}
			}
			
			String[] score_d = integratedScoreD.toString().split("\\.");
			String _score = "0";
			if(null != score_d && score_d.length > 0) _score = score_d[0];
			integratedScoreD                	= Double.valueOf(convertScore(courseScoreType,_score));
			
			StudentExamResultsVo vo 			= new StudentExamResultsVo();
			
			StudentMakeupList studentMakeupList = graduateDataService.findByHql1(studentInfo);
			if(studentMakeupList != null) {
				vo.setBkScore(studentMakeupList.getExamResults().getIntegratedScore());//补考成绩
			}else {
				vo.setBkScore("");
			}
			vo.setCheckStatus(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", null != rs.getCheckStatus() ? rs.getCheckStatus() : ""));
			vo.setCheckStatusCode(rs.getCheckStatus());
			vo.setCourseId(rs.getCourse().getResourceid());
			vo.setCourseEnName(ExStringUtils.trimToEmpty(rs.getCourse().getCourseEnName()));
			vo.setCourseName(rs.getCourse().getCourseName());
			vo.setExamClassType(JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", null != examClassType ? examClassType : ""));
			String CourseNature = null != planCourse ?  (null != planCourse.getCourseNature() ? planCourse.getCourseNature() : "") : "";
			vo.setCourseNature(JstlCustomFunction.dictionaryCode2Value("courseNature",CourseNature));
			
			if(null != rs.getStydyHour()){
				vo.setStydyHour(rs.getStydyHour().toString());
			}else{
				TeachingPlan tp = studentInfo.getTeachingPlan();
				Set<TeachingPlanCourse> tpcSet = null != tp ?  tp.getTeachingPlanCourses() : null;
				
				if(null != tpcSet && tpcSet.size() > 0){
					for(TeachingPlanCourse v :tpcSet){
						if(v.getCourse().getResourceid().equals(rs.getCourse().getResourceid())){
							vo.setStydyHour(null==v.getStydyHour()?"":v.getStydyHour().toString());
						}
					}
				}
			}
			
			if("thesis".equals(courseType)&&ExStringUtils.isNotBlank(rs.getExamsubId())){//毕业论文
				ExamSub sub = examSubService.get(rs.getExamsubId());
				vo.setPassTime(sub.getGraduateAuditDate());
			} else {
				vo.setExamStartTime(null==rs.getExamInfo()?null:rs.getExamInfo().getExamStartTime());
				vo.setExamEndTime(null==rs.getExamInfo()?null:rs.getExamInfo().getExamEndTime());
			}			
			vo.setIsmakeupexam(rs.getIsMakeupExam()); //补考成绩
			vo.setIsdelayexam(rs.getIsDelayExam());//延考成绩
			vo.setExamResultsType(courseScoreType);
			vo.setExamAbnormityCode(rs.getExamAbnormity());
			vo.setIsOutplancourse(isPlanOutCourse);
			vo.setCourseType(JstlCustomFunction.dictionaryCode2Value("CodeCourseType", courseType));
			vo.setCourseTypeCode(courseType);
			vo.setExamType(JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",rs.getCourse().getExamType()!=null?rs.getCourse().getExamType().toString():""));
			ExamSub sub = examSubService.get(rs.getExamsubId());
			vo.setExamSubType(null != sub ? sub.getExamType() : ""); //对应字典 ExamResult
			String term = null == planCourse ? "" : planCourse.getTerm();
			vo.setCourseTerm(ExStringUtils.isEmpty(term) ? "6" : term);//课程学期s
			
			if (!Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(courseScoreType)) {
				vo.setExamResultsChs(convertScoreStr(courseScoreType,integratedScore));
			}
			if (!"0".equals(examAbnormity)) {
				vo.setIsPass(Constants.BOOLEAN_NO);
				vo.setInCreditHour(0D);
				vo.setCreditHour(0D);
				vo.setUsuallyScore(null==rs.getUsuallyScore()?"0":rs.getUsuallyScore());
				if(ExStringUtils.isNotEmpty(printExam) && "true".equals(printExam) 
						&&"1".equals(examAbnormity)){
					vo.setExamResultsChs(ExamResults.CHEAT_SCORE);
					vo.setWrittenScore(ExamResults.CHEAT_SCORE);
					vo.setIntegratedScore(ExamResults.CHEAT_SCORE);
				} else {
					vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
					vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
					vo.setIntegratedScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
				}
				vo.setIntegratedScoreL(0L);
			} else {
				vo.setIsPass(isPassScore(courseScoreType,integratedScoreD));
				vo.setCreditHour(creditHour);
				vo.setInCreditHour(creditHour);
				vo.setUsuallyScore(convertScore(courseScoreType,rs.getUsuallyScore()));
				vo.setWrittenScore(convertScore(courseScoreType,rs.getWrittenScore()));
				vo.setIntegratedScore(convertScore(courseScoreType,rs.getIntegratedScore()));
				vo.setIntegratedScoreL(Double.valueOf(convertScore(courseScoreType,rs.getIntegratedScore())).longValue());
			}
			vo.setBatchName(null == sub ? "" : sub.getBatchName());//考试批次名称
			vo.setExamSubId(null == sub ? "" : sub.getResourceid());//考试批次ID
//			vo.setIsMakeupExam(rs.getIsMakeupExam());//是否补考
			String isBK = null == rs.getExamInfo() ? "" : rs.getExamInfo().getExamSub().getExamType();
			vo.setIsMakeupExam(isBK);//是否补考
			//增加考试结果id和标记删除,20161017
			vo.setExamResultId(rs.getResourceid());
			vo.setIsMarkDelete(rs.getIsMarkDelete());
			
			resultsList.add(vo);
		}*/
		
		return resultsList;
	}
	

	/**
	 * 获取学生所属教学计划统考课程对应表
	 * @param studentInfo
	 * @return
	 */
	private Map<String,TeachingPlanCourse>  getStatExamCourseByStudent(StudentInfo studentInfo){
		
		Map<String,TeachingPlanCourse> secMap   = new HashMap<String, TeachingPlanCourse>();
		StringBuffer courseIds 					= new StringBuffer();
		Set<TeachingPlanCourse> planCourses     = studentInfo.getTeachingPlan().getTeachingPlanCourses();
		
		for(TeachingPlanCourse course:planCourses){
			courseIds.append(",'"+course.getResourceid()+"'");
		}
		/*
		String stateExamCoursesHQL              = " from "+StateExamCourse.class.getSimpleName()
												+ " course where course.isDeleted=0 and course.teachingPlanCourse.resourceid in (";
		
		if(ExStringUtils.isNotEmpty(courseIds.toString())&&courseIds.toString().length()>1){
			stateExamCoursesHQL += courseIds.toString().substring(1)+")";
		}else {
			stateExamCoursesHQL += "'')";
		}										
		
		List<StateExamCourse> stateExamCourses  = stateExamCourseService.findByHql(stateExamCoursesHQL.toString());
		
		if (null!=stateExamCourses && stateExamCourses.size()>0) {
			for (StateExamCourse course:stateExamCourses) {
				secMap.put(course.getStateExamCourse().getResourceid(), course.getTeachingPlanCourse());
			}
		}
		*/
		return secMap;
	}
	/**
	 *  获取学生统考、学位课成绩VO
	 * @param studentInfo
	 * @return
	 * @throws ParseException
	 */
	private List<StudentExamResultsVo> getStatExamResultsVo(StudentInfo studentInfo) throws Exception{
		
		Map<String, Object> condition           = new HashMap<String, Object>();
		List <StudentExamResultsVo> resultsList = new ArrayList<StudentExamResultsVo>();
		
		//统考课程-教学计划课程对照表
		Map<String,TeachingPlanCourse>  secMap  = getStatExamCourseByStudent(studentInfo);
		//学籍ID 
		condition.put("studentId", studentInfo.getResourceid());  
		                     
		List<StateExamResults> statExamResults  = stateExamResultsService.findStateExamResultsByCondition(condition);
		
		if (null!=statExamResults && statExamResults.size()>0) {
			
			for(StateExamResults stat:statExamResults) {
				
				TeachingPlanCourse planCourse   = secMap.get(stat.getCourse().getResourceid());
				StudentExamResultsVo vo 		= new StudentExamResultsVo();
				
				vo.setCourseId(stat.getCourse().getResourceid());
				vo.setCourseCode(stat.getCourse().getCourseCode());
				vo.setCourseEnName(ExStringUtils.trimToEmpty(stat.getCourse().getCourseEnName()));
				vo.setCourseName(stat.getCourse().getCourseName());
				vo.setClassesid(studentInfo.getClasses().getResourceid());
				vo.setIsPass(stat.getIsIdented());
				vo.setExamResultsType("20");//成绩类型为字符型
				vo.setPassTime(stat.getPasstime());
				vo.setExamStartTime(stat.getPasstime());
				vo.setExamEndTime(stat.getPasstime());
				vo.setStudyNo(studentInfo.getStudyNo());
				vo.setStudentName(studentInfo.getStudentName());
				vo.setResourceid("state_"+stat.getCourse().getResourceid());
				if (null!=planCourse) {
					Double creditHour =null==planCourse.getCreditHour()?0D:planCourse.getCreditHour();
					String stydyHour  =null==planCourse.getStydyHour()?"0":planCourse.getStydyHour().toString();
					vo.setCreditHour(creditHour);
					vo.setStydyHour(stydyHour);
					vo.setIsOutplancourse(Constants.BOOLEAN_NO);
					
					if (Constants.BOOLEAN_YES.equals(stat.getIsIdented())) {
						vo.setInCreditHour(creditHour);
						vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("StateExamResultsScoreType", stat.getScoreType()));
						vo.setCheckStatus("成绩已认定 ");
						vo.setCheckStatusCode("4");
						vo.setIntegratedScoreL(75L);
					}else {
						vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("StateExamResultsScoreType", stat.getScoreType()));
						vo.setIntegratedScoreL(0L);
						vo.setInCreditHour(0D);
						vo.setCheckStatus("成绩未认定 ");
					
					}
				}else {
					
					Double creditHour = null==stat.getCourse().getPlanoutCreditHour()?0D:stat.getCourse().getPlanoutCreditHour();
					String studyHour  = null==stat.getCourse().getPlanoutStudyHour()?"0":stat.getCourse().getPlanoutStudyHour().toString();
					
					vo.setCreditHour(creditHour);
					vo.setStydyHour(studyHour);
					vo.setInCreditHour(creditHour);

					if (Constants.BOOLEAN_YES.equals(stat.getIsIdented())) {
						vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("StateExamResultsScoreType", stat.getScoreType()));
						vo.setIntegratedScore(JstlCustomFunction.dictionaryCode2Value("StateExamResultsScoreType", stat.getScoreType()));
						vo.setCheckStatusCode("4");
						vo.setCheckStatus("成绩已认定 ");
						vo.setIntegratedScoreL(75L);
					}else {
						vo.setExamResultsChs("成绩未认定 ");
						vo.setIntegratedScore("成绩未认定 ");
						vo.setIntegratedScoreL(0L);
						vo.setCheckStatus("成绩未认定 ");
					}
				}
				
				if (Constants.BOOLEAN_YES.equals(stat.getCourse().getIsDegreeUnitExam())) {
					vo.setCourseType("学位课程");
					vo.setCourseTypeCode("11");
					vo.setIsDegreeUnitExam(Constants.BOOLEAN_YES);
					vo.setMemo(ExStringUtils.trimToEmpty(stat.getMemo()));
				}
				if (Constants.BOOLEAN_YES.equals(stat.getCourse().getIsUniteExam())) {
					vo.setCourseType("统考课程");
					vo.setCourseTypeCode("11");
					vo.setIsStateExamResults(Constants.BOOLEAN_YES);
					vo.setMemo(ExStringUtils.trimToEmpty(stat.getMemo()));
				}
				vo.setExamAbnormityCode("0");
				
				resultsList.add(vo);
			}
		}
		
		return resultsList;
	}
	/**
	 * 获取学生免修、免考成绩VO
	 * @param stu
	 * @param tpc
	 * @return
	 * @throws Exception
	 */
	private  List<StudentExamResultsVo> getNoExamResultsVo(StudentInfo stu,Map<String,TeachingPlanCourse> tpc)throws Exception{
		
		List <StudentExamResultsVo> resultsList = new ArrayList<StudentExamResultsVo>();
		List<NoExamApply> list = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()+" where isDeleted=? and studentInfo.resourceid=? and checkStatus=?  order by course.courseName", 0,stu.getResourceid(),"1");
		
		if(ExCollectionUtils.isNotEmpty(list)){
			for (NoExamApply noExam : list) {
				StudentExamResultsVo vo 		= new StudentExamResultsVo();
				//vo.setResourceid(noExam.getResourceid());
				vo.setResourceid("noexam_"+noExam.getCourse().getResourceid());
				vo.setCourseId(noExam.getCourse().getResourceid());
				vo.setCourseCode(noExam.getCourse().getCourseCode());
				vo.setCourseEnName(ExStringUtils.trimToEmpty(noExam.getCourse().getCourseEnName()));
				vo.setCourseName(noExam.getCourse().getCourseName());
				vo.setExamType(JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",noExam.getCourse().getExamType()!=null?noExam.getCourse().getExamType().toString():""));
				vo.setExamResultsType(noExam.getCourseScoreType());//成绩类型为字符型
				vo.setPassTime(noExam.getCheckTime());
				vo.setExamStartTime(noExam.getCheckTime());
				vo.setExamEndTime(noExam.getCheckTime());
				vo.setIsNoExam(Constants.BOOLEAN_YES);//免考
				vo.setCheckStatus(JstlCustomFunction.dictionaryCode2Value("CodeUnScoreStyle", noExam.getUnScore()));
				vo.setCheckStatusCode("4");
				vo.setIsPass(isPassScore(noExam.getCourseScoreType(),null==noExam.getScoreForCount()?75D:noExam.getScoreForCount()));
				//vo.setIntegratedScore(noExam.getScoreForCount()!=null?noExam.getScoreForCount().toString():"0");
				vo.setIntegratedScoreL(noExam.getScoreForCount()!=null?noExam.getScoreForCount().longValue():75);
				vo.setIntegratedScore(noExam.getScoreForCount()!=null?noExam.getScoreForCount()+"":"75");
				vo.setIsunScore(noExam.getUnScore());//免修免考
				vo.setExamAbnormityCode("0");
				vo.setStudyNo(stu.getStudyNo());
				vo.setStudentName(stu.getStudentName());
				vo.setClassesid(stu.getClasses().getResourceid());
				if(tpc.containsKey(noExam.getCourse().getResourceid())){
					TeachingPlanCourse pc = (TeachingPlanCourse) tpc.get(noExam.getCourse().getResourceid());
					vo.setCourseType(JstlCustomFunction.dictionaryCode2Value("CodeCourseType", pc.getCourseType()));
					vo.setCourseTypeCode(pc.getCourseType());
					vo.setCreditHour(pc.getCreditHour()!=null?pc.getCreditHour():0D);
					vo.setInCreditHour(pc.getCreditHour()!=null?pc.getCreditHour():0D);
					vo.setStydyHour(pc.getStydyHour()!=null?pc.getStydyHour().toString():"0");
					vo.setIsOutplancourse(Constants.BOOLEAN_NO);
					String CourseNature = null != pc ?  (null != pc.getCourseNature() ? pc.getCourseNature() : "") : "";
					vo.setCourseNature(JstlCustomFunction.dictionaryCode2Value("courseNature",CourseNature));
					String examClassType = pc.getExamClassType() != null ? pc.getExamClassType() : "";
					vo.setExamClassType(JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", null != examClassType ? examClassType : ""));
				} else {
					vo.setCourseType(JstlCustomFunction.dictionaryCode2Value("CodeCourseType","22"));
					vo.setCourseTypeCode("22");
					vo.setIsOutplancourse(Constants.BOOLEAN_YES);
					vo.setCreditHour(null==noExam.getCourse().getPlanoutCreditHour()?0D:noExam.getCourse().getPlanoutCreditHour());
					vo.setStydyHour(null==noExam.getCourse().getPlanoutStudyHour()?"0":noExam.getCourse().getPlanoutStudyHour().toString());
					vo.setInCreditHour(null==noExam.getCourse().getPlanoutCreditHour()?0D:noExam.getCourse().getPlanoutCreditHour());
				}
				//课程学期   如果教学计划内找不到这么课程就筛入教学计划外
				String term = null == tpc.get(noExam.getCourse().getResourceid()) ? "6" : tpc.get(noExam.getCourse().getResourceid()).getTerm();
				vo.setCourseTerm(term);
				long year = stu.getGrade().getYearInfo().getFirstYear() + (Integer.parseInt(term)-1)/2;
				String year_term = year + "_" + (Integer.parseInt(term)%2==0?"2":"1");
				vo.setCourseYearTerm(year_term);
				resultsList.add(vo);
			}
		}
		
		return resultsList;
	}
	/**
	 * 成绩类型转换成百分制
	 * @param code
	 * @param score
	 * @return
	 */
	@Override
	public String convertScore(String code, String score){
		
		String score_100  = null;
		
		if (code == null || ExStringUtils.isEmpty(score)) {
			return "0";
		}
		List<String> scoreTypeList = new ArrayList<String>();
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_TWO);
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_THREE);
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_FOUR);
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_FIVE);
		if(scoreTypeList.contains(code)){
			if (code.equals(Constants.COURSE_SCORE_TYPE_TWO)){
				
				if ("1".equals(score) || "不通过".equals(score)){
					score_100 = "50";
				}else if ("2".equals(score) || "通过".equals(score)){
					score_100 = "75";
				}
				
			}else if (code.equals(Constants.COURSE_SCORE_TYPE_THREE)) {
				
				if("1".equals(score) || "差".equals(score)){
					score_100 = "50";
				}else if ("2".equals(score) || "良".equals(score)){
					score_100 = "75";
				}else if ("3".equals(score) || "优".equals(score)){
					score_100 = "95";
				}
				
			}else if (code.equals(Constants.COURSE_SCORE_TYPE_FOUR)) {
				
				if("1".equals(score) || "差".equals(score)){
					score_100 = "50";
				}else if ("2".equals(score) || "中".equals(score)){
					score_100 = "75";
				}else if ("3".equals(score) || "良".equals(score)){
					score_100 = "85";
				}else if ("4".equals(score) || "优".equals(score)){
					score_100 = "95";
				}
				
			}else if (code.equals(Constants.COURSE_SCORE_TYPE_FIVE)){
				
				if("1".equals(score) || "不及格".equals(score)){
					score_100 = "50";
				}else if ("2".equals(score) || "及格".equals(score)){
					score_100 = "65";
				}else if ("3".equals(score) || "中".equals(score)){
					score_100 = "75";
				}else if ("4".equals(score) || "良".equals(score)){
					score_100 = "85";
				}else if ("5".equals(score) || "优".equals(score)){
					score_100 = "95";
				}
			}
			if(score_100==null){
				List<Dictionary> dicList = CacheAppManager.getChildren("CodeScoreChar");
				for (Dictionary d : dicList) {
					if(d.getDictValue().equalsIgnoreCase(score) || (d.getDictCode().startsWith(code)) &&
							d.getDictName().equals(score)){
						score_100 = d.getShowOrder()+"";
						break;
					}
				}
			}
		}else{
			score_100     = score;
		}
		
		return score_100;
	}
	/**
	 * 非百分制成绩转换成字符类型
	 * @param code
	 * @param score
	 * @return
	 */
	@Override
	public String convertScoreStr(String code, String score){
		
		String score_Str  = null;
		
		if (!ExStringUtils.isNotBlank4All(code,score)) {
			return "0";
		}
		List<String> scoreTypeList = new ArrayList<String>();
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_TWO);
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_THREE);
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_FOUR);
		scoreTypeList.add(Constants.COURSE_SCORE_TYPE_FIVE);
		if(scoreTypeList.contains(code)){
			if (code.equals(Constants.COURSE_SCORE_TYPE_TWO)){
				if ("1".equals(score)){
					score_Str = "不通过";
				}else if ("2".equals(score)){
					score_Str = "通过";
				}
			}else if (code.equals(Constants.COURSE_SCORE_TYPE_THREE)) {
				if("1".equals(score)){
					score_Str = "差";
				}else if ("2".equals(score)){
					score_Str = "良";
				}else if ("3".equals(score)){
					score_Str = "优";
				}
			}else if (code.equals(Constants.COURSE_SCORE_TYPE_FOUR)) {
				if("1".equals(score)){
					score_Str = "差";
				}else if ("2".equals(score)){
					score_Str = "中";
				}else if ("3".equals(score)){
					score_Str = "良";
				}else if ("4".equals(score)){
					score_Str = "优";
				}
			}else if (code.equals(Constants.COURSE_SCORE_TYPE_FIVE)){
				if("1".equals(score)){
					score_Str = "不及格";
				}else if ("2".equals(score)){
					score_Str = "及格";
				}else if ("3".equals(score)){
					score_Str = "中";
				}else if ("4".equals(score)){
					score_Str = "良";
				}else if ("5".equals(score)){
					score_Str = "优";
				}
				
			}
			if(score_Str==null){
				List<Dictionary> dicList = CacheAppManager.getChildren("CodeScoreChar");
				for (Dictionary d : dicList) {
					if(d.getDictValue().equalsIgnoreCase(score)){
						score_Str = d.getDictName();
						break;
					}
				}
			}
		}else{
			score_Str = score;
		}
		return score_Str;
	}
	/**
	 * 判断一个成绩是否通过
	 * @param code
	 * @param score
	 * @return
	 */
	@Override
	public String isPassScore(String code, double score){
		if (ExStringUtils.isBlank(code)) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_TWO) && score < 75) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_THREE)&& score < 60) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_FOUR)&& score < 60) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_FIVE) && score < 65) {
			return "N";
		}
		if (code.equals(Constants.COURSE_SCORE_TYPE_ONEHUNHRED) && score < 60) {
			return "N";
		}
		return "Y";
	}

	/**
	 * 获取学生指定课程的最大成绩
	 * @param stu
	 * @param courseId
	 * @param isPass
	 * @return
	 * @throws Exception
	 */
	@Override
	public ExamResults getStudentPlanCourseMaxExamResults(StudentInfo stu, String courseId,boolean isPass) throws ServiceException {
		
		StringBuffer hql       = new StringBuffer();
		hql.append(" from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0 and rs.checkStatus=4 and rs.integratedScore is not null and rs.studentInfo.resourceid=? and rs.course.resourceid=?");
		List<ExamResults> list = (List<ExamResults>) exGeneralHibernateDao.findByHql(hql.toString(), stu.getResourceid(),courseId);
		//指定科目有发布的不为空的成绩
		if (null!=list && list.size()>0) {
			
			ExamResults maxRs  = null;
			for (ExamResults r : list) {
				//最大成绩不为空，比较下一条成绩
				if (null!=maxRs) {		
					String score100                 = convertScore(r.getCourseScoreType(),r.getIntegratedScore());
					Double score100D                = 0.0;
					if (ExStringUtils.isNotEmpty(score100)) {
						score100D                   = Double.parseDouble(score100);
					}

					String maxResultIntegratedscore = maxRs.getIntegratedScore();
					String maxResultCoursescoretype = maxRs.getCourseScoreType();
					
					String maxResultScore100        = convertScore(maxResultCoursescoretype,maxResultIntegratedscore);
					Double maxResultScore100D       = 0.0;
					if (ExStringUtils.isNotEmpty(maxResultScore100)) {
						maxResultScore100D          = Double.parseDouble(maxResultScore100);
					}
					
					
					if (score100D>maxResultScore100D) {
						maxRs = r;
					}
					
				//最大成绩为空，将当前成绩赋值给它	
				}else {
					maxRs      = r ;
				}			
			}
			
			//要求通过的成绩则判断最大成绩通过才返回，否则返回空值
			if (isPass) {	
				String pass    = isPassScore(maxRs.getCourseScoreType(),Double.parseDouble(maxRs.getIntegratedScore()));
				if ("Y".equals(pass)) {
					return maxRs;
				}else {
					return null;
				}
				
			//不要求通过的成绩返回最大成绩	
			}else {
				return maxRs;
			}
			
		//指定科目没有发布的不为空的成绩	
		}else {
			return null;
		}
	}


	/**
	 * 获取待毕业学生所有发布的有效成绩
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> studentToGraduatePublishedExamResultsList(Map<String, Object> condition) {
		List<Map<String, Object>> results =  new ArrayList<Map<String,Object>>();
		StringBuffer sql 				  = new StringBuffer();
		 sql.append(" select   t.studentid,t.courseid,t.integratedscore ,t.writtenscore,c.coursename ");
		 sql.append(" from edu_teach_examresults t  ");
		 sql.append("  inner join edu_base_course c on t.courseid = c.resourceid    ");
		 sql.append("  where t.isdeleted    = 0  ");
		 sql.append("  and t.checkstatus  = '4'  ");
		 sql.append(" and t.examabnormity= '0'   ");
		 sql.append("  and t.integratedscore is not null   ");
		 if (condition.containsKey("studentId")) {
				sql.append(" and t.studentid    ="+condition.get("studentId"));
		 }
		 
		 sql.append(" 	and  exists (  ");
		 sql.append(" 	select info.resourceid from edu_roll_studentinfo info ,edu_teach_plan pla,edu_base_grade grade ,edu_base_year y ,edu_teach_graduatedata gra ");
		 sql.append(" 	where info.isdeleted = 0 and info.resourceid = t.studentid  ");
		 sql.append(" 	and info.studentbaseinfoid is not null  ");
		 sql.append("   and info.enterauditstatus='Y'  ");
		 if(condition.containsKey("refresh")){
			 sql.append("   and (info.studentstatus='11' or info.studentstatus='21' or info.studentstatus='22' )  ");
		 }else{
			 sql.append("   and (info.studentstatus='11' or info.studentstatus='21' or info.studentstatus='22' or info.studentstatus='16' )  ");
		 }
		 sql.append("   and pla.resourceid=info.teachplanid  ");
		 sql.append("   and info.gradeid = grade.resourceid  ");
		 sql.append("   and grade.yearid = y.resourceid and info.resourceid = t.studentid ");
		 if(!condition.containsKey("studentId")){
			 sql.append(" and gra.studentid=info.resourceid ");
			 if(condition.containsKey("branchSchool")) {
					sql.append(" and info.branchschoolid = '"+condition.get("branchSchool")+"'");
			 }
			 if(condition.containsKey("major")) {
					sql.append(" and info.majorid = '"+condition.get("major")+"'");
			 }
			 if(condition.containsKey("classic")) {
					sql.append(" and info.classicid = '"+condition.get("classic")+"'");
			 }
			 if(condition.containsKey("name")) {
					sql.append(" and info.studentname like '"+condition.get("name")+"%'");
			 }
			 if(condition.containsKey("matriculateNoticeNo")) {
					sql.append(" and info.studyno like '"+condition.get("matriculateNoticeNo")+"'");
			 }
			 if(condition.containsKey("grade")) {
					sql.append(" and info.gradeid = '"+condition.get("grade")+"'");
			 }
			 if(condition.containsKey("stus")){
					sql.append(" and gra.resourceid in ("+condition.get("stus")+")");
			 }
			
		 }
		  
		 //sql.append("   and (to_number(replace(substr(pla.eduyear,0,3),'年',''))+to_number(y.firstyear+(grade.term-1)*0.5+0.5))<=(0.5+to_number('"+condition.get("thisYear")+"'))   ");
		 sql.append(" 	)order by t.studentid,t.courseid  ");
		try {
			results                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
 			for (Map<String, Object> map:results) {
				String integratedscore             = (String)map.get("INTEGRATEDSCORE");
				String writtenscore                = (String)map.get("WRITTENSCORE");
				String studentId                   = (String)map.get("STUDENTID");
				ScoreEncryptionDecryptionUtil util = ScoreEncryptionDecryptionUtil.getInstance();
				String decryptedWrScore            = util.decrypt(studentId, writtenscore);
				String decryptedScore              = util.decrypt(studentId, integratedscore);
				if("".equals(decryptedScore)) {
					map.put("INTEGRATEDSCORE", 0);
				}
				if("".equals(decryptedWrScore)) {
					map.put("WRITTENSCORE", 0);
				}
				if("".equals(decryptedScore)||"".equals(decryptedWrScore)){
					continue;
				}
				map.put("INTEGRATEDSCORE", decryptedScore);
				map.put("WRITTENSCORE", decryptedWrScore);
				
			}
			
		} catch (Exception e) {
			logger.error("查询学生所有有效的发布的成绩出错:{}"+e.fillInStackTrace());
		}
		return results;
	}
	/**
	 * 获取待学位审核学生所有的免试成绩
	 * @param condition
	 * @return
	 */
	@Override
	public List<Map<String, Object>> studentToGraduateNoExamResultsList(Map<String, Object> condition) {
		List<Map<String, Object>> results =  new ArrayList<Map<String,Object>>();
		StringBuffer sql 				  = new StringBuffer();
		 sql.append(" select   t.studentid,t.courseid,t.checkstatus,t.scoreforcount \"integratedscore\",c.coursename ");
		 sql.append(" from edu_teach_noexam t ,edu_base_course c ");
		 sql.append(" where t.courseid = c.resourceid and t.isdeleted  = 0  ");
		 sql.append(" and t.checkstatus= 1 ");
		 if (condition.containsKey("studentId")) {
				sql.append(" and t.studentid    ="+condition.get("studentId"));
		 }
		 sql.append(" 	and  exists (  ");
		 sql.append(" 	select info.resourceid from edu_roll_studentinfo info ,edu_teach_plan pla,edu_base_grade grade ,edu_base_year y ,edu_teach_graduatedata gra ");
		 sql.append(" 	where info.isdeleted = 0 and info.resourceid = t.studentid  ");
		 sql.append(" 	and info.studentbaseinfoid is not null  ");
		 sql.append("   and info.enterauditstatus='Y'  ");
		 if(condition.containsKey("refresh")){
			 sql.append("   and (info.studentstatus='11' or info.studentstatus='21' or info.studentstatus='22' )  ");
		 }else{
			 sql.append("   and (info.studentstatus='11' or info.studentstatus='21' or info.studentstatus='22' or info.studentstatus='16' )  ");
		 }
		 sql.append("   and pla.resourceid=info.teachplanid  ");
		 sql.append("   and info.gradeid = grade.resourceid  ");
		 sql.append("   and grade.yearid = y.resourceid and info.resourceid = t.studentid ");
		 if(!condition.containsKey("studentId")){
			 sql.append(" and gra.studentid=info.resourceid ");
			 if(condition.containsKey("branchSchool")) {
					sql.append(" and info.branchschoolid = '"+condition.get("branchSchool")+"'");
			 }
			 if(condition.containsKey("major")) {
					sql.append(" and info.majorid = '"+condition.get("major")+"'");
			 }
			 if(condition.containsKey("classic")) {
					sql.append(" and info.classicid = '"+condition.get("classic")+"'");
			 }
			 if(condition.containsKey("name")) {
					sql.append(" and info.studentname like '"+condition.get("name")+"%'");
			 }
			 if(condition.containsKey("matriculateNoticeNo")) {
					sql.append(" and info.studyno like '"+condition.get("matriculateNoticeNo")+"'");
			 }
			 if(condition.containsKey("grade")) {
					sql.append(" and info.gradeid = '"+condition.get("grade")+"'");
			 }
			 if(condition.containsKey("stus")){
					sql.append(" and gra.resourceid in ("+condition.get("stus")+")");
			 }
			
		 }
		 sql.append(" 	)order by t.studentid,t.courseid  ");
		try {
			results                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
		} catch (Exception e) {
			logger.error("查询学生所有免试（不含通过、免修、代修）成绩出错:{}"+e.fillInStackTrace());
		}
		return results;
	}

	/**
	 *  获取学生的通过的成绩(最高成绩)
	 * @param studentId
	 * @return
	 */
	@Override
	public Map<String,ExamResults> getStudentPassExamResulst(String studentId){
		//成绩临时存放LIST
		Map<String,ExamResults> rsultsMap = new HashMap<String, ExamResults>();
		Map<String,Object> map  		  = new HashMap<String, Object>();
		
		if (ExStringUtils.isNotEmpty(studentId)) {
			map.put("studentId",studentId);
			//等效学籍的所有有效成绩列表
			List<Map<String, Object>>  results= studentPublishedExamResultsList(map);
			//获取同一科目最大的成绩
			results							  = sortExamResultList(results);
			//获取每一科目的最大通过的成绩
			for (Map<String, Object> rs:results){
				String resourceid             = (String)rs.get("RESOURCEID");			
				String courseid               = (String)rs.get("COURSEID");
				String integratedscore        = (String)rs.get("INTEGRATEDSCORE");
				String coursescoretype        = (String)rs.get("COURSESCORETYPE");
				integratedscore        		  = convertScore(coursescoretype, integratedscore);
				String isPass                 = isPassScore(coursescoretype,Double.valueOf(integratedscore));
				if (Constants.BOOLEAN_YES.equals(isPass)){
					ExamResults ers           = get(resourceid);
					if (ExStringUtils.isNotBlank(ers.getCourseScoreType())&&!"11".equals(ers.getCourseScoreType())) {
						ers.setTempintegratedScore_d(convertScoreStr(coursescoretype,(String)rs.get("INTEGRATEDSCORE")));
					}
					rsultsMap.put(courseid,ers);
				}
			}
		}
		
		return rsultsMap;
	}

//	/**
//	 * 计算学生已获取的总学分数等相关数据
//	 * @return
//	 * @throws ServiceException
//	 */
//	@Override
//	public String calculateTotalCreditHour() throws Exception { 
//		
//		String returnMsg			   = "";
//		//List<StudentInfo> studentInfos = studentinfoservice.findByHql(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0  and exists ( select sk.studentId from "+StudentCheck.class.getSimpleName()+" sk where sk.studentId = stu.resourceid )");
//		List<StudentInfo> studentInfos = studentinfoservice.findByHql(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0  and stu.studyNo = '125202405109'");
//		List<Object[]> paramArray      = new ArrayList<Object[]>();
//		
//		String updateSql               = " update edu_roll_studentinfo stu set stu.finishedcredithour=?,stu.finishednecesscredithour=?,stu.finishedneccesscoursenum=?,stu.finishedoptionalcoursenum=? ,stu.finishedmaincoursenum=? , stu.finishedmaincourseaveragescore=?,stu.finishedbasecoursenum=?,stu.finishedbaseaveragescore=? , stu.graduatepapercore=? where stu.studyno=?";
//		for (StudentInfo stu :studentInfos) {
//			StudentExamResultsVo stataVo= null;
//			try {
//				List<StudentExamResultsVo> list  = studentExamResultsList(stu);
//				if (null!=list&&list.size()>0) {
//					stataVo	   		   = list.get(list.size()-1);
//					Object [] p		   = {stataVo.getTotalCredit(),stataVo.getRequiredCredit(),stataVo.getCompulsoryed(),stataVo.getLimitedCount(),stataVo.getMainCourse(),stataVo.getMainCourseScoreAvg(),stataVo.getBaseCourse(),stataVo.getBaseCourseScoreAvg(),stataVo.getThesisScore(),stu.getStudyNo()}; 
//					paramArray.add(p);
//				}
//			}catch(HibernateException e){ 
//				List<StudentExamResultsVo> list  = studentExamResultsList(studentinfoservice.get(stu.getResourceid()));
//				if (null!=list&&list.size()>0) {
//					stataVo	   		   = list.get(list.size()-1);
//					Object [] p		   = {stataVo.getTotalCredit(),stataVo.getRequiredCredit(),stataVo.getCompulsoryed(),stataVo.getLimitedCount(),stataVo.getMainCourse(),stataVo.getMainCourseScoreAvg(),stataVo.getBaseCourse(),stataVo.getBaseCourseScoreAvg(),stataVo.getThesisScore(),stu.getStudyNo()};
//					paramArray.add(p);
//				}
//			}catch (Exception e) {
//				logger.error("学分计算出错,学号:{}"+stu.getStudyNo());
//				continue;
//			}
//		}
//
//		try {
//			
//			if (!paramArray.isEmpty()) {
//				
//				int[] rows 				= baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(updateSql, paramArray);
//				
//				if (rows.length>0) {
//					returnMsg       	= "计算学生已获取的总学分：计算成功，计算人数："+rows.length;
//				}else {
//					returnMsg       	= "计算学生已获取的总学分：未读取到更新脚本！";
//				}
//				
//				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" delete from  EDU_TEMP_STUDENTCHECK ", null);
//			}else {
//				returnMsg       		= "计算学生已获取的总学分：没有提供要计算学分的学生！";
//			}
//		} catch (Exception e) {
//			returnMsg       			= "计算学生已获取的总学分：计算失败："+e.fillInStackTrace();
//		}
//		
//		return returnMsg;
//	}
	/**
	 * 计算学生已获取的总学分数等相关数据
	 * @return
	 * @throws ServiceException
	 */
	private int successNum = 0; //成功
	private int failNum = 0; //失败
	private List<String> ids = null;
	private int num = 0;

	@Override
	public String calculateTotalCreditHour() throws Exception {		
		StringBuffer returnMsg			   = new StringBuffer("");
		List<Map<String,Object>> listcount = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(" select count(*) as count from EDU_TEMP_STUDENTCHECK ",null);
		int countupdate = (null != listcount && listcount.size() > 0) ? Integer.parseInt((null == listcount.get(0) ? "0" : listcount.get(0).get("count")+"")) : 0; //总数
		String sql = " select distinct sk.studentId from (SELECT A.*, ROWNUM RN FROM EDU_TEMP_STUDENTCHECK A WHERE ROWNUM <= :max) sk where sk.RN >= :min and sk.isdeleted = 0 ";
		int max = 500;
		Double n = countupdate / 500.0;
		int number = 0;
		ids = new ArrayList<String>();
		while ((null != listcount && listcount.size() > 0) || number < n) {
			number++;
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("min", max-500+1);
			map.put("max", max);
			listcount = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql,map);
			returnMsg = setCalculate(listcount,countupdate,num,successNum,failNum);
			max += 500;
		}
		//listcount = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap("select info.resourceid studentId from edu_roll_studentinfo info where info.studyno = '201216392112008' ",null);
		//returnMsg = setCalculate(listcount,1,1,successNum,failNum);
		delStudentCheck(ids);//删除临时表里计算过学分的数据
		return returnMsg.toString();
	}
	private StringBuffer setCalculate(List<Map<String,Object>> c,int countupdate,int num,int successNum,int failNum){
		StringBuffer returnMsg = new StringBuffer("");
		if(null != c && c.size() > 0){
			List<StudentInfo> stuToGrade = new ArrayList<StudentInfo>();
			//需要更新的对象
			List<StudentInfo> updateList = new ArrayList<StudentInfo>();
			for(Map<String,Object> m : c){
				StudentInfo stu = null;
				if(m.containsKey("studentId")){
					stu = studentinfoservice.get(m.get("studentId")+"");
				}
				if(null == stu) {
					continue;
				} else {
					stuToGrade.add(stu);
				}
			}
			Map<String, Map<String, Object>> electiveExamCountMap = new HashMap<String, Map<String,Object>>();
			try {
				electiveExamCountMap = getElectiveExamCountMapByStuList(stuToGrade);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(null != stuToGrade && stuToGrade.size() > 0){
				for (StudentInfo stu :stuToGrade) {
					Integer examCount = Integer.parseInt(electiveExamCountMap.get(stu.getResourceid()).get("examcount").toString());
					try {
						List<StudentExamResultsVo> list  = studentExamResultsList(stu,examCount);
						StudentExamResultsVo stataVo= null;
						if (null!=list&&list.size()>0) {
							stataVo	   		   = list.get(list.size()-1);
							updateList.add(getStudentInfo(stataVo,stu));													
						}else{//没有成绩的学生也应该被删除
							ids.add(stu.getResourceid());
						}
					} catch (Exception e) {						
						logger.info("计算学生已获取的总学分出错,学号:{}"+stu.getStudyNo());
						logger.error("异常信息："+e.fillInStackTrace());
						continue;
					}
				}
				stuToGrade = null; // 不用时置空
				returnMsg = getCalculateTotalCrediHourMsg(updateList,countupdate,num,successNum,failNum);
			}
		}else{
			logger.error("计算学生已获取的总学分：没有学生需要计算学分！");
		}
		return returnMsg;
	}
		
	private StringBuffer getCalculateTotalCrediHourMsg(List<StudentInfo> updateList,int count,int num,int successNum,int failNum){		
		//开始执行更新对象
		try {
			if(!updateList.isEmpty() && updateList.size() > 0){
				Session session = exGeneralHibernateDao.getSessionFactory().openSession();
				Transaction tx = session.beginTransaction();
				for (int i = 0; i < updateList.size(); i++) {
					try {
						num ++;
						if(null == session || !session.isOpen()){ //为了防止执行的时候session为空无法操作的问题
							session = exGeneralHibernateDao.getSessionFactory().openSession();
						}
						if(null == updateList.get(0)){
							continue;
						}
						logger.info("计算学生已获取的总学分：共"+count+"条记录 ，目前计算到第"+num+"条："+updateList.get(i).getStudentName()+"（"+updateList.get(i).getStudyNo()+"）]");
						session.update(updateList.get(i));						
						ids.add(updateList.get(i).getResourceid()); //更新成功后加入删除集合		
						successNum++;
					} catch (Exception e) {
						logger.error("计算学生已获取的总学分：学号:{}"+updateList.get(i).getStudyNo());
						failNum++;
						continue;
					}
				}
				updateList = null;
				tx.commit();
				session.close();				
			}else{
				logger.error("计算学生已获取的总学分：没有学生需要计算学分！");
			}
		} catch (Exception e) {
			logger.error("计算学生已获取的总学分：计算失败："+e.fillInStackTrace());
		}
		return new StringBuffer("计算学生已获取的总学分：成功"+successNum+"条,失败"+failNum+"条,共"+count+"条!");
		
	}
	
	
	/**
	 * 把计算了学分的学生剔除
	 * @param ids
	 * @throws Exception
	 */
	private void delStudentCheck(List<String> ids){
		Map<String,Object> m = new HashMap<String, Object>();
		if(null != ids && ids.size() > 0){
			int i = 0;
			for (String str : ids) {
				i++;
				logger.error("计算学分后剔除临时表数据：共"+ids.size()+"条，目前正在删除到第"+i+"条");
				try {
					m.put("id", str);
					baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap("DELETE from EDU_TEMP_STUDENTCHECK sk where sk.studentid = :id  ",m);
				} catch (Exception e) {
					logger.error("计算学分后剔除临时表数据失败：id"+str);
					e.printStackTrace();
					continue;
				}
			}
		}
		ids = null;
		System.gc();
	}
	
	/**
	 * 装载学分信息
	 * @param vo
	 * @return
	 */
	@SuppressWarnings("finally")
	private StudentInfo getStudentInfo(StudentExamResultsVo vo,StudentInfo stu){
		try {
			stu.setFinishedCreditHour(vo.getTotalCredit());
			stu.setFinishedNecessCreditHour(vo.getRequiredCredit());
			stu.setFinishedNeccessCourseNum(vo.getCompulsoryed());
			stu.setFinishedOptionalCourseNum(vo.getLimitedCount());
			stu.setFinishedMainCourseNum(vo.getMainCourse());
			stu.setFinishedMainCourseAverageScore(vo.getMainCourseScoreAvg());
			stu.setFinishedBaseCourseNum(vo.getBaseCourse());
			stu.setFinishedBaseAverageScore(vo.getBaseCourseScoreAvg());
			stu.setGraduatePaperScore(vo.getThesisScore());
		} catch (Exception e) {
			logger.error("装载学分信息失败:{}"+(null == stu ? "学籍信息为空!" :"学生："+stu.getStudentName()));
		}finally{
			return stu;
		}
	}
	
	/**
	 * 计算学生已获取的总学分数等相关数据2
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public String graduationCalculateTotalCreditHour(List<StudentInfo> stus) throws Exception {		
		StudentExamResultsVo stataVo= null;
		String mes = "计算学分成功;";
		String studentid = "";
		try {
			if(null!=stus && stus.size()>0){
				Session session = exGeneralHibernateDao.getSessionFactory().openSession();
				Transaction tx = session.beginTransaction();
				for(StudentInfo stu : stus){
					studentid = stu.getResourceid();
					List<StudentExamResultsVo> list  = studentExamResultsList(stu,"audit");//审核
					if (null!=list&&list.size()>0) {
						stataVo	   		   = list.get(list.size()-1);
						stu.setFinishedCreditHour(stataVo.getTotalCredit());
						stu.setFinishedNecessCreditHour(stataVo.getRequiredCredit());
						session.merge(stu);
					}
				}
				tx.commit();
				session.close();
			}else{
				mes = "没有要计算学分的学生;";
			}
			
		}catch (Exception e) {
			logger.error("学分计算失败！{studentInfoid:"+studentid+"}");
			e.printStackTrace();
			mes = "计算学分失败;";
		}
		return mes;
	}
	
	@Override
	public String getMaxBKExamResult(Map<String, Object> condition) {
		StringBuffer sql 				  = new StringBuffer();
		String max = "";
		//一次查出正考和补考
		try {
			sql.append(" select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore,sub.examtype,rs.courseScoreType,rs.EXAMABNORMITY EXAMABNORMITY2,er.EXAMABNORMITY EXAMABNORMITY1 ");
			sql.append(" from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
			sql.append(" join EDU_TEACH_EXAMRESULTS er on er.studentid=rs.studentid and er.courseid=rs.courseid and er.isdeleted=0 and er.ismakeupexam='N' and er.checkstatus='4' ");
			sql.append(" join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
			sql.append(" where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype not in('N','Q','R','G') and rs.checkstatus='4' ");
			if (condition.containsKey("studentid")) {
				sql.append(" and rs.studentid=:studentid ");
			}
			if (condition.containsKey("courseid")) {
				sql.append(" and rs.courseid=:courseid ");
			}
			sql.append(" order by to_number(nvl(f_decrypt_score(rs.integratedscore, stu.resourceid),'0')) desc");
			List<Map<String, Object>> results2                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
			if (results2 != null && results2.size() > 0) {
				Map<String, Object> result2 = results2.get(0);//补考成绩
				if("2".equals(result2.get("EXAMABNORMITY2"))){//缺考
					max = "缺考";
				}else {
					max = result2.get("INTEGRATEDSCORE") != null ? convertScoreStr(result2.get("COURSESCORETYPE").toString(),result2.get("INTEGRATEDSCORE").toString()) : "";
				}
			}
		} catch (Exception e) {
			logger.error("查询学生最大补考成绩出错:{}"+e.fillInStackTrace());
		}
		return max;
	}
	
	/**
	 * 获取结补成绩，暂时未加入返校一补（R）和返校二补（G）
	 */
	@Override
	public String getJBExamResult(Map<String, Object> condition) {
		List<Map<String, Object>> results =  new ArrayList<Map<String,Object>>();		
		StringBuffer sql 				  = new StringBuffer();
		String jb = "";
		sql.append("select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore,rs.COURSESCORETYPE ");
		sql.append("from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
		sql.append("join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
		sql.append("where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype='Q' and rs.checkstatus='4' ");
		if (condition.containsKey("studentid")) {
			sql.append(" and rs.studentid=:studentid ");
		}
		if (condition.containsKey("courseid")) {
			sql.append(" and rs.courseid=:courseid ");
		}
		sql.append(" order by f_decrypt_score(rs.integratedscore, stu.resourceid) desc");
		try {
			results                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
			
			if (results != null && results.size() > 0) {
				Map<String, Object> result = results.get(0);
				String courseScoreType = result.get("COURSESCORETYPE").toString();
				jb = results.get(0).get("INTEGRATEDSCORE") != null ? convertScoreStr(courseScoreType,results.get(0).get("INTEGRATEDSCORE").toString()) : "";
			}
		} catch (Exception e) {
			logger.error("查询学生结补成绩出错:{}"+e.fillInStackTrace());
		}
		return jb;
	}
	
	/**
	 * 1、显示正考成绩
	 * 2、如果申请了免修免考则显示：免+成绩（默认75分）
	 * 3、如果是缓考，并且有一补成绩则显示：缓考+一补成绩；否则显示缓考
	 */
	@Override
	public String getExamResult(Map<String, Object> condition) {
		List<Map<String, Object>> results =  new ArrayList<Map<String,Object>>();		
		StringBuffer sql 				  = new StringBuffer();
		String rs = "";
		sql.append("select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore2,rs.* ");
		sql.append("from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
		sql.append("join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
		sql.append("where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype='N' and rs.checkstatus='4' ");
		if (condition.containsKey("studentid")) {
			sql.append(" and rs.studentid=:studentid ");
		}
		if (condition.containsKey("courseid")) {
			sql.append(" and rs.courseid=:courseid ");
		}
		sql.append(" order by f_decrypt_score(rs.integratedscore, stu.resourceid) desc");
		try {
			results                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
			if (results != null && results.size() > 0) {
				Map<String, Object> result = results.get(0);
				String courseScoreType = result.get("COURSESCORETYPE").toString();
				// 如果该门课程已申请了免修免考，则只显示免考成绩（免+成绩），不显示正考成绩
				List<NoExamApply> list = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()
						+" where isDeleted=? and studentInfo.resourceid=? and checkStatus=? and course.resourceid=?"
						, 0,condition.get("studentid").toString(),"1",condition.get("courseid").toString());
				if(ExCollectionUtils.isNotEmpty(list)){
					NoExamApply noExam = list.get(0);
					rs = "免" + (noExam.getScoreForCount()!=null?noExam.getScoreForCount().toString():"75");
				} else {
					if (result.get("EXAMABNORMITY") != null && "0".equals(result.get("EXAMABNORMITY").toString())) {//正考
						rs = results.get(0).get("INTEGRATEDSCORE2") != null ? convertScoreStr(courseScoreType,results.get(0).get("INTEGRATEDSCORE2").toString()) : "";
					} else if (result.get("EXAMABNORMITY") != null && "1".equals(result.get("EXAMABNORMITY").toString())) {//作弊
						rs = ExamResults.CHEAT_SCORE;
					}  else if (result.get("EXAMABNORMITY") != null && "2".equals(result.get("EXAMABNORMITY").toString())) {//缺考
						rs = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", "2");
					} else if (result.get("EXAMABNORMITY") != null && "5".equals(result.get("EXAMABNORMITY").toString())) {//缓考
						sql.setLength(0);
						sql.append("select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore2,rs.* ");
						sql.append("from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
						sql.append("join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
						sql.append("where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype='Y' and rs.checkstatus='4' ");
						if (condition.containsKey("studentid")) {
							sql.append(" and rs.studentid=:studentid ");
						}
						if (condition.containsKey("courseid")) {
							sql.append(" and rs.courseid=:courseid ");
						}
						sql.append(" order by f_decrypt_score(rs.integratedscore, stu.resourceid) desc");
						results                                = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
						// 如果还没有进行一补，则显示缓考，否则显示缓考+一补成绩
						rs = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", "5");
						if (results != null && results.size() > 0) {
							result = results.get(0);
							rs += (results.get(0).get("INTEGRATEDSCORE2") != null ? convertScoreStr(courseScoreType,results.get(0).get("INTEGRATEDSCORE2").toString()) : "");
						}
					}
				}
			}
				
			/*if ("".equals(rs)) {
				List<NoExamApply> list = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()
						+" where isDeleted=? and studentInfo.resourceid=? and checkStatus=? and course.resourceid=?"
						, 0,condition.get("studentid").toString(),"1",condition.get("courseid").toString());
				if(ExCollectionUtils.isNotEmpty(list)){
					NoExamApply noExam = list.get(0);
					rs = "免" + (noExam.getScoreForCount()!=null?noExam.getScoreForCount().toString():"75");
				}
			}*/
		} catch (Exception e) {
			logger.error("查询学生成绩出错:{}"+e.fillInStackTrace());
		}
		return rs;
	}
	
	/**
	 * 获取正考的综合成绩（未处理和处理后）
	 * @param condition
	 * @return
	 */
	@Override
	public Map<String,Object> getNormalExamResult(Map<String, Object> condition) {
		List<Map<String, Object>> results =  new ArrayList<Map<String,Object>>();		
		Map<String,Object> rs = new HashMap<String, Object>();
		StringBuffer sql 				  = new StringBuffer();
		String handledRs = "";
		Double original = 0D;
		String examAbnormity = "0";
		boolean isNormal = false;
		sql.append("select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore2,rs.* ");
		sql.append("from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
		sql.append("join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
		sql.append("where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype='N' and rs.checkstatus='4' ");
		if (condition.containsKey("studentid")) {
			sql.append(" and rs.studentid=:studentid ");
		}
		if (condition.containsKey("courseid")) {
			sql.append(" and rs.courseid=:courseid ");
		}
		sql.append(" order by f_decrypt_score(rs.integratedscore, stu.resourceid) desc");
		try {
			results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
			if (results != null && results.size() > 0) {
				Map<String, Object> result = results.get(0);
				String courseScoreType = result.get("COURSESCORETYPE").toString();
				examAbnormity = result.get("EXAMABNORMITY").toString();
				// 如果该门课程已申请了免修免考，则只显示免考成绩（免+成绩），不显示正考成绩
				List<NoExamApply> list = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()
						+" where isDeleted=? and studentInfo.resourceid=? and checkStatus=? and course.resourceid=?"
						, 0,condition.get("studentid").toString(),"1",condition.get("courseid").toString());
				if(ExCollectionUtils.isNotEmpty(list)){
					NoExamApply noExam = list.get(0);
					isNormal = true;
					original = (noExam.getScoreForCount()!=null?noExam.getScoreForCount():75D);
					handledRs = "免" + original.toString();
				} else {
					if (result.get("EXAMABNORMITY") != null && "0".equals(result.get("EXAMABNORMITY").toString())) {//正考
						isNormal = true;
						original = results.get(0).get("INTEGRATEDSCORE2") != null ? Double.valueOf(convertScoreStr(courseScoreType,results.get(0).get("INTEGRATEDSCORE2").toString())): 0D;
						handledRs = original.toString();
					} else if (result.get("EXAMABNORMITY") != null && "1".equals(result.get("EXAMABNORMITY").toString())) {//作弊
						handledRs = ExamResults.CHEAT_SCORE;
					}  else if (result.get("EXAMABNORMITY") != null && "2".equals(result.get("EXAMABNORMITY").toString())) {//缺考
						handledRs = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", "2");
					} else if (result.get("EXAMABNORMITY") != null && "5".equals(result.get("EXAMABNORMITY").toString())) {//缓考
						sql.setLength(0);
						sql.append("select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore2,rs.* ");
						sql.append("from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
						sql.append("join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
						sql.append("where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype='Y' and rs.checkstatus='4' ");
						if (condition.containsKey("studentid")) {
							sql.append(" and rs.studentid=:studentid ");
						}
						if (condition.containsKey("courseid")) {
							sql.append(" and rs.courseid=:courseid ");
						}
						sql.append(" order by f_decrypt_score(rs.integratedscore, stu.resourceid) desc");
						results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
						// 如果还没有进行一补，则显示缓考，否则显示缓考+一补成绩
						handledRs = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", "5");
						if (results != null && results.size() > 0) {
							result = results.get(0);
							isNormal = true;
							// TODO:注意：INTEGRATEDSCORE2这个值只能是数字
							original = (result.get("INTEGRATEDSCORE2") != null ?  ExNumberUtils.toDouble(result.get("INTEGRATEDSCORE2").toString()): 0D);
							handledRs += convertScoreStr(courseScoreType,original.toString());
						}
					}
				}
			}
			rs.put("isNormal", isNormal);
			rs.put("original", original);
			rs.put("handledRs", handledRs);
			rs.put("examAbnormity", examAbnormity);
				
		} catch (Exception e) {
			logger.error("查询学生成绩出错",e);
		}
		return rs;
	}

	/**
	 * 获取最后的补考成绩
	 * @param condition
	 * @return
	 */
	@Override
	public Map<String,Object> getMaxMakeupExamResult(Map<String, Object> condition) {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		Double max = 0D;
		boolean isMakeup = false;
		try {
			sql.append("select f_decrypt_score(rs.integratedscore, stu.resourceid) integratedscore,sub.examtype,rs.courseScoreType,sub.starttime ");
			sql.append("from EDU_TEACH_EXAMRESULTS rs join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
			sql.append("join edu_roll_studentinfo stu on rs.studentid=stu.resourceid ");
			sql.append("where rs.isdeleted=0 and sub.isdeleted=0 and stu.isdeleted=0 and sub.examtype<>'N' and rs.checkstatus='4' and rs.integratedscore is not null ");
			sql.append("and (select er.EXAMABNORMITY from EDU_TEACH_EXAMRESULTS er join EDU_TEACH_EXAMSUB es ");
			sql.append("on er.examsubid=es.resourceid and es.isdeleted=0 where er.isdeleted=0 and es.examtype='N' and er.checkstatus='4' ");
			sql.append("and er.studentid=:studentid and er.courseid=:courseid)!=5 ");
			
			if (condition.containsKey("studentid")) {
				sql.append(" and rs.studentid=:studentid ");
			}
			if (condition.containsKey("courseid")) {
				sql.append(" and rs.courseid=:courseid ");
			}
			sql.append(" order by sub.starttime desc");
			List<Map<String, Object>> results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
			if (results != null && results.size() > 0) {
				isMakeup = true ;
				max = Double.valueOf(results.get(0).get("INTEGRATEDSCORE") != null ? convertScoreStr(results.get(0).get("COURSESCORETYPE").toString()
						,results.get(0).get("INTEGRATEDSCORE").toString()) : "0");
			}
		
			returnMap.put("max", max);
			returnMap.put("isMakeup", isMakeup);
		} catch (Exception e) {
			logger.error("查询学生最后补考成绩出错",e);
		}
		return returnMap;
	}

	/**
	 * 根据条件获取未录入成绩（已开课并且设置了登分老师）的课程
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findNoExamResultCourse(Map<String, Object> condition) throws Exception {
		StringBuffer sql = new StringBuffer("");
		sql.append("select so.studyno,esy.batchname,c.coursename,ctr.teachername,css.term,pc.examclasstype,rs.checkstatus ");
		sql.append("from edu_roll_studentinfo so ");
		
		sql.append("inner join edu_teach_guiplan gp on so.gradeid=gp.gradeid and so.teachplanid=gp.planid and gp.isdeleted=0 "); 
		sql.append("inner join edu_teach_plancourse pc on pc.planid=gp.planid and pc.isdeleted=0 ");
		sql.append("inner join edu_teach_coursestatus css on css.isdeleted=0 and css.guiplanid=gp.resourceid and css.plancourseid=pc.resourceid and css.schoolids=so.branchschoolid ");
		if(!condition.containsKey("allCourse")){//增加还没有开课,但是是教学计划里面的课程,用于毕业审核,所有的课程不仅仅已开课的要判断的条件
			sql.append(" and css.isopen='Y'");
		}	
		sql.append("inner join edu_base_course c on pc.courseid=c.resourceid and c.isdeleted=0 ");
		sql.append("inner join edu_teach_courseteachercl ctr on ctr.courseid=pc.courseid and ctr.classesid=so.classesid and ctr.coursestatusid=css.resourceid and ctr.isdeleted=0 ");
		sql.append("inner join (select es.resourceid,y.firstyear,es.term,es.batchname from edu_teach_examsub es,edu_base_year y ");
		sql.append("where es.yearid=y.resourceid and es.isdeleted=0 and y.isdeleted=0 and es.examtype='N') esy on substr(css.term,0,4)=esy.firstyear and substr(css.term,7,7)=esy.term ");
		sql.append("left join edu_teach_examresults rs on rs.studentid = so.resourceid and rs.majorcourseid=pc.resourceid and rs.isdeleted=0 and esy.resourceid=rs.examsubid ");
		sql.append("where so.isdeleted=0 "); 
		sql.append("and not exists(select er.resourceid from edu_teach_examresults er "); 
//		sql.append("where er.studentid=so.resourceid and er.majorcourseid=pc.resourceid and er.isdeleted=0 and esy.resourceid=er.examsubid ");
		sql.append(" where er.studentid=so.resourceid  and c.resourceid=er.courseid   and er.isdeleted = 0 "); //放宽查询条件，对应课程有成绩即可，用于做过学籍异动的学生
		if(condition.containsKey("allCourse")){//用于毕业审核,所有的课程都要已发布
			sql.append(" and er.checkstatus = '4'");
		}
		sql.append(") ");
		if (condition.containsKey("courseNature")) {//课程类别
			sql.append(" and pc.courseNature =:courseNature");
		}
		if (condition.containsKey("courseNature_not")) {//课程类别
			sql.append(" and pc.courseNature !=:courseNature_not");
		}
		sql.append(" and not exists(select ne.resourceid from edu_teach_noexam ne where ne.studentid=so.resourceid and ne.courseid=pc.courseid and ne.isdeleted=0) ");
		
		if(condition.containsKey("studentNo")){
			sql.append(" and so.studyno=:studentNo ");
		}
		if(condition.containsKey("studentId")){
			sql.append(" and so.resourceid=:studentId ");
		}
		
		sql.append(" group by so.studyno,esy.batchname,c.coursename,ctr.teachername,css.term,pc.examclasstype,rs.checkstatus ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
	}

	@Override
	public Map<String, Map<String, Object>> getElectiveExamCountMapByStuList(List<StudentInfo> studentList) throws Exception {
		StringBuilder builder = new StringBuilder(studentList.size()*36);
		for (StudentInfo stu : studentList) {
			builder.append(",'"+stu.getResourceid()+"'");
		}
		String studentids = "''";
		if (builder.length() > 0) {
			studentids = builder.substring(1);
		}

		StringBuilder sqlBuilder = new StringBuilder(builder.length()+500);
		sqlBuilder.append(" select si.resourceid key,nvl(exam.examcount,0) examcount");
		sqlBuilder.append(" from edu_roll_studentinfo si");
		sqlBuilder.append(" left join (select eer.studentid,count(*) examcount from  edu_teach_electiveexamresults eer where eer.isdeleted=0 group by eer.studentid)exam on exam.studentid=si.resourceid");
		sqlBuilder.append(" where si.isdeleted=0 and si.resourceid in("+studentids+")");
		List<Map<String, Object>> mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlBuilder.toString(), new HashMap<String, Object>());
		return ExBeanUtils.convertMapsToMap(mapList);
	}
}