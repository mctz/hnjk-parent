package com.hnjk.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.ui.WebAuthenticationDetails;
import org.springframework.security.userdetails.UserDetails;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
import com.hnjk.security.service.impl.OrgUnitServiceImpl;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;


/**
 * 提供sprig security的辅助类. <code>SpringSecurityHelper</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-12-2 上午11:09:05
 * @see 
 * @version 1.0
 */
public class SpringSecurityHelper {		
	
	/**
	 * 根据用户ID拿系统用户对象
	 * @param userId
	 * @return
	 */
	public static User getUserByUserId(String userId){
		User user = null;
		//从缓存中拿用户
		user = (User)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).get(userId);
		if(null == user || null == user.getOrgUnit() || null == user.getRoles()){//从库中取出用户
			IUserService userService = (IUserService)SpringContextHolder.getBean("userService");//获取用户服务接口
			user = userService.get(userId);
		}
		return user;
	}
	
	/**
	 * 取得当前用户的登录名, 如果当前用户未登录则返回空字符串.
	 */
	public static String getCurrentUserName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();		
		if (auth == null) {
			return "";
		}
		return auth.getName();
	}

	/**
	 * 取得当前用户, 返回值为User类, 如果当前用户未登录则返回null.
	 */	
	public static User getCurrentUser() {
		Authentication principal = SecurityContextHolder.getContext().getAuthentication();
		
		if ( null == principal){
			return null;
		}
		// 更改为从缓存获取
		User user = (User)principal.getPrincipal();

		GrantedAuthority[] authorities = user.getAuthorities();
		Set<Role> roles = user.getRoles();
		//如果用户扩展为空，则再加载一次
		try {
			if(null == user.getUserExtends()){
				IUserService userService = (IUserService)SpringContextHolder.getBean("userService");
				user = userService.get(user.getResourceid());
				user.setAuthorities(authorities);
				user.setRoles(roles);
				System.out.println("重新载入用户扩展属性...");
			}			
		} catch (Exception e) {
			if(null == user.getUserExtends() || user.getUserExtends().isEmpty()){
				IUserService userService = (IUserService)SpringContextHolder.getBean("userService");
				user = userService.get(user.getResourceid());
				user.setAuthorities(authorities);
				user.setRoles(roles);
				System.out.println("重新载入用户扩展属性......");
			}
		}
		
		if(null == user.getOrgUnit()){
			OrgUnit orgUnit = OrgUnitServiceImpl.getOrgUnitFromCache(user.getUnitId());
			if(null != orgUnit && ExStringUtils.isNotEmpty(orgUnit.getResourceid()) ){
				user.setOrgUnit(orgUnit);
			}else{
				if(ExStringUtils.isNotEmpty(user.getUnitId())){
					IOrgUnitService orgUnitService = (IOrgUnitService)SpringContextHolder.getBean("orgUnitService");
					OrgUnit org = orgUnitService.get(user.getUnitId());
					user.setOrgUnit(org);
					EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_DEPT).put(user.getResourceid(), user.getOrgUnit());
				}
			}
		}
		
//		if(ExStringUtils.isNotEmpty(user.getUnitId())){
//			user.setOrgUnit(OrgUnitServiceImpl.getOrgUnitFromCache(user.getUnitId()));//从缓存中获取用户的组织单位，如果如果为空，则从数据库中加载
//			if(null == user.getOrgUnit() | ExStringUtils.isEmpty(user.getOrgUnit().getResourceid())){
//				IOrgUnitService orgUnitService = (IOrgUnitService)SpringContextHolder.getBean("orgUnitService");
//				OrgUnit orgUnit = orgUnitService.get(user.getUnitId());
//				user.setOrgUnit(orgUnit);
//				OrgUnitServiceImpl.addOrgUnitToCache(orgUnit);
//				EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_DEPT).put(user.getResourceid(), user.getOrgUnit());
//				System.out.println("重新载入用户组织属性...");
//			}
//		}
		
		
		return user;
	}
	
	/**
	 * 将UserDetails保存到Security Context.
	 * 
	 * @param userDetails 已初始化好的用户信息.
	 * @param request 用于获取用户IP地址信息.
	 */
	public static void saveUserDetailsToContext(UserDetails userDetails, HttpServletRequest request) {
		PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		authentication.setDetails(new WebAuthenticationDetails(request));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	/**
	 * 判断当前用户是否不在指定角色中
	 * @param roleCodes
	 * @return
	 */
	public static boolean isUserNotInRoles(String ...roleCodes){
		User user  = getCurrentUser();
		//获取用户角色
		List<Role> roleList = new ArrayList<Role>(user.getRoles());		
		for (String roleCode : roleCodes) {
			for(Role role:roleList){
				if(role.getRoleCode().indexOf(roleCode) >=0){
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 判断当前用户的角色是否只在给定范围内
	 * @param roleCodes
	 * @return
	 */
	public static boolean isUserOnlyInRoles(String ...roleCodes) {
		User user  = getCurrentUser();
		//List<String> roleCodeList = Arrays.asList(roleCodes);
		//获取用户角色
		boolean bool = false;
		List<String> roleCodeList = Arrays.asList(roleCodes);
		List<Role> roleList = new ArrayList<Role>(user.getRoles());
		for(Role role:roleList){
			if(roleCodeList.contains(role.getRoleCode())){
				bool = true;
			}else {
				bool = false;
				break;
			}
		}
		return bool;
	}
	
	private static boolean isContainsPrefix(String[] roleCodes, String roleCode) {
		// TODO Auto-generated method stub
		for (String string : roleCodes) {
			if(roleCode.indexOf(string) >=0){
				return true;
			}
		}
		return false;
	}

	public static boolean isUserInRole(String roleCode){
		User user  = getCurrentUser();
		//获取用户角色
		List<Role> roleList = new ArrayList<Role>(user.getRoles());		
		for(Role role:roleList){
			if(role.getRoleCode().indexOf(roleCode) >=0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断当前用户是否包含这些角色
	 * @param roleCodes
	 * @return
	 */
	public static boolean isUserInRoles(String... roleCodes){
		User user  = getCurrentUser();
		//获取用户角色
		List<Role> roleList = new ArrayList<Role>(user.getRoles());	
		List<String> roleCodeList = Arrays.asList(roleCodes);
		for(Role role:roleList){
			if(roleCodeList.contains(role.getRoleCode())){
				return true;
			}
		}
		return false;
	}
	/**
	 * 判断一个用户是否是教学中心老师
	 * @param user
	 * @return
	 */
	public static boolean isTeachingCentreTeacher(User user){
		if(user != null && user.getOrgUnit() != null 
				&& "DEPT_TEACHING_CENTRE".equals(user.getOrgUnit().getUnitCode())){
			return true;
		}else {
			return false;
		}
	}

	public static boolean isStudent() {
		User user  = getCurrentUser();
		//获取用户角色
		List<Role> roleList = new ArrayList<Role>(user.getRoles());
		for(Role role:roleList){
			if(role.getRoleCode().indexOf("ROLE_STUDENT") >=0){
				return true;
			}
		}
		return false;
	}
}
