package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ReOrderSetting;


/**
 * 
 * <code>补预约管理Service接口</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-11-23 上午10:25:02
 * @see 
 * @version 1.0
 */
public interface IReOrderSettingService extends IBaseService<ReOrderSetting>{
	
	/**
	 * 根据条件查询补预约管理记录-返回LIST
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<ReOrderSetting> findReOrderSettingByCondition(Map<String,Object> condition)throws ServiceException;
	/**
	 * 根据条件查询补预约管理记录-返回Page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findReOrderSettingByCondition(Map<String,Object> condition,Page page)throws ServiceException;
}
