package com.hnjk.edu.arrange.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.TeachCourse;

public interface ITeachCourseService extends IBaseService<TeachCourse>{

	/**
	 * 根据条件查询返回排课页
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	Page findTeachCourseByHql(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	/**
	 * 根据条件查询教学班
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	List<TeachCourse> findTeachCourseByCondition(Map<String, Object> condition)throws ServiceException;


	/**
	 * 根据查询条件，返回教学班select选项
	 * @param condition
	 * @param defaultValue
	 * @return
	 * @throws ServiceException
	 */
	String constructOptions(Map<String, Object> condition, String defaultValue) throws ServiceException;;

	/**
	 * 获取教学班号流水号
	 * @param brSchoolid
	 * @param openTerm
	 * @return
	 * @throws ServiceException
	 */
	String getTeachingCode(String brSchoolid, String openTerm) throws ServiceException;

	/**
	 * 将num所代表的数值加1，并返回结果的与num相同的位数
	 * 
	 * @param num 数值
	 * @return String
	 */
	String increase(String num);
}
