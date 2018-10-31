package com.hnjk.edu.roll.service.jdbcImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.platform.system.cache.CacheAppManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.service.IRollJDBCService;
/**
 * 学籍模块JDBC查询服务接口实现
 * <code>RollJDBCServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-4-12 下午03:17:21
 * @see 
 * @version 1.0
 */
@Transactional
@Service("rollJDBCService")
public class RollJDBCServiceImpl extends BaseSupportJdbcDao implements IRollJDBCService {

	@Override
	public List<Map<String, Object>> statStudentFactFee(Integer year, String byType, String brSchool) throws ServiceException {
		Map<String, Object> param = new HashMap<String, Object>();		
		param.put("year", year);
		if(ExStringUtils.isNotBlank(brSchool)){
			param.put("brSchool", brSchool);
		}
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql =  getStudentFactFeeSql(year,byType,brSchool);
			list = getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private StringBuffer getStudentFactFeeSql(Integer year, String byType, String brSchool) {
		StringBuffer sql = new StringBuffer();
		//缴费年度，统计类型编码，统计类型名称，应缴费用人数，应缴费用金额，完费人数，完费金额，部分缴费人数，部分缴费金额，欠费人数，欠费金额
		if("teachingType".equals(byType)){ //按办学模式统计
			sql.append(" select rs.chargeyear,f_dictionary('CodeTeachingType',rs.statCode) statCode,f_dictionary('CodeTeachingType',rs.statType) statType, ");
		} else if("major".equals(byType)){//按专业统计
			sql.append(" select rs.chargeyear,rs.statCode,rs.statType, ");
		} else if("classic".equals(byType)){//按层次统计
			sql.append(" select rs.chargeyear,rs.statCode,rs.statType, ");				
		} else if("brSchool".equals(byType)){//按学习中心统计
			sql.append(" select rs.chargeyear,rs.statCode,rs.statCode||'-'||rs.statType statType, ");			
		} else {//分级报表，按学习中心、办学模式统计
			sql.append(" select rs.chargeyear,rs.unitcode,rs.unitcode||'-'||rs.unitname unitname,f_dictionary('CodeTeachingType',rs.statCode) statCode,f_dictionary('CodeTeachingType',rs.statType) statType,  ");
		}
		sql.append(" nvl(sum(rs.allfeeCount),0) allfeeCount, ");
		sql.append(" trunc(nvl(sum(rs.recpayfee),0)) recpayfee,trunc(nvl(sum(rs.factpayfee),0)) factpayfee,trunc(nvl(sum(rs.deratefee),0)) deratefee, ");
		sql.append(" nvl(sum(rs.fullfeeCount),0) fullfeeCount,trunc(nvl(sum(fullfeeSum),0)) fullfeeSum, ");
		sql.append(" nvl(sum(rs.deratefeeCount),0) deratefeeCount,trunc(nvl(sum(rs.deratefeeSum),0)) deratefeeSum, ");
		sql.append(" nvl(sum(rs.owefeeCount),0) owefeeCount,trunc(nvl(sum(owefeeSum),0)) owefeeSum, ");
		sql.append(" nvl(sum(rs.partfeeCount),0) partfeeCount,trunc(nvl(sum(partfeeSum),0)) partfeeSum ");
		sql.append(" from ( ");
		sql.append("     select r.chargeyear,r.statCode,r.statType, ");
		if("subType".equals(byType)){ 
			sql.append(" r.unitcode,r.unitname, ");
		}
		sql.append("      sum(r.recpayfee) recpayfee,sum(r.factpayfee) factpayfee,sum(r.deratefee) deratefee,count(*) allfeeCount, ");
		sql.append("      decode(r.feestatus,0,count(*)) fullfeeCount,decode(r.feestatus,0,sum(r.factpayfee)) fullfeeSum, ");
		sql.append("      decode(r.deratefeeStatus,-1,count(*)) deratefeeCount,decode(r.deratefeeStatus,-1,sum(r.deratefee)) deratefeeSum, ");
		sql.append("      decode(r.feestatus,-1,count(*)) owefeeCount,decode(r.feestatus,-1,sum(r.recpayfee-r.factpayfee-r.deratefee)) owefeeSum, ");
		sql.append("      decode(r.partfeeStatus,-1,count(*)) partfeeCount,decode(r.partfeeStatus,-1,sum(r.factpayfee)) partfeeSum ");
		sql.append("     from ( select t.chargeyear, ");
		if("teachingType".equals(byType)){ 
			sql.append("      s.teachingtype as statType,case when s.teachingtype = 'net' or s.teachingtype = 'face' or s.teachingtype = 'direct' then 'net' else s.teachingtype end statCode,  ");
		} else if("major".equals(byType)){
			sql.append("      major.majorName statType,major.majorName statCode, ");
		} else if("classic".equals(byType)){
			sql.append("      classic.classicName statType,classic.classicCode statCode, ");
		} else if("brSchool".equals(byType)){
			sql.append("      unit.unitName statType,unit.unitCode statCode, ");
		} else {
			sql.append("      unit.unitcode,unit.unitname,s.teachingtype as statType,case when s.teachingtype = 'net' or s.teachingtype = 'face' or s.teachingtype = 'direct' then 'net' else s.teachingtype end statCode,  ");
		}		
		sql.append("           t.studentinfoid,t.recpayfee,t.factpayfee,t.deratefee, ");
		sql.append("           case when t.factpayfee + t.deratefee >= t.recpayfee then 0  ");
		sql.append("                when t.factpayfee + t.deratefee < t.recpayfee then -1  ");	
		sql.append("                end feeStatus, ");
		sql.append("           case when t.deratefee > 0 then -1 else 1 end deratefeeStatus, ");
		sql.append("          case when t.factpayfee+t.deratefee > 0 and t.factpayfee + t.deratefee < t.recpayfee then -1 else 1 end partfeeStatus ");
		sql.append("           from edu_roll_fistudentfee t ");
		sql.append("           join edu_roll_studentinfo s on s.resourceid=t.studentinfoid ");
		if("major".equals(byType)){
			sql.append("       join edu_base_major major on major.resourceid=s.majorid ");
		} else if("classic".equals(byType)){
			sql.append("       join edu_base_classic classic on classic.resourceid=s.classicid ");
		} else if("brSchool".equals(byType)){
			sql.append("       join hnjk_sys_unit unit on unit.resourceid=s.branchschoolid ");
		} else {
			sql.append("       join hnjk_sys_unit unit on unit.resourceid=s.branchschoolid ");
		}
		sql.append("           where t.isdeleted=0 ");
		if(ExStringUtils.isNotBlank(brSchool)){//过滤校外学习中心数据
			sql.append("       and  s.branchschoolid=:brSchool ");
		}
		if(year != 0){
			sql.append("           and t.chargeyear=:year ");
		}
		sql.append("         ) r ");
		sql.append("     group by r.chargeyear,"+("subType".equals(byType)?"r.unitcode,r.unitname,":"")+"r.statCode,r.statType,r.feestatus,r.deratefeeStatus,partfeeStatus  ");
		sql.append("     ) rs  ");
		sql.append(" group by rs.chargeyear,"+("subType".equals(byType)?"rs.unitcode,rs.unitname,":"")+"rs.statCode,rs.statType ");
		if("teachingType".equals(byType)){ 
			sql.append(" order by rs.chargeyear desc,decode(rs.statCode,'net', 1, 'adult', 2, 'specialbatch', 3),decode(rs.statType,'net', 1, 'face', 2, 'direct', 3) ");
		} else 	if("classic".equals(byType)){ 
			sql.append(" order by rs.chargeyear desc,decode(rs.statCode,'3', 1, '2', 2, '1', 3, '8',4),rs.statType ");			
		} else if("major".equals(byType)){
			sql.append(" order by rs.chargeyear desc,rs.statType ");
		} else if("brSchool".equals(byType)){
			sql.append(" order by rs.chargeyear desc,rs.statCode,rs.statType ");
		} else {
			sql.append(" order by rs.chargeyear desc,rs.unitcode,rs.unitname,decode(rs.statCode,'net', 1, 'adult', 2, 'specialbatch', 3),decode(rs.statType,'net', 1, 'face', 2, 'direct', 3) ");
		}
		return sql;
	}
	/**
	 * 	学习中心替学生申请免修免考缓考,查找学生的教学计划课程
	 */
	@Override
	public List<Map<String, Object>> findStudentPlanCourseForNoExamApp(String resourceid) throws Exception {
		
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql 		 = new StringBuffer();
		param.put("isDeleted",0);
		param.put("isiDented","Y");
		param.put("studentId",resourceid);

		//String isContainsNetworkForNoExam = CacheAppManager.getSysConfigurationByCode("isContainsNetworkForNoExam").getParamValue();
		String isShowNetworkForNoExamApply = ConfigPropertyUtil.getInstance().getProperty("isShowNetworkForNoExamApply");
		if (!"Y".equals(isShowNetworkForNoExamApply)) {
			param.put("coursetype1","thesis");
			param.put("coursetype2","66");
		}

		sql.append(" select pc.resourceid plancourseid,c.resourceid courseid ,c.coursename,pc.coursetype,pc.teachtype,pc.examClassType,c.examtype,c.isuniteexam,pc.credithour,pc.term,t.checkstatus,t.resourceid noexamid,t.memo,t.unscore ,t.score");
		sql.append("   from edu_teach_plancourse pc ");
		sql.append("  inner join edu_base_course c on pc.courseid = c.resourceid ");
		sql.append("   left join (   select n.resourceid,n.courseid,n.checkstatus,n.memo,n.scoreforcount score,n.unscore  from edu_teach_noexam n where n.isdeleted = :isDeleted  and n.studentid =:studentId  ");
	/*	if (ExStringUtils.isNotBlank(flag)) {
			sql.append(" and n.unscore=:flag");
			param.put("flag", flag);
		}*/
		
		sql.append(")  t on t.courseid = c.resourceid");
		sql.append("  where pc.isdeleted =:isDeleted ");
		sql.append("    and pc.planid = ( select stu.teachplanid from edu_roll_studentinfo stu where stu.resourceid = :studentId )");
		if (param.containsKey("coursetype1")) {
			sql.append("    and pc.coursetype <> :coursetype1");
		}
		if (param.containsKey("coursetype2")) {
			sql.append("    and pc.coursetype <> :coursetype2 ");
		}
		sql.append("    and not exists(  select sr.resourceid from edu_teach_statexamresults sr where sr.isdeleted =:isDeleted and sr.studentid =:studentId and sr.isidented=:isiDented and sr.courseid = c.resourceid )");
		sql.append(" order by pc.term,c.courseName ");

		return getBaseJdbcTemplate().findForListMap(sql.toString(), param);
	}
	
	/**
	 * 	申请缓考,查找学生的教学计划课程
	 */
	@Override
	public List<Map<String, Object>> findPlanCourseForAbnormalExam(String resourceid) throws Exception {
		
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql 		 = new StringBuffer();
		param.put("isDeleted",0);
		param.put("studentId",resourceid);

		param.put("coursetype1","thesis");
		param.put("coursetype2","66");

		sql.append(" select pc.resourceid plancourseid,c.resourceid courseid ,c.coursename,pc.coursetype,pc.teachtype,c.examtype,");
		sql.append(" c.isuniteexam,pc.credithour,pc.term,t.checkstatus,t.resourceid abnormalexamid,t.reason,t.abnormaltype,t.scoreforcount score,");
		sql.append(" t.examtype aeexamtype,sr.isidented,n.checkstatus noexamstatus, n.unscore ");
		sql.append("  from edu_teach_plancourse pc ");
		sql.append("  inner join edu_base_course c on pc.courseid = c.resourceid ");
		sql.append("  left join edu_teach_abnormalexam t on t.isdeleted = :isDeleted  and t.studentid =:studentId  ");
		sql.append("  and t.courseid = pc.courseid and t.plancourseid=pc.resourceid");
		
		sql.append("  left join edu_teach_statexamresults sr on sr.isdeleted =:isDeleted and sr.studentid =:studentId and sr.courseid = pc.courseid");
		sql.append("  left join edu_teach_noexam n on n.isdeleted = :isDeleted  and n.studentid =:studentId and n.courseid = pc.courseid");
		
		sql.append("  where pc.isdeleted =:isDeleted ");
		sql.append("  and pc.planid = ( select stu.teachplanid from edu_roll_studentinfo stu where stu.resourceid = :studentId )");
		sql.append("  and pc.coursetype <> :coursetype1");
		sql.append("  and pc.coursetype <> :coursetype2 ");
		sql.append(" order by pc.term ");

		return getBaseJdbcTemplate().findForListMap(sql.toString(), param);
	}
	
	/**
	 * 根据用户ID获取当前学籍的班级
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> findClassesByUserId(String userId) throws Exception{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		StringBuffer sql = new StringBuffer("");
		sql.append("select so.classesid classesId from edu_roll_studentinfo so,hnjk_sys_usersextend ue where so.isdeleted=0 and ue.isdeleted=0 ");
		sql.append("and ue.sysuserid=so.sysuserid and so.resourceid=ue.exvalue and ue.excode='defalutrollid' and ue.sysuserid=:userId ");
		return getBaseJdbcTemplate().findForMap(sql.toString(), param);
	}

	/**
	  * 根据条件获取学籍的有关信息
	  * @param condition
	  * @return
	  * @throws ServiceException
	  */
	@Override
	public List<Map<String, Object>> findStudentInfoByCondition(Map<String, Object> condition) throws Exception {
//		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer("");
		sql.append("select so.resourceid stuInfoId,so.classesid classesId,g.gradename,y.firstyear,m.majorname,decode(so.teachingtype,'2','业余','4','函数','') teachingtype,p.eduyear,cl.classesname,so.studyno,so.studentname,decode(s.gender,'1','男','2','女','') gender, ");
		sql.append("to_char(s.bornday,'yyyy/MM/dd') bornday,s.contactaddress,s.contactzipcode,s.mobile,to_char(g.indate,'yyyy\"年\"MM\"月\"') indate,to_char(g.graduatedate,'yyyy\"年\"MM\"月\"') graduatedate,su.suspInfo,r.retInfo,ci.changeInfo ");
		sql.append("from edu_roll_studentinfo so ");
		sql.append("inner join edu_base_student s on s.isdeleted=0 and so.studentbaseinfoid=s.resourceid ");
		sql.append("inner join edu_base_major m on m.isdeleted=0 and so.majorid=m.resourceid ");
		sql.append("inner join edu_roll_classes cl on cl.isdeleted=0 and so.classesid=cl.resourceid ");
		sql.append("inner join edu_base_classic cc on cc.isdeleted=0 and so.classicid=cc.resourceid ");
		sql.append("inner join edu_base_grade g on g.isdeleted=0 and so.gradeid=g.resourceid ");
		sql.append("inner join edu_base_year y on y.isdeleted=0 and g.yearid=y.resourceid ");
		sql.append("inner join edu_teach_plan p on p.isdeleted=0 and so.teachplanid=p.resourceid ");
		sql.append("left join (select max(suspInfo) suspInfo,studentId from (select wm_concat(to_char(susp.auditdate,'yyyy')||'年'||decode(susp.changetype,'02','留级','休学')) OVER(PARTITION BY susp.studentid ORDER BY susp.auditdate) suspInfo, ");
		sql.append("susp.studentid studentId from edu_roll_stuchangeinfo susp where susp.changetype in ('02','11') and susp.isdeleted=0 and susp.finalauditstatus='Y') group by studentId) su on su.studentId=so.resourceid ");
		sql.append("left join (select max(retInfo) retInfo,studentId from (select wm_concat(to_char(ret.auditdate,'yyyy')||'年复学') OVER(PARTITION BY ret.studentid ORDER BY ret.auditdate) retInfo,ret.studentid studentId  ");
		sql.append("from edu_roll_stuchangeinfo ret where ret.changetype='12' and ret.isdeleted=0 and ret.finalauditstatus='Y') group by studentId) r on r.studentId=so.resourceid ");
//		sql.append("left join ( select resu.studentid,sum(1) num1 from edu_base_studentresume resu where resu.isdeleted = 0 group by resu.studentid ) aa on aa.studentid = so.resourceid ");
//		sql.append("left join ( select person.studentbaseinfoid,sum(1) num2 from edu_base_personanralation person where person.isdeleted = 0 group by person.studentbaseinfoid ) bb on bb.studentbaseinfoid = so.resourceid ");
		sql.append("left join (select max(changeInfo) changeInfo,studnetId from (select wm_concat(decode(sci.changetype,'13',to_char(sci.auditdate,'yyyy')||'年退学','82',u.unitname||'转入'||cu.unitname,'23',pm.majorname||'转入'||cm.majorname, ");
		sql.append("(case when (sci.changebrschoolid!=sci.changebeforebrschoolid and sci.changemajorid!=pm.majorid)  then ");
		sql.append("u.unitname||pm.majorname||decode(sci.changebeforelearingstyle,'2','业余','函授')||'转入'||cu.unitname||cm.majorname||decode(sci.changeteachingtype,'2','业余','函授') ");
		sql.append("when sci.changebrschoolid!=sci.changebeforebrschoolid then u.unitname||'转入'||cu.unitname when sci.changemajorid!=pm.majorid then ");
		sql.append("pm.majorname||decode(sci.changebeforelearingstyle,'2','业余','函授')||'转入'||cm.majorname||decode(sci.changeteachingtype,'2','业余','函授') else '' end))) OVER(PARTITION BY sci.studentid ORDER BY sci.auditdate) changeInfo, ");
		sql.append("sci.studentid studnetId from edu_roll_stuchangeinfo sci ");
		sql.append("left join edu_base_major cm on cm.isdeleted=0 and sci.changemajorid=cm.resourceid ");
		sql.append("left join (select m.majorname,p.resourceid,p.majorid from edu_teach_plan p,edu_base_major m where p.isdeleted=0 and m.isdeleted=0 and p.majorid=m.resourceid ) pm on sci.changebeforeteachingplanid=pm.resourceid ");
		sql.append("left join hnjk_sys_unit cu on cu.isdeleted=0 and sci.changebrschoolid=cu.resourceid ");
		sql.append("left join hnjk_sys_unit u on u.isdeleted=0 and sci.changebeforebrschoolid=u.resourceid ");
		sql.append("where sci.isdeleted=0 and sci.changetype in ('82','23','81','13')) group by studnetId ) ci on ci.studnetId=so.resourceid ");
		sql.append("where so.isdeleted=0 and so.studentstatus='11' ");
		
		if(condition.containsKey("unitId")){
			sql.append("and so.branchschoolid=:unitId ");
		}
		if(condition.containsKey("classesId")){
			sql.append("and so.classesid=:classesId ");
		}
		if(condition.containsKey("gradeId")){
			sql.append("and so.gradeid=:gradeId ");
		}
		if(condition.containsKey("studyno")){
			sql.append("and so.studyno=:studyno ");
		}else if(condition.containsKey("studentNo")){
			sql.append("and so.studyno=:studentNo ");
		}
		/*if(condition.containsKey("matriculateNoticeNo")){
			sql.append("and so.studyno=:matriculateNoticeNo ");
		}*/
		if(condition.containsKey("majorId")){
			sql.append("and so.majorid=:majorId ");
		}
		if(condition.containsKey("classicId")){
			sql.append("and so.classicid=:classicId ");
		}
		if(condition.containsKey("studentstatus")){
			sql.append("and so.studentstatus=:studentstatus ");
		}
		if(condition.containsKey("certnum")){
			sql.append("and s.certnum=:certnum ");
		}
		if(condition.containsKey("name")){
			sql.append("and s.name='%"+condition.get("name")+"%' ");
		}
		if(condition.containsKey("entranceFlag")){
			sql.append("and so.enterauditstatus=:entranceFlag ");
		}
		if(condition.containsKey("studentIds")){
			sql.append("and so.resourceid in ('"+condition.get("studentIds")+"') ");
		}
		if (condition.containsKey("rollCard")) {
	 		sql.append(" and so.rollcardstatus=:rollCard ");
		}
		sql.append("order by so.gradeid,so.branchschoolid,so.classesid,so.studyno ");
		
		return getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
	}

}
