package com.hnjk.edu.evaluate.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.evaluate.model.StuQuestionnaire;

public interface IStuQuestionnaireService extends IBaseService<StuQuestionnaire>{
	/**
	 * 查找学生提交的问卷
	 * @param classesid
	 * @param studentInfoid
	 * @return list
	 */
	List<StuQuestionnaire> findByClassesId(String classesid,
			String studentInfoid);

	/**
	 * 保存学生提交的问卷
	 * @param studentInfoid
	 * @param commentlabel
	 * @param resourceids
	 * @param questionnaireid
	 * @return true/false
	 * @throws Exception
	 */
	boolean stuQuestionnaireSave(String studentInfoid, String commentlabel,
			String resourceids, String questionnaireid) throws Exception;

	/**
	 * 教评结果
	 * @param condition
	 * @param objPage
	 * @return page
	 * @throws Exception
	 */
	Page findStuQuestionnairePage(Map<String, Object> condition, Page objPage)
			throws Exception;

	/**
	 * 单个老师的教评结果
	 * @param condition
	 * @return list<Map>
	 * @throws Exception
	 */
	List<Map<String, Object>> findSqnCourse(Map<String, Object> condition)
			throws Exception;
	/**
	 * 查找学生提交的问卷列表
	 * @param condition
	 * @param objPage
	 * @return page
	 * @throws Exception
	 */
	Page findSqnListByQn(Map<String, Object> condition, Page objPage)
			throws Exception;

}
