package com.hnjk.edu.recruit.service.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.ExameeInfo;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IRecruitJDBCService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.security.model.OrgUnit;

/**
 * 学生报名服务实现
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-3-17 上午09:50:48
 * @see
 * @version 1.0
 */
@Transactional
@Service("enrolleeInfoService")
public class EnrolleeInfoServiceImpl extends BaseServiceImpl<EnrolleeInfo> implements IEnrolleeInfoService {
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("recruitJDBCService")
	private IRecruitJDBCService recruitJDBCService;

//	@Autowired
//	@Qualifier("examRoomPlanService")
//	private IExamRoomPlanService examRoomPlanService;

	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Override
	@Transactional(readOnly=true)
	public Page findEnrolleeInfoForRegisterList(Map<String, Object> condition,	Page objPage) throws ServiceException {
		StringBuffer sb = new StringBuffer(128);
		sb.append(" select en  from "+EnrolleeInfo.class.getSimpleName()+" en where en.isDeleted = 0 ");
//		condition.put("isDeleted", 0);
		sb.append(" and en.recruitMajor is not null ");
		sb.append(" and en.branchSchool is not null ");
//		sb.append(" and exists (from ExameeInfo e where e.isDeleted=0 and e.LQZY=en.recruitMajor.recruitMajorCode and e.SFZH=en.studentBaseInfo.certNum ");
		sb.append(" and exists (from ExameeInfo e where e.isDeleted=0 and e.SFZH=en.studentBaseInfo.certNum ");
		sb.append(" and e.ZKZH=en.examCertificateNo and e.KSH=en.enrolleeCode) ");
		if(condition.containsKey("branchSchool")){
			sb.append(" and  en.branchSchool.resourceid = :branchSchool");//学习中心
		}else{
			if (condition.containsKey("isDistribution")) {
				if ("Y".equals(condition.get("isDistribution"))) {
					sb.append(" and  en.branchSchool.resourceid not in (:branchSchoolResourceid)");//学习中心
				}else{
					sb.append(" and  en.branchSchool.resourceid = :branchSchoolResourceid");//学习中心
					
				}
			}
		}
		if(condition.containsKey("classic")) {
			sb.append(" and en.recruitMajor.classic.resourceid = :classic ");//层次
		}
		if(condition.containsKey("grade")) {
			sb.append(" and en.recruitMajor.recruitPlan.grade.resourceid=:grade ");	//年级
		}
		if(condition.containsKey("highClassic")) {
			sb.append(" and en.educationalLevel=:highClassic ");//入学前最高学历
		}
		if(condition.containsKey("stuName"))	{//学生姓名
			sb.append(" and en.studentBaseInfo.name like :name") ;
			condition.put("name", "%"+condition.get("stuName").toString()+"%");
		}
		
		if(condition.containsKey("matriculateNoticeNo")){
			sb.append(" and en.matriculateNoticeNo like :studyNo");//学号
			condition.put("studyNo", "%"+condition.get("matriculateNoticeNo").toString().trim()+"%");
		}
		if(condition.containsKey("isMatriculate")) {
			sb.append(" and en.isMatriculate = :isMatriculate ");			//是否录取
		}
		
		if(condition.containsKey("recruitPlan")) {
			sb.append(" and en.recruitMajor.recruitPlan.isDeleted=0 and en.recruitMajor.recruitPlan.resourceid = :recruitPlan "); //招生批次
		}
		if(condition.containsKey("major")) {
			sb.append(" and en.recruitMajor.major.isDeleted=0 and en.recruitMajor.major.resourceid = :major ");             //专业
		}
		if(condition.containsKey("examCertificateNo")) {
			sb.append(" and en.examCertificateNo = :examCertificateNo ");             //准考证号
		}
		if(condition.containsKey("certNum")) {
			sb.append(" and en.studentBaseInfo.isDeleted=0 and en.studentBaseInfo.certNum = :certNum ");                 //证件号
		}
		if(condition.containsKey("enrollType")) {
			sb.append(" and en.enrolleeType = :enrollType");                          //报名方式
		}
		if(condition.containsKey("teachingType")) {
			sb.append(" and en.teachingType =:teachingType");                         //办学模式
		}
		if(condition.containsKey("learningStyle")) {
			sb.append(" and en.stutyMode =:learningStyle  ");                         //学习方式
		}
		if(condition.containsKey("isApplyNoexam")) {
			sb.append(" and en.isApplyNoexam = :isApplyNoexam");                      //是否申请免试
		}
		if(condition.containsKey("isStudyFollow"))	{
			sb.append(" and en.isStudyFollow = :isStudyFollow");                      //是否跟读
		}

		sb.append(" and  (en.entranceflag ='Y' or (en.entranceflag ='Y' and en.noExamFlag='Y')) ");//通过入学资格审核的，或通过免试资格审核
		/*sb.append(" and  (en.entranceflag = :entranceflag1 or ( en.noExamFlag=:noExamFlag and en.entranceflag = :entranceflag2)) ");//通过入学资格审核的，或通过免试资格审核
		condition.put("entranceflag1", Constants.BOOLEAN_YES);
		condition.put("noExamFlag", Constants.BOOLEAN_YES);
		condition.put("entranceflag2", Constants.BOOLEAN_YES);*/
		
		//没有注册学籍 		
//		sb.append(" and not exists (");
//		sb.append(" select t.studyNo from "+StudentInfo.class.getSimpleName()+" t where t.isDeleted = :isDeleted ");
//		sb.append(" and t.studyNo = en.matriculateNoticeNo  ");
//		sb.append(")");
		sb.append("  and (en.registorFlag <> 'Y' OR en.registorFlag IS NULL) ");
		
		sb.append(" order by "+objPage.getOrderBy()+" "+ objPage.getOrder());
		sb.append(", en.branchSchool.unitCode,en.matriculateNoticeNo ");//按学习中心顺序
		return exGeneralHibernateDao.findByHql(objPage, sb.toString(), condition);
	}

