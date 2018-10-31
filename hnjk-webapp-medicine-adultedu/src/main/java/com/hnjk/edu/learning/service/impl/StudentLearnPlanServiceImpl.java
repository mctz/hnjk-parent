package com.hnjk.edu.learning.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hnjk.platform.system.model.Dictionary;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.SetFirstTermCourseForStuRegException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.TextBook;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.learning.vo.LearningPlanVo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.CourseOrder;
import com.hnjk.edu.teaching.model.CourseOrderStat;
import com.hnjk.edu.teaching.model.ElectiveExamResults;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.model.ReOrderSetting;
import com.hnjk.edu.teaching.model.TeachTask;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.service.ICourseOrderService;
import com.hnjk.edu.teaching.service.ICourseOrderStatService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IElectiveExamResultsService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSettingDetailsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.edu.teaching.service.IOrderCourseSettingService;
import com.hnjk.edu.teaching.service.IReOrderSettingService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
import com.hnjk.edu.teaching.service.impl.UsualResultsServiceImpl;
import com.hnjk.edu.teaching.util.StudentCourseOrderUtil;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
/**
 * 
 * <code>学员学习计划StudentLearnPlanServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 下午03:31:16
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentLearnPlanService")
public class StudentLearnPlanServiceImpl extends 
			 BaseServiceImpl<StudentLearnPlan> implements IStudentLearnPlanService{
		
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;//注入学籍服务
	
	@Autowired
	@Qualifier("courseorderstatservice")
	private ICourseOrderStatService courseorderstatservice;//注入学生预约情况统计服务
	
	@Autowired
	@Qualifier("courseOrderService")
	private ICourseOrderService courseOrderService;//注入学生预约课程服务
	
	@Autowired
	@Qualifier("orderCourseSettingService")
	private IOrderCourseSettingService orderCourseSettingService;//注入年度预约管理服务

	@Autowired
	@Qualifier("examSettingDetailsService")
	private IExamSettingDetailsService examSettingDetailsService;//注入考试计划设置明细服务

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;//注入考试/毕业论文批次预约服务 
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;//注入学生预约考试服务

	@Autowired
	@Qualifier("electiveExamResultsService")
	private IElectiveExamResultsService electiveExamResultsService;//注入选修课成绩服务
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;//在线学习模块公共服务
//	
//	@Autowired
//	@Qualifier("stateExamCourseService")
//	private IStateExamCourseService stateExamCourseService;//统考课程对应表服务
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService; //年级服务
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;//学生成绩服务
	
	
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;//毕业生论文预约服务
	
	@Autowired
	@Qualifier("reOrderSettingService")
	private IReOrderSettingService reOrderSettingService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//注入JDBC DAO
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService teachingPlanCourseTimetableService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	/**
	 * 根据学籍ID获取学员的学习计划
	 */
	@Override
	@Transactional(readOnly=true)
	public List<StudentLearnPlan> getStudentLearnPlanList(String studentId,String teachingPlanId)throws ServiceException {
		String hql = "from StudentLearnPlan plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.teachingPlan.resourceid=?";
		return (List<StudentLearnPlan>) exGeneralHibernateDao.findByHql(hql, new Object[]{0,studentId,teachingPlanId});
	}
	
	/**
	 * teachingPlanId为空时，查询出该学生的所有学习计划课程
	 */
	@Override
	@Transactional(readOnly=true)
	public List<StudentLearnPlan> getStudentLearnPlanListBySql(String studentId,String teachingPlanId)throws Exception{
		StringBuffer sqlBf = new StringBuffer();
		Map<String, Object> paramter = new HashMap<String, Object>();
		paramter.put("isdeleted", 0);
		paramter.put("studentid", studentId);
				
		
		sqlBf.append("select t.studentid,t.status,t.term,t.yearid,t.plansourceid, t.planoutcourseid,bc.coursename,bc.examtype,bc.planoutcredithour,");
		sqlBf.append(" (case when c.courseid is not null then c.courseid else bc.resourceid end)  courseid, r.integratedscore,r.writtenscore,r.usuallyscore,r.examabnormity,r.checkstatus,r.auditdate,r.coursescoretype,e.examstarttime,e.examendtime ");
		sqlBf.append(" from edu_learn_stuplan t ");
		sqlBf.append(" left join  edu_teach_examresults r on r.resourceid=t.examresultsid and r.isdeleted = :isdeleted ");
		sqlBf.append(" left join edu_teach_plancourse c on t.plansourceid=c.resourceid ");
		sqlBf.append(" left join edu_teach_examinfo e on t.examinfoid=e.resourceid ");
		sqlBf.append(" left join edu_base_course bc on (bc.resourceid=c.courseid or bc.resourceid=t.plansourceid or t.planoutcourseid=bc.resourceid) ");		 
		sqlBf.append(" where t.isdeleted = :isdeleted and t.studentid = :studentid ");
		if(ExStringUtils.isNotBlank(teachingPlanId)){
			paramter.put("planid", teachingPlanId);
			sqlBf.append(" and c.planid=:planid ");
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sqlBf.toString(), StudentLearnPlan.class, paramter);
	}
	
	/**
	 * 根据学籍ID获取学员的学习计划(计划外课程)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<StudentLearnPlan> getStudentPlanOutLearnPlanList(String studentId)throws ServiceException {
		String hql = "from StudentLearnPlan plan where plan.isDeleted=0 and plan.studentInfo.resourceid =? and plan.planoutCourse  is not null  ";
		return (List<StudentLearnPlan>) exGeneralHibernateDao.findByHql(hql, new String[]{studentId});
	}
	/**
	 * 免修免考成绩转换
	 * @param score
	 * @return
	 */
	private String noExamScoreChange(String score){
		String scoreStr = "";
		if ("0".equals(score)) {
			scoreStr          = "通过";
		}else if("1".equals(score)) {
			scoreStr          = "免考";
		}else if("2".equals(score)) {
			scoreStr          = "免修";
		}else if("3".equals(score)) {
			scoreStr          = "代修";
		}else {
			scoreStr = score;
		}
		return scoreStr;
	}
	/**
	 * 把学生的教学计划及学习计划转换为TeachingPlanVo对象集合
	 * @throws Exception 
	 */	
	@Override
	@Transactional(readOnly=true)
	@SuppressWarnings("unchecked")
	public Map<String,Object>  changeTeachingPlanToTeachingPlanVo(StudentInfo stu,List<StudentLearnPlan> learnPlanList, TeachingPlan teachingPlan) throws ServiceException {
		
		Map<String,Object>  voMap             = new HashMap<String, Object>();
		List<LearningPlanVo> list     		  = new ArrayList<LearningPlanVo>();			
	
		if(null!=teachingPlan && null!=teachingPlan.getTeachingPlanCourses()){
			
			/*~~~~~~~~~~~~~~~~~~1.准备数据~~~~~~~~~~~~~~~~~~~~~*/
			
//			Grade currentGrade      	  	  = gradeService.findUniqueByProperty("isDefaultGrade", "Y");//当前年级			
			Grade currentGrade      	  	  = gradeService.getDefaultGrade();//当前年级			
			String allowTerm       	   		  = StudentCourseOrderUtil.getAllowOrderCourseTerm(currentGrade, stu.getGrade());	//允许预约的学期
			
			int i             				  = 0;
			int compulsoryed  				  = 0;
			Double totalScore 				  = 0.0; 
			Double	compulsoryedScore		  = 0.0;
			
			//FIXED hzg 使用SQL方式查询出教学计划课程，包含课程基本信息，避免HIBERNATE LAZY查询
			//Set<TeachingPlanCourse> planSet   = teachingPlan.getTeachingPlanCourses();//教学计划课程
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("planid", teachingPlan.getResourceid());
			parameters.put("brschoolid", stu.getBranchSchool().getResourceid());
			parameters.put("gradeid", stu.getGrade().getResourceid());
			parameters.put("studentid", stu.getResourceid());
			
			List<TeachingPlanCourse> planCourseList = new ArrayList<TeachingPlanCourse>();
			
			try {
				StringBuffer sql = new StringBuffer();
				//学期调整为实际开课学期
				sql.append("select t.resourceid,t.coursetype,to_char((to_number(substr(cs.term,0,4))-y.firstyear)*2+to_number(substr(cs.term,6,7))) term,cs.term openTerm,t.credithour,cs.teachtype,t.courseid,c.coursename,c.examtype,c.isuniteexam,c.isdegreeunitexam,t.stydyHour,t.faceStudyHour,t.examClassType,t.isMainCourse,t.isDegreeCourse,t.scoreStyle courseScoreType,t.memo ");
				sql.append(" from edu_teach_plancourse t,edu_base_course c,EDU_TEACH_COURSESTATUS cs,edu_teach_guiplan gp ");
				sql.append(" join edu_base_grade g on g.resourceid=gp.gradeid join edu_base_year y on y.resourceid=g.yearid and y.isdeleted=0");
				sql.append(" where cs.plancourseid=t.resourceid and t.courseid=c.resourceid and t.planid=:planid and t.isdeleted=0 and cs.isdeleted=0 and cs.schoolids=:brschoolid and cs.guiplanid=gp.resourceid and gp.gradeid=:gradeid ");
				//联合查询选修课成绩
				sql.append(" union select * from(select '' resourceid,c.coursetype,to_char((y.firstyear-y1.firstyear)*2+to_number(es.term)) term,es.term openTerm,c.planoutcredithour,'facestudy',c.resourceid courseid,c.coursename,c.examtype,c.isuniteexam,c.isdegreeunitexam,c.planoutstudyhour stydyHour,c.planoutstudyhour faceStudyHour,'1' examClassType,'N' isMainCourse,'N' isDegreeCourse,");
				sql.append(" eer.courseScoreType,eer.integratedscore from edu_teach_electiveexamresults eer ");
				sql.append(" join edu_base_course c on c.resourceid=eer.courseid ");
				sql.append(" join edu_base_grade g on g.isdeleted=0 ");
				sql.append(" join edu_base_year y1 on y1.resourceid=g.yearid ");
				sql.append(" left join edu_teach_examsub es on es.resourceid=eer.examsubid left join edu_base_year y on y.resourceid=es.yearid ");
				sql.append(" join edu_roll_studentinfo si on si.resourceid=eer.STUDENTID and si.gradeid=g.resourceid");
				sql.append(" where  eer.isdeleted = 0 and si.resourceid=:studentid order by to_number(eer.integratedscore) desc)");
				sql.append(" order by term ASC,coursename ");
				planCourseList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(),TeachingPlanCourse.class, parameters);
			} catch (Exception e) {
				//忽略
			}						
			
			//组装学习计划数据，分为计划内和计划外
			List<StudentLearnPlan> _planInlearnPlanList = new ArrayList<StudentLearnPlan>();
			List<StudentLearnPlan> _planOutlearnPlanList = new ArrayList<StudentLearnPlan>();
			if(null !=learnPlanList && !learnPlanList.isEmpty()){
				for(StudentLearnPlan plan : learnPlanList){
					if(ExStringUtils.isNotBlank(plan.getPlanOutcourseId())){
						_planOutlearnPlanList.add(plan);
					}else{
						_planInlearnPlanList.add(plan);
					}
				}
				learnPlanList.clear();
			}
			
			
			Map<String,Object>  statCourseRs  = new HashMap<String, Object>(); //统考课程成绩
			Map<String,Object> noExamCourseRs = new HashMap<String, Object>(); //免修免考成绩 
			voMap.put("studentId", stu.getResourceid());
			
			//学生的所有有效成绩列表(包括选修课成绩)
			List<Map<String, Object>>  results= studentExamResultsService.studentPublishedExamResultsList(voMap);
			//获取同一科目最大的成绩
			results							  = studentExamResultsService.sortExamResultList(results);
			//选修课成绩
			//List<ElectiveExamResults> electiveExamList = electiveExamResultsService.studentExamResultsList(stu);
			for (Map<String, Object> map :results) {
				String courseId               = (String)map.get("COURSEID");
				voMap.put(courseId, map);
				
			}
//			List<Map<String,Object>> scl      = learningJDBCService.findStatCouseExamResults(stu.getResourceid());
			List<Map<String,Object>> ncl      = learningJDBCService.findNoExamCourseResults(stu.getResourceid());
			
//			if (null!=scl && !scl.isEmpty()) {
//				for (Map<String,Object> rs1:scl) {//统考课程成绩
//					statCourseRs.put(rs1.get("PLANCOURSEID").toString(),rs1);
//				}
//			}
			if (null!=ncl && !ncl.isEmpty()) {
				for (Map<String,Object> rs2:ncl) {//免修免考成绩 
					noExamCourseRs.put(rs2.get("COURSEID").toString(), rs2);
				}
			}
			
//			ExamSub thesisSub			 = examSubService.get("4aa6636631215ecd0131274f065201a0");
			
			/*~~~~~~~~~~~~~~~~~2. 业务，遍历教学计划，查询学生的免修免考、计划内、计划外学习计划信息~~~~~~~~~~~~~~*/
			
			Iterator<TeachingPlanCourse> it   = planCourseList.iterator();			
			while(it.hasNext()){//遍历教学计划课程
				i++;
				TeachingPlanCourse planCourse =  it.next();
				//Course course                 = planCourse.getCourse();				
				String planCourseExamType     = null==planCourse.getExamType()?"":planCourse.getExamType().toString();
				String teachingPlanCourseId   = ExStringUtils.defaultIfEmpty(planCourse.getResourceid(), "") ;
				String planCourseType         = ExStringUtils.defaultIfEmpty(planCourse.getCourseType(),"");				
				String planCourseName         = ExStringUtils.defaultIfEmpty(planCourse.getCourseName(),"");
				Double planCourseCreditHour   = null==planCourse.getCreditHour()?-1.0:planCourse.getCreditHour();
				String courseType             = JstlCustomFunction.dictionaryCode2Value("CodeCourseType",planCourseType);
				//这边获取的都是原计划的,因为开课那边没有改动
				String planCourseTerm 		  = ExStringUtils.defaultIfEmpty(planCourse.getTerm(),"");
				String courseTeachType = planCourse.getTeachType();
				//修复后就不用查调课了
				/*Map<String,Object> con = new HashMap<String, Object>();
				con.put("plancourseid", planCourse.getResourceid());
				con.put("schoolIds", stu.getBranchSchool().getResourceid());
				con.put("gradeid", stu.getGrade().getResourceid());
				List<TeachingPlanCourseStatus> temp = teachingPlanCourseStatusService.findByHql("from "+TeachingPlanCourseStatus.class.getSimpleName()+" t where t.teachingGuidePlan.grade.resourceid=:gradeid and t.teachingPlanCourse.resourceid=:plancourseid and t.isDeleted=0 and t.openStatus='Y' and t.isOpen='Y'and t.schoolIds=:schoolIds" , con);
				if(temp!=null && temp.size()>0 || ExStringUtils.isBlank(planCourse.getResourceid())){
					if(temp!=null && temp.size()>0){
						courseTeachType = temp.get(0).getTeachType();
					}
				}*/
				/*if(true){//修复调整开课没有同步,教学计划课程,导致这里查询不出来的数据
					List<TeachingPlanCourseStatus> tmp = teachingPlanCourseStatusService.findByHql("from "+TeachingPlanCourseStatus.class.getSimpleName()+" t where t.checkStatus='updateY' and t.isOpen='Y' and t.openStatus='Y'");
					List<String> strs = new ArrayList<String>();
					for(TeachingPlanCourseStatus tpcs : tmp){
						String yearTerm = tpcs.getTerm();
						String year = yearTerm.substring(0, 4);
						String term = yearTerm.substring(6, 7);
						long  tem = Long.parseLong(year) - tpcs.getTeachingGuidePlan().getGrade().getYearInfo().getFirstYear();
						strs.add("update EDU_TEACH_PLANCOURSE t set t.term='"+String.valueOf(tem * 2 + Long.parseLong(term))+"' where t.resourceid='"+tpcs.getTeachingPlanCourse().getResourceid()+"';") ;
					}
					System.out.println(strs);
				}*/
				
				String courseTerm             = JstlCustomFunction.dictionaryCode2Value("CodeTermType",planCourseTerm);
				Long studyHour                = planCourse.getStydyHour();
				Long faceStudyHour            = planCourse.getFaceStudyHour();
				String examClassType          = JstlCustomFunction.dictionaryCode2Value("CodeExamClassType",planCourse.getExamClassType());
				String isMainCourse           = JstlCustomFunction.dictionaryCode2Value("yesOrNo",planCourse.getIsMainCourse());
				String isDegreeCourse         = JstlCustomFunction.dictionaryCode2Value("yesOrNo",planCourse.getIsDegreeCourse());
				
				LearningPlanVo vo             = new  LearningPlanVo();
				vo.setTeachingPlan_Course_teachType_code(courseTeachType);
				vo.setTeachingPlan_Course_teachType_Name(JstlCustomFunction.dictionaryCode2Value("CodeCourseTeachType",courseTeachType));
				vo.setTeachingPlan_Course_examType(JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",planCourseExamType));
				vo.setTeachingPlan_Course_examType_code(planCourseExamType);
				vo.setTeachingPlan_Course_type_code(planCourseType);
				vo.setCourse_Id(planCourse.getCourseId());
				vo.setTeachingPlan_Id(teachingPlan.getResourceid());
				vo.setTeachingPlan_Course_num(i);
				vo.setTeachingPlan_Course_Id(teachingPlanCourseId);
				vo.setTeachingPlan_Course_name(planCourseName);
				vo.setTeachingPlan_Course_type(courseType);
				vo.setTeachingPlan_Course_term(courseTerm);
				vo.setTeachingPlan_Course_creditHour(planCourseCreditHour);
				vo.setTeachingPlan_Course_term_code(planCourseTerm);
				//2013-04-13新增
				vo.setTeachingPlan_Course_studyHour(studyHour);
				vo.setTeachingPlan_Course_faceStudyHour(faceStudyHour);
				vo.setTeachingPlan_Course_examClassType(examClassType);
				vo.setTeachingPlan_Course_isMainCourse(isMainCourse);
				vo.setTeachingPlan_Course_isDegreeCourse(isDegreeCourse);
				vo.setTeachingPlan_Course_openTerm_code(planCourse.getOpenTerm());
				Course course = courseService.get(planCourse.getCourseId());
				if(null!= course){
					vo.setIsHasResource(course.getHasResource());
					
					//20171107 增加教材列表的显示信息
					Set<TextBook> textBooks = new HashSet<TextBook>();
					textBooks = course.getTextBooks();
					vo.setTextBooks(textBooks);					
				}				
				//如果学生统考课程成绩不为空则显示统考课程成绩
				if (null!=statCourseRs.get(planCourse.getResourceid())) {
					
					Map<String,Object> rs = (Map<String, Object>) statCourseRs.get(planCourse.getResourceid());
					
					String scoreStr       = rs.get("SCORETYPE").toString();
					
					scoreStr              = noExamScoreChange(scoreStr);
					
					if ("必修课".equals(courseType)) {
						compulsoryed ++;
						compulsoryedScore +=planCourse.getCreditHour();
					}
	
					totalScore += planCourse.getCreditHour();
					vo.setTeachingPlan_Course_learnStatusInt(3);
					vo.setTeachingPlan_Course_bookingStatus("已考完");
					vo.setTeachingPlan_Course_learnStatus("完成学习");
					vo.setTeachingPlan_Course_scoreStr(scoreStr);
					vo.setSpecialCourseFlag("Y");//统考课程不需要重修
					try {
						vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(ExDateUtils.convertToDate(rs.get("PASSTIME").toString()), "yyyy-MM-dd"));
						vo.setTeachingPlan_Course_examEndTime("");
					} catch (Exception e) {
						//logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合-转换时间出错:{}",e.fillInStackTrace());
					}
					
				//如果学生免修免考成绩不为空则显示免修免考成绩
				}else if(null!=noExamCourseRs.get(planCourse.getCourseId())) {
					
					Map<String,Object> rs = (Map<String, Object>) noExamCourseRs.get(planCourse.getCourseId());
					
					String scoreStr       = rs.get("UNSCORE").toString();
					
					scoreStr              = noExamScoreChange(scoreStr);
					
					if ("必修课".equals(courseType)) {
						compulsoryed ++;
						compulsoryedScore +=planCourse.getCreditHour();
					}
	
					totalScore += planCourse.getCreditHour();
					vo.setTeachingPlan_Course_learnStatusInt(3);
					vo.setTeachingPlan_Course_bookingStatus("已考完");
					vo.setTeachingPlan_Course_learnStatus("完成学习");	
					vo.setTeachingPlan_Course_scoreStr(scoreStr);
					vo.setSpecialCourseFlag("Y");//免修免考不需要重修
					
					try {
						vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(ExDateUtils.convertToDate(rs.get("CHECKTIME").toString()), "yyyy-MM-dd"));
						vo.setTeachingPlan_Course_examEndTime("");
					} catch (Exception e) {
						//logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合-转换时间出错:{}",e.fillInStackTrace());
					}
	
				//没有免修、免考成绩
				}else{
					//选修课成绩
					if(ExStringUtils.isBlank(teachingPlanCourseId)){
						totalScore += planCourse.getCreditHour();
						vo.setTeachingPlan_Course_scoreStr(studentExamResultsService.convertScoreStr(planCourse.getCourseScoreType(), planCourse.getMemo()));
						vo.setTeachingPlan_Course_learnStatusInt(3);	
						list.add(vo);
						continue;
					}
					if (null!=_planInlearnPlanList&&!_planInlearnPlanList.isEmpty()) {
						//FIXED BY HZG 使用SQL方式，避免hibernate 多次查询
						for(StudentLearnPlan plan:_planInlearnPlanList){
							//ExamResults learnPlanExamResult = plan.getExamResults();
							String  leanPlanCourseId        = ExStringUtils.defaultIfEmpty(plan.getPlanSourceId(),"");//对应的教学计划课程ID
							if (!"".equals(teachingPlanCourseId)&&leanPlanCourseId.equals(teachingPlanCourseId)) {
								String  leanPlanIntegratedScoreStr = ExStringUtils.isBlank(plan.getIntegratedScore())?"":plan.getIntegratedScore();    //综合成绩
								String  leanPlanWrittenScoreStr = ExStringUtils.isBlank(plan.getWrittenScore())?"":plan.getWrittenScore();//卷面成绩
								String  leanPlanUsuallyScoreStr = ExStringUtils.isBlank(plan.getUsuallyScore())?"":plan.getUsuallyScore();//平时成绩
								String  leanPlanExamAbnormityStr= ExStringUtils.isBlank(plan.getExamAbnormity())?"0":plan.getExamAbnormity();         //成绩异常代码
								String  examResultsStatus       = ExStringUtils.isBlank(plan.getCheckStatus())?"-1":plan.getCheckStatus();          //成绩审核状态
								String  courseScoreType         = ExStringUtils.isEmpty(plan.getCourseScoreType())?"11":plan.getCourseScoreType();      //成绩类型 百分制 二分制 五分制
								Integer leanPlanStatus          = null==plan.getStatus()?-1:plan.getStatus();                                   //学习计划状态

							    //如果当前学习计划的状态为已发布，则获取当前课程有效的最高成绩
								if (3==leanPlanStatus) {
									Map<String,Object> courseRs = (Map<String,Object>)voMap.get(plan.getCourseId());
									if (null!=courseRs) {
										leanPlanIntegratedScoreStr  = ExStringUtils.isBlank((String)courseRs.get("INTEGRATEDSCORE"))?"":(String)courseRs.get("INTEGRATEDSCORE");
										leanPlanWrittenScoreStr = ExStringUtils.isBlank((String)courseRs.get("WRITTENSCORE"))?"":(String)courseRs.get("WRITTENSCORE");
										leanPlanUsuallyScoreStr =  ExStringUtils.isBlank((String)courseRs.get("USUALLYSCORE"))?"":(String)courseRs.get("USUALLYSCORE");									
										courseScoreType  = ExStringUtils.isBlank((String)courseRs.get("COURSESCORETYPE"))?"11":(String)courseRs.get("COURSESCORETYPE");									 
										leanPlanExamAbnormityStr= "0";
									}
								}
								String scoreStyle = planCourse.getCourseScoreType();
								Double leanPlanIntegratedScoreDou  = Double.parseDouble(studentExamResultsService.convertScore(courseScoreType, leanPlanIntegratedScoreStr));
								//Double leanPlanWrittenScoreDou = Double.parseDouble(studentExamResultsService.convertScore(courseScoreType, leanPlanWrittenScoreStr));
								Double leanPlanExamAbnormityDou = Double.parseDouble(leanPlanExamAbnormityStr);
								//默认成绩不进行百分制转换
								/*if (Double.parseDouble(leanPlanIntegratedScoreStr)>=0) {
									leanPlanIntegratedScoreDou         = Double.parseDouble(studentExamResultsService.convertScore(courseScoreType, leanPlanIntegratedScoreStr));
								}*/
								
								/*if(Double.parseDouble(leanPlanWrittenScoreStr)>=0){
									leanPlanWrittenScoreDou = Double.parseDouble(studentExamResultsService.convertScore(courseScoreType, leanPlanWrittenScoreStr));
								}*/
								//成绩，显示在页面上的
								//String 	scorStr  = String.valueOf(leanPlanIntegratedScoreDou);//综合成绩
								//String writtenScoreStr = String.valueOf(leanPlanWrittenScoreDou);//卷面成绩
								
								Date passDate  = plan.getAuditDate();
								//if (null!=learnPlanExamResult) {
								//	passDate                    = learnPlanExamResult.getAuditDate();
								//}
								
								//如果不是百分制的成绩则转换成对应的成绩级别字符
								/*if (!Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(courseScoreType)) {
									scorStr   		= studentExamResultsService.convertScoreStr(courseScoreType, leanPlanIntegratedScoreStr);
									writtenScoreStr   = studentExamResultsService.convertScoreStr(courseScoreType, leanPlanWrittenScoreStr);
								}*/
								//未预约学习
								if (leanPlanStatus==1) {

									if ("thesis".equals(planCourseType)) {
										vo.setThesisOrderTerm(plan.getTerm());
										vo.setThesisOrderYearInfo(plan.getYearId());
										vo.setTeachingPlan_Course_learnStatusInt(1);
										vo.setTeachingPlan_Course_bookingStatus("");
										vo.setTeachingPlan_Course_learnStatus("已预约");
									}else {
										vo.setTeachingPlan_Course_learnStatusInt(2);
										//统考课程、考试形式为统考的课程不需要预约考试
										if ((null!=planCourse.getIsUniteExam()&&Constants.BOOLEAN_YES.equals(planCourse.getIsUniteExam()))||
											//(null!=course.getIsDegreeUnitExam()&& Constants.BOOLEAN_YES.equals(course.getIsDegreeUnitExam()))||
											(null!=planCourse.getExamType()&&5==planCourse.getExamType())) {
											
											vo.setTeachingPlan_Course_bookingStatus("");
										}else {
											vo.setTeachingPlan_Course_bookingStatus("预约考试");
										}
										
										vo.setTeachingPlan_Course_learnStatus("已预约学习");
									}
									
									break;
									
								//预约考试	
								}else if (leanPlanStatus==2&&ExStringUtils.isEmpty(leanPlanIntegratedScoreStr)) {
									
									vo.setTeachingPlan_Course_learnStatusInt(2);
									vo.setTeachingPlan_Course_bookingStatus("准备考试");
									vo.setTeachingPlan_Course_learnStatus("已预约考试");	
									try {
										//if (null!=plan.getExamInfo()) {
											vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(plan.getExamStartTime(),"yyyy年MM月dd日 HH:mm:ss"));
											if ("3".equals(planCourseExamType) || "6".equals(planCourseExamType)) {
												vo.setTeachingPlan_Course_examEndTime(ExDateUtils.formatDateStr(plan.getExamEndTime(),"yyyy年MM月dd日 HH:mm:ss"));
											}else {
												vo.setTeachingPlan_Course_examEndTime(ExDateUtils.formatDateStr(plan.getExamEndTime(),"HH:mm:ss"));
											}
											
										//}
									}catch (Exception e) {
										//logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合操作出错：{}",e.fillInStackTrace());
									}	
									
									break;
									
								//已考完成绩录入中
								}else if(leanPlanStatus==2&&ExStringUtils.isNotEmpty(leanPlanIntegratedScoreStr)&& !"4".equals(examResultsStatus)){
									
									vo.setTeachingPlan_Course_learnStatusInt(3);
									vo.setTeachingPlan_Course_bookingStatus("成绩录入中");
									vo.setTeachingPlan_Course_learnStatus("已考完");	
									
									break;
									
								//此条件为防止旧数据中成绩已发布，但未更新对应考试批次的学生的学生计划	
								}else if(leanPlanStatus==2&&ExStringUtils.isEmpty(leanPlanIntegratedScoreStr)&& "4".equals(examResultsStatus)&&leanPlanIntegratedScoreDou>=60){
									
									if ("必修课".equals(courseType)) {
										compulsoryed ++;
										compulsoryedScore +=planCourse.getCreditHour();
									}
									totalScore += planCourse.getCreditHour();
									vo.setTeachingPlan_Course_learnStatusInt(3);
									vo.setTeachingPlan_Course_bookingStatus("已考完");
									vo.setTeachingPlan_Course_learnStatus("完成学习");

									if("30".equals(scoreStyle)){//等级成绩，分数转字符
										List<Dictionary> list_dict = CacheAppManager.getChildren("CodeScoreLevel");
										for (int temp=0;temp<list_dict.size();temp++) {
											Dictionary dictionary = list_dict.get(temp);
											int score = Integer.parseInt(dictionary.getDictValue());
											if(leanPlanIntegratedScoreDou>=score){
												vo.setTeachingPlan_Course_scoreStr(dictionary.getDictName());
												temp=list_dict.size();
											}
										}
									} else {
										vo.setTeachingPlan_Course_WrittenScore(leanPlanWrittenScoreStr);
										vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
										vo.setTeachingPlan_Course_scoreStr(leanPlanIntegratedScoreStr);
									}
									
									break;
									
								//此条件为防止旧数据中成绩已发布，但未更新对应考试批次的学生的学习计划	
								}else if(leanPlanStatus==2&&ExStringUtils.isEmpty(leanPlanIntegratedScoreStr)&& "4".equals(examResultsStatus)&&leanPlanIntegratedScoreDou<60){
									
									vo.setTeachingPlan_Course_learnStatusInt(2);
									
									//统考课程、考试形式为统考的课程不需要预约考试
									if ((null!=planCourse.getIsUniteExam()&&Constants.BOOLEAN_YES.equals(planCourse.getIsUniteExam()))||
										(null!=planCourse.getIsDegreeUnitExam()&& Constants.BOOLEAN_YES.equals(planCourse.getIsDegreeUnitExam()))||
										(null!=planCourse.getExamType()&&5==planCourse.getExamType())) {
										
										vo.setTeachingPlan_Course_bookingStatus("");
									}else {
										vo.setTeachingPlan_Course_bookingStatus("重新考试");
									}
									
									vo.setTeachingPlan_Course_learnStatus("成绩不合格");
									vo.setIsNeedReExamination("Y");
									
									//if (ExStringUtils.isNotEmpty(scorStr)) {
										vo.setTeachingPlan_Course_scoreStr(leanPlanIntegratedScoreStr);
										vo.setTeachingPlan_Course_WrittenScore(leanPlanWrittenScoreStr);
										vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
									///}else {
									//	vo.setTeachingPlan_Course_score(leanPlanIntegratedScoreDou);
									//}
									
									
									break;
								//已考完	 成绩正常并大于60
								}else if (leanPlanStatus==3&& "4".equals(examResultsStatus)&&leanPlanIntegratedScoreDou>=60 && leanPlanExamAbnormityDou==0 ) {
									
									if ("必修课".equals(courseType)) {
										compulsoryed ++;
										if (planCourse.getCreditHour() != null) {
											compulsoryedScore +=planCourse.getCreditHour();
										}
									}
									if (planCourse.getCreditHour() != null) {
										totalScore += planCourse.getCreditHour();
									}
									vo.setTeachingPlan_Course_learnStatusInt(3);																				
									
									if("thesis".equals(planCourseType)) {
										vo.setTeachingPlan_Course_bookingStatus("");
										vo.setTeachingPlan_Course_learnStatus("论文通过");
									}else {
										vo.setTeachingPlan_Course_bookingStatus("已考完");
										vo.setTeachingPlan_Course_learnStatus("完成学习");
									}

									if("30".equals(scoreStyle)){//等级成绩，分数转字符
										List<Dictionary> list_dict = CacheAppManager.getChildren("CodeScoreLevel");
										for (int temp=0;temp<list_dict.size();temp++) {
											Dictionary dictionary = list_dict.get(temp);
											int score = Integer.parseInt(dictionary.getDictValue());
											if(leanPlanIntegratedScoreDou>=score){
												vo.setTeachingPlan_Course_scoreStr(dictionary.getDictName());
												temp=list_dict.size();
											}
										}
									} else {
										vo.setTeachingPlan_Course_WrittenScore(leanPlanWrittenScoreStr);
										vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
										vo.setTeachingPlan_Course_scoreStr(leanPlanIntegratedScoreStr);
									}
									
									try {
										//if (null!=plan.getExamInfo()) {
											vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(plan.getExamStartTime(),"yyyy年MM月dd日 HH:mm:ss"));
											if ("3".equals(planCourseExamType) || "6".equals(planCourseExamType)) {
												vo.setTeachingPlan_Course_examEndTime(ExDateUtils.formatDateStr(plan.getExamEndTime(),"yyyy年MM月dd日 HH:mm:ss"));
											}else {
												vo.setTeachingPlan_Course_examEndTime(ExDateUtils.formatDateStr(plan.getExamEndTime(),"HH:mm:ss"));
											}
										//}
									}catch (Exception e) {
										//logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合操作出错：{}",e.fillInStackTrace());
									}	
									break;
									
								//已考完	 成绩正常并小于60	
								}else if (leanPlanStatus==3&& "4".equals(examResultsStatus)&&leanPlanIntegratedScoreDou<60 && leanPlanExamAbnormityDou==0) {
									
									vo.setTeachingPlan_Course_learnStatusInt(2);
									
									if("thesis".equals(planCourseType)) {
										vo.setTeachingPlan_Course_bookingStatus("重做论文");
										vo.setTeachingPlan_Course_learnStatus("论文未通过");
									}else {
										//统考课程、考试形式为统考的课程不需要预约考试
										
											if ((null!=planCourse.getIsUniteExam()&&Constants.BOOLEAN_YES.equals(planCourse.getIsUniteExam()))||
												(null!=planCourse.getIsDegreeUnitExam()&& Constants.BOOLEAN_YES.equals(planCourse.getIsDegreeUnitExam()))||
												(null!=planCourse.getExamType()&&5==planCourse.getExamType())) {
												
												vo.setTeachingPlan_Course_bookingStatus("");
											}else {
												vo.setTeachingPlan_Course_bookingStatus("重新考试");
											}
											
											vo.setTeachingPlan_Course_learnStatus("成绩不合格");
										
									}
									
									vo.setIsNeedReExamination("Y");
									//if (ExStringUtils.isNotEmpty(scorStr)) {
										vo.setTeachingPlan_Course_scoreStr(leanPlanIntegratedScoreStr);
										vo.setTeachingPlan_Course_WrittenScore(leanPlanWrittenScoreStr);
										vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
									//}else {
									//	vo.setTeachingPlan_Course_score(leanPlanIntegratedScoreDou>0?leanPlanIntegratedScoreDou:0);
									//}
									
									break;
								//已考完	 成绩异常
								}else if(leanPlanStatus==3 && leanPlanExamAbnormityDou>0) {
									
									vo.setTeachingPlan_Course_learnStatusInt(2);
									vo.setTeachingPlan_Course_bookingStatus("重新考试");
									String ExamAbnormityStr = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", String.valueOf(leanPlanExamAbnormityDou.intValue()));
									vo.setTeachingPlan_Course_learnStatus(ExamAbnormityStr);
									vo.setIsNeedReExamination("Y");
									break;
								//学习计划状态跟成绩状态数据不同步的状态
								}else if(leanPlanStatus==3&& !"4".equals(examResultsStatus)){
									vo.setTeachingPlan_Course_learnStatusInt(3);
									vo.setTeachingPlan_Course_bookingStatus("");
									vo.setTeachingPlan_Course_learnStatus("成绩审核中");
									break;
								}
								try {
									if (null!=passDate) {
										vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(passDate, "yyyy年MM月dd日 "));
										vo.setTeachingPlan_Course_examEndTime("");
									}
								} catch (Exception e) {
									logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合操作出错：{}",e.fillInStackTrace());
								}
							
							}else {
								continue;
							}
						}
						if ( null==vo.getTeachingPlan_Course_learnStatusInt() ||0==vo.getTeachingPlan_Course_learnStatusInt()) {
							if ("thesis".equals(planCourseType)) {
								vo.setTeachingPlan_Course_learnStatusInt(-1);
								vo.setTeachingPlan_Course_bookingStatus("预约毕业论文");
								vo.setTeachingPlan_Course_learnStatus("未预约");
							}else {
								vo.setTeachingPlan_Course_bookingStatus("预约学习");
								vo.setTeachingPlan_Course_learnStatus("未预约学习");
								vo.setTeachingPlan_Course_learnStatusInt(-1);
							}
						}
					}else {
						if ("thesis".equals(planCourseType)) {
							vo.setTeachingPlan_Course_learnStatusInt(-1);
							vo.setTeachingPlan_Course_bookingStatus("预约毕业论文");
							vo.setTeachingPlan_Course_learnStatus("未预约毕业论文");
						}else {
							vo.setTeachingPlan_Course_bookingStatus("预约学习");
							vo.setTeachingPlan_Course_learnStatus("未预约学习");
							vo.setTeachingPlan_Course_learnStatusInt(-1);
						}
						
					}
				}
				/**
				 * 注：2011.10.12由于毕业论文提出一定要在当前学期到达到教学计划中规定的论文学期才给预约，
				 * 但在此之前已有学生未达到论文学期预约了  2012（春）届毕业论文批次 ,客户提出2012（春）届批次已预约的学生需要显示，
				 * 特在此处加入过滤
				 */
				//FIXED hzg 异动到while循环外
				//ExamSub thesisSub			 = examSubService.get("4aa6636631215ecd0131274f065201a0");
				
				//教学计划课程的学期小于当前年级的学期不显示 
				//if (Integer.parseInt(planCourseTerm)>Integer.parseInt(allowTerm)&&!"thesis".equals(planCourseType)) {
				if (ExStringUtils.isNotBlank(planCourseTerm) && ExStringUtils.isNotBlank(allowTerm) && Integer.parseInt(planCourseTerm)>Integer.parseInt(allowTerm)) {	
					
					if("thesis".equals(planCourseType)){
					   /*
						if (!thesisSub.getYearInfo().getResourceid().equals(vo.getThesisOrderYearInfo())&&
					       !thesisSub.getTerm().equals(vo.getThesisOrderTerm())){
						
						    vo.setTeachingPlan_Course_bookingStatus("");
							vo.setTeachingPlan_Course_learnStatus("未预约");
							vo.setTeachingPlan_Course_learnStatusInt(-1);	
						}
						*/
					}else{
//						vo.setTeachingPlan_Course_scoreStr("");
//						vo.setTeachingPlan_Course_WrittenScore("");
//						vo.setTeachingPlan_Course_UsuallyScore("");
//						vo.setTeachingPlan_Course_bookingStatus("");
//						vo.setTeachingPlan_Course_examStartTime("");
//						vo.setTeachingPlan_Course_examEndTime("");
//						vo.setTeachingPlan_Course_learnStatus("未预约学习");
//						vo.setTeachingPlan_Course_learnStatusInt(-1);	
					}
					
				}
				list.add(vo);
			}
			
			//计划外课程的学习计划
			if(null != _planOutlearnPlanList && !_planOutlearnPlanList.isEmpty()) {
				changePlanOutCourseToTeachingPlanVo(voMap,stu,list.size(),_planOutlearnPlanList);
			}
			
			//设置页面中我的学习计划的顶部信息 学制：学制： 年级状态： 累计学分：....
			StudentExamResultsVo stuExamResults = null;
			try {
				//FIXED by hzg 利用现有查询结果集，避免再次查询
				stuExamResults   = studentExamResultsService.getStudentFinishedCreditHour(stu,results,null,null,planCourseList);
			} catch (Exception e) {
//				e.printStackTrace();
				logger.error("转换学生学习计划状态，获取学生已获取学分出错：{}"+e.fillInStackTrace());
			}
			
			LearningPlanVo vo = new LearningPlanVo();
			vo.setEduYear(teachingPlan.getEduYear());
			vo.setTeachingPlan_Name(teachingPlan.getMajor()+"-"+teachingPlan.getClassic());
			vo.setMinResult(teachingPlan.getMinResult());
			if (null!=stuExamResults&&null!=stuExamResults.getTotalCredit()&&null!=stuExamResults.getRequiredCredit()) {
				vo.setCompulsoryed(stuExamResults.getCompulsoryed());
				vo.setTotalScore(stuExamResults.getTotalCredit().doubleValue());
				vo.setCompulsoryedScore(stuExamResults.getRequiredCredit().doubleValue());
			}else {
				vo.setCompulsoryed(compulsoryed);
				vo.setTotalScore(totalScore);
				vo.setCompulsoryedScore(compulsoryedScore);
			}
			
			voMap.put("planTitle", vo);
			//是否替换成绩
			isDisplayScore(stu,list);
			voMap.put("planCourseList", list);
		}
		
		return voMap;
	}
	
	/**
	 * 是否替换成绩
	 * @param studentInfo
	 * @param results
	 * @throws ServiceException
	 */
	private void isDisplayScore(StudentInfo studentInfo,
			List<LearningPlanVo> results) throws ServiceException {
		String isDisplay =CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score").getParamValue();//全局参数：N 为不显示成绩，Y为显示成绩
//				String tips="";
		if(isDisplay.equalsIgnoreCase(Constants.BOOLEAN_NO)){
			//获取学生的缴费状态:  已缴费的则跳过，未缴费的则不显示成绩
			String hql4sp="from "+StudentPayment.class.getSimpleName()+" where studentInfo.resourceid =? and isDeleted =0 ";
			StudentPayment sp = studentPaymentService.findUnique(hql4sp, new Object[]{studentInfo.getResourceid()});
			if(null==sp){//没有缴费记录
//						tips="在系统中未找到你的缴费记录，按学校要求，未有缴费记录或未缴费，则不显示成绩。可以【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况";
				BLANKALLOrCurrentTerm(studentInfo,results,"");
			}else{
//						tips="你的学费状态为欠费（包括未缴费）。按学校要求，未有缴费记录或未缴费，则不显示成绩。可以【我的个人信息】【我的学籍信息】【缴费信息】中查看缴费情况";
				if(!"1".equals(sp.getChargeStatus())){//缴费状态,字典值,0-未缴费,1-已缴费,-1-欠费
					BLANKALLOrCurrentTerm(studentInfo,results,sp.getChargeStatus());
				}
			}
		}
	}
	private void BLANKALLOrCurrentTerm(StudentInfo stu,List<LearningPlanVo> results,String paymentStatus){
		String tips ="没有缴费记录";
		if(ExStringUtils.isNotBlank(paymentStatus)){
			tips=JstlCustomFunction.dictionaryCode2Value("CodeChargeStatus", paymentStatus);
		}		
		// Y/N ：Y表示全部学期的成绩都不显示		
		String BLANKAll =CacheAppManager.getSysConfigurationByCode("no_schooling_no_display_score_all_or_current").getParamValue();
		if(BLANKAll.equalsIgnoreCase(Constants.BOOLEAN_YES)){
			for (LearningPlanVo vo : results){
				vo.setTeachingPlan_Course_scoreStr("<font color='red'>"+tips+"</font>");
				vo.setTeachingPlan_Course_WrittenScore(tips);
				vo.setTeachingPlan_Course_UsuallyScore(tips);
			}
		}else{
			String currentTerm=CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
			String [] strs= currentTerm.split("\\.");
			String year =strs[0];
			String term = strs[1];
			int gradeYear=stu.getGrade().getYearInfo().getFirstYear().intValue();
			int intTerm =((Integer.parseInt(year)-gradeYear)*2+Integer.parseInt(term));
			String strTerm = String.valueOf(intTerm);
			for (LearningPlanVo vo : results){						
				if(vo.getTeachingPlan_Course_term_code().equals(strTerm)){
					vo.setTeachingPlan_Course_scoreStr("<font color='red'>"+tips+"</font>");
					vo.setTeachingPlan_Course_WrittenScore(tips);
					vo.setTeachingPlan_Course_UsuallyScore(tips);
				}				
			}
		}
	}
	/**
	 * 把学生的学习计划（计划外课程）转换为TeachingPlanVo对象集合
	 * @param voMap
	 * @param stu
	 */
	private void  changePlanOutCourseToTeachingPlanVo(Map<String,Object>  voMap,StudentInfo stu,int voIndex,List<StudentLearnPlan> studentLearnPlan){
		//FIXED by hzg 使用整理好的结果集，避免再次查询
		//List<StudentLearnPlan> planOutLearnPlan = this.getStudentPlanOutLearnPlanList(stu.getResourceid());
		List<LearningPlanVo> list     		    = new ArrayList<LearningPlanVo>();
		if (null!=studentLearnPlan && !studentLearnPlan.isEmpty()) {
			int i = 0;
			for(StudentLearnPlan plan:studentLearnPlan){
				++i;
				//Course course                 = plan.getPlanoutCourse();
				LearningPlanVo vo             = new  LearningPlanVo();
				String courseExamType         = ExStringUtils.isBlank(plan.getExamType())?"":plan.getExamType();
				String courseName             = ExStringUtils.defaultIfEmpty(plan.getCourseName(),"");
				Double courseCreditHour       = null==plan.getPlanoutCreditHour()?-1.0:plan.getPlanoutCreditHour();

				vo.setTeachingPlan_Course_examType(JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",courseExamType));
				vo.setCourse_Id(plan.getPlanOutcourseId());
				vo.setTeachingPlan_Course_name(courseName);
				vo.setTeachingPlan_Course_creditHour(courseCreditHour);
				vo.setIsPlanCourse(Constants.BOOLEAN_NO);
				vo.setTeachingPlan_Course_num(voIndex+i);
				//ExamResults learnPlanExamResult = plan.getExamResults();
				
				String  leanPlanIntegratedScoreStr = ExStringUtils.isBlank(plan.getIntegratedScore())?"-1.0":plan.getIntegratedScore();    //综合成绩
				String  leanPlanWrittenScoreStr = ExStringUtils.isBlank(plan.getWrittenScore())?"-1.0":plan.getWrittenScore();//卷面成绩
				String  leanPlanUsuallyScoreStr = ExStringUtils.isBlank(plan.getUsuallyScore())?"-1.0":plan.getUsuallyScore();//平时成绩				
				String  leanPlanExamAbnormityStr= ExStringUtils.isBlank(plan.getExamAbnormity())?"0":plan.getExamAbnormity();         //成绩异常代码
				String  examResultsStatus       = ExStringUtils.isBlank(plan.getCheckStatus())?"-1":plan.getCheckStatus();          //成绩审核状态
				String  courseScoreType         = ExStringUtils.isEmpty(plan.getCourseScoreType())?"11":plan.getCourseScoreType();      //成绩类型 百分制 二分制 五分制
				Integer leanPlanStatus          = null==plan.getStatus()?-1:plan.getStatus();                                   //学习计划状态
								
			    //如果当前学习计划的状态为已发布，则获取当前课程有效的最高成绩
				if (3==leanPlanStatus) {
					Map<String,Object> courseRs = (Map<String,Object>)voMap.get(plan.getPlanOutcourseId());
					if (null!=courseRs) {						
						leanPlanIntegratedScoreStr  = ExStringUtils.isBlank((String)courseRs.get("INTEGRATEDSCORE"))?"-1.0":(String)courseRs.get("INTEGRATEDSCORE");
						leanPlanWrittenScoreStr = ExStringUtils.isBlank((String)courseRs.get("WRITTENSCORE"))?"-1.0":(String)courseRs.get("WRITTENSCORE");
						leanPlanUsuallyScoreStr =  ExStringUtils.isBlank((String)courseRs.get("USUALLYSCORE"))?"-1.0":(String)courseRs.get("USUALLYSCORE");									
						courseScoreType  = ExStringUtils.isBlank((String)courseRs.get("COURSESCORETYPE"))?"11":(String)courseRs.get("COURSESCORETYPE");
						leanPlanExamAbnormityStr= "0";
					}
				}
				//if (null==leanPlanIntegratedScoreStr)       leanPlanIntegratedScoreStr ="-1.0";
				//if ("".equals(leanPlanIntegratedScoreStr))  leanPlanIntegratedScoreStr ="-1.0";
				//if (null==leanPlanExamAbnormityStr)      leanPlanExamAbnormityStr="0";
				//if ("".equals(leanPlanExamAbnormityStr)) leanPlanExamAbnormityStr="0";
				//if (null==examResultsStatus) 			 examResultsStatus="-1";
				//if ("".equals(examResultsStatus))		 examResultsStatus="-1";
				
				Double leanPlanIntegratedScoreDou  = -1.0;
				Double leanPlanExamAbnormityDou = Double.parseDouble(leanPlanExamAbnormityStr);
				//默认成绩不进行百分制转换
				if (Double.parseDouble(leanPlanIntegratedScoreStr)>=0) {
					leanPlanIntegratedScoreDou         = Double.parseDouble(studentExamResultsService.convertScore(courseScoreType, leanPlanIntegratedScoreStr));
				}
				Double leanPlanWrittenScoreDou = -1.0;
				if(Double.parseDouble(leanPlanWrittenScoreStr)>=0){
					leanPlanWrittenScoreDou = Double.parseDouble(studentExamResultsService.convertScore(courseScoreType, leanPlanWrittenScoreStr));
				}
				//成绩，显示在页面上的
				String 	scorStr  = String.valueOf(leanPlanIntegratedScoreDou);//综合成绩
				String writtenScoreStr = String.valueOf(leanPlanWrittenScoreDou);//卷面成绩
				
				Date passDate                   = plan.getAuditDate();
				//if (null!=learnPlanExamResult) {
				//	passDate                    = learnPlanExamResult.getAuditDate();
				//}
				//如果不是百分制的成绩则转换成对应的成绩级别字符
				if (!Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(courseScoreType)) {
					scorStr          = studentExamResultsService.convertScoreStr(courseScoreType, leanPlanIntegratedScoreStr);
					writtenScoreStr   = studentExamResultsService.convertScoreStr(courseScoreType, leanPlanWrittenScoreStr);
				}
			           
				//未预约学习
				if (leanPlanStatus==1) {
					vo.setTeachingPlan_Course_learnStatusInt(2);
					vo.setTeachingPlan_Course_bookingStatus("预约考试");
					vo.setTeachingPlan_Course_learnStatus("已预约学习");
					
				//预约考试	
				}else if (leanPlanStatus==2&&leanPlanIntegratedScoreDou==-1.0) {
					
					vo.setTeachingPlan_Course_learnStatusInt(2);
					vo.setTeachingPlan_Course_bookingStatus("准备考试");
					vo.setTeachingPlan_Course_learnStatus("已预约考试");	
					try {
						//if (null!=plan.getExamInfo()) {
							vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(plan.getExamStartTime(),"yyyy年MM月dd日 HH:mm:ss"));
							vo.setTeachingPlan_Course_examEndTime(ExDateUtils.formatDateStr(plan.getExamEndTime(),"HH:mm:ss"));
						//}
					}catch (Exception e) {
						//logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合操作出错：{}",e.fillInStackTrace());
					}	
					

					
				//已考完成绩录入中
				}else if(leanPlanStatus==2&&leanPlanIntegratedScoreDou!=-1.0&& !"4".equals(examResultsStatus)){
					
					vo.setTeachingPlan_Course_learnStatusInt(3);
					vo.setTeachingPlan_Course_bookingStatus("成绩录入中");
					vo.setTeachingPlan_Course_learnStatus("已考完");	

					
				//此条件为防止旧数据中成绩已发布，但未更新对应考试批次的学生的学生计划	
				}else if(leanPlanStatus==2&&leanPlanIntegratedScoreDou!=-1.0&& "4".equals(examResultsStatus)&&leanPlanIntegratedScoreDou>=60){
					
					vo.setTeachingPlan_Course_learnStatusInt(3);
					vo.setTeachingPlan_Course_bookingStatus("已考完");
					vo.setTeachingPlan_Course_learnStatus("完成学习");	
					
					//if (ExStringUtils.isNotEmpty(scorStr)) {
						vo.setTeachingPlan_Course_scoreStr(scorStr);
						vo.setTeachingPlan_Course_WrittenScore(writtenScoreStr);
						vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
					//}else {
					//	vo.setTeachingPlan_Course_score(leanPlanWrittenScoreDou);
					//}
					
					

					
				//此条件为防止旧数据中成绩已发布，但未更新对应考试批次的学生的学习计划	
				}else if(leanPlanStatus==2&&leanPlanIntegratedScoreDou!=-1.0&& "4".equals(examResultsStatus)&&leanPlanIntegratedScoreDou<60){
					
					vo.setTeachingPlan_Course_learnStatusInt(2);
					vo.setTeachingPlan_Course_bookingStatus("预约考试");
					vo.setTeachingPlan_Course_learnStatus("成绩不合格");
					vo.setIsNeedReExamination("Y");
					
					//if (ExStringUtils.isNotEmpty(scorStr)) {
						vo.setTeachingPlan_Course_scoreStr(scorStr);
						vo.setTeachingPlan_Course_WrittenScore(writtenScoreStr);
						vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
					//}else {
					//	if (leanPlanWrittenScoreDou>0) 
					//	vo.setTeachingPlan_Course_score(leanPlanWrittenScoreDou);
					//}
					
					

				//已考完	 成绩正常并大于60
				}else if (leanPlanStatus==3&&leanPlanIntegratedScoreDou>=60 && leanPlanExamAbnormityDou==0) {
					vo.setTeachingPlan_Course_learnStatusInt(3);
					vo.setTeachingPlan_Course_bookingStatus("已考完");
					vo.setTeachingPlan_Course_learnStatus("完成学习");

					//if (ExStringUtils.isNotEmpty(scorStr)) {
						vo.setTeachingPlan_Course_scoreStr(scorStr);
						vo.setTeachingPlan_Course_WrittenScore(writtenScoreStr);
						vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
					//}else {
					//	vo.setTeachingPlan_Course_score(leanPlanWrittenScoreDou);
					//}
					
					try {
						//if (null!=plan.getExamInfo()) {
							vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(plan.getExamStartTime(),"yyyy年MM月dd日 HH:mm:ss"));
							vo.setTeachingPlan_Course_examEndTime(ExDateUtils.formatDateStr(plan.getExamEndTime(),"HH:mm:ss"));
						//}
					}catch (Exception e) {
						//logger.error("把学生的教学计划及学习计划转换为TeachingPlanVo对象集合操作出错：{}",e.fillInStackTrace());
					}	

					
				//已考完	 成绩正常并小于60	
				}else if (leanPlanStatus==3&&leanPlanIntegratedScoreDou<60 && leanPlanExamAbnormityDou==0) {
					
					vo.setTeachingPlan_Course_learnStatusInt(2);
					vo.setTeachingPlan_Course_bookingStatus("重考");
					vo.setTeachingPlan_Course_learnStatus("成绩不合格");
					vo.setIsNeedReExamination("Y");
					//if (ExStringUtils.isNotEmpty(scorStr)) {
						vo.setTeachingPlan_Course_scoreStr(scorStr);
						vo.setTeachingPlan_Course_WrittenScore(writtenScoreStr);
						vo.setTeachingPlan_Course_UsuallyScore(leanPlanUsuallyScoreStr);
					//}else {
						//if (leanPlanWrittenScoreDou>0) 
						//vo.setTeachingPlan_Course_score(leanPlanWrittenScoreDou);
					//}

				//已考完	 成绩异常
				}else if(leanPlanStatus==3 && leanPlanExamAbnormityDou>0) {
					
					vo.setTeachingPlan_Course_learnStatusInt(2);
					vo.setTeachingPlan_Course_bookingStatus("预约考试");
					String ExamAbnormityStr = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", String.valueOf(leanPlanExamAbnormityDou.intValue()));
					vo.setTeachingPlan_Course_learnStatus(ExamAbnormityStr);
					vo.setIsNeedReExamination("Y");
				
				}
				try {
					if (null!=passDate) {
						vo.setTeachingPlan_Course_examStartTime(ExDateUtils.formatDateStr(passDate, "yyyy年MM月dd日 "));
						vo.setTeachingPlan_Course_examEndTime("");
					}
				} catch (Exception e) {
					logger.error("把学生的学习计划(计划外课程)转换为TeachingPlanVo对象集合操作出错：{}",e.fillInStackTrace());
				}
				list.add(vo);
			}
		}
		voMap.put("planOutCourseList", list);
	}
	
	//设置返回的resultMap的消息数组
	public Map<String,Object> setResultMapMsg(Map <String,Object> resultMap,String msg){
	
	   if (null==resultMap) {
		   resultMap = new HashMap<String, Object>();
	   }
	   
	    List<String> msgList =  (List<String>) resultMap.get("msg"); 

        if (null==msgList) {
        	msgList = new ArrayList<String>() ;
        	msgList.add(msg);
			resultMap.put("msg",msgList);
		}else {
			msgList.add(msg);
			resultMap.put("msg",msgList);
		}
        
		return resultMap;
	}
	
	
	/**-------------------------------------------------------------------------预约学习---------------------------------------------------------------------------------------------------------------**/
		
	

	/**
	 * 预约学习-检查是否允许预约所选课程
	 * @return resultMap                 
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> isAllowOrderCourse(Map<String, Object> paramMap) {
		
		boolean isArrowOrderCourse = false; 
		
		try {
			if (null!=paramMap) {
				
				//获得学生的学籍信息
				StudentInfo studentInfo          = (StudentInfo) paramMap.get("studentInfo");
				//学生的学习计划
				List<StudentLearnPlan> learnPlan = (List<StudentLearnPlan>) paramMap.get("learnPlan");
				//要进行预约操作的教学计划课程
				TeachingPlanCourse orderCourse   = (TeachingPlanCourse) paramMap.get("orderCourse");	
				//年度的预约状态
				boolean yearOrderStatus 		 = false;
				//当前年级
				Grade currentGrade      	  	 = gradeService.findUniqueByProperty("isDefaultGrade", "Y");
				//允许预约的学期
				String allowTerm       	   		 = StudentCourseOrderUtil.getAllowOrderCourseTerm(currentGrade,studentInfo.getGrade());
				//补预约权限记录
				List<ReOrderSetting> reOrderSet  = null;
				
				//-------------------------------------------学生自主预约,要求在年度预约时限范围内完成操作-------------------------------------------
				if("student".equals(paramMap.get("operater").toString())){
					
					List<OrderCourseSetting> settingList = checkYearCourseOrderBookIngStratus();
					if (null!=settingList&&!settingList.isEmpty()) {
						
						OrderCourseSetting setting=settingList.get(0);
						
						Date curTime       		 = new Date();
						Date startOrderTime 	 = setting.getStartDate();
						Date endOrderTime   	 = setting.getEndDate();
						
						if (null!=startOrderTime && null!=endOrderTime) {
							if (curTime.getTime()>= startOrderTime.getTime() &&
								curTime.getTime()<= endOrderTime.getTime()) {//在预约时限范围内
								yearOrderStatus  = true;
							}else {
								paramMap.put("branchSchool", studentInfo.getBranchSchool().getResourceid());
								paramMap.put("reOrderType", "1");
								paramMap.put("orderCourseSetting", setting.getResourceid());
								
								reOrderSet       = reOrderSettingService.findReOrderSettingByCondition(paramMap);
								paramMap.remove("branchSchool");
								paramMap.remove("reOrderType");
								paramMap.remove("orderCourseSetting");
								//补预约时间
								if (null!=reOrderSet&&reOrderSet.size()>0&&
									curTime.getTime()>=reOrderSet.get(0).getStartTime().getTime()&&
									curTime.getTime()<=reOrderSet.get(0).getEndTime().getTime()) {
									yearOrderStatus  = true;
								}else {
									yearOrderStatus  = false;
									String msg       = "当前不在预约时间范围内,如果已超过预约时间请与教务人员联系！";
									paramMap         = setResultMapMsg(paramMap,msg);
								}
							}
						}else {
							String msg      	 = "当前年度预约权限未设置时间范围,请联系教务管理员！";
							paramMap        	 = setResultMapMsg(paramMap,msg);
							yearOrderStatus 	 = false;
						}
						paramMap.put("orderCourseSetting", settingList.get(0));
					}else {
						String msg      		 = "年度预约未开放！";
						paramMap        		 = setResultMapMsg(paramMap,msg);
						yearOrderStatus 		 = false;
					}
					
				/* -------------------------------------------错过预约时限，老师帮学生预约-------------------------------------------
				（此操作不要求年度预约权限开放，但应传入一个年度预约设置对象以便预约统计）*/
				}else if ("teacher".equals(paramMap.get("operater").toString())) {
					
					if (paramMap.containsKey("chooseSetting")&&null!=paramMap.get("chooseSetting")){
						
						paramMap.put("orderCourseSetting",paramMap.get("chooseSetting"));
						paramMap.remove("chooseSetting");
						
						yearOrderStatus 		 = true;
					}else {
						String msg      		 = "请选择一个预约年度！";
						paramMap        		 = setResultMapMsg(paramMap,msg);
						yearOrderStatus 		 = false;
					}
					
				}
				
				//检查是否已超过年度预约上限
				paramMap = checkIsFullInStudentLearnPlan(paramMap);
				boolean isFullInStudentLearnPlan =(Boolean) paramMap.get("isFullInStudentLearnPlan");
				
				//个人的预约状态
				boolean personalOrderStatus = 1 ==studentInfo.getOrderCourseStatus()? true:false;
			
				boolean isAllowStatus       =  false;
				
				//学籍状态
				String studentstatus        = null==studentInfo.getStudentStatus()?"":studentInfo.getStudentStatus();
				//允许在学及延期的学生预约
				if ("11".equals(studentstatus)||"21".equals(studentstatus)||"25".equals(studentstatus)) {
					isAllowStatus           = true;
				}
				//是否交费
				Map isFeeMap  = courseOrderService.checkFeeByMatriculateNoticeNoForCourseOrder(studentInfo.getStudyNo());
				boolean isFee = (Boolean)isFeeMap.get("isFee");
				
				//当已交费，年度预约开放，个人预约权限开放,未超过年度预约上限的情况下才允许进行预约学习
				if (yearOrderStatus && personalOrderStatus&&isFullInStudentLearnPlan==false && isFee&&isAllowStatus) {	
					
					//1.检查当前预约的课程的学期是否为允许的学期
					paramMap  = isAllowTermOfCourseOrder(orderCourse,paramMap,allowTerm);
					boolean isAllowTerm    		  =  (Boolean)paramMap.get("isAllowTerm"); 
					
					//2.要进行预约操作的教学计划课程是否为第一学期课程 如果是第一学期课程返回False不允许继续预约
					paramMap = firstTermCourse(orderCourse,paramMap,allowTerm);
					boolean isfirstTermCoursePass = (Boolean)paramMap.get("isFirstTermCoursePass");
					
					//3.检查前置课程
					paramMap = beforeCourse(orderCourse,learnPlan,paramMap);
					boolean isbeforeCoursePass    = (Boolean)paramMap.get("isBeforeCoursePass");
					
					//4.检查考试时间冲突
					paramMap=examConFlict(orderCourse,learnPlan,paramMap);
					boolean isExamConFlictPass    = (Boolean)paramMap.get("isExamConFlictPass");
					
					//5.检查是否面授课程
					paramMap=checkIsFaceStudyCourse(orderCourse,paramMap);
					boolean isFaceStudyCourse    = (Boolean)paramMap.get("isFaceStudyCourse");
					
					//当 是否为第一学期课程检查,前置课程检查,考试时间冲突检查 ,是否面授课 都通过时才允许预约学习所选课程
					if ( isfirstTermCoursePass && isbeforeCoursePass && isExamConFlictPass &&isAllowTerm&&isFaceStudyCourse==false){
						isArrowOrderCourse = true;
					}else {
						isArrowOrderCourse = false;
					}
					
				}else {
					
					if (isAllowStatus == false) {
						String msg         = "系统只允许在学、延期状态的学员预约，您当前的学籍状态为："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", studentstatus);
						paramMap           = setResultMapMsg(paramMap,msg);
					}
					if(isFee == false){
						String msg         = "你的预约权限未开放，原因为未交费！";
						paramMap           = setResultMapMsg(paramMap,msg);
					}
					if (personalOrderStatus==false) {
						String msg         = "你的预约学习权限未开放，请联系管理人员！";
						paramMap           = setResultMapMsg(paramMap,msg);
					}
					if (isFullInStudentLearnPlan) {
						OrderCourseSetting setting = (OrderCourseSetting)paramMap.get("orderCourseSetting");
						String yearInfoName        = setting.getYearInfo().getYearName()+"-"+JstlCustomFunction.dictionaryCode2Value("CodeTerm", setting.getTerm());
						String msg                 = "你当已达到本年度预约学习上限！<br/>"+yearInfoName+",允许预约学习的课程数为："+setting.getLimitOrderNum();
						paramMap                   = setResultMapMsg(paramMap,msg);
					}
					isArrowOrderCourse = false;
					
				}
				paramMap.put("isArrowOrderCourse", isArrowOrderCourse);
			}else {
				paramMap.put("isArrowOrderCourse", isArrowOrderCourse);
			}
		} catch (Exception e) {
			
			paramMap.put("isArrowOrderCourse", false);
			String msg = "检查是否允许预约所选课程异常："+e.getMessage();
			paramMap = setResultMapMsg(paramMap, msg);
			
			logger.error("预约学习-检查是否允许预约所选课程异常：{}",e.fillInStackTrace());
		}
		
		return paramMap;
	}

	/**
	 * 预约学习-查询年度的预约权限
	 * @param checkYearCourseOrderBookIngStratus
	 * @return
	 */
	public List<OrderCourseSetting> checkYearCourseOrderBookIngStratus(){
		try {
			List<OrderCourseSetting> list= orderCourseSettingService.findOpenedSetting();
			return list;
		} catch (Exception e) {
			logger.error("预约学习-查询年度的预约权限异常：{}",e.fillInStackTrace());
			return null;
		}
	}
	/**
	 * 预约学习-检查是否已超过年度最大预约科目数据
	 * @param paramMap
	 * @return
	 */
	@Override
	public Map<String,Object> checkIsFullInStudentLearnPlan(Map<String, Object> paramMap){
		try {
			//学生的学习计划
			List<StudentLearnPlan> learnPlan = (List<StudentLearnPlan>) paramMap.get("learnPlan");
			//年度预约设置
			OrderCourseSetting setting 		 = (OrderCourseSetting) paramMap.get("orderCourseSetting");
			//年度预约设置关联年度ID
			String setting_yearId		     = setting.getYearInfo().getResourceid();
			//年度预约设置关联学期
			String setting_term              = setting.getTerm();
			
			boolean isFullInStudentLearnPlan = true;
			
			if (null==learnPlan||learnPlan.isEmpty()) {
				isFullInStudentLearnPlan = false;
				
			}else {
				int currentCourseOrderCounts  = 0 ; 
				//计算当前学习计划本年度已预约的科目数
				for (StudentLearnPlan p:learnPlan) {
					//学习计划关联年度ID
					String learnPlan_yearId = p.getYearInfo().getResourceid();
					//学习计划关联学期
					String learnPlan_term   = p.getTerm();
					if (learnPlan_yearId.equals(setting_yearId) & learnPlan_term.equals(setting_term)) {
						currentCourseOrderCounts +=1;
					}
				}
				if (currentCourseOrderCounts<=setting.getLimitOrderNum()) {
					isFullInStudentLearnPlan = false;
				}else {
					isFullInStudentLearnPlan = true;
				}
			}
			paramMap.put("isFullInStudentLearnPlan", isFullInStudentLearnPlan);
		} catch (Exception e) {
			paramMap.put("isFullInStudentLearnPlan", false);
			String msg = "检查是否已超过年度最大预约科目数出错！";
			paramMap = setResultMapMsg(paramMap, msg);
			
			logger.error("预约学习-检查是否已超过年度最大预约科目数异常：{}",e.fillInStackTrace());
		}
		
		return paramMap;
	}
	/**
	 *  预约学习-检查是否允许预约所选课程:   1、  要预约的课程是否是当前学期以前的课程
	 * @param orderCourse
	 * @param resultMap
	 * @return
	 */
	public Map isAllowTermOfCourseOrder(TeachingPlanCourse orderCourse,Map resultMap,String allowTerm ){
	
		boolean isAllowTerm  = false;
			
		int allowTerm1       = Integer.parseInt(allowTerm);
		int allowTerm2       = Integer.parseInt(orderCourse.getTerm());
		
		if (allowTerm2<=allowTerm1) {
			isAllowTerm      = true;
		}else {
			String msg 		 = "您当能只能预约学习第"+allowTerm1+"学期及以前的课程!";
			resultMap 		 = setResultMapMsg(resultMap,msg);
		}
		
		resultMap.put("isAllowTerm", isAllowTerm);
		return resultMap;
	}
	/**
	 * 预约学习-检查是否允许预约所选课程:    2、检查是否为第一学期的课程
	 * @param orderCourse
	 * @param resultMap
	 * @return
	 */
	public Map firstTermCourse(TeachingPlanCourse orderCourse,Map resultMap,String allowTerm) {
		
		try {
			boolean isfirstTermCourse = null==orderCourse.getTerm()?false:"1".equals(orderCourse.getTerm());
			
			//如果当前操作者为管理员则不检查是否为第一学期课程  2011.7.25提出此需求(原因是有部分旧生的第一学期课程预约不完整，学生不能预约)
			if ("teacher".equals(resultMap.get("operater").toString())) {
				resultMap.put("isFirstTermCoursePass", true);
			
			//当前操作者为学生时
			}else{
				//当前学生允许预约的学期不为第一学期(旧生)则第一学期的课程可以预约 2011.7.25提出此需求
				if (Integer.valueOf(allowTerm)>1) {
					
					resultMap.put("isFirstTermCoursePass", true);
					
				//当前学生允许预约的学期为第一学期则检查当前预约的课程是否为首学期的,如果是新生不允许预约
				}else {
					if (isfirstTermCourse==true) {
						
						resultMap.put("isFirstTermCoursePass", false);
						String msg = "新生第一学期课程由教务人员预约！";
						resultMap = setResultMapMsg(resultMap, msg);
						
					}else {
						resultMap.put("isFirstTermCoursePass", true);
					}
				}
				
			}
			
		} catch (Exception e) {
			resultMap.put("isFirstTermCoursePass", false);
			String msg = "检查是否为第一学期课程出错！";
			resultMap = setResultMapMsg(resultMap, msg);
			
			logger.error("预约学习-检查是否为第一学期的课程异常：{}",e.fillInStackTrace());
		}
		return resultMap;
	}

	/**
	 * 预约学习-检查是否允许预约所选课程:    3、检查是否有前置课程
	 * @param orderCourse
	 * @param learnPlan
	 * @param resultMap
	 * @return
	 */
	public Map beforeCourse(TeachingPlanCourse orderCourse,List<StudentLearnPlan> learnPlan,Map<String,Object> resultMap){
		try {
			if (null!=resultMap&&null!=resultMap.get("isFirstTermCoursePass")&&(Boolean)resultMap.get("isFirstTermCoursePass")) {
				
				//是否有前置课程
				boolean isbeforeCourse = null==orderCourse.getBeforeCourse()?false:true;
				
				//有前置课程并且学习计划不为空
				if (isbeforeCourse==true && null!=learnPlan && !learnPlan.isEmpty()) {
					
					Course beforeCourse = orderCourse.getBeforeCourse();
					String beforeCourseId  = beforeCourse.getResourceid();
					boolean isBeforeCoursePass = false;
					
					for (StudentLearnPlan plan :learnPlan) {
						Course learnPlanCourse   = plan.getTeachingPlanCourse().getCourse();
						String learnPlanCourseId = learnPlanCourse.getResourceid();
						ExamResults examResult   = plan.getExamResults();
						String writtenScoreStr   = null==examResult?"0":examResult.getWrittenScore();
						if ("".equals(writtenScoreStr)) {
							writtenScoreStr ="0";
						}
						int writtenScoreInt      = Integer.parseInt(writtenScoreStr); 
						
						if (beforeCourseId.equals(learnPlanCourseId)) {
							if (null!=examResult && writtenScoreInt>0) {
								isBeforeCoursePass = true;
								break;
							}
						}
					}
					
					if (isBeforeCoursePass) {
						resultMap.put("isBeforeCoursePass", true);
					}else {
						
						String msg = "选修"+orderCourse.getCourse().getCourseName()+"必需先修完"+orderCourse.getBeforeCourse().getCourseName();
						resultMap = setResultMapMsg(resultMap,msg);
						resultMap.put("isBeforeCoursePass", false);
					}
					
				//有前置课程并且学习计划为空	
				}else if(isbeforeCourse==true && (null==learnPlan || learnPlan.isEmpty())){
					
					resultMap.put("isBeforeCoursePass", false);
					String msg =  "选修"+orderCourse.getCourse().getCourseName()+" 必需先修完"+orderCourse.getBeforeCourse().getCourseName();
					resultMap  = setResultMapMsg(resultMap,msg);
					
				//没有前置课程
				}else if(isbeforeCourse==false){
					resultMap.put("isBeforeCoursePass", true);
				}
			}else {
				if (null==resultMap) {
					resultMap = new HashMap<String, Object>();
				}
				resultMap.put("isBeforeCoursePass", false);
			}
			
		} catch (Exception e) {
			resultMap.put("isBeforeCoursePass", false);
			String msg ="预约学习-检查是否有前置课程出错!";
			resultMap = setResultMapMsg(resultMap, msg);
			
			logger.error("预约学习-检查是否有前置课程异常：{}",e.fillInStackTrace());
		}
		return resultMap;
	}
	/**
	 * 预约学习-检查是否允许预约所选课程:    4、检查考试时间冲突
	 * @param orderCourse
	 * @param learnPlan
	 * @param resultMap
	 * @return
	 */
	public Map<String,Object> examConFlict(TeachingPlanCourse orderCourse,List<StudentLearnPlan> learnPlan,Map<String,Object> resultMap){
		try {
			//考试形式 (大作业)
			Long examType 		= null==orderCourse.getCourse().getExamType()?0L:orderCourse.getCourse().getExamType();
			//课程类型 (毕业论文,实践环节)
			String courseType 	= ExStringUtils.defaultIfEmpty(orderCourse.getCourseType(), "");
			//统考课
			//List<StateExamCourse> statExamCourses = stateExamCourseService.findByCriteria(Restrictions.eq("teachingPlanCourse.resourceid", orderCourse.getResourceid()));
			
			//当预约的课程为考试形式为大作业,或课程类型为毕业论文、实践环节，统考的课程就不用检查考试冲突 ,
			if (3==examType || "66".equals(courseType) || "thesis".equals(courseType)){//||(null!=statExamCourses&&statExamCourses.size()>0)) {
				resultMap.put("isExamConFlictPass",true);
				resultMap.put("isNeedChooseCourse", false);
				return resultMap;
			}
			//网络+面授的课程不需要检查考试冲突
			if (ExStringUtils.isNotBlank(orderCourse.getTeachType())&&"netsidestudy".equals(orderCourse.getTeachType())) {
				resultMap.put("isExamConFlictPass",true);
				resultMap.put("isNeedChooseCourse", false);
				return resultMap;
			}
			if (null!=resultMap&&null!=resultMap.get("isBeforeCoursePass")&&(Boolean)resultMap.get("isBeforeCoursePass")) {
				
				if (null!=learnPlan && !learnPlan.isEmpty()) {
					
					//年度预约设置
					OrderCourseSetting setting 		 = (OrderCourseSetting) resultMap.get("orderCourseSetting");
					
					String settingYearInfoId         = setting.getYearInfo().getResourceid();
					String settingTerm               = setting.getTerm();
					String detailsHQL 			     = "from "+ExamSettingDetails.class.getSimpleName()+" details where details.isDeleted=0 and  (details.isSpecial != 'Y' or  details.isSpecial is null) and details.courseId.resourceid=?";
					
					//预约课程的考试时间设置对象
					ExamSettingDetails orderCourseExamDetails =  null;
					List<ExamSettingDetails> detailsList      = examSettingDetailsService.findByHql(detailsHQL,orderCourse.getCourse().getResourceid());
					if (null!=detailsList && !detailsList.isEmpty()) {
						orderCourseExamDetails = detailsList.get(0);
					}
					boolean isExamConFlictPass = true;
					boolean isNeedChooseCourse = false;
					
					if (null!=orderCourseExamDetails) {
						//预约课程的考试设置名称 如:  第一天上午   第一天下午   第二天上午.....
						String orderCourseExamSettingName=orderCourseExamDetails.getExamSetting().getSettingName();
						
						for (StudentLearnPlan plan:learnPlan) {
							
							//学习计划中的课程ID
							String learnPlanCourseId = plan.getTeachingPlanCourse().getCourse().getResourceid();
							String planYearInfoId    = plan.getYearInfo().getResourceid();
							String planTerm          = plan.getTerm();
							
							//学习计划中的课程考试时间设置对象
							ExamSettingDetails learnPlanCourseExamDetails            =  null;
							List<ExamSettingDetails> learnPlanCourseExamDetailsList  = examSettingDetailsService.findByHql(detailsHQL,learnPlanCourseId);
							if (null!=learnPlanCourseExamDetailsList && !learnPlanCourseExamDetailsList.isEmpty()) {
								learnPlanCourseExamDetails = learnPlanCourseExamDetailsList.get(0);
							}
							
							//学习计划中的课程的考试设置名称 如: 第一天上午   第一天下午   第二天上午.....
							String learnPlanCourseExamSettingName = null==learnPlanCourseExamDetails?"":learnPlanCourseExamDetails.getExamSetting().getSettingName();

							//当 预约课程的考试设置名称 与 学习计划中的课程的考试设置名称 相同时，表示两门课程的考试时间在同一个时段
							if (orderCourseExamSettingName.equals(learnPlanCourseExamSettingName)& settingYearInfoId.equals(planYearInfoId) & settingTerm.equals(planTerm)) {
								isExamConFlictPass = false;
								isNeedChooseCourse = true;
								String msg ="<strong>考试时间冲突:</strong></br>";
							    msg+= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='blue'>"+orderCourse.getCourse().getCourseName()+"</font>---<font color='red'>"+orderCourseExamSettingName+"</font></br>";
						        msg+= "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color='blue'>"+plan.getTeachingPlanCourse().getCourse().getCourseName()+"</font>---<font color='red'>"+learnPlanCourseExamSettingName+"</font></br>";
						        resultMap = setResultMapMsg(resultMap,msg);
								break;
							}
						}
					}else {
						isExamConFlictPass=false;
						String msg ="未安排考试时间,暂时不允许预约!";
						resultMap = setResultMapMsg(resultMap,msg);
					}
					resultMap.put("isNeedChooseCourse", isNeedChooseCourse);
					resultMap.put("isExamConFlictPass",isExamConFlictPass);

				}else {
					resultMap.put("isExamConFlictPass",true);
				}
			}else {
				if (null==resultMap) {
					resultMap = new HashMap<String, Object>();
				}
				resultMap.put("isExamConFlictPass",false);
			}
		} catch (Exception e) {
			resultMap.put("isExamConFlictPass",false);
			String msg = "预约学习-检查考试时间冲突出错！";
			resultMap = setResultMapMsg(resultMap, msg);
			
			logger.error("预约学习-检查考试时间冲突异常：{}",e.fillInStackTrace());
		}
		return resultMap;
	}
	/**
	 * 预约学习-检查是否允许预约所选课程:    5、检查是否面授课
	 * @param orderCourse
	 * @param resultMap
	 * @return
	 */
	public Map<String,Object> checkIsFaceStudyCourse(TeachingPlanCourse orderCourse,Map<String,Object> resultMap){
		boolean isFaceStudyCourse = false;
		if(CacheAppManager.getSysConfigurationByCode("teachType.facestudy").getParamValue().equals(orderCourse.getTeachType())){
			isFaceStudyCourse     = true;
		}
		if (isFaceStudyCourse) {
			String msg ="面授课程不用预约学习!";
			resultMap = setResultMapMsg(resultMap,msg);
		}
		resultMap.put("isFaceStudyCourse",isFaceStudyCourse);
		return resultMap;
	}
	/**
	 * 预约学习-条件满足时的增加及更新操作
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> allowOrderCourseOperate(Map<String, Object> paramMap) throws ServiceException {
		

		if (null!=paramMap &&null!=paramMap.get("isArrowOrderCourse")&&(Boolean)paramMap.get("isArrowOrderCourse")) {
			
			//1.更新学习计划
			paramMap=updateStudentLearnPlan(paramMap);
			boolean updateStudentLearnPlan = null==paramMap.get("updateStudentLearnPlan")?false:(Boolean)paramMap.get("updateStudentLearnPlan");
			
			//2.增加教材预约
//			if (updateStudentLearnPlan) {
//				paramMap = addCourseBoookOrders(paramMap);
//			}else {
//				throw new ServiceException("预约学习-更新学习计划操作中断,数据不合法或缺少数据！");
//			}
			
		//	boolean addCourseBoookOrders = null==paramMap.get("addCourseBoookOrders")?false:(Boolean)paramMap.get("addCourseBoookOrders");
//			if (!addCourseBoookOrders) {
//				throw new ServiceException("预约学习-增加教材预约操作中断,数据不合法或缺少数据！");
//			}	
			
			//3.更新预约统计及明细表
			if (updateStudentLearnPlan) {
				paramMap=updateCourseOrderAndCourseOrderStat(paramMap);
			}else {
				throw new ServiceException("预约学习-更新学习计划操作中断,数据不合法或缺少数据！");
			}
			boolean updateCourseOrderAndCourseOrderStatSuccess = null==paramMap.get("updateCourseOrderAndCourseOrderStatSuccess")?false:(Boolean)paramMap.get("updateCourseOrderAndCourseOrderStatSuccess");
			
			//4.返回处理结果
			if (updateCourseOrderAndCourseOrderStatSuccess&&updateStudentLearnPlan) {
				paramMap.put("orderCourseOperateStatus", true);
			}else {
				paramMap.put("orderCourseOperateStatus", false);
			}
			
		}else {
			paramMap.put("orderCourseOperateStatus", false);
			String msg = "操作中断,数据不合法或缺少数据！";
			paramMap = setResultMapMsg(paramMap, msg);
			
			throw new ServiceException("操作中断,数据不合法或缺少数据！");
		}

		return paramMap;
		
	}
	
	
	/**
	 * 预约学习 1 ---------更新学习计划
	 */
	public Map<String,Object> updateStudentLearnPlan(Map<String,Object> paramMap) throws ServiceException {
	
		if (null!=paramMap&&null!=paramMap.get("isArrowOrderCourse")&&(Boolean)paramMap.get("isArrowOrderCourse")){
			
			//获得学生的学籍信息
			StudentInfo studentInfo 		    = (StudentInfo) paramMap.get("studentInfo");
			//要进行预约操作的教学计划课程
			TeachingPlanCourse orderPlanCourse  = (TeachingPlanCourse) paramMap.get("orderCourse");	
			
			//年度预约权限设置对象 
			OrderCourseSetting setting		    = (OrderCourseSetting) paramMap.get("orderCourseSetting");
			
			//学生的学习计划
			List<StudentLearnPlan> oldLearnPlan = (List<StudentLearnPlan>) paramMap.get("learnPlan");
			
			if (null!=oldLearnPlan && !oldLearnPlan.isEmpty()) {
				
				boolean isLearnPlanhas 			= false;
				
				for (StudentLearnPlan plan : oldLearnPlan) {
					TeachingPlanCourse learnPlanCourse = plan.getTeachingPlanCourse();
					if (orderPlanCourse.getResourceid().equals(learnPlanCourse.getResourceid())) {//学习计划中已有需要预约操作的课程
						isLearnPlanhas = true;
						String msg 	   = "你已预约了当前课程！";
						paramMap 	   = setResultMapMsg(paramMap, msg);
						paramMap.put("updateStudentLearnPlan", false);
						break;
					}
				}
				if(isLearnPlanhas==false){//学习计划中没有需要预约操作的课程
					
					StudentLearnPlan newLearnPlan = new StudentLearnPlan();
					newLearnPlan.setStudentInfo(studentInfo);
					newLearnPlan.setStatus(1);
					newLearnPlan.setTeachingPlanCourse(orderPlanCourse);
					newLearnPlan.setYearInfo(setting.getYearInfo());
					newLearnPlan.setTerm(setting.getTerm());
					
					this.saveOrUpdate(newLearnPlan);
					paramMap.put("updateStudentLearnPlan", true);
					
				}else if(isLearnPlanhas==true){
					return paramMap;
				}
				
			}else {
				
				StudentLearnPlan  newStudentLearnPlan = new StudentLearnPlan();
				newStudentLearnPlan.setStudentInfo(studentInfo);
				newStudentLearnPlan.setStatus(1);
				newStudentLearnPlan.setTeachingPlanCourse(orderPlanCourse);
				newStudentLearnPlan.setYearInfo(setting.getYearInfo());
				newStudentLearnPlan.setTerm(setting.getTerm());
				
				this.saveOrUpdate(newStudentLearnPlan);
				paramMap.put("updateStudentLearnPlan", true);
			}
			
		}else {
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			paramMap.put("updateStudentLearnPlan", false);
			String msg ="预约异常,数据不合法或者条件不足!";
			paramMap = setResultMapMsg(paramMap,msg);
		}

		return paramMap;
		
	}
	/**
	 * 预约学习 2 ---------增加教材预约
	 */
