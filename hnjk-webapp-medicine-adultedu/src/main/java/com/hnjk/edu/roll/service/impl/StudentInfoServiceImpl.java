package com.hnjk.edu.roll.service.impl;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.PersonalRalation;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.model.StudentResume;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.roll.vo.*;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.OrderCourseSetting;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IOrderCourseSettingService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.teaching.vo.StudentInfoAndDegreeCourseVO;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.IUserOperationLogsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IUserService;
import lombok.Cleanup;
import org.apache.commons.lang.ArrayUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

;

/**
 * 学生学籍
 * <code>StudentInfoServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-22 下午04:14:18
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentinfoservice")
public class StudentInfoServiceImpl extends BaseServiceImpl<StudentInfo> implements IStudentInfoService {
	public static final String STUDENTNO ="STUDENTNO";
	public static final String CERTNUM ="CERTNUM";
	@Autowired
	@Qualifier("orderCourseSettingService")
	private IOrderCourseSettingService orderCourseSettingService;//預約學習設置
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;//預約考試與畢業論文
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//JDBC 支持
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuchangeinfoservice;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictService;
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;//注入用户服务
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingguideplanservice;//年级教学计划服务

	@Autowired
	@Qualifier("userOperationLogsService")
	private IUserOperationLogsService userOperationLogsService;
	/*
	 * (non-Javadoc)
	 * @see com.hnjk.edu.roll.service.IStudentInfoService#getStudentOrderTime()
	 */
	@Override
	@Transactional(readOnly=true)
	public Map<String, Object> getStudentOrderTime() throws ServiceException{
		Map<String, Object> map = new HashMap<String, Object>();
		List<OrderCourseSetting> list = orderCourseSettingService.findByHql(" from "+OrderCourseSetting.class.getSimpleName()+" where isDeleted = ? and isOpened=? order by yearInfo.firstYear desc ", 0,"Y");
		if(null != list && list.size()>0){
			map.put("orderCourse", list.get(0));
		}
		
		List<ExamSub> list1 = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and  examsubStatus=? order by yearInfo.firstYear desc", 0,"2");
		if(null != list1 && list1.size()>0){
			for(ExamSub examSub:list1){
				if("exam".equals(examSub.getBatchType())){
					map.put("examSub", examSub);					
				}else{
					map.put("thesisSub", examSub);					
				}
			}
			
		}
		return map;
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findStuByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = findStudentInfo(condition, values);
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
		
	}
	private String findStudentInfo(Map<String, Object> condition,Map<String, Object> values) {
		StringBuffer hql = new StringBuffer(" from "+StudentInfo.class.getSimpleName()+" stu where isDeleted = :isDeleted ");
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		values.put("isDeleted", 0);
		hql.append(" and studentBaseInfo is not null ");
		if(condition.containsKey("branchSchool")){//学习中心
			hql.append(" and branchSchool.resourceid = :branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql.append(" and major.resourceid = :major ");
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql.append(" and classic.resourceid = :classic ");
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("classicCode")){//层次代码
			hql.append(" and classic.classicCode in ("+condition.get("classicCode")+") ");			
		}
		//查询时间需要使用原生sql
		/*if(condition.get("endDate")!=null && ExDateUtils.checkDateString(condition.get("endDate").toString())){//截止日期
			hql.append(" and resourceid in(select stuchange.studentid from(");
			hql.append(" select si.resourceid studentid,sci.changetype,sci.auditdate,nvl(sci.changebeforestudentstatus,si.studentstatus) studentstatus,");
			hql.append(" nvl2(sci.resourceid,row_number() over(partition by sci.studentid order by sci.auditdate),1) rn from edu_roll_studentinfo si");
			hql.append(" left join edu_roll_stuchangeinfo sci on sci.studentid=si.resourceid and sci.isdeleted=0 and sci.finalauditstatus='Y' and sci.changebeforestudentstatus is not null");
			hql.append(" and sci.auditdate>to_date('"+condition.get("endDate")+"','yyyy-mm-dd')");
			hql.append(" order by sci.studentid desc,sci.auditdate) stuchange where stuchange.rn=1");
			if(condition.containsKey("stuStatus")){//学籍状态
				sql.append(" and stuchange.studentstatus = :stuStatus");
				values.put("stuStatus", condition.get("stuStatus"));
			}
			sql.append(")");
		}else {*/
			if(condition.containsKey("stuStatus")){//学籍状态
				hql.append(" and studentStatus = :stuStatus ");
				values.put("stuStatus", condition.get("stuStatus"));
			}
		//}
		if(condition.containsKey("name")){//学生姓名
			hql.append(" and studentName like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("contactAddress")){//联系地址
			hql.append(" and studentBaseInfo.contactAddress like :contactAddress ");
			values.put("contactAddress", "%"+condition.get("contactAddress")+"%");
		}
		if(condition.containsKey("contactZipcode")){// 邮政编号
			hql.append(" and studentBaseInfo.contactZipcode = :contactZipcode ");
			values.put("contactZipcode", condition.get("contactZipcode"));
		}
		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			hql.append(" and studyNo like :matriculateNoticeNo ");
			values.put("matriculateNoticeNo","%"+ condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("studyNo")){//学号
			hql.append(" and studyNo = :studyNo ");
			values.put("studyNo",condition.get("studyNo"));
		}
		if(condition.containsKey("studyno")){//学号
			hql.append(" and studyNo like :studyno ");
			values.put("studyno","%"+condition.get("studyno")+"%");
		}
		if(condition.containsKey("gradeid")){//年级
			hql.append(" and grade.resourceid= :gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("orderCourseStatusFlag")){//预约课程状态
			hql.append(" and orderCourseStatus= :orderCourseStatusFlag ");
			values.put("orderCourseStatusFlag",Integer.parseInt(condition.get("orderCourseStatusFlag").toString()));
		}
		if(condition.containsKey("orderExamStatusFlag")){//预约考试状态
			hql.append(" and examOrderStatus= :orderExamStatusFlag ");
			values.put("orderExamStatusFlag",Integer.parseInt(condition.get("orderExamStatusFlag").toString()));
		}
		if (condition.containsKey("makeUpTeachingPlanOn")) {//学生教学计划补录
			hql.append(" and teachingPlan IS NULL ");
		}
		if(condition.containsKey("certNum")){//身份证号
			hql.append(" and  studentBaseInfo.certNum = :certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("gender")){//性别
			hql.append(" and studentBaseInfo.gender= :gender ");
			values.put("gender", condition.get("gender"));
		}
		if(condition.containsKey("mobile")){//手机号
			hql.append(" and (studentBaseInfo.contactPhone like :mobile or studentBaseInfo.mobile like :mobile)");
			values.put("mobile", "%"+condition.get("mobile")+"%");
		}
		if(condition.containsKey("schoolType")){//教学模式
			hql.append(" and teachingType = :schoolType ");		
			values.put("schoolType", condition.get("schoolType"));
		}
		if(condition.containsKey("teachingType")){//教学模式
			hql.append(" and teachingType = :teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("havePhoto")){			
			if(Constants.BOOLEAN_YES.equals(condition.get("havePhoto").toString())){
				hql.append(" and studentBaseInfo.photoPath is not null ");
			}else{
				hql.append(" and studentBaseInfo.photoPath is null ");
			}
		}
		//是否查询已注册账号的学籍
		if(condition.containsKey("isSysUser") && Constants.BOOLEAN_YES .equals(condition.get("isSysUser").toString())){
			hql.append(" and sysUser.resourceid IS NOT NULL ");
		}

		//是否查询符合毕业资格的学籍  graduationFilter
		if(condition.containsKey("graduationFilter")){			
			hql.append(" and resourceid in ("+condition.get("graduationFilter")+")");
		} 
		//是否查询学籍状态为毕业的学员 and 申请学位的同学们
		if(condition.containsKey("isThesis")){ 
			hql.append(" and studentStatus=:isThesis and isApplyGraduate=:isApplyGraduate");
			values.put("isThesis", condition.get("isThesis"));
			// 是否申请学位 Y-毕业+学位 N-毕业
			values.put("isApplyGraduate", condition.get("isApplyGraduate"));
		}
		//是否符合年级
		if(condition.containsKey("stuGrade")){
			hql.append(" and grade.resourceid = :grade ");
			values.put("grade", condition.get("stuGrade"));
		}
		//是否符合身份证号
		if(condition.containsKey("certNum")){
			hql.append(" and studentBaseInfo.certNum = :certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("isAbleOrderSubject")) {
			hql.append(" and isAbleOrderSubject = :isAbleOrderSubject ");
			values.put("isAbleOrderSubject",Integer.valueOf(condition.get("isAbleOrderSubject").toString()));
		}
		if (condition.containsKey("entranceFlag")) {
			hql.append(" and enterAuditStatus = :entranceFlag ");
			values.put("entranceFlag",condition.get("entranceFlag"));
		}
		//毕业维护管理，选择已通过毕业审核 但还没进行毕业录入的学员
		
		//目前允许使用的学籍状态为 在学 过期 延期 毕业四个状态 (但是显示的学籍状态需要之前使用过的学籍状态)
		//其中毕业资格审核需要过滤掉过期状态 毕业状态的 因为考虑到批量设置毕业日期 所以毕业状态的也要保留
		//最新的要求是学籍模块不包含毕业数据
		
		/*
		 * 放弃入学资格	20
			退学	13
			在学	11
			休学	12
			勒令退学	14
			开除学籍	15
			无学籍	19
			延期	21
			准毕业	22
			自动流失	17
			毕业	16
			学习期限已过	18
		 */
	
		hql.append(" and( studentStatus = :studentStatus1");
		values.put("studentStatus1", "11");//在学状态
		if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
			if(condition.containsKey("schoolrollstudentstatus")){
				hql.append(" or studentStatus in ("+condition.get("schoolrollstudentstatus")+")");
			}
		}
		hql.append(" or studentStatus in ('21','25'))");
		/*hql.append(" or studentStatus = :studentStatus12)");
		values.put("studentStatus12", "21");//延期状态*/		
		hql.append(" and studentStatus <> 16 and studentStatus <>24 "); //不查出结业毕业信息
		
		//传入勾选参数
		if(condition.containsKey("stus")){
			hql.append(" and resourceid in ("+condition.get("stus")+") ");	
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			hql.append(" and accountStatus = :accountStatus ");
			values.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));//无学籍状态
		}
		if(condition.containsKey("isGraduateQualifer")){
//			hql.append(" and (studentStatus ='11' or studentStatus ='21') ");
			hql.append(" and studentStatus in ('11','21','25') ");
			
			hql.append(" and teachingPlan is not null ");
		}
		if(condition.containsKey("isReachGraYear")){
			if("1".equals(condition.get("isReachGraYear"))){
				hql.append(" and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5-0.5))<=(0.5+to_number(:currentYearInfo))");
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}else if("0".equals(condition.get("isReachGraYear"))){
				hql.append(" and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5-0.5))>(0.5+to_number(:currentYearInfo))");
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}
		} 
		if(condition.containsKey("isPassEnter")){
			if("1".equals(condition.get("isPassEnter"))){
				hql.append(" and enterAuditStatus ='Y' ");
			}else if ("0".equals(condition.get("isPassEnter"))){
				hql.append(" and enterAuditStatus !='Y' ");
			}
		}  
		if(condition.containsKey("isApplyDelay")){
			if("1".equals(condition.get("isApplyDelay"))){
				hql.append(" and (isApplyGraduate= 'W' ) ");
			}else if ("0".equals(condition.get("isApplyDelay"))){
				hql.append(" and (isApplyGraduate= 'N' or isApplyGraduate='Y' ) ");
			}
			
		}   
		if(condition.containsKey("classesid")){//班级
			hql.append(" and classes.resourceid=:classesid ");
			values.put("classesid", condition.get("classesid"));
		}   
		
		if(condition.containsKey("classesMasterId") && condition.containsKey("teacherId")){
			hql.append(" and (classes.classesMasterId=:classesMasterId or exists(select tpct.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.resourceid=stu.classes.resourceid and tpct.teacherId=:teacherId)) ");
			values.put("classesMasterId", condition.get("classesMasterId"));
			values.put("teacherId", condition.get("teacherId"));
		}else {
			if(condition.containsKey("classesMasterId")){
				hql.append(" and classes.classesMasterId=:classesMasterId ");
				values.put("classesMasterId", condition.get("classesMasterId"));
			}
			if(condition.containsKey("teacherId")){
				hql.append(" and exists(select tpct.resourceid from TeachingPlanCourseTimetable tpct where tpct.isDeleted=0 and tpct.classes.resourceid=stu.classes.resourceid and tpct.teacherId=:teacherId) ");
				values.put("teacherId", condition.get("teacherId"));
			}
		}
		
		if(condition.containsKey("rollCard")){
			hql.append(" and rollCardStatus=:rollCard ");
			values.put("rollCard", condition.get("rollCard"));
		}
		
	/*	if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
//			hql += " and exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql +=" and rollCardStatus='Y'";
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
//			hql += " and not exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
			hql += " and not exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql +=" and (rollCardStatus!='Y' or rollCardStatus is null)";
		}*/
		// 有没有学籍移动医动的学生都应该显示出来
		//hql += " and not exists (from "+StuChangeInfo.class.getSimpleName()+" where studentInfo.resourceid=stu.resourceid and finalAuditStatus='N' and isDeleted=0)";
		//hql += " order by "+objPage.getOrderBy()+" " +objPage.getOrder() +",resourceid ,studentBaseInfo.contactAddress desc";
		if("10560".equals(schoolCode)){
			hql.append( " order by branchSchool.unitCode,grade.gradeName,studentName ");
		}else {
			hql.append( " order by  branchSchool.unitCode,grade.gradeName,classic.classicCode,major.majorCode,teachingType,studyNo ");
		}
		return hql.toString();
	}

	/**
	 * 根据条件获取学生学籍信息
	 * @param condition
	 * @param objPage
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Page findStudentInfoSecGraduate(Map<String, Object> condition, Page objPage) {
		Map<String, Object> values = new HashMap<String, Object>();
		String hql = " from "+GraduateData.class.getSimpleName()+" gd where isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		hql += " and gd.studentInfo.studentBaseInfo is not null  ";
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and gd.studentInfo.branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and gd.studentInfo.major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and gd.studentInfo.classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}		
		if(condition.containsKey("name")){//学生姓名
			hql += " and gd.studentInfo.studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("matriculateNoticeNo")){//学号
			hql += " and gd.studentInfo.studyNo like :studyno ";
			values.put("studyno","%"+condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("certNum")){//身份证号
			hql += " and  gd.studentInfo.studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("schoolType")){//教学模式
			hql += " and gd.studentInfo.teachingType = :schoolType ";		
			values.put("schoolType", condition.get("schoolType"));
		}
		if (condition.containsKey("teachingType")) {
			hql += " and gd.studentInfo.teachingType = :teachingType ";
			values.put("teachingType", condition.get("teachingType"));
		}
		//是否查询已注册账号的学籍
		if(condition.containsKey("isSysUser") && Constants.BOOLEAN_YES .equals(condition.get("isSysUser").toString())){
			hql += " and gd.studentInfo.sysUser.resourceid IS NOT NULL ";
		}
		//是否查询符合毕业资格的学籍  graduationFilter
		if(condition.containsKey("graduationFilter")){			
			hql += " and gd.studentInfo.resourceid in ("+condition.get("graduationFilter")+")";
		}
		if (condition.containsKey("entranceFlag")) {
			hql += " and gd.studentInfo.enterAuditStatus = :entranceFlag ";
			values.put("entranceFlag",condition.get("entranceFlag"));
		}
		//20160722 当传入的查询条件包含stuStatus时，查询结业状态的学生并且这些学生是符合结业换毕业的条件
		if(condition.containsKey("stuStatus") && ("24".equals(condition.get("stuStatus"))) || "27".equals(condition.get("stuStatus"))){//学籍状态
			hql += " and gd.studentInfo.studentStatus = :stuStatus ";
			values.put("stuStatus", condition.get("stuStatus"));			
			hql+= " and gd.isAllowSecGraduate = 'Y' ";
		}
		//是否符合年级
		if(condition.containsKey("gradeid")){
			hql += " and gd.studentInfo.grade.resourceid = :grade ";
			values.put("grade", condition.get("gradeid"));
		}				
		if(condition.containsKey("classesid")){//班级
			hql += " and gd.studentInfo.classes.resourceid=:classesid ";
			values.put("classesid", condition.get("classesid"));
		}
		if(condition.containsKey("havePhoto")){			
			if(Constants.BOOLEAN_YES.equals(condition.get("havePhoto").toString())){
				hql+=" and gd.studentInfo.studentBaseInfo.photoPath is not null ";
			}else{
				hql+=" and gd.studentInfo.studentBaseInfo.photoPath is null ";
			}
		}
		hql += " order by gd.studentInfo.branchSchool.unitCode,gd.studentInfo.classic.classicCode,gd.studentInfo.major.majorCode,gd.studentInfo.studyNo ";
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findStudentByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+StudentInfo.class.getSimpleName()+" stu where isDeleted = :isDeleted ");
		values.put("isDeleted", 0);
		hql.append(" and studentBaseInfo is not null  ");
		if(condition.containsKey("branchSchool")){//学习中心
			hql.append(" and branchSchool.resourceid = :branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql.append(" and major.resourceid = :major ");
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql.append(" and classic.resourceid = :classic ");
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("classicCode")){//层次代码
			hql.append(" and classic.classicCode in ("+condition.get("classicCode")+") ");			
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			hql.append(" and studentStatus = :stuStatus ");
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("name")){//学生姓名
			hql.append(" and studentName like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("contactAddress")){//联系地址
			hql.append(" and studentBaseInfo.contactAddress like :contactAddress ");
			values.put("contactAddress", "%"+condition.get("contactAddress")+"%");
		}
		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			hql.append(" and studyNo like :matriculateNoticeNo ");
			values.put("matriculateNoticeNo","%"+ condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("studyNo")){//学号
			hql.append(" and studyNo = :studyNo ");
			values.put("studyNo",condition.get("studyNo"));
		}
		if(condition.containsKey("gradeid")){//年级
			hql.append(" and grade.resourceid= :gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("orderCourseStatusFlag")){//预约课程状态
			hql.append(" and orderCourseStatus= :orderCourseStatusFlag ");
			values.put("orderCourseStatusFlag",Integer.parseInt(condition.get("orderCourseStatusFlag").toString()));
		}
		if(condition.containsKey("orderExamStatusFlag")){//预约考试状态
			hql.append(" and examOrderStatus= :orderExamStatusFlag ");
			values.put("orderExamStatusFlag",Integer.parseInt(condition.get("orderExamStatusFlag").toString()));
		}
		if (condition.containsKey("makeUpTeachingPlanOn")) {//学生教学计划补录
			hql.append(" and teachingPlan IS NULL ");
		}
		if(condition.containsKey("certNum")){//身份证号
			hql.append(" and  studentBaseInfo.certNum = :certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("gender")){//性别
			hql.append(" and studentBaseInfo.gender= :gender ");
			values.put("gender", condition.get("gender"));
		}
		if(condition.containsKey("schoolType")){//教学模式
			hql.append(" and teachingType = :schoolType ");		
			values.put("schoolType", condition.get("schoolType"));
		}
		
		//是否查询已注册账号的学籍
		if(condition.containsKey("isSysUser") && Constants.BOOLEAN_YES .equals(condition.get("isSysUser").toString())){
			hql.append(" and sysUser.resourceid IS NOT NULL ");
		}
		
		//是否查询符合毕业资格的学籍  graduationFilter
		if(condition.containsKey("graduationFilter")){			
			hql.append(" and resourceid in ("+condition.get("graduationFilter")+")");
		} 
		//是否查询学籍状态为毕业的学员 and 申请学位的同学们
		if(condition.containsKey("isThesis")){ 
			hql.append(" and studentStatus=:isThesis and isApplyGraduate=:isApplyGraduate");
			values.put("isThesis", condition.get("isThesis"));
			// 是否申请学位 Y-毕业+学位 N-毕业
			values.put("isApplyGraduate", condition.get("isApplyGraduate"));
		}
		//是否符合年级
		if(condition.containsKey("stuGrade")){
			hql.append(" and grade.resourceid = :grade ");
			values.put("grade", condition.get("stuGrade"));
		}
		//是否符合身份证号
		if(condition.containsKey("certNum")){
			hql.append(" and studentBaseInfo.certNum = :certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("isAbleOrderSubject")) {
			hql.append(" and isAbleOrderSubject = :isAbleOrderSubject ");
			values.put("isAbleOrderSubject",Integer.valueOf(condition.get("isAbleOrderSubject").toString()));
		}
		if (condition.containsKey("entranceFlag")) {
			hql.append(" and enterAuditStatus = :entranceFlag ");
			values.put("entranceFlag",condition.get("entranceFlag"));
		}
		//毕业维护管理，选择已通过毕业审核 但还没进行毕业录入的学员
		
		//目前允许使用的学籍状态为 在学 过期 延期 毕业四个状态 (但是显示的学籍状态需要之前使用过的学籍状态)
		//其中毕业资格审核需要过滤掉过期状态 毕业状态的 因为考虑到批量设置毕业日期 所以毕业状态的也要保留
		//最新的要求是学籍模块不包含毕业数据
		
		/*
		 * 放弃入学资格	20
			退学	13
			在学	11
			休学	12
			勒令退学	14
			开除学籍	15
			无学籍	19
			延期	21
			准毕业	22
			自动流失	17
			毕业	16
			学习期限已过	18
		 */
		
//		hql += " and( studentStatus = :studentStatus1";
//		values.put("studentStatus1", "11");//在学状态
		if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
			if(condition.containsKey("schoolrollstudentstatus")){
				hql.append(" or studentStatus in ("+condition.get("schoolrollstudentstatus")+")");
			}
		}
		
//		hql += " or studentStatus = :studentStatus12)";
//		values.put("studentStatus12", "21");//延期状态
//		hql += " and studentStatus <> 16 and studentStatus <>24 "; //不查出结业毕业信息
		
		//传入勾选参数
		if(condition.containsKey("stus")){
			hql.append(" and resourceid in ("+condition.get("stus")+") ");	
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			hql.append(" and accountStatus = :accountStatus ");
			values.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));//无学籍状态
		}
		if(condition.containsKey("isGraduateQualifer")){
//			hql.append(" and (studentStatus ='11' or studentStatus ='21') ");
			hql.append(" and studentStatus in ('11','21','25') ");
			
			hql.append(" and teachingPlan is not null ");
		}
		if(condition.containsKey("isReachGraYear")){
			if("1".equals(condition.get("isReachGraYear"))){
				hql.append(" and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5-0.5))<=(0.5+to_number(:currentYearInfo))");
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}else if("0".equals(condition.get("isReachGraYear"))){
				hql.append(" and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5-0.5))>(0.5+to_number(:currentYearInfo))");
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}
		} 
		if(condition.containsKey("isPassEnter")){
			if("1".equals(condition.get("isPassEnter"))){
				hql.append(" and enterAuditStatus ='Y' ");
			}else if ("0".equals(condition.get("isPassEnter"))){
				hql.append(" and enterAuditStatus !='Y' ");
			}
		}  
		if(condition.containsKey("isApplyDelay")){
			if("1".equals(condition.get("isApplyDelay"))){
				hql.append(" and (isApplyGraduate= 'W' ) ");
			}else if ("0".equals(condition.get("isApplyDelay"))){
				hql.append(" and (isApplyGraduate= 'N' or isApplyGraduate='Y' ) ");
			}
			
		}   
		if(condition.containsKey("classesid")){//班级
			hql.append(" and classes.resourceid=:classesid ");
			values.put("classesid", condition.get("classesid"));
		}   
		if(condition.containsKey("classesMasterId")){
			hql.append(" and classes.classesMasterId=:classesMasterId ");
			values.put("classesMasterId", condition.get("classesMasterId"));
		}
		if(condition.containsKey("rollCard")){
			hql.append(" and rollCardStatus=:rollCard ");
			values.put("rollCard", condition.get("rollCard"));
		}
		/*if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
//			hql += " and exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql.append(" and rollCardStatus='Y'");
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
//			hql += " and not exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and not exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql.append(" and (rollCardStatus!='Y' or rollCardStatus is null)");
		}*/
		if(condition.containsKey("havePhoto")){			
			if(Constants.BOOLEAN_YES.equals(condition.get("havePhoto").toString())){
				hql.append(" and studentBaseInfo.photoPath is not null ");
			}else{
				hql.append(" and studentBaseInfo.photoPath is null ");
			}
		}
