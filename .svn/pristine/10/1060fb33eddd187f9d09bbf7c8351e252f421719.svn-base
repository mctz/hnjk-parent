package com.hnjk.edu.finance.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.finance.model.FeeMajor;
/**
 * 缴费类别设置服务接口.
 * <code>IFeeMajorService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-8 下午03:28:48
 * @see 
 * @version 1.0
 */
public interface IFeeMajorService extends IBaseService<FeeMajor> {
	Page findFeeMajorByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	List<String> findFeeMajor() throws ServiceException;
	
	/**
	 * 根据属性获取唯一实体
	 * @param propertyName
	 * @param propertyValue
	 * @return
	 * @throws ServiceException
	 */
	FeeMajor findByProperty(String propertyName, String propertyValue) throws ServiceException;
	
	/**
	 * 根据条件获取唯一的缴费专业实体
	 * @param majorId
	 * @param teachingType
	 * @return
	 */
	FeeMajor findUniqueByCondition(String majorId,String teachingType);
	
	/**
	 * 批量物理删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws Exception;
}