//	public Map<String, Object> addCourseBoookOrders(Map<String, Object> paramMap) throws ServiceException {
//		
//		if (null!=paramMap&&null!=paramMap.get("updateStudentLearnPlan")&&(Boolean)paramMap.get("updateStudentLearnPlan")) {
//
//			//获得学生的学籍信息
//			StudentInfo studentInfo 		    = (StudentInfo) paramMap.get("studentInfo");
//			//要进行预约操作的教学计划课程
//			TeachingPlanCourse orderPlanCourse  = (TeachingPlanCourse) paramMap.get("orderCourse");	
//			
//			//年度预约权限设置对象 
//			OrderCourseSetting setting		    = (OrderCourseSetting) paramMap.get("orderCourseSetting");
//			
//			//要进行预约操作的教学计划课程教材
//			//CourseBook courseBook			    = courseBookService.findUniqueByProperty("course.resourceid", orderPlanCourse.getCourse().getResourceid());
//			Set<TeachingPlanCourseBooks> courseBook = orderPlanCourse.getTeachingPlanCourseBooks();
//			
//			
//			
//			for (TeachingPlanCourseBooks tpc: courseBook) {
//				
//				//预约教材明细
//				CourseBookOrders courseBookOrders   = new CourseBookOrders();
//				//年度预约教材统计对象
//				CourseBookOrderStat oldstat 		= courseBookOrderStatService.getCourseBookOrdersByYearInfoAndCourse(setting.getYearInfo().getResourceid(), setting.getTerm(), null==courseBook?"":tpc.getCourseBook().getResourceid());
//				if (oldstat==null&&tpc!=null) {//统计对象为空
//					
//					oldstat = new CourseBookOrderStat();
//
//					oldstat.setCourseBook(tpc.getCourseBook());
//					oldstat.setYearInfo(setting.getYearInfo());
//					oldstat.setTerm(setting.getTerm());
//					oldstat.setOrderNum(1L);
//					
//					courseBookOrders.setCount(1L);
//					courseBookOrders.setFee(tpc.getCourseBook().getPrice());
//					courseBookOrders.setStudentInfo(studentInfo);
//					courseBookOrders.setCourseBookOrderStat(oldstat);
//				
//					oldstat.getOrders().add(courseBookOrders);
//					
//					courseBookOrderStatService.saveOrUpdate(oldstat);	
//					
//					 
//				}else if(oldstat!=null&&tpc!=null){//统计对象不为空
//					
//					Long oldStatOrderNum = oldstat.getOrderNum();
//					
//					courseBookOrders.setCount(1L);
//					courseBookOrders.setFee(tpc.getCourseBook().getPrice());
//					courseBookOrders.setStudentInfo(studentInfo);
//					courseBookOrders.setCourseBookOrderStat(oldstat);
//					
//					oldstat.setOrderNum(null==oldStatOrderNum?0L:oldStatOrderNum+1);
//					oldstat.getOrders().add(courseBookOrders);
//					
//					courseBookOrderStatService.saveOrUpdate(oldstat);	
//					
//					
//				}else {//没有教材
//					String msg ="没有对应的教材信息，请与教务管理人员联系！";
//					paramMap   = setResultMapMsg(paramMap,msg);
//					
//				}
//			}
//			paramMap.put("addCourseBoookOrders", true);
//			
//		}else {
//			if (null==paramMap)paramMap = new HashMap<String, Object>();
//			paramMap.put("addCourseBoookOrders", false);
//			String msg ="数据不合法或者更新学习计划异常!";
//			paramMap = setResultMapMsg(paramMap,msg);
//			
//		}	
//			
//		return paramMap;
//	}
	

	/**
	 * 预约学习 3 ---------更新预约统计及明细表
	 */
	public Map<String, Object> updateCourseOrderAndCourseOrderStat(Map<String, Object> paramMap) throws ServiceException {
		
		if (null!=paramMap&&null!=paramMap.get("updateStudentLearnPlan")&&(Boolean)paramMap.get("updateStudentLearnPlan")) {
			
			//获得学生的学籍信息
			StudentInfo studentInfo        = (StudentInfo) paramMap.get("studentInfo");
			//教学计划
			TeachingPlan teachingPlan      = (TeachingPlan) paramMap.get("teachingPlan");
			//要进行预约操作的教学计划课程
			TeachingPlanCourse orderCourse = (TeachingPlanCourse) paramMap.get("orderCourse");	
			//年度预约权限设置对象 
			OrderCourseSetting setting     = (OrderCourseSetting) paramMap.get("orderCourseSetting");
			Date curDate                   =  new Date();
			if (null!=setting) {
			
				//学生查出预约统计对象
				CourseOrderStat oldCourseOrderStat = 
				courseorderstatservice.findCourseOrderStatByYearInfoAndCourse( setting.getYearInfo().getResourceid(),
				                                                                orderCourse.getCourse().getResourceid(),setting.getTerm());
				if (null!=oldCourseOrderStat) {
					//学生查出预约统计明细对象
					CourseOrder oldCourseOrder =
					courseOrderService.getCourseOrderByStuIdAndCourseId(new String []{studentInfo.getResourceid(),orderCourse.getCourse().getResourceid(),oldCourseOrderStat.getResourceid()});
				
					if (null==oldCourseOrder) {
						
						oldCourseOrder= new CourseOrder();
						oldCourseOrder.setStudentInfo(studentInfo);
						oldCourseOrder.setStatus(1L);
						oldCourseOrder.setShowOrder(0);
						oldCourseOrder.setCourseOrderStat(oldCourseOrderStat);
						oldCourseOrder.setOrderCourseTime(curDate);
						oldCourseOrder.setOrderCourseTerm(setting.getTerm());
						oldCourseOrder.setOrderCourseYear(setting.getYearInfo());
						
						oldCourseOrderStat.getCourseOrders().add(oldCourseOrder);
						
					}else {
						
						oldCourseOrder.setStatus(1L);
						oldCourseOrder.setOrderCourseTime(curDate);
						oldCourseOrder.setOrderCourseTerm(setting.getTerm());
						oldCourseOrder.setOrderCourseYear(setting.getYearInfo());
						
						courseOrderService.saveOrUpdate(oldCourseOrder);
					}
					
					oldCourseOrderStat.setOrderNum(oldCourseOrderStat.getOrderNum()+1);
					courseorderstatservice.saveOrUpdate(oldCourseOrderStat);
					
				}else {
					CourseOrderStat newcourseOrderStat = new CourseOrderStat();
					
					newcourseOrderStat.setCourse(orderCourse.getCourse());
					newcourseOrderStat.setYearInfo(setting.getYearInfo());
					newcourseOrderStat.setTerm(setting.getTerm());
					newcourseOrderStat.setOrderNum(1);
					
					CourseOrder newCourseOrder = new CourseOrder();
					newCourseOrder.setStudentInfo(studentInfo);
					newCourseOrder.setStatus(1L);
					newCourseOrder.setShowOrder(0);
					newCourseOrder.setCourseOrderStat(newcourseOrderStat);
					newCourseOrder.setOrderCourseTime(curDate);
					newCourseOrder.setOrderCourseTerm(setting.getTerm());
					newCourseOrder.setOrderCourseYear(setting.getYearInfo());
					
					newcourseOrderStat.getCourseOrders().add(newCourseOrder);
					
					courseorderstatservice.save(newcourseOrderStat);
				}
				
				paramMap.put("updateCourseOrderAndCourseOrderStatSuccess", true);
				
			}else {
				String msg ="当前年度预约未开放!";
				paramMap = setResultMapMsg(paramMap,msg);
				paramMap.put("updateCourseOrderAndCourseOrderStatSuccess", false);
			}	
			
		}else {
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			String msg ="预约异常,数据不合法或者不允许预约!";
			paramMap = setResultMapMsg(paramMap,msg);
			paramMap.put("updateCourseOrderAndCourseOrderStatSuccess", false);	
		}

		return paramMap;
	}
	
