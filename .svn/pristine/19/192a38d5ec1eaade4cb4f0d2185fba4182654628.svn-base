package com.hnjk.edu.arrange.controller;

import java.text.ParseException;
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

import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.model.TeachingWillingness;
import com.hnjk.edu.arrange.service.ITeachCourseClassesService;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.arrange.service.impl.TeachCourseClassesServiceImpl;
import com.hnjk.edu.arrange.vo.TeachCourseDetailVo;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.util.DateUtils;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

@Controller
public class TeachCourseClassesController extends BaseSupportController{

	private static final long serialVersionUID = 1L;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("teachCourseService")
	private ITeachCourseService teachCourseService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("teachCourseClassesService")
	private ITeachCourseClassesService teachCourseClassesService;

	/**
	 * 查看教学班班级信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseResult/classesInfo.html")
	public String arrangeCourseList(HttpServletRequest request,ModelMap model,Page objPage){
		objPage.setOrderBy(" teachCourse.unit.resourceid,teachCourse.openTerm,teachCourse.teachingClassname ");
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		boolean isBrschool = false;
		User user = SpringSecurityHelper.getCurrentUser();
		//判断是否为校外学习中心人员
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
			isBrschool = true;
			condition.put("brSchoolid", user.getOrgUnit().getResourceid());
			model.addAttribute("schoolname",ExStringUtils.trim(user.getOrgUnit().getUnitName()));
		}
		model.addAttribute("isBrschool", isBrschool);
		Page page = teachCourseClassesService.findTeachCourseByHql(condition,objPage);
		model.addAttribute("page", page);
		return "/edu3/arrange/arrangeCourseResult/classesInfo-list";
	}
	
	/**
	 * 编辑教学班级信息
	 * @param request
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/arrange/teachCourseClasses/edit.html")
	public String editTeachClasses(HttpServletRequest request,ModelMap model) throws ParseException{
		String resourceid = ExStringUtils.trim(request.getParameter("resourceid"));//教学班级id
		String brSchoolid = ExStringUtils.trim(request.getParameter("brSchoolid"));//教学点
		String teacherids = ExStringUtils.trim(request.getParameter("teacherids"));//登分老师
		String teacherNames = ExStringUtils.trim(request.getParameter("teacherNames"));//登分老师名称
		String teachEndDate = ExStringUtils.trim(request.getParameter("teachEndDate"));//上课结束日期
		String teachEndWeek = ExStringUtils.trim(request.getParameter("teachEndWeek"));//上课结束周
		String _brSchoolid = "";
		TeachCourseClasses teachClasses = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			teachClasses = teachCourseClassesService.get(resourceid);
			if(ExStringUtils.isBlank(brSchoolid) && teachClasses.getTeachCourse().getUnit()!=null){
				_brSchoolid = teachClasses.getTeachCourse().getUnit().getResourceid();
			}
			if(ExStringUtils.isBlank(teacherNames) && teachClasses.getRecordScorer()!=null){
				teacherNames = teachClasses.getRecordScorer().getCnName();
				teacherids = teachClasses.getRecordScorer().getResourceid();
			}
			
		}else{ //----------------------------------------新增
			teachClasses = new TeachCourseClasses();
			User user = SpringSecurityHelper.getCurrentUser();
			if(ExStringUtils.isBlank(brSchoolid) && user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
				_brSchoolid = user.getOrgUnit().getResourceid();
			}
		}
		if(ExStringUtils.isNotBlank(teachEndDate)){
			Date dEndDate = ExDateUtils.parseDate(teachEndDate, ExDateUtils.PATTREN_DATE_TIME);
			teachClasses.setTeachEndDate(dEndDate);
		}
		if(ExStringUtils.isNotBlank(teachEndWeek)){
			teachClasses.setTeachEndWeek(Integer.parseInt(teachEndWeek));
		}
		if(ExStringUtils.isNotBlank(teacherids)){
			model.addAttribute("teacherids", teacherids);
		}
		if(ExStringUtils.isNotBlank(teacherNames)){
			model.addAttribute("teacherNames", teacherNames);
		}
		if(ExStringUtils.isBlank(brSchoolid) && ExStringUtils.isNotBlank(_brSchoolid)){
			brSchoolid = _brSchoolid;
		}
		model.addAttribute("brSchoolid", brSchoolid);
		model.addAttribute("teachClasses", teachClasses);
		return "/edu3/arrange/arrangeCourseResult/classesInfo-form";
	}
	
	/**
	 * 选择登分老师
	 * @param request
	 * @param response
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/arrange/teachCourseClasses/selectteacher.html")
	public String setTeacher(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model){
		objPage.setOrderBy("orgUnit.unitCode ");
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String resourceid = ExStringUtils.trim(request.getParameter("resourceid"));//教学班级id
		String brSchoolid = ExStringUtils.trim(request.getParameter("brSchoolid"));//教学点
		String teachEndDate = ExStringUtils.trim(request.getParameter("teachEndDate"));//上课结束日期
		String teachEndWeek = ExStringUtils.trim(request.getParameter("teachEndWeek"));//上课结束周
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
		
		model.addAttribute("teacherlist", page);
		model.addAttribute("condition", condition);
		model.addAttribute("brSchoolid", brSchoolid);
		model.addAttribute("resourceid", resourceid);
		model.addAttribute("teachEndDate", teachEndDate);
		model.addAttribute("teachEndWeek", teachEndWeek);
		return "/edu3/arrange/arrangeCourseResult/classesInfo-selector";
	}
	
	@RequestMapping("/edu3/arrange/teachCourseClasses/save.html")
	public void saveTeachClasses(TeachCourseClasses tClasses,HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String teacherids 		= 		ExStringUtils.trimToEmpty(request.getParameter("teacherids"));
			do{
				if(ExStringUtils.isNotBlank(tClasses.getResourceid())){ //--------------------更新
					TeachCourseClasses persisttClasses = teachCourseClassesService.get(tClasses.getResourceid());
					ExBeanUtils.copyProperties(persisttClasses, tClasses);
					persisttClasses.setUpdateDate(new Date());
					persisttClasses.setOperator(SpringSecurityHelper.getCurrentUser());
					persisttClasses.setOperatorName(SpringSecurityHelper.getCurrentUserName());
					tClasses = persisttClasses;
				}
				User teacher = userService.get(teacherids);
				if(teacher!=null){
					tClasses.setRecordScorer(teacher);
					tClasses.setRecordScorerName(teacher.getCnName());
				}
				tClasses.setUpdateDate(new Date());
				tClasses.setOperator(SpringSecurityHelper.getCurrentUser());
				tClasses.setOperatorName(SpringSecurityHelper.getCurrentUserName());
				teachCourseClassesService.saveOrUpdate(tClasses);
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
	 * 删除
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/teachCourseClasses/delete.html")
	public void deleteTeachClasses(HttpServletRequest request,HttpServletResponse response){
		String teachCourseid = request.getParameter("teachCourseid");
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			TeachCourse tCourse = null;
			TeachCourseClasses tcClasses = null;
			if(ExStringUtils.isNotBlank(resourceid)){
				//TeachCourseClasses tCourseClasses = teachCourseClassesService.get(resourceid);
				//TeachCourse tCourse = tCourseClasses.getTeachCourse();
				String hql = " from "+TeachCourseClasses.class.getSimpleName()+" where isDeleted=0 and teachCourse.resourceid=? ";
				List<TeachCourseClasses> tCourseClasses = teachCourseClassesService.findByHql(hql, teachCourseid);
				if(tCourseClasses!=null && tCourseClasses.size()>0){
					tCourse = tCourseClasses.get(0).getTeachCourse();
					for (TeachCourseClasses teachCourseClasses : tCourseClasses) {
						if(teachCourseClasses.getResourceid().equals(resourceid)){
							tcClasses = teachCourseClasses;
							break;
						}
					}
				}
				if(tCourseClasses.size()<3 && tCourseClasses.size()>=0){
					teachCourseClassesService.batchDelete(tCourseClasses);
					teachCourseService.delete(teachCourseid);
				}else{
					teachCourseClassesService.delete(resourceid);
					tCourse.setClassNames(tCourse.getClassNames().replace(tcClasses.getClasses().getClassname(), ""));
					teachCourseService.update(tCourse);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("navTabId", "RES_ARRANGE_TEACHCOURSE_VIEW");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 设置登分老师
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/arrangeCourseResult/setRecordTeacher.html")
	public void setRecordTeacher(HttpServletRequest request,HttpServletResponse response) {
		String teachClassesid	=		ExStringUtils.trim(request.getParameter("teachClassesid"));
		String teacherids 		= 		ExStringUtils.trimToEmpty(request.getParameter("teacherids"));//主讲老师id
		String teacherNames 	= 		ExStringUtils.trimToEmpty(request.getParameter("teacherNames"));//老师名称
		Map<String,Object> map = new HashMap<String,Object>();
		int statusCode = 0;
		String message = "";
		if(ExStringUtils.isNotBlank(teachClassesid) && ExStringUtils.isNotBlank(teacherids)){ 
			try {
				TeachCourseClasses courseClasses = teachCourseClassesService.get(teachClassesid);
				courseClasses.setRecordScorer(userService.get(teacherids));
				courseClasses.setRecordScorerName(teacherNames);
				teachCourseClassesService.update(courseClasses);
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
