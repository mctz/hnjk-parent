package com.hnjk.edu.evaluate.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.evaluate.service.IQuestionnaireService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Controller
public class QuestionnaireController extends BaseSupportController{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -955037913002963941L;
	@Autowired
	@Qualifier("iQuestionnaireService")
	private IQuestionnaireService iQuestionnaireService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService iYearInfoService;
	
	/**
	 * 问卷题目列表
	 * @param objPage
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaire/list.html")
	public String exeQuestionList(Page objPage,ModelMap model,HttpServletRequest request) throws Exception{
		objPage.setOrder(Page.ASC);
		objPage.setPageSize(100);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		if(condition.containsKey("pageNum")){
			condition.remove("pageNum");
		}
		if(condition.containsKey("pageSize")){
			condition.remove("pageSize");
		}
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			String brSchoolId = cureentUser.getOrgUnit().getResourceid();
			condition.put("brSchool", brSchoolId);//判断用户是否为教学点账号
			model.addAttribute("linkageQuerySchoolId", brSchoolId);
		}
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		if(SpringSecurityHelper.isUserInRole(roleCode)){//教师角色，只能查看自己
			condition.put("teacherid", cureentUser.getResourceid());
		}
		if(!condition.containsKey("yearId")&& !condition.containsKey("term")){
			
		}else{
			YearInfo yearInfo = iYearInfoService.get(condition.get("yearId").toString());
			String csTerm = yearInfo.getFirstYear()+"_0"+condition.get("term");
			condition.put("csTerm", csTerm);
			objPage = iQuestionnaireService.findQuestionnairePage(condition, objPage);
		}
		model.addAttribute("page", objPage);
		model.addAttribute("condition", condition);
		return "/edu3/evaluate/questionnaire/questionnaire-list";
	}
	/**
	 * 修改问卷有效时间
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaire/editTime.html")
	public void addQuestionnaire(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceids");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		boolean isSuccess = iQuestionnaireService.editQuestionnaireTime(resourceids, startDate, endDate);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "操作成功！");				
		}else{
			map.put("statusCode", 300);
			map.put("message", "操作失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 修改问卷状态 是否发布 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/questionnaire/isPublish.html")
	public void editQuestionnaireStatus(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String resourceids = request.getParameter("resourceid");
		String operate = request.getParameter("operate");
		boolean isSuccess = iQuestionnaireService.cancelQuestionnaire(resourceids,operate);
		if(isSuccess){
			map.put("statusCode", 200);
			map.put("message", "操作成功！");				
		}else{
			map.put("statusCode", 300);
			map.put("message", "操作失败！请刷新网页后再试");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
}
