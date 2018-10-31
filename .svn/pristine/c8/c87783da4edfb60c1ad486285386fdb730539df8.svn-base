package com.hnjk.edu.teaching.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.teaching.model.CheckOpenCourse;
import com.hnjk.edu.teaching.model.CourseTeacherCl;
import com.hnjk.edu.teaching.service.ICourseStatusClService;
import com.hnjk.security.model.User;

/**
 * 课程-教师-班级 接口实现.
 * <code>CourseServiceImpl</code><p>
 */
@Transactional
@Service("courseStatusClService")
public class CourseStatusClServiceImpl extends BaseServiceImpl<CourseTeacherCl> implements ICourseStatusClService{

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public List<Map<String, Object>> findTeacherByCondition(Map<String, Object> param) {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("select cl.orgunitid brSchoolid,g.gradename,cl.classicid,cl.teachingtype,cl.majorid,cs.plancourseid,ct.courseid,"
				+ "u.resourceid teacherid,u.cnname from edu_teach_courseteachercl ct");
		sb.append(" join hnjk_sys_users u on u.resourceid=ct.teacherid");
		sb.append(" join edu_roll_classes cl on cl.resourceid=ct.classesid");
		sb.append(" join edu_base_grade g on g.resourceid=cl.gradeid");
		sb.append(" join edu_teach_coursestatus cs on cs.resourceid=ct.coursestatusid and cs.schoolids=cl.orgunitid");
		sb.append("  where ct.isdeleted=0 and cs.isdeleted=0 and cs.isopen='Y'");
		if(param.containsKey("brSchoolid")){
			sb.append(" and cl.orgunitid=:brSchoolid");
		}
		if(param.containsKey("gradeid")){
			sb.append(" and cl.gradeid=:gradeid");
		}
		if(param.containsKey("classicid")){
			sb.append(" and cl.classicid=:classicid");
		}
		if(param.containsKey("teachingType")){
			sb.append(" and cl.teachingType=:teachingType");
		}
		if(param.containsKey("majorid")){
			sb.append(" and cl.majorid=:majorid");
		}
		if(param.containsKey("courseid")){
			sb.append(" and ct.courseid=:courseid");
		}
		sb.append(" group by cl.orgunitid,g.gradename,cl.classicid,cl.teachingtype,cl.majorid,cs.plancourseid,ct.courseid,u.resourceid,u.cnname order by g.gradename desc");
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		try {
			resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> findCourseAndClasses(Map<String, Object> param) throws ServiceException{
		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		StringBuffer sb  = new StringBuffer();
		sb.append(" select cl.teacherid,cl.classesid,cl.courseid,cs.teachtype									");
		sb.append(" from edu_teach_courseteachercl cl                                                           ");
		sb.append(" join edu_teach_coursestatus cs on cs.resourceid = cl.coursestatusid and cs.isdeleted = 0    ");
		sb.append("  and cs.isopen = 'Y' and cs.openstatus = 'Y' and cs.teachtype = 'faceTeach'                 ");
		sb.append(" where cl.isdeleted = 0 and cl.lecturerid is not null                                        ");
		sb.append("  and substr(cs.term,0,4)=:firstYear  and substr(cs.term,7,1) =:term                             ");
		try {
			resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultList;
	}
	
	@Override
	public List<Map<String,Object>> findCourseTeacherCl(Map<String, Object> param) throws Exception{
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		StringBuffer sb = new StringBuffer();
		sb.append(" select  cl.resourceid,cl.lecturerid,cl.courseid,cl.classesid,co.term,co.updateterm,cs.teachtype from edu_teach_courseteachercl cl                                      ");
		sb.append(" join EDU_TEACH_CHECKOPENCOURSE co on co.coursestatusid = cl.coursestatusid   and co.checkstatus = 'Y'      ");
		sb.append(" join edu_teach_coursestatus cs on cs.resourceid = cl.coursestatusid                ");
		sb.append(" where cl.isdeleted = 0 and co.isdeleted = 0                                        ");
		if(param.containsKey("flag")){
			sb.append(" and co.operate =:flag  ");
			if("cancel".equalsIgnoreCase(param.get("flag").toString())){
				if(param.containsKey("courseStatusTerm")){
					sb.append(" and co.term=:courseStatusTerm ");
				}
			}else{
				if(param.containsKey("courseStatusTerm")){
					sb.append(" and co.updateterm=:courseStatusTerm ");
				}
			}
		}
		if(param.containsKey("courseStatusid")){
			sb.append(" and co.coursestatusid=:courseStatusid ");
		}
		if(param.containsKey("courseType")){
			sb.append(" and cs.teachtype=:courseType ");
		}
		sb.append(" group by  cl.lecturerid,cl.courseid,cl.classesid,co.term,co.updateterm,cs.teachtype,cl.resourceid ");
		resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), param);
		return resultList;
	}
	
	@Override
	public List<CourseTeacherCl> findCourseTeacherCl_new(Map<String, Object> param) throws ServiceException{
		String hql = " from "+CourseTeacherCl.class.getSimpleName()+ " cl where  "
				+ "  cl.lecturerid is not null and cl.courseStatusId.isDeleted=0 ";
		if(param.containsKey("teachType")){
			hql+=" and cl.courseStatusId.teachType=:teachType ";
		}
		if(param.containsKey("courseType")){
			hql+=" and cl.courseStatusId.teachType=:courseType ";
		}
		if(param.containsKey("firstYear")){
			hql+=" and substr(cl.courseStatusId.term,0,4)=:firstYear ";
		}
		if(param.containsKey("term")){
			hql+=" and substr(cl.courseStatusId.term,7,1)=:term ";
		}
		if(param.containsKey("brSchoolid")){
			hql+=" and cl.classesId.brSchool.resourceid=:brSchoolid ";
		}
		if(param.containsKey("courseid")){
			hql+=" and cl.courseid.resourceid=:courseid ";
		}
		List<CourseTeacherCl> returnList = findByHql(hql, param);
		return returnList;
	}
	
}
