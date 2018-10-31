package com.hnjk.edu.arrange.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.SchoolCalendar;

public interface ISchoolCalendarService extends IBaseService<SchoolCalendar> {
	
	//通过条件查询返回院历集合
	Page findSchoolCalendarByHql(Map<String, Object> condition, Page objPage) throws ServiceException;

	//通过条件查询返回院历集合
	List<SchoolCalendar> findSCalendarListByCondition(Map<String, Object> condition);;
		
	String constructOptions(Map<String, Object> condition, String defaultValue);;
}
