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
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.platform.system.model.Version;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.platform.system.service.IVersionService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Controller
public class VersionController extends BaseSupportController {

	private static final long serialVersionUID = -4061072182469139610L;
	
	@Qualifier("versionService")
	@Autowired
	private IVersionService versionService;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;
	
	@RequestMapping("/system/version/list.html")
	public String exeList(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model){
		objPage.setOrderBy("versionNo");
		objPage.setOrder("desc");
		Map<String, Object> condition = new HashMap<String, Object>();
		String versionNo = ExStringUtils.trimToEmpty(request.getParameter("versionNo"));
		String versionName = ExStringUtils.trimToEmpty(request.getParameter("versionName"));
		String isPublish = ExStringUtils.trimToEmpty(request.getParameter("isPublish"));
		
		if(ExStringUtils.isNotEmpty(versionNo)) {
			condition.put("versionNo", Integer.parseInt(versionNo));
		}
		if(ExStringUtils.isNotEmpty(versionName)) {
			condition.put("versionName", versionName);
		}
		if(ExStringUtils.isNotEmpty(isPublish)) {
			condition.put("isPublish", isPublish);
		}
		Page page = versionService.findByCondition(condition, objPage);
		
		model.addAttribute("versionList", page);
		model.addAttribute("condition", condition);
		return "/system/version/version-list";
	}

	/**
	 * 新增/编辑表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/system/version/edit.html")
	public String editVersion(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Version version = null;
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			version = versionService.get(resourceid);	
		}else{ //----------------------------------------新增
			version = new Version();	
		}
		model.addAttribute("version", version);
		return "/system/version/version-form";
	}
	
	/**
	 * 保存/更新
	 * @param setTime
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/system/version/save.html")
	public void saveVersion(Version version,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		try {
			do{
				Date today = new Date();
				List<Version> list = versionService.findByHql("from "+Version.class.getSimpleName()+" c where c.isDeleted=0"
						+ " and c.versionNo=?",version.getVersionNo());
				if(list!=null && list.size()>0 && !list.get(0).getResourceid().equals(version.getResourceid())){
					statusCode = 300;
					message = "该版本号已存在！";
					continue;
				}
				User user = SpringSecurityHelper.getCurrentUser();
				version.setOperator(user);
				version.setOperatorName(user.getCnName());
				version.setUpdateDate(today);
				Version _version = new Version();
				if(ExStringUtils.isNotBlank(version.getResourceid())){ //  更新
					_version = versionService.get(version.getResourceid());	
					version.setCreateDate(_version.getCreateDate());
				}else{ // 保存
					version.setCreateDate(today);
				}		
				
				ExBeanUtils.copyProperties(_version, version);
				versionService.saveOrUpdate(_version);
				map.put("navTabId", "RES_SYS_VERSION_UPDATE");
				map.put("reloadUrl", request.getContextPath() +"/system/version/edit.html?resourceid="+_version.getResourceid());
			} while(false);
		}catch (Exception e) {
			logger.error("保存出错",e);
			statusCode = 300;
			message = "保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 批量删除
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/system/version/delete.html")
	public void deleteVersion(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceids = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceids)){
				if(resourceids.split(",").length >1){//批量删除					
					versionService.batchCascadeDelete(resourceids.split(","));
				}else{//单个删除
					versionService.delete(resourceids);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
			}
		} catch (Exception e) {
			logger.error("删除出错",e);
			map.put("statusCode", 300);
			map.put("message", "删除失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
