package com.hnjk.edu.arrange.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.arrange.model.ArrangeTemplate;
import com.hnjk.edu.arrange.model.ArrangeTemplateDetail;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.service.IArrangeTemplateService;
import com.hnjk.edu.arrange.service.ISchoolCalendarService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseTimePeriodService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

@Controller
public class ArrangeTemplateServiceController extends BaseSupportController{
	
	private static final long serialVersionUID = 1L;

	@Autowired
	@Qualifier("arrangeTemplateService")
	private IArrangeTemplateService arrangeTemplateService;
	
	@Autowired
	@Qualifier("teachingPlanCourseTimePeriodService")
	private ITeachingPlanCourseTimePeriodService teachingPlanCourseTimePeriodService;
	
	@Autowired
	@Qualifier("schoolCalendarService")
	private ISchoolCalendarService schoolCalendarService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	/**
	 * @param request
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/arrange/template/list.html")
	public String queryArrangeTemplateList(HttpServletRequest request,Page objPage,ModelMap model) {
		objPage.setOrderBy("schoolCalendar.name,templateName");
		objPage.setOrder(Page.DESC);//设置默认排序方式	
		
		Map<String,Object> condition  = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("branchSchool", user.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool", true);
		}	
		Page page = arrangeTemplateService.findArrangeTemplateByHql(condition, objPage);
		model.addAttribute("templateList", page);
		model.addAttribute("condition", condition);
		return "/edu3/arrange/template/template-list";
	}
	
	/**
	 *  新增/编辑 排课模版
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/template/input.html")
	public String exeEditArrangeTemplate(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String templateid		=	request.getParameter("templateid");
		String brSchoolid		=	request.getParameter("brSchoolid");
		String schoolCalendarid = 	"";
		ArrangeTemplate template = null;
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		//教学点选项
		int unique = 1;
		if(ExStringUtils.isBlank(brSchoolid)){
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				brSchoolid = user.getOrgUnit().getResourceid();
			}else {
				unique = 0;
			}
		}
		//教学点选项
		String unitOption = orgUnitService.constructOptions(condition, brSchoolid, unique);
		String[] setting = null;
		if(ExStringUtils.isNotBlank(templateid)){ //-----编辑
			template = arrangeTemplateService.get(templateid);
			schoolCalendarid = template.getSchoolCalendar().getResourceid();
			if(ExStringUtils.isNotBlank(template.getTimePeriodids())){
				setting = template.getTimePeriodids().split(",");
			}
		}else{ //----------------------------------------新增
			template = new ArrangeTemplate();
		}
		if(ExStringUtils.isNotEmpty(brSchoolid)) condition.put("brSchoolid", brSchoolid);
		//上课时间选项
		String timePeriodOption = teachingPlanCourseTimePeriodService.constructOptions(condition, setting);
		//院历选项
		condition.put("status", 1);
		String sCalendarOption = schoolCalendarService.constructOptions(condition, schoolCalendarid);
		model.addAttribute("unitOption",unitOption);
		model.addAttribute("template",template);
		model.addAttribute("timePeriodOption", timePeriodOption);	
		model.addAttribute("templateid",templateid);
		model.addAttribute("sCalendarOption", sCalendarOption);
		return "/edu3/arrange/template/template-form";
	}
	
	/**
	 * ajax刷新表单数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/arrange/template/updateForm.html")
	public void updateForm(HttpServletRequest request,HttpServletResponse response){
		String brSchoolid	=	request.getParameter("brSchoolid");
		String templateid	=	request.getParameter("templateid");
		String schoolCalendarid	=	request.getParameter("schoolCalendarid");
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("status", 1);
			if(ExStringUtils.isNotBlank(brSchoolid) && !"undefined".equals(brSchoolid)){
				condition.put("brSchoolid", brSchoolid);
			}
			String schoolCalendarOption = schoolCalendarService.constructOptions(condition, schoolCalendarid);
			if(ExStringUtils.isNotBlank(schoolCalendarid)){
				SchoolCalendar schoolCalendar = schoolCalendarService.get(schoolCalendarid);
				map.put("maxWeek", schoolCalendar.getWeeks());
			}
			String[] setting = null;
			if(ExStringUtils.isNotBlank(templateid)){
				ArrangeTemplate template = arrangeTemplateService.get(templateid);
				if(template!=null && template.getTimePeriodids()!=null)
				setting = template.getTimePeriodids().split(",");
			}
			String timePeriodOption = teachingPlanCourseTimePeriodService.constructOptions(condition, setting);
			map.put("schoolCalendarOption", schoolCalendarOption);
			map.put("timePeriodOption", timePeriodOption);
		} catch (Exception e) {
			logger.error("操作出错：{}",e.fillInStackTrace());
			statusCode = 300;
			map.put("message", "刷新失败！");
		}
		map.put("statusCode", statusCode);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 保存排课模版
	 * @param schoolCalendar
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/template/save.html")
	public void exeSaveArrangeTemplate(ArrangeTemplate template,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
 		String sCalendarid		=		ExStringUtils.trimToEmpty(request.getParameter("schoolCalendarid"));
		String hasSCalendar 	=		ExStringUtils.trimToEmpty(request.getParameter("hasSCalendar"));
		ExStringUtils.trimToEmpty(request.getParameter("isAdd"));
		String[] timePeriod     =		request.getParameterValues("timePeriod");
		String weekOrDate 		= 		ExStringUtils.trimToEmpty(request.getParameter("weekOrDate"));//单选框的值：0-周/1-日期
		String[] days     		=		request.getParameterValues("day");
		String[] weeks 		    =		request.getParameterValues("week");
		String templateDetail_startDate = request.getParameter("templateDetail_startDate");
		String templateDetail_endDate = request.getParameter("templateDetail_endDate");
		Date dStartDate = null;
		Date dEndDate = null;
		try {
			do{
				if (hasSCalendar.equals("N")) {
					map.put("statusCode", 300);
					map.put("message", "请先添加院历");
					continue;
				}
				if(ExBeanUtils.xorForNull(template.getEndDate(), template.getStartDate()) && template.getEndDate().before(template.getStartDate())){
					map.put("statusCode", 300);
					map.put("message", "保存失败！开始时间要小于结束时间！");
					continue;
				}
				if(ExStringUtils.isNotBlank(templateDetail_startDate)){
					dStartDate = ExDateUtils.parseDate(templateDetail_startDate, ExDateUtils.PATTREN_DATE_TIME);
				}
				if(ExStringUtils.isNotBlank(templateDetail_endDate)){
					dEndDate = ExDateUtils.parseDate(templateDetail_endDate, ExDateUtils.PATTREN_DATE_TIME);
				}
				if(ExBeanUtils.xorForNull(dEndDate, dStartDate) && dEndDate.before(dStartDate)){
					map.put("statusCode", 300);
					map.put("message",  "保存失败！具体开始时间要小于具体结束时间！");
					continue;
				}
				if(!(timePeriod!=null && ExStringUtils.isNotEmpty(timePeriod.toString()))){
					map.put("statusCode", 300);
					map.put("message", "请添加上课时间段！");
					continue;
				}
				/*
				List<ArrangeTemplate> list = arrangeTemplateService.findByHql("from "+ArrangeTemplate.class.getSimpleName()+" c where c.isDeleted=0"
						+ " and c.templateName=?"
						+ " and c.schoolCalendar.resourceid=?",
						template.getTemplateName(),sCalendarid);
				if(list!=null && list.size()>0 && !sCalendarid.equals(template.getSchoolCalendar().getResourceid())){
					map.put("statusCode", 300);
					map.put("message", "该排课模版名称已使用，请使用其它模版名称！");
					continue;
				}*/
				
