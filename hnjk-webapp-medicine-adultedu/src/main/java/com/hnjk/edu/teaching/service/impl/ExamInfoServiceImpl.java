package com.hnjk.edu.teaching.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hnjk.core.foundation.utils.*;
import com.hnjk.edu.teaching.service.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.BaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.CourseExamPapers;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ICourseExamPapersService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
import com.hnjk.edu.recruit.service.IRecruitExamLogsService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.vo.ExamInfoVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
/**
 * 
 * <code>考试(课程安排)信息表ServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-17 上午10:15:07
 * @see 
 * @version 1.0
 */
@Transactional
@Service("examInfoService")
public class ExamInfoServiceImpl extends BaseServiceImpl<ExamInfo> implements IExamInfoService{
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private BaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
		
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("recruitExamLogsService")
	private IRecruitExamLogsService recruitExamLogsService;
	
	@Autowired
	@Qualifier("courseExamPapersService")
	private ICourseExamPapersService courseExamPapersService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;

	@Override
	public void deleteCourse(String c_id) {
		delete(c_id);
	}

	@Override
	public ExamInfo findExamInfoByCourseAndExamSub(String courseId, String examSubId) {
		StringBuffer hql = new StringBuffer();
					 hql.append(" from ExamInfo info where info.isDeleted=0");
					 hql.append("  and info.course.resourceid=?");
					 hql.append("  and info.examSub.resourceid=?");
		List<ExamInfo> list=(List<ExamInfo>) this.exGeneralHibernateDao.findByHql(hql.toString(), new String[]{courseId,examSubId});
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}else {
			return null;	
		}
	}

	/**
	 * 查找考试信息
	 * @param courseId
	 * @param examSubId
	 * @param teachType :networkTeach/faceTeach
	 * @return
	 */
	@Override
	public ExamInfo findExamInfoByCourseAndExamSubAndCourseType(String courseId, String examSubId, String teachType) {
		StringBuffer hql = new StringBuffer();
					 hql.append(" from ExamInfo info where info.isDeleted=0");
					 hql.append("  and info.course.resourceid=?");
					 hql.append("  and info.examSub.resourceid=?");
					 hql.append("  and info.examCourseType=? order by resourceid");
		List<ExamInfo> list=(List<ExamInfo>) this.exGeneralHibernateDao.findByHql(hql.toString(), new Object[]{courseId,examSubId, "networkTeach".equals(teachType) ?0:1});
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}else {
			return null;	
		}
	}

	/**
	 * 查找考试信息
	 * @param courseId
	 * @param examSubId
	 * @param examCourseType
	 * @return
	 */
	@Override
	public ExamInfo findExamInfoByCourseAndExamSubAndExamCourseType(String courseId, String examSubId, Integer examCourseType) {
		StringBuffer hql = new StringBuffer();
		hql.append(" from ExamInfo info where info.isDeleted=0");
		hql.append("  and info.course.resourceid=?");
		hql.append("  and info.examSub.resourceid=?");
		hql.append("  and info.examCourseType=? order by resourceid");
		List<ExamInfo> list=(List<ExamInfo>) this.exGeneralHibernateDao.findByHql(hql.toString(), new Object[]{courseId,examSubId,examCourseType});
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
	}

	/**
	 * 获取ExamInfoVo列表
	 */
	@Override
	public Page findExamInfoForExamResultsInput(Map<String, Object> condition, Page objectPage) {
		
		StringBuffer sql     = new StringBuffer();
		List<Object> 	list = new ArrayList<Object>();
		sql.append("    select  examInfo.examcoursecode,examInfo.examcoursetype,examInfo.ismachineexam,examInfo.resourceid as examInfoResourceId,course.resourceid courseid,course.COURSENAME,course.examType,examInfo.isabnormityend,");
		sql.append("           count(examResult.RESOURCEID) orderNumber,classes.classesname, classes.resourceid classesid,");
		if(condition.containsKey("flag")){
			sql.append(" sum(case when  (examResult.usCheckstatus is null or examResult.usCheckstatus = 0 or examResult.usCheckstatus = -1) and  (examResult.Checkstatus = 0 or examResult.Checkstatus = -1)  then 1 else 0 end ) memo  ");
		}else{
			sql.append(" sum(case when  examResult.Checkstatus = 0 or examResult.Checkstatus = -1 then 1 else 0 end ) memo  ");
		}
		sql.append("       from  EDU_TEACH_EXAMINFO examInfo");
		sql.append(" inner join  EDU_BASE_COURSE course on  examInfo.COURSEID     = course.RESOURCEID");
		sql.append(" join  EDU_TEACH_EXAMRESULTS examResult  on  examResult.EXAMINFOID = examInfo.RESOURCEID  ");
		sql.append(" join  edu_roll_studentinfo studentinfo  on  studentinfo.resourceid = examResult.studentid  ");
		sql.append(" join edu_teach_plancourse pc on pc.planid=studentinfo.teachplanid and pc.isdeleted=0 and pc.courseid=course.RESOURCEID");
		sql.append(" inner join edu_base_classic cl on studentinfo.classicid = cl.resourceid ");
		
		if (condition.containsKey("examSubId")){
			sql.append("    and  examResult.EXAMSUBID  =?");
			list.add(condition.get("examSubId"));
		}
		sql.append(" and  examResult.ISDELETED  = 0");
		sql.append(" join  edu_roll_classes classes  on  classes.resourceid = studentinfo.classesid  ");
		sql.append(" where  examInfo.ISDELETED    = 0");
		//sql.append(" and  (examInfo.EXAMCOURSETYPE=0 or examInfo.EXAMCOURSETYPE=3)");
		if (condition.containsKey("classic")){
			
			sql.append(" and cl.resourceid=? ");
			list.add(condition.get("classic"));
		}
		if (condition.containsKey("examSubId")){
			sql.append("    and  examInfo.EXAMSUBID    =? ");
			list.add(condition.get("examSubId"));
		}
		if (condition.containsKey("checkStatus")) {
			sql.append("    and  examResult.checkStatus=?");
			list.add(condition.get("checkStatus"));
		}
		if (condition.containsKey("courseId")) {
			sql.append("    and  course.resourceid=?");
			list.add(condition.get("courseId"));
		}
		if (condition.containsKey("classId")) {
			sql.append("    and  classes.resourceid=?");
			list.add(condition.get("classId"));
		}
		if (condition.containsKey("branchSchool")) {
			sql.append("    and  studentinfo.branchSchoolid=?");
			list.add(condition.get("branchSchool"));
		}
		if (condition.containsKey("courseName")) {
			sql.append("   and  course.COURSENAME     like '%"+condition.get("courseName")+"%'");
		}
		if (condition.containsKey("resultsCourseIds")) {
			sql.append("   and  course.resourceid in("+condition.get("resultsCourseIds")+")");
		}
		if (condition.containsKey("teachType")) {
			sql.append("   and  pc.teachtype=?");
			list.add(condition.get("teachType"));
		}

		if (condition.containsKey("courseTeachType")) {
			Integer examCourseCode = "networkTeach".equals(condition.get("courseTeachType").toString()) ?0:1;
			sql.append(" and examInfo.examcoursecode=?");
			list.add(examCourseCode);
		}else if (condition.containsKey("examCourseCode")) {
			sql.append("   and examInfo.examcoursecode like '%"+condition.get("examCourseCode").toString().toUpperCase()+"%'");
		}

		if (condition.containsKey("unitId")){
			sql.append("  and studentinfo.branchschoolid= '"+condition.get("unitId")+"' ");
		}
		
		sql.append(" group by  examInfo.examcoursecode,examInfo.isabnormityend,course.resourceid,course.COURSENAME,course.examType,examInfo.resourceid,examInfo.examcoursetype,examInfo.ismachineexam,classes.classesname,classes.resourceid ");
		sql.append(" order by  examInfo.examcoursecode");
		
		
		return baseSupportJdbcDao.baseJdbcTemplate.findList(objectPage, sql.toString(), list.toArray(), ExamInfoVo.class);
	}

	@Override
	public Page findExamInfoForAbnormitConfig(Map<String, Object> condition,
			Page objectPage) {
		StringBuffer sql     = new StringBuffer();
		List<Object> 	list = new ArrayList<Object>();
		sql.append("    select  examInfo.examcoursecode,examInfo.resourceid as examInfoResourceId,course.resourceid courseid,course.COURSENAME,examInfo.isabnormityend");
		sql.append("           ");
		sql.append("       from  EDU_TEACH_EXAMINFO examInfo");
		sql.append(" inner join  EDU_BASE_COURSE course on  examInfo.COURSEID     = course.RESOURCEID");
		sql.append(" left  join  EDU_TEACH_EXAMRESULTS examResult  on  examResult.EXAMINFOID = examInfo.RESOURCEID  ");
		if (condition.containsKey("examSubId")){
			sql.append("    and  examResult.EXAMSUBID  =?");
			list.add(condition.get("examSubId"));
		}
		sql.append("	    and  examResult.ISDELETED  = 0");
		sql.append("      where  examInfo.ISDELETED    = 0");
		sql.append("        and  (examInfo.ISMACHINEEXAM is null or examInfo.ISMACHINEEXAM='N') ");
		if (condition.containsKey("examSubId")){
			sql.append("    and  examInfo.EXAMSUBID    =? ");
			list.add(condition.get("examSubId"));
		}
		if (condition.containsKey("courseId")) {
			sql.append("    and  course.resourceid=?");
			list.add(condition.get("courseId"));
		}
		if (condition.containsKey("courseName")) {
			sql.append("   and  course.COURSENAME     like '%"+condition.get("courseName")+"%'");
		}
		if (condition.containsKey("resultsCourseIds")) {
			sql.append("   and  course.resourceid in("+condition.get("resultsCourseIds")+")");
		}
		if (condition.containsKey("examCourseCode")) {
			sql.append("   and examInfo.examcoursecode like '%"+condition.get("examCourseCode").toString().toUpperCase()+"%'");
		}
		sql.append(" and exists ( select * from  EDU_TEACH_EXAMRESULTS examResult  where   examResult.ISDELETED  = 0 and examResult.examinfoid =examInfo.resourceid  ");
		if (condition.containsKey("examSubId")){
			sql.append("    and  examResult.EXAMSUBID  =?");
			list.add(condition.get("examSubId"));
		}
		if (condition.containsKey("curUserId")) {
			sql.append(" and examResult.fillinmanid=?");
			list.add(condition.get("curUserId"));
		}
		sql.append(")");
		sql.append(" group by  examInfo.examcoursecode,examInfo.isabnormityend,course.resourceid,course.COURSENAME,examInfo.resourceid");
		sql.append(" order by  examInfo.examcoursecode");
		
		
		return baseSupportJdbcDao.baseJdbcTemplate.findList(objectPage, sql.toString(), list.toArray(), ExamInfoVo.class);
	}
	
	@Override
	public ExamInfo findExamInfoByCourseNameAndExamSubId(String courseName, String examSubId) {
		StringBuffer hql = new StringBuffer();
		 hql.append(" from ExamInfo info where info.isDeleted=0");
		 hql.append("  and info.course.courseName like '%"+courseName+"%'");
		 hql.append("  and info.examSub.resourceid=?");
		List<ExamInfo> list=(List<ExamInfo>) this.exGeneralHibernateDao.findByHql(hql.toString(), new String[]{examSubId});
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}else {
			return null;	
		}
	}
	/**
	 * 根据考试批次ID查找考试课程
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<Course> findExamInfoCourseBySubId(String examSubId) throws ServiceException {
		String hql ="select info.course from ExamInfo info where info.isDeleted=0 and info.examSub.resourceid=?";
		return (List<Course>) exGeneralHibernateDao.findByHql(hql,examSubId);
	}
	
	/**
	 * 根据批次获取考试批次课程相关信息
	 */
	@Override
	public List<ExamInfoVo> findExamResultsVOForCountExamResult(Map<String,Object> paramMap) throws Exception {
		
		List<ExamInfoVo> voList       = new ArrayList<ExamInfoVo>();
		Map<String,Object> numMap     = new HashMap<String, Object>();
		Map<String,Object> teacherMap = new HashMap<String, Object>();
		Map<String,Object> statusMap  = new HashMap<String, Object>();
		
		ExamSub examSub               = (ExamSub)paramMap.get("examSub");
		paramMap.put("examSubId",examSub.getResourceid());
		paramMap.put("yearInfoId",examSub.getYearInfo().getResourceid());
		paramMap.put("term", examSub.getTerm());
		paramMap.put("isDeleted",0);
		//考试课程SQL
		StringBuffer examInfoSQL  	  = new StringBuffer();
		examInfoSQL.append("    select info.RESOURCEID,course.COURSENAME,info.EXAMSTARTTIME,info.EXAMENDTIME, ");
		examInfoSQL.append("		   course.EXAMTYPE,course.RESOURCEID COURSEID,count(results.RESOURCEID) ORDERNUM");
		examInfoSQL.append("      from EDU_TEACH_EXAMINFO info  ");
		examInfoSQL.append("inner join EDU_BASE_COURSE course on info.COURSEID  = course.RESOURCEID ");
		examInfoSQL.append("left  join EDU_TEACH_EXAMRESULTS  results on results.EXAMSUBID = :examSubId and results.isDeleted=:isDeleted and results.COURSEID=course.RESOURCEID ");
		//过滤学习中心
		if (paramMap.containsKey("branchSchool")) {
			examInfoSQL.append(" inner join edu_roll_studentinfo stu on results.studentid = stu.resourceid and stu.branchschoolid =:branchSchool and stu.isDeleted =:isDeleted");
			
		}
		examInfoSQL.append("     where info.isDeleted =:isDeleted ");
		examInfoSQL.append("       and info.EXAMSUBID =:examSubId ");
		//过滤学习中心
		if (paramMap.containsKey("branchSchool")) {
			examInfoSQL.append("   and exists ( select rs.resourceid from EDU_TEACH_EXAMRESULTS rs  ");
			examInfoSQL.append(" inner join edu_roll_studentinfo stu on rs.studentid = stu.resourceid and stu.branchschoolid =:branchSchool ");
			examInfoSQL.append(" where rs.examinfoid = info.resourceid and rs.EXAMSUBID = :examSubId and rs.isDeleted=:isDeleted )");
		}
		examInfoSQL.append(" group by  course.COURSENAME,info.EXAMSTARTTIME,info.EXAMENDTIME,course.EXAMTYPE,info.RESOURCEID,course.RESOURCEID  ");
		List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examInfoSQL.toString(), paramMap);
		
		//实考人数SQL
		StringBuffer  examNumSQL      = new StringBuffer();
		examNumSQL.append(" select results.EXAMINFOID,count(results.RESOURCEID)EXAMNUM from EDU_TEACH_EXAMRESULTS results  ");
		//过滤学习中心
		if (paramMap.containsKey("branchSchool")) {
			examNumSQL.append(" inner join edu_roll_studentinfo stu on results.studentid = stu.resourceid and stu.branchschoolid =:branchSchool and stu.isDeleted =:isDeleted ");
		}
		examNumSQL.append(" where results.isDeleted=:isDeleted ");
		examNumSQL.append("    and results.EXAMABNORMITY='0'  ");
		examNumSQL.append("    and results.EXAMSUBID=:examSubId group by results.EXAMINFOID ");
		 
		List<Map<String,Object>> examNumList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examNumSQL.toString(),paramMap);//实考人实        
		for (Map<String,Object> map : examNumList) {
			numMap.put(map.get("EXAMINFOID").toString(),map.get("EXAMNUM"));
		}
		 
		//负责老师        
		StringBuffer taskSQL   		  = new StringBuffer();   
		taskSQL.append(" select t.courseid,u.cnname,m.mobile from EDU_TEACH_TEACHTASK t ");
		taskSQL.append(" left join hnjk_sys_users u on u.resourceid = t.teacherid ");
		taskSQL.append(" left join edu_base_edumanager m on u.resourceid = m.sysuserid  ");
		taskSQL.append(" where t.isdeleted = :isDeleted and t.taskstatus =:taskstatus ");
		taskSQL.append("   and t.YEARID=:yearInfoId and t.term=:term");
		paramMap.put("taskstatus", 3);      
		List<Map<String,Object>> teacherList  = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(taskSQL.toString(),paramMap);   //负责老师  
		
		for (Map<String,Object> map : teacherList) {
			teacherMap.put(map.get("courseid").toString(),map);
		}
		
		//成绩状态
		StringBuffer checkStatusBuffer = new StringBuffer();
		checkStatusBuffer.append(" select rs.examinfoid,rs.checkstatus ,count(rs.resourceid) counts ");
		checkStatusBuffer.append("   from edu_teach_examresults rs  ");
		//过滤学习中心
		if (paramMap.containsKey("branchSchool")) {
			checkStatusBuffer.append(" inner join edu_roll_studentinfo stu on rs.studentid = stu.resourceid and stu.branchschoolid =:branchSchool and stu.isDeleted =:isDeleted ");
			
		}
		checkStatusBuffer.append("  where rs.isdeleted = :isDeleted and rs.EXAMSUBID = :examSubId ");
		checkStatusBuffer.append(" group by rs.examinfoid,rs.checkstatus ");
		checkStatusBuffer.append(" order by rs.examinfoid ");
		
		List<Map<String,Object>> checkStatusList  = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(checkStatusBuffer.toString(),paramMap);   //成绩状态  
		
		for (Map<String,Object> map : checkStatusList) {
			String examInfo    = (String)    map.get("examinfoid");
			String CHECKSTATUS = (String)    map.get("checkstatus");
			BigDecimal COUNTS  = (BigDecimal)map.get("counts");
			String color       = "red";
			switch (Integer.valueOf(CHECKSTATUS)) {
			case 0:
				color       = "blue";
				break;
			case 1:
				color       = "green";
				break;
			case 2:
				color       = "red";
				break;
			case 3:
				color       = "green";
				break;	
			case 4:
				color       = "green";
				break;	
			default:
				color       = "red";
				break;
			}
			if (statusMap.containsKey(examInfo)) {
				String str     = (String) statusMap.get(examInfo);
				str           += "<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp";
				statusMap.put(examInfo,str);
			}else{
				statusMap.put(examInfo,"<font color='"+color+"'>"+JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", CHECKSTATUS)+"</font>:"+COUNTS.intValue()+"(人)&nbsp");
			}
		}
		
		if (null!=list && !list.isEmpty()) {  
			for (int i = 0; i < list.size(); i++) {
				
				Map examInfo         = (Map)list.get(i);
				
				String examInfoID    = examInfo.get("RESOURCEID").toString();
				String courseID      = examInfo.get("COURSEID").toString();
				String courseName    = null==examInfo.get("courseName")?"":examInfo.get("courseName").toString();
				String examType      = null==examInfo.get("EXAMTYPE")?"":examInfo.get("EXAMTYPE").toString();
				Date examStartDate   = null!=examInfo.get("EXAMSTARTTIME")?ExDateUtils.parseDate(examInfo.get("EXAMSTARTTIME").toString(), "yyyy-MM-dd HH:mm"):null;
				Date examEndDate     = null!=examInfo.get("EXAMENDTIME")?ExDateUtils.parseDate(examInfo.get("EXAMENDTIME").toString(), "yyyy-MM-ddHH:mm"):null;
				

				ExamInfoVo vo = new ExamInfoVo();
				vo.setCourseName(courseName);
				vo.setExamStartDate(examStartDate);
				vo.setExamEndDate(examEndDate);
				vo.setExamInfoResourceId(examInfoID);
				vo.setOrderNumber(examInfo.get("ORDERNUM").toString());
				if (numMap.containsKey(examInfoID)) {
					vo.setExamNumber(numMap.get(examInfoID).toString());
				}
				if (teacherMap.containsKey(courseID)) {
					Map<String,Object> teacher = (Map<String,Object>)teacherMap.get(courseID);
					vo.setTeacherName(null==teacher.get("cnname")?"":teacher.get("cnname").toString());
					vo.setTeacherPhone(null==teacher.get("mobile")?"":teacher.get("mobile").toString());
				}
				if (statusMap.containsKey(examInfoID)) {
					vo.setCheckStatus(statusMap.get(examInfoID).toString());
				}
				vo.setExamType(JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",examType));
				
				voList.add(vo);
			}
		}

		return voList;
	}

	/**
	 * 检查考试课程表是否被成绩关联
	 * @param resourceid
	 * @return
	 * @throws ServiceException
	 */

	@Override
	public boolean isLinkedByExamResults(String resourceid)throws ServiceException {
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isLinked       = false;
		String sql 		       = " select count(0) from edu_teach_examresults rs where rs.examinfoid = :examInfoId and rs.isdeleted =:isDeleted ";
		map.put("isDeleted", 0);
		map.put("examInfoId", resourceid);
		try {
			Long counts 	   = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql, map);
			if (counts>0) {
				isLinked = true;
			}
		} catch (Exception e) {
			logger.error("检查考试课程表是否被成绩关联出错：{}"+e.fillInStackTrace());
			return false;
		}

		return isLinked;
	}


	@Override
	public void batchCascadeDelete(String[] ids) throws ServiceException{
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				ExamInfo examInfo = get(id);
				if(Constants.BOOLEAN_YES.equals(examInfo.getIsMachineExam())){//机考
					boolean isLinked = this.isLinkedByExamResults(id);
					if (isLinked) {
						throw new ServiceException("不允许删除，所选考试计划已经安排了学生考试!");
					} else{
						delete(examInfo);		
					}
				} else {//非机考
					boolean isLinked = this.isLinkedByExamResults(id);
					if (isLinked) {
						throw new ServiceException("不允许删除，所选课程已有预约考试记录!");
					}else{
						delete(id);		
					}
				}						
			}
		}
	}
	/**
	 * 查找有预约的考试课程
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamInfo> findExistsExamOrderExamInfos(String examSubId) throws ServiceException {
		StringBuffer hql 		= new StringBuffer();
		Map<String,Object> map  = new HashMap<String, Object>();
		map.put("isDeleted", 0);
		map.put("examSubId", examSubId);
		map.put("examTyp1", 0L);      //考试形式为开卷
		map.put("examTyp2", 1L);	  //考试形式为闭卷
		map.put("examCourseType", 0); //网络教育考试课程
		map.put("isPractice","N");    //非实践类课程
		map.put("status",2);
		
		hql.append(" select info from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=:isDeleted ");
		hql.append(" and info.examSub.resourceid=:examSubId");
		hql.append(" and info.examCourseType=:examCourseType ");
		hql.append(" and (info.course.examType=:examTyp1 or info.course.examType=:examTyp2 or info.course.examType is null)");
		hql.append(" and info.course.isPractice=:isPractice");
		hql.append(" and exists( from "+StudentLearnPlan.class.getSimpleName()+" stp where stp.isDeleted=:isDeleted ");
		hql.append(" and stp.examInfo =info and stp.status=:status)");
		hql.append(" order by info.examStartTime,info.course.courseCode");
		
		return (List<ExamInfo>) exGeneralHibernateDao.findByHql(hql.toString(), map);
	}
	
	@Override
	public Page findExamInfoByCondition(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+ExamInfo.class.getSimpleName()+" where isDeleted = :isDeleted ");
		values.put("isDeleted", 0);		
		if (condition.containsKey("examInfoId")) {
			hql.append(" and  resourceid =:examInfoId ");
			values.put("examInfoId", condition.get("examInfoId"));
		}
		if(condition.containsKey("examSubId")){
			hql.append(" and examSub.resourceid =:examSubId ");
			values.put("examSubId", condition.get("examSubId"));
		}
		if(condition.containsKey("courseId")){
			hql.append(" and course.resourceid =:courseId ");
			values.put("courseId", condition.get("courseId"));
		}
		if(condition.containsKey("isMachineExam")){
			hql.append(" and isMachineExam =:isMachineExam ");
			values.put("isMachineExam", condition.get("isMachineExam"));
		}
		if(condition.containsKey("examCourseType")){
			hql.append(" and examCourseType =:examCourseType ");
			values.put("examCourseType", condition.get("examCourseType"));
		}
		//学院2016修改
		if(condition.containsKey("finalexaminfo_branchSchoolId")){
			hql.append(" and brSchool.resourceid =:finalexaminfo_branchSchoolId ");
			values.put("finalexaminfo_branchSchoolId", condition.get("finalexaminfo_branchSchoolId"));
			
		}
		if(condition.containsKey("fillinDateStart")){
			hql.append(" and examStartTime >= :fillinDateStart ");
			values.put("fillinDateStart", new Date(Long.parseLong(condition.get("fillinDateStart").toString())));
		}
		if(condition.containsKey("fillinDateEnd")){
			hql.append(" and examEndTime <= :fillinDateEnd ");
			values.put("fillinDateEnd", new Date(Long.parseLong(condition.get("fillinDateEnd").toString())));
		}
		
		if (condition.containsKey("queryHQL")) {
			hql.append(condition.get("queryHQL"));
		}
		hql.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	public void arrangeFinalExamInfo(String[] planIds, String examInfoId, String arrangeType) throws Exception {
		if(planIds!=null && planIds.length>0){
			ExamInfo examInfo = get(examInfoId);
			if("1".equals(arrangeType)){//安排机考
				CourseExamPapers paper = null;
				if("fixed".equals(examInfo.getExamPaperType())){//固定试卷
					paper = examInfo.getCourseExamPapers();//取得指定试卷
				} else {
					paper =  courseExamPapersService.fetchCourseExamPapersByRandom(examInfo.getCourseExamRules(), "final_exam", examInfo.getCourse().getCourseName()+System.currentTimeMillis());
					courseExamPapersService.save(paper);
				}	
				//学院2016修改
				if(!"N".equals(examInfo.getExamSub().getExamType())){ //如果是补考,就根据考试信息 给学生补预约记录  //学院2016修改
//					List<StudentLearnPlan> slplist=new ArrayList<StudentLearnPlan>();
					List<StudentLearnPlan> mkSavePlan = new ArrayList<StudentLearnPlan>();
					for (String planId : planIds) {
						StudentMakeupList smk=studentMakeupListService.get(planId);	
						// XXXXX
						/*StudentLearnPlan newLearnPlan = new StudentLearnPlan();
						newLearnPlan.setStudentInfo(smk.getStudentInfo());
						newLearnPlan.setStatus(2);
						newLearnPlan.setTeachingPlanCourse(smk.getTeachingPlanCourse());
						newLearnPlan.setYearInfo(examInfo.getExamSub().getYearInfo());
						newLearnPlan.setTerm(examInfo.getExamSub().getTerm());*/
						long _examCount = 2;
						if(examInfo.getExamSub()!=null){
							if("T".equals(examInfo.getExamSub().getExamType())){
								_examCount = 3;
							}else if("Q".equals(examInfo.getExamSub().getExamType())){
								_examCount = 4;
							}
						}
						
						ExamResults examResults = new ExamResults();
						examResults.setCourse(smk.getTeachingPlanCourse().getCourse());
						examResults.setMajorCourseId(smk.getTeachingPlanCourse().getResourceid());
						examResults.setStudentInfo(smk.getStudentInfo());
						examResults.setExamInfo(examInfo);
						examResults.setCheckStatus("-1");
						examResults.setExamAbnormity("0");
						examResults.setExamStartTime(examInfo.getExamStartTime());
						examResults.setExamEndTime(examInfo.getExamEndTime());
						examResults.setExamsubId(examInfo.getExamSub().getResourceid());
						examResults.setCourseType(smk.getTeachingPlanCourse().getCourseType());
						examResults.setPlanCourseTeachType(smk.getTeachingPlanCourse().getTeachType());
						examResults.setExamCount(_examCount );
						examResults.setCreditHour(smk.getTeachingPlanCourse().getCreditHour()!=null?smk.getTeachingPlanCourse().getCreditHour():null);
						examResults.setStydyHour(smk.getTeachingPlanCourse().getStydyHour()!=null?smk.getTeachingPlanCourse().getStydyHour().intValue():null);
						examResults.setCourseScoreType(examInfo.getCourseScoreType());
						examResults.setIsMachineExam(Constants.BOOLEAN_YES);
						
						if (null!=smk.getTeachingPlanCourse().getCourse().getExamType()) {
							examResults.setExamType(Integer.valueOf(smk.getTeachingPlanCourse().getCourse().getExamType().toString()));
						}
