package com.hnjk.edu.roll.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.*;
import com.hnjk.edu.basedata.service.*;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.recruit.helper.ComparatorRecruitMajor;
import com.hnjk.edu.recruit.model.*;
import com.hnjk.edu.recruit.service.*;
import com.hnjk.edu.recruit.util.ReplaceStr;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.*;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.roll.vo.*;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingJDBCService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.csv.CsvWriter;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;

import jxl.format.*;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 学生学籍
 * <code>StudentInfoController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-22 下午04:17:22
 * @see 
 * @version 1.0
 */
@Controller
public class StudentInfoController extends FileUploadAndDownloadSupportController{
	
	private static final long serialVersionUID = -8188089091305280629L;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("studentResumeService")
	private IStudentResumeService studentResumeService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	@Autowired
	@Qualifier("reginfoservice")
	private IReginfoService reginfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("studentInfoChangeJDBCService")
	private IStudentInfoChangeJDBCService studentInfoChangeJDBCService;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;
	
	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingplanservice;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("branchSchoolMajorService")
	private IBranchSchoolMajorService branchSchoolMajorService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("exameeInfoService")
	private IExameeInfoService exameeInfoService;
	
	@Autowired
	@Qualifier("graduationStatJDBCService")
	private IGraduationStatJDBCService graduationStatJDBCService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("rollJDBCService")
	private IRollJDBCService rollJDBCService;
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;//系统消息
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;

