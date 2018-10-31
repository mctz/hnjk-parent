package com.hnjk.edu.roll.service.jdbcImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.service.IGraduationStatJDBCService;

@Service("graduationStatJDBCService")
@Transactional
public class GraduationJDBCServiceImpl  extends BaseSupportJdbcDao  implements IGraduationStatJDBCService {

	@Override
	public List<Map<String, Object>> statGraduationInfo(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		//追加字段
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: sql.append(" to_char(a.graduatedate,'yyyy-mm-dd') \"graduatedate\", "); break;
		case 2: sql.append(" to_char(a.graduatedate,'yyyy-mm-dd') \"graduatedate\",d.classicname, ");  break;
		case 3: sql.append(" to_char(a.graduatedate,'yyyy-mm-dd') \"graduatedate\",d.classicname,e.unitname,f.gradename,h.majorname, "); break;
		case 4: sql.append(" d.classicname,f.gradename, "); break;
		case 5: sql.append(" e.unitname, "); break;
		default : break;
		}
		sql.append(" sum(1) \"total\", ");
		sql.append(" sum(decode(c.gender,'1',1,0)) \"man\", ");
		sql.append(" sum(decode(c.gender,'2',1,0)) \"lady\" ");
		sql.append(" from edu_teach_graduatedata a ,edu_roll_studentinfo b ,edu_base_student c  ");
		//追加表
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: break;
		case 2: sql.append(" ,edu_base_classic d "); break;
		case 3: sql.append(" ,edu_base_classic d  ,hnjk_sys_unit e  ,edu_base_grade f  ,edu_base_major h "); break;
		case 4: sql.append(" ,edu_base_classic d  ,edu_base_grade f ");break;
		case 5: sql.append(" ,hnjk_sys_unit e ");break;
		default : break;
		}
		sql.append(" where a.studentid = b.resourceid ");
		sql.append(" and a.isdeleted = 0 ");
		sql.append(" and b.studentbaseinfoid = c.resourceid ");
		
		//sql.append(" and a.publishstatus='Y' ");
		//追加联合条件
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: break;
		case 2: sql.append(" and d.resourceid= b.classicid "); break;
		case 3: sql.append(" and d.resourceid= b.classicid and e.resourceid = b.branchschoolid and f.resourceid = b.gradeid and h.resourceid = b.majorid "); break;
		case 4: sql.append(" and d.resourceid= b.classicid and f.resourceid = b.gradeid ");break;
		case 5: sql.append(" and e.resourceid = b.branchschoolid ");break;
		default : break;
		}
		//追加查询条件
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: 
			if(condition.containsKey("graduateDateRange")) {
				sql.append(" and a.graduatedate between "+condition.get("graduateDateRange"));
			}
			break;
		case 2: 
			if(condition.containsKey("graduateDateRange")) {
				sql.append(" and a.graduatedate between "+condition.get("graduateDateRange"));
			}
			if(condition.containsKey("classic")){
				sql.append(" and b.classicid = '"+condition.get("classic")+"' ");
			}
			break;
		case 3: 
			if(condition.containsKey("graduateDateRange")) {
				sql.append(" and a.graduatedate between "+condition.get("graduateDateRange"));
			}
			if(condition.containsKey("classic")){
				sql.append(" and b.classicid = '"+condition.get("classic")+"' ");
			}
			if(condition.containsKey("brSchool")){
				sql.append(" and b.branchschoolid = '"+condition.get("brSchool")+"' ");
			}
			if(condition.containsKey("grade")){
				sql.append(" and b.gradeid = '"+condition.get("grade")+"' ");
			}
			if(condition.containsKey("major")){
				sql.append(" and b.majorid = '"+condition.get("major")+"' ");
			}
			break;
		case 4: 
			if(condition.containsKey("classic")){
				sql.append(" and b.classicid = '"+condition.get("classic")+"' ");
			}
			if(condition.containsKey("grade")){
				sql.append(" and b.gradeid = '"+condition.get("grade")+"' ");
			}
			break;
		case 5: 
			if(condition.containsKey("brSchool")){
				sql.append(" and b.branchschoolid = '"+condition.get("brSchool")+"' ");
			}
			break;
		default : break;
		}
		boolean hascGDb = condition.containsKey("confirmGraduateDateb");
		boolean hascGDe = condition.containsKey("confirmGraduateDatee");
		if(hascGDb||hascGDe){
			if (hascGDb&&hascGDe){
			sql.append(" and a.resourceid in (select z.graduatedataid from EDU_ROLL_STUAUDIT z  where z.auditTime between to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-mm-dd') and to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-mm-dd')  and z.graduateAuditStatus='1' ) ");
			}
			if (hascGDb&&!hascGDe){
				sql.append(" and a.resourceid in (select z.graduatedataid from EDU_ROLL_STUAUDIT z  where z.auditTime > to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-mm-dd')   and z.graduateAuditStatus='1' ) ");
				}
			if (!hascGDb&&hascGDe){
				sql.append(" and a.resourceid in (select z.graduatedataid from EDU_ROLL_STUAUDIT z  where z.auditTime < to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-mm-dd')  and z.graduateAuditStatus='1' ) ");
				}
		}
		//追加分组条件
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: sql.append(" group by a.graduatedate order by a.graduatedate "); break;
		case 2: sql.append(" group by a.graduatedate,d.classicname order by a.graduatedate "); break;
		case 3: sql.append(" group by a.graduatedate,d.classicname,e.unitname,f.gradename,h.majorname order by f.gradename "); break;
		case 4: sql.append(" group by d.classicname,f.gradename order by f.gradename  "); break;
		case 5: sql.append(" group by e.unitname"); break;
		default : break;
		}
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> exportWithCustom(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append("select * from edu_roll_studentinfo info ,edu_base_student student ");
		sql.append("where info.studentbaseinfoid =student.resourceid ");
		sql.append("and info.studentbaseinfoid is not null ");
		sql.append("and info.isdeleted=0 ");
		if(condition.containsKey("gradeid")) {
			if(!"".equals(condition.get("gradeid"))) {
				sql.append("and info.gradeid = '" + condition.get("gradeid") + "' ");
			}
		}
		if(condition.containsKey("classicid")) {
			if(!"".equals(condition.get("classicid"))) {
				sql.append("and info.classicid = '" + condition.get("classicid") + "' ");
			}
		}
		if(condition.containsKey("branchschoolid")) {
			if(!"".equals(condition.get("branchschoolid"))) {
				sql.append("and info.branchschoolid = '" + condition.get("branchschoolid") + "' ");
			}
		}
		if(condition.containsKey("majorid")) {
			if(!"".equals(condition.get("majorid"))) {
				sql.append("and info.majorid = '" + condition.get("majorid") + "' ");
			}
		}
		if(condition.containsKey("studentstatus")) {
			if(!"".equals(condition.get("studentstatus"))) {
				sql.append("and info.studentstatus = '" + condition.get("studentstatus") + "' ");
			}
		}
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> exportDegreeStat(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select   t.graduatedate,unit.unitname,grade.gradename ,classic.classicname,major.majorname ,sum(1) \"total\",sum(decode(c.gender,'1',1,0)) \"man\" ,sum(decode(c.gender,'2',1,0)) \"lady\"    ");
		sql.append(" from edu_teach_graduatedata t ,edu_roll_studentinfo s ,edu_base_student c  ");
		sql.append(" ,edu_base_classic classic,edu_base_grade grade,edu_base_major major ,hnjk_sys_unit unit ");
		sql.append(" where t.isdeleted=0 " +
				//" and t.publishstatus='Y' " +
				" and classic.classiccode!='3' ");
		sql.append(" and   s.classicid = classic.resourceid ");
		sql.append(" and   s.gradeid   = grade.resourceid ");
		sql.append(" and   s.majorid   = major.resourceid ");
		sql.append(" and   s.branchschoolid = unit.resourceid ");
		sql.append(" and   t.studentid                    = s.resourceid ");
		sql.append(" and   s.studentbaseinfoid            = c.resourceid ");
		sql.append(" and   t.degreestarus='Y'  ");
		if(condition.containsKey("graduatedate")) {
			sql.append(" and   t.graduatedate                 = to_date('"+condition.get("graduatedate")+"','yyyy-mm-dd') ");
		}
		if(condition.containsKey("branchschoolid")) {
			sql.append(" and   s.branchschoolid               = '"+condition.get("branchschoolid")+"' ");
		}
		if(condition.containsKey("gradeid")) {
			sql.append(" and   s.gradeid                      = '"+condition.get("gradeid")+"' ");
		}
		if(condition.containsKey("classicid")) {
			sql.append(" and   s.classicid                    = '"+condition.get("classicid")+"' ");
		}
		if(condition.containsKey("majorid")) {
			sql.append(" and   s.majorid                      = '"+condition.get("majorid")+"' ");
		}
		
		//增加毕业确认时间绑定
		boolean hascGDb = condition.containsKey("confirmGraduateDatebInDegreeExport");
		boolean hascGDe = condition.containsKey("confirmGraduateDateeInDegreeExport");
		if(hascGDb||hascGDe){
			if (hascGDb&&hascGDe){
			sql.append(" and t.resourceid in (select z.graduatedataid from EDU_ROLL_STUAUDIT z  where z.auditTime between to_date('"+condition.get("confirmGraduateDatebInDegreeExport")+"','yyyy-mm-dd') and to_date('"+condition.get("confirmGraduateDateeInDegreeExport")+"','yyyy-mm-dd')  and z.graduateAuditStatus='1' ) ");
			}
			if (hascGDb&&!hascGDe){
				sql.append(" and t.resourceid in (select z.graduatedataid from EDU_ROLL_STUAUDIT z  where z.auditTime > to_date('"+condition.get("confirmGraduateDatebInDegreeExport")+"','yyyy-mm-dd')   and z.graduateAuditStatus='1' ) ");
				}
			if (!hascGDb&&hascGDe){
				sql.append(" and t.resourceid in (select z.graduatedataid from EDU_ROLL_STUAUDIT z  where z.auditTime < to_date('"+condition.get("confirmGraduateDateeInDegreeExport")+"','yyyy-mm-dd')  and z.graduateAuditStatus='1' ) ");
				}
		}
		
		sql.append(" group by  t.graduatedate,unit.unitname,grade.gradename ,classic.classicname,major.majorname ");
						
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/**
	 * 得到某一字段的不重复集合
	 */
	@Override
	public List<String> getSingleDistinctPropertyValue(
			String propertyName,String order) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select distinct "+propertyName);
		sql.append(" from edu_teach_graduatedata  ");
		sql.append(" where isdeleted = 0 ");
		if(null!=order){
			sql.append(" order by "+order +" desc");
		}
		List<Map<String,Object>> resultList_map= new ArrayList<Map<String,Object>>(0);
		List<String> resultList = new ArrayList<String>(0);
		try {
			resultList_map = this.baseJdbcTemplate.findForList(sql.toString(), null);
			for (Map<String,Object> map : resultList_map) {
				if (map.containsKey(propertyName.toUpperCase())) {
					resultList.add(ExStringUtils.toString(map.get(propertyName.toUpperCase())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/**
	 * 学籍卡打印
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> studentRollCardInfoList(Map<String, Object> condition) throws Exception {
		
		StringBuilder sql 		 = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted",0);
		sql.append(" select st.name,st.nameused,st.gender sex,st.nation,st.bornday,st.politics,st.certnum,u.unitname,g.gradename,c.shortname classicname,m.majorname,cl.classesname,si.studyno,st.certPhotoPath,st.certPhotoPathReverse,si.graduateschool,si.graduatedate,si.graduatemajor,si.memo, ");
		sql.append(" decode(instr(st.homeplace, ',', -1),0,st.homeplace,substr(st.homeplace, 0,instr(st.homeplace, ',', -1)-1)) homeplace,st.residence,st.mobile,st.email,st.homeaddress,st.homezipcode,st.homephone,st.officename,st.officephone, '' officezipcode,st.recruitPhotoPath,st.photopath,");
		sql.append(" st.height,st.bloodType,st.health,st.specialization,gd.degreeName,en.graduateId,");
		sql.append(" p.eduyear||'年' eduyear,gd.resourceid gdId,st.resourceid basestudentid,si.resourceid studentid,si.inDate stuindate,g.indate gindate,si.LEARNINGSTYLE teachtype,si.totalpoint TOTALPOINT,st.marriage,st.title,si.organizationDate,si.joinPartyDate ");
		sql.append(" ,gd.graduatedate,gd.diplomanum,gd.degreenum,gd.graduateType,gd.degreestarus,gd.degreestarus degreestatus from EDU_TEACH_GRADUATEDATA gd ");
		if(condition.containsKey("fromStudentRoll")){
			sql.append(" right join edu_roll_studentinfo si on si.resourceid = gd.studentid ");		
		}else{
			sql.append(" join edu_roll_studentinfo si on si.resourceid = gd.studentid ");
		}
		sql.append(" left join edu_teach_plan p on p.resourceid=si.teachplanid");
		sql.append(" join edu_base_student st  on si.studentbaseinfoid = st.resourceid ");
		sql.append(" left join EDU_RECRUIT_ENROLLEEINFO en on en.studentbaseinfoid = st.resourceid");
		/*if(condition.containsKey("fromStudentRoll")){
			sql.append(" left join ( select resu.studentid,sum(1) num1 from edu_base_studentresume resu where resu.isdeleted = 0 group by resu.studentid ) aa on aa.studentid = st.resourceid ");
			sql.append(" left join ( select person.studentbaseinfoid,sum(1) num2 from edu_base_personanralation person where person.isdeleted = 0 group by person.studentbaseinfoid ) bb on bb.studentbaseinfoid = st.resourceid ");
		}*/
//		if (condition.containsKey("name")) {
//			sql.append("and st.name like '%"+condition.get("name").toString()+"%' ");
////			param.put("name","'%"+condition.get("name").toString()+"%'");
//		}
		sql.append("   join hnjk_sys_unit u on si.branchschoolid = u.resourceid ");
		if (condition.containsKey("branchSchool")) {
			sql.append("and u.resourceid=:branchSchool");
			param.put("branchSchool",condition.get("branchSchool"));
		}
		sql.append("   join edu_base_grade g on si.gradeid = g.resourceid ");
		if (condition.containsKey("grade")) {
			sql.append("and g.resourceid=:grade");
			param.put("grade",condition.get("grade"));
		}
		sql.append("   join edu_base_classic c on si.classicid = c.resourceid ");
		if (condition.containsKey("classic")) {
			sql.append("and c.resourceid=:classic");
			param.put("classic", condition.get("classic"));
		}
		sql.append("   join edu_base_major m on si.majorid = m.resourceid ");
		if (condition.containsKey("major")) {
			sql.append("and m.resourceid=:major");
			param.put("major", condition.get("major"));
		}
		sql.append("  left join edu_roll_classes cl on si.classesid = cl.resourceid ");
		if (condition.containsKey("classes")) {//班级
			sql.append("and cl.resourceid=:classes");
			param.put("classes", condition.get("classes"));
		}else if(condition.containsKey("classesid")){
			sql.append("and cl.resourceid=:classesid");
			param.put("classesid", condition.get("classesid"));
		}
		if(condition.containsKey("fromStudentRoll")){
			sql.append("   where si.isdeleted = :isDeleted  and st.isdeleted = 0");
		}else{
			sql.append("   where gd.isdeleted = :isDeleted ");
		}
		/*if (condition.containsKey("classes")) { //班级
			sql.append(" and si.classesid = '"+condition.get("classes")+"'");
		}*/
		if (condition.containsKey("degreeStatus")) {//学位状态
			sql.append(" and gd.degreestarus = '"+condition.get("degreeStatus")+"' ");
		}
		if(condition.containsKey("stuStatus")){
			sql.append(" and si.studentstatus='"+condition.get("stuStatus")+"'");
		}
		
		if (condition.containsKey("resourceid")) {
			sql.append(" and gd.resourceid in('"+ExStringUtils.replace(condition.get("resourceid")+"", ",", "','")+"' ) ");
		}
		if (condition.containsKey("graduateDate")) {
			sql.append(" and gd.graduatedate = to_date('"+condition.get("graduateDate")+"','yyyy-mm-dd')");
		}
		if(condition.containsKey("confirmGraduateDateb")||condition.containsKey("confirmGraduateDatee")){
			param.put("graduateauditstatus",1);
			sql.append(" and exists(  select sa.resourceid from EDU_ROLL_STUAUDIT sa where sa.isdeleted = :isDeleted and sa.graduatedataid = gd.resourceid and sa.graduateauditstatus =:graduateauditstatus");
			if (condition.containsKey("confirmGraduateDateb")) {
				sql.append("             and sa.audittime > to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-MM-dd')");
			}
			if (condition.containsKey("confirmGraduateDatee")) {
				sql.append("             and sa.audittime < to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-MM-dd')");
			}
			sql.append(" )");
		}
		
		//if(condition.containsKey("fromStudentRoll")){
			//来自学籍管理 的统计
			if (condition.containsKey("entranceFlag")) {
				sql.append(" and si.enterauditstatus ='"+condition.get("entranceFlag")+"' ");
			}
			if(condition.containsKey("accountStatus")){
				sql.append(" and si.accoutstatus ='"+condition.get("accountStatus")+"' ");		
			}
			if(condition.containsKey("certNum")){//身份证号
				sql.append(" and st.certnum ='"+condition.get("certNum")+"' ");
			}
			/*if(condition.containsKey("classesid")){//班级
				sql.append(" and si.classesid ='"+condition.get("classesid")+"' ");
			}*/
			if(condition.containsKey("rollCard")){
				sql.append(" and si.rollcardstatus ='"+condition.get("rollCard")+"' ");
			}
			/*if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
		 		sql.append(" and nvl(aa.num1,0) > 0 ");
				sql.append(" and nvl(bb.num2,0) > 0 ");
			}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
				sql.append(" and nvl(aa.num1,0) = 0 ");
				sql.append(" and nvl(bb.num2,0) = 0 ");
			}*/ 
			if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
				if(condition.containsKey("schoolrollstudentstatus")){
					sql.append(" and( si.studentStatus = '11' or si.studentstatus = '21' or si.studentstatus = '25' or si.studentStatus in ("+condition.get("schoolrollstudentstatus")+"))");
				}
			}
			if (condition.containsKey("studyNo")) {
				sql.append("and si.studyno=:studyNo");
				param.put("studyNo",condition.get("studyNo"));
			}
		//}
		if (condition.containsKey("studentids")) {
			sql.append(" and si.resourceid in('"+ExStringUtils.replace(condition.get("studentids").toString(), ",", "','")+"' ) ");
		}
		sql.append(" order by u.unitcode,c.classiccode,m.majorcode,si.studyno ");
		return baseJdbcTemplate.findForListMap(sql.toString(), param);
	}
	
	
	
	/**
	 * 学籍卡打印
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> studentCardInfo(Map<String, Object> condition,String name) throws Exception {
		String sql = "  select  * from  edu_base_student bs ,edu_base_classic bc ,edu_base_grade bg,hnjk_sys_unit su, " +
		"edu_base_major bm,edu_roll_studentinfo rs where rs.studentbaseinfoid = bs.resourceid and  " +
		"rs.classicid = bc.resourceid and  rs.majorid = bm.resourceid and rs.gradeid = bg.resourceid  " +
		"and bs.name='"+name+"' and rs.branchschoolid = su.resourceid   order by bs.name";
		return baseJdbcTemplate.findForListMap(sql.toString(), null);
	}
	
	
	/**
	 * 学籍卡打印_学生学籍用
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> studentRollCardInfoList_stu(Map<String, Object> condition) throws Exception {
		
		StringBuilder sql 		 = new StringBuilder();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted",0);
		sql.append(" select st.name,st.nameused,st.gender sex,st.nation,st.bornday,st.politics,st.certnum,u.unitname,u.unitshortname,g.gradename,c.shortname classicname,m.majorname,si.teachingtype teachtype, si.studyno,");
		sql.append("   st.homeplace,st.residence,en.TOTALPOINT,st.mobile,st.email,st.homeaddress,st.homezipcode,st.homephone,st.officename,st.officephone, st.officezipcode officezipcode,st.photopath,st.industryType,");		
		sql.append("   gd.graduatetype,gd.graduatedate,gd.diplomanum,''graduatenum,gd.degreenum,gd.resourceid gdId,st.resourceid basestudentid,si.resourceid studentid,st.recruitPhotoPath,st.marriage,st.title,si.organizationDate,si.joinPartyDate,si.reWardsPuniShment,si.memo,");
		sql.append("   si.inDate stuindate,g.indate gindate,si.graduateschool,si.graduatedate beforeGraduatedate,graduatemajor  ");// 加入入学日期
		sql.append("   from edu_roll_studentinfo si ");		
		sql.append("   join edu_base_student st  on si.studentbaseinfoid = st.resourceid ");
		sql.append("   left join EDU_RECRUIT_ENROLLEEINFO en on en.studentbaseinfoid = st.resourceid ");
		sql.append("   join hnjk_sys_unit u on si.branchschoolid = u.resourceid ");
		if (condition.containsKey("branchSchool")) {
			sql.append("and u.resourceid=:branchSchool");
			param.put("branchSchool",condition.get("branchSchool"));
		}
		sql.append(" join edu_base_grade g on si.gradeid = g.resourceid ");
		if (condition.containsKey("stuGrade")) {
			sql.append("and g.resourceid=:grade");
			param.put("grade",condition.get("stuGrade"));
		}
		sql.append(" join edu_base_major m on si.majorid = m.resourceid ");
		if (condition.containsKey("major")) {
			sql.append("and m.resourceid=:major");
			param.put("major", condition.get("major"));
		}
		sql.append(" join edu_base_classic c on si.classicid = c.resourceid ");
		if (condition.containsKey("classic")) {
			sql.append("and c.resourceid=:classic");
			param.put("classic", condition.get("classic"));
		}
		//以学籍表的角度出发，学籍外联毕业生库
		sql.append(" left join EDU_TEACH_GRADUATEDATA gd on si.resourceid = gd.studentid ");
		sql.append(" where si.isdeleted = :isDeleted ");
//		if (condition.containsKey("name")) {
//			sql.append("and si.studentname like '%"+condition.get("name").toString()+"%'");
//		}
		if (condition.containsKey("studyNo")) {
			sql.append(" and si.studyno=:studyNo");
			param.put("studyNo",condition.get("studyNo"));
		}
		if (condition.containsKey("stuStatus")) {
			sql.append(" and si.studentstatus=:stuStatus");
			param.put("stuStatus", condition.get("stuStatus"));
		}
		if (condition.containsKey("certNum")) {
			sql.append(" and st.certnum=:certNum");
			param.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("rollCard")){
			sql.append(" and si.rollcardstatus =:rollCard ");
			param.put("rollCard", condition.get("rollCard"));
		}
		/*if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
			sql.append(" and exists (select * from EDU_BASE_STUDENTRESUME sr where sr.studentid=si.studentBaseInfoid )" );
			sql.append(" and exists (select *  from EDU_BASE_PERSONANRALATION pr where pr.studentBaseInfoid=si.studentBaseInfoid )");
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
			sql.append(" and not exists (select * from EDU_BASE_STUDENTRESUME sr where sr.studentid=si.studentBaseInfoid )" );
			sql.append(" and not exists (select *  from EDU_BASE_PERSONANRALATION pr where pr.studentBaseInfoid=si.studentBaseInfoid )");
		}*/
		if (condition.containsKey("teachPlan")){
			if(Constants.BOOLEAN_YES.equals(condition.get("teachPlan").toString())){
				sql.append(" and si.TEACHPLANID is not null ");
			}else if (Constants.BOOLEAN_NO.equals(condition.get("teachPlan").toString())){
				sql.append(" and si.TEACHPLANID is  null  ");
			}
		}
		if (condition.containsKey("entranceFlag")) {
			sql.append(" and si.enterAuditStatus = :entranceFlag");
			param.put("entranceFlag",condition.get("entranceFlag"));
		}
		if(condition.containsKey("accountStatus")){
			sql.append(" and si.accountStatus = :accountStatus");
			param.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));
		}
		if (condition.containsKey("resourceid")) {
			sql.append(" and si.resourceid in('"+ExStringUtils.replace(condition.get("resourceid").toString(), ",", "','")+"' ) ");
		}
		
		//根据m需求，学籍卡按层次专业学号升序排序
		sql.append(" order by c.classiccode,m.majorcode,si.studyno asc ");
		return baseJdbcTemplate.findForListMap(sql.toString(), param);
	}
	
	/**
	 * 级联  年级-专业
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String, Object>> cascadeGradeToMajor(Map<String,Object> param)throws ServiceException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql = new StringBuffer("select distinct  mj.MAJORNAME majorName,mj.resourceid from edu_teach_guiplan gplan");
			sql.append(" join  edu_base_grade grade on gplan.gradeid = grade.resourceid ");
			sql.append(" join edu_teach_plan pln on pln.resourceid = gplan.planid ");
			sql.append(" join edu_base_major mj on mj.resourceid = pln.majorid ");
			sql.append(" where gplan.isdeleted = 0 and pln.isdeleted  = 0 and mj.isdeleted = 0 ");
			sql.append(" and grade.isdeleted = 0 ");
			if(param.containsKey("gradeId")){
				sql.append(" and grade.resourceid = :gradeId ");
			}
			list = baseJdbcTemplate.findForListMap(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	/**
	 * 级联  层次-专业
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String, Object>> cascadeGradeToMajor1(Map<String,Object> param)throws ServiceException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql = new StringBuffer("select distinct  mj.MAJORNAME majorName,mj.resourceid,mj.MAJORCODE from edu_teach_guiplan gplan");
			sql.append(" join  edu_base_grade grade on gplan.gradeid = grade.resourceid ");
			sql.append(" join edu_teach_plan pln on pln.resourceid = gplan.planid ");
			sql.append(" join edu_base_major mj on mj.resourceid = pln.majorid ");
			sql.append(" join EDU_BASE_CLASSIC c on pln.CLASSICID=c.resourceid ");
			sql.append(" where gplan.isdeleted = 0 and pln.isdeleted  = 0 and mj.isdeleted = 0 ");
			sql.append(" and grade.isdeleted = 0 ");
			if(param.containsKey("gradeId")){
				sql.append(" and grade.resourceid = :gradeId ");
			}		
			if(param.containsKey("classic")){
				sql.append(" and c.resourceid = :classic ");
			}
			if(param.containsKey("branchSchool")){
				sql.append(" and pln.BRSCHOOLID = :branchSchool ");
			}
			sql.append(" order by mj.MAJORCODE,mj.MAJORNAME ");
			list = baseJdbcTemplate.findForListMap(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 级联  专业-班级 -教学站
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String, Object>> cascadeMajorToClasses(Map<String,Object> param)throws ServiceException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql = new StringBuffer("select distinct cls.CLASSESNAME classname,cls.resourceid from edu_roll_classes cls");
			sql.append(" join edu_base_major mj on mj.resourceid = cls.majorid ");
			sql.append(" join edu_base_grade grade on  cls.gradeid = grade.resourceid ");
			sql.append(" join hnjk_sys_unit ut on ut.resourceid = cls.orgunitid ");
			sql.append(" where cls.isdeleted = 0 and mj.isdeleted = 0 and grade.isdeleted = 0 and ut.isdeleted = 0 ");
			if(param.containsKey("majorId")){
				sql.append(" and mj.resourceid = :majorId ");
			}
			if(param.containsKey("gradeId")){
				sql.append(" and grade.resourceid = :gradeId ");
			}
			if(param.containsKey("brschoolId")){
				sql.append(" and  ut.resourceid = :brschoolId ");
			}
			list = baseJdbcTemplate.findForListMap(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 级联  专业-班级 -教学站  //学院2016修改
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String, Object>> cascadeMajorToCourse(Map<String,Object> param)throws ServiceException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql = new StringBuffer();
			if(param.containsKey("isMk")){
				sql.append("select distinct c.resourceid, c.coursecode,c.coursename  from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid  join edu_base_classic cc on stu.classicid = cc.resourceid and cc.isdeleted=0  join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid  join edu_base_major major on stu.majorid = major.resourceid  join edu_base_course c on a.courseid = c.resourceid  join edu_roll_classes es on stu.classesid = es.resourceid  join edu_base_grade g on g.resourceid = stu.gradeid  left join edu_teach_examresults re on a.resultsid  = re.resourceid  join edu_teach_examsub sub on sub.resourceid=re.examsubid  where stu.isdeleted = 0 and (stu.studentstatus='11' or stu.studentstatus='21' or stu.studentstatus='25') and a.isdeleted = 0 and sub.isdeleted=0  and a.nextexamsubid=:examsub and  (select count(*) from EDU_TEACH_EXAMRESULTS re where re.isdeleted = 0 and re.COURSEID = a.courseid and re.studentid = a.studentid and re.checkstatus='4')<4   ");
				if(param.containsKey("unid")){
					sql.append("  and stu.branchschoolid=:unid ");
				}
			}else
			{
				sql.append("select distinct c.resourceid,c.coursecode,c.coursename from EDU_TEACH_COURSESTATUS oc left join edu_teach_plancourse pc on pc.resourceid=oc.plancourseid left join edu_base_course c on c.resourceid=pc.courseid where oc.isopen='Y'  and oc.isDeleted=0  ");
				if(param.containsKey("examsub")){
					sql.append(" and oc.term=(select y.firstyear||'_0'||eb.term from edu_teach_examsub eb left join edu_base_year y on y.resourceid=eb.yearid  where  eb.resourceid=:examsub ) ");
				}
				if(param.containsKey("unid")){
					sql.append("  and oc.schoolids=:unid ");
				}
			}
			
			list = baseJdbcTemplate.findForListMap(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	
	/**
	 * 级联  层次-班级 -教学站
	 * @param param
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String, Object>> cascadeMajorToClasses1(Map<String,Object> param)throws ServiceException{
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql = new StringBuffer("select distinct cls.CLASSESNAME classname,cls.resourceid from edu_roll_classes cls");
			sql.append(" join edu_base_major mj on mj.resourceid = cls.majorid ");
			sql.append(" join edu_base_grade grade on  cls.gradeid = grade.resourceid ");
			sql.append(" join hnjk_sys_unit ut on ut.resourceid = cls.orgunitid ");
			if(param.containsKey("classic")) {
				sql.append(" left join  edu_roll_studentinfo st  on st.classesid=cls.resourceid ");
			}
			sql.append(" where cls.isdeleted = 0 and mj.isdeleted = 0 and grade.isdeleted = 0 and ut.isdeleted = 0 ");
			if(param.containsKey("majorId")){
				sql.append(" and mj.resourceid = :majorId ");
			}
			if(param.containsKey("gradeId")){
				sql.append(" and grade.resourceid = :gradeId ");
			}
			if(param.containsKey("brschoolId")){
				sql.append(" and  ut.resourceid = :brschoolId ");
			}
			if(param.containsKey("classic")){
				sql.append(" and st.CLASSICID = :classic ");
			}
			sql.append(" order by cls.CLASSESNAME ");
			list = baseJdbcTemplate.findForListMap(sql.toString(), param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Override
	public Page statisticalGraduation(
			Map<String, Object> condition,Page objPage) {
		try {
			StringBuffer sql = new StringBuffer("select ut.unitname,cls.classesname,pln.eduyear,so.teachingtype,cc.classicname,sum(decode(so.studentstatus, '16', 1, 0)) bycount,sum(decode(so.studentstatus, '24', 1, 0)) jycount");
			sql.append(" ,(select count(cls.resourceid) from edu_roll_studentinfo s where s.classesid = cls.resourceid) classCount ");
			sql.append(" from edu_teach_graduatedata ga ");
			sql.append(" join edu_roll_studentinfo so on so.resourceid = ga.studentid ");
			sql.append(" join hnjk_sys_unit ut on ut.resourceid = so.branchschoolid ");
			sql.append(" join edu_roll_classes cls on cls.resourceid = so.classesid ");
			sql.append(" join edu_teach_plan pln on pln.resourceid = so.teachplanid ");
			sql.append(" join edu_base_classic cc on cc.resourceid = so.classicid ");
			List<String> values = new ArrayList<String>(0);
			//条件
			sql.append(" where 1= 1 ");
			if(condition.containsKey("statistical_branchSchool"))//教学点 
			{
				sql.append(" and ut.resourceid = ? ");
				values.add(condition.get("statistical_branchSchool")+"");
			}
			if(condition.containsKey("statistical_classesSelect"))//班级
			{
				sql.append(" and cls.resourceid = ? ");
				values.add(condition.get("statistical_classesSelect")+"");
			}
			if(condition.containsKey("statistical_classic"))//层次
			{
				sql.append(" and cc.resourceid = ? ");
				values.add(condition.get("statistical_classic")+"");
			}
			if(condition.containsKey("statistical_teachingType"))//形式
			{
				sql.append(" and so.teachingtype = ? ");
				values.add(condition.get("statistical_teachingType")+"");
			}
			sql.append(" group by so.studentstatus, ut.unitname,cls.classesname,pln.eduyear,so.teachingtype,cc.classicname,cls.resourceid ");
			sql.append(" order by ut.unitname,cls.classesname");
			objPage = baseJdbcTemplate.findListMap(objPage, sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return objPage;
	}
	
	@Override
	public List<Map<String, Object>> statisticalGraduationForExport(
			Map<String, Object> condition) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql = new StringBuffer("select ut.unitname,cls.classesname,pln.eduyear,so.teachingtype,cc.classicname,sum(decode(so.studentstatus, '16', 1, 0)) bycount,sum(decode(so.studentstatus, '24', 1, 0)) jycount");
			sql.append(" ,(select count(cls.resourceid) from edu_roll_studentinfo s where s.classesid = cls.resourceid) classCount ");
			sql.append(" from edu_teach_graduatedata ga ");
			sql.append(" join edu_roll_studentinfo so on so.resourceid = ga.studentid ");
			sql.append(" join hnjk_sys_unit ut on ut.resourceid = so.branchschoolid ");
			sql.append(" join edu_roll_classes cls on cls.resourceid = so.classesid ");
			sql.append(" join edu_teach_plan pln on pln.resourceid = so.teachplanid ");
			sql.append(" join edu_base_classic cc on cc.resourceid = so.classicid ");
			List<String> values = new ArrayList<String>(0);
			//条件
			sql.append(" where 1= 1 ");
			if(condition.containsKey("statistical_branchSchool"))//教学点 
			{
				sql.append(" and ut.resourceid = ? ");
				values.add(condition.get("statistical_branchSchool")+"");
			}
			if(condition.containsKey("statistical_classesSelect"))//班级
			{
				sql.append(" and cls.resourceid = ? ");
				values.add(condition.get("statistical_classesSelect")+"");
			}
			if(condition.containsKey("statistical_classic"))//层次
			{
				sql.append(" and cc.resourceid = ? ");
				values.add(condition.get("statistical_classic")+"");
			}
			if(condition.containsKey("statistical_teachingType"))//形式
			{
				sql.append(" and so.teachingtype = ? ");
				values.add(condition.get("statistical_teachingType")+"");
			}
			sql.append(" group by so.studentstatus, ut.unitname,cls.classesname,pln.eduyear,so.teachingtype,cc.classicname,cls.resourceid ");
			sql.append(" order by ut.unitname,cls.classesname");
			list = baseJdbcTemplate.findForList(sql.toString(), values.toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<Map<String,Object>> getStudetnRegistryForm(Map<String, Object> condition) throws ServiceException{
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select stu.studyno,g.gradename,m.majorname,c.classicname,stu.teachingtype,stu.resourceid studentid,stu.selfAssessment,stu.reWardsPuniShment,");
		sql.append(" b.certnum,b.name,b.gender,b.nation,b.bornday,b.politics,b.homePlace,b.homeAddress,b.contactAddress,b.bornAddress,b.title,b.officeName,");
		sql.append(" b.contactzipcode homezipcode,b.officephone,b.homephone,b.mobile,stu.inDate,b.resourceid basestudentid,g.graduatedate,p.eduyear ");
		sql.append("  from edu_roll_studentinfo stu                                             ");
		sql.append(" join edu_base_student b on b.resourceid = stu.studentbaseinfoid            ");
		sql.append(" join edu_base_grade g on g.resourceid = stu.gradeid                        ");
		sql.append(" join edu_base_major m on m.resourceid = stu.majorid                        ");
		sql.append(" join edu_base_classic c on c.resourceid = stu.classicid                    ");
		sql.append(" join edu_teach_plan p on p.resourceid = stu.teachplanid                    ");
		sql.append(" where 1=1                                                                  ");
//		if(condition.containsKey("")){
//			sql.append(" and stu.studyno=:studyno ");
//			param.put("studyno", condition.get(""));
//		}
		if(condition.containsKey("studentids")){
			sql.append(" and stu.resourceid in('"+ExStringUtils.replace(condition.get("studentids").toString(), ",", "','")+"' )  ");
//					sql.append(" and si.resourceid in('"+ExStringUtils.replace(condition.get("studentids").toString(), ",", "','")+"' ) ");
		}
		try {
			list = baseJdbcTemplate.findForListMap(sql.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public List<Map<String,Object>> getStudetnResumes(Map<String, Object> condition) throws ServiceException{
		List<Map<String,Object>> resumes  = new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select res.studentid ,                                                                   ");
		sql.append("        res.startyear || '年' || res.startmonth || '月 至 ' || res.endyear || '年' ||    ");
		sql.append("        res.endmonth || '月' timeArea,                                                   ");
		sql.append("        res.company,                                                                     ");
		sql.append("        res.title,res.attestator                                                                        ");
		sql.append(" from EDU_BASE_STUDENTRESUME res                                                         ");
		sql.append(" join edu_base_student b on b.resourceid = res.studentid                                 ");
		sql.append(" join edu_roll_studentinfo s on s.studentbaseinfoid = b.resourceid                       ");
		sql.append(" where res.isdeleted = 0                                                                 ");
		sql.append(" and s.resourceid in ('"+ExStringUtils.replace(condition.get("studentids").toString(), ",", "','")+"' )   ");
		sql.append(" order by res.startyear                                                                  ");
		try {
			resumes=baseJdbcTemplate.findForList(sql.toString(),null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return resumes;
	}
}
