package com.hnjk.edu.teaching.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.teaching.model.BachelorExamResults;
import com.hnjk.edu.teaching.model.ElectiveExamResults;

public interface IElectiveExamResultsService extends IBaseService<ElectiveExamResults>{
	
	/**
	 * 通过条件查询返回选修课成绩
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findPageByCondition(Map<String, Object> condition, Page objPage) throws ServiceException;
	List<ElectiveExamResults> findListByCondition(Map<String, Object> condition) throws ServiceException;
	
	/**
	 * 导入选修课成绩
	 * @param filePath
	 * @return
	 */
	Map<String, Object> importElectiveExamResults(String filePath);
	
	/**
	 * 根据学籍信息获取选修课成绩
	 * @param studentInfo
	 * @return
	 */
	List<ElectiveExamResults> studentExamResultsList(StudentInfo studentInfo);
	
	/**
	 * 查找选修课成绩信息
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> findElectiveExamResultsByCondition(Map<String, Object> condition);
	/**
	 * 获取考试人数
	 * @param examSubId
	 * @param courseId
	 * @return
	 */
	int getCountsByExamSubAndCourse(Map<String, Object> condition);
	
	/**
	 * 按查询条件更新打印日期
	 * @param condition
	 */
	void batchUpdatePrintTime(Map<String, Object> condition);
	
	String constructCourseOptions(Map<String, Object> condition,String defaultValue);
	
}
