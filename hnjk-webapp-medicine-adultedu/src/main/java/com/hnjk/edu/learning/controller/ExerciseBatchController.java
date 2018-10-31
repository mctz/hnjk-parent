package com.hnjk.edu.learning.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.ActiveCourseExam;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.Exercise;
import com.hnjk.edu.learning.model.ExerciseBatch;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IExerciseBatchService;
import com.hnjk.edu.learning.service.IExerciseService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentExerciseService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.impl.ClassesServiceImpl;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimetable;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.edu.teaching.service.ITeachingActivityTimeSettingService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimetableService;
import com.hnjk.edu.teaching.service.impl.TeachingPlanCourseTimetableServiceImpl;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.util.ProjectHelper;
import com.sun.org.apache.xalan.internal.lib.ExsltStrings;

/**
 * 课后作业批次管理
 * <code>ExerciseBatchController</code><p>
 *  
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-6 上午11:23:09
 * @see 
 * @version 1.0
 */
@Controller
public class ExerciseBatchController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 4656263801217714374L;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("exerciseBatchService")
	private IExerciseBatchService exerciseBatchService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("studentExerciseService")
	private IStudentExerciseService studentExerciseService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("exerciseService")
	private IExerciseService exerciseService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	@Autowired
	@Qualifier("teachingActivityTimeSettingService")
	private ITeachingActivityTimeSettingService teachingActivityTimeSettingService;
	
	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimetableService")
	private ITeachingPlanCourseTimetableService tpctService;
	
	/**
	 * 课后作业批次列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/edu3/metares/exercise/unactive/list.html")
	public String listExerciseBatch(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		logger.info("内存溢出追踪：课后作业批次列表 /edu3/metares/exercise/unactive/list.html：User:"+SpringSecurityHelper.getCurrentUser().getUsername());
		objPage.setOrderBy("course.courseCode,yearInfo.firstYear desc,term desc,startDate,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式			
		
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String colName = ExStringUtils.trimToEmpty(request.getParameter("colName"));
		String colType = request.getParameter("colType");	
		String status = request.getParameter("status");
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String classesId = request.getParameter("classesId");
		String branchSchoolId ="";		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();	
		User user = SpringSecurityHelper.getCurrentUser();
		try {
			condition = ProjectHelper.accessDataFilterCondition(condition, user);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// 获取该用户作为班主任的所有班级
		String classesIds = "";
		try {
			condition.put("hasResource", "Y");
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			StringBuffer SBFcourse = new StringBuffer("<select style='width:240px;' class='flexselect' id='exerciseBatch_courseId' name='courseId' >");
			SBFcourse.append("<option value=''>请选择</option>");
			if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
				for(Map<String, Object> map : list){
					if(map.get("courseid").equals(courseId)){
						SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}else{
						SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}
				}
			}
			SBFcourse.append("</select>");
			model.put("exercisebatchlistCourseSelect", SBFcourse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		condition.clear();
		
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(colName)){
			condition.put("colName", colName);
		}
		if(ExStringUtils.isNotEmpty(colType)){
			condition.put("colType", colType);
		}
		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", status);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)){
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		condition.put("studentStatus", "'11','16','25'");
		Page page = exerciseBatchService.findExerciseBatchByCondition(condition, objPage);
		List<String> classesIdList = new ArrayList<String>();
		List<String> schoolClassesIdList = new ArrayList<String>();
		String _teacherId = "";
		if(ExCollectionUtils.isNotEmpty(page.getResult())){
			Map<String, Object> parms = new HashMap<String, Object>();
			try {
				parms = ProjectHelper.accessDataFilterCondition(parms, user);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			List<String> courseIdList = null;
			if(!parms.isEmpty()){
				 courseIdList = tpctService.findCourseIdByCondition(parms);
			}
			List<String> exercisebatchids = new ArrayList<String>();
			Map<String,Object> param = new HashMap<String,Object>();
			List _exerciseBatchList = new ArrayList();
			for (Object obj : page.getResult()) {
				ExerciseBatch b = (ExerciseBatch) obj;
				if(ExStringUtils.isNotEmpty(b.getClassesIds())){
					List<String> batchClassIds = new ArrayList<String>(Arrays.asList(b.getClassesIds().split(",")));
					if(ExCollectionUtils.isNotEmpty(schoolClassesIdList)){// 教务员
						batchClassIds.retainAll(schoolClassesIdList);
						if(ExCollectionUtils.isEmpty(batchClassIds)){
							continue;
						}
					} else {
						if(ExStringUtils.isNotEmpty(_teacherId) && ExCollectionUtils.isNotEmpty(classesIdList)){// 班主任并且是老师
							batchClassIds.retainAll(classesIdList);
							if(!(_teacherId.equals(b.getFillinManId()) || ExCollectionUtils.isNotEmpty(batchClassIds))){
								continue;
							}
						}else {
							if(ExStringUtils.isNotEmpty(_teacherId) && !_teacherId.equals(b.getFillinManId())){// 老师
								continue;
							}else if( ExCollectionUtils.isNotEmpty(classesIdList)){// 班主任
								batchClassIds.retainAll(classesIdList);
								if(ExCollectionUtils.isEmpty(batchClassIds)){
									continue;
								}
							}
						}
					}
				}
				if(ExCollectionUtils.isNotEmpty(courseIdList) && !courseIdList.contains(b.getCourse().getResourceid())){
					continue;
				}
				if(ExStringUtils.isEmpty(b.getClassesIds())){
					b.setClassesNames("所有有该门课程的班级");
				}
				_exerciseBatchList.add(b);
				exercisebatchids.add(b.getResourceid());
			}
			page.setResult(_exerciseBatchList);
			page.setTotalCount(_exerciseBatchList.size());
			model.addAttribute("exerciseBatchList", page);
			
			if(parms.containsKey("unitId")){//校外学习中心
				branchSchoolId = parms.get("unitId").toString();
				model.addAttribute("unitId", parms.get("unitId"));
			}
			List<Map<String,Object>> list = null;
			if(ExCollectionUtils.isNotEmpty(exercisebatchids)){
				param.put("exercisebatchids", exercisebatchids);
				param.put("studentStatus", "'11','16','25'");
				list = exerciseBatchService.findExerciseStatusCount(param);
			}
			
			if(ExCollectionUtils.isNotEmpty(list)){
				Map<String, Object> resultMap = new HashMap<String, Object>();
				for (Map<String, Object> map : list) {
					resultMap.put(map.get("EXERCISEBATCHID").toString(), map);										
				}
				model.addAttribute("resultMap", resultMap);
			}
		}
		model.addAttribute("condition", condition);
		
		String classesCondition = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(user)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and (resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+user.getResourceid()+"') "
						+ " or classesmasterid='"+user.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/exercisebatch/exercisebatch-list";
	}
	
	/**
	 * 新增编辑作业批次
	 * @param courseId
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactiveexercise/input.html")
	public String editExerciseBatch(String resourceid,String colType, ModelMap model) throws WebException {
		ExerciseBatch exerciseBatch = null;
		String courseId = "";
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			exerciseBatch = exerciseBatchService.get(resourceid);				
			if("2".equals(exerciseBatch.getColType())){
				for (Exercise exercise : exerciseBatch.getExercises()) {
					List<Attachs> attachs = attachsService.findAttachsByFormId(exercise.getResourceid());
					exercise.setAttachs(attachs);
				}
			}
			courseId = exerciseBatch.getCourse().getResourceid();
		}else{ //----------------------------------------新增
			exerciseBatch = new ExerciseBatch();	
			exerciseBatch.setIsScoring(Constants.BOOLEAN_YES);
			exerciseBatch.setScoringType("2");
			exerciseBatch.setIsShowcorrect(Constants.BOOLEAN_YES);
			exerciseBatch.setColType(ExStringUtils.defaultIfEmpty(colType, "1"));
			
			// /*里面的是以前注释的
			//Grade grade = gradeService.getDefaultGrade();
			//if(grade != null){
			//	exerciseBatch.setYearInfo(grade.getYearInfo());
			//	exerciseBatch.setTerm(grade.getTerm());
				/*String endDate = JstlCustomFunction.dictionaryCode2Value("CourseExercisesEndTime", grade.getTerm());
				endDate=grade.getGradeName().substring(0, 4)+"-"+endDate;
				if(endDate != null&& !endDate.equals("")){
					Date endD = new Date();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try {
						 endD = sdf.parse(endDate);
					} catch (ParseException e) {
					}
					exerciseBatch.setEndDate(endD);
				}*/
			//} 
			String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
			if (null!=yearTerm) {
				String[] ARRYyterm = yearTerm.split("\\.");
				exerciseBatch.setYearInfo(yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])));
				exerciseBatch.setTerm(ARRYyterm[1]);
			}
		}
		
		Map<String,Object> condition = new HashMap<String,Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		String isBrschool = "N";
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			isBrschool = "Y";
		}
		model.addAttribute("isBrschool", isBrschool);
		try {
			/*String teacherId = "";
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
				teacherId = user.getResourceid();	
			}
			if(ExStringUtils.isNotBlank(teacherId)){
				condition.put("teacherId",teacherId);
			}*/
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				String teacherId = "";
				String classesIds = classesService.findByMasterId(user.getResourceid());
				if(ExStringUtils.isNotEmpty(classesIds)){
					condition.put("classesIds",Arrays.asList(classesIds.split(",")));
				}
				if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){// 该用户为老师
					teacherId = user.getResourceid();	
				}
				if(ExStringUtils.isNotBlank(teacherId)){
					condition.put("teacherId",teacherId);
				}
			}else{
				condition.put("schoolId",user.getOrgUnit().getResourceid());
			}
			
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			StringBuffer SBFcourse = new StringBuffer("<select class='flexselect' id='exerciseBatchForm_CourseId' name='courseId' >");
			SBFcourse.append("<option value=''>请选择</option>");
			if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
				for(Map<String, Object> map : list){
					if(map.get("courseid").equals(courseId)){
						SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}else{
						SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
					}
				}
			}
			SBFcourse.append("</select>");
			model.put("exercisebatchformCourseSelect", SBFcourse.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		model.addAttribute("exerciseBatch", exerciseBatch);		
		
		long year  = ExDateUtils.getCurrentYear();
		model.addAttribute("year", year);
		
		try {
			//加上当前学期学习时间限制
			LearningTimeSetting setting = learningTimeSettingService.findLearningTimeSetting(exerciseBatch.getYearInfo().getResourceid(), exerciseBatch.getTerm());
//			model.addAttribute("learningStartTime", ExDateUtils.formatDateStr(setting.getStartTime(), ExDateUtils.PATTREN_DATE));
			model.addAttribute("learningStartTime", ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE));
			model.addAttribute("learningEndTime", ExDateUtils.formatDateStr(setting.getEndTime(), ExDateUtils.PATTREN_DATE));
		
		} catch (Exception e) {
		}
		return "/edu3/learning/exercisebatch/exercisebatch-form";
	}
	
	
	/**
	 *班级选择对话框
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/metares/exercise/unactiveexercise/classes-select.html")
	public String listClasses(Page objPage,HttpServletRequest request,ModelMap model) throws Exception{
		//查询条件
		Map<String,Object> paramMap = new HashMap<String,Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		paramMap.put("branchSchool", user.getOrgUnit().getResourceid());
		
		String courseId = request.getParameter("courseId");
		String yearInfo = request.getParameter("yearInfo");
		String term 	= request.getParameter("term");
		String onlineClassesId 	= request.getParameter("onlineClassesId");
				
		if(ExStringUtils.isNotBlank(yearInfo) || ExStringUtils.isNotBlank(term)){
			yearInfo = ExStringUtils.isNotBlank(yearInfo) ? yearInfo : "";
			term 	 = ExStringUtils.isNotBlank(term) ? term : "";
			YearInfo y = yearInfoService.get(yearInfo);
			yearInfo = null != y ? y.getFirstYear()+"" : "";
			paramMap.put("yearInfoTerm", "%"+yearInfo+"_0"+term+"%");
		}
		
		if(ExStringUtils.isNotBlank(courseId)){
			paramMap.put("courseId", courseId);
		}
		
		if(SpringSecurityHelper.isUserInRole(roleCode)){
			paramMap.put("teacherId", user.getResourceid());
		}
		objPage = exerciseBatchService.findonlineClasses(paramMap, objPage);
		request.setAttribute("classesList", objPage);
		request.setAttribute("courseId", courseId);
		request.setAttribute("yearInfo", yearInfo);
		request.setAttribute("term", term);
		
		if (ExStringUtils.isNotBlank(onlineClassesId)) {
			request.setAttribute("classesIdAry",onlineClassesId.split(","));
		}
		
		return "/edu3/learning/exercisebatch/selector-classes";
	}
	
	
	/**
	 * 保存课后作业批次
	 * @param exerciseBatch
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactiveexercise/save.html")
	public void saveExerciseBatch(ExerciseBatch exerciseBatch,String courseId,String yearInfoId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			//判断时间是否在正常的教学活动时间内
			Map<String,Object> condition = new HashMap<String,Object>();
			
			if(ExStringUtils.isNotEmpty(exerciseBatch.getTerm())) {
				condition.put("term", exerciseBatch.getTerm());
			}
			if(ExStringUtils.isNotEmpty(yearInfoId)) {
				condition.put("yearInfoId", yearInfoId);
			}
			String onlineClassesId = request.getParameter("onlineClassesId");//班级ids
			String onlineClasses = request.getParameter("onlineClasses");//班级名称
			//教学活动时间
			condition.put("mainProcessType", "teachingActiveTime");
			
			if(ExStringUtils.isBlank(courseId)){
				map.put("statusCode", 300);
				map.put("message", "课程未选择，请重新选择课程！");
			} 
			else {
				if("2".equals(exerciseBatch.getColType())){ //主观题
					exerciseBatch.setIsCountdown(Constants.BOOLEAN_NO);
					exerciseBatch.setIsShowcorrect(Constants.BOOLEAN_NO);
					exerciseBatch.setCountdownTime(null);
				} else {
					exerciseBatch.setScoringType("2");
				}
				exerciseBatch.setCourse(courseService.get(courseId));
				if(ExStringUtils.isNotEmpty(yearInfoId)){
					exerciseBatch.setYearInfo(yearInfoService.get(yearInfoId));
				}	
				User user = SpringSecurityHelper.getCurrentUser();
				String teacherId = null;
				if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
					teacherId = user.getResourceid();
				}
				if(ExStringUtils.isNotBlank(exerciseBatch.getResourceid())){ //--------------------更新
					ExerciseBatch preExerciseBatch = exerciseBatchService.get(exerciseBatch.getResourceid());					
					ExBeanUtils.copyProperties(preExerciseBatch, exerciseBatch);
					preExerciseBatch.setClassesIds(onlineClassesId);
					preExerciseBatch.setClassesNames(onlineClasses);
					preExerciseBatch.setOrderCourseNum(studentLearnPlanService.getOnlineStudentNum(courseId,exerciseBatch.getYearInfo().getFirstYear()+"",exerciseBatch.getTerm(),teacherId,onlineClassesId));
					exerciseBatchService.update(preExerciseBatch);				
				}else{ //-------------------------------------------------------------------保存
					exerciseBatch.setFillinMan(user.getCnName());
					exerciseBatch.setFillinManId(user.getResourceid());
					exerciseBatch.setClassesIds(onlineClassesId);
					exerciseBatch.setClassesNames(onlineClasses);
					exerciseBatch.setOrderCourseNum(studentLearnPlanService.getOnlineStudentNum(courseId,exerciseBatch.getYearInfo().getFirstYear()+"",exerciseBatch.getTerm(),teacherId,onlineClassesId));
					exerciseBatchService.save(exerciseBatch);
				}
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_METARES_EXERCISE_EXERCISEBATCH");
				map.put("reloadUrl", request.getContextPath() +"/edu3/metares/exercise/unactiveexercise/input.html?resourceid="+exerciseBatch.getResourceid());
				
				String isPublished = ExStringUtils.trimToEmpty(request.getParameter("isPublished"));
				if("Y".equals(isPublished) && exerciseBatch.getStatus()==0 && exerciseBatch.getExercises().isEmpty()){//稍后发布
					map.put("statusCode", 300);
					map.put("message", "您还未添加作业题目，不能稍后发布！");
				}
			}
		}catch (Exception e) {
			logger.error("保存课后作业批次出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除课后作业批次
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactiveexercise/remove.html")
	public void removeExerciseBatch(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				exerciseBatchService.deleteExerciseBatch(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/exercise/unactive/list.html");
			}
		} catch (Exception e) {
			logger.error("删除课后作业批次出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 是否发布作业
	 * @param resourceid
	 * @param type
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactiveexercise/status.html")
	public void statusExerciseBatch(String resourceid,Integer type,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				
				exerciseBatchService.statusExerciseBatch(resourceid.split("\\,"), type);
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/exercise/unactive/list.html");
			}
		} catch (Exception e) {
			logger.error("发布课后作业出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 选择题目列表
	 * @param colsId
	 * @param syllabusId
	 * @param isPublished
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/exercisebatch/exercise/list.html")
	public String listExercise(String colsId,String syllabusId,String isPublished,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("isPublished,syllabus.resourceid,showOrder");
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = new HashMap<String,Object>();	
		
		if(ExStringUtils.isNotEmpty(colsId)){
			ExerciseBatch exerciseBatch = exerciseBatchService.get(colsId);	
			model.addAttribute("exerciseBatch", exerciseBatch);
			if("1".equals(exerciseBatch.getColType())){//客观题
				String courseId = exerciseBatch.getCourse().getResourceid();
				condition.put("colsId", colsId);
				condition.put("courseId", courseId);
				if(ExStringUtils.isNotBlank(syllabusId)){
					condition.put("syllabusId", syllabusId);
				}
				condition.put("isPublished", Constants.BOOLEAN_YES);
				Set<Exercise> listExams = exerciseBatch.getExercises();
				List<String> ids = new ArrayList<String>();
				for (Exercise exercise : listExams) {
					ids.add(exercise.getCourseExam().getResourceid());				
				}
				if(!ids.isEmpty()){
					condition.put("notInCourseExams", ids);
				}
				Page page = activeCourseExamService.findActiveCourseExamByCondition(condition, objPage);
				model.addAttribute("activeCourseExamList", page);
				
				if(ExCollectionUtils.isNotEmpty(page.getResult())){
					Map<String,String> map = new HashMap<String, String>();
					for (Object	obj : page.getResult()) {
						ActiveCourseExam exam = (ActiveCourseExam)obj;
						if(!map.containsKey(exam.getSyllabus().getResourceid())){
							map.put(exam.getSyllabus().getResourceid(), exam.getResourceid());
						}
					}
					model.addAttribute("syllabusExam", map);
				}
				
				List<Syllabus> syllabusList = syllabusService.findSyllabusTreeList(courseId);
				model.addAttribute("syllabusList",syllabusList);
				if(condition.containsKey("notInCourseExams")){
					condition.remove("notInCourseExams");
				}
			} else {
				String resourceid = request.getParameter("resourceid");
				if(ExStringUtils.isNotBlank(resourceid)){
					Exercise exercise = exerciseService.get(resourceid);
					exercise.setAttachs(attachsService.findAttachsByFormId(exercise.getResourceid()));
					model.addAttribute("exercise", exercise);
				} else {
					Exercise exercise = new Exercise();
					model.addAttribute("exercise", exercise);
				}
				model.addAttribute("storeDir", SpringSecurityHelper.getCurrentUserName());
			}
		}			
		model.addAttribute("condition", condition);		
		return "/edu3/learning/exercisebatch/exercisebatch-exercise-list";
	}
	/**
	 * 保存添加的作业题目
	 * @param resourceid
	 * @param colsId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/exercisebatch/exercise/save.html")
	public void saveExercise(String resourceid,String colsId,String question,String answer,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(colsId)){
				ExerciseBatch exerciseBatch = exerciseBatchService.get(colsId);
				if("1".equals(exerciseBatch.getColType())){//客观题
					String[] ids = resourceid.split("\\,");				
					if(ids!=null && ids.length>0){
						if(exerciseBatch.getObjectiveNum()!=null && exerciseBatch.getExercises().size()+ids.length>exerciseBatch.getObjectiveNum()){
							throw new WebException("客观题题目数为<b>"+exerciseBatch.getObjectiveNum()+"</b>，当前已添加"+exerciseBatch.getExercises().size()+"道题目");
						}
						int showOrder = exerciseBatch.getNextShowOrder();
						Double score = 100.0/(exerciseBatch.getExercises().size()+ids.length);
						List<Exercise> list = new ArrayList<Exercise>();
						for (String id : ids) {
							ActiveCourseExam exam = activeCourseExamService.get(id);
							Exercise exercise = new Exercise();
							exercise.setExerciseBatch(exerciseBatch);
							exercise.setCourseExam(exam.getCourseExam());
							exercise.setShowOrder(showOrder++);
							exercise.setSyllabus(exam.getSyllabus());
							exercise.setScore(score);
							
							list.add(exercise);
						}	
						exerciseBatch.getExercises().addAll(list);
						exerciseBatchService.update(exerciseBatch);
					}
				} else {
					String[] uploadfileids = request.getParameterValues("uploadfileid");
					String limitNum = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("limitNum")), "0");
					User user = SpringSecurityHelper.getCurrentUser();
					Integer showOrder = exerciseBatch.getNextShowOrder();
					Exercise exercise = new Exercise();
					CourseExam courseExam = new CourseExam();
					if(ExStringUtils.isNotBlank(resourceid)){
						exercise = exerciseService.get(resourceid);
						courseExam = exercise.getCourseExam();
						showOrder = exercise.getShowOrder();
					}					
					courseExam.setQuestion(question);
					courseExam.setAnswer(answer);
					courseExam.setExamType("5");
					courseExam.setCourse(exerciseBatch.getCourse());
					courseExam.setFillinDate(new Date());
					courseExam.setFillinMan(user.getCnName());
					courseExam.setFillinManId(user.getResourceid());
					courseExam.setIsEnrolExam(Constants.BOOLEAN_NO);					
					courseExam.setExamform("exercise");
					courseExam.setShowOrder(showOrder);
					
					if(uploadfileids != null && uploadfileids.length > 0){
						exercise.setHasAttachs(Constants.BOOLEAN_YES);
					}
					exercise.setCourseExam(courseExam);
					exercise.setExerciseBatch(exerciseBatch);
					exercise.setShowOrder(showOrder);
					exercise.setLimitNum(Integer.valueOf(limitNum));
					exercise.setScore(100.0/(exerciseBatch.getExercises().size()+1));
					exerciseBatch.getExercises().add(exercise);
					exerciseBatchService.saveSubjectiveExercise(exerciseBatch,exercise,uploadfileids);
					map.put("callbackType", "closeCurrent");
				}								
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("reloadUrl", request.getContextPath()+"/edu3/metares/exercise/unactiveexercise/input.html?resourceid="+colsId);
			}
		} catch (Exception e) {
			logger.error("增加课后作业题目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除作业题目
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/exercisebatch/exercise/remove.html")
	public void removeExercise(String colsId,String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				exerciseBatchService.deleteExercise(colsId,resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/metares/exercise/unactiveexercise/input.html?resourceid="+colsId);
			}
		} catch (Exception e) {
			logger.error("删除课后作业出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 更新作业截止时间
	 * @param resourceid
	 * @param endDate
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/exercise/unactiveexercise/enddate/save.html")
	public void saveExerciseBatchEndDate(String resourceid, Date endDate,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid) && endDate != null){	
				ExerciseBatch batch = exerciseBatchService.get(resourceid);
				batch.setEndDate(endDate);
				exerciseBatchService.update(batch);
				
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
			}
		} catch (Exception e) {
			logger.error("更新作业截止时间出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "更新作业截止时间失败！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
