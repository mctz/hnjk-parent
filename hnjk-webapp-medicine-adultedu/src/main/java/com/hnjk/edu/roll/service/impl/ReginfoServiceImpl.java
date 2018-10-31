
package com.hnjk.edu.roll.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.ws.WebServiceException;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.SetFirstTermCourseForStuRegException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.ExameeInfo;
import com.hnjk.edu.recruit.model.ExportRecruitPlan;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IExameeInfoService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.roll.model.Reginfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IReginfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.CourseOrder;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.ICourseOrderService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IRoleService;
import com.hnjk.security.service.IUserService;

/**
 * <code>ReginfoServiceImpl</code><p>
 * 学生注册信息表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:12:54
 * @see 
 *
 * @version 1.0
 */
@Transactional
@Service("reginfoservice")
public class ReginfoServiceImpl extends BaseServiceImpl<Reginfo> implements IReginfoService {

	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;//报名信息服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
		
	@Autowired
	@Qualifier("roleService")
	private IRoleService roleService;//注入角色服务接口
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingguideplanservice;//年级教学计划服务
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;//招生计划服务
		
	@Autowired
	@Qualifier("userService")
	private IUserService userService;//注入用户服务
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentBaseInfoService;//学生基本信息服务
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;//学生教学计划服务
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;//年级服务
	
	@Autowired
	@Qualifier("courseOrderService")
	private ICourseOrderService courseOrderService;//注入学生预约课程服务
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	public void saveRegInfoList(List<Reginfo> regList) {
		exGeneralHibernateDao.saveOrUpdateCollection(regList);	
	}
	
	@Autowired
	@Qualifier("exameeInfoService")
	private IExameeInfoService exameeInfoService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	

	/*
	 * 统计符合注册条件的学生
	 */
	@Override
	@Transactional(readOnly=true)
	public List<EnrolleeInfo> countRegisterStudent(Map<String, Object> condition) throws ServiceException {
		StringBuffer sb = new StringBuffer();		
		Integer gradeYear = (Integer)condition.get("gradeYear");
		condition.remove("gradeYear");
		sb.append(" from "+EnrolleeInfo.class.getSimpleName()+" en  where en.isDeleted = :isDeleted ");
		condition.put("isDeleted", 0);
		sb.append(" and en.recruitMajor is not null ");
		sb.append(" and en.branchSchool is not null ");
		if(condition.containsKey("branchSchool")) {
			sb.append(" and  en.branchSchool.resourceid = :branchSchool");//学习中心
		}
		if(condition.containsKey("classic")) {
			sb.append(" and en.recruitMajor.classic.resourceid = :classic ");//层次
		}
		if(condition.containsKey("grade")) {
			sb.append(" and en.recruitMajor.recruitPlan.grade.resourceid=:grade ");	//年级
		}
			
		if(condition.containsKey("isMatriculate")) {
			sb.append(" and en.isMatriculate = :isMatriculate ");//是否录取
		}
	
		sb.append(" and  (en.entranceflag = :entranceflag1 or ( en.noExamFlag=:noExamFlag and en.entranceflag = :entranceflag2)) ");//通过入学资格审核的，或通过免试资格审核
		condition.put("entranceflag1", Constants.BOOLEAN_YES);
		condition.put("noExamFlag", Constants.BOOLEAN_YES);
		condition.put("entranceflag2", Constants.BOOLEAN_YES);
		
		//没有注册学籍 		
		sb.append(" and not exists (");
		sb.append(" select t.studyNo from "+StudentInfo.class.getSimpleName()+" t where t.isDeleted = :isDeleted ");
		sb.append(" and t.studyNo = en.matriculateNoticeNo ");
		sb.append(")");
		List stuList = exGeneralHibernateDao.findByHql(sb.toString(), condition);
		
		//查询是否欠费 
		//List feeList = exGeneralHibernateDao.findByHql(" from "+StudentFactFee.class.getSimpleName()+ " where  recpayFee = facepayFee+derateFee and chargeyear = ?", gradeYear);
		
		return stuList;
		/*
		 * 先不考虑费用问题
		List<EnrolleeInfo> retList = new ArrayList<EnrolleeInfo>();
				
		if(null != feeList && null != stuList){//遍历本年缴费记录，看学生是否已缴清，如果缴清，则放入注册列表中
			for (int i = 0; i < stuList.size(); i++) {
				EnrolleeInfo enrolleeInfo = (EnrolleeInfo)stuList.get(i);
				for (int j = 0; j < feeList.size(); j++) {
					StudentFactFee fee = (StudentFactFee)feeList.get(j);
					if(enrolleeInfo.getMatriculateNoticeNo().equals(fee.getStudyNo())){						
						//if(fee.getFacepayFee()+fee.getDerateFee() == fee.getRecpayFee()){//实缴+免缴>=应缴
							retList.add(enrolleeInfo);	
							break;
						//}
					}
				}
			}
			return retList;
		}
		return null;
		*/
						
		
	}



