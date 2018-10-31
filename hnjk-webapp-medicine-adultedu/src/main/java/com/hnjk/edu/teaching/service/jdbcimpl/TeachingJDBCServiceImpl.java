package com.hnjk.edu.teaching.service.jdbcimpl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.dialect.function.NvlFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.roll.vo.StudentSignatureVo;
import com.hnjk.edu.teaching.helper.ComparatorExamSeatAssigendForExamInfo;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.INoExamApplyService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.edu.teaching.vo.ExamOrderStatExportVo;
import com.hnjk.edu.teaching.vo.ExamPaperBagPrintInfoVo;
import com.hnjk.edu.teaching.vo.ExamResultsMakeUpVo;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.edu.teaching.vo.ExamSeatAssignExamCourseVo;
import com.hnjk.edu.teaching.vo.NotCourseOrderStuInfoVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.User;
import com.sun.org.apache.xalan.internal.lib.ExsltBase;


@Transactional
@Service("teachingJDBCService")
public class TeachingJDBCServiceImpl extends BaseServiceImpl<TeachingPlanCourse> implements ITeachingJDBCService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examResultsService")
	IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("noexamapplyservice")
	INoExamApplyService noexamapplyservice;
	
	@Autowired
	@Qualifier("examInfoService")
	IExamInfoService examInfoService;
	
	//预约教材统计
	@Override
	public List<Map<String,Object>> statBookOrder(Map<String, Object> condition) {
		
		List<Map<String,Object>> resutList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		
		sql.append("select unit.unitName,classic.CLASSICNAME,major.MAJORNAME,grade.gradename,");
		sql.append("	   course.courseName,book.bookName,book.author,count(orderbook.resourceid)COUNTS");
		sql.append("  from EDU_TEACH_ORDERBOOKSTAT stat,EDU_TEACH_ORDERBOOKS orderbook,EDU_BASE_COURSEBOOK book,EDU_ROLL_STUDENTINFO stu,");
		sql.append("	   EDU_BASE_MAJOR major,hnjk_sys_unit unit,EDU_BASE_CLASSIC classic,EDU_BASE_COURSE course,EDU_BASE_YEAR year,EDU_BASE_GRADE grade ");
		sql.append(" where stat.COURSEBOOKID = book.RESOURCEID");
		sql.append("   and orderbook.ORDERBOOKSTATID = stat.RESOURCEID");
		sql.append("   and book.COURSEID = course.resourceid");
		sql.append("   and orderbook.STUDENTID = stu.resourceid");
		sql.append("   and stu.CLASSICID = classic.resourceid");
		sql.append("   and stu.MAJORID = major.RESOURCEID");
		sql.append("   and stu.BRANCHSCHOOLID = unit.resourceid");
		sql.append("   and stat.YEARID=year.resourceid");
		sql.append("   and stu.GRADEID = grade.resourceid");
		sql.append("   and orderbook.isDeleted=0 ");
		
		if (condition.containsKey("branchSchool")) {
			sql.append(" and unit.resourceid=:branchSchool ");
		}
		if (condition.containsKey("classic")) {
			sql.append(" and classic.resourceid=:classic ");
		}
		if (condition.containsKey("major")) {
			sql.append(" and major.resourceid=:major ");
		}
		if (condition.containsKey("yearInfo")) {
			sql.append(" and year.resourceid=:yearInfo ");
		}
		if (condition.containsKey("term")) {
			sql.append(" and stat.term=:term ");
		}
		if (condition.containsKey("course")) {
			sql.append(" and course.resourceid=:course ");
		}
		
		sql.append("group by unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,grade.gradename,course.courseName,book.bookName,book.author ");
		sql.append("order by unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,grade.gradename,course.courseName ");
		try {
			resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resutList;
	}
	//预约教材统计
	@Override
	public Page statBookOrder(Map<String, Object> condition, Page objPage) throws ServiceException{
		StringBuffer sql = new StringBuffer();	
		
		sql.append("select unit.unitName,");
		sql.append("	   course.courseName,book.bookName,book.author,count(orderbook.resourceid)COUNTS");
		sql.append("  from EDU_TEACH_ORDERBOOKSTAT stat,EDU_TEACH_ORDERBOOKS orderbook,EDU_BASE_COURSEBOOK book,EDU_ROLL_STUDENTINFO stu,");
		sql.append("	   hnjk_sys_unit unit,EDU_BASE_COURSE course,EDU_BASE_YEAR year ");
		sql.append(" where stat.COURSEBOOKID = book.RESOURCEID");
		sql.append("   and orderbook.ORDERBOOKSTATID = stat.RESOURCEID");
		sql.append("   and book.COURSEID = course.resourceid");
		sql.append("   and orderbook.STUDENTID = stu.resourceid");
		//sql.append("   and stu.CLASSICID = classic.resourceid");
		//sql.append("   and stu.MAJORID = major.RESOURCEID");
		sql.append("   and stu.BRANCHSCHOOLID = unit.resourceid");
		sql.append("   and stat.YEARID=year.resourceid");
		//sql.append("   and stu.GRADEID = grade.resourceid");
		sql.append("   and orderbook.isDeleted=0 ");
		
		if (condition.containsKey("branchSchool")) {
			sql.append(" and unit.resourceid=:branchSchool ");
		}
		//if (condition.containsKey("classic"))      sql.append(" and classic.resourceid=:classic ");
		//if (condition.containsKey("major"))        sql.append(" and major.resourceid=:major ");
		if (condition.containsKey("yearInfo")) {
			sql.append(" and year.resourceid=:yearInfo ");
		}
		if (condition.containsKey("term")) {
			sql.append(" and stat.term=:term ");
		}
		if (condition.containsKey("course")) {
			sql.append(" and course.resourceid=:course ");
		}
		if (condition.containsKey("startTime")) {
			sql.append(" and orderbook.orderTime>=to_date(:startTime,'yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append(" and orderbook.orderTime<=to_date(:endTime,'yyyy-MM-dd HH24:mi:ss') ");
		}
		
		//sql.append(" group by unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,grade.gradename,course.courseName,book.bookName,book.author ");
		sql.append(" group by unit.UNITNAME,course.courseName,book.bookName,book.author ");
		
		return findBySql(objPage, sql.toString(), condition);
	}
	@Override
	public Page findBySql(Page page, String sql, Map<String, Object> values) throws ServiceException {
		Assert.notNull(page, "page不能为空");
		StringBuffer sql_page = new StringBuffer(1024);		
		sql_page.append(sql);
		
		if (page.isAutoCount()) {
			Long totalCount = countSqlResult(sql, values);
			page.setTotalCount(totalCount.intValue());
		}
		
		if(page.isOrderBySetted()){
			sql_page.append(" order by ").append(page.getOrderBy()).append(" ").append(page.getOrder()).append(" ");
//			sql += " order by "+page.getOrderBy()+" "+ page.getOrder()+" ";
		}		
		try {
			List resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(" select * from (select r.*,rownum rn from ("+sql_page.toString()+") r where rownum<="+(page.getFirst()+page.getPageSize())+") where rn>"+page.getFirst(), values);
			page.setResult(resutList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}	
	private Long countSqlResult(String sql, Map<String, Object> condition ){
		String countSql = "select count(*) from (" + sql+" ) ";
		try {
			Long count = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(countSql, condition);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("sql can't be auto count, hql is:" + countSql, e);
		}
	}
	//预约课程统计
	@Override
	public Page statCourseOrder(Map<String, Object> condition, Page objPage) throws ServiceException{
		StringBuffer sql = new StringBuffer();	
		sql.append("  select course.resourceid AS COURSEID,unit.resourceid AS UNITID,classic.resourceid AS CLASSICID,major.resourceid AS MAJORID,");
		sql.append("         unit.unitName,classic.CLASSICNAME,major.MAJORNAME,");
		sql.append("         course.COURSENAME,count(ordercourse.resourceid) counts");
		
		sql.append("    from EDU_TEACH_ORDERCOURSE ordercourse ");
		sql.append("    inner join EDU_TEACH_COURSESTAT coursestat  on ordercourse.COURSESTATID = coursestat.RESOURCEID and coursestat.isDeleted = :isDeleted ");
		sql.append("    inner join EDU_BASE_COURSE       course on coursestat.COURSEID = course.resourceid  and course.isDeleted =:isDeleted");
		if (condition.containsKey("course")) {
			sql.append(" and course.resourceid=:course");
		}
		sql.append("    inner join EDU_ROLL_STUDENTINFO  stu on ordercourse.STUDENTID = stu.resourceid  and stu.isDeleted =:isDeleted ");
		sql.append("    inner join EDU_BASE_MAJOR        major on stu.MAJORID = major.RESOURCEID  and major.isDeleted =:isDeleted");
		if (condition.containsKey("major")) {
			sql.append(" and major.resourceid=:major");
		}
		sql.append("    inner join hnjk_sys_unit         unit on stu.BRANCHSCHOOLID = unit.resourceid  and unit.isDeleted =:isDeleted ");
		if (condition.containsKey("branchSchool")) {
			sql.append(" and unit.resourceid=:branchSchool");
		}
		sql.append("    inner join EDU_BASE_CLASSIC      classic on stu.CLASSICID = classic.resourceid  and classic.isDeleted = :isDeleted ");
		if (condition.containsKey("classic")) {
			sql.append(" and classic.resourceid=:classic");
		}
		sql.append("   where ordercourse.isDeleted = 0 ");

		if (condition.containsKey("yearInfo")) {
			sql.append(" and ordercourse.ordercourseyear=:yearInfo");
		}
		if (condition.containsKey("term")) {
			sql.append(" and ordercourse.ordercourseterm=:term");
		}

		if (condition.containsKey("startTime")) {
			sql.append(" and ordercourse.orderCourseTime>=to_date(:startTime,'yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append(" and ordercourse.orderCourseTime<=to_date(:endTime,'yyyy-MM-dd HH24:mi:ss') ");
		}

		sql.append(" group by unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,course.courseName ,course.resourceid,");
		sql.append(" unit.resourceid,classic.resourceid,major.resourceid ");
		
		condition.put("isDeleted",0);
		return findBySql(objPage, sql.toString(), condition);
	}
	
	//预约课程统计
	@Override
	public List<Map<String,Object>> statCourseOrder(Map<String, Object> condition) {
		
		List<Map<String,Object>> resutList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		
		sql.append("  select unit.unitName,classic.CLASSICNAME,major.MAJORNAME,");
		sql.append("         course.COURSENAME,count(ordercourse.resourceid) counts");
		sql.append("    from EDU_TEACH_ORDERCOURSE ordercourse ");
		sql.append("    inner join EDU_TEACH_COURSESTAT coursestat  on ordercourse.COURSESTATID = coursestat.RESOURCEID and coursestat.isDeleted = :isDeleted ");
		sql.append("    inner join EDU_BASE_COURSE       course on coursestat.COURSEID = course.resourceid  and course.isDeleted =:isDeleted");
		if (condition.containsKey("course")) {
			sql.append(" and course.resourceid=:course");
		}
		sql.append("    inner join EDU_ROLL_STUDENTINFO  stu on ordercourse.STUDENTID = stu.resourceid  and stu.isDeleted =:isDeleted ");
		sql.append("    inner join EDU_BASE_MAJOR        major on stu.MAJORID = major.RESOURCEID  and major.isDeleted =:isDeleted");
		if (condition.containsKey("major")) {
			sql.append(" and major.resourceid=:major");
		}
		sql.append("    inner join hnjk_sys_unit         unit on stu.BRANCHSCHOOLID = unit.resourceid  and unit.isDeleted =:isDeleted ");
		if (condition.containsKey("branchSchool")) {
			sql.append(" and unit.resourceid=:branchSchool");
		}
		sql.append("    inner join EDU_BASE_CLASSIC      classic on stu.CLASSICID = classic.resourceid  and classic.isDeleted = :isDeleted ");
		if (condition.containsKey("classic")) {
			sql.append(" and classic.resourceid=:classic");
		}
		sql.append("   where ordercourse.isDeleted = 0 ");

		if (condition.containsKey("yearInfo")) {
			sql.append(" and ordercourse.ordercourseyear=:yearInfo");
		}
		if (condition.containsKey("term")) {
			sql.append(" and ordercourse.ordercourseterm=:term");
		}

		if (condition.containsKey("startTime")) {
			sql.append(" and ordercourse.orderCourseTime>=to_date(:startTime,'yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append(" and ordercourse.orderCourseTime<=to_date(:endTime,'yyyy-MM-dd HH24:mi:ss') ");
		}

		sql.append(" group by unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,course.courseName ");
		sql.append(" order by unit.UNITNAME,classic.CLASSICNAME,major.MAJORNAME,course.courseName ");		
		condition.put("isDeleted", 0);
		try {
			resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resutList;
	}
	/**
	 * 查询试室的座位安排信息-试室信息
	 * @param condition
	 * @return
	 */
	@Override
	public List<Map<String,Object>> findExamRoomInfoByBranchSchoolAndExamSub(Map<String,Object> condition) {
		
		Map<String,Object> paramMap     = new HashMap<String, Object>();
		List<Map<String,Object>> list 	= new ArrayList<Map<String,Object>>();
		paramMap.put("isDeleted", 0);
		
		StringBuffer sql = new StringBuffer();
		sql.append("     select er.resourceid,er.EXAMROOMNAME,er.iscomputerroom ,er.doubleseatnum ");
		/*//------------------------------------查询已安排人数------------------------------------
		sql.append("     (");
		sql.append("		select count(rs.resourceid) from  EDU_TEACH_EXAMRESULTS rs ");
		sql.append("        inner join edu_teach_examinfo i on rs.examinfoid = i.resourceid  and i.isdeleted =:isDeleted ");
		if (condition.containsKey("examSub")) {
			paramMap.put("examSub", condition.get("examSub"));
			sql.append("      and i.examsubid =:examSub ");
		}
		if (condition.containsKey("startTime")) {
			paramMap.put("startTime", condition.get("startTime"));
			sql.append("      and i.examstarttime  =:startTime");
		}
		if (condition.containsKey("endTime")) {
			paramMap.put("endTime", condition.get("endTime"));
			sql.append("      and i.examendtime =:endTime");
		}	
		sql.append("        where rs.isdeleted = :isDeleted and  rs.CLASSROOMID = er.resourceid ");
		sql.append("      ) as assigned");
		//------------------------------------查询已安排人数------------------------------------
		*/
		sql.append("       from EDU_BASE_EXAMROOM er ");
		sql.append("    where er.isDeleted=:isDeleted ");
		if (condition.containsKey("examPlace")) {
			sql.append("    and er.BRANCHSCHOOLID='001' ");
		}else{
			if (condition.containsKey("branchSchool")) {
				paramMap.put("branchSchool", condition.get("branchSchool"));
				sql.append("    and er.BRANCHSCHOOLID=:branchSchool ");
			}
		}
		
		if (condition.containsKey("examMode")&& "5".equals(condition.get("examMode"))) {
			sql.append("    and er.iscomputerroom=:isComputerroom ");
			paramMap.put("isComputerroom", Constants.BOOLEAN_YES);
		}
		//sql.append("   group by er.doubleseatnum,er.EXAMROOMNAME,er.iscomputerroom,er.resourceid  " );
		sql.append(" order by er.EXAMROOMNAME ");
		//sql.append("   order by (er.doubleseatnum - assigned) desc " );
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), paramMap );			
			list = findExamRoomAssignedByBranchSchoolAndExamSub(list,condition);
//			ComparatorExamSeatAssigendForExamRoom c = new ComparatorExamSeatAssigendForExamRoom();
//			Collections.sort(list,c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 查询试室的座位安排信息-试室信息_按课程分
	 * @param condition
	 * @return
	 */
	@Override
	public List<Map<String,Object>> findExamRoomInfoByBranchSchoolAndExamSubAndCourse(Map<String,Object> condition) {
		
		Map<String,Object> paramMap     = new HashMap<String, Object>();
		List<Map<String,Object>> list 	= new ArrayList<Map<String,Object>>();
		paramMap.put("isDeleted", 0);
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("     select erm.examroomname,erm.examroomsize,sub.batchname,u.unitname,c.coursename,sum(1) assigned,sub.resourceid subid,u.resourceid unitid,c.resourceid cid,erm.resourceid rid, to_char(info.examstarttime,'yyyy-MM-dd hh24:mi')||'-'||to_char(info.examendtime,'hh24:mi') examtime,info.resourceid infoid");
		sql.append("     from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid  ");
		sql.append("     join edu_teach_examinfo info on sp.examinfoid = info.resourceid      ");
		sql.append("     join edu_teach_examsub sub on sub.resourceid = info.examsubid  ");
		sql.append("     join edu_base_course c on c.resourceid = info.courseid  ");
		sql.append("     join edu_teach_examresults re on re.examinfoid = info.resourceid and re.studentid  = stu.resourceid ");   
		sql.append("     join edu_base_major m on stu.majorid = m.resourceid  ");
		sql.append("     join edu_base_examroom erm on re.classroomid = erm.resourceid "); 
		sql.append("     join hnjk_sys_unit u on erm.branchschoolid = u.resourceid  ");
		sql.append("     where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null "); 
		sql.append("     and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is not null and re.classroomid  is not null "); 
		sql.append("     and stu.studentstatus = 11 and  stu.isdeleted = 0   ");
		
		/* 之前方法中的条件 */
		if (condition.containsKey("examPlace")) {
			sql.append("    and erm.BRANCHSCHOOLID='001' ");
		}else{
			
			if (condition.containsKey("branchSchool")) {
				paramMap.put("branchSchool", condition.get("branchSchool"));
				sql.append("    and erm.BRANCHSCHOOLID=:branchSchool ");
			}
			
		}
		
		if (condition.containsKey("examMode")&& "5".equals(condition.get("examMode"))) {
			sql.append("    and erm.iscomputerroom=:isComputerroom ");
			paramMap.put("isComputerroom", Constants.BOOLEAN_YES);
		}
		/* 之前方法中的条件 */
		/*补充可用条件 批次和课程*/
		
		if(condition.containsKey("examSub")){
			sql.append(" and  sub.resourceid = :examSub ");
			paramMap.put("examSub", condition.get("examSub"));
		}
		if(condition.containsKey("courseId")){
			sql.append(" and  c.resourceid = :courseId ");
			paramMap.put("courseId", condition.get("courseId"));
		}
		
		
		if (condition.containsKey("multiValueId")) {
			if(condition.get("multiValueId").toString().contains(",")){
				String[] ids =condition.get("multiValueId").toString().split(",") ;
				sql.append(" and (");
				for (int i = 0; i < ids.length; i++) {
					if(ExStringUtils.isNotEmpty(ids[i])&&i!=ids.length-1){
						sql.append(" (erm.resourceid = '"+ids[i].split("__")[0]+"' and info.resourceid = '"+ids[i].split("__")[1]+"') or ");
					}else{
						sql.append(" (erm.resourceid = '"+ids[i].split("__")[0]+"' and info.resourceid = '"+ids[i].split("__")[1]+"')  ");
					}
				}
				sql.append(" ) ");
			}else{
				sql.append(" and erm.resourceid = '"+condition.get("multiValueId").toString().split("__")[0]+"' and info.resourceid = '"+condition.get("multiValueId").toString().split("__")[1]+"' ");
			}
		}
		if(condition.containsKey("examtime")){
			sql.append(" and to_char(info.examstarttime,'yyyy-MM-dd hh24:mi')||'-'||to_char(info.examendtime,'hh24:mi') = '"+condition.get("examtime")+"' ");
			
		}
		
		sql.append(" group by erm.examroomname,erm.examroomsize,sub.resourceid,sub.batchname,u.resourceid,u.unitname,c.resourceid,c.coursename,erm.resourceid,to_char(info.examstarttime,'yyyy-MM-dd hh24:mi')||'-'||to_char(info.examendtime,'hh24:mi') ,info.resourceid  ");
		sql.append(" order by erm.examroomname,info.resourceid ");
		
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), paramMap );			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	/**
	 * 查询试室的座位安排信息-试室已安排座位信息
	 * @param examRoomList
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> findExamRoomAssignedByBranchSchoolAndExamSub(List<Map<String,Object>> examRoomList,Map<String,Object> condition) throws Exception{
		StringBuffer sql 		    = new StringBuffer();
		Map<String,Object> paramMap = new HashMap<String, Object>();
		paramMap.put("isDeleted", 0);
		sql.append("  select rs.CLASSROOMID,count(rs.resourceid) assigned  ");
		sql.append("    from  EDU_TEACH_EXAMRESULTS rs ");
		sql.append("   inner join edu_teach_examinfo i on rs.examinfoid = i.resourceid  and i.isdeleted =:isDeleted ");
		if (condition.containsKey("examSub")) {
			paramMap.put("examSub", condition.get("examSub"));
			sql.append(" and i.examsubid =:examSub ");
		}
		if (condition.containsKey("examSubId")) {
			paramMap.put("examSubId", condition.get("examSubId"));
			sql.append(" and i.examsubid =:examSubId ");
		}
		if (condition.containsKey("startTime")) {
			paramMap.put("startTime", condition.get("startTime"));
			sql.append(" and i.examstarttime  =:startTime");
		}
		if (condition.containsKey("endTime")) {
			paramMap.put("endTime", condition.get("endTime"));
			sql.append(" and i.examendtime =:endTime");
		}
		if(condition.containsKey("courseId")){
			paramMap.put("courseId", condition.get("courseId"));
			sql.append(" and i.courseid =:courseId");
		}
		sql.append("   inner join EDU_BASE_EXAMROOM er on rs.CLASSROOMID = er.resourceid ");
		if (condition.containsKey("branchSchool")) {
			paramMap.put("branchSchool", condition.get("branchSchool"));
			sql.append("    and er.BRANCHSCHOOLID=:branchSchool ");
		}
			
		if (condition.containsKey("examRoomIds")) {
			sql.append(" and  er.resourceid in("+condition.get("examRoomIds")+")");
			
		}
		sql.append("   where rs.isdeleted = :isDeleted  ");
		if (condition.containsKey("examSub")) {
			paramMap.put("examSub", condition.get("examSub"));
			sql.append(" and rs.examsubid =:examSub ");
		}
		
		sql.append(" group by rs.CLASSROOMID,er.EXAMROOMNAME  ");  
		sql.append(" order by er.EXAMROOMNAME ");
		List<Map<String,Object>>list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), paramMap );	
		paramMap.clear();
		for (int i = 0; i < list.size(); i++) {
			paramMap.put((String) list.get(i).get("classroomid"), list.get(i).get("assigned"));
		}
		for (int i = 0; i < examRoomList.size(); i++) {
			examRoomList.get(i).put("assigned",null==paramMap.get(examRoomList.get(i).get("resourceid"))?new BigDecimal("0"):paramMap.get(examRoomList.get(i).get("resourceid")));
		}
		return examRoomList;
	}
	
	/**
	 * 查指定用户的所有在学学籍Id
	 * @param user
	 * @return
	 */
	@Override
	public List<String> findUserStudentInfoIdList(User user) {
		List<String> stuIdList 	  = new ArrayList<String>();
		Map<String,Object> param  = new HashMap<String,Object>();
		if (null!=user) {
			
			
			param.put("sysUserId", user.getResourceid());
			String sql = "select stu.resourceid from EDU_ROLL_STUDENTINFO stu where stu.isdeleted = 0  and stu.sysuserid=:sysUserId";
			try {
				List<Map<String,Object>> list  = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, param);
				if (null!=list && !list.isEmpty()) {
					for (Map<String,Object> map : list) {
						stuIdList.add(map.get("RESOURCEID").toString());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stuIdList;
	}

	/**
	 * 查看未预约学习学生信息
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findNotCourseOrderStudentList(Map<String, Object> condition,Page page)throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql   = new StringBuffer(" select stu.sysuserid, (select u.username from hnjk_sys_users u where u.resourceid=stu.sysuserid) as username,stu.resourceid,bstu.name,bstu.mobile ,bstu.officephone,bstu.CONTACTPHONE, bstu.email,g.gradename,c.classicname,m.majorname,unit.unitName ");
		sql.append(" from edu_roll_studentinfo stu,edu_base_grade g,edu_base_classic c,HNJK_SYS_UNIT unit,edu_base_major m ,edu_base_student bstu ");
		sql.append(" where stu.isdeleted=0 and stu.studentstatus='11' and stu.studentbaseinfoid = bstu.resourceid and stu.gradeid = g.resourceid ");
		sql.append(" and stu.classicid = c.resourceid and stu.branchschoolid = unit.resourceid and stu.majorid = m.resourceid ");
		
		//------------子查询条件------------
		sql.append(" and not exists ( select p.resourceid from edu_learn_stuplan p ,edu_teach_plancourse pc  ");
		sql.append("       where p.isdeleted = 0 and p.studentid = stu.resourceid and pc.resourceid = p.plansourceid ");
		if (condition.containsKey("yearInfo")) {
			sql.append(" and p.yearid =? ");
			param.add(condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append(" and p.term =? ");
			param.add(condition.get("term"));
		}
		if (condition.containsKey("courseId")) {
			sql.append(" and pc.courseid =? ");
			param.add(condition.get("courseId"));
		}
		sql.append(" )");
		//------------子查询条件------------
		
		if (condition.containsKey("branchSchool")) {
			sql.append(" and stu.branchschoolid =?");
			param.add(condition.get("branchSchool"));
		}
		if (condition.containsKey("gradeid")) {
			sql.append(" and stu.gradeid =? ");
			param.add(condition.get("gradeid"));
		}
		if (condition.containsKey("classic")) {
			sql.append(" and stu.classicid =? ");
			param.add(condition.get("classic"));
		}
		if (condition.containsKey("major")) {
			sql.append(" and stu.majorid =?  ");
			param.add(condition.get("major"));
		}
		if (condition.containsKey("studyNo")) {
			sql.append(" and stu.studyno =?  ");
			param.add(condition.get("studyNo"));
		}
		if (condition.containsKey("name")) {
			sql.append(" and stu.studentname like '%"+condition.get("name")+"%'");
		}
		sql.append(" order by unit.unitName,stu.studyno desc");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), param.toArray(), NotCourseOrderStuInfoVo.class);
		
	}
	/**
	 * 查找给定考试批次所有考试课程的时间段(考试周时间段)
	 * @param examSubId           考试批次ID
	 * @param examMode            考试形式（机考、非机考）
	 * @param isFillterExamMode   是否过滤考试形式
	 * @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> findExamTimeSegment(String examSubId,String examMode,String isFillterExamMode) {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql         =  new StringBuffer();
		param.put("examSubId", examSubId);
		sql.append(" select to_char(i.examstarttime,'yyyy-MM-dd HH24:mi:ss') as startTime ,to_char(i.examendtime,'yyyy-MM-dd HH24:mi:ss') as endTime ,to_char(i.examendtime,'HH24:mi:ss') as shortEndTime");
		sql.append(" from EDU_TEACH_EXAMINFO i ,edu_base_course c where  i.isdeleted = 0 and i.courseid = c.resourceid ");
		sql.append(" and c.examtype <> 2 ");    //    非口试
		sql.append(" and c.examtype <> 3 ");    //    非大作业
		sql.append(" and c.examtype <> 5 ");    //    非统考
		sql.append(" and c.examtype <> 6  ");   //    非网上考试
		sql.append(" and c.ispractice = 'N' "); //    非实践课程
		sql.append(" and i.examsubid  =:examSubId");     //    -- 考试批次ID    
		sql.append(" and i.examstarttime is not null and i.examendtime is not null ");
		
		if (ExStringUtils.isNotEmpty(isFillterExamMode)&&Constants.BOOLEAN_YES.equals(isFillterExamMode)) {
			if (ExStringUtils.isNotEmpty(examMode)&&"5".equals(examMode)) {
				sql.append(" and i.examcoursetype= :examcoursetype ");
				param.put("examcoursetype",3);
			}else {
				sql.append(" and i.examcoursetype= :examcoursetype ");
				param.put("examcoursetype",0);
			}	
		}
		
		sql.append(" group by i.examstarttime,i.examendtime ");
		sql.append(" order by i.examstarttime "); 
         
        try {
        	return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),param );
		} catch (Exception e) {
			return new ArrayList<Map<String,Object>>();
		}  
	}
	/**
	 * 查找有预约记录的考试课程信息(批量座位安排)
	 * @param condition
	 * @return
	 */
	@Override
	public Page findExamCourseInfoByCondition(Page page,Map<String, Object> condition) {		
		
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql   = new StringBuffer();
		 try {
			String examInfoIds = findExistsExamCourseByCondition(condition);
			//------------------------------------------考试课程信息------------------------------------------
			sql.append(" select info.resourceid,c.coursename,f_dictionary('CodeCourseExamType',c.examtype) as examtype,");
			sql.append("         info.examcoursecode,info.examstarttime,info.examendtime");
			//------------------------------------------考试课程信息------------------------------------------
			
			/*//------------------------------------------1.已安排人数------------------------------------------
			sql.append(" 		( select count(dis.resourceid) from edu_teach_examinfo i ");
			sql.append("		  inner join edu_teach_examresults rls  on i.resourceid = rls.examinfoid and rls.isdeleted=?");
			param.add(0);
			sql.append("		  inner join edu_teach_examdistribu dis on dis.examresultsid = rls.resourceid and dis.isdeleted = ?  ");
			param.add(0);
			sql.append("		  inner join edu_roll_studentinfo stu  on rls.studentid     = stu.resourceid ");
	
			sql.append("		  inner join hnjk_sys_unit u           on stu.branchschoolid = u.resourceid  "); //学习中心
			if (condition.containsKey("mergeExamBrschool")) {
				sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
			}else {
				sql.append("		  and u.resourceid=?");
				param.add(condition.get("branchSchool"));
			}
			
			sql.append("		  where dis.isdeleted = ?  and i.resourceid = info.resourceid");
			param.add(0);
			if (condition.containsKey("courseId")) {                  //课程
				sql.append("        and i.courseid   =?  ");
				param.add(condition.get("courseId"));
			}
			if (condition.containsKey("startTime")) {                 //考试开始时间
				sql.append("	    and i.EXAMSTARTTIME=?");
				param.add(condition.get("startTime"));
			}
			if (condition.containsKey("endTime")) {                   //考试结束时间
				sql.append("		and i.EXAMENDTIME=?");
				param.add(condition.get("endTime"));
			}
			sql.append("         )  assigend,");
			//------------------------------------------1.已安排人数------------------------------------------
		
			
			//------------------------------------------2.预约人数------------------------------------------
			sql.append("		(  select count(0) from edu_learn_stuplan stp ");
			sql.append("           inner join edu_teach_examinfo i      on stp.examinfoid = i.resourceid ");
			if (condition.containsKey("startTime")) {                 //考试开始时间
				sql.append("	    and i.EXAMSTARTTIME=?");
				param.add(condition.get("startTime"));
			}
			if (condition.containsKey("endTime")) {                   //考试结束时间
				sql.append("		and i.EXAMENDTIME=?");
				param.add(condition.get("endTime"));
			} 
			if (condition.containsKey("courseId")) {                  //课程
				sql.append("        and i.courseid   =?  ");
				param.add(condition.get("courseId"));
			}
			sql.append("		   inner join edu_roll_studentinfo stu  on stp.studentid = stu.resourceid ");
	
			sql.append("		   inner join hnjk_sys_unit u           on stu.branchschoolid = u.resourceid ");//学习中心
			if (condition.containsKey("mergeExamBrschool")) {
				sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
			}else {
				sql.append("		  and u.resourceid=?");
				param.add(condition.get("branchSchool"));
			}
			sql.append("		   where stp.isdeleted = ? and stp.status = ? ");
			param.add(0);
			param.add(2);
			sql.append("             and stp.examinfoid = info.resourceid");
			sql.append("        ) totalNum ");
			//------------------------------------------2.预约人数------------------------------------------
		
			*/
			//------------------------------------------3.考试课程------------------------------------------
			sql.append(" from edu_teach_examinfo info ");
			sql.append(" inner join edu_base_course c  on info.courseid = c.resourceid ");
			sql.append("       and c.examtype <> ? ");   //-- 口试
			param.add(2);
			sql.append("       and c.examtype <> ? ");   //-- 大作业
			param.add(3);
			sql.append("       and c.examtype <> ? ");   //-- 统考
			param.add(5);
			sql.append("       and c.examtype <> ? ");   //-- 网上考试
			param.add(6);
			sql.append("       and c.ispractice = ? ");//-- 实践课程
			param.add("N");
			sql.append(" where info.isDeleted = ? ");
			param.add(0);
			if (condition.containsKey("examMode")&& "5".equals(condition.get("examMode"))) {
				sql.append(" and info.examcoursetype= ? ");
				param.add(3);
			}else {
				sql.append(" and info.examcoursetype= ? ");
				param.add(0);
			}
			
			if (condition.containsKey("examSub")) {                   //考试批次
			sql.append(" and info.examsubid = ?");
			param.add(condition.get("examSub"));
			}
			if (condition.containsKey("startTime")) {                 //考试开始时间
				sql.append("	    and info.EXAMSTARTTIME=?");
				param.add(condition.get("startTime"));
			}
			if (condition.containsKey("endTime")) {                   //考试结束时间
				sql.append("		and info.EXAMENDTIME=?");
				param.add(condition.get("endTime"));
			}
			if (condition.containsKey("courseId")) {                  //课程
				sql.append("        and info.courseid   =?  ");
				param.add(condition.get("courseId"));
			}
			sql.append(" and info.resourceid in("+examInfoIds+")");
		/*	// 有预约记录的考试课程
			sql.append("  and exists ( select stp.resourceid from edu_learn_stuplan stp  ");
			sql.append("  			   	        inner join edu_roll_studentinfo stu  on stp.studentid = stu.resourceid 	");
			sql.append("  			   		    inner join hnjk_sys_unit u           on stu.branchschoolid = u.resourceid 	");//学习中心
			if (condition.containsKey("mergeExamBrschool")) {
				sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
			}else {
				sql.append("		  and u.resourceid=?");
				param.add(condition.get("branchSchool"));
			}
			sql.append(" 			   where stp.isdeleted = ? and stp.status = ? and stp.examinfoid = info.resourceid ) ");
			param.add(0);
			param.add(2);*/
			sql.append(" order by cast(c.examtype as int) desc ");
			//------------------------------------------3.考试课程------------------------------------------
		
		 	page    = baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), param.toArray(),ExamSeatAssignExamCourseVo.class);
		 	List<ExamSeatAssignExamCourseVo> pageList = page.getResult();
		 	StringBuffer ids = new StringBuffer();
		 	for (int i = 0; i < pageList.size(); i++) {
		 		ExamSeatAssignExamCourseVo vo = pageList.get(i);
		 		ids.append(",'"+vo.getRESOURCEID()+"'");
			}
		 	if (ExStringUtils.isNotBlank(ids.toString())) {
		 		condition.put("examinfoIds", ids.substring(1));
		 		page = findExamCourseAssigendByCondition(page,condition);// 已安排人数
		 		page = findExamCourseOrderNumByCondition(page,condition);// 预约人数
			}
		 	ComparatorExamSeatAssigendForExamInfo c = new ComparatorExamSeatAssigendForExamInfo();
		 	List resultList = page.getResult();
		 	Collections.sort(resultList,c);
		 	page.setResult(resultList);
	    	return  page;
		} catch (Exception e) {
			e.printStackTrace();
			return page;
		}  
	}
	/**
	 * 查找学习中心有预约的考试课程
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	private String findExistsExamCourseByCondition(Map<String, Object> condition) throws Exception{
		StringBuffer sql   		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("examSub", condition.get("examSub"));
		param.put("isDeleted", 0);
		param.put("status",2);
		sql.append(" select distinct stp.examinfoid from edu_learn_stuplan stp  ");
		sql.append("  inner join edu_roll_studentinfo stu  on stp.studentid = stu.resourceid 	");
		sql.append("  inner join hnjk_sys_unit u           on stu.branchschoolid = u.resourceid 	");//学习中心
		if (condition.containsKey("mergeExamBrschool")) {
			sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
		}else {
			sql.append("		  and u.resourceid=:branchSchool");
			param.put("branchSchool",condition.get("branchSchool"));
		}
		sql.append(" where stp.isdeleted = :isDeleted and stp.status = :status   ");
		sql.append("  and stp.examinfoid in( select info.resourceid from edu_teach_examinfo info where info.examsubid =:examSub and info.isdeleted = :isDeleted)");
		List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		sql  = new StringBuffer();
		if (null!=list&&!list.isEmpty()) {
			for (Map<String,Object> m : list ) {
				sql.append(",'"+m.get("examinfoid")+"'");
			}
		}
		if (ExStringUtils.isNotBlank(sql.toString())) {
			return sql.substring(1);
		}else {
			return sql.toString();
		}
	}
	/**
	 * 查找有预约记录考试课程的已安排人数(批量座位安排)
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	private Page findExamCourseAssigendByCondition(Page page,Map<String, Object> condition) throws Exception{
		
		StringBuffer sql   		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted",0);
		sql.append(" 		 select i.resourceid,count(dis.resourceid) assigend ");
		sql.append("		   from edu_teach_examinfo i ");
		sql.append("		  inner join edu_teach_examresults rls  on i.resourceid = rls.examinfoid and rls.isdeleted=:isDeleted");
		sql.append("		  left join edu_teach_examdistribu dis on dis.examresultsid = rls.resourceid and dis.isdeleted = :isDeleted  ");
		sql.append("		  inner join edu_roll_studentinfo stu   on rls.studentid     = stu.resourceid ");
		sql.append("		  inner join hnjk_sys_unit u            on stu.branchschoolid = u.resourceid  "); 
		if (condition.containsKey("mergeExamBrschool")) {
			sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
		}else {
			param.put("branchSchool",condition.get("branchSchool"));
			sql.append("		  and u.resourceid=:branchSchool");

		}
		sql.append("		  where i.isdeleted = :isDeleted  ");
		if (condition.containsKey("examinfoIds")) {
			sql.append("and i.resourceid in("+condition.get("examinfoIds")+")");
		}
		if (condition.containsKey("courseId")) {              
			sql.append("        and i.courseid   =:courseId  ");
			param.put("courseId",condition.get("courseId"));
		}
		if (condition.containsKey("startTime")) {                
			sql.append("	    and i.EXAMSTARTTIME=:startTime");
			param.put("startTime",condition.get("startTime"));
		}
		if (condition.containsKey("endTime")) {                   
			sql.append("		and i.EXAMENDTIME=:endTime");
			param.put("endTime",condition.get("endTime"));
		}
		sql.append(" group by i.resourceid ");
		List<Map<String,Object>>  list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		param.clear();
		for (int i = 0; i < list.size(); i++) {
			param.put(list.get(i).get("resourceid").toString(), list.get(i).get("assigend"));
		}
		for (int i = 0; i < page.getResult().size(); i++) {
			ExamSeatAssignExamCourseVo vo = (ExamSeatAssignExamCourseVo) page.getResult().get(i);
			long  assigend= null==param.get(vo.getRESOURCEID())? 0L :((BigDecimal) param.get(vo.getRESOURCEID())).longValue();
			vo.setASSIGEND(assigend);
		}
		return page;
	}
	/**
	 * 查找有预约记录考试课程的预约人数(批量座位安排)
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	private Page findExamCourseOrderNumByCondition(Page page,Map<String, Object> condition) throws Exception{
		StringBuffer sql   		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("isDeleted",0);
		param.put("status",2);
		sql.append(" select i.resourceid,count(stp.resourceid) totalNum ");
		sql.append("   from edu_teach_examinfo i    ");
		sql.append("  left join edu_learn_stuplan stp     on stp.examinfoid = i.resourceid  and stp.isdeleted =:isDeleted  and stp.status =:status ");
		sql.append("  inner join edu_roll_studentinfo stu  on stp.studentid = stu.resourceid ");
		sql.append("  inner join hnjk_sys_unit u           on stu.branchschoolid = u.resourceid "); 
		if (condition.containsKey("mergeExamBrschool")) {
			sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
		}else {
			sql.append("		  and u.resourceid=:branchSchool");
			param.put("branchSchool",condition.get("branchSchool"));
		}
		sql.append(" where i.isDeleted =:isDeleted");
		if (condition.containsKey("examinfoIds")) {
			sql.append(" and i.resourceid in("+condition.get("examinfoIds")+")");
		}
		if (condition.containsKey("startTime")) {                 //考试开始时间
			sql.append(" and i.EXAMSTARTTIME=:startTime");
			param.put("startTime",condition.get("startTime"));
		}
		if (condition.containsKey("endTime")) {                   //考试结束时间
			sql.append(" and i.EXAMENDTIME=:endTime");
			param.put("endTime",condition.get("endTime"));
		} 
		if (condition.containsKey("courseId")) {                  //课程
			sql.append(" and i.courseid   =:courseId ");
			param.put("courseId",condition.get("courseId"));
		}
		sql.append(" group by i.resourceid ");
		List<Map<String,Object>>  list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		
		param.clear();
		for (int i = 0; i < list.size(); i++) {
			param.put(list.get(i).get("resourceid").toString(), list.get(i).get("totalNum"));
		}
		for (int i = 0; i < page.getResult().size(); i++) {
			ExamSeatAssignExamCourseVo vo = (ExamSeatAssignExamCourseVo) page.getResult().get(i);
			long  totalNum = null==param.get(vo.getRESOURCEID())? 0L :((BigDecimal) param.get(vo.getRESOURCEID())).longValue();
			vo.setTOTALNUM(totalNum);
		}
		
		return page;
	}
	/*
	 *  统计课程的考试人数
	 */
	@Override
	public List<Map<String, Object>> findCourseExaminationNum(Map<String, Object> condition) {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		StringBuffer sql 			 = new StringBuffer();
		
		if ("forPage".equals(condition.get("statDirection"))) {
			//--------------------------------用于生成试卷标签页面中查询数据--------------------------------
			sql.append("     select i.examcoursecode,c.coursename,case i.ismachineexam when 'Y' then '机考' else  f_dictionary('CodeCourseExamType',c.examtype) end as examtype,  u.unitname,count(rls.resourceid) as totalNum ");
			sql.append("       from edu_teach_examresults rls ");
			sql.append(" inner join edu_roll_studentinfo stu on rls.studentid = stu.resourceid ");
			sql.append(" inner join hnjk_sys_unit u          on stu.branchschoolid = u.resourceid ");
			sql.append(" inner join edu_teach_examinfo i     on rls.examinfoid    = i.resourceid  and (i.examcoursetype=0 or i.examcoursetype = 3) ");
			if (condition.containsKey("examSubId")) {
				sql.append("  and i.examsubid =:examSubId  ");
				paramMap.put("examSubId", condition.get("examSubId"));
			}
			sql.append(" inner join edu_base_course c  on i.courseid = c.resourceid  and c.examtype <> 2 and c.examtype <> 3 and c.examtype <> 5 and c.examtype <> 6 and c.ispractice = 'N' ");
			sql.append("      where rls.isdeleted = 0 ");
			sql.append("   group by u.unitname,i.resourceid,i.ismachineexam,i.examcoursecode,c.coursename,c.coursecode,c.examtype  ");
			sql.append("   order by u.unitname,i.examcoursecode,c.coursename,totalNum desc ");
			
		}else if("forProgram".equals(condition.get("statDirection"))) {
			//--------------------------------用于后台生成试卷标签时查询数据--------------------------------
			sql.append("    select i.resourceid as examinfoId , u.resourceid as unitId,room.resourceid as examRoomId,count(rls.resourceid) as totalNum ");
			sql.append("       from edu_teach_examresults rls ");
			sql.append(" inner join edu_roll_studentinfo stu  on rls.studentid = stu.resourceid");
			sql.append(" inner join hnjk_sys_unit u           on stu.branchschoolid = u.resourceid ");
			sql.append(" inner join edu_teach_examinfo i      on rls.examinfoid    = i.resourceid  and (i.examcoursetype=0 or i.examcoursetype = 3) ");
			if (condition.containsKey("examSubId")) {
				sql.append(" and i.examsubid =:examSubId ");
				paramMap.put("examSubId", condition.get("examSubId"));
			}
			sql.append(" inner join edu_base_course c  on i.courseid = c.resourceid and c.examtype <> 2 and c.examtype <> 3 and c.examtype <> 5 and c.examtype <> 6 and c.ispractice = 'N' ");
			sql.append(" left  join edu_base_examroom room   on rls.classroomid = room.resourceid   ");
			sql.append(" where rls.isdeleted = 0 ");
			sql.append(" group by i.resourceid,u.resourceid,room.resourceid");
			sql.append(" order by u.resourceid,room.resourceid,i.resourceid,totalNum desc");
			//--------------------------------用于后台生成试卷标签时查询数据--------------------------------
		}
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),paramMap);
		} catch (Exception e) {
			return new ArrayList<Map<String,Object>>();
		}
	}

		
	//期末考试人数统计
	@Override
	public List<Map<String,Object>> statExamNum(Map<String, Object> condition) {
		
		List<Map<String,Object>> resutList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		
		sql.append("select u.resourceid,u.unitname,u.unitcode,u.contectcall,count(i.resourceid) ");
		sql.append("	   from hnjk_sys_unit u ");
		sql.append("  left join edu_roll_studentinfo i on u.resourceid = i.branchschoolid");
		sql.append("  where u.isdeleted = 0 and u.unittype = 'brSchool'");
		sql.append("	and exists ( ");
		sql.append("		select * from edu_learn_stuplan stp ");
		sql.append("        inner join edu_teach_examinfo info  on stp.examinfoid = info.resourceid");
		//考试批次的动态参数
		if (condition.containsKey("examsubid")) {
			sql.append(" and info.examsubid = :examsubid ");
		}
		sql.append(" where stp.isdeleted = 0 and stp.status = 2");
		//
		sql.append(" and i.resourceid = stp.studentid");
		sql.append(")");
//		学习中心的动态参数，在前台处理将相应的学习中心显示为高亮
//		if (condition.containsKey("srcschool")&&condition.containsKey("desschool"))      sql.append(" and ( u.resourceid=:srcschool or u.resourceid=:desschool ) ");
//		if (condition.containsKey("srcschool")&&!condition.containsKey("desschool"))     sql.append(" and u.resourceid=:srcschool ");
//		if (!condition.containsKey("srcschool")&&condition.containsKey("desschool"))     sql.append(" and u.resourceid=:desschool ");
		sql.append(" group by u.resourceid,u.unitname,u.unitcode,u.contectcall");
		sql.append(" order by count(i.resourceid)  desc");
		
		try {
			resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resutList;
	}
	
	//入学考试人数统计
	@Override
	public List<Map<String,Object>> statRecruitExamNum(Map<String, Object> condition) {
		
		List<Map<String,Object>> resutList= new ArrayList<Map<String,Object>>();
		StringBuffer sql = new StringBuffer();	
		sql.append("select u.resourceid,p.examplanname,u.unitname,u.unitcode,u.contectcall,count(i.resourceid) ");
		sql.append("	   from hnjk_sys_unit u  ");
		sql.append("  left join edu_recruit_enrolleeinfo i on i.branchschoolid = u.resourceid and i.signupFlag = 'Y' and i.isdeleted = 0 ");
		sql.append("  left join edu_recruit_major m on i.recruitmajorid = m.resourceid ");
		sql.append("  join edu_recruit_examplan p on m.recruitplanid =p.recruitplanid ");
		sql.append("  where u.isdeleted = 0 and u.unittype = 'brSchool' ");
		//招生批次的动态参数
		if (condition.containsKey("recruitplanid")) {
			sql.append("  and m.recruitplanid=:recruitplanid ");
		}
		
//		学习中心的动态参数，在前台处理将相应的学习中心显示为高亮
//		if (condition.containsKey("srcschool")&&condition.containsKey("desschool"))      sql.append(" and ( u.resourceid=:srcschool or u.resourceid=:desschool ) ");
//		if (condition.containsKey("srcschool")&&!condition.containsKey("desschool"))     sql.append(" and u.resourceid=:srcschool ");
//		if (!condition.containsKey("srcschool")&&condition.containsKey("desschool"))     sql.append(" and u.resourceid=:desschool ");
		sql.append("  group by u.resourceid,p.examplanname,u.unitname,u.unitcode,u.contectcall");
		sql.append("  order by count(i.resourceid)  desc ");
		
		try {
			resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resutList;
	}
	
	/**
	 * 打印标签时查找卷袋标签信息(按学习中心打印，按课程打印)
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findExamPaperBagInfoForPrint(Page page,Map<String, Object> condition) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql   = new StringBuffer();
		if (condition.containsKey("printByCourse")) {
			
			sql.append("     select i.resourceid,c.coursename,i.examcoursecode,i.examstarttime,i.examendtime," );
			sql.append(" case i.ismachineexam when 'Y' then '机考' else f_dictionary('CodeCourseExamType',c.examtype) end as examtype,");		
			sql.append(" sum(pbs.ordernum) as orderNum,sum(pbs.papernum) as paperNum ,sum(pbs.bagnum) as bagNum");
			sql.append("       from edu_teach_exampaperbagstat pbs  ");
			sql.append(" inner join edu_teach_examinfo i on i.resourceid = pbs.examinfoid  ");
			if (condition.containsKey("startTime")) {
				sql.append("    and i.examstarttime=? ");
				param.add(condition.get("startTime"));
			}
			if (condition.containsKey("endTime")) {
				sql.append("    and i.examendtime=?  ");
				param.add(condition.get("endTime"));
			}
			if (condition.containsKey("examSubId")) {
				sql.append("    and i.examsubid=?  ");
				param.add(condition.get("examSubId"));
			}
			if (condition.containsKey("isMachineExam")) {
				sql.append("    and i.ismachineexam=?  ");
				param.add(condition.get("isMachineExam"));
			}
			sql.append(" inner join edu_base_course c on i.courseid = c.resourceid ");
			if (condition.containsKey("courseId")) {
				sql.append("    and c.resourceid=?  ");
				param.add(condition.get("courseId"));
			}
			
			sql.append("      where pbs.isdeleted = 0   ");
			if (condition.containsKey("branchSchool")) {
				sql.append("    and pbs.unitid=?");
				param.add(condition.get("branchSchool"));
			}
			sql.append("   group by c.coursename,i.examcoursecode, i.ismachineexam,c.examtype,i.resourceid,i.examstarttime ,i.examendtime ");
			sql.append("   order by i.examcoursecode,c.coursename ");
			
		}else if (condition.containsKey("printByBrschool")) {
			
			sql.append("     select u.resourceid,u.unitname,sum(pbs.ordernum) as orderNum,sum(pbs.papernum) as paperNum ,sum(pbs.bagnum) as bagNum");
			sql.append("       from edu_teach_exampaperbagstat pbs ");
			sql.append(" inner join edu_teach_examinfo i on i.resourceid = pbs.examinfoid  ");
			if (condition.containsKey("isMachineExam")) {
				sql.append("    and i.ismachineexam=?  ");
				param.add(condition.get("isMachineExam"));
			}
			if (condition.containsKey("examSubId")) {
				sql.append("    and i.examsubid=?  ");
				param.add(condition.get("examSubId"));
			}
			sql.append(" inner join hnjk_sys_unit u on pbs.unitid = u.resourceid ");
			if (condition.containsKey("branchSchool")) {
				sql.append(" and  u.resourceid =?");
				param.add(condition.get("branchSchool"));
			}
			sql.append("   group by u.unitname,u.resourceid ");
			sql.append("   order by u.unitname ");
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), param.toArray(),ExamPaperBagPrintInfoVo.class);
	}
	/**
	 * 分页统计预约考试情况        version 2  (预约学习、预约考试合并同一页面时的第二个版本)
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page statExamOrder(Map<String, Object> condition, Page objPage)throws ServiceException {
		
		StringBuffer sql    = new StringBuffer();
		List<Object> param  = new ArrayList<Object>();
		
		sql.append("  select   unit.resourceid as brschoolId,unit.unitName, course.resourceid as COURSEID, course.COURSENAME, ");
		//------------------------------------------------考试开始时间------------------------------------------------
		sql.append("  ( select info.examstarttime from edu_teach_examinfo info ,edu_teach_examsub sub  ");
		sql.append("     where info.isdeleted = 0 and info.examsubid = sub.resourceid and sub.isdeleted = 0 and sub.batchtype='exam' and sub.examsubstatus='2' ");
		sql.append("       and info.examcoursetype=0 ");//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约统计只统计网络教育考试课程类型
		if (condition.containsKey("yearInfo")) {
			sql.append("   and sub.yearid =?");
			param.add(condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append("   and sub.term   =? ");
			param.add(condition.get("term"));
		}
		if (condition.containsKey("course")) {
			sql.append("   and info.courseid = ? ");
			param.add(condition.get("course"));
		}
		sql.append("       and info.courseid = course.resourceid   ");
		sql.append("  )  starttime,");
		//------------------------------------------------考试开始时间------------------------------------------------
	
		//------------------------------------------------考试结束时间------------------------------------------------
		sql.append("  ( select info.examendtime from edu_teach_examinfo info ,edu_teach_examsub sub  ");
		sql.append("     where info.isdeleted = 0 and info.examsubid = sub.resourceid and sub.isdeleted = 0 and sub.batchtype='exam' and sub.examsubstatus='2' ");
		sql.append("       and info.examcoursetype=0 ");//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约统计只统计网络教育考试课程类型
		if (condition.containsKey("yearInfo")) {
			sql.append("   and sub.yearid =?");
			param.add(condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append("   and sub.term   =? ");
			param.add(condition.get("term"));
		}
		if (condition.containsKey("course")) {
			sql.append("   and info.courseid = ? ");
			param.add(condition.get("course"));
		}
		sql.append("       and info.courseid = course.resourceid   ");
		sql.append("  )  endtime,");
		//------------------------------------------------考试结束时间------------------------------------------------
		
		//------------------------------------------------考试编号------------------------------------------------
		sql.append("  ( select  info.examcoursecode from edu_teach_examinfo info ,edu_teach_examsub sub  ");
		sql.append("     where info.isdeleted = 0 and info.examsubid = sub.resourceid and sub.isdeleted = 0  and sub.batchtype='exam' and sub.examsubstatus='2' ");
		sql.append("       and info.examcoursetype=0 ");//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约统计只统计网络教育考试课程类型
		if (condition.containsKey("yearInfo")) {
			sql.append("   and sub.yearid =?");
			param.add(condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append("   and sub.term   =? ");
			param.add(condition.get("term"));
		}
		if (condition.containsKey("course")) {
			sql.append("   and info.courseid = ? ");
			param.add(condition.get("course"));
		}
		sql.append("       and info.courseid = course.resourceid   ");
		sql.append("  )   examcoursecode,");
		//------------------------------------------------考试编号------------------------------------------------
	
		sql.append("  f_dictionary('CodeCourseExamType',course.examtype) examType,count(ordercourse.resourceid) counts  ");
		sql.append("  from EDU_TEACH_ORDERCOURSE ordercourse  ");
		
		//------------------------------------------------inner join 条件------------------------------------------------
		sql.append("  inner join EDU_TEACH_COURSESTAT coursestat on ordercourse.COURSESTATID = coursestat.RESOURCEID  ");
		sql.append("  inner join EDU_ROLL_STUDENTINFO stu          on ordercourse.STUDENTID    = stu.resourceid     ");
		sql.append("  inner join hnjk_sys_unit unit                on stu.BRANCHSCHOOLID       = unit.resourceid	 ");
		if (condition.containsKey("branchSchool")) {
			sql.append("     and unit.resourceid=?	 ");
			param.add(condition.get("branchSchool"));
		}
		sql.append("  inner join EDU_BASE_COURSE course            on coursestat.COURSEID      = course.resourceid ");
		if (condition.containsKey("course")) {
			sql.append("     and course.resourceid = ? ");
			param.add(condition.get("course"));
		}
		//------------------------------------------------inner join 条件------------------------------------------------
		
		//------------------------------------------------主 条件------------------------------------------------
		sql.append(" where ordercourse.isDeleted=0   ");
		/*if (condition.containsKey("yearInfo")) {
			sql.append("   and coursestat.yearid =?");
			param.add(condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append("   and coursestat.term  =? ");
			param.add(condition.get("term"));
		}*/
		if (condition.containsKey("yearInfo")) {
			sql.append("   and ordercourse.orderexamyear =?");
			param.add(condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append("   and ordercourse.orderexamterm  =? ");
			param.add(condition.get("term"));
		}
		sql.append("   and ordercourse.status  = 2 ");
		
		if (condition.containsKey("startTime")) {
			sql.append("   and ordercourse.orderExamTime >= to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append("   and ordercourse.orderExamTime <= to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		//把下面导出的查询复制
		sql.append(" and exists(");
		sql.append(" 		 select i.resourceid,rs.resourceid from edu_teach_examresults rs ");
		sql.append("          inner join edu_teach_examinfo i on i.resourceid = rs.examinfoid  and i.isdeleted = 0 ");
		if (condition.containsKey("startTime")) {
			sql.append(" and i.examstarttime=to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append(" and i.examendtime=to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		sql.append("     inner join edu_teach_examsub sub on i.examsubid = sub.resourceid and sub.isdeleted = 0 and sub.batchtype='exam'  and sub.yearid =? and sub.term =? ");
		sql.append(" where  rs.isdeleted = 0  and rs.studentid = ordercourse.studentid and rs.courseid = coursestat.courseid ");
		sql.append(" )");
		param.add(condition.get("yearInfo"));
		param.add(condition.get("term"));
		//------------------------------------------------主 条件------------------------------------------------
		
		sql.append(" group by unit.UNITNAME,unit.resourceid,course.courseName , course.coursecode,course.resourceid,course.examtype  ");
		sql.append(" order by unit.UNITNAME,starttime,course.examtype desc");
		//sql.append(" order by unit.UNITNAME,course.examtype desc");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(), param.toArray(), ExamOrderStatExportVo.class);
	}
	
	/**
	 * 统计预约考试情况        version 2  (预约学习、预约考试合并同一页面时的第二个版本)
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamOrderStatExportVo> statExamOrder(Map<String, Object> condition)throws ServiceException {
		
		
		StringBuffer sql    	 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		
		sql.append("  select   unit.resourceid as brschoolId,unit.unitName, course.resourceid as COURSEID, course.COURSENAME,course.COURSECODE, ");
		if (!condition.containsKey("ismachineexam")||Constants.BOOLEAN_NO.equals(condition.get("ismachineexam").toString())) {
			//------------------------------------------------考试开始时间------------------------------------------------
			sql.append("  ( select info.examstarttime from edu_teach_examinfo info ,edu_teach_examsub sub  ");
			sql.append("     where info.isdeleted = 0 and info.examsubid = sub.resourceid and sub.isdeleted = 0  and sub.batchtype='exam' and sub.examsubstatus='2' ");
			sql.append("       and info.examcoursetype=0 ");//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约统计只统计网络教育考试课程类型
			if (condition.containsKey("yearInfo")) {
				sql.append("   and sub.yearid =:yearInfo");
				param.put("yearInfo", condition.get("yearInfo"));
			}
			if (condition.containsKey("term")) {
				sql.append("   and sub.term   =:term ");
				param.put("term", condition.get("term"));
			}
			if (condition.containsKey("course")) {
				sql.append("   and info.courseid in("+condition.get("course")+") ");
			}
			sql.append("       and info.courseid = course.resourceid   ");
			sql.append("  )  starttime,");
			//------------------------------------------------考试开始时间------------------------------------------------
			
			//------------------------------------------------考试结束时间------------------------------------------------
			sql.append("  ( select info.examendtime from edu_teach_examinfo info ,edu_teach_examsub sub  ");
			sql.append("     where info.isdeleted = 0 and info.examsubid = sub.resourceid and sub.isdeleted = 0  and sub.batchtype='exam' and sub.examsubstatus='2' ");
			sql.append("       and info.examcoursetype=0 ");//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约统计只统计网络教育考试课程类型
			if (condition.containsKey("yearInfo")) {
				sql.append("   and sub.yearid =:yearInfo");
				param.put("yearInfo", condition.get("yearInfo"));
			}
			if (condition.containsKey("term")) {
				sql.append("   and sub.term   =:term ");
				param.put("term", condition.get("term"));
			}
			if (condition.containsKey("course")) {
				sql.append("   and info.courseid in("+condition.get("course")+") ");
			}
			sql.append("       and info.courseid = course.resourceid   ");
			sql.append("  )  endtime,");
			//------------------------------------------------考试结束时间------------------------------------------------
			
			//------------------------------------------------考试编号------------------------------------------------
			sql.append("  ( select  info.examcoursecode from edu_teach_examinfo info ,edu_teach_examsub sub  ");
			sql.append("     where info.isdeleted = 0 and info.examsubid = sub.resourceid and sub.isdeleted = 0  and sub.batchtype='exam' and sub.examsubstatus='2' ");
			sql.append("       and info.examcoursetype=0 ");//12.4.26因机考课程导致预约统计出错,经与朝上讨论决定预约统计只统计网络教育考试课程类型
			if (condition.containsKey("yearInfo")) {
				sql.append("   and sub.yearid =:yearInfo");
				param.put("yearInfo", condition.get("yearInfo"));
			}
			if (condition.containsKey("term")) {
				sql.append("   and sub.term   =:term ");
				param.put("term", condition.get("term"));
			}
			if (condition.containsKey("course")) {
				sql.append("   and info.courseid in("+condition.get("course")+") ");
			}
			sql.append("       and info.courseid = course.resourceid   ");
			sql.append("  )   examcoursecode,");
			//------------------------------------------------考试编号------------------------------------------------
		}
		sql.append("  f_dictionary('CodeCourseExamType',course.examtype) examType,count(ordercourse.resourceid) counts  ");
		sql.append("  from EDU_TEACH_ORDERCOURSE ordercourse  ");
		
		//------------------------------------------------inner join 条件------------------------------------------------
		sql.append("  inner join EDU_TEACH_COURSESTAT coursestat on ordercourse.COURSESTATID = coursestat.RESOURCEID  ");
		sql.append("  inner join EDU_ROLL_STUDENTINFO stu          on ordercourse.STUDENTID    = stu.resourceid     ");
		sql.append("  inner join hnjk_sys_unit unit                on stu.BRANCHSCHOOLID       = unit.resourceid	 ");
		if (condition.containsKey("branchSchool")) {
			sql.append("     and unit.resourceid in("+condition.get("branchSchool")+")");
		}
		sql.append("  inner join EDU_BASE_COURSE course            on coursestat.COURSEID      = course.resourceid ");
		if (condition.containsKey("course")) {
			sql.append("   and course.resourceid in("+condition.get("course")+") ");
		}
		//------------------------------------------------inner join 条件------------------------------------------------
		
		//------------------------------------------------主 条件------------------------------------------------
		sql.append(" where ordercourse.isDeleted=0   ");
		if (condition.containsKey("yearInfo")) {
			sql.append("   and ordercourse.orderexamyear =:yearInfo");
			param.put("yearInfo", condition.get("yearInfo"));
		}
		if (condition.containsKey("term")) {
			sql.append("   and ordercourse.orderexamterm  =:term ");
			param.put("term", condition.get("term"));
		}
		sql.append("   and ordercourse.status  = 2 ");
		
		if (condition.containsKey("orderExamStartTime")) {
			sql.append("   and ordercourse.orderExamTime >= to_date('"+condition.get("orderExamStartTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("orderExamEndTime")) {
			sql.append("   and ordercourse.orderExamTime <= to_date('"+condition.get("orderExamEndTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("ismachineexam")) {
			sql.append("   and ordercourse.ismachineexam  =:ismachineexam ");
			param.put("ismachineexam", condition.get("ismachineexam"));
		}	
		sql.append(" and exists(");
		sql.append(" 		 select i.resourceid,rs.resourceid from edu_teach_examresults rs ");
		sql.append("          inner join edu_teach_examinfo i on i.resourceid = rs.examinfoid  and i.isdeleted = 0 ");
		if (condition.containsKey("startTime")) {
			sql.append(" and i.examstarttime=to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append(" and i.examendtime=to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		sql.append("     inner join edu_teach_examsub sub on i.examsubid = sub.resourceid and sub.isdeleted = 0 and sub.batchtype='exam'  and sub.yearid =:yearInfo and sub.term =:term");
		sql.append(" where  rs.isdeleted = 0  and rs.studentid = stu.resourceid and rs.courseid = course.resourceid ");
		sql.append(" )");
		//------------------------------------------------主 条件------------------------------------------------
		
		sql.append(" group by unit.UNITNAME,unit.resourceid,course.courseName , course.coursecode,course.resourceid,course.examtype  ");
		//按学习中心排序
		if ("unit".equals(condition.get("flag"))) {
			if (condition.containsKey("ismachineexam")&&Constants.BOOLEAN_YES.equals(condition.get("ismachineexam").toString())) {
				sql.append(" order by unit.UNITNAME,course.examtype desc");
			}else {
				sql.append(" order by unit.UNITNAME,starttime,course.examtype desc");
			}
			
		//按课程排序	
		}else if("course".equals(condition.get("flag"))) {
			if (condition.containsKey("ismachineexam")&&Constants.BOOLEAN_YES.equals(condition.get("ismachineexam").toString())) {
				sql.append(" order by course.coursename,course.examtype ");
			}else {
				sql.append(" order by course.coursename,starttime,course.examtype ");
			}
			
		//按考试时间排序	
		}else if("time".equals(condition.get("flag"))) {
			if (condition.containsKey("ismachineexam")&&Constants.BOOLEAN_YES.equals(condition.get("ismachineexam").toString())) {
				sql.append(" order by course.coursename, course.examtype ");
			}else {
				sql.append(" order by starttime,course.coursename, course.examtype ");
			}
			
		//导出汇总表	
		}else if("total".equals(condition.get("flag"))) {
			
			sql = new StringBuffer();
			
			sql.append(" select ( select info.examcoursecode from edu_teach_examinfo info ,edu_teach_examsub sub  ");
			sql.append("     where info.isdeleted = 0 and info.examcoursetype=0 and info.examsubid = sub.resourceid and sub.isdeleted = 0");
			if (condition.containsKey("yearInfo")) {
				sql.append("   and sub.yearid =:yearInfo");
				param.put("yearInfo", condition.get("yearInfo"));
			}
			if (condition.containsKey("term")) {
				sql.append("   and sub.term   =:term ");
				param.put("term", condition.get("term"));
			}
			if (condition.containsKey("course")) {
				sql.append("   and info.courseid in("+condition.get("course")+") ");
			}
			sql.append("       and info.courseid = course.resourceid   ");
			sql.append("  )   examcoursecode,");
			sql.append("  course.COURSENAME,count(ordercourse.resourceid) counts ");
			
			sql.append("  from EDU_TEACH_ORDERCOURSE ordercourse ");
			sql.append(" inner join EDU_TEACH_COURSESTAT coursestat on ordercourse.COURSESTATID = coursestat.RESOURCEID  ");
			sql.append(" inner join EDU_ROLL_STUDENTINFO stu          on ordercourse.STUDENTID    = stu.resourceid     ");
			sql.append(" inner join hnjk_sys_unit unit                on stu.BRANCHSCHOOLID       = unit.resourceid	 ");
			if (condition.containsKey("branchSchool")) {
				sql.append("     and unit.resourceid in("+condition.get("branchSchool")+")");
			}
			sql.append("       join EDU_BASE_COURSE course   on coursestat.COURSEID      = course.resourceid ");

			sql.append(" where ordercourse.isDeleted=0   ");
			if (condition.containsKey("yearInfo")) {
				sql.append("   and ordercourse.orderexamyear =:yearInfo");
				param.put("yearInfo", condition.get("yearInfo"));
			}
			if (condition.containsKey("term")) {
				sql.append("   and ordercourse.orderexamterm  =:term ");
				param.put("term", condition.get("term"));
			}
			sql.append("   and ordercourse.status  = 2 ");
			//不知为何 之前total这里就没有这一段
			if (condition.containsKey("yearInfo")&&condition.containsKey("term")) {
			sql.append(" and exists(");
			sql.append(" 		 select i.resourceid,rs.resourceid from edu_teach_examresults rs ");
			sql.append("          inner join edu_teach_examinfo i on i.resourceid = rs.examinfoid  and i.isdeleted = 0 ");
			if (condition.containsKey("startTime")) {
				sql.append(" and i.examstarttime=to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
			}
			if (condition.containsKey("endTime")) {
				sql.append(" and i.examendtime=to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
			}
			sql.append("     inner join edu_teach_examsub sub on i.examsubid = sub.resourceid and sub.isdeleted = 0 and sub.batchtype='exam'  and sub.yearid =:yearInfo and sub.term =:term");
			sql.append(" where  rs.isdeleted = 0  and rs.studentid = stu.resourceid and rs.courseid = course.resourceid ");
			sql.append(" )");
			}
			sql.append(" group by course.COURSENAME, course.resourceid ");
			sql.append("   order by course.courseName ");
		}

		try {
			List<ExamOrderStatExportVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), ExamOrderStatExportVo.class, condition);
			if (condition.containsKey("ismachineexam")&& Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(condition.get("ismachineexam").toString()))) {
				for (ExamOrderStatExportVo vo: list) {
					vo.setEXAMCOURSECODE("Z"+vo.getCOURSECODE());
					vo.setEXAMTYPE("机考");
				}
			}
			return list;
		} catch (Exception e) {
			
		}
		return new ArrayList<ExamOrderStatExportVo>();
	}
	
	/**
	 * 分页统计统考报名考试情况   
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page statStateExamOrder(Map<String, Object> condition, Page objPage)throws ServiceException {
		
		StringBuffer sql    = new StringBuffer();
		List<Object> param  = new ArrayList<Object>();
		
		sql.append("  select st.branchschoolid brschoolId,u.unitname unitName,pc.courseid COURSEID,c.coursename COURSENAME ");
		sql.append("  ,ei.examstarttime starttime ,ei.examendtime endtime,ei.examcoursecode "); 
		sql.append("  examcoursecode,f_dictionary('CodeCourseExamType',c.examtype) examType,count(distinct st.resourceid) counts ");
		sql.append("  from edu_learn_stuplan stp,edu_roll_studentinfo st , ");
		sql.append("  edu_teach_examinfo ei,edu_teach_plancourse pc, ");
		sql.append("  hnjk_sys_unit u,edu_base_course c ");
		sql.append("  where stp.studentid =st.resourceid "); 
		sql.append("  and st.branchschoolid = u.resourceid  ");
		sql.append("  and stp.plansourceid = pc.resourceid  ");
		sql.append("  and pc.courseid = c.resourceid ");
		sql.append("  and stp.examinfoid = ei.resourceid "); 
		sql.append("  and ei.isdeleted = 0 ");
		sql.append("  and stp.status =2 ");
		sql.append("  and stp.isdeleted = 0 ");
		sql.append("  and (pc.examclasstype = 'unified' or pc.courseNature =  '1100') ");
		if (condition.containsKey("branchSchool")) {
			sql.append("     and u.resourceid=?	 ");
			param.add(condition.get("branchSchool"));
		}
		if (condition.containsKey("course")) {
			sql.append("     and c.resourceid = ? ");
			param.add(condition.get("course"));
		}
		if (condition.containsKey("examsub")) {
			sql.append("     and ei.examsubid = ? ");
			param.add(condition.get("examsub"));
		}
		if (condition.containsKey("startTime")) {
			sql.append("   and ei.examstarttime >= to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append("   and ei.examendtime <= to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}

		sql.append(" group by st.branchschoolid ,u.unitname ,pc.courseid ,c.coursename ,ei.examstarttime  ,ei.examendtime ,ei.examcoursecode ,c.examtype  ");
		sql.append(" order by u.unitname,ei.examstarttime,c.examtype desc");
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(objPage, sql.toString(), param.toArray(), ExamOrderStatExportVo.class);
	}
	/**
	 * 统考的课程列表   
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Map<String,Object>> courseOption(Map<String, Object> condition)throws ServiceException{
		
		StringBuffer sql    = new StringBuffer();
		
		sql.append("  select distinct c.coursecode,c.coursename,c.resourceid  ");
		sql.append(" from edu_teach_plancourse pc ,edu_base_course c  ");
		sql.append(" where pc.isdeleted = 0 and c.isdeleted= 0 and pc.courseid = c.resourceid and (pc.examClassType = 'unified' or pc.courseNature = '1100') ");
		sql.append(" order by c.coursecode asc ");		
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(0);
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), null);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return list;
	}
	/**
	 * 统计统考报名考试情况
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamOrderStatExportVo> statStateExamOrder(Map<String, Object> condition)throws ServiceException {
		
		
		StringBuffer sql    	 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>(0);
		
		sql.append("  select st.branchschoolid brschoolId,u.unitname unitName,pc.courseid COURSEID,c.coursename COURSENAME ");
		sql.append("  ,ei.examstarttime starttime ,ei.examendtime endtime,ei.examcoursecode "); 
		sql.append("  examcoursecode,f_dictionary('CodeCourseExamType',c.examtype) examType,count(distinct  st.resourceid)  counts");
		sql.append("  from edu_learn_stuplan stp,edu_roll_studentinfo st , ");
		sql.append("  edu_teach_examinfo ei,edu_teach_plancourse pc, ");
		sql.append("  hnjk_sys_unit u,edu_base_course c ");
		sql.append("  where stp.studentid =st.resourceid "); 
		sql.append("  and st.branchschoolid = u.resourceid  ");
		sql.append("  and stp.plansourceid = pc.resourceid  ");
		sql.append("  and pc.courseid = c.resourceid ");
		sql.append("  and stp.examinfoid = ei.resourceid "); 
		sql.append("  and ei.isdeleted = 0 ");
		sql.append("  and stp.status =2 ");
		sql.append("  and stp.isdeleted = 0 ");
		sql.append("  and (pc.examclasstype = 'unified' or pc.courseNature =  '1100') ");
		
		if (condition.containsKey("examsub")) {
			sql.append("     and ei.examsubid = :examsub ");
			param.put("examsub",condition.get("examsub"));
		}
		if (condition.containsKey("branchSchool")) {
			sql.append("     and u.resourceid in("+condition.get("branchSchool")+")");
		}
		if (condition.containsKey("course")) {
			sql.append("   and c.resourceid in("+condition.get("course")+") ");
		}
		if (condition.containsKey("startTime")) {
			sql.append(" and ei.examstarttime=to_date('"+condition.get("startTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		if (condition.containsKey("endTime")) {
			sql.append(" and ei.examendtime=to_date('"+condition.get("endTime")+"','yyyy-MM-dd HH24:mi:ss') ");
		}
		sql.append(" group by st.branchschoolid ,u.unitname ,pc.courseid ,c.coursename ,ei.examstarttime  ,ei.examendtime ,ei.examcoursecode ,c.examtype  ");
		//按学习中心排序
		if ("unit".equals(condition.get("flag"))) {
			if (condition.containsKey("ismachineexam")&&Constants.BOOLEAN_YES.equals(condition.get("ismachineexam").toString())) {
				sql.append(" order by u.UNITNAME,c.examtype desc");
			}else {
				sql.append(" order by u.UNITNAME,ei.examstarttime,c.examtype desc");
			}
			
		//按课程排序	
		}else if("course".equals(condition.get("flag"))) {
			if (condition.containsKey("ismachineexam")&&Constants.BOOLEAN_YES.equals(condition.get("ismachineexam").toString())) {
				sql.append(" order by c.coursename,c.examtype ");
			}else {
				sql.append(" order by c.coursename,ei.examstarttime,c.examtype ");
			}
			
		//按考试时间排序	
		}else if("time".equals(condition.get("flag"))) {
			if (condition.containsKey("ismachineexam")&&Constants.BOOLEAN_YES.equals(condition.get("ismachineexam").toString())) {
				sql.append(" order by c.coursename, c.examtype ");
			}else {
				sql.append(" order by ei.examstarttime,c.coursename, c.examtype ");
			}
		//导出汇总表	
		}

		try {
			List<ExamOrderStatExportVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), ExamOrderStatExportVo.class, condition);
			if (condition.containsKey("ismachineexam")&& Constants.BOOLEAN_YES.equals(ExStringUtils.trimToEmpty(condition.get("ismachineexam").toString()))) {
				for (ExamOrderStatExportVo vo: list) {
					vo.setEXAMCOURSECODE("Z"+vo.getCOURSECODE());
					vo.setEXAMTYPE("机考");
				}
			}
			return list;
		} catch (Exception e) {
			
		}
		return new ArrayList<ExamOrderStatExportVo>();
	}
	
	/**
	 * 统计座位安排情况(期末考试)
	 * @param condition
	 * @return
	 */
	@Override
	public Page examSeatAssignInfo(Page page,Map<String, Object> condition) {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql   = new StringBuffer();
		param.put("isDeleted", 0);
		param.put("checkstatus",-1);
		//学习中心统计
		if (Constants.BOOLEAN_YES.equals(condition.get("isBrschool").toString())) {
			sql.append(" select u.unitname,c.coursename,info.examstarttime,info.examendtime ,count(rs.resourceid) as orderNum,");
			//--------------------------------------------查询课程已安排座位数--------------------------------------------
			sql.append("  ( select count(rs.resourceid) from EDU_TEACH_EXAMRESULTS rs ");
			sql.append("    inner join edu_roll_studentinfo stu on stu.resourceid = rs.studentid  and stu.isdeleted=:isDeleted ");
			sql.append("    inner join hnjk_sys_unit u on stu.branchschoolid = u.resourceid  and u.resourceid =:branchSchool");
			param.put("branchSchool",condition.get("branchSchool"));
			sql.append("    inner join edu_teach_examinfo i on rs.examinfoid   = i.resourceid   ");
			sql.append("           and i.examstarttime = :examStartDate and i.examendtime = :examEndDate and i.examsubid  = :examSubId");
			param.put("examStartDate",condition.get("examStartDate"));
			param.put("examEndDate",condition.get("examEndDate"));
			param.put("examSubId",condition.get("examSubId"));
			sql.append("    where rs.isdeleted =:isDeleted  and rs.classroomid is not null  and rs.examseatnum is not null and rs.checkstatus =:checkstatus");
			sql.append("      and rs.examinfoid = info.resourceid");
			sql.append(" )as assigned,");
			//--------------------------------------------查询课程已安排座位数--------------------------------------------
			
			//--------------------------------------------查询学习中心所有已安排座位总数--------------------------------------------
			sql.append(" ( select count(rs.resourceid)  from EDU_TEACH_EXAMRESULTS rs  ");
			sql.append("    inner join edu_roll_studentinfo stu on stu.resourceid = rs.studentid  and stu.isdeleted=:isDeleted  ");
			sql.append("    inner join edu_teach_examinfo i on rs.examinfoid   = i.resourceid   ");
			sql.append("          and i.examstarttime = :examStartDate and i.examendtime = :examEndDate and i.examsubid  = :examSubId");
			param.put("examStartDate",condition.get("examStartDate"));
			param.put("examEndDate",condition.get("examEndDate"));
			param.put("examSubId",condition.get("examSubId"));
			sql.append("    where rs.isdeleted =:isDeleted and  rs.classroomid is not null and rs.examseatnum is not null and rs.checkstatus = :checkstatus");
			sql.append("      and stu.branchschoolid = :branchSchool");
			param.put("branchSchool",condition.get("branchSchool"));
			sql.append(" )as totalAssigned,");
			//--------------------------------------------查询学习中心所有已安排座位总数--------------------------------------------
			
			//--------------------------------------------查询已安排记录--------------------------------------------
			//--------------------------------------------查询学习中心座位总量--------------------------------------------
			sql.append("( select nvl(sum(r.doubleseatnum),0) from  EDU_BASE_EXAMROOM r where r.isdeleted =:isDeleted and r.branchschoolid = u.resourceid )as totalSeat");
			//--------------------------------------------查询学习中心座位总量--------------------------------------------
			sql.append("       from EDU_TEACH_EXAMRESULTS rs ");
			sql.append(" inner join edu_roll_studentinfo stu on stu.resourceid = rs.studentid  and stu.isdeleted=:isDeleted ");
			sql.append(" inner join hnjk_sys_unit u on stu.branchschoolid = u.resourceid  and u.resourceid = :branchSchool ");
			param.put("branchSchool",condition.get("branchSchool"));
			sql.append(" inner join edu_teach_examinfo info on rs.examinfoid   = info.resourceid   and info.isdeleted =:isDeleted");
			sql.append("        and info.examstarttime = :examStartDate and info.examendtime = :examEndDate and info.examsubid  = :examSubId");
			param.put("examStartDate",condition.get("examStartDate"));
			param.put("examEndDate",condition.get("examEndDate"));
			param.put("examSubId",condition.get("examSubId"));
			sql.append(" inner join edu_base_course c on info.courseid = c.resourceid and c.isdeleted =:isDeleted   ");
			sql.append(" where rs.isdeleted =:isDeleted ");
			sql.append("   and rs.checkstatus = :checkstatus");
			sql.append(" group by u.unitname,u.resourceid,info.examstarttime,info.examendtime,c.coursename,info.resourceid");
			
		//考务办统计	
		}else {
			sql.append(" select unit.unitname,");
			//--------------------------------------------查询预约人数--------------------------------------------
			sql.append(" 		( select count(rs.resourceid) from EDU_TEACH_EXAMRESULTS rs ");
			sql.append("  		   inner join edu_roll_studentinfo stu on stu.resourceid = rs.studentid  and stu.isdeleted=:isDeleted ");
			sql.append("           inner join edu_teach_examinfo i on rs.examinfoid   = i.resourceid  ");
			sql.append("       			  and i.examstarttime = :examStartDate and i.examendtime = :examEndDate and i.examsubid  = :examSubId");
			param.put("examStartDate",condition.get("examStartDate"));
			param.put("examEndDate",condition.get("examEndDate"));
			param.put("examSubId",condition.get("examSubId"));
			sql.append(" 				 where rs.isdeleted = :isDeleted and rs.checkstatus = :checkstatus");
			sql.append("                   and stu.branchschoolid = unit.resourceid");
			sql.append("         )as orderNum,");
			//--------------------------------------------查询预约人数--------------------------------------------
			//--------------------------------------------查询已安排记录--------------------------------------------
			sql.append("        ( select count(rs.resourceid) from EDU_TEACH_EXAMRESULTS rs ");
			sql.append(" 		   inner join edu_roll_studentinfo stu on stu.resourceid = rs.studentid  and stu.isdeleted=:isDeleted ");
			sql.append("           inner join edu_teach_examinfo i on rs.examinfoid   = i.resourceid and i.isdeleted=:isDeleted ");
			sql.append("        		  and i.examstarttime = :examStartDate and i.examendtime = :examEndDate and i.examsubid  = :examSubId");
			param.put("examStartDate",condition.get("examStartDate"));
			param.put("examEndDate",condition.get("examEndDate"));
			param.put("examSubId",condition.get("examSubId"));
			sql.append("          where rs.isdeleted = :isDeleted  and rs.classroomid is not null  and rs.examseatnum is not null and rs.checkstatus =:checkstatus");
			sql.append("            and stu.branchschoolid = unit.resourceid");
			sql.append("        )as assigned,");
			//--------------------------------------------查询已安排记录--------------------------------------------
			//--------------------------------------------查询学习中心座位总量--------------------------------------------
			sql.append("( select nvl(sum(r.doubleseatnum),0) from  EDU_BASE_EXAMROOM r where r.isdeleted =:isDeleted and r.branchschoolid = unit.resourceid )as totalSeat");
			//--------------------------------------------查询学习中心座位总量--------------------------------------------
			
			sql.append(" from hnjk_sys_unit unit where unit.isdeleted =:isDeleted  and unit.status=:unitStatus  and unit.unittype =:unittype  ");
			if (condition.containsKey("branchSchool")) {
				sql.append(" and unit.resourceid=:branchSchool");
				param.put("branchSchool", condition.get("branchSchool"));
			}
			sql.append(" order by unit.unitcode ");
			param.put("unitStatus","normal");
			param.put("unittype","brSchool");
		
		}
		
		return findBySql(page, sql.toString(), param);
	}
	
	@Override
	public List<ExamResultsVo> findFaceStudyExamResultsVo(Map<String, Object> condition) throws ServiceException {
		List<ExamResultsVo> list = new ArrayList<ExamResultsVo>();
		StringBuffer sql = getFacestudyExamResultsSql(condition);
		sql.append(" order by unit.unitcode,major.majorcode,stu.studyno asc");	
		try {
			List<Map<String, Object>> resultList =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
			if(ExCollectionUtils.isNotEmpty(resultList)){
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> map = resultList.get(i);
					String studentid = map.get("studentid")==null?"":map.get("studentid").toString();
					ExamResultsVo vo = new ExamResultsVo();
					vo.setSort(Integer.toString(i+1));
					vo.setBranchSchool(map.get("unitname")==null?"":map.get("unitname").toString());
					vo.setMajor(map.get("majorname")==null?"":map.get("majorname").toString());
					vo.setStudyNo(map.get("studyno")==null?"":map.get("studyno").toString());
					vo.setName(map.get("studentname")==null?"":map.get("studentname").toString());
					//班级
					vo.setClasses(map.get("classesname")==null?"":map.get("classesname").toString());
					vo.setCheckStatus(map.get("checkStatus")==null?"":map.get("checkStatus").toString());;
					if(!(condition.containsKey("flag")&&"0".equals(condition.get("flag")))){
						vo.setExamAbnormity(map.get("examabnormity")==null?"":map.get("examabnormity").toString());
						if(!"0".equals(vo.getExamAbnormity())){
							vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", ExStringUtils.trimToEmpty(vo.getExamAbnormity())));
							vo.setIntegratedScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", ExStringUtils.trimToEmpty(vo.getExamAbnormity())));
						} else {
							vo.setWrittenScore(map.get("writtenscore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("writtenscore").toString()));
							vo.setIntegratedScore(map.get("integratedscore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("integratedscore").toString()));
						}
						// 网上学习成绩（作为平时成绩）
						if(ExStringUtils.isBlank(vo.getCheckStatus())){
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("courseid", condition.get("courseId"));
							params.put("stuid", studentid);
							params.put("yearid", condition.get("yearid"));
							params.put("term", condition.get("term"));
							String score = getUsualResultByStudent(params);
							vo.setUsuallyScore(ExStringUtils.trimToEmpty(score));
						}else {
							vo.setUsuallyScore(map.get("usuallyscore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("usuallyscore").toString()));
						}
					} 
					list.add(vo);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	private StringBuffer getFacestudyExamResultsSql(Map<String, Object> condition) {
		StringBuffer sql    	 = new StringBuffer();
		/*condition.put("studentStatus1", "11");
		condition.put("studentStatus2", "21");*/
		condition.put("isPass",Constants.BOOLEAN_YES);
		sql.append(" select max(unit.unitname) unitname,max(major.majorname) majorname,max(stu.studyno) studyno,max(stu.studentname) studentname,max(cl.classesname) classesname, ");
		sql.append("        stu.resourceid studentid,max(r.writtenScore) writtenScore,max(r.usuallyScore) usuallyScore,max(r.onlineScore) onlineScore,max(r.integratedscore) integratedscore,max(r.courseid) courseid,max(r.resourceid) resultid, ");
		sql.append("        max(r.examinfoid) examinfoid,max(r.examsubid) examsubid,max(r.majorcourseid) majorcourseid,max(r.examabnormity) examabnormity,max(r.courseScoreType) courseScoreType,max(r.checkStatus) checkStatus,max(stu.studentstatus) studentstatus ");
		sql.append(" from edu_roll_studentinfo stu ");
		sql.append(" join hnjk_sys_unit unit on stu.branchschoolid=unit.resourceid ");
		sql.append(" join edu_base_major major on stu.majorid=major.resourceid ");
		//新增班级
		sql.append(" join edu_roll_classes cl  on stu.classesid = cl.resourceid and cl.isdeleted = 0 ");
		sql.append(" left join ( select * from edu_teach_examresults results where results.isdeleted=0 and results.courseid=:courseId");
//		sql.append(" left join ( select * from edu_teach_examresults results where results.isdeleted=0 and results.courseid=:courseId  and results.majorcourseid =:teachingPlanCourseId ");
		if (condition.containsKey("examSubId")) {
			sql.append(" and results.examsubid=:examSubId ");
		}
		sql.append(") r on stu.resourceid=r.studentid ");
		sql.append(" where stu.isdeleted=0 ");   
		sql.append(" and exists ( select * from edu_teach_plancourse pc  where pc.isdeleted=0 and pc.planid=stu.teachplanid and pc.teachtype=:teachType ) "); 
		
		
		
		if(condition.containsKey("teachplanid")){
			sql.append(" and stu.teachplanid=:teachplanid "); 
		}	
		if(condition.containsKey("gradeid")){
			sql.append(" and stu.gradeid=:gradeid "); 
		}
		if(condition.containsKey("branchschoolid")){
			sql.append(" and stu.branchschoolid=:branchschoolid "); 
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and stu.studyNo like :stuNo "); 
			condition.put("stuNo", "%"+condition.get("studyNo").toString().trim()+"%");
		}
		if(condition.containsKey("studentName")){
			sql.append(" and stu.studentName like :stuName ");
			condition.put("stuName", "%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("checkStatus")){
			if("-1".equals(condition.get("checkStatus"))){
				sql.append(" and r.checkstatus=:checkStatus or r.checkstatus is null ");
			}else{
				sql.append(" and r.checkstatus=:checkStatus ");
			} 
		}
		if(condition.containsKey("type")){
			if("fail".equals(condition.get("type"))){
				sql.append(" and ( ( f_decrypt_score( r.integratedscore,stu.resourceid) < 60 or r.examabnormity = 2 or r.examabnormity = 3 or r.examabnormity = 4 ) or r.examcount >=2 ) ");
				
			}
		}
		if(condition.containsKey("uncommit")){
			sql.append(" and (cast (r.checkstatus as int )<1  or r.checkstatus is null)"); 
		}
		//如果有分班信息
		if(condition.containsKey("classesId")){
			sql.append(" and stu.classesid = :classesId ");
		}
		//过滤在学或延期的学生且未通过此课程
//		sql.append(" and (stu.studentstatus = :studentStatus1 or stu.studentstatus = :studentStatus2 ) ");
		sql.append(" and stu.studentstatus in ('11','21','25') ");
		sql.append(" and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =:courseId and score.ispass = :isPass )");
//		sql.append(" and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =:courseId and score.ispass = :isPass )");
//		
//		sql.append(" and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =:courseId and score.ispass = :isPass )");
//		
//		List<NoExamApply> list1 = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()+" where isDeleted=? and studentInfo.resourceid=? and checkStatus=?  and course.resourceid=? ", 0,studentid,"1",courseId);
//		if(CacheAppManager.getSysConfigurationByCode("examsfYorN").getParamValue().equals("N")&&list1.size()>0){
		
		if(CacheAppManager.getSysConfigurationByCode("examsfYorN")!=null&& "N".equals(CacheAppManager.getSysConfigurationByCode("examsfYorN").getParamValue())){
			sql.append(" and not exists(select * from EDU_TEACH_NOEXAM no where no.isDeleted=0 and no.STUDENTID=stu.resourceid and no.checkStatus='1'  and no.COURSEID=:courseId ) ");
			
		}
		/*if (condition.containsKey("examSubId")) {
			if(condition.containsKey("teachingPlanCourseId")){
				sql.append(" and not exists (select * from edu_teach_examresults results where results.isdeleted = 0 and results.courseid = :courseId and results.examsubid < :examSubId and stu.resourceid = results.studentid  and results.majorcourseid=:teachingPlanCourseId) "); //匹配教学计划课程
			}else{
				sql.append(" and not exists (select * from edu_teach_examresults results where results.isdeleted = 0 and results.courseid = :courseId and results.examsubid < :examSubId and stu.resourceid = results.studentid) "); 
			}
		}*/
		sql.append(" group by stu.resourceid,unit.unitcode,major.majorcode,stu.studyno");
		return sql;
	}
	
	/* 补考sql */
	@Override
	public StringBuffer getNonFacestudyExamResultsSql(Map<String, Object> condition) {
		StringBuffer sql    	 = new StringBuffer();
//		condition.put("studentStatus1", "11");
//		condition.put("studentStatus2", "21");
//		condition.put("isPass",Constants.BOOLEAN_NO);
//		sql.append(" select unit.unitname,major.majorname,stu.studyno,stu.studentname, ");
//		sql.append("        stu.resourceid studentid,stu.studentstatus ");
//		sql.append(",edumakeup.writtenScore writtenScoreMakeup,edumakeup.usuallyScore usuallyScoreMakeup ");
//		sql.append(",edumakeup.integratedscore integratedscoreMakeup,edumakeup.courseid courseidMakeup,edumakeup.resourceid resultidMakeup");
//		sql.append(",edumakeup.examinfoid examinfoidMakeup,edumakeup.examsubid examsubidMakeup,edumakeup.majorcourseid majorcourseidMakeup");
//		sql.append(",edumakeup.examabnormity examabnormityMakeup,edumakeup.checkStatus checkStatusMakeup");
//		
//		sql.append(" from edu_roll_studentinfo stu ");
//		sql.append(" join hnjk_sys_unit unit on stu.branchschoolid=unit.resourceid ");
//		sql.append(" join edu_base_major major on stu.majorid=major.resourceid ");
//		sql.append(" left join ( select * from edu_teach_examresults results where results.isdeleted=0 and results.courseid=:courseId and results.examsubid=:examSubId ) r on stu.resourceid=r.studentid ");
		
		//后面修改
//		if(condition.containsKey("checkStatus")){
//			if("-1".equals(condition.get("checkStatus"))){
//				sql.append(" checkstatus=:checkStatus or checkstatus is null ");
//			}else{
//				sql.append(" checkstatus=:checkStatus ");
//			} 
//		} else {
//			sql.append(" checkstatus='0' or checkstatus='1' or checkstatus is null ");
//		}
		
//		sql.append(" where stu.isdeleted=0 ");   
//		
//		//查出不及格的学生
//		sql.append("  and  (( f_decrypt_score( r.integratedscore,stu.resourceid) < 60 or r.examabnormity = 2 or r.examabnormity = 3 or r.examabnormity = 4 ) )");// or r.examcount >=2
//		sql.append("   and not exists(select r.courseid from edu_teach_noexam ne where r.studentid = ne.studentid and r.courseid = ne.courseid and ne.checkstatus='1')");//==2-28
//		
//		
//		if(condition.containsKey("checkStatus")){
//			if("-1".equals(condition.get("checkStatus"))){
//				sql.append(" and edumakeup.checkstatus=:checkStatus or edumakeup.checkstatus is null ");
//			}else{
//				sql.append(" and edumakeup.checkstatus=:checkStatus ");
//			} 
//		}
//		
//		sql.append(" and exists ( select * from edu_teach_plancourse pc where pc.isdeleted=0 and pc.planid=stu.teachplanid and pc.teachtype='facestudy' ) "); 
////		sql.append("   and not exists(select r.courseid from edu_teach_noexam ne where r.studentid = ne.studentid and r.courseid = ne.courseid and ne.checkstatus='1')");//==2-28
//	
//		if(condition.containsKey("teachplanid")){
//			sql.append(" and stu.teachplanid=:teachplanid "); 
//		}	
//		if(condition.containsKey("gradeid")){
//			sql.append(" and stu.gradeid=:gradeid "); 
//		}
//		if(condition.containsKey("branchschoolid")){
//			sql.append(" and stu.branchschoolid=:branchschoolid "); 
//		}
//		if(condition.containsKey("studyNo")){
//			sql.append(" and stu.studyNo=:studyNo "); 
//		}
//		if(condition.containsKey("studentName")){
//			sql.append(" and stu.studentName like :stuName ");
//			condition.put("stuName", "%"+condition.get("studentName")+"%");
//		}
//		
//		if(condition.containsKey("uncommit")){
//			sql.append(" and (cast (edumakeup.checkstatus as int )<1  or edumakeup.checkstatus is null)"); 
//		}
//		//如果有分班信息
//		if(condition.containsKey("classesId")){
//			sql.append(" and stu.classesid = :classesId ");
//		}
//		//过滤在学或延期的学生且未通过此课程
//		sql.append(" and (stu.studentstatus = :studentStatus1 or stu.studentstatus = :studentStatus2 ) ");
		
		/*
		sql.append("select  unit.unitname,major.majorname,stu.studyno,stu.studentname,")
		.append(" stu.resourceid studentid,stu.studentstatus ,edumakeup.writtenScore writtenScoreMakeup,")
		.append(" edumakeup.usuallyScore usuallyScoreMakeup ,edumakeup.integratedscore integratedscoreMakeup,edumakeup.courseid courseidMakeup,")
		.append(" edumakeup.resourceid resultidMakeup,edumakeup.examinfoid examinfoidMakeup,edumakeup.examsubid examsubidMakeup,")
		.append(" edumakeup.majorcourseid majorcourseidMakeup,edumakeup.examabnormity examabnormityMakeup,edumakeup.checkStatus checkStatusMakeup")
		.append(" from edu_learn_stuplan sp join edu_teach_plancourse pc on sp.plansourceid=pc.resourceid ")
		.append(" join edu_roll_studentinfo stu on stu.resourceid=sp.studentid join edu_teach_plan pl on pl.resourceid=stu.teachplanid and pl.resourceid=pc.planid")
		.append(" join hnjk_sys_unit unit on stu.branchschoolid=unit.resourceid")
		.append(" join edu_base_major major on stu.majorid=major.resourceid")
		.append(" join edu_base_course c on pc.courseid = c.resourceid")
		.append(" left join ( select * from edu_teach_examresults results where results.isdeleted=0");
		
		if(condition.containsKey("courseId")){
			sql.append(" and results.courseid = :courseId ");
		}
		if(condition.containsKey("examSubId")){
			sql.append(" and results.examsubid = :examSubId ");
		} 
		
		sql.append(") edumakeup on stu.resourceid=edumakeup.studentid"); 
		
		sql.append(" where sp.isdeleted=0 and pc.isdeleted=0 and pc.teachtype='facestudy' and sp.ispass='N' and stu.isdeleted=0 and pl.isdeleted=0 and major.isdeleted=0 ");
//		.append(" exists ( select pc.courseid from edu_teach_plancourse pc where pc.isdeleted=0 and pc.planid=stu.teachplanid and pc.teachtype='facestudy' )")
//		.append(" and exists (select * from edu_learn_stuplan sp  where sp.isdeleted=0 and sp.studentid=stu.resourceid and sp.ispass='N')"); 

		if(condition.containsKey("checkStatus")){
			if("-1".equals(condition.get("checkStatus"))){
				sql.append(" and edumakeup.checkstatus=:checkStatus or edumakeup.checkstatus is null ");
			}else{
				sql.append(" and edumakeup.checkstatus=:checkStatus ");
			} 
		}
		
		if(condition.containsKey("teachplanid")){
			sql.append(" and stu.teachplanid=:teachplanid "); 
		}	
		if(condition.containsKey("gradeid")){
			sql.append(" and stu.gradeid=:gradeid "); 
		}
		if(condition.containsKey("branchschoolid")){
			sql.append(" and stu.branchschoolid=:branchschoolid "); 
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and stu.studyNo=:studyNo "); 
		}
		if(condition.containsKey("studentName")){
			sql.append(" and stu.studentName like :stuName ");
			condition.put("stuName", "%"+condition.get("studentName")+"%");
		}
		
		if(condition.containsKey("uncommit")){
			sql.append(" and (cast (edumakeup.checkstatus as int )<1  or edumakeup.checkstatus is null)"); 
		}
		//如果有分班信息
		if(condition.containsKey("classesId")){
			sql.append(" and stu.classesid = :classesId ");
		}
		
		sql.append(" and (stu.studentstatus = '11' or stu.studentstatus = '21' )");
		if(condition.containsKey("courseId")){
			sql.append(" and pc.courseid = :courseId ");
		}
		if(condition.containsKey("teachingPlanCourseId")){
			sql.append(" and pc.resourceid = :teachingPlanCourseId ");
		}
		
		sql.append("and not exists(select pc.courseid from edu_teach_noexam ne where ne.isdeleted=0 and sp.studentid = ne.studentid and pc.courseid = ne.courseid and ne.checkstatus='1')");
		*/
		
//		<th width="15%">课程名称</th>            
//        <th width="10%">教学站</th>            
//        <th width="18%">专业</th>                   
//        <th width="12%">学号</th>
//        <th width="9%">姓名</th>
//        <th width="9%">卷面成绩</th>	 
//        <th width="9%">综合成绩</th>	
//        <th width="7%">成绩异常</th>   
//        <th width="7%">录入状态</th> 

		
		  sql.append("select max(unit.unitname) unitname,max(c.coursename) coursename,max(a.courseid) courseid,max(major.majorname) majorname,max(stu.studyno) studyno,max(stu.studentname) studentname,max(a.plansourceid) plansourceid,")
		.append(" max(stu.resourceid) studentid,max(stu.studentstatus),max(edumakeup.writtenScore) writtenScoreMakeup,")
		.append(" max(edumakeup.usuallyScore) usuallyScoreMakeup ,max(edumakeup.integratedscore) integratedscoreMakeup,max(edumakeup.courseid) courseidMakeup,max(a.isMachineExam) isMachineExam,")
		.append(" max(edumakeup.resourceid) resultidMakeup,max(edumakeup.examinfoid) examinfoidMakeup,max(edumakeup.examsubid) examsubidMakeup,max(stu.branchschoolid) branchSchool,  ")
		.append(" max(edumakeup.majorcourseid) majorcourseidMakeup,max(edumakeup.examabnormity) examabnormityMakeup,max(edumakeup.checkStatus) checkStatusMakeup")
		.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid=a.studentid  ")
		.append(" join hnjk_sys_unit unit on stu.branchschoolid=unit.resourceid")
		.append(" join edu_base_major major on stu.majorid=major.resourceid")
		.append(" join edu_base_course c on a.courseid = c.resourceid   ");
		
//		.append(" join edu_teach_plancourse tp on tp.resourceid = a.plansourceid")

			sql.append(" left join (select * from edu_teach_examresults re where re.isdeleted=0 and re.examsubid = :examSubId and re.courseid=:courseid) edumakeup on stu.resourceid=edumakeup.studentid ");
			sql.append("  left join edu_teach_graduatedata gd on gd.studentid = a.studentid and gd.isdeleted = 0 ");
			sql.append("where a.isdeleted=0 and (stu.studentstatus='11' or stu.studentstatus='21' or stu.studentstatus='25' or (stu.studentstatus = '24' and gd.isallowsecgraduate = 'Y')) ");

			if(condition.containsKey("ksNum")){ 
	        	sql.append(" and  (select count(*) from EDU_TEACH_EXAMRESULTS re " +
	        			"where re.isdeleted = 0 and re.COURSEID = a.courseid " +
	        			"and re.studentid = a.studentid and re.checkstatus='4')<:ksNum ");
	        }
        if(condition.containsKey("examSubId")){
			sql.append(" and a.nextexamsubid=:examSubId "); 
		}	
        if(condition.containsKey("courseid")){
        	sql.append(" and c.resourceid=:courseid "); 
        }	
        if(condition.containsKey("majorid")){
			sql.append(" and major.resourceid=:majorid "); 
		}
		if(condition.containsKey("gradeId")){
			sql.append(" and stu.gradeid=:gradeId "); 
		}
		if(condition.containsKey("branchschoolid")){
			sql.append(" and stu.branchschoolid=:branchschoolid "); 
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and stu.studyno like :stuNo ");
			condition.put("stuNo", "%" + condition.get("studyNo").toString().trim() + "%");
		}
		if(condition.containsKey("studentName")){
			sql.append(" and stu.studentname like :stuName ");
			condition.put("stuName", "%"+condition.get("studentName")+"%");
		}
		//如果有分班信息
		if(condition.containsKey("classesId")){
			sql.append(" and stu.classesid = :classesId ");
		}
		
		if(condition.containsKey("checkStatus")){
			if("-1".equals(condition.get("checkStatus"))){
				sql.append(" and edumakeup.checkstatus=:checkStatus or edumakeup.checkstatus is null ");
			}else{
				sql.append(" and edumakeup.checkstatus=:checkStatus ");
			} 
		}
		sql.append(" group by stu.resourceid,stu.studyno ");
		sql.append(" order by stu.studyno ");
		
		return sql;
	}
	
	@Override
	public Page findFaceStudyExamResultsVo(Page page, Map<String, Object> condition) throws ServiceException {
		List<ExamResultsVo> list = new ArrayList<ExamResultsVo>();
		StringBuffer sql = getFacestudyExamResultsSql(condition);
		String courseId = (String) condition.get("courseId");
		String yearId = (String) condition.get("yearid");
		String term = (String) condition.get("term");
//		String teachplanid=(String) condition.get("teachplanid");
		String courseTeachType = (String)condition.get("courseTeachType");
		String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		try {
			page = findBySql(page, sql.toString(), condition);
			List resultList = page.getResult();
			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> map = (Map<String, Object>)resultList.get(i);
					String studentid = map.get("studentid")==null?"":map.get("studentid").toString();
					String hql = " from "+NoExamApply.class.getSimpleName()+" etn where etn.isDeleted = '0' and etn.studentInfo.resourceid =? and etn.course.resourceid =? and etn.checkStatus='1' order by etn.scoreForCount desc ";
					List<NoExamApply> noExamlist = noexamapplyservice.findByHql(hql, studentid,courseId);
					ExamResultsVo vo = new ExamResultsVo();
					vo.setStuId(studentid);
					vo.setExamInfoId(map.get("examinfoid")==null?"":map.get("examinfoid").toString());
					vo.setExamResultsResourceId(map.get("resultid")==null?"":map.get("resultid").toString());
					vo.setBranchSchool(map.get("unitname")==null?"":map.get("unitname").toString());
					vo.setMajor(map.get("majorname")==null?"":map.get("majorname").toString());
					vo.setStudyNo(map.get("studyno")==null?"":map.get("studyno").toString());
					vo.setName(map.get("studentname")==null?"":map.get("studentname").toString());
					vo.setCheckStatus(map.get("checkStatus")==null?"":map.get("checkStatus").toString());
					vo.setExamAbnormity(map.get("examabnormity")==null?"":map.get("examabnormity").toString());
					vo.setCourseScoreType(map.get("courseScoreType")==null?"22":map.get("courseScoreType").toString());
					vo.setStudentstatus(map.get("studentstatus")==null?"":map.get("studentstatus").toString());
					if(noExamlist!=null&&noExamlist.size()>0){
						NoExamApply noexamApply =noExamlist.get(0);
						vo.setIntegratedScore(noexamApply.getScoreForCount().toString().substring(0, 2));
						// 桂林医免修免考成绩显示合格
						if("10601".equals(schoolCode)){
							vo.setIntegratedScore("合格");
						}
						vo.setWrittenScore(ExStringUtils.isEmpty(map.get("writtenscore"))?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("writtenscore").toString()));
						vo.setUsuallyScore(ExStringUtils.isEmpty(map.get("usuallyscore"))?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("usuallyscore").toString()));
						//vo.setOnlineScore(map.get("onlinescore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("onlinescore").toString()));
						vo.setExamAbnormity("6");
						vo.setCheckStatus("4");
					}else{
						
						if("0".equals(vo.getExamAbnormity())){
							vo.setWrittenScore(ExStringUtils.isEmpty(map.get("writtenscore"))?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("writtenscore").toString()));
							vo.setIntegratedScore(ExStringUtils.isEmpty(map.get("integratedscore"))?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("integratedscore").toString()));
						} 						
						vo.setUsuallyScore(ExStringUtils.isEmpty(map.get("usuallyscore"))?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("usuallyscore").toString()));
						//vo.setOnlineScore(map.get("onlinescore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("onlinescore").toString()));
//						List<NoExamApply> list1 = noexamapplyservice.findByHql(" from "+NoExamApply.class.getSimpleName()+" where isDeleted=? and studentInfo.resourceid=? and checkStatus=?  and course.resourceid=? ", 0,studentid,"1",courseId);
//						if(CacheAppManager.getSysConfigurationByCode("examsfYorN").getParamValue().equals("N")&&list1.size()>0){
//							page.setTotalCount(page.getTotalCount()-1);
//						}else{
						// 网上学习成绩（作为平时成绩）,注意：只有是网络课程才走这个逻辑
						if((ExStringUtils.isBlank(vo.getCheckStatus())
								|| (ExStringUtils.isNotEmpty(vo.getCheckStatus()) && "0".equals(vo.getCheckStatus())))
								&& ExStringUtils.isNotBlank(courseTeachType) && courseTeachType.equals("networkTeach")){
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("courseid", courseId);
							params.put("stuid", studentid);
							params.put("yearid", yearId);
							params.put("term", term);
							String score = getUsualResultByStudent(params);
							if(ExStringUtils.isNotBlank(score)){
								vo.setUsuallyScore(score);
							}
						}
					}
					list.add(vo);
//					}
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		page.setResult(list);
		//Session session = exGeneralHibernateDao.getSessionFactory().openSession();
		//session.clear();
		//session.close();
		return page;
	}
	@Override
	public String getUsualResultByStudent(Map<String, Object> params)
			throws ServiceException {
		String score = "";
		StringBuffer sql = new StringBuffer("select f_decrypt_score(t.USUALRESULTS,t.STUDENTID) us from EDU_TEACH_USUALRESULTS t ");
		sql.append(" where t.STATUS = '1' and t.isdeleted = 0 ");
		if(params.containsKey("stuid")){
			sql.append(" and t.STUDENTID = :stuid ");
		}
		if(params.containsKey("courseid")){
			sql.append(" and t.courseid = :courseid ");
		}
		if(params.containsKey("yearid")){
			sql.append(" and t.YEARID = :yearid ");
		}
		if(params.containsKey("term")){
			sql.append(" and t.TERM = :term ");
		}
		if(params.containsKey("planCourseId")){
			sql.append(" and t.majorcourseid = :planCourseId ");
		}
		try {
			List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), params);
			if(null != list && list.size() > 0){
				score = list.get(0).get("US").toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return score;
	}
	//面授考试结果（需要补考的）
	@Override
	public Page findNonFaceStudyExamResultsVo(Page page, Map<String, Object> condition) throws ServiceException {
		List<ExamResultsMakeUpVo> list = new ArrayList<ExamResultsMakeUpVo>();
		
		String examResultsTimes=null;
		if(CacheAppManager.getSysConfigurationByCode("examResultsTimes")!=null){
			examResultsTimes=CacheAppManager.getSysConfigurationByCode("examResultsTimes").getParamValue();
		}
        if(examResultsTimes!=null){
			int ksNum=  Integer.parseInt(examResultsTimes);
			condition.put("ksNum", ksNum+1);
		}
		StringBuffer sql = getNonFacestudyExamResultsSql(condition);

			
		try {
			page = findBySql(page, sql.toString(), condition);
			List resultList = page.getResult(); 
			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> map = (Map<String, Object>)resultList.get(i);
					String studentid = map.get("studentid")==null?"":map.get("studentid").toString();
					ExamResultsMakeUpVo vo = new ExamResultsMakeUpVo();		
					vo.setStuId(studentid);
					vo.setCourseName(map.get("coursename")==null?"":map.get("coursename").toString());
					vo.setCourseId(map.get("courseid")==null?"":map.get("courseid").toString());
					vo.setExamResultsResourceId(map.get("resultidmakeup")==null?"":map.get("resultidmakeup").toString());
					vo.setBranchSchool(map.get("unitname")==null?"":map.get("unitname").toString());
					vo.setMajor(map.get("majorname")==null?"":map.get("majorname").toString());
					vo.setStudyNo(map.get("studyno")==null?"":map.get("studyno").toString());
					vo.setName(map.get("studentname")==null?"":map.get("studentname").toString());
					vo.setCheckStatus(map.get("checkStatus")==null?"":map.get("checkStatus").toString());
					vo.setExamAbnormity(map.get("examabnormity")==null?"":map.get("examabnormity").toString());
					vo.setStudentstatus(map.get("studentstatus")==null?"":map.get("studentstatus").toString());
					vo.setExamabnormityMakeup(map.get("examabnormityMakeup")==null?"":map.get("examabnormityMakeup").toString());
					vo.setCheckStatusMakeup(map.get("checkStatusMakeup")==null?"":map.get("checkStatusMakeup").toString());
					vo.setPlansourceid(map.get("plansourceid")==null?"":map.get("plansourceid").toString());
					vo.setExamAbnormity(map.get("examabnormitymakeup")==null?"":map.get("examabnormitymakeup").toString());
//					if("0".equals(vo.getExamAbnormity())){
//						vo.setWrittenScore(map.get("writtenscore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("writtenscore").toString()));
//					} 						
//					vo.setUsuallyScore(map.get("usuallyscore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("usuallyscore").toString()));
//					vo.setIntegratedScore(map.get("integratedscore")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("integratedscore").toString()));
					vo.setResultidMakeup(map.get("resultidMakeup")==null?"":map.get("resultidMakeup").toString());
					if("0".equals(vo.getExamabnormityMakeup())){
						vo.setWrittenScoreMakeup(map.get("writtenscoreMakeup")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("writtenscoreMakeup").toString()));
					} 						
					vo.setUsuallyScoreMakeup(map.get("usuallyscoreMakeup")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("usuallyscoreMakeup").toString()));
					vo.setIntegratedscoreMakeup(map.get("integratedscoreMakeup")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("integratedscoreMakeup").toString()));
					vo.setIsMachineExam(map.get("isMachineExam")==null?Constants.BOOLEAN_NO:map.get("IsMachineExam").toString());
//					List<ExamResults> list1 = examResultsService.findByHql(" from "+ExamResults.class.getSimpleName()+" where isDeleted=0 " +
//							" and studentInfo.resourceid=? and course.resourceid=? and checkStatus=? ", studentid,map.get("courseid").toString(),"4");

						list.add(vo);

				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		page.setResult(list);
		return page;
	}
	
	/* 导出成绩单(补考) */
	@Override
	public List<ExamResultsVo> findFailStudyExamResultsVo(Map<String, Object> condition) throws ServiceException {
		List<ExamResultsVo> list = new ArrayList<ExamResultsVo>();
		StringBuffer sql = getNonFacestudyExamResultsSql(condition);
//		sql.append(" order by unit.unitcode,major.majorcode,stu.studyno asc");	
		try {
			List<Map<String, Object>> resultList =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
			if(ExCollectionUtils.isNotEmpty(resultList)){
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> map = resultList.get(i);
					String studentid = map.get("studentid")==null?"":map.get("studentid").toString();
					ExamResultsVo vo = new ExamResultsVo();
					vo.setSort(Integer.toString(i+1));
					vo.setBranchSchool(map.get("unitname")==null?"":map.get("unitname").toString());
					vo.setMajor(map.get("majorname")==null?"":map.get("majorname").toString());
					vo.setStudyNo(map.get("studyno")==null?"":map.get("studyno").toString());
					vo.setName(map.get("studentname")==null?"":map.get("studentname").toString());
					if(!(condition.containsKey("flag")&&"0".equals(condition.get("flag")))){
						vo.setExamAbnormity(map.get("examabnormityMakeup")==null?"":map.get("examabnormityMakeup").toString());
						if(!"0".equals(vo.getExamAbnormity())){
							vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", ExStringUtils.trimToEmpty(vo.getExamAbnormity())));
						} else {
							vo.setWrittenScore(map.get("writtenScoreMakeup")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("writtenScoreMakeup").toString()));
						}						
//						vo.setUsuallyScore(map.get("usuallyScoreMakeup")==null?"":ScoreEncryptionDecryptionUtil.getInstance().decrypt(studentid,map.get("usuallyScoreMakeup").toString()));
					} 
					list.add(vo);
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	
	/**
	 * 试室安排总表
	 */
	@Override
	public List<Map<String, Object>> statExamRoomPlan(Map<String, Object> condition) {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer(500);
		/*误以为examresults的classroomid关联的是edu_base_classroom
		sql.append(" select h.unitname ,c.examstarttime , ");
		sql.append(" c.examendtime  ,d.coursename , ");
		sql.append(" d.coursecode   ,d.examtype , ");
		sql.append(" count(0) \"C\",b.classroomname , ");
		sql.append(" b.doubleseatnum   ");
		sql.append(" from edu_teach_examresults t ,  edu_base_classroom b, ");
		sql.append(" edu_teach_examinfo c ,  edu_base_course d,  ");
		sql.append(" edu_teach_examsub f,  edu_base_building g ,  hnjk_sys_unit h   ");
		
		
		sql.append(" where t.isdeleted = 0  ");
		sql.append(" and t.classroomid is not null  ");
		sql.append(" and t.examinfoid = c.resourceid  ");
		sql.append(" and t.classroomid = b.resourceid  ");
		sql.append(" and c.courseid = d.resourceid  ");
		sql.append(" and c.examsubid = f.resourceid  ");
		sql.append(" and b.buildingid = g.resourceid  ");
		sql.append(" and g.branchschoolid = h.resourceid ") ;
		
		 if (condition.containsKey("brSchooId")) {
			sql.append(" and g.branchschoolid =:brSchooId ");
			param.put("brSchooId", condition.get("brSchooId"));
		}
		if (condition.containsKey("examPlanId")) {
			sql.append(" and c.examsubid  =:examPlanId ");
			param.put("examPlanId", condition.get("examPlanId"));
		}
		
		sql.append(" group by h.unitname ,b.classroomname,b.doubleseatnum,d.coursename,d.coursecode,d.examtype,c.examstarttime,c.examendtime  ");
		sql.append(" order by c.examstarttime  ");
		*/
		
		sql.append(" select f.batchname,h.unitshortname,h.unitname ,to_char(c.examstarttime,'yyyy-mm-dd hh:mi')||'-'||to_char(c.examendtime,'hh:mi') \"examtime\" ,  ");
		sql.append("  d.coursename , ");
		sql.append(" d.coursecode   ,F_DICTIONARY('CodeCourseExamType',d.examtype)  \"examtype\" , ");
		sql.append("  count(0) \"C\",b.examroomName , ");
		sql.append("  b.doubleseatnum   ");
		sql.append("  from edu_teach_examresults t ,  edu_base_examroom b, ");
		sql.append("  edu_teach_examinfo c ,  edu_base_course d,  ");
		sql.append("  edu_teach_examsub f,  hnjk_sys_unit h   ");		
		sql.append("  where t.isdeleted = 0  ");
		sql.append("  and t.classroomid is not null  ");
		sql.append("  and t.examinfoid = c.resourceid  ");
		sql.append("  and t.classroomid = b.resourceid  ");
		sql.append("  and c.courseid = d.resourceid  ");
		sql.append("  and c.examsubid = f.resourceid  ");
		sql.append("  and b.branchschoolid = h.resourceid  ");
		if (condition.containsKey("brSchooId")) {
			sql.append(" and b.branchschoolid =:brSchooId ");
			param.put("brSchooId", condition.get("brSchooId"));
		}
		if (condition.containsKey("examPlanId")) {
			sql.append(" and c.examsubid  =:examPlanId ");
			param.put("examPlanId", condition.get("examPlanId"));
		}
		sql.append("  group by f.batchname,h.unitshortname,h.unitname ,b.examroomName,b.doubleseatnum,d.coursename,d.coursecode,d.examtype,c.examstarttime,c.examendtime ");
		sql.append("  order by c.examstarttime ");
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		try {
			resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),param );
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return resultList;
	}
	/**
	 * 期末考试表
	 */
	@Override
	public List<Map<String, Object>> getExamRoomSeat(Map<String, Object> condition) {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		//去掉了coursecode字段 因为考试编码不是E+course.coursecode 而是 examinfo的examcoursecode
		sql.append(" SELECT unit.unitname,unit.unitcode,results.classroomid,results.examinfoid,classroom.examroomname,course.coursename ,g.gradename,nvl(ec.examplace,1) examplace, ");
		sql.append(" 	   case when t.ismachineexam = 'Y' then '机考' else '笔试' end examMode, ");
		sql.append("       F_DICTIONARY('CodeCourseExamType',results.examtype) \"EXAMTYPE\",to_char(t.examstarttime,'yyyy-mm-dd') \"examdate\",");
		sql.append("       to_char(t.examstarttime,'HH24:mi:ss')||'~'||to_char(t.examendtime,'HH24:mi:ss') \"examtime\",results.EXAMSEATNUM,");
		sql.append("       info.STUDYNO,classic.CLASSICNAME, ");
		sql.append("       major.MAJORNAME,info.STUDENTNAME,t.examcoursecode ");
		sql.append("  FROM EDU_ROLL_STUDENTINFO info join EDU_TEACH_EXAMRESULTS results on    results.STUDENTID   = info.RESOURCEID      ");
		sql.append("  join EDU_BASE_CLASSIC classic on info.CLASSICID     = classic.RESOURCEID   ");
		sql.append("  join  EDU_BASE_MAJOR major on  info.MAJORID       = major.RESOURCEID    ");
		sql.append("  join  edu_base_grade g on info.gradeid = g.resourceid  ");
		sql.append("  join   EDU_BASE_COURSE course on results.COURSEID    = course.resourceid   ");
		sql.append("  join   edu_base_examroom classroom on classroom.resourceid = results.classroomid  ");
		sql.append("  join   hnjk_sys_unit unit on classroom.branchschoolid = unit.resourceid  ");
		sql.append("  join   edu_teach_examinfo   t on results.examinfoid = t.resourceid    ");
		sql.append("  left join   edu_teach_exambrschoolset ec   on t.examsubid = ec.examsubid and info.branchschoolid = ec.brschoolid and classic.resourceid = ec.classicid and course.resourceid = ec.courseid and ec.isdeleted = 0     ");
		
		sql.append(" WHERE  results.isDeleted   = 0 ");
		
		if (condition.containsKey("examSubID")) {
			sql.append(" and results.EXAMSUBID =:examSubID ");
			param.put("examSubID", condition.get("examSubID"));
		}
		if (condition.containsKey("examRoomID")) {
			sql.append(" and results.CLASSROOMID  in ('"+condition.get("examRoomID")+"') " );
//			param.put("examRoomID", condition.get("examRoomID"));
		}
		if (condition.containsKey("courseID")) {
			sql.append(" and course.RESOURCEID  =:courseId ");
			param.put("courseId", condition.get("courseID"));
		}
		if (condition.containsKey("multiValueId")) {//含examinfo信息的复合id
			if(condition.get("multiValueId").toString().contains(",")){
				String[] ids =condition.get("multiValueId").toString().split(",") ;
				sql.append(" and (");
				for (int i = 0; i < ids.length; i++) {
					if(ExStringUtils.isNotEmpty(ids[i])&&i!=ids.length-1){
						sql.append(" (results.CLASSROOMID = '"+ids[i].split("__")[0]+"' and t.resourceid = '"+ids[i].split("__")[1]+"') or ");
					}else{
						sql.append(" (results.CLASSROOMID = '"+ids[i].split("__")[0]+"' and t.resourceid = '"+ids[i].split("__")[1]+"')  ");
					}
				}
				sql.append(" ) ");
			}else{
				sql.append(" and results.CLASSROOMID = '"+condition.get("multiValueId").toString().split("__")[0]+"' and t.resourceid = '"+condition.get("multiValueId").toString().split("__")[1]+"' ");
			}
		}
		
		sql.append(" ORDER BY results.classroomid,results.examinfoid,results.examstarttime,results.examendtime,results.courseid ,cast(results.EXAMSEATNUM as int),major.majorcode  ASC "); 
		List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>();
		try {
			resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),param );
		} catch (Exception e) {
			e.printStackTrace();
		}  
		return resultList;
	}
	//教学计划课程与班级_old
	@Override
	public Page findTeachingPlanClassCourseByConditionOld(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();	
		sql.append(" select distinct pc.resourceid||'_'||classes.resourceid complexresourceid, pc.resourceid ,pc.examclasstype, p.resourceid planid,course.coursename,pc.term,pc.teachtype ,classes.classesname,classes.resourceid classesid ,pg.gradeid,cs.term coursestatusterm,unit.unitname,unit.resourceid unitId,course.resourceid courseid,u.cnname teachername,em.mobile ");
		sql.append(" from edu_teach_plan p "); 
		sql.append(" inner join edu_teach_guiplan pg  on  pg.planid = p.resourceid ");
		sql.append(" inner join edu_teach_plancourse pc on pc.planid = p.resourceid ");
		sql.append(" inner join edu_teach_coursestatus cs on pc.resourceid = cs.plancourseid and pg.resourceid = cs.guiplanid and cs.isopen ='Y' and cs.isdeleted= 0  ");
		sql.append(" inner join edu_roll_classes classes on classes.gradeid = pg.gradeid and classes.classicid = p.classicid and classes.teachingType = p.schooltype and classes.orgunitid = cs.schoolids and classes.isdeleted = 0 left join  edu_roll_studentinfo st  on st.classesid=classes.resourceid ");
		sql.append(" inner join hnjk_sys_unit unit on classes.orgunitid = unit.resourceid ");
		sql.append(" inner join edu_base_course course on course.resourceid = pc.courseid "); 
		sql.append(" inner join edu_base_grade g on g.resourceid = pg.gradeid "); 
		sql.append(" inner join edu_base_year y on y.resourceid = g.yearid ");
		sql.append(" inner join edu_base_classic c on c.resourceid = p.classicid ");
		sql.append(" inner join edu_base_major m on m.resourceid = p.majorid ");
		sql.append(" left join EDU_TEACH_COURSETEACHERCL tcl on tcl.COURSESTATUSID = cs.resourceid and tcl.COURSEID = course.resourceid and tcl.CLASSESID = classes.resourceid ");
		sql.append(" left join hnjk_sys_users u on u.resourceid=tcl.TEACHERID ");
		sql.append(" left join EDU_BASE_EDUMANAGER em on em.sysuserid=u.resourceid ");
//				sql.append(" left join edu_teach_timetable tt on tt.plancourseid = pc.resourceid and tt.classesid=classes.resourceid and tt.isdeleted= 0 ");
		
		if (condition.containsKey("jwyId")) {// 教务员录入自己负责的教学站班级的课程成绩
			sql.append(" inner join hnjk_sys_users users on users.unitid=unit.resourceid and users.resourceid=:jwyId ");
			values.put("jwyId", condition.get("jwyId"));
		}
		
		if(condition.containsKey("teachId")){// 登分老师录入自己负责的班级的课程成绩
			sql.append(" inner join edu_teach_courseteachercl ctl on ctl.CLASSESID = classes.resourceid and ctl.isdeleted= 0 and course.resourceid = ctl.COURSEID and ctl.TEACHERID = :teachId ");
			values.put("teachId", condition.get("teachId"));
		}
		
		sql.append(" inner join EDU_TEACH_COURSETEACHERCL ct on ct.courseid=course.resourceid and ct.classesid=classes.resourceid and ct.coursestatusid=cs.resourceid ");
		
		sql.append(" where p.isdeleted= 0 and pg.isdeleted = 0 and pc.isdeleted= 0 ");
		
		if(condition.containsKey("exam")){//课程成绩录入入口
			
		}
		if(condition.containsKey("learningStyle")){//教师ID
			sql.append(" and st.learningStyle = :learningStyle ");
			values.put("learningStyle", condition.get("learningStyle"));
		}
		if(condition.containsKey("userId")){//教师ID
			sql.append(" and ct.teacherid = :userId ");
			values.put("userId", condition.get("userId"));
		}
//				if(condition.containsKey("term")){//上课学期
//					sql.append(" and cs.term = :term ");
//					values.put("term", condition.get("term"));
//				}
		if(condition.containsKey("teachingPlanId")){//教学计划
			sql.append(" and p.resourceid = :teachingPlanId ");
			values.put("teachingPlanId", condition.get("teachingPlanId"));
		}
		if(condition.containsKey("courseId")){//课程
			sql.append(" and pc.courseid = :courseId ");
			values.put("courseId", condition.get("courseId"));
		}		
		if(condition.containsKey("teachType")){//教学方式
			sql.append(" and pc.teachType =:teachType ");				
			values.put("teachType", condition.get("teachType"));
		}
		if(condition.containsKey("teachingtype")){//学习方式
			sql.append(" and classes.teachingtype =:teachingtype ");				
			values.put("teachingtype", condition.get("teachingtype"));
		}
		if (condition.containsKey("beforeTerm")) {
			sql.append(" and cast( pc.term as int ) <= :beforeTerm ");
			values.put("beforeTerm", condition.get("beforeTerm"));
		}
		if (condition.containsKey("guidPlanId")) {
			sql.append(" and pg.resourceid = :guidPlanId ");
			values.put("guidPlanId", condition.get("guidPlanId"));
		}
		if (condition.containsKey("classesId")){
			sql.append(" and classes.resourceid = :classesId ");
			values.put("classesId", condition.get("classesId"));
		}
		if (condition.containsKey("gradeId")){
			sql.append(" and classes.gradeid = :gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if (condition.containsKey("branchSchool")){
			sql.append(" and classes.orgunitid = :branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){
			sql.append(" and m.resourceid=:major ");
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){
			sql.append(" and c.resourceid=:classic ");
			values.put("classic", condition.get("classic"));
		}
		
		//2014-4-1 哪一个学期开课，哪一个学期就可以录取成绩
		if("1".equals(condition.get("subTerm"))){
//					sql.append(" and cast( pc.term as int ) <= decode(g.term,1,("+condition.get("subYear")+"-y.firstyear)*2+1,("+condition.get("subYear")+"-y.firstyear)*2 )");
			sql.append(" and cs.term like '%_01' ");
			sql.append(" and cs.term like '"+condition.get("subYear")+"_%' ");
		}else if("2".equals(condition.get("subTerm"))){
//					sql.append(" and cast( pc.term as int ) <= decode(g.term,2,("+condition.get("subYear")+"-y.firstyear)*2+1,("+condition.get("subYear")+"-y.firstyear)*2+2 )");
			sql.append(" and cs.term like '%_02' ");
			sql.append(" and cs.term like '"+(Long.valueOf(condition.get("subYear").toString()))+"_%' ");
		}
		Page page = findBySql(objPage, sql.toString(), values);
		try {
			List<Map> results = page.getResult();
			for (Map result : results) {
				String sqls = "select distinct tt.teachername from edu_teach_timetable tt where tt.plancourseid = :plancourseid and tt.classesid=:classesid and tt.isdeleted= 0";
				Map param = new HashMap();
				param.put("plancourseid", result.get("resourceid"));
				param.put("classesid", result.get("classesid"));
				List tts = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqls, param);
				String tn = "";
				for (int i = 0; i < tts.size(); i++) {
					Map tt = (Map) tts.get(i);
					if ("".equals(tn)) {
						tn += tt.get("teachername").toString();
					} else {
						tn += "，" + tt.get("teachername").toString();
					}
				}
				result.put("teachername", tn);
				param.clear();
				sqls = "select u.cnname,em.mobile from hnjk_sys_users u join HNJK_SYS_ROLEUSERS ru on u.resourceid=ru.userid join hnjk_sys_roles r on r.resourceid=ru.roleid "
					+ "join EDU_BASE_EDUMANAGER em on em.sysuserid=u.resourceid where u.isdeleted=0 and u.enabled=1 and u.unitId=:unitId and r.roleCode=:roleCode";
				param.put("unitId", result.get("unitId"));
				param.put("roleCode", "ROLE_BRS_STUDENTSTATUS");
				List users = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqls, param);
				String sunames = "";
//						String sumobiles = "";
				for (int i = 0; i < users.size(); i++) {
					Map user = (Map) users.get(i);
					if ("".equals(sunames)) {
						sunames += (user.get("CNNAME")!=null?user.get("CNNAME").toString():"")+(user.get("MOBILE")!=null?"("+user.get("MOBILE").toString()+")":"");
//								sumobiles += (user.get("MOBILE")!=null?"("+user.get("MOBILE").toString()+")":"");
					} else {
						sunames += (user.get("CNNAME")!=null?"，" + user.get("CNNAME").toString():"")+(user.get("MOBILE")!=null?"("+user.get("MOBILE").toString()+")":"");
//								sumobiles += (user.get("MOBILE")!=null?"，" + user.get("MOBILE").toString():"");
					}
				}
				result.put("suteachername", sunames);
//						result.put("sumobile", sumobiles);
				param.clear();
				if (condition.get("examSubId") != null) {
					param.put("examSubId", condition.get("examSubId"));
				}
				param.put("teachplanid", result.get("planid"));
				param.put("classesId", result.get("classesid"));
				param.put("gradeid", result.get("gradeid"));
				param.put("teachingPlanCourseId", result.get("resourceid"));
				param.put("courseId", result.get("courseid"));
				param.put("teachType", "facestudy");
				StringBuffer sqlsb = new StringBuffer();
				sqlsb.append("select r.checkstatus,count(r.checkstatus) c from (").append(getFacestudyExamResultsSql(param))
					.append(") r group by r.checkstatus");
				List rss = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlsb.toString(), param);
				String checkStatus0 = "0";
				String checkStatus1 = "0";
				String checkStatus4 = "0";
				if (rss != null) {
					for (int i = 0; i < rss.size(); i++) {
						Map rs = (Map) rss.get(i);
						if (rs.get("checkstatus") != null && "0".equals(rs.get("checkstatus").toString())) {
							checkStatus0 = rs.get("c").toString();
						}
						if (rs.get("checkstatus") != null && "1".equals(rs.get("checkstatus").toString())) {
							checkStatus1 = rs.get("c").toString();
						}
						if (rs.get("checkstatus") != null && "4".equals(rs.get("checkstatus").toString())) {
							checkStatus4 = rs.get("c").toString();
						}
					}
				}
				result.put("checkStatus0", checkStatus0);
				result.put("checkStatus1", checkStatus1);
				result.put("checkStatus4", checkStatus4);
				result.put("inputcount", Integer.parseInt(checkStatus0) + Integer.parseInt(checkStatus1) + Integer.parseInt(checkStatus4));
			}
		} catch (Exception e) {
			System.out.println(e);
//					logger.error("获取面授课成绩审核应考人数出错："+e.fillInStackTrace());
		}
		return page;
//				return findBySql(objPage, sql.toString(), values);
	}
	
	//教学计划课程与班级_new
		@Override
		public Page findTeachingPlanClassCourseByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
			Map<String,Object> values =  new HashMap<String, Object>();
			String examSubId = (String)condition.get("examSubId");
			StringBuffer sql = new StringBuffer();	

			sql.append(" select distinct pc.resourceid||'_'||classes.resourceid complexresourceid, pc.resourceid ,pc.examclasstype, p.resourceid planid,course.coursename,pc.term,pc.teachtype pcteachtype,cs.teachtype ,classes.classesname,classes.classcode, ");
			sql.append(" classes.resourceid classesid ,m.majorname,cs.examform,pg.gradeid,cs.term coursestatusterm,unit.unitname,unit.resourceid unitId,course.resourceid courseid,tem.cnname teachername,tem.mobile,"+convertSqls("sst.resultstatus")+" checkStatus0, ");
			sql.append(" esy.batchname,"+convertSqls("sbt.resultstatus")+" checkStatus1,"+convertSqls("pst.resultstatus","noe.resultstatus")+" checkStatus4, ");
			sql.append(convertSqls("sst.resultstatus","sbt.resultstatus","pst.resultstatus","noe.resultstatus")+" inputcount,nvl(itn.counts, 0) inputTotalNum,ttl.lecturername ");
			//增加教师id用于录入成绩判断
			if(condition.containsKey("isAllow") && "N".equals(condition.get("isAllow"))){
				sql.append(",tem.TEACHERID teacherid");
			}
			//排序字段
			sql.append(",unit.unitcode,g.gradename,c.resourceid classicid,classes.teachingtype,m.majorcode,course.coursecode");
			sql.append(" from edu_teach_plancourse pc "); 
			sql.append(" inner join edu_teach_plan p on pc.planid = p.resourceid and p.isdeleted=0 ");
			sql.append(" inner join edu_roll_studentinfo st on st.isdeleted=0 and st.teachplanid=p.resourceid ");
			sql.append(" inner join edu_teach_guiplan pg on pg.planid = p.resourceid and pg.gradeid=st.gradeid and pg.ispublished='Y' and pg.isdeleted=0 ");
			sql.append(" inner join edu_teach_coursestatus cs on pc.resourceid = cs.plancourseid and pg.resourceid = cs.guiplanid and cs.isopen = 'Y' and cs.isdeleted = 0 and cs.schoolids=st.branchschoolid ");
			sql.append(" inner join edu_roll_classes classes on classes.resourceid = st.classesid ");

			sql.append(" inner join hnjk_sys_unit unit on classes.orgunitid = unit.resourceid ");
			sql.append(" inner join edu_base_course course on course.resourceid = pc.courseid "); 
			sql.append(" inner join edu_base_grade g on g.resourceid = st.gradeid "); 
			sql.append(" inner join edu_base_classic c on c.resourceid = st.classicid ");
			sql.append(" inner join edu_base_major m on m.resourceid = st.majorid ");
			//考试批次
			sql.append(" inner join (select es.resourceid,y.firstyear||'_0'||es.term esterm,es.batchname from edu_teach_examsub es inner join edu_base_year y on y.isdeleted=0 and es.yearid=y.resourceid ");
			sql.append(" where es.isdeleted=0 and es.examtype='N' ");
			if(condition.containsKey("examSubId")){
				sql.append(" and es.resourceid=:examSubId ");
				values.put("examSubId", examSubId);
			}
			sql.append(" ) esy on esy.esterm=cs.term ");
			// 教务员录入自己负责的教学站班级的课程成绩
			if (condition.containsKey("jwyId")) {
				sql.append(" inner join hnjk_sys_users users on users.unitid=unit.resourceid and users.resourceid=:jwyId ");
				values.put("jwyId", condition.get("jwyId"));
			}
			// 登分老师
			sql.append(" left join (select tcl.COURSESTATUSID,tcl.COURSEID,tcl.CLASSESID,u.cnname,em.mobile,tcl.teacherid from EDU_TEACH_COURSETEACHERCL tcl ");
			sql.append(" inner join hnjk_sys_users u on u.resourceid = tcl.TEACHERID inner join EDU_BASE_EDUMANAGER em on em.sysuserid = u.resourceid where tcl.isdeleted=0) tem ");
			sql.append(" on tem.COURSESTATUSID = cs.resourceid and tem.COURSEID = course.resourceid and tem.CLASSESID = classes.resourceid ");
			// 成绩状态
			sql.append(" left join ("+getCheckStatusNumSql("0",examSubId)+") sst on sst.pcclId=pc.resourceid || '_' || classes.resourceid and sst.examsubid=esy.resourceid ");
			sql.append(" left join ("+getCheckStatusNumSql("1",examSubId)+") sbt on sbt.pcclId=pc.resourceid || '_' || classes.resourceid and sbt.examsubid=esy.resourceid ");
			sql.append(" left join ("+getCheckStatusNumSql("4",examSubId)+") pst on pst.pcclId=pc.resourceid || '_' || classes.resourceid and pst.examsubid=esy.resourceid ");
			//免考人数
			sql.append(" left join (select so.classesid,ne.courseid,count(so.resourceid) resultstatus from edu_teach_noexam ne join edu_roll_studentinfo so on so.resourceid=ne.studentid ");
			sql.append(" where ne.isdeleted=0 and ne.checkStatus =1 and ne.unScore=1 group by so.classesid,ne.courseid) noe ");
			sql.append(" on noe.classesid=classes.resourceid and noe.courseid=course.resourceid ");
			//总人数
			sql.append(" left join ( "+inputTotalNumSql(condition)+") itn on itn.resourceid=pc.resourceid||'_'||classes.resourceid ");
			// 任课老师
			sql.append(" left join (select tt.plancourseid||'_'||tt.classesid ttId ,wm_concat(distinct tt.teachername) lecturername from edu_teach_timetable tt ");
			sql.append(" where tt.isdeleted= 0 group by tt.plancourseid,tt.classesid) ttl on ttl.ttId=pc.resourceid||'_'||classes.resourceid ");
			
			// 条件
			sql.append(" where pc.isdeleted= 0 ");
			//必须有登分老师
			sql.append(" and tem.teacherid is not null ");
			if(condition.containsKey("teachId")){// 登分老师录入自己负责的班级的课程成绩
				sql.append(" and tem.teacherid = :teachId ");
				values.put("teachId", condition.get("teachId"));
			}
			
			if(condition.containsKey("learningStyle")){//学习方式
				sql.append(" and st.learningStyle = :learningStyle ");
				values.put("learningStyle", condition.get("learningStyle"));
			}
			if(condition.containsKey("userId")){//教师ID
				sql.append(" and tem.teacherid = :userId ");
				values.put("userId", condition.get("userId"));
			}

			if(condition.containsKey("teachingPlanId")){//教学计划
				sql.append(" and p.resourceid = :teachingPlanId ");
				values.put("teachingPlanId", condition.get("teachingPlanId"));
			}
			if(condition.containsKey("courseId")){//课程
				sql.append(" and pc.courseid = :courseId ");
				values.put("courseId", condition.get("courseId"));
			}		
			if(condition.containsKey("teachType")){// 教学方式
				sql.append(" and pc.teachType =:teachType ");				
				values.put("teachType", condition.get("teachType"));
			}
			if(condition.containsKey("courseTeachType")){// 教学形式
				sql.append(" and cs.teachtype =:courseTeachType ");				
				values.put("courseTeachType", condition.get("courseTeachType"));
			}
			if(condition.containsKey("teachingtype")){//学习方式
				sql.append(" and classes.teachingtype =:teachingtype ");				
				values.put("teachingtype", condition.get("teachingtype"));
			}
			if (condition.containsKey("beforeTerm")) {
				sql.append(" and cast( pc.term as int ) <= :beforeTerm ");
				values.put("beforeTerm", condition.get("beforeTerm"));
			}
			if (condition.containsKey("guidPlanId")) {
				sql.append(" and pg.resourceid = :guidPlanId ");
				values.put("guidPlanId", condition.get("guidPlanId"));
			}
			if (condition.containsKey("classesId")){
				sql.append(" and classes.resourceid = :classesId ");
				values.put("classesId", condition.get("classesId"));
			}
			if (condition.containsKey("gradeId")){
				sql.append(" and classes.gradeid = :gradeId ");
				values.put("gradeId", condition.get("gradeId"));
			}
			if (condition.containsKey("branchSchool")){
				sql.append(" and classes.orgunitid = :branchSchool ");
				values.put("branchSchool", condition.get("branchSchool"));
			}
			if(condition.containsKey("major")){
				sql.append(" and m.resourceid=:major ");
				values.put("major", condition.get("major"));
			}
			if(condition.containsKey("classic")){
				sql.append(" and c.resourceid=:classic ");
				values.put("classic", condition.get("classic"));
			}
			
			//2014-4-1 哪一个学期开课，哪一个学期就可以录取成绩
			if("1".equals(condition.get("subTerm"))){
				sql.append(" and cs.term like '%_01' ");
				sql.append(" and cs.term like '"+condition.get("subYear")+"_%' ");
			}else if("2".equals(condition.get("subTerm"))){
				sql.append(" and cs.term like '%_02' ");
				sql.append(" and cs.term like '"+(Long.valueOf(condition.get("subYear").toString()))+"_%' ");
			}
			// 状态查询
			if(condition.containsKey("resultStatus")){
				String _rs = (String)condition.get("resultStatus");
				if("partSave".equals(_rs)) {// 部分保存
					sql.append(" and (( "+convertSql("sst.resultstatus")+">0 and "+convertSql("sst.resultstatus")+"<itn.counts) or ");
					sql.append(convertSql("sst.resultstatus")+"+"+convertSql("sbt.resultstatus")+"+"+convertSql("pst.resultstatus")+"=0) ");
				} else if("partSubmit".equals(_rs)) {//  部分提交
					sql.append(" and (0<"+convertSql("sbt.resultstatus")+" and "+convertSql("sbt.resultstatus")+"<itn.counts) ");
				} else if("partPublish".equals(_rs)) {// 部分发布
					sql.append(" and (0<"+convertSql("pst.resultstatus")+" and "+convertSql("pst.resultstatus")+"<itn.counts)");
				} else if("allSave".equals(_rs)) {// 全部保存
					sql.append(" and sst.resultstatus=itn.counts ");
				} else if("allSubmit".equals(_rs)) {// 全部提交
					sql.append(" and sbt.resultstatus=itn.counts ");
				} else if("allPublish".equals(_rs)) {// 全部发布
					sql.append(" and pst.resultstatus=itn.counts ");
				//增加未录入
				}else if ("notAllInput".equals(_rs)) {
					sql.append(" and (").append(convertSqls("sst.resultstatus","sbt.resultstatus","pst.resultstatus","noe.resultstatus")).append(")<itn.counts");
				}
			}
			
			//增加考核方式
			if(condition.containsKey("examClassType")){
				sql.append(" and pc.examclasstype=:examClassType ");
				values.put("examClassType", condition.get("examClassType"));
			}
			//20181015  因为系统中已经有统考成绩录入，因此在这个成绩录入处进行过滤
			if(condition.containsKey("filterExamClassType")){
				sql.append(" and (pc.examclasstype!=:filterExamClassType or pc.examclasstype is null)");
				values.put("filterExamClassType", condition.get("filterExamClassType"));
			}
			if(condition.containsKey("queryNotAllInput")){//查询未录入
				sql.append(" and sysdate>=es.examinputstarttime ");
			}
			if(condition.containsKey("studentStatuses")){// 学籍状态
				sql.append(" and st.studentstatus in ( ");
				sql.append(condition.get("studentStatuses"));
				sql.append(") ");
			}

			Page page = findBySql(objPage, sql.toString(), values);
			/*if("Y".equals(condition.get("queryNotAllInput"))){//用户登录查询未录入
				return page;
			}*/
			return page;

		}
		
		//教学计划课程与班级(补考)
		@Override
		public Page findTeachingPlanClassCourseByCondition2(Map<String, Object> condition, Page objPage) throws ServiceException{
			Map<String,Object> values =  new HashMap<String, Object>();
			StringBuffer sql = new StringBuffer();	
			sql.append(" select distinct pc.resourceid||'_'||i.classesid complexresourceid, pc.resourceid ,pc.examclasstype,course.coursename,pc.term,pc.teachtype ,classes.classesname,i.classesid ,tt.teachername,pg.gradeid,cs.term coursestatusterm,unit.unitname ");
			sql.append(" from edu_teach_plan p inner join edu_roll_studentinfo i on i.teachplanid = p.resourceid "); 
			sql.append(" inner join edu_teach_guiplan pg  on  pg.planid = p.resourceid ");
			sql.append(" inner join  edu_teach_plancourse pc on pc.planid = p.resourceid ");
			sql.append(" inner join edu_roll_classes classes on classes.resourceid = i.classesid ");
			sql.append(" inner join hnjk_sys_unit unit on classes.orgunitid = unit.resourceid ");
			sql.append(" inner join edu_base_course course on course.resourceid = pc.courseid "); 
			sql.append(" inner join edu_teach_coursestatus cs on pc.resourceid = cs.plancourseid and pg.resourceid = cs.guiplanid and cs.isopen ='Y' and cs.isdeleted= 0  ");
			sql.append(" inner join edu_base_grade g on g.resourceid = pg.gradeid "); 
			sql.append(" inner join edu_base_year y on y.resourceid = g.yearid ");
			sql.append(" left join edu_teach_timetable tt on tt.plancourseid = pc.resourceid and tt.classesid=i.classesid and tt.isdeleted= 0 ");
			sql.append(" where i.isdeleted= 0 and p.isdeleted= 0 and pg.isdeleted = 0 and pc.isdeleted= 0 ");
			
			if(condition.containsKey("teachingPlanId")){//教学计划
				sql.append(" and p.resourceid = :teachingPlanId ");
				values.put("teachingPlanId", condition.get("teachingPlanId"));
			}
			if(condition.containsKey("courseId")){//课程
				sql.append(" and pc.courseid = :courseId ");
				values.put("courseId", condition.get("courseId"));
			}		
			if(condition.containsKey("teachType")){//教学方式
				sql.append(" and pc.teachType =:teachType ");				
				values.put("teachType", condition.get("teachType"));
			}
			if (condition.containsKey("beforeTerm")) {
				sql.append(" and cast( pc.term as int ) <= :beforeTerm ");
				values.put("beforeTerm", condition.get("beforeTerm"));
			}
			if (condition.containsKey("guidPlanId")) {
				sql.append(" and pg.resourceid = :guidPlanId ");
				values.put("guidPlanId", condition.get("guidPlanId"));
			}
			if (condition.containsKey("classesId")){
				sql.append(" and classes.resourceid = :classesId ");
				values.put("classesId", condition.get("classesId"));
			}
			if (condition.containsKey("gradeId")){
				sql.append(" and classes.gradeid = :gradeId ");
				values.put("gradeId", condition.get("gradeId"));
			}
			if (condition.containsKey("branchSchool")){
				sql.append(" and classes.orgunitid = :branchSchool ");
				values.put("branchSchool", condition.get("branchSchool"));
			}
			
			if("1".equals(condition.get("subTerm"))){
				sql.append(" and cast( pc.term as int ) <= decode(g.term,1,("+condition.get("subYear")+"-y.firstyear)*2+1,("+condition.get("subYear")+"-y.firstyear)*2 )");
				sql.append(" and cs.term like '%_02' ");
				sql.append(" and cs.term like '"+condition.get("subYear")+"_%' ");
			}else if("2".equals(condition.get("subTerm"))){
				sql.append(" and cast( pc.term as int ) <= decode(g.term,2,("+condition.get("subYear")+"-y.firstyear)*2+1,("+condition.get("subYear")+"-y.firstyear)*2+2 )");
				sql.append(" and cs.term like '%_01' ");
				sql.append(" and cs.term like '"+(Long.valueOf(condition.get("subYear").toString())+1)+"_%' ");
			}
			return findBySql(objPage, sql.toString(), values);
		}
		
		/**
		 * 查找有预约记录考试课程的记录(考试座位安排)
		 * @param condition
		 * @return
		 * @throws Exception 
		 */
		@Override
		public Page findExamCourseStudentByCondition(Page page,Map<String, Object> condition) throws Exception{
			StringBuffer sql   		 = new StringBuffer();
			Object[] param = new Object[0];
			
			if(condition.containsKey("isBrschool")){//校外学习中心
				sql.append(" select  nvl(ec.examplace ,1) examplace, stu.* ,re.resourceid reId ,u.unitname,sub.batchname ,c.coursename ,info.examstarttime,info.examendtime");
				sql.append(" from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid ");
				sql.append(" join edu_teach_examinfo info on sp.examinfoid = info.resourceid     ");
				sql.append(" join edu_teach_examsub sub on sub.resourceid = info.examsubid ");
				sql.append(" join edu_base_course c on c.resourceid = info.courseid ");
				sql.append(" join edu_teach_examresults re on sp.examinfoid = re.examinfoid  and re.examsubid = sub.resourceid and re.studentid = stu.resourceid "); 
				sql.append(" join hnjk_sys_unit u on stu.branchschoolid = u.resourceid ");
				sql.append(" left join edu_teach_exambrschoolset ec on sub.resourceid = ec.examsubid and u.resourceid = ec.brschoolid and stu.classicid = ec.classicid and c.resourceid = ec.courseid and ec.isdeleted = 0 ");
				sql.append("  join edu_base_major m on stu.majorid = m.resourceid ");
				sql.append(" where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null ");
				sql.append(" and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is null and re.classroomid is null ");
				sql.append("  and stu.studentstatus = 11 and  stu.isdeleted = 0  ");
				
				if (condition.containsKey("mergeExamBrschool")) {
					sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
				}
				if(condition.containsKey("branchSchool")) {
					sql.append(" and u.resourceid='"+condition.get("branchSchool")+"'");
				}
				if (condition.containsKey("examSubId")) {
					sql.append(" and sub.resourceid = '"+condition.get("examSubId")+"'");
				}
				//区分校外
					sql.append("  and ec.examplace is  null ");
				
				if (condition.containsKey("courseId")) {                  //课程
					sql.append(" and info.courseid  ='"+condition.get("courseId")+"'"); 
				}
				
				sql.append(" order by  u.unitcode ,m.majorcode,stu.studyno  ");
				page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
			}else{
				if(condition.containsKey("examPlace")){
					if("1".equals(condition.get("examPlace"))){//校外
						sql.append(" select  nvl(ec.examplace ,1) examplace, stu.* ,re.resourceid reId ,u.unitname,sub.batchname ,c.coursename ,info.examstarttime,info.examendtime");
						sql.append(" from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid ");
						sql.append(" join edu_teach_examinfo info on sp.examinfoid = info.resourceid     ");
						sql.append(" join edu_teach_examsub sub on sub.resourceid = info.examsubid ");
						sql.append(" join edu_base_course c on c.resourceid = info.courseid ");
						sql.append(" join edu_teach_examresults re on sp.examinfoid = re.examinfoid  and re.examsubid = sub.resourceid  and re.studentid = stu.resourceid "); 
						sql.append(" join hnjk_sys_unit u on stu.branchschoolid = u.resourceid ");
						sql.append(" left join edu_teach_exambrschoolset ec on sub.resourceid = ec.examsubid and u.resourceid = ec.brschoolid and stu.classicid = ec.classicid and c.resourceid = ec.courseid and ec.isdeleted = 0 ");
						sql.append("  join edu_base_major m on stu.majorid = m.resourceid ");
						sql.append(" where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null ");
						sql.append(" and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is null and re.classroomid is null ");
						sql.append("  and stu.studentstatus = 11 and  stu.isdeleted = 0  ");
						
						if (condition.containsKey("mergeExamBrschool")) {
							sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
						}
						if(condition.containsKey("branchSchool")) {
							sql.append(" and u.resourceid='"+condition.get("branchSchool")+"'");
						}
						if (condition.containsKey("examSubId")) {
							sql.append(" and sub.resourceid = '"+condition.get("examSubId")+"'");
						}
						//区分校外
							sql.append("  and ec.examplace is  null ");
						
						if (condition.containsKey("courseId")) {                  //课程
							sql.append(" and info.courseid  ='"+condition.get("courseId")+"'"); 
						}
						sql.append(" order by  u.unitcode ,m.majorcode,stu.studyno  ");
						page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
					}else{//校内
						sql.append(" select  nvl(ec.examplace ,1) examplace, stu.* ,re.resourceid reId ,u.unitname,sub.batchname ,c.coursename ,info.examstarttime,info.examendtime");
						sql.append(" from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid ");
						sql.append(" join edu_teach_examinfo info on sp.examinfoid = info.resourceid     ");
						sql.append(" join edu_teach_examsub sub on sub.resourceid = info.examsubid ");
						sql.append(" join edu_base_course c on c.resourceid = info.courseid ");
						sql.append(" join edu_teach_examresults re on sp.examinfoid = re.examinfoid  and re.examsubid = sub.resourceid and re.studentid = stu.resourceid "); 
						sql.append(" join hnjk_sys_unit u on stu.branchschoolid = u.resourceid ");
						sql.append(" left join edu_teach_exambrschoolset ec on sub.resourceid = ec.examsubid and u.resourceid = ec.brschoolid and stu.classicid = ec.classicid and c.resourceid = ec.courseid and ec.isdeleted = 0 ");
						sql.append("  join edu_base_major m on stu.majorid = m.resourceid ");
						sql.append(" where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null ");
						sql.append(" and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is null and re.classroomid is null ");
						sql.append("  and stu.studentstatus = 11 and  stu.isdeleted = 0  ");
						
						if (condition.containsKey("mergeExamBrschool")) {
							sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
						}
						if(condition.containsKey("branchSchool")) {
							sql.append(" and u.resourceid='"+condition.get("branchSchool")+"'");
						}
						if (condition.containsKey("examSubId")) {
							sql.append(" and sub.resourceid = '"+condition.get("examSubId")+"'");
						}
						//区分校内
						sql.append("  and ec.examplace  = 0 ");
						
						if (condition.containsKey("courseId")) {                  //课程
							sql.append(" and info.courseid  ='"+condition.get("courseId")+"'"); 
						}
						sql.append(" order by  u.unitcode ,m.majorcode,stu.studyno  ");
						page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
					}
				}else{//全部显示
					sql.append(" select  nvl(ec.examplace ,1) examplace, stu.* ,re.resourceid reId ,u.unitname,sub.batchname ,c.coursename ,info.examstarttime,info.examendtime");
					sql.append(" from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid ");
					sql.append(" join edu_teach_examinfo info on sp.examinfoid = info.resourceid     ");
					sql.append(" join edu_teach_examsub sub on sub.resourceid = info.examsubid ");
					sql.append(" join edu_base_course c on c.resourceid = info.courseid ");
					sql.append(" join edu_teach_examresults re on sp.examinfoid = re.examinfoid  and re.examsubid = sub.resourceid and re.studentid = stu.resourceid "); 
					sql.append(" join hnjk_sys_unit u on stu.branchschoolid = u.resourceid ");
					sql.append(" left join edu_teach_exambrschoolset ec on sub.resourceid = ec.examsubid and u.resourceid = ec.brschoolid and stu.classicid = ec.classicid and c.resourceid = ec.courseid and ec.isdeleted = 0 ");
					sql.append("  join edu_base_major m on stu.majorid = m.resourceid ");
					sql.append(" where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null ");
					sql.append(" and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is null and re.classroomid is null ");
					sql.append("  and stu.studentstatus = 11 and  stu.isdeleted = 0  ");
					
					if (condition.containsKey("mergeExamBrschool")) {
						sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
					}
					if(condition.containsKey("branchSchool")) {
						sql.append(" and u.resourceid='"+condition.get("branchSchool")+"'");
					}
					if (condition.containsKey("examSubId")) {
						sql.append(" and sub.resourceid = '"+condition.get("examSubId")+"'");
					}
					if (condition.containsKey("isBrschool")) {
						sql.append("  and ec.examplace is  null ");
					}
					if (condition.containsKey("courseId")) {                  //课程
						sql.append(" and info.courseid  ='"+condition.get("courseId")+"'"); 
					}
					sql.append(" order by  u.unitcode ,m.majorcode,stu.studyno  ");
					page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
				}
				
				
			}
			
			
			
			return page;
		}
		
		/**
		 * 查找安排座位的学生记录(考试座位安排)
		 * @param condition
		 * @return
		 * @throws Exception 
		 */
		@Override
		public Page findStudentSeatByCondition(Page page,Map<String, Object> condition) throws Exception{
			StringBuffer sql   		 = new StringBuffer();
			Object[] param = new Object[0];
			
			sql.append(" select stu.* ,re.resourceid reId ,u.unitname,sub.batchname ,c.coursename ,cast(re.examseatnum as int ) examseatnum,erm.examroomname,m.majorname,re.resourceid resultId,sub.resourceid subId,c.resourceid cid");
			sql.append(" from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid ");
			sql.append(" join edu_teach_examinfo info on sp.examinfoid = info.resourceid     ");
			sql.append(" join edu_teach_examsub sub on sub.resourceid = info.examsubid ");
			sql.append(" join edu_base_course c on c.resourceid = info.courseid ");
			sql.append(" join edu_teach_examresults re on sp.examinfoid = re.examinfoid  and re.examsubid = sub.resourceid and re.studentid = stu.resourceid "); 
			sql.append(" join hnjk_sys_unit u on stu.branchschoolid = u.resourceid ");
			sql.append(" join edu_base_major m on stu.majorid = m.resourceid ");
			//sql.append(" left join edu_teach_exambrschoolset ec on sub.resourceid = ec.examsubid and u.resourceid = ec.brschoolid and stu.classicid = ec.classicid and c.resourceid = ec.courseid and ec.isdeleted = 0 ");
			
			sql.append("  join edu_base_examroom erm on re.classroomid = erm.resourceid ");
			sql.append(" where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null ");
			sql.append(" and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is not null and re.classroomid  is not null ");
			sql.append("  and stu.studentstatus = 11 and  stu.isdeleted = 0  ");
			
			if (condition.containsKey("mergeExamBrschool")) {
				sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
			}
			if (condition.containsKey("branchSchool")) {
				sql.append("		  and u.resourceid='"+condition.get("branchSchool")+"'");
			}
			
			if (condition.containsKey("examSubId")) {
				sql.append(" and sub.resourceid = '"+condition.get("examSubId")+"'");
			}
			if (condition.containsKey("courseId")) {  
				sql.append(" and info.courseid  ='"+condition.get("courseId")+"'"); 
			}
			if(condition.containsKey("studentNo")){
				sql.append(" and stu.studyno = '"+condition.get("studentNo")+"'");
			}
			if(condition.containsKey("name")){
				sql.append(" and stu.studentname like '%"+condition.get("name")+"%'");
			}
			if(condition.containsKey("classic")){
				sql.append(" and stu.classicid = '"+condition.get("classic")+"'");
			}
			if(condition.containsKey("examroomBranchSchool")){
				sql.append(" and erm.branchschoolid = '"+condition.get("examroomBranchSchool")+"'");
			}
			//sql.append(" and ec.examplace is null ");
			sql.append(" order by erm.resourceid, cast(re.examseatnum as int ) asc ");
			//List<Map<String,Object>>  list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
			page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
			
			return page;
		}
		
		/**
		 * 查找考试课程是否为校内考(考试座位安排)
		 * @param condition
		 * @return
		 * @throws Exception 
		 */
		@Override
		public String findCourseByCondition(Page page,Map<String, Object> condition) throws Exception{
			StringBuffer sql   		 = new StringBuffer();
			Object[] param = new Object[0];
			sql.append(" select stu.* ,re.resourceid reId ,u.unitname,sub.batchname ,c.coursename ");
			sql.append(" from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid ");
			sql.append(" join edu_teach_examinfo info on sp.examinfoid = info.resourceid     ");
			sql.append(" join edu_teach_examsub sub on sub.resourceid = info.examsubid ");
			sql.append(" join edu_base_course c on c.resourceid = info.courseid ");
			sql.append(" join edu_teach_examresults re on sp.examresultsid = re.resourceid "); 
			sql.append(" join hnjk_sys_unit u on stu.branchschoolid = u.resourceid ");
			sql.append(" left join edu_teach_exambrschoolset ec on sub.resourceid = ec.examsubid and u.resourceid = ec.brschoolid and stu.classicid = ec.classicid and c.resourceid = ec.courseid and ec.isdeleted = 0 ");
			sql.append(" where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null ");
			sql.append(" and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is null and re.classroomid is null ");
			sql.append("  and stu.studentstatus = 11 and  stu.isdeleted = 0  ");
			
			if (condition.containsKey("mergeExamBrschool")) {
				sql.append("		  and u.resourceid in("+condition.get("mergeExamBrschool")+")");
			}else {
				sql.append("		  and u.resourceid='"+condition.get("branchSchool")+"'");
			}
			if (condition.containsKey("examSubId")) {
				sql.append(" and sub.resourceid = '"+condition.get("examSubId")+"'");
			}
			
			
			if (condition.containsKey("courseId")) {                  //课程
				sql.append(" and info.courseid  ='"+condition.get("courseId")+"'"); 
			}
			sql.append(" and ec.examplace is not null ");
			page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
			if(page.getResult().size()>0){
				return "Y";
			}
			return "";
		}
		/**
		 * 获取考试时间
		 * @param condition
		 * @return
		 */
		@Override
		public List<Map<String,Object>> getExamTime(Map<String,Object> condition) throws ServiceException{
			Map<String,Object> paramMap     = new HashMap<String, Object>();
			List<Map<String,Object>> list 	= new ArrayList<Map<String,Object>>();
			paramMap.put("isDeleted", 0);
			
			StringBuffer sql = new StringBuffer();
			
			sql.append("     select distinct to_char(info.examstarttime,'yyyy-MM-dd hh24:mi')||'-'||to_char(info.examendtime,'hh24:mi') examtime");
			sql.append("     from edu_roll_studentinfo stu join edu_learn_stuplan sp on stu.resourceid = sp.studentid  ");
			sql.append("     join edu_teach_examinfo info on sp.examinfoid = info.resourceid      ");
			sql.append("     join edu_teach_examsub sub on sub.resourceid = info.examsubid  ");
			sql.append("     join edu_base_course c on c.resourceid = info.courseid  ");
			sql.append("     join edu_teach_examresults re on sp.examresultsid = re.resourceid ");  
			sql.append("     join edu_base_major m on stu.majorid = m.resourceid  ");
			sql.append("     join edu_base_examroom erm on re.classroomid = erm.resourceid "); 
			sql.append("     join hnjk_sys_unit u on erm.branchschoolid = u.resourceid  ");
			sql.append("     where  sp.status = 2 and sp.isdeleted = 0 and sp.examresultsid is not null "); 
			sql.append("     and info.isdeleted = 0 and re.isdeleted = 0  and re.examseatnum is not null and re.classroomid  is not null "); 
			sql.append("     and stu.studentstatus = 11 and  stu.isdeleted = 0   ");
			
			/* 之前方法中的条件 */
			if (condition.containsKey("examPlace")) {
				sql.append("    and erm.BRANCHSCHOOLID='001' ");
			}else{
				if (condition.containsKey("branchSchool")) {
					paramMap.put("branchSchool", condition.get("branchSchool"));
					sql.append("    and erm.BRANCHSCHOOLID=:branchSchool ");
				}
			}
			
			if (condition.containsKey("examMode")&& "5".equals(condition.get("examMode"))) {
				sql.append("    and erm.iscomputerroom=:isComputerroom ");
				paramMap.put("isComputerroom", Constants.BOOLEAN_YES);
			}
			/* 之前方法中的条件 */
			/*补充可用条件 批次和课程*/
			
			if(condition.containsKey("examSub")){
				sql.append(" and  sub.resourceid = :examSub ");
				paramMap.put("examSub", condition.get("examSub"));
			}
			if(condition.containsKey("courseId")){
				sql.append(" and  c.resourceid = :courseId ");
				paramMap.put("courseId", condition.get("courseId"));
			}
			
			sql.append(" group by erm.examroomname,erm.examroomsize,sub.resourceid,sub.batchname,u.resourceid,u.unitname,c.resourceid,c.coursename,erm.resourceid,to_char(info.examstarttime,'yyyy-MM-dd hh24:mi')||'-'||to_char(info.examendtime,'hh24:mi') ,info.resourceid  ");
			sql.append(" order by to_char(info.examstarttime,'yyyy-MM-dd hh24:mi')||'-'||to_char(info.examendtime,'hh24:mi') ");
			
			try {
				list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), paramMap );			
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return list;

		}
		
		/**
		 * 
		  * @description:根据年级查询教学计划
		  * @author xiangy  
		  * @date 2013-11-5 上午10:15:32 
		  * @version V1.0
		  * @param page
		  * @param condition
		  * @return
		  * @throws Exception
		 */
		@Override
		public Page findTeachingPlanByGrade(Page page,Map<String, Object> condition) throws Exception{
			StringBuffer sql   		 = new StringBuffer();
			Map<String,Object> values =  new HashMap<String, Object>();
			Object[] param = new Object[0];
			sql.append(" SELECT * ");
			sql.append("   FROM (SELECT PLAN.*, gplanandgrade.gradename,gplanandgrade.gradeid,gplanandgrade.resourceid gplanid,  unit.unitname,classic.classicname, major.majorname ");
			sql.append(" FROM edu_teach_plan PLAN LEFT JOIN (SELECT gplan.*, grade.gradename     ");
			sql.append(" FROM edu_teach_guiplan gplan, edu_base_grade grade ");
			sql.append("  WHERE gplan.gradeid =grade.resourceid  ");
			sql.append("  AND gplan.isdeleted = 0) gplanandgrade ON gplanandgrade.planid =PLAN.resourceid  ");
			sql.append("  LEFT JOIN hnjk_sys_unit unit ON unit.resourceid =PLAN.brschoolid  ");
			sql.append("  LEFT JOIN edu_base_major major ON major.resourceid =PLAN.majorid  ");
			sql.append("  left join edu_base_classic classic on plan.classicid=classic.resourceid  ");
			sql.append("  )WHERE isdeleted = 0  ");
			if(condition.containsKey("schoolType")){//教学类型
				sql.append(" and schoolType = "+condition.get("schoolType"));
			}
			if(condition.containsKey("isUsed")){//是否使用
				sql.append(" and isUsed = '"+condition.get("isUsed")+"'");
			}
			if(condition.containsKey("classic")){//层次
				sql.append(" and classicid = '"+condition.get("classic")+"'");
			}
			if(condition.containsKey("major")){//专业
				sql.append(" and majorid = '"+condition.get("major")+"'");
			}
			if(condition.containsKey("orgUnitId")){//按组织
				sql.append(" and BRSCHOOLID =  '"+condition.get("orgUnitId")+"'");
			}
			if(condition.containsKey("gradeId")){//按年级
				sql.append(" and gradeid =  '"+condition.get("gradeId")+"'");
			}
			page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(page, sql.toString(), param);
			return page;
		}
		
		/**
		 * 获取（以考试批次、年级、班级、课程为条件）各成绩状态的人数SQL
		 * @param resultStatus 成绩状态
		 * @return
		 */
		private String getCheckStatusNumSql(String resultStatus, String examSubId) {
			StringBuffer sql = new StringBuffer(500);
			sql.append("SELECT er.majorcourseid||'_'||so.classesid pcclId,so.gradeid,so.classesid,er.courseid,er.majorcourseid,so.teachplanid,er.examsubid,es1.batchname, COUNT(so.resourceid) resultstatus FROM edu_teach_examresults er, ")
			.append("edu_roll_studentinfo so, ")
			.append("edu_teach_examsub es1 ")
			.append("WHERE er.studentid=so.resourceid ")
			.append("AND er.examsubid=es1.resourceid ")
			.append("AND er.isdeleted=0 ")
			.append("AND so.isdeleted=0 ")
			.append("AND es1.isdeleted=0 ")
			.append("AND so.studentstatus in ('11','21','25') ")
			.append("AND not exists ( ")
			.append("select score.resourceid from edu_teach_examscore score ")
			.append("where score.studentid = so.resourceid ")
			.append("and score.courseid =er.courseid ")
			.append("and score.ispass ='Y' ) ")
//			.append("AND pc1.teachtype='facestudy' ")
			.append("AND er.checkstatus='")
			.append(resultStatus)
			.append("' AND es1.examtype='N' ");
			// 考生批次
			if(ExStringUtils.isNotBlank(examSubId)){
				sql.append(" and er.examsubid='").append(examSubId).append("' ");
			}
			sql.append(" GROUP BY so.gradeid,so.classesid,er.courseid,er.majorcourseid,so.teachplanid,er.examsubid,es1.batchname ");
			
			return sql.toString();
			
		}
		
		@Override
		@Transactional(readOnly=true)
		public Page findTeachingPlanExamresultMapByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
			List<Object> param = new ArrayList<Object>();
			StringBuilder sql = new StringBuilder();
			sql.append("select u.unitname,g.gradename,cc.classicname,c.teachingtype,m.majorname,c.classesname,c.classesmaster,p.TRAININGTARGET,p.resourceid planid");
			sql.append(",c.resourceid classesid,c.orgunitid,c.classicid,c.majorid,c.gradeid");
			sql.append(",(select count(resourceid) from EDU_ROLL_STUDENTINFO where isdeleted=0 and (studentStatus='11' or studentStatus='21') and classesid=c.resourceid) stunum");
			sql.append(",(select count(resourceid) from edu_teach_plancourse where isdeleted=0 and planid=p.resourceid) coursenum");
			sql.append(",(select count(cs.resourceid) from EDU_TEACH_COURSESTATUS cs join edu_teach_plancourse pc on cs.plancourseid=pc.resourceid");
			sql.append(" join edu_teach_guiplan gp on cs.guiplanid=gp.resourceid ");
			sql.append(" where cs.isdeleted=0 and c.orgunitid=cs.schoolids and cs.isopen='Y' and cs.checkstatus!='cancelY' and pc.planid=p.resourceid and gp.planid=p.resourceid and c.gradeid=gp.gradeid) opencoursenum");
			sql.append(" from EDU_ROLL_CLASSES c left join hnjk_sys_unit u on c.orgunitid=u.resourceid");
			sql.append(" join EDU_BASE_GRADE g on c.gradeid=g.resourceid");
			sql.append(" join EDU_BASE_CLASSIC cc on c.classicid=cc.resourceid");
			sql.append(" join EDU_BASE_MAJOR m on c.majorid=m.resourceid");
			sql.append(" join edu_teach_plan p on c.majorid=p.majorid and c.classicid=p.classicid and c.teachingType=p.schooltype");
			sql.append(" join edu_teach_plancourse pc on pc.planid=p.resourceid");
			sql.append(" join edu_teach_guiplan gp on gp.planid=p.resourceid and c.gradeid=gp.gradeid ");
			sql.append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=pc.resourceid and c.orgunitid = s.schoolids");
			sql.append(" where c.isdeleted=0");
			
			if(condition.containsKey("branchSchool")){//学习中心
				sql.append(" and c.orgunitid = ? ");
				param.add(condition.get("branchSchool"));
			}
			if(condition.containsKey("major")){//专业
				sql.append(" and c.majorid = ? ");
				param.add(condition.get("major"));
			}
			if(condition.containsKey("classic")){//层次
				sql.append(" and c.classicid = ? ");
				param.add(condition.get("classic"));
			}
			if(condition.containsKey("gradeid")){//年级
				sql.append(" and c.gradeid= ? ");
				param.add(condition.get("gradeid"));
			}
			if(condition.containsKey("classesid")){//班级
				sql.append(" and c.resourceid= ? ");
				param.add(condition.get("classesid"));
			}
			if(condition.containsKey("teachingType")){//学习方式
				sql.append(" and c.teachingType= ? ");
				param.add(condition.get("teachingType"));
			}
			if(condition.containsKey("classesmaster")){//班主任
				sql.append(" and c.classesmaster like ? ");
				param.add("%"+condition.get("classesmaster")+"%");
			}
			
			sql.append(" group by p.resourceid,u.unitname,g.gradename,cc.classicname,c.teachingtype,m.majorname,c.classesname,c.classesmaster,p.TRAININGTARGET");
			sql.append(" ,c.orgunitid,c.gradeid,c.resourceid,c.classicid,c.majorid");
			Page page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), param.toArray());
			List<Map<String, Object>> results = page.getResult();
			try {
				if (results != null) {
					for (Map<String, Object> result : results) {
						Map<String,Object> inputTotalNum = new HashMap<String,Object>();//需要录入的人数
						Map<String,Object> condition2 = new HashMap<String,Object>();
						condition2.put("teachingPlanId", result.get("planid").toString());
						condition2.put("classesId", result.get("classesid").toString());
						condition2.put("branchSchool", result.get("orgunitid").toString());
						condition2.put("classic", result.get("classicid").toString());
						condition2.put("major", result.get("majorid").toString());
						condition2.put("gradeId", result.get("gradeid").toString());
						condition2.put("teachType", "facestudy");
						condition2.put("studentStatus1", "11");
						condition2.put("studentStatus2", "21");
						condition2.put("studentStatus3", "25");
						condition2.put("isDeleted",0);
						condition2.put("isPass",Constants.BOOLEAN_YES);
						//查询需要录入的人数
						examResultsService.queryInputTotalNum(condition2,inputTotalNum);
						Page page2 = new Page();
						page2 = findTeachingPlanClassCourseByCondition(condition2, page2);
						List<Map<String, Object>> results2 = page2.getResult();
						int inputedcoursenum = 0;
						for (Map<String, Object> result2 : results2) {
							String complexresourcenum = "0";
							if (inputTotalNum.get(result2.get("complexresourceid").toString()) != null) {
								complexresourcenum = inputTotalNum.get(result2.get("complexresourceid").toString()).toString();
							}
							if (Integer.parseInt(result2.get("checkStatus4").toString()) == Integer.parseInt(complexresourcenum)) {
								inputedcoursenum++;
							}
						}
						if (Integer.parseInt(result.get("opencoursenum").toString()) < inputedcoursenum) {
							inputedcoursenum = Integer.parseInt(result.get("opencoursenum").toString());
						}
						result.put("inputedcoursenum", inputedcoursenum);
					}
				}
			} catch (Exception e) {
				System.out.println(e);
			}
			return page;
		}	
		
		/**
		 * 根据条件获取教学计划课程的录入情况信息
		 * @param condition
		 * @param objPage
		 * @return
		 * @throws ServiceException
		 */
		@Override
		@Transactional(readOnly=true)
		public Page findCourseInputInfoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
			List<Object> param = new ArrayList<Object>();
			StringBuffer sql = new StringBuffer("");
			sql.append("select u.unitname,g.gradename,cc.classicname,cl.teachingtype,m.majorname,p.trainingtarget,cl.classesname, ");
			sql.append("count(so.resourceid) studentNum,cl.classesmaster,tpc.totalCourse,cl.majorid,cl.classicid,cl.gradeid,so.teachplanid, ");
			sql.append("case when openCourse.openCourseNum is null then 0 else openCourse.openCourseNum end openCourseNum, ");
			sql.append("case when teacher.hasTeacherNum is null then 0 else teacher.hasTeacherNum end hasTeacherNum,so.classesid,cl.orgunitid, ");
			sql.append("case when arrangedCourse.arrangedCourseNum is null then 0 else arrangedCourse.arrangedCourseNum end arrangedCourseNum, ");
			sql.append("case when hasResult.hasResultNum is null then 0 else hasResult.hasResultNum end hasResultNum from edu_roll_studentinfo so ");
			sql.append("inner join edu_roll_classes cl on so.classesid=cl.resourceid and cl.isdeleted=0 ");
			sql.append("inner join edu_teach_guiplan gp on so.teachplanid=gp.planid and so.gradeid=gp.gradeid and gp.isdeleted=0 ");
			sql.append("inner join edu_teach_plan p on gp.planid=p.resourceid and p.isdeleted=0 ");
			sql.append("inner join edu_base_grade g on cl.gradeid=g.resourceid and g.isdeleted=0 ");
			sql.append("inner join edu_base_classic cc on cl.classicid=cc.resourceid and cc.isdeleted=0 ");
			sql.append("inner join edu_base_major m on cl.majorid=m.resourceid and m.isdeleted=0 ");
			sql.append("inner join hnjk_sys_unit u on cl.orgunitid=u.resourceid and u.isdeleted=0 ");
			// 教学计划总课程
			sql.append("left join (select pc.planid,count(pc.resourceid) totalCourse from edu_teach_plancourse pc  ");
			sql.append("where pc.isdeleted=0 group by pc.planid) tpc on tpc.planid=gp.planid ");
			// 所有开课课程
			sql.append("left join (select css.guiplanid,css.schoolids,count(css.resourceid) openCourseNum from edu_teach_coursestatus css where css.isopen='Y' ");
			sql.append(" and css.isdeleted=0 group by css.guiplanid,css.schoolids) openCourse on openCourse.guiplanid=gp.resourceid and openCourse.schoolids=cl.orgunitid ");
			//已排课课程
			sql.append("left join (select tt.classesid,count(distinct tt.plancourseid) arrangedCourseNum from edu_teach_timetable tt where tt.isdeleted=0 "); 
			sql.append("group by tt.classesid) arrangedCourse on arrangedCourse.classesid=cl.resourceid ");
			//已设置登分老师课程
			sql.append("left join (select ct.classesid,css1.schoolids,css1.guiplanid,count(distinct css1.plancourseid) hasTeacherNum from edu_teach_courseteachercl ct,edu_teach_coursestatus css1 ");
			sql.append("where ct.coursestatusid=css1.resourceid and ct.isdeleted=0 and css1.isdeleted=0 and css1.isopen='Y' group by ct.classesid,css1.schoolids,css1.guiplanid) teacher "); 
			sql.append("on teacher.classesid=cl.resourceid and cl.orgunitid=teacher.schoolids and gp.resourceid=teacher.guiplanid "); 
			// 已录入成绩课程
			sql.append("left join (select so1.classesid,es.examtype,count(distinct er.majorcourseid) hasResultNum "); 
			sql.append("from edu_teach_examresults er,edu_roll_studentinfo so1,edu_teach_plancourse pc1,edu_teach_examsub es ");
			sql.append("where er.studentid=so1.resourceid and er.examsubid=es.resourceid and er.majorcourseid=pc1.resourceid and pc1.planid=so1.teachplanid ");
			sql.append("and pc1.courseid=er.courseid and pc1.isdeleted=0 and so1.isdeleted=0 and er.isdeleted=0 and es.isdeleted=0 ");
//			sql.append("and pc1.teachtype='facestudy' and (so1.studentstatus='11' or so1.studentstatus='21') and es.examtype='N' ");
			sql.append("and pc1.teachtype='facestudy' and (so1.studentstatus='11' or so1.studentstatus='21') and es.examtype='N' ");
			sql.append("group by so1.classesid,es.examtype) hasResult on hasResult.classesid=cl.resourceid ");
			sql.append("where so.isdeleted=0 and (so.studentstatus='11' or so.studentstatus='21') ");
			
			if(condition.containsKey("branchSchool")){//学习中心
				sql.append(" and cl.orgunitid = ? ");
				param.add(condition.get("branchSchool"));
			}
			if(condition.containsKey("major")){//专业
				sql.append(" and cl.majorid = ? ");
				param.add(condition.get("major"));
			}
			if(condition.containsKey("classic")){//层次
				sql.append(" and cl.classicid = ? ");
				param.add(condition.get("classic"));
			}
			if(condition.containsKey("stuGrade")){//年级
				sql.append(" and cl.gradeid= ? ");
				param.add(condition.get("stuGrade"));
			}
			if(condition.containsKey("classesId")){//班级
				sql.append(" and cl.resourceid= ? ");
				param.add(condition.get("classesId"));
			}
			if(condition.containsKey("teachingType")){//学习方式
				sql.append(" and cl.teachingType= ? ");
				param.add(condition.get("teachingType"));
			}
			if(condition.containsKey("classesmaster")){//班主任
				sql.append(" and cl.classesmaster like ? ");
				param.add("%"+condition.get("classesmaster")+"%");
			}
			if(condition.containsKey("isInputed")){// 是否全部录入
				if("Y".equals(condition.get("isInputed"))){// 开课课程等于已录入课程数
					sql.append(" and openCourse.openCourseNum=hasResult.hasResultNum ");
				} else if("N".equals(condition.get("isInputed"))){
					sql.append(" and (openCourse.openCourseNum!=hasResult.hasResultNum or openCourse.openCourseNum is null ");
					sql.append("  or hasResult.hasResultNum is null) ");
				}
			}
			
			sql.append("group by u.unitname,g.gradename,cc.classicname,cl.teachingtype,m.majorname,p.trainingtarget,cl.classesname,cl.classesmaster,tpc.totalCourse, ");
			sql.append("openCourse.openCourseNum,cl.majorid,arrangedCourse.arrangedCourseNum,teacher.hasTeacherNum,hasResult.hasResultNum,so.classesid,cl.orgunitid, ");
			sql.append("cl.classicid,cl.gradeid,so.teachplanid ");
			sql.append("order by u.unitname,g.gradename,cc.classicname,cl.teachingtype,m.majorname,p.trainingtarget,cl.classesname ");
			
			Page page = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage, sql.toString(), param.toArray());
			
			return page;
		}
			
		
		/**
		 * 某门课程需要录入成绩的总人数
		 * @param condition
		 * @return
		 */
		private String inputTotalNumSql(Map<String, Object> condition) {
			StringBuffer sql = new StringBuffer("select pc.resourceid||'_'||stu.classesid resourceid, count(stu.resourceid) counts from edu_teach_plancourse pc  ");
			sql.append(" inner join edu_roll_studentinfo stu on stu.teachplanid = pc.planid and stu.studentstatus in('11','21','25') and stu.isdeleted =0 ");
			/*if(condition.containsKey("branchSchool")){
				sql.append(" and stu.branchschoolid =:branchSchool ");
			}*/
			sql.append(" where pc.isdeleted =0  ");
			if (condition.containsKey("courseId")) {
				sql.append(" and pc.courseid ='"+condition.get("courseId")+"' ");
			}
			/*if (condition.containsKey("teachType")) {
				sql.append(" and pc.teachtype = :teachType ");
			}*/
			sql.append("  and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =pc.courseid and score.ispass ='Y' )");
			if("N".equals(CacheAppManager.getSysConfigurationByCode("examsfYorN").getParamValue())){
				sql.append(" and not exists(select * from EDU_TEACH_NOEXAM re where re.isDeleted=0 and re.STUDENTID=stu.resourceid and re.checkStatus='1'  and re.COURSEID=pc.COURSEID ) ");
			}
			
			sql.append(" group by pc.resourceid,stu.classesid ");
			
			return sql.toString();
		}
		
		/**
		 * 字段值的转换：空->0
		 * @param column
		 * @return
		 */
		private String convertSql(String column){
			String sql = " CASE WHEN "+column+" IS NULL THEN 0 ELSE "+column+" END ";
			return sql;
		}
		
		/**
		 * 多个字段求和
		 * @param column
		 * @return
		 */
		private String convertSqls(String ...column){
			StringBuffer sql = new StringBuffer();
			for (String string : column) {
				sql.append("nvl("+string+",0)+");
			}
			if(ExStringUtils.isNotEmpty(sql.toString())) {
				return sql.substring(0, sql.length()-1);
			}else{
				return "";
			}
		}
		/**
		 * 根据条件获取已开课课程信息
		 * @throws Exception 
		 */
		@Override
		public List<Map<String, Object>> findOPenedCourseInfo( Map<String, Object> condition) throws Exception {
			StringBuffer sql = new StringBuffer("");
			sql.append("select c.resourceid,c.coursename from edu_base_course c, ");
			sql.append("edu_teach_plancourse pc, ");
			sql.append("edu_teach_coursestatus css ");
			sql.append("where css.plancourseid=pc.resourceid ");
			sql.append("and pc.courseid=c.resourceid ");
			sql.append("and css.isdeleted=0 ");
			sql.append("and pc.isdeleted=0 ");
			sql.append("and c.isdeleted=0 ");
			sql.append("and css.checkstatus!='cancelY' ");
			sql.append("and css.isopen='Y' ");
			if(condition.containsKey("guiplanid")){
				sql.append("and css.guiplanid=:guiplanid ");
			}
			if(condition.containsKey("openTerm")){
				sql.append("and css.term=:openTerm ");
			}
			if(condition.containsKey("orgunitid")){
				sql.append("and css.schoolids=:orgunitid ");
			}
			
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		}
		
		/**
		 *  根据年份和建议学期获取要开课课程的有关信息
		 */
		@Override
		public List<Map<String, Object>> findByYearAndTerm( Set<String> openTerm) throws Exception {
			StringBuffer sql = findOpenCourseInfoSQL();
			if(openTerm != null && openTerm.size() > 0){
				boolean isFirst = false;
				sql.append("and (");
				for(String _yearTerm : openTerm){
					String[] year_term = _yearTerm.split("_");
					if(isFirst) {
						sql.append(" or ");
					}
					sql.append("(y.firstyear="+Integer.valueOf(year_term[0])+" and pc.term="+Integer.valueOf(year_term[1])+") ");
					isFirst = true;
				}
				sql.append(") ");
			}
			
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), new HashMap<String, Object>());
		}
		/**
		 * 获取要开课课程的有关信息的SQL
		 * @return
		 */
		private StringBuffer findOpenCourseInfoSQL() {
			StringBuffer sql = new StringBuffer("");
			// TODO: 优化了招生计划后，就可以实现教学点只开自己招生的专业了
			sql.append("select distinct gp.resourceid guiplanId,g.gradename,pc.resourceid planCourseId,pc.planid,pc.term, ");
			sql.append("(y.firstyear+ceil(pc.term/2)-1)||'_0'||decode(mod(pc.term,2),0,2,1) openTerm  ");
			sql.append("from edu_teach_plancourse pc,edu_teach_guiplan gp, ");
			sql.append("edu_base_grade g, ");
			sql.append("edu_base_year y ");
			sql.append("where g.yearid=y.resourceid ");
			sql.append("and gp.gradeid=g.resourceid ");
			sql.append("and pc.planid=gp.planid ");
			sql.append("and pc.isdeleted=0 ");
			sql.append("and gp.isdeleted=0 ");
			sql.append("and g.isdeleted=0 ");
			sql.append("and y.isdeleted=0 ");
			sql.append("and gp.ispublished='Y' ");
			return sql;
		}
		
		/**
		 * 根据条件获取课程录入情况
		 * @param condition
		 * @param objPage
		 * @return
		 * @throws ServiceException
		 */
		@Override
		public Page findCourseExamInfoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
			Map<String,Object> values =  new HashMap<String, Object>();
			String examSubId = null;
			StringBuffer sql = new StringBuffer();	
			sql.append(" select distinct pc.resourceid||'_'||classes.resourceid complexresourceid, pc.resourceid , p.resourceid planid,course.coursename,pc.term,pc.teachtype ,classes.classesname,");
			sql.append(" classes.resourceid classesid ,pg.gradeid,cs.term coursestatusterm,unit.unitname,unit.resourceid unitId,course.resourceid courseid,ctl.teachername,ctl.mobile teacherPhone, academicStaff.emPhone, ");
			sql.append(" academicStaff.emName, "+convertSqls("sst.resultstatus")+" checkStatus0, "+convertSqls("sbt.resultstatus")+" checkStatus1,"+convertSqls("pst.resultstatus")+" checkStatus4, ");
			sql.append(convertSqls("sst.resultstatus")+"+"+convertSqls("sbt.resultstatus")+"+"+convertSqls("pst.resultstatus")+" inputcount, itn.counts inputTotalNum ");
			sql.append(" from edu_teach_plan p "); 
			sql.append(" inner join edu_teach_guiplan pg  on  pg.planid = p.resourceid ");
			sql.append(" inner join edu_teach_plancourse pc on pc.planid = p.resourceid ");
			sql.append(" inner join edu_teach_coursestatus cs on pc.resourceid = cs.plancourseid and pg.resourceid = cs.guiplanid and cs.isopen ='Y' and cs.isdeleted= 0 ");
			sql.append(" inner join edu_roll_classes classes on classes.gradeid = pg.gradeid and classes.classicid = p.classicid and classes.teachingType = p.schooltype and classes.orgunitid = cs.schoolids and classes.isdeleted = 0 " );
			sql.append(" left join  edu_roll_studentinfo st  on st.classesid=classes.resourceid ");
			sql.append(" inner join hnjk_sys_unit unit on classes.orgunitid = unit.resourceid ");
			sql.append(" inner join edu_base_course course on course.resourceid = pc.courseid "); 
			sql.append(" inner join edu_base_grade g on g.resourceid = pg.gradeid "); 
			//sql.append(" inner join edu_base_year y on y.resourceid = g.yearid ");
			sql.append(" inner join edu_base_classic c on c.resourceid = p.classicid ");
			sql.append(" inner join edu_base_major m on m.resourceid = p.majorid ");
			// 登分老师
			sql.append(" left join (select tcl.courseid,tcl.teacherid,tcl.coursestatusid,tcl.classesid,tcl.teachername,tclem.mobile from edu_teach_courseteachercl tcl, ");
			sql.append(" edu_base_edumanager tclem where tcl.teacherid=tclem.sysuserid and tcl.isdeleted=0 and tclem.isdeleted=0) ctl on ctl.coursestatusid = cs.resourceid ");
			sql.append(" and ctl.courseid = pc.courseid and ctl.classesid = classes.resourceid");
			// 教务员
			sql.append(" left join (select wm_concat(u.cnname) emName,wm_concat(em.officetel) emPhone,u.unitid "); 
			sql.append(" from edu_base_edumanager em,hnjk_sys_users u,hnjk_sys_roleusers ru,hnjk_sys_roles r ");
			sql.append(" where em.sysuserid=u.resourceid and u.resourceid=ru.userid and ru.roleid=r.resourceid ");
			sql.append(" and r.rolecode='ROLE_BRS_STUDENTSTATUS' and em.isdeleted=0 and u.isdeleted=0 group by u.unitid)  academicStaff on academicStaff.unitid=classes.orgunitid");
			// 成绩状态
			sql.append(" left join ("+getCheckStatusNumSql("0",examSubId)+") sst on sst.pcclId=pc.resourceid || '_' || classes.resourceid ");
			sql.append(" left join ("+getCheckStatusNumSql("1",examSubId)+") sbt on sbt.pcclId=pc.resourceid || '_' || classes.resourceid ");
			sql.append(" left join ("+getCheckStatusNumSql("4",examSubId)+") pst on pst.pcclId=pc.resourceid || '_' || classes.resourceid ");
			// 录入成绩人数
			sql.append(" left join ( "+inputTotalNumSql(condition)+") itn on itn.resourceid=pc.resourceid||'_'||classes.resourceid ");
			sql.append(" where p.isdeleted= 0 and pg.isdeleted = 0 and pc.isdeleted= 0 ");
			
			if(condition.containsKey("teachingPlanId")){//教学计划
				sql.append(" and p.resourceid = :teachingPlanId ");
				values.put("teachingPlanId", condition.get("teachingPlanId"));
			}
			if(condition.containsKey("courseId")){//课程
				sql.append(" and pc.courseid = :courseId ");
				values.put("courseId", condition.get("courseId"));
			}		
			if(condition.containsKey("teachType")){//教学方式
				sql.append(" and pc.teachType =:teachType ");				
				values.put("teachType", condition.get("teachType"));
			}
			if(condition.containsKey("teachingtype")){//学习方式
				sql.append(" and classes.teachingtype =:teachingtype ");				
				values.put("teachingtype", condition.get("teachingtype"));
			}
			if (condition.containsKey("guidPlanId")) {
				sql.append(" and pg.resourceid = :guidPlanId ");
				values.put("guidPlanId", condition.get("guidPlanId"));
			}
			if (condition.containsKey("classesId")){
				sql.append(" and classes.resourceid = :classesId ");
				values.put("classesId", condition.get("classesId"));
			}
			if (condition.containsKey("gradeId")){
				sql.append(" and classes.gradeid = :gradeId ");
				values.put("gradeId", condition.get("gradeId"));
			}
			if (condition.containsKey("branchSchool")){
				sql.append(" and classes.orgunitid = :branchSchool ");
				values.put("branchSchool", condition.get("branchSchool"));
			}
			if(condition.containsKey("majorId")){
				sql.append(" and m.resourceid=:majorId ");
				values.put("majorId", condition.get("majorId"));
			}
			if(condition.containsKey("classicId")){
				sql.append(" and c.resourceid=:classicId ");
				values.put("classicId", condition.get("classicId"));
			}
			
			Page page = findBySql(objPage, sql.toString(), values);
			
			return page;
		}
		
		/**
		 * 根据年级教学计划获取要开课的课程有关信息
		 * @param guiplanIds
		 * @return
		 * @throws ServiceException
		 */
		@Override
		public List<Map<String, Object>> findOpenCourseInfoByGuiplanId(String guiplanIds) throws Exception {
			StringBuffer sql = findOpenCourseInfoSQL();
			if(ExStringUtils.isNotEmpty(guiplanIds)){
				sql.append(" and gp.resourceid in ("+guiplanIds+") ");
			}
			
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), new HashMap<String, Object>());
		}
		
		/**
		 * 获取某个学生的最终成绩（安徽医学籍表）
		 * @param studentId
		 * @return
		 * @throws Exception
		 */
		@Override
		public List<Map<String, Object>> findFinalExamResults(String studentId) throws Exception {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("studentId", studentId);
			StringBuffer sql = new StringBuffer("");
			sql.append("select studentid,decode(examclasstype,'2','▲','')||coursename||'/'||stydyhour courseInfo,term,max(score) score from "); 
			sql.append("((select er.studentid,er.majorcourseid,c.coursename,pc.stydyhour,pc.examclasstype,csspc.term,to_number(nvl(f_decrypt_score(er.integratedscore,er.studentid),0)) score,er.examabnormity ");
			sql.append("from edu_teach_examresults er ");
			sql.append("inner join edu_roll_studentinfo so on er.studentid=so.resourceid and so.isdeleted=0 ");
			sql.append("inner join edu_teach_plancourse pc on pc.resourceid=er.majorcourseid and pc.isdeleted=0 ");
			sql.append("inner join (select css.plancourseid,css.schoolids,(to_number(substr(css.term,0,4))-y.firstyear)*2+to_number(substr(css.term,7,1)) term,gp.planid,gp.gradeid "); 
			sql.append("from edu_teach_coursestatus css,edu_teach_guiplan gp,edu_base_grade g,edu_base_year y where css.guiplanid=gp.resourceid and gp.gradeid=g.resourceid "); 
			sql.append("and g.yearid=y.resourceid and y.isdeleted=0 and g.isdeleted=0 and css.isdeleted=0 and gp.isdeleted=0 and css.isopen='Y' and css.openstatus='Y' ");
			sql.append("group by css.plancourseid,css.schoolids,css.term,gp.planid,gp.gradeid,y.firstyear) csspc on csspc.gradeid=so.gradeid "); 
			sql.append("and csspc.schoolids=so.branchschoolid and csspc.plancourseid=er.majorcourseid and pc.planid=csspc.planid and csspc.plancourseid=pc.resourceid ");
			sql.append("inner join edu_base_course c on pc.courseid=c.resourceid and c.isdeleted=0 ");
			sql.append("where er.isdeleted=0 ");
			sql.append("and er.studentid=:studentId ) ");
			sql.append("union "); 
			sql.append("(select ne.studentid, npc.resourceid majorcourseid,nc.coursename,npc.stydyhour,npc.examclasstype,ncsspc.term,to_number(nvl(ne.scoreforcount,'70')) score,'0' examabnormity ");
			sql.append("from edu_teach_noexam ne "); 
			sql.append("inner join edu_roll_studentinfo nso on ne.studentid=nso.resourceid and nso.isdeleted=0 ");
			sql.append("inner join edu_teach_plancourse npc on npc.courseid=ne.courseid and npc.planid=nso.teachplanid and npc.isdeleted=0 ");
			sql.append("inner join (select css.plancourseid,css.schoolids,(to_number(substr(css.term,0,4))-y.firstyear)*2+to_number(substr(css.term,7,1)) term,gp.planid,gp.gradeid ");
			sql.append("from edu_teach_coursestatus css,edu_teach_guiplan gp,edu_base_grade g,edu_base_year y where css.guiplanid=gp.resourceid and gp.gradeid=g.resourceid "); 
			sql.append("and g.yearid=y.resourceid and y.isdeleted=0 and g.isdeleted=0 and css.isdeleted=0 and gp.isdeleted=0 and css.isopen='Y' and css.openstatus='Y' ");
			sql.append("group by css.plancourseid,css.schoolids,css.term,gp.planid,gp.gradeid,y.firstyear) ncsspc on ncsspc.gradeid=nso.gradeid "); 
			sql.append("and ncsspc.schoolids=nso.branchschoolid and npc.resourceid=ncsspc.plancourseid and nso.teachplanid=ncsspc.planid "); 
			sql.append("inner join edu_base_course nc on ne.courseid=nc.resourceid and npc.courseid=nc.resourceid and nc.isdeleted=0 ");
			sql.append("where ne.isdeleted=0 "); 
			sql.append("and ne.checkstatus='1' "); 
			sql.append("and ne.studentid=:studentId )) ");
			sql.append("group by studentid,majorcourseid,coursename,stydyhour,examclasstype,term ");
			sql.append("order by studentid,term ");
			
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		}
	
		/**
		 * 获取补考签到表
		 * @param condition
		 * @return
		 * @throws Exception
		 */
		@Override
		public List<StudentSignatureVo> findFailStudentSignature(Map<String, Object> condition) throws Exception {
			List<ExamResultsMakeUpVo> list = new ArrayList<ExamResultsMakeUpVo>();
			
			String examResultsTimes=null;
			if(CacheAppManager.getSysConfigurationByCode("examResultsTimes")!=null){
				examResultsTimes=CacheAppManager.getSysConfigurationByCode("examResultsTimes").getParamValue();
			}
	        if(examResultsTimes!=null){
				int ksNum=  Integer.parseInt(examResultsTimes);
				condition.put("ksNum", ksNum+1);
			}
			StringBuffer sql = getNonStuNoSql(condition);
		
			try {
				return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StudentSignatureVo.class, condition);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		/* 获取补考名单sql */
		public StringBuffer getNonStuNoSql(Map<String, Object> condition) {
			StringBuffer sql = new StringBuffer();
	
			sql.append("select stu.studyno studyNo1")
					.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid=a.studentid  ")
					.append(" join hnjk_sys_unit unit on stu.branchschoolid=unit.resourceid")
					.append(" join edu_base_major major on stu.majorid=major.resourceid")
					.append(" join edu_base_course c on a.courseid = c.resourceid   ");
	
			sql.append(" left join (select * from edu_teach_examresults re where re.isdeleted=0 and re.examsubid = :examSubId and re.courseid=:courseId) edumakeup on stu.resourceid=edumakeup.studentid ");
			sql.append("  left join edu_teach_graduatedata gd on gd.studentid = a.studentid and gd.isdeleted = 0 ");
			sql.append("where a.isdeleted=0 and (stu.studentstatus='11' or stu.studentstatus='21' or (stu.studentstatus = '24' and gd.isallowsecgraduate = 'Y')) ");
	
			if (condition.containsKey("ksNum")) {
				sql.append(" and  (select count(*) from EDU_TEACH_EXAMRESULTS re "
						+ "where re.isdeleted = 0 and re.COURSEID = a.courseid "
						+ "and re.studentid = a.studentid and re.checkstatus='4')<:ksNum ");
			}
			if (condition.containsKey("examSubId")) {
				sql.append(" and a.nextexamsubid=:examSubId ");
			}
			if (condition.containsKey("courseId")) {
				sql.append(" and c.resourceid=:courseId ");
			}
			if (condition.containsKey("major")) {
				sql.append(" and major.resourceid=:major ");
			}
			if (condition.containsKey("gradeId")) {
				sql.append(" and stu.gradeid=:gradeId ");
			}
			if (condition.containsKey("branchschoolid")) {
				sql.append(" and stu.branchschoolid=:branchschoolid ");
			}
			// 如果有分班信息
			if (condition.containsKey("classesId")) {
				sql.append(" and stu.classesid = :classesId ");
			}
			// sql.append(" group by stu.resourceid,stu.studyno ");
			sql.append(" order by stu.studyno ");
	
			return sql;
		}
		/**
		 * 获取补考考试签到表
		 */
		@Override
		public List getFailStudentSignatureToPrint(
				Map<String, Object> condition) throws WebException {
			StringBuffer hql = new StringBuffer();
			Map<String, Object> para =new HashMap<String, Object>();
			hql.append("select distinct wm_concat(stu.studyno)stuno,c.coursename,unit.unitname,major.majorname,es.classesname,stu.teachplanid,max(s.term) courseterm,max(s.resourceid) coursestatusid ,ett.teachername , ebc.classicname,ete.batchname,tp.examclasstype ");
			hql.append(",stu.gradeid,stu.classesid,major.resourceid majorid,unit.resourceid unitid,c.resourceid courseid,a.plansourceid,max(tc.counts) counts,etes.examinputstarttime,etes.isReachTime,etes.examtype,s.examform,");
			hql.append("max("+convertSql("sst.resultstatus")+") checkStatus0, max("+convertSql("sbt.resultstatus")+") checkStatus1,max("+convertSql("pst.resultstatus")+") checkStatus4, ");
			hql.append("max("+convertSql("sst.resultstatus")+"+"+convertSql("sbt.resultstatus")+"+"+convertSql("pst.resultstatus")+") inputcount ,a.nextexamsubid examSubId");
			hql.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid ");
			hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
			hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
			hql.append(" join edu_base_course c on a.courseid = c.resourceid ");
			hql.append(" join edu_roll_classes es on stu.classesid = es.resourceid ");
			hql.append(" join edu_base_grade g on g.resourceid = stu.gradeid ");
			hql.append(" left join edu_teach_plancourse tp on tp.resourceid = a.plansourceid ");
			hql.append(" join edu_teach_guiplan gp on gp.planid=stu.teachplanid and gp.gradeid=stu.gradeid and gp.isdeleted=0 ");
			
			hql.append(" left join edu_teach_timetable ett on ett.plancourseid=tp.resourceid and  ett.isdeleted=0 and ett.courseid=c.resourceid and ett.classesid=stu.classesid ");
			hql.append(" left join edu_base_classic ebc  on ebc.resourceid = stu.classicid and ebc.isdeleted=0 ");
			hql.append(" left join edu_teach_examsub ete  on ete.resourceid =a.nextexamsubid and ete.isdeleted=0 ");
			
			hql.append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=tp.resourceid and s.schoolIds=stu.branchschoolid and s.isdeleted=0 ");
			// 状态查询需要
			hql.append(" left join ("+getFailTotalNumSql(condition)+") tc on  tc.countResourceid=a.plansourceid||'_'||stu.classesid||'_'||a.nextexamsubid ");

			hql.append(" left join ("+getFailCheckStatusNumSql("0")+") sst on sst.majorcourseid=tp.resourceid and sst.courseid=c.resourceid and sst.classesid=es.resourceid and sst.gradeid=g.resourceid and a.nextexamsubid=sst.examsubid ");
			hql.append(" left join ("+getFailCheckStatusNumSql("1")+") sbt on sbt.majorcourseid=tp.resourceid and sbt.courseid=c.resourceid and sbt.classesid=es.resourceid and sbt.gradeid=g.resourceid and a.nextexamsubid=sbt.examsubid ");
			hql.append(" left join ("+getFailCheckStatusNumSql("4")+") pst on pst.majorcourseid=tp.resourceid and pst.courseid=c.resourceid and pst.classesid=es.resourceid and pst.gradeid=g.resourceid and a.nextexamsubid=pst.examsubid ");
			hql.append(" left join edu_teach_graduateData gd on gd.studentid = stu.resourceid and gd.isdeleted = 0 ");
			hql.append(" join ( ");
			hql.append(" select sub.resourceid subid,sub.examtype,sub.term,eby.resourceid yearId,sub.examinputstarttime,case when sub.examinputstarttime<= sysdate then 'Y' else 'N' end as isReachTime from edu_teach_examsub sub "); 
			hql.append(" join edu_base_year eby on eby.resourceid = sub.yearid and eby.isdeleted = 0 ");
			hql.append(" where sub.isdeleted = 0 ");
			if (condition.containsKey("yearId")) {//年度
				hql.append(" and sub.yearId=:yearId ");
				para.put("yearId",condition.get("yearId"));
			}
			if (condition.containsKey("term")) {//学期
				hql.append(" and sub.term=:term ");
				para.put("term",condition.get("term"));
			}
			if (condition.containsKey("examType")) {//考试类型
				hql.append(" and sub.examType=:examType ");
				para.put("examType",condition.get("examType"));
			}
			hql.append(" )  etes on etes.subid = a.nextexamsubid ");
			if (condition.containsKey("jwyId")) {// 教务员录入自己负责的教学站班级的课程成绩
				hql.append(" inner join hnjk_sys_users users on users.unitid=unit.resourceid and users.resourceid=:jwyId ");
				para.put("jwyId", condition.get("jwyId"));
			}
			
			if(condition.containsKey("teachId")){// 登分老师录入自己负责的班级的课程成绩
				hql.append(" inner join edu_teach_courseteachercl ctl on ctl.CLASSESID = es.resourceid and ctl.isdeleted= 0 and c.resourceid = ctl.COURSEID and ctl.TEACHERID = :teachId ");
				para.put("teachId", condition.get("teachId"));
			}
			
			hql.append(" where stu.isdeleted=0 and (stu.studentstatus='11' or stu.studentstatus='21' or (stu.studentstatus='24' and gd.isAllowSecGraduate='Y') ) and a.isdeleted=0 ");
			
			if (condition.containsKey("examSubId")) {
				hql.append(" and a.nextexamsubid=:examSubId");
				para.put("examSubId",condition.get("examSubId"));
			}
			
			String examResultsTimes=null;
			if(CacheAppManager.getSysConfigurationByCode("examResultsTimes")!=null){
				examResultsTimes=CacheAppManager.getSysConfigurationByCode("examResultsTimes").getParamValue();
			}
			int ksnum=0;
	        if(examResultsTimes!=null){
				ksnum=  Integer.parseInt(examResultsTimes);
				para.put("ksnum", ksnum+1);
			}
	        if(para.containsKey("ksnum")){ 
	        	hql.append(" and  (select count(*) from EDU_TEACH_EXAMRESULTS re " +
	        			"where re.isdeleted = 0 and re.COURSEID = a.courseid and re.studentid = a.studentid and re.checkstatus='4')<:ksnum ");
	        }
			if (condition.containsKey("gradeId")) {
				 hql.append("  and stu.gradeid=:gradeId");
				 para.put("gradeId",condition.get("gradeId"));
			}
			if (condition.containsKey("branchSchool")) {
				hql.append("  and stu.branchschoolid=:unitId");
				para.put("unitId",condition.get("branchSchool"));
			}
			if (condition.containsKey("major")) {
				 hql.append(" and stu.majorid =:major");
				 para.put("major",condition.get("major"));
			}
			if (condition.containsKey("courseName")) {
				 hql.append("  and lower(c.coursename) like:courseName ");
				 para.put("courseName","%"+condition.get("courseName").toString().toLowerCase()+"%");
			}
			if (condition.containsKey("courseId")) {
				hql.append("  and a.courseid =:courseId ");
				para.put("courseId",condition.get("courseId"));
			}
			if (condition.containsKey("classesId")) {
				 hql.append("  and stu.classesid =:classesId");
				 para.put("classesId",condition.get("classesId"));
			}
			
			// 状态查询
			if(condition.containsKey("failResultStatus")){
				String _rs = (String)condition.get("failResultStatus");
				if("partSave".equals(_rs)) {// 部分保存
					hql.append(" and (( "+convertSql("sst.resultstatus")+">0 and "+convertSql("sst.resultstatus")+"<tc.counts) or ");
					hql.append(convertSql("sst.resultstatus")+"+"+convertSql("sbt.resultstatus")+"+"+convertSql("pst.resultstatus")+"=0) ");
				} else if("partSubmit".equals(_rs)) {//  部分提交
					hql.append(" and (0<"+convertSql("sbt.resultstatus")+" and "+convertSql("sbt.resultstatus")+"<tc.counts) ");
				} else if("partPublish".equals(_rs)) {// 部分发布
					hql.append(" and (0<"+convertSql("pst.resultstatus")+" and "+convertSql("pst.resultstatus")+"<tc.counts)");
				} else if("allSave".equals(_rs)) {// 全部保存
					hql.append(" and sst.resultstatus=tc.counts ");
				} else if("allSubmit".equals(_rs)) {// 全部提交
					hql.append(" and sbt.resultstatus=tc.counts ");
				} else if("allPublish".equals(_rs)) {// 全部发布
					hql.append(" and pst.resultstatus=tc.counts ");
				}
			}
			
			/*
			if (condition.containsKey("gradeidsVo")) {
				hql.append("  and stu.gradeid in ("+condition.get("gradeidsVo")+")");
			}
			if (condition.containsKey("majoridsVo")) {
				hql.append("  and major.resourceid in ("+condition.get("majoridsVo")+")");
			}
			if (condition.containsKey("unitidsVo")) {
				hql.append("  and unit.resourceid in ("+condition.get("unitidsVo")+")");
			}*/
			if (condition.containsKey("plansourceidsVo")) {
				 hql.append("  and a.plansourceid in ("+condition.get("plansourceidsVo")+")");
			}
			if (condition.containsKey("classesidsVo")) {
				hql.append("  and stu.classesid in ("+condition.get("classesidsVo")+")");
			}
			
			if (condition.containsKey("courseidsVo")) {
				hql.append("  and  c.resourceid in ("+condition.get("courseidsVo")+")");
			}
			
			hql.append(" group by unit.unitname,c.coursename ,major.majorname,es.classesname,stu.gradeid,stu.classesid,major.resourceid,unit.resourceid,etes.examtype,s.examform");
			hql.append(",c.resourceid,a.plansourceid,stu.teachplanid,a.nextexamsubid,etes.examinputstarttime,etes.isReachTime,ett.teachername , ebc.classicname ,ete.batchname,tp.examclasstype ");//,s.term
			hql.append(" order by a.nextexamsubid,unit.unitname,major.majorname,c.coursename ,es.classesname");
			
			List<Map<String,Object>> resultList= new ArrayList<Map<String,Object>>(0);
			try {
				resultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), para);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return resultList;
			
		}
		/**
		 * 获取（以考试批次、年级、班级、课程为条件）各成绩状态的人数SQL-补考
		 * @param resultStatus 成绩状态
		 * @return
		 */
		private String getFailCheckStatusNumSql(String resultStatus) {
			String sql ="SELECT sio.gradeid,sio.classesid,er1.courseid,er1.majorcourseid,sio.teachplanid,er1.examsubid, COUNT(sio.resourceid) resultstatus FROM edu_teach_examresults er1, "
					+"edu_roll_studentinfo sio left join edu_teach_graduatedata gd on gd.studentid = sio.resourceid and gd.isdeleted=0 , "
					+"edu_teach_examsub es1, "
					+"edu_teach_plancourse PC1 "
					+"WHERE er1.studentid=sio.resourceid "
					+"AND er1.examsubid=es1.resourceid "
					+"AND er1.majorcourseid=pc1.resourceid "
					+"AND pc1.isdeleted=0 "
					+"AND er1.isdeleted=0 "
					+"AND sio.isdeleted=0 "
					+"AND es1.isdeleted=0 "
					+"AND (sio.studentstatus=11 OR sio.studentstatus=21 or ( sio.studentstatus = '24' and gd.isAllowSecGraduate = 'Y' )) "
//					+"AND pc1.teachtype='facestudy' "
					+"AND es1.examtype!='N' "
					+"AND er1.checkstatus="+resultStatus
					+" GROUP BY sio.gradeid,sio.classesid,er1.courseid,er1.majorcourseid,sio.teachplanid,er1.examsubid ";
			
			return sql;
			
		}
		/**
		 * 获取某割考试批次某门课程补考的总人数-SQL
		 * @param condition
		 * @return
		 */
		private String getFailTotalNumSql(Map<String, Object> condition) {
			String sql = "select  mk.plansourceid||'_'||so.classesid||'_'||mk.nextexamsubid countResourceid,count(so.resourceid) counts from edu_teach_makeuplist mk "
				  + "join edu_roll_studentinfo so "
				  + "  on so.resourceid = mk.studentid and so.isdeleted=0 "
				  + " left join edu_teach_graduateData gd on gd.studentid = so.resourceid "
				  + "where mk.isdeleted = 0 "
				  + "  and (so.studentstatus = '11' or so.studentstatus = '21' or (so.studentstatus = '24' and gd.isAllowSecGraduate = 'Y') ) ";
				  if(condition.containsKey("ksNum")){
					  sql += " and (select count(*) "
							  + "        from EDU_TEACH_EXAMRESULTS er "
							  + "       where er.isdeleted = 0 "
							  + "         and er.COURSEID = mk.courseid "
							  + "        and er.studentid = mk.studentid "
							  + "         and er.checkstatus = '4') < "+condition.get("ksNum");
				  }
				  if (condition.containsKey("gradeId")) {
				  sql +="  and so.gradeid=:gradeId";
				}
				if (condition.containsKey("branchSchool")) {
					sql +="  and so.branchschoolid=:unitId";
				}
				if (condition.containsKey("major")) {
					sql +=" and so.majorid =:major";
				}
				if (condition.containsKey("courseId")) {
					sql +="  and mk.courseid =:courseId ";
				}
				if (condition.containsKey("classesId")) {
					sql +="  and so.classesid =:classesId";
				}
				sql += " group by mk.plansourceid, so.classesid, mk.nextexamsubid ";
			
			return sql;
		}
}