/**-------------------------------------------------------------------------预约考试---------------------------------------------------------------------------------------------------------------**/
	
	
	/**
	 * 预约考试-检查是否允许预约考试
	 */
	@Override
	public Map<String, Object> isAllowOrderExam(Map<String, Object> paramMap) throws ServiceException {
		try {
			Date curDate 			 = new Date();
			//获取当前年度的考试批次预约对象
			List<ExamSub> examSubList= examSubService.findOpenedExamSub("exam");
			StudentInfo studentInfo  = (StudentInfo)paramMap.get("studentInfo");
			//要进行预约操作的教学计划课程
			TeachingPlanCourse tpc   = (TeachingPlanCourse) paramMap.get("orderCourse");
			boolean isAllowStatus    =  false;
			//学籍状态
			String studentstatus     = null==studentInfo.getStudentStatus()?"":studentInfo.getStudentStatus();
			
			//允许在学及延期的学生预约
			if ("11".equals(studentstatus)||"21".equals(studentstatus)||"25".equals(studentstatus)) {
				isAllowStatus        = true;
			}
			if (isAllowStatus==false) {
				String msg           = "系统只允许在学、延期状态的学员预约，您当前的学籍状态为："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", studentstatus);
				paramMap             = setResultMapMsg(paramMap,msg);
				 throw new Exception("不合法的预约考试,当前学籍为不允许预约的学籍!");
			}
			//个人的预约状态
			boolean examOrderStatus  = 1 ==studentInfo.getExamOrderStatus()? true:false;
			if (examOrderStatus==false) {
				String msg           = "你的预约考试权限未开放，请联系管理人员！";
				paramMap             = setResultMapMsg(paramMap,msg);
				 throw new Exception("预约条件检查不通过不");
			}
			//网络+面授的课程不需要预约考试
			if (ExStringUtils.isNotBlank(tpc.getTeachType())&&"netsidestudy".equals(tpc.getTeachType())) {
				String msg           = "网络+面授类课程不需要预约考试!";
				paramMap             = setResultMapMsg(paramMap,msg);
				 throw new Exception(msg);
			}
			//没有当前年度的考试批次预约记录
			if (null==examSubList || examSubList.isEmpty()) {
				
				paramMap.put("isAllowOrderExam",false);
				String msg           = "未设置年度考试预约批次!";
				paramMap=setResultMapMsg(paramMap,msg);
				
				
			//有当前年度的考试预约批次记录	
			}else {
				
				ExamSub examSub      = examSubList.get(0);
				paramMap.put("examSub", examSub);
				//批次是否开放
				boolean isOpen       = null==examSub.getExamsubStatus()?false: "2".equals(examSub.getExamsubStatus());
				boolean isConFlict   = true;
				
				//考试形式 (大作业)
				Long examType 		= null==tpc.getCourse().getExamType()?0L:tpc.getCourse().getExamType();
				//是否统考课
				String isUniteExam  = ExStringUtils.defaultIfEmpty(tpc.getCourse().getIsUniteExam(), "");
				//是否学位统考课
				String isDegreeUnitExam = ExStringUtils.defaultIfEmpty(tpc.getCourse().getIsDegreeUnitExam(),"");
				//课程类型 (毕业论文,实践环节)
				String courseType 	= ExStringUtils.defaultIfEmpty(tpc.getCourseType(), "");
				
				//当预约的课程为考试形式为大作业,或课程类型为毕业论文、实践环节，统考的课程就不用检查考试冲突 
				if (2==examType ||  							     //考试形式为口试     
					3==examType ||                				 	 //考试形式为大作业       
				    5==examType ||               				 	 //考试形式为统考 
					"66".equals(courseType) || 	  				 	 //教学计划课程类型:实践环节
					"thesis".equals(courseType) ||				 	 //教学计划课程类型:毕业论文  
					Constants.BOOLEAN_YES.equals(isUniteExam)) { 	 //课程类型为统考课
					//Constants.BOOLEAN_YES.equals(isDegreeUnitExam)) {//课程类型为学位统考课
					
					isConFlict = false;
				}else{
					paramMap  			 = examTimeConFlict(paramMap);
					isConFlict           = null==paramMap.get("isConFlict")?true:(Boolean)paramMap.get("isConFlict");
				}
				
				//预约开始时间
				Date startTime       = examSub.getStartTime();
				//预约结束时间
				Date endTime         = examSub.getEndTime();
				
				//添加预约信息截止时间
				Date examsubEndTime  = examSub.getExamsubEndTime();
				//补预约权限记录
				List<ReOrderSetting> reOrderSet  = null;
				
				if (isOpen&&isConFlict==false) {//当前年度考试预约批次开放
					
					//学生自主预约,要求在年度预约时限范围内完成操作
					if("student".equals(paramMap.get("operater").toString())){
						
						if(null==startTime || null==endTime){
							
							paramMap.put("isAllowOrderExam",false);
							String msg = "未设置预约的开始时间或结束时间";
							paramMap=setResultMapMsg(paramMap,msg);
							
					    }else if ((curDate.getTime() >= startTime.getTime()) && (curDate.getTime() <= endTime.getTime()) ) {//当前时间在考试预约批次范围内
					    	
					    	paramMap.put("isAllowOrderExam",true);
						}else {
							paramMap.put("branchSchool", studentInfo.getBranchSchool().getResourceid());
							paramMap.put("reOrderType", "2");
							paramMap.put("examSubId", examSub.getResourceid());
							
							reOrderSet       = reOrderSettingService.findReOrderSettingByCondition(paramMap);
							paramMap.remove("branchSchool");
							paramMap.remove("reOrderType");
							paramMap.remove("examSubId");
							//补预约时间
							if (null!=reOrderSet&&reOrderSet.size()>0&&
									curDate.getTime()>=reOrderSet.get(0).getStartTime().getTime()&&
									curDate.getTime()<=reOrderSet.get(0).getEndTime().getTime()) {
								
								paramMap.put("isAllowOrderExam",true);
							}else {
								paramMap.put("isAllowOrderExam",false);
								String msg = "预约时间为:"+DateUtils.getFormatDate(startTime, "yyyy-MM-dd HH:mm:ss")+"-"+DateUtils.getFormatDate(endTime,"yyyy-MM-dd HH:mm:ss");
								paramMap=setResultMapMsg(paramMap,msg);
							}
						}
					
					// 错过预约时限，老师帮学生预约 前提是不能超过 添加预约信息截止时间
					}else if ("teacher".equals(paramMap.get("operater").toString())) {
						
						if (null==examsubEndTime) {
							paramMap.put("isAllowOrderExam",false);
							String msg = "没有设置添加预约信息截止时间!";
							paramMap=setResultMapMsg(paramMap,msg);
						}else if (curDate.getTime()<=examsubEndTime.getTime()) {
							paramMap.put("isAllowOrderExam",true);
						}else {
							paramMap.put("isAllowOrderExam",false);
							String msg = "已超过添加预约信息截止时间,不允许预约!";
							paramMap=setResultMapMsg(paramMap,msg);
						}
					}
					
				}else {//当前年度考试预约批次不开放
					if (isOpen==false) {
						String msg = "当前年度考试预约批次未开放!";
						paramMap=setResultMapMsg(paramMap,msg);
					}
					paramMap.put("isAllowOrderExam",false);
				}
			}
		} catch (Exception e) {
			paramMap.put("isAllowOrderExam",false);
			String msg = "检查是否允许预约考试操作出错:"+e.getMessage();
			paramMap = setResultMapMsg(paramMap, msg);
			
			logger.error("预约考试-检查是否允许预约考试异常：{}",e.fillInStackTrace());
		}
		return paramMap;
	}
	/**
	 * 预约考试-考试时间检查
	 */
	public Map<String, Object> examTimeConFlict(Map<String,Object> paramMap){
		//学生的学习计划
		List<StudentLearnPlan> learnPlan  = (List<StudentLearnPlan>) paramMap.get("learnPlan");
		//要进行预约操作的教学计划课程
		TeachingPlanCourse orderCourse    = (TeachingPlanCourse) paramMap.get("orderCourse");
		//考试批次中的各科目的考试时间
		Map<String,Date> examDateMap      = new HashMap<String, Date>();
		//当前年份的预约考试批次
		ExamSub examSub 			      = (ExamSub) paramMap.get("examSub");
		//预约考试批次的年度ID
		String examSubYearId              = examSub.getYearInfo().getResourceid();
		//预约考试批次的学期
		String examSubTerm                = examSub.getTerm();
		//要进行预约操作的课程考试时间
		Date orderCourseExamStartTime 	  = null;
		//是否考试时间冲突
		boolean isConFlict                = false;
		boolean isNeedChooseCourse        = false;
		try {
			//获取最新的考试时间
			for (ExamInfo info :examSub.getExamInfo()) {
				//非机考考试课程
				if (!Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(info.getIsMachineExam()))) {
					//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约考试只考试网络教育考试课程类型
					if (info.getCourse().getResourceid().equals(orderCourse.getCourse().getResourceid())&&
						null!=info.getExamCourseType()&&0==info.getExamCourseType()) {
						orderCourseExamStartTime=info.getExamStartTime();
					}else {
						examDateMap.put(info.getCourse().getResourceid(), info.getExamStartTime());
					}
				}
			}
			if (null==orderCourseExamStartTime) {
				throw new ServiceException("你所预约的考试科目在当前考试批次中未按排考试时间！");
			}
			//检查考试时间冲突
			for (StudentLearnPlan plan : learnPlan) {
				String courseId           = null==plan.getTeachingPlanCourse()?plan.getPlanoutCourse().getResourceid():plan.getTeachingPlanCourse().getCourse().getResourceid();
				Date lp_examStartDate     = examDateMap.get(courseId);    
				String orderExamYear      = null==plan.getOrderExamYear()?"":plan.getOrderExamYear().getResourceid();
				String orderExamTerm      = ExStringUtils.defaultIfEmpty(plan.getOrderExamTerm(), "");
				if (null != lp_examStartDate&&plan.getStatus()==2&&                          //学习计划状态为预约考试  
					orderExamYear.equals(examSubYearId)&&orderExamTerm.equals(examSubTerm)&& //学习计划的预约年度、学期跟考试批次年度、学期相同
					lp_examStartDate.getTime() == orderCourseExamStartTime.getTime()) {      //考试时间一样
					isConFlict            = true;
					isNeedChooseCourse    = true;
					String courseName     = null== plan.getTeachingPlanCourse()?plan.getPlanoutCourse().getCourseName():plan.getTeachingPlanCourse().getCourse().getCourseName();
					String msg 			  = "考试时间冲突:</br><font color='red'>"+courseName+"</font>-"+ExDateUtils.formatDateStr(lp_examStartDate, ExDateUtils.PATTREN_DATE_TIME)
							   			  + "</br><font color='red'>"+orderCourse.getCourse().getCourseName()+"</font>-"+ExDateUtils.formatDateStr(orderCourseExamStartTime, ExDateUtils.PATTREN_DATE_TIME);
					paramMap=setResultMapMsg(paramMap,msg);
					
					break;
				}
			}

		} catch (Exception e) {
			logger.error("预约考试-考试时间检查出错:{}"+e.fillInStackTrace());
			String msg = "检查考试时间冲突出错:"+e.getMessage();
			isConFlict = true;
			paramMap=setResultMapMsg(paramMap,msg);
		}
		paramMap.put("isNeedChooseCourse", isNeedChooseCourse);
		paramMap.put("isConFlict", isConFlict);
		return paramMap;
	}
	
	/**
	 * 预约考试-条件满足时的增加或更新操作
	 */
	@Override
	public Map<String, Object> allowOrderExamOperate(Map<String, Object> paramMap) throws ServiceException {
			
		if (null!=paramMap&&null!=paramMap.get("isAllowOrderExam")&&(Boolean)paramMap.get("isAllowOrderExam")) {
			
			//1.添加一个学生预约考试记录
			
			paramMap = orderExamAddExamResults(paramMap);
			boolean isAddExamResultSuccess =null==paramMap.get("isAddExamResultSuccess")?false:(Boolean)paramMap.get("isAddExamResultSuccess");
			
			//2.修改学习计划中的课程状态
			if (isAddExamResultSuccess){
				paramMap =orderExamUpdateLearnPlan(paramMap);
			}else {
				throw new ServiceException("预约考试-添加一个学生预约考试记录操作中断,数据不合法或缺少数据！");
			}
			
			boolean isUpdateLearnPlanSuccess = null==paramMap.get("isUpdateLearnPlanSuccess")?false:(Boolean)paramMap.get("isUpdateLearnPlanSuccess");
			
			//3.修改学生预约课程状态
			if (isUpdateLearnPlanSuccess) {
				paramMap = orderExamUpdateCourseOrder(paramMap);
			}else {
				throw new ServiceException("预约考试-修改学习计划中的课程状态操作中断,数据不合法或缺少数据！");
			}
			
			boolean isUpdateCourseOrderSuccess = null==paramMap.get("isUpdateCourseOrderSuccess")?false:(Boolean)paramMap.get("isUpdateCourseOrderSuccess");
			if (!isUpdateCourseOrderSuccess) {
				throw new ServiceException("预约考试-修改学生预约课程状态操作中断,数据不合法或缺少数据！");
			}

			//4.返回理结果
			if (isUpdateLearnPlanSuccess&&isUpdateCourseOrderSuccess&&isAddExamResultSuccess) {
				paramMap.put("orderExamOperateStatus", true);
			}else {
				paramMap.put("orderExamOperateStatus", false);
				throw new ServiceException("预约考试-操作中断,数据不合法或缺少数据！");
			}
			
		}else {
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			paramMap.put("orderExamOperateStatus", false);
			String msg ="预约考试-操作中断,数据不合法或缺少数据！";
			paramMap = setResultMapMsg(paramMap, msg);
			
			throw new ServiceException("预约考试-操作中断,数据不合法或缺少数据！");
		}

		return paramMap;
	}
	/**
	 * 预约考试1--------添加一个学生预约考试
	 */
	public Map<String, Object> orderExamAddExamResults(Map<String, Object> paramMap) throws ServiceException {
	
		if (null!=paramMap&&null!=paramMap.get("isAllowOrderExam")&&(Boolean)paramMap.get("isAllowOrderExam")) {
			//获得学生的学籍信息
			StudentInfo studentInfo        = (StudentInfo) paramMap.get("studentInfo");
			//要进行预约操作的教学计划课程
			TeachingPlanCourse orderCourse = (TeachingPlanCourse) paramMap.get("orderCourse");
			//当前年份的预约考试批次
			ExamSub examSub 			   = (ExamSub) paramMap.get("examSub");
			
			if (null!=examSub&&null!=examSub.getExamInfo()&&!examSub.getExamInfo().isEmpty()) {
				
				Set<ExamInfo> set          = examSub.getExamInfo();
				Iterator<ExamInfo> it      = set.iterator();
				
				//要进行预约操作的考试信息
				ExamInfo examInfo          = null;
				while (it.hasNext()) {
					ExamInfo examSubsExamInfo = it.next();//考试预约批次中的考试信息对象
					//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约考试只考试网络教育考试课程类型
					if (examSubsExamInfo.getCourse().getResourceid().equals(orderCourse.getCourse().getResourceid())&&null!=examSubsExamInfo.getExamCourseType()&&0==examSubsExamInfo.getExamCourseType()) {
						examInfo           = examSubsExamInfo;
						break;
					}
				}	
				if (null!=examInfo) {
					
					List<ExamResults> list = examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo", studentInfo),Restrictions.eq("examInfo", examInfo));
					
					if (list.size()>0) {
						paramMap.put("isAddExamResultSuccess",false);
						String msg =orderCourse.getCourse().getCourseName()+ "你已经预约了本课程，不需要再预约！";
						paramMap = setResultMapMsg(paramMap, msg);
					}else {
						ExamResults examResults = new ExamResults();
						examResults.setCourse(orderCourse.getCourse());
						examResults.setMajorCourseId(orderCourse.getResourceid());
						examResults.setStudentInfo(studentInfo);
						examResults.setExamInfo(examInfo);
						examResults.setCheckStatus("-1");
						examResults.setExamAbnormity("0");
						examResults.setExamStartTime(examInfo.getExamStartTime());
						examResults.setExamEndTime(examInfo.getExamEndTime());
						examResults.setExamsubId(examInfo.getExamSub().getResourceid());
						examResults.setCourseType(orderCourse.getCourseType());
						examResults.setPlanCourseTeachType(orderCourse.getTeachType());
						
						examResults.setCreditHour(orderCourse.getCreditHour()!=null?orderCourse.getCreditHour():null);
						examResults.setStydyHour(orderCourse.getStydyHour()!=null?orderCourse.getStydyHour().intValue():null);
						examResults.setCourseScoreType(examInfo.getCourseScoreType());
						
						if (null!=orderCourse.getCourse().getExamType()) {
							examResults.setExamType(Integer.valueOf(orderCourse.getCourse().getExamType().toString()));
						}
						
						examResultsService.saveOrUpdate(examResults);
						
						paramMap.put("isAddExamResultSuccess",true);
						paramMap.put("examResult", examResults);
					}
					
				}else {
					paramMap.put("isAddExamResultSuccess",false);
					String msg =orderCourse.getCourse().getCourseName()+ "未设置考试时间,暂时不允许预约！";
					paramMap = setResultMapMsg(paramMap, msg);
				}
			}else {
				paramMap.put("isAddExamResultSuccess",false);
				String msg = "当前年度预约批次未开放！";
				paramMap = setResultMapMsg(paramMap, msg);
			}
			
		}else{
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			paramMap.put("isAddExamResultSuccess",false);
			String msg = "数据不合法,或考试预约批次未开放!";
			paramMap= setResultMapMsg(paramMap, msg);
		}

		return paramMap;
	}
	/**
	 * 预约考试2--------修改学习计划
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public Map<String,Object> orderExamUpdateLearnPlan(Map<String, Object> paramMap)throws ServiceException{
		
		if (null!=paramMap&&null!=paramMap.get("isAllowOrderExam")&&(Boolean)paramMap.get("isAllowOrderExam")) {
			//学生的学习计划
			List<StudentLearnPlan> learnPlan  = (List<StudentLearnPlan>) paramMap.get("learnPlan");
			//要进行预约操作的教学计划课程
			TeachingPlanCourse orderCourse    = (TeachingPlanCourse) paramMap.get("orderCourse");
			//关联的成绩对象
			ExamResults rs 				      = (ExamResults) paramMap.get("examResult");
			ExamSub examSub 				  = (ExamSub) paramMap.get("examSub");
			boolean  isUpdateLearnPlanSuccess = false;
			//修改学习计划中的课程学习状态
			if (null!=learnPlan&&!learnPlan.isEmpty()&&null!=orderCourse) {
				
				Set<ExamInfo> examInfoSet     = examSub.getExamInfo();;
				ExamInfo examInfo 			  = null;//要进行预约操作的考试信息
				
				if (examInfoSet!=null) {
					for(ExamInfo info:examInfoSet) {
						//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约考试只考试网络教育考试课程类型
						if (info.getCourse().getResourceid().equals(orderCourse.getCourse().getResourceid())&&null!=info.getExamCourseType()&&0==info.getExamCourseType()) {
							examInfo 		  = info;
							break;
						}
				   }
				}
				if (null!=examInfo) {
					for (StudentLearnPlan oldLearnPlan:learnPlan) {
						if (orderCourse.getResourceid().equals(oldLearnPlan.getTeachingPlanCourse().getResourceid())) {
							
							oldLearnPlan.setStatus(2);
							oldLearnPlan.setExamInfo(examInfo);
							oldLearnPlan.setExamResults(rs);
							oldLearnPlan.setOrderExamYear(examSub.getYearInfo());
							oldLearnPlan.setOrderExamTerm(examSub.getTerm());
							
							this.saveOrUpdate(oldLearnPlan);
							isUpdateLearnPlanSuccess = true;
							break;
						}
					}
				}else {
					isUpdateLearnPlanSuccess = false;
					String msg = "考试计划中没有当前课程的信息，请联系教务管理人员";
					paramMap = setResultMapMsg(paramMap, msg);
				}
				
				paramMap.put("isUpdateLearnPlanSuccess", isUpdateLearnPlanSuccess);
			}else {
				paramMap.put("isUpdateLearnPlanSuccess", false);
				String  msg = "学习计划为空不能预约考试,请联系管理员！";
				paramMap    = setResultMapMsg(paramMap, msg);
			}
		}else {
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			String msg = "数据不合法,或考试预约批次未开放！";
			paramMap   = setResultMapMsg(paramMap, msg);
			paramMap.put("isUpdateLearnPlanSuccess", false);
		}

		return paramMap;
	}
	
	/**
	 * 预约考试3--------修改学生预约课程状态
	 */
	public Map<String, Object> orderExamUpdateCourseOrder(Map<String, Object> paramMap) throws ServiceException {
		
		if (null!=paramMap&&null!=paramMap.get("isAllowOrderExam")&&(Boolean)paramMap.get("isAllowOrderExam")) {
			
			//获得学生的学籍信息
			StudentInfo studentInfo        = (StudentInfo) paramMap.get("studentInfo");
			//要进行预约操作的教学计划课程
			TeachingPlanCourse orderCourse = (TeachingPlanCourse) paramMap.get("orderCourse");
			//修改课程预约中的课程学习状态
			String courseOrderHQL          = getCourseOrderHQL();
			
			ExamSub examSub 			   = (ExamSub) paramMap.get("examSub");
			Date curDate                   = new Date();
			
			List<CourseOrder> courOrders   = 
			courseOrderService.findByHql(courseOrderHQL,new String []{studentInfo.getResourceid(),orderCourse.getCourse().getResourceid(),examSub.getYearInfo().getResourceid(),examSub.getTerm()});
		
			if (null!=courOrders&&!courOrders.isEmpty()) {
				CourseOrder courseOrder    = courOrders.get(0);
				courseOrder.setStatus(2L);
				courseOrder.setOrderExamTime(curDate);
				courseOrder.setOrderExamTerm(examSub.getTerm());
				courseOrder.setOrderExamYear(examSub.getYearInfo());
				
				courseOrderService.saveOrUpdate(courseOrder);
				paramMap.put("isUpdateCourseOrderSuccess",true);
			}else {
				//学生查出预约统计对象
				CourseOrderStat courseOrderStat= 
				courseorderstatservice.findCourseOrderStatByYearInfoAndCourse(examSub.getYearInfo().getResourceid(),
				                                                                orderCourse.getCourse().getResourceid(),examSub.getTerm());

				if (null==courseOrderStat){
					courseOrderStat        = new CourseOrderStat();
					courseOrderStat.setCourse(orderCourse.getCourse());
					courseOrderStat.setYearInfo(examSub.getYearInfo());
					courseOrderStat.setTerm(examSub.getTerm());
					courseOrderStat.setOrderNum(0);
				}
				
				CourseOrder courseOrder    =  new CourseOrder();
				courseOrder.setOrderExamTerm(examSub.getTerm());
				courseOrder.setOrderExamYear(examSub.getYearInfo());
				courseOrder.setOrderExamTime(curDate);
				courseOrder.setStatus(2L);
				courseOrder.setStudentInfo(studentInfo);
				courseOrder.setCourseOrderStat(courseOrderStat);
				
				courseOrderStat.getCourseOrders().add(courseOrder);
				courseOrderStat.setOrderNum(courseOrderStat.getOrderNum()+1);
				
				courseorderstatservice.saveOrUpdate(courseOrderStat);
				paramMap.put("isUpdateCourseOrderSuccess",true);
				/*String msg 				   = "缺少课程预约数据！";
				paramMap  				   = setResultMapMsg(paramMap, msg);
				paramMap.put("isUpdateCourseOrderSuccess",false);*/
			}
			
		}else {
			if (null==paramMap) {
				paramMap = new HashMap<String, Object>();
			}
			String msg = "数据不合法,或考试预约批次未开放！";
			paramMap   = setResultMapMsg(paramMap, msg);
			paramMap.put("isUpdateCourseOrderSuccess",false);
		}

		return paramMap;
	}
	
	/**
	 * 获得一条学生预约课程的HQL
	 */
	private String getCourseOrderHQL(){
		
		StringBuffer hql = new StringBuffer();
		hql.append("from CourseOrder courseorder where  courseorder.isDeleted=0");
		hql.append(" and courseorder.studentInfo.resourceid=?");
		hql.append(" and courseorder.courseOrderStat.course.resourceid=?");
		hql.append(" and courseorder.courseOrderStat.yearInfo.resourceid=?");
		hql.append(" and courseorder.courseOrderStat.term=?");
		hql.append(" order by courseorder.courseOrderStat.yearInfo.firstYear desc");
		
		return hql.toString();
	}
	
	
	
	
	
	