	/*
	 * 注册学籍
	 * (non-Javadoc)
	 * @see com.hnjk.edu.roll.service.IReginfoService#regStudentInfo(com.hnjk.edu.recruit.model.EnrolleeInfo, java.util.List)
	 */	
	//@Transactional(noRollbackFor=SetFirstTermCourseForStuRegException.class)
	private List<StudentInfo> regStudentInfo(List<EnrolleeInfo> enrolleeInfoList, List<Reginfo> regList) throws ServiceException {
		
		List<StudentInfo> stuList = new ArrayList<StudentInfo>();
		
				
		/*~~~~~~~~~~~1.准备学生的系统用户资料~~~~~~~~~~~~~*/
		User user = null;
		String pwd =  CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue();//初始化密码
		String studentRoleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.student").getParamValue();//学生角色编码
		Role studentRole  = roleService.findUnique("from "+ Role.class.getSimpleName()+" where roleCode = ?", studentRoleCode);
		Set<Role> roleSet = new HashSet<Role>();
		roleSet.add(studentRole);
		
		StudentInfo studentInfo = null;
		StudentBaseInfo studentBaseInfo = null;
		try {
			/*~~~~~~~~~~~~~~~~~2.遍历学生注册表，生成学生账号*/
			for(Reginfo reginfo : regList){
				
				//判断学生是否关联了基本用户信息
				studentInfo = reginfo.getStudentInfo();					
				String studentBaseInfoId = studentInfo.getStudentBaseInfo().getResourceid();
				if(ExStringUtils.isEmpty(studentBaseInfoId)){
					logger.warn("学生：[ {} ] 没有个人基本信息！",studentInfo.getStudentName());
					continue;
					//throw new ServiceException("学生："+studentInfo.getStudentName()+" 没有个人基本信息！");
				}
				
				//判断系统用户表中是否存在账号,若不存在，则创建新用户
				if(!userService.isExistsUser(reginfo.getStudentInfo().getStudentBaseInfo().getCertNum())){
					user = new User();
					user.setCnName(reginfo.getStudentInfo().getStudentBaseInfo().getName());//中文名
					user.setUsername(reginfo.getStudentInfo().getStudentBaseInfo().getCertNum().replace("（", "(").replace("）", ")"));//账号，取学生身份证号
								
					user.setPassword(pwd);//密码，取系统配置的默认密码				
					user.setUnitId(reginfo.getStudentInfo().getBranchSchool().getResourceid());//教学中心ID
					user.setOrgUnit(reginfo.getStudentInfo().getBranchSchool());//学生所在教学中心					
					user.setRoles(roleSet);//学生角色
					user.setUserType(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue());//用户类型	
					userService.save(user);
				}else{
					user = userService.getUserByLoginId(reginfo.getStudentInfo().getStudentBaseInfo().getCertNum());
					user.setUnitId(reginfo.getStudentInfo().getBranchSchool().getResourceid());//教学中心ID
					user.setOrgUnit(reginfo.getStudentInfo().getBranchSchool());//学生所在教学中心			
					userService.update(user);
				}				
				
								
				studentBaseInfo = studentBaseInfoService.get(studentBaseInfoId);
				
				//studentBaseInfo.setSysUser(user);
				studentInfo.setStudentBaseInfo(studentBaseInfo);//设置学生基本信息
				studentInfo.setSysUser(user);//设置系统用户 
				reginfo.setStudentInfo(studentInfo);
				reginfo.setYearInfo(studentInfo.getGrade().getYearInfo());
				reginfo.setTerm(studentInfo.getGrade().getTerm());
				
				exGeneralHibernateDao.saveOrUpdate(reginfo);
				
				//更新默认学籍ID
				UserExtends userExtends = null;				
				if(null != user.getUserExtends().get(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
					userExtends = user.getUserExtends().get(UserExtends.USER_EXTENDCODE_DEFAULTROLLID);
					userExtends.setExValue(studentInfo.getResourceid());
				}else{
					userExtends = new UserExtends(UserExtends.USER_EXTENDCODE_DEFAULTROLLID,studentInfo.getResourceid(),user);
				}	
				user.getUserExtends().put(UserExtends.USER_EXTENDCODE_DEFAULTROLLID, userExtends);
				user.setEnabled(true);// 启用账号，专本连读的学生账号是不变的
				userService.update(user);				
				
				stuList.add(studentInfo);
			}
			/* ~~~~~~~~~~~~3.将学生报名信息表值为已注册~~~~~~~~~~~~*/
			enrolleeInfoService.batchSaveOrUpdate(enrolleeInfoList);
			//for(EnrolleeInfo enrolleeInfo : enrolleeInfoList){
			//	enrolleeInfoService.update(enrolleeInfo);
			//}		
		} catch (Exception e) {
			logger.error("注册用户出错：{}",e.fillInStackTrace());
			throw new WebServiceException("注册用户出错："+e.getMessage());
		}
		return stuList;
	}

	
	
	//预约注册成功的新生首学期课程
	@Override
	public int doRegisterStuFirstTermCourse(List<StudentInfo> registeredStus)	throws ServiceException {
		/**/
		int setFirstTermCourseNum = 0;
		//~~~~~~~~~~~~~~预约学生首学期课程~~~~~~~~~~				
		if(null != registeredStus && registeredStus.size()>0){					
			Map<String, Object> param  =  new HashMap<String, Object>();
			for (StudentInfo stu : registeredStus) {
				if(null != stu.getTeachingGuidePlan()){
					param.put("teachingGuidePlan", stu.getTeachingGuidePlan());
					param.put("studentInfo", stu);
					try{
						studentLearnPlanService.setFirstTermCourseOperaterForStudentReg(param);
						setFirstTermCourseNum++;
					}catch (SetFirstTermCourseForStuRegException e) {
						logger.warn("新生 [ {} ] 预约首学期教学计划失败:{}",stu.getStudentName(),e.getMessage());
						continue;
					}							
					
				}//end if
				
			}//end for
			
		}
		return setFirstTermCourseNum;
		
		//return 0;
	}

	//删除预约注册成功的新生首学期课程
	@Override
	public void deleteRegisterStuFirstTermCourse(StudentInfo studentInfo) throws ServiceException {
		/**/
		if(null != studentInfo){
			try{
				//删除相关预约批次
				List<CourseOrder> oldCourseOrderList = courseOrderService.findByCriteria(Restrictions.eq("studentInfo", studentInfo));
				for(CourseOrder courseOrder:oldCourseOrderList){
					courseOrderService.truncate(courseOrder);
				}
				
				//删除相关学习计划
				List<StudentLearnPlan> studentLearnPlanlist = studentLearnPlanService.findByCriteria(Restrictions.eq("studentInfo.resourceid",studentInfo.getResourceid()),Restrictions.eq("isDeleted",0));
				for(StudentLearnPlan studentLearnPlan:studentLearnPlanlist){
					studentLearnPlanService.truncate(studentLearnPlan);
				}
				
				//删除注册表
				String regHql = "delete from EDU_ROLL_REGINFO r where r.isDeleted=:isDeleted and r.STUDENTID=:studentId";
				Map<String,Object> regMap = new HashMap<String,Object>();
				regMap.put("isDeleted", 0);
				regMap.put("studentId", studentInfo.getResourceid());
				baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(regHql,regMap);
				
				//修改报名信息状态位
				List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findByCriteria(Restrictions.eq("matriculateNoticeNo",studentInfo.getStudyNo())
						,Restrictions.eq("studentBaseInfo",studentInfo.getStudentBaseInfo()),Restrictions.eq("isDeleted",0));//学生报名表	
				if(enrolleeInfoList.size()>0){
					EnrolleeInfo erinfo = enrolleeInfoList.get(0);
					erinfo.setRegistorFlag(Constants.BOOLEAN_NO);
					enrolleeInfoService.saveOrUpdate(erinfo);
				}
				//删除学籍信息
				studentInfoService.truncate(studentInfo);
			}catch (Exception e) {
				e.printStackTrace();
				logger.warn("驳回未注册失败:{}",studentInfo.getStudentName(),e.getMessage());
			}	
		}
		//return 0;
	}
	//删除预约注册成功的学生的用户名以及用户对应的角色，默认学籍等
	@Override
	public void deleteUserOrRole(String studentId,String sysuserid) throws Exception{
		
		//删除相关漠然学籍信息
		Map<String,Object> params = new HashMap<String,Object>();
		String hql = "delete hnjk_sys_usersextend u where u.excode=:defalutrollid and u.exvalue =:exvalue and u.sysuserid=:sysuserid";
		params.put("defalutrollid", UserExtends.USER_EXTENDCODE_DEFAULTROLLID);
		params.put("exvalue", studentId);
		params.put("sysuserid", sysuserid);
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(hql, params);
		
		//删除用户VS角色对应信息
		params.remove("defalutrollid");
		params.remove("exvalue");
		String URDql  = "DELETE FROM HNJK_SYS_ROLEUSERS WHERE userid IN (SELECT resourceid FROM HNJK_SYS_USERS WHERE usertype='student' and resourceid=:sysuserid)";
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(URDql, params);
		//删除论坛账户..
		String bbsHql = "delete EDU_BBS_USERINFO b where b.userid =:sysuserid ";
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(bbsHql, params);
		//删除用户信息
		String UDql = "DELETE FROM HNJK_SYS_USERS WHERE usertype='student' and resourceid=:sysuserid";
		baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(UDql, params);
	}


	/*
	 * 学生注册学籍过程 
	 */
	@Override
	public List<StudentInfo> doRegister(String[] enrolleeInfoIds) throws ServiceException{
		User user = SpringSecurityHelper.getCurrentUser();
		List<Reginfo> regList = new ArrayList<Reginfo>();
		List<EnrolleeInfo> enrolleeList = new ArrayList<EnrolleeInfo>();
		
		if(null == user){//如果当前用户为空，则表示为JMX远程调用，使用hnjk作为注册用户
			user = userService.getUserByLoginId("hnjk");			
		}
		
		//学号是否沿用录取库里的准学号
		String isReferenceStudyNo = "y";
		String studynoLevel = "";//新学号生成规则
		List<Map<String, Object>> maxStudentNoSuffixMap_list = null;
		Map<String, String> maxStudentNoSuffixMap = new HashMap<String, String>(0);
		try {
			//是否沿用准学号作为学生学号
			isReferenceStudyNo = CacheAppManager.getSysConfigurationByCode("yesorno.reference.quasiStudyno").getParamValue();
		} catch (Exception e) {
			
		}
		
		if("N".equals(isReferenceStudyNo)){
			studynoLevel = CacheAppManager.getSysConfigurationByCode("studentinfo.registered.studynorule").getParamValue();
			if ("1".equals(studynoLevel)) {//教科
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(0,3,4,8);//学生学号当前流水号，key:前缀, value:流水号
			} else if ("2".equals(studynoLevel)) {//广东医    12位
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(0,9,10,13);//学生学号当前流水号，key:前缀, value:流水号
			} else if ("3".equals(studynoLevel)) {//广大    12位
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(0,9,10,12);//学生学号当前流水号，key:前缀, value:流水号
				enrolleeInfoIds = enrolleeInfoService.orderByPinyinHeadChar(enrolleeInfoIds);//按教学点、专业、姓名首字母排序
			} else if("4".equals(studynoLevel)){ //安徽医学院学号生成    12位
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(0,6,7,10);//学生学号当前流水号，key:前缀, value:流水号
			} else if("5".equals(studynoLevel)){ //广西医科大的学籍学生成规则    12位
//				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(0,8,9,12);
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered2(1,3,7,2,9,12);
			}else if("6".equals(studynoLevel)){ //广西桂林医的学籍学生成规则     11位
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(0,7,8,11);
			}else if("7".equals(studynoLevel)){ //广西右江医的学籍学号生成规则  10位
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(1,7,8,10);//oracle substr是下标由1开始,第二个参数是截取的长度
			}else if("8".equals(studynoLevel)){ //广外：15位：学校代码（5位）+教学点识别码（2位）+年级代码（2位）+层次代码（1位）+专业代码（2位）+学生顺序号（3位）。
				maxStudentNoSuffixMap_list = enrolleeInfoService.getMaxStudyNoPrefixMapRegistered(1,12,13,15);//oracle substr是下标由1开始,第二个参数是截取的长度
			}else if("9".equals(studynoLevel)){//汕大：12位 年份两位（2位）+层次（1位）+学习形式（1位）+专业号（3位）+教学点（2位）+顺序号（3位）
				
			}
			
			for (Map<String, Object> map : maxStudentNoSuffixMap_list) {
				maxStudentNoSuffixMap.put(map.get("p").toString(), map.get("s").toString());
			}
		}
		List<StudentBaseInfo> stuBaseList = new ArrayList<StudentBaseInfo>();
		Grade _grade = null;
		for (int i = 0; i < enrolleeInfoIds.length; i++) {//遍历学生ID
			String studentId = enrolleeInfoIds[i].toString();
			/*~~~~~~~~~~~~~~1.构造学生注册信息~~~~~~~~~~~~~~~*/
			Reginfo reginfo = new Reginfo();//注册信息表
			reginfo.setYearInfo(null);
			reginfo.setFillinManId(user.getResourceid());
			reginfo.setFillinMan(user.getUsername());
			reginfo.setFillinDate(new Date());			
			if("hnjk".equals(user.getUsername())){
				reginfo.setMemo("系统自动注册");
			}
			/*~~~~~~~~~~~~~~2.构造学生学籍信息~~~~~~~~~~~~~*/
			StudentInfo stu = new StudentInfo();//学籍信息表
			
			/*~~~~~~~~~~~~~~3.校验学生是否满足注册学籍的条件~~~~~~~~~~~~~~~*/
			EnrolleeInfo enrolleeInfo = enrolleeInfoService.get(studentId);//学生报名表	
			StudentBaseInfo stuBase = studentBaseInfoService.get(enrolleeInfo.getStudentBaseInfo().getResourceid());
			//将学生录取时的相片复制到studentbaseInfo表中
			String tmpparam = "",tmpProperty="";
			if("0".equals(CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue())){
				tmpparam=enrolleeInfo.getExamCertificateNo();
				tmpProperty="ZKZH";
			}else{
				tmpparam=enrolleeInfo.getEnrolleeCode();
				tmpProperty="KSH";
			}
			ExameeInfo examee = exameeInfoService.findUniqueByProperty(tmpProperty, tmpparam);
			if(examee!=null){
				if(ExStringUtils.isNotBlank(examee.getBackPhotoPath())){
					stuBase.setRecruitPhotoPath(examee.getBackPhotoPath());
				}else if (ExStringUtils.isNotBlank(examee.getMainPhotoPath())) {
					stuBase.setRecruitPhotoPath(examee.getMainPhotoPath());
				}
				stuBaseList.add(stuBase);
			}
			else{
				logger.error("没有找到该考生(准考证)号的学生:"+tmpparam);
			}
			
			List<StudentInfo> stulist=studentInfoService.findByHql("from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted= 0 and stu.examCertificateNo= ? and stu.enrolleeCode= ? ", enrolleeInfo.getExamCertificateNo(),enrolleeInfo.getEnrolleeCode());
			if(stulist.size()>0) {
				continue;
			}
			/*
			 *TODO 这部分需要参考实际需求来实现*/
			String majorId  = enrolleeInfo.getRecruitMajor().getMajor().getResourceid();//专业ID
			if(ExStringUtils.isEmpty(majorId)){
				//考虑批量注册，如果学生部满足注册条件则忽略，而不是终止
				logger.warn("学生：[ {} ]注册失败：{}",enrolleeInfo.getStudentBaseInfo().getName(),"没有对应的招生专业.");
				continue;
				
			}
			String classic = enrolleeInfo.getRecruitMajor().getClassic().getResourceid();//层次ID
			if(ExStringUtils.isEmpty(classic)){
				logger.warn("学生：[ {} ]注册失败：{}",enrolleeInfo.getStudentBaseInfo().getName(),"没有对应的招生专业-层次.");
				continue;
			
			}
			String s1 = enrolleeInfo.getRecruitMajor().getRecruitPlan().getResourceid();
			if(ExStringUtils.isEmpty(s1)){
				logger.warn("学生：[ {} ]注册失败：{}",enrolleeInfo.getStudentBaseInfo().getName(),"没有对应的招生专业或招生计划.");
				continue;
				
			}
			
			RecruitPlan recruitPlan = recruitPlanService.get(s1) ;	//从学生的招生计划中，获取当前学生的注册年级		
			if(null == recruitPlan.getGrade()){
				logger.warn("学生：[ {} ]注册失败：{}",enrolleeInfo.getStudentBaseInfo().getName(),"招生批次中没有对应的年级.");
				continue;
				
			}
			_grade = recruitPlan.getGrade();
			String gradeId = _grade.getResourceid();//年级ID
			// 判断是否是保留学籍的考生
			if("10".equals(examee.getKSZT())){
				Calendar calendar = Calendar.getInstance();
				calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH)+1);
				YearInfo yi = yearInfoService.getByFirstYear(Long.valueOf(calendar.get(Calendar.YEAR)));
				if(yi!=null){
					_grade = gradeService.findUnique("from Grade g where g.isDeleted=0 and g.yearInfo.resourceid=?", yi.getResourceid());
					if(_grade==null){
						logger.warn("学生：[ {} ]注册失败：{}",enrolleeInfo.getStudentBaseInfo().getName(),"没有对应的年级.");
						continue;
					}
					gradeId = _grade.getResourceid();
				}
			}
			
//			String gradeId = recruitPlan.getGrade().getResourceid();//年级ID
			
			//获取学生的当前年级教学计划,年级+专业+层次+教学类型			
			List<TeachingGuidePlan> guidePlan = teachingguideplanservice.findByHql("from TeachingGuidePlan guidePlan where guidePlan.grade.resourceid=? " +
												" and guidePlan.ispublished='Y' and guidePlan.teachingPlan.major.resourceid=? " +
													" and guidePlan.teachingPlan.classic.resourceid=? and guidePlan.teachingPlan.schoolType=? ", 
					new String[]{gradeId,majorId,classic,enrolleeInfo.getTeachingType()});			
			
			if (null!=guidePlan && guidePlan.size()==1) {
				stu.setTeachingPlan(guidePlan.get(0).getTeachingPlan());//添加教学计划
				stu.setTeachingGuidePlan(guidePlan.get(0));//年级教学计划，用来预约首学期课程
				
				//如果不为空，则判断是否有学习中心教学计划，有则关联
				for(TeachingGuidePlan plan  : guidePlan ){
					TeachingPlan teachingPlan = plan.getTeachingPlan();					
					if(null != teachingPlan.getOrgUnit() && teachingPlan.getOrgUnit().getResourceid().equals(enrolleeInfo.getBranchSchool().getResourceid())){
						stu.setTeachingPlan(teachingPlan);
						stu.setTeachingGuidePlan(plan);
						break;
					}
				}
				
			}else if (null!=guidePlan && guidePlan.size()>1) {
				logger.warn("学生：[ {} ] 根据年级+专业+层次+办学模式查出多个教学计划，请手动关联该学生的年纪教学计划！",enrolleeInfo.getStudentBaseInfo().getName());
			}else {
				logger.warn("学生： [ {} ] 没有对应的教学计划，请手动关联该学生的年纪教学计划!",enrolleeInfo.getStudentBaseInfo().getName());
			}
			
			/*~~~~~~~~~~~~~4.填充学籍信息~~~~~~~~~~~~~~~*/
			stu.setInDate(_grade.getIndate());//入学日期  update:2014-7-21 16:32:53
			stu.setTeachingType(enrolleeInfo.getTeachingType());
			stu.setStudentBaseInfo(enrolleeInfo.getStudentBaseInfo());
			stu.setMajor(enrolleeInfo.getRecruitMajor().getMajor());
//			stu.setGrade(enrolleeInfo.getRecruitMajor().getRecruitPlan().getGrade()); 
			stu.setGrade(_grade); 
			stu.setBranchSchool(enrolleeInfo.getBranchSchool());
			stu.setClassic(enrolleeInfo.getRecruitMajor().getClassic());
			if("N".equals(ExStringUtils.trim(isReferenceStudyNo).toUpperCase())){ //选用生成学号的规则
				String studyno = getStudyNo(enrolleeInfo,maxStudentNoSuffixMap,studynoLevel); //按各学院注册学号生成规则生成学号
				if(ExStringUtils.isNotBlank(studyno)){
					stu.setStudyNo(studyno);
				}else{
					logger.error("注册学号生成错误;准学号："+enrolleeInfo.getMatriculateNoticeNo());
					continue;
				}
				//更新学号map
				String qianzui = "";
				String liushuhao = "";
				if(("1").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 8){//教科8位
					qianzui = studyno.substring(0,2);
					liushuhao = studyno.substring(3,7);					
				}else if(("2").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 12){//广东医12位
					qianzui = studyno.substring(0,8);
					liushuhao = studyno.substring(9,12);//20171220：更改为13位					
				}else if(("3").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 12){//广大12位
					qianzui = studyno.substring(0,8);
					liushuhao = studyno.substring(9,11);					
				}else if(("4").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 10){//安徽医10位
					qianzui = studyno.substring(0,5);
					liushuhao = studyno.substring(6,9);					
				}else if(("5").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 12){//广西医12位
					qianzui = studyno.substring(0,7);
					liushuhao = studyno.substring(8,11);					
				}else if(("6").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 11){//广西桂林医11位
					qianzui = studyno.substring(0,7);
					liushuhao = studyno.substring(8,10);					
				}else if(("7").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 10){//广西右江医10位
					qianzui = studyno.substring(0,7);//java substr 下标由0开始,第二个参数是截取的下标
					liushuhao = studyno.substring(7,10);					
				}else if(("8").equals(studynoLevel)&& ExStringUtils.isNotBlank(studyno) && studyno.length() == 15){//广外15位
					qianzui = studyno.substring(0,11);
					liushuhao = studyno.substring(12,14);					
				}else if("9".equals(studynoLevel) && ExStringUtils.isNotBlank(studyno) && studyno.length() == 12){
					
				}
				maxStudentNoSuffixMap.put(qianzui, liushuhao);
				
			}else{
				stu.setStudyNo(enrolleeInfo.getMatriculateNoticeNo()); //用报名信息里的准学号作为学号
			}
			stu.setEnrolleeCode(enrolleeInfo.getEnrolleeCode());
			stu.setStudentName(enrolleeInfo.getStudentBaseInfo().getName());
			stu.setAttendAdvancedStudies("1"); //CodeAttendAdvancedStudies
			stu.setLearningStyle(enrolleeInfo.getTeachingType());			
			stu.setStudyInSchool("9"); //字典编码 CodeStudyInSchool
			stu.setTotalPoint(enrolleeInfo.getTotalPoint());//入学总分
			stu.setExamCertificateNo(enrolleeInfo.getExamCertificateNo());//准考证号
			if(enrolleeInfo.getIsApplyNoexam().equals(Constants.BOOLEAN_NO)){
				stu.setEnterSchool("11");// CodeEnterSchool
			}else{
				stu.setEnterSchool("22");					
			}
			stu.setLearingStatus("11"); //CodeLearingStatus
			stu.setStudentStatus("11"); //CodeStudentStatus
			//stu.setExamOrderStatus(Constants.BOOLEAN_TRUE);//CodeExamSubscribeState
			stu.setRegistorNo(enrolleeInfo.getRegistorNo());
			//~~~~专科信息
			stu.setGraduateSchool(enrolleeInfo.getGraduateSchool());//毕业学校
			stu.setGraduateSchoolCode(enrolleeInfo.getGraduateSchoolCode());//毕业学校代码
			stu.setGraduateMajor(enrolleeInfo.getGraduateMajor());//毕业专业
			stu.setGraduateId(enrolleeInfo.getGraduateId());//毕业证书编码
			stu.setGraduateDate(enrolleeInfo.getGraduateDate());//毕业时间
			stu.setIsStudyFollow(enrolleeInfo.getIsStudyFollow());// 是否跟读
			reginfo.setStudentInfo(stu);				
			regList.add(reginfo);
			// 新增注册流水号  以教学点年级为单位  20170510
			Map<String,Object> currentNO = enrolleeInfoService.getMaxRegistorSerialNO(enrolleeInfo.getGrade().getResourceid(),enrolleeInfo.getBranchSchool().getResourceid());
			enrolleeInfo.setRegistorSerialNO(String.valueOf((Integer.parseInt(currentNO.get("registorSerialNO").toString())+1)));
			enrolleeInfo.setRegistorFlag(Constants.BOOLEAN_YES);
			
			enrolleeList.add(enrolleeInfo);
		}//end for
		if(stuBaseList.size()>0){
			studentBaseInfoService.batchSaveOrUpdate(stuBaseList);
		}
		/*~~~~~~~~~~~~5.注册，传入报名信息列表和学生注册表 goto regStudentInfo()~~~~~~~~~~~*/
		 return regStudentInfo(enrolleeList, regList);
	}
	
	private String getStudyNo(EnrolleeInfo enrolleeInfo,Map<String, String> maxStudentNoSuffixMap,String studynoLevel){
		String studyNo = "";
		List<Dictionary> listdict = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where dictCode = 'studyno.replace.classiccode' ");
		if (("1").equals(studynoLevel)) {//教科
			studyNo = getStudentNo(enrolleeInfo, maxStudentNoSuffixMap);
		} else if (("2").equals(studynoLevel)) {//广东医
			studyNo = genStudentNo(enrolleeInfo, maxStudentNoSuffixMap);
		} else if (("3").equals(studynoLevel)) {//广大
			studyNo = genStudentNo2(enrolleeInfo, maxStudentNoSuffixMap);
		}else if(("4").equals(studynoLevel)){ //安徽医学院
			studyNo = genStudentAHY(enrolleeInfo, maxStudentNoSuffixMap,listdict);
		}else if(("5").equals(studynoLevel)){
			studyNo = genStudentGXYKD(enrolleeInfo, maxStudentNoSuffixMap,listdict);
		}else if(("6").equals(studynoLevel)){//广西桂林医
			studyNo = genStudentGXGLY(enrolleeInfo, maxStudentNoSuffixMap);
		}else if(("7").equals(studynoLevel)){//广西右江医
			studyNo = exameeInfoService.genStudentYJY(enrolleeInfo, maxStudentNoSuffixMap,listdict);
		}else if(("8").equals(studynoLevel)){//广外
			studyNo = genStudentGW(enrolleeInfo, maxStudentNoSuffixMap);
		}
		
		return studyNo;
	}
	
	/*
	 * 学校代码（5位）+教学点识别码（2位）+年级代码（2位）+层次代码（1位）+专业代码（2位）+学生顺序号（3位）。
	 */
	private String genStudentGW(EnrolleeInfo ei,Map<String, String> maxStudentNoSuffixMap) {
		String prefix = genStudentGWPrefix(ei);	
			
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);//更新为当前流水号
			return prefix + suffix;
		
	}
	
	private String genStudentGWPrefix(EnrolleeInfo ei) {
			String prefix = "";
			try {
				//1-5:11846
				prefix += "11846";
				//教学点
				prefix += ei.getBranchSchool().getUnitCode4StuNo();
				//年级
				prefix += ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2,4);
				//层次
				prefix += ei.getRecruitMajor().getClassic().getClassicCode4StudyNo();
				//专业
				prefix += ei.getRecruitMajor().getMajor().getMajorCode4StudyNo();
			
			} catch (Exception e) {
				logger.error("获取广外学号前缀错误;"+e.fillInStackTrace());
			}
			return prefix;
		}
	/*
	 * 广西桂林医
	 *   1      2,3     4,5		     6,7,8   9,10,11
	 * 成教代码            年份	   教学点及学院代码		   专业代码 	      流水号
	 */
	private String genStudentGXGLY(EnrolleeInfo ei,Map<String, String> maxStudentNoSuffixMap) {
		String prefix = genStudentGXGLYPrefix(ei);	
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if(ExStringUtils.isNotBlank(matriculateNoticeNo)){
			prefix_exist=matriculateNoticeNo.substring(0,8);
		}
		if(!prefix_exist.equals(prefix)){//前缀不同时
			//9-11位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);//更新为当前流水号
			return prefix + suffix;
		}else{
			return matriculateNoticeNo;
		}
	}

	//广西桂林医前缀
	private String genStudentGXGLYPrefix(EnrolleeInfo ei) {
		String prefix = "";
		try {
			//1 成教代码7
			prefix += "7";
			//2-3 年份
			prefix += ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2,4);
			//4-5教学点
			prefix += ei.getBranchSchool().getUnitCode();
			//6-9 专业
			prefix += ei.getRecruitMajor().getMajor().getMajorCode();
		
		} catch (Exception e) {
			logger.error("获取广西桂林医学号前缀错误;"+e.fillInStackTrace());
		}
		return prefix;
	}



	//广西医学院学号生成  共12位
	// 1	            2,3	  4	        5,6	  7,8	 9,10,11,12											
	//成教学号首字母A	年份	 培养层次 	专业	   教学点 	   顺序号
	public String genStudentGXYKD(EnrolleeInfo ei, Map<String, String> maxStudentNoSuffixMap,List<Dictionary> listdict) {
		String prefix = genStudentGXYKDPrefix(ei,listdict);	
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if(ExStringUtils.isNotBlank(matriculateNoticeNo)){
			prefix_exist=matriculateNoticeNo.substring(0,8);
		}
		if(!prefix_exist.equals(prefix)){//前缀不同时
			//9-12位流水号
			String suffix = "";
			String prefixKey = prefix.substring(0, 3) + prefix.substring(6, 8);
			if (!maxStudentNoSuffixMap.containsKey(prefixKey)) {
				suffix = "0000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefixKey);
				if ("9999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefixKey, suffix);//更新为当前流水号
			return prefix + suffix;
		}else{
			return matriculateNoticeNo;
		}
	}
	
	//广西医科大学号前缀
	private String genStudentGXYKDPrefix(EnrolleeInfo ei,List<Dictionary> listdict){
		String prefix = "";
		try {
			//1 成教学号首字母A
			prefix += "A";
			//2-3 年份
			prefix += ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2,4);
//			//4 培养层次
//			if(null != listdict && listdict.size() > 0){ //层次需要转换
//				prefix += JstlCustomFunction.dictionaryCode2Name("studyno.replace.classiccode",ei.getRecruitMajor().getClassic().getClassicCode());
//			}else{
//				prefix += ei.getRecruitMajor().getClassic().getClassicCode();
//			}
			//4-6 专业
			prefix += ei.getRecruitMajor().getMajor().getMajorCode();
			//7-8教学点
			prefix += ei.getBranchSchool().getUnitCode();			
		} catch (Exception e) {
			logger.error("获取安徽医学院学号前缀错误;"+e.fillInStackTrace());
		}
		return prefix;
	}
	
	//安徽医学院学号生成  共10位
	//15:年份 3：成人教育 1：层次 02:专业 0001:流水号
	public String genStudentAHY(EnrolleeInfo ei, Map<String, String> maxStudentNoSuffixMap,List<Dictionary> listdict) {
		String prefix = genStudentAHYPrefix(ei,listdict);	
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if(ExStringUtils.isNotBlank(matriculateNoticeNo)){
			prefix_exist=matriculateNoticeNo.substring(0,8);
		}
		if(!prefix_exist.equals(prefix)){//前缀不同时
			//7-10位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "0000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("9999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);//更新为当前流水号
			return prefix + suffix;
		}else{
			return matriculateNoticeNo;
		}
	}
	
	//安徽医学院学号前缀
	private String genStudentAHYPrefix(EnrolleeInfo ei,List<Dictionary> listdict){
		String prefix = "";
		try {
			//1-2 年份
			prefix += ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2,4);
			//3 编制
			try {
				prefix += CacheAppManager.getSysConfigurationByCode("school.studyno.compile").getParamValue(); 
			} catch (Exception e) {
				logger.error("获取安徽医学院学号编制单位错误（全局参数：school.studyno.compile）;"+e.fillInStackTrace());
			}
			//4 层次
			if(null != listdict && listdict.size() > 0){ //层次需要转换
				prefix += JstlCustomFunction.dictionaryCode2Name("studyno.replace.classiccode",ei.getRecruitMajor().getClassic().getClassicCode());
			}else{
				prefix += ei.getRecruitMajor().getClassic().getClassicCode();
			}
			//5-6 专业
			prefix += ei.getRecruitMajor().getMajor().getMajorCode();
		} catch (Exception e) {
			logger.error("获取安徽医学院学号前缀错误;"+e.fillInStackTrace());
		}
		return prefix;
	}
	
	public String genStudentNo2(EnrolleeInfo ei, Map<String, String> maxStudentNoSuffixMap) {
		//学号前9位
		String prefix = genStudentNoPrefixGZDX(ei);	
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if(ExStringUtils.isNotBlank(matriculateNoticeNo)){
			prefix_exist=matriculateNoticeNo.substring(0,9);
		}
		if(!prefix_exist.equals(prefix)){//前缀不同时
			//10-12位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);//更新为当前流水号
			return prefix + suffix;
		}else{
			return matriculateNoticeNo;
		}
	}
	
	public String genStudentNo(EnrolleeInfo ei, Map<String, String> maxStudentNoSuffixMap) {
		//学号前9位
		String prefix = genStudentNoPrefix(ei);	
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if(ExStringUtils.isNotBlank(matriculateNoticeNo)){
			prefix_exist=matriculateNoticeNo.substring(0,8);
		}
		if(!prefix_exist.equals(prefix)){//前缀不同时
		//10-12位流水号  20171220：更改为13位，即10-13位为流水号
		String suffix = "";
		if (!maxStudentNoSuffixMap.containsKey(prefix)) {
			suffix = "0000";
		} else {
			suffix = maxStudentNoSuffixMap.get(prefix);
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		suffix = increase(suffix);
		maxStudentNoSuffixMap.put(prefix, suffix);//更新为当前流水号
			return prefix + suffix;
		}else{
			return matriculateNoticeNo;
		}
	}
	
	public String genStudentNoPrefix(EnrolleeInfo ei) {
		String prefix = "";
		try {
			String genStudentType = JstlCustomFunction.getSysConfigurationValue("sys.default.genStudentType", "server");
			if("gdm".equals(genStudentType)){
				//第1-2位 年份（公元后两位），
				Long year = ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear() + ("2".equals(ei.getRecruitMajor().getRecruitPlan().getTerm()) ? 1L : 0L);
				prefix += String.valueOf(year).substring(2);
				//第3位 培养层次编号，
				String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
				prefix += "本科".equals(classicEnd)?"5":"6";
				//第4位 学习形式编号，
				prefix += "2";//需求只写了业余是2
				//第5-7位 专业编号，先按需求的对应关系 实在不行取系统的recruitmajorCode
				String majorCode ="";
				majorCode =  ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());
				prefix += majorCode;
				//第8-9位 教学站编号，
				String school = ei.getBranchSchool().getUnitCode();
				prefix += school.substring(school.length()-2);
			}
		} catch (Exception e) {
			//第1-2位 年份（公元后两位），
			Long year = ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear() + ("2".equals(ei.getRecruitMajor().getRecruitPlan().getTerm()) ? 1L : 0L);
			prefix += String.valueOf(year).substring(2);
			//第3位 培养层次编号，
			String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
			prefix += "本科".equals(classicEnd)?"5":"6";
			//第4位 学习形式编号，
			prefix += "2";//需求只写了业余是2
			//第5-7位 专业编号，先按需求的对应关系 实在不行取系统的recruitmajorCode
			//20171220:在表结构中增加 字段用于编学号，不再代码中写死
			String majorCode =ExStringUtils.isBlank(ei.getRecruitMajor().getMajor().getMajorCode4StudyNo())?"000":ei.getRecruitMajor().getMajor().getMajorCode4StudyNo();			

			
//			String majorName = ei.getRecruitMajor().getMajor().getMajorName();
//			String majorCode ="";
//			if("临床医学".equals(majorName)){majorCode = "301";}
//			else if("护理学".equals(majorName) || "护理".equals(majorName)){majorCode = "701";}
//			else if("医学检验".equals(majorName)){majorCode = "304";}
//			else if("医学影像学".equals(majorName)){majorCode = "303";}
//			else if("药学".equals(majorName)){majorCode = "801";}
//			else if("信息管理与信息系统".equals(majorName)){majorCode = "704";}
//			else if("医学检验技术".equals(majorName)){majorCode = "354";}
//			else if("医学影像技术".equals(majorName)){majorCode = "353";}
//			else if("口腔医学".equals(majorName) && "本科".equals(classicEnd)){majorCode = "401";}
//			else if("口腔医学".equals(majorName) && !"本科".equals(classicEnd)){majorCode = "406";}
//			else if("中药学".equals(majorName)){
//				majorCode = "802";
//			} else if ("中药".equals(majorName)){
//				majorCode = "302";
//			}else if(ExStringUtils.isEmpty(majorCode)){
//				majorCode = ei.getRecruitMajor().getRecruitMajorCode();
//				majorCode =  ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());
//			}
			prefix += majorCode;
			//第8-9位 教学站编号，
			String school = ei.getBranchSchool().getUnitCode();
			prefix += school.substring(school.length()-2);
		}finally{
			return prefix;
		}
	}
	
	public String genStudentNoPrefixGZDX(EnrolleeInfo ei) {
		String prefix = "";
		try {
//			String genStudentType = JstlCustomFunction.getSysConfigurationValue("sys.default.genStudentType", "server");
//			if("gdm".equals(genStudentType)){
				//第1-2位 年份（公元后两位），
				Long year = ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear() + ("2".equals(ei.getRecruitMajor().getRecruitPlan().getTerm()) ? 1L : 0L);
				prefix += String.valueOf(year).substring(2);
				//第3位 培养层次编号，
				String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
				prefix += "本科".equals(classicEnd)?"5":"6";
				//第4位 学习形式编号，
				prefix += "2".equals(ei.getTeachingType())?"2":"3";
				//第5-7位 专业编号，先按需求的对应关系 实在不行取系统的recruitmajorCode
				String majorCode =ei.getRecruitMajor().getMajor().getMajorCode();
//				majorCode =  ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());
				prefix += majorCode;
				//第8-9位 教学站编号，
				String school = ei.getBranchSchool().getUnitCode();
				prefix += school.substring(school.length()-2);
//			}
		} catch (Exception e) {
			//第1-2位 年份（公元后两位），
			Long year = ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear() + ("2".equals(ei.getRecruitMajor().getRecruitPlan().getTerm()) ? 1L : 0L);
			prefix += String.valueOf(year).substring(2);
			//第3位 培养层次编号，
			String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
			prefix += "本科".equals(classicEnd)?"5":"6";
			//第4位 学习形式编号，
			prefix += "2";//需求只写了业余是2
			//第5-7位 专业编号，先按需求的对应关系 实在不行取系统的recruitmajorCode
			//20171220:在表结构中增加 字段用于编学号，不再代码中写死
			String majorCode =ExStringUtils.isBlank(ei.getRecruitMajor().getMajor().getMajorCode4StudyNo())?"000":ei.getRecruitMajor().getMajor().getMajorCode4StudyNo();			

//			
//			String majorName = ei.getRecruitMajor().getMajor().getMajorName();
//			String majorCode ="";
//			if("临床医学".equals(majorName)){majorCode = "301";}
//			else if("护理学".equals(majorName) || "护理".equals(majorName)){majorCode = "701";}
//			else if("医学检验".equals(majorName)){majorCode = "304";}
//			else if("医学影像学".equals(majorName)){majorCode = "303";}
//			else if("药学".equals(majorName)){majorCode = "801";}
//			else if("信息管理与信息系统".equals(majorName)){majorCode = "704";}
//			else if("医学检验技术".equals(majorName)){majorCode = "354";}
//			else if("医学影像技术".equals(majorName)){majorCode = "353";}
//			else if("口腔医学".equals(majorName) && "本科".equals(classicEnd)){majorCode = "401";}
//			else if("口腔医学".equals(majorName) && !"本科".equals(classicEnd)){majorCode = "406";}
//			else if("中药学".equals(majorName)){
//				majorCode = "802";
//			} else if ("中药".equals(majorName)){
//				majorCode = "302";
//			}else if(ExStringUtils.isEmpty(majorCode)){
//				majorCode = ei.getRecruitMajor().getRecruitMajorCode();
//				majorCode =  ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());
//			}
			prefix += majorCode;
			//第8-9位 教学站编号，
			String school = ei.getBranchSchool().getUnitCode();
			prefix += school.substring(school.length()-2);
		}finally{
			return prefix;
		}
	}
	
	public String getStudentNo(EnrolleeInfo ei, Map<String, String> maxStudentNoSuffixMap) {
		//学号前3位
		String prefix = getStudentNoPrefix(ei);	
		String prefix_exist = "";
		String matriculateNoticeNo = ei.getMatriculateNoticeNo();
		if(ExStringUtils.isNotBlank(matriculateNoticeNo)){
			prefix_exist=matriculateNoticeNo.substring(0,3);
		}
		if(!prefix_exist.equals(prefix)){//前缀不同时
			//4-8位流水号
			String suffix = "";
			if (!maxStudentNoSuffixMap.containsKey(prefix)) {
				suffix = "00000";
			} else {
				suffix = maxStudentNoSuffixMap.get(prefix);
				if ("99999".equals(suffix)) {
					throw new ServiceException("没有空余的学号了！");
				}
			}
			suffix = increase(suffix);
			maxStudentNoSuffixMap.put(prefix, suffix);//更新为当前流水号
			return prefix + suffix;
		}else{
			return matriculateNoticeNo;
		}
	}
	
	public String increase(String num) {
		char[] chars = num.toCharArray();
		for (int i = num.length() - 1; i >= 0; i--) {
			if (chars[i] == '9') {
				chars[i] = '0';
			} else {
				chars[i]++;
				break;
			}
		}
		return new String(chars);
	}
	
	public String getStudentNoPrefix(EnrolleeInfo ei) {
		String prefix = "";
		try {
//			String genStudentType = JstlCustomFunction.getSysConfigurationValue("sys.default.genStudentType", "server");
//			if("gdm".equals(genStudentType)){
				//第1-2位 年份（公元后两位），
				String year = ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2, 4);
				prefix += year;
				//第3位 培养层次编号，
				//根据CCMD替换层次
				String classicEnd = ei.getRecruitMajor().getClassic().getEndPoint();
				//导入数据是否需要和国家专业去转换 判断是否设置数据字典admission.replace.majorcode
				List<Dictionary> listdict = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where dictCode = 'admission.replace.majorcode' ");
				if(null != listdict && listdict.size() > 0){ //需要替换
					String rpcode = JstlCustomFunction.dictionaryCode2Value("admission.replace.majorcode",ei.getCCMD());
					if(ExStringUtils.isNotBlank(rpcode)){
						prefix += rpcode;
					}else{
						prefix += "本科".equals(classicEnd)?"5":"6";
					}
					
				}else{
					prefix += "本科".equals(classicEnd)?"5":"6";
				}
//			}
		} catch (Exception e) {
		}finally{
			return prefix;
		}
	}
	
	
	//学生自动注册，用于定时调度
	@Override
	public Map<String, Object> autoRegisterStudent() throws ServiceException {
	Map<String, Object> condition = new HashMap<String, Object>();
		
		Grade grade = gradeService.findUnique("from "+Grade.class.getSimpleName()+" where isDefaultGrade = ?", Constants.BOOLEAN_YES);
		
		condition.put("grade", grade.getResourceid());
		
		int gradeYear = new Long(grade.getYearInfo().getFirstYear()).intValue();
		if("2".equals(grade.getTerm())){//如果为该年度第二学期，则取起始年份+1
			gradeYear  = gradeYear+1;
		}
		
		condition.put("gradeYear", gradeYear);
		condition.put("isMatriculate", Constants.BOOLEAN_YES);				
		List<EnrolleeInfo> registerList =  countRegisterStudent(condition);
		
		int  registeredNum = 0,setFirstTermCourseNum = 0;
		if(null != registerList && !registerList.isEmpty()){
			String[] regResourceIds = new String[registerList.size()];
			for(int i= 0;i<registerList.size();i++){
				EnrolleeInfo enrolleeInfo = (EnrolleeInfo)registerList.get(i);
				regResourceIds[i] = enrolleeInfo.getResourceid();					
			}
			List<StudentInfo> registeredStus = doRegister(regResourceIds);
			if(null != registeredStus && !registeredStus.isEmpty()) {
				registeredNum = registeredStus.size();
				//预约新生首学期课程						
				setFirstTermCourseNum = doRegisterStuFirstTermCourse(registeredStus);
			}			
		
			
		}
		condition.clear();
		condition.put("registeredNum", registeredNum);
		condition.put("setFirstTermCourseNum", setFirstTermCourseNum);
		return condition;
	}



	@Override
	@Transactional(readOnly=true)
	public Page findRegByCondition(Map<String, Object> condition, Page objPage) throws ServiceException{
		Map<String,Object> values =  new HashMap<String, Object>();
		StringBuffer bf = new StringBuffer(128);
		bf.append(" from "+Reginfo.class.getSimpleName()+" where isDeleted = :isDeleted ");		
		values.put("isDeleted", 0);
		
		if(condition.containsKey("branchSchool")){
			bf.append(" and studentInfo.branchSchool.resourceid = :branchSchool ");
			values.put("branchSchool", condition.get("branchSchool"));
		}
		if(condition.containsKey("major")){
			bf.append(" and studentInfo.major.resourceid = :major ");
			values.put("major", condition.get("major"));
		}
		if(condition.containsKey("classic")){
			bf.append(" and studentInfo.classic.resourceid = :classic ");
			values.put("classic", condition.get("classic"));
		}
		if(condition.containsKey("grade")){
			bf.append(" and studentInfo.grade.resourceid = :grade ");
			values.put("grade", condition.get("grade"));
		}
		if(condition.containsKey("stuStatus")){
			bf.append(" and studentInfo.studentStatus = :stuStatus ");
			values.put("stuStatus", condition.get("stuStatus"));
		}
		if(condition.containsKey("name")){
			bf.append( " and studentInfo.studentName like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("matriculateNoticeNo")){
			bf.append(" and studentInfo.studyNo like :studyNo ");
			values.put("studyNo", "%"+condition.get("matriculateNoticeNo")+"%");
		}
		if(condition.containsKey("studentStatus")){
			bf.append( " and studentInfo.studentStatus = :studentStatus ");
			values.put("studentStatus",condition.get("studentStatus"));
		}
		bf.append(" order by "+objPage.getOrderBy() +" "+ objPage.getOrder());
		bf.append(" ,studentInfo.branchSchool.unitCode ");
		
		return exGeneralHibernateDao.findByHql(objPage, bf.toString(), values);
	}
	
	
	
	@Override
	public List<Reginfo> findByHql(Map<String, Object> condition){
		Map<String,Object> values =  new HashMap<String, Object>();
		String hql = " from "+Reginfo.class.getSimpleName()+" where 1=1 ";
		hql += " and isDeleted =0 ";
	
		if(condition.containsKey("branchSchool")){
			hql += " and studentInfo.branchSchool.resourceid = '"+condition.get("branchSchool").toString()+"' ";
		}
		if(condition.containsKey("major")){
			hql += " and studentInfo.major.resourceid ='"+condition.get("major").toString()+"' ";
		}
		if(condition.containsKey("classic")){
			hql += " and studentInfo.classic.resourceid = '"+condition.get("classic")+"' ";
		}
		if(condition.containsKey("grade")){
			hql += " and studentInfo.grade.resourceid ='"+condition.get("grade")+"'";
		}
		if(condition.containsKey("stuStatus")){
			hql += " and studentInfo.studentStatus = '"+condition.get("stuStatus")+"'";
		}
		if(condition.containsKey("name")){
			hql += " and studentInfo.studentName like '%"+condition.get("name")+"%'";
		}
		if(condition.containsKey("matriculateNoticeNo")){
			hql += " and studentInfo.studyNo like '%"+condition.get("matriculateNoticeNo")+"%'";
		}
		if(condition.containsKey("studentStatus")){
			hql += " and studentInfo.studentStatus ="+condition.get("studentStatus");
		}
		hql += " order by resourceid";
		Session session=exGeneralHibernateDao.getSessionFactory().getCurrentSession();
		org.hibernate.Query query=session.createQuery(hql);
		return query.list();
	}
	
	@Override
	public List<ExportRecruitPlan> entranceFlagStudentExcel(Map<String, Object> condition){
		List<ExportRecruitPlan> list=new ArrayList<ExportRecruitPlan>();
		StringBuffer sql=new StringBuffer();
		sql.append("select enr.enrolleeCode,bstu.name,bstu.gender,bstu.bornDay,bstu.certNum,"+
				   "bstu.politics,bstu.nation,bmaj.majorNationCode,bmaj.majorName,"+
				   "clas.classicName,rec.resourceid,rec.yearid from "+
					"("+
					"select enrolleeCode,signupDate,recruitMajorId,STUDENTBASEINFOID from "+
					"edu_recruit_enrolleeInfo where entranceFlag='Y' and signupDate is not null order by signupDate desc "+
					") "+
					"enr "+
					"left join EDU_BASE_STUDENT bstu on bstu.resourceid=enr.STUDENTBASEINFOID "+
					"left join edu_recruit_major major on major.resourceid=enr.recruitMajorId "+
					"left join edu_base_major bmaj on bmaj.resourceid=major.majorId "+
					"left join edu_base_classic clas on clas.resourceid=major.classic  "+
					"left join edu_recruit_recruitplan rec on rec.resourceid=major.recruitplanId "+
					"where 1=1 ");
			if(condition.containsKey("yearRresourceid")){//年度
				sql.append(" and rec.yearid='"+condition.get("yearRresourceid")+"'");
			}
			Session session  =exGeneralHibernateDao.getSessionFactory().getCurrentSession();
			org.hibernate.Query query=session.createSQLQuery(sql.toString());
			List st=query.list();
			for(int i=0;i<st.size();i++){
				Object[] obj=(Object[])st.get(i);
				ExportRecruitPlan ep=new ExportRecruitPlan();
				ep.setEnrolleeCode(obj[0]!=null?obj[0].toString():"");
				ep.setName(obj[1]!=null?obj[1].toString():"");
				ep.setGender(obj[2]!=null?obj[2].toString():"");
				ep.setBornDay(obj[3]!=null?obj[3].toString():"");
				ep.setCertNum(obj[4]!=null?obj[4].toString():"");
				ep.setPolitics(obj[5]!=null?obj[5].toString():"");
				ep.setNation(obj[6]!=null?obj[6].toString():"");
				ep.setMajorNationCode(obj[7]!=null?obj[7].toString():"");
				ep.setMajorName(obj[8]!=null?obj[8].toString():"");
				ep.setClassicName(obj[9]!=null?obj[9].toString():"");
				list.add(ep);
			}
		return list;
	}
}
