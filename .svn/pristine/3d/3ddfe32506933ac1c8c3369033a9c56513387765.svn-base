package com.hnjk.platform.system.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.SysConfiguration;

/**
 * 系统参数配置接口.
 * <code>ISysConfigurationService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-8-26 上午10:53:22
 * @see 
 * @version 1.0
 */
public interface ISysConfigurationService extends IBaseService<SysConfiguration>{
	
	/**
	 * 保存并更新缓存
	 * @param sysConfiguration
	 * @throws ServiceException
	 */
	void saveSysConfiguration(SysConfiguration sysConfiguration) throws ServiceException;
	
	/**
	 * update并更新缓存
	 * @param sysConfiguration
	 * @throws ServiceException
	 */
	void updateSysConfiguration(SysConfiguration sysConfiguration) throws ServiceException;
	
	/**
	 * 删除并更新缓存
	 * @param resourceid
	 * @throws ServiceException
	 */
	void deleteSysConfiguration(String resourceid) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param resourceids
	 * @throws ServiceException
	 */
	void batchDeleteSysConfiguration(String[] resourceids) throws ServiceException;
	
	/**
	 * 根据条件查询列表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findSysConfigurationByCondition(Map<String,Object> condition,Page page) throws ServiceException;
	
	/**
	 * 根据编码获取全局参数实体
	 * @param paramCode
	 * @return
	 */
	SysConfiguration findOneByCode(String paramCode);
}