	/**
	 * 修改录取学员信息-列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/studentinfo/register/revise-enrolleeinfo-list.html")
	public String reviseEnrolleeInfoList(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {

		Map<String, Object> condition = new HashMap<String, Object>();                                  	 // 查询条件
		String advanseCon 			  = ExStringUtils.trimToEmpty(request.getParameter("con"));         	 // 高级查询
		String branchSchool 		  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));	 // 学习中心
		String recruitPlan 			  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));		 // 招生批次
		String gradeId     			  = ExStringUtils.defaultIfEmpty(request.getParameter("grade"), "");	 // 年级
		String major 				  = ExStringUtils.trimToEmpty(request.getParameter("major"));			 // 专业
		String classic 				  = ExStringUtils.trimToEmpty(request.getParameter("classic"));			 // 层次
		String stuName 				  = ExStringUtils.trimToEmpty(request.getParameter("stuName"));		     // 姓名
		String examCertificateNo 	  = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));// 准考证号
		String certNum 				  = ExStringUtils.trimToEmpty(request.getParameter("certNum"));			 // 证件号码
		String enrollType 			  = ExStringUtils.trimToEmpty(request.getParameter("enrollType"));		 // 报名类型
		String learningStyle 		  = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));	 //学习形式
		String isApplyNoexam 		  = ExStringUtils.trimToEmpty(request.getParameter("isApplyNoexam"));	 //是否免试
		String teachingType           = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));	 //办学模式
		String orderBy 				  = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));			 //按xx排序
		String orderType 			  = ExStringUtils.trimToEmpty(request.getParameter("orderType"));		 //排序类型 ASC DESC

		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (StringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
		}
		if (StringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (StringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (StringUtils.isNotEmpty(stuName)) {
			condition.put("stuName", stuName);
		}
		if (StringUtils.isNotEmpty(examCertificateNo)) {
			condition.put("examCertificateNo", examCertificateNo);
		}
		if (StringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if (StringUtils.isNotEmpty(enrollType)) {
			condition.put("enrollType", Long.parseLong(enrollType));
		}
		if (StringUtils.isNotEmpty(gradeId)) {
			condition.put("grade",gradeId);
		}
		if (StringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType",teachingType);
		}
		if (StringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}
		if (StringUtils.isNotEmpty(isApplyNoexam)) {
			condition.put("isApplyNoexam",isApplyNoexam);
		}
		
		condition.put("isMatriculate",Constants.BOOLEAN_YES);
		
		//如果排序条件不为空，则加入排序
		if(StringUtils.isNotEmpty(orderBy) && StringUtils.isNotEmpty(orderType))  {			
			objPage.setOrderBy("en."+orderBy);
			objPage.setOrder(orderType);
		}else {
			objPage.setOrderBy("en.signupDate desc,en.signupFlag");
		}		
		
		model.addAttribute("condition", condition);

		if ("advance".equals(advanseCon)) {
			return "/edu3/roll/reviseenrolleeinfo/reviseenrolleeinfo-search";// 返回到高级检索
		}

		Page page = enrolleeInfoService.findEnrolleeInfoForRegisterList(condition,objPage);
		model.addAttribute("eilist", page);	
	
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		return "/edu3/roll/reviseenrolleeinfo/reviseenrolleeinfo-list";
	}

	/**
	 * 修改录取学员信息-编辑
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/studentinfo/register/revise-enrolleeinfo-edit.html")
	public String reviseEnrolleeInfoEdit(HttpServletRequest request, ModelMap model) throws WebException {
		Map<String,Object> condition = new HashMap<String, Object>();
		String resourceid 	  = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		if (ExStringUtils.isNotEmpty(resourceid)) {
			EnrolleeInfo ei   = enrolleeInfoService.get(resourceid);
			RecruitPlan recruitplan = ei.getRecruitMajor().getRecruitPlan();
			condition.put("grade",recruitplan.getGrade().getResourceid());
			model.put("enrolleeinfo", ei);
			model.put("student",ei.getStudentBaseInfo());
			//获取原报名批次所在年级下的所有申报记录
			List<BranchSchoolPlan> bspList = branchSchoolPlanService.getBranchSchoolPlanList(condition);
			Set<RecruitMajor>  rmSet       = ei.getRecruitMajor().getRecruitPlan().getRecruitMajor();
			Map<String,RecruitMajor> rmMap = new HashMap<String, RecruitMajor>();
			List<BranchSchoolMajor> rmList = branchSchoolMajorService.findByHql("  from "+BranchSchoolMajor.class.getSimpleName()+" brm where brm.isDeleted=0 and brm.isPassed='Y' and brm.branchSchoolPlan.branchSchool.resourceid=? and brm.recruitMajor.recruitPlan.grade.resourceid=?",ei.getBranchSchool().getResourceid(), ei.getRecruitMajor().getRecruitPlan().getGrade().getResourceid());
			
			for (RecruitMajor rm : rmSet) {
				
				String classicId1    = rm.getClassic().getResourceid();
				String majorId1      = rm.getMajor().getResourceid();
				String teachingType1 = ExStringUtils.trimToEmpty(rm.getTeachingType());
				
				for (BranchSchoolMajor brm : rmList) {
					String classicId2    = brm.getRecruitMajor().getClassic().getResourceid();
					String majorId2      = brm.getRecruitMajor().getMajor().getResourceid();
					String teachingType2 = ExStringUtils.trimToEmpty(brm.getRecruitMajor().getTeachingType());
					if (teachingType1.equals(teachingType2) && classicId1.equals(classicId2) && majorId1.equals(majorId2)) {
						rmMap.put(rm.getResourceid(), rm);
					}
				}
			}
			List<RecruitMajor> rms    = new ArrayList<RecruitMajor>(rmMap.values());
			ComparatorRecruitMajor crm= new  ComparatorRecruitMajor();
			if (null!=rmMap.values()) {
				Collections.sort(rms, crm);
			}
			
			// 设置籍贯默认参数
			if (StringUtils.isNotEmpty(ei.getStudentBaseInfo().getHomePlace())) {
				
				String[] homePlace = ei.getStudentBaseInfo().getHomePlace().split(",");
				if (homePlace != null && homePlace.length > 2) {
					model.addAttribute("homePlace_province", homePlace[0]);
					model.addAttribute("homePlace_city", homePlace[1]);
					model.addAttribute("homePlace_county", homePlace[2]);
				}
			}

			Map<String,Object> planMap  = new LinkedHashMap<String, Object>();
			for (int i = 0; i < bspList.size(); i++) {//去初重复数据
				if ("Y".equals(recruitplan.getIsSpecial())) {
					if (recruitplan.getBrSchoolIds().indexOf(bspList.get(i).getBranchSchool().getResourceid())>=0) {
						planMap.put(bspList.get(i).getBranchSchool().getResourceid(), bspList.get(i));
					}
				}else {
					planMap.put(bspList.get(i).getBranchSchool().getResourceid(), bspList.get(i));
				}
			}
			Collection<Object> collection = planMap.values();
			model.put("bspList", collection);
			model.put("bspMap", planMap);
			model.put("rmList",rms);
			model.put("rmMap", rmMap);
			// 设置户口默认参数
			if (StringUtils.isNotEmpty(ei.getStudentBaseInfo().getResidence())) {
				String[] residence = ei.getStudentBaseInfo().getResidence().split(",");
				if (residence != null && residence.length > 2) {
					model.addAttribute("residence_province", residence[0]);
					model.addAttribute("residence_city", residence[1]);
					model.addAttribute("residence_county", residence[2]);
				}
			}
			if(null == ei.getSignupDate()) {
				ei.setSignupDate(new Date());
			}
			model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(ei.getSignupDate()));//设置附件上传自动创建目录
		}
		return "/edu3/roll/reviseenrolleeinfo/reviseenrolleeinfo-form";
	}
	/**
	 * 修改录取学员信息-编辑
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/studentinfo/register/revise-enrolleeinfo-save.html")
	public void reviseEnrolleeInfoSave(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException {
	
		Map<String,Object> map = new HashMap<String, Object>();		
		EnrolleeInfo ei        = null;
		StudentBaseInfo sbi    = null;
		User user              = SpringSecurityHelper.getCurrentUser();
		// 设置参数
		try {
			//修改
			if (StringUtils.isNotEmpty(resourceid)) {
				
				ei  		   = enrolleeInfoService.get(resourceid);
				sbi 		   = ei.getStudentBaseInfo();
				
				setRequestParameter(request, ei, sbi);
				
				studentService.update(sbi);
				ei.setStudentBaseInfo(sbi);
				enrolleeInfoService.update(ei);
				
				map.put("statusCode", 200);
				map.put("navTabId", "RES_ROLL_REVISE_ENROLLEEINFO_EDIT");
				map.put("message","保存成功！");
				map.put("reloadUrl", request.getContextPath() +"/edu3/studentinfo/register/revise-enrolleeinfo-edit.html?resourceid="+ei.getResourceid() );
				logger.error("修改已录取学生信息----->>>操作人："+user.getCnName()+"  操作人ID："+user.getResourceid()+"  操作时间："+ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE_TIME));	
			}else {
				throw new WebException("请选择要操作的学生！");
			}
			
		} catch (Exception e) {			
			logger.error("修改已录取学生报名信息失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	private void setRequestParameter(HttpServletRequest request,EnrolleeInfo ei, StudentBaseInfo student)throws WebException{
		
		String recruitMajor    = ExStringUtils.trimToEmpty(request.getParameter("recruitMajor"));//招生专业
		String branchSchool    = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//校外学习中心

		//~~~~~~~~~~~~~~~~~~~~设置学生基本信息
		// 职称职务
		student.setTitle(ExStringUtils.trimToEmpty(request.getParameter("title")));
		// 姓名
		student.setName(ExStringUtils.trimToEmpty(request.getParameter("name")));
		// #性别
		student.setGender(ExStringUtils.trimToEmpty(request.getParameter("gender")));
		// #证件类型
		student.setCertType(ExStringUtils.trimToEmpty(request.getParameter("certType")));
		// 证件号码
		student.setCertNum(ExStringUtils.trimToEmpty(request.getParameter("certNum")));
		try {
			// 出生日期
			student.setBornDay(ExDateUtils.parseDate(ExStringUtils.trimToEmpty(request.getParameter("bornDay")), ExDateUtils.PATTREN_DATE));
		} catch (Exception e) {
			logger.error("修改已录取报名信息设置设置出生年月出错：{}"+e.fillInStackTrace());
		}
		// 籍贯
		student.setHomePlace(ExStringUtils.trimToEmpty(request.getParameter("homePlace")));
		// 户口所在地
		student.setResidence(ExStringUtils.trimToEmpty(request.getParameter("residence")));
		// #民族
		student.setNation(ExStringUtils.trimToEmpty(request.getParameter("nation")));
		// #政治面貌
		student.setPolitics(ExStringUtils.trimToEmpty(request.getParameter("politics")));
		// #婚姻状况
		student.setMarriage(ExStringUtils.trimToEmpty(request.getParameter("marriage")));
		// 单位名称
		student.setOfficeName(ExStringUtils.trimToEmpty(request.getParameter("officeName")));
		// 职业状态
		 student.setWorkStatus(ExStringUtils.trimToEmpty(request.getParameter("workStatus")));
		//行业类别
		 student.setIndustryType(ExStringUtils.trimToEmpty(request.getParameter("industryType")));
		// 通讯地址
		student.setContactAddress(ExStringUtils.trimToEmpty(request.getParameter("contactAddress")));
		// 通讯邮编
		student.setContactZipcode(ExStringUtils.trimToEmpty(request.getParameter("contactZipcode")));
		// 联系电话
		student.setContactPhone(ExStringUtils.trimToEmpty(request.getParameter("contactPhone")));
		// 手机
		student.setMobile(ExStringUtils.trimToEmpty(request.getParameter("mobile")));
		// 电子邮件
		student.setEmail(ExStringUtils.trimToEmpty(request.getParameter("email")));
		// 照片路径
		student.setPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("photoPath")));
		//身份证
		student.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPath")));
		//毕业证
		student.setEduPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("eduPhotoPath")));
		// 备注
		student.setMemo(ExStringUtils.trimToEmpty(request.getParameter("memo")));

		// ~~~~~~~~~~~~设置学生个人简历
		String[] resumeids    = request.getParameterValues("resumeid");
		String[] startYears   = request.getParameterValues("startYear");
		String[] startMonths  = request.getParameterValues("startMonth");
		String[] endYears     = request.getParameterValues("endYear");
		String[] endMonths    = request.getParameterValues("endMonth");
		String[] companys     = request.getParameterValues("company");
		String[] resumetitles = request.getParameterValues("resumetitle");
		if (resumeids != null) {
			Set<StudentResume> resumeSet = new HashSet<StudentResume>();
			for (int index = 0; index < resumeids.length; index++) {
				StudentResume resume = new StudentResume();
				;
				if (StringUtils.isNotEmpty(resumeids[index])) {
					resume = studentResumeService.get(resumeids[index]);
				} else {
					resume.setStudent(student);
				}
				resume.setStartYear(Long.parseLong(startYears[index]));
				resume.setStartMonth(Long.parseLong(startMonths[index]));
				resume.setEndYear(Long.parseLong(endYears[index]));
				resume.setEndMonth(Long.parseLong(endMonths[index]));
				resume.setCompany(companys[index]);
				resume.setTitle(resumetitles[index]);
				resumeSet.add(resume);
			}
			student.setStudentResume(null);
			student.setStudentResume(resumeSet);
		}
		
		// ~~~~~~~~~~~~~~~~~~~~~设置学生报名信息

		// 招生专业
		if (StringUtils.isNotEmpty(recruitMajor)) {
			RecruitMajor major = recruitMajorService.get(recruitMajor);
			ei.setRecruitMajor(major);
		}
		// 学习中心
		if (StringUtils.isNotEmpty(branchSchool)) {
			OrgUnit school = orgUnitService.get(branchSchool);
			ei.setBranchSchool(school);
		}
		// 默认的学习方式是"业余"
		ei.setStutyMode("3");		
		// 报名日期
		ei.setSignupDate(ExDateUtils.getCurrentDateTime());
		// 是否免试
		ei.setIsApplyNoexam(ExStringUtils.trimToEmpty(request.getParameter("isApplyNoexam")));
		// #最后学历
		ei.setEducationalLevel(ExStringUtils.trimToEmpty(request.getParameter("educationalLevel")));
		// 毕业院校
		ei.setGraduateSchool(ExStringUtils.trimToEmpty(request.getParameter("graduateSchool")));
		// 毕业学校代码
		ei.setGraduateSchoolCode(ExStringUtils.trimToEmpty(request.getParameter("graduateSchoolCode")));
		// 毕业专业
		ei.setGraduateMajor(ExStringUtils.trimToEmpty(request.getParameter("graduateMajor")));
		// 毕业证书号
		ei.setGraduateId(ExStringUtils.trimToEmpty(request.getParameter("graduateId")));
		try{
			// 毕业时间
			ei.setGraduateDate(ExDateUtils.parseDate(ExStringUtils.trimToEmpty(request.getParameter("graduateDate")), ExDateUtils.PATTREN_DATE));
		} catch (Exception e) {
			logger.error("保存报名信息设置设置毕业时间出错：{}"+e.fillInStackTrace());
		}
		// #媒体来源
		ei.setFromMedia(ExStringUtils.trimToEmpty(request.getParameter("fromMedia")));
		// 其他媒体来源
		ei.setFromOtherMedia(ExStringUtils.trimToEmpty(request.getParameter("fromOtherMedia")));
    
	}
	/**
	 * 入学资格查询
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/register/studentinfo/enrolshcool-list.html")
	public String exeEnrolShcool(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		//		objPage.setOrderBy("en.recruitMajor.recruitPlan.grade.yearInfo.firstYear ");
		objPage.setOrderBy("en.recruitMajor.recruitPlan.yearInfo.firstYear ");
		objPage.setOrder(Page.DESC);//设置默认排序方式

		User user = SpringSecurityHelper.getCurrentUser();
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		String highClassic = ExStringUtils.trimToEmpty(request.getParameter("highClassic"));//高学历层次
		String stuName = ExStringUtils.trimToEmpty(request.getParameter("stuName"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String isStudyFollow = ExStringUtils.trimToEmpty(request.getParameter("isStudyFollow"));//是否跟读
		String isDistribution=ExStringUtils.trimToEmpty(request.getParameter("isDistribution")); //分配情况

		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业



		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool =	user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}else{
			if (ExStringUtils.isNotEmpty(isDistribution)) {
				condition.put("isDistribution", isDistribution);
				OrgUnit unit = orgUnitService.findUnique("from OrgUnit u where u.unitName=?", "未分配");
				condition.put("branchSchoolResourceid", unit.getResourceid());
//				condition.put("branchSchoolResourceid", "4a40f0a044289986014429e4d12b0001");
			}
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(highClassic)) {
			condition.put("highClassic", highClassic);
		}
		if(ExStringUtils.isNotEmpty(stuName)) {
			condition.put("stuName",stuName);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(isStudyFollow)) {
			condition.put("isStudyFollow", isStudyFollow);
		}

		condition.put("isMatriculate", Constants.BOOLEAN_YES); //是否录取	

		Page page = enrolleeInfoService.findEnrolleeInfoForRegisterList(condition, objPage);
		model.addAttribute("eilist", page);
		if(ExStringUtils.isNotEmpty(stuName)) {
			condition.put("stuName", stuName);
		}
		model.addAttribute("condition", condition);
		//根据招生批次的年度的firstYear 从大到小排序，取最大的返回
		String hql = "";
		hql = " from "+RecruitPlan.class.getSimpleName()+" where isDeleted=0 and isPublished='Y' order by yearInfo.firstYear desc";
		List<RecruitPlan> planList = recruitPlanService.findByHql(hql);
		Date date = new Date();
		String isTimeInRange = "Y";
		if(planList!=null && planList.size()>0){
			if(planList.get(0).getReportTime()!=null && date.before(planList.get(0).getReportTime())){//在报道开始时间之前
				isTimeInRange = "N";
			}
			if(planList.get(0).getEndTime()!=null && date.after( planList.get(0).getEndTime())){//在报道结束时间之后
				isTimeInRange = "N";
			}
			model.put("isTimeInRange",isTimeInRange);
			model.put("startTime", planList.get(0).getReportTime());
			model.put("endTime", planList.get(0).getEndTime());
			model.put("planName", planList.get(0).getRecruitPlanname());
			model.put("recruitPlanID", planList.get(0).getResourceid());
		}
		
		
		model.put("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				 + CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());

		return "/edu3/roll/enrolschool/enrolshcool-list";
	}
	/**
	 * 下载未注册学生名单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/register/studentinfo/downloadUnRegList.html")
	public void downloadUnRegList(HttpServletRequest request ,HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String highClassic = ExStringUtils.trimToEmpty(request.getParameter("highClassic"));
		String stuName = ExStringUtils.trimToEmpty(request.getParameter("stuName"));
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String isStudyFollow = ExStringUtils.trimToEmpty(request.getParameter("isStudyFollow"));
		String type=CacheAppManager.getSysConfigurationByCode("Whether_studentno_sorting").getParamValue();//是否按学号进行排序
		String unitName = "";
		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool =	user.getOrgUnit().getResourceid();
			unitName = user.getOrgUnit().getUnitName();
			condition.put("brshSchool", "Y");
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			if(ExStringUtils.isEmpty(unitName)){
				   OrgUnit unit = orgUnitService.findUnique("from OrgUnit u where u.resourceid=?", branchSchool);
				   if(unit != null) {
					   unitName = unit.getUnitName() ;
				   }
			}
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(highClassic)) {
			condition.put("highClassic", highClassic);
		}
		if(ExStringUtils.isNotEmpty(stuName)) {
			condition.put("name",stuName);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(isStudyFollow)) {
			condition.put("isStudyFollow", isStudyFollow);
		}
		if("Y".equals(type)) {
			condition.put("type", type);
		}
				
		condition.put("isMatriculate", Constants.BOOLEAN_YES); //是否录取			
	 	try{
 			String error = "";
	 	    List<Map<String,Object>> list =   enrolleeInfoService.findEnrolleeInfoForRegisterList(condition);
	 	    if (!condition.containsKey("branchSchool") && list.size() > 3000) {
	 	    	renderHtml(response, "<script>alert('未注册数据导出超过3000条，请选择教学站再试！')</script>");
			} else {
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				GUIDUtils.init();
				//导出
				File excelFile = null;
				File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				//初始化配置参数
				//以导入报名信息的标识做为判断，0.ZKZH//准考证号 1.KSH//考生号
				if("0".equals(CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue())){
					exportExcelService.initParmasByfile(disFile, "regUnRegList2", list,null);
				}
				else{
				exportExcelService.initParmasByfile(disFile, "regUnRegList", list,null);
				}
				exportExcelService.getModelToExcel().setHeader("未注册学生信息列表");
				exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
				
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				String downloadFileName = "未注册学生.xls";
				if(ExStringUtils.isNotEmpty(unitName)) {
					downloadFileName = unitName + ".xls";
				}
				String downloadFilePath = excelFile.getAbsolutePath();
				downloadFile(response, downloadFileName,downloadFilePath,true);	
			}
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		 }
	}
	/**
	 * 批量注册导入
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/uploadToReg.html")
	public String uploadToReg(HttpServletRequest request,HttpServletResponse response,ModelMap model){		
		return "/edu3/roll/enrolschool/enrollschoolUpload-view";
	}
	/**
	 * 批量导入注册
	 * @param planid
	 * @param attachId
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/uploadToRegImport.html")
	@Transactional
	public void showExcelExportImport(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{				
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if( ExStringUtils.isNotBlank(attachId)){				
				Attachs attachs =  attachsService.get(attachId);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel = attachs.getFile();
				int registeredNum= 0 ;
				int setFirstTermCourseNum = 0;
				List<String> dictCodeList = new ArrayList<String>();
				//处理数据字典字段
				importExcelService.initParmas(excel, "regList", dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYNAME));
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				List<StudentInfoVo> modelList = importExcelService.getModelList();
				StringBuffer msg = new StringBuffer(); 
				String enrolleeInfoIds = "";
				StringBuffer errorStr = new StringBuffer();
				List<EnrolleeInfo> enrolleeInfoList = new ArrayList<EnrolleeInfo>();
				for (StudentInfoVo s : modelList) {
					StringBuffer error = new StringBuffer();
					//学号、考生号、身份证号
					String studyNo= s.getStudentNo();
					String enrolleecode = s.getExamNo();
					String certNum = s.getCertNum();
					if (ExStringUtils.isBlank(studyNo)) {
						error.append("学号为空");
					}
					if (ExStringUtils.isBlank(enrolleecode)) {
						error.append("考生号为空");
					}
					if (ExStringUtils.isBlank(certNum)) {
						error.append("身份证号为空");
					}
					if (error.length() == 0) {
						do{
							String needReg = s.getAgreeornot();
							String noReportReason = s.getNoReportReason();
							String isStudyFollow = s.getIsStudyFollow();
							String memo = s.getMemo();
							// 要注册、有未报到原因、跟读和备注的（注：为了提高速度，从有这些信息到无的，通过编辑功能来修改）
							if("是".equals(ExStringUtils.trimToEmpty(needReg)) || "Y".equals(ExStringUtils.trimToEmpty(needReg)) || ExStringUtils.isNotEmpty(noReportReason)
									|| (ExStringUtils.isNotEmpty(isStudyFollow) && "Y".equals(isStudyFollow)) || ExStringUtils.isNotEmpty(memo)){
								//以导入报名信息的标识做为判断，0.ZKZH//准考证号 1.KSH//考生号
								List<EnrolleeInfo> enrolleeInfos= null;
								if("0".equals(CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue())){
									enrolleeInfos = enrolleeInfoService.findByHql(" from "+EnrolleeInfo.class.getSimpleName()+" e where isDeleted= 0 and e.matriculateNoticeNo= ? and e.examCertificateNo = ? and e.studentBaseInfo.certNum = ?  ", new Object[]{studyNo,enrolleecode,certNum});
								}else{
									enrolleeInfos = enrolleeInfoService.findByHql(" from "+EnrolleeInfo.class.getSimpleName()+" e where isDeleted= 0 and e.matriculateNoticeNo= ? and e.enrolleeCode = ? and e.studentBaseInfo.certNum = ?  ", new Object[]{studyNo,enrolleecode,certNum});
								}
								EnrolleeInfo enrolleeinfo = null;
								if (enrolleeInfos.size() == 1) {									
									enrolleeinfo = enrolleeInfos.get(0);
									if("".equals(enrolleeinfo.getBranchSchool().getResourceid()) || "未分配".equals(enrolleeinfo.getBranchSchool().getUnitName())){
										error.append(enrolleeinfo.getStudentBaseInfo().getCertNum()+" 未分配教学站");
										continue;
									}
									enrolleeinfo.setNoReportReason(noReportReason);
									if(ExStringUtils.isEmpty(isStudyFollow)){
										isStudyFollow = "N";
									}
									enrolleeinfo.setIsStudyFollow(isStudyFollow);
									enrolleeinfo.setMemo(memo);
									enrolleeInfoList.add(enrolleeinfo);
								}else if(enrolleeInfos.size()>1){
									error.append("有多个报名信息");
									continue;
								}else{
									error.append("找不到该学生");
									continue;
								}
								// 要注册的
								if(enrolleeinfo!=null && ("是".equals(ExStringUtils.trimToEmpty(needReg)) || "Y".equals(ExStringUtils.trimToEmpty(needReg)))){
									if("Y".equals(enrolleeinfo.getRegistorFlag())){
										error.append("身份证号："+certNum+"已注册");
									}else{
										if (!"".equals(enrolleeInfoIds)) {
											enrolleeInfoIds += ",";
										}
										enrolleeInfoIds += enrolleeinfo.getResourceid();
									}
								}
							}
						} while (false);
					}
					
					if (error.length() > 0) {
						error.insert(0, "学生：" + s.getName() + "，");
						error.append("<br/>");
						errorStr.append(error);
					}
				}
				// 保存报名信息
				enrolleeInfoService.batchSaveOrUpdate(enrolleeInfoList);
				//处理注册逻辑
				if (ExStringUtils.isNotEmpty(enrolleeInfoIds)) {
					String[] arr = enrolleeInfoIds.split("\\,");	
					try {
						List<StudentInfo> registeredStus = new ArrayList<StudentInfo>();
						synchronized (this) {
							registeredStus = reginfoService.doRegister(arr);
						}
						
						if(null != registeredStus && !registeredStus.isEmpty()){
							registeredNum = registeredStus.size();
							/*~~~~~~~~~~~~~~预约学生首学期课程~~~~~~~~~~*/					
							setFirstTermCourseNum = reginfoService.doRegisterStuFirstTermCourse(registeredStus);
						}	
						msg.append("数据异常:"+errorStr.toString()+"<br/>");
						msg.append("注册成功:"+registeredNum+"人,成功预约首学期课程:"+setFirstTermCourseNum+"人。");
					} catch (Exception e) {
						e.fillInStackTrace();
						msg.append("执行注册时出现异常。<br>");
					}
					map.put("statusCode", 200);				
					map.put("reloadUrl", request.getContextPath()+"/edu3/register/studentinfo/enrolshcool-list.html");
					map.put("navTabId", "RES_SCHOOL_REGISTER_ENROLL");
				} else {
//					msg.append("未得到需注册的记录。");
					String errorMsg = "导入数据有错误<br/>";
					if(ExStringUtils.isEmpty(enrolleeInfoIds)){
						errorMsg = "没有注册的数据";
					}
					errorStr.insert(0, errorMsg);
					msg.append(errorStr);
					map.put("statusCode", 300);				
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.IMPORT, "导入注册：附件ID："+attachId+"||成功条数："+registeredNum+"||绑定教学计划条数:"+setFirstTermCourseNum);
				map.put("message",msg);
			} else {
				map.put("statusCode", 300);				
				map.put("message", "请上传附件.");
			}
		} catch (Exception e) {
			logger.error("导入批量注册出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入批量注册失败:<br/>"+e.getMessage());
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 查看
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/register/studentinfo/edit.html")
	public String exeEdit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			EnrolleeInfo enrollInfo = enrolleeInfoService.get(resourceid);	
			model.addAttribute("enrollInfo", enrollInfo);
			// TODO: 目前我们没有用到第三方的缴费系统（银校通）,以后会补上缴费信息
			/*try {
				JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
				String sql = "select StudentID,StudentName,Sex,ProfessionName,LevelName,FormalName,CollegeName,ClassName,ChargeYear,TuitionFee,OccurAmt from V_BankWater where datalength(studentid)>14 and StudentID='"+enrollInfo.getMatriculateNoticeNo()+"'";
				List list = jdbcTemplate.queryForList(sql);
				model.addAttribute("feeList", list);
			} catch (Exception e) {
				e.printStackTrace();
			}*/
		}
		return "/edu3/roll/enrolschool/enrolshcool-form";
	}
	/**
	 * 全部注册操作时，检查有效的注册信息
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/register/studentinfo/registeringAll.html")
	public void checkAllRegistering(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Page objPage =new Page();
		objPage.setOrderBy("en.recruitMajor.recruitPlan.yearInfo.firstYear ");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			//查询条件
			Map<String,Object> condition = new HashMap<String,Object>();
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
			String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
			String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
			String highClassic = ExStringUtils.trimToEmpty(request.getParameter("highClassic"));//高学历层次
			String stuName = ExStringUtils.trimToEmpty(request.getParameter("stuName"));//姓名
			String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
			String isStudyFollow = ExStringUtils.trimToEmpty(request.getParameter("isStudyFollow"));//是否跟读
			String isDistribution=ExStringUtils.trimToEmpty(request.getParameter("isDistribution")); //分配情况
			String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
			String numTotal =ExStringUtils.trimToEmpty(request.getParameter("numTotal")); //总数
			objPage.setPageSize(Integer.parseInt(numTotal)); //设置查询总数
			
			if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
				branchSchool =	user.getOrgUnit().getResourceid();
				condition.put("brshSchool", "Y");
			}
			OrgUnit unit = orgUnitService.findUnique("from OrgUnit u where u.unitName=?", "未分配");
			if(ExStringUtils.isNotEmpty(branchSchool)){
				if (unit.getResourceid().equals(branchSchool)) {
					throw new WebException("未分配教学站的学生不允许注册，请筛选出已分配教学站的学生");
				}
				condition.put("branchSchool", branchSchool);
			}else{
				if (ExStringUtils.isNotEmpty(isDistribution)) {
					condition.put("isDistribution", isDistribution);
					condition.put("branchSchoolResourceid", unit.getResourceid());
				}
//					else{
//					throw new WebException("未分配教学站的学生不允许注册，请筛选出已分配教学站的学生");
//				}
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			if(ExStringUtils.isNotEmpty(highClassic)) {
				condition.put("highClassic", highClassic);
			}
			if(ExStringUtils.isNotEmpty(stuName)) {
				condition.put("stuName",stuName);
			}
			if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
				condition.put("matriculateNoticeNo", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(isStudyFollow)) {
				condition.put("isStudyFollow", isStudyFollow);
			}

			condition.put("isMatriculate", Constants.BOOLEAN_YES); //是否录取	

//			List<EnrolleeInfo> listResource= enrolleeInfoService.findListByCondition(condition);
			Page page = enrolleeInfoService.findEnrolleeInfoForRegisterList(condition, objPage);
			List<EnrolleeInfo> listResource=page.getResult();
			int  registeredNum = 0,setFirstTermCourseNum = 0;
			int  unDistributionNum=0;
			StringBuffer sb = new StringBuffer();
			if (listResource!=null&&listResource.size()>0) {
				for (EnrolleeInfo ef:listResource) {
					if (ef.getBranchSchool().getResourceid().equals(unit.getResourceid())) {
						unDistributionNum++;
					}else{
						sb.append(ef.getResourceid()+",");
					}
				}
			}
			
			List<StudentInfo> registeredStus= new ArrayList<StudentInfo>();
			synchronized (this) {
				registeredStus = reginfoService.doRegister(sb.toString().split(","));	
			}

			if(null != registeredStus && !registeredStus.isEmpty()){
				registeredNum = registeredStus.size();
				/*~~~~~~~~~~~~~~预约学生首学期课程~~~~~~~~~~*/					
				setFirstTermCourseNum = reginfoService.doRegisterStuFirstTermCourse(registeredStus);
			}							
			String msg="注册学生成功！<br/>注册成功人数：<font color='red'>"+registeredNum+"</font>人.<br/>成功预约首学期课程人数：<font color='red'>"+setFirstTermCourseNum+"</font>人.";
			if (unDistributionNum>0) {
				msg=msg+"未分配教学站人数<font color='red'>"+unDistributionNum+"</font>注册失败";
			}
			map.put("statusCode", 200);
			map.put("message", msg);				
			map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/enrolshcool-list.html");
		} catch (Exception e) {
			map.put("statusCode", 300);
			map.put("message", "注册学生失败:<br/>"+e.getLocalizedMessage());

		}
		renderJson(response, JsonUtils.mapToJson(map));
		
	}
	
	/**
	 * 注册
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/registering.html")
	public void exeRegistering(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				int  registeredNum = 0,setFirstTermCourseNum = 0;
				String[] arr = resourceid.split("\\,");	
				List<StudentInfo> registeredStus= new ArrayList<StudentInfo>();
				synchronized (this) {
					registeredStus = reginfoService.doRegister(arr);	
				}
				
				if(null != registeredStus && !registeredStus.isEmpty()){
					registeredNum = registeredStus.size();
					/*~~~~~~~~~~~~~~预约学生首学期课程~~~~~~~~~~*/					
					setFirstTermCourseNum = reginfoService.doRegisterStuFirstTermCourse(registeredStus);
				}							
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT, "手动注册：resourceids:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "注册学生成功！<br/>注册成功人数：<font color='red'>"+registeredNum+"</font>人.<br/>成功预约首学期课程人数：<font color='red'>"+setFirstTermCourseNum+"</font>人.");				
				map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/enrolshcool-list.html");
			}
			
		} catch (Exception e) {
			logger.error("注册学生失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "注册学生失败:<br/>"+e.getLocalizedMessage());	
		}		
			
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu3/register/studentinfo/registering_getEnrollInfo.html")
	public void exeRegisteringgetEnrollInfo(String recruitPlanID,String certNum,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
//		String certNum = request.getParameter("certNum");
		User user  = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit = user.getOrgUnit();
//		ExameeInfo examee = exameeInfoService.findUniqueByProperty("SFZH", certNum);
		try {
			String hql = "";
			hql=" from "+ExameeInfo.class.getSimpleName()+" where SFZH=? and isDeleted=0 order by recruitPlan.yearInfo.firstYear desc";
			List<ExameeInfo> eeList=exameeInfoService.findByHql(hql, certNum);
			
			String hql2="";
			hql2 = " from "+EnrolleeInfo.class.getSimpleName()+" where recruitMajor.recruitPlan.resourceid=? and branchSchool.resourceid=? and registorFlag='Y' and isDeleted=0 ";
			List<EnrolleeInfo> enList=enrolleeInfoService.findByHql(hql2, recruitPlanID,unit.getResourceid());
			map.put("registeredCount", enList.size());
			if(eeList.size()<=0 || eeList==null){
				map.put("statusCode", 300);
				map.put("message","在系统中未找到该学生的基本信息");
				
			}
			
//			StudentBaseInfo stubase = studentService.findUniqueByProperty("certNum", certNum);
//			if(stubase==null){
//				map.put("statusCode", 300);
//				map.put("message","在系统中未找到该学生的基本信息");
//			}
			
			else{				
				List<Map<String, Object>> enrollInfo = enrolleeInfoService.getEnrollInfoByCertNum(certNum);
				String register_flag=enrollInfo.get(0).get("register_flag").toString();
				if("Y".equalsIgnoreCase(register_flag)){
					register_flag="已注册";
				}else{
					register_flag="未注册";
				}
				map.put("name", enrollInfo.get(0).get("name"));
				map.put("nation", JstlCustomFunction.dictionaryCode2Value("CodeNation", enrollInfo.get(0).get("nation").toString()));
				map.put("gender",JstlCustomFunction.dictionaryCode2Value("CodeSex",  enrollInfo.get(0).get("gender").toString()));
				map.put("certNum", enrollInfo.get(0).get("certNum"));
				map.put("bornday", ExDateUtils.formatDateStr((Date)enrollInfo.get(0).get("bornday"), ExDateUtils.PATTREN_DATE_CN));
				map.put("address", enrollInfo.get(0).get("address"));
				map.put("register_flag",register_flag);
				map.put("unitname", enrollInfo.get(0).get("unitname"));
				map.put("recruitphotopath", enrollInfo.get(0).get("backphotopath"));
				map.put("classic", enrollInfo.get(0).get("classic"));
				map.put("teachingType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",  enrollInfo.get(0).get("teachingType").toString()));
				map.put("major", enrollInfo.get(0).get("major"));
				map.put("registorSerialNO", enrollInfo.get(0).get("registorSerialNO")==null?"":enrollInfo.get(0).get("registorSerialNO"));
				map.put("reportUnitName", unit.getUnitName());
			}
		} catch (Exception e) {
			logger.error("获取学生录取信息失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "获取学生录取信息失败:<br/>"+e.getLocalizedMessage());	
		}
		map.put("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				 + CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu3/register/studentinfo/registering_ByPersonalIDReader.html")
	public void exeRegisteringByPersonalIDReader(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		logger.info("before decode:cardName:"+ExStringUtils.trimToEmpty(request.getParameter("cardName")));	
		String cardName = ExStringUtils.trimToEmpty(request.getParameter("cardName"));		
		User user  = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit = user.getOrgUnit();
//		ExameeInfo examee = exameeInfoService.findUniqueByProperty("SFZH", certNum);
		
		try {
			// 获取学校代码
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			String hql = "";
			hql=" from "+ExameeInfo.class.getSimpleName()+" where SFZH=? and isDeleted=0 order by recruitPlan.yearInfo.firstYear desc";
			List<ExameeInfo> eeList=exameeInfoService.findByHql(hql, certNum);
			//刷身份证注册的功能，注册方法执行前，通过身份证查询学籍表学生的学籍状态，如果有正常在学、休学、结业状态的学生，则提示并拒绝注册
			String hql2 = "";
			hql2=" from "+StudentInfo.class.getSimpleName()+" where studentBaseInfo.certNum=? and isDeleted=0 and studentStatus in ('11','12','24')";
			List<StudentInfo> stuList=studentInfoService.findByHql(hql2, certNum);
			String isAllowMulit = CacheAppManager.getSysConfigurationByCode("is_allow_mulit_schollroll").getParamValue();
			if(eeList.size()<0 || eeList==null){
				map.put("statusCode", 300);
				map.put("message","注册失败，在系统中未找到该学生的基本信息，请联系系统管理人员");
				
			}else if(stuList.size()>0&& stuList!=null && "N".equalsIgnoreCase(isAllowMulit)){
				map.put("statusCode", 300);
				map.put("message","注册失败!在当前系统的学籍模块中已存在该身份证号的学生："+stuList.get(0).getStudentBaseInfo().getCertNum()+
						"，当前的学籍状态为："+JstlCustomFunction.dictionaryCode2Name("CodeStudentStatus", stuList.get(0).getStudentStatus())+",一个学生不允许同时存在多个学籍，请联系成教院招生管理人员核实该学生的信息");
			}
//			else{
//				ExameeInfo ee = eeList.get(0);
//				String ksh="";
//				EnrolleeInfo enrolleeInfo ;
//				String province=CacheAppManager.getSysConfigurationByCode("School.province").getParamValue();//广东省  广西省  安徽省  
//				if(province.contains("广东")){
//					ksh=ee.getZKZH();
//					enrolleeInfo=enrolleeInfoService.findUnique(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=0 and examCertificateNo=? ", ksh);
//				}else{
//					ksh=ee.getKSH();
//					enrolleeInfo=enrolleeInfoService.findUnique(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=0 and enrolleeCode=? ", ksh);
//				}				
//			}
			
			
//			StudentBaseInfo stubase = studentService.findUniqueByProperty("certNum", certNum);
//			if(stubase==null){
//				map.put("statusCode", 300);
//				map.put("message","注册失败，在系统中未找到该学生的基本信息，请联系系统管理人员");
//			}
			else{			
				ExameeInfo ee = eeList.get(0);
				String ksh="";
				EnrolleeInfo enrolleeInfo ;
				String province=CacheAppManager.getSysConfigurationByCode("School.province").getParamValue();//广东省  广西省  安徽省  
				if(province.contains("广东")){//广东省，准考准号唯一（长号）
					ksh=ee.getZKZH();
					enrolleeInfo=enrolleeInfoService.findUnique(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=0 and examCertificateNo=? ", ksh);
				}else{//其他省，考生号唯一
					ksh=ee.getKSH();
					enrolleeInfo=enrolleeInfoService.findUnique(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=0 and enrolleeCode=? ", ksh);
				}	
//				EnrolleeInfo enrolleeInfo=enrolleeInfoService.findUnique(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=0 and studentBaseInfo.resourceid=? ", stubase.getResourceid());
				if(enrolleeInfo!=null){//如果学生未分配教学站，但使用身份证到教学点报到，则以该教学点教务人员的账号所在的教学点，设置为该学生注册报到的教学点
					String enrollName=ExStringUtils.trim(enrolleeInfo.getStudentBaseInfo().getName());
					if(!enrollName.equals(cardName)){//身份证号相同，但是姓名不一致
						map.put("statusCode", 300);
						map.put("message","注册失败:该学生身份证姓名与录取库中的姓名不一致，请联系成教院招生管理人员，由成教院管理人员进行注册");
					}else{
						if(Constants.BOOLEAN_YES.equals(enrolleeInfo.getRegistorFlag())){//是否已注册：是
							map.put("statusCode", 300);
							map.put("message","注册失败:该学生已注册");
						}else {//是否已注册：否
							if(enrolleeInfo.getBranchSchool()==null||"未分配".equals(enrolleeInfo.getBranchSchool().getUnitName())||"4a40f0a044289986014429e4d12b0001".equals(enrolleeInfo.getBranchSchool().getResourceid())){//是否分配教学站：否
								//先检查该教学点是否开设了这个专业，没开设的，不能在该点注册 
								try {
									String hql1="from "+RecruitMajor.class.getSimpleName()+" where isDeleted=0 and  brSchool.resourceid=? and teachingType=? and recruitPlan.resourceid=? and recruitMajorCode=? and classic.classicCode=?";
									RecruitMajor rm=recruitMajorService.findUnique(hql1, new Object[]{ unit.getResourceid(),ee.getXXXSDM(),ee.getRecruitPlan().getResourceid(),ee.getLQZY(),ee.getCCDM()});
									if(rm==null){
										map.put("statusCode", 300);
										map.put("message","注册失败:您的教学点未开设该学生录取的专业，该学生的录取专业为："+enrolleeInfo.getRecruitMajor().getMajor().getMajorName());
									}else{
										enrolleeInfo.setRecruitMajor(rm);
										enrolleeInfo.setBranchSchool(unit);
										if("10602".equals(schoolCode)){
											enrolleeInfo.setRegistorFlag(Constants.BOOLEAN_YES);
											map.put("statusCode", 200);
											map.put("message", "注册成功！");
										}
										enrolleeInfoService.update(enrolleeInfo);
										if(!"10602".equals(schoolCode)){
											doregister(request, map, enrolleeInfo);
										}
										
									}
								} catch (Exception e) {
									String _emsg = "<font  color='red'>"+unit.getUnitName()+" 教学点，录取编码为"+ee.getLQZY()+"有两个相同的招生专业</font>";
									map.put("statusCode", 300);
									map.put("message",_emsg);
									logger.error(_emsg, e);									
								}								
							}else{//是否分配教学站：是
								if("10602".equals(schoolCode)){
									if(!enrolleeInfo.getBranchSchool().getResourceid().equals(unit.getResourceid())){//是否与当前教务员所在教学点一致：否
										enrolleeInfo.setMemo(enrolleeInfo.getBranchSchool().getUnitName()+"转到"+unit.getUnitName());
									}
									enrolleeInfo.setBranchSchool(unit);
									enrolleeInfo.setRegistorFlag(Constants.BOOLEAN_YES);
									enrolleeInfoService.update(enrolleeInfo);
									map.put("statusCode", 200);
									map.put("message", "注册成功！");
								} else {
									if(!enrolleeInfo.getBranchSchool().getResourceid().equals(unit.getResourceid())){//是否与当前教务员所在教学点一致：否
										map.put("statusCode", 300);
										map.put("message","注册失败:该学生已分配到 “"+enrolleeInfo.getBranchSchool().getUnitName()+"” ,如果需要在本教学点注册，请联系成教院招生管理人员重新为学生分配教学点");
									}else{//是否与当前教务员所在教学点一致：是
										doregister(request, map, enrolleeInfo);
									}
								}
							}
						}
					}
					if((Integer)map.get("statusCode")==200){
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT, "身份证注册：身份证号:"+enrolleeInfo.getStudentBaseInfo().getCertNum());
					}
					
				}else{
					map.put("statusCode", 300);
					map.put("message","注册失败，在系统中未找到该学生的注册信息，请联系系统管理人员");
				}
					
			}
		} catch (Exception e) {
			logger.error("注册学生失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "注册学生失败:<br/>"+e.getLocalizedMessage());	
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	private void doregister(HttpServletRequest request,
			Map<String, Object> map, EnrolleeInfo enrolleeInfo) {
		int  registeredNum = 0,setFirstTermCourseNum = 0;
		//String[] arr = resourceid.split("\\,");
		String[] arr = {enrolleeInfo.getResourceid()};
		List<StudentInfo> registeredStus= new ArrayList<StudentInfo>();
		synchronized (this) {
			registeredStus = reginfoService.doRegister(arr);	
		}
		
		if(null != registeredStus && !registeredStus.isEmpty()){
			registeredNum = registeredStus.size();
			/*~~~~~~~~~~~~~~预约学生首学期课程~~~~~~~~~~*/					
			setFirstTermCourseNum = reginfoService.doRegisterStuFirstTermCourse(registeredStus);
		}							
		map.put("registorSerialNO", enrolleeInfo.getRegistorSerialNO()==null?"":enrolleeInfo.getRegistorSerialNO());
		map.put("statusCode", 200);
		map.put("message", "注册学生成功！<br/>注册成功人数：<font color='red'>"+registeredNum+"</font>人.<br/>成功预约首学期课程人数：<font color='red'>"+setFirstTermCourseNum+"</font>人.");				
		map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/enrolshcool-list.html");
	}
	/**
	 * 注销注册
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/back-to-registering.html")
	public void backRegistering(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				int  registeredNum = 0;
				String[] arr = resourceid.split("\\,");			
				for(String studentId:arr){
					StudentInfo studentInfo = studentInfoService.get(studentId);	
					if(null != studentInfo){
						/*~~~~~~~~~~~~~~删除相关的操作~~~~~~~~~*/					
						reginfoService.deleteRegisterStuFirstTermCourse(studentInfo);
						reginfoService.deleteUserOrRole(studentInfo.getResourceid(), studentInfo.getSysUser().getResourceid());
						registeredNum++;
					}	
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.DELETE, "注销注册：resourceids:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "注销注册成功！<br/>注销注册成功人数：<font color='red'>"+registeredNum+"</font>人");				
				map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/enrolshcool-list.html");
			}
			
		} catch (Exception e) {
			logger.error("注销注册学生失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "注销注册学生失败:<br/>"+e.getLocalizedMessage());	
		}		
			
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 自动注册
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/autoregister.html")
	public void exeAutoRegister(String act,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String, Object> condition = new HashMap<String, Object>();
		List<EnrolleeInfo> registerList = new ArrayList<EnrolleeInfo>();
		try{	
			Grade grade = gradeService.findUnique("from "+Grade.class.getSimpleName()+" where isDefaultGrade = ?", Constants.BOOLEAN_YES);
			condition.put("grade", grade.getResourceid());
			int gradeYear =  new Long(grade.getYearInfo().getFirstYear()).intValue();
			if("2".equals(grade.getTerm())){//如果为第二学期，则年份+1
				gradeYear = gradeYear+1;
			}
			condition.put("gradeYear",gradeYear);
			condition.put("isMatriculate", Constants.BOOLEAN_YES);				
			synchronized (this) {
				registerList = reginfoService.countRegisterStudent(condition);
			}
			
			if("count".equals(act)){//统计符合注册条件的人数，提示确认		
				
				map.put("registerNum", registerList.size());
				
			}else if("register".equals(act)){//注册
				int  registeredNum = 0,setFirstTermCourseNum = 0;
				if(null != registerList && !registerList.isEmpty()){
					String[] regResourceIds = new String[registerList.size()];
					for(int i= 0;i<registerList.size();i++){
						EnrolleeInfo enrolleeInfo = (EnrolleeInfo)registerList.get(i);
						regResourceIds[i] = enrolleeInfo.getResourceid();					
					}
					List<StudentInfo> registeredStus =new ArrayList<StudentInfo>();
					synchronized (this) {
					 registeredStus = reginfoService.doRegister(regResourceIds);
					}
					if(null != registeredStus && !registeredStus.isEmpty()) {
						registeredNum = registeredStus.size();
						//预约新生首学期课程						
						setFirstTermCourseNum = reginfoService.doRegisterStuFirstTermCourse(registeredStus);
					}			
				
					
				}
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, "自动注册：条数:"+registerList.size());
				map.put("message", "注册学生成功！<br/>注册成功人数：<font color='red'>"+registeredNum+"</font>人.<br/>成功预约首学期课程人数：<font color='red'>"+setFirstTermCourseNum+"</font>人.");				
				map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/enrolshcool-list.html");
			}
			map.put("statusCode", 200);
		}catch (Exception e) {
			logger.error("自动注册失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "自动注册学生失败：<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	
	
	
	/**
	 * 学籍信息管理
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/schoolroll-list.html")
	public String exeSchoolRoll(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
		String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
		String stuStatusRes			 = stuStatus;
		String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		String contactAddress        = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));//联系地址
		String isFromPage            = ExStringUtils.trimToEmpty(request.getParameter("isFromPage"));//从页面来
		String endDate				 = ExStringUtils.trimToEmpty(request.getParameter("endDate"));
		String mobile                = ExStringUtils.trimToEmpty(request.getParameter("mobile"));
//		if(ExStringUtils.isNotEmpty(isFromPage)||ExStringUtils.isEmpty(flag)){
//			objPage.setPageSize(100);
//		}
		
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		if(!ExStringUtils.isNotEmpty(classesid)) {
			classesid = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		}
		if(!ExStringUtils.isNotEmpty(classesid)) {
			classesid = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		}
		String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(endDate)) {
			condition.put("endDate", ExDateUtils.convertToDate(endDate));
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("studyno", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(stuGrade)) {
			condition.put("stuGrade", stuGrade);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(entranceFlag)) {
			condition.put("entranceFlag", entranceFlag);
		}
		if(ExStringUtils.isNotEmpty(accountStatus)) {
			condition.put("accountStatus", accountStatus);
		}
		if(ExStringUtils.isNotEmpty(contactAddress)) {
			condition.put("contactAddress", contactAddress);
		}
		if(ExStringUtils.isNotEmpty(mobile)) {
			condition.put("mobile", mobile);
		}
		if(ExStringUtils.isNotBlank(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotEmpty(rollCard)) {
			condition.put("rollCard", rollCard);
		}
		String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
		if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
			condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
		}
		//condition.put("isSysUser", Constants.BOOLEAN_YES);
		Page page = studentInfoService.findStuByCondition(condition, objPage);		

		if (ExStringUtils.isNotBlank(stuStatus)&&"11".equals(stuStatus)&&ExStringUtils.isNotEmpty(accountStatus)) {
			stuStatusRes = "1".equals(accountStatus) ?"a11":"b11";
		}
		condition.put("endDate", endDate);
		model.addAttribute("stulist", page);
		condition.put("flag", "1");
		model.addAttribute("condition", condition);
		model=getNewStudentStatus(model);
		model.addAttribute("stuStatusRes", stuStatusRes);
		
		return "/edu3/roll/sturegister/schoolroll-list";
	}
	
	/**
	 * 学籍库
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/schoolinfo-list.html")
	public String exeSchoolInfo(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
		String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
		String stuStatusRes			 = stuStatus;
		String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		String contactAddress        = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));//联系地址
		String isFromPage            = ExStringUtils.trimToEmpty(request.getParameter("isFromPage"));//从页面来
		String havePhoto             = ExStringUtils.trimToEmpty(request.getParameter("havePhoto"));//是否有相片
//		if(ExStringUtils.isNotEmpty(isFromPage)||ExStringUtils.isEmpty(flag)){
//			objPage.setPageSize(100);
//		}
		
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		if(!ExStringUtils.isNotEmpty(classesid)) {
			classesid = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		}
		if(!ExStringUtils.isNotEmpty(classesid)) {
			classesid = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		}
		String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(stuGrade)) {
			condition.put("stuGrade", stuGrade);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(entranceFlag)) {
			condition.put("entranceFlag", entranceFlag);
		}
		if(ExStringUtils.isNotEmpty(accountStatus)) {
			condition.put("accountStatus", accountStatus);
		}
		if(ExStringUtils.isNotEmpty(contactAddress)) {
			condition.put("contactAddress", contactAddress);
		}
		if(ExStringUtils.isNotBlank(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotEmpty(rollCard)) {
			condition.put("rollCard", rollCard);
		}
		if(ExStringUtils.isNotEmpty(havePhoto)) {
			condition.put("havePhoto", havePhoto);
		}
//		String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
//		if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
		//condition.put("isSysUser", Constants.BOOLEAN_YES);
		Page page = studentInfoService.findStudentByCondition(condition, objPage);		
		
//		if (ExStringUtils.isNotBlank(stuStatus)&&"11".equals(stuStatus)&&ExStringUtils.isNotEmpty(accountStatus)) {
//			stuStatusRes = accountStatus.equals("1")?"a11":"b11";
//		}
		model.addAttribute("stulist", page);
		condition.put("flag", "1");
		model.addAttribute("condition", condition);
		model=getNewStudentStatus(model);
		model.addAttribute("stuStatusRes", stuStatusRes);
		
		return "/edu3/roll/sturegister/schoolinfo-list";
	}
	
	
	/**
	 * 学籍信息管理--班主任角色
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/classteacher-list.html")
	public String exeClassTeacherSchoolRoll(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
		String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
		String stuStatusRes			 = stuStatus;
		String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		String contactAddress        = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));//联系地址
		String contactZipcode        = ExStringUtils.trimToEmpty(request.getParameter("contactZipcode"));//邮政编号
		String isFromPage            = ExStringUtils.trimToEmpty(request.getParameter("isFromPage"));//从页面来
//		if(ExStringUtils.isNotEmpty(isFromPage)||ExStringUtils.isEmpty(flag)){
//			objPage.setPageSize(100);
//		}
		
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(stuGrade)) {
			condition.put("stuGrade", stuGrade);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(entranceFlag)) {
			condition.put("entranceFlag", entranceFlag);
		}
		if(ExStringUtils.isNotEmpty(accountStatus)) {
			condition.put("accountStatus", accountStatus);
		}
		if(ExStringUtils.isNotEmpty(contactAddress)) {
			condition.put("contactAddress", contactAddress);
		}
		if(ExStringUtils.isNotEmpty(contactZipcode)) {
			condition.put("contactZipcode", contactZipcode);
		}
		if(ExStringUtils.isNotBlank(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotEmpty(rollCard)) {
			condition.put("rollCard", rollCard);
		}
		String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
		if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
			condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
		}
		//condition.put("isSysUser", Constants.BOOLEAN_YES);
		Page page = new Page();
		if(user.isContainRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.master").getParamValue())){
			condition.put("classesMasterId", user.getResourceid());
			page = studentInfoService.findStuByCondition(condition, objPage);		
		}
		

		if (ExStringUtils.isNotBlank(stuStatus)&&"11".equals(stuStatus)&&ExStringUtils.isNotEmpty(accountStatus)) {
			stuStatusRes = "1".equals(accountStatus) ?"a11":"b11";
		}
		model.addAttribute("stulist", page);
		condition.put("flag", "1");
		model.addAttribute("condition", condition);
		model=getNewStudentStatus(model);
		model.addAttribute("stuStatusRes", stuStatusRes);
		/*StringBuffer unitOption = new StringBuffer("<option value=''></option>");
		//List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		List<OrgUnit> orgList = orgUnitService.findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitCode asc ");
		if(null != orgList && orgList.size()>0){
			for(OrgUnit orgUnit : orgList){
				if(ExStringUtils.isNotEmpty(branchSchool)&&branchSchool.equals(orgUnit.getResourceid())){
					unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}else{
					unitOption.append("<option value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}
				
				
			}
		}
		model.addAttribute("unitOption", unitOption);
		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		
		for (Major m : majors) {
			if(ExStringUtils.isNotEmpty(major)&&major.equals(m.getResourceid())){
				majorOption.append("<option value ='"+m.getResourceid()+"' selected='selected' >"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}else{
				majorOption.append("<option value ='"+m.getResourceid()+"'>"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}
			
		}
		//如果已经选择了学习中心，就要过滤出相应的专业
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("unitId", branchSchool);
		}
		List<Map<String,Object>> teachingPlanMajors = teachingPlanService.getUnitTeachingPlanMajor(condition);
	
		majorOption = new StringBuffer("<option value=''></option>");
		
		for (Map<String,Object> m : teachingPlanMajors) {
			if(ExStringUtils.isNotEmpty(major)&&major.equals(m.get("resourceid"))){
				majorOption.append("<option value ='"+m.get("resourceid")+"' selected='selected' >"+m.get("majorinfo")+"</option>");
			}else{
				majorOption.append("<option value ='"+m.get("resourceid")+"'>"+m.get("majorinfo")+"</option>");
			}
		}
		model.addAttribute("majorOption", majorOption);
		model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(stuGrade,major,"classteacher_major","major","searchClassteacherMajorClick()",classic));
		model.addAttribute("classesSelect4",graduationQualifService.getGradeToMajorToClasses1(stuGrade,major,classesid,branchSchool,"classteacher_classesid","classesid",classic));*/
		return "/edu3/roll/sturegister/classteacher-list";
	}
	
	
	/**
	 * 修改学籍
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/register/studentinfo/editstu.html")
	public String exeEditStu(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			StudentInfo studentInfo = studentInfoService.get(resourceid);	
			model.addAttribute("studentInfo", studentInfo);
			//因为需要修改籍贯，籍贯需要将一个homeplace的字段转换成省市区三级
			String homePlace = ExStringUtils.trimToEmpty(studentInfo.getStudentBaseInfo().getHomePlace());
			homePlace = homePlace.replaceAll(",", "");
			String hpProvince = "";
			String hpCity = "";
			String hpDistrict="";
			if(homePlace.contains("省")){
				hpProvince = homePlace.substring(0, homePlace.indexOf("省")+1);
			}else if(homePlace.contains("特别行政区")||homePlace.contains("自治区")){
				hpProvince = homePlace.substring(0, homePlace.indexOf("区")+1);
			}else if(homePlace.contains("北京市")||homePlace.contains("上海市")||homePlace.contains("天津市")||homePlace.contains("重庆市")) {
				hpProvince = homePlace.substring(0, homePlace.indexOf("市")+1);
			}
			homePlace = homePlace.replaceAll(hpProvince, "");
			if(homePlace.contains("市")){
				hpCity = homePlace.substring(0,homePlace.indexOf("市")+1);
			}else if (homePlace.contains("自治州")){
				hpCity = homePlace.substring(0,homePlace.indexOf("州")+1);
			}
			homePlace = homePlace.replaceAll(hpCity, "");
			hpDistrict=homePlace;
			model.addAttribute("homePlaceProvince", hpProvince);
			model.addAttribute("homePlaceCity", hpCity);
			model.addAttribute("homePlaceDistrict", hpDistrict);
			
			//户口所在地
			String residence = ExStringUtils.trimToEmpty(studentInfo.getStudentBaseInfo().getResidence());
			residence = residence.replaceAll(",", "");
			String rProvince = "";
			String rCity = "";
			String rDistrict="";
			if(residence.contains("省")){
				rProvince = residence.substring(0, residence.indexOf("省")+1);
			}else if(residence.contains("特别行政区")||residence.contains("自治区")){
				rProvince = residence.substring(0, residence.indexOf("区")+1);
			}else if(residence.contains("北京市")||residence.contains("上海市")||residence.contains("天津市")||residence.contains("重庆市")) {
				rProvince = residence.substring(0, residence.indexOf("市")+1);
			}
			residence = residence.replaceAll(rProvince, "");
			if(residence.contains("市")){
				rCity = residence.substring(0,residence.indexOf("市")+1);
			}else if (residence.contains("自治州")){
				rCity = residence.substring(0,residence.indexOf("州")+1);
			}
			residence = residence.replaceAll(rCity, "");
			rDistrict=residence;
			model.addAttribute("HouseholdRegisterationProvince", rProvince);
			model.addAttribute("HouseholdRegisterationCity", rCity);
			model.addAttribute("HouseholdRegisterationDistrict", rDistrict);
			model=getNewStudentStatus(model);
			String stuStudentStatus = studentInfo.getStudentStatus();
			Integer accountStatus = studentInfo.getAccountStatus();
			if("11".equals(stuStudentStatus)){
				if(1==accountStatus){
					stuStudentStatus="a11";
				}else if(0==accountStatus){
					stuStudentStatus="b11";
				}
			}
			model.addAttribute("stuStatusRes" ,stuStudentStatus);
			model.addAttribute("schoolCode" ,CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode"));
			model.addAttribute("current",SpringSecurityHelper.getCurrentUser().getUsername());
			model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					 + CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
			model.addAttribute("phoneComfirm",CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue());
		}
		return "/edu3/roll/sturegister/schoolroll-form";
	}
	
	/**
	 * 保存审核学籍是否通过
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/auditResults.html")
	public void auditResults(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {	
			if(ExStringUtils.isNotEmpty(resourceid)){
				StudentInfo studentInfo = studentInfoService.get(resourceid);				
				String auditResults = ExStringUtils.trimToEmpty(request.getParameter("auditResults"));
				studentInfo.setAuditResults(auditResults);
				studentInfoService.saveOrUpdate(studentInfo);			
			}
				map.put("statusCode", 200);
				map.put("message", "审核成功！");
				map.put("navTabId", "RES_SCHOOL_SCHOOLROLL_INFO_AUDIT");
		}catch (Exception e) {
				logger.error("审核出错：{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "审核失败！");
			
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"审核入学资格："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	/**
	 * 审核学生修改的学籍
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/register/studentinfo/audit.html")
	public String audit(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid)){
			StudentInfo studentInfo = studentInfoService.get(resourceid);	
			model.addAttribute("studentInfo", studentInfo);
			//0 未审核 1 审核通过 2审核不通过     已审核的有撤销和审核不通过选择    撤销是回退到未审核状态  ，
			if(studentInfo!=null){
				if("1".equals(studentInfo.getAuditResults())){
					model.addAttribute("auditResults","Y"); 
				}else{
					model.addAttribute("auditResults","N");
				}
			}
		}
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
		return "/edu3/roll/sturegister/studentinfo-audit";
	}
	
	/**
	 * 设置学生修改学籍开放时间
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 * @throws ParseException 
	 */
	@RequestMapping("/edu3/register/studentinfo/changeStudentInfoTime.html")
	public String changeStudentInfoTime(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException, ParseException{
		String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
		
		model.addAttribute("stus", stus);
//		if(ExStringUtils.isNotBlank(resourceid)){
//			StudentInfo studentInfo = studentInfoService.get(resourceid);	
//			model.addAttribute("studentInfo", studentInfo);
//			model.addAttribute("stus", ExStringUtils.trimToEmpty(request.getParameter("stus")));
//			}
		return "/edu3/roll/sturegister/changestudentinfo-time";	
	}
	
	/**
	 * 保存设置修改学生学籍期限
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/saveDate.html")
	public void saveDate(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
		String stus = request.getParameter("stus");
		String[] stuList = stus.split(",");
		for(String s: stuList){
			if(ExStringUtils.isNotEmpty(s)){
				StudentInfo studentInfo = studentInfoService.get(s);				
				String openStartDate = ExStringUtils.trimToEmpty(request.getParameter("openStartDate"));
				String openEndDate = ExStringUtils.trimToEmpty(request.getParameter("openEndDate"));
				studentInfo.setOpenStartDate(openStartDate);
				studentInfo.setOpenEndDate(openEndDate);
				studentInfo.setAuditResults("3");//0表示管理已设置修改学籍时间段
				studentInfoService.saveOrUpdate(studentInfo);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_SCHOOL_SCHOOLROLL_MANAGER");	
			}else{
				map.put("statusCode", 300);
				map.put("message", "保存失败！");
			}
		}	
						
		}catch (Exception e) {
				logger.error("保存时间出错：{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "保存失败！");
			
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"修改学生学籍修改期限："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 学籍信息审核
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/changeschoolroll-audit.html")
	public String schoolrollAudit(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
		String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
		String stuStatusRes			 = stuStatus;
		String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
//		String contactAddress        = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));//联系地址
		String auditResults        	 = ExStringUtils.trimToEmpty(request.getParameter("auditResults"));//审核结果
		String isFromPage            = ExStringUtils.trimToEmpty(request.getParameter("isFromPage"));//从页面来
		
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(stuGrade)) {
			condition.put("stuGrade", stuGrade);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(entranceFlag)) {
			condition.put("entranceFlag", entranceFlag);
		}
		if(ExStringUtils.isNotEmpty(accountStatus)) {
			condition.put("accountStatus", accountStatus);
		}
//		if(ExStringUtils.isNotEmpty(contactAddress))      condition.put("contactAddress", contactAddress);
		if(ExStringUtils.isNotEmpty(auditResults)) {
			condition.put("auditResults", auditResults);
		}
		if(ExStringUtils.isNotBlank(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotEmpty(rollCard)) {
			condition.put("rollCard", rollCard);
		}
		String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
		if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
			condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
		}
		//condition.put("isSysUser", Constants.BOOLEAN_YES);
		//已审核修改学籍的学生不显示
		Page page = studentInfoService. findStuAuditResultsByCondition(condition, objPage);		

		if (ExStringUtils.isNotBlank(stuStatus)&&"11".equals(stuStatus)&&ExStringUtils.isNotEmpty(accountStatus)) {
			stuStatusRes = "1".equals(accountStatus) ?"a11":"b11";
		}
		model.addAttribute("stulist", page);
		condition.put("flag", "1");
		model.addAttribute("condition", condition);
		model=getNewStudentStatus(model);
		model.addAttribute("stuStatusRes", stuStatusRes);
		
		/*StringBuffer unitOption = new StringBuffer("<option value=''></option>");
		//List<OrgUnit> orgList = (List<OrgUnit>)EhCacheManager.getCache(CacheSecManager.CACHE_SEC_ORGS).get(CacheSecManager.CACHE_SEC_ORGS);
		List<OrgUnit> orgList = orgUnitService.findByHql(" from "+OrgUnit.class.getSimpleName()+" where isDeleted= 0 and unitType ='brSchool' order by unitCode asc ");
		if(null != orgList && orgList.size()>0){
			for(OrgUnit orgUnit : orgList){
				if(ExStringUtils.isNotEmpty(branchSchool)&&branchSchool.equals(orgUnit.getResourceid())){
					unitOption.append("<option selected='selected' value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}else{
					unitOption.append("<option value='"+orgUnit.getResourceid()+"'");				
					unitOption.append(">"+orgUnit.getUnitCode()+"-"+orgUnit.getUnitName()+"</option>");
				}
				
				
			}
		}
		model.addAttribute("unitOption", unitOption);
		*/
//		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
//		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
//		
//		for (Major m : majors) {
//			if(ExStringUtils.isNotEmpty(major)&&major.equals(m.getResourceid())){
//				majorOption.append("<option value ='"+m.getResourceid()+"' selected='selected' >"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
//			}else{
//				majorOption.append("<option value ='"+m.getResourceid()+"'>"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
//			}
//			
//		}
//		//如果已经选择了学习中心，就要过滤出相应的专业
//		if(ExStringUtils.isNotEmpty(branchSchool)){
//			condition.put("unitId", branchSchool);
//		}
//		List<Map<String,Object>> teachingPlanMajors = teachingPlanService.getUnitTeachingPlanMajor(condition);
//	
//		majorOption = new StringBuffer("<option value=''></option>");
//		
//		for (Map<String,Object> m : teachingPlanMajors) {
//			if(ExStringUtils.isNotEmpty(major)&&major.equals(m.get("resourceid"))){
//				majorOption.append("<option value ='"+m.get("resourceid")+"' selected='selected' >"+m.get("majorinfo")+"</option>");
//			}else{
//				majorOption.append("<option value ='"+m.get("resourceid")+"'>"+m.get("majorinfo")+"</option>");
//			}
//		}
//		model.addAttribute("majorOption", majorOption);
//		model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(stuGrade,major,"changeschool_major","major","searchExamResultMajorClick()",classic));
//		model.addAttribute("classesSelect4",graduationQualifService.getGradeToMajorToClasses1(stuGrade,major,classesid,branchSchool,"changeschool_classesid","classesId",classic));

		return "/edu3/roll/sturegister/changeschoolroll-list";
	}
	
	/**
	 * 查看个人学籍信息
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/studentinfo/view.html")
	public String viewStudentInfop(String resourceid,String studyNo,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotBlank(resourceid,studyNo)){
			Map<String,Object> condition = new HashMap<String, Object>();
			try {
				//查找该学生的学籍信息
				StudentInfo studentInfo = null;
				if (ExStringUtils.isNotBlank(resourceid)) {
					studentInfo = studentInfoService.get(resourceid);
				} else if (ExStringUtils.isNotBlank(studyNo)) {
					studentInfo = studentInfoService.findUniqueByProperty("studyNo",studyNo);
				}

				model.addAttribute("studentInfo", studentInfo);
				//查找该学生的考试成绩
				List<StudentExamResultsVo> examResults =  studentExamResultsService.studentExamResultsList(studentInfo,null);
				model.addAttribute("examResults", examResults);
				if(examResults!=null && examResults.size()>0){
					model.addAttribute("lastExamResultsVo", examResults.get(examResults.size()-1));
				}
				//查找该学生缴费标准
				condition.clear();
				condition.put("studyNo", studentInfo.getStudyNo());
				condition.put("studentInfoId", studentInfo.getResourceid());
				List<StudentPayment> studentPayments =  studentPaymentService.findStudentPaymentByCondition(condition);
				model.addAttribute("studentPayments", studentPayments);
//				List<StudentPayment> studentPayments = studentPaymentService.findByHql("from "+StudentPayment.class.getSimpleName()+" where isDeleted = ? and studyNo = ?  order by  chargeYear", 
//						0,studentInfo.getStudyNo());
//				if(null != studentPayments && studentPayments.size()>0){
					
//					Double totalFactFee = 0.0;
//					if(null != studentPayments && studentPayments.size()>0){
//						for(StudentPayment studentPayment : studentPayments){ 
//							if(studentPayment.getFacepayFee()!=null)
//							totalFactFee+=studentPayment.getFacepayFee();
//						}
//						
//					}
//					String totalFactFee1 = totalFactFee.toString();
//					model.addAttribute("totalFactFee", totalFactFee1);
//				}	
//				condition.clear();
//				condition.put("stuInfoId", studentInfo.getResourceid());
//				condition.put("orderby", " chargeyear desc ");
			} catch (Exception e) {
				logger.error("查看学生学籍出错：{}",e.fillInStackTrace());
			}
			
			model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
					+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
		}
		return "/edu3/roll/sturegister/schoolroll-view";		
	}
	
	/**
	 * 基本信息-通讯录、或查看非学籍信息的页面
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/studentinfo/base/view.html")
	public String viewBaseStudentInfop(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		if(ExStringUtils.isNotEmpty(resourceid)){
			StudentInfo studentInfo = studentInfoService.get(resourceid);	
			model.addAttribute("studentInfo", studentInfo);
			
			String photopathroot = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue()
				+ "common/students/";
			model.addAttribute("photopathroot", photopathroot);
		}
		return "/edu3/roll/sturegister/schoolroll-base-view";		
	}
	
	
	/**
	 * 保存更新表单
	 * @param resourceid
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/savestu.html")
	public void exeSave(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		boolean isExist = false;
		try {
			//保存前需要验证身份证号是否存在
			/*
			List<StudentInfo> students = studentInfoService.getAll();
			for (StudentInfo studentInfo : students) {
				String certNum =studentInfo.getStudentBaseInfo().getCertNum();
				String status = studentInfo.getStudentStatus();
				if(certNum.equals(request.getParameter("certNum"))&&!"15".equals(status)&&!"14".equals(status)&&!"18".equals(status)&&!"16".equals(status)){	
					isExist = true;
					throw new Exception("存在的相同的身份证号");
				}
			}*/
//			以上的方法会很慢
//			改用jdbc的sql方式
			/*
			List<Map<String,Object>> students = studentInfoChangeJDBCService.getStudentInfoStatusWithBaseInfo();
			for (Map<String, Object> map2 : students) {
				
				String status = null!=map2.get("STUDENTSTATUS")?map2.get("STUDENTSTATUS").toString():"";
				String certNum = null!=map2.get("CERTNUM")?map2.get("CERTNUM").toString():"";
				if(certNum.equals(request.getParameter("certNum"))&&!"15".equals(status)&&!"14".equals(status)&&!"18".equals(status)&&!"16".equals(status)){	
					String certNumOld = studentInfoService.get(resourceid).getStudentBaseInfo().getCertNum();
					if(!certNumOld.equals(certNum)){//当为本身的身份证号的时候，不抛异常
						isExist = true;
						throw new Exception("存在的相同的身份证号");
					}
				}
				
			}
			*/
			//-------------------------------------------------------------------保存
			do{
				if(ExStringUtils.isNotEmpty(resourceid)){
					StudentInfo stu = studentInfoService.get(resourceid);
					//由于目前默认关闭了学籍更改的日志记录功能，在此特别开启
					stu.setEnableLogHistory(true);
					setParams(request,stu);
					/**
					 * 在学籍信息里修改学籍信息里上传证件一律设为未审核状态 2014-6-19 12:56:23
					 */ 
					if(ExStringUtils.isNotEmpty(request.getParameter("editImg"))){
						stu.setAuditResults("2"); //图片审核状态：0为未审核
					}
					if("请选择,请选择,请选择".equals(stu.getStudentBaseInfo().getHomePlace())){
						stu.getStudentBaseInfo().setHomePlace(null);
					}
					if("请选择,请选择,请选择".equals(stu.getStudentBaseInfo().getResidence())){
						stu.getStudentBaseInfo().setResidence(null);
					}
					studentInfoService.saveOrUpdate(stu);
					//2012.11.27增加  修改学生名字同时修改用户表的cnName
					User stuUser = stu.getSysUser();
					stuUser.setCnName(stu.getStudentName());
					//2016.12.22 修改身份证时，同时修改用户的登录名，保持用户名与身份证号一致   lion
					stuUser.setUsername(stu.getStudentBaseInfo().getCertNum());
					userService.save(stuUser);
					//如果学籍状态从"毕业"调整成其他的其他状态
					String studentStatus = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
					/*
					if(!"16".equals(studentStatus)){
						List<GraduateData> graduateDatas = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+ " where  isDeleted = 0 and  studentInfo.resourceid=? ",resourceid );
						if(graduateDatas.size()>0){
							GraduateData tmp_graduatedata = graduateDatas.get(0);
							StudentInfo tmp_studentInfo = tmp_graduatedata.getStudentInfo();
							//tmp_studentInfo.setDegreeStatus(Constants.BOOLEAN_WAIT);
							studentInfoService.update(tmp_studentInfo);
							tmp_graduatedata.setIsDeleted(1);
							graduateDataService.update(tmp_graduatedata);
						}
					}
					*/
				}
			} while(false);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SCHOOL_ROLL_MANAGER");
		}catch (Exception e) {
			logger.error("保存学籍出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			if(isExist==true){
				map.put("message", "已经存在同一身份证号的有效学籍信息");
			}else{
				map.put("message", "保存失败！");
			}
			
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"保存学生学籍表："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}

	private void setParams(HttpServletRequest request,StudentInfo stu){
		// ~~~~~~~~~~~~~~~学籍信息~~~~~~~~~~~~~~~~
		
		/*
		 * 已移到学籍异动中
		String major = request.getParameter("major");
		String grade = request.getParameter("grade");
		String classic = request.getParameter("classic");
		String learningStyle = request.getParameter("learningStyle");
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//校外学习中心
		*/
		String attendAdvancedStudies = ExStringUtils.trimToEmpty(request.getParameter("attendAdvancedStudies"));//进修方式
		String studyInSchool = ExStringUtils.trimToEmpty(request.getParameter("studyInSchool"));//就读方式
		String studentKind = ExStringUtils.trimToEmpty(request.getParameter("studentKind"));//学习类别
		String learingStatus = ExStringUtils.trimToEmpty(request.getParameter("learingStatus"));//学习状态
		String studentStatus = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));//学籍状态
		String memo = ExStringUtils.trimToEmpty(request.getParameter("memo"));//备注
		String stuIndate = ExStringUtils.trimToEmpty(request.getParameter("stuIndate"));//入学日期
		String isStudyFollow = ExStringUtils.trimToEmpty(request.getParameter("isStudyFollow"));//是否跟读
		String selfAssessment = ExStringUtils.trimToEmpty(request.getParameter("selfAssessment"));//自我鉴定
		//增加组合学籍 在学+激活=正常注册    在学+停用=正常未注册
		String accountStatus = null;
		if(studentStatus.contains("a")){
			accountStatus="1";
			studentStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(studentStatus.contains("b")){
			accountStatus="0";
			studentStatus="11";//去掉学籍中有关帐号状态的标记
		}
		if(null!=accountStatus) {
			stu.setAccountStatus(Integer.valueOf(accountStatus));
		}
		//if(ExStringUtils.isNotEmpty(branchSchool)){
			//OrgUnit orgUnit = orgUnitService.get(branchSchool);
		//	stu.setBranchSchool(orgUnit);
		//}

		if(ExStringUtils.isNotBlank(attendAdvancedStudies)) {
			stu.setAttendAdvancedStudies(attendAdvancedStudies);
		}
		if(ExStringUtils.isNotBlank(studyInSchool)) {
			stu.setStudyInSchool(studyInSchool);
		}
		if(ExStringUtils.isNotBlank(studentKind)) {
			stu.setStudentKind(studentKind);
		}
		if(ExStringUtils.isNotBlank(learingStatus)) {
			stu.setLearingStatus(learingStatus);
		}
		if(ExStringUtils.isNotBlank(studentStatus)) {
			stu.setStudentStatus(studentStatus);
		}
		if(ExStringUtils.isNotBlank(memo)) {
			stu.setMemo(memo);
		}
		if(ExStringUtils.isNotBlank(selfAssessment)) {
			stu.setSelfAssessment(selfAssessment);
		}
		if(ExStringUtils.isNotBlank(stuIndate)) {
			try {
				stu.setInDate(ExDateUtils.parseDate(stuIndate,ExDateUtils.PATTREN_DATE)); //设置入学日期
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		
		if(ExStringUtils.isEmpty(isStudyFollow)){
			isStudyFollow = "N";
		}
		stu.setIsStudyFollow(isStudyFollow);
		/**
		 * 新增  户口相片路径 ， 其他相片路径
		 */
		stu.setBookletPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("bookletPhotoPath"))); //户口册相片
		stu.setOtherPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("otherPhotoPath"))); //其他相片
		
		//~~~~~~~~~~~~~~~~基本信息~~~~~~~~~~~~
		StudentBaseInfo stuinfo = stu.getStudentBaseInfo();
		//由于目前默认关闭了学籍更改的日志记录功能，在此特别开启
		stuinfo.setEnableLogHistory(true);
		String contactAddress = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));
		String contactZipcode = ExStringUtils.trimToEmpty(request.getParameter("contactZipcode"));
		String contactPhone = ExStringUtils.trimToEmpty(request.getParameter("contactPhone"));
		String mobile = ExStringUtils.trimToEmpty(request.getParameter("mobile"));
		String email = ExStringUtils.trimToEmpty(request.getParameter("email"));
		String homePage = ExStringUtils.trimToEmpty(request.getParameter("homePage"));
		//String height = ExStringUtils.trimToEmpty(request.getParameter("height"));
		String bloodType = ExStringUtils.trimToEmpty(request.getParameter("bloodType"));
		//String bornDay = ExStringUtils.trimToEmpty(request.getParameter("bornDay"));
		String bornAddress = ExStringUtils.trimToEmpty(request.getParameter("bornAddress"));
		String country = ExStringUtils.trimToEmpty(request.getParameter("country"));
		String certType = ExStringUtils.trimToEmpty(request.getParameter("certType"));
		String homePlace = ExStringUtils.trimToEmpty(request.getParameter("homePlaceAll"));
