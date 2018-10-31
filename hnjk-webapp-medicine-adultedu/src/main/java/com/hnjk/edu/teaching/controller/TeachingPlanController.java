package com.hnjk.edu.teaching.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseBooks;
import com.hnjk.edu.teaching.service.ICourseBookService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;


/**
 * 教学计划(套餐)表
 * <code>TeachingPlanController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-24 下午03:58:46
 * @see 
 * @version 1.0
 */
@Controller
public class TeachingPlanController extends BaseSupportController{
	
	private static final long serialVersionUID = 7080429774647158424L;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//注入SQL支持

	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("courseBookService")
	private ICourseBookService courseBookService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingguideplanservice;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	/**
	 * 教学计划设置列表
	 * @param schoolType
	 * @param classic
	 * @param major
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplan/list.html")
	public String exeList(String orgUnitId,String classic,String major,String schoolType,String isUsed,String planName, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("major.majorCode,schoolType,classic.resourceid,versionNum");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();		
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("orgUnitId", cureentUser.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool",true);
			List<Role> roleList = new ArrayList<Role>(cureentUser.getRoles());		
			Set<String> roleModules = new HashSet<String>();
			if(null != roleList && roleList.size()>0){
				for(Role role:roleList){
					if(ExStringUtils.isNotEmpty(role.getRoleModule())){
						roleModules.add(role.getRoleModule());
					}				
				}
			}
			//如果当前角色所属教学模式不为空，则按教学模式查找		
			if(!roleModules.isEmpty()){
				condition.put("roleModules", "'"+ExStringUtils.join(roleModules, "','")+"'");
			} 
		} 
		
		if(!condition.containsKey("roleModules") && ExStringUtils.isNotEmpty(schoolType)) {
			condition.put("schoolType", schoolType);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(isUsed)) {
			condition.put("isUsed", isUsed);
		}
		if(ExStringUtils.isNotEmpty(orgUnitId)) {
			condition.put("orgUnitId", orgUnitId);
		}
		if(ExStringUtils.isNotEmpty(planName)) {
			condition.put("plan", planName);
		}
		
		//如果已经选择了学习中心，就要过滤出相应的专业
		if(ExStringUtils.isNotEmpty(orgUnitId)){
			condition.put("unitId", orgUnitId);
		}
//		List<Map<String,Object>> teachingPlanMajors = teachingPlanService.getUnitTeachingPlanMajor(condition);
//		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
//		for (Map<String,Object> m : teachingPlanMajors) {
//			if(ExStringUtils.isNotEmpty(major)&&major.equals(m.get("resourceid"))){
//				majorOption.append("<option value ='"+m.get("resourceid")+"' selected='selected' >"+m.get("majorinfo")+"</option>");
//			}else{
//				majorOption.append("<option value ='"+m.get("resourceid")+"'>"+m.get("majorinfo")+"</option>");
//			}
//		}
//		model.addAttribute("majorOption", majorOption);
		
		Page page = teachingPlanService.findTeachingPlanByCondition(condition, objPage);
		
		List<TeachingPlan> plans = teachingPlanService.findTeachingPlanByCondition(new HashMap<String,Object>());
		StringBuffer planOptions = new StringBuffer("<option value=''></option>");
		for (TeachingPlan plan : plans) {
			planOptions.append("<option value='").append(plan.getResourceid()).append("'");
			if (ExStringUtils.isNotBlank(planName) && planName.equals(plan.getResourceid())) {
				planOptions.append(" selected='selected'");
			}
			planOptions.append(">");
			if (ExStringUtils.isNotBlank(plan.getPlanName())) {
				planOptions.append(plan.getPlanName());
			} else {
				planOptions.append(plan.getMajor().getMajorName()).append("-").append(plan.getClassic().getClassicName()).append("(").append(plan.getVersionNum()).append(")");
			}
			planOptions.append("</option>");
		}
		model.addAttribute("planOptions", planOptions);
		
		model.addAttribute("planList", page);
		model.addAttribute("condition", condition);
		StringBuffer unitOption = new StringBuffer("<option value=''></option>");
		//List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		List<OrgUnit> orgList = orgUnitService.findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitCode asc ");
		if(null != orgList && orgList.size()>0){
			for(OrgUnit orgUnit : orgList){
				if(ExStringUtils.isNotEmpty(orgUnitId)&&orgUnitId.equals(orgUnit.getResourceid())){
					unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}else{
					unitOption.append("<option value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}
			}
		}
		model.addAttribute("unitOption", unitOption);
		model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(null,major,"teachingplan_major","major","searchExamResultMajorClick()",classic));
		return "/edu3/teaching/teachingplan/teachingplan-list";
	}
	
	/**
	 * 新增编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplan/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String act = ExStringUtils.trimToEmpty(request.getParameter("act"));
		//从字典中获取学位条件
		List<Dictionary> dictionList = CacheAppManager.getChildren("DegreeCondition");
		if(null != dictionList ){
			model.addAttribute("degreeList", dictionList);
		}
		//获取办学单位根编码
		model.addAttribute("parentUnitCode",CacheAppManager.getSysConfigurationByCode("orgunit.brschool.root").getParamValue());
		//判断当前用户是否是学习中心
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("isBrschool", "brSchool");
		}
		if(ExStringUtils.isNotEmpty(act) && "view".equals(act)){
			TeachingPlan teachingPlan = teachingPlanService.get(resourceid);			
			model.addAttribute("teachingPlan", teachingPlan);
			return "/edu3/teaching/teachingplan/teachingplan-view";			
		} if(ExStringUtils.isNotEmpty(act) && "syllabusind".equals(act)){
			TeachingPlan teachingPlan = teachingPlanService.load(resourceid);	
			model.addAttribute("teachingPlan", teachingPlan);
			return "/edu3/teaching/teachingplan/syllabusind-list";
		} else {

			if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑		
				if("copy".equals(act)){//复制
					TeachingPlan oldTeachingPlan = teachingPlanService.get(resourceid);	
					TeachingPlan teachingPlan = new TeachingPlan();
					ExBeanUtils.copyProperties(teachingPlan, oldTeachingPlan);
					
					teachingPlan.setResourceid(null);
					teachingPlan.setFillinDate(new Date());
					teachingPlan.setIsUsed(Constants.BOOLEAN_NO);
					teachingPlan.setVersionNum(teachingPlanService.getNextVersionNum(teachingPlan.getMajor().getResourceid(), teachingPlan.getClassic().getResourceid()));
					teachingPlan.setTeachingPlanCourses(new LinkedHashSet<TeachingPlanCourse>(0));
					
					for (TeachingPlanCourse oldcourse : oldTeachingPlan.getTeachingPlanCourses()) {
						TeachingPlanCourse newcourse = new TeachingPlanCourse();
						ExBeanUtils.copyProperties(newcourse, oldcourse);
						newcourse.setResourceid(null);
						newcourse.setTeachingPlan(teachingPlan);
						newcourse.setTeachingPlanCourseBooks(new LinkedHashSet<TeachingPlanCourseBooks>(0));
						
						for (TeachingPlanCourseBooks oldbook : oldcourse.getTeachingPlanCourseBooks()) {
							TeachingPlanCourseBooks newbook = new TeachingPlanCourseBooks();
							ExBeanUtils.copyProperties(newbook, oldbook);
							newbook.setResourceid(null);
							newbook.setTeachingPlanCourse(newcourse);
							
							newcourse.getTeachingPlanCourseBooks().add(newbook);
						}
						
						teachingPlan.getTeachingPlanCourses().add(newcourse);
					}
					
					teachingPlanService.saveOrUpdate(teachingPlan);
					model.addAttribute("teachingPlan", teachingPlan);
				} else {
					TeachingPlan teachingPlan = teachingPlanService.load(resourceid);				
					model.addAttribute("teachingPlan", teachingPlan);
				}				
			}else{ //----------------------------------------新增
				TeachingPlan teachingPlan = new TeachingPlan();
				if(cureentUser.getOrgUnit().getUnitType().
						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
					
					teachingPlan.setOrgUnit(cureentUser.getOrgUnit());
				}
				teachingPlan.setEduYear("3");
				teachingPlan.setMinResult(0d);
				teachingPlan.setTheGraduationScore(0d);
				model.addAttribute("teachingPlan", teachingPlan);			
			}
			//tab 切换
			model.addAttribute("tabIndex", ExStringUtils.trimToEmpty(request.getParameter("tabIndex")));
			//查询课程列表
			List<Course> courseList = courseService.findByHql("from "+Course.class.getName()+" where isDeleted = ? and status = ? order by courseCode", 0,1L);
			model.addAttribute("courseList", courseList);
			TeachingPlan tpl=((TeachingPlan)model.get("teachingPlan"));
			Set<TeachingPlanCourse> tc=tpl.getTeachingPlanCourses();
			int sumtime=0;
			int mstime=0;
			int eptime=0;
			double xf= 0L;
			for (TeachingPlanCourse t : tc) {
				sumtime+=t.getStydyHour()==null?0:t.getStydyHour();
				mstime+=t.getFaceStudyHour()==null?0:t.getFaceStudyHour();
				eptime+=t.getExperimentPeriod()==null?0:t.getExperimentPeriod();
				xf+=t.getCreditHour()==null?0:t.getCreditHour();
			}
			model.addAttribute("sum", sumtime);
			model.addAttribute("ms", mstime);
			model.addAttribute("ep", eptime);
			model.addAttribute("xf",xf);
			return "/edu3/teaching/teachingplan/teachingplan-form";
		}
	}
	
	/**
	 * 设置选修门数
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplan/setElectiveNum.html")
	public String setElectiveNum(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String teachingPlanIds = request.getParameter("teachingPlanIds");
		model.addAttribute("teachingPlanIds", teachingPlanIds);
		return "/edu3/teaching/teachingplan/setNum";
	}
	
	/**
	 * 设置选修门数
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplan/saveElectiveNum.html")
	public void saveElectiveNum(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String teachingPlanIds = request.getParameter("teachingPlanIds");
		int num = Integer.valueOf(ExStringUtils.trimToEmpty(request.getParameter("num")));
		Map<String,Object> map        = new HashMap<String, Object>();
		String msg                    = "";
		int statusCode = 200;
		try {
			String sql = " update edu_teach_plan p set p.optionalCourseNum=:num where p.resourceid in("+ExStringUtils.addSymbol(teachingPlanIds.split(","), "'", "'")+")";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("num", num);
			int count = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, param);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.UPDATE,"设置选修课程门数：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			msg                = "设置成功！";
		} catch (Exception e) {
			logger.error(e.getMessage());
			statusCode = 300;
			msg                = "设置失败：";
		}
		map.put("message", msg);
		map.put("statusCode", statusCode);
		map.put("navTabId", "RES_TEACHING_ESTAB_PLAN");
		renderJson(response,JsonUtils.mapToJson(map));
	}
	/**
	 * 修改教学计划版本号
	 */
	@RequestMapping("/edu3/framework/teaching/teachingplan/editNum.html")
	public void editNum(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String id=ExStringUtils.trimToEmpty(request.getParameter("id"));
		int num=Integer.valueOf(ExStringUtils.trimToEmpty(request.getParameter("num")));
		Map<String,Object> map        = new HashMap<String, Object>();
		String success                = Constants.BOOLEAN_NO;
		String msg                    = "";
		if(ExStringUtils.isNotEmpty(id)){
			try {
				TeachingPlan teachingPlan = teachingPlanService.get(id);
				teachingPlan.setVersionNum(num);
				teachingPlanService.update(teachingPlan);
				success			   = Constants.BOOLEAN_YES;
				msg                = "修改成功！";
				map.put("num", num);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.UPDATE,"修改教学计划版本号：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			} catch (Exception e) {
				logger.error("修改版本出错：{}"+e.fillInStackTrace());
				msg                = "修改失败："+e.getMessage();
				success			   = Constants.BOOLEAN_NO;
			} 
			
		}
		map.put("msg", msg);
		map.put("success", success);
		renderJson(response,JsonUtils.mapToJson(map));
		
	}
	
