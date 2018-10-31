package com.hnjk.platform.system.cache;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.ContextLoader;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.ISensitiveWordService;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.security.cache.CacheSecManager;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IResourceService;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;

/**
 * 初始化缓存抽象服务实现.
 * @author hzg
 *
 */
@Transactional
public abstract class AbstractInitAppDataServiceImpl implements IinitAppDataService{

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;//注入字典服务
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;//注入系统全局配置服务
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;//注入角色服务
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;//注入用户服务
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;//注入组织服务
	
	@Autowired
	@Qualifier("resourceService")
	private IResourceService resourceService;//注入资源服务
	
	@Qualifier("sensitiveWordService")
	@Autowired
	private ISensitiveWordService sensitiveWordService;// 注入敏感词库
	
	
	@Override
	public abstract void doInitAppData() ;

	
	@Override
	public abstract void reloadResource();


	/*
	 * 加载数据字典
	 */
	public void initDictionary(){		
		List<Dictionary> dictionarys = dictionaryService.findByHql("from "+Dictionary.class.getSimpleName()+" where isDeleted = ? order by showOrder asc", 0);
		//for(Dictionary dictionary : dictionarys){			
			EhCacheManager.getCache(CacheAppManager.CACHE_SYS_DICTIONS).put(CacheAppManager.CACHE_SYS_DICTIONS, dictionarys);
		//}
		logger.info("加载数据字典：{}条...",dictionarys.size());
	}
	
	/*
	 * 加载系统配置参数
	 */
	public void initSysConfiguration(){
		List<SysConfiguration> sysConfigs = sysConfigurationService.getAllBycriteria();
		for(SysConfiguration sysConfiguration : sysConfigs){
			EhCacheManager.getCache(CacheAppManager.CACHE_APP_CONFIG).put(sysConfiguration.getParamCode(), sysConfiguration);//key-paramcode 
		}
		logger.info("加载系统全局参数：{}条...",sysConfigs.size());
	}
	
	/*
	 * 初始化并缓存系统资源，使用key - value方式，resourceid - object
	 * {@link ResourceServiceImpl}中的增、删、改需要同步这个缓存
	 *
	 */	
	public void initResource() {		
		try {
			//List<Resource> resList = resourceService.findResourceCatalog(null,ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
			Map<String, Object> condition = new HashMap<String, Object>(5);
			condition.put("code", ConfigPropertyUtil.getInstance().getProperty("web.security.gloabResourceCode"));
			List<Resource> resList = resourceService.findResourceTree(condition);
			EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).put(CacheSecManager.CACHE_SEC_RESOURCE, resList);
		
			logger.info("加载资源:"+resList.size()+"条.");
		} catch (Exception e) {
			
			e.printStackTrace();
		}		
		
	}
	
	
	/**
	 * 1.缓存资源对应角色编码
	 * 2.缓存角色对象
	 * 缓存同步位置:RoleRightController.java -> saveRoleResourceRef 方法
	 * key:resourcepath
	 * value:rolecode
	 */
	public void initResourceRoleRef() {
		List<Role> roles = roleService.getAll();
		List<Resource> resourceList =  (List<Resource>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_RESOURCE).get(CacheSecManager.CACHE_SEC_RESOURCE);
		//将角色-资源关系存入缓存，为了方便调用，使用Map存入
		Map<String, Set<String>> resRoleMap = new HashMap<String, Set<String>>(1000);
				
		for(Resource res : resourceList){
			resRoleMap.put(res.getResourcePath(),new HashSet<String>());				
		}
		if(null != roles && roles.size()>0){
			for(Role role : roles) {
				String value = role.getRoleCode();
				for(Resource resource : role.getAuthoritys()) {
					String key = resource.getResourcePath();
					if(key == null || "".equalsIgnoreCase(key)) {
						continue;
					}
					
					if(resRoleMap.containsKey(key)){//如果资源-角色映射中，存在该角色的授权资源，则将角色加入到映射中
						resRoleMap.get(key).add(value);
					}
					
					/*
					if(null == resRoleMap.get(key)) {//若没有角色						
						resRoleMap.put(key, roleSet);
					}else {
						if(!resRoleMap.get(key).equals(value)) {//如果没有，则添加
							resRoleMap.put(key, resRoleMap.get(key).concat(",").concat(value));
						}
					}
					*/
				}
				EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLES).put(role.getResourceid(),role);	// 将角色放入缓存
			}
			EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ROLE_RESOURCE).put(CacheSecManager.CACHE_SEC_ROLE_RESOURCE,resRoleMap);
			logger.info("加载系统角色："+roles.size()+"条,资源-角色映射："+resRoleMap.size()+"条...");
		}
	}
	
	/**
	 * 加载用户 | 用户关联部门 | 用户关联角色缓存信息
	 * 1.缓存用户信息
	 * 2.缓存用户部门
	 * 3.缓存用户组织
	 */	
	public void initOrgsAndUsers() {	
		List<User> users = userService.findByCriteria(
				Restrictions.eq("isDeleted",0),//删除标示
				//Restrictions.eq("accountNonExpired", true),//没被禁用的用户
				Restrictions.eq("userType",CacheAppManager.getSysConfigurationByCode("sysuser.usertype.edumanager").getParamValue())//只缓存老师教务管理人员
				);

		if(null != users && users.size()>0){
			for(User user : users) {
				try {
					EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_DEPT).put(user.getResourceid(),user.getOrgUnit());//放入组织与用户关系缓存中
					user.getOrgUnit().getUnitCode();
				}catch(Exception ex) {
					EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_DEPT).put(user.getResourceid(),null);
				}
				
				
				EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USER_ROLE).put(user.getResourceid(),user.getRoles());
				EhCacheManager.getCache(CacheSecManager.CACHE_SEC_USERS).put(user.getResourceid(), user);	// 将用户放入缓存
			
			}
			logger.info("加载系统用户"+users.size()+"个...");
		}			
		//缓存组织
		List<OrgUnit> orgs = orgUnitService.findByHql("from "+OrgUnit.class.getSimpleName()+" where isDeleted = ? order by unitName", 0);		
		if(null != orgs && orgs.size()>0){
			//for(OrgUnit org : orgs) {//改为缓存LIST，单个缓存，容易丢失对象信息
			EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).put(CacheSecManager.CACHE_SEC_ORGS, orgs);	
			//}
			logger.info("加载组织单位"+orgs.size()+"条...");
		}
		
	}
	
	/**
	 * 加载敏感词库
	 * @param request
	 */
	public void initSensitiveWord() {	
		ServletContext application = ContextLoader.getCurrentWebApplicationContext().getServletContext();
		// 将敏感词库放到内存中
		SensitivewordFilter sensitivewordFilter = sensitiveWordService.getSensitivewordFilter();
		application.setAttribute("sensitivewordFilter", sensitivewordFilter);
		// 将敏感词最新更新时间放在内存中
	/*	String lastUpdateStr = sensitiveWordService.getLastUpateDate();
		application.setAttribute("SW_lastUpdate", lastUpdateStr);*/
	}
}
