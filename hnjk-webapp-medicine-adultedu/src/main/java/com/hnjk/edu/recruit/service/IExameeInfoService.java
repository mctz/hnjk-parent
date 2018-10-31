package com.hnjk.edu.recruit.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.ExameeInfo;
import com.hnjk.edu.recruit.vo.ExameeInfoCancelPrintVo;
import com.hnjk.platform.system.model.Dictionary;

/**
 * 招生管理服务接口.
 * <code>IRecruitManageService</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-8-30 下午02:13:59
 * @see 
 * @version 1.0
 */
public interface IExameeInfoService extends IBaseService<ExameeInfo> {
	/**
	 * 招生专业审批
	 * @param recruitPlanId
	 * @param filePath
	 * @throws ServiceException
	 * @return 审批通过记录数
	 */
	int importRecruitMajor(String recruitPlanId, String filePath) throws ServiceException;
	/**
	 * 导入报名信息并转换为报名信息
	 * @param recruitPlanId
	 * @param attachId
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> importExameeInfo(String recruitPlanId, String attachId, String ip) throws ServiceException;
	/**
	 * 导入考生相片
	 * @param recruitPlanId 招生批次
	 * @param tempZipPath 解压目录
	 * @return
	 * @throws ServiceException
	 */
	Map<String, Object> importExameeInfoPhoto(String recruitPlanId,String tempZipPath) throws ServiceException;
	/**
	 * 分页查询考生信息
	 * @param objPage
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	Page findExameeInfoByCondition(Page objPage, Map<String, Object> condition) throws ServiceException;
	/**
	 * 注销入学资格
	 * @param ids
	 * @throws ServiceException
	 */
	void cancelExameeInfo(String[] ids) throws ServiceException;
	/**
	 * 注销恢复
	 * @param ids
	 * @throws ServiceException
	 */
	void recoveryExameeInfo(String[] ids) throws ServiceException;
	/**
	 * 申请审核注销入学资格
	 * @param ids
	 * @param status
	 * @param type
	 * @throws ServiceException
	 */
	public void auditCancelExameeInfo(String[] ids,String status,String type) throws ServiceException;
	/**
	 * 注销
	 * @param ids
	 * @throws ServiceException
	 */
	void withDrawExameeInfo(String[] ids) throws ServiceException;
	public Page findExameeInfoByConditionWithJDBC(Page objPage, Map<String, Object> condition) throws ServiceException;
	public List<Map<String,Object>> findExameeInfoByConditionWithJDBC(Map<String, Object> condition) throws ServiceException;

	/**
	 * 查找注销学生信息
	 * @return
	 * @throws ServiceException
	 */
	public List<ExameeInfoCancelPrintVo> findExameeinfoCancel() throws ServiceException;
	public List<Map<String,Object>> findOrgUnitByConditionWithJDBC(Map<String, Object> condition) throws ServiceException;
	
	public String genStudentYJY(EnrolleeInfo ei, Map<String, String> maxStudentNoSuffixMap,List<Dictionary> listdict) throws ServiceException;
	
	/**
	 * 录取注册信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> findEnrollStatisticalInfo(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 录取注册统计
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> findEnrollStatisticalResult(Map<String, Object> condition) throws ServiceException;
	/**
	 * 根据 resourceids 字符串，返回学生有效的手机号码
	 */
	String getMoblie(String resId);
	/**
	 * 根据4个子条件：招生批次、教学点、层次、专业 ，返回查询条件人数
	 */
	int getSelectCount(Map<String, Object> condition) throws Exception;
	/**
	 * 根据查询条件返回学生有效的手机号码
	 */
	String getMoblieBySelect(Map<String, Object> condition) throws Exception;
	/**
	 * 根据查询条件，返回未分配学生分页
	 * @param objPage
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	Page getPredistributionPage(Page objPage, Map<String, Object> condition)
			throws Exception;
	/**
	 * 处理教学点预分配申请
	 * @param enrollids
	 * @return
	 */
	Map<String, Object> handleApplyDistribute(String enrollids);
	/**
	 * 教学点撤销预分配申请
	 * @param resourceid
	 * @return
	 */
	Map<String, Object> handleDeleteApplyDistribute(String resourceid);
	/**
	 * 审核预分配
	 * @param resourceid 
	 * @param auditFlag 审核标识：1-通过/2-不通过
	 * @return
	 */
	Map<String, Object> handleApplyDistributeResult(String resourceid,
			String auditFlag);
}
