package com.hnjk.platform.system.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SensitiveWord;

/**
 * 敏感词接口
 * @author Zik 
 *
 */
public interface ISensitiveWordService extends IBaseService<SensitiveWord> {

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param ids
	 */
	void batchCascadeDelete(String[] ids) throws ServiceException;
	
	/**
	 * 根据敏感词获取实体
	 * @param word
	 * @return
	 * @throws ServiceException
	 */
	SensitiveWord getByWord(String word) throws ServiceException;
	
	/**
	 * 获取最近的更新时间
	 * @return
	 * @throws ServiceException
	 */
	String getLastUpateDate() throws ServiceException;
	
	/**
	 * 查询敏感词列表--不分页
	 * @param condition
	 * @return
	 */
	@Override
	List<SensitiveWord> findByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据附件导入敏感词
	 * @param attachs
	 * @return
	 * @throws ServiceException
	 */
	void importSensitiveWordByAttachs(Attachs attachs) throws Exception;
	
	/**
	 * 获取敏感词库实体
	 * @return
	 */
	SensitivewordFilter getSensitivewordFilter();
}
