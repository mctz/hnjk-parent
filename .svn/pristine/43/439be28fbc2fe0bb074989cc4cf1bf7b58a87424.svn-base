package com.hnjk.edu.basedata.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Major;

/**
 * <code>IMajorService</code>基础数据-专业基础信息-服务接口.<p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-3-15 下午02:00:14
 * @see 
 * @version 1.0
 */
public interface IMajorService extends IBaseService<Major>{
	
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findMajorByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;

	void batchCascadeDelete(String[] split) throws ServiceException;
	
	/**
	 * 过滤查找专业-学习中心设置功能中使用
	 * @param exceptids   不查询的ID
	 * @return
	 * @throws ServiceException
	 */
	public List<Major> findMajorForBranSchoolLimt(String exceptids)throws ServiceException;
	/**
	 * 通过条件获取专业列表
	 * @param condition
	 * @return
	 */
	public List<Major> findMajorByCondition(Map<String, Object> condition) throws ServiceException;
	/**
	 * 按条件获取年级里面的专业
	 * @param condition
	 * @return
	 */
	public List<Major> findAllMajorByGradeAndBr(Map<String, Object> condition);

	/**
	 * 按条件获取年级里面的专业(添加专业)
	 * @param condition
	 * @return
	 */
	List<Major> findAllMajorByGradeAndBrMakeup(Map<String, Object> condition);
	
	/**
	 * 构造专业的select标签的option（只供select标签用）
	 * 
	 * @param majorList
	 * @param defaultValue
	 * @return
	 */
	String constructOptions(List<Major> majorList,String defaultValue,String displayType);

	public List<Major> findMjorByMaster(String userid);
	
	/**
	 * 获取所有基础专业信息，专业编码为key键
	 * @return
	 */
	Map<String, Major> getMajorMapByCode();
}