/**-------------------------------------------------------------------------预约第一学期的课程-------------------------------------------------------------------------**/	
	
	
	/**
	 *    预约第一学期的课程及相关操作
	 * 
	 * 1. 将第一学期的课程加入学习计划         		  updateStudentLearnPlanForSetFirstTermCourse(Map<String, Object>)
	 * 2. 预约第一学期课程的教材            			  addCourseBoookOrdersForSetFirstTermCourse(Map<String, Object>)
	 * 3. 修改对应的预约统计及预约统计明细对象 　updateCourseOrderAndCourseOrderStatForSetFirstTermCourse(Map<String, Object>)
	 */
	@Override
	@Transactional
	public Map<String,Object> setFirstTermCourseOperaterForStudentReg(Map<String, Object> paramMap) throws SetFirstTermCourseForStuRegException {
		
		//1.更新学生的学习计划
		paramMap = updateStudentLearnPlanForSetFirstTermCourse(paramMap);
		boolean updateStudentLearnPlanStatus = null==paramMap.get("updateStudentLearnPlanStatus")?false:(Boolean)paramMap.get("updateStudentLearnPlanStatus");
		//2.添加教材预约记录
//		if (updateStudentLearnPlanStatus) {
//			paramMap=addCourseBoookOrdersForSetFirstTermCourse(paramMap);
//		}else {
//			throw new SetFirstTermCourseForStuRegException("预约第一学期的课程,更新学习计划出错！");
//		}
		//boolean addCourseBoookOrdersStatus = null==paramMap.get("addCourseBoookOrdersStatus")?false:(Boolean)paramMap.get("addCourseBoookOrdersStatus");
		boolean addCourseBoookOrdersStatus = true;
		paramMap.put("addCourseBoookOrdersStatus",addCourseBoookOrdersStatus);
		//3.更新预约学习统计
		if (addCourseBoookOrdersStatus) {
			paramMap = updateCourseOrderAndCourseOrderStatForSetFirstTermCourse(paramMap);
		}else {
			throw new SetFirstTermCourseForStuRegException("预约第一学期的课程,预约教材出错！");
		}
		boolean updateCourseOrderAndCourseOrderStatus = null==paramMap.get("updateCourseOrderAndCourseOrderStatus")?false:(Boolean)paramMap.get("updateCourseOrderAndCourseOrderStatus");
		if (!updateCourseOrderAndCourseOrderStatus) {
			throw new SetFirstTermCourseForStuRegException("预约第一学期的课程,更新预约统计及预约明细出错！");
		}
		//4.当步骤1-3都成功执行时更新学生是否预约第一学期课程标识
		if (updateStudentLearnPlanStatus&&addCourseBoookOrdersStatus&&updateCourseOrderAndCourseOrderStatus) {
			
			StudentInfo studentInfo   =null==paramMap.get("studentInfo")?null:(StudentInfo)paramMap.get("studentInfo");//学生对象
			try {
				studentInfo.setIsOrderFirstCourse(Constants.BOOLEAN_YES);
				studentInfoService.update(studentInfo);
				paramMap.put("setFirstTermCourseStatus", true);
			} catch (Exception e) {
				paramMap.put("setFirstTermCourseStatus", false);
				throw new SetFirstTermCourseForStuRegException("预约第一学期的课程,更新学生第一学期课程预约状态出错！");
			}
		}else {
			paramMap.put("setFirstTermCourseStatus", false);
			throw new SetFirstTermCourseForStuRegException("预约第一学期的课程,更新预约统计及预约明细出错！");
			
		}
		//5.返回结果
		return paramMap;
	}
	
	/**
	 *    预约第一学期的课程1---------将课程加入学习计划
	 * @param paramMap
	 * @return
	 */
	public Map<String,Object>  updateStudentLearnPlanForSetFirstTermCourse(Map<String,Object>  paramMap ){
		
		
		try {
				StudentInfo studentInfo   			= null==paramMap.get("studentInfo")?null:(StudentInfo)paramMap.get("studentInfo");//学生对象
				TeachingGuidePlan teachingGuidePlan = null==paramMap.get("teachingGuidePlan")?null:(TeachingGuidePlan) paramMap.get("teachingGuidePlan");//教学计划
				TeachingPlan teachingPlan           = teachingGuidePlan.getTeachingPlan();
				//当教学计划不为空时才进行加入操作
				if (null!=teachingPlan&&null!=teachingPlan.getTeachingPlanCourses()
								&&!teachingPlan.getTeachingPlanCourses().isEmpty()) {
					
					Set<TeachingPlanCourse> teachingPlanCourSet     = teachingPlan.getTeachingPlanCourses();//教学计划中的课程
					Iterator<TeachingPlanCourse> teachingPlanCourIt = teachingPlanCourSet.iterator();        //教学计划中的课程遍历Iterator
					
			
					//成功加入学习计划的教学计划课程
					List<TeachingPlanCourse> teachingPlanCourseOrderList = new ArrayList<TeachingPlanCourse>();
					while (teachingPlanCourIt.hasNext()) {
						
						TeachingPlanCourse teachingPlanCourse = teachingPlanCourIt.next();
						String term = null==teachingPlanCourse?"":teachingPlanCourse.getTerm();
						
						if ("1".equals(term)) {

							StudentLearnPlan newLearnPlan = new StudentLearnPlan();
							newLearnPlan.setStudentInfo(studentInfo);
							newLearnPlan.setStatus(1);
							newLearnPlan.setTeachingPlanCourse(teachingPlanCourse);
							newLearnPlan.setYearInfo(teachingGuidePlan.getGrade().getYearInfo());
							newLearnPlan.setTerm(teachingGuidePlan.getGrade().getTerm());
							
							this.save(newLearnPlan);
								
							teachingPlanCourseOrderList.add(teachingPlanCourse);
						}
					}
					if (!teachingPlanCourseOrderList.isEmpty()) {
						paramMap.put("updateStudentLearnPlanStatus",true);	
						paramMap.put("teachingPlanCourseOrderList", teachingPlanCourseOrderList);
					}else {
						String msg = "教学计划中没有第一学期的课程,请与教务管理人员联系!";
						paramMap = setResultMapMsg(paramMap, msg);
						paramMap.put("updateStudentLearnPlanStatus",false);	
					}
				}else {
					String msg = "没有对应的教学计划，请与教务管理人员联系！";
					paramMap = setResultMapMsg(paramMap, msg);
					paramMap.put("updateStudentLearnPlanStatus",false);
				}
		} catch (Exception e) {
			paramMap.put("updateStudentLearnPlanStatus",false);
			String msg ="将第一学期的课程加入学习计划出错,请联系管理员！"+e.fillInStackTrace();
			paramMap = setResultMapMsg(paramMap, msg);
			logger.error("注册学籍时将第一学期的课程加入学习计划出错{}",e.fillInStackTrace());
		}
		
		return paramMap;
	}
	
	
	/**
	 * 预约第一学期课程2---------预约教材
	 * @param paramMap
	 * @return
	 */