	/**
	 * 编辑教学计划课程
	 * @param resourceid
	 * @param teachingCourseId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/teachingplan/teachingcourse/edit.html")
	public String exeEditChild(String teachingPlanId,String teachingCourseId,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotEmpty(teachingPlanId)){
			if(ExStringUtils.isNotEmpty(teachingCourseId)){//编辑
				TeachingPlanCourse teachingPlanCourse = teachingPlanCourseService.get(teachingCourseId);
				model.addAttribute("teachingPlanCourse", teachingPlanCourse);
			}
			TeachingPlan teachingPlan = teachingPlanService.get(teachingPlanId);		
			model.addAttribute("teachingPlan", teachingPlan);
		}
		ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
		String showRankScore=property.getProperty("showRankScore");
		model.addAttribute("showRankScore", showRankScore);
		return "/edu3/teaching/teachingplan/teachingcourse-form";
	}
	
	/**
	 * 保存教学计划课程
	 * @param teachingPlanCourse
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/framework/teaching/teachingplan/teachingcourse/save.html")
	public void exeSaveTeachingCourse(TeachingPlanCourse teachingPlanCourse,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String teachingPlanId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanId"));		
		Map<String,Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotEmpty(teachingPlanId)){
			try {
				do{
					TeachingPlan teachingPlan = teachingPlanService.get(teachingPlanId);
					String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
					String beforeCourseId = ExStringUtils.trimToEmpty(request.getParameter("beforeCourseId"));
					//String courseBookId = ExStringUtils.trimToEmpty(request.getParameter("courseBookId"));
					if(teachingPlanCourse.getStydyHour()==null) {
						teachingPlanCourse.setStydyHour(0L);
					}
					if(teachingPlanCourse.getFaceStudyHour()==null) {
						teachingPlanCourse.setFaceStudyHour(0L);
					}
					if(teachingPlanCourse.getExperimentPeriod()==null) {
						teachingPlanCourse.setExperimentPeriod(0L);
					}
					if(teachingPlanCourse.getSelfStudyHour()==null) {
						teachingPlanCourse.setSelfStudyHour(0L);
					}
					// 计算总学时
					Long totalStudyHour = teachingPlanCourse.getFaceStudyHour() + teachingPlanCourse.getExperimentPeriod() +teachingPlanCourse.getSelfStudyHour();
					if(teachingPlanCourse.getStydyHour()< totalStudyHour){
						map.put("statusCode", 300);
						map.put("message", "（面授学时+实验学时+自学学时）不能超过总学时");
						break;
					}
					if(ExStringUtils.isNotEmpty(teachingPlanCourse.getResourceid())){//编辑
						TeachingPlanCourse pTeachingPlanCourse = teachingPlanCourseService.get(teachingPlanCourse.getResourceid());
						
						if(ExStringUtils.isNotEmpty(courseId)){
							teachingPlanCourse.setCourse(courseService.get(courseId));
						}
						
						if(ExStringUtils.isNotEmpty(beforeCourseId)){
							teachingPlanCourse.setBeforeCourse(courseService.get(beforeCourseId));
						}
						
						//if(ExStringUtils.isNotEmpty(courseBookId)){
						//	pTeachingPlanCourse.setCourseBook(courseBookService.get(courseBookId));
						//}
						
						teachingPlanCourse.setTeachingPlan(teachingPlan);
						ExBeanUtils.copyProperties(pTeachingPlanCourse, teachingPlanCourse);
						teachingPlanCourseService.update(pTeachingPlanCourse);
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.UPDATE,"修改教学计划课程：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
						map.put("navTabId", "RES_TEACHING_ESTAB_PLAN_EDIT");
						map.put("message", "保存成功！");	
						map.put("reloadTabUrl",  request.getContextPath() +"/edu3/teaching/teachingplan/edit.html?resourceid="+teachingPlan.getResourceid()+"&tabIndex=1");
						map.put("reloadUrl", request.getContextPath() +"/edu3/framework/teaching/teachingplan/teachingcourse/edit.html?teachingPlanId="+teachingPlan.getResourceid()+"&teachingCourseId="+teachingPlanCourse.getResourceid());
					}else{////新增			
						if(ExStringUtils.isNotEmpty(courseId)){
							teachingPlanCourse.setCourse(courseService.get(courseId));
						}
						
						if(ExStringUtils.isNotEmpty(beforeCourseId)){
							teachingPlanCourse.setBeforeCourse(courseService.get(beforeCourseId));
						}
						//if(ExStringUtils.isNotEmpty(courseBookId)){
						//	teachingPlanCourse.setCourseBook(courseBookService.get(courseBookId));
						//}
						
						teachingPlanCourse.setTeachingPlan(teachingPlan);					
						teachingPlan.getTeachingPlanCourses().add(teachingPlanCourse);
						
						teachingPlanService.update(teachingPlan);
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.INSERT,"新增教学计划课程：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
						//如果新增，则刷新NAVTAB
						map.put("navTabId", "RES_TEACHING_ESTAB_PLAN_EDIT");	
						map.put("reloadTabUrl",  request.getContextPath() +"/edu3/teaching/teachingplan/edit.html?resourceid="+teachingPlan.getResourceid()+"&tabIndex=1");
						map.put("message", "保存成功！<br/>请输入下一课程.");	
						map.put("reloadUrl", request.getContextPath() +"/edu3/framework/teaching/teachingplan/teachingcourse/edit.html?teachingPlanId="+teachingPlan.getResourceid());
					}
					
					map.put("statusCode", 200);										
				} while(false);
			} catch (Exception e) {
				logger.error("保存出错：{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "保存失败！");
			}
			
			renderJson(response, JsonUtils.mapToJson(map));
		}
	}
	
	/**
	 *删除教学计划课程 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/teachingplan/teachingcourse/delete.html")
	public void exeDeleteTeachingCourse(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String[] teachingCourseIds = request.getParameter("teachingCourseId").split(",");
		String teachingPlanId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanId"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(teachingPlanId) && null != teachingCourseIds){			
				TeachingPlan teachingPlan = teachingPlanService.get(teachingPlanId);				
				for(String tid : teachingCourseIds){
					TeachingPlanCourse teachingPlanCourse = teachingPlanCourseService.get(tid);
					teachingPlan.getTeachingPlanCourses().remove(teachingPlanCourse);
				}
				teachingPlanService.update(teachingPlan);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.DELETE,"删除教学计划课程：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("navTabId", "RES_TEACHING_ESTAB_PLAN_EDIT");	
				map.put("callbackType", "forward");
				map.put("forwardUrl",  request.getContextPath() +"/edu3/teaching/teachingplan/edit.html?resourceid="+teachingPlanId+"&tabIndex=1");
			}			
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+"选择的课程中，有课程已有成绩，不允许删除!");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 保存更新表单
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplan/save.html")
	public void exeSave(TeachingPlan teachingPlan,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String brSchoolId = ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		if(ExStringUtils.isNotEmpty(brSchoolId)){
			teachingPlan.setOrgUnit(orgUnitService.get(brSchoolId));
		}
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(teachingPlan.getResourceid())){ //--------------------更新
				TeachingPlan p_teachingPlan = teachingPlanService.get(teachingPlan.getResourceid());
				
				teachingPlan.setTrainingTarget(teachingPlan.getTrainingTarget());
				//relationChildMethod(p_teachingPlan , request);
				if(ExStringUtils.isNotEmpty(teachingPlan.getClassicId())){
					Classic classic = classicService.get(teachingPlan.getClassicId());
					teachingPlan.setClassic(classic);
				}
				if(ExStringUtils.isNotEmpty(teachingPlan.getMajorId())){
					Major major = majorService.get(teachingPlan.getMajorId());
					teachingPlan.setMajor(major);
				}
				ExBeanUtils.copyProperties(p_teachingPlan, teachingPlan);
				teachingPlanService.update(p_teachingPlan);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.UPDATE,"修改教学计划内容（非课程）：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}else{ //-------------------------------------------------------------------新增
				//如果存在相同专业和相同层次，则版本数+1
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("majorId", teachingPlan.getMajorId());
				parameters.put("classicId", teachingPlan.getClassicId());
				Long maxVersinNum = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong("select max(versionNum) from edu_teach_plan where " +
						"isDeleted = 0 and majorId = :majorId and classicId = :classicId ", parameters);
				if(maxVersinNum>0){
					teachingPlan.setVersionNum(maxVersinNum.intValue()+1);
				}
				if(ExStringUtils.isNotEmpty(teachingPlan.getClassicId())){
					Classic classic = classicService.get(teachingPlan.getClassicId());
					teachingPlan.setClassic(classic);
				}
				if(ExStringUtils.isNotEmpty(teachingPlan.getMajorId())){
					Major major = majorService.get(teachingPlan.getMajorId());
					teachingPlan.setMajor(major);
				}
				//relationChildMethod(teachingPlan , request);
				
				teachingPlanService.save(teachingPlan);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.INSERT,"新增教学计划内容（非课程）：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_PLAN");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingplan/edit.html?resourceid="+teachingPlan.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	
	/**
	 * 删除列表对象
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingplan/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));		
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					teachingPlanService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					teachingPlanService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "12", UserOperationLogs.DELETE,"删除基础教学计划：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachingplan/list.html");
			}			
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 学生教学计划补录-列表
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/teaching/teachingplan/makeupon-list.html")
	public String makeUpOnTeachingPlanForStuList(HttpServletRequest request,Page objPage,ModelMap model)throws WebException{
		
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.DESC);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		List <StudentInfo> stuList   = new ArrayList<StudentInfo>();
		
		String gradeid      = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String majorid      = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic      = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String schoolType   = ExStringUtils.trimToEmpty(request.getParameter("schoolType"));
		String name      	= ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo      = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String flag         = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		
		condition.put("major", majorid);
		condition.put("classic", classic);
		condition.put("gradeid", gradeid);
		condition.put("schoolType",schoolType);
		condition.put("makeUpTeachingPlanOn", "Y");
		
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("isBranchSchool", true);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if (ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("matriculateNoticeNo", studyNo);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		
		if (ExStringUtils.isNotEmpty(gradeid)&&ExStringUtils.isNotEmpty(majorid)&&ExStringUtils.isNotEmpty(classic)&&ExStringUtils.isNotEmpty(schoolType)) {

			Page page = studentinfoservice.findStuByCondition(condition, objPage);
			model.put("page", page);
			condition = teachingguideplanservice.getGuideTeachingPlanInfo(condition);
		}
		model.put("condition", condition);
		
		return "/edu3/teaching/teachingplan/stuTeachingPlanMakeUp-list";
	}
	
	/**
	 * 学生教学计划补录-保存
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/teachingplan/makeupon-sava.html")
	public void makeUpOnTeachingPlanForStuSave(String teachingPlanId,String stuIds,String gradeid,
			 							String majorid,String classic,HttpServletResponse response){
		
		Map <String,Object> map = new HashMap<String, Object>();
							map.put("gradeid",gradeid);
							map.put("majorid", majorid);
							map.put("classic", classic);
							
		TeachingPlan teachingPlan = teachingPlanService.get(teachingPlanId);
		List<StudentInfo> stuList = studentInfoService.findStuListByIds(stuIds);
		boolean isSuccess         = false;
		String msg                = "";
		if (null!=stuList&&!stuList.isEmpty()) {
			try {
				for (StudentInfo stu :stuList) {
					if (null!=teachingPlan.getOrgUnit()&&null!=stu.getBranchSchool()&&
						!stu.getBranchSchool().getResourceid().equals(teachingPlan.getOrgUnit().getResourceid())) {
						throw new WebException("不能将<font color='red'>"+stu.getBranchSchool().getUnitShortName()+"</font>的学生补录到<font color='red'>"+teachingPlan.getOrgUnit().getUnitShortName()+"</font>的教学计划！");
					}
					stu.setTeachingPlan(teachingPlan);
					studentInfoService.saveOrUpdate(stu);
				}
				msg 	 		  = "操作成功!";
				isSuccess 		  = true;
			} catch (Exception e) {
				logger.error("补录教学计划出错:{}",e.fillInStackTrace());
				isSuccess     	  = false;
				msg            	  = "操作出错:"+e.getMessage();
			}
		}else {
			msg 	  = "请选择要补录教学计划的学生!";
			isSuccess = false;
		}
		map.put("msg",msg);
		map.put("makeupteachingPlan", isSuccess);
		renderJson(response,JsonUtils.mapToJson(map));
	}
	/**
	 * 能力指标列表
	 * @param teachingPlanCourseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/teaching/teachingplan/syllabusind/list.html")
	public String listSyllabusind(String teachingPlanCourseId,ModelMap model) throws WebException{
		if(ExStringUtils.isNotEmpty(teachingPlanCourseId)){
			TeachingPlanCourse teachingPlanCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			model.addAttribute("teachingPlanCourse", teachingPlanCourse);
			List<Syllabus> syllabusList =  syllabusService.findSyllabusTreeList(teachingPlanCourse.getCourse().getResourceid());	
			if(syllabusList!=null && !syllabusList.isEmpty()){
				Map<String,String> map = syllabusindService.getSyllabusind(teachingPlanCourseId);
				if(!map.isEmpty()){
					Syllabus root = syllabusService.getSyllabusRoot(teachingPlanCourse.getCourse().getResourceid());
					map.put(root.getResourceid(), "");
				}
				String jsonString = JsonUtils.objectToJson(SyllabusServiceImpl.getSyllabusTree(syllabusList,map,true));
				model.addAttribute("syllabusTree", jsonString);
			}
		}			
		return "/edu3/teaching/teachingplan/syllabusind-form";
	}
	*/
	/**
	 * 保存能力指标
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/teaching/teachingplan/syllabusind/save.html")
	public void saveSyllabusind(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String teachingPlanCourseId = request.getParameter("teachingPlanCourseId");
			String[] syllabusIds = request.getParameterValues("syllabusId");
			String[] isCheckeds = request.getParameterValues("isChecked");
			String[] requireds = request.getParameterValues("required");
			String[] abilityTargets = request.getParameterValues("abilityTarget");
			if(ExStringUtils.isNotEmpty(teachingPlanCourseId)){
				TeachingPlanCourse teachingPlanCourse = teachingPlanCourseService.load(teachingPlanCourseId);
				List<Syllabusind> list = new ArrayList<Syllabusind>(0);
				if(null!=syllabusIds && syllabusIds.length>0){
					for (int i = 0; i < syllabusIds.length; i++) {
						if("1".equals(isCheckeds[i])){ //只处理选中的
							Syllabus syllabus = syllabusService.load(syllabusIds[i]);
							
							Syllabusind syllabusind = new Syllabusind();
							syllabusind.setTeachingPlanCourse(teachingPlanCourse);
							syllabusind.setSyllabus(syllabus);
							syllabusind.setAbilityTarget(abilityTargets[i]);
							syllabusind.setRequired(requireds[i]);
							
							list.add(syllabusind);
						}						
					}
				}				
				syllabusindService.batchSaveOrUpdate(teachingPlanCourse.getResourceid(),list);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");				
				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingplan/syllabusind/list.html?teachingPlanCourseId="+teachingPlanCourse.getResourceid());
			}
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	
	@RequestMapping("/edu3/framework/teaching/teachingplan/teachingcourse/rel.html")
	public String relCourseBook(String teachingPlanId,String teachingCourseId,HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("course.courseCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
				
		String schoolType = ExStringUtils.trimToEmpty(request.getParameter("schoolType"));
	
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String courseName = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
		String courseCode = ExStringUtils.trimToEmpty(request.getParameter("courseCode"));
		String bookName = ExStringUtils.trimToEmpty(request.getParameter("bookName"));
			
		if(ExStringUtils.isNotEmpty(courseName)){
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(courseCode)){
			condition.put("courseCode", courseCode);
		}
		if(ExStringUtils.isNotEmpty(bookName)){
			condition.put("bookName", bookName);
		}
		
		if(ExStringUtils.isNotEmpty(schoolType)){
			condition.put("schoolType", schoolType);
		}
		
		
		condition.put("teachingPlanId", teachingPlanId);
		condition.put("teachingCourseId", teachingCourseId);
		model.addAttribute("condition", condition);
		
		condition.put("status", 1);//启用
		TeachingPlanCourse teachingCourse =  teachingPlanCourseService.get(teachingCourseId);
		
		List<String> hasUsedCoursebookId = new ArrayList<String>();
		if(!teachingCourse.getTeachingPlanCourseBooks().isEmpty()){
			for(TeachingPlanCourseBooks book:teachingCourse.getTeachingPlanCourseBooks()){
				hasUsedCoursebookId.add(book.getCourseBook().getResourceid());
			}
			condition.put("hasUsedCoursebookId",hasUsedCoursebookId);
		}
		
		
		Page coursebookListPage = courseBookService.findCourseBookByCondition(condition, objPage);
		
		
		model.addAttribute("teachingCourse", teachingCourse);
		model.addAttribute("coursebookListPage", coursebookListPage);
		condition.remove("hasUsedCoursebookId");
		return "/edu3/teaching/teachingplan/teachingcoursebook-form";
	}
	
	/**
	 * 保存教学计划教材
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teaching/teachingplan/teachingcourse/rel/save.html")
	public void saveRelCourseBook(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String teachingPlanId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanId"));
		TeachingPlan plan = teachingPlanService.get(teachingPlanId);
		
		try{
			String teachingCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingCourseId"));
			String[] teachingCourseBookId = ExStringUtils.trimToEmpty(request.getParameter("teachingCourseBookId")).split("\\,");
			
			TeachingPlanCourse teachingCourse =  teachingPlanCourseService.get(teachingCourseId);
			Set<TeachingPlanCourseBooks> teachingPlanCourseBooks = teachingCourse.getTeachingPlanCourseBooks();
			for(int i=0;i<teachingCourseBookId.length;i++){
				TeachingPlanCourseBooks book = new TeachingPlanCourseBooks();
				book.setCourseBook(courseBookService.get(teachingCourseBookId[i]));
				book.setTeachingPlanCourse(teachingCourse);
				teachingPlanCourseBooks.add(book);
			}
			plan.getTeachingPlanCourses().add(teachingCourse);
			teachingPlanService.update(plan);
			map.put("statusCode", 200);
			map.put("message", "添加成功！");				
			map.put("navTabId", "RES_TEACHING_ESTAB_PLAN_EDIT");	
			map.put("callbackType", "forward");
			map.put("dialog", "teachingPlanCourseBook");
			map.put("forwardUrl",  request.getContextPath() +"/edu3/teaching/teachingplan/edit.html?resourceid="+teachingPlanId+"&tabIndex=1");
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));		
		
	}
	
	@RequestMapping("/edu3/framework/teaching/teachingplan/teachingcourse/rel/delete.html")
	public void deleteRelCourseBook(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String teachingPlanId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanId"));
		TeachingPlan plan = teachingPlanService.get(teachingPlanId);
		
		try{
			String teachingCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingCourseId"));
			String[] teachingCourseBookId = ExStringUtils.trimToEmpty(request.getParameter("teachingCourseBookId")).split("\\,");
			
			TeachingPlanCourse teachingCourse =  teachingPlanCourseService.get(teachingCourseId);
			Set<TeachingPlanCourseBooks> teachingPlanCourseBooks = teachingCourse.getTeachingPlanCourseBooks();
			
			for(int i=0;i<teachingCourseBookId.length;i++){
				for(TeachingPlanCourseBooks books : teachingPlanCourseBooks){
					if(books.getResourceid().equals(teachingCourseBookId[i])){						
						teachingPlanCourseBooks.remove(books);
						break;
					}
				}					
			}
				
			
			plan.getTeachingPlanCourses().add(teachingCourse);
			teachingPlanService.update(plan);
			map.put("statusCode", 200);
			map.put("message", "删除成功！");				
			map.put("navTabId", "RES_TEACHING_ESTAB_PLAN_EDIT");	
			map.put("callbackType", "forward");
			map.put("dialog", "teachingPlanCourseBook");
			map.put("forwardUrl",  request.getContextPath() +"/edu3/teaching/teachingplan/edit.html?resourceid="+teachingPlanId+"&tabIndex=1");
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));		
		
	}
	
}
