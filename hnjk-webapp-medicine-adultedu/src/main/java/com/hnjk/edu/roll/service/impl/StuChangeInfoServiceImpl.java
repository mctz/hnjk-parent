
/**
 * <code>StuChangeInfoServiceImpl</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:32:49
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.roll.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.PropertyUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IReturnPremiumService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.service.IYearPaymentStandardService;
import com.hnjk.edu.finance.util.TonlyPayUtil;
import com.hnjk.edu.finance.vo.HeadVO;
import com.hnjk.edu.finance.vo.StudentPaymentInfoVo;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.teaching.model.CourseOrder;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.StudentMakeupList;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.model.UsualResults;
import com.hnjk.edu.teaching.service.ICourseOrderService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.IStudentMakeupListService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseService;
import com.hnjk.edu.teaching.service.ITeachingPlanCourseStatusService;
import com.hnjk.edu.teaching.vo.StuChangeInfoVo;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;

/**
 * <code>StuChangeInfoServiceImpl</code><p>
 * 学籍异动表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:32:49
 * @see 
 * @version 1.0
 */
@Transactional
@Service("stuchangeinfoservice")
public class StuChangeInfoServiceImpl extends BaseServiceImpl<StuChangeInfo> implements IStuChangeInfoService {

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService; //注入学籍服务
		
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;//注入学生学习计划
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;//学生考试成绩服务
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;//教学计划服务
	
	@Autowired
	@Qualifier("courseOrderService")
	private ICourseOrderService courseOrderService;
	
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("yearPaymentStandardService")
	private IYearPaymentStandardService yearPaymentStandardService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired	
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("studentMakeupListService")
	private IStudentMakeupListService studentMakeupListService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;
	
	@Autowired
	@Qualifier("returnPremiumService")
	private IReturnPremiumService returnPremiumService;
	
