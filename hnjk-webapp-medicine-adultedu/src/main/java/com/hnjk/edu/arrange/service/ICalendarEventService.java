package com.hnjk.edu.arrange.service;

import java.util.List;
import java.util.Map;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.IBaseService;
import com.hnjk.edu.arrange.model.CalendarEvent;

public interface ICalendarEventService extends IBaseService<CalendarEvent> {
	
	//通过条件查询返回院历事件列表
	Page findSCalendarEventByHql(Map<String, Object> condition, Page objPage) throws ServiceException;
	
	List<CalendarEvent> finListByCondition(Map<String, Object> condition) throws ServiceException;

	String constructOptions(Map<String, Object> map, String eventid);
}
