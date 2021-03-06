package com.hnjk.edu.teaching.controller;


import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.helper.QueryParameter;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IStudentCheckService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.teaching.model.*;
import com.hnjk.edu.teaching.service.*;
import com.hnjk.edu.teaching.service.impl.ExamPrintServiceImpl;
import com.hnjk.edu.teaching.util.CalendarUtil;
import com.hnjk.edu.teaching.util.ExamResultMakeupUtil;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.edu.teaching.util.StudentCourseOrderUtil;
import com.hnjk.edu.teaching.vo.*;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.IUserOperationLogsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

import edu.emory.mathcs.backport.java.util.Arrays;
import jxl.Workbook;
import jxl.write.*;
import lombok.Cleanup;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.Boolean;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 考生成绩Controller
 *
 */

@Controller
public class ExamResultController extends FileUploadAndDownloadSupportController {

	private static Logger logger = LoggerFactory.getLogger(ExamResultController.class);

	private static final long serialVersionUID = 8229026357561678239L;

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;

	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;

	@Autowired
	@Qualifier("examResultsLogService")
	private IExamResultsLogService examResultsLogService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;

	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;

	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;

	@Autowired
	@Qualifier("examResultsAuditService")
	private IExamResultsAuditService examResultsAuditService;

	@Autowired
	@Qualifier("publishedExamResultsAuditService")
	private IPublishedExamResultsAuditService publishedExamResultsAuditService;

	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;

	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;

	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;

	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;

	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;

	@Autowired
	@Qualifier("studentCheckService")
	private IStudentCheckService studentCheckService;

	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;

	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;

	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;

	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;

	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;

	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;


	@Autowired
	@Qualifier("userOperationLogsService")
	private IUserOperationLogsService userOperationLogsService;

	@Autowired
	@Qualifier("userService")
	private IUserService userService;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;

	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;

	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;

	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;

	@Autowired
	@Qualifier("noexamapplyservice")
	INoExamApplyService noexamapplyservice;

	@Autowired
	@Qualifier("universalExamService")
	private IUniversalExamService iUniversalExamService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	/**
	 * 卷面成绩管理-列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/list.html")
	public String examResultManagerList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){

		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		page.setPageSize(50);
		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String teachType             = ExStringUtils.trimToEmpty(request.getParameter("teachType")); //教学类型（统考、非统考）
		String forwordurl 			 = "";

		User user = SpringSecurityHelper.getCurrentUser();
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
			OrgUnit unit = orgUnitService.get(branchSchool);
			model.addAttribute("unit", unit);
		}

		if ("networkstudy".equals(teachType)) {
			forwordurl               = "/edu3/teaching/examResult/examResultManager_list_1";
		}else {
			teachType = "facestudy";
			condition.put("teachType","facestudy");
			forwordurl               = "/edu3/teaching/examResult/examResultManager_list_2";
		}
		ExamSub examSub          = examSubService.get(examSubId);
		model.addAttribute("examSub", examSub);
		condition.put("examSubId", examSubId);
		long starTime = System.currentTimeMillis();
		if ("networkstudy".equals(teachType)) {
			page = examInfoService.findExamInfoForExamResultsInput(condition, page);
			Map<String,Object>  statusMap =queryExamInfoCheckStatus(page,condition);
			model.addAttribute("statusMap",statusMap);
		} else if ("facestudy".equals(teachType)) {
			page = examResultsService.queryExamResultsInfoForFaceTeachType(condition, page);
			//Map<String,Object>  statusMap =queryExamInfoCheckStatus(page,condition);
			//model.addAttribute("statusMap",statusMap);
		}
		logger.info("成绩审核查询页面,耗时："+(System.currentTimeMillis()-starTime)+"ms.");
		model.addAttribute("schoolCode", schoolCode);
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);


		return forwordurl;
	}

	/**
	 * 考试统计
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/statistics.html")
	public String examResultStatistics(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){

		Map<String,Object> condition = new HashMap<String, Object>();

		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examCourseCode 		 = ExStringUtils.trimToEmpty(request.getParameter("examCourseCode"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String teachType             = ExStringUtils.trimToEmpty(request.getParameter("teachType")); //课程的教学方式
		String classId				 = ExStringUtils.trimToEmpty(request.getParameter("classId")); //班级

		if (ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
//			OrgUnit unit = orgUnitService.get(branchSchool);
//			model.addAttribute("unit", unit);
		}
		if (ExStringUtils.isNotEmpty(examSubId)){
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(gradeid)){
			condition.put("gradeid", gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classic)) 	{
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(examCourseCode)){
			condition.put("examCourseCode", examCourseCode);
		}
		if (ExStringUtils.isNotEmpty(teachType)){
			condition.put("teachType", teachType);
		}
		if (ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId", classId);
		}

		page = examResultsService.examResultStatistics(condition, page);
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);

		StringBuffer unitOption = new StringBuffer("<option value=''></option>");
		//List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		List<OrgUnit> orgList = orgUnitService.findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitName asc ");
		if(null != orgList && orgList.size()>0){
			for(OrgUnit orgUnit : orgList){
				if(ExStringUtils.isNotEmpty(branchSchool) && branchSchool.equals(orgUnit.getResourceid())){
					unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}else{
					unitOption.append("<option value='"+orgUnit.getResourceid()+"'");
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}
			}
		}

		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		for (Major m : majors) {
			if(ExStringUtils.isNotEmpty(major) && major.equals(m.getResourceid())){
				majorOption.append("<option value ='"+m.getResourceid()+"' selected='selected' >"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}else{
				majorOption.append("<option value ='"+m.getResourceid()+"'>"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}
		}
		//如果已经选择了学习中心，就要过滤出相应的专业
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("unitId", branchSchool);
		}
		List<Map<String,Object>> teachingPlanMajors = teachingPlanService.getUnitTeachingPlanMajor(condition);
		majorOption = new StringBuffer("<option value=''></option>");
		for (Map<String,Object> m : teachingPlanMajors) {
			if(ExStringUtils.isNotEmpty(major) && major.equals(m.get("resourceid"))){
				majorOption.append("<option value ='"+m.get("resourceid")+"' selected='selected' >"+m.get("majorinfo")+"</option>");
			}else{
				majorOption.append("<option value ='"+m.get("resourceid")+"'>"+m.get("majorinfo")+"</option>");
			}
		}

		model.addAttribute("majorOption", majorOption);
		model.addAttribute("unitOption",unitOption);

		return "/edu3/teaching/examResult/examResultStatistics";
	}
	/**
	 * 获取考试科目的成绩状况
	 * @param page
	 * @return
	 */
	private Map<String,Object> queryExamInfoCheckStatus(Page page, Map<String,Object> condition){

		Map<String,Object>  resultsMap = new HashMap<String, Object>();
		StringBuffer ids 			   = new StringBuffer();
		try {
			if (condition.containsKey("branchSchool")) {
				resultsMap.put("branchSchool", condition.get("branchSchool"));
			}
			if (condition.containsKey("examSubId")) {
				resultsMap.put("examSubId", condition.get("examSubId"));
			}
			if (condition.containsKey("teachType")) {
				resultsMap.put("teachType", condition.get("teachType"));
			}
			if ("networkstudy".equals(condition.get("teachType"))) {
				for (int i = 0; i < page.getResult().size(); i++) {
					ExamInfoVo vo = (ExamInfoVo) page.getResult().get(i);
					String cla_id = vo.getClassesid();
					String c_id  = vo.getCourseid();
					String ei_id = vo.getExamInfoResourceId();
					String mc_id = "";
					resultsMap.put("classesid", cla_id);
					resultsMap.put("courseId", c_id);
					resultsMap.put("examInfoId", ei_id);
					//ids.append(",'"+vo.getExamInfoResourceId()+"'");
					//}
					//if (ExStringUtils.isNotBlank(ids.toString())&&ids.length()>0) {
					//resultsMap.put("examInfoIds", ids.substring(1));
					String sql = genHQL(resultsMap, "examInfoCheckStatus");
					List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, resultsMap);
					//resultsMap.clear();
					for (Map<String,Object> m :list) {
						//String examInfo    = (String) m.get("EXAMINFOID");
						//String classesid    = (String) m.get("classesid");
						String CHECKSTATUS = (String)m.get("CHECKSTATUS");
						BigDecimal COUNTS  = (BigDecimal)m.get("COUNTS");
						String color       = "red";
						switch (Integer.valueOf(CHECKSTATUS)) {
							case 0:
								color       = "blue";
								break;
							case 1:
								color       = "green";
								break;
							case 2:
								color       = "red";
								break;
							case 3:
								color       = "green";
								break;
							case 4:
								color       = "green";
								break;
							default:
								color       = "red";
								break;
						}
						if (resultsMap.containsKey(mc_id+cla_id+ei_id)) {
							String str     = (String) resultsMap.get(mc_id+cla_id+ei_id);
							str           += "<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp";
							resultsMap.put(mc_id+cla_id+ei_id,str);
						}else{
							resultsMap.put(mc_id+cla_id+ei_id,"<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp");
						}
					}
				}
			}else if (!"networkstudy".equals(condition.get("teachType"))) {
				for (int i = 0; i < page.getResult().size(); i++) {
					FaceExamResultsVo vo = (FaceExamResultsVo) page.getResult().get(i);
					String g_id  = vo.getGradeId();
					String cl_id = vo.getClassicId();
					String m_id  = vo.getMajorId();
					String c_id  = vo.getCourseId();
					String mc_id = vo.getMajorCourseId();
					String cla_id = vo.getClassesid();
					String ei_id = vo.getExamInfoId();

					resultsMap.put("gradeid", g_id);
					resultsMap.put("classic", cl_id);
					resultsMap.put("major", m_id);
					resultsMap.put("courseId", c_id);
					resultsMap.put("majorCourseId", mc_id);
					resultsMap.put("classesid", cla_id);
					resultsMap.put("examInfoId", ei_id);

					String sql          = genHQL(resultsMap,"faceCourseCheckStatus");
					List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, resultsMap);

					for (Map<String,Object> m :list) {

						String CHECKSTATUS = (String) m.get("CHECKSTATUS");
						BigDecimal COUNTS  = (BigDecimal)m.get("COUNTS");
						String color       = "red";
						switch (Integer.valueOf(CHECKSTATUS)) {
							case 0:
								color       = "blue";
								break;
							case 1:
								color       = "green";
								break;
							case 2:
								color       = "red";
								break;
							case 3:
								color       = "green";
								break;
							case 4:
								color       = "green";
								break;
							default:
								color       = "red";
								break;
						}
						if (resultsMap.containsKey(mc_id+cla_id+ei_id)) {
							String str     = (String) resultsMap.get(mc_id+cla_id+ei_id);
							str           += "<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp";
							resultsMap.put(mc_id+cla_id+ei_id,str);
						}else{
							resultsMap.put(mc_id+cla_id+ei_id,"<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultsMap;
	}
	/**
	 * 下载成绩单---课程列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objectPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/downloadtranscripts-examinfo-list.html")
	public String downLoadTranscripts(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objectPage){

		Map<String,Object> condition = new HashMap<String, Object> ();
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId            	 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		ExamSub examSub 			 = examSubService.get(examSubId);
		User user                    = SpringSecurityHelper.getCurrentUser();
		String resultsCourseIds 	 = "";
		boolean isTeacher 			 = false;

		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}

		if(null!=examSub){
			String teacherCode      = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();
			isTeacher               =  user.isContainRole(teacherCode);
//			if(isTeacher){	
//				condition.put("curUserId", user.getResourceid());
//				String courseHQL = genHQL(condition,"TeachTask");	
//				
//				List<Object> examResultsCourseIds = teachtaskservice.findByHql(TeachTask.class,courseHQL,examSub.getYearInfo().getResourceid(),examSub.getTerm());
//				if(null!=examResultsCourseIds && !examResultsCourseIds.isEmpty()){
//					for (int i = 0; i < examResultsCourseIds.size(); i++) {
//						resultsCourseIds+= ","+"'"+examResultsCourseIds.get(i)+"'";
//					}
//					resultsCourseIds     = resultsCourseIds.substring(1);
//				}
//				if (ExStringUtils.isNotEmpty(resultsCourseIds)){
//					condition.put("resultsCourseIds", resultsCourseIds);
//				} 
//			}
			objectPage = examInfoService.findExamInfoForExamResultsInput(condition, objectPage);

		}

		model.addAttribute("page",objectPage);
		model.addAttribute("condition",condition);

		return"/edu3/teaching/examResult/examInfoListForDownLoadTranscripts";
	}
	/**
	 * 生成HQL语句
	 * @return
	 */
	private String genHQL(Map<String,Object> condition,String flag){

		StringBuffer hql = new StringBuffer();
		if ("TeachTask".equals(flag)){//查询当前用户在教学任务书中所负责的课程

//			hql.append(" select task.course.resourceid from "+TeachTask.class.getSimpleName()+" task where task.isDeleted=0");
//			hql.append(" and task.taskStatus=3 and task.yearInfo.resourceid=?");
//			hql.append(" and task.term=?");
//			hql.append(" and (task.teacherId='"+condition.get("curUserId")+
//					   "' or task.defaultTeacherIds like '%"+condition.get("curUserId")+"%')");

		}else if ("ExamResults".equals(flag)){//查询当前用户在教学任务书中所负责的课程对应的课程成绩

			hql.append(" from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 ");
			/*if ( !condition.containsKey("checkStatus")) {
				hql.append(" and  results.writtenScore is null ");
				hql.append(" and ( results.examAbnormity is null or results.examAbnormity=0)") ;
			}
			hql.append(" and results.examSeatNum is not null");
			hql.append(" and results.examroom is not null");
			*/
			hql.append(" and results.examsubId=:examSubId");
			if (condition.containsKey("branchSchool")) {
				hql.append(" and results.studentInfo.branchSchool.resourceid=:branchSchool");
			}
			if (condition.containsKey("examInfoId")) {
				hql.append(" and results.examInfo.resourceid=:examInfoId");
			}
			if(condition.containsKey("courseIds")) {
				hql.append(" and results.course.resourceid in("+condition.get("courseIds")+")");
			}
			if (condition.containsKey("courseName")) {
				hql.append(" and results.course.courseName like '%"+condition.get("courseName")+"%'");
			}
			if (condition.containsKey("courseId")) {
				hql.append(" and results.course.resourceid=:courseId");
			}
			if (condition.containsKey("stuNo")) {
				hql.append(" and results.studentInfo.studyNo like '%"+condition.get("stuNo").toString().trim()+"%'");
			}
			if (condition.containsKey("stuName")) {
				hql.append(" and results.studentInfo.studentName like'%"+condition.get("stuName")+"%'");
			}
			if (condition.containsKey("checkStatus")) {
				hql.append(" and results.checkStatus=:checkStatus");
			}else {
				hql.append(" and  cast( results.checkStatus as int )< 2");
			}
			if (condition.containsKey("isAbnormityInput")&& "N".equals(condition.get("isAbnormityInput").toString())) {
				if(!condition.containsKey("filterExamType")){
					hql.append(" and results.examInfo.isAbnormityEnd='Y'");
				}
			}

			hql.append(" order by results.studentInfo.branchSchool.unitCode asc,results.course.resourceid,results.studentInfo.studyNo asc ");

		}else if ("ExamResults_statExam".equals(flag)){//

			hql.append(" from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 ");
			hql.append(" and results.examsubId=:examSubId");
			if (condition.containsKey("branchSchool")) {
				hql.append(" and results.studentInfo.branchSchool.resourceid=:branchSchool");
			}
			if (condition.containsKey("examInfoId")) {
				hql.append(" and results.examInfo.resourceid=:examInfoId");
			}
			if(condition.containsKey("courseIds")) {
				hql.append(" and results.course.resourceid in("+condition.get("courseIds")+")");
			}
			if (condition.containsKey("courseName")) {
				hql.append(" and results.course.courseName like '%"+condition.get("courseName")+"%'");
			}
			if (condition.containsKey("courseId")) {
				hql.append(" and results.course.resourceid=:courseId");
			}
			if (condition.containsKey("stuNo")) {
				hql.append(" and results.studentInfo.studyNo like '%"+condition.get("stuNo").toString().trim()+"%'");
			}
			if (condition.containsKey("stuName")) {
				hql.append(" and results.studentInfo.studentName like'%"+condition.get("stuName")+"%'");
			}
			if (condition.containsKey("checkStatus")) {
				hql.append(" and results.checkStatus=:checkStatus");
			}else {
				if(!condition.containsKey("isReadOnly")){
					hql.append(" and  (cast( results.checkStatus as int )< 2 or results.usCheckStatus is null )");
				}
			}
			if (condition.containsKey("usCheckStatus")) {
				hql.append(" and results.usCheckStatus=:usCheckStatus");
			}else {
				if(!condition.containsKey("isReadOnly")){
					hql.append(" and  (cast( results.usCheckStatus as int )< 2 or results.usCheckStatus is null )");
				}
			}
			if (condition.containsKey("examSeatNum")){
				hql.append(" and examSeatNum = '"+condition.get("examSeatNum")+"' ");
			}
			if (condition.containsKey("examRoom")){
				hql.append(" and examroom.examroomName = '"+condition.get("examRoom")+"' ");
			}
			//hql.append(" order by results.studentInfo.branchSchool.unitCode asc,results.course.resourceid,results.studentInfo.studyNo asc ");
			//按年级做第一排序、第二排专业、第三排学号。
			hql.append(" order by results.studentInfo.grade.gradeName,results.studentInfo.major.majorCode,results.studentInfo.studyNo asc ");

		}else if ("examCount".equals(flag)){//获取一个学生某门课程的考试次数

			hql.append(" select count(examResults.resourceid) from "+ExamResults.class.getSimpleName()+" examResults where examResults.isDeleted=0");
			hql.append(" and examResults.course.resourceid=? and examResults.studentInfo.resourceid=? and examResults.resourceid<>?");


		}else if ("studentInfo".equals(flag)) {
			hql.append("select stu.resourceid,u.unitname,g.gradename,ci.classicname,stu.learningStyle,m.majorname,cl.classesname,stu.studyno,stu.studentname,stu.studentstatus");
			hql.append(" from edu_roll_studentinfo stu join hnjk_sys_unit u on u.resourceid=stu.branchschoolid and u.isdeleted=0");
			hql.append(" join edu_base_grade g on g.resourceid=stu.gradeid and g.isdeleted=0");
			hql.append(" join edu_base_classic ci on ci.resourceid=stu.classicid and ci.isdeleted=0");
			hql.append(" join edu_base_major m on m.resourceid=stu.majorid and m.isdeleted=0");
			hql.append(" join edu_roll_classes cl on cl.resourceid=stu.classesid and cl.isdeleted=0");
			hql.append(" left join EDU_ROLL_STUAUDIT sa on sa.STUDENTINFOID=stu.resourceid and sa.graduateAuditStatus='1' and sa.isdeleted=0");
			hql.append(" left join EDU_TEACH_GRADUATEDATA gd on gd.STUDENTID=stu.resourceid and gd.resourceid=sa.GRADUATEDATAID and gd.isdeleted=0");
			hql.append(" where stu.isDeleted=0 ");
			if(condition.containsKey("branchSchool"))//教学点
			{
				hql.append(" and stu.branchSchoolid=?");
			}
			if(condition.containsKey("gradeid"))//年级
			{
				hql.append(" and stu.gradeid=?");
			}
			if(condition.containsKey("classic"))//层次
			{
				hql.append(" and stu.classicid=?");
			}
			if(condition.containsKey("learningStyle"))//学习形式
			{
				hql.append(" and stu.learningStyle=?");
			}
			if(condition.containsKey("major"))//专业
			{
				hql.append(" and stu.majorid=?");
			}
			if(condition.containsKey("classId"))//班级
			{
				hql.append(" and stu.classesid =?");
			}
			if(condition.containsKey("studyNo"))//学号
			{
				hql.append(" and stu.studyNo like '%"+condition.get("studyNo").toString().trim()+"%'");
			}
			if(condition.containsKey("name"))//姓名
			{
				hql.append(" and stu.studentName like '%"+condition.get("name")+"%'");
			}
			if(condition.containsKey("studentStatus")){//学籍状态
				hql.append(" and stu.studentStatus=?");
			}else if(condition.isEmpty()){
				hql.append(" and stu.studentStatus='11'");
			}

			if (condition.containsKey("graduateDateStr")) {//毕业日期
				hql.append(" and gd.graduateDate =to_date('"+condition.get("graduateDateStr")+"','yyyy-mm-dd')");
			}

			//添加关于毕业确认时间的查询 
			boolean hascGDb = condition.containsKey("confirmGraduateDateb");
			boolean hascGDe = condition.containsKey("confirmGraduateDatee");
			if(hascGDb||hascGDe){//毕业确认时间
				if (hascGDb&&hascGDe){
					hql.append(" sa.auditTime between to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-mm-dd') and to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-mm-dd') and gd.isDeleted='0') ");
				}
				if (hascGDb&&!hascGDe){
					hql.append(" sa.auditTime > to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-mm-dd') and gd.isDeleted='0') ");
				}
				if (!hascGDb&&hascGDe){
					hql.append(" sa.auditTime < to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-mm-dd') and gd.isDeleted='0') ");
				}
			}
			//是否全部成绩（已有考试记录）通过
			if(condition.containsKey("isAllPass")){
				hql.append(" and stu.resourceid "+("Y".equals(condition.get("isAllPass"))?"not":"")+" in(");
				hql.append("select distinct resourceid from(");
				hql.append("select si.resourceid,si.studyno,si.studentname,ml.courseid,ml.ispass,er.examsubid,");
				hql.append(" row_number() over(partition by er.studentid,er.courseid order by decode(er.ismakeupexam,'Q',1,'T',2,'Y',3,4))rn");
				hql.append(" from edu_teach_makeuplist ml join edu_roll_studentinfo si on si.resourceid=ml.studentid");
				hql.append(" join edu_teach_examresults er on er.resourceid=ml.resultsid");
				hql.append("  where ml.isdeleted=0 and er.isdeleted=0 and si.isdeleted=0");
				if(condition.containsKey("branchSchool")) {
					hql.append(" and si.branchSchoolid='"+condition.get("branchSchool")+"'");
				}
				if(condition.containsKey("gradeid")) {
					hql.append(" and si.gradeid='"+condition.get("gradeid")+"'");
				}
				if(condition.containsKey("classic")) {
					hql.append(" and si.classicid='"+condition.get("classic")+"'");
				}
				if(condition.containsKey("learningStyle")) {
					hql.append(" and si.learningStyle='"+condition.get("learningStyle")+"'");
				}
				if(condition.containsKey("major")) {
					hql.append(" and si.majorid='"+condition.get("major")+"'");
				}
				if(condition.containsKey("classId")) {
					hql.append(" and si.classesid='"+condition.get("classId")+"'");
				}
				if(condition.containsKey("studyNo"))//学号
				{
					hql.append(" and si.studyNo like '%"+condition.get("studyNo").toString().trim()+"%'");
				}
				if(condition.containsKey("name"))//姓名
				{
					hql.append(" and stu.studentName like '%"+condition.get("name")+"%'");
				}
				hql.append(" order by si.studyno,ml.courseid) where rn = 1 and ispass!='Y')");
			}
			//当默认排序出现重复的时候 就会一条记录覆盖另一条记录，重复出现。
			//hql.append(" order by u.unitcode,g.gradename desc,stu.classicid,stu. stu.studyNo ");
			hql.append(" order by u.unitcode,g.gradename desc,ci.classiccode,stu.learningstyle,m.majorcode,cl.classesname,stu.studyNo ");
		}else if("submitExamResults".equals(flag)){

			hql.append("from "+ExamResults.class.getSimpleName()+" examResults where examResults.isDeleted=0");
			hql.append(" and ( (examResults.checkStatus ='0' or examResults.checkStatus ='1') or ( examResults.checkStatus='2' and examResults.fillinManId=:curUserId and examResults.examsubId=:examSubId ) )");

			if (condition.containsKey("curUserId")) {
				hql.append(" and examResults.fillinManId=:curUserId");
			}
			if (condition.containsKey("examSubId")) {
				hql.append(" and examResults.examsubId=:examSubId");
			}
			if (condition.containsKey("courseId")) {
				hql.append(" and examResults.course.resourceid =:courseId");
			}
			if (condition.containsKey("courseName")) {
				hql.append(" and examResults.course.courseName like'%"+condition.get("courseName").toString()+"%'");
			}

			hql.append(" order by cast( examResults.checkStatus as int ) asc,examResults.studentInfo.branchSchool.unitCode asc,examResults.course.resourceid,examResults.examSeatNum asc ");
		/*}else if("examInfoCheckStatus".equals(flag)){
			hql.append(" select rs.examinfoid,rs.checkstatus ,count(rs.resourceid) counts ");
			hql.append("   from edu_teach_examresults rs  ");
			hql.append("   inner join edu_roll_studentinfo  stu on rs.studentid = stu.resourceid   ");
			if(condition.containsKey("branchSchool"))
			hql.append(" and stu.branchschoolid =:branchSchool ");
			if(condition.containsKey("examSubId"))
			hql.append(" and rs.examsubId =:examSubId ");
			hql.append("  where rs.isdeleted = 0    and rs.examinfoid in ("+condition.get("examInfoIds")+") ");
			hql.append(" group by rs.examinfoid,rs.checkstatus ");
			hql.append(" order by rs.examinfoid ");*/
		}else if("faceCourseCheckStatus".equals(flag) || "examInfoCheckStatus".equals(flag)){

			hql.append(" select rs.checkstatus ,count(rs.resourceid) counts ");
			hql.append("   from edu_teach_examresults rs   ");
			hql.append("   inner join edu_roll_studentinfo  stu on rs.studentid = stu.resourceid   ");
			if(condition.containsKey("branchSchool")) {
				hql.append(" and stu.branchschoolid =:branchSchool ");
			}
			if(condition.containsKey("classesid")) {
				hql.append(" and stu.CLASSESID =:classesid ");
			}
			if(condition.containsKey("gradeid")) {
				hql.append(" and stu.gradeid=:gradeid");
			}
			if(condition.containsKey("major")) {
				hql.append(" and stu.majorid=:major");
			}
			if(condition.containsKey("classic")) {
				hql.append(" and stu.classicid=:classic");
			}
			hql.append(" where rs.isdeleted = 0 and rs.checkstatus!='-1' ");
			if(condition.containsKey("examSubId")) {
				hql.append(" and rs.examsubid=:examSubId");
			}
			if(condition.containsKey("courseId")) {
				hql.append(" and rs.courseid =:courseId");
			}
			if(condition.containsKey("examInfoId")) {
				hql.append(" and rs.examinfoid =:examInfoId");
			}
			if(condition.containsKey("majorCourseId")) {
				hql.append(" and rs.majorcourseid =:majorCourseId");
			}
			if(condition.containsKey("teachType")) {
				hql.append(" and rs.PLANCOURSETEACHTYPE =:teachType");
			}
			hql.append(" group by rs.checkstatus ");

		}else if ("queryPublishedResults".equals(flag)) {

			hql.append(" select info.resourceid examInfoResourceId,info.examcoursecode examCourseCode ,c.coursename courseName,info.examcoursetype examCourseType,info.ismachineexam isMachineexam,count(rs.resourceid) orderNumber,c.examtype ");
			hql.append(" from edu_teach_examinfo info  ");
			hql.append(" inner join edu_base_course c on info.courseid = c.resourceid ");
			if(condition.containsKey("courseId")){
				hql.append(" and c.resourceid =? ");
			}
			hql.append(" inner join edu_teach_examresults rs on info.resourceid = rs.examinfoid and rs.isdeleted = 0 ");
			hql.append(" where info.isdeleted = 0 ");

			if(condition.containsKey("examSubId")){
				hql.append(" and info.examsubid  =? ");
			}
			if (condition.containsKey("examCourseCode")) {
				hql.append(" and info.examcoursecode like '%"+condition.get("examCourseCode")+"%'");
			}
			if (condition.containsKey("examInfoCourseType")){
				hql.append(" and info.examCourseType  =? ");
			}
			if (condition.containsKey("isMachineexam")&& "Y".equals(condition.get("isMachineexam"))){
				hql.append(" and info.ISMACHINEEXAM  =? ");
			}else if(condition.containsKey("isMachineexam")&& "N".equals(condition.get("isMachineexam"))){
				hql.append(" and ( info.ISMACHINEEXAM  =? or info.ISMACHINEEXAM is null)");
			}
			if (condition.containsKey("branchSchool")) {
				hql.append(" and exists(");
				hql.append("  select * from edu_teach_examresults rs1 ");
				hql.append(" inner join edu_roll_studentinfo stu on rs1.studentid = stu.resourceid ");
				hql.append("   and stu.branchschoolid =?");
				hql.append(" where rs1.isdeleted = 0 and rs1.examinfoid = info.resourceid and rs1.resourceid = rs.resourceid");
				hql.append(" )");
			}
			hql.append(" group by info.examcoursecode,c.coursename,info.examCourseType,info.ismachineexam,info.resourceid,c.examtype ");
		}

		return hql.toString();
	}

	/**
	 * 异常成绩录入状态-课程列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/abnormity-config-list.html")
	public String abnormityStatusConfing(HttpServletRequest request, ModelMap model, Page page){

		Map<String,Object> condition = new HashMap<String, Object> ();
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId            	 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		ExamSub examSub 			 = examSubService.get(examSubId);
		User user                    = SpringSecurityHelper.getCurrentUser();
		String resultsCourseIds 	 = "";
		boolean isTeacher 			 = false;

		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}

		if(null!=examSub){
			condition.put("curUserId", user.getResourceid());
			/*String teacherCode      = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();
			isTeacher               =  user.isContainRole(teacherCode);
			if(isTeacher){	
				condition.put("curUserId", user.getResourceid());
				String courseHQL = genHQL(condition,"TeachTask");	
				
				List<Object> examResultsCourseIds = teachtaskservice.findByHql(TeachTask.class,courseHQL,examSub.getYearInfo().getResourceid(),examSub.getTerm());
				if(null!=examResultsCourseIds && !examResultsCourseIds.isEmpty()){
					for (int i = 0; i < examResultsCourseIds.size(); i++) {
						resultsCourseIds+= ","+"'"+examResultsCourseIds.get(i)+"'";
					}
					resultsCourseIds     = resultsCourseIds.substring(1);
				}
				if (ExStringUtils.isNotEmpty(resultsCourseIds)){
					condition.put("resultsCourseIds", resultsCourseIds);
				} 
			}*/
			page = examInfoService.findExamInfoForAbnormitConfig(condition, page);

		}

		model.addAttribute("page",page);
		model.addAttribute("condition",condition);

		return "/edu3/teaching/examResult/examInfoListForAbnormityConfig";
	}
	/**
	 * 异常成绩录入状态
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/abnormity-status.html")
	public void abnormityInputStatus(HttpServletRequest request, HttpServletResponse response){

		Map<String,Object> map = new HashMap<String, Object>();
		String status    	   = ExStringUtils.trimToEmpty(request.getParameter("status"));
		String examInfoId      = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		try {
			if (ExStringUtils.isNotEmpty(examInfoId)) {
				ExamInfo info  = examInfoService.get(examInfoId);
				info.setIsAbnormityEnd(status);

				if ((ExStringUtils.isNotEmpty(info.getIsMachineExam())&&
						Constants.BOOLEAN_YES.equals(info.getIsMachineExam()))||
						(info.getCourse().getExamType()!=null && info.getCourse().getExamType()==6)) {//机考或网考
					List<ExamResults> list = examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examInfo", info));
					for (ExamResults rs :list) {
						if (ExStringUtils.isNotBlank(rs.getCheckStatus()) && Integer.valueOf(rs.getCheckStatus())<2) {
							if("0".equals(rs.getExamAbnormity())&&ExStringUtils.isBlank(rs.getWrittenMachineScore())){
								rs.setExamAbnormity("2");//设置为缺考
							}
							rs.setCheckStatus("1");
						}
					}
					examResultsService.batchSaveOrUpdate(list);
				}

				examInfoService.update(info);
			}
			map.put("statusCode", 200);
			map.put("message", "设置成功！");
		} catch (Exception e) {
			logger.error("异常成绩录入状态设置出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置异常成绩录入状态失败:"+e.getMessage());
		}

		renderJson(response, JsonUtils.mapToJson(map));

	}
	/**
	 * 在线录入成绩-列表
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/input-examresults-list.html")
	public String inputExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws ParseException{

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool          = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String stuNo                 = ExStringUtils.trimToEmpty(request.getParameter("stuNo"));
		String stuName               = ExStringUtils.trimToEmpty(request.getParameter("stuName"));
		String  checkStatus          = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String usCheckStatus         = ExStringUtils.trimToEmpty(request.getParameter("usCheckStatus"));
		String examRoom 			 = ExStringUtils.trimToEmpty(request.getParameter("examRoom"));
		String examSeatNum			 = ExStringUtils.trimToEmpty(request.getParameter("examSeatNum"));
		User curUser 				 = SpringSecurityHelper.getCurrentUser();
		ExamSub examSub 			 = examSubService.get(examSubId);
		ExamInfo info                = examInfoService.get(examInfoId);
		String curUserId             = curUser.getResourceid();
		String msg                   = "";
		String isTeacher             = "";
		String isAbnormityInput      = "";
		String isAllowInput          = Constants.BOOLEAN_YES;
		String type 				 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String isReadOnly 			 = ExStringUtils.trimToEmpty(request.getParameter("isReadOnly"));
		if("1".equals(isReadOnly)){
			condition.put("isReadOnly", isReadOnly);
		}
		if (null!=examSub && info!=null ){

			Date curTime             = new Date();
			Date examinputStartTime  = null==examSub.getExaminputStartTime()?new Date():examSub.getExaminputStartTime();
			Date examinputEndTime    = null==examSub.getExaminputEndTime()?new Date():examSub.getExaminputEndTime();
			Date examinputUsualStartTime  = null==examSub.getUsualScoreInputStartTime()?new Date():examSub.getUsualScoreInputStartTime();
			Date examinputUsualEndTime    = null==examSub.getUsualScoreInputEndTime()?new Date():examSub.getUsualScoreInputEndTime();
			String examsubStatus     = ExStringUtils.trimToEmpty(examSub.getExamsubStatus());
			String isAbnormityEnd    = ExStringUtils.trimToEmpty(info.getIsAbnormityEnd());

			String inputCode         = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();//期末成绩录入角色编码
			String abnormityInput    = CacheAppManager.getSysConfigurationByCode("examresultsAbnormityInput").getParamValue();//异常成绩录入人角色编码

			//isTeacher                = curUser.isContainRole(inputCode)==true?"Y":"N";
			isTeacher                = "Y";
			isAbnormityInput         = curUser.isContainRole(abnormityInput)==true?"Y":"N";

			condition.put("curUserId",curUserId);
			condition.put("isTeacher",isTeacher);
			condition.put("examType",info.getCourse().getExamType());
			//condition.put("isAbnormityInput",isAbnormityInput);
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
				condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
				condition.put("branchSchoolName", curUser.getOrgUnit().getUnitName());
				condition.put("isBranchSchool", "Y");
			}
			if ("1".equals(isReadOnly)||curTime.getTime()>=examinputStartTime.getTime()&&
					curTime.getTime()<=examinputEndTime.getTime()&&
					"3".equals(examsubStatus)&&	"Y".equals(isTeacher)&&!"normal".equals(type)||curTime.getTime()>=examinputUsualStartTime.getTime()&&
					curTime.getTime()<=examinputUsualEndTime.getTime()&&"normal".equals(type)){
				/*
				("Y".equals(isTeacher)&& ("Y".equals(isAbnormityEnd)||info.getCourse().getExamType()!=null&&info.getCourse().getExamType()>1)||"Y".equals(isAbnormityInput))){
				
				if("Y".equals(isTeacher)&&("Y".equals(isAbnormityEnd)||info.getCourse().getExamType()!=null&&info.getCourse().getExamType()>1)){
					condition.put("filterExamType","Y");
				}
				*/
				if(ExStringUtils.isNotEmpty(courseId)) {
					condition.put("courseId",courseId);
				}
				if(ExStringUtils.isNotEmpty(examSubId)) {
					condition.put("examSubId",examSubId);
				}
				if(ExStringUtils.isNotEmpty(branchSchool)) {
					condition.put("branchSchool",branchSchool);
				}
				if(ExStringUtils.isNotEmpty(stuNo)) {
					condition.put("stuNo",stuNo);
				}
				if(ExStringUtils.isNotEmpty(stuName)) {
					condition.put("stuName",stuName);
				}
				if(ExStringUtils.isNotEmpty(checkStatus)) {
					condition.put("checkStatus",checkStatus);
				}
				if(ExStringUtils.isNotEmpty(examInfoId)) {
					condition.put("examInfoId",examInfoId);
				}
				if(ExStringUtils.isNotEmpty(usCheckStatus)) {
					condition.put("usCheckStatus", usCheckStatus);
				}
				if(ExStringUtils.isNotEmpty(examRoom)) {
					condition.put("examRoom", examRoom);
				}
				if(ExStringUtils.isNotEmpty(examSeatNum)) {
					condition.put("examSeatNum", examSeatNum);
				}
				String yearInfoId  	     = examSub.getYearInfo().getResourceid();
				String term       	     = examSub.getTerm();
				String taskHQL    	     = genHQL(condition,"TeachTask");
				String resultsCourseIds  = ""; 
				
				/*if("Y".equals(isTeacher)){	
					List<Object> examResultsCourseIds = teachtaskservice.findByHql(TeachTask.class,taskHQL,yearInfoId,term);
					
					if(null!=examResultsCourseIds && !examResultsCourseIds.isEmpty()) {
						for (int i = 0; i < examResultsCourseIds.size(); i++) {
							resultsCourseIds+= ","+"'"+examResultsCourseIds.get(i)+"'";
						}
						resultsCourseIds     = resultsCourseIds.substring(1);
					}
					if(ExStringUtils.isNotEmpty(resultsCourseIds)) condition.put("courseIds",resultsCourseIds );
						
				}*/

				if (!condition.isEmpty()&&(condition.containsKey("courseIds")||condition.containsKey("courseId"))||condition.containsKey("examInfoId")) {
					//String examResultsHQL    = genHQL(condition,"ExamResults");
					String examResultsHQL    = genHQL(condition,"ExamResults_statExam");
					objPage 				 = examResultsService.findByHql(objPage, examResultsHQL, condition);
				}
			}else {
				msg = "考试批次未关闭或者不在成绩录入时间范围内，或者当前用户不是该课程的负责老师！<br/><strong>考试批次状态：</strong>"+("3".equals(examsubStatus)?"关闭":"未关闭")+"</br><strong>成绩录入时间：</strong><br/>"
						+ ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)
						+ " 至 "
						+ ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME);
				isAllowInput = Constants.BOOLEAN_NO;
			}

		}else {
			msg 	= "请选择一个要录入成绩的考试批次！";
			isAllowInput = Constants.BOOLEAN_NO;
		}

		condition.put("isAllowInput",isAllowInput);
		condition.put("examInfo", info);
		condition.put("msg",msg);
		List<ExamResults> scoreList = objPage.getResult();
		if(scoreList.size()>0){
			for (ExamResults rs : scoreList) {
				if(ExStringUtils.isNotEmpty(rs.getCheckStatus())&&Integer.valueOf(rs.getCheckStatus())<1){
					ExamInfo examinfo = rs.getExamInfo();
					Map<String,Object> data = caculateIntegratedScoreForNetStudy(examinfo, rs.getUsuallyScore(), rs.getWrittenScore(), rs.getWrittenHandworkScore(), rs);
					rs.setIntegratedScore(null!=data.get("integratedScore")?data.get("integratedScore").toString():"");
					examResultsService.update(rs);
				}
			}
		}
		model.addAttribute("page", objPage);
		model.addAttribute("condition",condition);
		if(!"normal".equals(type)){
			return "/edu3/teaching/examResult/inputExamResults-list";
		}else{
			return "/edu3/teaching/examResult/inputUsExamResults-list";
		}

	}
	/**
	 * 在线录入成绩-保存
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping(value = {"/edu3/teaching/result/input-examresults-save.html","/edu3/teaching/result/input-usexamresults-save.html"})
	public void inputExamResultsSave(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		String [] usuallyScores   	 = request.getParameterValues("usuallyScore");   //平时成绩
		String [] writtenScores   	 = request.getParameterValues("writtenScore");   //卷面成绩
		String [] examAbnormitys  	 = request.getParameterValues("examAbnormity");  //成绩异常
		String [] writtenHandworks   = request.getParameterValues("writtenHandworkScore");//混合机考笔考部份成绩
		String [] integratedScores   = request.getParameterValues("integratedScore"); //总评成绩

		String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL
		ExamSub sub               	 = examSubService.get(examSubId);    			 //要导入成绩的考试批次
		User user                    = SpringSecurityHelper.getCurrentUser();		 //当前用户
		String curUserName           = user.getCnName();    					  	 //当前用户中文名
		String curUserId             = user.getResourceid();						 //当前用户ID
		String type 				 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		if (null!=sub&&null!=resultsIds){

			Date curTime             = new Date();                                	 //当前时间
			String examsubStatus     = sub.getExamsubStatus();                    	 //考试批次状态
			Date examinputEndTime    = sub.getExaminputEndTime();                 	 //考试批次成绩录入结束时间
			Date examinputStartTime  = sub.getExaminputStartTime();               	 //考试批次成绩录入开始时间
			Date examinputUsualEndTime    = sub.getUsualScoreInputEndTime();                 	 //考试批次平时成绩录入结束时间
			Date examinputUsualStartTime  = sub.getUsualScoreInputStartTime();               	     //考试批次平时成绩录入开始时间

			String abnormityInput    = CacheAppManager.getSysConfigurationByCode("examresultsAbnormityInput").getParamValue();//异常成绩录入人角色编码
			boolean isAbnormityInput = user.isContainRole(abnormityInput);


			//当前时间在成绩录入开始时间及成绩录入结束时间之间且考试批次为关闭
			if (!"normal".equals(type)&&curTime.getTime()>=examinputStartTime.getTime()&&curTime.getTime()<=examinputEndTime.getTime()&&"3".equals(examsubStatus)||"normal".equals(type)&&curTime.getTime()>=examinputUsualStartTime.getTime()&&curTime.getTime()<=examinputUsualEndTime.getTime()) {

				try {
					String msg = "";
					if(!"normal".equals(type)){
						msg = examResultsService.examResultsInputSave(resultsIds, integratedScores,writtenScores,examAbnormitys, writtenHandworks, examCountHQL,curUserName, curUserId, curTime, isAbnormityInput);
					}else{
						msg = examResultsService.usExamResultsInputSave(resultsIds,curUserName,usuallyScores,curUserId,curTime);
					}

					if (ExStringUtils.isNotBlank(msg)) {
						condition.put("statusCode",300);
						condition.put("message",msg);
					}else{
						condition.put("statusCode",200);
						condition.put("message","保存成功！");
					}


				} catch (Exception e) {
					logger.error("录入学生成绩异常{}",e.fillInStackTrace());
					condition.put("statusCode",300);
					condition.put("message","录入学生成绩异常:"+e.getMessage());
				}

			}else {
				condition.put("statusCode",300);
				condition.put("message","考试批次未关闭或者已超过成绩录入时间范围！<br/> <strong>成绩录入时间:</strong><br/>"+
						ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)+ " 至 "+
						ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME));
			}

		}else {
			condition.put("statusCode",300);
			condition.put("message","成绩为空或者找不到考试批次,请与管理员联系！");
		}

		renderJson(response,JsonUtils.mapToJson(condition));

	}

	/**
	 * 提交成绩-列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/submit-examresults-list.html")
	public String processExamResultsList(HttpServletRequest request, HttpServletResponse response , ModelMap model, Page objPage){

		//objPage.setOrder(Page.ASC);
		//objPage.setOrderBy("  ");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseName= ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		User curUser 	 = SpringSecurityHelper.getCurrentUser();


		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(null!=curUser) {
			condition.put("curUserId", curUser.getResourceid());
		}

		//此处调用genHQL()方法需传入 examSubId curUserId两个参数
		String hql       = genHQL(condition, "submitExamResults");
		if (ExStringUtils.isNotEmpty(examSubId)) {
			objPage      = examResultsService.findByHql(objPage, hql, condition);
		}
		model.addAttribute("condition",condition);
		model.addAttribute("objPage", objPage);
		return"/edu3/teaching/examResult/submitExamResult_list";
	}
	/**
	 *  提交成绩-保存
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/submit-examresults-save.html")
	public void processExamResultsSave(HttpServletRequest request, HttpServletResponse response ) throws ParseException{

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		ExamSub sub               	 = examSubService.get(examSubId);    			 //要导入成绩的考试批次
		User curUser 	 			 = SpringSecurityHelper.getCurrentUser();

		if (null!=resultsIds && null!=sub) {

			Date curTime             = new Date();                                	 //当前时间
			String examsubStatus     = sub.getExamsubStatus();                    	 //考试批次状态
			Date examinputEndTime    = sub.getExaminputEndTime();                 	 //考试批次成绩录入结束时间
			Date examinputStartTime  = sub.getExaminputStartTime();               	 //考试批次成绩录入开始时间

			try {
				//当前时间在成绩录入开始时间及成绩录入结束时间之间且考试批次为关闭
				if (curTime.getTime()>=examinputStartTime.getTime()&&curTime.getTime()<=examinputEndTime.getTime()&&"3".equals(examsubStatus)) {

					for (int i = 0; i < resultsIds.length; i++) {

						ExamResults examResults = examResultsService.get(resultsIds[i]);

						String writtenScore     = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+resultsIds[i]));    //卷面成绩
						String usuallyScore     = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore"+resultsIds[i]));    //平时成绩
						String integratedScore  = ExStringUtils.trimToEmpty(request.getParameter("integratedScore"+resultsIds[i]));//综合成绩

						examResults.setWrittenScore(writtenScore);
						examResults.setUsuallyScore(usuallyScore);
						examResults.setIntegratedScore(integratedScore);
						examResults.setCheckStatus("1");

						examResultsService.update(examResults);

						ExamResultsLog log =  new ExamResultsLog();
						log.setOptionType(ExamResults.EXAMRESULTS_LOG_TYPE_SUBMIT);
						log.setExamSubId(sub.getResourceid());
						log.setFillinDate(curTime);
						log.setFillinMan(curUser.getUsername());
						log.setFillinManId(curUser.getResourceid());
						log.setAttachId(examResults.getResourceid());

						examResultsLogService.save(log);
					}
				}else {

					success = false;
					msg		= "考试批次未关闭或者已超过成绩录入时间范围！<br/>"
							+ "<strong>成绩录入时间:</strong><br/>"
							+ ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)
							+ " 至 "+ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME);
				}
			} catch (Exception e) {
				logger.error("提交成绩出错,考试批次：{}"+sub.getResourceid(),e.fillInStackTrace());
				success = false;
				msg     = "提交成绩时服务器异常："+e.fillInStackTrace();
			}
		}else {
			success = false;
			msg     = "成绩为空或者找不到考试批次,请与管理员联系！";
		}
		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 成绩审核-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/audit-examresults-list.html")
	public String auditExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrderBy("examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo asc ");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId 			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String examInfoId 			 = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String checkStatus           = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String pc_teachType             = ExStringUtils.trimToEmpty(request.getParameter("pc_teachType"));
		String cs_teachType             = ExStringUtils.trimToEmpty(request.getParameter("cs_teachType"));
		String orderBy               = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String classesid             = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		//String _examCourseType   = ExStringUtils.trimToEmpty(request.getParameter("examCourseType"));

		if(ExStringUtils.isNotEmpty(orderBy)) {
			condition.put("orderBy", orderBy);
		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);//查询时的参数
		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);//面试请求的参数
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus", checkStatus);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isEmpty(pc_teachType)) {
			pc_teachType = "facestudy";
		}else {
			condition.put("pc_teachType", pc_teachType);
		}
		if (ExStringUtils.isEmpty(cs_teachType)) {
			pc_teachType = "faceTeach";
		}else {
			condition.put("cs_teachType", cs_teachType);
		}
		if(ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesid", classesid);
		}

		if ("networkstudy".equals(pc_teachType)&&ExStringUtils.isNotEmpty(examInfoId)) {
			ExamInfo info 	  = examInfoService.get(examInfoId);
			objPage 		  = examResultsService.findExamResultByCondition(condition,objPage);
			List list 		  = examResultsService.calculateExamResultsListIntegratedScore(objPage.getResult(), info);
			objPage.setResult(list);
			condition.put("examInfo", info);
		}else {
			if (ExStringUtils.isNotEmpty(courseId)&&ExStringUtils.isNotEmpty(examSubId)) {
				ExamInfo info 		 = examInfoService.get(examInfoId);
				if(info==null){
					//int examCourseType   = cs_teachType.contains("face")?1:0;
					// 华教修改（旧逻辑）
//					List<ExamInfo> infos = examInfoService.findByCriteria(Restrictions.eq("examSub.resourceid", examSubId),Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
					//List<ExamInfo> infos = examInfoService.findByCriteria(Restrictions.eq("examSub.resourceid", examSubId),Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType));//学院2016修改
					info = examInfoService.findExamInfoByCourseAndExamSubAndCourseType(courseId,examSubId,cs_teachType);
				}

				if (null!=info) {
					objPage 		 = examResultsService.findExamResultByCondition(condition,objPage);
					StringBuilder stuIds   				    = new StringBuilder();
					if ("netsidestudy".equals(pc_teachType)) {
						for (ExamResults rs :(List<ExamResults>)objPage.getResult()) {
							stuIds.append(",'"+rs.getStudentInfo().getResourceid()+"'");
						}
					}
					if (ExStringUtils.isNotBlank(stuIds.toString())) {
						Map<String,BigDecimal> usualResults = usualResultsService.calculateNetsidestudyUsualResults(info.getCourse().getResourceid(),stuIds.substring(1));
						for (ExamResults rs :(List<ExamResults>)objPage.getResult()) {
							if (Integer.valueOf(ExStringUtils.defaultIfEmpty(rs.getCheckStatus(), "-1"))<2) {
								String us 						= usualResults.containsKey(rs.getStudentInfo().getResourceid())?usualResults.get(rs.getStudentInfo().getResourceid()).toString():"0";
								rs.setUsuallyScore(us);
							}
						}
					}

					List list 		 = examResultsService.calculateExamResultsListIntegratedScore(objPage.getResult(), info);
					//增加免考的学生进入审核名单当中
					List<StudentInfo> stulist = studentinfoservice.findByHql("from "+StudentInfo.class.getSimpleName()+" stu where stu.classes.resourceid=? and stu.studentStatus in('11','16') and stu.isDeleted = 0",classesid );
					List<String> stuids= new ArrayList<String>();
					for(StudentInfo stu :stulist){
						stuids.add(stu.getResourceid());
					}
					//List<NoExamApply> noexamlist = noexamapplyservice.findByCriteria(Restrictions.in("studentInfo.resourceid", stuids),Restrictions.eq("course.resourceid", courseId),Restrictions.eq("isDeleted", 0),Restrictions.eq("checkStatus", "1"));
					Map<String,Object> tempCondition = new HashMap<String, Object>();
					tempCondition.put("studentInfo.resourceid-idList",stuids);
					tempCondition.put("course.resourceid",courseId);
					tempCondition.put("isDeleted",0);
					tempCondition.put("checkStatus","1");
					if (condition.containsKey("studyNo")) {
						tempCondition.put("studentInfo.studyNo-likeL",studyNo);
					}
					if (condition.containsKey("name")) {
						tempCondition.put("studentInfo.studentName-likeL",name);
					}
					List<NoExamApply> noexamlist = noexamapplyservice.findByCondition(tempCondition);
					if(noexamlist!=null&&noexamlist.size()>0){
						for(NoExamApply noe:noexamlist){
							ExamResults er = new ExamResults();
							StudentInfo stu = noe.getStudentInfo();
							er.setStudentInfo(stu);
							er.setExamAbnormity("6");
							er.setCheckStatus("4");
							er.setCourse(noe.getCourse());
							er.setCourseScoreType("11");
							er.setCourseType("11");
							er.setExamInfo(info);
							list.add(er);
						}
					}
					objPage.setResult(list);
					condition.put("examInfoId", info.getResourceid());
					String facestudyscoreper = "";
					String facestudyscoreper2 = "";
					if(info.getExamCourseType()==1){
						facestudyscoreper = ExStringUtils.nvl(info.getFacestudyScorePer(), info.getExamSub().getFacestudyScorePer());
						facestudyscoreper2 = ExStringUtils.nvl(info.getFacestudyScorePer2(), info.getExamSub().getFacestudyScorePer2());
					}else {
						facestudyscoreper = ExStringUtils.nvl(info.getStudyScorePer(), info.getExamSub().getWrittenScorePer());
						facestudyscoreper2 = ExStringUtils.nvl(info.getNetsidestudyScorePer(), info.getExamSub().getNetsidestudyScorePer());
					}
					model.addAttribute("scoreper", "卷面成绩 ：平时成绩 = "+ExStringUtils.nvl(facestudyscoreper, "")+" ："+ExStringUtils.nvl(facestudyscoreper2, ""));
				}
			}
		}

		model.addAttribute("condition",condition);
		model.addAttribute("objPage", objPage);

		return"/edu3/teaching/examResult/auditExamResult_list";
	}

	/**
	 * 成绩审核-全部审核/勾选审核
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/edu3/teaching/result/examresults-batchAudit.html", method = RequestMethod.POST)
	public void batchAuditPassExamResults(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception{

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String type         		 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String checkStatus           = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String teachType             = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String classesid             = ExStringUtils.trimToEmpty(request.getParameter("classesid"));

		String resIds			 = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		String courseIds             = ExStringUtils.trimToEmpty(request.getParameter("courseIds"));
		String gradeids			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeIds"));
		String majors  			 	 = ExStringUtils.trimToEmpty(request.getParameter("majorIds"));
		String classics				 = ExStringUtils.trimToEmpty(request.getParameter("classics"));
		String classesids			 = ExStringUtils.trimToEmpty(request.getParameter("classesIds"));
		String teachTypes			 = ExStringUtils.trimToEmpty(request.getParameter("teachTypes"));

		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		ExamSub sub               	 = examSubService.get(examSubId);    			 //考试批次
		String auditType 			 = ExStringUtils.trimToEmpty(request.getParameter("auditType"));//审核类型：通过pass，撤销cancel
		if (ExStringUtils.isNotEmpty(examSubId)){
			condition.put("examSub", examSubId);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)){
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) 	{
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus", checkStatus);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(teachType)) {	   condition.put("teachType", teachType);}
		if(ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesid", classesid);
		}

		List<String> residList = new ArrayList<String>();
		List<String> courseList = new ArrayList<String>();
		List<String> gradeList = new ArrayList<String>();
		List<String> majorList = new ArrayList<String>();
		List<String> classicList = new ArrayList<String>();
		List<String> classesList = new ArrayList<String>();
		//List<String> teachTypeList = new ArrayList<String>();

		if("checked".equals(type)){//勾选审核
			residList = Arrays.asList(resIds.split(","));
			courseList = Arrays.asList(courseIds.split(","));
			gradeList = Arrays.asList(gradeids.split(","));
			majorList = Arrays.asList(majors.split(","));
			classicList = Arrays.asList(classics.split(","));
			classesList = Arrays.asList(classesids.split(","));
			//teachTypeList = Arrays.asList(teachTypes.split(","));
		}else { //查询条件审核
			condition.put("examSubId", examSubId);
			if ("networkstudy".equals(teachType)) {
				//condition.put("teachType", "networkTeach");
				//condition.put("examCourseType", 0);
			/*	page = examInfoService.findExamInfoForExamResultsInput(condition, page);
				List<ExamInfoVo> results = page.getResult();
				for (int i = 0; i<results.size();  i++) {
					ExamInfoVo examInfoVo = results.get(i);
					residList.add(examInfoVo.getExamInfoResourceId());
				}*/
			}else if (!"networkstudy".equals(teachType)&&ExStringUtils.isNotEmpty(branchSchool)) {
				page.setPageSize(Integer.MAX_VALUE);
				//condition.put("teachType", "faceTeach");
				//condition.put("examCourseType", 1);
			}
			page = examResultsService.queryExamResultsInfoForFaceTeachType(condition, page);
			List<FaceExamResultsVo> results = page.getResult();
			for (int i = 0; i < results.size(); i++) {
				FaceExamResultsVo faceExamResultsVo = results.get(i);
				courseList.add(faceExamResultsVo.getCourseId());
				gradeList.add(faceExamResultsVo.getGradeId());
				majorList.add(faceExamResultsVo.getMajorId());
				classicList.add(faceExamResultsVo.getClassicId());
				classesList.add(faceExamResultsVo.getClassesid());
				residList.add(faceExamResultsVo.getExamInfoId());
				//teachTypeList.add(faceExamResultsVo.getTeachType());
			}
		}
		do {
			//检查数据是否正确
			if(courseList.size()<residList.size()){
				msg = "课程不能为空！";
				success = false;
				continue;
			}
			if(gradeList.size()<residList.size()){
				msg = "年级不能为空！";
				success = false;
				continue;
			}
			if(majorList.size()<residList.size()){
				msg = "专业不能为空！";
				success = false;
				continue;
			}
			/*if(classicList.size()<residList.size()){
				msg = "层次不能为空！";
				success = false;
				continue;
			}*/
			if(classesList.size()<residList.size()){
				msg = "班级不能为空！";
				success = false;
				continue;
			}
			StringBuffer message = new StringBuffer();
			if("pass".equals(auditType)){ //审核通过
				for (int i = 0; i < courseList.size(); i++) {
					ExamInfo info = examInfoService.get(residList.get(i));
					if (null!=sub &&null!=info&& "3".equals(sub.getExamsubStatus())) {
						try {

							//统考课程的全部通过
							if (teachType.contains("net")) {
								examResultsService.auditCourseExamResultsPass(info, teachType);
								//	msg              = "第"+(i+1)+"条数据:《"+info.getCourse().getCourseName()+"》成绩审核通过操作成功！<br>";
								//面授课程或面授+网络课程的全部通过
							}else {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("branchSchool", branchSchool);
								map.put("examSubId", examSubId);
								map.put("gradeid", gradeList.get(i));
								map.put("major", majorList.get(i));
								map.put("classic", classicList.get(i));
								map.put("courseId", courseList.get(i));
								map.put("classesid", classesList.get(i));
								map.put("examInfoId", residList.get(i));
								examResultsService.auditExamResultsAllPassForFactCourse(map,info,teachType);
							}
							/* 写日志表EDU_SYS_OPERATELOGS，记录谁审核了成绩 */
							UserOperationLogs userOperationLog = new UserOperationLogs();
							User currentUser = SpringSecurityHelper.getCurrentUser();
							userOperationLog.setUserName(currentUser.getCnName());
							userOperationLog.setUserId(currentUser.getResourceid());
							userOperationLog.setOperationContent("《"+ info.getCourse().getCourseName() + "》课程成绩审核通过。");
							userOperationLog.setOperationType(UserOperationLogs.PASS);
							userOperationLog.setRecordTime(new Date());
							userOperationLog.setIpaddress(request.getLocalAddr());
							userOperationLog.setModules("5");
							userOperationLogsService.persist(userOperationLog);
						} catch (Exception e) {
							logger.error("审核成绩通过出错：{}"+sub.getResourceid(),e.fillInStackTrace());
							success 			 = false;
							msg     			 = "成绩审核出错："+e.getMessage();
						}
					}else {
						success = false;
						msg   = "第"+(i+1)+"条数据："+"成绩为空或者找不到考试批次或者考试批次未关闭,请与管理员联系！";
					}
					if(success){
						msg   = "共"+courseList.size()+"条记录审核通过操作成功！";
					}
				}
			}else if ("cancel".equals(auditType)) {//撤销成绩
				for (int i = 0; i < courseList.size(); i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					if(ExStringUtils.isNotBlank(examSubId)){
						map.put("examSub", examSubId);
					}

					map.put("examInfoId", residList.get(i));
					map.put("courseId", courseList.get(i));
					if(ExStringUtils.isNotBlank(branchSchool)){
						map.put("branchSchool", branchSchool);
					}
					map.put("gradeid", gradeList.get(i));
					map.put("major", majorList.get(i));

					if(ExStringUtils.isBlank(classesList.get(i))){
						message.append("班级信息不能为空！<br>");
						continue;
					}else {
						map.put("classesid", classesList.get(i));
					}

					List<ExamResults> list = examResultsService.queryExamResultsByCondition(map);
					examResultsService.submitExamResults(list, Constants.BOOLEAN_NO, message);
					/* 写日志表EDU_SYS_OPERATELOGS，记录谁撤销了修改 */
					UserOperationLogs userOperationLog = new UserOperationLogs();
					User currentUser = SpringSecurityHelper.getCurrentUser();
					userOperationLog.setUserName(currentUser.getCnName());
					userOperationLog.setUserId(currentUser.getResourceid());
					userOperationLog.setOperationContent("《"+ list.get(0).getCourse().getCourseName() + "》课程撤销提交");
					userOperationLog.setOperationType(UserOperationLogs.REPEAL);
					userOperationLog.setRecordTime(new Date());
					userOperationLog.setIpaddress(request.getLocalAddr());
					userOperationLog.setModules("5");
					userOperationLogsService.persist(userOperationLog);
				}
				if(message.length()>0){
					success = false;
				}
				msg = message.toString()+"<br>"+"共"+courseList.size()+"条记录撤销审核成功！";
			}
		} while (false);

		condition.put("success", success);
		condition.put("msg", msg);
		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 成绩审核-审核
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/examresults-audit-pass.html")
	public void auditPassExamResults(HttpServletRequest request, HttpServletResponse response){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String operatingType         = ExStringUtils.trimToEmpty(request.getParameter("operatingType"));
		String cs_teachType          = ExStringUtils.trimToEmpty(request.getParameter("cs_teachType"));
		String pc_teachType          = ExStringUtils.defaultIfEmpty(request.getParameter("pc_teachType"),"networkstudy");
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String classesid			 = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		//String examCourseType		 = ExStringUtils.trimToEmpty(request.getParameter("examCourseType"));
		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		ExamSub sub               	 = examSubService.get(examSubId);    			 //考试批次
		ExamInfo info                = examInfoService.get(examInfoId);

		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesid", classesid);
		}

		if (null!=sub &&null!=info&& "3".equals(sub.getExamsubStatus())) {
			try {
				//全部通过
				if ("all".equals(operatingType)) {
					//网络课程的全部通过
					if ("networkstudy".equals(pc_teachType)) {
						examResultsService.auditCourseExamResultsPass(info, cs_teachType);
						msg              = "《"+info.getCourse().getCourseName()+"》成绩审核通过操作成功！";

						//面授课程或面授+网络课程的全部通过
					}else if(!"networkstudy".equals(pc_teachType)&&ExStringUtils.isNotEmpty(pc_teachType)&& condition.containsKey("branchSchool")&&
							condition.containsKey("gradeid")&&condition.containsKey("major")&&condition.containsKey("classic")&&condition.containsKey("courseId")) {

						//condition.put("examInfoId", info.getResourceid());
						examResultsService.auditExamResultsAllPassForFactCourse(condition,info,cs_teachType);
						msg              = "《"+info.getCourse().getCourseName()+"》成绩审核通过操作成功！";
					}

					//个别审核
				}else {
					examResultsService.auditExamResultsSingle(resultsIds, request, info, cs_teachType);
					msg              = "审核操作成功！";
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.PASS,"成绩审核：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			} catch (Exception e) {
				logger.error("审核成绩通过出错：{}"+sub.getResourceid(),e.fillInStackTrace());
				success 			 = false;
				msg     			 = "成绩审核出错："+e.getMessage();
			}
		}else {
			success = false;
			msg     = "成绩为空或者找不到考试批次或者考试批次未关闭,请与管理员联系！";
		}

		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 成绩审核-审核不通过(废弃)
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/examresults-audit-unpass.html")
	public void auditUnPassExamResults(HttpServletRequest request, HttpServletResponse response){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		ExamSub sub               	 = examSubService.get(examSubId);    			 //要导入成绩的考试批次
		User curUser 	 			 = SpringSecurityHelper.getCurrentUser();		 //当前用户
		Date curTime            	 = new Date();                                	 //当前时间

		if (null!=resultsIds && null!=sub) {
			try {
				for (int i = 0; i < resultsIds.length; i++) {
					//审核意见
					String checkNotes       = ExStringUtils.defaultIfEmpty(request.getParameter("checkNotes"+resultsIds[i]), "");
					ExamResults examResults = examResultsService.get(resultsIds[i]);
					examResults.setCheckStatus("2");
					examResults.setAuditDate(curTime);
					examResults.setAuditMan(curUser.getUsername());
					examResults.setAuditManId(curUser.getResourceid());
					examResults.setCheckNotes(checkNotes);
					examResultsService.update(examResults);
				}
			} catch (Exception e) {
				logger.error("审核成绩不通过出错：{}"+sub.getResourceid(),e.fillInStackTrace());
				success = false;
				msg     = "审核成绩不通过时服务器异常："+e.fillInStackTrace();
			}
		}else {
			success = false;
			msg     = "成绩为空或者找不到考试批次,请与管理员联系！";
		}
		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 成绩复审-课程列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/review-list.html")
	public String examResultsReviewList(HttpServletRequest request, ModelMap model, Page page){

		page.setOrderBy("examCourseCode");
		page.setOrder(Page.ASC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examCourseCode 		 = ExStringUtils.trimToEmpty(request.getParameter("examCourseCode"));

		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(examCourseCode)) {
			condition.put("examCourseCode", examCourseCode);
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			ExamSub examSub          = examSubService.get(examSubId);
			model.addAttribute("examSub", examSub);
			condition.put("examSubId", examSubId);

			page = examInfoService.findExamInfoForExamReview(condition, page);
		}

		model.addAttribute("page", page);
		model.addAttribute("condition", condition);

		return "/edu3/teaching/examResult/examResultReview_course_list";
	}
	/**
	 * 成绩复审-成绩列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/review-examresults-list.html")
	public String examResultsReviewExamresultsList(HttpServletRequest request, ModelMap model, Page page){

		page.setOrderBy("examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo asc");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String auditStatus           = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));

		if (ExStringUtils.isBlank(auditStatus)) {
			auditStatus = "0";
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(auditStatus)) {
			condition.put("auditStatus", auditStatus);
		}
		condition.put("auditType",0);
		if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(examSubId)) {
			ExamSub examSub          = examSubService.get(examSubId);
			ExamInfo info            = examInfoService.get(examInfoId);
			model.addAttribute("examSub", examSub);
			model.addAttribute("info", info);
			page 				     = examResultsAuditService.findExamResultsAuditByCondition(condition, page);
		}

		model.addAttribute("page", page);
		model.addAttribute("condition", condition);

		return "/edu3/teaching/examResult/examResultReview_results_list";
	}
	/**
	 * 成绩复审
	 * @param request
	 * @param response
	 */

	@RequestMapping("/edu3/teaching/examresult/review.html")
	public void examResultsReview(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();
		String [] ids    			 = request.getParameterValues("resourceid");
		String flag      			 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String examSubId 			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfId 			 = ExStringUtils.trimToEmpty(request.getParameter("examInfId"));
		ExamSub sub      			 = examSubService.get(examSubId);    			 //考试批次
		boolean success  			 = true;                                         //操作结果
		String  msg      			 = "";                                           //返回的消息
		try {
			if (null!=ids && ids.length>0 && ExStringUtils.isNotBlank(flag)&&null!=sub&& "3".equals(sub.getExamsubStatus())) {
				examResultsAuditService.examResultsReview(ids, flag, request);
				msg                  = ids.length+"条成绩复审通过!";
			}else {
				success 	 		 = false;
				msg     	 		 = "未选择审核成绩记录，或者考试批次未关闭不允许操作！";
			}
		} catch (Exception e) {
			success 	 			 = false;
			msg     	 			 = "操作出错："+e.getMessage();
		}
		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 发布成绩-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/publish-examresults-list.html")
	public String publishExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("course.courseName");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId 			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String courseName			 = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
		String checkStatus           = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		User curUser 				 = SpringSecurityHelper.getCurrentUser();

		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus", checkStatus);
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			objPage = examResultsService.findExamResultByCondition(condition,objPage);
		}
		model.addAttribute("condition",condition);
		model.addAttribute("objPage", objPage);

		return"/edu3/teaching/examResult/publishExamResult_list";
	}
	/**
	 * 发布成绩-提醒信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/publish-examresults-remindinfo.html")
	public void publishExamResultsRemindInfo(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId             = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"),"");
		ExamSub examSub              = examSubService.get(examSubId);
		String msg                   = "";
		boolean success              = false;
		try {
			if (null!=examSub && !"Y".equals(examSub.getIsLocked())) {
				condition.put("examSub", examSub);
				condition.put("examSubId", examSub.getResourceid());
				msg          			 = examResultsService.getPublishRemindInfoByExamSubId(condition);
				success                  = true;
			}else {
				msg          			 = "当前考试批次已锁定或参数不合法！";
				success                  = false;
			}
		} catch (Exception e) {
			logger.error("获取发布批次成绩前的统计信息出错,批次ID：{}"+examSubId+e.fillInStackTrace());
			msg                      = "获取当前批次成绩概况出错"+e.fillInStackTrace();
			success                  = false;
		}
		if (condition.containsKey("examSub")) {
			condition.remove("examSub");
		}
		condition.put("success",success);
		condition.put("msg",msg);
		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 发布成绩-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/publish-examresults-save.html")
	public void publishExamResultsSave(HttpServletRequest request, HttpServletResponse response){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String teachType             = ExStringUtils.defaultIfEmpty(request.getParameter("teachType"),"networkstudy");
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));

		ExamInfo info                = examInfoService.get(examInfoId);              //要发布成绩的考试课程
		ExamSub examSub              = examSubService.get(examSubId);	   			 //要发布成绩的考试批次

		User curUser 	 			 = SpringSecurityHelper.getCurrentUser();		 //当前用户
		Date curTime                 = ExDateUtils.getCurrentDateTime();			 //当前时间
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		int counts                   = 0 ;
		String returnAttId    		 = "";


		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}

		if (!"networkstudy".equals(teachType)) {
			int examCourseType   	 = teachType.contains("face")?1:0;
			List<ExamInfo> infos 	 = examInfoService.findByCriteria(Restrictions.eq("examSub.resourceid", examSubId),Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
			if (null!=infos && !infos.isEmpty()) {
				info            	 = infos.get(0);
			}
		}

		if ( null != examSub &&"3".equals(examSub.getExamsubStatus()) &&!"Y".equals(examSub.getIsLocked())&&null!=info&&ExStringUtils.isNotEmpty(teachType)) {

			try {
				condition.put("examInfoId", info.getResourceid());
				boolean isAllowPublish= examResultsService.isAllowPublishedExamInfosResults(info,teachType,condition);

				if (isAllowPublish) {
					counts            = examResultsService.publishedExamResultsByExamInfo(info,teachType,condition);
				}
				if (counts<=0) {
					success 		  = false;
					msg    			  = "《"+info.getCourse().getCourseName()+"》没有符合发布条件的成绩记录！";
				}else {
					msg    			  = "《"+info.getCourse().getCourseName()+"》成绩已发布！";
					logger.info("发布成绩：操作人："+curUser.getCnName()+" 操作人ID："+curUser.getResourceid()+" 操作时间："+ExDateUtils.formatDateStr(curTime,ExDateUtils.PATTREN_DATE_TIME)+" 发布课程："+info.getCourse().getCourseName()+" 发布课程ID："+info.getResourceid()+" 发布人数："+counts);
				}
			} catch (Exception e) {
				logger.error("发布成绩出错：课程ID{}"+info.getResourceid(),e.fillInStackTrace());
				success					  = false;
				msg     				  = "成绩发布异常："+e.getMessage();
			}
		}else {
			success 				  = false;
			msg     				  = "当前考试批次未关闭或者已锁定，不允许发布成绩！";
		}
		condition.put("returnAttId",returnAttId);
		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 重置成绩-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/reset-examresults-list.html")
	public String resetExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("examStartTime");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 	 	 = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		String gradeid			 	 = ExStringUtils.defaultIfEmpty(request.getParameter("gradeid"), "");
		String major  			 	 = ExStringUtils.defaultIfEmpty(request.getParameter("major"),"");
		String classic				 = ExStringUtils.defaultIfEmpty(request.getParameter("classic"), "");
		String name  			 	 = ExStringUtils.defaultIfEmpty(request.getParameter("name"),"");
		String studyNo				 = ExStringUtils.defaultIfEmpty(request.getParameter("studyNo"), "");
		String studentStatus 		 = ExStringUtils.defaultIfEmpty(request.getParameter("studentStatus"),"");
		String learningStyle		 = ExStringUtils.defaultIfEmpty(request.getParameter("learningStyle"), "");

		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus",studentStatus);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}

		objPage = examResultsService.findExamResultByCondition(condition, objPage);

		model.addAttribute("condition", condition);
		model.addAttribute("objPage",objPage);

		return"/edu3/teaching/examResult/resetExamResult_list";
	}
	/**
	 * 重置成绩-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/reset-examresults-save.html")
	public void resetresetExamResultsSave(HttpServletRequest request, HttpServletResponse response){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		ExamSub sub               	 = examSubService.get(examSubId);    			 //要导入成绩的考试批次
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息

		if (null!=sub) {
			try {
				for (int i = 0; i < resultsIds.length; i++) {

					ExamResults examResults = examResultsService.get(resultsIds[i]);

					String writtenScore     = ExStringUtils.defaultIfEmpty(request.getParameter("writtenScore"+resultsIds[i]), "");    //卷面成绩
					String usuallyScore     = ExStringUtils.defaultIfEmpty(request.getParameter("usuallyScore"+resultsIds[i]), "");    //平时成绩
					String integratedScore  = ExStringUtils.defaultIfEmpty(request.getParameter("integratedScore"+resultsIds[i]), ""); //综合成绩
					String examAbnormity    = ExStringUtils.defaultIfEmpty(request.getParameter("examAbnormity"+resultsIds[i]), "");   //成绩异常

					examResults.setWrittenScore(writtenScore);
					examResults.setUsuallyScore(usuallyScore);
					examResults.setIntegratedScore(integratedScore);
					examResults.setExamAbnormity(examAbnormity);

					examResultsService.update(examResults);
				}
			} catch (Exception e) {
				logger.error("重置成绩出错：{}"+sub.getResourceid(),e.fillInStackTrace());
				success = false;
				msg     = "重置成绩时服务器异常："+e.fillInStackTrace();
			}
		}else {
			success = false;
			msg     = "请选择一个考试批次！";
		}

		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 查看成绩
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresults-view.html")
	public String searchExamResultsView(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("studentInfo.studyNo");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSub 	 	 		 = ExStringUtils.defaultIfEmpty(request.getParameter("examSub"),"");
		String branchSchool 	 	 = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		String branchSchoolName 	 = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchoolName"),"");
		String gradeid			 	 = ExStringUtils.defaultIfEmpty(request.getParameter("gradeid"), "");
		String major  			 	 = ExStringUtils.defaultIfEmpty(request.getParameter("major"),"");
		String classic				 = ExStringUtils.defaultIfEmpty(request.getParameter("classic"), "");
		String name  			 	 = ExStringUtils.defaultIfEmpty(request.getParameter("name"),"");
		String studyNo				 = ExStringUtils.defaultIfEmpty(request.getParameter("studyNo"), "");
		String studentStatus 		 = ExStringUtils.defaultIfEmpty(request.getParameter("studentStatus"),"");
		String checkStatus 		     = ExStringUtils.defaultIfEmpty(request.getParameter("checkStatus"),"");
		String learningStyle		 = ExStringUtils.defaultIfEmpty(request.getParameter("learningStyle"), "");
		User curUser                 = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit                 = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unit                     = curUser.getOrgUnit();
			condition.put("isBranchSchool", "Y");
		}

		if (null!=unit) {
			branchSchool = unit.getResourceid();
		}
		if (ExStringUtils.isNotEmpty(examSub)) {
			condition.put("examSub", examSub);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(branchSchoolName)) {
			condition.put("branchSchoolName", branchSchoolName);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus",studentStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus",checkStatus);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}

		objPage = examResultsService.findExamResultByCondition(condition, objPage);

		model.addAttribute("objPage",objPage);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/examResult/examResultView_list";
	}
	/**
	 * 查询学生成绩
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/search-examresults-list.html")
	public String searchStuExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("stu.grade.yearInfo.firstYear desc, stu.studyNo ");

		Map<String,Object> condition = new HashMap<String, Object>();
		List<Object> list = new ArrayList<Object>();
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
		String learningStyle		 = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String defaultCheckAll       = ExStringUtils.trimToEmpty(request.getParameter("defaultCheckAll"));
		String graduateDate          = ExStringUtils.trimToEmpty(request.getParameter("graduateDateStr"));
		String classId 				 = ExStringUtils.trimToEmpty(request.getParameter("classId"));//班级
		String courseId				 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));//课程
		String isAllPass			 = ExStringUtils.trimToEmpty(request.getParameter("isAllPass"));//是否全部通过

		//新添加毕业确认时间设置
		String confirmGraduateDateb = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDatee = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));

		User curUser                 = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit                 = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unit                         = curUser.getOrgUnit();
			model.addAttribute("isBrschool", true);
		}

		if (null!=unit) {
			branchSchool = unit.getResourceid();
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			list.add(branchSchool);
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)){
			list.add(gradeid);
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(classic)){
			list.add(classic);
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)){
			list.add(learningStyle);
			condition.put("learningStyle",learningStyle);
		}
		if(ExStringUtils.isNotEmpty(major)){
			list.add(major);
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classId)){
			list.add(classId);
			condition.put("classId",classId);
		}
		if(ExStringUtils.isNotEmpty(studyNo)){
			//list.add(studyNo);
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)){
			//list.add(name);
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)){
			list.add(studentStatus);
			condition.put("studentStatus",studentStatus);
		}

		if(ExStringUtils.isNotEmpty(defaultCheckAll)) {
			condition.put("defaultCheckAll",defaultCheckAll);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)){
			condition.put("graduateDateStr",graduateDate);
			condition.put("graduateDate", ExDateUtils.convertToDate(condition.get("graduateDateStr").toString()));
		};

		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",courseId);
		}
		if(ExStringUtils.isNotEmpty(isAllPass)) {
			condition.put("isAllPass",isAllPass);
		}

		if(ExStringUtils.isNotEmpty(confirmGraduateDateb)) {
			condition.put("confirmGraduateDateb",confirmGraduateDateb);
		}
		if(ExStringUtils.isNotEmpty(confirmGraduateDatee)) {
			condition.put("confirmGraduateDatee",confirmGraduateDatee);
		}

		String studentHQL			 = genHQL(condition,"studentInfo");
		objPage = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, studentHQL,list.toArray());//findByHql(objPage,studentHQL,  condition);

		try {
			List<Map<String,Object>> graduateDateList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(" select distinct g.graduatedate from EDU_TEACH_GRADUATEDATA g where g.isdeleted = 0 order by g.graduatedate desc", null);
			for (Map<String,Object> m :graduateDateList) {
				Date gd = (Date)m.get("graduatedate");
				m.put("graduatedate", ExDateUtils.formatDateStr(gd, ExDateUtils.PATTREN_DATE));

			}
			model.addAttribute("graduateDateList", graduateDateList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		model.addAttribute("condition", condition);
		condition.remove("graduateDate");
		model.addAttribute("objPage",objPage);

		//获取初始班级可选域
		Map<String,Object> condition2 = new HashMap<String, Object>(0);
		if(ExStringUtils.isNotEmpty(gradeid)){
			condition2.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition2.put("brSchoolid", branchSchool);
		}
		Map<String,Object> dmap = new HashMap<String, Object>(0);
		dmap.put("isUsed", Constants.BOOLEAN_YES);
		dmap.put("isDeleted", 0);
		//学期
		String hql = "from "+Dictionary.class.getSimpleName()+" where isDeleted = :isDeleted and isUsed = :isUsed and dictCode like 'CodeExportExam_%' order by showOrder ";
		List<Dictionary> listdict = dictionaryService.findByHql(hql, dmap);
		if(null != listdict && listdict.size() > 0){
			StringBuffer termStr = new StringBuffer("");
			for(Dictionary dict : listdict)   {
				termStr.append("<input type=\"checkbox\" value=\""+dict.getDictValue()+"\" name=\"term\" title=\""+dict.getDictName()+"\">"+dict.getDictName());
			}
			model.addAttribute("terms", termStr);

		}

		// 用于判断是否显示是否使用电子签名复选框
		String schoolCode ="";
		SysConfiguration schoolCodeConfig = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode");
		if(schoolCodeConfig!=null){
			schoolCode = schoolCodeConfig.getParamValue();
		}
		model.addAttribute("schoolCode",schoolCode);
		return "/edu3/teaching/examResult/searchExamResult_list";
	}
	/**
	 * 计算总学分
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/result/calculateTotalCreditHour.html")
	public void calculateStudentTotalCreditHour(HttpServletRequest request, HttpServletResponse response) throws Exception{

		//String studentId    = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		calculateTotalCreditHour();

	}
	//手动从EXCLE中导入成绩
	private void importExamResultsByFile() throws IOException{
		/*List<ExamResults> all  = new ArrayList<ExamResults>();
		Map<String,ExamResults> rsMap = new HashMap<String, ExamResults>();
		List<ExamResults> dy1 = examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examInfo.resourceid","4aa6636736dea9fe0136e79dcb504e1f"));
		List<ExamResults> dy2 = examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examInfo.resourceid","4aa6636736ee2ad50136f16c694d350f"));
		List<ExamResults> jjsx = examResultsService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("examInfo.resourceid","4aa6636736ee2ad50136f16e69f4353a"));
		
		for (ExamResults rs : dy1) {
			if ("2".equals(rs.getExamAbnormity())&&(ExStringUtils.isNotBlank(rs.getWrittenMachineScore())||ExStringUtils.isNotBlank(rs.getWrittenMachineScore()))) {
				Integer wms = Integer.valueOf(rs.getWrittenMachineScore());
				Integer whs = Integer.valueOf(rs.getWrittenHandworkScore());
				Integer us  = Integer.valueOf(rs.getUsuallyScore());
				int  ws     = wms+whs;
				rs.setWrittenScore(String.valueOf(wms+whs));
				rs.setExamAbnormity("0");
				if (ws>us) {
					rs.setIntegratedScore(String.valueOf(ws));
				}else {
					rs.setIntegratedScore(String.valueOf((ws*0.5)+(us*0.5)));
				}
			}
			rsMap.put(ExStringUtils.trimToEmpty(rs.getStudentInfo().getStudyNo())+"4aa6636736dea9fe0136e79dcb504e1f", rs);
		}
		
		for (ExamResults rs : dy2) {
			rsMap.put(rs.getStudentInfo().getStudyNo()+"4aa6636736ee2ad50136f16c694d350f", rs);
		}
		
		for (ExamResults rs : jjsx) {
			rsMap.put(rs.getStudentInfo().getStudyNo()+"4aa6636736ee2ad50136f16e69f4353a", rs);
		}
		

		String no         = "";
		InputStream is 		  = null;
		Workbook rwb	 	  = null;
		try {
			is				  = new FileInputStream("G:\\examresults\\tal.xls");
			rwb	 	  		  = Workbook.getWorkbook(is);
			Sheet sheet 	  = rwb.getSheet(4);
			int countRow      = sheet.getRows();

			for (int i = 1; i < countRow; i++) {
				Cell studyNoCel	   	    = sheet.getCell(1,i); 
				Cell ws 		    	= sheet.getCell(3,i); 
				String studyNoStr	    = ExStringUtils.trimToEmpty(studyNoCel.getContents());
				String wsStr		    = ExStringUtils.trimToEmpty(ws.getContents());
				
				no                      = studyNoStr;
				ExamResults rs          = rsMap.get(studyNoStr+"4aa6636736dea9fe0136e79dcb504e1f");
				if (null!=rs) {

					wsStr = ExStringUtils.isBlank(wsStr)?"0":wsStr;
				
					rs.setWrittenScore(String.valueOf(Double.valueOf(wsStr)));
					rs.setExamAbnormity("0");
					rs.setCheckStatus("1");

					all.add(rs);
				}
			}
			for (int i = 1; i < countRow; i++) {
				Cell studyNoCel	   	    = sheet.getCell(1,i); 
				Cell ws1 		    	= sheet.getCell(3,i); 
				Cell ws2 		        = sheet.getCell(5,i); 
				
				String studyNoStr	    = ExStringUtils.trimToEmpty(studyNoCel.getContents());
				String ws1Str		    = ExStringUtils.trimToEmpty(ws1.getContents());
				String ws2Str		    = ExStringUtils.trimToEmpty(ws2.getContents());
				no                      = studyNoStr;
				ExamResults rs          = rsMap.get(studyNoStr+"4aa6636736dea9fe0136e79dcb504e1f");
				if (null!=rs) {
					if ("缺考".equals(ws1Str)||"缺考".equals(ws2Str)||"无卷".equals(ws1Str)||"无卷".equals(ws2Str)) {
						if ("缺考".equals(ws1Str)||"缺考".equals(ws2Str)) {
							rs.setExamAbnormity("2");
						}else {
							rs.setExamAbnormity("3");
						}
						rs.setWrittenHandworkScore(null);
						rs.setWrittenMachineScore(null);
						rs.setWrittenScore(null);
					}else {
						if (!"ERROR 15".equals(ws1Str)) {
							ws1Str = ExStringUtils.isBlank(ws1Str)?"0":ws1Str;
							ws2Str = ExStringUtils.isBlank(ws2Str)?"0":ws2Str;
							
							rs.setWrittenScore(String.valueOf(Integer.valueOf(ws1Str)+Integer.valueOf(ws2Str)));
							rs.setWrittenMachineScore(ws1Str);
							rs.setWrittenHandworkScore(ws2Str);
							rs.setCheckStatus("1");
						}
					}
					all.add(rs);
				}
			}
			if (all.size()>0) {
				examResultsService.batchSaveOrUpdate(all);
			}
			System.out.println(all.size());
		}catch (Exception e) {
			
			e.printStackTrace();
			System.out.println("---------"+no);
		}finally{
			rwb.close();
			is.close();
		}*/
	}
	private void calculateTotalCreditHour() throws FileNotFoundException,IOException, ParseException, Exception {

		List<StudentInfo> studentInfos = studentinfoservice.findByHql(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted =0  and exists ( select sk.studentId from "+StudentCheck.class.getSimpleName()+" sk where sk.studentId = stu.resourceid)");
		String filePath                = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
		File f = new File(filePath+File.separator+"system"+File.separator+"backup.data");
		//File f = new File("c:\\sql.txt");
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(("-------------statTime:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"\r\n").getBytes());
		fos.flush();
		Map<String, Map<String, Object>> electiveExamCountMap = studentExamResultsService.getElectiveExamCountMapByStuList(studentInfos);
		for (StudentInfo stu :studentInfos) {
			StudentExamResultsVo stataVo= null;
			Integer examCount = Integer.parseInt(electiveExamCountMap.get(stu.getResourceid()).get("examcount").toString());
			try {
				List<StudentExamResultsVo> list 		= studentExamResultsService.studentExamResultsList(stu,examCount);
				if (null!=list&&list.size()>0) {
					stataVo= list.get(list.size()-1);
					//String sql = " update edu_temp_studentcheck2 stu set stu.finishedcredithour="+stataVo.getTotalCredit()+",stu.finishednecesscredithour="+stataVo.getRequiredCredit()+" where stu.studyno='"+stu.getStudyNo()+"';\r\n";
					String sql = " update edu_roll_studentinfo stu set stu.finishedcredithour="+stataVo.getTotalCredit()+",stu.finishednecesscredithour="+stataVo.getRequiredCredit()+",stu.finishedneccesscoursenum="+stataVo.getCompulsoryed()+",stu.finishedoptionalcoursenum="+stataVo.getLimitedCount()+",stu.finishedmaincoursenum="+stataVo.getMainCourse()+" , stu.finishedmaincourseaveragescore="+stataVo.getMainCourseScoreAvg()+",stu.finishedbasecoursenum="+stataVo.getBaseCourse()+",stu.finishedbaseaveragescore="+stataVo.getBaseCourseScoreAvg()+" , stu.graduatepapercore="+stataVo.getThesisScore()+"  where stu.studyno='"+stu.getStudyNo()+"';\r\n";
					//byte[] b = sql.getBytes();
					//fos.write(b);
					//fos.flush();
				}
			}catch(HibernateException e){
				List<StudentExamResultsVo> list 		= studentExamResultsService.studentExamResultsList(studentinfoservice.get(stu.getResourceid()),examCount);
				if (null!=list&&list.size()>0) {
					stataVo= list.get(list.size()-1);

					//String sql = " update edu_temp_studentcheck2 stu set stu.finishedcredithour="+stataVo.getTotalCredit()+",stu.finishednecesscredithour="+stataVo.getRequiredCredit()+" where stu.studyno='"+stu.getStudyNo()+"';\r\n";
					String sql = " update edu_roll_studentinfo stu set stu.finishedcredithour="+stataVo.getTotalCredit()+",stu.finishednecesscredithour="+stataVo.getRequiredCredit()+",stu.finishedneccesscoursenum="+stataVo.getCompulsoryed()+",stu.finishedoptionalcoursenum="+stataVo.getLimitedCount()+",stu.finishedmaincoursenum="+stataVo.getMainCourse()+" , stu.finishedmaincourseaveragescore="+stataVo.getMainCourseScoreAvg()+",stu.finishedbasecoursenum="+stataVo.getBaseCourse()+",stu.finishedbaseaveragescore="+stataVo.getBaseCourseScoreAvg()+" , stu.graduatepapercore="+stataVo.getThesisScore()+"  where stu.studyno='"+stu.getStudyNo()+"';\r\n";
					//byte[] b = sql.getBytes();
					//fos.write(b);
					//fos.flush();
				}
			}catch (Exception e) {
				System.out.println("stuNo:"+stu.getStudyNo());
				continue;
			}
			//System.out.println("stuName:"+stu.getStudentName()+"stuNo:"+stu.getStudyNo()+"----totalCreditHour:"+stu.getFinishedCreditHour()+"----necessCreditHour:"+stu.getFinishedNecessCreditHour()+"----LimitedCount:"+stu.getFinishedOptionalCourseNum());
		}
		fos.write(("-------------endTime:"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"\r\n").getBytes());
		fos.flush();
		fos.close();
	}
	/**
	 * 查看某个学生成绩
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/result/view-student-examresults.html")
	public String viewStudentExamResults(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws Exception{

		String studentId           = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		String flag                = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		StudentInfo studentInfo    = studentinfoservice.get(studentId);
		User curUser               = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool         = false;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool             = true;
		}
		if (null!=studentInfo) {
			List<StudentExamResultsVo> returnList   = new ArrayList<StudentExamResultsVo>();
			List<StudentExamResultsVo> list 		= studentExamResultsService.studentExamResultsList(studentInfo,null);

			List<GraduateData> graduateDatas        = graduateDataService.findByHql("from "+GraduateData.class.getSimpleName()+" gd where gd.isDeleted = ? and gd.studentInfo.resourceid=? order by graduateDate desc",0,studentId);
			StudentExamResultsVo stataVo= null;
			//成绩List中最后一条为已修学分信息
			if (null!=list&&list.size()>0) {
				stataVo= list.get(list.size()-1);
				list.remove(list.size()-1);
			}
			String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			for (int i = 0;i<list.size();i++) {
				StudentExamResultsVo vo = list.get(i);
//				String checkStatus 	    = ExStringUtils.trimToEmpty(vo.getCheckStatusCode());
//				if (Boolean.TRUE == isBrschool&&!"4".equals(checkStatus)) {
//					continue;
//				}
				// 桂林医免修免考成绩显示合格
				if("10601".equals(schoolCode) && ("免考".equals(vo.getCheckStatus()) || "免修".equals(vo.getExamAbnormityCode()))){
					vo.setIntegratedScore("合格");
				}
				if ("pass".equals(flag)) {
					if (Constants.BOOLEAN_YES.equals(vo.getIsPass())) {
						vo.setIndex(String.valueOf(i+1));
						returnList.add(vo);
					}
					continue;
				}else if ("required".equals(flag)) {
					if ("11".equals(vo.getCourseTypeCode())) {
						vo.setIndex(String.valueOf(i+1));
						returnList.add(vo);
					}
					continue;
				}else{
					returnList.add(vo);
					continue;
				}
			}

			if(null!=stataVo) {
				returnList.add(stataVo);
			}
			String eduYear = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm(),"yyyy-MM");
			if ("2".equals(studentInfo.getGrade().getTerm())) {
				eduYear    = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm(),"yyyy-MM");
			}
			if (ExStringUtils.isNotEmpty(studentInfo.getStudentStatus())&&"16".equals(studentInfo.getStudentStatus())&&
					null!=graduateDatas&&null!=graduateDatas.get(0).getGraduateDate()) {
				eduYear   += "至"+ExStringUtils.trimToEmpty(ExDateUtils.formatDateStr(graduateDatas.get(0).getGraduateDate(),"yyyy-MM"));
			}else {
				eduYear   += "至"+ExDateUtils.formatDateStr(new Date(), "yyyy-MM");
			}
			model.addAttribute("eduYear", eduYear);
			model.addAttribute("list", returnList);
			model.addAttribute("studentInfo", studentInfo);
			//学期
			Map<String,Object> dmap = new HashMap<String, Object>(0);
			dmap.put("isUsed", Constants.BOOLEAN_YES);
			dmap.put("isDeleted", 0);
			//学期
			String hql = "from "+Dictionary.class.getSimpleName()+" where isDeleted = :isDeleted and isUsed = :isUsed and dictCode like 'CodeExportExam_%' order by showOrder ";
			List<Dictionary> listdict = dictionaryService.findByHql(hql, dmap);
			if(null != listdict && listdict.size() > 0){
				StringBuffer termStr = new StringBuffer("");
				for(Dictionary dict : listdict)   {
					termStr.append("<input type=\"checkbox\" value=\""+dict.getDictValue()+"\" name=\"person_term\" title=\""+dict.getDictName()+"\">"+dict.getDictName());
				}
				model.addAttribute("persernal_terms", termStr);

			}

		}else {
			model.addAttribute("msg", "未找到此学生的学籍记录！");
		}
		return"/edu3/teaching/examResult/studentExamResult_list";
	}

	/**
	 * 查看发布的成绩课程列表
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/view-examresultlist.html")
	public String queryPublishExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page)throws WebException {

		page.setOrderBy("examCourseCode");
		page.setOrder(Page.ASC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId 			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examCourseCode 		 = ExStringUtils.trimToEmpty(request.getParameter("examCourseCode"));
		String examInfoCourseType    = ExStringUtils.trim(request.getParameter("examInfoCourseType"));
		String isMachineexam    	 = ExStringUtils.trim(request.getParameter("isMachineexam"));

		List<String> list       	 = new ArrayList<String>();
		User curUser                 = SpringSecurityHelper.getCurrentUser();


		if (ExStringUtils.isNotEmpty(courseId)){
			list.add(courseId);
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(examSubId)){
			condition.put("examSubId", examSubId);
			model.addAttribute("examSub", examSubService.get(examSubId));
			list.add(examSubId);
		}
		if (ExStringUtils.isNotEmpty(examCourseCode)){
			condition.put("examCourseCode", examCourseCode);
		}
		if (ExStringUtils.isNotEmpty(examInfoCourseType)){
			condition.put("examInfoCourseType", examInfoCourseType);
			list.add(examInfoCourseType);
		}
		if (ExStringUtils.isNotEmpty(isMachineexam)){
			condition.put("isMachineexam", isMachineexam);
			list.add(isMachineexam);
		}
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
			list.add( curUser.getOrgUnit().getResourceid());
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			String sql = genHQL(condition, "queryPublishedResults");
			page = baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql, list.toArray(), ExamInfoVo.class);
			//condition.put("teachType","networkstudy");
			//Map<String,Object> statMap = queryExamInfoCheckStatus(page,condition);
			//model.addAttribute("statMap", statMap);
		}

		model.addAttribute("page", page);
		model.addAttribute("condition", condition);

		return "/edu3/teaching/examResult/published-ExamResults-list";
	}

	/**
	 * 查看发布的成绩列表
	 * @param request
	 * @param response
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/view-published-examresultlist.html")
	public String publishedExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws ParseException{

		Map<String,Object> condition = new HashMap<String, Object>();

		String branchSchool          = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String stuNo                 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String stuName               = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String checkStatus           = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String gradeid               = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classic               = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String major                 = ExStringUtils.trimToEmpty(request.getParameter("major"));

		User curUser 				 = SpringSecurityHelper.getCurrentUser();
		ExamInfo info                = examInfoService.get(examInfoId);

		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("isBrschool","Y");
			branchSchool             = curUser.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid",gradeid);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic",classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major",major);
		}
		if(ExStringUtils.isNotEmpty(stuNo)) {
			condition.put("studyNo",stuNo);
		}
		if(ExStringUtils.isNotEmpty(stuName)) {
			condition.put("name",stuName);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus",checkStatus);
		}
		if(ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId",examInfoId);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool",branchSchool);
		}
		condition.put("checkStatus","4");
		if ( info!=null ){
			objPage = examResultsService.findExamResultByCondition(condition,objPage);
		}

		model.addAttribute("page", objPage);
		model.addAttribute("condition",condition);

		return "/edu3/teaching/examResult/view-published-ExamResults-list";
	}
	/**
	 * 成绩统计-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/count-examresults-list.html")
	public String countExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrderBy("yearInfo.firstYear");
		objPage.setOrder(Page.DESC);

		Map<String,Object> condition = new HashMap<String, Object>();

		String yearId                = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
		String term                  = ExStringUtils.trimToEmpty(request.getParameter("term"));
		if (ExStringUtils.isNotBlank(yearId)) {
			condition.put("yearId", yearId);
		}
		if (ExStringUtils.isNotBlank(term)) {
			condition.put("term", term);
		}
		User user                    = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			condition.put("branchSchool", user.getOrgUnit().getResourceid()) ;
		}

		Page examSubPage            =  examSubService.findExamSubByCondition(condition,objPage);

		model.addAttribute("examSubPage",examSubPage);
		model.addAttribute("condition",condition);

		return"/edu3/teaching/examResult/countExamResult_list";
	}
	/**
	 * 查看某一批次各考试课程的概况
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/count-examresults-view.html")
	public String countExamResultsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){

		Map<String,Object> condition = new HashMap<String, Object>();
		List<ExamInfoVo> list        = new ArrayList<ExamInfoVo> ();
		String examSubId             = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"),"");
		try {
			User user                = SpringSecurityHelper.getCurrentUser();
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
				condition.put("branchSchool", user.getOrgUnit().getResourceid()) ;
			}

			ExamSub examSub          = examSubService.get(examSubId);
			if (null!=examSub) {
				condition.put("examSub", examSub);
				list  = examInfoService.findExamResultsVOForCountExamResult(condition);
			}else {
				model.addAttribute("msg", "请选择一个考试批次！");
			}
		} catch (Exception e) {
			logger.error("查看某一批次各考试课程的概况出错,批次ID："+examSubId+"  错误信息:{}"+e.fillInStackTrace());
			model.addAttribute("msg","服务器异常！"+e.fillInStackTrace());
		}
		model.addAttribute("examSubId", examSubId);
		model.addAttribute("list", list);
		return "/edu3/teaching/examResult/examInfoListForCountExamResults";
	}
	/**
	 * 查看单科课程成绩列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/course-examresults-view.html")
	public String courseExamResultsView(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId  			 = ExStringUtils.defaultIfEmpty(request.getParameter("examSub"), "");
		String examInfoId 			 = ExStringUtils.defaultIfEmpty(request.getParameter("examInfoId"),"");
		ExamInfo examInfo            = examInfoService.get(examInfoId);
		ExamSub examSub  			 = examSubService.get(examSubId);

		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);
		}
		if(ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}

		User user                    = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			condition.put("branchSchool", user.getOrgUnit().getResourceid()) ;
		}

		if (null!=examSub && null!=examInfo) {
			objPage =  examResultsService.findExamResultByCondition(condition, objPage);
		}else {
			model.addAttribute("msg","请选择一个考试批次！");
		}
		model.addAttribute("examSub", examSub);
		model.addAttribute("examInfo", examInfo);
		model.addAttribute("objPage", objPage);
		model.addAttribute("condition",condition);

		return"/edu3/teaching/examResult/courseExamResult_list";
	}
	/**
	 * 查看成绩分布状况
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresults-distribution-all.html")
	public String examResultsDistribution(HttpServletRequest request, HttpServletResponse response, ModelMap model){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId     		 = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"),"");

		if (ExStringUtils.isNotBlank(examSubId)) {
			condition.put("examSubId",examSubId);
		}

		User user                    = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			condition.put("branchSchool", user.getOrgUnit().getResourceid()) ;
		}

		try {
			condition 				 = examResultsService.findExamResultsDistribution(condition);
		} catch (Exception e) {
			logger.error("获取成绩分布状况出错：{}",e.fillInStackTrace());
			condition.put("success",false);
			condition.put("errorMsg", "获取成绩分布状况出错,错误信息："+e.fillInStackTrace());
		}

		model.addAttribute("examSubId",examSubId);
		model.addAttribute("success", condition.get("success"));
		model.addAttribute("errorMsg", condition.get("errorMsg"));
		model.addAttribute("resultsList",condition.get("resultsList"));
		//model.addAttribute("sectionList",condition.get("sectionList"));

		return"/edu3/teaching/examResult/examResult_distribution_all";
	}

	/**
	 * 查看成绩分布状况-详细列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresults-distribution-view.html")
	public String examResultsDistributionView(HttpServletRequest request, HttpServletResponse response, ModelMap model){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId     		 = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"),"");
		String examInfoId    		 = ExStringUtils.defaultIfEmpty(request.getParameter("examInfoId"),"");
		String scoreType     	     = ExStringUtils.trimToEmpty(request.getParameter("scoreType"));//统计份数类型,w=卷面分,i=综合分
		String [] startScore 		 = request.getParameterValues("startScore");
		String [] endScore   		 = request.getParameterValues("endScore");

		condition.put("examSubId",examSubId);
		condition.put("examInfoId", examInfoId);
		condition.put("startScore",startScore);
		condition.put("endScore",endScore);

		try {
			User user                 = SpringSecurityHelper.getCurrentUser();
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
				condition.put("branchSchool", user.getOrgUnit().getResourceid()) ;
			}
			condition = examResultsService.findExamResultsDistribution(condition);
		} catch (Exception e) {
			logger.error("获取成绩分布状况出错：{}",e.fillInStackTrace());
			condition.put("success",false);
			condition.put("errorMsg", "获取成绩分布状况出错,错误信息："+e.fillInStackTrace());
		}
		model.addAttribute("examSubId",examSubId);
		model.addAttribute("examInfoId",examInfoId);
		model.addAttribute("scoreType",scoreType);
		model.addAttribute("success", condition.get("success"));
		model.addAttribute("errorMsg", condition.get("errorMsg"));
		model.addAttribute("resultsList",condition.get("resultsList"));
		model.addAttribute("sectionList",condition.get("sectionList"));

		return"/edu3/teaching/examResult/examResult_distribution_list";
	}


	/**
	 * 统考成绩认定-列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/statexam/list.html")
	public String examinationCourseList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("isDegreeUnitExam", "Y");

		objPage = courseService.findCourseByCondition(condition,objPage);

		model.addAttribute("objPage", objPage);
		model.addAttribute("condition",condition);
		model.addAttribute("schoolCode",CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());

		return "/edu3/teaching/examResult/examination_course_list";
	}
	/**
	 * 导入统考成绩单-页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/statexam/upload-file-view.html")
	public String uploadExamInationFile(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
		return"/edu3/teaching/examResult/examination-upload-view";
	}
	/**
	 * 课程认定-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/statexam/maintain-list.html")
	public String maintainExamInationCourseList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("passtime desc,results.studentInfo.studyNo");

		Map<String,Object> condition = new HashMap<String, Object>();

		String scoreType      = ExStringUtils.trimToEmpty(request.getParameter("scoreType"));
		String passtime       = ExStringUtils.trimToEmpty(request.getParameter("passtime"));
		String courseId       = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool   = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid        = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classic        = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String major          = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String studyNo        = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String stuName        = ExStringUtils.trimToEmpty(request.getParameter("stuName"));
		String studentStatus  = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
		String learningStyle  = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String classes 		  = ExStringUtils.trimToEmpty(request.getParameter("classes"));
		String isIdented      = ExStringUtils.trimToEmpty(request.getParameter("isIdented"));

		try {
			User user = SpringSecurityHelper.getCurrentUser();
			boolean isBrschool = edumanagerService.isBrSchool(user);
			if(isBrschool) {
				branchSchool = user.getOrgUnit().getResourceid();
				model.addAttribute("unit", user.getOrgUnit());
			}

			if (ExStringUtils.isNotEmpty(stuName)) {
				condition.put("stuName", stuName);
			}
			if (ExStringUtils.isNotEmpty(scoreType)) {
				condition.put("scoreType", scoreType);
			}
			if (ExStringUtils.isNotEmpty(passtime)) {
				condition.put("passtime", passtime);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if (ExStringUtils.isNotEmpty(gradeid)) {
				condition.put("gradeid", gradeid);
			}
			if (ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if (ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if (ExStringUtils.isNotEmpty(classes)) {
				condition.put("classes", classes);
			}
			if (ExStringUtils.isNotEmpty(studyNo)) {
				condition.put("studyNo", studyNo);
			}
			if (ExStringUtils.isNotEmpty(stuName)) {
				condition.put("stuName", stuName);
			}
			if (ExStringUtils.isNotEmpty(studentStatus)) {
				condition.put("studentStatus", studentStatus);
			}
			if (ExStringUtils.isNotEmpty(learningStyle)) {
				condition.put("learningStyle", learningStyle);
			}
			if (ExStringUtils.isNotEmpty(isIdented)) {
				condition.put("isIdented", isIdented);
			}


			if (ExStringUtils.isNotEmpty(courseId)) {

				Page _objPage = stateExamResultsService.findStateResultsByCondition(condition, objPage);
				model.addAttribute("objPage", _objPage);
			}
			model.addAttribute("condition", condition);
			model.addAttribute("isBrschool", isBrschool);
		} catch (Exception e) {
			logger.error("课程认定-列表出错", e);
		}

		return"/edu3/teaching/examResult/examination_maintain_list";
	}
	/**
	 * 课程认定-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/statexam/maintain-save.html")
	public void maintainExamInationCourseSave(HttpServletRequest request, HttpServletResponse response){


		Map<String,Object> condition  = new HashMap<String,Object>();
		boolean success               = true;
		String msg                    = "";
		int statusCode = 200;
		String stateExamResultsIds 	  = ExStringUtils.defaultIfEmpty(request.getParameter("stateExamResultsIds"), "");
		String courseId               = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		//认定过程：如果导入的学位外语课程与学生的教学计划的学位外语不一致，提示认定不通过，不进行认定的字段更新
		String studentid = ExStringUtils.trimToEmpty(request.getParameter("studentids"));
		List<String> arrayStateExamResultsIds= new ArrayList<String>();
		if(ExStringUtils.isNotBlank(stateExamResultsIds)){
			for(String tmp:stateExamResultsIds.split("\\,")){
				arrayStateExamResultsIds.add(tmp);
			}
		}
		if(ExStringUtils.isNotBlank(studentid)){
			String[] studentids = studentid.split("\\,");
			condition.put("studentid", Arrays.asList(studentids));
			List<StateExamResultsVo> listStaVo = stateExamResultsService.findStateExamResultsMismatching(condition);
			condition.remove("studentid");
			if(listStaVo!=null&&listStaVo.size()>0){
				msg += Tools.colorStr("以下学生认定失败：", "red");
				for(int i=0 ;i<listStaVo.size();i++){//移除教学计划与学位外语不匹配的学生
					String tmp =listStaVo.get(i).getStateExamid();
					if(arrayStateExamResultsIds.contains(tmp)) {
						arrayStateExamResultsIds.remove(tmp);
					}
					msg+="<br>"+listStaVo.get(i).getStudyNo()+"&nbsp;&nbsp;"+listStaVo.get(i).getStudentName()+"<br>学位外语课程为："+listStaVo.get(i).getCourseName();
					msg+=Tools.dashLine("red", 1);
				}
				statusCode = 300;
			}
		}
		try {
			if(arrayStateExamResultsIds.size()>0&&arrayStateExamResultsIds!=null){
				stateExamResultsService.maintainExamInationCourseSave(arrayStateExamResultsIds,courseId);
			}
		} catch (Exception e) {
			success  				  = false;
			msg      				  = "课程认定出错："+e.fillInStackTrace();
			logger.error("更新统考成绩出错,ID:"+stateExamResultsIds+"：{}"+e.fillInStackTrace());
		}

		condition.put("success", success);
		condition.put("msg", msg);
		condition.put("statusCode", statusCode);
		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 课程认定-取消认定
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/statexam/maintain-del.html")
	public void maintainExamInationCourseDel(HttpServletRequest request, HttpServletResponse response){

		List<String> ids              = new ArrayList<String>();
		Map<String,Object> condition  = new HashMap<String,Object>();
		boolean success               = false;
		String msg                    = "";
		String courseId               = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String stateExamResultsIds 	  = ExStringUtils.defaultIfEmpty(request.getParameter("stateExamResultsIds"), "");
		String sql   				  = "update EDU_TEACH_STATEXAMRESULTS results set results.isIdented='N' where results.resourceid in(:stateExamResultsIds)";

		try {
			Course course  			 			=  courseService.get(courseId);
			//获取统考课程   可以替代哪些课程
			Map<String,String> stateCourse      = new HashMap<String, String>();
			Map<String,String> replaceCourse    = new HashMap<String, String>();
			List<Dictionary> dict  			    = CacheAppManager.getChildren("CodeStateExamResultsReplaceCourse");
			//从数据字典中获取统考课程 课程名，用于跟导入的成绩单中做匹配(字典中排序为-1的选项为 统考课程)
			for (Dictionary d:dict ) {
				if (-1==d.getShowOrder()) {
					String  stateCourseName     = d.getDictName();   //统考课程名       
					String  stateCourseId       = d.getDictValue();  //统考课程ID    
					stateCourse.put(stateCourseName, stateCourseId);
				}else {
					String  replaceCourseId     = d.getDictValue();  //统考课程代替的课程ID 
					String [] rci               = replaceCourseId.split("_");
					String oldReplaceCourseId   = ExStringUtils.defaultIfEmpty(replaceCourse.get(rci[0]), "");
					String stateCourseId        = rci[0];
					replaceCourse.put(stateCourseId, oldReplaceCourseId+","+rci[1]);
				}
			}
			//如果当前认定的是有代替课程的统考课程，则将代替的课程也操行认定操作
			if (stateCourse.containsKey(course.getCourseName())) {
				String studentIds               = "";
				String replaceCourseId          = replaceCourse.get(course.getResourceid());
				String [] rcids                 = ExStringUtils.isBlank(replaceCourseId)?new String[]{""}:replaceCourseId.split(",");
				/*List<StateExamResults> list1    = stateExamResultsService.findByCriteria(Restrictions.in("resourceid",stateExamResultsIds.split(","))); 
				for (StateExamResults rs : list1) {
					 studentIds          	   +=  ","+rs.getStudentInfo().getResourceid();
					 rs.setIsIdented(Constants.BOOLEAN_NO);
				
				}
				stateExamResultsService.batchSaveOrUpdate(list1);
				List<StateExamResults> list2    = stateExamResultsService.findByCriteria(Restrictions.in("studentInfo.resourceid",studentIds.split(",")),Restrictions.in("course.resourceid",rcids)); 
				for (StateExamResults rs : list2) {
					 rs.setIsIdented(Constants.BOOLEAN_NO);
				}
				stateExamResultsService.batchSaveOrUpdate(list2);*/
				success=true;
			}else {

				for (String id :stateExamResultsIds.split(",")) {
					ids.add(id);
				}
				condition.put("stateExamResultsIds",ids);
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql,condition);
				success=true;
			}
			
			
			/*List<StateExamResults> list    = stateExamResultsService.findByCriteria(Restrictions.in("resourceid",stateExamResultsIds.split(","))); 
			List<StudentCheck> checks      = new ArrayList<StudentCheck>();
			for (StateExamResults rs:list) {
				StudentCheck check = new StudentCheck();
				check.setStudentId(rs.getStudentInfo().getResourceid());
				checks.add(check);
			}
			studentCheckService.batchSaveOrUpdate(checks);*/

		} catch (Exception e) {
			success= false;
			msg      = "课程认定出错："+e.fillInStackTrace();
			logger.error("更新统考成绩出错,ID:"+stateExamResultsIds+"：{}"+e.fillInStackTrace());

		}

		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 导入已审核的成绩单-页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/transcripts/upload-checked-transcripts-view.html")
	public String uploadCheckedTranscriptsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String examSubId = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"), "");
		model.addAttribute("examSubId",examSubId);
		return"/edu3/teaching/examResult/checked-transcripts-upload-view";
	}

	/**
	 * 网上考试卷面成绩查询
	 */
	@RequestMapping("/edu3/teaching/examresults/writtenscore/list.html")
	public String examResultsWrittenScoreList(HttpServletRequest request, ModelMap model, Page objPage) throws WebException {
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("examResults.examInfo.examSub.yearInfo.firstYear desc,examResults.course.courseName,examResults.studentInfo.studyNo");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSub = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));

		if(ExStringUtils.isNotBlank(examSub)){
			condition.put("examSub", examSub);
		}
		if(ExStringUtils.isNotBlank(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(name)){
			condition.put("name", name);
		}
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			User user = SpringSecurityHelper.getCurrentUser();
			condition.put("teacherId", user.getResourceid());
		}
		condition.put("onlineExam", Constants.BOOLEAN_YES);
		condition.put("notEmptyWrittenScore", Constants.BOOLEAN_YES);

		if(ExStringUtils.isNotBlank(examSub)){
			Page page = examResultsService.findExamResultByCondition(condition, objPage);
			model.addAttribute("examResultsPage", page);
		}
		model.addAttribute("condition", condition);
		return "/edu3/teaching/examResult/online-examresults-list";
	}
	/**
	 * 成绩调试列表
	 * @param examSubId
	 * @param courseId
	 * @param reques
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examinfo/adjust/list.html")
	public String examResultDebugList(String examSubId, String courseId, HttpServletRequest reques, ModelMap model, Page objPage){

		objPage.setOrderBy("course.courseCode");
		objPage.setOrder(Page.ASC);

		String infoId            	 = ExStringUtils.trimToEmpty(reques.getParameter("examInfoId"));
		Map<String,Object> condition = new HashMap<String, Object>();

		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId",examSubId);
			if(ExStringUtils.isNotBlank(courseId)){
				condition.put("courseId",courseId);
			}
			if (ExStringUtils.isNotBlank(infoId)) {
				condition.put("examInfoId",infoId);
			}
			Page page = examInfoService.findExamInfoByCondition(condition, objPage);
			model.addAttribute("examInfoPage",page);
			model.addAttribute("condition",condition);

			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				List<String> ids = new ArrayList<String>();
				for (Object obj : page.getResult()) {
					ExamInfo examInfo = (ExamInfo)obj;
					ids.add(examInfo.getResourceid());
				}
				String examInfoId = ExStringUtils.join(ids, ",");
				String [] startScore = new String []{"60","0"};
				String [] endScore = new String []{"100","59"};

				Map<String,Object> map = new HashMap<String, Object>();
				map.put("examSubId",examSubId);
				map.put("examInfoId", examInfoId);
				map.put("startScore",startScore);
				map.put("endScore",endScore);

				try {
					map = examResultsService.findExamResultsDistribution(map);
					List list = (List)map.get("resultsList");
					if(ExCollectionUtils.isNotEmpty(list)){
						Map<String, Object> examInfoList = new HashMap<String, Object>();
						for (Object object : list) {
							Map info = (Map)object;
							examInfoList.put(info.get("RESOURCEID").toString(), info);
						}
						model.addAttribute("examInfoList",examInfoList);
					}
				} catch (Exception e) {
					logger.error("获取成绩情况出错：{}",e.fillInStackTrace());
				}
			}
		}
		return "/edu3/teaching/examResult/examresults-adjust-list";
	}
	/**
	 * 调整成绩
	 * @param examInfoId
	 * @param adjustLine
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examinfo/adjust/save.html")
	public void adjustExamResults(String examInfoId, String adjustLine, HttpServletRequest request, HttpServletResponse response){
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(examInfoId)&&ExStringUtils.isNotBlank(adjustLine)){
				examResultsService.adjustExamResults(examInfoId,adjustLine);
				map.put("statusCode", 200);
				map.put("message", "成功调整成绩！");
			}
		} catch (Exception e) {
			logger.error("调整成绩出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "调整成绩出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 验证调整后及格率
	 * @param examInfoId
	 * @param adjustLine
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/teaching/examresults/adjust/ajax.html")
	public void ajaxAdjustExamResults(String examInfoId, String adjustLine, HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> condition  =  new HashMap<String,Object>();
		if(ExStringUtils.isNotBlank(examInfoId) && ExStringUtils.isNotBlank(adjustLine)){
			//考试成绩SQL
			String examScoreSQL	= "select results.EXAMINFOID,results.STUDENTID,results.WRITTENSCORE,results.USUALLYSCORE,results.EXAMABNORMITY from EDU_TEACH_EXAMRESULTS results where results.isDeleted=0 and results.EXAMINFOID=:examInfoId ";
			condition.put("examInfoId", examInfoId);
			try {
				List scoreList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examScoreSQL,condition);
				int totalOrderNum = 0;
				int passNum = 0;
				if(ExCollectionUtils.isNotEmpty(scoreList)){
					totalOrderNum = scoreList.size();
					Double adjustLineScore = Double.valueOf(adjustLine);
					Double adjustB = 36 - adjustLineScore;
					for (Object object : scoreList) {
						Map<String,Object> scoreMap	 = (Map<String,Object>)object;
						String examAbnormity = scoreMap.get("EXAMABNORMITY").toString();
						String writtenScore = null==scoreMap.get("WRITTENSCORE")?"":scoreMap.get("WRITTENSCORE").toString();
						String studentId = null==scoreMap.get("STUDENTID")?"":scoreMap.get("STUDENTID").toString();
						String usuallyScore = null==scoreMap.get("USUALLYSCORE")?"":scoreMap.get("USUALLYSCORE").toString();
						if(ExStringUtils.isNotBlank(studentId)&&ExStringUtils.isNotBlank(writtenScore)&&"0".equals(examAbnormity)){//正常成绩
							Double score = Double.parseDouble(ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentId,writtenScore));
							if(score+adjustB>0){
								Double tempScore = BigDecimalUtil.div(Math.sqrt(score+adjustB)*10, 1.0, 0);
								if(tempScore>score && tempScore<100){
									score = tempScore;
								}
							}
							if(ExStringUtils.isBlank(usuallyScore) && score>=55 && score<60){//在55~59分之间且无平时成绩
								score = 55.0;
							}
							if(score>=60.0){//及格
								passNum++;//及格人数加1
							}
						}
					}
					Double passNumRatio = 0.0==totalOrderNum?0.0:BigDecimalUtil.div(passNum*100.0, totalOrderNum, 1); //及格率
					condition.put("passNumRatio", passNumRatio+"%");
				}
			} catch (Exception e) {
			}
		}
		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 网络课程成绩录入-课程列表
	 * @param request
	 * @param response
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping(value={"/edu3/teaching/examresults/networkstudy-course-list.html","/edu3/teaching/examresults/networkstudy-course-list_us.html"})
	public String netWorkStudyExamResultList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){
		page.setOrderBy("examCourseCode");
		page.setOrder(Page.ASC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examCourseCode 		 = ExStringUtils.trimToEmpty(request.getParameter("examCourseCode"));
		User user                    = SpringSecurityHelper.getCurrentUser();
		String unitId 				 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		Date curDate = new Date();
		Integer timeStatus = 0 ;
		boolean isadmin=false;
		if(SpringSecurityHelper.isUserInRole("ROLE_ADMINISTRATOR")||SpringSecurityHelper.isUserInRole("ROLE_ADMIN")){
			isadmin=true;
			model.addAttribute("isadmin", "Y");
		}
		if(!"".equals(unitId)){
			condition.put("unitId", unitId);
		}
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			unitId =	user.getOrgUnit().getResourceid();
			condition.put("unitId", unitId);
		}
		String resultsCourseIds 	 = "";
		boolean isBrSchool = false;
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			isBrSchool = true ;
			model.addAttribute("isBrSchool", "Y");
		}else{
			//不是学习中心教务员就认为是可以总管所有学习中心的管理人员
			isadmin=true;
			model.addAttribute("isadmin", "Y");
		}
		boolean isTeacher 			 = false;
		String isAbnormityInput      = "";
		Date curTime                 = new Date();
		String type 				 = ExStringUtils.trimToEmpty(request.getParameter("type"));

		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(examCourseCode)) {
			condition.put("examCourseCode", examCourseCode);
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			ExamSub examSub          = examSubService.get(examSubId);
			Date usualScoreInputStartTime = examSub.getUsualScoreInputStartTime();
			Date usualScoreInputEndTime   = examSub.getUsualScoreInputEndTime();
			Date examInputStartTime       = examSub.getExaminputStartTime();
			Date examInputEndTime         = examSub.getExaminputEndTime();
			if("normal".equals(type)){
				if(null!=usualScoreInputStartTime&&curDate.before(usualScoreInputStartTime)){
					timeStatus = 1;
				}
				if(null!=usualScoreInputEndTime&&curDate.after(usualScoreInputEndTime)){
					timeStatus = 2;
				}
				if(null!=examInputEndTime&&curDate.after(examInputEndTime)){
					timeStatus = 2;
				}
			}else{
				if(null!=examInputStartTime&&curDate.before(examInputStartTime)){
					timeStatus = 1;
				}
				if(null!=examInputEndTime&&curDate.after(examInputEndTime)){
					timeStatus = 2;
				}
			}


			String teacherCode       = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();
			String abnormityInput    = CacheAppManager.getSysConfigurationByCode("examresultsAbnormityInput").getParamValue();//异常成绩录入人角色编码
			isTeacher                = user.isContainRole(teacherCode);
			isAbnormityInput         = user.isContainRole(abnormityInput)==true?"Y":"N";

			model.addAttribute("isAbnormityInput",isAbnormityInput);
			/*if(isTeacher){	
				condition.put("curUserId", user.getResourceid());
				String courseHQL     = genHQL(condition,"TeachTask");	
				
				List<Object> examResultsCourseIds = teachtaskservice.findByHql(TeachTask.class,courseHQL,examSub.getYearInfo().getResourceid(),examSub.getTerm());
				if(null!=examResultsCourseIds && !examResultsCourseIds.isEmpty()){
					for (int i = 0; i < examResultsCourseIds.size(); i++) {
						resultsCourseIds+= ","+"'"+examResultsCourseIds.get(i)+"'";
					}
					resultsCourseIds     = resultsCourseIds.substring(1);
				}
				if (ExStringUtils.isNotEmpty(resultsCourseIds)){
					condition.put("resultsCourseIds", resultsCourseIds);
				} else {
					condition.put("resultsCourseIds", "''");
				}
				model.addAttribute("isTeacher","Y");
			}*/

			//是否允许录入成绩
			if (curTime.getTime()>=examSub.getExaminputStartTime().getTime()&&
					curTime.getTime()<=examSub.getExaminputEndTime().getTime()&&
					"3".equals(examSub.getExamsubStatus())) {
				model.addAttribute("isAllowInputExamResults","Y");
			}
			if (curTime.getTime()>=examSub.getUsualScoreInputStartTime().getTime()&&
					curTime.getTime()<=examSub.getUsualScoreInputEndTime().getTime()&&
					"3".equals(examSub.getExamsubStatus())) {
				model.addAttribute("isAllowInputUsualExamResults","Y");
			}

			model.addAttribute("examSub", examSub);
			condition.put("examSubId", examSubId);
			if("normal".equals(type)){
				condition.put("flag", "1");
			}
			page = examInfoService.findExamInfoForExamResultsInput(condition, page);


		}
		if(ExStringUtils.isNotEmpty(type)){
			condition.put("type", type);
		}
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		model.addAttribute("timeStatus",timeStatus);
		return "/edu3/teaching/examResult/netWorkStudyExamResults-Course-list";
	}


	/**
	 * 面授课程成绩录入-教学计划列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresults/facestudy-plan-list.html")
	public String faceStudyExamResultPlanList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){
		objPage.setOrderBy("gp.grade.yearInfo.firstYear desc,gp.grade.term desc,gp.teachingPlan.classic,gp.teachingPlan.major.majorCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式

		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String examSubId 			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String gradeid 				 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String classesId			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		User curUser  				 = SpringSecurityHelper.getCurrentUser();

		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		condition.put("ispublished", Constants.BOOLEAN_YES);
		//condition.put("schoolType", "face");//网络成人直属

		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
		}
		if(ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(gradeid)){
			Page page = teachingGuidePlanService.findTeachingGradeByCondition(condition, objPage);
			model.addAttribute("gradeGuidePlanList", page);
		}

		model.addAttribute("condition", condition);
		return "/edu3/teaching/examResult/facestudyExamResults-Plan-list";
	}
	/**
	 * 面授课程成绩录入-课程列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/examresults/facestudy-course-list.html")
	public String faceStudyExamResultCourseList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws ParseException{

		objPage.setOrderBy("term");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
//		Map<String,Object> inputTotalNum = new HashMap<String,Object>();//需要录入的人数
		String examSubId			 = request.getParameter("examSubId");
		String guidPlanId 			 = request.getParameter("guidPlanId");
		String courseId 			 = request.getParameter("courseId");
		String teachType 			 = "facestudy" ;
		String msg 					 = "";
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotEmpty(guidPlanId)) {
			condition.put("guidPlanId", guidPlanId);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(teachType)) {
			condition.put("teachType", teachType);
		}

		if(ExStringUtils.isNotBlank(examSubId)&& ExStringUtils.isNotBlank(guidPlanId)){

			Grade curGrade           = null;
			ExamSub examSub 		 = examSubService.get(examSubId);
			Date curTime 			 = new Date();
			Date endTime 		     = examSub.getEndTime();

			TeachingGuidePlan guidePlan = teachingGuidePlanService.get(guidPlanId);

			List<Grade> gradeList    = gradeService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("yearInfo.resourceid", examSub.getYearInfo().getResourceid()),Restrictions.eq("term", examSub.getTerm()));
			if (null!=gradeList&&gradeList.isEmpty()) {
				curGrade             = gradeService.getDefaultGrade();
			}else {
				curGrade			 = gradeList.get(0);
			}


			model.addAttribute("guidePlan", guidePlan);
			model.addAttribute("examSub", examSub);

			if (curTime.after(endTime)){
				User curUser 		 = SpringSecurityHelper.getCurrentUser();
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
					condition.put("isBranchSchool", "Y");
					condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
				}
				condition.put("teachingPlanId", guidePlan.getTeachingPlan().getResourceid());
				int beforeTerm = Integer.valueOf(StudentCourseOrderUtil.getAllowOrderCourseTerm(curGrade,guidePlan.getGrade()));

				if (beforeTerm<=0) {
					msg        = "所选年级不允许在"+examSub.getBatchName()+"中录入成绩！";
				}

				condition.put("beforeTerm", beforeTerm);

				condition.put("gradeid", guidePlan.getGrade().getResourceid());
				condition.put("isDeleted",0);
				condition.put("studentStatus1", "11");
				condition.put("studentStatus2", "21");
				condition.put("isPass",Constants.BOOLEAN_YES);
				/*try {
					//查询需要录入的人数
					queryInputTotalNum(condition,inputTotalNum);
					//Long total = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong("select count(s.resourceid) from edu_roll_studentinfo s where s.isdeleted=0 and s.teachplanid=:teachingPlanId and s.branchschoolid=:branchSchool and s.gradeid=:gradeid", condition);
					//model.addAttribute("total", total);		
				} catch (Exception e) {
					
				}*/

//				Page page 			 = teachingPlanCourseService.findTeachingPlanCourseByCondition(condition, objPage);
				Page page			 = teachingJDBCService.findTeachingPlanClassCourseByCondition(condition, objPage);
				model.addAttribute("teachingPlanCourseList", page);

				condition.put("isAllowInputExamResults", Constants.BOOLEAN_YES);//允许录入
			} else {
				msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong>"
						+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);

			}
		} else {
			msg = "请选择一个考试批次和年级";
		}
		condition.put("msg", msg);
		model.addAttribute("condition", condition);
//		model.addAttribute("inputTotalNum", inputTotalNum);
		return "/edu3/teaching/examResult/facestudyExamResults-Course-list";
	}
	/**
	 * 面授课程成绩录入-课程列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/examresults/facestudy-course-list_newedition.html")
	public String faceStudyExamResultCourseListNew(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws ParseException{

		//objPage.setOrderBy("term,pc.resourceid||'_'||classes.resourceid");
		objPage.setOrderBy("unit.unitcode,g.gradename,c.resourceid,classes.teachingtype,m.majorcode,classes.classesname,term,course.coursecode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
//		objPage.setPageSize(50);
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
//		Map<String,Object> inputTotalNum = new HashMap<String,Object>();//需要录入的人数
		String fromPage 			 = ExStringUtils.trimToEmpty(request.getParameter("fromPage"));
		String examSubId			 = request.getParameter("examSubId");
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		// 教学形式
		String courseTeachType = ExStringUtils.trimToEmpty(request.getParameter("courseTeachType"));
//		if(ExStringUtils.isBlank(examSubId)){
//			Grade curGrade   = gradeService.getDefaultGrade();//默认年级
//			if(curGrade!=null){
//				List<ExamSub> list1 = examSubService.findByHql(" " +
//						"from "+ExamSub.class.getSimpleName()+" where isDeleted=? and yearInfo.resourceid=? and term=?  ", 0,curGrade.getYearInfo().getResourceid(),curGrade.getTerm());
//				if(list1.size()>0)examSubId=list1.get(0).getResourceid(); 
//			}
//		
//		}
		String teachType 			 = "facestudy" ;
		String msg 					 = "";
		String classesId             = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String gradeId 				 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic					 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String learningStyle					 = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String teachingtype					 = ExStringUtils.trimToEmpty(request.getParameter("teachingtype"));// 学习形式
		String resultStatus					 = ExStringUtils.trimToEmpty(request.getParameter("resultStatus"));
		String examClassType					 = ExStringUtils.trimToEmpty(request.getParameter("examClassType"));//考核方式

		if(ExStringUtils.isNotEmpty(examSubId)) { condition.put("examSubId", examSubId);}
		if(ExStringUtils.isNotBlank(teachType)) { condition.put("teachType", teachType);}
		if(ExStringUtils.isNotEmpty(gradeId)){condition.put("gradeId", gradeId);}
		if(ExStringUtils.isNotEmpty(classesId)){condition.put("classesId", classesId);}
		if(ExStringUtils.isNotEmpty(major)){condition.put("major", major);}
		if(ExStringUtils.isNotEmpty(courseId)){condition.put("courseId", courseId);}
		if(ExStringUtils.isNotEmpty(classic)){condition.put("classic", classic);}
		if(ExStringUtils.isNotEmpty(learningStyle)){condition.put("learningStyle", learningStyle);}
		if(ExStringUtils.isNotEmpty(teachingtype)){condition.put("teachingtype", teachingtype);}
		if(ExStringUtils.isNotEmpty(resultStatus)){condition.put("resultStatus", resultStatus);}
		if(ExStringUtils.isNotEmpty(examClassType)){condition.put("examClassType", examClassType);}
		if(ExStringUtils.isNotEmpty(courseTeachType)){
			condition.put("courseTeachType", courseTeachType);
		}

		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		//是否是学校中心人员或教学中心人员  Y/N ： 是/否 
//		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
//		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("isAllow", "Y");
			condition.put("isSchoolCenterTeacher", "N");
			condition.put("isBranchSchool", "Y");
			condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
		}else if (SpringSecurityHelper.isTeachingCentreTeacher(curUser)){
			condition.put("isAllow", "Y");
			condition.put("isSchoolCenterTeacher", "Y");
		}else if("UNIT_ROOT".equals(curUser.getOrgUnit().getUnitCode())){//成教院
			condition.put("isAllow", "Y");
			condition.put("isSchoolCenterTeacher", "N");
			condition.put("isBranchSchool", "N");
		}else {
			condition.put("isAllow", "N");
			condition.put("isSchoolCenterTeacher", "N");
		}

		if(ExStringUtils.isNotBlank(examSubId)){
			//Grade curGrade           = null;
			ExamSub examSub 		 = examSubService.get(examSubId);
			Date curTime 			 = new Date();
			//Date endTime 		     = examSub.getEndTime();
			//不再沿用预约截止时间作为成绩录入的开始时间，而使用【成绩录入开始时间】examinputStartTime 2016年09月19日  by lion
			Date endTime = examSub.getExaminputStartTime();
			/*List<Grade> gradeList    = gradeService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("yearInfo.resourceid", examSub.getYearInfo().getResourceid()),Restrictions.eq("term", examSub.getTerm()));
			if (null!=gradeList&&gradeList.isEmpty()) {
				curGrade             = gradeService.getDefaultGrade();
			}else {
				curGrade			 = gradeList.get(0);
			}*/

			model.addAttribute("examSub", examSub);

			if (curTime.after(endTime)){


				//考试批次对应的年份
//				Long subYear    = 1000L;
//				try {
//					String term = null==curGrade?"1":curGrade.getTerm();
//					if ("2".equals(term)) {
//						subYear = curGrade.getYearInfo().getFirstYear()+1;
//					}else {
//						subYear = curGrade.getYearInfo().getFirstYear();
//					}
//				} catch (Exception e) {
//					e.fillInStackTrace();
//					subYear	 = 1000L;
//				}
				//考试批次对应的学期
				String subTerm  = examSub.getTerm();
				if(ExStringUtils.isNotEmpty(subTerm)){
					condition.put("subTerm",subTerm);
				}
				Long subYear = examSub.getYearInfo().getFirstYear();
				condition.put("subYear",subYear);

				if(ExStringUtils.isNotEmpty(classesId)){
					condition.put("classesId", classesId);
				}

				// 判断是否顶级机构人员
				boolean isTop = false;
				List<User> users = userService.findByHql("from "+User.class.getSimpleName()+" u where u.orgUnit.unitCode=?", "UNIT_ROOT");
				for (int i = 0; i < users.size(); i++) {
					User user = users.get(i);
					if (user.getResourceid().equals(curUser.getResourceid())) {
						isTop = true;
						break;
					}
				}

				if (!isTop) {
					// 判断是否教务员
					boolean isJwy = false;
					boolean isCjcx = false;//查询成绩角色
					Set<Role> roles = curUser.getRoles();
					for (Role role : roles) {
						if ("ROLE_BRS_STUDENTSTATUS".equals(role.getRoleCode())) {
							isJwy = true;
							break;
						}else if ("ROLE_queryscore".equals(role.getRoleCode())) {
							isCjcx = true;
						}
					}
					if (isJwy || isCjcx) {
						if(isCjcx && "N".equals(condition.get("isAllow"))){
							condition.put("isAllow", "N");
						}else {// 教务员录入自己负责的教学站班级的课程成绩
							condition.put("jwyId", curUser.getResourceid());
						}

					} else {
						// 登分老师录入自己负责的班级的课程成绩
						condition.put("teachId", curUser.getResourceid());
					}
					if(null!=curUser.getOrgUnit()){
						if(!SpringSecurityHelper.isTeachingCentreTeacher(curUser)){
							branchSchool = curUser.getOrgUnit().getResourceid();
						}
					}

				}
				//如果已经选择了学习中心，就要过滤出相应的专业
				if(ExStringUtils.isNotEmpty(branchSchool)){
					condition.put("unitId", branchSchool);
					condition.put("branchSchool", branchSchool);
				}

				/*condition.put("isDeleted",0);
				condition.put("studentStatus1", "11");
				condition.put("studentStatus2", "21");
				condition.put("isPass",Constants.BOOLEAN_YES);*/

				// 只显示这些状态的学生
				condition.put("studentStatuses", "'11','21','25'");
				condition.put("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
				//20181015  因为系统中已经有统考成绩录入，因此在这个成绩录入处进行过滤 2162
				condition.put("filterExamClassType", "3");
				objPage = teachingJDBCService.findTeachingPlanClassCourseByCondition(condition, objPage);

				condition.put("isAllowInputExamResults", Constants.BOOLEAN_YES);//允许录入
				model.addAttribute("teachingPlanCourseList", objPage);
			} else {
				msg     = "不在成绩录入时间范围内，不允许录入成绩！<br/><strong>该批次开始录入时间:</strong>"
						+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);

			}
		} else {
			if("Y".equals(fromPage)){
				msg = "请选择一个考试批次";
			}
		}
		condition.put("msg", msg);
		if (!condition.containsKey("resultStatus")) {
			condition.put("resultStatus","");
		}
		model.addAttribute("condition", condition);
//		model.addAttribute("inputTotalNum", inputTotalNum);

//		List<ExamSub> examSubs = examSubService.findByHql(" from "+ExamSub.class.getSimpleName()+" where isDeleted = '0' and batchType='exam' and examType='N' order by examinputStartTime desc ");
//		model.addAttribute("examSubs",examSubs); 

		//	model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(gradeId,major,"query_examresults_major","major","examResultMajorClick()",classic));
		//	model.addAttribute("classesSelect4",graduationQualifService.getGradeToMajorToClasses1(gradeId,major,classesId,branchSchool,"searchExamResult_classesid","classesId",classic));
//		if(ExStringUtils.isNotEmpty(branchSchool))condition.put("branchSchool", branchSchool);

		//是否全部提交成绩才允许查看打印
		String isallsubmit = CacheAppManager.getSysConfigurationByCode("examresult.isallsubmit").getParamValue();
		model.addAttribute("isallsubmit", isallsubmit);
		model.addAttribute("user", curUser);
		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		return "/edu3/teaching/examResult/facestudyExamResults-Course-list2";
	}
	/**
	 * 查询需要录入的人数
	 * @param condition
	 * @throws Exception
	 */
	public void queryInputTotalNum(Map<String, Object> condition,Map<String, Object> inputTotalNum) throws Exception {
		StringBuffer sql = new StringBuffer("select pc.resourceid||'_'||stu.classesid resourceid,count(stu.resourceid) counts from edu_teach_plancourse pc  ");
		sql.append(" inner join edu_roll_studentinfo stu on stu.teachplanid = pc.planid and (stu.studentstatus = :studentStatus1 or stu.studentstatus = :studentStatus2) and stu.isdeleted =:isDeleted ");
		//sql.append("  and stu.gradeid =:gradeid    ");		
		if(condition.containsKey("branchSchool")){
			sql.append(" and stu.branchschoolid =:branchSchool ");
		}
		//sql.append(" where pc.isdeleted = :isDeleted and pc.planid = :teachingPlanId ");
		sql.append(" where pc.isdeleted = :isDeleted  ");
		if (condition.containsKey("courseId")) {
			sql.append(" and pc.courseid = :courseId ");
		}
		if (condition.containsKey("teachType")) {
			sql.append(" and pc.teachtype = :teachType ");
		}
		sql.append("  and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =pc.courseid and score.ispass = :isPass )");
		if("N".equals(CacheAppManager.getSysConfigurationByCode("examsfYorN").getParamValue())){
			sql.append(" and not exists(select * from EDU_TEACH_NOEXAM re where re.isDeleted=0 and re.STUDENTID=stu.resourceid and re.checkStatus='1'  and re.COURSEID=pc.COURSEID ) ");

		}

		sql.append(" group by pc.resourceid,stu.classesid ");
		List<Map<String,Object>> totalList =   baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
		for (Map<String,Object> total : totalList) {
			inputTotalNum.put(String.valueOf(total.get("resourceid")), ExStringUtils.isNotBlank(total.get("counts").toString())?total.get("counts").toString():"0");
		}
	}

	/**
	 * 提交在线录入成绩
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/input-examresults-submit.html")
	public void submitExamResults(String resourceid, String flag, HttpServletRequest request, HttpServletResponse response) throws WebException {

		Map<String ,Object> map 	   = new HashMap<String, Object>();
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		if(ExStringUtils.isNotEmpty(unitId)){
			map.put("unitId", unitId);
		}
		try {
			if(ExStringUtils.isNotBlank(resourceid)&&ExStringUtils.isNotBlank(flag)){
				map.put("examInfoId", resourceid);
				List<ExamResults> list = examResultsService.findExamResultsByCondition(map);
				//统考提交时计算综合成绩
				List<ExamResults> upadateIntegrateScore = new ArrayList<ExamResults>(0);
				for (ExamResults examResults : list) {
					ExamInfo info = examResults.getExamInfo();
					String usuallyScore = ExStringUtils.trimToEmpty(examResults.getUsuallyScore());
					String writtenScore = ExStringUtils.trimToEmpty(examResults.getWrittenScore());
					String writtenHandwork = ExStringUtils.trimToEmpty(examResults.getWrittenHandworkScore());
					Map<String,Object> data  = caculateIntegratedScoreForNetStudy(info, usuallyScore, writtenScore, writtenHandwork, examResults);
					if(null!=data.get("integratedScore")){
						examResults.setIntegratedScore(data.get("integratedScore").toString());
						upadateIntegrateScore.add(examResults);
					}
				}
				examResultsService.batchSaveOrUpdate(upadateIntegrateScore);
				examResultsService.submitExamResults(list, flag,null);

				map.put("statusCode", 200);
				map.put("message", "提交成功！");
			}
		} catch (Exception e) {
			logger.error("提交成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));

	}

	/**
	 * 撤销提交在线录入成绩
	 * @param examInfoResourceId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/input-examresults-cancelsubmit.html")
	public void cancelSubmitExamResults(String examInfoResourceId, String courseName, HttpServletRequest request,
										HttpServletResponse response) throws WebException {

		Map<String ,Object> map 	   = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer();
		String classesid = request.getParameter("classId");

		try {
			if(ExStringUtils.isNotBlank(examInfoResourceId)){
				if(ExStringUtils.isMessyCode(courseName)){
					courseName = ExStringUtils.getEncodeURIComponentByOne(courseName);
				}
				map.put("examInfoId", examInfoResourceId);
				if (ExStringUtils.isNotBlank(classesid)) {
					map.put("classesid", classesid);
				}
				List<ExamResults> list = examResultsService.findExamResultsByCondition(map);
				//String startDate = ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME_MILLI);
				examResultsService.submitExamResults(list, "unSumbit",message);
				//String endDate = ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME_MILLI);
				map.put("success", 200);
				map.put("msg", message.toString());
				//System.out.println("撤销成绩耗时："+startDate+"\t"+endDate);

				/* 写日志表EDU_SYS_OPERATELOGS，记录谁撤销了修改 */
				UserOperationLogs userOperationLog = new UserOperationLogs();
				User currentUser = SpringSecurityHelper.getCurrentUser();
				userOperationLog.setUserName(currentUser.getCnName());
				userOperationLog.setUserId(currentUser.getResourceid());
				userOperationLog.setOperationContent("《"+ courseName + "》课程撤销提交。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
				userOperationLog.setOperationType(UserOperationLogs.REPEAL);
				userOperationLog.setRecordTime(new Date());
				userOperationLog.setIpaddress(request.getLocalAddr());
				userOperationLog.setModules("5");
				userOperationLogsService.persist(userOperationLog);
			}
		} catch (Exception e) {
			logger.error("撤销提交成绩失败:{}",e.fillInStackTrace());
			map.put("success", 300);
			map.put("msg", message.toString());
		}

		renderJson(response, JsonUtils.mapToJson(map));

	}

	/**
	 * 撤销提交在线录入成绩(面授和网络面授)
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/input-examresults-cancelsubmit2.html")
	public void cancelSubmitExamResults2(HttpServletRequest request,
										 HttpServletResponse response) throws WebException {

		Map<String ,Object> map 	   = new HashMap<String, Object>();

		try {
			Map<String,Object> condition = new HashMap<String, Object>();
			String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			String teachType             = ExStringUtils.defaultIfEmpty(request.getParameter("teachType"),"networkstudy");
			String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
			String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
			String classesid			 = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
			if (ExStringUtils.isNotEmpty(examSubId)){
				condition.put("examSub", examSubId);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(branchSchool)){ condition.put("branchSchool", branchSchool);}

			if (ExStringUtils.isNotEmpty(gradeid)) {
				condition.put("gradeid", gradeid);
			}
			if (ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if (ExStringUtils.isNotEmpty(classic)){
				condition.put("classic", classic);
			}
			if (ExStringUtils.isNotEmpty(classesid)){
				condition.put("classesid", classesid);
			}

			int examCourseType   	 = teachType.contains("face")?1:0;
			List<ExamInfo> infos 	 = examInfoService.findByCriteria(Restrictions.eq("examSub.resourceid", examSubId),Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
			if (null!=infos && !infos.isEmpty()) {
				ExamInfo info  = infos.get(0);

				List<ExamResults> list = examResultsService.queryExamResultsByCondition(condition);
				StringBuffer stringBuffer = new StringBuffer();
//				String startDate = ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME_MILLI);
				examResultsService.submitExamResults(list, "unSumbit", stringBuffer);
//				String endDate = ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME_MILLI);
				map.put("success", 200);
				map.put("msg", stringBuffer.toString());
//				System.out.println("撤销成绩耗时："+startDate+"\t"+endDate);

				/* 写日志表EDU_SYS_OPERATELOGS，记录谁撤销了修改 */
				UserOperationLogs userOperationLog = new UserOperationLogs();
				User currentUser = SpringSecurityHelper.getCurrentUser();
				userOperationLog.setUserName(currentUser.getCnName());
				userOperationLog.setUserId(currentUser.getResourceid());
				userOperationLog.setOperationContent("《"+ info.getCourse().getCourseName() + "》课程撤销提交。参数："+Condition2SQLHelper.getConditionFromResquestByIterator(request));
				userOperationLog.setOperationType(UserOperationLogs.REPEAL);
				userOperationLog.setRecordTime(new Date());
				userOperationLog.setIpaddress(request.getLocalAddr());
				userOperationLog.setModules("5");
				userOperationLogsService.persist(userOperationLog);
			}


		} catch (Exception e) {
			logger.error("撤销成绩失败:{}",e.fillInStackTrace());
			map.put("success", 300);
			map.put("msg", "撤销成绩失败<br/>"+e.getMessage());
		}


		renderJson(response, JsonUtils.mapToJson(map));

	}

	/**
	 * 提交平时成绩
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/input-usexamresults-submit.html")
	public void submitUsExamResults(String resourceid, String flag, HttpServletRequest request, HttpServletResponse response) throws WebException {

		Map<String ,Object> map 	   = new HashMap<String, Object>();
		try {
			String unitId = "";
			User curUser = SpringSecurityHelper.getCurrentUser();
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
				unitId                     = curUser.getOrgUnit().getResourceid();
				map.put("unitId", unitId);
			}
			if(ExStringUtils.isEmpty(unitId)){
				unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
				if(ExStringUtils.isNotEmpty(unitId)){
					map.put("unitId", unitId);
				}
			}
			if(ExStringUtils.isNotBlank(resourceid)&&ExStringUtils.isNotBlank(flag)){
				map.put("examInfoId", resourceid);
				List<ExamResults> list = examResultsService.findExamResultsByCondition(map);

				examResultsService.submitUsExamResults(list, flag);

				map.put("statusCode", 200);
				map.put("message", "提交成功！");
			}
		} catch (Exception e) {
			logger.error("提交平时成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));

	}
	/**
	 * 指定教师成绩录入-课程列表
	 * @param request
	 * @param response
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresults/teacher-course-list.html")
	public String ExamResultInputCourseList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){
		page.setOrderBy("examCourseCode");
		page.setOrder(QueryParameter.ASC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examCourseCode 		 = ExStringUtils.trimToEmpty(request.getParameter("examCourseCode"));
		User user                    = SpringSecurityHelper.getCurrentUser();
		String unitId 				 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		Date curDate = new Date();
		Integer timeStatus = 0 ;
		boolean isadmin=false;
		if(SpringSecurityHelper.isUserInRole("ROLE_ADMINISTRATOR")||SpringSecurityHelper.isUserInRole("ROLE_ADMIN")){
			isadmin=true;
			model.addAttribute("isadmin", "Y");
		}
		if(!"".equals(unitId)){
			condition.put("unitId", unitId);
		}
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			unitId =	user.getOrgUnit().getResourceid();
			condition.put("unitId", unitId);
		}
		String resultsCourseIds 	 = "";
		boolean isBrSchool = false;
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			isBrSchool = true ;
			model.addAttribute("isBrSchool", "Y");
		}else{
			//不是学习中心教务员就认为是可以总管所有学习中心的管理人员
			isadmin=true;
			model.addAttribute("isadmin", "Y");
		}
		boolean isTeacher 			 = false;
		String isAbnormityInput      = "";
		Date curTime                 = new Date();
		String type 				 = ExStringUtils.trimToEmpty(request.getParameter("type"));


		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(examCourseCode)) {
			condition.put("examCourseCode", examCourseCode);
		}


		if(!"edumanager".equals(user.getUserType())){
			model.addAttribute("condition", condition);
			model.addAttribute("message","你没有权限进行登分操作！");
			return "/edu3/teaching/examResult/examResults-teacher-Course-list";
		}else{
			condition.put("teacherInput", user.getResourceid());
		}
		if (ExStringUtils.isNotEmpty(examSubId)) {
			ExamSub examSub          = examSubService.get(examSubId);
			Date usualScoreInputStartTime = examSub.getUsualScoreInputStartTime();
			Date usualScoreInputEndTime   = examSub.getUsualScoreInputEndTime();
			Date examInputStartTime       = examSub.getExaminputStartTime();
			Date examInputEndTime         = examSub.getExaminputEndTime();
			if("normal".equals(type)){
				if(null!=usualScoreInputStartTime&&curDate.before(usualScoreInputStartTime)){
					timeStatus = 1;
				}
				if(null!=usualScoreInputEndTime&&curDate.after(usualScoreInputEndTime)){
					timeStatus = 2;
				}
				if(null!=examInputEndTime&&curDate.after(examInputEndTime)){
					timeStatus = 2;
				}
			}else{
				if(null!=examInputStartTime&&curDate.before(examInputStartTime)){
					timeStatus = 1;
				}
				if(null!=examInputEndTime&&curDate.after(examInputEndTime)){
					timeStatus = 2;
				}
			}


			String teacherCode       = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();
			String abnormityInput    = CacheAppManager.getSysConfigurationByCode("examresultsAbnormityInput").getParamValue();//异常成绩录入人角色编码
			isTeacher                = user.isContainRole(teacherCode);
			isAbnormityInput         = user.isContainRole(abnormityInput)==true?"Y":"N";

			model.addAttribute("isAbnormityInput",isAbnormityInput);
			/*if(isTeacher){	
				condition.put("curUserId", user.getResourceid());
				String courseHQL     = genHQL(condition,"TeachTask");	
				
				List<Object> examResultsCourseIds = teachtaskservice.findByHql(TeachTask.class,courseHQL,examSub.getYearInfo().getResourceid(),examSub.getTerm());
				if(null!=examResultsCourseIds && !examResultsCourseIds.isEmpty()){
					for (int i = 0; i < examResultsCourseIds.size(); i++) {
						resultsCourseIds+= ","+"'"+examResultsCourseIds.get(i)+"'";
					}
					resultsCourseIds     = resultsCourseIds.substring(1);
				}
				if (ExStringUtils.isNotEmpty(resultsCourseIds)){
					condition.put("resultsCourseIds", resultsCourseIds);
				} else {
					condition.put("resultsCourseIds", "''");
				}
				model.addAttribute("isTeacher","Y");
			}*/

			//是否允许录入成绩
			if (curTime.getTime()>=examSub.getExaminputStartTime().getTime()&&
					curTime.getTime()<=examSub.getExaminputEndTime().getTime()&&
					"3".equals(examSub.getExamsubStatus())) {
				model.addAttribute("isAllowInputExamResults","Y");
			}
			if (curTime.getTime()>=examSub.getUsualScoreInputStartTime().getTime()&&
					curTime.getTime()<=examSub.getUsualScoreInputEndTime().getTime()&&
					"3".equals(examSub.getExamsubStatus())) {
				model.addAttribute("isAllowInputUsualExamResults","Y");
			}

			model.addAttribute("examSub", examSub);
			condition.put("examSubId", examSubId);
			if("normal".equals(type)){
				condition.put("flag", "1");
			}
			page = examInfoService.findExamInfoForExamResultsInput(condition, page);


		}
		if(ExStringUtils.isNotEmpty(type)){
			condition.put("type", type);
		}
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		model.addAttribute("timeStatus",timeStatus);
		return "/edu3/teaching/examResult/examResults-teacher-Course-list";
	}

	/*
	 * 用随堂练习分数替换平时分
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/replace-score.html")
	public void replaceAtOrdinaryTimesPoints(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws Exception{
		objPage.setOrderBy("unit.unitcode,major.majorcode,stu.studyno ");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = new HashMap<String, Object>();
		String rtmsg = "替换平时成绩成功!";
		Map<String,Object> map = new HashMap<String, Object>(); //存放返回信息
		String examSubId 				= 		ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String studentStatus1 			= 		ExStringUtils.trimToEmpty(request.getParameter("studentStatus1"));
		String studentStatus2 			= 		ExStringUtils.trimToEmpty(request.getParameter("studentStatus2"));
		String teachplanid 				= 		ExStringUtils.trimToEmpty(request.getParameter("teachplanid"));
		String classesId 				= 		ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String teachType 				= 		ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String courseId 				= 		ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String isPass 					= 		ExStringUtils.trimToEmpty(request.getParameter("isPass"));
		String teachingPlanCourseId 	= 		ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String branchschoolid			=		ExStringUtils.trimToEmpty(request.getParameter("branchschoolid"));
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		if(ExStringUtils.isNotEmpty(studentStatus1)) {
			condition.put("studentStatus1",studentStatus1);
		}
		if(ExStringUtils.isNotEmpty(studentStatus2)) {
			condition.put("studentStatus2",studentStatus2);
		}
		if(ExStringUtils.isNotEmpty(teachplanid)) {
			condition.put("teachplanid",teachplanid);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId",classesId);
		}
		if(ExStringUtils.isNotEmpty(teachType)) {
			condition.put("teachType",teachType);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",courseId);
		}
		if(ExStringUtils.isNotEmpty(isPass)) {
			condition.put("isPass",isPass);
		}
		if(ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId",teachingPlanCourseId);
		}
		if(ExStringUtils.isNotEmpty(branchschoolid)) {
			condition.put("branchschoolid",branchschoolid);
		}

		if (ExStringUtils.isNotBlank(examSubId)){ //查出要替换的学生信息
			List<ExamResultsVo> listExam = new ArrayList<ExamResultsVo>();
			try {
				List<ExamResultsVo> resultList = new ArrayList<ExamResultsVo>();
				ExamSub examSub 			 = examSubService.get(examSubId);
				Date curTime             = new Date();
				Date endTime  = null==examSub.getEndTime()?new Date():examSub.getEndTime();
				if (curTime.after(endTime)){
					objPage 	  = teachingJDBCService.findFaceStudyExamResultsVo(objPage, condition);
				}
				if(null != objPage) {
					resultList = objPage.getResult();
				}
				if(null != resultList && resultList.size() > 0){ //替换平时分
					for (int i = 0; i < resultList.size(); i++) {
						ExamResultsVo vo = resultList.get(i);
						String studentid = null != vo ? vo.getStuId() : "";
						String status = vo.getCheckStatus(); //成绩状态
						if(!"1".equals(status) && !"2".equals(status)&& !"3".equals(status)&& !"4".equals(status)){//没有提交/待审核/审核通过/发布的成绩才要被替换

							List<Map<String,Object>> list = learningJDBCService.toCalculate(studentid, courseId);
							Double _d1 = 0D; //随堂练习分数
							if(null!= list && list.size() > 0){ //获取随堂练习分数
								Map<String,Object> m = list.get(0);
								_d1 = Double.parseDouble(null != m.get("result") ? (m.get("result")+"") : "0");
								if(_d1 > 100D) {
									_d1 = 100D;
								}
							}
							String _d2_str = vo==null?"":vo.getUsuallyScore();
							Double _d2 = Double.parseDouble(ExStringUtils.isNotBlank(_d2_str) ? _d2_str : "0"); //平时分
							if(_d1 > _d2 && _d1 != 0D){ //如果平时成绩为空 或者小于随堂练习成绩才要被替换
								vo.setUsuallyScore(_d1+"");
								//查找成绩id
								condition.clear();
								condition.put("stuid", studentid);
								condition.put("isDeleted", 0);
								condition.put("courseid", courseId);
								condition.put("examsubId", examSubId);
								List<ExamResults> listresult = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted =:isDeleted and studentInfo.resourceid = :stuid and course.resourceid = :courseid and examsubId = :examsubId order by fillinDate desc "
										, condition);
								if(null!=listresult&&listresult.size()>0){
									vo.setExamResultsResourceId(listresult.get(0).getResourceid());
								}
								listExam.add(vo);
							}
						}
					}
				}
				if(null != listExam && listExam.size() > 0){ // 开始更新/插入成绩
					condition.clear();
					String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL	
					updateExamPoints(listExam, examSubId, teachingPlanCourseId, 60D, examCountHQL, SpringSecurityHelper.getCurrentUser());
				}else{
					rtmsg = "没有学生的平时成绩需要替换!";
				}
			} catch (Exception e) {
				logger.debug("替换平时分出错！");
				rtmsg = "替换平时成绩出错!";
				e.printStackTrace();
			}
		}
		map.put("rtmsg", rtmsg);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/*
	 * 刷新网上学习成绩
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/updateonlinescore.html")
	public void updateOnlineScore(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws Exception{
		objPage.setOrderBy("unit.unitcode,major.majorcode,stu.studyno ");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = new HashMap<String, Object>();
		String rtmsg = "刷新网上学习成绩成功!";
		Map<String,Object> map = new HashMap<String, Object>(); //存放返回信息
		String examSubId 				= 		ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String studentStatus1 			= 		ExStringUtils.trimToEmpty(request.getParameter("studentStatus1"));
		String studentStatus2 			= 		ExStringUtils.trimToEmpty(request.getParameter("studentStatus2"));
		String teachplanid 				= 		ExStringUtils.trimToEmpty(request.getParameter("teachplanid"));
		String classesId 				= 		ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String teachType 				= 		ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String courseId 				= 		ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String isPass 					= 		ExStringUtils.trimToEmpty(request.getParameter("isPass"));
		String teachingPlanCourseId 	= 		ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String branchschoolid			=		ExStringUtils.trimToEmpty(request.getParameter("branchschoolid"));
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		if(ExStringUtils.isNotEmpty(studentStatus1)) {
			condition.put("studentStatus1",studentStatus1);
		}
		if(ExStringUtils.isNotEmpty(studentStatus2)) {
			condition.put("studentStatus2",studentStatus2);
		}
		if(ExStringUtils.isNotEmpty(teachplanid)) {
			condition.put("teachplanid",teachplanid);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId",classesId);
		}
		if(ExStringUtils.isNotEmpty(teachType)) {
			condition.put("teachType",teachType);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",courseId);
		}
		if(ExStringUtils.isNotEmpty(isPass)) {
			condition.put("isPass",isPass);
		}
		if(ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId",teachingPlanCourseId);
		}
		if(ExStringUtils.isNotEmpty(branchschoolid)) {
			condition.put("branchschoolid",branchschoolid);
		}

		if (ExStringUtils.isNotBlank(examSubId)){ //查出要刷新的学生信息
			List<ExamResultsVo> listExam = new ArrayList<ExamResultsVo>();
			try {
				List<ExamResultsVo> resultList = new ArrayList<ExamResultsVo>();
				ExamSub examSub 			 = examSubService.get(examSubId);
				YearInfo yearInfo = examSub.getYearInfo();
				String term = examSub.getTerm();
				Date curTime             = new Date();
				Date endTime  = null==examSub.getEndTime()?new Date():examSub.getEndTime();
				if (curTime.after(endTime)){
					objPage 	  = teachingJDBCService.findFaceStudyExamResultsVo(objPage, condition);
				}
				if(null != objPage) {
					resultList = objPage.getResult();
				}

				if(null != resultList && resultList.size() > 0){ //刷新平时分
					for (int i = 0; i < resultList.size(); i++) {
						ExamResultsVo vo = resultList.get(i);
						String studentid = null != vo ? vo.getStuId() : "";
						String status = vo.getCheckStatus(); //成绩状态
						if(!"1".equals(status) && !"2".equals(status)&& !"3".equals(status)&& !"4".equals(status)){//没有提交/待审核/审核通过/发布的成绩才要被刷新
							//查找成绩id
							condition.clear();
							condition.put("stuid", studentid);
							condition.put("isDeleted", 0);
							condition.put("courseid", courseId);
							condition.put("examsubId", examSubId);
							List<ExamResults> listresult = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted =:isDeleted and studentInfo.resourceid = :stuid and course.resourceid = :courseid and examsubId = :examsubId order by fillinDate desc "
									, condition);
							if(null!=listresult&&listresult.size()>0){
								vo.setExamResultsResourceId(listresult.get(0).getResourceid());
							}
							listExam.add(vo);
						}
					}
				}
				if(null != listExam && listExam.size() > 0){// 开始更新/插入成绩
					condition.clear();
					String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL	
					updateOnlineExamPoints(listExam, examSubId, teachingPlanCourseId, 60D, examCountHQL, SpringSecurityHelper.getCurrentUser());
				}else{
					rtmsg = "没有学生的网上学习成绩需要刷新!";
				}

				if(null != resultList && resultList.size() > 0){ //刷新平时分
					for (int i = 0; i < resultList.size(); i++) {
						ExamResultsVo vo = resultList.get(i);
						String studentid = null != vo ? vo.getStuId() : "";
						String status = vo.getCheckStatus(); //成绩状态
						if(!"1".equals(status) && !"2".equals(status)&& !"3".equals(status)&& !"4".equals(status)){//没有提交/待审核/审核通过/发布的成绩才要被刷新
//							List<Map<String,Object>> list = learningJDBCService.toCalculate(studentid, courseId);
//							Double _d1 = 0D; //随堂练习分数
//							if(null!= list && list.size() > 0){ //获取随堂练习分数
//								Map<String,Object> m = list.get(0);
//								_d1 = Double.parseDouble(null != m.get("result") ? (m.get("result")+"") : "0");	
//								if(_d1 > 100D) _d1 = 100D;
//							}
//							vo.setOnlineScore(_d1+"");
							condition.clear();
							condition.put("courseId", courseId);
							condition.put("studentId", studentid);
							List<UsualResultsVo> list = usualResultsService.findUsualResultsVoByCondition(condition);
							if(null!= list && list.size() > 0){
								UsualResultsVo m = list.get(0);
								StudentLearnPlan slPlan = studentLearnPlanService.get(m.getStudentLearnPlanId());
								if (slPlan != null) {
									boolean doUpdate = false;
									if (slPlan.getOrderExamYear() == null) {
										slPlan.setOrderExamYear(yearInfo);
										doUpdate = true;
									}
									if (ExStringUtils.isBlank(slPlan.getOrderExamTerm())) {
										slPlan.setOrderExamTerm(term);
										doUpdate = true;
									}
									if (doUpdate) {
										studentLearnPlanService.update(slPlan);
									}
								}
							}
							list = usualResultsService.findUsualResultsVoByCondition(condition);
							if(null!= list && list.size() > 0){
								UsualResultsVo m = list.get(0);
								String[] planId = {m.getStudentLearnPlanId()};
								String[] isDealed = {"Y"};
								String[] bbsResults = {ExStringUtils.isNotBlank(m.getBbsResults())?m.getBbsResults():"0"};
								String[] selftestResults = {ExStringUtils.isNotBlank(m.getSelftestResults())?m.getSelftestResults():"0"};
								String[] otherResults = {ExStringUtils.isNotBlank(m.getOtherResults())?m.getOtherResults():"0"};
								String[] faceResults = {ExStringUtils.isNotBlank(m.getFaceResults())?m.getFaceResults():"0"};
								String[] askQuestionResults = {ExStringUtils.isNotBlank(m.getAskQuestionResults())?m.getAskQuestionResults():"0"};
								String[] courseExamResults = {ExStringUtils.isNotBlank(m.getCourseExamResults())?m.getCourseExamResults():"0"};
								String[] exerciseResults = {"0"};
								if (ExStringUtils.isNotBlank(m.getExerciseResults())) {
									exerciseResults[0] = m.getExerciseResults();
								} else {
									String yearId = examSub.getYearInfo().getResourceid();
//									String term = examSub.getTerm();
									UsualResultsRule currentRule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearId,term);
//									Map<String,Object> originalResults = new HashMap<String,Object>();
//									if(null != currentRule){
//										UsualResultsController urc = new UsualResultsController();
//										urc.originalResults(originalResults, m, currentRule);//找出原始成绩
//									} else {
//										//Double originalExerciseResults = studentExerciseService.avgStudentExerciseResult(courseId, vo.getStudentId(),yearInfo,term);
//										originalResults.put(m.getStudentLearnPlanId(), new UsualResults(0.0,0.0,0.0,0.0));//找不到规则默认为0
//									}
//									exerciseResults[0] = ((UsualResults)originalResults.get(m.getStudentLearnPlanId())).getOriginalExerciseResults().toString();
									if(currentRule.getExerciseResultPer()>0 && (ExStringUtils.isBlank(m.getUsualResultsId()) ||ExStringUtils.isBlank(m.getExerciseResults()))){//作业练习分
										exerciseResults[0] = studentExerciseService.avgStudentExerciseResult(m.getCourseId(), m.getStudentId(),m.getYearInfoId(),m.getTerm()).toString();
									}
								}
								usualResultsService.batchSaveOrUpdateUsualResults(planId, isDealed, bbsResults, exerciseResults, selftestResults, otherResults, faceResults, askQuestionResults, courseExamResults);
							}
							list = usualResultsService.findUsualResultsVoByCondition(condition);
							Double _d1 = 0D; //平时分总分
							if(null!= list && list.size() > 0){ //获取平时分总分
								UsualResultsVo m = list.get(0);
								_d1 = Double.parseDouble(null != m.getUsualResults() ? (m.getUsualResults()+"") : "0");
								if(_d1 > 100D) {
									_d1 = 100D;
								}
							}
							vo.setOnlineScore(_d1+"");
							//查找成绩id
							condition.clear();
							condition.put("stuid", studentid);
							condition.put("isDeleted", 0);
							condition.put("courseid", courseId);
							condition.put("examsubId", examSubId);
							List<ExamResults> listresult = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted =:isDeleted and studentInfo.resourceid = :stuid and course.resourceid = :courseid and examsubId = :examsubId order by fillinDate desc "
									, condition);
							if(null!=listresult&&listresult.size()>0){
								vo.setExamResultsResourceId(listresult.get(0).getResourceid());
							}
							listExam.add(vo);
						}
					}
				}
				if(null != listExam && listExam.size() > 0){// 开始更新/插入成绩
					condition.clear();
					String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL	
					updateOnlineExamPoints(listExam, examSubId, teachingPlanCourseId, 60D, examCountHQL, SpringSecurityHelper.getCurrentUser());
				}else{
					rtmsg = "没有学生的网上学习成绩需要刷新!";
				}
			} catch (Exception e) {
				logger.debug("刷新网上学习成绩出错！");
				rtmsg = "刷新网上学习成绩出错!";
				e.printStackTrace();
			}
		}
		map.put("rtmsg", rtmsg);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	//更新和插入刷新网上学习成绩的记录
	private void updateOnlineExamPoints(List<ExamResultsVo> listExam, String examSubId, String teachingPlanCourseId, Double per, String examCountHQL, User user) throws ParseException{
		if (ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId) && listExam!=null && listExam.size()>0){
			ExamSub examSub              = examSubService.get(examSubId);    			 //要录入成绩的考试批次
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			Date curTime             = new Date();                                	 	 //当前时间
			Date endTime    		 = examSub.getEndTime();                 	 		//考试批次预约结束时间
			if (curTime.after(endTime)){
				Course course = planCourse.getCourse();
				String teachType = planCourse.getTeachType();
				if(ExStringUtils.isBlank(teachType)||"networkstudy".equals(teachType)){
					throw new WebException("课程"+course.getCourseName()+"教学方式不是面授或网络面授!");
				}
				Integer examCourseType = teachType.contains("face")?1:0;
				ExamInfo examInfo 	   = null;
				List<ExamInfo> list	   = examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
				if (null!=list && !list.isEmpty()) {
					examInfo = list.get(0);
				} else {
					examInfo = new ExamInfo();
					examInfo.setCourse(course);
					examInfo.setIsOutplanCourse("0");
					examInfo.setExamSub(examSub);
					examInfo.setExamCourseType(examCourseType);
					double per1 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue());
					double per2 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue());
					double per3 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue());
					if (1 == examCourseType && null != examSub.getFacestudyScorePer2()) {
						if (null != examSub.getWrittenScorePer()) {
							per = examSub.getWrittenScorePer();
						}
						if (null != examSub.getFacestudyScorePer()) {
							per1 = examSub.getFacestudyScorePer();
						}
						if (null != examSub.getFacestudyScorePer2()) {
							per2 = examSub.getFacestudyScorePer2();
						}
						if (null != examSub.getFacestudyScorePer3()) {
							per3 = examSub.getFacestudyScorePer3();
						}
						examInfo.setStudyScorePer(per);
						examInfo.setFacestudyScorePer(per1);
						examInfo.setFacestudyScorePer2(per2);
						examInfo.setFacestudyScorePer3(per3);
					} else if (2 != examCourseType) {
						if (null==examSub.getNetsidestudyScorePer()) {
							per=examSub.getNetsidestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					} else {
						if (null != examSub.getWrittenScorePer()) {
							per = examSub.getWrittenScorePer();
							per1 = examSub.getWrittenScorePer();
							per2 = 100 - per1;
							per3 = 0d;
						}
						examInfo.setStudyScorePer(per);
						examInfo.setFacestudyScorePer(per1);
						examInfo.setFacestudyScorePer2(per2);
						examInfo.setFacestudyScorePer3(per3);
					}
					if(planCourse.getScoreStyle()!=null){
						examInfo.setCourseScoreType(planCourse.getScoreStyle());
					}else {
						examInfo.setCourseScoreType(examSub.getCourseScoreType());
					}

					examInfoService.save(examInfo);
				}
				ExamResults examResults = null;

				// 获取成绩计算规则字典表
				List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
				Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
				for(Dictionary d : resultCalculateRuleList) {
					resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
				}

				for (int i = 0; i < listExam.size(); i++) {
					ExamResultsVo map = listExam.get(i);
					String studyNo = map.getStudyNo();//学号
					try {
						if(ExStringUtils.isBlank(studyNo)){
							continue;
						}
						String resultsId = ExStringUtils.trimToEmpty(map.getExamResultsResourceId()); //成绩id
						if(ExStringUtils.isNotBlank(resultsId)){
							examResults = examResultsService.get(resultsId);//要保存的成绩
						} else {
							examResults = new ExamResults();
							examResults.setCourse(course);
							examResults.setExamInfo(examInfo);
							examResults.setMajorCourseId(teachingPlanCourseId);
							//examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
							examResults.setCheckStatus("0");
							examResults.setExamsubId(examSubId);
							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
							examResults.setCourseType(planCourse.getCourseType());
							StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNo);
							examResults.setStudentInfo(studentInfo);
							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setCourseScoreType(examInfo.getCourseScoreType());
							List examCountList      = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
									examResults.getStudentInfo().getResourceid(),ExStringUtils.trimToEmpty(examResults.getResourceid()));
							long examCount          = (Long)examCountList.get(0);			         //选考次数
							examResults.setExamCount(examCount+1);
						}
						examResults.setPlanCourseTeachType(planCourse.getTeachType());
						int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
						if (checkStatus>0) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
						}
						String examAbnormity    = map.getExamAbnormity();
						String writtenScore     = map.getWrittenScore();//卷面成绩
						String usuallyScore     = map.getUsuallyScore();//平时考核成绩
						String onlineScore      = map.getOnlineScore();//网上学习成绩
						String integratedScore  = map.getIntegratedScore();//综合成绩
//						if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
//							continue;
//						}
						if (ExStringUtils.isNotEmpty(examAbnormity)) {
							examResults.setExamAbnormity(examAbnormity);
						}
						if("0".equals(examResults.getExamAbnormity())){
							if(ExStringUtils.isNotBlank(writtenScore)&&(Double.valueOf(writtenScore)<0||Double.valueOf(writtenScore)>100)
									){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩必须为0-100分");
							}
							BigDecimal w_Score	 = new BigDecimal(writtenScore);
							examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						} else {
							examResults.setWrittenScore("");
						}
						if(ExStringUtils.isNotBlank(usuallyScore)&&(Double.valueOf(usuallyScore)<0||Double.valueOf(usuallyScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的平时考核成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(usuallyScore)){
							BigDecimal u_Score	 = new BigDecimal(usuallyScore);
							examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						if(ExStringUtils.isNotBlank(onlineScore)&&(Double.valueOf(onlineScore)<0||Double.valueOf(onlineScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的网上学习成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(onlineScore)){
							BigDecimal o_Score	 = new BigDecimal(onlineScore);
							examResults.setOnlineScore((o_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						if(ExStringUtils.isNotBlank(integratedScore)&&(Double.valueOf(integratedScore)<0||Double.valueOf(integratedScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的综合成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(integratedScore)){
							BigDecimal i_Score	 = new BigDecimal(integratedScore);
							examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						examResults.setFillinDate(curTime);
						examResults.setCheckStatus("0");
						examResults.setFillinMan(user.getCnName());
						examResults.setFillinManId(user.getResourceid());
						if ("0".equals(examResults.getCheckStatus()) || "1".equals(examResults.getCheckStatus())) {
							String integratedScoreStr = examResultsService.caculateIntegrateScore(examInfo,usuallyScore,writtenScore,onlineScore,examResults,resultCalculateRuleMap);
							examResults.setIntegratedScore(integratedScoreStr);
						}
//						examResultsService.saveOrUpdate(examResults);
						if (examResults.getResourceid() == null || "".equals(examResults.getResourceid())) {
							examResultsService.save(examResults);
						} else {
							examResultsService.update(examResults);
						}

						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),teachingPlanCourseId);
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}
						studentLearnPlanService.saveOrUpdate(learnPlan);

					} catch (Exception e) {
						logger.error("保存学生成绩异常，学生学号：{}"+studyNo,e.fillInStackTrace());
						break;
					}
				}
			}else {
				logger.error("考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
						+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME));
			}
		}
	}

	//更新和插入随堂练习替换平时成绩的记录
	private void updateExamPoints(List<ExamResultsVo> listExam, String examSubId, String teachingPlanCourseId, Double per, String examCountHQL, User user) throws ParseException{
		if (ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId) && listExam!=null && listExam.size()>0){
			ExamSub examSub              = examSubService.get(examSubId);    			 //要录入成绩的考试批次
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			Date curTime             = new Date();                                	 	 //当前时间
			Date endTime    		 = examSub.getEndTime();                 	 		//考试批次预约结束时间


			if (curTime.after(endTime)){
				Course course = planCourse.getCourse();

				String teachType = planCourse.getTeachType();
				if(ExStringUtils.isBlank(teachType)||"networkstudy".equals(teachType)){
					throw new WebException("课程"+course.getCourseName()+"教学方式不是面授或网络面授!");
				}
				Integer examCourseType = teachType.contains("face")?1:0;

				ExamInfo examInfo 	   = null;
				List<ExamInfo> list	   = examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
				if (null!=list && !list.isEmpty()) {
					examInfo = list.get(0);
				} else {
					examInfo = new ExamInfo();
					examInfo.setCourse(course);
					examInfo.setIsOutplanCourse("0");
					examInfo.setExamSub(examSub);
					examInfo.setExamCourseType(examCourseType);

					if (1==examCourseType) {
						if (null!=examSub.getFacestudyScorePer()) {
							per=examSub.getFacestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					}else if(2!=examCourseType) {
						if (null==examSub.getNetsidestudyScorePer()) {
							per=examSub.getNetsidestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					}
					if(planCourse.getScoreStyle()!=null){
						examInfo.setCourseScoreType(planCourse.getScoreStyle());
					}else {
						examInfo.setCourseScoreType(examSub.getCourseScoreType());
					}


					examInfoService.save(examInfo);
				}
				ExamResults examResults = null;
				for (int i = 0; i < listExam.size(); i++) {
					ExamResultsVo map = listExam.get(i);
					String studyNo = map.getStudyNo();//学号
					try {
						if(ExStringUtils.isBlank(studyNo)){
							continue;
						}
						String resultsId = ExStringUtils.trimToEmpty(map.getExamResultsResourceId()); //成绩id
						if(ExStringUtils.isNotBlank(resultsId)){
							examResults = examResultsService.get(resultsId);//要保存的成绩
						} else {
							examResults = new ExamResults();
							examResults.setCourse(course);
							examResults.setExamInfo(examInfo);
							examResults.setMajorCourseId(teachingPlanCourseId);
							//examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
							examResults.setCheckStatus("0");
							examResults.setExamsubId(examSubId);
							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
							examResults.setCourseType(planCourse.getCourseType());
							StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNo);
							examResults.setStudentInfo(studentInfo);

							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setCourseScoreType(examInfo.getCourseScoreType());
							List examCountList      = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
									examResults.getStudentInfo().getResourceid(),ExStringUtils.trimToEmpty(examResults.getResourceid()));
							long examCount          = (Long)examCountList.get(0);			         //选考次数
							examResults.setExamCount(examCount+1);
						}
						examResults.setPlanCourseTeachType(planCourse.getTeachType());
						int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
						if (checkStatus>0) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
						}
						String writtenScore     = map.getWrittenScore();//卷面成绩
						String examAbnormity    = map.getExamAbnormity();
						String usuallyScore     = map.getUsuallyScore();//平时成绩
						String integratedScore  = map.getIntegratedScore();//综合成绩
//						if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
//							continue;
//						}
						if (ExStringUtils.isNotEmpty(examAbnormity)) {
							examResults.setExamAbnormity(examAbnormity);
						}
						if("0".equals(examResults.getExamAbnormity())){
							if(ExStringUtils.isNotBlank(writtenScore)&&(Double.valueOf(writtenScore)<0||Double.valueOf(writtenScore)>100)
									){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩必须为0-100分");
							}
							BigDecimal w_Score	 = new BigDecimal(writtenScore);
							examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						} else {
							examResults.setWrittenScore("");
						}
						if(ExStringUtils.isNotBlank(usuallyScore)&&(Double.valueOf(usuallyScore)<0||Double.valueOf(usuallyScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的平时成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(usuallyScore)){
							BigDecimal u_Score	 = new BigDecimal(usuallyScore);
							examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						if(ExStringUtils.isNotBlank(integratedScore)&&(Double.valueOf(integratedScore)<0||Double.valueOf(integratedScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的综合成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(integratedScore)){
							BigDecimal i_Score	 = new BigDecimal(integratedScore);
							examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						examResults.setFillinDate(curTime);
						examResults.setCheckStatus("0");
						examResults.setFillinMan(user.getCnName());
						examResults.setFillinManId(user.getResourceid());

//						examResultsService.saveOrUpdate(examResults);
						if (examResults.getResourceid() == null || "".equals(examResults.getResourceid())) {
							examResultsService.save(examResults);
						} else {
							examResultsService.update(examResults);
						}

						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),teachingPlanCourseId);
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}
						studentLearnPlanService.saveOrUpdate(learnPlan);

					} catch (Exception e) {
						logger.error("保存学生成绩异常，学生学号：{}"+studyNo,e.fillInStackTrace());
						break;
					}
				}
			}else {
				logger.error("考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
						+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME));
			}
		}
	}


	/**
	 * 面授/网络面授成绩录入列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/result/facestudy/input-examresults-list.html")
	public String inputFacestudyExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws ParseException{
		objPage.setOrderBy("unit.unitcode,major.majorcode,stu.studyno ");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		//String guidPlanId            = request.getParameter("guidPlanId");
		String gradeid 				 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String courseid 				 = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String branchschoolid        = ExStringUtils.trimToEmpty(request.getParameter("branchschoolid"));
		String studyNo               = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName           = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String  checkStatus          = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String msg                   = "";
		String classesId 			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String unitId 			 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		if(ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId",teachingPlanCourseId);
		}
		//if(ExStringUtils.isNotEmpty(guidPlanId))     condition.put("guidPlanId",guidPlanId);
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		if(ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName",studentName);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus",checkStatus);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId",classesId);
		}
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId",unitId);
		}
		String courseExamForm = ExStringUtils.trimToEmpty(request.getParameter("courseExamForm"));;
		String isRevokeExam = Constants.BOOLEAN_YES;// 是否允许撤销成绩
		String isMachineExam = Constants.BOOLEAN_NO;// 是否机考考试
		String courseTeachType = ExStringUtils.trimToEmpty(request.getParameter("courseTeachType"));
		TeachingPlanCourse planCourse = null;
		if (ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId)){
			//examResultsService.flush();
			ExamSub examSub 			 = examSubService.get(examSubId);
			planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			model.addAttribute("course", planCourse.getCourse());
			User curUser = SpringSecurityHelper.getCurrentUser();
			Date curTime             = new Date();
			Date endTime  = null==examSub.getEndTime()?new Date():examSub.getEndTime();
			if (curTime.after(endTime)){
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
					branchschoolid = curUser.getOrgUnit().getResourceid();
				}
				condition.put("courseId",planCourse.getCourse().getResourceid());
				condition.put("teachingPlanCourseId",teachingPlanCourseId);
				condition.put("term", examSub.getTerm());
				condition.put("yearid", examSub.getYearInfo().getResourceid());
				condition.put("teachplanid",planCourse.getTeachingPlan().getResourceid());
				condition.put("teachType", ExStringUtils.trimToEmpty(planCourse.getTeachType()));
				condition.put("courseTeachType", courseTeachType);
				if(ExStringUtils.isNotEmpty(branchschoolid)) {
					condition.put("branchschoolid",branchschoolid);
				}

				if(ExStringUtils.isNotEmpty(gradeid)){
					condition.put("gradeid", gradeid);
				}
				objPage 	  = teachingJDBCService.findFaceStudyExamResultsVo(objPage, condition);
//				StringBuilder stuIds = new StringBuilder();

//				TeachingPlanCourseStatus courseStatus = null;// 获取开课记录

				model.addAttribute("courseTeachType",courseTeachType);
			}else {
				msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
						+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);
			}
		}else {
			msg 	= "请选择一个要录入成绩的考试批次和课程！";
		}
		condition.put("msg",msg);
		//对于进入页面前，那些没有计算综合成绩且没有提交的成绩记录计算综合成绩
		List<ExamResultsVo> scoreList = objPage.getResult();

		//TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
		Course course = planCourse.getCourse();
		//model.addAttribute("course", course);
		//String teachType = planCourse.getTeachType();
		Integer examCourseType = "faceTeach".equals(courseTeachType) ?1:0;
		Map<String, Object> _condition = new HashMap<String, Object>();
		if("3".equals(courseExamForm)){//课程考试形式,期末机考
			//examCourseType = 0;
			isMachineExam = Constants.BOOLEAN_YES;
			isRevokeExam =  Constants.BOOLEAN_NO;
			_condition.put("schoolId",branchschoolid );
		}

		_condition.put("examSubId", examSubId);
		_condition.put("courseId", course.getResourceid());
		_condition.put("scoreStyle", planCourse.getScoreStyle());
		_condition.put("examCourseType", examCourseType);
		_condition.put("isMachineExam", isMachineExam);

		ExamInfo info = examInfoService.getExamInfo(_condition);
		Double facestudyscoreper = info.getFacestudyScorePer();
		Double facestudyscoreper2 = info.getFacestudyScorePer2();
		if(examCourseType==0){
			facestudyscoreper = info.getStudyScorePer();
			facestudyscoreper2 = info.getNetsidestudyScorePer();
		}
		model.addAttribute("scoreper", "卷面成绩 ：平时成绩 = "+facestudyscoreper+" ："+facestudyscoreper2);
		// 获取成绩计算规则字典表
		List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
		Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
		for(Dictionary d : resultCalculateRuleList) {
			resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
		}

		if(scoreList.size()>0){

			List<ExamResults> examResults = new ArrayList<ExamResults>();
			for (ExamResultsVo vo : scoreList) {
				boolean isUpdate = false;
				ExamResults rs = examResultsService.get(vo.getExamResultsResourceId());
				//手动更新缓存数据,测试的时候缓存数据没有更新导致操作报错
				if(rs!=null && !rs.getCheckStatus().equals(vo.getCheckStatus())){
					rs = examResultsService.load(vo.getExamResultsResourceId());
				}
				if(rs!=null && !vo.getExamInfoId().equals(info.getResourceid())){
					rs.setExamInfo(info);
					isUpdate = true;
				}
				if(ExStringUtils.isNotEmpty(vo.getCheckStatus())&&Integer.valueOf(vo.getCheckStatus())<1&&"0".equals(vo.getExamAbnormity())){
					String integrateScoreStr = examResultsService.caculateIntegrateScore(info,vo.getUsuallyScore(),vo.getWrittenScore(),vo.getOnlineScore(),null,resultCalculateRuleMap);
					vo.setIntegratedScore(integrateScoreStr);
					if(rs!=null){
						rs.setIntegratedScore(integrateScoreStr);
						isUpdate = true;
					}
				} else if(rs!=null){
					//if (ExStringUtils.isNotBlank(vo.getExamResultsResourceId())) {
//						vo.setIntegratedScore("0");
						vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", ExStringUtils.trimToEmpty(vo.getExamAbnormity())));
					//}
				}
				if(isUpdate){
					examResults.add(rs);
				}
			}
			examResultsService.batchSaveOrUpdate(examResults);
		}

		String scoreSaveMode = CacheAppManager.getSysConfigurationByCode("score.save.mode").getParamValue();
		condition.put("scoreSaveMode", scoreSaveMode);
		if ("0".equals(scoreSaveMode)) {
			condition.put("isAllowInput", "N");
		} else {
			condition.put("isAllowInput", "Y");
		}
		// 获取不显示的所有异常成绩情况代码
		String noShowCodeStr = dictionaryService.getAllValues("noShowCodeExamAbnormity");

		//加入课程考试比例
		condition.put("facestudyScorePer", facestudyscoreper);
		condition.put("isMachineExam", isMachineExam);
		condition.put("isRevokeExam", isRevokeExam);
		condition.put("courseTeachType", courseTeachType);
		model.addAttribute("courseTeachType", courseTeachType);
		model.addAttribute("noShowCodeStr", noShowCodeStr);
		model.addAttribute("page", objPage);
		model.addAttribute("condition",condition);
		ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
		String isDefaultAbsent=cp.getProperty("isDefaultAbsent");
		model.addAttribute("isDefaultAbsent", isDefaultAbsent);
		return "/edu3/teaching/examResult/facestudyExamResults-input-list";
	}

	private String caculateIntegrateScoreForFaceStudy(ExamInfo info, String usuallyScore, String writtenScore, ExamResults rs){
		String integratedScoreStr = "";
		//是否混合机考课程
		boolean isMixTrue =false;
		if (null!=info&&null!=info.getIsmixture()) {
			isMixTrue           = null==info.getIsmixture()?false:Constants.BOOLEAN_YES.equals(info.getIsmixture());
		}

		Double wsp 	   		 	 = 60D; //卷面成绩分比例
		if(null!=info&&null!=info.getStudyScorePer()){
			wsp = info.getStudyScorePer();
		}
		Double usp      		 = 100-wsp;                                                  //平时分比例

		BigDecimal divisor 		 = new BigDecimal("1");
		BigDecimal hundredBig    = new BigDecimal(100);

		if(ExStringUtils.isEmpty(usuallyScore)){
			usuallyScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}
		String examAbnormity     = null!=rs?rs.getExamAbnormity():"0";

		//其它考试类型的课程卷面成绩等于录入的成绩

		BigDecimal wsBig         = new BigDecimal(writtenScore);
		BigDecimal usBig    	 = new BigDecimal(usuallyScore);

		BigDecimal wsPerBig      = new BigDecimal(wsp.toString());
		BigDecimal usPerBig      = new BigDecimal(usp.toString());

		BigDecimal wsPerRateBig  = wsPerBig.divide(hundredBig,2);
		BigDecimal usPerRateBig  = usPerBig.divide(hundredBig,2);

		//成绩中成绩异常不为空时不设置卷面成绩、平时成绩、综合成绩						
		if (ExStringUtils.isNotEmpty(examAbnormity) && !Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)){

			//成绩中成绩异常为空时设置卷面成绩、平时成绩、综合成绩
		}else {

			//考试科目中平时成绩比例、卷面成绩比例不为空
			if (null!=wsp && null!=usp && wsp > 0&&usp>0){

				Double writtenScoreD = wsBig.multiply(wsPerRateBig).doubleValue();//根据批次比例算出的卷面成绩
				Double usuallyScoreD = usBig.multiply(usPerRateBig).doubleValue();//根据批次比例算出的平时成绩

				//当综合成绩小于卷面成绩时，综合成绩等于卷面成绩
//				if ( (writtenScoreD+usuallyScoreD) < Double.parseDouble(writtenScore)){ 
//					integratedScoreStr =(wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
//				//当综合成绩大于卷面成绩时,综合成绩计算方式为：卷面成绩*卷面成绩比例+平时成绩*平时成绩比例
//				}else{ 
				BigDecimal integratedScore = new BigDecimal(String.valueOf(writtenScoreD+usuallyScoreD));
				integratedScoreStr =(integratedScore.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
//				}

				//考试科目中平时成绩比例、卷面成绩比例为空综合成绩等于卷面成绩
			}else {
				integratedScoreStr = (wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
			}
		}
		return integratedScoreStr;
	}

	/**
	 * 面授/网络面授成绩撤销
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/examresults-del.html")
	public void facestudyExamResultsDel(String resourceid, HttpServletRequest request, HttpServletResponse response) throws WebException {

		Map<String,Object> condition = new HashMap<String, Object>();
		String msg                   = "";
		boolean isSuccess            = false;

		if (ExStringUtils.isNotEmpty(resourceid)) {
			ExamResults rs 			 = examResultsService.get(resourceid);

			if ("0".equals(ExStringUtils.trimToEmpty(rs.getCheckStatus()))) {
//				StudentLearnPlan plan= studentLearnPlanService.findUniqueByProperty("examResults", rs);
//				if (null!=plan) {
//					studentLearnPlanService.delete(plan);
//				}
				examResultsService.delete(rs);
				isSuccess            = true;
				msg                  = "撤销成功！";
			}else {
				msg 				 = "只允许撤销成绩状态为保存的成绩记录！";
			}
		}else{
			msg 					 = "请选择一个要撤销的成绩！";
		}
		condition.put("isSuccess", isSuccess);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 在线录入面授/网络面授成绩保存
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/input-examresults-save.html")
	public void inputFacestudyExamResultsSave(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String teachingPlanCourseId  = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String[] studyNos      	     = request.getParameterValues("resourceid");     //要保存成绩的学号		
		String isMachineExam = ExStringUtils.defaultIfEmpty(request.getParameter("isMachineExam"), Constants.BOOLEAN_NO);     // 是否机考
		String only = ExStringUtils.trimToEmpty(request.getParameter("only"));
		String courseTeachType = request.getParameter("courseTeachType");
		if(ExStringUtils.isNotEmpty(only)){
			if(null==studyNos){
				studyNos=new String[]{only};
			}else{
				List<String> stos = Arrays.asList(studyNos);
				if(!stos.contains(only)){
					studyNos[studyNos.length]=only;
				}
			}
		}
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL		
		User user              	 = SpringSecurityHelper.getCurrentUser();        //当前用户
		Double per 					 = 60D;
		if (ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId) && studyNos!=null){
			ExamSub examSub              = examSubService.get(examSubId);    			 //要录入成绩的考试批次
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			Date curTime             = new Date();                                	 	 //当前时间
			Date endTime    		 = examSub.getEndTime();                  	 		//考试批次预约结束时间

			if (curTime.after(endTime)){
				Course course = planCourse.getCourse();

				//String teachType = planCourse.getTeachType();		 	    
				if(ExStringUtils.isBlank(planCourse.getTeachType())||"networkstudy".equals(planCourse.getTeachType())){
					throw new WebException("课程"+course.getCourseName()+"教学方式不是面授或网络面授!");
				}
				Integer examCourseType = courseTeachType.contains("face")?1:0;
				if(Constants.BOOLEAN_YES.equals(isMachineExam)){
					examCourseType = 3;
				}
				ExamInfo examInfo = examInfoService.saveExamInfo(per, examSub, course, examCourseType,isMachineExam);

				// 获取成绩计算规则字典表
				List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
				Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
				for(Dictionary d : resultCalculateRuleList) {
					resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
				}

				Double writtenScorePer = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue()); //卷面成绩分比例
				Double usuallyScorePer = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue()); //平时考核分比例

				// 按照成绩计算规则计算成绩
				if(examCourseType==0){// 网络面授课程比例
					writtenScorePer = null==examInfo.getStudyScorePer()?-1D:examInfo.getStudyScorePer();
					// 平时成绩
					usuallyScorePer = examInfo.getNetsidestudyScorePer();
					if(null == usuallyScorePer){
						usuallyScorePer =  examSub.getNetsidestudyScorePer();
					} 
				} else {
					if(null == examInfo.getFacestudyScorePer()){
						writtenScorePer = examSub.getFacestudyScorePer();
					} else {
						writtenScorePer = examInfo.getFacestudyScorePer();
					}
					// 平时成绩
					usuallyScorePer = examInfo.getFacestudyScorePer2();
					if(null == usuallyScorePer){
						usuallyScorePer =  examSub.getFacestudyScorePer2();
					} 
				}
				
				Double usuallyTopScore = usuallyScorePer;
				Double writtenTopScore = writtenScorePer;
				// 成绩计算规则
				String rule ="";
				if(resultCalculateRuleMap != null && resultCalculateRuleMap.size() > 0){
					rule= resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_USUALLYSCORE);
					if("2".equals(rule)){
						usuallyScorePer = 100D;
					}
					rule= resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_WRITTENSCORE);
					if("2".equals(rule)){
						writtenScorePer = 100D;
					}
				}

				Double usuallyScoreRate= -1D;
				Double writtenScoreRate= -1D;

				if (null!=usuallyScorePer&&usuallyScorePer>=0) {
					usuallyScoreRate   = usuallyScorePer/100;
				}
				if (null!=writtenScorePer&&writtenScorePer>=0) {
					writtenScoreRate   = writtenScorePer/100;
				}
//				String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				int writtenScoreScale = 0;
				int usuallyScoreScale = 0;
				int integratedScoreScale = 0;
				List<Dictionary> dictionaries = CacheAppManager.getChildren("CodeExamresultsScale");
				for (Dictionary dictionary : dictionaries) {
					if(dictionary.getDictName().startsWith("卷面成绩")){
						writtenScoreScale = Integer.parseInt(dictionary.getDictValue());
					}else if (dictionary.getDictName().startsWith("平时成绩")) {
						usuallyScoreScale = Integer.parseInt(dictionary.getDictValue());
					}else if (dictionary.getDictName().startsWith("综合成绩")) {
						integratedScoreScale = Integer.parseInt(dictionary.getDictValue());
					}
				}
				for (int i = 0; i < studyNos.length; i++) {
					if(ExStringUtils.isNotEmpty(only)){
						if(!studyNos[i].equals(only)){
							continue;
						}
					}
					try {
						if(ExStringUtils.isBlank(studyNos[i])){
							continue;
						}
						ExamResults examResults = null;
						String resultsId = ExStringUtils.trimToEmpty(request.getParameter("resultsId"+studyNos[i]));
						if(ExStringUtils.isNotBlank(resultsId)){
							examResults = examResultsService.get(resultsId);//要保存的成绩
						} else {
							examResults = new ExamResults();
							examResults.setCourse(course);
							examResults.setExamInfo(examInfo);
							examResults.setMajorCourseId(teachingPlanCourseId);
							//examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
							examResults.setCheckStatus("0");
							examResults.setExamsubId(examSubId);
							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
							examResults.setIsMakeupExam(examSub.getExamType());//考试类型
							examResults.setCourseType(planCourse.getCourseType());
							examResults.setIsMachineExam(isMachineExam);
							StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNos[i]);
							examResults.setStudentInfo(studentInfo);

							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setCourseScoreType(examInfo.getCourseScoreType());
							List examCountList      = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
									examResults.getStudentInfo().getResourceid(),ExStringUtils.trimToEmpty(examResults.getResourceid()));
							long examCount          = (Long)examCountList.get(0);			         //选考次数
							examResults.setExamCount(examCount+1);
						}
						if(examResults.getExamInfo().getResourceid() != examInfo.getResourceid()){//检查考试信息是否正确
							examResults.setExamInfo(examInfo);
						}
						examResults.setPlanCourseTeachType(planCourse.getTeachType());
						int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
						if (checkStatus>0) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
						}
						String examAbnormity    = ExStringUtils.trimToEmpty(request.getParameter("examAbnormity"+studyNos[i]));
						String writtenScore     = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+studyNos[i]));
						String usuallyScore     = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore"+studyNos[i]));
						String onlineScore     = ExStringUtils.trimToEmpty(request.getParameter("onlineScore"+studyNos[i]));
						String integratedScore  = ExStringUtils.trimToEmpty(request.getParameter("integratedScore"+studyNos[i]));

						Double usuallyScoreD = 0D;
						Double writtenScoreD = 0D;
						if(ExStringUtils.isNotEmpty(usuallyScore)){
							usuallyScoreD = Double.valueOf(usuallyScore);
						}
						if(ExStringUtils.isNotEmpty(writtenScore)){
							writtenScoreD = Double.valueOf(writtenScore);
						}

						if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {//这里未参加机考的直接返回了,导致无法保存,录入缺考状态
							if(examCourseType==3){//机考
								examAbnormity = "3";
								writtenScore = "";
							}else{
								continue;
							}
						}
						if (ExStringUtils.isNotEmpty(examAbnormity)) {
							examResults.setExamAbnormity(examAbnormity);
						}
						// 卷面成绩
						if("0".equals(examResults.getExamAbnormity())){
							Double _writtenScore = writtenTopScore/writtenScoreRate;
							if(writtenScoreD<0||writtenScoreD>_writtenScore){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩必须为0-"+_writtenScore.intValue()+"分");
							}
							BigDecimal w_Score	 = new BigDecimal(writtenScoreD);
							if(writtenScore.contains(".")){
								examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,writtenScoreScale,BigDecimal.ROUND_HALF_UP)).toString());
							}else {
								examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}

						} else {
							examResults.setWrittenScore("");
						}
						// 平时成绩
						Double _usuallyScore = usuallyTopScore/usuallyScoreRate;
						if(usuallyScoreD<0||usuallyScoreD>_usuallyScore){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的平时考核成绩必须为0-"+_usuallyScore.intValue()+"分");
						}
						if(ExStringUtils.isNotEmpty(usuallyScore)){
							BigDecimal u_Score	 = new BigDecimal(usuallyScoreD);
							if(usuallyScore.contains(".")){
								examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,usuallyScoreScale,BigDecimal.ROUND_HALF_UP)).toString());
							}else {
								examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}
						}
						// 网上学习成绩
						if(ExStringUtils.isNotBlank(onlineScore)&&(Double.valueOf(onlineScore)<0||Double.valueOf(onlineScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的网上学习成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(onlineScore)){
							BigDecimal o_Score	 = new BigDecimal(onlineScore);
							examResults.setOnlineScore((o_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						if("0".equals(examResults.getExamAbnormity())){
							Double temp_usuallyScore = Double.valueOf(usuallyScoreD)*usuallyScoreRate;
							Double temp_writtenScore = Double.valueOf(writtenScoreD)*writtenScoreRate;
							Double temp_integratedScore = temp_usuallyScore+temp_writtenScore;

							if(temp_integratedScore<0||temp_integratedScore>100){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的综合成绩必须为0-100分");
							}
							BigDecimal i_Score = new BigDecimal(temp_integratedScore);
							if(temp_integratedScore.intValue()!=temp_integratedScore){
								examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,integratedScoreScale,BigDecimal.ROUND_HALF_UP)).toString());
							}else {
								examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}
							
							/*if(ExStringUtils.isNotBlank(integratedScore)&&(Double.valueOf(integratedScore)<0||Double.valueOf(integratedScore)>100)){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的综合成绩必须为0-100分");
							}
							if(ExStringUtils.isNotBlank(integratedScore)){
								BigDecimal i_Score	 = new BigDecimal(integratedScore);
								examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}*/
						} else {
							examResults.setIntegratedScore("");
						}
						examResults.setFillinDate(curTime);
						examResults.setCheckStatus("0");
						examResults.setFillinMan(user.getCnName());
						examResults.setFillinManId(user.getResourceid());

//						examResultsService.saveOrUpdate(examResults);
						if (examResults.getResourceid() == null || "".equals(examResults.getResourceid())) {
							List<ExamResults> llist	   = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" te where te.studentInfo.resourceid= ? and te.isDeleted=0 and te.examsubId=? and  te.course.resourceid=? ",examResults.getStudentInfo().getResourceid(),examResults.getExamsubId(),examResults.getCourse().getResourceid());
							if(llist.size()>0) {
								throw new WebException("学生考试记录已存在，学生学号："+studyNos[i]+",请刷新页面！");
							}
							examResultsService.save(examResults);
						} else {
							examResultsService.update(examResults);
						}

						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),teachingPlanCourseId);
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}
						studentLearnPlanService.saveOrUpdate(learnPlan);

					} catch (Exception e) {
						logger.error("保存学生成绩异常，学生学号：{}"+studyNos[i],e.fillInStackTrace());
						success = false;
						msg     = "录入学生成绩异常:"+e.getMessage();
						break;
					}
				}
			}else {
				success = false;
				msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
						+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);
			}
		}else {
			success = false;
			msg     = "请选择一个考试批次！";
		}

		condition.put("success",success);
		condition.put("courseTeachType",courseTeachType);
		condition.put("facestudyScorePer",request.getParameter("facestudyScorePer"));
		if (ExStringUtils.isNotEmpty(msg)) {
			condition.put("msg", msg);
		}

		renderJson(response,JsonUtils.mapToJson(condition));

	}

	/**
	 * 提交面授/网络面授成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/input-examresults-submit.html")
	public void submitFacestudyExamResults(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			//String guidPlanId            = request.getParameter("guidePlanId");
			String gradeid 				= ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
			String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
			String classesId            = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			String unitId            = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
			//teachType 教学类型：网络/面授，取自开课表字段
			String teachType		= ExStringUtils.trimToEmpty(request.getParameter("teachType"));
			boolean isSubmited = true;
			if(ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId)&&ExStringUtils.isNotBlank(gradeid)){
				User curUser = SpringSecurityHelper.getCurrentUser();
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
						|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
//					map.put("branchschoolid", curUser.getOrgUnit().getResourceid());
					map.put("branchschoolid", unitId);
					TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
					//TeachingGuidePlan guidePlan = teachingGuidePlanService.get(guidPlanId);	
					map.put("courseId", planCourse.getCourse().getResourceid());
					//map.put("gradeid", guidePlan.getGrade().getResourceid());
					map.put("gradeid", gradeid);
					map.put("examSubId", examSubId);
					map.put("teachingPlanId", planCourse.getTeachingPlan().getResourceid());
					StringBuffer hql = new StringBuffer(1024);
					hql.append(" from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 and results.course.resourceid=:courseId and results.examsubId=:examSubId and results.studentInfo.studentStatus not in('12','13') ")	
					.append("and results.studentInfo.grade.resourceid=:gradeid and results.studentInfo.branchSchool.resourceid=:branchschoolid and results.studentInfo.teachingPlan.resourceid=:teachingPlanId ");
					if(ExStringUtils.isNotEmpty(classesId)){
						map.put("classesId", classesId);
						hql.append(" and  results.studentInfo.classes.resourceid = :classesId ");
					}
					List<ExamResults> list = examResultsService.findByHql(hql.toString(), map);
					Map<String,Object> condition = new HashMap<String, Object>();
					condition.put("examSubId",examSubId);
					condition.put("gradeid",gradeid);
					condition.put("teachplanid",planCourse.getTeachingPlan().getResourceid());
					condition.put("classesId",classesId);
//					String branchschoolid = curUser.getOrgUnit().getResourceid();
					String branchschoolid = unitId;
					condition.put("branchschoolid",branchschoolid);
					//此处teachType传值应该为为教学方式（统考/非统考）进行查询
					condition.put("teachType", ExStringUtils.trimToEmpty(planCourse.getTeachType()));
					//condition.put("courseTeachType", teachType);
					condition.put("courseId", planCourse.getCourse().getResourceid());
					Page objPage = new Page();
					objPage.setOrderBy("unit.unitcode,major.majorcode,stu.studyno ");
					objPage.setOrder(Page.ASC);
					objPage = teachingJDBCService.findFaceStudyExamResultsVo(objPage, condition);
					List<ExamResultsVo> scoreList = objPage.getResult();

					List<NoExamApply> noExamList = new ArrayList<NoExamApply>();
					for(int i=0;i<scoreList.size();i++){
						List<NoExamApply> noExam = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()+" etn where etn.isDeleted = '0' and etn.studentInfo.resourceid =? and etn.course.resourceid =? and etn.checkStatus='1' order by etn.scoreForCount desc ",scoreList.get(i).getStuId(),planCourse.getCourse().getResourceid() );
						noExamList.addAll(noExam);
					}
					int uninput = scoreList.size() - list.size()-noExamList.size();

					//是否全部录入成绩才允许提交
					String isallinput = CacheAppManager.getSysConfigurationByCode("examresult.isallinput").getParamValue();
					if ("1".equals(isallinput) && uninput > 0) {
						map.put("statusCode", 300);
						map.put("message", "全部录入成绩才允许提交！");
					} else {
						//非统考提交时计算综合成绩
						List<ExamResults> upadateIntegrateScore = new ArrayList<ExamResults>(0);
						// 获取成绩计算规则字典表
						List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
						Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
						for(Dictionary d : resultCalculateRuleList) {
							resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
						}
						Date now = new Date();//机考时间未结束不能提交
						boolean isSubmitedForMachineExam = true;
						boolean isNetWorkAndUnInputUS = false;
						for (ExamResults examResults : list) {
							String usuallyScore = examResults.getUsuallyScore();
							String writtenScore = examResults.getWrittenScore();
							String onlineScore  = examResults.getOnlineScore();
							// 跳过已发布和已提交的成绩
							if("4".equals(examResults.getCheckStatus()) || "1".equals(examResults.getCheckStatus())){
								continue;
							}
							if("0".equals(examResults.getExamAbnormity()) && !"4".equals(examResults.getCheckStatus()) && (ExStringUtils.isEmpty(usuallyScore) && ExStringUtils.isEmpty(writtenScore))){
								isSubmited = false;
								break;
							}
							if("Y".equals(examResults.getExamInfo().getIsMachineExam()) && (examResults.getExamInfo().getExamEndTime()==null || (examResults.getExamInfo().getExamEndTime().compareTo(now))>0)){
								isSubmitedForMachineExam = false;
								break;
							}

							ExamInfo info = examResults.getExamInfo();
							int isNetWorkCourse = "networkTeach".equals(teachType) ?0:1;
							if (isNetWorkCourse != info.getExamCourseType()) {
								Map<String,Object> _condition = new HashMap<String, Object>();
								_condition.put("examSubId",examSubId);
								_condition.put("courseId",planCourse.getCourse().getResourceid());
								_condition.put("scoreStyle",planCourse.getScoreStyle());
								_condition.put("examCourseType",isNetWorkCourse);
								info = examInfoService.getExamInfo(_condition);
								examResults.setExamInfo(info);
							}
							if(isNetWorkCourse==0&& "0".equals(examResults.getExamAbnormity()) && ExStringUtils.isBlank(usuallyScore)){//网络课程
								isNetWorkAndUnInputUS=true;
								break;
							}
							if(ExStringUtils.isEmpty(examResults.getIntegratedScore())){
								String intergrateScore = examResultsService.caculateIntegrateScore(info, usuallyScore, writtenScore, onlineScore, examResults,resultCalculateRuleMap);
								examResults.setIntegratedScore(ExStringUtils.defaultIfEmpty(intergrateScore, ""));
							}
							upadateIntegrateScore.add(examResults);

							//判断是否需要需要补考
							//20140617 温朝上提出，综合成绩小于60分都进去补考名单
							//20140624 陈浩珍提出，没有成绩与综合成绩小于60的都进入补考名单
//						if(ExStringUtils.isEmpty(intergrateScore) ||
//								(ExStringUtils.isNotEmpty(intergrateScore) && Double.parseDouble(intergrateScore)<60.0)
//								|| examResults.getIsDelayExam() == Constants.BOOLEAN_YES || examResults.getExamAbnormity().equals("2")){
//							String hqltp = " from "+TeachingPlanCourse.class.getSimpleName()+" a where a.isDeleted=0 and a.teachingPlan.resourceid=? and a.course.resourceid=? and a.teachType='facestudy'";
//							String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? ";
//							List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findByHql(hqltp,
//									examResults.getStudentInfo().getTeachingPlan().getResourceid(),examResults.getCourse().getResourceid());
//							if(null!=planCourseList && planCourseList.size()>0){//只处理非统考
//								
//								Long year = 0l;
//								String term = "";
//								YearInfo yearInfo = null;
//								ExamSub nextExamSub = null;
//								String hqlgui = "from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and teachingPlan.resourceid=?";
//								List<TeachingGuidePlan> guiList = teachingGuidePlanService.findByHql(hqlgui, planCourseList.get(0).getTeachingPlan().getResourceid());
//								if (guiList != null && guiList.size() > 0) {
//									yearInfo = guiList.get(0).getGrade().getYearInfo();//入学年级
//									year = guiList.get(0).getGrade().getYearInfo().getFirstYear();//所属年级
//									//获取课程上课学期
//									String hqlstatus = "from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingPlanCourse.resourceid=?"
//											+" and teachingGuidePlan.resourceid=? and schoolIds=?";
//									List<TeachingPlanCourseStatus> staList = teachingPlanCourseStatusService.findByHql(hqlstatus, planCourseList.get(0).getResourceid()
//											, guiList.get(0).getResourceid(), rs.getStudentInfo().getBranchSchool().getResourceid());
//									if (staList != null && staList.size() > 0) {
//										term = staList.get(0).getTerm();//课程上课学期
//									}
//									String[] terms = term.split("_");
//									Long index = 1l;//第几个上课学期
//									Long termYear = Long.parseLong(terms[0]);
//									for (Long i = year; i <= termYear; i++) {
//										for (int j = 1; j <= 2; j++) {
//											if (term.equals(i + "_0" + j)) {
//												break;
//											}
//											index++;
//										}
//									}
//									Long nextYear = 0l;
//									String nextTerm = "";
//									if (index>=1 && index<=5) {//下学期
//										if ("02".equals(terms[1])) {
//											nextYear = Long.parseLong(terms[0]) + 1;
//											nextTerm = "1";
//										} else {
//											nextYear = Long.parseLong(terms[0]);
//											nextTerm = "2";
//										}
//									} else {//第6学期
//										nextYear = year +2;
//										nextTerm = "2";
//									}
//									String hqlexamsub = "from "+ExamSub.class.getSimpleName()+" where isDeleted=0 and examType='Y'"
//										+" and yearInfo.firstYear=? and term=?";
//									List<ExamSub> examSubList = examSubService.findByHql(hqlexamsub, nextYear, nextTerm);
//									if (examSubList != null && examSubList.size() > 0) {
//										nextExamSub = examSubList.get(0);
//									}
//								}
//								
//								List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
//								StudentMakeupList makeup = null;
//								//如果学生这门课没通过，查询补考表，有则替换成绩，没有则添加
//								makeupList = studentMakeupListService.findByHql(hqlMakeup,examResults.getStudentInfo().getResourceid(),examResults.getCourse().getResourceid());
//							    if(null==makeupList || makeupList.size()==0){
//							    	makeup = new StudentMakeupList();
//							    	makeup.setStudentInfo(examResults.getStudentInfo());
//							    	makeup.setCourse(examResults.getCourse());
//							    	makeup.setExamResults(examResults);
//							    	makeup.setTeachingPlanCourse(planCourseList.get(0));
//							    	if (nextExamSub != null) {
//										makeup.setNextExamSubId(nextExamSub.getResourceid());
//									}
//							    	studentMakeupListService.saveOrUpdate(makeup);
//							    } else if(makeupList.size()>0){
//							    	makeup = makeupList.get(0);
//							    	makeup.setExamResults(examResults);
//							    	if (nextExamSub != null) {
//										makeup.setNextExamSubId(nextExamSub.getResourceid());
//									}
//							    	studentMakeupListService.saveOrUpdate(makeup);
//							    }
//							}
//						}
						}
						if(isNetWorkAndUnInputUS){
							map.put("statusCode", 300);
							map.put("message", "该课程为网络课程，平时成绩不允许为空！请先提交学生的网络课程成绩后再进行操作");
						}else if(isSubmited && isSubmitedForMachineExam){
							examResultsService.batchSaveOrUpdate(upadateIntegrateScore);
							examResultsService.submitExamResults(list, "Y",null);
							map.put("statusCode", 200);
							UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5",UserOperationLogs.INSERT, "提交成绩：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
//							map.put("message", "提交成功！还有" + uninput + "个学生未录入成绩！");		
							map.put("message", "提交成功！");
						}else if(!isSubmitedForMachineExam){
							map.put("statusCode", 300);
							map.put("message", "机考考试时间未结束！");
						}else{
							map.put("statusCode", 300);
							map.put("message", "平时成绩和卷面成绩不能为空！");
						}
					}
				} else {
					map.put("statusCode", 300);
					map.put("message", "你没有提交成绩的权限");
				}
			}
		} catch (Exception e) {
			logger.error("提交成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 已发布的成绩列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/correct/list.html")
	public String auditExamResultList(HttpServletRequest request, ModelMap model, Page page){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));

		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("scoreChange", "1");
			Page examResultsList = examResultsService.findAuditExamResulstsVoByCondition(condition, page);
			model.addAttribute("examResultsList", examResultsList);
		}

		model.addAttribute("condition",condition);
		return "/edu3/teaching/examResult/examResults-audit-list";
	}

	/**
	 * 编辑成绩
	 * @param resourceid
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/correct/edit.html")
	public String editAuditExamResult(String resourceid, String examSubId, ModelMap model){
		String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if(ExStringUtils.isNotBlank(resourceid)){
			String hql = " from "+PublishedExamResultsAudit.class.getSimpleName()+" where isDeleted = 0 and examResults.resourceid=? and auditType = 1 and auditStatus=0  ";
			PublishedExamResultsAudit audit = publishedExamResultsAuditService.findUnique(hql, resourceid);
			if(audit==null){
				ExamResults rs = examResultsService.get(resourceid);
				audit = new PublishedExamResultsAudit();
				audit.setExamResults(rs);
				/*if (ExStringUtils.isNotBlank(examSubId)) {
					examSub = examSubService.get(examSubId);
				}*/
			}

			String islastExam = "Y";
			String hql1="from "+ExamResults.class.getSimpleName()+ " er where isDeleted = 0 and er.studentInfo = ? and er.course = ?";
			List<ExamResults> erList = examResultsService.findByHql(hql1, audit.getExamResults().getStudentInfo(),audit.getExamResults().getCourse());
			String currentType = audit.getExamResults().getIsMakeupExam();
			for(ExamResults tmp:erList){
				if(currentType.equals(Constants.EXAMRESULT_TYPE_0)){//如果是正考，发现有一补，返回
					if(tmp.getIsMakeupExam().equals(Constants.EXAMRESULT_TYPE_1) ) {
						islastExam = "N";
						break;
					}
				}
				if(currentType.equals(Constants.EXAMRESULT_TYPE_1)){//如果是一补，发现有二补，返回
					if(tmp.getIsMakeupExam().equals(Constants.EXAMRESULT_TYPE_2) ) {
						islastExam = "N";
						break;
					}
				}
				if(currentType.equals(Constants.EXAMRESULT_TYPE_2)){//如果是二补，发现有结补，返回
					if(tmp.getIsMakeupExam().equals(Constants.EXAMRESULT_TYPE_3) ) {
						islastExam = "N";
						break;
					}
				}
			}
			ExamSub examSub = audit.getExamResults().getExamInfo().getExamSub();
			String noShowCodeStr = "";
			if(examSub != null){
				if("N".equals(examSub.getExamType())){//正考才需要
					// 获取不显示的所有异常成绩情况代码
					noShowCodeStr = dictionaryService.getAllValues("noShowCodeExamAbnormity");
				}
			}
			model.addAttribute("examSub", examSub);
			model.addAttribute("noShowCodeStr", noShowCodeStr);
			model.addAttribute("examResultAudit",audit);
			model.addAttribute("islastExam",islastExam);
		}
		model.addAttribute("schoolCode",schoolCode);
		return "/edu3/teaching/examResult/examResults-audit-form";
	}
	/**
	 * 保存更正的成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examresult/correct/save.html")
	public void saveAuditExamResult(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String ,Object> map 		    = new HashMap<String, Object>();
		try {
			String resourceid 			    = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
			String examResultsId 		    = ExStringUtils.trimToEmpty(request.getParameter("examResultsId"));
			String changedWrittenScore 	    = ExStringUtils.trimToEmpty(request.getParameter("changedWrittenScore"));
			String changedUsuallyScore 	    = ExStringUtils.trimToEmpty(request.getParameter("changedUsuallyScore"));
			String changedIntegratedScore   = ExStringUtils.trimToEmpty(request.getParameter("changedIntegratedScore"));
			String changedHandworkScore     = ExStringUtils.trimToEmpty(request.getParameter("changedWrittenHandworkScore"));
			String changedMachineScore   	= ExStringUtils.trimToEmpty(request.getParameter("changedWrittenMachineScore"));
			String changedExamAbnormity     = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("changedExamAbnormity")), "0");
			String memo 				    = ExStringUtils.trimToEmpty(request.getParameter("memo"));
			User curUser 				    = SpringSecurityHelper.getCurrentUser();

			if(ExStringUtils.isBlank(examResultsId)){
				throw new WebException("请重新选择一条需更正的成绩");
			}
			ExamResults rs 					= examResultsService.get(examResultsId);
			ExamInfo info                   = rs.getExamInfo();
			String isMixTrue                = null==info.getIsmixture()?"N":info.getIsmixture();

			if("0".equals(changedExamAbnormity)){
				if((ExStringUtils.isBlank(changedWrittenScore)||ExStringUtils.isBlank(changedIntegratedScore))&&Constants.BOOLEAN_NO.equals(isMixTrue)){
					throw new WebException("卷面成绩或综合成绩不能为空");
				}
				if (Constants.BOOLEAN_NO.equals(isMixTrue)) {
					Double cw 					= Double.valueOf(changedWrittenScore);
					Double ci 					= Double.valueOf(changedIntegratedScore);
					if((cw<0||cw>100||ci<0||ci>100)&&Constants.BOOLEAN_NO.equals(isMixTrue)){
						throw new WebException("卷面成绩或综合成绩只能为0-100分");
					}
				}
			} else {
				changedWrittenScore         = "";
				changedIntegratedScore      = "";
			}
			PublishedExamResultsAudit audit = new PublishedExamResultsAudit();
			if(ExStringUtils.isNotBlank(resourceid)){
				audit 						= publishedExamResultsAuditService.get(resourceid);
			} else {
				audit.setExamResults(rs);
				audit.setBeforeWrittenScore(rs.getWrittenScore());
				audit.setBeforeUsuallyScore(rs.getUsuallyScore());
				audit.setBeforeIntegratedScore(rs.getIntegratedScore());
				audit.setBeforeExamAbnormity(rs.getExamAbnormity());
			}
			if (Constants.BOOLEAN_YES.equals(isMixTrue)) {
				audit.setChangedWrittenScore(String.valueOf((Double.valueOf(changedHandworkScore)+Double.valueOf(changedMachineScore))));
				audit.setChangedWrittenHandworkScore(changedHandworkScore);
				audit.setChangedWrittenMachineScore(changedMachineScore);
			}else{
				audit.setChangedWrittenScore(changedWrittenScore);
			}
			audit.setScoreType(0);
			audit.setChangedUsuallyScore(changedUsuallyScore);
			audit.setChangedIntegratedScore(changedIntegratedScore);
			audit.setChangedExamAbnormity(changedExamAbnormity);
			if("5".equals(changedExamAbnormity)) {
				audit.getExamResults().setIsDelayExam("Y");
			} else {
				audit.getExamResults().setIsDelayExam("N");
			}
			audit.setChangedDate(new Date());
			audit.setChangedMan(curUser.getCnName());
			audit.setChangedManId(curUser.getResourceid());
			audit.setAuditStatus(0);
			audit.setMemo(memo);

			publishedExamResultsAuditService.saveOrUpdate(audit);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.INSERT,"成绩更正：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			map.put("statusCode", 200);
			map.put("message", "提交成功！");
			map.put("navTabId", "RES_TEACHING_RESULT_CORRECT_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/examresult/correct/edit.html?resourceid="+examResultsId);
		} catch (Exception e) {
			logger.error("提交更正成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 打印预览
	 */
	@RequestMapping("/edu3/teaching/examresult/correct/printView.html")
	public String printViewAuditExamResults(HttpServletRequest request, ModelMap model){
		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		return "/edu3/teaching/examResult/examResults-audit-printView";
	}

	@RequestMapping("/edu3/teaching/examresult/correct/print.html")
	public void printAuditExamResults(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> condition  = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		Map<String, Object> printParam = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		JasperPrint jasperPrint = null;
		// 报表文件
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("select es.batchname,c.coursename,si.studyno,si.studentname,");
			builder.append(" f_decrypt_score(nvl2(ar.resourceid,ar.beforewrittenscore,er.writtenscore),er.studentid)beforewrittenscore,");
			builder.append(" f_decrypt_score(nvl2(ar.resourceid,ar.beforeusuallyscore,er.usuallyscore),er.studentid)beforeusuallyscore,");
			builder.append(" f_decrypt_score(nvl2(ar.resourceid,ar.beforeintegratedscore,er.integratedscore),er.studentid)beforeintegratedscore,");
			builder.append(" nvl(ar.beforeexamabnormity,er.examabnormity)beforeexamabnormity,");
			builder.append(" f_decrypt_score(ar.changedwrittenscore,er.studentid)changedwrittenscore,");
			builder.append(" f_decrypt_score(ar.changedusuallyscore,er.studentid)changedusuallyscore,");
			builder.append(" f_decrypt_score(ar.changedintegratedscore,er.studentid)changedintegratedscore,");
			builder.append(" ar.changedexamabnormity,ar.changedman,to_char(ar.changeddate,'yyyy-mm-dd hh:mm:ss')changeddate,");
			builder.append(" ar.auditman,to_char(ar.auditdate,'yyyy-mm-dd hh:mm:ss')auditdate,ar.auditstatus,ar.memo");
			builder.append("  from edu_teach_examresults er ");
			builder.append(" join edu_roll_studentinfo si on er.studentid=si.resourceid");
			builder.append(" join edu_base_course c on c.resourceid=er.courseid");
			builder.append(" join edu_teach_examsub es on es.resourceid=er.examsubid");
			builder.append(" left join edu_teach_auditresults ar on ar.resultsid=er.resourceid and ar.isdeleted=0");
			builder.append(" where er.isdeleted=0 ");

			if(condition.containsKey("resourceids")){
				String resourceids = ExStringUtils.addSymbol(condition.get("resourceids").toString().split(","), "'", "'");
				builder.append(" and er.resourceid in("+resourceids+")");
			}else {
				if(condition.containsKey("brSchoolId")) {
					builder.append(" and si.branchschoolid=:brSchoolId");
				}
				if(condition.containsKey("gradeId")) {
					builder.append(" and si.gradeId=:gradeId");
				}
				if(condition.containsKey("classicId")) {
					builder.append(" and si.classicId=:classicId");
				}
				if(condition.containsKey("teachingType")) {
					builder.append(" and si.teachingType=:teachingType");
				}
				if(condition.containsKey("majorId")) {
					builder.append(" and si.majorId=:majorId");
				}
				if(condition.containsKey("teachType")) {
					builder.append(" and er.plancourseteachtype=:teachType");
				}
				if(condition.containsKey("examSubId")) {
					builder.append(" and er.examSubId=:examSubId");
				}
				if(condition.containsKey("classId")) {
					builder.append(" and si.classesid=:classId");
				}
				if(condition.containsKey("courseId")) {
					builder.append(" and er.courseId=:courseId");
				}
				if(condition.containsKey("studyNo")) {
					builder.append(" and si.studyNo=:studyNo");
				}
				if(condition.containsKey("studentName")) {
					builder.append(" and si.studentName=:studentName");
				}
			}
			dataList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(builder.toString(), condition);
			String reportPath = File.separator+"reports"+File.separator+"examPrint"+File.separator+"examResults_audit.jasper";
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			for (Map<String, Object> map : dataList) {
				String examAbnormity_1 = dictionaryService.dictCode2Val("CodeExamAbnormity", ExStringUtils.toString(map.get("beforeexamabnormity")));
				String examAbnormity_2 = dictionaryService.dictCode2Val("CodeExamAbnormity", ExStringUtils.toString(map.get("changedexamabnormity")));
				map.put("beforeexamabnormity", examAbnormity_1);
				map.put("changedexamabnormity", examAbnormity_2);
				//更正前异常成绩处理
				if(map.get("beforeexamabnormity")!=null && !"0".equals(map.get("beforeexamabnormity"))){
					if(ExStringUtils.isEmpty((String) map.get("beforewrittenscore"))){
						map.put("beforewrittenscore", examAbnormity_1);
					}
					if(ExStringUtils.isEmpty((String) map.get("beforeusuallyscore"))){
						map.put("beforeusuallyscore", examAbnormity_1);
					}
					if(ExStringUtils.isEmpty((String) map.get("beforeintegratedscore"))){
						map.put("beforeintegratedscore", examAbnormity_1);
					}
				}
				//更正后异常成绩处理
				if(map.get("changedexamabnormity")!=null && !"0".equals(map.get("changedexamabnormity"))){
					if(ExStringUtils.isEmpty((String) map.get("changedwrittenscore"))){
						map.put("changedwrittenscore", examAbnormity_2);
					}
					if(ExStringUtils.isEmpty((String) map.get("changedusuallyscore"))){
						map.put("changedusuallyscore", examAbnormity_2);
					}
					if(ExStringUtils.isEmpty((String) map.get("changedintegratedscore"))){
						map.put("changedintegratedscore", examAbnormity_2);
					}
				}
			}
			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
			jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource);
			if (null!=jasperPrint) {
				if(condition.containsKey("isPdf")){
					GUIDUtils.init();
					String filePath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,"UTF-8");
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
					exporter.exportReport();
					downloadFile(response,"成绩更正.pdf",filePath,true);
				}else{
					renderStream(response, jasperPrint);
				}
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (JRException e) {
			e.printStackTrace();
			renderHtml(response,"<script>alert('打印成绩更正出错："+e.getMessage()+"')</script>");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			renderHtml(response,"<script>alert('打印成绩更正出错："+e.getMessage()+"')</script>");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderHtml(response,"<script>alert('打印成绩更正出错："+e.getMessage()+"')</script>");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			renderHtml(response,"打印数据出错！");
		}

	}

	/**
	 * 发布的成绩更正复审列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/correct/audit/list.html")
	public String listAuditExamresults(HttpServletRequest request, ModelMap model, Page page){
		page.setOrderBy("examResults.course.courseCode,examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo");
		page.setOrder(Page.ASC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String auditStatus = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("auditStatus")), "0");
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String changedMan = ExStringUtils.trimToEmpty(request.getParameter("changedMan"));
		String auditMan = ExStringUtils.trimToEmpty(request.getParameter("auditMan"));
		String startTime = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime = ExStringUtils.trimToEmpty(request.getParameter("endTime"));

		if (ExStringUtils.isNotEmpty(auditStatus)) {
			condition.put("auditStatus", auditStatus);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if (ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(changedMan)) {
			condition.put("changedMan", changedMan);
		}
		if (ExStringUtils.isNotEmpty(auditMan)) {
			condition.put("auditMan", auditMan);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		condition.put("auditType",1);
		condition.put("scoreType", 0);
		Page examResultsAuditList = publishedExamResultsAuditService.findExamResultsAuditByCondition(condition, page);
		model.addAttribute("examResultsAuditList",examResultsAuditList);
		model.addAttribute("condition",condition);
		return "/edu3/teaching/examResult/examResultsAudit-list";
	}
	/**
	 * 发布成绩更正复审
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examresult/correct/audit/publish.html")
	public void publishAuditExamresults(String resourceid, HttpServletRequest request, HttpServletResponse response){
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				publishedExamResultsAuditService.publishAuditExamResults(resourceid.split("\\,"));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.PASS,"成绩更正审核：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "审核发布成功！");
			}
		} catch (Exception e) {
			logger.error("审核发布成功出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核发布成功出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 已发布的毕业论文成绩列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/thesis/correct/list.html")
	public String listThesisExamResults(HttpServletRequest request, ModelMap model, Page page){
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));

		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(studentName)){
			condition.put("studentName", studentName);
		}
		condition.put("batchType", "thesis");
		if(ExStringUtils.isNotBlank(examSubId)){
			Page examResultsList = examResultsService.findAuditExamResulstsVoByCondition(condition, page);
			model.addAttribute("examResultsList", examResultsList);
		}
		model.addAttribute("condition",condition);
		return "/edu3/teaching/examResult/thesisExamResults-audit-list";
	}
	/**
	 * 编辑毕业论文成绩
	 * @param resourceid
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/thesis/correct/edit.html")
	public String editAuditThesisExamResult(String resourceid,ModelMap model){
		if(ExStringUtils.isNotBlank(resourceid)){
			AuditExamResultsVo vo = examResultsService.findAuditExamResulstsVoByExamResultsId(resourceid);
			model.addAttribute("examResultAudit",vo);
		}
		return "/edu3/teaching/examResult/thsisExamResults-audit-form";
	}
	/**
	 * 保存更正的毕业论文成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examresult/thesis/correct/save.html")
	public void saveAuditThesisExamResult(AuditExamResultsVo vo, HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String ,Object> map 		    = new HashMap<String, Object>();
		try {
			User curUser 				    = SpringSecurityHelper.getCurrentUser();

			if(ExStringUtils.isBlank(vo.getResourceid())){
				throw new WebException("请重新选择一条需更正的成绩");
			}
			ExamResults rs 					= examResultsService.get(vo.getResourceid());
			if(ExStringUtils.isBlank(vo.getChangedThesisScore())){
				throw new WebException("终评成绩不能为空");
			}
			if(vo.getChangedFirstScore()==null){
				vo.setChangedFirstScore(0.0);
			}
			if(vo.getChangedSecondScore()==null){
				vo.setChangedSecondScore(0.0);
			}
			PublishedExamResultsAudit audit = new PublishedExamResultsAudit();
			if(ExStringUtils.isNotBlank(vo.getResultAuditId())){
				audit 						= publishedExamResultsAuditService.get(vo.getResultAuditId());
			} else {
				audit.setExamResults(rs);
				if(ExStringUtils.isNotBlank(vo.getPaperOrderId())){//纯网络
					audit.setBeforeFirstScore(vo.getFirstScore());
					audit.setBeforeSecondScore(vo.getSecondScore());
				}
				audit.setBeforeIntegratedScore(rs.getIntegratedScore());
				audit.setBeforeExamAbnormity(rs.getExamAbnormity());
			}
			audit.setScoreType(1);
			if(ExStringUtils.isNotBlank(vo.getPaperOrderId())){//纯网络
				audit.setChangedFirstScore(vo.getChangedFirstScore()!=null?vo.getChangedFirstScore():0.0);
				audit.setChangedSecondScore(vo.getChangedSecondScore()!=null?vo.getChangedSecondScore():0.0);
			}
			audit.setChangedIntegratedScore(vo.getChangedThesisScore());
			audit.setChangedExamAbnormity(rs.getExamAbnormity());
			audit.setChangedDate(new Date());
			audit.setChangedMan(curUser.getCnName());
			audit.setChangedManId(curUser.getResourceid());
			audit.setAuditStatus(0);
			audit.setMemo(vo.getMemo());
			publishedExamResultsAuditService.saveOrUpdate(audit);

			map.put("statusCode", 200);
			map.put("message", "提交成功！");
			map.put("navTabId", "RES_TEACHING_RESULT_THESIS_CORRECT_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/examresult/thesis/correct/edit.html?resourceid="+vo.getResourceid());
		} catch (Exception e) {
			logger.error("提交更正毕业论文成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 发布的毕业论文成绩更正复审列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresult/thesis/correct/audit/list.html")
	public String listAuditThesisExamresults(HttpServletRequest request, ModelMap model, Page page){
		page.setOrderBy("examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo");
		page.setOrder(Page.ASC);

		Map<String,Object> condition = new HashMap<String, Object>();
		String auditStatus = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("auditStatus")), "0");

		if (ExStringUtils.isNotEmpty(auditStatus)) {
			condition.put("auditStatus", auditStatus);
		}
		condition.put("scoreType", 1);
		Page examResultsAuditList = publishedExamResultsAuditService.findExamResultsAuditByCondition(condition, page);
		model.addAttribute("examResultsAuditList",examResultsAuditList);
		model.addAttribute("condition",condition);
		return "/edu3/teaching/examResult/thesisExamResultsAudit-list";
	}
	/**
	 * 发布毕业论文成绩更正复审
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examresult/thesis/correct/audit/publish.html")
	public void publishAuditThesisExamresults(String resourceid, HttpServletRequest request, HttpServletResponse response){
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				publishedExamResultsAuditService.publishAuditThesisExamResulsts(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "审核发布成功！");
			}
		} catch (Exception e) {
			logger.error("审核发布成功出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核发布成功出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 在线录入成绩-ajax计算综合成绩
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/caculateIntegratedScore.html")
	public void ajaxCaculateIntegratedScore(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Map<String,Object> data = new HashMap<String, Object>();
		String resourceid 		 = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String usuallyScore      = ExStringUtils.trimToEmpty(request.getParameter("uss"));
		String writtenScore      = ExStringUtils.trimToEmpty(request.getParameter("ws"));
		String writtenHandwork   = ExStringUtils.trimToEmpty(request.getParameter("whs"));

		//20131230  卷面成绩录入  55-59 改为60
		ExamResults rs 			 = examResultsService.get(resourceid);
//		if(rs.getIsAdjust().equals("Y")){
//			if(!writtenScore.equals("")){
//				if(Integer.parseInt(writtenScore)<60&&Integer.parseInt(writtenScore)>=55){
//					writtenScore = "60";
//				}
//			}
//		}
		ExamInfo info         = rs.getExamInfo();
		data = caculateIntegratedScoreForNetStudy(info, usuallyScore, writtenScore, writtenHandwork, rs);

		renderJson(response,JsonUtils.mapToJson(data));

	}

	private Map<String,Object> caculateIntegratedScoreForNetStudy(ExamInfo info, String usuallyScore, String writtenScore, String writtenHandwork, ExamResults rs){
		Map<String,Object> data = new HashMap<String, Object>(0);
		//是否混合机考课程
		boolean isMixTrue =false;
		if (null!=info&&null!=info.getIsmixture()) {
			isMixTrue           = null==info.getIsmixture()?false:Constants.BOOLEAN_YES.equals(info.getIsmixture());
		}

		Double wsp 	   		 	 = 60D; //卷面成绩分比例
		if(null!=info&&null!=info.getStudyScorePer()){
			wsp = info.getStudyScorePer();
		}
		Double usp      		 = 100-wsp;                                                  //平时分比例

		BigDecimal divisor 		 = new BigDecimal("1");
		BigDecimal hundredBig    = new BigDecimal(100);


		if(ExStringUtils.isEmpty(usuallyScore)){
			usuallyScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenHandwork)){
			writtenHandwork = "0";
		}
		String examAbnormity     = rs!=null?rs.getExamAbnormity():"0";
		if (isMixTrue) {
			if (null==info.getMixtrueScorePer()) {
				throw new ServiceException(info.getCourse().getCourseName()+"未设置笔考成绩比例！");
			}
			try {
				if(ExStringUtils.isNotBlank(writtenHandwork)&&(Double.valueOf(writtenHandwork)<0||Double.valueOf(writtenHandwork)>info.getMixtrueScorePer())){
					writtenHandwork = String.valueOf(info.getMixtrueScorePer().intValue()) ;
					data.put("msg", (rs!=null?(rs.getStudentInfo().getStudentName()+"的"):"")+"笔考成绩必须为0-"+info.getMixtrueScorePer().intValue()+"的数字</br>");
					data.put("highest", info.getMixtrueScorePer().intValue());
				}
			} catch (Exception e) {
				data.put("result", "300");
			}

			writtenScore         = ExStringUtils.defaultIfEmpty(null!=rs?rs.getWrittenMachineScore():"", "0");
			BigDecimal wh_Score	 = new BigDecimal(writtenHandwork);
			BigDecimal w_Score	 = new BigDecimal(writtenScore);

			//混合机考课程卷面成绩字段存的是：机考+笔考
			writtenScore=(wh_Score.add(w_Score).divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
		}else {
			Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
			Matcher m = pattern.matcher(writtenScore);
			if (Boolean.FALSE == m.matches() ) {
				writtenScore = String.valueOf(100) ;
				data.put("msg", (null!=rs?(rs.getStudentInfo().getStudentName()+"的"):"")+"卷面成绩应为0-100的数字</br>");
				data.put("highest2", 100);
			}
		}

		//其它考试类型的课程卷面成绩等于录入的成绩

		BigDecimal wsBig         = new BigDecimal(writtenScore);
		BigDecimal usBig    	 = new BigDecimal(ExStringUtils.trim(usuallyScore));

		BigDecimal wsPerBig      = new BigDecimal(wsp.toString());
		BigDecimal usPerBig      = new BigDecimal(usp.toString());

		BigDecimal wsPerRateBig  = wsPerBig.divide(hundredBig,2);
		BigDecimal usPerRateBig  = usPerBig.divide(hundredBig,2);

		//成绩中成绩异常不为空时不设置卷面成绩、平时成绩、综合成绩						
		if (ExStringUtils.isNotEmpty(examAbnormity) && !Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)){

			//成绩中成绩异常为空时设置卷面成绩、平时成绩、综合成绩
		}else {

			//考试科目中平时成绩比例、卷面成绩比例不为空
			if (null!=wsp && null!=usp && wsp > 0&&usp>0){

				Double writtenScoreD = wsBig.multiply(wsPerRateBig).doubleValue();//根据批次比例算出的卷面成绩
				Double usuallyScoreD = usBig.multiply(usPerRateBig).doubleValue();//根据批次比例算出的平时成绩

//				//当综合成绩小于卷面成绩时，综合成绩等于卷面成绩
//				if ( (writtenScoreD+usuallyScoreD) < Double.parseDouble(writtenScore)){ 
//					rs.setTempintegratedScore_d((wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
//				//当综合成绩大于卷面成绩时,综合成绩计算方式为：卷面成绩*卷面成绩比例+平时成绩*平时成绩比例
//				}else{ 
				BigDecimal integratedScore = new BigDecimal(String.valueOf(writtenScoreD+usuallyScoreD));
				rs.setTempintegratedScore_d((integratedScore.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
//				}

				//考试科目中平时成绩比例、卷面成绩比例为空综合成绩等于卷面成绩
			}else {
				rs.setTempintegratedScore_d((wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
			}
		}
		data.put("integratedScore", rs.getTempintegratedScore_d());
		return data;
	}




	/**
	 * 在线录入面授/网络面授成绩-ajax计算综合成绩
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/caculateIntegratedScore.html")
	public void caculateFaceExamIntegratedScore(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> data = new HashMap<String, Object>();
		//注意 没有考试记录的情况
		String resourceid 		 = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		ExamInfo info 	   = null;
		ExamResults rs 	   = null;
		if(ExStringUtils.isNotEmpty(resourceid)){
			rs   = examResultsService.get(resourceid);
			info = rs.getExamInfo();
		}else{
			String teachingPlanCourseId  = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
			String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			Course course = planCourse.getCourse();
			String teachType = planCourse.getTeachType();
			Integer examCourseType = teachType.contains("face")?1:0;
			List<ExamInfo> list	   = examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
			//当执行这个ajax方法时 理论上都会有一个examinfo，在发送这条请求的页面打开时，就已确保会有符合条件的examinfo
			info = list.get(0);
		}
		String usuallyScore      = ExStringUtils.trimToEmpty(request.getParameter("us"));
		String writtenScore      = ExStringUtils.trimToEmpty(request.getParameter("ws"));
		String onlineScore       = ExStringUtils.trimToEmpty(request.getParameter("os"));
		if(ExStringUtils.isEmpty(usuallyScore)){
			usuallyScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}
		if(ExStringUtils.isEmpty(onlineScore)){
			onlineScore = "0";
		}

		Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
		Matcher m = pattern.matcher(writtenScore);
		if (Boolean.FALSE == m.matches() && !"作弊".equals(writtenScore) && !"缺考".equals(writtenScore) && !"无卷".equals(writtenScore) && !"其它".equals(writtenScore)) {
			writtenScore = String.valueOf(100) ;
			data.put("msg", (null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"卷面成绩应为0-100的数字</br>");
//			data.put("highest", 100);
		}
		m = pattern.matcher(usuallyScore);
		if (Boolean.FALSE == m.matches()) {
			usuallyScore = String.valueOf(100) ;
			data.put("msg", (data.containsKey("msg")?data.get("msg"):"")+(null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"平时考核成绩应为0-100的数字</br>");
//			data.put("highest1", 100);
		}
		m = pattern.matcher(onlineScore);
		if (Boolean.FALSE == m.matches()) {
			onlineScore = String.valueOf(100) ;
			data.put("msg", (data.containsKey("msg")?data.get("msg"):"")+(null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"网上学习成绩应为0-100的数字</br>");
//			data.put("highest2", 100);
		}
		// 获取成绩计算规则字典表
		List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
		Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
		for(Dictionary d : resultCalculateRuleList) {
			resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
		}

		String integratedScoreStr = examResultsService.caculateIntegrateScore(info,usuallyScore,writtenScore,onlineScore,rs,resultCalculateRuleMap);
		data.put("integratedScore",integratedScoreStr);
		renderJson(response,JsonUtils.mapToJson(data));

	}

	/**
	 * 在线录入面授/网络面授成绩-ajax计算综合成绩(补考)
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/noncaculateIntegratedScore.html")
	public void noncaculateFaceExamIntegratedScore(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> data = new HashMap<String, Object>();
		Map<String,Object> condition = new HashMap<String, Object>();
		//注意 没有考试记录的情况
		String resourceid 		 = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String teachingPlanCourseId  = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
		String studyNo 				 =  ExStringUtils.trimToEmpty(request.getParameter("studyno"));
		String only				     = ExStringUtils.trimToEmpty(request.getParameter("only"));
		String courseid 		     = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String examAbnormity    = ExStringUtils.trimToEmpty(request.getParameter("examAbnormity"));
		String examSubId    = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));

//		String examSubId = null;

		ExamResults rs 	   = null;
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次

//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}

		if(ExStringUtils.isNotEmpty(resourceid)){
			rs   = examResultsService.get(resourceid);
		}

		String usuallyScore      = "";
		String writtenScore      = ExStringUtils.trimToEmpty(request.getParameter("ws"));
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}

		Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
		Matcher m = pattern.matcher(writtenScore);
		if (Boolean.FALSE == m.matches() ) {
			writtenScore = String.valueOf(100) ;
			data.put("msg", (null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"卷面成绩应为0-100的数字</br>");
			data.put("highest", 100);
		}
		//取消计算，直接赋成绩
		String integratedScoreStr = writtenScore;
		data.put("integratedScore",integratedScoreStr);
//		renderJson(response,JsonUtils.mapToJson(data));

		//======================


		if(ExStringUtils.isNotBlank(teachingPlanCourseId)) {
			condition.put("plansourceid", teachingPlanCourseId);
		}

		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL		
		User user              	 = SpringSecurityHelper.getCurrentUser();        //当前用户
		Double per 					 = 60D;


		ExamResults examResults = null;
		if(ExStringUtils.isNotBlank(examSubId)){

			if (ExStringUtils.isNotBlank(examSubId) && studyNo!=""){
				ExamSub examSub              = examSubService.get(examSubId);    			 //要录入成绩的考试批次
				TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
				Course course = courseService.get(courseid);
				StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNo);

				String teachType = planCourse.getTeachType();
				Integer examCourseType = teachType.contains("face")?1:0;

				ExamInfo examInfo 	   = null;
				List<ExamInfo> list	   = examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
				if (null!=list && !list.isEmpty()) {
					examInfo = list.get(0);
				} else {
					examInfo = new ExamInfo();
					examInfo.setCourse(course);
					examInfo.setIsOutplanCourse("0");
					examInfo.setExamSub(examSub);
					examInfo.setExamCourseType(examCourseType);

					if (1==examCourseType) {
						if (null!=examSub.getFacestudyScorePer()) {
							per=examSub.getFacestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					}
					if(planCourse.getScoreStyle()!=null){
						examInfo.setCourseScoreType(planCourse.getScoreStyle());
					}else {
						examInfo.setCourseScoreType(examSub.getCourseScoreType());
					}
					//examInfo.setCourseScoreType(examSub.getCourseScoreType());

					examInfoService.save(examInfo);
				}

				try {

					String resultsId = resourceid;

					if(ExStringUtils.isNotBlank(resultsId)){
						examResults = examResultsService.get(resultsId);//要保存的成绩
					} else {
						examResults = new ExamResults();
						examResults.setCourse(course);
						examResults.setExamInfo(examInfo);
						if(null != planCourse){
							examResults.setMajorCourseId(planCourse.getResourceid());
						}

						examResults.setIsMakeupExam(Constants.BOOLEAN_YES);
						examResults.setExamsubId(examSubId);//这里要改

						//examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
						examResults.setCheckStatus("0");

						examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
						examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
						examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
						examResults.setCourseType(planCourse.getCourseType());
						examResults.setStudentInfo(studentInfo);

						examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
						examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
						examResults.setCourseScoreType(examInfo.getCourseScoreType());
						List examCountList      = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
								examResults.getStudentInfo().getResourceid(),ExStringUtils.trimToEmpty(examResults.getResourceid()));
						long examCount          = (Long)examCountList.get(0);			         //选考次数
						examResults.setExamCount(examCount+1);
					}

					examResults.setPlanCourseTeachType(planCourse.getTeachType());
					int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
					if (checkStatus>0) {
						throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
					}

					if (ExStringUtils.isEmpty(examAbnormity)) {
						examResults.setExamAbnormity("0");
					} else {
						examResults.setExamAbnormity(examAbnormity);
					}

					String integratedScore  = writtenScore;

					if("0".equals(examResults.getExamAbnormity())){
						if(ExStringUtils.isNotBlank(writtenScore)&&(Double.valueOf(writtenScore)<0||Double.valueOf(writtenScore)>100)
								){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩必须为0-100分");
						}
					}
					if(ExStringUtils.isNotBlank(writtenScore)){
						BigDecimal w_Score	 = new BigDecimal(writtenScore);
						examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
					}
					if(ExStringUtils.isNotBlank(usuallyScore)){
						BigDecimal u_Score	 = new BigDecimal(usuallyScore);
						examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
					}
					if(ExStringUtils.isNotBlank(integratedScore)){
						BigDecimal i_Score	 = new BigDecimal(integratedScore);
						examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
					}
					examResults.setFillinDate(new Date());
					examResults.setFillinMan(user.getCnName());
					examResults.setFillinManId(user.getResourceid());

					examResultsService.saveOrUpdate(examResults);

					if(null != planCourse){
						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),planCourse.getResourceid());
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}
						studentLearnPlanService.saveOrUpdate(learnPlan);
					}

				} catch (Exception e) {
					logger.error("保存学生成绩异常，学生学号：{}"+studyNo,e.fillInStackTrace());
					success = false;
					msg     = "录入学生成绩异常:"+e.getMessage();
				}
			} else {
				success = false;
				msg = "没有补考考试批次!";
			}

		}else {
			success = false;
			msg = "没有补考考试批次!";
		}

		data.put("examAbnormity",ExStringUtils.isEmpty(examAbnormity)?"0":examAbnormity);
		if(null != examResults && null != examResults.getCheckStatus()){
			String checkStatusMsg = "";
			if(Integer.parseInt(examResults.getCheckStatus()) > -1){
				checkStatusMsg += "<td id='nonfacestudy_checkStatus_"+studyNo+ "' style='color:blue'>";
				checkStatusMsg += JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", examResults.getCheckStatus());
			}
			if(Integer.parseInt(examResults.getCheckStatus())==0){
				checkStatusMsg += "<a href='javaScript:void(0)' onclick=\"delnonfacestudyExamResults('"+examResults.getResourceid()+"','"+examResults.getCheckStatus()+"','"+examResults.getStudentInfo().getStudentName()+"','"+examResults.getCourse().getCourseShortName()+"')\">| 撤销</a>";
			}
			checkStatusMsg +="</td>";
			data.put("checkStatus",checkStatusMsg);
			data.put("resultsId",examResults.getResourceid());
		}

		data.put("success",success);
		if (ExStringUtils.isNotEmpty(msg)) {
			data.put("msg", msg);
		}

		renderJson(response,JsonUtils.mapToJson(data));
	}

	/**
	 * 补考学生列表
	 * @param request
	 * @param response
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/nonexamination-student-list.html")
	public String failExamStudentList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){
		page.setOrderBy("examCourseCode");
		page.setOrder(Page.ASC);

		try {
			Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			//String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			//String courseName 			 = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
			//String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			//String gradeId  			 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
			//String major  				 = ExStringUtils.trimToEmpty(request.getParameter("major"));
			//String classesId  			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			//String classic  			 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
			//String term  			     = ExStringUtils.trimToEmpty(request.getParameter("term"));
			//String teachType 			 = "facestudy" ;
			String fromPage 			 = ExStringUtils.trimToEmpty(request.getParameter("fromPage"));
			//String isPass 			     = ExStringUtils.trimToEmpty(request.getParameter("isPass"));
			//String finalPass 			     = ExStringUtils.trimToEmpty(request.getParameter("finalPass"));
			//String yearId = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
//			String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
			//String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));
			User user                    = SpringSecurityHelper.getCurrentUser();
			//String unitId 				 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
			//Date curDate = new Date();
			//Integer timeStatus = 0 ;
			//String branchSchoolName = "";

			//String resultsCourseIds 	 = "";
			//boolean isBrSchool = false;
			//boolean isadmin = false;
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				//unitId =	user.getOrgUnit().getResourceid();
				branchSchool = user.getOrgUnit().getResourceid();
				//branchSchoolName = user.getOrgUnit().getUnitName();
				//isBrSchool = true ;
				//model.addAttribute("isBrSchool", "Y");
			}else{
				//不是学习中心教务员就认为是可以总管所有学习中心的管理人员
				//isadmin=true;
				model.addAttribute("isadmin", "Y");
			}
			boolean isTeacher 			 = false;
			//String isAbnormityInput      = "";
			//Date curTime                 = new Date();
			if (ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			/*if (ExStringUtils.isNotEmpty(courseName)) {
				condition.put("courseName", courseName);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(gradeId)) {
				condition.put("gradeId", gradeId);
			}
			if (ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if (ExStringUtils.isNotEmpty(classesId)) {
				condition.put("classesId", classesId);
			}
			if (ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if (ExStringUtils.isNotEmpty(examSubId)) {
				condition.put("examSubId", examSubId);
			}
			if (ExStringUtils.isNotEmpty(isPass)) {
				condition.put("isPass", isPass);
			}
			if (ExStringUtils.isNotEmpty(finalPass)) {
				condition.put("finalPass", finalPass);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("term", term);
			}
			if (ExStringUtils.isNotEmpty(yearId)) {
				condition.put("yearId", yearId);
			}
			if (ExStringUtils.isNotEmpty(examType)) {
				condition.put("examType", examType);
			}
			condition.put("teachType", teachType);*/
			//		if (ExStringUtils.isNotEmpty(examSubId)) {
			//
			//			ExamSub examSub          = examSubService.get(examSubId);
			//			model.addAttribute("examSub", examSub);
			////			condition.put("examSubId", examSubId);
			//
			//			//考试批次对应的学期
			//			String subTerm  = examSub.getTerm();
			//			if(ExStringUtils.isNotEmpty(subTerm)){
			//				condition.put("subTerm",subTerm);
			//			}
			//			Long subYear = examSub.getYearInfo().getFirstYear();
			////			condition.put("subYear",subYear);
			//
			//		}

			//String teacherCode       = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();
			//String abnormityInput    = CacheAppManager.getSysConfigurationByCode("examresultsAbnormityInput").getParamValue();//异常成绩录入人角色编码
			//isTeacher                = user.isContainRole(teacherCode);
			//isAbnormityInput         = user.isContainRole(abnormityInput)==true?"Y":"N";
			//		if(isTeacher&&isAbnormityInput.equals("Y")){
			//
			//		}

//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//			if(!"年".equals(batchName.substring(4, 5))){
//				StringBuffer sb = new StringBuffer(batchName);
//				sb.insert(4, "年");
//				batchName = sb.toString();
//			}
//			logger.info("考试批次:{}", batchName);
//			paramMap.put("batchName", batchName);
//			List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次

//			String examSubId2 = examSubId;
//			if(null != examList && examList.size()>0){
//				examSubId = examList.get(0).getResourceid();
//			}
//			
//			if (ExStringUtils.isNotEmpty(examSubId))		condition.put("examSubId", examSubId);

			if("Y".equals(fromPage)){
				page = examResultsService.findFailExamResultStudentList(page,condition);
			}
			condition.put("fromPage","Y");

//			List<ExamSub> examSubs = examList;
//					examSubService.findByHql(" from "+ExamSub.class.getSimpleName()+" where isDeleted = 0 and batchType='exam' and isDeleted= 0 and batchName not like '%补考' order by examinputStartTime desc ");
//			model.addAttribute("examSubs",examSubs);

//			examSubId = examSubId2;
//			condition.put("examSubId", examSubId);
			//logger.error(((FailExamStudentVo)(page.getResult().get(0))).getIntegratedscore());
			model.addAttribute("page", page);
			model.addAttribute("condition", condition);
			//model.addAttribute("timeStatus", timeStatus);
			//model.addAttribute("test", "W7sm+6r9pU8=");

			//model.addAttribute("branchSchoolName", branchSchoolName);
			model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		} catch (Exception e) {
			logger.error("获取补考学生列表失败："+e.getMessage());
			throw new DataRetrievalFailureException("获取补考学生列表失败: " + e.getMessage(), e);
		}

		return "/edu3/teaching/examResult/nonExamination-student-list";
	}

	/**
	 * 补考学生列表(班主任角色)
	 * @param request
	 * @param response
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/nonExaminationToMaster-stu-list.html")
	public String failExamStudentToMasterList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){
		page.setOrderBy("examCourseCode");
		page.setOrder(Page.ASC);

		try {
			Map<String,Object> condition = new HashMap<String, Object>();
			String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			String courseName 			 = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
			String gradeId  			 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
			String major  				 = ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classesId  			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			String term  			 = ExStringUtils.trimToEmpty(request.getParameter("term"));
			String teachType 			 = "facestudy" ;
			String fromPage 			 = ExStringUtils.trimToEmpty(request.getParameter("fromPage"));

			User user                    = SpringSecurityHelper.getCurrentUser();
			String unitId 				 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
			Date curDate = new Date();
			Integer timeStatus = 0 ;
			boolean isadmin=false;
			if(SpringSecurityHelper.isUserInRole("ROLE_ADMINISTRATOR")||SpringSecurityHelper.isUserInRole("ROLE_ADMIN")){
				isadmin=true;
				model.addAttribute("isadmin", "Y");
			}
			if(!"".equals(unitId)){
				condition.put("unitId", unitId);
			}
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				unitId =	user.getOrgUnit().getResourceid();
//				condition.put("unitId", unitId);
				branchSchool = unitId;
			}
			String resultsCourseIds 	 = "";
			boolean isBrSchool = false;
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				isBrSchool = true ;
				model.addAttribute("isBrSchool", "Y");
			}else{
				//不是学习中心教务员就认为是可以总管所有学习中心的管理人员
				isadmin=true;
				model.addAttribute("isadmin", "Y");
			}
			boolean isTeacher 			 = false;
			String isAbnormityInput      = "";
			Date curTime                 = new Date();

			if (ExStringUtils.isNotEmpty(courseName)) {
				condition.put("courseName", courseName);
			}
			if (ExStringUtils.isNotEmpty(gradeId)) {
				condition.put("gradeId", gradeId);
			}
			if (ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if (ExStringUtils.isNotEmpty(classesId)) {
				condition.put("classesId", classesId);
			}
			//		if (ExStringUtils.isNotEmpty(examSubId))		condition.put("examSubId", examSubId);
			if (ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			condition.put("teachType", teachType);
			//		if (ExStringUtils.isNotEmpty(examSubId)) {
			//
			//			ExamSub examSub          = examSubService.get(examSubId);
			//			model.addAttribute("examSub", examSub);
			////			condition.put("examSubId", examSubId);
			//
			//			//考试批次对应的学期
			//			String subTerm  = examSub.getTerm();
			//			if(ExStringUtils.isNotEmpty(subTerm)){
			//				condition.put("subTerm",subTerm);
			//			}
			//			Long subYear = examSub.getYearInfo().getFirstYear();
			////			condition.put("subYear",subYear);
			//
			//		}

			String teacherCode       = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();
			String abnormityInput    = CacheAppManager.getSysConfigurationByCode("examresultsAbnormityInput").getParamValue();//异常成绩录入人角色编码
			isTeacher                = user.isContainRole(teacherCode);
			isAbnormityInput         = user.isContainRole(abnormityInput)==true?"Y":"N";
			//		if(isTeacher&&isAbnormityInput.equals("Y")){
			//
			//		}

			Map<String, Object> paramMap = new HashMap<String, Object>();
			String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
			if(!"年".equals(batchName.substring(4, 5))){
				StringBuffer sb = new StringBuffer(batchName);
				sb.insert(4, "年");
				batchName = sb.toString();
			}
			logger.info("考试批次:{}", batchName);
			paramMap.put("batchName", batchName);
			List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次

			String examSubId2 = examSubId;
			if(null != examList && examList.size()>0){
				examSubId = examList.get(0).getResourceid();
			}

			if (ExStringUtils.isNotEmpty(examSubId)) {
				condition.put("examSubId", examSubId);
			}

			if("Y".equals(fromPage)&&user.isContainRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.master").getParamValue())){
				condition.put("classesMasterId", user.getResourceid());
				page = examResultsService.findFailExamResultStudentList(page,condition);
			}
			condition.put("fromPage","Y");

			List<ExamSub> examSubs = examList;
//					examSubService.findByHql(" from "+ExamSub.class.getSimpleName()+" where isDeleted = 0 and batchType='exam' and isDeleted= 0 and batchName not like '%补考' order by examinputStartTime desc ");
			model.addAttribute("examSubs",examSubs);

			examSubId = examSubId2;
			condition.put("examSubId", examSubId);
			model.addAttribute("page", page);
			model.addAttribute("condition", condition);
			model.addAttribute("timeStatus", timeStatus);
		} catch (Exception e) {
			logger.error("获取补考学生列表失败："+e.getMessage());
			throw new DataRetrievalFailureException("获取补考学生列表失败: " + e.getMessage(), e);
		}

		return "/edu3/teaching/examResult/nonExaminationToMaster-stu-list";
	}

	/**
	 * 补考名单(查询学生成绩)
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	//@RequestMapping("/edu3/teaching/result/nonexamination-student-list.html")
	public String nonexaminationStudentList(HttpServletRequest request, HttpServletResponse response,
											ModelMap model, Page objPage){

		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("grade.yearInfo.firstYear");

		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
		String learningStyle		 = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String defaultCheckAll       = ExStringUtils.trimToEmpty(request.getParameter("defaultCheckAll"));
		String graduateDate          = ExStringUtils.trimToEmpty(request.getParameter("graduateDateStr"));
		String courseName				 = ExStringUtils.trimToEmpty(request.getParameter("courseName"));

		User curUser                 = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit                 = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unit                         = curUser.getOrgUnit();
			model.addAttribute("isBrschool", true);
		}

		if (null!=unit) {
			branchSchool = unit.getResourceid();
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus",studentStatus);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}
		if(ExStringUtils.isNotEmpty(defaultCheckAll)) {
			condition.put("defaultCheckAll",defaultCheckAll);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName",courseName);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)){
			condition.put("graduateDateStr",graduateDate);
			condition.put("graduateDate", ExDateUtils.convertToDate(condition.get("graduateDateStr").toString()));
		};

		String studentHQL			 = genHQL(condition,"studentInfo");

		objPage = studentinfoservice.findByHql(objPage,studentHQL,  condition);

		try {
			List<Map<String,Object>> graduateDateList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(" select distinct g.graduatedate from EDU_TEACH_GRADUATEDATA g where g.isdeleted = 0 order by g.graduatedate desc", null);
			for (Map<String,Object> m :graduateDateList) {
				Date gd = (Date)m.get("graduatedate");
				m.put("graduatedate", ExDateUtils.formatDateStr(gd, ExDateUtils.PATTREN_DATE));

			}
			model.addAttribute("graduateDateList", graduateDateList);
		} catch (Exception e) {
			e.printStackTrace();
		}

		condition.remove("graduateDate");
		model.addAttribute("condition", condition);
		model.addAttribute("objPage",objPage);

		return "/edu3/teaching/examResult/nonexamination-student-list";
	}

	/**
	 * 补考成绩录入-课程列表(补考)
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresults/failexam-course-list.html")
	public String failexamCourseList(HttpServletRequest request, HttpServletResponse response,
									 ModelMap model, Page objPage) throws ParseException{

//		objPage.setOrderBy("term");
		objPage.setOrder(Page.ASC);//设置默认排序方式
//		objPage.setPageSize(50);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);//查询条件
		Map<String, Object> condition2 = new HashMap<String, Object>();

		String fromPage 			 = ExStringUtils.trimToEmpty(request.getParameter("fromPage"));
		//String examSubId			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
//		String examResult			 = ExStringUtils.trimToEmpty(request.getParameter("examResult")); 
		//String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		//String teachType 			 = "facestudy" ;
		String msg 					 = "";
		//String classesId             = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		//String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		//String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		//String classicId  = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		//String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
		//String failResultStatus = ExStringUtils.trimToEmpty(request.getParameter("failResultStatus"));
//		String courseName = ExStringUtils.trimToEmpty(request.getParameter("courseName"));		
		//String yearId = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
		//String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));

		//		if(ExStringUtils.isNotEmpty(courseName))       condition.put("courseName", courseName);
		//		if(ExStringUtils.isNotEmpty(examResult))  condition.put("examResult", examResult);
		/*if(ExStringUtils.isNotBlank(teachType))  condition.put("teachType", teachType);
		if(ExStringUtils.isNotEmpty(examSubId))  condition.put("examSubId", examSubId);
		if(ExStringUtils.isNotEmpty(gradeId))    condition.put("gradeId", gradeId);
		if(ExStringUtils.isNotEmpty(classicId))    condition.put("classicId", classicId);
		if(ExStringUtils.isNotEmpty(major))      condition.put("major", major);
		if(ExStringUtils.isNotEmpty(term))       condition.put("term", term);
		if(ExStringUtils.isNotEmpty(courseId))       condition.put("courseId", courseId);
		if(ExStringUtils.isNotEmpty(classesId))		   condition.put("classesId", classesId);
		if(ExStringUtils.isNotEmpty(failResultStatus))		   condition.put("failResultStatus", failResultStatus);
		if(ExStringUtils.isNotEmpty(yearId))		   condition.put("yearId", yearId);
		if(ExStringUtils.isNotEmpty(examType))		   condition.put("examType", examType);*/
		//condition.put("teachType", "facestudy");
		//String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("isBranchSchool", "Y");
			condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
			condition2.put("brSchoolid", curUser.getOrgUnit().getResourceid());
			//classesCondition = " and cl.ORGUNITID='" + curUser.getOrgUnit().getResourceid() + "'";
		}else{
			model.addAttribute("isadmin", "Y");
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
				condition2.put("brSchoolid", branchSchool);
				//classesCondition = " and cl.ORGUNITID='" + branchSchool + "'";
			}
		}

		// 判断是否顶级机构人员
		boolean isTop = false;
		List<User> users = userService.findByHql("from "+User.class.getSimpleName()+" u where u.orgUnit.unitCode=?", "UNIT_ROOT");
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			if (user.getResourceid().equals(curUser.getResourceid())) {
				isTop = true;
				break;
			}
		}

		if (!isTop) {
			// 判断是否教务员
			boolean isJwy = false;
			//是否成绩查询
			boolean isCjcx = false;
			Set<Role> roles = curUser.getRoles();
			for (Role role : roles) {
				if ("ROLE_BRS_STUDENTSTATUS".equals(role.getRoleCode())) {
					isJwy = true;
					break;
				}else if ("ROLE_queryscore".equals(role.getRoleCode())) {
					isCjcx = true;
				}
			}
			if (isJwy || isCjcx) {
				if(!isJwy){
					condition.put("isAllow", "N");
				}else {// 教务员录入自己负责的教学站班级的课程成绩
					condition.put("jwyId", curUser.getResourceid());
				}

			} else {
				// 登分老师录入自己负责的班级的课程成绩
				condition.put("teachId", curUser.getResourceid());
			}
			if(null!=curUser.getOrgUnit()){
				if(!SpringSecurityHelper.isTeachingCentreTeacher(curUser)){
					branchSchool = curUser.getOrgUnit().getResourceid();
				}
			}
		}
		// 检查预约时间是否结束，只有结束才能录入成绩
//		if(ExStringUtils.isNotEmpty(examSubId)) {
//			ExamSub examSub 		 = examSubService.get(examSubId);
//			Date currentTime 			 = new Date();
//			Date endTime 		     = examSub.getEndTime();
//			if(currentTime.after(endTime)){
		//20181015  因为系统中已经有统考成绩录入，因此在这个成绩录入处进行过滤 2162
		condition.put("filterExamClassType", "3");
		if("Y".equals(fromPage)){
			objPage = examResultsService.failExamStudentListNew(objPage,condition);
//					List list = examResultsService.failExamStudentList(condition);
			model.addAttribute("failExamCourseList", objPage);
		}
//			} else {
//				msg  = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong>"
//	    				+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);		
//			}
//		} else {
//			if("Y".equals(fromPage)){
//				msg = "请选择一个考试批次";
//			}
//		}

		condition.remove("examSubId");
		condition.put("isDeleted",0);
		condition.put("studentStatus1", "11");
		condition.put("studentStatus2", "21");
		condition.put("isPass",Constants.BOOLEAN_NO);
		condition.put("msg", msg);
		condition.put("fromPage", "Y");

		model.addAttribute("condition", condition);
		//model.addAttribute("classesCondition", classesCondition);
		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());

		return "/edu3/teaching/examResult/failexam-course-list";
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresults/failExamList2Excel.html")
	public void failExamList2Excel(HttpServletRequest request, HttpServletResponse response) throws ParseException{

		Map<String,Object> condition = new HashMap<String,Object>();//查询条件

		String examSubId			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String teachType 			 = "facestudy" ;
		String classesId             = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String failResultStatus = ExStringUtils.trimToEmpty(request.getParameter("failResultStatus"));
		String yearId = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
		String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));

		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(teachType)) {
			condition.put("teachType", teachType);
		}
		if(ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(classesId))	{
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(failResultStatus))	{
			condition.put("failResultStatus", failResultStatus);
		}
		if(ExStringUtils.isNotEmpty(yearId)) {
			condition.put("yearId", yearId);
		}
		if(ExStringUtils.isNotEmpty(examType)) {
			condition.put("examType", examType);
		}
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("isBranchSchool", "Y");
			condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
		}else{
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
		}

		List<Map<String, Object>> vo = examResultsService
				.failExamStudentList2Excel(condition);
		String headerName = "补考录入情况列表";
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + "exportfiles");
		GUIDUtils.init();
		// 导出
		File excelFile = null;
		File disFile = new File(getDistfilepath() + File.separator
				+ GUIDUtils.buildMd5GUID(false) + ".xls");

		// 初始化配置参数
		exportExcelService.initParmasByfile(disFile, "FailExamList2Excel", vo,
				null, null);
		exportExcelService.getModelToExcel().setHeader(headerName);
		exportExcelService.getModelToExcel().setRowHeight(300);// 设置行高

		excelFile = exportExcelService.getExcelFile();// 获取导出的文件
		logger.info("获取导出的excel文件:{}", excelFile.getAbsoluteFile());
		String downloadFileName = headerName + ".xls";
		String downloadFilePath = excelFile.getAbsolutePath();
		try {
			downloadFile(response, downloadFileName, downloadFilePath, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 查询上次补考是否未录入
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examresults/checkLastFaileExam.html")
	public void checkLastFaileExam(HttpServletRequest request, HttpServletResponse response, Page page){
		//Page page = new Page(1, 100, false, Page.DESC, "");
		String userRole = "";
		String courseInfo = "";
		User user  = SpringSecurityHelper.getCurrentUser();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> condition = new HashMap<String, Object>();
		map.put("statusCode", 200);
		map.put("message", "");
		//获取用户角色
		try {
			/*List<Role> roleList = new ArrayList<Role>(user.getRoles());		
			for(Role role:roleList){
				if(role.getRoleCode().indexOf("ROLE_BRS_STUDENTSTATUS") >=0){
					userRole="jwy";
					break;
				}else if ("ROLE_LINE,ROLE_TEACHER_DUTY".contains(role.getRoleCode())) {
					userRole = "teacher";
				}
			}*/
			String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			String courseId	 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			String examType	 = ExStringUtils.trimToEmpty(request.getParameter("examType"));
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			if(ExStringUtils.isNotBlank4All(classesId,courseId,examType) && !"NY".contains(examType)){
				List<Map<String, Object>> result = null;
				String message = "<lable style='color: red;'>以下考试课程还未提交成绩，请及时录入并提交成绩！</lable><br>";
				/*if(userRole.equals("jwy")){
					condition.put("branchSchool", user.getOrgUnit().getResourceid());
				}else{
					condition.put("teachId", user.getResourceid());
				}*/
				condition.put("classesId", classesId);
				condition.put("courseId", courseId);
				condition.put("examType", examType=="Q"?"T":"Y");
				condition.put("teachType","facestudy");
				condition.put("resultStatus","notAllInput");
				condition.put("queryNotAllInput", "Y");

				//正考
				/*page = teachingJDBCService.findTeachingPlanClassCourseByCondition(condition, page);
				result = page.getResult();
				if(result!=null && result.size()>0){
					for (Map<String, Object> temp : result) {
						message += "班级："+temp.get("classesname")+"<br>考试批次："+temp.get("batchname")+"<br>课程："+temp.get("coursename")+"<br><br>";
					}
					courseInfo +=  "正考<lable style='color: red;'>"+result.size()+"</lable>门未录入<br>";
				}*/
				//补考
				condition.put("failResultStatus","notAllInput");
				page = examResultsService.failExamStudentListNew(page,condition);
				result = page.getResult();
				if(result!=null && result.size()>0){
					for (Map<String, Object> temp : result) {
						message += "班级："+temp.get("classesname")+"<br>考试批次："+temp.get("batchname")+"<br>课程："+temp.get("coursename")+"<br><br>";
					}
					courseInfo +=  "补考<lable style='color: red;'>"+result.size()+"</lable>门未录入";
					map.put("statusCode", 300);
					map.put("message", message);
				}
			}
		} catch (Exception e) {
			logger.error("查询未录入失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "查询失败");
		}
		map.put("courseInfo", courseInfo);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 面授/网络面授成绩录入列表（补考--------------）
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/non-input-examresults-list.html")
	public String nonInputFacestudyExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage)
			throws ParseException{
		//objPage.setOrderBy("unit.unitname,major.majorname,stu.studyno,edumakeup.courseid ");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = new HashMap<String, Object>();
		String gradeId 				 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String classesId			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String majorid        = ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String branchschoolid               = ExStringUtils.trimToEmpty(request.getParameter("branchschoolid"));
		String courseid           = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String teachingPlanCourseId           = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
		String  checkStatus          = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String  studentName          = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String  studyNo          = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String  examSubId          = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String msg                   = "";
//		if(ExStringUtils.isNotEmpty(examSubId))     condition.put("examSubId",examSubId);
		if(ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId",gradeId);
		}
		if(ExStringUtils.isNotEmpty(majorid)) {
			condition.put("majorid",majorid);
		}
		if(ExStringUtils.isNotEmpty(branchschoolid)) {
			condition.put("branchschoolid",branchschoolid);
		}
		if(ExStringUtils.isNotEmpty(courseid)) {
			condition.put("courseid",courseid);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId",classesId);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus",checkStatus);
		}
		if(ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName",studentName);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		if(ExStringUtils.isNotEmpty(teachingPlanCourseId))     {
			condition.put("teachingPlanCourseId",teachingPlanCourseId);
		}
		Course course = courseService.get(courseid);
		model.addAttribute("course", course);
		String isMachineExam = Constants.BOOLEAN_NO;
		String _unitId = "";
//		String examSubId="";

		//补考考试批次
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("成绩录入考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次
//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}
//		if(ExStringUtils.isNotEmpty(examSubId))     condition.put("examSubId",examSubId);
		ExamSub examSub = null;
		if (ExStringUtils.isNotBlank(examSubId)){
			examSub 			 = examSubService.get(examSubId);
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
//			model.addAttribute("course", planCourse.getCourse());
			User curUser = SpringSecurityHelper.getCurrentUser();
			//Date curTime             = new Date();
			Date endTime  = null==examSub.getEndTime()?new Date():examSub.getEndTime();

//			if (curTime.after(endTime)){	
			if(null != curUser){
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
					branchschoolid = curUser.getOrgUnit().getResourceid();
				}
			}

			condition.put("examSubId",examSubId);
			objPage 	  = teachingJDBCService.findNonFaceStudyExamResultsVo(objPage, condition);
			//StringBuilder stuIds = new StringBuilder();
//			}else {
//				msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
//			    		+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);			
//			}			
		}else {
			msg 	= "没有补考考试批次！";
		}
		condition.put("msg",msg);
		//对于进入页面前，那些没有计算综合成绩且没有提交的成绩记录计算综合成绩
		List<ExamResultsMakeUpVo> scoreList = objPage.getResult();

//		TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
//		Course course = planCourse.getCourse();
//		String teachType = planCourse.getTeachType();
//		Integer examCourseType = teachType.contains("face")?1:0;

		if(null != examSub){
			examSubId = examSub.getResourceid();

			if(scoreList.size()>0){
				isMachineExam = scoreList.get(0).getIsMachineExam();
				_unitId = scoreList.get(0).getBranchSchool();
				for (ExamResultsMakeUpVo vo : scoreList) {
					if(ExStringUtils.isNotEmpty(vo.getCheckStatusMakeup())&&Integer.valueOf(vo.getCheckStatusMakeup())<1 && "0".equals(vo.getExamAbnormity())){
						String integratedscoreMakeup = vo.getWrittenScoreMakeup();
						vo.setIntegratedscoreMakeup(integratedscoreMakeup);
						if(ExStringUtils.isNotEmpty(vo.getResultidMakeup())){
							ExamResults rs = examResultsService.get(vo.getResultidMakeup());
							rs.setIntegratedScore(integratedscoreMakeup);
							examResultsService.update(rs);
						}
					} else {
						if (ExStringUtils.isNotBlank(vo.getExamResultsResourceId())) {
//							vo.setIntegratedScore("0");
							vo.setExamResultsChs(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", ExStringUtils.trimToEmpty(vo.getExamAbnormity())));
						}
					}
				}
			}
		} else{
			//success = false;
			msg = "没有补考考试批次!";
		}

		condition.put("msg", msg);
		if(ExStringUtils.isNotEmpty(teachingPlanCourseId))     {
			condition.put("plansourceid",teachingPlanCourseId);
		}

		String scoreSaveMode = CacheAppManager.getSysConfigurationByCode("score.save.mode").getParamValue();
		condition.put("scoreSaveMode", scoreSaveMode);
		if ("0".equals(scoreSaveMode)) {
			condition.put("isAllowInput", "N");
		} else {
			condition.put("isAllowInput", "Y");
		}
		condition.put("isMachineExam", isMachineExam);
		condition.put("schoolId", _unitId);

		model.addAttribute("page", objPage);
		model.addAttribute("condition",condition);
		return "/edu3/teaching/examResult/nonfacestudyExamResults-input-list";
	}

	/**
	 * 查询要补考的人数
	 * @param condition
	 * @throws Exception
	 */
	private void queryNonTotalNum(Map<String, Object> condition) throws Exception {
		StringBuffer sql = new StringBuffer("select pc.resourceid||'_'||stu.classesid resourceid,count(stu.resourceid) counts from edu_teach_plancourse pc  ");
		sql.append(" inner join edu_roll_studentinfo stu on stu.teachplanid = pc.planid and (stu.studentstatus = :studentStatus1 or stu.studentstatus = :studentStatus2 or stu.studentstatus = :studentStatus3) ");

		if(condition.containsKey("branchSchool")){
			sql.append(" and stu.branchschoolid =:branchSchool ");
		}


//		sql.append(" left join ( select * from edu_teach_examresults re where re.isdeleted=0 and re.examsubid = :examSubId and re.courseid=:courseid) edumakeup on stu.resourceid=edumakeup.studentid  ");





		sql.append(" where pc.isdeleted = :isDeleted  ");

//		String examResultsTimes=null;
//		if(CacheAppManager.getSysConfigurationByCode("examResultsTimes")!=null){
//
//			examResultsTimes=CacheAppManager.getSysConfigurationByCode("examResultsTimes").getParamValue();
//		}
//        if(examResultsTimes!=null){
//			int ksNum=  Integer.parseInt(examResultsTimes);
//			ksNum+=1;
//			sql.append(" and  (select count(*) from EDU_TEACH_EXAMRESULTS re " +
//        			"where re.isdeleted = 0 and re.COURSEID = a.courseid " +
//        			"and re.studentid = a.studentid and re.checkstatus='4')<:ksNum "); 
//			condition.put("ksNum",ksNum);
//		}

		if (condition.containsKey("courseId")) {
			sql.append(" and pc.courseid = :courseId ");
		}
		if (condition.containsKey("teachType")) {
			sql.append(" and pc.teachtype = :teachType ");
		}


		sql.append("  and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =pc.courseid and score.ispass = :isPass )");
		sql.append(" group by pc.resourceid,stu.classesid ");
		List<Map<String,Object>> totalList =   baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
		for (Map<String,Object> total : totalList) {
			condition.put(String.valueOf(total.get("resourceid")), total.get("counts"));
		}
	}

	/**
	 * 在线录入面授/网络面授成绩保存(补考)
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/noninput-examresults-save.html")
	public synchronized void inputnonFacestudyExamResultsSave(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String teachingPlanCourseId  = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
		String[] studyNos      	     = request.getParameterValues("resourceid");     //要保存成绩的学号	
//		String parentId = ExStringUtils.trimToEmpty(request.getParameter("parentId"));
//		String[] parentIds      	 = request.getParameterValues("parentId"); 
		String only = ExStringUtils.trimToEmpty(request.getParameter("only"));
		String courseid = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String isMachineExam = ExStringUtils.defaultIfEmpty(request.getParameter("isMachineExam"),Constants.BOOLEAN_NO);
		String schoolId = ExStringUtils.trimToEmpty(request.getParameter("schoolId"));
//		String parentOnly = ExStringUtils.trimToEmpty(request.getParameter("parentOnly"));

		if(ExStringUtils.isNotEmpty(only)){
			if(null==studyNos){
				studyNos=new String[]{only};
			}else{
				List<String> stos = Arrays.asList(studyNos);
				if(!stos.contains(only)){
					studyNos[studyNos.length]=only;
				}
			}
		}

		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "保存成功!";                                           //返回的消息
		String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL		
		User user              	 = SpringSecurityHelper.getCurrentUser();        //当前用户
		Double per 					 = 60D;

		//要换成当前补考批次
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次

//		String hql = " from "+TeachingPlanCourse.class.getSimpleName()+" a where a.isDeleted=0 and a.teachingPlan.resourceid=? and a.course.resourceid=? and a.teachType='facestudy'";
//		TeachingPlanCourse planCourse = null;
//		if(studyNos!=null){
//			StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNos[0]);
//			List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findByHql(hql,
//					studentInfo.getTeachingPlan().getResourceid(),courseid);
//			if(null != planCourseList && planCourseList.size()>0){
//				planCourse = planCourseList.get(0);
//			}
//		}

		ExamSub examSub = examSubService.get(examSubId);

		if(null != examSub){
//			String examSubId = examList.get(0).getResourceid();

			if (ExStringUtils.isNotBlank(examSubId)&&teachingPlanCourseId!=null && studyNos!=null){


//				ExamSub examSub              = examSubService.get(examSubId);    			 //要录入成绩的考试批次
				TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
				Date curTime             = new Date();                                	 	 //当前时间
				Date endTime    		 = examSub.getEndTime();                 	 		//考试批次预约结束时间


//				if (curTime.after(endTime)){	
				Course course = planCourse.getCourse();

				String teachType = planCourse.getTeachType();
				if(ExStringUtils.isBlank(teachType)||"networkstudy".equals(teachType)){
					success = false;
					msg = "课程"+course.getCourseName()+"教学方式不是面授或网络面授!";
				} else {

					Integer examCourseType = teachType.contains("face")?1:0;
					Map<String, Object> _condition = new HashMap<String, Object>();
					if(Constants.BOOLEAN_YES.equals(isMachineExam)){
						examCourseType = 3;
						_condition.put("schoolId",schoolId );
					}
					_condition.put("courseId", course.getResourceid());
					_condition.put("examSubId", examSubId);
					_condition.put("examCourseType", examCourseType);
					_condition.put("isMachineExam", isMachineExam);
					ExamInfo examInfo = examInfoService.getExamInfo(_condition);

					for (int i = 0; i < studyNos.length; i++) {
						if(ExStringUtils.isNotEmpty(only)){
							if(!studyNos[i].equals(only)){
								continue;
							}
						}

						try {
							if(ExStringUtils.isBlank(studyNos[i])){
								continue;
							}

							ExamResults examResults = null;
							String resultsId = ExStringUtils.trimToEmpty(request.getParameter("resultsId"+studyNos[i]));

							if(ExStringUtils.isNotBlank(resultsId)){
								examResults = examResultsService.get(resultsId);//要保存的成绩
								examResults.setCheckStatus("0");
							} else {
								examResults = new ExamResults();
								examResults.setCourse(course);
								examResults.setExamInfo(examInfo);
								examResults.setMajorCourseId(planCourse.getResourceid());

//										examResults.setIsMakeupExam(Constants.BOOLEAN_YES);
								examResults.setIsMakeupExam(examSub.getExamType());
								examResults.setIsMachineExam(isMachineExam);
								examResults.setExamsubId(examSubId);//这里要改

								//examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
								examResults.setCheckStatus("0");

								examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
								examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
								examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
								examResults.setCourseType(planCourse.getCourseType());
								StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNos[i]);
								examResults.setStudentInfo(studentInfo);

								examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
								examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
								examResults.setCourseScoreType(examInfo.getCourseScoreType());
								List examCountList      = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
										examResults.getStudentInfo().getResourceid(),ExStringUtils.trimToEmpty(examResults.getResourceid()));
								long examCount          = (Long)examCountList.get(0);			         //选考次数
								examResults.setExamCount(examCount+1);
							}
							if(examResults.getExamInfo().getResourceid() != examInfo.getResourceid()){//检查考试信息是否正确
								examResults.setExamInfo(examInfo);
							}
							examResults.setPlanCourseTeachType(planCourse.getTeachType());
							int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
							if (checkStatus>0) {
								throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
							}
							String writtenScore     = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+studyNos[i]));
							String examAbnormity    = ExStringUtils.trimToEmpty(request.getParameter("examAbnormity"+studyNos[i]));
//									String usuallyScore     = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore"+studyNos[i]));
							String usuallyScore     = writtenScore;
//									String integratedScore  = ExStringUtils.trimToEmpty(request.getParameter("integratedScore"+studyNos[i]));

							String integratedScore  = "";
							if("0".equals(examAbnormity)){//如果考试不异常,则保存
								integratedScore  = writtenScore;
							}
							if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
								continue;
							}
							if (ExStringUtils.isNotEmpty(examAbnormity)) {
								examResults.setExamAbnormity(examAbnormity);
							}
							if("0".equals(examResults.getExamAbnormity())){
								if(ExStringUtils.isNotBlank(writtenScore)&&(Double.valueOf(writtenScore)<0||Double.valueOf(writtenScore)>100)){
									throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩必须为0-100分");
								}
								BigDecimal w_Score	 = new BigDecimal(writtenScore);
								examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());

//										//考试批次是一补
//										if (examSub.getExamType().equals("Y")) {
//											//考试类型“一补”：检查该学生所属课程的正考成绩是否有缓考状态，如果有，则把一补成绩复制到正考成绩记录里
//											Map<String,Object> condition2 = new HashMap<String, Object>();
//											condition2.put("studentid", studyNos[i]);
//											condition2.put("courseid", courseid);
//											condition2.put("examsubId", examSub.getResourceid());
//											List zkExamResult = examResultsService.getZKExamResult(condition2);
//											if (zkExamResult.size() > 0) {
//												ExamResults examResult = examResultsService.get(((Map)zkExamResult.get(0)).get("resourceid").toString());
//												examResult.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
//												examResultsService.update(examResult);
//											}
//										}

							} else {
								examResults.setWrittenScore("");
							}
							if(ExStringUtils.isNotBlank(usuallyScore)&&(Double.valueOf(usuallyScore)<0||Double.valueOf(usuallyScore)>100)){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的平时成绩必须为0-100分");
							}
//									if(ExStringUtils.isNotBlank(usuallyScore)){
//										BigDecimal u_Score	 = new BigDecimal(usuallyScore);
//										examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
//									}
							if(ExStringUtils.isNotBlank(integratedScore)&&(Double.valueOf(integratedScore)<0||Double.valueOf(integratedScore)>100)){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的综合成绩必须为0-100分");
							}
							if(ExStringUtils.isNotBlank(integratedScore)){
								BigDecimal i_Score	 = new BigDecimal(integratedScore);
								examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}else {
								examResults.setIntegratedScore(integratedScore);
							}
							examResults.setFillinDate(curTime);
//									examResults.setCheckStatus("0");
							examResults.setFillinMan(user.getCnName());
							examResults.setFillinManId(user.getResourceid());

//									examResultsService.saveOrUpdate(examResults);
							if (examResults.getResourceid() == null || "".equals(examResults.getResourceid())) {
								List<ExamResults> llist	   = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" te where te.studentInfo.resourceid= ? and te.isDeleted=0 and te.examsubId=? and  te.course.resourceid=? ",examResults.getStudentInfo().getResourceid(),examResults.getExamsubId(),examResults.getCourse().getResourceid());
								if(llist.size()>0) {
									throw new WebException("学生考试记录已存在，学生学号："+studyNos[i]+",请刷新页面！");
								}
								examResultsService.save(examResults);
							} else {
								examResultsService.update(examResults);
							}

							List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),planCourse.getResourceid());
							StudentLearnPlan learnPlan           = null;
							if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
								learnPlan = stuLearnPlan.get(0);
								learnPlan.setExamResults(examResults);
								learnPlan.setExamInfo(examResults.getExamInfo());
								learnPlan.setStatus(2);
							}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
								learnPlan = new StudentLearnPlan();
								learnPlan.setExamInfo(examResults.getExamInfo());
								learnPlan.setTeachingPlanCourse(planCourse);
								learnPlan.setExamResults(examResults);
								learnPlan.setStatus(2);
								learnPlan.setStudentInfo(examResults.getStudentInfo());
								learnPlan.setTerm(examSub.getTerm());
								learnPlan.setYearInfo(examSub.getYearInfo());
								learnPlan.setOrderExamYear(examSub.getYearInfo());
								learnPlan.setOrderExamTerm(examSub.getTerm());
							}
							studentLearnPlanService.saveOrUpdate(learnPlan);


						} catch (Exception e) {
							logger.error("保存学生成绩异常，学生学号：{}"+studyNos[i],e.fillInStackTrace());
							success = false;
							msg     = "录入学生成绩异常:"+e.getMessage();
							break;
						}
					}
//						}else {
//							success = false;
//							msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
//					    			+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);		
//						}
				}
			} else {
				success = false;
				msg = "没有补考考试批次!";
			}

		}else {
			success = false;
			msg     = "请选择一个考试批次！";
		}

		condition.put("success",success);
		if (ExStringUtils.isNotEmpty(msg)) {
			condition.put("msg", msg);
		}
		if (ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("plansourceid", teachingPlanCourseId);
		}
		if (ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId", teachingPlanCourseId);
		}

		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 提交面授/网络面授成绩(补考)
	 * 卷面成绩即综合成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/non-input-examresults-submit.html")
	public void submitNonFacestudyExamResults(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
			//String guidPlanId            = request.getParameter("guidePlanId");
			String gradeId 				 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
			String plansourceId		     = ExStringUtils.trimToEmpty(request.getParameter("plansourceId"));
			//String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String branchSchool 				 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String major            	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classesId             = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			String studentName             = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
			String studyNo             = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
			String checkStatus             = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
			//if(ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId)&&ExStringUtils.isNotBlank(guidPlanId)){			
			boolean success  		  	 = true;                                         //操作结果
			String  msg               	 = "";                                           //返回的消息

//			String examSubId = "";
//			Map<String, Object> paramMap = new HashMap<String, Object>();
//			String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//			if(!"年".equals(batchName.substring(4, 5))){
//				StringBuffer sb = new StringBuffer(batchName);
//				sb.insert(4, "年");
//				batchName = sb.toString();
//			}
//			logger.info("考试批次:{}", batchName);
//			paramMap.put("batchName", batchName);
//			List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次

			ExamSub examSub = examSubService.get(examSubId);
			if(null != examSub){
//				examSubId = examList.get(0).getResourceid();

				if(ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(plansourceId)){
					User curUser = SpringSecurityHelper.getCurrentUser();
					if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
							|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
//						map.put("branchSchool", curUser.getOrgUnit().getResourceid());
						map.put("branchSchool", branchSchool);
						TeachingPlanCourse planCourse = teachingPlanCourseService.get(plansourceId);
//						TeachingPlanCourse planCourse = null;
//						String hql = " from "+TeachingPlanCourse.class.getSimpleName()+" where teachingPlan.resourceid=? and course.resourceid=?";
//						List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findByHql(hql, plansourceId, courseId);
//						if (planCourseList.size() > 0) {
//							planCourse = planCourseList.get(0);
//						}
						map.put("courseId", planCourse.getCourse().getResourceid());
						map.put("gradeId", gradeId);
						map.put("examSubId", examSubId);
						map.put("teachingPlanId", planCourse.getTeachingPlan().getResourceid());
						String hql = " from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=0 and results.course.resourceid=:courseId and results.examsubId=:examSubId and results.studentInfo.grade.resourceid=:gradeId and results.studentInfo.branchSchool.resourceid=:branchSchool and results.studentInfo.teachingPlan.resourceid=:teachingPlanId ";
						if(ExStringUtils.isNotEmpty(classesId)){
							map.put("classesId", classesId);
							hql += " and results.studentInfo.classes.resourceid = :classesId ";
						}
						List<ExamResults> list = examResultsService.findByHql(hql, map);

//						String branchschoolid = curUser.getOrgUnit().getResourceid();
						String branchschoolid = branchSchool;
						Map<String,Object> condition = new HashMap<String, Object>();
//						condition.put("examSubId",examSubId);
//						condition.put("gradeId",gradeId);
//						condition.put("teachplanid",planCourse.getTeachingPlan().getResourceid());
//						condition.put("classesId",classesId);
//						condition.put("branchschoolid",branchschoolid);
//						condition.put("teachType", ExStringUtils.trimToEmpty(planCourse.getTeachType()));
//						condition.put("courseid", planCourse.getCourse().getResourceid());
						if(ExStringUtils.isNotEmpty(gradeId)) {
							condition.put("gradeId",gradeId);
						}
						if(ExStringUtils.isNotEmpty(major)) {
							condition.put("majorid",major);
						}
						if(ExStringUtils.isNotEmpty(branchschoolid)) {
							condition.put("branchschoolid",branchschoolid);
						}
						condition.put("courseid",planCourse.getCourse().getResourceid());
						if(ExStringUtils.isNotEmpty(classesId)) {
							condition.put("classesId",classesId);
						}
						if(ExStringUtils.isNotEmpty(checkStatus)) {
							condition.put("checkStatus",checkStatus);
						}
						if(ExStringUtils.isNotEmpty(studentName)) {
							condition.put("studentName",studentName);
						}
						if(ExStringUtils.isNotEmpty(studyNo)) {
							condition.put("studyNo",studyNo);
						}
						if(ExStringUtils.isNotEmpty(examSubId)) {
							condition.put("examSubId",examSubId);
						}
						condition.put("teachingPlanCourseId",plansourceId);
						Page objPage = new Page();
//						objPage.setOrderBy("unit.unitcode,major.majorcode,stu.studyno ");
						objPage.setOrder(Page.ASC);
						objPage = teachingJDBCService.findNonFaceStudyExamResultsVo(objPage, condition);
						List<ExamResultsVo> scoreList = objPage.getResult();
						int uninput = scoreList.size() - list.size();

						//是否全部录入成绩才允许提交
						String isallinput = CacheAppManager.getSysConfigurationByCode("examresult.isallinput").getParamValue();
						if ("1".equals(isallinput) && uninput > 0) {
							map.put("statusCode", 300);
							map.put("message", "全部录入成绩才允许提交！");
						} else {
							//非统考提交时计算综合成绩
							List<ExamResults> upadateIntegrateScore = new ArrayList<ExamResults>(0);
							for (ExamResults examResults : list) {
								//							ExamInfo info = examResults.getExamInfo();
								String usuallyScore = examResults.getUsuallyScore();
								String writtenScore = examResults.getWrittenScore();

								//							String intergrateScore = caculateIntegrateScoreForFaceStudy(info, usuallyScore, writtenScore, examResults);
								String intergrateScore = writtenScore;
								if(intergrateScore!=null) {
									examResults.setIntegratedScore(ExStringUtils.defaultIfEmpty(intergrateScore, ""));
								}
								upadateIntegrateScore.add(examResults);

								//判断是否需要需要补考
								//20140617 温朝上提出，综合成绩小于60分都进去补考名单
								//20140624 陈浩珍提出，没有成绩与综合成绩小于60的都进入补考名单
								//							if(ExStringUtils.isEmpty(intergrateScore)||
								//									(ExStringUtils.isNotEmpty(intergrateScore)&&Double.parseDouble(intergrateScore)<60.0)
								//									|| examResults.getExamAbnormity().equals("2")){
								//								String hqltp = " from "+TeachingPlanCourse.class.getSimpleName()+" a where a.isDeleted=0 and a.teachingPlan.resourceid=? and a.course.resourceid=? and a.teachType='facestudy'";
								//								List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findByHql(hqltp,
								//										examResults.getStudentInfo().getTeachingPlan().getResourceid(),examResults.getCourse().getResourceid());
								//								if(null!=planCourseList && planCourseList.size()>0){//只处理非统考
								//
								//									Long year = 0l;
								//									String term = "";
								//									YearInfo yearInfo = null;
								//									ExamSub nextExamSub = null;
								//									String hqlgui = "from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and teachingPlan.resourceid=?";
								//									List<TeachingGuidePlan> guiList = teachingGuidePlanService.findByHql(hqlgui, planCourseList.get(0).getTeachingPlan().getResourceid());
								//									if (guiList != null && guiList.size() > 0) {
								//										yearInfo = guiList.get(0).getGrade().getYearInfo();//所属年级
								//										year = guiList.get(0).getGrade().getYearInfo().getFirstYear();//所属年级
								//										//获取课程上课学期
								//										String hqlstatus = "from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingPlanCourse.resourceid=?"
								//												+" and teachingGuidePlan.resourceid=? and schoolIds=?";
								//										List<TeachingPlanCourseStatus> staList = teachingPlanCourseStatusService.findByHql(hqlstatus, planCourseList.get(0).getResourceid()
								//												, guiList.get(0).getResourceid(), examResults.getStudentInfo().getBranchSchool().getResourceid());
								//										if (staList != null && staList.size() > 0) {
								//											term = staList.get(0).getTerm();//课程上课学期
								//										}
								//										String[] terms = term.split("_");
								//										Long index = 1l;//第几个上课学期
								//										Long termYear = Long.parseLong(terms[0]);
								//										for (Long i = year; i <= termYear; i++) {
								//											for (int j = 1; j <= 2; j++) {
								//												if (term.equals(i + "_0" + j)) {
								//													break;
								//												}
								//												index++;
								//											}
								//										}
								//										Long nextYear = 0l;
								//										String nextTerm = "";
								//										String hqlexamsub = "";
								//										if (examSub.getExamType().equals("Y")) {//提交一补成绩
								//											if (index>=1 && index<=4) {
								//												nextYear = year +2;
								//												nextTerm = "1";
								//											} else {
								//												nextYear = year +2;
								//												nextTerm = "2";
								//											}
								//											hqlexamsub = "from "+ExamSub.class.getSimpleName()+" where isDeleted=0 and examType='T'"
								//												+" and yearInfo.firstYear=? and term=?";
								//										} else if (examSub.getExamType().equals("T") || examSub.getExamType().equals("Q")) {//提交二补、结补成绩
								//											nextYear = year +2;
								//											nextTerm = "2";
								//											hqlexamsub = "from "+ExamSub.class.getSimpleName()+" where isDeleted=0 and examType='Q'"
								//												+" and yearInfo.firstYear=? and term=?";
								//										}
								//										List<ExamSub> examSubList = examSubService.findByHql(hqlexamsub, nextYear, nextTerm);
								//										if (examSubList != null && examSubList.size() > 0) {
								//											nextExamSub = examSubList.get(0);
								//										}
								//									}
								//
								//									List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
								//									StudentMakeupList makeup = null;
								//									//如果学生这门课没通过，查询补考表，有则替换成绩，没有则添加
								//									String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? ";
								//									makeupList = studentMakeupListService.findByHql(hqlMakeup,examResults.getStudentInfo().getResourceid(),examResults.getCourse().getResourceid());
								//								    if(null==makeupList || makeupList.size()==0){
								//								    	makeup = new StudentMakeupList();
								//								    	makeup.setStudentInfo(examResults.getStudentInfo());
								//								    	makeup.setCourse(examResults.getCourse());
								//								    	makeup.setExamResults(examResults);
								//								    	makeup.setTeachingPlanCourse(planCourseList.get(0));
								//								    	if (nextExamSub != null) {
								//											makeup.setNextExamSubId(nextExamSub.getResourceid());
								//										}
								//								    	studentMakeupListService.saveOrUpdate(makeup);
								//								    } else if(makeupList.size()>0){
								//								    	makeup = makeupList.get(0);
								//								    	makeup.setExamResults(examResults);
								//								    	if (nextExamSub != null) {
								//											makeup.setNextExamSubId(nextExamSub.getResourceid());
								//										}
								//								    	studentMakeupListService.saveOrUpdate(makeup);
								//								    }
								//								}
								//							}
							}
							examResultsService.batchSaveOrUpdate(upadateIntegrateScore);
							examResultsService.submitExamResults(list, "Y",null);

							map.put("statusCode", 200);
							map.put("message", "提交成功！");
						}
					} else {
						map.put("statusCode", 300);
						map.put("message", "你没有提交成绩的权限");
					}
					if(ExStringUtils.isNotBlank(plansourceId)) {
						map.put("plansourceId", plansourceId);
					}
				}
			} else {
				success = false;
				msg = "没有补考考试批次!";
				map.put("statusCode", 300);
				map.put("message", "提交失败<br/>"+msg);
			}

		} catch (Exception e) {
			logger.error("提交成绩失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "提交失败<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 面授/网络面授成绩撤销(补考)
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/non-examresults-del.html")
	public void nonfacestudyExamResultsDel(String resourceid, HttpServletRequest request, HttpServletResponse response) throws WebException {

		Map<String,Object> condition = new HashMap<String, Object>();
		String msg                   = "";
		boolean isSuccess            = false;

		if (ExStringUtils.isNotEmpty(resourceid)) {
			ExamResults rs 			 = examResultsService.get(resourceid);
			if ("0".equals(ExStringUtils.trimToEmpty(rs.getCheckStatus()))) {
				StudentLearnPlan plan= studentLearnPlanService.findUniqueByProperty("examResults", rs);
				if (null!=plan) {
					studentLearnPlanService.delete(plan);
				}
				examResultsService.delete(rs);
				isSuccess            = true;
				msg                  = "撤销成功！";
			}else {
				msg 				 = "只允许撤销成绩状态为保存的成绩记录！";
			}
		}else{
			msg 					 = "请选择一个要撤销的成绩！";
		}
		condition.put("isSuccess", isSuccess);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 在线录入面授/网络面授成绩保存(补考)--录入界面的功能按钮
	 * @param request
	 * @param response
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/noninput-fun-examresults-save.html")
	public void inputnonFacestudyFunExamResultsSave(HttpServletRequest request, HttpServletResponse response) throws ParseException{
		Map<String,Object> condition = new HashMap<String, Object>();
//		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String teachingPlanCourseId  = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String[] studyNos      	     = request.getParameterValues("resourceid");     //要保存成绩的学号	
//		String parentId = ExStringUtils.trimToEmpty(request.getParameter("parentId"));
//		String[] parentIds      	 = request.getParameterValues("parentId"); 
		String only = ExStringUtils.trimToEmpty(request.getParameter("only"));

		if(ExStringUtils.isNotEmpty(only)){
			if(null==studyNos){
				studyNos=new String[]{only};
			}else{
				List<String> stos = Arrays.asList(studyNos);
				if(!stos.contains(only)){
					studyNos[studyNos.length]=only;
				}
			}
		}


//		if(ExStringUtils.isNotEmpty(parentOnly)){
//			if(null==parentIds){
//				parentIds=new String[]{parentOnly};
//			}else{
//				List<String> stos = Arrays.asList(parentIds);
//				if(!stos.contains(parentOnly)){
//					parentIds[parentIds.length]=parentOnly;
//				}
//			}
//		}




		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		String  examCountHQL      	 = genHQL(condition,"examCount");                //考试次数HQL		
		User user              	 = SpringSecurityHelper.getCurrentUser();        //当前用户
		Double per 					 = 60D;

		//要换成当前补考批次
//		String examSubId = "F31E765C04824FCBE040A8C0B8017019";
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
		paramMap.put("batchName", batchName);
		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次

		if(null != examList && examList.size()>0){
			String examSubId = examList.get(0).getResourceid();

			if (ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId) && studyNos!=null){
				ExamSub examSub              = examSubService.get(examSubId);    			 //要录入成绩的考试批次
				TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
				Date curTime             = new Date();                                	 	 //当前时间
				//Date endTime    		 = examSub.getEndTime();                 	 		//考试批次预约结束时间


//				if (curTime.after(endTime)){	
				Course course = planCourse.getCourse();

				String teachType = planCourse.getTeachType();
				if(ExStringUtils.isBlank(teachType)||"networkstudy".equals(teachType)){
					throw new WebException("课程"+course.getCourseName()+"教学方式不是面授或网络面授!");
				}
				Integer examCourseType = teachType.contains("face")?1:0;

				ExamInfo examInfo 	   = null;
				List<ExamInfo> list	   = examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
				if (null!=list && !list.isEmpty()) {
					examInfo = list.get(0);
				} else {
					examInfo = new ExamInfo();
					examInfo.setCourse(course);
					examInfo.setIsOutplanCourse("0");
					examInfo.setExamSub(examSub);
					examInfo.setExamCourseType(examCourseType);

					if (1==examCourseType) {
						if (null!=examSub.getFacestudyScorePer()) {
							per=examSub.getFacestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					}else if(2!=examCourseType) {
						if (null==examSub.getNetsidestudyScorePer()) {
							per=examSub.getNetsidestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					}
					if(planCourse.getScoreStyle()!=null){
						examInfo.setCourseScoreType(planCourse.getScoreStyle());
					}else {
						examInfo.setCourseScoreType(examSub.getCourseScoreType());
					}
					//	examInfo.setCourseScoreType(examSub.getCourseScoreType());

					examInfoService.save(examInfo);
				}
				for (int i = 0; i < studyNos.length; i++) {
					if(ExStringUtils.isNotEmpty(only)){
						if(!studyNos[i].equals(only)){
							continue;
						}
					}

//						if(ExStringUtils.isNotEmpty(parentOnly)){
//							if(!parentIds[i].equals(parentOnly)){
//								continue;
//							}
//						}

					try {
						if(ExStringUtils.isBlank(studyNos[i])){
							continue;
						}

//							if(ExStringUtils.isBlank(parentIds[i])){
//								continue;
//							}

						ExamResults examResults = null;
						String resultsId = ExStringUtils.trimToEmpty(request.getParameter("resultsId"+studyNos[i]));
						String parentsId = ExStringUtils.trimToEmpty(request.getParameter("parentsId"+studyNos[i]));

						if(ExStringUtils.isNotBlank(resultsId)){
							examResults = examResultsService.get(resultsId);//要保存的成绩
						} else {
							examResults = new ExamResults();
							examResults.setCourse(course);
							examResults.setExamInfo(examInfo);
							examResults.setMajorCourseId(teachingPlanCourseId);

//								ExamResults er = examResultsService.get(parentOnly);
//								er.setResourceid(parentId);
//								examResults.setParent(er);
//								examResults.setParentId(parentOnly); 

							examResults.setParentId(parentsId);

							examResults.setIsMakeupExam(Constants.BOOLEAN_YES);
							examResults.setExamsubId(examSubId);//这里要改

							//examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
							examResults.setCheckStatus("0");

							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
							examResults.setCourseType(planCourse.getCourseType());
							StudentInfo studentInfo = studentinfoservice.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNos[i]);
							examResults.setStudentInfo(studentInfo);

							examResults.setCreditHour(planCourse.getCreditHour()!=null?planCourse.getCreditHour():null);
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
							examResults.setCourseScoreType(examInfo.getCourseScoreType());
							List examCountList      = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
									examResults.getStudentInfo().getResourceid(),ExStringUtils.trimToEmpty(examResults.getResourceid()));
							long examCount          = (Long)examCountList.get(0);			         //选考次数
							examResults.setExamCount(examCount+1);
						}
						examResults.setPlanCourseTeachType(planCourse.getTeachType());
						int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
						if (checkStatus>0) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
						}
						String writtenScore     = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+studyNos[i]));
						String examAbnormity    = ExStringUtils.trimToEmpty(request.getParameter("examAbnormity"+studyNos[i]));
//							String usuallyScore     = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore"+studyNos[i]));
						String usuallyScore     = writtenScore;
						String integratedScore  = ExStringUtils.trimToEmpty(request.getParameter("integratedScore"+studyNos[i]));
						if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
							continue;
						}
						if (ExStringUtils.isNotEmpty(examAbnormity)) {
							examResults.setExamAbnormity(examAbnormity);
						}
						if("0".equals(examResults.getExamAbnormity())){
							if(ExStringUtils.isNotBlank(writtenScore)&&(Double.valueOf(writtenScore)<0||Double.valueOf(writtenScore)>100)){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩必须为0-100分");
							}
							BigDecimal w_Score	 = new BigDecimal(writtenScore);
							examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						} else {
							examResults.setWrittenScore("");
						}
						if(ExStringUtils.isNotBlank(usuallyScore)&&(Double.valueOf(usuallyScore)<0||Double.valueOf(usuallyScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的平时成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(usuallyScore)){
							BigDecimal u_Score	 = new BigDecimal(usuallyScore);
							examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						if(ExStringUtils.isNotBlank(integratedScore)&&(Double.valueOf(integratedScore)<0||Double.valueOf(integratedScore)>100)){
							throw new WebException(examResults.getStudentInfo().getStudentName()+"的综合成绩必须为0-100分");
						}
						if(ExStringUtils.isNotBlank(integratedScore)){
							BigDecimal i_Score	 = new BigDecimal(integratedScore);
							examResults.setIntegratedScore((i_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
						}
						examResults.setFillinDate(curTime);
						examResults.setCheckStatus("0");
						examResults.setFillinMan(user.getCnName());
						examResults.setFillinManId(user.getResourceid());

						examResultsService.saveOrUpdate(examResults);

						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),teachingPlanCourseId);
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}
						studentLearnPlanService.saveOrUpdate(learnPlan);


					} catch (Exception e) {
						logger.error("保存学生成绩异常，学生学号：{}"+studyNos[i],e.fillInStackTrace());
						success = false;
						msg     = "录入学生成绩异常:"+e.getMessage();
						break;
					}
				}
//				}else {
//					success = false;
//					msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
//			    			+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);		
//				}
			} else {
				success = false;
				msg = "没有补考考试批次!";
			}

		}else {
			success = false;
			msg     = "请选择一个考试批次！";
		}

		condition.put("success",success);
		if (ExStringUtils.isNotEmpty(msg)) {
			condition.put("msg", msg);
		}

		renderJson(response,JsonUtils.mapToJson(condition));
	}
	/**
	 * 补考卷面成绩管理-列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/makeup_list.html")
	public String examResultManagerMakeupList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page page){
		page.setPageSize(50);

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId  			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId_makeup"));
		String courseName 			 = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId_makeup"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool_makeup"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid_makeup"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic_makeup"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major_makeup"));
		String classId				 = ExStringUtils.trimToEmpty(request.getParameter("classId_makeup"));
		String checkStatus				 = ExStringUtils.trimToEmpty(request.getParameter("checkStatus_makeup"));
		String courseTeachType				 = ExStringUtils.trimToEmpty(request.getParameter("courseTeachType"));

		//String forwordurl 			 = "";
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		/*if (ExStringUtils.isNotEmpty(branchSchool)){
			OrgUnit unit = orgUnitService.get(branchSchool);
			model.addAttribute("unit", unit);
		}*/
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major_makeup", major);
		}
		if (ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId_makeup", classId);
		}
		if (ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId_makeup", courseId);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic_makeup", classic);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid_makeup", gradeid);
		}
		if (ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus_makeup", checkStatus);
		}
		// 教学形式
		if (ExStringUtils.isNotEmpty(courseTeachType)) {
			condition.put("courseTeachType", courseTeachType);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool_makeup", branchSchool);

			if (ExStringUtils.isNotEmpty(examSubId)) {

				ExamSub examSub          = examSubService.get(examSubId);
				model.addAttribute("examSub", examSub);
				condition.put("examSubId_makeup", examSubId);
			}

//			page = examResultsService.queryExamResultsInfoForFaceTeachType(condition, page);
			page = examResultsService.queryExamResultsInfoForFaceTeachTypeMakeup2(condition, page);
//			Page page2 = examResultsService.queryExamResultsInfoForFaceTeachTypeMakeup2(condition, page);

			//Map<String,Object>  statusMap = queryMakeupExamInfoCheckStatus(page,condition);
			//model.addAttribute("statusMap",statusMap);
		}
		model.addAttribute("schoolCode", schoolCode);
		model.addAttribute("page", page);
		//forwordurl               = "/edu3/teaching/examResult/examResultManager_makeup_list";

		model.addAttribute("condition", condition);
		return "/edu3/teaching/examResult/examResultManager_makeup_list";
	}

	/**
	 * 获取考试科目的补考成绩状况
	 * @param page
	 * @return
	 */
	private Map<String,Object> queryMakeupExamInfoCheckStatus(Page page, Map<String,Object> condition){

		Map<String,Object>  resultsMap = new HashMap<String, Object>();
		//StringBuffer ids 			   = new StringBuffer();
		try {
			if (condition.containsKey("branchSchool_makeup")) {
				resultsMap.put("branchSchool", condition.get("branchSchool_makeup"));
			}


			String branchSchool = condition.get("branchSchool_makeup")!=null?(String) condition.get("branchSchool_makeup"):"";
			if(condition.containsKey("examSubId_makeup")){//不为必填
				String examSubId    = (String) condition.get("examSubId_makeup");
				resultsMap.put("examSubId", examSubId);
			}
//				resultsMap.put("branchSchool", branchSchool);			
//				resultsMap.put("teachType", condition.get("teachType"));			

			for (int i = 0; i < page.getResult().size(); i++) {

				FaceMakeupExamResultsVo vo = (FaceMakeupExamResultsVo) page.getResult().get(i);
				String g_id  = vo.getGradeId();
				String cl_id = vo.getClassId();
				String m_id  = vo.getMajorId();
				String c_id  = vo.getCourseId();
				String s_id  = vo.getExamSubId();
				String ei_id  = vo.getResourceid();

				resultsMap.put("gradeId", g_id);
				resultsMap.put("classId", cl_id);
				resultsMap.put("major", m_id);
				resultsMap.put("courseId", c_id);
				resultsMap.put("examSubId", s_id);
				resultsMap.put("examInfoId", ei_id);

				String sql          = genHQLMakeup(resultsMap);
				List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, resultsMap);

				for (Map<String,Object> m :list) {

					String CHECKSTATUS = (String) m.get("CHECKSTATUS");
					BigDecimal COUNTS  = (BigDecimal)m.get("COUNTS");
					String color       = "red";
					switch (Integer.valueOf(CHECKSTATUS)) {
						case 0:
							color       = "blue";
							break;
						case 1:
							color       = "green";
							break;
						case 2:
							color       = "red";
							break;
						case 3:
							color       = "green";
							break;
						case 4:
							color       = "green";
							break;
						default:
							color       = "red";
							break;
					}
//						if (resultsMap.containsKey(branchSchool+g_id+cl_id+m_id+c_id+s_id)) {
					if (resultsMap.containsKey(cl_id+ei_id)) {
//							String str     = (String) resultsMap.get(branchSchool+g_id+cl_id+m_id+c_id+s_id);
						String str     = (String) resultsMap.get(cl_id+ei_id);
						str           += "<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp";
//							resultsMap.put(branchSchool+g_id+cl_id+m_id+c_id+s_id,str);
						resultsMap.put(cl_id+ei_id,str);
					}else{
						resultsMap.put(cl_id+ei_id,"<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp");
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultsMap;
	}

	private String genHQLMakeup(Map<String,Object> condition){
		StringBuffer hql = new StringBuffer();
		hql.append(" select rs.checkstatus ,count(rs.resourceid) counts ");
		hql.append("   from edu_teach_examresults rs   ");
		hql.append("   inner join edu_roll_studentinfo  stu on rs.studentid = stu.resourceid   ");

		if(condition.containsKey("branchSchool")) {
			hql.append(" and stu.branchschoolid =:branchSchool ");
		}
		if(condition.containsKey("gradeId")) {
			hql.append(" and stu.gradeid=:gradeId");
		}
		if(condition.containsKey("major")) {
			hql.append(" and stu.majorid=:major");
		}
		if(condition.containsKey("classic")) {
			hql.append(" and stu.classicid=:classic");
		}
		//2014-5-22
		if(condition.containsKey("classId")) {
			hql.append(" and stu.classesid=:classId");
		}
		hql.append("   join EDU_TEACH_EXAMSUB sub on sub.resourceid = rs.EXAMSUBID and sub.batchtype='exam' and sub.examtype!='N' and sub.isdeleted=0 ");
		hql.append(" where rs.isdeleted = 0 and rs.checkstatus!='-1' ");
		if(condition.containsKey("examSubId")) {
			hql.append(" and rs.examsubid=:examSubId");
		}
		if(condition.containsKey("courseId")) {
			hql.append(" and rs.courseid =:courseId");
		}
		if(condition.containsKey("majorCourseId")) {
			hql.append(" and rs.majorcourseid =:majorCourseId");
		}
		if(condition.containsKey("examInfoId")) {
			hql.append(" and rs.examinfoid =:examInfoId");
		}
		if(condition.containsKey("teachType")) {
			hql.append(" and rs.PLANCOURSETEACHTYPE =:teachType");
		}

		hql.append(" group by rs.checkstatus ");

		return hql.toString();
	}

	/**
	 * 补考成绩审核-审核
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/examresults-audit-pass-makeup.html")
	public void auditMakeupPassExamResults(HttpServletRequest request, HttpServletResponse response){

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String operatingType         = ExStringUtils.trimToEmpty(request.getParameter("operatingType"));
//		String teachType             = ExStringUtils.defaultIfEmpty(request.getParameter("teachType"),"networkstudy");
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeId			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classId				 = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		String examInfoId			 = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));

		String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		ExamSub sub               	 = examSubService.get(examSubId);    			 //考试批次
		ExamInfo info                = examInfoService.get(examInfoId);

		//String teachType = "facestudy";

		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId", gradeId);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classId)) {
			condition.put("classesid", classId);
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}

		//int examCourseType   	 = teachType.contains("face")?1:0;
//		List<ExamInfo> infos 	 = examInfoService.findByCriteria(Restrictions.eq("examSub.resourceid", examSubId),Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
//		if (null!=infos && !infos.isEmpty()) {
//			 info            	 = infos.get(0);
//		}
		if (null!=sub && "3".equals(sub.getExamsubStatus())) {
			try {
				//全部通过
				if ("all".equals(operatingType)) {
					if(condition.containsKey("gradeId")
							&&condition.containsKey("major")&&condition.containsKey("classesid")
							&&condition.containsKey("courseId")) {

//						condition.put("examInfoId", info.getResourceid());
//						examResultsService.auditExamResultsAllPassForFactCourse(condition,info,teachType);
						examResultsService.auditExamResultsAllPassForFactMakeupCourse(condition,info,null, examSubId);
						msg              = "成绩审核通过操作成功！";
					}

					//个别审核
				}else {
					examResultsService.makeupAuditExamResultsSingle(resultsIds, request, info, null, examSubId);
					msg              = "审核操作成功！";
				}

			} catch (Exception e) {
				logger.error("审核成绩通过出错：{}"+sub.getResourceid(),e.fillInStackTrace());
				success 			 = false;
				msg     			 = "成绩审核出错："+e.getMessage();
			}
		}else {
			success = false;
			msg     = "成绩为空或者找不到考试批次或者考试批次未关闭,请与管理员联系！";
		}

		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 批量补考成绩审核
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/examresults-batchAudit-pass-makeup.html")
	public void batchAuditMakeupPassExamResults(HttpServletRequest request, HttpServletResponse response, Page page){
		page.setOrderBy("examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo asc ");
		Map<String,Object> condition = new HashMap<String, Object>();

		String examSubId      	  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String type        			 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String teachType             = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeId			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String classic  			 	 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String classId				 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String checkStatus              = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));

		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major_makeup", major);
		}
		if (ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId_makeup", classId);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId_makeup", courseId);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic_makeup", classic);
		}
		if (ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeid_makeup", gradeId);
		}
		if (ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus_makeup", checkStatus);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool_makeup", branchSchool);
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			ExamSub examSub          = examSubService.get(examSubId);
			condition.put("examSubId_makeup", examSubId);
		}

		String resIds             = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		String examSubIds            = ExStringUtils.trimToEmpty(request.getParameter("examSubIds"));
		String courseIds             = ExStringUtils.trimToEmpty(request.getParameter("courseIds"));
		String gradeids			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeIds"));
		String majors  			 	 = ExStringUtils.trimToEmpty(request.getParameter("majorIds"));
		String classesids			 = ExStringUtils.trimToEmpty(request.getParameter("classesIds"));

		//String [] resultsIds      	 = request.getParameterValues("resourceid");     //要保存成绩的ID
		boolean success  		  	 = true;                                         //操作结果
		String  msg               	 = "";                                           //返回的消息
		ExamSub sub               	 = null;    			 //考试批次

		List<String> residList = new ArrayList<String>();
		List<String> examSubList = new ArrayList<String>();
		List<String> courseList = new ArrayList<String>();
		List<String> gradeList = new ArrayList<String>();
		List<String> majorList = new ArrayList<String>();
		//List<String> classicList = new ArrayList<String>();
		List<String> classesList = new ArrayList<String>();

		if("checked".equals(type)){//勾选审核
			residList = Arrays.asList(resIds.split(","));//考试信息id
			examSubList = Arrays.asList(examSubIds.split(","));
			courseList = Arrays.asList(courseIds.split(","));
			gradeList = Arrays.asList(gradeids.split(","));
			majorList = Arrays.asList(majors.split(","));
			classesList = Arrays.asList(classesids.split(","));
		}else {//查询条件审核
			page.setPageSize(Integer.MAX_VALUE);
			page = examResultsService.queryExamResultsInfoForFaceTeachTypeMakeup2(condition, page);
			List<FaceMakeupExamResultsVo> results = page.getResult();
			FaceMakeupExamResultsVo faceMakeupExamResultsVo;
			for (int i = 0; i < results.size(); i++) {
				faceMakeupExamResultsVo = results.get(i);
				courseList.add(faceMakeupExamResultsVo.getCourseId());
				gradeList.add(faceMakeupExamResultsVo.getGradeId());
				majorList.add(faceMakeupExamResultsVo.getMajorId());
				classesList.add(faceMakeupExamResultsVo.getClassId());
				examSubList.add(faceMakeupExamResultsVo.getExamSubId());
				residList.add(faceMakeupExamResultsVo.getResourceid());
			}
		}
		Map<String, Object> map;
		ExamInfo examInfo = null;
		for (int i = 0; i < courseList.size() && success!=false; i++) {
			sub               	 = examSubService.get(examSubList.get(i));
			if (null!=sub && "3".equals(sub.getExamsubStatus())) {
				try {
					//面授课程或面授+网络课程的全部通过	
					map = new HashMap<String, Object>();
					map.put("branchSchool", branchSchool);
					map.put("examSub", examSubList.get(i));
					map.put("gradeId", gradeList.get(i));
					map.put("major", majorList.get(i));
					map.put("courseId", courseList.get(i));
					map.put("classesid", classesList.get(i));
					map.put("examInfoId", residList.get(i));

					//查询开课记录,用于修复考试信息
					/*Map<String, Object> _condition = new HashMap<String, Object>();
					_condition.put("brSchoolid",branchSchool);
					_condition.put("isOpenCourse", "Y");
					_condition.put("gradeid",gradeList.get(i));
					_condition.put("majorid",majorList.get(i));
					_condition.put("courseId",courseList.get(i));
					List<Map<String, Object>> courseStatusList = teachingPlanCourseStatusService.findTeachingPlanCourseStatusMapByConditionNew(_condition);
					if(courseStatusList!=null && courseStatusList.size()>0){
						examInfo = examInfoService.findExamInfoByCourseAndExamSubAndCourseType(courseList.get(i),examSubList.get(i), courseStatusList.get(0).get("teachtype").toString());
					}*/
					examInfo = examInfoService.findExamInfoByCourseAndExamSubAndExamCourseType(courseList.get(i),examSubList.get(i),2);
					examResultsService.auditExamResultsAllPassForFactMakeupCourse(map,examInfo,teachType, examSubList.get(i));

				} catch (Exception e) {
					logger.error("审核成绩通过出错：{}"+sub.getResourceid(),e.fillInStackTrace());
					success 			 = false;
					msg     			 = "成绩审核出错："+e.getMessage();
				}
			}else {
				success = false;
				msg     = "第"+(i+1)+"条数据："+"成绩为空或者找不到考试批次或者考试批次未关闭,请与管理员联系！";
			}
		}
		if(success){
			msg              = "共"+courseList.size()+"条记录审核通过操作成功！";
		}
		condition.put("success", success);
		condition.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(condition));
	}

	/**
	 * 补考成绩审核-列表
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/audit-examresults-makeup-list.html")
	public String auditMakeupExamResultsList(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage){

		objPage.setOrderBy("examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo asc ");

		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId 			 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeId			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classId				 = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
//		String examInfoId 			 = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String checkStatus           = ExStringUtils.trimToEmpty(request.getParameter("checkStatus"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String orderBy               = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String examInfoId               = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));

		if(ExStringUtils.isNotEmpty(orderBy)) {
			condition.put("orderBy", orderBy);
		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSub", examSubId);//查询时的参数
		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);//面试请求的参数
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId", gradeId);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId", classId);
		}
//		if(ExStringUtils.isNotEmpty(examInfoId)) 	   condition.put("examInfoId", examInfoId);
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(checkStatus)) {
			condition.put("checkStatus", checkStatus);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}

		String teachType = "facestudy";
		condition.put("teachType", teachType);

		if (ExStringUtils.isNotEmpty(courseId)&&ExStringUtils.isNotEmpty(examSubId)) {
//			ExamInfo info 		 = null;
//			int examCourseType   = teachType.contains("face")?1:0;
//			List<ExamInfo> infos = examInfoService.findByCriteria(Restrictions.eq("examSub.resourceid", examSubId),Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
//			if (null!=infos && !infos.isEmpty()) {
//				 info            = infos.get(0);
//			}
//			if (null!=info) {
			objPage 		 = examResultsService.findExamResultByCondition(condition,objPage);
//				StringBuilder stuIds   				    = new StringBuilder();
//				if (ExStringUtils.isNotBlank(stuIds.toString())) {
//					Map<String,BigDecimal> usualResults = usualResultsService.calculateNetsidestudyUsualResults(info.getCourse().getResourceid(),stuIds.substring(1));
//					for (ExamResults rs :(List<ExamResults>)objPage.getResult()) {
//						if (Integer.valueOf(ExStringUtils.defaultIfEmpty(rs.getCheckStatus(), "-1"))<2) {
//							String us 						= usualResults.containsKey(rs.getStudentInfo().getResourceid())?usualResults.get(rs.getStudentInfo().getResourceid()).toString():"0";
//							rs.setUsuallyScore(us);
//						}
//					}      
//				}

//				List list 		 = examResultsService.calculateExamResultsListIntegratedScore(objPage.getResult(), info, teachType);
//				objPage.setResult(list);
//				condition.put("examInfoId", info.getResourceid());
//			}

		}

		model.addAttribute("condition",condition);
		model.addAttribute("objPage", objPage);

		return"/edu3/teaching/examResult/auditExamResult_makeup_list";
	}

	/**
	 * 跳转到导出封面窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/printResultCoverInput.html")
	public String printResultCoverInput(String studentId, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		model.addAttribute("studentId", studentId);
		Map<String, Object> condition = new HashMap<String, Object>();
		Page page = new Page();
		page.setPageSize(100000);
		List<ExamSub> examSubList = examSubService.findExamSubByCondition(condition, page).getResult();
		StringBuffer examSubOption = new StringBuffer(1000);
		examSubOption.append("<option value=''></option>");
		if (examSubList != null) {
			for (ExamSub examSub : examSubList) {
				examSubOption.append("<option value='").append(examSub.getResourceid()).append("'>").append(examSub.getBatchName()).append("</option>");
			}
		}
		model.addAttribute("examSubOption", examSubOption);
		//User user = SpringSecurityHelper.getCurrentUser();
		//	model.addAttribute("classesSelect",graduationQualifService.getGradeToMajorToClasses(null,null,null,null,"coverinput_classesId","classesId"));
		return "/edu3/teaching/examResult/printResultCoverInput";
	}

	/**
	 * 导出成绩册封面
	 */
	@RequestMapping("/edu3/teaching/result/printResultCover.html")
	public void printResultCover(String classesId, String examSubId, HttpServletRequest request, HttpServletResponse response) throws WebException {
		try{
			ExamSub examSub = examSubService.get(examSubId);
			Classes classes    = classesService.get(classesId);
			User user = SpringSecurityHelper.getCurrentUser();
			if (ExStringUtils.isNotBlank(examSubId) && null!=classes) {
				List<StudentExamResultsVo> returnList   = new ArrayList<StudentExamResultsVo>();
				List<StudentInfo> stuList = studentinfoservice.findByHql("from "+StudentInfo.class.getSimpleName()+" where classes.resourceid=?", classesId);
				Map<String, Map<String, Object>> electiveExamCountMap = studentExamResultsService.getElectiveExamCountMapByStuList(stuList);
				for (StudentInfo stu : stuList) {
					Integer examCount = Integer.parseInt(electiveExamCountMap.get(stu.getResourceid()).get("examcount").toString());
					List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(stu,examCount);
					for (int i = 0; i < list.size(); i++) {
						StudentExamResultsVo vo = list.get(i);
						if (examSubId.equals(vo.getExamSubId())) {
							boolean add = true;
							for (StudentExamResultsVo studentExamResultsVo : returnList) {
								if (studentExamResultsVo.getCourseId().equals(vo.getCourseId())) {
									add = false;
									break;
								}
							}
							if (add) {
								returnList.add(vo);
							}
						}
					}
				}
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//文件输出服务器路径
				GUIDUtils.init();
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				// 打开文件
				WritableWorkbook book = Workbook.createWorkbook(disFile);
				// 生成名为“第一页”的工作表，参数0表示这是第一页
				WritableSheet sheet = book.createSheet("成绩册封面", 0);

				// 设置字体 
				WritableFont font1 = new WritableFont(WritableFont.createFont("宋体"), 22);
				WritableCellFormat format1 = new WritableCellFormat(font1);
				// 把水平对齐方式指定为居中
				format1.setAlignment(jxl.format.Alignment.CENTRE);
				// 把垂直对齐方式指定为居中
				format1.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				// 设置边框线 
//				format1.setBorder(Border.ALL, BorderLineStyle.THIN); 

				WritableFont font2 = new WritableFont(WritableFont.createFont("宋体"), 12);
				WritableCellFormat format2 = new WritableCellFormat(font2);
				// 把水平对齐方式指定为居右
				format2.setAlignment(jxl.format.Alignment.RIGHT);
				// 把垂直对齐方式指定为居中
				format2.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				// 设置边框线 
//				format2.setBorder(Border.ALL, BorderLineStyle.THIN); 
				format2.setWrap(true);//通过调整宽度和高度自动换行

				WritableCellFormat format3 = new WritableCellFormat(font2);
				// 把水平对齐方式指定为居左
				format3.setAlignment(jxl.format.Alignment.LEFT);
				// 把垂直对齐方式指定为居中
				format3.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
				// 设置边框线 
//				format3.setBorder(Border.ALL, BorderLineStyle.THIN); 
				format3.setWrap(true);//通过调整宽度和高度自动换行

//				CellView cellView = new CellView(); //自动调整列宽
//				cellView.setAutosize(true); //设置自动大小  

				int colwidth1 = 40;
				int colwidth2 = 45;
				String title = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()
						+ CacheAppManager.getSysConfigurationByCode("graduateData.transactType").getParamValue() + "成绩册";
				//标题行开始
				sheet.mergeCells(0, 0, 1, 0);
				sheet.setColumnView(0, colwidth1+colwidth2);
//				sheet.setColumnView(0, cellView);//根据内容自动设置列宽
				sheet.setRowView(0, 1000);
				sheet.addCell(new Label(0, 0, title, format1));

				sheet.setRowView(1, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, 1, "专业、班级：", format2));
				sheet.addCell(new Label(1, 1, classes.getClassname(), format3));

				sheet.setRowView(2, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, 2, "层次、形式：", format2));
				sheet.addCell(new Label(1, 2, classes.getClassic().getClassicName()+JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle", classes.getTeachingType()), format3));

				sheet.setRowView(3, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, 3, "办学地点：", format2));
				sheet.addCell(new Label(1, 3, classes.getBrSchool().getUnitName(), format3));

				sheet.setRowView(4, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, 4, "学年：", format2));
				sheet.addCell(new Label(1, 4, examSub.getYearInfo().getYearName(), format3));

				sheet.setRowView(5, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, 5, "学期：", format2));
				sheet.addCell(new Label(1, 5, JstlCustomFunction.dictionaryCode2Value("CodeExportExam", examSub.getTerm()), format3));

				sheet.mergeCells(0, 6, 0, 6+returnList.size()-1);
				sheet.setColumnView(0, colwidth1);
				sheet.setRowView(6, 500 * returnList.size());
				sheet.addCell(new Label(0, 6, "课程名称：", format2));

				int rowIdx = 6;
				for (StudentExamResultsVo vo : returnList) {
					sheet.setColumnView(1, colwidth2);
					sheet.setRowView(rowIdx, 500);
					sheet.addCell(new Label(1, rowIdx, vo.getCourseName(), format3));
					rowIdx++;
				}

				int startIdx = 6+returnList.size();
				sheet.setRowView(startIdx, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, startIdx, "", format2));
				sheet.addCell(new Label(1, startIdx, "教务员："+user.getCnName(), format2));

				startIdx++;
				sheet.setRowView(startIdx, 500);
				sheet.setColumnView(0, colwidth1);
				sheet.setColumnView(1, colwidth2);
				sheet.addCell(new Label(0, startIdx, "", format2));
				sheet.addCell(new Label(1, startIdx, ExDateUtils.formatDateStr(ExDateUtils.getCurrentDateTime(), "yyyy年MM月dd日"), format2));

				// 写入数据并关闭文件 
				book.write();
				book.close();
				logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
				downloadFile(response, title+".xls", disFile.getAbsolutePath(),true);
			} else {
				renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"请选择考试批次！\")</script>");
			}
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载成绩册封面:"+e.getMessage()+"\")</script>");
		}
	}

	/**
	 * 查看教学计划成绩录入情况
	 */
	@RequestMapping("/edu3/teaching/examresult/teachingplanexamresult-list.html")
	public String listTeachingPlanExamresult(HttpServletRequest request,HttpServletResponse response, ModelMap model, Page objPage) throws WebException {
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
		String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String teachingType = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		String classesmaster = ExStringUtils.trimToEmpty(request.getParameter("classesmaster"));
		String isInputed = ExStringUtils.trimToEmpty(request.getParameter("isInputed"));
		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		Map<String, Object> condition = new HashMap<String, Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
			model.addAttribute("showSchoolName", user.getOrgUnit().getUnitName());
		}

		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuGrade)) {
			condition.put("stuGrade", stuGrade);
		}
		if(ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesId", classesid);
		}
		if(ExStringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if(ExStringUtils.isNotEmpty(classesmaster)) {
			condition.put("classesmaster", classesmaster);
		}
		if(ExStringUtils.isNotEmpty(isInputed)) {
			condition.put("isInputed", isInputed);
		}

		if("export".equals(flag)){
			objPage.setPageSize(1000000);
		}
//		Page page = teachingJDBCService.findTeachingPlanExamresultMapByCondition(condition, objPage);
		Page page = teachingJDBCService.findCourseInputInfoByCondition(condition, objPage);
		model.addAttribute("tpelist", page);
		model.addAttribute("condition", condition);

		if("export".equals(flag)){
			String fileName = "教学计划成绩录入情况";
			try {
				fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("GB2312");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
			return "/edu3/teaching/examResult/teachingplanexamresult_export";
		}
		return "/edu3/teaching/examResult/teachingplanexamresult-list";
	}

	/**
	 * 查看成绩录入情况(正考和补考)
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/teaching/examresult/teachingplanexamresult-uninputlist.html")
	public String uninputTeachingPlanExamResult(HttpServletRequest request, HttpServletResponse response, ModelMap model, Page objPage) throws ParseException{

		objPage.setOrderBy("term");
		objPage.setOrder(Page.ASC);//设置默认排序方式
//		objPage.setPageSize(50);
		String returnUrl = "/edu3/teaching/examResult/teachingplanexamresult-uninputlist";
		try {
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String majorid = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
			String classicid	= ExStringUtils.trimToEmpty(request.getParameter("classicId"));
			String planid = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanId"));
			String teachingtype = ExStringUtils.trimToEmpty(request.getParameter("teachingtype"));
			String type = ExStringUtils.trimToEmpty(request.getParameter("type"));// 用于区别正考、补考
			//String fromPage = ExStringUtils.trimToEmpty(request.getParameter("fromPage"));// 用于区别是否使用JS点击进来
			// 要传到页面的参数
			String gradeName = ExStringUtils.trimToEmpty(request.getParameter("gradeName"));
			String classicName = ExStringUtils.trimToEmpty(request.getParameter("classicName"));
			String className = ExStringUtils.trimToEmpty(request.getParameter("className"));
			String majorName = ExStringUtils.trimToEmpty(request.getParameter("majorName"));
			String classesMaster = ExStringUtils.trimToEmpty(request.getParameter("classesMaster"));
			if(ExStringUtils.isMessyCode(gradeName)) {
				gradeName = ExStringUtils.getEncodeURIComponentByOne(gradeName);
			}
			if(ExStringUtils.isMessyCode(classicName)) {
				classicName = ExStringUtils.getEncodeURIComponentByOne(classicName);
			}
			if(ExStringUtils.isMessyCode(className)) {
				className = ExStringUtils.getEncodeURIComponentByOne(className);
			}
			if(ExStringUtils.isMessyCode(majorName)) {
				majorName = ExStringUtils.getEncodeURIComponentByOne(majorName);
			}
			if(ExStringUtils.isMessyCode(classesMaster)) {
				classesMaster = ExStringUtils.getEncodeURIComponentByOne(classesMaster);
			}
			/*if(ExStringUtils.isNotEmpty(fromPage)){
				gradeName = ExStringUtils.trimToEmpty(URLDecoder.decode(request.getParameter("gradeName"), "UTF-8"));
				classicName = ExStringUtils.trimToEmpty(URLDecoder.decode(request.getParameter("classicName"), "UTF-8"));
				className = ExStringUtils.trimToEmpty(URLDecoder.decode(request.getParameter("className"), "UTF-8"));
				majorName = ExStringUtils.trimToEmpty(URLDecoder.decode(request.getParameter("majorName"), "UTF-8"));
				classesMaster =ExStringUtils.trimToEmpty(URLDecoder.decode(request.getParameter("classesMaster"), "UTF-8"));
			}*/
			if(ExStringUtils.isNotEmpty(planid)) {
				condition.put("teachingPlanId", planid);
			}
			if(ExStringUtils.isNotEmpty(gradeId)){condition.put("gradeId", gradeId);}
			if(ExStringUtils.isNotEmpty(classesId)){condition.put("classesId", classesId);}
			if(ExStringUtils.isNotEmpty(majorid)){condition.put("majorId", majorid);}
			if(ExStringUtils.isNotEmpty(classicid)){condition.put("classicId", classicid);}
			if(ExStringUtils.isNotEmpty(teachingtype)){condition.put("teachingtype", teachingtype);}
			if(ExStringUtils.isNotEmpty(branchSchool)){condition.put("branchSchool", branchSchool);}
//			condition.put("teachType", "facestudy"); // facestudy：统考、非统考
			condition.put("type", type);

			condition.put("gradeName", gradeName);
			condition.put("classicName", classicName);
			condition.put("teachingtype", teachingtype);
			condition.put("className", className);
			condition.put("majorName", majorName);
			condition.put("classesMaster", classesMaster);
			//User curUser = SpringSecurityHelper.getCurrentUser();
			
			/*// 判断是否顶级机构人员
			boolean isTop = false;
			List<User> users = userService.findByHql("from "+User.class.getSimpleName()+" u where u.orgUnit.unitCode=?", "UNIT_ROOT");
			for (int i = 0; i < users.size(); i++) {
				User user = users.get(i);
				if (user.getResourceid().equals(curUser.getResourceid())) {
					isTop = true;
					break;
				}
			}
			
			if (!isTop) {
				// 判断是否教务员
				boolean isJwy = false;
				Set<Role> roles = curUser.getRoles();
				for (Role role : roles) {
					if ("ROLE_BRS_STUDENTSTATUS".equals(role.getRoleCode())) {
						isJwy = true;
						break;
					}
				}
				if (isJwy) {
					// 教务员录入自己负责的教学站班级的课程成绩
					condition.put("jwyId", curUser.getResourceid());
				} else {
					// 登分老师录入自己负责的班级的课程成绩
					condition.put("teachId", curUser.getResourceid());
				}
				if(null!=curUser.getOrgUnit()){
					branchSchool = curUser.getOrgUnit().getResourceid();
				}
			}*/
			if("normal".equals(type)){
				objPage = teachingJDBCService.findCourseExamInfoByCondition(condition, objPage);
			}else if ("makeup".equals(type)){
				String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));// 考试批次，用于补考
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				if(ExStringUtils.isNotEmpty(examSubId)){condition.put("examSubId", examSubId);}
				if(ExStringUtils.isNotEmpty(courseId)){condition.put("courseId", courseId);}
				objPage = examResultsService.findMakeupInfoByContion(objPage, condition);
				returnUrl = "/edu3/teaching/examResult/teachingplanexamresult-makeupInputInfo";
			}
			model.addAttribute("condition", condition);
			model.addAttribute("teachingPlanCourseList", objPage);
		} catch (Exception e) {
			logger.error("查看成绩录入情况出错", e);
		}

		return returnUrl;
	}

	/**
	 * 上传成绩单签名
	 *
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/uploadSign.html")
	public String uploadExamResultReportSign(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		try {
			String rootUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue();
			model.addAttribute("rootUrl", rootUrl);
			String serPath =  CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() + "examResultReportSign";
			// 校本部院长签名
			Attachs signDean0 = attachsService.getByConditions("signDean0.png", "examResultReportSign", serPath);
			// 校本部审核人签名
			Attachs signAuditor0 = attachsService.getByConditions("signAuditor0.png", "examResultReportSign", serPath);
			// 校外点院长签名
			Attachs signDean1 = attachsService.getByConditions("signDean1.png", "examResultReportSign", serPath);
			// 校外点审核人签名
			Attachs signAuditor1 = attachsService.getByConditions("signAuditor1.png", "examResultReportSign", serPath);

			model.addAttribute("signDean0", signDean0);
			model.addAttribute("signAuditor0", signAuditor0);
			model.addAttribute("signDean1", signDean1);
			model.addAttribute("signAuditor1", signAuditor1);
		} catch (Exception e) {
			logger.error("上传成绩单签名出错", e);
		}
		return "/edu3/teaching/examResult/upload-examResultReport-sign";
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/downloadExaminationMaintain.html")
	public void downloadExaminationMaintain(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String learningStyle  = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classes 		  = ExStringUtils.trimToEmpty(request.getParameter("classes"));


		try {
			//登录用户是否为教务员
			User user = SpringSecurityHelper.getCurrentUser();
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				branchSchool = user.getOrgUnit().getResourceid();
			}
			if(ExStringUtils.isNotBlank(gradeid)) {
				condition.put("gradeid", gradeid);
			}
			if(ExStringUtils.isNotBlank(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotBlank(learningStyle)) {
				condition.put("learningStyle", learningStyle);
			}
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotBlank(classes)) {
				condition.put("classes", classes);
			}
			if(ExStringUtils.isNotBlank(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}

			List<StateExamResultsVo> list = stateExamResultsService.findStateExamResultsVoByHql(condition);

			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//模板文件路径
			String templateFilepathString = "statusExamResultsExport.xls";

			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeTeachingType");
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeStudentStatus");
			dictCodeList.add("CodeUnScoreStyle");
			exportExcelService.initParmasByfile(disFile, "statusExamResultsExportId", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高

			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1,null );
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "学位外语成绩单.xls", excelFile.getAbsolutePath(),true);
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@RequestMapping("/edu3/teaching/result/operateRecord.html")
	public String showOperateRecord(HttpServletRequest request, ModelMap model){
		String classesid = request.getParameter("classesid");
		String courseId =  request.getParameter("courseId");
		//String examInfoId = request.getParameter("examInfoId");
		String examsubId =  request.getParameter("examsubId");
		StringBuilder builder = new StringBuilder();
		Map<String, Object> param = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		builder.append("select distinct exam.opeman,exam.opedate,exam.opetype from (");
		builder.append(" select er.fillinman opeman,er.fillindate opedate,'成绩录入'opetype,er.studentid,er.courseid,er.examsubid");
		builder.append(" from edu_teach_examresults er  where er.isdeleted=0 and er.examsubid=:examsubId and er.courseid=:courseId");
		builder.append(" union select er.auditman,er.auditdate,'成绩审核',er.studentid,er.courseid,er.examsubid");
		builder.append(" from edu_teach_examresults er where er.isdeleted=0 and er.examsubid=:examsubId and er.courseid=:courseId");
		builder.append(" )exam join edu_roll_studentinfo si on si.resourceid=exam.studentid");
		builder.append(" where si.classesid=:classesid order by opedate");

		param.put("examsubId", examsubId);
		param.put("courseId", courseId);
		param.put("classesid", classesid);
		try {
			result = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(builder.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("result", result);
		return "/edu3/teaching/examResult/examResults-operateRecord-form";
	}

	/**
	 * 撤销平时考核成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/facestudy/unSubmitOnlineCourse.html")
	public void unSubmitOnlineCourse(HttpServletRequest request, HttpServletResponse response){

		String[] studyNos      	     = request.getParameterValues("resourceid");     //要保存成绩的学号
		String courseId				 = request.getParameter("courseId");
		//String examSubId			 = request.getParameter("examSubId");
		Map<String,Object> condition = new HashMap<String, Object>();
		List<UsualResults> usualResultsList = new ArrayList<UsualResults>();
		boolean success  		  	 = true;                                         //操作结果
		String  msg = "撤销提交成功！";;
		try {
			UsualResults uResults;
			for (int i = 0; i < studyNos.length; i++) {
				if(ExStringUtils.isBlank(studyNos[i])){
					continue;
				}
				String hql = " from "+UsualResults.class.getSimpleName()+" where isDeleted=0 and studentInfo.studyNo=?"
						+ " and course.resourceid=?";
				uResults = usualResultsService.findUnique(hql, new Object[]{studyNos[i],courseId });
				if(uResults!=null){
					uResults.setStatus("0");
					usualResultsList.add(uResults);
				}
			}
			usualResultsService.batchSaveOrUpdate(usualResultsList);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.REPEAL,"平时考核成绩-撤销提交：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		} catch (Exception e) {
			// TODO: handle exception
			msg = "撤销提交失败！";
		}
		condition.put("success",success);
		condition.put("msg", msg);
		renderJson(response,JsonUtils.mapToJson(condition));
	}
	
	@RequestMapping("/edu3/teaching/universalExam/universalExam-new-list.html")
	public String displayUniversalExamList(HttpServletRequest request,ModelMap model,Page objPage) throws Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		
		if(ExStringUtils.isNotBlank(condition.get("term"))){
			ExamSub examsub = examSubService.get(condition.get("term").toString());
			if(examsub!=null){
				String _term=examsub.getYearInfo().getFirstYear()+"_0"+examsub.getTerm();
				condition.put("_term",_term );
				condition.put("examSubId",examsub.getResourceid());
				model.addAttribute("_term", _term);
			}
			
		}
		if(!condition.isEmpty()){
			//区域权限
			boolean isAdultEducation = true;	
			User user = SpringSecurityHelper.getCurrentUser();		
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
				isAdultEducation=false;
				condition.put("unitId", user.getOrgUnit().getResourceid());
			}
			model.addAttribute("isAdultEducation", isAdultEducation);
			
			objPage = iUniversalExamService.getUniversalExamList(condition,objPage);
			model.addAttribute("page", objPage);
			model.addAttribute("condition", condition);
		}			
		
		return "/edu3/teaching/examResult/universalExam-new-list";
	}
	
	@RequestMapping("/edu3/teaching/universalExam/universalExam-new-details.html")
	public String displayUniversalExamDetails(HttpServletRequest request,ModelMap model,Page objPage) throws Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		if(!condition.isEmpty()&&ExStringUtils.isNotBlank(condition.get("examType"))){
			//区域权限
			User user = SpringSecurityHelper.getCurrentUser();		
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
				condition.put("unitId", user.getOrgUnit().getResourceid());
			}
			objPage = iUniversalExamService.getUniversalExamDetails(condition, objPage);
			model.addAttribute("condition", condition);
			model.addAttribute("page", objPage);
		}
		
		return "/edu3/teaching/examResult/universalExam-new-details";
	}
	
	@RequestMapping("/edu3/teaching/universalExam/universalExam-new-export-details.html")
	public void exportDetails(HttpServletRequest request,HttpServletResponse response) throws ServiceException, Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		//区域权限
		User user = SpringSecurityHelper.getCurrentUser();		
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			condition.put("unitId", user.getOrgUnit().getResourceid());
		}
		List<UniversalExamDetailsVO> listVO = iUniversalExamService.getUniversalExamDetails(condition);
		int index=1;
		for(UniversalExamDetailsVO vo:listVO){
			vo.setIndex(index);
			index++;
		}
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		GUIDUtils.init();
		//导出
		File excelFile = null;
		File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
		
		List<String> dictCodeList = new ArrayList<String>();
		dictCodeList.add("ExamResult");
//		dictCodeList.add("CodeNoExamAppType");
//		dictCodeList.add("CodeAbnormalType");
		Map<String , Object> map1 = dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE);
		exportExcelService.initParmasByfile(disFile, "universalExamDetailsExport", listVO ,map1);
		exportExcelService.getModelToExcel().setHeader("统考学生名单");//设置大标题
		exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
		excelFile = exportExcelService.getExcelFile();//获取导出的文件
		logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
		String downloadFileName = "统考学生名单.xls";
		String downloadFilePath = excelFile.getAbsolutePath();
		downloadFile(response, downloadFileName,downloadFilePath,true);
		
	}
	
	@RequestMapping("/edu/teaching/universalExam/universalExam-new-import.html")
	public String importUErequest(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		model.addAttribute("condition", condition);
		model.addAttribute("_term", condition.get("_term").toString());
		return "/edu3/teaching/examResult/universalExam-new-import";
	}
	
	@RequestMapping("/edu/teaching/universalExam/universalExam-new-import-details.html")
	public void importUniversalExamDetails(String attachId, HttpServletRequest request,HttpServletResponse response){
		final String EmptyScore="成绩为空，跳过不导入";
		final String outsideScore="成绩不允许小于0或大于100";
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		//Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode=200;
		String message="导入完成！";
		String rendResponseStr="{statusCode:300,message:'导入失败',forwardUrl:''};";
		File excelFile = null;
		String upLoadurl = "/edu/teaching/universalExam/universalExam-new-import-fail.html";
		try {
			if( ExStringUtils.isNotBlank(attachId)){
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("ExamResult");
				importExcelService.initParmas(excel, "universalExamDetailsExport",dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYNAME));
				importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
				List<UniversalExamDetailsVO> voList= importExcelService.getModelList();
				if(ExCollectionUtils.isEmpty(voList)){
					throw new Exception("导入模版错误！");
				}
				//1、统计总行数 2、过滤空行 3、传入逻辑处理  4、返回逻辑处理结果 5、返回导入结果统计 6、下载不成功名单
				int totalCount = voList.size();
				int emptyCount = 0;				
				List<UniversalExamDetailsVO> failList= new ArrayList<UniversalExamDetailsVO>();//导入失败列表
				UniversalExamDetailsVO vo;
				for(Iterator it=voList.iterator();it.hasNext();){
					vo =(UniversalExamDetailsVO) it.next();
					if("N".equals(condition.get("examType"))){//20181030 ：如果成绩比例为100：0，0列允许为空，由于与成绩比例相关联，这里不判断为空，在后面导入成绩时再判断
//						if(ExStringUtils.isBlank(vo.getWrittenScore())){//成绩为空的，移除，并添加到失败列表中
//							vo.setMemo(EmptyScore);
//							failList.add(vo);
//							it.remove();
//							emptyCount++;
//						}else 
							if(ExStringUtils.isNotBlank(vo.getWrittenScore())){//卷面不为空
							if(ExNumberUtils.isNumber(vo.getWrittenScore())){//卷面为数值
								if(Double.parseDouble(vo.getWrittenScore())<0 ||Double.parseDouble(vo.getWrittenScore())>100){
									vo.setMemo(outsideScore);
									failList.add(vo);
									it.remove();
								}else{
//									if(ExStringUtils.isBlank(vo.getUsuallyScore())){//平时为空
//										vo.setMemo(EmptyScore);
//										failList.add(vo);
//										it.remove();
//										emptyCount++;
//									}else{//平时不为空
									if(ExStringUtils.isNotBlank(vo.getUsuallyScore())){
										if(ExNumberUtils.isNumber(vo.getUsuallyScore())){
											if(Double.parseDouble(vo.getUsuallyScore())<0 ||Double.parseDouble(vo.getUsuallyScore())>100){
												vo.setMemo(outsideScore);
												failList.add(vo);
												it.remove();
											}
										}else{
											vo.setMemo("卷面成绩为数值时，平时成绩也只允许为数值型");
											failList.add(vo);
											it.remove();
										}
									}
								}
							}else{
								if("缺".equals(vo.getWrittenScore().trim()) || "缺考".equals(vo.getWrittenScore().trim())){
									continue;
								}else if("免考".equals(vo.getWrittenScore().trim())||"免".equals(vo.getWrittenScore().trim())||"缓考".equals(vo.getWrittenScore().trim())||"缓".equals(vo.getWrittenScore().trim())){
//									it.remove();
									continue;
								}else{
									vo.setMemo(UniversalExamDetailsVO.MEMO_UNRECOGNIZED);
									failList.add(vo);
									it.remove();
								}
							}
						}						
					}else{						
						if(ExStringUtils.isBlank(vo.getWrittenScore())){//补考成绩只需要校验卷面成绩是否为空
							vo.setMemo(EmptyScore);
							failList.add(vo);
							it.remove();
							emptyCount++;
						}else {
							if(ExNumberUtils.isNumber(vo.getWrittenScore())){
								if(Double.parseDouble(vo.getWrittenScore())<0 ||Double.parseDouble(vo.getWrittenScore())>100){
									vo.setMemo(outsideScore);
									failList.add(vo);
									it.remove();
								}
							}else{
								if("缺".equals(vo.getWrittenScore().trim()) || "缺考".equals(vo.getWrittenScore().trim())){
									continue;
								}else{
									vo.setMemo(UniversalExamDetailsVO.MEMO_UNRECOGNIZED);
									failList.add(vo);
									it.remove();
								}
							}
						}
					}
				}
				
//				iUniversalExamService.handleUniversalExamDetails(condition, voList);
				failList.addAll(iUniversalExamService.handleUniversalExamDetails(condition, voList));
				
				int index=1;
				for(UniversalExamDetailsVO vo_t:failList){
					vo_t.setIndex(index++);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.IMPORT,"导入统考成绩：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				if(ExCollectionUtils.isNotEmpty(failList)){
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					//导出
					GUIDUtils.init();
					String fileName = GUIDUtils.buildMd5GUID(false);
					File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
					exportExcelService.initParmasByfile(disFile, "universalExamDetailsImportFail", failList ,null);
					exportExcelService.getModelToExcel().setHeader("统考成绩导入失败名单");//设置大标题
					exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
					excelFile = exportExcelService.getExcelFile();//获取导出的文件
					
					logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
					rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (totalCount - failList.size()) 
							+"条 | 导入失败"+  failList.size()
							+"条！其中导入的空成绩条数为 "+emptyCount+" 条!',forwardUrl:'"+upLoadurl+"?excelFile="+fileName+"'};";
				}else{
					rendResponseStr="{statusCode:200,message:'导入完成！导入成功"+totalCount+"条！',forwardUrl:''};";
				}
				
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			rendResponseStr="{statusCode:300,message:'导入失败!"+e.getMessage()+"',forwardUrl:''};";
		}finally{
			StringBuffer html = new StringBuffer();
			html.append("<script>");
			html.append("var response = "+rendResponseStr);
			html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
			html.append("</script>");
			renderHtml(response, html.toString());
		}
	}
	
	@RequestMapping("/edu/teaching/universalExam/universalExam-new-import-fail.html")
	public void downloadFail2Import(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "统考成绩导入失败名单.xls", disFile.getAbsolutePath(),true);
	}
	
	@RequestMapping("/edu3/teaching/universalExam/universalExam-new-export-count.html")
	public void exporUniversalExamCount(HttpServletRequest request,ModelMap model,HttpServletResponse response) throws Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		
		if(ExStringUtils.isNotBlank(condition.get("term"))){
			ExamSub examsub = examSubService.get(condition.get("term").toString());
			if(examsub!=null){
				String _term=examsub.getYearInfo().getFirstYear()+"_0"+examsub.getTerm();
				condition.put("_term",_term );
				condition.put("examSubId",examsub.getResourceid());
			}
			
		}
		List<UniversalExamCountVO> voList = new ArrayList<UniversalExamCountVO>();
		if(!condition.isEmpty()){
			//区域权限
			User user = SpringSecurityHelper.getCurrentUser();		
			if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
				condition.put("unitId", user.getOrgUnit().getResourceid());
			}
			voList = iUniversalExamService.getUniversalExamCount(condition);			
			HashSet<String> unitNames = new HashSet<String>();
			for(UniversalExamCountVO vo:voList){
				unitNames.add(vo.getUnitName());
				if(ExNumberUtils.isNumber(vo.getwCount())){//20180905 当有缓考时，目前在导出统计表中显示为-1，因此设置为0
					if(Integer.valueOf(vo.getwCount())<=0){
						vo.setwCount("0");
					}
				}
			}
			// 序号 年级 层次 专业 考试课程 unitNames（20180919 任务2134 增加层次）
			List<List<String>> resultList = new ArrayList<List<String>>();
			List<String> list = new ArrayList<String>();
			list.add("序号");
			list.add("年级");
			list.add("层次");
			list.add("专业");
			list.add("考试课程");	
			
			for(String unitName:unitNames){
				list.add(unitName);
			}
			list.add("小计");
			
			resultList.add(list);
			int index=1;
			List<String> _list;
			for(UniversalExamCountVO vo:voList){
				if("0".equals(vo.getwCount())){
					continue;
				}else{
					_list = new ArrayList<String>();
					_list.add(String.valueOf(index));
					index++;
					_list.add(vo.getGradeName());
					_list.add(vo.getClassicName());
					_list.add(vo.getMajorName());
					_list.add(vo.getCourseName());
					String tmpCount="";
					for(String unitName:unitNames){
						if(vo.getUnitName().equals(unitName)){
							_list.add(vo.getwCount());
							tmpCount=vo.getwCount();//不能break	
						}else{
							_list.add("");
						}
					}
					_list.add(tmpCount);
					resultList.add(_list);
				}				
			}
			List<String> tmp = new ArrayList<String>();
			//相同年级、层次、专业、课程名称的整合在一起
			int _index=0;
			for(Iterator it = resultList.iterator();it.hasNext();){
				_list = (List<String>)it.next();
				if(ExCollectionUtils.isEmpty(tmp)){
					tmp=_list;
				}else{
					if(tmp.get(1).equals(_list.get(1))//年级、层次、专业、课程名称
							&& tmp.get(2).equals(_list.get(2))
							&& tmp.get(3).equals(_list.get(3))
							&& tmp.get(4).equals(_list.get(4))){
						for(int k=5;k<_list.size();k++){
							if(ExStringUtils.isNotBlank(_list.get(k))){
								int currentCount=Integer.valueOf(tmp.get(_list.size()-1));
								int newCount =Integer.valueOf( _list.get(k));
								int finalCount = currentCount+newCount;
								tmp.set(_list.size()-1,String.valueOf(finalCount));
								tmp.set(k, _list.get(k));								
								it.remove();
								break;
							}
						}
					}else{
						_index++;
						_list.set(0,String.valueOf(_index));
						tmp=_list;
					}
				}
			}
			String year = condition.get("_term").toString().substring(0,4);
			String __term = "1".equals(condition.get("_term").toString().substring(6, 7)) ?"一":"二";
			String examType = condition.get("examType").toString();
			String _examType = "N".equals(examType) ?"正考": "Y".equals(examType) ?"一补": "T".equals(examType) ?"二补":"结补";
			String title=condition.get("_term").toString().substring(0,4)+"年第"+__term+"学期学期统考"+_examType+"人数反馈表";
			
			@Cleanup XSSFWorkbook wb = new XSSFWorkbook();
			XSSFSheet sheet = wb.createSheet();
			XSSFCellStyle cellStyle = wb.createCellStyle();
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setBorderBottom(BorderStyle.THIN);  
	        cellStyle.setBorderLeft(BorderStyle.THIN);  
	        cellStyle.setBorderRight(BorderStyle.THIN);  
	        cellStyle.setBorderTop(BorderStyle.THIN);
			
			XSSFRow row_0 = sheet.createRow(0);
			XSSFCell cell_0 = row_0.createCell(0);
			cell_0.setCellValue(title);
			cell_0.setCellStyle(cellStyle);
			int totalcol = 5+unitNames.size();
			CellRangeAddress cra =new CellRangeAddress(0, 0, 0, totalcol);//合并标题的单元格 
			sheet.addMergedRegion(cra);
			RegionUtil.setBorderBottom(1, cra, sheet,wb); // 下边框  
	        RegionUtil.setBorderLeft(1, cra, sheet,wb); // 左边框  
	        RegionUtil.setBorderRight(1, cra, sheet,wb); // 有边框  
	        RegionUtil.setBorderTop(1, cra, sheet,wb); // 上边框  
	        
	        for(int i=0;i<resultList.size();i++){
	        	XSSFRow row_x = sheet.createRow(i+1);
	        	_list = resultList.get(i);
	        	for(int j=0;j<_list.size();j++){
	        		XSSFCell cell_x = row_x.createCell(j);
	        		cell_x.setCellStyle(cellStyle);
	        		cell_x.setCellValue(_list.get(j));
	        	}
	        }
//	        sheet.autoSizeColumn((short)2);
//	        sheet.autoSizeColumn((short)3);
//	        sheet.autoSizeColumn((short)4);
	        //列宽自适应
//	        for(short col=0;col<totalcol-1;col++){
//	        	 sheet.autoSizeColumn(col,true);
//	        }
	        setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();
	        String fileName = GUIDUtils.buildMd5GUID(false);
			File disFile   = new File(getDistfilepath()+ File.separator +fileName + ".xls");
			@Cleanup FileOutputStream fileOut = new FileOutputStream(disFile);
			wb.write(fileOut);

			downloadFile(response, year+"年第"+__term+"学期"+_examType+"统考人数反馈表.xls",disFile.getAbsolutePath(),true);
		}
		
	}
	
	/**
	 * 下载导入学位外语成绩模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/statexam/downloadTemplate.html")
	public void downloadDegreeCourseTemp(HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			String templateFilepathString = "degreeCourseResultImport.xls";
			downloadFile(response, "学位外语成绩模板.xls", templateFilepathString,false);
		}catch(Exception e){
			logger.error("下载导入学位外语成绩模板出错：",e);
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"下载导入学位外语成绩模板失败\");</script>");
		}
	}
	
	/**
	 * 进入导入学位外语成绩页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/statexam/importResults-view.html")
	public String uploadDegreeCourseResultForm(HttpServletRequest request,HttpServletResponse response, ModelMap model){		
		model.addAttribute("user", SpringSecurityHelper.getCurrentUser());
		return "/edu3/teaching/examResult/degreeCourse/importResults-view";
	}
	
	/**
	 * 导入学位外语成绩逻辑处理
	 * 
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/result/statexam/importResults.html")
	public void importDegreeCourseResults(String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "导入学位外语成绩成功！";
		int totalNum =0 ;
		int failNum = 0;
		try {			
			List<DegreeCourseResultVo> failList = null;
			if( ExStringUtils.isNotBlank(attachId)){			
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				importExcelService.initParmas(excel, "importDegreeCourseResult", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				importExcelService.getExcelToModel().setStartTitleRow(3);
				List<DegreeCourseResultVo> modelList = importExcelService.getModelList();
				if(ExCollectionUtils.isNotEmpty(modelList)){
					String passDate = ExStringUtils.defaultIfEmpty(request.getParameter("passDate"), "");
					Map<String, Object> returnMap = stateExamResultsService.handleDegreeCourseResultImport(modelList,passDate);
					totalNum = modelList.size();
					if(returnMap!=null &&returnMap.size()>0 ){
						statusCode = (Integer)returnMap.get("statusCode");
						if(statusCode!=200) {
							message = (String)returnMap.get("message");
							if(statusCode==400){
								failList = (List<DegreeCourseResultVo>)returnMap.get("failList");
							}
						}
					}
					
					//导出学位外语成绩中的失败记录
					if(ExCollectionUtils.isNotEmpty(failList)){
						failNum = failList.size();
						setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
						//导出
						GUIDUtils.init();
						String fileName = GUIDUtils.buildMd5GUID(false);
						File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
							
						//模板文件路径
						String templateFilepathString = "degreeCourseResultImportError.xls";
						//初始化配置参数
						exportExcelService.initParmasByfile(disFile,"importDegreeCourseResultError", failList,null);
						exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, null);
						exportExcelService.getModelToExcel().setRowHeight(400);
						File excelFile = exportExcelService.getExcelFile();//获取导出的文件(将导出文件写到硬盘对应的目录中)
						logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());	
						String upLoadurl = "/edu3/teaching/result/statexam/exportDegreeCourseResultError.html?excelFile="+fileName;
						map.put("exportErrorDegreeCourseResult", upLoadurl);
					}
				} else {
					statusCode = 300;				
					message = "没有数据";
				}
			} else {
				statusCode = 300;				
				message = "请上传附件.";
			}
		} catch (Exception e) {
			logger.error("导入学位外语成绩出错:{}",e);
			statusCode = 300;
			message = "导入学位外语成绩失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("resultMsg", "导入成功："+(totalNum-failNum)+"条，失败："+failNum+"条");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出学位外语成绩失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/statexam/exportDegreeCourseResultError.html")
	public void exportDegreeCourseResultError(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入学位外语成绩失败记录.xls", disFile.getAbsolutePath(),true);
	}

	/**
	 * 导出学生补考成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examresults/exportFailExamResult.html")
	public void exportFailExamResult(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map     = new HashMap<String, Object>();

		String classesIds		= request.getParameter("classesIds");
		String courseIds		= request.getParameter("courseIds");
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
		String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		List<ExamSub> subList = examSubService.findByCondition(condition);
		ExamSub examSub = null;
		JasperPrint jasperPrint= null;//输出的报表
		if (subList != null && subList.size() > 0) {
			examSub = subList.get(0);
			if (ExStringUtils.isNotBlank4All(classesIds, courseIds)) {//勾选查询
				String classidArray[] = classesIds.split(",");
				String courseidArray[] = courseIds.split(",");
				StringBuilder builderSQL = new StringBuilder(50*classidArray.length);
				builderSQL.append(" and (");
				for (int index = 0; index < classidArray.length; index++) {
					builderSQL.append("(exam.studentInfo.classes.resourceid='").append(classidArray[index]).append("' and exam.course.resourceid='").append(courseidArray[index]).append("')");
					if (index != classidArray.length-1) {
						builderSQL.append(" or ");
					}
				}
				builderSQL.append(")");
				condition.remove("classesIds");
				condition.remove("courseIds");
				condition.put("calssesAndCourseIds",builderSQL.toString());
			}
			if (condition.containsKey("teacherName")) {
				if (ExStringUtils.isMessyCode(condition.get("teacherName").toString())) {
					condition.put("teacherName",ExStringUtils.getEncodeURIComponentByOne(condition.get("teacherName").toString()));
				}
			}
			//condition.put("checkStatus_in","'1','4'");
			List<ExamResults> list = examResultsService.findExamResultsByCondition(condition);

			if (list != null && list.size() > 0) {
				try {
					String export_jasper = "courseExamResults_facestudy_makeup_export.jasper";
					String reprotFile = File.separator + "reports" + File.separator + "examPrint" + File.separator + export_jasper;
					String title = schoolName;
					String term = examSub.getTerm();
					Long year = examSub.getYearInfo().getFirstYear();

					//改成某年某季学期非统考课程期末考试
					String batchName = year + "年" + ("1".equals(term) ? "春季" : "秋季");
					List<Map<String, Object>> dm = examResultsService.getMapInfoListByExamResultListForPrint(list, null, "N");

					if ("10560".equals(schoolCode)) {//汕大
						title += schoolConnectName + "学院";
						map.put("title2", "补考、补修成绩记载表");
						batchName += "学期";
					} else {//默认广大
						title += "成人高等教育考试";
						//由广州大学 函授部提出  修改为与系统查询的考试批次名称一致的叫法   20160612
						batchName = examSub.getBatchName();
					}
					JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dm);

					File reprot_file = new File(URLDecoder.decode(request.getSession().getServletContext().getRealPath(reprotFile), "utf-8"));

					map.put("school", title);//学校名称
					map.put("batchName", batchName);//考试批次名称
					jasperPrint = JasperFillManager.fillReport(reprot_file.getPath(), map, dataSource); // 填充报表
					String fileType = request.getParameter("fileType");
					GUIDUtils.init();
					File disFile = new File(getDistfilepath() + File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
					String filePath = disFile.getAbsolutePath();
					JRAbstractExporter exporter = null;
					if ("pdf".equalsIgnoreCase(fileType)) {
						exporter = new JRPdfExporter();
					} else if ("doc".equalsIgnoreCase(fileType) || "docx".equalsIgnoreCase(fileType)) {
						exporter = new JRDocxExporter();
					} else if ("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)) {
						exporter = new JRXlsExporter();
					}
					//JRXlsExporter exporter  = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, filePath);
					exporter.exportReport();

					try {
						//设置excel打印参数
						ExamPrintServiceImpl.setExcelPrintSetting(filePath);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					downloadFile(response, "补考总评成绩." + fileType, filePath, true);
				} catch (Exception e) {
					String msg = "导出成绩出错：{}" + e.fillInStackTrace();
					logger.error(msg);
					renderHtml(response, "<script>alert(" + "\"" + msg + "\"" + ")</script>");
				}
			} else {
				renderHtml(response, "<script>alert(\"抱歉，查询学生成绩为空！\")</script>");
			}
		}else {
			String msg = "没有当前补考批次";
			renderHtml(response, "<script>alert(\""+msg+"\")</script>");
		}
	}
}