	@Override
	public List<Map<String,Object>> findEnrolleeInfoForRegisterList(Map<String, Object> condition) throws ServiceException {
		StringBuffer sb = new StringBuffer("");
		sb.append(" select en.matriculateNoticeNo, en.enrolleeCode,en.examCertificateNo,stu.certNum,stu.name,stu.contactAddress, u.unitname,rm.RECRUITMAJORNAME ,ci.CLASSICNAME,stu.contactPhone ," +
				" stu.CONTACTADDRESS,en.noReportReason,en.isStudyFollow,en.memo from EDU_RECRUIT_ENROLLEEINFO en" +
				",hnjk_sys_unit u ,edu_recruit_major rm,edu_recruit_recruitplan rp ,edu_base_student stu,EDU_BASE_CLASSIC ci" +
				" where u.resourceid = en.branchschoolid and en.recruitmajorid = rm.resourceid and rm.recruitplanid = rp.resourceid " +
				"and stu.resourceid= en.studentbaseinfoid and en.isDeleted = :isDeleted  and rm.CLASSIC = ci.resourceid ");
		condition.put("isDeleted", 0);
		sb.append(" and en.recruitMajorid is not null ");
		sb.append(" and en.branchSchoolid is not null ");
		if(condition.containsKey("branchSchool")) {
			sb.append(" and  en.branchSchoolid = :branchSchool");//学习中心
		}
		if(condition.containsKey("classic")) {
			sb.append(" and ci.resourceid = :classic ");//层次
		}
		if(condition.containsKey("grade")) {
			sb.append(" and rp.gradeid=:grade ");	//年级
		}
		if(condition.containsKey("highClassic")) {
			sb.append(" and en.educationalLevel=:highClassic ");//入学前最高学历
		}
		if(condition.containsKey("stuName"))	{//学生姓名
			sb.append(" and stu.name like :name") ;
			condition.put("name", "%"+condition.get("stuName").toString()+"%");
		}
		
		if(condition.containsKey("matriculateNoticeNo")) {
			sb.append(" and en.matriculateNoticeNo = :matriculateNoticeNo");//学号
		}
		if(condition.containsKey("isMatriculate")) {
			sb.append(" and en.isMatriculate = :isMatriculate ");			//是否录取
		}
		
		if(condition.containsKey("recruitPlan")) {
			sb.append(" and rp.resourceid = :recruitPlan "); //招生批次
		}
		if(condition.containsKey("major")) {
			sb.append(" and rm.majorid = :major ");             //专业
		}
		if(condition.containsKey("examCertificateNo")) {
			sb.append(" and en.examCertificateNo = :examCertificateNo ");             //准考证号
		}
		if(condition.containsKey("certNum")) {
			sb.append(" and stu.certNum = :certNum ");                 //证件号
		}
		if(condition.containsKey("enrollType")) {
			sb.append(" and en.enrolleeType = :enrollType");                          //报名方式
		}
		if(condition.containsKey("teachingType")) {
			sb.append(" and en.teachingType =:teachingType");                         //办学模式
		}
		if(condition.containsKey("learningStyle")) {
			sb.append(" and en.stutyMode =:learningStyle  ");                         //学习方式
		}
		if(condition.containsKey("isApplyNoexam")) {
			sb.append(" and en.isApplyNoexam = :isApplyNoexam");                      //是否申请免试
		}
		if(condition.containsKey("isStudyFollow"))	{
			sb.append(" and en.isStudyFollow = :isStudyFollow");                      //是否跟读
		}

		sb.append(" and  (en.entranceflag = :entranceflag1 or ( en.noExamFlag=:noExamFlag and en.entranceflag = :entranceflag2)) ");//通过入学资格审核的，或通过免试资格审核
		condition.put("entranceflag1", Constants.BOOLEAN_YES);
		condition.put("noExamFlag", Constants.BOOLEAN_YES);
		condition.put("entranceflag2", Constants.BOOLEAN_YES);
		
		
		
		//没有注册学籍 	
		sb.append("and (en.REGISTER_FLAG <> 'Y' OR en.REGISTER_FLAG IS NULL)");
		sb.append(" and not exists (");
		sb.append(" select t.studyNo from edu_roll_studentinfo t where t.isDeleted = :isDeleted ");
		sb.append(" and t.studyNo = en.matriculateNoticeNo ");
		sb.append(")");
		
//		sb.append(" order by rp.startdate  desc,u.unitcode,en.matriculateNoticeNo ");
		sb.append(" order by ");
		if(condition.containsKey("type")) {
			sb.append("matriculateNoticeNo,");
		}
		sb.append("en.branchschoolid,en.recruitmajorid,F_TRANS_PINYIN_CAPITAL(stu.name)");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnList;
	}
	@Override
	@Transactional(readOnly=true)
	public Page findEnrolleeByCondition(Map<String, Object> condition, Page page)throws ServiceException {
		String hql = createHqlByCondition(condition);
		return exGeneralHibernateDao.findByHql(page, hql.toString(), condition);
	}

	
	@Override
	@Transactional(readOnly=true)
	public List findListByCondition(Map<String, Object> condition)	throws ServiceException {		
		String sql = createHqlByCondition(condition);
		condition.remove("orderBy");
		condition.remove("orderType");
		condition.remove("hasEnrollNO");
		condition.remove("registorFlag1");
		return exGeneralHibernateDao.findByHql(sql,condition);
	}

