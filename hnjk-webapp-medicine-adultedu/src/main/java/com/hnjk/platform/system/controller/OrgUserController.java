package com.hnjk.platform.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Edumanager;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.service.IEdumanagerService;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.taglib.tree.TreeBuilder;
import com.hnjk.extend.taglib.tree.TreeNode;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.OrgUnitExtends;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;

/**
 * 组织与用户管理Controller. <p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-12下午04:41:55
 * @see 
 * @version 1.0
 */
@Controller
public class OrgUserController  extends BaseSupportController{

	private static final long serialVersionUID = 3414468055619360637L;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private  IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("edumanagerService")
	private IEdumanagerService edumanagerService;//注入教学管理人员服务接口
		
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//注入SQL支持
	
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;
	/**
	 * 用户登录入口
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/user/login.html")
	public String userLogin() throws WebException{
		return "/login_new";
	}
	
	
	/**
	 * 返回用户列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/user/list.html")
	public String userList(String cnName,String username,Page objPage,ModelMap model,HttpServletRequest request) throws WebException{
		String isSubPage = ExStringUtils.trimToEmpty(request.getParameter("isSubPage"));//是否子页面请求
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));//默认组织单位		
	
			if(ExStringUtils.isNotEmpty(isSubPage) && "y".equalsIgnoreCase(isSubPage)){//如果子页面请求
				objPage.setOrderBy("showOrder");
				objPage.setOrder(Page.ASC);//设置默认排序方式
				try {
					//获取角色列表			
					List<Role> roles = roleService.findByHql("from "+Role.class.getSimpleName()+" where isDeleted = ?", 0);
					model.addAttribute("roles", roles);			
					Map<String,Object> condition = new HashMap<String,Object>();//查询条件
					if(ExStringUtils.isNotEmpty(username)) { condition.put("userName", username);condition.put("username", username);}
					if(ExStringUtils.isNotEmpty(cnName)) {
						condition.put("cnName", cnName);
					}
					if(ExStringUtils.isNotEmpty(unitId)) {
						condition.put("unitId", unitId);
					}
					
					condition.put("userType",CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());//用户类型为管理人员
					User cureentUser = SpringSecurityHelper.getCurrentUser();
					request.getSession().setAttribute("cureentUser", cureentUser.getResourceid());
					
					Page userList = userService.findUserByCondition(condition, objPage);
					model.addAttribute("condition", condition);
					model.addAttribute("userList", userList);
				} catch (Exception e) {
					logger.error("输出用户列表出错："+e.fillInStackTrace());
				}				
				
				return "/system/org/user-list-sub";
			}else{//组织树				
				String defaultCheckedValue = ExStringUtils.trimToEmpty(request.getParameter("checkedValue"));			
				List<OrgUnit> orgList  = new ArrayList<OrgUnit>();
				try {
					orgList = orgUnitService.findOrgTree(null);
				} catch (Exception e) {					
					logger.error("输出用户列表-组织树出错："+e.fillInStackTrace());
				}				
				String jsonString = JsonUtils.objectToJson(TreeBuilder.buildTreeById(orgList,"UNIT_ROOT" ,defaultCheckedValue));
				model.addAttribute("unitTree", jsonString);
				return "/system/org/user-list";
			}	
		
		
		
	}
	
	
	/**
	 * 网院通讯录
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/person/info/addressbook.html")
	public String addressBookList(String cnName,String username,Page objPage,ModelMap model,HttpServletRequest request) throws WebException{
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		
		objPage.setOrderBy("orgUnit.unitLevel,showOrder");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		
		//获取单位树
		try{
			List<OrgUnit> orgList = orgUnitService.findOrgTree(null);
			List<TreeNode> treeNodes = new ArrayList<TreeNode>();//树列表
			if(null != orgList && orgList.size()>0){
				for(OrgUnit unit : orgList){
					String parentId = "";
					boolean isLeaf = true;

					if(null !=unit.getParent()) {
						parentId = unit.getParent().getResourceid();
					}
					if("Y".equals(unit.getIsChild())) {
						isLeaf = false;
					}
					
					treeNodes.add(new TreeNode(unit.getResourceid(), 
							unit.getUnitName(), 
							parentId,unit.getUnitLevel(), 
							isLeaf, "goSelected('"+unit.getResourceid()+"');")
					);
				}//end for
			}//end if						
			model.addAttribute("unitId", unitId);
			model.addAttribute("unitTree", treeNodes);
			
			Map<String,Object> condition = new HashMap<String,Object>();//查询条件
			if(ExStringUtils.isNotEmpty(username)) {
				condition.put("username", username);
			}
			if(ExStringUtils.isNotEmpty(cnName)) {
				condition.put("cnName", cnName);
			}
			if(ExStringUtils.isNotEmpty(unitId)) {
				condition.put("unitId", unitId);
			}
			condition.put("userType", CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());
			condition.put("isDeleted", 0);
			
			Page userList = userService.findUserByCondition(condition, objPage);
			model.addAttribute("condition", condition);
			model.addAttribute("userList", userList);
		}catch(Exception e){
			logger.error("查找用户出错：{}",e.getStackTrace());
		}
		
		return "/system/org/addressbook-list";
	}
	
	/**
	 * 新增用户
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/user/input.html")
	public String addUser(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//String sql = "select * from hnjk_sys_roles t  where t.isdeleted=0 start with t.parentid is null  connect by prior t.resourceid=t.parentid order by t.showorder";
		List<Role> roleList = roleService.findRoleTreeList(null);
		List<Role> list = new ArrayList<Role>();
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		if(ExStringUtils.isNotBlank(resourceid)){//修改
			try {
				Map<String,String> map = new HashMap<String, String>();
				map.put("userid", resourceid);
				//String sql2 = "select t.roleid from hnjk_sys_roleusers t where t.userid=:userid";
				//list = userService.findRoleByUser(sql2, map);
				list = userService.findUserRoles(resourceid);
			} catch (Exception e) {				
				logger.error("查找角色id报错！"+e.fillInStackTrace());
			}
			User user = userService.get(resourceid);	
			model.addAttribute("user", user);
			
			if(ExStringUtils.isEmpty(unitId) && null!=user.getOrgUnit()){
				unitId = user.getOrgUnit().getResourceid();
			}
		}else{
			User user = new Edumanager();
			user.setUserType(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue());
			model.addAttribute("user", user);			
		}
		
		if(!ExStringUtils.isEmpty(unitId)){
			OrgUnit orgUnit = orgUnitService.get(unitId);
			model.addAttribute("orgUnit", orgUnit);
		}	
			
		model.addAttribute("userRole", list);
		model.addAttribute("roleList", roleList);
		model.addAttribute("resourceid", resourceid);
		return "/system/org/user-form";
	}
	
	/**
	 * 保存用户
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/user/save.html")
	public void saveUser(Edumanager user,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String hidRoleId  = ExStringUtils.trimToEmpty(request.getParameter("hidRoleId")); //隐藏角色id
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));//用户所属单位ID
		String[] roleIds  = request.getParameterValues("roleId"); //角色IdS
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "保存成功！";
		for(int k=0;k<1;k++){
			try {
				if(ExStringUtils.isNotBlank(user.getResourceid())){//---------编辑	
					User persistUser = userService.get(user.getResourceid());		
					if(!persistUser.getUsername().equals(user.getUsername())){
						User u = userService.findUniqueByProperty("username", user.getUsername());
						if(null != u){
							statusCode = 300;
							message = "该用户名已存在！<br/>如果该用户被删除，您可以将在编辑页面中，选择恢复功能.";
							break;
						}	
					}
					if(!user.getPassword().equals(persistUser.getPassword())){	//更改密码，如果前后密码发生变化，则MD5加密					
						user.setPassword(BaseSecurityCodeUtils.getMD5(user.getPassword()));			
					}
							
					ExBeanUtils.copyProperties(persistUser, user);
					if(ExStringUtils.isNotBlank(unitId)){
						OrgUnit orgUnit = orgUnitService.get(user.getUnitId());
						persistUser.setOrgUnit(orgUnit);
						orgUnit.getUser().add(persistUser);
					}
							
					if(ExStringUtils.isNotEmpty(hidRoleId)){ //删除中间表
						Criterion[] criterion = new Criterion[1];
						criterion[0] = Restrictions.in("resourceid", hidRoleId.split(","));
						List removeList = roleService.findByCriteria(criterion);
						persistUser.getRoles().removeAll(removeList);
					}
							
					if(null!=roleIds && roleIds.length>0){
						for(String roleid : roleIds){
							Role role = roleService.load(roleid);
							persistUser.getRoles().add(role);
						}
					}		
					
					userService.update(persistUser);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"管理员更新用户：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
					
				}else{ //---------------------------------------------------新增
					if(ExStringUtils.isNotBlank(unitId)){
						User u = userService.findUniqueByProperty("username", user.getUsername());
						if(null != u){
							statusCode = 300;
							message = "该用户名已存在！<br/>如果该用户被删除，您可以将在编辑页面中，选择恢复功能.";
							break;
						}	
						OrgUnit orgUnit = orgUnitService.load(user.getUnitId());
						user.setOrgUnit(orgUnit);
						orgUnit.getUser().add(user);
					}
					
					if(null!=roleIds && roleIds.length>0){
						for(String roleid : roleIds){
							Role role = roleService.load(roleid);
							user.getRoles().add(role);
						}
					}
						userService.save(user);	
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.INSERT,"管理员添加用户：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				}
				
				map.put("navTabId", "RES_ORG_USER");
				map.put("reloadUrl", request.getContextPath() +"/edu3/system/org/user/input.html?resourceid="+user.getResourceid());
			} catch (Exception e) {
				logger.error("保存用户错误:"+e.fillInStackTrace());
				statusCode =  300;
				message = "保存失败!";							
			}
		}
		map.put("statusCode", statusCode);
		map.put("message", message);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 删除用户
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/user/delete.html")
	public void deleteUser(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{	
	
		Map<String ,Object> map = new HashMap<String, Object>();	
		try {
			if(!ExStringUtils.isEmpty(resourceid)){
				if(resourceid.split("\\,").length >1){//批量删除					
					userService.batchDelete(resourceid.split("\\,"));
				}else{//单个删除
					userService.delete(resourceid);
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.DELETE,"管理员删除用户：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("localArea", "_userListContent");
				//map.put("forward", request.getContextPath()+"/edu3/system/org/user/list.html");
			}	

		} catch (Exception e) {
			logger.error("删除用户出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 用户个人设置
	 * @param userid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/user/setting.html")
	public String changeUserInfo(String act,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String actStr = ExStringUtils.trimToEmpty(act);
		User user  	  = SpringSecurityHelper.getCurrentUser();
		user 		  = userService.get(user.getResourceid());
		OrgUnit unit  = null;
		//因为学生基本信息和教务人员基本信息为不同的model，所以这里使用currentRole加以区分
		if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(user.getUserType())){//如果为学生，则取学生的基本信息				
			//1.找出默认学籍的学籍信息,
			StudentInfo studentInfo = null;
			if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				String defaultStudentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				if(ExStringUtils.isNotEmpty(defaultStudentId)){
					studentInfo = studentInfoService.get(defaultStudentId);
				}
			}
			//2.如果没有默认学籍，则根据系统用户找出改学生信息
			if(null == studentInfo){
				List<StudentInfo> list = studentInfoService.findByHql("from "+StudentInfo.class.getSimpleName()+ " where isDeleted = ? and sysUser.resourceid = ?", 
							0,user.getResourceid()) ;
				if(null != list && !list.isEmpty()) {
					studentInfo = list.get(0);
				}
			}
			model.addAttribute("user", studentInfo);
			model.addAttribute("currentRole", "student");
		}else {
			model.addAttribute("user", user);
			model.addAttribute("currentRole", "edumanage");
		}
		//学习中心
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())&&
		  !user.getUserType().equals(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue())) {
			model.addAttribute("brSchool", true);
			unit = user.getOrgUnit();
		}
		String phoneComfirm = CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue();
		model.addAttribute("phoneComfirm",phoneComfirm);
		//这个userid用于修改密码，及个人设置，这是所有用户都共用user这个model
		model.addAttribute("userid", user.getResourceid());
			
		String fwdUrl = "/system/org/changeuser-form";//更改用户信息
		
		if("changepwd".equals(actStr)){//更改密码
			fwdUrl = "/system/org/changeuser-pwd-form";
		}else if("setting".equals(actStr)){	//更改个人设置
			if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_FACE)){
				//获取用户头像
				model.addAttribute("userface", ExStringUtils.defaultIfEmpty(user.getPropertys(UserExtends.USER_EXTENDCODE_FACE).getExValue(),""));
			}
			//获取用户所在城市
			if(null != user.getUserExtends() && null != user.getPropertys(UserExtends.USER_EXTENDCODE_CITY) && ExStringUtils.isNotEmpty(user.getPropertys(UserExtends.USER_EXTENDCODE_CITY).getExValue())){
				model.addAttribute("province", user.getPropertys(UserExtends.USER_EXTENDCODE_CITY).getExValue().split(",")[0]);
				model.addAttribute("city", user.getPropertys(UserExtends.USER_EXTENDCODE_CITY).getExValue().split(",")[1]);
			}else{
				model.addAttribute("province", "广东省");
				model.addAttribute("city","广州市");
			}
				
			//TODO 获取用户菜单风格
					
			model.addAttribute("storeDir", user.getUsername());				
			model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL"
			fwdUrl = "/system/org/changeuser-setting-form";
			
		}else if ("unitSetting".equals(actStr)) {//设置学习中心联系点信息
			model.addAttribute("unit",unit);
			
			fwdUrl = "/system/org/changeOrgUnit-form";
		}
		return fwdUrl;
	}
	
	
	/**
	 * 更改联系方式 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/system/org/changeuser-form/editcontact.html")
	public void editContact(HttpServletRequest request,HttpServletResponse response){
		
		String certNum  			  = StringUtils.trimToEmpty(request.getParameter("certNum"));
		String userid  			  = StringUtils.trimToEmpty(request.getParameter("userid"));
		String editFlag 			  = StringUtils.trimToEmpty(request.getParameter("editFlag"));
		String results 				  = StringUtils.trimToEmpty(request.getParameter("editResult"));
		String msgAuthCode = StringUtils.trimToEmpty(request.getParameter("msgAuthCode"));	// 短信验证码
		String isChange  			  = StringUtils.trimToEmpty(request.getParameter("isChange"));
		Map<String,Object> map        = new HashMap<String, Object>();
		String success                = Constants.BOOLEAN_NO;
		String msg                    = "";
		
		do{
			String phoneComfirm = CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue();
			if("1".equals(phoneComfirm) && "mobile".equals(editFlag) && !"N".equals(isChange)){// 需要手机短信验证
				String cacheMsgAuthCode = memcachedManager.get(results);
				if(ExStringUtils.isEmpty(cacheMsgAuthCode) ){
					 success	 = Constants.BOOLEAN_NO;
					 msg  = "手机验证码不正确或已经过期，请重新发送！";
					 continue;
				}
				if(!cacheMsgAuthCode.equals(msgAuthCode)){
					success	 = Constants.BOOLEAN_NO;
					msg  = "手机验证码不正确！";
					 continue;
				}
			}
			
			Set<String> set = new HashSet<String>();
			if (StringUtils.isNotEmpty(certNum)) {//学生
				// 检查该号码是否已被使用
//				List<StudentBaseInfo> studentList = studentService.findByHql("from "+StudentBaseInfo.class.getSimpleName()+" s where s.mobile=? and s.isDeleted=0", results);
//				if(ExCollectionUtils.isNotEmpty(studentList)) {
//					for(StudentBaseInfo stuBase : studentList){
//						set.add(stuBase.getCertNum());
//					}
//				}
				List<StudentInfo> stuList = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()+" where studentBaseInfo.mobile=? and firstComfirmStatus=? and studentStatus=? and isDeleted=0 ", new Object[]{results,"1","11"});
				if(ExCollectionUtils.isNotEmpty(stuList)) {
					for(StudentInfo stu : stuList){
						set.add(stu.getStudentBaseInfo().getCertNum());
					}
				}
			}else if (ExStringUtils.isNotEmpty(userid)) {//edumanage
				// 检查该号码是否已被使用
				List<User> userList = userService.findByHql("from "+User.class.getSimpleName()+" s where s.mobile=? and s.isDeleted=0", results);
				if(ExCollectionUtils.isNotEmpty(userList)) {
					for(User u : userList){
						set.add(u.getResourceid());
					}
				}
			}else {
				msg = "修改失败！";
			}
			if(ExCollectionUtils.isNotEmpty(set) && !set.contains(certNum)){
				success	 = Constants.BOOLEAN_NO;
				msg  = "该手机号码已被使用！";
				continue;
			}
		} while(false);
		
		map.put("msg", msg);
		map.put("success", success);
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 保存更改设置
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/user/savesetting.html")
	public void saveChangeUserInfo(String act,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid              = ExStringUtils.defaultIfEmpty(request.getParameter("resourceid"),"");//id		
		User cureentUser      		   = SpringSecurityHelper.getCurrentUser();
		
		Map<String ,Object> map        = new HashMap<String, Object>();	
		
		try {
			if("info".equals(act)){//修改个人信息
				String cnName          = ExStringUtils.trimToEmpty(request.getParameter("cnName"));//中文名
				String officeTel       = ExStringUtils.trimToEmpty(request.getParameter("officeTel"));//办公电话
				String homeTel         = ExStringUtils.trimToEmpty(request.getParameter("homeTel"));//家庭电话
				String mobile          = ExStringUtils.trimToEmpty(request.getParameter("mobile"));//手机
				String email           = ExStringUtils.trimToEmpty(request.getParameter("email"));//邮件
				String studentRoleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();//学生角色编码
				if(SpringSecurityHelper.isUserInRole(studentRoleCode)){//如果是学生
					String customUsername = ExStringUtils.trimToEmpty(request.getParameter("customUsername"));//用户昵称
					//校验昵称是否重复
					StudentInfo studentInfo = studentInfoService.get(resourceid);	
					
					if(!customUsername.equals(studentInfo.getSysUser().getCustomUsername())){
						List<User> uList = userService.findByHql(" from "+User.class.getSimpleName()+" where customUsername = ? or username = ? ", customUsername,customUsername);
						if(null != uList && uList.size() >0){
							throw new WebException("该简易登录账号已被他人使用，请修改！");
						}
					}				
					
					//设置基本信息
					StudentBaseInfo studentBaseInfo = studentInfo.getStudentBaseInfo();
					//开启日志记录
					//studentBaseInfo.setEnableLogHistory(true);
					studentBaseInfo.setName(cnName);
					studentBaseInfo.setOfficePhone(officeTel);
					studentBaseInfo.setHomePhone(homeTel);
					studentBaseInfo.setMobile(mobile);
					studentBaseInfo.setEmail(email);
					studentInfo.setStudentBaseInfo(studentBaseInfo);
					
					//设置系统用户信息
					User sysUser = studentInfo.getSysUser();
					sysUser.setCnName(cnName);
					sysUser.setCustomUsername(customUsername);
					studentInfo.setSysUser(sysUser);
					
					studentInfoService.update(studentInfo);
					
				}else{
					Edumanager edumanager = edumanagerService.get(resourceid);
					edumanager.setCnName(cnName);
					edumanager.setOfficeTel(officeTel);
					edumanager.setHomeTel(homeTel);
					edumanager.setMobile(mobile);
					edumanager.setEmail(email);
					edumanagerService.update(edumanager);		
					
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"用户修改个人信息：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "设置成功！");
				map.put("navTabId", "RES_ORG_USER");				
				
			}else if("changepwd".equals(act)){//修改个人密码
				String oldPassword = ExStringUtils.trimToEmpty(request.getParameter("oldPassword"));//旧密码
				String newPassword = ExStringUtils.trimToEmpty(request.getParameter("newPassword"));//新密码
				//验证旧密码是否正确
				//User user 		   = userService.findUnique("from "+User.class.getSimpleName()+" where username = ? and password = ? ", cureentUser.getUsername(),BaseSecurityCodeUtils.getMD5(oldPassword));
				/*
				if(null != user){
					user.setPassword(BaseSecurityCodeUtils.getMD5(newPassword));
					userService.update(user);
					
					map.put("statusCode", 200);
					map.put("message", "密码已更改，请牢记新密码！");
					map.put("navTabId", "RES_ORG_USER");					
				}else{//校验失败
					map.put("statusCode", 300);
					map.put("message", "旧密码不正确！");
				}
				*/
				userService.changedUserPassword(cureentUser.getResourceid(), oldPassword, newPassword);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"用户修改密码：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "密码已更改，请牢记新密码！");
				map.put("navTabId", "RES_ORG_USER");
				
			}else if("setting".equals(act)){//修改个人设置
				
				String userface  = ExStringUtils.trimToEmpty(request.getParameter("userface"));//用户自定义头像			
				String city 	 = ExStringUtils.trimToEmpty(request.getParameter("usercity"));//用户所在城市
				String menustyle = ExStringUtils.trimToEmpty(request.getParameter("menustyle"));//菜单类型
				
				User user = userService.get(resourceid);
				
				UserExtends userExtends = null;
				
				if(null != user.getUserExtends().get(UserExtends.USER_EXTENDCODE_FACE)){
					userExtends  = user.getUserExtends().get(UserExtends.USER_EXTENDCODE_FACE);
					userExtends.setExValue(userface);
				}else{
					userExtends  = new UserExtends(UserExtends.USER_EXTENDCODE_FACE,userface,user);
				}				
				
				user.getUserExtends().put(UserExtends.USER_EXTENDCODE_FACE, userExtends);
				
				if(null != user.getUserExtends().get(UserExtends.USER_EXTENDCODE_CITY)){
					userExtends  = user.getUserExtends().get(UserExtends.USER_EXTENDCODE_CITY);
					userExtends.setExValue(city);
				}else{
					userExtends  = new UserExtends(UserExtends.USER_EXTENDCODE_CITY,city,user);
				}
						
				user.getUserExtends().put(UserExtends.USER_EXTENDCODE_CITY, userExtends);
				
				if(null != user.getUserExtends().get(UserExtends.USER_EXTENDCODE_MENUSTYLE)){
					userExtends  = user.getUserExtends().get(UserExtends.USER_EXTENDCODE_MENUSTYLE);
					userExtends.setExValue(menustyle);
				}else{
					userExtends  = new UserExtends(UserExtends.USER_EXTENDCODE_MENUSTYLE,menustyle,user);
				}
				
				user.getUserExtends().put(UserExtends.USER_EXTENDCODE_MENUSTYLE, userExtends);
				
				//user.setUserExtends(userExtends);
				
				userService.saveOrUpdate(user);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"用户修改个人设置：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "设置成功！");
				map.put("navTabId", "RES_ORG_USER");
			}else if ("unitSetting".equals(act)) {//修改学习中心联系信息
				User user 	        = userService.get(resourceid);
				OrgUnit unit   	    = null;
				String principal    = ExStringUtils.trimToEmpty(request.getParameter("principal"));//负责人
				String linkman		= ExStringUtils.trimToEmpty(request.getParameter("linkman"));//联系人
				String contectCall  = ExStringUtils.trimToEmpty(request.getParameter("contectCall"));//联系电话
				String localCity    = ExStringUtils.trimToEmpty(request.getParameter("localCity"));//所属城市
				String zipcode      = ExStringUtils.trimToEmpty(request.getParameter("zipcode"));//邮编
				String email        = ExStringUtils.trimToEmpty(request.getParameter("email"));//邮件
				String address      = ExStringUtils.trimToEmpty(request.getParameter("address"));//地址
				
				if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
					unit 		    = user.getOrgUnit();
				}
				if (null!=unit) {
					
					if (ExStringUtils.isNotEmpty(principal)) {
						unit.setPrincipal(principal);
					}
					if (ExStringUtils.isNotEmpty(linkman)) {
						unit.setLinkman(linkman);
					}
					if (ExStringUtils.isNotEmpty(contectCall)) {
						unit.setContectCall(contectCall);
					}
					if (ExStringUtils.isNotEmpty(localCity)) {
						unit.setLocalCity(localCity);
					}
					if (ExStringUtils.isNotEmpty(zipcode)) {
						unit.setZipcode(zipcode);
					}
					if (ExStringUtils.isNotEmpty(email)) {
						unit.setEmail(email);
					}
					if (ExStringUtils.isNotEmpty(address)) {
						unit.setAddress(address);
					}
					
					orgUnitService.update(unit);
					
					map.put("statusCode", 200);
					map.put("message", "设置成功！");
					map.put("navTabId", "RES_ORG_USER");	
				}else {
					throw new WebException("非法设置操作，当前用户没有权限！");
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"用户修改学习中心联系信息：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			}
		} catch (Exception e) {
			logger.error("用户设置出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置出错<br/>"+e.getMessage());
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
	@RequestMapping("/edu3/framework/system/org/user/validateCode.html")
	public void validateCode(String resourceid,String username,HttpServletRequest request,HttpServletResponse response) throws WebException{
		String msg = "";
		if(ExStringUtils.isNotBlank(username)){
			User user = userService.findUniqueByProperty("username", username);
			if(null != user && !user.getResourceid().equals(resourceid)){
				msg = "exist";
			}
		}			
		renderText(response, msg);
	}
	
	/**
	 * 激活、停用账号
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/system/org/user/updateuserstatus.html")
	public void updateUserStatus(String resourceid,String statusCode,String fwPage,HttpServletRequest request,HttpServletResponse response) throws WebException{		
		Map<String ,Object> map = new HashMap<String, Object>();		
		try {
			if(StringUtils.isNotEmpty(resourceid)){
				String[] resourceids = resourceid.split(",");
				for(String id : resourceids){
					 User user = userService.get(id);
					 user.setEnabledChar(statusCode); 
					 userService.saveOrUpdate(user);
		    	}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"激活/停用用户：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				//设置跳转页面
				if("1".equals(fwPage)){//招生
					//map.put("forward", request.getContextPath()+"/edu3/recruit/recruitmanage/brschoolaccount-status.html");
					map.put("localArea", "_brschoolAccoutListContent");
				}else if("2".equals(fwPage)){//教师
					map.put("forward", request.getContextPath()	+ "/edu3/teaching/edumanager/edumanager-list.html");
				}else if("0".equals(fwPage)){//系统管理
					//map.put("forward", request.getContextPath()+"/edu3/system/org/user/list.html");
					map.put("localArea", "_userListContent");
				}
				
			}
		} catch (Exception e) {
			logger.error("招生账户操作出错",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 重置用户密码
	 */
	@RequestMapping("/edu3/framework/system/org/user/resetUserPassword.html")
	public void resetUserPassword(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{		
		Map<String ,Object> map = new HashMap<String, Object>();		
		try {
			if(StringUtils.isNotEmpty(resourceid)){
				String[] resourceids = resourceid.split(",");
				String initPassword = CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue();
				
				userService.resetUserPassword(resourceids, initPassword);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "8", UserOperationLogs.UPDATE,"重置用户密码：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				
				//设置跳转页面
				map.put("localArea", "_userListContent");
				map.put("statusCode", 200);
				map.put("message", "操作成功！密码已经重置为初始默认密码 <font color='red'>"+initPassword+"</font>");	
			}
		} catch (Exception e) {
			logger.error("重置密码出错",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "重置密码发生错误:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 更新用户登录信息
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/system/org/user/updateinfo.html")
	public void updateUserLoginInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();		
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			//User user2 = userService.get(user.getResourceid());
			final String updateSql = " update hnjk_sys_users set LASTLOGINTIME = :lastloginTime,LOGINIP=:loginIp,FROMNET=:fromNet where resourceid = :userId";
			user.setLoginIp(user.getLoginIp());
			user.setFromNet(user.getFromNet());
			user.setLastLoginTime(new Date());
			
			CacheSecManager.WAIT_UPDATE_QUEUE.add(user);
						
			if(CacheSecManager.WAIT_UPDATE_QUEUE.size()>50){				//List<User> list = new ArrayList<User>();	
				int results = 0;
				for ( User u : CacheSecManager.WAIT_UPDATE_QUEUE) {
					//list.add(u);
					Map<String,Object> parameters = new HashMap<String, Object>();
					parameters.put("lastloginTime", new Date());
					parameters.put("loginIp",u.getLoginIp());
					parameters.put("fromNet", u.getFromNet());
					parameters.put("userId",u.getResourceid());
					
					
					results+= baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(updateSql, parameters);
				}
				//userService.batchSaveOrUpdate(list);
				logger.info("update user last login info:{}"+results);
				//list.clear();
				CacheSecManager.WAIT_UPDATE_QUEUE.clear();
			}
			
			map.put("status", "ok");
			
		} catch (Exception e) {
			e.printStackTrace();
			map.put("status", "notok");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 站点信息管理
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/brschool/edit.html")
	public String editBrSchool(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		User user = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit = null;
		if(user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
			unit = orgUnitService.load(user.getOrgUnit().getResourceid());	
			model.addAttribute("orgunit",unit);
		} 	
		return "/system/org/brschool-form";
	}
	/**
	 * 站点信息保存
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/system/org/brschool/save.html")
	public void saveBrSchool(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String, Object> map = new HashMap<String, Object>();
		User user 	        = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit   	    = null;
		String address      = ExStringUtils.trimToEmpty(request.getParameter("address"));//站点地址
		String principal    = ExStringUtils.trimToEmpty(request.getParameter("principal"));//负责人姓名
		String contectCall  = ExStringUtils.trimToEmpty(request.getParameter("contectCall"));//负责人电话
		String teachingMan	= ExStringUtils.trimToEmpty(request.getParameter("teachingMan"));//教务员姓名
		String teachingTel	= ExStringUtils.trimToEmpty(request.getParameter("teachingTel"));//教务员电话
		String teachingEmail= ExStringUtils.trimToEmpty(request.getParameter("teachingEmail"));//教务员e-mail
		String teachingQQ	= ExStringUtils.trimToEmpty(request.getParameter("teachingQQ"));//教务员QQ
		String majorDirector= ExStringUtils.trimToEmpty(request.getParameter("majorDirector"));//专业主任信息
				
		try {
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
				unit 		    = user.getOrgUnit();
			}
			if (null!=unit) {			
				if (ExStringUtils.isNotEmpty(principal)) {
					unit.setPrincipal(principal);
				}
				if (ExStringUtils.isNotEmpty(contectCall)) {
					unit.setContectCall(contectCall);
				}
				if (ExStringUtils.isNotEmpty(address)) {
					unit.setAddress(address);
				}			
				
				String[] exCodes = {OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGMAN,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGTEL,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGEMAIL,OrgUnitExtends.UNIT_EXTENDCODE_TEACHINGQQ,OrgUnitExtends.UNIT_EXTENDCODE_MAJORDIRECTOR};
				String[] exValues = {teachingMan,teachingTel,teachingEmail,teachingQQ,majorDirector};
				
				OrgUnitExtends unitExtends = null;	
				for (int i = 0; i < exCodes.length; i++) {
					if(null != unit.getOrgUnitExtends().get(exCodes[i])){
						unitExtends  = unit.getOrgUnitExtends().get(exCodes[i]);
						unitExtends.setExValue(exValues[i]);
					}else{
						unitExtends  = new OrgUnitExtends(exCodes[i], exValues[i], unit);
					}				
					unit.getOrgUnitExtends().put(exCodes[i], unitExtends);
				}
				
				orgUnitService.update(unit);			
				map.put("statusCode", 200);	
				map.put("message", "设置站点信息成功");
			}else {
				throw new WebException("非法设置操作，当前用户没有权限！");
			}
		} catch (Exception e) {
			logger.error("设置站点信息出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置站点信息失败:"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
