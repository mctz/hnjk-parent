package com.hnjk.platform.system.controller;

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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IUserService;

/**
 * 系统全局参数配置.
 * <code>SysConfigurationController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-26 下午02:53:18
 * @see 
 * @version 1.0
 */
@Controller
public class SysConfigurationController extends BaseSupportController{

	private static final long serialVersionUID = -2001039944798744803L;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;
	

	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	
	@RequestMapping("/edu3/system/configuration/list.html")
	public String listConfiguration(String paramCode,String paramName,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("paramCode");
		objPage.setOrder("asc");
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(paramName)) {
			condition.put("paramName", paramName);
		}
		if(ExStringUtils.isNotEmpty(paramCode)) {
			condition.put("paramCode", paramCode);
		}
		
		Page page = sysConfigurationService.findSysConfigurationByCondition(condition, objPage);
		//String sql = "select * from HNJK_SYS_CONFIG WHERE ISDELETED = ? ";
		//Page page = baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql, new Object[]{new Integer(0)}, SysConfiguration.class);
		
		model.addAttribute("configList", page);
		model.addAttribute("condition", condition);
		return "/system/configuration/configuration-list";
	}
	/**
	 * 入学考试成绩录入用户设定,全局参数列表
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/configuration/entranceExamScoreInput-list.html")
	public String listConfigurationForEntranceExamScoreInput(Page objPage,ModelMap model)throws WebException{
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		condition.put("prefixOfParamName", "入学考试成绩录入人_");
		Page page = sysConfigurationService.findSysConfigurationByCondition(condition, objPage);

		model.addAttribute("configList", page);
		return "/system/configuration/entranceExamScoreInput-list";
	}
	/**
	 * 入学考试成绩录入用户设定,全局参数表单
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/configuration/entranceExamScoreInput-edit.html")
	public String editConfigurationForEntranceExamScoreInput(String resourceid,HttpServletRequest request,ModelMap model)throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			SysConfiguration sysConfiguration = sysConfigurationService.get(resourceid);		
			model.put("sysConfiguration", sysConfiguration);
		}
		return "/system/configuration/entranceExamScoreInput-form";
	}
	/**
	 * 入学考试成绩录入用户设定,选择用户
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/configuration/entranceExamScoreInput-select-user.html")
	public String configurationForEntranceExamScoreInputSelectUser(String cnName,HttpServletRequest request,ModelMap model,Page page)throws WebException{
		
		Map<String,Object> condition = new HashMap<String, Object>();
		SysConfiguration config      = sysConfigurationService.findUniqueByProperty("paramCode", "sysuser.usertype.edumanager");
		
		if (ExStringUtils.isNotEmpty(cnName)) {
			condition.put("cnName",cnName);
		}
											  condition.put("userType",config.getParamValue());	  
											  condition.put("isDeleted", 0);
											  condition.put("enabled",true);
		page 						 = userService.findUserByCondition(condition,page);
		
		model.put("page", page);
		model.put("condition", condition);
		if (condition.containsKey("enabled")) {
			condition.remove("enabled");
		}
		return "/system/configuration/entranceExamScoreInput-select-user";
	}
	/**
	 * 入学考试成绩录入用户设定,全局参数保存
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/configuration/entranceExamScoreInput-save.html")
	public void saveConfigurationForEntranceExamScoreInput(SysConfiguration sysConfiguration,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(sysConfiguration.getResourceid())){ //--------------------更新
				SysConfiguration persist = sysConfigurationService.get(sysConfiguration.getResourceid());
				ExBeanUtils.copyProperties(persist, sysConfiguration);
				sysConfigurationService.updateSysConfiguration(persist);
			}		
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SYS_CONFIGURATION");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/configuration/entranceExamScoreInput-edit.html?resourceid="+sysConfiguration.getResourceid());
		} catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/system/configuration/input.html")
	public String editConfiguration(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			SysConfiguration sysConfiguration = sysConfigurationService.get(resourceid);		
			model.addAttribute("sysConfiguration", sysConfiguration);
		}else{
			model.addAttribute("sysConfiguration", new SysConfiguration());			
		}
		return "/system/configuration/configuration-form";
	}
	
	@RequestMapping("/edu3/system/configuration/save.html")
	public void saveConfiguration(SysConfiguration sysConfiguration,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(sysConfiguration.getResourceid())){ //--------------------更新
				SysConfiguration persist = sysConfigurationService.get(sysConfiguration.getResourceid());
				ExBeanUtils.copyProperties(persist, sysConfiguration);
				persist.setParamValue(persist.getParamValue().trim());
				persist.setMemo(persist.getMemo().trim());
				sysConfigurationService.updateSysConfiguration(persist);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"修改全局参数：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}else{ //-------------------------------------------------------------------保存
				sysConfiguration.setParamValue(sysConfiguration.getParamValue().trim());
				sysConfiguration.setMemo(sysConfiguration.getMemo().trim());
				sysConfigurationService.saveSysConfiguration(sysConfiguration);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.INSERT,"新增全局参数：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SYS_CONFIGURATION");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/configuration/input.html?resourceid="+sysConfiguration.getResourceid());
		} catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/system/configuration/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					sysConfigurationService.batchDelete(resourceid.split("\\,"));
				}else{//单个删除
					sysConfigurationService.deleteSysConfiguration(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.DELETE,"删除全局参数：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/system/configuration/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/framework/system/sys/configuration/validateCode.html")
	public void validateCode(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String code = ExStringUtils.defaultIfEmpty(request.getParameter("paramCode"),"");		
		boolean flag = false;
		if(ExStringUtils.isNotBlank(code)){
			SysConfiguration sysConfiguration = sysConfigurationService.findUniqueByProperty("paramCode", code);
			if(null != sysConfiguration){
				flag = true;
			}		
		}	
		renderJson(response,  JsonUtils.booleanToJson(flag) );
	}
	/**
	 * 随堂练习截止时间
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/configuration/activecourseexam/deadline.html")
	public String editConfigurationActiveCourseExam(String resourceid,HttpServletRequest request,ModelMap model)throws WebException{
		SysConfiguration sysConfiguration = sysConfigurationService.findUniqueByProperty("paramCode", "teaching.activecourseexam.deadline");
		if(sysConfiguration==null){
			sysConfiguration = new SysConfiguration();
			sysConfiguration.setParamCode("teaching.activecourseexam.deadline");
			sysConfiguration.setParamName("当前学期随堂练习截止时间");
			sysConfiguration.setMemo("当前学期随堂提交练习截止时间");
		}
		model.addAttribute("sysConfiguration", sysConfiguration);
		return "/system/configuration/activecourseexamdeadline-form";
	}
	/**
	 * 保存随堂练习截止时间
	 * @param sysConfiguration
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/configuration/activecourseexam/deadline/save.html")
	public void saveConfigurationActiveCourseExam(SysConfiguration sysConfiguration,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			sysConfiguration.setParamCode("teaching.activecourseexam.deadline");
			if(ExStringUtils.isNotBlank(sysConfiguration.getResourceid())){ //--------------------更新
				SysConfiguration persist = sysConfigurationService.get(sysConfiguration.getResourceid());
				ExBeanUtils.copyProperties(persist, sysConfiguration);
				sysConfigurationService.updateSysConfiguration(persist);
			}else{ //-------------------------------------------------------------------保存
				sysConfigurationService.saveSysConfiguration(sysConfiguration);
			}	
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_ACTIVECOURSEEXAM_DEADLINE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/configuration/activecourseexam/deadline.html");
		} catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 提供机考客户端的参数配置.
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/examclient/getparam.html")
	public void getExamClientParam(HttpServletRequest request,HttpServletResponse response) throws WebException{
		//机考入口
		String key = ExStringUtils.trimToEmpty(request.getParameter("key"));		
		renderText(response, CacheAppManager.getSysConfigurationByCode(key).getParamValue());
	}
}
