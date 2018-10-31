package com.hnjk.edu.arrange.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.data.time.TimePeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.beans.DatePeriod;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.ArrangeTemplate;
import com.hnjk.edu.arrange.model.CalendarEvent;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.model.TeachCourseDetail;
import com.hnjk.edu.arrange.model.TeachingWillingness;
import com.hnjk.edu.arrange.service.IArrangeTemplateService;
import com.hnjk.edu.arrange.service.ISchoolCalendarService;
import com.hnjk.edu.arrange.service.ITeachCourseClassesService;
import com.hnjk.edu.arrange.service.ITeachCourseDetailService;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.arrange.service.ITeachingWillingnessService;
import com.hnjk.edu.basedata.model.Classroom;
import com.hnjk.edu.basedata.service.IClassroomService;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimePeriodService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

@Controller
public class TeachCourseDetailController extends BaseSupportController{

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("classroomService")
	private IClassroomService classroomService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("teachCourseService")
	private ITeachCourseService teachCourseService;
	
	@Autowired
	@Qualifier("teachCourseDetailService")
	private ITeachCourseDetailService teachCourseDetailService;
	
	@Autowired
	@Qualifier("teachCourseClassesService")
	private ITeachCourseClassesService teachCourseClassesService;
	
	@Autowired
	@Qualifier("teachingWillingnessService")
	private ITeachingWillingnessService teachingWillingnessService;
	
