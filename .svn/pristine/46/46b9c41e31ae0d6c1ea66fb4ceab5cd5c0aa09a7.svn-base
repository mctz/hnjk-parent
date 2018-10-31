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
import com.hnjk.edu.arrange.model.ArrangeTemplate;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.service.IArrangeTemplateService;
import com.hnjk.edu.util.Condition2SQLHelper;

@Transactional
@Service("arrangeTemplateService")
public class ArrangeTemplateServiceImpl extends BaseServiceImpl<ArrangeTemplate> implements IArrangeTemplateService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public Page findArrangeTemplateByHql(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+ArrangeTemplate.class.getSimpleName()+" c where c.isDeleted=:isDeleted");
		values.put("isDeleted", 0);
		if(condition.containsKey("branchSchool")){
			hql.append(" and c.schoolCalendar.unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("branchSchool"));
		}
		if(condition.containsKey("schoolCalendarid")){
			hql.append(" and c.schoolCalendar.resourceid=:schoolCalendarid ");
			values.put("schoolCalendarid", condition.get("schoolCalendarid"));
		}
		if(condition.containsKey("templateName")){
			hql.append(" and c.templateName=:templateName ");
			values.put("templateName", condition.get("templateName"));
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@Override
	public List<ArrangeTemplate> findTemplateListByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append(" from "+ArrangeTemplate.class.getSimpleName()+" at where isDeleted=:isDeleted");
		values.put("isDeleted", 0);
		if(condition.containsKey("brSchoolid")){
			sql.append(" and at.schoolCalendar.unit.resourceid =:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("yearid")){
			sql.append(" and at.schoolCalendar.yearInfo.resourceid =:yearid");
			values.put("yearid", condition.get("yearid"));
		}
		if(condition.containsKey("term")){
			sql.append(" and at.schoolCalendar.term =:term");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("schoolCalendarid")){
			sql.append(" and at.schoolCalendar.resourceid =:resourceid");
			values.put("resourceid", condition.get("schoolCalendarid"));
		}
		sql.append(" and at.schoolCalendar.status =:status");
		values.put("status", 1);
		sql.append(" order by at.templateName ");
		return findByHql(sql.toString(),values);
	}

	@Override
	public String constructOptions(Map<String, Object> condition,String defaultValue) throws ServiceException {
		StringBuffer templateOption = new StringBuffer("<option value=''></option>");
		List<ArrangeTemplate> templateList = findTemplateListByCondition(condition);
		if(null != templateList && templateList.size()>0){
			for(ArrangeTemplate t : templateList){
				if(t.getResourceid().equals(defaultValue)){
					templateOption.append("<option selected='selected' value='"+t.getResourceid()+"'");				
					templateOption.append(">"+t.getTemplateName()+"</option>");
				}else {
					templateOption.append("<option value='"+t.getResourceid()+"'");				
					templateOption.append(">"+t.getTemplateName()+"</option>");
				}
			}
		}
		return templateOption.toString();
	}
}
