package com.hnjk.platform.system;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hnjk.core.support.base.model.IBaseModel;
import com.hnjk.core.support.context.SpringContextHolder;
import com.hnjk.platform.system.service.IUserOperationLogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * 用户操作日志辅助工具
 * <code>UserOperationLogsHelper</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-8-16 下午04:08:40
 * @see 
 * @version 1.0
 */
public class UserOperationLogsHelper {
	protected static Logger logger = LoggerFactory.getLogger(UserOperationLogsHelper.class);

	private static IUserOperationLogsService userOperationLogsService = SpringContextHolder.getBean("userOperationLogsService");;
	
	/**
	 * 记录操作日志
	 * @param ipaddress ip
	 * @param url 资源
	 * @param operationType 操作类型
	 * @param operationContent 操作内容
	 */
	public static void saveUserOperationLogs(String ipaddress, String modules, String operationType, String operationContent) {
		try {
			//IUserOperationLogsService userOperationLogsService = (IUserOperationLogsService)SpringContextHolder.getBean("userOperationLogsService");
			userOperationLogsService.saveUserOperationLogs(ipaddress, modules, operationType, operationContent);
		} catch (Exception e) {
			logger.error("记录用户操作日志出错:{}", e.fillInStackTrace());
		}		
	}
	/**
	 * 记录操作日志 - 批量操作,内容格式：资源名称(className)+资源ids(resourceid)
	 * @param ipaddress ip
	 * @param url 资源
	 * @param operationType 操作类型
	 * @param entityList 实体列表
	 */
	public static void saveUserOperationLogs(String ipaddress, String modules, String operationType, List<? extends IBaseModel> entityList)	{
		try {
			//IUserOperationLogsService userOperationLogsService = (IUserOperationLogsService)SpringContextHolder.getBean("userOperationLogsService");
			userOperationLogsService.saveUserOperationLogs(ipaddress, modules, operationType, entityList);
		} catch (Exception e) {
			logger.error("记录用户操作日志出错:{}", e.fillInStackTrace());
		}
	}
	/**
	 * 记录操作日志 - 单个实体操作,内容格式：资源名称(className)+资源id(resourceid)
	 * @param ipaddress ip
	 * @param url 资源
	 * @param operationType 操作类型
	 * @param entity
	 */
	public static void saveUserOperationLogs(String ipaddress, String modules, String operationType, IBaseModel entity) {
		try {
			//IUserOperationLogsService userOperationLogsService = (IUserOperationLogsService)SpringContextHolder.getBean("userOperationLogsService");
			userOperationLogsService.saveUserOperationLogs(ipaddress, modules, operationType, entity);
		} catch (Exception e) {
			logger.error("记录用户操作日志出错:{}", e.fillInStackTrace());
		}
	}
}