				User user = SpringSecurityHelper.getCurrentUser();//当前用户
				SchoolCalendar schoolCalendar = schoolCalendarService.findUniqueByProperty("resourceid", sCalendarid);
				if(ExStringUtils.isNotBlank(template.getResourceid())){ //--------------------编辑
					template = arrangeTemplateService.get(template.getResourceid());
					template.setUpdateDate(new Date());
				}else{ //-------------------------------------------------------------------添加
					template.setCreateDate(new Date());
				}
				template.setSchoolCalendar(schoolCalendar);
				template.setOperator(user);
				template.setOperatorName(user.getCnName());
				template.setTimePeriodids(ExStringUtils.toString(timePeriod));
				
				if(days!=null)
					Arrays.asList(days);
				if(weeks!=null)
					Arrays.asList(weeks);
				String timePeriodNames = "";
				for (int index = 0; index < timePeriod.length; index++) {//更新模版详情的时间段
					TeachingPlanCourseTimePeriod  tPeriod= teachingPlanCourseTimePeriodService.get(timePeriod[index]);
					timePeriodNames += tPeriod.getCourseTimeName();
				}
				template.setTimePeriodNames(timePeriodNames.substring(0, timePeriodNames.length()-1));
				
				if("1".equals(weekOrDate)){//日期
					template.setDateType(1);
					int week1 = Integer.parseInt(ExDateUtils.convertDay2WeekNum(dStartDate));//dStartDate的星期
					long diffDays = ExDateUtils.getDateDiffNum(templateDetail_startDate, templateDetail_endDate);//相差天数
					String days2 = "";
					if(diffDays>=7){
						template.setDays("1,2,3,4,5,6,7");
					}else {
						for (int i =0; i <= diffDays; i++) {
							days2 += ((week1+i)%7==0?7:(week1+i)%7)+",";
						}
						template.setDays(days2.substring(0, days2.length()-1));
					}
					template.setWeeks("");
					/*for (int j = 0; j < daylList.size(); j++) {//星期
						ArrangeTemplateDetail tDetail = new ArrangeTemplateDetail();
						tDetail.setDay(daylList.indexOf(j));
						tDetail.setArrangeTemplate(template);
						tDetail.setStartDate(dStartDate);
						tDetail.setEndDate(dEndDate);
						tDetail.setTimePeriods(timePeriodSet);
						templateDetails.add(tDetail);
					}*/
					template.setStartDate(dStartDate);
					template.setEndDate(dEndDate);
				}else {//周期
					template.setDateType(0);
					if (ExStringUtils.isBlank(ExStringUtils.toString(days))) {
						map.put("statusCode", 300);
						map.put("message", "请选择星期");
						continue;
					}
					template.setDays(ExStringUtils.toString(days));
					if(weeks!=null){
						template.setWeeks(ExStringUtils.toString(weeks));
					}
					/*for (int i = 0; i < weeklList.size(); i++) {//周
						for (int j = 0; j < daylList.size(); j++) {//星期
							ArrangeTemplateDetail tDetail = new ArrangeTemplateDetail();
							tDetail.setDay(daylList.indexOf(j));
							tDetail.setWeek(weeklList.indexOf(i));
							tDetail.setArrangeTemplate(template);
							tDetail.setTimePeriods(timePeriodSet);
							templateDetails.add(tDetail);
						}
					}*/
				}
				arrangeTemplateService.saveOrUpdate(template);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_ARRANGE_TEMPLATE_INPUT");
				//map.put("reloadUrl", request.getContextPath() +"/edu3/arrange/template/input.html?templateid="+template.getResourceid());
			}while(false);
		}catch (Exception e) {
			logger.error("保存排课模版出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除排课模版
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/template/remove.html")
	public void exeDeleteArrangeTemplate(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				ArrangeTemplate template = arrangeTemplateService.get(resourceid);
				template.setIsDeleted(1);
				Set<ArrangeTemplateDetail> templateDetails = template.getTemplateDetais();
				String[] resids = new String[200];
				int i = 0;
				for (ArrangeTemplateDetail templateDetail : templateDetails) {
					resids[i++] = templateDetail.getResourceid();
				}
				//arrangeTemplateDetailService.batchTruncate(ArrangeTemplateDetail.class, resids);
				arrangeTemplateService.update(template);
				//arrangeTemplateService.batchDelete(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/arrange/template/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
