package com.hnjk.edu.teaching.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.MergeExamroom;
import com.hnjk.edu.teaching.service.IMergeExamRoomService;

/**
 * 
 * <code>考场合并MergeExamRoomServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2011-8-23 下午04:09:59
 * @see 
 * @version 1.0
 */
@Transactional
@Service("mrgeExamRoomService")
public class MergeExamRoomServiceImpl extends BaseServiceImpl<MergeExamroom> implements IMergeExamRoomService{

	/**
	 * 根据条件查询考场合并记录表
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findMergeExamRoomByCondition(Page page,Map<String, Object> condition) throws ServiceException {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDeleted",0);
		
		StringBuffer hql    		 = new StringBuffer(" from "+MergeExamroom.class.getSimpleName()+" mergeexamroom  where mergeexamroom.isDeleted=:isDeleted");
		if (condition.containsKey("examType")) {
			hql.append(" and mergeexamroom.examType=:examType");
			paramMap.put("examType",condition.get("examType"));
		}
		if (condition.containsKey("outBranchSchool")) {
			hql.append(" and mergeexamroom.outBrSchool.resourceid=:outBranchSchool");
			paramMap.put("outBranchSchool",condition.get("outBranchSchool"));
		}
		if (condition.containsKey("inBranchSchool")) {
			hql.append(" and mergeexamroom.inBrSchool.resourceid=:inBranchSchool");
			paramMap.put("inBranchSchool",condition.get("inBranchSchool"));
		}
		
		return exGeneralHibernateDao.findByHql(page, hql.toString(), paramMap);
	}

	/**
	 * 查询给定学习中心接收的合并记录(接收学习中心ID)
	 * @param examType          考试类型(入学考\期末考)
	 * @param inBrschoolId      接收的学习中心ID
	 * @param recruitPlan       招生批次ID
	 * @param recruitExamPlan   机考场次ID
	 * @param examSub           期末考试批次ID
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<String> findMergeExamRoomByUnitId(int examType,String inBrschoolId,String recruitPlan,String recruitExamPlan,String examSub) throws ServiceException {
		StringBuffer hql 		     = new StringBuffer();
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("isDeleted",0);
		paramMap.put("examType",examType);
		
		hql.append(" select mergeexamroom.outBrSchool.resourceid  from "+MergeExamroom.class.getSimpleName()+" mergeexamroom  where mergeexamroom.isDeleted=:isDeleted");
		hql.append(" and mergeexamroom.examType=:examType");
		hql.append(" and mergeexamroom.inBrSchool.resourceid in("+inBrschoolId+")");
		
		if (ExStringUtils.isNotEmpty(recruitPlan)) {
			hql.append(" and mergeexamroom.recruitPlan.resourceid=:recruitPlan");
			paramMap.put("recruitPlan", recruitPlan);
		}
		
		if (ExStringUtils.isNotEmpty(recruitExamPlan)&&!"all".equals(recruitExamPlan)) {
			
			hql.append(" and mergeexamroom.recruitExamPlan.resourceid=:recruitExamPlan");
			paramMap.put("recruitExamPlan", recruitExamPlan);
			
		}else if(ExStringUtils.isNotEmpty(recruitExamPlan)&&"all".equals(recruitExamPlan)){
			hql.append(" and mergeexamroom.recruitExamPlan.resourceid is not null");
		}
		if (ExStringUtils.isNotEmpty(examSub)) {
			hql.append(" and mergeexamroom.examSub.resourceid=:examSub");
			paramMap.put("examSub", examSub);
		}
		
		return (List<String>) exGeneralHibernateDao.findByHql(hql.toString(), paramMap);
	}
	
	
}
