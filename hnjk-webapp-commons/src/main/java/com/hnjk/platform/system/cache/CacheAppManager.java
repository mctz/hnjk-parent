package com.hnjk.platform.system.cache;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.support.context.Constants;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;

@Component
public class CacheAppManager implements InitializingBean{
	/**字典缓存名*/
	public final static String CACHE_SYS_DICTIONS = "CACHE_APP_DICT";
	/**门户栏目缓存名*/
    //public final static String CACHE_PORTAL_CHANNEL = "CACHE_APP_CHANNEL";
    /**门户通知文章缓存*/
	//public final static String CACHE_PORTAL_ARTICLE = "CACHE_APP_ARTICLE";
	/**系统配置参数*/
	public final static String CACHE_APP_CONFIG = "CACHE_APP_CONFIG";
	/**在线帮助栏目缓存名*/
	//public final static String CACHE_HELP_CHANNEL = "CACHE_APP_HELPCHANNEL";
	/**在线帮助文章缓存名*/
	//public final static String CACHE_HELP_ARTICLE = "CACHE_APP_HELPARTICLE";
	
	/** 用户操作日志临时缓存 */
	public final static String CACHE_APP_USEROPERATIONLOGS = "CACHE_APP_USEROPERATIONLOGS";
	
	@Autowired	
	private IinitAppDataService initAppDataService;
	
	
	
	/**
	 * 执行初始化数据.
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		//initAppDataServiceImpl.initDictionary();
		//initAppDataServiceImpl.initSysConfiguration();
		initAppDataService.doInitAppData();
//		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		//获取学校代码，根据学校代码来判断是否启动socketServer
		SysConfiguration sys=getSysConfigurationByCode("graduateData.schoolCode");
		String schoolCodeValue = sys.getParamValue();
		if(ExStringUtils.isNotBlank(schoolCodeValue) && "10571".equals(schoolCodeValue)){
			initAppDataService.startNettyServer();
		}
		
		//initAppDataServiceImpl.initResource();
		///initAppDataServiceImpl.initResourceRoleRef();
		//initAppDataServiceImpl.initOrgsAndUsers();
	}
	
	/**
	 * 数据字典
	 * 暂时不支持parentId 来查询
	 * 先用 parentCode返回下面的子
	 */	
	public static List<Dictionary> getChildren(String parentIdOrCode){			
		@SuppressWarnings("unchecked")List<Dictionary> all = (List<Dictionary>)EhCacheManager.getCache(CACHE_SYS_DICTIONS).get(CACHE_SYS_DICTIONS);		
//		if(parentIdOrCode.startsWith("RES")) {		
		if(null != all){			
			for(Dictionary r : all) {
				if(null != r && ExStringUtils.isNotEmpty(r.getDictCode()) && 
						r.getDictCode().equalsIgnoreCase(parentIdOrCode)) {
					parentIdOrCode = r.getResourceid();
					break;
				}
			}
		}
	
//		}
		// 通过parentid获取
		List<Dictionary> result = new ArrayList<Dictionary>();
		for(Dictionary r : all) {		
			if(null != r && null != r.getParentDict() ) {				
				r.getParentDict().getResourceid();
				if( r.getParentDict().getResourceid().equalsIgnoreCase(parentIdOrCode) && 
						r.getIsDeleted() == Constants.BOOLEAN_FALSE){
					if(ExStringUtils.isNotEmpty(r.getIsUsed()) && Constants.BOOLEAN_NO.equals(r.getIsUsed())){//排除禁用的
						continue;
					}
					result.add(r);
				}
				
			}
		}
		return result;
	}
		
	/**
	 * 根据字典编码拿字典
	 * @param dictCode
	 * @return
	 */	
	public static Dictionary getDictionaryByCode(String dictCode) {
		@SuppressWarnings("unchecked")List<Dictionary> all = (List<Dictionary>)EhCacheManager.getCache(CACHE_SYS_DICTIONS).get(CACHE_SYS_DICTIONS);
		if( null != all ){
			for(Dictionary dict : all) {
				if(!StringUtils.isEmpty(dict.getDictCode()) && dict.getDictCode().equalsIgnoreCase(dictCode)) {
					return dict;
				}
			}
		}
		return null;
	}
	
	/**
	 * 根据参数编码拿参数
	 * @param paramCode
	 * @return
	 */
	public static SysConfiguration getSysConfigurationByCode(String paramCode){	
		SysConfiguration config = EhCacheManager.getCache(CacheAppManager.CACHE_APP_CONFIG).get(paramCode, SysConfiguration.class);
		if (config == null) {
			throw new NullPointerException("无法获取全局参数！【"+paramCode+"】");
		}
		return config;
	}
		
}
