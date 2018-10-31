package com.hnjk.edu.teaching.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.*;
import com.hnjk.edu.teaching.service.*;
import com.hnjk.edu.teaching.vo.TeachingPlanCourseCodeVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import edu.emory.mathcs.backport.java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 
 * <code>TeachingPlanCourseStatusServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2013-4-10 下午04:35:59
 * @see 
 * @version 1.0
 */
@Transactional
@Service("teachingPlanCourseStatusService")
public class TeachingPlanCourseStatusServiceImpl extends BaseServiceImpl<TeachingPlanCourseStatus> implements ITeachingPlanCourseStatusService {

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("checkOpenCourseService")
	private ICheckOpenCourseService checkOpenCourseService;
	
	@Override
	@Transactional(readOnly=true)
	public Page findTeachingPlanCourseStatusMapByConditionNew(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		if(condition.containsKey("openCourse")){// 开课页面
			sql = getOpenCourse(condition,param);
		}else{// 开课审核页面
			sql = getTeachingPlanCourseStatusSqlNew(condition,param);
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), param.toArray());
	}
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> findTeachingPlanCourseStatusMapByConditionNew(	Map<String, Object> condition) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql = getOpenCourse(condition,param);
		List<Map<String, Object>> planCourseList=new ArrayList<Map<String,Object>>();
		try {
			planCourseList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), param.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return planCourseList;
	}
	
	private StringBuilder getOpenCourse(Map<String, Object> condition, List<Object> param) {		
		StringBuilder sql = new StringBuilder();
		if(condition.containsKey("brSchoolid")){
			sql.append("select s.resourceid,pc.resourceid plancourseid,pc.planid,g.gradename,m.majorname majorname,cl.classicname,nvl(u.unitname,'"+condition.get("schoolname")+"') unitname,c.coursecode,c.coursename,s.schoolname,y.firstyear,");
		}else{
			sql.append("select s.resourceid,pc.resourceid plancourseid,pc.planid,g.gradename,m.majorname majorname,cl.classicname,nvl(u.unitname,'适用所有教学站') unitname,c.coursecode,c.coursename,s.schoolname,y.firstyear,");
		}
		//sql.append("select s.resourceid,pc.resourceid plancourseid,pc.planid,g.gradename,m.majorname,cl.classicname,nvl(s.schoolname,'适用所有教学站') unitname,c.coursecode,c.coursename,");
		sql.append(" p.schooltype teachingType,pc.term,gp.resourceid guiplanid,gp.gradeid,p.majorid,p.classicid,p.brschoolid,s.term courseterm,s.checkstatus checks,s.openstatus openstatus,s.isopen isopen, s.teachtype, s.examform ");
		sql.append(" from edu_teach_plancourse pc ").append(" join edu_base_course c on c.resourceid=pc.courseid ");
		sql.append(" join edu_teach_plan p on p.resourceid=pc.planid ").append(" join edu_base_major m on m.resourceid=p.majorid ");
		sql.append(" join edu_base_classic cl on cl.resourceid=p.classicid ").append("left join hnjk_sys_unit u on (u.resourceid=p.brschoolid) ");
		sql.append(" join edu_teach_guiplan gp on gp.planid=p.resourceid ").append(" join edu_base_grade g on g.resourceid=gp.gradeid ").append(" join edu_base_year y on y.resourceid=g.yearid ");
			
		//sql.append(" left join EDU_TEACH_CHECKOPENCOURSE ck on ck.coursestatusid = s.resourceid");
		//sql.append("  left join hnjk_sys_unit u on u.resourceid = ck.brschoolid ");
		sql.append(" left join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=pc.resourceid and s.isdeleted=0 ");
		if(condition.containsKey("brSchoolid")){
			sql.append(" and s.schoolids = ? ");		
			param.add(condition.get("brSchoolid"));
		}else if(!condition.containsKey("isOpenCourse") && !condition.containsKey("teachType")){
			//20180119 目前不知道这个有什么用，而且这个条件成立时都是显示未开课，暂时注释掉
			//sql.append(" and s.resourceid is null ");
		}
		
		sql.append(" where pc.isdeleted=0 and gp.isdeleted=0  and (p.brschoolid is not null or p.brschoolid is null ) ");
//		if(!"timetable".equals(condition.get("optype"))){
//			long currentYear = ExDateUtils.getCurrentYear();
//			if(condition.containsKey("isBrschool")){//学习中心教务员只开当前学期或之前的课程
//				sql.append(" and y.firstYear+decode(g.term,'2',1,0)+pc.term/2<= ").append(currentYear+1);
//			} 		
//		}
//		if(condition.containsKey("isOpenStatus")){ //开课状态			
//			if("N".equals(condition.get("isOpenStatus"))){
//				sql.append(" and (ck.openstatus = ? or  ck.openstatus is null) ");
//			}else{
//				sql.append(" and ck.openstatus = ? ");
//			}
//			param.add(condition.get("isOpenStatus"));
//		}
		if(condition.containsKey("isOpenCourse")){ //是否开课
			if("N".equals(condition.get("isOpenCourse"))){
				sql.append(" and (s.isopen <> 'Y' or s.isopen is null) ");
			}else{
				sql.append(" and s.isopen = ? ");
				param.add(condition.get("isOpenCourse"));
			}
		}
		
		if(condition.containsKey("teachType")){//teachType增加课程教学类型,用s的开课的,但是要用inner join
			sql.append(" and s.teachtype=? ");
			param.add(condition.get("teachType"));
		}
//		
//		if(condition.containsKey("checkSchool")){ //审核教学点
//			sql.append(" and ck.brschoolid = ? ");
//			param.add(condition.get("checkSchool"));
//		}
//		if(condition.containsKey("checkStatus")){ //审核状态
//			sql.append(" and ck.checkstatus = ? ");
//			param.add(condition.get("checkStatus"));
//		}
		if(condition.containsKey("gradeid")){//年级
			sql.append(" and g.resourceid=? ");
			param.add(condition.get("gradeid"));
		}
		if(condition.containsKey("courseId")){//课程
			sql.append(" and c.resourceid=? ");
			param.add(condition.get("courseId"));
		}
		if(condition.containsKey("majorid")){//专业
			sql.append(" and m.resourceid=? ");
			param.add(condition.get("majorid"));
		}
		if(condition.containsKey("classicid")){//层次
			sql.append(" and cl.resourceid=? ");
			param.add(condition.get("classicid"));
		}
//		if(condition.containsKey("brSchoolid")){//学习中心
//			sql.append(" and (u.resourceid=? or p.brschoolid is null ) ");
//			param.add(condition.get("brSchoolid"));
//		}
		if(condition.containsKey("teachingType")){//学习方式
			sql.append(" and p.schooltype=? ");
			param.add(condition.get("teachingType"));
		}		
		if(condition.containsKey("isOpen")){//开课状态
			sql.append(" and s.isOpen=? ");
			param.add(condition.get("isOpen"));
		}
		if(condition.containsKey("term")){//上课学期			
			sql.append(" and s.term=? ");
			param.add(condition.get("term"));
		}
		if(condition.containsKey("orgterm")){//原计划学期
			sql.append(" and pc.term=? ");
			param.add(condition.get("orgterm"));
		}
		if (condition.containsKey("resourceids")&&condition.containsKey("plancourseidsVo")) { //混合
			
			sql.append(" and s.resourceid in ("+condition.get("resourceids").toString()+") ");
			sql.append(" or (pc.resourceid in ("+condition.get("plancourseidsVo").toString()+") ");
			if(condition.containsKey("guiplanidsVo")){
				sql.append(" and gp.resourceid in ("+condition.get("guiplanidsVo").toString()+") ");
			}
			if(condition.containsKey("planidsVo")){
				sql.append(" and p.resourceid in ("+condition.get("planidsVo").toString()+") ");
			}
			sql.append(")");
		}else{
			if(condition.containsKey("resourceids")){
				sql.append(" and s.resourceid in ("+condition.get("resourceids").toString()+") ");
			}
			if(condition.containsKey("plancourseidsVo")){
				sql.append(" and pc.resourceid in ("+condition.get("plancourseidsVo").toString()+") ");
			}
			if(condition.containsKey("guiplanidsVo")){
				sql.append(" and gp.resourceid in ("+condition.get("guiplanidsVo").toString()+") ");
			}
			if(condition.containsKey("planidsVo")){
				sql.append(" and p.resourceid in ("+condition.get("planidsVo").toString()+") ");
			}
		}
		sql.append(" order by pc.term ");
//		if(condition.containsKey("classesid")){
//			sql.append(" and k.resourceid=? ");
//			param.add(condition.get("classesid"));
//		}
//		
//		if(condition.containsKey("checklist")){ //开课审核列表
//			sql.append(" order by ck.checktime desc ");
//		}else{
//			sql.append(" order by g.gradename,u.unitcode,m.majorcode,cl.classiccode,p.schooltype ").append(" ,pc.term,pc.resourceid ");
//		}
		return sql;
	}
	

	private StringBuilder getTeachingPlanCourseStatusSqlNew(Map<String, Object> condition, List<Object> param) {		
		StringBuilder sql = new StringBuilder();
		sql.append("select s.resourceid,pc.resourceid plancourseid,pc.planid,g.gradename,m.majorname,cl.classicname,nvl(u.unitname,'适用所有教学站') unitname,c.coursecode,c.coursename,y.firstyear,");
		sql.append(" p.schooltype teachingType,pc.term,s.isopen,gp.resourceid guiplanid,gp.gradeid,p.majorid,p.classicid,p.brschoolid,s.term courseterm,s.checkstatus checks,ck.openstatus openstatus");
		if(condition.containsKey("checklist")){ //开课审核列表
			sql.append(" ,ck.checktime time,ck.CHECKNAME name,s.checkmes mes,ck.term nowTerm,ck.checkstatus checks,ck.openstatus openstatus,ck.operate,ck.resourceid ckid,ck.UPDATETERM updateterm,ck.applyTime applytime,ck.applyName applyname,s.schoolName ");
		}
		sql.append(" from edu_teach_plancourse pc ").append(" join edu_base_course c on c.resourceid=pc.courseid ");
		sql.append(" join edu_teach_plan p on p.resourceid=pc.planid ").append(" join edu_base_major m on m.resourceid=p.majorid ");
		sql.append(" join edu_base_classic cl on cl.resourceid=p.classicid ").append("left join hnjk_sys_unit u on (u.resourceid=p.brschoolid) ");
		sql.append(" join edu_teach_guiplan gp on gp.planid=p.resourceid ").append(" join edu_base_grade g on g.resourceid=gp.gradeid ").append(" join edu_base_year y on y.resourceid=g.yearid ");
		sql.append(" left join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=pc.resourceid and s.isdeleted=0 ");	
		if(condition.containsKey("checklist")){ //开课审核列表
			sql.append(" right join EDU_TEACH_CHECKOPENCOURSE ck on ck.coursestatusid = s.resourceid and ck.isdeleted=0 ");
		}else{
			sql.append(" left join EDU_TEACH_CHECKOPENCOURSE ck on ck.coursestatusid = s.resourceid and ck.isdeleted=0 ");
			
		}
		sql.append(" where pc.isdeleted=0 and gp.isdeleted=0  and (u.resourceid is not null and p.brschoolid is not null or p.brschoolid is null ) ");
//		if(!"timetable".equals(condition.get("optype"))){
//			long currentYear = ExDateUtils.getCurrentYear();
//			if(condition.containsKey("isBrschool")){//学习中心教务员只开当前学期或之前的课程
//				sql.append(" and y.firstYear+decode(g.term,'2',1,0)+pc.term/2<= ").append(currentYear+1);
//			} 		
//		}
		if(condition.containsKey("isOpenStatus")){ //开课状态			
			if("N".equals(condition.get("isOpenStatus"))){
				sql.append(" and (ck.openstatus = ? or  ck.openstatus is null) ");
			}else{
				sql.append(" and ck.openstatus = ? ");
			}
			param.add(condition.get("isOpenStatus"));
		}

		if(condition.containsKey("isOpenCourse")){ //是否开课
			if("N".equals(condition.get("isOpenCourse"))){
				sql.append(" and (s.isopen = ? or  s.isopen is null) ");
			}else{
				sql.append(" and s.isopen = ? ");
			}
			
			param.add(condition.get("isOpenCourse"));
		}
		
		if(condition.containsKey("checkSchool")){ //审核教学点
			sql.append(" and s.schoolIds = ? ");
			param.add(condition.get("checkSchool"));
		}
		if(condition.containsKey("checkStatus")){ //审核状态
			sql.append(" and ck.checkstatus = ? ");
			param.add(condition.get("checkStatus"));
		}
		if(condition.containsKey("checkpp")){ //审核人
			sql.append(" and ck.checkName = ? ");
			param.add(condition.get("checkpp"));
		}
		if(condition.containsKey("operation")){ //操作
			sql.append(" and ck.operate = ? ");
			param.add(condition.get("operation"));
		}
		
		
		if(condition.containsKey("gradeid")){//年级
			sql.append(" and g.resourceid=? ");
			param.add(condition.get("gradeid"));
		}
		if(condition.containsKey("majorid")){//专业
			sql.append(" and m.resourceid=? ");
			param.add(condition.get("majorid"));
		}
		if(condition.containsKey("classicid")){//层次
			sql.append(" and cl.resourceid=? ");
			param.add(condition.get("classicid"));
		}
		if(condition.containsKey("brSchoolid")){//学习中心
			sql.append(" and (u.resourceid=? or p.brschoolid is null ) ");
			param.add(condition.get("brSchoolid"));
		}
		if(condition.containsKey("teachingType")){//学习方式
			sql.append(" and p.schooltype=? ");
			param.add(condition.get("teachingType"));
		}		
		if(condition.containsKey("isOpen")){//开课状态
			sql.append(" and s.isOpen=? ");
			param.add(condition.get("isOpen"));
		}
		if(condition.containsKey("term")){//上课学期			
			sql.append(" and s.term=? ");
			param.add(condition.get("term"));
		}
		if(condition.containsKey("orgterm")){//原计划学期
			sql.append(" and pc.term=? ");
			param.add(condition.get("orgterm"));
		}
		if(condition.containsKey("classesid")){
			sql.append(" and k.resourceid=? ");
			param.add(condition.get("classesid"));
		}
		if(condition.containsKey("courseId")){//课程
			sql.append(" and c.resourceid=? ");
			param.add(condition.get("courseId"));
		}
		
		
		if(condition.containsKey("checklist")){ //开课审核列表
			sql.append(" order by ck.applytime desc ");
		}else{
			sql.append(" order by g.gradename,u.unitcode,m.majorcode,cl.classiccode,p.schooltype ").append(" ,pc.term,pc.resourceid ");
		}
		return sql;
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public Page findTeachingPlanCourseStatusMapByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = null;
		if(condition.containsKey("OpenCourseResultFlag")){//开课结果界面
			sql = getOpenCourseStatusSql(condition,param);
		}else {
			sql = getTeachingPlanCourseStatusSql(condition,param);
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), param.toArray());
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> findTeachingPlanCourseByCondition(Map<String, Object> condition) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		List<Map<String, Object>> planCourseList = new ArrayList<Map<String,Object>>();
		StringBuilder sql = getTeachingPlanCourseStatusSql(condition,param);	
		try {
			planCourseList = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), param.toArray());
		} catch (Exception e) {
			//logger.error("导出排课模板时，获取课程出错：", e);
			logger.error("获取课程出错：", e);
		}
		return planCourseList;
	}
	
	private StringBuilder getOpenCourseStatusSql(Map<String, Object> condition, List<Object> param) {		
		StringBuilder sql = new StringBuilder();
		sql.append(" select * from (");
		sql.append(" select 'null' resourceid,pc.resourceid plancourseid,pc.planid,pc.stydyHour,pc.examClassType,gp.resourceid guiplan,g.gradename,cl.classicname,u.unitname,c.coursecode,c.coursename,m.majorname,");
		sql.append(" p.schooltype teachingType,pc.term,p.majorid,p.classicid,u.resourceid brschoolid,s.term courseterm,c.resourceid cid ");
		sql.append(" ,k.classesname,k.resourceid classesid ").append(",(select decode(count(*),0,0,1) from edu_teach_timetable a where a.isdeleted=0 and a.classesid=k.resourceid and a.plancourseid=pc.resourceid and a.term = s.term) status ");
		sql.append(" ,tcl.resourceid tclid,tcl.TEACHERNAME teacherName, tcl.TEACHERID teacherId ");
		sql.append(",(select count(*) from edu_roll_studentinfo stu where stu.isdeleted=0 and stu.classesid=k.resourceid and stu.studentStatus = '11') stucount");

		//新增（开课结果界面）
		sql.append(",-1 generateStatus, 1 classcount,s.teachType");//生成状态,合班数,教学类型
		sql.append(" from edu_teach_plancourse pc ").append(" join edu_base_course c on c.resourceid=pc.courseid ");
		sql.append(" join edu_teach_plan p on p.resourceid=pc.planid ").append(" join edu_base_major m on m.resourceid=p.majorid ");
		sql.append(" join edu_base_classic cl on cl.resourceid=p.classicid ");
		sql.append(" join edu_teach_guiplan gp on gp.planid=p.resourceid ").append(" join edu_base_grade g on g.resourceid=gp.gradeid ").append(" join edu_base_year y on y.resourceid=g.yearid ");
		sql.append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=pc.resourceid and s.isdeleted=0 ");
		sql.append(" join edu_roll_classes k on k.gradeid=gp.gradeid and k.majorid=p.majorid and k.classicid=p.classicid and k.teachingType=p.schooltype and k.orgunitid = s.schoolids and k.isdeleted=0 ");
		sql.append(" join hnjk_sys_unit u on (u.resourceid=k.orgunitid) ");
		sql.append(" left join EDU_TEACH_COURSETEACHERCL tcl on tcl.COURSESTATUSID = s.resourceid and tcl.COURSEID = c.resourceid and tcl.CLASSESID = k.resourceid ");
//		添加显示授课老师列
		sql.append(" LEFT JOIN (SELECT ett.plancourseid,wm_concat(distinct(ett.teachername)) AS lecturers,ett.classesid FROM EDU_TEACH_TIMETABLE ETT WHERE ett.isdeleted = 0GROUP BY ett.plancourseid,ett.classesid) ETT ON ett.plancourseid = pc.resourceid AND ett.classesid = K.Resourceid" );
		sql.append(" where pc.isdeleted=0 and gp.isdeleted=0  and (u.resourceid is not null and p.brschoolid is not null or p.brschoolid is null ) ");
		
		/*if (condition.containsKey("status")) {
			sql.append(" and (select decode(count(*),0,0,1) from edu_teach_timetable a where a.isdeleted=0 and a.classesid=k.resourceid and a.plancourseid=pc.resourceid and a.term = s.term)");
			if ("Y".equals(condition.get("status").toString())) {
				sql.append("=1 ");
			} else {
				sql.append("=0 ");
			}
		}*/
		if(condition.containsKey("res")){
			sql.append(" and s.resourceid = ? ");
			param.add(condition.get("res")); //id
		}
		if(condition.containsKey("guiplanid")){
			sql.append(" and gp.resourceid = ? ");
			param.add(condition.get("guiplanid")); //年级教学计划id
		}
		if(condition.containsKey("plancourseid")){
			sql.append(" and pc.resourceid = ? ");
			param.add(condition.get("plancourseid")); //教学计划课程id
		}
		if(condition.containsKey("gradeid")){//年级
			sql.append(" and g.resourceid=? ");
			param.add(condition.get("gradeid"));
		}
		if(condition.containsKey("minYear")){
			sql.append(" and y.firstYear>? ");
			param.add(condition.get("minYear"));
		}
		if(condition.containsKey("majorid")){//专业
			sql.append(" and m.resourceid=? ");
			param.add(condition.get("majorid"));
		}
		if(condition.containsKey("isOpen")){//开课状态
			sql.append(" and s.isOpen=? ");
			param.add(condition.get("isOpen"));
		}
		if(condition.containsKey("classesid")){
			sql.append(" and k.resourceid=? ");
			param.add(condition.get("classesid"));
		}
		if(condition.containsKey("resourceids")){
			sql.append(" and s.resourceid in ("+condition.get("resourceids").toString()+") ");
		}
		if(condition.containsKey("classesids")){
			sql.append(" and k.resourceid in ("+condition.get("classesids").toString()+") ");
		}
		sql.append(" and (k.resourceid,c.resourceid) not in(select tc.classesid,tc.courseid from edu_arrange_teachclasses tc join edu_arrange_teachcourse tce on tce.resourceid=tc.teachcourseid and tce.isdeleted=0 where tc.isdeleted=0)");
		//联合查询教学班级表
		sql.append(" union select tcourse.resourceid,pc.resourceid plancourseid,pc.planid,tcourse.studyHour,tcourse.examClassType,'',substr(y.yearname,0,4)||'学年',ci.classicname,u.unitname,c.coursecode,tcourse.coursename,");
		sql.append(" (select wm_concat(m.majorname) from edu_base_major m join edu_roll_classes rcl on rcl.majorid=m.resourceid join edu_arrange_teachclasses atc on atc.classesid=rcl.resourceid where atc.teachcourseid=tcourse.resourceid),");
		sql.append(" tcourse.teachingtype,'','',ci.resourceid,u.resourceid,tcourse.openterm,c.resourceid,");
		sql.append(" tcourse.teachingclassname,tcourse.teachingcode,tcourse.arrangestatus,");
		sql.append(" '',tcourse.teachernames,'',tcourse.studentnumbers,tcourse.generateStatus,");
		sql.append(" (select count(*) from edu_arrange_teachclasses tc where tc.teachcourseid=tcourse.resourceid and tc.isdeleted=0),tcourse.teachType");
		sql.append(" from edu_arrange_teachcourse tcourse");
		sql.append(" left join (select * from (select t.*,row_number() over(partition by t.teachcourseid order by t.courseid) rn from edu_arrange_teachclasses t) A where A.rn = 1) tclasse on tclasse.teachcourseid=tcourse.resourceid");
		sql.append(" left join hnjk_sys_unit u on u.resourceid=tcourse.unitid");
		sql.append(" left join edu_teach_plancourse pc on pc.resourceid=tclasse.plancourseid and pc.isdeleted=0");
		sql.append(" left join edu_base_course c on c.resourceid=pc.courseid and c.isdeleted=0");
		sql.append(" left join edu_base_classic ci on ci.resourceid=tcourse.classicid");
		sql.append(" left join edu_base_year y on y.resourceid=tcourse.yearid");
		sql.append(" where tcourse.isdeleted=0 ) where 1=1");
		
		if(condition.containsKey("brSchoolid")){//学习中心
			sql.append(" and brschoolid=? ");
			param.add(condition.get("brSchoolid"));
		}
		if(condition.containsKey("classicid")){//层次
			sql.append(" and classicid=? ");
			param.add(condition.get("classicid"));
		}
		if(condition.containsKey("teachingType")){//学习方式
			sql.append(" and teachingType=? ");
			param.add(condition.get("teachingType"));
		}
		if(condition.containsKey("teachCourseid")){//教学班
			sql.append(" and resourceid=? ");
			param.add(condition.get("teachCourseid"));
		}
		if(condition.containsKey("courseId")){ 
			sql.append(" and cid = ? ");
			param.add(condition.get("courseId")); //课程
		}	
		if(condition.containsKey("term")){//上课学期
			sql.append(" and courseterm=? ");
			param.add(condition.get("term"));
		}
		if(condition.containsKey("status")){//排课状态
			if ("Y".equals(condition.get("status").toString())) {
				sql.append(" and status=1 ");
			} else {
				sql.append(" and status=0");
			}
		}
		if(condition.containsKey("generateStatus")){//生成状态
			sql.append(" and generateStatus=? ");
			param.add(Integer.parseInt(condition.get("generateStatus").toString()));
		}
		if(condition.containsKey("startNum")){//最小人数
			sql.append(" and stucount>=? ");
			param.add(Integer.parseInt(condition.get("startNum").toString()));
		}
		if(condition.containsKey("endNum")){//最大人数
			sql.append(" and stucount<=? ");
			param.add(Integer.parseInt(condition.get("endNum").toString()));
		}
		//开课结果界面的排序：层次，学习形式，考核形式，课程，必修选，学时
		sql.append(" order by unitname,classicname,teachingType,examClassType,coursename,stydyHour ");
		return sql;
	}
	
	private StringBuilder getTeachingPlanCourseStatusSql(Map<String, Object> condition, List<Object> param) {		
		StringBuilder sql = new StringBuilder();
		sql.append("select * from(select s.resourceid,pc.resourceid plancourseid,pc.planid,pc.stydyHour,pc.examClassType,g.gradename,m.majorname,cl.classicname,u.unitname,c.coursecode,c.coursename,");
		sql.append(" p.schooltype teachingType,pc.term,s.isopen,gp.resourceid guiplanid,gp.gradeid,p.majorid,p.classicid,u.resourceid brschoolid,s.term courseterm,c.resourceid cid ");//p.brschoolid
		//添加班级人数
		sql.append(" ,(select count(*) from edu_roll_studentinfo si where si.classesid=k.resourceid and si.studentstatus in('11','16','21','24','25') and si.isdeleted=0) stunumber");
		if("timetable".equals(condition.get("optype"))){//课程安排
			sql.append(" ,k.classesname,k.resourceid classesid ").append(",(select decode(count(*),0,0,1) from edu_teach_timetable a where a.isdeleted=0 and a.classesid=k.resourceid and a.plancourseid=pc.resourceid and a.term = s.term) status ");
			sql.append(" ,tcl.TEACHERNAME teacherName, tcl.TEACHERID teacherId , tcl.lecturerid lecturerid , tcl.lecturerName lecturerName,s.resourceid courseStatusid ");
			sql.append(",(select count(*) from edu_roll_studentinfo stu where stu.isdeleted=0 and stu.classesid=k.resourceid and stu.studentStatus = '11') stucount");
//			添加显示授课老师列
			sql.append(" ,ETT.lecturers ");
			if ("data".equals(condition.get("exportType"))) {//导出排课详情信息
				sql.append(" ,em.TEACHERCODE,tt.TEACHERNAME TEACHERNAME1,cr.CLASSROOMNAME,tt.WEEK,tt.TEACHDATE,tp.TIMEPERIOD,to_char(tp.STARTTIME,'hh:mi')||'-'||to_char(tp.ENDTIME,'hh:mi') time,tt.MERGEMEMO");
			}
		}

		if("Y".equals(condition.get("setTeach"))){
			sql.append(" ,tcl.TEACHERNAME teacherName,tcl.TEACHERID teacherId ");
		}

		sql.append(" from edu_teach_plancourse pc ").append(" join edu_base_course c on c.resourceid=pc.courseid ");
		sql.append(" join edu_teach_plan p on p.resourceid=pc.planid ").append(" join edu_base_major m on m.resourceid=p.majorid ");
		sql.append(" join edu_base_classic cl on cl.resourceid=p.classicid ");
		sql.append(" join edu_teach_guiplan gp on gp.planid=p.resourceid ").append(" join edu_base_grade g on g.resourceid=gp.gradeid ").append(" join edu_base_year y on y.resourceid=g.yearid ");
		sql.append("timetable".equals(condition.get("optype"))?"":" left ").append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=pc.resourceid and s.isdeleted=0 ");
		if("timetable".equals(condition.get("optype"))){//课程安排
			sql.append(" join edu_roll_classes k on k.gradeid=gp.gradeid and k.majorid=p.majorid and k.classicid=p.classicid and k.teachingType=p.schooltype and k.orgunitid = s.schoolids and k.isdeleted=0 ");
			sql.append(" join hnjk_sys_unit u on (u.resourceid=k.orgunitid) ");
			//sql.append(" left join (select si.classesid,count(si.resourceid) stuNum from edu_roll_studentinfo si where si.isdeleted=0 group by si.classesid) stuNum on stuNum.classesid=k.resourceid");
			/**
			 * update 2014-7-10 15:20:19 因为设置了 课程-教师-班级管理表  所以做调整
			 */
			sql.append(" left join EDU_TEACH_COURSETEACHERCL tcl on tcl.COURSESTATUSID = s.resourceid and tcl.COURSEID = c.resourceid and tcl.CLASSESID = k.resourceid and tcl.ISDELETED = 0");

//			添加显示授课老师列
			sql.append(" LEFT JOIN (SELECT ett.plancourseid,wm_concat(distinct ett.teachername) AS lecturers,ett.classesid FROM EDU_TEACH_TIMETABLE ETT WHERE ett.isdeleted = 0 GROUP BY ett.plancourseid,ett.classesid) ETT ON ett.plancourseid = pc.resourceid AND ett.classesid = K.Resourceid" );
			if ("data".equals(condition.get("exportType"))) {//导出排课详情信息
				sql.append(" left join EDU_TEACH_TIMETABLE tt on tt.CLASSESID=k.RESOURCEID and tt.COURSEID=c.RESOURCEID and tt.ISDELETED=0");
				sql.append(" left join EDU_BASE_EDUMANAGER em on em.RESOURCEID=tt.TEACHERID and em.ISDELETED=0");
				sql.append(" left join EDU_BASE_CLASSROOM cr on cr.RESOURCEID=tt.CLASSROOMID and cr.ISDELETED=0");
				sql.append(" left join EDU_TEACH_TIMESETTING tset on tset.RESOURCEID=tt.TIMEPERIODID and tset.ISDELETED=0");
				sql.append(" left join EDU_TEACH_TIMEPERIOD tp on tp.RESOURCEID=tt.TIMEPERIODID and tp.ORGUNITID=k.ORGUNITID and tp.ISDELETED=0");
			}
//			按批次ID搜索时，过滤批次包含的考试信息课程
			if(condition.containsKey("examSubId")){
				if("Y".equals(condition.get("isFinalExam"))){
//				判断是正考批次，选择对应上课学期的课程
					sql.append(" JOIN (");
					sql.append("	SELECT eby.firstyear || '_0' || eteb.term AS term from edu_teach_examsub eteb ");
					sql.append("	INNER JOIN edu_base_year eby ON eby.resourceid = eteb.yearid ");
					sql.append("	WHERE eteb.resourceid = '" + condition.get("examSubId") + "'");
					sql.append(" ) tmpExamSub on tmpExamSub.term = s.term");
				}else{
//				判断是非正考批次，选择补考名单中的课程,因为录入并发布成绩后才生成补考名单，所以补考名单在录入成绩之前是完整的。
					sql.append(" JOIN(");
					sql.append("	SELECT etmt.plansourceid AS plansourceid ,ers.branchschoolid AS branchschoolid");
					sql.append(" 	FROM edu_teach_makeuplist etmt  ");
					sql.append("	INNER JOIN edu_roll_studentinfo ers ON ers.resourceid = etmt.studentid");
					sql.append("	WHERE etmt.nextexamsubid = '"+ condition.get("examSubId") +"' AND etmt.plansourceid IS NOT NULL ");
					sql.append("	GROUP BY etmt.plansourceid,ers.branchschoolid ");
					sql.append(" ) tmpMakeuplist ON pc.Resourceid = tmpMakeuplist.plansourceid AND k.orgunitid = tmpMakeuplist.branchschoolid");
				}
			}
		}
		
		sql.append(" where pc.isdeleted=0 and gp.isdeleted=0  and (u.resourceid is not null and p.brschoolid is not null or p.brschoolid is null ) ");
		if(!"timetable".equals(condition.get("optype"))){
			long currentYear = ExDateUtils.getCurrentYear();
			if(condition.containsKey("isBrschool")){//学习中心教务员只开当前学期或之前的课程
				sql.append(" and y.firstYear+decode(g.term,'2',1,0)+pc.term/2<= ").append(currentYear+1);
			} 		
		}else{
			if (condition.containsKey("status")) {
				sql.append(" and (select decode(count(*),0,0,1) from edu_teach_timetable a where a.isdeleted=0 and a.classesid=k.resourceid and a.plancourseid=pc.resourceid and a.term = s.term)");
				if ("Y".equals(condition.get("status").toString())) {
					sql.append("=1 ");
				} else {
					sql.append("=0 ");
				}
			}
		}
		if(condition.containsKey("teachername")){ //登分老师名称
			sql.append(" and tcl.TEACHERNAME like '%"+condition.get("teachername")+"%'");
		}
		if(condition.containsKey("courseId")){ 
			sql.append(" and c.resourceid = ? ");
			param.add(condition.get("courseId")); //课程
		}
		if(condition.containsKey("teacherid")){ 
			sql.append(" and exists (select resourceid from edu_teach_timetable a where a.isdeleted=0 and a.classesid=k.resourceid");
			sql.append(" 	and a.plancourseid=pc.resourceid and a.term=s.term and a.teacherid=?) ");
			param.add(condition.get("teacherid")); //登分老师ID
		}
//		增加讲课老师查询
		if(condition.containsKey("lecturer")){ 
			sql.append(" AND ett.lecturers IS NOT NULL AND ett.lecturers LIKE '%"+condition.get("lecturer")+"%' ");
		}
		if(condition.containsKey("res")){
			sql.append(" and s.resourceid = ? ");
			param.add(condition.get("res")); //id
		}
		if(condition.containsKey("guiplanid")){
			sql.append(" and gp.resourceid = ? ");
			param.add(condition.get("guiplanid")); //年级教学计划id
		}
		if(condition.containsKey("plancourseid")){
			sql.append(" and pc.resourceid = ? ");
			param.add(condition.get("plancourseid")); //教学计划课程id
		}
		if(condition.containsKey("gradeid")){//年级
			sql.append(" and g.resourceid=? ");
			param.add(condition.get("gradeid"));
		}
		if(condition.containsKey("majorid")){//专业
			sql.append(" and m.resourceid=? ");
			param.add(condition.get("majorid"));
		}
		if(condition.containsKey("classicid")){//层次
			sql.append(" and cl.resourceid=? ");
			param.add(condition.get("classicid"));
		}
		if(condition.containsKey("brSchoolid")){//学习中心
			sql.append(" and u.resourceid=? ");
			param.add(condition.get("brSchoolid"));
		}
		if(condition.containsKey("teachingType")){//学习方式
			sql.append(" and p.schooltype=? ");
			param.add(condition.get("teachingType"));
		}		
		if(condition.containsKey("isOpen")){//开课状态
			sql.append(" and s.isOpen=? ");
			param.add(condition.get("isOpen"));
		}
		if(condition.containsKey("term")){//上课学期
			sql.append(" and s.term=? ");
			param.add(condition.get("term"));
		}
		if(condition.containsKey("orgterm")){//原计划学期
			sql.append(" and pc.term=? ");
			param.add(condition.get("orgterm"));
		}
		if(condition.containsKey("classesid")){
			sql.append(" and k.resourceid=? ");
			param.add(condition.get("classesid"));
		}
		if(condition.containsKey("resourceids")){
			sql.append(" and s.resourceid in ("+condition.get("resourceids").toString()+") ");
//			param.add(condition.get("residary"));
		}
		if(condition.containsKey("classesids")){
			sql.append(" and k.resourceid in ("+condition.get("classesids").toString()+") ");
//			param.add(condition.get("residary"));
		}

		if(condition.containsKey("OpenCourseResultFlag")){//开课结果页面
			sql.append(" and (k.resourceid,c.resourceid) not in(select tc.classesid,tc.courseid from edu_arrange_teachclasses tc where tc.isdeleted=0)");
		}

		if(condition.containsKey("isAdjust")){
			if("Y".equals(condition.get("isAdjust"))){
				sql.append(" and s.checkStatus='updateY'");
			}else if ("N".equals(condition.get("isAdjust"))) {
				sql.append(" and s.checkStatus!='updateY'");
			}
		}
		if(condition.containsKey("selectedExportParam")){//勾选导出参数
			sql.append(" and (k.resourceid,pc.resourceid) in ("+condition.get("selectedExportParam")+") ");
		}

//		排课的情况
		if("timetable".equals(condition.get("optype"))){
			if("Y".equals(condition.get("OpenCourseResultFlag"))){
				//开课结果界面的排序：层次，学习形式，考核形式，课程，必修选，学时
				sql.append(" order by u.unitcode,cl.classiccode, p.schooltype,pc.examClassType,c.coursename,pc.courseType,pc.stydyHour");
			}else {
//				修改排序，根据上课学期和课程名字 排序
				sql.append(" order by g.gradename,u.unitcode,cl.classiccode,p.schooltype ")
				.append("timetable".equals(condition.get("optype"))?",s.term":"")
				.append(",c.coursename,m.majorname,k.classesname,STATUS,TCL.TEACHERNAME");
			}

		}else{
			sql.append(" order by g.gradename,u.unitcode,m.majorcode,cl.classiccode,p.schooltype ").append("timetable".equals(condition.get("optype"))?",k.classesname,s.term":"").append(" ,pc.term,pc.resourceid ");
		}
		//班级人数大于0才显示
		sql.append(") where stunumber>0");
		return sql;
	}
	
	@Override
	public void saveOrUpdateCourseStatusList(List<TeachingPlanCourseStatus> courseStatusList, List<TeachingPlanCourseTimetable> timetableList)throws ServiceException {
		batchSaveOrUpdate(courseStatusList);
		if(ExCollectionUtils.isNotEmpty(timetableList)){//取消开课是删除安排
			exGeneralHibernateDao.batchDelete(timetableList);
		}
		if(ExCollectionUtils.isNotEmpty(courseStatusList)){
			saveOrupdateStudentInfoStuPlan(courseStatusList);//补充预约
		}
	}
	
	
	@Override
	public void saveOrupdateStudentInfoStuPlan(List<TeachingPlanCourseStatus> courseStatusList) throws ServiceException {
		try {
			List<StudentLearnPlan> studentLearnPlanList = new ArrayList<StudentLearnPlan>();
			for(TeachingPlanCourseStatus tpc : courseStatusList){
				//openY,cancelY,updateY
				if("openY".equals(tpc.getCheckStatus()) || "cancelY".equals(tpc.getCheckStatus()) || "updateY".equals(tpc.getCheckStatus())){
					String Sschoolid = tpc.getSchoolIds();
					TeachingGuidePlan Ogplan = tpc.getTeachingGuidePlan();
					TeachingPlanCourse Otpc = tpc.getTeachingPlanCourse();
					Grade grade = Ogplan.getGrade();
					String Sterm = "";
					if("cancelY".equals(tpc.getCheckStatus())){
						Sterm = tpc.getAfterterm()==null?"":tpc.getAfterterm();
					}else{
						Sterm = tpc.getTerm()==null?"":tpc.getTerm();
					}
					String[] ARRYterm = Sterm.split("_0");
//					YearInfo yearInfo = grade.getYearInfo();
					YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(ARRYterm[0]));
					if(null == yearInfo ){
//						logger.debug("年级规则错误");
//						break;
						throw new ServiceException("请添加"+ARRYterm[0]+"-"+(Integer.valueOf(ARRYterm[0])+1)+"学年度");
					}
					//找到相应的学生
					StringBuffer hql = new StringBuffer();
					hql.append(" from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = 0 and stu.teachingPlan.resourceid = ? ");
					hql.append(" and stu.grade.resourceid = ? ");
					List<StudentInfo> LISTstu = new ArrayList<StudentInfo>();
					if(ExStringUtils.isNotBlank(Sschoolid)){
						hql.append(" and stu.branchSchool.resourceid = ? ");
						LISTstu = studentInfoService.findByHql(hql.toString(), Ogplan.getTeachingPlan().getResourceid(),grade.getResourceid(),Sschoolid);
					}else{
						LISTstu = studentInfoService.findByHql(hql.toString(), Ogplan.getTeachingPlan().getResourceid(),grade.getResourceid());
					}
					
					if(null != LISTstu && LISTstu.size() > 0){
						// 获取所有某一年某个学期某门课程的学生学习计划记录
						/*List<StudentLearnPlan> LISTslp = studentLearnPlanService.findByHql(" from "+StudentLearnPlan.class.getSimpleName()+" pn where pn.isDeleted = 0 " +
								" and pn.teachingPlanCourse.resourceid = ? and pn.yearInfo.resourceid = ? and pn.term = ? ", Otpc.getResourceid(),yearInfo.getResourceid(),ARRYterm[1]);
						Map<String,StudentLearnPlan> studentPlanMap = new HashMap<String, StudentLearnPlan>();
						if(LISTslp !=null && LISTslp.size() > 0){
							for(StudentLearnPlan slp : LISTslp){
								studentPlanMap.put(slp.getStudentInfo().getResourceid(), slp);
							}
						}*/
						List<StudentLearnPlan> LISTslp = studentLearnPlanService.findByHql(" from "+StudentLearnPlan.class.getSimpleName()+" pn where pn.isDeleted = 0 " +
								" and pn.teachingPlanCourse.resourceid = ? and pn.studentInfo.branchSchool.resourceid = ? and pn.studentInfo.grade.resourceid = ? ", Otpc.getResourceid(),Sschoolid,grade.getResourceid());
						Map<String,StudentLearnPlan> studentPlanMap = new HashMap<String, StudentLearnPlan>();
						if(LISTslp !=null && LISTslp.size() > 0){
							for(StudentLearnPlan slp : LISTslp){
								studentPlanMap.put(slp.getStudentInfo().getResourceid(), slp);
							}
						}
						for(StudentInfo stu : LISTstu){
							try {
								/*List<StudentLearnPlan> LISTslp = studentLearnPlanService.findByHql(" from "+StudentLearnPlan.class.getSimpleName()+" pn where pn.isDeleted = 0 and pn.studentInfo.resourceid = ? " +
										" and pn.teachingPlanCourse.resourceid = ? and pn.yearInfo.resourceid = ? and pn.term = ? ", stu.getResourceid(),Otpc.getResourceid(),yearInfo.getResourceid(),ARRYterm[1]);
								if(null != LISTslp && LISTslp.size() > 0){
									if("cancelY".equals(tpc.getCheckStatus())){
										for(StudentLearnPlan sn : LISTslp){
											studentLearnPlanService.truncate(sn);
										}
									} else {
										continue;
									}
								}*/
								/*if(studentPlanMap.containsKey(stu.getResourceid())) {
									if("cancelY".equals(tpc.getCheckStatus())){
										studentLearnPlanService.truncate(studentPlanMap.get(stu.getResourceid()));
									} else {
										continue;
									}
								}
								if(!"cancelY".equals(tpc.getCheckStatus())){
									StudentLearnPlan newLearnPlan = new StudentLearnPlan();
									newLearnPlan.setStudentInfo(stu);
									newLearnPlan.setStatus(1);
									newLearnPlan.setTeachingPlanCourse(Otpc);
									newLearnPlan.setYearInfo(yearInfo);
									newLearnPlan.setTerm(ARRYterm[1]);
									newLearnPlan.setOrderExamYear(yearInfo);
									newLearnPlan.setOrderExamTerm(ARRYterm[1]);
									studentLearnPlanList.add(newLearnPlan);
								}*/
								StudentLearnPlan newLearnPlan = new StudentLearnPlan();
								if(studentPlanMap.containsKey(stu.getResourceid())) {
									newLearnPlan = studentPlanMap.get(stu.getResourceid());
								}
								
								if("cancelY".equals(tpc.getCheckStatus())){
//									studentLearnPlanService.truncate(newLearnPlan);
									if(newLearnPlan.getResourceid()!=null){
										studentLearnPlanService.delete(newLearnPlan);
									}
								} else {
									if(newLearnPlan.getYearInfo()!=null && newLearnPlan.getTerm() !=null
											&& newLearnPlan.getYearInfo().getResourceid().equals(yearInfo.getResourceid())
											&& newLearnPlan.getTerm().equals(ARRYterm[1])){
										continue;
									}
									/*ExamInfo ei = newLearnPlan.getExamInfo();
									if(ei == null ){
										ei =new ExamInfo();
										ei.setCourse(Otpc.getCourse());
										ei.setExamType("0");
										examInfoService.save(ei);
									}*/
									newLearnPlan.setStudentInfo(stu);
									newLearnPlan.setStatus(1);
//									newLearnPlan.setExamInfo(ei);//学院2016修改
									newLearnPlan.setTeachingPlanCourse(Otpc);
									newLearnPlan.setYearInfo(yearInfo);
									newLearnPlan.setTerm(ARRYterm[1]);
									newLearnPlan.setOrderExamYear(yearInfo);
									newLearnPlan.setOrderExamTerm(ARRYterm[1]);
									studentLearnPlanList.add(newLearnPlan);
								}
							} catch (Exception e) {
								logger.debug("学生："+stu.getStudentName()+",学号："+stu.getStudyNo()+" 补充预约出错;");
								e.printStackTrace();
							}
						}
					}
				}
			}
			// 批量保存
			if(studentLearnPlanList!=null && studentLearnPlanList.size()>0){
				studentLearnPlanService.batchSaveOrUpdate(studentLearnPlanList);
			}
		} catch (Exception e) {
			logger.debug("给学生补充预约出错;");
			e.printStackTrace();
		}
	}

	/**
	 * 根据条件获取开课记录
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<TeachingPlanCourseStatus> findByCondition(String guiplanId, String planCourseId, String unitId, String isOpen) throws Exception {
		StringBuffer hql = new StringBuffer("");
		hql.append("from "+TeachingPlanCourseStatus.class.getSimpleName()+ " css where css.isDeleted=0 ");
		hql.append(" and css.isOpen=? ");
		hql.append(" and css.teachingGuidePlan.resourceid=? ");
		hql.append(" and css.teachingPlanCourse.resourceid=? ");
		hql.append(" and css.schoolIds=? ");
		
		return (List<TeachingPlanCourseStatus>)exGeneralHibernateDao.findByHql(hql.toString(),isOpen,guiplanId,planCourseId,unitId);
	}

	/**
	 * 处理自动开课逻辑
	 * @param openCourseInfoList
	 * @param user
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, String> handleAutoOpenCourses(List<Map<String, Object>> openCourseInfoList, User user, OrgUnit orgUnit) throws Exception {
		Map<String, String> returnInfoMap = new HashMap<String, String>();
		String returnCode = "200";
		String message ="自动开课成功！";
		StringBuffer _message = new StringBuffer("");
		do{
			try {
				if(orgUnit == null) {
					returnCode = "300";
					message = "教学点不能为空！";
					break;
				}
				String unitName = orgUnit.getUnitName();
				String unitId = orgUnit.getResourceid();
				// 上课学期字典表
				List<Dictionary> courseTermList =  CacheAppManager.getChildren("CodeCourseTermType");
				Map<String,String> courseTermMap = new HashMap<String, String>();
				if(courseTermList!=null && courseTermList.size()>0){
					for(Dictionary d : courseTermList){
						courseTermMap.put(d.getDictValue(), d.getDictName());
					}
				}
				// 开课信息
				List<String> _openTemList = new ArrayList<String>();
				List<TeachingPlanCourseStatus> planCourseStatusList = new ArrayList<TeachingPlanCourseStatus>();
				List<CheckOpenCourse> checkOpenCourseList = new ArrayList<CheckOpenCourse>();
				if(openCourseInfoList != null && openCourseInfoList.size() > 0){
					List<String> existedOpenCourseList = new ArrayList<String>();// 已添加的开课记录
					TeachingPlanCourseStatus courseStatus = null;
					CheckOpenCourse checkOpenCourse = null;
					for(Map<String, Object> courseStatusInfo : openCourseInfoList){
						String guiplanId = (String)courseStatusInfo.get("guiplanId");
						String planCourseId = (String)courseStatusInfo.get("planCourseId");
						String _openTerm = (String)courseStatusInfo.get("openTerm");
						// 检查是否已经添加
						if(existedOpenCourseList.contains(guiplanId+planCourseId+unitId+"openY")){
							continue;
						}
						// 检查某教学点，某年级教学计划，某教学计划课程是否已经开课
						List<TeachingPlanCourseStatus> _planCourseStatusList = findByCondition(guiplanId, planCourseId, unitId,"Y");
						if(_planCourseStatusList != null && _planCourseStatusList.size() > 0){
							continue;
						}
						TeachingGuidePlan teachingGuidePlan = teachingGuidePlanService.get(guiplanId);
						if(teachingGuidePlan==null){
							continue;
						}
						TeachingPlanCourse planCourse = teachingPlanCourseService.get(planCourseId);
						if(planCourse == null){
							continue;
						}
						if(!courseTermMap.containsKey(_openTerm) && !_openTemList.contains(_openTerm)){
							_openTemList.add(_openTerm);
							String[] year_term = _openTerm.split("_0");
							_message.append("<font color='red'>"+year_term[0]+"年"+("1".equals(year_term[0])?"春":"秋")+"季学期</font><br/>");
						}
						// 创建开课记录
						List<TeachingPlanCourseStatus> _noOpenedList = findByCondition(guiplanId, planCourseId, unitId,"N");
						if(_noOpenedList!=null && _noOpenedList.size()>0){
							courseStatus = _noOpenedList.get(0);
						}else {
							courseStatus = new TeachingPlanCourseStatus();
						}
						courseStatus.setTeachingGuidePlan(teachingGuidePlan);
						courseStatus.setTeachingPlanCourse(planCourse);
						courseStatus.setSchoolIds(unitId);
						courseStatus.setSchoolName(unitName);
						courseStatus.setIsOpen("Y");
						courseStatus.setCheckStatus("openY");
						courseStatus.setOpenStatus("Y");
						courseStatus.setTerm(_openTerm);
						planCourseStatusList.add(courseStatus);
						existedOpenCourseList.add(guiplanId+planCourseId+unitId+"openY");
						//创建开课审核记录
						Date _today = new Date();
						checkOpenCourse = new CheckOpenCourse();
						checkOpenCourse.setApplyid(user.getResourceid());
						checkOpenCourse.setApplyName(user.getCnName());
						checkOpenCourse.setApplyTime(_today);
						checkOpenCourse.setOperate("open");
						checkOpenCourse.setCheckName(user.getCnName());
						checkOpenCourse.setCheckTime(_today);
						checkOpenCourse.setCheckStatus("Y");
						checkOpenCourse.setUpdateTerm(_openTerm);
						checkOpenCourse.setCourseStatusId(courseStatus);
						checkOpenCourseList.add(checkOpenCourse);
					}
					
					// 保存开课记录
					batchSaveOrUpdate(planCourseStatusList);
					// 保存开课审核记录
					checkOpenCourseService.batchSaveOrUpdate(checkOpenCourseList);
					// 保存补充预约记录
					saveOrupdateStudentInfoStuPlan(planCourseStatusList);
				}
			} catch (Exception e) {
				logger.error("处理自动开课逻辑出错", e);
				returnCode = "300";
				message = "自动开课失败！";
			}
		}while(false);
		returnInfoMap.put("returnCode", returnCode);
		returnInfoMap.put("message", message);
		returnInfoMap.put("_message", _message.toString());
		
		return returnInfoMap;
	}

	/**
	 * 获取学生没有学习计划但已经开课的开课信息
	 * @param studentIds
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> findByStudentInfoIds(String studentIds) throws Exception {
		studentIds = ExStringUtils.defaultIfEmpty(studentIds, "");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("studentIds", Arrays.asList(studentIds.split(",")));
		StringBuffer sql = new StringBuffer();
		sql.append("select sop.stuid studentId,sop.picid planCourseId,substr(css.term,0,4) firstYear,substr(css.term,7,1) term,sp.resourceid spid,sp.examresultsid erid from edu_teach_coursestatus css ");
		sql.append("inner join (select so.resourceid stuid,so.branchschoolid,gp.resourceid gpid ,pc.resourceid picid ");
		sql.append("from edu_roll_studentinfo so,edu_teach_guiplan gp,edu_teach_plancourse pc ");
		sql.append("where gp.isdeleted=0 and so.isdeleted=0 and pc.isdeleted=0 and pc.planid=so.teachplanid and gp.gradeid=so.gradeid and gp.planid=so.teachplanid) sop ");
		sql.append("on css.guiplanid=sop.gpid and css.schoolids=sop.branchschoolid and css.plancourseid=sop.picid ");
		sql.append("inner join edu_base_year y on y.isdeleted=0 and y.firstyear=substr(css.term,0,4) ");
		sql.append("left join edu_learn_stuplan sp on sp.isdeleted=0 and sp.studentid=sop.stuid and sp.plansourceid=sop.picid ");
//		sql.append("and sp.examresultsid is null and (sp.yearid != y.resourceid or sp.term != substr(css.term, 7, 1)) ");
		sql.append("where css.isdeleted=0 and css.isopen='Y' and css.openstatus='Y' and  sop.stuid in (:studentIds) ");
		sql.append(" group by sop.stuid,sop.picid,sop.gpid,css.term,sp.resourceid,sp.examresultsid  ");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
	}

	/**
	 * 根据条件获取开课年度和学期
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> findYearInfoAndTerm(Map<String, Object> condition) throws Exception {
		Map<String, Object> map = null;
		StringBuffer sql = new StringBuffer("");
		sql.append("select y.resourceid yearInfo,substr(css.term,7,1) term from edu_teach_coursestatus css,edu_base_year y where css.isdeleted=0 and css.isopen='Y' and css.plancourseid=:planCourseId  and css.schoolids=:schoolId ");
		sql.append(" and css.guiplanid=(select gp.resourceid from edu_teach_guiplan gp where gp.isdeleted=0 and gp.gradeid=:gradeId and gp.planid=:planId and gp.ispublished='Y') ");
		sql.append(" and substr(css.term,0,4)=y.firstyear and y.isdeleted=0 ");
		List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		if(ExCollectionUtils.isNotEmpty(list)){
			map = list.get(0);
		}
		
		return map;
	}

	/**
	 * 设置课程教学类型
	 * @param courseTeachType
	 * @param resourceids
	 * @throws Exception
	 */
	@Override
	public void setCourseTeachType(String courseTeachType, String resourceids) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseTeachType", courseTeachType);
		params.put("resourceids", Arrays.asList(resourceids.split(",")));
		StringBuffer sql = new StringBuffer("update edu_teach_coursestatus css set css.teachtype=:courseTeachType where css.isopen='Y' and css.resourceid in (:resourceids) ");
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql.toString(), params);
	}

	/**
	 * 根据条件获取某条开课记录
	 * @param gradeId
	 * @param planId
	 * @param planCourseId
	 * @param schoolId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TeachingPlanCourseStatus findOneByCondition(String gradeId, String planId, String planCourseId, String schoolId) {
		StringBuffer hql = new StringBuffer("from "+TeachingPlanCourseStatus.class.getSimpleName()+" tpct where tpct.isDeleted=0 and tpct.teachingGuidePlan.grade.resourceid=? and tpct.teachingGuidePlan.isDeleted=0 ");
		hql.append(" and tpct.teachingGuidePlan.teachingPlan.resourceid=? and tpct.teachingPlanCourse.resourceid=? and tpct.schoolIds=? and tpct.isOpen='Y' and tpct.teachingGuidePlan.ispublished='Y'");
		return findUnique(hql.toString(), gradeId,planId,planCourseId,schoolId);
	}
	
	/**
	 * 获取学习计划但已经开课的开课信息
	 * @param studentId
	 * @param gradeId
	 * @param schoolId
	 * @param teachPlanId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findPlanInfoByCondition(String studentId,String gradeId,String schoolId,String teachPlanId,String planCourseId) throws Exception {
		studentId = ExStringUtils.defaultIfEmpty(studentId, "");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("studentId", studentId);
		param.put("gradeId", gradeId);
		param.put("schoolId", schoolId);
		param.put("teachPlanId", teachPlanId);
		StringBuffer sql = new StringBuffer();
		sql.append("select '"+studentId+"' studentId,sop.picid planCourseId,substr(css.term,0,4) firstYear,substr(css.term,7,1) term,sp.resourceid spid from edu_teach_coursestatus css ");
		sql.append("inner join (select '"+schoolId+"' branchschoolid,gp.resourceid gpid ,pc.resourceid picid from edu_teach_guiplan gp,edu_teach_plancourse pc ");
		sql.append("where gp.isdeleted=0  and pc.isdeleted=0 and gp.gradeid=:gradeId and gp.planid=pc.planid and pc.planid=:teachPlanId ) sop ");
		sql.append("on css.guiplanid=sop.gpid and css.schoolids=sop.branchschoolid and css.plancourseid=sop.picid ");
		sql.append("inner join edu_base_year y on y.isdeleted=0 and y.firstyear=substr(css.term,0,4) ");
		sql.append("left join edu_learn_stuplan sp on sp.isdeleted=0 and sp.studentid=:studentId and sp.plansourceid=sop.picid ");
		//sql.append("and sp.examresultsid is null and (sp.yearid != y.resourceid or sp.term != substr(css.term, 7, 1)) ");
		sql.append("where css.isdeleted=0 and css.isopen='Y' and css.openstatus='Y' and sop.picid not in ("+("".equals(planCourseId) ?"''":planCourseId)+")");
		sql.append(" group by sop.picid,sop.gpid,css.term,sp.resourceid  ");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
	}

	/**
	 * 获取某个教学点某个年级某门课程的开课学期（年度Id和学期)
	 * @param planCourseId
	 * @param gradeId
	 * @param schoolId
	 * @param teachPlanId
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findYearAndTerm(String planCourseId,String gradeId, String schoolId, String teachPlanId) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("planCourseId", planCourseId);
		param.put("gradeId", gradeId);
		param.put("schoolId", schoolId);
		param.put("teachPlanId", teachPlanId);
		StringBuffer sql = new StringBuffer();
		sql.append("select y.resourceid yearInfoId,substr(css.term, 7, 1) term from edu_teach_coursestatus css ");
		sql.append("inner join edu_teach_guiplan gp on gp.isdeleted=0 and gp.gradeid=:gradeId and gp.planid=:teachPlanId and gp.resourceid=css.guiplanid ");
		sql.append("inner join edu_base_year y on y.isdeleted=0 and y.firstyear=substr(css.term, 0, 4) ");
		sql.append("where css.isdeleted=0 and css.isopen='Y' and css.schoolids=:schoolId and css.plancourseid=:planCourseId  ");
		sql.append("group by y.resourceid,css.term  ");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
	}

	/**
	 * 设置课程考试形式
	 * @param courseExamForm
	 * @param resourceids
	 * @throws Exception
	 */
	@Override
	public void saveCourseExamForm(String courseExamForm, String resourceids) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("courseExamForm", courseExamForm);
		params.put("resourceids", Arrays.asList(resourceids.split(",")));
		
		//设置考试形式时，把学生成绩录入、补考名单的考试形式一起修改 ，考试形式标识字段：isMachineexam  Y|N		
		Map<String, Object> params2 = new HashMap<String, Object>();
		params2.put("resourceids", Arrays.asList(resourceids.split(",")));
		if("3".equals(courseExamForm)){
			params2.put("isMachineExam", "Y");
		}else{
			params2.put("isMachineExam", "N");
		}
		
		StringBuffer sql = new StringBuffer("update edu_teach_coursestatus css set css.examform=:courseExamForm where css.isopen='Y' and css.resourceid in (:resourceids) ");
		StringBuffer sql1 = new StringBuffer();//成绩表
		StringBuffer sql2 = new StringBuffer();//补考名单表
		
		sql1.append(" update edu_teach_examresults e set e.ismachineexam =:isMachineExam where e.resourceid in       ");
		sql1.append(" (select e.resourceid from edu_teach_coursestatus k                                   ");
		sql1.append(" join edu_teach_examresults e on e.majorcourseid = k.plancourseid and e.isdeleted = 0 ");
		sql1.append(" join edu_roll_studentinfo s on s.branchschoolid=k.schoolids                          ");
		sql1.append("  and s.resourceid = e.studentid and s.isdeleted=0                                    ");
		sql1.append(" join edu_teach_guiplan g on g.resourceid = k.guiplanid and                           ");
		sql1.append("  g.planid = s.teachplanid and g.gradeid = s.gradeid and g.isdeleted = 0              ");
		sql1.append(" where k.resourceid in (:resourceids) )                           ");
		
		sql2.append(" update edu_teach_makeuplist m set m.ismachineexam =:isMachineExam where m.resourceid in      ");
		sql2.append(" (select m.resourceid from edu_teach_coursestatus k                                 ");
		sql2.append(" join edu_teach_makeuplist m on m.plansourceid = k.plancourseid and m.isdeleted =0  ");
		sql2.append(" join edu_roll_studentinfo s on s.branchschoolid=k.schoolids                        ");
		sql2.append("  and s.resourceid = m.studentid and s.isdeleted=0                                  ");
		sql2.append(" join edu_teach_guiplan g on g.resourceid = k.guiplanid and                         ");
		sql2.append(" g.planid = s.teachplanid and g.gradeid = s.gradeid and g.isdeleted = 0             ");
		sql2.append(" where k.resourceid in (:resourceids) )                        ");
		
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql.toString(), params);
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql1.toString(), params2);
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql2.toString(), params2);
		
	}
	
	@Override
	public TeachingPlanCourseStatus findOneByCondition2(String gradeId, String planId, String courseId, String schoolId) {
		StringBuffer hql = new StringBuffer("from "+TeachingPlanCourseStatus.class.getSimpleName()+" tpct where tpct.isDeleted=0 and tpct.teachingGuidePlan.grade.resourceid=? and tpct.teachingGuidePlan.isDeleted=0 ");
		hql.append(" and tpct.teachingGuidePlan.teachingPlan.resourceid=? and tpct.teachingPlanCourse.course.resourceid=? and tpct.teachingPlanCourse.teachingPlan=tpct.teachingGuidePlan.teachingPlan and tpct.schoolIds=? and tpct.isOpen='Y' and tpct.teachingGuidePlan.ispublished='Y'");
		return findUnique(hql.toString(), gradeId,planId,courseId,schoolId);
	}
	
	@Override
	public List<TeachingPlanCourseCodeVo> findTeachingPlanCourseCodeByCondition(Map<String, Object> condition) throws ServiceException {
		List<String> paramList = new ArrayList<String>();
		String sql = getTeachingPlanCourseCodeSql(condition,paramList);
		List<TeachingPlanCourseCodeVo> result = new ArrayList<TeachingPlanCourseCodeVo>();
		try {
			result = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql, paramList.toArray(), TeachingPlanCourseCodeVo.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@Override
	/**
	 * 按照教学点、年级、层次、学习形式、专业、班级、课程从1开始编号
	 */
	public Page findTeachingPlanCourseCodeByCondition(Map<String, Object> condition, Page page) {
		List<String> paramList = new ArrayList<String>();
		String sql = getTeachingPlanCourseCodeSql(condition,paramList);
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(page,sql, paramList.toArray(), TeachingPlanCourseCodeVo.class);
	}
	private String getTeachingPlanCourseCodeSql(Map<String, Object> condition,List<String> paramList) {
		StringBuffer sb = new StringBuffer();
		sb.append("select gradeName,unitName,classicName,teachingType,majorName,classesName,term,courseCode,courseName,code from (select");
		sb.append(" u.resourceid unitid,g.resourceid gradeid,ci.resourceid classicid,m.resourceid majorid,cl.resourceid classesid,c.resourceid courseid,");
		sb.append(" g.gradename,u.unitname,ci.classicname,m.majorname,cl.classesname,cl.teachingtype,cs.term,c.coursecode,c.coursename,u.unitcode||'_'||");
		sb.append(" row_number()over(order by g.gradename,u.unitcode,ci.resourceid,cl.teachingtype,m.majorcode,cl.classesname,cs.term,c.coursecode) code");
		sb.append(" from edu_teach_coursestatus cs ");
		sb.append(" join hnjk_sys_unit u on u.resourceid=cs.schoolids");
		sb.append(" join edu_teach_guiplan gp on gp.resourceid=cs.guiplanid");
		sb.append(" join edu_base_grade g on g.resourceid=gp.gradeid");
		sb.append(" join edu_teach_plancourse pc on pc.resourceid=cs.plancourseid");
		sb.append(" join edu_base_course c on c.resourceid=pc.courseid");
		sb.append(" join edu_roll_studentinfo si on si.branchschoolid=u.resourceid and si.gradeid=g.resourceid and si.teachplanid=pc.planid");
		sb.append(" join edu_base_classic ci on ci.resourceid=si.classicid");
		sb.append(" join edu_base_major m on m.resourceid=si.majorid");
		sb.append(" join edu_roll_classes cl on cl.resourceid=si.classesid");
		sb.append(" where cs.isopen='Y' and cs.isdeleted=0 and cl.isdeleted=0 and pc.isdeleted=0");
		sb.append(" group by u.resourceid,u.unitcode,g.resourceid,ci.resourceid,m.resourceid,m.majorcode,cl.resourceid,c.resourceid,");
		sb.append(" u.unitname,g.gradename,ci.classicname,m.majorname,cl.classesname,cl.teachingtype,cs.term,c.coursecode,c.coursename");
		sb.append(" )cpc where 1=1");
		if(condition.containsKey("unitid")){
			sb.append(" and cpc.unitid=?");
			paramList.add(condition.get("unitid").toString());
		}else if (condition.containsKey("branchSchool")) {
			sb.append(" and cpc.unitid=?");
			paramList.add(condition.get("branchSchool").toString());
		}
		if(condition.containsKey("gradeid")){
			sb.append(" and cpc.gradeid=?");
			paramList.add(condition.get("gradeid").toString());
		}
		if(condition.containsKey("classicid")){
			sb.append(" and cpc.classicid=?");
			paramList.add(condition.get("classicid").toString());
		}
		if(condition.containsKey("teachingType")){
			sb.append(" and cpc.teachingType=?");
			paramList.add(condition.get("teachingType").toString());
		}
		if(condition.containsKey("majorid")){
			sb.append(" and cpc.majorid=?");
			paramList.add(condition.get("majorid").toString());
		}
		if(condition.containsKey("classesid")){
			sb.append(" and cpc.classesid=?");
			paramList.add(condition.get("classesid").toString());
		}else if (condition.containsKey("classesId")) {
			sb.append(" and cpc.classesid=?");
			paramList.add(condition.get("classesId").toString());
		}
		if(condition.containsKey("courseid")){
			sb.append(" and cpc.courseid=?");
			paramList.add(condition.get("courseid").toString());
		}else if (condition.containsKey("courseId")) {
			sb.append(" and cpc.courseid=?");
			paramList.add(condition.get("courseId").toString());
		}
		if(condition.containsKey("code")){
			sb.append(" and (cpc.code=? or cpc.code like ?");
			paramList.add(condition.get("code").toString());
			paramList.add("%_"+ExStringUtils.removeStartString(condition.get("code").toString(), "_"));
		}
		return sb.toString();
	}

}
