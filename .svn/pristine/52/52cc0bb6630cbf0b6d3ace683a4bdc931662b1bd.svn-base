
package com.hnjk.edu.arrange.controller;

import java.util.Arrays;
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
import com.hnjk.edu.arrange.model.ArrangeTemplate;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.service.IArrangeTemplateService;
import com.hnjk.edu.arrange.service.ICalendarEventService;
import com.hnjk.edu.arrange.service.ISchoolCalendarService;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

@Controller
public class SchoolCalendarController extends BaseSupportController{
	
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
	
	@Autowired
	@Qualifier("arrangeTemplateService")
	private IArrangeTemplateService arrangeTemplateService;
	
	/**
	 * 查询院历列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/arrange/schoolCalendar/list.html")
	public String schoolCalendarQuery(HttpServletRequest request,Page objPage, ModelMap model) {
		objPage.setOrderBy("yearInfo.yearName,term");
		objPage.setOrder(Page.DESC);//设置默认排序方式	
		Map<String,Object> condition  = new HashMap<String, Object>();
		condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("branchSchool", user.getOrgUnit().getResourceid());
			model.addAttribute("brschoolName", user.getOrgUnit().getUnitName());
			model.addAttribute("isBrschool", true);
		}	
		Page page = schoolCalendarService.findSchoolCalendarByHql(condition, objPage);
		model.addAttribute("schoolCalendarList", page);
		model.addAttribute("condition", condition);
		return "/edu3/arrange/schoolCalendar/schoolCalendar-list";
	}
	
	/**
	 *  新增/编辑 院历
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/schoolCalendar/input.html")
	public String exeEditSchoolCalendar(String resourceid,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			SchoolCalendar schoolCalendar = schoolCalendarService.get(resourceid);
			model.addAttribute("schoolCalendar", schoolCalendar);
		}else{ //----------------------------------------新增
			SchoolCalendar schoolCalendar = new SchoolCalendar();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				schoolCalendar.setUnit(user.getOrgUnit());
			}
			model.addAttribute("schoolCalendar", schoolCalendar);
		}
		return "/edu3/arrange/schoolCalendar/schoolCalendar-form";
	}
	
	/**
	 * 保存院历
	 * @param schoolCalendar
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/schoolCalendar/save.html")
	public void exeSaveSchoolCalendar(SchoolCalendar schoolCalendar,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			do{
				String yearid = ExStringUtils.trimToEmpty(request.getParameter("yearInfoId"));
				String branchSchool =  ExStringUtils.trimToEmpty(request.getParameter("branchSchoolId"));
				map.put("statusCode", 300);
				map.put("message", "开始时间要小于结束时间！");
				if(schoolCalendar.getTermEndDate().before(schoolCalendar.getTermStartDate())){
					continue;
				}
				if(schoolCalendar.getEndDate().before(schoolCalendar.getStartDate())){
					continue;
				}
				if(schoolCalendar.getGraduateEndDate()!=null && schoolCalendar.getGraduateStartDate()!=null){
					if(schoolCalendar.getGraduateEndDate().before(schoolCalendar.getGraduateStartDate())){
						continue;
					}
					if(schoolCalendar.getGraduateStartDate().before(schoolCalendar.getTermStartDate()) || schoolCalendar.getGraduateEndDate().after(schoolCalendar.getTermEndDate())){
						map.put("message", "教学周必须在学期日期范围内！");
						continue;
					}
				}
				if(schoolCalendar.getStartDate().before(schoolCalendar.getTermStartDate()) || schoolCalendar.getEndDate().after(schoolCalendar.getTermEndDate())){
					map.put("message", "教学周必须在学期日期范围内！");
					continue;
				}
				List<SchoolCalendar> list = schoolCalendarService.findByHql("from "+SchoolCalendar.class.getSimpleName()+" c where c.isDeleted=0"
						+ " and c.unit.resourceid=?"
						+ " and c.yearInfo.resourceid=?"
						+ " and c.term=?",
						branchSchool,yearid,schoolCalendar.getTerm());
				if(list!=null && list.size()>0 && !list.get(0).getResourceid().equals(schoolCalendar.getResourceid())){
					map.put("statusCode", 300);
					map.put("message", "同一年度和学期只能有一个院历！");
					continue;
				}
				if(ExStringUtils.isNotBlank(schoolCalendar.getResourceid())){ //--------------------更新
					SchoolCalendar persistSchoolCalendar = schoolCalendarService.get(schoolCalendar.getResourceid());
					ExBeanUtils.copyProperties(persistSchoolCalendar, schoolCalendar);
					schoolCalendar = persistSchoolCalendar;
				}else{ //-------------------------------------------------------------------保存
					schoolCalendar.setCreateDate(new Date());
					schoolCalendar.setStatus(0);//默认发布状态为未发布
				}
				if(ExStringUtils.isNotEmpty(yearid)){
					YearInfo yearInfo = yearInfoService.findUniqueByProperty("resourceid", yearid);
					schoolCalendar.setYearInfo(yearInfo);
				}
				if(schoolCalendar.getUnit() != null){
					branchSchool = schoolCalendar.getUnit().getResourceid();
				}
				OrgUnit orgUnit = orgUnitService.findUniqueByProperty("resourceid", branchSchool);
				schoolCalendar.setUnit(orgUnit);
				schoolCalendar.setUpdateDate(new Date());
				schoolCalendar.setOperator(SpringSecurityHelper.getCurrentUser());
				schoolCalendar.setOperatorName(SpringSecurityHelper.getCurrentUserName());
				schoolCalendarService.saveOrUpdate(schoolCalendar);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_ARRANGE_SCHOOLCALENDAR_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/arrange/schoolCalendar/input.html?resourceid="+schoolCalendar.getResourceid());
			
			}while(false);
		}catch (Exception e) {
			logger.error("保存院历出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除院历
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/schoolCalendar/remove.html")
	public void exeDeleteSchoolCalendar(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				boolean isDelete = true;
				List<String> idsList = Arrays.asList(resourceid.split("\\,"));
				for (String id : idsList) {
					SchoolCalendar sCalendar = schoolCalendarService.findUniqueByProperty("resourceid", id);
					String hql = " from "+ArrangeTemplate.class.getSimpleName()+" where isDeleted=0 and schoolCalendar.resourceid=?";
					List<ArrangeTemplate> templates = arrangeTemplateService.findByHql(hql, id);
					if(sCalendar.getStatus()==1 && templates.size()>0){//如果已发布
						map.put("statusCode", 300);
						map.put("message", "不能删除已发布并且已使用的院历！");
						isDelete = false;
					}
				}		
				if(isDelete){//是否可以进行删除操作
					schoolCalendarService.batchDelete(resourceid.split("\\,"));
					map.put("statusCode", 200);
					map.put("message", "删除成功！");				
					map.put("forward", request.getContextPath()+"/edu3/arrange/schoolCalendar/schoolCalendar-list");
				}
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 发布院历
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/arrange/schoolCalendar/publish.html")
	public void exePublishSchoolCalendar(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				List<String> idsList = Arrays.asList(resourceid.split("\\,"));
				for (String id : idsList) {
					SchoolCalendar sCalendar = schoolCalendarService.findUniqueByProperty("resourceid", id);
					sCalendar.setStatus(1);
					schoolCalendarService.update(sCalendar);
				}		
			map.put("statusCode", 200);
			map.put("message", "发布成功！");				
			map.put("forward", request.getContextPath()+"/edu3/arrange/schoolCalendar/schoolCalendar-list");
			}
		} catch (Exception e) {
			logger.error("发布出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "发布出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
