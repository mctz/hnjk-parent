package com.hnjk.edu.recruit.controller;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.cache.MemcachedManager;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.model.StudentResume;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IStudentResumeService;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.portal.service.IArticleService;
import com.hnjk.edu.portal.service.IChannelService;
import com.hnjk.edu.recruit.helper.ComparatorRecruitMajor;
import com.hnjk.edu.recruit.model.*;
import com.hnjk.edu.recruit.service.*;
import com.hnjk.edu.recruit.util.ReplaceStr;
import com.hnjk.edu.recruit.util.zipUtil;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentBaseInfoService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.extend.plugin.excel.config.ConfigConstant;
import com.hnjk.extend.plugin.excel.config.ExcelConfigInfo;
import com.hnjk.extend.plugin.excel.config.ExcelConfigPropertyParam;
import com.hnjk.extend.plugin.excel.util.ValidateColumn;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
import lombok.Cleanup;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsAbstractExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 学生报名Controller
 * <p>
 * 
 * @author： liuz,广州华南教育科技发展有限公司.
 * @since： 2010-3-22 上午11:43:37
 * @see
 * @version 1.0
 */
@Controller
public class EnrolleeInfoController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -6985818416296996274L;

	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	@Autowired
	@Qualifier("exameeInfoService")
	private IExameeInfoService exameeInfoService;
	
	
	@Autowired
	@Qualifier("articleService")
	private IArticleService articleService;
	

	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("channelService")
	private IChannelService channelService;
	
	@Autowired
	@Qualifier("studentBaseInfoService")
	private IStudentBaseInfoService studentBaseInfoService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;

	@Autowired
	@Qualifier("studentResumeService")
	private IStudentResumeService studentResumeService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;

	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;

	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;

	@Autowired
	@Qualifier("branchSchoolPlanService")
	private IBranchSchoolPlanService branchSchoolPlanService;
	
	@Autowired
	@Qualifier("branchSchoolMajorService")
	private IBranchSchoolMajorService branchSchoolMajorService;
		
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;	
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;
	
