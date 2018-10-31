package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachTaskDetails;
/**
 * 教学任务书明细服务接口
 * <code>ITeachTaskDetailsService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-2-16 下午04:07:13
 * @see 
 * @version 1.0
 */
public interface ITeachTaskDetailsService extends IBaseService<TeachTaskDetails> {
	/**
	 * 根据条件查找列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachTaskDetails> findTeachTaskDetailsByCondition(Map<String, Object> condition) throws ServiceException;
	
	void batchCascadeDelete(String[] split,String teachTaskId)throws ServiceException;
	
	Page findTeachTaskDetailsByCondition(Map<String, Object> condition, Page objPage);
}
