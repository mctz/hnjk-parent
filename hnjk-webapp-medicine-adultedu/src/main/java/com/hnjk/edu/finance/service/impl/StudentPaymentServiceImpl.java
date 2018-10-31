package com.hnjk.edu.finance.service.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.HttpUrlConnectionUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.MD5CryptorUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.service.BaseServiceImpl;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.AnnualFees;
import com.hnjk.edu.finance.model.FeeMajor;
import com.hnjk.edu.finance.model.PaymentFeePrivilege;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.model.StudentPaymentDetails;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.model.YearPaymentStandard;
import com.hnjk.edu.finance.model.YearPaymentStandardDetails;
import com.hnjk.edu.finance.service.IAnnualFeesService;
import com.hnjk.edu.finance.service.IFeeMajorService;
import com.hnjk.edu.finance.service.IPaymentFeePrivilegeService;
import com.hnjk.edu.finance.service.IStudentPaymentDetailsService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.service.IYearPaymentStandardService;
import com.hnjk.edu.finance.util.HttpURLConnectionTL;
import com.hnjk.edu.finance.util.SignUtils;
import com.hnjk.edu.finance.util.TonlyPayUtil;
import com.hnjk.edu.finance.vo.CheckFileTotalVO;
import com.hnjk.edu.finance.vo.CheckFileVO;
import com.hnjk.edu.finance.vo.EnquiryPaymentDetailsVO;
import com.hnjk.edu.finance.vo.PayDetailsVO;
import com.hnjk.edu.finance.vo.PaymentRerultVO;
import com.hnjk.edu.finance.vo.ResultVO;
import com.hnjk.edu.finance.vo.StuReturnFeeCommissionInfoVo;
import com.hnjk.edu.finance.vo.StudentPaymentVo;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IReginfoService;
import com.hnjk.edu.roll.service.IStudentBaseInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

/**
 * 学生缴费标准服务.
 * <code>StudentPaymentServiceImpl</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-11-12 下午01:10:21
 * @see 
 * @version 1.0
 */
@Transactional
@Service("studentPaymentService")
public class StudentPaymentServiceImpl extends BaseServiceImpl<StudentPayment> implements IStudentPaymentService {
	
	@Autowired
	@Qualifier("feeMajorService")
	private IFeeMajorService feeMajorService;
	
	@Autowired
	@Qualifier("yearPaymentStandardService")
	private IYearPaymentStandardService yearPaymentStandardService;
	
	@Autowired
	@Qualifier("paymentFeePrivilegeService")
	private IPaymentFeePrivilegeService paymentFeePrivilegeService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;	
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentBaseInfoService")
	private IStudentBaseInfoService studentBaseInfoService;
	
	@Autowired
	@Qualifier("studentPaymentDetailsService")
	private IStudentPaymentDetailsService studentPaymentDetailsService; 
	
	@Autowired
	@Qualifier("reginfoservice")
	private IReginfoService reginfoService;
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;//报名信息服务
	
	@Autowired
	@Qualifier("annualFeesService")
	private IAnnualFeesService annualFeesService;
	
	private static final String CODE_SUCCESS = "0";
	
	@Override
	public void createStudentPayment(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException {
		if(ExCollectionUtils.isNotEmpty(enrolleeInfoList)){
			Map<Integer, YearInfo> yearInfoMap = getYearInfoMap();//所有年度
			List<StudentPayment> studentPaymentList = new ArrayList<StudentPayment>();//学生缴费标准列表
			for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
				RecruitMajor recruitMajor = enrolleeInfo.getRecruitMajor();//招生专业
				Major major = recruitMajor.getMajor();//基础专业
				OrgUnit brSchool = enrolleeInfo.getBranchSchool();//学习中心
				FeeMajor feeMajor = feeMajorService.get(major.getResourceid());
				if(feeMajor == null || ExStringUtils.isBlank(feeMajor.getPaymentType())){
					throw new ServiceException("专业: "+major.getMajorName()+"没有设置缴费类别,无法生成学生缴费标准.");
				}
				String paymentType = feeMajor.getPaymentType();//缴费类别
				YearInfo yearInfo = enrolleeInfo.getGrade().getYearInfo();//年度
				YearPaymentStandard yearPaymentStandard = yearPaymentStandardService.getYearPaymentStandard(enrolleeInfo.getGrade().getResourceid(),paymentType);//年缴费标准
				if(yearPaymentStandard == null){
					throw new ServiceException(yearInfo.getYearName()+" "+JstlCustomFunction.dictionaryCode2Value("CodePaymentType", paymentType)+"的年缴费标准还未设置.");
				}				
				//获取学费优惠
				PaymentFeePrivilege paymentFeePrivilege = null;//学费优惠
				//招生专业学习中心优惠设置
				paymentFeePrivilege = paymentFeePrivilegeService.getPaymentFeePrivilege(brSchool.getResourceid(), recruitMajor.getResourceid());
				//学习中心优惠
				if(paymentFeePrivilege == null){//如果学习中心没有招生专业优惠，获取学习中心优惠
					paymentFeePrivilege = paymentFeePrivilegeService.getPaymentFeePrivilege(brSchool.getResourceid(), null);
				} 
				//生成新的年缴费标准
				Double totalRecpayFee = 0.0;//年缴费标准之和
				for (YearPaymentStandardDetails payment : yearPaymentStandard.getYearPaymentStandardDetails()) {
					totalRecpayFee += payment.getCreditFee();
				}
				Map<String, Object> condition = new HashMap<String, Object>();
				StudentPayment stuPayment=null;
				for (YearPaymentStandardDetails payment : yearPaymentStandard.getYearPaymentStandardDetails()) {
					condition.put("studyNo", enrolleeInfo.getMatriculateNoticeNo());
					
					
					Integer year = yearInfo.getFirstYear().intValue()+payment.getFeeTerm()-1;
					
					YearInfo chargeYearInfo = yearInfoMap.get(year);
					if(chargeYearInfo == null){
						throw new ServiceException("请先设置年度："+year+"-"+(year+1)+"学年度");
					}
					
					condition.put("yearInfoId",chargeYearInfo.getResourceid() );
					List<StudentPayment> list1 =  findStudentPaymentByCondition(condition);
					if(list1.size()==1) {
						stuPayment = list1.get(0);

						stuPayment.setYearInfo(chargeYearInfo);
						stuPayment.setChargeYear(year);//缴费自然年
						stuPayment.setStudyNo(enrolleeInfo.getMatriculateNoticeNo());//学号					
						stuPayment.setChargeTerm(payment.getFeeTerm());//缴费期数
						stuPayment.setChargeEndDate(payment.getCreditEndDate());//缴费截止日		
						
						stuPayment.setTerm(enrolleeInfo.getGrade().getTerm());
						stuPayment.setGrade(enrolleeInfo.getGrade());
						stuPayment.setTeachingType(enrolleeInfo.getTeachingType());
						stuPayment.setName(enrolleeInfo.getStudentBaseInfo().getName());
						stuPayment.setBranchSchool(brSchool);
						stuPayment.setMajor(enrolleeInfo.getRecruitMajor().getMajor());
						stuPayment.setClassic(enrolleeInfo.getRecruitMajor().getClassic());
						if(stuPayment.getFacepayFee()!=null) {
							stuPayment.setFacepayFee(0.0);
						}
					}else if(list1.size()>1){
						
					}else{
						stuPayment = new StudentPayment();

						stuPayment.setYearInfo(chargeYearInfo);
						stuPayment.setChargeYear(year);//缴费自然年
						stuPayment.setStudyNo(enrolleeInfo.getMatriculateNoticeNo());//学号					
						stuPayment.setChargeTerm(payment.getFeeTerm());//缴费期数
						stuPayment.setChargeEndDate(payment.getCreditEndDate());//缴费截止日		
							
						stuPayment.setTerm(enrolleeInfo.getGrade().getTerm());
						stuPayment.setGrade(enrolleeInfo.getGrade());
						stuPayment.setTeachingType(enrolleeInfo.getTeachingType());
						stuPayment.setName(enrolleeInfo.getStudentBaseInfo().getName());
						stuPayment.setBranchSchool(brSchool);
						stuPayment.setMajor(enrolleeInfo.getRecruitMajor().getMajor());
						stuPayment.setClassic(enrolleeInfo.getRecruitMajor().getClassic());
						stuPayment.setChargeStatus("0");//缴费状态，未缴费
						stuPayment.setDerateFee(0.0);//默认减免为0
						stuPayment.setFacepayFee(0.0);
					}					
					Double recpayFee = payment.getCreditFee();//年缴费标准,无优惠情况				
					if(paymentFeePrivilege != null && paymentFeePrivilege.getRecruitMajor()!=null){//招生专业学习中心优惠设置
						//新的年缴费标准=（优惠费总金额/年缴费标准之和）*年缴费标准
						recpayFee = (paymentFeePrivilege.getTotalPrivilegeFee() / totalRecpayFee) * payment.getCreditFee();						
					} else if(paymentFeePrivilege != null && paymentFeePrivilege.getRecruitMajor()==null){//学习中心学费优惠设置
						//新的年缴费标准=（年缴费标准/优惠前每学分缴费标准）*优惠后每学分缴费标准
						recpayFee = (payment.getCreditFee() / paymentFeePrivilege.getBeforePrivilegeFee()) * paymentFeePrivilege.getAfterPrivilegeFee();
					} 	
					if(stuPayment!=null){
						stuPayment.setRecpayFee(recpayFee);
						studentPaymentList.add(stuPayment);//加入更新列表
					}
				}	
				
				
			}
			batchSaveOrUpdate(studentPaymentList);//批量保存学生缴费标准
		}
	}
	
	

	/**
	 * 年度数据 key:起始年,value:年度数据
	 * @return
	 */
	private Map<Integer, YearInfo> getYearInfoMap() {
		Map<Integer, YearInfo> yearInfoMap = new HashMap<Integer, YearInfo>();
		List<YearInfo> yearInfoList = yearInfoService.findByCriteria(Restrictions.eq("isDeleted", 0));
		for (YearInfo yearInfo : yearInfoList) {
			yearInfoMap.put(yearInfo.getFirstYear().intValue(), yearInfo);
		}
		return yearInfoMap;
	}
	