//						newLearnPlan.setExamResults(examResults);
						examResultsService.saveOrUpdate(examResults);
						// 将成绩绑定到学习计划中
						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,smk.getStudentInfo().getResourceid(),smk.getTeachingPlanCourse().getResourceid());
						StudentLearnPlan learnPlan  = null;        
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {
							// 获取开课信息
							TeachingPlanCourseStatus courseStatus = teachingPlanCourseStatusService.findOneByCondition(smk.getStudentInfo().getGrade().getResourceid(),smk.getStudentInfo().getTeachingPlan().getResourceid(),smk.getTeachingPlanCourse().getResourceid(),smk.getStudentInfo().getBranchSchool().getResourceid());
							if(courseStatus!=null){
								String year_term = courseStatus.getTerm();
								YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(year_term.split("_0")[0]));
								learnPlan = new StudentLearnPlan();
								learnPlan.setExamInfo(examResults.getExamInfo());
								learnPlan.setTeachingPlanCourse(smk.getTeachingPlanCourse());
								learnPlan.setExamResults(examResults);
								learnPlan.setStatus(2);
								learnPlan.setStudentInfo(examResults.getStudentInfo());
								learnPlan.setTerm(year_term.split("_0")[1]);
								learnPlan.setYearInfo(yearInfo);	
								learnPlan.setOrderExamYear(yearInfo);
								learnPlan.setOrderExamTerm(year_term.split("_0")[1]);
							}
						}		
						mkSavePlan.add(learnPlan);
