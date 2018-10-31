package com.hnjk.edu.teaching.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.ExamResultsLog;
import com.hnjk.edu.teaching.service.IExamResultsLogService;

/**
 * 成绩导入导出日志ServiceServiceImpl
 * @author luof
 *
 */
@Service("examResultsLogService")
@Transactional
public class ExamResultsLogServiceImpl extends BaseServiceImpl<ExamResultsLog> implements IExamResultsLogService{

	/**
	 * 根据考试批次及日志类型查找成绩导入导出日志
	 * @param examSubId
	 * @param optionType
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamResultsLog> findExamResultsLogByExamSubIdAndOptionType(String examSubId, String optionType) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		hql.append("from "+ExamResultsLog.class.getSimpleName()+" log where log.isDeleted=0");
		hql.append(" and log.examSubId=? and log.optionType=? order by log.fillinDate desc");
	
		return (List<ExamResultsLog>) this.exGeneralHibernateDao.findByHql(hql.toString(), examSubId,optionType);
	}
	/**
	 * 根据考试批次查找成绩导入导出日志
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamResultsLog> findExamResultsLogByExamSubId(String examSubId) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+ExamResultsLog.class.getSimpleName()+" log where log.isDeleted=0");
		hql.append(" and log.examSubId=? order by log.fillinDate desc");
		return (List<ExamResultsLog>) this.exGeneralHibernateDao.findByHql(hql.toString(), examSubId);
	}

}