	@Override
	public void deleteStudentPayment(List<EnrolleeInfo> enrolleeInfoList) throws ServiceException {
		if(ExCollectionUtils.isNotEmpty(enrolleeInfoList)){
			List<StudentPayment> studentPaymentList = new ArrayList<StudentPayment>();//学生缴费标准列表
			for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
				List<StudentPayment> list = findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.eq("studyNo", enrolleeInfo.getMatriculateNoticeNo()));
				studentPaymentList.addAll(list);
			}
			batchDelete(studentPaymentList);
		}
	}
	
	@Override
	@Transactional(readOnly=true)
	public Page findStudentPaymentByContidion(Map<String, Object> condition, Page objPage) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = getfindStudentPaymentByContidion(condition, values);
		if(objPage.isOrderBySetted()){
			hql.append(" order by "+objPage.getOrderBy()+" "+objPage.getOrder()+",studentInfo.studyNo ");
		}
		return findByHql(objPage, hql.toString(), values);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<StudentPayment> findStudentPaymentByCondition(Map<String, Object> condition) throws ServiceException {
		Map<String, Object> values = new HashMap<String, Object>();
		StringBuffer hql = getfindStudentPaymentByContidion(condition, values);
		if(condition.containsKey("orderby")){
			hql.append(" order by "+condition.get("orderby")+",studentInfo.studyNo ");
		}
		
		return findByHql(hql.toString(), values);
	}
	
	private StringBuffer getfindStudentPaymentByContidion(Map<String, Object> condition, Map<String, Object> values){
		StringBuffer hql = new StringBuffer("from "+StudentPayment.class.getSimpleName()+" where isDeleted=:isDeleted ");
		values.put("isDeleted", 0);
		if(condition.containsKey("brSchool")){//学习中心
			hql.append(" and studentInfo.branchSchool.resourceid=:brSchool ");
			values.put("brSchool", condition.get("brSchool"));
		}
		if(condition.containsKey("branchSchoolId")){//学习中心
			hql.append(" and studentInfo.branchSchool.resourceid=:branchSchoolId ");
			values.put("branchSchoolId", condition.get("branchSchoolId"));
		}
		if(condition.containsKey("gradeid")){//年级
			hql.append(" and studentInfo.grade.resourceid=:gradeid ");
			values.put("gradeid", condition.get("gradeid"));
		}
		if(condition.containsKey("majorid")){//专业
			hql.append(" and studentInfo.major.resourceid=:majorid ");
			values.put("majorid", condition.get("majorid"));
		}
		if(condition.containsKey("classicid")){//层次
			hql.append(" and studentInfo.classic.resourceid=:classicid ");
			values.put("classicid", condition.get("classicid"));
		}
		if(condition.containsKey("name")){//学生姓名
			hql.append(" and studentInfo.studentName like :name ");
			values.put("name", "%"+condition.get("name")+"%");
		}
		if(condition.containsKey("studyNo")){//学号
			hql.append(" and studentInfo.studyNo =:studyNo ");
			values.put("studyNo", condition.get("studyNo"));
		}
		if(condition.containsKey("yearInfoId")){//缴费年度
			hql.append(" and yearInfo.resourceid=:yearInfoId ");
			values.put("yearInfoId", condition.get("yearInfoId"));
		}
		if(condition.containsKey("term")){//缴费学期
			hql.append(" and term=:term ");
			values.put("term", condition.get("term"));
		}
		if(condition.containsKey("chargeEndDate")){//截止日期
			hql.append(" and chargeEndDate=:chargeEndDate ");
			values.put("chargeEndDate", condition.get("chargeEndDate"));
		}
		if(condition.containsKey("deferEndDate")){//缓缴日期
			hql.append(" and deferEndDate=:deferEndDate ");
			values.put("deferEndDate", condition.get("deferEndDate"));
		}
		if(condition.containsKey("studentInfoId")){//学籍ID
			hql.append(" and studentInfo.resourceid=:studentInfoId ");
			values.put("studentInfoId", condition.get("studentInfoId"));
		}
		if(condition.containsKey("enrolleecode")){// 考生号
			hql.append(" and studentInfo.enrolleeCode=:enrolleecode ");
			values.put("enrolleecode", condition.get("enrolleecode"));
		}
		if(condition.containsKey("examCertificateNo")){// 准考证号
			hql.append(" and studentInfo.examCertificateNo=:examCertificateNo ");
			values.put("examCertificateNo", condition.get("examCertificateNo"));
		}
		if(condition.containsKey("studentStatus")){//学籍状态
			hql.append(" and studentInfo.studentStatus=:studentStatus ");
			values.put("studentStatus", condition.get("studentStatus"));
		}
		if(condition.containsKey("studentStatus_in")){//学籍状态
			hql.append(" and studentInfo.studentStatus in ('"+condition.get("studentStatus_in")+"') ");
		}
		if(condition.containsKey("classesId")){//班级ID
			hql.append(" and studentInfo.classes.resourceid=:classesId ");
			values.put("classesId", condition.get("classesId"));
		}
		if(condition.containsKey("chargeStatus")){//缴费状态
			if("2".equals(condition.get("chargeStatus"))){
				hql.append(" and (chargeStatus='0' or chargeStatus='-1') ");
			}else{
				hql.append(" and chargeStatus=:chargeStatus ");
				values.put("chargeStatus", condition.get("chargeStatus"));
			}
		}
		if(condition.containsKey("resourceid")){//ID
			hql.append(" and resourceid=:resourceid ");
			values.put("resourceid", condition.get("resourceid"));
		}
		return hql;
	}
	
	@Override
	public List<StudentPayment> findDeferStudentPayment(StudentPayment studentPayment) throws ServiceException {
		String hql = "from "+StudentPayment.class.getSimpleName()+" where isDeleted=? and studyNo=? and chargeTerm=? and isDefer=? and resourceid<>? order by deferEndDate asc,resourceid";
		return findByHql(hql, 0,studentPayment.getStudyNo(),studentPayment.getChargeTerm(),Constants.BOOLEAN_YES,studentPayment.getResourceid());
	}
	
	@Override
	public List<Map<String, Object>> statStudentPayment(Map<String, Object> condition, String byType) throws ServiceException {
		
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		try {
			StringBuffer sql =  getStudentPaymentSql(condition, byType);
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
		} catch (Exception e) {
			logger.error("缴费情况统计出错",e.fillInStackTrace());
		}
		return list;
	}
	
	private StringBuffer getStudentPaymentSql(Map<String, Object> condition, String byType) {
		StringBuffer sql = new StringBuffer();
		//缴费年度，统计类型编码，统计类型名称，应缴费用人数，应缴费用金额，完费人数，完费金额，部分缴费人数，部分缴费金额，欠费人数，欠费金额
		sql.append(" select t.firstyear, ");
		if("teachingType".equals(byType)){ //按办学模式统计
			//sql.append(" f_dictionary('CodeTeachingType',(case when t.teachingtype = 'net' or t.teachingtype = 'face' or t.teachingtype = 'direct' then 'net' else t.teachingtype end)) statCode,f_dictionary('CodeTeachingType',t.teachingtype) statType, case when t.teachingtype = 'net' or t.teachingtype = 'face' or t.teachingtype = 'direct' then 'net' else t.teachingtype end pteachingtype, ");
			sql.append(" t.teachingtype as statType, t.dictname as teachingtype, ");
		} else if("major".equals(byType)){//按专业统计
			sql.append(" t.majorname as statType, t.majorname as statcode, ");
		} else if("classic".equals(byType)){//按层次统计
			sql.append(" t.classicname as statType, t.classiccode as statcode, ");				
		} else {//按学习中心统计
			sql.append(" t.unitcode||'-'||t.unitname as statType, t.unitcode as statcode, ");			
		} 
		sql.append(" 	count(t.studyno) allfeeCount, sum(t.recpayfee) recpayfee,sum(t.facepayfee) facepayfee, sum(t.deratefee) deratefee, ");
		sql.append(" 	count(case when t.facepayfee + t.deratefee >= t.recpayfee then t.studyno else null end) fullfeeCount, ");
		sql.append(" 	sum(case when t.facepayfee + t.deratefee >= t.recpayfee then t.facepayfee else 0 end) fullfeeSum, ");
		sql.append(" 	count(case when t.facepayfee + t.deratefee < t.recpayfee then t.studyno else null end) owefeeCount, ");
		sql.append(" 	sum(case when t.facepayfee + t.deratefee < t.recpayfee then t.recpayfee - t.facepayfee - t.deratefee else 0 end) owefeeSum, ");
		sql.append(" 	count(case when t.deratefee > 0 then t.studyno else null end) deratefeeCount, ");
		sql.append(" 	sum(case when t.deratefee > 0 then t.deratefee else 0 end) deratefeeSum, ");
		sql.append(" 	count(case when t.facepayfee > 0 and t.facepayfee + t.deratefee < t.recpayfee then t.studyno else null end) partfeeCount,");
		sql.append(" 	sum(case when t.facepayfee > 0 and t.facepayfee + t.deratefee < t.recpayfee then t.facepayfee else 0 end) partfeeSum ");
		String joinsql = " join hnjk_sys_dict d on d.dictcode like 'CodeTeachingType_%' and d.isused='Y' and d.dictvalue=f.teachingtype"
				+ " join edu_base_year y on y.resourceid=f.yearid ";
		if("major".equals(byType)){
			sql.append(" from (select m.majorcode,m.majorname,f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted, sum(f.recpayfee) recpayfee,sum(f.deratefee) deratefee,nvl(sum(f.facepayfee),0) facepayfee from edu_fee_stufee f "+joinsql+" join edu_base_major m on m.resourceid=f.majorid where f.isdeleted=0 group by m.majorcode,m.majorname,f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted) t ");
		} else if("classic".equals(byType)){
			sql.append(" from (select cl.classiccode,cl.classicname,f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted, sum(f.recpayfee) recpayfee,sum(f.deratefee) deratefee,nvl(sum(f.facepayfee),0) facepayfee from edu_fee_stufee f "+joinsql+" join edu_base_classic cl on cl.resourceid=f.classicid where f.isdeleted=0 group by cl.classiccode,cl.classicname,f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted) t ");
		} else if("brSchool".equals(byType)){
			sql.append(" from (select u.unitcode,u.unitname,f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted, sum(f.recpayfee) recpayfee,sum(f.deratefee) deratefee,nvl(sum(f.facepayfee),0) facepayfee from edu_fee_stufee f "+joinsql+" join hnjk_sys_unit u on u.resourceid=f.branchschoolid where f.isdeleted=0 group by u.unitcode,u.unitname,f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted) t ");
		} else {
			sql.append(" from (select f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted, sum(f.recpayfee) recpayfee,sum(f.deratefee) deratefee,nvl(sum(f.facepayfee),0) facepayfee from edu_fee_stufee f "+joinsql+" where f.isdeleted=0 group by f.yearid,y.firstyear,f.studyno,f.studentid,f.chargeterm,f.teachingtype,d.dictname,f.classicid,f.majorid,f.branchschoolid,f.classicid,f.isdeleted) t ");
		}
		
		sql.append(" where t.isdeleted=0 ");
		if(condition.containsKey("branchSchool")){//过滤校外学习中心数据
			sql.append("       and  t.branchschoolid=:branchSchool ");
		}
		if(condition.containsKey("yearInfoId")){
			sql.append("       and t.yearid=:yearInfoId ");
		}
		if("teachingType".equals(byType)){ 
			sql.append(" group by t.firstyear, t.teachingtype,t.dictname ");
			sql.append(" order by t.firstyear desc ");
			//sql.append(" order by y.firstyear desc,decode(pteachingtype,'net', 1, 'adult', 2, 'specialbatch', 3),decode(t.teachingtype,'net', 1, 'face', 2, 'direct', 3) ");
		} else 	if("classic".equals(byType)){ 
			sql.append(" group by t.firstyear, t.classiccode,t.classicname ");
			sql.append(" order by t.firstyear desc ");
			//sql.append(" order by t.firstyear desc,decode(t.classiccode,'3', 1, '2', 2, '1', 3, '8',4) ");			
		} else if("major".equals(byType)){
			sql.append(" group by t.firstyear, t.majorcode,t.majorname ");
			sql.append(" order by t.firstyear desc,t.majorcode ");
		} else {
			sql.append(" group by t.firstyear, t.unitcode,t.unitname ");
			sql.append(" order by t.firstyear desc,t.unitcode ");
		}
		return sql;
	}
	@Override
	@Transactional(readOnly=true)
	public Page findStudentPaymentStuByContidion(Map<String, Object> condition, Page objPage) throws ServiceException {
		List<Object> values = new ArrayList<Object>();
		StringBuffer hql = findStudentPaymentStuByContidion(condition, values);
		Page list = baseSupportJdbcDao.getBaseJdbcTemplate().findListMap(objPage,hql.toString(), values.toArray());
		return list;
	}
	
	
	@Override
	@Transactional(readOnly=true)
	public List findStudentPaymentStu1ByContidion(Map<String, Object> condition) throws ServiceException {
		List<Object> values = new ArrayList<Object>();
		
		StringBuffer hql = findStudentPaymentStuByContidion(condition, values);
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			list = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(hql.toString(), values.toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	
	private StringBuffer findStudentPaymentStuByContidion(Map<String, Object> condition,List<Object> values) {
		StringBuffer sql = new StringBuffer();
		

		sql.append(" select pay1.recpayfee recpayfeeall,grade.resourceid,grade.GRADENAME,pay1.studyno,pay1.name,s.certnum,pay1.branchschoolid,e1.LXDH mobile,pay.recpayfee,pay.facepayfee,pay.deratefee,"
					+"(pay.recpayfee-pay.facepayfee-pay.deratefee) pays,major.majorname,major.resourceid,pay.chargeStatus,unit.unitname ");
	
		sql.append(" from  ( select fee.studyno studyno,sum(fee.recpayfee)  recpayfee,sum(fee.facepayfee) facepayfee,sum(fee.deratefee) deratefee," +
				" (case when sum(fee.facepayfee)=0 then '0' when sum(fee.facepayfee)>0 and sum(fee.recpayfee)-sum(fee.facepayfee)-sum(fee.deratefee)>0 then '-1' "+
				" when sum(fee.facepayfee)-sum(fee.facepayfee)=sum(fee.deratefee) then '1' end)  chargeStatus,NAME,GRADEID,BRANCHSCHOOLID "); 
		sql.append(" from EDU_FEE_STUFEE  fee where fee.isdeleted=0 group by fee.studyno,fee.NAME,fee.GRADEID,fee.BRANCHSCHOOLID) pay1 ");
		
		
		sql.append(" left join  ( select fee.studyno studyno,sum(fee.recpayfee)  recpayfee,sum(fee.facepayfee) facepayfee,sum(fee.deratefee) deratefee," +
				" (case when sum(fee.facepayfee)=0 and sum(fee.recpayfee)-sum(fee.facepayfee)>sum(fee.deratefee) then '0' when sum(fee.facepayfee)>0 and sum(fee.recpayfee)-sum(fee.facepayfee)-sum(fee.deratefee)>0 then '-1' "+
				" when sum(fee.recpayfee)-sum(fee.facepayfee)=sum(fee.deratefee) then '1' end)  chargeStatus"); 
		sql.append(" from EDU_FEE_STUFEE  fee where fee.isdeleted=0 and ((sysdate>fee.chargeenddate and fee.isdefer = 'N') or sysdate>fee.deferenddate) group by fee.studyno) pay on pay.studyno = pay1.studyno ");
		sql.append(" left join  edu_roll_studentinfo stu on pay1.studyno = stu.studyno and stu.isdeleted=0 ");
		sql.append(" left join edu_base_student s on s.resourceid = stu.studentbaseinfoid ");
		sql.append(" left join hnjk_sys_unit unit on unit.resourceid = pay1.branchschoolid ");
		sql.append(" left join edu_base_grade grade on grade.resourceid = pay1.gradeid ");
		
		sql.append(" left join EDU_RECRUIT_ENROLLEEINFO ex on ex.matriculateNoticeNo = pay1.studyno");
		sql.append(" left join EDU_RECRUIT_EXAMINEE e1 on e1.ZKZH = ex.examCertificateNo");
		
		sql.append(" left join edu_base_major major on major.resourceid = stu.majorid where 1=1 and e1.kszt = '5'");
		
		if(condition.containsKey("brSchool")){//学习中心
			sql.append(" and unit.resourceid=:brSchool ");
			values.add( condition.get("brSchool"));
		}
		
//		if(condition.containsKey("majorid")){//专业
//			sql.append(" and major.resourceid=:majorid ");
//			values.add(condition.get("majorid"));
//		}
		if(!condition.containsKey("studynos")){
			if(condition.containsKey("name")){//学生姓名
				sql.append(" and pay1.name like :name ");
				values.add("%"+condition.get("name")+"%");
			}
		}
		if(condition.containsKey("studyNo")){//学号
			sql.append(" and pay.studyNo =:studyNo ");
			values.add(condition.get("studyNo"));
		}
	
		if(condition.containsKey("gradeid")){//年级
			sql.append(" and grade.resourceid=:gradeid ");
			values.add(condition.get("gradeid"));
		}
		
		if(condition.containsKey("qianfeipay")){//年级
			sql.append(" and pay.recpayfee-pay.facepayfee-pay.deratefee >=:qianfeipay ");
			values.add(condition.get("qianfeipay"));
		}
		
		if(condition.containsKey("studynos")){
			String [] nos = condition.get("studynos").toString().split(",");
			
			for (int i = 0; i < nos.length; i++) {
//					if(i==ids.length-1){
//						studynos1+=ids[i];
//					}else{
//						studynos1+=ids[i]+",";
//					}
//				}
				if(i==0&&nos.length==1){
					sql.append(" and pay1.studyno =:no  ");
		
				}else {
					if(i==0&&nos.length>1){
						sql.append(" and (pay1.studyno =:no  ");
						
					}else if(i<nos.length-1){
						sql.append(" or pay1.studyno =:no  ");
						
					}else if(i==nos.length-1){
						sql.append(" or pay1.studyno =:no)  ");
					}
					
					
				}
				values.add(nos[i]);
				
			}
//			
//			sql.append(" and stu.studyno in (:studynos) ");
//			values.add(condition.get("studynos"));
		}
		
		if(condition.containsKey("chargeStatus")){//缴费状态
			
			if("2".equals(condition.get("chargeStatus"))){
				sql.append(" and (pay.chargeStatus='0' or pay.chargeStatus='-1') ");
			}else{
				sql.append(" and pay.chargeStatus=:chargeStatus ");
				values.add( condition.get("chargeStatus"));
			}
		}
		sql.append(" order by pay.studyNo ");
		
		return sql;
	}

	/**
	 * 给学生生成缴费记录
	 * @param studentInfoList
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> generateStudentFeeRecord(List<StudentInfo>studentInfoList) throws ServiceException {
		StringBuffer msg = new StringBuffer("");
		int statusCode = 200;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, YearPaymentStandard> ypsMap = new HashMap<String, YearPaymentStandard>();
		Map<String, YearPaymentStandardDetails> ypsdMap = new HashMap<String, YearPaymentStandardDetails>();
		Map<String, FeeMajor> feeMajorMap = new HashMap<String, FeeMajor>();
		Map<String, String> messageMap = new HashMap<String, String>();
		List<StudentPayment> studentFeeRecordList = new ArrayList<StudentPayment>();//学生缴费标准列表
		List<AnnualFees> annualFeesList = new ArrayList<AnnualFees>();//学生年度缴费
		String _msg = "";
		
		try {
			if(ExCollectionUtils.isNotEmpty(studentInfoList)){
				Map<String,AnnualFees> annualFeesMap = null;
				List<AnnualFees> _annualFeesList = null;
				Map<String,Object> condition = new HashMap<String, Object>();
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				String feeMajorKey = null;
				for (StudentInfo studentInfo: studentInfoList) {
					// 获取学生的年度缴费标准
					condition.clear();
					condition.put("studentInfoId", studentInfo.getResourceid());
					_annualFeesList = annualFeesService.findAnnualFeesByCondition(condition );
					annualFeesMap = convertAnnualFeesToMap(_annualFeesList);
					
					boolean flag = false;//标志用于下面是否需要增加年度缴费
					condition.clear();
					condition.put("studyNo", studentInfo.getStudyNo());
					condition.put("studentInfoId", studentInfo.getResourceid());
					List<StudentPayment> StudentPaymentList =  findStudentPaymentByCondition(condition);
					
					if(ExCollectionUtils.isNotEmpty(StudentPaymentList)) {// 已经有记录的不处理
						//暂时只确认有三条记录,当做三个学年的时候,就跳过,否则生成剩下的年度记录,但是不生成总的缴费记录
						if(annualFeesMap!=null&&annualFeesMap.size()==3) {
							continue;
						}else{
							flag = true;
						}
					}
					Major major = studentInfo.getMajor();//基础专业
					String teachingType = "2";
					if(schoolCode.equals("10598")){// 广西
						teachingType = studentInfo.getTeachingType();
					}
				
					FeeMajor feeMajor = null;
					feeMajorKey = major.getResourceid()+"_"+teachingType;
					if(feeMajorMap.containsKey(feeMajorKey)){
						feeMajor = feeMajorMap.get(feeMajorKey);
					} else {
						if(messageMap.containsKey(feeMajorKey)){
							continue;
						}
//						feeMajor  = feeMajorService.findByProperty("resourceid",major.getResourceid());
						feeMajor  = feeMajorService.findUniqueByCondition(major.getResourceid(), teachingType);
						if(feeMajor == null || ExStringUtils.isBlank(feeMajor.getPaymentType())){
							_msg = "专业: "+major.getMajorName()+"没有设置缴费类别,无法生成学生缴费标准.<br>";
							messageMap.put(major.getResourceid(), _msg);
							msg.append(_msg);
							continue;
						}
						feeMajorMap.put(feeMajorKey, feeMajor);
					}
					String paymentType = feeMajor.getPaymentType();//缴费类别
					YearInfo yearInfo = studentInfo.getGrade().getYearInfo();//年度
					String year_paymentType_key = yearInfo.getResourceid()+"_"+paymentType;
					YearPaymentStandard yearPaymentStandard  = null;
					if(ypsMap.containsKey(year_paymentType_key)){
						yearPaymentStandard = ypsMap.get(year_paymentType_key);
					} else {
						if(messageMap.containsKey(year_paymentType_key)){
							continue;
						}
						yearPaymentStandard = yearPaymentStandardService.getYearPaymentStandard(studentInfo.getGrade().getResourceid(),paymentType);//年缴费标准
						if(yearPaymentStandard == null || ExCollectionUtils.isEmpty(yearPaymentStandard.getYearPaymentStandardDetails())){
							_msg = studentInfo.getGrade().getGradeName()+" "+JstlCustomFunction.dictionaryCode2Value("CodePaymentType", paymentType)+"的年缴费标准还未设置.<br>";
							messageMap.put(year_paymentType_key, _msg);
							msg.append(_msg);
							continue;
						}				
						ypsMap.put(year_paymentType_key, yearPaymentStandard);
					}
					OrgUnit brSchool = studentInfo.getBranchSchool();//学习中心
					/** TODO: 目前学费优惠的规则还没有确定，规则不一定符合，所以先不用 **/
					/*//获取学费优惠(招生专业和学习中心的优惠不叠加)
					PaymentFeePrivilege paymentFeePrivilege = null;//学费优惠
					condition.clear();
					condition.put("grade", studentInfo.getGrade().getResourceid());
					condition.put("yearInfo", yearInfo.getResourceid());
					condition.put("major", major.getResourceid());
					condition.put("classic", studentInfo.getClassic().getResourceid());
					condition.put("branchSchool", brSchool.getResourceid());
					condition.put("teachingType", studentInfo.getTeachingType());
					List<RecruitMajor> recruitMajorList =recruitMajorService.findMajorByCondition(condition);
					if(ExCollectionUtils.isNotEmpty(recruitMajorList)){
						//招生专业学习中心优惠设置
						paymentFeePrivilege = paymentFeePrivilegeService.getPaymentFeePrivilege(brSchool.getResourceid(), recruitMajorList.get(0).getResourceid());
					}
					//学习中心优惠
					if(paymentFeePrivilege == null){//如果学习中心没有招生专业优惠，获取学习中心优惠
						paymentFeePrivilege = paymentFeePrivilegeService.getPaymentFeePrivilege(brSchool.getResourceid(), null);
					} 
					Double totalRecpayFee = 0.0;//年缴费标准之和
					
*/					
					Map<String,YearInfo> yearInfoMap = convertYearToMap(yearInfoService.findAllByOrder());
					
					Date earlistDate = null;
					YearPaymentStandardDetails firstPayment = null;
					if(ypsdMap.containsKey(year_paymentType_key)){
						firstPayment = ypsdMap.get(year_paymentType_key);
//						totalRecpayFee = firstPayment.getTotalRecpayFee();
					} else {
						for (YearPaymentStandardDetails payment : yearPaymentStandard.getYearPaymentStandardDetails()) {
							// 获取第一年缴费标准
							if(earlistDate==null){
								earlistDate = payment.getCreditEndDate();
								firstPayment = payment;
							} else {
								if(earlistDate.after(payment.getCreditEndDate())){
									earlistDate = payment.getCreditEndDate();
									firstPayment = payment;
								}
							}
							// 计算的应缴金额
//							totalRecpayFee += payment.getCreditFee();
							
						}
//						firstPayment.setTotalRecpayFee(totalRecpayFee);
						ypsdMap.put(year_paymentType_key, firstPayment);
					}
					
					//生成年度缴费信息
					int _year = yearInfo.getFirstYear().intValue();
					// 学费
					String chargingItems = "tuition";
					for (YearPaymentStandardDetails payment : yearPaymentStandard.getYearPaymentStandardDetails()) {
						
						int _firstYear = _year+payment.getFeeTerm()-1;
						// 生成学生年度缴费标准（三年）
						AnnualFees annualFees = null;
						if(annualFeesMap!=null){
							annualFees = annualFeesMap.get(_firstYear+studentInfo.getStudyNo()+chargingItems);
						}
						//生成学生年度缴费记录
						if(annualFees==null){
							annualFees = new AnnualFees();
							annualFees.setChargeStatus(0);
							annualFees.setChargeYear(_firstYear);
							annualFees.setDerateFee(0.00);
							annualFees.setFacepayFee(0.00);
							annualFees.setMemo(ExDateUtils.getCurrentDateTimeStr());
							annualFees.setRecpayFee(payment.getCreditFee());
							annualFees.setReturnPremiumFee(0.00);
							annualFees.setStudentInfo(studentInfo);
							annualFees.setStudyNo(studentInfo.getStudyNo());
							annualFees.setYearInfo(yearInfoMap.get(String.valueOf(_firstYear)));//第一学年
							annualFees.setChargingItems(chargingItems);
							annualFees.setPayAmount(0d);
						}
						annualFeesList.add(annualFees);//加入更新列表
					}
					if(!flag){//如果不需要则直接不创建缴费就可以
						//生成学生缴费记录
						StudentPayment stuPayment = new StudentPayment();
						stuPayment.setStudentInfo(studentInfo);// 学籍信息			
						stuPayment.setStudyNo(studentInfo.getStudyNo());
						stuPayment.setName(studentInfo.getStudentName());
						stuPayment.setGrade(studentInfo.getGrade());
						stuPayment.setYearInfo(yearInfo);
						stuPayment.setTeachingType(studentInfo.getTeachingType());
						stuPayment.setBranchSchool(brSchool);
						stuPayment.setMajor(major);
						stuPayment.setClassic(studentInfo.getClassic());
						stuPayment.setChargeStatus("0");//缴费状态，未缴费
						stuPayment.setDerateFee(0.0);//默认减免为0
						stuPayment.setFacepayFee(0.0);// 已缴金额
						stuPayment.setChargeTime(new Date());// 生成缴费记录日期
						stuPayment.setChargeEndDate(firstPayment.getCreditEndDate());
						stuPayment.setChargeTerm(firstPayment.getFeeTerm());// 缴费标准期数
						stuPayment.setStudyNoType("2");// 18级之后的学生使用身份证号作为通联对接的学号
						Double recpayFee = firstPayment.getCreditFee();//应缴金额,无优惠情况		
						/** TODO: 优惠这部分以后按实际需求实现，目前先按规则 **/
						/*if(paymentFeePrivilege != null && paymentFeePrivilege.getRecruitMajor()!=null
								&& paymentFeePrivilege.getTotalPrivilegeFee()!=null
								&& paymentFeePrivilege.getTotalPrivilegeFee()!=0){//招生专业学习中心优惠设置
							//新的年缴费标准=（优惠费总金额/年缴费标准之和）*年缴费标准
							recpayFee = (paymentFeePrivilege.getTotalPrivilegeFee() / totalRecpayFee) * recpayFee	;						
						} else if(paymentFeePrivilege != null && paymentFeePrivilege.getRecruitMajor()==null
								&& paymentFeePrivilege.getBeforePrivilegeFee()!=null
								&& paymentFeePrivilege.getBeforePrivilegeFee()!=0
								&& paymentFeePrivilege.getAfterPrivilegeFee()!=null
								&& paymentFeePrivilege.getAfterPrivilegeFee()!=0){//学习中心学费优惠设置
							//新的年缴费标准=（年缴费标准/优惠前每学分缴费标准）*优惠后每学分缴费标准
							recpayFee = (recpayFee/ paymentFeePrivilege.getBeforePrivilegeFee()) * paymentFeePrivilege.getAfterPrivilegeFee();
						} 	*/
						stuPayment.setRecpayFee(recpayFee);
					
						studentFeeRecordList.add(stuPayment);//加入更新列表
					}
				}	
				//批量保存学生缴费标准	
				batchSaveOrUpdate(studentFeeRecordList);
				annualFeesService.batchSaveOrUpdate(annualFeesList);
			}
		} catch (Exception e) {
			logger.error("给学生生成缴费记录出错",e );
			// 清空
			msg.setLength(0);
			msg.append("生成缴费记录失败！");
		} finally {
			// 有失败的记录
			if(msg.length() > 0){
				statusCode = 300;
			}
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", msg.toString());
			returnMap.put("paymentList", studentFeeRecordList);
		}
		return returnMap;
	}

	private Map<String, YearInfo> convertYearToMap(List<YearInfo> yearList) {
		Map<String, YearInfo> map = new HashMap<String, YearInfo>();
		if(ExCollectionUtils.isNotEmpty(yearList)){
			for(YearInfo y : yearList){
				map.put(y.getFirstYear().toString(), y);
			}
		}
		return map;
	}



	private Map<String, AnnualFees> convertAnnualFeesToMap(List<AnnualFees> _annualFeesList) {
		Map<String, AnnualFees> map = null;
		if(ExCollectionUtils.isNotEmpty(_annualFeesList)){
			map = new HashMap<String, AnnualFees>();
			for(AnnualFees af : _annualFeesList){
				map.put(af.getYearInfo().getFirstYear()+af.getStudyNo()+af.getChargingItems(), af);
			}
		}
		
		return map;
	}



	/**
	 * 处理学生缴费信息导入
	 * @param modelList
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> handleStudentPaymentImport(List<StudentPaymentVo> modelList, User user) throws ServiceException {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		List<StudentPayment> updateList = new ArrayList<StudentPayment>();// 缴费记录更新列表
		List<StudentPaymentDetails> addList = new ArrayList<StudentPaymentDetails>();// 缴费明细新增列表
		try {
			// 获取缴费类型全局参数
			SysConfiguration payTypeConf = CacheAppManager.getSysConfigurationByCode("payment.payType");
			Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
			if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
					&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){
				payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
			}
			if(ExCollectionUtils.isNotEmpty(modelList)){
				Map<String ,Object> condition = null;
				String studyNo=null;
				String name= null;
				StudentInfo studentInfo = null;
				StudentPaymentDetails studentPaymentDetails =null;
				StudentPayment studentPayment =null;
				long starttime1 = System.currentTimeMillis();
				for (StudentPaymentVo s : modelList) {
					long starttime = System.currentTimeMillis();
					condition = new HashMap<String, Object>();
					studyNo= s.getStudyNo();
					name= ExStringUtils.defaultIfEmpty(s.getName(), "");
					condition.clear();
					condition.put("studyNo", studyNo);
					// 检验该学生是否存在
					studentInfo = studentInfoService.findUniqueStudentInfo(condition);
					if(studentInfo == null){
						msg.append("学号为："+studyNo+"这位学生不存在<br>");
						continue;
					}
					if(!name.equals(studentInfo.getStudentName())){
						msg.append("学号为："+studyNo+"这位学生的姓名有误<br>");
						continue;
					}
					// 检验导入的金额是否有误
					Double recpayFee = s.getRecpayFee()==null?0:s.getRecpayFee();// 应缴金额
					Double facepayFee = s.getFacepayFee()==null?0:s.getFacepayFee();// 实缴金额
					Double unpaidFee = s.getUnpaidFee()==null?0:s.getUnpaidFee();// 未缴金额
					if(unpaidFee<0){//未交金额如果为负数，则转为正数
						unpaidFee=-unpaidFee;
					}
					if(recpayFee <= 0){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的应缴金额不能小于或等于0<br/>");
						continue;
					}
					if(facepayFee <= 0){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的实缴金额不能小于或等于0<br>");
						continue;
					}
					/*if(unpaidFee < 0){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的未缴金额不能小于0<br>");
						continue;
					}*/
					if(recpayFee!=(facepayFee+unpaidFee)){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的数据有误[应缴金额不等于(实缴金额+欠费金额)]<br>");
						continue;
					}
					condition.clear();
					condition.put("studentInfoId", studentInfo.getResourceid());
					List<StudentPayment> studentPaymentList = findStudentPaymentByCondition(condition);
					if(ExCollectionUtils.isEmpty(studentPaymentList)){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生还没有生成缴费记录<br>");
						continue;
					}
					if(studentPaymentList.size() > 1){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的缴费记录有多条<br>");
						continue;
					}
					studentPayment = studentPaymentList.get(0);
					Double _sysPaidFee = studentPayment.getFacepayFee()==null?0:studentPayment.getFacepayFee();// 系统中实缴金额
					Double _recpayFee = studentPayment.getRecpayFee();// 系统中的应缴金额
					Double _derateFee = studentPayment.getDerateFee()==null?0:studentPayment.getDerateFee();// 减免金额
					if(!_recpayFee.equals(recpayFee)){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的应缴金额与系统中的应缴金额有误差<br>");
						continue;
					}
					Double _facepayFee = 0d;
					if(payType==2){// 叠加
						studentPayment.setFacepayFee(_sysPaidFee + facepayFee);// 实缴金额
						_facepayFee = _sysPaidFee + facepayFee+_derateFee;
					} else {
						studentPayment.setFacepayFee(facepayFee);
						_facepayFee = facepayFee+_derateFee;
					}
					
					studentPayment.setRecpayFee(recpayFee);
					if(_facepayFee == 0){
						studentPayment.setChargeStatus("0");// 未缴费
					}else if(recpayFee>_facepayFee){
						studentPayment.setChargeStatus("-1");// 欠费
					}else if(_facepayFee < recpayFee) {
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的数据有误[(缴费金额+减免金额)不能大于应缴金额]！<br>");
						continue;
					} else {
						studentPayment.setChargeStatus("1");// 已缴费
					}
					studentPayment.setEnableLogHistory(true);
					
					//学生缴费明细
					studentPaymentDetails = new StudentPaymentDetails();
					studentPaymentDetails.setStudentInfo(studentInfo);
					studentPaymentDetails.setOperateDate(new Date());
					studentPaymentDetails.setPayType(payType);// 缴费类型
					if(payType==2){// 叠加
						studentPaymentDetails.setPayAmount(facepayFee);// 缴费金额
						studentPaymentDetails.setPaidAmount(_sysPaidFee+facepayFee);// 已缴金额
					}else {// 直接更新
						if((facepayFee-_sysPaidFee)<0){
							statusCode = 300;
							msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的缴费金额不能小于上次缴费金额！");
							continue;
						}
						studentPaymentDetails.setPayAmount(_sysPaidFee==0?facepayFee:((facepayFee-_sysPaidFee)<0?0:(facepayFee-_sysPaidFee)));// 缴费金额
						studentPaymentDetails.setPaidAmount(facepayFee);
					}
					
					studentPaymentDetails.setShouldPayAmount(recpayFee);
					studentPaymentDetails.setOperatorName(user.getCnName());
					studentPaymentDetails.setOperatorId(user.getResourceid());
					updateList.add(studentPayment);
					addList.add(studentPaymentDetails);
					long endtime = System.currentTimeMillis();
					logger.info((endtime-starttime)/1000f+"秒");
				}
				// 更新学生缴费记录
				long endtime1 = System.currentTimeMillis();
				logger.info((endtime1-starttime1)/1000f+"秒");
				batchSaveOrUpdate(updateList);
				// 保存学生缴费明细
				studentPaymentDetailsService.batchSaveOrUpdate(addList);
			}
		} catch (Exception e) {
			logger.error("处理学生缴费信息导入出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("导入学生缴费信息失败！");
		} finally {
			if(msg.length() >0) {
				statusCode = 300;
			}
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
		}
		return retrunMap;
	}
	
	@Override
	public Map<String, Object> handleStudentPaymentImport_new(List<StudentPaymentVo> modelList, User user) throws ServiceException{
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		
		List<StudentPaymentVo> failList = new ArrayList<StudentPaymentVo>();//失败列表
		
		SysConfiguration payTypeConf = CacheAppManager.getSysConfigurationByCode("payment.payType");
		SysConfiguration feeDirectType = CacheAppManager.getSysConfigurationByCode("payment.isDirectImport");
		boolean isDirectImport = "Y".equals(feeDirectType.getParamValue().trim());
		
		Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
		if(isDirectImport) {
			payTypeConf = null;//如果使用直接导入缴费信息，则直接更新不累加
		}
		if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
				&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){
			payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
		}
		//根据学号，查找出所有学生的缴费情况，放入List 中，根据学号排序，后面使用二分查找进行匹配处理
		//1、处理学号
		long st1 = System.currentTimeMillis();
		StringBuffer numStr = new StringBuffer(2000);
		int inNum = 1;
		StudentPaymentVo s;
