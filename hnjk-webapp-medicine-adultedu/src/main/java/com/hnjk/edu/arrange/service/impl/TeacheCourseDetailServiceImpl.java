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
import com.hnjk.edu.arrange.model.TeachCourseDetail;
import com.hnjk.edu.arrange.service.ITeachCourseDetailService;
import com.hnjk.edu.util.Condition2SQLHelper;

@Transactional
@Service("teachCourseDetailService")
public class TeacheCourseDetailServiceImpl extends BaseServiceImpl<TeachCourseDetail> implements ITeachCourseDetailService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Override
	public Page findArrangeCourseByCondition(Map<String, Object> condition,Page objPage) throws ServiceException{
		// TODO Auto-generated method stub
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer();
		hql = Condition2SQLHelper.getHqlByCondition(condition, values, hql, TeachCourseDetail.class.getSimpleName()+" c ");
		
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@Override
	public Page findArrangeCourseDetailsByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		// TODO Auto-generated method stub
		Map<String,Object> values =  new HashMap<String, Object>();
		
		StringBuffer hql = new StringBuffer(" from "+TeachCourseDetail.class.getSimpleName()+" c where isDeleted=:isDeleted");
		values.put("isDeleted", 0);
		if(condition.containsKey("yearId")){
			hql.append(" and c.teachCourse.yearInfo.resourceid=:yearId ");
			values.put("yearId", condition.get("yearId"));
		}
		if(condition.containsKey("term")){
			hql.append(" and c.teachCourse.term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("brSchoolid")){
			hql.append(" and c.teachCourse.unit.resourceid=:brSchoolid ");
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("classicid")){
			hql.append(" and c.teachCourse.classic.resourceid=:classicid ");
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("teachingType")){
			hql.append(" and c.teachCourse.teachingtype=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("teachCourseid")){
			hql.append(" and c.teachCourse.resourceid=:teachCourseid ");
			values.put("teachCourseid", condition.get("teachCourseid"));
		}
		if(condition.containsKey("openTerm")){
			hql.append(" and c.teachCourse.openTerm=:openTerm ");
			values.put("openTerm", condition.get("openTerm"));
		}
		if(condition.containsKey("courseId")){
			hql.append(" and c.teachCourse.course.resourceid=:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("status")){
			hql.append(" and c.teachCourse.status=:status ");
			values.put("status", condition.get("status"));
		}
		if(condition.containsKey("className")){
			hql.append(" and c.teachCourse.classNames like:className ");
			values.put("className", "%"+condition.get("className")+"%");
		}
		if(condition.containsKey("teacherName")){
			hql.append(" and c.teachCourse.teacherNames like:teacherName ");
			values.put("teacherName", "%"+condition.get("teacherName")+"%");
		}
		if(condition.containsKey("operatorName")){
			hql.append(" and c.operatorName like:operatorName ");
			values.put("operatorName", "%"+condition.get("operatorName")+"%");
		}
		if(ExStringUtils.isNotEmpty(objPage.getOrderBy())){			
			hql.append(" order by c."+objPage.getOrderBy().replace(",", ",c.") +" "+ objPage.getOrder());
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TeachCourseDetail> findArrangeCourseListByHql(Map<String, Object> condition) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+TeachCourseDetail.class.getSimpleName()+" where isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		if(condition.containsKey("brSchoolid")){//教学点
			hql += " and teachCourse.unit.resourceid =:brSchoolid ";
			values.put("brSchoolid", condition.get("brSchoolid"));
		}
		if(condition.containsKey("openTerm")){//上课学期
			hql += " and teachCourse.openTerm =:openTerm ";
			values.put("openTerm", condition.get("openTerm"));
		}
		if(condition.containsKey("teachCourseid")){//教学班
			hql += " and teachCourse.resourceid =:teachCourseid ";
			values.put("teachCourseid", condition.get("teachCourseid"));
		}
		if(condition.containsKey("courseid")){//课程
			hql += " and teachCourse.course.resourceid =:courseid ";
			values.put("courseid", condition.get("courseid"));
		}
		if(condition.containsKey("orderBy")){
			hql += " order by  "+condition.get("orderBy");
		}
		return (List<TeachCourseDetail>) exGeneralHibernateDao.findByHql(hql,values);
	}

	@Override
	public List<Map<String, Object>> findCourseDetailConflictMapByCondition(Map<String, Object> condition) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		List<Map<String, Object>> maps = null;
		hql.append(" select t.* from (");
		hql.append(" select d.teachcourseid,d.resourceid detailid,tc.classesid as conflict,cl.classesname as name,'class' type,d.days,d.weeks,d.timeperiodids from edu_arrange_detail d");
		hql.append(" left join edu_arrange_teachclasses tc on tc.teachcourseid=d.teachcourseid");
		hql.append(" left join edu_roll_classes cl on cl.resourceid= tc.classesid");
		hql.append(" where d.isdeleted=0 and tc.isdeleted=0 and cl.isdeleted=0");
		hql.append(" union");
		hql.append(" select d.teachcourseid,d.resourceid,d.teacherid,u.cnname,'teacher' type,d.days,d.weeks,d.timeperiodids from edu_arrange_detail d");
		hql.append(" left join hnjk_sys_users u on u.resourceid=d.teacherid");
		hql.append(" where d.isdeleted=0 and u.isdeleted=0");
		hql.append(" union");
		hql.append(" select d.teachcourseid,d.resourceid,d.classroomid,cr.classroomname,'room' type,d.days,d.weeks,d.timeperiodids from edu_arrange_detail d");
		hql.append(" left join edu_base_classroom cr on cr.resourceid=d.classroomid");
		hql.append(" where d.isdeleted=0 and cr.isdeleted=0) t");
		hql.append(" join edu_arrange_teachcourse tcourse on tcourse.resourceid=t.teachcourseid and tcourse.isdeleted=0");
		hql.append(" and t.conflict is not null and t.days is not null and t.weeks is not null and t.timeperiodids is not null");
		if(condition.containsKey("brSchoolid")){//教学点
			hql.append(" and tcourse.unitid='"+condition.get("brSchoolid")+"'");
		}
		if(condition.containsKey("ischeck")){//检查冲突
			hql.append(" where 1=1 ");
			if(condition.containsKey("notdetailid")){//编辑排课详情form
				hql.append(" and t.detailid!=:notdetailid");
				values.put("notdetailid", condition.get("notdetailid"));
			}
		}else {//根据条件查询不可排课时间
			hql.append(" where 1=2");
			if(condition.containsKey("classroomid")){//课室
				hql.append(" or t.type='room' and t.conflict=:classroomid");
				values.put("classroomid", condition.get("classroomid"));
			}
			if(condition.containsKey("teachCourseid")){//教学班（教学班下的所有班级）
				hql.append(" or t.type='class' and t.teachcourseid=:teachCourseid");
				values.put("teachCourseid", condition.get("teachCourseid"));
			}
			if(condition.containsKey("classesid")){//班级
				hql.append(" or t.type='class' and t.conflict=:classesid");
				values.put("classesid", condition.get("classesid"));
			}
			if(condition.containsKey("teacherids")){//教师
				hql.append(" or t.type='teacher' and t.conflict=:teacherids");
				values.put("teacherids", condition.get("teacherids"));
			}else if(condition.containsKey("teacherid")){
				hql.append(" or t.type='teacher' and t.conflict=:teacherid");
				values.put("teacherid", condition.get("teacherid"));
			}
		}
		//冲突检测优先级，班级>老师>课室
		hql.append(" order by decode(t.type, 'class', 1, 'teacher', 2, 'room', 3),t.conflict,t.days,t.weeks,t.timeperiodids");
		try {
			maps = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return maps;
	}
}
