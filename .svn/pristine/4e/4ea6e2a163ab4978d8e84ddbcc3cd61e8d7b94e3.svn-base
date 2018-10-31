package com.hnjk.edu.arrange.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.arrange.model.CalendarEvent;
import com.hnjk.edu.arrange.service.ICalendarEventService;

@Transactional
@Service("calendarEventService")
public class CalendarEventServiceImpl extends BaseServiceImpl<CalendarEvent> implements ICalendarEventService{
	
	@Override
	public Page findSCalendarEventByHql(Map<String, Object> condition, Page objPage) throws ServiceException {
		
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = getHqlByCondition(condition,values,CalendarEvent.class.getSimpleName());
		
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public List<CalendarEvent> finListByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = getHqlByCondition(condition,values,CalendarEvent.class.getSimpleName());
		if(condition.containsKey("addSql")){
			hql.append(" "+condition.get("addSql")+" ");
		}
		return (List<CalendarEvent>) exGeneralHibernateDao.findByHql(hql.toString(), condition);
	}
	
	private StringBuffer getHqlByCondition(Map<String, Object> condition,Map<String, Object> values, String simpleName) {
		StringBuffer hql = new StringBuffer(" from "+CalendarEvent.class.getSimpleName()+" c where c.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("resourceids")){
			hql.append(" and c.schoolCalendar.resourceid in(:yearInfoId) ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}else {
			if(condition.containsKey("yearInfoId")){
				hql.append(" and c.schoolCalendar.yearInfo.resourceid=:yearInfoId ");
				values.put("yearInfoId", condition.get("yearInfoId"));
			}
			if(condition.containsKey("term")){
				hql.append(" and c.schoolCalendar.term=:term ");
				values.put("term", condition.get("term"));
			}
			if(condition.containsKey("brSchoolid")){
				hql.append(" and c.schoolCalendar.unit.resourceid=:brSchoolid ");
				values.put("brSchoolid", condition.get("brSchoolid"));
			}
			if(condition.containsKey("schoolCalendarid")){
				hql.append(" and c.schoolCalendar.resourceid=:schoolCalendarid ");
				values.put("schoolCalendarid", condition.get("schoolCalendarid"));
			}
			if(condition.containsKey("name")){
				hql.append(" and c.name like :name");
				values.put("name", "%"+condition.get("name")+"%");
			}
			if(condition.containsKey("type")){
				hql.append(" and to_char(c.type) =:type or to_char(c.type2) =:type or to_char(c.type3) =:type");
				values.put("type", condition.get("type"));
			}
		}
		return hql;
	}

	@Override
	public String constructOptions(Map<String, Object> condition,String defaultValue) throws ServiceException {
		StringBuffer calendarEventOption = new StringBuffer("<option value=''></option>");
		List<CalendarEvent> calendarEventList = finListByCondition(condition);
		if(null != calendarEventList && calendarEventList.size()>0){
			for(CalendarEvent t : calendarEventList){
				if(t.getResourceid().equals(defaultValue)){
					calendarEventOption.append("<option selected='selected' value='"+t.getResourceid()+"'");				
					calendarEventOption.append(">"+t.getName()+"</option>");
				}else {
					calendarEventOption.append("<option value='"+t.getResourceid()+"'");				
					calendarEventOption.append(">"+t.getName()+"</option>");
				}
			}
		}
		return calendarEventOption.toString();
	}
}
