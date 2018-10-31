package com.hnjk.edu.learning.controller;

import java.util.ArrayList;
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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseReference;
import com.hnjk.edu.learning.service.ICourseReferenceService;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
/**
 * 课程参考资料管理
 * <code>CourseReferenceController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-11-19 上午10:33:05
 * @see 
 * @version 1.0
 */
@Controller
public class CourseReferenceController extends BaseSupportController {

	private static final long serialVersionUID = -704093113812346659L;

	@Autowired
	@Qualifier("courseReferenceService")
	private ICourseReferenceService courseReferenceService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	/**
	 * 课程参考资料列表
	 * @param courseId
	 * @param resourceid
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/coursereference/list.html")
	public String listCourseReference(String courseId,String resourceid,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("course,referenceType,referenceName");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		String referenceName = request.getParameter("referenceName");
		String referenceType = request.getParameter("referenceType");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(referenceName)) {
			condition.put("referenceName", referenceName);
		}
		if(ExStringUtils.isNotEmpty(referenceType)) {
			condition.put("referenceType", referenceType);
		}
		condition.put("nullSyllabusId", "");
		
		Page courseReferencePage = courseReferenceService.findCourseReferenceByCondition(condition, objPage);
		
		model.addAttribute("courseReferencePage", courseReferencePage);
		model.addAttribute("condition", condition);
		
		CourseReference courseReference = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseReference = courseReferenceService.get(resourceid);			
		}else{ //----------------------------------------新增
			Course course = courseService.get(courseId);
			courseReference = new CourseReference();
			courseReference.setCourse(course);
		}
		List<Dictionary> typesList = getSpecialReferenceTypes();
		model.addAttribute("typesList", typesList);	
		model.addAttribute("courseReference", courseReference);		
		return "/edu3/learning/coursereference/coursereference-list";
	}
	//获取参考资料类型字典值
	private List<Dictionary> getSpecialReferenceTypes() {
		List<Dictionary> types = CacheAppManager.getChildren("CodeReferenceType");
		List<Dictionary> typesList = new ArrayList<Dictionary>();
		for (Dictionary dictionary : types) {
			if(!("reference_doc".equalsIgnoreCase(dictionary.getDictValue()) || "reference_web".equalsIgnoreCase(dictionary.getDictValue()))){
				typesList.add(dictionary);
			}
		}
		return typesList;
	}
	/**
	 * 保存参考资料
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/coursereference/save.html")
	public void saveCourseReference(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String courseId = request.getParameter("courseId");	
			String syllabusId = request.getParameter("syllabusId");	
			String resourceid = request.getParameter("resourceid");
			String referenceType = request.getParameter("referenceType");
			String from = request.getParameter("from");
			Course course = courseService.get(courseId);
			Syllabus syllabus = null;
			if(ExStringUtils.isNotEmpty(syllabusId)) {
				syllabus = syllabusService.get(syllabusId);
			}
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				String referenceName = request.getParameter("referenceName");				
				String url = request.getParameter("url");
				
				CourseReference courseReference = courseReferenceService.get(resourceid);
				courseReference.setReferenceName(referenceName);
				courseReference.setReferenceType(referenceType);
				courseReference.setUrl(url);		
				courseReference.setSyllabus(syllabus);
				courseReferenceService.update(courseReference);		
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.UPDATE, courseReference);
			}else{ //-------------------------------------------------------保存
				String[] urls = request.getParameterValues("url");
				String[] referenceNames = request.getParameterValues("referenceName");
				if(referenceNames!=null&&referenceNames.length>0&&urls!=null&&urls.length>0){		
					List<CourseReference> list = new ArrayList<CourseReference>();
					for (int i = 0; i < referenceNames.length; i++) {
						if(ExStringUtils.isNotEmpty(referenceNames[i])&&ExStringUtils.isNotEmpty(urls[i])){
							CourseReference courseReference = new CourseReference();
							courseReference.setReferenceName(referenceNames[i]);
							courseReference.setReferenceType(referenceType);
							courseReference.setUrl(urls[i]);
							courseReference.setCourse(course);
							courseReference.setSyllabus(syllabus);
							list.add(courseReference);
						}
					}
					courseReferenceService.batchSaveOrUpdate(list);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.INSERT, list);
				}
			}			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			if(ExStringUtils.isNotEmpty(from)){
				map.put("navTabId", "courseReference");
				map.put("reloadUrl", request.getContextPath() +"/edu3/learning/coursereference/list.html?courseId="+course.getResourceid());
			} else {		
				map.put("currentIndex", "3");
				map.put("syllabusId", syllabusId);				
				map.put("courseId", course.getResourceid());
			}			
		}catch (Exception e) {
			logger.error("保存课程参考资料出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除参考资料
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/coursereference/remove.html")
	public void removeCourseReference(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){	
				courseReferenceService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/metares/courseware/list.html");
				
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				String syllabusId = ExStringUtils.trimToEmpty(request.getParameter("syllabusId"));
				map.put("currentIndex", "3");
				map.put("syllabusId", syllabusId);
				map.put("courseId", courseId);
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.INSERT, "CourseReference: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除课程参考资料出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
}
