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

import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.service.ITeachingActivityTimeSettingService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;

/**
 *教学活动环节中的时间设置Controller.
 * <code>ITeachingActivityTimeSettingService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2012-12-26 上午09:30:48
 * @see 
 * @version 1.0
 */
@Controller
public class TeachingActivityTimeSettingController extends BaseSupportController{

	private static final long serialVersionUID = 6344727971374946287L;
	
	
	@Autowired
	@Qualifier("teachingActivityTimeSettingService")
	private ITeachingActivityTimeSettingService teachingActivityTimeSettingService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 教学活动环节中的时间设置-列表 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/teachingactivitytimeSetting/list.html")
	public String teachingActivityTimeSettingList(TeachingActivityTimeSetting setting,HttpServletRequest request,ModelMap model,Page page){
		
		page.setOrderBy("setting.startTime");
		page.setOrder(Page.DESC);
		
		Map<String,Object> condition = new HashMap<String, Object>();
		if (ExStringUtils.isNotBlank(setting.getYearInfoId())) {
			condition.put("yearInfoId", setting.getYearInfoId());
		}
		if (ExStringUtils.isNotBlank(setting.getTerm())) {
			condition.put("term", setting.getTerm());
		}
		if (ExStringUtils.isNotBlank(setting.getMainProcessType())) {
			condition.put("mainProcessType", setting.getMainProcessType());
		}
		if (null!=setting.getStartTime()) {
			condition.put("startTime",setting.getStartTime());
		}
		if (null!=setting.getEndTime()) {
			condition.put("endTime",setting.getEndTime());
		}
		
		teachingActivityTimeSettingService.findTeachingActivityTimeSettingByCondition(condition, page);
		
		model.put("page", page);
		model.put("condition", condition);
		
		return "/edu3/teaching/teachingActivityTimeSetting/setting-list";
	}
	/**
	 * 教学活动环节中的时间设置-表单
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/teachingactivitytimeSetting/form.html")
	public String teachingActivityTimeSettingForm(String resourceid,HttpServletRequest request,ModelMap model){
		if (ExStringUtils.isNotBlank(resourceid)) {
			model.put("setting", teachingActivityTimeSettingService.get(resourceid));
		}
		return "/edu3/teaching/teachingActivityTimeSetting/setting-form";
	}
	/**
	 * 教学活动环节中的时间设置-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/teachingactivitytimeSetting/save.html")
	public void teachingActivityTimeSettingSave(TeachingActivityTimeSetting setting,HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map 						 = new HashMap<String, Object>();
		try {
			TeachingActivityTimeSetting save_setting = new TeachingActivityTimeSetting();
			String resourceid  						 = setting.getResourceid();
			String yearInfoId  						 = setting.getYearInfoId();
			
			if (ExStringUtils.isNotBlank(resourceid)) {
				save_setting                         = teachingActivityTimeSettingService.get(resourceid);
			}
			
			ExBeanUtils.copyBeans(setting,save_setting);
			save_setting.setYearInfo(yearInfoService.get(yearInfoId));
			
			teachingActivityTimeSettingService.saveOrUpdate(save_setting);
			
			map.put("statusCode", 200);
			map.put("message","保存成功！");
			map.put("navTabId", "RES_TEACHING_TEACHING_ACTIVITY_TIME_SETTING_FORM");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/teachingactivitytimeSetting/form.html?resourceid="+save_setting.getResourceid());
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "4",  UserOperationLogs.INSERT,(ExStringUtils.isNotBlank(resourceid)?"修改":"新增")+"教学活动时间:resourceid:"+resourceid);
		} catch (Exception e) {
			logger.error("教学活动环节中的时间设置-保存出错：{}"+e.fillInStackTrace());
			
			map.put("statusCode", 300);
			map.put("message","保存失败:"+e.getMessage());
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 教学活动环节中的时间设置-删除
	 * @param resourceid
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/teachingactivitytimeSetting/del.html")
	public void teachingActivityTimeSettingDel(String resourceid,HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map 						 = new HashMap<String, Object>();
		try {
			teachingActivityTimeSettingService.batchDelete(resourceid.split(","));
			map.put("statusCode", 200);
			map.put("message","删除成功！");
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "4",UserOperationLogs.DELETE, "删除教学活动时间:resourceid:"+resourceid);
		} catch (Exception e) {
			logger.error("教学活动环节中的时间设置-删除出错：{}"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message","删除失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
