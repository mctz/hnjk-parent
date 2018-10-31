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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachingWillingness;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.arrange.service.ITeachingWillingnessService;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimePeriodService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

@Controller
public class TeachingWillingnessController extends BaseSupportController{

	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("teachingWillingnessService")
	private ITeachingWillingnessService teachingWillingnessService;
	
	@Autowired
	@Qualifier("teachCourseService")
	private ITeachCourseService teachCourseService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimePeriodService")
	private ITeachingPlanCourseTimePeriodService teachingPlanCourseTimePeriodService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictService;
	
	@RequestMapping("/edu3/arrange/teachingWillingness/list.html")
	public String teachingWillingnessList(HttpServletRequest request,ModelMap model,Page objPage){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String brSchoolid			 =		ExStringUtils.trim(request.getParameter("brSchoolid"));//学习中心
		User user = SpringSecurityHelper.getCurrentUser();
		boolean isTeacher = false;
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			model.addAttribute("isBrschool", true);
			brSchoolid = user.getOrgUnit().getResourceid();
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
			List<String> roleList = new ArrayList<String>();
			roleList.add("ROLE_LINE");
			roleList.add("ROLE_TEACHER_DUTY");
			roleList.add("ROLE_TEACHER");
			if(user.getRoles()!=null){
				for (Role role : user.getRoles()) {
					if(roleList.contains(role.getRoleCode())){
						isTeacher = true;
					}else if(role.getRoleCode().equals("ROLE_BRS_STUDENTSTATUS")){
						isTeacher = false;
						break;
					}
				}
			}
		}
		if(isTeacher){
			condition.put("teacherid", user.getResourceid());
		}
		if(ExStringUtils.isNotBlank(brSchoolid)) condition.put("brSchoolid", brSchoolid);
		Page page = teachingWillingnessService.findWillingnessByCondition(condition, objPage);
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		return "/edu3/arrange/teachingWillingness/teachingWillingness-list";
	}

	/**
	 * 编辑意愿信息
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/teachingWillingness/edit.html")
	public String editWillingness(HttpServletRequest request, ModelMap model,Page objPage) throws WebException{
		//查询条件
		Map<String,Object> condition 	= 		new HashMap<String,Object>();
		String resId 					= 		ExStringUtils.trimToEmpty(request.getParameter("resId"));
		String teachCourseid 			= 		ExStringUtils.trimToEmpty(request.getParameter("teachCourseid"));
		String teacherids 		= 		ExStringUtils.trimToEmpty(request.getParameter("teacherids"));
		String teacherNames 	= 		ExStringUtils.trimToEmpty(request.getParameter("teacherNames"));
		String classroomType 	= 		ExStringUtils.trimToEmpty(request.getParameter("classroomType"));
		String applyInfo 		= 		ExStringUtils.trimToEmpty(request.getParameter("applyInfo"));
		String[] timePeriod     =		request.getParameterValues("timePeriod");
		String[] days    		=		request.getParameterValues("days");
		User user 						= 		SpringSecurityHelper.getCurrentUser();
		TeachingWillingness willingness = 		null;
		String userRole 				= 		"";
		String branchSchool 	= 		ExStringUtils.trimToEmpty(request.getParameter("brSchoolid"));
		TeachCourse teachCourse = null;
		//查询上课时间
		String[] setting = null;
		if(ExStringUtils.isNotBlank(resId)){//编辑意愿信息
			willingness = teachingWillingnessService.get(resId);
			setting = willingness.getTimePeriodids().split(",");
			teachCourse = willingness.getTeachCourse();
			teachCourseid = teachCourse.getResourceid();
		}else {		//意愿申请
			willingness = new TeachingWillingness();
			setting = timePeriod;
			if(ExStringUtils.isNotBlank(teachCourseid)){
				teachCourse = teachCourseService.get(teachCourseid);
				branchSchool = teachCourse.getUnit().getResourceid();
				willingness.setTeachCourse(teachCourse);
			}else {//暂时不用
				//构造select标签
				StringBuffer option = new StringBuffer();
				List<TeachCourse> TeachCourseList = teachCourseService.findTeachCourseByCondition(condition);
				if(null != TeachCourseList && TeachCourseList.size()>0){
					for(TeachCourse tCourse : TeachCourseList){
							option.append("<option value='"+tCourse.getResourceid()+"'");				
							option.append(">"+tCourse.getTeachingClassname()+"</option>");
					}
				}else {
					option.append("<option value=0");				
					option.append(">请先添加教学班</option>");
				}
			}
		}
		if(ExStringUtils.isBlank(teacherNames) && willingness.getProposer()!=null){
			teacherNames = willingness.getProposer().getCnName();
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){ 
			condition.put("brSchoolid", branchSchool);
			model.addAttribute("brSchoolid", branchSchool);
		}
		String timePeriodOption = teachingPlanCourseTimePeriodService.constructOptions(condition , setting);
		if(ExStringUtils.isNotBlank(classroomType)){
			willingness.setClassroomType(classroomType);
		}
		if(ExStringUtils.isNotBlank(applyInfo)){
			willingness.setInfo(applyInfo);
		}
		if(timePeriod!=null && ExStringUtils.isNotBlank(timePeriod.toString())){
			willingness.setTimePeriodids(ExStringUtils.toString(timePeriod));
		}
		if(days!=null && ExStringUtils.isNotBlank(days.toString())){
			willingness.setDays(ExStringUtils.toString(days));
		}
		
		//判断是否为 老师/教务员
		Set<Role> roles =  user.getRoles();
		for (Role role : roles) {
			if(role.getRoleCode().equals("ROLE_LINE") || role.getRoleCode().equals("ROLE_TEACHER_DUTY")){//在线老师，主讲老师
				userRole = "teacher";
				teacherids = user.getResourceid();
			}else if(role.getRoleCode().equals("ROLE_BRS_STUDENTSTATUS")||role.getRoleCode().equals("ROLE_ADMIN")){//学习中心教务员/超级管理员
				userRole = "jwy";
				break;
			}
		}
		model.addAttribute("user", user);
		model.addAttribute("teacherids", teacherids);
		model.addAttribute("teacherNames", teacherNames);
		model.addAttribute("userRole", userRole);
		model.addAttribute("willingness", willingness);
		model.addAttribute("teachCourseid", teachCourseid);
		model.addAttribute("timePeriodOption", timePeriodOption);
		return "/edu3/arrange/teachingWillingness/teachingWillingness-form";
	}
	
	/**
	 * 保存意愿信息
	 * @param teachCourse
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/teachingWillingness/save.html")
	public void saveWillingness(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String willid 			= 		ExStringUtils.trimToEmpty(request.getParameter("resId"));
			String teachCourseid 	= 		ExStringUtils.trimToEmpty(request.getParameter("teachCourseid"));
			String teacherids 		= 		ExStringUtils.trimToEmpty(request.getParameter("teacherids"));
			String teacherNames 	= 		ExStringUtils.trimToEmpty(request.getParameter("teacherNames"));
			String userid 			= 		ExStringUtils.trimToEmpty(request.getParameter("userid"));
			String classroomType	= 		ExStringUtils.trimToEmpty(request.getParameter("classroomType"));
			String info 			= 		ExStringUtils.trimToEmpty(request.getParameter("info"));
			String[] days     		=		request.getParameterValues("day");
			String[] timePeriod     =		request.getParameterValues("timePeriod");
			do{
				if(!userid.equals(SpringSecurityHelper.getCurrentUser().getResourceid())){
					map.put("statusCode", 300);
					map.put("message", "会话超时，请尝试重新登录！");
					continue;
				}
				TeachingWillingness willingness = null;
				if(ExStringUtils.isNotBlank(willid)){//编辑
					willingness = teachingWillingnessService.get(willid);
					map.put("isEdit", "Y");
				}else {//意愿申请
					//判断是否已申请
					Map<String, Object> values = new HashMap<String, Object>();
					String hql = " from "+TeachingWillingness.class.getSimpleName()+" where isDeleted=0 and teachCourse.resourceid=:teachCourseid and proposer.resourceid=:teacherids";
					values.put("teachCourseid", teachCourseid);
					values.put("teacherids", teacherids);
					List<TeachingWillingness> willingnesselList = teachingWillingnessService.findByHql(hql,values);
					if(willingnesselList.size()>0){
						map.put("statusCode", 300);
						map.put("message", "您已经申请过了！");
						continue;
					}
					willingness = new TeachingWillingness();
					willingness.setCreateDate(new Date());
				}
				TeachCourse teachCourse = teachCourseService.get(teachCourseid);
				willingness.setTeachCourse(teachCourse);
				//获取上课时间段名称
				String timePeriodNames = "";
				if(timePeriod!=null){
					for (String string : timePeriod) {
						TeachingPlanCourseTimePeriod tPeriod = teachingPlanCourseTimePeriodService.get(string);
						timePeriodNames += tPeriod.getTimeName()+" "+dictService.dictCode2Val("CodeCourseTimePeriod", tPeriod.getTimePeriod())+DateUtils.getFormatDate(tPeriod.getStartTime(),"HH:mm")+"-"+DateUtils.getFormatDate(tPeriod.getEndTime(),"HH:mm")+"，";
					}
				}
				if(ExStringUtils.isNotEmpty(timePeriodNames)){
					timePeriodNames = timePeriodNames.substring(0, timePeriodNames.length()-1);
				}
				willingness.setClassroomType(classroomType);
				willingness.setInfo(info);
				willingness.setOperator(SpringSecurityHelper.getCurrentUser());
				willingness.setProposer(userService.get(teacherids));
				willingness.setDays(ExStringUtils.toString(days));
				willingness.setTimePeriodids(ExStringUtils.toString(timePeriod));
				willingness.setTimePeriodNames(timePeriodNames);
				teachingWillingnessService.saveOrUpdate(willingness);
				teachCourse.setWillTeacherName(teacherNames);
				teachCourse.setSelectedStatus(1);
				teachCourseService.update(teachCourse);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
			}while(false);
		
		} catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 *老师选择对话框
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/arrange/teachingWillingness/selectteacher.html")
	public String listRecordScoreTeacher(Page objPage,HttpServletRequest request,ModelMap model) throws Exception{
		objPage.setOrderBy("orgUnit.unitCode ");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool		=		ExStringUtils.trim(request.getParameter("branchSchool"));//学习中心
		String willid 			= 		ExStringUtils.trimToEmpty(request.getParameter("resId"));//意愿信息id
		String teachCourseid 	= 		ExStringUtils.trimToEmpty(request.getParameter("teachCourseid"));//教学班id
		String userid 			= 		ExStringUtils.trimToEmpty(request.getParameter("userid"));//当前用户
		String classroomType 	= 		ExStringUtils.trimToEmpty(request.getParameter("classroomType"));
		String applyInfo 		= 		ExStringUtils.trimToEmpty(request.getParameter("applyInfo"));
		String[] days     		=		request.getParameterValues("days");
		String[] timePeriod     =		request.getParameterValues("timePeriod");
		String cnName 			=		ExStringUtils.trimToEmpty(request.getParameter("cnName"));//教师名称
		String teacherCode 		=		ExStringUtils.trimToEmpty(request.getParameter("teacherCode"));//教师编号
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("unitId", branchSchool);
			condition.put("branchSchool", branchSchool);
		}
		condition.put("teacherType", "'ROLE_LINE','ROLE_TEACHER_DUTY','ROLE_TEACHER'");//教师类型：在线老师，主讲老师，课程负责老师
		condition.put("isDeleted", 0);
		if(ExStringUtils.isNotBlank(cnName)) condition.put("cnName",cnName);
		if(ExStringUtils.isNotBlank(teacherCode)) condition.put("teacherCode",teacherCode);
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);		
		condition.put("selectedTeachers", ExStringUtils.trimToEmpty(request.getParameter("teacherids")));
		condition.put("selectedNames", ExStringUtils.trimToEmpty(request.getParameter("teacherNames")));
		model.addAttribute("condition", condition);
		model.addAttribute("teacherlist", page);
		model.addAttribute("resId", willid);
		model.addAttribute("branchSchool", branchSchool);
		model.addAttribute("teachCourseid", teachCourseid);
		model.addAttribute("userid", userid);
		model.addAttribute("classroomType", classroomType);
		model.addAttribute("applyInfo", applyInfo);
		model.addAttribute("days", ExStringUtils.toString(days));
		model.addAttribute("timePeriod", ExStringUtils.toString(timePeriod));
		return "/edu3/arrange/selectCourseResult/willingness-selector";
	}
	
	/**
	 * ajax设置老师
	 * @param resId
	 * @param userid
	 * @param userRole
	 * @param setteacherName
	 * @param teacherId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/setteacher.html")
	public void setTeacher(String setteacherName,String teacherId,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode = 0;
		String message = "";
		if(ExStringUtils.isNotBlank(teacherId)){ //-----设置登分教师
			try {
				statusCode = 200;
				message = "设置成功！";
				map.put("teacherids", teacherId);
				map.put("teacherNames", setteacherName);
			} catch (Exception e) {
				logger.error("设置出错：{}",e.fillInStackTrace());
				statusCode = 300;
				message = "设置失败！";
			} finally {
				map.put("statusCode", statusCode);
				map.put("message", message);
			}
		} else {
			map.put("statusCode", 300);
			map.put("message", "老师为空！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 撤销意愿信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/teachingWillingness/delete.html")
	public void deleteWillingness(HttpServletRequest request,HttpServletResponse response){
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				teachingWillingnessService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/arrange/teachingWillingness/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * ajax查询教学班信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/teachingWillingness/queryTeachCourse.html")
	public void queryTeachCourse(HttpServletRequest request,HttpServletResponse response){
		String resourceid = request.getParameter("teachCourseid");
		Map<String ,Object> map = new HashMap<String, Object>();
		TeachCourse teachCourse = teachCourseService.get(resourceid);
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("brSchoolid", teachCourse.getUnit().getResourceid());
		String timePeriodOption = teachingPlanCourseTimePeriodService.constructOptions(condition , null);
		map.put("teachCourse", teachCourse);
		map.put("timePeriodOption", timePeriodOption);
		renderJson(response,JsonUtils.mapToJson(map));
	}
}
