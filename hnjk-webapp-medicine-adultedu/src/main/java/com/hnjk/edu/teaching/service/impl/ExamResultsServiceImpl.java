package com.hnjk.edu.teaching.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentCheck;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.*;
import com.hnjk.edu.teaching.service.*;
import com.hnjk.edu.teaching.util.ScoreEncryptionDecryptionUtil;
import com.hnjk.edu.teaching.vo.*;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * <code>学生成绩Service实现</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-8-24 下午01:40:49
 * @see 
 * @version 1.0
 */
@Service("examResultsService")
@Transactional
public class ExamResultsServiceImpl extends BaseServiceImpl<ExamResults> implements IExamResultsService {
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("examResultsAuditService")
	private IExamResultsAuditService examResultsAuditService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;

	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;

	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("yearInfoService")
    private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;
	
	//申请缓考列表
	@Override
	public Page findExamDelayList(Map<String, Object> condition, Page objPage) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
			hql.append(" from "+ExamResults.class.getSimpleName()+" exam where exam.isDeleted=0");
			
			hql.append(" and exam.writtenScore is null");
			hql.append(" and exam.usuallyScore is null");
			hql.append(" and exam.integratedScore is null");
			hql.append(" and exam.examSeatNum is null");
			
		if (condition.containsKey("branchSchool")) {
			hql.append(" and exam.studentInfo.branchSchool.resourceid=:branchSchool");
		}
		
		if (condition.containsKey("gradeid")) {
			hql.append(" and exam.studentInfo.grade.resourceid=:gradeid");
		}
		
		if (condition.containsKey("major")) {
			hql.append(" and exam.studentInfo.major.resourceid=:major");
		}
		
		if (condition.containsKey("classic")) {
			hql.append(" and exam.studentInfo.classic.resourceid=:classic");
		}
		
		if (condition.containsKey("name")) {
			hql.append(" and exam.studentInfo.studentName like '%"+condition.get("name")+"%'");
		}
		
		if (condition.containsKey("studyNo")) {
			hql.append(" and exam.studentInfo.studyNo=:studyNo");
		}
		
