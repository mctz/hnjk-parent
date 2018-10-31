package com.hnjk.edu.learning.controller;

import java.io.File;
import java.math.BigDecimal;
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

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.BbsReply;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.BbsUserInfo;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IBbsReplyService;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.ILearningTimeSettingService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.LearningTimeSetting;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.service.IUsualResultsService;
//import com.hnjk.edu.teaching.service.ITeachTaskService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 论坛帖子管理
 * <code>BbsTopicController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-7 上午11:56:42
 * @see 
 * @version 1.0
 */
@Controller
public class BbsTopicController extends FileUploadAndDownloadSupportController {
	private static final long serialVersionUID = -251967200227054624L;
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("bbsSectionService")
	private IBbsSectionService bbsSectionService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao; 
	
	@Autowired
	@Qualifier("bbsReplyService")
	private IBbsReplyService bbsReplyService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("learningTimeSettingService")
	private ILearningTimeSettingService learningTimeSettingService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	/**
	 * 论坛帖子列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws ParseException 
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/list.html")
	public String listBbsTopic(HttpServletRequest request, Page objPage, ModelMap model) throws WebException, ParseException{
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String orderType = ExStringUtils.trimToEmpty(request.getParameter("orderType"));
		if(StringUtils.isNotEmpty(orderBy) && StringUtils.isNotEmpty(orderType)){//如果排序条件不为空，则加入排序
			objPage.setOrderBy(orderBy);
			objPage.setOrder(orderType);//设置默认排序方式	
		}else{
			objPage.setOrderBy("fillinDate");
			objPage.setOrder(Page.DESC);//设置默认排序方式	
		}	
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));//高级查询页面
		
//		String hql = " from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and parent is null ";
		User user = SpringSecurityHelper.getCurrentUser();
		if(user != null && !SpringSecurityHelper.isUserInRole("ROLE_ADMIN")){ //非管理员
			String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
			if(SpringSecurityHelper.isUserInRole(roleCode)){//老师
//				hql += " and isCourseSection='Y' ";
				condition.put("teacherId", user.getResourceid());
			} else if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue())){//学生
				condition.put("masterId", user.getResourceid());
			} 
		}		
		
//		Map<String, Object> map = bbsSectionService.getSectionList(hql);
//		model.addAttribute("parentBbsSections", map);
		
		String bbsSectionId = request.getParameter("bbsSectionId");
		String title = request.getParameter("title");
		String topicType = request.getParameter("topicType");
		String status = request.getParameter("status");
		String fillinDateStartStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateStartStr"));
		String fillinDateEndStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateEndStr"));
		String fillinMan = ExStringUtils.trimToEmpty(request.getParameter("fillinMan"));
		if(ExStringUtils.isNotBlank(fillinMan)){
			condition.put("fillinMan", fillinMan);
		}		
		
		if(ExStringUtils.isNotEmpty(fillinDateStartStr)){
			condition.put("fillinDateStartStr", fillinDateStartStr);
			condition.put("fillinDateStart", ExDateUtils.convertToDate(fillinDateStartStr).getTime());
		}
		if(ExStringUtils.isNotEmpty(fillinDateEndStr)){
			condition.put("fillinDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(fillinDateEndStr), 1).getTime());
			condition.put("fillinDateEndStr", fillinDateEndStr);
		}
				
		condition.put("parenTopic", "null");		
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue();
		condition.put("notSectionCode", sectionCode);
		String feedbackSectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();//获取版块编码
		if(ExStringUtils.isNotBlank(feedbackSectionCode)){
			condition.put("feedbackSectionCode", feedbackSectionCode);
		}
		if(ExStringUtils.isNotEmpty(bbsSectionId)) {
			condition.put("bbsSectionId", bbsSectionId);
		}
		if(ExStringUtils.isNotEmpty(title)) {
			condition.put("title", title);
		}
		if(ExStringUtils.isNotEmpty(topicType)) {
			condition.put("topicType", topicType);
		}
		if(ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);
		}
		
		model.addAttribute("condition", condition);
		
		if ("advance".equals(advanseCon)) {
			return "/edu3/learning/bbstopic/bbstopic-search";// 返回到高级检索
		}
		
		Page bbsTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
		
		model.addAttribute("bbsTopicListPage", bbsTopicListPage);
		
		return "/edu3/learning/bbstopic/bbstopic-list";
	}
	/**
	 * 随堂问答列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/interlocution/bbstopic/list.html")
	public String listInterlocution(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("bbsUserInfo.sysUser.orgUnit.unitCode, course.courseCode,fillinMan,fillinDate desc,resourceid");
		objPage.setOrder(Page.DESC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
				
		String title = ExStringUtils.trimToEmpty(request.getParameter("title"));
		String status = ExStringUtils.trimToEmpty(request.getParameter("status"));	
		String courseName = ExStringUtils.trimToEmpty(request.getParameter("courseName"));	
		String isAnswered = ExStringUtils.trimToEmpty(request.getParameter("isAnswered"));
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String courseId = request.getParameter("courseId");
		String classesId = request.getParameter("classesId");
		String orgUnitId = request.getParameter("orgUnitId");
		String content = request.getParameter("content");
		String fillinMan = request.getParameter("fillinMan");
		String startLong = request.getParameter("startLong");
		String endLong = request.getParameter("endLong");
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		// 获取该用户作为班主任的所有班级
		String classesIds = "";
		try {
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				String teacherId = "";
				classesIds = classesService.findByMasterId(user.getResourceid());
				if(ExStringUtils.isNotEmpty(classesIds)){
					condition.put("classesIds",Arrays.asList(classesIds.split(",")));
				}
				
				if(SpringSecurityHelper.isUserInRole(roleCode)){// 该用户为老师
					teacherId = user.getResourceid();	
				}
				if(ExStringUtils.isNotBlank(teacherId)){
					condition.put("teacherId",teacherId);
				}
			}else{
				condition.put("schoolId",user.getOrgUnit().getResourceid());
			}
			
			/*List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			StringBuffer SBFcourse = new StringBuffer("<select class='flexselect' id='interlocution_courseId' name='courseId' style='width:55%;'>");
			SBFcourse.append("<option value=''>请选择</option>");
			if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
				for(Map<String, Object> map : list){
					if(map.get("courseid").equals(courseId)){
						SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursename")+"</option>");
					}else{
						SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursename")+"</option>");
					}
				}
			}
			SBFcourse.append("</select>");
			model.put("interlocutionlistCourseSelect", SBFcourse.toString());*/
		} catch (Exception e) {
			e.printStackTrace();
		}
		condition.clear();
		
		if(ExStringUtils.isNotEmpty(title)){
			condition.put("title", title);		
		}
		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", status);	
		}
		if(ExStringUtils.isNotEmpty(courseName)){
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(isAnswered)){
			condition.put("isAnswered", isAnswered);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)){
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(orgUnitId)){
			condition.put("orgUnitId", orgUnitId);
		}
		if(ExStringUtils.isNotEmpty(content)){
			condition.put("content", content);
		}
		if(ExStringUtils.isNotEmpty(fillinMan)){
			condition.put("fillinMan", fillinMan);
		}
		if(ExStringUtils.isNotEmpty(startLong)){
			condition.put("startLong", startLong);
		}
		if(ExStringUtils.isNotEmpty(endLong)){
			condition.put("endLong", endLong);
		}
		condition.put("studentStatus", "'11','16','25'");
		condition.put("topicType", "2");//提问
		condition.put("notEmptySyllabus", "Y");//关联的知识节点不为空
		
		boolean isTeacher = SpringSecurityHelper.isUserInRole(roleCode);
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode").getParamValue();//获取版块编码
		condition.put("sectionCode", sectionCode);
		if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
			if(isTeacher){
				condition.put("teacherId", user.getResourceid());	
				condition.put("isFilterYearInfo", Constants.BOOLEAN_YES);
				model.addAttribute("user", user);
			}		
			if(ExStringUtils.isNotEmpty(classesIds)){// 班主任
				condition.put("classesIds",Arrays.asList(classesIds.split(",")));
			}
		} else {// 教务员
			condition.put("schoolId",user.getOrgUnit().getResourceid());
		}
		
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("isBrschool", true);
		}
		Page bbsTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
		
		model.addAttribute("bbsTopicListPage", bbsTopicListPage);
		model.addAttribute("condition", condition);
		
		String classesCondition = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(user)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and (resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+user.getResourceid()+"') "
										   + " or classesmasterid='"+user.getResourceid()+"') ";
				/*if(ExStringUtils.isNotEmpty(branchSchoolId)){
					classesCondition += " and brSchool.resourceid='"+branchSchoolId+"' ";
				}*/
			} else{
				classesCondition += " and brSchool.resourceid='"+user.getOrgUnit().getResourceid()+"' ";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		// 对这门课是否有操作权限
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/bbstopic/interlocution-list";
	}
	
	/**
	 * 是否该课程的老师
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/isCourseTeacher.html")
	public void isCourseTeacher(String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		if(ExStringUtils.isNotBlank(courseId)){		
//			User user = SpringSecurityHelper.getCurrentUser();
//			boolean isCourseTeacher = teachTaskService.isCourseTeacher(courseId, user.getResourceid(), 0);
//			renderText(response, isCourseTeacher?"Y":"N");
		}
		renderText(response, "Y");
	}
	
	/**
	 * 删除论坛帖子
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/remove.html")
	public void removeBbsTopic(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsTopicService.deleteBbsTopic(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/topicreply/bbstopic/list.html");
			}
		} catch (Exception e) {
			logger.error("删除论坛帖子出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 编辑论坛帖子
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/input.html")
	public String editBbsTopic(String resourceid, ModelMap model) throws WebException {		
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			BbsTopic bbsTopic = bbsTopicService.get(resourceid);	
			List<Attachs> attachs = attachsService.findAttachsByFormId(resourceid);
			bbsTopic.setAttachs(attachs);
			model.addAttribute("bbsTopic", bbsTopic);
			
			User user = SpringSecurityHelper.getCurrentUser();
			model.addAttribute("storeDir", user.getUsername());
			
//			String hql = " from "+BbsSection.class.getSimpleName()+" where isDeleted=0 and parent is null ";
//			Map<String, Object> map = bbsSectionService.getSectionList(hql);
//			model.addAttribute("parentBbsSections", map);
		}			
		return "/edu3/learning/bbstopic/bbstopic-form";
	}
		
	/**
	 * 保存论坛帖子
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/save.html")
	public void saveBbsTopic(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			String resourceid = request.getParameter("resourceid");
			String bbsSectionId = request.getParameter("bbsSectionId");
			String title = request.getParameter("title");
			String content = request.getParameter("content");	
			String topicType = request.getParameter("topicType");
			Integer status = new Integer(ExStringUtils.defaultIfEmpty(request.getParameter("status"), "0"));
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				BbsTopic bbsTopic = bbsTopicService.get(resourceid);
				if(ExStringUtils.isNotEmpty(bbsSectionId)&&!bbsSectionId.equals(bbsTopic.getBbsSection().getResourceid())) //移动的别的版块
				{
					bbsTopic.setBbsSection(bbsSectionService.get(bbsSectionId));
				}
				bbsTopic.setContent(content);
				bbsTopic.setTitle(title);
				bbsTopic.setStatus(status);
				bbsTopic.setTopicType(topicType);
				
				String[] uploadfileids = request.getParameterValues("uploadfileid");
				bbsTopicService.saveOrUpdateBbsTopic(bbsTopic, uploadfileids,null);
				
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_METARES_BBS_TOPIC_EDIT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/metares/topicreply/bbstopic/input.html?resourceid="+resourceid);
			}			
		}catch (Exception e) {
			logger.error("保存帖子出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 设置帖子状态
	 * @param resourceid
	 * @param status 置顶(3)、加精(1)、锁定(-1)、取消置顶(0)、取消加精(0)、解除锁定(-3)
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/status.html")
	public void statusBbsTopic(String resourceid,Integer status,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsTopicService.setBbsTopicStatus(resourceid.split("\\,"), status);				
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/topicreply/bbstopic/list.html");
			}
		} catch (Exception e) {
			logger.error("设置论坛帖子状态出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/metares/topicreply/bbstopic/move.html")
	public void moveBbsTopic(String resourceid,String sectionId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsTopicService.moveBbsTopic(resourceid.split("\\,"), sectionId);
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/topicreply/bbstopic/list.html");
			}
		} catch (Exception e) {
			logger.error("移动帖子出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 标记问题
	 * @param resourceid
	 * @param sectionId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/mark.html")
	public void markBbsTopic(String resourceid,String keywords,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsTopicService.markBbsTopic(resourceid.split("\\,"),"FAQ",keywords);
				map.put("statusCode", 200);
				map.put("message", "操作成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/interlocution/bbstopic/list.html");
			}
		} catch (Exception e) {
			logger.error("标记随堂问答出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 学生反馈列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/feedback/list.html")
	public String listFeedback(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
	
		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();//获取角色编码
		if(SpringSecurityHelper.isUserInRole(roleCode)){
			User user = SpringSecurityHelper.getCurrentUser();			
			String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();//获取版块编码
			condition.put("sectionCode", sectionCode);
			condition.put("fillinManId", user.getResourceid());				
			
			Page feedbackPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
			
			model.addAttribute("feedbackPage", feedbackPage);			
		}
		condition.put("from", from);			
		model.addAttribute("condition", condition);
		return "/edu3/learning/bbstopic/feedback-list";
	}
	/**
	 * 学生新增反馈
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/feedback/input.html")
	public String studentFeedback(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {		
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		
		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
		BbsTopic bbsTopic = new BbsTopic();
		if(ExStringUtils.isNotBlank(resourceid)){
			bbsTopic = bbsTopicService.get(resourceid);	
			model.addAttribute("bbsTopic", bbsTopic);
			return "/edu3/learning/bbstopic/feedback-view";
		}else {		
			String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			if("interactive".equals(from) || ExStringUtils.isNotBlank(courseId)){
				bbsTopic.setFacebookType("4");
				
				if (ExStringUtils.isNotEmpty(courseId)) {
					Course course = courseService.get(courseId);
					bbsTopic.setCourse(course);
					bbsTopic.setTitle(course.getCourseName()+" - 课件学习的问题反馈");
				}
			}
			
		} 
		model.addAttribute("bbsTopic", bbsTopic);
		model.addAttribute("from", from);
		
		return "/edu3/learning/bbstopic/feedback-form";
	}
	/**
	 * 保存反馈信息
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/feedback/save.html")
	public void saveFeedback(String content,String title,String from,String facebookType,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String facebookTargetType = ExStringUtils.trimToEmpty(request.getParameter("facebookTargetType"));
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(content)){		
				User user = SpringSecurityHelper.getCurrentUser();
				BbsTopic bbsTopic = new BbsTopic();
				bbsTopic.setTitle(title);
				bbsTopic.setContent(content);			
				
				String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();//获取版块编码
				BbsSection bbsSection = bbsSectionService.findUniqueByProperty("sectionCode", sectionCode);
				bbsTopic.setBbsSection(bbsSection);				
				bbsTopic.setTopicType("6");
				bbsTopic.setFacebookType(facebookType);
				
				bbsTopic.setFillinDate(new Date());
				bbsTopic.setFillinMan(user.getCnName());
				
				bbsTopic.setLastReplyDate(bbsTopic.getFillinDate());
				bbsTopic.setLastReplyMan(user.getCnName());		
				
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				if(ExStringUtils.isNotEmpty(courseId)){
					Course course = courseService.get(courseId);
					bbsTopic.setCourse(course);
				}
				bbsTopic.setTags(facebookTargetType);
				
				bbsTopicService.saveOrUpdateBbsTopic(bbsTopic, null, null);				
				map.put("statusCode", 200);
				map.put("message", "操作成功！");		
				map.put("from", ExStringUtils.trimToEmpty(from));
				map.put("navTabId", "RES_STUDENT_FEEDBACK");
				map.put("reloadUrl", request.getContextPath()+"/edu3/student/feedback/list.html?from="+from);
			}
		} catch (Exception e) {
			logger.error("学生保存反馈出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 教务人员回复反馈
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teacher/feedback/list.html")
	public String listTeacherFeedback(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String orderType = ExStringUtils.trimToEmpty(request.getParameter("orderType"));
		if(StringUtils.isNotEmpty(orderBy) && StringUtils.isNotEmpty(orderType)){//如果排序条件不为空，则加入排序
			objPage.setOrderBy(orderBy);
			objPage.setOrder(orderType);//设置默认排序方式	
		}else{
			objPage.setOrderBy("fillinDate");
			objPage.setOrder(Page.DESC);//设置默认排序方式	
		}	
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));//高级查询页面
	
		String facebookType = request.getParameter("facebookType");
		String fillinDateStartStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateStartStr"));
		String fillinDateEndStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateEndStr"));
		String fillinMan = ExStringUtils.trimToEmpty(request.getParameter("fillinMan"));
		String isAnswered = request.getParameter("isAnswered");
		String courseId = request.getParameter("courseId");
		if(ExStringUtils.isNotEmpty(facebookType)){
			condition.put("facebookType", facebookType);
		}
		if(ExStringUtils.isNotBlank(fillinMan)){
			condition.put("fillinMan", fillinMan);
		}	
		if(ExStringUtils.isNotBlank(isAnswered)){
			condition.put("isAnswered", isAnswered);
		}	
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(fillinDateStartStr)){
			condition.put("fillinDateStartStr", fillinDateStartStr);
			condition.put("fillinDateStart", ExDateUtils.convertToDate(fillinDateStartStr).getTime());
		}
		if(ExStringUtils.isNotEmpty(fillinDateEndStr)){
			condition.put("fillinDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(fillinDateEndStr), 1).getTime());
			condition.put("fillinDateEndStr", fillinDateEndStr);
		}			
		User cureentUser = SpringSecurityHelper.getCurrentUser();			
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("orgUnitId", cureentUser.getOrgUnit().getResourceid());
			condition.put("nottags", "CodeFacebookTargetType_2");
		}
		
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf("localDepart")>=0){//如果为学院用户
			condition.put("nottags", "CodeFacebookTargetType_1");
		}
		
		
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();//获取版块编码
		condition.put("sectionCode", sectionCode);			
		
		model.addAttribute("condition", condition);	
		if ("advance".equals(advanseCon)) {
			return "/edu3/learning/bbstopic/replyfeedback-search";// 返回到高级检索
		}
		Page feedbackPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
		
		model.addAttribute("feedbackPage", feedbackPage);
			
		return "/edu3/learning/bbstopic/replyfeedback-list";
	}
	/**
	 * 回复学生反馈
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teacher/feedback/input.html")
	public String teacherFeedback(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {		
		if(ExStringUtils.isNotBlank(resourceid)){
			BbsTopic bbsTopic = bbsTopicService.get(resourceid);
			model.addAttribute("bbsTopic", bbsTopic);
			
			User user = SpringSecurityHelper.getCurrentUser();
			model.addAttribute("storeDir", user.getUsername());
			
			String act = ExStringUtils.trimToEmpty(request.getParameter("act"));
			if("view".equals(act)){
				return "/edu3/learning/bbstopic/feedback-view";
			}
		}			
		return "/edu3/learning/bbstopic/replyfeedback-form";
	}
	/**
	 * 保存回复反馈信息
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teacher/feedback/save.html")
	public void saveTeacheFeedback(String resourceid,String replyContent,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){	
				User user = SpringSecurityHelper.getCurrentUser();
				BbsReply bbsReply = new BbsReply();	
				BbsTopic bbsTopic = bbsTopicService.get(resourceid);
							
				bbsReply.setReplyContent(replyContent);
				bbsReply.setReplyDate(new Date());
				bbsReply.setReplyMan(user.getCnName());			
				bbsReply.setShowOrder(bbsReplyService.getNextShowOrder(resourceid));//排序号
				bbsTopic.setLastReplyDate(bbsReply.getReplyDate());
				bbsTopic.setLastReplyMan(bbsReply.getReplyMan());
				if(bbsTopic.getReplyCount()==null) {
					bbsTopic.setReplyCount(0);
				}
				bbsTopic.setReplyCount(bbsTopic.getReplyCount().intValue()+1);
				bbsReply.setBbsTopic(bbsTopic);				
				bbsTopic.setIsAnswered(Constants.BOOLEAN_TRUE);//应答状态
				
				bbsReplyService.saveOrUpdateBbsReply(bbsReply, null);
				map.put("statusCode", 200);
				map.put("message", "操作成功！");		
				map.put("callbackType", "closeCurrent");
				map.put("navTabId", "RES_TEACHING_ESTAB_FEEDBACK");
				map.put("reloadUrl", request.getContextPath()+"/edu3/teacher/feedback/list.html");
			}
		} catch (Exception e) {
			logger.error("保存反馈出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 导出反馈问题
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teacher/feedback/export.html")
	public void exportTeachingGuidePlan(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String facebookType = request.getParameter("facebookType");
		String fillinDateStartStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateStartStr"));
		String fillinDateEndStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateEndStr"));
		String fillinMan = ExStringUtils.trimToEmpty(request.getParameter("fillinMan"));
		String isAnswered = request.getParameter("isAnswered");
		if(ExStringUtils.isNotEmpty(facebookType)){
			condition.put("facebookType", facebookType);
		}
		if(ExStringUtils.isNotBlank(fillinMan)){
			condition.put("fillinMan", fillinMan);
		}	
		if(ExStringUtils.isNotBlank(isAnswered)){
			condition.put("isAnswered", isAnswered);
		}	
		if(ExStringUtils.isNotEmpty(fillinDateStartStr)){
			condition.put("fillinDateStart", ExDateUtils.convertToDate(fillinDateStartStr).getTime());
		}
		if(ExStringUtils.isNotEmpty(fillinDateEndStr)){
			condition.put("fillinDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(fillinDateEndStr), 1).getTime());
		}			
		User cureentUser = SpringSecurityHelper.getCurrentUser();			
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("orgUnitId", cureentUser.getOrgUnit().getResourceid());
		}		
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.feedback").getParamValue();//获取版块编码
		condition.put("sectionCode", sectionCode);		
		
		List<BbsTopic> list = bbsTopicService.findBbsTopicByCondition(condition);		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeFacebookType");
			//模板文件路径
			String templateFilepathString = "feedbackTopic.xls";
			//初始化配置参数1
			exportExcelService.initParmasByfile(disFile, "feedbackTopic", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, null);
			exportExcelService.getModelToExcel().setRowHeight(500);
			exportExcelService.getModelToExcel().setNormolCellFormat(null);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
	      
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "学生问题反馈.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}

	
	/**
	 * 帖子评分列表
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/framework/bbstopic/score/list.html")
	public String listBbsTopicScore(String resourceid,HttpServletRequest request, ModelMap model) throws WebException, Exception{
		User user = SpringSecurityHelper.getCurrentUser();
		if(ExStringUtils.isNotBlank(resourceid)){
			BbsTopic bbsTopic = bbsTopicService.get(resourceid);
			model.addAttribute("bbsTopic", bbsTopic);
			int totalTopicNum = 0;
			int validTopicNum = 0;
			if(bbsTopic!=null && bbsTopic.getCourse()!=null){
				// 获取该学生该门课程的总帖数和有效贴数
				Map<String, Object> topicNum = learningJDBCService.getStuCourseTopicNum(bbsTopic.getBbsUserInfo().getSysUser().getResourceid(), bbsTopic.getCourse().getResourceid());
				if(topicNum!=null && topicNum.size() > 0){
					totalTopicNum = topicNum.get("totalTopicNum")==null?0:((BigDecimal)topicNum.get("totalTopicNum")).intValue();
					validTopicNum = topicNum.get("validTopicNum")==null?0:((BigDecimal)topicNum.get("validTopicNum")).intValue();
				}
			}
			model.addAttribute("totalTopicNum", totalTopicNum);
			model.addAttribute("validTopicNum", validTopicNum);
			List<BbsReply> replyList = bbsReplyService.findByHql("from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsTopic.resourceid=? order by replyDate asc,resourceid", bbsTopic.getResourceid());
			String isReply = "N";
			if(ExCollectionUtils.isNotEmpty(replyList)){//设置附件
				isReply = "Y";
				for (BbsReply bbsReply : replyList) {
					if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsReply.getIsAttachs())){
						List<Attachs> attachs = attachsService.findAttachsByFormId(bbsReply.getResourceid());
						bbsReply.setAttachs(attachs);
					}
					if(user.getResourceid().equals(bbsReply.getBbsUserInfo().getSysUser().getResourceid())){
						model.addAttribute("teacherReply", bbsReply);
					}
				}
			}
			model.addAttribute("isReply", isReply);
			model.addAttribute("replyList", replyList);
			
			//获取帖子相邻id
			//广大栾老师提出，点击下一个帖子时，如果在查询条件中选择了教学站，则不允许超出该教学点的范围  20170411
			String orgUnitID = request.getParameter("orgUnitID");
			
			Map<String, Object> topicMap = getTopicMap(bbsTopic, user.getResourceid(),orgUnitID);
			model.addAttribute("orgUnitID", orgUnitID);
			model.addAttribute("topicMap", topicMap);
			
			model.addAttribute("storeDir", user.getUsername());
		}
		return "/edu3/learning/bbstopic/bbstopicscore-form";
	}
	
	/**
	 * 获取帖子相邻id
	 * @param bbsTopic
	 */
	private Map<String, Object> getTopicMap(BbsTopic bbsTopic, String userId ,String orgUnitID) {
		Map<String, Object> topicMap = null;
		StringBuilder sql = new StringBuilder();
		sql.append(" select r.preresourceid,r.resourceid,r.nextresourceid from (")
		.append(" select lag(t.resourceid) over(order by t.fillindate desc,t.resourceid desc) preresourceid,t.resourceid,lead(t.resourceid) over(order by t.fillindate desc,t.resourceid desc) nextresourceid from edu_bbs_topic t ")
		.append(" inner join edu_bbs_userinfo ui on ui.isdeleted=0 and t.fillinmanid=ui.resourceid inner join hnjk_sys_usersextend ue on ue.isdeleted=0 and ui.userid=ue.sysuserid and ue.excode='defalutrollid' ")
		.append(" inner join edu_roll_studentinfo so on so.isdeleted=0 and so.resourceid=ue.exvalue ");
		if(ExStringUtils.isNotBlank(orgUnitID)){
			sql.append(" and so.branchschoolid=:orgUnitID ");
		}
		sql.append(" inner join edu_teach_timetable tt on tt.isdeleted=0 and tt.courseid=t.courseid and tt.classesid=so.classesid and tt.teacherid=:teacherId ")
		.append(" where t.isdeleted=0 and t.syllabusid is not null and t.courseid=:courseId and t.yearid=:yearInfoId and t.term=:term ");
		
		sql.append(" order by t.courseid,t.fillindate desc,t.resourceid desc ")
		.append(" ) r where r.resourceid=:resourceid ");
		
		Map<String, Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(orgUnitID)){
			condition.put("orgUnitID", orgUnitID);
		}
		
		condition.put("teacherId", userId);
		condition.put("courseId", bbsTopic.getCourse().getResourceid());
		condition.put("yearInfoId", bbsTopic.getYearInfo().getResourceid());
		condition.put("term", bbsTopic.getTerm());
		condition.put("resourceid", bbsTopic.getResourceid());
		try {
			topicMap = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql.toString(), condition);
		} catch (Exception e) {
			logger.error("获取帖子相邻id失败:{}",e);
		}
		return topicMap;
	}	
	
	/**
	 * 保存帖子评分
	 * @param resourceid
	 * @param topicType
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbstopic/score/save.html")
	public void saveBbsTopicScore(String resourceid, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			do{
				String _replyContent = ExStringUtils.trimToEmpty(request.getParameter("replyContent"));
				if(ExStringUtils.isEmpty(_replyContent)){
					map.put("statusCode", 300);
					map.put("message", "内容不能为空");
					continue;
				}
				// 判断是否包含敏感词
				SensitivewordFilter sensitivewordFilter = (SensitivewordFilter)request.getSession().getServletContext().getAttribute("sensitivewordFilter");
				Set<String> sensitivewordSet = sensitivewordFilter.getSensitiveWord(_replyContent,SensitivewordFilter.maxMatchType);
				if(ExCollectionUtils.isNotEmpty(sensitivewordSet)){
					map.put("statusCode", 300);
					map.put("message", "内容含有敏感词："+sensitivewordSet+",请修改后再提交");
					continue;
				}
				if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新		
					bbsTopicService.saveBbsTopicScore(request);
					BbsTopic bbsTopic = bbsTopicService.get(resourceid);
//					BbsTopic bbsTopic =bbsTopicService.get(request.getParameter("resourceid"));
					map.put("statusCode", 200);
					map.put("message", "保存成功！");					
					map.put("reloadUrl", request.getContextPath()+"/edu3/framework/bbstopic/score/list.html?resourceid="+resourceid+"&orgUnitID="+bbsTopic.getBbsUserInfo().getSysUser().getUnitId());
					//计算学生分数
					try {
						
						Course c = bbsTopic.getCourse();
						User user = bbsTopic.getBbsUserInfo().getSysUser();
						String defaultStudentId = "";
						StudentInfo stu = null;
						if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
							defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
							stu = studentInfoService.get(defaultStudentId);
						}
						/*Grade grade = gradeService.getDefaultGrade();
						usualResultsService.saveSpecificUsualResults(stu, grade.getYearInfo().getResourceid(), grade.getTerm(), c.getResourceid());*/
						/*String _yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
						//String _yearInfoId = "";
						String _term = "";
						String _year = "";
						if (null!=_yearTerm) {
							String[] ARRYyterm = _yearTerm.split("\\.");
							_year = ARRYyterm[0];
							//_yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(_year)).getResourceid();
							_term = ARRYyterm[1];
						}*/
						
						TeachingPlanCourse teachingPlanCourse = studentLearnPlanService.getStudentLearnPlanByCourse(c.getResourceid(), stu.getResourceid(),"studentId").getTeachingPlanCourse();
						TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService
								.findOneByCondition(stu.getGrade()
										.getResourceid(), stu.getTeachingPlan().getResourceid(),
										teachingPlanCourse.getResourceid(), stu.getBranchSchool().getResourceid());
						if(teachingPlanCourseStatus==null){
							logger.error("更新学生成绩失败:{课程还没有开课}");
							continue;
						}
						String yearTermStr = teachingPlanCourseStatus.getTerm();
						String year = "";
						String term = "";
						String yearInfoId = "";
						if (null!=yearTermStr) {
							year = yearTermStr.substring(0, 4);
							term = yearTermStr.substring(6, 7);
						}
						YearInfo yearInfo = yearInfoService.getByFirstYear(Long.parseLong(year));
						yearInfoId = yearInfo.getResourceid();
						/*//这里要判断是否和当前学期一致,不然不能更新成绩
						if(!_year.equals(year) || !_term.equals(term)){
							logger.error("更新学生成绩失败:{课程开课的时间与当前学期不对应}");
							continue;
						}*/
						//是否在网上学习时间内
						/*LearningTimeSetting learningTimeSetting = learningTimeSettingService.findLearningTimeSetting(yearInfoId, term);
						Date now = new Date();
						if(learningTimeSetting == null || now.before(learningTimeSetting.getStartTime()) || now.after(learningTimeSetting.getEndTime())){
							logger.error("更新学生成绩失败:{当前时间不在网上学习时间内,请联系管理员设置}");
						}else{*/
						usualResultsService.saveSpecificUsualResults(stu, yearInfoId, term, c.getResourceid());
						//}
					} catch (Exception e) {
						logger.error("更新学生成绩失败:{}",e);
					}
				}			
			} while(false);
		} catch (ServiceException e) {
			logger.error("保存帖子评分出错：",e);
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		} catch (Exception e) {
			logger.error("保存帖子评分出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 批量回复随堂问答页面
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequestMapping("/edu3/metares/topicreply/bbstopic/batchReply.html")
	public String replyTopicForm(HttpServletRequest request, HttpServletResponse reponse, ModelMap model){
		String topicIds = request.getParameter("resourceIds");
		User user = SpringSecurityHelper.getCurrentUser();
		List<BbsTopic> topicList = new ArrayList<BbsTopic>();
		try {
			if(ExStringUtils.isNotEmpty(topicIds)){
				String[] topicIdArray = topicIds.split(",");
				for(String topicId : topicIdArray){
					if(ExStringUtils.isNotEmpty(topicId)){
						BbsTopic bbsTopic = bbsTopicService.get(topicId);
						int totalTopicNum = 0;
						int validTopicNum = 0;
						if(bbsTopic!=null && bbsTopic.getCourse()!=null){
							// 获取该学生该门课程的总帖数和有效贴数
							Map<String, Object> topicNum = learningJDBCService.getStuCourseTopicNum(bbsTopic.getBbsUserInfo().getSysUser().getResourceid(), bbsTopic.getCourse().getResourceid());
							if(topicNum!=null && topicNum.size() > 0){
								totalTopicNum = topicNum.get("totalTopicNum")==null?0:((BigDecimal)topicNum.get("totalTopicNum")).intValue();
								validTopicNum = topicNum.get("validTopicNum")==null?0:((BigDecimal)topicNum.get("validTopicNum")).intValue();
							}
						}
						bbsTopic.setTotalTopicNum(totalTopicNum);
						bbsTopic.setValidTopicNum(validTopicNum);
						List<BbsReply> replyList = bbsReplyService.findByHql("from "+BbsReply.class.getSimpleName()+" where isDeleted=0 and bbsTopic.resourceid=? order by replyDate asc,resourceid", bbsTopic.getResourceid());
						String isReply = "N";
						if(ExCollectionUtils.isNotEmpty(replyList)){//设置附件
							isReply = "Y";
							for (BbsReply bbsReply : replyList) {
								bbsReply.setIsMine(Constants.BOOLEAN_NO);
								if(!Constants.BOOLEAN_NO.equalsIgnoreCase(bbsReply.getIsAttachs())){
									List<Attachs> attachs = attachsService.findAttachsByFormId(bbsReply.getResourceid());
									bbsReply.setAttachs(attachs);
								}
								if(user.getResourceid().equals(bbsReply.getBbsUserInfo().getSysUser().getResourceid())){
									bbsReply.setIsMine(Constants.BOOLEAN_YES);
								}
							}
						}
						bbsTopic.setIsReply(isReply);
						bbsTopic.setReplyList(replyList);
						topicList.add(bbsTopic);
					}
				}
			}
		} catch (Exception e) {
			logger.error("进入批量回复随堂问答页面出错", e);
		}
		model.addAttribute("bbsTopicList", topicList);
		
		return "/edu3/learning/bbstopic/replyTopic-form";
	}
	
	/**
	 * 批量保存回复
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/framework/bbstopic/score/batchSave.html")
	public void batchSaveTopic(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		String[] topicIds = request.getParameterValues("resourceid");
		String scoreType = request.getParameter("scoreType");
		String replyContent = request.getParameter("replyContent");
		StringBuffer resourceIds;
		int statusCode = 200;
		String message ="保存成功！";
		resourceIds = new StringBuffer("");
		try {
			do {
				if(ExStringUtils.isEmpty(replyContent)){
					statusCode = 300;
					message = "回复内容不能为空";
					continue;
				}
				// 判断是否包含敏感词
				SensitivewordFilter sensitivewordFilter = (SensitivewordFilter)request.getSession().getServletContext().getAttribute("sensitivewordFilter");
				Set<String> sensitivewordSet = sensitivewordFilter.getSensitiveWord(ExStringUtils.trimToEmpty(replyContent),SensitivewordFilter.maxMatchType);
				if(ExCollectionUtils.isNotEmpty(sensitivewordSet)){
					statusCode = 300;
					message = "回复内容含有敏感词："+sensitivewordSet+",请修改后再提交";
					continue;
				}
				
				User user = SpringSecurityHelper.getCurrentUser();
				if (topicIds != null && topicIds.length > 0) {
					for (String topicId : topicIds) {
						resourceIds.append(topicId + ",");
						bbsTopicService.saveReplyTopicScore(topicId, scoreType,replyContent, user);
					}
				}
			} while (false);
		} catch (ServiceException e) {
			logger.error("批量保存回复出错", e);
			statusCode = 300;
			message ="保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("reloadUrl", request.getContextPath()+"/edu3/metares/topicreply/bbstopic/batchReply.html?resourceIds="+resourceIds.toString());
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * Excel导出随堂问答统计表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/bbsTopic/excel/export.html")
	public void doExcelExportBbsTopic(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		//从库中查出数据		
		String title = ExStringUtils.trimToEmpty(request.getParameter("title"));
		String status = ExStringUtils.trimToEmpty(request.getParameter("status"));	
		String courseName = ExStringUtils.trimToEmpty(request.getParameter("courseName"));	
		String isAnswered = ExStringUtils.trimToEmpty(request.getParameter("isAnswered"));
		String yearInfoId = request.getParameter("yearInfoId");
		String term = request.getParameter("term");
		String courseId = request.getParameter("courseId");
		String classesId = request.getParameter("classesId");
		String orgUnitId = request.getParameter("orgUnitId");
		String content = request.getParameter("content");
		String fillinMan = request.getParameter("fillinMan");
		String startLong = request.getParameter("startLong");
		String endLong = request.getParameter("endLong");
		
		//导出的表头
		String branchSchoolText ="";
		User curUser          = SpringSecurityHelper.getCurrentUser();
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			orgUnitId = curUser.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotEmpty(orgUnitId)) {
			branchSchoolText = orgUnitService.get(orgUnitId).getUnitName() ;
		}
	
		StringBuffer excelTitle = new StringBuffer();
		
		Map<String,Object> condition = new HashMap<String, Object>(0);
		
		if(ExStringUtils.isNotEmpty(title)){
			condition.put("title", title);		
		}
		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", status);	
		}
		if(ExStringUtils.isNotEmpty(courseName)){
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(isAnswered)){
			condition.put("isAnswered", isAnswered);
		}
		if(ExStringUtils.isNotEmpty(yearInfoId)){
			condition.put("yearInfoId", yearInfoId);
		}
		if(ExStringUtils.isNotEmpty(term)){
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(orgUnitId)){
			condition.put("orgUnitId", orgUnitId);
		}
		if(ExStringUtils.isNotEmpty(content)){
			condition.put("content", content);
		}
		if(ExStringUtils.isNotEmpty(fillinMan)){
			condition.put("fillinMan", fillinMan);
		}
		if(ExStringUtils.isNotEmpty(startLong)){
			condition.put("startLong", startLong);
		}
		if(ExStringUtils.isNotEmpty(endLong)){
			condition.put("endLong", endLong);
		}
		condition.put("topicType", "2");//提问
		condition.put("notEmptySyllabus", "Y");//关联的知识节点不为空
		
		List<BbsTopic> list = bbsTopicService.findBbsTopicByCondition(condition);
		List<Map<String,Object>> newlist = new ArrayList<Map<String,Object>>(0);
		
		if(0<list.size()){
			for (BbsTopic m : list) {
				
				Map<String,Object> newMap = new HashMap<String,Object>();
				String classesName = "";
				String unitName = "";
				try {
					String hqls = "from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.resourceid=(select ue.exValue from "+UserExtends.class.getSimpleName()+" ue where ue.isDeleted=0 and ue.exCode='defalutrollid' and ue.user.resourceid=?) ";
					StudentInfo _studentInfo = studentInfoService.findUnique(hqls, m.getBbsUserInfo().getSysUser().getResourceid());
					if (_studentInfo != null) {
						if(_studentInfo.getClasses()!=null){
							classesName = _studentInfo.getClasses().getClassname();
						}
						unitName = _studentInfo.getBranchSchool().getUnitName();
					}
					newMap.put("brSchoolName", unitName);
					newMap.put("courseName", m.getCourse()==null?"":m.getCourse().getCourseName());
					newMap.put("syllabusName", m.getSyllabus()==null?"":m.getSyllabus().getSyllabusName());
					newMap.put("title", m.getTitle());
					newMap.put("fillinMan", m.getFillinMan());				
					newMap.put("classes",classesName);
					newMap.put("fillinDate", m.getFillinDate()==null?"":ExDateUtils.formatDateStr((Date)m.getFillinDate(), "yyyy-MM-dd HH:mm:ss"));
					newMap.put("isAnswered",m.getIsAnswered()==0?"未解决":"已答复");
					newMap.put("howLongReply",m.getHowLongReply());
					newMap.put("firstReplyMan",m.getFirstReplyAccount());
					newMap.put("tags",m.getTags()==null?"否":m.getTags().contains("FAQ")?"是":"否");
					newMap.put("scoreType", m.getScoreType()==null?"": "1".equals(m.getScoreType()) ?"是":"否");
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				newlist.add(newMap);
			}
		}
		
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//存放路径
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			List<String> dictCodeList = new ArrayList<String>();
			Map<String , Object> mapdict = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			
			exportExcelService.initParmasByfile(disFile, "bbsTopticVo", newlist,mapdict);
			exportExcelService.getModelToExcel().setHeader(title.toString()+"随堂问答统计表");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高 
	
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "随堂问答统计表.xls", excelFile.getAbsolutePath(),true);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
}
