package com.hnjk.platform.system.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.AccessLogs;

import java.util.List;
import java.util.Map;

/**
 * 系统访问日志管理服务接口.
 * <code>IAccessLogsService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-6-16 下午04:20:08
 * @see 
 * @version 1.0
 */
public interface IAccessLogsService extends IBaseService<AccessLogs> {

	/**
	 * 解析并保存日志
	 * @param fileName
	 * @throws Exception
	 */
	void parseAndSaveAccessLogs(String fileName) throws Exception;
	/**
	 * 分页查询访问日志
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findAccessLogsByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	/**
	 * 日志文件
	 * @return List<String>
	 * @throws ServiceException
	 */
	List<String> accessLogsDate() throws ServiceException;
}
