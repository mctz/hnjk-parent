package com.hnjk.edu.teaching.controller;

import java.io.File;
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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.taglib.tree.TreeNode;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;

/**
 * 课程库管理.
 * <code>TeachingCourseController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-7-6 下午05:01:23
 * @see 
 * @version 1.0
 */
@Controller
public class CourseController  extends BaseSupportController{

	
	private static final long serialVersionUID = -8696918255222952278L;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	/**
	 * 课程库列表
	 * @param courseName
	 * @param courseCode
	 * @param defaultTeacher
	 * @param status
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/list.html")
	public String listCourse(String hasResource,String courseName,String courseCode,String defaultTeacher,String status,String courseType,String courseType2, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("courseCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(courseName)){
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(courseCode)){
			condition.put("courseCode", courseCode);
		}

		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", status);
		}
		courseType2 = ExStringUtils.trimToEmpty(courseType2);
		if(ExStringUtils.isNotEmpty(courseType2)){
			if("unitExam".equals(courseType2)) {
				condition.put("isUniteExam", "Y");//统考
			}
			if("degreeUnitExam".equals(courseType2)) {
				condition.put("isDegreeUnitExam", "Y");//学位统考
			}
			if("practice".equals(courseType2)) {
				condition.put("isPractice", "Y");//实践
			}
		}
		if (ExStringUtils.isNotEmpty(courseType)) {
			condition.put("courseType", courseType);
		}
		if (ExStringUtils.isNotEmpty(hasResource)) {
			condition.put("hasResource", hasResource);
		}
		
		Page courseListPage = courseService.findCourseByCondition(condition, objPage);
		
		model.addAttribute("courseListPage", courseListPage);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/teachingcourse/teachingcourse-list";
	}
	
	/**
	 * 课程新增编辑表单
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/edit.html")
	public String editCourse(HttpServletRequest request,String resourceid,ModelMap model) throws WebException{
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			Course course = courseService.load(resourceid);	
			model.addAttribute("teachingCourse", course);
			
			model.addAttribute("storePath", course.getCourseCode());
			model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
		}else{ //----------------------------------------新增
			Course course = new Course();
			course.setCourseType("11");
			course.setStatus(1L);
			course.setPlanoutCreditHour(3.0);
			model.addAttribute("teachingCourse", course);
		}
		model.addAttribute("schoolCode", schoolCode);
		return "/edu3/teaching/teachingcourse/teachingcourse-form";
	}
	
	/**
	 * 保存课程
	 * @param course
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/save.html")
	public void saveCourse(Course course,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		try {
			if(course.getStatus()==2){//课程停用时
				course.setStopTime(new Date());
			}
			if(ExStringUtils.isNotBlank(course.getResourceid())){ //--------------------更新
				Course persistCourse = courseService.get(course.getResourceid());				
				ExBeanUtils.copyProperties(persistCourse, course);				
				courseService.update(persistCourse);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.INSERT,"更新课程：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}else{ //-------------------------------------------------------------------保存
				String hql = "from "+Course.class.getSimpleName()+" c where isDeleted=0 and courseCode=?";
				List<Course> courseList = courseService.findByHql(hql, course.getCourseCode());
				if(courseList!=null && courseList.size()>0){
					statusCode = 300;
					message = "课程编码已存在！";
				}else {
					courseService.save(course);
				}
				
			}
			map.put("statusCode", statusCode);
			map.put("message", message);
			map.put("navTabId", "RES_TEACHING_ESTAB_COURSE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingcourse/edit.html?resourceid="+course.getResourceid());
		}catch (Exception e) {
			logger.error("保存课程库出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 设置辅导老师
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/setAssistant.html")
	public void setAssistant(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String courseId = request.getParameter("courseId");
//			String assistantIds = request.getParameter("assistantIds");
//			String assistantNames = request.getParameter("assistantNames");
			if(ExStringUtils.isNotBlank(courseId)){ //--------------------更新
				Course course = courseService.get(courseId);	
//				course.setAssistantIds(assistantIds);
//				course.setAssistantNames(assistantNames);				
				courseService.update(course);
			}
			map.put("statusCode", 200);
			map.put("message", "设置辅导老师成功！");	
			map.put("forward", request.getContextPath()+"/edu3/teaching/teachingcourse/list.html");
		}catch (Exception e) {
			logger.error("设置辅导老师出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查课程编码的唯一性
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/validateCode.html")
	public void validateCourseCode(String courseCode,HttpServletRequest request,HttpServletResponse response) throws WebException{
		
		renderJson(response, Boolean.toString(courseService.isExistsCourseCode(ExStringUtils.trimToEmpty(courseCode))));
	}
	
	/**
	 * 查看课程具体信息
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/view.html")
	public String viewCourse(String courseId,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(courseId)){ 
			Course course = courseService.load(courseId);	
			model.addAttribute("course", course);			
		}
		return "/edu3/teaching/teachingcourse/teachingcourse-view";
	}
	
	/**
	 * 查看用户具体信息
	 * @param courseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/edumanager/view.html")
	public String viewTeacher(String userId,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(userId)){ 
			Edumanager edumanager = edumanagerService.load(userId);	
			model.addAttribute("edumanager", edumanager);			
		}
		return "/edu3/teaching/teacher/edumanager-view";
	}
	
	/**
	 * 删除课程
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/delete.html")
	public void removeCourse(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					courseService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					courseService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除课程：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachingcourse/list.html");
			}
		} catch (Exception e) {
			logger.error("删除课程出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 获得所有课程(用于文本框自动提示)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/getAllCourse.html")
	public void getAllSchool(HttpServletRequest request,HttpServletResponse response) throws Exception {
		List<Course> courses = courseService.getAll();	
		List<JsonModel> jsonList = new ArrayList<JsonModel>();
		for (Course c : courses) {
			jsonList.add(new JsonModel(c.getCourseShortName(), c.getCourseName(), c.getResourceid()));
		}		
		renderJson(response, JsonUtils.listToJson(jsonList));
	}

	
	/**
	 * 设置课程状态 停用/启用
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/enabled.html")
	public void enableCourse(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			List<String> idList = new ArrayList<String>();
			if(ExStringUtils.isNotBlank(resourceid)){
				String type = ExStringUtils.defaultIfEmpty(request.getParameter("type"), "");	
				
				String[] arr = resourceid.split("\\,");
				for (int i = 0; i < arr.length; i++) {
					idList.add(arr[i]);
				}
				if("enable".equals(type)){
					courseService.enableCourse(idList, true);
				}else if("disenable".equals(type)){
					courseService.enableCourse(idList, false);
				}				
			}
			map.put("statusCode", 200);
			map.put("message", "操作成功！");				
			map.put("forward", request.getContextPath()+"/edu3/teaching/teachingcourse/list.html");
		} catch (Exception e) {
			logger.error("设置课程状态失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败<br/>"+e.getLocalizedMessage());				
		
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 老师选择对话框
	 * @param objPage
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/teacher.html")
	public String listTeacher(Page objPage,HttpServletRequest request) throws Exception{
		objPage.setOrderBy("teacherCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String unitId=request.getParameter("unitId");//学习中心
		String titleOfTechnical=request.getParameter("titleOfTechnical");//职称
		String gender=request.getParameter("gender");//性别
		String cnName=request.getParameter("cnName");//姓名
		
		if(ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if(ExStringUtils.isNotEmpty(titleOfTechnical)) {
			condition.put("titleOfTechnical", titleOfTechnical);
		}
		if(ExStringUtils.isNotEmpty(gender)) {
			condition.put("gender", gender);
		}
		if(ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName", cnName);
		}
		condition.put("roleCode", CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue());
		condition.put("isDeleted", 0);
		
		Page page = edumanagerService.findEdumanagerByCondition(condition, objPage);		
			
		request.setAttribute("teacherlist", page);
		request.setAttribute("condition", condition);
		return "/edu3/teaching/teachingcourse/selector_teachers";
	}
	
	/**
	 * 建立知识结构树
	 * @param courseId 课程id
	 * @param syllabusId 父节点id
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/addsyllabus.html")
	public String listSyllabus(String courseId,String syllabusId,String parentSyllabusId,String resourceid,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		List<Syllabus> sybs = syllabusService.findSyllabusTreeList(courseId);
		if(sybs==null||sybs.size()<1){	//列表为空，新增教学大纲根节点	
			Syllabus syllabus = new Syllabus();	
			Course course = courseService.load(courseId);
			syllabus.setCourse(course);
			syllabus.setSyllabusName(course.getCourseName());
			syllabus.setSyllabusLevel(0L);
			syllabusService.save(syllabus);
			sybs.add(syllabus);			
		} 
		
		List<TreeNode> treeNodes = new ArrayList<TreeNode>();//树列表	
		for(Syllabus s : sybs){
			String parentId = "";
			boolean isLeaf = true;
			
			if(null != s.getParent()) {
				parentId = s.getParent().getResourceid();
			}
			if("Y".equals(s.getIsChild())) {
				isLeaf = false;
			}
			
			treeNodes.add(new TreeNode(s.getResourceid(), s.getNodeName(), parentId,s.getNodeLevel().intValue(), isLeaf, "goSyllabusSelected('"+s.getCourse().getResourceid()+"','"+s.getResourceid()+"');"));
		} 			
		model.addAttribute("syllabustree", treeNodes);
		model.addAttribute("syllabusList", sybs);
		//分页查询
		objPage.setOrderBy("syllabusLevel,showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("courseId", courseId);
		String nodeName = request.getParameter("nodeName");
		String required = request.getParameter("required");
		String abilityTarget = request.getParameter("abilityTarget");
		
		if(ExStringUtils.isNotEmpty(syllabusId)) {
			condition.put("syllabusId", syllabusId);
			Syllabus syllabus = syllabusService.get(syllabusId);
			model.addAttribute("syllabus", syllabus);
		} else {
			if(ExStringUtils.isNotEmpty(parentSyllabusId)) { //新增
				condition.put("syllabusId", parentSyllabusId);
				Syllabus syllabus = new Syllabus();
				syllabus.setShowOrder(syllabusService.getNextShowOrder(parentSyllabusId));
				syllabus.setCourse(courseService.get(courseId));	
				syllabus.setParent(syllabusService.get(parentSyllabusId));
				Long nodeLevle = syllabus.getParent().getSyllabusLevel();
				nodeLevle = (nodeLevle+1 > 3L)? 3L :nodeLevle+1;
				syllabus.setSyllabusType(nodeLevle.toString());
				//syllabus.setAbilityTarget("1");
				//syllabus.setRequired("1");
				model.addAttribute("syllabus", syllabus);
			} else if(ExStringUtils.isNotEmpty(resourceid)) { //编辑
				Syllabus syllabus = syllabusService.get(resourceid);
				model.addAttribute("syllabus", syllabus);				
				if(syllabus.getParent()!=null) {
					condition.put("syllabusId",syllabus.getParent().getResourceid());
				}
			} else{
				condition.put("syllabusId",syllabusService.getSyllabusRoot(courseId).getResourceid());
			}
		}		
		if(ExStringUtils.isNotEmpty(nodeName)) {
			condition.put("nodeName", nodeName);
		}
		if(ExStringUtils.isNotEmpty(required)) {
			condition.put("required", required);
		}
		if(ExStringUtils.isNotEmpty(abilityTarget)) {
			condition.put("abilityTarget", abilityTarget);
		}
		
		Page page = syllabusService.findSyllabusByCondition(condition, objPage);
		
		model.addAttribute("syllabuslist", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/teachingcourse/syllabus-list";
	}
	
	/**
	 * 新增编辑课程教学大纲
	 * @param courseId
	 * @param resourceid
	 * @param syllabusId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/syllabus/input.html")
	public String editSyllabus(String courseId,String resourceid,String parentSyllabusId, ModelMap model) throws WebException{
		Syllabus syllabus = null;		
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			syllabus = syllabusService.load(resourceid);
			if(ExStringUtils.isEmpty(parentSyllabusId) && syllabus.getParent()!=null){
				parentSyllabusId = syllabus.getParent().getResourceid();
			}
		}else{ //----------------------------------------新增	
			Course course = courseService.load(courseId);
			syllabus = new Syllabus();
			syllabus.setShowOrder(syllabusService.getNextShowOrder(parentSyllabusId));
			syllabus.setCourse(course);	
		}
		
		Syllabus parentSyllabus = null;
		if(ExStringUtils.isNotEmpty(parentSyllabusId)){
			parentSyllabus = syllabusService.get(parentSyllabusId);
		}
		syllabus.setParent(parentSyllabus);
		model.addAttribute("syllabus", syllabus);
		
		List<Syllabus> sybs = syllabusService.findSyllabusTreeList(syllabus.getCourse().getResourceid());
		model.addAttribute("syllabusList", sybs);
		return "/edu3/teaching/teachingcourse/syllabus-form";
	}
	/**
	 * 保存教学大纲节点
	 * @param syllabus
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/syllabus/save.html")
	public void syllabusSave(Syllabus syllabus,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();		
		try {
			boolean isUpdate = ExStringUtils.isNotBlank(syllabus.getResourceid());
			syllabusService.saveOrUpdateSyllabus(syllabus);		
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("reloadUrl", request.getContextPath()+"/edu3/teaching/teachingcourse/addsyllabus.html?parentSyllabusId="+syllabus.getParentId()+"&courseId="+syllabus.getCourseId());
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", isUpdate?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, syllabus);
		}catch (Exception e) {
			logger.error("保存教学大纲节点出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除教学大纲节点
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/syllabus/delete.html")
	public void removeSyllabus(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除	并更新父节点		
					syllabusService.batchCascadeDelete(resourceid.split("\\,"));
				}else{//单个删除
					syllabusService.deleteSyllabus(resourceid,1);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/teachingcourse/addsyllabus.html");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "Syllabus: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除教学大纲节点出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	
	/**
	 * 选择Excel的导出方式
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	/*
	@RequestMapping("/edu3/teaching/teachingcourse/excel/exportchoice.html")
	public String doChoiceCourse(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		return "/edu3/teaching/teachingcourse/exportchoice";
	}
	*/
	/**
	 * Excel导出课程表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/excel/export.html")
	public void doExcelExportCourse(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		//从库中查出数据		
		List<Course> list = courseService.getAllBycriteria();
		/*不知道要不要过滤课程的启用状态
		List<Course> list_tmp= courseService.getAllBycriteria();
		List<Course> list    = new ArrayList<Course>();
		for (Course course : list_tmp) {
			if(course.getStatus()==1){
				list.add(course);
			}
		}
		*/
		Long showOrderNo = new Long(0);
		for (Course course : list) {
			showOrderNo++;
			course.setShowOrderNo(showOrderNo);
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			if("default".equals(exportAct)){//默认导出	
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeCourseState");
				dictCodeList.add("CodeExamForm");
				Map<String , Object> map1 = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				map1.put("CodeExamForm_0", "开卷");
				map1.put("CodeExamForm_1", "闭卷");
				map1.put("CodeExamForm_2", "口试");
				map1.put("CodeExamForm_3", "大作业");
				List<Integer> list3 = new ArrayList<Integer>();
				
				exportExcelService.initParmasByfile(disFile, "teachingcourse", list,map1);
				exportExcelService.getModelToExcel().setHeader("课程信息表");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			}
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "课程信息表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
	/**
	 * 跳转到上传页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	
	@RequestMapping("/edu3/teaching/teachingcourse/addCover.html")
	public String addCover(HttpServletRequest request,HttpServletResponse response,ModelMap model,String courseId){
		Course course = null;
		if(ExStringUtils.isNotBlank(courseId)){ 
			course = courseService.load(courseId);	
			model.addAttribute("course", course);
		}	
		model.addAttribute("storePath", course.getCourseCode());
		/*model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
		*/
		//本地测试
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+"/cover/");
		return "/edu3/teaching/teachingcourse/teachingcourse-cover-upload";
	}
	
	/**
	 * 保存上传课程封面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	
	@RequestMapping("/edu3/teaching/teachingcourse/saveCover.html")
	public void saveCover(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String ,Object> map = new HashMap<String, Object>();
		String courseId= request.getParameter("courseId");
		if(ExStringUtils.isNotBlank(courseId)){ 
			Course course = courseService.load(courseId);	
			course.setCover(request.getParameter("courseCover"));
			courseService.save(course);
			map.put("statusCode", 200);
			map.put("message", "设置成功！");
		}else{
			map.put("statusCode", 300);
			map.put("message", "没有获取到课程联系管理员");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
		
}
