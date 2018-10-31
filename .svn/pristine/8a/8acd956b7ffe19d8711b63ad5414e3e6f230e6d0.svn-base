package com.hnjk.edu.learning.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.learning.model.BbsReply;

/**
 * 论坛帖子回复服务接口
 * <code>IBbsReplyService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-9-8 下午02:23:19
 * @see 
 * @version 1.0
 */
public interface IBbsReplyService extends IBaseService<BbsReply> {
	/**
	 * 分页查询论坛帖子回复
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findBbsReplyByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;
	/**
	 * 保存回复及附件
	 * @param bbsReply
	 * @param attachIds
	 * @throws ServiceException
	 */
	void saveOrUpdateBbsReply(BbsReply bbsReply,String[] attachIds) throws ServiceException;
	/**
	 * 删除论坛帖子回复
	 * @param ids
	 * @throws ServiceException
	 */
	void deleteBbsReply(String[] ids) throws ServiceException;
	/**
	 * 获取下一个排序号
	 * @param bbsTopicId
	 * @return
	 * @throws ServiceException
	 */
	public Integer getNextShowOrder(String bbsTopicId) throws ServiceException;
	
	/**
	 * 获取某个人对某个随堂问答的最新回复
	 * @param userId
	 * @param topicId
	 * @return
	 */
	public BbsReply getSomeOneLastReply(String userId, String topicId);
}