	private String createHqlByCondition(Map<String, Object> condition) {
		StringBuffer hql = new StringBuffer("from "+EnrolleeInfo.class.getSimpleName()+" ei where ei.isDeleted =:isDeleted ");
		condition.put("isDeleted", 0);
//		if(condition.containsKey("kszt")||condition.containsKey("isExistsPhoto")){
//			hql.append(" and exists (from "+ExameeInfo.class.getSimpleName()+" ee where ee.isDeleted =:isDeleted and ee.ZKZH = ei.examCertificateNo ");
//				if(condition.containsKey("kszt")){//考生状态
//					hql.append(" and ee.KSZT=? ");
//				}
//				if(condition.containsKey("isExistsPhoto")){//是否存在照片
//					if(Constants.BOOLEAN_YES.equals(condition.get("isExistsPhoto"))){
//						hql.append(" and ee.mainPhotoPath is not null ");
//					} else {
//						hql.append(" and ee.mainPhotoPath is null ");
//					}
//					hql.append(" and :isExistsPhoto=:isExistsPhoto  ");
//				}
//			
//			hql.append(" )");
//		}
		if(condition.containsKey("resourceIds")){// resourceids
			hql.append(" and ei.resourceid in (:resourceIds) ");
		}
		if(condition.containsKey("enrolleeCode")){//考生号
			hql.append(" and ei.enrolleeCode=:enrolleeCode ");
		}
		// 学习中心
		if (condition.containsKey("branchSchool")) {
			hql.append(" and ei.branchSchool.resourceid = :branchSchool ");
		}
		// 招生批次或招生批次的年度 by hzg
		if (condition.containsKey("recruitPlan")) {
			//hql.append(" and (ei.recruitMajor.recruitPlan.resourceid = :recruitPlan or ei.recruitMajor.recruitPlan.yearInfo.resourceid = :recruitPlan) ");
			hql.append(" and (ei.recruitMajor.recruitPlan.resourceid = :recruitPlan or ei.grade.yearInfo.resourceid = :recruitPlan) ");
		}
		if (condition.containsKey("recruitPlanId")) {
			hql.append(" and ei.recruitMajor.recruitPlan.resourceid = :recruitPlanId");
		}
		if (condition.containsKey("grade")) {
			hql.append(" and ei.recruitMajor.recruitPlan.grade.resourceid=:grade");
		}
		// 招生专业
		if (condition.containsKey("recruitMajor")) {
			hql.append(" and ei.recruitMajor.resourceid = :recruitMajor ");
		}
		// 专业
		if (condition.containsKey("major")) {
			hql.append(" and ei.recruitMajor.major.resourceid = :major ");
		}
		// 层次
		if (condition.containsKey("classic")) {
			hql.append(" and ei.recruitMajor.classic.resourceid = :classic ");
		}
		// 年级
		if (condition.containsKey("grade2")) {
			hql.append(" and ei.recruitMajor.recruitPlan.grade.resourceid = :grade2 ");
		}
		// 高学历层次
		if (condition.containsKey("highClassic")) {
			hql.append(" and ei.educationalLevel = :highClassic ");
		}
		// 姓名
		if (condition.containsKey("name")) {
			hql.append(" and ei.studentBaseInfo.name like :name");
			condition.put("name", "%"+condition.get("name").toString()+"%");
		}
		// 学号
		if (condition.containsKey("matriculateNoticeNo")) {
			hql.append(" and ei.matriculateNoticeNo = :matriculateNoticeNo ");
		}
		// 准考证号
		if (condition.containsKey("examCertificateNo")) {
			hql.append(" and ei.examCertificateNo = :examCertificateNo ");
		}
		// 证件号码
		if (condition.containsKey("certNum")) {
			hql.append(" and ei.studentBaseInfo.certNum = :certNum ");
		}
		// 入学资格审核标志
		if (condition.containsKey("entranceFlag")) {
			hql.append(condition.get("entranceFlag"));
		}
		// 录取状态
		if (condition.containsKey("isMatriculate")) {
			hql.append(" and ei.isMatriculate = :isMatriculate");
		}
		// 报名资格审核标志
		if (condition.containsKey("signupFlag")) {
			hql.append(" and ei.signupFlag = :signupFlag");
		}
		//办学模式
		if (condition.containsKey("teachingType")) {
			hql.append(" and ei.teachingType =:teachingType");
		}
		// 报名类型
		if (condition.containsKey("enrollType")) {
			hql.append(" and ei.enrolleeType = :enrollType");
		}
		// 注册状态
		if (condition.containsKey("registorFlag")) {
			hql.append(" and ei.registorFlag is null");
		}
		
		if(condition.containsKey("registorFlag1")){
			if("N".equals(condition.get("registorFlag1").toString())){
				hql.append(" and ei.isDeleted = 0 AND (ei.registorFlag is null or ei.registorFlag='N')");
			} else if("Y".equals(condition.get("registorFlag1").toString())) {
				hql.append(" and ei.isDeleted = 0 AND ei.registorFlag = 'Y' ");
			}	
			
//			if("Y".equals(condition.get("registorFlag").toString())){
//				sql.append("and ei.MATRICULATENOTICENO not exists (select t.studyNo from edu_roll_studentinfo t join EDU_RECRUIT_ENROLLEEINFO info on t.studyNo = info.matriculateNoticeNo and t.isDeleted =0)");
//			} 
		}
		//是否申请免试
		if (condition.containsKey("isApplyNoexam")) {
			hql.append(" and ei.isApplyNoexam = :isApplyNoexam");
		}
		//免试资格审核标志
		if (condition.containsKey("noExamFlag")) {
			hql.append(condition.get("noExamFlag"));
		}
		//是否机考
		if (condition.containsKey("isMachineExam")) {
			if ("Y".equals(condition.get("isMachineExam"))) {
				hql.append(" and ei.branchSchool.isMachineExam =:isMachineExam  ");
			}else {
				hql.append(" and (ei.branchSchool.isMachineExam ='N' or  ei.branchSchool.isMachineExam is null) ");
			}
		}
		//学习形式
		if (condition.containsKey("learningStyle")) {
			hql.append(" and ei.stutyMode =:learningStyle  ");
		}
		//专升本文科,专升本理科...
		if (condition.containsKey("courseGroup")) {
			hql.append(" and ei.courseGroupName =:courseGroup");
		}
		if (condition.containsKey("isExistsPhoto")){//是否存在照片
			if(Constants.BOOLEAN_YES.equals(condition.get("isExistsPhoto"))){
				hql.append(" and ei.studentBaseInfo.photoPath is not null ");
			} else {
				hql.append(" and ei.studentBaseInfo.photoPath is null ");
			}			
		}
		// 是否已经生成录取编号
		if(condition.containsKey("hasEnrollNO")){
			if(Constants.BOOLEAN_YES.equals(condition.get("hasEnrollNO"))){
				hql.append(" and ei.enrollNO is not null ");
			} else {
				hql.append(" and ei.enrollNO is null ");
			}
		}
		
		if(condition.containsKey("orderBy") && condition.containsKey("orderType")){
			if(ExStringUtils.isNotEmpty(condition.get("orderBy").toString())&& ExStringUtils.isNotEmpty(condition.get("orderType").toString()) ){
				hql.append(condition.get("orderBy")+" "+condition.get("orderType"));
			}else{
				hql.append(condition.get("orderBy"));
			}
		}else {
			hql.append(" order by ei.recruitMajor.recruitPlan.startDate desc,ei.branchSchool.unitCode");
		}		

		return hql.toString();
	}
	
	
	@Override
	public String genStudentid(EnrolleeInfo ei) {
		// 获得学号前12位
		String prefix = getStudentidPrefix(ei);
		// 获得学号后4位流水号
		String suffix = getStudentidSuffix(prefix);

		return prefix + suffix;
	}

	
	@Override
	public String getStudentidPrefix(EnrolleeInfo ei) {
		// 1-4位招生计划年度
		String prefix = String.valueOf(ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear());
		// 第5位是季度（春-0，秋-1）
		if ("1".equals(ei.getRecruitMajor().getRecruitPlan().getTerm())) {
			prefix += "1";
		} else {
			//当招生批次所在学期是一个年度中第二个学期时,年份加一
			prefix  = String.valueOf(Integer.valueOf(prefix)+1);
			prefix += "0";
		}
		// 6-8位是学习中心编号
		prefix += ei.getBranchSchool().getUnitCode();
		// 第9位是层次代码
		prefix += ei.getRecruitMajor().getClassic().getClassicCode();
		// 10-11是专业编号
		prefix += ei.getRecruitMajor().getMajor().getMajorCode();
		//如果专业编号长度小于等于2的情况下第12位是学习形式
		if (ExStringUtils.isNotBlank(ei.getRecruitMajor().getMajor().getMajorCode())&&ei.getRecruitMajor().getMajor().getMajorCode().length()<=2) {
			// 12位是学习形式
			prefix += ei.getStutyMode();
		}
		return prefix;
	}

	
	@Override
	@Transactional(readOnly=true)
	public String getStudentidSuffix(String prefix) {
		String suffix = "";
		List result = findByHql("from "+EnrolleeInfo.class.getSimpleName()+" ei where  ei.isDeleted ='0'  and ei.matriculateNoticeNo like '" + prefix + "___' order by ei.matriculateNoticeNo desc ");
		if (result.size() == 0) {
			suffix = "0000";
		} else {
			EnrolleeInfo ei = (EnrolleeInfo) result.get(0);
			suffix = ei.getMatriculateNoticeNo().substring(prefix.length());
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		return ExStringUtils.increase(suffix);
	}

	
	@Override
	public String genExamCertificateNo(EnrolleeInfo ei) {
		// 获得准考证号前11位
		String prefix = getExamCertificateNoPrefix(ei);
		// 获得准考证后3位流水号
		String suffix = getExamCertificateNoSuffix(prefix);
		return prefix + suffix;
	}

	
	@Override
	public String getExamCertificateNoPrefix(EnrolleeInfo ei) {
		// 1-4位招生计划年度
		String prefix = String.valueOf(ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear());
		// 第5位是季度（春-0，秋-1）
		if ("1".equals(ei.getRecruitMajor().getRecruitPlan().getTerm())) {
			prefix += "1";
		} else {
			//当招生批次所在学期是一个年度中第二个学期时,年份加一
			prefix  = String.valueOf(Integer.valueOf(prefix)+1);
			prefix += "0";
		}
		// 6-8位是学习中心编号
		prefix += ei.getBranchSchool().getUnitCode();
		// 第9位是层次代码
		prefix += ei.getRecruitMajor().getClassic().getClassicCode();
		// 10-11是专业编号
		prefix += ei.getRecruitMajor().getMajor().getMajorCode();

		return prefix;
	}

	
	@Override
	@Transactional(readOnly=true)
	public String getExamCertificateNoSuffix(String prefix) {
		String suffix = "";
		List result = exGeneralHibernateDao.findByHql("from "+EnrolleeInfo.class.getSimpleName()+" ei where  ei.isDeleted =0 and ei.examCertificateNo like '" + prefix + "___' order by ei.examCertificateNo desc");
		if (result.size() == 0) {
			suffix = "000";
		} else {
			EnrolleeInfo ei = (EnrolleeInfo) result.get(0);
			suffix = ei.getExamCertificateNo().substring(prefix.length());
			if ("999".equals(suffix)) {
				throw new ServiceException("没有空余的准考证号了！");
			}
		}
		return ExStringUtils.increase(suffix);
	}

	
	@Override
	public String genExamCertificateNoPrefix(RecruitMajor recruitMajor,	OrgUnit branchSchool) {
		// 1-4位招生计划年度
		String prefix = String.valueOf(recruitMajor.getRecruitPlan().getYearInfo().getFirstYear());
		// 第5位是季度（春-0，秋-1）
		if ("1".equals(recruitMajor.getRecruitPlan().getTerm())) {
			prefix += "1";
		} else {
			//当招生批次所在学期是一个年度中第二个学期时,年份加一
			prefix  = String.valueOf(Integer.valueOf(prefix)+1);
			prefix += "0";
		}

		// 6-8位是学习中心编号
		prefix += branchSchool.getUnitCode();
		// 第9位是层次代码
		prefix += recruitMajor.getClassic().getClassicCode();
		// 10-11是专业编号
		prefix += recruitMajor.getMajor().getMajorCode();

		return prefix;
	}

	
	@Override
	public String genEnrolleeCode(EnrolleeInfo ei) {
		// 获得考生号前11位前缀
		String prefix = genEnrolleeCodePrefix(ei);
		// 获得考生号后4位流水号
		String suffix = genEnrolleeCodeSuffix(prefix);

		return prefix + suffix;
	}

	
	@Override
	public String genEnrolleeCodePrefix(EnrolleeInfo ei) {
		// 1-2位年度
		RecruitMajor major      = ei.getRecruitMajor();
		String recruitPlanID    = major.getRecruitPlan().getResourceid();
		RecruitPlan recruitPlan = recruitPlanService.get(recruitPlanID);
		String prefix           = recruitPlan.getYearInfo().getFirstYear().toString().substring(2);
		//当招生批次所在学期是一个年度中第二个学期时,年份加一
		if ("2".equals(ei.getRecruitMajor().getRecruitPlan().getTerm())) {
			prefix  = String.valueOf(Integer.valueOf(prefix)+1);
		}
		// 3-9位是固定字符"W110571"
		prefix     += "W110571";
		// 10-11位是季度（春-10，秋-11）
		if ("1".equals(ei.getRecruitMajor().getRecruitPlan().getTerm())) {
			prefix += "11";
		} else {
			prefix += "10";
		}
		return prefix;
	}

	@Override
	public String genEnrolleeCodeSuffix(String prefix) {
		String suffix       = "";
		/*String hql = " from EnrolleeInfo ei where  ei.isDeleted ='0' "
				+ " and ei.enrolleeCode like '" + prefix + "____'"
				+ " order by ei.enrolleeCode desc ";

		List result = exGeneralHibernateDao.findByHql(hql, null);*/
		String maxStudentId = recruitJDBCService.getMaxStudentid(prefix);
		if (null==maxStudentId  || "".equals(maxStudentId)) {
			suffix = "0000";
		} else {
			//EnrolleeInfo ei = (EnrolleeInfo) result.get(0);
			suffix = maxStudentId.substring(prefix.length());
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的考生号了！");
			}
		}
		return ExStringUtils.increase(suffix);
	}

	
	@Override
	public String genEnrolleeRegistorNo(EnrolleeInfo ei) {
		String season = ei.getRecruitMajor().getRecruitPlan().getTerm();
		String prefix = genEnrolleeRegistorNoPrefix(ei);
		String suffix = genEnrolleeRegistorNoSuffix(season);
		
		while(isContainsRegistorNo(prefix+ suffix))	{
			suffix = genEnrolleeRegistorNoSuffix(season);
		}
		return prefix+ suffix;
	}

	
	@Override
	public String genEnrolleeRegistorNoPrefix(EnrolleeInfo ei) {
		String prefix = ei.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear().toString().substring(2);
		String classic=ei.getRecruitMajor().getClassic().getShortName();
		
		//当招生批次所在学期是一个年度中第二个学期时,年份加一
		if ("2".equals(ei.getRecruitMajor().getRecruitPlan().getTerm())) {
			prefix  = String.valueOf(Integer.valueOf(prefix)+1);
		}
		
		prefix += "W1";
		if("高起本".equals(classic)) {
			prefix += "1";
		} else if("高起专".equals(classic)) {
			prefix += "2";
		} else if("专升本".equals(classic)) {
			prefix += "3";
		} else if("专业硕士".equals(classic)) {
			prefix += "4";
		} else if("本科第二学位".equals(classic)) {
			prefix += "5";
		} else if("研究生课程进修".equals(classic)) {
			prefix += "6";
		} else {
			prefix += "7";
		}
		
		prefix += "10571";
		return prefix;
	}

	
	@Override
	public String genEnrolleeRegistorNoSuffix(String season) {
		String suffix="";
		if(!"1".equals(season)) {
			season="0";
		}
		for(int i = 0 ; i<8 ; i++ ){
			suffix += (int)(Math.random()*10);
		}
		
		return suffix.substring(0,3)+season+suffix.substring(4);
	}

	
	@Override
	public boolean isContainsRegistorNo(String registorNo) {		
		return findByCriteria(Restrictions.eq("registorNo", registorNo)).size()>0;
	}

