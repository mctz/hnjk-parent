package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;

/**
 * 学习中心招生专业服务接口<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-17 上午09:49:38
 * @see 
 * @version 1.0
*/
public interface IBranchSchoolMajorService extends IBaseService<BranchSchoolMajor> {

	/**
	 * 查找学习中心招生专业列表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findBranchSchoolMajorList(Map<String, Object> condition,  Page page)throws ServiceException;
	/**
	 * 查找学习中心招生专业列表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<BranchSchoolMajor> findBranchSchoolMajorList(Map<String, Object> condition)throws ServiceException;
	/**
	 * 获取学习中心可用的招生专业列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<BranchSchoolMajor> getCanUseMajorList(Map<String, Object> condition)throws ServiceException; 


}