//	@Autowired
//	@Qualifier("examRoomPlanService")
//	private IExamRoomPlanService examRoomPlanService;
	
	@Autowired
	@Qualifier("memcacheManager")
	private MemcachedManager memcachedManager;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	/**
	 * 获取所有学生报名信息列表
	 * 
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/enroll/enrolleeinfo-list.html")
	public String listEnrolleeInfo(HttpServletRequest request, Page objPage,ModelMap model) throws WebException {
		// 查询条件
		Map<String, Object> condition = new HashMap<String, Object>();
		// 高级查询
		String advanseCon 			  = StringUtils.trimToEmpty(request.getParameter("con"));
		// 学习中心
		String branchSchool 		  = StringUtils.trimToEmpty(request.getParameter("branchSchool"));
		// 招生批次
		String recruitPlan 			  = StringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		//年级
		String gradeId     			  = StringUtils.defaultIfEmpty(request.getParameter("grade"), "");
		// 专业
		String major 				  = StringUtils.trimToEmpty(request.getParameter("major"));
		// 层次
		String classic 				  = StringUtils.trimToEmpty(request.getParameter("classic"));
		// 姓名
		String name 				  = StringUtils.trimToEmpty(request.getParameter("name"));
		// 准考证号
		String examCertificateNo 	  = StringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		// 证件号码
		String certNum 				  = StringUtils.trimToEmpty(request.getParameter("certNum"));
		// 报名类型
		String enrollType 			  = StringUtils.trimToEmpty(request.getParameter("enrollType"));
		//学习形式
		String learningStyle 		  = StringUtils.trimToEmpty(request.getParameter("learningStyle"));
		//是否免试
		String isApplyNoexam 		  = StringUtils.trimToEmpty(request.getParameter("isApplyNoexam"));
		//按xx排序
		String orderBy 				  = StringUtils.trimToEmpty(request.getParameter("orderBy"));
		//排序类型 ASC DESC
		String orderType 			  = StringUtils.trimToEmpty(request.getParameter("orderType"));
		//办学模式
		String teachingType           = StringUtils.trimToEmpty(request.getParameter("teachingType"));
		//是否存在照片
		String isExistsPhoto           = StringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));

		// 如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
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
		if (StringUtils.isNotEmpty(name)) {
			condition.put("name", name);
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
		if (StringUtils.isNotEmpty(isExistsPhoto)) {
			condition.put("isExistsPhoto",isExistsPhoto);
		}
		//默认排序	
		condition.put("orderBy", " order by ei.recruitMajor.recruitPlan.startDate desc,ei.branchSchool.unitCode,ei.signupFlag,ei.resourceid");
		condition.put("orderType","");		
		//如果排序条件不为空，则加入排序
		if(StringUtils.isNotEmpty(orderBy) && StringUtils.isNotEmpty(orderType))  {			
			condition.put("orderBy", " order by ei."+orderBy);
			condition.put("orderType", orderType);
		}		
		
		model.addAttribute("condition", condition);

		if ("advance".equals(advanseCon)) {
			return "/edu3/recruit/enroll/enrolleeinfo-search";// 返回到高级检索
		}

		Page page = enrolleeInfoService.findEnrolleeByCondition(condition,objPage);
		model.addAttribute("eilist", page);
		
		//List<OrgUnit> bchSchools = orgUnitService.findOrgUnitListByType(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
		//model.addAttribute("brSchools", bchSchools);		
	
		condition.put("orderBy", orderBy);
		condition.put("orderType", orderType);
		return "/edu3/recruit/enroll/enrolleeinfo-list";
	}

	/**
	 * 网上报名
	 * 
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping(value = { "/edu3/recruit/enroll/enrolleeinfo-add.html",	"/portal/site/service/enrolleeinfo-web.html" })
	public String addEnrolleeInfo(HttpServletRequest request, ModelMap model) throws WebException {
		
		try {
				String brSchoolId 					 = StringUtils.trimToEmpty(request.getParameter("uid"));//默认学习中心
				String url  						 = request.getRequestURI();
				//String type 						 = url.substring(url.lastIndexOf("/"));
				
				List<RecruitPlan> publishedPlanList  = recruitPlanService.getPublishedPlanList("fromWeb");//开放的招生批次
								
				model.addAttribute("enrolleeType", 1L);//报名方式为网上报名
				if (StringUtils.isNotEmpty(brSchoolId)) {//指定学习中心报名
					OrgUnit branschool				 = orgUnitService.get(brSchoolId);
					
					getBrschoolDefaultRecruitPlanAndMajor(branschool.getResourceid(),publishedPlanList,model);
					
					model.addAttribute("branschool", branschool);
					model.addAttribute("planList", publishedPlanList);					
					model.addAttribute("enrolleeTitle", "校外学中心网上报名");
					
					return "/edu3/recruit/enroll/enrolleeinfo-brschool-add";
				}
						
					
				model.addAttribute("publishedPlanList", publishedPlanList);
				model.addAttribute("brschoolId",brSchoolId);					
				model.addAttribute("enrolleeTitle", "网上报名");
				
			} catch (Exception e) {
				logger.error("网上报名-获取开放的招生批次出错：{}",e.fillInStackTrace());
			}
			
			return "/edu3/recruit/enroll/enrolleeinfo-add";
	}
	
	
	/**
	 * 指写学习中习打开报名页面时，显示一个默认的批次及对应的专业
	 * @param brSchoolId
	 * @param publishedPlanList
	 * @param model
	 */
	private void getBrschoolDefaultRecruitPlanAndMajor(String brSchoolId, List<RecruitPlan> publishedPlanList,ModelMap model){
		
		Map<String,RecruitMajor> rmjMap  = new HashMap<String,RecruitMajor>(); //申报通过的专业
		List<RecruitMajor> defaultPlanRMJ= new ArrayList<RecruitMajor>();      //随机默认批次的跟申报通过专业的交集
		Map<String,Object> condition 	 = new HashMap<String, Object>();
		
	
		String gradeId                   = "";
		RecruitPlan    defaultPlan       = null;
		String defaultPlanId             = "";
		
		for (RecruitPlan plan:publishedPlanList) {

			if ("Y".equals(plan.getIsSpecial()) && StringUtils.isEmpty(defaultPlanId)) {
				if (StringUtils.isNotEmpty(plan.getBrSchoolIds()) && plan.getBrSchoolIds().indexOf(brSchoolId)!=-1) {
					defaultPlanId = plan.getResourceid();
				}
			}else {
				if (StringUtils.isEmpty(defaultPlanId)) {
					defaultPlanId = plan.getResourceid();
				}
			}
		}
		
		if (StringUtils.isNotEmpty(defaultPlanId)) {
			defaultPlan = recruitPlanService.get(defaultPlanId);
			gradeId     = defaultPlan.getGrade().getResourceid();
		}
		
		condition.put("brSchool", brSchoolId);	
		condition.put("grade",gradeId);
		//获取随机的默认批次对应年级下学习中心所有申报通过的校外学习中心批次
		List<BranchSchoolPlan> branchSchoolPlanList   = branchSchoolPlanService.getAuditedBrsPlanListForBranSchool(condition);
		if (null!=branchSchoolPlanList && !branchSchoolPlanList.isEmpty() ) {
			for (BranchSchoolPlan brschoolPlan:branchSchoolPlanList) {//校外学习中心申报的招生批次
				
				Set<BranchSchoolMajor> bsmSet         = brschoolPlan.getBranchSchoolMajor();
				//将通过审核的申报专业放入集合中
				for (BranchSchoolMajor bsm :bsmSet) {
					rmjMap.put(bsm.getRecruitMajor().getResourceid(),bsm.getRecruitMajor());
				}
			}
		}
		
		if (!rmjMap.isEmpty()) {
			Set<RecruitMajor> rmSet 		  = defaultPlan.getRecruitMajor();
			for (RecruitMajor rm1:rmSet) {
				String teachingType1          = null==rm1.getTeachingType()?"":rm1.getTeachingType();			 //选择的招生批次专业的办学模式
				String classic1               = rm1.getClassic().getResourceid();							     //选择的招生批次专业的层次
				String major1                 = rm1.getMajor().getResourceid();  								 //选择的招生批次专业的专业      
				for (RecruitMajor rm2:rmjMap.values()) {
					String teachingType2      = null==rm2.getTeachingType()?"":rm2.getTeachingType();			 //学习中心申报的专业的办学模式
					String classic2           = rm2.getClassic().getResourceid();								 //学习中心申报的专业的层次
					String major2             = rm2.getMajor().getResourceid();  								 //学习中心申报的专业的专业      
					//当申报通过的专业跟选择的招生批次专业层次、办学模式、基础专业都相同时则表示该学习中心有这个招生专业的权限
					if (teachingType1.equals(teachingType2) & classic1.equals(classic2) & major1.equals(major2)) {
						defaultPlanRMJ.add( rm1);
					}
				}
			}
		
		}
		model.addAttribute("defaultPlanMajor", defaultPlanRMJ);
		model.addAttribute("defaultPlanId", defaultPlanId);
	}
	
	/**
	 * 保存网上报名信息
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/enroll/webenrolleeinfo-save.html")
	public String saveWebEnrolleeInfo(String resourceid,HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException {
		
		EnrolleeInfo ei     = null;
		StudentBaseInfo sbi = null;
		if (StringUtils.isNotEmpty(resourceid)) {
			ei              = enrolleeInfoService.get(resourceid);
			sbi   			= ei.getStudentBaseInfo();
		} else {
			ei 			    = new EnrolleeInfo();
			sbi 			= new StudentBaseInfo();
			ei.setSignupFlag(Constants.BOOLEAN_WAIT);  //报名资格-待审核
			ei.setNoExamFlag(Constants.BOOLEAN_WAIT);  //免试资格-待审核
			ei.setEntranceflag(Constants.BOOLEAN_WAIT);//入学资格-待审核
		}
		// 设置参数
		try {
			//MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
			//setRequestParameter(multipartRequest, response, ei, sbi);
			setRequestParameter(request, ei, sbi);
			
			
			// 生成考生号
			if (StringUtils.isEmpty(ei.getEnrolleeCode())) {
				String enrolleeCode = enrolleeInfoService.genEnrolleeCode(ei);
				ei.setEnrolleeCode(enrolleeCode);
			}
			
			/*studentService.saveOrUpdate(sbi);
			ei.setStudentBaseInfo(sbi);
			enrolleeInfoService.saveOrUpdate(ei);*/
			enrolleeInfoService.saveOrUpdateEnrolleeinfo(sbi, ei);
			
			model.addAttribute("results", "success");
			model.addAttribute("backurl", "/portal/index.html");
			
		} catch (Exception e) {
		
			if (e.getCause() instanceof ConstraintViolationException) {
				model.addAttribute("message", "保存失败:<br/>准考证号重复，请重新提交！");
			}else {
				model.addAttribute("message", "保存失败:<br/>"+e.getMessage());
			}
			model.addAttribute("results", "failure");
			model.addAttribute("backurl","/portal/service/student/service.html?url=/portal/site/service/enrolleeinfo-web.html");
		}
		return "/system/base/messageview";
	}

	/**
	 * 修改学生报名信息表单
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/recruit/enroll/enrolleeinfo-edit.html")
	public String editEnrolleeInfo(String resourceid,HttpServletRequest request, HttpServletResponse response,	ModelMap model) throws Exception {

		User user 				   = SpringSecurityHelper.getCurrentUser();
		List<RecruitPlan> planList = recruitPlanService.getPublishedPlanList("");
		List<RecruitPlan> alowplan = new ArrayList<RecruitPlan>();	
		//----------------------------------------------------修改报名信息----------------------------------------------------
		if (StringUtils.isNotEmpty(resourceid)) {
			
			EnrolleeInfo ei = enrolleeInfoService.get(resourceid);
			model.addAttribute("enrolleeinfo", ei);
			model.addAttribute("student", ei.getStudentBaseInfo());
			model.addAttribute("resourceid", resourceid);
			
			Set<RecruitMajor>  rmSet       = ei.getRecruitMajor().getRecruitPlan().getRecruitMajor();
			Map<String,RecruitMajor> rmMap = new HashMap<String, RecruitMajor>();
			List<BranchSchoolMajor> rmList = branchSchoolMajorService.findByHql("  from "+BranchSchoolMajor.class.getSimpleName()+" brm where brm.isDeleted=0 and brm.isPassed='Y' and brm.branchSchoolPlan.branchSchool.resourceid=? and brm.recruitMajor.recruitPlan.grade.resourceid=?",ei.getBranchSchool().getResourceid(), ei.getRecruitMajor().getRecruitPlan().getGrade().getResourceid());
			
			for (RecruitMajor rm : rmSet) {
				
				String classicId1    = rm.getClassic().getResourceid();
				String majorId1      = rm.getMajor().getResourceid();
				String teachingType1 = rm.getTeachingType();
				
				for (BranchSchoolMajor brm : rmList) {
					String classicId2    = brm.getRecruitMajor().getClassic().getResourceid();
					String majorId2      = brm.getRecruitMajor().getMajor().getResourceid();
					String teachingType2 = brm.getRecruitMajor().getTeachingType();
					if (teachingType1.equals(teachingType2) && classicId1.equals(classicId2) && majorId1.equals(majorId2)) {
						rmMap.put(classicId1+majorId1+teachingType1, rm);
					}
				}
			}
			List<RecruitMajor> rms    = new ArrayList<RecruitMajor>(rmMap.values());
			ComparatorRecruitMajor crm    	  = new  ComparatorRecruitMajor();
			if (null!=rmMap.values()) {
				Collections.sort(rms, crm);
			}
			model.addAttribute("enrolleeType", 0);//只要是在后台的报名信息列表中选择了修改则该学生就变成现场报名
			model.addAttribute("enrolleeTitle", ei.getEnrolleeType().equals(0L) ? "现场报名" : "网上报名");
			model.addAttribute("rmList",rms);
			// 设置籍贯默认参数
			if (StringUtils.isNotEmpty(ei.getStudentBaseInfo().getHomePlace())) {
				
				String[] homePlace = ei.getStudentBaseInfo().getHomePlace().split(",");
				if (homePlace != null && homePlace.length > 2) {
					model.addAttribute("homePlace_province", homePlace[0]);
					model.addAttribute("homePlace_city", homePlace[1]);
					model.addAttribute("homePlace_county", homePlace[2]);
				}
			}
			// 设置户口默认参数
			if (StringUtils.isNotEmpty(ei.getStudentBaseInfo().getResidence())) {
				String[] residence = ei.getStudentBaseInfo().getResidence().split(",");
				if (residence != null && residence.length > 2) {
					model.addAttribute("residence_province", residence[0]);
					model.addAttribute("residence_city", residence[1]);
					model.addAttribute("residence_county", residence[2]);
				}
			}
			if(null == ei.getSignupDate()){ ei.setSignupDate(new Date());}
			model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(ei.getSignupDate()));//设置附件上传自动创建目录
			
			
		//----------------------------------------------------新增现场报名----------------------------------------------------
		}else{
			model.addAttribute("enrolleeinfoEditForm","Y");
			model.addAttribute("enrolleeType", 0);
			model.addAttribute("enrolleeTitle", "现场报名");
			//默认省区
			model.addAttribute("homePlace_province", "广东省");
			model.addAttribute("homePlace_city", "广州市");
			model.addAttribute("homePlace_county", "天河区");
			//默认省区
			model.addAttribute("residence_province", "广东省");			
			model.addAttribute("residence_city", "广州市");
			model.addAttribute("residence_county", "天河区");
			model.addAttribute("storeDir", ExDateUtils.DATE_PATH_FORMAT.format(new Date()));//设置附件上传自动创建目录
			
			model.addAttribute("act", "create");//新增保存成功后，弹出新的页面录入下一个
		}
		
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {	
			model.addAttribute("isBrSchool", "Y");
			model.addAttribute("branchSchoolid", user.getOrgUnit().getResourceid());//当前校外学习中心			
			getBrschoolDefaultRecruitPlanAndMajor(user.getOrgUnit().getResourceid(),planList,model);
			
			//过滤掉选定学习中心的特批数据
			for (RecruitPlan plan :planList) {
				boolean isAllow           = false;
				//招生批次学习模式不为空，指定了学习中心，且当前在指定范围内，允许申报
				if (StringUtils.isNotEmpty(plan.getTeachingType()) & StringUtils.isNotEmpty(plan.getBrSchoolIds())) {
					String brSchoolIds    = plan.getBrSchoolIds();
					if (brSchoolIds.indexOf(user.getOrgUnit().getResourceid())>=0){
						alowplan.add(plan);
					}
				
				//招生批次学习模式不为空，没有指定学习中心，则当前用户具有的办学模式在批次中，允许申报
				}else if (StringUtils.isNotEmpty(plan.getTeachingType()) & !StringUtils.isNotEmpty(plan.getBrSchoolIds())) {
					
					if (StringUtils.isNotEmpty(user.getOrgUnit().getSchoolType())) {
						
					  String[] userSchoolTypes = user.getOrgUnit().getSchoolType().split(",");
					  
					  for (int j = 0; j < userSchoolTypes.length; j++) {
						String type			   = userSchoolTypes[j];
						if (plan.getTeachingType().indexOf(type)>=0){      
							isAllow            = true;
							break;
						}
					  }
					}else {
						isAllow            	   = false;
					}
					
					if (isAllow) {
						alowplan.add(plan);
					}
				//招生批次学习模式为空，没有指定学习中心，默认对所有学习中心开放	
				}else {
					alowplan.add(plan);
				}
			}
		}
		
		model.addAttribute("publishedPlanList",alowplan.size()>0?alowplan:planList);
		model.addAttribute("rootUrl", request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()
						+ CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());//拿到附件映射的全局URL
		
		if (null == planList || planList.isEmpty()) {
			model.addAttribute("hasPublishedPlan",false);
		}
		
		//如果是学习中心用户跳转到校外学习中心现场报名表单(编辑情况除外)
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {	
			model.addAttribute("isBrSchool", "Y");
			model.addAttribute("branchSchoolid", user.getOrgUnit().getResourceid());//当前校外学习中心			
			getBrschoolDefaultRecruitPlanAndMajor(user.getOrgUnit().getResourceid(),planList,model);
		
			
		}	
		
		
		return "/edu3/recruit/enroll/enrolleeinfo-form";
	}

	/**
	 * 保存修改报名信息
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/enroll/saveenrolleeinfo.html")
	public void saveEnrolleeInfo(String resourceid, HttpServletRequest request,	HttpServletResponse response, ModelMap model) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
	
		long oldEnrolleeType   = 0L;
		EnrolleeInfo ei        = null;
		StudentBaseInfo sbi    = null;
		
		String act = StringUtils.trimToEmpty(request.getParameter("act"));//用来判断是否新创建表单
		
		//修改
		if (StringUtils.isNotEmpty(resourceid)) {
			ei  		       = enrolleeInfoService.get(resourceid);
			oldEnrolleeType    = ei.getEnrolleeType();
			sbi 			   = ei.getStudentBaseInfo();
		
		//新增
		} else {
			ei 				   = new EnrolleeInfo();
			sbi 			   = new StudentBaseInfo();
			ei.setSignupFlag(Constants.BOOLEAN_WAIT);  //报名资格-待审核
			ei.setNoExamFlag(Constants.BOOLEAN_WAIT);  //免试资格-待审核
			ei.setEntranceflag(Constants.BOOLEAN_WAIT);//入学资格-待审核
		}

		// 设置参数
		try {
			//setRequestParameter(request, response, ei, sbi);

			//生成考生号
			//if (StringUtils.isEmpty(ei.getEnrolleeCode())) {
				//String enrolleeCode = enrolleeInfoService.genEnrolleeCode(ei);
				//ei.setEnrolleeCode(enrolleeCode);
			//}
			//studentService.saveOrUpdate(sbi);
			//ei.setStudentBaseInfo(sbi);
			//enrolleeInfoService.saveOrUpdate(ei);
			//System.out.println("----------:"+Thread.currentThread().getId()+"||"+Thread.currentThread().getName());
			

			setRequestParameter(request, ei, sbi);
			/*studentService.saveOrUpdate(sbi);
			ei.setStudentBaseInfo(sbi);
			enrolleeInfoService.saveOrUpdate(ei);*/
			
			enrolleeInfoService.saveOrUpdateEnrolleeinfo(sbi, ei);
			
			map.put("statusCode", 200);
			map.put("navTabId", "RES_RECRUIT_ENROLLEE_EDIT");
			if("create".equals(act)){
				map.put("message", "保存成功！<br/>请继续录入下一个报名信息！");
				map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/enroll/enrolleeinfo-edit.html" );
			}else{
				map.put("message","保存成功！");
				map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/enroll/enrolleeinfo-edit.html?resourceid="+ei.getResourceid() );
			}		
		
		} catch (Exception e) {			
			logger.error("保存学生报名信息失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			if (e.getCause() instanceof ConstraintViolationException) {
				map.put("message", "保存失败:<br/>准考证号重复，请重新提交！");
			}else {
				map.put("message", "保存失败:<br/>"+e.getMessage());
			}
			
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	private void setRequestParameter(HttpServletRequest request,EnrolleeInfo ei, StudentBaseInfo student)throws WebException{
		
		String recruitMajor    = StringUtils.trimToEmpty(request.getParameter("recruitMajor"));//招生专业
		String branchSchool    = StringUtils.trimToEmpty(request.getParameter("branchSchool"));//校外学习中心
	
		boolean isExists	   = studentService.checkExistsByIdCardNum(StringUtils.trimToEmpty(request.getParameter("certNum")));
		
		if (isExists&&StringUtils.isEmpty(ei.getResourceid())) {
			throw new ServiceException("证件号码已存在!");
		}
		
		String examCertificateNo  = StringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		if (StringUtils.isEmpty(examCertificateNo) || 												//新报名的情况下
		   (null!= ei.getRecruitMajor() && !ei.getRecruitMajor().getResourceid().equals(recruitMajor))||//修改专业的情况下
		   (null!=ei.getBranchSchool()) && !ei.getBranchSchool().getResourceid().equals(branchSchool)   //修改学习中心的情况下
		){
			examCertificateNo     = genExamCertificateNo(recruitMajor, branchSchool);
		}
		ei.setExamCertificateNo(examCertificateNo);
    	EnrolleeInfo enrolleeInfo = enrolleeInfoService.findUnique("from "+EnrolleeInfo.class.getSimpleName()+" ei where ei.isDeleted =? and ei.examCertificateNo = ?", 0,ei.getExamCertificateNo());
    	if (null != enrolleeInfo&&StringUtils.isEmpty(ei.getExamCertificateNo())) {
    		throw new WebException("保存报名信息出错，发现重复的准考证！<br/>请重新提交一次！");
		}
    	//修改专业的情况下
		if (StringUtils.isNotEmpty(ei.getResourceid())&& !ei.getRecruitMajor().getResourceid().equals(recruitMajor)) {
//			Set<ExamScore> set      = ei.getExamScore();
//			for (ExamScore score:set) {
//				Double orignalPoint = score.getOriginalPoint();
//				if (null!=orignalPoint) {
//					throw new WebException("成绩不为空，不允许修改专业！");
//				}
//			}
			//List<ExamRoomPlan> seatList = examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" plan where plan.isDeleted=0 and plan.recruitPlanId=? and plan.enrolleeinfo.resourceid=?", ei.getRecruitMajor().getRecruitPlan().getResourceid(),ei.getResourceid());
			//if (null!=seatList && seatList.size()>0) {
			//	throw new WebException("已安排座位，不允许修改专业！");
			//}
		}
    	
		//~~~~~~~~~~~~~~~~~~~~设置学生基本信息
		// 职称职务
		student.setTitle(StringUtils.trimToEmpty(request.getParameter("title")));
		// 姓名
		student.setName(StringUtils.trimToEmpty(request.getParameter("name")));
		// #性别
		student.setGender(StringUtils.trimToEmpty(request.getParameter("gender")));
		// #证件类型
		student.setCertType(StringUtils.trimToEmpty(request.getParameter("certType")));
		// 证件号码
		student.setCertNum(StringUtils.trimToEmpty(request.getParameter("certNum")));
		try {
			// 出生日期
			student.setBornDay(ExDateUtils.parseDate(StringUtils.trimToEmpty(request.getParameter("bornDay")), ExDateUtils.PATTREN_DATE));
		} catch (Exception e) {
			logger.error("保存报名信息设置设置出生年月出错：{}"+e.fillInStackTrace());
		}
		
		// 籍贯
		student.setHomePlace(StringUtils.trimToEmpty(request.getParameter("homePlace")));
		// 户口所在地
		student.setResidence(StringUtils.trimToEmpty(request.getParameter("residence")));
		// #民族
		student.setNation(StringUtils.trimToEmpty(request.getParameter("nation")));
		// #政治面貌
		student.setPolitics(StringUtils.trimToEmpty(request.getParameter("politics")));
		// #婚姻状况
		student.setMarriage(StringUtils.trimToEmpty(request.getParameter("marriage")));
		// 单位名称
		student.setOfficeName(StringUtils.trimToEmpty(request.getParameter("officeName")));
		// 职业状态
		 student.setWorkStatus(StringUtils.trimToEmpty(request.getParameter("workStatus")));
		//行业类别
		 student.setIndustryType(StringUtils.trimToEmpty(request.getParameter("industryType")));
		// 通讯地址
		student.setContactAddress(StringUtils.trimToEmpty(request.getParameter("contactAddress")));
		// 通讯邮编
		student.setContactZipcode(StringUtils.trimToEmpty(request.getParameter("contactZipcode")));
		// 联系电话
		student.setContactPhone(StringUtils.trimToEmpty(request.getParameter("contactPhone")));
		// 手机
		student.setMobile(StringUtils.trimToEmpty(request.getParameter("mobile")));
		// 电子邮件
		student.setEmail(StringUtils.trimToEmpty(request.getParameter("email")));
		// 照片路径
		student.setPhotoPath(StringUtils.trimToEmpty(request.getParameter("photoPath")));
		//身份证
		student.setCertPhotoPath(StringUtils.trimToEmpty(request.getParameter("certPhotoPath")));
		//毕业证
		student.setEduPhotoPath(StringUtils.trimToEmpty(request.getParameter("eduPhotoPath")));
		// 备注
		student.setMemo(StringUtils.trimToEmpty(request.getParameter("memo")));

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
			RecruitPlan plan   = major.getRecruitPlan();
			Date curDate       = new Date ();
			Date endDate       = plan.getEndDate();
			endDate            = ExDateUtils.addDays(endDate,1);
			if (curDate.getTime()>=endDate.getTime() && StringUtils.isEmpty(ei.getResourceid())) {
				throw new ServiceException(plan.getRecruitPlanname()+",报名时间已结束!");
			}
			//if (null==major.getExamSubject() || major.getExamSubject().size()<=0) {
			//	throw new ServiceException( major.getRecruitMajorName()+"</br>未设置考试科目，请与招生办联系！");
			//}
			ei.setRecruitMajor(major);
		 	ei.setTeachingType(major.getTeachingType());
		 	ei.setGrade(plan.getGrade());
		 	//是否特殊批次
		 	if(StringUtils.isNotEmpty(major.getRecruitPlan().getIsSpecial())){
		 		ei.setIsSpeciallycode(major.getRecruitPlan().getIsSpecial());
		 	}
		 	//if (null!=major.getExamSubject() && major.getExamSubject().size()>0) {
			//	ExamSubject subject=(ExamSubject)major.getExamSubject().toArray()[0];
			//	ei.setCourseGroupName(subject.getCourseGroupName());
		//	}
		}
		// 学习中心
		if (StringUtils.isNotEmpty(branchSchool)) {
			OrgUnit school = orgUnitService.get(branchSchool);
			ei.setBranchSchool(school);
		}
		

		// 报名类型：0-现场报名;1-网上报名
		ei.setEnrolleeType(Long.valueOf(StringUtils.trimToEmpty(request.getParameter("enrolleeType"))));
		// 默认的学习方式是"业余"
		ei.setStutyMode("3");		
		// 报名日期
		ei.setSignupDate(ExDateUtils.getCurrentDateTime());
		// 是否免试
		ei.setIsApplyNoexam(StringUtils.trimToEmpty(request.getParameter("isApplyNoexam")));
		// #最后学历
		ei.setEducationalLevel(StringUtils.trimToEmpty(request.getParameter("educationalLevel")));
		// 毕业院校
		ei.setGraduateSchool(StringUtils.trimToEmpty(request.getParameter("graduateSchool")));
		// 毕业学校代码
		ei.setGraduateSchoolCode(StringUtils.trimToEmpty(request.getParameter("graduateSchoolCode")));
		// 毕业专业
		ei.setGraduateMajor(StringUtils.trimToEmpty(request.getParameter("graduateMajor")));
		// 毕业证书号
		ei.setGraduateId(StringUtils.trimToEmpty(request.getParameter("graduateId")));
		try{
			// 毕业时间
			ei.setGraduateDate(ExDateUtils.parseDate(StringUtils.trimToEmpty(request.getParameter("graduateDate")), ExDateUtils.PATTREN_DATE));
		} catch (Exception e) {
			logger.error("保存报名信息设置设置毕业时间出错：{}"+e.fillInStackTrace());
		}
		// #媒体来源
		ei.setFromMedia(StringUtils.trimToEmpty(request.getParameter("fromMedia")));
		// 其他媒体来源
		ei.setFromOtherMedia(StringUtils.trimToEmpty(request.getParameter("fromOtherMedia")));
		
		
		// 生成考生号
		if (StringUtils.isEmpty(ei.getEnrolleeCode())) {
			String enrolleeCode = enrolleeInfoService.genEnrolleeCode(ei);
			ei.setEnrolleeCode(enrolleeCode);
		}

		if (StringUtils.isEmpty(examCertificateNo) ||StringUtils.isEmpty(recruitMajor) ||StringUtils.isEmpty(branchSchool)) {
			throw new WebException("准考证号生成有误,招生专业、学习中心不能为空!");
		}
    
	}
	/*private void setRequestParameter(HttpServletRequest request,HttpServletResponse response, EnrolleeInfo ei,	StudentBaseInfo student) throws Exception {
		
		boolean isExists	  = studentService.checkExistsByIdCardNum(ExStringUtils.trimToEmpty(request.getParameter("certNum")));
		
		if (isExists&&ExStringUtils.isEmpty(ei.getResourceid())) {
			throw new WebException("证件号码已存在!");
		}
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
		// 出生日期
		student.setBornDay(ExDateUtils.parseDate(ExStringUtils.trimToEmpty(request.getParameter("bornDay")), ExDateUtils.PATTREN_DATE));
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

		String recruitMajor    = ExStringUtils.trimToEmpty(request.getParameter("recruitMajor"));//招生专业
		String branchSchool    = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//校外学习中心
		//String isSpeciallycode = ExStringUtils.trimToEmpty(request.getParameter("isSpeciallycode"));//是否特殊批次
		
		
		// 招生专业
		if (StringUtils.isNotEmpty(recruitMajor)) {
			
			RecruitMajor major = recruitMajorService.get(recruitMajor);
			RecruitPlan plan   = major.getRecruitPlan();
			Date curDate       = new Date ();
			Date endDate       = plan.getEndDate();
			endDate            = ExDateUtils.addDays(endDate,1);
			if (curDate.getTime()>=endDate.getTime()) {
				//throw new Exception(plan.getRecruitPlanname()+",报名时间已结束!");
			}
			if (null==major.getExamSubject() || major.getExamSubject().size()<=0) {
				throw new Exception(major.getRecruitMajorName()+"</br>未设置考试科目，请与招生办联系！");
			}
			ei.setRecruitMajor(major);
		 	ei.setTeachingType(major.getTeachingType());
		 	ei.setGrade(plan.getGrade());
		 	//是否特殊批次
		 	if(ExStringUtils.isNotEmpty(major.getRecruitPlan().getIsSpecial())){
		 		ei.setIsSpeciallycode(major.getRecruitPlan().getIsSpecial());
		 	}
		 	if (null!=major.getExamSubject() && major.getExamSubject().size()>0) {
				ExamSubject subject=(ExamSubject)major.getExamSubject().toArray()[0];
				ei.setCourseGroupName(subject.getCourseGroupName());
			}
		}
		// 学习中心
		if (StringUtils.isNotEmpty(branchSchool)) {
			OrgUnit school = orgUnitService.get(branchSchool);
			ei.setBranchSchool(school);
		}
		//是否特殊批次
		if (ExStringUtils.isNotEmpty(isSpeciallycode)) {
			ei.setIsSpeciallycode(isSpeciallycode);
		}
		// 准考证号
		String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));

		
		// 报名类型：0-现场报名;1-网上报名
		ei.setEnrolleeType(Long.valueOf(ExStringUtils.trimToEmpty(request.getParameter("enrolleeType"))));
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
		// 毕业时间
		ei.setGraduateDate(ExDateUtils.parseDate(ExStringUtils.trimToEmpty(request.getParameter("graduateDate")), ExDateUtils.PATTREN_DATE));
		// #媒体来源
		ei.setFromMedia(ExStringUtils.trimToEmpty(request.getParameter("fromMedia")));
		// 其他媒体来源
		ei.setFromOtherMedia(ExStringUtils.trimToEmpty(request.getParameter("fromOtherMedia")));

	}*/
	/**
	 * 转发到首页
	 * 
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/portal/site/service/webenrolleeinfo-message.html")
	public String forwardPortal(ModelMap model) throws WebException {
		model.addAttribute("massage", "报名成功！系统已经处理您的报名请求！");
		model.addAttribute("backurl","/portal/site/service/enrolleeinfo-web.html");
		return "/system/base/messageview";
	}

	/**
	 * 删除学生报名信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/enroll/delenrolleeinfo.html")
	public void delete(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			if (!StringUtils.isEmpty(resourceid)) {
				if (resourceid.split(",").length > 1) {// 批量删除
					enrolleeInfoService.batchDelete(resourceid.split(","));
				} else {// 单个删除
					EnrolleeInfo entity     = enrolleeInfoService.get(resourceid);
					boolean isAllow         =  true;
 					if (isAllow) {
 						enrolleeInfoService.delete(entity);
					}else {
						throw new WebException(entity.getStudentBaseInfo().getName()+",资格审核通过或已安排座位或成绩不为空，不允许删除!");
					}
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("forward", request.getContextPath()+ "/edu3/recruit/enroll/enrolleeinfo-list.html");
			}
		} catch (Exception e) {
			logger.error("删除学生报名信息失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除学生报名信息失败:</br>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 给已注册过的学生申报新的专业-表单
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/recruit/enroll/reenrolleeinfo.html")
	public String reEnrolleeInfo(HttpServletRequest request, ModelMap model)throws Exception {
		
		String resourceid 			  			 = StringUtils.trimToEmpty(request.getParameter("resourceid"));
		String act 								 = StringUtils.trimToEmpty(request.getParameter("act"));
		if (StringUtils.isNotEmpty(resourceid)) {
			
			EnrolleeInfo enrolleeinfo 			 = enrolleeInfoService.get(resourceid);
			List<RecruitPlan> publishedPlanList  = recruitPlanService.getPublishedPlanList("");//开放的招生批次
			publishedPlanList.remove(enrolleeinfo.getRecruitMajor().getRecruitPlan());
			model.addAttribute("publishedPlanList", publishedPlanList);
			model.addAttribute("enrolleeinfo", enrolleeinfo);
			if("addNewMajor".equals(act)){//如果是老生报新专业
				model.addAttribute("newMajorForm", "Y");//老生报读新专业默认不显示准考证号
			}
			
		}
		User user 				    			 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			model.addAttribute("brSchoolId", user.getOrgUnit().getResourceid());			
		}
		if(user.isContainRole("ROLE_RECRUIT_LEADER")||
		   user.isContainRole("ROLE_RECRUIT_DIRECTOR")||
		   user.isContainRole("ROLE_RECRUIT_MANAGER")){
		   model.addAttribute("isRecruitUser","Y");			
		}		
		
		return "/edu3/recruit/enroll/enrolleeinfo-reform";
	}

	/**
	 * 给已注册过的学生申报新的专业-表单
	 * @param enrolleeInfoId
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
//	@RequestMapping("/edu3/recruit/enroll/addNewMajor-edit.html")
//	public String addNewMajor(String enrolleeInfoId ,HttpServletRequest request,HttpServletResponse response, ModelMap model)throws Exception {
//		
//		String resourceid 		  		= request.getParameter("resourceid");
//		EnrolleeInfo enrolleeInfo 		= null;
//		String stuId 			  		= "";
//		List<RecruitPlan> publishedPlan = recruitPlanService.getPublishedPlanList("");
//	
//		if (ExStringUtils.isNotEmpty(enrolleeInfoId)) {
//			enrolleeInfo 		  		= enrolleeInfoService.get(enrolleeInfoId);
//
//		}
//		User user 				     = SpringSecurityHelper.getCurrentUser();
//		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
//			model.put("brSchoolId", user.getOrgUnit().getResourceid());
//		}
//		model.put("newMajorForm", "Y");//老生报读新专业默认不显示准考证号
//		model.put("enrolleeInfo",enrolleeInfo);
//		model.put("publishedPlanList", publishedPlan);
//		
//		return "/edu3/recruit/enroll/enrolleeinfo-addNewMajor";
//	}
	
	/**
	 * 给已注册过的学生申报新的专业-保存
	 * @param request
	 * @param response
	 * @throws WebException
	 * @throws ParseException
	 */
