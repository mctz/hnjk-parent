package com.hnjk.edu.teaching.controller;

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
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.teaching.service.ICourseOrderStatService;
import com.hnjk.edu.teaching.service.ITeachTaskService;

/**
 * 学生预约情况统计表，主要用来生成教学任务书。
 * <code>CourseOrderStatController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-7-19 上午11:48:18
 * @see 
 * @version 1.0
 */
@Controller
public class CourseOrderStatController extends BaseSupportController{

	private static final long serialVersionUID = -8612569003218492007L;
	
	
	/**
	 * 列表
	 * @param planName
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/courseorderstat/list.html")
	public String exeList(String yearInfoid, String term, String courseName, String generatorFlag, Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("generatorFlag,yearInfo desc,term desc,course.courseName");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		if(ExStringUtils.isNotEmpty(yearInfoid)) {
			condition.put("yearInfoid", yearInfoid);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("term", term);
		}
		if(ExStringUtils.isNotEmpty(ExStringUtils.trimToEmpty(courseName))) {
			condition.put("courseName", ExStringUtils.trimToEmpty(courseName));
		}
		if(ExStringUtils.isNotEmpty(generatorFlag)) {
			condition.put("generatorFlag", generatorFlag);
		}
		Page page = courseOrderStatService.findCourseOrderStatByCondition(condition, objPage);
		
		model.addAttribute("cosList", page);
		model.addAttribute("condition", condition);
		return "/edu3/teaching/courseorderstat/courseorderstat-list";
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
	@RequestMapping("/edu3/teaching/courseorderstat/save.html")
	public void exeSave(String resourceid, HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				String[] resourceids = resourceid.split("\\,");
				courseOrderStatService.batchSaveOrUpdateCourseOrdersAndTeachTask(resourceids);
			} 
			map.put("statusCode", 200);
			map.put("message", "成功生成教学任务书！");
			map.put("forward", request.getContextPath()+"/edu3/teaching/courseorderstat/list.html");
		}catch (Exception e) {
			logger.error("生成教学任务书出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "生成教学任务书失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	@Autowired
	@Qualifier("courseorderstatservice")
	private ICourseOrderStatService courseOrderStatService;
	
}
