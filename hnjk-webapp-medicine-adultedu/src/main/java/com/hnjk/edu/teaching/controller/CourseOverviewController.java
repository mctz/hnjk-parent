package com.hnjk.edu.teaching.controller;

import java.util.Date;
import java.util.HashMap;
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
import com.hnjk.edu.learning.model.CourseOverview;
import com.hnjk.edu.learning.service.ICourseOverviewService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * 课程概况管理
 * <code>CourseOverviewController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-2 上午11:34:14
 * @see 
 * @version 1.0
 */
@Controller
public class CourseOverviewController extends BaseSupportController {

	private static final long serialVersionUID = 7160276818688332479L;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("courseOverviewService")
	private ICourseOverviewService courseOverviewService;
	
	/**
	 * 课程概况列表
	 * @param courseId
	 * @param type
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseoverview/list.html")
	public String listCourseOverview(String courseId,String resourceid,String type,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("fillinDate");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(type)) {
			condition.put("type", type);
		}
		
		Page courseOverviewPage = courseOverviewService.findCourseOverviewByCondition(condition, objPage);
		
		model.addAttribute("courseOverviewPage", courseOverviewPage);
		model.addAttribute("condition", condition);
		
		
		Course course = null;
		CourseOverview courseOverview = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseOverview = courseOverviewService.get(resourceid);		
			course = courseOverview.getCourse();
		}else{ //----------------------------------------新增
			course = courseService.get(courseId);
			courseOverview = new CourseOverview();
			courseOverview.setCourse(course);
			courseOverview.setType("2");
		}
		model.addAttribute("courseOverview", courseOverview);
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		model.addAttribute("course", course);
		return "/edu3/teaching/courseoverview/courseoverview-list";
	}
	
	/**
	 * 新增编辑课程概况
	 * @param courseId
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseoverview/input.html")
	public String editCourseOverview(String courseId, String resourceid, ModelMap model) throws WebException{
		CourseOverview courseOverview = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseOverview = courseOverviewService.load(resourceid);			
		}else{ //----------------------------------------新增
			Course course = courseService.load(courseId);
			courseOverview = new CourseOverview();
			courseOverview.setCourse(course);
		}
		model.addAttribute("courseOverview", courseOverview);
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/teaching/courseoverview/courseoverview-form";
	}
	
	/**
	 * 保存课程概况
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseoverview/save.html")
	public void saveCourseOverview(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String courseId = request.getParameter("courseId");
			String type = request.getParameter("type");
			String content = request.getParameter("content");
			String resourceid = request.getParameter("resourceid");			
			CourseOverview courseOverview = new CourseOverview();
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				courseOverview = courseOverviewService.get(resourceid);
				if(!ExStringUtils.equals(courseOverview.getType(), type) && courseOverviewService.isExistsType(type, courseId)){
					throw new WebException("该类型已存在,请选择别的类型!");
				}
				courseOverview.setContent(content);
				courseOverview.setType(type);
				courseOverviewService.update(courseOverview);
			}else{ //-------------------------------------------------------------------保存
				if(courseOverviewService.isExistsType(type, courseId)){
					throw new WebException("该类型已存在,请选择别的类型!");
				}
				Course course = courseService.load(courseId);
				User user = SpringSecurityHelper.getCurrentUser();				
				courseOverview.setCourse(course);
				courseOverview.setContent(content);
				courseOverview.setType(type);
				courseOverview.setFillinDate(new Date());
				courseOverview.setFillinMan(user.getCnName());
				courseOverview.setFillinManId(user.getResourceid());
				courseOverviewService.save(courseOverview);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_COURSEOVERVIEW");
			//map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/courseoverview/input.html?resourceid="+courseOverview.getResourceid());
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/courseoverview/list.html?courseId="+courseOverview.getCourse().getResourceid());
			
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", ExStringUtils.isNotBlank(resourceid)?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, courseOverview);
		}catch (Exception e) {
			logger.error("保存课程概况出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除课程概况
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseoverview/delete.html")
	public void removeCourseOverview(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){				
				courseOverviewService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/courseoverview/list.html");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseOverview: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除课程概况出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	
}