//	@RequestMapping("/edu3/recruit/enroll/addNewMajor-save.html")
//	public void saveNewMajor(String resourceid,String studentId,HttpServletRequest request,HttpServletResponse response)throws WebException, ParseException {
//		
//		StudentBaseInfo studentBaseInfo = studentService.get(studentId);
//		EnrolleeInfo ei 				= new EnrolleeInfo();
//		Map<String,Object> map 			= new HashMap<String, Object>();
//		
//		if (studentBaseInfo!=null) {
//			//设置参数
//			try {
//				setEnrolleeInfoParameter(request,ei);
//				// 生成考生号
//				if (StringUtils.isEmpty(ei.getEnrolleeCode())) {
//					String enrolleeCode = enrolleeInfoService.genEnrolleeCode(ei);
//					ei.setEnrolleeCode(enrolleeCode);
//				}
//				ei.setStudentBaseInfo(studentBaseInfo);
//				enrolleeInfoService.saveOrUpdate(ei);
//				
//				map.put("statusCode", 200);
//				map.put("message", "保存成功");
//				map.put("navTabId", "RES_RECRUIT_ENROLLEE_LIST");
//				map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/enroll/enrolleeinfo-list.html");
//			} catch (Exception e) {
//				logger.error("保存学生申报新的专业出错:{}",e.fillInStackTrace());
//				map.put("statusCode", 300);
//				map.put("message", "保存学生申报新的专业出错:"+e.getMessage());
//			}
//		}else {
//				map.put("statusCode", 300);
//				map.put("message", "学生申报新的专业出错,该学生缺少基础信息，请与管理人员联系!");
//		}
//		renderJson(response, JsonUtils.mapToJson(map));
//		
//	}
	
	/**
	 * 保存重新报名信息
	 * 
	 * @param resourceid
	 * @param request
	 * @return
	 * @throws WebException
	 * @throws ParseException
	 */
	@RequestMapping("/edu3/recruit/enroll/resave.html")
	public void saveReEnrolleeInfo(String resourceid,	HttpServletRequest request, HttpServletResponse response)throws WebException, ParseException {
		
		Map<String, Object> map  = new HashMap<String, Object>();
		String studentId		 = StringUtils.trimToEmpty(request.getParameter("studentId"));
		
		try {
			
			EnrolleeInfo ei 	 = new EnrolleeInfo();
						
			setEnrolleeInfoParameter(request,ei,resourceid);				
			// 重新生成考生号
			if (StringUtils.isEmpty(ei.getEnrolleeCode())) {
				String enrolleeCode = enrolleeInfoService.genEnrolleeCode(ei);
				ei.setEnrolleeCode(enrolleeCode);
			}
		
			StudentBaseInfo studentBaseInfo = studentService.get(studentId);
			
			//设置重新报名的学生基本信息
			setRequestReBaseStudentParameter(request,studentBaseInfo);				
			ei.setStudentBaseInfo(studentBaseInfo);
			enrolleeInfoService.saveOrUpdate(ei);
				
			
			
			/*
			if (StringUtils.isNotEmpty(resourceid)) {//修改
				EnrolleeInfo ei 		  = enrolleeInfoService.get(resourceid);
				Date curDate 			  = new Date();		
				// 设置学生报名信息
				// 招生专业
				RecruitMajor major 		  = recruitMajorService.get(ExStringUtils.trimToEmpty(request.getParameter("recruitMajor")));
				
				RecruitPlan plan   		  = major.getRecruitPlan();
				Date endDate       		  = plan.getEndDate();
				endDate            		  = ExDateUtils.addDays(endDate,1);
				if (curDate.getTime()>=endDate.getTime()) {
					throw new Exception(plan.getRecruitPlanname()+",报名时间已结束!");
				}
				if ("Y".equals(ei.getSignupFlag())) {
					throw new Exception("该学生已通过报名资格审核，不允许重新报名！");
				}
				
				ei.setRecruitMajor(major);
				ei.setRecruitPlan(major.getRecruitPlan().getResourceid());
				ei.setRecruitplanDate(ExDateUtils.formatDate(curDate, ExDateUtils.PATTREN_DATE));
				ei.setGrade(major.getRecruitPlan().getGrade());
				
				if (null!=major.getExamSubject() && major.getExamSubject().size()>0) {
					ExamSubject subject=(ExamSubject)major.getExamSubject().toArray()[0];
					ei.setCourseGroupName(subject.getCourseGroupName());
				}
				
				// 学习中心				
				OrgUnit school 			  = orgUnitService.get(ExStringUtils.trimToEmpty(request.getParameter("branchSchool")));
				ei.setBranchSchool(school);
				//报名资格审核状态
				//ei.setSignupFlag(ei.getSignupFlag());
				// 准考证号
				ei.setExamCertificateNo(ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo")));
				// 默认的学习方式是"业余"
				//ei.setStutyMode("3");				
				// 报名日期
				ei.setSignupDate(ExDateUtils.formatDate(curDate, ExDateUtils.PATTREN_DATE));
				// 是否免试
				ei.setIsApplyNoexam(request.getParameter("isApplyNoexam"));
				// #最后学历
				ei.setEducationalLevel(request.getParameter("educationalLevel"));
				// 毕业院校
				ei.setGraduateSchool(request.getParameter("graduateSchool"));
				// 毕业学校代码
				ei.setGraduateSchoolCode(request.getParameter("graduateSchoolCode"));
				// 毕业专业
				ei.setGraduateMajor(request.getParameter("graduateMajor"));
				// 毕业证书号
				ei.setGraduateId(request.getParameter("graduateId"));
				// 毕业时间
				ei.setGraduateDate(ExDateUtils.parseDate(request.getParameter("graduateDate"),ExDateUtils.PATTREN_DATE));
				// #媒体来源
				ei.setFromMedia(request.getParameter("fromMedia"));
				// 其他媒体来源
				ei.setFromOtherMedia(request.getParameter("fromOtherMedia"));
				//报名类型设置为现现场报名
				ei.setEnrolleeType(0L);
				//设置所属的办学模式
				ei.setTeachingType(major.getTeachingType());
				//设置学生基本信息
				ei.setStudentBaseInfo(ei.getStudentBaseInfo());
				// 生成考生号
				if (StringUtils.isEmpty(ei.getEnrolleeCode())) {
					String enrolleeCode = enrolleeInfoService.genEnrolleeCode(ei);
					ei.setEnrolleeCode(enrolleeCode);
				}
				enrolleeInfoService.saveOrUpdate(ei);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_RECRUIT_ENROLLEE_LIST");
			}
			*/
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_RECRUIT_ENROLLEE_LIST");
			map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/enroll/enrolleeinfo-list.html");
		} catch (Exception e) {
			logger.error("重新报名失败:{}", e.fillInStackTrace());
			map.put("statusCode", 300);
			if (e.getCause() instanceof ConstraintViolationException) {
				map.put("message", "保存失败:<br/>准考证号重复，请重新提交！");
			}else {
				map.put("message", "保存失败:"+e.getMessage());
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 生成准考证号
	 * @param recruitMajorid
	 * @param branchSchoolid
	 * @return
	 */
	private String genExamCertificateNo(String recruitMajorid ,String branchSchoolid){
		RecruitMajor recruitMajor = recruitMajorService.get(recruitMajorid);
		OrgUnit branchSchool 	  = orgUnitService.get(branchSchoolid);
		// 获得前缀
		String prefix 			  = enrolleeInfoService.genExamCertificateNoPrefix(recruitMajor, branchSchool);
		// 获得流水号
		String suffix 			  = enrolleeInfoService.getExamCertificateNoSuffix(prefix);

		return prefix + suffix;
	}
	
	/*
	 * 设置重新报名的学生基本信息参数
	 */
	private void setRequestReBaseStudentParameter(HttpServletRequest request,StudentBaseInfo student) throws Exception{
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("name")))) {
			student.setName(StringUtils.trimToEmpty(request.getParameter("name")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("certType")))) {
			student.setCertType(StringUtils.trimToEmpty(request.getParameter("certType")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("certNum")))) {
			student.setCertNum(StringUtils.trimToEmpty(request.getParameter("certNum")));
		}
		
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("gender")))) {
			student.setGender(StringUtils.trimToEmpty(request.getParameter("gender")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("bornDay")))) {
			student.setBornDay(ExDateUtils.parseDate(StringUtils.trimToEmpty(request.getParameter("bornDay")), ExDateUtils.PATTREN_DATE));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("nation")))) {
			student.setNation(StringUtils.trimToEmpty(request.getParameter("nation")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("residence")))) {
			student.setResidence(StringUtils.trimToEmpty(request.getParameter("residence")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("marriage")))) {
			student.setMarriage(StringUtils.trimToEmpty(request.getParameter("marriage")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("title")))) {
			student.setTitle(StringUtils.trimToEmpty(request.getParameter("title")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("workStatus")))) {
			student.setWorkStatus(StringUtils.trimToEmpty(request.getParameter("workStatus")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("industryType")))) {
			student.setIndustryType(StringUtils.trimToEmpty(request.getParameter("industryType")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("officeName")))) {
			student.setOfficeName(StringUtils.trimToEmpty(request.getParameter("officeName")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("officePhone")))) {
			student.setOfficePhone(StringUtils.trimToEmpty(request.getParameter("officePhone")));
		}
		
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("contactAddress")))) {
			student.setContactAddress(StringUtils.trimToEmpty(request.getParameter("contactAddress")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("contactZipcode")))) {
			student.setContactZipcode(StringUtils.trimToEmpty(request.getParameter("contactZipcode")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("mobile")))) {
			student.setMobile(StringUtils.trimToEmpty(request.getParameter("mobile")));
		}
		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("contactPhone")))) {
			student.setContactPhone(StringUtils.trimToEmpty(request.getParameter("contactPhone")));
		}

		if(StringUtils.isNotEmpty(StringUtils.trimToEmpty(request.getParameter("email")))) {
			student.setEmail(StringUtils.trimToEmpty(request.getParameter("email")));
		}
	}
	
	

	/**
	 * 获得准考证号
	 * 
	 * @param recruitMajorid
	 * @param branchSchoolid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/get-examcode.html")
	public void getExamcode(String recruitMajorid, String branchSchoolid,HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException {

		if (StringUtils.isNotEmpty(recruitMajorid)&& StringUtils.isNotEmpty(branchSchoolid)) {
			
			/*RecruitMajor recruitMajor = recruitMajorService.get(recruitMajorid);
			OrgUnit branchSchool 	  = orgUnitService.get(branchSchoolid);
			// 获得前缀
			String prefix 			  = enrolleeInfoService.genExamCertificateNoPrefix(recruitMajor, branchSchool);
			// 获得流水号
			String suffix 			  = enrolleeInfoService.getExamCertificateNoSuffix(prefix);*/
			String examCertificateNo  = genExamCertificateNo(recruitMajorid,branchSchoolid);

			renderJson(response, JsonUtils.objectToJson(examCertificateNo));
		}
	}

	/**
	 * 验证身份证号
	 * 
	 * @param idNumber
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/checkidnumber.html")
	public void checkIdNumber(String idNumber, String certType,
			HttpServletResponse response) throws WebException {

		String msg = "[";
		if ("idcard".equals(certType)) {

			boolean isExists = studentService.checkExistsByIdCardNum(idNumber);
			if (isExists) {
				msg += "{\"result\":\"failure\",\"msg\":\"你的报名信息已存在，请到现场报名！\"}]";
			} else {
				msg += "{\"result\":\"success\",\"msg\":\"\"}]";
			}
			/*// 如果是15位的身份证号，则先把它升级成18位
			if (idNumber.length() == 15) {
				idNumber = new UpdateIDNumber().uptoeighteen(idNumber);
			}
			Checker checker = new Checker(idNumber);
			if (!checker.check()) {
				msg += "{\"result\":\"idcard\",\"msg\":\""
						+ checker.getErrorMsg() + "\"}";
			}*/
		}

		//msg += "{\"result\":\"success\",\"msg\":\"\"}]";
		renderText(response, msg);
	}

	/**
	 * 验证招生专业是否属于专什本层次
	 * 
	 * @param recruitMajorid
	 * @param graduateSchool
	 * @param graduateSchoolCode
	 * @param graduateId
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/checkclassic.html")
	public void checkClassic(String recruitMajorid, String graduateSchool,String graduateSchoolCode, String graduateId,HttpServletResponse response) throws WebException {
		String msg = "";
		if (StringUtils.isNotEmpty(recruitMajorid)) {
			RecruitMajor major = recruitMajorService.get(recruitMajorid);
			if ("2".equals(major.getClassic().getClassicCode())) {
				if (StringUtils.isEmpty(graduateSchool)) {
					msg += "入学前学历学校名称不能为空\n";
				}
				if (StringUtils.isEmpty(graduateSchoolCode)) {
					msg += "入学前学历学校代码不能为空\n";
				} else if (graduateSchoolCode.length() != 5) {
					msg += "入学前学历学校代码是毕业证书号的前五位\n";
				}
				if (StringUtils.isEmpty(graduateId)) {
					msg += "入学前学历证书编号不能为空\n";
				}
			}
		}
		renderText(response, msg);
	}

	/**
	 * 取出层次代码
	 * 
	 * @param recruitMajorid
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/getclassiccode.html")
	public void checkClassic(String recruitMajorid, HttpServletResponse response)
			throws WebException {
		if (StringUtils.isNotEmpty(recruitMajorid)) {
			RecruitMajor major = recruitMajorService.get(recruitMajorid);
			renderText(response, major.getClassic().getClassicCode());
		}
	}

	
	
	/**
	 * 给已注册过的学生申报新的专业-设置报名信息
	 * @param request
	 * @param ei
	 * @throws ParseException 
	 */
	
	private void setEnrolleeInfoParameter(HttpServletRequest request,EnrolleeInfo ei,String resourceid) throws Exception{
		
		EnrolleeInfo oldEnro = enrolleeInfoService.get(resourceid);
		
		//设置学生报名信息
		String recruitMajor = StringUtils.trimToEmpty(request.getParameter("recruitMajor"));
		String branchSchool = StringUtils.trimToEmpty(request.getParameter("branchSchool"));

		// 招生专业
		if (StringUtils.isNotEmpty(recruitMajor)) {
			RecruitMajor major = recruitMajorService.get(recruitMajor);
			RecruitPlan plan   = major.getRecruitPlan();
			List<String> status= new ArrayList<String>();
			status.add("11");//在学
			status.add("12");//休学
			status.add("21");//延期
			//2011.12.28朝上提出同一学生，同的招生批次只能有一个报名信息
			if (null!=oldEnro) {
				String hql = " from "+EnrolleeInfo.class.getSimpleName()+" en where en.isDeleted=? and en.recruitMajor.recruitPlan.resourceid=? and en.studentBaseInfo.resourceid=?";
				List<EnrolleeInfo> checkExistsEnrolleeInfo = enrolleeInfoService.findByHql(hql, 0,plan.getResourceid(),oldEnro.getStudentBaseInfo().getResourceid());
				if (checkExistsEnrolleeInfo.size()>0) {
					throw new WebException("所选招生批次已存在该用户的报名信息,如要修改信息请使用修改功能！");
				}
			}
			//检查是否存在同层次在学、休学、延期的学籍信息
			List<StudentInfo> list = studentinfoservice.findByCriteria(Restrictions.eq("studyNo",oldEnro.getMatriculateNoticeNo()),Restrictions.eq("classic.resourceid",major.getClassic().getResourceid()),Restrictions.eq("isDeleted",0),Restrictions.in("studentStatus",status));
			if (null!=list && !list.isEmpty()) {
				throw new WebException("系统中已存在：<font color='red'>"+oldEnro.getStudentBaseInfo().getName()+"</font>的学籍信息");
			}
			
			Date curDate       = new Date ();
			Date endDate       = plan.getEndDate();
			endDate            = ExDateUtils.addDays(endDate,1);
			if (curDate.getTime()>=endDate.getTime()) {
				throw new Exception(plan.getRecruitPlanname()+",报名时间已结束!");
			}
			//if (null==major.getExamSubject() || major.getExamSubject().size()<=0) {
			//	throw new Exception(major.getRecruitMajorName()+"</br>未设置考试科目，请与招生办联系！");
			//}
			ei.setRecruitMajor(major);
			ei.setGrade(plan.getGrade());
			ei.setTeachingType(major.getTeachingType());
			
			//if (null!=major.getExamSubject() && major.getExamSubject().size()>0) {
			//	ExamSubject subject=(ExamSubject)major.getExamSubject().toArray()[0];
			//	ei.setCourseGroupName(subject.getCourseGroupName());
			//}
			
			
		}
		// 学习中心
		if (StringUtils.isNotEmpty(branchSchool)) {
			OrgUnit school = orgUnitService.get(branchSchool);
			ei.setBranchSchool(school);
		}
		// 准考证号
		
		String examCertificateNo  = StringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		if (StringUtils.isEmpty(examCertificateNo)) {
			 examCertificateNo    = genExamCertificateNo(recruitMajor, branchSchool);
		}
	   
		if (StringUtils.isEmpty(examCertificateNo) ||StringUtils.isEmpty(recruitMajor) ||StringUtils.isEmpty(branchSchool)) {
			throw new WebException("准考证号生成有误,招生专业、学习中心不能为空!");
		}
		ei.setExamCertificateNo(examCertificateNo);
		
		EnrolleeInfo enrolleeInfo = enrolleeInfoService.findUnique("from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted = 0 and examCertificateNo = ?", ei.getExamCertificateNo());
    	if (null != enrolleeInfo) {
    		throw new WebException("保存报名信息出错，发现重复的准考证！<br/>请重新提交一次！");
		}
		
		// 报名类型：0-现场报名;1-网上报名
		ei.setEnrolleeType(0L);
		// 默认的学习方式是"业余"
		ei.setStutyMode("3");
		// 报名日期
		ei.setSignupDate(ExDateUtils.getCurrentDateTime());
		// 是否免试
		ei.setIsApplyNoexam(request.getParameter("isApplyNoexam"));
		// #最后学历
		ei.setEducationalLevel(request.getParameter("educationalLevel"));
		// 毕业院校
		ei.setGraduateSchool(request.getParameter("graduateSchool"));
		// 毕业学校代码
		ei.setGraduateSchoolCode(request.getParameter("graduateSchoolCode"));
		// 毕业专业
		ei.setGraduateMajor(request.getParameter("graduateMajor"));
		// 毕业证书号
		ei.setGraduateId(request.getParameter("graduateId"));
		// 毕业时间
		ei.setGraduateDate(ExDateUtils.parseDate(request.getParameter("graduateDate"), ExDateUtils.PATTREN_DATE));
		// #媒体来源
		ei.setFromMedia(request.getParameter("fromMedia"));
		// 其他媒体来源
		ei.setFromOtherMedia(request.getParameter("fromOtherMedia"));
		//设置为报名资格审核通过
		//ei.setSignupFlag("Y");
		ei.setSignupFlag(Constants.BOOLEAN_WAIT);  //报名资格-待审核
		ei.setNoExamFlag(Constants.BOOLEAN_WAIT);  //免试资格-待审核
		ei.setEntranceflag(Constants.BOOLEAN_WAIT);//入学资格-待审核
	
	}
	
	
	/**
	 * 检查所填后的证件号是否唯一
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/checkexistsuser.html")
	public void checkExistsUser(HttpServletRequest request ,HttpServletResponse response){
		//注册学籍时，用证件号作为用户的登录名，不区分证件类型要保证件号唯一
		String certNum        = StringUtils.trimToEmpty(request.getParameter("certNum"));
		boolean isExists	  = studentService.checkExistsByIdCardNum(certNum);
		
		renderJson(response, JsonUtils.objectToJson(isExists));
	}
	
	/**
	 * 打印报名汇总表-条件选择
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/enroll/print/statenrolleeinforeport-condition.html")
	public String statEnrolleeInfoReportPrintCondition(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		User user 				   = SpringSecurityHelper.getCurrentUser();
		List<OrgUnit> brSchoolList = orgUnitService.findOrgUnitListByType(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			brSchoolList.clear();
			OrgUnit u 			   = orgUnitService.get(user.getOrgUnit().getResourceid());
			brSchoolList.add(u);
		}	
		model.addAttribute("brSchoolList", brSchoolList);
		return "/edu3/recruit/enroll/print-statenrolleeinforeport-condition";
	}
	/**
	 * 打印报名汇总表-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/enroll/print/statenrolleeinforeport-view.html")
	public String statEnrolleeInfoReportPrintView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		model.addAttribute("recruitPlan",StringUtils.trimToEmpty(request.getParameter("recruitPlan")));
		model.addAttribute("classic",StringUtils.trimToEmpty(request.getParameter("classic")));
		model.addAttribute("major",StringUtils.trimToEmpty(request.getParameter("major")));
		model.addAttribute("brSchool",StringUtils.trimToEmpty(request.getParameter("brSchool")));
		
		return "/edu3/recruit/enroll/print-statenrolleeinforeport-printview";
	}
	/**
	 * 打印报名汇总表-打印
	 * @param request
	 * @param response
	 * @param model
	 */
	@RequestMapping("/edu3/recruit/enroll/print/statenrolleeinforeport.html")
	public void statEnrolleeInfoReportPrint(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		Map<String,Object> map = new HashMap<String, Object>();
		JasperPrint jasperPrint= null;//输出的报表
		String recruitPlanName = "";
		String recruitPlan 	   = StringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String classic 		   = StringUtils.trimToEmpty(request.getParameter("classic"));
		String major 		   = StringUtils.trimToEmpty(request.getParameter("major"));
		String brSchool 	   = StringUtils.trimToEmpty(request.getParameter("brSchool"));
		//如果是学习中心用户，只操作本学习中心的数据
		User user              = SpringSecurityHelper.getCurrentUser();
		
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			brSchool =	user.getOrgUnit().getResourceid();
		}
	
	    if (StringUtils.isNotEmpty(classic)) {
			map.put("classic", classic);
		}
	    if (StringUtils.isNotEmpty(major)) {
			map.put("major", major);
		}
	    if (StringUtils.isNotEmpty(brSchool)& !"null".equals(brSchool) &!"undefined".equals(brSchool)){    
	    	map.put("brSchool", brSchool);
	    }
		
	    if (StringUtils.isNotEmpty(recruitPlan)){ 
	    	map.put("recruitPlan", recruitPlan);
	    	recruitPlanName        = recruitPlanService.load(recruitPlan).getRecruitPlanname();
	    }
	   // Connection conn            = null;
	    try {	    	
	    	String reprotFile     = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
	    							File.separator+"recruitPrint"+File.separator+"statEnrollReport_crosstab.jasper"),	"utf-8");
			//File reprot_file      = new File(reprotFile);
			List<String> pIds     = new ArrayList<String>(); 
			List<Map<String, Object>> brSchoolIds = getCurrentConditionsBranSchoolId(map);
			map.put("recruitPlanName", recruitPlanName);
			map.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
			if (null!=brSchoolIds && !brSchoolIds.isEmpty()) {
				
				int flag 		  = 0;  //报表填充标识  当值为0时创建报表 大于0时在已创建的报表对象中添加页面 
				String idsStr     = ""; //学习中心ID
				
				for (int i = 0; i <brSchoolIds.size(); i++) {
					Map m  = brSchoolIds.get(i);
					pIds.add(m.get("RESOURCEID").toString());
				}
				
				while (pIds.size()>=12) {
					
					
					List<String> ids = pIds.subList(0, 12);
					for (int i = 0; i < ids.size(); i++) {
						idsStr   +=",'"+ids.get(i)+"'";
					}
					map.put("units", idsStr.substring(1));
					idsStr        = "";
					if (flag==0) {
						//jasperPrint = JasperFillManager.fillReport(reprotFile, map, conn); // 填充报表
						jasperPrint = enrolleeInfoService.printReport(reprotFile, map, enrolleeInfoService.getConn());
					}else {
						//JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, map, conn); // 填充报表
						JasperPrint jasperPage = enrolleeInfoService.printReport(reprotFile, map, enrolleeInfoService.getConn());
						List jsperPageList=jasperPage.getPages();
						for (int j = 0; j < jsperPageList.size(); j++) {
							jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
						}
						jasperPage = null;//清除临时报表的内存占用
					}
					
					pIds.subList(0, 12).clear();
					flag +=1;
				}
				
				if (pIds.size()>0) {
					for (int i = 0; i < pIds.size(); i++) {
						idsStr   +=",'"+pIds.get(i)+"'";
					}
					map.put("units", idsStr.substring(1));
					idsStr        = "";
					if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
						//JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, map, conn); // 填充报表
						JasperPrint jasperPage = enrolleeInfoService.printReport(reprotFile, map, enrolleeInfoService.getConn());
						List jsperPageList=jasperPage.getPages();
						for (int j = 0; j < jsperPageList.size(); j++) {
							jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
						}
						jasperPage = null;//清除临时报表的内存占用
					}else {
						//jasperPrint = JasperFillManager.fillReport(reprotFile, map, conn); // 填充报表
						jasperPrint = enrolleeInfoService.printReport(reprotFile, map, enrolleeInfoService.getConn());
					}
				}
				
			}

			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			} 

		} catch (Exception e) {
			logger.error("打印报名汇总表出错：{}"+e.fillInStackTrace());
			renderHtml(response, "打印报名汇总表出错!"+e.fillInStackTrace());
		}
		
	}
	/**
	 * 导出报名汇总表-条件选择
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/recruit/enroll/export/statenrolleeinfo-condition.html")
	public String statEnrolleeInfoReportDownloadCondition(HttpServletRequest request,ModelMap model) throws Exception{
		   
		User user 				   = SpringSecurityHelper.getCurrentUser();
		List<OrgUnit> brSchoolList = orgUnitService.findOrgUnitListByType(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			brSchoolList.clear();
			OrgUnit u 			   = orgUnitService.get(user.getOrgUnit().getResourceid());
			brSchoolList.add(u);
		}
		List<RecruitPlan>  planList= recruitPlanService.getPublishedPlanList("");
		
		model.addAttribute("planList",planList);
		model.addAttribute("brSchoolList", brSchoolList);
		
		return "/edu3/recruit/enroll/export-statenrolleeinforeport-condition";
	}
	/**
	 * 导出报名汇总表-导出
	 * @param request
	 */
	@RequestMapping("/edu3/recruit/enroll/export/statenrolleeinfo.html")
	public void statEnrolleeInfoReportDownload(HttpServletRequest request,HttpServletResponse response){
	
		Map<String,Object> map = new HashMap<String, Object>();
		JasperPrint jasperPrint= null;//输出的报表
	
		String ids             = "";
		String recruitPlan     = StringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String brSchool        = StringUtils.trimToEmpty(request.getParameter("brSchool")); 

		String classic 		   = StringUtils.trimToEmpty(request.getParameter("classic"));
		String major 		   = StringUtils.trimToEmpty(request.getParameter("major"));
		String auditStatus     = StringUtils.trimToEmpty(request.getParameter("auditStatus"));
		
		//如果是学习中心用户，只操作本学习中心的数据
		User user              = SpringSecurityHelper.getCurrentUser();
		//Connection conn        = null;
	    if (StringUtils.isNotEmpty(classic)) {
			map.put("classic", classic);
		}
	    if (StringUtils.isNotEmpty(major)) {
			map.put("major", major);
		}
	    if (StringUtils.isNotEmpty(recruitPlan)){
	    	ids = "";
	    	for (String s:recruitPlan.split(",")) {
	    		ids   += ",'"+s+"'";
			}
	    	map.put("recruitPlan",ids.substring(1));
	    }
	    if (StringUtils.isNotEmpty(brSchool)){    
	    	ids = "";
	    	for (String s :brSchool.split(",")) {
	    		ids   += ",'"+s+"'";
			}
	    	map.put("units", " d.resourceid in("+ids.substring(1)+")");
	    }
	    //如果是学习中心用户只统计当前学习中心情数据
	    if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
	    	map.put("units", " d.resourceid in('"+user.getOrgUnit().getResourceid()+"')");
		}
	    //如果没选择学习中心，传入1=1  使用jasperreport模板导出时动态传入SQL
	    if (!map.containsKey("units")) {
	    	map.put("units"," 1=1 ");
		}
	    if (StringUtils.isNotEmpty(auditStatus)) {
	    	map.put("auditStatus",auditStatus);
		}else {
			map.put("auditStatus"," 1=1 ");
		}
	    try {
	    
	    	//版本一、使用jasperreport模板导出
	    	//conn                    = enrolleeInfoService.getConn();
	    	String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
	    							  File.separator+"recruitPrint"+File.separator+"statEnrollReport_crosstab_export.jasper"),"utf-8");
			File reprot_file        = new File(reprotFile);
			GUIDUtils.init();			
			String filePath         = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
			
			//jasperPrint 		    = JasperFillManager.fillReport(reprot_file.getPath(), map, conn); // 填充报表
			jasperPrint 			= enrolleeInfoService.printReport(reprot_file.getPath(), map, enrolleeInfoService.getConn());
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);  
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
            exporter.setParameter(JRXlsAbstractExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);  
            exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,filePath); 
            //exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.xls"); 
            exporter.exportReport(); 

            downloadFile(response,"报名信息汇总表.xlsx",filePath,true);
            //downloadFile(response,"报名汇总表.xlsx","D:\\aa.xls",false);
            
		} catch (Exception e) {
			logger.error("导出报名汇总表出错：{}"+e.fillInStackTrace());
		}
		
	}
	/**
	 * 查询报名信息
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	private List<Map<String,Object>> queryEnrolleeInfoList(Map<String, Object> condition) throws Exception{
		
		StringBuffer sql    = new StringBuffer(" select d.unitcode UNITCODE,d.unitshortname as UNITNAME,c.CLASSICNAME as CLASSICNAME,e.MAJORNAME as MAJORNAME ,	count( a.resourceid ) counts ");
		sql.append("  from  EDU_RECRUIT_ENROLLEEINFO a ,EDU_RECRUIT_MAJOR b ,EDU_BASE_CLASSIC c, HNJK_SYS_UNIT d,EDU_BASE_MAJOR e");
		sql.append(" where  a.RECRUITMAJORID = b.RESOURCEID");
		sql.append("   and  a.isdeleted      = 0 ");
		sql.append("   and  b.isdeleted      = 0 ");
		sql.append("   and  b.MAJORID        = e.RESOURCEID ");
		sql.append("   and b.CLASSIC         = c.RESOURCEID ");
		sql.append("   and  a.BRANCHSCHOOLID = d.RESOURCEID ");
		sql.append("   and  a.signupflag     = 'Y'");
		if (condition.containsKey("recruitPlan")) {
			sql.append(" and  b.RECRUITPLANID in("+condition.get("recruitPlan")+")");
		}
		if (condition.containsKey("units")) {
			sql.append(" and  "+condition.get("units"));
		}
		if (condition.containsKey("classic")) {
			sql.append(" and  c.RESOURCEID =:classic");
		}
		if (condition.containsKey("major")) {
			sql.append(" and  b.MAJORID = :major");
		}
		sql.append(" group by d.unitcode ,d.unitshortname,c.CLASSICNAME,e.MAJORNAME ");
		sql.append(" order  by c.classicname ");
		
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), condition);
	}

	private List<Map<String, Object>> getCurrentConditionsBranSchoolId(Map<String,Object> map) throws Exception{
		
		StringBuffer sql = new StringBuffer();
		sql.append("select  distinct(d.resourceid) from  EDU_RECRUIT_ENROLLEEINFO a ,EDU_RECRUIT_MAJOR b ,EDU_BASE_CLASSIC c, ");
		sql.append(" HNJK_SYS_UNIT d,EDU_BASE_MAJOR e   where  a.isdeleted = 0 and  a.RECRUITMAJORID = b.RESOURCEID ");
		sql.append(" and  b.MAJORID = e.RESOURCEID  and  b.CLASSIC = c.RESOURCEID and  a.BRANCHSCHOOLID = d.RESOURCEID");
		sql.append(" and a.SIGNUPFLAG ='Y'");
		
		if (map.containsKey("recruitPlan") ) {
			sql.append(" and b.RECRUITPLANID =:recruitPlan");
		}
		if (map.containsKey("classic") ) {
			sql.append(" and c.RESOURCEID =:classic");
		}
		if (map.containsKey("major") ) {
			sql.append(" and b.MAJORID =:major");
		}
		if (map.containsKey("brSchool") ) {
			sql.append(" and d.resourceid=:brSchool");
		}
		return baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(),map);
	}

	/**
	 * 打印报名名单--选择条件窗口
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/enroll/printexamexcused-condition.html")
	public String printExamFormCondition(HttpServletRequest request,ModelMap model){
		return "/edu3/recruit/enroll/printexamExcused-condition";
	}
	/**
	 * 打印报名申请名单-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/enroll/printexamexcused-view.html")
	public String printExamExcusedListView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		//logger.error("${baseUrl}/edu3/recruit/enroll/printexamexcused-view.html?recruitPlan="+request.getParameter("recruitPlan")+"&major="+request.getParameter("major")+"&classic="+request.getParameter("classic")+"&name="+request.getParameter("name")+"&teacheType="+request.getParameter("teachingType")+"&enrollType="+request.getParameter("enrollType")+"&certNum="+request.getParameter("certNum"));
		model.addAttribute("info",StringUtils.trimToEmpty(request.getParameter("info")));
		model.addAttribute("recruitPlan",StringUtils.trimToEmpty(request.getParameter("recruitPlan")));
		model.addAttribute("enrolleeinfo", StringUtils.trimToEmpty(request.getParameter("enrolleeinfoid")));
		model.addAttribute("major",StringUtils.trimToEmpty(request.getParameter("major")));
		model.addAttribute("classic",StringUtils.trimToEmpty(request.getParameter("classic")));
		model.addAttribute("teachingType", StringUtils.trimToEmpty(request.getParameter("teachingType")));
		model.addAttribute("enrollType",StringUtils.trimToEmpty(request.getParameter("enrollType")));
		model.addAttribute("certNum",StringUtils.trimToEmpty(request.getParameter("certNum")));
		
		return "/edu3/recruit/enroll/examExcusedList-printview";
	}
	
	/**
	 * 打印报名申请名单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/recruit/enroll/printExamExcusedList.html")
	public void printExamExcusedList(HttpServletRequest request,HttpServletResponse response){
		//logger.warn("${baseUrl}/edu3/recruit/enroll/printexamexcused-view.html?recruitPlan="+request.getParameter("recruitPlan")+"&major="+request.getParameter("major")+"&classic="+request.getParameter("classic")+"&name="+request.getParameter("name")+"&teacheType="+request.getParameter("teachingType")+"&enrollType="+request.getParameter("enrollType")+"&certNum="+request.getParameter("certNum"));
		String recruitPlan	=	StringUtils.trimToEmpty(request.getParameter("recruitPlan")); 
		String major		=	StringUtils.trimToEmpty(request.getParameter("major"));
		String classic		=	StringUtils.trimToEmpty(request.getParameter("classic"));
		String teachingType	=	StringUtils.trimToEmpty(request.getParameter("teachingType"));
		String enrollType	=	StringUtils.trimToEmpty(request.getParameter("enrollType"));
		String certNum		=	StringUtils.trimToEmpty(request.getParameter("certNum"));
		String enrolleeinfo =   StringUtils.trimToEmpty(request.getParameter("enrolleeinfo"));
		String basicPath    =	ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath")+"common"+File.separator+"students";
		Map<String,Object> param=new HashMap<String, Object>();
		if(StringUtils.isNotEmpty(recruitPlan)){
			RecruitPlan recruitPlan2 = recruitPlanService.get(recruitPlan);
			param.put("recruitPlanName", recruitPlan2.getRecruitPlanname());
		}else{
			EnrolleeInfo enrolleeInfo2 = enrolleeInfoService.get(enrolleeinfo);
			RecruitPlan recruitPlan2 = recruitPlanService.get(enrolleeInfo2.getRecruitMajor().getRecruitPlan().getResourceid());
			String planname = recruitPlan2.getRecruitPlanname();
			param.put("recruitPlanName", planname);
		}
		JasperPrint jasperPrint=null;
		//Connection conn 	   = null;
		try {		
			param.put("recruitPlan", recruitPlan.isEmpty()?" 1=1 ":"rm.recruitplanid='"+recruitPlan+"'");
			param.put("major",major.isEmpty()?" 1=1 ":"rm.majorID='"+major+"'");
			param.put("classic",classic.isEmpty()?" 1=1 ": "rm.classic='"+classic+"'");
			param.put("enrollType",enrollType.isEmpty()?" 1=1 ": "i.enrolleetype="+enrollType+" ");
			param.put("teachingType",teachingType.isEmpty()?" 1=1 ": "i.teachingType='"+teachingType+"'");
			param.put("certNum",certNum.isEmpty()?" 1=1 ": "certNum='"+certNum+"'");
			param.put("enrolleeinfo", enrolleeinfo.isEmpty()?" 1=1 ": "i.resourceid='"+enrolleeinfo+"'");
			param.put("basicPath",basicPath);
			param.put("SUBREPORT_DIR", "//");
			param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				
			//String img_Path=new File(URLDecoder.decode(request.getSession().getServletContext().getRealPath("images"+File.separator+"default_icon.jpg"),"utf-8")).getPath();
			//param.put("defaultPath",img_Path);
			 //conn 	   		 = enrolleeInfoService.getConn();
			String reportfile=URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"recruitPrint"+File.separator+"recruitReport_crosstab.jasper"),"utf-8");
			File report_file =new File(reportfile);
			//jasperPrint=JasperFillManager.fillReport(report_file.getPath(), param,conn);	
			jasperPrint = enrolleeInfoService.printReport(report_file.getPath(), param, enrolleeInfoService.getConn());
			if(null!=jasperPrint){
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response, "缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印免试申请名单失败{}",e.fillInStackTrace());
			renderHtml(response, "打印免试申请名单失败。");
		}
	}
	/**
	 * 录取时报名信息查看
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/enroll/matriculate-enrolleeinfo.html")
	public String viewEnrolleeInfoListForMatriculate(HttpServletRequest request,ModelMap model,Page page){
		
		Map<String,Object> param = new HashMap<String, Object>();
		
		String recruitPlanId     = StringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
		String recruitMajorId    = StringUtils.trimToEmpty(request.getParameter("recruitMajorId"));
		String brSchoolId        = StringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		String courseGroup       = StringUtils.trimToEmpty(request.getParameter("courseGroup"));
		String viewFlag          = StringUtils.trimToEmpty(request.getParameter("viewFlag"));
		
		if (StringUtils.isNotEmpty(recruitPlanId)){     param.put("recruitPlanId", recruitPlanId);}
		if (StringUtils.isNotEmpty(recruitMajorId)){    param.put("recruitMajorId", recruitMajorId);}
		if (StringUtils.isNotEmpty(brSchoolId)){        param.put("brSchoolId", brSchoolId);}
		if (StringUtils.isNotEmpty(courseGroup)){       param.put("courseGroup", courseGroup);}
		if (StringUtils.isNotEmpty(viewFlag)){          param.put("viewFlag", viewFlag);}
		
		
		
		
		return "";
	}
	
	/**
	 * 跳转图片采集页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/recruit/enroll/enrolleeinfo-webcam.html")
	public String getWebcamPage(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String enrolleeinfoId = StringUtils.trimToEmpty(request.getParameter("enrolleeinfoId"));//学生ID
		if(StringUtils.isNotBlank(enrolleeinfoId)){
			EnrolleeInfo ei = enrolleeInfoService.get(enrolleeinfoId);
			if(null == ei.getSignupDate()){ ei.setSignupDate(new Date());}
			model.addAttribute("storePath", ExDateUtils.DATE_PATH_FORMAT.format(ei.getSignupDate()));//设置附件上传自动创建目录
			model.addAttribute("id",ei.getResourceid());
			model.addAttribute("replaceName",ei.getStudentBaseInfo().getCertNum()+"_photo");
			model.addAttribute("studentName",ei.getStudentBaseInfo().getName());
		}
		
		return "/edu3/recruit/enroll/enrolleeinfo-webcam";
	}
	
	/**
	 * 学生图像采集
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/enrolleeinfo/photo/upload.html")
	public void webcamForEnrolleeInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String storePath   		 = StringUtils.trimToEmpty(request.getParameter("storePath"));				
		String replaceName 		 = StringUtils.trimToEmpty(request.getParameter("replaceName"));//获取替换的别名
		String clientkey 		= StringUtils.trimToEmpty(request.getParameter("clientkey"));//客户端校验KEY
		String id 				= StringUtils.trimToEmpty(request.getParameter("id"));//学生报名信息ID
		EnrolleeInfo  enrolleeInfo = null;
		try{
			if(!clientkey.equals(BaseSecurityCodeUtils.getMD5("WWW.HNJK.NET"))){//校验客户端
				renderText(response, "501");//非法请求
				return;
			}
			
			if(StringUtils.isBlank(id)){
				renderText(response, "502");//无效学生ID
				return;
			}
			enrolleeInfo  = enrolleeInfoService.get(id);
			if(null == enrolleeInfo){
				renderText(response, "503");//无效学生信息
				return;
			}
			
		}catch (Exception e) {
		}
		//设置文件存储路径
		String databaseUrl = "";
		if(StringUtils.isNotEmpty(storePath)){
			//String distPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() ;
			String distPath = ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath");
			String[] arr = storePath.split(",");
			databaseUrl = arr[arr.length-1];
			for(int i=0;i<arr.length;i++){
				distPath += arr[i]+File.separator;				
			}
			this.setDistfilepath(distPath);
		}
		//替换存储文件名
		if(StringUtils.isNotEmpty(replaceName)){
			Map<String, String> replaceMap = new HashMap<String, String>(0);
			replaceMap.put("Filedata", replaceName);//表单域
			this.setReplaceNameMap(replaceMap);
		}else{
			this.setReplaceNameMap(null);
		}
		
		Attachs attachs = null;
		
		//存储
		try{
			List<AttachInfo> list = doUploadFile(request, response,null);//上传
			if(null != list && list.size()>0){
				AttachInfo attachInfo = list.get(0);				
				attachs = new Attachs();
				ExBeanUtils.copyProperties(attachs, attachInfo);	
				//更新学生基本信息
				StudentBaseInfo baseInfo = enrolleeInfo.getStudentBaseInfo();
				baseInfo.setPhotoPath("/"+databaseUrl+"/"+attachs.getSerName());
				enrolleeInfoService.update(enrolleeInfo);
			}
		
		}catch (Exception e) {
			logger.error("采集学生图片出错:"+e.fillInStackTrace());
			renderText(response,"500");//上传失败
		}
			
		renderText(response,"200");//上传成功		
		
	}
	
	/**
	 * 录取查询
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */	
	@RequestMapping("/cx.html")
	public String studentMatriculateQuery(HttpServletRequest request,ModelMap model) throws Exception {
		long start = System.currentTimeMillis();
		Map<String,Object> condition  = new HashMap<String, Object>();
		User user                     = SpringSecurityHelper.getCurrentUser();
		String fromFlag               = StringUtils.trimToEmpty(request.getParameter("fromFlag"));//
		String enrolleeCode  	  	  = StringUtils.trimToEmpty(request.getParameter("enrolleeCode"));// 准考号
		String certNum 				  = StringUtils.trimToEmpty(request.getParameter("certNum"));	// 证件号码
		String studentName 			  = StringUtils.trimToEmpty(request.getParameter("studentName"));	// 姓名
		String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		String phoneComfirm = CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue();
		String firstOpen = StringUtils.trimToEmpty(request.getParameter("firstOpen"));
		String ruturnPath = "/edu3/recruit/matriculate/studentmatriculatequery-list";
		model.addAttribute("phoneComfirm",phoneComfirm);
		model.addAttribute("school",school);
		model.addAttribute("schoolCode",schoolCode);
		if(ExStringUtils.isEmpty(firstOpen)){
			return ruturnPath;
		}
		String message 			  = "没有查到相关录取信息";
		//StringUtils.isNotEmpty(enrolleeCode)&&
		// 身份证号为空验证
		if (StringUtils.isNotEmpty(certNum)) {
			certNum = ExStringUtils.upperCase(certNum);
			condition.put("certNum", certNum);
			condition.put("isDeleted", 0);
			//condition.put("enrolleeCode", enrolleeCode);
		}else {
			model.addAttribute("message","请输入身份号码！");
		}
		
		// 姓名为空验证
		if (StringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}else {
			model.addAttribute("message","请输入姓名！");
		}
		EnrolleeInfo ei = null;
		if(StringUtils.isNotEmpty(certNum) && StringUtils.isNotEmpty(studentName) ){
			condition.put("isDeleted", 0);
			StringBuffer hql = new StringBuffer(" from "+EnrolleeInfo.class.getSimpleName()+" ere  join fetch  ere.recruitMajor "
					+ " major join fetch major.recruitPlan  where ere.isDeleted=:isDeleted and ere.studentBaseInfo.certNum=:certNum "
					+ " and ere.studentBaseInfo.name=:studentName ");
			
			if (StringUtils.isNotEmpty(enrolleeCode)) {
				condition.put("enrolleeCode", enrolleeCode);
				hql.append(" and ere.enrolleeCode=:enrolleeCode ");
			}
			List<EnrolleeInfo> list   = enrolleeInfoService.findByHql(hql.toString(),condition);
			condition.put("enrolleeCode", enrolleeCode);
			if (list.size() == 0) {
				message = "抱歉，此人未被录取，请进一步核查信息。";
			} else if (list.size() >= 1) {
				//判断是否在录取查询时间范围内
				List<EnrolleeInfo> removeList = new ArrayList<EnrolleeInfo>();
				for (EnrolleeInfo enrolleeInfo : list) {
					boolean flag = isInRangeTime(enrolleeInfo);
					if(!flag) {
						removeList.add(enrolleeInfo);
					}
				}
				list.removeAll(removeList);
				if(list.size()>1){
					message = "由于出现重名的情况，请输入你的考生号或者身份证号再继续查询！";
				}else if(list.size()==1){
					ei = list.get(0);
				}else {
					message = "录取查询未开放，或已过录取查询时间！";
				}
			}
			if(ei!=null){
				ei.setIsQuery("Y");
				if (Constants.BOOLEAN_YES.equals(StringUtils.trimToEmpty(ei.getIsMatriculate()))) {
					message = "恭喜，您已被我校录取！" ;
					if(!("10600".equals(schoolCode) || "10602".equals(schoolCode))){
						List<Map<String,Object>> fee = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select t.recpayfee from edu_roll_fistudentfee t where t.studentid = ? and t.chargeyear = ? ", new Object[]{ei.getMatriculateNoticeNo(),ExDateUtils.getCurrentYear()});
						if (null!=fee && !fee.isEmpty()) {
							model.addAttribute("fee",fee.get(0));
						}

						//根据KSH（因为考生号各地不一样，有机会重复，须加上招生计划，精确查询）
					
						List<Map<String,Object>> fee1 = baseSupportJdbcDao.getBaseJdbcTemplate().findForList("select e.RESOURCEID,e.XZ from EDU_RECRUIT_EXAMINEE e where e.ZKZH=? and e.recruitplanid =? ", 
								new Object[]{ei.getExamCertificateNo(), ei.getRecruitMajor().getRecruitPlan().getResourceid()});
						
						if (null!=fee1 && !fee1.isEmpty()) {
							model.addAttribute("fee1",fee1.get(0));
							//添加45开头的学生查询条件
							updateInputUnit1(fee1.get(0).get("RESOURCEID").toString(),model,certNum);
						}
						
						/*for (int i=1;i <6 ;i++) {
						  String de=CacheAppManager.getSysConfigurationByCode("AdmissionDocument"+String.valueOf(i))!=null?CacheAppManager.getSysConfigurationByCode("AdmissionDocument"+String.valueOf(i)).getParamValue():"";
						  if(de==null){
							  break;
						  }
						  model.addAttribute("AdmissionDocument"+String.valueOf(i),de);
					  }*/
					}
					
					RecruitMajor rm = ei.getRecruitMajor();
		            Major m = rm.getMajor();
					 String zy = m.getMajorName().replace("专科", "").replace("本科", "").replace("高起专","").replace("专升本", "");
					 ei.getRecruitMajor().getMajor().setMajorName(zy);
					model.addAttribute("ei", ei);
					model.addAttribute("schoolName", ei.getBranchSchool()==null?"":ei.getBranchSchool().getUnitName());
				} else if (Constants.BOOLEAN_NO.equals(StringUtils.trimToEmpty(ei.getIsMatriculate()))) {
					message = "抱歉，您未被我校录取！";
				}
				enrolleeInfoService.update(ei);
			}
		
		  try {
			  EnrolleeInfo eil=(EnrolleeInfo)model.get("ei");
			  if(eil != null){						
//				  String baseid=eil.getStudentBaseInfo().getResourceid();
//				  List<Map<String,Object>> gr = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select Gender from edu_base_student  where resourceid=? ", new Object[]{baseid});
				  
				  String path= request.getSession().getServletContext().getRealPath(File.separator+"TheAdmissionNotice"+File.separator+"background.jpg");
				  
				/*  String sex="";
				  if(gr.size()!=0){
					  String sexx=(String) gr.get(0).get("Gender");
					  sex =  sexx.equals("1")?"男":"女";
				  }*/
				  String sexx=(String) eil.getStudentBaseInfo().getGender();
				  String sex = "1".equals(sexx) ?"男":"女";
				  String sheng=CacheAppManager.getSysConfigurationByCode("School.province").getParamValue();
				  //eil=enrolleeInfoService.get(eil.getResourceid());
//				  RecruitMajor maj=recruitMajorService.get(eil.getRecruitMajor().getResourceid());
//				  RecruitPlan pla=recruitPlanService.get(maj.getRecruitPlan().getResourceid());
				  RecruitPlan pla = eil.getRecruitMajor().getRecruitPlan();
				  Date time = pla.getReportTime();
				  SimpleDateFormat df=new SimpleDateFormat("yyyy年MM月dd日");
				  String tt = "";
				  if(time!=null){
					  tt=df.format(time);
				  }
				  Date end =pla.getEndTime();
				  String endt = "";
				  if(end!=null){
					  endt=df.format(end);
				  }
		           //读取模板图片内容
				  @Cleanup InputStream inputStream = new FileInputStream(path);
		           BufferedImage image = ImageIO.read(inputStream);
		           generateAdmissionNotice(request,model, schoolCode, eil, sex, sheng, tt, endt, image);
		            
			  }
          } catch(Exception e) {
             e.printStackTrace();
          }
			model.addAttribute("message", message);
			model.addAttribute("fromFlag",fromFlag);
			model.addAttribute("condition",condition);
			model.addAttribute("currentUser", user);
		} else {
			message = "请输入姓名和身份证号码！";
			model.addAttribute("message", message);
		}
		long end = System.currentTimeMillis();
		System.out.println("录取查询页面"+(end-start));
		return ruturnPath;
	}

	/**
	 * 判断是否在录取查询时间范围内
	 * @param enrolleeInfo
	 * @return
	 */
	private boolean isInRangeTime(EnrolleeInfo enrolleeInfo) {
		Date statTime   = enrolleeInfo.getRecruitMajor().getRecruitPlan().getEnrollStartTime();
		Date endTime    = enrolleeInfo.getRecruitMajor().getRecruitPlan().getEnrollEndTime();
		Date currentTime= new Date();
		if (null!=endTime) {
			endTime = ExDateUtils.addDays(endTime, 1);
		}
		if (null!=statTime && null!=endTime) {	
			if (currentTime.getTime()>=statTime.getTime() && currentTime.getTime()<=endTime.getTime()) {
				return true;	
			}
		}
		return false;
	}

	/**暂不使用
	 * 实现将多个文件进行压缩，生成指定目录下的指定名字的压缩文件
	 * @param filename 指定生成的压缩文件的名称
	 * @param temp_path 指定生成的压缩文件所存放的目录
	 * @param filename 指定生成的压缩文件的名称
	 * @param list ：List集合：用于存放多个File（文件）
	 * @author Git
	 * 
	 */
	private static void createZip(String filename, String temp_path, List<File> list) {
		File file = new File(temp_path);
		File zipFile = new File(temp_path + File.separator + filename);

		try {
			ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
			zipOut.setEncoding("GBK");
			zipOut.setComment(file.getName());
			if (file.isDirectory()) {
				for (int i = 0; i < list.size(); ++i) {
					@Cleanup InputStream input = new FileInputStream(list.get(i));
					zipOut.putNextEntry(new ZipEntry(file.getName() + File.separator + list.get(i).getName()));
					int temp = 0;
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
					logger.info("[" + list.get(i).getName() + "] zip to File:[" + filename + "] success ");
				}
			}
			zipOut.close();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	/** 
	 * 生成录取通知书
	 * @param request
	 * @param model
	 * @param schoolCode
	 * @param eil
	 * @param sex
	 * @param sheng
	 * @param tt
	 * @param endt
	 * @param image
	 * @throws Exception 
	 * @author Git
	 */
	public void generateAdmissionNotice(HttpServletRequest request,ModelMap model, String schoolCode, EnrolleeInfo eil, String sex, String sheng,
			String tt, String endt, BufferedImage image) throws Exception {

		String cover = request.getSession().getServletContext()
				.getRealPath(File.separator + "TheAdmissionNotice" + File.separator + "cover.jpg");
		String seal = request.getSession().getServletContext()
				.getRealPath(File.separator + "TheAdmissionNotice" + File.separator + "seal.png");
		String year = eil.getRecruitMajor().getRecruitPlan().getYearInfo().getFirstYear()+"";
		String noticeSubDir = "Notice" + year;

		String outputDir = Constants.EDU3_DATAS_LOCALROOTPATH + File.separator + noticeSubDir;
		
        String theAdmissionNoticeSubDir = request.getSession().getServletContext()
				.getRealPath(File.separator + "TheAdmissionNotice" + File.separator + noticeSubDir);
        File theAdmissionNoticeSubDirFile = new File(theAdmissionNoticeSubDir);
        if(!theAdmissionNoticeSubDirFile.isDirectory()){
        	theAdmissionNoticeSubDirFile.mkdirs();
        }

		String no = theAdmissionNoticeSubDirFile.getAbsolutePath() +  File.separator + eil.getMatriculateNoticeNo() + ".jpg";		
        
        String noticeFile = "Notice" + eil.getMatriculateNoticeNo() + ".jpg";
        String notice = outputDir + File.separator + noticeFile;
		String noticePDF = "Notice" + eil.getMatriculateNoticeNo() + ".pdf";
		//String zipFile = "Notice" + eil.getMatriculateNoticeNo() + ".zip";
		String zipFile = eil.getStudentBaseInfo().getName() + year + ".zip";
		
		model.addAttribute("cover", cover);
		model.addAttribute("notice", notice);
		model.addAttribute("no", no);
		model.addAttribute("outputDir", outputDir);

		//页面下载
		model.addAttribute("noticeSubDir", noticeSubDir);
		model.addAttribute("zipFile", URLDecoder.decode(zipFile, "utf-8"));
		model.addAttribute("noticeFile", noticeFile);
		
		int sealX = 534, sealY = 291;
		Graphics2D admissionNotice2D = null;
		String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()
				+ CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
		if ("10598".equals(schoolCode)) {// 广西医科大
			admissionNotice2D = gxykdxAdmissionNotice(model, eil, image);
			sealX = 614;
			sealY = 982;
		}else if("11078".equals(schoolCode)){// 广州大学
			school = "广州大学成人高等教育新生入学通知书";
			admissionNotice2D = gzdxAdmissionNotice(model, school, eil, sex, sheng, tt, endt, image);
			sealX = 534;
			sealY = 291;
		}else if ("10600".equals(schoolCode)) {//广西中医药
			admissionNotice2D = gxzyyAdmissionNotice(model, eil, image);
			sealX = 614;
			sealY = 982;
			seal = null;
		}else if ("10602".equals(schoolCode)) {//广西师范
			admissionNotice2D = gxsfAdmissionNotice(model, eil, image);
			sealX = 614;
			sealY = 982;
			seal = null;
		} else if ("11846".equals(schoolCode)) {//广东外语 - 未提供模版
			seal = null;
			admissionNotice2D = ahykdxAdmissionNotice(model, school, eil, sex, sheng, tt, endt, image);
		} else {
			admissionNotice2D = ahykdxAdmissionNotice(model, school, eil, sex, sheng, tt, endt, image);
		}
		
		String formatName = no.substring(no.lastIndexOf(".") + 1);
		File imageFile = new File(no);
		if(imageFile.exists()){
			imageFile.delete();
			imageFile.createNewFile();  
		}else {
			imageFile.createNewFile();
		}
		ImageIO.write(image, formatName, imageFile);
		//盖章
		he(no, seal, no, sealX , sealY,schoolCode);
		List<Dictionary> dictionaries = CacheAppManager.getChildren("CodePackageOffer");
		if ("10598".equals(schoolCode) || "10600".equals(schoolCode) || "10602".equals(schoolCode)) {
			String rootPath = File.separator + "TheAdmissionNotice" + File.separator;
			List<String> fileList = new ArrayList<String>();
			for (Dictionary dictionary : dictionaries) {
				if(eil.getRecruitMajor().getClassic()!=null){
					if("专科".equals(eil.getRecruitMajor().getClassic().getClassicName()) || "高起专".equals(eil.getRecruitMajor().getClassic().getClassicName())){
						if(dictionary.getDictName().contains("本科") || dictionary.getDictName().contains("专升本")){
							continue;
						}
					}else {
						if(dictionary.getDictName().contains("专科") || dictionary.getDictName().contains("高起专")){
							continue;
						}
					}
				}
				
				if(ExStringUtils.isNotBlank(dictionary.getDictValue())){
					if(dictionary.getDictName().endsWith("录取通知书")){
						fileList.add(request.getSession().getServletContext().getRealPath(rootPath+dictionary.getDictValue()));
					}else {
						fileList.add(CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+rootPath+dictionary.getDictValue());
					}
				}
			}
			
	        File outputFile = new File(outputDir);
	        if(!outputFile.isDirectory()){
	        	outputFile.mkdirs();
	        }
	        
			//拼接
	        //暂时不用了，因为用pdf模版的方式一页一页加，方便打印
//			imageMosaic(notice, cover, no);
			
			// 转pdf 
			String jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					File.separator+"recruit"+File.separator+"notice.jasper"),"utf-8");
			
			Map<String, Object> mapCover = new HashMap<String,Object>();
			Map<String, Object> mapImage = new HashMap<String,Object>();
			mapImage.put("imagePath",no);
			mapCover.put("imagePath",cover);

			List<Map<String, Object>> _tmpCoverList = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> _tmpImageList = new ArrayList<Map<String, Object>>();
			_tmpCoverList.add(mapCover);
			_tmpImageList.add(mapImage);
			
			JRMapCollectionDataSource dataSourceCover = new JRMapCollectionDataSource(_tmpCoverList);
			JRMapCollectionDataSource dataSourceImage = new JRMapCollectionDataSource(_tmpImageList);

			JasperPrint jaspeImagerPrint = JasperFillManager.fillReport(jasperFile, mapImage,dataSourceImage); // 内容-填充报表
			JasperPrint jasperCoverPrint = JasperFillManager.fillReport(jasperFile, mapCover,dataSourceCover); // 封面-填充报表
			
			
			if (null != jasperCoverPrint && jaspeImagerPrint != null) {
				GUIDUtils.init();
				String pdfPath = outputDir + File.separator + noticePDF;
				
				@SuppressWarnings("unchecked")
				List<JRBasePrintPage> pages = jasperCoverPrint.getPages();
				for (JRBasePrintPage page : pages) {
					jaspeImagerPrint.addPage(page);
				}

				JRPdfExporter exporter = new JRPdfExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jaspeImagerPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, pdfPath);
				exporter.exportReport();
				
				fileList.add(pdfPath);
			}
			
//			_jasperPrint = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
//
//			//第一页为封面
//			if (count == 0) {
//				jasperPrint = _jasperPrint;
//				count++;
//			} else {//后面的页为内容
//				@SuppressWarnings("unchecked")
//				List<JRBasePrintPage> pages = (List<JRBasePrintPage>) _jasperPrint.getPages();
//				for (JRBasePrintPage page : pages) {
//					jasperPrint.addPage(page);
//				}
//			}
//			
			//打zip
			List<File> list = new ArrayList<File>();
			File tmpFile = null;
			for(String str: fileList){
				tmpFile = new File(str);
				if(tmpFile.exists()){
					list.add(tmpFile);
				}
			}
			//createZip(zipFile, outputDir, list);
			//20171201 开线程打包(使用线程池)
			ExecutorService cachedThreadPool = Executors.newFixedThreadPool(100);  
			cachedThreadPool.execute(new zipUtil(zipFile, outputDir, list));

			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PRINT, "学生下载录取通知书：学生身份证号："+eil.getStudentBaseInfo().getCertNum());
			/*zipUtil thread = new zipUtil(zipFile, outputDir, list);
			Thread t1=new Thread(thread);
			t1.start();*/
			
		}else if ("10366".equals(schoolCode)) {
			List<Dictionary> dictionaryList = new ArrayList<Dictionary>();
			for (Dictionary dictionary : dictionaries) {
				if(eil.getRecruitMajor().getClassic()!=null){
					if("专科".equals(eil.getRecruitMajor().getClassic().getClassicName()) || "高起专".equals(eil.getRecruitMajor().getClassic().getClassicName())){
						if(dictionary.getDictName().contains("本科") || dictionary.getDictName().contains("专升本")){
							continue;
						}
					}else {
						if(dictionary.getDictName().contains("专科") || dictionary.getDictName().contains("高起专")){
							continue;
						}
					}
				}
				dictionaryList.add(dictionary);
			}
			model.addAttribute("dictionaryList", dictionaryList);
		}
	}

	/**
	 * 安徽医科大学 生成录取通知书
	 * @param model
	 * @param school
	 * @param eil
	 * @param sex
	 * @param sheng
	 * @param tt
	 * @param endt
	 * @param image
	 * @return
	 */
	private Graphics2D ahykdxAdmissionNotice(ModelMap model, String school, EnrolleeInfo eil, String sex, String sheng, String tt,
			String endt, BufferedImage image) {
		Graphics2D g = image.createGraphics();//得到图形上下文
		g.setColor(Color.BLACK); //设置画笔颜色
		// 显示录取编号
		SysConfiguration sysConfig =CacheAppManager.getSysConfigurationByCode("exameeInfo.NO.isShow");
		String isShow = "N";// 是否显示录取编号，默认否（不显示）
		if(sysConfig != null){
			isShow = sysConfig.getParamValue();
		}
		if("Y".equals(isShow)){
			 g.setFont(new Font("宋体", Font.BOLD, 20));
			 String enrollNO = "NO."+eil.getEnrollNO();
			 g.drawString(enrollNO,700-enrollNO.length(),65);
		}
		// 显示标题
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//设置字体
		g.setFont(new Font("黑体", Font.LAYOUT_LEFT_TO_RIGHT, 27));//写入签名
		//下面这一句中的43，image.getHeight()-10可以改成你要的坐标。
		 school=school+"新生录取通知书";
		 int topheight=(842-school.length()*27)/2;
		 int sizi=19;
		g.drawString(school,topheight, image.getHeight() - image.getHeight() +130);
		g.setFont(new Font("宋体", Font.BOLD, 20));
		String name=eil.getStudentBaseInfo().getName();
		g.drawString(name,175-name.length()*19, image.getHeight() - image.getHeight() +190);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 19));
		g.drawString(" 同学：",195, image.getHeight() - image.getHeight() +190);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 12));
		String ksh = "";
		try {
			ksh = CacheAppManager.getSysConfigurationByCode("system.ksh.Isreplce.zkzh").getParamValue(); 
			
			if(StringUtils.isNotBlank(ksh) && "Y".equals(StringUtils.trim(ksh).toUpperCase())){
				ksh = eil.getExamCertificateNo();
				model.addAttribute("Isreplce","Y");
			}else{
				ksh = eil.getEnrolleeCode();
			} 
		} catch (Exception e2) {
			ksh = eil.getEnrolleeCode();
		}
		RecruitMajor rm = eil.getRecruitMajor();
		Major m = rm.getMajor();
		String show=CacheAppManager.getSysConfigurationByCode("AdmissionQueryShows").getParamValue();
		String stuno= "Y".equals(show) ?"   学号："+eil.getMatriculateNoticeNo():"";
		g.drawString("性别："+sex+stuno+"    考生号："+ksh+"	     身份证号："+eil.getStudentBaseInfo().getCertNum(),150, image.getHeight() - image.getHeight() +230);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("经成人高等学校招生全国统一考试，"+sheng+"教育招生考试院审核，你已被",150, image.getHeight() - image.getHeight() +270);
		g.drawString("我校",120, image.getHeight() - image.getHeight() +310);
		String zy = m.getMajorName().replace("专科", "").replace("本科", "").replace("高起专","").replace("专升本", "");
		 
		m.setMajorName(zy);
		rm.setMajor(m);
		eil.setRecruitMajor(rm);
		String c1=eil.getRecruitMajor().getClassic().getClassicName();
		model.addAttribute("classicId", eil.getRecruitMajor().getClassic().getResourceid());
		g.setFont(new Font("宋体", Font.BOLD, 21));
		g.drawString(" "+c1+" ",152,image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("层次, ",235, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.BOLD, 21));
     
		int w3=zy.length()-2;
		int w4=w3*19;
		g.drawString(" "+zy+" ",283, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("专业,",370+w4, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.BOLD, 21));
		String cc=" 业余学习形式  ";
		 
		g.drawString(cc,410+w4, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		String ttTime="";
		if(tt.equals(endt)){
			ttTime = tt;
		}else{
			ttTime = tt+" 至 "+endt;
		}
		String s2=" 录取，请按《入学须知》要求于"+ttTime+"到我校办理入学手续。";
//		String s2=" 录取，请按《入学须知》要求于"+tt+" 至 "+endt+"到我校办理入学手续。";
		int wi=zy.length();
		int w1=wi-2;
		int w2=w1*sizi;
		String s3=s2.substring(0,33-w1-22);
		String s4=s2.substring(33-w1-22);
		 // String s5=s2.substring(47);
		 if(zy.length()>7){
		   s4=s2.substring(33-w1-22,46);
		   String s5=s2.substring(46);
		   g.drawString(s5,120, image.getHeight() - image.getHeight() +390);
		 }
		 
		g.drawString(s3,555+w2, image.getHeight() - image.getHeight() +310);
		 
    
		g.drawString(s4,120, image.getHeight() - image.getHeight() +350);
		 
		g.drawString("报到具体事项详见《入学须知》。",152, image.getHeight() - image.getHeight() +450);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 17));
		String showTime=CacheAppManager.getSysConfigurationByCode("AdmissionShowTime").getParamValue(); 
		g.drawString(showTime,500,image.getHeight()-image.getHeight()+500);
		g.setFont(new Font("宋体",Font.LAYOUT_LEFT_TO_RIGHT,17));
 
		g.dispose();
		
		return g;
	}
	
	/**
	 * 广西中医药大学 生成录取通知书
	 * @param model
	 * @param eil
	 * @param image
	 * @return
	 * @author Git
	 */
	private Graphics2D gxzyyAdmissionNotice(ModelMap model,EnrolleeInfo eil,BufferedImage image) {
		final int pxErHao = 29;
		final int pxXiaoEr = 24;
		final int pxSanHao = 21;
		final int pxXiaoSan = 20;
		final String SONG = "宋体";
		final String HEI = "黑体";
		final String ROMAN = "Times New Roman";
		
		Graphics2D g = image.createGraphics();// 得到图形上下文
		
		//初始坐标:
		int init_x = 150;
		int init_y = 200;
		int x = init_x;
		int y = init_y;
		
		g.setColor(Color.BLACK); // 设置画笔颜色
		
		// 显示录取编号
		SysConfiguration sysConfig = CacheAppManager.getSysConfigurationByCode("exameeInfo.NO.isShow");
		String isShow = "N";// 是否显示录取编号，默认否（不显示）
		if (sysConfig != null) {
			isShow = sysConfig.getParamValue();
		}
		//学制
		BigDecimal studyperiod = BigDecimal.valueOf(eil.getRecruitMajor().getStudyperiod());
		String studyperiodStr = studyperiod.toString();
		if(studyperiod.compareTo(BigDecimal.valueOf(3)) == 0 ){
			studyperiodStr = "三";
		}else if (studyperiod.compareTo(BigDecimal.valueOf(5)) == 0 ) {
			studyperiodStr = "五";
		}
		//第一行开始
		x = init_x-33;
		y = init_y-9;
		String name = eil.getStudentBaseInfo().getName();
		int nameFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.BOLD, nameFontSize));
		g.drawString(name, x, y);

		/*x = x + name.length() * nameFontSize;
		String nameSuffix = " 同学 ";
		int nameSuffixFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, nameSuffixFontSize));
		g.drawString(nameSuffix, x, y);

		x = x + (nameSuffix.length() - 1) * nameSuffixFontSize;
		String kshLabel = "（考生号";
		int kshLabelFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, kshLabelFontSize));
		g.drawString(kshLabel, x, y);

		x = x + kshLabel.length() * kshLabelFontSize;*/
		x += 323;
		String ksh = "";
		try {
			ksh = CacheAppManager.getSysConfigurationByCode("system.ksh.Isreplce.zkzh").getParamValue();

			if (StringUtils.isNotBlank(ksh) && "Y".equals(StringUtils.trim(ksh).toUpperCase())) {
				ksh = eil.getExamCertificateNo();
				model.addAttribute("Isreplce", "Y");
			} else {
				ksh = eil.getEnrolleeCode();
			}
		} catch (Exception e2) {
			ksh = eil.getEnrolleeCode();
		}
		//ksh = ":" + ksh;
		int kshFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, kshFontSize));
		g.drawString(ksh, x, y);

		/*x = x + ksh.length() / 2 * kshFontSize + 5;
		String kshLabelSuffix = "）";
		int kshLabelSuffixFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, kshLabelSuffixFontSize));
		g.drawString(kshLabelSuffix, x, y);*/

		// 第二行开始
		RecruitMajor rm = eil.getRecruitMajor();
		Major m = rm.getMajor();

		boolean flag = false;
		String majorName = m.getMajorName();
		/*if(majorName .contains("公共事业管理")){//判断是否为公共事业管理进行特殊处理
			studyperiodStr = "两年半";
			flag=true;
		}*/
		/*int contentLineOneFontSize = pxSanHao;
		x = init_x + 2 * contentLineOneFontSize;
		y = y + 70;
		String contentLineOne = "经广西壮族自治区招生考试委员会批准，你已被我校";
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineOneFontSize));
		g.drawString(contentLineOne, x, y);*/

		y = y + 70;
		/*x = init_x;
		y = y + 45;
		String contentLineTwo = "录取为成人教育";
		int contentLineTwoFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineTwoFontSize));
		g.drawString(contentLineTwo, x, y);*/
		
		/*x = x + contentLineTwo.length() * contentLineTwoFontSize;
		String gradeName = BigDecimal.valueOf(rm.getRecruitPlan().getGrade().getYearInfo().getFirstYear()).toString();
		int gradeStrFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, gradeStrFontSize));
		g.drawString(gradeName, x, y);*/

		/*x = x + gradeName.length() / 2  * gradeStrFontSize + 5;
		String contentLineTwoPartTwo = "级学生。专业";
		int contentLineTwoPartTwoFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineTwoPartTwoFontSize));
		g.drawString(contentLineTwoPartTwo, x, y);*/

		/*x = x + contentLineTwoPartTwo.length() * contentLineTwoPartTwoFontSize;
		String zy = m.getMajorName().replace("专科", "").replace("本科", "").replace("高起专", "").replace("专升本", "");
		majorName = zy;*/
		
		//去括号
		int majorNameFontSize = pxSanHao;
		int len = 10-majorName.length();
		y += 8;
		x = 240+len*majorNameFontSize/2;
		majorName = majorName.replace("（", "(").replace("）", ")");
		majorName = ReplaceStr.replace(new String[]{"("}, new String[]{")"}, majorName);
		g.setFont(new Font(SONG, Font.BOLD, majorNameFontSize));
		//g.drawString((flag==true?":":"：")+majorName, x, y);
		g.drawString(majorName, x, y);

		/*//填充空格
		for (int i = majorName.length(); i < 9; i++) {
			x += majorNameFontSize;
		}
		x = x + majorName.length() * majorNameFontSize;
		String contentLineTwoSuffix = "；";
		int contentLineTwoSuffixFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineTwoSuffixFontSize));
		g.drawString(contentLineTwoSuffix, x, y);*/

		// 第三行
		/*x = init_x;
		y = y + 45;
		String contentLineThreeCCLabel = "层次"+(flag==true?"":"：");
		int contentLineThreeCCLabelFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeCCLabelFontSize));
		g.drawString(contentLineThreeCCLabel, x, y);*/

		//x = x + contentLineThreeCCLabel.length() * contentLineThreeCCLabelFontSize;
		x = 518;
		String classic = eil.getRecruitMajor().getClassic().getClassicName();
		int classicFontSize = pxSanHao;
		g.setFont(new Font(HEI, Font.BOLD, classicFontSize));
		//g.drawString((flag==true?":":"")+classic, x, y);
		g.drawString(classic.substring(2), x, y);

		/*x = x + classic.length() * classicFontSize + (flag==true?14:0);
		String contentLineThreeLearningStyleLabel = "；学习形式"+(flag==true?"":"：");
		int contentLineThreeLearningStyleLabelFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeLearningStyleLabelFontSize));
		g.drawString(contentLineThreeLearningStyleLabel, x, y);*/

		/*x = x + contentLineThreeLearningStyleLabel.length() * contentLineThreeLearningStyleLabelFontSize;
		String learningStyle = eil.getRecruitMajor().getTeachingType();
		int learningStyleFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, learningStyleFontSize));
		String learningStyleStr = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", learningStyle);
		g.drawString((flag==true?":":"")+learningStyleStr, x, y);*/

		/*x = x + learningStyleStr.length() * learningStyleFontSize + (flag==true?14:0);
		String contentLineThreeStudyperiodLabel = "；学制"+(flag==true?"":"：");
		int contentLineThreeStudyperiodLabelFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeStudyperiodLabelFontSize));
		g.drawString(contentLineThreeStudyperiodLabel, x, y);*/

		//x = x + contentLineThreeStudyperiodLabel.length() * contentLineThreeStudyperiodLabelFontSize;
		x += 140;
		int studyperiodFontSize = pxSanHao;
		g.setFont(new Font(HEI, Font.BOLD, studyperiodFontSize));
		g.drawString((flag==true?":":"")+studyperiodStr, x, y);

		y += 230;
		x=210;
		g.setFont(new Font(SONG, Font.BOLD, studyperiodFontSize));
		g.drawString(eil.getMatriculateNoticeNo()==null?"":eil.getMatriculateNoticeNo(), x, y);
		y += 40;
		x=210;
		g.setFont(new Font(SONG, Font.BOLD, studyperiodFontSize));
		g.drawString(eil.getMatriculateNoticeNo()==null?"":eil.getMatriculateNoticeNo(), x, y);
		/*x = x + studyperiodStr.length() * classicFontSize + (flag==true?14:0);
		String contentLineThreeSuffix = "。请你持本";
		int contentLineThreeSuffixFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeSuffixFontSize));
		g.drawString(contentLineThreeSuffix, x, y);*/

		// 第四行
		/*x = init_x;
		y = y + 45;
		String contentLineFour = "通知书按入学须知要求到我校继续教育学院或教学点报到。";
		int contentLineFourFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineFourFontSize));
		g.drawString(contentLineFour, x, y);*/

		// 第五行
		/*y = y + 90;
		x = init_x + 405;
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		int schoolNameFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, schoolNameFontSize));
		g.drawString(schoolName, x, y);*/

		// 第六行
		/*x = init_x + 395;
		y = y + 60;
		String admissionShowTime = CacheAppManager.getSysConfigurationByCode("AdmissionShowTime").getParamValue();
		int admissionShowTimeFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, admissionShowTimeFontSize));
		g.drawString(admissionShowTime, x, y);
		g.dispose();*/

		return g;
	}

	/**
	 * 广西医科大学 生成录取通知书
	 * @param model
	 * @param eil
	 * @param image
	 * @return
	 * @author Git
	 */
	private Graphics2D gxykdxAdmissionNotice(ModelMap model,EnrolleeInfo eil,BufferedImage image) {
		
		final int pxErHao = 29;
		final int pxXiaoEr = 24;
		final int pxSanHao = 21;
		final int pxXiaoSan = 20;
		final String SONG = "宋体";
		final String HEI = "黑体";
		final String ROMAN = "Times New Roman";
		
		Graphics2D g = image.createGraphics();// 得到图形上下文
		
		//初始坐标:
		
		int init_x = 210;
		int init_y = 750;
		int x = init_x;
		int y = init_y;
		
		g.setColor(Color.BLACK); // 设置画笔颜色
		
		
		// 显示录取编号
		SysConfiguration sysConfig = CacheAppManager.getSysConfigurationByCode("exameeInfo.NO.isShow");
		String isShow = "N";// 是否显示录取编号，默认否（不显示）
		if (sysConfig != null) {
			isShow = sysConfig.getParamValue();
		}
		if ("Y".equals(isShow)) {
			x = x + 445;
			y = y - 50;
			String enrollNoLabel = "序号";
			int enrollNoLabelFontSize = pxXiaoSan;
			g.setFont(new Font(SONG, Font.PLAIN, enrollNoLabelFontSize));
			g.drawString(enrollNoLabel, x, y);

			x = x + enrollNoLabel.length() * enrollNoLabelFontSize;
			int enrollNoFontSize = pxXiaoSan;
			g.setFont(new Font(ROMAN, Font.PLAIN, enrollNoFontSize));
			String enrollNO = eil.getEnrollNO();
			g.drawString(StringUtils.stripToEmpty(enrollNO), x, y);
		}
		//学制
		BigDecimal studyperiod = BigDecimal.valueOf(eil.getRecruitMajor().getStudyperiod());
		String studyperiodStr = studyperiod.compareTo(BigDecimal.valueOf(3)) == 0 ? "三年" : studyperiod.toString();
		//第一行开始
		x = init_x + 18;
		y = init_y;
		String name = eil.getStudentBaseInfo().getName();
		int nameFontSize = pxErHao;
		g.setFont(new Font(SONG, Font.BOLD, nameFontSize));
		g.drawString(name, x, y);

		x = x + name.length() * nameFontSize;
		String nameSuffix = " 同学 ";
		int nameSuffixFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, nameSuffixFontSize));
		g.drawString(nameSuffix, x, y);

		x = x + (nameSuffix.length() - 1) * nameSuffixFontSize;
		String kshLabel = "（考生号";
		int kshLabelFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, kshLabelFontSize));
		g.drawString(kshLabel, x, y);

		x = x + kshLabel.length() * kshLabelFontSize;
		String ksh = "";
		try {
			ksh = CacheAppManager.getSysConfigurationByCode("system.ksh.Isreplce.zkzh").getParamValue();

			if (StringUtils.isNotBlank(ksh) && "Y".equals(StringUtils.trim(ksh).toUpperCase())) {
				ksh = eil.getExamCertificateNo();
				model.addAttribute("Isreplce", "Y");
			} else {
				ksh = eil.getEnrolleeCode();
			}
		} catch (Exception e2) {
			ksh = eil.getEnrolleeCode();
		}
		ksh = ":" + ksh;
		int kshFontSize = pxXiaoEr;
		g.setFont(new Font(ROMAN, Font.BOLD, kshFontSize));
		g.drawString(ksh, x, y);

		x = x + ksh.length() / 2 * kshFontSize + 5;
		String kshLabelSuffix = "）";
		int kshLabelSuffixFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, kshLabelSuffixFontSize));
		g.drawString(kshLabelSuffix, x, y);

		// 第二行开始
		RecruitMajor rm = eil.getRecruitMajor();
		Major m = rm.getMajor();

		boolean flag = false;
		String majorName = m.getMajorName();
		if(majorName .contains("公共事业管理")){//判断是否为公共事业管理进行特殊处理
			studyperiodStr = "两年半";
			flag=true;
		}
		int contentLineOneFontSize = pxSanHao;
		x = init_x + 2 * contentLineOneFontSize;
		y = y + 70;
		String contentLineOne = "经广西壮族自治区招生考试委员会批准，你已被我校";
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineOneFontSize));
		g.drawString(contentLineOne, x, y);

		x = init_x;
		y = y + 45;
		String contentLineTwo = "录取为成人教育";
		int contentLineTwoFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineTwoFontSize));
		g.drawString(contentLineTwo, x, y);

		x = x + contentLineTwo.length() * contentLineTwoFontSize;
		String gradeName = BigDecimal.valueOf(rm.getRecruitPlan().getGrade().getYearInfo().getFirstYear()).toString();
		int gradeStrFontSize = pxSanHao;
		g.setFont(new Font(ROMAN, Font.LAYOUT_LEFT_TO_RIGHT, gradeStrFontSize));
		g.drawString(gradeName, x, y);

		x = x + gradeName.length() / 2  * gradeStrFontSize + 5;
		String contentLineTwoPartTwo = "级学生。专业";
		int contentLineTwoPartTwoFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineTwoPartTwoFontSize));
		g.drawString(contentLineTwoPartTwo, x, y);

		x = x + contentLineTwoPartTwo.length() * contentLineTwoPartTwoFontSize;
		String zy = m.getMajorName().replace("专科", "").replace("本科", "").replace("高起专", "").replace("专升本", "");
		majorName = zy;
		
		//去括号
		majorName = majorName.replace("（", "(").replace("）", ")");
		majorName = ReplaceStr.replace(new String[]{"("}, new String[]{")"}, majorName);
		int majorNameFontSize = pxSanHao;
		g.setFont(new Font(HEI, Font.LAYOUT_LEFT_TO_RIGHT, majorNameFontSize));
		g.drawString((flag==true?":":"：")+majorName, x, y);

		//填充空格
		for (int i = majorName.length(); i < 9; i++) {
			x += majorNameFontSize;
		}
		x = x + majorName.length() * majorNameFontSize;
		String contentLineTwoSuffix = "；";
		int contentLineTwoSuffixFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineTwoSuffixFontSize));
		g.drawString(contentLineTwoSuffix, x, y);

		// 第三行
		x = init_x;
		y = y + 45;
		String contentLineThreeCCLabel = "层次"+(flag==true?"":"：");
		int contentLineThreeCCLabelFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeCCLabelFontSize));
		g.drawString(contentLineThreeCCLabel, x, y);

		x = x + contentLineThreeCCLabel.length() * contentLineThreeCCLabelFontSize;
		String classic = eil.getRecruitMajor().getClassic().getClassicName();
		int classicFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, classicFontSize));
		g.drawString((flag==true?":":"")+classic, x, y);

		x = x + classic.length() * classicFontSize + (flag==true?14:0);
		String contentLineThreeLearningStyleLabel = "；学习形式"+(flag==true?"":"：");
		int contentLineThreeLearningStyleLabelFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeLearningStyleLabelFontSize));
		g.drawString(contentLineThreeLearningStyleLabel, x, y);

		x = x + contentLineThreeLearningStyleLabel.length() * contentLineThreeLearningStyleLabelFontSize;
		String learningStyle = eil.getRecruitMajor().getTeachingType();
		int learningStyleFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, learningStyleFontSize));
		String learningStyleStr = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", learningStyle);
		g.drawString((flag==true?":":"")+learningStyleStr, x, y);

		x = x + learningStyleStr.length() * learningStyleFontSize + (flag==true?14:0);
		String contentLineThreeStudyperiodLabel = "；学制"+(flag==true?"":"：");
		int contentLineThreeStudyperiodLabelFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeStudyperiodLabelFontSize));
		g.drawString(contentLineThreeStudyperiodLabel, x, y);

		x = x + contentLineThreeStudyperiodLabel.length() * contentLineThreeStudyperiodLabelFontSize;
		int studyperiodFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, studyperiodFontSize));
		g.drawString((flag==true?":":"")+studyperiodStr, x, y);

		x = x + studyperiodStr.length() * classicFontSize + (flag==true?14:0);
		String contentLineThreeSuffix = "。请你持本";
		int contentLineThreeSuffixFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineThreeSuffixFontSize));
		g.drawString(contentLineThreeSuffix, x, y);

		// 第四行
		x = init_x;
		y = y + 45;
		String contentLineFour = "通知书按入学须知要求到我校继续教育学院或教学点报到。";
		int contentLineFourFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, contentLineFourFontSize));
		g.drawString(contentLineFour, x, y);

		// 第五行
		y = y + 90;
		x = init_x + 405;
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		int schoolNameFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, schoolNameFontSize));
		g.drawString(schoolName, x, y);

		// 第六行
		x = init_x + 395;
		y = y + 60;
		String admissionShowTime = CacheAppManager.getSysConfigurationByCode("AdmissionShowTime").getParamValue();
		int admissionShowTimeFontSize = pxSanHao;
		g.setFont(new Font(SONG, Font.BOLD, admissionShowTimeFontSize));
		g.drawString(admissionShowTime, x, y);
		g.dispose();

		return g;
	}
	
	/**
	 *广州大学 生成录取通知书
	 * @param model
	 * @param school
	 * @param eil
	 * @param sex
	 * @param sheng
	 * @param tt
	 * @param endt
	 * @param image
	 * @return
	 */
	private Graphics2D gzdxAdmissionNotice(ModelMap model, String school, EnrolleeInfo eil, String sex, String sheng, String tt,
			String endt, BufferedImage image) {
		Graphics2D g = image.createGraphics();//得到图形上下文
		g.setColor(Color.BLACK); //设置画笔颜色
		// 显示录取编号
		SysConfiguration sysConfig =CacheAppManager.getSysConfigurationByCode("exameeInfo.NO.isShow");
		String isShow = "N";// 是否显示录取编号，默认否（不显示）
		if(sysConfig != null){
			isShow = sysConfig.getParamValue();
		}
		if("Y".equals(isShow)){
			 g.setFont(new Font("宋体", Font.BOLD, 20));
			 String enrollNO = "NO."+eil.getEnrollNO();
			 g.drawString(enrollNO,700-enrollNO.length(),65);
		}
		// 显示标题
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//设置字体
		g.setFont(new Font("黑体", Font.LAYOUT_LEFT_TO_RIGHT, 27));//写入签名
		//下面这一句中的43，image.getHeight()-10可以改成你要的坐标。
		 int topheight=(842-school.length()*27)/2;
		 int sizi=19;
		g.drawString(school,topheight, image.getHeight() - image.getHeight() +130);
		g.setFont(new Font("宋体", Font.BOLD, 20));
		String name=eil.getStudentBaseInfo().getName();
		g.drawString(name,175-name.length()*19, image.getHeight() - image.getHeight() +190);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 19));
		g.drawString(" 同学：",195, image.getHeight() - image.getHeight() +190);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 12));
		String ksh = "";
		try {
			ksh = CacheAppManager.getSysConfigurationByCode("system.ksh.Isreplce.zkzh").getParamValue(); 
			
			if(StringUtils.isNotBlank(ksh) && "Y".equals(StringUtils.trim(ksh).toUpperCase())){
				ksh = eil.getExamCertificateNo();
				model.addAttribute("Isreplce","Y");
			}else{
				ksh = eil.getEnrolleeCode();
			} 
		} catch (Exception e2) {
			ksh = eil.getEnrolleeCode();
		}
		RecruitMajor rm = eil.getRecruitMajor();
		Major m = rm.getMajor();
		String show=CacheAppManager.getSysConfigurationByCode("AdmissionQueryShows").getParamValue();
		String stuno= "Y".equals(show) ?"   学号："+eil.getMatriculateNoticeNo():"";
		g.drawString("性别："+sex+stuno+"    考生号："+ksh+"	     身份证号："+eil.getStudentBaseInfo().getCertNum(),155, image.getHeight() - image.getHeight() +230);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("你参加成人高等学校招生考试，经"+sheng+"招生委员会批准，已被我校",155, image.getHeight() - image.getHeight() +270);
