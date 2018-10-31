package com.hnjk.edu.recruit.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.BranchSchoolMajor;
import com.hnjk.edu.recruit.service.IBranchSchoolMajorService;

/**
 * 学习中心招生专业服务实现
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-17 上午09:50:48
 * @see
 * @version 1.0
 */
@Service("branchSchoolMajorService")
@Transactional
public class BranchSchoolMajorServiceImpl extends
		BaseServiceImpl<BranchSchoolMajor> implements IBranchSchoolMajorService {

	@Override
	@Transactional(readOnly=true)
	public Page findBranchSchoolMajorList(Map<String, Object> condition,
			Page page) throws ServiceException {

		StringBuffer hql = new StringBuffer(
				" from BranchSchoolMajor major where major.isDeleted ='0' ");
	   //  学习中心
		if (condition.containsKey("branchSchool")) {
		     hql.append(" and major.branchSchool.resourceid =:branchSchool");
		}	
		// 招生批次
		if (condition.containsKey("recruitPlan")) {
			hql.append(" and major.recruitMajor.recruitPlan.resourceid =:recruitPlan ");
		}
		//审核状态
		if(condition.containsKey("checkStatus"))
		{
		  hql.append(" and major.status =:checkStatus ");
		}
		//如果是审批，则查看已经申报的招生专业
		if(condition.containsKey("check"))
		{
		  hql.append(" and major.fillinMan is not null");
		}
		page.setOrderBy("major.recruitMajor.recruitMajorName");
		page.setOrder(Page.ASC);
//		hql.append(" order by major.recruitMajor.recruitMajorName ");
		return exGeneralHibernateDao.findByHql(page, hql.toString(), condition);
	}

	@Override
	@Transactional(readOnly=true)
	public List<BranchSchoolMajor> getCanUseMajorList(Map<String, Object> condition) throws ServiceException {
		StringBuffer hql			= new StringBuffer();
		Map<String, Object> paraMap = new HashMap<String, Object>();		
		hql.append(" from "+BranchSchoolMajor.class.getSimpleName()+" major where major.isDeleted ='0' ");
		//  学习中心
		if (condition.containsKey("branchSchool")) {
		     hql.append(" and major.branchSchoolPlan.branchSchool.resourceid =:branchSchool");
		     paraMap.put("branchSchool", condition.get("branchSchool"));
		}	
		// 招生批次
		if (condition.containsKey("recruitPlan")) {
			hql.append(" and major.recruitMajor.recruitPlan.resourceid =:recruitPlan ");
			paraMap.put("recruitPlan", condition.get("recruitPlan"));
		}
		//是否审核通过
		if (condition.containsKey("isAudited")) {
			  hql.append(" and major.branchSchoolPlan.isAudited =:isAudited");
			  paraMap.put("isAudited", condition.get("isAudited"));
		}
		//招生专业
		if (condition.containsKey("recruitMajor")) {
			hql.append(" and major.recruitMajor.resourceid =:recruitMajor ");
			paraMap.put("recruitMajor", condition.get("recruitMajor"));
		}
		return  findByHql(hql.toString(),paraMap);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<BranchSchoolMajor> findBranchSchoolMajorList(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer(" from "+BranchSchoolMajor.class.getSimpleName()+
											" major where major.isDeleted ='0' and major.branchSchoolPlan.isAudited='Y'");
		//招生批次
		if (condition.containsKey("recruitPlan")) {
			hql.append(" and major.branchSchoolPlan.resourceid ='"+condition.get("recruitPlan")+"'");
		}
		//校外学习中心的招生年批次ID列表
		if (condition.containsKey("brSchoolPlanIds")) {
			hql.append(" and major.branchSchoolPlan.resourceid in("+condition.get("brSchoolPlanIds")+")");
		}
		//校外学习中心
		if (condition.containsKey("branchSchoolId")) {
			hql.append(" and major.branchSchoolPlan.branchSchool.resourceid='"+condition.get("branchSchoolId")+"'");
		}
		
		return (List<BranchSchoolMajor>) exGeneralHibernateDao.findByHql(hql.toString());
	}



}
