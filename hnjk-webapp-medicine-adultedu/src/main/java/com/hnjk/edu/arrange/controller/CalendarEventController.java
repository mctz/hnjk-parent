
package com.hnjk.edu.arrange.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.CalendarEvent;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.service.ICalendarEventService;
import com.hnjk.edu.arrange.service.ISchoolCalendarService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

@Controller
public class CalendarEventController extends BaseSupportController{
	
	private static final long serialVersionUID = 1791167585417607033L;

	@Autowired
	@Qualifier("schoolCalendarService")
	private ISchoolCalendarService schoolCalendarService;
	
	@Autowired
	@Qualifier("calendarEventService")
	private ICalendarEventService calendarEventService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;	
	
	
	/**
	 * 查询院历事件列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/arrange/schoolCalendar/event.html")
	public String calendarEventQuery(String ids,HttpServletRequest request,Page objPage, ModelMap model){
		objPage.setOrderBy("name");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Page page = null;
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("brSchoolid", user.getOrgUnit().getResourceid());
			model.addAttribute("brSchoolNam", user.getOrgUnit().getUnitName());
			model.addAttribute("isBrschool", true);
		}	
		if(ExStringUtils.isNotBlank(ids)){
			ids = "'" + ids.replace(",", "','") + "'";
			condition.put("resourceids", ids);
		}
		page = calendarEventService.findSCalendarEventByHql(condition, objPage);
		model.addAttribute("sCalendarEventList", page);
		model.addAttribute("condition", condition);
		return "/edu3/arrange/calendarEvent/calendarEvent-list";
	}
	
	/**
	 * 新增/编辑院历事件
	 * @param resourceid
	 * @param buildingId
	 * @param model
	 * @return 
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/calendarEvent/input.html")
	public String exeEditCalendarEvent(String eventid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		CalendarEvent calendarEvent;
		String calendarid = "";
		if(ExStringUtils.isNotBlank(eventid)){ //-----编辑
			calendarEvent = calendarEventService.get(eventid);
			calendarid = calendarEvent.getSchoolCalendar().getResourceid();
		}else{ //-------------------------------------新增
			calendarEvent = new CalendarEvent();
			calendarEvent.setCreateDate(new Date());
		}
		calendarEvent.setUpdateDate(new Date());
		calendarEvent.setOperator(user);
		calendarEvent.setOperatorName(user.getCnName());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			map.put("brSchoolid", user.getOrgUnit().getResourceid());
		}
		map.put("!status", 1);
		String option = schoolCalendarService.constructOptions(map,calendarid);
		model.addAttribute("option", option);
		model.addAttribute("sCalendarEvent", calendarEvent);
		return "/edu3/arrange/calendarEvent/calendarEvent-form";
	}
	
	/**
	 * 保存院历事件
	 * @param sCalendarEvent
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/schoolCalendarEvent/save.html")
	public void exeSaveCalendarEvent(CalendarEvent sCalendarEvent,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			do{
				String sCalendarid = ExStringUtils.trimToEmpty(request.getParameter("schoolCalendarName"));
				String hasSCalendar = ExStringUtils.trimToEmpty(request.getParameter("hasSCalendar"));
				if (hasSCalendar.equals("N")) {
					map.put("statusCode", 300);
					map.put("message", "请先添加院历");
					continue;
				}
				
				SchoolCalendar schoolCalendar = schoolCalendarService.findUniqueByProperty("resourceid", sCalendarid);
				map.put("statusCode", 300);
				map.put("message", "开始时间要结束时间， 并且必须在院历的学期时间范围内！");
				if(sCalendarEvent.getEndDate()!=null && sCalendarEvent.getStartDate()!=null ){
					if(sCalendarEvent.getEndDate().before(sCalendarEvent.getStartDate())){
						continue;
					}
					if(schoolCalendar.getStartDate().after(sCalendarEvent.getStartDate()) || schoolCalendar.getEndDate().before(sCalendarEvent.getEndDate())){
						continue;
					}
				}
				if(sCalendarEvent.getEndDate2()!=null && sCalendarEvent.getStartDate2()!=null ){
					if(sCalendarEvent.getEndDate2().before(sCalendarEvent.getStartDate2())){
						continue;
					}
					if(schoolCalendar.getStartDate().after(sCalendarEvent.getStartDate2()) || schoolCalendar.getEndDate().before(sCalendarEvent.getEndDate2())){
						continue;
					}
				}
				if(sCalendarEvent.getEndDate3()!=null && sCalendarEvent.getStartDate3()!=null ){
					if(sCalendarEvent.getEndDate3().before(sCalendarEvent.getStartDate3())){
						continue;
					}
					if(schoolCalendar.getStartDate().after(sCalendarEvent.getStartDate3()) || schoolCalendar.getEndDate().before(sCalendarEvent.getEndDate3())){
						continue;
					}
				}
				
				if(ExStringUtils.isNotBlank(sCalendarEvent.getResourceid())){ //--------------------更新
					CalendarEvent persistSCalendarEvent = calendarEventService.get(sCalendarEvent.getResourceid());
					ExBeanUtils.copyProperties(persistSCalendarEvent, sCalendarEvent);
					sCalendarEvent = persistSCalendarEvent;
				}else{ //-------------------------------------------------------------------保存
					sCalendarEvent.setCreateDate(new Date());
				}
				sCalendarEvent.setSchoolCalendar(schoolCalendar);
				sCalendarEvent.setUpdateDate(new Date());
				sCalendarEvent.setOperator(SpringSecurityHelper.getCurrentUser());
				sCalendarEvent.setOperatorName(SpringSecurityHelper.getCurrentUserName());
				calendarEventService.saveOrUpdate(sCalendarEvent);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_ARRANGE_CALENDAREVENT_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/arrange/calendarEvent/input.html?eventid="+sCalendarEvent.getResourceid());
			
			}while(false);
		}catch (Exception e) {
			logger.error("保存院历事件出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除院历事件
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/calendarEvent/remove.html")
	public void exeDeleteCalendarEvent(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0)				
					calendarEventService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/arrange/calendarEvent/calendarEvent-list");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
