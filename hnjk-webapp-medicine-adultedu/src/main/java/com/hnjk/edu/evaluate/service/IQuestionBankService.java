package com.hnjk.edu.evaluate.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.evaluate.model.QuestionBank;

public interface IQuestionBankService extends IBaseService<QuestionBank>{
	/**
	 * 通过条件查找问卷题目，返回 page
	 * @param condition
	 * @param objPage
	 * @return page
	 */
	public Page findQuestionBankByCondition(Map<String, Object> condition,	Page objPage);
	/**
	 * 通过课程类型获取问题题目总分值
	 * @param courseType
	 * @return double
	 * @throws ServiceException
	 */
	double getFaceTotalScore(String courseType) throws ServiceException;
	/**
	 * 保存问卷题目
	 * @param qb
	 * @return true/false
	 * @throws ServiceException
	 */
	boolean saveQuestion(QuestionBank qb) throws ServiceException;
	/**
	 * 批量删除问卷题目
	 * @param resourceids
	 * @return  true/false
	 * @throws ServiceException
	 */
	boolean deleteQuestion(String resourceids) throws ServiceException;
	/**
	 * 根据查询条件查找所有问卷题目
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<QuestionBank> findQuestionBankAll(Map<String, Object> condition)
			throws ServiceException;

}
