package com.hnjk.security.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.GrantedAuthority;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.security.model.Resource;

/**
 * 系统资源管理服务. <code>IResourceService</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-11 下午05:13:01
 * @modify: 
 * @主要功能：
 * @see 
 * @version 1.0
 */
public interface IResourceService extends IBaseService<Resource>{
	
	/**
	 * 保存资源，刷新缓存
	 * @param resource
	 * @throws ServiceException
	 */
	public void saveResource(Resource resource) throws ServiceException;
	
	/**
	 * 删除资源并跟新缓存
	 * @param resourceid
	 * @throws ServiceException
	 */
	void deleteResource(String resourceid) throws ServiceException;
	
	
	/**
	 * 更新资源
	 * @param resource
	 * @throws ServiceException
	 */
	void updateResource(Resource resource) throws ServiceException;
	
	/**
	 * 获取授权
	 * @param resourcePath
	 * @return
	 * @throws ServiceException
	 */
	GrantedAuthority[] getAuthoritys(String resourcePath) throws ServiceException;
		
	/**
	 * 根据条件查找资源
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findResourceByCondition(Map<String,Object> condition, Page page) throws ServiceException;
	
			
	/**
	 * 级联删除中间表
	 * @param resourceid
	 */
	void deleteCascadeArray(String ids);
	
	/**
	 * 根据rootCode查找资源树
	 * @param condition key type,code,id
	 * @return
	 */
	List<Resource> findResourceTree(Map<String, Object> condition);

	/**
	 * 根据条件查找资源
	 */
	List<Resource> findBySql (Map<String,Object> condition) throws ServiceException;
}
