package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.ExportRecruitPlan;
import com.hnjk.edu.recruit.model.RecruitPlan;


/**
 * 招生计划服务接口<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-10 上午10:59:20
 * @see 
 * @version 1.0
*/
public interface IRecruitPlanService extends IBaseService<RecruitPlan> {
	
  /**
   * 根据条件查找招生计划
 * @param condition
 * @param page
 * @return
 * @throws ServiceException
 */
public Page findPlanByCondition(Map<String, Object> condition, Page page) throws ServiceException; 
/**
 * 根据条件查找招生计划
 * @param condition
 * @return
 * @throws ServiceException
 */
public List<RecruitPlan> findPlanByCondition (Map<String, Object> condition)throws ServiceException;
/**
 * 获得最近的一个招生计划
 * @return
 * @throws ServiceException
 */
public RecruitPlan getLastPlan()throws ServiceException; 

/**
 * 获取一个开放的招生计划
 * @return
 * @throws ServiceException
 */
public RecruitPlan getOnePublishedPlan()throws ServiceException;

/**
 * 获取开放的招生计划
 * @return
 * @throws ServiceException
 */
public List<RecruitPlan>  getPublishedPlanList(String type)throws Exception;

/**
 * 获取开放的校外学习中心批次（如：直属班批次）
 * @param brSchoolId 校外学习中心ID
 * @return
 * @throws ServiceException
 *//*
public List<RecruitPlan> getPublishedBrschoolPlanList(String brSchoolId)throws Exception;*/

/**
 * 导出表1. 试点高校网络教育招生计划备案表 导出所使用的属性列表方法
 */
public List<ExportRecruitPlan> exportList(Map<String,Object> condition);

/**
 * 获取招生批次的教学模式，返回JSON集合
 * @param condition
 * @return
 * @throws ServiceException
 */
public List<JsonModel> getTeachingTypeJsonList(String planId)throws ServiceException;


}