//		g.drawString("",120, image.getHeight() - image.getHeight() +310);
		String zy = m.getMajorName().replace("专科", "").replace("本科", "").replace("高起专","").replace("专升本", "");
		zy = zy.replace("（", "(").replace("）", ")");
		//是否需要把XYMC特殊处理
		List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
				, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
		if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(zy)){
			String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
			String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
			for (int i = 0; i < listDictZYDM.size(); i++) {
				String rpstr1 = ExStringUtils.trim(listDictZYDM.get(i).getDictName()+"").replace("（", "(").replace("）", ")");
				String rpstr2 = ExStringUtils.trim(listDictZYDM.get(i).getDictValue()+"").replace("（", "(").replace("）", ")");
				zymcReplaceArrayL[i] = rpstr1;
				zymcReplaceArrayR[i] = rpstr2;	
			}
			zy = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, zy); //去除符号（含符号）里面的字符
		}
		m.setMajorName(zy);
		rm.setMajor(m); 
		eil.setRecruitMajor(rm);
		String c1=eil.getRecruitMajor().getClassic().getClassicName();
		model.addAttribute("classicId", eil.getRecruitMajor().getClassic().getResourceid());
		g.setFont(new Font("宋体", Font.BOLD, 21));
		g.drawString(" "+c1+" ",152,image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("层次, ",235, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.BOLD, 21));
		int w3=zy.length()-2;
		int w4=w3*19;
		g.drawString(" "+zy+" ",283, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("专业,",370+w4, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.BOLD, 21));
		String cc=" "+JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", eil.getTeachingType());
		g.drawString(cc,410+w4, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		g.drawString("学习形式",485+w4, image.getHeight() - image.getHeight() +310);
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, sizi));
		String ttTime ="";
		if(tt.equals(endt)){
			ttTime = tt;
		}else{
			ttTime = tt+" 至 "+endt;
		}
		String s2=" 录取，请按《入学须知》要求于"+ttTime+"到我校办理入学手续。";
		int wi=zy.length();
		int w1=wi-2;
		int w2=w1*sizi;
		String s3=s2.substring(0,33-w1-22);
		String s4=s2.substring(33-w1-22);
		 // String s5=s2.substring(47);
		 if(zy.length()>7){
		   s4=s2.substring(33-w1-22,s2.length());
		   String s5=s2.substring(s2.length());
		   g.drawString(s5,120, image.getHeight() - image.getHeight() +390);
		 }
		 
		g.drawString(s3,560+w2, image.getHeight() - image.getHeight() +310);
		 
    
		g.drawString(s4,120, image.getHeight() - image.getHeight() +350);
		 
		g.drawString("报到具体事项详见《入学须知》。",155, image.getHeight() - image.getHeight() +400);
		g.drawString("校长签名",400, image.getHeight() - image.getHeight() +450);
		g.drawString(CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue(),560, image.getHeight() - image.getHeight() +440);
		g.drawString("（招生部门盖章）",530, image.getHeight() - image.getHeight() +470);
		
		g.setFont(new Font("宋体", Font.LAYOUT_LEFT_TO_RIGHT, 17));
		String showTime=CacheAppManager.getSysConfigurationByCode("AdmissionShowTime").getParamValue(); 
		g.drawString(showTime,500,image.getHeight()-image.getHeight()+500);
		g.setFont(new Font("宋体",Font.LAYOUT_LEFT_TO_RIGHT,17));
 
		g.dispose();
		
		return g;
	}
	
	private Graphics2D gxsfAdmissionNotice(ModelMap model,EnrolleeInfo eil,BufferedImage image) {
		
		final int pxErHao = 29;
		final int pxXiaoEr = 24;
		final int pxSanHao = 21;
		final int pxXiaoSan = 20;
		final String SONG = "宋体";
		final String kAITI = "楷体";
		final int mainBodyFontSize = 22;
		final String mainBodyFontStyle = kAITI;
		final int LINE_LENGTH = 32;
		Graphics2D g = image.createGraphics();// 得到图形上下文
		
		//初始坐标:
		
		int init_x = 125;
		int init_y = 700;
		int x = init_x;
		int y = init_y;
		
		g.setColor(Color.BLACK); // 设置画笔颜色
		
		// 显示录取编号
		SysConfiguration sysConfig = CacheAppManager.getSysConfigurationByCode("exameeInfo.NO.isShow");
		String isShow = "N";// 是否显示录取编号，默认否（不显示）
		if (sysConfig != null) {
			isShow = sysConfig.getParamValue();
		}
		//学制
		BigDecimal studyperiod = BigDecimal.valueOf(eil.getRecruitMajor().getStudyperiod());
		String studyperiodStr = studyperiod.compareTo(BigDecimal.valueOf(3)) == 0 ? "三年" : studyperiod.toString();
		//第一行开始
		x = init_x;
		y = init_y;
		String name = eil.getStudentBaseInfo().getName();
		int nameFontSize = pxErHao;
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, nameFontSize));
		g.drawString(name, x, y);

		x = x + name.length() * nameFontSize;
		String cerNumSuffix = "（身份证号";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, 20));
		g.drawString(cerNumSuffix, x, y);
		
		x = x + cerNumSuffix.length() * 20+10;
		cerNumSuffix = eil.getStudentBaseInfo().getCertNum();
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, 19));
		g.drawString(cerNumSuffix, x, y);
		
		x = x + cerNumSuffix.length()*14;
		cerNumSuffix = "）";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, 20));
		g.drawString(cerNumSuffix, x, y);

		x = x + cerNumSuffix.length()*20;
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, pxXiaoEr));
		g.drawString("同学：", x, y);

		/*x = x + kshLabel.length() * kshLabelFontSize;
		String ksh = "";
		try {
			ksh = CacheAppManager.getSysConfigurationByCode("system.ksh.Isreplce.zkzh").getParamValue();

			if (StringUtils.isNotBlank(ksh) && "Y".equals(StringUtils.trim(ksh).toUpperCase())) {
				ksh = eil.getExamCertificateNo();
				model.addAttribute("Isreplce", "Y");
			} else {
				ksh = eil.getEnrolleeCode();
			}
		} catch (Exception e2) {
			ksh = eil.getEnrolleeCode();
		}
		ksh = ":" + ksh;
		int kshFontSize = pxXiaoEr;
		g.setFont(new Font(ROMAN, Font.BOLD, kshFontSize));
		g.drawString(ksh, x, y);

		x = x + ksh.length() / 2 * kshFontSize + 5;
		String kshLabelSuffix = "）";
		int kshLabelSuffixFontSize = pxXiaoEr;
		g.setFont(new Font(SONG, Font.LAYOUT_LEFT_TO_RIGHT, kshLabelSuffixFontSize));
		g.drawString(kshLabelSuffix, x, y);*/

		// 第二行开始
		RecruitMajor rm = eil.getRecruitMajor();
		Major m = rm.getMajor();

		boolean flag = false;
		String majorName = m.getMajorName();
		if(majorName .contains("公共事业管理")){//判断是否为公共事业管理进行特殊处理
			studyperiodStr = "两年半";
			flag=true;
		}
		//去括号
		//majorName = "广东省广州市天河区长福路";
		majorName = majorName.replace("（", "(").replace("）", ")");
		majorName = ReplaceStr.replace(new String[]{"("}, new String[]{")"}, majorName);
		
		x = init_x + 43;
		y = y + 45;
		String contentLineOne = "经广西壮族自治区招生考试委员会批准，录取你为我校"+eil.getGrade().getGradeName()+"成人高";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(contentLineOne, x, y);

		x = init_x;
		y = y + 45;
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString("等教育", x, y);
		x = x + 3 * mainBodyFontSize+15;
		String classic = eil.getRecruitMajor().getClassic().getClassicName();
		int classicFontSize = pxSanHao;
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, classicFontSize));
		g.drawString(classic, x, y);
		
		x = x + classic.length() * classicFontSize+15;
		String contentLineTwoPartTwo = "层次";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(contentLineTwoPartTwo, x, y);

		x = x + contentLineTwoPartTwo.length() * mainBodyFontSize;
		x += (15-majorName.length())*mainBodyFontSize/2 ;
		//String zy = majorName.replace("专科", "").replace("本科", "").replace("高起专", "").replace("专升本", "");
		//majorName = zy;
		
		int majorNameFontSize = pxSanHao;
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, majorNameFontSize));
		g.drawString(majorName, x, y);
		
		
		//x += (15-majorName.length())*mainBodyFontSize/2 ;
		//y = y + 45;
		x = 652;
		String contentLineThreeCCLabel = "专业";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(contentLineThreeCCLabel, x, y);
		
		//x = init_x;
		x = x + contentLineThreeCCLabel.length() * majorNameFontSize+23;
		String learningStyle = eil.getRecruitMajor().getTeachingType();
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, mainBodyFontSize));
		String learningStyleStr = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", learningStyle);
		g.drawString(learningStyleStr, x, y);

		x += mainBodyFontSize*2 + 23;
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString("学习", x, y);
		
		y += 43;
		x = init_x;
		String contentLineThreeStudyperiodLabel = "形式学生，你的学号为";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(contentLineThreeStudyperiodLabel, x, y);

		x = x + contentLineThreeStudyperiodLabel.length() * mainBodyFontSize+5;
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, mainBodyFontSize));
		g.drawString(eil.getMatriculateNoticeNo()+"。", x, y);

		y += 45;
		x = init_x+45;
		String content = "";
		String reportSite = "请你按《新生入学须知》的要求到我校相应的学院或教学点报到。";
		/*String contentLineThreeSuffix = "请持本录取通知书于";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(contentLineThreeSuffix, x, y);

		content = eil.getBranchSchool().getReportTime()==null?"":eil.getBranchSchool().getReportTime();
		int length = content.length()-(ExStringUtils.getDigitalAndSpaceCount(content)/2);
		
		//x += (contentLineThreeSuffix.length()+(15-length)/2) * mainBodyFontSize;
		x += contentLineThreeSuffix.length() * mainBodyFontSize;
		g.setFont(new Font(mainBodyFontStyle, Font.BOLD, mainBodyFontSize));
		g.drawString(content, x, y);
		
		//x += (length + (15-length)/2) * mainBodyFontSize;
		x += length * mainBodyFontSize + 4 + ExStringUtils.getDigitalAndSpaceCount(content);
		content = "到";
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(content, x, y);
		
		String reportSite = eil.getBranchSchool().getReportSite()==null?"":eil.getBranchSchool().getReportSite();
		int position = LINE_LENGTH-4-9-length;
		x += content.length() * mainBodyFontSize;
		content = ExStringUtils.substring(reportSite, 0, position);*/
		int position = LINE_LENGTH-2;
		content = ExStringUtils.substring(reportSite, 0, position);
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		
		g.drawString(content, x, y);
		
		// 第四行
		x = init_x;
		y = y + 45;
		content = ExStringUtils.substring(reportSite, position, reportSite.length());
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(content, x, y);
		
		/*length = content.length()-(ExStringUtils.getDigitalAndSpaceCount(content)/2);
		x +=  length * mainBodyFontSize + ExStringUtils.getDigitalAndSpaceCount(content);
		String contentLineFour = "报到注册，有关事项详见《新生入学须知》。";
		content = ExStringUtils.substring(contentLineFour, 0, LINE_LENGTH-length);
		g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
		g.drawString(content, x, y);
		if(length+contentLineFour.length()>LINE_LENGTH){
			// 第五行
			x = init_x;
			y = y + 45;
			content = ExStringUtils.substring(contentLineFour, LINE_LENGTH-length, contentLineFour.length());
			g.setFont(new Font(mainBodyFontStyle, Font.LAYOUT_LEFT_TO_RIGHT, mainBodyFontSize));
			g.drawString(content, x, y);
		}*/

		g.dispose();

		return g;
	}

	/**
	 * 图片拼接
	 * @param path
	 * @param partOne
	 * @param partTwo
	 * @throws Exception
	 * @author Git
	 */
	private static void imageMosaic(String path,String partOne, String partTwo) throws Exception{
		try {
			// 读取第一张图片
			File fileOne = new File(partOne);
			BufferedImage ImageOne = ImageIO.read(fileOne);
			int width = ImageOne.getWidth();// 图片宽度
			int height = ImageOne.getHeight();// 图片高度
			// 从图片中读取RGB
			int[] ImageArrayOne = new int[width * height];
			ImageArrayOne = ImageOne.getRGB(0, 0, width, height, ImageArrayOne, 0, width);
			// 对第二张图片做相同的处理
			File fileTwo = new File(partTwo);
			BufferedImage ImageTwo = ImageIO.read(fileTwo);
			int widthTwo = ImageTwo.getWidth();// 图片宽度
			int heightTwo = ImageTwo.getHeight();// 图片高度
			int[] ImageArrayTwo = new int[widthTwo * heightTwo];
			ImageArrayTwo = ImageTwo.getRGB(0, 0, widthTwo, heightTwo, ImageArrayTwo, 0, widthTwo);

			// 生成新图片
			BufferedImage ImageNew = new BufferedImage(width, height + heightTwo, BufferedImage.TYPE_INT_RGB);
			ImageNew.setRGB(0, 0, width, height, ImageArrayOne, 0, width);// 设置上半部分的RGB
			ImageNew.setRGB(0, height, widthTwo, heightTwo, ImageArrayTwo, 0, widthTwo);// 设置下半部分的RGB

			File outFile = new File(path);
			ImageIO.write(ImageNew, "jpg", outFile);// 写图片
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		path = "E:\\tmp\\tt2.jpg";
//		
//		InputStream imagein1 = new FileInputStream(partOne);
//		InputStream imagein2 = new FileInputStream(partTwo);
//
//		BufferedImage image1 = ImageIO.read(imagein1);
//		BufferedImage image2 = ImageIO.read(imagein2);
//		BufferedImage imageNew = new BufferedImage(image1.getWidth()>image2.getWidth()?image1.getWidth():image2.getWidth(), image1.getHeight()+image2.getHeight(), BufferedImage.TYPE_INT_RGB);
//		
//		Graphics g = imageNew.getGraphics();
//		g.drawImage(image1, 0, 0 , null);
//		g.drawImage(image2, 0, image1.getHeight() , null);
//
//		String formatName = path.substring(path.lastIndexOf(".") + 1);
//		ImageIO.write(imageNew, formatName, new File(path));
//
//		imagein1.close();
//		imagein2.close();
		
	}

	public static void he(String urll, String seal, String nl, int x, int y ,String schoolCode) throws Exception {
		@Cleanup InputStream imagein = new FileInputStream(urll);
		BufferedImage image = ImageIO.read(imagein);
		Graphics g = image.getGraphics();
		// 617--982
		// g.drawImage(image2,image.getWidth()-image2.getWidth()-150,image.getHeight()-image2.getHeight()-110-5,null);
		// 534--291
		// g.drawImage(image2,image.getWidth()-image2.getWidth()-70,image.getHeight()-image2.getHeight()-70,null);
		if ("10598".equals(schoolCode)) {
			x=x-13;
			y=y-12;
		}
		
		if(ExStringUtils.isNotEmpty(seal)){
			@Cleanup InputStream imagein2 = new FileInputStream(seal);
			BufferedImage image2 = ImageIO.read(imagein2);
			g.drawImage(image2, x, y, null);
		}
		String formatName = nl.substring(nl.lastIndexOf(".") + 1);
		ImageIO.write(image, formatName, new File(nl));
		
	}
	    
	   
		public void updateInputUnit1(String ids,ModelMap model,String certNum){
			model.addAttribute("ids", ids);
			StringBuffer unitOption = new StringBuffer();
			if (StringUtils.isNotBlank(ids)) {
				Map<String, Object> condition = new HashMap<String, Object>();
				ids = "'" + ids.replace(",", "','") + "'";
				condition.put("examineeid", ids);
				List<Map<String, Object>> unitList = exameeInfoService.findOrgUnitByConditionWithJDBC(condition);
				List list=new ArrayList();
				for (Map<String, Object> m : unitList) {
					list.add(m.get("unitcode"));
				}
				
				final int size =list.size();
				String[] arr = (String[])list.toArray(new String[size]);
				String b;
				for (int i = 0; i < arr.length; i++) {
					for (int j = 0; j < arr.length; j++) {
						try{
							if(Integer.valueOf(arr[j])>Integer.valueOf(arr[i])){
								b=arr[i];
								arr[i]=arr[j];
								arr[j]=b;
							}
						}catch(Exception e){
							if(arr[j].charAt(0)>arr[i].charAt(0)){
								b=arr[i];
								arr[i]=arr[j];
								arr[j]=b;
							}
						}				
					}
				}
				List<Map<String, Object>> unitList1=new ArrayList<Map<String, Object>>();
				 list = Arrays.asList(arr);
				 for (int i=0; i<list.size() ;i++) {
					for (Map<String, Object> map : unitList) {
						if(list.get(i).equals(map.get("unitcode"))){
							unitList1.add(i,map);
						}
					}
				}
					
				if(null != unitList1 && unitList1.size()>0){
					//教学点栓选条件
					List<Dictionary> list_dict = CacheAppManager.getChildren("EnrollByCernum");
					 Map<String,String> map_dict = new HashMap<String, String>(0);
					 String start = certNum.substring(0, 2);
					 if (ExCollectionUtils.isNotEmpty(list_dict)) {
						 for (Dictionary dict : list_dict) {
							 map_dict.put(dict.getDictName(), dict.getDictValue());
						 }
					 }
					 
					for(Map<String, Object> unit : unitList1){
						if(!(map_dict.containsKey(start) && Arrays.asList(map_dict.get(start).split(",")).contains(unit.get("unitcode")))){
							unitOption.append("<option value='"+unit.get("resourceid")+"'");				
							unitOption.append(">"+unit.get("unitcode")+"-"+unit.get("unitname")+"</option>");
						}							
					}
				}
			}
			
			model.addAttribute("unitOption", unitOption);
		}	
	    
	    
	    
	/**
	 * 录取查询后更改联系方式 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/matriculate/studentmatriculatequery-edit.html")
	public void editContact(HttpServletRequest request,HttpServletResponse response){
		
		String editFlag 			  = StringUtils.trimToEmpty(request.getParameter("editFlag"));
		String results 				  = StringUtils.trimToEmpty(request.getParameter("editResult"));
		String enrolleeCode  	  	  = StringUtils.trimToEmpty(request.getParameter("enrolleeCode"));// 准考证号
		String certNum 				  = StringUtils.trimToEmpty(request.getParameter("certNum"));	// 证件号码
		String msgAuthCode = StringUtils.trimToEmpty(request.getParameter("msgAuthCode"));	// 短信验证码
		Map<String,Object> map        = new HashMap<String, Object>();
		String success                = Constants.BOOLEAN_NO;
		String msg                    = "";
		
		do{
			if (StringUtils.isNotEmpty(certNum)) {
				String phoneComfirm = CacheAppManager.getSysConfigurationByCode("phoneComfirm").getParamValue();
				if("1".equals(phoneComfirm) && "mobile".equals(editFlag)){// 需要手机短信验证
					String cacheMsgAuthCode = memcachedManager.get(results);
					if(ExStringUtils.isEmpty(cacheMsgAuthCode) ){
						 success	 = Constants.BOOLEAN_NO;
						 msg  = "手机验证码不正确或已经过期，请重新发送！";
						 continue;
					}
					if(!cacheMsgAuthCode.equals(msgAuthCode)){
						success	 = Constants.BOOLEAN_NO;
						msg  = "手机验证码不正确！";
						 continue;
					}
					// 检查该号码是否已被使用
					//存在号码被回收的情况，并且在后面学生登录系统时，要求再次确认手机号码，因此，暂定录取页面，不检查号码是否被使用  20170428 lion
//					List<StudentBaseInfo> studentList = studentService.findByHql("from "+StudentBaseInfo.class.getSimpleName()+" s where s.mobile=? and s.isDeleted=0", results);
//					Set<String> certNumSet = new HashSet<String>();
//					if(ExCollectionUtils.isNotEmpty(studentList)) {
//						for(StudentBaseInfo stuBase : studentList){
//							certNumSet.add(stuBase.getCertNum());
//						}
//					}
//					if(ExCollectionUtils.isNotEmpty(certNumSet) && !certNumSet.contains(certNum)){
//						success	 = Constants.BOOLEAN_NO;
//						msg  = "该手机号码已被使用！";
//						continue;
//					}
				}
				List<EnrolleeInfo> list   = enrolleeInfoService.findByHql(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=?  and studentBaseInfo.certNum=?",0,certNum);
				if (CollectionUtils.isNotEmpty(list)) {
					EnrolleeInfo   ei     = list.get(0);
					StudentBaseInfo info  = ei.getStudentBaseInfo();
					try {
						BeanUtils.setProperty(info, editFlag, results);
						ei.setStudentBaseInfo(info);
						enrolleeInfoService.update(ei);
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, "学生修改录取查询的电话号码:学生身份证号："+certNum);
						success	 = Constants.BOOLEAN_YES;
						msg  = "修改成功！";
					} catch (Exception e) {
						logger.error("录取查询后更改联系方式出错：{}"+e.fillInStackTrace());
						msg = "修改失败："+e.getMessage();
						success	 = Constants.BOOLEAN_NO;
					} 
				}else {
					    msg = "修改失败，证件号码有误，查无此人！";
				}
			}else {
						msg = "修改失败，证件号码为空！";
			}
		} while(false);
		
		map.put("msg", msg);
		map.put("success", success);
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * 录取查询后更改联系地址
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/matriculate/studentmatriculatequery-editAddress.html")
	public void editAddress(HttpServletRequest request,HttpServletResponse response){
		String address 				  = StringUtils.trimToEmpty(request.getParameter("address"));	// 联系地址
		String certNum 				  = StringUtils.trimToEmpty(request.getParameter("certNum"));	// 证件号码
		Map<String,Object> map        = new HashMap<String, Object>();
		String success                = Constants.BOOLEAN_NO;
		String msg                    = "";
	
		if (StringUtils.isNotEmpty(certNum)) {
			List<EnrolleeInfo> list   = enrolleeInfoService.findByHql(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=?  and studentBaseInfo.certNum=?",0,certNum);
			if (CollectionUtils.isNotEmpty(list)) {
				EnrolleeInfo   ei     = list.get(0);
				StudentBaseInfo info  = ei.getStudentBaseInfo();
				try {
					info.setContactAddress(address);
					ei.setStudentBaseInfo(info);
					enrolleeInfoService.update(ei);
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, "学生修改录取查询的联系地址:学生身份证号："+certNum);
					success			   = Constants.BOOLEAN_YES;
					msg                = "修改成功！";
				} catch (Exception e) {
					logger.error("录取查询后更改联系地址出错：{}"+e.fillInStackTrace());
					msg                = "修改失败："+e.getMessage();
					success			   = Constants.BOOLEAN_NO;
				} 
			}else {
				    msg                = "修改失败，证件号码有误，查无此人！";
			}
		}else {
					msg                = "修改失败，证件号码为空！";
		}
		
		map.put("msg", msg);
		map.put("success", success);
		renderJson(response,JsonUtils.mapToJson(map));
	}
//	/**
//	 * 录取查询后更改QQ
//	 * @param request
//	 * @param response
//	 */
//	@RequestMapping("/edu3/recruit/matriculate/studentmatriculatequery-editQq.html")
//	public void editQq(HttpServletRequest request,HttpServletResponse response){
//		String qq 				  = ExStringUtils.trimToEmpty(request.getParameter("qq"));	// 联系地址
//		String certNum 				  = ExStringUtils.trimToEmpty(request.getParameter("certNum"));	// 证件号码
//		Map<String,Object> map        = new HashMap<String, Object>();
//		String success                = Constants.BOOLEAN_NO;
//		String msg                    = "";
//	
//		if (StringUtils.isNotEmpty(certNum)) {
//			List<EnrolleeInfo> list   = enrolleeInfoService.findByHql(" from "+EnrolleeInfo.class.getSimpleName()+" where isDeleted=?  and studentBaseInfo.certNum=?",0,certNum);
//			if (ExCollectionUtils.isNotEmpty(list)) {
//				EnrolleeInfo   ei     = list.get(0);
//				StudentBaseInfo info  = ei.getStudentBaseInfo();
//				try {
//					info.setQq(qq);
//					ei.setStudentBaseInfo(info);
//					enrolleeInfoService.update(ei);
//					success			   = Constants.BOOLEAN_YES;
//					msg                = "修改成功！";
//				} catch (Exception e) {
//					logger.error("录取查询后更改QQ出错：{}"+e.fillInStackTrace());
//					msg                = "修改失败："+e.getMessage();
//					success			   = Constants.BOOLEAN_NO;
//				} 
//			}else {
//				    msg                = "修改失败，证件号码有误，查无此人！";
//			}
//		}else {
//					msg                = "修改失败，证件号码为空！";
//		}
//		
//		map.put("msg", msg);
//		map.put("success", success);
//		renderJson(response,JsonUtils.mapToJson(map));
//	}
	
	/**
	 * 编辑报名信息
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/recruit/enroll/edit.html")
	public String edit(String resourceid,HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		try {
			logger.info("编辑报名信息 所传的参数：resourceid="+resourceid);
			EnrolleeInfo enrolleeInfo = new EnrolleeInfo();
			if (StringUtils.isNotEmpty(resourceid)) {
				enrolleeInfo = enrolleeInfoService.get(resourceid);
			}
			model.addAttribute("enrolleeinfo", enrolleeInfo);
		} catch (Exception e) {
			logger.error("编辑报名信息出错", e);
		}
		
		return "/edu3/roll/enrolschool/enrolshcool-edit-form";
	}
	
	/**
	 * 保存报名信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/recruit/enroll/save.html")
	public void save(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String statusCode = "200";
		String message = "保存成功！";
		try {
			String resourceid = StringUtils.defaultIfEmpty(request.getParameter("resourceid"), "");
			String noReportReason = StringUtils.defaultIfEmpty(request.getParameter("noReportReason"), "");
			String isStudyFollow = StringUtils.defaultIfEmpty(request.getParameter("isStudyFollow"), "");
			String memo = StringUtils.defaultIfEmpty(request.getParameter("memo"), "");
			if (StringUtils.isNotEmpty(resourceid)) {
				EnrolleeInfo enrolleeInfo = enrolleeInfoService.get(resourceid);
				enrolleeInfo.setNoReportReason(noReportReason);
				enrolleeInfo.setIsStudyFollow(isStudyFollow);
				enrolleeInfo.setMemo(memo);
				enrolleeInfoService.saveOrUpdate(enrolleeInfo);
			}
		} catch (Exception e) {
			logger.error("保存报名信息出错", e);
			statusCode = "300";
			message = "保存失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 录取查询
	 * 
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/recruitQuery.html")
	public String studentecruitQuery(HttpServletRequest request,ModelMap model) throws WebException {

		Map<String,Object> condition  = new HashMap<String, Object>();
		User user                     = SpringSecurityHelper.getCurrentUser();
		String fromFlag               = StringUtils.trimToEmpty(request.getParameter("fromFlag"));//
		String certNum 				  = StringUtils.trimToEmpty(request.getParameter("certNum")).toUpperCase();	// 证件号码,默认加大写
		String studentName 				  = StringUtils.trimToEmpty(request.getParameter("studentName"));	// 姓名
		String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();

		model.addAttribute("school",school);
		model.addAttribute("schoolCode",schoolCode);
		
		String message 			  = " ";
		//StringUtils.isNotEmpty(enrolleeCode)&&
		// 身份证号为空验证
		if (StringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
			condition.put("isDeleted", 0);
			//condition.put("enrolleeCode", enrolleeCode);
		}else {
			model.addAttribute("message","请输入身份号码！");
		}
		
		// 姓名为空验证
		if (StringUtils.isNotEmpty(studentName)) {
			condition.put("studentName", studentName);
		}else {
			model.addAttribute("message","请输入姓名！");
		}
		
		if(StringUtils.isNotEmpty(certNum) && StringUtils.isNotEmpty(studentName) ){
			condition.put("isDeleted", 0);
			StringBuffer hql = new StringBuffer(" from "+EnrolleeInfo.class.getSimpleName()+" ere  join fetch  ere.recruitMajor "
					+ " major join fetch major.recruitPlan  where ere.isDeleted=:isDeleted and ere.studentBaseInfo.certNum=:certNum "
					+ " and ere.studentBaseInfo.name=:studentName ");
			
			
			List<EnrolleeInfo> list   = enrolleeInfoService.findByHql(hql.toString(),condition);
			
			
			if (list.size() == 0) {
				message = "你输入的查询条件有误，查无此人！";
				
			} else if (list.size() > 1) {
				message = "由于出现重名的情况，请输入你的考生号或者身份证号再继续查询！";
				
			} else {
				
				try {
					
					EnrolleeInfo ei = list.get(0);
					
					/*Date statTime   = ei.getRecruitMajor().getRecruitPlan().getEnrollStartTime();
					Date endTime    = ei.getRecruitMajor().getRecruitPlan().getEnrollEndTime();
					Date currentTime= new Date();
					if (null!=endTime) {
						endTime     = ExDateUtils.addDays(endTime, 1);
					}

					if (null!=statTime && null!=endTime) {	
						if (currentTime.getTime()>=statTime.getTime() && currentTime.getTime()<=endTime.getTime()) {*/
								if (Constants.BOOLEAN_YES.equals(StringUtils.trimToEmpty(ei.getIsMatriculate()))) {
									List<Map<String,Object>> fee = baseSupportJdbcDao.getBaseJdbcTemplate().findForList(" select t.recpayfee from edu_roll_fistudentfee t where t.studentid = ? and t.chargeyear = ? ", new Object[]{ei.getMatriculateNoticeNo(),ExDateUtils.getCurrentYear()});
									if (null!=fee && !fee.isEmpty()) {
										model.addAttribute("fee",fee.get(0));
									}
		
									//根据KSH（因为考生号各地不一样，有机会重复，须加上招生计划，精确查询）
								
									List<Map<String,Object>> fee1 = baseSupportJdbcDao.getBaseJdbcTemplate().findForList("select e.RESOURCEID,e.XZ from EDU_RECRUIT_EXAMINEE e where e.ZKZH=? and e.recruitplanid =? ", 
											new Object[]{ei.getExamCertificateNo(), ei.getRecruitMajor().getRecruitPlan().getResourceid()});
									
									if (null!=fee1 && !fee1.isEmpty()) {
										model.addAttribute("fee1",fee1.get(0));
										//添加45开头的学生查询条件
										updateInputUnit1(fee1.get(0).get("RESOURCEID").toString(),model,certNum);
									}
									RecruitMajor rm = ei.getRecruitMajor();
						            Major m = rm.getMajor();
									 String zy = m.getMajorName().replace("专科", "").replace("本科", "").replace("高起专","").replace("专升本", "");
									 ei.getRecruitMajor().getMajor().setMajorName(zy);
									model.addAttribute("ei", ei);
									model.addAttribute("schoolName", ei.getBranchSchool().getUnitName());
								} else if (Constants.BOOLEAN_NO.equals(StringUtils.trimToEmpty(ei.getIsMatriculate()))) {
									message = "没有查到相关录取信息";
								}
						/*}else {
								message = "录取查询未开放，或已过录取查询时间！<br/>查询时间："+
										  ExDateUtils.formatDateStr(statTime, "yyyy-MM-dd")+" 至 "+
										  ExDateUtils.formatDateStr(endTime, "yyyy-MM-dd");
						}
					}else {
						message = "录取查询未开放！";
					}*/
					
				} catch (Exception e) {
					
					logger.error("查询录取状态出错："+e.getMessage());
					message   = "查询录取状态出错："+e.getMessage();
					return "/edu3/recruit/matriculate/onlyrecruitquery-list";
				}
		
			}
			model.addAttribute("message", message);
			model.addAttribute("fromFlag",fromFlag);
			model.addAttribute("condition",condition);
			model.addAttribute("currentUser", user);
		} else {
			message = "请输入姓名和身份证号码！";
			model.addAttribute("message", message);
		}
	            
		return "/edu3/recruit/matriculate/onlyrecruitquery-list";
	}
}
