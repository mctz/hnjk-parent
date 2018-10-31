package com.hnjk.edu.arrange.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.service.ISchoolCalendarService;
import com.hnjk.edu.util.Condition2SQLHelper;

@Transactional
@Service("schoolCalendarService")
public class SchoolCalendarServiceImpl extends BaseServiceImpl<SchoolCalendar> implements ISchoolCalendarService{

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public Page findSchoolCalendarByHql(Map<String, Object> condition, Page objPage) throws ServiceException {
			
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql = Condition2SQLHelper.getHqlByCondition(condition, values, hql, SchoolCalendar.class.getSimpleName()+" c ");
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public List<SchoolCalendar> findSCalendarListByCondition(Map<String, Object> condition) {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" from "+SchoolCalendar.class.getSimpleName()+" sc where isDeleted=0 ");
		if(condition.get("unitid")!=null && !condition.get("unitid").equals("")){
			sql.append(" and sc.unit.resourceid =:unitid ");
			values.put("unitid", condition.get("unitid"));
		}else if (condition.containsKey("brSchoolid")) {
			sql.append(" and sc.unit.resourceid =:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("yearid")){
			sql.append(" and sc.yearInfo.resourceid =:yearid");
			values.put("yearid", condition.get("yearid"));
		}
		if(condition.containsKey("term")){
			sql.append(" and sc.term =:term");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("status")){
			sql.append(" and sc.status =:status");
			values.put("status", condition.get("status"));
		}
		if(condition.containsKey("!status")){
			sql.append(" and (sc.status !=:status or sc.status is null)");
			values.put("status", condition.get("!status"));
		}
		if(condition.containsKey("openTerm")){
			sql.append(" and to_char(sc.yearInfo.firstYear)=:firstYear");
			values.put("firstYear", condition.get("openTerm").toString().substring(0, 4));
			sql.append(" and term=:term");
			values.put("term", condition.get("openTerm").toString().substring(6));
		}
		if(condition.containsKey("defaultValue")){
			sql.append(" or sc.resourceid =:defaultValue");
			values.put("defaultValue", condition.get("defaultValue"));
		}
		sql.append(" order by sc.yearInfo.firstYear desc,sc.term desc ");
		return findByHql(sql.toString(),values);
	}

	@Override
	public String constructOptions(Map<String, Object> condition,String defaultValue) {
		StringBuffer schoolCalendarOption = new StringBuffer("<option value=''></option>");
		condition.put("defaultValue", defaultValue);
		List<SchoolCalendar> schoolCalendars = findSCalendarListByCondition(condition);
		if(null != schoolCalendars && schoolCalendars.size()>0){
			for(SchoolCalendar t : schoolCalendars){
				if(t.getResourceid().equals(defaultValue)){
					schoolCalendarOption.append("<option selected='selected' value='"+t.getResourceid()+"'");				
					schoolCalendarOption.append(">"+t.getName()+"</option>");
				}else {
					schoolCalendarOption.append("<option value='"+t.getResourceid()+"'");				
					schoolCalendarOption.append(">"+t.getName()+"</option>");
				}
			}
		}
		return schoolCalendarOption.toString();
	}
}
