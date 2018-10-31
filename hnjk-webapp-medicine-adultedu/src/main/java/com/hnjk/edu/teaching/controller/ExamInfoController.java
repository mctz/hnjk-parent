package com.hnjk.edu.teaching.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ICourseExamPapersService;
import com.hnjk.edu.learning.service.ICourseExamRulesService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 考试信息Controller
 * @author luof
 *
 */
@Controller
public class ExamInfoController extends BaseSupportController{
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
//	@Autowired
//	@Qualifier("brschoolExamInfoService")
//	private IBrschoolExamInfoService brschoolExamInfoService;
	
	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("courseExamRulesService")
	private ICourseExamRulesService courseExamRulesService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("courseExamPapersService")
	private ICourseExamPapersService courseExamPapersService;
	
	/**
	 * 根据考试批次ID获取考试课程
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examinfo/findExamSubCourseList.html")
	public void findExamInfoCourseBySubId(HttpServletRequest request,HttpServletResponse response){
		
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		List<Course> courseList=new ArrayList<Course>();
		
		if (ExStringUtils.isNotEmpty(examSubId)) {
			courseList = examInfoService.findExamInfoCourseBySubId(examSubId);
		}
		renderJson(response, JsonUtils.listToJson(courseList));
	}
	/**
	 * 面授课程-列表
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	/*
	@RequestMapping("/edu3/teaching/brschool/examinfo-list.html")
	public String brSchoolExamInfoList(HttpServletRequest request,ModelMap model,Page page){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSub    			 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String courseId     		 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String status                = ExStringUtils.trimToEmpty(request.getParameter("status")); 
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String examStartTime		 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String examEndTime  		 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		
		Date startTime               = null;
		Date endTime                 = null;
		
		User curUser                 = SpringSecurityHelper.getCurrentUser();
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = curUser.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		if (ExStringUtils.isNotEmpty(examStartTime)){
			startTime                 = ExDateUtils.convertToDateTime(examStartTime);
		}
		if (ExStringUtils.isNotEmpty(examEndTime)){
			endTime                  = ExDateUtils.convertToDateTime(examEndTime);
		}
		
		if (ExStringUtils.isNotEmpty(status))    	condition.put("status", Integer.parseInt(status));
		if (ExStringUtils.isNotEmpty(courseId)) 	condition.put("courseId", courseId);
		if (ExStringUtils.isNotEmpty(examSub)) 		condition.put("examSub", examSub);
		if (ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
		if(null!=startTime)  						condition.put("startTime", startTime);
		if(null!=endTime)  							condition.put("endTime", endTime);
		
		if (!condition.isEmpty()&&condition.containsKey("examSub")) {
			page = brschoolExamInfoService.findBrschoolExamInfoByCondition(condition, page);
		}
		
		model.put("page",page);
		model.put("condition", condition);
		
		return "/edu3/teaching/examSub/brSchoolExamInfo-list";
	}
	
	*/
	