	@Override
	public void delete(EnrolleeInfo entity) throws ServiceException {
		//signupFlag(报名资格审核) entranceflag(入学审核) matriculateNoticeNo(录取-学号)
		if(entity.getSignupFlag().equals(Constants.BOOLEAN_YES)
				|| entity.getEntranceflag().equals(Constants.BOOLEAN_YES)
				|| ExStringUtils.isNotEmpty(entity.getMatriculateNoticeNo())){
			throw new ServiceException("学生:"+entity.getStudentBaseInfo().getName()+" 已通过审核，不能删除！");
		}
		
		StudentBaseInfo info  = entity.getStudentBaseInfo();
		String stuId          = entity.getStudentBaseInfo().getResourceid();
		studentService.clearCache(info);
		StudentBaseInfo info2 = studentService.get(stuId);
	
		entity.setMatriculateNoticeNo(null);
		entity.setExamCertificateNo(null);//准考证号置空
		super.delete(entity);

		//如果报名信息未关联学籍和其他报名信息，则删除所关联的基本信息
		List<StudentInfo> stu = studentinfoservice.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentBaseInfo", info));
		List<EnrolleeInfo> list = this.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studentBaseInfo",  info),Restrictions.ne("resourceid", entity.getResourceid()));
		
		super.delete(entity);
		if (stu.size()==0&&list.size()==0) {
			studentService.delete(info2);
		}
		
	}