//		String homePlace_province = ExStringUtils.trimToEmpty(request.getParameter("homePlace_province")); 
//		String homePlace_city = ExStringUtils.trimToEmpty(request.getParameter("homePlace_city"));
//		String homePlace_county = ExStringUtils.trimToEmpty(request.getParameter("homePlace_county"));
//		String homePlace = new StringBuffer().append(homePlace_province).append(homePlace_city).append(homePlace_county).toString();
		String gaqCode = ExStringUtils.trimToEmpty(request.getParameter("gaqCode"));
		String nation = ExStringUtils.trimToEmpty(request.getParameter("nation"));
		String health = ExStringUtils.trimToEmpty(request.getParameter("health"));
		String marriage = ExStringUtils.trimToEmpty(request.getParameter("marriage"));
		String politics = ExStringUtils.trimToEmpty(request.getParameter("politics"));
		String faith = ExStringUtils.trimToEmpty(request.getParameter("faith"));
		String residenceKind = ExStringUtils.trimToEmpty(request.getParameter("residenceKind"));
		String residence = ExStringUtils.trimToEmpty(request.getParameter("residenceAll"));
		//String residence = ExStringUtils.trimToEmpty(request.getParameter("residence"));
		String currenAddress = ExStringUtils.trimToEmpty(request.getParameter("currenAddress"));
		String homeAddress = ExStringUtils.trimToEmpty(request.getParameter("homeAddress"));
		String homezipCode = ExStringUtils.trimToEmpty(request.getParameter("homezipCode"));
		String homePhone = ExStringUtils.trimToEmpty(request.getParameter("homePhone"));
		String officeName = ExStringUtils.trimToEmpty(request.getParameter("officeName"));
		String officePhone = ExStringUtils.trimToEmpty(request.getParameter("officePhone"));
		//String officeAddress = request.getParameter("officeAddress");
		//String officeZipcode = request.getParameter("officeZipcode");
		String title = ExStringUtils.trimToEmpty(request.getParameter("title"));
		String specialization = ExStringUtils.trimToEmpty(request.getParameter("specialization"));
		String basememo = ExStringUtils.trimToEmpty(request.getParameter("basememo"));
		//增加对性别和身份证号以及出生日期、姓名的修改
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String nameUsed = ExStringUtils.trimToEmpty(request.getParameter("nameUsed"));//曾用名
		String gender = ExStringUtils.trimToEmpty(request.getParameter("gender"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String bornDay = ExStringUtils.trimToEmpty(request.getParameter("bornDay"));
		stuinfo.setName(name);
		stuinfo.setNameUsed(nameUsed);
		stuinfo.setGender("男".equals(gender) ?"1":"2");
		stuinfo.setCertNum(certNum);
		stuinfo.setBornDay(ExDateUtils.convertToDate(bornDay));
		stuinfo.setCertType(certType);
			
		stuinfo.setContactAddress(contactAddress);
		stuinfo.setContactZipcode(contactZipcode);
		stuinfo.setContactPhone(contactPhone);
		stuinfo.setMobile(mobile);
		stuinfo.setEmail(email);
		stuinfo.setHomePage(homePage);
		
		//stuinfo.setHeight(Long.getLong(height));
		if("".equals(request.getParameter("height"))){
			stuinfo.setHeight(null);
		}else{
			stuinfo.setHeight(Long.parseLong(ExStringUtils.trimToEmpty(request.getParameter("height"))));
		}
		stuinfo.setBloodType(bloodType);
		//stuinfo.setBornDay(ExDateUtils.convertToDate(bornDay));
		stuinfo.setBornAddress(bornAddress);
		stuinfo.setCountry(country);
		stuinfo.setHomePlace(homePlace);
		stuinfo.setGaqCode(gaqCode);
		stuinfo.setNation(nation);
		stuinfo.setHealth(health);
		stuinfo.setMarriage(marriage);
		stuinfo.setPolitics(politics);
		stuinfo.setFaith(faith);
		stuinfo.setResidenceKind(residenceKind);
		stuinfo.setResidence(residence);
		stuinfo.setCurrenAddress(currenAddress);
		stuinfo.setHomeAddress(homeAddress);
		stuinfo.setHomezipCode(homezipCode);
		stuinfo.setHomePhone(homePhone);
		stuinfo.setOfficeName(officeName);
		stuinfo.setOfficePhone(officePhone);
		//stuinfo.setOfficeAddress(officeAddress);
		//stuinfo.setOfficeZipcode(officeZipcode);
		stuinfo.setTitle(title);
		stuinfo.setSpecialization(specialization);
		stuinfo.setMemo(basememo);
		

		stuinfo.setPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("photoPath")));//照片
		stuinfo.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPath")));//身份证复印件
		stuinfo.setCertPhotoPathReverse(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPathReverse")));//身份证复印件
		stuinfo.setEduPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("eduPhotoPath")));//毕业证复印件
		
		//学籍信息
		//增加对入学资格审核的修改
		String enterAuditStatus = ExStringUtils.trimToEmpty(request.getParameter("enterAuditStatus"));
		String graduateSchool = ExStringUtils.trimToEmpty(request.getParameter("gSchoolName"));;
		String graduateSchoolCode = ExStringUtils.trimToEmpty(request.getParameter("gSchoolCode"));;
		String graduateMajor = ExStringUtils.trimToEmpty(request.getParameter("gMajor"));;
		String graduateId = ExStringUtils.trimToEmpty(request.getParameter("gId"));;
		try {
			if(null!=request.getParameter("gDate")&&!"".equals(request.getParameter("gDate"))){
				String graduateDateStr = request.getParameter("gDate");
				Date graduateDate = ExDateUtils.parseDate(graduateDateStr,ExDateUtils.PATTREN_DATE);
				stu.setGraduateDate(graduateDate);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		stu.setStudentBaseInfo(stuinfo);
		stu.setEnterAuditStatus(enterAuditStatus);
		stu.setGraduateSchool(graduateSchool);
		stu.setGraduateSchoolCode(graduateSchoolCode);
		stu.setGraduateMajor(graduateMajor);
		stu.setGraduateId(graduateId);
		stu.setStudentName(name);		
	}
	
	/**
	 * 学生选择对话框
	 * @param objPage
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/framework/system/selector/student.html")
	public String nodePicker(Page objPage,HttpServletRequest request,HttpServletResponse response) throws Exception{
		
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String classic=request.getParameter("classic");//层次
		String stuStatus=request.getParameter("stuStatus");//学籍状态
		String name=request.getParameter("name");//姓名
		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		request.setAttribute("leftList", orgUnitService.findOrgUnitListByType(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue()));
		request.setAttribute("stulist", page);
		request.setAttribute("condition", condition);
		return "/edu3/roll/sturegister/selector_students";
	}
	
	/**
	 * 设置学生账号 停用/启用
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/enableaccount.html")
	public void studentAccount(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			List<String> idList = new ArrayList<String>();
			if(ExStringUtils.isNotBlank(resourceid)){
				//参数值：disenable停用，  enable 禁用
				String type = ExStringUtils.defaultIfEmpty(request.getParameter("type"), "");	
				
				String[] arr = resourceid.split("\\,");
				for (int i = 0; i < arr.length; i++) {//遍历学生ID
					idList.add(arr[i]);
				}
				if("enable".equals(type)){
					studentInfoService.enableStudentAccount(idList, true);
				}else if("disenable".equals(type)){
					studentInfoService.enableStudentAccount(idList, false);
				}				
			}
			map.put("statusCode", 200);
			map.put("message", "操作成功！");				
			map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/schoolroll-list.html");
		} catch (Exception e) {
			logger.error("设置学生账号失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败<br/>"+e.getLocalizedMessage());				
		
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"设置学生账号状态："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 学生通讯录
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/student/info/addressbook.html")
	public String listStudentInfo(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));
		String orderType = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("orderType")),Page.ASC);
		if(ExStringUtils.isNotEmpty(orderBy)){
			objPage.setOrderBy(orderBy);
		} else {
			objPage.setOrderBy("branchSchool,studentStatus,grade,classic,studyNo");			
		}	
		objPage.setOrder(orderType);
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = request.getParameter("branchSchool");//学习中心
		String major = request.getParameter("major");//专业
		String classic = request.getParameter("classic");//层次
		String stuStatus = request.getParameter("stuStatus");//学籍状态
		String name = request.getParameter("name");//姓名
		String matriculateNoticeNo = request.getParameter("matriculateNoticeNo");//学号
		String gradeid = request.getParameter("gradeid");//年级
		String gender = request.getParameter("gender");//性别
		String schoolType = request.getParameter("schoolType");//办学模式
		
		User user = SpringSecurityHelper.getCurrentUser();		
		String unittype = CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue();
		if (unittype.equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("brSchool", unittype);	
		}
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(gender)) {
			condition.put("gender", gender);
		}
		if(ExStringUtils.isNotEmpty(schoolType)) {
			condition.put("schoolType", schoolType);
		}
		
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		model.addAttribute("condition", condition);
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));//高级查询页面
		if ("advance".equalsIgnoreCase(advanseCon)) {
			return "/edu3/roll/stuaddressbook/stuaddressbook-search";// 返回到高级检索
		}
		
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		model.addAttribute("stulist", page);	
		return "/edu3/roll/stuaddressbook/stuaddressbook-list";
	}
	/**
	 * 重置学生账户密码为原始密码
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/resetpassword.html")
	public void resetStudentAccountPassword(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				String initPassword = CacheAppManager.getSysConfigurationByCode("sysuser.initpwd").getParamValue();
				studentInfoService.resetStudentAccountPassword(resourceid.split("\\,"), BaseSecurityCodeUtils.getMD5(initPassword));			
			}
			map.put("statusCode", 200);
			map.put("message", "成功重置密码！");				
			map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/schoolroll-list.html");
		} catch (Exception e) {
			logger.error("重置学生账号密码失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败<br/>"+e.getLocalizedMessage());				
		
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 得到组合的学籍状态
	 * @param model
	 * @return
	 */
	private ModelMap getNewStudentStatus(ModelMap model){
		List<Dictionary> children = CacheAppManager.getChildren("CodeStudentStatus");
		StringBuffer studentStatusRes = new StringBuffer();
		for (Dictionary childDict : children) {
			String code = childDict.getDictName();
			String value = childDict.getDictValue();
			if("11".equals(value)){
				studentStatusRes.append("a"+value+",正常注册,");
				studentStatusRes.append("b"+value+",正常未注册,");
			}else{
				studentStatusRes.append(value+"," +code+",");
			}
		}
		model.addAttribute("stuStatusSet",studentStatusRes.toString());
		return model;
	}
	//**v3功能移
	/**
	 * 选择批量设置入学资格的状态
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	 @RequestMapping({"/edu3/register/studentinfo/setRecruitStatus.html","/edu3/register/studentinfo/setStudentStatus.html"})
	 public String setRecruitStatus(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
			
		model.addAttribute("stus", ExStringUtils.trimToEmpty(request.getParameter("stus")));
		model.addAttribute("grade",ExStringUtils.trimToEmpty(request.getParameter("grade")));
		model.addAttribute("major",ExStringUtils.trimToEmpty(request.getParameter("major")));
		model.addAttribute("totalCounts", ExStringUtils.trimToEmpty(request.getParameter("totalCounts")));
		model.addAttribute("classic",ExStringUtils.trimToEmpty(request.getParameter("classic")));
		model.addAttribute("stuStatus",ExStringUtils.trimToEmpty(request.getParameter("stuStatus")));
		model.addAttribute("accountStatus",ExStringUtils.trimToEmpty(request.getParameter("accountStatus")));
		model.addAttribute("entranceFlag",ExStringUtils.trimToEmpty(request.getParameter("entranceFlag")));
		model.addAttribute("branchSchool",ExStringUtils.trimToEmpty(request.getParameter("branchSchool")));
		model.addAttribute("flag", ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.addAttribute("pageNum", ExStringUtils.trimToEmpty(request.getParameter("flag")));

		return "/edu3/roll/sturegister/batchSetRecruitStatus";
	}
	 

	/**
	 * 退回学生学籍卡
	 */
	@RequestMapping("/edu3/register/studentinfo/studentcardinfoback.html")
	public void studentCardInfoBack(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String ,Object> map1 = new HashMap<String, Object>();
		Integer status = 300;
		String mes = "退回学籍卡信息失败！";
		try{
			String[] studyid = ExStringUtils.trimToEmpty(request.getParameter("resourceid")).split(",");
			String stuids = ExStringUtils.addSymbol(studyid, "'", "'");
			if(studyid.length==0){
				status = 300;
				mes = "参数错误！";
			}else{
				map1 = studentInfoService.backStudenInfoCard(stuids);
				if(map1 != null){
					status =  (Integer) map1.get("isSuccess");
					mes = (String) map1.get("mes");
				}
			}
		}catch(Exception e){
			status = 300;
			mes = "退回学籍卡信息操作异常！";
			e.printStackTrace();			
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"退回学生学籍卡："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		map.put("statusCode", status);
		map.put("message", mes);
		renderJson(response, JsonUtils.mapToJson(map));
	}

	 /**
	 * 导出班级花名册XLS 自定义导出
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/exportClassesRosterDiy.html")
	public String doExcelExportClassesRosterDiy(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String branchSchool= request.getParameter("branchSchool");
		String grade    = request.getParameter("grade");
		String classes     = request.getParameter("classes");
		String classic  = request.getParameter("classic");
		String major    = request.getParameter("major");
		
		
		String branchSchool_txt = ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("branchSchool_txt"));
		String classic_txt = ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("classic_txt"));
		String major_txt   = ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("major_txt"));
		String grade_txt   = ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("grade_txt"));
		String classes_txt = ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("classes_txt"));
		

		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool = user.getOrgUnit().getResourceid();		
			branchSchool_txt = user.getOrgUnit().getUnitName();
		}
		
		map.addAttribute("flag",flag);
		map.addAttribute("branchSchool",branchSchool);
		map.addAttribute("grade",grade);
		map.addAttribute("classic",classic);
		map.addAttribute("major",major);
		map.addAttribute("classes",classes);
		
		map.addAttribute("branchSchool_txt",branchSchool_txt);
		map.addAttribute("classic_txt",classic_txt);
		map.addAttribute("major_txt",major_txt);
		map.addAttribute("grade_txt",grade_txt);
		map.addAttribute("classes_txt",classes_txt);
		
		return "edu3/roll/sturegister/exportClassesRosterDIY";
	}
		
		
	 /**
	 * 导出班级花名册XLS
	 * @param request
	 * @param response

	 * @throws WebException
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	@RequestMapping("/edu3/register/studentinfo/exportClassesRoster.html")
	public void doXlsExportClassesRoster(StudentInfoVo studentInfoVo,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> condition = new HashMap<String, Object>();

		String flag         = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String classic 		= ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String major 		= ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String grade     = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级		
		String classes    = ExStringUtils.trimToEmpty(request.getParameter("classes")); //班级
		
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		StringBuffer excelTitle       = new StringBuffer(schoolName+"成人教育学院");
		StringBuffer excelName        = new StringBuffer(excelTitle);
		
		User user 		    = SpringSecurityHelper.getCurrentUser();				
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool 	 = user.getOrgUnit().getResourceid();
			excelTitle.append("（"+ExStringUtils.trimToEmpty(user.getOrgUnit().getUnitName())+"）");
		}
		if("master".equals(flag)){
			condition.put("classesMasterId", user.getResourceid());
		}
		
		StringBuffer excelSecondTitle = new StringBuffer();
		
		
		List<Map<String,Object>> list_original = new ArrayList<Map<String,Object>>();
		if(ExStringUtils.isNotEmpty(classic)&&ExStringUtils.isNotEmpty(major)&&ExStringUtils.isNotEmpty(branchSchool)
				&&ExStringUtils.isNotEmpty(grade)&&ExStringUtils.isNotEmpty(classes)){
			condition.put("branchSchool", branchSchool);
			condition.put("classic", classic);
			condition.put("major", major);
			condition.put("stuGrade", grade);
			condition.put("classesid", classes);
			condition.put("excludeStuStatus","16");
			Classes clsObj = classesService.get(classes);
			Classic clcObj = classicService.get(classic);
			Major  mjObj   = majorService.get(major);
			Grade gdObj    = gradeService.get(grade);
			excelSecondTitle.append("【"+gdObj.getGradeName()+"】【"+clcObj.getClassicName()+"】【"+mjObj.getMajorName()+"】【"+clsObj.getClassname()+"】________表");
			excelName.append("("+clsObj.getClassname()+")");
			list_original = studentInfoChangeJDBCService.getStudentInfoToExport(condition);
		}
		
		excelName.append("花名册");
		
		GUIDUtils.init();
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			
			List<String> filterColumnList = new ArrayList<String>();
			
			//学籍以及学籍基本信息的字段
			Field[] properties = StudentInfoVo.class.getDeclaredFields(); 
			for (Field field : properties) {
				String propertiesName = field.getName(); 
				if("serialVersionUID".equals(propertiesName)){
					continue;
				}
				String methodName = "get"+propertiesName.subSequence(0, 1).toString().toUpperCase()+propertiesName.substring(1, propertiesName.length());;
//				System.out.println(propertiesName+":"+methodName);
				Method method = StudentInfoVo.class.getDeclaredMethod(methodName);
//				System.out.println(methodName+"::"+method.invoke(studentInfoVo,null));
				if(null!=method.invoke(studentInfoVo, null) ){
					filterColumnList.add(propertiesName);
				}
			}
			filterColumnList.add("message");
			filterColumnList.add("sortNum");
			
			List<StudentInfoVo> list = new ArrayList<StudentInfoVo>();
			int sortNum = 1;
			for (Map<String,Object> si : list_original) {
				StudentInfoVo siv = new StudentInfoVo();
				siv.setSortNum(String.valueOf(sortNum++));
				siv.setName(null!=si.get("studentname")?si.get("studentname").toString():"-");
				siv.setStudentNo(null!=si.get("studyno")?si.get("studyno").toString():"-");
//				siv.setGender(null!=si.get("gender")?si.get("gender").toString():"-");
//				siv.setMobile(null==si.get("phone")?"-":"\\".equals(si.get("phone").toString())?"-":si.get("phone").toString());//联系电话   （手机电话\固定电话）
				siv.setMobile(si.get("mobile") != null ? si.get("mobile").toString() : (si.get("contactPhone") != null ? si.get("contactPhone").toString() : "-"));
				siv.setExamNo(null!=si.get("enrolleecode")?si.get("enrolleecode").toString():"-");
				
				siv.setGender(null!=si.get("gender")?si.get("gender").toString():"-");
				siv.setCertNum(null!=si.get("certnum")?si.get("certnum").toString():"-");
				siv.setOfficePhone(null!=si.get("KSH")?si.get("KSH").toString():"-");
				siv.setContactZipcode(null!=si.get("contactzipcode")?si.get("contactzipcode").toString():"-");
				siv.setContactAddress(null!=si.get("contactaddress")?si.get("contactaddress").toString():"-");
				list.add(siv);
			}
			File excelFile  = null;
			File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeNation");
			dictCodeList.add("CodeStudentStatus");
			Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			
			exportExcelService.initParmasByfile(disFile, "classesRosterInfo", list,map,filterColumnList);
			exportExcelService.getModelToExcel().setHeader(excelTitle.toString());//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			
			WritableFont headerFont = new WritableFont(WritableFont.createFont("宋体"),20,WritableFont.NO_BOLD, false,UnderlineStyle.NO_UNDERLINE);
	        WritableCellFormat headerFormat = new WritableCellFormat(headerFont);
	        headerFormat.setAlignment(Alignment.CENTRE);
	        headerFormat.setBackground(Colour.WHITE);
	        headerFormat.setBorder(Border.ALL, BorderLineStyle.NONE);
	        
	        WritableFont secondFont = new WritableFont(WritableFont.createFont("宋体"),14,WritableFont.NO_BOLD, false,UnderlineStyle.NO_UNDERLINE);
	        WritableCellFormat secondFormat = new WritableCellFormat(secondFont);
	        secondFormat.setAlignment(Alignment.CENTRE);
	        secondFormat.setBackground(Colour.WHITE);
	        secondFormat.setBorder(Border.ALL, BorderLineStyle.NONE);
	        
	        WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			font.setBoldStyle(WritableFont.BOLD);
	        WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
	        format.setBackground(Colour.GRAY_25);
	        format.setBorder(Border.ALL, BorderLineStyle.THIN);
			exportExcelService.getModelToExcel().setTitleCellFormat(format);
			exportExcelService.getModelToExcel().setHeaderCellFormat(headerFormat);
			exportExcelService.getModelToExcel().setRowHeight(300);
			//设置二级标签
			exportExcelService.getModelToExcel().setSecondHeader(excelSecondTitle.toString());
			exportExcelService.getModelToExcel().setIntSecondHeaderHeight(240);
			exportExcelService.getModelToExcel().setSecondHeaderFormat(secondFormat);
			
			excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
			
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response,excelName+".xls",excelFile.getAbsolutePath(),true);
			
		}catch (Exception e) {
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		}
	}
	
	 
	 /**
	 * 导出分班信息XLS
	 * @param request
	 * @param response

	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/exportXlsDivideClass.html")
	public void doXlsExportDivideClass(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();

		String flag         = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String classic 		= ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String major 		= ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String teachingType 		= ExStringUtils.trimToEmpty(request.getParameter("teachingType"));//专业
		String stuStatus    = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String includeStuStatus    = ExStringUtils.trimToEmpty(request.getParameter("includeStuStatus"));//学籍状态	
		String accountStatus = stuStatus;
		
		String stuGrade     = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));//年级		
		String name 		= ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String rollCard     = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		String certNum      = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//身份证号
		
		String matriculateNoticeNo      = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号 
		String classesid    = ExStringUtils.trimToEmpty(request.getParameter("classesid")); //班级
		String classesids    = ExStringUtils.trimToEmpty(request.getParameter("classesids")); //班级
		String entranceFlag = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格
		String addSql		= ExStringUtils.trimToEmpty(request.getParameter("addSql"));
		String studentIds           = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));//学籍资源ID
		User user 		    = SpringSecurityHelper.getCurrentUser();				
		String branchSchoolName = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool 	 = user.getOrgUnit().getResourceid();
		}
		
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		
		String unitStr = "";
		String classicStr = "";
		String majorStr = "";
		String gradeStr = "";
		String stuStatusStr = "";
		String teachPlanStr = "";
		String entranceFlagStr = "";
		String rollCardStr = "";
		String accountStr = "";
		String schoolTypeStr = "";
		String hasGraduateCardStr = "";
		StringBuffer titleStrbuf = new StringBuffer();
		if (ExStringUtils.isNotBlank(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			branchSchoolName = orgUnitService.get(branchSchool).getUnitShortName();
			unitStr = "学习中心:"+ branchSchoolName;
		}
		if (ExStringUtils.isNotBlank(classic)) {
			condition.put("classic", classic);
			classicStr = "层次:" + classicService.get(classic).getShortName();
		}
		if (ExStringUtils.isNotBlank(major)) {
			condition.put("major", major);
			majorStr = "专业:" + majorService.get(major).getMajorName();
		}
		if (ExStringUtils.isNotBlank(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if (ExStringUtils.isNotBlank(includeStuStatus)) {
			condition.put("includeStuStatus", includeStuStatus);
		}
		if (ExStringUtils.isNotBlank(stuStatus)) {
			condition.put("stuStatus", stuStatus);
			stuStatusStr = "学籍状态:"+ JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", stuStatus);
		}
		
		if (ExStringUtils.isNotBlank(stuGrade)) {
			condition.put("stuGrade", stuGrade);
			gradeStr = "年级:" + gradeService.get(stuGrade).getGradeName();
		}
		if (ExStringUtils.isNotBlank(name)){
			name = ExStringUtils.getEncodeURIComponentByTwice(name);
			condition.put("name", name);
		}
		if (ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("studyNo", matriculateNoticeNo);
		}
		if (ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if (ExStringUtils.isNotEmpty(rollCard)) {
			condition.put("rollCard", rollCard);
			rollCardStr = "学籍卡提交:"+ JstlCustomFunction.dictionaryCode2Value("CodeRollCardStatus",rollCard);
		}
		if(ExStringUtils.isNotEmpty(classesid)){
			condition.put("classesid", classesid);
		}
		if(ExStringUtils.isNotEmpty(classesids)){
			condition.put("classesids", classesids);
		}
		if(ExStringUtils.isNotEmpty(addSql)){
			condition.put("addSql", addSql);
		}
		if (ExStringUtils.isNotEmpty(entranceFlag)) {
			condition.put("entranceFlag", entranceFlag);
			entranceFlagStr = "入学资格审核:"+ JstlCustomFunction.dictionaryCode2Value("CodeAuditStatus", entranceFlag);
		}
		
		if(ExStringUtils.isNotEmpty(studentIds)){
			condition.put("studentIds","'" + studentIds.replaceAll(",", "','") + "'");
		}
		if(ExStringUtils.isNotEmpty(flag)&&"W".equals(flag)){
			condition.put("notClasses", flag);
		}
		
		if(ExStringUtils.isNotEmpty(unitStr)){
			titleStrbuf.append(unitStr+" ");
		}
		if(ExStringUtils.isNotEmpty(classicStr)){
			titleStrbuf.append(classicStr+" ");
		}
		if(ExStringUtils.isNotEmpty(majorStr)){
			titleStrbuf.append(majorStr+" ");
		}
		if(ExStringUtils.isNotEmpty(gradeStr)){
			titleStrbuf.append(gradeStr+" ");
		}
		if(ExStringUtils.isNotEmpty(stuStatusStr)){
			titleStrbuf.append(stuStatusStr+" ");
		}
		if(ExStringUtils.isNotEmpty(teachPlanStr)){
			titleStrbuf.append(teachPlanStr+" ");
		}
		if(ExStringUtils.isNotEmpty(entranceFlagStr)){
			titleStrbuf.append(entranceFlagStr+" ");
		}
		if(ExStringUtils.isNotEmpty(rollCardStr)){
			titleStrbuf.append(rollCardStr+" ");
		}
		if(ExStringUtils.isNotEmpty(accountStr)){
			titleStrbuf.append(accountStr+" ");
		}
		if(ExStringUtils.isNotEmpty(schoolTypeStr)){
			titleStrbuf.append(schoolTypeStr+" ");
		} 
		//2014-3-21
		if(ExStringUtils.isNotEmpty(hasGraduateCardStr)){
			titleStrbuf.append(hasGraduateCardStr+" ");
		}
		
		titleStrbuf.append("分班信息表");
		//自定义选择导出的数据列
		String[] excelColumnNames 	  = {"name","examNo","studentNo","gender","certNum","bornDay","nation","grade",
				"classic","major","unit","contactAddress","mobile","classes"};//request.getParameterValues("excelColumnName");
		List<String> filterColumnList = new ArrayList<String>();//定义过滤列
		if(null !=excelColumnNames && excelColumnNames.length>0){
			for(int i=0;i<excelColumnNames.length;i++){
				filterColumnList.add(excelColumnNames[i]);
			}
		}
		condition.put("excludeStuStatus","16");
		GUIDUtils.init();
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			List<Map<String,Object>> list_original = studentInfoChangeJDBCService.getStudentInfoToExport(condition);
			//为方便查看定位，这里都添加输出
			logger.info("导出学籍信息XLS:/edu3/register/studentinfo/exportXls.html,username:"+user.getUsername(),user.getUsername());
			List<StudentInfoVo> list = new ArrayList<StudentInfoVo>();
			
			for (Map<String,Object> si : list_original) {
				StudentInfoVo siv = new StudentInfoVo();				
				siv.setName(null!=si.get("studentname")?si.get("studentname").toString():"-");
				siv.setExamNo(null!=si.get("examNo")?si.get("examNo").toString():"-");
				siv.setStudentNo(null!=si.get("studyno")?si.get("studyno").toString():"-");
				siv.setGender(null!=si.get("gender")?JstlCustomFunction.dictionaryCode2Value("CodeSex",si.get("gender").toString()):"-");
				siv.setCertNum(null!=si.get("certnum")?si.get("certnum").toString():"-");
				siv.setBornDay(null!=si.get("bornday")?si.get("bornday").toString().replaceAll("-", ""):"-");
				siv.setNation(null!=si.get("nation")?JstlCustomFunction.dictionaryCode2Value("CodeNation",si.get("nation").toString()):"-");
				siv.setGrade(null!=si.get("gradename")?si.get("gradename").toString():"-");
				siv.setStudyStatus(null!=si.get("studentstatus")?JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus",si.get("studentstatus").toString()):"-");
				
				siv.setClassic(null!=si.get("classicname")?si.get("classicname").toString():"-");
				siv.setMajor(null!=si.get("majorname")?si.get("majorname").toString():"-");
				siv.setUnit(null!=si.get("unitname")?si.get("unitname").toString():"-");
				siv.setContactAddress(null!=si.get("contactAddress")?si.get("contactAddress").toString():"-");
				siv.setMobile(null==si.get("contactPhone")?"-":"\\".equals(si.get("contactPhone").toString().trim())?"-":si.get("contactPhone").toString());//联系电话   （手机电话\固定电话）
				siv.setClasses(null!=si.get("classes")?si.get("classes").toString():"");//班级
				
				list.add(siv);
			}
			
			File excelFile  = null;
			File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			/*List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeNation");
			dictCodeList.add("CodeStudentStatus");
			Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			*/
//			CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
			
			exportExcelService.initParmasByfile(disFile, "studentInfoDivideClass", list,null,filterColumnList);
			exportExcelService.getModelToExcel().setHeader(titleStrbuf.toString());//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
			font.setBoldStyle(WritableFont.BOLD);
	        WritableCellFormat format = new WritableCellFormat(font);
			format.setAlignment(Alignment.CENTRE);
	        format.setBackground(Colour.YELLOW);
	        format.setBorder(Border.ALL, BorderLineStyle.THIN);
			exportExcelService.getModelToExcel().setTitleCellFormat(format);
			exportExcelService.getModelToExcel().setRowHeight(300);
			excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
			String downloadFileName = "分班信息表" + (ExStringUtils.isEmpty(branchSchoolName)?"":"("+branchSchoolName+")")+".xls";
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response,downloadFileName,excelFile.getAbsolutePath(),true);
			
		}catch (Exception e) {
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		}
		
	}
	

	/**
	 * 分班Excel导入-路径输入
	 * 
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/impDivideXlsView.html")
	public String importStudentFactFeeInfo(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		return "/edu3/roll/sturegister/divideclasses-upload";
	}
	
	/**
	 * 导入分班信息XLS
	 * @param request
	 * @param response

	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/doXlsImpDivideClass.html")
	public void doXlsImpDivideClass(HttpServletRequest request,HttpServletResponse response) throws WebException{

		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		//提示信息字符串
		String  rendResponseStr = "";
		
		List<StudentInfoVo> falseList = new ArrayList<StudentInfoVo>();
		
		int sucssCount = 0;//成功操作数
		int count = 0;//总数
		int wCount = 0;//未操作总数
		
		File excelFile = null;
		String upLoadurl = "/edu3/register/studentinfo/doXlsInpDivideClass-falseUpload.html";
		User user 		    = SpringSecurityHelper.getCurrentUser();			
		String branchSchool = "";
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool 	= user.getOrgUnit().getResourceid();					
		}
		
		//获取excel的缴费记录，并插入到数据库中
		if ("import".equals(exportAct)) {
			//设置目标文件路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
			try {
				
				//上传文件到服务器
				List<AttachInfo> list = doUploadFile(request, response, null);
				AttachInfo attachInfo = list.get(0);
				//创建EXCEL对象 获得待导入的excel的内容
				File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
				
				importExcelService.initParmas(excel, "studentInfoDivideClass",null);
				importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
				//获得待导入excel内容的List
				List<StudentInfoVo> stiVos = importExcelService.getModelList();
				if(stiVos==null){
					throw new Exception("导入分班模版错误！");
				}
				//转换为对应类型的List
				List<String> studentNos = new ArrayList<String>();
				List<String> classNames = new ArrayList<String>();
				//List<StudentInfoVo> stiVos = new ArrayList<StudentInfoVo>();
				List<Classes> classesList = new ArrayList<Classes>();
				List<StudentInfo> studentInfoList = new ArrayList<StudentInfo>();//学籍信息
				for (int i = 0; i < stiVos.size(); i++) {
					StudentInfoVo impPayment = stiVos.get(i);
					//stiVos.add(impPayment);
					if(studentNos.size()>0 && studentNos.size()%999==0){
						studentNos.add(impPayment.getStudentNo()+"') or si.studyNo in ('");
					}else {
						studentNos.add(impPayment.getStudentNo());
					}
					if(!classNames.contains(impPayment.getClasses())){
						if(classNames.size()>0 && classNames.size()%50==0){
							classNames.add(impPayment.getClasses()+"') or cl.classname in('");
						}else {
							classNames.add(impPayment.getClasses());
						}
					}
				}
				//根据学号排序
				Collections.sort(stiVos, new Comparator<StudentInfoVo>() {
					@Override
					public int compare(StudentInfoVo o1, StudentInfoVo o2) {
						// TODO Auto-generated method stub
						return o1.getStudentNo().compareTo(o2.getStudentNo());
					}
				});
				//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
				if (null!=stiVos) {
					count = stiVos.size();
					GUIDUtils.init();
					StringBuffer hql = new StringBuffer("from "+StudentInfo.class.getSimpleName()+" si where si.studyNo in ("+ExStringUtils.addSymbol(studentNos,"'","'")+") and si.isDeleted=0 order by si.studyNo");
					studentInfoList = studentInfoService.findByHql(hql.toString(), new Object[]{});
					hql.setLength(0);
					hql.append("from "+Classes.class.getSimpleName()+" cl where cl.isDeleted=0 and cl.classname in("+ExStringUtils.addSymbol(classNames,"'","'") +")");
					classesList = classesService.findByHql(hql.toString(), new Object[]{});
					List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
					for(StudentInfoVo stiVo : stiVos){
						boolean isFind = false;
						String message = "未找到此学号的学籍信息！";
						for(StudentInfo stu : studentInfoList){
							if(stu.getStudyNo().equals(stiVo.getStudentNo())){//判断学号是否存在
								if(ExStringUtils.isNotEmpty(branchSchool)&&null!=stu.getBranchSchool()&&!branchSchool.equals(stu.getBranchSchool().getResourceid())){
									message = "该学生不是本教学站点的学生！";
								}else {
									//判断班级是否存在
									if(ExStringUtils.isNotBlank(stiVo.getClasses())){
										for (Classes classes : classesList) {
											if(ExBeanUtils.isNotNullOfAll(classes.getBrSchool(),classes.getGrade(),classes.getClassic(),classes.getMajor())
													&& stu.getBranchSchool().getResourceid().equals(classes.getBrSchool().getResourceid()) && stu.getGrade().getResourceid().equals(classes.getGrade().getResourceid())
													&& stu.getClassic().getResourceid().equals(classes.getClassic().getResourceid()) && stu.getMajor().getResourceid().equals(classes.getMajor().getResourceid())
													&& stu.getTeachingType().equals(classes.getTeachingType()) && stiVo.getClasses().equals(classes.getClassname())){
												isFind = true;
												stu.setClasses(classes);
												studentInfos.add(stu);
												break;
											}
										}
										if(!isFind){
											message = "找不到符合条件的此班级！";
										}else {
											sucssCount++;
										}
									}else if(null!=stu.getClasses()){
										stu.setClasses(null);
										studentInfos.add(stu);
									}else {
										wCount++;
									}
								}
								break;
							}
						}
						if(!isFind){
							stiVo.setMessage(message);
							falseList.add(stiVo);
						}
					}
					studentInfoService.batchSaveOrUpdate(studentInfos);
				}
				
				//导出缴费信息导入失败的信息以及原因
				if(falseList!=null&&falseList.size()>0){
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					//exportExcelService.getModelToExcel().setRowHeight(400);
					String fileName = GUIDUtils.buildMd5GUID(false);
					File disFile 	= new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
//					CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
					
					exportExcelService.initParmasByfile(disFile, "studentInfoDivideClassError", falseList ,null);
					exportExcelService.getModelToExcel().setHeader("分班信息表");//设置大标题
					exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
					WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
					font.setBoldStyle(WritableFont.BOLD);
			        WritableCellFormat format = new WritableCellFormat(font);
					format.setAlignment(Alignment.CENTRE);
			        format.setBackground(Colour.YELLOW);
			        format.setBorder(Border.ALL, BorderLineStyle.THIN);
					exportExcelService.getModelToExcel().setTitleCellFormat(format);
					exportExcelService.getModelToExcel().setRowHeight(300);
					excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
					
					logger.info("获取导出分班失败记录的excel文件:{}",excelFile.getAbsoluteFile());
					
//					downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
					rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ sucssCount 
					+"条 | 导入失败"+ (count - sucssCount - wCount)
					+"条 | 导入未操作"+ wCount +"条！',forwardUrl:'"+upLoadurl+"?excelFile="+ fileName +"'};";
				}
				if(ExStringUtils.isBlank(rendResponseStr)){
					rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ sucssCount 
					+"条 | 导入失败"+ (count - sucssCount - wCount)
					+"条 | 导入未操作"+ wCount +"条！',forwardUrl:''};";
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				rendResponseStr = "{statusCode:300,message:'操作失败!"+e.getMessage()+"'};";
			}
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"导入分班："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	/**
	 * 导出导入分班功能失败信息
	 * @param excelFile
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/doXlsInpDivideClass-falseUpload.html")
	public void uploadFalseToImport(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导出分班失败信息列表.xls", disFile.getAbsolutePath(),true);
	}
		
	 /**
	  * 导出学籍信息XLS
	  * @param request
	  * @param response
	  * @throws WebException
	  */
	 @RequestMapping("/edu3/register/studentinfo/exportXls.html")
	 public void doXlsExort(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();

		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String classic 		= ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String major 		= ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String stuGrade     = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));//年级		
		String stuStatus    = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String endDate		= ExStringUtils.trimToEmpty(request.getParameter("endDate"));//截止日期
		String name 		= ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String certNum      = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//身份证号
		String studyNo      = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号 
		String rollCard     = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));// 学籍卡状态
		String stuClasses   = ExStringUtils.trimToEmpty(request.getParameter("stuClasses"));//班级
		//2013-04-23补
		String teachPlan    = ExStringUtils.trimToEmpty(request.getParameter("teachPlan")); //是否学习前异动
		String entranceFlag = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格
		String accountStatus= "";
		String exportType   = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出类型
		
		User user 		    = SpringSecurityHelper.getCurrentUser();					
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool 	= user.getOrgUnit().getResourceid();					
		}
					
		if(stuStatus.contains("a")){
			accountStatus	="1";
			stuStatus	    ="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus	="0";
			stuStatus		="11";//去掉学籍中有关帐号状态的标记
		}
		String unitStr = "";
		String classicStr = "";
		String majorStr = "";
		String gradeStr = "";
		String stuStatusStr = "";
		String teachPlanStr = "";
		String entranceFlagStr = "";
		String rollCardStr = "";
		String accountStr = "";
		String dateStr = "";
		StringBuffer titleStrbuf = new StringBuffer();
		
		if("1".equals(exportType)){//勾选导出
			String studentIds = request.getParameter("selectedid");//学生学籍ID
			studentIds  = ExStringUtils.replace(studentIds, ",", "','");
			studentIds  = "'"+studentIds+"'";
			condition.put("studentIds", studentIds);
			}else{
			if(ExStringUtils.isNotBlank(branchSchool)){  condition.put("branchSchool", branchSchool);unitStr = "学习中心:"+orgUnitService.get(branchSchool).getUnitShortName();}
			if(ExStringUtils.isNotBlank(classic)){ 	    condition.put("classic", classic);classicStr = "层次:"+classicService.get(classic).getShortName();}
			if(ExStringUtils.isNotBlank(major)){         condition.put("major", major);majorStr = "专业:"+majorService.get(major).getMajorName();}
			if(ExStringUtils.isNotBlank(stuGrade)){      condition.put("stuGrade", stuGrade);gradeStr = "年级:"+ gradeService.get(stuGrade).getGradeName();	}
			if(ExStringUtils.isNotBlank(stuStatus)){
				condition.put("stuStatus", stuStatus);
				stuStatusStr = "学籍状态:"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", stuStatus);
			}
			if(ExStringUtils.isNotBlank(endDate)){	condition.put("endDate", endDate);dateStr = "（"+endDate+"）";}
			if(ExStringUtils.isNotBlank(name)) 	{
				try {
					name = java.net.URLDecoder.decode(name , "UTF-8");
					condition.put("name", name);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(ExStringUtils.isNotBlank(accountStatus)){ condition.put("accountStatus", accountStatus); if("1".equals(accountStatus)){accountStr="帐号状态:"+"激活";}else if("0".equals(accountStatus)){accountStr="帐号状态:"+"停用";} }
			if(ExStringUtils.isNotEmpty(studyNo)) {
				condition.put("studyNo",studyNo);
			}
			if(ExStringUtils.isNotEmpty(certNum)) {
				condition.put("certNum", certNum);
			}
			if(ExStringUtils.isNotEmpty(stuClasses)) {
				condition.put("stuClasses", stuClasses);
			}
	//		if(ExStringUtils.isNotEmpty(rollCard)){      condition.put("rollCard",rollCard);  rollCardStr="学籍卡提交:"+JstlCustomFunction.dictionaryCode2Value("yesOrNo", rollCard);}
			if(ExStringUtils.isNotEmpty(rollCard)){      condition.put("rollCard",rollCard);  rollCardStr="学籍卡提交:"+JstlCustomFunction.dictionaryCode2Value("CodeRollCardStatus", rollCard);}
			if(ExStringUtils.isNotEmpty(teachPlan)){     condition.put("teachPlan", teachPlan);if("N".equals(teachPlan)){teachPlanStr="可用学习前异动";}else if("Y".equals(teachPlan)){teachPlanStr="不可用学习前异动";}}
			if(ExStringUtils.isNotEmpty(entranceFlag)){  condition.put("entranceFlag", entranceFlag);entranceFlagStr="入学资格审核:"+JstlCustomFunction.dictionaryCode2Value("CodeAuditStatus", entranceFlag); }
			if(ExStringUtils.isNotEmpty(unitStr)){
				titleStrbuf.append(unitStr+" ");
			}
		}
		if(ExStringUtils.isNotEmpty(classicStr)){
				titleStrbuf.append(classicStr+" ");
			}
			if(ExStringUtils.isNotEmpty(majorStr)){
				titleStrbuf.append(majorStr+" ");
			}
			if(ExStringUtils.isNotEmpty(gradeStr)){
				titleStrbuf.append(gradeStr+" ");
			}
			if(ExStringUtils.isNotEmpty(stuStatusStr)){
				titleStrbuf.append(stuStatusStr+" ");
			}
			if(ExStringUtils.isNotEmpty(teachPlanStr)){
				titleStrbuf.append(teachPlanStr+" ");
			}
			if(ExStringUtils.isNotEmpty(entranceFlagStr)){
				titleStrbuf.append(entranceFlagStr+" ");
			}
			if(ExStringUtils.isNotEmpty(rollCardStr)){
				titleStrbuf.append(rollCardStr+" ");
			}
			if(ExStringUtils.isNotEmpty(accountStr)){
				titleStrbuf.append(accountStr+" ");
			}
			titleStrbuf.append("学籍信息表");
			if(ExStringUtils.isNotEmpty(dateStr)){
				titleStrbuf.append(dateStr+" ");
			}
			//自定义选择导出的数据列
			String[] excelColumnNames 	  = request.getParameterValues("excelColumnName");
			List<String> filterColumnList = new ArrayList<String>();//定义过滤列
			if(null !=excelColumnNames && excelColumnNames.length>0){
				for(int i=0;i<excelColumnNames.length;i++){
					filterColumnList.add(excelColumnNames[i]);
				}
			}
			//condition.put("excludeStuStatus","16");
			
			GUIDUtils.init();
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				List<Map<String,Object>> list_original = studentInfoChangeJDBCService.getStudentInfoToExport(condition);
				List<StudentInfoVo> list = new ArrayList<StudentInfoVo>(0);
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				for (Map<String,Object> si : list_original) {
					StudentInfoVo siv = new StudentInfoVo();				
					siv.setName(null!=si.get("studentname")?si.get("studentname").toString():"-");
					siv.setStudentNo(null!=si.get("studyno")?si.get("studyno").toString():"-");
					siv.setExamNo(null!=si.get("enrolleecode")?si.get("enrolleecode").toString():"-");
					
					siv.setGender(null!=si.get("gender")?si.get("gender").toString():"-");
					siv.setCertNum(null!=si.get("certnum")?si.get("certnum").toString():"-");
					siv.setNation(null!=si.get("nation")?si.get("nation").toString():"-");
//					siv.setHascard(Integer.valueOf(si.get("num1").toString())>0&&Integer.valueOf(si.get("num2").toString())>0?"是":"否");
//					siv.setHascard("Y".equals(si.get("rollcardstatus").toString())?"是":"否");
					siv.setHascard(null!=si.get("rollcardstatus")?si.get("rollcardstatus").toString():"0");
					
					siv.setGrade(null!=si.get("gradename")?si.get("gradename").toString():"-");
					siv.setClassic(null!=si.get("classicname")?si.get("classicname").toString():"-");
					siv.setMajor(null!=si.get("majorname")?si.get("majorname").toString():"-");
					siv.setClasses(null!=si.get("classes")?si.get("classes").toString():"-");
					siv.setUnit(null!=si.get("unitname")?si.get("unitname").toString():"-");
					siv.setStudyStatus(null!=si.get("studentstatus")?si.get("studentstatus").toString():"-");
					siv.setAccountStatus(null!=si.get("accoutstatus").toString()?si.get("accoutstatus").toString():"-");
					siv.setBornDay(null!=si.get("bornday")?si.get("bornday").toString().replaceAll("-", ""):"-");
					
					siv.setPolitics(null!=si.get("politics")?si.get("politics").toString():"-");
					siv.setMarriage(null!=si.get("marriage")?si.get("marriage").toString():"-");
					siv.setCertType(null!=si.get("certtype")?si.get("certtype").toString():"-");
					siv.setWorkStatus(null!=si.get("workstatus")?si.get("workstatus").toString():"-");
					siv.setEnterSchool(null!=si.get("ENTERSCHOOL")?si.get("ENTERSCHOOL").toString():"-");
					siv.setAttendAdvancedStudies(null!=si.get("ATTENDADVANCEDSTUDIES")?si.get("ATTENDADVANCEDSTUDIES").toString():"-");
					siv.setLearningStyle(null!=si.get("teachingType")?si.get("teachingType").toString():"-");
					siv.setStudyInSchool(null!=si.get("STUDYINSCHOOL")?si.get("STUDYINSCHOOL").toString():"-");
					siv.setStudentKind(null!=si.get("STUDENTKIND")?si.get("STUDENTKIND").toString():"-");
					siv.setLearingstatus(null!=si.get("LEARINGSTATUS")?si.get("LEARINGSTATUS").toString():"-");
					
					siv.setContactAddress(null!=si.get("contactaddress")?si.get("contactaddress").toString():"-");
					siv.setContactZipcode(null!=si.get("contactzipcode")?si.get("contactzipcode").toString():"-");
					siv.setContactPhone(null!=si.get("contactphone")?si.get("contactphone").toString():"-");
					siv.setExamCertificateNo(null!=si.get("EXAMCERTIFICATENO")?si.get("EXAMCERTIFICATENO").toString():"-");
					siv.setTotalPoint(null!=si.get("TOTALPOINT")?Long.parseLong(si.get("TOTALPOINT").toString()):0);
					siv.setMobile(null!=si.get("mobile")?si.get("mobile").toString():"-");
					siv.setEmail(null!=si.get("email")?si.get("email").toString():"-");
					siv.setHomeAddress(null!=si.get("homeaddress")?si.get("homeaddress").toString():"-");
					siv.setHomezipCode(null!=si.get("homezipcode")?si.get("homezipcode").toString():"-");
					siv.setHomePhone(null!=si.get("homephone")?si.get("homephone").toString():"-");
					siv.setOfficeName(null!=si.get("officename")?si.get("officename").toString():"-");
					siv.setOfficeZipCode(null!=si.get("officezipcode")?si.get("officezipcode").toString():"-");
					siv.setOfficePhone(null!=si.get("officephone")?si.get("officephone").toString():"-");
					siv.setIndate(null!=si.get("indate")?(si.get("indate").toString().length() > 10 ? si.get("indate").toString().substring(0,10):si.get("indate").toString() ):"-");
//					if("10601".equals(schoolCode)){
						siv.setEduYear(null!=si.get("eduyear")?si.get("eduyear").toString():"-");
						String preGraduateDate="";//毕业时间的计算方式：年级+学制+年级中默认毕业日期的月和日
						String defaultGD = null!=si.get("defaultGD")?si.get("defaultGD").toString():"";
						if(ExStringUtils.isNotBlank(defaultGD)){
							String monthday = defaultGD.substring(4,10);
							if(null!=si.get("preGraduateYear")){
								String preGraduateYear = si.get("preGraduateYear").toString();
								int month = Integer.valueOf(monthday.substring(1,3));
								if(month>=7){//当月份大于7月份时，年份减1
									preGraduateYear = String.valueOf(Integer.valueOf(preGraduateYear)-1);
								}								
								preGraduateDate=preGraduateYear+monthday;
							}
						}
						
						siv.setPreGraduateDate(preGraduateDate);
//					}
					list.add(siv);
				}
				
				File excelFile  = null;
				File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeNation");
				dictCodeList.add("CodeStudentStatus");
				dictCodeList.add("CodeAccountStatus");
				// add
				dictCodeList.add("CodePolitics_simple");
				dictCodeList.add("CodePolitics");
				dictCodeList.add("CodeWorkStatus");
				dictCodeList.add("CodeAttendAdvancedStudies");
				dictCodeList.add("CodeLearningStyle");
				dictCodeList.add("CodeStudyInSchool");
				dictCodeList.add("CodeStudentKind");
				dictCodeList.add("CodeEnterSchool");
				dictCodeList.add("CodeLearingStatus");
				dictCodeList.add("CodeRollCardStatus");
				
				Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				map.put("CodeAccessStatus_0", "停用");
				map.put("CodeAccessStatus_1", "激活");
				map.put("CodeCertType_idcard", "身份证");
				map.put("CodeCertType_armycard", "军官证");
				map.put("CodeCertType_passports", "护照");
				map.put("CodeCertType_gatcard", "港、澳、台居民证件活");
				exportExcelService.initParmasByfile(disFile, "studentInfoVo", list,map,filterColumnList);
				exportExcelService.getModelToExcel().setHeader(titleStrbuf.toString());//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
				font.setBoldStyle(WritableFont.BOLD);
		        WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
		        format.setBackground(Colour.YELLOW);
		        format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);
				exportExcelService.getModelToExcel().setRowHeight(300);
				excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
				
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response,titleStrbuf+".xls", excelFile.getAbsolutePath(),true);
				
			}catch (Exception e) {
				logger.error("导出excel文件出错："+e.fillInStackTrace());
				renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
			}
			
		} 
	 /**
	  * 导出学籍信息CSV
	  * @param request
	  * @param response
	  * @throws WebException
	  */
	@RequestMapping("/edu3/register/studentinfo/exportExcel.html")
	public void doCsvExport(HttpServletRequest request,HttpServletResponse response) throws WebException{	
		String exportType = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//按勾选查询的标记		
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			if("1".equals(exportType)){//勾选导出
				String studentIds = request.getParameter("selectedid");//学生学籍ID
				studentIds  = ExStringUtils.replace(studentIds, ",", "','");
				studentIds  = "'"+studentIds+"'";
				condition.put("studentIds", studentIds);
			}else{//按条件导出			
				String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
				User user = SpringSecurityHelper.getCurrentUser();					
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
					branchSchool = user.getOrgUnit().getResourceid();					
				}
				if(ExStringUtils.isNotBlank(branchSchool)) {
					condition.put("branchSchool", branchSchool);
				}
				String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
				if(ExStringUtils.isNotBlank(classic)) {
					condition.put("classic", classic);
				}
				String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
				if(ExStringUtils.isNotBlank(major)) {
					condition.put("major", major);
				}
				String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));//年级		
				if(ExStringUtils.isNotBlank(stuGrade)) {
					condition.put("stuGrade", stuGrade);
				}
				String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态	
					
				String accountStatus = "";
				if(stuStatus.contains("a")){
					accountStatus="1";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}else if(stuStatus.contains("b")){
					accountStatus="0";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}
				if(ExStringUtils.isNotBlank(stuStatus)) {
					condition.put("stuStatus", stuStatus);
				}
				String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
				if(ExStringUtils.isNotBlank(name)) {
					condition.put("name", name);
				}
				if(ExStringUtils.isNotBlank(accountStatus)) {
					condition.put("accountStatus", accountStatus);
				}
								
			}
				
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();
			String csvFilePath = getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".csv";
				
			List<StudentInfoVo> list = studentInfoService.exportStudentList(condition);
							 
			//使用CSV方式
			CsvWriter wr  = new CsvWriter(csvFilePath,',',Charset.forName("GBK"));           
		    String[] title = {"姓名","学号","性别","身份证","民族","	年级","培养层次","专业","学习中心","学籍状态","帐号状态"};
		    wr.writeRecord(title);
		    List<String> dictCodeList = new ArrayList<String>();
			//处理数据字典字段
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeNation");
			dictCodeList.add("CodeStudentStatus");
			Map<String, Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE);
			dictMap.put("Status_0", "停用");    
			dictMap.put("Status_1", "激活");
				
			String fileName = "学籍信息表.zip";
		    if(null != list && !list.isEmpty()){
				for(StudentInfoVo studentInfo : list){
					if(ExStringUtils.isNotBlank(studentInfo.getGender())) {
						studentInfo.setGender(null == dictMap.get("CodeSex_"+studentInfo.getGender()) ? "":dictMap.get("CodeSex_"+studentInfo.getGender()).toString());
					}
					if(ExStringUtils.isNotBlank(studentInfo.getNation())) {
						studentInfo.setNation(null == dictMap.get("CodeNation_"+studentInfo.getNation())? "" :dictMap.get("CodeNation_"+studentInfo.getNation()).toString());
					}
					if(ExStringUtils.isNotBlank(studentInfo.getAccountStatus())) {
						studentInfo.setAccountStatus(null == dictMap.get("Status_"+studentInfo.getAccountStatus())? "":dictMap.get("Status_"+studentInfo.getAccountStatus()).toString());
					}
					if(ExStringUtils.isNotBlank(studentInfo.getInSchoolStatus())) {
						studentInfo.setInSchoolStatus(null == dictMap.get("CodeStudentStatus_"+studentInfo.getInSchoolStatus())?"":dictMap.get("CodeStudentStatus_"+studentInfo.getInSchoolStatus()).toString());
					}
					wr.writeRecord(studentInfo.toExportArray());
				}
				StudentInfoVo vo = list.get(0);
				if(condition.containsKey("stuGrade")) {
					fileName = vo.getGrade().concat(fileName);
				}
				if(condition.containsKey("branchSchool")) {
					fileName = vo.getUnit().concat(fileName);
				}
				if(condition.containsKey("major")) {
					fileName = vo.getMajor().concat(fileName);
				}
				if(condition.containsKey("classic")) {
					fileName = vo.getClassic().concat(fileName);
				}
				if(condition.containsKey("stuStatus")) {
					fileName = vo.getInSchoolStatus().concat(fileName);
				}
			}        
		    wr.close();
		        
		    downloadFile(response, fileName, csvFilePath,true,true);
		} catch (Exception e) {
			logger.error("导出学籍信息出错:{}",e.fillInStackTrace());
		}		
			
	}	
		
	/**
	 * 设置入学资格状态、学籍状态
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/batchSetRecruitStatus.html")
	public void batchSetRecruitStatus(HttpServletRequest request,HttpServletResponse response) throws WebException{
			
		Map<String,Object> map = new HashMap<String, Object>(0);
			
		String flag            = ExStringUtils.trim(request.getParameter("flag"));
		String changeToEntrance= ExStringUtils.trimToEmpty(request.getParameter("recruitStatus"));
		String changeToStuStatu= ExStringUtils.trim(request.getParameter("studentInfoStatus"));
			
		String stus 		   = ExStringUtils.trimToEmpty(request.getParameter("stus"));
		String grade 		   = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String major		   = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String totalCounts     = ExStringUtils.trimToEmpty(request.getParameter("totalCounts"));
		String classic		   = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String stuStatus	   = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
		String accountStatus   = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		String entranceFlag	   = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));
		String branchSchool    = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		//String extensionTime   = ExStringUtils.trimToEmpty(request.getParameter("batchSetStatus_extensionTime"));
		String batchSetStatus_memo = ExStringUtils.trimToEmpty(request.getParameter("batchSetStatus_memo"));
		String batchSetStatus_extensionTime = ExStringUtils.trimToEmpty(request.getParameter("batchSetStatus_extensionTime"));
		if (ExStringUtils.isMessyCode(batchSetStatus_memo)) {
			batchSetStatus_memo = ExStringUtils.getEncodeURIComponentByOne(batchSetStatus_memo);
		}
		if(ExStringUtils.isNotEmpty(flag)) {
			map.put("flag",flag);
		}
		if(ExStringUtils.isNotEmpty(stus)) {
			map.put("stuIds",stus);
		}
		if(ExStringUtils.isNotEmpty(totalCounts)) {
			map.put("totalCounts", totalCounts);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			map.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			map.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			map.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			map.put("stuStatus", stuStatus);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			map.put("stuGrade", grade);
		}
		if(ExStringUtils.isNotEmpty(entranceFlag)) {
			map.put("entranceFlag", entranceFlag);
		}
		if(ExStringUtils.isNotEmpty(accountStatus)) {
			map.put("accountStatus", accountStatus);
		}
		if(ExStringUtils.isNotEmpty(batchSetStatus_extensionTime)) {
			map.put("extensionTime", batchSetStatus_extensionTime);
		}
		if(ExStringUtils.isNotEmpty(batchSetStatus_memo)) {
			map.put("batchSetStatus_memo", batchSetStatus_memo);
		}
		if(ExStringUtils.isNotEmpty(batchSetStatus_extensionTime)) {
			map.put("batchSetStatus_extensionTime", batchSetStatus_extensionTime);
		}
		
		if("0".equals(flag)){
			map.put("changeToStatus",changeToEntrance);
		}else if("1".equals(flag)){
			map.put("changeToStatus", changeToStuStatu);
		}
		try{
			if("1".equals(flag)||"0".equals(flag)){
				studentInfoService.batchSetStatus(map);
			}else if("2".equals(flag)){
				//这个恢复有点复杂
			    //首先要获取撤销异动之前的异动记录，恢复最新的前条记录之效果，再将之后的记录逻辑删除
				String studentChangeInfo = ExStringUtils.trimToEmpty(request.getParameter("studentChangeInfo"));
				String settingDate       = ExStringUtils.trimToEmpty(request.getParameter("settingDate"));
				studentInfoService.redoStudentStatus(studentChangeInfo,settingDate,map);
			}
			
			if (!map.containsKey("message")) {
				if("2".equals(flag)){
					map.put("message", "恢复设置学籍状态成功！");
				}else{
					map.put("message", "批量设置成功！<br/>");
					if (map.containsKey("message2")) {
						map.put("message", map.get("message").toString() + map.get("message2").toString());
					}
				}
			}
			map.put("statusCode", 200);					
		} catch (Exception e) {
			map.put("statusCode", 300);
			map.put("message", "操作失败<br/>"+e.getLocalizedMessage());				
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 学籍信息维护历史
	 * @param request
	 * @param respose
	 * @param modelMap
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/getStudentInfoChangeHistory.html")
	public String getStudentInfoChangeHistory(HttpServletRequest request,HttpServletResponse respose,ModelMap modelMap,Page objPage){
		String val 			= ExStringUtils.trimToEmpty(request.getParameter("val"));
		String changeType   = ExStringUtils.trimToEmpty(request.getParameter("changeType"));
		if("statusonly".equals(val)){
			changeType = "学籍状态"; 
		}
		String certnum  	= ExStringUtils.trimToEmpty(request.getParameter("certnum"));
		String studentname  = ExStringUtils.trimToEmpty(request.getParameter("studentname"));
		String studyno    		= ExStringUtils.trimToEmpty(request.getParameter("studyno"));
		//成教增加的四个查询条件
		String studentstatus = ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
		String gradeid       = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String operatedateb  = ExStringUtils.trimToEmpty(request.getParameter("operatedateb"));
		String operatedatee  = ExStringUtils.trimToEmpty(request.getParameter("operatedatee"));
		Integer pageNum = 1;
		Integer pageTotal = 1;
		if(0!=objPage.getPageNum()){
			pageNum = objPage.getPageNum();
		}
		//条件map
		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		//结果集map
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
		if(!"".equals(certnum)){
			map_condition.put("certnum", certnum);
		}
		if(!"".equals(studentname)){
			map_condition.put("studentname", studentname);		
		}
		if(!"".equals(studyno)){
			map_condition.put("studyno", studyno);
		}
		//成教中加的4个条件
		if(ExStringUtils.isNotEmpty(gradeid)){
			map_condition.put("gradeid",gradeid );
		}
		if(ExStringUtils.isNotEmpty(studentstatus)){
			map_condition.put("studentstatus",studentstatus );		
		}
		if(ExStringUtils.isNotEmpty(operatedateb)){
			map_condition.put("operatedateb",operatedateb );
		}
		if(ExStringUtils.isNotEmpty(operatedatee)){
			map_condition.put("operatedatee",operatedatee );
		}
		//执行查询	
		resultList= studentInfoChangeJDBCService.getStudentInfoChangeHistory(map_condition);
		//由List转为分页
		List<Map<String, Object>> tmpList = new ArrayList<Map<String,Object>>(0);
		List<StudentInfoVo> finalList = new ArrayList<StudentInfoVo>(0);
		
		//需求的表格为：学号、注册号、姓名、身份证号、层次、专业名称、学习中心、年级、变更项目、项目原信息、项目变更后内容、[原因说明没有办法获得]、变更日期
		List<Map<String,Object>> convertList = new ArrayList<Map<String,Object>>(0);
		for (Map<String, Object> maptmp0: resultList) {
			  
			String beforeValue = (String)maptmp0.get("修改前的内容");
			String afterValue  = (String)maptmp0.get("修改后的内容"); 
			String[] before =beforeValue.split(",\"");
			String[] after  =afterValue.split(",\"");
			Map<String,String> mapbefore = new HashMap<String, String>(0);
			Map<String,String> mapafter  = new HashMap<String, String>(0);
			StringBuffer beforeResult = new StringBuffer();
			StringBuffer afterResult  = new StringBuffer();
			for (String string : before) {
				string = string.replaceAll("\\{", "");
				string = string.replaceAll("\\}", "");
				string = string.replaceAll("未知", "");
				string = string.replaceAll("null", "\"\"");
				string = string.replaceAll("\"", "");
				if(string.split(":").length==2){
					mapbefore.put(string.split(":")[0], string.split(":")[1]);
				}
				if(string.lastIndexOf(':')==string.length()-1){
					mapbefore.put(string.split(":")[0], "空");
				}
					
			}
			for (String string : after) {
				string = string.replaceAll("\\{", "");
				string = string.replaceAll("\\}", "");
				string = string.replaceAll("未知", "");
				string = string.replaceAll("null", "\"\"");
				string = string.replaceAll("\"", "");
				if(string.split(":").length==2){
					mapafter.put(string.split(":")[0], string.split(":")[1]);
				}
				if(string.lastIndexOf(':')==string.length()-1){
					mapafter.put(string.split(":")[0], "空");
				}
			}
			Set<String> keySet = mapbefore.keySet();
			for (String string : keySet) {
				if(!"".equals(string)&&!mapbefore.get(string).equals(mapafter.get(string))){
					//说明有改变的值
					Map<String,Object> mapUnit = new HashMap<String, Object>(0);
					mapUnit.put("修改学生姓名", maptmp0.get("修改学生姓名") ); 
					mapUnit.put("修改学生学号", maptmp0.get("修改学生学号") ); 
					mapUnit.put("修改学生身份证号", maptmp0.get("修改学生身份证号") ); 
					mapUnit.put("修改实体类型", maptmp0.get("修改实体类型") );
					mapUnit.put("修改人姓名", maptmp0.get("修改人姓名") ); 
					mapUnit.put("所属单位", maptmp0.get("所属单位") ); 
					mapUnit.put("使用ip",  maptmp0.get("使用ip") ); 
					mapUnit.put("修改时间", maptmp0.get("修改时间") );
					mapUnit.put("变更项目",    string ); 
					mapUnit.put("项目原信息",   mapbefore.get(string) );  
					mapUnit.put("项目变更后内容", mapafter.get(string) );
					//成教新增的字段
					mapUnit.put("年级", maptmp0.get("年级") );
					mapUnit.put("专业", maptmp0.get("专业") );
					mapUnit.put("层次", maptmp0.get("层次") );
					mapUnit.put("班级", maptmp0.get("班级") );
					mapUnit.put("办学单位", maptmp0.get("办学单位") );
					
					if(ExStringUtils.isNotEmpty(changeType)){
						String changeItem = ExStringUtils.trimToEmpty(string==null?"":AnalyzePropertiesNameOfStuInfo(string.toString()));
						if(!changeType.equals(changeItem)){
							continue;
						}
					}
					convertList.add(mapUnit);
				}
			}
		}
		
		//分页处理
		if(null!=convertList&&convertList.size()>0){
			Integer num = convertList.size();
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(num);
			Integer pageSize = objPage.getPageSize();
			if(num%pageSize>0){
				pageTotal = 1+ num/pageSize;
			}else{
				pageTotal = num/pageSize;
			}
			modelMap.addAttribute("pageTotal", pageTotal);
			//每页显示20条记录
			for (int i =0 ;i<pageSize ;i++) {
				if(num>(pageSize*(pageNum-1)+i)) {
					tmpList.add(convertList.get(pageSize*(pageNum-1)+i));
				}
			}
			
			for (Map<String, Object> mapTmp : tmpList) {
				StudentInfoVo infoVo = new StudentInfoVo();
				if(mapTmp.containsKey("修改学生姓名")){infoVo.setAccountStatus(ExStringUtils.trimToEmpty(mapTmp.get("修改学生姓名")==null?"":mapTmp.get("修改学生姓名").toString()));} 
				if(mapTmp.containsKey("修改学生学号")){infoVo.setBloodType(ExStringUtils.trimToEmpty(mapTmp.get("修改学生学号")==null?"":mapTmp.get("修改学生学号").toString()));} 
				if(mapTmp.containsKey("修改学生身份证号")){infoVo.setBornAddress(ExStringUtils.trimToEmpty(mapTmp.get("修改学生身份证号")==null?"":mapTmp.get("修改学生身份证号").toString()));} 
				if(mapTmp.containsKey("修改实体类型")){infoVo.setBornDay(ExStringUtils.trimToEmpty(mapTmp.get("修改实体类型")==null?"":mapTmp.get("修改实体类型").toString()));} 
				if(mapTmp.containsKey("变更项目")){infoVo.setCertNum(ExStringUtils.trimToEmpty(mapTmp.get("变更项目")==null?"":AnalyzePropertiesNameOfStuInfo(mapTmp.get("变更项目").toString())));} 
				if(mapTmp.containsKey("项目原信息")){infoVo.setCertType(ExStringUtils.trimToEmpty(mapTmp.get("变更项目")==null||mapTmp.get("项目原信息")==null?"":convertCodeToValue(mapTmp.get("变更项目").toString(),mapTmp.get("项目原信息").toString())        ));}
				if(mapTmp.containsKey("项目变更后内容")){infoVo.setCountry(ExStringUtils.trimToEmpty(mapTmp.get("变更项目")==null||mapTmp.get("项目变更后内容")==null?"":convertCodeToValue(mapTmp.get("变更项目").toString(),mapTmp.get("项目变更后内容").toString())  ));}
				if(mapTmp.containsKey("修改人姓名")){infoVo.setClassic(ExStringUtils.trimToEmpty(mapTmp.get("修改人姓名")==null?"":mapTmp.get("修改人姓名").toString()));} 
				if(mapTmp.containsKey("所属单位")){infoVo.setContactAddress(ExStringUtils.trimToEmpty(mapTmp.get("所属单位")==null?"":mapTmp.get("所属单位").toString()));} 
				if(mapTmp.containsKey("使用ip")){infoVo.setContactPhone(ExStringUtils.trimToEmpty(mapTmp.get("使用ip")==null?"":mapTmp.get("使用ip").toString()));} 
				if(mapTmp.containsKey("修改时间")){infoVo.setContactZipcode(ExStringUtils.trimToEmpty(mapTmp.get("修改时间")==null?"":mapTmp.get("修改时间").toString()));} 
				//成教新增的字段
				if(mapTmp.containsKey("年级")){infoVo.setEmail(ExStringUtils.trimToEmpty(mapTmp.get("年级")==null?"":mapTmp.get("年级").toString()));} 
				if(mapTmp.containsKey("专业")){infoVo.setCurrenAddress(ExStringUtils.trimToEmpty(mapTmp.get("专业")==null?"":mapTmp.get("专业").toString()));} 
				if(mapTmp.containsKey("层次")){infoVo.setDegree(ExStringUtils.trimToEmpty(mapTmp.get("层次")==null?"":mapTmp.get("层次").toString()));} 
				if(mapTmp.containsKey("班级")){infoVo.setDiplomaNum(ExStringUtils.trimToEmpty(mapTmp.get("班级")==null?"":mapTmp.get("班级").toString()));} 
				if(mapTmp.containsKey("办学单位")){infoVo.setEduYear(ExStringUtils.trimToEmpty(mapTmp.get("办学单位")==null?"":mapTmp.get("办学单位").toString()));} 
				
				finalList.add(infoVo);
			}
			objPage.setResult(finalList);
		}else{
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(0);
			objPage.setResult(finalList);
			modelMap.addAttribute("pageTotal", 0);
		}
		modelMap.addAttribute("stuInfosChangeHistory", objPage);
		List<StudentInfoVo> list=objPage.getResult();
		for (StudentInfoVo map : list) {
			if("出生日期".equals(map.getCertNum())){
				try {
					Long s1=Long.parseLong(map.getCertType()) ;
					map.setCertType(fmtTime(s1));
					
				} catch (NumberFormatException e) {
					
				}
				try {
					Long s2=Long.parseLong(map.getCountry()) ;
					map.setCountry(fmtTime(s2));
				} catch (NumberFormatException e) {
					
				}
				
				
			}
		}
		Map<String,Object> condition = new HashMap<String,Object>(0);
		
		modelMap.addAttribute("condition", map_condition);
		convertList=new ArrayList<Map<String,Object>>(0);
		modelMap.addAttribute("stuInfosChangeHistorys", convertList);
		modelMap.addAttribute("pageNum",pageNum);
		modelMap.addAttribute("val",val);
		return "/edu3/roll/sturegister/getStudentInfoChangeHistory";
	}
	/**
	 * 学籍信息维护历史导出
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/exportStudentInfoChangeHistory.html")
	public void exportStudentInfoChangeHistory(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Page objPage){
		String certnum  	= ExStringUtils.trimToEmpty(request.getParameter("certnum"));
		String studentname  = ExStringUtils.trimToEmpty(request.getParameter("studentname"));
		String studyno    		= ExStringUtils.trimToEmpty(request.getParameter("studyno"));
		String val			= ExStringUtils.trimToEmpty(request.getParameter("val"));
		String changeType   = ExStringUtils.trimToEmpty(request.getParameter("changeType"));
		//成教增加的四个查询条件
		String studentstatus = ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
		String gradeid       = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String operatedateb  = ExStringUtils.trimToEmpty(request.getParameter("operatedateb"));
		String operatedatee  = ExStringUtils.trimToEmpty(request.getParameter("operatedatee"));
		if("statusonly".equals(val)){
			changeType = "学籍状态"; 
		}
		//条件map
		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		//结果集map
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
		if(!"".equals(certnum)){
			map_condition.put("certnum", certnum);
		}
		if(!"".equals(studentname)){
			map_condition.put("studentname", studentname);		
		}
		if(!"".equals(studyno)){
			map_condition.put("studyno", studyno);
		}
		//成教中加的4个条件
		if(ExStringUtils.isNotEmpty(gradeid)){
			map_condition.put("gradeid",gradeid );
		}
		if(ExStringUtils.isNotEmpty(studentstatus)){
			map_condition.put("studentstatus",studentstatus );		
		}
		if(ExStringUtils.isNotEmpty(operatedateb)){
			map_condition.put("operatedateb",operatedateb );
		}
		if(ExStringUtils.isNotEmpty(operatedatee)){
			map_condition.put("operatedatee",operatedatee );
		}
		//执行查询	
		resultList= studentInfoChangeJDBCService.getStudentInfoChangeHistory(map_condition);
		//由List转为分页
		List<Map<String, Object>> tmpList = new ArrayList<Map<String,Object>>(0);
		List<StudentInfoVo> finalList = new ArrayList<StudentInfoVo>(0);
		
		//需求的表格为：学号、注册号、姓名、身份证号、层次、专业名称、学习中心、年级、变更项目、项目原信息、项目变更后内容、[原因说明没有办法获得]、变更日期
		List<Map<String,Object>> convertList = new ArrayList<Map<String,Object>>(0);
		for (Map<String, Object> maptmp0: resultList) {
			  
			String beforeValue = (String)maptmp0.get("修改前的内容");
			String afterValue  = (String)maptmp0.get("修改后的内容"); 
			String[] before =beforeValue.split(",\"");
			String[] after  =afterValue.split(",\"");
			Map<String,String> mapbefore = new HashMap<String, String>(0);
			Map<String,String> mapafter  = new HashMap<String, String>(0);
			StringBuffer beforeResult = new StringBuffer();
			StringBuffer afterResult  = new StringBuffer();
			for (String string : before) {
				string = string.replaceAll("\\{", "");
				string = string.replaceAll("\\}", "");
				string = string.replaceAll("未知", "");
				string = string.replaceAll("null", "\"\"");
				string = string.replaceAll("\"", "");
				if(string.split(":").length==2){
					mapbefore.put(string.split(":")[0], string.split(":")[1]);
				}
				if(string.lastIndexOf(':')==string.length()-1){
					mapbefore.put(string.split(":")[0], "空");
				}
					
			}
			for (String string : after) {
				string = string.replaceAll("\\{", "");
				string = string.replaceAll("\\}", "");
				string = string.replaceAll("未知", "");
				string = string.replaceAll("null", "\"\"");
				string = string.replaceAll("\"", "");
				if(string.split(":").length==2){
					mapafter.put(string.split(":")[0], string.split(":")[1]);
				}
				if(string.lastIndexOf(':')==string.length()-1){
					mapafter.put(string.split(":")[0], "空");
				}
			}
			Set<String> keySet = mapbefore.keySet();
			for (String string : keySet) {
				if(!"".equals(string)&&!mapbefore.get(string).equals(mapafter.get(string))){
					//说明有改变的值
					Map<String,Object> mapUnit = new HashMap<String, Object>(0);
					mapUnit.put("修改学生姓名", maptmp0.get("修改学生姓名") ); 
					mapUnit.put("修改学生学号", maptmp0.get("修改学生学号") ); 
					mapUnit.put("修改学生身份证号", maptmp0.get("修改学生身份证号") ); 
					mapUnit.put("修改实体类型", maptmp0.get("修改实体类型") );
					mapUnit.put("修改人姓名", maptmp0.get("修改人姓名") ); 
					mapUnit.put("所属单位", maptmp0.get("所属单位") ); 
					mapUnit.put("使用ip",  maptmp0.get("使用ip") ); 
					mapUnit.put("修改时间", maptmp0.get("修改时间") );
					mapUnit.put("变更项目",    string ); 
					mapUnit.put("项目原信息",   mapbefore.get(string) );  
					mapUnit.put("项目变更后内容", mapafter.get(string) ); 
					//成教新增的字段
					mapUnit.put("年级", maptmp0.get("年级") );
					mapUnit.put("专业", maptmp0.get("专业") );
					mapUnit.put("层次", maptmp0.get("层次") );
					mapUnit.put("班级", maptmp0.get("班级") );
					mapUnit.put("办学单位", maptmp0.get("办学单位") );
					
					if(ExStringUtils.isNotEmpty(changeType)){
						String changeItem = ExStringUtils.trimToEmpty(string==null?"":AnalyzePropertiesNameOfStuInfo(string.toString()));
						if(!changeType.equals(changeItem)){
							continue;
						}
					}
					convertList.add(mapUnit);
				}
			}
		}
		
		//分页处理
		if(null!=convertList&&convertList.size()>0){
			for (Map<String, Object> mapTmp : convertList) {
				StudentInfoVo infoVo = new StudentInfoVo();
				if(mapTmp.containsKey("修改学生姓名")){infoVo.setAccountStatus(ExStringUtils.trimToEmpty(mapTmp.get("修改学生姓名")==null?"":mapTmp.get("修改学生姓名").toString()));} 
				if(mapTmp.containsKey("修改学生学号")){infoVo.setBloodType(ExStringUtils.trimToEmpty(mapTmp.get("修改学生学号")==null?"":mapTmp.get("修改学生学号").toString()));} 
				if(mapTmp.containsKey("修改学生身份证号")){infoVo.setBornAddress(ExStringUtils.trimToEmpty(mapTmp.get("修改学生身份证号")==null?"":mapTmp.get("修改学生身份证号").toString()));} 
				if(mapTmp.containsKey("修改实体类型")){infoVo.setBornDay(ExStringUtils.trimToEmpty(mapTmp.get("修改实体类型")==null?"":mapTmp.get("修改实体类型").toString()));} 
				if(mapTmp.containsKey("变更项目")){infoVo.setCertNum(ExStringUtils.trimToEmpty(mapTmp.get("变更项目")==null?"":AnalyzePropertiesNameOfStuInfo(mapTmp.get("变更项目").toString())));} 
				if(mapTmp.containsKey("项目原信息")){infoVo.setCertType(ExStringUtils.trimToEmpty(mapTmp.get("变更项目")==null||mapTmp.get("项目原信息")==null?"":convertCodeToValue(mapTmp.get("变更项目").toString(),mapTmp.get("项目原信息").toString())        ));}
				if(mapTmp.containsKey("项目变更后内容")){infoVo.setCountry(ExStringUtils.trimToEmpty(mapTmp.get("变更项目")==null||mapTmp.get("项目变更后内容")==null?"":convertCodeToValue(mapTmp.get("变更项目").toString(),mapTmp.get("项目变更后内容").toString())  ));}
				if(mapTmp.containsKey("修改人姓名")){infoVo.setClassic(ExStringUtils.trimToEmpty(mapTmp.get("修改人姓名")==null?"":mapTmp.get("修改人姓名").toString()));} 
				if(mapTmp.containsKey("所属单位")){infoVo.setContactAddress(ExStringUtils.trimToEmpty(mapTmp.get("所属单位")==null?"":mapTmp.get("所属单位").toString()));} 
				if(mapTmp.containsKey("使用ip")){infoVo.setContactPhone(ExStringUtils.trimToEmpty(mapTmp.get("使用ip")==null?"":mapTmp.get("使用ip").toString()));} 
				if(mapTmp.containsKey("修改时间")){infoVo.setContactZipcode(ExStringUtils.trimToEmpty(mapTmp.get("修改时间")==null?"":mapTmp.get("修改时间").toString().split(" ")[0]));} 
				//成教新增的字段
				if(mapTmp.containsKey("年级")){infoVo.setEmail(ExStringUtils.trimToEmpty(mapTmp.get("年级")==null?"":mapTmp.get("年级").toString()));} 
				if(mapTmp.containsKey("专业")){infoVo.setCurrenAddress(ExStringUtils.trimToEmpty(mapTmp.get("专业")==null?"":mapTmp.get("专业").toString()));} 
				if(mapTmp.containsKey("层次")){infoVo.setDegree(ExStringUtils.trimToEmpty(mapTmp.get("层次")==null?"":mapTmp.get("层次").toString()));} 
				if(mapTmp.containsKey("班级")){infoVo.setDiplomaNum(ExStringUtils.trimToEmpty(mapTmp.get("班级")==null?"":mapTmp.get("班级").toString()));} 
				if(mapTmp.containsKey("办学单位")){infoVo.setEduYear(ExStringUtils.trimToEmpty(mapTmp.get("办学单位")==null?"":mapTmp.get("办学单位").toString()));} 
				
				
				finalList.add(infoVo);
			}
		}else{
			
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			if("statusonly".equals(val)){
				exportExcelService.initParmasByfile(disFile, "StudentInfoChangeStudentStatusHis",finalList,null);//初始化配置参数——学籍状态
			}else{
				exportExcelService.initParmasByfile(disFile, "StudentInfoChangeHis",finalList,null);//初始化配置参数
			}
			
			exportExcelService.getModelToExcel().setHeader("学生信息维护历史");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "学生信息维护历史表"+".xls", excelFile.getAbsolutePath(),true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
	/**
	 * 转换属性值转为中文
	 * @return
	 */
	private String AnalyzePropertiesNameOfStuInfo(String propertiesName){
		Map<String,String> map = new HashMap<String, String>(0);
	    //studentBaseInfo
		map.put("name","姓名");
	    map.put("nameUsed","曾用名");
	    map.put("namePY","姓名拼音");
	    map.put("gender","性别");
	    map.put("photoPath","相片路径");
	    map.put("certType","证件类型");
	    map.put("certNum","证件号码");
	    map.put("contactAddress","联系地址");
	    map.put("contactZipcode","联系邮编");
	    map.put("contactPhone","联系电话");
	    map.put("mobile","移动电话");
	    map.put("email","电子邮箱");
	    map.put("homePage","个人主页");
	    map.put("bankBook","银行账户");
	    map.put("height","身高");
	    map.put("bloodType","血型");
	    map.put("bornDay","出生日期");
	    map.put("bornAddress","出生地");
	    map.put("country","国籍");
	    map.put("homePlace","籍贯");
	    map.put("gaqCode","港澳侨代码");
	    map.put("nation","民族");
	    map.put("health","健康状态");
	    map.put("marriage","婚姻状况");
	    map.put("politics","政治面貌");
	    map.put("faith","宗教信仰");
	    map.put("residenceKind","户口性质");
	    map.put("residence","户口所在地"); 
	    map.put("currenAddress","现住址");
	    map.put("homeAddress","住址");
	    map.put("homezipCode","住址邮编");
	    map.put("homePhone","家庭电话");
	    map.put("officeName","公司名称");
	    map.put("officePhone","公司电话");
	    map.put("title","职务职称");
	    map.put("specialization","特长");
	    map.put("memo","备注");
	    map.put("loginName","登录名");
	    map.put("certPhotoPath","身份证书扫描件图片路径");
	    map.put("eduPhotoPath","学历证书扫描件图片路径");
	    map.put("bookletPhotoPath","户口册图片路径");
	    map.put("otherPhotoPath","其他图片路径");
	    map.put("industryType","行业类别");
	    map.put("workStatus","职业状态");
	    //studentInfo
	    map.put("studyNo","学生号");
	 	map.put("enrolleeCode","考生号");
	 	map.put("studentName","学生姓名");
	 	map.put("attendAdvancedStudies","进修性质"); //:字典 学历教育/非学历教育
	 	map.put("learningStyle","学习方式"); //:字典 脱产/半脱产/业余/全日制
	 	map.put("studyInSchool","就读方式"); //字典：走读/住读/借宿/其他
	 	map.put("studentKind","学习类别"); //:CodeStudentKind
	 	map.put("enterSchool","入学性质"); //：字典 考试入学/免试入学,根据申请免试
	 	map.put("learingStatus","在学状态"); //字典：正式录取/进修
	 	map.put("studentStatus","学籍状态"); //字典：在学 11/休学 12/退学 13/勒令退学 14/开除学籍 15/毕业 16/自动流失 17/学习期限已过 18
	 	map.put("memo","备注");
	 	map.put("examOrderStatus","考试预约状态");  //= Constants.BOOLEAN_TRUE","考试预约状态：0-不允许 1-允许
	 	map.put("isAhead","提前修读");          // = Constants.BOOLEAN_NO","是否提前修读
	 	map.put("isOrderSubject","预约毕业论文"); // = Constants.BOOLEAN_NO","是否预约毕业论文
	 	map.put("isApplyGraduate","毕业学位申请");  //= Constants.BOOLEAN_YES","是否申请学位 Y-毕业+学位 N-毕业    ?W-延迟毕业
	 	map.put("registorNo","注册号");  ;       //录取时生成，注册时从报名信息表中拷贝过来，用于上报新生
	 	map.put("accountStatus","学生帐号状态");   //= Constants.BOOLEAN_TRUE;//学生账号状态 默认，开启	
	 	map.put("orderCourseStatus","学生预约状态"); // = Constants.BOOLEAN_TRUE;//学生预约状态 默认，启用 用来控制是否能预约学习的权限，如未交费不让预约学习
	 	map.put("isOrderFirstCourse","预约第一学期课程"); //= Constants.BOOLEAN_NO;//默认为未预约
	 	map.put("teachingType","教学模式");         //字典值
	 	map.put("finishedCreditHour","已修总学分");
	 	map.put("finishedNecessCreditHour","已修必修课总学分");
	 	map.put("isAbleOrderSubject","允许预约毕业论文"); // = Constants.BOOLEAN_TRUE;//默认为允许
	 	map.put("finishedOptionalCourseNum","已完成限选课门数"); // = 0;//已完成选修课门数
	 	map.put("enterAuditStatus","入学资格"); //= Constants.BOOLEAN_WAIT;//入学资格审核
	 	map.put("graduateSchool","毕业院校");						 // 毕业院校
	 	map.put("graduateSchoolCode","毕业学校代码");
	 	map.put("graduateMajor","毕业专业");
	 	map.put("graduateId","毕业证书号");
	 	map.put("graduateDate","毕业日期");
	 	map.put("transcriptsNo","英文成绩单编号");
	 	map.put("degreeStatus","学位状态"); //= Constants.BOOLEAN_WAIT; //W-待审核
	    //以上属于基本类型的判断
	 	map.put("major","专业");
	 	map.put("grade","年级");
	 	map.put("branchSchool","校外学习中心");
	 	map.put("classic","层次");
	 	map.put("teachingPlan","教学计划");
	 	map.put("sysUser","系统用户名");
	 	map.put("inDate","入学日期");
		return map.get(propertiesName);
	}
	/**
	 * 实现 不同的字段按照对应的字典
	 * @param propertiesName
	 * @param value
	 * @return
	 */
	private String convertCodeToValue(String propertiesName,String value){
		//studentBaseInfo
		if("gender".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeSex", value);} 
		else if("certType".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeCertType", value);} 
		else if("bloodType".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeBloodStyle", value);} 
		else if("country".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeCountry", value);} 
		else if("gaqCode".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeGAQ", value);} 
		else if("nation".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeNation", value);} 
		else if("health".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeHealth", value);} 
		else if("marriage".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeMarriage", value);} 
		else if("politics".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodePolitics_simple", value);}
		else if("residenceKind".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeRegisteredResidenceKind", value);}  
		//studentInfo
		else if("attendAdvancedStudies".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeAttendAdvancedStudies", value);}                      //字典 学历教育/非学历教育
		else if("learningStyle".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle", value);}              //字典 脱产/半脱产/业余/全日制
		else if("studyInSchool".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeStudyInSchool", value);}              //字典：走读/住读/借宿/其他
		else if("studentKind".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeStudentKind", value);} 	              //:CodeStudentKind
		else if("enterSchool".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeEnterSchool", value);}                //：字典 考试入学/免试入学,根据申请免试
		else if("learingStatus".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeLearingStatus", value);}              //字典：正式录取/进修
		else if("studentStatus".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", value);}              //字典：在学 11/休学 12/退学 13/勒令退学 14/开除学籍 15/毕业 16/自动流失 17/学习期限已过 18
		else if("teachingType".equals(propertiesName)){value=JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", value);}   
		else if("examOrderStatus".equals(propertiesName)&&!"空".equals(value)){if(Constants.BOOLEAN_TRUE==Integer.parseInt(value)){value="允许";}else if(Constants.BOOLEAN_FALSE==Integer.parseInt(value)){value="不允许";}}               // 考试预约状态：0-不允许 1-允许
		else if("isAhead".equals(propertiesName)){if(Constants.BOOLEAN_YES.equals(value)){value="是";}else if(Constants.BOOLEAN_NO.equals(value)){value="否";}}                      // 是否提前修读
		else if("isOrderSubject".equals(propertiesName)){if(Constants.BOOLEAN_YES.equals(value)){value="是";}else if(Constants.BOOLEAN_NO.equals(value)){value="否";}}               // 是否预约毕业论文
		else if("isApplyGraduate".equals(propertiesName)){
			if("Y".equals(value)){value="申请毕业，申请学位";}
			else if(Constants.BOOLEAN_NO.equals(value)){value="申请毕业";}
			else if(Constants.BOOLEAN_WAIT.equals(value)){value="延迟毕业";} 
		}// 是否申请学位 Y-毕业+学位 N-毕业    ?W-延迟毕业
		     
		else if("accountStatus".equals(propertiesName)){if(Constants.BOOLEAN_TRUE==Integer.parseInt(value)){value="开启";}else if(Constants.BOOLEAN_FALSE==Integer.parseInt(value)){value="关闭";}}                //学生账号状态 默认，开启	
		else if("orderCourseStatus".equals(propertiesName)&&!"空".equals(value)){if(Constants.BOOLEAN_TRUE==Integer.parseInt(value)){value="启用";}else if(Constants.BOOLEAN_FALSE==Integer.parseInt(value)){value="禁用";}}          //学生预约状态 默认，启用 用来控制是否能预约学习的权限，如未交费不让预约学习
		else if("isAbleOrderSubject".equals(propertiesName)&&!"空".equals(value)){if(Constants.BOOLEAN_TRUE==Integer.parseInt(value)){value="允许";}else if(Constants.BOOLEAN_FALSE==Integer.parseInt(value)){value="不允许";}} 
		else if("isOrderFirstCourse".equals(propertiesName)){if(Constants.BOOLEAN_YES.equals(value)){value="是";}else if(Constants.BOOLEAN_NO.equals(value)){value="否";}}           //默认为未预
		else if("enterAuditStatus".equals(propertiesName)){
			if(Constants.BOOLEAN_YES.equals(value)){value="审核通过";}
			else if(Constants.BOOLEAN_NO.equals(value)){value="审核不通过";}
			else if("W".equals(value)){value="待审核";}
		}
		else if("degreeStatus".equals(propertiesName)){
			if(Constants.BOOLEAN_YES.equals(value)){value="审核通过";}
			else if(Constants.BOOLEAN_NO.equals(value)){value="审核不通过";}
			else if(Constants.BOOLEAN_WAIT.equals(value)){value="待审核";} 
		}  
		else if("major".equals(propertiesName)){value = majorService.get(value).getMajorName();}
		else if("grade".equals(propertiesName)){value = gradeService.get(value).getGradeName();}
		else if("branchSchool".equals(propertiesName)){value = orgUnitService.get(value).getUnitName();}
		else if("classic".equals(propertiesName)){value = classicService.get(value).getClassicName();}
		else if("teachingPlan".equals(propertiesName)){value = teachingplanservice.get(value).getTeachingPlanName();}
		else if("sysUser".equals(propertiesName)){ value = userService.get(value).getCnName();}
		return value;
	}
	
	
	/**
	 * 学籍统计条件
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/exportStuInfoStatCondition.html")
	public String exportStuInfoStatCondition(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		model=getNewStudentStatus(model);
		return "/edu3/roll/sturegister/exportStuInfoStatCondition";
	}
	/**
	 * 学籍统计
	 * @param request
	 * @param respose
	 * @param modelMap
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/studentInfoStat.html")
	public String guaduationStat(HttpServletRequest request,HttpServletResponse respose,ModelMap modelMap,Page objPage){
		modelMap=getNewStudentStatus(modelMap);
		String statStudentStatus  	= ExStringUtils.trimToEmpty(request.getParameter("statStudentStatus"));
		String stuStatusRes = statStudentStatus;
		modelMap.addAttribute("stuStatusRes", stuStatusRes);
		String accountStatus = null;
		if(statStudentStatus.contains("a")){
			accountStatus="1";
			statStudentStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(statStudentStatus.contains("b")){
			accountStatus="0";
			statStudentStatus="11";//去掉学籍中有关帐号状态的标记
		}
		String statStuBranchSchool  = ExStringUtils.trimToEmpty(request.getParameter("statStuBranchSchool"));
		String statStuGrade    		= ExStringUtils.trimToEmpty(request.getParameter("statStuGrade"));
		String statStuClassic 		= ExStringUtils.trimToEmpty(request.getParameter("statStuClassic"));
		String statStuMajor    		= ExStringUtils.trimToEmpty(request.getParameter("statStuMajor"));
		String statStuStudyMode     = ExStringUtils.trimToEmpty(request.getParameter("statStuStudyMode"));
		String param 				= ExStringUtils.trimToEmpty(request.getParameter("stuStatParam"));
		
		Integer pageNum = 1;
		Integer pageTotal = 1;
		if(0!=objPage.getPageNum()){
			pageNum = objPage.getPageNum();
		}
		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		map_condition.put("statType", param);
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
		if(!"".equals(statStudentStatus)){
			map_condition.put("statStudentStatus", statStudentStatus);
		}
		if(!"".equals(statStuBranchSchool)){
			map_condition.put("statStuBranchSchool", statStuBranchSchool);		
		}
		if(!"".equals(statStuGrade)){
			map_condition.put("statStuGrade", statStuGrade);
		}
		if(!"".equals(statStuClassic)){
			map_condition.put("statStuClassic", statStuClassic);
		}
		if(!"".equals(statStuMajor)){
			map_condition.put("statStuMajor", statStuMajor);
		}
		if(!"".equals(statStuStudyMode)){
			map_condition.put("statStuStudyMode", statStuStudyMode);
		}
		if(null!=accountStatus){
			map_condition.put("accountStatus", accountStatus);
		}
	
		if("13".equals(param)){
			List<Map<String,Object>> li=studentInfoChangeJDBCService.countEdumanager(param);
			List<Map<String,Object>> liwm=studentInfoChangeJDBCService.countwomanEdumanager(param);
			
			if(liwm.size()>0){
				Map<String,Object> lw=liwm.get(0);
				modelMap.addAttribute("counwm",lw.get("COUN"));
			}
			int i=0;
			for (Map<String, Object> m : li) {
				Object j=m.get("COUN");
				int k=0;
				try {
					k=Integer.valueOf(String.valueOf(j)).intValue();
				} catch (Exception e) {
					
				}
				i+=k;
			}
			modelMap.addAttribute("counsum", i);
			objPage.setResult(li);
			objPage.setAutoCount(true);
			objPage.setPageNum(1);
			objPage.setTotalCount(li.size());
		
		}else if("14".equals(param)){
			resultList= studentInfoChangeJDBCService.countDegreeEdumanager();
			objPage.setResult(resultList);
			objPage.setAutoCount(true);
			objPage.setPageNum(1);
			objPage.setTotalCount(resultList.size());
		}else{
			//因为要计算年龄,传入当前时间
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			map_condition.put("current_date", df.format(new Date()));
			
			resultList= studentInfoChangeJDBCService.statStudentInfo(map_condition);
			if("6".equals(param)){//按年龄统计人数
				List<Map<String,Object>> tmpAddList = new ArrayList<Map<String,Object>>(0);
				List<Map<String,Object>> tmpFinalList = new ArrayList<Map<String,Object>>(0);
				for (Map<String, Object> map : resultList) {
					if("本科 男".equals(map.get("统计项目")+"")){
						map.put("统计项目", "本科学生");
						for (Map<String, Object> map_inner : resultList) {
							if("本科 女".equals(map_inner.get("统计项目")+"")){
								map = mapAdd(map,map_inner);
							}
						}
					}
					if("专科 男".equals(map.get("统计项目")+"")){
						map.put("统计项目", "专科学生");
						for (Map<String, Object> map_inner : resultList) {
							if("专科 女".equals(map_inner.get("统计项目")+"")){
								map = mapAdd(map,map_inner);
							}
						}
					}
					tmpAddList.add(map);
				}
				for (Map<String, Object> map : tmpAddList) {
					if("本科学生".equals(map.get("统计项目")+"")){
						tmpFinalList.add(map);
						for (Map<String, Object> map_inner : resultList) {
							if("本科 女".equals(map_inner.get("统计项目")+"")){
								map_inner.put("统计项目", "&nbsp;&nbsp;其中:女");
								tmpFinalList.add(map_inner);
							}
						}
					}
					if("专科学生".equals(map.get("统计项目")+"")){
						tmpFinalList.add(map);
						for (Map<String, Object> map_inner : resultList) {
							if("专科 女".equals(map_inner.get("统计项目")+"")){
								map_inner.put("统计项目", "&nbsp;&nbsp;其中:女");
								tmpFinalList.add(map_inner);
							}
						}
					}
				}
				resultList=tmpFinalList;
				
			}
			
			//由List转为分页
			List<Map<String, Object>> tmpList = new ArrayList<Map<String,Object>>(0);
			List<StudentInfoVo> finalList = new ArrayList<StudentInfoVo>(0);
			if(null!=resultList&&resultList.size()>0){
				Integer num = resultList.size();
				objPage.setAutoCount(true);
				objPage.setPageNum(pageNum);
				objPage.setTotalCount(num);
				Integer pageSize = objPage.getPageSize();
				if(num%pageSize>0){
					pageTotal = 1+ num/pageSize;
				}else{
					pageTotal = num/pageSize;
				}
				modelMap.addAttribute("pageTotal", pageTotal);
				//每页显示20条记录
				for (int i =0 ;i<pageSize ;i++) {
					if(num>(pageSize*(pageNum-1)+i)) {
						tmpList.add(resultList.get(pageSize*(pageNum-1)+i));
					}
				}
				
				for (Map<String,Object> tmpUnit : tmpList) {
					StudentInfoVo infoVo = new StudentInfoVo();
					if("6".equals(param)){
						if(tmpUnit.containsKey("统计项目")) {
							infoVo.setAccountStatus(ExStringUtils.trimToEmpty((String)tmpUnit.get("统计项目")));
						}
						if(tmpUnit.containsKey("17岁以下")) {
							infoVo.setBloodType(ExStringUtils.trimToEmpty(tmpUnit.get("17岁以下").toString()));
						}
						if(tmpUnit.containsKey("17岁")) {
							infoVo.setBornAddress(ExStringUtils.trimToEmpty(tmpUnit.get("17岁").toString()));
						}
						if(tmpUnit.containsKey("18岁")) {
							infoVo.setBornDay(ExStringUtils.trimToEmpty(tmpUnit.get("18岁").toString()));
						}
						if(tmpUnit.containsKey("19岁")) {
							infoVo.setCertNum(ExStringUtils.trimToEmpty(tmpUnit.get("19岁").toString()));
						}
						if(tmpUnit.containsKey("20岁")) {
							infoVo.setCertType(ExStringUtils.trimToEmpty(tmpUnit.get("20岁").toString()));
						}
						if(tmpUnit.containsKey("21岁")) {
							infoVo.setClassic(ExStringUtils.trimToEmpty(tmpUnit.get("21岁").toString()));
						}
						if(tmpUnit.containsKey("22岁")) {
							infoVo.setContactAddress(ExStringUtils.trimToEmpty(tmpUnit.get("22岁").toString()));
						}
						if(tmpUnit.containsKey("23岁")) {
							infoVo.setContactPhone(ExStringUtils.trimToEmpty(tmpUnit.get("23岁").toString()));
						}
						if(tmpUnit.containsKey("24岁")) {
							infoVo.setContactZipcode(ExStringUtils.trimToEmpty(tmpUnit.get("24岁").toString()));
						}
						if(tmpUnit.containsKey("25岁")) {
							infoVo.setCountry(ExStringUtils.trimToEmpty(tmpUnit.get("25岁").toString()));
						}
						if(tmpUnit.containsKey("26岁")) {
							infoVo.setCurrenAddress(ExStringUtils.trimToEmpty(tmpUnit.get("26岁").toString()));
						}
						if(tmpUnit.containsKey("27岁")) {
							infoVo.setDegree(ExStringUtils.trimToEmpty(tmpUnit.get("27岁").toString()));
						}
						if(tmpUnit.containsKey("28岁")) {
							infoVo.setEmail(ExStringUtils.trimToEmpty(tmpUnit.get("28岁").toString()));
						}
						if(tmpUnit.containsKey("29岁")) {
							infoVo.setEnterAttr(ExStringUtils.trimToEmpty(tmpUnit.get("29岁").toString()));
						}
						if(tmpUnit.containsKey("30岁")) {
							infoVo.setEnterSchool(ExStringUtils.trimToEmpty(tmpUnit.get("30岁").toString()));
						}
						if(tmpUnit.containsKey("30岁以上")) {
							infoVo.setExamNo(ExStringUtils.trimToEmpty(tmpUnit.get("30岁以上").toString()));
						}
					}else{
						if(tmpUnit.containsKey("total")){infoVo.setAccountStatus(tmpUnit.get("total").toString());}
						if(tmpUnit.containsKey("man")){infoVo.setBloodType(tmpUnit.get("man").toString());}
						if(tmpUnit.containsKey("lady")){infoVo.setBornAddress(tmpUnit.get("lady").toString());}
						if(tmpUnit.containsKey("本科生")){infoVo.setBornDay(tmpUnit.get("本科生").toString());}
						if(tmpUnit.containsKey("专科生")){infoVo.setCertNum(tmpUnit.get("专科生").toString());}
						if(tmpUnit.containsKey("纯网")){infoVo.setCertType(tmpUnit.get("纯网").toString());}
						if(tmpUnit.containsKey("网成")){infoVo.setContactAddress(tmpUnit.get("网成").toString());}
						if(tmpUnit.containsKey("共产党员")){infoVo.setContactPhone(tmpUnit.get("共产党员").toString());}
						if(tmpUnit.containsKey("共青团员")){infoVo.setContactZipcode(tmpUnit.get("共青团员").toString());}
						if(tmpUnit.containsKey("民主党派")){infoVo.setCountry(tmpUnit.get("民主党派").toString());}
						if(tmpUnit.containsKey("华侨")){infoVo.setCurrenAddress(tmpUnit.get("华侨").toString());}
						if(tmpUnit.containsKey("港澳台")){infoVo.setDegree(tmpUnit.get("港澳台").toString());}
						if(tmpUnit.containsKey("少数民族")){infoVo.setEmail(tmpUnit.get("少数民族").toString());}
						if(tmpUnit.containsKey("残疾人")){infoVo.setEnterAttr(tmpUnit.get("残疾人").toString());};
						//if(tmpUnit.containsKey("在学")){infoVo.setEnterSchool(tmpUnit.get("在学").toString());}
						if(tmpUnit.containsKey("正常注册")){infoVo.setEnterSchool(tmpUnit.get("正常注册").toString());}
						if(tmpUnit.containsKey("正常未注册")){infoVo.setExamNo(tmpUnit.get("正常未注册").toString());}
						if(tmpUnit.containsKey("过期")){infoVo.setFaith(tmpUnit.get("过期").toString());}
						if(tmpUnit.containsKey("延期")){infoVo.setGaqCode(tmpUnit.get("延期").toString());}
						if(tmpUnit.containsKey("自动流失")){infoVo.setGender(tmpUnit.get("自动流失").toString());}
						if(tmpUnit.containsKey("开除学籍")){infoVo.setHealth(tmpUnit.get("开除学籍").toString());}
						if(tmpUnit.containsKey("退学")){infoVo.setHeight(tmpUnit.get("退学").toString());}
						if(tmpUnit.containsKey("classicname")){infoVo.setClassic(ExStringUtils.trimToEmpty((String)tmpUnit.get("classicname")));}
						if(tmpUnit.containsKey("unitname")){infoVo.setUnit(ExStringUtils.trimToEmpty((String)tmpUnit.get("unitname")));}
						if(tmpUnit.containsKey("gradename")){infoVo.setGrade(ExStringUtils.trimToEmpty((String)tmpUnit.get("gradename")));}
						if(tmpUnit.containsKey("majorname")){infoVo.setMajor(ExStringUtils.trimToEmpty((String)tmpUnit.get("majorname")));}
						if(tmpUnit.containsKey("teachingtype")){infoVo.setTeachingType(ExStringUtils.trimToEmpty((String)tmpUnit.get("teachingtype")));}
						if(tmpUnit.containsKey("homePlace")){infoVo.setHomePlace(ExStringUtils.trimToEmpty((String)tmpUnit.get("homePlace")));}
					}
					finalList.add(infoVo);
				}
				
				objPage.setResult(finalList);
			}else{
				objPage.setAutoCount(true);
				objPage.setPageNum(pageNum);
				objPage.setTotalCount(0);
				objPage.setResult(finalList);
				modelMap.addAttribute("pageTotal", 0);
			}
		}
		
		modelMap.addAttribute("stuInfos1", objPage);
		
		
		
		Map<String,Object> condition = new HashMap<String,Object>(0);
		
		
		condition.put("statStudentStatus",statStudentStatus );
		condition.put("statStuGrade",statStuGrade );
		condition.put("statStuBranchSchool",statStuBranchSchool );
		condition.put("statStuClassic",statStuClassic );
		condition.put("statStuMajor",statStuMajor );
		condition.put("statStuStudyMode",statStuStudyMode );
		condition.put("stuStatParam",param );
		modelMap.addAttribute("condition", condition);
//		if("3".equals(param)){
		resultList=new ArrayList<Map<String,Object>>(0);
//		}
		modelMap.addAttribute("stuInfos", resultList);
		modelMap.addAttribute("pageNum",pageNum);
		return "/edu3/roll/sturegister/exportStuInfoStatCondition";
	}
	/**
	 * 获得各个学历下的人数总和
	 * @param map_man
	 * @param map_lady
	 * @return
	 */
	private Map<String,Object> mapAdd(Map<String,Object> map_man,Map<String,Object> map_lady){
		Integer i1=Integer.parseInt(map_man.get("17岁以下").toString())+Integer.parseInt(map_lady.get("17岁以下").toString());
		map_man.put("17岁以下",i1.toString());
		for(Integer i = 17;i<31;i++){
			i1=Integer.parseInt(map_man.get(i+"岁").toString())+Integer.parseInt(map_lady.get(i+"岁").toString());
			map_man.put(i+"岁",i1.toString());
		}
		i1=Integer.parseInt(map_man.get("30岁以上").toString())+Integer.parseInt(map_lady.get("30岁以上").toString());
		map_man.put("30岁以上",i1.toString());
		return map_man; 
	}
	
	/**
	 * 导出学籍信息统计
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/register/studentinfo/exportStudentInfoStat.html")
	public void exportGraduationInfos(HttpServletRequest request,HttpServletResponse response) {
	//从库中查出数据		
		//得到参数
		
		String statStudentStatus  	= ExStringUtils.trimToEmpty(request.getParameter("statStudentStatus"));
		String statStuBranchSchool  = ExStringUtils.trimToEmpty(request.getParameter("statStuBranchSchool"));
		String statStuGrade    		= ExStringUtils.trimToEmpty(request.getParameter("statStuGrade"));
		String statStuClassic 		= ExStringUtils.trimToEmpty(request.getParameter("statStuClassic"));
		String statStuMajor    		= ExStringUtils.trimToEmpty(request.getParameter("statStuMajor"));
		String statStuStudyMode     = ExStringUtils.trimToEmpty(request.getParameter("statStuStudyMode"));
		String param 				= ExStringUtils.trimToEmpty(request.getParameter("stuStatParam"));
		if("13".equals(param)){
			try{
				List<StudentInfoVo> results = new ArrayList<StudentInfoVo>(0);
				File excelFile = null;
				List<Map<String,Object>> li=studentInfoChangeJDBCService.countEdumanager(param);
				List<Map<String,Object>> liwm=studentInfoChangeJDBCService.countwomanEdumanager(param);
				for (Map<String, Object> map : li) {
					StudentInfoVo stu=new StudentInfoVo();
					String type=map.get("TYPE")==null?"":map.get("TYPE").toString();
					type=JstlCustomFunction.dictionaryCode2Value("CodeTitleOfTechnicalCode",type);
					if(type.length()<1){
						type="未定级";
					}
					stu.setBloodType(type);
					stu.setAccountStatus(map.get("coun")==null?"":map.get("coun").toString());
					results.add(stu);
				}
				
				StudentInfoVo stu1=new StudentInfoVo();
				Map<String,Object> m=liwm==null?new HashMap<String, Object>():liwm.get(0);
				stu1.setBloodType("其中：女");
				stu1.setAccountStatus(m.get("coun")==null?"":m.get("coun").toString());
				results.add(stu1);
				int i=0;
				for (Map<String, Object> m1 : li) {
					Object j=m1.get("COUN");
					int k=0;
					try {
						k=Integer.valueOf(String.valueOf(j)).intValue();
					} catch (Exception e) {
						
					}
					i+=k;
				}
				StudentInfoVo stu2=new StudentInfoVo();
				stu2.setBloodType("总计：");
				stu2.setAccountStatus(String.valueOf(i));
				results.add(stu2);
				List<MergeCellsParam> mergeCellsParams = new ArrayList<MergeCellsParam>();//合并坐标
//				//合并第一列第一行到第六列第一行的所有单元格 
					 for (int j = 0; j < results.size(); j++) {
							mergeCellsParams.add(new MergeCellsParam(0,6+j,1,6+j));
					}
					 mergeCellsParams.add(new MergeCellsParam(0,6+results.size(),0,6+results.size()+6));
					 StudentInfoVo   l1=new StudentInfoVo();  l1.setBloodType("其中聘任制");l1.setDegree("小  计"); results.add(l1);
					 StudentInfoVo   l2=new StudentInfoVo();l2.setDegree("其中：女");results.add(l2);
					 StudentInfoVo   l3=new StudentInfoVo();l3.setDegree("正 高 级");results.add(l3);
					 StudentInfoVo   l4=new StudentInfoVo();l4.setDegree("副 高 级");results.add(l4);
					 StudentInfoVo   l5=new StudentInfoVo();l5.setDegree("中    级");results.add(l5);
					 StudentInfoVo   l6=new StudentInfoVo();l6.setDegree("初    级");results.add(l6);
					 StudentInfoVo   l7=new StudentInfoVo();l7.setDegree("未定职级");results.add(l7);
					 
				GUIDUtils.init();
				File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(true) + ".xls");
				String pa = "countTeachers.xls";
				exportExcelService.initParamsByfile(disFile, "StudentInfoStat13", results,null,null,mergeCellsParams);//初始化配置参数
				
				exportExcelService.getModelToExcel().setTemplateParam(pa,6,null);
//				exportExcelService.getModelToExcel().setStartRow(6);
//				exportExcelService.getModelToExcel().setStartColumn(1);
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高			
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				 
			
				
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, "外聘老师统计表"+".xls", excelFile.getAbsolutePath(),true);
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}else if("14".equals(param)){
			List<EdumanagerVo> results = new ArrayList<EdumanagerVo>(0);
			List<Map<String,Object>> resultList= studentInfoChangeJDBCService.countDegreeEdumanager();
			int i=0;
			for (Map<String, Object> m : resultList) {
				EdumanagerVo v=new EdumanagerVo();
				v.setNum(String.valueOf(++i));
				v.setC_q(m.get("c_q")==null?"":m.get("c_q").toString());
				v.setC_b(m.get("c_b")==null?"":m.get("c_b").toString());
				v.setC_s(m.get("c_s")==null?"":m.get("c_s").toString());
				v.setB_q(m.get("b_q")==null?"":m.get("b_q").toString());
				v.setB_b(m.get("b_b")==null?"":m.get("b_b").toString());
				v.setB_s(m.get("b_s")==null?"":m.get("b_s").toString());
				v.setS_q(m.get("s_q")==null?"":m.get("s_q").toString());
				v.setS_b(m.get("s_b")==null?"":m.get("s_b").toString());
				v.setS_s(m.get("s_s")==null?"":m.get("s_s").toString());
				v.setBk_q(m.get("bk_q")==null?"":m.get("bk_q").toString());
				v.setBk_b(m.get("bk_b")==null?"":m.get("bk_b").toString());
				v.setBk_s(m.get("bk_s")==null?"":m.get("bk_s").toString());
				v.setQ_q(m.get("q_q")==null?"":m.get("q_q").toString());
				v.setQ_b(m.get("q_b")==null?"":m.get("q_b").toString());
				v.setQ_s(m.get("q_s")==null?"":m.get("q_s").toString());
				v.setType(m.get("type")==null?"":m.get("type").toString());
				results.add(v);
			}
				File excelFile = null;
			 try{
					GUIDUtils.init();
					File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(true) + ".xls");
					String pa=SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"Edumanager.xls";
					exportExcelService.initParamsByfile(disFile, "EdumanagerVo", results,null,null,null);//初始化配置参数
					
					exportExcelService.getModelToExcel().setTemplateParam(pa,6,null);
//					exportExcelService.getModelToExcel().setStartRow(6);
//					exportExcelService.getModelToExcel().setStartColumn(1);
					exportExcelService.getModelToExcel().setRowHeight(500);//设置行高			
					excelFile = exportExcelService.getExcelFile();//获取导出的文件
					 
				
					
					logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
					downloadFile(response, "外聘老师学位,学历统计表"+".xls", excelFile.getAbsolutePath(),true);
				}catch (Exception e) {
					e.printStackTrace();
				}
		}else{
			String accountStatus = null;
			if(statStudentStatus.contains("a")){
				accountStatus="1";
				statStudentStatus="11";//去掉学籍中有关帐号状态的标记
			}else if(statStudentStatus.contains("b")){
				accountStatus="0";
				statStudentStatus="11";//去掉学籍中有关帐号状态的标记
			}
			//	创建查询条件	
			HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
			map_condition.put("statType", param);
			List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
			if(!"".equals(statStudentStatus)){
				map_condition.put("statStudentStatus", statStudentStatus);
			}
			if(!"".equals(statStuBranchSchool)){
				map_condition.put("statStuBranchSchool", statStuBranchSchool);		
			}
			if(!"".equals(statStuGrade)){
				map_condition.put("statStuGrade", statStuGrade);
			}
			if(!"".equals(statStuClassic)){
				map_condition.put("statStuClassic", statStuClassic);
			}
			if(!"".equals(statStuMajor)){
				map_condition.put("statStuMajor", statStuMajor);
			}
			if(!"".equals(statStuStudyMode)){
				map_condition.put("statStuStudyMode", statStuStudyMode);
		}
		if(null!=accountStatus){
			map_condition.put("accountStatus", accountStatus);
		}
		//因为要计算年龄,传入当前时间
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		map_condition.put("current_date", df.format(new Date()));
		//执行查询语句
		resultList= studentInfoChangeJDBCService.statStudentInfo(map_condition);
		if("6".equals(param)){//按年龄统计人数
			List<Map<String,Object>> tmpAddList = new ArrayList<Map<String,Object>>(0);
			List<Map<String,Object>> tmpFinalList = new ArrayList<Map<String,Object>>(0);
			for (Map<String, Object> map : resultList) {
				if("硕士 男".equals(map.get("统计项目")+"")){
					map.put("统计项目", "硕士生");
					for (Map<String, Object> map_inner : resultList) {
						if("硕士 女".equals(map_inner.get("统计项目")+"")){
							map = mapAdd(map,map_inner);
						}
					}
				}
				if("本科 男".equals(map.get("统计项目")+"")){
					map.put("统计项目", "本科生");
					for (Map<String, Object> map_inner : resultList) {
						if("本科 女".equals(map_inner.get("统计项目")+"")){
							map = mapAdd(map,map_inner);
						}
					}
				}
				if("专科 男".equals(map.get("统计项目")+"")){
					map.put("统计项目", "专科生");
					for (Map<String, Object> map_inner : resultList) {
						if("专科 女".equals(map_inner.get("统计项目")+"")){
							map = mapAdd(map,map_inner);
						}
					}
				}
				tmpAddList.add(map);
			}
			for (Map<String, Object> map : tmpAddList) {
				if("硕士生".equals(map.get("统计项目")+"")){
					tmpFinalList.add(map);
					for (Map<String, Object> map_inner : resultList) {
						if("硕士 女".equals(map_inner.get("统计项目")+"")){
							map_inner.put("统计项目", "    其中:女");
							tmpFinalList.add(map_inner);
						}
					}
				}
				if("本科生".equals(map.get("统计项目")+"")){
					tmpFinalList.add(map);
					for (Map<String, Object> map_inner : resultList) {
						if("本科 女".equals(map_inner.get("统计项目")+"")){
							map_inner.put("统计项目", "    其中:女");
							tmpFinalList.add(map_inner);
						}
					}
				}
				if("专科生".equals(map.get("统计项目")+"")){
					tmpFinalList.add(map);
					for (Map<String, Object> map_inner : resultList) {
						if("专科 女".equals(map_inner.get("统计项目")+"")){
							map_inner.put("统计项目", "    其中:女");
							tmpFinalList.add(map_inner);
						}
					}
				}
			}
			resultList=tmpFinalList;
			
		}
		//转为Bean形式
		List<StudentInfoVo> results = new ArrayList<StudentInfoVo>(0);
		int[] total = new int[37];
		StudentInfoVo totalVo = new StudentInfoVo();
		if(resultList!=null){
			for (Map<String,Object> tmpUnit : resultList) {
				StudentInfoVo infoVo = new StudentInfoVo();
				if("6".equals(param)){
					boolean isAdd = false;
					if(tmpUnit.containsKey("统计项目")){infoVo.setAccountStatus(ExStringUtils.trimToEmpty((String)tmpUnit.get("统计项目")));
						if(!ExStringUtils.trimToEmpty((String)tmpUnit.get("统计项目")).contains("女")){
							isAdd = true;
						}
					}
					if(tmpUnit.containsKey("17岁以下")){infoVo.setBloodType(ExStringUtils.trimToEmpty(tmpUnit.get("17岁以下").toString()));if(isAdd) {
						total[0]+=Integer.parseInt(tmpUnit.get("17岁以下").toString());
					}
					totalVo.setBloodType(String.valueOf(total[0]));} 
					if(tmpUnit.containsKey("17岁")) {infoVo.setBornAddress(ExStringUtils.trimToEmpty(tmpUnit.get("17岁").toString()));if(isAdd) {
						total[1]+=Integer.parseInt(tmpUnit.get("17岁").toString());
					}
					totalVo.setBornAddress(String.valueOf(total[1]));}
					if(tmpUnit.containsKey("18岁")){infoVo.setBornDay(ExStringUtils.trimToEmpty(tmpUnit.get("18岁").toString()));if(isAdd) {
						total[2]+=Integer.parseInt(tmpUnit.get("18岁").toString());
					}
					totalVo.setBornDay(String.valueOf(total[2]));}
					if(tmpUnit.containsKey("19岁")){infoVo.setCertNum(ExStringUtils.trimToEmpty(tmpUnit.get("19岁").toString())); if(isAdd) {
						total[3]+=Integer.parseInt(tmpUnit.get("19岁").toString());
					}
					totalVo.setCertNum(String.valueOf(total[3]));}
					if(tmpUnit.containsKey("20岁")) {infoVo.setCertType(ExStringUtils.trimToEmpty(tmpUnit.get("20岁").toString()));if(isAdd) {
						total[4]+=Integer.parseInt(tmpUnit.get("20岁").toString());
					}
					totalVo.setCertType(String.valueOf(total[4]));}
					if(tmpUnit.containsKey("21岁")) {infoVo.setClassic(ExStringUtils.trimToEmpty(tmpUnit.get("21岁").toString()));if(isAdd) {
						total[5]+=Integer.parseInt(tmpUnit.get("21岁").toString());
					}
					totalVo.setClassic(String.valueOf(total[5]));}
					if(tmpUnit.containsKey("22岁")) {infoVo.setContactAddress(ExStringUtils.trimToEmpty(tmpUnit.get("22岁").toString()));if(isAdd) {
						total[6]+=Integer.parseInt(tmpUnit.get("22岁").toString());
					}
					totalVo.setContactAddress(String.valueOf(total[6]));}
					if(tmpUnit.containsKey("23岁")) {infoVo.setContactPhone(ExStringUtils.trimToEmpty(tmpUnit.get("23岁").toString()));if(isAdd) {
						total[7]+=Integer.parseInt(tmpUnit.get("23岁").toString());
					}
					totalVo.setContactPhone(String.valueOf(total[7]));}
					if(tmpUnit.containsKey("24岁")) {infoVo.setContactZipcode(ExStringUtils.trimToEmpty(tmpUnit.get("24岁").toString()));if(isAdd) {
						total[8]+=Integer.parseInt(tmpUnit.get("24岁").toString());
					}
					totalVo.setContactZipcode(String.valueOf(total[8]));}
					if(tmpUnit.containsKey("25岁")) {infoVo.setCountry(ExStringUtils.trimToEmpty(tmpUnit.get("25岁").toString()));if(isAdd) {
						total[9]+=Integer.parseInt(tmpUnit.get("25岁").toString());
					}
					totalVo.setCountry(String.valueOf(total[9]));}
					if(tmpUnit.containsKey("26岁")) {infoVo.setCurrenAddress(ExStringUtils.trimToEmpty(tmpUnit.get("26岁").toString()));if(isAdd) {
						total[10]+=Integer.parseInt(tmpUnit.get("26岁").toString());
					}
					totalVo.setCurrenAddress(String.valueOf(total[10]));}
					if(tmpUnit.containsKey("27岁")) {infoVo.setDegree(ExStringUtils.trimToEmpty(tmpUnit.get("27岁").toString()));if(isAdd) {
						total[11]+=Integer.parseInt(tmpUnit.get("27岁").toString());
					}
					totalVo.setDegree(String.valueOf(total[11]));}
					if(tmpUnit.containsKey("28岁")) {infoVo.setEmail(ExStringUtils.trimToEmpty(tmpUnit.get("28岁").toString()));if(isAdd) {
						total[12]+=Integer.parseInt(tmpUnit.get("28岁").toString());
					}
					totalVo.setEmail(String.valueOf(total[12]));}
					if(tmpUnit.containsKey("29岁")) {infoVo.setEnterAttr(ExStringUtils.trimToEmpty(tmpUnit.get("29岁").toString()));if(isAdd) {
						total[13]+=Integer.parseInt(tmpUnit.get("29岁").toString());
					}
					totalVo.setEnterAttr(String.valueOf(total[13]));}
					if(tmpUnit.containsKey("30岁")) {infoVo.setEnterSchool(ExStringUtils.trimToEmpty(tmpUnit.get("30岁").toString()));if(isAdd) {
						total[14]+=Integer.parseInt(tmpUnit.get("30岁").toString());
					}
					totalVo.setEnterSchool(String.valueOf(total[14]));}
					if(tmpUnit.containsKey("30岁以上")) {infoVo.setExamNo(ExStringUtils.trimToEmpty(tmpUnit.get("30岁以上").toString()));if(isAdd) {
						total[15]+=Integer.parseInt(tmpUnit.get("30岁以上").toString());
					}
					totalVo.setExamNo(String.valueOf(total[15]));
					}
				}else{
					if(tmpUnit.containsKey("total")){infoVo.setAccountStatus(tmpUnit.get("total").toString());total[16]+=Integer.parseInt(tmpUnit.get("total").toString());
					totalVo.setAccountStatus(String.valueOf(total[16]));}
					if(tmpUnit.containsKey("man")){infoVo.setBloodType(tmpUnit.get("man").toString());total[17]+=Integer.parseInt(tmpUnit.get("man").toString());
					totalVo.setBloodType(String.valueOf(total[17]));}
					if(tmpUnit.containsKey("lady")){infoVo.setBornAddress(tmpUnit.get("lady").toString());total[18]+=Integer.parseInt(tmpUnit.get("lady").toString());
					totalVo.setBornAddress(String.valueOf(total[18]));}
					if(tmpUnit.containsKey("本科生")){infoVo.setBornDay(tmpUnit.get("本科生").toString());total[19]+=Integer.parseInt(tmpUnit.get("本科生").toString());
					totalVo.setBornDay(String.valueOf(total[19]));}
					if(tmpUnit.containsKey("专科生")){infoVo.setCertNum(tmpUnit.get("专科生").toString());total[20]+=Integer.parseInt(tmpUnit.get("专科生").toString());
					totalVo.setCertNum(String.valueOf(total[20]));}
					if(tmpUnit.containsKey("纯网")){infoVo.setCertType(tmpUnit.get("纯网").toString());total[21]+=Integer.parseInt(tmpUnit.get("纯网").toString());
					totalVo.setCertType(String.valueOf(total[21]));}
					if(tmpUnit.containsKey("网成")){infoVo.setContactAddress(tmpUnit.get("网成").toString());total[22]+=Integer.parseInt(tmpUnit.get("网成").toString());
					totalVo.setContactAddress(String.valueOf(total[22]));}
					if(tmpUnit.containsKey("共产党员")){infoVo.setContactPhone(tmpUnit.get("共产党员").toString());total[23]+=Integer.parseInt(tmpUnit.get("共产党员").toString());
					totalVo.setContactPhone(String.valueOf(total[23]));}
					if(tmpUnit.containsKey("共青团员")){infoVo.setContactZipcode(tmpUnit.get("共青团员").toString());total[24]+=Integer.parseInt(tmpUnit.get("共青团员").toString());
					totalVo.setContactZipcode(String.valueOf(total[24]));}
					if(tmpUnit.containsKey("民主党派")){infoVo.setCountry(tmpUnit.get("民主党派").toString());total[25]+=Integer.parseInt(tmpUnit.get("民主党派").toString());
					totalVo.setCountry(String.valueOf(total[25]));}
					if(tmpUnit.containsKey("华侨")){infoVo.setCurrenAddress(tmpUnit.get("华侨").toString());total[26]+=Integer.parseInt(tmpUnit.get("华侨").toString());
					totalVo.setCurrenAddress(String.valueOf(total[26]));}
					if(tmpUnit.containsKey("港澳台")){infoVo.setDegree(tmpUnit.get("港澳台").toString());total[27]+=Integer.parseInt(tmpUnit.get("港澳台").toString());
					totalVo.setDegree(String.valueOf(total[27]));}
					if(tmpUnit.containsKey("少数民族")){infoVo.setEmail(tmpUnit.get("少数民族").toString());total[28]+=Integer.parseInt(tmpUnit.get("少数民族").toString());
					totalVo.setEmail(String.valueOf(total[28]));}
					if(tmpUnit.containsKey("残疾人")){infoVo.setEnterAttr(tmpUnit.get("残疾人").toString());total[29]+=Integer.parseInt(tmpUnit.get("残疾人").toString());
					totalVo.setEnterAttr(String.valueOf(total[29]));}
					if(tmpUnit.containsKey("正常注册")){infoVo.setEnterSchool(tmpUnit.get("正常注册").toString());total[30]+=Integer.parseInt(tmpUnit.get("正常注册").toString());
					totalVo.setEnterSchool(String.valueOf(total[30]));}
					if(tmpUnit.containsKey("正常未注册")){infoVo.setExamNo(tmpUnit.get("正常未注册").toString());total[31]+=Integer.parseInt(tmpUnit.get("正常未注册").toString());
					totalVo.setExamNo(String.valueOf(total[31]));}
					if(tmpUnit.containsKey("过期")){infoVo.setFaith(tmpUnit.get("过期").toString());total[32]+=Integer.parseInt(tmpUnit.get("过期").toString());
					totalVo.setFaith(String.valueOf(total[32]));}
					if(tmpUnit.containsKey("延期")){infoVo.setGaqCode(tmpUnit.get("延期").toString());total[33]+=Integer.parseInt(tmpUnit.get("延期").toString());
					totalVo.setGaqCode(String.valueOf(total[33]));}
					if(tmpUnit.containsKey("自动流失")){infoVo.setGender(tmpUnit.get("自动流失").toString());total[34]+=Integer.parseInt(tmpUnit.get("自动流失").toString());
					totalVo.setGender(String.valueOf(total[34]));}
					if(tmpUnit.containsKey("开除学籍")){infoVo.setHealth(tmpUnit.get("开除学籍").toString());total[35]+=Integer.parseInt(tmpUnit.get("开除学籍").toString());
					totalVo.setHealth(String.valueOf(total[35]));}
					if(tmpUnit.containsKey("退学")){infoVo.setHeight(tmpUnit.get("退学").toString());total[36]+=Integer.parseInt(tmpUnit.get("退学").toString());
					totalVo.setHeight(String.valueOf(total[36]));}
					if(tmpUnit.containsKey("classicname")){infoVo.setClassic(ExStringUtils.trimToEmpty((String)tmpUnit.get("classicname")));}
					if(tmpUnit.containsKey("unitname")){infoVo.setUnit(ExStringUtils.trimToEmpty((String)tmpUnit.get("unitname")));}
					if(tmpUnit.containsKey("gradename")){infoVo.setGrade(ExStringUtils.trimToEmpty((String)tmpUnit.get("gradename")));}
					if(tmpUnit.containsKey("majorname")){infoVo.setMajor(ExStringUtils.trimToEmpty((String)tmpUnit.get("majorname")));}
					if(tmpUnit.containsKey("teachingtype")){infoVo.setTeachingType(ExStringUtils.trimToEmpty(JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", (String)tmpUnit.get("teachingtype"))));}
					if(tmpUnit.containsKey("homePlace")){infoVo.setHomePlace(ExStringUtils.trimToEmpty((String)tmpUnit.get("homePlace")));}
				}
				results.add(infoVo);
			}
		}
		
		List<StudentInfoVo> tmp=results;
		//合计列
		switch(Integer.parseInt(param)){
		case 1:case 3:case 4:  totalVo.setGrade("合计");results.add(totalVo);break;
		case 2:case 5:case 9:  totalVo.setUnit("合计");results.add(totalVo);break;
		case 6:totalVo.setAccountStatus("合计"); results=new ArrayList<StudentInfoVo>(0);results.add(totalVo);results.addAll(tmp); break;
		case 7:totalVo.setHomePlace("合计")    ; results=new ArrayList<StudentInfoVo>(0);results.add(totalVo);results.addAll(tmp); break;
		case 8:totalVo.setClassic("合计")      ; results=new ArrayList<StudentInfoVo>(0);results.add(totalVo);results.addAll(tmp); break;
		
		default:break;
		}
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			List<String> filterColumnList = new ArrayList<String>();
			String statType = "";
			switch(Integer.valueOf(param)){
			case 1:
				filterColumnList.add("grade"); 
				filterColumnList.add("classic");
				filterColumnList.add("accountStatus");
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress");
				statType = "按年级层次进行统计——";
				break;
			case 2:
				filterColumnList.add("unit"); 
				filterColumnList.add("grade"); 
				filterColumnList.add("classic");
				filterColumnList.add("major"); 
				filterColumnList.add("accountStatus"); 
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress"); 
				filterColumnList.add("certType"); 
				filterColumnList.add("contactAddress");
				statType = "按学习中心年级层次专业进行统计——";
				break;
			case 3:
				filterColumnList.add("grade"); 
				filterColumnList.add("major"); 
				filterColumnList.add("classic");
				filterColumnList.add("accountStatus"); 
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress"); 
				filterColumnList.add("certType"); 
				filterColumnList.add("contactAddress");
				statType = "按年级专业层次进行统计——";
				break;
			case 4:
				filterColumnList.add("grade"); 
				filterColumnList.add("teachingType"); 
				filterColumnList.add("accountStatus"); 
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress");
				statType = "按年级学习模式进行统计——";
				break;
			case 5:  
				filterColumnList.add("unit"); 
				filterColumnList.add("grade"); 
				filterColumnList.add("accountStatus"); 
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress");
				statType = "按学习中心年级进行统计——";
				break;
			case 7:
				filterColumnList.add("homePlace"); 
				filterColumnList.add("certNum"); 
				filterColumnList.add("bornDay");
				statType = "按在校生来源情况进行统计——";
				break;
			case 8:
				filterColumnList.add("classic");
				filterColumnList.add("contactPhone"); 
				filterColumnList.add("contactZipcode"); 
				filterColumnList.add("country"); 
				filterColumnList.add("currenAddress"); 
				filterColumnList.add("degree"); 
				filterColumnList.add("email"); 
				filterColumnList.add("enterAttr");
				statType = "按政治面貌证件名称民族进行统计——";
				break;
			case 6:
				filterColumnList.add("accountStatus"); 
				filterColumnList.add("bloodType");
				filterColumnList.add("bornAddress");
				filterColumnList.add("bornDay"); 
				filterColumnList.add("certNum"); 
				filterColumnList.add("certType"); 
				filterColumnList.add("classic"); 
				filterColumnList.add("contactAddress");
				filterColumnList.add("contactPhone"); 
				filterColumnList.add("contactZipcode"); 
				filterColumnList.add("country"); 
				filterColumnList.add("currenAddress"); 
				filterColumnList.add("degree"); 
				filterColumnList.add("email"); 
				filterColumnList.add("enterAttr");
				filterColumnList.add("enterSchool"); 
				filterColumnList.add("examNo");       
				statType = "按在校生按年龄进行统计——";
				break;
			case 9:
				filterColumnList.add("unit");
				filterColumnList.add("grade");
				filterColumnList.add("enterSchool");
				filterColumnList.add("examNo");
				filterColumnList.add("faith");
				filterColumnList.add("gaqCode");
				filterColumnList.add("gender");
				filterColumnList.add("health");
				filterColumnList.add("height");
				statType = "按学籍状态进行统计——";
			case 10:
				filterColumnList.add("unit"); 
				filterColumnList.add("accountStatus");
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress");
				//statType = "按教学站进行统计——";
				break;
			case 11:
				filterColumnList.add("unit");
				filterColumnList.add("classic");
				filterColumnList.add("accountStatus");
				filterColumnList.add("bloodType"); 
				filterColumnList.add("bornAddress");
				//statType = "按教学站层次进行统计——";
				break;
			default: break;
			}
			statType=statType+"学籍信息统计导出表";
			//初始化参数
			if("6".equals(param)){
				exportExcelService.initParmasByfile(disFile, "StudentInfoStat2",results,null,filterColumnList);//初始化配置参数
			}else{
				exportExcelService.initParmasByfile(disFile, "StudentInfoStat1",results,null,filterColumnList);//初始化配置参数
			}
			exportExcelService.getModelToExcel().setHeader(statType);//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, statType+".xls", excelFile.getAbsolutePath(),true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		}
	}
	/**
	 * 修改学习中心更新专业 班级范围
	 * @param request
	 * @param response
	 * @throws WebException
	 */
/*	@RequestMapping("/edu3/register/studentinfo/brschool_Major.html")
	public void brschool_Major(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(0);
		Map<String,Object> condition = new HashMap<String, Object>(0);
		String majorId = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String unitId  = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String gradeId   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		
		String flag      = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			unitId = user.getOrgUnit().getResourceid();
		}
		
		if(ExStringUtils.isNotEmpty(unitId)){
			condition.put("unitId", unitId);
		}
		
		List<Map<String,Object>> teachingPlanMajors = teachingplanservice.getUnitTeachingPlanMajor(condition);
		if(ExStringUtils.isNotEmpty(majorId)){
			condition.put("majorId", majorId);
		}
		if(ExStringUtils.isNotEmpty(classicId)){
			condition.put("classicId", classicId);
		}
		if(ExStringUtils.isNotEmpty(gradeId)){
			condition.put("gradeId", gradeId);
		}
		
		if(user.isContainRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.master").getParamValue())
				&&flag.equals("master")){
			condition.put("classesMasterId", user.getResourceid());
		}
		
		
		List<Map<String,Object>> classes	        = classesService.getClassesByCondition(condition);
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		
		for (Map<String,Object> m : teachingPlanMajors) {
			if(ExStringUtils.isNotEmpty(majorId)&&majorId.equals(m.get("resourceid"))){
				majorOption.append("<option value ='"+m.get("resourceid")+"' selected='selected' >"+m.get("majorinfo")+"</option>");
			}else{
				majorOption.append("<option value ='"+m.get("resourceid")+"'>"+m.get("majorinfo")+"</option>");
			}
			
		}
		StringBuffer classesOption = new StringBuffer("<option value=''></option>");
		for(Map<String,Object> c : classes){
			if(ExStringUtils.isNotEmpty(classesId)&&classesId.equals(c.get("resourceid"))){
				classesOption.append("<option value ='"+c.get("resourceid")+"' selected='selected' >"+c.get("classesname")+"</option>");
			}else{
				classesOption.append("<option value ='"+c.get("resourceid")+"'>"+c.get("classesname")+"</option>");
			}
		}
		
		
		
		map.put("majorOption", majorOption);
		map.put("classesOption",classesOption);
		renderJson(response, JsonUtils.mapToJson(map));
	}*/
	
	/**
	 * 批量修改学籍状态历史查看
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	
	@RequestMapping("/edu3/register/studentinfo/getBatchStudentInfoStatusChangeHistory.html")
	public String getBatchStudentInfoStatusChangeHistory(HttpServletRequest request ,HttpServletResponse response,ModelMap model,Page objPage) throws WebException{
		String certnum  	 = ExStringUtils.trimToEmpty(request.getParameter("certnum"));
		String studentname   = ExStringUtils.trimToEmpty(request.getParameter("studentname"));
		String studyno    	 = ExStringUtils.trimToEmpty(request.getParameter("studyno"));
		String studentstatus = ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
		String gradeid       = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String operatedateb  = ExStringUtils.trimToEmpty(request.getParameter("operatedateb"));
		String operatedatee  = ExStringUtils.trimToEmpty(request.getParameter("operatedatee"));
		
		
		Map<String,Object> condition = new HashMap<String,Object>(0);
		if(ExStringUtils.isNotEmpty(certnum)){
			condition.put("certnum", certnum);
		}
		if(ExStringUtils.isNotEmpty(studentname)){
			condition.put("studentname", studentname);
		}
		if(ExStringUtils.isNotEmpty(studyno)){
			condition.put("studyno", studyno);
		}
		if(ExStringUtils.isNotEmpty(studentstatus)){
			condition.put("studentstatus", studentstatus);
		}
		if(ExStringUtils.isNotEmpty(gradeid)){
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(operatedateb)){
			condition.put("operatedateb", operatedateb);
		}
		if(ExStringUtils.isNotEmpty(operatedatee)){
			condition.put("operatedatee", operatedatee);
		}
		Page page = studentInfoChangeJDBCService.getStudentInfoStatusChangeHistory(condition,objPage);
		model.addAttribute("condition", condition);
		model.addAttribute("studentInfoStatusHis",page);
		return "/edu3/roll/sturegister/getBatchStudentInfoStatusChangeHistory";
	} 
	
	private String fmtTime(Long time){
		String sDateTime = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    	java.util.Date dt = new Date(time);  
	    	sDateTime = sdf.format(dt);  //得到精确到秒的表示：08/31/2006 21:08:00
		} catch (Exception e) {
		}
    	return sDateTime;
	}
	
	/**
	 * 学籍信息维护历史导出
	 * @param request
	 * @param response
	 * @param modelMap
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/exportBatchStudentInfoStatusChangeHistory.html")
	public void exportBatchStudentInfoStatusChangeHistory(HttpServletRequest request,HttpServletResponse response,ModelMap modelMap,Page objPage){
		String certnum  	 = ExStringUtils.trimToEmpty(request.getParameter("certnum"));
		String studentname   = ExStringUtils.trimToEmpty(request.getParameter("studentname"));
		String studyno    	 = ExStringUtils.trimToEmpty(request.getParameter("studyno"));
		String studentstatus = ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
		String gradeid       = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String operatedateb  = ExStringUtils.trimToEmpty(request.getParameter("operatedateb"));
		String operatedatee  = ExStringUtils.trimToEmpty(request.getParameter("operatedatee"));
		//条件map
		HashMap<String,Object> map_condition = new HashMap<String,Object>(0);
		//结果集map
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>(0);
		List<StudentInfoVo> finalList = new ArrayList<StudentInfoVo>(0);
		if(!"".equals(certnum)){
			map_condition.put("certnum", certnum);
		}
		if(!"".equals(studentname)){
			map_condition.put("studentname", studentname);		
		}
		if(!"".equals(studyno)){
			map_condition.put("studyno", studyno);
		}
		
		if(ExStringUtils.isNotEmpty(gradeid)){
			map_condition.put("gradeid",gradeid );
		}
		if(ExStringUtils.isNotEmpty(studentstatus)){
			map_condition.put("studentstatus",studentstatus );		
		}
		if(ExStringUtils.isNotEmpty(operatedateb)){
			map_condition.put("operatedateb",operatedateb );
		}
		if(ExStringUtils.isNotEmpty(operatedatee)){
			map_condition.put("operatedatee",operatedatee );
		}
		resultList = studentInfoChangeJDBCService.getStudentInfoStatusChangeHistoryList(map_condition);
		for (Map<String,Object> map : resultList) {
			StudentInfoVo siv = new StudentInfoVo();
			siv.setBloodType(null==map.get("studyno")?"":map.get("studyno").toString());
			siv.setAccountStatus(null==map.get("name")?"":map.get("name").toString());
			siv.setBornAddress(null==map.get("certnum")?"":map.get("certnum").toString());
			siv.setEmail(null==map.get("gradename")?"":map.get("gradename").toString());
			siv.setCurrenAddress(null==map.get("majorname")?"":map.get("majorname").toString());
			siv.setDegree(null==map.get("classicname")?"":map.get("classicname").toString());
			siv.setDiplomaNum(null==map.get("classesname")?"":map.get("classesname").toString());
			siv.setEduYear(null==map.get("unitname")?"":map.get("unitname").toString());
			siv.setCertType(null==map.get("beforestatus")?"":map.get("beforestatus").toString());
			siv.setCountry(null==map.get("afterstatus")?"":("延期".equals(map.get("afterstatus").toString())?"延期 延期至:"+(null==map.get("delaydate")?"":map.get("delaydate").toString()):map.get("afterstatus").toString()));
			siv.setContactZipcode(null==map.get("auditdate")?"":map.get("auditdate").toString());
			siv.setClassic(null==map.get("auditman")?"":map.get("auditman").toString());
			siv.setContactAddress(null==map.get("memo")?"":map.get("memo").toString());
			
			finalList.add(siv);
		}
		 
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			exportExcelService.initParmasByfile(disFile, "StudentInfoChangeStudentStatusHis",finalList,null);//初始化配置参数——学籍状态
		
			exportExcelService.getModelToExcel().setHeader("批量设置学生学籍状态历史");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "批量设置学生学籍状态历史"+".xls", excelFile.getAbsolutePath(),true);
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
	/**
	 * 入学资格状态-表单
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/recruitStatusImport/form.html")
	public String importNoExamApplyForm(ModelMap model){
		User curUser       = SpringSecurityHelper.getCurrentUser();
		List<Attachs> list = attachsService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("formId",curUser.getResourceid()),Restrictions.eq("formType","recruitStatusUpload"));
		model.addAttribute("user",curUser );
		model.addAttribute("attachsList", list);
		return "/edu3/roll/sturegister/recruitStatus-upload-view";
	}
	/**
	 * 导入入学资格状态-保存
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/register/studentinfo/recruitStatusImport/upload.html")
	public void importNoExamApply(HttpServletRequest request,HttpServletResponse response){
		
		Map<String,Object> map      = new HashMap<String, Object>();
		String attId 				= ExStringUtils.trimToEmpty(request.getParameter("attachId"));
		String msg                  = "";
		try {
			
			 Attachs attach  		= attachsService.get(attId);
			 File excel          	= attach.getFile();
			 
			 importExcelService.initParmas(excel,"recruitStatus",map);
			 importExcelService.getExcelToModel().setSheet(0);
			 List<StudentInfoVo> list= (List<StudentInfoVo>)importExcelService.getModelList();
			 List<String> studentInfoIds = new ArrayList<String>(0);
			 Dictionary dictionary = CacheAppManager.getDictionaryByCode("CodeAuditStatus");
			 List<Dictionary> list_dict = CacheAppManager.getChildren("CodeAuditStatus");
			 Map<String,String> map_dict = new HashMap<String, String>(0);
			 String legalStr = "";
			 if (null!=dictionary&&null!=list_dict&&!list_dict.isEmpty()) {
				 for (Dictionary dict : list_dict) {
					 map_dict.put(dict.getDictName(),dict.getDictValue() );
					 legalStr += "["+dict.getDictName()+"]";
				 }
				
			 }
			 int row = 0;
			 //遍历每个导入的对象，待更改的数据的合法情况
			 for (StudentInfoVo studentInfoVo : list) {
				row++;
				Map<String,Object> condition = new HashMap<String, Object>(0);
				String enrolleeCode = studentInfoVo.getStudentNo();
				String name   = studentInfoVo.getName();
				String certNum = studentInfoVo.getCertNum();
				String major  = studentInfoVo.getMajor();
				String enterAuditStatus = studentInfoVo.getEnterSchool();
				if(ExStringUtils.isNotEmpty(enrolleeCode)&&ExStringUtils.isNotEmpty(name)&&ExStringUtils.isNotEmpty(certNum)&&ExStringUtils.isNotEmpty(major)&&ExStringUtils.isNotEmpty(enterAuditStatus)){
					condition.put("enrolleeCode", enrolleeCode);
					condition.put("name", name);
					condition.put("certNum", certNum);
					condition.put("major", major);
				}else{
					msg += "<font color='red'>第"+row+"行:"+(ExStringUtils.isEmpty(enrolleeCode)?"空考生号":enrolleeCode)+"_"+(ExStringUtils.isEmpty(name)?"空姓名":name)+"导入信息不完整</font><br>";
					continue;
				}
				if(!map_dict.containsKey(enterAuditStatus)){
					msg += "<font color='red'>第"+row+"行:"+enrolleeCode+"_"+name+"导入表格的入学状态不合要求，请输入"+legalStr+"</font><br>";
					continue;
				}
				List<Map<String,Object>> studentids = studentInfoChangeJDBCService.getStudentInfoWithEnrolleeCodeEct(condition);
				if(studentids.size()<1){
					msg += "<font color='red'>第"+row+"行:"+enrolleeCode+"_"+name+"数据不匹配</font><br>";
					continue;
				}else{
					String id = studentids.get(0).get("resourceid").toString();
					StudentInfo si = studentInfoService.get(id);
					si.setEnterAuditStatus(map_dict.get(enterAuditStatus));
					studentInfoService.saveOrUpdate(si);
					msg += "<font color='blue'>第"+row+"行:"+enrolleeCode+"_"+name+"成功导入</font><br>";
				}
				
			 }
			 map.put("msg", "导入完毕<br>"+msg);
			 map.put("success",true);
			 if ((Boolean)map.get("success")==true) {
				 if (excel.exists()) {
					 excel.delete();
				 }
				 attachsService.delete(attach);
			 }
		} catch (Exception e) {
			msg                     = "导入入学资格状态申请出错:{}"+e.fillInStackTrace();
			logger.error(msg);
			map.put("success", false);
			map.put("msg", msg);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 学生信息打印预览
	 * @param studentId
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/exameeinfo/printview.html")
	public String printviewExameeInfo(String studentId, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("studentId",ExStringUtils.trimToEmpty(studentId));
		String unitId 		 		= ExStringUtils.trimToEmpty(request.getParameter("unitId"));//学习中心
		String majorId 				= ExStringUtils.trimToEmpty(request.getParameter("majorId"));//专业
		String classicId 			= ExStringUtils.trimToEmpty(request.getParameter("classicId"));//层次
		String stuStatus			= ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
		String entranceFlag      	= ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
		String accountStatus 		= ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		String name 				= ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo  = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String stuGrade 			= ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String certNum 				= ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesId 			= ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String rollCard             = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		String from             	= ExStringUtils.trimToEmpty(request.getParameter("from"));//判断来自学籍库还是学籍信息
		model.addAttribute("unitId",unitId);
		model.addAttribute("majorId",majorId);
		model.addAttribute("classicId",classicId);
		model.addAttribute("stuStatus",stuStatus);
		model.addAttribute("entranceFlag",entranceFlag);
		model.addAttribute("accountStatus",accountStatus);
		model.addAttribute("name",name);
		model.addAttribute("matriculateNoticeNo",matriculateNoticeNo);
		model.addAttribute("stuGrade",stuGrade);
		model.addAttribute("certNum",certNum);
		model.addAttribute("classesId",classesId);
		model.addAttribute("rollCard",rollCard);
		model.addAttribute("from",from);
		return "/edu3/roll/sturegister/exameeinfo-printview-inrollpage";
	}
	/**
	 * 打印考生信息表 学籍区
	 * @param studentId
	 * @param request
	 * @param response
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/register/studentinfo/exameeinfo/print.html")
	public void printExameeInfo(String studentId,HttpServletRequest request,HttpServletResponse response){		
		Map<String, Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint   = null;//输出的报表
		String [] ids = ExStringUtils.trimToEmpty(studentId).split("\\,");
		try {
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			param.put("schoolname", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());
			String jasperPath = "exameeInfoPrint.jasper";
			if ("12962".equals(schoolCode)) {//广外艺
				jasperPath = "exameeInfoPrint_wyys.jasper";
			}
			if(ArrayUtils.isNotEmpty(ids)&&ExStringUtils.isNotEmpty(studentId)){
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						  File.separator+"recruit"+File.separator+jasperPath),"utf-8");
				param.put("basicPath",ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath")+"common"+File.separator+"students");

				List<StudentInfo> studentInfoList = studentInfoService.findByCriteria(StudentInfo.class, new Criterion[]{Restrictions.in("resourceid", Arrays.asList(ids))});
				StringBuffer enrolleeCodes = new StringBuffer();
				// 各个学校(ksh和zkzh)不同要使用全局参数来区分
				String uniqueFlag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
				for (StudentInfo studentInfo : studentInfoList) {
					if (enrolleeCodes.length() != 0) {
						enrolleeCodes.append(",");
					}
					if(ExStringUtils.isNotEmpty(uniqueFlag) && "0".equals(uniqueFlag)){// 准考证号
						enrolleeCodes.append("'").append(studentInfo.getExamCertificateNo()).append("'");
					} else {
						enrolleeCodes.append("'").append(studentInfo.getEnrolleeCode()).append("'");
					}
				}

				// 默认是ksh为长号
				String exameeInfoHql = " from "+ExameeInfo.class.getSimpleName()+" where isDeleted=0 and KSH in ("+enrolleeCodes.toString()+") ";
				// zkzh为长号
				if(ExStringUtils.isNotEmpty(uniqueFlag) && "0".equals(uniqueFlag)){
					exameeInfoHql = " from "+ExameeInfo.class.getSimpleName()+" where isDeleted=0 and ZKZH in ("+enrolleeCodes.toString()+") ";
				}
				List<ExameeInfo> exameeInfoList = exameeInfoService.findByHql(exameeInfoHql);

//				List<String> studyNos = new ArrayList<String>(0);
//				for (StudentInfo studentInfo : studentInfoList) {
//					studyNos.add(studentInfo.getStudyNo());
//				}
//				List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findByCriteria(EnrolleeInfo.class, new Criterion[]{Restrictions.in("matriculateNoticeNo", studyNos)});
//				List<ExameeInfo> exameeInfoList = new ArrayList<ExameeInfo>(0);
//				for (EnrolleeInfo ei : enrolleeInfoList) {
//					List<ExameeInfo> exameeInfoList_tmp = exameeInfoService.findByHql(" from "+ExameeInfo.class.getSimpleName()+" where isDeleted =0 and ZKZH='"+ei.getExamCertificateNo()+"' ");
//					exameeInfoList.addAll(exameeInfoList_tmp);
//				}
				List<Map<String, Object>> examInfoMapList = new ArrayList<Map<String,Object>>();
				for (ExameeInfo exameeInfo : exameeInfoList) {
					examInfoMapList.add(convertToMap(exameeInfo));	//转换为map
				}

				JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(examInfoMapList);
				jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
			}else{
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						  File.separator+"recruit"+File.separator+"exameeInfoPrint.jasper"),"utf-8");
				param.put("basicPath",ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath")+"common"+File.separator+"students");
				Map<String,Object> condition = new HashMap<String,Object>();

				String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));//学习中心
				String major 				 = ExStringUtils.trimToEmpty(request.getParameter("majorId"));//专业
				String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classicId"));//层次
				String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
				String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
				String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
				String name 				 = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
				String matriculateNoticeNo   = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
				String stuGrade 			 = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));
				String certNum 				 = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
				String classesid 			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
				String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
				String from             	 = ExStringUtils.trimToEmpty(request.getParameter("from"));
				if(stuStatus.contains("a")){
					accountStatus="1";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}else if(stuStatus.contains("b")){
					accountStatus="0";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}

				if(ExStringUtils.isNotEmpty(branchSchool)) {
					condition.put("branchSchool", branchSchool);
				}
				if(ExStringUtils.isNotEmpty(major)) {
					condition.put("major", major);
				}
				if(ExStringUtils.isNotEmpty(classic)) {
					condition.put("classic", classic);
				}
				if(ExStringUtils.isNotEmpty(stuStatus)) {
					condition.put("stuStatus", stuStatus);
				}
				if(ExStringUtils.isNotEmpty(name)) {
					condition.put("name", name);
				}
				if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
					condition.put("matriculateNoticeNo", matriculateNoticeNo);
				}
				if(ExStringUtils.isNotEmpty(stuGrade)) {
					condition.put("stuGrade", stuGrade);
				}
				if(ExStringUtils.isNotEmpty(certNum)) {
					condition.put("certNum", certNum);
				}
				if(ExStringUtils.isNotEmpty(entranceFlag)) {
					condition.put("entranceFlag", entranceFlag);
				}
				if(ExStringUtils.isNotEmpty(accountStatus)) {
					condition.put("accountStatus", accountStatus);
				}
				if(ExStringUtils.isNotEmpty(classesid)) {
					condition.put("classesid", classesid);
				}
				if(ExStringUtils.isNotEmpty(rollCard)) {
					condition.put("rollCard", rollCard);
				}
				if(ExStringUtils.isNotEmpty(from)) {
					condition.put("from", from);
				}
				String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
				if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
					condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
				}
				condition.put("enroll", "1");
				List<Map<String, Object>> result = studentInfoService.findStuByConditionByJDBC(condition);//信息表
				List<Map<String, Object>> examInfoMapList = new ArrayList<Map<String,Object>>();
				List<String> infoids = new ArrayList<String>(0);
				for (Map<String, Object> map : result) {
					infoids.add(map.get("resourceid").toString());
				}
				if(ExCollectionUtils.isNotEmpty(infoids)){
					List<ExameeInfo>exameeInfoList = exameeInfoService.findByCriteria(ExameeInfo.class, new Criterion[]{Restrictions.in("resourceid", infoids)});
					for (ExameeInfo exameeInfo : exameeInfoList) {
						examInfoMapList.add(convertToMap(exameeInfo));	//转换为map
					}
				}

				JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(examInfoMapList);
				jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
			}			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印报名信息出错:{}",e.fillInStackTrace());
		}
	}
	/**
	 * 考生信息转为map
	 * @param exameeInfo
	 * @return
	 */
	public Map<String, Object> convertToMap(ExameeInfo exameeInfo){
		Map<String, Object> map = ExBeanUtils.convertBeanToMap(exameeInfo);
		String[] dictCode = {"CodeForeignLanguage","CodeSex","CodeCharacteristic","CodeRecruitType","CodeNation","CodePolitics","CodeMajorAttribute","CodeEducationalLevel","CodeMajorCatogery","CodeTeachingType"};
		String[] dictField = {"WYYZDM","XBDM","KSTZBZ","ZSLBDM","MZDM","ZZMMDM","BKZYSXDM","WHCDDM","ZYLBDM","XXXSDM"};
		//backPhotoPath  
		//修改为 getBackPhotoPath() 之前使用的是getMainPhotoPath ，但是导入学生录取相片时，是导入到 backPhotoPath 这个属性的 20170414
		map.put("backPhotoPath", exameeInfo.getBackPhotoPath());
		//字典值转为字典名称
		for (int i = 0; i < dictCode.length; i++) {
			map.put(dictField[i], JstlCustomFunction.dictionaryCode2Value(dictCode[i], map.get(dictField[i])!=null?map.get(dictField[i]).toString():""));
		}
		String[] codeField = {"KSH","ZKZH","SFZH","YZBM"};
		//号码加上空格间隔
		for (String code : codeField) {
			map.put(code, map.get(code)!=null?ExStringUtils.join(map.get(code).toString().split("")," "):"");
		}
		//考生成绩
		int i = 1;
		for (ExameeInfoScore score : exameeInfo.getExameeInfoScores()) {
			//没有办法，一个表格只能显示15条考生的成绩信息，而实际上导数据时常常把31条的数据导入，故而只能将所有0分的成绩通通过滤。
			if(score.getScoreValue()==null || 0.0==score.getScoreValue()){
				continue;
			}
			map.put("CJXN"+i, "  "+JstlCustomFunction.dictionaryCode2Value("CodeExameeInfoScore", score.getScoreCode()));
			map.put("CJX"+i++, score.getScoreValue());
		}
		//考生志愿
		i = 1;
		for (ExameeInfoWish wish : exameeInfo.getExameeInfoWishs()) {
			map.put("ZYDH"+i++, "  "+wish.getZYBM()+" "+wish.getZYMC());
		}	
//		map.put("recruitPlanYear", exameeInfo.getRecruitPlan().getYearInfo().getFirstYear()+("2".equals(exameeInfo.getRecruitPlan().getTerm())?1L:0L));
		// TODO:目前只有广东医使用这个功能，等其他学校要使用再根据具体情况处理
		map.put("recruitPlanYear", exameeInfo.getRecruitPlan().getYearInfo().getFirstYear()+("2".equals(exameeInfo.getRecruitPlan().getTerm())?1L:0L)-1);
		return map;
	}
	
	/**
	 * 计算可打印考生表的数目
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/exameeinfo/calulatePrintNum.html")
	public void calulatePrintNum(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(0);
		String id = ExStringUtils.trimToEmpty(request.getParameter("id"));
		String question = "";
		if(ExStringUtils.isNotEmpty(id)){
			String [] studentId = ExStringUtils.trimToEmpty(id).split("\\,");
			List<StudentInfo> studentInfoList = studentInfoService.findByCriteria(StudentInfo.class, new Criterion[]{Restrictions.in("resourceid", Arrays.asList(studentId))});
			StringBuffer enrolleeCodes = new StringBuffer();
			// 各个学校(ksh和zkzh)不同要使用全局参数来区分
			String uniqueFlag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			for (StudentInfo studentInfo : studentInfoList) {
				if (enrolleeCodes.length() != 0) {
					enrolleeCodes.append(",");
				}
				if(ExStringUtils.isNotEmpty(uniqueFlag) && "0".equals(uniqueFlag)){// 准考证号
					enrolleeCodes.append("'").append(studentInfo.getExamCertificateNo()).append("'");
				} else {
					enrolleeCodes.append("'").append(studentInfo.getEnrolleeCode()).append("'");
				}
			}
			// 默认是ksh为长号
			String exameeInfoHql = " from "+ExameeInfo.class.getSimpleName()+" where isDeleted=0 and KSH in ("+enrolleeCodes.toString()+") ";
			// zkzh为长号
			if(ExStringUtils.isNotEmpty(uniqueFlag) && "0".equals(uniqueFlag)){
				exameeInfoHql = " from "+ExameeInfo.class.getSimpleName()+" where isDeleted=0 and ZKZH in ("+enrolleeCodes.toString()+") ";
			}
			List<ExameeInfo> exameeInfoList = exameeInfoService.findByHql(exameeInfoHql);
			
//			List<String> studyNos = new ArrayList<String>(0);
//			for (StudentInfo studentInfo : studentInfoList) {
//				studyNos.add(studentInfo.getStudyNo());
//			}
//			List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findByCriteria(EnrolleeInfo.class, new Criterion[]{Restrictions.in("matriculateNoticeNo", studyNos)});
//			List<ExameeInfo> exameeInfoList = new ArrayList<ExameeInfo>(0);
//			for (EnrolleeInfo ei : enrolleeInfoList) {
//				List<ExameeInfo> exameeInfoList_tmp = exameeInfoService.findByHql(" from "+ExameeInfo.class.getSimpleName()+" where isDeleted =0 and ZKZH='"+ei.getExamCertificateNo()+"' ");
//				exameeInfoList.addAll(exameeInfoList_tmp);
//			}
			question = "选定学号人数:"+studentId.length+"人；对应考生信息人数:"+exameeInfoList.size()+"人。";
		}else{
			Map<String,Object> condition = new HashMap<String,Object>();
			
			String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));//学习中心
			String major 				 = ExStringUtils.trimToEmpty(request.getParameter("majorId"));//专业
			String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classicId"));//层次
			String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
			String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
			String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
			String name 				 = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
			String matriculateNoticeNo   = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
			String stuGrade 			 = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
			String certNum 				 = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
			String classesid 			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
			String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
			String from                  = ExStringUtils.trimToEmpty(request.getParameter("from"));//有值来自学籍库，为空来自学籍信息
			if(stuStatus.contains("a")){
				accountStatus="1";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}else if(stuStatus.contains("b")){
				accountStatus="0";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}
			
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotEmpty(stuStatus)) {
				condition.put("stuStatus", stuStatus);
			}
			if(ExStringUtils.isNotEmpty(name)) {
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
				condition.put("matriculateNoticeNo", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotEmpty(stuGrade)) {
				condition.put("stuGrade", stuGrade);
			}
			if(ExStringUtils.isNotEmpty(certNum)) {
				condition.put("certNum", certNum);
			}
			if(ExStringUtils.isNotEmpty(entranceFlag)) {
				condition.put("entranceFlag", entranceFlag);
			}
			if(ExStringUtils.isNotEmpty(accountStatus)) {
				condition.put("accountStatus", accountStatus);
			}
			if(ExStringUtils.isNotEmpty(classesid)) {
				condition.put("classesid", classesid);
			}
			if(ExStringUtils.isNotEmpty(rollCard)) {
				condition.put("rollCard", rollCard);
			}
			String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
			if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
				condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
			}
			if(ExStringUtils.isNotEmpty(from)) {
				condition.put("from", from);
			}
			List<Map<String,Object>> students = studentInfoService.findStuByConditionByJDBC(condition);	//只是为了计数
			condition.put("enroll", "1");
			List<ExameeInfo> result = studentInfoService.findStuByConditionByJDBC(condition);
			if(1000<result.size()){
				map.put("over1000","1");
			}
			
			
			question = "选定学号人数:"+students.get(0).get("rollcount")+"人；对应考生信息人数:"+result.size()+"人。";
			
		}
		map.put("question", question);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	
	/**
	 * 学籍卡打印-条目数计算
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/calulatePrintNumForStudentCard.html")
	public void calulatePrintNumForStudentCard(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(0);
		Map<String,Object> condition = new HashMap<String, Object>(0);
		String id = ExStringUtils.trimToEmpty(request.getParameter("id"));
		
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String grade = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String fromStudentRoll  = ExStringUtils.trimToEmpty(request.getParameter("fromStudentRoll"));//从学籍管理发来的请求
		
		String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
		String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		String certNum 				 = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String classesid 			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
		//String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
		
		if (ExStringUtils.isNotBlank(fromStudentRoll)) {
			condition.put("fromStudentRoll",fromStudentRoll);
		}
		if(ExStringUtils.isNotEmpty(id)){
			condition.put("studentids", id);
		}else{
			if(stuStatus.contains("a")){
				accountStatus="1";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}else if(stuStatus.contains("b")){
				accountStatus="0";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}
			if (ExStringUtils.isNotBlank(branchSchool)) {
				condition.put("branchSchool",branchSchool);
			}
			if (ExStringUtils.isNotBlank(major)) {
				condition.put("major",major);
			}
			if (ExStringUtils.isNotBlank(classic)) {
				condition.put("classic",classic);
			}
			if (ExStringUtils.isNotBlank(stuStatus)) {
				condition.put("stuStatus",stuStatus);
			}
			if (ExStringUtils.isNotBlank(name)) {
				condition.put("name",name);
			}
			if (ExStringUtils.isNotBlank(studyNo)) {
				condition.put("studyNo",studyNo);
			}
			if (ExStringUtils.isNotBlank(grade)) {
				condition.put("grade",grade);
			}
			
			if (ExStringUtils.isNotBlank(entranceFlag)) {
				condition.put("entranceFlag",entranceFlag);
			}
			if (ExStringUtils.isNotBlank(accountStatus)) {
				condition.put("accountStatus",accountStatus);
			}
			if (ExStringUtils.isNotBlank(certNum)) {
				condition.put("certNum",certNum);
			}
			if (ExStringUtils.isNotBlank(classesid)) {
				condition.put("classesid",classesid);
			}
			if (ExStringUtils.isNotBlank(rollCard)) {
				condition.put("rollCard",rollCard);
			}
			//if (ExStringUtils.isNotEmpty(schoolrollstudentstatus)) condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
		}
		
		
		String question = "";
		try {
			List<Map<String,Object>> dataList 			     =  graduationStatJDBCService.studentRollCardInfoList(condition);
			if(dataList!=null && 500<dataList.size()){
				map.put("over500","1");
			}
			question = "打印学籍卡人数:"+(dataList!=null?dataList.size():0)+"人。";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("question", question);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 学籍卡打印-预览
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/register/studentinfo/studentCard/print-view.html")
	public String rollCardPrintView(HttpServletRequest request,ModelMap model){
		model.put("flag", ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.put("branchSchool", ExStringUtils.trimToEmpty(request.getParameter("unitId")));
		model.put("major", ExStringUtils.trimToEmpty(request.getParameter("majorId")));
		model.put("classic", ExStringUtils.trimToEmpty(request.getParameter("classicId")));
		model.put("stuStatus", ExStringUtils.trimToEmpty(request.getParameter("stuStatus")));
		model.put("name", ExStringUtils.trimToEmpty(request.getParameter("name")));
		model.put("studyNo",ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo")));
		model.put("grade", ExStringUtils.trimToEmpty(request.getParameter("gradeId")));
		
		model.put("fromStudentRoll", ExStringUtils.trimToEmpty(request.getParameter("fromStudentRoll")));
		model.put("entranceFlag", ExStringUtils.trimToEmpty(request.getParameter("entranceFlag")));
		model.put("accountStatus", ExStringUtils.trimToEmpty(request.getParameter("accountStatus")));
		model.put("certNum", ExStringUtils.trimToEmpty(request.getParameter("certNum")));
		model.put("classesId", ExStringUtils.trimToEmpty(request.getParameter("classesId")));
		model.put("rollCard", ExStringUtils.trimToEmpty(request.getParameter("rollCard")));
		
		model.put("studentids", ExStringUtils.trimToEmpty(request.getParameter("id")));
		//model.put("school", ExStringUtils.trimToEmpty(request.getParameter("school")));
		
		return "/edu3/roll/graduationStudent/student-rollcard-printview";
	}
	
	/**
	 * 学籍卡双面打印-预览
	 * @param request
	 * @param model
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/register/studentinfo/studentCardTwosided/print-view.html")
	public String rollCardTwosidedPrintView(HttpServletRequest request,ModelMap model){
		model.put("flag", ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.put("branchSchool", ExStringUtils.trimToEmpty(request.getParameter("unitId")));
		model.put("major", ExStringUtils.trimToEmpty(request.getParameter("majorId")));
		model.put("classic", ExStringUtils.trimToEmpty(request.getParameter("classicId")));
		model.put("stuStatus", ExStringUtils.trimToEmpty(request.getParameter("stuStatus")));
		model.put("name", ExStringUtils.trimToEmpty(request.getParameter("name")));
		model.put("studyNo",ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo")));
		model.put("grade", ExStringUtils.trimToEmpty(request.getParameter("gradeId")));
		
		model.put("fromStudentRoll", ExStringUtils.trimToEmpty(request.getParameter("fromStudentRoll")));
		model.put("entranceFlag", ExStringUtils.trimToEmpty(request.getParameter("entranceFlag")));
		model.put("accountStatus", ExStringUtils.trimToEmpty(request.getParameter("accountStatus")));
		model.put("certNum", ExStringUtils.trimToEmpty(request.getParameter("certNum")));
		model.put("classesId", ExStringUtils.trimToEmpty(request.getParameter("classesId")));
		model.put("rollCard", ExStringUtils.trimToEmpty(request.getParameter("rollCard")));
		
		model.put("studentids", ExStringUtils.trimToEmpty(request.getParameter("id")));
		model.put("type", ExStringUtils.trimToEmpty(request.getParameter("type")));
		
		return "/edu3/roll/graduationStudent/student-rollcardtwosided-printview";
	}
	
	
	/**
	 * 保存学生学籍表单更新
	 * @param resourceid
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/changeBaseInfo.html")
	public void changeBaseInfo(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String isSubmit = ExStringUtils.trimToEmpty(request.getParameter("isSubmit")); //学籍信息是否已提交 Y是/N否
		try {		
			if(ExStringUtils.isNotEmpty(resourceid)){
				StudentInfo stu = studentInfoService.get(resourceid);
				//由于目前默认关闭了学籍更改的日志记录功能，在此特别开启
				stu.setEnableLogHistory(true);
				if("Y".equals(isSubmit)){
					setStuImgParams(request,stu); //已提交的状态  可改变的只有图片信息
				}else{
					setStuParams(request,stu);
				}
				stu.setAuditResults("0");//0表示学生已修改学籍，待审核
				studentInfoService.saveOrUpdate(stu);	
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_STUDENT_MYSTUDENTINFO");
			}
		}catch (Exception e) {
			logger.error("保存学籍出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"保存学生学籍表："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 改变图片
	 * @param request
	 * @param stu
	 */
	private void setStuImgParams(HttpServletRequest request,StudentInfo stu){
		//~~~~~~~~~~~~~~~~基本信息~~~~~~~~~~~~
		StudentBaseInfo stuinfo = stu.getStudentBaseInfo();
		User user = SpringSecurityHelper.getCurrentUser();
		String requestUrl = request.getRequestURI();
		try {
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" update hnjk_sys_users u set u.requesturl= '"+requestUrl+"' where u.resourceid = '"+user.getResourceid()+"'", null);
		} catch (Exception e1) {
			logger.error("更新用户访问路径：{}",e1.fillInStackTrace());
		}
		stuinfo.setPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("photoPath")));
		stuinfo.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPath")));
		stuinfo.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPathReverse")));
		stuinfo.setEduPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("eduPhotoPath")));
		stu.setBookletPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("bookletPhotoPath")));//户口册相片
		stu.setOtherPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("otherPhotoPath")));//其他相片
		stu.setStudentBaseInfo(stuinfo);
	}
	
	
	private void setStuParams(HttpServletRequest request,StudentInfo stu){		
		//~~~~~~~~~~~~~~~~基本信息~~~~~~~~~~~~
		StudentBaseInfo stuinfo = stu.getStudentBaseInfo();
		User user = SpringSecurityHelper.getCurrentUser();
		String requestUrl = request.getRequestURI();
		try {
			baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(" update hnjk_sys_users u set u.requesturl= '"+requestUrl+"' where u.resourceid = '"+user.getResourceid()+"'", null);
		} catch (Exception e1) {
			logger.error("更新用户访问路径：{}",e1.fillInStackTrace());
		}
		stuinfo.setEnableLogHistory(true);
		stuinfo.setName(ExStringUtils.trimToEmpty(request.getParameter("name")));
		stuinfo.setGender(ExStringUtils.trimToEmpty(request.getParameter("gender")));
		stuinfo.setCertType(ExStringUtils.trimToEmpty(request.getParameter("certType")));
		stuinfo.setCertNum(ExStringUtils.trimToEmpty(request.getParameter("certNum")));
		
		stuinfo.setContactAddress(ExStringUtils.trimToEmpty(request.getParameter("contactAddress"))); //联系地址
		stuinfo.setContactZipcode(ExStringUtils.trimToEmpty(request.getParameter("contactZipcode")));//联系邮编
		stuinfo.setContactPhone(ExStringUtils.trimToEmpty(request.getParameter("contactPhone")));  //联系电话
		stuinfo.setMobile(ExStringUtils.trimToEmpty(request.getParameter("mobile"))); //移动电话
		stuinfo.setEmail(ExStringUtils.trimToEmpty(request.getParameter("email"))); //邮件
		stuinfo.setHomePage(ExStringUtils.trimToEmpty(request.getParameter("homePage"))); //个人主页
		if("".equals(request.getParameter("height")) || null == request.getParameter("height")){ //身高
			stuinfo.setHeight(null);
		}else{
			stuinfo.setHeight(Long.parseLong(ExStringUtils.trimToEmpty(request.getParameter("height"))));
		}
		
		stuinfo.setBloodType(ExStringUtils.trimToEmpty(request.getParameter("bloodType")));//血型
		if("".equals(request.getParameter("bornDay")) || null == request.getParameter("bornDay")){ //出生日期
			stuinfo.setBornDay(new Date());
		}else{
			stuinfo.setBornDay(ExDateUtils.convertToDate( ExStringUtils.trimToEmpty(request.getParameter("bornDay"))));
		}
		stuinfo.setBornAddress(ExStringUtils.trimToEmpty(request.getParameter("bornAddress")));//出生地
		stuinfo.setCountry(ExStringUtils.trimToEmpty(request.getParameter("country"))); //国籍
		stuinfo.setHomePlace(ExStringUtils.trimToEmpty(request.getParameter("homePlace")));
		stuinfo.setGaqCode(ExStringUtils.trimToEmpty(request.getParameter("gaqCode"))); //港澳侨代码
		stuinfo.setNation(ExStringUtils.trimToEmpty(request.getParameter("nation"))); //民族
		stuinfo.setHealth(ExStringUtils.trimToEmpty(request.getParameter("health"))); //身体健康状态
		stuinfo.setMarriage(ExStringUtils.trimToEmpty(request.getParameter("marriage")));//婚姻状况
		stuinfo.setPolitics(ExStringUtils.trimToEmpty(request.getParameter("politics")));//政治面目
		stuinfo.setFaith(ExStringUtils.trimToEmpty(request.getParameter("faith")));//宗教信仰
		stuinfo.setResidenceKind(ExStringUtils.trimToEmpty(request.getParameter("residenceKind")));//户口性质
		stuinfo.setResidence(ExStringUtils.trimToEmpty(request.getParameter("residence")));//
		stuinfo.setCurrenAddress(ExStringUtils.trimToEmpty(request.getParameter("currenAddress"))); //现住址
		stuinfo.setHomeAddress(ExStringUtils.trimToEmpty(request.getParameter("homeAddress")));//家庭住址
		stuinfo.setHomezipCode(ExStringUtils.trimToEmpty(request.getParameter("homezipCode")));//家庭住址邮编
		stuinfo.setHomePhone(ExStringUtils.trimToEmpty(request.getParameter("homePhone")));//家庭电话
		stuinfo.setOfficeName(ExStringUtils.trimToEmpty(request.getParameter("officeName")));//公司名称
		stuinfo.setOfficePhone(ExStringUtils.trimToEmpty(request.getParameter("officePhone")));//公司电话
		stuinfo.setTitle(ExStringUtils.trimToEmpty(request.getParameter("title")));//职务职称
		stuinfo.setSpecialization(ExStringUtils.trimToEmpty(request.getParameter("specialization")));//特长
		stuinfo.setMemo(ExStringUtils.trimToEmpty(request.getParameter("memo")));//备注
		stuinfo.setPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("photoPath"))); //头像相片
		stuinfo.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPath"))); //身份证照片
		stuinfo.setCertPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("certPhotoPathReverse"))); //身份证照片
		stuinfo.setEduPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("eduPhotoPath"))); //毕业证照片
		
		/**
		 * 新增其他相片上传
		 */
		stu.setBookletPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("bookletPhotoPath")));//户口册相片
		stu.setOtherPhotoPath(ExStringUtils.trimToEmpty(request.getParameter("otherPhotoPath")));//其他相片

		stu.setStudentBaseInfo(stuinfo);
	}
	
	/**
	  * 导出学信网
	  * @param request
	  * @param response
	  * @throws WebException
	  */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/register/studentinfo/exportxxweb.html")
	 public void xxwebExport(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();

		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String classic 		= ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String major 		= ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String stuGrade     = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));//年级		
		String stuStatus    = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态	
		String name 		= ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String certNum      = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//身份证号
		String studyNo      = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号 
		String rollCard     = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//学籍卡提交情况
		//2013-04-23补
		String teachPlan    = ExStringUtils.trimToEmpty(request.getParameter("teachPlan")); //是否学习前异动
		String entranceFlag = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格
		
		
		String accountStatus= "";
		User user 		    = SpringSecurityHelper.getCurrentUser();					
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			branchSchool 	= user.getOrgUnit().getResourceid();					
		}
					
		if(stuStatus.contains("a")){
			accountStatus	="1";
			stuStatus	    ="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus	="0";
			stuStatus		="11";//去掉学籍中有关帐号状态的标记
		}
		String unitStr = "";
		String classicStr = "";
		String majorStr = "";
		String gradeStr = "";
		String stuStatusStr = "";
		String teachPlanStr = "";
		String entranceFlagStr = "";
		String rollCardStr = "";
		String accountStr = "";
		StringBuffer titleStrbuf = new StringBuffer();
		
		if(ExStringUtils.isNotBlank(branchSchool)){  condition.put("branchSchool", branchSchool);unitStr = "学习中心:"+orgUnitService.get(branchSchool).getUnitShortName();}
		if(ExStringUtils.isNotBlank(classic)){ 	    condition.put("classic", classic);classicStr = "层次:"+classicService.get(classic).getShortName();}
		if(ExStringUtils.isNotBlank(major)){         condition.put("major", major);majorStr = "专业:"+majorService.get(major).getMajorName();}
		if(ExStringUtils.isNotBlank(stuGrade)){      condition.put("stuGrade", stuGrade);gradeStr = "年级:"+ gradeService.get(stuGrade).getGradeName();	}
		if(ExStringUtils.isNotBlank(stuStatus)){		condition.put("stuStatus", stuStatus);stuStatusStr = "学籍状态:"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatus", stuStatus); }
		if(ExStringUtils.isNotBlank(name)) 	{
			try {
				name = java.net.URLDecoder.decode(name , "UTF-8");
				condition.put("name", name);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		if(ExStringUtils.isNotBlank(accountStatus)){ condition.put("accountStatus", accountStatus); if("1".equals(accountStatus)){accountStr="帐号状态:"+"激活";}else if("0".equals(accountStatus)){accountStr="帐号状态:"+"停用";} }
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo",studyNo);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		if(ExStringUtils.isNotEmpty(rollCard)){      condition.put("rollCard",rollCard);  rollCardStr="学籍卡提交:"+JstlCustomFunction.dictionaryCode2Value("CodeRollCardStatus", rollCard);}
		if(ExStringUtils.isNotEmpty(teachPlan)){     condition.put("teachPlan", teachPlan);if("N".equals(teachPlan)){teachPlanStr="可用学习前异动";}else if("Y".equals(teachPlan)){teachPlanStr="不可用学习前异动";}}
		if(ExStringUtils.isNotEmpty(entranceFlag)){  condition.put("entranceFlag", entranceFlag);entranceFlagStr="入学资格审核:"+JstlCustomFunction.dictionaryCode2Value("CodeAuditStatus", entranceFlag); }
		if(ExStringUtils.isNotEmpty(unitStr)){
			titleStrbuf.append(unitStr+" ");
		}
		
		if(ExStringUtils.isNotEmpty(classicStr)){
				titleStrbuf.append(classicStr+" ");
			}
			if(ExStringUtils.isNotEmpty(majorStr)){
				titleStrbuf.append(majorStr+" ");
			}
			if(ExStringUtils.isNotEmpty(gradeStr)){
				titleStrbuf.append(gradeStr+" ");
			}
			if(ExStringUtils.isNotEmpty(stuStatusStr)){
				titleStrbuf.append(stuStatusStr+" ");
			}
			if(ExStringUtils.isNotEmpty(teachPlanStr)){
				titleStrbuf.append(teachPlanStr+" ");
			}
			if(ExStringUtils.isNotEmpty(entranceFlagStr)){
				titleStrbuf.append(entranceFlagStr+" ");
			}
			if(ExStringUtils.isNotEmpty(rollCardStr)){
				titleStrbuf.append(rollCardStr+" ");
			}
			if(ExStringUtils.isNotEmpty(accountStr)){
				titleStrbuf.append(accountStr+" ");
			}
			titleStrbuf.append("新生学信平台注册表");
			//自定义选择导出的数据列
			String[] excelColumnNames 	  = request.getParameterValues("excelColumnName");
			List<String> filterColumnList = new ArrayList<String>();//定义过滤列
			if(null !=excelColumnNames && excelColumnNames.length>0){
				for(int i=0;i<excelColumnNames.length;i++){
					filterColumnList.add(excelColumnNames[i]);
				}
			}
			
			condition.put("excludeStuStatus","16");
			
			
			GUIDUtils.init();
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				
				condition.put("isXxWeb","XxWeb"); //导出学信 标识改变sql语句
				List<Map<String,Object>> list_original = studentInfoChangeJDBCService.getStudentInfoToExport(condition);
				List<StudentInfoVo> list = new ArrayList<StudentInfoVo>(0);
				
				//是否需要把XYMC特殊处理
				List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
						, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
				
				for (Map<String,Object> si : list_original) {
					StudentInfoVo siv = new StudentInfoVo();
					//学信
					siv.setKSH(null != si.get("KSH")?si.get("KSH").toString():"-"); //考生号
					siv.setXH(null != si.get("XH")?si.get("XH").toString():"-"); //学号
					siv.setXM(null != si.get("XM")?si.get("XM").toString():"-"); //姓名
					siv.setXB(null != si.get("XB")?si.get("XB").toString():"-"); //性别
					siv.setCSRQ((null != si.get("CSRQ")?(si.get("CSRQ").toString().length()>10?si.get("CSRQ").toString().substring(0,10):si.get("CSRQ").toString()):"-").replace("-", "/")); //出生日期
					if(null != si.get("LX") && "idcard".equals(si.get("LX"))){
						siv.setSFZH(null != si.get("HM")?si.get("HM").toString():"-"); //身份证号
					}else{
						siv.setSFZH("-"); 
					}
					siv.setZZMM(null != si.get("ZZMM")?si.get("ZZMM").toString():"-"); //政治面貌
					siv.setMZ(null != si.get("MZ")?si.get("MZ").toString():"-"); //民族
					siv.setZYDM(null != si.get("ZYDM")?si.get("ZYDM").toString():"-"); //专业代码
					//查看专业名称是否需要处理
					String zymc = si.get("ZYMC")+"".replace("（", "(").replace("）", ")");
					if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(zymc)){
						String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
						String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
						for (int i = 0; i < listDictZYDM.size(); i++) {
							String rpstr1 = ExStringUtils.trim(listDictZYDM.get(i).getDictName()+"").replace("（", "(").replace("）", ")");
							String rpstr2 = ExStringUtils.trim(listDictZYDM.get(i).getDictValue()+"").replace("（", "(").replace("）", ")");
							zymcReplaceArrayL[i] = rpstr1;
							zymcReplaceArrayR[i] = rpstr2;	
						}
						zymc = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, zymc); //去除符号（含符号）里面的字符
					}
					siv.setZYMC(ExStringUtils.isNotBlank(zymc) ? zymc : "-"); //专业名称					
					siv.setFY(null != si.get("FY")?si.get("FY").toString():"-"); //分院
					//siv.setXSH(null != si.get("XSH")?si.get("XSH").toString():"-"); //系所函
					siv.setBH(null != si.get("BH")?si.get("BH").toString():"-"); //班号
					siv.setCC(null != si.get("CC")?si.get("CC").toString():"-"); //层次
					siv.setXXXS(null != si.get("XXXS")?si.get("XXXS").toString():"-"); //学习形式
					siv.setXZ(null != si.get("XZ")?si.get("XZ").toString():"-"); //学制
					siv.setRXRQ(null != si.get("RXRQ")?ExDateUtils.formatDateStr(ExDateUtils.convertToDateTime(si.get("RXRQ").toString()), "yyyy/MM/dd"):"-"); //入学日期
					//siv.setYJBYRQ(null != si.get("YJBYRQ")?si.get("YJBYRQ").toString():"-"); //预计毕业日期

					list.add(siv);
				}
				
				//自定义设置标题
				LinkedHashMap<String, String> dynamicTitleMap = new LinkedHashMap<String, String>(6);
				dynamicTitleMap.put("KSH", "KSH");
				dynamicTitleMap.put("XH", "XH");
				dynamicTitleMap.put("XM", "XM");
				dynamicTitleMap.put("XB", "XB");
				dynamicTitleMap.put("CSRQ", "CSRQ");
				dynamicTitleMap.put("SFZH", "SFZH");
				dynamicTitleMap.put("ZZMM", "ZZMM");
				dynamicTitleMap.put("MZ", "MZ");
				dynamicTitleMap.put("ZYDM", "ZYDM");
				dynamicTitleMap.put("ZYMC", "ZYMC");
				dynamicTitleMap.put("FY", "FY");
				dynamicTitleMap.put("XSH", "XSH");
				dynamicTitleMap.put("BH", "BH");
				dynamicTitleMap.put("CC", "CC");
				dynamicTitleMap.put("XXXS", "XXXS");
				dynamicTitleMap.put("XZ", "XZ");
				dynamicTitleMap.put("RXRQ", "RXRQ");
				dynamicTitleMap.put("YJBYRQ", "YJBYRQ");    
				
				
				File excelFile  = null;
				File disFile 	= new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex"); //性别字典
				dictCodeList.add("CodeNation"); //民族字典
				dictCodeList.add("CodeTeachingType"); //学习形式字典
				dictCodeList.add("CodePolitics");//政治面貌
				
				
				Map<String , Object> map = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				exportExcelService.initParmasByfile(disFile, "studentInfoXxWebVo", list,map,filterColumnList);
				exportExcelService.getModelToExcel().setHeader(titleStrbuf.toString());//设置大标题
				exportExcelService.getModelToExcel().setDynamicTitleMap(dynamicTitleMap);
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
				font.setBoldStyle(WritableFont.BOLD);
		        WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
		        format.setBackground(Colour.YELLOW);
		        format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);
				exportExcelService.getModelToExcel().setRowHeight(300);
				excelFile 	= exportExcelService.getExcelFile();//获取导出的文件
				
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response,titleStrbuf+".xls", excelFile.getAbsolutePath(),true);
				
			}catch (Exception e) {
				logger.error("导出excel文件出错："+e.fillInStackTrace());
				renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
			}
			
		} 
	
	/**
	 * 进入图片审核界面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	 @RequestMapping({"/edu3/register/studentinfo/auditStudentinfoImg.html"})
	 public String setImgStatus(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
			
		model.addAttribute("stus", ExStringUtils.trimToEmpty(request.getParameter("stus"))); //用户id
		model.addAttribute("aduitImgUserName",ExStringUtils.trimToEmpty(request.getParameter("aduitImgUserName"))); //用户名称
		
		StudentInfo info  = studentInfoService.get(ExStringUtils.trimToEmpty(request.getParameter("stus")));
		if(null != info){
			if(ExStringUtils.isNotBlank(info.getAuditResults())){
				model.addAttribute("aduit",info.getAuditResults()); //图片审核状态
			}else{
				model.addAttribute("aduit",0);
			}
			
		}
		return "/edu3/roll/sturegister/auditStudentinfoImg";
	}
	 
	
	
	/**
	 * 对学籍中上传的图片进行审核
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/aduitImgStatus.html")
	public void auditStudentinfoImg(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>(0);
		String userId = ExStringUtils.trimToEmpty(request.getParameter("userid"));  //选中的用户id
		//审核状态   撤销即变为未审核  0  审核通过1 审核不通过2
		String auditStatus  = ExStringUtils.trimToEmpty(request.getParameter("aduitimgstatus")); 
		
		try {
			StudentInfo info  = studentInfoService.get(userId);
			
			if(null != info){
				if(ExStringUtils.isNotEmpty(userId)){
					info.setAuditResults(auditStatus); //设置图片审核状态	
				}
				studentInfoService.saveOrUpdate(info); //更新
			}
		} catch (Exception e) {
			map.put("errMsg", "设置图片审核状态失败！");
			e.printStackTrace();
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.PASS,"审核图片："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 关联教学计划
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/jointeachingplan.html")
	public void joinTeachingPlan(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String[] arr = new String[]{};
		try {
			if (ExStringUtils.isNotBlank(resourceid)) {
				arr = resourceid.split("\\,");
			} else {
				//查询条件
				Map<String,Object> condition = new HashMap<String,Object>();
				String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("unitId"));//学习中心
				String major 				 = ExStringUtils.trimToEmpty(request.getParameter("majorId"));//专业
				String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classicId"));//层次
				String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
				String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
				String stuStatusRes			 = stuStatus;
				String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
				String contactAddress        = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));//联系地址
				String isFromPage            = ExStringUtils.trimToEmpty(request.getParameter("isFromPage"));//从页面来
				if(stuStatus.contains("a")){
					accountStatus="1";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}else if(stuStatus.contains("b")){
					accountStatus="0";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}
				
				String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
				String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
				String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));//年级
				String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
				String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
				String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
				User user = SpringSecurityHelper.getCurrentUser();
				if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
					branchSchool = user.getOrgUnit().getResourceid();
					model.addAttribute("isBrschool", true);
				}
				
				if(ExStringUtils.isNotEmpty(branchSchool)) {
					condition.put("branchSchool", branchSchool);
				}
				if(ExStringUtils.isNotEmpty(major)) {
					condition.put("major", major);
				}
				if(ExStringUtils.isNotEmpty(classic)) {
					condition.put("classic", classic);
				}
				if(ExStringUtils.isNotEmpty(stuStatus)) {
					condition.put("stuStatus", stuStatus);
				}
				if(ExStringUtils.isNotEmpty(name)) {
					condition.put("name", name);
				}
				if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
					condition.put("matriculateNoticeNo", matriculateNoticeNo);
				}
				if(ExStringUtils.isNotEmpty(stuGrade)) {
					condition.put("stuGrade", stuGrade);
				}
				if(ExStringUtils.isNotEmpty(certNum)) {
					condition.put("certNum", certNum);
				}
				if(ExStringUtils.isNotEmpty(entranceFlag)) {
					condition.put("entranceFlag", entranceFlag);
				}
				if(ExStringUtils.isNotEmpty(accountStatus)) {
					condition.put("accountStatus", accountStatus);
				}
				if(ExStringUtils.isNotEmpty(contactAddress)) {
					condition.put("contactAddress", contactAddress);
				}
				if(ExStringUtils.isNotBlank(classesid)){
					condition.put("classesid", classesid);
				}
				if(ExStringUtils.isNotEmpty(rollCard)) {
					condition.put("rollCard", rollCard);
				}
				String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
				if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
					condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
				}
				//condition.put("isSysUser", Constants.BOOLEAN_YES);
				List<StudentInfo> stuList = studentInfoService.findStuByCondition(condition);	
				List<String> idList = new ArrayList<String>();
				for (StudentInfo stu : stuList) {
					idList.add(stu.getResourceid());
				}
				arr = new String[idList.size()];
				idList.toArray(arr);
			}
			studentInfoService.joinTeachingPlan(arr);
			map.put("statusCode", 200);
			map.put("message", "关联教学计划成功！");				
			map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/schoolroll-list.html");
		} catch (Exception e) {
			logger.error("关联教学计划失败:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "关联教学计划失败:<br/>"+e.getLocalizedMessage());	
		}		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 在校生证明打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/register/studentinfo/print-confirmationview.html")
	public String printConfirmationView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("stus",ExStringUtils.trimToEmpty(request.getParameter("stus")));
		return "/edu3/roll/sturegister/printConfirmation";
	}

	/**
	 * 在校生证明打印
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/register/studentinfo/print-confirmation.html")
	public void confirmationPrint(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		JasperPrint jasperPrint= null;//输出的报表
		String basicPath    =	CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common";
		String stus 	   = ExStringUtils.trimToEmpty(request.getParameter("stus"));    
		String[] studentIds = stus.split(",");
	    StringBuffer ids = new StringBuffer();
	    ids.append("(");
	    for (String string : studentIds) {
			ids.append("'"+string+"',");
		}
	    ids=ids.deleteCharAt(ids.length()-1);
		ids.append(")");
	    //打印部分的业务
		Map<String,Object> param=new HashMap<String, Object>();
		try {
			param.put("stus", ids.toString());
			param.put("confirmationDate", ExDateUtils.formatDateStr(ExDateUtils.getCurrentDateTime(), "yyyy年MM月dd日"));
			param.put("basicPath",basicPath);
			param.put("SUBREPORT_DIR", "//");
			String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
			param.put("logoPath", logoPath);
			param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue() + "继续教育学院");//学校名称
			String reportfile=URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"studentinfo"+File.separator+"confirmation.jasper"),"utf-8");
			File report_file =new File(reportfile);
			//jasperPrint=JasperFillManager.fillReport(report_file.getPath(), param,conn);	
			jasperPrint = studentInfoService.printReport(report_file.getPath(), param, studentInfoService.getConn());
			if(null!=jasperPrint){
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response, "缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印在校生证明失败{}",e.fillInStackTrace());
			renderHtml(response, "打印在校生证明失败。");
		}
	}
	
	/**
	 * 给学生生成缴费记录
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/generateStudentFee.html")
	public void generateStudentFee(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> condition = new HashMap<String,Object>();
		int statusCode = 200;
		String message = "生成缴费记录成功！";
		try {
			// 处理参数
			String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
			String classic 				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
			String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));// 年级
			String major 				 = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
			String stuStatus			 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//原始学籍状态
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
			String rollCard              = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
			String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));// 身份证号
			String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
			String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesId"));// 班级
			String entranceFlag      	 = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
			String contactAddress        = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));//联系地址
			String accountStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));// 账号状态
			// 处理学籍状态和账号状态
			if(stuStatus.contains("a")){
				accountStatus="1";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}else if(stuStatus.contains("b")){
				accountStatus="0";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}
			
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotEmpty(stuGrade)) {
				condition.put("stuGrade", stuGrade);
			}
			if(ExStringUtils.isNotEmpty(major)) {			
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(stuStatus)) {
				condition.put("stuStatus", stuStatus);
			}
			if(ExStringUtils.isNotEmpty(name)) {		
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(rollCard))	 {	 
				condition.put("rollCard", rollCard);		
			}
			if(ExStringUtils.isNotEmpty(certNum)) {
				condition.put("certNum", certNum);
			}
			if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
				condition.put("studyno", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotBlank(classesid)){
				condition.put("classesid", classesid);
			}
			if(ExStringUtils.isNotEmpty(entranceFlag))	 {
				condition.put("entranceFlag", entranceFlag);
			}
			if(ExStringUtils.isNotEmpty(contactAddress)) {
				condition.put("contactAddress", contactAddress);
			}
			if(ExStringUtils.isNotEmpty(accountStatus)) {
				condition.put("accountStatus", accountStatus);
			}
			String schoolrollstudentstatus = CacheAppManager.getSysConfigurationByCode("roll.studentstatus").getParamValue();
			if(ExStringUtils.isNotEmpty(schoolrollstudentstatus)) {
				condition.put("schoolrollstudentstatus", schoolrollstudentstatus);
			}
			// 获取要生成缴费记录的学生学籍信息
			Map<String, Object> returnMap = new HashMap<String, Object>();
			//增加同步锁，避免同时操作而导致重复生成学生的缴费记录
			long starttime = System.currentTimeMillis();
			List<StudentInfo> studentInfoList = null;
			synchronized (this) {
				studentInfoList = studentInfoService.findStudentInfoListByCondition(condition);
				
				// 给学生生成缴费记录
				returnMap = studentPaymentService.generateStudentFeeRecord(studentInfoList);
			}
			long endtime = System.currentTimeMillis();
			logger.info("生成缴费记录执行共 "+studentInfoList.size()+" 条，执行时间为："+(endtime-starttime)/1000f+" 秒" );
			if(returnMap != null && returnMap.size() > 0) {
				if((Integer)returnMap.get("statusCode")==300) {
					statusCode = (Integer)returnMap.get("statusCode");
					message = (String)returnMap.get("message");
				}
			}
		} catch (Exception e) {
			logger.error(" 给学生生成缴费记录出错", e);
			statusCode = 300;
			message = "生成缴费记录失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.INSERT,"给学生生成缴费记录："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 验证学号
	 * @param studentNum
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/verifyStuNo.html")
	public void verifyStuNo(String studentNum,HttpServletRequest request, HttpServletResponse response) throws WebException {
		String msg = "";
		if(ExStringUtils.isNotBlank(studentNum)){
			StudentInfo stu = studentInfoService.findUniqueByProperty("studyNo", studentNum);
			if(null != stu){
				msg = "exist";
			}
		}			
		renderText(response, msg);
	}
	
	/**
	 * 预览打印个性化学籍表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/studentCard/printRollTable-view.html")
	public String printSchoolRollTableView(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		String name =  ExStringUtils.trimToEmpty(request.getParameter("name"));
		if (ExStringUtils.isNotBlank(name)) {
			try {
				name    = URLEncoder.encode(name,"utf-8");
			} catch (UnsupportedEncodingException e) {
				
			}
		}
		model.addAttribute("flag", ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.addAttribute("unitId", ExStringUtils.trimToEmpty(request.getParameter("unitId")));
		model.addAttribute("classicId", ExStringUtils.trimToEmpty(request.getParameter("classicId")));
		model.addAttribute("majorId", ExStringUtils.trimToEmpty(request.getParameter("majorId")));
		model.addAttribute("gradeId", ExStringUtils.trimToEmpty(request.getParameter("gradeId")));
		model.addAttribute("classesId", ExStringUtils.trimToEmpty(request.getParameter("classesId")));
		model.addAttribute("matriculateNoticeNo", ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo")));
		model.addAttribute("name",name);
		model.addAttribute("rollCard", ExStringUtils.trimToEmpty(request.getParameter("rollCard")));
		model.addAttribute("certNum", ExStringUtils.trimToEmpty(request.getParameter("certNum")));
		model.addAttribute("stuStatus", ExStringUtils.trimToEmpty(request.getParameter("stuStatus")));
		model.addAttribute("entranceFlag", ExStringUtils.trimToEmpty(request.getParameter("entranceFlag")));
		model.addAttribute("studentIds", ExStringUtils.trimToEmpty(request.getParameter("studentIds")));
		
		return "/edu3/roll/graduationStudent/student-printRollTablet-view";
	}
	
	/**
	 * 计算打印学籍表的人数
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/schoolRollTable/calculateNum.html")
	public void calculatePrintRollTableNum(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		getSchoolRollTableParam(request, condition);
		String question = "";
		try {
			List<Map<String,Object>> dataList 	=  rollJDBCService.findStudentInfoByCondition(condition);
			if(500<=dataList.size()){
				map.put("over500","1");
			}
			question = "打印学籍卡人数:"+dataList.size()+"人。";
		} catch (Exception e) {
			logger.error("计算打印学籍表的人数出错", e);
		}
		map.put("question", question);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 【打印学籍表】- 打印个性化学籍表
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolRollTable/printOrExport.html")
	public void printSchoolRollTable(HttpServletRequest request, HttpServletResponse response) throws WebException{
//		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String exportType   = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出类型
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>(); // 打印
			Map<String, Object> printParam = new HashMap<String, Object>();
			if("1".equals(exportType)){//勾选导出
				String studentIds = request.getParameter("selectedid");//学生学籍ID
				studentIds  = ExStringUtils.replace(studentIds, ",", "','");
				studentIds  = "'"+studentIds+"'";
				condition.put("studentIds", studentIds);
			}else {
				getSchoolRollTableParam(request, condition);
			}
			// 获取要打印学籍表的学生
			List<Map<String,Object>> studentInfoList 	=  rollJDBCService.findStudentInfoByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(studentInfoList)){
				// 报表irport模板路径
				String reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator;
				// 报表文件
				String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
				String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
				// 标题
				String title = schoolName + schoolConnectName;
				// logo路径
				String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
				if("10366".equals(schoolCode)){//安徽医（主要使用学校）
					reportPath += "studentInfoCards_ahy.jasper";
				}else if("10560".equals(schoolCode)){//汕头大学
					reportPath += "studentInfoCards_stdx.jasper";
				}else if("10601".equals(schoolCode)){//广西桂林医（待完善，目前用的是“学籍表”功能）
					reportPath += "studentInfoCards_gly.jasper";
				} else if ("11846".equals(schoolCode)) {//广外（待完善，目前用的是“学籍表”功能）
					reportPath += "studentInfoCards_wywm.jasper";
				} else {//默认
					reportPath += "studentInfoCards_ahy.jasper";
				}
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
				int showOrder = 0;
				String _classesId = "";
				for(Map<String,Object> info : studentInfoList){
					printParam.clear();
					String classesId = (String)info.get("classesId");
					if(ExStringUtils.isNotEmpty(_classesId)){
						if(_classesId.equals(classesId)){
							showOrder++;
						}else {
							showOrder = 1;
							_classesId = classesId;
						}
					}else {
						showOrder = 1;
						_classesId = classesId;
					}
					printParam.put("title", title);
					printParam.put("logoPath", logoPath);
					// 学籍信息
					printParam.put("NO", showOrder+"");// 学籍表顺序号
					printParam.put("grade", info.get("gradename").toString().substring(0, 4));//年级
					printParam.put("gradeName", ((BigDecimal)info.get("firstyear")).toString());// 年级
					printParam.put("majorName", (String)info.get("majorname").toString().replaceAll("A", "").replaceAll("B", ""));// 专业名称
					printParam.put("teachingType", (String)info.get("teachingtype"));// 学习形式
					printParam.put("xz", Tools.eduyearTransfer((String)info.get("eduyear")));// 学制
					printParam.put("className", (String)info.get("classesname"));// 班级名称
					printParam.put("studentNo", (String)info.get("studyno"));// 学号
					printParam.put("studentName", (String)info.get("studentname"));// 学生名字
					printParam.put("gender", (String)info.get("gender"));// 性别
					printParam.put("birth", (String)info.get("bornday"));// 生日
					printParam.put("address", (String)info.get("contactaddress"));// 联系地址
					printParam.put("postcode", (String)info.get("contactzipcode"));// 邮编
					printParam.put("phone", (String)info.get("mobile"));// 联系电话
					printParam.put("indate", (String)info.get("indate"));// 入学日期
					printParam.put("graduateDate", (String)info.get("graduatedate"));// 毕业日期
					printParam.put("suspend", (String)info.get("suspInfo"));// 留级、休学信息
					printParam.put("return", (String)info.get("retInfo"));// 复学信息
					printParam.put("studentChangeInfo", (String)info.get("changeInfo"));// 学籍异动信息
					JRBeanCollectionDataSource dataSource = null;
					if("10366".equals(schoolCode)){//安徽医
						// 学生成绩信息处理
						List<Map<String, Object>> examResulsList = teachingJDBCService.findFinalExamResults((String)info.get("stuInfoId"));
						List<ExamResultsInfoVO> examResultsInfoList = studentInfoService.handleExamResultInfo(examResulsList); 
						dataSource = new JRBeanCollectionDataSource(examResultsInfoList);
					}else {
						List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
						list.add(new HashMap<String, Object>());
						dataSource = new JRBeanCollectionDataSource(list);
					}
					JasperPrint jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
					jasperPrints.add(jasperPrint);

				}
			}

			JasperPrint jasperPrint =ExBeanUtils.convertListToJasperPrint(jasperPrints);

			if (jasperPrints.size()>0 ) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印学籍表出错", e);
		}
	}
	
	/**
	 * 获取打印学籍表的参数
	 * @param request
	 * @param condition
	 */
	private void getSchoolRollTableParam(HttpServletRequest request, Map<String, Object> condition) {
		String studentIds = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));
		String name =  ExStringUtils.trimToEmpty(request.getParameter("name"));
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String majorId = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String studentNo = ExStringUtils.trimToEmpty(request.getParameter("studentNo"));
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String rollCard = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
		String entranceFlag = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));
		
		if(ExStringUtils.isNotEmpty(studentIds)){
			condition.put("studentIds", studentIds.replace(",", "','"));
		} else {
			if(ExStringUtils.isNotEmpty(unitId)){
				condition.put("unitId", unitId);
			}
			if(ExStringUtils.isNotEmpty(classicId)){
				condition.put("classicId", classicId);
			}
			if(ExStringUtils.isNotEmpty(majorId)){
				condition.put("majorId", majorId);
			}
			if(ExStringUtils.isNotEmpty(gradeId)){
				condition.put("gradeId", gradeId);
			}
			if(ExStringUtils.isNotEmpty(classesId)){
				condition.put("classesId", classesId);
			}
			if(ExStringUtils.isNotEmpty(studentNo)){
				condition.put("studentNo", studentNo);
			}
			if(ExStringUtils.isNotEmpty(matriculateNoticeNo)){
				condition.put("studentNo", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotEmpty(rollCard)){
				condition.put("rollCard", rollCard);
			}
			if(ExStringUtils.isNotEmpty(certNum)){
				condition.put("certNum", certNum);
			}
			if(ExStringUtils.isNotEmpty(stuStatus)){
				condition.put("stuStatus", stuStatus);
			}
			if(ExStringUtils.isNotEmpty(name)){
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(entranceFlag)){
				condition.put("entranceFlag", entranceFlag);
			}
		}
	}
	
	/**
	 * 创建学习计划
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/generateStuPlan.html")
	public void generateStuPlan(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "创建学习计划成功！";
		try {
			String studentIds = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));
			condition.put("studentIds", Arrays.asList(studentIds.split(",")));
			// 获取要创建学习计划的学生
			List<StudentInfo> studentInfoList =  studentInfoService.findByCondition(condition);
			studentLearnPlanService.generateStuPlan(studentInfoList, studentIds,null);
			
		} catch (Exception e) {
			logger.error("", e);
			statusCode = 300;
			message = "创建学习计划失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu3/roll/studentinfo/studentStatistics.html")
	public String printStudentStatistics(HttpServletRequest request, HttpServletResponse response){
		return "/edu3/roll/sturegister/printStudentStatistics-view";
	}
	@RequestMapping("/edu3/roll/studentinfo/studentStatistics-view.html")
	public void printStudentStatisticsView(HttpServletRequest request, HttpServletResponse response){
		User user = SpringSecurityHelper.getCurrentUser();
//		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
		String brSchool = user.getOrgUnit().getResourceid();					
//		}
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("brSchool", brSchool);

		JasperPrint jasperPrint = null; // 打印
		Map<String, Object> printParam = new HashMap<String, Object>();		
		String currentYear = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue().substring(0,4);
		Long year = Long.parseLong(currentYear.substring(0,4));
		Long []years = {year-4,year-3,year-2,year-1,year};
		List<String> majorList = new ArrayList<String>();
		majorList.add("LCYX5");
		majorList.add("YXYXX5");
		majorList.add("YXJY5");
		majorList.add("KQYX5");
		majorList.add("HLX5");
		majorList.add("YX5");
		majorList.add("XXGL5");
		majorList.add("XXGL5");
		majorList.add("ZYX5");
		majorList.add("LCYX6");
		majorList.add("KQYX6");
		majorList.add("HL6");
		majorList.add("YX6");
		majorList.add("YXJYJS6");
		majorList.add("YXYXJS6");
		majorList.add("ZY6");
		condition.put("years", Arrays.asList(years));
		
		condition.put("LCYX5", "112001");
		condition.put("YXYXX5", "112002");
		condition.put("YXJY5", "112003");
		condition.put("KQYX5", "112004");
		condition.put("HLX5", "112005");
		condition.put("YX5", "112006");
		condition.put("XXGL5", "112007");
		condition.put("ZYX5", "112008");
		condition.put("LCYX6", "412008");
		condition.put("KQYX6", "412009");
		condition.put("HL6", "412010");
		condition.put("YX6", "412011");
		condition.put("YXJYJS6", "412012");
		condition.put("YXYXJS6", "412013");
		condition.put("ZY6", "412014");
		// 获取要打印的数据
		List<StudentStatisticsVo> list 	=  studentInfoService.CalStudentStatistics(majorList,condition);
		
		if(ExCollectionUtils.isNotEmpty(list)){
			//添加总计
			int sum1 = 0;
			int sum2 = 0;
			int sum3 = 0;
			for (StudentStatisticsVo stu:list){
				sum1+=Integer.parseInt(stu.getSUM1());
				sum2+=Integer.parseInt(stu.getSUM2());
				sum3+=Integer.parseInt(stu.getSUM3());
			}		
			StudentStatisticsVo tmpVo = new StudentStatisticsVo(String.valueOf(sum1), String.valueOf(sum2), String.valueOf(sum3));
			list.add(tmpVo);
			
			// 报表irport模板路径
			try {
				String reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"guangdongMedicalUniversity.jasper";
				// 报表文件
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");

				// logo路径
				String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
				printParam.put("unitName", user.getOrgUnit().getUnitName());
				printParam.put("logoPath", logoPath);
				printParam.put("year", currentYear);
				printParam.put("printDate", ExDateUtils.getCurrentDate());
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
				jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		if (null!=jasperPrint) {
			renderStream(response, jasperPrint);
		}else {
			renderHtml(response,"缺少打印数据！");
		}
	
	}
	@RequestMapping("/edu3/roll/studentinfo/stuRollPhotoimport-view.html")
	public String stuRollPhotoimport_view(HttpServletRequest request,ModelMap model) throws WebException{
		//Map<String ,Object> model = new HashMap<String, Object>();
		List<Grade> grade = gradeService.findByHql(" from "+Grade.class.getSimpleName()+" where isDeleted = 0 order by gradeName desc ");
		StringBuffer gradeselect = new StringBuffer();
		gradeselect.append(" <select name = 'grade' id = 'gradeid' style='width:120px'> ");
		for(Grade g: grade){
			gradeselect.append(" <option value ='"+g.getResourceid()+"' > "+g.getGradeName()+" </option> ");
		}
		gradeselect.append(" </select>");
		model.addAttribute("gradeselect", gradeselect.toString());
		return "/edu3/roll/enrolschool/stuRoll-photo-upload";
	}
	@RequestMapping("/edu3/roll/studentinfo/stuRollPhotoimport.html")
	public void stuRollPhotoimport(String grade,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String tempZipPath = null;//解压路径
		try {			
			if(ExStringUtils.isNotBlank(grade) && ExStringUtils.isNotBlank(attachId)){
				Attachs attach = attachsService.get(attachId);				
				tempZipPath = Constants.EDU3_DATAS_LOCALROOTPATH+"temp"+File.separator+ExStringUtils.substringBefore(attach.getSerName(), "."+attach.getAttType());
				ZipUtils.unZip(tempZipPath, attach.getSerPath()+File.separator+attach.getSerName());//解压到临时目录
				Map<String ,Object> resultMap = studentInfoService.importPhoto(grade,tempZipPath);
				//Map<String, Object> resultMap = exameeInfoService.importExameeInfoPhoto(planid, tempZipPath);
				map.putAll(resultMap);
				
				map.put("statusCode", 200);				
				//map.put("reloadUrl", request.getContextPath()+"/edu3/recruit/exameeinfo/list.html");
				map.put("navTabId", "RES_STUDENT_ROLL_PHOTO");
				
			}
		} catch (Exception e) {
			logger.error("导入学生相片出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入学生相片失败:<br/>"+e.getMessage());			
		} finally {
			try {
				if(ExStringUtils.isNotBlank(tempZipPath) && new File(tempZipPath).exists()){
					FileUtils.delFolder(tempZipPath);//清空临时解压目录
				}
			} catch (Exception e2) {
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu3/register/studentinfo/readCertNoRegister.html")
	public String readCertNoRegister(String devicestype,HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String schoolCode=CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		String recruitPlanID = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanID"));//招生批次
		OrgUnit unit = SpringSecurityHelper.getCurrentUser().getOrgUnit();
		String hql = " from "+EnrolleeInfo.class.getSimpleName()+" where recruitMajor.recruitPlan.resourceid=? and branchSchool.resourceid=? and registorFlag='Y' and isDeleted=0 ";
		List<EnrolleeInfo> enList=enrolleeInfoService.findByHql(hql, recruitPlanID,unit.getResourceid());
		model.addAttribute("registeredCount", enList.size());
		model.addAttribute("schoolCode", schoolCode);
		model.addAttribute("devicestype", devicestype);
		model.addAttribute("recruitPlanID", recruitPlanID);
		model.put("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				 + CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		return "/edu3/roll/enrolschool/readCertNoRegister";
	}
	
	/**
	 * 帮助学生确认基本信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/helpConfirmMsgView.html")
	public String helpComfirmMsgView(HttpServletRequest request,ModelMap model) {
		try {
			String studentId = request.getParameter("studentId");
			if(ExStringUtils.isNotEmpty(studentId)){
				StudentInfo defaultStudentInfo = studentInfoService.get(studentId);
				model.addAttribute("studentInfo", defaultStudentInfo);
			}
			
			model.addAttribute("phoneComfirm", "0");//确认手机登陆	
		} catch (Exception e){
			logger.error("helpComfirmMsgView --- 获取学生基本信息出错",e);	
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"帮助学生确认首次登录："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		return "/edu3/roll/enrolschool/confirmStuinfo";
	}
	
	/**
	 * 帮助学生提交学籍卡
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/helpSubmitStuCard.html")
	public void helpSubmitStuCard(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "提交学籍卡成功";
		try {		
			do{
				String studentIds = request.getParameter("studentIds");
				if(ExStringUtils.isEmpty(studentIds)){
					statusCode = 300;
					message = "请选择至少一条记录操作";
					continue;
				}
				int successNum = studentInfoService.submitStuCard(studentIds.replaceAll(",", "','"));
				if(successNum<1){
					statusCode = 300;
					message = "提交学籍卡失败";
				}
			}while(false);
		} catch (Exception e) {
			logger.error("帮助学生提交学籍卡",e);
			statusCode = 300;
			message = "提交学籍卡失败";			
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);	
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.UPDATE,"帮助学生提交学籍卡："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 跳转到导入窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/studentinfo/importStudyNoDialog.html")
	public String importStudyNoDialog(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		model.addAttribute("title", "导入学号");
		model.addAttribute("url", "/edu3/roll/studentinfo/importStudyNo.html");
		return "edu3/roll/inputDialogForm";
	}
	
	@RequestMapping("/edu3/roll/studentinfo/importStudyNo.html")
	public void importStudyNo(HttpServletRequest request, HttpServletResponse response, String exportAct) throws WebException, IOException{
		Map<String ,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		StringBuffer message = new StringBuffer();
		try {
			do {
				String attchID = ExStringUtils.trimToEmpty(request.getParameter("attachId"));
				User user = SpringSecurityHelper.getCurrentUser();
				Attachs attachs =  attachsService.get(attchID);//获取上传的文件，因为是单个文件，所以get(0)就可
				File excel = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
				importExcelService.initParmas(excel, "studyNoImportVo", null);
				importExcelService.getExcelToModel().setSheet(0);//设置excel sheet 0 
				List<StudyNoImportVo> modelList = importExcelService.getModelList();
				if(ExCollectionUtils.isNotEmpty(modelList)){
					long et1 = System.currentTimeMillis();
					Map<String, Object> returnMap = studentInfoService.importStudyNo(modelList);
					long st2 = System.currentTimeMillis();
					logger.info("花费时间："+(st2-et1)/1000f+"秒");
					if(returnMap!=null &&returnMap.size()>0 ){
						statusCode = (Integer)returnMap.get("statusCode");
						message.append(returnMap.get("message"));
					}
					if(returnMap.containsKey("failRecord")){
						File excelFile = null;
						String url= "/edu3/roll/studentinfo/studyNoImportFail.html";
						List<StudyNoImportVo> failList =(List<StudyNoImportVo>) returnMap.get("failRecord");
						setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
						//导出
						GUIDUtils.init();
						String fileName = GUIDUtils.buildMd5GUID(false);
						File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
						//模板文件路径
						String templateFilepathString = "studyNoImportFail.xls";
						//初始化配置参数
						exportExcelService.initParmasByfile(disFile,"studyNoImportVo_exportFail", failList,null);
						exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, null);
							
						excelFile = exportExcelService.getExcelFile();//获取导出的文件
						logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
						map.put("failDLUrl", url+"?excelFile="+fileName);
					}
				}else {
					statusCode = 300;				
					message.append("没有数据！");
				}
			} while (false);
		} catch (Exception e) {
			logger.error("导入出错:{}",e.fillInStackTrace());
			statusCode = 300;
			message.append("导入学号出错！");
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message.toString());	
			map.put("navTabId", "RES_SCHOOL_REGISTER_ENROLL");
			map.put("reloadUrl", request.getContextPath()+"/edu3/register/studentinfo/schoolroll-list.html");
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3", UserOperationLogs.IMPORT,"导入学号："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
			renderJson(response,JsonUtils.mapToJson(map));
		}
	}
	
	@RequestMapping("/edu3/roll/studentinfo/studyNoImportFail.html")
	public void downloadStudentPaymentImportFail(String excelFile,HttpServletRequest request, HttpServletResponse response){
		try{
			//模板文件路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
			downloadFile(response, "学号导入失败记录.xls", disFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出学号导入失败记录出错："+e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出学号导入失败记录出错\");</script>");
		}
	}
	
	@RequestMapping("/edu3/register/studentinfo/viewCerPhoto.html")
	public String viewCerPhoto(String path,HttpServletRequest request,ModelMap model){
		model.addAttribute("path", path);
		model.put("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
				 + CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		return "/edu3/roll/sturegister/viewCerPhoto-form";
	}
	
	@RequestMapping("/edu3/register/studentinfo/repayingReason.html")
	public String viewCerPhoto(HttpServletRequest request,ModelMap model){
		model = Condition2SQLHelper.getModelMapFromResquestByIterator(request, model);
		return "/edu3/roll/sturegister/repaying-form";
	}
	
	@RequestMapping("/edu3/register/studentinfo/repaying.html")
	public void repaying(HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String, Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "清退成功！";
		User user  = SpringSecurityHelper.getCurrentUser();
		Date date = new Date();
		try {
			String reason = ExStringUtils.trimToEmpty(request.getParameter("reason"));
			String studentIds = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));
			condition.put("studentIds", Arrays.asList(studentIds.split(",")));
			List<StuChangeInfo> stuChangeInfos = new ArrayList<StuChangeInfo>();
			List<StudentInfo> studentInfoList =  studentInfoService.findByCondition(condition);
			if(ExStringUtils.isMessyCode(reason)){
				reason = ExStringUtils.getEncodeURIComponentByOne(reason);
			}
			for (StudentInfo studentInfo : studentInfoList) {
				//给每个学生添加学籍异动记录
				StuChangeInfo scInfo = new StuChangeInfo();
				if("13".equals(studentInfo.getStudentStatus()) || "26".equals(studentInfo.getStudentStatus())){
					//二次退学：将暂缓学生进行退学，这里用备注信息进行区分
					String hql = "from "+StuChangeInfo.class.getSimpleName()+" where isDeleted=0 and studentInfo.resourceid=? and changeType='18'";
					scInfo = stuChangeInfoService.findUnique(hql, studentInfo.getResourceid());
					scInfo.setReason(reason);
				}else {
					scInfo.setChangeBeforeBrSchool(studentInfo.getBranchSchool());
					scInfo.setChangeBeforeLearingStyle(studentInfo.getTeachingType());
					scInfo.setChangeBeforeClass(studentInfo.getClasses());
					scInfo.setChangeBeforeStudentStatus(studentInfo.getStudentStatus());
					scInfo.setReason(reason);
					scInfo.setStudentInfo(studentInfo);
					//studentInfo.setStudentStatus("13");
					if(null!=studentInfo.getSysUser()){ 
						studentInfo.getSysUser().setEnabled(false);
					}
					studentInfo.setAccountStatus(Constants.BOOLEAN_FALSE);
					studentInfoService.update(studentInfo);
				}
				scInfo.setChangeType("18");//清退
				studentInfo.setStudentStatus("26");//清退
				scInfo.setApplicationDate(date);
				scInfo.setApplicationMan(user.getCnName());
				scInfo.setApplicationManId(user.getResourceid());
				scInfo.setAuditDate(date);
				scInfo.setAuditMan(user.getCnName());
				scInfo.setAuditManId(user.getResourceid());
				scInfo.setFinalAuditStatus("Y");
				//将学籍状态从‘退学’改为‘清退’
				if("13".equals(studentInfo.getStudentStatus())){
					studentInfoService.update(studentInfo);
				}
				stuChangeInfos.add(scInfo);
			}
			stuChangeInfoService.batchSaveOrUpdate(stuChangeInfos);
			
		} catch (Exception e) {
			logger.error("", e);
			statusCode = 300;
			message = "清退操作失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/schoolroll/loginConfirmMSG/list.html")
	public String loginConfirmMSG(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage) throws WebException{
		objPage.setOrderBy("sendTime");
		objPage.setOrder(Page.DESC);
		String flag = request.getParameter("flag");
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		condition.put("formId", "confirmInformation_studentLogin");
		if("export".equals(flag)){
			objPage.setPageSize(1000000);
		}
		Page messagePage = sysMsgService.findMessageByCondition(condition, objPage);
		List<Message> messageResults = messagePage.getResult();
		for (Message message : messageResults) {
			//Map<String, Object> map = JsonUtils.jsonToMap(message.getFormType());
			message.setTitle(ExStringUtils.remove4All(message.getContent(),"<br/>","<font color='green'>","</font>"));
			message.setStudyNo(message.getMsgTitle().substring(message.getMsgTitle().indexOf("(")+1,message.getMsgTitle().indexOf(")")));
		}
		model.addAttribute("condition", condition);
		model.addAttribute("messagePage", messagePage);
		if("export".equals(flag)){
			String fileName = "登录确认信息审核情况表";
			try {
				fileName = new String(fileName.getBytes("GBK"), "iso8859-1");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			response.setCharacterEncoding("GB2312");
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
			return "/edu3/roll/sturegister/loginConfirmMSG_export";
		}
		return "/edu3/roll/sturegister/loginConfirmMSG-list";
	}

	@RequestMapping("/edu3/schoolroll/loginConfirmMSG/view.html")
	public String viewContent(String resourceid,ModelMap model) {
		Message message = sysMsgService.get(resourceid);
		model.addAttribute("message",message);
		return "/edu3/roll/sturegister/loginConfirmMSG_view";

	}
	@RequestMapping("/edu3/schoolroll/loginConfirmMSG/audit.html")
	public void auditConfirmMSG(String resourceid,String studyNo,HttpServletRequest request,HttpServletResponse response){
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank4All(resourceid,studyNo)){
				String[] studyNos = studyNo.split("\\,");
				String[] resids = resourceid.split("\\,");
				//Gson gson = new Gson();
				Map<String, Object> mapInfo = new HashMap<String, Object>();
 				for (int i = 0; i < studyNos.length; i++) {
 					StudentInfo info = studentInfoService.findUniqueByProperty("studyNo",studyNos[i]);
					Message message = sysMsgService.get(resids[i]);
					//mapInfo = gson.fromJson(message.getFormType(), mapInfo.getClass());
					mapInfo = JsonUtils.jsonToMap(message.getFormType());
					if(mapInfo!=null){
						if(mapInfo.containsKey("studentName")){
							String name = mapInfo.get("studentName").toString().replaceAll("'","").trim();
							info.setStudentName(name);
							info.getStudentBaseInfo().setName(name);
						}
			        	if(mapInfo.containsKey("gender")){
			        		String gender = mapInfo.get("gender").toString().replaceAll("'","").trim();
			        		info.getStudentBaseInfo().setGender(gender);
			        	}
			        	if(mapInfo.containsKey("certType")){
			        		String certType = mapInfo.get("certType").toString().replaceAll("'","").trim();
			        		info.getStudentBaseInfo().setCertType(certType);
			        	}
			        	if(mapInfo.containsKey("certNum")){
			        		User user = info.getSysUser();
			        		String certNum = mapInfo.get("certNum").toString().replaceAll("'","").trim();
			        		info.getStudentBaseInfo().setCertNum(certNum);
							if(("idcard").equals(info.getStudentBaseInfo().getCertType())){
								user.setUsername(certNum);
							}
							userService.saveOrUpdate(user);
			        	}
			        	studentInfoService.update(info);
					}else {
						mapInfo = new HashMap<String, Object>();
					}
					message.setStatus("Y");
					sysMsgService.saveOrUpdate(message);
				}
			}
			map.put("statusCode", 200);
			map.put("message", "操作成功！");				
			map.put("forward", request.getContextPath()+"/edu3/schoolroll/loginConfirmMSG/list.html");
		} catch (Exception e) {
			logger.error("审核学生首次登录申请失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败<br/>"+e.getLocalizedMessage());				
		
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 选择入学日期
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/inputEnrollDate.html")
	public String inputEnrollDate(HttpServletRequest request,ModelMap model) throws WebException {
		model.addAttribute("title", "请选择入学日期");
		model.addAttribute("url", "/edu3/register/studentinfo/updateEnrollDate.html");
		Condition2SQLHelper.getModelMapFromResquestByIterator(request,model);
		return "/edu3/roll/sturegister/updateDate_form";
	}

	/**
	 * 修改入学日期
	 * @param studentids
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/register/studentinfo/updateEnrollDate.html")
	public void updateEnrollDate(String studentids,HttpServletRequest request,HttpServletResponse response) {
		Map<String ,Object> map = new HashMap<String, Object>();
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);

		String inDate = request.getParameter("enrollDate");
		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) { //校外学习中心只操作本部数据
			String brSchool = user.getOrgUnit().getResourceid();
			condition.put("branchSchoolId",brSchool);
		}
		try {
			Map<String,Object> colMap = new HashMap<String, Object>();
			colMap.put("inDate",inDate);
			if (ExStringUtils.isBlank(studentids)) {
				StringBuilder builder = new StringBuilder();
				List<StudentInfo> studentInfos = studentInfoService.findByCondition(condition);
				for (StudentInfo info : studentInfos) {
					builder.append(info.getResourceid()).append(",");
				}
				studentids = builder.substring(0,builder.length()-1);
			}
			studentInfoService.batchSetStudendInfo(colMap, condition);
			map.put("statusCode", 200);
			map.put("message", "操作成功！");
			//map.put("forward", request.getContextPath()+"/edu3/register/studentinfo/schoolinfo-list.html");
		} catch (Exception e) {
			logger.error("修改学生入学日期失败：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 给学生生成教材费订单
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/studentinfo/createTextbookFee.html")
	public void createTextbookFee(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String,Object> condition = new HashMap<String,Object>();
		int statusCode = 200;
		String message = "生成教材费订单成功！";
		try {
			// 处理参数
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
			String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
			String stuGrade = ExStringUtils.trimToEmpty(request.getParameter("stuGrade"));// 年级
			String major 	= ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
			String rollCard  = ExStringUtils.trimToEmpty(request.getParameter("rollCard"));//是否提交学籍卡
			String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));// 身份证号
			String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
			String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesId"));// 班级
			String entranceFlag = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));//入学资格状态
			String resouceids = ExStringUtils.trimToEmpty(request.getParameter("resouceids"));
			
			if(ExStringUtils.isNotBlank(resouceids)){
				condition.put("stus", "'"+resouceids.replaceAll(",", "','")+"'");
			} else {
				if(ExStringUtils.isNotEmpty(branchSchool)) {
					condition.put("branchSchool", branchSchool);
				}
				if(ExStringUtils.isNotEmpty(classic)) {
					condition.put("classic", classic);
				}
				if(ExStringUtils.isNotEmpty(stuGrade)) {
					condition.put("stuGrade", stuGrade);
				}
				if(ExStringUtils.isNotEmpty(major)) {			
					condition.put("major", major);
				}
				if(ExStringUtils.isNotEmpty(name)) {		
					condition.put("name", name);
				}
				if(ExStringUtils.isNotEmpty(rollCard))	 {	 
					condition.put("rollCard", rollCard);		
				}
				if(ExStringUtils.isNotEmpty(certNum)) {
					condition.put("certNum", certNum);
				}
				if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
					condition.put("studyno", matriculateNoticeNo);
				}
				if(ExStringUtils.isNotBlank(classesid)){
					condition.put("classesid", classesid);
				}
				if(ExStringUtils.isNotEmpty(entranceFlag))	 {
					condition.put("entranceFlag", entranceFlag);
				}
			}
			
			// 获取要生成教材费订单的学生学籍信息
			Map<String, Object> returnMap = new HashMap<String, Object>();
			//增加同步锁，避免同时操作而导致重复生成学生的教材费订单
			List<StudentInfo> studentInfoList = null;
			synchronized (this) {
				studentInfoList = studentInfoService.findStudentInfoListByCondition(condition);
				// 给学生生成教材费订单
				returnMap = tempStudentFeeService.createTextbookFee(studentInfoList);
			}
			if(returnMap != null && returnMap.size() > 0) {
				if((Integer)returnMap.get("statusCode")==300) {
					statusCode = (Integer)returnMap.get("statusCode");
					message = (String)returnMap.get("message");
				}
			}
		} catch (Exception e) {
			logger.error("给学生生成教材费订单出错", e);
			statusCode = 300;
			message = "生成教材费订单失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 下载毕业实习材料导入模版
	 * @param response
	 */
	@RequestMapping("/edu3/roll/sturegister/graduationPracticeMaterials/download.html")
	public void downloadGraduationPracticeMaterials(HttpServletResponse response) throws WebException {
		try{
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//模板文件路径
			String templateFilepathString = "graduationPracticeMaterials.xls";
			downloadFile(response, "毕业实习材料导入模版.xls", templateFilepathString,false);
		}catch(Exception e){
			String msg = "导出excel文件出错：找不到该文件录入模版.xls";
			logger.error("下载文件模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}

	/**
	 * 导入文件选择窗口
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/sturegister/graduationPracticeMaterials/import.html")
	public String inputDialog(ModelMap model) throws WebException {
		model.addAttribute("title", "导入毕业实习材料提交状态");
		model.addAttribute("formId", "graduationPracticeMaterials_import");
		model.addAttribute("url", "/edu3/roll/sturegister/graduationPracticeMaterials/save.html?mateType=graduation");
		return "edu3/roll/inputDialogForm";
	}

}
