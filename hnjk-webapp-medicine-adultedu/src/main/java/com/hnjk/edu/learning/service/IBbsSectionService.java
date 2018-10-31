package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.BbsSection;

/**
 * 课程论坛版块管理服务接口
 * <code>IBbsSectionService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-6 下午04:33:21
 * @see 
 * @version 1.0
 */
public interface IBbsSectionService extends IBaseService<BbsSection> {
	/**
	 * 分页查询课程论坛版块
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findBbsSectionByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 查出版块及其子版块列表
	 * @param parentId
	 * @return
	 * @throws ServiceException
	 */
	List<BbsSection> findBbsSectionBy(String parentId) throws ServiceException;
	/**
	 * 课程板块
	 * @return
	 * @throws ServiceException
	 */
	List<BbsSection> findCourseBbsSection() throws ServiceException;
	/**
	 * 删除课程版块
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteBbsSection(String[] ids) throws ServiceException;
	/**
	 * 版块编码是否存在
	 * @param sectionCode
	 * @return
	 * @throws ServiceException
	 */
	boolean isExistsSectionCode(String sectionCode) throws ServiceException;
	/**
	 * 统计今日贴
	 * @param bbsSectionId
	 * @return
	 * @throws ServiceException
	 */
	Integer statTodayTopicCount(String bbsSectionId,String courseId) throws ServiceException;
	/**
	 * 统计版块帖子总量
	 * @param bbsSectionId
	 * @return
	 * @throws ServiceException
	 */
	Integer statInvitationCount(String bbsSectionId,String courseId) throws ServiceException;
	/**
	 * 统计版块主题总量
	 * @param bbsSectionId
	 * @return
	 * @throws ServiceException
	 */
	Integer statTopicCount(String bbsSectionId,String courseId) throws ServiceException;
	/**
	 * 是否为版主
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	boolean isModerator(String id) throws ServiceException;
	 /**
	  * 获取版块列表
	  * @param hql
	  * @return
	  * @throws ServiceException
	  */
	Map<String, Object> getSectionList(String hql) throws ServiceException;
	/**
	 * 获取下一排序号
	 * @param parentId
	 * @return
	 * @throws ServiceException
	 */
	Integer getNextShowOrder(String parentId) throws ServiceException;
	
	/**
	 * 根据编码获取版块
	 * @param sectionCode
	 * @return
	 * @throws ServiceException
	 */
	BbsSection getBySectionCode(String sectionCode) throws ServiceException;
}
