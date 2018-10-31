package com.hnjk.platform.system.controller;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.OrgUnitExtends;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 组织单位Controller类 <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-4下午12:17:06
 * @see 
 * @version 1.0
 */
@Controller
public class OrgUnitController extends BaseSupportController{

	private static final long serialVersionUID = 6145709423126552733L;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;	
	
	/**
	 * 树与列表
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/unit/list.html")
	public String listOrgUnit(Page objPage,HttpServletRequest request, ModelMap model) throws WebException{
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));//默认组织单位		
		String unitName = ExStringUtils.trimToEmpty(request.getParameter("unitName"));//组织名称
		
		if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
			
			objPage.setOrderBy("unitCode");
			objPage.setOrder(Page.ASC);
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			if(ExStringUtils.isNotEmpty(unitName)) {
				condition.put("unitName", unitName);
			}
			if(ExStringUtils.isNotEmpty(unitId)) {
				condition.put("unitId", unitId);
			}
			
			Page orgUnitList = orgUnitService.findOrgByConditionByHql(condition, objPage);
			model.addAttribute("condition", condition);
			model.addAttribute("orgUnitList", orgUnitList);	
			
			return "/system/org/unit-list-sub";
		}else{
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));			
			List<OrgUnit> orgList  = new ArrayList<OrgUnit>();
			try {
				orgList = orgUnitService.findOrgTree(null);
			} catch (Exception e) {					
				logger.error("输出组织列表-组织树出错："+e.fillInStackTrace());
			}				
			String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(orgList,"UNIT_ROOT" ,defaultCheckedValue));
			model.addAttribute("unitTree", jsonString);
			return "/system/org/unit-list";
		}
		
	}
	
	/**
	 * 新增组织
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/orgunit/input.html")
	public String addOrgUint(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String unitId = request.getParameter("unitId");
		if(ExStringUtils.isNotBlank(resourceid)){
			OrgUnit orgunit = orgUnitService.load(resourceid);	
			model.addAttribute("orgunit", orgunit);
			
			if(ExStringUtils.isEmpty(unitId) && null!=orgunit.getParent()){
				unitId = orgunit.getParent().getResourceid();
			}			
		}else{
			model.addAttribute("orgunit", new OrgUnit());			
		}
		OrgUnit parentOrg = orgUnitService.load(unitId);	

		model.addAttribute("parentOrg", parentOrg);
		return "/system/org/unit-form";
	}
	
	/**
	 * 保存
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/orgunit/save.html")
	public void saveOrg(OrgUnit orgUnit,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String teachingMan	= ExStringUtils.trimToEmpty(request.getParameter("teachingMan"));//教务员姓名
			String teachingTel	= ExStringUtils.trimToEmpty(request.getParameter("teachingTel"));//教务员电话
			String teachingEmail= ExStringUtils.trimToEmpty(request.getParameter("teachingEmail"));//教务员e-mail
			String teachingQQ	= ExStringUtils.trimToEmpty(request.getParameter("teachingQQ"));//教务员QQ
			String majorDirector= ExStringUtils.trimToEmpty(request.getParameter("majorDirector"));//专业主任信息
			String[] exCodes = {OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGMAN,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGTEL,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGEMAIL,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGQQ,OrgUnitExtends.UNIT_EXTENDCODE_MAJORDIRECTOR};
			String[] exValues = {teachingMan,teachingTel,teachingEmail,teachingQQ,majorDirector};
			if(ExStringUtils.isNotEmpty(orgUnit.getResourceid())){ //------------编辑
				OrgUnit persistOrgUnit = orgUnitService.load(orgUnit.getResourceid());
				
				if(ExStringUtils.isNotEmpty(orgUnit.getParentId())){
					OrgUnit parentOrg = orgUnitService.load(orgUnit.getParentId());
					parentOrg.setIsChild(Constants.BOOLEAN_YES);
					orgUnit.setParent(parentOrg);
					orgUnit.setUnitLevel(parentOrg.getUnitLevel()+1);
				}
				orgUnit.setOrgUnitExtends(persistOrgUnit.getOrgUnitExtends());
				ExBeanUtils.copyProperties(persistOrgUnit, orgUnit);				
				
				OrgUnitExtends unitExtends = null;	
				for (int i = 0; i < exCodes.length; i++) {
					if(null != persistOrgUnit.getOrgUnitExtends().get(exCodes[i])){
						unitExtends  = persistOrgUnit.getOrgUnitExtends().get(exCodes[i]);
						unitExtends.setExValue(exValues[i]);
					}else{
						unitExtends  = new OrgUnitExtends(exCodes[i], exValues[i], persistOrgUnit);
					}				
					persistOrgUnit.getOrgUnitExtends().put(exCodes[i], unitExtends);
				}
				orgUnitService.updateOrgUnit(persistOrgUnit);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "1", UserOperationLogs.UPDATE, persistOrgUnit);
			}else{ //----------------------------------------------------------新增
				//OrgUnit org = orgUnitService.findByHql("from OrgUnit where isDeleted=0 and unitCode=?", orgUnit.getUnitCode());
				List<OrgUnit> orgList = orgUnitService.findByHql("from OrgUnit where isDeleted=0 and unitCode=?", orgUnit.getUnitCode());
				if(null != orgList && orgList.size()>0){
					throw new WebException("存在相同编码的组织单位，请重新输入！");
				}
				if(ExStringUtils.isNotEmpty(orgUnit.getParentId())){
					OrgUnit parentOrg = orgUnitService.load(orgUnit.getParentId());
					parentOrg.setIsChild(Constants.BOOLEAN_YES);
					orgUnit.setParent(parentOrg);
					orgUnit.setUnitLevel(parentOrg.getUnitLevel()+1);
				}
				orgUnit.setIsChild(Constants.BOOLEAN_NO);
				orgUnit.setCreateTime(new Date());
				
				OrgUnitExtends unitExtends = null;	
				for (int i = 0; i < exCodes.length; i++) {
					if(null != orgUnit.getOrgUnitExtends().get(exCodes[i])){
						unitExtends  = orgUnit.getOrgUnitExtends().get(exCodes[i]);
						unitExtends.setExValue(exValues[i]);
					}else{
						unitExtends  = new OrgUnitExtends(exCodes[i], exValues[i], orgUnit);
					}				
					orgUnit.getOrgUnitExtends().put(exCodes[i], unitExtends);
				}
				
				orgUnitService.addOrgUnit(orgUnit);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "1", UserOperationLogs.INSERT, orgUnit);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_ORG_USER");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/orgunit/input.html?resourceid="+orgUnit.getResourceid());
		} catch (Exception e) {				
			logger.error("保存用户错误:"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 删除
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/system/orgunit/delete.html")
	public void deleteOrgUnit(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					orgUnitService.batchCascadeDelete(resourceid.split("\\,"));
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "1", UserOperationLogs.DELETE, "unitids:"+resourceid);
				}else{//单个删除
					orgUnitService.delete(resourceid);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "1", UserOperationLogs.DELETE, "unitid:"+resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");	
				map.put("localArea", "_orgUnitListContent");
				//map.put("forward", request.getContextPath()+"/edu3/system/org/unit/list.html");
			}	

		} catch (Exception e) {
			logger.error("删除用户出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查编码的唯一性
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/system/orgunit/validateCode.html")
	public void validateCode(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		String unitCode = request.getParameter("unitCode");
		String msg = "";
		if(ExStringUtils.isNotBlank(unitCode)){
			String hql = "from OrgUnit o where isDeleted=0 and o.unitCode=?";
			List list = orgUnitService.findByHql(hql, unitCode.trim());
			if(ExStringUtils.isEmpty(resourceid) && !list.isEmpty()){					
				msg = "exist";
			}else if(ExStringUtils.isNotEmpty(resourceid)){
				OrgUnit orgUnit = orgUnitService.load(resourceid);
				if(!list.isEmpty() && !orgUnit.getUnitCode().equals(unitCode.trim())){
					msg = "exist";
				}
			}
		}			
		try {
			PrintWriter writer = response.getWriter();
			writer.append(msg);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping("/edu3/framework/system/selector/orgunit.html")
	public String orgUnitSelector(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
		try {
			//组织ID input id
			String idsN = ExStringUtils.trimToEmpty(request.getParameter("idsN"));
			//组织name input id
			String namesN = ExStringUtils.trimToEmpty(request.getParameter("namesN"));
			//根组织编码
			String rootUnitCode = ExStringUtils.trimToEmpty(request.getParameter("rootCode"));
			//选择类型 1-多选，0-单选
			String checkType = ExStringUtils.trimToEmpty(request.getParameter("checkType"));
			
			//默认选择
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));
		
			List<OrgUnit> orgList = null;
			if(ExStringUtils.isNotEmpty(rootUnitCode)){
				orgList = orgUnitService.findOrgTree(rootUnitCode);
			}else{
				orgList = orgUnitService.findOrgTree(null);
			}
		
			String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(orgList,ExStringUtils.isNotEmpty(rootUnitCode) ? rootUnitCode:"UNIT_ROOT" ,defaultCheckedValue));
			model.addAttribute("idsN",idsN);
			model.addAttribute("namesN",namesN);
			model.addAttribute("unitTree", jsonString);
			model.addAttribute("checkType", checkType);
			
		} catch (Exception e) {
			logger.error("输出组织结构树出错:{}",e.fillInStackTrace());
		}			
		return "/system/org/unit-selector";
	}
	
	/**
	 * 教学点分布情况
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/system/org/distribution.html")
	public String distribution(ModelMap model,Page objPage){
		List<OrgUnit> orgUnitList = new ArrayList<OrgUnit>();
		objPage.setOrderBy("unitCode");
		objPage.setOrder(Page.ASC);
		try {
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("isChild", "N");
			condition.put("unitType", "brSchool");
			condition.put("isRecruit", "Y");
			List<OrgUnit> results = orgUnitService.findOrgByConditionByHql(condition, objPage).getResult();
			//宝安教学点和龙岗教学点合并为深圳市宝康卫生培训中心
			for (OrgUnit unit : results) {
				/*if(unit.getUnitCode().equals("13")){
					unit.setUnitName("深圳市宝康卫生培训中心");
					unit.setUnitShortName("深圳市宝康卫生培训中心");
				}else if (unit.getUnitCode().equals("18")) {
					continue;
				}*/
				orgUnitList.add(unit);
			}
			model.addAttribute("condition", condition);
			model.addAttribute("orgUnitList", orgUnitList);	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("orgUnitList", orgUnitList);	
		return "/system/org/api/gdy_distribution";
	}

	/**
	 * 教学点信息
	 * @return
	 */
	@RequestMapping("/edu3/system/org/unitInfo.html")
	public String getSynopsis(String resourceid,ModelMap model){
		OrgUnit unit = orgUnitService.get(resourceid);
		/*if(unit.getUnitCode().equals("13")){
			unit.setUnitName("深圳市宝康卫生培训中心");
			unit.setUnitShortName("深圳市宝康卫生培训中心");
		}*/
		model.addAttribute("unit",unit);
		return "/system/org/api/gdy_unitInfo";
	}
	
	/**
	 * 学校简介
	 * @return
	 */
	@RequestMapping("/edu3/system/org/synopsis.html")
	public String getSynopsis(ModelMap model){
		return "/system/org/api/gdy_synopsis";
	}
	
	/**
	 * 报到须知
	 * @return
	 */
	@RequestMapping("/edu3/system/org/reportingGuidelines.html")
	public String getGuidelines(HttpServletRequest request,ModelMap model){
		String guidelines = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()
				+ "wxgzh";
			model.addAttribute("guidelines", guidelines);
		return "/system/org/api/gdy_guidelines";
	}
	
	/**
	 * 报到通知
	 * @return
	 */
	@RequestMapping("/edu3/system/reportInform.html")
	public String getReportInform(HttpServletRequest request,ModelMap model){
		String reportInform = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()
				+ "wxgzh";
			model.addAttribute("reportInform", reportInform);
			
		return "/system/org/api/reportInform";
	}
	 
}
