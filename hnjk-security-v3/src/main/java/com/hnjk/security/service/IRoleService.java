package com.hnjk.security.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.security.model.Role;

/**
 * 系统角色服务. <code>IRoleService</code><p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-11 下午05:04:16
 * @modify: 
 * @主要功能：
 * @see 
 * @version 1.0
 */
public interface IRoleService extends IBaseService<Role>{
		
	/**
	 * 根据角色编码查找角色对象
	 * @param rolecode
	 * @return
	 * @throws ServiceException
	 */
	Role getRoleByRolecode(String rolecode) throws ServiceException;
	
	/**
	 * 根据条件查找角色
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findRoleByCondition(Map<String,Object> condition, Page page) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void deleteArray(String[] split)  throws ServiceException;
	
	/**
	 * 单个删除
	 * @param roleid
	 * @throws ServiceException
	 */
	void deleteRole(String roleid) throws ServiceException;
	
	//更新
	void saveOrUpdateRole(Role persistRole, List<String> resId) throws ServiceException;
	
	/**
	 * 查找角色树列表
	 * @param parentCode
	 * @return
	 * @throws ServiceException
	 */
	List<Role> findRoleTreeList(String parentCode) throws ServiceException;	
	
	/**
	 * 根据角色编码集合获取名称集合
	 * 
	 * @param codes
	 * @return
	 * @throws ServiceException
	 */
	String findNamesByCodes(String codes) throws ServiceException;

	/**
	 * 获取角色资源权限
	 * @param role
	 * @return
	 */
	String getAuthoritysByRole(Role role) throws ServiceException;
}
