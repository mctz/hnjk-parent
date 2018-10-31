package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.BbsGroup;
/**
 * 学生讨论小组服务接口
 * <code>IBbsGroupService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-27 下午02:08:00
 * @see 
 * @version 1.0
 */
public interface IBbsGroupService extends IBaseService<BbsGroup> {
	/**
	 * 分页查询学习小组
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findBbsGroupByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 查找某门课程的所有小组
	 * @param couseId
	 * @return
	 * @throws ServiceException
	 */
	List<BbsGroup> findBbsGroupByCourseId(String courseId) throws ServiceException;
	/**
	 * 删除学习小组
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteBbsGroup(String[] ids) throws ServiceException;
	/**
	 * 保存小组及成员
	 * @param bbsGroup
	 * @throws ServiceException
	 */
	void saveOrUpdateBbsGroup(BbsGroup bbsGroup) throws ServiceException;
	/**
	 * 根据用户查找学生学习小组
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	List<BbsGroup> findBbsGroupByUser(String userId) throws ServiceException;
	
	/**
	 * 根据条件查询在线教学学生
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findOnlineStu(Map<String, Object> condition,Page objPage) throws ServiceException;
	
	
	Page findOnlineStuGroup(Map<String, Object> condition,Page objPage) throws ServiceException;
}
