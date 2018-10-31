package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.BbsGroupUsers;
/**
 * 学习小组成员管理服务接口
 * <code>IBbsGroupUsersService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-28 上午11:43:33
 * @see 
 * @version 1.0
 */
public interface IBbsGroupUsersService extends IBaseService<BbsGroupUsers> {
	/**
	 * 分页查询学习小组成员
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findBbsGroupUsersByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 删除学习小组成员
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteBbsGroupUsers(String[] ids) throws ServiceException;
	/**
	 * 查出已加入小组的成员列表
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	List<BbsGroupUsers> findBbsGroupUsersByCourseId(String courseId) throws ServiceException;
	/**
	 * 是否小组成员
	 * @param userId
	 * @param groupId
	 * @return
	 * @throws ServiceException
	 */
	boolean isUserInGroup(String userId,String groupId) throws ServiceException;
}
