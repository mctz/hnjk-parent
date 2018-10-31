package com.hnjk.edu.arrange.service;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.model.TeachCourseDetail;

public interface ITeachCourseDetailService extends IBaseService<TeachCourseDetail>{

	/**
	 * 查询排课结果
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findArrangeCourseByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;

	/**
	 * 查询排课详情
	 * @param condition
	 * @param objPage
	 * @return
	 */
	Page findArrangeCourseDetailsByCondition(Map<String, Object> condition,Page objPage) throws ServiceException;

	/**
	 * 查询排课详情
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachCourseDetail> findArrangeCourseListByHql(Map<String, Object> condition) throws ServiceException;

	/**
	 * 查询排课详情，用于冲突检测
	 * @param condition
	 * @return
	 */
	List<Map<String, Object>> findCourseDetailConflictMapByCondition(Map<String, Object> condition) throws ServiceException;

}
