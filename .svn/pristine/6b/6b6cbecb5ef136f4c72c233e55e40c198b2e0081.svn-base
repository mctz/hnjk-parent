package com.hnjk.edu.learning.controller;

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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.BbsGroup;
import com.hnjk.edu.learning.model.BbsGroupUsers;
import com.hnjk.edu.learning.model.BbsSection;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.IBbsGroupService;
import com.hnjk.edu.learning.service.IBbsGroupUsersService;
import com.hnjk.edu.learning.service.IBbsSectionService;
import com.hnjk.edu.learning.service.IBbsTopicService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 学生讨论小组管理
 * <code>BbsGroupController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-27 下午02:24:21
 * @see 
 * @version 1.0
 */
@Controller
public class BbsGroupController extends BaseSupportController {
	private static final long serialVersionUID = -2175085560729504828L;
	
	@Autowired
	@Qualifier("bbsGroupService")
	private IBbsGroupService bbsGroupService;
	
	@Autowired
	@Qualifier("bbsGroupUsersService")
	private IBbsGroupUsersService bbsGroupUsersService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("bbsTopicService")
	private IBbsTopicService bbsTopicService;
	
	@Autowired
	@Qualifier("bbsSectionService")
	private IBbsSectionService bbsSectionService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 学习小组列表
	 * @param groupName
	 * @param courseName
	 * @param status
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroup/list.html")
	public String listBbsGroup(String groupName,String courseName,String status, String classesId, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("course,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式	
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(groupName)) {
			condition.put("groupName", groupName);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(status)) {
			condition.put("status", Integer.parseInt(status));
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		if(SpringSecurityHelper.isUserInRole(roleCode)){//老师角色只查询自己课程的小组
			condition.put("teacherId", user.getResourceid());			
			model.addAttribute("user", user);
		}
		
		Page bbsGroupListPage = bbsGroupService.findBbsGroupByCondition(condition, objPage);
		
		model.addAttribute("bbsGroupListPage", bbsGroupListPage);
		model.addAttribute("condition", condition);
		
		String classesCondition = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(user)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+user.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/bbsgroup/bbsgroup-list";
	}
	
	/**
	 * 新增编辑学习小组
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/framework/bbs/bbsgroup/input.html")
	public String editBbsGroup(String resourceid, ModelMap model) throws WebException {
		BbsGroup bbsGroup = null;
		String courseId = "";
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			bbsGroup = bbsGroupService.get(resourceid);	
			String groupUserIds = "";
			for (BbsGroupUsers u : bbsGroup.getGroupUsers()) {
				groupUserIds += u.getStudentInfo().getResourceid()+",";
			}
			bbsGroup.setGroupUserIds(groupUserIds);
			courseId = bbsGroup.getCourse().getResourceid();
		}else{ //----------------------------------------新增
			bbsGroup = new BbsGroup();	
		}
		/* 加载课程 */
		Map<String,Object> condition = new HashMap<String,Object>();
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String teacherId = "";
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
				teacherId = user.getResourceid();	
			}
			if(ExStringUtils.isNotBlank(teacherId)){
				condition.put("teacherId",teacherId);
			}
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("bbsGroupEditFor_courseId","courseId",courseId,list);
			model.addAttribute("bbsgroupformCourseSelect", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		model.addAttribute("bbsGroup", bbsGroup);	
		
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+curUser.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/bbsgroup/bbsgroup-form";
	}
	/**
	 * 保存学习小组
	 * @param bbsGroup
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroup/save.html")
	public void saveBbsGroup(BbsGroup bbsGroup,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {				
			String courseId = request.getParameter("courseId");	
			if(ExStringUtils.isNotEmpty(courseId)) {
				bbsGroup.setCourse(courseService.get(courseId));
			}
			String classesId = request.getParameter("classesId");	
			if(ExStringUtils.isNotEmpty(classesId)) {
				bbsGroup.setClasses(classesService.get(classesId));
			}
			if(ExStringUtils.isNotBlank(bbsGroup.getResourceid())){ //--------------------更新
				BbsGroup preBbsGroup = bbsGroupService.get(bbsGroup.getResourceid());
				bbsGroup.setGroupUsers(preBbsGroup.getGroupUsers());
				ExBeanUtils.copyProperties(preBbsGroup, bbsGroup);
				bbsGroupService.saveOrUpdateBbsGroup(preBbsGroup);				
			}else{ //--------------------------------------------------保存	
				bbsGroupService.saveOrUpdateBbsGroup(bbsGroup);	//保存小组与成员信息
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_METARES_BBSGROUP_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/framework/bbs/bbsgroup/input.html?resourceid="+bbsGroup.getResourceid());
		}catch (Exception e) {
			logger.error("保存学习小组出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除学习小组
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroup/remove.html")
	public void removeBbsGroup(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsGroupService.deleteBbsGroup(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/framework/bbs/bbsgroup/list.html");
			}
		} catch (Exception e) {
			logger.error("删除学习小组出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 选择小组成员及组长
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroup/member.html")
	public String nodePicker(Page objPage,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		objPage.setOrderBy("studentInfo.studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String type = request.getParameter("type");//页面跳转类型
		String name = request.getParameter("name");//姓名
		String studyNo = request.getParameter("studyNo");//学号
		String courseId = request.getParameter("courseId");//课程
		String branchSchool = request.getParameter("branchSchool");//学习中心
		String classic = request.getParameter("classic");//层次	
		String groupId = request.getParameter("groupId");
		String classesId = request.getParameter("classesId");
		String groupUserIds = "";
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			branchSchool = user.getOrgUnit().getResourceid();
			request.setAttribute("branchSchool", branchSchool);
			condition.put("teacherId", user.getResourceid());
		}
		if(ExStringUtils.isNotEmpty(groupId)){
			BbsGroup group = bbsGroupService.get(groupId);
			courseId = group.getCourse().getResourceid();
			condition.put("groupId", groupId);
			model.addAttribute("bbsGroup", group);			
			for (BbsGroupUsers u : group.getGroupUsers()) {
				groupUserIds += u.getStudentInfo().getResourceid()+",";
			}
			model.addAttribute("groupUserIds", groupUserIds);//已加入小组成员ids
			model.addAttribute("groupUserCount", group.getGroupUsers().size());//已加入小组成员个数
		}
		
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
			List<BbsGroupUsers> list = bbsGroupUsersService.findBbsGroupUsersByCourseId(courseId);
			String filterids = "";
			for (BbsGroupUsers u : list) {
				if(!groupUserIds.contains(u.getStudentInfo().getResourceid())) {
					filterids += u.getStudentInfo().getResourceid()+",";
				}
			}
			model.addAttribute("filterids", filterids);//已加入小组成员ids
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		condition.put("status", 1);	//已预约学习的学生
		
		Page page = studentLearnPlanService.findStudentBBSgroupLeader(condition, objPage);
		if(ExStringUtils.isNotEmpty(type)) {
			condition.put("type", type);
		}
		model.addAttribute("stuplanlist", page);
		
		String idsN = request.getParameter("idsN");
		String namesN = request.getParameter("namesN");
		if(ExStringUtils.isNotEmpty(idsN)) {
			condition.put("idsN", idsN);
		}
		if(ExStringUtils.isNotEmpty(namesN)) {
			condition.put("namesN", namesN);
		}
		model.addAttribute("condition", condition);
				
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+curUser.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		if(ExStringUtils.isNotEmpty(type)){//选择组长
			model.addAttribute("type", type);
			return "/edu3/learning/bbsgroup/selector_members";			
		} else { //分配小组成员
			return "/edu3/learning/bbsgroup/bbsgroupusers-form";
		}
	}
	/**
	 * 小组成员列表
	 * @param groupId
	 * @param courseName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroupusers/list.html")
	public String listBbsGroupUsers(String groupId,String courseName, String classesId,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式	
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(groupId)) {
			condition.put("groupId", groupId);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		
		Page bbsGroupUsersListPage = bbsGroupUsersService.findBbsGroupUsersByCondition(condition, objPage);
		
		model.addAttribute("bbsGroupUsersListPage", bbsGroupUsersListPage);
		model.addAttribute("condition", condition);		
		
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+curUser.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/bbsgroup/bbsgroupusers-list";
	}
	
	/**
	 * 保存学习小组成员
	 * @param groupId
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroupusers/save.html")
	public void saveBbsGroupUsers(String groupId,String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			if(ExStringUtils.isNotBlank(groupId)&&ExStringUtils.isNotBlank(resourceid)){
				String[] ids = resourceid.split(",");
				BbsGroup bbsGroup = bbsGroupService.get(groupId);
//				List<BbsGroupUsers> list = new ArrayList<BbsGroupUsers>();
				boolean isUpdate = false;
				for (String id : ids) {					
					StudentInfo stu = studentInfoService.get(id);
					if(!bbsGroupUsersService.isUserInGroup(stu.getSysUser().getResourceid(), bbsGroup.getResourceid())){//该学生还没加入小组
						BbsGroupUsers guser = new BbsGroupUsers();
						guser.setStudentInfo(stu);
						guser.setBbsGroup(bbsGroup);
						bbsGroup.getGroupUsers().add(guser);
						isUpdate = true;
					}					
				}	
				if(isUpdate) {
					bbsGroupService.update(bbsGroup);
				}
				
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_METARES_BBSGROUPUSERS");
				map.put("reloadUrl", request.getContextPath() +"/edu3/framework/bbs/bbsgroup/member.html?groupId="+groupId);
			}
		}catch (Exception e) {
			logger.error("分配学习小组成员出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	@RequestMapping("/edu3/framework/bbs/bbsgroupusers/autoassign.html")
	public String autoassignBbsGroupUsers(Page objPage,HttpServletRequest request,ModelMap model) throws WebException{
		return "/edu3/learning/bbsgroup/bbsgroupusers-auto";
	}
	/**
	 * 自动分配小组
	 * @param courseId 课程id
	 * @param bbsgroupnum 小组数
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroupusers/assign.html")
	public void assignBbsGroupUsers(String courseId,Integer bbsgroupnum,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			if(ExStringUtils.isNotBlank(courseId) && bbsgroupnum!=null){
				if(bbsgroupnum==0){
					throw new WebException("小组数不能为0！");
				}
				List<StudentLearnPlan> list = getStudentLearnPlanList(courseId);
				if(list!=null){
					if(bbsgroupnum>list.size()){
						throw new WebException("小组数大于学生人数,请重新指定小组数！");
					} else {
						List<BbsGroup> bbsGroups = new ArrayList<BbsGroup>();
						Course course = courseService.get(courseId);
						int num = list.size()/bbsgroupnum;//每组人数
						for (int i = 0; i < bbsgroupnum; i++) {
							BbsGroup bbsGroup = new BbsGroup();
							bbsGroup.setCourse(course);
							bbsGroup.setGroupName(ExDateUtils.getCurrentYear()+"·"+course.getCourseName()+"·第"+(i+1)+"组");
							
							bbsGroups.add(bbsGroup);
							
							int start = i*num;
							int end = start + num;
							if(i==bbsgroupnum-1){
								end = list.size();
							}
							
							bbsGroup.setLeaderId(list.get(start).getStudentInfo().getResourceid());
							bbsGroup.setLeaderName(list.get(start).getStudentInfo().getStudentName());
							for (int index = start; index < end; index++) {
								BbsGroupUsers u = new BbsGroupUsers();
								u.setBbsGroup(bbsGroup);
								u.setStudentInfo(list.get(index).getStudentInfo());
								
								bbsGroup.getGroupUsers().add(u);
							}
						}
						bbsGroupService.batchSaveOrUpdate(bbsGroups);
						map.put("statusCode", 200);
						map.put("message", "分配成功！");
						map.put("navTabId", "RES_METARES_BBSGROUP");
					}
				}
			}	
		} catch (Exception e) {
			logger.error("分配学习小组成员出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 获取课程可分配人数
	 * @param courseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroupusers/getstudentcount.html")
	public void ajaxStudentNumber(String courseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		if(ExStringUtils.isNotBlank(courseId)){
			List<StudentLearnPlan> plans = getStudentLearnPlanList(courseId);	
			Integer num = 0;
			if(plans!=null){
				num = plans.size();
			}	
			renderJson(response, JsonUtils.objectToJson(num));
		}			
	}

	private List<StudentLearnPlan> getStudentLearnPlanList(String courseId) {
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
			List<BbsGroupUsers> list = bbsGroupUsersService.findBbsGroupUsersByCourseId(courseId);
			if(list!=null){
				List<String> ids = new ArrayList<String>();
				for (BbsGroupUsers u : list) {
					ids.add(u.getStudentInfo().getResourceid());
				}
				if(!ids.isEmpty()){
					condition.put("notStuIds", "'"+ExStringUtils.join(ids, "','")+"'");
				}				
			}			
		}		
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())) {
			condition.put("teacherId", user.getResourceid());
		}
		condition.put("status", 1);	//已预约学习的学生
		condition.put("orderBy", "yearInfo.firstYear desc,term desc,studentInfo.branchSchool.resourceid,studentInfo.classic.resourceid,studentInfo.grade.resourceid");
		
		List<StudentLearnPlan> plans = studentLearnPlanService.findStudentLearnPlanByCondition(condition);
		return plans;
	}
	/**
	 * 根据课程获取小组列表，用于选择列表
	 * @param courseId
	 * @param response
	 * @throws WebException
	 */	
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/framework/bbs/bbsgrouptopic/getGroup.html")
	public void getBbsGroupList(String courseId,String parenTopicId,HttpServletResponse response) throws WebException{	
		if(ExStringUtils.isNotBlank(courseId)){
			List<BbsGroup> bbsGroups = bbsGroupService.findBbsGroupByCourseId(courseId);				
			List<List<String>> list = new ArrayList<List<String>>();
			for (BbsGroup group : bbsGroups) {
				List<String> str = new ArrayList<String>();
				str.add(group.getResourceid());
				str.add(group.getGroupName());
				List<BbsTopic> groupTopics = null;
				if(ExStringUtils.isNotEmpty(parenTopicId)){
					groupTopics = bbsTopicService.findByHql(" from "+BbsTopic.class.getSimpleName()+" where isDeleted=0 and bbsGroup.isDeleted=0 and bbsGroup.resourceid=? and parenTopic.resourceid=? ", group.getResourceid(),parenTopicId);
				}
				if(groupTopics!=null&&!groupTopics.isEmpty()) {
					str.add("Y");
				} else {
					str.add("N");
				}
				list.add(str);
			}			
			renderJson(response, JsonUtils.listToJson(list));
		}		
	}
	/**
	 * 获取课程列表
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/getCourse.html")
	public void getCourseList(HttpServletRequest request,HttpServletResponse response) throws WebException{	
		List list = null;
		try {
			String hql = "select new map(c.resourceid as resourceid,c.courseName as courseName) from "+Course.class.getName()+" c where c.isDeleted=0  ";
			User user = SpringSecurityHelper.getCurrentUser();
			if(user!=null){
				String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
				//if(SpringSecurityHelper.isUserInRole(roleCode))			
				//	hql += "  and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.course.resourceid=c.resourceid and (t.teacherId like '%"+user.getResourceid()+"%' or t.assistantIds like '%"+user.getResourceid()+"%') )  ";
			}
			hql += " order by c.courseName ";
			list = courseService.findByHql(hql);	
		}catch (Exception e) {
			logger.error("获取课程列表出错：{}",e.fillInStackTrace());
		}	
		renderJson(response, JsonUtils.listToJson(list));
	}
	/**
	 * 删除学习小组成员
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgroupusers/remove.html")
	public void removeBbsGroupUsers(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				bbsGroupUsersService.deleteBbsGroupUsers(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/framework/bbs/bbsgroupusers/list.html");
			}
		} catch (Exception e) {
			logger.error("删除学习小组成员出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 查看小组
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbsgroup/view.html")
	public String viewBbsGroup(String resourceid, HttpServletRequest request, ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			BbsGroup bbsGroup = bbsGroupService.get(resourceid);
			model.addAttribute("bbsGroup", bbsGroup);
		}
		return "/edu3/learning/bbsgroup/bbsgroup-view";
	}
	/**
	 * 小组讨论话题列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgrouptopic/list.html")
	public String listBbsGroupTopic(HttpServletRequest request, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("course,fillinDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
				
		String title = request.getParameter("title");		
		String groupName = request.getParameter("groupName");
		String courseName = request.getParameter("courseName");
		String classesId = request.getParameter("classesId");
		String fillinDateStartStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateStartStr"));
		String fillinDateEndStr = ExStringUtils.trimToEmpty(request.getParameter("fillinDateEndStr"));
		condition.put("fillinDateStartStr", fillinDateStartStr);
		condition.put("fillinDateEndStr", fillinDateEndStr);
		
		if(ExStringUtils.isNotEmpty(fillinDateStartStr)) {
			condition.put("fillinDateStart", ExDateUtils.convertToDate(fillinDateStartStr).getTime());
		}
		if(ExStringUtils.isNotEmpty(fillinDateEndStr)) {
			condition.put("fillinDateEnd", ExDateUtils.addDays(ExDateUtils.convertToDate(fillinDateEndStr), 1).getTime());
		}
		if(ExStringUtils.isNotEmpty(title)) {
			condition.put("title", title);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(groupName)) {
			condition.put("groupName", groupName);
		}
		condition.put("topicType", "4");//讨论
		condition.put("parenTopic", "null");
		
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		
		String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue();//获取版块编码
		condition.put("sectionCode", sectionCode);
		if(SpringSecurityHelper.isUserInRole(roleCode)) {
			condition.put("teacherId", user.getResourceid());
		}
				
		Page bbsGroupTopicListPage = bbsTopicService.findBbsTopicByCondition(condition, objPage);
		
		model.addAttribute("bbsGroupTopicListPage", bbsGroupTopicListPage);
		model.addAttribute("condition", condition);
		
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.teacherId='"+curUser.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
		
		return "/edu3/learning/bbsgrouptopic/bbsgrouptopic-list";
	}
	/**
	 * 新增编辑讨论话题
	 * @param resourceid
	 * @param groupId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgrouptopic/input.html")
	public String editBbsGroupTopic(String resourceid, ModelMap model) throws WebException {		
		User user = SpringSecurityHelper.getCurrentUser();		
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			BbsTopic parenTopic = bbsTopicService.get(resourceid);
			
			model.addAttribute("bbsTopic", parenTopic);
		} else {
			BbsTopic parenTopic = new BbsTopic();
			parenTopic.setViewPermiss("1");//小组可见		
			model.addAttribute("bbsTopic", parenTopic);
		}
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/learning/bbsgrouptopic/bbsgrouptopic-form";
	}
	/**
	 * 保存讨论话题
	 * @param parentTopic
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgrouptopic/save.html")
	public void saveBbsGroupTopic(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			String resourceid = request.getParameter("resourceid");
			String[] groupIds = request.getParameterValues("groupIds");
			String courseId = request.getParameter("courseId");
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			String viewPermiss = request.getParameter("viewPermiss");
			Date endTime = ExDateUtils.parseDate(request.getParameter("endTime"), ExDateUtils.PATTREN_DATE);
			
			Course course = null;
			if(ExStringUtils.isNotEmpty(courseId)) {
				course = courseService.get(courseId);
			}
			
			BbsTopic parentTopic = null;
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新		
				parentTopic = bbsTopicService.get(resourceid);
				if(!(course!=null && course.getResourceid().equals(parentTopic.getCourse().getResourceid()))){
					parentTopic.setCourse(course);
					parentTopic.getChilds().clear();
				}
				parentTopic.setContent(content);
				parentTopic.setTitle(title);
				parentTopic.setEndTime(endTime);
				if(ExDateUtils.getTodayEnd().before(parentTopic.getEndTime()))//更改的时间大于今天
				{
					parentTopic.setStatus(0);
				}
				parentTopic.setViewPermiss(viewPermiss);
				for (BbsTopic child : parentTopic.getChilds()) {
					child.setContent(content);
					child.setTitle(title);
					child.setEndTime(endTime);
					child.setStatus(parentTopic.getStatus());
					child.setViewPermiss(viewPermiss);
				}
				//bbsTopicService.update(parentTopic);
			} else {				
				User user = SpringSecurityHelper.getCurrentUser();
				parentTopic = new BbsTopic();
				parentTopic.setContent(content);
				parentTopic.setTitle(title);
				parentTopic.setEndTime(endTime);
				parentTopic.setViewPermiss(viewPermiss);
				String sectionCode = CacheAppManager.getSysConfigurationByCode("web.bbs.sectioncode.group").getParamValue();//获取版块编码
				BbsSection bbsSection = bbsSectionService.findUnique(" from "+ BbsSection.class.getSimpleName()+" where isDeleted=0 and sectionCode=? ", sectionCode);
				parentTopic.setBbsSection(bbsSection);
				parentTopic.setTopicType("4");
				parentTopic.setFillinDate(new Date());
				parentTopic.setFillinMan(user.getCnName());
//				parentTopic.setFillinManId(user.getResourceid());
				parentTopic.setLastReplyMan(parentTopic.getFillinMan());
//				parentTopic.setLastReplyManId(parentTopic.getFillinManId());
				parentTopic.setLastReplyDate(parentTopic.getFillinDate());
				parentTopic.setCourse(course);
				BbsSection section = parentTopic.getBbsSection();
				if (section != null) {
					parentTopic.getBbsSection().setTopicCount(section.getTopicCount()+1);
				}
				
			}			
			
			bbsTopicService.saveOrUpdateBbsTopic(parentTopic,null,groupIds);
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_METARES_BBSGROUPTOPIC_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/framework/bbs/bbsgrouptopic/input.html?resourceid="+parentTopic.getResourceid());
		}catch (Exception e) {
			logger.error("保存讨论话题出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除小组讨论话题
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/bbs/bbsgrouptopic/remove.html")
	public void removeBbsGroupTopic(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){		
				String[] ids = resourceid.split("\\,");
				boolean isDeleted = true;
				for (String id : ids) {
					BbsTopic bbsTopic = bbsTopicService.get(id); 
					for (BbsTopic child : bbsTopic.getChilds()) {
						if(-1==child.getStatus() || child.getReplyCount()>0){//话题结束或回复不为零，不可删除
							isDeleted = false;
							break;
						}
					}					
					if(!isDeleted) {
						break;
					}
				}
				if(!isDeleted){
					map.put("statusCode", 300);
					map.put("message", "话题正在讨论或已经结束，不可删除！");		
				} else {
					bbsTopicService.deleteBbsTopic(resourceid.split("\\,"));
					map.put("statusCode", 200);
					map.put("message", "删除成功！");				
					map.put("forward", request.getContextPath()+"/edu3/framework/bbs/bbsgrouptopic/list.html");
				}				
			}
		} catch (Exception e) {
			logger.error("删除讨论话题出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 任课情况
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/learning/student/addressbook.html")
	public String studentAddressBook(Page objPage,HttpServletRequest request,ModelMap model) throws WebException{

		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));//课程	
		String yearInfo = request.getParameter("yearInfo");
		String term = request.getParameter("term");
		String branchSchool = request.getParameter("branchSchool");
		String classesId = request.getParameter("classesId");
		
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			String teacherId = "";
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
				teacherId = user.getResourceid();
				if(!SpringSecurityHelper.isTeachingCentreTeacher(user) && null != user.getOrgUnit()){
					branchSchool = user.getOrgUnit().getResourceid();
					request.setAttribute("onlineSchoolName", branchSchool);
				}
			}
			if(ExStringUtils.isNotBlank(teacherId)){
				condition.put("teacherId",teacherId);
			}
			List<Map<String, Object>> list = activeCourseExamService.getTeacherOnlineCourse(condition);
			String courseStr = getCourseSelect("student_addressbook_courseId","courseId",courseId,list);
			model.addAttribute("studentaddressbookCourseSelect", courseStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(yearInfo) || ExStringUtils.isNotEmpty(term)) {
			YearInfo year = yearInfoService.get(yearInfo);
			String Syear = null != year ? year.getFirstYear()+"" : "%";
			condition.put("yearInfoTerm", Syear+"_0"+(ExStringUtils.isNotBlank(term)?term:"%"));	
		}		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(classesId)){
			condition.put("classesId", classesId);
		}
		//根据表EDU_TEACH_TIMETABLE查询得到，只能查询到已有排课时间的教学计划课程
		Page page = bbsGroupService.findOnlineStuGroup(condition, objPage);
		if(ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("yearInfo", yearInfo);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		request.setAttribute("stuplanlist", page);
		request.setAttribute("condition", condition);
		
		String classesCondition = "";
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
//			classesCondition = "brSchool.resourceid='" + curUser.getOrgUnit().getResourceid() + "'";
			if(!SpringSecurityHelper.isUserInRole("ROLE_BRS_STUDENTSTATUS")){
				classesCondition += " and resourceid in (select tt.classes.resourceid from TeachingPlanCourseTimetable tt where tt.isDeleted=0 and tt.teacherId='"+curUser.getResourceid()+"')";
			}
		}else{
			model.addAttribute("isadmin", "Y");
//			if(ExStringUtils.isNotEmpty(branchSchool)){
//				classesCondition = "brSchool.resourceid='" + branchSchool + "'";
//			}
		}
		model.addAttribute("classesCondition", classesCondition);
				
		return "/edu3/learning/bbsgroup/student-addressbook";
	}
	
	
	private String getCourseSelect(String id, String name,String courseid,List<Map<String, Object>> list) {
		StringBuffer SBFcourse = new StringBuffer("<select class='flexselect' id='"+id+"' name='"+name+"' >");
		SBFcourse.append("<option value=''>请选择</option>");
		if(ExCollectionUtils.isNotEmpty(list) && list.size() > 0){
			for(Map<String, Object> map : list){
				if(map.get("courseid").equals(courseid)){
					SBFcourse.append("<option value='"+map.get("courseid")+"' selected='selected'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
				}else{
					SBFcourse.append("<option value='"+map.get("courseid")+"'>"+map.get("coursecode")+"-"+map.get("coursename")+"</option>");
				}
			}
		}
		SBFcourse.append("</select>");
		return SBFcourse.toString();
	}

	/**
	 * 学生名单
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/learning/student/online-student.html")
	public String onlinestudent(Page objPage,HttpServletRequest request,ModelMap model,String classesid) throws WebException{
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("studyNo");
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		Map<String,Object> condition = new HashMap<String, Object>();
		Map<String,Object> values = new HashMap<String, Object>();
		condition.put("classesId", classesid);
		values.put("classesId", classesid);
		if(ExStringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		String hql = " from "+StudentInfo.class.getSimpleName()+" where isDeleted = 0 and classes.resourceid = :classesId ";
		if (condition.containsKey("studentName")) {
			hql += " and studentName like :studentName";
			values.put("studentName", "%"+studentName+"%");
		}
		if (condition.containsKey("studyNo")) {
			hql += " and studyNo=:studyNo";
			values.put("studyNo", studyNo);
		}
		hql += " and studentStatus in('11','16','25')";
		Page page = studentInfoService.findByHql(objPage, hql, values);
		request.setAttribute("onlineStudent", page);	
		request.setAttribute("onlineClassesid",classesid);
		request.setAttribute("condition", condition);
		return "/edu3/learning/bbsgroup/student-online";
	}
	
	
}
