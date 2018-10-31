package com.hnjk.edu.teaching.controller;

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
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.model.ReOrderSetting;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IOrderCourseSettingService;
import com.hnjk.edu.teaching.service.IReOrderSettingService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 补预约管理controller.
 * <code>CourseBookController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-11-23 上午10:38:32
 * @see 
 * @version 1.0
 */
@Controller
public class ReOrderSettingController  extends BaseSupportController {
	
	@Autowired
	@Qualifier("reOrderSettingService")
	private IReOrderSettingService reOrderSettingService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("orderCourseSettingService")
	private IOrderCourseSettingService orderCourseSettingService;
	
	/**
	 * 补预约管理 - 列表
	 * @param request
	 * @param response
	 * @param model
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/recourseorder/list.html")
	public String reOrderSettingList(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page page){
		
		Map<String,Object> condition = new HashMap<String, Object>();

		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String reOrderType           = ExStringUtils.trimToEmpty(request.getParameter("reOrderType"));

		
		User user                    = SpringSecurityHelper.getCurrentUser();
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool             = user.getOrgUnit().getResourceid();
		}
		
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(reOrderType)) {
			condition.put("reOrderType", reOrderType);
		}
		
		if (!condition.isEmpty()) {
			page = reOrderSettingService.findReOrderSettingByCondition(condition,page);
		}
		model.put("condition", condition);
		model.put("page", page);
		
		return "/edu3/teaching/reCourseOrder/reCourseOrder-list";
	}
	/**
	 * 补预约管理-表单
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/recourseorder/edit.html")
	public String editReOrderSetting(String resourceid,HttpServletRequest request,ModelMap model){
		
		if (ExStringUtils.isNotEmpty(resourceid)) {
			ReOrderSetting setting = reOrderSettingService.get(resourceid);
			model.put("setting", setting);
		}
		User user 				     = SpringSecurityHelper.getCurrentUser();
		List<OrgUnit> brSchoolList   = orgUnitService.findOrgUnitListByType(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			brSchoolList.clear();
			OrgUnit u 			     = orgUnitService.get(user.getOrgUnit().getResourceid());
			brSchoolList.add(u);
			model.put("isBrschool", true);
		}	
		model.put("brSchoolList", brSchoolList);
		
		return "/edu3/teaching/reCourseOrder/reCourseOrder-form";
	}
	/**
	 * 补预约管理-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/recourseorder/save.html")
	public void saveReOrderSetting(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map	     	   = new HashMap<String, Object>();
		User user 				     	   = SpringSecurityHelper.getCurrentUser();
		String resourceid            	   = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String reOrderType           	   = ExStringUtils.trimToEmpty(request.getParameter("reOrderType"));
		String examSub               	   = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String orderCourseSetting    	   = ExStringUtils.trimToEmpty(request.getParameter("orderCourseSetting"));
		String [] branchSchool_ids   	   = request.getParameterValues("branchSchoolms2side__dx");   
		String startTime             	   = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime               	   = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		Date curDate                 	   = new Date();
		OrderCourseSetting os        	   = null;  
		ExamSub sub                  	   = null;
		try {
			
			if (null==branchSchool_ids||ExStringUtils.isEmpty(reOrderType)||
				ExStringUtils.isEmpty(startTime)||ExStringUtils.isEmpty(endTime)) {
				
					throw new WebException("保存失败,参数不完整！");
			}
			if ("1".equals(reOrderType)) {
				os 					 	   = orderCourseSettingService.load(orderCourseSetting);
			}else if ("2".equals(reOrderType)) {
				sub                  	   = examSubService.load(examSub);
			}
			if (ExStringUtils.isNotEmpty(resourceid)) {
				
				ReOrderSetting setting     = reOrderSettingService.get(resourceid);
				OrgUnit unit		   	   = orgUnitService.load(branchSchool_ids[0]);
				setting.setBrSchool(unit);
				setting.setEndTime(ExDateUtils.convertToDateTime(endTime));
				setting.setStartTime(ExDateUtils.convertToDateTime(startTime));
				setting.setReOrderType(reOrderType);
				setting.setFillinDate(curDate);
				setting.setFillinMan(user.getUsername());
				setting.setFillinManId(user.getResourceid());
				
				reOrderSettingService.update(setting);
			}else {				
				for (String id:branchSchool_ids) {
					
					OrgUnit unit		   = orgUnitService.load(id);
					ReOrderSetting setting = new ReOrderSetting();
					setting.setBrSchool(unit);
					setting.setEndTime(ExDateUtils.convertToDateTime(endTime));
					setting.setStartTime(ExDateUtils.convertToDateTime(startTime));
					setting.setReOrderType(reOrderType);
					setting.setFillinDate(curDate);
					setting.setFillinMan(user.getUsername());
					setting.setFillinManId(user.getResourceid());
					
					if (null!=os) {
						setting.setOrderCourseSetting(os);
					}
					if (null!=sub) {
						setting.setExamSub(sub);
					}
					reOrderSettingService.save(setting);
				}
			}
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("callbackType", "closeCurrent");

		} catch (Exception e) {
			logger.error("保存补预约管理出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:"+e.getMessage());
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 补预约管理-删除
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/recourseorder/del.html")
	public void delReOrderSetting(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map	       = new HashMap<String, Object>();
		String resourceid 		       = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		List<ReOrderSetting> delList   = new ArrayList<ReOrderSetting>();
		try {
			for (String id:resourceid.split(",")) {
				ReOrderSetting setting = reOrderSettingService.get(id);
				delList.add(setting);
			}
			reOrderSettingService.batchDelete(delList);
			map.put("statusCode", 200);
			map.put("message", "删除成功！");	
			map.put("forward", request.getContextPath()+ "/edu3/teaching/brschool/examinfo-list.html");
		}catch (Exception e) {
			logger.error("删除补预约权限设置出错:{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