			hql.append(" order by "+objPage.getOrderBy()+ " "+objPage.getOrder());
			
			
		return this.exGeneralHibernateDao.findByHql(objPage, hql.toString(), condition);
	}
	
	/**
	 * 根据专业课程ID(教学计划课程ID)+课程ID+学生ID查询一个学生考试成绩记录
	 */
	@Override
	public List<ExamResults>  findExamResultsByCondition(Map<String,Object> condition) throws ServiceException {
		
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer hql 		 = new StringBuffer ();
		param.put("isDeleted", 0);
	
		hql.append(" from ExamResults exam where exam.isDeleted=:isDeleted and exam.studentInfo is not null ");
		
		if (condition.containsKey("majorCourseId")) {
			hql.append(" and  exam.majorCourseId=:majorCourseId");
			param.put("majorCourseId", condition.get("majorCourseId"));
		}
		if (condition.containsKey("studentId")) {
			hql.append(" and  exam.studentInfo.resourceid=:studentId");
			param.put("studentId", condition.get("studentId"));
		}
		if (condition.containsKey("studyNo")){     
			hql.append(" and exam.studentInfo.studyNo=:studyNo");
			param.put("studyNo", condition.get("studyNo"));
		}	
		if (condition.containsKey("checkStatus")){  
			hql.append(" and exam.checkStatus=:checkStatus ");
			param.put("checkStatus", condition.get("checkStatus"));
		}
		if (condition.containsKey("checkStatus_in")) {
			hql.append(" and exam.checkStatus in(:checkStatus_in) ");
			param.put("checkStatus_in", condition.get("checkStatus_in"));
		}
		if (condition.containsKey("curUserId")){
			hql.append(" and exam.fillinManId=:curUserId");
			param.put("curUserId", condition.get("curUserId"));
		}
		if (condition.containsKey("classic")){      
			hql.append(" and exam.studentInfo.classic.resourceid=:classic");
			param.put("classic", condition.get("classic"));
		} else if (condition.containsKey("classicId")) {
			hql.append(" and exam.studentInfo.classic.resourceid=:classicId");
			param.put("classicId", condition.get("classicId"));
		}
		if (condition.containsKey("grade")){
			hql.append(" and exam.studentInfo.grade.resourceid=:grade");
			param.put("grade", condition.get("grade"));
		}else if (condition.containsKey("gradeid")){	  
			hql.append(" and exam.studentInfo.grade.resourceid=:gradeid");
			param.put("gradeid", condition.get("gradeid"));
		} else if (condition.containsKey("gradeId")) {
			hql.append(" and exam.studentInfo.grade.resourceid=:gradeId");
			param.put("gradeId", condition.get("gradeId"));
		}
		if (condition.containsKey("major")){		  
			hql.append(" and exam.studentInfo.major.resourceid=:major");
			param.put("major", condition.get("major"));
		}
		if(condition.containsKey("classes")){
			hql.append(" and exam.studentInfo.classes.resourceid=:classes");
			param.put("classes", condition.get("classes"));
		}else if (condition.containsKey("classesid")){		  
			hql.append(" and exam.studentInfo.classes.resourceid=:classesid");
			param.put("classesid", condition.get("classesid"));
		} else if (condition.containsKey("classesId")) {
			hql.append(" and exam.studentInfo.classes.resourceid=:classesId");
			param.put("classesId", condition.get("classesId"));
		}

		if (condition.containsKey("name")){		  
			hql.append(" and exam.studentInfo.studentName like '%"+condition.get("name").toString()+"%'");
		}
		if (condition.containsKey("examSub")){    
			hql.append(" and exam.examsubId=:examSub");
			param.put("examSub", condition.get("examSub"));
		}
		if (condition.containsKey("yearId")) {
			hql.append(" and exam.examInfo.examSub.yearInfo.resourceid=:yearId");
			param.put("yearId", condition.get("yearId"));
		}
		if (condition.containsKey("term")) {
			hql.append(" and exam.examInfo.examSub.term=:term");
			param.put("term", condition.get("term"));
		}
		if (condition.containsKey("examType")) {
			hql.append(" and exam.examInfo.examSub.examType=:examType");
			param.put("examType", condition.get("examType"));
		}
		if (condition.containsKey("examInfoId")){ 
			hql.append(" and exam.examInfo.resourceid=:examInfoId");
			param.put("examInfoId", condition.get("examInfoId"));
		}
		if (condition.containsKey("courseName")){ 
			hql.append(" and exam.course.courseName like '%"+condition.get("courseName").toString()+"%'");
		}
		if (condition.containsKey("courseId")){
			hql.append(" and exam.course.resourceid=:courseId");
			param.put("courseId", condition.get("courseId"));
		}else if (condition.containsKey("courseids")) {
			hql.append(" and exam.course.resourceid in(:courseids)");
			param.put("courseids", condition.get("courseids"));
		}
		if (condition.containsKey("learningStyle")) {
			hql.append(" and exam.studentInfo.learningStyle=:learningStyle");
			param.put("learningStyle", condition.get("learningStyle"));
		}
		if (condition.containsKey("unitId")){
			hql.append(" and exam.studentInfo.branchSchool.resourceid = :unitId ");
			param.put("unitId", condition.get("unitId"));
		}else if (condition.containsKey("branchSchool")) {
			hql.append(" and exam.studentInfo.branchSchool.resourceid = :branchSchool ");
			param.put("branchSchool", condition.get("branchSchool"));
		}
		if (condition.containsKey("studentStatus")){
			hql.append(" and exam.studentInfo.studentStatus in(:studentStatus) ");
			param.put("studentStatus", condition.get("studentStatus"));
		}
		if(condition.containsKey("resourceids")){
			hql.append(" and exam.resourceid in(:resourceids) ");
			param.put("resourceids", condition.get("resourceids"));
		}
		hql.append(" and (exam.studentInfo.classes.resourceid,exam.course.resourceid) in(select ctl.classesId.resourceid,ctl.courseid.resourceid from CourseTeacherCl ctl where ctl.isDeleted=0");
		if (condition.containsKey("teacherName")) {
			hql.append(" and ctl.teacherName =:teacherName");
			param.put("teacherName",condition.get("teacherName"));
		}
		if (condition.containsKey("courseTeachType")) {
			hql.append(" and ctl.courseStatusId.teachType =:courseTeachType");
			param.put("courseTeachType",condition.get("courseTeachType"));
		}
		hql.append(")");
		if (condition.containsKey("calssesAndCourseIds")) {
			hql.append(" ").append(condition.get("calssesAndCourseIds"));
		}
		if(condition.containsKey("addSql")){
			hql.append(" ").append(condition.get("addSql").toString());
		}
		hql.append(" order by exam.studentInfo.branchSchool.unitCode,exam.studentInfo.classes.classname,exam.studentInfo.studyNo");
		return (List<ExamResults>) this.exGeneralHibernateDao.findByHql(hql.toString(), param);
	}

	
	/**
	 * 根据条件查询学生考试成绩记录
	 */
	@Override
	public Page findExamResultByCondition(Map<String, Object> condition, Page objecPage) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();

		hql.append("from "+ExamResults.class.getSimpleName()+" examResults where examResults.isDeleted=0");
	
		// 学习中心 (没有考场合并记录)
		if (condition.containsKey("branchSchool") && !condition.containsKey("mergeExamBrschool")) {
			hql.append(" and examResults.studentInfo.branchSchool.resourceid=:branchSchool ");
			
		// 有其它学习中心需要合并到当前学习中习	
		}else if(condition.containsKey("branchSchool") && condition.containsKey("mergeExamBrschool")){
			hql.append(" and examResults.studentInfo.branchSchool.resourceid in ("+condition.get("mergeExamBrschool")+") ");
		}
		
		if (condition.containsKey("studyNo")){
			hql.append(" and examResults.studentInfo.studyNo=:studyNo");}
		
		if (condition.containsKey("classesid")){
			hql.append(" and examResults.studentInfo.classes.resourceid=:classesid");}

		if (condition.containsKey("checkStatus")) {
			hql.append(" and examResults.checkStatus=:checkStatus ");
		}

		if (condition.containsKey("curUserId")) {
			hql.append(" and examResults.fillinManId=:curUserId");
		}
		
		if (condition.containsKey("classic")){
			hql.append(" and examResults.studentInfo.classic.resourceid=:classic");}
		
		if (condition.containsKey("gradeid")){
			hql.append(" and examResults.studentInfo.grade.resourceid=:gradeid");}
		
		if (condition.containsKey("major")){
			hql.append(" and examResults.studentInfo.major.resourceid=:major");}
		
		if (condition.containsKey("name")){
			hql.append(" and examResults.studentInfo.studentName like '%"+condition.get("name").toString()+"%'");}
		
		if (condition.containsKey("examSub")){
			hql.append(" and examResults.examsubId=:examSub");}
		if (condition.containsKey("examInfoId")){
			hql.append(" and examResults.examInfo.resourceid=:examInfoId");}
		
		if (condition.containsKey("courseName")){
			hql.append(" and examResults.course.courseName like '%"+condition.get("courseName").toString()+"%'");}

		if (condition.containsKey("courseId")) {
			hql.append(" and examResults.course.resourceid=:courseId");
		}
		
		if (condition.containsKey("examTime")){
			hql.append(" and to_char(examResults.examStartTime,'yyyy-MM-dd') = to_char(to_date('"+condition.get("examTime").toString()+"','yyyy-MM-dd'),'yyyy-MM-dd')");}
		
		if (condition.containsKey("learningStyle")){
			hql.append(" and examResults.studentInfo.learningStyle=:learningStyle");}
		
		if (condition.containsKey("excludeAssigned")){//去除已安排过的记录
			hql.append(condition.get("excludeAssigned"));
		}
		if (condition.containsKey("studentStatus")){
			hql.append(" and examResults.studentInfo.studentStatus=:studentStatus");
		} else {
			hql.append(" and examResults.studentInfo.studentStatus in('11','12','16','21','24','25')");
		}

		if(condition.containsKey("teacherId")){//课程老师
			//hql.append(" and exists ( from "+TeachTask.class.getSimpleName()+" t where t.isDeleted=0 and t.taskStatus=3 and t.yearInfo.resourceid=examResults.examInfo.examSub.yearInfo.resourceid and t.term=examResults.examInfo.examSub.term and t.course.resourceid=examResults.course.resourceid and t.teacherId =:teacherId  ) ");
		}
		if(condition.containsKey("classId")){//班级
			hql.append(" and examResults.studentInfo.classes.resourceid = :classId ");
		}
		if(condition.containsKey("onlineExam")){//网考
			hql.append(" and examResults.course.examType=6 ");
		}
		if(condition.containsKey("notEmptyWrittenScore")){
			hql.append(" and examResults.writtenScore is not null ");
		}
		if(condition.containsKey("orderBy")){
			if("studyNoAsc".equals(condition.get("orderBy"))){
				hql.append(" order by examResults.studentInfo.studyNo asc ,");
			}else if("studyNoDesc".equals(condition.get("orderBy"))){
				hql.append(" order by examResults.studentInfo.studyNo desc ,");
			}else if("scoreAsc".equals(condition.get("orderBy"))){
				hql.append(" order by DECODE(examResults.integratedScore,null,-2,f_decrypt_score(examResults.integratedScore, examResults.studentInfo.resourceid)) asc ,");
			}else if("scoreDesc".equals(condition.get("orderBy"))){
				hql.append(" order by DECODE(examResults.integratedScore,null,-2,f_decrypt_score(examResults.integratedScore, examResults.studentInfo.resourceid)) desc ,");
			}else if("wscoreAsc".equals(condition.get("orderBy"))){
				hql.append(" order by DECODE(examResults.writtenScore,null,-2,f_decrypt_score(examResults.writtenScore, examResults.studentInfo.resourceid)) asc ,");
			}else if("wscoreDesc".equals(condition.get("orderBy"))){
				hql.append(" order by DECODE(examResults.writtenScore,null,-2,f_decrypt_score(examResults.writtenScore, examResults.studentInfo.resourceid)) desc ,");
			}
			hql.append("  " +objecPage.getOrderBy()+ " "+objecPage.getOrder());
			
		}else{
			hql.append(" order by "+objecPage.getOrderBy()+ " "+objecPage.getOrder());
		
		}
			
		
		return this.exGeneralHibernateDao.findByHql(objecPage, hql.toString(), condition);
	}
	/**
	 * 根据传入的ID使用IN语句查询ExamResults列表
	 */
	@Override
	public List<ExamResults> findExamResultListByIds(String ids) throws ServiceException {
		String [] idsArray = ids.split(","); 
		String  typeOfInId = "";
		for(String id:idsArray){
			   typeOfInId += ",'"+id+"'"; 
		}
		String hql         = " from ExamResults result where result.resourceid in("+typeOfInId.substring(1)+")";
		
		return (List<ExamResults>) this.exGeneralHibernateDao.findByHql(hql);
	}
	/**
	 * 查找已按排座位的ExamResults列表
	 */
	@Override
	public List<ExamResults> findHaveSeatExamResultList(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
			hql.append("from "+ExamResults.class.getSimpleName()+" result where result.isDeleted=0");
			
		if ("room".equals(condition.get("flag")) || "assigend".equals(condition.get("flag"))) {
			
			hql.append(" and result.examroom is not null ");
			hql.append(" and result.examSeatNum is not null ");
			
		}else if("unassigend".equals(condition.get("flag"))){
			
			hql.append(" and result.examroom is  null ");
			hql.append(" and result.examSeatNum is  null ");
			
		}
//		if (condition.containsKey("mergeExamBrschool")) {
//			hql.append(" and result.studentInfo.branchSchool.resourceid in("+condition.get("mergeExamBrschool")+")");
//			
//		}else {
//			hql.append(" and result.studentInfo.branchSchool.resourceid=:branchSchool");
//		}	
		if (condition.containsKey("examRoomId")) {
			hql.append(" and result.examroom.resourceid=:examRoomId");
		}
		
		if (condition.containsKey("studyNo")) {
			hql.append(" and result.studentInfo.studyNo=:studyNo");
		}
		
		if (condition.containsKey("classic")) {
			hql.append(" and result.studentInfo.classic.resourceid=:classic");
		}
		
		if (condition.containsKey("gradeid")) {
			hql.append(" and result.studentInfo.grade.resourceid=:gradeid");
		}
		
		if (condition.containsKey("major")) {
			hql.append(" and result.studentInfo.major.resourceid=:major");
		}
		
		if (condition.containsKey("name")) {
			hql.append(" and result.studentInfo.studentName like %'"+condition.get("name").toString()+"'%");
		}
		
		if (condition.containsKey("examSub")) {
			hql.append(" and result.examsubId=:examSub");
		}
		
		if (condition.containsKey("examInfoId")) {
			hql.append(" and result.examInfo.resourceid=:examInfoId");
		}
		
		if (condition.containsKey("courseId")) {
			hql.append(" and result.course.resourceid=:courseId");
		}
		
		if (condition.containsKey("startTime")) {
			hql.append(" and result.examInfo.examStartTime=:startTime");
		}
		
		if (condition.containsKey("endTime")) {
			hql.append(" and result.examInfo.examEndTime=:endTime");
		}
		
		if ("room".equals(condition.get("flag")) || "assigend".equals(condition.get("flag"))) {
			hql.append(" and exists ( select dis.resourceid from "+ExamDistribu.class.getSimpleName()+" dis where dis.isDeleted = 0 and dis.examResults.resourceid = result.resourceid)");
		}else if("unassigend".equals(condition.get("flag"))){	
			hql.append(" and not exists ( select dis.resourceid from "+ExamDistribu.class.getSimpleName()+" dis where dis.isDeleted = 0 and dis.examResults.resourceid = result.resourceid)");
		}	
			hql.append(" order by  result.course.resourceid,cast(result.examSeatNum as int)  ");		
			
		if (condition.containsKey("mergeExamBrschool")) {
			condition.remove("mergeExamBrschool");
		}
		if(condition.containsKey("flag")) {
			condition.remove("flag");
		}
		
		return  findByHql( hql.toString(),condition);
	}
	@Override
	public String findExamResultIdsForClearnExamRoom(Map<String,Object> condition) throws ServiceException {
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer hql 	   = new StringBuffer();
		hql.append(" select result.resourceid from ExamResults result where result.isDeleted=0");
		hql.append(" and result.examroom is not null ");
		hql.append(" and result.examSeatNum is not null ");
		
		if (condition.containsKey("mergeExamBrschool")) {
			hql.append(" and result.studentInfo.branchSchool.resourceid in("+condition.get("mergeExamBrschool")+")");
		}else {
			hql.append(" and result.studentInfo.branchSchool.resourceid=:branchSchool");
			map.put("branchSchool", condition.get("branchSchool"));
		}	
		
		if (condition.containsKey("examInfoIds")) {
			hql.append(" and result.examInfo.resourceid in("+condition.get("examInfoIds")+")");
		}
		
		if (condition.containsKey("examInfoId")){
			hql.append(" and result.examInfo.resourceid=:examInfoId");
			map.put("examInfoId", condition.get("examInfoId"));
		} 
		if (condition.containsKey("examRoomIds")){
			hql.append(" and result.examroom.resourceid in("+condition.get("examRoomIds")+")");
		}	
		if (condition.containsKey("examRoomId")){
			hql.append(" and result.examroom.resourceid=:examRoomId");
			map.put("examRoomId", condition.get("examRoomId"));
		}    
		if (condition.containsKey("examSubId")){
			hql.append(" and result.examsubId=:examSubId");
			map.put("examSubId", condition.get("examSubId"));
		}     
		if (condition.containsKey("courseId")){
			hql.append(" and result.course.resourceid=:courseId");
			map.put("courseId", condition.get("courseId"));
		}     
		if (condition.containsKey("name")){
			hql.append(" and result.studentInfo.studentName like '%"+condition.get("name")+"%'");
		}          
		if (condition.containsKey("major")){
			hql.append(" and result.studentInfo.major.resourceid=:major ");
			map.put("major", condition.get("major"));
		}         
		if (condition.containsKey("classic")){
			hql.append(" and result.studentInfo.classic.resourceid=:classic ");
			map.put("classic", condition.get("classic"));
		}      
		if (condition.containsKey("gradeid")){
			hql.append(" and result.studentInfo.grade.resourceid=:gradeid ");
			map.put("gradeid", condition.get("gradeid"));
		}       
		
		if (condition.containsKey("startTime")) {
			hql.append(" and result.examInfo.examStartTime=:startTime");
			map.put("startTime", condition.get("startTime"));
		}
			
		
		if (condition.containsKey("endTime")) {
			hql.append(" and result.examInfo.examEndTime=:endTime");
			map.put("endTime", condition.get("endTime"));
		}
	
		
		hql.append(" and exists (");
		hql.append("              select dis.resourceid from "+ExamDistribu.class.getSimpleName()+" dis where dis.isDeleted=0 and dis.examResults.resourceid=result.resourceid ");
		hql.append("			 )");
		
		List list = findByHql( hql.toString(),map);
		StringBuffer ids = new StringBuffer();
		for (int i=0;i<list.size();i++) {
			ids.append(",'"+list.get(i)+"'");
		}
		if (!list.isEmpty()) {
			return ids.toString().substring(1);
		}else {
			return "";
		}
		
	}
	@Override
	public int findExamResultCount(Map<String,Object> condition) throws ServiceException {
		Map<String,Object> param = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
			hql.append(" from ExamResults result where result.isDeleted=0");
		if (condition.containsKey("examSubID")) {
			hql.append("  and result.examsubId=:examSubID");
			param.put("examSubID", condition.get("examSubID"));
		}
		if (condition.containsKey("courseID")) {
			 hql.append("  and result.course.resourceid=:courseID");
			 param.put("courseID", condition.get("courseID"));
		}
		if (condition.containsKey("examRoomID")) {
			hql.append("  and result.examroom.resourceid=:examRoomID");
			param.put("examRoomID", condition.get("examRoomID"));
		}
		if (condition.containsKey("courseIds")) {
			 hql.append("  and result.course.resourceid in("+condition.get("courseIds")+")");
			 param.put("courseID", condition.get("courseID"));
		}
		List list= this.exGeneralHibernateDao.findByHql(hql.toString(), param);	         
		if (null!=list && !list.isEmpty()) {
			return list.size();
		}else {
			return 0;
		}
	}
	
	
	
	/**
	 * 根据考试批次ID,考试信息ID,查找ExamResultsVo列表
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamResultsVo> findExamResultVoList(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer();
		List<ExamResultsVo> resultList=new ArrayList<ExamResultsVo>();
		Map<String,Object> map = new HashMap<String, Object>();
		hql.append("from "+ExamResults.class.getSimpleName()+" examResult where examResult.isDeleted=0 ");
		
		if ("1".equals(condition.get("flag"))) {
			hql.append("	    and  (examResult.checkStatus   = -1 or examResult.checkStatus   = 0) ");
			//hql.append("	    and  examResult.examAbnormity = 0");
			//hql.append(" 	    and  examResult.writtenScore is null");
			//hql.append(" 	    and  examResult.integratedScore is null");
			//hql.append(" 		and  examResult.examInfo.isAbnormityEnd='Y'");
		}
		if ("1".equals(condition.get("flag2"))) {
			hql.append("	    and  (examResult.usCheckStatus   = -1 or examResult.usCheckStatus   = 0 or examResult.usCheckStatus is null ) ");
		}

		if (condition.containsKey("examSubId")) {
			hql.append(" AND examResult.examsubId=:examSubId");
			map.put("examSubId", condition.get("examSubId"));
		}
		
		if (condition.containsKey("examInfoId")){
			hql.append(" AND examResult.examInfo.resourceid=:examInfoId");
			map.put("examInfoId", condition.get("examInfoId"));
		}
		if (condition.containsKey("unitId")){
			hql.append(" and examResult.studentInfo.branchSchool.resourceid =:unitId");
			map.put("unitId", condition.get("unitId"));
		}
		//hql.append(" order by examResult.studentInfo.branchSchool.unitCode,examResult.course.resourceid,examResult.examSeatNum");		
		//hql.append(" order by examResult.studentInfo.branchSchool.unitCode,examResult.course.resourceid,examResult.studentInfo.studyNo asc ");
		//按年级做第一排序、第二排专业、第三排学号。
		hql.append(" order by examResult.studentInfo.grade.gradeName,examResult.studentInfo.major.majorCode,examResult.studentInfo.studyNo asc ");
		List<ExamResults> list =  findByHql(hql.toString(),map);;
		String isMixTrue = "false" ; 
		if(condition.containsKey("isMixTrue")){
			isMixTrue = condition.get("isMixTrue").toString();
		}
		if (null!=list&&!list.isEmpty()) {
			
			for (int i = 0; i < list.size(); i++) {
				
				ExamResults results=list.get(i);
				StudentInfo studentInfo = results.getStudentInfo();
				ExamResultsVo vo = new ExamResultsVo();
				if(null!=results.getExamroom()){
					vo.setExamroom(results.getExamroom().getExamroomName());
				}
				vo.setExamseatno(results.getExamSeatNum());
				vo.setSort(String.valueOf(i+1));
				vo.setBranchSchool(studentInfo.getBranchSchool().getUnitName());
				vo.setMajor(studentInfo.getMajor().getMajorName());
				vo.setName(studentInfo.getStudentBaseInfo().getName());
				vo.setStudyNo(studentInfo.getStudyNo());
				if ("1".equals(condition.get("flag"))) {
					vo.setUsuallyScore(results.getUsuallyScore());
					vo.setExamAbnormity("");
					vo.setIntegratedScore("");
					if("0".equals(ExStringUtils.trimToEmpty(results.getExamAbnormity()))){
						//vo.setWrittenScore("");
						vo.setWrittenScore(results.getWrittenScore());//2013-4-24 需求-考试管理功能升级 3.1.1 导出已录入的分数
					} else {
						vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", results.getExamAbnormity()));
					}					
					vo.setReferenceUsuallyScore(results.getUsuallyScore());
					if("true".equals(isMixTrue)){
						vo.setWrittenScore(results.getWrittenHandworkScore());
					}
				}else {
					vo.setUsuallyScore(results.getUsuallyScore());
					vo.setWrittenScore(results.getWrittenScore());
					if("true".equals(isMixTrue)){
						vo.setWrittenScore(results.getWrittenHandworkScore());
					}
					vo.setIntegratedScore(results.getIntegratedScore());	
				}
				if(condition.containsKey("needGradeAndClassic")){
					vo.setGrade(studentInfo.getGrade().getGradeName());
					vo.setClassic(studentInfo.getClassic().getShortName());
				}
				
				resultList.add(vo);
				vo = null;
				results = null;
			}
		}
		list=null;
		
		return  resultList;
	}

	/**
	 * 根据考试批次ID及成绩状态查找成绩列表
	 * @param examSubId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamResults> findExamResultListByExamSubAndCheckStatus(String examSubId) throws ServiceException {
		String hql = " from ExamResults examResult where examResult.isDeleted=0 ";
			   hql+= "  and examResult.examsubId=? and examResult.checkStatus='3'";
		return (List<ExamResults>) this.exGeneralHibernateDao.findByHql(hql, examSubId);
	}
	/**
	 * 根据考试批次ID及学号、课程查找成绩对象
	 * @param examSubId
	 * @param stuNo
	 * @param courseId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public ExamResults findExamResulByExamSubAndStuNoAndCourseId(String examSubId, String stuNo, String courseId, String classesId) throws ServiceException {
		
		String hql = " from "+ExamResults.class.getSimpleName()+" examResult where examResult.isDeleted=? ";
			   hql+= " and  examResult.examsubId=? and examResult.studentInfo.studyNo=? and examResult.course.resourceid=? ";
		List<ExamResults> list	= new ArrayList<ExamResults>(0);	
		if(ExStringUtils.isNotEmpty(classesId)){
			hql += " and examResult.studentInfo.classes.resourceid = ? ";
			list =  (List<ExamResults>) this.exGeneralHibernateDao.findByHql(hql,0,examSubId,stuNo,courseId,classesId);
		}else{
			list =  (List<ExamResults>) this.exGeneralHibernateDao.findByHql(hql,0,examSubId,stuNo,courseId); 
		}
		   
		if (null!=list && !list.isEmpty()) {
			return list.get(0);
		}else {
			return null;
		}
		
	}
	/**
	 * 撤销成绩(清零)
	 * @param paramMap
	 * @return
	 * @throws Exception 
	 */
	@Override
	public int revocationImprotResults(Map<String, Object> paramMap) throws Exception {
		
		String sql = "UPDATE EDU_TEACH_EXAMRESULTS SET WRITTENSCORE=null,USUALLYSCORE =null,INTEGRATEDSCORE=null,EXAMABNORMITY=null";
		if (null!=paramMap &&  ExamResults.REVOCATION_TYPE_ALL.equals(paramMap.get("revocationType"))) {
			 sql  += " WHERE EXAMSUBID = :examSubId ";
		}else if(null!=paramMap && ExamResults.REVOCATION_TYPE_COURSE.equals(paramMap.get("revocationType"))){
			 sql  += " WHERE EXAMSUBID = :examSubId and COURSEID=:courseId ";
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, paramMap);
	}
	/**
	 * 查询学籍所有成绩
	 */
	@Override
	public List<ExamResults> findExamResultsByStudentInfoId(String studentId) throws ServiceException {
		String hql ="from ExamResults examResult where examResult.isDeleted=0 ";
			   hql+=" and examResult.checkStatus ='4'";
		       hql+=" and examResult.studentInfo.resourceid=?";
		
		return (List<ExamResults>) this.exGeneralHibernateDao.findByHql(hql, studentId);
	}
	
	/**
	 * 获取成绩分布概况
	 */
	@Override
	public Map<String, Object> findExamResultsDistribution(Map<String,Object> condition) throws Exception {
		
		List<String> sectionList	     	 = new LinkedList<String>();
		List<Map<String,Object>> resList 	 = new ArrayList<Map<String,Object>>();
		
		Map<String,Double> totalScoreMap     = new HashMap<String, Double>();   //考试课程总分
		Map<String,Integer>   passNumMap     = new HashMap<String, Integer>();  //考试课程的通过人数
		Map<String,Integer>   sectionNumMap  = new HashMap<String, Integer>();  //考试课程的各成绩区间人数
		Map<String, Integer> examNumMap  	 = new HashMap<String, Integer>();  //考试课程实考人数        
		Map<String, Integer> absentNumMap	 = new HashMap<String, Integer>();  //考试课程缺考人数        
		Map<String, Integer> cheatNumMap 	 = new HashMap<String, Integer>();  //考试课程作弊人数    
		Map<String, Integer> noPageNumMap	 = new HashMap<String, Integer>();  //考试课程无卷人数    
		Map<String, Integer> otherNumMap 	 = new HashMap<String, Integer>();  //考试课程成绩状态为其它的人数    
		
		Map<String,Double> totalIntegratedScoreMap = new HashMap<String, Double>(); //考试课程综合总分
		Map<String,Integer> integratedPassNumMap = new HashMap<String, Integer>();  //考试课程的综合成绩通过人数
		Map<String,Integer> integratedSectionNumMap = new HashMap<String, Integer>();//考试课程综合成绩的各成绩区间人数
		
		DecimalFormat dcmFmt 				 = new DecimalFormat("0.0");        //格式化输出
		
		boolean success         	     	 = true;
		String errorMsg         	     	 = "";
		String examInfoIds      	     	 = "";
		String examSubId      		     	 = null==condition.get("examSubId")?"":condition.get("examSubId").toString();
		String examInfoId     		     	 = null==condition.get("examInfoId")?"":condition.get("examInfoId").toString();
		String [] startScore  		     	 = null==condition.get("startScore")?null:(String[]) condition.get("startScore");
		String [] endScore    		     	 = null==condition.get("endScore")?null:(String[]) condition.get("endScore");
		
		condition.put("isDeleted", 0);
		//如果未指定成绩分布区间，默认一个区间
		if (null==startScore || null==endScore) {
			startScore 		  		     	 = new String []{"90","81","60","0"};
			endScore  		    	     	 = new String []{"100","89","80","59"};	
		}
		//指定课程查询看成绩分布情况
		if (null!=examInfoId) {
			String [] examInfoIdArray = examInfoId.split(",");
			for (int i = 0; i < examInfoIdArray.length; i++) {
				examInfoIds    			    += ","+"'"+examInfoIdArray[i]+"'";
			}
			examInfoIds        			     = examInfoIds.substring(1);
		}
		//将成绩分布区间放入LIST
		for (int i = 0; i < startScore.length; i++) {
			if (ExStringUtils.isNotBlank(startScore[i]) && ExStringUtils.isNotBlank(endScore[i])) {
				sectionList.add(startScore[i]+"~"+endScore[i]);
			}
		}
		if (ExStringUtils.isNotEmpty(examSubId)) {
			//Long l = System.currentTimeMillis();
			
			//批次所有考试课程ID,课程名,预约人数	  
			StringBuffer examInfoSQL  	 	 =  new StringBuffer();
			examInfoSQL.append("    select info.RESOURCEID,info.ISMACHINEEXAM,info.examstarttime,info.examendtime,course.COURSENAME,count(results.RESOURCEID) ORDERNUM ");
			examInfoSQL.append("      from EDU_TEACH_EXAMINFO info ");
			examInfoSQL.append("inner join EDU_BASE_COURSE course on  info.COURSEID  = course.RESOURCEID ");
			examInfoSQL.append(" left join EDU_TEACH_EXAMRESULTS  results on results.examinfoid=info.resourceid  and results.isDeleted=:isDeleted  ");
			//过滤学习中心
			if (condition.containsKey("branchSchool")) {
				examInfoSQL.append(" inner join edu_roll_studentinfo stu on results.studentid = stu.resourceid and stu.branchschoolid =:branchSchool and stu.isDeleted =:isDeleted ");
			}
			examInfoSQL.append("     where info.EXAMSUBID =:examSubId  ");
			if (ExStringUtils.isNotEmpty(examInfoId)) {
				examInfoSQL.append("       and info.RESOURCEID in("+examInfoIds+") ");
			}
			examInfoSQL.append("       and info.isDeleted=:isDeleted ");
			//过滤学习中心
			if (condition.containsKey("branchSchool")) {
				examInfoSQL.append("   and exists ( select rs.resourceid from EDU_TEACH_EXAMRESULTS rs  ");
				examInfoSQL.append(" inner join edu_roll_studentinfo stu on rs.studentid = stu.resourceid and stu.branchschoolid =:branchSchool ");
				examInfoSQL.append(" where rs.examinfoid = info.resourceid and rs.EXAMSUBID = :examSubId and rs.isDeleted=:isDeleted )");
			}
			examInfoSQL.append("  group by course.COURSENAME,info.RESOURCEID ,info.ISMACHINEEXAM,info.examstarttime,info.examendtime");
				   
			List<Map<String,Object>> examInfoList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examInfoSQL.toString(), condition);
			
			//统计实考、缺考、作弊SQL
			StringBuffer countSQL            = new StringBuffer();
			countSQL.append(" select results.EXAMINFOID,results.EXAMABNORMITY,count(results.EXAMABNORMITY)COUNTS from EDU_TEACH_EXAMRESULTS results ");
			//过滤学习中心
			if (condition.containsKey("branchSchool")) {
				countSQL.append(" inner join edu_roll_studentinfo stu on results.studentid = stu.resourceid and stu.branchschoolid =:branchSchool and stu.isDeleted =:isDeleted ");
			}
			countSQL.append("  where results.isDeleted=:isDeleted and results.EXAMSUBID=:examSubId ");
			if (ExStringUtils.isNotEmpty(examInfoId)) {
				countSQL.append("    and results.EXAMINFOID in("+examInfoIds+") ");
			}
			countSQL.append("  group by results.EXAMABNORMITY,results.EXAMINFOID");
				   
			List<Map<String,Object>> countNumList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(countSQL.toString(), condition);		
			
			//获得考试课程的实考、缺考、作弊、无卷、其它、人数
	    	for (Map<String,Object> countMap :countNumList) {
	    		
	    		String infoId        	 	 = null==countMap.get("EXAMINFOID")?"":countMap.get("EXAMINFOID").toString();
				String examAbnoramity    	 = null==countMap.get("EXAMABNORMITY")?"":countMap.get("EXAMABNORMITY").toString();
				int counts               	 = null==countMap.get("COUNTS")?0:Integer.parseInt(countMap.get("COUNTS").toString());
				int examNum   			 	 = 0 ;  //实考人数
				int cheatNum  			 	 = 0 ;  //作弊人数
				int absentNum 			 	 = 0 ;  //缺考人数
				int noPageNum            	 = 0 ;  //无卷人数
				int otherNum             	 = 0 ;  //其它人数
				
				//正常
				if (examAbnoramity.equals(Constants.EXAMRESULT_ABNORAMITY_0)) {
					examNum   			     = examNumMap.containsKey(infoId)?examNumMap.get(infoId):0;
					examNum	 			    += counts;
				//作弊	
				}else if (examAbnoramity.equals(Constants.EXAMRESULT_ABNORAMITY_1)) {
					examNum   			 	 = examNumMap.containsKey(infoId)?examNumMap.get(infoId):0;
					examNum  			    += counts;
					cheatNum 			    += counts;
				//缺考	
				}else if (examAbnoramity.equals(Constants.EXAMRESULT_ABNORAMITY_2)) {
					absentNum		        += counts;
				//无卷	记入实考人数，当0分处理
				}else if (examAbnoramity.equals(Constants.EXAMRESULT_ABNORAMITY_3)){
					examNum   			     = examNumMap.containsKey(infoId)?examNumMap.get(infoId):0;
					examNum  			    += counts;
					noPageNum               += counts;
				//其它	记入实考人数，当0分处理
				}else if (examAbnoramity.equals(Constants.EXAMRESULT_ABNORAMITY_4)){
					examNum   			    = examNumMap.containsKey(infoId)?examNumMap.get(infoId):0;
					examNum  			    += counts;
					otherNum                += counts;
				}
				
				if (examNum>0) {
					examNumMap.put(countMap.get("EXAMINFOID").toString(), examNum);
				}
				if (cheatNum>0) {
					cheatNumMap.put(countMap.get("EXAMINFOID").toString(), cheatNum);
				}
				if (absentNum>0) {
					absentNumMap.put(countMap.get("EXAMINFOID").toString(), absentNum);
				}
				if (noPageNum>0) {
					noPageNumMap.put(countMap.get("EXAMINFOID").toString(), noPageNum);
				}
				if (otherNum>0) {
					otherNumMap.put(countMap.get("EXAMINFOID").toString(), otherNum);
				}
	    	}
	    	countNumList = null;
	    	//System.out.println("--------耗时1："+(System.currentTimeMillis()-l));
	    	
			//考试成绩SQL
			StringBuffer examScoreSQL		        = new StringBuffer();
			examScoreSQL.append("select results.EXAMINFOID,results.STUDENTID,results.WRITTENSCORE,results.examabnormity,results.INTEGRATEDSCORE from EDU_TEACH_EXAMRESULTS results ");
			//过滤学习中心
			if (condition.containsKey("branchSchool")) {
				examScoreSQL.append(" inner join edu_roll_studentinfo stu on results.studentid = stu.resourceid and stu.branchschoolid =:branchSchool and stu.isDeleted =:isDeleted ");
			}
			examScoreSQL.append(" where  results.EXAMSUBID=:examSubId  ");
			if (ExStringUtils.isNotEmpty(examInfoId)) {
				examScoreSQL.append("   and results.EXAMINFOID in("+examInfoIds+")  ");
			}
			examScoreSQL.append("   and  results.isDeleted=:isDeleted ");	   
			List<Map<String,Object>> scoreList  	= baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examScoreSQL.toString(),condition);
			
			//System.out.println("--------耗时2："+(System.currentTimeMillis()-l));
			
			ScoreEncryptionDecryptionUtil scoreUtil = ScoreEncryptionDecryptionUtil.getInstance();
		
    		for (int k = 0; k < scoreList.size(); k++){				   //遍历成绩列表
    			
    			Map<String,Object> scoreMap	 = (Map<String,Object>)scoreList.get(k);
    			String scoreExamInfoId       = scoreMap.get("EXAMINFOID").toString();//当前成绩的考试课程ID
				String scoreStr              = null==scoreMap.get("WRITTENSCORE")?"":scoreMap.get("WRITTENSCORE").toString();
				String studentId             = null==scoreMap.get("STUDENTID")?"":scoreMap.get("STUDENTID").toString();
				String examabnormity         = null==scoreMap.get("examabnormity")?"":scoreMap.get("examabnormity").toString();	
				String integratedScoreStr    = null==scoreMap.get("INTEGRATEDSCORE")?"":scoreMap.get("INTEGRATEDSCORE").toString();
				
				if ((Constants.EXAMRESULT_ABNORAMITY_3.equals(examabnormity)||Constants.EXAMRESULT_ABNORAMITY_4.equals(examabnormity))||(Constants.EXAMRESULT_ABNORAMITY_0.equals(examabnormity)&&ExStringUtils.isBlank(scoreStr))) {
					scoreStr                 = scoreUtil.encrypt(studentId, "0");
				}
				//成绩状态为 无卷 、其它这两种状态 ,记入实考人数，当0分处理
				Double score         		 = ("0".equals(examabnormity))?Double.parseDouble(scoreUtil.decrypt(studentId,scoreStr)):0;
				Double totalScore 			 = totalScoreMap.containsKey(scoreExamInfoId)?totalScoreMap.get(scoreExamInfoId):0D;
				totalScore      		    += score;
				totalScoreMap.put(scoreExamInfoId, totalScore);
				
				if ("0".equals(examabnormity)&& score >=60) {  
					int passNum      		 = passNumMap.containsKey(scoreExamInfoId)?passNumMap.get(scoreExamInfoId):0;
					passNum          		 = passNum +1;
					passNumMap.put(scoreExamInfoId, passNum);
				}
				//综合总分
				Double integratedScore       = ("0".equals(examabnormity)&&ExStringUtils.isNotBlank(integratedScoreStr))?Double.parseDouble(scoreUtil.decrypt(studentId,integratedScoreStr)):0;
				Double totalIntegratedScore  = totalIntegratedScoreMap.containsKey(scoreExamInfoId)?totalIntegratedScoreMap.get(scoreExamInfoId):0D;
				totalIntegratedScore	    += integratedScore;
				totalIntegratedScoreMap.put(scoreExamInfoId, totalIntegratedScore);
				if ("0".equals(examabnormity)&& integratedScore >=60) {  
					int passNum      		 = integratedPassNumMap.containsKey(scoreExamInfoId)?integratedPassNumMap.get(scoreExamInfoId):0;
					passNum          		 = passNum +1;
					integratedPassNumMap.put(scoreExamInfoId, passNum);
				}
				
	    		for (int j = 0; j < sectionList.size(); j++){				   //遍历给定的成绩段
	    			
	    			String []sections        = sectionList.get(j).split("~");
					Double tempStartScore    = Double.parseDouble(sections[0]);//给定成绩段的起始成绩
		    		Double tempEndScore      = Double.parseDouble(sections[1]);//给定成绩段的结束成绩
		    		int  sectionNum 		 = sectionNumMap.containsKey(scoreExamInfoId+sectionList.get(j))?sectionNumMap.get(scoreExamInfoId+sectionList.get(j)):0;
		    		int  integratedSectionNum = integratedSectionNumMap.containsKey(scoreExamInfoId+sectionList.get(j))?integratedSectionNumMap.get(scoreExamInfoId+sectionList.get(j)):0;
		    		
		    		if (("3".equals(examabnormity)||"4".equals(examabnormity))&&0==tempStartScore) {
		    			sectionNum     += 1;
						sectionNumMap.put(scoreExamInfoId+sectionList.get(j), sectionNum);
						integratedSectionNum += 1;
						integratedSectionNumMap.put(scoreExamInfoId+sectionList.get(j), integratedSectionNum);
						continue;
					}
					if ("0".equals(examabnormity)&&score>=tempStartScore&&score<=tempEndScore) {
						sectionNum     += 1;
						sectionNumMap.put(scoreExamInfoId+sectionList.get(j), sectionNum);
					}
					if ("0".equals(examabnormity)&&integratedScore>=tempStartScore&&integratedScore<=tempEndScore) {
						integratedSectionNum += 1;
						integratedSectionNumMap.put(scoreExamInfoId+sectionList.get(j), integratedSectionNum);
					}
	    		}	
    		}
    		
    		scoreList = null;
    		
			for (Map<String,Object> examInfo :examInfoList) {
					
				String examinfoId    = examInfo.get("RESOURCEID").toString();
				int passNum          = passNumMap.containsKey(examinfoId)? passNumMap.get(examinfoId):0;//合格成绩个数
				int integratedPassNum= integratedPassNumMap.containsKey(examinfoId)?integratedPassNumMap.get(examinfoId):0;//综合成绩合格数
		    	Double totalScoreSum = totalScoreMap.containsKey(examinfoId)?totalScoreMap.get(examinfoId):0.0;                                               	   //成绩总分，用于计算平均分
		    	Double totalOrderNum = Double.valueOf(examInfo.get("ORDERNUM").toString());	   //当前课目预约总人数
		    	Double totalScoreNum = examNumMap.containsKey(examinfoId)?Double.valueOf(examNumMap.get(examinfoId)):0.0D;//当前课目成绩总记录数
				 
		    	int tempSectionNum    = 0;							   //给定分数段内的成绩个数
	    		int tempGrandTotalNum = 0;							   //给定分数段内的累计成绩个数
	    		int tempIntegrateSectionNum = 0;                      //给定分数段内的综合成绩个数
	    		int tempIntegrateGrandTotalNum = 0;                   //给定分数段内的累计综合成绩个数
	    		
	    		for (int j = 0; j < sectionList.size(); j++){		   //遍历给定的成绩段
	    			tempSectionNum   		 = sectionNumMap.containsKey(examinfoId+sectionList.get(j))?sectionNumMap.get(examinfoId+sectionList.get(j)):0;
	    			tempGrandTotalNum       += tempSectionNum;
	    			Double sectionRatio      = tempSectionNum   /totalScoreNum;//当前成绩段成绩比例
		    		Double grandTotalRatio   = tempGrandTotalNum/totalScoreNum;//当前成绩累计成绩比例
		    		if (sectionRatio.equals(Double.NaN)) {
						sectionRatio    = 0.0;
					}
			    	if (grandTotalRatio.equals(Double.NaN)) {
						grandTotalRatio = 0.0;
					}

		    		examInfo.put("DISTRIBUTIONNUM"+sectionList.get(j),tempSectionNum);
		    		examInfo.put("DISTRIBUTIONRATIO"+sectionList.get(j), dcmFmt.format(sectionRatio*100)+"%");
		    		examInfo.put("GRANDTOTALRATIO"+sectionList.get(j), dcmFmt.format(grandTotalRatio*100)+"%");
		    		tempSectionNum			  = 0;							   //人数归零
		    		//以下为综合成绩区段
		    		tempIntegrateSectionNum  = integratedSectionNumMap.containsKey(examinfoId+sectionList.get(j))?integratedSectionNumMap.get(examinfoId+sectionList.get(j)):0;
		    		tempIntegrateGrandTotalNum += tempIntegrateSectionNum;
	    			Double integratedSectionRatio      = tempIntegrateSectionNum/totalScoreNum;//当前成绩段成绩比例
		    		Double integratedGrandTotalRatio   = tempIntegrateGrandTotalNum/totalScoreNum;//当前成绩累计成绩比例
		    		if (integratedSectionRatio.equals(Double.NaN)) {
						integratedSectionRatio    = 0.0;
					}
			    	if (integratedGrandTotalRatio.equals(Double.NaN)) {
						integratedGrandTotalRatio = 0.0;
					}

		    		examInfo.put("INTEGRATED_DISTRIBUTIONNUM"+sectionList.get(j),tempIntegrateSectionNum);
		    		examInfo.put("INTEGRATED_DISTRIBUTIONRATIO"+sectionList.get(j), dcmFmt.format(integratedSectionRatio*100)+"%");
		    		examInfo.put("INTEGRATED_GRANDTOTALRATIO"+sectionList.get(j), dcmFmt.format(integratedGrandTotalRatio*100)+"%");
		    		tempIntegrateSectionNum			  = 0;							//人数归零		    		
	    		}
	    		
		    	int examNum       	  = examNumMap.containsKey(examInfo.get("resourceid"))?examNumMap.get(examInfo.get("resourceid")):0;	//实考人数 
		    	int absentNum     	  = absentNumMap.containsKey(examInfo.get("resourceid"))?absentNumMap.get(examInfo.get("resourceid")):0;//缺考人数
		    	int cheatNum      	  = cheatNumMap.containsKey(examInfo.get("resourceid"))?cheatNumMap.get(examInfo.get("resourceid")):0;  //作弊人数
		    	
		    	Double examNumRatio   = 0.0==totalOrderNum?0.0:examNum  /totalOrderNum;    //实考率
		    	Double absentNumRatio = 0.0==totalOrderNum?0.0:absentNum/totalOrderNum;    //缺考率
		    	Double cheatNumRatio  = 0.0==totalOrderNum?0.0:cheatNum /totalScoreNum;    //作弊率
		    	Double passNumRatio   = 0.0==totalOrderNum?0.0:passNum  /totalScoreNum;    //通过率
		    	Double avgScore       = 0.0==totalOrderNum?0.0:totalScoreSum/totalScoreNum;//平均分
		    	Double integratedPassNumRatio = 0.0==totalOrderNum?0.0:integratedPassNum/totalScoreNum;//综合通过率
		    	
		    	if (examNumRatio.equals(Double.NaN)) {
					examNumRatio    = 0.0;
				}
		    	if (absentNumRatio.equals(Double.NaN)) {
					absentNumRatio  = 0.0;
				}
		    	if (cheatNumRatio.equals(Double.NaN)) {
					cheatNumRatio   = 0.0;
				}
		    	if (passNumRatio.equals(Double.NaN)) {
					passNumRatio    = 0.0;
				}
		    	
		    	examInfo.put("EXAMNUM",examNum);
		    	examInfo.put("EXAMNUMRATIO",dcmFmt.format(examNumRatio*100)+"%");
		    	examInfo.put("ABSENTNUM",absentNum);
		    	examInfo.put("ABSENTNUMRATIO", dcmFmt.format(absentNumRatio*100)+"%");
		    	examInfo.put("CHEATNUM",cheatNum);
		    	examInfo.put("CHEATNUMRATIO", dcmFmt.format(cheatNumRatio*100)+"%");
		    	examInfo.put("PASSRATIO",dcmFmt.format(passNumRatio*100)+"%");
		    	examInfo.put("AVGSCORE",dcmFmt.format(avgScore));		    	
		    	examInfo.put("INTEGRATEDPASSRATIO",dcmFmt.format(integratedPassNumRatio*100)+"%");	
		    	
		    	resList.add(examInfo);
					
		    }    
		}else{
			success  = false;
			errorMsg = "请选择一个考试有效的批次！";

		}
		condition.put("success", success);
		condition.put("errorMsg",errorMsg);
		condition.put("sectionList", sectionList);
		condition.put("resultsList",resList);

	   return condition;
	}
	/**
	 * 发布成绩前的提示信息
	 */
	@Override
	public String getPublishRemindInfoByExamSubId(Map<String,Object> condition) throws Exception {
		
		ExamSub examSub     = (ExamSub)condition.get("examSub");
		String msg          = "";
		
		//应考人数
		String orderNumSQL  = " select count(results.RESOURCEID)ORDERNUM from EDU_TEACH_EXAMRESULTS results where results.isDeleted=0 ";
			   orderNumSQL += "    and results.EXAMSUBID=:examSubId";
		List orderNumList   = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(orderNumSQL, condition);	   
		
		//统计实考、缺考、作弊SQL
		String countSQL     = " select results.EXAMABNORMITY,count(results.EXAMABNORMITY)COUNTS from EDU_TEACH_EXAMRESULTS results ";
			   countSQL    += "  where results.isDeleted=0 and results.EXAMSUBID=:examSubId ";
			   countSQL    += "  group by results.EXAMABNORMITY";
		List countNumList 	= baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(countSQL, condition);		
		
		int examNum         = 0;//实考人数 
    	int absentNum       = 0;//缺考人数
    	int cheatNum        = 0;//作弊人数
    	
    	
    	for (int f = 0; f < countNumList.size(); f++) {//获得考试课程的实考、缺考、作弊人数
    		
			Map countMap    = (Map)countNumList.get(f);
			//正常
			if ("0".equals(countMap.get("EXAMABNORMITY"))) {
				examNum    += Integer.parseInt(countMap.get("COUNTS").toString());
			//作弊	
			}else if ("1".equals(countMap.get("EXAMABNORMITY"))) {
				cheatNum   += Integer.parseInt(countMap.get("COUNTS").toString());
				examNum    += Integer.parseInt(countMap.get("COUNTS").toString());
			//缺考	
			}else if ("2".equals(countMap.get("EXAMABNORMITY"))) {
				absentNum  += Integer.parseInt(countMap.get("COUNTS").toString());
			//无卷	
			}else if ("3".equals(countMap.get("EXAMABNORMITY"))){
				
			//其它	
			}else if ("4".equals(countMap.get("EXAMABNORMITY"))){
				examNum    += Integer.parseInt(countMap.get("COUNTS").toString());
			}
		}
		msg                 = "确定要发布<strong>"+examSub.getBatchName()+"</strong>的成绩吗？"+"<br/>";
		msg				   += "<strong>应考人数：</strong><font color='red'>"+((Map)orderNumList.get(0)).get("ORDERNUM")+"</font><br/>";
		msg		           += "<strong>实考人数：</strong><font color='red'>"+examNum+"</font><br/>";
		msg		           += "<strong>缺考人数：</strong><font color='red'>"+absentNum+"</font><br/>";
		msg		           += "<strong>作弊人数：</strong><font color='red'>"+cheatNum+"</font><br/>";
		
		return msg;
	}
	/**
	 * 调整成绩
	 * @param examInfoId 考试课程
	 * @param adjustLine 分数线
	 * @throws ServiceException
	 */
	@Override
	public void adjustExamResults(String examInfoId, String adjustLine) throws ServiceException {
		Double adjustScore = Double.parseDouble(adjustLine);
		ExamInfo examInfo = (ExamInfo) exGeneralHibernateDao.get(ExamInfo.class, examInfoId);
		examInfo.setAdjustLine(adjustScore);		

		StringBuffer hql = new StringBuffer("from "+ExamResults.class.getSimpleName()+" examResults where examResults.isDeleted=0 ");
		hql.append(" and examResults.examInfo.resourceid=? ");
		hql.append(" and examResults.writtenScore is not null ");		
		List<ExamResults> list = findByHql(hql.toString(), examInfoId);
		
		if(ExCollectionUtils.isNotEmpty(list)){
			Double adjustB = Math.pow(60.0/10.0, 2.0) - adjustScore;
			for (ExamResults examResults : list) {
				if(examResults.getWrittenScore()!=null){
					Double score = Double.parseDouble(examResults.getWrittenScore());
					if(score+adjustB>0){
						Double tempScore = BigDecimalUtil.div(Math.sqrt(score+adjustB)*10, 1.0, 0);
						if(tempScore>score && tempScore<100){
							score = tempScore;
						}
					}							
					if(ExStringUtils.isBlank(examResults.getUsuallyScore()) && score>=55 && score<60){//在55~59分之间且无平时成绩,按55分计
						score = 55.0;
					}
					examResults.setWrittenScore(new BigDecimal(score).divide(BigDecimal.ONE, 0, BigDecimal.ROUND_HALF_UP).toString());
					update(examResults);					
				}
			}
		}
	}	
	/**
	 * 提交成绩 flag:Y/unSumbit/N;提交/撤销提交/撤销审核
	 * @throws ServiceException
	 */
	@Override
	public void submitExamResults(List<ExamResults> list,String flag,StringBuffer message) throws Exception {
		if(list != null && list.size()>0){
			for (ExamResults result : list) {
			//ExamResults result = list.get(0);
				StudentInfo studentInfo = result.getStudentInfo();
				//提交
				if (Constants.BOOLEAN_YES.equals(flag)) {
					
					ExamInfo info = result.getExamInfo();
					Course course = result.getCourse();
					Long examType = (null==course||course.getExamType()==null)?0:course.getExamType();   
					Integer type  = (null==info||info.getExamCourseType()==null)?0:info.getExamCourseType();
					
					if((null!=examType&&examType == 6) || (null!=type&&type==3)){//网上考试,期末机考
						String checkStatus = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(result.getCheckStatus()),Constants.EXAMRESULT_CHECKSTATUS_INIT);
						if(Integer.valueOf(checkStatus)<1){//未提交的成绩
							if(null!=type&&type==3&&Constants.EXAMRESULT_ABNORAMITY_0.equals(ExStringUtils.trimToEmpty(result.getExamAbnormity()))&&ExStringUtils.isBlank(result.getWrittenMachineScore())){
								result.setExamAbnormity(Constants.EXAMRESULT_ABNORAMITY_2);//机考成绩为空，设置为缺考
							}
							if (null!=examType&&examType == 6&&Constants.EXAMRESULT_ABNORAMITY_0.equals(ExStringUtils.trimToEmpty(result.getExamAbnormity()))&&ExStringUtils.isBlank(result.getWrittenScore())) {
								result.setExamAbnormity(Constants.EXAMRESULT_ABNORAMITY_2);//网上考试成绩为空，设置为缺考
							}
							result.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_SUBMIT);//设置为提交状态
						}
					}else{
						if(Constants.EXAMRESULT_CHECKSTATUS_SAVE.equals(result.getCheckStatus())){
							if(ExStringUtils.isBlank(result.getExamAbnormity())){
								//throw new ServiceException("学生"+result.getStudentInfo().getStudentName()+"的成绩异常状态为空，请先无法提交");
								continue;
							}
							if(Constants.EXAMRESULT_ABNORAMITY_0.equals(result.getExamAbnormity())&&ExStringUtils.isBlank(result.getWrittenScore())){
								//throw new ServiceException("学生"+result.getStudentInfo().getStudentName()+"的卷面成绩为空,请先录入再提交 ");
								continue;
							}
							result.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_SUBMIT);//设置为提交状态
						} else if (Constants.EXAMRESULT_CHECKSTATUS_INIT.equals(result.getCheckStatus())) {
							//throw new ServiceException("学生"+result.getStudentInfo().getStudentName()+"的成绩还未保存");
							continue;
						}
					}					
				}else if("unSumbit".equals(flag)){
					// 撤销提交
					//新：批量撤销提交状态的成绩 
					Map<String, Object> values = new HashMap<String, Object>();
					values.put("classesid",studentInfo.getClasses().getResourceid());
					values.put("courseid", result.getCourse().getResourceid());
					values.put("examsubid", result.getExamsubId());
					String stuTable = " select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0 and si.classesid=:classesid"; 
					String hql = "update edu_teach_examresults er set er.checkstatus='0' where er.isdeleted=0 and er.checkstatus='1' and er.courseid=:courseid and er.examsubid =:examsubid and er.studentid in("+stuTable+")";
					int succesNum = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
					if (succesNum == 0) {
						message.append("无需撤销提交成绩！");
					} else {
						message.append("撤销成绩成功！");
					}
					break;
				}else{
					// 撤销审核
					Map<String, Object> values = new HashMap<String, Object>();
					values.put("classesid",studentInfo.getClasses().getResourceid());
					values.put("courseid", result.getCourse().getResourceid());
					String hql = "";
					String stuTable = " select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0 and si.classesid=:classesid"; 
					String esubTable = "select es.resourceid from edu_teach_examsub es where es.isdeleted=0 ";
					String info = "#"+SpringSecurityHelper.getCurrentUserName()+"在"+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME)+"撤销审核";
					
					//新：删除所有补考成绩
					ExamSub examSub = examSubService.get(result.getExamsubId());
					if(examSub!=null){

						if("N".equals(examSub.getExamType())){
							//撤销正考
							esubTable += " and es.examType!='N'";
							//1、删除补考名单
							hql = "update edu_teach_makeuplist ml set ml.isdeleted=1,ml.memo=ml.memo||'"+info+"' where ml.isdeleted=0 and ml.courseid=:courseid and ml.nextexamsubid in("+esubTable+") and ml.studentid in("+stuTable+")";
							baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
							//2、删除补考成绩
							hql = "update edu_teach_examresults er set er.isdeleted=1,er.memo=er.memo||'"+info+"' where er.isdeleted=0 and er.courseid=:courseid and er.examsubid in("+esubTable+") and er.studentid in("+stuTable+")";
							baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
							//3、撤销正考成绩
							hql = "update edu_teach_examresults er set er.checkstatus='0',er.memo=er.memo||'"+info+"' where er.checkstatus='4' and er.isdeleted=0 and er.courseid=:courseid and er.examsubid in('"+examSub.getResourceid()+"') and er.studentid in("+stuTable+")";
							int succesNum = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
							if(succesNum==0){
								message.append("无需撤销审核成绩！");
							}
							break;
						}else {
							//撤销补考(2018-03-16)
							//1、删除当前及后期补考名单
							String[] examTypes = {"N","Y","T","Q","R","G"};
							examTypes = ExStringUtils.removeStartString(examTypes, examSub.getExamType());
							esubTable += " and es.examType in("+ExStringUtils.addSymbol(examTypes,"'", "'")+")";
							hql = "update edu_teach_makeuplist ml set ml.isdeleted=1,ml.memo=ml.memo||'"+info+"' where ml.isdeleted=0 and ml.courseid=:courseid and ml.nextexamsubid in("+esubTable+") and ml.studentid in("+stuTable+")";
							baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
							//2、删除后期补考成绩
							hql = "update edu_teach_examresults er set er.isdeleted=1,er.memo=er.memo||'"+info+"' where er.isdeleted=0 and er.courseid=:courseid and er.examsubid in("+esubTable+") and er.studentid in("+stuTable+")";
							baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
							//3、撤销当前补考成绩
							hql = "update edu_teach_examresults er set er.checkstatus='0',er.memo=er.memo||'"+info+"' where er.checkstatus='4' and er.isdeleted=0 and er.courseid=:courseid and er.examsubid in('"+examSub.getResourceid()+"') and er.studentid in("+stuTable+")";
							int succesNum = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, values);
							if(succesNum==0){
								message.append("无需撤销审核补考成绩！");
							}
							break;
						}
					}
				}
			}
		}
	}
	
	/**
	 * 提交平时成绩
	 * @throws ServiceException
	 */
	@Override
	public void submitUsExamResults(List<ExamResults> list,String flag) throws ServiceException {
		if(list != null && list.size()>0){
			for (ExamResults result : list) {
				//提交
				if (Constants.BOOLEAN_YES.equals(flag)) {
					if(Constants.EXAMRESULT_CHECKSTATUS_SAVE.equals(result.getUsCheckStatus())){
						if(ExStringUtils.isBlank(result.getUsuallyScore())){
							continue;
							//throw new ServiceException("学生"+result.getStudentInfo().getStudentName()+"的平时成绩为空，无法提交");
						}
						result.setUsCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_SUBMIT);//设置为提交状态
					} 
										
				//取消提交
				}else {
					if(Constants.EXAMRESULT_CHECKSTATUS_SUBMIT.equals(result.getCheckStatus())){
						result.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_SAVE);//设置为保存状态
					}
				}
			}
		}
	}
	/**
	 * 计算综合成绩
	 * @param rs
	 * @param info
	 * @return
	 */
	public ExamResults calculateIntegratedScore(ExamResults rs, ExamInfo info, Map<String, String> resultCalculateRuleMap){
		try {
			//		Double wsp 	   		 	 = null==info.getStudyScorePer()?0D:info.getStudyScorePer(); //卷面成绩分比例
			Double wsp = null;
			if(info.getExamCourseType()==0){
				wsp 	   		 	 = null==info.getStudyScorePer()?0D:info.getStudyScorePer(); //卷面成绩分比例
			} else {//面授卷面成绩分比例
				if(Constants.COURSE_SCORE_TYPE_TWO.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_THREE.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_FOUR.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_FIVE.equals(rs.getCourseScoreType())){// 非百分制成绩处理
					rs.setTempintegratedScore_d(studentExamResultsService.convertScoreStr(rs.getCourseScoreType(),rs.getIntegratedScore()));
					rs.setTempusuallyScore_d(studentExamResultsService.convertScoreStr(rs.getCourseScoreType(),rs.getUsuallyScore()));
					rs.setTempwrittenScore_d(studentExamResultsService.convertScoreStr(rs.getCourseScoreType(),rs.getWrittenScore()));
					return rs;
				}

				if(ExStringUtils.isBlank(info.getFacestudyScorePer())){// 考试批次课程信息中没有这个比例，则取考试批次的
					ExamSub es = info.getExamSub();
					wsp = es.getFacestudyScorePer();
				} else {
					wsp = info.getFacestudyScorePer();
				}
			}
			Double usp      		 = 100-wsp;                                                  //平时分比例

			String rule = "";
			if(resultCalculateRuleMap != null && resultCalculateRuleMap.size() > 0) {
				rule = resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_USUALLYSCORE);
				if("2".equals(rule)){
					usp = 100D;
				}
				rule = resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_WRITTENSCORE);
				if("2".equals(rule)){
					wsp = 100D;
				}
			}

			BigDecimal divisor 		 = new BigDecimal("1");
			BigDecimal hundredBig    = new BigDecimal(100);
			if(ExStringUtils.isBlank(rs.getUsuallyScore())){
				rs.setTempintegratedScore_d(rs.getIntegratedScore());
				return rs;
			}
			String usuallyScore      = ExStringUtils.isBlank(rs.getUsuallyScore())?"0":rs.getUsuallyScore();
			String writtenScore      = ExStringUtils.isBlank(rs.getWrittenScore())?"0":rs.getWrittenScore();
			String examAbnormity     = rs.getExamAbnormity();

			BigDecimal wsBig         = new BigDecimal(writtenScore);
			BigDecimal usBig    	 = new BigDecimal(usuallyScore);

			BigDecimal wsPerBig      = new BigDecimal(wsp.toString());
			BigDecimal usPerBig      = new BigDecimal(usp.toString());

			BigDecimal wsPerRateBig  = wsPerBig.divide(hundredBig,2);
			BigDecimal usPerRateBig  = usPerBig.divide(hundredBig,2);


			//成绩中成绩异常不为空时不设置卷面成绩、平时成绩、综合成绩
			if (ExStringUtils.isNotEmpty(examAbnormity) && !Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)){
				return rs;
				//成绩中成绩异常为空时设置卷面成绩、平时成绩、综合成绩
			}else {

				//考试科目中平时成绩比例、卷面成绩比例不为空
				if (null!=wsp && null!=usp && wsp >= 0&&usp>0){

					Double	usuallyScoreD = usBig.multiply(usPerRateBig).doubleValue();//根据批次比例算出的平时成绩
					Double	writtenScoreD = wsBig.multiply(wsPerRateBig).doubleValue();//根据批次比例算出的卷面成绩

					rs.setTempusuallyScore_d((usBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					rs.setTempwrittenScore_d((wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());

					//当综合成绩小于卷面成绩时，综合成绩等于卷面成绩   20140110 开放注释  要求取高分
//				if ( (writtenScoreD+usuallyScoreD) < Double.parseDouble(writtenScore)){
//					rs.setTempintegratedScore_d((wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
//				//当综合成绩大于卷面成绩时,综合成绩计算方式为：卷面成绩*卷面成绩比例+平时成绩*平时成绩比例
//				}else{
//					BigDecimal integratedScore = new BigDecimal(String.valueOf(writtenScoreD+usuallyScoreD));
//					rs.setTempintegratedScore_d((integratedScore.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
//				}
					//2014-4-1 成绩比例不为空时，按比例取，不取最高分
					if (ExStringUtils.isBlank(rs.getTempintegratedScore_d())) {
						BigDecimal integratedScore = new BigDecimal(String.valueOf(writtenScoreD+usuallyScoreD));
						rs.setTempintegratedScore_d((integratedScore.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					}

					//考试科目中平时成绩比例、卷面成绩比例为空综合成绩等于卷面成绩
				}else {
					rs.setTempusuallyScore_d((usBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					rs.setTempwrittenScore_d((wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					if (ExStringUtils.isBlank(rs.getTempintegratedScore_d())) {
						rs.setTempintegratedScore_d((wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					}
				}
			}
		} catch (Exception e) {
			logger.error("成绩审核-计算综合成绩失败："+e.getMessage());
		}
		return rs;
	}
	/**
	 * 传入成绩列表计算综合成绩
	 * @param list
	 * @param info 考试信息必须正确，可修复考试信息关联不正确的成绩
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamResults> calculateExamResultsListIntegratedScore(List<ExamResults> list, ExamInfo info) throws ServiceException {
		
		if (null!=info&&null!=list&&list.size()>0) {
			// 获取成绩计算规则字典表
			List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
			Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
			for(Dictionary d : resultCalculateRuleList) {
				resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
			}
			Map<String, Object> condition = new HashMap<String, Object>();
			
			condition.put("stuChange", "12");//复学条件
			List<ExamResults> _list = new ArrayList<ExamResults>();
			for (ExamResults rs:list) {
				if(rs.getFillinDate()!=null){
					condition.put("auditDate", rs.getFillinDate());//复学前录入的成绩
				}	
				if(rs.getExamEndTime()!=null){
					condition.put("auditDate", rs.getExamEndTime());//复学前录入的成绩
				}	
				condition.put("stuNum", rs.getStudentInfo().getStudyNo());//学生学号
				List<StuChangeInfo> stuChangeInfos = stuChangeInfoService.findByCondition(condition);
				//如果考试信息与不一致，则修改为成绩的考试信息
				if(stuChangeInfos.size()<=0){
					if(rs.getExamInfo()!=info){
						rs.setExamInfo(info);
						_list.add(rs);
					}
				}
				calculateIntegratedScore(rs,info,resultCalculateRuleMap);
			}
			batchSaveOrUpdate(_list);
		}
		return list;
	}
	/**
	 * 成绩审核-个别审核 (检查提交的成绩是否符合规范)
	 * @param info                     考试课程
	 * @param teachType                教学方式
	 * @param studentName              学生姓名
	 * @param examAbnormity            异常成绩状态
	 * @param isMixTrue				       是否混合机考
	 * @param integratedScoreD         提交的综合成绩
	 * @param usuallyScoreD     	        提交的平时成绩
	 * @param writtenScoreD			        提交的卷面成绩
	 * @param writtenHandworkScoreD    提交的笔考成绩(针对混合机考)
	 * @param writtenMachineScoreD	        提交的机考成绩(针对混合机考)
	 * @param mixScorePer			        混合机考课程设定的笔考成绩比例
	 * @throws ServiceException
	 */
	public void auditExamResultsSingleCheck(ExamInfo info, String teachType, String studentName, String examAbnormity, String isMixTrue,
											double integratedScoreD, double usuallyScoreD, double writtenScoreD, double writtenHandworkScoreD,
											double writtenMachineScoreD, double mixScorePer, Map<String, String> resultCalculateRuleMap)throws ServiceException {
		Double writtenScorePer = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue());
		Double usuallyScorePer = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue());
		if("netsidestudy".equals(teachType)){// 网络面授课程比例
			writtenScorePer = null==info.getStudyScorePer()?-1D:info.getStudyScorePer(); 
		} else {
			if(null==info.getFacestudyScorePer()){
				writtenScorePer = info.getExamSub().getFacestudyScorePer();
			} else {
				writtenScorePer = info.getFacestudyScorePer(); 
			}
		}
		// 平时成绩比例
		if(null == info.getFacestudyScorePer2()){
			usuallyScorePer = info.getExamSub().getFacestudyScorePer2();
		} else {
			usuallyScorePer = info.getFacestudyScorePer2();
		}
		Double usuallyTopScore = usuallyScorePer;
		Double writtenTopScore = writtenScorePer;
		// 成绩计算规则
		String rule ="";
		if(resultCalculateRuleMap != null && resultCalculateRuleMap.size() > 0){
			rule= resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_USUALLYSCORE);
			if("2".equals(rule)){
				usuallyScorePer = 100D;
			}
			rule= resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_WRITTENSCORE);
			if("2".equals(rule)){
				writtenScorePer = 100D;
			}
		}
		
		Double usuallyScoreRate= -1D; 
		Double writtenScoreRate= -1D; 
		
		String courseName      = info.getCourse().getCourseName();

		if (null!=usuallyScorePer&&usuallyScorePer>=0) {
			usuallyScoreRate   = usuallyScorePer/100;
		}
		if (null!=writtenScorePer&&writtenScorePer>=0) {
			writtenScoreRate   = writtenScorePer/100;
		}
		
		Double temp_usuallyScore = usuallyScoreD*usuallyScoreRate;
		Double temp_writtenScore = writtenScoreD*writtenScoreRate;
		Double temp_integratedScore = temp_usuallyScore+temp_writtenScore;
		
		//提交正常成绩时检查成绩是否符合规范
		if(ExStringUtils.isNotBlank(examAbnormity)&&Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)){
	
			Double _writtenScore = writtenTopScore/writtenScoreRate;
			//提交的卷面分不能大于100(针对非混全机考的情况)
			if (Constants.BOOLEAN_NO.equals(isMixTrue)&&(writtenScoreD<0||writtenScoreD>_writtenScore)) {
				throw new ServiceException(studentName+"的卷面成绩不能小于0或大于"+_writtenScore.intValue());
			}
			//提交的卷面*卷面成绩比例+平时*平时成绩比例不能大于100(针对非混全机考的情况)
			if(Constants.BOOLEAN_NO.equals(isMixTrue)&&(writtenScoreD*writtenScoreRate+temp_usuallyScore)>100){
				throw new ServiceException("《"+courseName+"》的平时分比例为："+usuallyScorePer.intValue()+"%"+",卷面成绩比例为:"
						+writtenScorePer.intValue()+"%,提交的"+studentName+"的卷面+平时分按比例算出总分大于:100");
			}
			//提交的笔考+机考成绩不能大于100(针对混全机考的情况)
			if (Constants.BOOLEAN_YES.equals(isMixTrue)&&((writtenMachineScoreD+writtenHandworkScoreD)>100)) {
				throw new ServiceException(studentName+"的笔考+机考成绩不能大于:100");
			}
			//混合机考的笔考成绩不能大于设定的笔考成绩分数(针对混全机考的情况)
			if (Constants.BOOLEAN_YES.equals(isMixTrue)&&writtenHandworkScoreD>mixScorePer) {
				throw new ServiceException(studentName+"的笔考成绩不能大于:"+mixScorePer);
			}
			//提交的卷面*卷面成绩比例+平时*平时成绩比例不能大于100(针对混全机考的情况)
			if(Constants.BOOLEAN_YES.equals(isMixTrue)&&((writtenMachineScoreD+writtenHandworkScoreD)*writtenScoreRate+temp_usuallyScore)>100){
				throw new ServiceException("《"+courseName+"》的平时分比例为："+usuallyScorePer.intValue()+"%"+",卷面成绩比例为:"+writtenScorePer.intValue()
							+"%,提交的"+studentName+"的卷面+平时分按比例算出总分大于:100");
			}
			//提交的平时分不能大于设定的100
			Double _usuallyScore = usuallyTopScore/usuallyScoreRate;
			if (usuallyScoreD<0||usuallyScoreD>_usuallyScore) {
				throw new ServiceException(studentName+"的平时成绩不能小于0或大于"+_usuallyScore.intValue());
			}
			//提交正常成绩的情况下综合成绩应为0-100
			if(integratedScoreD<0||integratedScoreD>100){
				throw new ServiceException(studentName+"的综合成绩不能小于0或大于100");
			}
			// 根据卷面成绩+平时成绩计算出来的综合成绩
			if(temp_integratedScore<0||temp_integratedScore>100){
				throw new ServiceException(studentName+"计算出来的综合成绩不能小于0或大于100");
			}
			
		}
		
	}
	/**
	 * 成绩审核-个别审核 
	 * @param idsArray
	 * @param request
	 * @throws ServiceException
	 */
	@Override
	public void auditExamResultsSingle(String[] idsArray, HttpServletRequest request, ExamInfo info, String teachType)throws Exception {
		
		if (null!=idsArray&&idsArray.length>0&&ExStringUtils.isNotEmpty(teachType)) {

			User curUser 		   				= SpringSecurityHelper.getCurrentUser();	  //当前用户
			Date curTime 		   				= new Date();                                 //当前时间
			List<ExamResults> list 				= this.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("resourceid", idsArray));
			list                   				= calculateExamResultsListIntegratedScore(list,info);//计算综合成绩
			String hql 		  	   				= " from "+ExamResultsAudit.class.getSimpleName()+" where isDeleted = ? and examResults.resourceid=? and auditType = ?  ";
			String isMixTrue       				= null==info.getIsmixture()?"N":info.getIsmixture();
			Double mixScorePer     				= null==info.getMixtrueScorePer()?0d:info.getMixtrueScorePer();
			if(ExStringUtils.isBlank(info.getCourseScoreType())) {
				throw new ServiceException("考试课程《"+ExStringUtils.trimToEmpty(info.getExamCourseCode())+info.getCourse().getCourseName()+"》成绩类型未设置，不允许进行审核操作！");
			}
			
			Map<String,StudentLearnPlan> map    = new HashMap<String, StudentLearnPlan>();
			List<StudentCheck> checkList  		= new ArrayList<StudentCheck>();
			
			String rsIds                        = "'"+ExStringUtils.join(idsArray,"','")+"'";
			List<StudentLearnPlan> lp     		= studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=?  and plan.examResults.resourceid in("+rsIds+")",0);
			for (StudentLearnPlan p : lp) {
				map.put(p.getExamResults().getResourceid(), p);
			}
			// 获取成绩计算规则字典表
			List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
			Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
			for(Dictionary d : resultCalculateRuleList) {
				resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
			}
			List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
			for (ExamResults rs:list) {
				
				boolean hasChange               = false;//是否更正
				
				//---------------------------------------------审核时提交的数据---------------------------------------------
				String checkNotes               = ExStringUtils.trimToEmpty(request.getParameter("checkNotes_"+rs.getResourceid()));     //审核时提交的 审核意见
				String writtenScore             = ExStringUtils.trimToEmpty(request.getParameter("writtenScore_"+rs.getResourceid()));   //审核时提交的 卷面成绩
				String usuallyScore 	        = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore_"+rs.getResourceid()));   //审核时提交的 平时成绩
				String integratedScore          = ExStringUtils.trimToEmpty(request.getParameter("integratedScore_"+rs.getResourceid()));//审核时提交的 综合成绩
				String examAbnormity            = ExStringUtils.trimToEmpty(request.getParameter("examAbnormity_"+rs.getResourceid()));  //审核时提交的 成绩异常
				String writtenMachineScore      = ExStringUtils.trimToEmpty(request.getParameter("writtenMachineScore_"+rs.getResourceid()));   //审核时提交的 机考成绩
				String writtenHandworkScore     = ExStringUtils.trimToEmpty(request.getParameter("writtenHandworkScore_"+rs.getResourceid()));  //审核时提交的 笔考成绩
				
				writtenScore  		            = ExStringUtils.isBlank(writtenScore)?"-1":writtenScore;
				usuallyScore 		   	        = ExStringUtils.isBlank(usuallyScore)?"-1":usuallyScore;
				integratedScore 	   	        = ExStringUtils.isBlank(integratedScore)?"-1":integratedScore;
				writtenMachineScore             = ExStringUtils.isBlank(writtenMachineScore)?"-1":writtenMachineScore;
				writtenHandworkScore	        = ExStringUtils.isBlank(writtenHandworkScore)?"-1":writtenHandworkScore;
				
				double writtenScoreD            = Double.valueOf(writtenScore);
				double integratedScoreD         = Double.valueOf(integratedScore);
				double usuallyScoreD            = Double.valueOf(usuallyScore);
				double writtenMachineScoreD     = Double.valueOf(writtenMachineScore);
				double writtenHandworkScoreD    = Double.valueOf(writtenHandworkScore);
				double t_writtenScore 			= (writtenMachineScoreD + writtenHandworkScoreD);//重新计算卷面成绩针对混合机考
				//---------------------------------------------审核时提交的数据---------------------------------------------
				
				//检查提交的成绩是否符合规范
				auditExamResultsSingleCheck(info,teachType,rs.getStudentInfo().getStudentName(),examAbnormity,isMixTrue,integratedScoreD,usuallyScoreD,
						writtenScoreD,writtenHandworkScoreD,writtenMachineScoreD,mixScorePer,resultCalculateRuleMap);	
				
				//-----------------------------------------------成绩原始数据-----------------------------------------------
				String writtenScore_old         = rs.getWrittenScore();
				String usuallyScore_old         = rs.getUsuallyScore();
				String integratedScore_old 	    = rs.getTempintegratedScore_d();
				String examAbnormity_old   	    = rs.getExamAbnormity();
				String writtenMachineScore_old  = rs.getWrittenMachineScore();
				String writtenHandworkScore_old = rs.getWrittenHandworkScore();
				
				writtenScore_old 		        = ExStringUtils.isBlank(writtenScore_old)?"-1":writtenScore_old;
				usuallyScore_old 		        = ExStringUtils.isBlank(usuallyScore_old)?"-1":usuallyScore_old;
				integratedScore_old 	        = ExStringUtils.isBlank(integratedScore_old)?"-1":integratedScore_old;
				writtenMachineScore_old         = ExStringUtils.isBlank(writtenMachineScore_old)?"-1":writtenMachineScore_old;
				writtenHandworkScore_old	    = ExStringUtils.isBlank(writtenHandworkScore_old)?"-1":writtenHandworkScore_old;
				
				double writtenScoreD_old        = Double.valueOf(writtenScore_old);
				double usuallyScoreD_old        = Double.valueOf(usuallyScore_old);
				double integratedScoreD_old     = Double.valueOf(integratedScore_old);
				double writtenMachineScoreD_old = Double.valueOf(writtenMachineScore_old);
				double writtenHandworkScoreD_old= Double.valueOf(writtenHandworkScore_old);
				//-----------------------------------------------成绩原始数据-----------------------------------------------
				
				
				//成绩变更记录
				ExamResultsAudit audit 	   		 = examResultsAuditService.findUnique(hql,0,rs.getResourceid(),0);
				if (null==audit) {
					audit           = new ExamResultsAudit();
				}
				
				audit.setExamResults(rs);
				audit.setMemo(checkNotes);
				audit.setChangedDate(curTime);
				audit.setChangedMan(curUser.getCnName());
				audit.setChangedManId(curUser.getResourceid());
				
				//1. 审核时提交的是 正常成绩,变更情况有六种:A 从异常更改为正常  B 更改卷面成绩 C 更改平时成绩 D 更改综合成绩  E 更改机考成绩  F 更改笔考成绩
				if (ExStringUtils.isNotBlank(examAbnormity)&&Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)) {
					//A 更改异常字段 (从异常更改为正常: 改之前记录异常    改之后记录正常)  
					if (!examAbnormity.trim().equals(examAbnormity_old.trim())) {
						hasChange = true;
						
						//设置更改前的各项值
						audit.setBeforeExamAbnormity(examAbnormity_old);
						if (!"-1".equals(writtenHandworkScore_old)) {
							audit.setBeforeWrittenHandworkScore(writtenHandworkScore_old);
						}
						if (!"-1".equals(writtenMachineScore_old)) {
							audit.setBeforeWrittenMachineScore(writtenMachineScore_old);
						}
						if (!"-1".equals(usuallyScore_old)) {
							audit.setBeforeUsuallyScore(usuallyScore_old);
						}
						audit.setBeforeIntegratedScore("");
						audit.setBeforeWrittenScore("");
						
						//设置更改后的各项值
						audit.setChangedExamAbnormity(examAbnormity);
						if (!"-1".equals(writtenScore)) {
							audit.setChangedWrittenScore(writtenScore);
						}
						if (!"-1".equals(usuallyScore)) {
							audit.setChangedUsuallyScore(usuallyScore);
						}
						if (!"-1".equals(integratedScore)) {
							audit.setChangedIntegratedScore(integratedScore);
						}
						if (!"-1".equals(writtenHandworkScore)) {
							audit.setChangedWrittenHandworkScore(writtenHandworkScore);
						}
						if (!"-1".equals(writtenMachineScore)) {
							audit.setChangedWrittenMachineScore(writtenMachineScore);
						}
						
						audit.setAuditStatus(0);
						examResultsAuditService.saveOrUpdate(audit);
					}
					//普通考试及普通机考 B 更改卷面成绩   C 更改平时成绩 D 更改综合成绩
					if (Constants.BOOLEAN_NO.equals(isMixTrue)&&
					   (writtenScoreD!=writtenScoreD_old ||     
						usuallyScoreD!=usuallyScoreD_old || 
						integratedScoreD!=integratedScoreD_old)) {
						hasChange = true;
						
						//设置更改前的各项值
						audit.setBeforeExamAbnormity(examAbnormity_old);
						if (!"-1".equals(writtenScore_old)) {
							audit.setBeforeWrittenScore(writtenScore_old);
						}
						if (!"-1".equals(usuallyScore_old)) {
							audit.setBeforeUsuallyScore(usuallyScore_old);
						}
						if (!"-1".equals(integratedScore_old)) {
							audit.setBeforeIntegratedScore(integratedScore_old);
						}
						
						//设置更改后的各项值
						audit.setChangedExamAbnormity(examAbnormity);
						if (!"-1".equals(writtenScore)) {
							audit.setChangedWrittenScore(String.valueOf(writtenScoreD));
						}
						if (!"-1".equals(usuallyScore)) {
							audit.setChangedUsuallyScore(String.valueOf(usuallyScoreD));
						}
						if (!"-1".equals(integratedScore)) {
							audit.setChangedIntegratedScore(String.valueOf(integratedScoreD));
						}
						
						audit.setAuditStatus(0);
						examResultsAuditService.saveOrUpdate(audit);
						
					}
					//混合机考  C 更改平时成绩 D 更改综合成绩 E 更改机考成绩  F 更改笔考成绩
					if (Constants.BOOLEAN_YES.equals(isMixTrue)&&
					   (writtenMachineScoreD!=writtenMachineScoreD_old||
					    writtenHandworkScoreD!=writtenHandworkScoreD_old||
					    usuallyScoreD!=usuallyScoreD_old || 
						integratedScoreD!=integratedScoreD_old)) {
						hasChange = true;
						
						//设置更改前的各项值
						audit.setBeforeExamAbnormity(examAbnormity_old);
						if (!"-1".equals(writtenScore_old)) {
							audit.setBeforeWrittenScore(writtenScore_old);
						}
						if (!"-1".equals(usuallyScore_old)) {
							audit.setBeforeUsuallyScore(usuallyScore_old);
						}
						if (!"-1".equals(integratedScore_old)) {
							audit.setBeforeIntegratedScore(integratedScore_old);
						}
						if (!"-1".equals(writtenMachineScore_old)) {
							audit.setBeforeWrittenMachineScore(writtenMachineScore_old);
						}
						if (!"-1".equals(writtenHandworkScore_old)) {
							audit.setBeforeWrittenHandworkScore(writtenHandworkScore_old);
						}
						
						//设置更改后的各项值
						audit.setChangedExamAbnormity(examAbnormity);
						//重新计算综合成绩      暂时先维持上一版做法，审核页面中允许更改任何一列成绩，但不重新计算综合成绩,根据页面中提交的为准
						//rs.setWrittenScore(String.valueOf(t_writtenScore));
						//calculateIntegratedScore(rs, info, teachType); 

						if (t_writtenScore>=0) {
							audit.setChangedWrittenScore(String.valueOf(t_writtenScore));
						}
						if (!"-1".equals(usuallyScore)) {
							audit.setChangedUsuallyScore(String.valueOf(usuallyScoreD));
						}
						if (!"-1".equals(writtenMachineScore)) {
							audit.setChangedWrittenMachineScore(String.valueOf(writtenMachineScoreD));
						}
						if (!"-1".equals(writtenHandworkScore)) {
							audit.setChangedWrittenHandworkScore(String.valueOf(writtenHandworkScoreD));
						}
						if (!"-1".equals(integratedScore)) {
							audit.setChangedIntegratedScore(String.valueOf(integratedScoreD));
						}
						
						//设置复审记录中的更改后的综合成绩   暂时先维持上一版做法，审核页面中允许更改任何一列成绩，但不重新计算综合成绩,根据页面中提交的为准
					    //audit.setChangedIntegratedScore(rs.getTempintegratedScore_d());
						//将成绩记录中卷面成绩还原，待复审通过后才执行更改
						//rs.setWrittenScore(writtenScore_old);
						
						audit.setAuditStatus(0);
						examResultsAuditService.saveOrUpdate(audit);
					}
				//2.审核时提交的是 异常成绩,变更情况有一种: 从正常更改为异常或者更换异常值
				}else {
					
					// 更改异常字段 (从正常更改为异常: 改之前记录正常 改之后记录异常)
					if (!examAbnormity.trim().equals(examAbnormity_old.trim())) {
						hasChange = true;
						
						//设置更改前的各项值
						audit.setBeforeExamAbnormity(examAbnormity_old);
						if (!"-1".equals(usuallyScore_old)) {
							audit.setBeforeUsuallyScore(usuallyScore_old);
						}
						if (!"-1".equals(writtenScore_old)) {
							audit.setBeforeWrittenScore(writtenScore_old);
						}
						if (!"-1".equals(integratedScore_old)) {
							audit.setBeforeIntegratedScore(integratedScore_old);
						}
						if (!"-1".equals(writtenHandworkScore_old)) {
							audit.setBeforeWrittenHandworkScore(writtenHandworkScore_old);
						}
						if (!"-1".equals(writtenMachineScore_old)) {
							audit.setBeforeWrittenMachineScore(writtenMachineScore_old);
						}
						
						//设置更改后的各项值
						audit.setChangedExamAbnormity(examAbnormity);
						if (!"-1".equals(usuallyScore)) {
							audit.setChangedUsuallyScore(usuallyScore);
						}
						audit.setChangedWrittenHandworkScore("");
						audit.setChangedWrittenMachineScore("");
						audit.setChangedWrittenScore("");
						audit.setChangedIntegratedScore("");
						
						audit.setAuditStatus(0);
						examResultsAuditService.saveOrUpdate(audit);
					}
				}
				//网络面授类课程写入平时分(课程成绩表中的平时分由审核页面提交的为准)
				if ("netsidestudy".equals(teachType)&&ExStringUtils.isNotBlank(usuallyScore)&&!"-1".equals(usuallyScore)) {
					
					UsualResults us		  			= new UsualResults();
					StudentLearnPlan plan 			= null;
					if (map.containsKey(rs.getResourceid())) {
						plan              			= map.get(rs.getResourceid());
						us         		  			= null!=plan.getUsualResults()?plan.getUsualResults():us;
					}
					us.setCourse(rs.getCourse());
					us.setYearInfo(rs.getExamInfo().getExamSub().getYearInfo());
					us.setTerm(rs.getExamInfo().getExamSub().getTerm());
					us.setStudentInfo(rs.getStudentInfo());
					us.setCourseExamResults(usuallyScore);
					us.setUsualResults(usuallyScore);
					us.setFillinMan(curUser.getUsername());
					us.setFillinManId(curUser.getResourceid());
					us.setFillinDate(curTime);
					us.setStatus("1");
					
					if (null!=plan){
						 plan.setUsualResults(us);
					} 
					usualResultsService.saveOrUpdate(us);
					//urs.add(us);
				}
				
				rs.setAuditDate(curTime);
				rs.setAuditMan(curUser.getUsername());
				rs.setAuditManId(curUser.getResourceid());
				rs.setCheckNotes(checkNotes);
				
				//进入待审核状态,走复审流程，复审通过直接设置为审核通过
				if (hasChange) {
					rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_WAIT);
				//审核通过将综合分写入IntegratedScore字段	
				}else {
					StudentLearnPlan plan = null;
					if (map.containsKey(rs.getResourceid())) {
						plan = map.get(rs.getResourceid());
						plan.setStatus(3);
					}
					StudentCheck check = new StudentCheck();
					check.setStudentId(rs.getStudentInfo().getResourceid());
					checkList.add(check);
					
					rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
					if (!"-1".equals(integratedScore_old)) {
						rs.setIntegratedScore(integratedScore_old);	
						if(plan!=null){//加上最终成绩和通过
							plan.setFinalScore(integratedScore_old);
							plan.setIsPass("Y");
						}
					}
					
					//判断是否需要需要补考
					//20140617 温朝上提出，综合成绩小于60分都进去补考名单
					//20140624 陈浩珍提出，没有成绩与综合成绩小于60的都进入补考名单
					/**
					 *  没有成绩与综合成绩小于60、缺考、缓考和作弊都进入补考，
					 *  但是作弊直接进入二补；具体做法：
					 *  作弊的学生正考一发布就直接插入两条记录（正考作弊、一补0分），
					 *  进入二补补考名单
					 */
					if(integratedScoreD == -1 ||
							(integratedScoreD >= 0 && integratedScoreD < 60.0)
							|| "5".equals(rs.getExamAbnormity()) || "2".equals(rs.getExamAbnormity())
							|| "1".equals(rs.getExamAbnormity())){
						
						if(plan!=null){//如果进入补考,则最终不通过
							plan.setIsPass("N");
						}
						handleMadeUp(curTime, curUser, rs);
					}
					//如果补考成绩及格，标记上次补考名单中的是否通过为  Y 
					if(ExStringUtils.isNotBlank(rs.getIntegratedScore())&&Double.parseDouble(rs.getIntegratedScore())>=60){//由于当前只能录入百分制度的分值成绩，所以只判断成绩>=60分为及格
						String hql1 = "from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.nextExamSubId=? and a.studentInfo.resourceid=? and a.course.resourceid=? ";
						List<StudentMakeupList> stumakeupList = studentMakeupListService.findByHql(hql1, rs.getExamsubId(),rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
						if(stumakeupList!=null && stumakeupList.size()>0){
							StudentMakeupList stumakeup= new StudentMakeupList();
							stumakeup=	stumakeupList.get(0);
							stumakeup.setIsPass("Y");
							makeupList.add(stumakeup);
							//studentMakeupListService.saveOrUpdate(stumakeup);
							//补考通过则,最终通过
							if(plan!=null){
								plan.setIsPass("Y");
							}
						}
						
					}
				}
			}
			
			this.batchSaveOrUpdate(list);
			if (!lp.isEmpty()) {
				studentLearnPlanService.batchSaveOrUpdate(lp);
			}
			if (!makeupList.isEmpty()) {
				studentMakeupListService.batchSaveOrUpdate(makeupList);
			}
			if (checkList.size()>0) {
				exGeneralHibernateDao.batchSaveOrUpdate(checkList);
			}
			
		}else {
			throw new ServiceException("请选择要审核的成绩记录！");
		}
	}
	/**
	 * 查询考试课程的考试总人数
	 * @param map
	 * @return
	 * @throws Exception 
	 */
	private Long getExamCountByExamInfo(Map<String, Object> map)throws Exception{
		
		StringBuffer sql = new StringBuffer() ;
		sql.append(" select count(r1.resourceid) counts from edu_teach_examresults r1 where r1.isdeleted = 0 ");
		sql.append("    and r1.examinfoid = :examInfoId  group by r1.examinfoid ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(sql.toString(), map);
	}
	/**
	 * 查找考试课程的成绩状态
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public List<Map<String,Object>> queryExamCheckStatusForExamInfo(Map<String, Object> map)throws Exception{

		StringBuffer sql 	   = new StringBuffer() ;
		sql.append(" select r2.checkstatus ,count(r2.resourceid) counts from edu_teach_examresults r2  where r2.isdeleted = :isDeleted ");
		sql.append("    and r2.examinfoid =:examInfoId  group by r2.checkstatus  order by r2.checkstatus ");
		
		return  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),map);
	}
	/**
	 *  成绩审核-整个考试科目全部通过
	 * @param info
	 * @param teachType
	 * @throws Exception 
	 */
	@Override
	public void auditCourseExamResultsPass(ExamInfo info, String teachType)throws Exception {
		
		User curUser 	 	   = SpringSecurityHelper.getCurrentUser();		 //当前用户
		Map<String,Object> map = new  HashMap<String,Object>();
		Date curTime           = new Date();                                 //当前时间
			
		map.put("examInfoId", info.getResourceid());
		map.put("isDeleted",0);
		//课程各种成绩状态人数
		List<Map<String,Object>> list = queryExamCheckStatusForExamInfo(map);
		Long orderNum_1        = getExamCountByExamInfo(map);
		Long orderNum_2 	   = 0L;
		Long publishNum 	   = 0L; 
		
		if (null!=list&&list.size()>0) {
			for (Map<String,Object> m : list) {
				String checkStatus = (String) 	  m.get("CHECKSTATUS");
				BigDecimal counts  = (BigDecimal) m.get("COUNTS");
				if (Constants.EXAMRESULT_CHECKSTATUS_SUBMIT.equals(checkStatus)||Constants.EXAMRESULT_CHECKSTATUS_PASS.equals(checkStatus)||Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(checkStatus)) {
					orderNum_2    += counts.longValue();
				}else if(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(checkStatus)){
					publishNum    += counts.longValue();
				}
			}
		}
		
		/*if (publishNum>0) {
			throw new ServiceException("成绩已发布，不允许将成绩设置为审核通过状态！");
		}*/
		if (orderNum_2<orderNum_1) {
			throw new ServiceException("成绩提交加审核通过的人数小于预约人数，不允许全部审核通过！");
		}
		if (null==info.getStudyScorePer()) {
			throw new ServiceException("成绩比例未设置，不允许审核成绩!");
		}
		if(ExStringUtils.isBlank(info.getCourseScoreType())) {
			throw new ServiceException("考试课程《"+ExStringUtils.trimToEmpty(info.getExamCourseCode())+info.getCourse().getCourseName()+"》成绩类型未设置，不允许进行审核操作！");
		}
		List<ExamResults> examResults = this.findByCriteria(Restrictions.eq("examInfo",info),Restrictions.eq("isDeleted",0));
		examResults                   = calculateExamResultsListIntegratedScore(examResults, info);
		
		List<StudentCheck> checkList  = new ArrayList<StudentCheck>();
		//设置成绩状态
		for (ExamResults rs :examResults) {
			
			rs.setAuditDate(curTime);
			rs.setAuditMan(curUser.getCnName());
			rs.setAuditManId(curUser.getResourceid());
			rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
			rs.setIntegratedScore(rs.getTempintegratedScore_d());
			
			StudentCheck check = new StudentCheck();
			check.setStudentId(rs.getStudentInfo().getResourceid());
			checkList.add(check);

		}
		
		this.batchSaveOrUpdate(examResults);
		//更新学习计划
		String updateLearnPlan 		  = " UPDATE EDU_LEARN_STUPLAN stuplan set stuplan.status='3' where stuplan.isDeleted=:isDeleted AND stuplan.examinfoid=:examInfoId ";
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(updateLearnPlan, map);
		
		//将发布所涉及的学籍ID放入临时表，计算学生获取的学分
		if (checkList.size()>0) {
			exGeneralHibernateDao.batchSaveOrUpdate(checkList);
		}
	}
	/**
	 * 根据条件查询成绩记录
	 * @param condition
	 * @return
	 */
	@Override
	public List<ExamResults> queryExamResultsByCondition(Map<String,Object> condition){
		
		StringBuffer hql 		 = new StringBuffer();
		Map<String,Object> param = new HashMap<String, Object>();
		hql.append("from "+ExamResults.class.getSimpleName()+" examResults where examResults.isDeleted=:isDeleted ");
		param.put("isDeleted", 0);
		
		if (condition.containsKey("studentId")){ 
			hql.append(" and examResults.studentInfo.resourceid=:studentId");
			param.put("studentId", condition.get("studentId"));	
		}
		if (condition.containsKey("unRecord")){ 
			hql.append(" and examResults.checkStatus!=:unRecord");
			param.put("unRecord", condition.get("unRecord"));	
		}
		if (condition.containsKey("classic")){      
			hql.append(" and examResults.studentInfo.classic.resourceid=:classic");
			param.put("classic", condition.get("classic"));
		}
		if (condition.containsKey("gradeid")){	  
			hql.append(" and examResults.studentInfo.grade.resourceid=:gradeid");
			param.put("gradeid", condition.get("gradeid"));
		}
		if (condition.containsKey("major")){		  
			hql.append(" and examResults.studentInfo.major.resourceid=:major");
			param.put("major", condition.get("major"));	
		}
		if (condition.containsKey("examSub")){     
			hql.append(" and examResults.examsubId=:examSub");
			param.put("examSub", condition.get("examSub"));	
		}
		if (condition.containsKey("examInfoId")) {
			hql.append(" and examResults.examInfo.resourceid=:examInfoId");
			param.put("examInfoId", condition.get("examInfoId"));	
		}
		if (condition.containsKey("courseId")){
			hql.append(" and examResults.course.resourceid=:courseId");
			param.put("courseId", condition.get("courseId"));	
		}
		if(condition.containsKey("majorCourseId")){
			hql.append(" and examResults.majorCourseId=:majorCourseId");
			param.put("majorCourseId", condition.get("majorCourseId"));	
		}
		if (condition.containsKey("branchSchool")){ 
			hql.append(" and examResults.studentInfo.branchSchool.resourceid=:branchSchool");
			param.put("branchSchool", condition.get("branchSchool"));	
		}
		if (condition.containsKey("classesid")){ 
			hql.append(" and examResults.studentInfo.classes.resourceid=:classesid");
			param.put("classesid", condition.get("classesid"));	
		}
		if(condition.containsKey("orderby")){
			hql.append(" order by examResults.studentInfo.branchSchool.unitCode,examResults.studentInfo.studyNo ");
		}
		return this.findByHql(hql.toString(), param);
	}
	
	
	/**
	 *  成绩审核-全部通过(面授课程、面授+网授课程)
	 * @param condition
	 * @param info
	 * @param teachType
	 * @throws ServiceException
	 */
	@Override
	public void auditExamResultsAllPassForFactCourse(Map<String, Object> condition, ExamInfo info, String teachType) throws Exception {
		condition.put("unRecord", "-1");
		List<ExamResults> list = queryExamResultsByCondition(condition);
		
		if (null!=list && !list.isEmpty()) {
		
			int submitNum 	   = 0;
			int publishNum     = 0;
			Date curTime       = new Date ();
			User curUser       = SpringSecurityHelper.getCurrentUser();
			StringBuilder rsIds= new StringBuilder();
			for (ExamResults rs:list ) {
				rsIds.append(",'"+rs.getResourceid()+"'");
				if (Constants.EXAMRESULT_CHECKSTATUS_SUBMIT.equals(rs.getCheckStatus())||Constants.EXAMRESULT_CHECKSTATUS_PASS.equals(rs.getCheckStatus())||Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(rs.getCheckStatus())) {
					submitNum +=1;
				}else if(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(rs.getCheckStatus())){
					publishNum    += 1;
				}
			}
			/*if (publishNum>0) {
				throw new ServiceException("成绩已发布，不允许将成绩设置为审核通过状态！");
			}*/
			if(ExStringUtils.isBlank(info.getCourseScoreType())) {
				throw new ServiceException("考试课程《"+ExStringUtils.trimToEmpty(info.getExamCourseCode())+info.getCourse().getCourseName()+"》成绩类型未设置，不允许进行审核操作！");
			}
			if (submitNum<list.size()) {
				throw new ServiceException("成绩提交人数小于实考人数，不允许全部审核通过！");
			}

			if (null==info.getStudyScorePer() && info.getExamCourseType()==0) {
				throw new ServiceException("成绩比例未设置，不允许审核成绩!");
			}
			/*//如果是AB类课程则，重新计算平时成绩
			if ("networkTeach".equals(teachType)) {
				Map<String,BigDecimal> usualResults = usualResultsService.calculateNetsidestudyUsualResults(info.getCourse().getResourceid(),"");
				for (ExamResults rs :list) {
					String us 						= usualResults.containsKey(rs.getStudentInfo().getResourceid())?usualResults.get(rs.getStudentInfo().getResourceid()).toString():"0";
					rs.setUsuallyScore(us);
				}
			}*/
			
			//计算综合成绩
			list 					      			= calculateExamResultsListIntegratedScore(list, info);
			List<UsualResults> urs   			    = new ArrayList<UsualResults>();
			List<StudentLearnPlan> up     			= new ArrayList<StudentLearnPlan>();
			Map<String,StudentLearnPlan> map    	= new HashMap<String, StudentLearnPlan>();
			List<StudentCheck> checkList  			= new ArrayList<StudentCheck>();
			List<StudentLearnPlan> lp     			= studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=?  and plan.examResults.resourceid in("+rsIds.substring(1)+")",0);
			for (StudentLearnPlan p : lp) {
				map.put(p.getExamResults().getResourceid(), p);
			}
			
			for (ExamResults rs:list) {		
				//网络面授类课程写入平时分
				if ("netsidestudy".equals(teachType)&&ExStringUtils.isNotBlank(rs.getUsuallyScore())) {
					
					UsualResults us		  			= new UsualResults();
					StudentLearnPlan plan 			= null;
					if (map.containsKey(rs.getResourceid())) {
						plan              			= map.get(rs.getResourceid());
						us         		  			= null!=plan.getUsualResults()?plan.getUsualResults():us;
					}
					us.setCourse(rs.getCourse());
					us.setYearInfo(rs.getExamInfo().getExamSub().getYearInfo());
					us.setTerm(rs.getExamInfo().getExamSub().getTerm());
					us.setStudentInfo(rs.getStudentInfo());
					us.setCourseExamResults(rs.getUsuallyScore());
					us.setUsualResults(rs.getUsuallyScore());
					us.setFillinMan(curUser.getUsername());
					us.setFillinManId(curUser.getResourceid());
					us.setFillinDate(curTime);
					us.setStatus("1");
					
					if (null!=plan){
						 plan.setUsualResults(us); up.add(plan);
					} 
					
					urs.add(us);
				}
				
				rs.setAuditDate(curTime);
				rs.setAuditMan(curUser.getCnName());
				rs.setAuditManId(curUser.getResourceid());
				rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
				if(Constants.COURSE_SCORE_TYPE_TWO.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_THREE.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_FOUR.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_FIVE.equals(rs.getCourseScoreType())){// 非百分制成绩处理,这里在计算综合成绩的时候处理过,成了等级了,不要改动
					rs.setTempintegratedScore_d(studentExamResultsService.convertScore(rs.getCourseScoreType(),rs.getIntegratedScore()));
				}else{
					rs.setIntegratedScore(rs.getTempintegratedScore_d());
				}		
				StudentLearnPlan plan = null;
				if (map.containsKey(rs.getResourceid())) {
					plan = map.get(rs.getResourceid());
					plan.setCheckStatus("3");
					plan.setIntegratedScore(rs.getIntegratedScore());
					plan.setIsPass("Y");
				}
								
				StudentCheck check = new StudentCheck();
				check.setStudentId(rs.getStudentInfo().getResourceid());
				checkList.add(check);
			}
			
			this.batchSaveOrUpdate(list);
			List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
			for (ExamResults rs : list) {
				//判断是否需要需要补考
				//20140617 温朝上提出，综合成绩小于60分都进去补考名单
				//20140624 陈浩珍提出，没有成绩与综合成绩小于60的都进入补考名单
				/**
				 *  没有成绩与综合成绩小于60、缺考、缓考和作弊都进入补考，
				 *  但是作弊直接进入二补；具体做法：
				 *  作弊的学生正考一发布就直接插入两条记录（正考作弊、一补0分），
				 *  进入二补补考名单
				 */
				StudentLearnPlan plan = null;
				if (map.containsKey(rs.getResourceid())) {
					plan = map.get(rs.getResourceid());;
				}
				Double integratedScore ;//出现非百分制的优良中差等级
				if(Constants.COURSE_SCORE_TYPE_TWO.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_THREE.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_FOUR.equals(rs.getCourseScoreType())
						|| Constants.COURSE_SCORE_TYPE_FIVE.equals(rs.getCourseScoreType())){// 非百分制成绩处理,这里在计算综合成绩的时候处理过,成了等级了,不要改动
					if(ExStringUtils.isEmpty(rs.getIntegratedScore()) ||
							(ExStringUtils.isNotEmpty(rs.getIntegratedScore()) )
							&& "N".equals(studentExamResultsService.isPassScore(rs.getCourseScoreType(),Double.parseDouble(studentExamResultsService.convertScore(rs.getCourseScoreType(),rs.getIntegratedScore()))))
							|| "5".equals(rs.getExamAbnormity()) || "2".equals(rs.getExamAbnormity()) || "1".equals(rs.getExamAbnormity())){
						if(plan!=null){
							plan.setIsPass("N");//进入补考,则不通过
						}
						handleMadeUp(curTime, curUser, rs);
					}
					//如果补考成绩及格，标记上次补考名单中的是否通过为  Y 
					if(ExStringUtils.isNotBlank(rs.getIntegratedScore())&&"N".equals(studentExamResultsService.isPassScore(rs.getCourseScoreType(),Double.parseDouble(studentExamResultsService.convertScore(rs.getCourseScoreType(),rs.getIntegratedScore()))))){
						String hql = "from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.nextExamSubId=? and a.studentInfo.resourceid=? and a.course.resourceid=? ";
						List<StudentMakeupList> stumakeupList = studentMakeupListService.findByHql(hql, rs.getExamsubId(),rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
						if(stumakeupList!=null && stumakeupList.size()>0){
							StudentMakeupList stumakeup= new StudentMakeupList();
							stumakeup=	stumakeupList.get(0);
							stumakeup.setIsPass("Y");
							makeupList.add(stumakeup);
							//studentMakeupListService.saveOrUpdate(stumakeup);
							if(plan!=null){
								plan.setIsPass("Y");//补考通过,则通过
							}
						}
						
					}
				}else{
					if(ExStringUtils.isEmpty(rs.getIntegratedScore()) ||
							(ExStringUtils.isNotEmpty(rs.getIntegratedScore()) && Double.parseDouble(rs.getIntegratedScore())<60.0)
							|| "5".equals(rs.getExamAbnormity()) || "2".equals(rs.getExamAbnormity()) || "1".equals(rs.getExamAbnormity())){
						if(plan!=null){
							plan.setIsPass("N");//进入补考,则不通过
						}
						handleMadeUp(curTime, curUser, rs);
					}
					//如果补考成绩及格，标记上次补考名单中的是否通过为  Y 
					if(ExStringUtils.isNotBlank(rs.getIntegratedScore())&&Double.parseDouble(rs.getIntegratedScore())>=60){//由于当前只能录入百分制度的分值成绩，所以只判断成绩>=60分为及格
						String hql = "from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.nextExamSubId=? and a.studentInfo.resourceid=? and a.course.resourceid=? ";
						List<StudentMakeupList> stumakeupList = studentMakeupListService.findByHql(hql, rs.getExamsubId(),rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
						if(stumakeupList!=null && stumakeupList.size()>0){
							StudentMakeupList stumakeup= new StudentMakeupList();
							stumakeup=	stumakeupList.get(0);
							stumakeup.setIsPass("Y");
							makeupList.add(stumakeup);
							//studentMakeupListService.saveOrUpdate(stumakeup);
							if(plan!=null){
								plan.setIsPass("Y");//补考通过,则通过
							}
						}
						
					}
				}
				
				
			}
			//更新学习计划
			for (StudentLearnPlan p : lp) {
				p.setStatus(3);
			}
			
			if (!up.isEmpty()) {
				studentLearnPlanService.batchSaveOrUpdate(up);
			}
			if (!urs.isEmpty()) {
				usualResultsService.batchSaveOrUpdate(urs);
			}
			if (!makeupList.isEmpty()) {
				studentMakeupListService.batchSaveOrUpdate(makeupList);
			}
			//将发布所涉及的学籍ID放入临时表，计算学生获取的学分
			if (checkList.size()>0) {
				exGeneralHibernateDao.batchSaveOrUpdate(checkList);
			}
		}else {
			throw new ServiceException("没有需要审核的记录！");
		}
		
	}
	
	/**
	 * 处理成绩审核时的补考逻辑
	 * @param curTime
	 * @param curUser
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	@Override
	public void handleMadeUp(Date curTime, User curUser, ExamResults rs)
			throws Exception {
		for(int k=0;k<1;k++){
			String hqltp = " from "+TeachingPlanCourse.class.getSimpleName()+" a where a.isDeleted=0 and a.teachingPlan.resourceid=? and a.course.resourceid=? and a.teachType='facestudy'";
			List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findByHql(hqltp,
					rs.getStudentInfo().getTeachingPlan().getResourceid(), rs.getCourse().getResourceid());
			if(null!=planCourseList && planCourseList.size()>0){//只处理非统考
				
				Long year = 0L;
				String term = "";
				YearInfo yearInfo = null;
				ExamSub nextExamSub = null;
				ExamSub _examSub =  examSubService.get(rs.getExamsubId());
				TeachingPlanCourse teachingPlanCourse = planCourseList.get(0);
				TeachingPlanCourseStatus planCourseStatus = null;
				String hqlgui = "from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=0 and teachingPlan.resourceid=? and grade.resourceid=?";
				List<TeachingGuidePlan> guiList = teachingGuidePlanService.findByHql(hqlgui, rs.getStudentInfo().getTeachingPlan().getResourceid(),rs.getStudentInfo().getGrade().getResourceid());
				
				Long nextYear = 0L;
				String nextTerm = "";
				Long index = 1L;//第几个上课学期
				// 考试规则
				String examinationRule = CacheAppManager.getSysConfigurationByCode("examinationRule").getParamValue();
				// 结补规则
				String finalExaminationRule = CacheAppManager.getSysConfigurationByCode("finalExaminationRule").getParamValue();
				
				String isMachineExam = Constants.BOOLEAN_NO;
				if (guiList != null && guiList.size() > 0) {
					yearInfo = guiList.get(0).getGrade().getYearInfo();//入学年级
					year = guiList.get(0).getGrade().getYearInfo().getFirstYear();//所属年级
					//获取课程上课学期
					String hqlstatus = "from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingPlanCourse.resourceid=?"
							+" and teachingGuidePlan.resourceid=? and schoolIds=? and isOpen='Y' and checkStatus!='cancelY' ";

					List<TeachingPlanCourseStatus> staList = teachingPlanCourseStatusService.findByHql(hqlstatus, teachingPlanCourse.getResourceid()
							, guiList.get(0).getResourceid(), rs.getStudentInfo().getBranchSchool().getResourceid());

					if (staList != null && staList.size() > 0) {
//						String staId = staList.get(0).getResourceid();
						planCourseStatus = staList.get(0);
						term = planCourseStatus.getTerm();//课程上课学期
						if(planCourseStatus.getExamForm()!=null &&planCourseStatus.getExamForm()==3){
							isMachineExam = Constants.BOOLEAN_YES;
						}
					} else {
						throw new ServiceException("该门课程还未开课！");
					}
					String[] terms = term.split("_");
					Long termYear = Long.parseLong(terms[0]);
					for (Long i = year; i <= termYear; i++) {
						for (int j = 1; j <= 2; j++) {
							if (term.equals(i + "_0" + j)) {
								break;
							}
							index++;
						}
					}
					String _examType ="N";
					String examTypeStr = "一补";
					// 获取考试规则
					/**
					 * 1：（正考：开课学期；---------------广大才使用这个逻辑------------
					 * 			一补：开课的下一学期，第6学期仍在第6学期；
					 * 			二补：第5学期，第6学期仍在第6学期；
					 * 			结补：第6学期；）
					 * 2：（正考：开课学期；
					 * 			补考【一补】：开课的下一学期，第6学期仍在第6学期；
					 * 			结补：第6学期；
					 * 			返校一补：在第8学期、-------广西医科大使用这个逻辑---------
					 * 			返校二补：在第10学期）
					 * 3： （正考：开课学期；
					 * 			补考【一补】：开课的下一个学期，不及格继续接着下学期的一补
					 * 			结补：第6学期）
					 */
					// TODO:目前，所有考试规则的正考是一样的，因此不修改
					if("N".equals(_examSub.getExamType())){
						if (index>=1 && index<=5) {//下学期
							if ("02".equals(terms[1])) {
								nextYear = Long.parseLong(terms[0]) + 1;
								nextTerm = "1";
							} else {
								nextYear = Long.parseLong(terms[0]);
								nextTerm = "2";
							}
						} else {//第6学期
							nextYear = year +2;
							nextTerm = "2";
						} 
						_examType ="Y";
						examTypeStr = "一补";
						//  2017/03/02  增加逻辑，由广州大学胡科确认该逻辑为学校学生手册的规定
						//在安排结业补考之前，第六学期的课程:
						//  1、除 毕业实习、毕业设计 的其他课程是否只安排正考及一次“毕业前补考”，即对就系统的二补，若在毕业审核后，符合结业换毕业，安排结业补考
						//  2、毕业实习、毕业设计 这两门课程，不安排补考，正考不及格，若在毕业审核后，符合结业换毕业，安排结业补考
						//   该逻辑 从2015级学生开始生效
						if("1".equals(examinationRule)){
							if(index==6){
								int isThesis=rs.getCourse().getCourseName().indexOf("毕业");
								if(isThesis!=-1){//课程名称包含 “毕业” 两个字    （已查询教学计划课程名称，在第六学期的，包含毕业两字的一定是毕业实习或毕业设计）
									_examType ="Q";
									examTypeStr = "结补";
								}else{
									_examType ="T";
									examTypeStr = "二补";
								}
							}
						}
					} else if ("Y".equals(_examSub.getExamType())) {//提交一补成绩
						if("1".equals(examinationRule)){//一补进入二补
							if (index>=1 && index<=4) {
								nextYear = year +2;
								nextTerm = "1";
							} else {
								nextYear = year +2;
								nextTerm = "2";
							}
							_examType ="T";
							examTypeStr = "二补";
						} else if("2".equals(examinationRule)){// 一补进入结补
							nextYear = year + 2;
							nextTerm = "2";
							_examType ="Q";
							examTypeStr = "结补";
						}else if("3".equals(examinationRule)){// 一补完了不及格进入下学期的一补
							int _term = Integer.parseInt(_examSub.getTerm());
							long _year = _examSub.getYearInfo().getFirstYear();
							if(_term == 1){
								nextYear = _year;
								nextTerm = "2";
							}else if (_term == 2){
								nextYear = _year + 1;
								nextTerm = "1";
							}
							if(((year+2.0)==_year)&&(_term==2)){
								nextYear = _year;
								nextTerm = "2";
								_examType ="Q";
								examTypeStr = "结补";
							}else{
								_examType ="Y";
								examTypeStr = "一补";
							}
							
						}
						
					} else if ("T".equals(_examSub.getExamType())
							|| "Q".equals(_examSub.getExamType())) {//提交二补、结补成绩
						if("1".equals(finalExaminationRule)){//结补之后，就是进行返校补
							nextYear = year + 3;//第8学期
							nextTerm = "2";
							_examType ="R";
							examTypeStr = "返校补";
						}else if("1".equals(examinationRule)){
							nextYear = year + 2;
							nextTerm = "2";
							_examType ="Q";
							examTypeStr = "结补";
						}else {
							continue;
						}
					}else if ("R".equals(_examSub.getExamType())) {//返校一补
						if("1".equals(finalExaminationRule)){//返校一补之后，就是返校二补
							nextYear = year + 4;//第10学期
							nextTerm = "2";
							_examType ="G";
							examTypeStr = "返校二补";
						}else {
							continue;
						}
					}else if ("G".equals(_examSub.getExamType())){//返校二补之后另外弄
						continue;
					}
					
					nextExamSub = examSubService.findExamSubBycondition(_examType, nextYear, nextTerm);
					if(nextExamSub == null){
						nextExamSub = new ExamSub();
						nextExamSub.setResourceid(nextYear+nextTerm+_examType);
						nextExamSub.setBatchName(nextYear+"年第"+nextTerm+"学期"+examTypeStr);
						nextExamSub.setExamType(_examType);
						nextExamSub.setYearInfo(yearInfoService.findOrCreateYearInfoByFirstYear(nextYear));
						nextExamSub.setTerm(nextTerm);
						nextExamSub.setCourseScoreType("11");
						nextExamSub.setIsseatPublished("Y");
						nextExamSub.setExamsubStatus("3");
						nextExamSub.setBatchType("exam");
						nextExamSub.setIsSpecial("N");
						nextExamSub.setIsAbnormityEnd("N");
						examSubService.save(nextExamSub);
					}
				}
				Map<String,Object> _condition = new HashMap<String, Object>();
				_condition.put("examSubId", nextExamSub.getResourceid());
				_condition.put("courseId",rs.getCourse().getResourceid());
				_condition.put("scoreStyle",teachingPlanCourse.getScoreStyle());
				_condition.put("examCourseType", "networkTeach".equals(planCourseStatus.getTeachType()) ?0:1);
				_condition.put("isMachineExam",isMachineExam);
				ExamInfo examInfo = examInfoService.getExamInfo(_condition);
				// 作弊逻辑，只有考试类型为作弊并且为正考时执行
				if("1".equals(rs.getExamAbnormity()) && "N".equals(_examSub.getExamType()) && "1".equals(examinationRule)){
					TeachingPlanCourse pc = planCourseList.get(0);
					// 插入一条已发布的一补成绩
					ExamResults _examResults = new ExamResults();
					_examResults.setCourse(rs.getCourse());	
					_examResults.setExamInfo(examInfo);
					_examResults.setMajorCourseId(pc.getResourceid());
					_examResults.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
					_examResults.setExamsubId(nextExamSub.getResourceid());
					_examResults.setCreditHour(pc.getCreditHour()!=null?pc.getCreditHour():null);
					_examResults.setStydyHour(pc.getStydyHour()!=null?pc.getStydyHour().intValue():null);
					_examResults.setExamType(rs.getCourse().getExamType()!=null?rs.getCourse().getExamType().intValue():null);
					_examResults.setIsMakeupExam(nextExamSub.getExamType());//考试类型
					_examResults.setCourseType(pc.getCourseType());														
					_examResults.setStudentInfo(rs.getStudentInfo());
					_examResults.setCourseScoreType(examInfo.getCourseScoreType());
					_examResults.setExamCount(2L);	
					_examResults.setPlanCourseTeachType(pc.getTeachType());
					_examResults.setExamAbnormity("0");
					_examResults.setIsMachineExam(isMachineExam);;
					_examResults.setWrittenScore("0");
					_examResults.setUsuallyScore("0");
					_examResults.setOnlineScore("0");
					_examResults.setIntegratedScore("0");
					_examResults.setFillinDate(curTime);
					_examResults.setFillinMan(curUser.getCnName());
					_examResults.setFillinManId(curUser.getResourceid());
					
					save(_examResults);
					// 这个成绩直接进二补名单
					rs = _examResults;
					// 获取二补的考试批次
					if( index >=1 && index <=4){
						nextTerm = "1";
					} else {
						nextTerm = "2";
					}
					nextYear = year+2;
					
					nextExamSub = examSubService.findExamSubBycondition("T", nextYear, nextTerm);
					if(nextExamSub == null){
						nextExamSub = new ExamSub();
						nextExamSub.setResourceid(nextYear+nextTerm+"T");
						nextExamSub.setBatchName(nextYear+"年第"+nextTerm+"学期"+"二补");
						nextExamSub.setExamType("T");
						nextExamSub.setYearInfo(yearInfoService.findOrCreateYearInfoByFirstYear(nextYear));
						nextExamSub.setTerm(nextTerm);
						nextExamSub.setCourseScoreType("11");
						nextExamSub.setIsseatPublished("Y");
						nextExamSub.setExamsubStatus("3");
						nextExamSub.setBatchType("exam");
						nextExamSub.setIsSpecial("N");
						nextExamSub.setIsAbnormityEnd("N");
						examSubService.save(nextExamSub);
					}
				}
				
				List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
				StudentMakeupList makeup = null;
				String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? and a.examResults.resourceid=? "
						+ " and a.teachingPlanCourse.resourceid=? ";
				//如果学生这门课没通过，查询补考表，有则替换成绩，没有则添加
				makeupList = studentMakeupListService.findByHql(hqlMakeup,rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid(),rs.getResourceid(),planCourseList.get(0).getResourceid());
			    if(null==makeupList || makeupList.size()==0){
			    	makeup = new StudentMakeupList();
			    	makeup.setStudentInfo(rs.getStudentInfo());
			    	makeup.setCourse(rs.getCourse());
			    	makeup.setExamResults(rs);
			    	makeup.setTeachingPlanCourse(planCourseList.get(0));
			    	makeup.setIsPass("N");
			    	makeup.setIsMachineExam(isMachineExam);
			    	if (nextExamSub != null) {
						makeup.setNextExamSubId(nextExamSub.getResourceid());
					}
			    } else if(makeupList.size()>0){
			    	makeup = makeupList.get(0);
			    	makeup.setExamResults(rs);
			    	makeup.setIsMachineExam(isMachineExam);
			    	if (nextExamSub != null) {
						makeup.setNextExamSubId(nextExamSub.getResourceid());
					}
			    }
			    if(makeup != null){
			    	studentMakeupListService.saveOrUpdate(makeup);
			    }
			}
		}
	}
	
	/**
	 * 给定考试课程是否允许发布
	 * @param info
	 * @param teachType
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public boolean isAllowPublishedExamInfosResults(ExamInfo info, String teachType, Map<String,Object> condition)throws Exception {
		
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isAllow        = true;
		
		//网络课程
		if ("networkstudy".equals(teachType)) {
			map.put("examInfoId", info.getResourceid());
			map.put("isDeleted",0);
			//课程各种成绩状态人数
			List<Map<String,Object>> list = queryExamCheckStatusForExamInfo(map);
			Long statusNum_1       = getExamCountByExamInfo(map);
			Long statusNum_2 	   = 0L;


			if (null!=list&&list.size()>0) {
				for (Map<String,Object> m : list) {
					String checkStatus = (String) 	  m.get("CHECKSTATUS");
					BigDecimal counts  = (BigDecimal) m.get("COUNTS");
					if("3".equals(checkStatus)||"4".equals(checkStatus)){
						statusNum_2   += counts.longValue();
					}
				}
			}
			/*if (publishNum>0) {
				throw new ServiceException("操作有误：《"+info.getCourse().getCourseName()+"》成绩已发布！");
			}*/
			if (statusNum_2<statusNum_1) {
				throw new ServiceException("《"+info.getCourse().getCourseName()+"》成绩中有"+(statusNum_1-statusNum_2)+"人成绩未审核通过，不允许发布！");
			}
		//面授、面授+网授	
		}else {
			int passNum 		   = 0;
			List<ExamResults> list = queryExamResultsByCondition(condition);
			if (null!=list&&list.size()>0) {
				for (ExamResults rs:list ) {
					if ("3".equals(rs.getCheckStatus())||"4".equals(rs.getCheckStatus())) {
						passNum +=1;
					}
				}

				if (list.size()>passNum) {
					throw new ServiceException("发布出错："+(list.size()-passNum)+"成绩未审核通过，不允许发布！");
				}
			}
		}

		return isAllow;
	}
	/**
	 * 发布成绩时将所涉及的学生写入成绩计算学籍ID表
	 * @param teachType
	 * @param condition
	 * @throws Exception
	 */
	public void updateCalculateCreditHourStudent(String teachType,Map<String,Object> condition)throws Exception{
		
		Map<String,Object> param              = new HashMap<String, Object>();
		List<StudentCheck> checkList      	  = new ArrayList<StudentCheck>();
		List<Map<String,Object>> stuIds   	  = new ArrayList<Map<String,Object>>();
		StringBuffer calculateStuCreditSql    = new StringBuffer();
		
		param.put("isDeleted", 0);
		param.put("status", "3");
		
		calculateStuCreditSql.append(" select results.studentid from  EDU_TEACH_EXAMRESULTS results ");
		
		if ("networkstudy".equals(teachType)) {
			param.put("examInfoId",condition.get("examInfoId"));
			calculateStuCreditSql.append("  WHERE results.isDeleted=:isDeleted AND results.CHECKSTATUS=:status AND results.examinfoid=:examInfoId ");
		}else {
			calculateStuCreditSql.append(" inner join edu_roll_studentinfo stu on results.studentid = stu.resourceid ");
			if (condition.containsKey("branchSchool")){ 
				calculateStuCreditSql.append(" AND stu.branchschoolid=:branchSchool ");
				param.put("branchSchool", condition.get("branchSchool"));	
			}
			calculateStuCreditSql.append(" inner join edu_base_grade g on g.resourceid = stu.gradeid ");
			if (condition.containsKey("gradeid")){	  
				calculateStuCreditSql.append(" AND g.resourceid=:gradeid ");
				param.put("gradeid", condition.get("gradeid"));
			}
			calculateStuCreditSql.append(" inner join edu_base_classic c on c.resourceid = stu.classicid ");
			if (condition.containsKey("classic")){      
				calculateStuCreditSql.append(" AND  c.resourceid=:classic ");
				param.put("classic", condition.get("classic"));
			}
			calculateStuCreditSql.append(" inner join edu_base_major m on m.resourceid = stu.majorid ");
			if (condition.containsKey("major")){		  
				calculateStuCreditSql.append(" AND m.resourceid=:major ");
				param.put("major", condition.get("major"));	
			}
			calculateStuCreditSql.append(" WHERE results.isDeleted=:isDeleted AND results.CHECKSTATUS=:status ");
			if (condition.containsKey("examSub")){     
				calculateStuCreditSql.append(" AND results.examsubid=:examSub ");
				param.put("examSub", condition.get("examSub"));	
			}
			if (condition.containsKey("examInfoId")) {
				calculateStuCreditSql.append(" AND results.examinfoid=:examInfoId ");
				param.put("examInfoId", condition.get("examInfoId"));	
			}
			if (condition.containsKey("courseId")){
				calculateStuCreditSql.append(" AND results.courseid=:courseId ");
				param.put("courseId", condition.get("courseId"));	
			}
		}
		stuIds =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(calculateStuCreditSql.toString(),param);
		for (Map<String,Object> map: stuIds) {
			StudentCheck check = new StudentCheck();
			check.setStudentId(map.get("studentid").toString());
			checkList.add(check);
		}
		if (checkList.size()>0) {
			exGeneralHibernateDao.batchSaveOrUpdate(checkList);
		}
	}
	/**
	 * 发布考试课程的成绩
	 * @param info
	 * @param teachType
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@Override
	public int publishedExamResultsByExamInfo(ExamInfo info, String teachType, Map<String,Object> condition)throws Exception {
		
		Map<String,Object> map = new HashMap<String, Object>();
		int counts             = 0;

		map.put("isDeleted",0);
		if ("networkstudy".equals(teachType)) {
			map.put("examInfoId", condition.get("examInfoId"));
			
			String updateResults   = " UPDATE EDU_TEACH_EXAMRESULTS results SET results.CHECKSTATUS='4' WHERE results.isDeleted=:isDeleted AND results.CHECKSTATUS='3' AND results.examinfoid=:examInfoId";
			String updateLearnPlan = " UPDATE EDU_LEARN_STUPLAN stuplan set stuplan.status='3' where stuplan.isDeleted=:isDeleted AND stuplan.examinfoid=:examInfoId ";
			
			//将发布所涉及的学籍ID放入临时表，供计算学分用
			updateCalculateCreditHourStudent(teachType,map);
			counts             	   = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(updateResults, map);
			if (counts>=0) {
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(updateLearnPlan, map);
			}
		}else{
			
			StringBuffer updateLearnPlan = new StringBuffer();
			updateLearnPlan.append(" update "+StudentLearnPlan.class.getSimpleName()+" plan set plan.status=:status3 ")
						   .append(" where exists ( ")
						   .append(" select plan1.resourceid from "+StudentLearnPlan.class.getSimpleName()+" plan1 ")
						   .append(" where plan.resourceid=plan1.resourceid and plan1.isDeleted=:isDeleted ");
			
			StringBuffer updateResults = new StringBuffer();
			updateResults.append("UPDATE  "+ExamResults.class.getSimpleName()+" exam SET exam.checkStatus=:status2 ")
						.append(" WHERE exists  (  ")
						.append(" SELECT exam1.resourceid FROM "+ExamResults.class.getSimpleName()+" exam1 ")
						.append(" WHERE exam.resourceid=exam1.resourceid and exam1.isDeleted=:isDeleted AND exam1.checkStatus=:status1 ");
			
			map.put("status1", "3");
			map.put("status2", "4");			
			map.put("status3", 3);
			if (condition.containsKey("classic")){      
				updateResults.append(" AND exam1.studentInfo.classic.resourceid=:classic ");
				updateLearnPlan.append(" and plan1.studentInfo.classic.resourceid=:classic ");
				map.put("classic", condition.get("classic"));
			}
			if (condition.containsKey("gradeid")){	  
				updateResults.append(" AND exam1.studentInfo.grade.resourceid=:gradeid ");
				updateLearnPlan.append(" and plan1.studentInfo.grade.resourceid=:gradeid ");
				map.put("gradeid", condition.get("gradeid"));
			}
			if (condition.containsKey("major")){		  
				updateResults.append(" AND exam1.studentInfo.major.resourceid=:major ");
				updateLearnPlan.append(" and plan1.studentInfo.major.resourceid=:major");
				map.put("major", condition.get("major"));	
			}
			if (condition.containsKey("examSub")){     
				updateResults.append(" AND exam1.examsubId=:examSub ");
				updateLearnPlan.append(" and plan1.examInfo.examSub.resourceid=:examSub ");
				map.put("examSub", condition.get("examSub"));	
			}
			if (condition.containsKey("examInfoId")) {
				updateResults.append(" AND exam1.examInfo.resourceid=:examInfoId ");
				updateLearnPlan.append(" and plan1.examInfo.resourceid=:examInfoId ");
				map.put("examInfoId", condition.get("examInfoId"));	
			}
			if (condition.containsKey("courseId")){
				updateResults.append(" AND exam1.course.resourceid=:courseId ");
				updateLearnPlan.append(" and plan1.examInfo.course.resourceid=:courseId ");
				map.put("courseId", condition.get("courseId"));	
			}
			if (condition.containsKey("branchSchool")){ 
				updateResults.append(" AND exam1.studentInfo.branchSchool.resourceid=:branchSchool ");
				updateLearnPlan.append(" and plan1.studentInfo.branchSchool.resourceid=:branchSchool ");
				map.put("branchSchool", condition.get("branchSchool"));	
			}
			updateResults.append(" ) ");
			updateLearnPlan.append(" ) "); 
			
			//将发布所涉及的学籍ID放入临时表，供计算学分用
			updateCalculateCreditHourStudent(teachType,map);
			
			counts = this.exGeneralHibernateDao.executeHQL(updateResults.toString(), map);
			
			if (counts>=0) {
				this.exGeneralHibernateDao.executeHQL(updateLearnPlan.toString(),map);
			}
		}
		
		return counts;
	}
	/**
	 * 面授、面授+网授课程成绩审核查询
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Page queryExamResultsInfoForFaceTeachType(Map<String, Object> condition, Page page)  {
		StringBuffer sql = new StringBuffer(1024);
		List<Object> param = new ArrayList<Object>(20);
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		sql.append(" select u.unitCode,u.unitShortName,u.unitName,g.gradename,cl.classicname,m.majorname,classes.classesname,c.coursecode,c.coursename,es.batchname,decode(ei.examCourseType,1,ei.facestudyscoreper,ei.studyScorePer) facestudyscoreper");
		sql.append(",ctl.lecturerName,decode(ei.examCourseType,1,ei.facestudyscoreper2,ei.netsidestudyScorePer) facestudyscoreper2,ei.resourceid examInfoId,ei.examCourseType");
		sql.append(",g.resourceid gradeId,cl.resourceid classicId,m.resourceid majorId,classes.resourceid classesid,c.resourceid courseId,es.resourceid ");
		//应考人数应该为班级人数  2017-05-08
		sql.append(" ,(select count(*) from edu_roll_studentinfo si where si.classesid=classes.resourceid and si.studentstatus in('11','16','21','24','25') and si.isdeleted=0) headExam");//count(rs.resourceid) realExam ");
		//实考人数，实际参加考试的人数（不包括缺考和缓考）2017-09-18
		sql.append(" ,sum(case when rs.examabnormity not in('2','5') and rs.checkstatus!= '-1' and stu.studentstatus in ('11', '16', '21', '24', '25') then 1 else 0 end) realExam ");
		sql.append(",pc.resourceid majorCourseId,es.resourceid examsubid,decode(ei.EXAMCOURSETYPE, 0,'networkTeach',1, 'faceTeach', '') teachType ");
		//统计各个成绩状态人数 保存/提交/发布
		sql.append(",sum(decode(rs.CHECKSTATUS,'0',1,0)) checkstatus_save");
		sql.append(",sum(decode(rs.CHECKSTATUS,'1',1,0)) checkstatus_submit");
		sql.append(",sum(decode(rs.CHECKSTATUS,'4',1,0)) checkstatus_publish");
		
		
		/*20180730注释
		 * sql.append("  from edu_teach_plancourse pc ");
		sql.append(" join EDU_TEACH_COURSESTATUS cs on cs.PLANCOURSEID=pc.resourceid and cs.isopen='Y' and cs.isdeleted=0 ");
		sql.append(" join edu_teach_guiplan gp on gp.resourceid=cs.GUIPLANID and gp.ispublished='Y' and gp.isdeleted=0 ");
		sql.append(" inner join edu_base_course c on pc.courseid = c.resourceid ");

		//2012.9.12朝上提出不过滤毕业学生
		sql.append(" join edu_roll_studentinfo stu on pc.planid = stu.teachplanid ");//and stu.studentstatus in('11','16','21','24','25')
		sql.append(" join hnjk_sys_unit u on u.resourceid=cs.schoolids and stu.branchschoolid= u.resourceid");
		sql.append(" join edu_base_grade g on stu.gradeid = g.resourceid and g.resourceid=gp.gradeid and stu.gradeid=gp.gradeid and cs.schoolids=stu.branchschoolid and stu.isDeleted=0 ");
		sql.append(" join edu_base_classic cl on stu.classicid = cl.resourceid ");
		sql.append(" join edu_base_major m on stu.majorid = m.resourceid ");
		sql.append(" join edu_roll_classes classes on classes.resourceid = stu.classesid");
		sql.append(" join EDU_TEACH_EXAMINFO ei on ei.courseid=pc.courseid and ei.EXAMCOURSETYPE=decode(cs.TEACHTYPE,'networkTeach',0,'faceTeach',1,2) and ei.ISDELETED = 0 ");
		sql.append(" join edu_teach_examsub es on es.resourceid=ei.examsubid and es.isdeleted = 0 ");
		sql.append(" join edu_teach_examresults rs on rs.courseid=pc.courseid and rs.studentid = stu.resourceid and rs.EXAMSUBID=es.RESOURCEID and rs.examinfoid=ei.resourceid and rs.isdeleted = 0 ");*/
		sql.append("  from edu_teach_examresults rs ");
	    sql.append("  inner join edu_roll_studentinfo stu on stu.isDeleted=0 and stu.resourceid=rs.studentid ");
	    sql.append(" and stu.studentstatus in('11','16','21','24','25')");
	    sql.append("  inner join edu_teach_plancourse pc on pc.isdeleted=0 and rs.majorcourseid=pc.resourceid and pc.teachtype=? "); 
	    param.add(condition.get("teachType"));

	    sql.append(" left join EDU_TEACH_COURSETEACHERCL ctl on ctl.courseid=pc.courseid and ctl.CLASSESID=stu.classesid and ctl.isdeleted=0");
		sql.append("  inner join EDU_BASE_COURSE c  on pc.courseid=c.resourceid ");
	    sql.append("  inner join HNJK_SYS_UNIT u on u.resourceid=stu.branchschoolid ");
	    sql.append("  inner join EDU_BASE_GRADE g on stu.gradeid=g.resourceid ");
	    sql.append("  inner join EDU_BASE_CLASSIC cl on stu.classicid=cl.resourceid ");
	    sql.append("  inner join EDU_BASE_MAJOR m on stu.majorid=m.resourceid ");
	    sql.append("  inner join EDU_ROLL_CLASSES classes on classes.resourceid=stu.classesid ");
	    sql.append("  inner join EDU_TEACH_EXAMINFO ei on ei.courseid=pc.courseid and ei.ISDELETED=0 and ei.resourceid=rs.examinfoid "); 
	    sql.append("  inner join EDU_TEACH_EXAMSUB es on es.resourceid = ei.examsubid and es.isdeleted = 0 and es.resourceid=rs.examsubid ");
		sql.append(" where rs.isdeleted=0 ");

		if (!"10560".equals(schoolCode)) {//汕大
			sql.append(" and ctl.teacherid is not null");
		}
		if (condition.containsKey("unRecord")){
			sql.append(" and rs.checkStatus!=?");
			param.add(condition.get("unRecord"));
		}
		if (condition.containsKey("checkStatus")){
			sql.append(" and rs.checkStatus=?");
			param.add(condition.get("checkStatus"));
		}
		if (condition.containsKey("courseTeachType")) {
			sql.append(" and decode(ei.EXAMCOURSETYPE, 0, 'networkTeach', 1, 'faceTeach', '')=?");
			param.add(condition.get("courseTeachType"));
		}
		if (condition.containsKey("branchSchool")){
			sql.append(" and stu.branchschoolid=?");
			param.add(condition.get("branchSchool"));
		}
		if (condition.containsKey("examSubId")) {
			sql.append(" and es.resourceid=?");
			param.add(condition.get("examSubId"));
		}
		if (condition.containsKey("examInfoId")){
			sql.append(" and ei.resourceid=?");
			param.add(condition.get("examInfoId"));
		}
		if (condition.containsKey("courseId")) {
			sql.append(" and c.resourceid=?");
			param.add(condition.get("courseId"));
		}
		if (condition.containsKey("gradeid")){
			sql.append(" and g.resourceid=?");
			param.add(condition.get("gradeid"));
		}
		if (condition.containsKey("classic")){
			sql.append(" and cl.resourceid=? ");
			param.add(condition.get("classic"));
		}
		if (condition.containsKey("major")){
			sql.append(" and  m.resourceid =? ");
			param.add(condition.get("major"));
		}
		if (condition.containsKey("classId")){
			sql.append(" and classes.resourceid=?");
			param.add(condition.get("classId"));
		}
		//注释cs.teachType，避免学籍异动学生出现两条记录,但是注释后发现对成绩审核有影响
		sql.append(" group by u.unitCode,u.unitShortName,u.unitName,g.gradename,cl.classicname,m.majorname,c.coursecode,c.coursename,es.batchname,ctl.lecturerName,g.resourceid,cl.resourceid,m.resourceid,c.resourceid,es.resourceid,pc.resourceid,");
		sql.append("classes.classesname,classes.resourceid,ei.resourceid,ei.facestudyscoreper,ei.facestudyscoreper2,ei.examCourseType, ei.studyScorePer,ei.netsidestudyScorePer");
		sql.append(" order by ");
		if ("10560".equals(schoolCode)) {
			sql.append("ctl.lecturerName,");
		}
		sql.append("u.unitCode,u.unitShortName,g.gradename desc,cl.classicname,m.majorname,classes.classesname,ei.examCourseType");

		try {
//			long starTime = System.currentTimeMillis();
			page  = baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), param.toArray(), FaceExamResultsVo.class);
			//logger.info("成绩审核查询页面,执行sql语句耗时："+(System.currentTimeMillis()-starTime)+"ms.");
		} catch (Exception e) {
			logger.error("获取面授课成绩审核应考人数出错："+e.fillInStackTrace());
		}
		return page;
	}
	
	/**
	 * 考试统计
	 * @param condition
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Page examResultStatistics(Map<String, Object> condition, Page page)  {
		StringBuffer sql 		  = new StringBuffer();
		Map<String,Object> param              = new HashMap<String, Object>();
		
		sql.append("select sub.batchname,unit.unitname,g.gradename,m.majorname,classes.classesname,c.classicname,p.schooltype,classes.classesmaster,course.coursename,course.coursecode");
		sql.append(" from edu_teach_plan p "); 
		sql.append(" inner join edu_teach_guiplan pg on pg.planid = p.resourceid ");
		sql.append(" inner join edu_teach_plancourse pc on pc.planid = p.resourceid ");
		sql.append(" inner join edu_teach_coursestatus cs on pc.resourceid = cs.plancourseid and pg.resourceid = cs.guiplanid and cs.isopen ='Y' and cs.isdeleted= 0  ");
		sql.append(" inner join edu_roll_classes classes on classes.gradeid = pg.gradeid and classes.classicid = p.classicid and classes.teachingType = p.schooltype and classes.orgunitid = cs.schoolids and classes.MAJORID=p.majorid and classes.isdeleted = 0 ");
		sql.append(" inner join hnjk_sys_unit unit on classes.orgunitid = unit.resourceid ");
		sql.append(" inner join edu_base_grade g on g.resourceid = pg.gradeid "); 
		sql.append(" inner join edu_base_year y on y.resourceid = g.yearid ");
		sql.append(" inner join edu_base_classic c on c.resourceid = p.classicid ");
		sql.append(" inner join edu_base_major m on m.resourceid = p.majorid ");
		sql.append(" inner join EDU_ROLL_STUDENTINFO stu on p.resourceid=stu.teachplanid and stu.classesid=classes.resourceid ");
		sql.append(" inner join edu_teach_examresults rs on rs.courseid=pc.courseid and rs.studentid = stu.resourceid and rs.isdeleted =0 ");
		sql.append(" inner join EDU_TEACH_EXAMSUB sub on rs.examsubid=sub.resourceid ");
		sql.append(" inner join edu_base_course course on course.resourceid = pc.courseid "); 
		sql.append(" where 1=1 ");
		if (condition.containsKey("examSubId")){
			sql.append(" and rs.examsubid=:examSubId ");
			param.put("examSubId", condition.get("examSubId"));
		}
		
		sql.append(" group by sub.resourceid,pc.courseid,sub.batchname,unit.unitname,g.gradename,classes.classesname,c.classicname,p.schooltype,classes.classesmaster,course.coursename,course.coursecode,m.majorname,course.resourceid ");
		sql.append(" order by course.resourceid ");
		
		try {
			page  = findBySql(page, sql.toString(), param);
		} catch (Exception e) {
			logger.error("考试统计出错："+e.fillInStackTrace());
		}
		return page;
	}

	@Override
	public Page findAuditExamResulstsVoByCondition(Map<String, Object> condition, Page page) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();		
		if(condition.containsKey("scoreChange")){
			condition.remove("scoreChange");
			sql = getAuditChangeExamResultsSqlByCondition(condition, param);
		}else{
			sql = getAuditExamResultsSqlByCondition(condition, param);
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), param.toArray(), AuditExamResultsVo.class);
	}
	
	@Override
	public AuditExamResultsVo findAuditExamResulstsVoByExamResultsId(String examResultsId) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("batchType", "thesis");
		condition.put("examResultsId", examResultsId);
		StringBuffer sql = getAuditExamResultsSqlByCondition(condition, param);
		try {			
			List<AuditExamResultsVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), param.toArray(), AuditExamResultsVo.class);
			if(ExCollectionUtils.isNotEmpty(list)){
				return list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 待更正成绩查询sql
	 * @param condition
	 * @param param
	 * @return
	 */
	private StringBuffer getAuditExamResultsSqlByCondition(Map<String, Object> condition, List<Object> param) {
		StringBuffer sql = new StringBuffer();
		if(condition.containsKey("batchType") && "thesis".equals(condition.get("batchType"))){//毕业论文
			sql.append("select t.resourceid,t.studentid,p.resourceid paperOrderId,p.firstscore,p.secondscore,t.integratedscore,t.examabnormity,");
			sql.append(" a.resourceid resultAuditId,a.changedfirstscore,a.changedsecondscore,a.changedintegratedscore,a.changedexamabnormity,  ");
			sql.append(" t.examsubid,sub.batchname,s.studyno,s.studentname,a.memo ");
			sql.append(" from edu_teach_examresults t ");
			sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid ");
			sql.append(" join edu_teach_examsub sub on t.examsubid=sub.resourceid ");
			sql.append(" left join edu_teach_papersorder p on p.isdeleted=0 and p.status=1 and p.studentid=t.studentid and p.examsubid=t.examsubid ");
			sql.append(" left join edu_teach_auditresults a on (a.resultsid=t.resourceid and a.auditType=1 and a.auditStatus=0) ");
			sql.append(" where t.isdeleted=? and t.checkstatus=? ");
			param.add(0);
			param.add("4");//审核发布的成绩
			if(condition.containsKey("examSubId")){
				sql.append(" and t.examsubid=? ");
				param.add(condition.get("examSubId"));
			}
			if(condition.containsKey("studyNo")){
				sql.append(" and s.studyno like ? ");
				param.add("%" + condition.get("studyNo").toString().trim() + "%");
			}
			if(condition.containsKey("studentName")){
				sql.append(" and s.studentname like ? ");
				param.add("%"+condition.get("studentName")+"%");
			}
			if(condition.containsKey("examResultsId")){//成绩id
				sql.append(" and t.resourceid=? ");
				param.add(condition.get("examResultsId"));
			}
			sql.append(" order by t.examsubid,s.studyno ");
		} else {
			sql.append("select t.resourceid,t.studentid,t.writtenscore,t.usuallyscore,t.integratedscore,t.examabnormity,");
			sql.append(" a.resourceid resultAuditId,a.changedwrittenscore,a.changedusuallyscore,a.changedintegratedscore,a.changedexamabnormity,  ");
			sql.append(" t.examsubid,sub.batchname,t.courseid,c.coursename,s.studyno,s.studentname,t.plancourseteachtype teachType ,i.ismixture as isMixTrue,t.writtenhandworkscore,pc.examClassType");
			sql.append(" from edu_teach_examresults t ");
			sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid ");
			sql.append(" join edu_teach_examsub sub on t.examsubid=sub.resourceid ");		
			sql.append(" join edu_base_course c on t.courseid=c.resourceid");
			sql.append(" join edu_teach_examinfo i on t.examinfoid = i.resourceid");
			sql.append(" join edu_teach_plancourse pc on pc.resourceid = t.majorcourseid ");
			sql.append(" left join edu_teach_auditresults a on (a.resultsid=t.resourceid and a.auditType=1) ");
			sql.append(" where t.isdeleted=? and t.checkstatus='4' ");
			param.add(0);
			if(condition.containsKey("examSubId")){
				sql.append(" and t.examsubid=? ");
				param.add(condition.get("examSubId"));
			}
			if(condition.containsKey("courseId")){
				sql.append(" and t.courseid=? ");
				param.add(condition.get("courseId"));
			}
			if(condition.containsKey("studyNo")){
				sql.append(" and s.studyno =? ");
				param.add(condition.get("studyNo"));
			}
			if(condition.containsKey("studentName")){
				sql.append(" and s.studentname like ? ");
				param.add("%"+condition.get("studentName")+"%");
			}
			if(condition.containsKey("teachType")){
				sql.append(" and t.plancourseteachtype =? ");
				param.add(condition.get("teachType"));
			}
			if(condition.containsKey("examClassType")){
				sql.append(" and pc.examclasstype=? ");
				param.add(condition.get("examClassType"));
			}
			sql.append(" order by t.examsubid,t.courseid,s.studyno ");
		}
		return sql;
	}
	/**
	 * 成绩更正列表查询sql
	 * @param condition
	 * @param param
	 * @return
	 */
	private StringBuffer getAuditChangeExamResultsSqlByCondition(Map<String, Object> condition, List<Object> param) {
		StringBuffer sql = new StringBuffer();
		if(condition.containsKey("batchType") && "thesis".equals(condition.get("batchType"))){//毕业论文
			sql.append("select t.resourceid,t.studentid,p.resourceid paperOrderId,p.firstscore,p.secondscore,t.integratedscore,t.examabnormity,");
			sql.append(" t.examsubid,sub.batchname,s.studyno,s.studentname,classes.classesname");
			sql.append(" ,g.gradename,ci.classicname,m.majorname from edu_teach_examresults t ");
			sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid ");
			sql.append(" join edu_base_major m on m.resourceid=s.majorid ");
			sql.append(" join edu_base_grade g on g.resourceid=s.gradeid ");
			sql.append(" join edu_base_classic ci on ci.resourceid=s.classicid ");
			sql.append(" join edu_roll_classes classes on s.classesid=classes.resourceid ");
			sql.append(" join edu_teach_examsub sub on t.examsubid=sub.resourceid ");
			sql.append(" left join edu_teach_papersorder p on p.isdeleted=0 and p.status=1 and p.studentid=t.studentid and p.examsubid=t.examsubid ");
			sql.append(" where t.isdeleted=0 and t.checkstatus='4' ");
			if(condition.containsKey("examResultsId")){//成绩id
				sql.append(" and t.resourceid=? ");
				param.add(condition.get("examResultsId"));
			}
			
			//sql.append(" order by t.examsubid,s.studyno ");
		} else {
			sql.append("select t.resourceid,t.studentid,t.writtenscore,t.usuallyscore,t.integratedscore,t.examabnormity,");
			sql.append(" t.examsubid,sub.batchname,t.courseid,c.coursename,s.studyno,s.studentname,t.plancourseteachtype teachType ,i.ismixture as isMixTrue,t.writtenhandworkscore,classes.classesname");
			sql.append(" ,g.gradename,ci.classicname,m.majorname,pc.examclasstype from edu_teach_examresults t ");
			sql.append(" join edu_roll_studentinfo s on t.studentid=s.resourceid ");
			sql.append(" join edu_base_major m on m.resourceid=s.majorid ");
			sql.append(" join edu_base_grade g on g.resourceid=s.gradeid ");
			sql.append(" join edu_base_classic ci on ci.resourceid=s.classicid ");
			sql.append(" join edu_roll_classes classes on s.classesid=classes.resourceid ");
			sql.append(" join edu_teach_examsub sub on t.examsubid=sub.resourceid ");		
			sql.append(" join edu_base_course c on t.courseid=c.resourceid");
			sql.append(" join edu_teach_examinfo i on t.examinfoid = i.resourceid");
			sql.append(" join edu_teach_plancourse pc on pc.resourceid = t.majorcourseid ");
			sql.append(" where t.isdeleted=0 and t.checkstatus='4' ");
			
			if(condition.containsKey("teachType")){
				sql.append(" and t.plancourseteachtype =? ");
				param.add(condition.get("teachType"));
			}
			//sql.append(" order by t.examsubid,t.courseid,s.studyno ");
		}
		if(condition.containsKey("examSubId")){
			sql.append(" and t.examsubid=? ");
			param.add(condition.get("examSubId"));
		}
		if(condition.containsKey("brSchoolId")){//教学点
			sql.append(" and s.branchschoolid=? ");
			param.add(condition.get("brSchoolId"));
		}
		if(condition.containsKey("gradeId")){//年级
			sql.append(" and s.gradeid=? ");
			param.add(condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){//层次
			sql.append(" and s.classicId=? ");
			param.add(condition.get("classicId"));
		}
		if(condition.containsKey("teachingType")){//学习形式
			sql.append(" and s.teachingType=? ");
			param.add(condition.get("teachingType"));
		}
		if(condition.containsKey("majorId")){//专业
			sql.append(" and s.majorid=? ");
			param.add(condition.get("majorId"));
		}
		if(condition.containsKey("classId")){//班级
			sql.append(" and classes.resourceid=? ");
			param.add(condition.get("classId"));
		}
		if(condition.containsKey("courseId")){
			sql.append(" and t.courseid=? ");
			param.add(condition.get("courseId"));
		}
		if(condition.containsKey("studyNo")){
			sql.append(" and s.studyno like ? ");
			param.add("%" + condition.get("studyNo").toString().trim() + "%");
		}
		if(condition.containsKey("studentName")){
			sql.append(" and s.studentname like ? ");
			param.add("%"+condition.get("studentName")+"%");
		}
		if(condition.containsKey("examClassType")){
			sql.append(" and pc.examclasstype=? ");
			param.add(condition.get("examClassType"));
		}
		return sql;
	}
	/**
	 * 卷面成绩在线录入保存
	 * @param resultsIds        成绩ID
	 * @param integratedScores  提交的综合成绩
	 * @param writtenScores     提交的卷面成绩
	 * @param examAbnormitys    提交的成绩异常状态
	 * @param writtenHandworks  考试科目为混合机考时的笔考成绩
	 * @param examCountHQL      查询选考次数的HQL
	 * @param curUserName       当前用户名
	 * @param curUserId         当前用户ID
	 * @param curTime           当前时间
	 * @param isAbnormityInput  是否是异常成绩录入用户
	 */
	@Override
	public String examResultsInputSave(String[] resultsIds,String[] integratedScores,
			String[] writtenScores, String[] examAbnormitys,
			String[] writtenHandworks, String examCountHQL, String curUserName,
			String curUserId, Date curTime, boolean isAbnormityInput)
			throws ServiceException {
		
		List<ExamResults> resultList = new ArrayList<ExamResults>();     
		BigDecimal divisor   		 = new BigDecimal("1");
		StringBuffer returnMsg       = new StringBuffer();
		Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
		
		for (int i = 0; i < resultsIds.length; i++) {
			
			ExamResults examResults = get(resultsIds[i]);//要保存的成绩
			ExamInfo info 			= examResults.getExamInfo();
			Long examType           = null==examResults.getCourse().getExamType()?0:examResults.getCourse().getExamType();
			boolean isMixTrue       = false;
			boolean isNeedSave      = false;
			String examAbnormity_old= ExStringUtils.trimToEmpty(examResults.getExamAbnormity());
			String writtenScore_old = ExStringUtils.trimToEmpty(examResults.getWrittenScore());
			String handworkScore_old= ExStringUtils.trimToEmpty(examResults.getWrittenHandworkScore());
			String integratedScore_old=ExStringUtils.trimToEmpty(examResults.getIntegratedScore());
			//是否混合机考课程
			if (null!=info&&null!=info.getIsmixture()) {
				isMixTrue           = null==info.getIsmixture()?false:Constants.BOOLEAN_YES.equals(info.getIsmixture());
			}
			
			List examCountList      = findByHql(examCountHQL,examResults.getCourse().getResourceid(),
																   examResults.getStudentInfo().getResourceid(),examResults.getResourceid());
			long examCount          = (Long)examCountList.get(0);			         //选考次数
			
			String writtenScore     = "";
			if(null!=writtenScores) {
				writtenScore        = ExStringUtils.trimToEmpty(writtenScores[i]);   //卷面成绩
			}
			
			String examAbnormity    = "";
			if(null!=examAbnormitys) {
				examAbnormity       = examAbnormitys[i];                              //成绩异常
			}
			
				                 
			String writtenHandwork  = "";
			if(null!=writtenHandworks) {
				writtenHandwork	  = ExStringUtils.trimToEmpty(writtenHandworks[i]); //混合机考笔考部份成绩
			}
			
			String integratedScore  = "";
			if(null!=integratedScores) {
				integratedScore     = ExStringUtils.trimToEmpty(integratedScores[i]); //综合成绩
			}
			//成绩已保存的不进行保存操作
			if(Integer.valueOf(examResults.getCheckStatus())>0) {
				continue;
			}
			
			//录入异常成绩
			if (ExStringUtils.isNotBlank(examAbnormity)&& !"0".equals(ExStringUtils.trimToEmpty(examAbnormity))) {
				
				examResults.setExamAbnormity(examAbnormity);
				if(!"0".equals(examAbnormity)){
					examResults.setWrittenScore("");
					//examResults.setUsuallyScore("");
					examResults.setIntegratedScore("");
				} 
				isNeedSave          = true;
				if (examAbnormity_old.equals(examAbnormity)) {
					isNeedSave      = false; //如果提交的状态未更改，则不更新数据库
				}
				
			//录入正常成绩(混合机考笔考部份，普通考试卷面成绩)	
			}else {
				if(!examAbnormity_old.equals(examAbnormity)){
					examResults.setExamAbnormity(examAbnormity);
					isNeedSave = true;
				}
				if (ExStringUtils.isBlank(writtenScore)&&false==isMixTrue){   if(isNeedSave){resultList.add(examResults);} continue;}
				if (ExStringUtils.isBlank(writtenHandwork)&&true==isMixTrue){ if(isNeedSave){resultList.add(examResults);} continue;}
				
				if (ExStringUtils.isNotBlank(examAbnormity)) {
					examResults.setExamAbnormity(examAbnormity);
				}
				//混合机考课程卷面成绩等于机考部份加笔考部份
				if (isMixTrue) {
					if (null==info.getMixtrueScorePer()) {
						throw new ServiceException(info.getCourse().getCourseName()+"未设置笔考成绩比例！");
					}
					try {
						if(ExStringUtils.isNotBlank(writtenHandwork)&&(Double.valueOf(writtenHandwork)<0||Double.valueOf(writtenHandwork)>info.getMixtrueScorePer())){
							returnMsg.append(examResults.getStudentInfo().getStudentName()+"的笔考成绩必须为0-"+info.getMixtrueScorePer().intValue()+"的数字</br>");
							continue;
						}
					} catch (Exception e) {
						continue;
					}
					
					writtenScore         = ExStringUtils.defaultIfEmpty(examResults.getWrittenMachineScore(), "0");
					BigDecimal wh_Score	 = new BigDecimal(writtenHandwork);
					BigDecimal w_Score	 = new BigDecimal(writtenScore);
					
					//混合机考课程卷面成绩字段存的是：机考+笔考
					if (null==examResults.getWrittenHandworkScore()|| 
					   (null!=examResults.getWrittenHandworkScore() && !Double.valueOf(examResults.getWrittenHandworkScore()).equals(wh_Score.longValue()))) {
						examResults.setWrittenScore((wh_Score.add(w_Score).divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					}
					examResults.setWrittenHandworkScore((wh_Score.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					
					isNeedSave          = true;
					if (handworkScore_old.equals(writtenHandwork)) {
						isNeedSave      = false; //如果提交的笔考成绩更改，则不更新数据库
					}
					
				//其它考试类型的课程卷面成绩等于录入的成绩
				}else {
					
					Matcher m = pattern.matcher(writtenScore);
					if (Boolean.FALSE == m.matches()) {
						returnMsg.append(examResults.getStudentInfo().getStudentName()+"的卷面成绩应为0-100的数字</br>");
						continue;
					}
					/*try {
						if(ExStringUtils.isNotBlank(writtenScore)&&(Double.valueOf(writtenScore)<0||Double.valueOf(writtenScore)>100)){
							returnMsg.append(examResults.getStudentInfo().getStudentName()+"的笔考成绩必须为0-100的数字</br>");
							continue;
						}
					} catch (Exception e) {
						continue;
					}*/
					BigDecimal w_Score	 = new BigDecimal(writtenScore);
					examResults.setWrittenScore((w_Score.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
					isNeedSave          = true;
					
					if (writtenScore_old.equals(writtenScore)) {
						isNeedSave      		= false; //如果提交的卷面成绩未更改，则不更新数据库
					}
				}
				
			}
			if(!integratedScore_old.equals(integratedScore)){
				isNeedSave = true;
			}
			examResults.setIntegratedScore(integratedScore);
			if (isNeedSave) {
				
				examResults.setFillinDate(curTime);
				examResults.setExamCount(examCount+1);
				
				examResults.setCheckStatus("0");
				examResults.setFillinMan(curUserName);
				examResults.setFillinManId(curUserId);
				
				resultList.add(examResults);
			}
		}
		this.batchSaveOrUpdate(resultList);
		
		return returnMsg.toString();
	}
	
	/**
	 * 平时成绩在线录入保存
	 */
	@Override
	public String usExamResultsInputSave(String[] resultsIds,String curUserName,String[] usuallyScores,String curUserId, Date curTime)throws ServiceException {
		
		List<ExamResults> resultList = new ArrayList<ExamResults>();     
		BigDecimal divisor   		 = new BigDecimal("1");
		StringBuffer returnMsg       = new StringBuffer();
		Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");

		ExamResults examResults;
		for (int i = 0; i < resultsIds.length; i++) {
			
			examResults = get(resultsIds[i]);//要保存的成绩
			ExamInfo info 			= examResults.getExamInfo();
			Long examType           = null==examResults.getCourse().getExamType()?0:examResults.getCourse().getExamType();

			//成绩已保存的不进行保存操作
			if(null==examResults.getUsCheckStatus()) {
				examResults.setUsCheckStatus("0");
			}
			if(null==examResults.getCheckStatus()) {
				examResults.setCheckStatus("0");
			}
			if(Integer.valueOf(examResults.getUsCheckStatus())>0||Integer.valueOf(examResults.getCheckStatus())>0) {
				continue;
			}
			String usuallyScore = usuallyScores[i];
			examResults.setUsuallyScore(usuallyScore);
			examResults.setFillinDate(curTime);
			examResults.setUsCheckStatus("0");
			examResults.setUsFillinMan(curUserName);
			examResults.setUsFillinManId(curUserId);
			resultList.add(examResults);
			
		}
		this.batchSaveOrUpdate(resultList);
		
		return returnMsg.toString();
	}
	
	/**
	 * 非统考课程补考学生统计
	 */
	@Override
	public  List failExamStudentList(Map<String, Object> condition)throws ServiceException {
		Map<String, Object> para =new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(1000);
//			hql.append(" select distinct pc.resourceid, c.coursename,stu.classesid,es.classesname,count(re.resourceid) counts,pc.term coursestatusterm from edu_teach_examresults re ");
//			hql.append(" join edu_roll_studentinfo stu on stu.resourceid  = re.studentid  and  (stu.studentstatus = '11' or stu.studentstatus = '21') ");
//			hql.append(" join edu_base_course c on re.courseid = c.resourceid ");
//			hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
//			hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
//			hql.append("  join edu_roll_classes es on stu.classesid = es.resourceid ");
//			
//			//后加
////			hql.append(" join edu_learn_stuplan stuplan on stuplan.examresultsid = re.resourceid and stuplan.isPass='N'");
//			
//			//hql.append("  left join edu_teach_plancourse pc on stu.teachplanid = pc.planid and re.courseid = pc.courseid  and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' ");
//			hql.append(" join edu_teach_plancourse pc on stu.teachplanid = pc.planid and re.courseid = pc.courseid  and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' ");
//			hql.append(" where stu.isDeleted = 0 and re.isDeleted=0");
//			//判断需要补考的学生，20130829 
////			hql.append(" and ( ( f_decrypt_score2( re.integratedscore,stu.resourceid) < 60 or re.examabnormity = 2 or re.examabnormity = 3 or re.examabnormity = 4 ) or re.examcount >=2 )");
//			hql.append(" and ( ( f_decrypt_score( re.integratedscore,stu.resourceid) < 60 or re.examabnormity = 2 or re.examabnormity = 3 or re.examabnormity = 4 ))");
//			hql.append(" and not exists(select re.courseid from edu_teach_noexam ne where ne.isdeleted=0 and re.studentid = ne.studentid and re.courseid = ne.courseid and ne.checkstatus='1')");
		
		//2014-5-4
		hql.append("select distinct pc.resourceid, c.coursename,stu.classesid,es.classesname,count(sp.resourceid) counts,pc.term coursestatusterm from edu_learn_stuplan sp")
		   .append(" join edu_roll_studentinfo stu on stu.resourceid  = sp.studentid  and  (stu.studentstatus = '11' or stu.studentstatus = '21' or stu.studentstatus = '25')")
		   .append(" join edu_teach_plancourse pc on sp.plansourceid=pc.resourceid join edu_teach_plan pl on pl.resourceid=stu.teachplanid")
		   .append(" join edu_base_course c on pc.courseid = c.resourceid  join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid")
		   .append(" join edu_base_major major on stu.majorid = major.resourceid   join edu_roll_classes es on stu.classesid = es.resourceid")
		   .append(" where sp.isdeleted='0' and stu.isdeleted = 0 and sp.ispass='N' and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' and pl.isdeleted=0")
		   .append(" and not exists(select pc.courseid from edu_teach_noexam ne where sp.studentid = ne.studentid and pc.courseid = ne.courseid and ne.checkstatus='1')");
		
//		if (condition.containsKey("examSubId")) {
//			hql.append("  and re.examsubid=:examSubId");
//			para.put("examSubId", condition.get("examSubId"));
//		}
//	 if (condition.containsKey("examSubId")) {
//			hql.append(" and re.fillindate = (select max(res.fillindate) from edu_teach_examresults res where res.isdeleted = 0 and res.examsubid<>:examSubId and stu.resourceid=res.studentid and pc.courseid=res.courseid)");
//			 para.put("examSubId", condition.get("examSubId"));
//		}
		if (condition.containsKey("gradeId")) {
			 hql.append("  and stu.gradeId=:gradeId");
			 para.put("gradeId", condition.get("gradeId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append("  and stu.branchschoolid=:unitId");
			para.put("unitId", condition.get("branchSchool"));
		}
		if (condition.containsKey("major")) {
			 hql.append("  and stu.majorid =:major");
			 para.put("major", condition.get("major"));
		}
		if (condition.containsKey("courseName")) {
			 hql.append("  and lower(c.courseName) like:courseName ");
			 para.put("courseName", "%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if (condition.containsKey("classesId")) {
			 hql.append("  and stu.classesId =:classesId");
			 para.put("classesId", condition.get("classesId"));
		}
		
		hql.append(" group by c.coursename ,es.classesname,stu.classesid,pc.term,pc.resourceid");
//		hql.append(" order by c.coursename "); group by c.coursename ,pc.term
		
		List list = new ArrayList();
		try {
			 list =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), para) ;
		} catch (Exception e) {
		}	
		
		return list;
	}
	
	/**
	 * 非统考课程补考学生统计---------old
	 */
	@Override
	public Page failExamStudentList(Page page, Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> para =new HashMap<String, Object>();
		
//		List<Object> para = new ArrayList<Object>();
		/*
		hql.append("select distinct pc.resourceid, c.coursename,stu.classesid,es.classesname,count(sp.resourceid) counts,pc.term coursestatusterm from edu_learn_stuplan sp")
		   .append(" join edu_roll_studentinfo stu on stu.resourceid  = sp.studentid  and  (stu.studentstatus = '11' or stu.studentstatus = '21')")
		   .append(" join edu_teach_plancourse pc on sp.plansourceid=pc.resourceid join edu_teach_plan pl on pl.resourceid=pc.planid and pl.resourceid=stu.teachplanid")
		   .append(" join edu_base_course c on pc.courseid = c.resourceid  join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid")
		   .append(" join edu_base_major major on stu.majorid = major.resourceid   join edu_roll_classes es on stu.classesid = es.resourceid")
//		   .append(" join edu_teach_examresults re on sp.examresultsid  = re.resourceid")
		   .append(" where sp.isdeleted='0' and stu.isdeleted = 0 and sp.ispass='N' and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' and pl.isdeleted=0")
		   .append(" and not exists(select pc.courseid from edu_teach_noexam ne where ne.isdeleted = '0' and sp.studentid = ne.studentid and pc.courseid = ne.courseid and ne.checkstatus='1')");
		
//		if (condition.containsKey("examSubId")) {
//			hql.append("  and re.examsubid=:examSubId");
//			para.put("examSubId", condition.get("examSubId"));
//		}
//		if (condition.containsKey("examSubId")) {
//			hql.append(" and re.fillindate = (select max(res.fillindate) from edu_teach_examresults res where res.isdeleted = 0 and res.examsubid<>:examSubId and stu.resourceid=res.studentid and pc.courseid=res.courseid)");
//			 para.put("examSubId", condition.get("examSubId"));
//		}
		if (condition.containsKey("gradeId")) {
			 hql.append("  and stu.gradeId=:gradeId");
			 para.put("gradeId",condition.get("gradeId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append("  and stu.branchschoolid=:unitId");
			para.put("unitId",condition.get("branchSchool"));
		}
		if (condition.containsKey("major")) {
			 hql.append("  and stu.majorid =:major");
			 para.put("major",condition.get("major"));
		}
		if (condition.containsKey("courseName")) {
			 hql.append("  and lower(c.courseName) like:courseName ");
			 para.put("courseName","%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if (condition.containsKey("classesId")) {
			 hql.append("  and stu.classesId =:classesId");
			 para.put("classesId",condition.get("classesId"));
		}
		
		hql.append(" group by c.coursename ,es.classesname,stu.classesid,pc.term,pc.resourceid");
		
		return findBySql(page, hql.toString(), para);	        
		*/
		
//		hql.append("select count(es.classesname) counts, c.coursename,unit.unitname,major.majorname,es.classesname");
		hql.append("select distinct c.coursename,unit.unitname,major.majorname,es.classesname,stu.teachplanid,max(s.term) courseterm,max(s.resourceid) coursestatusid");
		hql.append(",stu.gradeid,stu.classesid,major.resourceid majorid,unit.resourceid unitid,c.resourceid courseid,a.plansourceid");
//		hql.append(",(select s.term from edu_teach_coursestatus s join edu_teach_guiplan gp on s.guiplanid=gp.resourceid where s.plancourseid=tp.resourceid and s.isdeleted=0) courseterm");
		hql.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid ");
		hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
		hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
		hql.append(" join edu_base_course c on a.courseid = c.resourceid ");
		hql.append(" join edu_roll_classes es on stu.classesid = es.resourceid ");
		hql.append(" join edu_base_grade g on g.resourceid = stu.gradeid ");
//		hql.append(" left join edu_teach_examresults re on a.resultsid  = re.resourceid ");
		hql.append(" left join edu_teach_plancourse tp on tp.resourceid = a.plansourceid ");
//		hql.append(" left join edu_teach_examsub sub on sub.resourceid=re.examsubid ");
		hql.append(" join edu_teach_guiplan gp on gp.planid=stu.teachplanid and gp.gradeid=stu.gradeid and gp.isdeleted=0 ");
		hql.append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=tp.resourceid and s.schoolIds=stu.branchschoolid and s.isdeleted=0 ");

		if (condition.containsKey("jwyId")) {// 教务员录入自己负责的教学站班级的课程成绩
			hql.append(" inner join hnjk_sys_users users on users.unitid=unit.resourceid and users.resourceid=:jwyId ");
			para.put("jwyId", condition.get("jwyId"));
		}
		
		if(condition.containsKey("teachId")){// 登分老师录入自己负责的班级的课程成绩
			hql.append(" inner join edu_teach_courseteachercl ctl on ctl.CLASSESID = es.resourceid and ctl.isdeleted= 0 and c.resourceid = ctl.COURSEID and ctl.TEACHERID = :teachId ");
			para.put("teachId", condition.get("teachId"));
		}
		
		hql.append(" where stu.isdeleted=0 and (stu.studentstatus='11' or stu.studentstatus='21' or stu.studentstatus='25') and a.isdeleted=0 ");
//		hql.append(" and sub.isdeleted=0 ");
		
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
		
		hql.append(" group by unit.unitname,c.coursename ,major.majorname,es.classesname,stu.gradeid,stu.classesid,major.resourceid,unit.resourceid");
		hql.append(",c.resourceid,a.plansourceid,a.resourceid,stu.teachplanid");//,s.term
		hql.append(" order by unit.unitname,major.majorname,c.coursename ,es.classesname");
		
		List results = findBySql(page, hql.toString(), para).getResult();
		if(results != null){
			Map result;
			List resutList;
			StringBuilder sql;
			Map param;
			for (int i = 0; i < results.size(); i++) {
				result = (Map) results.get(i);
//				String sql = "select count(a.resourceid) counts from edu_teach_makeuplist a "
				sql = new StringBuilder("select count(cc.resourceid) counts from (select max(a.resourceid) resourceid from edu_teach_makeuplist a ");
				sql.append("join edu_roll_studentinfo stu ");
				sql.append("  on stu.resourceid = a.studentid ");
				sql.append("join hnjk_sys_unit unit ");
				sql.append("  on stu.branchschoolid = unit.resourceid ");
				sql.append("join edu_base_major major ");
				sql.append("  on stu.majorid = major.resourceid ");
				sql.append("join edu_base_course c ");
				sql.append("  on a.courseid = c.resourceid ");
				sql.append("left join (select * ");
				sql.append("             from edu_teach_examresults re ");
				sql.append("            where re.isdeleted = 0 ");
				sql.append("              and re.examsubid = :examSubId ");
				sql.append("               and re.courseid = :courseid) edumakeup ");
				sql.append("   on stu.resourceid = edumakeup.studentid ");
				sql.append("where a.isdeleted = 0 ");
				sql.append("  and (stu.studentstatus = '11' or stu.studentstatus = '21' or stu.studentstatus = '25') ");
				sql.append(" and (select count(*) ");
				sql.append("        from EDU_TEACH_EXAMRESULTS re ");
				sql.append("       where re.isdeleted = 0 ");
				sql.append("         and re.COURSEID = a.courseid ");
				sql.append("        and re.studentid = a.studentid ");
				sql.append("         and re.checkstatus = '4') < :ksNum ");
				sql.append(" and a.nextexamsubid = :examSubId");
				sql.append(" and c.resourceid = :courseid ");
				sql.append(" and major.resourceid = :majorid ");
				sql.append(" and stu.gradeid = :gradeId ");
				sql.append(" and stu.branchschoolid = :branchschoolid ");
				sql.append(" and stu.classesid = :classesId ");
				sql.append(" group by stu.resourceid, stu.studyno) cc");

				param = new HashMap();
				param.put("examSubId", condition.get("examSubId"));
				param.put("courseid", result.get("courseid"));
				param.put("ksNum", para.get("ksnum"));
				param.put("majorid", result.get("majorid"));
				param.put("gradeId", result.get("gradeid"));
				param.put("branchschoolid", result.get("unitid"));
				param.put("classesId", result.get("classesid"));
				try {
					resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
					Map counts = (Map) resutList.get(0);
					result.put("counts", counts.get("counts"));
					param.clear();
					param.put("ksNum", ksnum+1);
					param.put("examSubId", condition.get("examSubId"));
					param.put("majorid", result.get("majorid"));
					param.put("gradeId", result.get("gradeid"));
					param.put("classesId", result.get("classesid"));
					param.put("branchschoolid", result.get("unitid"));
					param.put("courseid", result.get("courseid"));
					param.put("teachingPlanCourseId", result.get("plansourceid"));
					StringBuffer sqlsb = new StringBuffer();
					sqlsb.append("select r.checkStatusMakeup checkstatus,count(r.checkStatusMakeup) c from (").append(teachingJDBCService.getNonFacestudyExamResultsSql(param))
						.append(") r group by r.checkStatusMakeup");
					List rss = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlsb.toString(), param);
					String checkStatus0 = "0";
					String checkStatus1 = "0";
					String checkStatus4 = "0";
					Map rs;
					for (int j = 0; j < rss.size(); j++) {
						rs = (Map) rss.get(j);
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
					result.put("checkStatus0", checkStatus0);
					result.put("checkStatus1", checkStatus1);
					result.put("checkStatus4", checkStatus4);
					result.put("inputcount", Integer.parseInt(checkStatus0) + Integer.parseInt(checkStatus1) + Integer.parseInt(checkStatus4));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return page;
	}
	
	/**
	 * 非统考课程补考学生统计---------new
	 * 
	 * 20160504 修改查询条件考试批次为年度、学期、考试类型
	 */
	@Override
	public Page failExamStudentListNew(Page objpage, Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> para =new HashMap<String, Object>();
		
		failExamSql(condition, hql, para);
		
		Page page = findBySql(objpage, hql.toString(), para);
		
		return page;
	}
	/**
	 * @param condition
	 * @param hql
	 * @param para
	 * @throws NumberFormatException
	 */
	private void failExamSql(Map<String, Object> condition, StringBuffer hql,
			Map<String, Object> para) throws NumberFormatException {
		hql.append("select * from(select distinct c.coursename,unit.unitname,major.majorname,es.classesname,stu.teachplanid,max(s.term) courseterm,max(s.resourceid) coursestatusid,a.nextexamsubid examSubId,etes.batchname");
		hql.append(",stu.gradeid,stu.classesid,major.resourceid majorid,unit.resourceid unitid,c.resourceid courseid,a.plansourceid,max(tc.counts) counts,etes.examinputstarttime,etes.isReachTime,etes.examtype,s.examform,s.teachType,ctl.teacherName");
		//hql.append("max("+convertSql("sst.resultstatus")+") checkStatus0, max("+convertSql("sbt.resultstatus")+") checkStatus1,max("+convertSql("pst.resultstatus")+") checkStatus4, ");

		hql.append(",count(case when er.CHECKSTATUS='0' then 1 else null end) checkStatus0");
		hql.append(",count(case when er.CHECKSTATUS='1' then 1 else null end) checkStatus1");
		hql.append(",count(case when er.CHECKSTATUS='4' then 1 else null end) checkStatus4");
		//hql.append(",max("+convertSql("sst.resultstatus")+"+"+convertSql("sbt.resultstatus")+"+"+convertSql("pst.resultstatus")+") inputcount ");
		hql.append(",count(case when er.RESOURCEID is not null then 1 else null end) inputcount");
		hql.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid ");
		hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
		hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
		hql.append(" join edu_base_course c on a.courseid = c.resourceid ");
		hql.append(" join edu_roll_classes es on stu.classesid = es.resourceid ");
		hql.append(" join edu_base_grade g on g.resourceid = stu.gradeid ");
		hql.append(" join edu_teach_plancourse tp on tp.resourceid = a.plansourceid ");
		hql.append(" join edu_teach_guiplan gp on gp.planid=stu.teachplanid and gp.gradeid=stu.gradeid ");
		hql.append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=tp.resourceid and s.schoolIds=stu.branchschoolid ");
		hql.append(" left join edu_teach_courseteachercl ctl on ctl.CLASSESID = es.resourceid and ctl.isdeleted= 0 and c.resourceid = ctl.COURSEID and ctl.COURSESTATUSID=s.resourceid ");
		// 状态查询需要
		hql.append(" left join ("+getFailTotalNumSql(condition)+") tc on  tc.countResourceid=a.plansourceid||'_'||stu.classesid||'_'||a.nextexamsubid ");

		//hql.append(" left join ("+getFailCheckStatusNumSql("0")+") sst on sst.majorcourseid=tp.resourceid and sst.courseid=c.resourceid and sst.classesid=es.resourceid and sst.gradeid=g.resourceid and a.nextexamsubid=sst.examsubid ");
		//hql.append(" left join ("+getFailCheckStatusNumSql("1")+") sbt on sbt.majorcourseid=tp.resourceid and sbt.courseid=c.resourceid and sbt.classesid=es.resourceid and sbt.gradeid=g.resourceid and a.nextexamsubid=sbt.examsubid ");
		//hql.append(" left join ("+getFailCheckStatusNumSql("4")+") pst on pst.majorcourseid=tp.resourceid and pst.courseid=c.resourceid and pst.classesid=es.resourceid and pst.gradeid=g.resourceid and a.nextexamsubid=pst.examsubid ");
		hql.append(" left join edu_teach_examresults er on er.examsubid = a.nextexamsubid and er.courseid=c.resourceid and er.studentid = stu.resourceid and er.isdeleted=0");
		hql.append(" left join edu_teach_graduateData gd on gd.studentid = stu.resourceid and gd.isdeleted = 0 ");
		hql.append(" join ( ");
		hql.append(" select sub.batchname,sub.resourceid subid,sub.examtype,sub.term,eby.resourceid yearId,sub.examinputstarttime,case when sub.examinputstarttime<= sysdate then 'Y' else 'N' end as isReachTime from edu_teach_examsub sub "); 
		hql.append(" join edu_base_year eby on eby.resourceid = sub.yearid and eby.isdeleted = 0 ");
		hql.append(" where sub.isdeleted=0 ");
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
		if(condition.containsKey("queryNotAllInput")){
			hql.append(" and sysdate>=sub.examinputstarttime");
		}
		hql.append(" )  etes on etes.subid = a.nextexamsubid ");
		if (condition.containsKey("jwyId")) {// 教务员录入自己负责的教学站班级的课程成绩
			hql.append(" inner join hnjk_sys_users users on users.unitid=unit.resourceid and users.resourceid=:jwyId ");
			para.put("jwyId", condition.get("jwyId"));
		}
		if(condition.containsKey("teachId")){// 登分老师录入自己负责的班级的课程成绩
			hql.append(" and ctl.TEACHERID = :teachId  ");
			para.put("teachId", condition.get("teachId"));
		}
		if (condition.containsKey("teacherName")) {
			hql.append(" and ctl.teacherName = :teacherName");
			para.put("teacherName", condition.get("teacherName"));
		}
		hql.append(" where stu.isdeleted=0 and s.isdeleted=0  and gp.isdeleted=0 and (stu.studentstatus='11' or stu.studentstatus='21'  or stu.studentstatus='25' or (stu.studentstatus='24' and gd.isAllowSecGraduate='Y')) and a.isdeleted=0 ");
		//20181015  因为系统中已经有统考成绩录入，因此在这个成绩录入处进行过滤 2162
		if (condition.containsKey("filterExamClassType")) {//考试类型
			hql.append(" and (tp.examClassType!=:filterExamClassType or tp.examClassType is null) ");
			para.put("filterExamClassType",condition.get("filterExamClassType"));
		}
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
		
		if (condition.containsKey("branchSchool")) {
			hql.append("  and stu.branchschoolid=:unitId");
			para.put("unitId",condition.get("branchSchool"));
		}
		if (condition.containsKey("gradeId")) {
			 hql.append("  and stu.gradeid=:gradeId");
			 para.put("gradeId",condition.get("gradeId"));
		}
		if (condition.containsKey("classicId")) {
			 hql.append("  and stu.classicid=:classicId");
			 para.put("classicId",condition.get("classicId"));
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
		if (condition.containsKey("courseTeachType")) {//课程教学类型
			hql.append("  and s.teachType =:courseTeachType");
			para.put("courseTeachType",condition.get("courseTeachType"));
		}
		
		hql.append(" group by unit.unitname,c.coursename ,major.majorname,es.classesname,stu.gradeid,stu.classesid,major.resourceid,unit.resourceid,etes.examtype,s.examform,s.teachType");
		hql.append(",c.resourceid,a.plansourceid,stu.teachplanid,a.nextexamsubid,etes.examinputstarttime,etes.isReachTime,etes.batchname,ctl.teacherName ");//,s.term
		hql.append(" order by unit.unitname,major.majorname,c.coursename ,es.classesname)");
		// 状态查询
		if(condition.containsKey("failResultStatus")){
			hql.append(" where ");
			String _rs = (String)condition.get("failResultStatus");
			if("partSave".equals(_rs)) {// 部分保存
				hql.append(" ((0<checkStatus0 and checkStatus0<counts) or inputcount=0)");
			} else if("partSubmit".equals(_rs)) {//  部分提交
				hql.append(" (0<checkStatus1 and checkStatus1<counts) ");
			} else if("partPublish".equals(_rs)) {// 部分发布
				hql.append(" (0<checkStatus4 and checkStatus4<counts)");
			} else if("allSave".equals(_rs)) {// 全部保存
				hql.append(" checkStatus0=counts ");
			} else if("allSubmit".equals(_rs)) {// 全部提交
				hql.append(" checkStatus1=counts ");
			} else if("allPublish".equals(_rs)) {// 全部发布
				hql.append(" checkStatus4=counts ");
				//增加未录入
			}else if ("notAllInput".equals(_rs)) {
				hql.append(" inputcount<counts");
			}
		}
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
			  + "  and (so.studentstatus = '11' or so.studentstatus = '21' or so.studentstatus = '25' or (so.studentstatus = '24' and gd.isAllowSecGraduate = 'Y') ) ";
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
				+"AND (sio.studentstatus='11' OR sio.studentstatus='21' OR sio.studentstatus='25' or ( sio.studentstatus = '24' and gd.isAllowSecGraduate = 'Y' )) "
//				+"AND pc1.teachtype='facestudy' "
				+"AND es1.examtype!='N' "
				+"AND er1.checkstatus="+resultStatus
				+" GROUP BY sio.gradeid,sio.classesid,er1.courseid,er1.majorcourseid,sio.teachplanid,er1.examsubid ";
		
		return sql;
		
	}
	
	public Page findBySql(Page page, String sql, Map<String, Object> values) throws ServiceException {
		Assert.notNull(page, "page不能为空");
				
		if (page.isAutoCount()) {
			Long totalCount = countSqlResult(sql.toString(), values);
			page.setTotalCount(totalCount.intValue());
		}
		
		if(page.isOrderBySetted()){
			sql += " order by "+page.getOrderBy()+" "+ page.getOrder()+" ";
		}		
		try {			
			List resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(" select * from (select r.*,rownum rn from ("+sql.toString()+") r where rownum<="+(page.getFirst()+page.getPageSize())+") where rn>"+page.getFirst(), values);
			page.setResult(resutList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return page;
	}	
	
	private Long countSqlResult(String sql, Map<String, Object> condition ){
		String fromSql = sql;
		String countSql = "select count(*) from (" + fromSql+" ) ";
		try {
			Long count = baseSupportJdbcDao.getBaseJdbcTemplate().findForLong(countSql, condition);
			return count;
		} catch (Exception e) {
			throw new RuntimeException("sql can't be auto count, hql is:" + countSql, e);
		}
	}
	
	/**
	 * 非统考课程补考学生成绩导出统计
	 */
	@Override
	public  List failExamStudentResultList(Map<String, Object> condition)throws ServiceException {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> para =new HashMap<String, Object>();
		sql.append(" select unit.unitname, ma.majorname,stu.studyno,stu.studentname,c.coursename,re.writtenscore,re.usuallyscore,stu.resourceid studentid from edu_teach_examresults re  ");
		sql.append(" join edu_roll_studentinfo stu on re.studentid = stu.resourceid ");
		sql.append(" join edu_base_grade g on g.resourceid = stu.gradeid ");
		sql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
		sql.append(" join edu_base_major ma on stu.majorid = ma.resourceid ");
		sql.append(" join edu_base_course c on re.courseid = c.resourceid ");
		sql.append(" where  stu.isDeleted = 0 and stu.sysuserid is not null");
		sql.append(" and c.isuniteexam = 'N' " );
		if(condition.containsKey("gradeId")){
			sql.append(" and g.resourceid =:gradeId ");
			para.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("branchschoolid")){
			sql.append(" and stu.branchschoolid=:branchschoolid "); 
		}
		if(condition.containsKey("major")){
			sql.append("  and ma.resourceid =:major  ");
			para.put("major", condition.get("major"));
		}
		if(condition.containsKey("term")){
			sql.append("  and g.term =:term ");
			para.put("term",  condition.get("term"));
		}
		if(condition.containsKey("courseName")){
			sql.append("and c.coursename like '%"+condition.get("courseName")+"%'");
		}
		if(condition.containsKey("classesId")){
			sql.append(" and stu.classesid  =:classesId");
			para.put("classesId", condition.get("classesId"));
		}
		
		List list = new ArrayList();
		try {
			 list =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), para) ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/***
	 * 补考学生列表
	 * @param condition
	 * @param page
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findFailExamResultStudentList(Page page, Map<String,Object> condition) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		hql = getMakeupListHql(condition, param, hql);
		page    = baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, hql.toString(), param.toArray(),FailExamStudentVo.class);
		return page;
		
	}
	/**
	 * @param condition
	 * @param param
	 * @param hql
	 * @throws NumberFormatException
	 */
	public static StringBuffer getMakeupListHql(Map<String, Object> condition, List<Object> param, StringBuffer hql) throws NumberFormatException {
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if (condition.containsKey("export")) {//导出
			/*temp.spr,*/
			hql.append(" select distinct temp.finalPass,temp.coursename,temp.coursecode,temp.majorname,temp.majorid,temp.studyno,temp.courseid,temp.resourceid,temp.studentname,temp.className,temp.classicname,temp.gradename,temp.term,temp.teacherName,temp.signer,temp.firstyear,temp.unitname");
			//桂林医查询导出新增字段
			if ("10601".equals(schoolCode)) {
				hql.append(",temp.integratedscore,temp.examabnormity,temp.nextScore,temp.lecturerName,temp.startTime");
			}
			hql.append(" from (");
		}
		hql.append(" select distinct sp.resourceid spr,sp.ispass finalPass,c.resourceid courseid,major.resourceid majorid,c.coursename,c.coursecode,unit.unitcode,unit.unitname,major.majorname,stu.studyno,stu.resourceid,stu.studentname,es.classesname className,");
		hql.append(" re.integratedscore,re.examabnormity,a.ispass,cc.classicname,g.gradename ,etes.examType,etes.yearId,etes.firstyear subyear,etes.term subterm,etes.startTime,etc.term,etc.teachType,'' teacherName,'' signer ,etcl.lecturerName,etr1.integratedscore nextScore,etr1.examabnormity nonexamabnormity,y.firstyear ");
		hql.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid ");
		hql.append(" left join edu_teach_graduateData gd on gd.studentid=stu.resourceid and gd.isdeleted=0 ");
		hql.append(" join edu_base_classic cc on stu.classicid = cc.resourceid and cc.isdeleted=0 ");
		hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid and unit.isdeleted=0 ");
		hql.append(" join edu_base_major major on stu.majorid = major.resourceid and major.isdeleted=0 ");
		hql.append(" join edu_base_course c on a.courseid = c.resourceid and c.isdeleted=0 ");
		hql.append(" join edu_roll_classes es on stu.classesid = es.resourceid and es.isdeleted=0 ");
		hql.append(" join edu_base_grade g on g.resourceid = stu.gradeid and g.isdeleted=0 ");
		hql.append(" join edu_base_year y on y.resourceid = g.yearid and g.isdeleted=0 ");
//		hql.append(" left join edu_teach_examresults ete  on ete.examsubid = a.nextexamsubid and ete.isdeleted = 0 and ete.studentid = stu.resourceid and ete.courseid = c.resourceid  " );
		hql.append(" join edu_teach_guiplan gp on gp.gradeid=stu.gradeid and gp.planid=stu.teachplanid and gp.isdeleted=0");//20180509,补考名单重复，需关联年级教学计划
		hql.append(" left join edu_teach_coursestatus etc on etc.isopen = 'Y' and etc.plancourseid = a.plansourceid and etc.schoolids = unit.resourceid and etc.guiplanid=gp.resourceid and etc.isdeleted = 0 ");
		hql.append(" left join EDU_TEACH_COURSETEACHERCL etcl on etcl.classesid = es.resourceid and etcl.courseid = c.resourceid  and etcl.coursestatusid = etc.resourceid ");//由于学籍异动的关系，使用left join
		hql.append(" join edu_teach_plancourse pc on pc.resourceid = a.plansourceid and pc.isdeleted=0 ");
		hql.append(" join edu_learn_stuplan sp on sp.studentid=a.studentid and sp.plansourceid=pc.resourceid and sp.isdeleted=0 ");
		hql.append(" left join edu_teach_examresults re on a.resultsid  = re.resourceid and re.isdeleted=0 ");
		hql.append(" left join edu_teach_examresults etr1  on  etr1.examsubid  = a.nextexamsubid and etr1.studentid = a.studentid and etr1.courseid = a.courseid and etr1.isdeleted = 0 ");
		hql.append(" join ( ");
		hql.append(" select sub.resourceid subid,sub.examtype,sub.term,eby.resourceid yearId,eby.firstyear,sub.startTime from edu_teach_examsub sub ");
		hql.append(" join edu_base_year eby on eby.resourceid = sub.yearid and eby.isdeleted = 0 ");
		
		hql.append(" where sub.isdeleted = 0 ");
		if (condition.containsKey("yearId")) {//年度
			hql.append(" and sub.yearId=:yearId ");
			param.add(condition.get("yearId"));
		}
		if (condition.containsKey("term")) {//学期
			hql.append(" and sub.term=:term ");
			param.add(condition.get("term"));
		}
		if (condition.containsKey("examType")) {//考试类型
			hql.append(" and sub.examType=:examType ");
			param.add(condition.get("examType"));
		}

//		hql.append(" join edu_teach_examsub sub on sub.resourceid=re.examsubid ");
		hql.append(" )  etes on etes.subid = a.nextexamsubid and etes.subid!=re.examsubid");
		hql.append(" where stu.isdeleted = 0 and (stu.studentstatus='11' or stu.studentstatus='21' or stu.studentstatus='25' or (stu.studentstatus='24' and gd.isAllowSecGraduate='Y'))");
		hql.append(" and a.isdeleted = 0  ");
	
		if (condition.containsKey("examSubId")) {
			hql.append(" and a.nextexamsubid=:examSubId");
			param.add(condition.get("examSubId"));
		}
		
		if (condition.containsKey("classic")) {
			hql.append(" and stu.classicid=:classic");
			param.add(condition.get("classic"));
		}
		if (condition.containsKey("courseTeachType")) {//课程教学类型
			hql.append(" and etc.teachType=:courseTeachType");
			param.add(condition.get("courseTeachType"));
		}
		String examResultsTimes=null;
		if(CacheAppManager.getSysConfigurationByCode("examResultsTimes")!=null){
			examResultsTimes=CacheAppManager.getSysConfigurationByCode("examResultsTimes").getParamValue();
		}
        if(examResultsTimes!=null){
			int ksNum=  Integer.parseInt(examResultsTimes);
			ksNum+=1;
			hql.append(" and  (select count(*) from EDU_TEACH_EXAMRESULTS re " +
        			"where re.isdeleted = 0 and re.COURSEID = a.courseid " +
        			"and re.studentid = a.studentid and re.checkstatus='4')<:ksNum "); 
        	param.add(ksNum);
		}
        //结补不及格课程门数
        String finalExamFailNum=null;
		if(CacheAppManager.getSysConfigurationByCode("finalExamFailNum")!=null){
			finalExamFailNum=CacheAppManager.getSysConfigurationByCode("finalExamFailNum").getParamValue();
		}
		if(finalExamFailNum!=null && condition.containsKey("examType") && "Q".equals(condition.get("examType"))){
			int failNum=  Integer.parseInt(finalExamFailNum);
			failNum += 1;
			hql.append(" and (select count(*) from edu_teach_makeuplist ml where ml.studentid=stu.resourceid and ml.isdeleted=0 and ml.ispass!='Y' and ml.nextexamsubid=etes.subid)<:failNum ");
			param.add(failNum);
		}
		if (condition.containsKey("gradeId")) {
			 hql.append(" and stu.gradeid=:gradeId");
			 param.add(condition.get("gradeId"));
		}
		if (condition.containsKey("branchSchool")) {
			hql.append(" and stu.branchschoolid=:branchSchool");
			param.add(condition.get("branchSchool"));
		}else if (condition.containsKey("unitId")) {
			hql.append(" and stu.branchschoolid=:unitId");
			 param.add(condition.get("unitId"));
		}
		if (condition.containsKey("major")) {
			 hql.append(" and stu.majorid =:major");
			 param.add(condition.get("major"));
		}
		if (condition.containsKey("courseName")) {
			 hql.append(" and lower(c.coursename) like:courseName ");
			 param.add("%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if (condition.containsKey("courseId")) {
			hql.append(" and a.courseId=:courseId ");
			param.add(condition.get("courseId"));
		}
		if (condition.containsKey("isPass")) {
			if("Y".equals(condition.get("isPass"))){
				hql.append(" and a.isPass=:isPass ");
			}else {
				hql.append(" and (a.isPass=:isPass or a.isPass is null) ");
			}
			param.add(condition.get("isPass"));
		}
		if (condition.containsKey("finalPass")) {
			hql.append(" and sp.isPass=:finalPass ");
			param.add(condition.get("finalPass"));
		}
		if (condition.containsKey("classesId")) {
			 hql.append(" and stu.classesid =:classesId");
			 param.add(condition.get("classesId"));
		}
		if (condition.containsKey("studyNo")) {
			hql.append(" and stu.studyNo =:studyNo");
			param.add(condition.get("studyNo"));
		}
		if(condition.containsKey("classesMasterId")){
			hql.append(" and es.classesMasterId=:classesMasterId ");
			param.add(condition.get("classesMasterId"));
		}
		
		hql.append(" order by unit.unitcode,g.gradename,major.resourceid,stu.studyno,etc.term,c.coursecode,etes.firstyear,etes.term");
		
		if (condition.containsKey("export")) {//导出 2018-8-31添加课程编码排序（桂林医）
			hql.append(" ) temp order by temp.gradename,temp.classicname,temp.majorname,temp.className");
			if ("10601".equals(schoolCode)) {//桂林医，根据班级和课程导出
				hql.append(",temp.coursecode");
			}
			hql.append(",temp.studyno,temp.term");
		}
		return hql;
	}
	
	/***
	 * 非统考课程补考学生导出
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<FailExamStudentVo> findFailExamResultStudentExport(Map<String,Object> condition)
			throws ServiceException {
		List<Object> param = new ArrayList<Object>();
//		Map<String,Object> param1 = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer();
		hql.append(" select c.coursename,unit.unitname,major.majorname,stu.studyno,stu.studentname from edu_teach_examresults re ");
		hql.append(" join edu_roll_studentinfo stu on stu.resourceid  = re.studentid  and  (stu.studentstatus = '11' or stu.studentstatus = '21' or stu.studentstatus = '25') ");
		hql.append(" join edu_base_course c on re.courseid = c.resourceid ");
		hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
		hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
//		hql.append("  left join edu_teach_plancourse pc on stu.teachplanid = pc.planid and re.courseid = pc.courseid  and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' ");
		hql.append("  join edu_teach_plancourse pc on stu.teachplanid = pc.planid and re.courseid = pc.courseid  and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' ");
		hql.append(" where stu.isDeleted = 0 and re.isDeleted = 0");
		//判断没有通过考试的学生，20130829
		hql.append(" and ( f_decrypt_score( re.integratedscore,stu.resourceid) < 60 or re.examabnormity = 2 or re.examabnormity = 3 or re.examabnormity = 4 )");
		hql.append("   and not exists(select re.courseid from edu_teach_noexam ne where re.studentid = ne.studentid and re.courseid = ne.courseid and ne.checkstatus='1')");//==2-28
	if (condition.containsKey("examSubId")) {
		hql.append("  and re.examsubid=:examSubId");
		param.add(condition.get("examSubId"));
	}
	if (condition.containsKey("gradeId")) {
		 hql.append("  and stu.gradeId=:gradeId");
		 param.add(condition.get("gradeId"));
		 
	}
	if (condition.containsKey("brshSchool")) {
		hql.append("  and stu.branchschoolid=:brshSchool");
		 param.add(condition.get("brshSchool"));
	}
	if (condition.containsKey("major")) {
		 hql.append("  and stu.majorid =:major");
		 param.add(condition.get("major"));
	}
	if (condition.containsKey("courseName")) {
		 hql.append("  and lower(c.courseName) like:courseName ");
		 param.add("%"+condition.get("courseName").toString().toLowerCase()+"%");
	}
	if (condition.containsKey("classesId")) {
		 hql.append("  and stu.classesId =:classesId");
		 param.add(condition.get("classesId"));
	}
	
	hql.append(" order by unit.unitname ,major.majorname,c.coursename ,stu.studyNo desc ");
		
		List<FailExamStudentVo> list = new ArrayList<FailExamStudentVo>();
		
		 try {
			 list = baseSupportJdbcDao.getBaseJdbcTemplate().findList( hql.toString(), param.toArray(), FailExamStudentVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return  list;
		
	}
	
	/**
	 * 有补考学生的课程列表
	 */
	public Page findFailCourseList(Page page, Map<String,Object> condition) throws ServiceException {
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
			hql.append(" select distinct c.coursename,g.term term from edu_teach_examresults re ");
			hql.append(" join edu_roll_studentinfo stu on stu.resourceid  = re.studentid  and  (stu.studentstatus = '11' or stu.studentstatus = '21' or stu.studentstatus = '25') ");
			hql.append(" join edu_base_course c on re.courseid = c.resourceid ");
			hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
			hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
			hql.append("  join edu_roll_classes es on stu.classesid = es.resourceid ");
			hql.append("  left join edu_teach_plancourse pc on stu.teachplanid = pc.planid and re.courseid = pc.courseid  and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' ");
			hql.append(" where stu.isDeleted = 0  ");
			//判断需要补考的学生，20130829 
			hql.append(" and ( ( f_decrypt_score( re.integratedscore,stu.resourceid) < 60 or re.examabnormity = 2 or re.examabnormity = 3 or re.examabnormity = 4 ) or re.examcount >=2 )");
			hql.append("   and not exists(select re.courseid from edu_teach_noexam ne where re.studentid = ne.studentid and re.courseid = ne.courseid )");//==2-28
		if (condition.containsKey("examSubId")) {
			hql.append("  and re.examsubid=:examSubId");
			param.add(condition.get("examSubId"));
		}
		if (condition.containsKey("gradeId")) {
			 hql.append("  and stu.gradeId=:gradeId");
			 param.add(condition.get("gradeId"));
			 
		}
		if (condition.containsKey("unitId")) {
			hql.append("  and stu.branchschoolid=:unitId");
			 param.add(condition.get("unitId"));
		}
		if (condition.containsKey("major")) {
			 hql.append("  and stu.majorid =:major");
			 param.add(condition.get("major"));
		}
		if (condition.containsKey("courseName")) {
			 hql.append("  and lower(c.courseName) like:courseName ");
			 param.add("%"+condition.get("courseName").toString().toLowerCase()+"%");
		}
		if (condition.containsKey("classesId")) {
			 hql.append("  and stu.classesId =:classesId");
			 param.add(condition.get("classesId"));
		}
		
		hql.append(" order by unit.unitname ,major.majorname,c.coursename ,stu.studyNo desc  ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, hql.toString(), param.toArray(), FailExamStudentVo.class);
		
	}
	
	/**
	 * 导出实考与报考人数
	 */
	@Override
	public List<EnterExamToTalVo> printEnterExamList(Map<String, Object> condition) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer hql = new StringBuffer();
		
//		hql.append("select sub.batchname,pc.teachtype,c.coursename from edu_teach_examsub sub");
//		hql.append(" join edu_roll_studentinfo stu on stu.resourceid  = re.studentid ");
//		hql.append(" join edu_roll_studentinfo stu on stu.resourceid  = re.studentid ");
//		hql.append(" join edu_base_course c on re.courseid = c.resourceid ");
//		hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
//		hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
//		hql.append(" join edu_teach_plancourse pc on stu.teachplanid = pc.planid and re.courseid = pc.courseid  and  pc.isdeleted = '0' and pc.teachtype = 'facestudy' ");
//		hql.append(" where stu.isDeleted = 0 and re.isDeleted = 0");
		
		hql.append("select '0' realTotal, pc.courseid courseid,c.coursename courseName,sub.batchname examSubName," +
				"pc.teachtype teachTypeName, count(distinct st.resourceid) enterTotal")
		.append(" from edu_learn_stuplan stp,edu_roll_studentinfo st ,edu_teach_examinfo ei,edu_teach_plancourse pc,")
		.append("hnjk_sys_unit u,edu_base_course c,edu_teach_examsub sub")
		.append(" where stp.studentid =st.resourceid and st.branchschoolid = u.resourceid and stp.plansourceid = pc.resourceid")
		.append(" and pc.courseid = c.resourceid and stp.examinfoid = ei.resourceid and ei.examsubid = sub.resourceid")
		.append(" and ei.isdeleted = 0 and stp.status =2 and stp.isdeleted = 0 ");
		
		if (condition.containsKey("examSubId")) {
			hql.append(" and ei.examsubid=:examSubId");
			param.add(condition.get("examSubId"));
		}
		if (condition.containsKey("brshSchool")) {
			hql.append(" and st.branchschoolid=:brshSchool");
			param.add(condition.get("brshSchool"));
		}
		if (condition.containsKey("courseId")) {
			hql.append(" and c.resourceid =:courseId");
			param.add(condition.get("courseId"));
		}
		if (condition.containsKey("examCourseCode")) {
			hql.append(" and ei.examcoursecode like :examCourseCode");
			param.add("%"+condition.get("examCourseCode") +"%");
		}
		if (condition.containsKey("teachType")) {
			hql.append(" and pc.teachtype =:teachType");
			param.add(condition.get("teachType"));
		}
		
		hql.append(" group by pc.courseid ,c.coursename,sub.batchname,pc.teachtype");
		
		
		
//		StringBuffer sql 		  = new StringBuffer();
//
//		sql.append(" select g.gradename,cl.classicname,m.majorname,c.coursename,count(stu.resourceid) headExam,count(rs.resourceid) realExam, ");
//		sql.append("   g.resourceid gradeId,cl.resourceid classicId,m.resourceid majorId,c.resourceid courseId,pc.resourceid majorCourseId");
//		sql.append("  from edu_teach_plancourse pc   ");
//		sql.append(" inner join edu_base_course c on pc.courseid = c.resourceid  ");
//		if (condition.containsKey("courseId")) {
//			sql.append(" and c.resourceid=?");
//			param.add(condition.get("courseId"));
//		}
//		//2012.9.12朝上提出不过滤毕业学生
//		sql.append(" left join edu_roll_studentinfo stu on pc.planid = stu.teachplanid and stu.isDeleted=? ");
//		//param.add("11");
//		param.add(0);
//		if (condition.containsKey("branchSchool")){
//			sql.append(" and stu.branchschoolid=?");
//			param.add(condition.get("branchSchool"));
//		}
//		sql.append(" left join edu_teach_examresults rs on rs.courseid=pc.courseid and rs.studentid = stu.resourceid and rs.isdeleted =? ");
//		param.add(0);
//		if (condition.containsKey("examSubId")) {
//			sql.append(" and rs.examsubid=?");
//			param.add(condition.get("examSubId"));
//		}
//		sql.append(" inner join edu_base_grade g on stu.gradeid = g.resourceid ");
//		if (condition.containsKey("gradeid")){
//			sql.append(" and g.resourceid=?");
//			param.add(condition.get("gradeid"));
//		}
//		sql.append(" inner join edu_base_major m on stu.majorid = m.resourceid ");
//		if (condition.containsKey("major")){
//			sql.append(" and  m.resourceid =? ");
//			param.add(condition.get("major"));
//		}
//		sql.append(" inner join edu_base_classic cl on stu.classicid = cl.resourceid ");
//		if (condition.containsKey("classic")){
//			sql.append(" and cl.resourceid=? ");
//			param.add(condition.get("classic"));
//		}
//
//		sql.append(" where pc.isdeleted =? ");
//		param.add(0);
//		sql.append(" and ( pc.coursetype<> ? or pc.coursetype is null) ");
//		param.add("thesis");
//		sql.append(" and pc.teachtype=? ");
//		param.add(condition.get("teachType"));
//		sql.append(" group by g.gradename,cl.classicname,m.majorname,c.coursename,g.resourceid,cl.resourceid,m.resourceid,c.resourceid,pc.resourceid ");
//		sql.append(" order by g.gradename desc ");
		
		
		
		
		List<EnterExamToTalVo> list = new ArrayList<EnterExamToTalVo>();
		
		try {
			 list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), param.toArray(), EnterExamToTalVo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return  list;
	}
	
	/**
	 * 导出实考与报考人数
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> getRealExamList(Map<String, Object> condition) throws Exception {
		StringBuffer sql = new StringBuffer();
		Map<String, Object> param = new HashMap<String, Object>();
		sql.append("select results.courseid,count(distinct st.resourceid) realToal from edu_teach_examresults results,edu_teach_examinfo ei,edu_roll_studentinfo st,")
		.append("edu_teach_plancourse pc ")
		.append(" where results.isDeleted=0 and results.examinfoid=ei.resourceid and results.studentid=st.resourceid and results.courseid=pc.courseid")
		.append(" and ((results.examabnormity=0 and results.integratedscore is not null) or (results.examabnormity=1 or results.examabnormity=4))")
		;
		
		if (condition.containsKey("examSubId")) {
			sql.append(" and results.examsubid=:examSubId");
			param.put("examSubId", condition.get("examSubId"));
		}
		
		if (condition.containsKey("courseId")) {
			sql.append(" and results.courseid=:courseId");
			param.put("courseId", condition.get("courseId"));
		}
		
		if (condition.containsKey("teachType")) {
			sql.append(" and pc.teachtype=:teachType");
		}
		
		if (condition.containsKey("brshSchool")) {
			sql.append(" and st.branchschoolid=:brshSchool");
			param.put("brshSchool", condition.get("brshSchool"));
		}
		
		if (condition.containsKey("examCourseCode")) {
			sql.append(" and ei.examcoursecode like:examCourseCode");
			param.put("examCourseCode", "%" +condition.get("examCourseCode")+"%");
		}
		
		sql.append(" group by results.courseid");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page queryExamResultsInfoForFaceTeachTypeMakeup2(Map<String, Object> condition, Page page) throws ServiceException {
		StringBuffer sql = new StringBuffer();
		List<Object> param = new ArrayList<Object>(20);
		
		sql.append(" select exinfo.resourceid,unit.resourceid branchSchool,unit.unitshortname,g.gradename,g.resourceid gradeId,m.majorname,m.resourceid majorId,count(re.resourceid) headExam, ");
//		sql.append(" select unit.resourceid branchSchool,unit.unitshortname,g.gradename,g.resourceid gradeId,m.majorname,m.resourceid majorId, ");
		sql.append(" re.examsubid,");
		sql.append(" c.coursecode,c.coursename,c.resourceid courseId,sub.batchname,es.classesname,es.resourceid classId,exinfo.resourceid  examInfoResourceId");
		sql.append(",count(case when re.examabnormity not in ('2','5') and re.checkstatus!='-1' then 1 else null end) realExam,etc.teachType");
		//统计各个成绩状态人数 保存/提交/发布
		sql.append(",count(case when re.CHECKSTATUS='0' then 1 else null end) checkstatus_save");
		sql.append(",count(case when re.CHECKSTATUS='1' then 1 else null end) checkstatus_submit");
		sql.append(",count(case when re.CHECKSTATUS='4' then 1 else null end) checkstatus_publish");
//		sql.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid");
		sql.append(" from edu_teach_examresults re join edu_roll_studentinfo stu on stu.resourceid = re.studentid");
		sql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid");
		sql.append(" join edu_base_major m on stu.majorid = m.resourceid");
		sql.append(" join edu_base_course c on re.courseid = c.resourceid");
		sql.append(" join edu_roll_classes es on stu.classesid = es.resourceid");
		sql.append(" join edu_base_grade g on g.resourceid = stu.gradeid");
		sql.append(" inner join edu_base_classic cl on stu.classicid = cl.resourceid ");
		sql.append(" join edu_teach_plancourse tp on tp.courseid = c.resourceid and tp.planid = stu.teachplanid and tp.isdeleted=0");
//		if(condition.containsKey("examSubId")){
//			sql.append(" join edu_teach_examresults re on stu.resourceid=re.studentid and re.examsubid='" + condition.get("examSubId") + "' and re.courseid=a.courseid");
//		}
		//sql.append(" join EDU_TEACH_EXAMINFO exinfo on exinfo.resourceid = re.EXAMINFOID");
		//开课记录表-教学类型
		sql.append(" join edu_teach_guiplan gp on gp.gradeid=stu.gradeid and gp.planid=stu.teachplanid and gp.isdeleted=0");//20180509,补考名单重复，需关联年级教学计划
		sql.append(" join edu_teach_coursestatus etc on etc.isopen = 'Y' and etc.plancourseid = tp.resourceid and etc.schoolids = unit.resourceid and etc.guiplanid=gp.resourceid and etc.isdeleted = 0 ");
		//考试信息及考试批次
		sql.append(" join EDU_TEACH_EXAMINFO exinfo on exinfo.resourceid = re.EXAMINFOID and exinfo.courseid = c.resourceid and exinfo.isdeleted = 0");
		//后面建议加上此逻辑，因为要做数据修复比较麻烦，所以暂时不这么处理（2018.07） ei.EXAMCOURSETYPE=decode(cs.TEACHTYPE,'networkTeach',0,'faceTeach',1,2)
		sql.append(" join EDU_TEACH_EXAMSUB sub on sub.resourceid = exinfo.EXAMSUBID and sub.resourceid = re.examsubid and sub.batchtype='exam' and sub.examtype!='N'");
		//毕业数据库
		sql.append(" left join edu_teach_graduatedata gd on gd.studentid = re.studentid and gd.isdeleted = 0 ");
		sql.append(" where stu.isdeleted = 0 and (stu.studentstatus='11' or stu.studentstatus='21' or stu.studentstatus='25'  or (stu.studentstatus = '24' and gd.isallowsecgraduate = 'Y'))");
		sql.append(" and re.isdeleted = 0 ");
			
		if(condition.containsKey("examSubId_makeup")){
			sql.append(" and re.examsubid=? ");
			param.add(condition.get("examSubId_makeup"));
		}
		if(condition.containsKey("courseName")){
			sql.append(" and c.coursename like '%"+condition.get("courseName")+"%'");
		}
		if (condition.containsKey("checkStatus_makeup")){
			sql.append(" and re.checkStatus=?");
			param.add(condition.get("checkStatus_makeup"));
		}
		if (condition.containsKey("courseId_makeup")){
			sql.append(" and re.courseid=?");
			param.add(condition.get("courseId_makeup"));
		}
		if (condition.containsKey("branchSchool_makeup")){
			sql.append(" and stu.branchschoolid=?");
			param.add(condition.get("branchSchool_makeup"));
		}
		if (condition.containsKey("gradeid_makeup")){
			sql.append(" and g.resourceid=?");
			param.add(condition.get("gradeid_makeup"));
		}
		if (condition.containsKey("major_makeup")){
			sql.append(" and  m.resourceid =? ");
			param.add(condition.get("major_makeup"));
		}
		if (condition.containsKey("classId_makeup")){
			sql.append(" and es.resourceid =? ");
			param.add(condition.get("classId_makeup"));
		}
		if (condition.containsKey("classic_makeup")){
			sql.append(" and cl.resourceid =? ");
			param.add(condition.get("classic_makeup"));
		}
		if (condition.containsKey("courseTeachType")) {
			sql.append(" and etc.teachType =? ");
			param.add(condition.get("courseTeachType"));
		}
		sql.append(" group by unit.resourceid,unit.unitshortname,g.gradename,g.resourceid,m.majorname,m.resourceid,c.coursecode,c.coursename,c.resourceid,es.classesname,es.resourceid,sub.batchname,exinfo.resourceid,re.examsubid,etc.teachType ");
		sql.append(" order by unit.unitshortname,g.gradename desc ");

		try {
			page  = baseSupportJdbcDao.getBaseJdbcTemplate().findList(page, sql.toString(), param.toArray(), FaceMakeupExamResultsVo.class);
			/*
			List<FaceMakeupExamResultsVo> results = page.getResult();
			for (FaceMakeupExamResultsVo result : results) {
				String sqls = "select count(rs.resourceid) as realExam "
						  + " from EDU_TEACH_EXAMRESULTS rs, "
						  + "      EDU_ROLL_STUDENTINFO  stu "
						  + " where rs.STUDENTID = stu.resourceid "
						  + "  and rs.isDeleted = 0 "
//						  + "    and stu.BRANCHSCHOOLID = '01017' "
//						  + "    and stu.MAJORID = '177' "
						  + "  and rs.EXAMSUBID = :examSubId and rs.COURSEID = :courseId and stu.CLASSESID = :classId "
						  + " and rs.EXAMINFOID=:examInfoId and rs.examabnormity not in ('2','5') and rs.checkstatus!='-1' ";
				Map params = new HashMap();
				params.put("courseId", result.getCourseId());
				params.put("examSubId", result.getExamSubId());
				params.put("classId", result.getClassId());
				params.put("examInfoId", result.getExamInfoResourceId());
				List resutList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqls, params);
				if (resutList.size() > 0) {
					Map realExam = (Map) resutList.get(0);
					result.setRealExam(Integer.parseInt(realExam.get("realExam").toString()));
				}
			}	*/
		} catch (Exception e) {
			logger.error("获取面授课成绩审核应考人数出错："+e.fillInStackTrace());
		}
		
		return page;
	}
	
	@Override
	/**
	 *  补考成绩审核-全部通过(面授课程、面授+网授课程)
	 * @param ids
	 * @param request
	 * @param info
	 * @param teachType:统考/非统考
	 * @throws ServiceException
	 */
	public void auditExamResultsAllPassForFactMakeupCourse(Map<String, Object> condition, ExamInfo info, String teachType, String examSubId)
			throws Exception {
		condition.put("unRecord", "-1");
		List<ExamResults> list = queryExamResultsByCondition(condition);

		if (null!=list && !list.isEmpty()) {
			try {
				
				int submitNum 	   = 0;
				int publishNum     = 0;
				Date curTime       = new Date ();
				User curUser       = SpringSecurityHelper.getCurrentUser();
				StringBuilder rsIds= new StringBuilder();
				for (ExamResults rs:list ) {
					rsIds.append(",'"+rs.getResourceid()+"'");
					if (Constants.EXAMRESULT_CHECKSTATUS_SUBMIT.equals(rs.getCheckStatus())||Constants.EXAMRESULT_CHECKSTATUS_PASS.equals(rs.getCheckStatus())||Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(rs.getCheckStatus())) {
						submitNum +=1;
					}else if(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(rs.getCheckStatus())){
						publishNum += 1;
					}
				}
				
	//			if(ExStringUtils.isBlank(info.getCourseScoreType())) throw new ServiceException("考试课程《"+ExStringUtils.trimToEmpty(info.getExamCourseCode())+info.getCourse().getCourseName()+"》成绩类型未设置，不允许进行审核操作！");
				if (submitNum<list.size()) {
					throw new ServiceException("成绩提交人数小于实考人数，不允许全部审核通过！");
				}
	
	//			if (null==info.getStudyScorePer()) {
	//				throw new ServiceException("成绩比例未设置，不允许审核成绩!");
	//			}
				
				//计算综合成绩
	//			list 					      			= calculateExamResultsListIntegratedScore(list, info);
				List<StudentLearnPlan> up     			= new ArrayList<StudentLearnPlan>();
				Map<String,StudentLearnPlan> map    	= new HashMap<String, StudentLearnPlan>();
				List<StudentCheck> checkList  			= new ArrayList<StudentCheck>();
				List<StudentLearnPlan> lp     			= studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=?  and plan.examResults.resourceid in("+rsIds.substring(1)+")",0);
				for (StudentLearnPlan p : lp) {
					map.put(p.getExamResults().getResourceid(), p);
				}
				
	//			String hql = " from "+TeachingPlanCourse.class.getSimpleName()+" a where a.isDeleted=0 and a.teachingPlan.resourceid=? and a.course.resourceid=? and a.teachType='facestudy'";
				String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? and a.examResults.resourceid=? ";
				//List<StudentMakeupList> makeupList = Collections.EMPTY_LIST;
				StudentMakeupList makeup = null;
				ExamSub examSub               	 = examSubService.get(examSubId);    			 //考试批次
				List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
				StudentLearnPlan plan = null;
				TeachingPlanCourse pc = null;
				List<StudentMakeupList> makeupList_temp;
				for (ExamResults rs:list) {		
					
					rs.setAuditDate(curTime);
					rs.setAuditMan(curUser.getCnName());
					rs.setAuditManId(curUser.getResourceid());
					rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
	//				rs.setIntegratedScore(rs.getTempintegratedScore_d());
					rs.setExamCount(rs.getExamCount()+1);
					StudentCheck check = new StudentCheck();
					check.setStudentId(rs.getStudentInfo().getResourceid());
					checkList.add(check);
					if(info!=null && rs.getExamInfo()!=null && !rs.getExamInfo().getResourceid().equals(info.getResourceid())){
						rs.setExamInfo(info);
					}

					if (map.containsKey(rs.getResourceid())) {
						plan = map.get(rs.getResourceid());
						plan.setStatus(3);
					}else {
						plan = null;
					}
					//发布通过设置为最终分数
					if(plan!=null){
						plan.setIntegratedScore(rs.getIntegratedScore());
					}
					
					//2014-5-15
	//				List<TeachingPlanCourse> planCourseList = teachingPlanCourseService.findByHql(hql,
	//						rs.getStudentInfo().getTeachingPlan().getResourceid(),rs.getCourse().getResourceid());

					if (map.get(rs.getResourceid()) != null) {
						pc = map.get(rs.getResourceid()).getTeachingPlanCourse();
					}else {
						pc = null;
					}
					if(!"netsidestudy".equals(teachType)){//只处理非统考
						String abnormity = rs.getExamAbnormity();//0-正常1-作弊2-缺考3-无卷4-其它-5缓考
						String integrate = rs.getIntegratedScore();
						String checkStatus = rs.getCheckStatus();
						if("0".equals(abnormity) && Double.parseDouble(integrate)>=60.0){//如果学生这门课通过，查询补考表，有这门课，则修改是否补考通过状态
							makeupList_temp = studentMakeupListService.findByHql(hqlMakeup,rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid(),rs.getResourceid());
						    if(null!=makeupList_temp && makeupList_temp.size()>0){
						    	makeup = makeupList_temp.get(0);
						    	makeup.setIsPass("Y");
						    	studentMakeupListService.update(makeup);
						    	//补考通过则设置最终通过
								if(plan!=null){
									plan.setIsPass("Y");
								}
						    }
						}

	//					if("1".equals(abnormity) || "2".equals(abnormity) || "3".equals(abnormity)|| "4".equals(abnormity)
	//							|| "5".equals(abnormity) || Integer.valueOf(rs.getIntegratedScore())<60){//如果学生这门课没通过，查询补考表，有则不处理，没有则添加
	//						makeupList = studentMakeupListService.findByHql(hqlMakeup,rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
	//					    if(null==makeupList || makeupList.size()==0){
	//					    	makeup = new StudentMakeupList();
	//					    	makeup.setStudentInfo(rs.getStudentInfo());
	//					    	makeup.setCourse(rs.getCourse());
	//					    	makeup.setExamResults(rs);
	//					    	if(null!=pc){
	//					    		makeup.setTeachingPlanCourse(pc);
	//					    	}
	//					    	studentMakeupListService.saveOrUpdate(makeup);
	//					    } else if(makeupList.size()>0){
	//					    	makeup = makeupList.get(0);
	//					    	makeup.setExamResults(rs);
	//					    	studentMakeupListService.saveOrUpdate(makeup);
	//					    }
	//					}
						//判断是否需要需要补考
						//20140617 温朝上提出，综合成绩小于60分都进去补考名单
						//20140624 陈浩珍提出，没有成绩与综合成绩小于60的都进入补考名单
						if(ExStringUtils.isEmpty(rs.getIntegratedScore())||
							(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(checkStatus) && ExStringUtils.isNotEmpty(rs.getIntegratedScore()) && Double.parseDouble(rs.getIntegratedScore())<60.0)
							|| "2".equals(rs.getExamAbnormity()) || "1".equals(rs.getExamAbnormity())){
							//进入补考则设置最终不通过
							if(plan!=null){
								plan.setIsPass("N");
							}
							handleMadeUp(curTime, curUser, rs);
						}

						if(ExStringUtils.isNotBlank(rs.getIntegratedScore())&&Double.parseDouble(rs.getIntegratedScore())>=60){//由于当前只能录入百分制度的分值成绩，所以只判断成绩>=60分为及格
							String hql = "from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.nextExamSubId=? and a.studentInfo.resourceid=? and a.course.resourceid=? ";
							makeupList_temp = studentMakeupListService.findByHql(hql, rs.getExamsubId(),rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
							if(makeupList_temp!=null && makeupList_temp.size()>0){
								StudentMakeupList stumakeup= new StudentMakeupList();
								stumakeup=	makeupList_temp.get(0);
								stumakeup.setIsPass("Y");
								makeupList.add(stumakeup);
								//studentMakeupListService.saveOrUpdate(stumakeup);
								//补考通过则设置最终通过
								if(plan!=null){
									plan.setIsPass("Y");
								}
							}
						}
					}
				}
				
				batchSaveOrUpdate(list);
				
				//更新学习计划
				for (StudentLearnPlan p : lp) {
					p.setStatus(3);
				}
				
				if (up!=null && up.size()>0){
					studentLearnPlanService.batchSaveOrUpdate(up);
				}
				if (makeupList!=null && makeupList.size()>0){
					studentMakeupListService.batchSaveOrUpdate(makeupList);
				}
				//将发布所涉及的学籍ID放入临时表，计算学生获取的学分
				if (checkList!=null && checkList.size()>0) {
					exGeneralHibernateDao.batchSaveOrUpdate(checkList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			throw new ServiceException("没有需要审核的记录！");
		}
	}

	@Override
	/**
	 *  补考成绩审核-个别审核
	 */
	public void makeupAuditExamResultsSingle(String[] idsArray, HttpServletRequest request, ExamInfo info, String teachType, String examSubId)
			throws Exception {
		if (null!=idsArray&&idsArray.length>0&&ExStringUtils.isNotEmpty(teachType)) {

			User curUser 		   				= SpringSecurityHelper.getCurrentUser();	  //当前用户
			Date curTime 		   				= new Date();                                 //当前时间
			List<ExamResults> list 				= this.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("resourceid", idsArray));
			list                   				= calculateExamResultsListIntegratedScore(list,info);//计算综合成绩
			
			Map<String,StudentLearnPlan> map    = new HashMap<String, StudentLearnPlan>();
			List<StudentCheck> checkList  		= new ArrayList<StudentCheck>();
			
			String rsIds                        = "'"+ExStringUtils.join(idsArray,"','")+"'";
			List<StudentLearnPlan> lp     		= studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=?  and plan.examResults.resourceid in("+rsIds+")",0);
			for (StudentLearnPlan p : lp) {
				map.put(p.getExamResults().getResourceid(), p);
			}
			String hqlMakeup = " from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.studentInfo.resourceid=? and a.course.resourceid=? and a.examResults.resourceid=? ";
			//List<StudentMakeupList> makeupList = Collections.EMPTY_LIST;
			StudentMakeupList makeup = null;
			ExamSub examSub               	 = examSubService.get(examSubId);    			 //考试批次
			List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
			StudentLearnPlan plan = null;
			TeachingPlanCourse pc = null;
			StudentCheck check;
			for (ExamResults rs:list) {
				
				//---------------------------------------------审核时提交的数据---------------------------------------------
				String checkNotes               = ExStringUtils.trimToEmpty(request.getParameter("checkNotes_"+rs.getResourceid()));     //审核时提交的 审核意见
				
				rs.setAuditDate(curTime);
				rs.setAuditMan(curUser.getUsername());
				rs.setAuditManId(curUser.getResourceid());
				rs.setCheckNotes(checkNotes);
				
				//审核通过将综合分写入IntegratedScore字段
				if (map.containsKey(rs.getResourceid())) {
					plan = map.get(rs.getResourceid());
					plan.setStatus(3);
				}else {
					plan = null;
				}
				check = new StudentCheck();
				check.setStudentId(rs.getStudentInfo().getResourceid());
				checkList.add(check);
				
				rs.setCheckStatus(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH);
				rs.setExamCount(rs.getExamCount()==null?2L:rs.getExamCount());	
				//发布通过设置为最终分数
				if(plan!=null){
					plan.setIntegratedScore(rs.getIntegratedScore());
				}
				//2014-5-14  1.非统考，如果学生这门课通过，查询补考表，有这门课，则假删除
				//			 2.非统考，如果学生这门课没通过，查询补考表，有则不处理，没有则添加

				if (map.get(rs.getResourceid()) != null) {
					pc = map.get(rs.getResourceid()).getTeachingPlanCourse();
				}else {
					pc = null;
				}
				if(!"netsidestudy".equals(teachType)){//只处理非统考
					String abnormity = rs.getExamAbnormity();//0-正常1-作弊2-缺考3-无卷4-其它-5缓考
					String integrate = rs.getIntegratedScore();
					String checkStatus = rs.getCheckStatus();
					if("0".equals(abnormity) && Double.parseDouble(integrate)>=60.0){//如果学生这门课通过，查询补考表，有这门课，则修改是否补考通过状态
						makeupList = studentMakeupListService.findByHql(hqlMakeup,rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid(),rs.getResourceid());
					    if(null!=makeupList && makeupList.size()>0){
					    	makeup = makeupList.get(0);
					    	makeup.setIsPass("Y");
					    	studentMakeupListService.update(makeup);
					    	
					    	if(plan!=null){//如果补考通过设置最终通过
								plan.setIsPass("Y");
							}
					    }
					}
					
//					if("1".equals(abnormity) || "2".equals(abnormity) || "3".equals(abnormity)|| "4".equals(abnormity) 
//							|| "5".equals(abnormity) || Integer.valueOf(rs.getIntegratedScore())<60){//如果学生这门课没通过，查询补考表，有则替换成绩，没有则添加
//						makeupList = studentMakeupListService.findByHql(hqlMakeup,rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
//					    if(null==makeupList || makeupList.size()==0){
//					    	makeup = new StudentMakeupList();
//					    	makeup.setStudentInfo(rs.getStudentInfo());
//					    	makeup.setCourse(rs.getCourse());
//					    	makeup.setExamResults(rs);
//					    	if(null != pc){
//					    		makeup.setTeachingPlanCourse(pc);
//					    	}
//					    	studentMakeupListService.saveOrUpdate(makeup);
//					    } else if(makeupList.size()>0){
//					    	makeup = makeupList.get(0);
//					    	makeup.setExamResults(rs);
//					    	studentMakeupListService.saveOrUpdate(makeup);
//					    }
//					}
					//判断是否需要需要补考
					//20140617 温朝上提出，综合成绩小于60分都进去补考名单
					//20140624 陈浩珍提出，没有成绩与综合成绩小于60的都进入补考名单
					if(
						ExStringUtils.isEmpty(integrate)||
						(Constants.EXAMRESULT_CHECKSTATUS_PUBLISH.equals(checkStatus) && ExStringUtils.isNotEmpty(integrate) && Double.parseDouble(integrate)<60.0)
						|| "2".equals(abnormity) || "1".equals(abnormity)){
						if(plan!=null){//如果进入补考通过设置最终不通过
							plan.setIsPass("N");
						}
						handleMadeUp(curTime, curUser, rs);
					}
					//如果补考成绩及格，标记上次补考名单中的是否通过为  Y 
					if(ExStringUtils.isNotBlank(rs.getIntegratedScore())&&Double.parseDouble(rs.getIntegratedScore())>=60){//由于当前只能录入百分制度的分值成绩，所以只判断成绩>=60分为及格
						String hql = "from "+StudentMakeupList.class.getSimpleName()+" a where a.isDeleted=0 and a.nextExamSubId=? and a.studentInfo.resourceid=? and a.course.resourceid=? ";
						makeupList = studentMakeupListService.findByHql(hql, rs.getExamsubId(),rs.getStudentInfo().getResourceid(),rs.getCourse().getResourceid());
						if(makeupList!=null && makeupList.size()>0){
							StudentMakeupList stumakeup= new StudentMakeupList();
							stumakeup=	makeupList.get(0);
							stumakeup.setIsPass("Y");
							makeupList.add(stumakeup);
							//studentMakeupListService.saveOrUpdate(stumakeup);
							if(plan!=null){//如果补考通过设置最终通过
								plan.setIsPass("Y");
							}
						}
						
					}
				}
			}
			
			this.batchSaveOrUpdate(list);
			if (!lp.isEmpty()) {
				studentLearnPlanService.batchSaveOrUpdate(lp);
			}
			if (!makeupList.isEmpty()) {
				studentMakeupListService.batchSaveOrUpdate(makeupList);
			}
			if (checkList.size()>0) {
				exGeneralHibernateDao.batchSaveOrUpdate(checkList);
			}
			
		}else {
			throw new ServiceException("请选择要审核的成绩记录！");
		}
		
	}
	
	/**
	 * 获取正考中的缓考成绩
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public  List getZKExamResult(Map<String, Object> condition)throws ServiceException {
		Map<String, Object> para =new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
//		String hql = "from "+ExamResults.class.getSimpleName()+" where examSub.examType='N' and studentInfo.resourceid=?"
//		+" and course.resourceid=? and examSub.resourceid<>? and isDelayExam='Y'";
		sql.append("select a.resourceid ");
		sql.append("from EDU_TEACH_EXAMRESULTS a inner join EDU_TEACH_EXAMSUB b on a.examsubId=b.resourceid ");
		sql.append("where b.examType='N' and a.isDelayExam='Y' ");
		
		if (condition.containsKey("studentid")) {
			 sql.append(" and a.studentid=:studentid");
			 para.put("studentid", condition.get("studentid"));
		}
		if (condition.containsKey("courseid")) {
			sql.append(" and a.courseid=:courseid");
			para.put("courseid", condition.get("courseid"));
		}
		if (condition.containsKey("examsubId")) {
			 sql.append(" and a.examsubId=:examsubId");
			 para.put("examsubId", condition.get("examsubId"));
		}
		sql.append(" order by b.resourceid desc");
		
		List list = new ArrayList();
		try {
			 list =  baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), para) ;
		} catch (Exception e) {
		}	
		
		return list;
	}
	
	/**
	 * 计算课程的综合成绩
	 * @param info
	 * @param usuallyScore
	 * @param writtenScore
	 * @param onlineScore
	 * @param rs
	 * @return
	 */
	@Override
	public String caculateIntegrateScore(ExamInfo info, String usuallyScore, String writtenScore, String onlineScore, ExamResults rs,
										 Map<String, String> resultCalculateRuleMap){
		//判断是否为字符成绩

		if (info == null) {
			throw new WebException("考试课程信息为空！");
		}
		String integratedScoreStr = "";
		//是否混合机考课程
		boolean isMixTrue =false;
		if (null != info && null != info.getIsmixture()) {
			isMixTrue = null == info.getIsmixture() ? false : Constants.BOOLEAN_YES.equals(info.getIsmixture());
		}
				
		Double wsp 	   		 	 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue()); //面授卷面成绩分比例
		//Double usp      		 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue()); //平时考核分比例
		Double osp      		 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue()); //网络卷面学习分比例
		Double usp = null;
		Double online_paper = info.getStudyScorePer();
		Double online_usual = info.getNetsidestudyScorePer();
 		if ("1".equals(info.getExamCourseType().toString())) {//面授课程
			if (null != info.getFacestudyScorePer()) {
				wsp = info.getFacestudyScorePer();
			} else if(info.getExamSub().getFacestudyScorePer()!=null){
				wsp = info.getExamSub().getFacestudyScorePer();
			}
			usp = 100-wsp;
		} else {
			if (null != info.getStudyScorePer()) {
				wsp = info.getStudyScorePer();
				usp = info.getNetsidestudyScorePer(); // 平时分比例
			}else if (info.getExamSub().getWrittenScorePer()!=null) {
				wsp = info.getExamSub().getWrittenScorePer();
			}else {
				wsp = osp;
			}
			usp = 100-wsp;
		}
	
		// 按照成绩计算规则计算比例
		String rule = "";
		if(resultCalculateRuleMap != null && resultCalculateRuleMap.size() > 0) {
			rule = resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_USUALLYSCORE);
			if("2".equals(rule)){
				usp = 100D;
			}
			rule = resultCalculateRuleMap.get(ExamResults.RESULTCALCULATERULE_WRITTENSCORE);
			if("2".equals(rule)){
				wsp = 100D;
			}
		}
		
		BigDecimal divisor 		 = new BigDecimal("1");
		BigDecimal hundredBig    = new BigDecimal(100);
		
		if(ExStringUtils.isEmpty(usuallyScore)){
			usuallyScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}
		if(ExStringUtils.isEmpty(onlineScore)){
			onlineScore = "0";
		}
		String examAbnormity = null != rs ? rs.getExamAbnormity() : "0";
		
		//其它考试类型的课程卷面成绩等于录入的成绩
		Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
		Matcher m = pattern.matcher(writtenScore);
		BigDecimal wsBig = null;
		if (Boolean.TRUE == m.matches()){
			wsBig         = new BigDecimal(writtenScore);
		}else{
			wsBig         = new BigDecimal(0);
		}
		
		BigDecimal usBig    	 = new BigDecimal(usuallyScore);
		BigDecimal osBig    	 = new BigDecimal(onlineScore);
		
		BigDecimal wsPerBig      = new BigDecimal(wsp.toString());
		BigDecimal usPerBig      = new BigDecimal(usp.toString());
		BigDecimal osPerBig      = new BigDecimal(osp.toString());
		
//		BigDecimal wsPerRateBig  = wsPerBig.divide(hundredBig,2);		
//		BigDecimal usPerRateBig  = usPerBig.divide(hundredBig,2);
//		BigDecimal osPerRateBig  = osPerBig.divide(hundredBig,2);
		BigDecimal wsPerRateBig  = wsPerBig.divide(hundredBig);		
		BigDecimal usPerRateBig  = usPerBig.divide(hundredBig);
		BigDecimal osPerRateBig  = osPerBig.divide(hundredBig);
		int integratedScoreScale = 0;
		List<Dictionary> dictionaries = CacheAppManager.getChildren("CodeExamresultsScale");
		for (Dictionary dictionary : dictionaries) {
			if (dictionary.getDictName().startsWith("综合成绩")) {
				integratedScoreScale = Integer.parseInt(dictionary.getDictValue());
				break;
			}
		}
		//成绩中成绩异常不为空时不设置卷面成绩、平时成绩、综合成绩						
		if (ExStringUtils.isNotEmpty(examAbnormity) && !Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)){
				
		//成绩中成绩异常为空时设置卷面成绩、平时成绩、综合成绩	
		}else {
			
			//考试科目中平时成绩比例、卷面成绩比例不为空
			if (null != wsp && wsp >= 0) {
				
				Double usuallyScoreD = usBig.multiply(usPerRateBig).doubleValue();//根据批次比例算出的平时考核成绩
 				Double writtenScoreD = wsBig.multiply(wsPerRateBig).doubleValue();//根据批次比例算出的卷面成绩
				Double onlineScoreD  = osBig.multiply(osPerRateBig).doubleValue();//根据批次比例算出的网上学习成绩
				
				//当综合成绩小于卷面成绩时，综合成绩等于卷面成绩
				/*if ( (writtenScoreD+usuallyScoreD) < Double.parseDouble(writtenScore)){ 
					integratedScoreStr =(wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
				//当综合成绩大于卷面成绩时,综合成绩计算方式为：卷面成绩*卷面成绩比例+平时成绩*平时成绩比例
				}else{ */
					BigDecimal integratedScore = new BigDecimal(String.valueOf(writtenScoreD + usuallyScoreD));
					/*if (null != info && "1".equals(info.getExamCourseType().toString())) {
						integratedScore = new BigDecimal(String.valueOf(writtenScoreD + usuallyScoreD + onlineScoreD));
					} else {
						integratedScore = new BigDecimal(String.valueOf(writtenScoreD + usuallyScoreD));
					}*/
					if(new BigDecimal(integratedScore.intValue()).compareTo(integratedScore)!=0){
						integratedScoreStr =(integratedScore.divide(divisor,integratedScoreScale,BigDecimal.ROUND_HALF_UP)).toString();
					}else {
						integratedScoreStr =(integratedScore.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
					}
					
//				}

			//考试科目中平时成绩比例、卷面成绩比例为空综合成绩等于卷面成绩	
			} else {
				if(new BigDecimal(wsBig.intValue()).compareTo(wsBig)!=0){
					integratedScoreStr = (wsBig.divide(divisor,integratedScoreScale,BigDecimal.ROUND_HALF_UP)).toString();
				}else {
					integratedScoreStr = (wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
				}
					
			}
		}	
		return integratedScoreStr;
	}
	
	/**
	 * 根据学生学籍、课程和考试批次类型查询成绩
	 */
	@Override
	public List<Map<String, Object>> findBySidAndCidAndExamType(
			Map<String, Object> params) throws ServiceException {
		List<Map<String, Object>> examResultList = null;
		try {
			String examResult_sql = "select er.resourceid,er.examsubid from edu_teach_examresults er,edu_teach_examsub es where "
					+ " er.examsubid=es.resourceid and er.isdeleted=0 and es.isdeleted=0 and er.studentid=:studentId "
					+ " and er.courseid=:courseId and es.examtype=:examtype ";
			
			examResultList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examResult_sql, params);
		} catch (Exception e) {
			logger.error("根据学生学籍、课程和考试批次类型查询成绩出错", e);
		}
		return examResultList;
	}

	/**
	 * 查询需要录入的人数
	 * @param condition
	 * @throws Exception
	 */
	@Override
	public void queryInputTotalNum(Map<String, Object> condition, Map<String, Object> inputTotalNum) throws Exception {
		StringBuffer sql = new StringBuffer("select pc.resourceid||'_'||stu.classesid resourceid,count(stu.resourceid) counts from edu_teach_plancourse pc  ");
		sql.append(" inner join edu_roll_studentinfo stu on stu.teachplanid = pc.planid and (stu.studentstatus = :studentStatus1 or stu.studentstatus = :studentStatus2 or stu.studentstatus = :studentStatus3) ");
		//sql.append("  and stu.gradeid =:gradeid    ");		
		if(condition.containsKey("branchSchool")){
			sql.append(" and stu.branchschoolid =:branchSchool ");
		}
		//sql.append(" where pc.isdeleted = :isDeleted and pc.planid = :teachingPlanId ");
		sql.append(" where pc.isdeleted = :isDeleted  ");
		if (condition.containsKey("courseId")) {
			sql.append(" and pc.courseid = :courseId ");
		}
		if (condition.containsKey("teachType")) {
			sql.append(" and pc.teachtype = :teachType ");
		}
		sql.append("  and not exists ( select score.resourceid from edu_teach_examscore score where score.studentid = stu.resourceid and score.courseid =pc.courseid and score.ispass = :isPass )");
		if("N".equals(CacheAppManager.getSysConfigurationByCode("examsfYorN").getParamValue())){
			sql.append(" and not exists(select * from EDU_TEACH_NOEXAM re where re.isDeleted=0 and re.STUDENTID=stu.resourceid and re.checkStatus='1'  and re.COURSEID=pc.COURSEID ) ");
			
		}
		
		sql.append(" group by pc.resourceid,stu.classesid ");
		List<Map<String,Object>> totalList =   baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),condition);
		for (Map<String,Object> total : totalList) {
			inputTotalNum.put(String.valueOf(total.get("resourceid")), ExStringUtils.isNotBlank(total.get("counts").toString())?total.get("counts").toString():"0");
		}
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
	 * 获取补考课程成绩录入情况
	 * @param objpage
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Page findMakeupInfoByContion(Page objpage, Map<String, Object> condition) throws ServiceException {
		StringBuffer hql = new StringBuffer();
		Map<String, Object> para =new HashMap<String, Object>();
		
		hql.append("select distinct c.coursename,unit.unitname,major.majorname,es.classesname,stu.teachplanid,max(s.term) courseterm,max(s.resourceid) coursestatusid");
		hql.append(",stu.gradeid,stu.classesid,major.resourceid majorid,unit.resourceid unitid,c.resourceid courseid,a.plansourceid,max(tc.counts) counts, ");
		hql.append("ctl.teachername,ctl.mobile teacherPhone, academicStaff.emPhone, academicStaff.emName,esub.batchname, ");
		hql.append("max("+convertSql("sst.resultstatus")+") checkStatus0, max("+convertSql("sbt.resultstatus")+") checkStatus1,max("+convertSql("pst.resultstatus")+") checkStatus4, ");
		hql.append("max("+convertSql("sst.resultstatus")+"+"+convertSql("sbt.resultstatus")+"+"+convertSql("pst.resultstatus")+") inputcount ");
		hql.append(" from edu_teach_makeuplist a join edu_roll_studentinfo stu on stu.resourceid = a.studentid ");
		hql.append("  join edu_teach_examsub esub on esub.resourceid = a.nextexamsubid and esub.isdeleted=0 ");
		hql.append(" join hnjk_sys_unit unit on stu.branchschoolid = unit.resourceid ");
		hql.append(" join edu_base_major major on stu.majorid = major.resourceid ");
		hql.append(" join edu_base_course c on a.courseid = c.resourceid ");
		hql.append(" join edu_roll_classes es on stu.classesid = es.resourceid ");
		hql.append(" join edu_base_grade g on g.resourceid = stu.gradeid ");
		hql.append(" left join edu_teach_plancourse tp on tp.resourceid = a.plansourceid ");
		hql.append(" join edu_teach_guiplan gp on gp.planid=stu.teachplanid and gp.gradeid=stu.gradeid and gp.isdeleted=0 ");
		hql.append(" join edu_teach_coursestatus s on s.guiplanid=gp.resourceid and s.plancourseid=tp.resourceid and s.schoolIds=stu.branchschoolid and s.isdeleted=0 ");
		// 登分老师
		hql.append(" left join (select tcl.courseid,tcl.teacherid,tcl.coursestatusid,tcl.classesid,tcl.teachername,tclem.mobile from edu_teach_courseteachercl tcl, ");
		hql.append(" edu_base_edumanager tclem where tcl.teacherid=tclem.sysuserid and tcl.isdeleted=0 and tclem.isdeleted=0) ctl on ctl.coursestatusid = s.resourceid ");
		hql.append(" and ctl.courseid = tp.courseid and ctl.classesid = es.resourceid");
		// 教务员
		hql.append(" left join (select wm_concat(u.cnname) emName,wm_concat(em.officetel) emPhone,u.unitid "); 
		hql.append(" from edu_base_edumanager em,hnjk_sys_users u,hnjk_sys_roleusers ru,hnjk_sys_roles r ");
		hql.append(" where em.sysuserid=u.resourceid and u.resourceid=ru.userid and ru.roleid=r.resourceid ");
		hql.append(" and r.rolecode='ROLE_BRS_STUDENTSTATUS' and em.isdeleted=0 and u.isdeleted=0 group by u.unitid)  academicStaff on academicStaff.unitid=es.orgunitid");
		// 成绩状态
		hql.append(" left join ("+getFailTotalNumSql(condition)+") tc on  tc.countResourceid=a.plansourceid||'_'||stu.classesid||'_'||a.nextexamsubid ");

		hql.append(" left join ("+getFailCheckStatusNumSql("0")+") sst on sst.majorcourseid=tp.resourceid and sst.courseid=c.resourceid and sst.classesid=es.resourceid and sst.gradeid=g.resourceid and a.nextexamsubid=sst.examsubid ");
		hql.append(" left join ("+getFailCheckStatusNumSql("1")+") sbt on sbt.majorcourseid=tp.resourceid and sbt.courseid=c.resourceid and sbt.classesid=es.resourceid and sbt.gradeid=g.resourceid and a.nextexamsubid=sbt.examsubid ");
		hql.append(" left join ("+getFailCheckStatusNumSql("4")+") pst on pst.majorcourseid=tp.resourceid and pst.courseid=c.resourceid and pst.classesid=es.resourceid and pst.gradeid=g.resourceid and a.nextexamsubid=pst.examsubid ");
		
		hql.append(" where stu.isdeleted=0 and (stu.studentstatus='11' or stu.studentstatus='21') and a.isdeleted=0 ");
		
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
		if (condition.containsKey("majorId")) {
			 hql.append(" and stu.majorid =:majorId");
			 para.put("majorId",condition.get("majorId"));
		}
		if (condition.containsKey("courseId")) {
			hql.append("  and a.courseid =:courseId ");
			para.put("courseId",condition.get("courseId"));
		}
		if (condition.containsKey("classesId")) {
			 hql.append("  and stu.classesid =:classesId");
			 para.put("classesId",condition.get("classesId"));
		}
		if(condition.containsKey("teachingPlanId")){//教学计划
			hql.append(" and stu.teachplanid = :teachingPlanId ");
			para.put("teachingPlanId", condition.get("teachingPlanId"));
		}
		
		hql.append(" group by unit.unitname,c.coursename ,major.majorname,es.classesname,stu.gradeid,stu.classesid,major.resourceid,unit.resourceid,esub.batchname, ");
		hql.append("c.resourceid,a.plansourceid,a.resourceid,stu.teachplanid,a.nextexamsubid,ctl.teachername,ctl.mobile, academicStaff.emPhone, academicStaff.emName ");
		hql.append(" order by unit.unitname,major.majorname ,es.classesname,esub.batchname,c.coursename");
		objpage.setOrderBy(null);
		Page page = findBySql(objpage, hql.toString(), para);
		
		return page;
	}
	
	/**
	 * 通过教学计划，教学计划课程，教学点和年级判断是否有成绩
	 * （不包含转教学点，转专业，转学习形式的学生）
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public boolean hasExamResults(Map<String, Object> condition) throws Exception {
		boolean flag = false;
		StringBuffer sql = new StringBuffer();
		sql.append("select er.resourceid,er.studentid,er.majorcourseid,er.examsubid,sci.changetype from edu_teach_examresults er ");
		sql.append("inner join edu_roll_studentinfo so on er.studentid=so.resourceid and so.isdeleted=0  ");
		sql.append("left join edu_roll_stuchangeinfo sci on er.studentid=sci.studentid ");
		sql.append("and sci.changetype in ('22','23','81','82','83','C_24','12') and sci.isdeleted=0 and sci.finalauditstatus='Y' ");//20160712 增加 复学12 lion
		sql.append("where sci.changetype is null and er.isdeleted=0 ");
		sql.append("and er.majorcourseid=:planCourse and so.gradeid=:gradeId ");
		sql.append("and so.teachplanid=:planId and so.branchschoolid=:schoolId ");
		List<Map<String,Object>> examResultsList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		if(examResultsList!=null && examResultsList.size() > 0 ) {
			flag = true;
		}
		return flag;
	}
	
	/**
	 * 删掉某个学生某门课程补考成绩
	 * @param studentInfoId
	 * @param planCourseId
	 * @return
	 */
	@Override
	public int deleteByStuIdAndPlanCourseId(String studentInfoId, String planCourseId) {
		try {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("studentInfoId", studentInfoId);
			param.put("planCourseId", planCourseId);
			String sql = "update edu_teach_examresults er set  er.isdeleted=1 where er.studentid=:studentInfoId and er.majorcourseid=:planCourseId and er.ismakeupexam!='N' ";
			return baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, param);
		} catch (Exception e) {
			logger.error("删掉某个学生某门课程补考成绩出错", e);
		}
		return 0;
	}
	
	/**
	 * 获取某个学生某门课程某个考试批次的成绩
	 * @param studentInfoId
	 * @param planCourseId
	 * @param examSubId
	 * @return
	 */
	@Override
	public ExamResults getByCondtition(String studentInfoId, String planCourseId, String examSubId) {
		String examResultHql = "from " + ExamResults.class.getSimpleName() + " where isDeleted=0 and majorCourseId=? "
				+ " and studentInfo.resourceid=? and examsubId=? ";
		return exGeneralHibernateDao.findUnique(examResultHql, planCourseId,studentInfoId,examSubId);
	}

	/**
	 * 根据考试批次ID及学号、课程查找成绩对象
	 * @param examSubId
	 * @param stuNo
	 * @param examInfoId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<ExamResults> findExamResulByExamSubAndStuNoAndExamInfoId(String examSubId, String[] stuNo, String examInfoId) throws ServiceException {
		List<Criterion> list = new ArrayList<Criterion>();
		list.add(Restrictions.eq("isDeleted", 0));
		list.add(Restrictions.eq("examsubId", examSubId));
		list.add(Restrictions.eq("examInfo.resourceid", examInfoId));
		list.add(Restrictions.in("studentInfo.resourceid", stuNo));
		
		List<ExamResults> examResultsList = findByCriteria(list.toArray(new Criterion[list.size()]));
		return ExCollectionUtils.isNotEmpty(examResultsList) ? examResultsList: null;
		
	}
	
	@Override
	public List<Map<String, Object>> failExamStudentList2Excel(Map<String, Object> condition){
		StringBuffer hql = new StringBuffer();
		Map<String, Object> para =new HashMap<String, Object>();
		
		failExamSql(condition, hql, para);
		List<Map<String, Object>> vo = new ArrayList<Map<String,Object>>();
		try {
			vo = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), para);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vo;
	}

	/**
	 * 根据学籍、教学计划和代替缓考批次获取某个学生的最终成绩
	 * 
	 * @param studentId
	 * @param planId
	 * @param delayExamType
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String,Object>> findExamScore(String studentId, String planId, String delayExamType) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(3);
		params.put("studentId", studentId);
		params.put("planId", planId);
		params.put("delayExamType", delayExamType);
		
		StringBuffer examSql = new StringBuffer(1024); 
		examSql.append("select c.coursename,pc.credithour,pc.term,ber.score normalScore,ber.examabnormity,de.score deScore,mk.score mkScore,noe.score noexamScore,pc.coursetype ")
		.append("from edu_teach_plancourse pc inner join edu_base_course c on pc.courseid=c.resourceid  ")
		.append("left join (select distinct er.majorcourseid,er.courseid,er.examabnormity,f_decrypt_score(er.integratedscore,er.studentid) score ")
		.append("from edu_teach_examresults er where er.isdeleted=0 and er.checkstatus='4' and er.ismakeupexam='N' and er.studentid=:studentId) ber on ber.majorcourseid=pc.resourceid ")
		.append("left join (select distinct er.majorcourseid,er.courseid,er.examabnormity,f_decrypt_score(er.integratedscore,er.studentid) score ")
		.append("from edu_teach_examresults er where er.isdeleted=0 and er.checkstatus='4' and er.ismakeupexam=:delayExamType and er.studentid=:studentId) de on de.majorcourseid=pc.resourceid ")
		.append("left join (select er.majorcourseid,er.courseid,max(f_decrypt_score(er.integratedscore,er.studentid)) score from edu_teach_examresults er ")
		.append("where er.isdeleted=0 and er.ismakeupexam!='N' and er.studentid='' group by er.majorcourseid,er.courseid ) mk on mk.majorcourseid=pc.resourceid ")
		.append("left join (select ne.courseid,ne.scoreforcount score from edu_teach_noexam ne ")
		.append("where ne.isdeleted=0 and ne.studentid=:studentId and ne.checkstatus='1') noe on noe.courseid=pc.courseid ")
		.append("where pc.isdeleted=0 and pc.planid=:planId order by pc.term ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(examSql.toString(), params);
	}
	
	@Override
	public Double getGPA_gdwy(Double integratedScoreNum, List<Dictionary> dictList) {
		for (Dictionary dict:dictList) {
			String scale = dict.getDictName();
			int score = Integer.parseInt(scale.replaceAll(">","").replaceAll("=",""));
			if (scale.startsWith(">=")) {
				if (integratedScoreNum >= score) {
					return Double.parseDouble(dict.getDictValue());
				}
			} else if (scale.startsWith("=")) {
				if (integratedScoreNum - score == 0) {
					return Double.parseDouble(dict.getDictValue());
				}
			} else if (scale.startsWith(">")) {
				if (integratedScoreNum - score > 0) {
					return Double.parseDouble(dict.getDictValue());
				}
			}
		}
		return 0.0;
	}

	@Override
	public Map<String, Object> getAvgScoreGroupByTeachType(String studentid, String openTerm) {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("select pc.courseid,c.coursecode,c.coursename,nvl(nvl(f_decrypt_score(r.integratedscore,r.studentid),nvl2(ne.resourceid,nvl(ne.scoreforcount,75),null)),'0') integratedscore,pc.examclasstype");
		sql.append(" from edu_teach_coursestatus cs");
		sql.append(" join edu_teach_plancourse pc on pc.resourceid = cs.plancourseid and pc.isdeleted = 0");
		sql.append(" join edu_base_course c on c.resourceid = pc.courseid");
		sql.append(" join edu_roll_studentinfo s on s.teachplanid = pc.planid and s.branchschoolid = cs.schoolids and s.isdeleted = 0");
		sql.append(" join edu_teach_guiplan gp on gp.planid = pc.planid and cs.guiplanid = gp.resourceid and gp.isdeleted = 0 and gp.gradeid = s.gradeid");
		sql.append(" left join edu_teach_noexam ne on ne.studentid = s.resourceid and ne.courseid = c.resourceid and ne.checkstatus = '1' and ne.isdeleted = 0");
		sql.append(" left join edu_teach_examresults r on r.studentid = s.resourceid and r.majorcourseid = pc.resourceid and r.checkstatus = '4' and r.ismakeupexam = 'N' and r.isdeleted = 0");
		sql.append(" where cs.openstatus = 'Y' and cs.term like '"+openTerm+"%'  and s.resourceid='"+studentid+"' and cs.isdeleted = 0");
		sql.append(" order by pc.examclasstype,c.resourceid");//,decode(nvl2(ne.resourceid,'Q',r.ismakeupexam),'N',1,'Y',2,'T',3,4) desc
		List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();

		try {
			mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),null);
			Double avgScoreForFaceStudy = 0.0;
			Double avgScoreForNetworkStudy = 0.0;
			Double minNetworkStudyScore = 100.0;
			Double maxNetworkStudyScore = 0.0;
			int faceStudyCount = 0;
			int networkStudyCount = 0;
			Boolean isAllPass = true;
			Boolean isAllPassForNetworkStudyScore = true;
			Boolean hasNetworkStudyScore = false;
			if (mapList != null && mapList.size() > 0) {
				String courseid = "";
				for (Map<String, Object> map : mapList) {
					if (courseid.equals(map.get("courseid"))) {
						courseid = map.get("courseid").toString();
						continue;
					}
					Double integratedscore = ExNumberUtils.toDouble(ExStringUtils.toString(map.get("integratedscore")));
					if (integratedscore < 60 && isAllPass) {
						isAllPass = false;
					}
					if ("3".equals(map.get("examclasstype"))) {
						if (integratedscore < 60 && isAllPassForNetworkStudyScore) {
							isAllPassForNetworkStudyScore = false;
						}
						hasNetworkStudyScore = true;
						networkStudyCount++;
						avgScoreForNetworkStudy += integratedscore;
						if (integratedscore < minNetworkStudyScore) {
							minNetworkStudyScore = integratedscore;
						}
						if (integratedscore > maxNetworkStudyScore) {
							maxNetworkStudyScore = integratedscore;
						}
					} else {
						faceStudyCount++;
						avgScoreForFaceStudy += integratedscore;
					}
					courseid = map.get("courseid").toString();
				}
			}
			returnMap.put("isAllPass",isAllPass);
			returnMap.put("isAllPassForNetworkStudyScore",isAllPassForNetworkStudyScore);
			returnMap.put("hasNetworkStudyScore",hasNetworkStudyScore);
			if (hasNetworkStudyScore) {
				returnMap.put("avgScoreForNetworkStudy",ExNumberUtils.toDouble(avgScoreForNetworkStudy/networkStudyCount));
			}else {
				returnMap.put("avgScoreForNetworkStudy",ExNumberUtils.toDouble(avgScoreForNetworkStudy));
			}
			returnMap.put("minNetworkStudyScore",minNetworkStudyScore);
			returnMap.put("maxNetworkStudyScore",maxNetworkStudyScore);
			if (faceStudyCount == 0) {
				returnMap.put("avgScoreForFaceStudy",avgScoreForFaceStudy);
			} else {
				returnMap.put("avgScoreForFaceStudy",ExNumberUtils.toDouble(avgScoreForFaceStudy/faceStudyCount));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	@Override
	public List<Map<String, Object>> getExamResultMap_STDX(Map<String, Object> map, List<Map<String, Object>> mapList, String classesId, String isMakeUp) {
		int studentNum90 = 0;
		int studentNum80 = 0;
		int studentNum70 = 0;
		int studentNum60 = 0;
		int studentNum59 = 0;
		int absentNum = 0;
		for (Map<String, Object> temp : mapList) {
			String integratedScoreStr = temp.get("integratedScore").toString();
			if(ExStringUtils.isNumeric(integratedScoreStr, 2)){
				int integratedScore = Integer.parseInt(integratedScoreStr);
				if(integratedScore>=90){
					studentNum90++;
				}else if (integratedScore>=80) {
					studentNum80++;
				}else if (integratedScore>=70) {
					studentNum70++;
				}else if (integratedScore>=60) {
					studentNum60++;
				}
			}else if(integratedScoreStr.contains("优")){
				studentNum90++;
			}else if (integratedScoreStr.contains("良")) {
				studentNum80++;
			}else if (integratedScoreStr.contains("中")) {
				studentNum70++;
			}else if ("".equals(integratedScoreStr) || "合格".equals(integratedScoreStr)) {
				studentNum60++;
			}else if ("缺考".equals(integratedScoreStr) || "不及格".equals(integratedScoreStr) || "不合格".equals(integratedScoreStr)){
				absentNum++;
			}
		}
		//两列显示
		if ("N".equals(isMakeUp)) {
			mapList = ExBeanUtils.transformMultipleColumns4Map(2,33,mapList);
			int i = 0;
			Map<String, Object> _map = new HashMap<String, Object>();
			List<Map<String, Object>> tempList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> temp : mapList) {
				_map.put("sortNum_"+i%2,temp.get("sortNum"));
				_map.put("stuNo_"+i%2, temp.get("stuNo"));
				_map.put("stuName_"+i%2, temp.get("stuName"));
				_map.put("usuallyScore_"+i%2, temp.get("usuallyScore"));
				_map.put("writtenScore_"+i%2, temp.get("writtenScore"));
				_map.put("integratedScore_"+i%2, temp.get("integratedScore"));
				if(i%2==1){
					tempList.add(_map);
					_map = new HashMap<String, Object>();
				}
				i++;
			}
			mapList = tempList;
		}

		//统计
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("classesid", classesId);
		parameter.put("stuStatus", "11','16");//在学，毕业
		Double studentNum = Double.valueOf(studentInfoService.getStudentNum(parameter)) ;
		studentNum59 = studentNum.intValue()-studentNum90-studentNum80-studentNum70-studentNum60;
		map.put("studentNum", studentNum.intValue()+"");
		map.put("joinNum", studentNum.intValue()-absentNum+"");
		map.put("absentNum", absentNum+"");
		map.put("studentNum90", studentNum90+"");
		map.put("studentNum80", studentNum80+"");
		map.put("studentNum70", studentNum70+"");
		map.put("studentNum60", studentNum60+"");
		map.put("studentNum59", studentNum59+"");
		map.put("studentNum90Percent", ExNumberUtils.toString(studentNum90/studentNum*100));
		map.put("studentNum80Percent", ExNumberUtils.toString(studentNum80/studentNum*100));
		map.put("studentNum70Percent", ExNumberUtils.toString(studentNum70/studentNum*100));
		map.put("studentNum60Percent", ExNumberUtils.toString(studentNum60/studentNum*100));
		map.put("studentNum59Percent", ExNumberUtils.toString(studentNum59/studentNum*100));
		return mapList;
	}

	@Override
	public List<Map<String, Object>> getMapInfoListByExamResultListForPrint(List<ExamResults> examResultList, List<NoExamApply> noexamList, String isPrint) {
		List<Map<String,Object>> dm= new ArrayList<Map<String,Object>>();
		int i=1;
		Map<String,String> examAbnormityDict = dictionaryService.getDictMapByParentDictCode("CodeExamAbnormity");
		if(examResultList!=null && examResultList.size()>0){
			for (ExamResults rs : examResultList) {
				Map<String,Object> m = new HashMap<String, Object>();
				m.put("sortNum",String.valueOf(i++));
				m.put("stuNo",rs.getStudentInfo().getStudyNo());
				m.put("stuName",rs.getStudentInfo().getStudentName());
				m.put("unitName",rs.getStudentInfo().getBranchSchool().getUnitShortName());
				m.put("gradeName", ExStringUtils.trimToEmpty(rs.getStudentInfo().getGrade().getGradeName()));
				m.put("classicName", ExStringUtils.trimToEmpty(rs.getStudentInfo().getClassic().getShortName()));
				m.put("majorName",rs.getStudentInfo().getMajor().getMajorName());
				m.put("className",rs.getStudentInfo().getClasses().getClassname());
				m.put("courseName",rs.getCourse().getCourseName());
				m.put("usuallyScore",ExStringUtils.trimToEmpty(rs.getUsuallyScore()));

				if (ExStringUtils.isNotBlank(rs.getExamAbnormity()) && !"0".equals(rs.getExamAbnormity())) {
					if (ExStringUtils.isNotEmpty(isPrint) && "Y".equals(isPrint) && "1".equals(rs.getExamAbnormity())) {
						m.put("writtenScore", ExamResults.CHEAT_SCORE);
						m.put("integratedScore", ExamResults.CHEAT_SCORE);
					} else {
						m.put("writtenScore", examAbnormityDict.get(rs.getExamAbnormity()));
						m.put("integratedScore", examAbnormityDict.get(rs.getExamAbnormity()));
					}
				}else {
					m.put("writtenScore", ExStringUtils.trimToEmpty(rs.getWrittenScore()));
					m.put("integratedScore", ExStringUtils.trimToEmpty(rs.getIntegratedScore()));
				}
				dm.add(m);
			}
		}
		if(noexamList!=null && noexamList.size()>0){
			for(NoExamApply noexam : noexamList){
				StudentInfo stu = noexam.getStudentInfo();
				Map<String,Object> m = new HashMap<String, Object>();
				//m.put("sortNum",i++);
				m.put("stuNo",stu.getStudyNo());
				m.put("stuName",stu.getStudentName());
				m.put("unitName",stu.getBranchSchool().getUnitShortName());
				m.put("gradeName", ExStringUtils.trimToEmpty(stu.getGrade().getGradeName()));
				m.put("classicName", ExStringUtils.trimToEmpty(stu.getClassic().getShortName()));
				m.put("majorName",stu.getMajor().getMajorName());
				m.put("usuallyScore","");
				m.put("writtenScore","");
				m.put("integratedScore", ExStringUtils.toString(noexam.getScoreForCount())+examAbnormityDict.get("6"));
				dm.add(m);
			}
			//排序
			ExBeanUtils.sortMaps(dm,"stuNo");
			i = 1;
			for (Map<String,Object> map : dm){
				map.put("sortNum", i++);
			}
		}
		return dm;
	}

	/**
	 * 获取最终成绩
	 * 
	 * @param studentId
	 * @param planId
	 * @param delayExamType
	 * @return
	 */
	@Override
	public List<DegreeCourseExamVO> findFinalExamForGW(String studentId, String planId,String delayExamType) {
		List<DegreeCourseExamVO> finalExamList = null;
		
		try {
			// 获取某个学生的最终成绩
			List<Map<String,Object>> examList = findExamScore(studentId, planId, delayExamType);
			if(ExCollectionUtils.isNotEmpty(examList)){
				finalExamList = new ArrayList<DegreeCourseExamVO>(30);
				DegreeCourseExamVO  examVo = null;
				Map<String,DegreeCourseExamVO> degreeExamMap = new HashMap<String, DegreeCourseExamVO>(10);
				int i = 1;
				String courseName = null;
				BigDecimal creditHour = BigDecimal.ZERO;
				String term = null;
				String normalScore = null;
				String examabnormity = null;
				String deScore = null;
				String mkScore = null;
				String noexamScore = null;
				String score = null;
				String title = null;
				for(Map<String,Object> m : examList){
					courseName = (String)m.get("coursename");
					creditHour = (BigDecimal)m.get("credithour");
					term = (String)m.get("term");
					normalScore = (String)m.get("normalScore");
					examabnormity = (String)m.get("examabnormity");
					deScore = (String)m.get("deScore");
					mkScore = (String)m.get("mkScore");
					noexamScore = (String)m.get("noexamScore");
					// 获取最终成绩，TODO:如果成绩有几种类型，则还要区分
					score = getFinalExamScore(normalScore, examabnormity, deScore, mkScore, noexamScore);
					
					// 判断课程名称的最后一位是否是数字
					if(Character.isDigit(courseName.charAt(courseName.length()-1))){
						courseName = courseName.substring(0, courseName.length()-1);
						if(degreeExamMap.containsKey(courseName)){
							examVo = degreeExamMap.get(courseName);
							examVo.setCreditHour((creditHour.add(BigDecimal.valueOf(Double.valueOf(examVo.getCreditHour())))).toPlainString());
							setScoreByTerm(examVo, term, score);
							continue;
						}
					}
					
					examVo = new DegreeCourseExamVO();
					examVo.setSerialNumber(i);
					examVo.setCourseName(courseName);
					examVo.setCreditHour(creditHour.setScale(1).toPlainString());
					setScoreByTerm(examVo, term, score);
					
					degreeExamMap.put(courseName, examVo);
					finalExamList.add(examVo);
					i++;
				}
				int middleLine = i/2;
				finalExamList.get(middleLine-2).setTitle("成");
				finalExamList.get(middleLine-1).setTitle("绩");
				finalExamList.get(middleLine).setTitle("栏");
				
			}
			
		} catch (Exception e) {
			logger.error("获取最终成绩出错", e);
		}
		
		return finalExamList;
	}

	private String getExamTitle(int total,int i,String title) {
		int k = total/2-i;
		switch (k) {
			case 0:
				title = "栏";
				break;
			case 1:
				title = "绩";
				break;
			case 2:
				title = "成";
				break;
			default:
				break;
		}
		return title;
	}

	/**
	 * 设置分数
	 * 
	 * @param examVo
	 * @param term
	 * @param score
	 */
	private void setScoreByTerm(DegreeCourseExamVO examVo, String term,
			String score) {
		if("1".equals(term)){
			examVo.setScoreOne(score);
		} else if("2".equals(term)){
			examVo.setScoreTwo(score);
		} else if("3".equals(term)){
			examVo.setScoreThree(score);
		} else if("4".equals(term)){
			examVo.setScoreFour(score);
		} else if("5".equals(term)){
			examVo.setScoreFive(score);
		} else {
			examVo.setScoreSix(score);
		}
	}

	/**
	 * 获取学生各科成绩平均分和论文成绩
	 * 
	 * @param studentId
	 * @param planId
	 * @param delayExamType
	 * @return
	 */
	@Override
	public Map<String, String> getAvgAndThesisScore(String studentId, String planId, String delayExamType) {
		Map<String,String> scoreMap = new HashMap<String, String>(2);
		try {
			// 获取某个学生的最终成绩
			List<Map<String,Object>> examList = findExamScore(studentId, planId, delayExamType);
			 if(ExCollectionUtils.isNotEmpty(examList)){
				 String normalScore = null;
				 String examabnormity = null;
				 String deScore = null;
				 String mkScore = null;
				 String noexamScore = null;
				 Double score = 0d;
				 String coursetype = null;
				 String thesisScore = null;
				 int i = 0;
				 String examScore = null;
				 for(Map<String,Object> m : examList){
					normalScore = (String)m.get("normalScore");
					examabnormity = (String)m.get("examabnormity");
					deScore = (String)m.get("deScore");
					mkScore = (String)m.get("mkScore");
					noexamScore = (String)m.get("noexamScore");
					coursetype = (String)m.get("coursetype");
					examScore = getFinalExamScore(normalScore, examabnormity, deScore,mkScore, noexamScore);
					if(ExStringUtils.isBlank(examScore)){
						continue;
					}
					// 获取最终成绩，TODO:如果成绩有几种类型，则还要区分
					if("thesis".equals(coursetype)){// 毕业论文
						thesisScore = examScore;
						continue;
					}
					score += Double.valueOf(examScore);
					i++;
				 }
				 
				 scoreMap.put("avgScore", BigDecimal.valueOf(score/i).setScale(1,BigDecimal.ROUND_HALF_UP).toPlainString());
				 scoreMap.put("thesisScore", thesisScore);
			 }
		} catch (Exception e) {
			logger.error("获取学生各科成绩平均分和论文成绩出错", e);
		}
		
		return scoreMap;
	}

	/**
	 * 获取最终分数
	 * 
	 * @param normalScore
	 * @param examabnormity
	 * @param deScore
	 * @param mkScore
	 * @param noexamScore
	 * @return
	 */
	private String getFinalExamScore(String normalScore, String examabnormity,
			String deScore, String mkScore, String noexamScore) {
		String score = null;;
		if(ExStringUtils.isNotEmpty(noexamScore)){// 有免修免考
			score = noexamScore;
		} else if(ExStringUtils.isBlank(examabnormity) ){ // 还未录入课程成绩的课程
			return null;
		}else if("5".equals(examabnormity)){// 正考缓考
			if(Double.valueOf(deScore).compareTo(60d)>=0){//及格
				score = deScore;
			} else {
				// 及格则按60分显示，不及格则直接显示成绩
				score = Double.valueOf(mkScore).compareTo(60d)>=0?"60":mkScore;
			}
		}else if("0".equals(examabnormity) && Double.valueOf(normalScore).compareTo(60d)>=0){// 正考并且及格
			score = normalScore;
		} else{
			// 及格则按60分显示，不及格则直接显示成绩
			score = Double.valueOf(mkScore).compareTo(60d)>=0?"60":mkScore;
		}
		
		return score;
	}
	
}
