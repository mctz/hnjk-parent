package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.hibernate.ExGeneralHibernateDao;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.NationMajor;
import com.hnjk.edu.recruit.model.BranchSchoolPlan;
import com.hnjk.edu.recruit.model.BranchShoolNewMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.security.model.User;


/**
 * 校外学习中心开设批次服务接口<code>IBranchSchoolPlanService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-6-11 下午01:38:24
 * @see 
 * @version 1.0
*/
public interface IBranchSchoolPlanService extends IBaseService<BranchSchoolPlan>{
	
    /**
     * 根据条件查找学习中心招生批次
     * @param condition
     * @return
     * @throws ServiceException
     */
    public BranchSchoolPlan getBranchSchoolPlanByCondition(Map<String,Object> condition)throws ServiceException;
    
    /**
     * 根据条件查找学习中心招生批次
     * @param condition
     * @return
     * @throws ServiceException
     */
    public List<BranchSchoolPlan> getBranchSchoolPlanList(Map<String,Object> condition)throws ServiceException;
    
    /**
     * 根据条件查找学习中心招生批次列表
     * @param condition
     * @return
     * @throws ServiceException
     */
    public Page getBrsPlanListByCondition(Page page,Map<String,Object> condition)throws ServiceException;
    
    /**
     * 获取审核通过的校外学习中心招生批次
     * @return
     * @throws ServiceException
     */
    public List getAuditedBrsPlanList()throws ServiceException;
    /**
     * 获取给定校外学习中心审核通过的招生批次
     * @return
     * @throws ServiceException
     */
    public List<BranchSchoolPlan> getAuditedBrsPlanListForBranSchool(Map<String,Object> condition)throws ServiceException;
    
    /**
     *  校外学习中心申报专业流程-入口
     * @param request                      HttpServletRequest对象
     * @param param						        申报新专业时用于保存参数的MAP
     * @param map                          提供HQL查询时的参数传递MAP 
     * @param user                         当前用户 
     * @param uploadNewMajorFileFileIds    申报新专业时上传的附件ID
     * @param baseMajors                   从基础专业库中申报新专业的ID
     * @param nationMajors                 从国家专业库中申报新专业的ID
     * @param brSchoolplanId               校外学习中心招生批次ID
     * @param wf_id                        流程实例ID
     * @param majorIds                     申报招生批次中的招生专业ID-标识符
     * @param recruitMajors                申报招生批次中的招生专业ID
     * @param limitNums                    申报招生批次中的招生专业的指标数
     * @param lowerNums					        申报招生批次中的招生专业的下限数
     * @param recruitPlan                  校外学习中心要申报专业的招生批次
     * @return                             BranchSchoolPlan---校外学习中心招生批次 
     * @throws Exception
     */
    public BranchSchoolPlan appRecruitPlanFlowerStart(HttpServletRequest request, Map<String, Object> param,Map<String, Object> map, User user,String[] uploadNewMajorFileFileIds,
    												  String[] baseMajors,String[] nationMajors, String brSchoolplanId,String wf_id,String[] majorIds, String[] recruitMajors,
    												  String[] limitNums,String[] lowerNums, RecruitPlan recruitPlan) throws Exception;
    /**
     * 校外学习中心申报专业流程-结束时增加学习中心限制专业及基础专业
     * @param brMajor
     * @param nationMajor
     */
    public void appRecruitPlanFlowerEnd(BranchSchoolPlan brSchoolPlan)throws Exception;
    /**
     * 校外学习中心申报专业流程-申报新专业
     * @param brSchoolPlan
     * @param param
     */
    public void appRecruitPlanFlawerAddNewMajor(BranchSchoolPlan brSchoolPlan,Map<String,Object> param)throws Exception;
    
    
    /**
     * 校外学习中心申报专业流程-申报招生批次专业
     * @param majorIds
     * @param brSchoolPlan
     */
	
	
    public void appRecruitPlanAppBrMajor(String[] majorIds,String[] recruitMajors,String[] limitNums,String[] lowerNums ,BranchSchoolPlan brSchoolPlan)throws Exception;

    public ExGeneralHibernateDao getExGeneralHibernateDao()throws ServiceException;
    
    /**
     * 查询给定ID的招生批次是否已经有通过审核的申报记录
     * @param planId  招生批次ID，可以用传单个，也可以传以逗号分隔的多个ID
     * @return
     * @throws Exception
     */
    public Map<String, Object> isBrSchoolAPPAuditPlan(String planId)throws Exception;
    
    /**
     * 查询学习中心某一年度申报通过的批次
     * @param unitId
     * @param preYear
     * @return
     * @throws Exception
     */
    public List<BranchSchoolPlan> findPreAuditBranchSchoolPlanList(String unitId,Long year);
}