//		数据检查：
//		1、应缴金额是否等于实缴费+欠费
//		2、都不能小于0
//		3、实缴金额=0不导入
		for(Iterator it = modelList.iterator();it.hasNext();){
			s = (StudentPaymentVo)it.next();
			Double recpayFee = s.getRecpayFee()==null?0:s.getRecpayFee();// 应缴金额
			Double facepayFee = s.getFacepayFee()==null?0:s.getFacepayFee();// 实缴金额
			Double unpaidFee = s.getUnpaidFee()==null?0:s.getUnpaidFee();// 未缴金额
			if(!isDirectImport){
				if(recpayFee<0||facepayFee<0||unpaidFee<0){
					s.setFalseMsg("应缴、实缴、欠费金额不能小于0");
					failList.add(s);
					it.remove();
					continue;
				}
				if(facepayFee==0 && !isDirectImport){
					s.setFalseMsg("实缴金额为0，不执行导入");
					failList.add(s);
					it.remove();
					continue;
				}
			}
			if(recpayFee!=(facepayFee+unpaidFee)){
				s.setFalseMsg("应缴金额=实缴金额+欠费金额");
				failList.add(s);
				it.remove();
				continue;
			}
		}
		ComparatorSPVO StudentPaymentvoList=  new ComparatorSPVO();
		long et1 = System.currentTimeMillis();
		Collections.sort(modelList, StudentPaymentvoList);
		long st2 = System.currentTimeMillis();
		logger.info("排序花费时间："+(st2-et1)/1000f+"秒");
		//处理数据进行查询
		for(int i=0;i<modelList.size();i++){
			if(i==modelList.size()-1){
				numStr.append("'"+modelList.get(i).getStudyNo()+"'");
			}else if(inNum==1000&& i>0){
				numStr.append("'"+modelList.get(i).getStudyNo()+"') or f.studentInfo.studyNo in ( ");
				inNum = 1;
			}else{
				numStr.append("'"+modelList.get(i).getStudyNo()+"',");
				inNum++;
			}
		}
		int successCount = 0;
