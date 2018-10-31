package com.hnjk.platform.system.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.cache.InitAppDataServiceImpl;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.service.IResourceService;
import com.hnjk.security.service.IRoleService;

/**
 * 角色权限\资源\管理Controller. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-2下午03:18:47
 * @see 
 * @version 1.0
 */
@Controller
public class RoleRightController  extends BaseSupportController{

	private static final long serialVersionUID = 2167984389888677943L;
	
	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;	
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;

	@Autowired	
	private InitAppDataServiceImpl initAppDataServiceImpl;
	
	/**
	 * 列出所有授权资源列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/system/authoriza/resource/list.html")
	public String listResource(String resourceCode,String resourceName,HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
	
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		
		if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
			String resId = ExStringUtils.trimToEmpty(request.getParameter("resId"));
			objPage.setOrderBy("showOrder");
			objPage.setOrder(Page.ASC);//设置默认排序方式
			
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			if(ExStringUtils.isNotBlank(resourceCode)) {
				condition.put("resourceCode", resourceCode);
			}
			if(ExStringUtils.isNotBlank(resourceName)) {
				condition.put("resourceName", resourceName);
			}
			if(ExStringUtils.isNotBlank(resId)) {
				condition.put("resId", resId);
			}
			
			try{
				Page page = resourceService.findResourceByCondition(condition, objPage);
				
				model.addAttribute("resList", page);
				model.addAttribute("condition", condition);
			}catch (Exception e) {
				logger.error("输出资源列表出错："+e.fillInStackTrace());
			}

			return "/system/authoriza/resource-list-sub";
		}else{
			String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));
			List<Resource> reslist = new ArrayList<Resource>();
			try {
				//reslist = resourceService.findResourceTree(ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("type", "menu");
				condition.put("code", ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
				reslist = resourceService.findResourceTree(condition);
			} catch (Exception e) {
				logger.error("输出资源列表-资源树出错："+e.fillInStackTrace());
			}
			String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(reslist,ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"),defaultCheckedValue));
			
			model.addAttribute("resourceTree", jsonString);
			
			return "/system/authoriza/resource-list";
		}		
		
		
	}
	
	/**
	 * 资源授权表单
	 * @param resourceid
	 * @param resId
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/system/authoriza/resource/input.html")
	public String addResource(String resourceid,String resId,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){ //---修改
			Resource resource = resourceService.load(resourceid);
			model.addAttribute("resource", resource);
			if(ExStringUtils.isEmpty(resId) && null!=resource.getParent()){
				resId = resource.getParent().getResourceid();
			}
		}else{ //-------------------------------------新增
			Resource resource = new Resource();
			resource.setResourceCode("RES_");
			resource.setResourcePath("#");
			resource.setResourceType("func");
			resource.setPageType("list");
			model.addAttribute("resource", resource);
		}
		Resource parentResource = resourceService.load(resId);
		//列出资源树		
		try {
			//List<Resource> resList = resourceService.findResourceCatalog("menu",ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("type", "menu");
			condition.put("code", ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
			List<Resource> resList = resourceService.findResourceTree(condition);
			model.addAttribute("resList", resList);
			model.addAttribute("parentResource", parentResource);
	
		} catch (Exception e) {
			logger.error("查找资源树出错："+e.fillInStackTrace());
		}
		
		return "/system/authoriza/resource-form";
	}
	
	/**
	 * 保存资源授权
	 * @param request
	 * @param response
	 * @param resource
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/authoriza/resource/save.html")
	public void saveResource(Resource resource,String parentId, HttpServletResponse response,HttpServletRequest request) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		Resource parentResource = null;
		//处理父子关系
		if(!ExStringUtils.isEmpty(parentId)){
			parentResource = resourceService.get(parentId);						
		}
		
		try {
			if(ExStringUtils.isNotBlank(resource.getResourceid())){//-----------编辑
				Resource p_Resource = resourceService.get(resource.getResourceid());				
				//处理级别
				if(null != p_Resource.getParent()){
					int level = parentResource.getResourceLevel() - p_Resource.getParent().getResourceLevel() ;
					//自己及子都调整级别
					resource.setResourceLevel(p_Resource.getResourceLevel()+level);
					if(null != p_Resource.getChildren() && p_Resource.getChildren().size()>0){
						for(Resource resource2 : p_Resource.getChildren()){
							resource2.setResourceLevel(resource2.getResourceLevel()+level);
							p_Resource.getChildren().add(resource2);
						}
					}
				}
				
				if("menu".equals(p_Resource.getResourceType())){//如果是菜单，设置父有子
					parentResource.setIsChild(Constants.BOOLEAN_YES);
				}
				resource.setParent(parentResource);
				resource.setChildren(p_Resource.getChildren());
				
				ExBeanUtils.copyProperties(p_Resource, resource);

				resourceService.updateResource(p_Resource);	
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"修改资源：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}else{ //------------------------------------------------------------新增
				Resource res = resourceService.findUniqueByProperty("resourceCode", resource.getResourceCode());
				if(null != res){
					throw new WebException("已存在相同编号的资源，请重新输入！");
				}
				if("menu".equals(parentResource.getResourceType())){
					parentResource.setIsChild(Constants.BOOLEAN_YES);
				}
				resource.setParent(parentResource);
				parentResource.getChildren().add(resource);
				resource.setResourceLevel(parentResource.getResourceLevel()+1);

				resource.setIsChild(Constants.BOOLEAN_NO);
				resourceService.saveResource(resource);	
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.INSERT,"新增资源：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_AUTHORIZA_RESOURCE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/authoriza/resource/input.html?resourceid="+resource.getResourceid());
		} catch (Exception e) {				
			logger.error("保存错误:"+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 删除资源授权
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/authoriza/resource/delete.html")
	public void deleteResource(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					resourceService.deleteCascadeArray(resourceid);			
				}else{//单个删除
					resourceService.deleteResource(resourceid);
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("localArea", "_resourceListContent");
				//map.put("forward", request.getContextPath()+"/edu3/system/authoriza/resource/list.html");
			}	

		} catch (Exception e) {
			logger.error("删除资源出错:{}",e.fillInStackTrace());		
			map.put("statusCode", 300);
			map.put("message", "删除出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 检查编码唯一性
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/framework/system/authoriza/resource/validateCode.html")
	public void validateCode(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String type = request.getParameter("type");
		String resourceCode = request.getParameter("resourceCode");
		String roleCode = request.getParameter("roleCode");
		String resourceid = request.getParameter("resourceid");
		String msg = "";
		if("resource".equalsIgnoreCase(type)){
			if(ExStringUtils.isNotBlank(resourceCode)){
				String hql = "from Resource r where r.resourceCode=?";
				List list = resourceService.findByHql(hql, resourceCode.trim());
				if(ExStringUtils.isEmpty(resourceid) && !list.isEmpty()){
					msg = "exist";
				}else if(ExStringUtils.isNotEmpty(resourceid)){
					Resource resource = resourceService.load(resourceid);
					if(!list.isEmpty() && !resource.getResourceCode().equals(resourceCode.trim())){
						msg = "exist";
					}
				}
			}			
		}else if("role".equalsIgnoreCase(type)){
			if(ExStringUtils.isNotBlank(roleCode)){
				String hql = "from Role r where r.roleCode=?";
				List list = roleService.findByHql(hql, roleCode.trim());
				if(ExStringUtils.isEmpty(resourceid) && !list.isEmpty()){ 
					msg = "exist"; 
				}else if(ExStringUtils.isNotEmpty(resourceid)){
					Role role = roleService.load(resourceid);
					if(!list.isEmpty() && !role.getRoleCode().equals(roleCode.trim())){
						msg = "exist";						
					}
				}
			}
		}		
		try {
			PrintWriter writer = response.getWriter();
			writer.append(msg);
			writer.close();
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * 重载资源到缓存中缓存
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/authoriza/resource/recache.html")
	public void reCache() throws WebException{
		//清理资源缓存
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).remove(CacheSecManager.CACHE_SEC_RESOURCE);
		EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE).remove(CacheSecManager.CACHE_SEC_ROLE_RESOURCE);

		//重新载入
		initAppDataServiceImpl.reloadResource();
		initAppDataServiceImpl.reloadResourceRoleRef();
	}
	
	/**
	 * 列出所有的角色列表=============================================角色管理================================================================
	 * \@param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/authoriza/role/list.html")
	public String listRole(String roleCode,String roleName,Page objPage,ModelMap model) throws WebException{
		//查询所有角色列表
		objPage.setOrderBy("roleCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
			
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		if(ExStringUtils.isNotEmpty(roleCode)) {
			condition.put("roleCode", roleCode);
		}
		if(ExStringUtils.isNotEmpty(roleName)) {
			condition.put("roleName", roleName);
		}

		Page roleList = roleService.findRoleByCondition(condition, objPage);
		
		model.addAttribute("roleList", roleList);
		model.addAttribute("condition", condition);
		return "/system/authoriza/role-list";
	}
	
	/**
	 * 新增角色表单
	 * @param resourceid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/authoriza/role/input.html")
	public String addRole(String resourceid,HttpServletRequest request,ModelMap model) throws WebException{		
		
		if(ExStringUtils.isNotBlank(resourceid)){//-------------编辑
			Role role = roleService.get(resourceid);		
			model.addAttribute("role", role);
			//改角色已经分配的权限
			//StringBuffer roleAuthoritys = new StringBuffer();
			String roleAuthoritys = roleService.getAuthoritysByRole(role);
			/*if(null != role.getAuthoritys() && role.getAuthoritys().size()>0){
				for(Resource resource : role.getAuthoritys()){
					roleAuthoritys.append(resource.getResourceid());
					roleAuthoritys.append(",");
				}
			}	*/
			model.addAttribute("roleAuthoritys", roleAuthoritys);
			
		}else{ //------------------------------------------------新增
			model.addAttribute("role", new Role());
		}
		try {
			//List<Resource> resList = resourceService.findResourceCatalog("menu",ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));//获取全部资源
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("type","menu");
			condition.put("code", ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
			List<Resource> resList = resourceService.findResourceTree(condition);
			model.addAttribute("resList",resList);
		} catch (Exception e) {
			logger.error("查找资源树出错："+e.fillInStackTrace());
		}
		
		return "/system/authoriza/role-form";
	}
	
	/**
	 * 保存角色表单
	 * @param role
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/system/authoriza/role/save.html")
	public void saveRole(Role role, HttpServletRequest request,HttpServletResponse response) throws WebException{
//		String[] authoritysIds = role.getResourceFuncId();//分配给角色的资源
//		String[] authoritysIds = request.getParameterValues("resourceFuncId");
		
		String resourceFuncIds = ExStringUtils.trimToEmpty(request.getParameter("resourceFuncIds"));
		List<String> selIdlist = new ArrayList<String>();//已选的ID
		if(ExStringUtils.isNotBlank(resourceFuncIds)){
			selIdlist =  Arrays.asList(resourceFuncIds.split(","));
		}
		
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			Role persistRole = null;
			if(ExStringUtils.isNotEmpty(role.getResourceid())){//修改
				persistRole = roleService.get(role.getResourceid());				
			
			}else {//新增				
				persistRole = new Role();				
			}
			Role tmpRole = new Role();
			ExBeanUtils.copyProperties(persistRole,role);
			ExBeanUtils.copyProperties(tmpRole,persistRole);
			if(ExStringUtils.isNotBlank(role.getParentId())){//设置父角色
				Role parentRole = roleService.get(role.getParentId());
				persistRole.setParent(parentRole);
				parentRole.getChildren().add(persistRole);
			}	
				
			roleService.saveOrUpdateRole(persistRole, selIdlist);
						
			if(ExStringUtils.isNotEmpty(role.getResourceid())){//修改
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"修改角色：角色编码："+role.getRoleCode());
			}else{
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.INSERT,"新增角色：角色编码："+role.getRoleCode());
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_AUTHORIZA_ROLE_EDIT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/system/authoriza/role/input.html?resourceid="+persistRole.getResourceid());
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败：<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 角色删除
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/authoriza/role/delete.html")
	public void deleteRole(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					roleService.deleteArray(resourceid.split(","));
				}else{//单个删除
					roleService.deleteRole(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.DELETE,"删除角色：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath()+"/edu3/system/authoriza/role/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/*
	@RequestMapping("/edu3/framework/system/authoriza/role/selectResourceList.html")
	public void listResourceRole(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resid = request.getParameter("resid"); //资源id
		String roleid = request.getParameter("roleid"); //角色id
		String msg = "";
		//String sql = "select * from hnjk_sys_resources t start with t.parentid=? connect by prior t.resourceid=t.parentid"; //查找子资源
		String sql2 = "select t.authorityid from hnjk_sys_roleauthority t where t.roleid=? and t.authorityid in (select t.resourceid from hnjk_sys_resources t start with t.resourceid=? connect by prior t.resourceid=t.parentid)";
		//List<Resource> resList = resourceService.findByHqlStartWith(sql,new String[]{resid});
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("id", resid);
		List<Resource> resList = resourceService.findResourceTree(condition);
		StringBuilder sb = new StringBuilder(); //-------查找已有资源id集合
		List resIdList = resourceService.findBySqlStartWithObject(sql2,new String[]{roleid,resid});
		for(int index=0;index<resIdList.size();index++){
			sb.append(",");
			sb.append(resIdList.get(index).toString());
		}
		
		List<Resource> resListJson = new ArrayList<Resource>();
		for(Resource res : resList){
			Resource resource = new Resource();
			ExBeanUtils.copyProperties(resource, res);
			resource.setParent(null);
			resource.setChildren(null);
			resListJson.add(resource);
		}
		
		//Gson gson = new Gson();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("resList", resListJson);
		map.put("resHideId", sb.toString());
		
		//msg = gson.toJson(map);
		msg = JsonUtils.mapToJson(map);
		response.setCharacterEncoding("utf8");
		try {
			PrintWriter writer = response.getWriter();
			writer.append(msg);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	
	
	/**
	 * dk
	 * 修改角色对应的资源
	 */
	@RequestMapping("/edu3/framework/system/authoriza/saveRoleResourceRef.html")
	public void saveRoleResourceRef(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String roleId = request.getParameter("roleId");
		if(!StringUtils.isEmpty(roleId)) {
			String resourceIds = request.getParameter("resourceIds");			
			Role role = roleService.get(roleId);
			
			List<String> selectedResourceids = new ArrayList<String>();//提交的资源
			if(null != resourceIds){
				selectedResourceids = new ArrayList<String>(Arrays.asList(resourceIds.split(",")));
			}
			
			try {
				roleService.saveOrUpdateRole(role,selectedResourceids);
				
			}catch(Exception ex) {
				renderText(response, "false");
				logger.error("分配权限出错：{}",ex.fillInStackTrace());
			}
			renderText(response, "true");
		}else {
			renderText(response, "false");
		}
		
	}
	
	@RequestMapping("/edu3/framework/system/selector/role.html")
	public String nodePicker(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String idsV = request.getParameter("idsV");
		idsV = idsV == null ? "" : idsV;
		List<Role> list = roleService.getAll();
		StringBuffer sb = new StringBuffer();
		if(null != list && list.size()>0){
			for(Role o : list){
				sb.append("tree.N[\"").append(StringUtils.isEmpty(o.getParentId()) ? "0" : o.getParentId()).append("_").append(o.getResourceid()).append("\"]");
				sb.append(" = ").append("\"ctrl:sel;checked:"+(idsV.indexOf(o.getResourceid())>=0?"1":"0")+";T:").append(o.getRoleName()).append(";\";\r\n");
			}
		}				
		request.setAttribute("htmlStr", sb.toString());
		return "/system/authoriza/selector_role";
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/framework/system/selector/resource.html")
	public String nodePicker_(HttpServletRequest request,HttpServletResponse response) throws Exception{
		String roleId = request.getParameter("roleId");
		StringBuffer sb = new StringBuffer();
		if(!StringUtils.isEmpty(roleId)) {
			Role role = roleService.get(roleId);
			// 已经有权限的id
			StringBuffer sids = new StringBuffer();
			for(Resource res : role.getAuthoritys()) {
				sids.append(res.getResourceid()).append(",");
			}
			
			List<Resource> list =(List<Resource>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).get(CacheSecManager.CACHE_SEC_RESOURCE);
			if(null != list && list.size()>0){
				for(Resource o : list){			
						sb.append("tree.N[\"").append(o.getParent() == null ? "0" : o.getParent().getResourceid()).append("_").append(o.getResourceid()).append("\"]");
						sb.append(" = ").append("\"ctrl:sel;checked:"+(sids.indexOf(o.getResourceid())>=0?"1":"0")+";T:").append(o.getResourceName()).append(";\";\r\n");
				}
			}
		}
		request.setAttribute("htmlStr", sb.toString());
		return "/system/authoriza/selector_res";
	}
	
	/*
	@RequestMapping("/edu3/framework/system/gethelpdoc.html")
	public void getHelpDoc(HttpServletRequest request,HttpServletResponse response) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();//获取当前用户
		Role role = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if(null != user.getRoles()){
				role = new ArrayList<Role>(user.getRoles()).get(0);
			}
			if(null != role){
				//如果附件不为空，则载入附件
				List<Attachs> attachList = attachsService.findByHql("from "+Attachs.class.getName()+ " where isDeleted=0 and formId = ? order by uploadTime desc", role.getResourceid());	
				if(null!=attachList && attachList.size()>0){
					Attachs attachs = attachList.get(0);
					map.put("attachid", attachs.getResourceid());	
					map.put("error", "");
				}else{		
					map.put("error", "没有对应的帮助文档！");
					
				}
			}
			renderJson(response, JsonUtils.mapToJson(map));
		} catch (Exception e) {
			
		}
		
	}
	*/
}
