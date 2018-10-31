package com.hnjk.security.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.security.model.Resource;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserAdaptor;

/**
 * 用户服务接口. <code>IUserService</code>
 * <p>;
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2009-11-11 下午03:01:33
 * @modify:
 * @主要功能：
 * @see
 * @version 1.0
 */
public interface IUserService extends IBaseService<User> {
			
	/**
	 * 根据用户登录账号获取用户对象
	 * 
	 * @param userName
	 * @return
	 * @throws ServiceException
	 */
	User getUserByLoginId(String userName) throws ServiceException;

	/**
	 * 是否存在该账号的用户
	 * 
	 * @param userName
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsUser(String userName) throws ServiceException;

	/**
	 * 获取用户的授权资源 
	 * @param username   用户登录名
	 * @param resourceLevel    资源级别. 资源是一个树形结构，资源级别表示该节点所处层级.
	 * @return
	 * @throws ServiceException
	 */
	List<Resource> getUserAuthoritys(String username, int resourceLevel) throws ServiceException;

	/**
	 * 根据条件查找用户
	 * <排除学生>学生在学籍信息里查找
	 * 
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	Page findUserByCondition(Map<String, Object> condition, Page page) throws ServiceException;
			
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split);
		
	
	/**
	 * 根据用户ID 获取用户代理，给工作流等外部组件用
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	UserAdaptor getUserAdaptor(String userId) throws ServiceException;
	
	/**
	 * 修改用户密码.
	 * @param userId
	 * @param oldPwd
	 * @param newPwd
	 * @throws ServiceException
	 */
	void changedUserPassword(String userId,String oldPwd,String newPwd) throws ServiceException;
	
	/**
	 * 根据ID查找用户的角色列表
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	List<Role> findUserRoles(String userId) throws ServiceException;
	
	/**
	 * 根据用户名集合获取名称集合
	 * 
	 * @param accounts
	 * @return
	 * @throws ServiceException
	 */
	String findNamesByAccounts(String accounts) throws ServiceException;
	
	/**
	 * 根据条件获取用户列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<User> findUserByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据条件获取用户列表--分页
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findByCondition(Map<String, Object> condition,Page page) throws ServiceException;
	
	/**
	 * 更新用户在线时长信息
	 * @param sql
	 * @param params
	 * @throws Exception
	 */
	public void updateLoginLongInfo(String sql, Object... params) throws Exception;

	/**
	 * 根据条件获取app使用情况列表--分页
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findAppUseConditionByJDBC(Map<String, Object> condition, Page objPage) throws ServiceException;

	/**
	 * 查询用户终端使用情况
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> findAppUseConditionByCondition(Map<String, Object> condition);

	/**
	 * 根据用户名集合获取不在范围类的用户名称
	 * 
	 * @param accounts
	 * @return
	 * @throws ServiceException
	 */
	String findNamesByOutResourceids(String cnname,String unitid) throws ServiceException;
	/**
	 * 重置用户密码
	 * @param resourceids 用户id集合
	 * @param newPwd 新密码
	 */
	void resetUserPassword(String[] resourceids, String newPwd)
			throws ServiceException;;
}
