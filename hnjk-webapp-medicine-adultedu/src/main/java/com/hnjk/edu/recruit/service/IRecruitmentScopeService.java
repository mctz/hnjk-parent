package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.RecruitmentScope;

/** 
 * @author Zik, 广东学苑教育发展有限公司
 * @since 2018年6月28日 下午4:12:36 
 * 
 */
public interface IRecruitmentScopeService extends IBaseService<RecruitmentScope> {

	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 批量删除
	 * @param split
	 */
	void batchCascadeDelete(String[] split) throws ServiceException;
	
	/**
	 * 获取某个教学点的招生范围
	 * @param unitId
	 * @return
	 * @throws ServiceException
	 */
	List<String> findScopeByUnitId(String unitId)  throws Exception;
}