//		String sqlfee = "select * from EDU_FEE_STUFEE f where f.isdeleted=0 and f.studyno in ("+numStr.toString()+" ) order by f.studyno";
		try {
			long st = System.currentTimeMillis();
			//过滤掉  退学及毕业的学生
			//List<StudentPayment> list = findByHql("from "+StudentPayment.class.getSimpleName()+" f where  f.studentInfo.studyNo in ("+numStr.toString()+" ) and f.studentInfo.studentStatus in('11','12','24','25') and f.isDeleted=0 order by f.studentInfo.studyNo", new Object[]{});
			StringBuffer hql = new StringBuffer();
			List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();
			List<StudentPayment> studentPaymentList = new ArrayList<StudentPayment>();
			//Map<String, Object> param = new HashMap<String, Object>();
			
			//	1、系统是否存在学生   注：只是提示而已，意义不大，但如果处理，比较麻烦，因此不作处理
			//	2、是否生成缴费记录
			//	3、姓名是否正确
			//	4、应缴金额与系统的记录是否一致
			if(modelList!=null && modelList.size()>0){
				hql.append("from "+StudentPayment.class.getSimpleName()+" f where f.studentInfo.studyNo in ("+numStr.toString()+" ) and f.studentInfo.studentStatus in('11','12','24','25') and f.isDeleted=0 order by f.studentInfo.studyNo");
				studentPaymentList = findByHql(hql.toString(), new Object[]{});
				
				if(isDirectImport){
//					直接导入缴费信息：查找没有生成缴费记录的学生并生成缴费记录
					hql.setLength(0);
					hql.append("from "+StudentInfo.class.getSimpleName()+" si ");
					hql.append(" where si.studyNo in ("+numStr.toString().replaceAll("f.studentInfo", "si")+") and si.studentStatus in('11','12','24','25')");
					hql.append(" and si.resourceid not in(select f.studentInfo.resourceid from StudentPayment f where f.isDeleted=0 and f.studentInfo.studyNo in ("+numStr.toString()+"))  order by si.studyNo");
					studentInfoList = studentInfoService.findByHql(hql.toString(),  new Object[]{});
					successCount = saveOrUpdateByStudentInfo(modelList,studentInfoList,failList,payType,user);
				}
				long et = System.currentTimeMillis();
				//logger.debug("共查询到："+(studentPaymentList.size()+studentInfoList.size())+" 条数据  执行时间为： "+(et-st)/1000f +" 秒");
				saveOrUpdateByStudentPayment(modelList,studentPaymentList,failList,payType,user);
				
				long endtime1 = System.currentTimeMillis();
				logger.debug("数据检查花费共： "+(et1-st1)/1000f+"秒");
				logger.debug("业务逻辑检查共："+(endtime1-st2)/1000f+"秒");
				logger.debug("总共消耗时间："+(endtime1-st1)/1000f+"秒");
			}
		} catch (Exception e) {
			logger.error("处理学生缴费信息导入出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("导入学生缴费信息出错！");
		} finally {
			msg.append("<br>共导入学生缴费记录：<font color='green'>"+(successCount+modelList.size()+failList.size())+" </font> 条 <br> 失败记录：<font color='red'>"+failList.size()+" </font> 条");
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
			if(failList!=null && failList.size()>0){
				retrunMap.put("failRecord", failList);
			}
		}
		return retrunMap;
	}
	
	/**
	 * 查找学生学籍信息并更新缴费表
	 * @param modelList
	 * @param list
	 * @param failList
	 * @param payType
	 * @param user
	 * @return 
	 */
	private int saveOrUpdateByStudentInfo(List<StudentPaymentVo> modelList, List<StudentInfo> list, List<StudentPaymentVo> failList, Integer payType, User user) {
		List<StudentPayment> updateList = new ArrayList<StudentPayment>();// 缴费记录更新列表
		List<StudentPaymentDetails> addList = new ArrayList<StudentPaymentDetails>();// 缴费明细新增列表
		List<StudentPaymentVo> removeList = new ArrayList<StudentPaymentVo>();
		for (StudentPaymentVo vo : modelList){
			String studyNO =vo.getStudyNo();
			String studentName =vo.getName().trim();
			Double recpayFee =vo.getRecpayFee()==null?0:vo.getRecpayFee();//应缴金额
			Double facepayFee = vo.getFacepayFee()==null?0:vo.getFacepayFee();// 实缴金额
			boolean isFound = true;
			for(Iterator it = list.iterator();it.hasNext();){
				StudentInfo si = (StudentInfo)it.next();
				if(si.getStudyNo().equalsIgnoreCase(studyNO)){
					isFound=true;
					if(!studentName.equals(si.getStudentName())){
						vo.setFalseMsg("姓名不匹配，学生姓名在系统中名为："+si.getStudentName());
						it.remove();
						failList.add(vo);
						break;
					}else{
						StudentPayment sp = new StudentPayment();
						sp.setStudentInfo(si);
						sp.setStudyNo(si.getStudyNo());
						sp.setName(si.getStudentName());
						sp.setBranchSchool(si.getBranchSchool());
						sp.setGrade(si.getGrade());
						sp.setClassic(si.getClassic());
						sp.setMajor(si.getMajor());
						sp.setTeachingType(si.getTeachingType());
						sp.setChargeTerm(1);
						sp.setDerateFee(0.0);
						sp.setIsDefer("N");
						sp.setIsDeleted(0);
						sp.setIsMultiDefer("N");
						sp.setChargeTime(new Date());
						sp.setFacepayFee(facepayFee);
						sp.setRecpayFee(recpayFee);
						sp.setEnableLogHistory(true);
						if(facepayFee==0){
							sp.setChargeStatus("0");//未缴费
						}else if(facepayFee<recpayFee){
							sp.setChargeStatus("-1");//欠费
						}else{
							sp.setChargeStatus("1");//当前已缴清
						}
						//学生缴费明细
						StudentPaymentDetails studentPaymentDetails = new StudentPaymentDetails();
						studentPaymentDetails.setStudentInfo(sp.getStudentInfo());
						studentPaymentDetails.setOperateDate(new Date());
						studentPaymentDetails.setPayType(payType);// 缴费类型
						studentPaymentDetails.setPayAmount(facepayFee);// 缴费金额
						studentPaymentDetails.setPaidAmount(facepayFee);
						studentPaymentDetails.setShouldPayAmount(recpayFee);
						studentPaymentDetails.setOperatorName(user.getCnName());
						studentPaymentDetails.setOperatorId(user.getResourceid());
						updateList.add(sp);
						addList.add(studentPaymentDetails);
						removeList.add(vo);
						break;
					}
				}else{
					isFound=false;
				}					
			}
			/*if(!isFound){
				vo.setFalseMsg("缴费模块中未找到该学生：【学号不正确】或【已退学】或【已毕业】");
				failList.add(vo);
			}*/
		}
		//删除已添加的缴费信息
		modelList.removeAll(removeList);
		//删除不匹配的缴费信息
		modelList.removeAll(failList);
		// 更新学生缴费记录
		batchSaveOrUpdate(updateList);
		// 保存学生缴费明细
		studentPaymentDetailsService.batchSaveOrUpdate(addList);
		logger.info("共导入学生缴费记录（无缴费记录）："+(modelList.size()+removeList.size())+" 条    失败记录:"+modelList.size());
		return removeList.size();
	}

	/**
	 * 查找学生缴费信息并更新缴费表
	 * @param modelList
	 * @param list
	 * @param failList
	 * @param payType
	 * @param user
	 * @return 
	 */
	private void saveOrUpdateByStudentPayment(List<StudentPaymentVo> modelList, List<StudentPayment> list,List<StudentPaymentVo> failList, Integer payType, User user) {
		boolean isDirectImport = "Y".equals(CacheAppManager.getSysConfigurationByCode("payment.isDirectImport").getParamValue().trim());
		List<StudentPayment> updateList = new ArrayList<StudentPayment>();// 缴费记录更新列表
		List<StudentPaymentDetails> addList = new ArrayList<StudentPaymentDetails>();// 缴费明细新增列表
		for (StudentPaymentVo vo : modelList){
			String studyNO =vo.getStudyNo();
			String studentName =vo.getName().trim();
			Double recpayFee =vo.getRecpayFee()==null?0:vo.getRecpayFee();//应缴金额
			Double facepayFee = vo.getFacepayFee()==null?0:vo.getFacepayFee();// 实缴金额
			boolean isFound = true;
			for(Iterator it = list.iterator();it.hasNext();){
				StudentPayment sp = (StudentPayment)it.next();
				if(sp.getStudyNo().equalsIgnoreCase(studyNO)){
					isFound=true;
					if(!studentName.equals(sp.getName())){
						vo.setFalseMsg("姓名不匹配，学生姓名在系统中名为："+sp.getName());
						it.remove();
						failList.add(vo);
						break;
					}else{
						if(!sp.getRecpayFee().equals(recpayFee) && !isDirectImport){
							vo.setFalseMsg("表格中应缴金额与系统不一致，系统当前应缴金额为："+sp.getRecpayFee());
							it.remove();
							failList.add(vo);
							break;
						}else{
							Double _facepayFee = 0d;//待更新的实缴金额
							Double _sysPaidFee = sp.getFacepayFee()==null?0:sp.getFacepayFee();// 系统中实缴金额
							Double _derateFee = sp.getDerateFee()==null?0:sp.getDerateFee();// 减免金额
							if(payType==2){// 叠加
								sp.setFacepayFee(_sysPaidFee + facepayFee);// 实缴金额
								_facepayFee = _sysPaidFee + facepayFee+_derateFee;
							} else {
								sp.setFacepayFee(facepayFee);
								_facepayFee = facepayFee+_derateFee;
							}
							sp.setEnableLogHistory(true);
							if(_facepayFee==0){
								sp.setChargeStatus("0");//未缴费
							}else if(_facepayFee<recpayFee){
								sp.setChargeStatus("-1");//欠费
							}else{
								sp.setChargeStatus("1");//当前已缴清
							}
							if(isDirectImport) {
								sp.setChargeTerm((sp.getChargeYear()==null?0:sp.getChargeYear())+1);//更新缴费期数
							}
							//学生缴费明细
							StudentPaymentDetails studentPaymentDetails = new StudentPaymentDetails();
							studentPaymentDetails.setStudentInfo(sp.getStudentInfo());
							studentPaymentDetails.setOperateDate(new Date());
							studentPaymentDetails.setPayType(payType);// 缴费类型
							if(payType==2){// 叠加
								studentPaymentDetails.setPayAmount(facepayFee);// 缴费金额
								studentPaymentDetails.setPaidAmount(_sysPaidFee+facepayFee);// 已缴金额
							}else {// 直接更新
								studentPaymentDetails.setPayAmount(_sysPaidFee==0?facepayFee:((facepayFee-_sysPaidFee)<0?0:(facepayFee-_sysPaidFee)));// 缴费金额
								studentPaymentDetails.setPaidAmount(facepayFee);
							}
							studentPaymentDetails.setShouldPayAmount(recpayFee);
							studentPaymentDetails.setOperatorName(user.getCnName());
							studentPaymentDetails.setOperatorId(user.getResourceid());
							updateList.add(sp);
							addList.add(studentPaymentDetails);
							break;
						}
					}
				}else{
					isFound=false;
				}					
			}
			if(!isFound || list.size()==0){
				if(isDirectImport){
					vo.setFalseMsg("缴费模块中未找到该学生：【学号不正确】或【已退学】或【已毕业】");
				}else {
					vo.setFalseMsg("缴费模块中未找到该学生：【未生成缴费记录】或【已退学】或【已毕业】");
				}
				failList.add(vo);
			}
		}
		modelList.removeAll(failList);
		// 更新学生缴费记录
		batchSaveOrUpdate(updateList);
		// 保存学生缴费明细
		studentPaymentDetailsService.batchSaveOrUpdate(addList);
		logger.info("共导入学生缴费记录（有缴费记录）："+modelList.size()+" 条    失败记录:"+failList.size());
	}
	
	public class ComparatorSPVO implements Comparator{

		 @Override
		 public int compare(Object arg0, Object arg1) {
			 StudentPaymentVo vo1=(StudentPaymentVo)arg0;
			 StudentPaymentVo vo2=(StudentPaymentVo)arg1;
		   //首先比较年龄，如果年龄相同，则比较名字
		  return vo1.getStudyNo().compareTo(vo2.getStudyNo());
		 
		 }
	}
	
	/**
	 * 缴费--学费
	 * @param studentPayment
	 * @param payDetailsVO
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	@Override
	@Transactional
	public Map<String, Object> payFee(StudentPayment studentPayment,PayDetailsVO payDetailsVO, User user) throws ServiceException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			do{
				Double facepay = payDetailsVO.getPayAmount()==null?0:payDetailsVO.getPayAmount();
				if(facepay <= 0){
					statusCode = 300;
					message = "缴费金额不能小于或等于0！";
					continue;
				}
			
				// 学费
				// 获取缴费类型全局参数
				SysConfiguration payTypeConf = CacheAppManager.getSysConfigurationByCode("payment.payType");
				Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
				if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
						&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){
					payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
				}
				
				Double _sysPaidFee = studentPayment.getFacepayFee()==null?0:studentPayment.getFacepayFee();
				Double recpayFee = studentPayment.getRecpayFee();// 应缴金额
				Double derateFee = studentPayment.getDerateFee();// 减免金额
				
				Double _facepayFee = 0d;
				Double _facepay = 0d;
				if(payType==2){// 叠加
					_facepay = _sysPaidFee + facepay;// 实缴金额
					_facepayFee = _sysPaidFee + facepay+derateFee;// 实际要缴的金额
				} else {
					_facepay = facepay;
					_facepayFee = facepay+derateFee;// 实际要缴的金额
				}
				
				if(recpayFee < _facepayFee){
					statusCode = 300;
					message = "缴费金额+减免金额不能大于应缴金额！";
					continue;
				}
				
				studentPayment.setFacepayFee(_facepay);// 实缴金额
				
				if(_facepayFee == 0){
					studentPayment.setChargeStatus("0");// 未缴费
				}else if(recpayFee  > _facepayFee){
					studentPayment.setChargeStatus("-1");// 欠费
				}else {
					studentPayment.setChargeStatus("1");// 已缴费
				}
				
				studentPayment.setEnableLogHistory(true);//记录日志
				// 更新缴费记录
				update(studentPayment);
				// 新增缴费明细
				Date today = new Date();
				StudentPaymentDetails studentPaymentDetails = new StudentPaymentDetails();
				StudentInfo _stuInfo = studentPayment.getStudentInfo();
				studentPaymentDetails.setEduOrederNo(payDetailsVO.getEduOrederNo());
				studentPaymentDetails.setStudentInfo(_stuInfo);
				studentPaymentDetails.setShouldPayAmount(recpayFee);
				studentPaymentDetails.setPayType(payType);// 缴费类型
				studentPaymentDetails.setCreateDate(today);
				if(payDetailsVO.getPayTime()!=null){
					today = payDetailsVO.getPayTime();
				}
				studentPaymentDetails.setOperateDate(today);
				studentPaymentDetails.setPayNo(payDetailsVO.getPayNo());
				if(payType==2){// 叠加
					studentPaymentDetails.setPayAmount(facepay);// 缴费金额
					studentPaymentDetails.setPaidAmount(_sysPaidFee+facepay);// 已缴金额
				}else {// 直接更新
					if((facepay-_sysPaidFee)<0){
						statusCode = 300;
						message = "缴费金额不能小于上次缴费金额！";
						continue;
					}
					studentPaymentDetails.setPayAmount(_sysPaidFee==0?facepay:((facepay-_sysPaidFee)<0?0:(facepay-_sysPaidFee)));// 缴费金额
					studentPaymentDetails.setPaidAmount(facepay);
				}
				int _year = studentPayment.getYearInfo().getFirstYear().intValue()+studentPayment.getChargeTerm()-1;
				studentPaymentDetails.setYear(_year+"");
				studentPaymentDetails.setPosSerialNumber(payDetailsVO.getPosSerialNumber());
				studentPaymentDetails.setReceiptNumber(payDetailsVO.getReceiptNumber());
				studentPaymentDetails.setCarrCardNo(payDetailsVO.getCarrCardNo());
				studentPaymentDetails.setCarrTermNum(payDetailsVO.getCarrTermNum());
				studentPaymentDetails.setPaymentMethod(payDetailsVO.getPaymentMethod());
				studentPaymentDetails.setCheckPayable(payDetailsVO.getCheckPayable());
				studentPaymentDetails.setMemo(payDetailsVO.getMemo());
				studentPaymentDetails.setOperatorName(user.getCnName());
				studentPaymentDetails.setOperatorId(user.getResourceid());
				studentPaymentDetails.setChargeMoney(payDetailsVO.getChargeMoney());
				studentPaymentDetails.setClassName(_stuInfo.getGrade().getGradeName()+_stuInfo.getMajor().getMajorName()+_stuInfo.getClassic().getClassicName());
				// 收费项
				studentPaymentDetails.setChargingItems(payDetailsVO.getChargingItems());
				// 是否开单位发票
				studentPaymentDetails.setIsInvoicing(payDetailsVO.getIsInvoicing());
				// 单位名称
				studentPaymentDetails.setInvoiceTitle(payDetailsVO.getInvoiceTitle());
				studentPaymentDetailsService.saveOrUpdate(studentPaymentDetails);
				// 更新学生年度缴费信息
				AnnualFees annualFees = annualFeesService.findUnique("from "+AnnualFees.class.getSimpleName()+" af where af.isDeleted=0 and af.studentInfo.resourceid=? and af.chargeYear=? and af.chargingItems=?", 
						_stuInfo.getResourceid(),_year,payDetailsVO.getChargingItems());
				if(annualFees!=null){
					annualFees.setFacepayFee(facepay);
					int chargeStatus = 0;
					if(annualFees.getRecpayFee().doubleValue()==facepay.doubleValue()){
						chargeStatus = 1;
					}else if(annualFees.getRecpayFee().doubleValue()>facepay.doubleValue()){
						chargeStatus = -1;
					} 
					annualFees.setChargeStatus(chargeStatus);
					annualFeesService.saveOrUpdate(annualFees);
				} 
				
				returnMap.put("studentPaymentDetails", studentPaymentDetails);
			} while(false);
		} catch (Exception e) {
			logger.error("缴费出错：", e);
			statusCode = 300;
			message = "缴费失败！";
		}finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message);
		}
		return returnMap;
	}

	/**
	 * 验证缴费金额
	 * @param facepay
	 * @param studentPayment
	 * @return
	 */
	@Override
	public Map<String, Object> validateFacepay(Double facepay, StudentPayment studentPayment) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		do{
			// 获取缴费类型全局参数
			SysConfiguration payTypeConf = CacheAppManager.getSysConfigurationByCode("payment.payType");
			Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
			if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
					&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){
				payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
			}
			
			Double _sysPaidFee = studentPayment.getFacepayFee()==null?0:studentPayment.getFacepayFee();
			Double recpayFee = studentPayment.getRecpayFee();// 应缴金额
			Double derateFee = studentPayment.getDerateFee();// 减免金额
			
			Double _facepayFee = 0d;
			if(payType==2){// 叠加
				_facepayFee = _sysPaidFee + facepay+derateFee;// 实际要缴的金额
			} else {
				_facepayFee = facepay+derateFee;// 实际要缴的金额
			}
			
			if(recpayFee < _facepayFee){
				statusCode = 300;
				message = "缴费金额+减免金额不能大于应缴金额！";
				continue;
			}
		} while(false);
		
		returnMap.put("statusCode", statusCode);
		returnMap.put("message", message);
		
		return returnMap;
	}


	/**
	 * 网上缴费
	 * 
	 * 首次缴费则包括注册
	 * 
	 * @param majorKey
	 * @param uniqueId
	 * @param user
	 * @param payDetailsVO
	 * @param tempStudentFee
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> payOnline(String majorKey, int uniqueId,User user, PayDetailsVO payDetailsVO,TempStudentFee tempStudentFee) {
		Map<String, Object> payResultMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			do {
				String propertyName = "examCertificateNo";
				if (uniqueId == 1) {
					propertyName = "enrolleeCode";
				}
				// 先判断该学生是否已有学籍
				StudentInfo studentInfo = studentInfoService.findUniqueByProperty(propertyName, majorKey);
				StudentPayment payment = null;
				if(studentInfo == null){
					EnrolleeInfo ei = enrolleeInfoService.findUniqueByProperty(propertyName, majorKey);
					String[] eiIds = new String[] { ei.getResourceid() };
					// 注册
					List<StudentInfo> registeredStus = reginfoService.doRegister(eiIds);
					if (ExCollectionUtils.isNotEmpty(registeredStus)) {
						studentInfo = registeredStus.get(0);
						/*~~~~~~~~~~~~~~预约学生首学期课程~~~~~~~~~~*/