	@Autowired
	@Qualifier("arrangeTemplateService")
	private IArrangeTemplateService arrangeTemplateService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimePeriodService")
	private ITeachingPlanCourseTimePeriodService teachingPlanCourseTimePeriodService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("schoolCalendarService")
	private ISchoolCalendarService schoolCalendarService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	/**
	 * 排课详细页面/排课
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping(value={"/edu3/arrange/arrangecoursedetail/list.html","/edu3/arrange/arrangeCourse/arrange.html"})
	public String arrangeCourseDetailList(HttpServletRequest request,ModelMap model,Page objPage){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String teachCourseid	=		ExStringUtils.trim(request.getParameter("teachCourseid"));//教学班
		String isArrange 		=		ExStringUtils.trim(request.getParameter("isArrange"));//判读是否从排课页面打开
		String ischeck			= 		request.getParameter("ischeck"); 
		TeachCourse teachCourse = null;
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			condition.put("brSchoolid", user.getOrgUnit().getResourceid());
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		if(ExStringUtils.isNotBlank(isArrange)){
			if("Y".equals(isArrange) && ExStringUtils.isNotBlank(teachCourseid)){
				condition.put("teachCourseid", teachCourseid);
				teachCourse = teachCourseService.get(teachCourseid);
				Integer studyHour = 0;
				for (TeachCourseDetail courseDetail : teachCourse.getTeachCourseDetails()) {
					studyHour += courseDetail.getStudyHour()!=null?courseDetail.getStudyHour():0;
				}
				teachCourse.setArrangeStudyHour(studyHour);
				String hql = " from "+TeachCourseClasses.class.getSimpleName()+" where isDeleted=0 and teachCourse.resourceid=?";
				List<TeachCourseClasses> tcClassesList = teachCourseClassesService.findByHql(hql, teachCourse.getResourceid());
				String scoreTeacherNames = "";
				for (TeachCourseClasses tcClass : tcClassesList) {
					if(tcClass.getRecordScorer()!=null && !scoreTeacherNames .contains(tcClass.getRecordScorer().getCnName())){
						scoreTeacherNames += tcClass.getRecordScorer().getCnName()+"，";
					}
				}
				if(ExStringUtils.isNotBlank(scoreTeacherNames)) scoreTeacherNames = scoreTeacherNames.substring(0, scoreTeacherNames.length()-1);
				teachCourse.setRecordScorerName(scoreTeacherNames);
				model.addAttribute("teachCourse",teachCourseService.get(teachCourseid));
			}
			model.addAttribute("isArrange", isArrange);
		}
		Page aCourseDetailPage = teachCourseDetailService.findArrangeCourseDetailsByCondition(condition,objPage);
			
		model.addAttribute("page", aCourseDetailPage);
		model.addAttribute("condition", condition);
		model.addAttribute("ischeck", ischeck);
		return "/edu3/arrange/arrangeCourseDetail/arrangeCourseDetail-list";
	}
	
	/**
	 * 编辑排课详情
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException 
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/edit.html")
	public String editCourseDetail(HttpServletRequest request,ModelMap model,Page objPage) throws ParseException {
		String courseDetailid	=		ExStringUtils.trim(request.getParameter("courseDetailid"));//排课详情
		String brSchoolid 		= 		ExStringUtils.trim(request.getParameter("brSchoolid"));//教学点
		String teachCourseid 	= 		ExStringUtils.trim(request.getParameter("teachCourseid"));//教学班
		String templateid 		= 		ExStringUtils.trim(request.getParameter("templateid"));//排课模版
		String[] days     		=		request.getParameterValues("days");//星期
		String[] weeks 		    =		request.getParameterValues("weeks");//周数
		String timePeriod       =		ExStringUtils.trim(request.getParameter("timePeriod"));//上课时间
		String classroomType	= 		ExStringUtils.trimToEmpty(request.getParameter("classroomType"));//课室类型
		String classroomid		= 		ExStringUtils.trimToEmpty(request.getParameter("classroomid"));//课室
		String weekOrDate 		= 		ExStringUtils.trimToEmpty(request.getParameter("weekOrDate"));//周期/日期
		String startDate		= 		request.getParameter("startDate");//开始时间
		String endDate			= 		request.getParameter("endDate");//结束时间
		String teacherids 		= 		ExStringUtils.trimToEmpty(request.getParameter("teacherids"));//主讲老师id
		String teacherNames 	= 		ExStringUtils.trimToEmpty(request.getParameter("teacherNames"));//老师名称
		String isArrange		= 		request.getParameter("isArrange");//是否是排课
		String isAdd			= 		request.getParameter("isAdd");//是否新增
		String ischeck			=		request.getParameter("ischeck");//是否冲突检查
		String selector			= 		request.getParameter("selector");//是否从选择老师页面返回
		TeachCourseDetail courseDetail = null;
		TeachCourse teachCourse = null;
		int isBrschool = 0;
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
			isBrschool = 1;
			brSchoolid = user.getUnitId();
		}
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		//查询上课时间
		String[] settingList = null;
		if(ExStringUtils.isNotBlank(courseDetailid) && !"undefined".equals(courseDetailid)){ //-----编辑
			courseDetail = teachCourseDetailService.get(courseDetailid);
			teachCourse = courseDetail.getTeachCourse();
			brSchoolid = courseDetail.getTeachCourse().getUnit().getResourceid();
			if(!"Y".equals(selector)){
				weekOrDate = courseDetail.getDateType().toString();
				days = courseDetail.getDays()!=null?courseDetail.getDays().split(","):null;
				weeks = courseDetail.getWeeks()!=null?courseDetail.getWeeks().split(","):null;
				startDate = courseDetail.getStartdate()!=null?courseDetail.getStartdate().toString():"";
				endDate = courseDetail.getEnddate()!=null?courseDetail.getEnddate().toString():"";
				teacherids = courseDetail.getTeacher().getResourceid();
				teacherNames = courseDetail.getTeacher().getCnName();
				classroomType = courseDetail.getClassroom().getClassroomType();
				classroomid = courseDetail.getClassroom().getResourceid();
				if(ExStringUtils.isNotBlank(courseDetail.getTimePeriodids())){
					settingList = courseDetail.getTimePeriodids().split(",");
				}
			}
		}else{ //----------------------------------------新增
			courseDetail = new TeachCourseDetail();
			if(ExStringUtils.isNotBlank(teachCourseid)){
				teachCourse = teachCourseService.get(teachCourseid);
				brSchoolid = teachCourse.getUnit().getResourceid();
			}
			if("Y".equals(isAdd)) {
				//教学点选项
				String unitOption = orgUnitService.constructOptions(null, brSchoolid, isBrschool);
				model.addAttribute("unitOption",unitOption);
			}
		}
		if(timePeriod!=null) {
			settingList = timePeriod.split(",");
		}
		if(ExStringUtils.isNotEmpty(brSchoolid)) condition.put("brSchoolid", brSchoolid);
		//查询可选上课时间
		String timePeriodOption = teachingPlanCourseTimePeriodService.constructOptions(condition,settingList);
		
		if("0".equals(weekOrDate)){//周期
			model.addAttribute("weeks", ExStringUtils.toString(weeks));
		}else if ("1".equals(weekOrDate)) {//日期
			if(ExStringUtils.isNotBlank(startDate)){
				Date dStartDate = ExDateUtils.parseDate(startDate, ExDateUtils.PATTREN_DATE_TIME);
				model.addAttribute("startDate",dStartDate);
			}
			if(ExStringUtils.isNotBlank(endDate)){
				Date dEndDate = ExDateUtils.parseDate(endDate, ExDateUtils.PATTREN_DATE_TIME);
				model.addAttribute("endDate",dEndDate);
			}
		}
		if(ExStringUtils.isNotBlank(classroomType)){
			condition.put("classroomType", classroomType);
		}
		condition.put("isUseCourse", "Y");
		String  classroomOption = classroomService.constructOptions(condition,classroomid);
		model.addAttribute("classroomOption",classroomOption);
		
		//意愿信息
		String hql = " from "+TeachingWillingness.class.getSimpleName()+" where isDeleted=0";
		Map<String, Object> values = new HashMap<String, Object>();
		if(teachCourse!=null) {
			hql += "and teachCourse.resourceid=:resourceid";
			values.put("resourceid", teachCourse.getResourceid());
		}
		List<TeachingWillingness> willingnessList = teachingWillingnessService.findByHql(hql, values);
		if(willingnessList!=null && willingnessList.size()>0){
			model.addAttribute("willingnessList", willingnessList);
			if(ExStringUtils.isBlank(teacherids)){
				teacherids = willingnessList.get(0).getProposer().getResourceid();
			}
			if(ExStringUtils.isBlank(teacherNames)){
				teacherNames = willingnessList.get(0).getProposer().getCnName();
			}
		}
		model.addAttribute("isArrange",isArrange);
		model.addAttribute("isAdd",isAdd);
		model.addAttribute("ischeck",ischeck);
		model.addAttribute("brSchoolid",brSchoolid);
		model.addAttribute("templateid", templateid);
		model.addAttribute("days", ExStringUtils.toString(days));
		model.addAttribute("weekOrDate", weekOrDate);
		model.addAttribute("classroomType", classroomType);
		model.addAttribute("classroomid", classroomid);
		model.addAttribute("courseDetail",courseDetail);
		model.addAttribute("timePeriodOption", timePeriodOption);
		model.addAttribute("teacherids", teacherids);
		model.addAttribute("teacherNames", teacherNames);
		if(teachCourse!=null) model.addAttribute("teachCourseid", teachCourse.getResourceid());
		return "edu3/arrange/arrangeCourseDetail/arrangeCourseDetail-form";
	}
	
	/**
	 * 选择授课老师
	 * @param request
	 * @param response
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/selectteacher.html")
	public String selectTeacher(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model){
		objPage.setOrderBy("orgUnit.unitCode ");
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String brSchoolid		=		ExStringUtils.trim(request.getParameter("brSchoolid"));//学习中心
		String templateid 		= 		ExStringUtils.trim(request.getParameter("templateid"));//排课模版
		String courseDetailid 	= 		ExStringUtils.trimToEmpty(request.getParameter("courseDetailid"));//排课详情id
		String teachCourseid 	= 		ExStringUtils.trimToEmpty(request.getParameter("teachCourseid"));//教学班id
		String teachtype 		= 		ExStringUtils.trimToEmpty(request.getParameter("teachtype"));
		String[] days     		=		request.getParameterValues("days");//星期
		String[] weeks 		    =		request.getParameterValues("weeks");//周数
		String[] timePeriod     =		request.getParameterValues("timePeriod");
		String classroomType	= 		ExStringUtils.trimToEmpty(request.getParameter("classroomType"));//课室类型
		String classroomid		= 		ExStringUtils.trimToEmpty(request.getParameter("classroomid"));//课室
		String weekOrDate 		= 		ExStringUtils.trimToEmpty(request.getParameter("weekOrDate"));//周期/日期
		String isArrange 		= 		ExStringUtils.trimToEmpty(request.getParameter("isArrange"));//是否是排课
		String isAdd			= 		ExStringUtils.trimToEmpty(request.getParameter("isAdd"));//是否新增
		String startDate		= 		request.getParameter("startDate");//开始时间
		String endDate			= 		request.getParameter("endDate");//结束时间
		String teachClassesid	= 		request.getParameter("teachClassesid");//教学班级
		String ischeck			=		request.getParameter("ischeck");//是否冲突检查：Y/N
		String cnName 			=		ExStringUtils.trimToEmpty(request.getParameter("cnName"));//教师名称
		String teacherCode 		=		ExStringUtils.trimToEmpty(request.getParameter("teacherCode"));//教师编号
		if(ExStringUtils.isNotEmpty(brSchoolid)){
			condition.put("unitId", brSchoolid);
			condition.put("brSchoolid", brSchoolid);
		}
		condition.put("teacherType", "'ROLE_LINE','ROLE_TEACHER_DUTY','ROLE_TEACHER'");//教师类型：在线老师，主讲老师，课程负责老师
		condition.put("isDeleted", 0);
		if(ExStringUtils.isNotBlank(cnName)) condition.put("cnName",cnName);
		if(ExStringUtils.isNotBlank(teacherCode)) condition.put("teacherCode",teacherCode);
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);		
		condition.put("selectedTeachers", ExStringUtils.trimToEmpty(request.getParameter("teacherids")));
		condition.put("selectedNames", ExStringUtils.trimToEmpty(request.getParameter("teacherNames")));
		if("0".equals(weekOrDate)){
			model.addAttribute("weeks", ExStringUtils.toString(weeks));
		}else if ("1".equals(weekOrDate)) {
			model.addAttribute("startDate", startDate);
			model.addAttribute("endDate", endDate);
		}
		model.addAttribute("teacherlist", page);
		model.addAttribute("condition", condition);
		model.addAttribute("brSchoolid", brSchoolid);
		model.addAttribute("templateid", templateid);
		model.addAttribute("classroomType", classroomType);
		model.addAttribute("classroomid", classroomid);
		model.addAttribute("courseDetailid", courseDetailid);
		model.addAttribute("teachCourseid", teachCourseid);
		model.addAttribute("teachtype", teachtype);
		model.addAttribute("days", ExStringUtils.toString(days));
		model.addAttribute("timePeriod", ExStringUtils.toString(timePeriod));
		model.addAttribute("classroomid", classroomid);
		model.addAttribute("weekOrDate", weekOrDate);
		model.addAttribute("isArrange", isArrange);
		model.addAttribute("isAdd", isAdd);
		model.addAttribute("ischeck", ischeck);
		model.addAttribute("teachClassesid", teachClassesid);
		return "/edu3/arrange/arrangeCourseDetail/courseDetail-selector";
	}
	/**
	 * 更新form表单
	 * @param brSchoolid
	 * @param templateid
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/update.html")
	public void updateArrangeDetailForm(HttpServletRequest request , HttpServletResponse response){
		String brSchoolid 		= 		request.getParameter("brSchoolid");//教学点
		String teachCourseid 	= 		request.getParameter("teachCourseid");//教学班
		String courseDetailid	=		request.getParameter("courseDetailid");//排课详情
		String templateid		= 		request.getParameter("templateid");//排课模版
		String teacherid 		= 		request.getParameter("teacherid");//老师
		String teacherNames 	= 		request.getParameter("teacherNames");//老师名称
		String[] days     		=		request.getParameterValues("days");//星期
		String[] weeks 		    =		request.getParameterValues("weeks");//周数
		String weekOrDate 		= 		ExStringUtils.trimToEmpty(request.getParameter("weekOrDate"));//周期/日期
		String timePeriod       =		ExStringUtils.trim(request.getParameter("timePeriod"));//上课时间
		String classroomType 	= 		request.getParameter("classroomType");//课室类型
		//String buildingid 		= 		request.getParameter("buildingid");//教学楼
		String classroomid 		= 		request.getParameter("classroomid");//课室
		String openTerm 		= 		request.getParameter("openTerm");//上课学期
		String isUpdate 		=		request.getParameter("isUpdate");
		Map<String, Object> map = 		new HashMap<String, Object>();
		int statusCode = 200;
		int maxWeek = 24;
		try {
			String classStyle = "' class='conflict'>";//<option value='resourceid'> 中间加上样式
			Map<String, Object> condition = new HashMap<String, Object>();
			SchoolCalendar schoolCalendar = null;
			ArrangeTemplate template = null;
			if(ExStringUtils.isNotBlank(brSchoolid) && !"undefined".equals(brSchoolid)){
				condition.put("brSchoolid", brSchoolid);
			}
			//院历
			if(ExStringUtils.isNotBlank(openTerm)) condition.put("openTerm", openTerm);
			List<SchoolCalendar> sCalendars = schoolCalendarService.findSCalendarListByCondition(condition);
			if(sCalendars!=null && sCalendars.size()>0){
				schoolCalendar = sCalendars.get(0);
				maxWeek = schoolCalendar.getWeeks();
				//condition.put("schoolCalendarid", schoolCalendar.getResourceid());
			}
			map.put("maxWeek",maxWeek);//最大周数
			String templateOption = arrangeTemplateService.constructOptions(condition, templateid);
			String teachCourseOption = teachCourseService.constructOptions(condition, teachCourseid);
			//if(days!=null && weeks!=null && ExStringUtils.isNotBlank(teachCourseid) && ExStringUtils.isNotBlank(teacherid)){
				
			//}
			//上课时间多选框
			String[] settingList = null;
			if(ExStringUtils.isNotBlank(templateid)){
				template = arrangeTemplateService.get(templateid);
			}
			
			if("N".equals(isUpdate)){
				map.put("days",ExStringUtils.toString(days));
				map.put("weeks",ExStringUtils.toString(weeks));
				map.put("weekOrDate",weekOrDate);
				if(ExStringUtils.isNotBlank(timePeriod)) {
					settingList = timePeriod.split(",");
				}
			}else {
				//星期，周数/日期
				if(template!=null){
					if(ExStringUtils.isNotBlank(template.getDays())) map.put("days", template.getDays());//星期
					if(ExStringUtils.isNotBlank(template.getWeeks())) map.put("weeks", template.getWeeks());//周数
					if(ExStringUtils.isNotBlank(template.getTimePeriodids())) settingList = template.getTimePeriodids().split(",");//时间段
					map.put("startDate",template.getStartDate());
					map.put("endDate",template.getEndDate());
					map.put("weekOrDate", template.getDateType());
				}
				//意愿信息
				if(ExStringUtils.isNotBlank(teachCourseid) && !"undefined".equals(teachCourseid)){//意愿信息
					List<TeachingWillingness> willingnessList = teachingWillingnessService.findByHql(" from "+TeachingWillingness.class.getSimpleName()
							+" where isDeleted=0 and teachCourse.resourceid=?",teachCourseid);
					String willingnessInfo = "<tr>"+
								"<td style='text-align: center;font-weight: bold;width: 10%'>申请教师</td>"+
								"<td style='text-align: center;font-weight: bold;width: 10%'>申请课室类型</td>"+
								"<td style='text-align: center;font-weight: bold;width: 25%'>意愿上课时间</td>"+
								"<td style='text-align: center;font-weight: bold;width: 25%'>星期</td>"+
								"<td style='text-align: center;font-weight: bold;width: 25%'>申请信息</td>"+
							"</tr>";
					for (TeachingWillingness will : willingnessList) {
						willingnessInfo += "<tr><td style='text-align: center;'>"+will.getProposer().getCnName()+"</td>";
						willingnessInfo += "<td style='text-align: center;'>"+dictionaryService.dictCode2Val("CodeClassRoomStyle", will.getClassroomType())+"</td>";
						willingnessInfo += "<td style='text-align: center;'>"+(will.getTimePeriodNames()!=null?will.getTimePeriodNames():"")+"</td>";
						willingnessInfo += "<td style='text-align: center;'>"+(will.getDaysName()!=null?will.getDaysName():"")+"</td>";
						willingnessInfo += "<td style='text-align: center;'>"+(will.getInfo()!=null?will.getInfo():"")+"</td></tr>";
						teacherNames = will.getProposer().getCnName();
						teacherid =  will.getProposer().getResourceid();
					}
					if(ExStringUtils.isBlank(classroomType) && willingnessList.size()>0){
						classroomType = willingnessList.get(0).getClassroomType();
					}
					map.put("willingnessInfo", willingnessInfo);
				}
			}
			//上课时间段
			String timePeriodOption = teachingPlanCourseTimePeriodService.constructOptions(condition,settingList);
			//课室
			if(ExStringUtils.isNotBlank(classroomType)){
				condition.put("classroomType", classroomType);
				map.put("classroomType", classroomType);
			}
			condition.put("isUseCourse", "Y");
			String  classroomOption = classroomService.constructOptions(condition,classroomid);
			
			//冲突提示
			if(weeks!=null && days!=null && (ExStringUtils.isNotBlank(timePeriod) || ExStringUtils.isNotBlank(teacherid) || ExStringUtils.isNotBlank(classroomid))){
				Map<String, Object> condition1 = new HashMap<String, Object>();
				if(ExStringUtils.isNotBlank(teachCourseid)) condition1.put("brSchoolid", brSchoolid);
				if(ExStringUtils.isNotBlank(teachCourseid)) condition1.put("teachCourseid", teachCourseid);
				if(ExStringUtils.isNotBlank(teacherid)) condition1.put("teacherid", teacherid);
				if(ExStringUtils.isNotBlank(openTerm)) condition1.put("openTerm", openTerm);
				if(ExStringUtils.isNotBlank(classroomid)) condition1.put("classroomid", classroomid);
				if(ExStringUtils.isNotBlank(courseDetailid)) condition1.put("notdetailid", courseDetailid);
				condition1.put("ischeck", "Y");
				List<Map<String, Object>> conflictMapList = teachCourseDetailService.findCourseDetailConflictMapByCondition(condition1);
				if(conflictMapList!=null && conflictMapList.size()>0){
					for (Map<String, Object> map2 : conflictMapList) {
						boolean isConflict = false;
						String _type = map2.get("type").toString();
						String _days = map2.get("days").toString();
						String _weeks = map2.get("weeks").toString();
						String _timeperiodids = map2.get("timeperiodids").toString();
						String _name = map2.get("name").toString();
						String _conflict = map2.get("conflict").toString();
						String _teachcourseid = map2.get("teachcourseid").toString();
						if(ExStringUtils.isContainsStr(ExStringUtils.toString(days),_days)){
							isConflict = true;
						}
						
						if(isConflict){//周期
							isConflict = false;
							for (String _week : Arrays.asList(_weeks.split(","))) {
								if(ExStringUtils.isContainsStr(ExStringUtils.toString(weeks),_week)){
									isConflict = true;
									break;
								}
							}
						}
						
						if(isConflict){//上课时间段
							isConflict = false;
							List<String> timePeriodList = Arrays.asList(timePeriod.split(","));
							//标注不可选的上课时间段
							for(String time: Arrays.asList(_timeperiodids.split(","))){
								if(_conflict.equals(teacherid) || _conflict.equals(classroomid) || _teachcourseid.equals(teachCourseid)) timePeriodOption = ExStringUtils.replace(timePeriodOption,time+"'>", time+classStyle);
								if(timePeriodList.contains(time)) isConflict = true;
							}
						}
						if(isConflict){
							if("teacher".equals(_type)){//标注老师冲突
								teacherNames = ExStringUtils.replace(teacherNames,_name, "！"+_name).replace("！！", "！");;
							}else if ("room".endsWith(_type)) {//标注课室冲突
								classroomOption = ExStringUtils.replace(classroomOption,_conflict+"'>", _conflict+classStyle);
							}
						}
					}
				}
			}
			map.put("templateOption",templateOption);
			map.put("teacherid",teacherid);
			map.put("teacherNames",teacherNames);
			map.put("teachCourseOption",teachCourseOption);
			map.put("timePeriodOption",timePeriodOption);
			map.put("classroomOption",classroomOption);
		} catch (Exception e) {
			logger.error("操作出错：{}",e.fillInStackTrace());
			statusCode = 300;
			map.put("message", "刷新失败！");
		}
		map.put("statusCode", statusCode);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 获取可排课时间表
	 * @param teachCourseid
	 * @param teacherid
	 * @param classroomType
	 * @param classroomid
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/arrangeTimePeriod.html")
	public String viewArrangeTimePeriod(String brSchoolid,String teachCourseid,String teacherid,String classroomid,String optType,ModelMap model,Page objPage){
		objPage.setOrderBy("brSchool.unitCode,timePeriod,startTime,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String, Object> condition = new HashMap<String, Object>();
		
		if(ExStringUtils.isNotBlank(brSchoolid)) condition.put("brSchoolid", brSchoolid);
		//时间段第几节
		objPage.setPageSize(100);
		List<TeachingPlanCourseTimePeriod> timeperiodList = teachingPlanCourseTimePeriodService.findTeachingPlanCourseTimePeriodByCondition(condition, objPage).getResult();
	
		if(ExStringUtils.isNotBlank(teacherid)) condition.put("teacherid", teacherid);
		if(ExStringUtils.isNotBlank(classroomid)) condition.put("classroomid", classroomid);
		if(ExStringUtils.isNotBlank(teachCourseid)) condition.put("teachCourseid", teachCourseid);
		List<Map<String, Object>> conflictMapList = teachCourseDetailService.findCourseDetailConflictMapByCondition(condition);
		List<List<Map<String, Object>>> timeList = new ArrayList<List<Map<String,Object>>>();
		for (int i = 0; i < 3; i++) {//老师，上课时间，课室 可排课时间表
			List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
			for(int j=0;j<7;j++){//初始化mapList星期一至星期天
				Map<String, Object> map = new HashMap<String, Object>();
				for (TeachingPlanCourseTimePeriod timePeriod : timeperiodList) {
					map.put(timePeriod.getResourceid(), "");
				}
				mapList.add(map);
			}
			timeList.add(mapList);
		}
		
		if(conflictMapList!=null){
			//根据上课学期获取院历的教学周
			SchoolCalendar calendar = null;
			TeachCourse tCourse = teachCourseService.get(teachCourseid);
			condition.put("openTerm", tCourse.getOpenTerm());
			List<SchoolCalendar> schoolCalendars = schoolCalendarService.findSCalendarListByCondition(condition);
			if(schoolCalendars.size()>0){
				calendar = schoolCalendars.get(0);
			}
			String teacherids = "";
			String classroomids = "";
			for (Map<String, Object> map2 : conflictMapList) {//遍历冲突
				String days = map2.get("days").toString();
				String[] weeks= map2.get("weeks").toString().split(",");
				String classroom = map2.get("name").toString();
				String teacher = map2.get("name").toString();
				String conflict = map2.get("conflict").toString();
				String type = map2.get("type").toString();
				String[] timeperiodids = map2.get("timeperiodids").toString().split(",");
				for (int i = 0; i < timeList.size(); i++) {//遍历（老师，时间，课室）
					Map<String, Object> timePeriodMap = timeList.get(i).get(Integer.parseInt(days)-1);
					for (String timeid : timeperiodids) {//遍历上课时间
						String timeValue = timePeriodMap.get(timeid)==null?"":timePeriodMap.get(timeid).toString();
						if(ExStringUtils.isBlank(timeValue)){
							if(i==0 && type.equals("teacher") && !timeValue.contains(teacher)){//老师
								timePeriodMap.put(timeid, teacher);
								if(!teacherids.contains(conflict)) teacherids += "'"+conflict+"',";
							}else if (i==1) {//上课时间
								timePeriodMap.put(timeid, ExStringUtils.toString(weeks));
							}else if (i==2 && type.equals("room") && !timeValue.contains(classroom)) {//课室
								timePeriodMap.put(timeid, classroom);
								if(!classroomids.contains(conflict)) classroomids += "'"+conflict+"',";
							}else {
								continue;
							}
						}else {
							String name =  timePeriodMap.get(timeid).toString();
							if(i==0 && type.equals("teacher") && !name.contains(teacher)){
								timePeriodMap.put(timeid, ","+conflict);
								teacherids += "'"+conflict+"',";
							}else if (i==1 && !name.contains(ExStringUtils.toString(weeks))) {
								Integer.parseInt(weeks[weeks.length-1]);
								timePeriodMap.put(timeid, ","+ExStringUtils.toString(weeks));
							}else if (i==2 && type.equals("room") && !name.contains(classroom)) {
								timePeriodMap.put(timeid, ","+conflict);
								classroomids += "'"+conflict+"',";
							}else {
								continue;
							}
						}
					}
					/*for (String timeid : timeperiodids) {//删除重复时间
						String timeValue = timePeriodMap.get(timeid)==null?"":timePeriodMap.get(timeid).toString();
						timeValue = ExStringUtils.orderDistinctDigitalString(timeValue,30);
						timePeriodMap.put(timeid, ExStringUtils.getWeeksName(timeValue));
					}*/
				}
			}
			if(ExStringUtils.isNotBlank(teacherids)){
				teacherids = teacherids.substring(1, teacherids.length()-2);
			}
			if(ExStringUtils.isNotBlank(classroomids)){
				classroomids = classroomids.substring(1, teacherids.length()-2);
			}
			String ableSelectTeacherNames = userService.findNamesByOutResourceids(teacherids,brSchoolid);
			String ableSelectClassroomNames = classroomService.findNamesByOutResourceids(classroomids,brSchoolid);
			//取反：根据冲突查出可选老师，时间，课室
			for (int i = 0; i < timeList.size(); i++) {//遍历（老师，时间，课室）
				for(int j=0;j<timeList.get(i).size();j++){//遍历星期
					Map<String, Object> timePeriodMap = timeList.get(i).get(j);
					for (String timeid : timePeriodMap.keySet()) {//遍历时间段
						String timeValue = timePeriodMap.get(timeid).toString();
						if(i==0){
							if(ExStringUtils.isBlank(timeValue)){
								timePeriodMap.put(timeid, "所有老师");
							}else {
								String tempName = ableSelectTeacherNames;
								for (String teacherName : timePeriodMap.get(timeid).toString().split("，")) {
									if (tempName.contains(teacherName+"，")) {
										tempName = tempName.replace(teacherName+"，", "");
									}else if (tempName.contains("，"+teacherName)) {
										tempName = tempName.replace("，"+teacherName, "");
									}else if(tempName.contains(teacherName)){
										tempName = tempName.replace(teacherName, "");
									}
								}
								timePeriodMap.put(timeid, tempName);
							}
						}else if (i==1) {
							if(ExStringUtils.isBlank(timeValue)){
								timePeriodMap.put(timeid, "所有周期");
							}else {
								timeValue = ExStringUtils.getInverseDigitalString(timeValue,calendar.getWeeks());
								timePeriodMap.put(timeid, timeValue);
							}
						}else if (i==2) {
							if(ExStringUtils.isBlank(timeValue)){
								timePeriodMap.put(timeid, "所有课室");
							}else {
								String tempName = ableSelectClassroomNames;
								for (String classroomName : timePeriodMap.get(timeid).toString().split("，")) {
									if (tempName.contains(classroomName+"，")) {
										tempName = tempName.replace(classroomName+"，", "");
									}else if (tempName.contains("，"+classroomName)) {
										tempName = tempName.replace("，"+classroomName, "");
									}else if(tempName.contains(classroomName)){
										tempName = tempName.replace(classroomName, "");
									}
								}
								timePeriodMap.put(timeid, tempName);
							}
						}
					}
				}
			}
		}
		model.addAttribute("timeList", timeList);
		model.addAttribute("timeperiodList", timeperiodList);
		return "/edu3/arrange/arrangeCourseDetail/timePeriod-view";
	}
	
	/**
	 * 保存排课详情
	 * @param courseDetail
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/save.html")
	public void saveCourseDetail(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String resourceid 		= 		ExStringUtils.trimToEmpty(request.getParameter("courseDetailid"));//排课详情id
			String teachCourseid 	= 		ExStringUtils.trimToEmpty(request.getParameter("teachCourseid"));//教学班id
			String teacherids 		= 		ExStringUtils.trimToEmpty(request.getParameter("teacherids"));//主讲老师
			String teacherNames 	= 		ExStringUtils.trimToEmpty(request.getParameter("teacherNames")).replace("！", "");
			String classroomid		= 		ExStringUtils.trimToEmpty(request.getParameter("classroomid"));//课室
			String weekOrDate 		= 		ExStringUtils.trimToEmpty(request.getParameter("weekOrDate"));//周期/日期
			String[] days     		=		request.getParameterValues("day");//星期
			String[] weeks 		    =		request.getParameterValues("week");//周数
			String[] timePeriod     =		request.getParameterValues("timePeriod");//上课时间
			String startDate		= 		request.getParameter("startDate");//开始时间
			String endDate			= 		request.getParameter("endDate");//结束时间
			String isArrange 		=		ExStringUtils.trim(request.getParameter("isArrange"));//判读是否从排课页面打开
			String ischeck			= 		request.getParameter("ischeck"); 
			List<String> weekList   = new ArrayList<String>();
			Date dStartDate = null;
			Date dEndDate = null;
			do{
				List<TeachCourseDetail> tCourseDetailList = new ArrayList<TeachCourseDetail>();
				TeachCourse teachCourse = teachCourseService.get(teachCourseid);
				TeachCourseDetail tCourseDetail = null;
				Classroom classroom = null;
				User user = SpringSecurityHelper.getCurrentUser();
				User teacher = null;
				if(ExStringUtils.isNotBlank(startDate)){
					dStartDate = ExDateUtils.parseDate(startDate, ExDateUtils.PATTREN_DATE_TIME);
				}
				if(ExStringUtils.isNotBlank(endDate)){
					dEndDate = ExDateUtils.parseDate(endDate, ExDateUtils.PATTREN_DATE_TIME);
				}
				if(weekOrDate.equals("1") && dEndDate!=null && dStartDate!=null && dEndDate.before(dStartDate)){
					map.put("statusCode", 300);
					map.put("message",  "保存失败！具体开始时间要小于具体结束时间！");
					continue;
				}
				for (String week : weeks) {
					weekList.add(week);
				}
				if(ExStringUtils.isNotEmpty(classroomid)){
					classroom = classroomService.get(classroomid);
				}
				if(ExStringUtils.isNotBlank(teacherids)){
					teacher = userService.get(teacherids);
				}
				
				//院历(计算学时)
				Map<String, Object> values = new HashMap<String, Object>();
				if(teachCourse.getUnit()!=null){
					values.put("unitid", teachCourse.getUnit().getResourceid());
				}
				if(ExStringUtils.isNotBlank(teachCourse.getOpenTerm())){
					values.put("openTerm", teachCourse.getOpenTerm());
				}
				List<SchoolCalendar> calendars = schoolCalendarService.findSCalendarListByCondition(values);
				List<DatePeriod> datePeriodList = new ArrayList<DatePeriod>();//可排课时间段
				Map<String,String> arrangeWeekMap = new HashMap<String, String>();//排课教学周
				int firstDay = 0;
				SchoolCalendar calendar = null;
				if(calendars!=null && calendars.size()>0){
					calendar = calendars.get(0);
					firstDay = calendar.getFirstDay();
					datePeriodList = calendar.getDatePeriodBytype(new String[]{"0","1"},new String[]{"-1","2"});
					if(weekOrDate.equals("1")){//日期
						datePeriodList = ExDateUtils.date4Intersection(datePeriodList, new DatePeriod(dStartDate,dEndDate));
					}else {//周期
						int maxWeek = calendar.getWeeks();//可排课最大周期
						for(int i=weeks.length;i>0;){
							String week = weeks[--i];
							if(Integer.parseInt(week)>maxWeek){
								weekList.remove(week);
							}else {
								break;
							}
						}
					}
					arrangeWeekMap = calendar.getWeeksInDatePeriodsBystartDate(calendar,datePeriodList);
				}
				calendar.setIsUsed("Y");
				int studyHouryOneDay = 0;//一天的排课学时
				String timePeriodNames = "";//上课时间段名称
				Set<TeachingPlanCourseTimePeriod> timePeriods = new HashSet<TeachingPlanCourseTimePeriod>();//上课时间段
				if(timePeriod != null){
					for (String string : timePeriod) {
						TeachingPlanCourseTimePeriod tPeriod = teachingPlanCourseTimePeriodService.get(string);
						timePeriodNames += tPeriod.getCourseTimeName()+"，";
						studyHouryOneDay += tPeriod.getStydyHour();
						timePeriods.add(tPeriod);
					}
					timePeriodNames = timePeriodNames.substring(0, timePeriodNames.length()-1);
				}
				//保存排课
				String hql = " from "+TeachCourseDetail.class.getSimpleName()+" where isDeleted=0 and teachCourse.resourceid=?";
				List<TeachCourseDetail> tCourseDetailRemoveList = new ArrayList<TeachCourseDetail>();
				for (int i = 0; i < days.length && ExStringUtils.isNotBlank(days[i]); i++) {
					if(ExStringUtils.isNotBlank(resourceid)){//编辑
						tCourseDetail = teachCourseDetailService.get(resourceid);
					}else {//--------------------新增
						hql = " from "+TeachCourseDetail.class.getSimpleName()+" where isDeleted=0 and teacher.resourceid=? and days=? and teachCourse.resourceid=?";
						List<TeachCourseDetail> tCourseDetails = teachCourseDetailService.findByHql(hql, teacherids,days[i],teachCourseid);
						if(tCourseDetails.size()>0){//如果已经有老师某个星期的排课记录，则替换上课时间，并删除多余的记录
							tCourseDetail = tCourseDetails.get(0);
							tCourseDetailRemoveList = tCourseDetails;
							tCourseDetailRemoveList.remove(tCourseDetail);
						}else {
							tCourseDetail = new TeachCourseDetail();
							tCourseDetail.setCreateDate(new Date());
							tCourseDetail.setTeachCourse(teachCourse);
						}
					}
					tCourseDetail.setClassroom(classroom);
					tCourseDetail.setTeacher(teacher);
					tCourseDetail.setDays(days[i]);
					tCourseDetail.setTimePeriods(timePeriods);
					tCourseDetail.setTimePeriodids(ExStringUtils.toString(timePeriod));
					tCourseDetail.setTimePeriodNames(timePeriodNames);
					tCourseDetail.setOperator(user);
					tCourseDetail.setOperatorName(user.getCnName());
					tCourseDetail.setUpdateDate(new Date());
					if(ExStringUtils.isNotBlank(weekOrDate)){
						int studyHour = 0;//学时
						String weekName = "";//星期
						
						if(tCourseDetail.getDateType()==0){//周期
							List<String> actualWeekList = ExStringUtils.toList4Digital(arrangeWeekMap.get(days[i]));//可排课教学周
							actualWeekList = ExStringUtils.intersectDigitalArray(actualWeekList,weeks);//实际排课教学周
							studyHour = studyHouryOneDay*actualWeekList.size();
							weekName = ExStringUtils.toString(weeks);
						}else if (tCourseDetail.getDateType()==1) {//日期
							//排课详情开始时间至结束时间
							int detailDay = (int) ExDateUtils.getDateDiffNum(ExDateUtils.formatDateStr(dStartDate,ExDateUtils.PATTREN_DATE_TIME),
									ExDateUtils.formatDateStr(dEndDate,ExDateUtils.PATTREN_DATE_TIME));
							//教学周开始时间至排课详情开始时间
							int teachDay = (int) ExDateUtils.getDateDiffNum(ExDateUtils.formatDateStr(calendars.get(0).getStartDate(),ExDateUtils.PATTREN_DATE_TIME),
									ExDateUtils.formatDateStr(dStartDate,ExDateUtils.PATTREN_DATE_TIME));
							studyHour = (detailDay/7)*studyHouryOneDay;
							if(detailDay%7>0){//如果有余数并且包含星期，加一个单位学时
								for (int j = 0; j < detailDay; j++) {
									if(Integer.parseInt(days[i])%7==(j%7+firstDay)%7){
										studyHour += studyHouryOneDay;
									}
								}
							}
							for (int k=0;k<=detailDay/7;k++) {
								weekName += teachDay/7 + k + ",";
							}
							if(ExStringUtils.isNotBlank(weekName)){
								weekName = weekName.substring(0, weekName.length()-1);
							}
							tCourseDetail.setStartdate(dStartDate);
							tCourseDetail.setEnddate(dEndDate);
						}
						int leadStudyHoury = 0;//学时差
						for (TeachingPlanCourseTimePeriod tPeriod : timePeriods) {
							//如果时间段不在上课时间段范围类，则学时差减一个单位的学时
							boolean isInCoursePeriod = false;//是否在上课时间段
							if(weekOrDate.equals("0")){//周期
								for(DatePeriod dPeriod: datePeriodList){
									if(tPeriod.getStartTime().getTime()%1440 >= dPeriod.getStartDate().getTime()%1440 && 
											tPeriod.getEndTime().getTime()%1440 <= dPeriod.getEndDate().getTime()%1440){
										leadStudyHoury -= tPeriod.getStydyHour();
										isInCoursePeriod = true;
										break;
									}
								}
								if(!isInCoursePeriod) leadStudyHoury -= tPeriod.getStydyHour();
							}else if (weekOrDate.equals("1")) {//日期
								if(tPeriod.getStartTime().getTime()%1440 < dStartDate.getTime()%1440) leadStudyHoury -= tPeriod.getStydyHour();
								if(tPeriod.getEndTime().getTime()%1440 > dEndDate.getTime()%1440) leadStudyHoury -= tPeriod.getStydyHour();
							}
						}
						tCourseDetail.setWeeks(weekName);
						tCourseDetail.setDateType(Integer.valueOf(weekOrDate));
						tCourseDetail.setStudyHour(studyHour+leadStudyHoury);
					}
					tCourseDetailList.add(tCourseDetail);
				}
				teachCourseDetailService.batchDelete(tCourseDetailRemoveList);
				teachCourseDetailService.batchSaveOrUpdate(tCourseDetailList);
				if(ExStringUtils.isNotBlank(teachCourse.getTeacherNames())){
					if(teachCourse.getTeacherNames().contains(teacherNames)){
						teacherNames = teachCourse.getTeacherNames();
					}else {
						teacherNames = teachCourse.getTeacherNames()+","+teacherNames;
					}
				}
				teachCourse.setTeacherNames(teacherNames);
				teachCourse.setArrangeStatus(1);
				teachCourseService.update(teachCourse);
				schoolCalendarService.save(calendar);//更改使用状态
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_ARRANGE_ARRANGECOURSE_ARRANGE");
				map.put("reloadUrl", request.getContextPath() +"/edu3/arrange/arrangeCourse/arrange.html?teachCourseid="+teachCourseid+"&isArrange="+isArrange+"&ischeck="+ischeck);
			}while(false);
		
		} catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 新增排课
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/add.html")
	public String addCourseDetail(HttpServletRequest request,ModelMap model,Page objPage){
		String brSchoolid = "";
		String unitOption = "";//教学点选项
		User user = SpringSecurityHelper.getCurrentUser();
		int unique = 0;
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			brSchoolid = user.getUnitId();
			unique = 1;
			model.addAttribute("brSchoolid", brSchoolid);
		}
		unitOption = orgUnitService.constructOptions(null, brSchoolid,unique);
		model.addAttribute("unitOption", unitOption);
		return "edu3/arrange/arrangeCourseDetail/arrangeCourseDetail-form";
	}
	
	/**
	 * 删除排课详情
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseDetail/delete.html")
	public void deleteCourseDetail(HttpServletRequest request,HttpServletResponse response) {
		String courseDetailid	=		ExStringUtils.trim(request.getParameter("resourceid"));
		String teachCourseid	=		ExStringUtils.trim(request.getParameter("teachCourseid"));
		String courseDetailids[] = courseDetailid.split(",");
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode = 0;
		String message = "";
		if(ExStringUtils.isNotBlank(courseDetailid)){ 
			try {
				TeachCourse tCourse = null;
				List<TeachCourse> tCourseList = new ArrayList<TeachCourse>();
				List<String> tempDetailidList = new ArrayList<String>();
				for (int i = 0; i < courseDetailids.length; i++) {
					TeachCourseDetail tCourseDetail = teachCourseDetailService.get(courseDetailids[i]);
					//删除任课老师
					if(tCourse!=null && !tCourse.getResourceid().equals(teachCourseid)){
						Set<TeachCourseDetail> teachCourseDetails = tCourse.getTeachCourseDetails();
						String teacherNames = "";
						//遍历排课详情，重新设置老师名称
						for (TeachCourseDetail teachCourseDetail : teachCourseDetails) {
							//如果不在删除列表，并且名称不重复，则添加老师名称
							if(!tempDetailidList.contains(teachCourseDetail.getResourceid())){//删除id
								if(!teacherNames.contains(teachCourseDetail.getTeacher().getCnName())){
									teacherNames += ","+teachCourseDetail.getTeacher().getCnName();
								}
							}
						}
						if(ExStringUtils.isNotEmpty(teacherNames)) teacherNames = teacherNames.substring(1, teacherNames.length());
						tCourse.setTeacherNames(teacherNames);
						tCourseList.add(tCourse);
						tempDetailidList.removeAll(tempDetailidList);
						teacherNames = "";
					}
					
					tCourse = tCourseDetail.getTeachCourse();
					tempDetailidList.add(courseDetailids[i]);
					teachCourseid = tCourseDetail.getTeachCourse().getResourceid();
				}
				//删除最后一个教学班的排课详情
				Set<TeachCourseDetail> teachCourseDetails = tCourse.getTeachCourseDetails();
				String teacherNames = "";
				//遍历排课详情，重新设置老师名称
				for (TeachCourseDetail teachCourseDetail : teachCourseDetails) {
					//如果不在删除列表，并且名称不重复，则添加老师名称
					if(!tempDetailidList.contains(teachCourseDetail.getResourceid())){//删除id
						if(!teacherNames.contains(teachCourseDetail.getTeacher().getCnName())){
							teacherNames += ","+teachCourseDetail.getTeacher().getCnName();
						}
					}
				}
				if(ExStringUtils.isNotEmpty(teacherNames)) teacherNames = teacherNames.substring(1, teacherNames.length());
				tCourse.setTeacherNames(teacherNames);
				tCourseList.add(tCourse);
				tempDetailidList.removeAll(tempDetailidList);
				teachCourseDetailService.batchDelete(courseDetailids);
				teachCourseService.batchSaveOrUpdate(tCourseList);
				statusCode = 200;
				message = "操作成功！";
			} catch (Exception e) {
				logger.error("操作出错：{}",e.fillInStackTrace());
				statusCode = 300;
				message = "操作失败！";
			} finally {
				map.put("statusCode", statusCode);
				map.put("message", message);
			}
		} else {
			map.put("statusCode", 300);
			map.put("message", "选项为空！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
