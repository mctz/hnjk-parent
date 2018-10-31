package com.hnjk.security.cache;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;
import com.hnjk.security.service.impl.OrgUnitServiceImpl;

/**
 * 缓存统一管理
 * 
 * 开发人员定义的缓存名称，需要定义在这里
 * 
 * reload功能放在 CacheAppManager.java 中来实现
 * 
 * @author： dk
 * 
 * CACHE_SEC_USER_TOP_MENU_IDS		-->	用户有权限的一级菜单ID
 * CACHE_SEC_USER_RES_IDS			--> 用户有权限的资源ID
 * 
 * CACHE_RESOURCE					--> 所有资源对象缓存
 * CACHE_SEC_USERS					--> 用户信息缓存,由于用户信息查询，数据量非常庞大，多达几万条数据，并且使用非常频繁，很多服务，子系统都会频繁查询用户信息，
 * 									甚至批量查询用户的信息(包含角色，组织)，这个时候如果从数据库查询，web以及
 * 									数据库性能都将会很低，有可能导致其他功能无法正常使用，因此改用从内存来读取的方式
 * 									需要开发人员严格控制 增删改 三个动作 应该从统一入口(已经做好同步)来操作
 * CACHE_ORGS						--> 看上面注释
 * CACHE_ROLES						--> 看上面注释
 * 
 * CACHE_SEC_USER_DEPT				--> 用户跟部门的对应关系,一个用户对应的一个部门
 * CACHE_SEC_USER_ROLE				--> 用户跟角色的对应关系,一个用户对应多个角色
 * 
 */

public class CacheSecManager {
	protected Logger logger = LoggerFactory.getLogger(getClass());
		
	/** 所有资源对象缓存 格式:&lt;key:Resourceid,value:Resource&gt;*/
	public static final String CACHE_SEC_RESOURCE = "CACHE_SEC_RESOURCE";
	/** 系统里用户对象缓存 格式:&lt;key:Resourceid,value:User&gt;*/
	public static final String CACHE_SEC_USERS = "CACHE_SEC_USERS";
	/** 系统里组织对象缓存 格式:&lt;key:Resourceid,value:OrgUnit&gt;*/
	public static final String CACHE_SEC_ORGS = "CACHE_SEC_ORGS";
	/** 系统里角色对象缓存 格式:&lt;key:Resourceid,value:Role&gt;*/
	public static final String CACHE_SEC_ROLES = "CACHE_SEC_ROLES";
	/** 用户对应的组织缓存 格式:&lt;key:UserResourceid,value:OrgUnit&gt;*/
	public static final String CACHE_SEC_USER_DEPT = "CACHE_SEC_USER_DEPT";
	/** 用户对应的角色缓存 格式:&lt;key:UserResourceid,value:Set&lt;Role&gt;&gt;*/
	public static final String CACHE_SEC_USER_ROLE = "CACHE_SEC_USER_ROLE";
	/**等待更新的用户*/
	public static final Set<User> WAIT_UPDATE_QUEUE = new LinkedHashSet<User>();
	
	/** 资源对应角色编码<url-roleCodes>*/
	public static final String CACHE_SEC_ROLE_RESOURCE = "CACHE_SEC_ROLE_RESOURCE";
	//public static Map<String,String> resRoleMap = new HashMap<String,String>();
	/** 用户信息(指定属性) 『当改用本地应用调远程服务的时候,需要一个UserInfo对象,所以在本地应用留了一份集合』 */
	//public static Map<String,UserAdaptor> userInfoMap = new HashMap<String,UserAdaptor>();
	
	
	/**
	 * 资源 : 通过 parentId 或者 parentCode返回下面的子
	 */
	@SuppressWarnings("unchecked")
	public static List<Resource> getChildren(String parentIdOrCode){
		List<Resource> all = (List<Resource>)EhCacheManager.getCache(CACHE_SEC_RESOURCE).get(CACHE_SEC_RESOURCE);
		if(parentIdOrCode.startsWith("RES")) {
			for(Resource r : all) {
				if(r.getResourceCode().equalsIgnoreCase(parentIdOrCode)) {
					parentIdOrCode = r.getResourceid();
					break;
				}
			}
		}
		// 通过parentid获取
		List<Resource> result = new ArrayList<Resource>();
		for(Resource r : all) {
			if(r.getParent() != null && r.getParent().getResourceid().equalsIgnoreCase(parentIdOrCode) && r.getIsDeleted() == Constants.BOOLEAN_FALSE) {
				result.add(r);
			}
		}
		return result;
	}
	
	public static Resource getResourceFromCache(String resourceid){		
		@SuppressWarnings("unchecked")
		List<Resource> all = (List<Resource>)EhCacheManager.getCache(CACHE_SEC_RESOURCE).get(CACHE_SEC_RESOURCE);
		for(Resource r : all){
			if(resourceid.equals(r.getResourceid())){
				return r;
			}
		}
		return null;
	}
	
	/**
	 * 根据用户ID获取用户对象
	 * @param userId
	 * @return
	 */
	public static User getUserFromCache(String userId){
		User user = (User)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).get(userId);
		if(null == user){
			IUserService userService = (IUserService)SpringContextHolder.getBean("userService");
			user = userService.get(userId);
			EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).put(userId, user);
		}
		return user;
	}
	
	/**
	 * @param type VALUE:[org|user|role]
	 * @param ids
	 * @return
	 */
	public static String ids2names(String type, String ids) {
		List<String> list = new ArrayList<String>();
		
		if(type.toLowerCase().startsWith("org")) {
			for(String id : ids.split(",")) {
				if(!StringUtils.isEmpty(id)) {
					OrgUnit org = OrgUnitServiceImpl.getOrgUnitFromCache(id);
					
					if(null == org){//如果缓存中没有，则从库中取
						IOrgUnitService orgUnitService = (IOrgUnitService)SpringContextHolder.getBean("orgUnitService");
						org = orgUnitService.get(id);
						OrgUnitServiceImpl.addOrgUnitToCache(org);						
					}
					if(org!=null) {
						list.add(org.getUnitName());
					}
				}
			}
		}else if(type.toLowerCase().startsWith("role")) {
			for(String id : ids.split(",")) {
				if(!StringUtils.isEmpty(id)) {
					Role role = (Role)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLES).get(id);
					if(null  == role){
						IRoleService roleService = (IRoleService)SpringContextHolder.getBean("roleService");
						role = roleService.get(id);
						EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLES).put(role.getResourceid(), role);
					}
					if(role!=null) {
						list.add(role.getRoleName());
					}
				}
			}
		}else	if(type.toLowerCase().startsWith("user")) {
			for(String id : ids.split(",")) {
				if(!StringUtils.isEmpty(id)) {
					User user = (User)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).get(id);
					if(null == user){
						IUserService userService = (IUserService)SpringContextHolder.getBean("userService");
						user = userService.get(id);
						EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).put(user.getResourceid(), user);
					}
					if(user!=null) {
						list.add(user.getCnName());
					}
				}
			}
		}
		return StringUtils.join(list,",");
	}
}
