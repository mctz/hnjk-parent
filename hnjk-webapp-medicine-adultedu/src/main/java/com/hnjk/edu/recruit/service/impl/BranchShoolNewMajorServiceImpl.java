package com.hnjk.edu.recruit.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.BranchShoolNewMajor;
import com.hnjk.edu.recruit.service.IBranchShoolNewMajorService;

/**
 * 校外学习中心申报新专业服务接口实现<code>IBranchShoolNewMajorServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-4-18 下午01:38:24
 * @see 
 * @version 1.0
*/
@Service("branchShoolNewMajorService")
@Transactional
public class BranchShoolNewMajorServiceImpl extends BaseServiceImpl<BranchShoolNewMajor> implements IBranchShoolNewMajorService{
	
	 @Override
	 public ExGeneralHibernateDao getExGeneralHibernateDao()throws ServiceException{
		 return this.exGeneralHibernateDao;
	 }

	 /**
	  * 根据条件查询学习中心申报的新专业
	  * @param condition
	  * @return
	  * @throws ServiceException
	  */
	@Override
	public List<BranchShoolNewMajor> findBranchShoolNewMajorByCondition(Map<String, Object> condition) {
		StringBuffer hql = new StringBuffer();
		Map<String,Object> paramMap = new HashMap<String, Object>();
		hql.append(" from "+BranchShoolNewMajor.class.getSimpleName()+" newMajor where newMajor.isDeleted=0");
		if (condition.containsKey("branchSchoolPlan")) {
			hql.append(" and newMajor.branchSchoolPlan.resourceid=:branchSchoolPlan");
			paramMap.put("branchSchoolPlan", condition.get("branchSchoolPlan"));
		}
		if (condition.containsKey("isIntoRecruitPlan")) {
			hql.append(" and newMajor.isIntoRecruitPlan=:isIntoRecruitPlan");
			paramMap.put("isIntoRecruitPlan", condition.get("isIntoRecruitPlan"));
		}
		if (condition.containsKey("isPass")) {
			paramMap.put("isPass", condition.get("isPass"));
			hql.append(" and newMajor.isPassed=:isPass");
		}
		if(condition.containsKey("orderBy")){//排序
			hql.append(" order by "+condition.get("orderBy"));
		}
		return (List<BranchShoolNewMajor>) this.exGeneralHibernateDao.findByHql(hql.toString(), paramMap);
	}

}