	@Override
	public void batchDelete(String[] ids) throws ServiceException {
		if(null != ids && ids.length>0){
			for(int i=0;i<ids.length;i++){
				EnrolleeInfo entity  = this.get(ids[i]);
				
				boolean isAllow      = true;
				if (isAllow) {
					delete(get(ids[i]));
				}else {
					throw new ServiceException(entity.getStudentBaseInfo().getName()+",不允许删除!");
				}	
			}
		}
	}
	
	private Map<String,Object> genExamCertificateNOSuffix2(String prefix){
		Map<String,Object> resultsMap = new HashMap<String, Object>();
		String suffix = "";
		List result   = exGeneralHibernateDao.findByHql("from "+EnrolleeInfo.class.getSimpleName()+" ei where  ei.isDeleted ='0' and ei.examCertificateNo like '" + prefix + "___' order by ei.examCertificateNo desc");
		if (result.size() == 0) {
			suffix = "000";
		} else {
			EnrolleeInfo ei = (EnrolleeInfo) result.get(0);
			suffix = ei.getExamCertificateNo().substring(prefix.length());
			if ("999".equals(suffix)) {
				throw new ServiceException("没有空余的准考证号了！");
			}
			resultsMap.put("maxExamCertificateNoObj",ei);
		}
		resultsMap.put("suffix", ExStringUtils.increase(suffix)) ;
		return resultsMap;
	}
	


