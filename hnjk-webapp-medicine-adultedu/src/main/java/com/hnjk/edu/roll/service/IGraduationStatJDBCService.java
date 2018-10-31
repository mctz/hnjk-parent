package com.hnjk.edu.roll.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;

public interface IGraduationStatJDBCService {
	/**
	 * 毕业信息统计
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statGraduationInfo(Map <String,Object> condition);
	/**
	 * 自定义导出列表
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> exportWithCustom(Map<String, Object> condition);
	/**
	 * 学位统计表导出
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> exportDegreeStat(Map<String, Object> condition);
	/**
	 * 返回某一字段的distinct集合
	 * @param propertyName 数据库字段名
	 * @return
	 */
	public List<String> getSingleDistinctPropertyValue(String propertyName,String order);
	/**
	 * 学籍卡打印
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> studentRollCardInfoList(Map<String, Object> condition) throws Exception;
	
	public List<Map<String, Object>> studentCardInfo(Map<String, Object> condition,String name) throws Exception;
	
	/**
	 * 学籍卡打印_学籍信息用
	 * @param condition
	 * @return
	 */
	public List<Map<String, Object>> studentRollCardInfoList_stu(Map<String, Object> condition) throws Exception;
	
	//学院2016修改
	public List<Map<String, Object>> cascadeMajorToCourse(Map<String,Object> param)throws ServiceException;

	/**
	 * 级联  年级-专业
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> cascadeGradeToMajor(Map<String,Object> param)throws ServiceException;
	/**
	 * 级联  层次-专业
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> cascadeGradeToMajor1(Map<String,Object> param)throws ServiceException;
	
	/**
	 * 级联  专业-班级
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> cascadeMajorToClasses(Map<String,Object> param)throws ServiceException;
	/**
	 * 级联  层次-班级
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	public List<Map<String, Object>> cascadeMajorToClasses1(Map<String,Object> param)throws ServiceException;
	
	/**
	 * 毕业统计
	 * @param condition
	 * @return
	 */
	public Page statisticalGraduation(Map<String, Object> condition,Page objPage);
	
	/**
	 * 毕业统计导出
	 * @param condition
	 * @return
	 */
	public List<Map<String,Object>> statisticalGraduationForExport(Map<String, Object> condition);
	public List<Map<String,Object>> getStudetnRegistryForm(Map<String, Object> condition) throws ServiceException;
	public List<Map<String,Object>> getStudetnResumes(Map<String, Object> condition) throws ServiceException;
}