//						reginfoService.doRegisterStuFirstTermCourse(registeredStus);
						// 给学生生成缴费记录
						Map<String, Object> createPaymentMap = generateStudentFeeRecord(registeredStus);
						statusCode = (Integer) createPaymentMap.get("statusCode");
						if ( statusCode == 300) {
							message = (String) createPaymentMap.get("message");
							continue;
						} 
						// 获取学生缴费标准信息
						List<StudentPayment> paymentList = (List<StudentPayment>)createPaymentMap.get("paymentList");
						if(ExCollectionUtils.isNotEmpty(paymentList)){
							payment = paymentList.get(0);// TODO: 由于这个是一个学生一个学生处理的，就这样获取，否则使用学生匹配获取
						}
					}
				}else{
					payment = findUnique("from "+ StudentPayment.class.getSimpleName()+ " where isDeleted=0 and studentInfo.resourceid=?",studentInfo.getResourceid());
				}
				
//				StudentPayment payment = findUnique("from "+ StudentPayment.class.getSimpleName()+ " where isDeleted=0 and studentInfo.resourceid=?",studentInfo.getResourceid());
				if(payment==null){
					statusCode = 300;
					message =studentInfo.getStudyNo()+"该学生没有生成缴费记录";
					continue;
				}
				
				if(payDetailsVO == null ){
					payDetailsVO = new PayDetailsVO();
				}
				// 抬头
				payDetailsVO.setCheckPayable(studentInfo.getStudentName()+"(学号"+studentInfo.getStudyNo()+")");
				Map<String, Object> returnMap = payFee(payment,payDetailsVO, user);
				statusCode = (Integer) returnMap.get("statusCode");
				if (statusCode == 200) {
					// 更改缴费信息状态
					if(tempStudentFee!=null){
						tempStudentFee.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
						tempStudentFeeService.saveOrUpdate(tempStudentFee);
					}
				}else {
					message = (String) returnMap.get("message");
				}
			} while (false);
		} catch (Exception e) {
			statusCode = 300;
			message = "网上缴费注册失败";
			logger.error("网上缴费注册出错", e);
		} finally {
			payResultMap.put("statusCode", statusCode);
			payResultMap.put("message", message);
		}
		
		return payResultMap;
	}



	/**
	 * 处理学生缴费信息导入（右江医）
	 * @param modelList
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> handleStudentPaymentImport1(List<StudentPaymentVo> modelList, User user) throws ServiceException {
		Map<String, Object> retrunMap = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer msg = new StringBuffer(""); // 出错信息
		List<StudentPayment> updateList = new ArrayList<StudentPayment>();// 缴费记录更新列表
		List<StudentPaymentDetails> addList = new ArrayList<StudentPaymentDetails>();// 缴费明细新增列表
		List<StudentPaymentVo> falseList =new ArrayList<StudentPaymentVo>(); //错误的集合
		
		try {
			// 获取缴费类型全局参数
			SysConfiguration payTypeConf = CacheAppManager.getSysConfigurationByCode("payment.payType");
			Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
			if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
					&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){
				payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
			}
			if(ExCollectionUtils.isNotEmpty(modelList)){
				Map<String ,Object> condition = null;
				String studyNo=null;
				String certNum=null;
				String name= null;
				StudentInfo studentInfo = null;
//				StudentBaseInfo studentBaseInfo=null; //学生的基础信息，身份号
				StudentPaymentDetails studentPaymentDetails =null;
				StudentPayment studentPayment =null;
				for (StudentPaymentVo s : modelList) {
					condition = new HashMap<String, Object>();
//					studyNo= s.getStudyNo();
					condition.clear();
					certNum=s.getCertNum();
					name= ExStringUtils.defaultIfEmpty(s.getName(), "");
					condition.put("certNum", certNum);
					condition.put("name", name);
					// 检验该学生是否存在
//					studentInfo = studentInfoService.findUniqueStudentInfo(condition);
					List<Object> paramValues = new ArrayList<Object>(); 
						paramValues.add(condition.get("certNum"));
						paramValues.add(condition.get("name"));
//					studentBaseInfo =studentBaseInfoService.findUnique(" from "+StudentBaseInfo.class.getSimpleName()+" so where so.isDeleted=0 and so.certNum=? and so.name=? and so.rownum=1", paramValues.toArray());
					List<StudentBaseInfo> studentBaseInfos =studentBaseInfoService.findByHql(" from "+StudentBaseInfo.class.getSimpleName()+" so where so.isDeleted=0 and so.certNum=? and so.name=? ", paramValues.toArray());
					if(studentBaseInfos == null||studentBaseInfos.size()<=0){
//						msg.append("身份证号与学生名字不匹配");
						s.setFalseMsg("身份证号与学生名字不匹配");
						falseList.add(s);
						continue;
					}
					condition.clear();
					condition.put("name", s.getName());
					condition.put("gradeName", s.getGradeName());
					paramValues.clear();
					paramValues.add(condition.get("name"));
					paramValues.add(condition.get("gradeName"));
					
					studentInfo=studentInfoService.findUnique(" from "+StudentInfo.class.getSimpleName()+" so  where so.isDeleted=0  and so.studentName=? and so.grade.gradeName=? ", paramValues.toArray());
					if(studentInfo==null){
//						msg.append("学号为："+studyNo+"这位学生的姓名有误<br>");
						s.setFalseMsg("这位学生年级有误");
						falseList.add(s);
						continue;
					}
					// 检验导入的金额是否有误
					Double recpayFee = s.getRecpayFee()==null?0:s.getRecpayFee();// 应缴金额
					Double facepayFee = s.getFacepayFee()==null?0:s.getFacepayFee();// 实缴金额
					Double unpaidFee = s.getUnpaidFee()==null?0:s.getUnpaidFee();// 未缴金额
					if(unpaidFee<0){//未交金额如果为负数，则转为正数
						unpaidFee=-unpaidFee;
					}
					if(recpayFee <= 0){
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的应缴金额不能小于或等于0<br/>");
						s.setFalseMsg("这位学生的应缴金额不能小于或等于0");
						falseList.add(s);
						continue;
					}
					if(facepayFee <= 0){
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的实缴金额不能小于或等于0<br>");
						s.setFalseMsg("这位学生的实缴金额不能小于或等于0");
						falseList.add(s);
						continue;
					}
					/*if(unpaidFee < 0){
						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的未缴金额不能小于0<br>");
						continue;
					}*/
					if(recpayFee!=(facepayFee+unpaidFee)){
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的数据有误[应缴金额不等于(实缴金额+欠费金额)]<br>");
						s.setFalseMsg("这位学生的数据有误[应缴金额不等于(实缴金额+欠费金额)]");
						falseList.add(s);
						continue;
					}
					condition.clear();
					condition.put("studentInfoId", studentInfo.getResourceid());
					List<StudentPayment> studentPaymentList = findStudentPaymentByCondition(condition);
					if(ExCollectionUtils.isEmpty(studentPaymentList)){
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生还没有生成缴费记录<br>");
						s.setFalseMsg("这位学生的数据有误[应缴金额不等于(实缴金额+欠费金额)]");
						falseList.add(s);
						continue;
					}
					if(studentPaymentList.size() > 1){
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的缴费记录有多条<br>");
						s.setFalseMsg("这位学生的缴费记录有多条");
						falseList.add(s);
						continue;
					}
					studentPayment = studentPaymentList.get(0);
					Double _sysPaidFee = studentPayment.getFacepayFee()==null?0:studentPayment.getFacepayFee();// 系统中实缴金额
					Double _recpayFee = studentPayment.getRecpayFee();// 系统中的应缴金额
					Double _derateFee = studentPayment.getDerateFee()==null?0:studentPayment.getDerateFee();// 减免金额
					if(!_recpayFee.equals(recpayFee)){
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的应缴金额与系统中的应缴金额有误差<br>");
						s.setFalseMsg("这位学生的应缴金额与系统中的应缴金额有误差");
						falseList.add(s);
						continue;
					}
					Double _facepayFee = 0d;
					if(payType==2){// 叠加
						studentPayment.setFacepayFee(_sysPaidFee + facepayFee);// 实缴金额
						_facepayFee = _sysPaidFee + facepayFee+_derateFee;
					} else {
						studentPayment.setFacepayFee(facepayFee);
						_facepayFee = facepayFee+_derateFee;
					}
					
					studentPayment.setRecpayFee(recpayFee);
					if(_facepayFee == 0){
						studentPayment.setChargeStatus("0");// 未缴费
					}else if(recpayFee>_facepayFee){
						studentPayment.setChargeStatus("-1");// 欠费
					}else if(_facepayFee < recpayFee) {
//						msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的数据有误[(缴费金额+减免金额)不能大于应缴金额]！<br>");
						s.setFalseMsg("这位学生的数据有误[(缴费金额+减免金额)不能大于应缴金额]");
						falseList.add(s);
						continue;
					} else {
						studentPayment.setChargeStatus("1");// 已缴费
					}
					studentPayment.setEnableLogHistory(true);
					
					//学生缴费明细
					studentPaymentDetails = new StudentPaymentDetails();
					studentPaymentDetails.setStudentInfo(studentInfo);
					studentPaymentDetails.setOperateDate(new Date());
					studentPaymentDetails.setPayType(payType);// 缴费类型
					if(payType==2){// 叠加
						studentPaymentDetails.setPayAmount(facepayFee);// 缴费金额
						studentPaymentDetails.setPaidAmount(_sysPaidFee+facepayFee);// 已缴金额
					}else {// 直接更新
						if((facepayFee-_sysPaidFee)<0){
							statusCode = 300;
//							msg.append("学号为："+studyNo+",姓名为："+name+"这位学生的缴费金额不能小于上次缴费金额！");
							s.setFalseMsg("这位学生的缴费金额不能小于上次缴费金额！");
							falseList.add(s);
							continue;
						}
						studentPaymentDetails.setPayAmount(_sysPaidFee==0?facepayFee:((facepayFee-_sysPaidFee)<0?0:(facepayFee-_sysPaidFee)));// 缴费金额
						studentPaymentDetails.setPaidAmount(facepayFee);
					}
					
					studentPaymentDetails.setShouldPayAmount(recpayFee);
					studentPaymentDetails.setOperatorName(user.getCnName());
					studentPaymentDetails.setOperatorId(user.getResourceid());
					updateList.add(studentPayment);
					addList.add(studentPaymentDetails);
				}
				// 更新学生缴费记录
				batchSaveOrUpdate(updateList);
				// 保存学生缴费明细
				studentPaymentDetailsService.batchSaveOrUpdate(addList);
			}
		} catch (Exception e) {
			logger.error("处理学生缴费信息导入出错", e);
			statusCode = 300;
			msg.setLength(0);
			msg.append("导入学生缴费信息失败！");
		} finally {
			if(msg.length() >0) {
				statusCode = 300;
			}
			retrunMap.put("statusCode", statusCode);
			retrunMap.put("message", msg.toString());
			retrunMap.put("falseList", falseList);
		}
		return retrunMap;
	}

	/**
	 * 获取对账单（通过通联接口）
	 * @param serverUrl
	 * @param tradeDate
	 * @param merNo
	 * @param key
	 * @return
	 */
	@Override
	public Map<String, Object> getStatementByDate(String serverUrl,String tradeDate, String merNo, String key) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		HttpURLConnectionTL httpConnTL = new HttpURLConnectionTL();
		StringBuffer orderListContent = new StringBuffer();
		String mac = null;
		// 定义标志位 0表示初始位 1表示读取签名位
		int flag = 0;
		try{
			String signMsg = MD5CryptorUtils.getMD5ofStr(merNo + tradeDate + key);
			URL url = new URL(serverUrl + "?merNo=" + merNo + "&tradeDate=" + tradeDate + "&signMsg=" + signMsg);
			HttpURLConnection httpConn = httpConnTL.getHttpsURLConnection(url);
			httpConn.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
			String lines;
			int i = 0;
			CheckFileTotalVO totalVO = null;
			List<CheckFileVO> checkFileVOList = new ArrayList<CheckFileVO>();
			CheckFileVO checkFileVO = null;
			BigDecimal hundred = new BigDecimal(100);
			BigDecimal thousand = new BigDecimal(1000);
			while ((lines = reader.readLine()) != null) {
				if (flag == 1) {
					mac = lines;
					break;
				}
				if (lines.length() > 0) {
					orderListContent.append(lines + "\r\n");
					// 放在excel文件中
					if(i==0){
						if(lines.startsWith("ERRORCODE")){// 获取有误的
							statusCode = 300;
							returnMap.put("msg", lines.split(" ")[1].split(":")[1]);
							break;
						}else{
							String[] totalArray = lines.split("\\|");
							totalVO = new CheckFileTotalVO();
							totalVO.setTradeDate(totalArray[0]);
							totalVO.setTradeItems(Integer.parseInt(totalArray[1]));
							totalVO.setTotalAmount((new BigDecimal(totalArray[2]).divide(hundred)).setScale(2, BigDecimal.ROUND_HALF_UP));
							totalVO.setTotalFee((new BigDecimal(totalArray[3]).divide(hundred)).setScale(2, BigDecimal.ROUND_HALF_UP));
						}
					}else{
						String[] orderArray = lines.split("\\|"); 
						checkFileVO = new CheckFileVO();
						checkFileVO.setEduOrederNo(orderArray[0]);
						checkFileVO.setPayMethod(TonlyPayUtil.convertPayMethod(orderArray[1]));
						BigDecimal payAmount = (new BigDecimal(orderArray[2]).divide(hundred)).setScale(2, BigDecimal.ROUND_HALF_UP);
						checkFileVO.setPayAmount(payAmount.doubleValue());
						BigDecimal fee = BigDecimal.ZERO;
						if("10".equals(orderArray[1])){// TODO: 目前POS支付方式还没有手续费
							fee = payAmount.divide(thousand).setScale(2, BigDecimal.ROUND_HALF_UP);
						}else{
							if(ExStringUtils.isNotEmpty(orderArray[3])){
								fee = (new BigDecimal(orderArray[3]).divide(hundred)).setScale(2, BigDecimal.ROUND_HALF_UP);
							}
						}
						checkFileVO.setFee(fee.doubleValue());
						checkFileVO.setPayTime(ExDateUtils.formatDateStr(ExDateUtils.convertToDateTimeCombine(orderArray[4]), ExDateUtils.PATTREN_DATE_TIME));
						checkFileVO.setStudyNo(orderArray[5]);
						checkFileVO.setOrderNo(orderArray[6]);
						checkFileVO.setPayNo(orderArray[7]);
						checkFileVOList.add(checkFileVO);
					}
					i++;
				} else {
					flag = 1;
				}
			}
			// 关闭BufferedReader
			reader.close();
			// 断开连接
			httpConn.disconnect();
			// 下载对账文件
			// 验证
			if(statusCode==200){
				if(verifySign(orderListContent.toString().trim()+ "&" + key, mac)){
					returnMap.put("checkFileTotal", totalVO);
					returnMap.put("checkFileList", checkFileVOList);
				}else{
					statusCode = 300;
					returnMap.put("msg", "验证失败");
				}
			}
		} catch(Exception e){
			statusCode = 300;
			returnMap.put("msg", "下载对账文件失败");
			logger.error("获取对账单（通过通联接口）逻辑出错", e);
		} finally{
			returnMap.put("statusCode", statusCode);
		}
		
		return returnMap;
	}
	
	/**
	 * 验证账单内容
	 * @param srcStr
	 * @param mac
	 * @return
	 */
	private static boolean verifySign(String srcStr, String mac) {
		if (MD5CryptorUtils.getMD5ofStr(srcStr).equals(mac)){
			return true;
		}else{
			return false;
		}
	}



	@Override
	public List<StuReturnFeeCommissionInfoVo> getStuFeeCommissionInfo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select distinct u.unitname,g.gradename,ci.classicname,m.majorname,si.studyno,si.studentname,nvl(bs.contactphone,bs.mobile) mobile,");
		sql.append(" si.indate,sci.applicationdate,nvl(af.recpayfee,pyd.creditfee) creditfee,u.royaltyrate,");
		//sql.append(" sci.changebeforeschoolname bankName,sci.changebeforemajorname bankAddress,sci.changebeforeclassicname cardNo");
		sql.append(" sci.bankName,sci.bankAddress,sci.cardNo");
		sql.append(" from edu_roll_studentinfo si join hnjk_sys_unit u on u.resourceid=si.branchschoolid and u.isdeleted=0");
		sql.append(" join edu_base_grade g on g.resourceid=si.gradeid and g.isdeleted=0");
		sql.append(" join edu_base_classic ci on ci.resourceid=si.classicid and ci.isdeleted=0");
		sql.append(" join edu_base_major m on m.resourceid=si.majorid and m.isdeleted=0");
		sql.append(" join edu_roll_stuchangeinfo sci on sci.studentid=si.resourceid and sci.isdeleted=0");
		sql.append(" join edu_base_student bs on bs.resourceid=si.studentbaseinfoid");
		sql.append(" left join edu_fee_annualfees af on af.studentid=si.resourceid and af.chargeyear=to_char(sysdate,'yyyy') and af.isdeleted=0");
		sql.append(" join edu_base_year y on y.firstyear=extract(year from sysdate) and y.isdeleted=0");
		sql.append(" left join edu_fee_payyear py on py.gradeid=si.gradeid and py.paymenttype=ci.classiccode and py.isdeleted=0");
		sql.append(" left join edu_fee_payyeardetails pyd on pyd.payyearid=py.resourceid and pyd.isdeleted=0");
		sql.append(" where sci.changetype='13'");
		
		if(map.containsKey("studentid")){
			sql.append(" and si.resourceid=:studentid");
		}
		if(map.containsKey("studentids")){
			sql.append(" and si.resourceid in(:studentids)");
		}
		List<StuReturnFeeCommissionInfoVo> results = new ArrayList<StuReturnFeeCommissionInfoVo>();
 		try {
 			results = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), StuReturnFeeCommissionInfoVo.class, map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}

	/**
	 * 按日期查询已缴费的学生缴费信息
	 * @param startDate
	 * @param endDate
	 */
	@Override
	public void queryBatchForGW(String startDate,String endDate){
		try{
			do{
				// 接口基本信息
				ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
				String serverUrl = property.getProperty("gw.fee_serverUrl");
				String queryBatch = property.getProperty("gw.queryBatch");
				String sysId = property.getProperty("gw.fee_sysId");
				String sysCert = property.getProperty("gw.fee_sysCert");
				// 缴费批次编号（年份）
				String year = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
				StringBuffer serverUrlAndParam = new StringBuffer(500);
				serverUrlAndParam.append(serverUrl).append(queryBatch).append("?");
				Map<String, Object> queryParamMap = new HashMap<String, Object>(10);
				queryParamMap.put("sysId", sysId);
				queryParamMap.put("year", year);
				queryParamMap.put("sysCert", sysCert);
				queryParamMap.put("startDate", startDate);
				queryParamMap.put("endDate", endDate);
				queryParamMap.put("sign", SignUtils.signByMD5(queryParamMap).toLowerCase());
				queryPaymentForGW(serverUrlAndParam, queryParamMap);
			} while(false);
		} catch(ParseException e){
			logger.error("时间解析出错", e);
		} catch(UnsupportedEncodingException e){
			logger.error("编码出错", e);
		}
	}
	
	/**
	 * 根据身份证号获取学生缴费记录
	 * @param certNum
	 */
	@Override
	public void queryForGW(String certNum){
		try{
			do{
				// 接口基本信息
				ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
				String serverUrl = property.getProperty("gw.fee_serverUrl");
				String query = property.getProperty("gw.query");
				String sysId = property.getProperty("gw.fee_sysId");
				String sysCert = property.getProperty("gw.fee_sysCert");
				// 缴费批次编号（年份）
				String year = CacheAppManager.getSysConfigurationByCode("payment_batchNo").getParamValue();
				StringBuffer serverUrlAndParam = new StringBuffer(500);
				serverUrlAndParam.append(serverUrl).append(query).append("?");
				Map<String, Object> queryParamMap = new HashMap<String, Object>(10);
				queryParamMap.put("sysId", sysId);
				queryParamMap.put("year", year);
				queryParamMap.put("sysCert", sysCert);
				queryParamMap.put("idCard", certNum);
				queryParamMap.put("sign", SignUtils.signByMD5(queryParamMap).toLowerCase());
				queryPaymentForGW(serverUrlAndParam, queryParamMap);
			} while(false);
		} catch(ParseException e){
			logger.error("时间解析出错", e);
		} catch(UnsupportedEncodingException e){
			logger.error("编码出错", e);
		}
	}
	
	
	/**
	 * 
	 * @param serverUrlAndParam
	 * @param queryParamMap
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws ParseException
	 */
	@Override
	public StringBuffer queryPaymentForGW(StringBuffer serverUrlAndParam, Map<String, Object> queryParamMap)
			throws UnsupportedEncodingException, ParseException {
		StringBuffer msg = new StringBuffer(500);
		// 去除密钥
		queryParamMap.remove("sysCert");
		String result = HttpUrlConnectionUtils.getRequest(serverUrlAndParam.append(SignUtils.createQueryString(queryParamMap)).toString());
		ResultVO resultVO = JsonUtils.jsonToBean(result, ResultVO.class);
		if(!CODE_SUCCESS.equals(resultVO.getCode())){
			logger.info(resultVO.getMsg());
		}
		List<PaymentRerultVO> paymentRerultVOList = resultVO.getData();
		if(!CollectionUtils.isEmpty(paymentRerultVOList)){
			String spdHql = "From StudentPaymentDetails spd where spd.isDeleted=0 and spd.studentInfo.resourceid=? and spd.eduOrederNo=?";
			String certNum = null;
			List<StudentPayment> paymentList = new ArrayList<StudentPayment>(500);
			List<StudentPaymentDetails> paymentDetailsList = new ArrayList<StudentPaymentDetails>(500);
			for(PaymentRerultVO pvo : paymentRerultVOList){
				// 获取身份证号
				certNum = pvo.getIdCard();
				if(ExStringUtils.isBlank(certNum)){
					continue;
				}
				List<EnquiryPaymentDetailsVO> epaymentDetailsList = pvo.getRecords();
				if(CollectionUtils.isEmpty(epaymentDetailsList)){
					continue;
				}
				// 检查该学生是否存在
				StudentInfo studentInfo = studentInfoService.findBycertNum(certNum);
				if(studentInfo==null){
					logger.info("------"+certNum+"的学籍不存在");
					continue;
				}
				// 检查该学生的缴费标准是否已创建
				StudentPayment payment = findUniqueByProperty("studentInfo.resourceid", studentInfo.getResourceid());
				if(payment==null){
					logger.info("------"+certNum+"的缴费标准还未生成");
					continue;
				}
				StudentPaymentDetails paymentDetails = null;
				String chargeStatus = null;
				for(EnquiryPaymentDetailsVO eqd : epaymentDetailsList){
					// 只处理学费
					if(!"4".equals(eqd.getItemType())){
						continue;
					}
					// 检查某个学生的某条记录是否已经存在
					StudentPaymentDetails spd = studentPaymentDetailsService.findUnique(spdHql, studentInfo.getResourceid(),eqd.getId());
					if(spd!=null){
						continue;
					}
					Double realAmount = new BigDecimal(eqd.getRealAmount()).doubleValue();
					paymentDetails = createPaymentDetails(studentInfo, eqd,realAmount);
					
					Double facepayFee = payment.getFacepayFee()+realAmount;
					Double recpayFee = payment.getRecpayFee();
					payment.setFacepayFee(facepayFee);
					if(recpayFee.equals(facepayFee)){
						chargeStatus = "1";
					}else if(facepayFee == 0){
						chargeStatus = "0";
					}else {
						chargeStatus = "-1";
					}
					payment.setChargeStatus(chargeStatus);
					paymentList.add(payment);
					paymentDetailsList.add(paymentDetails);
				}
			}
			
			if(!CollectionUtils.isEmpty(paymentList)){
				batchSaveOrUpdate(paymentList);
			}
			if(!CollectionUtils.isEmpty(paymentDetailsList)){
				studentPaymentDetailsService.batchSaveOrUpdate(paymentDetailsList);
			}
		}
		return msg;
	}



	private StudentPaymentDetails createPaymentDetails(StudentInfo studentInfo,EnquiryPaymentDetailsVO eqd, Double realAmount)
			throws ParseException {
		StudentPaymentDetails paymentDetails;
		paymentDetails =  new StudentPaymentDetails();
		paymentDetails.setEduOrederNo(eqd.getId());
		paymentDetails.setStudentInfo(studentInfo);
		paymentDetails.setCreateDate(new Date());
		paymentDetails.setOperateDate(ExDateUtils.parseDate(eqd.getUpdateTime(), ExDateUtils.PATTREN_DATE_TIME_COMBINE));
		paymentDetails.setPaidAmount(realAmount);
		paymentDetails.setPayAmount(realAmount);
		paymentDetails.setShouldPayAmount(realAmount);
		paymentDetails.setPaymentMethod("2");
		return paymentDetails;
	}

	/**
	 * 缴费--教材费
	 * @param tempStudentFee
	 * @param payDetailsVO
	 * @param user
	 * @return
	 * @throws ServiceException
	 */
	@Override
	public Map<String, Object> payFeeForTextbook(TempStudentFee tempStudentFee, PayDetailsVO payDetailsVO, User user) throws ServiceException {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			do{
				// 检查该考生是否已经有了学籍，没有则提示先注册学籍（针对新生）
				StudentInfo studentInfo = tempStudentFee.getStudentInfo();
				// 先判断该学生是否已有学籍
				if(studentInfo == null){
					 // 获取唯一标识
					String uniqueId = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
					String propertyName = "examCertificateNo";
					if ("1".equals(uniqueId)) {
						propertyName = "enrolleeCode";
					}
					// 新生
					studentInfo = studentInfoService.findUniqueByProperty(propertyName, tempStudentFee.getExamCertificateNo());
					if(studentInfo == null){
						statusCode = 300;
						message = "请先将该考生注册为学籍，再缴教材费！";
						continue;
					}
				}
				// 检查金额
				Double facepay = payDetailsVO.getPayAmount()==null?0:payDetailsVO.getPayAmount();
				if(facepay <= 0){
					statusCode = 300;
					message = "缴费金额不能小于或等于0！";
					continue;
				}
			
				// 获取缴费类型全局参数
				SysConfiguration payTypeConf = CacheAppManager.getSysConfigurationByCode("payment.payType");
				Integer payType = StudentPaymentDetails.PAYTYPE_UPDATE;
				if(payTypeConf != null && ExStringUtils.isNotEmpty(payTypeConf.getParamValue())
						&& StudentPaymentDetails.PAYTYPE_OVERLAY.equals(Integer.valueOf(payTypeConf.getParamValue()))){
					payType = StudentPaymentDetails.PAYTYPE_OVERLAY;
				}
				YearInfo year = tempStudentFee.getYearInfo();
				// 新增缴费明细
				Date today = new Date();
				StudentPaymentDetails studentPaymentDetails = new StudentPaymentDetails();
				studentPaymentDetails.setEduOrederNo(payDetailsVO.getEduOrederNo());
				studentPaymentDetails.setStudentInfo(studentInfo);
				studentPaymentDetails.setShouldPayAmount(facepay);
				studentPaymentDetails.setPayType(payType);// 缴费类型
				studentPaymentDetails.setCreateDate(today);
				if(payDetailsVO.getPayTime()!=null){
					today = payDetailsVO.getPayTime();
				}
				studentPaymentDetails.setOperateDate(today);
				studentPaymentDetails.setPayNo(payDetailsVO.getPayNo());
				studentPaymentDetails.setPayAmount(facepay);// 缴费金额
				studentPaymentDetails.setPaidAmount(facepay);// 已缴金额
				studentPaymentDetails.setYear(String.valueOf(year.getFirstYear()));
				studentPaymentDetails.setPosSerialNumber(payDetailsVO.getPosSerialNumber());
				studentPaymentDetails.setReceiptNumber(payDetailsVO.getReceiptNumber());
				studentPaymentDetails.setCarrCardNo(payDetailsVO.getCarrCardNo());
				studentPaymentDetails.setCarrTermNum(payDetailsVO.getCarrTermNum());
				studentPaymentDetails.setPaymentMethod(payDetailsVO.getPaymentMethod());
				studentPaymentDetails.setCheckPayable(payDetailsVO.getCheckPayable());
				studentPaymentDetails.setMemo(payDetailsVO.getMemo());
				studentPaymentDetails.setOperatorName(user.getCnName());
				studentPaymentDetails.setOperatorId(user.getResourceid());
				studentPaymentDetails.setChargeMoney(payDetailsVO.getChargeMoney());
				studentPaymentDetails.setClassName(studentInfo.getGrade().getGradeName()+studentInfo.getMajor().getMajorName()+studentInfo.getClassic().getClassicName());
				// 收费项
				studentPaymentDetails.setChargingItems(payDetailsVO.getChargingItems());
				// 是否开单位发票
				studentPaymentDetails.setIsInvoicing(payDetailsVO.getIsInvoicing());
				// 单位名称
				studentPaymentDetails.setInvoiceTitle(payDetailsVO.getInvoiceTitle());
				studentPaymentDetailsService.saveOrUpdate(studentPaymentDetails);
				// 更新学生年度缴费信息
				AnnualFees annualFees = annualFeesService.findUnique("from "+AnnualFees.class.getSimpleName()+" af where af.isDeleted=0 and af.studentInfo.resourceid=? and af.chargeYear=? and af.chargingItems=?", 
						studentInfo.getResourceid(),year.getFirstYear().intValue(),payDetailsVO.getChargingItems());
				if(annualFees!=null){
					annualFees.setFacepayFee(facepay);
					int chargeStatus = 0;
					if(annualFees.getRecpayFee().doubleValue()==facepay.doubleValue()){
						chargeStatus = 1;
					}else if(annualFees.getRecpayFee().doubleValue()>facepay.doubleValue()){
						chargeStatus = -1;
					} 
					annualFees.setChargeStatus(chargeStatus);
					
				} else {
					// 新增
					annualFees = new AnnualFees();
					annualFees.setChargeStatus(1);
					annualFees.setChargeYear(year.getFirstYear().intValue());
					annualFees.setChargingItems(payDetailsVO.getChargingItems());
					annualFees.setDerateFee(0d);
					annualFees.setFacepayFee(facepay);
					annualFees.setReturnPremiumFee(0d);
					annualFees.setPayAmount(0d);
					annualFees.setRecpayFee(facepay);
					annualFees.setStudentInfo(studentInfo);
					annualFees.setStudyNo(studentInfo.getStudyNo());
					annualFees.setYearInfo(year);
				}
				annualFeesService.saveOrUpdate(annualFees);
				// 更新预缴费订单
				tempStudentFee.setPayStatus(Constants.FEE_PAYSTATUS_PAYED);
				tempStudentFee.setEduOrderNo(payDetailsVO.getEduOrederNo());
				tempStudentFeeService.saveOrUpdate(tempStudentFee);
				
				returnMap.put("studentPaymentDetails", studentPaymentDetails);
			} while(false);
		} catch (Exception e) {
			logger.error("缴教材费出错：", e);
			statusCode = 300;
			message = "缴费失败！";
		}finally {
			returnMap.put("statusCode", statusCode);
			returnMap.put("message", message);
		}
		return returnMap;
	}

}
