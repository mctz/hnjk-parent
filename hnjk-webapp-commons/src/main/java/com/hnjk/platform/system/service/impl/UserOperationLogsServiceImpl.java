package com.hnjk.platform.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.cache.EhCacheManager;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.model.IBaseModel;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IUserOperationLogsService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 用户操作日志服务接口实现.
 * <code>UserOperationLogsServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-8-15 下午04:27:27
 * @see 
 * @version 1.0
 */
@Transactional
@Service("userOperationLogsService")
public class UserOperationLogsServiceImpl extends BaseServiceImpl<UserOperationLogs> implements IUserOperationLogsService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public void saveUserOperationLogs(String ipaddress, String modules, String operationType, String operationContent)	throws ServiceException {
		User user = SpringSecurityHelper.getCurrentUser();
		UserOperationLogs log = new UserOperationLogs(ipaddress, user==null?"":user.getResourceid(), user==null?"":user.getCnName(), modules, operationType, operationContent, "0", new Date());
		addUserOperationLogsToCache(log);		
	}
	
	@Override
	public void saveUserOperationLogs(String ipaddress, String modules, String operationType, List<? extends IBaseModel> entityList) throws ServiceException {
		if(ExCollectionUtils.isNotEmpty(entityList)){
			//内容格式：资源名称(className)+资源ids(resourceid)
			String operationContent = entityList.get(0).getClass().getSimpleName()+": ";
			try {
				operationContent += ExCollectionUtils.fetchPropertyToString(entityList, "resourceid", ",");
			} catch (Exception e) {
			}
			saveUserOperationLogs(ipaddress, modules, operationType, operationContent);
		}		
	}

	@Override
	public void saveUserOperationLogs(String ipaddress, String modules, String operationType, IBaseModel entity) throws ServiceException {
		//内容格式：资源名称(className)+资源id(resourceid)
		String operationContent = entity.getClass().getSimpleName()+": "+entity.getResourceid();
		saveUserOperationLogs(ipaddress, modules, operationType, operationContent);
	}	
	
	private void addUserOperationLogsToCache(UserOperationLogs log){
			
		Set<UserOperationLogs> userOperationLogsSet = (Set<UserOperationLogs>) EhCacheManager.getCache(CacheAppManager.CACHE_APP_USEROPERATIONLOGS).get(CacheAppManager.CACHE_APP_USEROPERATIONLOGS);
		
		if(userOperationLogsSet==null){
			userOperationLogsSet = new LinkedHashSet<UserOperationLogs>(100);
			EhCacheManager.getCache(CacheAppManager.CACHE_APP_USEROPERATIONLOGS).put(CacheAppManager.CACHE_APP_USEROPERATIONLOGS,userOperationLogsSet);
		}
		userOperationLogsSet.add(log);
		
//		ConfigPropertyUtil.getInstance().getProperty("useroperationlogs.updatesize")
		Integer updateSize = Integer.valueOf(ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(CacheAppManager.getSysConfigurationByCode("useroperationlogs.updatesize").getParamValue()), "100"));
		if(userOperationLogsSet.size()>updateSize){//达到阈值时，批量更新
//			String sql = "insert into edu_sys_operatelogs (resourceid,ipaddress,userid,username,modules,operationtype,operationcontent,exportflag,recordtime,isdeleted,version) values(:resourceid,:ipaddress,:userId,:userName,:modules,:operationType,:operationContent,:exportFlag,:recordTime,:isDeleted,:version)";
			String sql = "insert into edu_sys_operatelogs (resourceid,ipaddress,userid,username,modules,operationtype,operationcontent,exportflag,recordtime) values(:resourceid,:ipaddress,:userId,:userName,:modules,:operationType,:operationContent,:exportFlag,:recordTime)";
			List<Map<String, Object>> paramList = new ArrayList<Map<String,Object>>();
			GUIDUtils.init();
			for (UserOperationLogs u : userOperationLogsSet) {
				Map<String, Object> parameters = new HashMap<String, Object>();
				parameters.put("resourceid", GUIDUtils.buildMd5GUID(false));
				parameters.put("ipaddress",u.getIpaddress());
				parameters.put("userId", u.getUserId());
				parameters.put("userName",u.getUserName());
				parameters.put("modules",u.getModules());
				parameters.put("operationType",u.getOperationType());
				parameters.put("operationContent",u.getOperationContent());
				parameters.put("exportFlag",u.getExportFlag());
				parameters.put("recordTime",u.getRecordTime());
				/*parameters.put("isDeleted",0);
				parameters.put("version",0);//使用0L后台报错
*/				paramList.add(parameters);
			}
			baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(sql, paramList.toArray(new Map[paramList.size()]));
			userOperationLogsSet.clear();
//			EhCacheManager.getCache(CacheAppManager.CACHE_APP_USEROPERATIONLOGS).remove(CacheAppManager.CACHE_APP_USEROPERATIONLOGS);
		}
	}

}
