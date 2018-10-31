package com.hnjk.edu.arrange.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.model.TeachingWillingness;
import com.hnjk.edu.arrange.service.ITeachingWillingnessService;
import com.hnjk.edu.util.Condition2SQLHelper;

@Transactional
@Service("teachingWillingnessService")
public class TeachingWillingnessServiceImpl extends BaseServiceImpl<TeachingWillingness> implements ITeachingWillingnessService{

	@Override
	public Page findWillingnessByCondition(Map<String, Object> condition,Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+TeachingWillingness.class.getSimpleName()+" c where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("brSchoolid")){//教学点
			hql.append(" and c.teachCourse.unit.resourceid=:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("classicid")){//层次
			hql.append(" and c.teachCourse.classic.resourceid=:classicid ");
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("teachingType")){//学习方式
			hql.append(" and c.teachCourse.teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("openTerm")){//上课学期
			hql.append(" and c.teachCourse.openTerm=:openTerm ");
			values.put("openTerm", condition.get("openTerm"));
		}
		if(condition.containsKey("courseId")){//课程名称
			hql.append(" and c.teachCourse.course.resourceid=:courseid ");
			values.put("courseid", condition.get("courseId"));
		}
		if(condition.containsKey("teachCourseid")){//教学班
			hql.append(" and c.teachCourse.resourceid=:teachCourseid ");
			values.put("teachCourseid", condition.get("teachCourseid"));
		}
		if(condition.containsKey("teacherid")){//申请教师
			hql.append(" and c.proposer.resourceid=:teacherid ");
			values.put("teacherid", condition.get("teacherid"));
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

}
