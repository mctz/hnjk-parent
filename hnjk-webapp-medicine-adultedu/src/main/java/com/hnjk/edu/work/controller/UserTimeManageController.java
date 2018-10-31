package com.hnjk.edu.work.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.edu.work.model.UserTimeManage;
import com.hnjk.edu.work.service.IUserTimeManageService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
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
import java.util.List;
import java.util.Map;

/**
 * Function : 学生工作时间管理 - 控制器
 * <p>Author : msl
 * <p>Date   : 2018-07-19
 * <p>Description : extends BaseSupportController
 */
@Controller
public class UserTimeManageController extends BaseSupportController {

	@Autowired
	@Qualifier("userTimeManageService")
	private IUserTimeManageService userTimeManageService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	/**
	 * 查询学生工作时间管理列表
	 * @param request
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/userTimeManage/list.html")
	public String findUserTimeManagePage(HttpServletRequest request, Page page, ModelMap model) {
		page.setOrderBy("yearInfo.yearName,term");
		page.setOrder(Page.DESC);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("unit.resourceid", user.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool", true);
		}else {
			model.addAttribute("isBrschool", false);
		}
		Page pageResult = userTimeManageService.findByCondition(page,condition);
		model.addAttribute("condition",condition);
		model.addAttribute("pageResult",pageResult);
		return "edu3/work/userTimeManage/timeManage_list";
	}


	/**
	 *  新增/编辑 工作时间管理
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/userTimeManage/input.html")
	public String editUserTimeManage(String resourceid,ModelMap model) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			model.addAttribute("brschool", true);
		}
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			UserTimeManage userTimeManage = userTimeManageService.get(resourceid);
			model.addAttribute("userTimeManage", userTimeManage);
		}else{ //----------------------------------------新增
			UserTimeManage userTimeManage = new UserTimeManage();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				userTimeManage.setUnit(user.getOrgUnit());
			}
			model.addAttribute("userTimeManage", userTimeManage);
		}
		return "edu3/work/userTimeManage/timeManage_form";
	}


	/**
	 * 保存 工作时间管理
	 * @param userTimeManage
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/userTimeManage/save.html")
	public void saveUserTimeManage(UserTimeManage userTimeManage, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			do{
				String yearid = ExStringUtils.trimToEmpty(request.getParameter("yearInfoId"));
				String branchSchool =  ExStringUtils.trimToEmpty(request.getParameter("branchSchoolId"));
				map.put("statusCode", 300);
				map.put("message", "开始时间要小于结束时间！");
				if(userTimeManage.getEndTime().before(userTimeManage.getStartTime())){
					continue;
				}


				if(ExStringUtils.isNotBlank(userTimeManage.getResourceid())){ //--------------------更新
					UserTimeManage persistuserTimeManage = userTimeManageService.get(userTimeManage.getResourceid());
					ExBeanUtils.copyProperties(persistuserTimeManage, userTimeManage);
					userTimeManage = persistuserTimeManage;
				}else{ //-------------------------------------------------------------------新增
					List<UserTimeManage> list = userTimeManageService.findByHql("from "+UserTimeManage.class.getSimpleName()+" c where c.isDeleted=0"
									+ " and c.unit.resourceid=? and c.yearInfo.resourceid=? and c.term=? and c.workType=?",
							branchSchool,yearid,userTimeManage.getTerm(),userTimeManage.getWorkType());
					if(list!=null && list.size()>0 && ExStringUtils.isBlank(userTimeManage.getResourceid())){
						//年度、学期和类型作为唯一
						map.put("statusCode", 300);
						map.put("message", "同一年度和学期只允许添加一条记录！");
						continue;
					}
				}
				if(ExStringUtils.isNotEmpty(yearid)){
					YearInfo yearInfo = yearInfoService.findUniqueByProperty("resourceid", yearid);
					userTimeManage.setYearInfo(yearInfo);
				}
				OrgUnit orgUnit = orgUnitService.findUniqueByProperty("resourceid", branchSchool);
				userTimeManage.setUnit(orgUnit);

				userTimeManageService.saveOrUpdate(userTimeManage);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_WORK_TIMEMANAGE_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/work/userTimeManage/input.html?resourceid="+userTimeManage.getResourceid());

			}while(false);
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除 工作时间管理
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/userTimeManage/remove.html")
	public void deleteUserTimeManage(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0) {
					userTimeManageService.batchDelete(resourceid.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("forward", request.getContextPath()+"/edu3/work/userTimeManage/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
