package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.MergeExamroom;

public interface IMergeExamRoomService extends IBaseService<MergeExamroom>{
	/**
	 * 根据条件查询考场合并记录表
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page findMergeExamRoomByCondition(Page page,Map<String,Object> condition)throws ServiceException;
	
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
	public List<String> findMergeExamRoomByUnitId(int examType,String inBrschoolId,String recruitPlan,String recruitExamPlan,String examSub)throws ServiceException;
}
