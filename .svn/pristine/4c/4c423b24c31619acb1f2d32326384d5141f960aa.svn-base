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
import com.hnjk.edu.arrange.model.TeachCourse;
import com.hnjk.edu.arrange.service.ITeachCourseService;
import com.hnjk.edu.util.Condition2SQLHelper;

@Transactional
@Service("teachCourseService")
public class TeachCourseServiceImpl extends BaseServiceImpl<TeachCourse> implements ITeachCourseService{
	
	
	@Override
	public Page findTeachCourseByHql(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql = Condition2SQLHelper.getHqlByCondition(condition, values, hql, TeachCourse.class.getSimpleName()+" c ");
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public List<TeachCourse> findTeachCourseByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql = Condition2SQLHelper.getHqlByCondition(condition, values, hql, TeachCourse.class.getSimpleName());
		if(condition.containsKey("teachCourseid")){
			hql.append(" and teachCourseid =:teachCourseid ");
			values.put("teachCourseid", condition.get("teachCourseid"));
		}
		if(condition.containsKey("addSql")){
			hql.append(" "+condition.get("addSql")+" ");
		}
		hql.append(" order by unit.unitName,course.courseName,openTerm");
		return findByHql(hql.toString(), values);
	}
	
	@Override
	public String constructOptions(Map<String, Object> condition,String defaultValue) throws ServiceException {
		StringBuffer teachCourseOption = new StringBuffer("<option value=''></option>");
		List<TeachCourse> teachCourseList = findTeachCourseByCondition(condition);
		if(null != teachCourseList && teachCourseList.size()>0){
			for(TeachCourse t : teachCourseList){
				if(t.getResourceid().equals(defaultValue)){
					teachCourseOption.append("<option selected='selected' value='"+t.getResourceid()+"'");				
					teachCourseOption.append(">"+t.getTeachingClassname()+"</option>");
				}else {
					teachCourseOption.append("<option value='"+t.getResourceid()+"'");				
					teachCourseOption.append(">"+t.getTeachingClassname()+"</option>");
				}
			}
		}
		return teachCourseOption.toString();
	}

	@Override
	@Transactional(readOnly=true)
	public String getTeachingCode(String brSchoolid, String openTerm) throws ServiceException {
		String suffix = "";
		List result = exGeneralHibernateDao.findByHql(" from "+TeachCourse.class.getSimpleName()+" where unit.resourceid='"+brSchoolid+"' and isDeleted=0 and teachingCode like '"+openTerm+ "___' order by teachingCode desc");
		if (result.size() == 0) {
			suffix = "000";
		} else {
			TeachCourse ei = (TeachCourse) result.get(0);
			suffix = ei.getTeachingCode().substring(openTerm.length());
			if ("999".equals(suffix)) {
				throw new ServiceException("没有空余的教学班号了！");
			}
		}
		return openTerm+this.increase(suffix);
	}
	
	@Override
	public String increase(String num) {
		char[] chars = num.toCharArray();
		for (int i = num.length() - 1; i >= 0; i--) {
			if (chars[i] == '9') {
				chars[i] = '0';
			} else {
				chars[i]++;
				break;
			}
		}
		return new String(chars);
	}
}
