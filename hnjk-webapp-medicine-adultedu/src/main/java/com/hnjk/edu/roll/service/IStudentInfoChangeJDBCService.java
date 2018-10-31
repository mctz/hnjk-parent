package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.rao.dao.helper.Page;

public interface IStudentInfoChangeJDBCService {
	/**
	 * 学籍异动
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statStudentInfoChange(Map <String,Object> condition);
	/**
	 * 按学习类型统计学籍异动条数 
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statStudentChangeNumByLearnStyle(Map <String,Object> condition);
	/**
	 * 按专业统计学籍异动条数
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByMajor(Map<String, Object> condition); 
	/**
	 * 按年级统计学籍异动条数 
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByGrade(Map<String, Object> condition);
	/**
	 * 按学习中心统计学籍异动条数
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByBrSchool(Map<String, Object> condition) ;
	/**
	 * 按层次统计学籍异动条数
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByClassic(Map<String, Object> condition) ;
	/**
	 * 按学号统计学籍异动条数
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByStyNo(Map<String, Object> condition);
	/**
	 * 按姓名统计学籍异动条数 
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByName(Map<String, Object> condition) ;
	/**
	 * 按异动类型统计学籍异动条数 
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentChangeNumByChangeStyle(Map<String, Object> condition);
	/**
	 * 得到所有的学籍异动记录中的学籍id
	 * @return
	 */
	public List<String> getAllStudentInfoId();
	/**
	 * 得到无关联基本信息的学籍信息id 无关学籍异动
	 * @return
	 */
	public List<String> getStudentInfoIdWithoutBaseInfo() ;
	/**
	 * 得到关联基本信息的学籍信息certNum studentStatus 无关学籍异动
	 * @return
	 */
	public List<Map<String,Object>> getStudentInfoStatusWithBaseInfo() ;
	/**
	 * 得到学籍信息统计
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> statStudentInfo(Map<String, Object> condition);
	/**
	 * 学籍信息维护日志查询
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getStudentInfoChangeHistory(Map<String, Object> condition) ;
	/**
	 * 毕业审核学籍信息
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getStudentInfoForGraduateAudit(Map<String, Object> condition);
	/**
	 * 学位审核学籍信息
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getStudentInfoForDegreeAudit(Map<String, Object> condition);
	/**
	 * 毕业审核条数
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getNumForGraduateAudit(Map<String, Object> condition); 
	/**
	 * 学位审核课程信息
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> getInfoForDegreeAudit(Map<String, Object> condition);
	/**
	 * 获取导出数据
	 * @param condition
	 * @return
	 * @throws WebException
	 */
	public List getStudentInfoToExport(Map<String,Object> condition) throws WebException;
	/**
	 * 获取学生学籍状态修改历史
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	public Page getStudentInfoStatusChangeHistory( Map<String, Object> condition,Page page) throws ServiceException; 
	/**
	 * 获取学生学籍状态修改历史列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> getStudentInfoStatusChangeHistoryList(Map<String, Object> condition) throws ServiceException;
	/**
	 * 通过考生号，姓名 身份证 专业获取学籍号
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String,Object>> getStudentInfoWithEnrolleeCodeEct(Map<String, Object> condition) throws ServiceException;
	/**
	 * 根据前缀获取学号最大值
	 * @param suffix
	 * @return
	 * @throws ServiceException
	 */
	public String genMaxStudyNoWithSuffix(String prefix) throws ServiceException;
	/**
	 * 根据前缀获取学号最大值
	 * @param suffix
	 * @return
	 * @throws ServiceException
	 */
	public String genMaxStudyNoWithSuffix2(String prefix) throws ServiceException;
	/**
	 * 根据前缀获取学号最大值
	 * @param suffix
	 * @return
	 * @throws ServiceException
	 */
	public String getMaxStudyNoWithSuffix(String prefix) throws ServiceException;
	
	public String genMaxStudyNoWithSuffixGXYKD(String prefix) throws ServiceException;
	
	public String getMaxStudyNoWithSuffixGXYKD(String prefix) throws ServiceException;
	
	/**
	 * 根据前缀获取安徽医当前最大的学号
	 * @param prefix
	 * @return
	 * @throws ServiceException
	 */
	public String getMaxStudyNoWithSuffixAHYKD(String prefix) throws ServiceException;
	public List<Map<String, Object>> countEdumanager(String type);
	List<Map<String, Object>> countwomanEdumanager(String type);
	List<Map<String, Object>> countDegreeEdumanager();
}
