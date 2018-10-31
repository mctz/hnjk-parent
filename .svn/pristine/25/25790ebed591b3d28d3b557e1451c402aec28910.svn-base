package com.hnjk.edu.learning.controller;

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
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseExamRules;
import com.hnjk.edu.learning.model.CourseExamRulesDetails;
import com.hnjk.edu.learning.service.ICourseExamRulesDetailsService;
import com.hnjk.edu.learning.service.ICourseExamRulesService;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 试卷成卷规则管理.
 * <code>CourseExamRulesController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-26 下午04:45:56
 * @see 
 * @version 1.0
 */
@Controller
public class CourseExamRulesController extends BaseSupportController {

	private static final long serialVersionUID = 5012102847283775509L;

	@Autowired
	@Qualifier("courseExamRulesService")
	private ICourseExamRulesService courseExamRulesService;
	
	@Autowired
	@Qualifier("courseExamRulesDetailsService")
	private ICourseExamRulesDetailsService courseExamRulesDetailsService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	/**
	 * 成卷规则列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/courseexamrules/list.html")
	public String listCourseExamRules(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("courseName,course.resourceid,versionNum");
		objPage.setOrder(Page.ASC);//设置默认排序方式			

		String courseName = request.getParameter("courseName");
		String courseId = request.getParameter("courseId");
		String isEnrolExam = ExStringUtils.defaultIfEmpty(request.getParameter("isEnrolExam"), Constants.BOOLEAN_NO);
		String paperSourse = ExStringUtils.defaultIfEmpty(request.getParameter("paperSourse"), "final_exam");		
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();			
		if("entrance_exam".equals(paperSourse)){
			isEnrolExam = Constants.BOOLEAN_YES;
		} else {
			isEnrolExam = Constants.BOOLEAN_NO;
		}
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue())){//主讲老师
			condition.put("teacherId", user.getResourceid());
			isEnrolExam = Constants.BOOLEAN_NO;
			paperSourse = "final_exam";
		}		
		condition.put("paperSourse", paperSourse);
		condition.put("isEnrolExam", isEnrolExam);
		if("Y".equals(isEnrolExam)) {
			 if(ExStringUtils.isNotEmpty(courseName)){
				 condition.put("courseName", courseName);
			 }
		} else {
			if(ExStringUtils.isNotEmpty(courseId)){
				 condition.put("courseId", courseId);
			 }
		}		
		
		Page page = courseExamRulesService.findCourseExamRulesByCondition(condition, objPage);
		
		model.addAttribute("courseExamRulesPage", page);
		model.addAttribute("condition", condition);
		return "/edu3/learning/courseexamrules/courseexamrules-list";
	}
	/**
	 * 新增编辑成卷规则
	 * @param resourceid
	 * @param act
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/courseexamrules/input.html")
	public String editCourseExamRules(String resourceid,String act, ModelMap model) throws WebException{		
		CourseExamRules courseExamRules = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			courseExamRules = courseExamRulesService.get(resourceid);			
		}else{ //----------------------------------------新增
			courseExamRules = new CourseExamRules();
			if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher.duty").getParamValue())){//主讲老师
				courseExamRules.setIsEnrolExam(Constants.BOOLEAN_NO);	
				model.addAttribute("isTeacher", true);
			} else {
				courseExamRules.setIsEnrolExam(Constants.BOOLEAN_YES);	
			}		
			courseExamRules.setIsEnrolExam(Constants.BOOLEAN_NO);
		}	
		model.addAttribute("courseExamRules", courseExamRules);
		return "/edu3/learning/courseexamrules/courseexamrules-form";
	}	
	/**
	 * 保存成卷规则
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/courseexamrules/save.html")
	public void saveCourseExamRules(String resourceid,String paperSourse,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String courseName = request.getParameter("courseName");
			String courseId = request.getParameter("courseId");
			String isEnrolExam = request.getParameter("isEnrolExam");
			String examTimeLong = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("examTimeLong")), "0");
			CourseExamRules courseExamRules = new CourseExamRules();
			if(ExStringUtils.isNotBlank(resourceid)){//编辑
				courseExamRules = courseExamRulesService.get(resourceid);
			} else { //新增
				courseExamRules.setVersionNum(courseExamRulesService.getNextVersionNum(isEnrolExam, courseName, courseId));
			}			
			courseExamRules.setIsEnrolExam(isEnrolExam);
			courseExamRules.setExamTimeLong(Integer.parseInt(examTimeLong));
			if(Constants.BOOLEAN_YES.equals(isEnrolExam)){//入学考
				courseExamRules.setCourseName(courseName);
			} else {
				if(ExStringUtils.isNotBlank(courseId)){
					Course course = courseService.get(courseId);
					courseExamRules.setCourse(course);
				}
				courseExamRules.setPaperSourse(paperSourse);
			}
			relationCourseExamRulesDetails(request, courseExamRules);
			courseExamRulesService.saveOrUpdate(courseExamRules);
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_LEARNING_COURSEEXAMRULES_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/learning/courseexamrules/input.html?resourceid="+courseExamRules.getResourceid());
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", ExStringUtils.isNotBlank(resourceid)?UserOperationLogs.UPDATE:UserOperationLogs.INSERT, courseExamRules);
		}catch (Exception e) {
			logger.error("保存成卷规则出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	//关联成卷规则明细
	private void relationCourseExamRulesDetails(HttpServletRequest request, CourseExamRules courseExamRules) throws Exception {
		String[] detailsIds = request.getParameterValues("detailsId");
		String[] examNodeTypes = request.getParameterValues("examNodeType");
		String[] examTypes = request.getParameterValues("examType");
		String[] examNums = request.getParameterValues("examNum");
		String[] examValues = request.getParameterValues("examValue");
		String[] showOrders = request.getParameterValues("showOrder");
		if(null != detailsIds && detailsIds.length>0){
			for (int i = 0; i < detailsIds.length; i++) {						
				Integer examNum = Integer.parseInt(ExStringUtils.trimToEmpty(examNums[i]));
				Double examValue = Double.parseDouble(ExStringUtils.trimToEmpty(examValues[i]));
				if(examNum <= 0 || examValue<=0){
					throw new WebException(JstlCustomFunction.dictionaryCode2Value("CodeExamNodeType", examNodeTypes[i])+" - "+JstlCustomFunction.dictionaryCode2Value("CodeExamType", examTypes[i])+":填写题目数和分值必须大于0");
				}
				Long count = courseExamRulesService.getAvailableCourseExamsCount(courseExamRules, examNodeTypes[i], examTypes[i]);//可用题目数
				if(examNum > count.intValue()){
					throw new WebException(JstlCustomFunction.dictionaryCode2Value("CodeExamNodeType", examNodeTypes[i])+" - "+JstlCustomFunction.dictionaryCode2Value("CodeExamType", examTypes[i])+":填写题目数大于可用数"+count);
				}
				
				CourseExamRulesDetails courseExamRulesDetails = new CourseExamRulesDetails();
				if(ExStringUtils.isNotEmpty(detailsIds[i])){
					courseExamRulesDetails = courseExamRulesDetailsService.get(detailsIds[i]);
				} 
				courseExamRulesDetails.setCourseExamRules(courseExamRules);
				courseExamRulesDetails.setExamNodeType(examNodeTypes[i]);
				courseExamRulesDetails.setExamType(examTypes[i]);
				courseExamRulesDetails.setExamNum(examNum);
				courseExamRulesDetails.setExamValue(examValue);
				courseExamRulesDetails.setShowOrder(Integer.parseInt(showOrders[i]));
				
				courseExamRules.getCourseExamRulesDetails().add(courseExamRulesDetails);
			}
		}
	}
	/**
	 * 删除成卷规则
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/learning/courseexamrules/remove.html")
	public void removeCourseExamRules(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				courseExamRulesService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/learning/courseexamrules/list.html");
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseExamRules: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除成卷规则出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除规则明细
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/courseexamruledetails/remove.html")
	public void removeCourseExamRuleDetails(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				courseExamRulesDetailsService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);			
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "9", UserOperationLogs.DELETE, "CourseExamRulesDetails: "+resourceid);
			}
		} catch (Exception e) {
			logger.error("删除成卷规则明细出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
}
