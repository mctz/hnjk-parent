package com.hnjk.edu.evaluate.service;

import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.evaluate.model.Questionnaire;

public interface IQuestionnaireService extends IBaseService<Questionnaire>{
	/**
	 * 根据条件查找问卷
	 * @param values
	 * @return list
	 * @throws ServerException
	 */
	List<Questionnaire> findQuestionnaire(Map<String, Object> values)
			throws ServerException;
	/**
	 * 根据条件查找问卷
	 * @param condition
	 * @param objPage
	 * @return page
	 * @throws ServerException
	 * @throws Exception
	 */
	Page findQuestionnairePage(Map<String, Object> condition, Page objPage)
			throws ServerException, Exception;
	/**
	 * 查找问卷
	 * @param classesid
	 * @param studentInfoid
	 * @param returnFlag
	 * @return list
	 * @throws ServerException
	 */
	List<Questionnaire> getQuestionnaire(String classesid,
			String studentInfoid, String returnFlag) throws ServerException;
	/**
	 * 批量删除问卷
	 * @param values
	 * @throws ServerException
	 */
	void batchDeleteQuestionnaire(Map<String, Object> values)
			throws ServerException;
	/**
	 * 查找问卷
	 * @param values
	 * @return list
	 * @throws ServiceException
	 */
	List<Questionnaire> findQuestionnaireList(Map<String, Object> values)
			throws ServiceException;
	/**
	 * 取消问卷
	 * @param resourceids
	 * @param operate
	 * @return true/false
	 * @throws Exception
	 */
	boolean cancelQuestionnaire(String resourceids, String operate) throws Exception;
	/**
	 * 批量修改问卷起止时间
	 * @param resourceids
	 * @param startDate
	 * @param endDate
	 * @return true/false
	 * @throws Exception
	 */
	boolean editQuestionnaireTime(String resourceids, String startDate,
			String endDate) throws Exception;
	/**
	 * 更新问卷
	 * @param resourceids
	 * @param teachType
	 * @return
	 * @throws Exception
	 */
	boolean updateQuestionnaire(String resourceids, String teachType)
			throws Exception;

}
