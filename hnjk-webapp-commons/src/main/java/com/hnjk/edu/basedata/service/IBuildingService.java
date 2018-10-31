package com.hnjk.edu.basedata.service;

import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Building;
/**
 * 教学楼管理服务接口
 * <code>IBuildingService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-10-21 下午01:49:54
 * @see 
 * @version 1.0
 */
public interface IBuildingService extends IBaseService<Building> {
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findBuildingByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	/**
	 * 获取下一个排序号
	 * @return
	 * @throws ServiceException
	 */
	Long getNextShowOrder() throws ServiceException;

	/**
	 * 根据条件构造成select标签中的option(只供select标签用)
	 * @param condition
	 * @return 
	 * @throws ServiceException
	 */
	StringBuffer constructOptions(Map<String, Object> condition, String buildingid) throws ServiceException;
}
