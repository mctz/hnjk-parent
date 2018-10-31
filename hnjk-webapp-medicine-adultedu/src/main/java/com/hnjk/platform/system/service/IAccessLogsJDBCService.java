package com.hnjk.platform.system.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;

/**
 * 日志统计服务接口.
 * <code>IAccessLogsJDBCService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-6-20 下午03:00:03
 * @see 
 * @version 1.0
 */
public interface IAccessLogsJDBCService {

	/**
	 * 访问情况统计
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> statAccessLogsStatus(Map<String, Object> condition) throws ServiceException;
	/**
	 * 访问结果分析
	 * @param type serverstatus、clientbrowser、clientos
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> statAccessLogsResults(String type) throws ServiceException;
	/**
	 * 日志总记录数
	 * @return
	 * @throws ServiceException
	 */
	Double statAccessLogsResultsTotal() throws ServiceException;
	/**
	 * 访问排行
	 * @param type 1-并发访问次数、2-并发访问用户数、3-访问最多的资源、4-处理最慢的资源、5-错误做多的资源
	 * @return
	 * @throws ServiceException
	 */
	List<Map<String, Object>> statAccessLogsTop(int type) throws ServiceException;
}