//						slplist.add(newLearnPlan);
						RecruitExamLogs logs = new RecruitExamLogs();//生成考试状态记录
						logs.setStudentInfo(smk.getStudentInfo());
						logs.setBrSchool(smk.getStudentInfo().getBranchSchool());
						logs.setExamInfo(examInfo);			
						logs.setCourseExamPapers(paper);//预先生成试卷					
											
						recruitExamLogsService.save(logs);		
					}
					studentLearnPlanService.batchSaveOrUpdate(mkSavePlan);
					/*for (StudentLearnPlan plan : slplist) {
						plan.getExamResults().setExamInfo(examInfo);//更新成绩考试课程信息
						plan.getExamResults().setIsMachineExam(Constants.BOOLEAN_YES);
						plan.setExamInfo(examInfo);//更新学习计划考试信息					
					}
					studentLearnPlanService.batchSaveOrUpdate(slplist);
					for (StudentLearnPlan plan : slplist) {
						CourseExamPapers paper = null;
						if("fixed".equals(examInfo.getExamPaperType())){//固定试卷
							paper = examInfo.getCourseExamPapers();//取得指定试卷
						} else {
							paper =  courseExamPapersService.fetchCourseExamPapersByRandom(examInfo.getCourseExamRules(), "final_exam", examInfo.getCourse().getCourseName()+System.currentTimeMillis());
							courseExamPapersService.save(paper);
						}		
						RecruitExamLogs logs = new RecruitExamLogs();//生成考试状态记录
						logs.setStudentInfo(plan.getStudentInfo());
						logs.setBrSchool(plan.getStudentInfo().getBranchSchool());
						logs.setExamInfo(examInfo);			
						logs.setCourseExamPapers(paper);//预先生成试卷					
											
						recruitExamLogsService.save(logs);					
					}*/
				}else{
					for (String planId : planIds) {
						StudentLearnPlan plan = studentLearnPlanService.get(planId);
						
						plan.setExamInfo(examInfo);//更新学习计划考试信息		
						ExamResults examResults = new ExamResults();
						examResults.setCourse(plan.getTeachingPlanCourse().getCourse());
						examResults.setMajorCourseId(plan.getTeachingPlanCourse().getResourceid());
						examResults.setStudentInfo(plan.getStudentInfo());
						examResults.setExamInfo(examInfo);
						examResults.setCheckStatus("-1");
						examResults.setExamAbnormity("0");
						examResults.setIsMachineExam(Constants.BOOLEAN_YES);
						examResults.setExamStartTime(examInfo.getExamStartTime());
						examResults.setExamEndTime(examInfo.getExamEndTime());
						examResults.setExamsubId(examInfo.getExamSub().getResourceid());
						examResults.setCourseType(plan.getTeachingPlanCourse().getCourseType());
						examResults.setPlanCourseTeachType(plan.getTeachingPlanCourse().getTeachType());
						examResults.setExamCount(1L);
						examResultsService.saveOrUpdate(examResults);
						plan.setExamResults(examResults);//更新成绩考试课程信息
						//List<CourseOrder> courseOrders =courseOrderService.findByHql("from "+CourseOrder.class.getSimpleName()+" where isDeleted=? and studentInfo.resourceid=? and courseOrderStat.course.resourceid=? and orderExamYear.resourceid=? and orderExamTerm=? ",
						//														      0,plan.getStudentInfo().getResourceid(), examInfo.getCourse().getResourceid(),examInfo.getExamSub().getYearInfo().getResourceid(),examInfo.getExamSub().getTerm());
						
						//if(ExCollectionUtils.isNotEmpty(courseOrders)){
						//	CourseOrder courseOrder = courseOrders.get(0);
						//	courseOrder.setIsMachineExam(Constants.BOOLEAN_YES);
						//}
						
						/*CourseExamPapers paper = null;
						if("fixed".equals(examInfo.getExamPaperType())){//固定试卷
							paper = examInfo.getCourseExamPapers();//取得指定试卷
						} else {
							paper =  courseExamPapersService.fetchCourseExamPapersByRandom(plan.getExamInfo().getCourseExamRules(), "final_exam", plan.getExamInfo().getCourse().getCourseName()+System.currentTimeMillis());
							courseExamPapersService.save(paper);
						}	*/				
						
						RecruitExamLogs logs = new RecruitExamLogs();//生成考试状态记录
						logs.setStudentInfo(plan.getStudentInfo());
						logs.setBrSchool(plan.getStudentInfo().getBranchSchool());
						logs.setExamInfo(examInfo);			
						logs.setCourseExamPapers(paper);//预先生成试卷					
											
						recruitExamLogsService.save(logs);					
					}
				}
				
				/*
				 * 这是旧的逻辑（华教修改）
				 * for (String planId : planIds) {
					StudentLearnPlan plan = studentLearnPlanService.get(planId);
					plan.getExamResults().setExamInfo(examInfo);//更新成绩考试课程信息
					plan.getExamResults().setIsMachineExam(Constants.BOOLEAN_YES);
					plan.setExamInfo(examInfo);//更新学习计划考试信息					
					
					//List<CourseOrder> courseOrders =courseOrderService.findByHql("from "+CourseOrder.class.getSimpleName()+" where isDeleted=? and studentInfo.resourceid=? and courseOrderStat.course.resourceid=? and orderExamYear.resourceid=? and orderExamTerm=? ",
					//														      0,plan.getStudentInfo().getResourceid(), examInfo.getCourse().getResourceid(),examInfo.getExamSub().getYearInfo().getResourceid(),examInfo.getExamSub().getTerm());
					
					//if(ExCollectionUtils.isNotEmpty(courseOrders)){
					//	CourseOrder courseOrder = courseOrders.get(0);
					//	courseOrder.setIsMachineExam(Constants.BOOLEAN_YES);
					//}
					
					CourseExamPapers paper = null;
					if("fixed".equals(examInfo.getExamPaperType())){//固定试卷
						paper = examInfo.getCourseExamPapers();//取得指定试卷
					} else {
						paper =  courseExamPapersService.fetchCourseExamPapersByRandom(plan.getExamInfo().getCourseExamRules(), "final_exam", plan.getExamInfo().getCourse().getCourseName()+System.currentTimeMillis());
						courseExamPapersService.save(paper);
					}					
					
					RecruitExamLogs logs = new RecruitExamLogs();//生成考试状态记录
					logs.setStudentInfo(plan.getStudentInfo());
					logs.setBrSchool(plan.getStudentInfo().getBranchSchool());
					logs.setExamInfo(examInfo);			
					logs.setCourseExamPapers(paper);//预先生成试卷					
										
					recruitExamLogsService.save(logs);					
				}*/
			} else {
				Date today = new Date();
				if(today.after(examInfo.getExamStartTime())){//考试已经考试
					throw new ServiceException("考试已经开始，不能取消安排");
				} else {
					StringBuilder rsIds = new StringBuilder();
					if(!"N".equals(examInfo.getExamSub().getExamType())){// 补考
						List<StudentLearnPlan> mkUpdatePlan = new ArrayList<StudentLearnPlan>();
						for (String planId : planIds) {
							StudentMakeupList smk=studentMakeupListService.get(planId);	
							ExamResults _examResults =examResultsService.findUnique("from "+ExamResults.class.getSimpleName()+" where isDeleted=0 majorCourseId=? and studentInfo.resourceid=? and examsubId=? ", smk.getTeachingPlanCourse().getResourceid(),smk.getStudentInfo().getResourceid(),smk.getNextExamSubId());
							if(!"-1".equals(_examResults.getCheckStatus())){
								continue;
							}
							//清空座位信息
							rsIds.append(",'"+_examResults.getResourceid()+"'");
							List<RecruitExamLogs> recruitExamLogs =recruitExamLogsService.findByHql("from "+RecruitExamLogs.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and examInfo.resourceid=? ", smk.getStudentInfo().getResourceid(), examInfo.getResourceid());
							if(ExCollectionUtils.isNotEmpty(recruitExamLogs)){//删除考试状态记录
								recruitExamLogsService.delete(recruitExamLogs.get(0));
							}	
							// 删除补考成绩
							examResultsService.delete(_examResults);
							// 修改学习计划中的成绩
							List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,smk.getStudentInfo().getResourceid(),smk.getTeachingPlanCourse().getResourceid());
							StudentLearnPlan learnPlan  = null;        
							if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
								learnPlan = stuLearnPlan.get(0);
								learnPlan.setExamResults(smk.getExamResults());
								learnPlan.setExamInfo(smk.getExamResults().getExamInfo());
								learnPlan.setStatus(2);
								mkUpdatePlan.add(learnPlan);
							}
						}
						studentLearnPlanService.batchSaveOrUpdate(mkUpdatePlan);
						
					} else {// 正考
						for (String planId : planIds) {
							StudentLearnPlan plan = studentLearnPlanService.get(planId);
							ExamResults er = plan.getExamResults();
							if(er!=null && !"-1".equals(er.getCheckStatus())){
								continue;
							}
							plan.setExamInfo(null);//更新学习计划考试信息	
							plan.setExamResults(null);
							// 更新学习计划
							studentLearnPlanService.saveOrUpdate(plan);
							// 删除成绩
							baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap("update edu_teach_examresults set isdeleted=1 where resourceid='"+er.getResourceid()+"'", null);
							//清空座位信息
							rsIds.append(",'"+er.getResourceid()+"'");
							List<RecruitExamLogs> recruitExamLogs =recruitExamLogsService.findByHql("from "+RecruitExamLogs.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and examInfo.resourceid=? ", plan.getStudentInfo().getResourceid(), examInfo.getResourceid());
							if(ExCollectionUtils.isNotEmpty(recruitExamLogs)){//删除考试状态记录
								recruitExamLogsService.delete(recruitExamLogs.get(0));
							}		
						}
					}
					//清空座位信息
					if (rsIds.length()>1) {
						baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" delete from edu_teach_examdistribu d where d.examresultsid in("+rsIds.substring(1)+")",null);
					}
					
					/*旧的逻辑
					 * ExamInfo oldExamInfo = null;
					for (ExamInfo info : examInfo.getExamSub().getExamInfo()) {
						if(!Constants.BOOLEAN_YES.equals(info.getIsMachineExam()) && 
						   info.getCourse().getResourceid().equals(examInfo.getCourse().getResourceid())&&
//						   null!=info.getExamCourseType()&&0==info.getExamCourseType()){
						   info.getExamStartTime()==null){
							oldExamInfo = info;
							break;
						}
					}
					if(oldExamInfo!=null){
						StringBuilder rsIds = new StringBuilder();
						for (String planId : planIds) {
							StudentLearnPlan plan = studentLearnPlanService.get(planId);
							plan.getExamResults().setExamInfo(oldExamInfo);//更新成绩考试课程信息
							plan.getExamResults().setIsMachineExam(Constants.BOOLEAN_NO);
							plan.getExamResults().setExamroom(null);
							plan.getExamResults().setExamSeatNum(null);
							
							plan.setExamInfo(oldExamInfo);//更新学习计划考试信息		
							//清空座位信息
							rsIds.append(",'"+plan.getExamResults().getResourceid()+"'");
							
							//List<CourseOrder> courseOrders =courseOrderService.findByHql("from "+CourseOrder.class.getSimpleName()+" where isDeleted=? and studentInfo.resourceid=? and courseOrderStat.course.resourceid=? and orderExamYear.resourceid=? and orderExamTerm=? ",
							//	    													 0,plan.getStudentInfo().getResourceid(), examInfo.getCourse().getResourceid(),examInfo.getExamSub().getYearInfo().getResourceid(),examInfo.getExamSub().getTerm());
							//if(ExCollectionUtils.isNotEmpty(courseOrders)){
							//	CourseOrder courseOrder = courseOrders.get(0);
							//	courseOrder.setIsMachineExam(Constants.BOOLEAN_NO);
							//}							
							
							List<RecruitExamLogs> recruitExamLogs =recruitExamLogsService.findByHql("from "+RecruitExamLogs.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and examInfo.resourceid=? ", plan.getStudentInfo().getResourceid(), examInfo.getResourceid());
							if(ExCollectionUtils.isNotEmpty(recruitExamLogs)){//删除考试状态记录
								recruitExamLogsService.delete(recruitExamLogs.get(0));
							}					
						}
						if("Y".equals(examInfo.getExamSub().getExamType())){//如果是补考 就删掉   //学院2016修改
							studentLearnPlanService.batchDelete(planIds);
						}
						//清空座位信息
						if (rsIds.length()>1) {
							baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" delete from edu_teach_examdistribu d where d.examresultsid in("+rsIds.substring(1)+")",null);
						}
						
					}
					*/
				}
			}			
		}
	}

	//学院2016修改
	@Override
	public Page getstudentmakeuplist(ExamInfo ei,Page pag,Map<String,Object> condition){
		StringBuffer hql=new StringBuffer("from "+StudentMakeupList.class.getSimpleName()+" sml where sml.isDeleted=0 and sml.nextExamSubId='"+ei.getExamSub().getResourceid()+"' and sml.studentInfo.branchSchool.resourceid='"+ei.getBrSchool().getResourceid()+"' ");
		hql.append(" and sml.studentInfo.studentStatus='11'  and sml.studentInfo.isDeleted=0 ");
		hql.append(" and sml.course.resourceid='"+ei.getCourse().getResourceid()+"' ");
		hql.append(" and sml.isMachineExam='Y' ");
//		hql.append(" and  not exists(from "+StudentLearnPlan.class.getSimpleName()+" sl where sl.isDeleted=0 and sl.studentInfo.resourceid=sml.studentInfo.resourceid and sl.yearInfo.resourceid='"+ei.getExamSub().getYearInfo().getResourceid()+"'  ");
//		hql.append(" and sl.term ='"+ei.getExamSub().getTerm()+"' and sl.examInfo.resourceid is not null and sl.teachingPlanCourse.resourceid=sml.teachingPlanCourse.resourceid )");
//		hql.append(" and not exists(from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0 and f_decrypt_score( rs.integratedScore,rs.studentInfo.resourceid) >= 60 and rs.studentInfo.resourceid=sml.studentInfo.resourceid and rs.course.resourceid=sml.course.resourceid and rs.checkStatus=4 ) ");//2016-10-10修改
		if(condition.containsKey("currentIndex") && "1".equals(condition.get("currentIndex"))){
			hql.append(" and not exists(from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0  and rs.studentInfo.resourceid=sml.studentInfo.resourceid and rs.majorCourseId=sml.teachingPlanCourse.resourceid and rs.examsubId=sml.nextExamSubId ) ");//2016-10-10修改
		} else {
			hql.append(" and exists(from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0  and rs.studentInfo.resourceid=sml.studentInfo.resourceid and rs.majorCourseId=sml.teachingPlanCourse.resourceid and rs.examsubId=sml.nextExamSubId "
					+ "and (rs.examInfo.isMachineExam='Y'  or rs.examInfo.isMachineExam is null) and rs.examInfo.examCourseType=3 and rs.examInfo.isDeleted=0 ) ");//2016-10-10修改
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and sml.studentInfo.branchSchool.resourceid=:branchSchool");
		}
		
		if (condition.containsKey("studyNo")) {
			hql.append(" and sml.studentInfo.studyNo like  '%"+condition.get("studyNo").toString()+"%' ");
		}
		
		if (condition.containsKey("classic")) {
			hql.append(" and sml.studentInfo.classic.resourceid=:classic");
		}
		
		if (condition.containsKey("gradeid")) {
			hql.append(" and sml.studentInfo.grade.resourceid=:gradeid");
		}
		
		if (condition.containsKey("classes")) {
			hql.append(" and sml.studentInfo.classes.resourceid=:classes");
		}
		if (condition.containsKey("major")) {
			hql.append(" and sml.studentInfo.major.resourceid=:major");
		}
		
		if (condition.containsKey("name")) {
			hql.append(" and sml.studentInfo.studentName like '%"+condition.get("name").toString()+"%' ");
		}
		if (condition.containsKey("leType")) {
			hql.append(" and sml.studentInfo.teachingType=:leType");
		}
		
		return studentMakeupListService.findByHql(pag, hql.toString(), condition);
	}

	
	/**
	 * 查询有复审记录的考试课程
	 * @param condition
	 * @param objPage
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findExamInfoForExamReview(Map<String, Object> condition,Page objPage) throws ServiceException {
		StringBuffer sql 	 = new StringBuffer();
		List<Object> 	list = new ArrayList<Object>();
		
		sql.append(" select i.resourceid examInfoResourceId , i.examCourseType, i.examcoursecode examCourseCode, c.coursename courseName ,reviewNum.counts orderNumber ");
		sql.append("  from edu_teach_examinfo i ");
		sql.append("  inner join edu_base_course c on c.resourceid = i.courseid and c.isdeleted = ? ");
		list.add(0);
		sql.append("  inner join ( select rs.examinfoid, count(ars.resourceid) counts ");
		sql.append("   			     from  edu_teach_auditresults ars ");
		sql.append("  				inner join  edu_teach_examresults rs on rs.resourceid = ars.resultsid and rs.isdeleted =?  ");
		list.add(0);
		sql.append("  				 where  ars.isdeleted = ?  ");
		list.add(0);
		sql.append(" 				 and (ars.auditstatus=0 or ars.auditstatus is null) ");
		sql.append(" 				 and (ars.audittype=0 or ars.audittype is null) ");
		sql.append("				group by rs.examinfoid ");
		sql.append("			  ) reviewNum on reviewNum.examinfoid = i.resourceid ");
		
		sql.append("  where i.isdeleted = ?");
		list.add(0);
		//sql.append("	and (i.ismachineexam = 'N' or i.ismachineexam is null) ");
		if (condition.containsKey("examSubId")) {
			sql.append("    and i.examsubid = ? ");
			list.add(condition.get("examSubId"));
		}
		if (condition.containsKey("courseId")) {
			sql.append("    and i.courseid =  ? ");
			list.add(condition.get("courseId"));
		}
		if (condition.containsKey("examCourseCode")) {
			sql.append("  and i.examcoursecode like '%"+condition.get("examCourseCode")+"%' ");
		}
		sql.append("  order by i.examCourseType,reviewNum.counts desc  ");
		
		return baseSupportJdbcDao.baseJdbcTemplate.findList(objPage, sql.toString(), list.toArray(), ExamInfoVo.class);
	}


	/**
	 * 查询有复审记录的考试课程
	 * @param ids
	 * @param examInfo
	 * @return
	 * @throws Exception 
	 * @throws ServiceException
	 */
	@Override
