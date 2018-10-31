package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.LinkageQuery;
import com.hnjk.edu.teaching.vo.LinkageQueryInfo;

/**
 * 联动查询接口
 * @author zik, 广东学苑教育发展有限公司
 *
 */
public interface ILinkageQueryService extends IBaseService<LinkageQuery> {

	/**
	 * 根据条件查询联动查询-Page
	 * 
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page findLinkageQueryByCondition(Page page, Map<String, Object> condition) throws ServiceException;
		
	/**
	 * 根据条件查询联动查询-List
	 * 
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	List<LinkageQuery> findByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 根据条件判断该实体是否已存在
	 * 
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	LinkageQuery isExisted(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 同步某个年级的招生专业到联动查询表
	 * 
	 * @param gradeId
	 * @throws ServiceException
	 */
	void sysncRecruitMajorToLQ(String gradeId) throws ServiceException;
	
	/**
	 * 根据条件查询联动查询的信息
	 * 
	 * @param condition
	 * @param operate
	 * @return
	 * @throws ServiceException
	 */
	List<LinkageQueryInfo> findLinkageQueryInfoByCondition(Map<String, Object> condition, String operate) throws ServiceException;
	
	/**
	 * 构造年级select标签的options
	 * 
	 * @param condition
	 * @param defaultValue
	 * @param operate
	 * @param selInfo
	 * @return
	 * @throws ServiceException
	 */
	String constructOptions(Map<String,Object> condition, String defaultValue, String operate, Map<String, Object> selInfo) throws ServiceException;
	
	/**
	 * 构造教学点select标签的options
	 * 
	 * @param defaultValue
	 * @param schoolId
	 * @param unitInfo
	 * @return
	 * @throws ServiceException
	 */
	String constructUnitOptions(String defaultValue,String schoolId, Map<String, Object> unitInfo) throws ServiceException;
	
	/**
	 * 构建select标签的属性
	 * @param selInfo
	 * @return
	 * @throws ServiceException
	 */
	String constructSelectAttribute(Map<String, Object> selInfo) throws ServiceException;

	void sysncStudentInfoToLQ(String gradeId);

	/**
	 * 清除联动查询缓存
	 */
	void clearCacheMap();
}
