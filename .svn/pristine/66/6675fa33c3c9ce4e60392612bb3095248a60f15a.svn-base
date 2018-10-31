package com.hnjk.edu.textbook.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.TextBook;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.textbook.service.ITextBookService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;

@Controller
public class TextBookController extends BaseSupportController{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4352992484141572286L;
	
	@Autowired
	@Qualifier("iTextBookService")
	private ITextBookService iTextBookService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService iCourseService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService iTeachingPlanCourseService;
	
	/**
	 * 返回page
	 */
	@RequestMapping("/edu3/sysmanager/textbook/list.html")
	public String getTextBookPage(Page objPage,ModelMap model,HttpServletRequest request) throws Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		objPage.setOrder(Page.ASC);
		Page page = iTextBookService.getPageBycondition(condition, objPage);
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		return "/edu3/basedata/textbook/list";
	}
	/**
	 * 返回  修改/新增 的页面
	 */
	@RequestMapping("/edu3/sysmanager/textbook/edit.html")
	public String addTextBook(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		if(ExStringUtils.isNotBlank(resourceid)){
			TextBook tb = iTextBookService.load(resourceid);
			model.addAttribute("textBook", tb);
		}else{
			model.addAttribute("textBook", new TextBook());
		}
		return "/edu3/basedata/textbook/form";
	}
	/**
	 * 保存新增或修改的内容
	 */
	@RequestMapping("/edu3/sysmanager/textbook/save.html")
	public void saveTextBook(TextBook textbook,HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String courseid = request.getParameter("courseid");
		if(ExStringUtils.isNotBlank(courseid)){
			Course course = iCourseService.get(courseid);
			textbook.setCourse(course);
		}
		try {			
			boolean isSuccess=iTextBookService.saveTextBook(textbook);
			if(isSuccess){
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.UPDATE,"更新教材信息：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
//				map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/quality/evaluation/questionBank/edit.html?resourceid="+tmp.getResourceid());
			}else{
				map.put("statusCode", 300);
				map.put("message", "保存失败！请刷新网页后再试");
			}
		} catch (Exception e) {
			logger.error("保存教材信息出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 删除
	 */
	@RequestMapping("/edu3/sysmanager/textbook/delete.html")
	public void deleteTextBook(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		boolean isSuccess = iTextBookService.deleteTextBook(resourceids);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "删除成功！");	
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.DELETE,"删除教材信息：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}else{
			map.put("statusCode", 300);
			map.put("message", "删除失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 修改状态
	 */
	@RequestMapping("/edu3/sysmanager/textbook/setting.html")
	public void settingTextBook(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		String flag = request.getParameter("flag");
		boolean isSuccess = true;
		if(!flag.equalsIgnoreCase(Constants.BOOLEAN_YES) && !flag.equalsIgnoreCase(Constants.BOOLEAN_NO)){
			isSuccess = false;
		}else{
			isSuccess = iTextBookService.settingTextBook(resourceids, flag);
		}		
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "修改成功！");	
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "7", UserOperationLogs.UPDATE,"更新教材使用状态：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}else{
			map.put("statusCode", 300);
			map.put("message", "修改失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 查看列表
	 */
	@RequestMapping("/edu3/sysmanager/textbook/viewTextBook.html")
	public String viewTextBook(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		if(ExStringUtils.isNotBlank(resourceid)){
			TeachingPlanCourse tpc = iTeachingPlanCourseService.get(resourceid);			
			Course course = tpc.getCourse();
			model.addAttribute("textBooks", course.getTextBooks());
		}else{
			
		}
		return "/edu3/basedata/textbook/viewTextBook";
	}
}
