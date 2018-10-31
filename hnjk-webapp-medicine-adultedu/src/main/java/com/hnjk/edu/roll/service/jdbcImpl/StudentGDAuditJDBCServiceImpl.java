package com.hnjk.edu.roll.service.jdbcImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.edu.roll.service.IStudentGDAuditJDBCService;
import com.hnjk.edu.roll.vo.GraduationInfoExportVo;
@Service("studentGDAuditJDBCService")
@Transactional
public class StudentGDAuditJDBCServiceImpl  extends BaseSupportJdbcDao implements
		IStudentGDAuditJDBCService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 查询毕业审核结果
	 */
	@Override
	public List<Map<String, Object>> getGraduateAuditResults(Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select info.resourceid,info.studyno,info.studentname,info.finishedcredithour,info.finishednecesscredithour,info.finishedoptionalcoursenum, "); 
		sql.append(" info.isapplygraduate,info.enterauditstatus,gaudit.graduateauditstatus,gaudit.graduateauditmemo ");
		if (condition.containsKey("exportGaudit")){
			sql.append(" ,gaudit.theGraduationStatis,gaudit.theGraduationMemo ");
			sql.append(" ,case when gaudit.confirm = '1' then '确认' else '未确认' end  confirm ");
			sql.append(" ,case when (to_number(replace(substr(tplan.eduYear,0,3),'年',''))+to_number(yearinfo.firstYear+(grade.term-1)*0.5-0.5))<=(0.5+to_number('"+condition.get("currentYearInfo")+"')) then '达到' ");
			sql.append(" else '未达到' end as isaccYear ");
			sql.append(" ,case when (info.studentstatus = '11' or info.studentstatus = '21' or info.studentstatus = '16' or info.studentstatus = '24' or info.studentstatus = '25' ) then '达到' else '未达到' end as isaccStustatus ");
			sql.append(" ,tplan.theGraduationScore as theGraduationScore ");
		}
		//增加四个字段
		sql.append(" ,grade.gradename,classic.classicname,major.majorname,unit.unitname ");
		if(condition.containsKey("queryEleExamCount") && "Y".equals(condition.get("queryEleExamCount"))){
			sql.append(" ,nvl(exam.examcount,0) examcount");
		}
		sql.append(" from edu_roll_stuaudit gaudit,edu_roll_studentinfo info ");
		if(condition.containsKey("queryEleExamCount") && "Y".equals(condition.containsKey("queryEleExamCount"))){
			sql.append(" left join (select eer.studentid,count(*) examcount from  edu_teach_electiveexamresults eer where eer.isdeleted=0 group by eer.studentid)exam on exam.studentid=info.resourceid");
		}
		//增加三个新的查询条件
		sql.append(" ,edu_teach_plan tplan,edu_base_grade grade,edu_base_year yearinfo ");
		//增加
		sql.append(" ,edu_base_classic classic,edu_base_major major,hnjk_sys_unit unit  ");
		sql.append(" where gaudit.studentinfoid = info.resourceid and gaudit.isDeleted = 0 ");
		//新增的三个表的联合条件
		sql.append( " and info.teachplanid=tplan.resourceid " +  
					" and grade.resourceid = info.gradeid " +
					" and grade.yearid = yearinfo.resourceid " );
		//增加
		sql.append(" and info.classicid = classic.resourceid "+
				   " and info.majorid = major.resourceid " +
				   " and info.branchschoolid = unit.resourceid ");
		if (condition.containsKey("branchSchool")) {
			sql.append(" and info.branchschoolid=:branchSchool ");
		}
		if (condition.containsKey("classId")) {
			sql.append(" and info.CLASSESID=:classId ");
		}
		if (condition.containsKey("grade")) {
			sql.append(" and info.gradeid =:grade ");
		}
		if (condition.containsKey("major")) {
			sql.append(" and info.majorid=:major ");
		}
		if (condition.containsKey("classic")) {
			sql.append(" and info.classicid=:classic ");
		}
		if (condition.containsKey("name")) {
			sql.append(" and info.studentname=:name ");
		}
		if (condition.containsKey("studyNo")) {
			sql.append(" and info.studyno=:studyNo ");
		}
		if (condition.containsKey("auditStatus")) {
			sql.append(" and gaudit.graduateauditstatus=:auditStatus ");
		}
		if (condition.containsKey("confirmStatus")) {
			sql.append(" and gaudit.graduateauditmemo like :confirmStatus ");
		}
		if (condition.containsKey("stus")) {
			sql.append(" and info.resourceid in ('"+condition.get("stus")+"') ");
		}
		if (condition.containsKey("studentstatus")) {
			sql.append(" and info.studentstatus =:studentstatus ");
		}
		if (condition.containsKey("forConfirm")) {
			sql.append(" and (gaudit.graduateauditstatus='1' or gaudit.thegraduationstatis = '1') "); //毕业 + 结业
		}
		if (condition.containsKey("stuIds")) {
			sql.append(" and info.resourceid not in  "+condition.get("stuIds"));
		}
		if (condition.containsKey("statusNeed")) {
			sql.append(" and (info.studentstatus = '11' or info.studentstatus = '21' or info.studentstatus = '24' or info.studentstatus = '25' )  ");
		}
		//新的三个查询条件
		if(condition.containsKey("isReachGraYear")&&condition.containsKey("currentYearInfo")){
			if("1".equals(condition.get("isReachGraYear"))){
				sql.append(" and (to_number(replace(substr(tplan.eduYear,0,3),'年',''))+to_number(yearinfo.firstYear+(grade.term-1)*0.5+0.5))<=(0.5+to_number('"+condition.get("currentYearInfo")+"'))");
				
			}else if("0".equals(condition.get("isReachGraYear"))){
				sql.append(" and (to_number(replace(substr(tplan.eduYear,0,3),'年',''))+to_number(yearinfo.firstYear+(grade.term-1)*0.5+0.5))>(0.5+to_number('"+condition.get("currentYearInfo")+"'))");
				
			}
		} 
		if(condition.containsKey("isPassEnter")){
			if("1".equals(condition.get("isPassEnter"))){
				sql.append(" and enterAuditStatus ='Y' ");
			}else if ("0".equals(condition.get("isPassEnter"))){
				sql.append(" and enterAuditStatus !='Y' ");
			}
		}  
		if(condition.containsKey("isApplyDelay")){
			if("1".equals(condition.get("isApplyDelay"))){
				sql.append(" and (isApplyGraduate= 'W' ) ");
			}else if ("0".equals(condition.get("isApplyDelay"))){
				sql.append(" and (isApplyGraduate= 'N' or isApplyGraduate='Y' ) ");
			}	
		}  
		if(condition.containsKey("accoutstatus")){
			sql.append(" and accoutstatus = :accoutstatus ");
		}
		//dateDuration 时间范围标识
		if(condition.containsKey("dateDuration")){
			sql.append(" and auditTime between to_date(:graduateAuditBeginTime,'yyyy-mm-dd') and to_date(:graduateAuditEndTime,'yyyy-mm-dd') ");
		}
		sql.append(" order by gaudit.auditTime desc ");
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>(0) ;
		try {
			result=this.baseJdbcTemplate.findForListMap(sql.toString(),condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	/**
	 * 查询学位审核结果
	 */
	@Override
	public List<Map<String, Object>> getDegreeAuditResults(Map<String, Object> condition) { 
		StringBuffer sql = new StringBuffer();
		sql.append(" select info.studyno,info.studentname,et.bornDay,et.gender,et.certNum,hu.unitName,ec.classesname,classic.classicName,info.learningStyle,es.memo,graduas.degreeName,graduas.graduateType, gaudit.degreeauditmemo,gaudit.degreeauditstatus,graduas.diplomaNum,graduas.degreeName,graduas.degreeNum,info.enrolleecode,info.examcertificateno "); 
		sql.append(" from edu_roll_stuaudit gaudit left join edu_roll_studentinfo info on gaudit.studentinfoid = info.resourceid"
				+ " left join edu_base_classic classic on  info.classicid = classic.resourceid left join edu_teach_graduatedata graduas on graduas.studentid = info.resourceid"
				+ " left join EDU_BASE_STUDENT et on et.resourceid=info.STUDENTBASEINFOID left join EDU_ROLL_CLASSES ec on ec.resourceid=info.CLASSESID"
				+ " left join hnjk_sys_unit hu on hu.resourceid=info.BRANCHSCHOOLID  left join EDU_TEACH_STATEXAMRESULTS es on info.resourceid=es.STUDENTID and es.isDeleted = 0");
		sql.append("  where  gaudit.isDeleted = 0 and ec.isdeleted=0 and graduas.isdeleted=0 and classic.classiccode != '3'  ");
		
		//如果是做审核则需要未确认的作为审核条件
			sql.append(" and  gaudit.degreeAuditStatus is not null ");
			
		if (condition.containsKey("branchSchool")) {
			sql.append(" and info.branchschoolid=:branchSchool ");
		}
		if (condition.containsKey("grade")) {
			sql.append(" and info.gradeid =:grade ");
		}
		if (condition.containsKey("major")) {
			sql.append(" and info.majorid=:major ");
		}
		if (condition.containsKey("classic")) {
			sql.append(" and info.classicid=:classic ");
		}
		if (condition.containsKey("name")) {
			sql.append(" and info.studentname like :name||'%' ");
		}
		if (condition.containsKey("studyNo")) {
			sql.append(" and info.studyno=:studyNo ");
		}
		if (condition.containsKey("auditStatus")) {
			sql.append(" and gaudit.degreeAuditStatus=:auditStatus ");
		}
		if (condition.containsKey("confirmStatus")) {
			sql.append(" and gaudit.graduateauditmemo=:confirmStatus ");
		}
		if (condition.containsKey("stus")) {
			sql.append(" and info.resourceid in ('"+condition.get("stus")+"') ");
		}
		if (condition.containsKey("graduas")) {
			sql.append(" and graduas.resourceid in ('"+condition.get("graduas")+"') ");
		}
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>(0) ;
		try {
			result=this.baseJdbcTemplate.findForListMap(sql.toString(),condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public List<Map<String, Object>> getStudentIdInAudit(Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select info.resourceid \"STUDENTINFOID\" "); 
		sql.append(" from edu_roll_stuaudit gaudit,edu_roll_studentinfo info ");
		sql.append(" where gaudit.studentinfoid = info.resourceid and gaudit.isDeleted = 0 ");
		
		List<Map<String, Object>> result=new ArrayList<Map<String,Object>>(0) ;
		try {
			result=this.baseJdbcTemplate.findForListMap(sql.toString(),condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@Override
	public List<GraduationInfoExportVo> getGraduateAuditResults_new(Map <String,Object> condition){
		StringBuffer sql = new StringBuffer();
		sql.append("	select ers.studyno studyno,ers.studentName ,  ");
		sql.append("	decode(era.graduateauditstatus,1,'审核通过','审核不通过') graduateauditstatus,era.graduateauditmemo, ");
		sql.append("	decode(era.thegraduationstatis,1,'审核通过','审核不通过') thegraduationstatis,era.thegraduationmemo, ");
		sql.append("	decode(era.confirm,1,'已确认','未确认') confirm, ");
		sql.append("	case when (to_number("+condition.get("currentYearInfo")+")-to_number(substr(ebg.gradename,1,4)))>=2 then '达到' else '未达到' end eduYear, ");
		sql.append("	case when (ers.studentstatus = '11' or ers.studentstatus = '21' or ers.studentstatus = '16' or ers.studentstatus = '24') then '达到' else '未达到' end studentStatus, ");
		sql.append("	ers.finishedcredithour,ers.finishednecesscredithour,etp.thegraduationscore, ");
		sql.append("	ebg.gradename,ebc.classicname,ebm.majorname,hsu.unitname,count(ee.courseid) courseCount,wm_concat(ee.coursename) courselist ");
		sql.append("	from edu_roll_stuaudit era ");
		sql.append("	join edu_roll_studentinfo ers on ers.resourceid = era.studentinfoid  ");//and ers.studentstatus in ('16','24')
		if (condition.containsKey("classId")) {
			sql.append(" and ers.CLASSESID=:classId ");
		}
		if (condition.containsKey("branchSchool")) {
			sql.append(" and ers.branchschoolid=:branchSchool ");
		}
		if (condition.containsKey("grade")) {
			sql.append(" and ers.gradeid =:grade ");
		}
		if (condition.containsKey("major")) {
			sql.append(" and ers.majorid=:major ");
		}
		if (condition.containsKey("classic")) {
			sql.append(" and ers.classicid=:classic ");
		}
		if (condition.containsKey("name")) {
			sql.append(" and ers.studentname=:name ");
		}
		if (condition.containsKey("studyNo")) {
			sql.append(" and ers.studyno=:studyNo ");
		}
		if (condition.containsKey("stus")) {
			sql.append(" and ers.resourceid in ('"+condition.get("stus")+"') ");
		}
		if (condition.containsKey("studentstatus")) {
			sql.append(" and ers.studentstatus =:studentstatus ");
		}
		if (condition.containsKey("stuIds")) {
			sql.append(" and ers.resourceid not in  "+condition.get("stuIds"));
		}
		if (condition.containsKey("statusNeed")) {
			sql.append(" and (ers.studentstatus = '11' or info.studentstatus = '21' or info.studentstatus = '25')  ");
		}
		sql.append("	join edu_base_grade ebg on ebg.resourceid =ers.gradeid  ");
		sql.append("	left join edu_teach_graduatedata etg on etg.resourceid = era.graduatedataid ");
		sql.append("	join edu_teach_plan etp on etp.resourceid = ers.teachplanid  ");
		sql.append("	join edu_base_classic ebc on ebc.resourceid = ers.classicid ");
		sql.append("	join edu_base_major ebm on ebm.resourceid = ers.majorid ");
		sql.append("	join hnjk_sys_unit hsu on hsu.resourceid = ers.branchschoolid ");		
		sql.append("	left join ( ");
		sql.append("  select cc.resourceid,cc.coursename,cc.courseid,cc.coursescoretype,cc.maxscore from  ");
		sql.append("  (  ");
		sql.append("  select ers.resourceid,ebco.coursename,ete.courseid,ete.coursescoretype,  ");
		sql.append("  decode(max(to_number(F_decrypt_score(ete.integratedscore,ete.studentid))),null,0,max(to_number(F_decrypt_score(ete.integratedscore,ete.studentid)))) maxscore  ");
		sql.append("  from edu_teach_examresults ete  ");
		sql.append("  join edu_roll_studentinfo ers on ers.resourceid = ete.studentid  ");
		sql.append("  join edu_base_course ebco on ebco.resourceid = ete.courseid and ebco.isdeleted = 0  ");
		sql.append("  where ete.isdeleted = 0  and ete.checkstatus = '4'  ");
		sql.append("  and not exists (select etn.resourceid from edu_teach_noexam etn where etn.studentid = ers.resourceid and etn.courseid = ete.courseid and etn.checkstatus=1 and etn.isdeleted=0)  ");
		sql.append("  and not exists (select ersa.resourceid from edu_roll_stuaudit ersa where ersa.graduateauditstatus =1 and ersa.isdeleted = 0 and ersa.studentinfoid = ers.resourceid) ");
		sql.append("  group by ers.resourceid,ete.courseid,ete.coursescoretype,ebco.coursename  ");
		sql.append("  order by ers.resourceid,ete.courseid,ebco.coursename  ");
		sql.append("  ) cc where  cc.maxscore<=59 and cc.coursescoretype = '11'  ");
		sql.append("  and not exists(  ");
		sql.append("  select dd.resourceid,dd.coursename,dd.courseid,dd.coursescoretype,dd.maxscore from   ");
		sql.append("  (  ");
		sql.append("  select ers.resourceid,ebco.coursename,ete.courseid,ete.coursescoretype,  ");
		sql.append("  decode(max(to_number(F_decrypt_score(ete.integratedscore,ete.studentid))),null,0,max(to_number(F_decrypt_score(ete.integratedscore,ete.studentid)))) maxscore  ");
		sql.append("  from edu_teach_examresults ete  ");
		sql.append("  join edu_roll_studentinfo ers on ers.resourceid = ete.studentid  ");
		sql.append("  join edu_base_course ebco on ebco.resourceid = ete.courseid and ebco.isdeleted = 0  ");
		sql.append("  where   ");
		sql.append("  ete.isdeleted = 0  and ete.checkstatus = '4'   ");
		sql.append("  and not exists (select etn.resourceid from edu_teach_noexam etn where etn.studentid = ers.resourceid and etn.courseid = ete.courseid and etn.checkstatus=1 and etn.isdeleted=0)  ");
		sql.append("  and not exists (select ersa.resourceid from edu_roll_stuaudit ersa where ersa.graduateauditstatus =1 and ersa.isdeleted = 0 and ersa.studentinfoid = ers.resourceid) ");
		sql.append("  group by ers.resourceid,ete.courseid,ete.coursescoretype,ebco.coursename  ");
		sql.append("  order by ers.resourceid,ete.courseid,ebco.coursename  ");
		sql.append("  ) dd where dd.coursescoretype = '25' and dd.maxscore>=2512  and dd.resourceid = cc.resourceid and dd.courseid = cc.courseid  ");
		sql.append("  )  ");		
		sql.append("	) ee on ee.resourceid=era.studentinfoid  ");
		sql.append("	where era.isdeleted = 0  ");
		if (condition.containsKey("auditStatus")) {
			sql.append(" and era.graduateauditstatus=:auditStatus ");
		}
		if (condition.containsKey("confirmStatus")) {
			sql.append(" and era.confirm =:confirmStatus ");
		}
		if (condition.containsKey("forConfirm")) {
			sql.append(" and (era.graduateauditstatus='1' or era.thegraduationstatis = '1') "); //毕业 + 结业
		}
		sql.append("	group by  ers.studyno,ers.studentname,era.graduateauditstatus,era.graduateauditmemo,era.thegraduationstatis, ");
		sql.append("	era.thegraduationmemo,era.confirm,ers.studentstatus,ers.finishedcredithour,ers.finishednecesscredithour, ");
		sql.append("	etp.thegraduationscore,ebg.gradename,ebc.classicname,ebm.majorname,hsu.unitname ");
		sql.append("	order by ebg.gradename,ebc.classicname,ebm.majorname,hsu.unitname,ers.studyno ");
	
		try {
			List<GraduationInfoExportVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), GraduationInfoExportVo.class,condition);
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
