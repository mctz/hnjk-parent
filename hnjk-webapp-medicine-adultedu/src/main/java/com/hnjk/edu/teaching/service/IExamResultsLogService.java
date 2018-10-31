package com.hnjk.edu.teaching.service;

import java.util.List;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.ExamResultsLog;

/**
 * 成绩导入导出日志ServiceInterface
 * @author luof
 *
 */
public interface IExamResultsLogService extends IBaseService<ExamResultsLog>{
	/**
	 * 根据考试批次及日志类型查找成绩导入导出日志
	 * @param examSubId
	 * @param optionType
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResultsLog> findExamResultsLogByExamSubIdAndOptionType(String examSubId,String optionType) throws ServiceException;
	/**
	 * 根据考试批次查找成绩导入导出日志
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	public List<ExamResultsLog> findExamResultsLogByExamSubId(String examSubId)throws ServiceException;
	
}
