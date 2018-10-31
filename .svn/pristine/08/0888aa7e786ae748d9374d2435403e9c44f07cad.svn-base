package com.hnjk.edu.teaching.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimePeriodService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
/**
 * 时间段管理
 * <code>TeachingPlanCourseTimePeriodController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-13 下午04:40:38
 * @see 
 * @version 1.0
 */
@Controller
public class TeachingPlanCourseTimePeriodController extends BaseSupportController {
	private static final long serialVersionUID = 8428005343933899386L;

	@Autowired
	@Qualifier("teachingPlanCourseTimePeriodService")
	private ITeachingPlanCourseTimePeriodService teachingPlanCourseTimePeriodService;	
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@RequestMapping("/edu3/teaching/timeperiod/list.html")
	public String listTeachingPlanCourseTimePeriod(String brSchoolid,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("brSchool.unitCode,timePeriod,startTime,resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			brSchoolid = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}

		Page page = teachingPlanCourseTimePeriodService.findTeachingPlanCourseTimePeriodByCondition(condition, objPage);
		
		model.addAttribute("condition", condition);
		model.addAttribute("timePeriodPage", page);			
		return "/edu3/teaching/timeperiod/timeperiod-list";
	}

	@RequestMapping("/edu3/teaching/timeperiod/input.html")
	public String editTeachingPlanCourseTimePeriod(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		TeachingPlanCourseTimePeriod timePeriod = new TeachingPlanCourseTimePeriod();
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			timePeriod.setBrSchool(user.getOrgUnit());
			model.addAttribute("isBrschool", true);
		}		
		if(ExStringUtils.isNotBlank(resourceid)){
			timePeriod = teachingPlanCourseTimePeriodService.get(resourceid);			
		}
		model.addAttribute("timePeriod", timePeriod);
		return "/edu3/teaching/timeperiod/timeperiod-form";
	}
	
	@RequestMapping("/edu3/teaching/timeperiod/save.html")
	public void saveTeachingPlanCourseTimePeriod(String resourceid,String brSchoolid,String timePeriod,String stydyHour,String startTime,String endTime,String timeName,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			TeachingPlanCourseTimePeriod courseTimePeriod = new TeachingPlanCourseTimePeriod();
			if(ExStringUtils.isNotBlank(resourceid)){
				courseTimePeriod = teachingPlanCourseTimePeriodService.get(resourceid);
			}
			courseTimePeriod.setTimePeriod(timePeriod);
			courseTimePeriod.setTimeName(timeName);
			if(ExStringUtils.isNotBlank(brSchoolid)){
				courseTimePeriod.setBrSchool(orgUnitService.get(brSchoolid));
			}
			if(ExStringUtils.isNotBlank(startTime)){
				courseTimePeriod.setStartTime(ExDateUtils.parseDate(startTime, "HH:mm"));
			}
			if(ExStringUtils.isNotBlank(endTime)){
				courseTimePeriod.setEndTime(ExDateUtils.parseDate(endTime, "HH:mm"));
			}
			if(ExStringUtils.isNumeric(stydyHour, 1)){
				courseTimePeriod.setStydyHour(Integer.valueOf(stydyHour));
			}
			teachingPlanCourseTimePeriodService.saveOrUpdate(courseTimePeriod);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_COURSETIMEPERIOD_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/timeperiod/input.html?resourceid="+courseTimePeriod.getResourceid());				
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	

	@RequestMapping("/edu3/teaching/timeperiod/remove.html")
	public void removeTeachingPlanCourseTimePeriod(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				teachingPlanCourseTimePeriodService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	@RequestMapping("/edu3/teaching/timeperiod/distinct.html")
	public void distinctTeachingPlanCourseTimePeriod(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			teachingPlanCourseTimePeriodService.distinctTimePeriod();
			map.put("statusCode", 200);
			map.put("message", "删除成功！");
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