	@Override
	public void saveOrUpdateEnrolleeinfo(StudentBaseInfo sbi, EnrolleeInfo ei)throws ServiceException{
	 	
		studentService.saveOrUpdate(sbi);
		ei.setStudentBaseInfo(sbi);
		this.saveOrUpdate(ei);
		
		
	}

	@Override
	public void batchSaveOrUpdateEnrolleeInfo(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException {
		List<StudentBaseInfo> studentBaseInfoList = new ArrayList<StudentBaseInfo>();
		for (EnrolleeInfo ei : enrolleeInfoList) {
			studentBaseInfoList.add(ei.getStudentBaseInfo());
		}
		exGeneralHibernateDao.batchSaveOrUpdate(studentBaseInfoList);
		batchSaveOrUpdate(enrolleeInfoList);
	}
	
	@Override
	public List<Map<String,Object>> genMaxStudyNoPrefixMap(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select s.prefix p,max(s.suffix) s from ( ");
		sql.append(" 		select substr(t.matriculatenoticeno,0,9) prefix ,substr(t.matriculatenoticeno,10,12) suffix ,t.matriculatenoticeno ");
		sql.append(" 		from edu_recruit_enrolleeinfo t where length(t.matriculatenoticeno) = 12 ");
		sql.append(" 		) s ");
		sql.append(" group by s.prefix ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return returnList;
	}
	
	@Override
	public List<Map<String,Object>> genMaxStudyNoPrefixMap2(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select s.prefix p,max(s.suffix) s from ( ");
		sql.append(" 		select substr(t.matriculatenoticeno,0,9) prefix ,substr(t.matriculatenoticeno,10,12) suffix ,t.matriculatenoticeno ");
		sql.append(" 		from edu_recruit_enrolleeinfo t where length(t.matriculatenoticeno) = 12 ");
		sql.append(" 		) s ");
		sql.append(" group by s.prefix ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return returnList;
	}
	
	@Override
	public List<Map<String,Object>> genMaxStudyNoPrefixAHY() throws ServiceException{
		StringBuffer sql = new StringBuffer("");
		sql.append(" select s.prefix p,max(s.suffix) s from ( ");
		sql.append(" 		select substr(t.matriculatenoticeno,0,6) prefix ,substr(t.matriculatenoticeno,7,10) suffix ,t.matriculatenoticeno ");
		sql.append(" 		from edu_recruit_enrolleeinfo t where length(t.matriculatenoticeno) = 10 ");
		sql.append(" 		) s ");
		sql.append(" group by s.prefix ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return returnList;
	}
	
	@Override
	public List<Map<String,Object>> getMaxStudyNoPrefixMap(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select s.prefix p,max(s.suffix) s from ( ");
		sql.append(" 		select substr(t.matriculatenoticeno,0,3) prefix ,substr(t.matriculatenoticeno,4,8) suffix ,t.matriculatenoticeno ");
		sql.append(" 		from edu_recruit_enrolleeinfo t where length(t.matriculatenoticeno) = 8 ");
		sql.append(" 		) s ");
		sql.append(" group by s.prefix ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return returnList;
	}
	/**
	 * 获取当前数据库中  以教学点、年级 为单位的最大注册流水号
	 * @param gradeid
	 * @param unitid
	 * @return 当前最大的流水号
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> getMaxRegistorSerialNO(String gradeid, String unitid) throws ServiceException{
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("gradeid", gradeid);
		param.put("unitid", unitid);
		StringBuffer sb = new StringBuffer();
		sb.append(" select max(e.registorSerialNO) as registorSerialNO from EDU_RECRUIT_ENROLLEEINFO e where e.grade='"+gradeid+"' and e.branchschoolid='"+unitid+"' and e.isdeleted = 0 and e.registorSerialNO is not null group by e.grade,e.branchschoolid");
//		String hql = "select max(registorSerialNO),e from "+EnrolleeInfo.class.getSimpleName()+" e where e.grade.resourceid=:gradeid and e.branchSchool.resourceid=:unitid group by e.grade.resourceid,e.branchSchool.resourceid,e.registorSerialNO";
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Map<String, Object>> maps = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sb.toString(), map);
			if(maps!=null && maps.size()>0) {
				map = maps.get(0);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(map==null||map.size()==0){
			map.put("registorSerialNO", "0");
			return map;
		}else{
			return map;
		}
		
	}
	
	@Override
	public List<Map<String, Object>> getMaxStudyNoPrefixMapRegistered(int a,int b,int c,int d) throws ServiceException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select s.prefix p,max(s.suffix) s from ( ");
		sql.append(" 		select substr(t.studyno,"+a+","+b+") prefix ,substr(t.studyno,"+c+","+d+") suffix ,t.studyno ");
		sql.append(" 		from edu_roll_studentinfo t where length(t.studyno) = "+d+" ");
		sql.append(" 		) s ");
		sql.append(" group by s.prefix ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
		} catch (Exception e) {
			e.fillInStackTrace();
		}
		return returnList;
	}
	
	@Override
	public List<Map<String, Object>> getMaxStudyNoPrefixMapRegistered2(int a,int b,int c,int d,int e,int f) throws ServiceException {
		StringBuffer sql = new StringBuffer("");
		sql.append(" select s.prefix p,max(s.suffix) s from ( ");
		sql.append(" 		select substr(t.studyno,"+a+","+b+")||substr(t.studyno,"+c+","+d+") prefix ,substr(t.studyno,"+e+","+f+") suffix ,t.studyno ");
		sql.append(" 		from edu_roll_studentinfo t where length(t.studyno) = "+f+" ");
		sql.append(" 		) s ");
		sql.append(" group by s.prefix ");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
		} catch (Exception ex) {
			ex.fillInStackTrace();
		}
		return returnList;
	}
	
	@Override
	public String[] orderByPinyinHeadChar(String[] enrolleeInfoIds) throws ServiceException {
		if (enrolleeInfoIds == null || enrolleeInfoIds.length == 0) {
			return null;
		}
		StringBuffer ids = new StringBuffer();
		for (String id : enrolleeInfoIds) {
			if (ids.length() > 0) {
				ids.append(",");
			}
			ids.append("'").append(id).append("'");
		}
		StringBuffer sql = new StringBuffer();
		sql.append("select ei.resourceid ");
//		sql.append(",ei.branchschoolid,ei.recruitmajorid,s.name,F_TRANS_PINYIN_CAPITAL(s.name) ");
		sql.append(" from EDU_RECRUIT_ENROLLEEINFO ei join EDU_BASE_STUDENT s on ei.studentbaseinfoid=s.resourceid ");
		sql.append(" where ei.resourceid in (").append(ids.toString()).append(") ");
		sql.append(" order by ei.branchschoolid,ei.recruitmajorid,F_TRANS_PINYIN_CAPITAL(s.name)");
		List<Map<String,Object>> returnList = new ArrayList<Map<String,Object>>(0);
		try {
			returnList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), null);
			enrolleeInfoIds = new String[returnList.size()];
			for (int i = 0; i < returnList.size(); i++) {
				Map r = returnList.get(i);
				enrolleeInfoIds[i] = r.get("resourceid").toString();
			}
		} catch (Exception ex) {
			ex.fillInStackTrace();
		}
		return enrolleeInfoIds;
	}

	/**
	 * 获取某招生计划最大的录取编号
	 * @param recruitPlanId
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Integer getMaxEnrollNO(String recruitPlanId) throws ServiceException {
		int maxEnrollNO = 0;
		Map<String, Object> paramMap = new HashMap<String, Object>();
		try {
			paramMap.put("recruitPlanId", recruitPlanId);
			String sql = "select max(eo.enrollno) maxEnrollNO from edu_recruit_enrolleeinfo eo,edu_recruit_major rm "
					+"where eo.recruitmajorid=rm.resourceid and rm.isdeleted=0 and eo.isdeleted=0 and rm.recruitplanid=:recruitPlanId ";
			Map<String, String> maxEnrollNOMap = baseSupportJdbcDao.getBaseJdbcTemplate().findForMap(sql, paramMap);
			if(maxEnrollNOMap != null && maxEnrollNOMap.size() >0){
				String maxEnrollNOStr = maxEnrollNOMap.get("maxEnrollNO");
				if(ExStringUtils.isNotEmpty(maxEnrollNOStr)){
					maxEnrollNO = Integer.parseInt(maxEnrollNOStr);
				}
			}
		} catch (Exception e) {
			logger.error("获取某年级最大的录取编号出错", e);
		}
		return maxEnrollNO;
	}
	@Override
	public List<Map<String,Object>> getEnrollInfoByCertNum(String certNum) throws ServiceException{
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("certNum", certNum);
		StringBuffer sql = new StringBuffer();
		sql.append(" select s.name,s.nation,s.gender,s.certNum,s.bornday,decode(e.register_flag,null,'N',e.register_flag) register_flag,u.unitname,ee.backphotopath,                           ");
		sql.append(" ee.ccmc classic,ee.lqzymc major,ee.xxxsdm teachingType,decode(s.homeaddress,null,s.contactaddress,s.homeaddress) address ,e.registorSerialNO              ");
		sql.append("  from edu_recruit_enrolleeinfo e                                                ");
		sql.append(" join edu_recruit_recruitplan p on p.gradeid = e.grade and p.ispublished = 'Y'   ");
		sql.append(" join edu_base_student s on s.resourceid = e.studentbaseinfoid                   ");
		sql.append(" join hnjk_sys_unit u on u.resourceid = e.branchschoolid ");
		sql.append(" join edu_recruit_examinee ee on ee.sfzh = s.certnum and e.enrolleecode = ee.ksh ");
		sql.append(" where s.certnum =:certNum                                          ");
		sql.append(" order by  e.signupdate desc                                                     ");
		try {
			return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), param);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
