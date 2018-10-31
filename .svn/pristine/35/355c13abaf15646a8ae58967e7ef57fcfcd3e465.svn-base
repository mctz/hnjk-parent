package com.hnjk.edu.learning.service.jdbcimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
/**
 * 在线学习模块公共JDBCService实现
 * <code>LearningJDBCServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-17 上午10:45:25
 * @see 
 * @version 1.0
 */
@Transactional
@Service("learningJDBCService")
public class LearningJDBCServiceImpl extends BaseSupportJdbcDao implements ILearningJDBCService{
	private Logger logger =  LoggerFactory.getLogger(LearningJDBCServiceImpl.class);	
	
	@Override
	@Transactional(readOnly=true)
	public Page statStudentBbsTopicAndReply(Map<String, Object> condition,Page objPage) throws ServiceException {	
		String q = " where 1=1 ";
		if(condition.containsKey("unitId")) {
			q += " and stuinfo.branchschoolid=:unitId ";
		}
		if(condition.containsKey("gradeid")) {
			q += " and stuinfo.gradeid=:gradeid ";
		}
		if(condition.containsKey("classic")) {
			q += " and stuinfo.classicid=:classic ";
		}
		if(condition.containsKey("major")) {
			q += " and stuinfo.majorid=:major ";
		}
		if(condition.containsKey("studyNo")) {
			q += " and stuinfo.studyno like '%"+condition.get("studyNo").toString()+"%' ";
		}
		if(condition.containsKey("studentName")) {
			q += " and lower(stuinfo.studentName) like '%"+condition.get("studentName").toString().toLowerCase()+"%' ";
		}
		if(condition.containsKey("courseId")) {
			q += " and course.resourceid=:courseId ";
		} 
		
		String topicQuery = getQueryTimeString(condition, "t.fillindate") + getQueryIsBestString(condition, "t.isBest");
		String replyQuery = getQueryTimeString(condition, "r.replydate") + getQueryIsBestString(condition, "r.isBest");
				
		StringBuffer sql = new StringBuffer();	
		String selCountHeader = "select count(*) ";
		String selHeader = "select unit.unitname,unit.unitshortname,grade.gradename,major.majorname,classic.classicname,course.coursename,stuinfo.studyno,stuinfo.studentname,uinfo.username,nvl(rts.topic,0) topic,nvl(rts.reply,0) reply,nvl(rts.isbest,0) isbest ";
		sql.append("from Edu_Roll_Studentinfo stuinfo left join Edu_Bbs_Userinfo uinfo on stuinfo.sysuserid=uinfo.userid left join  ");
		sql.append("  (select rs.sid,rs.courseid,sum(rs.topic) topic,sum(rs.reply) reply,sum(rs.isbest) isbest ");
		sql.append("  from ( ");
		sql.append("      select u.sid,u.courseid, u.stype,decode(u.stype,'topic',count(*),0) topic,decode(u.stype,'reply',count(*),0) reply,decode(u.isbest,'Y',count(*),0) isbest ");
		sql.append("       from ( ");
		sql.append("      select t.resourceid,t.fillinmanid sid,t.courseid,t.isbest,'topic' stype from edu_bbs_topic t where t.isdeleted=0 and t.parentid is null and t.courseid is not null "+topicQuery);
		sql.append("      union all ");
		sql.append("      select r.resourceid,r.replymanid sid,tp.courseid,r.isbest, 'reply' stype from edu_bbs_reply r left join edu_bbs_topic tp on r.topicid=tp.resourceid where r.isdeleted=0 and tp.courseid is not null "+replyQuery);
		sql.append("      ) u  group by u.sid,u.courseid, u.stype,u.isbest) rs group by rs.sid,rs.courseid) rts ");
		sql.append("   on uinfo.resourceid=rts.sid join edu_base_course course on rts.courseid=course.resourceid join hnjk_sys_unit unit on stuinfo.branchschoolid=unit.resourceid ");
		sql.append("   join edu_base_grade grade on stuinfo.gradeid=grade.resourceid join edu_base_major major on stuinfo.majorid=major.resourceid join edu_base_classic classic on stuinfo.classicid=classic.resourceid ");
		sql.append(q);
		
		String orderby = " order by course.courseName,unit.unitname,grade.gradeName,major.majorname,classic.classicname,stuinfo.studentname ";
		if(objPage.isOrderBySetted()) {
			orderby = " order by "+objPage.getOrderBy()+" "+ objPage.getOrder();
		}
		
		try {	
			if(objPage.isAutoCount()){
				Long count = this.baseJdbcTemplate.findForLong(selCountHeader+sql.toString(), condition);
				objPage.setTotalCount(count.intValue());
			}
			List resutList = baseJdbcTemplate.findForListMap(" select * from (select r.*,rownum rn from ("+selHeader+sql.toString()+orderby+") r where rownum<="+(objPage.getFirst()+objPage.getPageSize())+") where rn>"+objPage.getFirst(), condition);
			objPage.setResult(resutList);
			return objPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	//获取时间段查询条件
	private String getQueryTimeString(Map<String, Object> condition,String field){
		String query = "";
		if(condition.containsKey("startTime")){
			query += " and "+ field +" >= to_date('"+condition.get("startTime").toString()+"','yyyy-MM-dd') ";
		}
		if(condition.containsKey("endTime")){
			query += " and "+ field +" < to_date('"+condition.get("endTime").toString()+"','yyyy-MM-dd')+1 ";
		}
		return query;
	}
	//获取优秀贴查询条件
	private String getQueryIsBestString(Map<String, Object> condition,String field){
		String query = "";
		if(condition.containsKey("isBest") && Constants.BOOLEAN_YES.equals(condition.get("isBest").toString())){
			query += " and "+ field +" ='Y' ";
		}
		return query;
	}

	@Override
	@Transactional(readOnly=true)
	public Page statEdumanagerBbsTopicAndReply(Map<String, Object> condition, Page objPage) throws ServiceException {
		String q = " where 1=1 ";
		if(condition.containsKey("unitId")) {
			q += " and users.unitid=:unitId ";
		}
		if(condition.containsKey("cnName")) {
			q += " and lower(users.cnName) like '%"+condition.get("cnName").toString().toLowerCase()+"%' ";
		}
		if(condition.containsKey("username")) {
			q += " and lower(users.username) like '%"+condition.get("username").toString().toLowerCase()+"%' ";
		}
		if(condition.containsKey("courseId")) {
			q += " and course.resourceid=:courseId ";
		}
		String topicTime = getQueryTimeString(condition, "t.fillindate");
		String replyTime = getQueryTimeString(condition, "r.replydate");
		
		StringBuffer sql = new StringBuffer();	
		String selCountHeader = "select count(*) ";
		String selHeader = "select unit.unitname,course.coursename,users.cnname,users.username,nvl(rts.topic,0) topic,nvl(rts.reply,0) reply ";
		sql.append("from hnjk_sys_users users left join Edu_Bbs_Userinfo uinfo on users.resourceid=uinfo.userid and users.usertype<>'student' left join  ");
		sql.append("  (select rs.sid,rs.courseid,sum(rs.topic) topic,sum(rs.reply) reply ");
		sql.append("  from ( ");
		sql.append("      select u.sid,u.courseid, u.stype,decode(u.stype,'topic',count(*),0) topic,decode(u.stype,'reply',count(*),0) reply ");
		sql.append("       from ( ");
		sql.append("      select t.resourceid,t.fillinmanid sid,t.courseid,'topic' stype from edu_bbs_topic t where t.isdeleted=0 and t.parentid is null "+topicTime);
		sql.append("      union all ");
		sql.append("      select r.resourceid,r.replymanid sid,tp.courseid, 'reply' stype from edu_bbs_reply r left join edu_bbs_topic tp on r.topicid=tp.resourceid where r.isdeleted=0 "+replyTime);
		sql.append("      ) u  group by u.sid,u.courseid, u.stype) rs group by rs.sid,rs.courseid) rts ");
		sql.append("   on uinfo.resourceid=rts.sid join edu_base_course course on rts.courseid=course.resourceid join hnjk_sys_unit unit on users.unitid=unit.resourceid ");
		sql.append(q);
		String orderby = "order by course.courseName,users.cnname,unit.unitname ";
		if(objPage.isOrderBySetted()) {
			orderby = " order by "+objPage.getOrderBy()+" "+ objPage.getOrder();
		}
		
		try {			
			if(objPage.isAutoCount()){
				Long count = this.baseJdbcTemplate.findForLong(selCountHeader+sql.toString(), condition);
				objPage.setTotalCount(count.intValue());
			}
			List resutList = baseJdbcTemplate.findForListMap(" select * from (select r.*,rownum rn from ("+selHeader+sql.toString()+orderby+") r where rownum<="+(objPage.getFirst()+objPage.getPageSize())+") where rn>"+objPage.getFirst(), condition);
			objPage.setResult(resutList);
			return objPage;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	@Override
	public List<Map<String,Object>> statStudentActiveCourseExam(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		String q = "";
		if(condition.containsKey("courseId")) {
			q += " and course.resourceid=:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("gradeid")){
			q += " and stuinfo.gradeid=:gradeid ";
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("classicid")){
			q += " and stuinfo.classicid=:classicid ";
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("unitid")){
			q += " and stuinfo.branchschoolid=:unitid ";
			values.put("unitid", condition.get("unitid"));
		}
		if(condition.containsKey("teachingtype")){
			q += " and stuinfo.teachingtype=:teachingtype ";
			values.put("teachingtype", condition.get("teachingtype"));
		}
		if(condition.containsKey("studentStatus")){
			q += " and stuinfo.studentStatus in("+condition.get("studentStatus")+") ";
		}
		values.put("isdeleted", 0);
		
		StringBuffer sql = new StringBuffer();	
		sql.append(" select r.coursename,r.unitname,r.gradename,r.classicname,r.teachingtype,r.nodename,sum(correctcount) correctcount,sum(mistakecount) mistakecount ");
		sql.append(" from (select course.coursename,unit.unitname,grade.gradename,classic.classicname,stuinfo.teachingtype,syllabus.nodename,decode(stuexam.iscorrect,'Y',count(*),0) correctcount,decode(stuexam.iscorrect,'N',count(*),0) mistakecount ");
		sql.append("       from edu_lear_studentcourseexam stuexam,edu_roll_studentinfo stuinfo,edu_lear_courseexam exam,edu_teach_syllabustree syllabus,edu_base_course course ");
		sql.append("            ,edu_base_grade grade,edu_base_classic classic,hnjk_sys_unit unit ");
		sql.append("       where stuexam.isdeleted=:isdeleted and stuexam.studentid=stuinfo.resourceid and stuexam.courseexamid=exam.resourceid ");
		sql.append("             and exam.syllabustreeid=syllabus.resourceid and syllabus.courseid=course.resourceid ");
		sql.append("             and stuinfo.gradeid=grade.resourceid and stuinfo.classicid=classic.resourceid and stuinfo.branchschoolid=unit.resourceid ");
		sql.append("             "+q);
		sql.append("       group by course.coursename,unit.unitname,grade.gradename,classic.classicname,stuinfo.teachingtype,syllabus.nodename,stuexam.iscorrect) r ");
		sql.append(" group by r.coursename,r.unitname,r.gradename,r.classicname,r.teachingtype,r.nodename ");
		sql.append(" order by r.coursename,r.unitname,r.gradename,r.classicname,r.teachingtype,r.nodename ");
		
		try {	
			List<Map<String,Object>> resutList = baseJdbcTemplate.findForListMap(sql.toString(), values);
			return resutList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//查看学生答题情况
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> stateStudentActiveCourseExam(Map<String, Object> condition) {
		Map<String, Object> values = new HashMap<String, Object>();
		String q = "";
		if(condition.containsKey("schoolId")){
			q += " and cltt.plancourseid=pc.resourceid and cltt.classesid=classes.resourceid and cltt.term=y.firstyear||'_0'||plan.term ";
			values.put("schoolId", condition.get("schoolId"));
		} else {
			if(condition.containsKey("teacherId") && condition.containsKey("classesIds") ){
				q += " and cttt.plancourseid=pc.resourceid and cttt.classesid=classes.resourceid and cttt.term=y.firstyear||'_0'||plan.term ";
				values.put("teacherId", condition.get("teacherId"));
				values.put("classesIds", condition.get("classesIds"));
			}else{
				if(condition.containsKey("teacherId")){
					q += " and cttt.plancourseid=pc.resourceid and cttt.classesid=classes.resourceid and cttt.term=y.firstyear||'_0'||plan.term ";
					values.put("teacherId", condition.get("teacherId"));
				}
				if(condition.containsKey("classesIds")){
					q += " and cttt.plancourseid=pc.resourceid and cttt.classesid=classes.resourceid and cttt.term=y.firstyear||'_0'||plan.term ";
					values.put("classesIds", condition.get("classesIds"));
				}
			}
		}
		if(condition.containsKey("courseId")) {
			q += " and course.resourceid=:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("classesId")) {
			q += " and classes.resourceid=:classesId ";
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("yearInfoId")){
			q += " and plan.yearid=:yearInfoId ";
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			q += " and plan.term=:term ";
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("unitid")){
			q += " and stuinfo.branchschoolid=:unitid ";
			values.put("unitid", condition.get("unitid"));
		}
		if(condition.containsKey("studentStatus")){
			q += " and stuinfo.studentStatus in("+condition.get("studentStatus")+") ";
		}
		values.put("isdeleted", 0);
		
		StringBuffer sql = new StringBuffer();	
		sql.append(" select r.coursename,r.syllabusid,r.nodename,r.examid,r.showorder,sum(correctcount) correctcount,sum(mistakecount) mistakecount,r.classesname "); 
		sql.append("		 from (select course.coursename,syllabus.resourceid syllabusid,syllabus.nodename,exam.resourceid examid,exam.showorder,decode(stuexam.iscorrect,'Y',count(*),0) correctcount,decode(stuexam.iscorrect,'N',count(*),0) mistakecount,classes.classesname ");  
		sql.append("           from edu_lear_studentcourseexam stuexam,edu_roll_studentinfo stuinfo,edu_learn_stuplan plan,edu_teach_plancourse pc,edu_lear_courseexam exam,edu_teach_syllabustree syllabus,edu_base_course course  ");
		sql.append("                ,edu_base_grade grade,edu_base_classic classic,hnjk_sys_unit unit,EDU_ROLL_CLASSES classes,edu_base_year y ");
		if(condition.containsKey("schoolId")){
			sql.append("           ,(select tt.plancourseid,tt.classesid,tt.term from edu_teach_timetable tt,edu_roll_classes cl where tt.classesid=cl.resourceid and tt.isdeleted=0 and cl.isdeleted=0 and cl.orgunitid=:schoolId group by tt.plancourseid,tt.classesid,tt.term) cltt ");  
		}else {
			if(condition.containsKey("teacherId") && condition.containsKey("classesIds") ){
				sql.append("           ,(select tt.plancourseid,tt.classesid,tt.term from edu_teach_timetable tt where tt.isdeleted=0 and (tt.teacherid=:teacherId or tt.classesid in (:classesIds)) group by tt.plancourseid,tt.classesid,tt.term) cttt ");
			} else {
				if(condition.containsKey("teacherId")){
					sql.append("           ,(select tt.plancourseid,tt.classesid,tt.term from edu_teach_timetable tt where tt.isdeleted=0 and tt.teacherid=:teacherId group by tt.plancourseid,tt.classesid,tt.term) cttt ");
				}
				if(condition.containsKey("classesIds")){
					sql.append("           ,(select tt.plancourseid,tt.classesid,tt.term from edu_teach_timetable tt where tt.isdeleted=0 and tt.classesid in (:classesIds) group by tt.plancourseid,tt.classesid,tt.term) cttt ");
				}
			}
		}
		sql.append("           where stuexam.isdeleted=0 and plan.isdeleted = 0 and exam.isdeleted=0 and exam.ispublished='Y' and stuexam.studentid=stuinfo.resourceid and stuexam.courseexamid=exam.resourceid ");  
		sql.append("                 and exam.syllabustreeid=syllabus.resourceid and syllabus.courseid=course.resourceid and plan.studentid=stuinfo.resourceid and plan.plansourceid=pc.resourceid and pc.courseid=course.resourceid  ");
		sql.append("                 and stuinfo.gradeid=grade.resourceid and stuinfo.classicid=classic.resourceid and stuinfo.branchschoolid=unit.resourceid and stuinfo.classesid=classes.resourceid and plan.yearid=y.resourceid and y.isdeleted=0 "); 
		sql.append("                 "+q);
		sql.append("           group by course.coursename,syllabus.resourceid,syllabus.nodename,exam.resourceid,exam.showorder,stuexam.iscorrect,classes.classesname) r "); 
		sql.append("     group by r.coursename,r.syllabusid,r.nodename,r.examid,r.showorder,r.classesname ");
		sql.append("     order by r.coursename,r.syllabusid,r.nodename,r.showorder,r.classesname ");
		try {	
			List<Map<String,Object>> resutList = baseJdbcTemplate.findForListMap(sql.toString(), values);
			return resutList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/*
	 * 查看学生随堂练习得分累积情况
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.ILearningJDBCService#scoreStudentActiveCourseExam(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public Page scoreStudentActiveCourseExam(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		try {	
//			if(objPage.isAutoCount()){
//				Long count = this.baseJdbcTemplate.findForLong(" select count(*) from ( "+sql.toString()+") ", condition);
//				objPage.setTotalCount(count.intValue());
//			}
//			List resutList = baseJdbcTemplate.findForListMap(" select * from (select r.*,rownum rn from ("+sql.toString()+") r where rownum<="+(objPage.getFirst()+objPage.getPageSize())+") where rn>"+objPage.getFirst(), condition);
//			objPage.setResult(resutList);
			StringBuffer sqlCount = getScoreStudentActiveCourseExamSql(condition, params, 0);//计数sql				
			objPage.setAutoCount(false);//不自动计数
			long counts = baseJdbcTemplate.getJdbcTemplate().queryForLong(sqlCount.toString(), params);
			objPage.setTotalCount(Long.valueOf(counts).intValue());//手动设置总数
			
			StringBuffer sql = getScoreStudentActiveCourseExamSql(condition, params, 1);//查询sql		
			List<Map<String, Object>> stuExamScoreInfoList = baseJdbcTemplate.findForListMap(sql.toString(), params);
			objPage.setResult(stuExamScoreInfoList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objPage;
	}
	/**
	 * 学生随堂练习得分累积情况sql
	 * @param condition
	 * @param params
	 * @param type 0=计数,1=查询
	 * @return
	 */
	private StringBuffer getScoreStudentActiveCourseExamSql(Map<String, Object> condition, Map<String, Object> params, int type) {
		params.clear();
		StringBuffer sql = new StringBuffer();	
		if(type==0){
			sql.append(" select count(pa.resourceid) ");
		} else {
			sql.append(" select pa.studentid,  si.studyno, si.studentname,y.yearName,(case when pa.orderexamterm is not null then pa.orderexamterm else pa.term end) term, course.resourceid courseid, course.coursename,nvl(sp.newCount,0)+nvl(sp.oldCount,0) correctcount,classes.classesname,nvl(sn.submitNum,0) submitNum, ");
			if(condition.containsKey("courseExamCount")){//先算好课程发布的随堂练习总数
				sql.append("     "+condition.get("courseExamCount")+" + nvl(sp.oldCount,0) counts ");
			}	
		}		
		sql.append("   from edu_learn_stuplan pa ");
		sql.append("   left join edu_teach_plancourse ca on pa.plansourceid=ca.resourceid and ca.isdeleted = 0 ");
		sql.append("   join edu_roll_studentinfo si on si.resourceid=pa.studentid ");
//		sql.append("   join edu_base_course course on (course.resourceid = ca.courseid or course.resourceid=pa.planoutcourseid) ");
		sql.append("   join edu_base_course course on course.resourceid = ca.courseid ");
		sql.append("   join edu_base_year y on y.resourceid=(case when pa.orderexamyear is not null then pa.orderexamyear else pa.yearid end) ");
		sql.append("   join edu_roll_classes classes on si.classesid=classes.resourceid ");
		//sql.append("   join edu_teach_timetable tt on tt.plancourseid=pa.plansourceid and tt.isdeleted=0 and y.firstyear||'_0'||pa.term=tt.term and tt.classesid=classes.resourceid ");
		if(condition.containsKey("teacherId")){
			sql.append(" join (select tt.classesid,tt.plancourseid,tt.courseid,tt.teacherid,tt.term from  EDU_TEACH_TIMETABLE tt where tt.isdeleted=0 group by tt.classesid,tt.plancourseid,tt.courseid,tt.teacherid,tt.term)te");
		    sql.append(" on classes.resourceid = te.classesid and te.courseid = course.resourceid and pa.plansourceid = te.plancourseid and te.term like '%'||y.firstyear||'_0'||pa.term||'%' ");
		    sql.append(" and te.teacherid =:teacherId ");
		    params.put("teacherId",condition.get("teacherId"));
		}
		//开课记录
		sql.append(" join edu_teach_guiplan gp on gp.planid=si.teachplanid and gp.gradeid=si.gradeid and gp.isdeleted=0 ");
		sql.append(" join edu_teach_coursestatus cs on cs.guiplanid=gp.resourceid and cs.plancourseid=ca.resourceid and cs.schoolids=si.BRANCHSCHOOLID and cs.isdeleted=0 and cs.teachtype='networkTeach' ");
		if(type==1){
			sql.append("   left join ");
			sql.append("   (select rrs.studentid,rrs.courseid,sum(rrs.newCount) newCount,sum(rrs.oldCount) oldCount ");
			sql.append("    from(select rs.studentid,rs.courseid,decode(rs.ispublished,'Y',count(*),0) newCount,decode(rs.ispublished,'N',count(*),0) oldCount ");
			sql.append("      from (select distinct stuexam.courseexamid,stuexam.studentid,t.courseid,case when cexam.isdeleted=1 or cexam.ispublished<>'Y' then 'N' else 'Y' end ispublished "); 
			sql.append("            from edu_lear_studentcourseexam stuexam ");
			sql.append("            join edu_lear_courseexam cexam on cexam.resourceid=stuexam.courseexamid ");
			sql.append("            join edu_teach_syllabustree t on t.resourceid=cexam.syllabustreeid ");
			if(condition.containsKey("studyNo")){
				sql.append(" join edu_roll_studentinfo info on info.resourceid=stuexam.studentid and info.studyno like :studyNo ");
				params.put("studyNo",condition.get("studyNo")+"%");
			}
			sql.append("            where stuexam.isdeleted=0 and t.courseid=:courseId ");
			params.put("courseId",condition.get("courseId"));
			sql.append("            and stuexam.iscorrect='Y' ) rs ");
			sql.append("      group by rs.studentid,rs.courseid, rs.ispublished ");
			sql.append("   ) rrs ");
			sql.append("   group by rrs.studentid,rrs.courseid ) sp ");
			sql.append(" on pa.studentid=sp.studentid and course.resourceid=sp.courseid ");	
			sql.append("   left join ");
			sql.append("(select sce.studentid,syt.courseid,nvl(count(sce.resourceid),0)  submitNum from edu_lear_studentcourseexam sce ");
			sql.append("inner join edu_lear_courseexam ce on ce.isdeleted=0 and sce.courseexamid=ce.resourceid ");
			sql.append("inner join edu_teach_syllabustree syt on syt.isdeleted=0 and syt.resourceid=ce.syllabustreeid ");
			sql.append("where sce.isdeleted=0 and syt.courseid=:courseId  and sce.result is not null ");
			params.put("courseId",condition.get("courseId"));
			sql.append("group by sce.studentid,syt.courseid) sn on sn.courseid=course.resourceid and sn.studentid=pa.studentid ");
		} 		
		sql.append(" where pa.isdeleted=0 ");	
		if(condition.containsKey("yearInfoId")){
			sql.append(" and (pa.yearid=:yearInfoId or pa.orderexamyear=:yearInfoId) ");
			params.put("yearInfoId",condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			sql.append(" and (pa.term=:term or pa.orderexamterm=:term) ");
			params.put("term",condition.get("term"));
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and si.studyNo like :studyNo ");
			params.put("studyNo",condition.get("studyNo")+"%");
		}
		if(condition.containsKey("classesId")){
			sql.append(" and si.classesid =:classesId ");
			params.put("classesId",condition.get("classesId"));
		}
		if(condition.containsKey("studentName")){
			sql.append(" and si.studentName like :studentName ");
			params.put("studentName","%"+condition.get("studentName").toString()+"%");
		}
		if (condition.containsKey("classesIdList")){
			sql.append(" and si.classesid in (:classesIdList) ");
			params.put("classesIdList",condition.get("classesIdList"));
		}
		if(condition.containsKey("schoolId")){
			sql.append(" and si.branchschoolid=:schoolId ");
			params.put("schoolId",condition.get("schoolId"));
		}else if (condition.containsKey("unitId")){
			sql.append(" and si.branchschoolid=:unitId ");
			params.put("unitId",condition.get("unitId"));
		}
		if(condition.containsKey("studentStatus")){
			sql.append(" and si.studentStatus in("+condition.get("studentStatus")+") ");
		}
		sql.append(" and course.resourceid=:courseId ");
		params.put("courseId",condition.get("courseId"));
		
		if(condition.containsKey("studentIds")){
			sql.append(" and si.resourceid in (:studentIds) ");
			params.put("studentIds",condition.get("studentIds"));
		}
		
		if(type==1){
			sql.append(" order by si.studyno ");
		}
		return sql;
	}
	/*
	 * 查看随堂练习分布情况
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.ILearningJDBCService#distributeStudentActiveCourseExam(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> distributeStudentActiveCourseExam(Map<String, Object> condition) throws ServiceException {
		StringBuffer sql = new StringBuffer();	
		sql.append("select st.*,c.coursename from (");
		sql.append("    select t.*,");
		sql.append("          (select count(s.resourceid) from edu_lear_courseexam s ");
		sql.append("           join edu_lear_exams es on es.resourceid=s.examid ");
		sql.append("           where s.isdeleted=0 and s.ispublished='Y' and es.examtype!='6' and s.syllabustreeid=t.resourceid) counts ");
		sql.append("    from edu_teach_syllabustree t ");
		sql.append("    where t.isdeleted=0 and t.courseId=:courseId ");
		sql.append("    start with t.parentid is null ");
		sql.append("    connect by prior t.resourceid=t.parentid ");
		sql.append("    order siblings by t.nodeLevel,t.showOrder ) st ");
		sql.append(" join edu_base_course c on c.resourceid=st.courseid ");
		try {	
			List<Map<String,Object>> resutList = baseJdbcTemplate.findForListMap(sql.toString(), condition);
			return resutList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据教学计划课程查询对应的统考课程成绩
	 * @param studentId   学生ID
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> findStatCouseExamResults(String studentId) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("studentId", studentId);
		map.put("isDeleted", 0);
		map.put("isIdented",Constants.BOOLEAN_YES);
		
		StringBuffer sql = new StringBuffer();
		/*
		第一版查询教学计划与统考课程对照表
		sql.append(" select scs.plancourseid, srs.scoretype ,srs.passtime ,srs.COURSEID");
		sql.append("   from edu_teach_statexamresults srs,EDU_TEACH_STATCOURSE scs ");	
		sql.append("  where srs.isdeleted=0 and scs.isdeleted=0 ");	
		sql.append("    and scs.courseid = srs.courseid ");	
		sql.append("    and srs.isidented='Y' ");	
		sql.append( "   and srs.studentid =:studentId ");	
		sql.append("    and exists (");       
		sql.append("           select pc.resourceid from edu_teach_plancourse pc ,EDU_TEACH_PLAN tp,edu_roll_studentinfo stu"); 
		sql.append("        	where pc.isdeleted   = 0 "); 
		sql.append("			  and stu.isdeleted  = 0 ");
		sql.append("              and pc.resourceid  = scs.plancourseid  ");
		sql.append("              and stu.teachplanid= tp.resourceid");
		sql.append("        	  and pc.planid 	 = tp.resourceid "); 
		sql.append("        	  and stu.resourceid = :studentId "); 
		sql.append("    )"); 
		sql.append(" order by srs.passtime desc");
		*/
		//第一版在导入统考成绩时根据设置的代替课程生成对应课程成绩，显示时只需要查出成绩即可
		sql.append(" select pc.resourceid plancourseid, srs.scoretype, srs.passtime, srs.COURSEID ");
		sql.append("   from edu_teach_statexamresults srs,edu_teach_plancourse pc ,edu_roll_studentinfo stu ");	
		sql.append("  where srs.courseid = pc.courseid and pc.planid = stu.teachplanid and stu.resourceid = srs.studentid  ");
		sql.append("    and srs.isdeleted = :isDeleted and pc.isdeleted = :isDeleted ");
		sql.append("    and srs.isidented = :isIdented ");	
		sql.append( "   and stu.resourceid =:studentId ");	    
		sql.append(" order by srs.passtime desc");
		try {
			return baseJdbcTemplate.findForListMap(sql.toString(),map);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	/**
	 * 根据教学计划课程对应的基础课程查询对应的免修免考成绩
	 * @param studentId
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findNoExamCourseResults(String studentId) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("studentId", studentId);
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.studentid,t.courseid,t.coursescoretype,t.scoreforcount,t.checktime,t.unscore ");
		sql.append( "  from edu_teach_noexam t ");
		sql.append( " where t.isdeleted  = 0  ");
		sql.append("    and t.checkstatus= 1 ");
		sql.append("    and t.studentid=:studentId ");
		sql.append("    and exists(");
		sql.append("		 select pc.courseid from edu_teach_plancourse pc ,EDU_TEACH_PLAN tp,edu_roll_studentinfo stu");
		sql.append("        	where pc.isdeleted   = 0 "); 
		sql.append("			  and stu.isdeleted  = 0 ");
		sql.append("              and pc.courseid    = t.courseid  ");
		sql.append("              and stu.teachplanid= tp.resourceid");
		sql.append("        	  and pc.planid 	 = tp.resourceid "); 
		sql.append("        	  and stu.resourceid = :studentId "); 
		sql.append("    )");
		sql.append(" order by t.checktime desc ");
		
		try {
			return baseJdbcTemplate.findForListMap(sql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * 根据教学计划课程查询对应的统考课程成绩
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> findToGraduateStatCouseExamResults() {
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer();
			sql.append(" select srs.studentid,scs.plancourseid, srs.scoretype ,srs.passtime ");
			sql.append("   from edu_teach_statexamresults srs,EDU_TEACH_STATCOURSE scs ");	
			sql.append("  where srs.isdeleted=0 and scs.isdeleted=0 ");	
			sql.append("    and scs.courseid = srs.courseid ");	
			sql.append("    and srs.isidented='Y' ");	
			sql.append("    and exists (");       
			sql.append("           select pc.resourceid from edu_teach_plancourse pc ,EDU_TEACH_PLAN tp,edu_roll_studentinfo stu"); 
			sql.append("        	where pc.isdeleted   = 0 "); 
			sql.append("			  and stu.isdeleted  = 0 ");
			sql.append("              and pc.resourceid  = scs.plancourseid  ");
			sql.append("              and stu.teachplanid= tp.resourceid");
			sql.append("        	  and pc.planid 	 = tp.resourceid "); 
			sql.append("        	  and stu.resourceid = srs.studentid "); 
			sql.append("    )"); 
		try {
			return baseJdbcTemplate.findForListMap(sql.toString(),map);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	/**
	 * 根据教学计划课程对应的基础课程查询对应的免修免考成绩
	 * @return
	 */
	@Override
	public List<Map<String, Object>> findToGraduateNoExamCourseResults() {
		
		Map<String,Object> map = new HashMap<String,Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select t.studentid,t.courseid,t.checkstatus ");
		sql.append( "  from edu_teach_noexam t ");
		sql.append( " where t.isdeleted  = 0  ");
		sql.append("    and t.checkstatus= 1 ");

		sql.append("    and exists(");
		sql.append("		 select pc.courseid from edu_teach_plancourse pc ,EDU_TEACH_PLAN tp,edu_roll_studentinfo stu");
		sql.append("        	where pc.isdeleted   = 0 "); 
		sql.append("			  and stu.isdeleted  = 0 ");
		sql.append("              and pc.courseid    = t.courseid  ");
		sql.append("              and stu.teachplanid= tp.resourceid");
		sql.append("        	  and pc.planid 	 = tp.resourceid "); 
		sql.append("              and stu.resourceid=t.studentid");
		sql.append("    )");
		sql.append(" order by t.checktime desc ");
		
		try {
			return baseJdbcTemplate.findForListMap(sql.toString(), map);
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Map<String,Object>>();
		}
	}
	
	
	
	@Override
	@Transactional(readOnly=true)
	public Page statStudentBbsScore(Map<String, Object> condition,Page objPage) throws ServiceException {				
		//学习计划id,年度,学期,课程,学号,姓名,学习中心,主题贴,回复贴,总发帖,优秀贴,网络辅导成绩
		String sqlHeader = " select stuplan.resourceid,y.yearname,major.majorname,classic.classicname,stuplan.term,stuplan.courseName,stuinfo.studyno,stuinfo.studentname,unit.unitname,nvl(rts.topic,0) topic,nvl(rts.reply,0) reply,nvl(rts.isbest,0) isbest,stuplan.bbsresults,stuplan.studentid,nvl(stuplan.status,'0') status ";
		StringBuffer sqlFrom = getBbsResultsStatFromSql(condition);		
		
		String orderBy = " order by stuplan.courseName,unit.unitname,major.majorname,classic.classicname,stuinfo.studyno  ";
		
		try {	
			if(objPage.isAutoCount()){
				Long count = this.baseJdbcTemplate.findForLong("select count(*) "+sqlFrom.toString(), condition);
				objPage.setTotalCount(count.intValue());
			}
			List<Map<String,Object>> resutList = baseJdbcTemplate.findForListMap(" select * from (select r.*,rownum rn from ("+sqlHeader+sqlFrom.toString()+orderBy+") r where rownum<="+(objPage.getFirst()+objPage.getPageSize())+") where rn>"+objPage.getFirst(), condition);
			for (Map<String, Object> r : resutList) {
				String bbsresults = r.get("BBSRESULTS")!=null?r.get("BBSRESULTS").toString():"";
				String studentid = r.get("STUDENTID")!=null?r.get("STUDENTID").toString():"";
				if(ExStringUtils.isNotBlank(bbsresults) && ExStringUtils.isNotBlank(studentid)){
					r.put("BBSRESULTS",ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,bbsresults));
				} else {
					r.put("BBSRESULTS","0");
				}
			}
			objPage.setResult(resutList);			
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return objPage;
	}
	//网络辅导查询语句from部分
	private StringBuffer getBbsResultsStatFromSql(Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" from ( ");
		sql.append("   select p.resourceid,p.studentid,p.yearid,p.term,c.resourceid courseid,c.coursename,ur.bbsresults,ur.status ");
		sql.append("   from edu_learn_stuplan p join edu_teach_plancourse pc on pc.resourceid=p.plansourceid ");
		sql.append("   join edu_base_course c on (c.resourceid=pc.courseid or c.resourceid=p.planoutcourseid) ");
		sql.append("   left join edu_teach_usualresults ur on ur.resourceid=p.usualresultsid ");
		sql.append("   where p.isdeleted=0  and c.resourceid=:courseId and p.yearid=:yearInfoId and p.term=:term ");
		sql.append("   ) stuplan ");
		sql.append(" left join edu_roll_studentinfo stuinfo on stuplan.studentid=stuinfo.resourceid ");
		sql.append(" left join edu_bbs_userinfo uinfo on stuinfo.sysuserid=uinfo.userid  ");
		sql.append(" left join   ");
		sql.append("       (select rs.sid,rs.courseid,sum(rs.topic) topic,sum(rs.reply) reply,sum(rs.isbest) isbest  ");
		sql.append("       from (  ");
		sql.append("           select u.sid,u.courseid, u.stype,decode(u.stype,'topic',count(*),0) topic,decode(u.stype,'reply',count(*),0) reply,decode(u.isbest,'Y',count(*),0) isbest  ");
		sql.append("            from (  ");
		sql.append("           select t.resourceid,t.fillinmanid sid,t.courseid,t.isbest,'topic' stype from edu_bbs_topic t where t.isdeleted=0 and t.parentid is null and t.courseid is not null  ");
		sql.append("           union all  ");
		sql.append("           select r.resourceid,r.replymanid sid,tp.courseid,r.isbest, 'reply' stype from edu_bbs_reply r left join edu_bbs_topic tp on r.topicid=tp.resourceid where r.isdeleted=0 and tp.courseid is not null  ");
		sql.append("           ) u  group by u.sid,u.courseid, u.stype,u.isbest) rs group by rs.sid,rs.courseid) rts  ");
		sql.append(" on uinfo.resourceid=rts.sid and rts.courseid=stuplan.courseid ");
		sql.append(" join edu_base_year y on y.resourceid=stuplan.yearid join hnjk_sys_unit unit on stuinfo.branchschoolid=unit.resourceid  ");
		sql.append(" join edu_base_major major on stuinfo.majorid=major.resourceid join edu_base_classic classic on stuinfo.classicid=classic.resourceid ");
		sql.append(" where 1=1 ");
		if(condition.containsKey("unitId")) {
			sql.append(" and stuinfo.branchschoolid=:unitId ");
		}
		if(condition.containsKey("studyNo")) {
			sql.append(" and stuinfo.studyno like '%"+condition.get("studyNo").toString()+"%' ");
		}
		if(condition.containsKey("studentName")) {
			sql.append(" and stuinfo.studentName like '%"+condition.get("studentName").toString().toLowerCase()+"%' ");
		}
		if(condition.containsKey("studentStatus")){
			sql.append(" and stuinfo.studentStatus in("+condition.get("studentStatus")+") ");
		}
		return sql;
	}
	
	@Override
	public List<Map<String, Object>> statStudentBbsScore(Map<String, Object> condition) throws ServiceException {
		String sqlHeader = " select stuplan.resourceid,y.yearname,major.majorname,classic.classicname,stuplan.term,stuplan.courseName,stuinfo.studyno,stuinfo.studentname,unit.unitname,nvl(rts.topic,0) topic,nvl(rts.reply,0) reply,nvl(rts.topic,0)+nvl(rts.reply,0) allTopics,nvl(rts.isbest,0) isbest,stuplan.bbsresults,stuplan.studentid,nvl(stuplan.status,'0') status ";
		StringBuffer sqlFrom = getBbsResultsStatFromSql(condition);		
		sqlFrom.append(" order by stuplan.courseName,unit.unitname,major.majorname,classic.classicname,stuinfo.studyno  ");
		List<Map<String,Object>> resutList = new ArrayList<Map<String,Object>>();
		try {	
			resutList = baseJdbcTemplate.findForListMap(sqlHeader+sqlFrom.toString(), condition);
			for (Map<String, Object> r : resutList) {
				String bbsresults = r.get("BBSRESULTS")!=null?r.get("BBSRESULTS").toString():"";
				String studentid = r.get("STUDENTID")!=null?r.get("STUDENTID").toString():"";
				if(ExStringUtils.isNotBlank(bbsresults) && ExStringUtils.isNotBlank(studentid)){
					r.put("BBSRESULTS",ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,bbsresults));
				} else {
					r.put("BBSRESULTS","0");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return resutList;
	}
	
	/*
	 * 查看随堂练习分布以及完成情况
	 * (non-Javadoc)
	 * @see com.hnjk.edu.learning.service.ILearningJDBCService#distributeStudentActiveCourseExam(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<Map<String, Object>> distributeStudentActiveCourseExamFinished(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		String q = "";
		if(condition.containsKey("courseId")) {
			q += " and course.resourceid=:courseId ";
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("studentId")){
			q += " and stuinfo.resourceid=:studentId ";
			values.put("studentId", condition.get("studentId"));
		}
		
		values.put("isdeleted", 0);
		StringBuffer sql = new StringBuffer();	
		sql.append("select st.*,c.coursename from (select t.*, ");
		sql.append("(select count(s.resourceid) from edu_lear_courseexam s join edu_lear_exams es on es.resourceid=s.examid where s.isdeleted=0 and es.examtype!='6' and s.ispublished='Y' and s.syllabustreeid=t.resourceid) counts, ");
		sql.append("(select count(stuexam.resourceid) correctcount from edu_lear_studentcourseexam stuexam,edu_lear_courseexam exam where exam.ispublished='Y' and stuexam.courseExamId = exam.resourceid and exam.syllabustreeid = t.resourceid and stuexam.studentid=:studentId and stuexam.iscorrect = 'Y' and stuexam.isdeleted = 0 and stuexam.result is not null ) correctcount, ");
		sql.append("(select count(stuexam.resourceid) mistakecount from edu_lear_studentcourseexam stuexam,edu_lear_courseexam exam where exam.ispublished='Y' and stuexam.courseExamId = exam.resourceid and exam.syllabustreeid = t.resourceid and stuexam.studentid=:studentId and stuexam.iscorrect = 'N' and stuexam.isdeleted = 0 and stuexam.result is not null) mistakecount ");
		sql.append("from edu_teach_syllabustree t where t.isdeleted=0 and t.courseId=:courseId ");
		sql.append("start with t.parentid is null connect by prior t.resourceid=t.parentid  order siblings by t.nodeLevel,t.showOrder ) st ");
		sql.append("join edu_base_course c on c.resourceid=st.courseid ");
		
		try {	
			List<Map<String,Object>> resutList = baseJdbcTemplate.findForListMap(sql.toString(), condition);
			return resutList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	

	
	/**
	 * 计算随堂练习分数
	 * @throws Exception 
	 */
	@Override
	public List<Map<String,Object>> toCalculate(String studyno, String courseid) throws Exception{
		StringBuffer sql = new StringBuffer(" select ce.resourceid courseid,nvl(sum(sm.result),0) result from edu_lear_studentcourseexam sm ");
		sql.append(" join edu_roll_studentinfo info on info.resourceid = sm.studentid ");
		sql.append(" join edu_lear_courseexam cm on cm.resourceid = sm.courseexamid ");
		sql.append(" join EDU_LEAR_EXAMS es on es.resourceid = cm.examid ");
		sql.append(" join edu_base_course ce on ce.resourceid = es.courseid ");
		sql.append(" where info.resourceid = :studyno and ce.resourceid = :courseid and cm.Ispublished = 'Y' ");
		sql.append(" group by ce.resourceid ");
		Map<String,Object> param  = new HashMap<String, Object>();
		param.put("studyno", studyno); //学号
		param.put("courseid", courseid); //课程
		return baseJdbcTemplate.findForListMap(sql.toString(), param);   //返回  courseid 课程id / result 随堂练习得分
	}
	
	/**
	 * 获取某个学生某门课程的总帖子数和有效帖子数
	 * @param bbsUserInfoId
	 * @param courseId
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getStuCourseTopicNum(String bbsUserInfoId, String courseId) throws Exception {
		StringBuffer sql = new StringBuffer("");
		sql.append("select count(t.resourceid) totalTopicNum,sum(t.scoretype) validTopicNum from edu_bbs_topic t,edu_bbs_section s,edu_bbs_userinfo bu ");
		sql.append("where t.isdeleted=0 and s.isdeleted=0 and bu.isdeleted=0 and t.fillinmanid=bu.resourceid "); 
		sql.append("and t.sectionid=s.resourceid and s.sectioncode='online_interlocution' and bu.userid=:fillinmanId and t.courseid=:courseId ");
		Map<String,Object> param  = new HashMap<String, Object>();
		param.put("fillinmanId", bbsUserInfoId); //学号
		param.put("courseId", courseId); //课程
		return baseJdbcTemplate.findForMap(sql.toString(), param);
	}
	
	/**
	 * 获取学生随堂练习得分累积情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String, Object>> findStuExamScoreInfo( Map<String, Object> condition) throws ServiceException {
		Map<String, Object> params = new HashMap<String, Object>();
		List<Map<String, Object>> stuExamScoreInfoList = null;
		try {	
			StringBuffer sql = getScoreStudentActiveCourseExamSql(condition, params, 1);			
			stuExamScoreInfoList = getBaseJdbcTemplate().findForListMap(sql.toString(),  params);
		} catch (Exception e) {
			logger.error("获取学生随堂练习得分累积情况出错", e);
			stuExamScoreInfoList = new ArrayList<Map<String,Object>>();
		}
		
		return stuExamScoreInfoList;
	}
	
	/**
	 * 网上学习情况统计 - 分页
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Page findLearningInfoStatistics(Map<String, Object> condition, Page objPage) throws Exception {
		List<Object>  params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(6000);
		findLearningInfoStatisticsSql(condition, params, sql);
		sql.append(" order by c.coursecode,y.firstyear,sp.term,so.studyno "); 
		return baseJdbcTemplate.findListMap(objPage, sql.toString(), params.toArray());
	}
	
	/**
	 * 获取网上学习情况统计的SQL
	 * @param condition
	 * @param params
	 * @param sql
	 */
	private void findLearningInfoStatisticsSql(Map<String, Object> condition, List<Object> params, StringBuilder sql) {
		// 学习情况统计
		sql.append(" select sp.resourceid,c.coursename,y.yearname,sp.term,so.studyno,so.studentname,u.unitname,g.gradename,m.majorname,cl.classesname,nvl(sn.submitNum,0)||'/'||nvl(cetotal.cetotalNum,0) submitTotalNum, ");                                                               
		sql.append(" decode(nvl(cetotal.cetotalNum,0),0,0,round((nvl(scescore.score,0)/nvl(cetotal.cetotalNum,0))*100,2)) score,nvl(effectiveTopic.effectiveNum,0)||'/'||nvl(topicTotal.topicTotalNum,0) effectiveTotalNum,");
		//随堂问答
		sql.append(" decode(c.topicnum,0,100,nvl(effectiveTopic.effectiveNum,0)*100/nvl(c.topicnum,nvl(topicTotal.topicTotalNum,1))) effectiveProgress,");
		sql.append(" nvl(submit.submitNum,0)||'/'||nvl(checked.checkedNum,0) submitCheckedNum,decode(nvl(mtotal.totaltime,0),0,0,round((nvl(progress.learnedtime,0)/nvl(mtotal.totaltime,0))*100,2)) stuprogress ");
		sql.append(" from edu_learn_stuplan sp  ");
		sql.append(" join edu_teach_plancourse pc on pc.resourceid=sp.plansourceid and pc.isdeleted=0  ");
		sql.append(" join edu_roll_studentinfo so on so.isdeleted=0 and so.resourceid=sp.studentid  ");
		sql.append(" join edu_base_course c on c.isdeleted=0 and c.resourceid=pc.courseid and c.hasresource='Y' ");
		sql.append(" join edu_base_year y on y.isdeleted=0 and y.resourceid=sp.yearid "); 
		sql.append(" join edu_roll_classes cl on cl.isdeleted=0 and so.classesid=cl.resourceid "); 
		sql.append(" join hnjk_sys_unit u on u.isdeleted=0 and u.resourceid=so.branchschoolid "); 
		sql.append(" join edu_base_major m on m.isdeleted=0 and m.resourceid=so.majorid "); 
		sql.append(" join edu_base_grade g on g.isdeleted=0 and g.resourceid=so.gradeid "); 
		//开课记录
		sql.append(" join edu_teach_guiplan gp on gp.planid=so.teachplanid and gp.gradeid=so.gradeid and gp.isdeleted=0 ");
		sql.append(" join edu_teach_coursestatus cs on cs.guiplanid=gp.resourceid and cs.plancourseid=pc.resourceid and cs.schoolids=so.BRANCHSCHOOLID and cs.isdeleted=0 and cs.teachtype='networkTeach' ");
//		sql.append(" join edu_teach_timetable tt on tt.plancourseid=sp.plansourceid and tt.isdeleted=0 and y.firstyear||'_0'||sp.term=tt.term and tt.classesid=so.classesid ");
		
		if(condition.containsKey("teacherId") || condition.containsKey("classesIds")){
			sql.append(" join (select ttab.plancourseid,ttab.classesid,ttab.courseid,ttab.term from edu_teach_timetable ttab where ttab.isdeleted = 0 ");
			if(condition.containsKey("teacherId")){
				sql.append(" and ttab.teacherid=? ");
				params.add(condition.get("teacherId"));
			}
			sql.append(" group by ttab.plancourseid,ttab.classesid,ttab.courseid,ttab.term) tt on tt.plancourseid = sp.plansourceid and y.firstyear || '_0' || sp.term = tt.term and tt.classesid = so.classesid ");
		}
		if(condition.containsKey("classesIds")){
			sql.append(" and so.classesid in ('"+condition.get("classesIds")+"') ");
		}
		// 随堂练习
		sql.append(" left join "); 
		sql.append(" (select count(t.resourceid) cetotalNum,s.courseid from edu_lear_courseexam t ")
		.append("inner join edu_teach_syllabustree st on st.isdeleted=0 and st.resourceid=t.syllabustreeid ")
		.append("join edu_lear_exams s on t.examid=s.resourceid where t.isdeleted=0 and t.ispublished='Y' and s.examtype<=5 and st.courseid=s.courseid group by s.courseid) cetotal "); 
		sql.append(" on cetotal.courseid=pc.courseid ");
		sql.append(" left join "); 
		sql.append(" (select sce.studentid,syt.courseid,nvl(count(sce.resourceid),0) submitNum from edu_lear_studentcourseexam sce "); 
		sql.append(" inner join edu_lear_courseexam ce on ce.isdeleted=0 and sce.courseexamid=ce.resourceid "); 
		sql.append(" inner join edu_teach_syllabustree syt on syt.isdeleted=0 and syt.resourceid=ce.syllabustreeid "); 
		sql.append(" where sce.isdeleted=0 and sce.result is not null group by sce.studentid,syt.courseid) sn on sn.courseid=pc.courseid and sn.studentid=sp.studentid "); 
		sql.append(" left join "); 
		sql.append(" (select sce.studentid,st.courseid,nvl(count(sce.resourceid),0) score from edu_lear_studentcourseexam sce "); 
		sql.append(" inner join edu_lear_courseexam ce on ce.resourceid=sce.courseexamid "); 
		sql.append(" inner join edu_teach_syllabustree st on st.resourceid=ce.syllabustreeid "); 
		sql.append(" where sce.isdeleted=0 and sce.iscorrect='Y' and ce.ispublished='Y' group by sce.studentid,st.courseid ) scescore on scescore.studentid=sp.studentid and scescore.courseid=pc.courseid ");
		// 随堂问答
		sql.append(" left join "); 
		sql.append(" (select ui.userid,t.courseid,count(t.resourceid) effectiveNum from edu_bbs_topic t ");
		sql.append(" inner join edu_bbs_userinfo ui on ui.isdeleted=0 and ui.resourceid=t.fillinmanid "); 
		sql.append(" where t.isdeleted=0 and t.scoretype='1' group by ui.userid,t.courseid) effectiveTopic on effectiveTopic.userid=so.sysuserid and effectiveTopic.courseid=pc.courseid "); 
		sql.append(" left join "); 
		sql.append(" (select ui.userid,t.courseid,count(t.resourceid) topicTotalNum from edu_bbs_topic t ");
		sql.append(" inner join edu_bbs_userinfo ui on ui.isdeleted=0 and ui.resourceid=t.fillinmanid "); 
		sql.append(" where t.isdeleted=0 group by ui.userid,t.courseid) topicTotal on topicTotal.userid=so.sysuserid and topicTotal.courseid=pc.courseid "); 
		// 课后作业 
		sql.append(" left join "); 
		sql.append(" (select se.studentid,eb.courseid,eb.yearid,eb.term,count(se.resourceid) checkedNum from edu_lear_studentexercise  se ");
		sql.append(" inner join edu_lear_exercisebatch eb on eb.isdeleted=0 and se.exercisebatchid=eb.resourceid "); 
		sql.append(" where se.exerciseid is null and se.isdeleted=0 and se.status=2 group by se.studentid,eb.courseid,eb.yearid,eb.term ) checked "); 
		sql.append(" on checked.studentid=sp.studentid and checked.courseid=pc.courseid and checked.yearid=sp.yearid and checked.term=sp.term "); 
		sql.append(" left join "); 
		sql.append(" (select se.studentid,eb.courseid,eb.yearid,eb.term,count(se.resourceid) submitNum from edu_lear_studentexercise  se ");
		sql.append(" inner join edu_lear_exercisebatch eb on eb.isdeleted=0 and se.exercisebatchid=eb.resourceid "); 
		sql.append(" where se.exerciseid is null and se.isdeleted=0 and se.status > 0 group by se.studentid,eb.courseid,eb.yearid,eb.term ) submit ");  
		sql.append(" on submit.studentid=sp.studentid and submit.courseid=pc.courseid and submit.yearid=sp.yearid and submit.term=sp.term "); 
		// 学习进度
		sql.append(" left join "); 
//		sql.append(" (select spg.studentinfoid,spg.courseid,sum(spg.learnedtime) learnedtime,sum(spg.totaltime) totaltime  from edu_lear_studyprogress spg "); 
		sql.append(" (select spg.studentinfoid,spg.courseid,sum(spg.learnedtime) learnedtime  from edu_lear_studyprogress spg "); 
		sql.append(" where spg.isdeleted=0 group by spg.studentinfoid,spg.courseid ) progress on progress.studentinfoid=sp.studentid and progress.courseid=pc.courseid "); 
		// 整门课程视频的总时长
		sql.append(" left join "); 
		sql.append(" (select mst.courseid,sum(m.totaltime) totaltime from edu_lear_mate m,edu_teach_syllabustree mst where m.isdeleted=0 and mst.isdeleted=0 and mst.resourceid=m.syllabustreeid and m.matetype=2 group by mst.courseid) mtotal on  mtotal.courseid=pc.courseid "); 
		sql.append(" where sp.isdeleted=0 "); 
		
		
		if(condition.containsKey("resourceids")){
			sql.append(" and sp.resourceid in ('"+condition.get("resourceids")+"') ");
		}
		if(condition.containsKey("branchSchool")){
			sql.append(" and u.resourceid=? ");
			params.add(condition.get("branchSchool"));
		}else if(condition.containsKey("schoolId")){
			sql.append(" and u.resourceid=? ");
			params.add(condition.get("schoolId"));
		}
		if(condition.containsKey("gradeId")){
			sql.append(" and g.resourceid=? ");
			params.add(condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			sql.append(" and so.classicid=? ");
			params.add(condition.get("classicId"));
		}
		if(condition.containsKey("schoolType")){
			sql.append(" and so.teachingtype=? ");
			params.add(condition.get("schoolType"));
		}
		if(condition.containsKey("studentStatus")){
			sql.append(" and so.studentStatus in("+condition.get("studentStatus")+") ");
		}
		if(condition.containsKey("majorId")){
			sql.append(" and m.resourceid=? ");
			params.add(condition.get("majorId"));
		}
		if(condition.containsKey("classesId")){
			sql.append(" and cl.resourceid=? ");
			params.add(condition.get("classesId"));
		}
		if(condition.containsKey("courseId")){
			sql.append(" and c.resourceid=? ");
			params.add(condition.get("courseId"));
		}
		if(condition.containsKey("yearInfoId")){
			sql.append(" and y.resourceid=? ");
			params.add(condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){
			sql.append(" and sp.term=? ");
			params.add(condition.get("term"));
		}
		if(condition.containsKey("studentId")){
			sql.append(" and so.resourceid=? ");
			params.add(condition.get("studentId"));
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and so.studyno=? ");
			params.add(condition.get("studyNo"));
		}
		if(condition.containsKey("studentName")){
			sql.append(" and so.studentname like ?");
			params.add("%"+condition.get("studentName")+"%");
		}
		
	}
	
	/**
	 * 网上学习情况统计 - 列表
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> findLearningInfoStatistics(Map<String, Object> condition) throws Exception {
		List<Object>  params = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder(6000);
		findLearningInfoStatisticsSql(condition, params, sql);
		sql.append(" order by c.coursecode,y.firstyear,sp.term,so.studyno "); 
		return baseJdbcTemplate.findForList(sql.toString(), params.toArray());
	}
	
	
}