//	public void examInfoConfig(String ids, String studyScorePer,String courseScoreType, StringBuffer msg) throws Exception {
	public void examInfoConfig(String ids, ExamInfo examInfo, StringBuffer msg) throws Exception {
		
		List<ExamInfo> saveList = new ArrayList<ExamInfo>();
		Map<String,Object> map  = new HashMap<String, Object>();
		List<Object[]> param    = new ArrayList<Object[]>();
		List<ExamInfo> list 	= findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.in("resourceid", ids.split(",")));
		String courseScoreType  = examInfo.getCourseScoreType();
		Integer examCourseType  = examInfo.getExamCourseType();
		Double studyScorePerD	= examInfo.getStudyScorePer();
		Double facestudyScorePerD = examInfo.getFacestudyScorePer();
		Double facestudyScorePerD2 = examInfo.getFacestudyScorePer2();
		Double facestudyScorePerD3 = examInfo.getFacestudyScorePer3();
		Double netsidestudyScorePer = examInfo.getNetsidestudyScorePer();
		
		String sql1             = " select count(0) from edu_teach_examresults rs where rs.examinfoid =:examInfoId and rs.isdeleted =:isDeleted and cast ( rs.checkstatus  as int )>2 ";
		String sql2             = " update edu_teach_examresults rs set rs.coursescoretype=? where rs.examinfoid = ? and rs.isdeleted =? ";
		
		map.put("isDeleted", 0);
		for (ExamInfo info :list) {
			map.put("examInfoId", info.getResourceid());
			Long counts         = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql1, map);
			if (counts<=0) {
				if (examCourseType == 1) {
					info.setFacestudyScorePer(facestudyScorePerD);
					info.setFacestudyScorePer2(facestudyScorePerD2);
				} else {
					info.setStudyScorePer(studyScorePerD);
					info.setNetsidestudyScorePer(netsidestudyScorePer);
				}
				info.setFacestudyScorePer3(facestudyScorePerD3);
				if(ExStringUtils.isNotBlank(courseScoreType)){
					info.setCourseScoreType(courseScoreType);
					Object [] p	= {courseScoreType,info.getResourceid(),0};
					param.add(p);
				}
				saveList.add(info);
			}else {
				msg.append("《"+info.getCourse().getCourseName()+"》成绩已审核，不允许设置成绩比例</br>");
			}
		}
		batchSaveOrUpdate(saveList);
		if (!param.isEmpty()) {
			baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(sql2, param);
		}
	}
	
	/**
	 * 保存考试课程信息
	 */
	@Override
	@SuppressWarnings("unchecked")
	public ExamInfo saveExamInfo(Double per, ExamSub examSub, Course course, Integer examCourseType, String isMachineExam) {
		ExamInfo examInfo 	   = null;
		try {
//			List<ExamInfo> list	   = (List<ExamInfo>)exGeneralHibernateDao.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 "
//					+ " and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? and info.isMachineExam=? order by info.resourceid", course.getResourceid(),
//					examSub.getResourceid(),examCourseType,isMachineExam);
			String sql = "select ef.* from edu_teach_examinfo ef where ef.isdeleted=0 and ef.examsubid=? and ef.courseid=? and ef.examCourseType=? and ef.isMachineExam=?";
			List<Object> param = new ArrayList<Object>();
			param.add(examSub.getResourceid());
			param.add(course.getResourceid());
			param.add(examCourseType);
			param.add(isMachineExam);
			List<ExamInfo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql,param.toArray() , ExamInfo.class);
			if (null!=list && !list.isEmpty()) {
				examInfo = list.get(0);
			} else {
				examInfo = new ExamInfo();
				examInfo.setCourse(course);	
				examInfo.setIsOutplanCourse("0");
				examInfo.setExamSub(examSub);					
				examInfo.setExamCourseType(examCourseType);
				examInfo.setIsMachineExam(isMachineExam);

				double per1 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue());
				double per2 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue());
				double per3 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue());
				if ((1 == examCourseType || 3 == examCourseType) && null != examSub.getFacestudyScorePer2()) {
					if (null != examSub.getWrittenScorePer()) {
						per = examSub.getWrittenScorePer();
					}
					if (null != examSub.getFacestudyScorePer()) {
						per1 = examSub.getFacestudyScorePer();
					}
					if (null != examSub.getFacestudyScorePer2()) {
						per2 = examSub.getFacestudyScorePer2();
					}
					if (null != examSub.getFacestudyScorePer3()) {
						per3 = examSub.getFacestudyScorePer3();
					}
					examInfo.setStudyScorePer(per);
					examInfo.setFacestudyScorePer(per1);
					examInfo.setFacestudyScorePer2(per2);
					examInfo.setFacestudyScorePer3(per3);
				} else if (2 != examCourseType) {
					if (null!=examSub.getNetsidestudyScorePer()) {
						per=examSub.getNetsidestudyScorePer();
					}
					examInfo.setStudyScorePer(per);
				} else {
					if (null != examSub.getWrittenScorePer()) {
						per = examSub.getWrittenScorePer();
						per1 = examSub.getWrittenScorePer();
						per2 = 100 - per1;
						per3 = 0d;
					}
					examInfo.setStudyScorePer(per);
					examInfo.setFacestudyScorePer(per1);
					examInfo.setFacestudyScorePer2(per2);
					examInfo.setFacestudyScorePer3(per3);
				}
				
				examInfo.setCourseScoreType(examSub.getCourseScoreType());
				Session session = exGeneralHibernateDao.getSessionFactory().openSession();
				Transaction tx = session.beginTransaction();
				session.merge(examInfo);
				tx.commit();
				session.close();