//		hql += " and not exists (from "+StuChangeInfo.class.getSimpleName()+" where studentInfo.resourceid=stu.resourceid and finalAuditStatus='N' and isDeleted=0)";
		//hql += " order by "+objPage.getOrderBy()+" " +objPage.getOrder() +",resourceid ,studentBaseInfo.contactAddress desc";
		hql.append(" order by branchSchool.unitCode,classic.classicCode,major.majorCode,studyNo ");
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
		
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findStuByParam(Map<String, Object> condition, Page objPage)throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentInfo.class.getSimpleName()+" where isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		hql += " and studentBaseInfo is not null  ";
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classesid")){//班级
			hql += " and classes.resourceid = :classesid ";
			values.put("classesid", condition.get("classesid"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("classicCode")){//层次代码
			hql += " and classic.classicCode in ("+condition.get("classicCode")+") ";			
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			hql += " and studentStatus = :stuStatus ";
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("name")){//学生姓名
			hql += " and studentBaseInfo.name like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			hql += " and studyNo like :matriculateNoticeNo ";
			values.put("matriculateNoticeNo","%"+ condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("studyNo")){//学号
			hql += " and studyNo like :studyNo ";
			values.put("studyNo","%" + condition.get("studyNo") + "%");
		}
		if(condition.containsKey("gradeid")){//年级
			hql += " and grade.resourceid= :gradeid ";
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("orderCourseStatusFlag")){//预约课程状态
			hql += " and orderCourseStatus= :orderCourseStatusFlag ";
			values.put("orderCourseStatusFlag",Integer.parseInt(condition.get("orderCourseStatusFlag").toString()));
		}
		if(condition.containsKey("orderExamStatusFlag")){//预约考试状态
			hql += " and examOrderStatus= :orderExamStatusFlag ";
			values.put("orderExamStatusFlag",Integer.parseInt(condition.get("orderExamStatusFlag").toString()));
		}
		if (condition.containsKey("makeUpTeachingPlanOn")) {//学生教学计划补录
			hql += " and teachingPlan IS NULL ";
		}
		if(condition.containsKey("certNum")){//身份证号
			hql += " and  studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("gender")){//性别
			hql += " and studentBaseInfo.gender= :gender ";
			values.put("gender", condition.get("gender"));
		}
		if(condition.containsKey("schoolType")){//教学模式
			hql += " and teachingType = :schoolType ";		
			values.put("schoolType", condition.get("schoolType"));
		}
		//是否查询已注册账号的学籍
		if(condition.containsKey("isSysUser") && Constants.BOOLEAN_YES .equals(condition.get("isSysUser").toString())){
			hql += " and sysUser.resourceid IS NOT NULL ";
		}

		//是否查询符合毕业资格的学籍  graduationFilter
		if(condition.containsKey("graduationFilter")){			
			hql += " and resourceid in ("+condition.get("graduationFilter")+")";
		} 
		//是否查询学籍状态为毕业的学员 and 申请学位的同学们
		if(condition.containsKey("isThesis")){ 
			hql+=" and studentStatus=:isThesis and isApplyGraduate=:isApplyGraduate";
			values.put("isThesis", condition.get("isThesis"));
			// 是否申请学位 Y-毕业+学位 N-毕业
			values.put("isApplyGraduate", condition.get("isApplyGraduate"));
		}
		//是否符合年级
		if(condition.containsKey("stuGrade")){
			hql += " and grade.resourceid = :grade ";
			values.put("grade", condition.get("stuGrade"));
		}
		//排除的学籍ID(等效学生功能中排除选择的主学籍)
		if (condition.containsKey("mainStudentId")) {
			hql += " and resourceid <> :mainStudentId ";
			values.put("mainStudentId", condition.get("mainStudentId"));
		}
		//是否符合身份证号
		if(condition.containsKey("certNum")){
			hql += " and studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("isAbleOrderSubject")) {
			hql += " and isAbleOrderSubject = :isAbleOrderSubject ";
			values.put("isAbleOrderSubject",Integer.valueOf(condition.get("isAbleOrderSubject").toString()));
		}
		
		hql += " order by "+objPage.getOrderBy() +" "+ objPage.getOrder();
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
	}
	
	@Override
	public void enableStudentAccount(List<String> ids, boolean isEnabled) throws ServiceException {
		StudentInfo studentInfo = null;
		if (null != ids && ids.size() >0) {
			for(String id :ids){				
				studentInfo = get(id);
				if(null == studentInfo.getSysUser()){
					throw new ServiceException("学生:"+studentInfo.getStudentName()+" 未注册账号，请先注册！");
				}
				if(isEnabled){
					studentInfo.getSysUser().setEnabled(true);
					studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
				}else{
					studentInfo.getSysUser().setEnabled(false);
					studentInfo.setAccountStatus(Constants.BOOLEAN_FALSE);
				}
				update(studentInfo);
			}
			
		}
		
		
	}
	
	@Override
	public void resetStudentAccountPassword(String[] ids, String initPassword) throws ServiceException {
		if(ids!=null && ids.length>0){
			for (String id : ids) {
				if(ExStringUtils.isNotBlank(id)){
					StudentInfo studentInfo = get(id);
					if(null == studentInfo.getSysUser()){
						throw new ServiceException("学生:"+studentInfo.getStudentName()+" 未注册账号，请先注册！");
					}					
					studentInfo.getSysUser().setPassword(initPassword);
					update(studentInfo);
				}
			}
		}
		
	}
	
	/**
	 * 设置学生预约学习状态
	 */
	@Override
	@Transactional
	public boolean changeStuOrderCourseStatus(String resourceid,String configStatus,int status) throws ServiceException {
		String []ids = resourceid.split(",");
		try {
			for(int i=0;i<ids.length;i++){
				StudentInfo studentInfo = this.findUniqueByProperty("resourceid", ids[i]);
				if ("1".equals(configStatus)) {
					studentInfo.setOrderCourseStatus(status);
				}
				if ("2".equals(configStatus)) {
					studentInfo.setExamOrderStatus(status);
				}
				this.update(studentInfo);
			}
			return true;
		} catch (Exception e) {			
			throw new ServiceException(e.fillInStackTrace());
			
		}		
	}
	/**
	 * 设置学生预约毕业论文状态
	 * @param resourceid
	 * @param status
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public boolean changeStuGraduatePaperOrderStatus(String resourceid,int status) throws ServiceException {
		String []ids = resourceid.split(",");
		try {
			for(int i=0;i<ids.length;i++){
				StudentInfo studentInfo = this.findUniqueByProperty("resourceid", ids[i]);
				studentInfo.setIsAbleOrderSubject(status);
				this.update(studentInfo);
			}
			return true;
		} catch (Exception e) {			
			throw new ServiceException(e.fillInStackTrace());
			
		}	
	}
	/**
	 * 根据条件查找未预约第一学期课程的学生
	 */
	@Override
	@Transactional(readOnly=true)
	public List<StudentInfo> findNoFirstTermCourseStu(Map<String, Object> condition) throws ServiceException {
		
		StringBuffer hql = new StringBuffer("from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted = :isDeleted");	
		
		hql.append(" and stu.isOrderFirstCourse=:isOrderFirstCourse");
		hql.append(" and stu.teachingPlan.resourceid is not null");
		
		condition.put("isDeleted", 0);
		condition.put("isOrderFirstCourse", Constants.BOOLEAN_NO);
		
		if(condition.containsKey("gradeid")) {
			hql.append(" and stu.grade.resourceid=:gradeid");
		}
		if(condition.containsKey("majorid")) {
			hql.append(" and stu.major.resourceid=:majorid");
		}
		if(condition.containsKey("classic")) {
			hql.append(" and stu.classic.resourceid=:classic");
		}
		if(condition.containsKey("branchSchool")) {
			hql.append(" and stu.branchSchool.resourceid = :branchSchool");
		}
		if (condition.containsKey("schoolType")) {
			hql.append(" and stu.teachingPlan.schoolType= :schoolType ");
		}
 			
		
		if (condition.containsKey("name")) {
			hql.append(" and stu.studentBaseInfo.name=:name");
		}
		if (condition.containsKey("studyNo")) {
			hql.append(" and stu.studyNo=:studyNo");
		}
		

		return findByHql( hql.toString(),condition);
	}

	/**
	 * 根据传入的多个ID查找StudentInfo列表
	 * @param ids (xxxx,xxxx,xxxx)
	 */
	@Override
	@Transactional(readOnly=true)
	public List<StudentInfo> findStuListByIds(String ids) throws ServiceException {
		
		String newIds = "";
		if (null!=ids) {
			String[] idArray = ids.split(",");
			for (int i = 0; i < idArray.length; i++) {
				newIds += ",'"+idArray[i]+"'";
			}
			newIds = newIds.substring(1);
		}
		String hql = "from StudentInfo stu where stu.isDeleted=0";
		       hql += " and stu.resourceid in("+newIds+")";
		       
		return findByHql(hql);
	}

	@Override
	@Transactional(readOnly=true)
	public StudentInfo getStudentInfoByUser(User user) throws ServiceException {
		StudentInfo studentInfo = null;
		if(null!=user.getUserExtends()&&null!=user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
			String studentId = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			if(ExStringUtils.isNotEmpty(studentId)) {
				studentInfo = findUniqueByProperty("resourceid", studentId);
			}
		}
		/*String hql = " from "+StudentInfo.class.getSimpleName()+" si where si.isDeleted=0 and si.sysUser=? order by si.studentStatus ";
		List<StudentInfo> studentInfos = findByHql(hql, user);*/
		return studentInfo;
	}
	
	/**
	 * 根据系统用户ID获取所有对应的学籍
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentInfo> findStuListByUserId(String userId) throws ServiceException {
		String hql = " from "+StudentInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.sysUser.resourceid=? order by info.studyNo desc";
		return (List<StudentInfo>) exGeneralHibernateDao.findByHql(hql, userId);
	}

	//是否欠费
	@Override
	public boolean isSubscriberFee(String studentId) throws ServiceException {
		// TODO Auto-generated method stub
		return false;
	}
			
	
	/**
	 * 查询学生毕业条件完成情况
	 * @param studentInfo
	 * @return Map<String,String>
	 * lim 限选课是否完成  lim_s 已修读的限选课门数  lim_t 需要修读的限选课门数
	 * nes 必修课是否完成  nes_s 已修读的必修课总分  nes_t 需要修读的必修课总分
	 * tol 总分是否完成      tol_s 已修读的总分              tol_t 需要修读的总分
	 * app 是否申请延迟毕业  ent 是否通过入学资格审核  sta 是否符合毕业的学籍状态  year 是否达到毕业年限
	 * lim=Y and nes=Y and tol=Y and ent=Y and sta=Y and year=Y and app=N
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> getStudentGraduateAccomplishStatus(StudentInfo studentInfo) throws ServiceException {
		Date currentDate = ExDateUtils.getCurrentDateTime();
		List<YearInfo> list = yearInfoService.findAllByOrder();
		List<YearInfo> tmp = new ArrayList<YearInfo>(0);
		String currentYearInfo ="";
		for (int i = list.size()-1; i >=0; i--) {
			tmp.add(list.get(i));
		}
		list=tmp;
		tmp= new ArrayList<YearInfo>(0);
		for (YearInfo yearInfo : list) {
			if(currentDate.after(yearInfo.getFirstMondayOffirstTerm())){
				tmp.add(yearInfo);
			}else{
				break;
			}
		}
		if(tmp.size()>0){
			YearInfo tmpYear = tmp.get(tmp.size()-1);
			Date firstMondayOfSecondTerm = tmpYear.getFirstMondayOfSecondTerm();
			if(currentDate.after(firstMondayOfSecondTerm)){
				currentYearInfo = String.valueOf(1+(long)tmpYear.getFirstYear());//比如说为2011-11-11时 
			}else{
				currentYearInfo = String.valueOf(0.5+(long)tmpYear.getFirstYear());
			}	
		}
		
		Map<String,Object> resultMap = new HashMap<String, Object>(0);
		//1、年限要求  
		//2、申请毕业要求 
		//3、学籍状态要求
		//4、入学资格状态要求
		//5、总学分要求
		//6、必修课完成要求
		//7、限选课要求
		//String studentStatus = null!=studentInfo.get("studentstatus")?studentInfo.get("studentstatus").toString():"";
		TeachingPlan teachingplan = studentInfo.getTeachingPlan();
		Integer lim_s = studentInfo.getFinishedOptionalCourseNum();
		Integer lim_t = teachingplan.getOptionalCourseNum();
		lim_s = null!=lim_s?lim_s:0;
		lim_t = null!=lim_t?lim_t:0;
		resultMap.put("lim_s", lim_s);
		resultMap.put("lim_t", lim_t);
		if(lim_s>=lim_t){
			resultMap.put("lim", Constants.BOOLEAN_YES);
		} else{
			resultMap.put("lim", Constants.BOOLEAN_NO);
		}
		Double nes_s 		= studentInfo.getFinishedNecessCreditHour();//已完成必修课学分
		Double nes_t 	= teachingplan.getMustCourseTotalCreditHour();//教学计划要求总学分
		nes_s = null!=nes_s?nes_s:0;
		nes_t = null!=nes_t?nes_t:0;
		resultMap.put("nes_s", nes_s);
		resultMap.put("nes_t", nes_t );
		if(nes_s>=nes_t){
			resultMap.put("nes", Constants.BOOLEAN_YES);
		} else{
			resultMap.put("nes", Constants.BOOLEAN_NO);
		}
		Double tol_s = studentInfo.getFinishedCreditHour();//学生完成总学分
		Double tol_t = teachingplan.getMinResult();//教学计划要求最低毕业学分
		tol_s = null!=tol_s?tol_s:0;
		tol_t = null!=tol_t?tol_t:0;
		resultMap.put("tol_s", tol_s);
		resultMap.put("tol_t", tol_t);
		if(tol_s>=tol_t){
			resultMap.put("tol", Constants.BOOLEAN_YES);
		} else{
			resultMap.put("tol", Constants.BOOLEAN_NO);
		}
		String app_s =studentInfo.getIsApplyGraduate(); //申
		if(app_s.equals(Constants.BOOLEAN_WAIT)){
			resultMap.put("app", Constants.BOOLEAN_YES);
		}else{
			resultMap.put("app", Constants.BOOLEAN_NO);
		}
		String ent_s = studentInfo.getEnterAuditStatus();//入
		if(ent_s.equals(Constants.BOOLEAN_NO)){
			resultMap.put("ent", Constants.BOOLEAN_NO);
		}else{
			resultMap.put("ent", Constants.BOOLEAN_YES);
		}
		
		String sta_s = studentInfo.getStudentStatus();//学籍
		if(!"11".equals(sta_s) &&!"21".equals(sta_s)){
			resultMap.put("sta", Constants.BOOLEAN_YES);
		} else{
			resultMap.put("sta", Constants.BOOLEAN_NO);
		}
		
		Double length = Double.parseDouble(teachingplan.getEduYear().toString().substring(0,3).replace("年", ""));//学制
		Double gra_s = Double.parseDouble(studentInfo.getGrade().getYearInfo().getFirstYear().toString())+Long.parseLong(studentInfo.getGrade().getTerm().toString())*0.5;
		Double gra_t = Double.parseDouble(currentYearInfo)+0.5;
		length = null!=length?length:0;
		gra_s = null!=gra_s?gra_s:0;
		gra_t = null!=gra_t?gra_t:0;
		if(gra_s+length>gra_t){
			resultMap.put("year", Constants.BOOLEAN_NO);
		}else{
			resultMap.put("year", Constants.BOOLEAN_YES);
		}
	
		return resultMap;
	}

	/**
	 * 导出学籍表
	 * @param parameter
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	@Transactional(readOnly=true)	
	public List<StudentInfoVo> exportStudentList(Map<String, Object> parameter)	throws ServiceException {
		StringBuffer sql = new StringBuffer();
		parameter.put("isdeleted", 0);
		sql.append("select t.studentname as name,t.studyno as studentNo,b.gender as gender, b.certnum as certNum,b.nation,g.gradename as grade,")
		 	.append("c.classicname as classic,m.majorname as major,u.unitname as unit,t.studentstatus as inSchoolStatus,t.accoutstatus as accountStatus")
		 	.append(" from edu_roll_studentinfo t,edu_base_student b,edu_base_grade g,edu_base_classic c,edu_base_major m,hnjk_sys_unit u")
		 	.append(" where t.isdeleted = :isdeleted ")		 	
		 	.append(" and t.studentbaseinfoid=b.resourceid") 
		 	.append(" and t.gradeid=g.resourceid")  
		 	.append(" and t.classicid=c.resourceid ") 
		 	.append(" and t.majorid=m.resourceid ") 
		 	.append(" and t.branchschoolid=u.resourceid ");		    
		 	if(parameter.containsKey("stuStatus"))//学籍状态
			{
				sql.append(" and t.studentstatus = :stuStatus");
			}
		 	if(parameter.containsKey("branchSchool"))//学习中心
			{
				sql.append(" and  t.branchschoolid = :branchSchool");
			}
		 	if(parameter.containsKey("stuGrade"))//年级
			{
				sql.append(" and  t.gradeid = :stuGrade");
			}
		 	if(parameter.containsKey("classic"))//层次
			{
				sql.append(" and t.classicid = :classic");
			}
		 	if(parameter.containsKey("major"))//专业
			{
				sql.append(" and t.majorid = :major");
			}
		 	if(parameter.containsKey("studentIds"))//学籍ID
		 		//sql.append(" and t.resourceid in (:studentIds)");
			{
				sql.append(" and t.resourceid in ("+parameter.get("studentIds").toString()+")");
			}
		 	if(parameter.containsKey("name")){
		 		sql.append(" and t.name like :name ");
		 		String name = parameter.get("name").toString();
		 		parameter.remove("name");
		 		parameter.put("name", "%"+name+"%");
		 	}
		 	
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StudentInfoVo.class, parameter);
		} catch (Exception e) {
			throw new ServiceException("导出学生学籍信息出错:"+e.fillInStackTrace());
		} 		
		
	}
	
	/**
	 * 批量设置学籍、入学资格状态
	 * @param map
	 * @throws ServiceException
	 * @throws ParseException 
	 */
	@Override
	public void batchSetStatus(Map<String, Object> map) throws ServiceException, ParseException {
		
		User user                   = SpringSecurityHelper.getCurrentUser();
		Date date                   = ExDateUtils.getCurrentDateTime();
		String curDateStr           = ExDateUtils.formatDateStr(date, ExDateUtils.PATTREN_DATE_TIME);
		String flag			        = map.containsKey("flag")?map.get("flag").toString():"";
		String stus			        = map.containsKey("stuIds")?map.get("stuIds").toString():"";
		String changeToStatus       = map.containsKey("changeToStatus")?map.get("changeToStatus").toString():"";
		String totalCounts          = map.containsKey("totalCounts")?map.get("totalCounts").toString():"0";
		String extensionTime        = map.containsKey("extensionTime")?map.get("extensionTime").toString():"";
		String batchSetStatus_memo  = map.containsKey("batchSetStatus_memo")?map.get("batchSetStatus_memo").toString():"";
		Date extensionDate          = ExStringUtils.isNotBlank(extensionTime)?ExDateUtils.convertToDate(extensionTime):new Date();
		List<StudentInfo> list      = new ArrayList<StudentInfo>();
		List<StuChangeInfo> chl     = new ArrayList<StuChangeInfo>();
		StringBuilder loggerStr     = new StringBuilder();
		StringBuilder message       = new StringBuilder();
		StringBuilder message2      = new StringBuilder();
		
		List<Dictionary> typeList   = CacheAppManager.getChildren("CodeStuStatusToStuChangeTypeCode");
		Map<String,String> typeMap  = new HashMap<String, String>();
		for (Dictionary dict:typeList) {
			typeMap.put(dict.getDictName(), dict.getDictValue());
		}
		StringBuffer changeType     = new StringBuffer("");
		
		//设置选择的学生的状态
		if (ExStringUtils.isNotBlank(stus)) {
			
			String[] resids    = stus.split(",");
			list           	   = findByCriteria(Restrictions.in("resourceid", resids));
			if ("0".equals(flag)) {//设置入学资格
		
				for (StudentInfo stu :list) {
					
					if ("Y".equals(changeToStatus)) {
						TeachingPlan teachingPlan = null;
						teachingPlan = stu.getTeachingPlan();
						if (teachingPlan == null) {
							message2.append(stu.getStudentName() + "没有匹配的教学计划！<br/>");
						}
					}
					
					if (ExStringUtils.isNotBlank(stu.getEnterAuditStatus())&&stu.getEnterAuditStatus().equals(Constants.BOOLEAN_YES)){
						message.append(stu.getStudentName()+"入学资格已审核，不允许再操作！<br/>");
						continue;
					}
					
					String status_temp      = ExStringUtils.trimToEmpty(stu.getEnterAuditStatus());
					stu.setEnterAuditStatus(changeToStatus);

					loggerStr.append("批量设置入学资格审核状态，学号："+stu.getStudyNo()+"设置前状态："+status_temp+" 设置状态："+changeToStatus+
							         " 操作人:"+user.getCnName()+" 操作人ID:"+user.getResourceid()+" 时间:"+curDateStr+"/r/n");
				}
				
			}else if("1".equals(flag)){//设置学籍状态

				for (StudentInfo stu :list) {
					
					String status_temp      = ExStringUtils.trimToEmpty(stu.getStudentStatus());
					if ("11".equals(changeToStatus) || "21".equals(changeToStatus)) {
						if (null==stu.getSysUser()) {
							message.append("<font color='red'>"+stu.getStudentName()+"</font>,未关联系统用户,请联系系统管理人员</br>");
						}
						if (null!=stu.getSysUser()) {
							stu.getSysUser().setEnabled(true);
						}
						stu.setAccountStatus(Constants.BOOLEAN_TRUE);
						if("21".equals(changeToStatus)) {
							stu.setDelayDate(extensionDate);
						}
					}else {
						if(null!=stu.getSysUser()) {
							stu.getSysUser().setEnabled(false);
						}
						stu.setAccountStatus(Constants.BOOLEAN_FALSE);
					}
					StuChangeInfo changeInfo = new StuChangeInfo();
					changeInfo.setChangeBeforeStudentStatus(stu.getStudentStatus());
					stu.setStudentStatus(changeToStatus);
					
					//异动类型默认是C加设置的学籍状态
					changeType.setLength(0);
					changeType.append("C_"+changeToStatus);
					//如果字典CodeStuStatusToStuChangeTypeCode中有配置的话则取字典中的值       
					if(typeMap.containsKey(status_temp+":"+changeToStatus)){
						changeType.setLength(0);
						changeType.append(typeMap.get(status_temp+":"+changeToStatus));    
					}
					
					
					changeInfo.setStudentInfo(stu);
					changeInfo.setChangeType(changeType.toString());
					changeInfo.setApplicationDate(date);
					changeInfo.setReason(curDateStr+"批量设置学籍状态！");
					changeInfo.setFinalAuditStatus(Constants.BOOLEAN_YES);
					changeInfo.setAuditDate(date);
					changeInfo.setAuditMan(user.getCnName());
					changeInfo.setAuditManId(user.getResourceid());
					changeInfo.setMemo("从"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", status_temp)+"变动至"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", changeToStatus)+"备注信息:"+("23".equals(changeToStatus)?("转到"+batchSetStatus_memo+"学校"):batchSetStatus_memo)+("21".equals(changeToStatus)?"【延期至】"+extensionTime:""));
					
					chl.add(changeInfo);
					
					loggerStr.append("批量设置学籍状态，学号："+stu.getStudyNo()+"设置前状态："+status_temp+" 设置状态："+changeToStatus+
							         " 操作人:"+user.getCnName()+" 操作人ID:"+user.getResourceid()+" 时间:"+curDateStr+"/r/n");
				}
				
			}
		//根据查询根据设置学生的状态	
		}else {
			
			list			 = findStuByCondition(map);
			if (list.size()!=Integer.valueOf(totalCounts)) {
				throw new WebException("根据提供的条件查询到数据库中该条件下的记录数与页面中的不一至，请按相同的条件点击查询，再执行相应的批量设置操作！");
			}
			
			for (StudentInfo stu : list) {
				if ("0".equals(flag)) {//设置入学资格
					
					if (ExStringUtils.isNotBlank(stu.getEnterAuditStatus())&&stu.getEnterAuditStatus().equals(Constants.BOOLEAN_YES)){
						message.append(stu.getStudentName()+"入学资格已审核，不允许再操作！</br>");
						continue;
					}
					
					String status_temp      = ExStringUtils.trimToEmpty(stu.getEnterAuditStatus());
					stu.setEnterAuditStatus(changeToStatus);
					loggerStr.append("批量设置入学资格审核状态，学号："+stu.getStudyNo()+"设置前状态："+status_temp+" 设置状态："+changeToStatus+
							         " 操作人:"+user.getCnName()+" 操作人ID:"+user.getResourceid()+" 时间:"+curDateStr+"/r/n");
					
				}else if("1".equals(flag)) {//设置学籍状态
					
					String status_temp      = ExStringUtils.trimToEmpty(stu.getStudentStatus());
					stu.setStudentStatus(changeToStatus);
					if ("11".equals(changeToStatus) || "21".equals(changeToStatus)) {
					
						if (null==stu.getSysUser()) {
							message.append("<font color='red'>"+stu.getStudentName()+"</font>,未关联系统用户,请联系系统管理人员</br>");
						}
						if (null!=stu.getSysUser()) {
							stu.getSysUser().setEnabled(true);
						}
						stu.setAccountStatus(Constants.BOOLEAN_TRUE);
						if("21".equals(changeToStatus)) {
							stu.setDelayDate(extensionDate);
						}
					}else {
						if(null!=stu.getSysUser()) {
							stu.getSysUser().setEnabled(false);
						}
						stu.setAccountStatus(Constants.BOOLEAN_FALSE);
					}
					
					//异动类型默认是C加设置的学籍状态
					changeType.setLength(0);
					changeType.append("C_"+changeToStatus);
					//如果字典CodeStuStatusToStuChangeTypeCode中有配置的话则取字典中的值       
					if(typeMap.containsKey(status_temp+":"+changeToStatus)){
						changeType.setLength(0);
						changeType.append(typeMap.get(status_temp+":"+changeToStatus));    
					}
					
					StuChangeInfo changeInfo = new StuChangeInfo();
					changeInfo.setStudentInfo(stu);
					changeInfo.setChangeBeforeStudentStatus(status_temp);
					changeInfo.setChangeType(changeType.toString());
					changeInfo.setApplicationDate(date);
					changeInfo.setReason(curDateStr+"批量设置学籍状态！");
					changeInfo.setFinalAuditStatus(Constants.BOOLEAN_YES);
					changeInfo.setAuditDate(date);
					changeInfo.setAuditMan(user.getCnName());
					changeInfo.setAuditManId(user.getResourceid());
					changeInfo.setMemo("从"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", status_temp)+"变动至"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", changeToStatus));
					
					chl.add(changeInfo);
					
					loggerStr.append("批量设置学籍状态，学号："+stu.getStudyNo()+"设置前状态："+status_temp+" 设置状态："+changeToStatus+
							         " 操作人:"+user.getCnName()+" 操作人ID:"+user.getResourceid()+" 时间:"+curDateStr+"/r/n");
				}
			}
		}
		if (ExStringUtils.isNotBlank(message.toString())) {
			map.put("message",message.toString() + message2.toString());
		}
		if (ExStringUtils.isNotBlank(message2.toString())) {
			map.put("message2",message2.toString());
		}
		logger.debug(loggerStr.toString());	//记录日志
		stuchangeinfoservice.batchSaveOrUpdate(chl);
		List <StudentInfo> resultlist = new ArrayList<StudentInfo>(0);
		for (StudentInfo studentInfo : list) {
			studentInfo.setEnableLogHistory(true);
			resultlist.add(studentInfo);
		}
		batchSaveOrUpdate(resultlist);
	}
	/**
	 * 恢复学籍状态
	 * @param stuChangeInfoId
	 * @throws ServiceException
	 */
	@Override
	public void redoStudentStatus(String stuChangeInfoId,String settingDate,Map<String,Object> map) throws ServiceException, ParseException{
		StringBuilder loggerStr     = new StringBuilder();
		StringBuilder message       = new StringBuilder();
		StringBuffer changeType     = new StringBuffer("");
		User user                   = SpringSecurityHelper.getCurrentUser();
		Date date                   = ExDateUtils.getCurrentDateTime();
		String curDateStr           = ExDateUtils.formatDateStr(date, ExDateUtils.PATTREN_DATE_TIME);
		List<Dictionary> typeList   = CacheAppManager.getChildren("CodeStuStatusToStuChangeTypeCode");
		Map<String,String> typeMap  = new HashMap<String, String>();
		for (Dictionary dict:typeList) {
			typeMap.put(dict.getDictName(), dict.getDictValue());
		}
		StuChangeInfo stuchange = stuchangeinfoservice.get(stuChangeInfoId);
		StudentInfo studentInfo = stuchange.getStudentInfo();
		//获得所有的批量设置学籍状态的异动
		List<StuChangeInfo> stuChanges = stuchangeinfoservice.findByHql(" from "+StuChangeInfo.class.getSimpleName()+" where studentInfo.resourceid = ? and reason like ?  and finalAuditStatus ='Y' order by auditDate asc", new Object[]{stuchange.getStudentInfo().getResourceid(),"%批量设置学籍状态！%"});
		List<StuChangeInfo> sBefore = new ArrayList<StuChangeInfo>(0);
		List<StuChangeInfo> sBefore_d = new ArrayList<StuChangeInfo>(0);
		List<StuChangeInfo> sAfter  = new ArrayList<StuChangeInfo>(0);
		int flag = 0; 
		for (StuChangeInfo s : stuChanges) {
			if(stuchange.getResourceid().equals(s.getResourceid())){
				flag = 1 ;
				sAfter.add(s);
			}else{
				if(0==flag){
					sBefore.add(s);
					if(1==s.getIsDeleted()){
						sBefore_d.add(s);
					}
				}else if(1==flag){
					if(0==s.getIsDeleted()){
						sAfter.add(s);
					}
				}
			}
		}
		String status_before  = ExStringUtils.trimToEmpty(studentInfo.getStudentStatus());
		//后来的数据 可根据批量设定的异动前学籍状态获取
		String status_after = stuchange.getChangeBeforeStudentStatus();
		//之前的数据根据备注中的设定
		if(ExStringUtils.isEmpty(status_after)){
			String memo = stuchange.getMemo();
			memo = memo.substring(memo.indexOf("从")+1, memo.indexOf("变动至"));
			List<Dictionary> s = dictService.findByHql(" from "+Dictionary.class.getSimpleName()+" where isDeleted = 0 and dictCode like ?", new Object[]{"CodeStudentStatus%"});
			for (Dictionary dictionary : s) {
				if(memo.equals(dictionary.getDictName())){
					status_after = dictionary.getDictValue();
				}
			}
		}
		if(ExStringUtils.isNotEmpty(status_after)){
		//延期和休学状态就要另设日期
		studentInfo.setStudentStatus(status_after);
		if ("11".equals(status_after)||"21".equals(status_after)) {
			if (null==studentInfo.getSysUser()) {
				message.append("<font color='red'>"+studentInfo.getStudentName()+"</font>,未关联系统用户,请联系系统管理人员</br>");
			}
			if (null!=studentInfo.getSysUser()) {
				studentInfo.getSysUser().setEnabled(true);
			}
			studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
			if("21".equals(status_after)) {
				studentInfo.setDelayDate(ExDateUtils.convertToDate(settingDate));
			}
		}else {
			if(null!=studentInfo.getSysUser()) {
				studentInfo.getSysUser().setEnabled(false);
			}
			studentInfo.setAccountStatus(Constants.BOOLEAN_FALSE);
		}
		
		//异动类型默认是C加设置的学籍状态
		changeType.setLength(0);
		changeType.append("C_"+status_after);
		//如果字典CodeStuStatusToStuChangeTypeCode中有配置的话则取字典中的值       
		if(typeMap.containsKey(status_before+":"+status_after)){
			changeType.setLength(0);
			changeType.append(typeMap.get(status_before+":"+status_after));    
		}
		
//		StuChangeInfo changeInfo = new StuChangeInfo();
//		changeInfo.setStudentInfo(studentInfo);
//		changeInfo.setChangeType(changeType.toString());
//		changeInfo.setApplicationDate(date);
//		changeInfo.setReason(curDateStr+"(恢复)批量设置学籍状态！");
//		changeInfo.setFinalAuditStatus(Constants.BOOLEAN_YES);
//		changeInfo.setAuditDate(date);
//		changeInfo.setAuditMan(user.getCnName());
//		changeInfo.setAuditManId(user.getResourceid());
//		if("12".equals(status_after)){
//			changeInfo.setBackSchoolDate(ExDateUtils.convertToDate(settingDate));
//		}
//		changeInfo.setMemo("从"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", status_before)+"变动至"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", status_after));
//		loggerStr.append("批量设置学籍状态，学号："+studentInfo.getStudyNo()+"设置前状态："+status_before+" 设置状态："+status_after+
//		         " 操作人:"+user.getCnName()+" 操作人ID:"+user.getResourceid()+" 时间:"+curDateStr+"/r/n");
//		logger.debug(loggerStr.toString());	//记录日志
//		if (ExStringUtils.isNotBlank(message.toString())) {
//			map.put("message",message.toString());
//		}
//		stuchangeinfoservice.saveOrUpdate(changeInfo);
//		//恢复之前的
//		for (StuChangeInfo s2 : sBefore_d) {
//			s2.setIsDeleted(0);
//			stuchangeinfoservice.update(s2);
//		}
//		//删除本尊和之后的
//		stuchangeinfoservice.batchDelete(sAfter);
//		studentInfo.setEnableLogHistory(true);
//		saveOrUpdate(studentInfo);
		}
	}
	
	/**
	 * findStuByCondition方法的重载
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentInfo> findStuByCondition(Map<String, Object> condition)throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+StudentInfo.class.getSimpleName()+" stu where isDeleted = :isDeleted ";
		values.put("isDeleted", 0);
		hql += " and studentBaseInfo is not null  ";
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("classicCode")){//层次代码
			hql += " and classic.classicCode in ("+condition.get("classicCode")+") ";			
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			hql += " and studentStatus = :stuStatus ";
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("name")){//学生姓名
			hql += " and studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			hql += " and studyNo like :matriculateNoticeNo ";
			values.put("matriculateNoticeNo","%"+ condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("studyNo")){//学号
			hql += " and studyNo = :studyNo ";
			values.put("studyNo",condition.get("studyNo"));
		}
		if(condition.containsKey("gradeid")){//年级
			hql += " and grade.resourceid= :gradeid ";
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("orderCourseStatusFlag")){//预约课程状态
			hql += " and orderCourseStatus= :orderCourseStatusFlag ";
			values.put("orderCourseStatusFlag",Integer.parseInt(condition.get("orderCourseStatusFlag").toString()));
		}
		if(condition.containsKey("orderExamStatusFlag")){//预约考试状态
			hql += " and examOrderStatus= :orderExamStatusFlag ";
			values.put("orderExamStatusFlag",Integer.parseInt(condition.get("orderExamStatusFlag").toString()));
		}
		if (condition.containsKey("makeUpTeachingPlanOn")) {//学生教学计划补录
			hql += " and teachingPlan IS NULL ";
		}
		if(condition.containsKey("certNum")){//身份证号
			hql += " and  studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("gender")){//性别
			hql += " and studentBaseInfo.gender= :gender ";
			values.put("gender", condition.get("gender"));
		}
		if(condition.containsKey("schoolType")){//教学模式
			hql += " and teachingType = :schoolType ";		
			values.put("schoolType", condition.get("schoolType"));
		}
		//是否查询已注册账号的学籍
		if(condition.containsKey("isSysUser") && Constants.BOOLEAN_YES .equals(condition.get("isSysUser").toString())){
			hql += " and sysUser.resourceid IS NOT NULL ";
		}

		//是否查询符合毕业资格的学籍  graduationFilter
		if(condition.containsKey("graduationFilter")){			
			hql += " and resourceid in ("+condition.get("graduationFilter")+")";
		} 
		//是否查询学籍状态为毕业的学员 and 申请学位的同学们
		if(condition.containsKey("isThesis")){ 
			hql+=" and studentStatus=:isThesis and isApplyGraduate=:isApplyGraduate";
			values.put("isThesis", condition.get("isThesis"));
			// 是否申请学位 Y-毕业+学位 N-毕业
			values.put("isApplyGraduate", condition.get("isApplyGraduate"));
		}
		//是否符合年级
		if(condition.containsKey("stuGrade")){
			hql += " and grade.resourceid = :grade ";
			values.put("grade", condition.get("stuGrade"));
		}
		//是否符合身份证号
		if(condition.containsKey("certNum")){
			hql += " and studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("isAbleOrderSubject")) {
			hql += " and isAbleOrderSubject = :isAbleOrderSubject ";
			values.put("isAbleOrderSubject",Integer.valueOf(condition.get("isAbleOrderSubject").toString()));
		}
		if (condition.containsKey("entranceFlag")) {
			hql += " and enterAuditStatus = :entranceFlag ";
			values.put("entranceFlag",condition.get("entranceFlag"));
		}

		hql += " and( studentStatus = :studentStatus1";
		values.put("studentStatus1", "11");//在学状态

		if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
			if(condition.containsKey("schoolrollstudentstatus")){
				hql += " or studentStatus in ("+condition.get("schoolrollstudentstatus")+")";
			}

		}
		hql += " or studentStatus in ('21','25')) ";
		/*hql += " or studentStatus = :studentStatus12)";
		values.put("studentStatus12", "21");//延期状态
		 */		
		//传入勾选参数
		if(condition.containsKey("stus")){
			hql += " and resourceid in ("+condition.get("stus")+") ";	
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			hql += " and accountStatus = :accountStatus ";
			values.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));//无学籍状态
		}
		if(condition.containsKey("isGraduateQualifer")){
//			hql += " and (studentStatus ='11' or studentStatus ='21') ";
			hql += " and studentStatus in ('11','21') ";
			
			hql += " and teachingPlan is not null ";
		}
		if(condition.containsKey("isReachGraYear")){
			if("1".equals(condition.get("isReachGraYear"))){
				hql += " and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5+0.5))<=(0.5+to_number(:currentYearInfo))";
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}else if("0".equals(condition.get("isReachGraYear"))){
				hql += " and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5+0.5))>(0.5+to_number(:currentYearInfo))";
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}
		} 
		if(condition.containsKey("isPassEnter")){
			if("1".equals(condition.get("isPassEnter"))){
				hql += " and enterAuditStatus ='Y' ";
			}else if ("0".equals(condition.get("isPassEnter"))){
				hql += " and enterAuditStatus !='Y' ";
			}
		}  
		if(condition.containsKey("isApplyDelay")){
			if("1".equals(condition.get("isApplyDelay"))){
				hql += " and (isApplyGraduate= 'W' ) ";
			}else if ("0".equals(condition.get("isApplyDelay"))){
				hql += " and (isApplyGraduate= 'N' or isApplyGraduate='Y' ) ";
			}
			
		}   
		if(condition.containsKey("classesid")){//班级
			hql += " and classes.resourceid=:classesid ";
			values.put("classesid", condition.get("classesid"));
		}   
		if(condition.containsKey("rollCard")){
			hql += " and rollCardStatus=:rollCard ";
			values.put("rollCard", condition.get("rollCard"));
		}
		/*if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
//			hql += " and exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql +=" and rollCardStatus='Y'";
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
//			hql += " and not exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and not exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql +=" and (rollCardStatus!='Y' or rollCardStatus is null)";
		}*/
		
		return (List<StudentInfo>) exGeneralHibernateDao.findByHql(hql.toString(), values);
	}
	/**
	 * jdbc的方式查询学籍 /录取数据
	 */
	@Override
	public List findStuByConditionByJDBC(Map<String, Object> condition)throws ServiceException {
		Map<String, Object> map = new HashMap<String, Object>(0);
		StringBuffer sql = new StringBuffer();
		if(condition.containsKey("enroll")){
//			sql.append(" select examinee.resourceid from EDU_RECRUIT_EXAMINEE examinee inner join EDU_RECRUIT_ENROLLEEINFO enroll on enroll.EXAMCERTIFICATENO =examinee.ZKZH inner join edu_roll_studentinfo t on t.studyno = enroll.matriculatenoticeno ");
			sql.append(" select examinee.resourceid from EDU_RECRUIT_EXAMINEE examinee inner join EDU_RECRUIT_ENROLLEEINFO enroll on enroll.EXAMCERTIFICATENO =examinee.ZKZH and enroll.enrolleecode=examinee.KSH ");
			sql.append( " inner join edu_roll_studentinfo t on t.enrolleecode = enroll.enrolleecode and t.examcertificateno=enroll.examcertificateno ");
		}else{
			sql.append(" select count(0) rollcount from edu_roll_studentinfo t  ");
		}
		sql.append("   inner join edu_base_grade g on g.resourceid = t.gradeid" +
				" inner join edu_base_classic c on c.resourceid = t.classicid " +
				" inner join edu_base_major m on m.resourceid = t.majorid " +
				" inner join hnjk_sys_unit u on u.resourceid= t.branchschoolid " +
		        " inner join edu_base_student b on t.studentbaseinfoid = b.resourceid ");
		sql.append(" left join ( select resu.studentid,sum(1) num1 from edu_base_studentresume resu where resu.isdeleted = 0 group by resu.studentid ) aa on aa.studentid = b.resourceid ");
		sql.append(" left join ( select person.studentbaseinfoid,sum(1) num2 from edu_base_personanralation person where person.isdeleted = 0 group by person.studentbaseinfoid ) bb on bb.studentbaseinfoid = b.resourceid ");
		sql.append(" left join edu_base_year y on y.resourceid = g.yearid  ");
		sql.append(" left join edu_teach_plan tp on tp.resourceid = t.teachplanid ");
		
		sql.append(" where 1=1  ");
		sql.append(" and t.isdeleted= 0 ");
		sql.append(" and b.resourceid is not null ");
		if(condition.containsKey("branchSchool")){//学习中心
			sql.append(" and t.branchschoolid ='"+condition.get("branchSchool")+"' ");
		}
		if(condition.containsKey("major")){//专业
			sql.append(" and t.majorid ='"+condition.get("major")+"' ");
		}
		if(condition.containsKey("classic")){//层次
			sql.append(" and t.classicid ='"+condition.get("classic")+"' ");
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			sql.append(" and t.studentstatus ='"+condition.get("stuStatus")+"' ");
		}
		if(condition.containsKey("name")){//学生姓名
			sql.append(" and t.studentname ='"+condition.get("name")+"' ");
		}
		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			sql.append(" and t.studyno ='"+condition.get("matriculateNoticeNo")+"' ");
		}
		if(condition.containsKey("stuGrade")){//是否符合年级
			sql.append(" and t.gradeid ='"+condition.get("stuGrade")+"' ");
		}
		if(condition.containsKey("certNum")){//身份证号
			sql.append(" and b.certnum ='"+condition.get("certNum")+"' ");
		}
		if (condition.containsKey("entranceFlag")) {
			sql.append(" and t.enterauditstatus ='"+condition.get("entranceFlag")+"' ");
		}
		if(condition.containsKey("classesid")){//班级
			sql.append(" and t.classesid ='"+condition.get("classesid")+"' ");
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			sql.append(" and t.accoutstatus ='"+condition.get("accountStatus")+"' ");		
		}
		if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
			if(condition.containsKey("schoolrollstudentstatus")){
				if (condition.containsKey("from")) {
					sql.append(" and( t.studentStatus = '11' or t.studentStatus = '16' or t.studentstatus = '21' or t.studentstatus = '25' or t.studentStatus in ("+condition.get("schoolrollstudentstatus")+"))");
				}else{
					sql.append(" and( t.studentStatus = '11' or t.studentstatus = '21' or t.studentstatus = '25' or t.studentStatus in ("+condition.get("schoolrollstudentstatus")+"))");
				}
			}

		}
		
		if(condition.containsKey("rollCard")){
			sql.append(" and t.rollcardstatus ='"+condition.get("rollCard")+"' ");
		}
		/*if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
	 		sql.append(" and nvl(aa.num1,0) > 0 ");
			sql.append(" and nvl(bb.num2,0) > 0 ");
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
			sql.append(" and nvl(aa.num1,0) = 0 ");
			sql.append(" and nvl(bb.num2,0) = 0 ");
		}   */
		List result = new ArrayList(0);
		try {
			result = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 学籍卡保存
	 * @param request
	 * @throws ServiceException
	 */
	@Override
	public void studentRollInfoCardSave(HttpServletRequest request,Map<String,Object> map,StudentInfo studentInfo)throws ServiceException {

		User user 			  = SpringSecurityHelper.getCurrentUser();
		
		String residence      = ExStringUtils.trimToEmpty(request.getParameter("residence"));
		String homePlace      = ExStringUtils.trimToEmpty(request.getParameter("homePlace"));
		String mobile   	  = ExStringUtils.trimToEmpty(request.getParameter("mobile"));
		String email   		  = ExStringUtils.trimToEmpty(request.getParameter("email"));
		String homeAddress    = ExStringUtils.trimToEmpty(request.getParameter("homeAddress"));
		String homezipCode    = ExStringUtils.trimToEmpty(request.getParameter("homezipCode"));
		String homePhone      = ExStringUtils.trimToEmpty(request.getParameter("homePhone"));
		String officeName     = ExStringUtils.trimToEmpty(request.getParameter("officeName"));
		String officeZipcode  = ExStringUtils.trimToEmpty(request.getParameter("officeZipcode"));
		String officePhone    = ExStringUtils.trimToEmpty(request.getParameter("officePhone"));
		String officeTitle = ExStringUtils.trimToEmpty(request.getParameter("officeTitle"));
		String politics       = ExStringUtils.trimToEmpty(request.getParameter("politics"));
		String marriage       = ExStringUtils.trimToEmpty(request.getParameter("marriage"));
		String organizationDate = ExStringUtils.trimToEmpty(request.getParameter("organizationDate"));
		String joinPartyDate = ExStringUtils.trimToEmpty(request.getParameter("joinPartyDate"));
		//个人简历
		String[] startYears   = request.getParameterValues("startYear");
		String[] startMonths  = request.getParameterValues("startMonth");
		String[] endYears     = request.getParameterValues("endYear");
		String[] endMonths    = request.getParameterValues("endMonth");
		String[] companys     = request.getParameterValues("company");
		String[] title 		  = request.getParameterValues("title");
		
		//家庭主要成员
		String[] f_name       = request.getParameterValues("f_name");
		String[] f_gender     = request.getParameterValues("f_gender");
		String[] f_ralation   = request.getParameterValues("f_ralation");
		String[] f_workPlace  = request.getParameterValues("f_workPlace");
		String[] f_title      = request.getParameterValues("f_title");
		String[] f_contact    = request.getParameterValues("f_contact");
		
		//主要社会关系
		String[] s_name       = request.getParameterValues("s_name");
		String[] s_ralation   = request.getParameterValues("s_ralation");
		String[] s_workPlace  = request.getParameterValues("s_workPlace");
		String[] s_contact    = request.getParameterValues("s_contact");
		
		//自我鉴定
		String selfAssessment = request.getParameter("selfAssessment");
		if (user.isContainRole("ROLE_STUDENT")) {
			
			user                            = userService.get(user.getResourceid());
			if(ExBeanUtils.isNullOfAll(studentInfo)){
				studentInfo 	    = getStudentInfoByUser(user);
			}
			
			StudentBaseInfo studentBaseInfo = studentInfo.getStudentBaseInfo();
			
			UserExtends us 	  				= null;
			if(null!=user.getUserExtends()&&user.getUserExtends().containsKey(UserExtends.USER_EXTENDCODE_ROLLCARDSUBMITTIME)){
				us            				= user.getUserExtends().get(UserExtends.USER_EXTENDCODE_ROLLCARDSUBMITTIME);
			}else {
				us            				= new UserExtends();
			}
			us.setUser(user);
			us.setExCode(UserExtends.USER_EXTENDCODE_ROLLCARDSUBMITTIME);
			
			try {
				us.setExValue(ExDateUtils.formatDateStr(ExDateUtils.getCurrentDateTime(),ExDateUtils.PATTREN_DATE));
			} catch (ParseException e) {
				
			}
			user.getUserExtends().put(UserExtends.USER_EXTENDCODE_ROLLCARDSUBMITTIME, us);

			studentInfo.setSelfAssessment(selfAssessment);
			studentInfo.setSysUser(user);
			try {
				if(ExStringUtils.isNotBlank(organizationDate)){
					studentInfo.setOrganizationDate(ExDateUtils.parseDate(organizationDate, ExDateUtils.PATTREN_DATE));
				}else{
					studentInfo.setOrganizationDate(null);
				}
				if(ExStringUtils.isNotBlank(joinPartyDate)){
					studentInfo.setJoinPartyDate(ExDateUtils.parseDate(joinPartyDate, ExDateUtils.PATTREN_DATE));
				}else{
					studentInfo.setJoinPartyDate(null);
				}
			} catch (ParseException e) {
				logger.error("入团或入党时间设置出错");
				e.printStackTrace();
			}
			studentBaseInfo.setResidence(residence);
			studentBaseInfo.setMobile(mobile);
			studentBaseInfo.setEmail(email);
			studentBaseInfo.setHomeAddress(homeAddress);
			studentBaseInfo.setHomezipCode(homezipCode);
			studentBaseInfo.setHomePhone(homePhone);
			studentBaseInfo.setOfficeName(officeName);
			studentBaseInfo.setOfficeZipcode(officeZipcode);
			studentBaseInfo.setOfficePhone(officePhone);
			studentBaseInfo.setTitle(officeTitle);
			studentBaseInfo.setHomePlace(homePlace);
			studentBaseInfo.setPolitics(politics);
			studentBaseInfo.setMarriage(marriage);
			//增加上传身份证复印件  20161108
			studentBaseInfo.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPath")));
			studentBaseInfo.setCertPhotoPathReverse(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPathReverse")));
			
			
			Set<PersonalRalation> ralations = studentBaseInfo.getStudentRalation();
			Set<StudentResume>    resumes   = studentBaseInfo.getStudentResume();
			ralations.clear();
			resumes.clear();
			
			//个人简历
			if (null!=startYears&&null!=startMonths&&null!=endYears&&null!=endMonths&&null!=companys&&null!=title) {
				
				for (int i = 0; i < startYears.length; i++) {
					if (ExStringUtils.isNotBlank(startYears[i])&&
						ExStringUtils.isNotBlank(startMonths[i])&&
						ExStringUtils.isNotBlank(endYears[i])&&
						ExStringUtils.isNotBlank(endMonths[i])&&
						ExStringUtils.isNotBlank(companys[i])&&
						ExStringUtils.isNotBlank(title[i])) {
						
						StudentResume res   = new StudentResume();	
						
						res.setStudent(studentBaseInfo);
						res.setStartYear(Long.valueOf(startYears[i]));
						res.setStartMonth(Long.valueOf(startMonths[i]));
						res.setEndYear(Long.valueOf(endYears[i]));
						res.setEndMonth(Long.valueOf(endMonths[i]));
						res.setCompany(companys[i]);
						res.setTitle(title[i]);
						
						resumes.add(res);
					}
				}
			}
			//主要家庭关系
			if (null!=f_name&&null!=f_ralation&&null!=f_workPlace&&null!=f_contact) {
				for (int i = 0; i < f_name.length; i++) {
					
					if (ExStringUtils.isNotBlank(f_name[i])&&
						ExStringUtils.isNotBlank(f_gender[i])&&
						ExStringUtils.isNotBlank(f_ralation[i])&&
						ExStringUtils.isNotBlank(f_workPlace[i])&&
						ExStringUtils.isNotBlank(f_title[i])&&
						ExStringUtils.isNotBlank(f_contact[i])) {

						PersonalRalation ralation = new PersonalRalation();
						
						ralation.setStudentBaseInfo(studentBaseInfo);
						ralation.setName(f_name[i]);
						ralation.setGender(f_gender[i]);
						ralation.setRalationType(PersonalRalation.RALATIONTYPE_F);
						ralation.setRalation(f_ralation[i]);
						ralation.setWorkPlace(f_workPlace[i]);
						ralation.setTitle(f_title[i]);
						ralation.setContact(f_contact[i]);
						
						ralations.add(ralation);
					}
				}
			}
			//主要社会关系 
			if (null!=s_name&&null!=s_ralation&&null!=s_workPlace&&null!=s_contact) {
				
				for (int i = 0; i < s_name.length; i++) {
					
					if (ExStringUtils.isNotBlank(s_name[i])&&ExStringUtils.isNotBlank(s_ralation[i])
							&&ExStringUtils.isNotBlank(s_workPlace[i])
							/*&&ExStringUtils.isNotBlank(s_contact[i])*/) {

						PersonalRalation ralation = new PersonalRalation();
						
						ralation.setStudentBaseInfo(studentBaseInfo);
						ralation.setName(s_name[i]);
						ralation.setRalationType(PersonalRalation.RALATIONTYPE_S);
						ralation.setRalation(s_ralation[i]);
						ralation.setWorkPlace(s_workPlace[i]);
						ralation.setContact(s_contact[i]);
						
						ralations.add(ralation);
					}
				}
			}
			
			if (resumes.size()>0) {
				studentBaseInfo.setStudentResume(resumes);
			}
			if (ralations.size()>0) {
				studentBaseInfo.setStudentRalation(ralations);
			}
			   
			studentInfo.setStudentBaseInfo(studentBaseInfo);
			
			studentService.update(studentBaseInfo);
			update(studentInfo);
			userService.update(user);
			
		}else {
			map.put("statusCode",300);
			map.put("message", "当前用户不能为学生录入个人信息！");
		}
		
	}
	
	//审核已修改的学籍信息(以后可能在这功能里加其他条件，独立搞这个实现方法)
	@Override
	@Transactional(readOnly=true)
	public Page findStuAuditResultsByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		//String hql = " from "+StudentInfo.class.getSimpleName()+" stu where isDeleted = :isDeleted and  AUDITRESULTS=null";
		String hql = " from "+StudentInfo.class.getSimpleName()+" stu where isDeleted = :isDeleted";
		values.put("isDeleted", 0);
		hql += " and studentBaseInfo is not null  ";
		if(condition.containsKey("branchSchool")){//学习中心
			hql += " and branchSchool.resourceid = :branchSchool ";
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){//专业
			hql += " and major.resourceid = :major ";
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){//层次
			hql += " and classic.resourceid = :classic ";
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("classicCode")){//层次代码
			hql += " and classic.classicCode in ("+condition.get("classicCode")+") ";			
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			hql += " and studentStatus = :stuStatus ";
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("name")){//学生姓名
			hql += " and studentName like :name ";
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("contactAddress")){//联系地址
			hql +=" and studentBaseInfo.contactAddress like :contactAddress ";
			values.put("contactAddress", "%"+condition.get("contactAddress")+"%");
		}
		if(condition.containsKey("auditResults")){//审核结果
			hql +=" and auditResults like :auditResults ";
			values.put("auditResults", "%"+condition.get("auditResults")+"%");
		}
		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			hql += " and studyNo like :matriculateNoticeNo ";
			values.put("matriculateNoticeNo","%"+ condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("studyNo")){//学号
			hql += " and studyNo = :studyNo ";
			values.put("studyNo",condition.get("studyNo"));
		}
		if(condition.containsKey("gradeid")){//年级
			hql += " and grade.resourceid= :gradeid ";
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("orderCourseStatusFlag")){//预约课程状态
			hql += " and orderCourseStatus= :orderCourseStatusFlag ";
			values.put("orderCourseStatusFlag",Integer.parseInt(condition.get("orderCourseStatusFlag").toString()));
		}
		if(condition.containsKey("orderExamStatusFlag")){//预约考试状态
			hql += " and examOrderStatus= :orderExamStatusFlag ";
			values.put("orderExamStatusFlag",Integer.parseInt(condition.get("orderExamStatusFlag").toString()));
		}
		if (condition.containsKey("makeUpTeachingPlanOn")) {//学生教学计划补录
			hql += " and teachingPlan IS NULL ";
		}
		if(condition.containsKey("certNum")){//身份证号
			hql += " and  studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("gender")){//性别
			hql += " and studentBaseInfo.gender= :gender ";
			values.put("gender", condition.get("gender"));
		}
		if(condition.containsKey("schoolType")){//教学模式
			hql += " and teachingType = :schoolType ";		
			values.put("schoolType", condition.get("schoolType"));
		}
		
		//是否查询已注册账号的学籍
		if(condition.containsKey("isSysUser") && Constants.BOOLEAN_YES .equals(condition.get("isSysUser").toString())){
			hql += " and sysUser.resourceid IS NOT NULL ";
		}

		//是否查询符合毕业资格的学籍  graduationFilter
		if(condition.containsKey("graduationFilter")){			
			hql += " and resourceid in ("+condition.get("graduationFilter")+")";
		} 
		//是否查询学籍状态为毕业的学员 and 申请学位的同学们
		if(condition.containsKey("isThesis")){ 
			hql+=" and studentStatus=:isThesis and isApplyGraduate=:isApplyGraduate";
			values.put("isThesis", condition.get("isThesis"));
			// 是否申请学位 Y-毕业+学位 N-毕业
			values.put("isApplyGraduate", condition.get("isApplyGraduate"));
		}
		//是否符合年级
		if(condition.containsKey("stuGrade")){
			hql += " and grade.resourceid = :grade ";
			values.put("grade", condition.get("stuGrade"));
		}
		//是否符合身份证号
		if(condition.containsKey("certNum")){
			hql += " and studentBaseInfo.certNum = :certNum ";
			values.put("certNum", condition.get("certNum"));
		}
		if (condition.containsKey("isAbleOrderSubject")) {
			hql += " and isAbleOrderSubject = :isAbleOrderSubject ";
			values.put("isAbleOrderSubject",Integer.valueOf(condition.get("isAbleOrderSubject").toString()));
		}
		if (condition.containsKey("entranceFlag")) {
			hql += " and enterAuditStatus = :entranceFlag ";
			values.put("entranceFlag",condition.get("entranceFlag"));
		}
		hql += " and( studentStatus = :studentStatus1";
		values.put("studentStatus1", "11");//在学状态
		if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
			if(condition.containsKey("schoolrollstudentstatus")){
				hql += " or studentStatus in ("+condition.get("schoolrollstudentstatus")+")";
			}
		}
		hql += " or studentStatus in ('21','25')) ";
		/*hql += " or studentStatus = :studentStatus12)";
		values.put("studentStatus12", "21");//延期状态
*/		
		//传入勾选参数
		if(condition.containsKey("stus")){
			hql += " and resourceid in ("+condition.get("stus")+") ";	
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			hql += " and accountStatus = :accountStatus ";
			values.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));//无学籍状态
		}
		if(condition.containsKey("isGraduateQualifer")){
//			hql += " and (studentStatus ='11' or studentStatus ='21') ";
			hql += " and studentStatus in ('11','21','25') ";
			
			hql += " and teachingPlan is not null ";
		}
		if(condition.containsKey("isReachGraYear")){
			if("1".equals(condition.get("isReachGraYear"))){
				hql += " and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5+0.5))<=(0.5+to_number(:currentYearInfo))";
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}else if("0".equals(condition.get("isReachGraYear"))){
				hql += " and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5+0.5))>(0.5+to_number(:currentYearInfo))";
				values.put("currentYearInfo", condition.get("currentYearInfo"));
			}
		} 
		if(condition.containsKey("isPassEnter")){
			if("1".equals(condition.get("isPassEnter"))){
				hql += " and enterAuditStatus ='Y' ";
			}else if ("0".equals(condition.get("isPassEnter"))){
				hql += " and enterAuditStatus !='Y' ";
			}
		}  
		if(condition.containsKey("isApplyDelay")){
			if("1".equals(condition.get("isApplyDelay"))){
				hql += " and (isApplyGraduate= 'W' ) ";
			}else if ("0".equals(condition.get("isApplyDelay"))){
				hql += " and (isApplyGraduate= 'N' or isApplyGraduate='Y' ) ";
			}
			
		}   
		if(condition.containsKey("classesid")){//班级
			hql += " and classes.resourceid=:classesid ";
			values.put("classesid", condition.get("classesid"));
		}   
		
		if(condition.containsKey("rollCard")){
			hql += " and rollCardStatus=:rollCard ";
			values.put("rollCard", condition.get("rollCard"));
		}
		/*if (condition.containsKey("rollCard")&&Constants.BOOLEAN_YES.equals(condition.get("rollCard").toString())) {
//			hql += " and exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql +=" and rollCardStatus='Y'";
		}else if(condition.containsKey("rollCard")&&Constants.BOOLEAN_NO.equals(condition.get("rollCard").toString())) {
//			hql += " and not exists ( from "+StudentResume.class.getSimpleName()+" where student=stu.studentBaseInfo )";
//			hql += " and not exists ( from "+PersonalRalation.class.getSimpleName()+" where studentBaseInfo=stu.studentBaseInfo )";
			hql +=" and (rollCardStatus!='Y' or rollCardStatus is null)";
		}*/
		hql += " order by "+objPage.getOrderBy()+" " +objPage.getOrder() +",resourceid ,studentBaseInfo.contactAddress desc";
		return exGeneralHibernateDao.findByHql(objPage, hql, values);
		
	}
	
	@Override
	public List<StudentInfo> findStuListByRid(String rid) throws ServiceException ,Exception{
		Map<String,Object> condition = new HashMap<String,Object>();
		String sql = "select * from EDU_BASE_STUDENT t where  t.resourceid=:rid";
		//String sql = "select stuchange.* from edu_roll_studentinfo t left join edu_roll_stuchangeinfo stuchange on t.resourceid=stuchange.studentid where t.studyno=:userId";
		condition.put("rid", rid);
		List<StudentInfo> btList = null;
		if(ExStringUtils.isNotEmpty(rid)){
			btList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql, StudentInfo.class, condition);
		}
		return btList;
	}
	
	/**
	 * 退回学籍信息卡
	 * @param studyNo
	 * @return 
	 * @throws ServiceException
	 */
	@Override
	public Map<String,Object> backStudenInfoCard(String studyNo)throws ServiceException{
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select * from edu_roll_studentinfo info where info.resourceid in("+studyNo+") ");
		String mes = "退回学籍信息卡失败！";
		Integer isSuccess = 300;
		try {
			List<StudentInfo> results = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StudentInfo.class, map);
			if(null != results && results.size()>0){
				sql.setLength(0);
				sql.append(" update edu_roll_studentinfo info set info.rollcardstatus = '1' where info.resourceid in("+studyNo+") and info.rollcardstatus='2' ");
				int i = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql.toString(),map);
				if( i > 1){
					mes = i+"个学生成功退回学籍信息卡！";
				}else if(i>0){
					mes = "退回学籍信息卡成功！";
				}else{
					mes = "学籍卡未提交，无需退回！";
				}
				isSuccess = 200;
			}else{
				mes = "无法找到学生信息！";
			}
		} catch (Exception e) {
			mes = "退回学籍操作异常！";
			e.printStackTrace();
		}
		map.put("mes",mes);
		map.put("isSuccess", isSuccess);
		return map;
	};
	
	@Override
	@Transactional(readOnly=true)	
	public int getStudentNum(Map<String, Object> parameter)	throws ServiceException {
		int num = 0;
		StringBuffer sql = new StringBuffer();
		Map<String, Object> map = new HashMap<String, Object>(); 
		sql.append("select count(*) c from edu_roll_studentinfo s where s.isdeleted=0");
		if(parameter.containsKey("brSchoolId")){
			sql.append(" and s.branchschoolid=:brSchoolId");
		}
		if(parameter.containsKey("gradeId")){
			sql.append(" and s.gradeid=:gradeId");
		}
		if(parameter.containsKey("classicId")){
			sql.append(" and s.classicid=:classicId");
		}
		if(parameter.containsKey("teachingType")){
			sql.append(" and s.teachingType=:teachingType");
		}
		if(parameter.containsKey("majorId")){
			sql.append(" and s.majorid=:majorId");
		}
	 	if(parameter.containsKey("classesid")) {
	 		map.put("classesid", parameter.get("classesid"));
	 		sql.append(" and s.classesid=:classesid ");
	 	}else if (parameter.containsKey("classesId")) {
	 		map.put("classesId", parameter.get("classesId"));
	 		sql.append(" and s.classesid=:classesId ");
		}
	 	if(!parameter.containsKey("stuStatus")) {
	 		parameter.put("stuStatus", "11");
	 	}
	 	sql.append(" and s.studentStatus in('"+parameter.get("stuStatus")+"')");
		try {
			List<Map<String, Object>> results = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), map);
			if (results.size() > 0) {
				num = Integer.parseInt(results.get(0).get("c").toString());
			}
		} catch (Exception e) {
			throw new ServiceException("出错:"+e.fillInStackTrace());
		} 		
		return num;
	}
	
	@Override
	public void joinTeachingPlan(String[] ids) throws ServiceException {
		if (ids != null && ids.length > 0) {
			for (String id : ids) {
				StudentInfo stu = this.get(id);
				if (stu.getTeachingPlan() != null) {
					continue;
				}
				String gradeId = stu.getGrade().getResourceid();
				String majorId = stu.getMajor().getResourceid();
				String classicId = stu.getClassic().getResourceid();
				String teachingType = stu.getTeachingType();
				//获取学生的当前年级教学计划,年级+专业+层次+教学类型			
				List<TeachingGuidePlan> guidePlan = teachingguideplanservice.findByHql("from TeachingGuidePlan guidePlan where guidePlan.grade.resourceid=? " +
						" and guidePlan.ispublished='Y' and guidePlan.teachingPlan.major.resourceid=? " +
						" and guidePlan.teachingPlan.classic.resourceid=? and guidePlan.teachingPlan.schoolType=? ", 
						new String[]{gradeId,majorId,classicId,teachingType});			
				if (null!=guidePlan && guidePlan.size()==1) {
					stu.setTeachingPlan(guidePlan.get(0).getTeachingPlan());//添加教学计划
					stu.setTeachingGuidePlan(guidePlan.get(0));//年级教学计划，用来预约首学期课程
					
					//如果不为空，则判断是否有学习中心教学计划，有则关联
					for(TeachingGuidePlan plan  : guidePlan ){
						TeachingPlan teachingPlan = plan.getTeachingPlan();					
						if(null != teachingPlan.getOrgUnit() && teachingPlan.getOrgUnit().getResourceid().equals(stu.getBranchSchool().getResourceid())){
							stu.setTeachingPlan(teachingPlan);
							stu.setTeachingGuidePlan(plan);
							break;
						}
					}
				}else if (null!=guidePlan && guidePlan.size()>1) {
					logger.warn("学生：[ {} ] 根据年级+专业+层次+办学模式查出多个教学计划，请手动关联该学生的年纪教学计划！",stu.getStudentBaseInfo().getName());
					throw new ServiceException("学生： [ " + stu.getStudentBaseInfo().getName() + " ] 根据年级+专业+层次+办学模式查出多个教学计划，请手动关联该学生的年级教学计划！");
				}else {
					logger.warn("学生： [ {} ] 没有对应的教学计划，请手动关联该学生的年纪教学计划!",stu.getStudentBaseInfo().getName());
					throw new ServiceException("学生： [ " + stu.getStudentBaseInfo().getName() + " ] 没有对应的教学计划，请手动关联该学生的年级教学计划!");
				}
			}
		}
	}

	@Override
	public List<StudentInfo> findByUserAndGrage(Map<String, Object> condition)
			throws ServiceException {
		
		String studentInfoHql = "from "+ StudentInfo.class.getSimpleName()+" where isDeleted=0 ";
		if(condition.containsKey("userId")) {
			studentInfoHql += " and sysUser.resourceid=:userId";
		}
		if(condition.containsKey("selectGrageId")) {
		    studentInfoHql += " and grade.resourceid=:selectGrageId";
	    }
	  
		return findByHql(studentInfoHql, condition);
	}

	/**
	 * 根据条件获取学生学籍信息列表
	 * 
	 * 	@param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentInfo> findStudentInfoListByCondition(Map<String, Object> condition)throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		String hql = findStudentInfo(condition, values);
		return findByHql(hql, values);
	}

	/**
	 * 根据条件获取唯一学籍信息
	 * @param condition
	 * @return
	 */
	@Override
	public StudentInfo findUniqueStudentInfo(Map<String, Object> condition) {
		List<Object> paramValues = new ArrayList<Object>(); 
		String hql = "from "+StudentInfo.class.getSimpleName()+" so where so.isDeleted=0 ";
		if(condition.containsKey("studyNo")){
			hql += " and so.studyNo=? ";
			paramValues.add(condition.get("studyNo"));
		}
		if(condition.containsKey("studyName")){
			hql += " and so.studentName=? ";
			paramValues.add(condition.get("studyName"));
		}
		if(condition.containsKey("studyId")){
			hql += " and so.resourceid=? ";
			paramValues.add(condition.get("studyId"));
		}
		return findUnique(hql, paramValues.toArray());
	}
	
	/**
	 * 处理成绩信息（安徽医学籍表）
	 * @param examResulsList
	 */
	@Override
	public List<ExamResultsInfoVO> handleExamResultInfo(List<Map<String, Object>> examResulsList) {
		List<ExamResultsInfoVO> examResultsInfoList = new ArrayList<ExamResultsInfoVO>();
		List<Map<String, Object>> oneTermInfo = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> twoTermInfo = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> threeTermInfo = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> fourTermInfo = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> fiveTermInfo = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> sixTermInfo = new ArrayList<Map<String,Object>>();
		
		if(ExCollectionUtils.isNotEmpty(examResulsList)){
			for(Map<String, Object> examInfo : examResulsList){
				int term = ((BigDecimal)examInfo.get("term")).intValue();
				switch (term) {
					case 1:
						oneTermInfo.add(examInfo);
						break;
					case 2:
						twoTermInfo.add(examInfo);
						break;
					case 3:
						threeTermInfo.add(examInfo);
						break;
					case 4:
						fourTermInfo.add(examInfo);
						break;
					case 5:
						fiveTermInfo.add(examInfo);
						break;
				    default :
				    	sixTermInfo.add(examInfo);
						break;
				}
			}
		}
		// 1~3学期成绩信息
		int maxNum = Tools.getMax(new int[]{oneTermInfo.size(),twoTermInfo.size(),threeTermInfo.size()});
		int additionalNum = 0;
		for(int i=0; i<maxNum; i++){
			ExamResultsInfoVO examResultsInfoVO = new ExamResultsInfoVO();
			if(oneTermInfo.size()>0&& oneTermInfo.size()>i){
				Map<String, Object> oneTerm = oneTermInfo.get(i);
				if(oneTerm!=null&&oneTerm.size()>0){
					examResultsInfoVO.setCourseName_1((String)oneTerm.get("courseInfo"));
					examResultsInfoVO.setExamResult_1(((BigDecimal)oneTerm.get("score")).toString());
					additionalNum += Tools.getRowNum((String)oneTerm.get("courseInfo"), 30) - 1;
				}
			}
			if(twoTermInfo.size()>0&& twoTermInfo.size()>i){
				Map<String, Object> twoTerm = twoTermInfo.get(i);
				if(twoTerm!=null&&twoTerm.size()>0){
					examResultsInfoVO.setCourseName_2((String)twoTerm.get("courseInfo"));
					examResultsInfoVO.setExamResult_2(((BigDecimal)twoTerm.get("score")).toString());
					additionalNum += Tools.getRowNum((String)twoTerm.get("courseInfo"), 30) - 1;
				}
			}
			if(threeTermInfo.size()>0&& threeTermInfo.size()>i){
				Map<String, Object> threeTerm = threeTermInfo.get(i);
				if(threeTerm!=null&&threeTerm.size()>0){
					examResultsInfoVO.setCourseName_3((String)threeTerm.get("courseInfo"));
					examResultsInfoVO.setExamResult_3(((BigDecimal)threeTerm.get("score")).toString());
					additionalNum += Tools.getRowNum((String)threeTerm.get("courseInfo"), 30) - 1;
				}
			}
			examResultsInfoList.add(examResultsInfoVO);
		}
		// 填充空白（1~3学期和4~6学期各有10条记录）
		for(int j=0;j<(8-maxNum-additionalNum);j++){
			//空一行
			ExamResultsInfoVO temp = new ExamResultsInfoVO();
			examResultsInfoList.add(temp);
		}
		// 4~6学期成绩信息
		ExamResultsInfoVO _examResultsInfoVO = new ExamResultsInfoVO("第四学期科目/学时","成绩","第五学期科目/学时","成绩","第六学期科目/学时","成绩");
		examResultsInfoList.add(_examResultsInfoVO);
		int _maxNum = Tools.getMax(new int[]{fourTermInfo.size(),fiveTermInfo.size(),sixTermInfo.size()});
		int _additionalNum = 0;
		for(int i=0; i<_maxNum; i++){
			ExamResultsInfoVO examResultsInfoVO = new ExamResultsInfoVO();
			if(fourTermInfo.size()>0&& fourTermInfo.size()>i){
				Map<String, Object> fourTerm = fourTermInfo.get(i);
				if(fourTerm!=null&&fourTerm.size()>0){
					examResultsInfoVO.setCourseName_1((String)fourTerm.get("courseInfo"));
					examResultsInfoVO.setExamResult_1(((BigDecimal)fourTerm.get("score")).toString());
					_additionalNum += Tools.getRowNum((String)fourTerm.get("courseInfo"), 30) - 1;
				}
			}
			if(fiveTermInfo.size()>0&& fiveTermInfo.size()>i){
				Map<String, Object> fiveTerm = fiveTermInfo.get(i);
				if(fiveTerm!=null&&fiveTerm.size()>0){
					examResultsInfoVO.setCourseName_2((String)fiveTerm.get("courseInfo"));
					examResultsInfoVO.setExamResult_2(((BigDecimal)fiveTerm.get("score")).toString());
					_additionalNum += Tools.getRowNum((String)fiveTerm.get("courseInfo"), 30) - 1;
				}
			}
			if(sixTermInfo.size()>0&& sixTermInfo.size()>i){
				Map<String, Object> sixTerm = sixTermInfo.get(i);
				if(sixTerm!=null&&sixTerm.size()>0){
					examResultsInfoVO.setCourseName_3((String)sixTerm.get("courseInfo"));
					examResultsInfoVO.setExamResult_3(((BigDecimal)sixTerm.get("score")).toString());
					_additionalNum += Tools.getRowNum((String)sixTerm.get("courseInfo"), 30) - 1;
				}
			}
			examResultsInfoList.add(examResultsInfoVO);
		}
		// 填充空白（1~3学期和4~6学期各有10条记录）
		for(int j=0;j<(8-_maxNum-_additionalNum);j++){
		//if(_maxNum==0){
			ExamResultsInfoVO examResultsInfoVO = new ExamResultsInfoVO();
			examResultsInfoList.add(examResultsInfoVO);
		//}
		}
		return examResultsInfoList;
	}

	/**
	 * 根据条件获取学籍信息（不包含其他逻辑，单纯的查询）
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StudentInfo> findByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer(" from "+StudentInfo.class.getSimpleName()+" so where so.isDeleted=0 ");
		
		if(condition.containsKey("studentId")){
			hql.append(" and so.resourceid=:studentId ");
			values.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("studyNo")){
			hql.append(" and so.studyNo=:studyNo ");
			values.put("studyNo", condition.get("studyNo"));
		} else if (condition.containsKey("studyNoPerfix")) {
			hql.append(" and so.studyNo like :studyNoPerfix ");
			values.put("studyNoPerfix", condition.get("studyNoPerfix")+"%");
		}

		if(condition.containsKey("branchSchoolId")){
			hql.append(" and so.branchSchool.resourceid=:branchSchoolId ");
			values.put("branchSchoolId", condition.get("branchSchoolId"));
		} else if (condition.containsKey("branchSchool")) {
			hql.append(" and so.branchSchool.resourceid=:branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("gradeId")){
			hql.append(" and so.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		} else if (condition.containsKey("gradeid")) {
			hql.append(" and so.grade.resourceid=:gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("classicId")){
			hql.append(" and so.classic.resourceid=:classicId ");
			values.put("classicId", condition.get("classicId"));
		} else if (condition.containsKey("classicid")) {
			hql.append(" and so.classic.resourceid=:classicid ");
			values.put("classicid", condition.get("classicid"));
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and so.teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and so.major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		} else if (condition.containsKey("majorid")) {
			hql.append(" and so.major.resourceid=:majorid ");
			values.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("classesid")){
			hql.append(" and so.classes.resourceid=:classesid ");
			values.put("classesid", condition.get("classesid"));
		} else if (condition.containsKey("classes")) {
			hql.append(" and so.classes.resourceid=:classes ");
			values.put("classes", condition.get("classes"));
		}
		if(condition.containsKey("teachPlanid")){
			hql.append(" and so.teachingPlan.resourceid=:teachPlanid ");
			values.put("teachPlanid", condition.get("teachPlanid"));
		}
		if(condition.containsKey("name")){
			hql.append(" and so.studentName like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("enrolleecode")){
			hql.append(" and so.enrolleeCode=:enrolleeCode ");
			values.put("enrolleecode", condition.get("enrolleecode"));
		}
		if(condition.containsKey("examCertificateNo")){
			hql.append(" and so.examCertificateNo=:examCertificateNo ");
			values.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		if(condition.containsKey("studentIds")){
			hql.append(" and so.resourceid in (:studentIds) ");
			values.put("studentIds", condition.get("studentIds"));
		}
		if(condition.containsKey("certNum")){
			hql.append(" and so.studentBaseInfo.certNum=:certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("studentInfoStatus")){
			hql.append(" and so.studentStatus in("+condition.get("studentInfoStatus")+") ");
		} else if (condition.containsKey("studentStatus")) {
			hql.append(" and so.studentStatus in("+condition.get("studentStatus")+") ");
		}
		if (condition.containsKey("addHql")) {
			hql.append(condition.get("addHql").toString());
		}
		hql.append(" order by so.studyNo ");
		return findByHql(hql.toString(), values);
	}
	
	@Override
	public Page findPageByCondition(Map<String, Object> condition,Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = new StringBuffer("from "+StudentInfo.class.getSimpleName()+" so where so.isDeleted=0 ");
		
		if(condition.containsKey("studentId")){
			hql.append(" and so.resourceid=:studentId ");
			values.put("studentId", condition.get("studentId"));
		}
		if(condition.containsKey("studyNo")){
			hql.append(" and so.studyNo=:studyNo ");
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("branchSchoolId")){
			hql.append(" and so.branchSchool.resourceid=:branchSchoolId ");
			values.put("branchSchoolId", condition.get("branchSchoolId"));
		}
		if(condition.containsKey("gradeId")){
			hql.append(" and so.grade.resourceid=:gradeId ");
			values.put("gradeId", condition.get("gradeId"));
		}
		if(condition.containsKey("classicId")){
			hql.append(" and so.classic.resourceid=:classicId ");
			values.put("classicId", condition.get("classicId"));
		}
		if (condition.containsKey("teachingType")) {
			hql.append(" and so.teachingType=:teachingType ");
			values.put("teachingType", condition.get("teachingType"));
		}
		if(condition.containsKey("majorId")){
			hql.append(" and so.major.resourceid=:majorId ");
			values.put("majorId", condition.get("majorId"));
		}
		if(condition.containsKey("classesid")){
			hql.append(" and so.classes.resourceid=:classesid ");
			values.put("classesid", condition.get("classesid"));
		}
		if(condition.containsKey("teachPlanid")){
			hql.append(" and so.teachingPlan.resourceid=:teachPlanid ");
			values.put("teachPlanid", condition.get("teachPlanid"));
		}
		if(condition.containsKey("name")){
			hql.append(" and so.studentName like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("enrolleecode")){
			hql.append(" and so.enrolleeCode=:enrolleeCode ");
			values.put("enrolleecode", condition.get("enrolleecode"));
		}
		if(condition.containsKey("examCertificateNo")){
			hql.append(" and so.examCertificateNo=:examCertificateNo ");
			values.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		if(condition.containsKey("studentIds")){
			hql.append(" and so.resourceid in (:studentIds) ");
			values.put("studentIds", condition.get("studentIds"));
		}
		if(condition.containsKey("certNum")){
			hql.append(" and so.studentBaseInfo.certNum=:certNum ");
			values.put("certNum", condition.get("certNum"));
		}
		if(condition.containsKey("studentInfoStatus")){
			hql.append(" and so.studentStatus in("+condition.get("studentInfoStatus")+") ");
		}
		if(ExStringUtils.isNotBlank(objPage.getOrderBy())){
			hql.append(" order by resourceid ,"+objPage.getOrderBy() +" "+ objPage.getOrder());
		}else {
			hql.append(" order by so.studyNo ");
		}
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	/**
	 * 将学籍信息列表转为Map
	 * @param studentInfoList
	 * @return
	 */
	@Override
	public Map<String, StudentInfo> transStudentInfoListToMap(List<StudentInfo> studentInfoList) {
		Map<String, StudentInfo> studentInfoMap = new HashMap<String, StudentInfo>();
		if(ExCollectionUtils.isNotEmpty(studentInfoList)){
			for(StudentInfo so : studentInfoList){
				studentInfoMap.put(so.getResourceid(), so);
			}
		}
		return studentInfoMap;
	}
	@Override
	public List<StudentStatisticsVo> CalStudentStatistics(List<String> majorList, Map<String , Object> condition) throws ServiceException{
		StringBuffer hql = new StringBuffer();
		Map<String,Object> values = new HashMap<String, Object>();
		//getMajorHql(hql,majorList.get(0),values);
		hql.append(" select gradeSum.gradeName, SUM3,SUM2,SUM1, ");
		for(int i =0;i< majorList.size();i++){			
			if(i==majorList.size()-1){
				hql.append(majorList.get(i)+" ");
			}else{
				hql.append(majorList.get(i)+", ");
			}
		}
		hql.append(" from ( ");
		hql.append(" select ebg.gradename gradeName,count(ers.resourceid) SUM3 from edu_roll_studentinfo ers ");
		hql.append(" join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
		hql.append(" join edu_base_year eby on eby.resourceid = ebg.yearid and eby.firstyear in (:years) ");
		hql.append(" where  ers.isDeleted = 0  and ers.studentStatus = '11' and ers.branchschoolid = :brSchool ");
		hql.append(" group by ebg.gradename order by ebg.gradeName ) gradeSum ");
		hql.append(" left join ( ");
		hql.append(" select ebg.gradename gradeName,count(ers.resourceid) SUM1 from edu_roll_studentinfo ers ");
		hql.append(" join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
		hql.append(" join edu_base_year eby on eby.resourceid = ebg.yearid and eby.firstyear in (:years) ");
		hql.append(" where  ers.isDeleted = 0  and ers.studentStatus = '11' and ers.branchschoolid = :brSchool and ers.classicid = '5a402f0339764b8e013976677c820001' ");//本科
		hql.append(" group by ebg.gradename order by ebg.gradeName ) classicSum5 on classicSum5.gradeName = gradeSum.gradeName");
		hql.append(" left join ( ");
		hql.append(" select ebg.gradename gradeName,count(ers.resourceid) SUM2 from edu_roll_studentinfo ers ");
		hql.append(" join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
		hql.append(" join edu_base_year eby on eby.resourceid = ebg.yearid and eby.firstyear in (:years) ");
		hql.append(" where  ers.isDeleted = 0  and ers.studentStatus = '11' and ers.branchschoolid = :brSchool and ers.classicid = '5a402f0339764b8e013976687cfb0002' ");//专科
		hql.append(" group by ebg.gradename order by ebg.gradeName ) classicSum6 on classicSum6.gradeName = gradeSum.gradeName ");
		
		
		for(int i =0;i< majorList.size();i++){
			hql.append(" left join ( ");
			getMajorHql(hql,majorList.get(i),values,condition);
			hql.append(" ) ");
			hql.append(majorList.get(i)+i+ " on "+majorList.get(i)+i+".gradename = gradeSum.gradename ");
			
		}
		hql.append(" order by gradeSum.gradeName ");
		values.put("years", condition.get("years"));
		values.put("brSchool", condition.get("brSchool"));
		
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), StudentStatisticsVo.class, values);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public static void getMajorHql(StringBuffer hql,String major,Map<String,Object> values,Map<String,Object> condition){
		hql.append(" select ebg.gradename gradeName,count(ers.resourceid)  "+major);
		hql.append(" from edu_roll_studentinfo  ers " );
		hql.append(" join edu_base_grade ebg on ebg.resourceid = ers.gradeid ");
		hql.append(" join edu_base_year eby on eby.resourceid = ebg.yearid and eby.firstyear in (:years) ");
		hql.append(" join edu_base_major ebm on ebm.resourceid= ers.majorid and ebm.majorcode=:major"+major);		
		hql.append(" where ers.branchSchoolid=:brSchool and ers.isDeleted=0 and ers.studentStatus = '11' ");	
		hql.append(" group by ers.gradeid,ebg.gradename ");
		hql.append(" order by ebg.gradename ");
		values.put("major"+major, condition.get(major));
		
	}
	@Override
	public List<StudentSignatureVo> findStudentSignature(Map<String, Object> condition) throws ServiceException{
		StringBuffer sql = new StringBuffer();
		sql.append(" select ers.studyNo studyNo1,ers.studentName studentName1 from edu_roll_studentinfo ers ");
		sql.append(" join edu_roll_classes erc on erc.resourceid = ers.classesid   and erc.isdeleted = 0 ");
		if(condition.containsKey("classesid")){
			sql.append(" and erc.resourceid =:classesid ");
		}
		sql.append(" where ers.studentstatus = '11' and ers.isdeleted = 0 and not exists ( ");
		sql.append(" select eta.resourceid from edu_teach_abnormalexam eta where eta.studentid = ers.resourceid and eta.isdeleted = 0  and eta.checkstatus = '1' and eta.isoutplancourse='N' ");
		if(condition.containsKey("courseid")){
			sql.append(" and eta.courseid = :courseid ");
		}
		sql.append(" ) and not exists( ");
		sql.append(" select etn.resourceid from edu_teach_noexam etn where etn.studentid = ers.resourceid and etn.isdeleted = 0  and etn.checkstatus = '1' ");
		if(condition.containsKey("courseid")){
			sql.append(" and etn.courseid = :courseid ");
		}
		sql.append(" ) ");
		sql.append(" order by ers.studyNo ");
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StudentSignatureVo.class, condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 更新学生在线时长信息
	 * @param sql
	 * @param params
	 * @throws Exception
	 */
	@Override
	public void updateLoginLongInfo(String sql, Object... params) throws Exception {
		baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update(sql, params);
	}

	/**
	 * 获取某个用户当前的学籍信息
	 * @param userId
	 * @return
	 * @throws Exception 
	 */
	@Override
	public StudentInfo getByUserId(String userId) throws Exception {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("userId", userId);
		StudentInfo studentInfo = (StudentInfo)baseSupportJdbcDao.getBaseJdbcTemplate().findForObject("select * from edu_roll_studentinfo where isdeleted=0 and resourceid=(select exvalue from hnjk_sys_usersextend where excode='defalutrollid' and isdeleted=0 and sysuserid=:userId)", StudentInfo.class, parameters);
		return studentInfo;
	}
	@Override
	public Map<String,Object> importPhoto(String gradeid, String filepath) throws Exception{
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//1.解压上传文件到临时目录tempZipPath
		//2.找出考生对应的相片，记录对应的考生不存在的相片
		
		//根据全局参数的属性值进行匹配
		String property = CacheAppManager.getSysConfigurationByCode("stuRoll.photo.property").getParamValue().toUpperCase();
		Map<String, Map<String, Object>> stuInfoMap = new HashMap<String, Map<String,Object>>();//获取考生信息
		if(ExStringUtils.isNotBlank(property)){
			stuInfoMap=getstuInfoMap(gradeid,property);
		}else{//默认使用 studyno
			stuInfoMap=getstuInfoMap(gradeid,STUDENTNO);
		}
		if(stuInfoMap.size()==0){
			throw new ServiceException("未找到该年级的学生!");
		}
		Set<Map<String, Object>> stuInfoSet = new HashSet<Map<String,Object>>();//待更新相片的考生
		List<String> noStuInfoPhotoList = new ArrayList<String>();//对应的考生不存在的相片
		File[] photoList = new File(filepath).listFiles();
		if(ArrayUtils.isNotEmpty(photoList)){
			//服务器图片存储路径：{EDU3_DATAS_LOCALROOTPATH}/common/students/STUDENTNO.JPG
			String disPath = Constants.EDU3_DATAS_LOCALROOTPATH+"common"+File.separator+"students"+File.separator;//图片实际存储路径前缀
			for (File file : photoList) {
				String fileName = file.getName();//1、学号  2、身份证
				//String prefix = ExStringUtils.upperCase(ExStringUtils.substring(fileName, 0,1));//文件名首字母
				String tmp_fileName = ExStringUtils.substringBefore(ExStringUtils.upperCase(ExStringUtils.substring(fileName, 0)),".JPG");//考生号
				
				if(stuInfoMap.containsKey(tmp_fileName)){//已导入考生信息
					Map<String, Object> map = stuInfoMap.get(tmp_fileName);
					//map.put(prefix, "/"+map.get("SIGNUPDATE")+"/"+fileName);//网络路径
					map.put("fileName", File.separator+fileName);//相片名称
					map.put("tempPath", file.getAbsolutePath());//原存储路径
					//map.put("disPath", disPath+map.get("SIGNUPDATE")+File.separator);////图片实际存储目录
					map.put("servPath", disPath+File.separator+fileName);//新的实际存储路径
					stuInfoSet.add(map);
				} else {
					if("certnum".equalsIgnoreCase(property)){
						noStuInfoPhotoList.add("不存在身份证号为"+tmp_fileName+"的学生: "+fileName);//不存在考生
					}else {
						noStuInfoPhotoList.add("不存在学号为"+tmp_fileName+"的学生: "+fileName);//不存在考生
					}
					
				}
			}
			//3.把检查通过的相片复制到实际存储目录下,更新考生相片
			List<Map<String, Object>> failList = new ArrayList<Map<String,Object>>();
			for (Map<String, Object> map : stuInfoSet) {
				try {
					if(map.get("disPath")!=null){//创建相片存储目录
						if(!new File(map.get("disPath").toString()).exists()){
							new File(map.get("disPath").toString()).mkdirs();
						}
					}
					if(map.get("tempPath")!=null){//复制主相片
						FileUtils.copyFile(map.get("tempPath").toString(), map.get("servPath").toString());//复制图片到指定目录
					}
									
				} catch (IOException e) {
					e.printStackTrace();
					failList.add(map);//复制失败						
				}
			}	
			//把相片复制到实际存储目录过程失败的相片移出更新列表
			if(ExCollectionUtils.isNotEmpty(failList)){
				stuInfoSet.removeAll(failList);
				for (Map<String, Object> map : failList) {
					noStuInfoPhotoList.add("导入学生相片失败: "+(map.get("fileName")!=null?map.get("fileName"):""));
				}
			}
			String message = "";
			if(ExCollectionUtils.isNotEmpty(stuInfoSet)){
				//String exameeInfoSql = "update edu_base_student set mainPhotoPath=:F,backPhotoPath=:Z where resourceid=:EXAMINEEID";
				String baseInfoSql = "update edu_base_student set photoPath=:fileName where resourceid=:RESOURCEID";
				//baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(exameeInfoSql, stuInfoSet.toArray(new Map[stuInfoSet.size()]));
				baseSupportJdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(baseInfoSql, stuInfoSet.toArray(new Map[stuInfoSet.size()]));
				message += "<span style='color:green;'>成功导入的相片数: "+stuInfoSet.size()+"</span>";
				logger.info("成功导入的相片数: "+stuInfoSet.size());
			}
			//4.记录导入失败照片
			if(ExCollectionUtils.isNotEmpty(noStuInfoPhotoList)){
				message += "<br/><span style='color:red;'>导入失败的相片数: "+noStuInfoPhotoList.size()+"</span>";
				for (String msg : noStuInfoPhotoList) {
					logger.info(msg);
				}
				resultMap.put("errorMessageList", noStuInfoPhotoList);
			}
			resultMap.put("message", message);
		}
		return resultMap;
	}
	public Map<String, Map<String, Object>> getstuInfoMap(String gradeid,String property){
		Map<String, Map<String, Object>> result = new HashMap<String, Map<String,Object>>();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ebs.resourceid,ers.studyno STUDENTNO,ebs.certnum CERTNUM from edu_roll_studentinfo ers ");
		sql.append(" join edu_base_student ebs on ebs.resourceid=ers.studentbaseinfoid and ebs.isdeleted = 0 ");
		sql.append(" where ers.isdeleted = 0 and ers.gradeid =? ");
		try {
			List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(sql.toString(), new Object[]{gradeid});
			if(ExCollectionUtils.isNotEmpty(list)){
				for (Map<String, Object> map : list) {
					result.put(map.get(property).toString(), map);
				}
			}
		} catch (Exception e) {
			logger.error("查询出错",e.fillInStackTrace());
		}
		return result;
	}

	@Override
	public List<StudentInfo> findAllExamResultsStudentInfo(Map<String, Object> condition) {
		
		StringBuffer sql = new StringBuffer();
		List<StudentInfo> students = new ArrayList<StudentInfo>();
		Map<String, Object> values = new HashMap<String, Object>();

		sql.append(" select distinct u.unitName,m.majorName,cl.classesname className,p.eduYear,s.classesid,max(t.studentid) studentid from edu_roll_studentinfo s ");
		sql.append(" join hnjk_sys_unit u on u.resourceid = s.BRANCHSCHOOLID");
		sql.append(" left join edu_teach_plan p on p.resourceid = s.teachplanid and p.isdeleted=0");
		sql.append(" join edu_base_major m on m.resourceid = s.majorid and m.isdeleted=0");
		sql.append(" join edu_roll_classes cl on cl.resourceid = s.classesid and cl.isdeleted=0");
		sql.append(" join edu_teach_examresults t on s.resourceid=t.studentid and t.isDeleted=0");
		sql.append(" left join  EDU_TEACH_STATEXAMRESULTS st on s.resourceid=st.studentid");
		sql.append(" left join  EDU_TEACH_NOEXAM n on s.resourceid=n.studentid");
		sql.append(" where t.checkstatus > 0");
		if (condition.containsKey("branchSchool")) {
			sql.append(" and s.BRANCHSCHOOLID=:branchSchool");
			values.put("branchSchool",condition.get("branchSchool"));
		}
		if (condition.containsKey("gradeid")) {
			sql.append(" and s.gradeid=:gradeid");
			values.put("gradeid",condition.get("gradeid"));
		}
		if (condition.containsKey("classicid")) {
			sql.append(" and s.classicid=:classicid");
			values.put("classicid",condition.get("classicid"));
		}
		if (condition.containsKey("learningStyle")) {
			sql.append(" and s.teachingType=:learningStyle");
			values.put("learningStyle",condition.get("learningStyle"));
		}
		if (condition.containsKey("majorid")) {
			sql.append(" and s.majorid=:majorid");
			values.put("majorid",condition.get("majorid"));
		}
		if (condition.containsKey("classesid")) {
			sql.append(" and s.classesid=:classesid");
			values.put("classesid",condition.get("classesid"));
		} else if (condition.containsKey("classes")) {
			sql.append(" and s.classesid=:classes");
			values.put("classes",condition.get("classes"));
		}
		sql.append(" group by u.unitName,m.majorName,cl.classesname,cl.resourceid,p.eduYear,s.classesid,t.courseid,st.courseid,n.courseid");//,t.courseid,st.courseid,n.courseid order by s.classesid
		try {
			List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), values);

			if(ExCollectionUtils.isNotEmpty(list)){
				StringBuilder studentids = new StringBuilder();
				for (Map<String, Object> map : list) {
					studentids.append(map.get("studentid")).append(",");
				}
				condition.clear();
				condition.put("studentIds", Arrays.asList(studentids.substring(0,studentids.length()-1).split(",")));
				students = findByCondition(condition);
			}
		} catch (Exception e) {
			logger.error("查询出错",e.fillInStackTrace());
		}
		
		return students;
	}

	/**
	 * 提交学籍卡
	 * @param studentIds
	 * @return
	 * @throws Exception
	 */
	@Override
	public int submitStuCard(String studentIds) throws Exception {
		String sql = "update edu_roll_studentinfo info set info.rollcardstatus='2' where info.resourceid in ('"+studentIds+"')";
		return baseSupportJdbcDao.getBaseJdbcTemplate().executeForObject(sql, new Object());
	}

	@Override
	public Map<String, Object> importStudyNo(List<StudyNoImportVo> modelList) {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		List<StudyNoImportVo> failList = new ArrayList<StudyNoImportVo>();//失败列表
		if(!(modelList!=null && modelList.size()>0)){
			retrunMap.put("statusCode", statusCode);
			return retrunMap;
		}
		ComparatorSPVO voList=  new ComparatorSPVO();
		Collections.sort(modelList, voList);
		//处理数据进行查询
		StringBuffer certNumStr = new StringBuffer();
		StringBuffer studyNoStr = new StringBuffer();
		StringBuffer certNumAndStudyNoStr = new StringBuffer();
		int inNum = 1;
		for(int i=0;i<modelList.size();i++){
			if(inNum==500 && i>0){
				studyNoStr.append("'"+modelList.get(i).getStudyNo()+"') or si.studyNo in (");
				certNumStr.append("('"+modelList.get(i).getExamCertificateNo()+"','"+modelList.get(i).getCertNum()+"')");
				certNumStr.append(") or (si.examCertificateNo,bs.certNum) in (");
				certNumAndStudyNoStr.append("('"+modelList.get(i).getExamCertificateNo()+"','"+modelList.get(i).getCertNum()+"','"+modelList.get(i).getStudyNo()+"')");
				certNumAndStudyNoStr.append(") or (si.examCertificateNo,bs.certNum,si.studyNo) in (");
				inNum = 1;
			}else{
				studyNoStr.append("'"+modelList.get(i).getStudyNo()+"'");
				certNumStr.append("('"+modelList.get(i).getExamCertificateNo()+"','"+modelList.get(i).getCertNum()+"')");
				certNumAndStudyNoStr.append("('"+modelList.get(i).getExamCertificateNo()+"','"+modelList.get(i).getCertNum()+"','"+modelList.get(i).getStudyNo()+"')");
				if(i!=modelList.size()-1){
					studyNoStr.append(",");
					certNumStr.append(",");
					certNumAndStudyNoStr.append(",");
					inNum++;
				}
			}
		}
		StringBuffer hql = new StringBuffer();
		List<String> studyNos = new ArrayList<String>();
		List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
		if(modelList!=null && modelList.size()>0){
			hql.append(" select si.*,bs.certNum from edu_roll_studentinfo si join edu_base_student bs on bs.resourceid=si.studentBaseInfoid");
			hql.append(" where (nvl(si.examCertificateNo,'null'),bs.certNum) in("+certNumStr.toString()+") and si.studyNo not in("+studyNoStr.toString()+")");
			hql.append(" or (nvl(si.examCertificateNo,'null'),bs.certNum,si.studyNo) in("+certNumAndStudyNoStr.toString()+") ");
			hql.append(" order by bs.certNum,si.examCertificateNo");
			try {
				studentInfoList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), StudentInfo.class, new HashMap<String, Object>());
				hql.setLength(0);
				hql.append(" select si.*,bs.certNum from edu_roll_studentinfo si join edu_base_student bs on bs.resourceid=si.studentBaseInfoid");
				hql.append(" where si.studyNo in ("+studyNoStr.toString()+") and (nvl(si.examCertificateNo,'null'),bs.certNum,si.studyNo) not in("+certNumAndStudyNoStr.toString()+")");
				hql.append(" order by bs.certNum,si.examCertificateNo");
				List<StudentInfo> studyNoList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), StudentInfo.class, new HashMap<String, Object>());
				for (StudentInfo no : studyNoList) {
					studyNos.add(no.getStudyNo());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int index = 0;
		List<StudentInfo> saveStudentInfos = new ArrayList<StudentInfo>();
		for (StudyNoImportVo vo : modelList){
			String studyNo =vo.getStudyNo();
			String examCertificateNo = vo.getExamCertificateNo();
			String certNum = vo.getCertNum();
			if(studyNos.contains(studyNo)){
				vo.setFalseMsg("学号： "+studyNo+"，该学号已存在！");
				index++;
			}else if(studentInfoList!=null && studentInfoList.size()>index){
				StudentInfo si = studentInfoList.get(index);
				if(ExStringUtils.toString(examCertificateNo).equalsIgnoreCase(ExStringUtils.toString(si.getExamCertificateNo()))
						&& certNum.equalsIgnoreCase(si.getCertNum()) 
				){
					si.setStudyNo(studyNo);
					index++;
					saveStudentInfos.add(si);
					continue;
				}else {
					vo.setFalseMsg("学号： "+studyNo+"，该学生信息有误！");
				}
			}
			failList.add(vo);
			msg.append("学号： "+studyNo+"，导入失败！<br>");
		}
		
		if(saveStudentInfos.size()>0){
			String sql = "update edu_roll_studentinfo si set si.studyNo=:studyNo where si.resourceid=:resourceid";
			Map<String, Object> parameters = new HashMap<String, Object>();
			for (StudentInfo studentInfo : saveStudentInfos) {
				parameters.put("studyNo", studentInfo.getStudyNo());
				parameters.put("resourceid", studentInfo.getResourceid());
				try {
					baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql, parameters);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(failList.size()>0){
				return importStudyNo(failList);
			}else {
				retrunMap.put("statusCode", 200);
				retrunMap.put("message", "导入成功！");
				return retrunMap;
			}
			
		}else {
			if(failList.size()>0){
				msg.append(" 失败记录：<font color='red'>"+failList.size()+" </font> 条");
				retrunMap.put("statusCode", 300);
				retrunMap.put("message", msg.toString());
				retrunMap.put("failRecord", failList);
			}else {
				retrunMap.put("statusCode", 200);
				retrunMap.put("message", "导入成功！");
			}
			return retrunMap;
		}
	}
	
	public class ComparatorSPVO implements Comparator{
		 @Override
		 public int compare(Object arg0, Object arg1) {
			 StudyNoImportVo vo1=(StudyNoImportVo)arg0;
			 StudyNoImportVo vo2=(StudyNoImportVo)arg1;
		  return vo1.getCertNum().compareToIgnoreCase(vo2.getCertNum())==0?
				(vo1.getExamCertificateNo().compareToIgnoreCase(vo2.getExamCertificateNo())):
				vo1.getCertNum().compareToIgnoreCase(vo2.getCertNum());
		 }
	}

	/**
	 * 根据身份证号获取当前学籍
	 * @param certNum
	 * @return
	 */
	@Override
	public StudentInfo findBycertNum(String certNum) {
		StringBuffer hql = new StringBuffer(1024);
		hql.append("select so from StudentInfo so,UserExtends ue where so.sysUser.username=? and ue.user.resourceid=so.sysUser.resourceid and ue.exCode='defalutrollid' and ue.exValue=so.resourceid");
		return findUnique(hql.toString(), certNum);
	}

	@Override
	public Map<String, Map<String, Object>> getStudentInfoMapByStuList(List<StudentInfo> studentList) throws Exception {
		StringBuilder builder = new StringBuilder(studentList.size()*36);
		for (StudentInfo stu : studentList) {
			builder.append(",'"+stu.getResourceid()+"'");
		}
		String studentids = builder.substring(1);
		StringBuilder sqlBuilder = new StringBuilder(builder.length()+1000);
		//毕业日期
		sqlBuilder.append("select si.resourceid key,to_char(nvl(gd.graduatedate,sysdate),'yyyy-mm')graduatedate1,to_char(gd.graduatedate,'yyyy-mm-dd')graduatedate2,");
		sqlBuilder.append(" u.unitname,u.unitshortname,g.gradename,y.firstyear,substr(g.gradename,0,4) gradeyear,g.term gradeterm,ci.classicname,ci.shortname classicshortname,");
		sqlBuilder.append(" si.teachingtype,m.majorname,cl.classCode,cl.classesname,bs.gender,p.theGraduationScore,nvl(p.eduyear,'') eduyear,p.degreename,nvl(exam.examcount,0) examcount");
		sqlBuilder.append(" from edu_roll_studentinfo si");
		sqlBuilder.append(" left join edu_teach_graduatedata gd on gd.isdeleted=0 and gd.studentid=si.resourceid");
		sqlBuilder.append(" left join hnjk_sys_unit u on u.isdeleted=0 and u.resourceid=si.branchschoolid");
		sqlBuilder.append(" left join edu_base_grade g on g.isdeleted=0 and g.resourceid=si.gradeid");
		sqlBuilder.append(" left join edu_base_year y on y.resourceid=g.yearid and y.isdeleted=0");
		sqlBuilder.append(" left join edu_base_classic ci on ci.isdeleted=0 and ci.resourceid=si.classicid");
		sqlBuilder.append(" left join edu_base_major m on m.isdeleted=0 and m.resourceid=si.majorid");
		sqlBuilder.append(" left join edu_roll_classes cl on cl.isdeleted=0 and cl.resourceid=si.classesid");
		sqlBuilder.append(" left join edu_teach_plan p on p.isdeleted=0 and p.resourceid=si.teachplanid");
		sqlBuilder.append(" left join edu_base_student bs on bs.isdeleted=0 and bs.resourceid=si.studentbaseinfoid");
		sqlBuilder.append(" left join (select eer.studentid,count(*) examcount from  edu_teach_electiveexamresults eer where eer.isdeleted=0 group by eer.studentid)exam on exam.studentid=si.resourceid");
		sqlBuilder.append(" where si.isdeleted=0 and si.resourceid in("+studentids+")");
		List<Map<String, Object>> mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlBuilder.toString(), new HashMap<String, Object>());
		return ExBeanUtils.convertMapsToMap(mapList);
	}

	@Override
	public void batchSetStudendInfo(Map<String,Object> colMap,Map<String,Object> condition) {
		String date = ExDateUtils.getCurrentDate();
		User curUser = SpringSecurityHelper.getCurrentUser();
		StringBuilder builder = new StringBuilder();
		builder.append("update edu_roll_studentinfo si set memo=memo");
		for (Map.Entry<String,Object> entry: colMap.entrySet()) {
			if ("inDate".equals(entry.getKey())) {
				builder.append(","+entry.getKey()+"=to_date('"+entry.getValue()+"','yyyy-MM-dd')");
			} else {
				builder.append(","+entry.getKey()+"="+entry.getValue());
			}
		}
		builder.append(" where isdeleted=0");
		String querySql = getSqlByQuetyParam(condition);
		builder.append(querySql);
		try {
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(),condition);
			UserOperationLogs userOperationLog = new UserOperationLogs();
			userOperationLog.setUserName(curUser.getCnName());
			userOperationLog.setUserId(curUser.getResourceid());
			userOperationLog.setOperationContent(colMap.toString()+"<br>"+condition.toString());
			userOperationLog.setOperationType(UserOperationLogs.DELETE);
			userOperationLog.setRecordTime(new Date());
			userOperationLog.setModules("3");
			userOperationLogsService.persist(userOperationLog);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private String getSqlByQuetyParam(Map<String,Object> condition) {
		StringBuilder builder = new StringBuilder();
		builder.append(" and studentBaseInfoid in (select bs.resourceid from edu_base_student bs where bs.isdeleted=0 ");
		if(condition.containsKey("certNum")){//身份证号
			builder.append(" and certNum = :certNum ");
		}
		if(condition.containsKey("gender")){//性别
			builder.append(" and gender= :gender ");
		}
		builder.append(")");
		if(condition.containsKey("branchSchool")){//学习中心
			builder.append(" and branchSchoolid = :branchSchool ");
		}
		if(condition.containsKey("gradeid")){//年级
			builder.append(" and gradeid= :gradeid ");
		} else if (condition.containsKey("stuGrade")) {
			builder.append(" and gradeid= :stuGrade ");
		}
		if(condition.containsKey("classic")){//层次
			builder.append(" and classicid = :classic ");
		}
		if(condition.containsKey("schoolType")){//教学模式
			builder.append(" and teachingType = :schoolType ");
		} else if (condition.containsKey("teachingType")) {
			builder.append(" and teachingType = :teachingType ");
		}
		if(condition.containsKey("major")){//专业
			builder.append(" and majorid = :major ");
		}
		if(condition.containsKey("classesid")){//班级
			builder.append(" and classesid=:classesid ");
		}
		if(condition.containsKey("stuStatus")){//学籍状态
			builder.append(" and studentStatus = :stuStatus ");
		}
		if(condition.containsKey("name")){//学生姓名
			builder.append(" and studentName like :name ");
			condition.put("name", "%"+condition.get("name")+"%");
		}

		if(condition.containsKey("matriculateNoticeNo")){//准考证号
			builder.append(" and examCertificateNo = :matriculateNoticeNo ");
		} else if (condition.containsKey("examCertificateNo")) {
			builder.append(" and examCertificateNo = :examCertificateNo ");
		}
		if(condition.containsKey("studyNo")){//学号
			builder.append(" and studyNo like :studyNo ");
			condition.put("studyNo","%"+condition.get("studyNo")+"%");
		} else if (condition.containsKey("studyNoPerfix")) {
			builder.append(" and studyNo like :studyNoPerfix ");
			condition.put("studyNoPerfix",condition.get("studyNoPerfix")+"%");
		}

		if(condition.containsKey("orderCourseStatusFlag")){//预约课程状态
			builder.append(" and orderCourseStatus= :orderCourseStatusFlag ");
			condition.put("orderCourseStatusFlag",Integer.parseInt(condition.get("orderCourseStatusFlag").toString()));
		}
		if(condition.containsKey("orderExamStatusFlag")){//预约考试状态
			builder.append(" and examOrderStatus= :orderExamStatusFlag ");
			condition.put("orderExamStatusFlag",Integer.parseInt(condition.get("orderExamStatusFlag").toString()));
		}
		if (condition.containsKey("makeUpTeachingPlanOn")) {//学生教学计划补录
			builder.append(" and teachingPlan IS NULL ");
		}

		//是否查询学籍状态为毕业的学员 and 申请学位的同学们
		if(condition.containsKey("isThesis")){
			builder.append(" and studentStatus=:isThesis and isApplyGraduate=:isApplyGraduate");
		}

		if (condition.containsKey("entranceFlag")) {
			builder.append(" and enterAuditStatus = :entranceFlag ");
		}

		if(!condition.containsKey("graduateAudit")||!condition.containsKey("stuStatusCondition")){
			if(condition.containsKey("schoolrollstudentstatus")){
				builder.append(" or studentStatus in ("+condition.get("schoolrollstudentstatus")+")");
			}
		}

		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			builder.append(" and accountStatus = :accountStatus ");
			condition.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));//无学籍状态
		}
		if(condition.containsKey("isGraduateQualifer")){
			builder.append(" and studentStatus in ('11','21','25') ");
			builder.append(" and teachingPlan is not null ");
		}

		if(condition.containsKey("isPassEnter")){
			if("1".equals(condition.get("isPassEnter"))){
				builder.append(" and enterAuditStatus ='Y' ");
			}else if ("0".equals(condition.get("isPassEnter"))){
				builder.append(" and enterAuditStatus !='Y' ");
			}
		}

		if(condition.containsKey("isApplyDelay")){
			if("1".equals(condition.get("isApplyDelay"))){
				builder.append(" and (isApplyGraduate= 'W' ) ");
			}else if ("0".equals(condition.get("isApplyDelay"))){
				builder.append(" and (isApplyGraduate= 'N' or isApplyGraduate='Y' ) ");
			}
		}

		if(condition.containsKey("rollCard")){
			builder.append(" and rollCardStatus=:rollCard ");
		}

		if(condition.containsKey("havePhoto")){
			if(Constants.BOOLEAN_YES.equals(condition.get("havePhoto").toString())){
				builder.append(" and studentBaseInfo.photoPath is not null ");
			}else{
				builder.append(" and studentBaseInfo.photoPath is null ");
			}
		}
		if (condition.containsKey("studentids")) {
			builder.append(" and resourceid in('"+ExStringUtils.replace(condition.get("studentids").toString(),",","','")+"')");
		}
		return builder.toString();
	}

	/**
	 * 获取学生的基本信息和学位外语课程信息
	 * 
	 * @param studyNo
	 * @return
	 * @throws Exception 
	 */
	@Override
	public StudentInfoAndDegreeCourseVO findByStudyNo(String studyNo) throws Exception {
		Map<String, Object> params = new HashMap<String, Object>(1);
		params.put("studyNo", studyNo);
		
		StringBuffer sql = new StringBuffer(500);
		sql.append("select so.studyno studyNo,s.certnum certNum,so.studentname studentName,c.coursename degreeCourseName,so.resourceid studentId,c.resourceid courseId,sr.resourceid resultId ")
		.append("from edu_roll_studentinfo so inner join edu_base_student s on so.studentbaseinfoid=s.resourceid ")
		.append("left join edu_teach_guiplan gp on gp.isdeleted=0 and gp.ispublished='Y' and gp.gradeid=so.gradeid and gp.planid=so.teachplanid ")
		.append("left join edu_base_course c on c.isdeleted=0 and c.resourceid=gp.degreeforeignlanguage and c.isdegreeunitexam='Y' ")
		.append("left join edu_teach_statexamresults sr on sr.studentid=so.resourceid and sr.courseid=c.resourceid and sr.isdeleted=0 ")
		.append("where so.isdeleted=0 and so.studyno=:studyNo ");
		
		return (StudentInfoAndDegreeCourseVO) baseSupportJdbcDao.getBaseJdbcTemplate().findForObject(sql.toString(), StudentInfoAndDegreeCourseVO.class, params);
	}

	@Override
	public int getStudentNumByClassid(String classesid) {
		int num = 0;
		StringBuffer sql = new StringBuffer();
		Map<String, Object> map = new HashMap<String, Object>();
		sql.append("select count(*) c from edu_roll_studentinfo s where s.isdeleted=0 and s.studentstatus='11'");

		map.put("classesid", classesid);
		sql.append(" and s.classesid=:classesid ");

		try {
			Map<String, Object> results = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql.toString(), map);
			if (results!=null) {
				num = Integer.parseInt(results.get("c").toString());
			}
		} catch (Exception e) {
			throw new ServiceException("出错:"+e.fillInStackTrace());
		}
		return num;
	}

	@Override
	public Map<String, Object> importPracticeMaterials(String filePath, String type) {
		Map<String,Object> returnMap = new HashMap<String, Object>();
		int  count = 0;		//总条数
		int successCount = 0;// 成功的条数
		int startRow = 1;	//第一条数据所在行数
		Map<String,Object> validateMap = new HashMap<String, Object>();
		try {
			@Cleanup InputStream inputStream = new FileInputStream(filePath);
			@Cleanup Workbook workbook = WorkbookFactory.create(inputStream);

			StringBuffer returnMsg = new StringBuffer();

			String brSchoolid = "";
			boolean isBrSchool = false;
			User user = SpringSecurityHelper.getCurrentUser();
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				brSchoolid = user.getOrgUnit().getResourceid();
				isBrSchool = true;
			}
			Sheet sheet = workbook.getSheetAt(0);
			count = sheet.getLastRowNum()-startRow+1;
			do{
				if (count == 0) {
					returnMsg.append("<font color='red'>请先填入数据再进行导入！</font><br>");
					continue;
				}

				List<StudentInfoVo> studentInfoVos = new ArrayList<StudentInfoVo>();
				StringBuilder studyNoBuilder = new StringBuilder(sheet.getLastRowNum()*12);
				for (int rowNum=startRow; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row row = sheet.getRow(rowNum);
					if (row != null) {
						String studyNoStr = ExStringUtils.toString(row.getCell(0));
						String studentNameStr = ExStringUtils.toString(row.getCell(1));
						String statusStr = ExStringUtils.toString(row.getCell(2));
						if (ExStringUtils.isBlank(studyNoStr,studentNameStr)) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行:"+studyNoStr+"]</font>"+"学号和姓名为必填项！<br>");
							continue;
						}
						if (ExStringUtils.isContainsStr(studyNoBuilder.toString(), studyNoStr)) {
							returnMsg.append("<font color='red'>[第" + (rowNum+1) + "行:"+studyNoStr+"]</font>"+"学号重复！<br>");
							continue;
						}
						StudentInfoVo infoVo = new StudentInfoVo();
						infoVo.setStudentNo(studyNoStr);
						infoVo.setStudentName(studentNameStr);
						infoVo.setStatus(statusStr.trim());
						studentInfoVos.add(infoVo);
						studyNoBuilder.append(studyNoStr).append("','");
					}
				}
				String sql  = "select si.STUDYNO,si.STUDENTNAME,si.RESOURCEID,si.BRANCHSCHOOLID,gd.STUDENTID  from EDU_ROLL_STUDENTINFO si left join EDU_TEACH_GRADUATEDATA gd on gd.STUDENTID=si.RESOURCEID and gd.ISDELETED=0 where si.isdeleted=0 and si.STUDYNO in('"+studyNoBuilder.toString()+"')";
				List<Map<String,Object>> mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql,null);
				Map<String,Map<String,Object>> studentInfoMap = ExBeanUtils.convertMapsToMap(mapList);
				StringBuilder studyNoForYes = new StringBuilder();
				StringBuilder studyNoForNo = new StringBuilder();

				for (int index = 0; index < studentInfoVos.size(); index++) {
					StudentInfoVo vo = studentInfoVos.get(index);
					if (studentInfoMap.containsKey(vo.getStudentNo())) {//判断学号是否存在
						Map<String, Object> stuMap = studentInfoMap.get(vo.getStudentNo());
						if ("degree".equals(type) && ExStringUtils.isBlank(stuMap.get("STUDENTID"))) {//学位审核材料导入
							returnMsg.append("<font color='red'>[第" + (index+1) + "行:"+vo.getStudentNo()+"]</font>"+"该学生毕业审核未通过！<br>");
							continue;
						}
						if (isBrSchool && !brSchoolid.equals(stuMap.get("BRANCHSCHOOLID"))) {//判断是否为当前教学点学生
							returnMsg.append("<font color='red'>[第" + (index+1) + "行:"+vo.getStudentNo()+"]</font>"+"该学生不是本教学点的学生！<br>");
							continue;
						}
						if ("是".equals(vo.getStatus())) {
							successCount++;
							studyNoForYes.append(vo.getStudentNo()).append("','");
						} else if ("否".equals(vo.getStatus())) {
							successCount++;
							studyNoForNo.append(vo.getStudentNo()).append("','");
						} else {
							returnMsg.append("<font color='red'>[第" + (index+1) + "行:"+vo.getStudentNo()+"]</font>"+"提交状态只能填入“是”和“否”！<br>");
							continue;
						}
					} else {
						returnMsg.append("<font color='red'>[第" + (index+1) + "行:"+vo.getStudentNo()+"]</font>"+"学号不存在！<br>");
					}
				}
				String mateColumn = "";
				if ("graduation".equals(type)) {
					mateColumn = "hasPracticeMaterials";
				} else if ("degree".equals(type)) {
					mateColumn = "hasDegreeMaterials";
				}
				sql = "update edu_roll_studentinfo si set si."+mateColumn+"='Y' where si.studyNo in('"+studyNoForYes.toString()+"') and (si."+mateColumn+"='N' or si."+mateColumn+" is null)";
				int count1 = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql,null);
				sql = "update edu_roll_studentinfo si set si."+mateColumn+"='N' where si.studyNo in('"+studyNoForNo.toString()+"') and (si."+mateColumn+"='Y' or si."+mateColumn+" is null)";
				int count2 = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(sql,null);
			}while(false);
			returnMap.put("totalCount", (count < 0 ? 0 : count));
			returnMap.put("successCount", successCount);
			returnMap.put("message", returnMsg.toString());
		} catch (FileNotFoundException e) {
			logger.error("导入文件不存在", e);
			returnMap.put("message", "导入失败");
		} catch (IOException e) {
			logger.error("读取文件出错", e);
			returnMap.put("message", "导入失败");
		} catch (Exception e) {
			logger.error("解析文件出错", e);
			returnMap.put("message", "导入失败");
		}
		return returnMap;
	}
}