//	public Map<String,Object>  addCourseBoookOrdersForSetFirstTermCourse(Map<String,Object> paramMap){
//		
//		try {
//			if (null!=paramMap&&null!=paramMap.get("updateStudentLearnPlanStatus")&&(Boolean)paramMap.get("updateStudentLearnPlanStatus")) {
//
//				//获得学生的学籍信息
//				StudentInfo studentInfo = null== paramMap.get("studentInfo")?null:(StudentInfo)paramMap.get("studentInfo");
//				//要进行预约操作的教学计划课程
//				List<TeachingPlanCourse> orderPlanCourseList = (List<TeachingPlanCourse>) paramMap.get("teachingPlanCourseOrderList");	
//				
//				if (null!=orderPlanCourseList&&!orderPlanCourseList.isEmpty()) {
//					
//					for(TeachingPlanCourse orderPlanCourse:orderPlanCourseList){
//						//教材						
//						//CourseBook courseBook 			  = courseBookService.findUniqueByProperty("course.resourceid", orderPlanCourse.getCourse().getResourceid());
//						Set<TeachingPlanCourseBooks> courseBook  = orderPlanCourse.getTeachingPlanCourseBooks();
//
//						CourseBookOrders courseBookOrders        = new CourseBookOrders();
//						for (TeachingPlanCourseBooks tpc:courseBook) {
//							//年度预约教材统计对象 
//							CourseBookOrderStat oldstat 	  = courseBookOrderStatService.getCourseBookOrdersByYearInfoAndCourse(studentInfo.getGrade().getYearInfo().getResourceid(), studentInfo.getGrade().getTerm(), null==courseBook?"":tpc.getCourseBook().getResourceid());
//							if (oldstat==null&&courseBook!=null) {//统计对象为空
//								
//								oldstat = new CourseBookOrderStat();
//
//								oldstat.setCourseBook(tpc.getCourseBook());
//								oldstat.setYearInfo(studentInfo.getGrade().getYearInfo());
//								oldstat.setTerm(studentInfo.getGrade().getTerm());
//								
//								courseBookOrders.setCount(1L);
//								courseBookOrders.setFee(tpc.getCourseBook().getPrice());
//								courseBookOrders.setStudentInfo(studentInfo);
//								courseBookOrders.setCourseBookOrderStat(oldstat);
//							
//								oldstat.getOrders().add(courseBookOrders);
//								
//								courseBookOrderStatService.saveOrUpdate(oldstat);	
//								 
//							}else if(oldstat!=null&&courseBook!=null){//统计对象不为空
//								
//								Long oldStatOrderNum = oldstat.getOrderNum();
//								if (null==oldStatOrderNum) oldStatOrderNum= 1L;
//								
//								courseBookOrders.setCount(1L);
//								courseBookOrders.setFee(tpc.getCourseBook().getPrice());
//								courseBookOrders.setStudentInfo(studentInfo);
//								courseBookOrders.setCourseBookOrderStat(oldstat);
//								
//								oldstat.setOrderNum(oldStatOrderNum+1);
//								oldstat.getOrders().add(courseBookOrders);
//								
//								courseBookOrderStatService.saveOrUpdate(oldstat);	
//								
//							}else {//没有教材
//								String msg ="没有对应的教材信息，请与教务管理人员联系！";
//								paramMap = setResultMapMsg(paramMap,msg);
//							
//							}
//						}
//					}
//					
//					paramMap.put("addCourseBoookOrdersStatus", true);
//				}else{
//					paramMap.put("addCourseBoookOrdersStatus", false);
//				}
//				
//			}else {
//				if (null==paramMap)paramMap = new HashMap<String, Object>();
//				paramMap.put("addCourseBoookOrdersStatus", false);
//			}	
//		} catch (Exception e) {
//			String msg ="预约第一学期课程的教材出错！";
//			paramMap = setResultMapMsg(paramMap, msg);
//			paramMap.put("addCourseBoookOrdersStatus", false);
//			logger.error("预约第一学期课程的教材出错！{}",e.fillInStackTrace());
//		}
//		return paramMap;
//	}
	
	
	/**
	 * 预约第一学期课程3---------修改对应的预约统计及预约统计明细对象
	 * @param paramMap
	 */
	public Map<String,Object> updateCourseOrderAndCourseOrderStatForSetFirstTermCourse (Map<String,Object> paramMap){
			
		try {
			if (null!=paramMap&&null!=paramMap.get("addCourseBoookOrdersStatus")&&(Boolean)paramMap.get("addCourseBoookOrdersStatus")) {
				//学生学籍
				StudentInfo studentInfo = null== paramMap.get("studentInfo")?null:(StudentInfo)paramMap.get("studentInfo");
				//要进行预约操作的教学计划课程
				List<TeachingPlanCourse> orderPlanCourseList = (List<TeachingPlanCourse>) paramMap.get("teachingPlanCourseOrderList");	
				//要进行预约操作的学生ID
				String stuId = studentInfo.getGrade().getYearInfo().getResourceid();
				String term  = studentInfo.getGrade().getTerm();
				Date curDate = new Date();
				if(null!=orderPlanCourseList&&!orderPlanCourseList.isEmpty()){
					
					for (TeachingPlanCourse teachingPlanCourse :orderPlanCourseList) {
						
							String courseID=teachingPlanCourse.getCourse().getResourceid();
							//学生查出预约统计对象
							CourseOrderStat oldCourseOrderStat = 
							courseorderstatservice.findCourseOrderStatByYearInfoAndCourse(stuId,courseID,term);
							
							if (null!=oldCourseOrderStat) {//要进行操作的课程的预约统计对象不为空
								
								//学生查出预约统计明细对象
								CourseOrder oldCourseOrder =
								courseOrderService.getCourseOrderByStuIdAndCourseId(new String []{studentInfo.getResourceid(),teachingPlanCourse.getCourse().getResourceid(),oldCourseOrderStat.getResourceid()});
							
								if (null==oldCourseOrder) {
									oldCourseOrder= new CourseOrder();
									oldCourseOrder.setStudentInfo(studentInfo);
									oldCourseOrder.setStatus(1L);
									oldCourseOrder.setShowOrder(0);
									oldCourseOrder.setCourseOrderStat(oldCourseOrderStat);
									oldCourseOrder.setOrderCourseTime(curDate);
									oldCourseOrder.setOrderCourseTerm(term);
									oldCourseOrder.setOrderCourseYear(studentInfo.getGrade().getYearInfo());
									
									oldCourseOrderStat.getCourseOrders().add(oldCourseOrder);
									
								}else {
									oldCourseOrder.setStatus(1L);
									courseOrderService.saveOrUpdate(oldCourseOrder);
								}
								
								oldCourseOrderStat.setOrderNum(oldCourseOrderStat.getOrderNum()+1);
								courseorderstatservice.saveOrUpdate(oldCourseOrderStat);
								
							}else {//要进行操作的课程的预约统计对象为空
								
								CourseOrderStat newcourseOrderStat = new CourseOrderStat();
								
								newcourseOrderStat.setCourse(teachingPlanCourse.getCourse());
								newcourseOrderStat.setYearInfo(studentInfo.getGrade().getYearInfo());
								newcourseOrderStat.setTerm(term);
								newcourseOrderStat.setOrderNum(1);
								
								CourseOrder newCourseOrder = new CourseOrder();
								newCourseOrder.setStudentInfo(studentInfo);
								newCourseOrder.setStatus(1L);
								newCourseOrder.setShowOrder(0);
								newCourseOrder.setCourseOrderStat(newcourseOrderStat);
								newCourseOrder.setOrderCourseTime(curDate);
								newCourseOrder.setOrderCourseTerm(term);
								newCourseOrder.setOrderCourseYear(studentInfo.getGrade().getYearInfo());
								
								newcourseOrderStat.getCourseOrders().add(newCourseOrder);
								
								courseorderstatservice.save(newcourseOrderStat);
							}	
					}
					paramMap.put("updateCourseOrderAndCourseOrderStatus", true);
				}else {
					paramMap.put("updateCourseOrderAndCourseOrderStatus", false);
				}
				
			}else {
				if (null==paramMap) {
					paramMap = new HashMap<String, Object>();
				}
				paramMap.put("updateCourseOrderAndCourseOrderStatus", false);
			}
			
		} catch (Exception e) {
			paramMap.put("updateCourseOrderAndCourseOrderStatus", false);
			String msg = "修改年度预约统计及预约统计明细出错,请联系管理员！";
			paramMap = setResultMapMsg(paramMap, msg);
			logger.error("注册学籍时预约第一学期-修改年度预约统计及预约统计明细出错{}",e.fillInStackTrace());
		}
		
		return paramMap;
	}

	/**
	 * 根据条件查询学生学习计划
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findStudentLearnPlanByCondition(Map<String, Object> paramMap,Page objePage) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=0");
		hql.append(" and plan.studentInfo.studentStatus in ('11','21','25') and plan.studentInfo.isDeleted=0 ");
		// 已经有成绩的不显示在未安排机考页面
		if(paramMap.containsKey("selType") && "unComputerTest".equals(paramMap.get("selType"))){
//			hql.append(" and not exists(from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0 and f_decrypt_score( rs.integratedScore,rs.studentInfo.resourceid) >= 60 and rs.studentInfo.resourceid=plan.studentInfo.resourceid and rs.course.resourceid=plan.teachingPlanCourse.course.resourceid and rs.checkStatus=4 ) ");//2016-10-10修改
			hql.append(" and not exists(from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0 and rs.studentInfo.resourceid=plan.studentInfo.resourceid and rs.majorCourseId=plan.teachingPlanCourse.resourceid  ) ");//2016-10-10修改
			hql.append(" and not exists(from "+NoExamApply.class.getSimpleName()+" nea where nea.isDeleted=0 and nea.studentInfo.resourceid=plan.studentInfo.resourceid and nea.course.resourceid='"+paramMap.get("courseId")+"' and nea.checkStatus='1' ) ");
		} 
			
		if (paramMap.containsKey("branchSchool")) {
			hql.append(" and plan.studentInfo.branchSchool.resourceid=:branchSchool");
		}
		
//		if (paramMap.containsKey("studyNo"))      
//			hql.append(" and plan.studentInfo.studyNo=:studyNo");
		if (paramMap.containsKey("studyNo"))     //学院2016修改 
		{
			hql.append(" and plan.studentInfo.studyNo like '%"+paramMap.get("studyNo").toString()+"%' ");
		}
		
		if (paramMap.containsKey("leType"))      //学院2016修改
		{
			hql.append(" and plan.studentInfo.teachingType=:leType");
		}
		
		if (paramMap.containsKey("classic")) {
			hql.append(" and plan.studentInfo.classic.resourceid=:classic");
		}
		
		if (paramMap.containsKey("gradeid")) {
			hql.append(" and plan.studentInfo.grade.resourceid=:gradeid");
		}
		
		if (paramMap.containsKey("classes")) {
			hql.append(" and plan.studentInfo.classes.resourceid=:classes");
		}

		//预约学习年度
		if (paramMap.containsKey("yearInfo")) {
			hql.append(" and plan.yearInfo.resourceid=:yearInfo");
		}
		//预约学习学期
		if (paramMap.containsKey("term")) {
			hql.append(" and plan.term=:term");
		}
		
		//预约考试年度
		if (paramMap.containsKey("examOrderYearInfo")) {
			hql.append(" and plan.orderExamYear.resourceid=:examOrderYearInfo");
		}
		//预约考试学期
		if (paramMap.containsKey("examOrderTerm")) {
			hql.append(" and plan.orderExamTerm=:examOrderTerm");
		}
		
		if (paramMap.containsKey("major")) {
			hql.append(" and plan.studentInfo.major.resourceid=:major");
		}
		
		if (paramMap.containsKey("name")) {
			hql.append(" and lower(plan.studentInfo.studentName) like '%"+paramMap.get("name").toString()+"%' ");
		}
		
		if(paramMap.containsKey("shoolTeacherId")){//某个老师授课某门课程所属的所有教学点
			hql.append(" and exists (from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.brSchool.resourceid=plan.studentInfo.branchSchool.resourceid and tpct.teacherId=:shoolTeacherId and tpct.course.resourceid=:courseId )");
		}
		if(paramMap.containsKey("classesTeacherId")){//某个老师授课某门课程所属的所有班级
			hql.append(" and exists (from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.resourceid=plan.studentInfo.classes.resourceid and tpct.teacherId=:classesTeacherId )");
		}
		if(paramMap.containsKey("teacherId")){//某个老师授课某门课程所属的班级
			hql.append(" and exists (from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.resourceid=plan.studentInfo.classes.resourceid and tpct.teacherId=:teacherId and tpct.course.resourceid=:courseId )");
		}
		if (paramMap.containsKey("courseId")) {
			hql.append(" and plan.teachingPlanCourse.course.resourceid=:courseId ");
		}
		
		if(paramMap.containsKey("status")){
			hql.append(" and plan.status=:status ");
		}
		if (paramMap.containsKey("courseName")) {
			hql.append(" and lower(plan.teachingPlanCourse.course.courseName) like '%"+paramMap.get("courseName").toString().toLowerCase()+"%' ");
		}
		
		if (paramMap.containsKey("notStuIds")) {
			hql.append(" and plan.studentInfo.resourceid not in ("+paramMap.get("notStuIds")+") ");
		}
		
		if(paramMap.containsKey("examSubId")){
			hql.append(" and plan.examInfo.examSub.resourceid=:examSubId ");
		}
		if(paramMap.containsKey("examInfoId")){
			hql.append(" and plan.examInfo.resourceid=:examInfoId ");
		}
		if(paramMap.containsKey("examCourseId")){
			hql.append(" and plan.examInfo.course.resourceid=:examCourseId ");
		}
		if(paramMap.containsKey("isRedoCourseExam")){
			hql.append(" and plan.isRedoCourseExam=:isRedoCourseExam ");
		}
		if (paramMap.containsKey("queryHQL")) {
			hql.append(paramMap.get("queryHQL"));
		}
		
		if (paramMap.containsKey("ispaike")) {// 只显示已经排课的
			hql.append(" and exists( from "+TeachingPlanCourseTimetable.class.getSimpleName()+" tct where tct.isDeleted=0 and plan.studentInfo.classes.resourceid=tct.classes.resourceid and plan.teachingPlanCourse.resourceid=tct.teachingPlanCourse.resourceid and tct.term='"+paramMap.get("ispaike")+"' ) ");
		}
		
		if(paramMap.containsKey("teacherId")){//课程老师
			String teacherId = "%"+paramMap.get("teacherId")+"%";
			hql.append(" and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=plan.teachingPlanCourse.course.resourceid ");
			hql.append(" and (t.teacherId like '"+teacherId+"' or  t.assistantIds like '"+teacherId+"' ) and t.yearInfo.resourceid=plan.yearInfo.resourceid and t.term=plan.term ) ");
		}
		if(ExStringUtils.isNotEmpty(objePage.getOrderBy())) {
			hql.append(" order by plan."+objePage.getOrderBy().replace(",", ",plan.") + " "+objePage.getOrder());
		}
		return this.exGeneralHibernateDao.findByHql(objePage, hql.toString(), paramMap);
	}
	
	@Override
	public List<StudentLearnPlan> findStudentLearnPlanByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = getStudentLearnPlanHqlByCondition(condition, values);		
		return  findByHql(hql,values);
	}

	/**
	 * 学习列表查询hql
	 * @param condition
	 * @param values
	 * @return
	 */
	private String getStudentLearnPlanHqlByCondition(Map<String, Object> condition, Map<String, Object> values) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("courseId")){//课程id
			hql.append(" and plan.teachingPlanCourse.course.resourceid=:courseId ");
			values.put("courseId", condition.get("courseId"));
		}	
		if(condition.containsKey("planCourseId")){//教学计划课程id
			hql.append(" and plan.teachingPlanCourse.resourceid=:planCourseId ");
			values.put("planCourseId", condition.get("planCourseId"));
		}
		if(condition.containsKey("branchSchoolId")){//教学点
			hql.append(" and plan.studentInfo.branchSchool.resourceid=:branchSchoolId ");
			values.put("branchSchoolId", condition.get("branchSchoolId"));
		}
		if(condition.containsKey("gradeId")){//年级
			hql.append(" and plan.studentInfo.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("status")){//学习状态
			hql.append(" and plan.status=:status ");
			values.put("status", condition.get("status"));
		}
		if (condition.containsKey("studentId")) {//学生id
			hql.append(" and plan.studentInfo.resourceid =:studentId ");
			values.put("studentId", condition.get("studentId"));
		}
		if (condition.containsKey("notStuIds"))  { //排除学生ids   
			hql.append(" and plan.studentInfo.resourceid not in ("+condition.get("notStuIds")+") ");
		}		
		if (condition.containsKey("yearInfo")) {  //预约年度
			hql.append(" and plan.yearInfo.resourceid=:yearInfo ");
			values.put("yearInfo", condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) { //预约学期
			hql.append(" and plan.term=:term ");
			values.put("term", condition.get("term"));
		}			
		if (condition.containsKey("examOrderYearInfo")) {//预约考试年度
			hql.append(" and plan.orderExamYear.resourceid=:examOrderYearInfo ");
			values.put("examOrderYearInfo", condition.get("examOrderYearInfo"));
		}		
		if (condition.containsKey("examOrderTerm")) {//预约考试学期
			hql.append(" and plan.orderExamTerm=:examOrderTerm ");
			values.put("examOrderTerm", condition.get("examOrderTerm"));
		}		
//		if(condition.containsKey("teacherId")){//课程老师
//			String teacherId = "%"+condition.get("teacherId")+"%";
//			hql.append(" and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=plan.teachingPlanCourse.course.resourceid and t.yearInfo.resourceid=plan.yearInfo.resourceid and t.term=plan.term and (t.teacherId like '"+teacherId+"' or  t.assistantIds like '"+teacherId+"' ) ) ");
//		}
		if (condition.containsKey("examSubId")) {//考试批次
			hql.append(" and plan.examInfo.examSub.resourceid=:examSubId ");
			values.put("examSubId", condition.get("examSubId"));
		}
		if(condition.containsKey("examCourseId")){
			hql.append(" and plan.examInfo.course.resourceid=:examCourseId ");
			values.put("examCourseId", condition.get("examCourseId"));
		}
		if(condition.containsKey("netstudy")){
			hql.append(" and plan.examInfo.examCourseType in (0,3) ");
		}
		if(condition.containsKey("orderBy")){//排序
			hql.append(" order by plan."+condition.get("orderBy").toString().replace(",", ",plan."));
		}
		return hql.toString();
	}
	
	
	
/**-------------------------------------------------------------------------预约毕业论文---------------------------------------------------------------------------------------------------------------**/
	
	
	/**
	 *预约毕业论文---------检查是否允许预约毕业论文
	 * @param paramMap
	 * @return
	 */
	@Override
	public Map<String, Object> isAllowOrderGraduatePaper(Map<String, Object> paramMap) throws ServiceException {
			try{
				Date curDate 			    = new Date();
				
				List<ExamSub> examSubList   = examSubService.findOpenedExamSub("thesis"); //毕业论文批次对象
				StudentInfo studentInfo     = (StudentInfo)paramMap.get("studentInfo");   //学籍对象
				TeachingPlan teachingPlan   = (TeachingPlan)paramMap.get("teachingPlan"); //教学计划对象
				Double applyPaperMinResult  = null==teachingPlan.getApplyPaperMinResult()?0D:teachingPlan.getApplyPaperMinResult();//教学计划对应的申请毕业论文的最低学分
				String teachingType         = null==studentInfo.getTeachingType()?"":studentInfo.getTeachingType();
				boolean isAllowStatus       = false;
				//当前年级
				Grade currentGrade      	= gradeService.findUniqueByProperty("isDefaultGrade", "Y");
				//允许预约的学期
				String allowTerm       	    = StudentCourseOrderUtil.getAllowOrderCourseTerm(currentGrade,studentInfo.getGrade());
				
				//学籍状态
				String studentstatus        = null==studentInfo.getStudentStatus()?"":studentInfo.getStudentStatus();
				Integer graduatePaperStatus = null==studentInfo.getIsAbleOrderSubject()?1:studentInfo.getIsAbleOrderSubject();
				if ("11".equals(studentstatus)||"25".equals(studentstatus)) {
					isAllowStatus           = true;
				}
				if (isAllowStatus==false) {
					
					String msg         = "系统只允许在学状态的学员预约，您当前的学籍状态为："+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", studentstatus);
					paramMap.put("isAllowOrderGraduatePaper",false);
					
					throw new Exception(msg);
				}
				if (graduatePaperStatus !=1) {
					String msg         = "对不起，您被暂停本次论文预约资格，请在下学期预约！";
					paramMap.put("isAllowOrderGraduatePaper",false);
					
					throw new Exception(msg);
				}
				if ("face".equals(teachingType)&&Integer.parseInt(allowTerm)<5) {
					String msg         = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", teachingType)+",论文预约时间在第五学期！";
					paramMap.put("isAllowOrderGraduatePaper",false);
					throw new Exception(msg);
				}
				if (applyPaperMinResult<=0) {
					String msg         = "教学计划:"+teachingPlan.getMajor().getMajorName()+"_"+teachingPlan.getClassic().getClassicName()+"，未设置论文申请最低学分,请与教务人员联系！";
					paramMap.put("isAllowOrderGraduatePaper",false);
					throw new Exception(msg);
				}
				
				//没有开放的毕业论文预约批次
				if (null==examSubList || examSubList.isEmpty()) {
					
					paramMap.put("isAllowOrderGraduatePaper",false);
					String msg          = "没有开放的毕业论文预约批次!";
					paramMap=setResultMapMsg(paramMap,msg);
					
					
				//有开放的毕业论文预约批次
				}else {
					
					ExamSub examSub     = examSubList.get(0);
					paramMap.put("graduatePaperBatch", examSub);
					//批次是否开放
					boolean isOpen      = null==examSub.getExamsubStatus()?false: "2".equals(examSub.getExamsubStatus());
					
					//预约开始时间
					Date startTime      = examSub.getStartTime();
					//预约结束时间
					Date endTime        = examSub.getEndTime();
					
					
					if (isOpen) {//毕业论文预约批次开放

						
						if(null==startTime || null==endTime){
							
							paramMap.put("isAllowOrderGraduatePaper",false);
							String msg = "未设置预约的开始时间或结束时间";
							paramMap=setResultMapMsg(paramMap,msg);
							
					    }else if ((curDate.getTime() >= startTime.getTime()) && (curDate.getTime() <= endTime.getTime()) ) {//当前时间在考试预约批次范围内
					    	StudentExamResultsVo vo = studentExamResultsService.getStudentFinishedCreditHour(studentInfo);
					    	Double studentTotalCredit = vo.getTotalCredit();
					    	if (studentTotalCredit >= applyPaperMinResult) {
					    		paramMap.put("isAllowOrderGraduatePaper",true);
							}else{
								String msg = teachingPlan.getMajor().getMajorName()+"_"+teachingPlan.getClassic().getClassicName()+",毕业论文申请最低学分为："+applyPaperMinResult;
								paramMap=setResultMapMsg(paramMap,msg);
								paramMap.put("isAllowOrderGraduatePaper",false);
							}
						}else {
							paramMap.put("isAllowOrderGraduatePaper",false);
							String msg = "预约时间为:"+DateUtils.getFormatDate(startTime, "yyyy-MM-dd HH:mm:ss")+"-"+DateUtils.getFormatDate(endTime,"yyyy-MM-dd HH:mm:ss");
							paramMap=setResultMapMsg(paramMap,msg);
						}
						
						
					}else {//当前年度考试预约批次不开放
						
						paramMap.put("isAllowOrderGraduatePaper",false);
						String msg = "毕业论文预约未开放!";
						paramMap=setResultMapMsg(paramMap,msg);
					}
				}
		} catch (Exception e) {
			paramMap.put("isAllowOrderGraduatePaper",false);
			String msg = "检查是否允许预约毕业论文操作出错:"+e.getMessage();
			paramMap = setResultMapMsg(paramMap, msg);
			
			logger.error("检查是否允许预约毕业论文操作出错：{}",e.fillInStackTrace());
		}
		return paramMap;
	}
	
	/**
	 *预约毕业论文---------条件满足时的增加或更新操作
	 * @param paramMap
	 * @return
	 */
	@Override
	public Map<String, Object> allowOrderGraduatePaper(Map<String, Object> paramMap) throws ServiceException {
		
		StudentInfo studentInfo             = (StudentInfo)paramMap.get("studentInfo");       //学籍对象
		ExamSub graduatePaperBatch          = (ExamSub)paramMap.get("graduatePaperBatch");    //毕业论文批次
		TeachingPlanCourse orderCourse      = (TeachingPlanCourse)paramMap.get("orderCourse");//毕业论文对应的教学计划课程
		List<StudentLearnPlan> oldLearnPlan = (List<StudentLearnPlan>) paramMap.get("learnPlan");//学生的学习计划
		StudentLearnPlan learnPlan 			= new StudentLearnPlan();

		
		learnPlan.setStudentInfo(studentInfo);
		learnPlan.setStatus(1);
		learnPlan.setTeachingPlanCourse(orderCourse);
		learnPlan.setYearInfo(graduatePaperBatch.getYearInfo());
		learnPlan.setTerm(graduatePaperBatch.getTerm());
		
		if (null!=oldLearnPlan && !oldLearnPlan.isEmpty()) {
			
			for (StudentLearnPlan plan : oldLearnPlan) {
				TeachingPlanCourse learnPlanCourse = plan.getTeachingPlanCourse();
				if (orderCourse.getResourceid().equals(learnPlanCourse.getResourceid())) {//学习计划中已有需要预约操作的课程
					learnPlan = plan;
					learnPlan.setStatus(1);
					learnPlan.setTeachingPlanCourse(orderCourse);
					learnPlan.setYearInfo(graduatePaperBatch.getYearInfo());
					learnPlan.setTerm(graduatePaperBatch.getTerm());
					
					break;
				}
			}
		}
		
		this.saveOrUpdate(learnPlan);
		
		GraduatePapersOrder paperOrder = new GraduatePapersOrder();
		paperOrder.setCourse(orderCourse.getCourse());
		paperOrder.setExamSub(graduatePaperBatch);
		paperOrder.setExamSubId(graduatePaperBatch.getResourceid());
		paperOrder.setOrderTime(ExDateUtils.getToday());
		paperOrder.setStudentId(studentInfo.getResourceid());
		paperOrder.setStatus(0);
		paperOrder.setCurrentTache("1");
		paperOrder.setStudentInfo(studentInfo);
		//2012-06-15,K0528C_毕业论文功能调整:取消毕业论文申请的审核步骤
		paperOrder.setStatus(Constants.BOOLEAN_TRUE);
		
		graduatePapersOrderService.save(paperOrder);
		
		studentInfo.setIsOrderSubject(Constants.BOOLEAN_YES);
		studentInfoService.update(studentInfo);
		
		paramMap.put("orderGraduatePaperOperateStatus", true);
		
		return paramMap;
	}
	
	@Override
	public Integer getOrderCourseNum(String courseId,String yearInfoId,String term) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("courseId", courseId);
		String hql = "select count(distinct plan) from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=0 and plan.teachingPlanCourse.course.resourceid=:courseId ";
		if(ExStringUtils.isNotBlank(yearInfoId)){
			hql += " and plan.yearInfo.resourceid=:yearInfoId ";
			values.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotBlank(term)){
			hql += " and plan.term=:term ";
			values.put("term", term);
		}
		hql += " or plan.isDeleted=0 and plan.teachingPlanCourse.course.resourceid=:courseId and plan.status<3 ";
		Long count = exGeneralHibernateDao.findUnique(hql,values);		
		return count!=null?count.intValue():0;
	}	
	
	@Override
	public Integer getOnlineStudentNum(String courseId, String yearInfo,
			String term, String teacherid,String classids) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("courseId", courseId);
		String hql = " select plan.classes.resourceid from "+TeachingPlanCourseTimetable.class.getSimpleName()+" plan where plan.isDeleted=0 and plan.teachingPlanCourse.course.resourceid=:courseId ";
		String Sterm = yearInfo+"_0"+term;
		if(ExStringUtils.isNotBlank(Sterm)){
			hql += " and plan.term=:Sterm ";
			values.put("Sterm", Sterm);
		}
		if(ExStringUtils.isNotBlank(teacherid)){
			hql += " and plan.teacherId=:teacherid ";
			values.put("teacherid", teacherid);
		}
		hql += " group by plan.classes.resourceid ";
		List<String> te = (List<String>) exGeneralHibernateDao.findByHql(hql,values);
		Long count = 0L;
		if(null != te && te.size() > 0){
			String[] classIdArray = null;
			if(null != classids){
				classIdArray = classids.split(",");
			}
			for(String t : te){
				if(null != classIdArray && classIdArray.length > 0){
					for(String id : classIdArray){
						if(id.equals(t)){
							List<StudentInfo> Lstudentinfo = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.classes.resourceid = ? ", t);
							count += null != Lstudentinfo && Lstudentinfo.size() > 0 ? Lstudentinfo.size() : 0L;
						}
					}
				}else {
					List<StudentInfo> Lstudentinfo = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.classes.resourceid = ? ", t);
					count += null != Lstudentinfo && Lstudentinfo.size() > 0 ? Lstudentinfo.size() : 0L;
				}
			}
		}
		return count!=null?count.intValue():0;
	}
	
	@Override
	public StudentLearnPlan getStudentLearnPlanByCourse(String courseId,String id,String idType) throws ServiceException {		
		String sql = null;
		if("userId".equals(idType)){//学生账户id
			sql = " select t.* from edu_learn_stuplan t join edu_roll_studentinfo s on s.resourceid=t.studentid left join edu_teach_plancourse p on p.resourceid=t.plansourceid where t.isdeleted=0 and s.isdeleted=0 and s.sysuserid=:id and (p.courseid=:courseId or t.planoutcourseid=:courseId) order by decode(p.planid,s.teachplanid,1)";
		} else {//学籍id
			sql = " select t.* from edu_learn_stuplan t left join edu_teach_plancourse p on p.resourceid=t.plansourceid join edu_roll_studentinfo si on si.resourceid=t.studentid where t.isdeleted=0 and t.studentid=:id and (p.courseid=:courseId or t.planoutcourseid=:courseId) order by decode(p.planid,si.teachplanid,1)";
		}
		Session session  = exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		SQLQuery query = session.createSQLQuery(sql).addEntity(StudentLearnPlan.class);
		query.setParameter("courseId",courseId);
		query.setParameter("id",id);
		List<StudentLearnPlan> list = query.list();
		for (StudentLearnPlan studentLearnPlan : list) {
			
		}
		return ExCollectionUtils.isNotEmpty(list)?list.get(0):null;
	}

	/**
	 * 查询默认年级下类型为学籍卡修改开放时间的活动信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TeachingActivityTimeSetting> findTeachingactivitytime(Map<String,Object> condition)throws ServiceException{
		StringBuilder hql 		 = new StringBuilder(" from "+TeachingActivityTimeSetting.class.getSimpleName()+" setting where setting.isDeleted = :isDeleted ");
		hql.append(" and yearInfo.resourceid = :yearid and term = :term and mainProcessType = :acttype ");
		return (List<TeachingActivityTimeSetting>) exGeneralHibernateDao.findByHql(hql.toString(), condition);
	};
	
	/**
	 * 论坛成员
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findStudentBBSgroupLeader(Map<String, Object> paramMap,Page objePage) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
			hql.append("from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted=0 ");
		
		if (paramMap.containsKey("branchSchool")) {
			hql.append(" and stu.branchSchool.resourceid=:branchSchool");
		}
		
		if (paramMap.containsKey("studyNo")) {
			hql.append(" and stu.studyNo=:studyNo");
		}
		
		if (paramMap.containsKey("classic")) {
			hql.append(" and stu.classic.resourceid=:classic");
		}
		
		if (paramMap.containsKey("classesId")) {
			hql.append(" and stu.classes.resourceid=:classesId");
		}

		if (paramMap.containsKey("name")) {
			hql.append(" and lower(stu.studentName) like '%"+paramMap.get("name").toString()+"%' ");
		}
		
		if(paramMap.containsKey("teacherId")) {
			hql.append(" and exists ( from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.teachingPlanCourse.course.resourceid = :courseId and te.isDeleted = 0 and stu.classes.resourceid = te.classes.resourceid and te.teacherId = :teacherId ) ");
		} else {
			hql.append(" and exists ( from "+TeachingPlanCourseTimetable.class.getSimpleName()+" te where te.teachingPlanCourse.course.resourceid = :courseId and te.isDeleted = 0 and stu.classes.resourceid = te.classes.resourceid ) ");
		}
		
		return this.exGeneralHibernateDao.findByHql(objePage, hql.toString(), paramMap);
	}

	/**
	 * 为学生创建学习计划
	 * @param studentInfoList
	 * @param studentIds
	 * @throws Exception
	 */
	@Override
	public void generateStuPlan(List<StudentInfo> studentInfoList, String studentIds,List<UsualResults> urs) throws Exception {
		if(ExStringUtils.isEmpty(studentIds) && studentInfoList!=null && studentInfoList.size()>0){
			List<String> resourceids = new ArrayList<String>();
			for (StudentInfo studentInfo : studentInfoList) {
				resourceids.add(studentInfo.getResourceid());
			}
			studentIds = resourceids.toString();
		}
		List<Map<String, Object>> openCourseList = teachingPlanCourseStatusService.findByStudentInfoIds(studentIds);
		generateStudyPlanImpl(studentInfoList, openCourseList,urs);
	}

	private void generateStudyPlanImpl(List<StudentInfo> studentInfoList,
			List<Map<String, Object>> openCourseList ,List<UsualResults> urs) {
		List<UsualResults> usualResults = new ArrayList<UsualResults>();
		if(ExCollectionUtils.isNotEmpty(openCourseList)){
			Map<String, StudentInfo> studentInfoMap = studentInfoService.transStudentInfoListToMap(studentInfoList);
			List<StudentLearnPlan> studentLearnPlanList = new ArrayList<StudentLearnPlan>();
			for(Map<String, Object> info : openCourseList){
				String studentId = (String)info.get("studentId");
				String planCourseId = (String)info.get("planCourseId");
				String firstYear = (String)info.get("firstYear");
				String term = (String)info.get("term");
				String planId = (String)info.get("spid");
				String erId = (String) info.get("erid");
				if(ExStringUtils.isEmpty(studentId) || ExStringUtils.isEmpty(planCourseId)
						|| ExStringUtils.isEmpty(firstYear) || ExStringUtils.isEmpty(term)){
					continue;
				}
				
				TeachingPlanCourse planCourse = teachingPlanCourseService.get(planCourseId);
				YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(firstYear));
				if(yearInfo == null){
					continue;
				}
				StudentLearnPlan tmpLP = null;
				if(ExStringUtils.isEmpty(planId)){//新建
					// 创建学习计划
					StudentLearnPlan newLearnPlan = new StudentLearnPlan();
					newLearnPlan.setStudentInfo(studentInfoMap.get(studentId));
					newLearnPlan.setStatus(1);
					newLearnPlan.setTeachingPlanCourse(planCourse);
					newLearnPlan.setYearInfo(yearInfo);
					newLearnPlan.setTerm(term);
					newLearnPlan.setOrderExamYear(yearInfo);
					newLearnPlan.setOrderExamTerm(term);
					tmpLP= newLearnPlan;
//					studentLearnPlanList.add(newLearnPlan);
				}else{
					StudentLearnPlan oldLearnPlan = get(planId);
					if(oldLearnPlan!=null && ExStringUtils.isBlank(erId)){
						oldLearnPlan.setYearInfo(yearInfo);
						oldLearnPlan.setTerm(term);
						oldLearnPlan.setOrderExamYear(yearInfo);
						oldLearnPlan.setOrderExamTerm(term);
						tmpLP = oldLearnPlan;
//						studentLearnPlanList.add(oldLearnPlan);
					}
				}
				//TODO  1728 
				if(tmpLP!=null){
					if(urs!=null&&urs.size()>0){
						for(UsualResults us:urs){
							if(us.getCourse().getResourceid().equals(tmpLP.getTeachingPlanCourse().getCourse().getResourceid())){//如果匹配到同一门课程,更改年度、学期、教学计划课程 的信息
								us.setYearInfo(yearInfo);
								us.setTerm(term);
								us.setMajorCourseId(planCourseId);
								tmpLP.setUsualResults(us);
								usualResults.add(us);
								break;
							}
						}
					}
					studentLearnPlanList.add(tmpLP);
				}
			}
			
			// 批量保存
			if(studentLearnPlanList!=null && studentLearnPlanList.size()>0){
				batchSaveOrUpdate(studentLearnPlanList);
			}
			if(usualResults!=null && usualResults.size()>0){
				usualResultsService.batchSaveOrUpdate(usualResults);
			}
		}
	}
	
	
	/**
	 * 根据条件创建学习计划
	 * @param studentInfoList
	 * @param studentId
	 * @param gradeId
	 * @param schoolId
	 * @param teachPlanId
	 * @throws Exception
	 */
	
	@Override
	public void generateStudyPlanByCondition(List<StudentInfo> studentInfoList, String studentId, String gradeId, String schoolId, String teachPlanId, String planCourseId, List<UsualResults> urs) throws Exception {
		List<Map<String, Object>> openCourseList = teachingPlanCourseStatusService.findPlanInfoByCondition(studentId, gradeId, schoolId, teachPlanId,planCourseId);
		generateStudyPlanImpl(studentInfoList, openCourseList,urs);
	}
	
	
}