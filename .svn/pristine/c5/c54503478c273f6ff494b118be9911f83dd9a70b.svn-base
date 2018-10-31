package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.ExamineeChangeInfo;


/**
 * 考生异动信息服务接口<p>
 * 
 * @author：zik  广东学苑教育发展有限公司
 * @since： 2015-12-31 下午15:24:28
 * @see 
 * @version 1.0
*/
public interface IExamineeChangeInfoService extends IBaseService<ExamineeChangeInfo> {
	
	/**
	 * 根据条件获取转点记录列表-分页
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 根据条件获取转点记录列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamineeChangeInfo> findByCondition(Map<String, Object> condition) throws ServiceException;
}
