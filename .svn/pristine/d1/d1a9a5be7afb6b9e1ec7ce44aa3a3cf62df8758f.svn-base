package com.hnjk.edu.teaching.controller;

import java.text.ParseException;
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
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.teaching.model.ExamSetting;
import com.hnjk.edu.teaching.model.ExamSettingDetails;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamSettingDetailsService;
import com.hnjk.edu.teaching.service.IExamSettingService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**
 * 考试计划设置表 <code>ExamSettingController</code>
 * <p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 下午03:58:46
 * @see
 * @version 1.0
 */
@Controller
public class ExamSettingController extends BaseSupportController {

	@Autowired
	@Qualifier("examSettingService")
	private IExamSettingService examSettingService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;

	@Autowired
	@Qualifier("examSettingDetailsService")
	private IExamSettingDetailsService examSettingDetailsService;
	
	private static final long serialVersionUID = 7080429774647158464L;
	/**
	 * 考试计划设置 - 列表
	 * @param req
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/list.html")
	public String exeList(HttpServletRequest req , Page objPage,ModelMap model) throws WebException {
		
		objPage.setOrderBy("setting.settingName");
		objPage.setOrder(Page.DESC);// 设置默认排序方式
		
		Map<String, Object> condition = new HashMap<String, Object>();// 查询条件
		String courseid 		      = ExStringUtils.trimToEmpty(req.getParameter("courseid"));
		String settingName 			  = ExStringUtils.trimToEmpty(req.getParameter("settingName"));
		String timeSegment        	  = ExStringUtils.trimToEmpty(req.getParameter("timeSegment"));
		String startTime              = ExStringUtils.trimToEmpty(req.getParameter("startTime"));
		String endTime                = ExStringUtils.trimToEmpty(req.getParameter("endTime"));
		User user 					  = SpringSecurityHelper.getCurrentUser();
		
		if (ExStringUtils.isNotEmpty(courseid)) {
			condition.put("courseid", courseid);
		}
		if (ExStringUtils.isNotEmpty(settingName)) {
			condition.put("settingName", settingName);
		}
		if (ExStringUtils.isNotEmpty(timeSegment)) {
			condition.put("timeSegment", timeSegment);
		}
		if (ExStringUtils.isNotEmpty(startTime)) {
			condition.put("startTime", startTime);
		}
		if (ExStringUtils.isNotEmpty(endTime)) {
			condition.put("endTime", endTime);
		}
		
		
		//如果是学习中心用户，只操作本学习中心的数据
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			condition.put("brshSchool",user.getOrgUnit().getResourceid());
		}
		
		 objPage 					  = examSettingService.findExamSettingJoinSubTable(condition, objPage);

		model.addAttribute("examSettingList", objPage);
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/examSetting/examSetting-list";
	}
	/**
	 * 查看单个考试计划的考试设置课程
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/view-course.html")
	public String viewExamSettingCourse(HttpServletRequest request ,HttpServletResponse response,ModelMap model)throws WebException{
		ExamSetting setting 	= null;
		String examSettingId 	= ExStringUtils.trimToEmpty(request.getParameter("examSettingId"));
		if (ExStringUtils.isNotEmpty(examSettingId)) {
			setting			    =  examSettingService.load(examSettingId);
			
		}
		model.put("examSetting", setting);
		return "/edu3/teaching/examSetting/examSetting-courses";
	}
	/**
	 * 查看所有已按排的课程
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/view-all-course.html")
	public String viewAllExamSettingCourse(HttpServletRequest request ,HttpServletResponse response,ModelMap model)throws WebException{
		List<ExamSettingDetails> list = examSettingDetailsService.findAllExamSettingDetails();
		model.put("list", list);
		return "/edu3/teaching/examSetting/view-all-examSetting-courses";
	}
	/**
	 * 新增编辑表单
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/input.html")
	public String exeEdit(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		
		if (ExStringUtils.isNotBlank(resourceid)) { // -----编辑
			ExamSetting examSetting 			   = examSettingService.get(resourceid);
			Map<String,ExamSettingDetails> details = new HashMap<String, ExamSettingDetails>();
			for (ExamSettingDetails detail:examSetting.getDetails()) {
				details.put(detail.getCourseId().getResourceid(),detail);
			}
			model.addAttribute("examSettingDetails", details);
			model.addAttribute("examSetting", examSetting);
		} else { // ----------------------------------------新增
			model.addAttribute("examSetting", new ExamSetting());
		}
		
		List<Course> courseList = courseService.findByHql(" from "+Course.class.getSimpleName()+"  course where course.status=1 and course.isDeleted =0 and course.isUniteExam='N' and (course.examType=0 or course.examType=1 or course.examType=2 or course.examType=3 or course.isPractice='Y' or course.examType is null ) order by course.courseCode ");
		model.addAttribute("courseList",courseList);
		
		return "/edu3/teaching/examSetting/examSetting-form";
	}

	/**
	 * 保存更新表单
	 * 
	 * @param grade
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/save.html")
	public void exeSave(ExamSetting examSetting, HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws WebException {
		
		Map<String, Object> map = new HashMap<String, Object>();
		String[] course_ids     = request.getParameterValues("selectCourse");
		User user               = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit            = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			unit                    = user.getOrgUnit();
		}
		
		try {
			if (ExStringUtils.isNotBlank(examSetting.getResourceid())) { // --------------------更新
				
				examSettingService.updateExamSetting(course_ids, examSetting);
				
			} else { // -------------------------------------------------------------------保存
				relationChildMethod(examSetting,request);
				if (unit!=null) {
					examSetting.setBrSchool(unit);
				}
				examSettingService.save(examSetting);
				
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_EXAM_PLANSETTING");
			map.put("reloadUrl", request.getContextPath()
					+ "/edu3/teaching/exam/plansetting/input.html?resourceid="
					+ examSetting.getResourceid());
		} catch (Exception e) {
			logger.error("保存考试批次出错：{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除对象
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/remove.html")
	public void exeClose(HttpServletRequest request,
			HttpServletResponse response, ModelMap model) throws WebException {
		String resourceid = request.getParameter("resourceid");
		String c_id = request.getParameter("c_id");
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (ExStringUtils.isNotBlank(resourceid)) {
				if (resourceid.split("\\,").length > 1) {// 批量删除
					examSettingService.batchDelete(resourceid.split("\\,"));
				} else {// 单个删除
					examSettingService.delete(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("forward", request.getContextPath()
						+ "/edu3/teaching/exam/plansetting/list.html");
			}
			if (ExStringUtils.isNotBlank(c_id)) {
				examSettingService.deleteExamCourse(c_id);
			}
		} catch (Exception e) {
			logger.error("删除考试批次出错:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	private void relationChildMethod(ExamSetting examSetting, HttpServletRequest request)
			throws ParseException {
		
		String[] course_ids = request.getParameterValues("courseSelectms2side__dx");
		//User curUser        = SpringSecurityHelper.getCurrentUser();
		
		if (course_ids!=null && course_ids.length>0) {
			Course course;
			ExamSettingDetails examCourse;
			for (int index = 0; index < course_ids.length; index++) {
				
				examCourse  = new ExamSettingDetails();
				course = courseService.get(course_ids[index]);
				
				examCourse.setCourseId(course);
				examCourse.setExamSetting(examSetting);
				examCourse.setCourseName(course.getCourseName());
				examCourse.setShowOrder(index);
				examSetting.getDetails().add(examCourse);
			}
		}
	}

	/**
	 * 通过AJAX获取考试计划设置列表-JSON形式返回
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/plansetting/get-examsetting-list.html")
	public void queryExamSetting(HttpServletRequest request,HttpServletResponse response){
	
		List<JsonModel> models  = new ArrayList<JsonModel>();
		String exceptId 	   = ExStringUtils.trimToEmpty(request.getParameter("exceptId"));
		String brschoolId      = ExStringUtils.trimToEmpty(request.getParameter("brschoolId"));
		StringBuffer hql       = new StringBuffer( " from "+ExamSetting.class.getSimpleName()+" setting where setting.isDeleted=0");
		if(ExStringUtils.isNotEmpty(exceptId)){
			String [] ids      = exceptId.split(",");
			for (int i = 0; i < ids.length; i++) {
				hql.append(" and setting.resourceid <> '"+ids[i]+"'");
			}
		}
		if (ExStringUtils.isNotEmpty(brschoolId)) {
			hql.append(" and setting.brSchool.resourceid ='"+brschoolId+"'");
		}
		List<ExamSetting> list = examSettingService.findByHql(hql.toString());
		for (ExamSetting setting : list) {
			JsonModel jsonModel= new JsonModel();
			jsonModel.setKey(setting.getResourceid());
			jsonModel.setValue(setting.getSettingName());
			
			models.add(jsonModel);
		}
		renderJson(response,JsonUtils.listToJson(models));
	}

}
