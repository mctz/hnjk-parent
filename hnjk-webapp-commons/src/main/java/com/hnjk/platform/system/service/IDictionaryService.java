package com.hnjk.platform.system.service;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.Dictionary;

import java.util.List;
import java.util.Map;

/**
 * 数据字典
 * <code>IDictionaryService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-15 下午05:39:55
 * @see 
 * @version 1.0
 */
public interface IDictionaryService extends IBaseService<Dictionary> {
	
	/**前缀key方式*/
	public final static String PREKEY_TYPE_BYNAME	= "byName";
	
	public final static String PREKEY_TYPE_BYCODE	= "byCode";
	
	/**
	 * 批量级联删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;
	
	/**
	 * 修改并更新缓存
	 * @param dictionary
	 */
	void updateDict(Dictionary dictionary, List<Dictionary> childs) throws ServiceException;


	/**
	 * 保存并更新缓存
	 * @param dictionary
	 */
	void saveDict(Dictionary dictionary) throws ServiceException;

	/**
	 * 单个删除
	 * @param dictId
	 * @throws ServiceException
	 */
	void deleteDict(String dictId) throws ServiceException;

	/**
	 * 根据查询条件找分页列表
	 * @param condition 条件
	 * @param page
	 * @return
	 */
	Page findDictionaryByCondition(Map<String, Object> condition, Page page) throws ServiceException;

	/**
	 * 根据查询条件找字典
	 * @param condition
	 * @return
	 */
	List findDictionaryByCondition(Map<String, Object> condition);

	/**
	 * 根据字典编码，返回这个字典的所有key/value键值对
	 * @param dictCode
	 * @param isPreKey 是否需要设置key前缀
	 * @param prekeyType 前缀key设置方式：byName|byCode，按名字或按编码
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> getDictionByMap(List<String> dictCode, boolean isPreKey, String prekeyType) throws ServiceException;
	
	/**
	 * 根据字典编码，返回这个字典所有子集的值（以","分隔的字符串）
	 * 
	 * @param dictCode
	 * @return
	 * @throws ServiceException
	 */
	String getAllValues(String dictCode) throws ServiceException;

	/**
	 * 根据数据字典和字典值获取字典名称
	 * @param dictCode
	 * @param dictValue
	 * @return
	 */
	public String dictCode2Val(String dictCode, String dictValue);

	/**
	 * 根据父字典编码获取子集,用于判断字典名称是否在数据字典中
	 * <p>key:dictName  value:dictValue
	 * @param dictCode
	 * @return Map
	 */
	Map<String,String> getMapByParentDictCode(String dictCode);

	/**
	 * 根据父字典编码获取子集,用于获取字典名称
	 * <p>key:dictValue value:dictName
	 * @param dictCode
	 * @return Map
	 */
	Map<String,String> getDictMapByParentDictCode(String dictCode);
}
