package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.BranchShoolNewMajor;


/**
 * 校外学习中心申报新专业服务接口<code>IBranchShoolNewMajorService</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-4-18 下午01:38:24
 * @see 
 * @version 1.0
*/
public interface IBranchShoolNewMajorService extends IBaseService<BranchShoolNewMajor>{
	/**
	 * 获取
	 * @return
	 * @throws ServiceException
	 */
	public ExGeneralHibernateDao getExGeneralHibernateDao()throws ServiceException;
	 /**
	  * 根据条件查询学习中心申报的新专业
	  * @param condition
	  * @return
	  * @throws ServiceException
	  */
	 public List<BranchShoolNewMajor> findBranchShoolNewMajorByCondition(Map<String,Object> condition)throws ServiceException;
}
