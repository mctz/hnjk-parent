package com.hnjk.edu.arrange.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.model.TeachCourseDetail;
import com.hnjk.edu.arrange.service.ITeachCourseClassesService;
import com.hnjk.edu.arrange.service.ITeachCourseDetailService;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.arrange.service.ITeachingWillingnessService;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ICourseStatusClService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

@Controller
public class SelectCourseResultController extends BaseSupportController {

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("teachCourseClassesService")
	private ITeachCourseClassesService teachCourseClassesService;
	
	@Autowired
	@Qualifier("teachCourseService")
	private ITeachCourseService teachCourseService;
	
	@Autowired
	@Qualifier("teachCourseDetailService")
	private ITeachCourseDetailService teachCourseDetailService;
	
	@Autowired
	@Qualifier("courseStatusClService")
	private ICourseStatusClService courseStatusClService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 选课结果列表
	 * @param request
	 * @param model
	 * @param objPage
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/selectcourseresult/list.html")
	public String selectCourseList(HttpServletRequest request, ModelMap model, Page objPage)throws WebException{
		//查询条件
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		boolean isBrschool = false;
		String userRole = "";
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			isBrschool = true;
			condition.put("brSchoolid",user.getOrgUnit().getResourceid());
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		//判断是否为 老师/教务员
		Set<Role> roles =  user.getRoles();
		for (Role role : roles) {
			if(role.getRoleCode().equals("ROLE_LINE") || role.getRoleCode().equals("ROLE_TEACHER_DUTY")){//在线老师，主讲老师
				userRole = "teacher";
			}else if(role.getRoleCode().equals("ROLE_BRS_STUDENTSTATUS")||role.getRoleCode().equals("ROLE_ADMIN")){//学习中心教务员/超级管理员
				userRole = "jwy";
				break;
			}
		}
		condition.put("generateStatus", "1");
		Page page = teachCourseService.findTeachCourseByHql(condition, objPage);
		model.addAttribute("page", page);
		model.addAttribute("userRole", userRole); 
		model.addAttribute("condition", condition);
		model.addAttribute("isBrschool", isBrschool); 
		model.addAttribute("userid",user.getResourceid());
		return "/edu3/arrange/selectCourseResult/selectCourse-list";
	}
	
	/**
	 * 编辑选课结果
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/selectcourseresult/edit.html")
	public String editCourseResult(HttpServletRequest request, ModelMap model,Page objPage) throws WebException{
		//查询条件
		String resId 			= 		ExStringUtils.trimToEmpty(request.getParameter("resId"));
		TeachCourse teachCourse = teachCourseService.get(resId);
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("teachCourseid", resId);
		Page page = teachCourseClassesService.findTeachCourseByHql(condition, objPage);
		model.addAttribute("teachCourse", teachCourse);
		model.addAttribute("page", page);
		return "/edu3/arrange/selectCourseResult/selectCourse-form";
	}
	
	/**
	 * 保存选课结果
	 * @param teachCourse
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/selectcourseresult/save.html")
	public void saveCourseResult(TeachCourse teachCourse,HttpServletRequest request,HttpServletResponse response,ModelMap model)throws WebException{
		Map<String,Object> map  = 		new HashMap<String, Object>();
		//String classicid		=		ExStringUtils.trim(request.getParameter("classicid"));//层次
		String[] teachClassid   =		request.getParameterValues("teachClassid");//班级
		String[] teachEndDate   =		request.getParameterValues("teachEndDate");//结束日期
		String[] teachEndWeek   =		request.getParameterValues("teachEndWeek");//结束周
		List<TeachCourseClasses> teachClassesList = new ArrayList<TeachCourseClasses>();
		try {
			if(ExStringUtils.isNotBlank(teachCourse.getResourceid())){
				TeachCourse _teachCourse = teachCourseService.get(teachCourse.getResourceid());
				//_teachCourse.setOpenTerm(teachCourse.getOpenTerm());//上课学期
				_teachCourse.setStudyHour(teachCourse.getStudyHour());//学时
				_teachCourse.setCourseName(teachCourse.getCourseName());//课程名称
				//_teachCourse.setTeachingtype(teachCourse.getTeachingtype());//学习形式
				//_teachCourse.setExamClassType(teachCourse.getExamClassType());//考试类型
				_teachCourse.setTeachingClassname(teachCourse.getTeachingClassname());//教学班名称
				_teachCourse.setMemo(teachCourse.getMemo());
				
				/*if(ExStringUtils.isNotBlank(classicid)){//层次
					_teachCourse.setClassic(classicService.get(classicid));
				}*/
				for (int i=0; i<teachClassid.length; i++) {
					TeachCourseClasses teachClasses = teachCourseClassesService.get(teachClassid[i]);
					if(ExStringUtils.isNotBlank(teachEndDate[i])){
						Date enDate = ExDateUtils.parseDate(teachEndDate[i], ExDateUtils.PATTREN_DATE);
						teachClasses.setTeachEndDate(enDate);
					}
					if(ExStringUtils.isNotBlank(teachEndWeek[i])){
						teachClasses.setTeachEndWeek(Integer.parseInt(teachEndWeek[i]));
					}
					teachClassesList.add(teachClasses);
				}
				teachCourseService.update(_teachCourse);
				teachCourseClassesService.batchSaveOrUpdate(teachClassesList);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_ARRANGE_COURSERESULR_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/arrange/selectcourseresult/edit.html?resId="+teachCourse.getResourceid());
		} catch (Exception e) {
			logger.error("保存开课结果出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 撤销选课结果
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/selectcourseresult/repeal.html")
	public void repealCourseResult(HttpServletRequest request,HttpServletResponse response){
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			List<String> resids = Arrays.asList(resourceid.split(","));
			List<TeachCourse> tCourses = new ArrayList<TeachCourse>();
			for (String string : resids) {
				TeachCourse tCourse = teachCourseService.get(string);
				tCourse.setGenerateStatus(0);
				tCourses.add(tCourse);
			}
			teachCourseService.batchSaveOrUpdate(tCourses);
			map.put("statusCode", 200);
			map.put("message", "撤销成功！");				
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 发布/取消发布 选课结果
	 * @param request
	 * @param response
	 */
	@RequestMapping(value={"/edu3/arrange/selectcourseresult/publish.html","/edu3/arrange/selectcourseresult/notpublish.html"})
	public void publishCourseResult(HttpServletRequest request,HttpServletResponse response){
		String resourceid = request.getParameter("resourceid");
		String operate = ExStringUtils.trim(request.getParameter("operate"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid) && operate!=null){
				String resids[] = resourceid.split("\\,");
				for (String id : resids) {
					TeachCourse teachCourse = teachCourseService.get(id);
					if("publish".equals(operate)){
						teachCourse.setPublishStatus(1);
					}else {
						teachCourse.setPublishStatus(0);
					}
					teachCourseService.update(teachCourse);
				}
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("reloadUrl", request.getContextPath()+"/edu3/arrange/selectcourseresult/list.html");
			}else {
				map.put("statusCode", 300);
				map.put("message", "操作出错，没有操作类型！");
			}
		} catch (Exception e) {
			logger.error("操作出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 打开查询条件界面
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/teachCourse/setting.html")
	public String setTeachCourse(HttpServletRequest request, HttpServletResponse response) throws WebException {
		return "/edu3/arrange/selectCourseResult/setting";
	}
	
}
