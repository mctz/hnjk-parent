package com.hnjk.edu.basedata.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.YearInfo;

/**
 * <code>IYearInfoService</code>基础数据-年级-服务接口.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午01:56:02
 * @see 
 * @version 1.0
 */
public interface IYearInfoService extends IBaseService<YearInfo>{
	
	/**
	 * 分页查询列表
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findYearInfoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;
	/**
	 * 查找年度-供学生预约操作使用
	 * @return
	 * @throws ServiceException
	 */
	public List<YearInfo> findYearInfoListForOrderOperater()throws ServiceException;
	
	/**
	 * 查询全部加排序
	 * @return
	 */
	public List<YearInfo> findAllByOrder();
	
	/**
	 * 根据起始年份获取年度
	 * @param firstYear
	 * @return
	 * @throws ServiceException
	 */
	YearInfo getByFirstYear(Long firstYear) throws ServiceException;
	
	/**
	 * 获取所有年度信息，年度名称为key键
	 * @return
	 */
	Map<String, YearInfo> getYearInfoMapByName();

	/**
	 * 根据firseYear查找年度信息，没有则新建
	 * @param firseYear
	 * @return
	 */
	YearInfo findOrCreateYearInfoByFirstYear(Long firseYear);
}
