package com.hnjk.edu.evaluate.service;

import java.rmi.ServerException;
import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.evaluate.model.Questionnaire;
import com.hnjk.edu.evaluate.model.QuestionnaireBatch;
import com.hnjk.edu.teaching.model.CourseTeacherCl;

public interface IQuestionnaireBatchService extends IBaseService<QuestionnaireBatch>{

	/**
	 * 根据条件查找教评批次
	 * @param condition
	 * @param objPage
	 * @return page
	 * @throws ServiceException
	 */
	Page findQuestionnaireBatch(Map<String, Object> condition, Page objPage)
			throws ServiceException;
	/**
	 * 保存批次
	 * @param qb
	 * @param yearId
	 * @return true/false
	 * @throws ServiceException
	 */
	boolean saveQuestionnaireBatch(QuestionnaireBatch qb, String yearId)
			throws ServiceException;

	/**
	 * 删除批次
	 * @param resourceids
	 * @return true/false
	 * @throws ServiceException
	 */
	boolean deleteQuestionnaireBatch(String resourceids)
			throws ServiceException;
	/**
	 * 发布批次
	 * @param resourceids
	 * @return true/false
	 * @throws ServiceException
	 * @throws Exception
	 */
	boolean publishQuestionnaireBatch(String resourceids,String brSchoolid)
			throws ServiceException, Exception;
	/**
	 * 创建问卷
	 * @param qlist
	 * @param param
	 * @param qb
	 * @param cl
	 * @throws ServerException
	 * @throws ServiceException
	 */
	void createQuestionnaire(List<Questionnaire> qlist,
			Map<String, Object> param, QuestionnaireBatch qb, CourseTeacherCl cl)
			throws ServerException, ServiceException;
	/**
	 * 查找批次list
	 * @param condition
	 * @return list
	 * @throws ServiceException
	 */
	List<QuestionnaireBatch> findQuestionnaireBatchList(
			Map<String, Object> condition) throws ServiceException;
	/**
	 * 更新问卷
	 * @param qlist
	 * @param param
	 * @param qb
	 * @param cl
	 * @throws ServiceException
	 * @throws ServerException
	 */
	void updateQuestionnaire(List<Questionnaire> qlist,
			Map<String, Object> param, QuestionnaireBatch qb, CourseTeacherCl cl)
			throws ServiceException, ServerException;
}