//				save(examInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return examInfo;
	}


	//学院2016修改
	@Override
	public void updateStulear(ExamInfo ei) throws Exception {
		String hql="from  "+StudentLearnPlan.class.getSimpleName()+" sl where sl.isDeleted=0 and sl.yearInfo.resourceid=? and sl.term=?  and sl.teachingPlanCourse.course.resourceid=? and sl.studentInfo.branchSchool.resourceid=?  ";
		List<Object>  param=new ArrayList<Object>();
		/*ExamInfo newei= new ExamInfo();
		newei.setCourse(ei.getCourse());
		newei.setBrSchool(ei.getBrSchool());
		newei.setExamSub(ei.getExamSub());
		ExamSub eb=ei.getExamSub();
		save(newei);
		eb.getExamInfo().add(newei);*/
		ExamSub eb=ei.getExamSub();
		param.add(eb.getYearInfo().getResourceid());
		param.add(eb.getTerm());
		param.add(ei.getCourse().getResourceid());
		param.add(ei.getBrSchool().getResourceid());
		List<StudentLearnPlan> slList=studentLearnPlanService.findByHql(hql, param.toArray());
		
		for (StudentLearnPlan sl : slList) {
			sl.setStatus(2);
//			sl.setExamInfo(ei);
		}
		studentLearnPlanService.batchSaveOrUpdate(slList);
		
	}

	@Override
	public Page getExamCount(Page pag) {
		String sql="select eb.batchname,u.unitname,c.coursename,count(*) ct from edu_learn_stuplan sl left join edu_teach_examinfo ei on ei.resourceid=sl.examinfoid left join edu_teach_examsub eb on eb.resourceid=ei.examsubid left  join edu_base_course c on c.resourceid=ei.courseid left join edu_roll_studentinfo st on st.resourceid=sl.studentid left join hnjk_sys_unit u on u.resourceid=st.branchschoolid where ei.examtype='0' and sl.isdeleted=0 and ei.examstarttime is not null group by eb.batchname,u.unitname,c.coursename";
		return baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(pag, sql, null);
		
	}

	/**
	 * 安排/取消期末机考
	 * @param planList
	 * @param mkList
	 * @param examInfoId
	 * @param arrangeType
	 * @throws Exception
	 */
	@Override
	public void operateFinalExamInfo(List<StudentLearnPlan> planList,List<StudentMakeupList> mkList, String examInfoId,String arrangeType) throws Exception {
		if(ExCollectionUtils.isNotEmpty(planList) || ExCollectionUtils.isNotEmpty(mkList)){
			ExamInfo examInfo = get(examInfoId);
			if("1".equals(arrangeType)){//安排机考
				CourseExamPapers paper = null;
				if("fixed".equals(examInfo.getExamPaperType())){//固定试卷
					paper = examInfo.getCourseExamPapers();//取得指定试卷
				} else {
					paper =  courseExamPapersService.fetchCourseExamPapersByRandom(examInfo.getCourseExamRules(), "final_exam", examInfo.getCourse().getCourseName()+System.currentTimeMillis());
					courseExamPapersService.save(paper);
				}	
				//学院2016修改
				if(!"N".equals(examInfo.getExamSub().getExamType())){ //如果是补考,就根据考试信息 给学生补预约记录  //学院2016修改
					List<StudentLearnPlan> mkSavePlan = new ArrayList<StudentLearnPlan>();
					for (StudentMakeupList  smk: mkList) {
						long _examCount = 2;
						if(examInfo.getExamSub()!=null){
							if("T".equals(examInfo.getExamSub().getExamType())){
								_examCount = 3;
							}else if("Q".equals(examInfo.getExamSub().getExamType())){
								_examCount = 4;
							}
						}
						ExamResults examResults = new ExamResults();
						examResults.setCourse(smk.getTeachingPlanCourse().getCourse());
						examResults.setMajorCourseId(smk.getTeachingPlanCourse().getResourceid());
						examResults.setStudentInfo(smk.getStudentInfo());
						examResults.setExamInfo(examInfo);
						examResults.setCheckStatus("-1");
						examResults.setExamAbnormity("0");
						examResults.setExamStartTime(examInfo.getExamStartTime());
						examResults.setExamEndTime(examInfo.getExamEndTime());
						examResults.setExamsubId(examInfo.getExamSub().getResourceid());
						examResults.setCourseType(smk.getTeachingPlanCourse().getCourseType());
						examResults.setPlanCourseTeachType(smk.getTeachingPlanCourse().getTeachType());
						examResults.setExamCount(_examCount);
						examResults.setCreditHour(smk.getTeachingPlanCourse().getCreditHour()!=null?smk.getTeachingPlanCourse().getCreditHour():null);
						examResults.setStydyHour(smk.getTeachingPlanCourse().getStydyHour()!=null?smk.getTeachingPlanCourse().getStydyHour().intValue():null);
						examResults.setCourseScoreType(examInfo.getCourseScoreType());
						examResults.setIsMachineExam(Constants.BOOLEAN_YES);
						
						if (null!=smk.getTeachingPlanCourse().getCourse().getExamType()) {
							examResults.setExamType(Integer.valueOf(smk.getTeachingPlanCourse().getCourse().getExamType().toString()));
						}
						examResultsService.saveOrUpdate(examResults);
						// 将成绩绑定到学习计划中
						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,smk.getStudentInfo().getResourceid(),smk.getTeachingPlanCourse().getResourceid());
						StudentLearnPlan learnPlan  = null;        
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {
							// 获取开课信息
							TeachingPlanCourseStatus courseStatus = teachingPlanCourseStatusService.findOneByCondition(smk.getStudentInfo().getGrade().getResourceid(),smk.getStudentInfo().getTeachingPlan().getResourceid(),smk.getTeachingPlanCourse().getResourceid(),smk.getStudentInfo().getBranchSchool().getResourceid());
							if(courseStatus!=null){
								String year_term = courseStatus.getTerm();
								YearInfo yearInfo = yearInfoService.getByFirstYear(Long.valueOf(year_term.split("_0")[0]));
								learnPlan = new StudentLearnPlan();
								learnPlan.setExamInfo(examResults.getExamInfo());
								learnPlan.setTeachingPlanCourse(smk.getTeachingPlanCourse());
								learnPlan.setExamResults(examResults);
								learnPlan.setStatus(2);
								learnPlan.setStudentInfo(examResults.getStudentInfo());
								learnPlan.setTerm(year_term.split("_0")[1]);
								learnPlan.setYearInfo(yearInfo);	
								learnPlan.setOrderExamYear(yearInfo);
								learnPlan.setOrderExamTerm(year_term.split("_0")[1]);
							}
						}		
						mkSavePlan.add(learnPlan);
//						studentLearnPlanService.saveOrUpdate(learnPlan);
						RecruitExamLogs logs = new RecruitExamLogs();//生成考试状态记录
						logs.setStudentInfo(smk.getStudentInfo());
						logs.setBrSchool(smk.getStudentInfo().getBranchSchool());
						logs.setExamInfo(examInfo);			
						logs.setCourseExamPapers(paper);//预先生成试卷					
											
						recruitExamLogsService.save(logs);			
					}
					studentLearnPlanService.batchSaveOrUpdate(mkSavePlan);
				}else{
					for (StudentLearnPlan plan: planList) {
						plan.setExamInfo(examInfo);//更新学习计划考试信息		
						ExamResults examResults = new ExamResults();
						examResults.setCourse(plan.getTeachingPlanCourse().getCourse());
						examResults.setMajorCourseId(plan.getTeachingPlanCourse().getResourceid());
						examResults.setStudentInfo(plan.getStudentInfo());
						examResults.setExamInfo(examInfo);
						examResults.setCheckStatus("-1");
						examResults.setExamAbnormity("0");
						examResults.setIsMachineExam(Constants.BOOLEAN_YES);
						examResults.setExamStartTime(examInfo.getExamStartTime());
						examResults.setExamEndTime(examInfo.getExamEndTime());
						examResults.setExamsubId(examInfo.getExamSub().getResourceid());
						examResults.setCourseType(plan.getTeachingPlanCourse().getCourseType());
						examResults.setPlanCourseTeachType(plan.getTeachingPlanCourse().getTeachType());
						examResults.setExamCount(1L);
						examResultsService.saveOrUpdate(examResults);
						plan.setExamResults(examResults);//更新成绩考试课程信息
						
						RecruitExamLogs logs = new RecruitExamLogs();//生成考试状态记录
						logs.setStudentInfo(plan.getStudentInfo());
						logs.setBrSchool(plan.getStudentInfo().getBranchSchool());
						logs.setExamInfo(examInfo);			
						logs.setCourseExamPapers(paper);//预先生成试卷					
											
						recruitExamLogsService.save(logs);					
					}
				}
				
			} else {
				Date today = new Date();
				if(today.after(examInfo.getExamStartTime())){//考试已经考试
					throw new ServiceException("考试已经开始，不能取消安排");
				} else {
					StringBuilder rsIds = new StringBuilder();
					if(!"N".equals(examInfo.getExamSub().getExamType())){// 补考
						List<StudentLearnPlan> mkUpdatePlan = new ArrayList<StudentLearnPlan>();
						for (StudentMakeupList  smk: mkList) {
							ExamResults _examResults =examResultsService.findUnique("from "+ExamResults.class.getSimpleName()+" where isDeleted=0 and majorCourseId=? and studentInfo.resourceid=? and examsubId=? ", smk.getTeachingPlanCourse().getResourceid(),smk.getStudentInfo().getResourceid(),smk.getNextExamSubId());
							if(!"-1".equals(_examResults.getCheckStatus())){
								continue;
							}
							//清空座位信息
							rsIds.append(",'"+_examResults.getResourceid()+"'");
							List<RecruitExamLogs> recruitExamLogs =recruitExamLogsService.findByHql("from "+RecruitExamLogs.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and examInfo.resourceid=? ", smk.getStudentInfo().getResourceid(), examInfo.getResourceid());
							if(ExCollectionUtils.isNotEmpty(recruitExamLogs)){//删除考试状态记录
								recruitExamLogsService.delete(recruitExamLogs.get(0));
							}	
							// 删除补考成绩
							examResultsService.delete(_examResults);
							// 修改学习计划中的成绩
							List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,smk.getStudentInfo().getResourceid(),smk.getTeachingPlanCourse().getResourceid());
							StudentLearnPlan learnPlan  = null;        
							if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
								learnPlan = stuLearnPlan.get(0);
								learnPlan.setExamResults(smk.getExamResults());
								learnPlan.setExamInfo(smk.getExamResults().getExamInfo());
								learnPlan.setStatus(2);
								mkUpdatePlan.add(learnPlan);
							}
						}
						studentLearnPlanService.batchSaveOrUpdate(mkUpdatePlan);
					} else {// 正考
						for (StudentLearnPlan plan: planList) {
							if(plan.getExamResults()!=null && !"-1".equals(plan.getExamResults().getCheckStatus())){
								continue;
							}
							//清空座位信息
							rsIds.append(",'"+plan.getExamResults().getResourceid()+"'");
							plan.setExamInfo(null);//更新学习计划考试信息	
							plan.setExamResults(null);
							// 更新学习计划
							studentLearnPlanService.saveOrUpdate(plan);
							List<RecruitExamLogs> recruitExamLogs =recruitExamLogsService.findByHql("from "+RecruitExamLogs.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and examInfo.resourceid=? ", plan.getStudentInfo().getResourceid(), examInfo.getResourceid());
							if(ExCollectionUtils.isNotEmpty(recruitExamLogs)){//删除考试状态记录
								recruitExamLogsService.delete(recruitExamLogs.get(0));
							}		
						}
						// 删除成绩
						baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap("update edu_teach_examresults set isdeleted=1 where resourceid in ("+rsIds.substring(1)+")",null);
					}
					//清空座位信息
					if (rsIds.length()>1) {
						baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" delete from edu_teach_examdistribu d where d.examresultsid in("+rsIds.substring(1)+")",null);
					}
				}
			}			
		}
	}

	/**
	 * 根据条件获取考试课程信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<ExamInfo> findExamInfoByCondition(Map<String, Object> condition) {
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuffer examInfoSql = new StringBuffer(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 " );
		if(condition.containsKey("resourceid")){
			examInfoSql.append(" and info.resourceid =:resourceid ");
			param.put("resourceid", condition.get("resourceid"));
		}else {
			if(condition.containsKey("courseId")){
				examInfoSql.append(" and info.course.isDeleted =0 and info.course.resourceid=:courseId ");
				param.put("courseId", condition.get("courseId"));
			}
			 if(condition.containsKey("examSubId")){
				 examInfoSql.append(" and info.examSub.isDeleted =0 and info.examSub.resourceid=:examSubId ");
				 param.put("examSubId", condition.get("examSubId"));
			 }
			 if(condition.containsKey("examCourseType")){
				 examInfoSql.append(" and info.examCourseType=:examCourseType ");
				 param.put("examCourseType", condition.get("examCourseType"));
			 }
			 if(condition.containsKey("isMachineExam")){
				 examInfoSql.append(" and info.isMachineExam=:isMachineExam ");
				 param.put("isMachineExam", condition.get("isMachineExam"));
			 }
		}
		String orderType = "desc";
		if(condition.containsKey("examCourseType")){
			if(condition.get("examCourseType").equals(0)) {
				orderType = "asc";
			}
		}
		examInfoSql.append(" order by info.examCourseType,info.resourceid "+orderType);
		return findByHql(examInfoSql.toString(), param);
	}
	@Override
	public ExamInfo saveExamInfoNow(ExamSub examSub, Course course, Integer examCourseType, String isMachineExam) throws Exception{
		ExamInfo examInfo ;
		//先查找是否存在，存在就返回该记录
		
		
		String hql = " from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0  and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? and info.isMachineExam=? order by info.resourceid";
		List<ExamInfo> list = this.findByHql(hql, course.getResourceid(),examSub.getResourceid(),examCourseType,isMachineExam);
		if(ExCollectionUtils.isNotEmpty(list) ){
			examInfo= list.get(0);
		}else{//不存在就马上插入一条记录			
			String insertSQL="insert into edu_teach_examinfo (RESOURCEID, EXAMSUBID, COURSEID,ISOUTPLANCOURSE,VERSION, ISDELETED,EXAMCOURSETYPE, ISMACHINEEXAM, ISSHOWSCORE,"
					+ "STUDYSCOREPER, ISABNORMITYEND, ISMIXTURE,COURSESCORETYPE,FACESTUDYSCOREPER, FACESTUDYSCOREPER2,  NETSIDESTUDYSCOREPER, TEACHTYPE ) "
					+ "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			List<Object[]> param      = new ArrayList<Object[]>();
			GUIDUtils.init();
			String resourceid 			 = GUIDUtils.buildMd5GUID(false);
			param.add(new Object[]{resourceid,examSub.getResourceid(),course.getResourceid(),"N",0,0,examCourseType,isMachineExam,"N",
					examSub.getFacestudyScorePer(),"N","N",examSub.getCourseScoreType(),examSub.getFacestudyScorePer(),examSub.getFacestudyScorePer2(),examSub.getNetsidestudyScorePer(),"11"});
					
			examInfo = this.findByHql(hql, course.getResourceid(),examSub.getResourceid(),examCourseType,isMachineExam).get(0);
		}
		
		return examInfo;
	}

	/**
	 *
	 * @param condition:examSubId,courseId,scoreStyle,isMachineExam,examCourseType
	 * @return
	 */
	@Override
	public ExamInfo getExamInfo(Map<String, Object> condition) {

		List<ExamInfo> list	   = findExamInfoByCondition(condition);

		ExamInfo info = null;
		if(list.size()>0){
			info = list.get(0);
		}
		Integer examCourseType = Integer.parseInt(condition.get("examCourseType").toString());
		//如果没有考试信息 或 考试信息课程类型（网络/面授）不一致，则新增考试信息
		if(info!=null && !info.getExamCourseType().equals(examCourseType) ||info==null){
			ExamSub examSub = null;
			if(info!=null){
				ExamInfo _info = new ExamInfo();
				ExBeanUtils.copyProperties(_info, info);
				_info.setCourse(info.getCourse());
				_info.setExamSub(info.getExamSub());
				info = _info;
				info.setResourceid(null);
				examSub = info.getExamSub();
			}else{
				String examSubId = condition.get("examSubId").toString();
				String courseId = condition.get("courseId").toString();
				String scoreStyle = ExStringUtils.isBlank(condition.get("scoreStyle"))?"11":condition.get("scoreStyle").toString();
				String isMachineExam = (String)condition.get("isMachineExam");
				examSub = examSubService.get(examSubId);
				info = new ExamInfo();
				info.setCourse(courseService.get(courseId));
				info.setIsOutplanCourse("0");
				info.setExamSub(examSub);
				if(ExStringUtils.isNotBlank(isMachineExam)){
					info.setIsMachineExam(isMachineExam);
				}
				info.setCourseScoreType(scoreStyle);

			}
			info.setExamCourseType(examCourseType);
			if ("N".equals(examSub.getExamType())) {
				//生成正考考试信息
				if (1 == examCourseType) {
					if (null != examSub.getFacestudyScorePer()) {
						info.setFacestudyScorePer(examSub.getFacestudyScorePer());
					}
					if (null != examSub.getFacestudyScorePer2()) {
						info.setFacestudyScorePer2(examSub.getFacestudyScorePer2());
					}
				} else if (0 == examCourseType) {
					if (null != examSub.getNetsidestudyScorePer()) {
						info.setNetsidestudyScorePer(examSub.getNetsidestudyScorePer());
					}
					if (null != examSub.getWrittenScorePer()) {
						info.setStudyScorePer(examSub.getWrittenScorePer());
					}
				}
			} else {
				//生成补考考试信息
				info.setIsOutplanCourse("0");
				//info.setExamCourseType(2);
				info.setIsMachineExam("N");
				info.setIsShowScore("N");
				info.setIsAbnormityEnd("N");
				info.setIsmixture("N");
				info.setCourseScoreType("11");
				info.setFacestudyScorePer(100.0);
				info.setFacestudyScorePer2(0.0);
				info.setFacestudyScorePer3(0.0);
			}
			save(info);
		}
		return info;
	}
}
