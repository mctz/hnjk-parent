package com.hnjk.edu.recruit.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.recruit.model.ExamineeChangeInfo;
import com.hnjk.edu.recruit.service.IExamineeChangeInfoService;



/**
 * 考生异动信息服务实现<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2015-12-31 下午15:26:38
 * @see 
 * @version 1.0
*/
@Service("examineeChangeInfoService")
@Transactional
public class ExamineeChangeInfoServiceImpl extends BaseServiceImpl<ExamineeChangeInfo> implements IExamineeChangeInfoService{

	/**
	 * 根据条件获取转点记录列表-分页
	 * 
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
 		StringBuffer hql = findByConditionHql(condition, params);
		
		Page page = findByHql(objPage, hql.toString(), params);
		return page;
	}
	
	/**
	 * 根据条件获取转点记录列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamineeChangeInfo> findByCondition( Map<String, Object> condition) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
 		StringBuffer hql = findByConditionHql(condition, params);
 		if(condition.containsKey("examineeChangeInfoIds")){
 			hql.append(" and eci.resourceid in (:examineeChangeInfoIds) ");
 			params.put("examineeChangeInfoIds", condition.get("examineeChangeInfoIds"));
 		}
 		
 		hql.append(" order by eci.recruitPlan.recruitPlanname,eci.major.majorCode,eci.rolloutSchool.unitCode,eci.rollinSchool.unitCode ");
		return findByHql(hql.toString(), params);
	}

	private StringBuffer findByConditionHql(Map<String, Object> condition, Map<String, Object> params) {
		StringBuffer hql = new StringBuffer("from "+ExamineeChangeInfo.class.getSimpleName()+" eci where eci.isDeleted=0 ");
		
		if(condition.containsKey("recruitPlanId")){
			hql.append(" and  eci.recruitPlan.resourceid=:recruitPlanId ");
			params.put("recruitPlanId", condition.get("recruitPlanId"));
		}
		if(condition.containsKey("classic")){
			hql.append(" and  eci.classic.resourceid=:classic ");
			params.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("examineeName")){
			hql.append(" and  eci.examineeName like :examineeName ");
			params.put("examineeName", "%"+condition.get("examineeName")+"%");
		}
		if(condition.containsKey("enrolleeCode")){
			hql.append(" and  eci.enrolleeInfo.enrolleeCode=:enrolleeCode ");
			params.put("enrolleeCode", condition.get("enrolleeCode"));
		}
		if(condition.containsKey("examCertificateNo")){
			hql.append(" and  eci.enrolleeInfo.examCertificateNo=:examCertificateNo ");
			params.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		if(condition.containsKey("major")){
			hql.append(" and  eci.major.resourceid=:major ");
			params.put("major", condition.get("major"));
		}
		if(condition.containsKey("rollType") && "rollinOrOut".equals(condition.get("rollType"))){
			hql.append(" and  (eci.rolloutSchool.resourceid=:rolloutSchool or eci.rollinSchool.resourceid=:rollinSchool) ");
			params.put("rolloutSchool", condition.get("rolloutSchool"));
			params.put("rollinSchool", condition.get("rollinSchool"));
		} else {
			if(condition.containsKey("rolloutSchool")){
				hql.append(" and  eci.rolloutSchool.resourceid=:rolloutSchool ");
				params.put("rolloutSchool", condition.get("rolloutSchool"));
			}
			if(condition.containsKey("rollinSchool")){
				hql.append(" and  eci.rollinSchool.resourceid=:rollinSchool ");
				params.put("rollinSchool", condition.get("rollinSchool"));
			}
		}
		return hql;
	}


}
