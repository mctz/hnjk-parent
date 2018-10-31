package com.hnjk.platform.system.controller;

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

import com.hnjk.Version;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.mail.IMailService;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 系统共用控制类. <p>
 * 用于一些常用的公共页面处理.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： Aug 25, 200910:55:57 AM
 * @see 
 * @version 1.0
 */
@Controller
public class SysCommonController extends BaseSupportController{

	private static final long serialVersionUID = 465078903230359029L;
	
	/**注入邮件服务**/
	@Autowired
	@Qualifier("emailService")
	private IMailService mailService;
	

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;//导入组织
	
	/**
	 * 选择组织架构
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/selector/org.html")
	public String selectOrgUnit(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		/*~~~~~~~~~~~~~~~~~~~参数~~~~~~~~~~~~~~~~~~~~*/
		String parentUnitCode = ExStringUtils.defaultIfEmpty(request.getParameter("parentUnitCode"),"UNIT_ROOT");//父节点编码
		String codesN = ExStringUtils.trimToEmpty(request.getParameter("codesN"));//页面编码集合
		String idsN = ExStringUtils.trimToEmpty(request.getParameter("idsN"));//页面ID集合
		String namesN = ExStringUtils.trimToEmpty(request.getParameter("namesN"));//页面单位名称集合
		String checkedValues = ExStringUtils.trimToEmpty(request.getParameter("checkedValues"));//已选择编码集合
		String checkBoxType = ExStringUtils.defaultIfEmpty(request.getParameter("checkBoxType"), "checkbox");//checkbox类型 radio,checkbox
		try {
			List<OrgUnit> orgList = orgUnitService.findOrgTree(null);
			String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(orgList,parentUnitCode,""));
			model.addAttribute("codesN",codesN);
			model.addAttribute("namesN",namesN);
			model.addAttribute("idsN", idsN);
			model.addAttribute("checkedValues",checkedValues);
			model.addAttribute("checkBoxType", checkBoxType);
			model.addAttribute("unitTree", jsonString);
		} catch (Exception e) {
			logger.error("输出组织架构树出错:",e.fillInStackTrace());
		}		
		
		return "/system/org/org-selector";
	}
	
	
	
	/**
	 * 发送错误报告
	 * @param request
	 * @param response
	 * @param model
	 * @throws Exception
	 */
	@RequestMapping(value="/system/sendError.html")
	public void sendErrorReport(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		//拼装错误报告
		String errMsg = ExStringUtils.defaultIfEmpty(request.getParameter("errMsg"), "");
		//String currenUser = ExStringUtils.defaultIfEmpty(request.getParameter("currentUser"), "");
		User user = SpringSecurityHelper.getCurrentUser();
		StringBuffer content = new StringBuffer();
		content.append("以下是错误内容：<br/>");
		content.append(errMsg);
		content.append("错误报告人："+user.getUsername()+"\t错误发生时间："+new Date().toString()+"<br/>");
				
		if(ExStringUtils.isNotEmpty(CacheAppManager.getSysConfigurationByCode("web.system.admin.email").getParamValue())){
			mailService.sendRichMail("管理员", 
					CacheAppManager.getSysConfigurationByCode("web.system.admin.email").getParamValue(),
					"",
					"错误报告-V"+Version.SOFTWARE_VERSION+" 构建时间:"+Version.BUILD_DATE, 
					content.toString(), "");
		}
		map.put("success", true);
		renderJson(response,JsonUtils.mapToJson(map));
	}
}
