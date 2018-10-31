package com.hnjk.platform.system.service;

import java.util.List;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.model.IBaseModel;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.UserOperationLogs;
/**
 * 用户操作日志服务接口.
 * <code>IUserOperationLogsService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-8-15 下午03:53:20
 * @see 
 * @version 1.0
 */
public interface IUserOperationLogsService extends IBaseService<UserOperationLogs> {
	/**
	 * 记录操作日志
	 * @param ipaddress ip
	 * @param url 资源
	 * @param operationType 操作类型
	 * @param operationContent 操作内容
	 * @throws ServiceException
	 */
	void saveUserOperationLogs(String ipaddress, String url, String operationType, String operationContent) throws ServiceException;
	/**
	 * 记录操作日志 - 批量操作,内容格式：资源名称(className)+资源ids(resourceid)
	 * @param ipaddress ip
	 * @param url 资源
	 * @param operationType 操作类型
	 * @param entityList 实体列表
	 * @throws ServiceException
	 */
	void saveUserOperationLogs(String ipaddress, String url, String operationType, List<? extends IBaseModel> entityList) throws ServiceException;
	/**
	 * 记录操作日志 - 单个实体操作,内容格式：资源名称(className)+资源id(resourceid)
	 * @param ipaddress ip
	 * @param url 资源
	 * @param operationType 操作类型
	 * @param entity
	 * @throws ServiceException
	 */
	void saveUserOperationLogs(String ipaddress, String url, String operationType, IBaseModel entity) throws ServiceException;	
}
