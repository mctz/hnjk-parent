package com.hnjk.edu.roll.service.jdbcImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.service.IStudentInfoChangeJDBCService;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.platform.taglib.JstlCustomFunction;

@Service("studentInfoChangeJDBCService")
@Transactional
public class StudentInfoChangeJDBCServiceImpl  extends BaseSupportJdbcDao implements IStudentInfoChangeJDBCService{
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public List<Map<String, Object>> statStudentInfoChange(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		sql.append("select b.studyno \"学号\",c.name \"姓名\" ,d.unitname \"学习中心\", e.gradename \"年级\"  ,f.classicname \"层次\" ,g.majorname \"专业\" ");
		sql.append(",c.nation \"民族\" ,c.certnum \"身份证号\" ,a.applicationdate \"申请时间\",a.changetype \"异动类型\" ,a.finalauditstatus \"申请结果\" ");             
		sql.append("from edu_roll_stuchangeinfo a ,edu_roll_studentinfo b ,edu_base_student c ");
		sql.append(",hnjk_sys_unit d ,edu_base_grade e ,edu_base_classic f ,edu_base_major g ");
		sql.append("where a.studentid = b.resourceid and b.studentbaseinfoid = c.resourceid ");
		sql.append("and d.resourceid = b.branchschoolid ");
		sql.append("and e.resourceid = b.gradeid ");
		sql.append("and f.resourceid = b.classicid ");
		sql.append("and g.resourceid = b.majorid ");
		sql.append("and a.isdeleted = 0 and a.changetype is not null ");
		
		if (condition.containsKey("stuName")) {
			sql.append(" and c.name=:stuName ");
		}
		if (condition.containsKey("branchSchool")) {
			sql.append(" and b.branchschoolid=:branchSchool ");
		}
		if (condition.containsKey("major")) {
			sql.append(" and b.majorid=:major ");
		}
		if (condition.containsKey("classic")) {
			sql.append(" and b.classicid=:classic ");
		}
		if (condition.containsKey("stuStatus")) {
			sql.append(" and b.studentstatus=:stuStatus ");
		}
		if (condition.containsKey("stuChange")) {
			sql.append(" and a.changetype=:stuChange ");
		}
		if (condition.containsKey("gradeid")) {
			sql.append(" and b.gradeid=:gradeid ");
		}
		if (condition.containsKey("learningStyle")) {
			sql.append(" and b.learningStyle=:learningStyle ");
		}
		if (condition.containsKey("stuNum")) {
			sql.append(" and b.studyno=:stuNum ");
		}
		//面向正常注册和正常未注册
		if (condition.containsKey("accountStatus")) {
			sql.append(" and b.accoutstatus=:accountStatus ");
		}
	
		if (condition.containsKey("applicationDateb")) {
			sql.append(" and a.applicationdate >= to_date('"+condition.get("applicationDateb").toString()+"','yyyy-MM-dd') ");
		}
		if (condition.containsKey("applicationDatee")) {
			sql.append(" and a.applicationdate <= to_date('"+condition.get("applicationDatee").toString()+"','yyyy-MM-dd') ");
		}
		if (condition.containsKey("auditDateb")) {
			sql.append(" and a.auditdate >= to_date('"+condition.get("auditDateb").toString()+"','yyyy-MM-dd') ");
		}
		if (condition.containsKey("auditDatee")) {
			sql.append(" and a.auditdate <= to_date('"+condition.get("auditDatee").toString()+"','yyyy-MM-dd') ");
		}
		sql.append("order by b.studyno ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByLearnStyle(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
 		//学习方式
		sql.append("select  c.dictname \"学习方式\",count(0) \"异动条数合计\"");
		sql.append("from edu_roll_stuchangeinfo a,edu_roll_studentinfo b,hnjk_sys_dict c ");
		sql.append("where a.studentid = b.resourceid and c.parentid='829C90102DC24CEBE040007F010022CC' and c.dictvalue=b.learningstyle ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0  and c.isdeleted=0 and a.changetype is not null ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by c.dictname ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}	
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByMajor(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		//按专业
		sql.append("select  c.majorname \"专业\",count(0) \"异动条数合计\" ");
		sql.append("from edu_roll_stuchangeinfo a,edu_roll_studentinfo b,edu_base_major c  ");
		sql.append("where a.studentid = b.resourceid and c.resourceid = b.majorid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0  and c.isdeleted=0 and a.changetype is not null  ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by c.majorname ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByGrade(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		//按年级
		sql.append("select  c.gradename \"年级\",count(0) \"异动条数合计\" ");
		sql.append("from edu_roll_stuchangeinfo a,edu_roll_studentinfo b,edu_base_grade c "); 
		sql.append("where a.studentid = b.resourceid and c.resourceid = b.gradeid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0  and c.isdeleted=0 and a.changetype is not null ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by c.gradename ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByBrSchool(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		// 学习中心
		sql.append("select  c.unitname \"学习中心\",count(0) \"异动条数合计\" ");
		sql.append("from edu_roll_stuchangeinfo a,edu_roll_studentinfo b,hnjk_sys_unit c  ");
		sql.append("where a.studentid = b.resourceid and c.resourceid = b.branchschoolid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0 and c.isdeleted=0 and a.changetype is not null  ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by c.unitname ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByClassic(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		//层次
		sql.append("select  c.classicname \"层次\",count(0) \"异动条数合计\" ");
		sql.append("from edu_roll_stuchangeinfo a,edu_roll_studentinfo b,edu_base_classic c ");
		sql.append("where a.studentid = b.resourceid and c.resourceid = b.classicid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0  and c.isdeleted=0 and a.changetype is not null ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by c.classicname ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByStyNo(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		//学号
		sql.append("select  b.studyno \"学号\",count(0) \"异动条数合计\" ");
		sql.append("from edu_roll_stuchangeinfo a ,edu_roll_studentinfo b ");
		sql.append("where a.studentid = b.resourceid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0 and a.changetype is not null ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by b.studyno ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByName(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		//姓名
		sql.append("select  c.name \"姓名\",count(0) \"异动条数合计\" ");
		sql.append("from edu_roll_stuchangeinfo a ,edu_roll_studentinfo b,edu_base_student c ");
		sql.append("where a.studentid = b.resourceid and b.studentbaseinfoid = c.resourceid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0 and c.isdeleted=0 and a.changetype is not null  ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		if(condition.containsKey("stus")){
			if(!"".equals(condition.get("stus"))){
				sql.append(" and b.resourceid in ("+condition.get("stus")+") ");
			}
		}
		
		sql.append("group by b.studyno,c.name ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//因为学籍异动中有的学籍是有学号没姓名
//		Map<String,Object> map = new HashMap<String, Object>(0);
//		map.put("姓名","有学号没姓名的数目" );
//		map.put("异动条数合计", statStudentChangeNumLostName(condition));
//		resultList.add(map);
		return resultList;
	}
	@Override
	public List<Map<String, Object>> statStudentChangeNumByChangeStyle(
			Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		StringBuffer sql = new StringBuffer();	
		//异动类型
		sql.append("select  c.dictname \"异动类型\",count(0) \"异动条数合计\"");
		sql.append("from edu_roll_stuchangeinfo a,edu_roll_studentinfo b,hnjk_sys_dict c ");
		sql.append("where a.studentid = b.resourceid and c.parentid='829C90102DDC4CEBE040007F010022CC' and c.dictvalue=a.changetype ");
		sql.append(" and a.isdeleted=0 and b.isdeleted = 0 and c.isdeleted=0 and a.changetype is not null ");
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append("group by c.dictname ");
		sql.append("order by count(0) desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/**
	 * 学籍异动中对应的部分学籍没有学生基本信息，且学籍未被删除。
	 * @param condition
	 * @return
	 */
	private Integer statStudentChangeNumLostName(Map<String, Object> condition) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		Integer num = 0;
		StringBuffer sql = new StringBuffer();	
		//学籍异动中有学号没姓名的数目
		sql.append("select count(0) \"数目\" from ");
		sql.append("(select b.studentbaseinfoid ,b.studyno from  edu_roll_stuchangeinfo a, edu_roll_studentinfo b where a.studentid = b.resourceid and a.isdeleted = 0 and b.isdeleted=0 and a.changetype is not null " );
		if(condition.containsKey("auditDate")){
			if(!"".equals(condition.get("auditDate"))){
				sql.append(" and a.auditdate>to_date('"+condition.get("auditDate")+"','yyyy-mm-dd' ) ");
			}
		}
		sql.append(" ) d ");
		sql.append(" left join edu_base_student c on  d.studentbaseinfoid=c.resourceid and c.isdeleted=0 ");
		sql.append(" where c.name is null ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(0<resultList.size()){
			num = Integer.parseInt(resultList.get(0).get("数目").toString());
		}
		return num;
	}
//	public List<Map<String, Object>> findAll() {
//		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
//		StringBuffer sql = new StringBuffer();	
//		//学籍异动中有学号没姓名的数目
//		sql.append("select a.resourceid \"编号\" ,c.name \"姓名\",d.unitname \"学习中心\"");
//		sql.append("from edu_roll_stuchangeinfo a ,edu_roll_studentinfo b,edu_base_student c ,hnjk_sys_unit d ");
//		sql.append("where a.studentid = b.resourceid and b.studentbaseinfoid = c.resourceid  and d.resourceid = b.branchschoolid ");
//		sql.append("and a.isdeleted=0 and b.isdeleted = 0 and c.isdeleted=0 and d.isdeleted =0 and a.changetype is not null ");
//		try {
//			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return resultList;
//	}
	@Override
	public List<String> getAllStudentInfoId() {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
		StringBuffer sql = new StringBuffer();
		sql.append(" select b.resourceid \"id\" from edu_roll_stuchangeinfo a ,edu_roll_studentinfo b ");
		sql.append(" where a.studentid = b.resourceid and a.isdeleted = 0 and b.isdeleted = 0  ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>(0);
		for (Map<String,Object> map : resultList) {
			result.add(map.get("id").toString());
		}
		return result;
	}
	/**
	 * 得到无关联基本信息的学籍信息id 无关学籍异动
	 * @return
	 */
	@Override
	public List<String> getStudentInfoIdWithoutBaseInfo() {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
		StringBuffer sql = new StringBuffer();
		sql.append(" select a.resourceid \"id\" from edu_roll_studentinfo a ");
		sql.append(" where a.studentbaseinfoid is null and a.isdeleted = 0  ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<String>(0);
		for (Map<String,Object> map : resultList) {
			result.add(map.get("id").toString());
		}
		return result;
	}
	@Override
	public List<Map<String,Object>> getStudentInfoStatusWithBaseInfo() {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
 		Integer num = 0;
		StringBuffer sql = new StringBuffer();	
		//学籍异动中有学号没姓名的数目
		sql.append(" select a.studentstatus,b.certnum ");
		sql.append(" from edu_roll_studentinfo a ,edu_base_student b ");
		sql.append(" where a.studentbaseinfoid = b.resourceid ");
		sql.append(" and a.isdeleted=0 and b.isdeleted=0 ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/**
	 * 学籍信息统计 
	 */
	@Override
	public List<Map<String, Object>> statStudentInfo(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		String genderStat = "sum(1) \"total\",sum(decode(basestu.gender,'1',1,0)) \"man\", sum(decode(basestu.gender,'2',1,0)) \"lady\" ";
		String classicStat = " sum(decode(classic.classicname,'本科',1,'专升本',1,0)) \"本科生\"  ,sum(decode(classic.classicname,'专科',1,'高起专',1,0)) \"专科生\" ";
		String teachtypeStat = " sum(decode(info.teachingtype,'face',0,1)) \"纯网\",sum(decode(info.teachingtype,'face',1,0)) \"网成\" ";
		String statusStat = " sum(decode(basestu.politics,'01',1,0)) \"共产党员\",sum(decode(basestu.politics,'03',1,0)) \"共青团员\",sum(decode(basestu.politics,'04',1,'05',1,'06',1,'07',1,'08',1,'09',1,'10',1,'11',1,0)) \"民主党派\",sum(decode(basestu.gaqcode,'',0,1)) \"华侨\",sum(decode(basestu.gaqcode,'',0,1)) \"港澳台\",sum(decode(basestu.nation,'01',0,1)) \"少数民族\",sum(decode(basestu.health,'残疾',1,0)) \"残疾人\" ";
		String stuStatusStat = " sum(decode(info.studentstatus+1000*info.accoutstatus ,'1011',1,0)) \"正常注册\",sum(decode(info.studentstatus+1000*info.accoutstatus ,'11',1,0)) \"正常未注册\",sum(decode(info.studentstatus ,'18',1,0)) \"过期\",sum(decode(info.studentstatus ,'21',1,0)) \"延期\",sum(decode(info.studentstatus ,'17',1,0)) \"自动流失\",sum(decode(info.studentstatus ,'15',1,0)) \"开除学籍\",sum(decode(info.studentstatus ,'13',1,0)) \"退学\" "; 
		StringBuffer statAge = new StringBuffer();
		StringBuffer statAge_group = new StringBuffer();
		if(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))==6){
			
			/*statAge_group.append("case ");
			statAge_group.append("when classic.classicname='本科' and basestu.gender='1' then '本科 男' ");
			statAge_group.append("when classic.classicname='本科' and basestu.gender='2' then '本科 女' ");
			statAge_group.append("when classic.classicname='专科'and basestu.gender='1' then '专科 男' ");
			statAge_group.append("when classic.classicname='专科'and basestu.gender='2' then '专科 女' end   ");*/
			statAge_group.append("case ");
			statAge_group.append("when (classic.classicname='本科' or classic.classicname='专升本') and basestu.gender='1' then '本科 男' ");
			statAge_group.append("when (classic.classicname='本科' or classic.classicname='专升本') and basestu.gender='2' then '本科 女' ");
			statAge_group.append("when (classic.classicname='专科' or classic.classicname='高起专') and basestu.gender='1' then '专科 男' ");
			statAge_group.append("when (classic.classicname='专科' or classic.classicname='高起专') and basestu.gender='2' then '专科 女' end   ");
			statAge.append("sum(decode(substr(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365) -17,0,1),'-',1,0)) \"17岁以下\",  ");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'17',1,0)) \"17岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'18',1,0)) \"18岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'19',1,0)) \"19岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'20',1,0)) \"20岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'21',1,0)) \"21岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'22',1,0)) \"22岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'23',1,0)) \"23岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'24',1,0)) \"24岁\",");
		    statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'25',1,0)) \"25岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'26',1,0)) \"26岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'27',1,0)) \"27岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'28',1,0)) \"28岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'29',1,0)) \"29岁\",");
			statAge.append("sum(decode(floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365 ) ,'30',1,0)) \"30岁\",");
			statAge.append("sum(decode(substr(30-floor((to_date('"+condition.get("current_date")+"','yyyy-mm-dd')- basestu.bornday)/365) ,0,1),'-',1,0)) \"30岁以上\" ");
		}
		sql.append(" select ");
		
		//追加字段
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: sql.append(" grade.gradename,classic.classicname,"+genderStat ); break;
		case 2: sql.append(" unit.unitname,grade.gradename,classic.classicname,major.majorname,"+genderStat+", "+teachtypeStat);  break;
		case 3: sql.append(" grade.gradename,major.majorname,classic.classicname,"+genderStat+", "+teachtypeStat); break;
		case 4: sql.append(" grade.gradename,info.teachingtype,"+genderStat);  break;
		case 5: sql.append(" unit.unitname,grade.gradename,"+genderStat); break;
		case 6: sql.append( statAge_group+"\"统计项目\" ," +statAge );  break;
		case 7: sql.append(" SUBSTR(basestu.residence,0,2) \"homePlace\" , "+classicStat); break;
		case 8: sql.append(" case when classic.classicname='本科' or classic.classicname='专升本' then '本科生' when classic.classicname='专科' or classic.classicname='高起专' then '专科生' end \"classicname\", "+statusStat);  break;
		case 9: sql.append(" unit.unitname,grade.gradename, "+stuStatusStat);break;
		case 10: sql.append(" unit.unitname, "+genderStat);break;
		case 11: sql.append(" unit.unitname,classic.classicname, "+genderStat);break;
		default : break;
		}
		
		
		sql.append(" from edu_roll_studentinfo info,hnjk_sys_unit unit ,edu_base_grade grade,edu_base_classic classic,edu_base_major major,edu_base_student basestu  ");
		sql.append(" where ");
		//联合条件
		sql.append(" info.branchschoolid = unit.resourceid and  ");
		sql.append(" info.gradeid = grade.resourceid and  ");
		sql.append(" info.classicid = classic.resourceid and  ");
		sql.append(" info.majorid = major.resourceid and  ");
		sql.append(" info.studentbaseinfoid = basestu.resourceid ");
		
		//查询条件
		sql.append(" and grade.isdeleted = 0 ");
		sql.append(" and info.isdeleted=0 ");
		sql.append(" and unit.isdeleted=0 ");
		sql.append(" and classic.isdeleted=0 ");
		sql.append(" and major.isdeleted=0 ");
		sql.append(" and basestu.isdeleted=0 ");
		
		if(condition.containsKey("statStudentStatus")) {
			sql.append(" and info.studentstatus = "+condition.get("statStudentStatus"));
		}
		if(condition.containsKey("statStuBranchSchool")) {
			sql.append(" and info.branchschoolid = '"+condition.get("statStuBranchSchool")+"'");
		}
		if(condition.containsKey("statStuGrade")) {
			sql.append(" and info.gradeid = '"+condition.get("statStuGrade")+"'");
		}
		if(condition.containsKey("statStuClassic")) {
			sql.append(" and info.classicid = '"+condition.get("statStuClassic")+"'");
		}
		if(condition.containsKey("statStuMajor")) {
			sql.append(" and info.majorid = '"+condition.get("statStuMajor")+"'");
		}
		if(condition.containsKey("statStuStudyMode")) {
			sql.append(" and info.teachingType = '"+condition.get("statStuStudyMode")+"'");
		}
		if(condition.containsKey("accountStatus")) {
			sql.append(" and info.accoutstatus = '"+condition.get("accountStatus")+"'");
		}
		//追加分组条件
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: sql.append(" group by grade.gradename,classic.classicname "); break;
		case 2: sql.append(" group by grade.gradename,classic.classicname,unit.unitname,major.majorname "); break;
		case 3: sql.append(" group by grade.gradename,classic.classicname,major.majorname "); break;
		case 4: sql.append(" group by grade.gradename,info.teachingtype "); break;
		case 5: sql.append(" group by grade.gradename,unit.unitname "); break;	
		case 6: sql.append(" group by "+ statAge_group +" order by 统计项目  " ); break;
		case 7: sql.append(" group by SUBSTR(basestu.residence,0,2) "); break;
		case 8: sql.append(" group by case when classic.classicname='本科' or classic.classicname='专升本' then '本科生' when classic.classicname='专科' or classic.classicname='高起专' then '专科生' end  "); break;
		case 9: sql.append(" group by unitname,gradename ");break;
		case 10: sql.append(" group by unitname ");break;
		case 11: sql.append(" group by unitname,classicname ");break;
		default : break;
		}
		//追加部分排序
		switch(Integer.valueOf(ExStringUtils.trimToEmpty(condition.get("statType").toString()))){
		case 1: 
		case 2: 
		case 3: 
		case 4:
		case 5: sql.append(" order by grade.gradename desc "); break;	
		case 9: sql.append(" order by unitname ,gradename desc ");break;
		case 10: sql.append(" order by unitname  desc ");break;
		case 11: sql.append(" order by unitname ,classicname desc ");break;
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
	/***
	 * 高基表教师外聘统计
	 * 
	 */
	@Override
	public List<Map<String, Object>> countEdumanager(String type){
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		StringBuffer sql = new StringBuffer();
		sql.append("select ter.titleoftechnical type,count(*) coun from EDU_BASE_EDUMANAGER  ter  where ter.isdeleted=0and ter.ORGUNITTYPE='outschool' group by ter.titleoftechnical ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/***
	 * 高基表教师外聘学位统计
	 * 
	 */
	@Override
	public List<Map<String, Object>> countDegreeEdumanager(){
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		StringBuffer sql = new StringBuffer();
		List<Map<String,Object>> ret=new ArrayList<Map<String,Object>>();
	
		//Map<String,Object> edu=new HashMap<String, Object>();
		//Map<String,Object> count=new HashMap<String, Object>();
		
		sql.append(" select l.Educationallevel	xl,l.degree xw,count(l.degree) coun from EDU_BASE_EDUMANAGER l where l.isdeleted=0  and l.ORGUNITTYPE='outschool'  group by l.educationallevel,l.degree ,l.titleoftechnical ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.add(foreach("外聘老师",resultList));
		sql.replace(0, sql.length(), "");
		
		sql.append("select l.Educationallevel xl,l.degree xw ,count(l.degree) coun from EDU_BASE_EDUMANAGER l where l.isdeleted=0  and l.gender=2  group by l.educationallevel,l.degree ,l.titleoftechnical");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.add(foreach("其中：女	",resultList));
		sql.replace(0, sql.length(), "");
		sql.append("select l.Educationallevel xl,l.degree xw,count(l.degree) coun from EDU_BASE_EDUMANAGER l where l.isdeleted=0  and not regexp_like(l.certnum,'\\d{15}|\\d{18}')  group by l.educationallevel,l.degree ,l.titleoftechnical");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.add(foreach("外籍教师",resultList));
		
		sql.replace(0, sql.length(), "");
		sql.append("select l.Educationallevel	xl,l.degree xw,count(l.degree) coun from EDU_BASE_EDUMANAGER l where l.isdeleted=0  and l.isotherteacher='1'  group by l.educationallevel,l.degree ,l.titleoftechnical ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ret.add(foreach("其它高校教师",resultList));
		sql.replace(0, sql.length(), "");
		sql.append("select ter.titleoftechnical type,ter.Educationallevel	xl,ter.degree xw,count(*) coun   from EDU_BASE_EDUMANAGER  ter  where ter.isdeleted=0   group by ter.titleoftechnical ,ter.Educationallevel,ter.degree");
		List<Map<String,Object>> countnum =new ArrayList<Map<String,Object>>();
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
			countnum=this.baseJdbcTemplate.findForListMap("select ter.titleoftechnical type,count(*) from EDU_BASE_EDUMANAGER  ter  where ter.isdeleted=0   group by ter.titleoftechnical", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (Map<String, Object> map : countnum) {
			List<Map<String,Object>> tempret = new ArrayList<Map<String,Object>>();
			String ty=map.get("type")==null?"":map.get("type").toString();
			for (Map<String, Object> m : resultList) {
				if(ty.equals(m.get("type")==null?"":m.get("type").toString())){
					tempret.add(m);
				}
			}
			ty=JstlCustomFunction.dictionaryCode2Value("CodeTitleOfTechnicalCode",ty);
			
			ret.add(foreach("".equals(ty)?"未定级":ty,tempret));
		}
		return ret;
	}
	
	public Map<String, Object> foreach(String type,List<Map<String,Object>> resultList){
		Map<String,Object> waipin=new HashMap<String, Object>();
		int b_b =0;  //学历博士，获得博士学位的
		int b_s =0;  //学历博士，获得硕士学位的
		int b_q =0;  //学历博士，总人数
		
		int s_b =0;  //学历硕士，获得博士学位的
		int s_s =0;  //学历硕士，获得硕士学位的
		int s_q =0;  //学历硕士，总人数
		
		int bk_b =0;  //学历本科，获得博士学位的
		int bk_s =0;  //学历本科，获得硕士学位的
		int bk_q =0;  //学历本科，总人数
		
		int q_b =0;  //学历专科及以下，获得博士学位的
		int q_s =0;  //学历专科及以下，获得硕士学位的
		int q_q =0;  //学历专科及以下，总人数
		for (Map<String, Object> map : resultList) {
			String xl=map.get("xl")!=null?map.get("xl").toString():"";
			String xw=map.get("xw")!=null?map.get("xw").toString():"";
			int co=map.get("coun")!=null?Integer.valueOf(String.valueOf(map.get("coun"))).intValue():0;
			if(xl.length()>0){
				if("01".equals(xl)){
					b_q+=co;
					if("200".equals(xw)){
						b_b+=co;
					}else if("300".equals(xw)){
						b_s+=co;
					}
				}else if("11".equals(xl)){
					s_q+=co;
					if("200".equals(xw)){
						s_b+=co;
					}else if("300".equals(xw)){
						s_s+=co;
					}
				}else if(("20".equals(xl))){
					bk_q+=co;
					if("200".equals(xw)){
						bk_b+=co;
					}else if("300".equals(xw)){
						bk_s+=co;
					}
				}else{
					q_q+=co;
					if("200".equals(xw)){
						q_b+=co;
					}else if("300".equals(xw)){
						q_s+=co;
					}
				}
			}
		}
		waipin.put("type",type);
		waipin.put("b_b", b_b);
		waipin.put("b_s", b_s);
		waipin.put("b_q", b_q);
		waipin.put("s_b", s_b);
		waipin.put("s_s", s_s);
		waipin.put("s_q", s_q);
		waipin.put("bk_b", bk_b);
		waipin.put("bk_s", bk_s);
		waipin.put("bk_q", bk_q);
		waipin.put("q_b", q_b);
		waipin.put("q_s", q_s);
		waipin.put("q_q", q_q);
		waipin.put("c_q",b_q+s_q+bk_q+q_q);
		waipin.put("c_s",b_s+s_s+bk_s+q_s);
		waipin.put("c_b",bk_b+s_b+b_b+q_b);
		
		return waipin;
	}
	
	/***
	 * 高基表教师外聘统计“女”人数
	 * 
	 */
	@Override
	public List<Map<String, Object>> countwomanEdumanager(String type){
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		StringBuffer sql = new StringBuffer();
		sql.append("select   e.gender g,count(e.gender) coun from EDU_BASE_EDUMANAGER   e where e.gender=2  and e.isdeleted=0 and e.ORGUNITTYPE='outschool'  group by e.gender");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	
	/**
	 * 学籍信息维护日志查询
	 */
	@Override
	public List<Map<String, Object>> getStudentInfoChangeHistory(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		//追加字段
		
		sql.append(" info.studentname \"修改学生姓名\" ,info.studyno     \"修改学生学号\",bstudent.certnum \"修改学生身份证号\"," );
		sql.append(" case when history.entiryname='StudentBaseInfo' then '学籍基本信息' when history.entiryname='StudentInfo' then '学籍信息' end \"修改实体类型\" ,"); 
		sql.append(" history.beforevalue \"修改前的内容\",history.aftervalue  \"修改后的内容\",history.operatorman \"修改人姓名\" ,");
		sql.append(" unit.unitname \"所属单位\",history.operatorip \"使用ip\",history.operatortime \"修改时间\"");
		sql.append(" ,grade.gradename \"年级\" ,major.majorname \"专业\" , classic.classicname \"层次\" ,class.classesname \"班级\" ,unit2.unitname \"办学单位\" ");
		
		sql.append(" from hnjk_sys_history history ,hnjk_sys_users users ,hnjk_sys_unit unit,edu_roll_studentinfo info ,edu_base_student bstudent  ");
		sql.append(" ,edu_base_grade grade ,edu_base_major major , edu_base_classic classic , hnjk_sys_unit unit2,edu_roll_classes class ");
		//联合条件
		sql.append(" where history.isdeleted=0 ");
		sql.append(" and history.operatormanid = users.resourceid ");
		sql.append(" and users.unitid = unit.resourceid ");
		sql.append(" and (info.resourceid = history.entiryid or bstudent.resourceid = history.entiryid)");
		sql.append(" and info.studentbaseinfoid = bstudent.resourceid ");
		sql.append(" and history.historytype='UPDATE' ");
		sql.append(" and info.gradeid = grade.resourceid and info.majorid = major.resourceid and info.classicid = classic.resourceid and info.branchschoolid = unit2.resourceid and info.classesid= class.resourceid ");
		//查询条件
		
		if(condition.containsKey("studyno")) {
			sql.append(" and info.studyno = '"+condition.get("studyno")+"'");
		}
		if(condition.containsKey("certnum")) {
			sql.append(" and bstudent.certnum = '"+condition.get("certnum")+"'");
		}
		if(condition.containsKey("studentname")) {
			sql.append(" and info.studentname = '"+condition.get("studentname")+"'");
		}
		if(condition.containsKey("studentstatus")) {
			sql.append(" and info.studentstatus = '"+condition.get("studentstatus")+"'");
		}
		if(condition.containsKey("gradeid")) {
			sql.append(" and info.gradeid = '"+condition.get("gradeid")+"'");
		}
		if(condition.containsKey("operatedateb")) {
			sql.append(" and history.operatortime >= to_date('"+condition.get("operatedateb")+"','yyyy-MM-dd') ");
		}
		if(condition.containsKey("operatedatee")) {
			sql.append(" and history.operatortime <= to_date('"+condition.get("operatedatee")+"','yyyy-MM-dd') ");
		}
		sql.append(" order by history.operatortime desc ");
		
		
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 毕业审核学籍信息
	 */
	@Override
	public List<Map<String, Object>> getStudentInfoForGraduateAudit(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select info.studentstatus,info.studyno, info.resourceid ,info.studentname,info.finishedcredithour,info.finishednecesscredithour,info.finishedoptionalcoursenum, ");
		sql.append(" info.isapplygraduate,info.enterauditstatus,info.studentstatus,info.teachplanid,tplan.optionalcoursenum,tplan.minresult,decode(info.accoutstatus,'0','停用','启用') \"account\",info.studentstatus, "); 
		sql.append(" (select sum(t.credithour) from edu_teach_plancourse t where t.planid=tplan.resourceid and t.isdeleted = '0' and (t.coursetype='11' or t.coursetype='thesis')) \"tpmust\" , ");
		sql.append(" tplan.eduyear,grade.term,yearinfo.firstyear,tplan.theGraduationScore theGraduationScore  ");
		sql.append(" ,info.hasPracticeMaterials,info.enterAuditStatus,bstu.photoPath");
		if(condition.containsKey("stuStatus")&& "24".equals(condition.get("stuStatus"))){
			sql.append(" ,gd.isallowsecgraduate ");
		}
		sql.append(" from edu_roll_studentinfo info ");
		sql.append(" join edu_base_grade grade on info.gradeid = grade.resourceid ");
		sql.append(" join edu_teach_plan tplan on info.teachplanid = tplan.resourceid");
		sql.append(" join edu_base_year yearinfo on grade.yearid = yearinfo.resourceid ");
		sql.append(" join edu_base_student bstu on bstu.resourceid=info.studentBaseInfoid");
		if(condition.containsKey("stuStatus")&& "24".equals(condition.get("stuStatus"))){
			sql.append(" ,edu_teach_graduateData gd ");//结业状态，但isAllowSecGraduate =Y 的学生可以做结业换毕业的审核  20160722
		}
		sql.append(" where  info.isdeleted=0 ");
		if(condition.containsKey("stuStatus")&& "24".equals(condition.get("stuStatus"))){
			sql.append( " and gd.studentid = info.resourceid and gd.isdeleted = 0 ");
		}
		if(condition.containsKey("branchSchool")) {
			sql.append(" and info.branchschoolid = '"+condition.get("branchSchool")+"'");
		}
		if(condition.containsKey("major")) {
			sql.append(" and info.majorid = '"+condition.get("major")+"'");
		}
		if(condition.containsKey("classic")) {
			sql.append(" and info.classicid = '"+condition.get("classic")+"'");
		}
		if(condition.containsKey("stuStatus")) {
			sql.append(" and info.studentstatus = '"+condition.get("stuStatus")+"'");
			if("24".equals(condition.get("stuStatus"))){
				sql.append(" and gd.isAllowSecGraduate = 'Y' ");
			}
		}
		if(condition.containsKey("name")) {
			sql.append(" and info.studentname like '%"+condition.get("name")+"%'");
		}
		if(condition.containsKey("matriculateNoticeNo")) {
			sql.append(" and info.studyno like '%"+condition.get("matriculateNoticeNo")+"%'");
		}
		if(condition.containsKey("grade")) {
			sql.append(" and info.gradeid = '"+condition.get("grade")+"'");
		}
		if(condition.containsKey("stus")){
			sql.append(" and info.resourceid in ("+condition.get("stus")+")");
		}
//		if(condition.containsKey("isGraduateQualifer")){
//			//只有在学和延期的才能毕业审核
//			sql.append(" and (info.studentstatus = '11' or info.studentstatus = '24')");
//			
//		}
		//拷贝自下面的方法
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
			sql.append(" and accoutstatus = "+condition.get("accoutstatus")+" ");
		}
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}

	/**
	 * 毕业审核学籍信息
	 */
	@Override
	public List<Map<String, Object>> getNumForGraduateAudit(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select count(0) \"total\" ");
		sql.append(" from edu_roll_studentinfo info ,edu_teach_plan tplan,edu_base_grade grade,edu_base_year yearinfo ");
		sql.append(" where  info.isdeleted=0 and info.studentbaseinfoid is not null  "+
					" and info.teachplanid=tplan.resourceid " +  
					" and grade.resourceid = info.gradeid " +
					" and grade.yearid = yearinfo.resourceid " +
					" and info.teachplanid is not null  and (info.studentstatus = '11' or info.studentstatus = '21' or info.studentstatus = '25') ");
		//查询条件
		if(condition.containsKey("branchSchool")) {
			sql.append(" and info.branchschoolid = '"+condition.get("branchSchool")+"'");
		}
		if(condition.containsKey("major")) {
			sql.append(" and info.majorid = '"+condition.get("major")+"'");
		}
		if(condition.containsKey("classic")) {
			sql.append(" and info.classicid = '"+condition.get("classic")+"'");
		}
		if(condition.containsKey("stuStatus")) {
			sql.append(" and info.studentstatus = '"+condition.get("stuStatus")+"'");
		}
		if(condition.containsKey("name")) {
			sql.append(" and info.studentname like '%"+condition.get("name")+"%'");
		}
		if(condition.containsKey("matriculateNoticeNo")) {
			sql.append(" and info.studyno like '%"+condition.get("matriculateNoticeNo")+"%'");
		}
		if(condition.containsKey("grade")) {
			sql.append(" and info.gradeid = '"+condition.get("grade")+"'");
		}
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
			sql.append(" and accoutstatus = "+condition.get("accoutstatus")+" ");
		}
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	/**
	 * 学位审核学籍信息
	 */
	@Override
	public List<Map<String, Object>> getStudentInfoForDegreeAudit(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select info.studyno, info.resourceid  ,info.studentname ,info.teachplanid,classic.classiccode ,graduatedata.degreestarus \"degreeStatus\" ");
		sql.append(" from edu_roll_studentinfo info , edu_base_grade grade, edu_teach_plan tplan,edu_base_year yearinfo,edu_base_classic classic ,edu_teach_graduatedata graduatedata ");
		sql.append(" where  info.isdeleted=0 and info.studentbaseinfoid is not null  and ");
		sql.append(" info.teachplanid = tplan.resourceid ");
		sql.append(" and info.gradeid = grade.resourceid ");
		sql.append(" and grade.yearid = yearinfo.resourceid ");
		sql.append(" and info.classicid = classic.resourceid ");
		sql.append(" and info.resourceid = graduatedata.studentid ");
		if(condition.containsKey("biye")){
			sql.append(" and info.studentStatus='16' ");//毕业
		}
		if(condition.containsKey("benke")){
			sql.append(" and classic.classiccode='5' ");//本科
		}
		//因为待审核时，对于已经审核通过的就不进行审核了,但由于在审核结束后还用到了这个方法得到学位审核数据,这个时候就不该有这个条件.
		if(!condition.containsKey("forDegreeAuditResult")){
			sql.append(" and (graduatedata.degreestarus is null or graduatedata.degreestarus!='Y' ) ") ;
		}
		//查询条件
		if(condition.containsKey("branchSchool")) {
			sql.append(" and info.branchschoolid = '"+condition.get("branchSchool")+"'");
		}
		if(condition.containsKey("major")) {
			sql.append(" and info.majorid = '"+condition.get("major")+"'");
		}
		if(condition.containsKey("classic")) {
			sql.append(" and info.classicid = '"+condition.get("classic")+"'");
		}
		if(condition.containsKey("name")) {
			sql.append(" and info.studentname like '"+condition.get("name")+"%'");
		}
		if(condition.containsKey("matriculateNoticeNo")) {
			sql.append(" and info.studyno like '"+condition.get("matriculateNoticeNo")+"'");
		}
		if(condition.containsKey("grade")) {
			sql.append(" and info.gradeid = '"+condition.get("grade")+"'");
		}
		if(condition.containsKey("stus")){
			sql.append(" and graduatedata.resourceid in ("+condition.get("stus")+")");
		}
		if(condition.containsKey("studentId")){
			sql.append(" and graduatedata.studentid ="+condition.get("studentId"));
		}
		if(condition.containsKey("degreeEnglish")){
			if ("Y".equals(condition.get("degreeEnglish"))) {
				sql.append(" and exists (select sr.resourceid from EDU_TEACH_STATEXAMRESULTS sr where sr.isDeleted = 0 and sr.isIdented = 'Y' and sr.STUDENTID=info.resourceid)");
			} else if ("N".equals(condition.get("degreeEnglish"))) {
				sql.append(" and not exists (select sr.resourceid from EDU_TEACH_STATEXAMRESULTS sr where sr.isDeleted = 0 and sr.isIdented = 'Y' and sr.STUDENTID=info.resourceid)");
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
	/**
	 * 学位审核成绩课程信息
	 */
	@Override
	public List<Map<String, Object>> getInfoForDegreeAudit(
			Map<String, Object> condition) {
		StringBuffer sql = new StringBuffer();
		sql.append(" select info.resourceid, plc.coursenature,plc.coursetype,plc.ismaincourse,plc.courseid ");
		sql.append(" from edu_teach_plancourse plc ,edu_roll_studentinfo info,edu_teach_plan pl,edu_teach_graduatedata gra ,edu_base_classic classic");
		sql.append(" where info.isdeleted=0 and info.studentbaseinfoid is not null  and info.teachplanid = pl.resourceid ");
		sql.append(" and pl.resourceid = plc.planid and gra.studentid=info.resourceid "); 
		sql.append(" and info.classicid=classic.resourceid ");
		sql.append(" and classic.classiccode!='3' ");
		
	
		//查询条件
		if(condition.containsKey("branchSchool")) {
			sql.append(" and info.branchschoolid = '"+condition.get("branchSchool")+"'");
		}
		if(condition.containsKey("major")) {
			sql.append(" and info.majorid = '"+condition.get("major")+"'");
		}
		if(condition.containsKey("classic")) {
			sql.append(" and info.classicid = '"+condition.get("classic")+"'");
		}
		if(condition.containsKey("name")) {
			sql.append(" and info.studentname like '"+condition.get("name")+"%'");
		}
		if(condition.containsKey("matriculateNoticeNo")) {
			sql.append(" and info.studyno like '"+condition.get("matriculateNoticeNo")+"'");
		}
		if(condition.containsKey("grade")) {
			sql.append(" and info.gradeid = '"+condition.get("grade")+"'");
		}
		if(condition.containsKey("stus")){
			sql.append(" and gra.resourceid in ("+condition.get("stus")+")");
		}
		if(condition.containsKey("studentId")){
			sql.append(" and gra.studentid ="+condition.get("studentId"));
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
	public List getStudentInfoToExport(Map<String,Object> condition) throws WebException{
		
		StringBuffer sql = new StringBuffer();	
		Map<String,Object> values = new HashMap<String, Object>(0);
		if(condition.containsKey("isXxWeb")){ //导出学信网
			sql.append(" select info.enrolleeCode KSH,t.studyno XH,t.studentname XM,b.gender XB,b.bornday CSRQ, ");
			sql.append(" b.certtype LX,b.certnum HM,b.politics ZZMM,b.nation MZ,m.majorcode ZYDM,m.majorname ZYMC, ");
			sql.append(" u.unitname FY,c.classicname CC,t.teachingType XXXS,tp.eduYear XZ,cls.CLASSESNAME BH,t.INDATE RXRQ ");
		}else{	
			sql.append(" select t.enrolleeCode as examNo,cls.classesname as classes, t.studentname ,t.studyno ,b.gender,b.certnum ,b.nation,g.gradename,nvl(t.indate,g.indate) indate ,c.classicname ,m.majorname  ,info.enrolleecode  ");
//			sql.append(" ,u.unitname ,t.studentstatus ,t.accoutstatus ,nvl(aa.num1,0) num1 ,nvl(bb.num2,0) num2, nvl(t.rollcardstatus,'N') rollcardstatus ");
			sql.append(" ,u.unitname ,t.studentstatus ,t.accoutstatus ,nvl(aa.num1,0) num1 ,nvl(bb.num2,0) num2, nvl(t.rollcardstatus,'0') rollcardstatus ");
			sql.append(" ,t.enrolleecode,to_char(b.bornday,'yyyy-MM-dd') bornday,tp.eduyear,t.finishednecesscredithour,t.finishedcredithour,decode(g.term,'1',to_char(y.firstmondayoffirstterm,'yyyy-MM-dd'),'2',to_char(y.firstmondayofsecondterm,'yyyy-MM-dd')) entrancedate " +
					",b.politics,b.marriage,b.certtype,b.workstatus,t.ATTENDADVANCEDSTUDIES,t.teachingType,t.STUDYINSCHOOL,t.STUDENTKIND,t.ENTERSCHOOL,t.LEARINGSTATUS,t.EXAMCERTIFICATENO,t.TOTALPOINT" +
					" ,b.contactaddress ,b.contactzipcode ,b.contactphone  "+
					" ,b.mobile ,b.email ,b.homeaddress ,b.homezipcode ,b.homephone , "+
					" b.officename ,b.officezipcode ,b.officephone,cls.CLASSESNAME BH ,y.firstYear+to_number(tp.eduyear) preGraduateYear,g.graduateDate defaultGD");			
		}
		sql.append(" from edu_roll_studentinfo t inner join edu_base_grade g on g.resourceid = t.gradeid" +
				" inner join edu_base_classic c on c.resourceid = t.classicid " +
				" inner join edu_base_major m on m.resourceid = t.majorid " +
				" inner join hnjk_sys_unit u on u.resourceid= t.branchschoolid " +
		        " inner join edu_base_student b on t.studentbaseinfoid = b.resourceid ");
		sql.append(" left join ( select resu.studentid,sum(1) num1 from edu_base_studentresume resu where resu.isdeleted = 0 group by resu.studentid ) aa on aa.studentid = b.resourceid ");
		sql.append(" left join ( select person.studentbaseinfoid,sum(1) num2 from edu_base_personanralation person where person.isdeleted = 0 group by person.studentbaseinfoid ) bb on bb.studentbaseinfoid = b.resourceid ");
		sql.append(" left join edu_base_year y on y.resourceid = g.yearid  ");
		sql.append(" left join edu_teach_plan tp on tp.resourceid = t.teachplanid ");
		sql.append(" left join edu_recruit_enrolleeinfo info on info.studentbaseinfoid = t.studentbaseinfoid ");
		
		sql.append(" left join EDU_ROLL_CLASSES cls on cls.resourceid = t.CLASSESID ");
		sql.append(" where t.isdeleted=0 ");
		//查找时间范围类的异动前学籍状态
		if(condition.get("endDate")!=null && ExDateUtils.checkDateString(condition.get("endDate").toString())){//截止日期
			sql.append(" and t.resourceid in(select stuchange.studentid from(");
			sql.append(" select si.resourceid studentid,sci.changetype,sci.auditdate,nvl(sci.changebeforestudentstatus,si.studentstatus) studentstatus,");
			sql.append(" nvl2(sci.resourceid,row_number() over(partition by sci.studentid order by sci.auditdate),1) rn from edu_roll_studentinfo si");
			sql.append(" left join edu_roll_stuchangeinfo sci on sci.studentid=si.resourceid and sci.isdeleted=0 and sci.finalauditstatus='Y' and sci.changebeforestudentstatus is not null");
			sql.append(" and sci.auditdate>to_date('"+condition.get("endDate")+"','yyyy-mm-dd')");
			sql.append(" order by sci.studentid desc,sci.auditdate) stuchange where stuchange.rn=1");
			if(condition.containsKey("stuStatus")){//学籍状态
				sql.append(" and stuchange.studentstatus = :stuStatus");
				values.put("stuStatus", condition.get("stuStatus"));
			}
			sql.append(")");
		}/*else {
			//20171026
			//sql.append(" and not exists(SELECT *from EDU_ROLL_STUCHANGEINFO st1 where st1.isdeleted=0 and  st1.studentid = t.resourceid and finalAuditStatus = 'N')");
		}*/
		if(condition.containsKey("stuStatus")){//学籍状态
			sql.append(" and  t.studentstatus = :stuStatus");
			values.put("stuStatus", condition.get("stuStatus"));
		}else {
			sql.append(" and t.studentStatus not in('16','24') ");//过滤【毕业】和【结业】
		}
		if(condition.containsKey("studentstatusall21")){//延期生
			sql.append(" and ( 1=1 ");
		}
		/*if(condition.containsKey("excludeStuStatus")){//排除的学籍状态 16-毕业
			sql.append(" and  t.studentstatus <> :excludeStuStatus");
			values.put("excludeStuStatus", condition.get("excludeStuStatus"));
		}*/
		if(condition.containsKey("includeStuStatus")){//包含的学籍状态
			sql.append(" and  t.studentstatus in ("+condition.get("includeStuStatus")+")");
		}

	 	if(condition.containsKey("branchSchool")){//学习中心
	 		sql.append(" and   t.branchschoolid = :branchSchool");		
	 		values.put("branchSchool", condition.get("branchSchool"));
	 	}
	 	if(condition.containsKey("stuGrade")){//年级
	 		sql.append(" and   t.gradeid = :stuGrade");
	 		values.put("stuGrade", condition.get("stuGrade"));
	 	}
	 		
	 	if(condition.containsKey("classic")){//层次
	 		sql.append(" and  t.classicid = :classic");
	 		values.put("classic", condition.get("classic"));
	 	}
	 	if(condition.containsKey("teachingType")){//学习形式
	 		sql.append(" and  t.teachingType = :teachingType");
	 		values.put("teachingType", condition.get("teachingType"));
	 	}
	 	if(condition.containsKey("classesids")){//班级id
	 		sql.append(" and  t.CLASSESID in ("+ExStringUtils.addSymbol(condition.get("classesids").toString().split(","), "'", "'")+")");
	 	}
	 	if(condition.containsKey("stuClasses")){//班级
	 		sql.append(" and  t.CLASSESID = :stuClasses");
	 		values.put("stuClasses", condition.get("stuClasses"));
	 	}
	 	if(condition.containsKey("classname")){//班级名称
	 		sql.append(" and  cls.classname like :classname");
	 		values.put("classname", "%"+condition.get("classname")+"%");
	 	}
	 	if(condition.containsKey("major")){//专业
	 		sql.append(" and  t.majorid = :major");
	 		values.put("major", condition.get("major"));
	 	}
	 	if (condition.containsKey("entranceFlag")) {
	 		sql.append(" and t.enterauditstatus = :entranceFlag ");
			values.put("entranceFlag",condition.get("entranceFlag"));
		}	
		if(condition.containsKey("accountStatus")){
			sql.append(" and t.accoutstatus = :accountStatus ");
			values.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));
		}
	 	if(condition.containsKey("name")){
	 		sql.append(" and  t.studentname like :name ");
	 		String name = condition.get("name").toString();
	 		values.put("name", "%"+name+"%");
	 	}
	 	if (condition.containsKey("rollCard")) {
	 		sql.append(" and t.rollcardstatus=:rollCard ");
	 		values.put("rollCard", condition.get("rollCard"));
		}
	 /*	if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
	 		sql.append(" and nvl(aa.num1,0) > 0 ");
			sql.append(" and nvl(bb.num2,0) > 0 ");
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
			sql.append(" and nvl(aa.num1,0) = 0 ");
			sql.append(" and nvl(bb.num2,0) = 0 ");
		}*/
	 	if (condition.containsKey("teachPlan")){
			if(Constants.BOOLEAN_YES.equals(condition.get("teachPlan").toString())){
				sql.append(" and t.teachplanid is not null ");
			}else if (Constants.BOOLEAN_NO.equals(condition.get("teachPlan").toString())){
				sql.append(" and t.teachplanid is null ");
			}
		}
	 	if(condition.containsKey("certNum")){
	 		sql.append(" and b.certnum = :certNum ");
	 		values.put("certNum",condition.get("certNum"));
	 	}
	 	if(condition.containsKey("studyNo")){
	 		sql.append(" and t.studyno = :studyNo ");
	 		values.put("studyNo",condition.get("studyNo"));
	 	}
	 	//这个更上面的那个 teachplan 的条件重复了，有时间合并掉
	 	if(condition.containsKey("teachingPlanNotNull")){
			sql.append("  and t.teachplanid is not null ");
		}
	 	//毕业前数据导出 需要按层次分年级范围 一般地，这种查询数据量大 hql查询慢 以致会话超时 报 internal error
	 	String stuGradeGZ="";
	 	String stuGradeGB="";
	 	String stuGradeZB="";
	 	String classicAndGradeCondition="";
	 	if(condition.containsKey("stuGradeGZ")){
	 		stuGradeGZ=" c.classiccode = '3' and g.resourceid in ("+condition.get("stuGradeGZ")+")";
	 	}
	 	if(condition.containsKey("stuGradeGB")){
	 		stuGradeGB=" c.classiccode = '1' and g.resourceid in ("+condition.get("stuGradeGB")+")";
	 	}
		if(condition.containsKey("stuGradeZB")){
			stuGradeZB=" c.classiccode = '2' and g.resourceid in ("+condition.get("stuGradeZB")+")";
		}
		if(ExStringUtils.isNotEmpty(stuGradeZB)||ExStringUtils.isNotEmpty(stuGradeGB)||ExStringUtils.isNotEmpty(stuGradeGZ)){
			classicAndGradeCondition += "and ("+stuGradeGZ;
			if(ExStringUtils.isNotEmpty(stuGradeGZ)&&ExStringUtils.isNotEmpty(stuGradeGB)){
				classicAndGradeCondition += " or ";
			}
			classicAndGradeCondition += stuGradeGB;

			if((ExStringUtils.isNotEmpty(stuGradeGZ)||ExStringUtils.isNotEmpty(stuGradeGB))&&ExStringUtils.isNotEmpty(stuGradeZB)){
				classicAndGradeCondition += " or ";
			}
			classicAndGradeCondition +=stuGradeZB +" ) " ;
		}
		sql.append(classicAndGradeCondition);
		
		if(condition.containsKey("studentstatusall21")){//所有年级的延期生
			sql.append("  or t.studentstatus = '21'  ");
			if(condition.containsKey("branchSchool")){//学习中心
		 		sql.append(" and   t.branchschoolid = :branchSchool");		
		 		values.put("branchSchool", condition.get("branchSchool"));
		 	}
			sql.append("   ) ");
		}
		if(condition.containsKey("classesid")){
	 		sql.append(" and cls.resourceid = :classesid ");
	 		values.put("classesid", condition.get("classesid"));
	 	}
		if(condition.containsKey("notClasses")){
			sql.append(" and cls.resourceid is null ");
		}
		if(condition.containsKey("studentIds")){
			
			sql.append(" and t.resourceid in ("+condition.get("studentIds").toString()+")");
		}
		if(condition.containsKey("classesMasterId")){
			sql.append(" and cls.classesMasterId=:classesMasterId ");
			values.put("classesMasterId", condition.get("classesMasterId"));
		}
		if(condition.containsKey("addSql")){
			if("Y".equals(condition.get("addSql"))){
				sql.append(" and  t.classicid is not null");
			}else {
				sql.append(" "+condition.get("addSql").toString());
			}
	 	}
		sql.append(" order by t.studyno,t.classicid,t.gradeid,t.majorid desc ");
	 	List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	@Override
	public Page getStudentInfoStatusChangeHistory(
			Map<String, Object> condition,Page page) throws ServiceException {
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		sql.append("select b.studyno ,c.name  ,d.unitname , e.gradename   ,f.classicname  ,g.majorname  ");
		sql.append(",c.certnum  ,to_char(a.auditdate,'yyyy-MM-dd') auditdate ");             
		sql.append(" ,a.reason, substr(a.memo,instr(a.memo,'从')+1,instr(a.memo,'变动至')-instr(a.memo,'从')-1) beforestatus ");
		sql.append(" ,decode(instr(a.memo,'备注信息:'),0,substr(a.memo,instr(a.memo,'变动至')+3), substr(a.memo,instr(a.memo,'变动至')+3,instr(a.memo,'备注信息:')-instr(a.memo,'变动至')-3)) afterstatus ");
		sql.append(" ,case when instr(a.memo,'备注信息:')>0 then substr(a.memo,instr(a.memo,'备注信息:')+5) end memo ");
		sql.append(" ,a.auditman,a.auditmanid,classes.classesname" +
				",case when instr(a.memo,'【延期至】')>0 then substr(a.memo,instr(a.memo,'【延期至】')+5) end  delaydate ");
		sql.append(" from edu_roll_stuchangeinfo a " +
				" left join edu_roll_studentinfo b on a.studentid = b.resourceid " +
				" left join edu_base_student c on b.studentbaseinfoid = c.resourceid ");
		sql.append(" left join hnjk_sys_unit d on d.resourceid = b.branchschoolid " +
				" left join edu_base_grade e on e.resourceid = b.gradeid " +
				" left join edu_base_classic f on f.resourceid = b.classicid " +
				" left join edu_base_major g on g.resourceid = b.majorid " +
				" left join edu_roll_classes classes on classes.resourceid = b.classesid ");
		sql.append(" where a.isdeleted = 0 and a.changetype is not null ");
		sql.append(" and a.reason like '%批量设置学籍状态！%' ");
		sql.append(" and a.finalauditstatus = 'Y' ");
		
		
		if(condition.containsKey("certnum")){
			sql.append(" and c.certnum = '"+condition.get("certnum")+"'");
		}
		if(condition.containsKey("studentname")){
			sql.append(" and c.name = '"+condition.get("studentname")+"'");
		}
		if(condition.containsKey("studyno")){
			sql.append(" and b.studyno = '"+condition.get("studyno")+"'");
		}
		if(condition.containsKey("studentstatus")){
			sql.append(" and b.studentstatus = '"+condition.get("studentstatus")+"'");
		}
		if(condition.containsKey("gradeid")){
			sql.append(" and b.gradeid = '"+condition.get("gradeid")+"'");
		}
		if(condition.containsKey("operatedateb")){
			sql.append(" and a.auditdate >= to_date('"+condition.get("operatedateb")+"','yyyy-MM-dd')");
		}
		if(condition.containsKey("operatedatee")){
			sql.append(" and a.auditdate <= to_date('"+condition.get("operatedatee")+"','yyyy-MM-dd')");
		}
		
		
		sql.append(" order by a.auditdate desc ");
		try {
			page = this.baseJdbcTemplate.findListMap(page, sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}
	
	@Override
	public List<Map<String,Object>> getStudentInfoStatusChangeHistoryList(
			Map<String, Object> condition) throws ServiceException {
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		sql.append("select b.studyno ,c.name  ,d.unitname , e.gradename   ,f.classicname  ,g.majorname  ");
		sql.append(",c.certnum  ,to_char(a.auditdate,'yyyy-MM-dd') auditdate ");             
		sql.append(" ,a.reason, substr(a.memo,instr(a.memo,'从')+1,instr(a.memo,'变动至')-instr(a.memo,'从')-1) beforestatus ");
		sql.append(" ,decode(instr(a.memo,'备注信息:'),0,substr(a.memo,instr(a.memo,'变动至')+3), substr(a.memo,instr(a.memo,'变动至')+3,instr(a.memo,'备注信息:')-instr(a.memo,'变动至')-3)) afterstatus ");
		sql.append(" ,case when instr(a.memo,'备注信息:')>0 then substr(a.memo,instr(a.memo,'备注信息:')+5) end memo ");
		sql.append(" ,a.auditman,a.auditmanid,classes.classesname" +
				",case when instr(a.memo,'【延期至】')>0 then substr(a.memo,instr(a.memo,'【延期至】')+5) end  delaydate ");
		sql.append(" from edu_roll_stuchangeinfo a " +
				" left join edu_roll_studentinfo b on a.studentid = b.resourceid " +
				" left join edu_base_student c on b.studentbaseinfoid = c.resourceid ");
		sql.append(" left join hnjk_sys_unit d on d.resourceid = b.branchschoolid " +
				" left join edu_base_grade e on e.resourceid = b.gradeid " +
				" left join edu_base_classic f on f.resourceid = b.classicid " +
				" left join edu_base_major g on g.resourceid = b.majorid " +
				" left join edu_roll_classes classes on classes.resourceid = b.classesid ");
		sql.append(" where a.isdeleted = 0 and a.changetype is not null ");
		sql.append(" and a.reason like '%批量设置学籍状态！%' ");
		sql.append(" and a.finalauditstatus = 'Y' ");
		
		
		if(condition.containsKey("certnum")){
			sql.append(" and c.certnum = '"+condition.get("certnum")+"'");
		}
		if(condition.containsKey("studentname")){
			sql.append(" and c.name = '"+condition.get("studentname")+"'");
		}
		if(condition.containsKey("studyno")){
			sql.append(" and b.studyno = '"+condition.get("studyno")+"'");
		}
		if(condition.containsKey("studentstatus")){
			sql.append(" and b.studentstatus = '"+condition.get("studentstatus")+"'");
		}
		if(condition.containsKey("gradeid")){
			sql.append(" and b.gradeid = '"+condition.get("gradeid")+"'");
		}
		if(condition.containsKey("operatedateb")){
			sql.append(" and a.auditdate >= to_date('"+condition.get("operatedateb")+"','yyyy-MM-dd')");
		}
		if(condition.containsKey("operatedatee")){
			sql.append(" and a.auditdate <= to_date('"+condition.get("operatedatee")+"','yyyy-MM-dd')");
		}
		
		
		sql.append(" order by a.auditdate desc ");
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	
	/**
	 * 通过考生号，姓名 身份证 专业获取学籍号
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String,Object>> getStudentInfoWithEnrolleeCodeEct(
			Map<String, Object> condition) throws ServiceException {
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		sql.append("select distinct c.resourceid from edu_recruit_enrolleeinfo a "
		+" left join edu_base_student b on a.studentbaseinfoid = b.resourceid " 
		+" left join edu_roll_studentinfo c on a.branchschoolid = c.branchschoolid "
		+" left join edu_base_major d on c.majorid = d.resourceid "
		+" and b.resourceid = c.studentbaseinfoid  "
		+" and c.studentbaseinfoid = a.studentbaseinfoid "
		+" and a.grade = c.gradeid "
		+" and d.resourceid = c.majorid "
		+" where c.isdeleted = 0  ");
		if(condition.containsKey("certNum")){
			sql.append(" and b.certnum = '"+condition.get("certNum")+"'");
		}
		if(condition.containsKey("name")){
			sql.append(" and c.studentname = '"+condition.get("name")+"'");
		}
		if(condition.containsKey("enrolleeCode")){
			sql.append(" and a.enrolleeCode = '"+condition.get("enrolleeCode")+"'");
		}
		if(condition.containsKey("major")){
			sql.append(" and d.majorname = '"+condition.get("major")+"'");
		}
		try {
			resultList = this.baseJdbcTemplate.findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultList;
	}
	@Override
	public String genMaxStudyNoWithSuffix(String prefix) throws ServiceException{
		String result ="";
		String sql = " select decode( nvl(max(t.studyno),'-'),'-','-',replace(max(t.studyno),'"+prefix+"','') )  maxstudyno "
			+ "from edu_roll_studentinfo t where length(t.studyno)=13 and t.studyno like '"+prefix+"%'";
		try {
			result = String.valueOf(this.baseJdbcTemplate.findForLong(sql, null));
		} catch (Exception e) {
			logger.error("根据前缀获取最大学号出错.",e.fillInStackTrace());
		}
		return result;
	}
	@Override
	public String genMaxStudyNoWithSuffix2(String prefix) throws ServiceException{
		String result ="";
		String sql = " select decode( nvl(max(t.studyno),'-'),'-','-',replace(max(t.studyno),'"+prefix+"','') )  maxstudyno "
			+ "from edu_roll_studentinfo t where length(t.studyno)=12 and t.studyno like '"+prefix+"%'";
		try {
			result = String.valueOf(this.baseJdbcTemplate.findForLong(sql, null));
		} catch (Exception e) {
			logger.error("根据前缀获取最大学号出错.",e.fillInStackTrace());
		}
		return result;
	}
	@Override
	public String genMaxStudyNoWithSuffixGXYKD(String prefix) throws ServiceException{
		String result ="";
//		String sql = " select decode( nvl(max(t.studyno),'-'),'-','-',REGEXP_REPLACE(max(t.studyno),'"+prefix.substring(0, 3)+ "[0-9]{3}" + prefix.substring(6, 8)+"','') )  maxstudyno "
		String sql = " select decode( nvl(max(REGEXP_REPLACE(t.studyno, '"
			+prefix.substring(0, 3)+"[0-9]{3}"+prefix.substring(6, 8)+"', '"+prefix.substring(0, 3)+prefix.substring(6, 8)
			+"')),'-'),'-','-',replace(max(REGEXP_REPLACE(t.studyno, '"+prefix.substring(0, 3)+ "[0-9]{3}" + prefix.substring(6, 8)+"', '"+prefix.substring(0, 3)+prefix.substring(6, 8)
			+"')), '"+prefix.substring(0, 3)+prefix.substring(6, 8)+"', '') )  maxstudyno "
			+ "from edu_roll_studentinfo t where length(t.studyno)=12 and t.studyno like '"+prefix.substring(0, 3)+ "___" + prefix.substring(6, 8)+"%'";
		try {
			result = String.valueOf(this.baseJdbcTemplate.findForLong(sql, null));
		} catch (Exception e) {
			logger.error("根据前缀获取最大学号出错.",e.fillInStackTrace());
		}
		return result;
	}
	@Override
	public String getMaxStudyNoWithSuffixGXYKD(String prefix) throws ServiceException{
		String result ="";
		String sql = " select decode( nvl(max(t.studyno),'-'),'-','-',REGEXP_REPLACE(max(t.studyno),'"+prefix.substring(0, 3)+ "[0-9]{3}" + prefix.substring(6, 8)+"','') )  maxstudyno "
		+ "from edu_roll_studentinfo t where length(t.studyno)=12 and t.studyno like '"+prefix.substring(0, 3)+ "___" + prefix.substring(6, 8)+"%'";
		try {
			result = String.valueOf(this.baseJdbcTemplate.findForLong(sql, null));
		} catch (Exception e) {
			logger.error("根据前缀获取最大学号出错.",e.fillInStackTrace());
		}
		return result;
	}
	@Override
	public String getMaxStudyNoWithSuffix(String prefix) throws ServiceException{
		String result ="";
		String sql = " select decode( nvl(max(t.studyno),'-'),'-','-',replace(max(t.studyno),'"+prefix+"','') )  maxstudyno "
		+ "from edu_roll_studentinfo t where length(t.studyno)=8 and t.studyno like '"+prefix+"%'";
		try {
			result = String.valueOf(this.baseJdbcTemplate.findForLong(sql, null));
		} catch (Exception e) {
			logger.error("根据前缀获取最大学号出错.",e.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 根据前缀获取安徽医当前最大的学号
	 */
	@Override
	public String getMaxStudyNoWithSuffixAHYKD(String prefix)
			throws ServiceException {
		String result ="";
		String sql = " select decode( nvl(max(t.studyno),'-'),'-','-',replace(max(t.studyno),'"+prefix+"','') )  maxstudyno "
		+ "from edu_roll_studentinfo t where length(t.studyno)=10 and t.studyno like '"+prefix+"%'";
		try {
			result = String.valueOf(this.baseJdbcTemplate.findForLong(sql, null));
		} catch (Exception e) {
			logger.error("根据前缀获取最大学号出错.",e.fillInStackTrace());
		}
		return result;
	}
}