	@Override
	public Page findByCondition(Map<String, Object> condition, Page objPage)throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuilder hql = new StringBuilder(getHqlByCondition(condition,values));
		hql.append(" order by ").append(objPage.getOrderBy()).append(" ").append(objPage.getOrder());
		return exGeneralHibernateDao.findByHql(objPage, hql.toString(), values);
	}

	private String getHqlByCondition(Map<String, Object> condition,Map<String, Object> values) {
		StringBuilder hql = new StringBuilder(" from "+StuChangeInfo.class.getSimpleName()+" where isDeleted=0 ");
		hql.append(" and studentInfo.studentName IS NOT NULL");
		if(condition.containsKey("stuName")){
			hql.append(" and studentInfo.studentName like :stuName ");
			values.put("stuName","%"+ condition.get("stuName").toString().trim()+"%");
		}
		//查出转入本中心的及本中心的异动记录     20131215  添加转出异动信息
		if(condition.containsKey("branchSchool")){
			hql.append(" and (studentInfo.branchSchool.resourceid = :branchSchool or changeBrschool.resourceid = :branchSchool  or changeBeforeBrSchool =:branchSchool )");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){
			hql.append(" and studentInfo.major.resourceid = :major ");
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){
			hql.append(" and studentInfo.classic.resourceid = :classic ");
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("stuStatus")){
			hql.append(" and studentInfo.studentStatus = :stuStatus ");
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("stuChange")){
			hql.append(" and changeType = :stuChange ");
			values.put("stuChange", condition.get("stuChange"));
		}
		//学籍异动>>学生登录查看学籍异动信息
		if(condition.containsKey("acquiesceStudentid")){
			hql.append(" and studentInfo.resourceid = :acquiesceStudentid");
			values.put("acquiesceStudentid", condition.get("acquiesceStudentid"));
		}
		if(condition.containsKey("gradeid")){
			hql.append(" and studentInfo.grade.resourceid = :gradeid");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("learningStyle")){
			hql.append(" and (changeLearningStyle = :learningStyle or changeBeforeLearingStyle =:learningStyle ) ");
			values.put("learningStyle", condition.get("learningStyle"));
		}
		if(condition.containsKey("stuNum")){
			hql.append(" and studentInfo.studyNo like :stuNum");
			values.put("stuNum", "%" + condition.get("stuNum").toString().trim() + "%");
		}
		if(condition.containsKey("finalAuditStatus")){
			hql.append(" and (finalAuditStatus = :finalAuditStatus )  ");
			values.put("finalAuditStatus", condition.get("finalAuditStatus"));
		}
		if(condition.containsKey("applicationDate")){
			hql.append(" and applicationDate >= :applicationDate");
			values.put("applicationDate",ExDateUtils.convertToDate(condition.get("applicationDate").toString()));
		}
		if(condition.containsKey("auditDate")){
			hql.append(" and auditDate >= :auditDate");
			values.put("auditDate", ExDateUtils.convertToDate(condition.get("auditDate").toString()));
		}

		//新增按范围查询审核和申请时间
		if (condition.containsKey("applicationDateb")||condition.containsKey("applicationDatee")) {
			if (condition.containsKey("applicationDateb")) {
				hql.append(" and applicationDate >= :applicationDateb");
				values.put("applicationDateb",ExDateUtils.convertToDate(condition.get("applicationDateb").toString()));
			}
			if (condition.containsKey("applicationDatee")) {
				hql.append(" and applicationDate <= :applicationDatee");
				values.put("applicationDatee",ExDateUtils.convertToDate(condition.get("applicationDatee").toString()));
			}
		}
		if (condition.containsKey("auditDateb")||condition.containsKey("auditDatee")) {
			if (condition.containsKey("auditDateb")) {
				hql.append(" and auditDate >= :auditDateb");
				values.put("auditDateb",ExDateUtils.convertToDate(condition.get("auditDateb").toString()));
			}
			if (condition.containsKey("auditDatee")) {
				hql.append(" and auditDate <= :auditDatee");
				values.put("auditDatee",ExDateUtils.convertToDate(condition.get("auditDatee").toString()));
			}
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
		if(condition.containsKey("accountStatus")){
			hql.append(" and studentInfo.accountStatus = :accountStatus ");
			values.put("accountStatus", Integer.valueOf(condition.get("accountStatus").toString()));//无学籍状态
		}
		if(condition.containsKey("changeProperty")){
			String changeProperty = condition.get("changeProperty").toString();
			if("1".equals(changeProperty)){
				hql.append(" and studentInfo.branchSchool.resourceid <> changeBeforeBrSchool.resourceid  ");
				hql.append(" and finalAuditStatus ='Y'  ");
			}else if("2".equals(changeProperty)){
				hql.append(" and ( (changeBeforeMajorName is not null and studentInfo.major.majorName <> changeBeforeMajorName)  or (changeBeforeMajorName is null and studentInfo.major.resourceid <> changeBeforeTeachingGuidePlan.teachingPlan.major.resourceid) )");
				hql.append(" and finalAuditStatus ='Y'  ");
			}else if("3".equals(changeProperty)){
				hql.append(" and studentInfo.teachingType <> changeBeforeLearingStyle  ");
				hql.append(" and finalAuditStatus ='Y'  ");
			}
		}
		if(condition.containsKey("classesMasterId")){
			hql.append(" and 	studentInfo.classes.classesMasterId= :classesMasterId");

			values.put("classesMasterId", condition.get("classesMasterId"));
		}
		return hql.toString();
	}

	@Override
	public List findByCondition(Map<String, Object> condition)throws ServiceException {
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = getHqlByCondition(condition,values);
		return exGeneralHibernateDao.findByHql(hql, values);
	}
	
	/**
	 * 批量审核，更新学生学籍信息	,type = Y|N
	 */
	@Override
	public String auditBatchStudentChangeInfo(String[] ids,String type) throws ServiceException {
		StringBuffer msg  = new StringBuffer();
		if(null != ids){
			for(int i=0;i<ids.length;i++){				
				StuChangeInfo info = get(ids[i]);
				StudentInfo studentInfo = studentInfoService.get(info.getStudentInfo().getResourceid());
				
				String studentStatus = studentInfo.getStudentStatus();
				String changeType    = info.getChangeType();
				if("12".equals(changeType)&&!"12".equals(studentStatus)&&Constants.BOOLEAN_YES.equals(type)){
					msg.append(studentInfo.getStudyNo()+"_"+studentInfo.getStudentName()+"学籍不为休学，无法进行复学异动操作。");
					continue;
				}
				studentInfo.setEnableLogHistory(false);
				//当为审核通过的时候才去修改学籍
				if(Constants.BOOLEAN_YES.equals(type)){
					msg.append(auditPassStudent(info,studentInfo));
				}
				info.setFinalAuditStatus(type);
				info.setAuditDate(new Date());
				User user = SpringSecurityHelper.getCurrentUser();
				info.setAuditMan(user.getCnName());
				info.setAuditManId(user.getResourceid());
				
				update(info);
			}
			
		}
		return msg.toString();
	}

	/**
	 * 批量审批学籍异动信息，更新学生相关信息	,type = Y|N
	 * 
	 * @author Zik
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> auditBatchStuChangeInfo(String[] ids, String type,HttpServletRequest request)
			throws Exception {
		
		StringBuffer msg  = new StringBuffer();
		boolean ispass = true;
		int successNum = 0;
		Map<String, Object> msgMap = new HashMap<String, Object>(0);
		if(null != ids){
			for(int i=0;i<ids.length;i++){				
				StuChangeInfo info = get(ids[i]);
				StudentInfo studentInfo = studentInfoService.get(info.getStudentInfo().getResourceid());
				
				String studentStatus = studentInfo.getStudentStatus();
				String changeType    = info.getChangeType();
				if("12".equals(changeType)&&!"12".equals(studentStatus)&&Constants.BOOLEAN_YES.equals(type)){
					msg.append(Tools.colorStr(studentInfo.getStudyNo()+"_"+studentInfo.getStudentName(),"red")+"学籍不为休学，无法进行复学异动操作。");
					msg.append("<br/>" + Tools.dashLine("red", 1));
					continue;
				}
				studentInfo.setEnableLogHistory(false);
				//当为审核通过的时候才去修改学籍
				if(Constants.BOOLEAN_YES.equals(type)){
					Map<String, Object> messageMap;
					messageMap = auditStuChangeInfo(info,studentInfo,request);
					msg.append((String)messageMap.get("message"));
					ispass = (Boolean)messageMap.get("ispass");
				}
				if(ispass){
					successNum++;
					info.setFinalAuditStatus(type);
					info.setAuditDate(new Date());
					User user = SpringSecurityHelper.getCurrentUser();
					info.setAuditMan(user.getCnName());
					info.setAuditManId(user.getResourceid());
					
					update(info);
				}
			}
		}
		msgMap.put("msg", msg.toString());
		msgMap.put("successNum", successNum);
		return msgMap;
	}
	
	//保存或更新
	@Override
	public void saveOrUpdateStuChangeInfo(StuChangeInfo stuChangeInfo, StudentInfo stu) throws ServiceException {
		checkChangeCondition(stu,stuChangeInfo);
		super.saveOrUpdate(stuChangeInfo);
	}


	
	private String auditPassStudent(StuChangeInfo stuChangeInfo, StudentInfo stu){
		
		
		/*
		if(stuChangeInfo.getChangeType().equals("82")){ //更改学习中心
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
		}else if(stuChangeInfo.getChangeType().equals("23")){ //更改专业
			TeachingGuidePlan teachingGuidePlan = checkChangeCondition(stu,stuChangeInfo);
			//设置学生学籍，专业，教学计划(成教的转专业除了年级层次不变 单位 专业 学形 班级都会可能变)
			stu.setMajor(teachingGuidePlan.getTeachingPlan().getMajor());
			stu.setTeachingPlan(teachingGuidePlan.getTeachingPlan());
			stu.setTeachingGuidePlan(teachingGuidePlan);
			stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			stu.setClasses(stuChangeInfo.getChangeClass());
			updateStudentLearnPlan(stu);
		}else if(stuChangeInfo.getChangeType().equals("81")){//更改学习方式
			stu.setLearningStyle(stuChangeInfo.getChangeLearningStyle());			
		}else if(stuChangeInfo.getChangeType().equals("83")){//如果改层次	,	
			TeachingGuidePlan teachingGuidePlan = checkChangeCondition(stu,stuChangeInfo);
			//设置学生学籍的层次及教学计划
			stu.setClassic(teachingGuidePlan.getTeachingPlan().getClassic());
			stu.setTeachingPlan(teachingGuidePlan.getTeachingPlan());
			stu.setTeachingGuidePlan(teachingGuidePlan);
			updateStudentLearnPlan(stu);
		}else if(stuChangeInfo.getChangeType().equals("11")){//休学
			stu.setStudentStatus("12");
			stu.getSysUser().setEnabled(false);
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
		}else if(stuChangeInfo.getChangeType().equals("13")){//退学
			stu.setStudentStatus("13");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(false);
			}
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);//新增帐号状态的修改
		}else if(stuChangeInfo.getChangeType().equals("12")){//复学
			//成教的复学，就是成教异动中的转专业+更换年级
			TeachingGuidePlan teachingGuidePlan = checkChangeCondition(stu,stuChangeInfo);
			//设置学生学籍，专业，教学计划(成教的转专业除了年级层次不变 单位 专业 学形 班级都会可能变)
			stu.setMajor(teachingGuidePlan.getTeachingPlan().getMajor());
			stu.setTeachingPlan(teachingGuidePlan.getTeachingPlan());
			stu.setTeachingGuidePlan(teachingGuidePlan);
			stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			stu.setClasses(stuChangeInfo.getChangeClass());
			stu.setGrade(teachingGuidePlan.getGrade());
			stu.setStudentStatus("11");
			stu.setAccountStatus(Constants.BOOLEAN_TRUE);
			stu.getSysUser().setEnabled(true);
		}else if("101".equals(stuChangeInfo.getChangeType())){//过期，与退学相同处理
			stu.setStudentStatus("18");
			stu.getSysUser().setEnabled(false);
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
		}else if("100".equals(stuChangeInfo.getChangeType())){
			stu.setStudentStatus("21");
			stu.setAccountStatus(Constants.BOOLEAN_TRUE);
			stu.getSysUser().setEnabled(true);
		}else if("42".equals(stuChangeInfo.getChangeType())){
			stu.setStudentStatus("15");
			stu.getSysUser().setEnabled(false);
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
		}
		*/
		//如今可用的学籍异动为 转专业 23 转办学单位 82 转学习形式 81 休学11 复学12 退学13 6种
		//根据修改属性转专业<转办学单位<转学习形式 均涉及教学计划的转换  休学 复学 退学 归为修改学籍状态类 其中复学在转办学单位的基础上 还可修改年级
		String message = "";
		if("23".equals(stuChangeInfo.getChangeType())){ //更改专业
			//涉及修改 专业 班级 教学计划
			TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setMajor(tgp.getTeachingPlan().getMajor());
			stu.setTeachingPlan(tgp.getTeachingPlan());
			stu.setTeachingGuidePlan(tgp);
			stu.setClasses(stuChangeInfo.getChangeClass());
			message = updateStudentLearnPlan(stu);
		}else if("82".equals(stuChangeInfo.getChangeType())){//转办学单位
			//涉及修改 学院 专业 班级 教学计划
			//TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			//stu.setMajor(tgp.getTeachingPlan().getMajor());
			//stu.setTeachingPlan(tgp.getTeachingPlan());
			//stu.setTeachingGuidePlan(tgp);
			stu.setClasses(stuChangeInfo.getChangeClass());
			stu.getSysUser().setUnitId(stuChangeInfo.getChangeBrschool().getResourceid());
			message = updateStudentLearnPlan(stu);
		}else if("81".equals(stuChangeInfo.getChangeType())){//转学习形式
			//涉及修改 学习形式 学院 专业 班级 教学计划
			TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
			stu.setLearningStyle(stuChangeInfo.getChangeTeachingType());
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			stu.setMajor(tgp.getTeachingPlan().getMajor());
			stu.setTeachingPlan(tgp.getTeachingPlan());
			stu.setTeachingGuidePlan(tgp);
			stu.setClasses(stuChangeInfo.getChangeClass());
			stu.getSysUser().setUnitId(stuChangeInfo.getChangeBrschool().getResourceid());
			message = updateStudentLearnPlan(stu);
		}else if("11".equals(stuChangeInfo.getChangeType())){//休学
			//涉及修改 学籍状态
			stu.setStudentStatus("12");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(false);
			}
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
			studentInfoService.saveOrUpdate(stu);
		}else if("13".equals(stuChangeInfo.getChangeType())){//退学
			//涉及修改 学籍状态		
			stu.setStudentStatus("13");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(false);
			}
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
		}else if("12".equals(stuChangeInfo.getChangeType())){//复学
			//涉及修改 学籍状态 班级  学院 专业 班级 教学计划
			TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			stu.setMajor(tgp.getTeachingPlan().getMajor());
			stu.setTeachingPlan(tgp.getTeachingPlan());
			stu.setTeachingGuidePlan(tgp);
			stu.setClasses(stuChangeInfo.getChangeClass());
//			message = updateStudentLearnPlan(stu);
			stu.setStudentStatus("11");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(true);
			}
			stu.setAccountStatus(Constants.BOOLEAN_TRUE);
			stu.setGrade(tgp.getGrade());
			message = updateStudentLearnPlan(stu);
		}else if("C_24".equals(stuChangeInfo.getChangeType())){//校级转入
			//涉及修改 学籍状态 用户 更新学生的学习计划]
	    	message = updateStudentLearnPlan(stu);
			stu.setStudentStatus("11");
			stu.setLearningStyle(stuChangeInfo.getChangeTeachingType());
			stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
			String pwd =  CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue();//初始化密码
			String studentRoleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();//学生角色编码
			Role studentRole  = roleService.findUnique("from "+ Role.class.getSimpleName()+" where roleCode = ?", studentRoleCode);
			Set<Role> roleSet = new HashSet<Role>();
			roleSet.add(studentRole);
			User user = new User();
			user.setCnName(stu.getStudentName());//中文名
			user.setUsername(stu.getStudentBaseInfo().getCertNum());//账号，取学生身份证号
			user.setPassword(pwd);//密码，取系统配置的默认密码				
			user.setUnitId(stu.getBranchSchool().getResourceid());//教学中心ID
			user.setOrgUnit(stu.getBranchSchool());//学生所在教学中心					
			user.setRoles(roleSet);//学生角色
			user.setUserType(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue());//用户类型	
			user.setEnabled(true);
			userService.save(user);
			UserExtends userExtends = new UserExtends(UserExtends.USER_EXTENDCODE_DEFAULTROLLID,stu.getResourceid(),user);
			user.getUserExtends().put(UserExtends.USER_EXTENDCODE_DEFAULTROLLID, userExtends);
			userService.update(user);				
			stu.setSysUser(user);
			stu.setAccountStatus(Constants.BOOLEAN_TRUE);
		}
		studentInfoService.saveOrUpdate(stu);
		return message;
	}
	
	/**
	 * 审批学籍异动信息
	 * 
	 * @author Zik
	 * @param stuChangeInfo 学籍异动信息
	 * @param stu  学籍信息
	 * @return
	 * @throws Exception 
	 */
	private Map<String,Object> auditStuChangeInfo(StuChangeInfo stuChangeInfo, StudentInfo stu,HttpServletRequest request) throws Exception{
		//如今可用的学籍异动为 转专业 23 转办学单位 82 转学习形式 81 休学11 复学12 退学13 6种
		//根据修改属性转专业<转办学单位<转学习形式 均涉及教学计划的转换  休学 复学 退学 归为修改学籍状态类 其中复学在转办学单位的基础上 还可修改年级
		String message = "";
		boolean ispass = true;
		Map<String,Object> messageMap = new HashMap<String, Object>(0);
		// 是否使用通联支付
		String useTongLianPay = CacheAppManager.getSysConfigurationByCode("useTongLianPay").getParamValue();
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if("23".equals(stuChangeInfo.getChangeType())){ //更改专业
			//涉及修改 专业 班级 教学计划
			TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setMajor(tgp.getTeachingPlan().getMajor());
			stu.setTeachingPlan(tgp.getTeachingPlan());
			stu.setTeachingGuidePlan(tgp);
			stu.setClasses(stuChangeInfo.getChangeClass());
			stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
			message = updateStudentLearnPlan(stu);
			message += updateResumeSchoolResult_new(stu,null,null);
			//级联修改缴费模块的信息
			Map<String, Object> condition = new HashMap<String, Object>();
			List<Map<String, Object>> applyMapList = new ArrayList<Map<String,Object>>();
			
			condition.put("studentInfoId", stu.getResourceid());
			List<StudentPayment> list = studentPaymentService.findStudentPaymentByCondition(condition);
			for(StudentPayment studentPayment : list){
				studentPayment.setMajor(tgp.getTeachingPlan().getMajor());
			}
			studentPaymentService.batchSaveOrUpdate(list);

			condition.put("studentName", stu.getStudentName());
			List<TempStudentFee> tempList = tempStudentFeeService.findListByContidion(condition);
			for(TempStudentFee studentFee : tempList){
				studentFee.setMajor(tgp.getTeachingPlan().getMajor());
				
				Map<String, Object> applyMap = new HashMap<String, Object>();
				applyMap.put("STUDENT_ID", studentFee.getExamCertificateNo());	
				applyMap.put("MAJOR", studentFee.getMajor().getMajorName());
				applyMapList.add(applyMap);
			}
			tempStudentFeeService.batchSaveOrUpdate(tempList);
			
			if("Y".equals(useTongLianPay)){
				updateStudentFee(request,applyMapList);//同步到第三方
			}
			
			// 处理学费退费或补交逻辑
			// 是否需要自动生成退费补交订单
			String isAutoCreateRsOrder = CacheAppManager.getSysConfigurationByCode("isAutoCreateRsOrder").getParamValue();
			if(isAutoCreateRsOrder.equals("Y")){
				returnPremiumService.handleTuitionForStuChange(stuChangeInfo);
			}
					
			/*Set<TeachingPlanCourse> tpclist=tgp.getTeachingPlan().getTeachingPlanCourses();
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("unid", stu.getBranchSchool().getResourceid());
			for (TeachingPlanCourse tpc : tpclist) {//2016-10-10
				String term=getIsopenCourse(tpc.getResourceid(),stu.getBranchSchool().getResourceid());
				if(term!=null){//判断是否有开课记录
					String[] ARRYterm = term.split("_0");
					YearInfo yearInfo = yearInfoService.findUniqueByProperty("firstYear", Long.valueOf(ARRYterm[0]));//学院2016修改
					if(null == yearInfo ){
						logger.debug("年级规则错误");
						break;
					}
					param.put("courseid", tpc.getCourse().getResourceid());
					param.put("yearid", yearInfo.getResourceid());
					param.put("term", ARRYterm[1]);
					
					ExamInfo ei1=new ExamInfo();
					
					ExamInfo ei =new ExamInfo();
					ei=getExaminfo(param);
					StudentLearnPlan newLearnPlan = new StudentLearnPlan();
					if(ei==null){
						ei1.setCourse(tpc.getCourse());
						ei1.setExamType("0");
						examInfoService.save(ei1);
						newLearnPlan.setExamInfo(ei1);//学院2016修改
					}else{
						newLearnPlan.setExamInfo(ei);//学院2016修改
					}
					newLearnPlan.setStudentInfo(stu);
					newLearnPlan.setStatus(2);
					newLearnPlan.setTeachingPlanCourse(tpc);
					
					newLearnPlan.setYearInfo(yearInfo);
					newLearnPlan.setTerm(ARRYterm[1]);
					studentLearnPlanService.saveOrUpdate(newLearnPlan);
				}
				
			}*/
		}else if("82".equals(stuChangeInfo.getChangeType())){//转办学单位
			//涉及修改 学院 专业 班级 教学计划
			//TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			stu.setClasses(stuChangeInfo.getChangeClass());
			if("11846".equals(schoolCode)){
				TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
				stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
				stu.setTeachingPlan(tgp.getTeachingPlan());
				stu.setTeachingGuidePlan(tgp);
			}
			
			//更改用户教学点信息
			User user = stu.getSysUser();
			user.setUnitId(stu.getBranchSchool().getResourceid());//教学中心ID
			user.setOrgUnit(stu.getBranchSchool());//学生所在教学中心					
			userService.update(user);				
			stu.setSysUser(user);
			//stu.setMajor(tgp.getTeachingPlan().getMajor());
			//stu.setTeachingPlan(tgp.getTeachingPlan());
			//stu.setTeachingGuidePlan(tgp);
			message = updateStudentLearnPlan(stu);
			
			//级联修改缴费模块的信息
			Map<String, Object> condition = new HashMap<String, Object>();
			List<Map<String, Object>> applyMapList = new ArrayList<Map<String,Object>>();
			
			condition.put("studentInfoId", stu.getResourceid());
			List<StudentPayment> list = studentPaymentService.findStudentPaymentByCondition(condition);
			for(StudentPayment studentPayment : list){
				studentPayment.setBranchSchool(stu.getBranchSchool());
			}
			studentPaymentService.batchSaveOrUpdate(list);
			
			condition.put("studentName", stu.getStudentName());
			List<TempStudentFee> tempList = tempStudentFeeService.findListByContidion(condition);
			for(TempStudentFee studentFee : tempList){
				studentFee.setUnit(stu.getBranchSchool());
					
				Map<String, Object> applyMap = new HashMap<String, Object>();
				applyMap.put("STUDENT_ID", studentFee.getExamCertificateNo());	
				applyMap.put("COLLEGE", studentFee.getUnit().getUnitName());
				applyMapList.add(applyMap);
			}
			tempStudentFeeService.batchSaveOrUpdate(tempList);
			if("Y".equals(useTongLianPay)){
				updateStudentFee(request,applyMapList);
			}
		}else if("81".equals(stuChangeInfo.getChangeType())){//转学习形式
			//涉及修改 学习形式 学院 专业 班级 教学计划
			TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
			stu.setLearningStyle(stuChangeInfo.getChangeTeachingType());
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());
			stu.setMajor(tgp.getTeachingPlan().getMajor());
			stu.setTeachingPlan(tgp.getTeachingPlan());
			stu.setTeachingGuidePlan(tgp);
			stu.setClasses(stuChangeInfo.getChangeClass());
			//更改用户教学点信息
			User user = new User();
			user = stu.getSysUser();
			user.setUnitId(stu.getBranchSchool().getResourceid());//教学中心ID
			user.setOrgUnit(stu.getBranchSchool());//学生所在教学中心					
			userService.update(user);				
			stu.setSysUser(user);
			message = updateStudentLearnPlan(stu);
			
			//级联修改缴费模块的信息
			Map<String, Object> condition = new HashMap<String, Object>();
			List<Map<String, Object>> applyMapList = new ArrayList<Map<String,Object>>();
			
			condition.put("studentInfoId", stu.getResourceid());
			List<StudentPayment> list = studentPaymentService.findStudentPaymentByCondition(condition);
			for(StudentPayment studentPayment : list){
				studentPayment.setBranchSchool(stu.getBranchSchool());
				studentPayment.setMajor(tgp.getTeachingPlan().getMajor());
			}
			studentPaymentService.batchSaveOrUpdate(list);

			condition.put("studentName", stu.getStudentName());
			List<TempStudentFee> tempList = tempStudentFeeService.findListByContidion(condition);
			for(TempStudentFee studentFee : tempList){
				studentFee.setUnit(stu.getBranchSchool());
				studentFee.setMajor(tgp.getTeachingPlan().getMajor());
				
				Map<String, Object> applyMap = new HashMap<String, Object>();
				applyMap.put("STUDENT_ID", studentFee.getExamCertificateNo());	
				applyMap.put("COLLEGE", studentFee.getUnit().getUnitName());
				applyMap.put("MAJOR", studentFee.getMajor().getMajorName());
				applyMapList.add(applyMap);
			}
			tempStudentFeeService.batchSaveOrUpdate(tempList);
			if("Y".equals(useTongLianPay)){
				updateStudentFee(request,applyMapList);			
			}
			// 处理学费退费或补交逻辑
			// 是否需要自动生成退费补交订单
			String isAutoCreateRsOrder = CacheAppManager.getSysConfigurationByCode("isAutoCreateRsOrder").getParamValue();
			if(isAutoCreateRsOrder.equals("Y")){
				returnPremiumService.handleTuitionForStuChange(stuChangeInfo);
			}
		}else if("11".equals(stuChangeInfo.getChangeType())){//休学
			//涉及修改 学籍状态
			stu.setStudentStatus("12");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(false);
			}
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
			// 处理学费退费或补交逻辑
			// 是否需要自动生成退费补交订单
			String isAutoCreateRsOrder = CacheAppManager.getSysConfigurationByCode("isAutoCreateRsOrder").getParamValue();
			if(isAutoCreateRsOrder.equals("Y")){
				returnPremiumService.handleTuitionForStuChange(stuChangeInfo);
			}
		}else if("13".equals(stuChangeInfo.getChangeType())){//退学
			//涉及修改 学籍状态		
			stu.setStudentStatus("13");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(false);
			}
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
			
			// 删掉未付款的预缴费订单
			tempStudentFeeService.deleteByExamCertificateNo(stu.getExamCertificateNo());
			// 处理学费退费或补交逻辑
			// 是否需要自动生成退费补交订单
			String isAutoCreateRsOrder = CacheAppManager.getSysConfigurationByCode("isAutoCreateRsOrder").getParamValue();
			if(isAutoCreateRsOrder.equals("Y")){
				returnPremiumService.handleTuitionForStuChange(stuChangeInfo);
			}
		}else if("12".equals(stuChangeInfo.getChangeType())){//复学
			//涉及修改 学籍状态 班级  学院 专业 班级 教学计划
			TeachingGuidePlan tgp = stuChangeInfo.getChangeTeachingGuidePlan();
			stu.setBranchSchool(stuChangeInfo.getChangeBrschool());//教学点
			stu.setMajor(tgp.getTeachingPlan().getMajor());//专业
			stu.setTeachingPlan(tgp.getTeachingPlan());//基础教学计划
			stu.setTeachingGuidePlan(tgp);//年级教学计划
			stu.setClasses(stuChangeInfo.getChangeClass());//班级
			stu.setAccountStatus(Constants.BOOLEAN_TRUE);
			stu.setGrade(tgp.getGrade());
			
			message = updateMarkDeleteResults(stu);//删除标记删除的成绩
			message += updateStudentLearnPlan(stu);//理论教学计划的课程信息
			message += updateResumeSchoolResult_new(stu,null,null);
			stu.setStudentStatus("11");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(true);
			}
			
			// 处理缴费逻辑
			handlePayment(stu, request, useTongLianPay, tgp,stuChangeInfo);

		}else if("C_24".equals(stuChangeInfo.getChangeType())){//校级转入
			//涉及修改 学籍状态 用户 更新学生的学习计划]
			    User haveUser = userService.findUnique("from User where username=? and isDeleted=0", stu.getStudentBaseInfo().getCertNum());
			    /**
			     * 由于存在专本连的的情况，因此允许账号存在，只更新学籍，保留原有账号信息，但重置账号的密码为11，以及设置账号状态为可用  20160308 lion
			     */
			    if(haveUser != null){
			    	message = Tools.colorStr(stu.getStudentBaseInfo().getCertNum(),"red") + " 账号已存在,请检查是否是专本连读的学生或是输入身份证号错误";
//			    	ispass = false;
			    	message = updateStudentLearnPlan(stu);
					stu.setStudentStatus("11");
					stu.setLearningStyle(stuChangeInfo.getChangeTeachingType());
					stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
					String pwd =  CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue();//初始化密码
					
					User user = new User();
					user = haveUser;
					user.setPassword(pwd);//密码，取系统配置的默认密码				
					user.setUnitId(stu.getBranchSchool().getResourceid());//教学中心ID
					user.setOrgUnit(stu.getBranchSchool());//学生所在教学中心					
					
					user.setUserType(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue());//用户类型	
					user.setEnabled(true);
					//userService.update(user);
					UserExtends userExtends = new UserExtends(UserExtends.USER_EXTENDCODE_DEFAULTROLLID,stu.getResourceid(),user);
					user.getUserExtends().put(UserExtends.USER_EXTENDCODE_DEFAULTROLLID, userExtends);
					userService.update(user);				
					stu.setSysUser(user);
					stu.setAccountStatus(Constants.BOOLEAN_TRUE);			    	
			    } else {
			    	message = updateStudentLearnPlan(stu);
					stu.setStudentStatus("11");
					stu.setLearningStyle(stuChangeInfo.getChangeTeachingType());
					stu.setTeachingType(stuChangeInfo.getChangeTeachingType());
					String pwd =  CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue();//初始化密码
					String studentRoleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();//学生角色编码
					Role studentRole  = roleService.findUnique("from "+ Role.class.getSimpleName()+" where roleCode = ?", studentRoleCode);
					Set<Role> roleSet = new HashSet<Role>();
					roleSet.add(studentRole);
					User user = new User();
					user.setCnName(stu.getStudentName());//中文名
					user.setUsername(stu.getStudentBaseInfo().getCertNum());//账号，取学生身份证号
					user.setPassword(pwd);//密码，取系统配置的默认密码				
					user.setUnitId(stu.getBranchSchool().getResourceid());//教学中心ID
					user.setOrgUnit(stu.getBranchSchool());//学生所在教学中心					
					user.setRoles(roleSet);//学生角色
					user.setUserType(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue());//用户类型	
					user.setEnabled(true);
					userService.save(user);
					UserExtends userExtends = new UserExtends(UserExtends.USER_EXTENDCODE_DEFAULTROLLID,stu.getResourceid(),user);
					user.getUserExtends().put(UserExtends.USER_EXTENDCODE_DEFAULTROLLID, userExtends);
					userService.update(user);				
					stu.setSysUser(user);
					stu.setAccountStatus(Constants.BOOLEAN_TRUE);
			    }
		}else if("slowGraduation".equals(stuChangeInfo.getChangeType())){// 缓毕业
			//涉及修改 学籍状态
			stu.setStudentStatus("25");
			if(null!=stu.getSysUser()){ 
				stu.getSysUser().setEnabled(false);
			}
			stu.setAccountStatus(Constants.BOOLEAN_FALSE);
		}
		
		if(ExStringUtils.isNotEmpty(message)) {
			message += "<br/>"+Tools.dashLine("red", 1);
		}
		if(ispass) {
			studentInfoService.saveOrUpdate(stu);
		}
		messageMap.put("message", message);
		messageMap.put("ispass", ispass);
		return messageMap;
	}
	
	/**
	 * 处理缴费情况
	 * @param stu
	 * @param request
	 * @param useTongLianPay
	 * @param tgp
	 */
	private void handlePayment(StudentInfo stu, HttpServletRequest request,String useTongLianPay, TeachingGuidePlan tgp,StuChangeInfo stuChangeInfo) {
		List<Map<String, Object>> applyMapList = new ArrayList<Map<String,Object>>();
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("studentInfoId", stu.getResourceid());
		List<StudentPayment> studentPaymentList = studentPaymentService.findStudentPaymentByCondition(condition);
		StudentPayment studentPayment = null;
		if(ExCollectionUtils.isNotEmpty(studentPaymentList)){
			studentPayment = studentPaymentList.get(0);
		}
		if(studentPayment != null){
			TeachingGuidePlan beforeTgp = stuChangeInfo.getChangeBeforeTeachingGuidePlan();
			Major beforeMajor = beforeTgp.getTeachingPlan().getMajor();
			Grade beforeGrade = beforeTgp.getGrade();
			// 获取最新的休学或退学异动信息
			StuChangeInfo _stuChangeInfo = findLatestByChangeType("'12','13'");
			if(_stuChangeInfo != null){
				StudentPaymentInfoVo _nowPaymentInfo = yearPaymentStandardService.findByCondition(beforeGrade.getResourceid(),
						tgp.getTeachingPlan().getMajor().getResourceid(), studentPayment.getChargeTerm()+1);
				Double creditFee = 0d;
				Date creditEndDate = null;
				if(_nowPaymentInfo != null){
					creditFee = _nowPaymentInfo.getCreditFee()==null?0:_nowPaymentInfo.getCreditFee();
					creditEndDate = _nowPaymentInfo.getCreditEndDate();
				}
				// 年级不同，专业相同
				if(!beforeGrade.getResourceid().equals(tgp.getGrade().getResourceid()) 
						&& tgp.getTeachingPlan().getMajor().getResourceid().equals(beforeMajor.getResourceid())){
					StudentPaymentInfoVo _beforePaymentInfo = yearPaymentStandardService.findByCondition(tgp.getGrade().getResourceid(),
							beforeMajor.getResourceid(), studentPayment.getChargeTerm()+1);
					if(_beforePaymentInfo != null){
						// 复学日期开始缴费日期之后并且休学或退学日期在开始缴费日期之前
						if((_beforePaymentInfo.getPayBeginDate().after(_stuChangeInfo.getAuditDate())
								&&_beforePaymentInfo.getPayBeginDate().before(new Date()))){
							creditFee = _beforePaymentInfo.getCreditFee()==null?0:_beforePaymentInfo.getCreditFee();
							creditEndDate = creditEndDate==null?_beforePaymentInfo.getCreditEndDate():creditEndDate;
						}
					}
				} else {
					if(_nowPaymentInfo == null || !(_nowPaymentInfo.getPayBeginDate().after(_stuChangeInfo.getAuditDate())
							&&_nowPaymentInfo.getPayBeginDate().before(new Date()))){
						creditFee = 0d;
						creditEndDate = null;
					} 
				}
				if(creditEndDate != null){
					Double recpayFee = studentPayment.getRecpayFee()==null?0:studentPayment.getRecpayFee();
					Double facepayFee = studentPayment.getFacepayFee()==null?0:studentPayment.getFacepayFee();
					Double derateFee = studentPayment.getDerateFee()==null?0:studentPayment.getDerateFee();
					Double _facepayFee = facepayFee + derateFee;
					Double _recpayFee = recpayFee + creditFee;
					studentPayment.setChargeTime(creditEndDate);
					studentPayment.setChargeTerm(studentPayment.getChargeTerm()+1);
					studentPayment.setRecpayFee(_recpayFee);// 应缴金额
					if(_facepayFee==0){
						studentPayment.setChargeStatus("0");
					} else if(_facepayFee>0&& _facepayFee < _recpayFee){
						studentPayment.setChargeStatus("-1");
					} else if (_facepayFee.equals(_recpayFee)){
						studentPayment.setChargeStatus("1");
					}
				}
			}
			
			studentPayment.setBranchSchool(stu.getBranchSchool());
			studentPayment.setMajor(tgp.getTeachingPlan().getMajor());
			studentPayment.setGrade(tgp.getGrade());
			studentPaymentService.saveOrUpdate(studentPayment);
			
			condition.put("studentName", stu.getStudentName());
			List<TempStudentFee> tempList = tempStudentFeeService.findListByContidion(condition);
			// 获取当前缴费批次
			String batchNo = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
			boolean hasRecord =false; 
			for(TempStudentFee studentFee : tempList){
				studentFee.setUnit(stu.getBranchSchool());
				studentFee.setMajor(tgp.getTeachingPlan().getMajor());
				studentFee.setGrade(tgp.getGrade());
				if(studentFee.getBatchNo().equals(batchNo)){
					hasRecord = true;
				}else if("1".equals(studentFee.getPayStatus())) {// 未付款
					studentFee.setIsDeleted(1);
				}
				
				Map<String, Object> applyMap = new HashMap<String, Object>();
				applyMap.put("STUDENT_ID", studentFee.getExamCertificateNo());	
				applyMap.put("COLLEGE", studentFee.getUnit().getUnitName());
				applyMap.put("MAJOR", studentFee.getMajor().getMajorName());
				applyMapList.add(applyMap);
			}
			
			if(!hasRecord){// 没有当前缴费批次的记录，则添加
				double recpayFee = studentPayment.getRecpayFee()==null?0:studentPayment.getRecpayFee();
				double feacepayFee = studentPayment.getFacepayFee()==null?0:studentPayment.getFacepayFee();
				double amount = recpayFee-feacepayFee;
				if(!("1".equals(studentPayment.getChargeStatus()) || amount==0)){
					String uniqueId = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
					String examCertificateNo = studentPayment.getStudentInfo().getExamCertificateNo();
					if("1".equals(uniqueId)){
						examCertificateNo = studentPayment.getStudentInfo().getEnrolleeCode();
					}
					// 当前年
					String currentYear = CacheAppManager.getSysConfigurationByCode("currentYear").getParamValue();
					YearInfo year = yearInfoService.getByFirstYear(Long.valueOf(currentYear));
					tempList.add(tempStudentFeeService.createTempStudentFee(batchNo, studentPayment, examCertificateNo,amount,year));
				}
			}
			
			if(ExCollectionUtils.isNotEmpty(tempList)){
				tempStudentFeeService.batchSaveOrUpdate(tempList);
			}
		
		}
		
		if("Y".equals(useTongLianPay)){
			updateStudentFee(request,applyMapList);//申请接口
		}
	}

	
	//@Override
	//public void auditPassStudentChangeInfo(StuChangeInfo stuChangeInfo, StudentInfo stu) throws ServiceException{		
	//	saveOrUpdate(stuChangeInfo);
		
	//}
	
	//校验这个学生是否在他所在年级有对应的教学计划，如果有，则设置到学籍信息中
	/*
	 * @param stu 学生学籍
	 * @param stuChangeInfo 学生学籍异动信息	
	 *  这个验证用在了保存学籍异动记录之前的验证，而且也用在了审核异动时，读取年度教学计划
	 */
	private TeachingGuidePlan checkChangeCondition(StudentInfo stu,StuChangeInfo stuChangeInfo) throws WebException{		
		TeachingGuidePlan teachingGuidePlans = null;
		if("23".equals(stuChangeInfo.getChangeType())){//转专业
			try{
			//首先，查找关联到学习中心的学习计划是否唯一
				teachingGuidePlans = teachingGuidePlanService.findUnique("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=? and grade.resourceid = ?" +
						" and teachingPlan.major.resourceid =? and teachingPlan.classic.resourceid = ? and teachingPlan.schoolType =? and ispublished = ? and teachingPlan.orgUnit.unitName=? ", 
						0,
						stu.getGrade().getResourceid(),
						stuChangeInfo.getChangeMajor().getResourceid(),
						stu.getClassic().getResourceid(),
						//stu.getTeachingType(),
						stuChangeInfo.getChangeTeachingType(),
						Constants.BOOLEAN_YES,
//						stu.getBranchSchool().getUnitName()
						stuChangeInfo.getChangeBrschool().getUnitName()
						);
			}catch(Exception e){
				throw new WebException("有多个关联到该学生所在的学习中心的"+stu.getGrade().getGradeName()+"-"+stuChangeInfo.getChangeMajor().getMajorName()+"-"+stu.getClassic().getClassicName()+"的教学计划！请与学习中心人员联系.");
			}
			try{
			//如果查找关联到学习中心的学习计划为空，再查找未关联学习中心的学习计划
				if(null == teachingGuidePlans){
					teachingGuidePlans = teachingGuidePlanService.findUnique("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=? and grade.resourceid = ?" +
							" and teachingPlan.major.resourceid =? and teachingPlan.classic.resourceid = ? and teachingPlan.schoolType =? and ispublished = ? and teachingPlan.orgUnit is null  ", 
							0,
							stu.getGrade().getResourceid(),
							stuChangeInfo.getChangeMajor().getResourceid(),
							stu.getClassic().getResourceid(),
							//stu.getTeachingType(),
							stuChangeInfo.getChangeTeachingType(),
							Constants.BOOLEAN_YES
							);
				}
			}catch(Exception e){
				throw new WebException("该学生所在的学习中心有多个可用的"+stu.getGrade().getGradeName()+"-"+stuChangeInfo.getChangeMajor().getMajorName()+"-"+stu.getClassic().getClassicName()+"的教学计划！请与学习中心人员联系.");
			}
			if(null == teachingGuidePlans){
				throw new WebException("该学生所在的年级没有 "+stuChangeInfo.getChangeMajor().getMajorName()+"-"+stu.getClassic().getClassicName()+"这个教学计划！请与学习中心人员联系.");
				
			}			
			
		}else if("83".equals(stuChangeInfo.getChangeType())){//转层次
			
//			 teachingGuidePlans = teachingGuidePlanService.findUnique("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=? and grade.resourceid = ?" +
//					" and teachingPlan.major.resourceid =? and teachingPlan.classic.resourceid = ? and teachingPlan.schoolType =? and ispublished = ?", 
//					0,
//					stu.getGrade().getResourceid(),
//					stu.getMajor().getResourceid(),
//					stuChangeInfo.getChangeClassicId().getResourceid() ,
//					stu.getTeachingType(),
//					Constants.BOOLEAN_YES
//					);
//			 以上是对于之前的转层次的验证，验证符合条件下的年度教学计划是否有且只有一个，但现在方式改为转层次时，由用户选择具体的年度教学计划,所以只要验证记录的异动后的年度教学计划唯一即可。
			 if(null!=stuChangeInfo.getChangeTeachingGuidePlan()){
				 teachingGuidePlans = teachingGuidePlanService.findUnique("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=? and resourceid=? and ispublished = ?", 
							0,
							stuChangeInfo.getChangeTeachingGuidePlan().getResourceid(),
							Constants.BOOLEAN_YES
							);
			 }else{
				 //对于之前生成的转层次异动记录
				 teachingGuidePlans = teachingGuidePlanService.findUnique("from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted=? and grade.resourceid = ?" +
							" and teachingPlan.major.resourceid =? and teachingPlan.classic.resourceid = ? and teachingPlan.schoolType =? and ispublished = ?", 
							0,
							stu.getGrade().getResourceid(),
							stu.getMajor().getResourceid(),
							stuChangeInfo.getChangeClassicId().getResourceid() ,
							stu.getTeachingType(),
							Constants.BOOLEAN_YES
							);
			 }
			if(null == teachingGuidePlans){
				//throw new WebException("你所在的年级没有 '"+stu.getMajor().getMajorName()+"("+stuChangeInfo.getChangeClassicId().getClassicName()+")' 这个教学计划！<br/>请与学习中心人员联系.");
				throw new WebException("您所选择的年度教学计划的数据有误！<br/>请与学习中心人员联系.");
				
			}			
		}
		return teachingGuidePlans;
	}
	@Override
	public String updateResumeSchoolResult_new(StudentInfo stu, TeachingPlanCourse plancourse, TeachingPlanCourseStatus cs) throws WebException{
		String message = "";
		List<ExamResults> resultreplace    = new ArrayList<ExamResults>();
		List<StudentMakeupList> makeuplist = new ArrayList<StudentMakeupList>();
		List<ExamResults> resultreplacetmp = new ArrayList<ExamResults>();
		List<Course> courses = new ArrayList<Course>();
		List<String> courseids = new ArrayList<String>();
		List<TeachingPlanCourse> plancourses = new ArrayList<TeachingPlanCourse>();
		if(plancourse!=null){
			courses.add(plancourse.getCourse());
			courseids.add(plancourse.getCourse().getResourceid());
			plancourses.add(plancourse);
		}else{
			plancourses=teachingPlanCourseService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("teachingPlan", stu.getTeachingPlan()));			
			for(TeachingPlanCourse p:plancourses){
				courses.add(p.getCourse());
				courseids.add(p.getCourse().getResourceid());
			}
		}
		//1、替换成绩		
		
		//查找一个学生所有的成绩  		
		Map<String ,Object> condition = new HashMap<String, Object>();
		condition.put("stu", stu.getResourceid());
		condition.put("courseids", courseids);
		//加上课程ID过滤，只替换匹配到的课程
		String hql = " from "+ExamResults.class.getSimpleName()+" where isDeleted = 0 and studentInfo.resourceid=:stu and course.resourceid in(:courseids) ";
		resultreplacetmp   = studentExamResultsService.findByHql(hql,condition);
		for(ExamResults er :resultreplacetmp){
			String memo=er.getMemo()==null?"":er.getMemo();
			try {
				memo+="/time："+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME);
			} catch (ParseException e) {				
			}
			memo+="_esid:"+er.getExamsubId();
			memo+="_pcid:"+er.getMajorCourseId();
			memo+="_eiid:"+er.getExamInfo().getResourceid();
			String year_term="";
			String year="";
			String term="";
			//获取应替换的年度及学期
			year_term = getReplaceYearandTerm(stu,er,er.getIsMakeupExam(),cs);
			year=year_term.substring(0,4);
			term=year_term.substring(6,7);
			//examCourseType =1 只替换面授课程
			String hql1 = " from "+ExamInfo.class.getSimpleName() +" where isDeleted = 0 and examSub.yearInfo.firstYear=? and examSub.term=? and examSub.examType=? and course.resourceid=? and examCourseType='1' ";
			List<ExamInfo> examinfolist = new ArrayList<ExamInfo>();
			examinfolist =examInfoService.findByHql(hql1, Long.parseLong(year),String.valueOf(term),er.getIsMakeupExam(),er.getCourse().getResourceid());
			if(examinfolist!=null && examinfolist.size()>=1){//有且只有一条记录时，直接替换批次
				er.setExamsubId(examinfolist.get(0).getExamSub().getResourceid());
				er.setExamInfo(examinfolist.get(0));				
				if(getPlancourse(er.getCourse(),plancourses)==null){
					message = "替换"+er.getCourse().getCourseName()+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getIsMakeupExam())+"考试成绩的教学计划课程ID出错，请联系管理员";
					throw new WebException("替换"+er.getCourse().getCourseName()+"  "+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getIsMakeupExam())+"考试成绩的教学计划课程ID出错，请联系管理员");
				}
				er.setMajorCourseId(getPlancourse(er.getCourse(),plancourses).getResourceid());//修改PlancourseId
				er.setMemo(memo);
				resultreplace.add(er);
			}else if(examinfolist==null||examinfolist.size()==0){//为空时，先添加examInfo的记录,再进行替换
				String hql2 = " from "+ ExamSub.class.getSimpleName()+" where isDeleted = 0 and yearInfo.firstYear=? and term=? and examType=? ";
				List<ExamSub> esList= new ArrayList<ExamSub>();
				esList= examSubService.findByHql(hql2,Long.parseLong(year),String.valueOf(term),er.getIsMakeupExam());
				if(esList==null||esList.size()==0){//如果没有考试信息，直接使用异常进行中断
					message = "请添加"+year+"第"+term+"学期"+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getIsMakeupExam())+"学期的考试批次";
					throw new WebException("请添加"+year+"第"+term+"学期"+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getIsMakeupExam())+"学期的考试批次");
				}else{//添加一条考试examInfo的记录
					ExamInfo ei = new ExamInfo();
					ei.setExamSub(esList.get(0));
					ei.setCourse(er.getCourse());
					ei.setIsOutplanCourse("0");
					ei.setExamCourseType(1);//面授课程
					ei.setCourseScoreType("11");
					ei.setFacestudyScorePer(esList.get(0).getFacestudyScorePer());
					ei.setFacestudyScorePer2(esList.get(0).getFacestudyScorePer2());
					ei.setFacestudyScorePer3(esList.get(0).getFacestudyScorePer3());
					ei.setStudyScorePer(esList.get(0).getWrittenScorePer());
					examInfoService.saveOrUpdate(ei);
					er.setExamsubId(esList.get(0).getResourceid());
					er.setExamInfo(ei);
					if(getPlancourse(er.getCourse(),plancourses)==null){
						message = "替换"+er.getCourse().getCourseName()+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getIsMakeupExam())+"考试成绩的教学计划课程ID出错，请联系管理员";
						throw new WebException("替换"+er.getCourse().getCourseName()+"  "+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getIsMakeupExam())+"考试成绩的教学计划课程ID出错，请联系管理员");
					}
					er.setMajorCourseId(getPlancourse(er.getCourse(),plancourses).getResourceid());//修改PlancourseId
					er.setMemo(memo);
					resultreplace.add(er);
				}
			}			
		}
		//2、替换补考名单
		List<StudentMakeupList> makeup = new ArrayList<StudentMakeupList>();
		makeup = studentMakeupListService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo", stu),
				Restrictions.in("course",courses));
		if(makeup!=null&&makeup.size()>0){
			for(StudentMakeupList sm:makeup){
				String memo=sm.getMemo()==null?"":sm.getMemo();
				try {
					memo+="/time："+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME);
				} catch (ParseException e) {				
				}
				memo+="_nesid:"+sm.getNextExamSubId();
				memo+="_pcid:"+sm.getTeachingPlanCourse().getResourceid();
				String examType=examSubService.get(sm.getNextExamSubId()).getExamType();
				String year_term=getReplaceYearandTerm(stu,sm.getExamResults(),examType,cs);
				String year=year_term.substring(0,4);
				String term=year_term.substring(6,7);
				String hql2 = " from "+ ExamSub.class.getSimpleName()+" where isDeleted = 0 and yearInfo.firstYear=? and term=? and examType=? ";
				List<ExamSub> examList = new ArrayList<ExamSub>();
				//年度、学期、考试类型
				examList = examSubService.findByHql(hql2, Long.parseLong(year),term,examType);
				if(examList!=null&&examList.size()==1){
					sm.setNextExamSubId(examList.get(0).getResourceid());
					if(getPlancourse(sm.getCourse(),plancourses)==null){
						message = "替换"+sm.getCourse().getCourseName()+JstlCustomFunction.dictionaryCode2Value("ExamResult", examType)+"  补考名单的教学计划课程ID出错，请联系管理员";
						throw new WebException("替换"+sm.getCourse().getCourseName()+"  "+JstlCustomFunction.dictionaryCode2Value("ExamResult",examType)+"  补考名单的教学计划课程ID出错，请联系管理员");
					}
					sm.setTeachingPlanCourse(getPlancourse(sm.getCourse(),plancourses));
					sm.setMemo(memo);
					makeuplist.add(sm);
				}
			}
		}		
		studentExamResultsService.batchSaveOrUpdate(resultreplace);
		studentMakeupListService.batchSaveOrUpdate(makeuplist);
		return message;
	}
	private String getReplaceYearandTerm(StudentInfo stu,ExamResults er,String examType,TeachingPlanCourseStatus cs) {
		
		//规则1：目前适用于广大,补考次数为3
		/**
		 * 正考：开课学期
		 * 一补：开课的一下学期，第6学期仍在最6学期
		 * 二补：1-4学期开课的课程在第5学期，5-6学期开课的在第6学期
		 * 结补：第6学期
		 */
		//规则2：目前适用于广西医、安徽医、桂林医等，补考次数为2
		/**
		 * 正考：开课学期
		 * 一补：开课的一下学期，第6学期仍在最6学期
		 * 结补：第6学期
		 */
		//规则3：目前适用于广东医，补考次数为N
		/**
		 * 正考：开课学期
		 * 一补：开课的一下学期，第6学期仍在最6学期 
		 */
		//
		String rule= CacheAppManager.getSysConfigurationByCode("examinationRule").getParamValue();
		//正考:所有学校统一，开课学期即成绩所在学期
		String year_term="";
		if("N".equals(examType)){
			year_term=getOpenyear(stu,cs,er.getCourse());
			if("error".equals(year_term)){
				throw new WebException("替换成绩："+er.getCourse().getCourseName()+"，无法查询到开课记录");
			}else{				
				return year_term;
			}
		}
		//一补:第6学期处理方式不一致	
		else if("Y".equals(examType)){
			year_term=getOpenyear(stu,cs,er.getCourse());
			if("error".equals(year_term)){
				throw new WebException("替换成绩："+er.getCourse().getCourseName()+"，无法查询到开课记录");
			}else{
				//判断是否是第6学期
				if(isLastTerm(stu, year_term)&&!"3".equals(rule)){//是第6学期，且不是第3种规则，直接返回学期
					return year_term;
				}else{//学期+1
					String year=year_term.substring(0,4);
					String term=year_term.substring(6,7);
					int tmpt=Integer.parseInt(term);
					int tmpy=Integer.parseInt(year);
					tmpt++;//学期+1
					if(tmpt>2){//学期大于2，则年+1，学期-2
						tmpy++;
						tmpt=tmpt-2;
					}
					year_term = tmpy+"_0"+tmpt;
					return year_term;
				}
			}
		}
		//二补
		else if("T".equals(examType)){
			year_term=getOpenyear(stu,cs,er.getCourse());
			if("error".equals(year_term)){
				throw new WebException("替换成绩："+er.getCourse().getCourseName()+"，无法查询到开课记录");
			}else{
				int currentTerm=getCurrentTerm(stu,year_term);
				if(currentTerm<=4){//1-4学期的课程，返回第5学期
					int year = stu.getGrade().getYearInfo().getFirstYear().intValue()+2;
					year_term=year+"_01";
					return year_term;
				}else{//返回第6学期
					int year = stu.getGrade().getYearInfo().getFirstYear().intValue()+2;
					year_term=year+"_02";
					return year_term;
				}
			}
		}
		//结补
		else if("Q".equals(examType)){
			year_term=getOpenyear(stu,cs,er.getCourse());
			if("error".equals(year_term)){
				throw new WebException("替换成绩："+er.getCourse().getCourseName()+"，无法查询到开课记录");
			}else{				
				int year=stu.getGrade().getYearInfo().getFirstYear().intValue()+Integer.parseInt(stu.getTeachingPlan().getEduYear())-1;
				year_term=year+"_02";
				return year_term;
			}
		}
		return "";
	}
	/**
	 * 根据学生的年级和教学计划的学制，计算出最大学期
	 * @param stu
	 * @param year_term
	 * @return
	 */
	private boolean isLastTerm(StudentInfo stu,String year_term){
		boolean flag=false;
		int maxterm = getMaxTerm(stu);
		int currentterm=getCurrentTerm(stu,year_term);
		if(currentterm==maxterm){
			flag=true;
		}
		return flag;
	}
	/**
	 * 获取最大学期
	 * @param stu
	 * @return
	 */
	private int getMaxTerm(StudentInfo stu){
		int eduyear=Integer.parseInt(stu.getTeachingPlan().getEduYear());
		int maxterm = eduyear*2;
		return maxterm;
	}
	/**
	 * 根据学生的年级及开课学期，返回int 类型的学期
	 * @param stu
	 * @param year_term
	 * @return
	 */
	private int getCurrentTerm(StudentInfo stu,String year_term){
		int year=stu.getGrade().getYearInfo().getFirstYear().intValue();
		String tmpy=year_term.substring(0,4);
		String tmpt=year_term.substring(6,7);
		int currentterm=(Integer.parseInt(tmpy)-year)*2+Integer.parseInt(tmpt);
		return currentterm;
	}
	/**
	 * 根据学生的教学计划以及成绩的课程ID，查找教学计划课程的开课学期
	 * @param stu
	 * @param er
	 * @return
	 */
	private String getOpenyear(StudentInfo stu,TeachingPlanCourseStatus cs,Course course){
		List<TeachingPlanCourseStatus> courseStatus = new ArrayList<TeachingPlanCourseStatus>();
		if(cs==null){
			String hql = " from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingGuidePlan.resourceid =? and teachingPlanCourse.course.resourceid =? "
					+ " and schoolIds=? and openStatus='Y' and isOpen=?";			
			courseStatus = teachingPlanCourseStatusService.findByHql(hql,stu.getTeachingGuidePlan().getResourceid(),course.getResourceid(),stu.getBranchSchool().getResourceid(),"Y");
			if(courseStatus!=null&&courseStatus.size()>0){
				cs = courseStatus.get(0);
				String open_year_term = cs.getTerm();
				return open_year_term;
			}else{
				return "error";
			}
		}else{
			String open_year_term = cs.getTerm();
			return open_year_term;
		}
		
	}
	private TeachingPlanCourse getPlancourse(Course course,List<TeachingPlanCourse> plancourses){
		TeachingPlanCourse plancourse=null;
		for(TeachingPlanCourse p:plancourses){
			if(course.equals(p.getCourse())){
				plancourse=p;
				break;
			}
		}
		return plancourse;
	}
	/**
	 * 废弃
	 * @param stu
	 * @param sci
	 * @return
	 * @throws WebException
	 */
	private String updateResumeSchoolResult(StudentInfo stu,StuChangeInfo sci) throws WebException{
		String message = "";
		List<ExamResults> resultreplace                    = new ArrayList<ExamResults>();
		List<StudentMakeupList> makeuplist = new ArrayList<StudentMakeupList>();
		//查找一个学生所有的正考成绩
		List<ExamResults> resultreplacetmp = new ArrayList<ExamResults>();
		resultreplacetmp   = studentExamResultsService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo", stu),Restrictions.eq("isMakeupExam", "N"));
		for(ExamResults er :resultreplacetmp){
			
			//成绩所在的年度及学期
			String result_year = String.valueOf(er.getExamInfo().getExamSub().getYearInfo().getFirstYear());
			String result_term = er.getExamInfo().getExamSub().getTerm();
			//查找开课的年度和学期
			String hql = " from "+TeachingPlanCourseStatus.class.getSimpleName()+" where isDeleted=0 and teachingGuidePlan.resourceid =? and teachingPlanCourse.course.resourceid =? "
					+ " and schoolIds=? and openStatus=? ";
			List<TeachingPlanCourseStatus> courseStatus = new ArrayList<TeachingPlanCourseStatus>();
			courseStatus = teachingPlanCourseStatusService.findByHql(hql,stu.getTeachingGuidePlan().getResourceid(),er.getCourse().getResourceid(),stu.getBranchSchool().getResourceid(),"Y");
			if(courseStatus!=null&&courseStatus.size()==1){
				String open_year = courseStatus.get(0).getTerm().substring(0,4);
				String open_term = courseStatus.get(0).getTerm().substring(6,7);
				//计算开课的年度及学期与成线年度及学期的差值
				int dValue_year=Integer.parseInt(open_year)-Integer.parseInt(result_year);
				int dValue_term=Integer.parseInt(open_term)-Integer.parseInt(result_term)==0?0:1;
				//根据年度及学期、考试类型修改成绩的考试批次
				List<ExamResults> resultlist = new ArrayList<ExamResults>();
				resultlist = studentExamResultsService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo", stu),
						Restrictions.eq("course",courseStatus.get(0).getTeachingPlanCourse().getCourse()));
				for(ExamResults e:resultlist){
					int firstyear = 0;
					int term = 0;
					firstyear=(int) (e.getExamInfo().getExamSub().getYearInfo().getFirstYear()+dValue_year);
					int tmpterm = Integer.parseInt(e.getExamInfo().getExamSub().getTerm());
					term=tmpterm==1?tmpterm+dValue_term:tmpterm-dValue_term;
					String hql1 = " from "+ExamInfo.class.getSimpleName() +" where isDeleted = 0 and examSub.yearInfo.firstYear=? and examSub.term=? and examSub.examType=? and course.resourceid=? ";
					List<ExamInfo> examinfolist = new ArrayList<ExamInfo>();
					examinfolist =examInfoService.findByHql(hql1, (long)firstyear,String.valueOf(term),e.getIsMakeupExam(),e.getCourse().getResourceid());
					if(examinfolist!=null && examinfolist.size()==1){//有且只有一条记录时，直接替换批次
						e.setExamsubId(examinfolist.get(0).getExamSub().getResourceid());
						e.setExamInfo(examinfolist.get(0));
						e.setMajorCourseId(courseStatus.get(0).getTeachingPlanCourse().getResourceid());//修改PlancourseId
						resultreplace.add(e);
					}else if(examinfolist==null||examinfolist.size()==0){//为空时，先添加examInfo的记录,再进行替换
						String hql2 = " from "+ ExamSub.class.getSimpleName()+" where isDeleted = 0 and yearInfo.firstYear=? and term=? and examType=? ";
						List<ExamSub> esList= new ArrayList<ExamSub>();
						esList= examSubService.findByHql(hql2,(long)firstyear,String.valueOf(term),e.getIsMakeupExam());
						if(esList==null||esList.size()==0){//如果没有考试信息，直接使用异常进行中断
							message = "请添加"+firstyear+"第"+term+"学期"+JstlCustomFunction.dictionaryCode2Value("ExamResult", e.getIsMakeupExam())+"学期的考试批次";
							throw new WebException("请添加"+firstyear+"第"+term+"学期"+JstlCustomFunction.dictionaryCode2Value("ExamResult", e.getIsMakeupExam())+"学期的考试批次");
						}else{//添加一条考试examInfo的记录
							ExamInfo ei = new ExamInfo();
							ei.setExamSub(esList.get(0));
							ei.setCourse(e.getCourse());
							ei.setIsOutplanCourse("0");
							ei.setExamCourseType(1);//面授课程
							ei.setCourseScoreType("11");
							ei.setFacestudyScorePer(esList.get(0).getFacestudyScorePer());
							ei.setFacestudyScorePer2(esList.get(0).getFacestudyScorePer2());
							ei.setFacestudyScorePer3(esList.get(0).getFacestudyScorePer3());
							ei.setStudyScorePer(esList.get(0).getWrittenScorePer());
							examInfoService.saveOrUpdate(ei);
							e.setExamsubId(esList.get(0).getResourceid());
							e.setExamInfo(ei);
							System.out.println(courseStatus.get(0).getTeachingPlanCourse().getResourceid());
							System.out.println(e.getMajorCourseId());
							e.setMajorCourseId(courseStatus.get(0).getTeachingPlanCourse().getResourceid());//修改PlancourseId							
							resultreplace.add(e);
						}
					}					
				}
				//根据考试成绩、学生id、examsub 修改补考名单的nextexamsubid
				List<StudentMakeupList> makeup = new ArrayList<StudentMakeupList>();
				makeup = studentMakeupListService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo", stu),
						Restrictions.eq("course",courseStatus.get(0).getTeachingPlanCourse().getCourse()));
				for(StudentMakeupList sm:makeup){					
					int firstyear = 0;
					int term = 0;
					ExamSub tmp = examSubService.get(sm.getNextExamSubId());
					firstyear = (int) (tmp.getYearInfo().getFirstYear()+dValue_year);
					int tmpterm = Integer.parseInt( tmp.getTerm());
					term =tmpterm==1?tmpterm+dValue_term:tmpterm-dValue_term;
					String hql2 = " from "+ ExamSub.class.getSimpleName()+" where isDeleted = 0 and yearInfo.firstYear=? and term=? and examType=? ";
					List<ExamSub> examList = new ArrayList<ExamSub>();
					//年度、学期、考试类型
					examList = examSubService.findByHql(hql2, (long)firstyear,String.valueOf(term),examSubService.get(sm.getNextExamSubId()).getExamType());
					if(examList!=null&&examList.size()==1){
						sm.setNextExamSubId(examList.get(0).getResourceid());
						sm.setTeachingPlanCourse(courseStatus.get(0).getTeachingPlanCourse());					
						makeuplist.add(sm);
					}
				}
			}
			
		}
	//	examInfoService.batchSaveOrUpdate(examInfoList);
		studentExamResultsService.batchSaveOrUpdate(resultreplace);
		studentMakeupListService.batchSaveOrUpdate(makeuplist);
		return message;
	}
	
	public String updatePlancourse(StudentInfo stu,TeachingPlanCourseStatus cs,String result_year,String result_term){
		String message="";
		List<ExamResults> resultreplace                    = new ArrayList<ExamResults>();
		List<StudentMakeupList> makeuplist = new ArrayList<StudentMakeupList>();		
		String open_year = cs.getTerm().substring(0,4);
		String open_term = cs.getTerm().substring(6,7);
		//计算开课的年度及学期与成绩年度及学期的差值
		int dValue_year=Integer.parseInt(open_year)-Integer.parseInt(result_year);
		int dValue_term=Integer.parseInt(open_term)-Integer.parseInt(result_term)==0?0:1;
		//根据年度及学期、考试类型修改成绩的考试批次
		List<ExamResults> resultlist = new ArrayList<ExamResults>();
		resultlist = studentExamResultsService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("studentInfo", stu),
				Restrictions.eq("course",cs.getTeachingPlanCourse().getCourse()));
		for(ExamResults e:resultlist){
			int firstyear = 0;
			int term = 0;
			firstyear=(int) (e.getExamInfo().getExamSub().getYearInfo().getFirstYear()+dValue_year);
			int tmpterm = Integer.parseInt(e.getExamInfo().getExamSub().getTerm());
			term=tmpterm==1?tmpterm+dValue_term:tmpterm-dValue_term;
			String hql1 = " from "+ExamInfo.class.getSimpleName() +" where isDeleted = 0 and examSub.yearInfo.firstYear=? and examSub.term=? and examSub.examType=? and course.resourceid=? ";
			List<ExamInfo> examinfolist = new ArrayList<ExamInfo>();
			examinfolist =examInfoService.findByHql(hql1, (long)firstyear,String.valueOf(term),e.getIsMakeupExam(),e.getCourse().getResourceid());
			if(examinfolist!=null && examinfolist.size()==1){//有且只有一条记录时，直接替换批次
				e.setExamsubId(examinfolist.get(0).getExamSub().getResourceid());
				e.setExamInfo(examinfolist.get(0));
				e.setMajorCourseId(cs.getTeachingPlanCourse().getResourceid());//修改PlancourseId
				resultreplace.add(e);
			}else if(examinfolist==null||examinfolist.size()==0){//为空时，先添加examInfo的记录,再进行替换
				String hql2 = " from "+ ExamSub.class.getSimpleName()+" where isDeleted = 0 and yearInfo.firstYear=? and term=? and examType=? ";
				List<ExamSub> esList= new ArrayList<ExamSub>();
				esList= examSubService.findByHql(hql2,(long)firstyear,String.valueOf(term),e.getIsMakeupExam());
				if(esList==null||esList.size()==0){//如果没有考试信息，直接使用异常进行中断
					message = "请添加"+firstyear+"第"+term+"学期"+JstlCustomFunction.dictionaryCode2Value("ExamResult", e.getIsMakeupExam())+"学期的考试批次";
					throw new WebException("请添加"+firstyear+"第"+term+"学期"+JstlCustomFunction.dictionaryCode2Value("ExamResult", e.getIsMakeupExam())+"学期的考试批次");
				}else{//添加一条考试examInfo的记录
					ExamInfo ei = new ExamInfo();
					ei.setExamSub(esList.get(0));
					ei.setCourse(e.getCourse());
					ei.setIsOutplanCourse("0");
					ei.setExamCourseType(1);//面授课程
					ei.setCourseScoreType("11");
					ei.setFacestudyScorePer(esList.get(0).getFacestudyScorePer());
					ei.setFacestudyScorePer2(esList.get(0).getFacestudyScorePer2());
					ei.setFacestudyScorePer3(esList.get(0).getFacestudyScorePer3());
					ei.setStudyScorePer(esList.get(0).getWrittenScorePer());
					examInfoService.saveOrUpdate(ei);
					e.setExamsubId(esList.get(0).getResourceid());
					e.setExamInfo(ei);
					System.out.println(cs.getTeachingPlanCourse().getResourceid());
					System.out.println(e.getMajorCourseId());
					e.setMajorCourseId(cs.getTeachingPlanCourse().getResourceid());//修改PlancourseId							
					resultreplace.add(e);
				}
			}					
		}
		//根据考试成绩、学生id、examsub 修改补考名单的nextexamsubid
		List<StudentMakeupList> makeup = new ArrayList<StudentMakeupList>();
		makeup = studentMakeupListService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo", stu),
				Restrictions.eq("course",cs.getTeachingPlanCourse().getCourse()));
		for(StudentMakeupList sm:makeup){					
			int firstyear = 0;
			int term = 0;
			ExamSub tmp = examSubService.get(sm.getNextExamSubId());
			firstyear = (int) (tmp.getYearInfo().getFirstYear()+dValue_year);
			int tmpterm = Integer.parseInt( tmp.getTerm());
			term =tmpterm==1?tmpterm+dValue_term:tmpterm-dValue_term;
			String hql2 = " from "+ ExamSub.class.getSimpleName()+" where isDeleted = 0 and yearInfo.firstYear=? and term=? and examType=? ";
			List<ExamSub> examList = new ArrayList<ExamSub>();
			//年度、学期、考试类型
			examList = examSubService.findByHql(hql2, (long)firstyear,String.valueOf(term),examSubService.get(sm.getNextExamSubId()).getExamType());
			if(examList!=null&&examList.size()==1){
				sm.setNextExamSubId(examList.get(0).getResourceid());
				sm.setTeachingPlanCourse(cs.getTeachingPlanCourse());					
				makeuplist.add(sm);
			}
		}
	
		return message;
	}	
	//更新学生学习计划
	private String updateStudentLearnPlan(StudentInfo stu) throws ServiceException{
		//修改学生的学习计划
		List<StudentLearnPlan> studentLearnPlans        = studentLearnPlanService.findByHql(" from "+StudentLearnPlan.class.getSimpleName()+" where studentInfo.resourceid = ? and studentInfo.isDeleted=0 and isDeleted=0 ", stu.getResourceid());
		TeachingPlan teachingPlan = stu.getTeachingPlan();
		Set<TeachingPlanCourse> distTeachingPlanCourse = null;
		String message = "";
		if (teachingPlan != null) {
			distTeachingPlanCourse = teachingPlan.getTeachingPlanCourses();
		} else {
			message = "<br/>" + Tools.colorStr(stu.getStudentName(), "red") + "没有关联教学计划！";
		}
		
		List<CourseOrder> orderList       				= new ArrayList<CourseOrder>();
		List<ExamResults> resultList                    = new ArrayList<ExamResults>();
		List<ExamResults> unExamList                    = new ArrayList<ExamResults>();
		List<StudentLearnPlan> p_studentLearnPlans      = new ArrayList<StudentLearnPlan>();
		List<StudentLearnPlan> remove_studentLearnPlans = new ArrayList<StudentLearnPlan>();
		//20170417  如果已经有了网上学习的成绩，同样需要处理  
		List<UsualResults> urs = new ArrayList<UsualResults>();
		StudentLearnPlan p_stuLearnPlan 				= null;
		
		if(null != studentLearnPlans && !studentLearnPlans.isEmpty() && !distTeachingPlanCourse.isEmpty()){			
	    	for(StudentLearnPlan studentLearnPlan : studentLearnPlans){	
	    		if(studentLearnPlan.getExamResults() == null || studentLearnPlan.getExamResults().getIsDeleted()==1){// 学习计划中没有成绩直接删除（注：后面会创建新的）
	    			
	    			remove_studentLearnPlans.add(studentLearnPlan);
	    		}else{// 有成绩要修改课程，年度和学期
	    			p_stuLearnPlan = studentLearnPlan;
		    		if(null != studentLearnPlan.getTeachingPlanCourse()){
			    		Course origCourse = studentLearnPlan.getTeachingPlanCourse().getCourse();
			    		//将原教学计划课程与现教学计划课程匹配，如果课程一致，则修改为现在教学计划的课程
			    		for(TeachingPlanCourse teachingPlanCourse : distTeachingPlanCourse){
			    			Course distCourse = teachingPlanCourse.getCourse();
			    			
			    			if(origCourse.getResourceid().equals(distCourse.getResourceid())){
			    				//课程匹配
			    				//TODO 学分问题，两教学计划的课程学分数不一致
			    				p_stuLearnPlan.setTeachingPlanCourse(teachingPlanCourse);
			    				p_stuLearnPlan.setPlanoutCourse(null);
			    				
			    				// 获取开课学期
			    				try {
									List<Map<String, Object>> year_termList = teachingPlanCourseStatusService.findYearAndTerm(teachingPlanCourse.getResourceid(), stu.getGrade().getResourceid(),
											stu.getBranchSchool().getResourceid(),stu.getTeachingPlan().getResourceid());
									if(ExCollectionUtils.isNotEmpty(year_termList)){
										Map<String, Object> year_term = year_termList.get(0);
										String yearInfoId = (String)year_term.get("yearInfoId");
										String term = (String)year_term.get("term");
										YearInfo _yearInfo = yearInfoService.get(yearInfoId);
										p_stuLearnPlan.setYearInfo(_yearInfo);
										p_stuLearnPlan.setTerm(term);
									}
								} catch (Exception e) {
									throw new ServiceException("学籍异动时，获取开课学期出错！");
								}
					    		p_studentLearnPlans.add(p_stuLearnPlan);
			    				logger.debug("匹配到的课程："+origCourse.getCourseName());

			    				//remove_studentLearnPlans.add(studentLearnPlan);
			    				
			    				break;
			    			}
			    		}	
		    		}
	    		}
	    		
	    		if(studentLearnPlan.getUsualResults()!=null){//学生已有网上学习成绩 （是否提交过随堂练习、随堂提问是否得分、课后作业是否得分）
    				urs.add(studentLearnPlan.getUsualResults());
    			}
	    	}
	    	String courseOrderHql			= " from "+CourseOrder.class.getSimpleName()+" o where o.isDeleted=? and o.studentInfo.resourceid=? and o.courseOrderStat.course.resourceid=? ";
	    	//移走匹配好的课程
	    	//studentLearnPlans.removeAll(remove_studentLearnPlans);//这个应该是匹配好的,p_studentLearnPlans
	    	List<StudentMakeupList> makeupList = new ArrayList<StudentMakeupList>();
	    	for(StudentLearnPlan studentLearnPlan :p_studentLearnPlans){//计划内&有成绩，如果是统考课程，替换成绩\补考名单两张表的plancourseid
	    		TeachingPlanCourse tpc = studentLearnPlan.getTeachingPlanCourse();
	    		if(tpc.getExamClassType().equals("3")){//统考课程
	    			//成绩表
	    			Course course = tpc.getCourse();
	    			ExamResults er = studentLearnPlan.getExamResults();
	    			if(null!=er){
	    				if(er.getCourse().getResourceid().equals(course.getResourceid())){
	    					er.setMajorCourseId(tpc.getResourceid());
	    					unExamList.add(er);
	    				}
	    				
	    			}
	    			//补考名单
	    			List<StudentMakeupList> makeups = new ArrayList<StudentMakeupList>();
	    			String hql = "from "+StudentMakeupList.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and course.resourceid=? ";
	    			makeups=studentMakeupListService.findByHql(hql, studentLearnPlan.getStudentInfo().getResourceid(),course.getResourceid());
	    			if(makeups.size()>0){
	    				for(StudentMakeupList m:makeups){
	    					m.setTeachingPlanCourse(tpc);
	    					makeupList.add(m);
	    				}
	    			}
	    		}
	    	}
	    	studentLearnPlans.removeAll(p_studentLearnPlans);
	    	studentLearnPlans.removeAll(remove_studentLearnPlans);
	    	for(StudentLearnPlan studentLearnPlan : studentLearnPlans){//计划外&有成绩
	    		if(null != studentLearnPlan.getTeachingPlanCourse()){
	    			Course origCourse = studentLearnPlan.getTeachingPlanCourse().getCourse();
		    		ExamResults examResults = studentExamResultsService.getStudentPlanCourseMaxExamResults(stu, origCourse.getResourceid(), true);
		    		if(null != examResults){//如果该课程成绩不为空，将转换为计划外课程
		    			//gchw:此处感觉不宜将教学计划课程清空，如若清空，计划外课程永远不能变回计划内课程
		    			//studentLearnPlan.setTeachingPlanCourse(null);
	    				studentLearnPlan.setPlanoutCourse(origCourse);
		    			studentLearnPlan.setExamResults(examResults);    					
    					logger.debug("异动为计划外的课程："+origCourse.getCourseName());
    					
    					p_studentLearnPlans.add(studentLearnPlan);
    				}else{//否则，标示为-1
    					
    					if(null!=studentLearnPlan.getExamResults()) {
							resultList.add(studentExamResultsService.get(studentLearnPlan.getExamResults().getResourceid()));
						}
    					String courseId     = null==studentLearnPlan.getTeachingPlanCourse()?studentLearnPlan.getPlanoutCourse().getResourceid():studentLearnPlan.getTeachingPlanCourse().getCourse().getResourceid();
    					String yearId       = "";
    					String term         = "";
    					String hql          = "";
    					if (studentLearnPlan.getStatus()==1) {
    						yearId          = studentLearnPlan.getYearInfo().getResourceid();
							term            = studentLearnPlan.getTerm();
							hql  			= courseOrderHql +" and o.orderCourseYear.resourceid=? and o.orderCourseTerm=? ";
						}else if(studentLearnPlan.getStatus()==2) {
							yearId          = null==studentLearnPlan.getOrderExamYear()?"":studentLearnPlan.getOrderExamYear().getResourceid();
							term            = null==studentLearnPlan.getOrderExamTerm()?"":studentLearnPlan.getOrderExamTerm();
							if(ExStringUtils.isEmpty(yearId)){
								yearId          = studentLearnPlan.getYearInfo().getResourceid();
							}
							if(ExStringUtils.isEmpty(term)){
								term            = studentLearnPlan.getTerm();
							}
							hql  			= courseOrderHql +" and o.orderCourseYear.resourceid=? and o.orderCourseTerm=? ";
						}
    					if (ExStringUtils.isNotEmpty(courseId)&&ExStringUtils.isNotEmpty(yearId)&&ExStringUtils.isNotEmpty(term)) {
    						orderList.addAll(courseOrderService.findByHql(hql,0,stu.getResourceid(),courseId,yearId,term));
						}
    					//studentLearnPlan.setIsDeleted(-1);//这边就不应该设-1了,直接进renmove就好了
    					remove_studentLearnPlans.add(studentLearnPlan);
    				}
		    		//p_studentLearnPlans.add(studentLearnPlan);//这个也不应该全都加,应该进入计划外的加
	    		}
	    	}
	    	for (ExamResults rs : resultList) {
	    		rs.setIsDeleted(1);
			}
	    	for (CourseOrder co : orderList) {
				co.setIsDeleted(1);
			}
	    	studentExamResultsService.batchSaveOrUpdate(resultList);
	    	courseOrderService.batchSaveOrUpdate(orderList);
	    	studentLearnPlanService.batchDelete(remove_studentLearnPlans);
	    	studentLearnPlanService.batchSaveOrUpdate(p_studentLearnPlans);
	    	if(unExamList.size()>0){
	    		studentExamResultsService.batchSaveOrUpdate(unExamList);
	    	}
	    	if(makeupList.size()>0){
	    		studentMakeupListService.batchSaveOrUpdate(makeupList);
	    	}
	    }
		try {
			// 给还没有学习计划的课程创建学习计划,这里没问题
			List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
			studentInfoList.add(stu);
//			String spids = "";
//			for (StudentLearnPlan learnPlan : p_studentLearnPlans) {
//				if(ExStringUtils.isEmpty(spids)){
//					spids = "'"+learnPlan.getTeachingPlanCourse().getResourceid()+"'";
//				}else {
//					spids += ","+"'"+learnPlan.getTeachingPlanCourse().getResourceid()+"'";
//				}
//				
//			}
//			if(spids!=""){
//				spids.substring(1, spids.length());
//			}
			studentLearnPlanService.generateStuPlan(studentInfoList,stu.getResourceid(),urs);	
//			studentLearnPlanService.generateStudyPlanByCondition(studentInfoList, stu.getResourceid(),stu.getGrade().getResourceid(),
//					stu.getBranchSchool().getResourceid(),stu.getTeachingPlan().getResourceid(),spids,urs);
		} catch (Exception e) {
			throw new ServiceException("学籍异动时，创建学习计划出错！");
		}
		return message;
	}




	/**
	 * 获取指定学生的三种异动类型最新日期异动记录(转中心、转教点、其它)
	 * @param stu
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, StuChangeInfo> findStudentChangeRecord(String stuId)throws ServiceException {
		
		List<StuChangeInfo> change_1    = new ArrayList<StuChangeInfo>();
		List<StuChangeInfo> change_2    = new ArrayList<StuChangeInfo>();
		List<StuChangeInfo> change_3    = new ArrayList<StuChangeInfo>();
		Map<String,StuChangeInfo> map   = new HashMap<String, StuChangeInfo>();
		
		List<StuChangeInfo> changeList  = findByHql(" from "+StuChangeInfo.class.getSimpleName()+" sc where sc.isDeleted=? and sc.finalAuditStatus=? and sc.studentInfo.resourceid = ? ",0,Constants.BOOLEAN_YES,stuId);

		
		for (StuChangeInfo stu:changeList ) {
			if ("23".equals(stu.getChangeType())) {
				change_1.add(stu);
			}
			if ("82".equals(stu.getChangeType())) {
				change_2.add(stu);
			}
		}
		if (!change_1.isEmpty()) {
			changeList.removeAll(change_1);
		}
		if (!change_2.isEmpty()) {
			changeList.removeAll(change_2);
		}
		if (!changeList.isEmpty()) {
			change_3 = changeList;
		}
		
		Collections.sort(change_1, new Comparator<StuChangeInfo>(){
			@Override
			public int compare(StuChangeInfo o1, StuChangeInfo o2) {
				if (o1.getAcceptDate()!=null&&o1.getAuditDate()!=null) {
					return o2.getAuditDate().compareTo(o1.getAuditDate());
				}else {
					return 0 ;
				}
			}
		} );
		Collections.sort(change_2, new Comparator<StuChangeInfo>(){
			@Override
			public int compare(StuChangeInfo o1, StuChangeInfo o2) {
				if (o1.getAcceptDate()!=null&&o1.getAuditDate()!=null) {
					return o2.getAuditDate().compareTo(o1.getAuditDate());
				}else {
					return 0 ;
				}
			}
		} );
		Collections.sort(change_3, new Comparator<StuChangeInfo>(){
			@Override
			public int compare(StuChangeInfo o1, StuChangeInfo o2) {
				if (o1.getAcceptDate()!=null&&o1.getAuditDate()!=null) {
					return o2.getAuditDate().compareTo(o1.getAuditDate());
				}else {
					return 0 ;
				}
			}
		} );
		
		if(!change_1.isEmpty()) {
			map.put("majorChange", change_1.get(0));
		}
		if(!change_2.isEmpty()) {
			map.put("brSchoolChange", change_2.get(0));
		}
		if(!change_3.isEmpty()) {
			map.put("otherChange", change_3.get(0));
		}
		
		return map;
	}
	@Override
	public List<Map<String,Object>> findStuChangeInfoMapList(Map<String, Object> condition) throws ServiceException {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select * from EDU_ROLL_STUCHANGEINFO sc,edu_roll_studentinfo stu where " );
		sb.append(" sc.studentid = stu.resourceid and sc.isDeleted = 0 ");
		if(condition.containsKey("branchSchool")) {
			sb.append(" and (stu.branchschoolid =:branchSchool or sc.changebrschoolid =:branchSchool   or sc.changebeforebrschoolid = :branchSchool ) ");//学习中心
		}
		if(condition.containsKey("major")) {
			sb.append(" and stu.majorid = :major ");//专业
		}
		if(condition.containsKey("classic")) {
			sb.append("  and stu.classicid = :classic ");	//层次
		}
		if(condition.containsKey("stuStatus")) {
			sb.append("  and stu.studentstatus =:stuStatus ");
		}
		if(condition.containsKey("stuName"))	{//学生姓名
			sb.append(" and stu.studentname like :name") ;
			condition.put("name", "%"+condition.get("stuName").toString()+"%");
		}
		
		if(condition.containsKey("stuChange")) {
			sb.append(" and sc.changetype = :stuChange");
		}
		if(condition.containsKey("gradeid")) {
			sb.append("  and stu.gradeid = :gradeid ");
		}
		
		if(condition.containsKey("learningStyle")) {
			sb.append(" and  ( sc.changebeforelearingstyle =:learningStyle or sc.changelearningstyle =:learningStyle ) ");
		}
		if(condition.containsKey("stuNum")) {
			sb.append(" and stu.studyno = :stuNum ");             //专业
		}
		if(condition.containsKey("applicationDateb")) {
			sb.append(" and  sc.applicationdate >=:applicationDateb ");             //准考证号
		}
		if(condition.containsKey("applicationDatee")) {
			sb.append(" and  sc.applicationdate <=:applicationDateb ");                 //证件号
		}
		if(condition.containsKey("auditDateb")) {
			sb.append(" and sc.auditdate >= :auditDateb");                          //报名方式
		}
		if(condition.containsKey("auditDatee")) {
			sb.append(" and sc.auditdate <=:auditDatee");
		}
		if(condition.containsKey("accountStatus")) {
			sb.append(" and stu.accoutstatus =:accountStatus"); //办学模式
		}
		if(condition.containsKey("finalAuditStatus")) {
			sb.append(" and sc.finalAuditStatus =:finalAuditStatus"); //审核状态
		}
		
		
		sb.append(" order by sc.applicationdate desc ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}
	/**
	 * 获取学生的所有打印信息
	 * @param studentInfo
	 * @param condition
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StuChangeInfoVo> stuChangeInfoPrint(StuChangeInfo stuChangeInfo) {		
		List<StuChangeInfoVo> returnList   = new LinkedList<StuChangeInfoVo>();
		List <StuChangeInfoVo> resultsList = new ArrayList<StuChangeInfoVo>();		
		return returnList;
	}
	
	/**
	 * 根据系统用户ID获取所有对应的学籍
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StuChangeInfo> findStuListByUserId(String name) throws ServiceException ,Exception{
		Map<String,Object> condition = new HashMap<String,Object>();
		//String sql = "SELECT c. * FROM edu_roll_studentinfo t,edu_roll_stuchangeinfo c  WHERE t.resourceid=c.studentid and  t.studyno=:userId";
		String sql = "select stuchange.* from edu_roll_stuchangeinfo stuchange left join EDU_ROLL_STUDENTINFO info on stuchange.studentid=info.resourceid  left join EDU_BASE_STUDENT stu on info.studentbaseinfoid=stu.resourceid where stu.name=:name";
		String hql = " from "+StuChangeInfo.class.getSimpleName()+" where 1=1  and studentInfo.studentName like :stuName ";
		condition.put("name", name);
		List<StuChangeInfo> scList = null;
		if(ExStringUtils.isNotEmpty(name)){
			scList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql, StuChangeInfo.class, condition);
		}
		return scList;
	}
	
	/**
	 * 根据ID获取所有对应的学院
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<OrgUnit> findStuListByUnit(String id) throws ServiceException ,Exception{
		Map<String,Object> condition = new HashMap<String,Object>();
		String sql = "select sysunit.UnitShortName from hnjk_sys_unit sysunit where sysunit.resourceid=:id";
		condition.put("id", id);
		List<OrgUnit> unList = null;
		if(ExStringUtils.isNotEmpty(id)){
			unList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql, OrgUnit.class, condition);
		}
		return unList;
	}
	
	
	/**
	 * 根据查询条件获取所有对应的异动学籍信息
	 * @param 
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public List<StuChangeInfo> findStuChangeInfoByCondition(Map<String, Object> condition) throws ServiceException {
		StringBuffer sb = new StringBuffer("");
		
		String hql = " from "+StuChangeInfo.class.getSimpleName()+" where 1=1 ";
		//hql += " and isDeleted = :isDeleted ";
		//values.put("isDeleted", 0);
		
		if(condition.containsKey("stuName")){
			hql += " and studentInfo.studentName like :stuName ";
		}
		//查出转入本中心的及本中心的异动记录     20131215  添加转出异动信息
		if(condition.containsKey("branchSchool")){
			hql += " and (studentInfo.branchSchool.resourceid = :branchSchool or changeBrschool.resourceid = :branchSchool  or changeBeforeBrSchool =:branchSchool )";
			//hql += " and studentInfo.branchSchool.resourceid = :branchSchool";
		}
		if(condition.containsKey("major")){
			hql += " and studentInfo.major.resourceid = :major ";
		}
		if(condition.containsKey("classic")){
			hql += " and studentInfo.classic.resourceid = :classic ";
		}
		if(condition.containsKey("stuStatus")){
			hql += " and studentInfo.studentStatus = :stuStatus ";
		}
		if(condition.containsKey("stuChange")){
			hql += " and changeType = :stuChange ";
		}
		//学籍异动>>学生登录查看学籍异动信息
		if(condition.containsKey("acquiesceStudentid")){
			hql+=" and studentInfo.resourceid = :acquiesceStudentid";
		}
		if(condition.containsKey("gradeid")){
			hql += " and studentInfo.grade.resourceid = :gradeid";
		}
		if(condition.containsKey("learningStyle")){
			hql += " and (changeLearningStyle = :learningStyle or changeBeforeLearingStyle =:learningStyle ) ";
		}
		if(condition.containsKey("stuNum")){
			hql += " and studentInfo.studyNo = :stuNum";
		}
		if(condition.containsKey("finalAuditStatus")){
			hql += " and (finalAuditStatus = :finalAuditStatus )  ";
		}
		if(condition.containsKey("applicationDate")){
			hql += " and applicationDate >= :applicationDate";
		}
		if(condition.containsKey("auditDate")){
			hql += " and auditDate >= :auditDate";
		}
		
		//新增按范围查询审核和申请时间
		if (condition.containsKey("applicationDateb")||condition.containsKey("applicationDatee")) {
			if (condition.containsKey("applicationDateb")) {
				hql += " and applicationDate >= :applicationDateb";
			}
			if (condition.containsKey("applicationDatee")) {
				hql += " and applicationDate <= :applicationDatee";
			}
		}
		if (condition.containsKey("auditDateb")||condition.containsKey("auditDatee")) {
			if (condition.containsKey("auditDateb")) {
				hql += " and auditDate >= :auditDateb";
			}
			if (condition.containsKey("auditDatee")) {
				hql += " and auditDate <= :auditDatee";
			}
		}
		//增加对帐号状态的判断 学籍办设定了正常注册 正常未注册的学籍实际就是在学的学籍状态和帐号状态的组合
				if(condition.containsKey("accountStatus")){
						hql += " and studentInfo.accountStatus = :accountStatus ";
					}
		if(condition.containsKey("changeProperty")){
			String changeProperty = condition.get("changeProperty").toString();
			if("1".equals(changeProperty)){
				hql += " and studentInfo.branchSchool.resourceid <> changeBeforeBrSchool.resourceid  ";
				hql += " and finalAuditStatus ='Y'  ";
			}else if("2".equals(changeProperty)){
				hql += " and ( (changeBeforeMajorName is not null and studentInfo.major.majorName <> changeBeforeMajorName)  or (changeBeforeMajorName is null and studentInfo.major.resourceid <> changeBeforeTeachingGuidePlan.teachingPlan.major.resourceid) )";
				hql += " and finalAuditStatus ='Y'  ";
			}else if("3".equals(changeProperty)){
				hql += " and studentInfo.teachingType <> changeBeforeLearingStyle  ";
				hql += " and finalAuditStatus ='Y'  ";
			}
			
		}
		List<StuChangeInfo> returnList = new ArrayList<StuChangeInfo>();
		try {
			//returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), condition);
			returnList = (List<StuChangeInfo>) exGeneralHibernateDao.findByHql(hql.toString(),condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StuChangeInfo> findByResourceIds(String stuChangeIds)
			throws ServiceException {
		String hql = "from "+StuChangeInfo.class.getSimpleName()+ " sci where sci.isDeleted=? and "
				+ " sci.resourceid in ('" +stuChangeIds+"') ";
		
		return (List<StuChangeInfo>)exGeneralHibernateDao.findByHql(hql, 0);
	}
	
	/**
	 * 根据异动类型获取最新的异动信息
	 * @param changeTypes 如果有多个则以“,”隔开
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public StuChangeInfo findLatestByChangeType(String changeTypes) throws ServiceException {
		StringBuffer hql = new StringBuffer("");
		Map<String, Object> param = new HashMap<String, Object>();
		StuChangeInfo stuChangeInfo = null;
		hql.append("from "+StuChangeInfo.class.getSimpleName()+" sio where sio.isDeleted=0 ");
		if(changeTypes.split(",").length>1){
			hql.append("and sio.changeType in ("+changeTypes+") ");
		}else if (changeTypes.split(",").length==1){
			hql.append("and sio.changeType =:changeTypes ");
			param.put("changeTypes", changeTypes) ;
		}
		hql.append(" order by sio.auditDate desc ");
		List<StuChangeInfo> stuChangeInfoList = findByHql(hql.toString(), param);
		if(ExCollectionUtils.isNotEmpty(stuChangeInfoList)){
			stuChangeInfo = stuChangeInfoList.get(0);
		}
		return stuChangeInfo;
	}
	/**
	 *根据学籍异动表 studentChangeInfo的resourceid删除学生的退学记录，如果并获取该生退学前的学籍状态，恢复学生退学前的状态，当前已经的有正常注册  11   休学  12
	 *@param id
	 */
	@Override
	public void DeleteDropout(String id) throws ServiceException {
		
		StuChangeInfo tmp = this.get(id);
		StudentInfo stu = studentInfoService.get(tmp.getStudentInfo().getResourceid());		
		//if(tmp.getChangeBeforeStudentStatus())
		stu.setStudentStatus(tmp.getChangeBeforeStudentStatus());		
		
		if("11".equals(tmp.getChangeBeforeStudentStatus())){//
			User user = userService.get(stu.getSysUser().getResourceid());
			user.setEnabled(true);
			stu.setAccountStatus(Constants.BOOLEAN_TRUE);
			userService.update(user);
		}
		studentInfoService.update(stu);	
		this.delete(id);
	}
	
	public String getIsopenCourse(String pcid,String uid){//2016-10-10
		String sql="select cts.term from edu_teach_plan p left join edu_teach_plancourse pc on pc.planid=p.resourceid left join EDU_TEACH_COURSESTATUS cts on cts.plancourseid=pc.resourceid and cts.term is not null and cts.isdeleted=0 and cts.isopen='Y'  where p.isdeleted=0 and pc.isdeleted=0 and pc.resourceid='"+pcid+"' and cts.schoolIds='"+uid+"'";
		try {
		 List<Map<String,Object>> l=	baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, null);
		 if(l.size()>0){
			 return l.get(0).get("term").toString();
		 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public ExamInfo getExaminfo(Map<String,Object> param){//2016-10-10
		String sql=" select ei.resourceid from edu_teach_examsub eb left join edu_teach_examinfo ei on ei.examsubid=eb.resourceid where eb.isdeleted=0 and ei.ismachineexam='N' and ei.isdeleted=0 and eb.yearid=:yearid and eb.term=:term and ei.courseid=:courseid and ei.brschoolid=:unid";
		try {
			 List<Map<String,Object>> l=	baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, param);
			 
			 if(l.size()>0){
				 return examInfoService.get(l.get(0).get("resourceid").toString());
			 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
	
	//模拟更新学生学习计划,用于复学成绩的标记删除
		@Override
		public List<StudentLearnPlan> updateStudentLearnPlanForMarkDelete(StudentInfo stu, TeachingGuidePlan teachingGuidePlan, String brSchoolid) throws Exception{
			//修改学生的学习计划
			List<StudentLearnPlan> studentLearnPlans        = studentLearnPlanService.findByHql(" from "+StudentLearnPlan.class.getSimpleName()+" where studentInfo.resourceid = ? ", stu.getResourceid());
			//TeachingPlan teachingPlan = stu.getTeachingPlan();
			Set<TeachingPlanCourse> distTeachingPlanCourse = null;
			//String message = "";
			if (teachingGuidePlan != null) {
				distTeachingPlanCourse = teachingGuidePlan.getTeachingPlan().getTeachingPlanCourses();
			} else {
				return null;
			}
			
			//List<CourseOrder> orderList       				= new ArrayList<CourseOrder>();
			//List<ExamResults> resultList                    = new ArrayList<ExamResults>();
			List<StudentLearnPlan> p_studentLearnPlans      = new ArrayList<StudentLearnPlan>();
			List<StudentLearnPlan> remove_studentLearnPlans = new ArrayList<StudentLearnPlan>();
			
			if(null != studentLearnPlans && !studentLearnPlans.isEmpty() && !distTeachingPlanCourse.isEmpty()){			
		    	for(StudentLearnPlan studentLearnPlan : studentLearnPlans){	
		    		StudentLearnPlan p_stuLearnPlan 				= new StudentLearnPlan();
		    		if(studentLearnPlan.getExamResults() == null || studentLearnPlan.getExamResults().getIsDeleted()==1){// 学习计划中没有成绩直接删除（注：后面会创建新的）
		    			remove_studentLearnPlans.add(studentLearnPlan);
		    		}else{// 有成绩要修改课程，年度和学期
		    			try {
		    				PropertyUtils.copyProperties(p_stuLearnPlan, studentLearnPlan);
		    			}catch(Exception e){
		    				e.printStackTrace();
		    				throw new Exception("拷贝属性出错");
		    			}
		    			//p_stuLearnPlan = studentLearnPlan;//这里可能会保存到.重新建一个
			    		if(null != studentLearnPlan.getTeachingPlanCourse()){
				    		Course origCourse = studentLearnPlan.getTeachingPlanCourse().getCourse();
				    		//将原教学计划课程与现教学计划课程匹配，如果课程一致，则修改为现在教学计划的课程
				    		for(TeachingPlanCourse teachingPlanCourse : distTeachingPlanCourse){
				    			Course distCourse = teachingPlanCourse.getCourse();
				    			
				    			if(origCourse.getResourceid().equals(distCourse.getResourceid())){
				    				//课程匹配
				    				//TODO 学分问题，两教学计划的课程学分数不一致
				    				p_stuLearnPlan.setTeachingPlanCourse(teachingPlanCourse);
				    				p_stuLearnPlan.setPlanoutCourse(null);
				    				// 获取开课学期
				    				try {
										List<Map<String, Object>> year_termList = teachingPlanCourseStatusService.findYearAndTerm(teachingPlanCourse.getResourceid(), teachingGuidePlan.getGrade().getResourceid(),
												brSchoolid,teachingGuidePlan.getTeachingPlan().getResourceid());
										if(ExCollectionUtils.isNotEmpty(year_termList)){
											Map<String, Object> year_term = year_termList.get(0);
											String yearInfoId = (String)year_term.get("yearInfoId");
											String term = (String)year_term.get("term");
											YearInfo _yearInfo = yearInfoService.get(yearInfoId);
											p_stuLearnPlan.setYearInfo(_yearInfo);
											p_stuLearnPlan.setTerm(term);
											if(!"0".equals(studentLearnPlan.getExamResults().getExamAbnormity())){
												p_stuLearnPlan.setFinalScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", studentLearnPlan.getExamResults().getExamAbnormity()));
											}else{
												p_stuLearnPlan.setFinalScore(studentLearnPlan.getExamResults().getIntegratedScore());
											}
											//p_stuLearnPlan.setStudentId(stu.getResourceid());
										}
									} catch (Exception e) {
										throw new ServiceException("学籍异动时，获取开课学期出错！");
									}
						    		p_studentLearnPlans.add(p_stuLearnPlan);
				    				logger.debug("匹配到的课程："+origCourse.getCourseName());
				    				remove_studentLearnPlans.add(studentLearnPlan);
				    				break;
				    			}
				    		}	
			    		}
		    		}
		    	}
		    	//String courseOrderHql			= " from "+CourseOrder.class.getSimpleName()+" o where o.isDeleted=? and o.studentInfo.resourceid=? and o.courseOrderStat.course.resourceid=? ";
		    	//移走匹配好的课程
		    	studentLearnPlans.removeAll(remove_studentLearnPlans);
		    	for(StudentLearnPlan studentLearnPlan : studentLearnPlans){
		    		if(null != studentLearnPlan.getTeachingPlanCourse()){
		    			StudentLearnPlan p_stuLearnPlan = new StudentLearnPlan();
		    			Course origCourse = studentLearnPlan.getTeachingPlanCourse().getCourse();
			    		ExamResults examResults = studentExamResultsService.getStudentPlanCourseMaxExamResults(stu, origCourse.getResourceid(), true);
			    				//studentLearnPlan.getExamResults();
			    		if(null != examResults){//如果该课程成绩不为空，将转换为计划外课程
			    			//gchw:此处感觉不宜将教学计划课程清空，如若清空，计划外课程永远不能变回计划内课程
			    			//studentLearnPlan.setTeachingPlanCourse(null);
			    			try {
			    				PropertyUtils.copyProperties(p_stuLearnPlan, studentLearnPlan);
			    			}catch(Exception e){
			    				e.printStackTrace();
			    				throw new Exception("拷贝属性出错");
			    			}
			    			p_stuLearnPlan.setPlanoutCourse(origCourse);
			    			p_stuLearnPlan.setExamResults(examResults);
			    			if(!"0".equals(studentLearnPlan.getExamResults().getExamAbnormity())){
								p_stuLearnPlan.setFinalScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", studentLearnPlan.getExamResults().getExamAbnormity()));
							}else{
								p_stuLearnPlan.setFinalScore(studentLearnPlan.getExamResults().getIntegratedScore());
							}
	    					logger.debug("异动为计划外的课程："+origCourse.getCourseName());
	    					p_studentLearnPlans.add(p_stuLearnPlan);
	    				}/*else{//否则，标示为-1
	    					
	    					if(null!=studentLearnPlan.getExamResults()) resultList.add(studentExamResultsService.get(studentLearnPlan.getExamResults().getResourceid()));
	    					String courseId     = null==studentLearnPlan.getTeachingPlanCourse()?studentLearnPlan.getPlanoutCourse().getResourceid():studentLearnPlan.getTeachingPlanCourse().getCourse().getResourceid();
	    					String yearId       = "";
	    					String term         = "";
	    					String hql          = "";
	    					if (studentLearnPlan.getStatus()==1) {
	    						yearId          = studentLearnPlan.getYearInfo().getResourceid();
								term            = studentLearnPlan.getTerm();
								hql  			= courseOrderHql +" and o.orderCourseYear.resourceid=? and o.orderCourseTerm=? ";
							}else if(studentLearnPlan.getStatus()==2) {
								yearId          = null==studentLearnPlan.getOrderExamYear()?"":studentLearnPlan.getOrderExamYear().getResourceid();
								term            = null==studentLearnPlan.getOrderExamTerm()?"":studentLearnPlan.getOrderExamTerm();
								if(ExStringUtils.isEmpty(yearId)){
									yearId          = studentLearnPlan.getYearInfo().getResourceid();
								}
								if(ExStringUtils.isEmpty(term)){
									term            = studentLearnPlan.getTerm();
								}
								hql  			= courseOrderHql +" and o.orderCourseYear.resourceid=? and o.orderCourseTerm=? ";
							}
	    					if (ExStringUtils.isNotEmpty(courseId)&&ExStringUtils.isNotEmpty(yearId)&&ExStringUtils.isNotEmpty(term)) {
	    						orderList.addAll(courseOrderService.findByHql(hql,0,stu.getResourceid(),courseId,yearId,term));
							}
	    					studentLearnPlan.setIsDeleted(-1);
	    				}*/
			    		
		    		}
		    	}
		    	/*for (ExamResults rs : resultList) {
		    		rs.setIsDeleted(1);
				}
		    	for (CourseOrder co : orderList) {
					co.setIsDeleted(1);
				}
		    	studentExamResultsService.batchSaveOrUpdate(resultList);
		    	courseOrderService.batchSaveOrUpdate(orderList);
		    	studentLearnPlanService.batchSaveOrUpdate(p_studentLearnPlans);*/
		    }
			/*try {
				// 给还没有学习计划的课程创建学习计划
				List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
				studentInfoList.add(stu);
				studentLearnPlanService.generateStudyPlanByCondition(studentInfoList, stu.getResourceid(),stu.getGrade().getResourceid(),
						stu.getBranchSchool().getResourceid(),stu.getTeachingPlan().getResourceid());
			} catch (Exception e) {
				throw new ServiceException("学籍异动时，创建学习计划出错！");
			}*/
			return p_studentLearnPlans;
		}
		
		//复学删除标记删除的成绩
		private String updateMarkDeleteResults(StudentInfo stu) throws Exception {
			//处理标记删除逻辑
			List<ExamResults> templist = new ArrayList<ExamResults>();
			List<ExamResults> results = studentExamResultsService.findByHql("from ExamResults t where t.course.resourceid in ( select rs.course.resourceid from ExamResults rs where rs.isDeleted=0 and rs.isMarkDelete='Y' and rs.studentInfo.resourceid=?) and t.isDeleted=0 and t.studentInfo.resourceid=?", stu.getResourceid(),stu.getResourceid());
			List<Course> courses = new ArrayList<Course>();
			List<StudentMakeupList> makeuplist = new ArrayList<StudentMakeupList>();
			for(ExamResults rs : results){
				rs.setIsDeleted(1);
				courses.add(rs.getCourse());
				templist.add(rs);
			}
									
			//同时删除补考名单
			if(results.size()!=0 && courses.size()!=0){
				List<StudentMakeupList> makeup = new ArrayList<StudentMakeupList>();
				makeup = studentMakeupListService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentInfo", stu),Restrictions.in("course",courses));
				if(makeup!=null&&makeup.size()>0){
					for(StudentMakeupList sm:makeup){
						sm.setIsDeleted(1);
						makeuplist.add(sm);			
					}
				}
				
				studentExamResultsService.batchSaveOrUpdate(templist);
				studentMakeupListService.batchSaveOrUpdate(makeuplist);
			}
								
			return "";//暂时不用返回什么信息
		}
		
		
		/*
		 * 修改学生缴费注册信息的申请接口
		 */
		private void updateStudentFee(HttpServletRequest request,List<Map<String, Object>> list){
			try{
				//级联修改缴费模块的信息
				int totalSize = 0;
				int faileSize = 0;// 失败数量
				Date today = new Date();
				String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
				StringBuffer msg = new StringBuffer();
				// 接收异步通知接口
				String receiveUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/tempStudentFee/editInfo.html";
				HeadVO headVO = new HeadVO(Constants.EDU_STU_INFO_MODIFY,dateTime,receiveUrl);
				
				List<Map<String, Object>> applyMapList = new ArrayList<Map<String,Object>>();
				for(Map<String, Object> map : list)	{
					applyMapList.add(map);
					if(applyMapList.size()%50==0){
						String _msg = null;
						Document returnDoc =TonlyPayUtil.modifyStudentInfo(headVO, applyMapList);
						msg = getReturnInfo(returnDoc, msg,faileSize);
						_msg = msg.toString();
						int sizeIndex = _msg.lastIndexOf("-");
						faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
						msg.setLength(0);
						totalSize+=50;
						applyMapList.clear();
					}
				}
				if(ExCollectionUtils.isNotEmpty(applyMapList)){// 注销剩下的
					String _msg = null;
					Document returnDoc =TonlyPayUtil.modifyStudentInfo(headVO, applyMapList);
					msg = getReturnInfo(returnDoc, msg,faileSize);
					_msg = msg.toString();
					int sizeIndex = _msg.lastIndexOf("-");
					faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
					totalSize+=applyMapList.size();
					applyMapList.clear();
				}
				logger.info("申请成功："+(totalSize-faileSize)+" 条，申请失败："+faileSize+" 条");
			}catch(Exception e){
				logger.error("学籍异动时,修改学生信息,同步第三方申请失败" + e);
			}
		}

	/*
	 * 第三方接口的返回信息
	 */
	private StringBuffer getReturnInfo(Document returnDoc, StringBuffer msg, int faileSize){
		Element root = returnDoc.getRootElement();
		Element body = XMLUtils.getChild(root, "BODY");
	//	String fileNo =XMLUtils.getChild(body, "FILE_NO").getText();//TODO:以后添加 处理批次号
		List<Element> dataList = XMLUtils.getChildElements(body, "DATALIST");
		if(ExCollectionUtils.isNotEmpty(dataList)){
			String receiptCode = null;
			String studentId = "";
			String receiptMsg = null;
			for(Element data : dataList){
	//			System.out.println("-------------"+data.asXML());
				receiptCode = XMLUtils.getChild(data, "RETURN_CODE").getText();
				if(!Constants.RETURN_STATUS_SUCESS.equals(receiptCode)){// 失败
					faileSize = faileSize+1;
					if(XMLUtils.getChild(data, "BUSINESS_ID")!=null){
						studentId =XMLUtils.getChild(data, "BUSINESS_ID").getText()+" : ";
					}
					receiptMsg = XMLUtils.getChild(data, "RETURN_MSG").getText();
					msg.append(studentId+receiptMsg+"<br>");
				}
			}
		}
		msg.append("-"+faileSize);
		return msg;
	}
	
	@Override
	public List<StuChangeInfo> getStuChangeInfoByMonth(String brschoolId) throws ServiceException{
		List<StuChangeInfo> list =new ArrayList<StuChangeInfo>();
		//注意：1、查找距离当前系统时间为11~12、23~24个月的休学申请，以申请时间为准  2、学生学籍状态仍为休学
		String hql = " from "+StuChangeInfo.class.getSimpleName()+" where studentInfo.branchSchool.resourceid=:brschoolId and changeType='11' and isDeleted=0 and finalAuditStatus='Y' and "
				+ "( add_months(applicationDate,11)<= current_date() and add_months(applicationDate,12) > current_date() or add_months(applicationDate,23)<= current_date() and add_months(applicationDate,24) > current_date())" ;
//		String hql = " from "+StuChangeInfo.class.getSimpleName()+" where studentInfo.branchSchool.resourceid=:brschoolId and changeType='11' and isDeleted=0 and finalAuditStatus='Y' and studentInfo.studentStatus='11'";
		Map<String,Object> values = new HashMap<String, Object>();
		values.put("brschoolId", brschoolId);
		
		list=findByHql(hql, values);
		return list;
	}
	
}
