package com.hnjk.edu.learning.controller;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.m3u8.utils.PlayerM3U8Uitls;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseLearningGuid;
import com.hnjk.edu.learning.model.CourseReference;
import com.hnjk.edu.learning.model.MateResource;
import com.hnjk.edu.learning.service.IActiveCourseExamService;
import com.hnjk.edu.learning.service.ICourseLearningGuidService;
import com.hnjk.edu.learning.service.ICourseReferenceService;
import com.hnjk.edu.learning.service.IMateResourceService;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.extend.taglib.tree.TreeNode;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 课程素材管理
 * <code>MateResourceController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午05:17:22
 * @see 
 * @version 1.0
 */
@Controller
public class MateResourceController extends BaseSupportController {

	private static final long serialVersionUID = -2654135051018767480L;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	@Autowired
	@Qualifier("mateResourceService")
	private IMateResourceService mateResourceService;
	
	@Autowired
	@Qualifier("courseReferenceService")
	private ICourseReferenceService courseReferenceService;
	
	@Autowired
	@Qualifier("courseLearningGuidService")
	private ICourseLearningGuidService courseLearningGuidService;
	
	@Autowired
	@Qualifier("activeCourseExamService")
	private IActiveCourseExamService activeCourseExamService;
	
	/**
	 * 用户可供选择的课程列表
	 * @param courseName
	 * @param courseCode
	 * @param status
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/listCourse.html")
	public String listCourse(String courseName,String courseCode,String status,String hasResource,String isQualityResource, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("courseCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		User user = SpringSecurityHelper.getCurrentUser();
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();
		if(user != null){
			if(SpringSecurityHelper.isUserInRole(roleCode)) {
				condition.put("teacherId", user.getResourceid());
			}
			if(ExStringUtils.isNotEmpty(courseName)) {
				condition.put("courseName", courseName);
			}
			if(ExStringUtils.isNotEmpty(courseCode)) {
				condition.put("courseCode", courseCode);
			}
			if(ExStringUtils.isNotEmpty(status)) {
				condition.put("status", status);
			}
			if(ExStringUtils.isNotEmpty(hasResource)) {
				condition.put("hasResource", hasResource);
			}
			if(ExStringUtils.isNotEmpty(isQualityResource)) {
				condition.put("isQualityResource", isQualityResource);
			}
					
			Page courseListPage = courseService.findCourseByHql(condition, objPage);
			
			model.addAttribute("courseListPage", courseListPage);
			model.addAttribute("condition", condition);
		}			
		return "/edu3/learning/materesource/teachercourse-list";
	}
	/**
	 * 课程素材资源列表
	 * @param courseId
	 * @param syllabusId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/list.html")
	public String listMateResource(String courseId,String syllabusId,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "1");
		List<Syllabus> sybs = syllabusService.findSyllabusTreeList(courseId);
		if(sybs != null && sybs.size() > 0){	
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
				
				treeNodes.add(new TreeNode(s.getResourceid(), s.getSyllabusName(), parentId,s.getSyllabusLevel().intValue(), isLeaf, "goCoursewareSelected('"+s.getCourse().getResourceid()+"','"+s.getResourceid()+"');"));
			} 			
			model.addAttribute("materesourcetree", treeNodes);
			
			//查询条件
			Map<String,Object> condition = new HashMap<String,Object>();	
			if(ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if(ExStringUtils.isNotEmpty(syllabusId)) {
				condition.put("syllabusId", syllabusId);
			}
			condition.put("currentIndex", currentIndex);
			
			model.addAttribute("condition", condition);
			model.addAttribute("syllabusList", sybs);
		} 
		return "/edu3/learning/materesource/materesource-list";
	}
	
	/**
	 * 查询列表
	 * @param courseId
	 * @param syllabusId
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/metares/list.html")
	public String listMate(String courseId,String syllabusId,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		List<Syllabus> sybs = syllabusService.findSyllabusTreeList(courseId);
		model.addAttribute("syllabusList", sybs);
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		Syllabus syllabus = null;//知识节点
		Course course = null;
		String currentIndex = ExStringUtils.defaultIfEmpty(request.getParameter("currentIndex"), "0");
		condition.put("currentIndex", currentIndex);		
		if(ExStringUtils.isNotEmpty(syllabusId)) {
			syllabus = syllabusService.get(syllabusId);
			condition.put("syllabusId", syllabusId);
			course = syllabus.getCourse();
		}				
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
			if(course==null) {
				course = courseService.get(courseId);
			}
		}
		model.addAttribute("course", course);
		String resourceid = request.getParameter("resourceid");
		if("0".equals(currentIndex)){
			dealCourseLearningGuid(model, condition, syllabus, resourceid,objPage);
			return "/edu3/learning/materesource/courselearningguid-list";
		} else if("1".equals(currentIndex)){
			dealMateResource(model, condition, syllabus, resourceid,objPage);
			return "/edu3/learning/materesource/coursemateresource-list";
		} else if("2".equals(currentIndex)){
			dealActiveCourseExam(model, condition, syllabus, resourceid,objPage);
			return "/edu3/learning/materesource/activecourseexam-list";
		} else {		
			dealCourseReference(courseId, model, condition, syllabus,resourceid,objPage);
			return "/edu3/learning/materesource/coursereference-list";
		}
	}
	/**
	 * 随堂练习
	 * @param model
	 * @param condition
	 * @param syllabus
	 * @param resourceid
	 */
	private void dealActiveCourseExam(ModelMap model,Map<String, Object> condition, Syllabus syllabus, String resourceid,Page objPage) {
		objPage.setOrderBy(" syllabus.resourceid,showOrder,resourceid ");
		objPage.setOrder(Page.ASC);
		model.addAttribute("resultList",activeCourseExamService.findActiveCourseExamByCondition(condition,objPage));
		
		model.addAttribute("condition", condition);
	}
	/**
	 * 参考资料相关
	 * @param courseId
	 * @param model
	 * @param condition
	 * @param syllabus
	 * @param resourceid
	 */
	private void dealCourseReference(String courseId, ModelMap model,Map<String, Object> condition, Syllabus syllabus, String resourceid,Page objPage) {
		objPage.setOrderBy(" syllabus.resourceid,course.courseName,referenceType,referenceName,resourceid ");
		objPage.setOrder(Page.ASC);
		condition.put("notNullSyllabus", Constants.BOOLEAN_YES);
		model.addAttribute("resultList",courseReferenceService.findCourseReferenceByCondition(condition,objPage));
		
		CourseReference courseReference = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseReference = courseReferenceService.get(resourceid);			
		}else{ //----------------------------------------新增
			Course course = courseService.get(courseId);
			courseReference = new CourseReference();
			courseReference.setCourse(course);
			courseReference.setSyllabus(syllabus);
		}
		model.addAttribute("courseReference", courseReference);	
		model.addAttribute("condition", condition);
	}
	/**
	 * 学习材料相关数据
	 * @param model
	 * @param condition
	 * @param syllabus
	 * @param resourceid
	 */
	private void dealMateResource(ModelMap model,Map<String, Object> condition, Syllabus syllabus, String resourceid,Page objPage) {
		objPage.setOrderBy(" syllabus.syllabusName,showOrder,resourceid ");
		objPage.setOrder(Page.ASC);
		condition.put("channelType", "meta");
		model.addAttribute("resultList", mateResourceService.findMateResourceByCondition(condition,objPage));
					
		MateResource mateResource = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			mateResource = mateResourceService.get(resourceid);
		}else{ //----------------------------------------新增
			mateResource = new MateResource();
			mateResource.setMateType("2");
			mateResource.setIsPublished(Constants.BOOLEAN_YES);
			mateResource.setSyllabus(syllabus);
			mateResource.setChannelType("meta");
			if(syllabus!=null){
				mateResource.setShowOrder(mateResourceService.getNextShowOrder(syllabus.getResourceid(),"meta"));
			}
		}
		model.addAttribute("mateResource", mateResource);
		model.addAttribute("condition", condition);
	}
	/**
	 * 学习目标相关
	 * @param model
	 * @param condition
	 * @param syllabus
	 * @param resourceid
	 */
	private void dealCourseLearningGuid(ModelMap model,Map<String, Object> condition, Syllabus syllabus, String resourceid,Page objPage) {
		objPage.setOrderBy(" syllabus.syllabusName,resourceid ");
		objPage.setOrder(Page.ASC);
		model.addAttribute("resultList", courseLearningGuidService.findCourseLearningGuidByCondition(condition,objPage));
		
		CourseLearningGuid courseLearningGuid = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseLearningGuid = courseLearningGuidService.load(resourceid);			
		}else{ //----------------------------------------新增
			courseLearningGuid = new CourseLearningGuid();
			courseLearningGuid.setSyllabus(syllabus);
		}
		model.addAttribute("courseLearningGuid", courseLearningGuid);
		
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		model.addAttribute("condition", condition);
	}
	
	/**
	 * 检测该门课程是否已建立教学大纲
	 * @param courseId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/course/syllabus/ajax.html")
	public void isEmptySyllabus(String courseId,String hasResource,HttpServletResponse response) throws WebException {		
		boolean isempty = true;
		if(ExStringUtils.isNotBlank(courseId)){
			if(ExStringUtils.isNotBlank(hasResource)){ //查询课程是否有资源
				Course course = courseService.get(courseId);
				if(Constants.BOOLEAN_YES.equals(course.getHasResource())){
					isempty = false;
				}
			} else {
				Long count = syllabusService.countSyllabusTreeList(courseId);			
				if(count != null && count > 1){	
					isempty = false;
				}
			}
		}					
		renderJson(response, JsonUtils.booleanToJson(isempty));
	}
	/**
	 * 课程素材资源新增编辑
	 * @param syllabusId
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/input.html")
	public String editMateResource(String syllabusId,String resourceid, ModelMap model) throws WebException {
		MateResource mateResource = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			mateResource = mateResourceService.load(resourceid);
		}else{ //----------------------------------------新增
			mateResource = new MateResource();
			Syllabus syllabus = syllabusService.get(syllabusId);	
			mateResource.setSyllabus(syllabus);
			mateResource.setMateType("2");
			mateResource.setChannelType("meta");
		}
		model.addAttribute("mateResource", mateResource);	
		return "/edu3/learning/materesource/materesource-form";
	}
	
	/**
	 * 保存课程素材资源
	 * @param mateResource
	 * @param syllabusId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/save.html")
	public void saveMateResource(MateResource mateResource,String syllabusId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			boolean isUpdate = ExStringUtils.isNotBlank(mateResource.getResourceid());
			if(ExStringUtils.isNotBlank(mateResource.getResourceid())){ //--------------------更新
				MateResource preMateResource = mateResourceService.get(mateResource.getResourceid());
				if(ExStringUtils.isNotEmpty(syllabusId)){
					Syllabus syllabus = syllabusService.get(syllabusId);
					mateResource.setSyllabus(syllabus);
				}
				ExBeanUtils.copyProperties(preMateResource, mateResource);
				if("2".equals(preMateResource.getMateType())){
					int totalTime = PlayerM3U8Uitls.downloadM3u8(preMateResource.getMateUrl());
					preMateResource.setTotalTime(totalTime);
				}
				mateResourceService.update(preMateResource);
			}else{ //-------------------------------------------------------------------保存
				if(ExStringUtils.isNotEmpty(syllabusId)){
					Syllabus syllabus = syllabusService.get(syllabusId);
					mateResource.setSyllabus(syllabus);
				}
				if("2".equals(mateResource.getMateType())){
					int totalTime = PlayerM3U8Uitls.downloadM3u8(mateResource.getMateUrl());
					mateResource.setTotalTime(totalTime);
				}
				mateResourceService.save(mateResource);
			}
			map.put("statusCode", 200);
			map.put("message", "保存课程素材成功！");
			
			String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String parentSyllabusId = ExStringUtils.trimToEmpty(request.getParameter("parentSyllabusId"));
			map.put("currentIndex", "1");
			map.put("syllabusId", parentSyllabusId);
			map.put("courseId", courseId);
			
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", isUpdate?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, mateResource);
		}catch (Exception e) {
			logger.error("保存课程素材出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除课程素材资源
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/remove.html")
	public void removeMateResource(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){								
				mateResourceService.batchCascadeDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/courseware/list.html");
				
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				String syllabusId = ExStringUtils.trimToEmpty(request.getParameter("syllabusId"));
				map.put("currentIndex", "1");
				map.put("syllabusId", syllabusId);
				map.put("courseId", courseId);
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, MateResource.class.getSimpleName()+": "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除课程素材出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查编码唯一性
	 * @param mateCode
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/validateCode.html")
	public void validateBbsSectionCode(String mateCode,HttpServletResponse response) throws WebException{
		renderJson(response, Boolean.toString(mateResourceService.isExistsMateCode(mateCode)));
	}
	
	/**
	 * 预览
	 * @param mateType
	 * @param mateUrl
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/metares/courseware/view.html")
	public String viewMateResource(String mateType,String mateUrl, ModelMap model) throws WebException {
		String resourceUrl = ExStringUtils.trimToEmpty(CacheAppManager.getSysConfigurationByCode("resourceUrl").getParamValue());
		
		model.addAttribute("mateType", mateType);
		model.addAttribute("mateUrl", mateUrl);
		model.addAttribute("resourceUrl", resourceUrl);
		return "/edu3/learning/materesource/materesource-view";
	}
	/**
	 * 课件复制课程选择
	 * @param fromCourseId
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/teachingcourse/copy/select.html")
	public String selectCopyCourse(String fromCourseId, ModelMap model) throws WebException {
		if(ExStringUtils.isNotBlank(fromCourseId)){//原始课程
			Course fromCourse = courseService.get(fromCourseId);
			model.addAttribute("fromCourse",fromCourse);
		}
		return "/edu3/learning/materesource/course-copy-select";
	}
	
	/**
	 * 复制课程及课件资源
	 * @param fromCourseId
	 * @param toCourseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/teachingcourse/copy.html")
	public void copyCourse(String fromCourseId,String toCourseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(fromCourseId) && ExStringUtils.isNotBlank(toCourseId)){								
				mateResourceService.copyCourse(fromCourseId,toCourseId);
				map.put("statusCode", 200);
				map.put("message", "复制课件成功！");		
				map.put("callbackType", "forward");
				map.put("forwardUrl", request.getContextPath()+"/edu3/teaching/teachingcourse/list.html");
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.COPY, "复制课件:\nfrom Course: "+fromCourseId+"\nto Course: "+toCourseId);
			}
		} catch (Exception e) {
			logger.error("复制课件出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "复制课件出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 教师复习总结录像
	 * @param reviseCourseId
	 * @param resourceid
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/materevise/list.html")
	public String listMateRevise(String reviseCourseId, String resourceid, HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy(" course.courseCode,showOrder,resourceid ");
		objPage.setOrder(Page.ASC);
		
		Map<String,Object> condition = new HashMap<String,Object>();
		if(ExStringUtils.isNotEmpty(reviseCourseId)){
			condition.put("reviseCourseId", reviseCourseId);
		}
		condition.put("channelType", "revise");
		
		Page page = mateResourceService.findMateResourceByCondition(condition,objPage);
		model.addAttribute("reviseList", page);
					
		MateResource mateRevise = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			mateRevise = mateResourceService.get(resourceid);
		}else{ //----------------------------------------新增
			mateRevise = new MateResource();
			mateRevise.setMateType("2");
			mateRevise.setIsPublished(Constants.BOOLEAN_YES);
			mateRevise.setChannelType("revise");
			if(reviseCourseId != null){
				mateRevise.setCourse(courseService.get(reviseCourseId));
				mateRevise.setShowOrder(mateResourceService.getNextShowOrder(reviseCourseId,"revise"));
			}
		}
		model.addAttribute("mateRevise", mateRevise);
		model.addAttribute("condition", condition);
		return "/edu3/learning/materevise/materevise-list";
	}
	/**
	 * 保存教师复习总结录像
	 * @param mateRevise
	 * @param reviseCourseId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/materevise/save.html")
	public void saveMateRevise(MateResource mateRevise,String reviseCourseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			boolean isUpdate = ExStringUtils.isNotBlank(mateRevise.getResourceid());
			if(ExStringUtils.isNotEmpty(reviseCourseId)){
				Course course = courseService.get(reviseCourseId);
				mateRevise.setCourse(course);
			}
			if(ExStringUtils.isNotBlank(mateRevise.getResourceid())){ //--------------------更新
				MateResource preMateRevise = mateResourceService.get(mateRevise.getResourceid());				
				ExBeanUtils.copyProperties(preMateRevise, mateRevise);
				mateResourceService.update(preMateRevise);
			}else{ //-------------------------------------------------------------------保存
				mateResourceService.save(mateRevise);
			}
			map.put("statusCode", 200);
			map.put("message", "保存教师复习总结录像成功！");	
			map.put("navTabId", "RES_LEARNING_MATE_REVISE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/learning/materevise/list.html?reviseCourseId="+ExStringUtils.trimToEmpty(reviseCourseId));
			
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", isUpdate?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, mateRevise);
		}catch (Exception e) {
			logger.error("保存教师复习总结录像出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除教师复习总结录像
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/materevise/remove.html")
	public void removeMateRevise(String resourceid, String reviseCourseId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){								
				mateResourceService.batchCascadeDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath() +"/edu3/learning/materevise/list.html?reviseCourseId="+ExStringUtils.trimToEmpty(reviseCourseId));
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "MateResource: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除教师复习总结录像出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 计算视频时长
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/materevise/calculateVideoTime.html")
	public void calculateVideoTime(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String ,Object> condition = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "计算视频时长成功！";
		
		try {
			String courseIds = ExStringUtils.trimToEmpty(request.getParameter("courseIds"));
			condition.put("courseIds", Arrays.asList(courseIds.split(",")));
			List<MateResource> updateList = new ArrayList<MateResource>();
			List<MateResource> mateResourceList = mateResourceService.findMateResourceByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(mateResourceList)){
				for(MateResource mr : mateResourceList){
					// 获取视频总时长
					if("2".equals(mr.getMateType())){//视频
						int totalTime = PlayerM3U8Uitls.downloadM3u8(mr.getMateUrl());
						mr.setTotalTime(totalTime);
						updateList.add(mr);
					} else if ("7".equals(mr.getMateType())) {//视频(三分屏)
						int totalTime = PlayerM3U8Uitls.downloadM3u8(mr.getMateUrl());
						mr.setTotalTime(totalTime);
					} else if ("8".equals(mr.getMateType())) {//视频(网页)
						int totalTime = PlayerM3U8Uitls.downloadM3u8(mr.getMateUrl());
						mr.setTotalTime(totalTime);
					}
				}
				mateResourceService.batchSaveOrUpdate(updateList);
			}
		} catch (ServiceException e) {
			logger.error("计算视频时长出错",e);
			statusCode = 300;
			message = "计算视频时长失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
