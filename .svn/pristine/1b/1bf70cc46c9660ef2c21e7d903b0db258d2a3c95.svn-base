package com.hnjk.edu.arrange.service.impl;

import java.util.HashMap;
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
import com.hnjk.edu.arrange.model.TeachCourseClasses;
import com.hnjk.edu.arrange.service.ITeachCourseClassesService;

@Transactional
@Service("teachCourseClassesService")
public class TeachCourseClassesServiceImpl extends BaseServiceImpl<TeachCourseClasses> implements ITeachCourseClassesService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public Page findTeachCourseByHql(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+TeachCourseClasses.class.getSimpleName()+" c where c.isDeleted=0 and c.teachCourse.isDeleted=0 ");

		if(condition.containsKey("yearInfoId")){//年度
			hql.append(" and c.teachCourse.yearInfo.resourceid=:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){//学期
			hql.append(" and c.teachCourse.term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("branchSchool")){//教学点
			hql.append(" and c.teachCourse.unit.resourceid=:brSchoolId ");
			values.put("brSchoolId", condition.get("branchSchool"));
		}
		if(condition.containsKey("teachCourseid")){//教学班
			hql.append(" and c.teachCourse.resourceid=:teachCourseid ");
			values.put("teachCourseid", condition.get("teachCourseid"));
		}else if (condition.containsKey("teachCourseids")) {
			hql.append(" and c.teachCourse.resourceid in("+condition.get("teachCourseids")+") ");
		}
		if(condition.containsKey("teachingClassname")){//教学班名
			hql.append(" and c.teachCourse.teachingClassname=:teachingClassname ");
			values.put("teachingClassname", condition.get("teachingClassname"));
		}
		if(condition.containsKey("openTerm")){//开课学期
			hql.append(" and c.teachCourse.openTerm=:openTerm ");
			values.put("openTerm", condition.get("openTerm"));
		}
		if(condition.containsKey("classicid")){//层次
			hql.append(" and c.teachCourse.classic.resourceid=:classicid ");
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("teachingType")){//学习形式
			hql.append(" and c.teachCourse.teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("teachType")){//教学类型
			hql.append(" and c.teachCourse.teachType=:teachType ");
			values.put("teachType", condition.get("teachType"));
		}
		if(condition.containsKey("courseid")){//课程
			hql.append(" and c.course.resourceid=:courseid ");
			values.put("courseid", condition.get("courseid"));
		}
					
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){
			hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
}
