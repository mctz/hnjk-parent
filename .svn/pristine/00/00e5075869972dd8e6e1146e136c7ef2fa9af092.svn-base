package com.hnjk.edu.teaching.service;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseExamination;

import java.util.Map;

/**
 * 排考服务接口
 * @author msl
 * @since 2018-05-21
 */
public interface ITeachingPlanCourseExaminationService extends IBaseService<TeachingPlanCourseExamination> {

	/**
	 * 通过查询条件返回Page集合
	 * @param condition
	 * @return
	 */
	Page findPageByCondition(Map<String, Object> condition,Page objPage) throws WebException;

	/**
	 * 导入排考结果
	 *
	 * @param condition
	 * @param filePath
	 * @return
	 */
	Map<String,Object> importExaminationResult(Map<String, Object> condition, String filePath) throws WebException;
}