	/**
	 * 面授课程-表单
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	/*
	
	@RequestMapping("/edu3/teaching/brschool/examinfo-form.html")
	public String brSchoolExamInfoForm (HttpServletRequest request,ModelMap model){
		String resourceid  = ExStringUtils.trimToEmpty(request.getParameter("resourceid")); 
		
		if (ExStringUtils.isNotEmpty(resourceid)) {
			BrschoolExamInfo info = brschoolExamInfoService.load(resourceid);
			model.put("brschoolExamInfo", info);
		}
		User curUser   	   = SpringSecurityHelper.getCurrentUser();
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.put("isBrschool", true);
			model.put("brschool", curUser.getOrgUnit());
		}
		String hql 	= "from "+Course.class.getSimpleName()+" course where course.status=1 and course.isDeleted =0 and course.isUniteExam='N' and (course.examType=0 or course.examType=1 or course.examType=2 or course.examType=3 or course.isPractice='Y' or course.examType is null )order by course.courseName";
		
		List<Course> courseList 	= courseService.findByHql(hql);
		model.put("courseList",courseList);
		
		return "/edu3/teaching/examSub/brSchoolExamInfo-form";
	}
	*/
	/**
	 * 面授课程-保存
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	/*
	@RequestMapping("/edu3/teaching/brschool/examinfo-save.html")
	public void saveBrSchoolExamInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map	     = new HashMap<String, Object>();
		String resourceid			 = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String examSub    		 	 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String[] course_ids     	 = request.getParameterValues("coursems2side__dx");
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String examStartTime		 = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String examEndTime  		 = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		Date curDate                 = new Date();
		try {
			
			if (null==course_ids||ExStringUtils.isEmpty(examSub)||ExStringUtils.isEmpty(branchSchool)||
					ExStringUtils.isEmpty(examStartTime)||ExStringUtils.isEmpty(examEndTime)) {
					throw new WebException("保存失败,参数不完整！");
			}
			
			User curUser   	  			 = SpringSecurityHelper.getCurrentUser();

			if (ExStringUtils.isNotEmpty(resourceid)) {
				BrschoolExamInfo info    = brschoolExamInfoService.get(resourceid);
				
				if (1==info.getStatus()) {
					throw new WebException(info.getCourse().getCourseName()+"已通过审核，不允许修改！");
				}
				
				info.setExamSub(examSubService.get(examSub));
				info.setBrSchool(orgUnitService.get(branchSchool));
				info.setCourse(courseService.get(course_ids[0]));
				info.setStartTime(ExDateUtils.convertToDateTime(examStartTime));
				info.setEndTime(ExDateUtils.convertToDateTime(examEndTime));
				info.setFillinMan(curUser.getUsername());
				info.setFillinManId(curUser.getResourceid());
				info.setFillinTime(curDate);
				
				brschoolExamInfoService.update(info);
				
			}else {
				ExamSub sub = examSubService.load(examSub);
				OrgUnit unit= orgUnitService.load(branchSchool);
				for (String id:course_ids) {
					BrschoolExamInfo info    = new BrschoolExamInfo();
					
					info.setExamSub(sub);
					info.setBrSchool(unit);
					info.setCourse(courseService.load(id));
					info.setStartTime(ExDateUtils.convertToDateTime(examStartTime));
					info.setEndTime(ExDateUtils.convertToDateTime(examEndTime));
					info.setFillinMan(curUser.getUsername());
					info.setFillinManId(curUser.getResourceid());
					info.setFillinTime(curDate);
					
					brschoolExamInfoService.save(info);
				}
			}
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("callbackType", "closeCurrent");

			//map.put("navTabId", "RES_TEACHING_EXAM_BRSCHOOL_EXAMINFO_LIST");
			//map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/brschool/examinfo-list.html?examSub="+examSub+"&branchSchool="+branchSchool);
			
		} catch (Exception e) {
			logger.error("保存面授考试课程出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:"+e.getMessage());
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 面授课程-删除
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	/*
	@RequestMapping("/edu3/teaching/brschool/examinfo-del.html")
	public void delBrSchoolExamInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map	       = new HashMap<String, Object>();
		String resourceid 		       = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		List<BrschoolExamInfo> delList = new ArrayList<BrschoolExamInfo>();
		try {
			for (String id:resourceid.split(",")) {
				BrschoolExamInfo info = brschoolExamInfoService.get(id);
				if (1==info.getStatus()) {
					throw new WebException(info.getCourse().getCourseName()+"已通过审核，不允许删除！");
				}
				delList.add(info);
			}
			
			brschoolExamInfoService.batchDelete(delList);
			map.put("statusCode", 200);
			map.put("message", "删除成功！");	
			map.put("forward", request.getContextPath()+ "/edu3/teaching/brschool/examinfo-list.html");
		}catch (Exception e) {
			logger.error("删除面授考试课程出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 面授课程-审核
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	/*
	@RequestMapping("/edu3/teaching/brschool/examinfo-audit.html")
	public void brSchoolExamInfoAudit(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map	       = new HashMap<String, Object>();
		String resourceid 		       = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String flag                    = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		List<BrschoolExamInfo> delList = new ArrayList<BrschoolExamInfo>();
		User user                      = SpringSecurityHelper.getCurrentUser();
		Date curDate                   = new Date();
		StringBuffer message           = new StringBuffer();
		
		try {
			
			for (String id:resourceid.split(",")) {
				BrschoolExamInfo info = brschoolExamInfoService.get(id);
				if (1==info.getStatus() && "1".equals(flag)) {
					message.append(info.getCourse().getCourseName()+"已通过审核</br>");
					continue;
				}
				if (1==info.getStatus() && "0".equals(flag)) {
					message.append(info.getCourse().getCourseName()+"已通过审核,不允许设置为审核不通过</br>");
					continue;
				}
				info.setStatus(Integer.parseInt(flag));
				info.setAuditMan(user.getUsername());
				info.setAuditManId(user.getResourceid());
				info.setAuditTime(curDate);
				
				brschoolExamInfoService.update(info);
			}
			if(ExStringUtils.isNotEmpty(message.toString())){
				map.put("statusCode", 300);
				map.put("message", message.toString());	
				
			}else {
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("forward", request.getContextPath()+ "/edu3/teaching/brschool/examinfo-list.html");
			}
			
		}catch (Exception e) {
			logger.error("面授考试课程审核出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 面授课程-转入考试批次
	 * @param request
	 * @param model
	 * @param page
	 * @return
	 */
	/*
	@RequestMapping("/edu3/teaching/brschool/examinfo-into-examsub.html")
	public void brSchoolExamInfoIntoExamSub(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map	       = new HashMap<String, Object>();
		String resourceid 		       = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		User user                      = SpringSecurityHelper.getCurrentUser();
		Date curDate                   = new Date();
		StringBuffer message           = new StringBuffer();
		
		try {
			ExamSub examSub            = null;
			
			for (String id:resourceid.split(",")) {
				
				BrschoolExamInfo info  = brschoolExamInfoService.get(id);
				if (null==examSub) {
					examSub       	   = info.getExamSub();
				}
				Date examinputStartTime= examSub.getExaminputStartTime();
				if (curDate.getTime()>examinputStartTime.getTime()) {
					throw new WebException(examSub.getBatchName()+"，成绩录入已开始，不允许转入！");
					
				}
				List<ExamInfo> list     = examInfoService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("course",info.getCourse()),Restrictions.eq("examSub",info.getExamSub()));
				if (!list.isEmpty()) {
					message.append(examSub.getBatchName()+"，已存在考试课程《"+info.getCourse().getCourseName()+"》，不允许转入！");
					continue;
				}
				if (1!=info.getStatus()) {
					message.append("《"+info.getCourse().getCourseName()+"》，未审核不允许转入！");
					continue;
				}
				info.setChagneManId(user.getResourceid());
				info.setChangeMan(user.getUsername());
				info.setChangeTime(curDate);
				brschoolExamInfoService.update(info);
				
				ExamInfo examInfo       = new ExamInfo();
				examInfo.setCourse(info.getCourse());
				examInfo.setExamCourseType(1);
				examInfo.setExamStartTime(info.getStartTime());
				examInfo.setExamEndTime(info.getEndTime());
				examInfo.setExamSub(examSub);
				examInfo.setCourseScoreType(examSub.getCourseScoreType());
				examInfo.setStudyScorePer(examSub.getFacestudyScorePer());
				examInfo.setMemo("由"+info.getBrSchool().getUnitShortName()+"填入的面授考试课程");
				
				
				examSub.getExamInfo().add(examInfo);
				
			}
			if (null!=examSub) {
				examSubService.update(examSub);
			}
			
			if(ExStringUtils.isNotEmpty(message.toString())){
				map.put("statusCode", 300);
				map.put("message", message.toString());	
				
			}else {
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("forward", request.getContextPath()+ "/edu3/teaching/brschool/examinfo-list.html");
			}
			
		}catch (Exception e) {
			logger.error("面授考试课程转入考试批次出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 期末机考课程考试信息
	 * @param examSubId
	 * @param courseId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examinfo/list.html")
	public String listFinalExamInfo(String examSubId,String courseId,HttpServletRequest request,Page objPage,ModelMap model){	
		objPage.setOrderBy("examSub.yearInfo.firstYear desc,examSub.term desc,course.courseCode,resourceid");
		objPage.setOrder(Page.ASC);
		Map<String, Object> condition = new HashMap<String, Object>();
		
		//学院2016修改
		String finalexaminfo_branchSchoolId=ExStringUtils.trimToEmpty(request.getParameter("finalexaminfo_branchSchoolId"));
		String fillinDateStartStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateStartStr"));
		String fillinDateEndStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateEndStr"));
		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//校外学习中心人员
			model.addAttribute("isorunit","Y");
			finalexaminfo_branchSchoolId= user.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotBlank(finalexaminfo_branchSchoolId)){
			condition.put("finalexaminfo_branchSchoolId", finalexaminfo_branchSchoolId);
		}
		if(ExStringUtils.isNotEmpty(fillinDateStartStr)){
			condition.put("fillinDateStartStr", fillinDateStartStr);
			condition.put("fillinDateStart", ExDateUtils.convertToDate(fillinDateStartStr).getTime());
		}
		if(ExStringUtils.isNotEmpty(fillinDateEndStr)){
			condition.put("fillinDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(fillinDateEndStr), 1).getTime());
			condition.put("fillinDateEndStr", fillinDateEndStr);
		}	
		
		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(courseId)){
			condition.put("courseId", courseId);
		}
		condition.put("isMachineExam", Constants.BOOLEAN_YES);//机考
		condition.put("examCourseType", 3);//机考
		
		Page page = examInfoService.findExamInfoByCondition(condition, objPage);
		model.addAttribute("finalExamInfoPage",page);
		model.addAttribute("condition",condition);
		return "/edu3/teaching/examinfo/finalexaminfo-list";
	}
	/**
	 * 新增编辑期末考试时间
	 * @param examSubId
	 * @param courseId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examinfo/input.html")
	public String editFinalExamInfo(String resourceid,HttpServletRequest request,ModelMap model){	
		User user=SpringSecurityHelper.getCurrentUser();//学院2016修改
		if(null!=user){
			model.addAttribute("unid", user.getOrgUnit().getResourceid());
		}
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
			model.addAttribute("isorunit", "Y");
		}

		if(ExStringUtils.isNotBlank(resourceid)){
			ExamInfo finalExamInfo = examInfoService.get(resourceid);
			model.addAttribute("finalExamInfo", finalExamInfo);
		} else {
			ExamInfo finalExamInfo = new ExamInfo();
			finalExamInfo.setExamPaperType("random");
			model.addAttribute("finalExamInfo", finalExamInfo);
		}
		return "/edu3/teaching/examinfo/finalexaminfo-form";
	}
	/**
	 * 保存期末机考时间
	 * @param examInfo
	 * @param courseId
	 * @param examSubId
	 * @param courseExamRulesId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examinfo/save.html")
	public void saveFinalExamInfo(ExamInfo examInfo,String courseId,String examSubId,String courseExamRulesId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			User user=SpringSecurityHelper.getCurrentUser();//学院2016修改
			String courseExamPapersId = request.getParameter("courseExamPapersId");
			String ei_branchSchoolId=ExStringUtils.trimToEmpty(request.getParameter("ei_branchSchoolId"));
			examInfo.setIsMachineExam(Constants.BOOLEAN_YES);
			examInfo.setExamCourseType(3);
			examInfo.setExamType("0");
			if(ExStringUtils.isNotBlank(ei_branchSchoolId)){
				examInfo.setBrSchool(orgUnitService.get(ei_branchSchoolId));
			}else{
				examInfo.setBrSchool(user.getOrgUnit());
			}

			if(ExStringUtils.isNotBlank(courseId)){
				Course course = courseService.get(courseId);
				examInfo.setCourse(course);
			}
			if(ExStringUtils.isNotBlank(examSubId)){
				ExamSub examSub = examSubService.get(examSubId);
				examInfo.setExamSub(examSub);		
				examInfo.setStudyScorePer(examSub.getWrittenScorePer());
				examInfo.setCourseScoreType(examSub.getCourseScoreType());
			}
			if(ExStringUtils.isNotBlank(courseExamRulesId)){
				CourseExamRules rule = courseExamRulesService.get(courseExamRulesId);
				examInfo.setCourseExamRules(rule);
			}
			if(ExStringUtils.equals("fixed", examInfo.getExamPaperType()) && ExStringUtils.isBlank(courseExamPapersId)){//固定试卷
				throw new WebException("固定考试试卷，请选择一份试卷");
			}
			if(ExStringUtils.isNotBlank(courseExamPapersId)){
				CourseExamPapers courseExamPapers = courseExamPapersService.get(courseExamPapersId);
				examInfo.setCourseExamPapers(courseExamPapers);
			}
			if(!ExStringUtils.equals("fixed", examInfo.getExamPaperType())){
				examInfo.setCourseExamPapers(null);
			}
			if(ExStringUtils.isNotBlank(examInfo.getResourceid())){
				ExamInfo finalExamInfo = examInfoService.get(examInfo.getResourceid());
				Date today = new Date();
				if(today.after(finalExamInfo.getExamStartTime())){
					throw new WebException("考试已经开始，不能再修改数据");
				}
				if(examInfo.getExamStartTime()!=null && !ExDateUtils.isSameInstant(finalExamInfo.getExamStartTime(), examInfo.getExamStartTime()) && today.after(examInfo.getExamStartTime())){
					throw new WebException("考试开始时间必须晚于当前时间");
				}
				//ExBeanUtils.copyProperties(finalExamInfo, examInfo);
				finalExamInfo.setExamStartTime(examInfo.getExamStartTime());
				finalExamInfo.setExamEndTime(examInfo.getExamEndTime());
				finalExamInfo.setMachineExamName(examInfo.getMachineExamName());
				finalExamInfo.setIsShowScore(examInfo.getIsShowScore());
				finalExamInfo.setIsmixture(examInfo.getIsmixture());
				finalExamInfo.setMixtrueScorePer(examInfo.getMixtrueScorePer());
				finalExamInfo.setExamPaperType(examInfo.getExamPaperType());
				finalExamInfo.setCourseExamPapers(examInfo.getCourseExamPapers());
				finalExamInfo.setExamType(examInfo.getExamType());//考试类型
				
				finalExamInfo.getExamSub().getExamInfo().add(finalExamInfo);
				examInfoService.update(finalExamInfo);
			} else {
				examInfo.setShowOrder(examInfo.getExamSub().getExamInfo().size()+1L);
				examInfo.getExamSub().getExamInfo().add(examInfo);
				if("Y".equals(examInfo.getIsMachineExam())||"0".equals(examInfo.getExamType())){//学院2016修改
					examInfo.setStudyScorePer(100d);
					examInfo.setFacestudyScorePer(100d);
					examInfo.setMixtrueScorePer(100d);
				}

				examInfoService.save(examInfo);
				examInfoService.updateStulear(examInfo);
			}					
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_EXAM_FINALEXAM_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/examinfo/input.html?resourceid="+examInfo.getResourceid());
		}catch (Exception e) {
			logger.error("保存期末机考时间出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 选择成卷规则
	 * @param courseId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/framework/finalexaminfo/courseexamrule/list.html")
	public String listFinalExamInfoCourseExamRules(String courseId,String ids,String names,String vid,HttpServletRequest request,Page objPage,ModelMap model){	
		objPage.setOrderBy("versionNum");
		objPage.setOrder(Page.DESC);
		Map<String, Object> condition = new HashMap<String, Object>();

		if(ExStringUtils.isNotBlank(courseId)){
			condition.put("courseId", courseId);
			condition.put("isEnrolExam", Constants.BOOLEAN_NO);//非入学考
			condition.put("paperSourse", "final_exam");//期末考试题库	
			
			Page page = courseExamRulesService.findCourseExamRulesByCondition(condition, objPage);
			model.addAttribute("courseExamRulesPage",page);
			
			condition.put("ids", ExStringUtils.trimToEmpty(ids));
			condition.put("names", ExStringUtils.trimToEmpty(names));
			condition.put("vid", ExStringUtils.trimToEmpty(vid));
			model.addAttribute("condition",condition);
		}		
		return "/edu3/teaching/examinfo/courseExamRules-list";
	}
	/**
	 * 删除期末机考考试时间
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examinfo/remove.html")
	public void removeFinalExamInfo(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				examInfoService.batchCascadeDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
			}
		} catch (Exception e) {
			logger.error("删除期末机考考试时间出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 期末机考学生名单
	 * @param examInfoId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examinfo/student/list.html")
	public String viewStudentExerciseFinished(String examInfoId,HttpServletRequest request,Page objPage,ModelMap model) throws WebException {
		Map<String ,Object> condition = new HashMap<String, Object>();		
		String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
		condition.put("currentIndex", currentIndex);
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String classes= ExStringUtils.trimToEmpty(request.getParameter("classes"));
		String leType=ExStringUtils.trimToEmpty(request.getParameter("leType"));
		//学院2016修改

		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotBlank(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotBlank(leType)){//学院2016修改
			condition.put("leType", leType);
		}
		if(ExStringUtils.isNotBlank(major)){
			condition.put("major", major);
		}
		if(ExStringUtils.isNotBlank(classic)){
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotBlank(classes)){//学院2016修改
			condition.put("classes", classes);
		}
		if(ExStringUtils.isNotBlank(gradeid)){
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(name)){
			condition.put("name", name);
		}
		if(ExStringUtils.isNotBlank(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(examInfoId)){
			objPage.setOrderBy("studentInfo.studyNo");
			objPage.setOrder(Page.ASC);			
			
			ExamInfo examInfo = examInfoService.get(examInfoId);
			if("0".equals(currentIndex)){				
				condition.put("examInfoId", examInfoId);
				condition.put("queryHQL", " and (plan.examInfo.isMachineExam='Y' or plan.examInfo.isMachineExam is null) and plan.examInfo.examCourseType=3 and plan.examInfo.isDeleted=0 ");
			} else {			
//				condition.put("status", 2);//预约考试	// 2016-10-12 ,zik改
				String examSubId = examInfo.getExamSub().getResourceid();
//				condition.put("examSubId", examSubId);
				String year=examInfo.getExamSub().getYearInfo().getFirstYear().toString();
				String ispaike=year+"_0"+examInfo.getExamSub().getTerm();
				condition.put("ispaike", ispaike);
				condition.put("yearInfo", examInfo.getExamSub().getYearInfo().getResourceid());
				condition.put("term", examInfo.getExamSub().getTerm());
				//学院2016修改
				String _courseId = examInfo.getCourse().getResourceid();
				condition.put("courseId", _courseId);
//				condition.put("queryHQL", " and (plan.examInfo.isMachineExam='N' or plan.examInfo.isMachineExam is null) ");
				condition.put("queryHQL", " and plan.examInfo.resourceid is null and exists (from "+ExamInfo.class.getSimpleName()+" ei where ei.examSub.resourceid='"+examSubId+"' and ei.isDeleted=0 "
						+ "and (ei.isMachineExam='Y' or ei.isMachineExam is null) and ei.examCourseType=3 and ei.course.resourceid='"+_courseId+"' and ei.brSchool.resourceid='"+branchSchool+"') ");
				condition.put("selType", "unComputerTest");
			}	
			if(!"N".equals(examInfo.getExamSub().getExamType())){// 2016-10-12 ,zik改
				condition.put("currentIndex", currentIndex);
				Page page = examInfoService.getstudentmakeuplist(examInfo, objPage,condition);
				model.addAttribute("finalExamStudentList", page);
				model.addAttribute("ismuk","Y");
			}else{
				Page page = studentLearnPlanService.findStudentLearnPlanByCondition(condition,objPage);	
				model.addAttribute("finalExamStudentList", page);
			}
			
			condition.put("examInfoId", examInfoId);
			model.addAttribute("examInfo",examInfo);
		}
		model.addAttribute("condition", condition);	
		model.addAttribute("classesSelect5",graduationQualifService.getGradeToMajorToClasses1(gradeid,major,classes,branchSchool,"searchExamResult_classesid1","classes",classic));//学院2016修改
		return "/edu3/teaching/examinfo/student-list";
	}
	/**
	 *安排机考考试计划
	 * @param resourceid
	 * @param examInfoId
	 * @param arrangeType
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/examinfo/student/arrange.html")
	public void arrangeFinalExamInfo(String resourceid,String examInfoId,String arrangeType,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {	
			if(ExStringUtils.isBlank(resourceid)){ //2016-10-12修改
				Map<String ,Object> condition = new HashMap<String, Object>();
				String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
				condition.put("currentIndex", currentIndex);
				String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
				String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
				String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));
				String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
				String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
				String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
				String classes= ExStringUtils.trimToEmpty(request.getParameter("classes"));
				String leType=ExStringUtils.trimToEmpty(request.getParameter("leType"));
			
				User user = SpringSecurityHelper.getCurrentUser();
				if(ExStringUtils.isEmpty(branchSchool)
						&&CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){//如果为校外学习中心人员
					branchSchool = user.getOrgUnit().getResourceid();
				}
				
				if(ExStringUtils.isNotBlank(branchSchool)){
					condition.put("branchSchool", branchSchool);
				}
				if(ExStringUtils.isNotBlank(leType)){//学院2016修改
					condition.put("leType", leType);
				}
				if(ExStringUtils.isNotBlank(major)){
					condition.put("major", major);
				}
				if(ExStringUtils.isNotBlank(classes)){//学院2016修改
					condition.put("classes", classes);
				}
				if(ExStringUtils.isNotBlank(classic)){
					condition.put("classic", classic);
				}
				if(ExStringUtils.isNotBlank(gradeid)){
					condition.put("gradeid", gradeid);
				}
				if(ExStringUtils.isNotBlank(name)){
					condition.put("name", name);
				}
				if(ExStringUtils.isNotBlank(studyNo)){
					condition.put("studyNo", studyNo);
				}
				if(ExStringUtils.isNotBlank(examInfoId)){
					ExamInfo examInfo = examInfoService.get(examInfoId);
					if("0".equals(currentIndex)){				
						condition.put("examInfoId", examInfoId);
						condition.put("queryHQL", " and (plan.examInfo.isMachineExam='Y' or plan.examInfo.isMachineExam is null) and plan.examInfo.examCourseType=3 and plan.examInfo.isDeleted=0 ");
					} else {			
						String examSubId = examInfo.getExamSub().getResourceid();
						String year=examInfo.getExamSub().getYearInfo().getFirstYear().toString();
						String ispaike=year+"_0"+examInfo.getExamSub().getTerm();
						condition.put("ispaike", ispaike);
						condition.put("yearInfo", examInfo.getExamSub().getYearInfo().getResourceid());
						condition.put("term", examInfo.getExamSub().getTerm());
						//学院2016修改
						String _courseId = examInfo.getCourse().getResourceid();
						condition.put("courseId", _courseId);
//						condition.put("queryHQL", " and plan.examInfo.resourceid is null and exists (from "+ExamInfo.class.getSimpleName()+" ei where ei.examSub.resourceid='"+examSubId+"' and ei.isDeleted=0 "
//								+ "and (ei.isMachineExam='Y' or ei.isMachineExam is null) and ei.examCourseType=3 and ei.course.resourceid='"+_courseId+"' and ei.brSchool.resourceid='"+branchSchool+"') ");
						condition.put("queryHQL", " and plan.examInfo.resourceid is null and exists (from "+ExamInfo.class.getSimpleName()+" ei where ei.examSub.resourceid='"+examSubId+"' and ei.isDeleted=0 "
								+ "and (ei.isMachineExam='Y' or ei.isMachineExam is null) and ei.examCourseType=3 and ei.course.resourceid='"+_courseId+"' and ei.brSchool.resourceid='"+branchSchool+"') "
								+ "and exists( from "+TeachingPlanCourseStatus.class.getSimpleName()+" tpct where tpct.isDeleted=0 and tpct.teachingGuidePlan.grade.resourceid=plan.studentInfo.grade.resourceid and tpct.teachingGuidePlan.isDeleted=0 "
		                        +" and tpct.teachingGuidePlan.teachingPlan.resourceid=plan.studentInfo.teachingPlan.resourceid  and tpct.teachingPlanCourse.course.resourceid='"+_courseId+"' and tpct.schoolIds='"+branchSchool+"' and tpct.isOpen='Y' and tpct.teachingGuidePlan.ispublished='Y' ) ");
						condition.put("selType", "unComputerTest");
					}	
					Page page1=new Page();
					page1.setPageSize(10000);
					Page page=new Page();
					 List<StudentMakeupList> mkList = null;
					 List<StudentLearnPlan> planList =  null;
					 if(!"N".equals(examInfo.getExamSub().getExamType())){// 2016-10-12 ,zik改
							condition.put("currentIndex", currentIndex);
							 page = examInfoService.getstudentmakeuplist(examInfo, page1,condition);
							 mkList=page.getResult();
						}else{
							page = studentLearnPlanService.findStudentLearnPlanByCondition(condition,page1);
							planList=page.getResult();
						}
					examInfoService.operateFinalExamInfo(planList, mkList, examInfoId, arrangeType);
					
				}
			}else{
				examInfoService.arrangeFinalExamInfo(resourceid.split("\\,"),examInfoId,arrangeType);
			}
					
			map.put("statusCode", 200);
			map.put("message", "1".equals(arrangeType)?"安排成功！":"取消安排成功！");		
		} catch (Exception e) {
			logger.error("安排期末机考学生出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 选择考试试卷
	 * @param courseId
	 * @param ids
	 * @param names
	 * @param vid
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/framework/finalexaminfo/courseexampapers/list.html")
	public String listFinalExamInfoCourseExamPapers(String courseId,String ids,String names,String vid,HttpServletRequest request,Page objPage,ModelMap model){	
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.DESC);
		Map<String, Object> condition = new HashMap<String, Object>();

		if(ExStringUtils.isNotBlank(courseId)){
			condition.put("courseId", courseId);			
			condition.put("paperType", "final_exam");//期末考试
			condition.put("isOpened", Constants.BOOLEAN_YES);
			
			Page page = courseExamPapersService.findCourseExamPapersByCondition(condition, objPage);
			model.addAttribute("courseExamPapersPage",page);
			
			condition.put("ids", ExStringUtils.trimToEmpty(ids));
			condition.put("names", ExStringUtils.trimToEmpty(names));
			condition.put("vid", ExStringUtils.trimToEmpty(vid));
			model.addAttribute("condition",condition);
		}		
		return "/edu3/teaching/examinfo/courseExamPapers-list";
	}
	
	//学院2016修改
	@RequestMapping("/edu3/teaching/examinfo/count/list.html")
	public String getExamConutCl(HttpServletRequest equest,Page objPage,ModelMap model){
		model.addAttribute("examcontlist",examInfoService.getExamCount(objPage)) ;
		return "/edu3/teaching/examinfo/exam-count-list";
	}

}
