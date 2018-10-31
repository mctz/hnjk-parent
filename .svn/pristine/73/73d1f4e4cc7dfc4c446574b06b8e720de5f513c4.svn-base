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
import com.hnjk.edu.learning.model.CourseLearningGuid;
import com.hnjk.edu.learning.service.ICourseLearningGuidService;
import com.hnjk.edu.teaching.model.Syllabus;
import com.hnjk.edu.teaching.service.ISyllabusService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 课程学习指南管理
 * <code>CourseLearningGuidController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-2 上午11:29:03
 * @see 
 * @version 1.0
 */
@Controller
public class CourseLearningGuidController extends BaseSupportController {

	private static final long serialVersionUID = 4307427969582591063L;
	
	@Autowired
	@Qualifier("courseLearningGuidService")
	private ICourseLearningGuidService courseLearningGuidService;
	
	@Autowired
	@Qualifier("syllabusService")
	private ISyllabusService syllabusService;
	
	/**
	 * 课程学习指南列表
	 * @param syllabusId
	 * @param type
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courselearningguid/list.html")
	public String listCourseLearningGuid(String syllabusId,String type,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(syllabusId)){
			condition.put("syllabusId", syllabusId);
		}
		if(ExStringUtils.isNotEmpty(type)){
			condition.put("type", type);
		}
		
		Page courseLearningGuidPage = courseLearningGuidService.findCourseLearningGuidByCondition(condition, objPage);
		
		model.addAttribute("courseLearningGuidPage", courseLearningGuidPage);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/courselearningguid/courselearningguid-list";
	}
	
	/**
	 * 新增编辑学习指南
	 * @param syllabusId
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courselearningguid/input.html")
	public String editCourseLearningGuid(String syllabusId, String resourceid, ModelMap model) throws WebException{
		CourseLearningGuid courseLearningGuid = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseLearningGuid = courseLearningGuidService.load(resourceid);			
		}else{ //----------------------------------------新增
			Syllabus syllabus = syllabusService.load(syllabusId);
			courseLearningGuid = new CourseLearningGuid();
			courseLearningGuid.setSyllabus(syllabus);
		}
		model.addAttribute("courseLearningGuid", courseLearningGuid);
		User user = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("storeDir", user.getUsername());
		return "/edu3/teaching/courselearningguid/courselearningguid-form";
	}
	
	/**
	 * 保存学习指南
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courselearningguid/save.html")
	public void saveCourseLearningGuid(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String syllabusId = request.getParameter("syllabusId");
			String type = request.getParameter("type");
			String content = request.getParameter("content");
			String resourceid = request.getParameter("resourceid");
			CourseLearningGuid courseLearningGuid = new CourseLearningGuid();
			Syllabus syllabus = syllabusService.get(syllabusId);
			if(ExStringUtils.isNotBlank(resourceid)){ //--------------------更新
				courseLearningGuid = courseLearningGuidService.get(resourceid);
				courseLearningGuid.setSyllabus(syllabus);
				courseLearningGuid.setContent(content);
				courseLearningGuid.setType(type);
				courseLearningGuidService.update(courseLearningGuid);
			}else{ //-------------------------------------------------------------------保存				
				User user = SpringSecurityHelper.getCurrentUser();				
				courseLearningGuid.setSyllabus(syllabus);
				courseLearningGuid.setContent(content);
				courseLearningGuid.setType(type);
				courseLearningGuid.setFillinDate(new Date());
				courseLearningGuid.setFillinMan(user.getCnName());
				courseLearningGuid.setFillinManId(user.getResourceid());
				courseLearningGuidService.save(courseLearningGuid);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_COURSELEARNINGGUID");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/courselearningguid/input.html?resourceid="+courseLearningGuid.getResourceid());
			String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
			String parentSyllabusId = ExStringUtils.trimToEmpty(request.getParameter("parentSyllabusId"));
			map.put("currentIndex", "0");
			map.put("syllabusId", parentSyllabusId);
			map.put("courseId", courseId);
			
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", ExStringUtils.isNotBlank(resourceid)?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, courseLearningGuid);
		}catch (Exception e) {
			logger.error("保存学习指南出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除课程学习指南
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courselearningguid/delete.html")
	public void removeCourseLearningGuid(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){				
				courseLearningGuidService.batchCascadeDelete(resourceid.split("\\,"));				
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/teaching/courselearningguid/list.html");
				
				String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
				String syllabusId = ExStringUtils.trimToEmpty(request.getParameter("syllabusId"));
				map.put("currentIndex", "0");
				map.put("syllabusId", syllabusId);
				map.put("courseId", courseId);
				
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseLearningGuid: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除课程学习指南出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}	

}
