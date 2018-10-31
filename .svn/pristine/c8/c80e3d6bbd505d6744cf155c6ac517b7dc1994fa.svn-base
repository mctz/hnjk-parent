package com.hnjk.edu.learning.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.BbsTopic;
import com.hnjk.security.model.User;

/**
 *  论坛帖子管理服务接口
 * <code>IBbsTopicService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-7 上午11:53:07
 * @see 
 * @version 1.0
 */
public interface IBbsTopicService extends IBaseService<BbsTopic> {
	/**
	 * 分页查询论坛帖子
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findBbsTopicByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 查询帖子列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<BbsTopic> findBbsTopicByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 全文检索
	 * @param page
	 * @param filed
	 * @param keys
	 * @return
	 * @throws ServiceException
	 */
	Page findBbsTopicByFullText(Page page,String[] filed,String keys) throws ServiceException;
	/**
	 * 保存帖子及附件
	 * @param bbsTopic
	 * @param attachIds 附件id列表，可为空
	 * @param groupIds 小组列表，普通主题可为空
	 * @throws ServiceException
	 */
	void saveOrUpdateBbsTopic(BbsTopic bbsTopic,String[] attachIds,String[] groupIds) throws ServiceException;
	/**
	 * 删除论坛帖子
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteBbsTopic(String[] ids) throws ServiceException;
	/**
	 * 设置帖子状态
	 * @param ids 帖子id
	 * @param status 帖子状态:加精(1)、锁定(-1)
	 * @throws ServiceException
	 */
	void setBbsTopicStatus(String[] ids,Integer status) throws ServiceException;
	/**
	 * 移动帖子
	 * @param ids
	 * @param bbsSectionId
	 * @throws ServiceException
	 */
	void moveBbsTopic(String[] ids,String bbsSectionId) throws ServiceException;
	/**
	 * 计算优秀帖子数
	 * @param courseId
	 * @param StudentInfoId
	 * @return
	 * @throws ServiceException
	 */
	Integer statTopicAndReply(Map<String,Object> condition) throws ServiceException;
	/**
	 * 标记问题
	 * @param ids
	 * @param bbsSectionId
	 * @throws ServiceException
	 */
	void markBbsTopic(String[] ids,String mark,String keywords) throws ServiceException;
	/**
	 * 查找需要回复的BBS(存在未解答的提问),如果需要排除已生成任务书且主讲老师或辅导老师不为空请传入excludeNoTask=true
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<Map<String,Object>> findNeedsReplyBbsTopicInfo(Map<String,Object> param)throws Exception;
	/**
	 * 保存随堂问答评分
	 * @param request
	 * @throws ServiceException
	 */
	void saveBbsTopicScore(HttpServletRequest request) throws ServiceException;
	
	/**
	 * 保存回复问题的评分信息--（供批量回复使用）
	 * @param topicId
	 * @param scoreType
	 * @param replyContent
	 * @param user
	 * @throws ServiceException
	 */
	void saveReplyTopicScore(String topicId, String scoreType, String replyContent, User user) throws ServiceException;
}
