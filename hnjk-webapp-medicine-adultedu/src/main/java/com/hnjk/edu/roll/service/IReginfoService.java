package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.ExportRecruitPlan;
import com.hnjk.edu.roll.model.Reginfo;
import com.hnjk.edu.roll.model.StudentInfo;

/**
 * <code>IReginfoService</code><p>
 * 新生注册服务接口.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-26 下午03:11:54
 * @see 
 * @version 2.0
 */
public interface IReginfoService extends IBaseService<Reginfo> {
	/**
	 * 保存集合
	 * @param regList
	 * @deprecated 请 使用 <code>regStudentInfo</code> 方法
	 */
	void saveRegInfoList(List<Reginfo> regList) throws ServiceException;
		
	/**
	 * 统计符合注册条件的学生
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<EnrolleeInfo> countRegisterStudent(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 注册
	 * @param enrolleeInfoIds
	 * @throws ServiceException
	 * @return 返回注册成功的学生列表
	 */
	 List<StudentInfo>  doRegister(String[] enrolleeInfoIds) throws ServiceException;
	 
	 /**
	  * 预约注册成功的新生首学期课程
	  * @param registeredStus
	  * @return
	  * @throws ServiceException
	  */
	 int doRegisterStuFirstTermCourse(List<StudentInfo> registeredStus) throws ServiceException;
	
	 /**
	  * 学生自动注册
	  * @return
	  * @throws ServiceException
	  */
	 Map<String, Object> autoRegisterStudent()throws ServiceException; 
	 
	/**
	 * 查询分页
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findRegByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 新生导出时条件查询
	 * @param map
	 * @return
	 */
	List<Reginfo> findByHql(Map<String, Object> map);
	
	/**
	 * 导出入学资格审查学生列表LIST
	 * @param condition
	 * @return
	 */
	public List<ExportRecruitPlan> entranceFlagStudentExcel(Map<String, Object> condition);
    /**
     * 新增注销注册
     */
	public void deleteRegisterStuFirstTermCourse(StudentInfo studentInfo) throws ServiceException;	
	public void deleteUserOrRole(String studentId,String sysuserid) throws Exception;	
	
}
