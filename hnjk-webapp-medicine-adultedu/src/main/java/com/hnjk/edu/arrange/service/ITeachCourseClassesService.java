package com.hnjk.edu.arrange.service;

import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachCourseClasses;

public interface ITeachCourseClassesService extends IBaseService<TeachCourseClasses>{

	//通过条件查询返回排课集合
	Page findTeachCourseByHql(Map<String, Object> condition, Page objPage) throws ServiceException;
}
