package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.RecruitMajor;


/**
 * 招生专业服务接口<p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-11 上午09:33:44
 * @see 
 * @version 1.0
*/
public interface IRecruitMajorService extends IBaseService<RecruitMajor> {
	/**
	 * 根据条件查找招生专业-返回分页对象
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page findMajorByCondition(Map<String, Object> condition,Page page) throws ServiceException; 
	/**
	 * 根据条件查找招生专业-返回LIST
	 * @param condition
	 * @param order
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public List<RecruitMajor> findMajorByCondition(Map<String, Object> condition) throws ServiceException; 
	/**
	 * 根据条件查找未分配入学考试科目的招生专业
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page getUnAssignMajorList(Page page,Map<String, Object> condition)throws ServiceException; 

	/**
	 * 根据条件查找当前批次下学习中心未申报的招生专业
	 * @param page
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public Page getRecruitMajorList(Page page,Map<String, Object> condition)throws ServiceException; 
	/**
	 * 根据校外学习中心限定的专业Id及招生批次ID查找招生专业
	 * @param brSchoolMajorIds
	 * @param planId
	 * @return
	 * @throws ServiceException
	 */
	public List<RecruitMajor> findBranSchoolLimitRecruitMajorListByPlanId(Map<String,Object> param)throws ServiceException;
	
	/**
	 * 根据条件获取招生专业，返回JSON集合
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<JsonModel> getRecruitMajorJsonList(Map<String, Object> condition)throws ServiceException;

	/**
	 * 校外学习中心申报新专业成功后转后校外学习中心招生专业
	 * @throws ServiceException
	 */
	public void branSchoolNewMajorIntoRecruitMajor(Map<String,Object> map)throws Exception;
	/**
	 * 根据教学点id查询教学点招生专业通过的列表
	 * @param unitid
	 * @return
	 * @throws Exception
	 */
	List<RecruitMajor> findByUnitid(String unitid) throws Exception;
}
