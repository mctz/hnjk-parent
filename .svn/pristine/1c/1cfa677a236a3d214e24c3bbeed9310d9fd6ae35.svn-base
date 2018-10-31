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
import com.hnjk.edu.arrange.model.ArrangeTemplateDetail;
import com.hnjk.edu.arrange.model.SchoolCalendar;
import com.hnjk.edu.arrange.service.IArrangeTemplateDetailService;
import com.hnjk.edu.arrange.service.IArrangeTemplateService;
import com.hnjk.edu.teaching.model.TeachingPlanCourseTimePeriod;

@Transactional
@Service("arrangeTemplateDetailService")
public class ArrangeTemplateDetailServiceImpl extends BaseServiceImpl<ArrangeTemplateDetail> implements IArrangeTemplateDetailService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public Page findArrangeTemplateDetailByHql(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+ArrangeTemplateDetail.class.getSimpleName()+" c where c.isDeleted = :isDeleted ");
		values.put("isDeleted", 0);

		if(condition.containsKey("yearInfoId")){
			hql.append(" and c.schoolCalendar.yearInfo.resourceid=:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and c.schoolCalendar.term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("branchSchool")){
			hql.append(" and c.schoolCalendar.unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("branchSchool"));
		}
		if(condition.containsKey("schoolCalendarid")){
			hql.append(" and c.schoolCalendar.resourceid=:schoolCalendarid ");
			values.put("schoolCalendarid", condition.get("schoolCalendarid"));
		}
		if(condition.containsKey("templateName")){
			hql.append(" and c.templateName like :templateName ");
			values.put("templateName", "%"+condition.get("templateName")+"%");
		}
					
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

}
