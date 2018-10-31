
/**
 * <code>StuChangeInfoController</code><p>
 * 
 * @author：广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:36:32
 * @see 
 * @version 1.0
*/

package com.hnjk.edu.roll.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.base.JRBasePrintPage;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.CardNoUtils;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Classic;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.StudentBaseInfo;
import com.hnjk.edu.basedata.service.IClassicService;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.IStudentService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.service.IRefundbackService;
import com.hnjk.edu.finance.service.IReturnPremiumService;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.vo.StuReturnFeeCommissionInfoVo;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.portal.model.Message;
import com.hnjk.edu.portal.model.MessageReceiver;
import com.hnjk.edu.portal.service.ISysMsgService;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StuChangeInfo;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IStuChangeInfoService;
import com.hnjk.edu.roll.service.IStudentInfoChangeJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingActivityTimeSettingService;
import com.hnjk.edu.teaching.service.ITeachingGuidePlanService;
import com.hnjk.edu.teaching.service.ITeachingPlanService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
import com.opensymphony.workflow.Workflow;
import com.opensymphony.workflow.spi.WfAgent;
import com.opensymphony.workflow.spi.WorkflowEntry;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * <code>StuChangeInfoController</code><p>
 * 学籍异动表.
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-4-26 下午03:36:32
 * @see 
 * @version 1.0
 */
@Controller
public class StuChangeInfoController extends FileUploadAndDownloadSupportController  {

	private static final long serialVersionUID = 2957572768843140121L;
	
	@Autowired
	@Qualifier("stuchangeinfoservice")
	private IStuChangeInfoService stuChangeInfoService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;

	@Autowired
	@Qualifier("classicService")
	private IClassicService classicService;

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService; 
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingGuidePlanService;

	@Autowired
	@Qualifier("studentInfoChangeJDBCService")
	private IStudentInfoChangeJDBCService studentInfoChangeJDBCService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("teachingActivityTimeSettingService")
	private ITeachingActivityTimeSettingService teachingActivityTimeSettingService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("studentService")
	private IStudentService studentService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
//	@Autowired
//	@Qualifier("exameeInfoService")
//	private IExameeInfoService exameeInfoService;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;

	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingPlanService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("refundbackService")
	private IRefundbackService refundbackService;
	
	@Autowired
	@Qualifier("returnPremiumService")
	private IReturnPremiumService returnPremiumService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("sysMsgService")
	private ISysMsgService sysMsgService;
	
	Page page=null;
	
	/**
	 * 学籍异动
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/stuchange-list.html")
	public String exeList(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("applicationDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String stuName = ExStringUtils.trimToEmpty(request.getParameter("stuName"));//姓名	
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String finalAuditStatus = ExStringUtils.trimToEmpty(request.getParameter("finalAuditStatus"));//审核结果
	
		String applicationDateb = ExStringUtils.trimToEmpty(request.getParameter("applicationDateb"));//申请时间起
		String applicationDatee = ExStringUtils.trimToEmpty(request.getParameter("applicationDatee"));//申请时间终
		String auditDateb = ExStringUtils.trimToEmpty(request.getParameter("auditDateb"));//审核时间起
		String auditDatee = ExStringUtils.trimToEmpty(request.getParameter("auditDatee"));//审核时间终
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String classes      = ExStringUtils.trimToEmpty(request.getParameter("classes"));
		String stuStatusRes = stuStatus;
		String accountStatus = null;
		
		String changeProperty = ExStringUtils.trimToEmpty(request.getParameter("changeProperty"));
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
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
		if(null!=accountStatus){
			condition.put("accountStatus", accountStatus);
		}
		model.addAttribute("stuStatusSet",studentStatusRes.toString());
		model.addAttribute("stuStatusRes", stuStatusRes);
		String stuChange = ExStringUtils.trimToEmpty(request.getParameter("stuChange"));//异动类型
		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);	//学籍状态
		}
		if(ExStringUtils.isNotEmpty(stuChange)) {
			condition.put("stuChange", stuChange);	//异动类型
		}
		if(ExStringUtils.isNotEmpty(stuName)) {
			condition.put("stuName", stuName);		//姓名
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);				//专业
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);		//层次
		}
		
		if(ExStringUtils.isNotEmpty(finalAuditStatus)) {
			condition.put("finalAuditStatus", finalAuditStatus);//审核结果
		}
		if(ExStringUtils.isNotEmpty(applicationDateb)) {
			condition.put("applicationDateb", applicationDateb);//申请时间起
		}
		if(ExStringUtils.isNotEmpty(applicationDatee)) {
			condition.put("applicationDatee", applicationDatee);//申请时间终
		}
		if(ExStringUtils.isNotEmpty(auditDateb)) {
			condition.put("auditDateb", auditDateb);//审核时间起
		}
		if(ExStringUtils.isNotEmpty(auditDatee)) {
			condition.put("auditDatee", auditDatee);//审核时间终
		}
		if(ExStringUtils.isNotEmpty(classes)) {
			condition.put("classes", classes);
		}
		
	    //新增的三个查询条件
		String gradeid =ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String learningStyle =ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String stuNum =ExStringUtils.trimToEmpty(request.getParameter("stuNum"));
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);		      //年级
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle", learningStyle);	  //学习形式
		}
		if(ExStringUtils.isNotEmpty(stuNum)) {
			condition.put("stuNum", stuNum);				  //学号
		}

		//如果为校外学习中心,并且不是学生
		String brSchoolId = "";
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().				
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0
				&&!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(cureentUser.getUserType())
				)//条件描述：用户的组织对象不为NULL，&& 用户组织类型为校外学习中心&& 当前用户的用户类型不是学生
		{
			condition.put("isBranchSchool",Constants.BOOLEAN_YES);
			condition.put("branchSchool", cureentUser.getOrgUnit().getResourceid());
			brSchoolId = cureentUser.getOrgUnit().getResourceid();
			model.addAttribute("unittype", CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue());
		}
		//如果为学生身份，则只查改学生的申请记录
		else if(CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(cureentUser.getUserType())){
			//获取当前学生的默认学籍
			String acquiesceStudentid = cureentUser.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			condition.put("acquiesceStudentid", acquiesceStudentid);			
			Page page = stuChangeInfoService.findByCondition(condition, objPage);
			model.addAttribute("stulist", page);
			model.addAttribute("condition", condition);
			return "/edu3/roll/stuchangeinfo/stuchangeinfo-stu-list";
		}
		//学习中心
		else{	
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
		}
		if(ExStringUtils.isNotEmpty(changeProperty)){
			condition.put("changeProperty", changeProperty);
		}
		
		Set<Role> ro=cureentUser.getRoles();
		if(ro.size()==1){
			for (Role role : ro) {
				// 判断用户是否只有一个角色并且是班主任角色
				if("班主任角色".equals(role.getRoleName())){
					condition.put("classesMasterId", cureentUser.getResourceid());
				}
				// 判断用户是否只有一个角色并且是教材费管理员角色
				if("ROLE_TEXTBOOKFEE_ADMIN".equals(role.getRoleCode())){
					condition.put("finalAuditStatus", "Y");
				}
			}
			
		}
			
		Page page = stuChangeInfoService.findByCondition(condition, objPage);
		model.addAttribute("stulist", page);
		model.addAttribute("condition", condition);
		
	/*	StringBuffer unitOption = new StringBuffer("<option value=''></option>");
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
		model.addAttribute("unitOption",unitOption);
		model.addAttribute("majorSelect4",graduationQualifService.getGradeToMajor1(gradeid,major,"stuchange_major","major","searchExamResultMajorClick()",classic));
		model.addAttribute("classesSelect4",graduationQualifService.getGradeToMajorToClasses1(gradeid,major,classes,branchSchool,"stuchange_classesid","classesId",classic));
*/
		model.addAttribute("brSchoolId",brSchoolId);
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		model.addAttribute("schoolCode",schoolCode);
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-list";
	}
	
	/**
	 * 编辑(流程)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/edit.html")
	public String edit(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception{
		String resourceid 			  = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));					// 获取表单ID
		String wf_id 	  			  = ExStringUtils.trimToEmpty(request.getParameter("wf_id"));	// 获取流程实例ID
		String APP_FROM   			  = ExStringUtils.trimToEmpty(request.getParameter("APP_FROM"));		
		User currentUser = SpringSecurityHelper.getCurrentUser();
		StudentInfo studentInfo = null;
		boolean isManager=false;//是否是教务员登录
		//获取当前学生的默认学籍ID
		if("student".equalsIgnoreCase(currentUser.getUserType())){
			String defaultRollId = currentUser.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			studentInfo = studentInfoService.findUnique("from "+StudentInfo.class.getSimpleName()+" where isDeleted = ? and resourceid = ?", 0,defaultRollId);
		}else{
			isManager=true;
		}
				
		StuChangeInfo stuChangeInfo = null;
		if(ExStringUtils.isNotEmpty(APP_FROM) && ExStringUtils.isNotEmpty(wf_id)) {
			Workflow wf  = WfAgent.getWorkflow(request);
			
			Map<String, Object> metaMap = wf.getWorkflowDescriptMeta("stuchangeinfo", Long.parseLong(wf_id), "step");
			if(null != metaMap && !metaMap.isEmpty()){
					String deny_form_input = (String)metaMap.get("deny_form_input");
					List<String> metaList = Arrays.asList(deny_form_input.split("\\,"));
					model.addAttribute("denyList", metaList);
			}
			
			
			stuChangeInfo = stuChangeInfoService.findUniqueByProperty("wf_id", Long.parseLong(wf_id));
		}else{
			// 通过id判断,id不为空就查询出来,为空则 new
			if(ExStringUtils.isNotEmpty(resourceid)){
				stuChangeInfo =  stuChangeInfoService.get(resourceid);
			}else{
				stuChangeInfo = new StuChangeInfo();
				if(null != studentInfo){
					stuChangeInfo.setStudentInfo(studentInfo);
				}
				
			}
				
		}
		//过滤掉学生的专业
		String majorSql = "from "+Major.class.getSimpleName()+" where isDeleted = ?";		
			
		List<Major> majors = majorService.findByHql(majorSql, 0);
		model.addAttribute("majors", majors);
		model.addAttribute("isManager",isManager);
		model.addAttribute("stuChangeInfo", stuChangeInfo);
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-from";
	}
	
	/**
	 * 替学生异动
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/self/edit.html")
	public String editBySelf(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid 			  = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));					// 获取表单ID
		String finished               = ExStringUtils.trimToEmpty(request.getParameter("finished"));
		User currentUser = SpringSecurityHelper.getCurrentUser();
		
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		StudentInfo studentInfo = null;
		boolean isManager=false;//是否是教务员登录
		//获取当前学生的默认学籍ID
		if("student".equalsIgnoreCase(currentUser.getUserType())){
			String defaultRollId = currentUser.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			studentInfo = studentInfoService.findUnique("from "+StudentInfo.class.getSimpleName()+" where isDeleted = ? and resourceid = ?", 0,defaultRollId);
		}else{
			isManager=true;
		}
				
		StuChangeInfo stuChangeInfo = null;
		
		if(ExStringUtils.isNotEmpty(resourceid)){//修改
			stuChangeInfo = stuChangeInfoService.get(resourceid);
			List<Attachs> list = attachsService.findAttachsByFormId(resourceid);
			model.addAttribute("attachList",list);
		}else{//新增
			stuChangeInfo = new StuChangeInfo();
			stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_YES);
			stuChangeInfo.setApplicationDate(new Date());
			if(null != studentInfo) {
				stuChangeInfo.setStudentInfo(studentInfo);
			}
		}		
		
		//过滤掉学生的专业
		//String majorSql = "from "+Major.class.getSimpleName()+" where isDeleted = ?";		
			
		//List<Major> majors = majorService.findByHql(majorSql, 0);
		//model.addAttribute("majors", majors);
		model.addAttribute("isManager",isManager);
		model.addAttribute("stuChangeInfo", stuChangeInfo);
		model.addAttribute("opMan",currentUser.getCnName());
		model.addAttribute("opManId",currentUser.getResourceid());
		model.addAttribute("finished",finished);
		model.addAttribute("schoolCode", schoolCode);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar   = new GregorianCalendar(); 
		calendar.setTime(new Date()); 
		calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
		Date date			= calendar.getTime();
		model.addAttribute("currentDateAfter", df.format(date));
		model.addAttribute("currentDate", df.format(new Date()));
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-self-from2";
	}
	
	
	/**
	 * 班主任角色-替学生异动
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/stuid.html")
	public String editBySelf1(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException{
		
		String stuNo=request.getParameter("stuchangeNO");
		if(stuNo!=null){
			model.addAttribute("stuchangeId", stuNo);
		}
		String resourceid 			  = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));					// 获取表单ID
		String finished               = ExStringUtils.trimToEmpty(request.getParameter("finished"));
		User currentUser = SpringSecurityHelper.getCurrentUser();
		
		StudentInfo studentInfo = null;
		boolean isManager=false;//是否是教务员登录
		//获取当前学生的默认学籍ID
		if("student".equalsIgnoreCase(currentUser.getUserType())){
			String defaultRollId = currentUser.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			studentInfo = studentInfoService.findUnique("from "+StudentInfo.class.getSimpleName()+" where isDeleted = ? and resourceid = ?", 0,defaultRollId);
		}else{
			isManager=true;
		}
				
		StuChangeInfo stuChangeInfo = null;
		
		if(ExStringUtils.isNotEmpty(resourceid)){//修改
			stuChangeInfo = stuChangeInfoService.get(resourceid);
		}else{//新增
			stuChangeInfo = new StuChangeInfo();
			stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_YES);
			stuChangeInfo.setApplicationDate(new Date());
			if(null != studentInfo) {
				stuChangeInfo.setStudentInfo(studentInfo);
			}
		}		
		
		//过滤掉学生的专业
		//String majorSql = "from "+Major.class.getSimpleName()+" where isDeleted = ?";		
			
		//List<Major> majors = majorService.findByHql(majorSql, 0);
		//model.addAttribute("majors", majors);
		model.addAttribute("isManager",isManager);
		model.addAttribute("stuChangeInfo", stuChangeInfo);
		model.addAttribute("opMan",currentUser.getCnName());
		model.addAttribute("opManId",currentUser.getResourceid());
		model.addAttribute("finished",finished);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar   = new GregorianCalendar(); 
		calendar.setTime(new Date()); 
		calendar.add(Calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
		Date date			= calendar.getTime();
		model.addAttribute("currentDateAfter", df.format(date));
		model.addAttribute("currentDate", df.format(new Date()));
		System.out.println("学生学号："+stuNo);
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-self-from211";
	}
	
	
	
	
	/**
	 * 根据教学类型获取专业
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/getmajor.html")
	public void getTeachTypeMajor(HttpServletRequest request, HttpServletResponse response){
		
		String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String gradeId   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String brSchoolId= ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (ExStringUtils.isNotBlank(brSchoolId)) {
			map.put("brSchoolId", brSchoolId);
		}
		if (ExStringUtils.isNotBlank(gradeId)) {
			map.put("gradeId", gradeId);
		}
		if (ExStringUtils.isNotBlank(teachType)) {
			map.put("teachType", teachType);
		}
		map.put("yesStr", Constants.BOOLEAN_YES);
		map.put("isDeleted",0);
		
		if(ExStringUtils.isNotBlank(teachType)&&ExStringUtils.isNotBlank(brSchoolId)&&ExStringUtils.isNotBlank(gradeId)){

			String majorSql = " select distinct m.resourceid,m.majorname from edu_recruit_major rm  inner join edu_base_major m on rm.majorid = m.resourceid ";		
			majorSql       += " and exists( select brm.resourceid from edu_recruit_brschmajor brm inner join edu_recruit_brschplan brp on brm.brschplanid = brp.resourceid ";
			majorSql       += " 			   and brp.isaudited = :yesStr and brp.branchschoolid = :brSchoolId  ";
			majorSql       += "				 inner join edu_recruit_recruitplan rp on brp.recruitplanid = rp.resourceid and rp.gradeid = :gradeId ";
			majorSql       += " 			 where brm.isdeleted= :isDeleted and brm.ispassed = :yesStr and brm.recruitmajorid = rm.resourceid  )";
			majorSql       += " where rm.isdeleted = :isDeleted and rm.teachingtype =:teachType";
			
			List<Map<String,Object>> majors = new ArrayList<Map<String,Object>>();
			try {
				majors                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(majorSql,map);
			} catch (Exception e) {
				
			}
			if(null != majors && !majors.isEmpty()){
				map.put("majorList", majors);
			}else{
				map.put("error", "没有对应的专业!");
			}
			
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 根据新办学单位 学习形式 层次 年级 班级 教学类型获取专业
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/getInfosmajor.html")
	public void getInfosMajor(HttpServletRequest request, HttpServletResponse response){
		
		String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String gradeId   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String brSchoolId= ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String changeType = ExStringUtils.trimToEmpty(request.getParameter("changeType"));
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (ExStringUtils.isNotBlank(brSchoolId)) {
			map.put("brSchoolId", brSchoolId);
		}
		if (ExStringUtils.isNotBlank(gradeId)) {
			map.put("gradeId", gradeId);
		}
		if (ExStringUtils.isNotBlank(teachType)) {
			map.put("teachType", teachType);
		}
		if (ExStringUtils.isNotBlank(classicId)) {
			map.put("classicId", classicId);
		}
		
		
		if(ExStringUtils.isNotBlank(teachType)&&ExStringUtils.isNotBlank(brSchoolId)&&ExStringUtils.isNotBlank(gradeId)){

			//据说成教的招生不在系统。所以没有各学习中心的招生专业这类数据
			StringBuffer majorSql = new StringBuffer();
			
			majorSql.append(" select distinct m.resourceid,m.majorCode||'-'||m.majorname  majorname from edu_teach_plan a ,edu_teach_guiplan b ,edu_base_major m where a.resourceid = b.planid and a.majorid = m.resourceid and a.isdeleted = 0 and b.isdeleted =0 "); 
			majorSql.append(" and( (a.brschoolid is null ) or (a.brschoolid is not null and a.brschoolid = :brSchoolId)) ");
			majorSql.append(" and a.classicid  = :classicId ");
			majorSql.append(" and b.gradeid    = :gradeId ");
			majorSql.append(" and a.schooltype = :teachType ");
			
			List<Map<String,Object>> majors = new ArrayList<Map<String,Object>>();
			try {
				majors                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(majorSql.toString(),map);
			} catch (Exception e) {
				
			}
			if(null != majors && !majors.isEmpty()){
				if("82".equals(changeType)){
						boolean hasMajor = false;
						List<Map<String,Object>> newMajors = new ArrayList<Map<String, Object>>();
						for(Map<String,Object> m:majors){
							if(m.get("resourceid").equals(major)) {
								hasMajor = true;
								newMajors.add(m);
								break;
							}
								
						}
						if(hasMajor){
							map.put("majorList", newMajors);
						}else{
							map.put("error", "该校没有对应的专业，不能对学生进行学籍异动操作!");
						}
					}else{
						map.put("majorList", majors);
					}
				
				
			}else{
				map.put("error", "没有对应的专业!");
			}
			
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 根据新办学单位 学习形式 层次 年级 班级 教学类型 专业获取班级
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/getClasses.html")
	public void getClasses(HttpServletRequest request, HttpServletResponse response){
		
		String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String gradeId   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String brSchoolId= ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String majorId   = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		
		String studentresourceid = ExStringUtils.trimToEmpty(request.getParameter("studentresourceid"));
		if(ExStringUtils.isNotEmpty(studentresourceid)){
			StudentInfo stu = studentInfoService.get(studentresourceid);
			if(ExStringUtils.isEmpty(teachType)){
				teachType = stu.getTeachingType();
			}
			if(ExStringUtils.isEmpty(gradeId)){
				gradeId   = stu.getGrade().getResourceid();
			}
			if(ExStringUtils.isEmpty(brSchoolId)){
				brSchoolId= stu.getBranchSchool().getResourceid();
			}
			if(ExStringUtils.isEmpty(classicId)){
				classicId = stu.getClassic().getResourceid();
			}
			if(ExStringUtils.isEmpty(majorId)){
				majorId   = stu.getMajor().getResourceid();
			}
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (ExStringUtils.isNotBlank(brSchoolId)) {
			map.put("brSchoolId", brSchoolId);
		}
		if (ExStringUtils.isNotBlank(gradeId)) {
			map.put("gradeId", gradeId);
		}
		if (ExStringUtils.isNotBlank(teachType)) {
			map.put("teachType", teachType);
		}
		if (ExStringUtils.isNotBlank(classicId)) {
			map.put("classicId", classicId);
		}
		if (ExStringUtils.isNotBlank(majorId)) {
			map.put("majorId", majorId);
		}
		
		if(ExStringUtils.isNotBlank(teachType)&&ExStringUtils.isNotBlank(brSchoolId)&&ExStringUtils.isNotBlank(gradeId)){

			//据说成教的招生不在系统。所以没有各学习中心的招生专业这类数据
			StringBuffer majorSql = new StringBuffer();
			
			majorSql.append(" select distinct t.resourceid,t.classesname from edu_roll_classes t where t.isdeleted= 0 ");
			majorSql.append(" and t.gradeid = :gradeId ");  
			majorSql.append(" and t.classicid = :classicId ");
			majorSql.append(" and t.majorid = :majorId ");
			majorSql.append(" and t.orgunitid =  :brSchoolId ");
			majorSql.append(" and t.teachingtype = :teachType ");
			
			
			List<Map<String,Object>> classes = new ArrayList<Map<String,Object>>();
			try {
				classes                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(majorSql.toString(),map);
			} catch (Exception e) {
				
			}
			if(null != classes && !classes.isEmpty()){
				map.put("classesList", classes);
			}else{
				map.put("error", "没有对应的班级!");
			}
			
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 根据教学类型获取教学计划—复学、转专业
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/getteachplan.html")
	public void getTeachPlanBeforeS(HttpServletRequest request, HttpServletResponse response){
		
		String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String gradeId   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String brSchoolId= ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String majorId   = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (ExStringUtils.isNotBlank(teachType)) {
			map.put("teachType", teachType);
		}
		
		List<Map<String,Object>> teachPlans = new ArrayList<Map<String,Object>>();
		map.put("teachPlanList", teachPlans);
		if(ExStringUtils.isNotBlank(teachType)&&ExStringUtils.isNotBlank(brSchoolId)&&ExStringUtils.isNotBlank(gradeId)){
			StringBuffer teachPlanSql_ = new StringBuffer();
			teachPlanSql_.append(" select distinct b.resourceid,case when a.brschoolid is null then m.majorname||'-'||c.classicname||'('||a.versionnum||')' else u.unitshortname||g.gradename||m.majorname||c.classicname end  teachplanname ");
			teachPlanSql_.append(" from edu_teach_plan a inner join  edu_teach_guiplan b  on a.resourceid = b.planid "); 
			teachPlanSql_.append(" left join edu_base_grade g on b.gradeid = g.resourceid ");
			teachPlanSql_.append(" left join edu_base_major m on a.majorid = m.resourceid ");
			teachPlanSql_.append(" left join edu_base_classic c on a.classicid = c.resourceid "); 
			teachPlanSql_.append(" left join hnjk_sys_unit u on u.resourceid = a.brschoolid ");
			teachPlanSql_.append(" where  a.isdeleted = 0 and b.isdeleted =0 ");	
    		teachPlanSql_.append(" and( (a.brschoolid is null ) or (a.brschoolid is not null and a.brschoolid = :brSchoolId)) ");
			teachPlanSql_.append(" and a.classicid  = :classicId ");
			teachPlanSql_.append(" and b.gradeid    = :gradeId ");
			teachPlanSql_.append(" and a.schooltype = :teachType ");
			teachPlanSql_.append(" and a.majorid    = :majorId ");
			try {
				if (ExStringUtils.isNotBlank(classicId)) {
					map.put("classicId", classicId);
				}
				if (ExStringUtils.isNotBlank(brSchoolId)) {
					map.put("brSchoolId", brSchoolId);
				}
				if (ExStringUtils.isNotBlank(gradeId)) {
					map.put("gradeId", gradeId);
				}
				if (ExStringUtils.isNotBlank(majorId)) {
					map.put("majorId", majorId);
				}
				teachPlans                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(teachPlanSql_.toString(),map);
			} catch (Exception e) {
				logger.debug("获取教学计划异常:"+e.fillInStackTrace());
			}
			
			
			if(null != teachPlans && !teachPlans.isEmpty()){
				map.put("teachPlanList", teachPlans);
			}else{
				map.put("error", (teachPlans.size()==0?"没有可用教学计划":""));
			}
			
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 查看学生学籍异动
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/view.html")
	public String stuChangeInfoView(String studentId ,HttpServletRequest request,ModelMap model) throws WebException{
		if(ExStringUtils.isNotEmpty(studentId)){
			List<StuChangeInfo> stuChangeInfoList = stuChangeInfoService.findByHql("from "+StuChangeInfo.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ? order by applicationDate desc ", 0,studentId);
			if(null != stuChangeInfoList && !stuChangeInfoList.isEmpty()){
				StuChangeInfo stuChangeInfo = stuChangeInfoList.get(0);
				model.addAttribute("stuChangeInfo", stuChangeInfo);
				model.addAttribute("stuChangeInfoList", stuChangeInfoList);
			}			
		}
		String stuchangeinfoid = ExStringUtils.trimToEmpty(request.getParameter("resourceid")); 
		if(ExStringUtils.isNotEmpty(stuchangeinfoid)){
			model.addAttribute("stuchangeinfoid", stuchangeinfoid);
		}
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-view";
	}
	
	/**
	 *我的学习 >>我的申请 >> 学习异动申请  >>删除申请  	
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduation/student/stuApplyDel.html")
	public void stuApplyDel(HttpServletRequest request, HttpServletResponse response,String resourceid)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try{
			if(ExStringUtils.isNotEmpty(resourceid)){
				stuChangeInfoService.batchDelete(resourceid.split(","));
				map.put("statusCode", 200);
				map.put("message", "删除成功！");				
				map.put("forward", request.getContextPath() +"/edu3/schoolroll/graduation/student/list.html");
			}
		}catch(Exception e){			
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 审核
	 * @param request
	 * @param response
	 * @param resourceid
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/graduation/student/stuaudit.html")
	public void stuApplyAudit(HttpServletRequest request, HttpServletResponse response,String resourceid)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String auditType = ExStringUtils.trimToEmpty(request.getParameter("auditType"));
		try{
			if(ExStringUtils.isNotEmpty(resourceid) && ExStringUtils.isNotBlank(auditType)){
				String[] studentChangeInfoIds = resourceid.split(",");
				List<String> batchAudit = new ArrayList<String>(0);
				String statusCode = "200";
				String message = "";
				int successNum = 0;
				int failNum = 0;
				for (String studentChangeInfoId : studentChangeInfoIds) {
					StuChangeInfo stuChangeInfo = stuChangeInfoService.get(studentChangeInfoId);
					List<ExamResults> examList = examResultsService.findByCriteria(Restrictions.eq("studentInfo.resourceid", stuChangeInfo.getStudentInfo().getResourceid()),Restrictions.eq("isDeleted",0),Restrictions.ne("checkStatus","4"));
					if(Constants.BOOLEAN_YES.equals(stuChangeInfo.getFinalAuditStatus())){
						failNum ++;
						statusCode = "300";
						message    += Tools.colorStr(stuChangeInfo.getStudentInfo().getStudentName(),"red")+"【"+JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange", stuChangeInfo.getChangeType())+"】异动申请已经通过审核，<br/>无法再次审核，如有需要，请为该学生<br/>申请一条新的异动。";	
					}else if("82".equals(stuChangeInfo.getChangeType()) &&!Constants.BOOLEAN_YES.equals(stuChangeInfo.getChangeBrschoolAccept())){
						failNum ++;
						statusCode = "300";
						message    += Tools.colorStr(stuChangeInfo.getStudentInfo().getStudentName(), "red")+"的教点异动申请，接收中心未确认接收！";
					}else if(examList!=null&&examList.size()>0 && !"13".equals(stuChangeInfo.getChangeType())){
						failNum ++;
						statusCode = "300";						
						message    += Tools.colorStr(stuChangeInfo.getStudentInfo().getStudentName(),"red")+"&nbsp&nbsp"+stuChangeInfo.getStudentInfo().getStudyNo()+"&nbsp&nbsp"+"存在以下课程的成绩未发布，请撤销成绩或将成绩发布之后再进行异动审核：";
						for(ExamResults er :examList){
							message +=Tools.colorStr("<br/>"+er.getCourse().getCourseName()+"&nbsp&nbsp&nbsp&nbsp"+er.getExamInfo().getExamSub().getBatchName()+"&nbsp&nbsp&nbsp&nbsp"+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getExamInfo().getExamSub().getExamType()),"red");
						}
					}else{
						batchAudit.add(studentChangeInfoId);//未通过审核的申请记录
					}
					if(ExStringUtils.isNotEmpty(message)) {
						message += "<br/>"+Tools.dashLine("red", 1);
					}
				}
				String[] res = new String[batchAudit.size()];
				batchAudit.toArray(res);
				Map<String,Object> msgMap = stuChangeInfoService.auditBatchStuChangeInfo(res,auditType,request);
				message += (String)msgMap.get("msg");
				successNum = (Integer)msgMap.get("successNum");
				failNum = failNum + (batchAudit.size() - successNum);
				String auditResult = "审核成功"+Tools.colorStr(successNum+"", "red") + "条，审核失败" + Tools.colorStr(failNum+"", "red") + "条";
				if(ExStringUtils.isNotEmpty(message)) {
					auditResult += "<br/>"+Tools.dashLine("red", 1);
				}
				
				map.put("statusCode", statusCode);
				map.put("message", auditResult+message);				
				map.put("forward", request.getContextPath() +"/edu3/schoolroll/graduation/student/list.html");
				
			}
		}catch(Exception e){
			e.printStackTrace();
			//logger.error("审核出错："+e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核失败！<br/>"+e.getMessage());
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 转学习中心的异动，接收学习中心确认接收
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/stuchangeinfo/changebrschool/accept.html")
	public void changeBrschoolAccept(String resourceid,HttpServletRequest request,HttpServletResponse response){
	
		List<StuChangeInfo> list = new ArrayList<StuChangeInfo>();
		Map<String,Object> map   = new HashMap<String, Object>();
		StringBuilder msg        = new StringBuilder("");
		Date curDate   			 = ExDateUtils.getCurrentDateTime();
		User curUser             = SpringSecurityHelper.getCurrentUser();
		if (ExStringUtils.isNotBlank(resourceid)) {
			try {
				String [] ids  = resourceid.split(",");
				
				for (int i = 0; i <ids.length; i++) {
					StuChangeInfo info = stuChangeInfoService.get(ids[i]);
					if ("82".equals(info.getChangeType()) &&!Constants.BOOLEAN_YES.equals(info.getFinalAuditStatus())) {
						if (!curUser.getOrgUnit().getResourceid().equals(info.getChangeBrschool().getResourceid())) {
							msg.append("你不是接收学习中心的用户，不允许操作。<font color='red'>"+info.getStudentInfo().getStudentName()+"</font>接收中心为:<font color='red'>"+info.getChangeBrschool().getUnitShortName()+"</font></br>");
							continue;
						}
						info.setAcceptMan(curUser.getCnName());
						info.setAcceptManId(curUser.getResourceid());
						info.setAcceptDate(curDate);
						info.setChangeBrschoolAccept(Constants.BOOLEAN_YES);
						list.add(info);
					}else {
						msg.append("<font color='red'>"+info.getStudentInfo().getStudentName()+"</font>的异动申请已审核通过，或者不是转教点的异动申请，不允许操作!</br>");
					}
				}
				stuChangeInfoService.batchSaveOrUpdate(list);
				if (ExStringUtils.isNotBlank(msg.toString())) {
					map.put("statusCode", 300);
					map.put("message", msg);
				}else {
					map.put("statusCode", 200);
					map.put("message","接收成功!");
				}
			} catch (Exception e) {
				logger.error("转学习中心的异动，接收学习中心确认接收出错：{}"+e.fillInStackTrace());
			}
		}else {
			map.put("statusCode", 300);
			map.put("message", "请选择要确认接收的学生！");
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	/**
	 * (替学生异动)保存、不走流程
	 * 班主任权限
	 * @param stuChangeInfo
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/self/save.html")
	public void doSave(StuChangeInfo stuChangeInfo, HttpServletRequest request, HttpServletResponse response) throws WebException{
		
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));// 获取表单ID
		String stuNum = ExStringUtils.trimToEmpty(request.getParameter("stuNum"));// stuNum	
			
		StudentInfo stu = studentInfoService.findUniqueByProperty("studyNo", stuNum);
		
		Map<String,Object> map = new HashMap<String, Object>();
		String errorMsg = "学籍异动申请出错:";
		String opMan 	= ExStringUtils.trimToEmpty(request.getParameter("opMan"));
		String opManId 	= ExStringUtils.trimToEmpty(request.getParameter("opManId"));
		try {
			//添加判断：只有在学和过期的学籍状态才能申请延期
			if(!"11".equals(stu.getStudentStatus())&&!"18".equals(stu.getStudentStatus())&&"100".equals(stuChangeInfo.getChangeType())){
				errorMsg = errorMsg+stu.getStudentName()+"不是在学或过期状态,不能申请延期的学籍异动。<br/>";
				throw new Exception("违反申请延期异动的条件.");
			}
			if(ExStringUtils.isNotEmpty(resourceid)) {//修改
				StuChangeInfo p_stuChangeInfo = stuChangeInfoService.get(resourceid);
				ExBeanUtils.copyProperties(p_stuChangeInfo, stuChangeInfo);
				changeTypeMethod(p_stuChangeInfo, request, stu);
				p_stuChangeInfo.setStudentInfo(stu);				
				//Date date                   = ExDateUtils.getCurrentDateTime();
				p_stuChangeInfo.setApplicationMan(opMan);
				p_stuChangeInfo.setApplicationManId(opManId);
				//p_stuChangeInfo.setApplicationDate(date);
				stuChangeInfoService.saveOrUpdateStuChangeInfo(p_stuChangeInfo,stu);				
				//stuChangeInfoService.update(p_stuChangeInfo);
				
			}else {
				
				/*Grade curGrade   = gradeService.getDefaultGrade();
				if(null == curGrade){
					map.put("statusCode",300);
					map.put("message", "没有设置默认年级！");
				}*/
				String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				//String yearInfoId = "";
				String term = "";
				if (null==yearTerm) {
					map.put("statusCode",300);
					map.put("message", "没有设置当前学期！");
				}else{
					List<ExamResults> examList = examResultsService.findByCriteria(Restrictions.eq("studentInfo.resourceid", stu.getResourceid()),Restrictions.eq("isDeleted",0),Restrictions.ne("checkStatus","4"));
					if((examList==null||examList.size()>0) && !"13".equals(stuChangeInfo.getChangeType())){//学籍异动申请前（除退学外），判断成绩是否全部发布
						String message="<strong>申请失败:</strong><br>";
						message    += Tools.colorStr(stu.getStudentName(),"red")+"&nbsp&nbsp"+stu.getStudyNo()+"&nbsp&nbsp"+"存在以下课程的成绩未发布，请撤销成绩或将成绩发布之后才能为学生进行学籍异动申请：";
						for(ExamResults er :examList){
							message +=Tools.colorStr("<br/>"+er.getCourse().getCourseName()+"&nbsp&nbsp&nbsp&nbsp"+er.getExamInfo().getExamSub().getBatchName()+"&nbsp&nbsp&nbsp&nbsp"+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getExamInfo().getExamSub().getExamType()),"red");
						}
						map.put("statusCode",300);
						map.put("message", message);
					}else{
						String[] ARRYyterm = yearTerm.split("\\.");
						//yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
						term = ARRYyterm[1];
						
						Criterion [] cri = new Criterion [4];
						cri[0]           = Restrictions.eq("isDeleted",0);
						cri[1]           = Restrictions.eq("yearInfo",yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])));
						cri[2]           = Restrictions.eq("term",term);
						cri[3]           = Restrictions.eq("mainProcessType","stuChange");
						
						List<TeachingActivityTimeSetting> list = teachingActivityTimeSettingService.findByCriteria(TeachingActivityTimeSetting.class, cri, Order.desc("startTime"));
						Date curDate     = ExDateUtils.getCurrentDateTime();

						if (null!=list && !list.isEmpty()&&
							curDate.getTime()>=list.get(0).getStartTime().getTime()&&
							curDate.getTime()<=list.get(0).getEndTime().getTime()
						) {
							changeTypeMethod(stuChangeInfo, request, stu);
							
							stuChangeInfo.setStudentInfo(stu);
											
							//stuChangeInfoService.save(stuChangeInfo);
							//Date date                   = ExDateUtils.getCurrentDateTime();
							stuChangeInfo.setApplicationMan(opMan);
							stuChangeInfo.setApplicationManId(opManId);
							//stuChangeInfo.setApplicationDate(date);
							stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_WAIT);
							stuChangeInfoService.saveOrUpdateStuChangeInfo(stuChangeInfo,stu);
							
							map.put("statusCode", 200);
							map.put("message", "保存成功！");
							map.put("navTabId", "RES_SCHOOLROLL_STU_CHANGE");
							map.put("reloadUrl", request.getContextPath() +"/edu3/register/stuchangeinfo/stuid.html?resourceid="+stuChangeInfo.getResourceid()+"&finished=Y");
						}else {
							map.put("statusCode",300);
							map.put("message", "当前学期未设置学籍异动时间范围，或者当前时间不在时间范围内："+((null!=list&&!list.isEmpty())?(ExDateUtils.formatDateStr(list.get(0).getStartTime(),ExDateUtils.PATTREN_DATE_TIME)+" 至 "+ExDateUtils.formatDateStr(list.get(0).getEndTime(),ExDateUtils.PATTREN_DATE_TIME)):""));
						}
					}
					
				}
			}
				
		}catch (Exception e) {
			logger.error("保存学籍异动申请出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", errorMsg+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
		//~~~~~~~~~2.转专业、转层次，需要异动的地方有：学生学籍信息、学生学籍关联的教学计划、学生学习计划（如果之前教学计划课程与现在教学计划课程相等，则替换）、学生成绩
	}
	
	/**
	 * (替学生异动)保存、不走流程
	 * 
	 * @param stuChangeInfo
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/self/save1.html")
	public void doSave1(StuChangeInfo stuChangeInfo, HttpServletRequest request, HttpServletResponse response) throws WebException{
		
		String resourceid = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));// 获取表单ID
		String stuNum = ExStringUtils.trimToEmpty(request.getParameter("stuNum"));// stuNum	
		String applicationDate = ExStringUtils.trimToEmpty(request.getParameter("applicationDate_1"));
		StudentInfo stu = studentInfoService.findUniqueByProperty("studyNo", stuNum);
		
		Map<String,Object> map = new HashMap<String, Object>();
		String errorMsg = "学籍异动申请出错:";
		String opMan 	= ExStringUtils.trimToEmpty(request.getParameter("opMan"));
		String opManId 	= ExStringUtils.trimToEmpty(request.getParameter("opManId"));
		String[] attchids 	= request.getParameterValues("attachId");
		try {
			//添加判断：只有在学和过期的学籍状态才能申请延期
			if(!"11".equals(stu.getStudentStatus())&&!"18".equals(stu.getStudentStatus())&&"100".equals(stuChangeInfo.getChangeType())){
				errorMsg = errorMsg+stu.getStudentName()+"不是在学或过期状态,不能申请延期的学籍异动。<br/>";
				throw new Exception("违反申请延期异动的条件.");
			}
			if(ExStringUtils.isNotEmpty(resourceid)) {//修改
				StuChangeInfo p_stuChangeInfo = stuChangeInfoService.get(resourceid);
				ExBeanUtils.copyProperties(p_stuChangeInfo, stuChangeInfo);
				changeTypeMethod(p_stuChangeInfo, request, stu);
				p_stuChangeInfo.setStudentInfo(stu);
				Date date = ExDateUtils.getCurrentDateTime(applicationDate);
				p_stuChangeInfo.setApplicationMan(opMan);
				p_stuChangeInfo.setApplicationManId(opManId);
				//p_stuChangeInfo.setApplicationDate(date);
				stuChangeInfoService.saveOrUpdateStuChangeInfo(p_stuChangeInfo,stu);				
				//stuChangeInfoService.update(p_stuChangeInfo);
				
			}else {
				
				/*Grade curGrade   = gradeService.getDefaultGrade();
				if(null == curGrade){
					map.put("statusCode",300);
					map.put("message", "没有设置默认年级！");
				}*/
				String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
				//String yearInfoId = "";
				String term = "";
				if (null==yearTerm) {
					map.put("statusCode",300);
					map.put("message", "没有设置当前学期！");
				}else{
					List<ExamResults> examList = examResultsService.findByCriteria(Restrictions.eq("studentInfo.resourceid", stu.getResourceid()),Restrictions.eq("isDeleted",0),Restrictions.ne("checkStatus","4"));
					if((examList==null||examList.size()>0) && !"13".equals(stuChangeInfo.getChangeType())){//学籍异动申请前（除退学外），判断成绩是否全部发布
						String message="<strong>申请失败:</strong><br>";
						message    += Tools.colorStr(stu.getStudentName(),"red")+"&nbsp&nbsp"+stu.getStudyNo()+"&nbsp&nbsp"+"存在以下课程的成绩未发布，请撤销成绩或将成绩发布之后才能为学生进行学籍异动申请：";
						for(ExamResults er :examList){
							message +=Tools.colorStr("<br/>"+er.getCourse().getCourseName()+"&nbsp&nbsp&nbsp&nbsp"+er.getExamInfo().getExamSub().getBatchName()+"&nbsp&nbsp&nbsp&nbsp"+JstlCustomFunction.dictionaryCode2Value("ExamResult", er.getExamInfo().getExamSub().getExamType()),"red");
						}
						map.put("statusCode",300);
						map.put("message", message);
					}else{
						String[] ARRYyterm = yearTerm.split("\\.");
						//yearInfoId = yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])).getResourceid();
						term = ARRYyterm[1];
						Criterion [] cri = new Criterion [4];
						cri[0]           = Restrictions.eq("isDeleted",0);
						cri[1]           = Restrictions.eq("yearInfo",yearInfoService.getByFirstYear(Long.parseLong(ARRYyterm[0])));
						cri[2]           = Restrictions.eq("term",term);
						cri[3]           = Restrictions.eq("mainProcessType","stuChange");
						
						List<TeachingActivityTimeSetting> list = teachingActivityTimeSettingService.findByCriteria(TeachingActivityTimeSetting.class, cri, Order.desc("startTime"));
						Date curDate     = ExDateUtils.getCurrentDateTime();

						if (null!=list && !list.isEmpty()&&
							curDate.getTime()>=list.get(0).getStartTime().getTime()&&
							curDate.getTime()<=list.get(0).getEndTime().getTime()
						) {
							changeTypeMethod(stuChangeInfo, request, stu);
							
							stuChangeInfo.setStudentInfo(stu);
											
							//stuChangeInfoService.save(stuChangeInfo);
							Date date  = ExDateUtils.getCurrentDateTime(applicationDate);
							stuChangeInfo.setApplicationMan(opMan);
							stuChangeInfo.setApplicationManId(opManId);
							stuChangeInfo.setApplicationDate(date);
							stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_WAIT);
							stuChangeInfoService.saveOrUpdateStuChangeInfo(stuChangeInfo,stu);
							
							map.put("statusCode", 200);
							map.put("message", "保存成功！");
							map.put("navTabId", "RES_SCHOOLROLL_STU_CHANGE");
							map.put("reloadUrl", request.getContextPath() +"/edu3/register/stuchangeinfo/self/edit.html?resourceid="+stuChangeInfo.getResourceid()+"&finished=Y");
							//发送消息
							User user = SpringSecurityHelper.getCurrentUser();
							String changeTypeCN =JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange",stuChangeInfo.getChangeType() );
							String content="学号："+stuChangeInfo.getStudentInfo().getStudyNo()+" 姓名："+stuChangeInfo.getStudentInfo().getStudentName()+" 申请了 <font color='red'>"+changeTypeCN+"</font> 学籍异动，请尽快处理！";
							MessageReceiver receiver = new MessageReceiver();
							receiver.setResourceid(null);							
							Message msg = new Message();
							msg.setMsgTitle("来自<font color='green'>"+user.getOrgUnit().getUnitName()+"</font>的学籍异动申请通知");
							msg.setContent(content.toString());
							msg.setMsgType("usermsg");
							msg.setSendTime(new Date());
							msg.setFormId(stuChangeInfo.getResourceid());
							msg.setFormType("STUDENTCHANGEAPPLY");
							msg.setSender(user);
							msg.setSenderName(user.getCnName());
							msg.setOrgUnit(user.getOrgUnit());
							if(null != attchids){
								msg.setHasAttachs("Y");
							}							
							receiver.setReceiveType("role");
							receiver.setRoleCodes("ROLE_BRS_XUEJI");
//							receiver.setOrgUnitCodes("");
							receiver.setMessage(msg);
							sysMsgService.saveMsgAndMsgReceiver(msg, receiver);
//							sysMsgService.sendMsg(opManId,opMan,user.getOrgUnit(),
//									"usermsg","<font color='green'>来自"+user.getOrgUnit().getUnitName()+"学籍异动申请</font>",msg.toString(),"ROLE_BRS_XUEJI","role",stuChangeInfo.getResourceid(),"STUDENTCHANGEAPPLY");
						}else {
							map.put("statusCode",300);
							map.put("message", "当前学期未设置学籍异动时间范围，或者当前时间不在时间范围内："+((null!=list&&!list.isEmpty())?(ExDateUtils.formatDateStr(list.get(0).getStartTime(),ExDateUtils.PATTREN_DATE_TIME)+" 至 "+ExDateUtils.formatDateStr(list.get(0).getEndTime(),ExDateUtils.PATTREN_DATE_TIME)):""));
						}
					}
				
				}
			}
			if(null != attchids){
				attachsService.updateAttachByFormId(attchids, stuChangeInfo.getResourceid(), "STUDENTCHANGEAPPLY", opManId,opMan);
			}	
		}catch (Exception e) {
			logger.error("保存学籍异动申请出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", errorMsg+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
		//~~~~~~~~~2.转专业、转层次，需要异动的地方有：学生学籍信息、学生学籍关联的教学计划、学生学习计划（如果之前教学计划课程与现在教学计划课程相等，则替换）、学生成绩
	}
	
	/**
	 * 保存（走流程）
	 * @param stuChangeInfo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/save.html")
	public void save(StuChangeInfo stuChangeInfo, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String resourceid = request.getParameter("resourceid");// 获取表单ID
		String stuNum = request.getParameter("stuNum");// stuNum		
		StudentInfo stu = studentInfoService.findUniqueByProperty("studyNo", stuNum);
		
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotEmpty(resourceid)) {//修改
				StuChangeInfo p_stuChangeInfo = stuChangeInfoService.get(resourceid);
				ExBeanUtils.copyProperties(p_stuChangeInfo, stuChangeInfo);
				changeTypeMethod(p_stuChangeInfo, request, stu);
				p_stuChangeInfo.setStudentInfo(stu);
				
				
				Workflow wf  = WfAgent.getWorkflow(request);
				if("1".equalsIgnoreCase(request.getParameter("CM"))) {	// 提交的标志
					WfAgent.doAction(request);
					
					long wfstate = wf.getEntryState(p_stuChangeInfo.getWf_id());

					//如果提交归档则状态变成审核通过
					if(WorkflowEntry.COMPLETED == (new Long(wfstate).intValue())){
						p_stuChangeInfo.setFinalAuditStatus(String.valueOf(Constants.BOOLEAN_TRUE));										
						
						//stuChangeInfoService.auditPassStudentChangeInfo(p_stuChangeInfo,stu);
						stuChangeInfoService.auditBatchStudentChangeInfo(new String[]{resourceid},Constants.BOOLEAN_YES);
					}else{
						stuChangeInfoService.saveOrUpdateStuChangeInfo(p_stuChangeInfo,stu);
					}
				}
				
			}else {
				
				stuChangeInfo.setWf_id(WfAgent.initialize(request));
				
				changeTypeMethod(stuChangeInfo, request, stu);
				
				stuChangeInfo.setStudentInfo(stu);			
				
				//~~~~~~~end:~~~~~~~~~//////////
				stuChangeInfoService.saveOrUpdateStuChangeInfo(stuChangeInfo,stu);
			}
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SCHOOLROLL_STU_CHANGE");
			map.put("reloadUrl", request.getContextPath() +"/edu3/register/stuchangeinfo/edit.html?resourceid="+stuChangeInfo.getResourceid());
		}catch (Exception e) {
			logger.error("学籍异动申请出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "学籍异动申请出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	//~~~~ 设置参数
	private void changeTypeMethod(StuChangeInfo stuChangeInfo,HttpServletRequest request, StudentInfo stu) throws Exception {
				
		String changeType = stuChangeInfo.getChangeType();
		String memo 						= ExStringUtils.trimToEmpty(request.getParameter("memo"));
		String reason 						= ExStringUtils.trimToEmpty(request.getParameter("reason"));
		String changeBrschoolid     		= "";//异动后教学点
		String changeClassicid				= "";//异动后层次
		String changeClassicName			= "";
		String changeTeachingType	 		= "";//异动后学习形式
		String changeMajorid 	            = "";//异动后专业
		String changeClassesid	    		= "";//异动后班级
		String changeTeachingGuidePlanid	= "";//异动后年级教学计划
		String endDate						= "";
		
		//保存异动前信息
		if(stu.getBranchSchool()!=null){
			stuChangeInfo.setChangeBeforeBrSchool(stu.getBranchSchool());
			stuChangeInfo.setChangeBeforeSchoolName(stu.getBranchSchool().getUnitName());
		}
		if(stu.getClassic()!=null){
			stuChangeInfo.setChangeBeforeClassicName(stu.getClassic().getShortName());
		}
		stuChangeInfo.setChangeBeforeLearingStyle(stu.getTeachingType());
		if(stu.getMajor()!=null){
			stuChangeInfo.setChangeBeforeMajorName(stu.getMajor().getMajorName());
		}
		if(stu.getClasses()!=null){
			stuChangeInfo.setChangeBeforeClass(stu.getClasses());
		}
		
			//异动前学籍状态
		stuChangeInfo.setChangeBeforeStudentStatus(stu.getStudentStatus());
			//异动前年级教学计划
		TeachingPlan tp = stu.getTeachingPlan();
		Grade g = stu.getGrade();
		List<TeachingGuidePlan> tgps = teachingGuidePlanService.findByHql(" from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted = 0 and grade.resourceid = ? and teachingPlan.resourceid = ? ",new Object[]{g.getResourceid(),tp.getResourceid()} );
		if(!(tgps!=null && 1==tgps.size())){
			throw new Exception("无法确定原年度教学计划");
		}else {
			stuChangeInfo.setChangeBeforeTeachingGuidePlan(tgps.get(0));
		}
		
		/**
		 * 学籍异动记录为的是记录当时学籍的整体情况，所以不妨将较为完整的前后信息都保存下来
		*/
		if("12".equals(changeType)){ //复学
			changeMajorid               = ExStringUtils.trimToEmpty(request.getParameter("backSchool_majorid"));                 
			changeBrschoolid     		= ExStringUtils.trimToEmpty(request.getParameter("backSchool_schoolCenterid"));
			changeTeachingType 			= ExStringUtils.trimToEmpty(request.getParameter("backSchool_teachingType"));
			changeTeachingGuidePlanid	= ExStringUtils.trimToEmpty(request.getParameter("backSchool_changeguidteachPlanId"));
			changeClassesid    		 	= ExStringUtils.trimToEmpty(request.getParameter("backSchool_classesid"));
			endDate				        = ExStringUtils.trimToEmpty(request.getParameter("backSchool_backSchoolDate"));
		}else if("23".equals(changeType)){ //更改专业
			//成教需要保存专业、班级、学习中心、学习形式、年度教学计划
			changeMajorid		 = request.getParameter("majorid");
			changeBrschoolid     = ExStringUtils.trimToEmpty(request.getParameter("changeMajor_brschoolid"));
			changeTeachingType	 = ExStringUtils.trimToEmpty(request.getParameter("changeMajor_TeachingTypeid"));
			changeClassesid		 = ExStringUtils.trimToEmpty(request.getParameter("changeMajor_classesid"));
			changeTeachingGuidePlanid = ExStringUtils.trimToEmpty(request.getParameter("changeMajor_changeguidteachPlanId"));
			
//**新版的需求之中 成教系统无更改层次的异动
//		}
//		if(changeType.equals("83")){ //更改层次
//以上是之前的做法，通过传入待异动的层次，根据已有的专业年级办学模式的条件，找到一个年度教学计划，但现在这种情况会出现多个，
//故改为在前台有用户选择一个教学计划，传给后台并记录下来。
//			String teachingGuidePlan = ExStringUtils.trimToEmpty(request.getParameter("teachingGuidePlan"));
//			if (ExStringUtils.isNotEmpty(teachingGuidePlan)){
//				TeachingGuidePlan changeAfterTeachingGuidePlan = teachingGuidePlanService.get(teachingGuidePlan);
//				stuChangeInfo.setChangeTeachingGuidePlan(changeAfterTeachingGuidePlan);
//			}
		}else if("82".equals(changeType)){ //更改学习中心
			/** 其实 更改学习中心不应只保存有关学习中心的内容，因为当前选择更改学习中心是一个综合的结果
			String schoolCenter = request.getParameter("schoolCenterid");
			if(ExStringUtils.isNotEmpty(schoolCenter)){
				OrgUnit unit = orgUnitService.get(schoolCenter);
				//stu.setBranchSchool(unit);
				stuChangeInfo.setChangeBrschool(unit);
			}
			*/
			//成教需要保存专业、班级、学习中心、学习形式、年度教学计划
			changeMajorid 			 = ExStringUtils.trimToEmpty(request.getParameter("changeSchool_majorName"));
//			changeTeachingType = stu.getTeachingType();
			changeTeachingType = ExStringUtils.trimToEmpty(request.getParameter("changeSchool_TeachingTypeid"));;
			changeBrschoolid     	 = ExStringUtils.trimToEmpty(request.getParameter("changeSchool_schoolCenterid"));
			changeTeachingGuidePlanid = ExStringUtils.trimToEmpty(request.getParameter("changeSchool_changeguidteachPlanId"));
			changeClassesid    		  = ExStringUtils.trimToEmpty(request.getParameter("changeSchool_classesid"));
		}else if("81".equals(changeType)){ //更改学习方式
			/** 其实 更改学习模式，一般只会带来班级的修改，因为只有班级才跟学习方式有所关联，所以修改的是班级和学习形式。
			 *  保存前后完整信息
			*/
			
			changeMajorid               = ExStringUtils.trimToEmpty(request.getParameter("changeTeachingType_majorName"));                 
			changeBrschoolid     		= ExStringUtils.trimToEmpty(request.getParameter("changeTeachingType_stuCenterid"));
			changeTeachingType 			= ExStringUtils.trimToEmpty(request.getParameter("changeTeachingType_teachingType"));
			changeTeachingGuidePlanid   = ExStringUtils.trimToEmpty(request.getParameter("changeTeachingType_changeguidteachPlanId"));
			changeClassesid    		 	= ExStringUtils.trimToEmpty(request.getParameter("changeTeachingType_classesid"));
		}else if("11".equals(changeType)){ //休学
			endDate		        = ExStringUtils.trimToEmpty(request.getParameter("pauseStudy_backSchoolDate"));
			stuChangeInfo.setChangeTeachingGuidePlan(tgps.get(0));
			stuChangeInfo.setChangeBrschool(stu.getBranchSchool());				
			stuChangeInfo.setChangeClass(stu.getClasses());	
			stuChangeInfo.setChangeTeachingType(stu.getTeachingType());
			
		}else if("13".equals(changeType)){ //退学
			reason 							= ExStringUtils.trimToEmpty(request.getParameter("stopStudy_reason"));
			String bankName					= ExStringUtils.trimToEmpty(request.getParameter("bankName"));
			String cardNo					= ExStringUtils.trimToEmpty(request.getParameter("cardNo"));
			String bankAddress				= ExStringUtils.trimToEmpty(request.getParameter("bankAddress"));
			
			stuChangeInfo.setChangeTeachingGuidePlan(tgps.get(0));
			//保存退费银行信息(所属银行，银行卡号，开户行名称)
			//stuChangeInfo.setChangeBeforeSchoolName(bankName);
			//stuChangeInfo.setChangeBeforeMajorName(bankAddress);
			//stuChangeInfo.setChangeBeforeClassicName(cardNo);
			stuChangeInfo.setBankName(bankName);
			stuChangeInfo.setBankAddress(bankAddress);
			stuChangeInfo.setCardNo(cardNo);
			
			stuChangeInfo.setChangeBrschool(stu.getBranchSchool());				
			stuChangeInfo.setChangeClass(stu.getClasses());	
			stuChangeInfo.setChangeTeachingType(stu.getTeachingType());
			
			String change_reason = request.getParameter("change_reason");
			if(ExStringUtils.isNotEmpty(change_reason)){
				reason = change_reason;
			}
		}else if("98".equals(changeType)){ //多重异动
			changeMajorid = request.getParameter("majorid");
			changeClassicid = request.getParameter("classicid");
			changeBrschoolid = request.getParameter("schoolCenterid");
			changeTeachingType = request.getParameter("change_to_style");
		}else if("slowGraduation".equals(changeType)){ //缓毕业
			stuChangeInfo.setChangeBrschool(stu.getBranchSchool());				
			stuChangeInfo.setChangeClass(stu.getClasses());	
			stuChangeInfo.setChangeTeachingType(stu.getTeachingType());
			stuChangeInfo.setChangeTeachingGuidePlan(tgps.get(0));
		}
		//异动后信息
		if(ExStringUtils.isNotEmpty(changeBrschoolid)){
			OrgUnit changeUnit = orgUnitService.get(changeBrschoolid);
			stuChangeInfo.setChangeBrschool(changeUnit);				
		}
		if(ExStringUtils.isNotEmpty(changeClassicid)){
			Classic changeClassic = classicService.get(changeClassicid);
			stuChangeInfo.setChangeClassicId(changeClassic);
		}
		if(ExStringUtils.isNotEmpty(changeTeachingType)){
			stuChangeInfo.setChangeTeachingType(changeTeachingType);
		}
		if(ExStringUtils.isNotEmpty(changeMajorid)){
			Major changeMajor = majorService.get(changeMajorid);
			stuChangeInfo.setChangeMajor(changeMajor);
		}
		if(ExStringUtils.isNotEmpty(changeClassesid)){
			Classes changeClass = classesService.get(changeClassesid);
			stuChangeInfo.setChangeClass(changeClass);
			stuChangeInfo.setChangeClassicId(changeClass.getClassic());
		}
		if(ExStringUtils.isNotEmpty(changeTeachingGuidePlanid)){
			TeachingGuidePlan changeTeachingGuidePlan = teachingGuidePlanService.get(changeTeachingGuidePlanid);
			stuChangeInfo.setChangeTeachingGuidePlan(changeTeachingGuidePlan);
			stuChangeInfo.setChangeClassicId(changeTeachingGuidePlan.getTeachingPlan().getClassic());
		}
		if(ExStringUtils.isNotEmpty(endDate)){
			stuChangeInfo.setEndDate((ExDateUtils.convertToDate(endDate)));
		}
		if(ExStringUtils.isNotEmpty(memo)){
			stuChangeInfo.setMemo(memo);
		}
		if(ExStringUtils.isNotEmpty(reason)){
			stuChangeInfo.setReason(reason);
		}
	}

	/**
	 * 根据教学类型 年级 学习中心 层次 专业信息获取年度教学计划
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/getguidteachplan.html")
	public void getTeachPlanBeforeStudy(HttpServletRequest request, HttpServletResponse response){
		
		String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String gradeId   = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String brSchoolId= ExStringUtils.trimToEmpty(request.getParameter("brSchoolId"));
		String classicId = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String majorId   = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		Map<String, Object> map = new HashMap<String, Object>();
		String studentresourceid = ExStringUtils.trimToEmpty(request.getParameter("studentresourceid"));
		if(ExStringUtils.isNotEmpty(studentresourceid)){
			StudentInfo stu = studentInfoService.get(studentresourceid);
			if(ExStringUtils.isEmpty(teachType)){
				teachType = stu.getTeachingType();
			}
			if(ExStringUtils.isEmpty(gradeId)){
				gradeId   = stu.getGrade().getResourceid();
			}
			if(ExStringUtils.isEmpty(brSchoolId)){
				brSchoolId= stu.getBranchSchool().getResourceid();
			}
			if(ExStringUtils.isEmpty(classicId)){
				classicId = stu.getClassic().getResourceid();
			}
			if(ExStringUtils.isEmpty(majorId)){
				majorId   = stu.getMajor().getResourceid();
			}
		}
		if (ExStringUtils.isNotBlank(teachType)) {
			map.put("teachType", teachType);
		}
		
		List<Map<String,Object>> teachPlans = new ArrayList<Map<String,Object>>();
		map.put("teachPlanList", teachPlans);
		if(ExStringUtils.isNotEmpty(teachType)&&ExStringUtils.isNotBlank(brSchoolId)&&ExStringUtils.isNotBlank(gradeId)){
			StringBuffer teachPlanSql_ = new StringBuffer();
			teachPlanSql_.append(" select distinct b.resourceid,case when a.brschoolid is null then m.majorname||'-'||c.classicname||'('||a.versionnum||')' else u.unitshortname||g.gradename||m.majorname||c.classicname end  teachplanname ");
			teachPlanSql_.append(" from edu_teach_plan a inner join  edu_teach_guiplan b  on a.resourceid = b.planid "); 
			teachPlanSql_.append(" left join edu_base_grade g on b.gradeid = g.resourceid ");
			teachPlanSql_.append(" left join edu_base_major m on a.majorid = m.resourceid ");
			teachPlanSql_.append(" left join edu_base_classic c on a.classicid = c.resourceid "); 
			teachPlanSql_.append(" left join hnjk_sys_unit u on u.resourceid = a.brschoolid ");
			teachPlanSql_.append(" where  a.isdeleted = 0 and b.isdeleted =0 and b.ispublished = 'Y' ");	
    		teachPlanSql_.append(" and( (a.brschoolid is null ) or (a.brschoolid is not null and a.brschoolid = :brSchoolId)) ");
			teachPlanSql_.append(" and a.classicid  = :classicId ");
			teachPlanSql_.append(" and b.gradeid    = :gradeId ");
			teachPlanSql_.append(" and a.schooltype = :teachType ");
			teachPlanSql_.append(" and a.majorid    = :majorId ");
			try {
				if (ExStringUtils.isNotBlank(classicId)) {
					map.put("classicId", classicId);
				}
				if (ExStringUtils.isNotBlank(brSchoolId)) {
					map.put("brSchoolId", brSchoolId);
				}
				if (ExStringUtils.isNotBlank(gradeId)) {
					map.put("gradeId", gradeId);
				}
				if (ExStringUtils.isNotBlank(majorId)) {
					map.put("majorId", majorId);
				}
				teachPlans                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(teachPlanSql_.toString(),map);
			} catch (Exception e) {
				logger.debug("获取教学计划异常:"+e.fillInStackTrace());
			}
			
			
			if(null != teachPlans && !teachPlans.isEmpty()){
				map.put("teachPlanList", teachPlans);
			}else{
				map.put("error", (teachPlans.size()==0?"没有可用教学计划(未创建教学计划或教学计划未发布)。":""));
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 根据学籍信息中的年级 层次 专业 教学方式，获取可转学习中心的学习中心范围_转学习中心
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/framework/register/stuchangeinfo/getSchool.html")
	public void getSchool(HttpServletRequest request, HttpServletResponse response){
		
		String studentInfoid = ExStringUtils.trimToEmpty(request.getParameter("studentresourceid"));
		Map<String, Object> map = new HashMap<String, Object>();
		List<Map<String,Object>> schools = new ArrayList<Map<String,Object>>();
		String teachType 	= ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String gradeId   	= ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String classicId 	= ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		String changeType 	= ExStringUtils.trimToEmpty(request.getParameter("changeType"));
		String major 		= ExStringUtils.trimToEmpty(request.getParameter("major"));
		String schoolid 	= ExStringUtils.trimToEmpty(request.getParameter("schoolid"));
		if(ExStringUtils.isNotEmpty(studentInfoid)){
			StudentInfo studentinfo = studentInfoService.get(studentInfoid);
			if(ExStringUtils.isEmpty(teachType)) {
				teachType = studentinfo.getTeachingType();
			}
			if(ExStringUtils.isEmpty(gradeId)) {
				gradeId   = studentinfo.getGrade().getResourceid();
			}
			if(ExStringUtils.isEmpty(classicId)) {
				classicId = studentinfo.getClassic().getResourceid();
			}
			if(ExStringUtils.isEmpty(schoolid)) {
				schoolid  = studentinfo.getBranchSchool().getResourceid();
			}
		}
		if(ExStringUtils.isNotBlank(gradeId)){
			StringBuffer schoolSql_ = new StringBuffer();
			schoolSql_.append(" select distinct u.unitcode, u.resourceid , u.unitname ");
			schoolSql_.append(" from edu_teach_plan a inner join  edu_teach_guiplan b  on a.resourceid = b.planid "); 
			schoolSql_.append(" left join edu_base_grade g on b.gradeid = g.resourceid ");
			schoolSql_.append(" left join edu_base_major m on a.majorid = m.resourceid ");
			schoolSql_.append(" left join edu_base_classic c on a.classicid = c.resourceid "); 
			schoolSql_.append(" left join hnjk_sys_unit u on (u.resourceid = a.brschoolid or a.brschoolid is null and u.schooltype like '%"+teachType+"%' )");//广东医成教的教学计划很多是没有关联学校的
			schoolSql_.append(" where  a.isdeleted = 0 and b.isdeleted =0 and u.isdeleted=0");	
			schoolSql_.append(" and a.classicid  = :classicId ");
			schoolSql_.append(" and b.gradeid    = :gradeId ");
			schoolSql_.append(" and a.schooltype = :teachType ");
			if("82".equals(changeType)){
				schoolSql_.append(" and m.resourceid = :major ");
				if(ExStringUtils.isNotBlank(schoolid)){
					map.put("schoolid", schoolid);
					schoolSql_.append(" and u.resourceid <> :schoolid ");
				}		   
			}
			schoolSql_.append(" and (u.resourceid is not null and a.brschoolid is not null or a.brschoolid is null ) order by u.unitcode asc");
			try {
				if (ExStringUtils.isNotBlank(teachType)) {
					map.put("teachType", teachType);
				}
				if (ExStringUtils.isNotBlank(classicId)) {
					map.put("classicId", classicId);
				}
				if (ExStringUtils.isNotBlank(gradeId)) {
					map.put("gradeId", gradeId);
				}
				if (ExStringUtils.isNotBlank(major)) {
					map.put("major", major);
				}
				schools                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(schoolSql_.toString(),map);
			} catch (Exception e) {
				logger.debug("获取学习中心异常:"+e.fillInStackTrace());
			}
			if(null != schools && !schools.isEmpty()){
				map.clear();
				map.put("schoolList", schools);
			}else{
				map.put("error", (schools.size()==0?"没有可选的学习中心":""));
			}
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * Excel导出学籍异动统计表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/excel/export.html")
	public void doExcelExportStudentChange(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		//从库中查出数据		
		//姓名  转码传递的中文参数
		String stuName =ExStringUtils.getEncodeURIComponentByTwice(ExStringUtils.trimToEmpty(request.getParameter("stuName")));
		
		
		//教学站
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		//专业
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));
		//层次
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		//学籍状态
		String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));

		
		String accountStatus =null;		
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		//变动类别
		String stuChange = ExStringUtils.trimToEmpty(request.getParameter("stuChange"));
		//年级
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		//联系电话
		String mobile = ExStringUtils.trimToEmpty(request.getParameter("mobile"));
		//学习方式
		String learningStyle = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		//学号
		String stuNum = ExStringUtils.trimToEmpty(request.getParameter("stuNum"));
		//开始申请时间
		String applicationDateb = ExStringUtils.trimToEmpty(request.getParameter("applicationDateb"));
		//申请时间结束
		String applicationDatee = ExStringUtils.trimToEmpty(request.getParameter("applicationDatee")); 
		//开始审核时间
		String auditDateb		= ExStringUtils.trimToEmpty(request.getParameter("auditDateb"));
		//审核结束时间
		String auditDatee		= ExStringUtils.trimToEmpty(request.getParameter("auditDatee"));
		//审核结果 W为未审核 N为不通过  Y为审核通过
		String finalAuditStatus		= ExStringUtils.trimToEmpty(request.getParameter("finalAuditStatus"));
		
		//导出的表头
		String branchSchoolText ="";
		User curUser          = SpringSecurityHelper.getCurrentUser();
		if(curUser.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = curUser.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			branchSchoolText = orgUnitService.get(branchSchool).getUnitName() ;
		}
	/*	String majorText = ExStringUtils.trimToEmpty(request.getParameter("majorText"));
		String classicText = ExStringUtils.trimToEmpty(request.getParameter("classicText"));
		String stuStatusText = ExStringUtils.trimToEmpty(request.getParameter("stuStatusText"));
		String stuChangeText = ExStringUtils.trimToEmpty(request.getParameter("stuChangeText"));
		String gradeidText = ExStringUtils.trimToEmpty(request.getParameter("gradeidText"));
		String learningStyleText = ExStringUtils.trimToEmpty(request.getParameter("learningStyleText"));*/
		StringBuffer title = new StringBuffer();
		
		Map<String,Object> condition = new HashMap<String, Object>(0);
		if(!"".equals(stuName)) {
			condition.put("stuName",stuName );
		}
		if(!"".equals(branchSchool)) {
			condition.put("branchSchool",branchSchool );
		}
		if(!"".equals(major)) {
			condition.put("major", major);
		}
		if(!"".equals(classic)) {
			condition.put("classic", classic);
		}
		if(!"".equals(stuStatus)) {
			condition.put("stuStatus",stuStatus );
		}
		if(!"".equals(stuChange)) {
			condition.put("stuChange",stuChange );
		}
		if(!"".equals(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(!"".equals(learningStyle)) {
			condition.put("learningStyle",learningStyle );
		}
		if(!"".equals(stuNum)) {
			condition.put("stuNum", stuNum);
		}
		if(!"".equals(finalAuditStatus)) {
			condition.put("finalAuditStatus", finalAuditStatus);
		}
		if(!"".equals(mobile)) {
			condition.put("mobile", mobile);
		}
		
		if(ExStringUtils.isNotBlank(applicationDateb)) {
			condition.put("applicationDateb", applicationDateb);
		}
		if(ExStringUtils.isNotBlank(applicationDatee)) {
			condition.put("applicationDatee", applicationDatee);
		}
		if(ExStringUtils.isNotBlank(auditDateb)) {
			condition.put("auditDateb", auditDateb);
		}
		if(ExStringUtils.isNotBlank(auditDatee)) {
			condition.put("auditDatee", auditDatee);
		}
		
		if(null!=accountStatus){
			condition.put("accountStatus", accountStatus);
		}
		
		List<StuChangeInfo> list = stuChangeInfoService.findByCondition(condition);
//		List<Map<String,Object>> list = stuChangeInfoService.findStuChangeInfoMapList(condition);
		List<Map<String,Object>> newlist = new ArrayList<Map<String,Object>>(0);
		
		if(0<list.size()){
			for (StuChangeInfo m : list) {
			 
				TeachingGuidePlan plan = new TeachingGuidePlan(); 
				if(m.getChangeBeforeTeachingGuidePlan()!=null){
					plan = m.getChangeBeforeTeachingGuidePlan();
				}
				Map<String,Object> newMap = new HashMap<String,Object>();
				
				newMap.put("studyNo", m.getStuNum());
				newMap.put("name", m.getStuName());
				newMap.put("examCertificateNo", m.getStudentInfo().getExamCertificateNo());
				newMap.put("enrolleeCode", m.getStudentInfo().getEnrolleeCode());
				newMap.put("changeType",JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange", m.getChangeType()==null?"": m.getChangeType().toString()));
				newMap.put("changeBeforeBrSchool", m.getChangeBeforeBrSchool()!=null?m.getChangeBeforeBrSchool().getUnitName():"");
				newMap.put("changeBeforeClass", m.getChangeBeforeClass()!= null?m.getChangeBeforeClass().getClassname():"");
				newMap.put("changeBeforeLearingStyle", m.getChangeBeforeLearingStyle()==null?"":JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", m.getChangeBeforeLearingStyle().toString()));
				if(m.getStudentInfo()!=null){
					newMap.put("gradeName", m.getStudentInfo().getGrade().getGradeName());				
					newMap.put("changeBrschool", m.getStudentInfo().getBranchSchool()!=null?m.getStudentInfo().getBranchSchool().getUnitShortName():"");
					newMap.put("changeMajor",m.getStudentInfo().getMajor()==null?"":m.getStudentInfo().getMajor().getMajorName());
					newMap.put("changeClass",m.getStudentInfo().getClasses()==null?"":m.getStudentInfo().getClasses().getClassname());
					newMap.put("changeClassicId", m.getStudentInfo().getClassic()==null?"":m.getStudentInfo().getClassic().getClassicName());
					newMap.put("mobile",m.getStudentInfo().getStudentBaseInfo().getMobile());
					newMap.put("changeLearningStyle", m.getStudentInfo().getTeachingType()==null?"":JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",m.getStudentInfo().getTeachingType()));
					if("11".equals(m.getChangeType()) || "13".equals(m.getChangeType()) || "18".equals(m.getChangeType())){
						newMap.put("changeBeforeMajorName", m.getStudentInfo().getMajor()==null?"":m.getStudentInfo().getMajor().getMajorName());
						newMap.put("changeBeforeClassicName", m.getStudentInfo().getClassic()==null?"":m.getStudentInfo().getClassic().getShortName());
					}else {
						newMap.put("changeBeforeMajorName", m.getChangeBeforeMajorName()!=null?m.getChangeBeforeMajorName():plan==null?"":plan.getTeachingPlan().getMajor().getMajorName());
						newMap.put("changeBeforeClassicName", m.getChangeBeforeClassicName()!=null?m.getChangeBeforeClassicName():plan==null?"":plan.getTeachingPlan().getClassic().getShortName());
					}
				}else {
					newMap.put("gradeName","");
					newMap.put("changeBrschool","");
					newMap.put("changeMajor","");
					newMap.put("changeClass","");
					newMap.put("changeClassicId","");
					newMap.put("mobile","");
					newMap.put("changeLearningStyle","");
					newMap.put("changeBeforeMajorName", "");
					newMap.put("changeBeforeClassicName", "");
				}
				
				try {
					newMap.put("applicationDate", m.getApplicationDate()==null?"":ExDateUtils.formatDateStr((Date)m.getApplicationDate(), "yyyy-MM-dd HH:mm:ss"));
					newMap.put("auditDate", m.getAuditDate()==null?"":ExDateUtils.formatDateStr((Date)m.getAuditDate(), "yyyy-MM-dd HH:mm:ss"));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				newMap.put("finalAuditStatus", m.getFinalAuditStatus()==null?"": "Y".equals(m.getFinalAuditStatus()) ?"审核通过": "N".equals(m.getFinalAuditStatus()) ?"审核不通过":"待审核");
				newMap.put("reason", m.getReason()==null?"":m.getReason());
				newlist.add(newMap);
			}
		}
		
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");//存放路径
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			if("default".equals(exportAct)){//默认导出	
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeStudentStatusChange");
				dictCodeList.add("CodeNation");
				Map<String , Object> mapdict = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				//自定义的审核状态的映射
				mapdict.put("CodeAudit_Y", "审核通过");
				mapdict.put("CodeAudit_N", "未审核");
				mapdict.put("CodeAudit_1", "审核通过");
				mapdict.put("CodeAudit_0", "未审核");
				//stuName = new String(stuName.getBytes("ISO8859-1"),"UTF-8");
				/*branchSchoolText = new String(branchSchoolText.getBytes("ISO8859-1"),"UTF-8");
				majorText = new String(majorText.getBytes("ISO8859-1"),"UTF-8");
				classicText = new String(classicText.getBytes("ISO8859-1"),"UTF-8");
				stuStatusText = new String(stuStatusText.getBytes("ISO8859-1"),"UTF-8");
				stuChangeText = new String(stuChangeText.getBytes("ISO8859-1"),"UTF-8");
				gradeidText = new String(gradeidText.getBytes("ISO8859-1"),"UTF-8");
				learningStyleText = new String(learningStyleText.getBytes("ISO8859-1"),"UTF-8");*/
//				if(!"".equals(stuName)) title.append(stuName+" ");
//				if(!"".equals(branchSchoolText)) title.append(branchSchoolText+" ");
//				if(!"请选择".equals(majorText)) title.append(majorText+" ");
//				if(!"请选择".equals(classicText)) title.append(classicText+" ");
//				if(!"请选择".equals(stuStatusText)) title.append(stuStatusText+" ");
//				if(!"请选择".equals(stuChangeText)) title.append(stuChangeText+" ");
//				if(!"请选择".equals(gradeidText)) title.append(gradeidText+" ");
//				if(!"请选择".equals(learningStyleText)) title.append(learningStyleText+" ");
				
				exportExcelService.initParmasByfile(disFile, "stuInfoChangeV1", newlist,mapdict);
				exportExcelService.getModelToExcel().setHeader(title.toString()+"学籍异动明细表");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高 
		
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			}
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "学籍异动明细表.xls", excelFile.getAbsolutePath(),true);
			
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	@RequestMapping("/edu3/register/stuchangeinfo/excel/exportStatCondition.html")
	public String stuChangeStatCondition(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-statcondition";
	}
	
	/**
	 * Excel导出学籍异动次数统计表
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/excel/stuChangeStatExport.html")
	public void doExcelExportStudentChangeNumStat(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		//从库中查出数据		
		String statClass = ExStringUtils.trimToEmpty(request.getParameter("statClass"));
		String auditDate = "";
		if(!"".equals(request.getParameter("auditDate"))){
			auditDate=ExStringUtils.trimToEmpty(request.getParameter("auditDate")).substring(0, 10);
		}
		String statClassName = "分类方式";
		Map<String,Object> condition = new HashMap<String, Object>(0);
		if(!"".equals(auditDate)) {
			condition.put("auditDate",auditDate );
		}
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>(0);
		switch (Integer.valueOf(statClass)){
			case 0:result = studentInfoChangeJDBCService.statStudentChangeNumByLearnStyle(condition); statClassName="学习方式" 	;break;
			case 1:result = studentInfoChangeJDBCService.statStudentChangeNumByMajor(condition);	  statClassName="专业"	  	;break;
			case 2:result = studentInfoChangeJDBCService.statStudentChangeNumByGrade(condition);	  statClassName="年级"		;break;
			case 3:result = studentInfoChangeJDBCService.statStudentChangeNumByBrSchool(condition);	  statClassName="学习中心"	;break;
			case 4:result = studentInfoChangeJDBCService.statStudentChangeNumByClassic(condition);	  statClassName="层次"		;break;
			case 5:result = studentInfoChangeJDBCService.statStudentChangeNumByStyNo(condition);	  statClassName="学号"		;break;
			case 6:
				String students = ExStringUtils.trimToEmpty(request.getParameter("students"));
				condition.put("stus",students);
				result = studentInfoChangeJDBCService.statStudentChangeNumByName(condition); 	 
				statClassName="姓名"		;break;
			case 7:result = studentInfoChangeJDBCService.statStudentChangeNumByChangeStyle(condition);statClassName="异动类型"	;break;
			default: break;
		}
		List<StuChangeInfo> list = new ArrayList<StuChangeInfo>(0);
		if(0<result.size()){
			for (Map<String, Object> tmp : result) {
				StuChangeInfo stuChangeInfo = new StuChangeInfo();
				Set<String> set = tmp.keySet();
				for (String string : set) {
					String str = null==tmp.get(string)?"":tmp.get(string).toString();
					if(statClassName.equals(string)) {
						stuChangeInfo.setReason(str);
					} else if("异动条数合计".equals(string)) {
						stuChangeInfo.setMemo(str) ;
					}
				}
				list.add(stuChangeInfo);
			}
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			if("default".equals(exportAct)){//默认导出	
				switch (Integer.valueOf(statClass)) {
				case 0:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByLearnStyle", list,null);break;
				case 1:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByMajor", list,null);break;
				case 2:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByGrade", list,null);break;
				case 3:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByBrSchool", list,null);break;
				case 4:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByClassic", list,null);break;
				case 5:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByStyNo", list,null);break;
				case 6:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByName", list,null);break;
				case 7:exportExcelService.initParmasByfile(disFile, "stuInfoChangeStatByChangeStyle", list,null);break;
				default:
					break;
				}
				exportExcelService.getModelToExcel().setHeader(statClassName+"学籍异动条数统计表"+" "+auditDate);//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			}
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response,statClassName+"学籍异动条数统计表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
	
	
	/**
	 * 打印异动信息预览
	 * @param request
	 * @param model
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/excel/printStuChangeInfo.html")
	public String printStuChangeInfo(HttpServletRequest request,ModelMap model){
		String branchSchool        		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//教学站
		String major      		     = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classes      = ExStringUtils.trimToEmpty(request.getParameter("stuchangeinfo_list_class"));//班级
		String gradeid      		     = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));//年级
		String stuStatus      		 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学习状态
		String classic               = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String learningStyle           = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));//学习方式
		String stuNum 		 			= ExStringUtils.trimToEmpty(request.getParameter("stuNum"));//学号
		String stuName     			 = ExStringUtils.trimToEmpty(request.getParameter("stuName"));//姓名
		String stuChange    		 = ExStringUtils.trimToEmpty(request.getParameter("stuChange"));//变动类型
		String changeProperty              = ExStringUtils.trimToEmpty(request.getParameter("changeProperty"));  //变动属性  
		String finalAuditStatus                  = ExStringUtils.trimToEmpty(request.getParameter("finalAuditStatus"));//审核结果
		String applicationDateb              = ExStringUtils.trimToEmpty(request.getParameter("applicationDateb"));//申请开始时间
		String applicationDatee                  = ExStringUtils.trimToEmpty(request.getParameter("applicationDatee"));//申请结束时间
		String auditDateb                  = ExStringUtils.trimToEmpty(request.getParameter("auditDateb"));//审核开始时间
		String auditDatee                  = ExStringUtils.trimToEmpty(request.getParameter("auditDatee"));//审核开始时间		
		String flag                  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String acquiesceStudentid                  = ExStringUtils.trimToEmpty(request.getParameter("acquiesceStudentid"));
		String accountStatus                  = ExStringUtils.trimToEmpty(request.getParameter("accountStatus"));
		try {
			stuName = URLEncoder.encode(stuName,"UTF-8");
		} catch (UnsupportedEncodingException e) {
//			logger.error();
		}	
		model.addAttribute("branchSchool",branchSchool);
		model.addAttribute("major",major);
		model.addAttribute("classes",classes);
		model.addAttribute("gradeid",gradeid);
		model.addAttribute("stuStatus",stuStatus);
		model.addAttribute("classic",classic);
		model.addAttribute("learningStyle",learningStyle);
		model.addAttribute("stuNum",stuNum);
		model.addAttribute("stuName",stuName);
		model.addAttribute("stuChange",stuChange);
		model.addAttribute("changeProperty",changeProperty);
		model.addAttribute("finalAuditStatus",finalAuditStatus);
		model.addAttribute("applicationDateb",applicationDateb);		
		model.addAttribute("applicationDatee",applicationDatee);
		model.addAttribute("auditDateb",auditDateb);
		model.addAttribute("auditDatee",auditDatee);
		model.addAttribute("flag",flag);
		model.addAttribute("acquiesceStudentid",acquiesceStudentid);
		model.addAttribute("accountStatus",accountStatus);
		return "/edu3/roll/stuchangeinfo/stuChangeInfo-printview";
	}
	
	
	/**
	 * 打印
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/excel/printStuChangeInfoList.html")
	public void printStatCourseOrderDetails(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();		
		String branchSchool        		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//教学站
		String major      		     = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classes      = ExStringUtils.trimToEmpty(request.getParameter("stuchangeinfo_list_class"));//班级
		String gradeid      		     = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));//年级
		String stuStatus      		 = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学习状态
		String classic               = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String learningStyle           = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));//学习方式
		String stuNum 		 			= ExStringUtils.trimToEmpty(request.getParameter("stuNum"));//学号
		String stuName     			 = ExStringUtils.trimToEmpty(request.getParameter("stuName"));//姓名
		String stuChange    		 = ExStringUtils.trimToEmpty(request.getParameter("stuChange"));//变动类型
		String changeProperty              = ExStringUtils.trimToEmpty(request.getParameter("changeProperty"));  //变动属性  
		String finalAuditStatus                  = ExStringUtils.trimToEmpty(request.getParameter("finalAuditStatus"));//审核结果
		String applicationDateb              = ExStringUtils.trimToEmpty(request.getParameter("applicationDateb"));//申请开始时间
		String applicationDatee                  = ExStringUtils.trimToEmpty(request.getParameter("applicationDatee"));//申请结束时间
		String auditDateb                  = ExStringUtils.trimToEmpty(request.getParameter("auditDateb"));//审核开始时间
		String auditDatee                  = ExStringUtils.trimToEmpty(request.getParameter("auditDatee"));//审核开始时间
		String flag                  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		try {
			if (ExStringUtils.isNotBlank(stuName)){
				stuName = URLDecoder.decode(stuName,"utf-8");
			} 
		} catch (Exception e) {
		}		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			 branchSchool  			 = user.getOrgUnit().getResourceid();
		}		
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classes)) {
			condition.put("classes", classes);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}
		if (ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}
		if (ExStringUtils.isNotEmpty(stuNum)) {
			condition.put("stuNum", stuNum);
		}
		if (ExStringUtils.isNotEmpty(stuName)) {
			condition.put("stuName", stuName);
		}
		if (ExStringUtils.isNotEmpty(stuChange)) {
			condition.put("stuChange", stuChange);
		}
		if (ExStringUtils.isNotEmpty(changeProperty)) {
			condition.put("changeProperty", changeProperty);
		}
		if (ExStringUtils.isNotEmpty(finalAuditStatus)) {
			condition.put("finalAuditStatus", finalAuditStatus);
		}
		if (ExStringUtils.isNotEmpty(applicationDateb)) {
			condition.put("applicationDateb", applicationDateb);
		}
		if (ExStringUtils.isNotEmpty(applicationDatee)) {
			condition.put("applicationDatee", applicationDatee);
		}
		if (ExStringUtils.isNotEmpty(auditDateb)) {
			condition.put("auditDateb", auditDateb);
		}
		if (ExStringUtils.isNotEmpty(auditDatee)) {
			condition.put("auditDatee", auditDatee);
		}
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}

		List<StuChangeInfo> returnList = stuChangeInfoService.findStuChangeInfoByCondition(condition);
		
		Map<String,Object> map = new HashMap<String, Object>();
		
		List<Map<String,Object>> dataList = new ArrayList<Map<String,Object>>(0); 

		int i=1;
		for(StuChangeInfo sci :returnList){
			Map<String,Object> m = new HashMap<String,Object>();
			m.put("sortNum", String.valueOf(i++));//序号
			m.put("stuNum", sci.getStuNum());//学号
			m.put("stuName", sci.getStuName());//姓名
			
			if(sci.getStudentInfo().getGrade()!=null){
				m.put("grade", sci.getStudentInfo().getGrade());//年级
			}else{
				m.put("grade", "-");//年级
			}
			if(sci.getStudentInfo().getStudentBaseInfo().getMobile()!=null){
				m.put("mobile", sci.getStudentInfo().getStudentBaseInfo().getMobile());//手机号码
			}else{
				m.put("mobile", "-");//手机号码
			}
			
			//m.put("changeType", sci.getChangeType());//异动类型
			if(sci.getChangeType()!=null){
				m.put("changeType", JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange", sci.getChangeType()));//异动类型
			}else{
				m.put("changeType", "-");//异动类型
			}
			
			if(sci.getStudentInfo().getBranchSchool()!=null){
				m.put("unit", sci.getStudentInfo().getBranchSchool().getUnitShortName());//原单位
			}
			if(sci.getStudentInfo().getMajor()!=null){
				m.put("major", sci.getStudentInfo().getMajor());//专业
			}else{
				m.put("major", "-");//专业
			}
			
			if(sci.getStudentInfo().getClasses()!=null){
				m.put("classes", sci.getStudentInfo().getClasses().getClassname());//班级
			}else{
				m.put("classes", "-");//班级
			}
			if(sci.getStudentInfo().getClassic()!=null){
				m.put("classic", sci.getStudentInfo().getClassic().getShortName());//层次
			}else{
				m.put("classic", "-");//层次
			}
			
			//m.put("teachingType", sci.getStudentInfo().getTeachingType());//现形式
			if( sci.getStudentInfo().getTeachingType()!=null){
				m.put("teachingType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", sci.getStudentInfo().getTeachingType()));//现形式
			}else{
				m.put("teachingType", "-");//现形式
			}
			
			if(sci.getChangeBeforeBrSchool()!=null){
				m.put("ounit", sci.getChangeBeforeBrSchool().getUnitShortName());//原单位
			}else{
				m.put("ounit", "-");//原单位
			}
			if(sci.getChangeBrschool()!=null){
				m.put("wunit", sci.getChangeBrschool().getUnitShortName());//拟转入学院
			}else{
				m.put("wunit", "-");//拟转入学院
			}
			if(sci.getChangeBeforeMajorName()!=null){
				m.put("omajor", sci.getChangeBeforeMajorName());//原专业 方式一
			}else if(sci.getChangeBeforeTeachingGuidePlan()!=null) {
				m.put("omajor", sci.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getMajor().getMajorName());//原专业方式2
			}else{
				m.put("omajor", "-");//原专业
			}
			if(sci.getChangeBeforeClass()!=null){
				m.put("oclasses", sci.getChangeBeforeClass());//原班级  方式1
			}else{
				m.put("oclasses", "-");
			}
			if(sci.getChangeBeforeClassicName()!=null){
				m.put("oclassic", sci.getChangeBeforeClassicName());//原层次
				//m.put("oclassic", JstlCustomFunction.dictionaryCode2Value("CodeEducationalLevel", sci.getChangeBeforeClassicName()));//原层次
			}else if(sci.getChangeBeforeTeachingGuidePlan()!=null) {
				m.put("oclassic", sci.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getClassic().getShortName());//原层次  方式2
				//m.put("oclassic", JstlCustomFunction.dictionaryCode2Value("CodeEducationalLevel", sci.getChangeBeforeTeachingGuidePlan().getTeachingPlan().getClassic().getShortName()));//原层次
			}else{
				m.put("oclassic", "-");//原专业 方式一
			}
			
			if(sci.getChangeBeforeLearingStyle()!=null){
				//m.put("oteachType", sci.getChangeBeforeLearingStyle());//原形式
				m.put("oteachType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", sci.getChangeBeforeLearingStyle()));//原形式
			}else{
				m.put("oteachType", "-");//原形式
			}
			if(sci.getApplicationDate()!=null){
				String auditDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(sci.getApplicationDate());
				m.put("applyDate", auditDate);//申请时间
			}else{
				m.put("applyDate", "-");//申请时间
			}
			if(sci.getAuditDate()!=null){
				String auditDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(sci.getAuditDate());
				m.put("auditDate", auditDate);//审核时间	
			}else{
				m.put("auditDate","-");//审核时间	
			}		
			m.put("memo","");//备注
			if(sci.getFinalAuditStatus()!=null &&  "Y".equals(sci.getFinalAuditStatus())){
				m.put("finalAuditStatus","通过");//审核结果
			}else if(sci.getFinalAuditStatus()!=null &&  "N".equals(sci.getFinalAuditStatus())){
				m.put("finalAuditStatus","不通过");//审核结果
			}else{
				m.put("finalAuditStatus","待审核");//审核结果
			}
			
			dataList.add(m);
		}
		try {
			JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
			String jasperFile  = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
				File.separator+"stuChangeInfo"+File.separator+"stuChangeInfoByStuIds.jasper"),"utf-8");
			//map.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
//			map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo2.path").getParamValue());
			//map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
//			map.put("printTime",ExDateUtils.formatDateStr(new Date(),"yyyy年MM月dd日"));		
			map.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
			JasperPrint jasperPrint               = JasperFillManager.fillReport(jasperFile, map, dataSource); // 填充报表
			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"没有打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印出错：{}"+e.fillInStackTrace());
			renderHtml(response,"<script>alert('打印出错："+e.getMessage()+"')</script>");
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 按姓名统计时查找学生
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/stuchange-chooseStus.html")
	public String findStu(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String statType = request.getParameter("statTypeRec");
		String auditDate = request.getParameter("auditDateRec");
		String sName 	= request.getParameter("sName");
		String brSchool = request.getParameter("brSchool");
		Map<String ,Object> condition = new HashMap<String, Object>(0);
		Page stuPage = new Page();
		if("".equals(sName)&&"".equals(brSchool)){
			//数据太多脚本过大需要改为page的形式分页显示 map是查询过程中的条件
			Map<String, Object> map = new HashMap<String, Object>(0);
			if(!"".equals(auditDate)){
				try {
					map.put("id3",ExDateUtils.parseDate(auditDate, ExDateUtils.PATTREN_DATE));
					stuPage=stuChangeInfoService.findByHql(stuPage, " from "+StuChangeInfo.class.getName()+" where studentInfo.branchSchool.unitName is not null and studentInfo.studentBaseInfo.name is not null and auditDate>:id3",map);
				} catch (ParseException e) {
					e.printStackTrace();
				}		
			}else{
				stuPage=stuChangeInfoService.findByHql(stuPage, " from "+StuChangeInfo.class.getName()+" where studentInfo.branchSchool.unitName is not null and studentInfo.studentBaseInfo.name is not null",null);
			}
		}else if("".equals(sName)&&!"".equals(brSchool)){
			Map<String, Object> map = new HashMap<String, Object>(0);
			if(!"".equals(auditDate)){
				try {
					map.put("id3",ExDateUtils.parseDate(auditDate, ExDateUtils.PATTREN_DATE));
					map.put("id", brSchool);
					stuPage=stuChangeInfoService.findByHql(stuPage," from "+StuChangeInfo.class.getName()+" where  studentInfo.branchSchool.resourceid=:id and auditDate>:id3", map);
				} catch (ParseException e) {
					e.printStackTrace();
				}					
			}else{
				map.put("id", brSchool);
				stuPage=stuChangeInfoService.findByHql(stuPage," from "+StuChangeInfo.class.getName()+" where  studentInfo.branchSchool.resourceid=:id ", map);
			}
		}else if(!"".equals(sName)&&"".equals(brSchool)){
			Map<String, Object> map = new HashMap<String, Object>(0);
			if(!"".equals(auditDate)){
				try {
					map.put("id3",ExDateUtils.parseDate(auditDate, ExDateUtils.PATTREN_DATE));
					map.put("id", sName);
					stuPage=stuChangeInfoService.findByHql(stuPage," from "+StuChangeInfo.class.getName()+" where  studentInfo.studentBaseInfo.name=:id and auditDate>:id3", map);
				} catch (ParseException e) {
					e.printStackTrace();
				}	
			}else{
				map.put("id", sName);
				stuPage=stuChangeInfoService.findByHql(stuPage," from "+StuChangeInfo.class.getName()+" where  studentInfo.studentBaseInfo.name=:id ",map);
			}
		}else{
			Map<String, Object> map = new HashMap<String, Object>(0);
			if(!"".equals(auditDate)){
				try {
					map.put("id3",ExDateUtils.parseDate(auditDate, ExDateUtils.PATTREN_DATE));
					map.put("id2", sName);
					map.put("id1", brSchool);
					stuPage=stuChangeInfoService.findByHql(stuPage," from "+StuChangeInfo.class.getName()+" where studentInfo.branchSchool.resourceid=:id1 and studentInfo.studentBaseInfo.name=:id2 and auditDate>:id3", map);
				} catch (ParseException e) {
					e.printStackTrace();
				}	
			}else{
				map.put("id2", sName);
				map.put("id1", brSchool);
				stuPage=stuChangeInfoService.findByHql(stuPage, " from "+StuChangeInfo.class.getName()+" where studentInfo.branchSchool.resourceid=:id1 and studentInfo.studentBaseInfo.name=:id2 ",map);
			}
		}
		condition.put("isSubmitted", "1");
		condition.put("statType", statType);
		condition.put("auditDate", auditDate);
		condition.put("sName", sName);
		condition.put("brSchool", brSchool);
		model.addAttribute("stus",stuPage);
		model.addAttribute("condition",condition);
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-statcondition";
	}
	/**
	 * 撤销学籍异动（删除未审核通过的学籍异动）
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/delete.html")
	public void exeDelete(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		StringBuffer message = new StringBuffer();
		StringBuffer messageS = new StringBuffer();
		StringBuffer messageF = new StringBuffer();
		Map<String ,Object> map = new HashMap<String, Object>();
		User curUser          = SpringSecurityHelper.getCurrentUser();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
								
				String[] resourceidSingle = (resourceid.split("\\,"));
				for (String id : resourceidSingle) {
					StuChangeInfo tmp = stuChangeInfoService.get(id);
					/*
					 * 条件1：异动类型（82-更改教学点）
					 * 条件2：当前用户不是学生教学中心的，并且也不是一下角色（ROLE_ROLL_XUEJI2-教学管理中心-学籍管理，ROLE_ROLL_XUEJI-教学管理中心-学籍主任）
					 */
					if ((!Constants.BOOLEAN_YES.equals(tmp.getFinalAuditStatus()))&&"82".equals(tmp.getChangeType())&&
						(!curUser.getOrgUnit().getResourceid().equals(tmp.getStudentInfo().getBranchSchool().getResourceid())
								&&!curUser.isContainRole("ROLE_ROLL_XUEJI2")&&!curUser.isContainRole("ROLE_ROLL_XUEJI"))) {
						messageF.append("撤销转教点的异动申请，需由发起异动的教点用户执行。<font color='red'>"+tmp.getStudentInfo().getStudentName()+"</font>的异动申请中心为："+tmp.getStudentInfo().getBranchSchool().getUnitName()+"</br>");
						continue;
					}
					if(Constants.BOOLEAN_YES.equals(tmp.getFinalAuditStatus())){
						//if(tmp.getChangeType().equals(""))
						getMessage(tmp, messageS,messageF, false);
						continue;
					}
					stuChangeInfoService.delete(id);
					getMessage(tmp, messageS,messageF, true);
				}

				map.put("statusCode", 200);
				message.append("撤销学籍异动操作完成。");
				//map.put("message", message);
				map.put("messageF", messageF);
				map.put("messageS", messageS);
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 删除误退学的学生   20160412
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/deleteDropout.html")
	public void exeDeleteDropout(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String resourceid = request.getParameter("resourceid");
		StringBuffer message = new StringBuffer();
		StringBuffer messageS = new StringBuffer();
		StringBuffer messageF = new StringBuffer();
		Map<String ,Object> map = new HashMap<String, Object>();
		User curUser          = SpringSecurityHelper.getCurrentUser();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
								
				String[] resourceidSingle = (resourceid.split("\\,"));
				for (String id : resourceidSingle) {
					StuChangeInfo tmp = stuChangeInfoService.get(id);
					/*
					 * 条件1：异动类型--退学   13  
					 * 条件2：审核通过
					 */
					if(Constants.BOOLEAN_YES.equals(tmp.getFinalAuditStatus()) &&
							!"13".equals(tmp.getChangeType()) && !"18".equals(tmp.getChangeType())){
						messageF.append("只有退学的学生才能恢复学籍:"+tmp.getStudentInfo().getStudentName());
						//getMessage(tmp, messageS,messageF, false);
						continue;
					}
					stuChangeInfoService.DeleteDropout(id);
					
					getMessage(tmp, messageS,messageF, true);
				}

				map.put("statusCode", 200);
				message.append("撤销学籍异动操作完成。");
				//map.put("message", message);
				map.put("messageF", messageF);
				map.put("messageS", messageS);
			}
		} catch (Exception e) {
			logger.error("删除栏目出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 生成信息
	 * @param tmp
	 * @param messageS
	 * @param messageF
	 * @param isSuccessed
	 */
	private void getMessage(StuChangeInfo tmp,StringBuffer messageS,StringBuffer messageF,boolean isSuccessed){
		String changeType = JstlCustomFunction.dictionaryCode2Value("CodeStudentStatusChange", tmp.getChangeType());
		if(!isSuccessed){
			messageF.append("【");
			messageF.append(tmp.getStudentInfo().getStudentName());
			messageF.append("】");
			messageF.append(tmp.getApplicationDate());
			messageF.append("提交的【");
			messageF.append(changeType);
			messageF.append("】的异动申请已通过审核，不可删除。<br>");
		}else{
			messageS.append("【");
			messageS.append(tmp.getStudentInfo().getStudentName());
			messageS.append("】");
			messageS.append(tmp.getApplicationDate());
			messageS.append("提交的【");
			messageS.append(changeType);
			messageS.append("】的异动申请成功删除。<br>");
		}
	}
	
	/**
	 * Ajax 查询
	 * @param stuNum
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/schoolroll/awardspun/querystuinfo.html")
	public void exeQuery(String stuNum, HttpServletRequest request, HttpServletResponse response) throws Exception{
		if(ExStringUtils.isNotEmpty(stuNum)) {
			Map<String,String> map = new HashMap<String, String>();
			try{
				StudentInfo info = studentInfoService.findUniqueByProperty("studyNo", stuNum);
				if(null == info){
					throw new WebException("没有对应的学籍信息！");
				}
				map.put("studentid",info.getResourceid());
				User currentUser = SpringSecurityHelper.getCurrentUser();
				if(null!=currentUser.getOrgUnit() && currentUser.getOrgUnit().getUnitType().				
						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0
						&&!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(currentUser.getUserType())
						)//条件描述：用户的组织对象不为NULL，&& 用户组织类型为校外学习中心&& 当前用户的用户类型不是学生
				{
				
					if(!info.getBranchSchool().getResourceid().equals(currentUser.getOrgUnit().getResourceid())){
						throw new WebException("该学生不属于你的学习中心，请确认学号是否正确无误。");
					}
					
				}
				map.put("studyNo", info.getStudyNo());
				map.put("studentName", info.getStudentName());
				List<StuChangeInfo> stuc = stuChangeInfoService.findByHql(" from "+StuChangeInfo.class.getSimpleName() +" stuc where stuc.isDeleted = 0 and stuc.studentInfo = ?", info);
				if(stuc.size()>0){
					for(StuChangeInfo c:stuc){
						if(c.getFinalAuditStatus().equals(Constants.BOOLEAN_WAIT)){
							map.put("isLastApply", "N");
							break;
						}
					}					
				}
				//新添加的 majorId 和 trainplan.SchoolType
				map.put("majorId", info.getMajor()!=null?info.getMajor().getResourceid():"");				
				map.put("schoolType", info.getTeachingPlan()!=null?info.getTeachingPlan().getSchoolType():"");//教学计划所属的办学模式
				map.put("classic", info.getClassic()!=null?info.getClassic().getClassicName():"");
				map.put("classicId", info.getClassic()!=null?info.getClassic().getResourceid():"");
				map.put("grade", info.getGrade()!=null?info.getGrade().getGradeName():"");
				map.put("gradeId", info.getGrade()!=null?info.getGrade().getResourceid():"");//年级ID
				map.put("teachingPlan", info.getTeachingPlan()!=null?info.getTeachingPlan().getResourceid():"");//所修教学计划
				map.put("brSchool", info.getBranchSchool()!=null?info.getBranchSchool().getResourceid():"");//所在学习中心
				map.put("studentstatus", info.getStudentStatus());//学籍状态
				if(null!=info.getClasses()){
					map.put("classes", info.getClasses().getClassname());//班级名称
					map.put("classesId", info.getClasses().getResourceid());//班级名称id
				}
				map.put("teachingtype", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",info.getTeachingType()));
				map.put("teachingtypeid", info.getTeachingType());
				if(null == info.getBranchSchool()){
					throw new WebException("学生："+info.getStudentName()+" 没有关联学习中心！");
				}
				map.put("schoolCenter", info.getBranchSchool()!=null?info.getBranchSchool().getUnitName():"");
				if(null == info.getMajor()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的专业！");
				}
				map.put("major", info.getMajor()!=null?info.getMajor().getMajorName():"");
				if(null == info.getClassic()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的层次！");
				}
				if(null == info.getTeachingPlan()) {
					throw new WebException("学生："+info.getTeachingPlan().getSchoolType()+" 没有关联的学习形式！");
				}

				if(null == info.getGrade()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的年级！");
				}
				
				map.put("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
				//因为转教学中心的需要 如果这个点同年级 ，学习形式 ，层次，专业下的教学点才会出现在教学站下拉列表里
				
				
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("isdeleted", 0);
				param.put("grade", info.getGrade()!=null?info.getGrade().getResourceid():"");
				param.put("classic", info.getClassic().getResourceid());
				param.put("type", info.getTeachingType());
				param.put("major", info.getMajor().getResourceid());
				
			}catch (Exception e) {
				map.put("error", e.getMessage());
			}
			
			
			renderText(response, JsonUtils.mapToJson(map));
		}
	}
	
	/**
	 *	替学生学籍异动查询
	 * @param stuNum
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/schoolroll/awardspun/querystuinfo1.html")
	public String exeQuery1(String stuNum, HttpServletRequest request, HttpServletResponse response,ModelMap model) throws Exception{
		if(ExStringUtils.isNotEmpty(stuNum)) {
			Map<String,String> map = new HashMap<String, String>();
			try{
				StudentInfo info = studentInfoService.findUniqueByProperty("studyNo", stuNum);
				if(null == info){
					throw new WebException("没有对应的学籍信息！");
				}
				map.put("studentid",info.getResourceid());
				User currentUser = SpringSecurityHelper.getCurrentUser();
				if(null!=currentUser.getOrgUnit() && currentUser.getOrgUnit().getUnitType().				
						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0
						&&!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(currentUser.getUserType())
						)//条件描述：用户的组织对象不为NULL，&& 用户组织类型为校外学习中心&& 当前用户的用户类型不是学生
				{
				
					if(!info.getBranchSchool().getResourceid().equals(currentUser.getOrgUnit().getResourceid())){
						throw new WebException("该学生不属于你的学习中心，请确认学号是否正确无误。");
					}
					
				}
				map.put("studyNo", info.getStudyNo());
				map.put("studentName", info.getStudentName());
				
				//新添加的 majorId 和 trainplan.SchoolType
				map.put("majorId", info.getMajor()!=null?info.getMajor().getResourceid():"");				
				map.put("schoolType", info.getTeachingPlan()!=null?info.getTeachingPlan().getSchoolType():"");//教学计划所属的办学模式
				map.put("classic", info.getClassic()!=null?info.getClassic().getClassicName():"");
				map.put("classicId", info.getClassic()!=null?info.getClassic().getResourceid():"");
				map.put("grade", info.getGrade()!=null?info.getGrade().getGradeName():"");
				map.put("gradeId", info.getGrade()!=null?info.getGrade().getResourceid():"");//年级ID
				map.put("teachingPlan", info.getTeachingPlan()!=null?info.getTeachingPlan().getResourceid():"");//所修教学计划
				map.put("brSchool", info.getBranchSchool()!=null?info.getBranchSchool().getResourceid():"");//所在学习中心
				map.put("studentstatus", info.getStudentStatus());//学籍状态
				if(null!=info.getClasses()){
					map.put("classes", info.getClasses().getClassname());//班级名称
					map.put("classesId", info.getClasses().getResourceid());//班级名称id
				}
				map.put("teachingtype", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",info.getTeachingType()));
				map.put("teachingtypeid", info.getTeachingType());
				if(null == info.getBranchSchool()){
					throw new WebException("学生："+info.getStudentName()+" 没有关联学习中心！");
				}
				map.put("schoolCenter", info.getBranchSchool()!=null?info.getBranchSchool().getUnitName():"");
				if(null == info.getMajor()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的专业！");
				}
				map.put("major", info.getMajor()!=null?info.getMajor().getMajorName():"");
				if(null == info.getClassic()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的层次！");
				}
				if(null == info.getTeachingPlan()) {
					throw new WebException("学生："+info.getTeachingPlan().getSchoolType()+" 没有关联的学习形式！");
				}

				if(null == info.getGrade()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的年级！");
				}
				
				//因为转教学中心的需要 如果这个点同年级 ，学习形式 ，层次，专业下的教学点才会出现在教学站下拉列表里
				
				
				Map<String,Object> param = new HashMap<String, Object>();
				param.put("isdeleted", 0);
				param.put("grade", info.getGrade()!=null?info.getGrade().getResourceid():"");
				param.put("classic", info.getClassic().getResourceid());
				param.put("type", info.getTeachingType());
				param.put("major", info.getMajor().getResourceid());
				
			}catch (Exception e) {
				map.put("error", e.getMessage());
			}
			
			
			
		}
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-changeIntoSchool";
	}
	
	
	
	/**
	 * 打开审批学籍异动的页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/audit.html")
	public String setStudentChangeInfoAuditStatus(HttpServletRequest request,HttpServletResponse response ,ModelMap model)throws WebException{
		String studentId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		
		if(ExStringUtils.isNotEmpty(studentId)){
			model.addAttribute("studentId", studentId);
		}
		return "/edu3/roll/stuchangeinfo/studentchangeinfo-auditstatussetting";
	}
	
	
	/**
	 * 校级转入异动
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/changeIntoSchool.html")
	public String changeIntoSchool(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid 			  = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));					// 获取表单ID
		String finished               = ExStringUtils.trimToEmpty(request.getParameter("finished"));
		
		User currentUser = SpringSecurityHelper.getCurrentUser();
		
		StudentInfo studentInfo = null;
		boolean isManager=false;//是否是教务员登录
		//获取当前学生的默认学籍ID
		if("student".equalsIgnoreCase(currentUser.getUserType())){
			String defaultRollId = currentUser.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
			studentInfo = studentInfoService.findUnique("from "+StudentInfo.class.getSimpleName()+" where isDeleted = ? and resourceid = ?", 0,defaultRollId);
		}else{
			isManager=true;
		}
				
		StuChangeInfo stuChangeInfo = null;
		
		if(ExStringUtils.isNotEmpty(resourceid)){//修改
			stuChangeInfo = stuChangeInfoService.get(resourceid);
		}else{//新增
			stuChangeInfo = new StuChangeInfo();
			stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_YES);
			if(null != studentInfo) {
				stuChangeInfo.setStudentInfo(studentInfo);
			}
		}		
		model.addAttribute("isManager",isManager);
		model.addAttribute("stuChangeInfo", stuChangeInfo);
		model.addAttribute("opMan",currentUser.getCnName());
		model.addAttribute("opManId",currentUser.getResourceid());
		model.addAttribute("finished",finished);
		
		/*List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		
		for (Major m : majors) {
				majorOption.append("<
 value ='"+m.getResourceid()+"'>"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
		}
		model.addAttribute("majorOption", majorOption);*/
		
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-changeIntoSchool";
	}
	
	/**
	 * 保存校级转入
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/changeIntoSchool/save.html")
	public void changeIntoSchoolDoSave(HttpServletRequest request, HttpServletResponse response) throws WebException{
				Map<String,Object> map = new HashMap<String, Object>();
		String errorMsg = "校级转入申请出错:";
		String opMan 	= ExStringUtils.trimToEmpty(request.getParameter("opMan"));
		String opManId 	= ExStringUtils.trimToEmpty(request.getParameter("opManId"));
		
		String  changeIntoSchool_gradeName = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_gradeName"));
		String  changeIntoSchool_stuName = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_stuName"));
		String  changeIntoSchool_stuCenterid = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_stuCenterid"));
		String  changeIntoSchool_changeBeForeSchoolName = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_changeBeForeSchoolName"));
		String  changeIntoSchool_classicId = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_classicId"));
		String  changeIntoSchool_changeBeforeClassic = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_changeBeforeClassic"));
		String  changeIntoSchool_teachingType = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_teachingType"));
		String  changeIntoSchool_changeBeforeLearingStyle = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_changeBeforeLearingStyle"));
		String  changeIntoSchool_majorId = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_majorId"));
//		String  changeIntoSchool_changeBeforeMajorId = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_changeBeforeMajorId"));
		String  changeIntoSchool_changeClass = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_changeClass"));
		String  changeIntoSchool_teachingPlan = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_teachingPlan"));
		
		String changeIntoSchool_reason = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_reason"));
		String changeIntoSchool_memo   = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_memo"));
		
		String  changeIntoSchool_changeBeforeMajorName = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_changeBeforeMajorName"));
		String  studentNum = ExStringUtils.trimToEmpty(request.getParameter("inputStuNo"));
		String  isReferenceStudyNoStr = ExStringUtils.trimToEmpty(request.getParameter("createStuNo"));
		//2014-4-14
//		String exameeInfo_KSH = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_KSH"));//考生号
//		String exameeInfo_ZKZH = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_ZKZH"));//准考证号
//		String exameeInfo_XM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_XM"));//姓名
//		String exameeInfo_WYYZDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_WYYZDM"));//外语语种
//		String exameeInfo_CSRQ = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_CSRQ"));//出生日期
//		String exameeInfo_XBDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_XBDM"));//性别
//		String exameeInfo_SFZH = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_SFZH"));//身份证号
//		String exameeInfo_KSTZBZ = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_KSTZBZ"));//考生特征标志
//		String exameeInfo_ZSLBDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_ZSLBDM"));//招生类别
//		String exameeInfo_MZDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_MZDM"));//民族
//		String exameeInfo_ZZMMDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_ZZMMDM"));//政治面貌
//		String exameeInfo_BKZYSXDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_BKZYSXDM"));//报考专业属性
//		String exameeInfo_YZBM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_YZBM"));//邮政编码
//		String exameeInfo_LXDH = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_LXDH"));//联系电话
//		String exameeInfo_WHCDDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_WHCDDM"));//文化程度
//		String exameeInfo_ZYLBDM = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_ZYLBDM"));//专业类别
//		String exameeInfo_GZRQ = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_GZRQ"));//工作年月
//		String exameeInfo_BYRQ = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_BYRQ"));//毕业日期
//		String exameeInfo_BYXX = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_BYXX"));//毕业学校
//		String exameeInfo_TXDZ = ExStringUtils.trimToEmpty(request.getParameter("exameeInfo_TXDZ"));//录取通知书邮寄地址
//		//成<br/>绩<br/>信<br/>息
//		String[] exameeInfo_scoreCodes = request.getParameterValues("exameeInfo_scoreCodes");
//		String[] exameeInfo_scoreValues = request.getParameterValues("exameeInfo_scoreValues");
//		
//		//志<br/>愿<br/>信<br/>息
//		String[] exameeInfo_ZYBM = request.getParameterValues("exameeInfo_ZYBM");// 编号：
//		String[] exameeInfo_ZYMCD = request.getParameterValues("exameeInfo_ZYMC");//  专业名：
		// 保存考生信息 
//		ExameeInfo exameeInfo = new ExameeInfo();
//		exameeInfo.setKSH(exameeInfo_KSH);//考生号
//		exameeInfo.setZKZH(exameeInfo_ZKZH);//准考证号
//		exameeInfo.setXM(exameeInfo_XM);//姓名
//		exameeInfo.setWYYZDM(exameeInfo_WYYZDM);//外语语种
//		exameeInfo.setCSRQ(ExDateUtils.convertToDate(exameeInfo_CSRQ));//出生日期
//		exameeInfo.setXBDM("男".equals(exameeInfo_XBDM)?"1":"2");//性别
//		exameeInfo.setSFZH(exameeInfo_SFZH);//身份证号
//		exameeInfo.setKSTZBZ(exameeInfo_KSTZBZ);//考生特征标志
//		exameeInfo.setZSLBDM(exameeInfo_ZSLBDM);//招生类别
//		exameeInfo.setMZDM(exameeInfo_MZDM);//民族
//		exameeInfo.setBKZYSXDM(exameeInfo_ZZMMDM);//政治面貌
//		exameeInfo.setZZMMDM(exameeInfo_ZZMMDM);//报考专业属性
//		exameeInfo.setYZBM(exameeInfo_YZBM);//邮政编码
//		exameeInfo.setLXDH(exameeInfo_LXDH);//联系电话
//		exameeInfo.setWHCDDM(exameeInfo_WHCDDM);//专业类别
//		exameeInfo.setZYLBDM(exameeInfo_ZYLBDM);//专业类别
//		exameeInfo.setGZRQ(exameeInfo_GZRQ);//工作年月
//		exameeInfo.setBYRQ(exameeInfo_BYRQ);//毕业日期
//		exameeInfo.setBYXX(exameeInfo_BYXX);//毕业学校
//		exameeInfo.setTXDZ(exameeInfo_TXDZ);//录取通知书邮寄地址
		
//		exameeInfoService.saveOrUpdate(exameeInfo);
		
//		Set<ExameeInfoScore> scores= new HashSet<ExameeInfoScore>(0);
//		for(int i=0;i<exameeInfo_scoreCodes.length;i++){
//			if(ExStringUtils.trimToEmpty(exameeInfo_scoreCodes[i])!=""){
//				ExameeInfoScore score = new ExameeInfoScore();
////				score.setExameeInfo(exameeInfo);
//				score.setScoreCode(exameeInfo_scoreCodes[i]);
//				if(ExStringUtils.trimToEmpty(exameeInfo_scoreValues[i])!=""){
//					score.setScoreValue(Double.parseDouble(exameeInfo_scoreValues[i]));
//				}else{
//					score.setScoreValue(0.0);
//				}
//				scores.add(score);
//			}
//		}
//		exameeInfo.setExameeInfoScores(scores);
		
//		Set<ExameeInfoWish> wishs = new HashSet<ExameeInfoWish>(0);
//		if(null != exameeInfo_ZYBM && exameeInfo_ZYBM.length >0){
//			for(int i=0;i<exameeInfo_ZYBM.length;i++){
//				if(ExStringUtils.trimToEmpty(exameeInfo_ZYBM[i]) !=""){
//					ExameeInfoWish wish = new ExameeInfoWish();
//					wish.setZYBM(ExStringUtils.trimToEmpty(exameeInfo_ZYBM[i]));
////					if(){
////						
////					}
//					
//					wishs.add(wish);
//				}
//			}
//		}
		
//		exameeInfoService.saveOrUpdate(exameeInfo);
		
//		currentTerm = JstlCustomFunction.getSysConfigurationValue("defaultgrade", "server");
		
		
		try {
			// 新增内容
			String  changeIntoSchool_totalPoint = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_totalPoint"));
			Long totalPoint = null;// 入学总分
			if(ExStringUtils.isNotEmpty(changeIntoSchool_totalPoint)) {
				totalPoint = Long.valueOf(changeIntoSchool_totalPoint);
			}
			
			String  changeIntoSchool_inDate = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_inDate"));
			String  changeIntoSchool_enrolleeCode = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_enrolleeCode"));
			String  changeIntoSchool_examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("changeIntoSchool_examCertificateNo"));

			
			/*Grade curGrade   = gradeService.getDefaultGrade();*/
			String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();
			String yearInfo = "";
			String term = "";
			if (null!=yearTerm) {
				
				String[] ARRYyterm = yearTerm.split("\\.");
				yearInfo = ARRYyterm[0];
				term = ARRYyterm[1];
			}
			Criterion [] cri = new Criterion [4];
			cri[0]           = Restrictions.eq("isDeleted",0);
			cri[1]           = Restrictions.eq("yearInfo",yearInfoService.getByFirstYear(Long.parseLong(yearInfo)));
			cri[2]           = Restrictions.eq("term",term);
			cri[3]           = Restrictions.eq("mainProcessType","stuChange");
			String studyNo = studentNum;
			List<TeachingActivityTimeSetting> list = teachingActivityTimeSettingService.findByCriteria(TeachingActivityTimeSetting.class, cri, Order.desc("startTime"));
			Date curDate     = ExDateUtils.getCurrentDateTime();
			if (null!=list && !list.isEmpty()&&curDate.getTime()>=list.get(0).getStartTime().getTime()&&curDate.getTime()<=list.get(0).getEndTime().getTime()) {
				
				StuChangeInfo stuChangeInfo = new StuChangeInfo();	
				StudentInfo stu 			= new StudentInfo();
				StudentBaseInfo sb 			= new StudentBaseInfo();
				Grade grade = gradeService.get(changeIntoSchool_gradeName);
				OrgUnit unit = orgUnitService.get(changeIntoSchool_stuCenterid);
				Classic classic = classicService.get(changeIntoSchool_classicId);
				Classic classic_before = classicService.get(changeIntoSchool_changeBeforeClassic);
				Major major = majorService.get(changeIntoSchool_majorId);
//				Major major_before = majorService.get(changeIntoSchool_changeBeforeMajorId);
				Classes c = classesService.get(changeIntoSchool_changeClass);
				TeachingGuidePlan tgp = teachingGuidePlanService.get(changeIntoSchool_teachingPlan);
				
				//2014-4-15
//				String studyNo = genStudentNo(classic,major,changeIntoSchool_teachingType,grade);
//				String studyNo = "";
				if("checked".equals(isReferenceStudyNoStr)){
					studyNo="";
					String isReferenceStudyNo = "Y";
					//是否沿用准学号作为学生学号
					isReferenceStudyNo = CacheAppManager.getSysConfigurationByCode("yesorno.reference.quasiStudyno").getParamValue();
					if ("N".equals(isReferenceStudyNo)) {
						String registeredStudynorule = CacheAppManager.getSysConfigurationByCode("studentinfo.registered.studynorule").getParamValue();
						if ("1".equals(registeredStudynorule)) {//教科
							studyNo = getStudentNo(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("2".equals(registeredStudynorule)) {//广东医
							studyNo = genStudentNoNew(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("3".equals(registeredStudynorule)) {//广大
							studyNo = genStudentNoNew2(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("4".equals(registeredStudynorule)) {//安徽医学院
							studyNo = genStudentNoAHYKD(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("5".equals(registeredStudynorule)) {//广西医科大
							studyNo = genStudentNoGXYKD(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						}
					} else {
						String studynorule = CacheAppManager.getSysConfigurationByCode("studentinfo.studynorule").getParamValue();
						if ("1".equals(studynorule)) {//教科
							studyNo = getStudentNo(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("2".equals(studynorule)) {//广东医
							studyNo = genStudentNoNew(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("3".equals(studynorule)) {//广大
							studyNo = genStudentNoNew2(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("4".equals(studynorule)) {//安徽医学院
							studyNo = genStudentNoAHYKD(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						} else if ("5".equals(studynorule)) {//广西医科大
							studyNo = genStudentNoGXYKD(unit.getUnitCode(),classic,major,changeIntoSchool_teachingType,grade);
						}
					}		
				}
			
				if(ExStringUtils.isEmpty(studyNo)){
					errorMsg += "<br/> 生成学号出错，学号为空！";
					throw new WebException("学号为空");
				}
				
				//2014-4-16 补全学生基本信息
//				sb.setName(changeIntoSchool_stuName);
//				sb.setGender("-");
//				sb.setCertType("-");
//				sb.setMarriage("-");
//				sb.setBornDay(ExDateUtils.convertToDate("1970-1-1"));
//				sb.setCertNum("ZS"+studyNo);
//				sb.setMobile("-");
//				sb.setPolitics("-");
				setParamsStuinfo(request,sb);
				
				Date inDate = null;// 入学日期
				if(ExStringUtils.isNotEmpty(changeIntoSchool_inDate)){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					inDate = sdf.parse(changeIntoSchool_inDate);
				} else {
					inDate = grade.getIndate();
				}
				
				studentService.save(sb);
				stu.setStudentBaseInfo(sb);
				stu.setStudentName(changeIntoSchool_stuName);
				stu.setGrade(grade);
				stu.setClassic(classic);
				stu.setClasses(c);
				stu.setMajor(major);
				stu.setTeachingPlan(tgp.getTeachingPlan());
				stu.setTeachingType(changeIntoSchool_teachingType);
				stu.setLearningStyle(changeIntoSchool_teachingType);
				stu.setBranchSchool(unit);
				stu.setStudentStatus("24");
				stu.setStudyNo(studyNo);
				stu.setTotalPoint(totalPoint);
				stu.setInDate(inDate);
				stu.setEnrolleeCode(changeIntoSchool_enrolleeCode);
				stu.setExamCertificateNo(changeIntoSchool_examCertificateNo);
				studentInfoService.save(stu);
				stuChangeInfo.setStudentInfo(stu);
				stuChangeInfo.setChangeBeforeSchoolName(changeIntoSchool_changeBeForeSchoolName);
				stuChangeInfo.setChangeBeforeLearingStyle(changeIntoSchool_changeBeforeLearingStyle);
				
				//2014-4-15
				/*if(null!=major_before){
					stuChangeInfo.setChangeBeforeMajorName(major_before.getMajorName());
				} else {
					// 没有必要新增一条转入前的专业
					Major major_b = new Major();
					major_b.setMajorName(changeIntoSchool_changeBeforeMajorName);
					major_b.setResourceid(changeIntoSchool_changeBeforeMajorId);
					majorService.saveOrUpdate(major_b);
					stuChangeInfo.setChangeBeforeMajorName(changeIntoSchool_changeBeforeMajorName);
				}*/
				stuChangeInfo.setChangeBeforeMajorName(changeIntoSchool_changeBeforeMajorName);
				
				if(null!=classic_before) {
					stuChangeInfo.setChangeBeforeClassicName(classic_before.getShortName()) ;
				}
				stuChangeInfo.setChangeBeforeSchoolName(changeIntoSchool_changeBeForeSchoolName) ;
				stuChangeInfo.setChangeTeachingGuidePlan(tgp);
				stuChangeInfo.setChangeType("C_24");
				stuChangeInfo.setReason(changeIntoSchool_reason);
				stuChangeInfo.setMemo(changeIntoSchool_memo);
				stuChangeInfo.setChangeBrschool(unit);
				stuChangeInfo.setChangeClass(c);
				stuChangeInfo.setChangeTeachingType(changeIntoSchool_teachingType);
				Date date                   = ExDateUtils.getCurrentDateTime();
				stuChangeInfo.setApplicationMan(opMan);
				stuChangeInfo.setApplicationManId(opManId);
				stuChangeInfo.setApplicationDate(date);
				stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_WAIT);
				stuChangeInfoService.saveOrUpdateStuChangeInfo(stuChangeInfo,stu);
				
				
				//保存考生信息
//				ExameeInfo exameeInfo = new ExameeInfo();
//				exameeInfo.setKSH(exameeInfo_KSH);//考生号
//				exameeInfo.setZKZH(exameeInfo_ZKZH);//准考证号
//				exameeInfo.setXM(exameeInfo_XM);//姓名 
//				exameeInfo.setWYYZDM("");//外语语种
//				exameeInfo.setCSRQ(ExDateUtils.convertToDate(exameeInfo_CSRQ));//出生日期
//				exameeInfo.setXBDM("男".equals(exameeInfo_XBDM)?"1":"2");//性别
//				exameeInfo.setSFZH(exameeInfo_SFZH);//身份证号
////				exameeInfo.setKSTZBZ(exameeInfo_KSTZBZ);//考生特征标志
//				exameeInfo.setZSLBDM("");//招生类别
//				exameeInfo.setMZDM(exameeInfo_MZDM);//民族
//				exameeInfo.setZZMMDM(exameeInfo_ZZMMDM);//政治面貌ZZMMDM
//				exameeInfo.setBKZYSXDM("");//报考专业属性BKZYSXDM
//				exameeInfo.setYZBM(exameeInfo_YZBM);//邮政编码YZBM
//				exameeInfo.setLXDH(exameeInfo_LXDH);//联系电话LXDH  
//				exameeInfo.setWHCDDM("");//文化程度WHCDDM
//				exameeInfo.setZYLBDM("");//专业类别ZYLBDM
////				exameeInfo.setGZRQ(exameeInfo_GZRQ);//工作年月
////				exameeInfo.setBYRQ(exameeInfo_BYRQ);//毕业日期
////				exameeInfo.setBYXX(exameeInfo_BYXX);//毕业学校
//				exameeInfo.setTXDZ("");//录取通知书邮寄地址TXDZ
//				exameeInfo.setLQZY("");//录取专业LQZY
//				exameeInfo.setCCDM(classic_before.getClassicCode());//层次
//				exameeInfo.setXXXSDM("");//XXXSDM学习形式
//				exameeInfoService.saveOrUpdate(exameeInfo);				
					
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_SCHOOLROLL_STU_CHANGE");
				map.put("reloadUrl", request.getContextPath() +"/edu3/register/stuchangeinfo/changeIntoSchool.html?resourceid="+stuChangeInfo.getResourceid()+"&finished=Y");
			}else {
				map.put("statusCode",300);
				map.put("message", "当前学期未设置学籍异动时间范围，或者当前时间不在时间范围内："+((null!=list&&!list.isEmpty())?(ExDateUtils.formatDateStr(list.get(0).getStartTime(),ExDateUtils.PATTREN_DATE_TIME)+" 至 "+ExDateUtils.formatDateStr(list.get(0).getEndTime(),ExDateUtils.PATTREN_DATE_TIME)):""));
			}	
		}catch (NumberFormatException nfe){
			logger.error("保存学籍异动申请出错：{}",nfe.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "入学总分转换出错!");
		}catch (ParseException pe){
			logger.error("保存学籍异动申请出错：{}",pe.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "解析入学日期出错!");
		}catch(DataIntegrityViolationException dve){
			logger.error("保存学籍异动申请出错：{}",dve.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "学生账号已经存在");
		}catch (Exception e) {
			logger.error("保存学籍异动申请出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
//			map.put("message", errorMsg+e.getLocalizedMessage());
			map.put("message", errorMsg);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 获取学生学号
	 * @param classic
	 * @param major
	 * @param teachType
	 * @param curGrade
	 * @return
	 */
	public String genStudentNo(Classic classic,Major major ,String teachType, Grade curGrade) {
		//学号前12位
		String prefix = genStudentNoPrefix(classic,major,teachType,curGrade);		
		//15-18位流水号
		String suffix = studentInfoChangeJDBCService.getMaxStudyNoWithSuffix(prefix);
		if("-".equals(suffix)){
			suffix = "0000";
		} else {
			suffix = increase(suffix);
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		int len = suffix.length();
		for(int i =0 ;i<4-len;i++){
			suffix = "0"+suffix ;
		}
		return prefix + suffix;
	}
	public String genStudentNoNew(String unitCode,Classic classic,Major major ,String teachType, Grade curGrade) {
		//学号前9位
		String prefix = genStudentNoPrefixNew(unitCode,classic,major,teachType,curGrade);		
		//10-12位流水号   20171220：更改为 13位，即10-13位为流水号
		String suffix = studentInfoChangeJDBCService.genMaxStudyNoWithSuffix(prefix);
		if("-".equals(suffix)){
			suffix = "0000";
		} else {
			suffix = increase(suffix);
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		int len = suffix.length();
		for(int i =0 ;i<4-len;i++){
			suffix = "0"+suffix ;
		}
		return prefix + suffix;
	}
	public String genStudentNoNew2(String unitCode,Classic classic,Major major ,String teachType, Grade curGrade) {
		//学号前9位
		String prefix = genStudentNoPrefixGZDX(unitCode,classic,major,teachType,curGrade);		
		//10-12位流水号
		String suffix = studentInfoChangeJDBCService.genMaxStudyNoWithSuffix2(prefix);
		if("-".equals(suffix)){
			suffix = "000";
		} else {
			suffix = increase(suffix);
			if ("999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		int len = suffix.length();
		for(int i =0 ;i<3-len;i++){
			suffix = "0"+suffix ;
		}
		return prefix + suffix;
	}
	public String genStudentNoGXYKD(String unitCode,Classic classic,Major major ,String teachType, Grade curGrade) {
		//学号前8位
		String prefix = genStudentNoPrefixGXYKD(unitCode,classic,major,teachType,curGrade);		
		//9-12位流水号
		String suffix = studentInfoChangeJDBCService.genMaxStudyNoWithSuffixGXYKD(prefix);
		if("-".equals(suffix)){
			suffix = "0000";
		} else {
			suffix = increase(suffix);
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		int len = suffix.length();
		for(int i =0 ;i<4-len;i++){
			suffix = "0"+suffix ;
		}
		return prefix + suffix;
	}
	public String getStudentNo(String unitCode,Classic classic,Major major,String teachType, Grade curGrade) {
		String prefix = "";
		String year = curGrade.getGradeName().substring(2, 4);
		String classicCode = classic.getClassicCode();
		prefix = year + classicCode;//学号前3位
		//4-8位流水号
		String suffix = studentInfoChangeJDBCService.getMaxStudyNoWithSuffix(prefix);
		if("-".equals(suffix)){
			suffix = "00000";
		} else {
			suffix = increase(suffix);
			if ("99999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		int len = suffix.length();
		for(int i =0 ;i<5-len;i++){
			suffix = "0"+suffix ;
		}
		return prefix + suffix;
	}
	//获取学号前缀
	public String genStudentNoPrefix(Classic classic,Major major,String teachType, Grade curGrade) {
		// 1-5位院校代码
		String prefix = "10571";
		// 6-7位年度  这里未明 感觉有4位
		Long year = curGrade.getYearInfo().getFirstYear() + ("2".equals(curGrade.getTerm()) ? 1L : 0L);
		prefix += String.valueOf(year);
		// 8位层次
		prefix += classic.getClassicCode();
		// 9位学习形式
		prefix += teachType;
		// 10-12位是专业代码(取后3位)
		String majorCode = major.getMajorCode();
		prefix += ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());	
		// 13-14位是班级(按照专业、层次的不同分)
		prefix += "01";
		prefix += "XJZR";
		return prefix;
	}
	//获取学号前缀
	public String genStudentNoPrefixNew(String unitCode,Classic classic,Major major,String teachType, Grade curGrade) {
		String prefix = "";
		// 1-2位--年度，当前学年？学生年级？
		String year = curGrade.getGradeName().substring(2,curGrade.getGradeName().length()-1);
		prefix += String.valueOf(year);
		
		// 3位--培养层次
		List<Dictionary> listdict = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where dictCode = 'studyno.replace.classiccode' ");
//		prefix += classic.getClassicCode();
		if(null != listdict && listdict.size() > 0){ //层次需要转换
			prefix += JstlCustomFunction.dictionaryCode2Name("studyno.replace.classiccode",classic.getClassicCode());
		}else{
			prefix += classic.getClassicCode();
		}
		
		//4位--学习形式
		prefix += teachType;

		// 5-7位是专业代码(取后3位)
//		String majorCode = major.getMajorCode();		
//		prefix += ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());
		//20171220:在表结构中增加 字段用于编学号，不再代码中写死
		String majorCode =ExStringUtils.isBlank(major.getMajorCode4StudyNo())?"000":major.getMajorCode4StudyNo();
//		RecruitMajor recruitMajor = recruitMajorService.findUniqueByProperty("major", major.getResourceid());
//		if(null != recruitMajor){
//			majorCode = recruitMajor.get
//		}
		try {
			
			// TODO:以后在2.0中会结合招生计划使用，
		    /*String sql = "select r.majorcodeforstudyno,r.majorid from edu_recruit_major r where r.majorid='" + major.getResourceid() + "'";
			List<RecuMajorVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql, RecuMajorVo.class, null);
			if(null != list && !list.isEmpty()){
				if(null != list.get(0).getMajorcodeforstudyno()) {
					majorCode = list.get(0).getMajorcodeforstudyno();
					prefix += majorCode;
				}
			}*/
			// TODO:目前先使用这种写死的方式，以后会删掉
//			String majorName = major.getMajorName();
//			if("临床医学".equals(majorName)){majorCode = "301";}
//			else if("护理学".equals(majorName) || "护理".equals(majorName)){majorCode = "701";}
//			else if("医学检验".equals(majorName)){majorCode = "304";}
//			else if("医学影像学".equals(majorName)){majorCode = "303";}
//			else if("药学".equals(majorName)){majorCode = "801";}
//			else if("信息管理与信息系统".equals(majorName)){majorCode = "704";}
//			else if("医学检验技术".equals(majorName)){majorCode = "354";}
//			else if("医学影像技术".equals(majorName)){majorCode = "353";}
//			else if("口腔医学".equals(majorName) && "本科".equals(classic.getEndPoint())){majorCode = "401";}
//			else if("口腔医学".equals(majorName) && !"本科".equals(classic.getEndPoint())){majorCode = "406";}
//			else if("中药学".equals(majorName)){
//				majorCode = "802";
//			} else if ("中药".equals(majorName)){
//				majorCode = "302";
//			} else if(ExStringUtils.isEmpty(majorCode)){
//				majorCode = major.getMajorCode();
//				majorCode =  ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());
//			}
			prefix += majorCode;
			
		} catch (Exception e) {
			if(logger.isErrorEnabled()){
				logger.error("{}", e.fillInStackTrace());
			}
		}
//		RecuMajorVo recuMajorVo = baseSupportJdbcDao.getBaseJdbcTemplate().findForObject(sql, RecuMajorVo.class, null);
		
		//8-9位--教学站
		prefix += unitCode;	
		
		return prefix;
	}
	//获取学号前缀
	public String genStudentNoPrefixGZDX(String unitCode,Classic classic,Major major,String teachType, Grade curGrade) {
		String prefix = "";
		// 1-2位--年度，当前学年？学生年级？
		String year = curGrade.getGradeName().substring(2,curGrade.getGradeName().length()-1);
		prefix += String.valueOf(year);
		
		// 3位--培养层次
		prefix += classic.getClassicCode();
		
		//4位--学习形式
		prefix += teachType;
		
		// 5-7位是专业代码(取后3位)
		String majorCode = major.getMajorCode();		
		prefix += majorCode;
		
		//8-9位--教学站
		prefix += unitCode;	
		
		return prefix;
	}
	//获取广西医科大学号前缀
	public String genStudentNoPrefixGXYKD(String unitCode,Classic classic,Major major,String teachType, Grade curGrade) {
		String prefix = "A";
		// 2-3位--年度，当前学年？学生年级？
		String year = curGrade.getGradeName().substring(2,curGrade.getGradeName().length()-1);
		prefix += String.valueOf(year);
		
		// 4-6位是专业代码(取后3位)
		String majorCode = major.getMajorCode();	
		prefix += majorCode;
//		prefix += ExStringUtils.substring(majorCode, majorCode.length()-3, majorCode.length());	
		
		//7-8位--教学站
		prefix += unitCode;	
		
		return prefix;
	}
	//自增
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
	/**
	 * 根据办学单位获取专业_学籍异动查询
	 * @param request
	 * @param response
	 */
/*	@RequestMapping("/edu3/framework/register/stuchangeinfo/brschool_Major_Classes.html")
	public void getBrschool_Major_Classes(HttpServletRequest request, HttpServletResponse response){
		
		
		String majorId   	= ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String brSchoolId	= ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String classId		= ExStringUtils.trimToEmpty(request.getParameter("classId"));
		Map<String, Object> map = new HashMap<String, Object>();
		
		if (ExStringUtils.isNotBlank(brSchoolId))  map.put("brSchoolId", brSchoolId);
		if (ExStringUtils.isNotBlank(majorId))     map.put("majorId", majorId);
		if (ExStringUtils.isNotBlank(classId))   map.put("teachType", classId);
		map.put("yesStr", Constants.BOOLEAN_YES);
		map.put("isDeleted",0);
		
		String majorSql = "select distinct m.resourceid ,m.majorname ,m.majorcode ";
		majorSql       += "  from edu_base_major m,edu_roll_classes c "; 
		majorSql       += " where c.majorid = m.resourceid and c.isdeleted = 0 "; 
		if (ExStringUtils.isNotBlank(brSchoolId)) majorSql       += " and c.orgunitid =:brSchoolId ";
		majorSql       += " order by m.majorcode asc ";
			
		
		String classSql = "select distinct c.resourceid ,c.classesname  ";
		classSql       += "  from edu_base_major m,edu_roll_classes c "; 
		classSql       += " where c.majorid = m.resourceid and c.isdeleted = 0 "; 
		if (ExStringUtils.isNotBlank(brSchoolId)) classSql       += " and c.orgunitid =:brSchoolId ";
		if (ExStringUtils.isNotBlank(majorId))    classSql       += " and c.majorid   =:majorId ";
		classSql       += " order by c.classesname asc ";
		List<Map<String,Object>> majors = new ArrayList<Map<String,Object>>();
		List<Map<String,Object>> classes = new ArrayList<Map<String,Object>>();
		try {
			majors                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(majorSql,map);
			classes                     = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(classSql,map);
		} catch (Exception e) {
				
		}
		if(null != majors && !majors.isEmpty()){
			StringBuffer majorOption = new StringBuffer("<option value=''></option>");
			for(Map<String,Object> m : majors){
				if(ExStringUtils.isNotEmpty(majorId)&&majorId.equals(m.get("resourceid"))){
					majorOption.append("<option selected='selected' value='"+m.get("resourceid")+"'");				
					majorOption.append(">"+m.get("majorcode")+"-"+m.get("majorname")+"</option>");
				}else{
					majorOption.append("<option value='"+m.get("resourceid")+"'");				
					majorOption.append(">"+m.get("majorcode")+"-"+m.get("majorname")+"</option>");
				}
			}
			map.put("majorOption", majorOption);
		}
		if(null != classes && !classes.isEmpty()){
			StringBuffer classesOption = new StringBuffer("<option value=''></option>");
			for(Map<String,Object> c : classes){
				if(ExStringUtils.isNotEmpty(classId)&&classId.equals(c.get("resourceid"))){
					classesOption.append("<option selected='selected' value='"+c.get("resourceid")+"'");				
					classesOption.append(">"+c.get("classesname")+"</option>");
				}else{
					classesOption.append("<option value='"+c.get("resourceid")+"'");				
					classesOption.append(">"+c.get("classesname")+"</option>");
				}
			}
			map.put("classesOption", classesOption);
		}	
		renderJson(response, JsonUtils.mapToJson(map));
	}*/
	
	/**
	 * (学习异动-校际转入)
	 * @param request
	 * @param stuinfo
	 */
	private void setParamsStuinfo(HttpServletRequest request,StudentBaseInfo stuinfo){
		//~~~~~~~~~~~~~~~~基本信息~~~~~~~~~~~~
		//由于目前默认关闭了学籍更改的日志记录功能，在此特别开启
		stuinfo.setEnableLogHistory(true);
		String contactAddress = ExStringUtils.trimToEmpty(request.getParameter("contactAddress"));
		String contactZipcode = ExStringUtils.trimToEmpty(request.getParameter("contactZipcode"));
		String contactPhone = ExStringUtils.trimToEmpty(request.getParameter("contactPhone"));
		String mobile = ExStringUtils.trimToEmpty(request.getParameter("mobile"));
		String email = ExStringUtils.trimToEmpty(request.getParameter("email"));
		String homePage = ExStringUtils.trimToEmpty(request.getParameter("homePage"));
		String height = ExStringUtils.trimToEmpty(request.getParameter("height"));
		String bloodType = ExStringUtils.trimToEmpty(request.getParameter("bloodType"));
		String bornAddress = ExStringUtils.trimToEmpty(request.getParameter("bornAddress"));
		String country = ExStringUtils.trimToEmpty(request.getParameter("country"));
		String certType = ExStringUtils.trimToEmpty(request.getParameter("certType"));
		String homePlace = ExStringUtils.trimToEmpty(request.getParameter("homePlaceAll"));//籍贯
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
		String residence = ExStringUtils.trimToEmpty(request.getParameter("residenceAll"));//居住地
//		String residence = ExStringUtils.trimToEmpty(request.getParameter("residence"));
		String currenAddress = ExStringUtils.trimToEmpty(request.getParameter("currenAddress"));
		String homeAddress = ExStringUtils.trimToEmpty(request.getParameter("homeAddress"));
		String homezipCode = ExStringUtils.trimToEmpty(request.getParameter("homezipCode"));
		String homePhone = ExStringUtils.trimToEmpty(request.getParameter("homePhone"));
		String officeName = ExStringUtils.trimToEmpty(request.getParameter("officeName"));
		String officePhone = ExStringUtils.trimToEmpty(request.getParameter("officePhone"));
//		String officeAddress = request.getParameter("officeAddress");
		String officeZipcode = request.getParameter("officeZipcode");
		String title = ExStringUtils.trimToEmpty(request.getParameter("title"));
		String specialization = ExStringUtils.trimToEmpty(request.getParameter("specialization"));
		String basememo = ExStringUtils.trimToEmpty(request.getParameter("basememo"));
		//增加对性别和身份证号以及出生日期、姓名的修改
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String gender = ExStringUtils.trimToEmpty(request.getParameter("gender"));
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String bornDay = ExStringUtils.trimToEmpty(request.getParameter("bornDay"));
		stuinfo.setName(name);
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
		stuinfo.setHeight(Long.getLong(height));
		stuinfo.setBloodType(bloodType);
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
//		stuinfo.setOfficeAddress(officeAddress);
		stuinfo.setOfficeZipcode(officeZipcode);
		stuinfo.setTitle(title);
		stuinfo.setSpecialization(specialization);
		stuinfo.setMemo(basememo);
		
	}
	
	
	/**
	 * 跨学年复学
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings({ "unchecked", "null" })
	@RequestMapping("/edu3/register/studentinfo/strideStudy.html")
	public String strideStudy(HttpServletRequest request, HttpServletResponse response,ModelMap model) throws WebException{
		System.out.println(request.getParameter("stuid"));
		String stuno = request.getParameter("stuid");//学号
		
		if(ExStringUtils.isNotEmpty(stuno)) {
			try{
				User currentUser = SpringSecurityHelper.getCurrentUser();

				StudentInfo info = studentInfoService.findUniqueByProperty("studyNo", stuno);
				if(null == info){
					throw new WebException("没有对应的学籍信息！");
				}
//				if( !"12".equals(info.getStudentStatus())){
//					throw new WebException("学籍状态为休学的学生才能操作此功能！");
//				}
				model.put("studentid",info.getResourceid());
								
				//登录用户是否为教务员
				if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(currentUser.getOrgUnit().getUnitType())){
					model.put("isadmin", "N");
					model.put("school", info.getStudentName());
				}else{
					//不是学习中心教务员就认为是可以总管所有学习中心的管理人员
					model.put("isadmin", "Y");
				}
				
				model.addAttribute("opMan",currentUser.getCnName());
				model.addAttribute("opManId",currentUser.getResourceid());
				if(null!=currentUser.getOrgUnit() && currentUser.getOrgUnit().getUnitType().				
						indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0
						&&!CacheAppManager.getSysConfigurationByCode("sysuser.usertype.student").getParamValue().equals(currentUser.getUserType())
						)//条件描述：用户的组织对象不为NULL，&& 用户组织类型为校外学习中心&& 当前用户的用户类型不是学生
				{
				
					if(!info.getBranchSchool().getResourceid().equals(currentUser.getOrgUnit().getResourceid())){
						throw new WebException("该学生不属于你的学习中心，请确认学号是否正确无误。");
					}
					
				}
				model.put("studyNo", info.getStudyNo());
				model.put("studentName", info.getStudentName());
				if(null == info.getBranchSchool()){
					throw new WebException("学生："+info.getStudentName()+" 没有关联学习中心！");
				}
				model.put("schoolCenter", info.getBranchSchool().getUnitName());
				model.put("schoolCenterid", info.getBranchSchool().getResourceid());
				if(null == info.getMajor()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的专业！");
				}
				model.put("major", info.getMajor().getMajorName());
				if(null == info.getClassic()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的层次！");
				}
				//新添加的 majorId 和 trainplan.SchoolType
				model.put("majorId", info.getMajor().getResourceid());				
				model.put("schoolType", info.getTeachingPlan().getSchoolType());//教学计划所属的办学模式
				if(null == info.getTeachingPlan().getSchoolType()) {
					throw new WebException("学生："+info.getTeachingPlan().getSchoolType()+" 没有关联的学习形式！");
				}
				
				Grade grade = gradeService.getDefaultGrade();
				if( null == grade ){
					throw new WebException("年级："+grade.getGradeName()+" 没有默认的年级！");
				}
				model.put("gradeName", grade.getGradeName());//当前年级
				model.put("gradeId2", grade.getResourceid());//当前年级id
				model.put("classic", info.getClassic().getClassicName());
				model.put("classicid", info.getClassic().getResourceid());
				model.put("classicId", info.getClassic().getResourceid());
				if(null == info.getGrade()) {
					throw new WebException("学生："+info.getStudentName()+" 没有关联的年级！");
				}
				model.put("grade", info.getGrade().getGradeName());
				model.put("gradeId", info.getGrade().getResourceid());//年级ID
				//if(ExStringUtils.isEmpty(info.getLearningStyle())){
				//	throw new WebException("学生："+info.getStudentName()+" 没有关联的学习形式！");
				//}
				//map.put("learingStyle", info.getLearningStyle());//学习形式
				//if(null == info.getTeachingPlan()){
				//	throw new WebException("学生："+info.getStudentName()+" 没有关联教学计划！");
				//}
				model.put("teachingPlan", info.getTeachingPlan().getResourceid());//所修教学计划
				model.put("brSchool", info.getBranchSchool().getResourceid());//所在学习中心
				model.put("studentstatus", info.getStudentStatus());//学籍状态
				if(null!=info.getClasses()){
					model.put("classes", info.getClasses().getClassname());//班级名称
					model.put("classesId", info.getClasses().getResourceid());//班级名称id
				}
				model.put("teachingtype", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",info.getTeachingType()));
				model.put("teachingtypeid", info.getTeachingType());
				
				//查找班级
				if(ExStringUtils.isNotBlank(info.getTeachingType())&&ExStringUtils.isNotBlank(info.getBranchSchool().getResourceid())&&ExStringUtils.isNotBlank(grade.getResourceid())){
					
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("gradeId", grade.getResourceid());
					map.put("classicId", info.getClassic().getResourceid());
					map.put("majorId", info.getMajor().getResourceid());
					map.put("brSchoolId", info.getBranchSchool().getResourceid());
					map.put("teachType", info.getTeachingType());
					
					//据说成教的招生不在系统。所以没有各学习中心的招生专业这类数据
					StringBuffer majorSql = new StringBuffer();
					
					majorSql.append(" select distinct t.resourceid,t.classesname from edu_roll_classes t where t.isdeleted= 0 ");
					majorSql.append(" and t.gradeid = :gradeId ");  
					majorSql.append(" and t.classicid = :classicId ");
					majorSql.append(" and t.majorid = :majorId ");
					majorSql.append(" and t.orgunitid =  :brSchoolId ");
					majorSql.append(" and t.teachingtype = :teachType ");
					
					
					List<Map<String,Object>> classes = new ArrayList<Map<String,Object>>();
					try {
						classes                      = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(majorSql.toString(),map);
					} catch (Exception e) {
						
					}
					if(null != classes && !classes.isEmpty()){
						model.put("classesList", classes);
					}else{
						throw new WebException("班级："+classes+" 没有关联的班级！");
					}
					
				}
			}catch (Exception e) {
				model.put("error", e.getMessage());
			}
	   }
	
		return "/edu3/roll/sturegister/schoolroll-strideStudy";
	}
	
	
	/**
	 * 保存（跨学年复学）
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/register/studentinfo/strideStudy/save.html")
	public void strideStudySave(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		String errorMsg = "学籍异动申请出错:";
		
		String stuNum = request.getParameter("strideStudy_stubum");// 学号		
		StudentInfo stu = studentInfoService.findUniqueByProperty("studyNo", stuNum);//学籍信息
		
		StuChangeInfo stuinfo = new StuChangeInfo();//异动信息
		
		
		try {
			if(null == stu){
				throw new Exception("学籍信息为空！");
			}
			setStuInfo(stuinfo,request,stu);
			
			stuChangeInfoService.save(stuinfo);
			
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_SCHOOLROLL_STU_CHANGE");
		//	map.put("reloadUrl", request.getContextPath() +"/edu3/register/stuchangeinfo/edit.html?resourceid="+stuChangeInfo.getResourceid());
		}catch (Exception e) {
			logger.error("学籍异动申请出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "学籍异动申请出错:<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	

	/**
	 * 为跨学年复学异动对象赋值
	 * @param stuChangeInfo
	 * @param request
	 * @param stu
	 * @throws Exception
	 */
	private void setStuInfo(StuChangeInfo stuChangeInfo,HttpServletRequest request, StudentInfo stu) throws Exception {
		try {
			String backSchool_majorid               = ExStringUtils.trimToEmpty(request.getParameter("strideStudy_majorid"));   //复学专业              
			String backSchool_brschool     		 	= ExStringUtils.trimToEmpty(request.getParameter("strideStudy_schoolCenterid"));//复学学院
			String backSchool_TeachingType 			= ExStringUtils.trimToEmpty(request.getParameter("strideStudy_teachingType"));//学习方式
			String backSchool_changeguidteachPlanId = ExStringUtils.trimToEmpty(request.getParameter("strideStudy_changeguidteachPlanId"));//复学教学计划
			String backSchool_classesid    		 	= ExStringUtils.trimToEmpty(request.getParameter("strideStudy_classesid"));//复学班级
			String memo 							= ExStringUtils.trimToEmpty(request.getParameter("memo"));//备注
			String reason 							= ExStringUtils.trimToEmpty(request.getParameter("reason"));//原因
			String backSchool_backSchoolDate        = ExStringUtils.trimToEmpty(request.getParameter("pauseStudy_backSchoolDate"));//复学时间
			String opMan 	= ExStringUtils.trimToEmpty(request.getParameter("strideStudy_opman")); //操作员
			String opManId 	= ExStringUtils.trimToEmpty(request.getParameter("strideStudy_opmanid"));//操作员id
			Date date                   = ExDateUtils.getCurrentDateTime();
			
			if(ExStringUtils.isNotEmpty(backSchool_changeguidteachPlanId)){
				TeachingPlan tp = stu.getTeachingPlan();
				Grade g = stu.getGrade();
				List<TeachingGuidePlan> tgps = teachingGuidePlanService.findByHql(" from "+TeachingGuidePlan.class.getSimpleName()+" where isDeleted = 0 and grade.resourceid = ? and teachingPlan.resourceid = ? ",new Object[]{g.getResourceid(),tp.getResourceid()} ); 
				if(1!=tgps.size()){
					throw new Exception("无法确定原年度教学计划");
				}else{
					stuChangeInfo.setChangeBeforeTeachingGuidePlan(tgps.get(0));
					TeachingGuidePlan tgp_after = teachingGuidePlanService.get(backSchool_changeguidteachPlanId);
					stuChangeInfo.setChangeTeachingGuidePlan(tgp_after);
				}
			}
			if(ExStringUtils.isNotEmpty(backSchool_brschool)){ //学习中心
				stuChangeInfo.setChangeBeforeBrSchool(stu.getBranchSchool());
				OrgUnit changeUnit = orgUnitService.get(backSchool_brschool);
				stuChangeInfo.setChangeBrschool(changeUnit); //跨学年复学后学习中心
				stuChangeInfo.setChangeBeforeBrSchool(changeUnit);//原学习中心
				stuChangeInfo.setChangeBeforeSchoolName(changeUnit.getUnitName());//原学习中心名称
			}
			if(ExStringUtils.isNotEmpty(backSchool_classesid)){//班级
				Classes changeClass = classesService.get(backSchool_classesid);
				stuChangeInfo.setChangeClass(changeClass);//跨学年复学后班级	
				stuChangeInfo.setChangeBeforeClass(stu.getClasses());//原班级
			}
			if(ExStringUtils.isNotEmpty(backSchool_TeachingType)){//学习方式
				stuChangeInfo.setChangeBeforeLearingStyle(backSchool_TeachingType);//原学习方式
				stuChangeInfo.setChangeTeachingType(backSchool_TeachingType);//跨学年复学后学习方式
			}
			if(ExStringUtils.isNotEmpty(backSchool_majorid)){//专业
				Major major = majorService.get(backSchool_majorid);
				stuChangeInfo.setChangeMajor(major);//跨学年复学后专业ID
				stuChangeInfo.setChangeBeforeMajorName(major.getMajorName());//原专业名称
			}
			if(ExStringUtils.isNotEmpty(memo)){
				stuChangeInfo.setMemo(memo);//备注
			}
			if(ExStringUtils.isNotEmpty(reason)){
				stuChangeInfo.setReason(reason);//原因
			}
			if(ExStringUtils.isNotEmpty(backSchool_backSchoolDate)){
				stuChangeInfo.setEndDate(ExDateUtils.convertToDate(backSchool_backSchoolDate));//申请复学时间
			}
			if(ExStringUtils.isNotEmpty(opMan)){
				stuChangeInfo.setApplicationMan(opMan);//操作员
			}		
			if(ExStringUtils.isNotEmpty(opManId)){
				stuChangeInfo.setApplicationManId(opManId);//操作员ID
			}
			
			if(ExStringUtils.isNotEmpty(stu.getClassic().getClassicName())){
				stuChangeInfo.setChangeBeforeClassicName(stu.getClassic().getClassicName());//原层次名称
				stuChangeInfo.setChangeClassicId(stu.getClassic());//跨学年复学后层次
			}

			stuChangeInfo.setApplicationDate(date); //申请时间
			stuChangeInfo.setFinalAuditStatus(Constants.BOOLEAN_WAIT); //审批状态
			stuChangeInfo.setChangeBeforeStudentStatus(stu.getStudentStatus());//原学生状态
			stuChangeInfo.setChangeType("c_25");//异动类型	跨学年复学
			stuChangeInfo.setStudentInfo(stu);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 安徽医校际转入学号的生成
	 * @author Zik
	 * @param unitCode
	 * @param classic
	 * @param major
	 * @param changeIntoSchool_teachingType
	 * @param grade
	 * @return
	 */
	public String genStudentNoAHYKD(String unitCode, Classic classic,
			Major major, String changeIntoSchool_teachingType, Grade grade) {
		
		// 学号前6位
		String prefix = genStudentAHYKDPrefix(unitCode, classic, major, changeIntoSchool_teachingType, grade);
		//7-10位流水号
		String suffix = studentInfoChangeJDBCService.getMaxStudyNoWithSuffixAHYKD(prefix);
		if("-".equals(suffix)){
			suffix = "0000";
		} else {
			suffix = increase(suffix);
			if ("9999".equals(suffix)) {
				throw new ServiceException("没有空余的学号了！");
			}
		}
		int len = suffix.length();
		for(int i =0 ;i<4-len;i++){
			suffix = "0"+suffix ;
		}
		return prefix + suffix;
	}
	
	/**
	 * 获取安徽医科大学学号前缀
	 * @author Zik
	 * @param unitCode
	 * @param classic
	 * @param major
	 * @param changeIntoSchool_teachingType
	 * @param grade
	 * @return
	 */
	private String genStudentAHYKDPrefix(String unitCode, Classic classic,
			Major major, String changeIntoSchool_teachingType, Grade grade){
		String prefix = "";
		// 1-2位--年度，当前学年？学生年级？
		String year = grade.getGradeName().substring(2,grade.getGradeName().length()-1);
		prefix += String.valueOf(year);
		//3 编制
		try {
			prefix += CacheAppManager.getSysConfigurationByCode("school.studyno.compile").getParamValue(); 
		} catch (Exception e) {
			logger.error("获取安徽医学院学号编制单位错误（全局参数：school.studyno.compile）;"+e.fillInStackTrace());
			throw  new WebException("获取安徽医学院学号编制单位错误");
		}
		//4 层次
		List<Dictionary> listdict = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+" where dictCode = 'studyno.replace.classiccode' ");
		if(null != listdict && listdict.size() > 0){ //层次需要转换
			String classicCode = JstlCustomFunction.dictionaryCode2Name("studyno.replace.classiccode",classic.getClassicCode());
			if(ExStringUtils.isEmpty(classicCode)) {
				throw  new WebException("获取学号前缀错误，层次编码为空");
			}
			prefix += classicCode;
		}else{
			prefix += classic.getClassicCode();
		}
		//5-6 专业
		prefix += major.getMajorCode();
		return prefix;
	}
	
	/**
	 * 学籍异动审批表-打印预览
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/stuchangeinfo/approvalForm-printview.html")
	public String viewApprovalForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		String stuChangeIds = request.getParameter("stuChangeIds");
		String isPdf =  request.getParameter("isPdf");
		String isRefundInformation = request.getParameter("isRefundInformation");
		// 传参数
		if(ExStringUtils.isNotEmpty(isPdf)){
			model.addAttribute("isPdf", isPdf);
		}
		if(ExStringUtils.isNotEmpty(isRefundInformation)){
			model.addAttribute("isRefundInformation", isRefundInformation);
		}
		model.addAttribute("stuChangeIds", stuChangeIds);
		
		return "/edu3/roll/stuchangeinfo/approvalForm-printview";
	}
	
	/**
	 * 学籍异动审批表-打印
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/stuchangeinfo/approvalForm-print.html")
	public void printApprovalForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		String stuChangeIds = request.getParameter("stuChangeIds");
		if(stuChangeIds != null) {
			stuChangeIds = ExStringUtils.trim(stuChangeIds);
		}
		
		try {
			stuChangeIds = stuChangeIds.replaceAll(",", "','");
			List<StuChangeInfo> stuChangeInfoList = stuChangeInfoService.findByResourceIds(stuChangeIds);
			// 打印所选的学籍异动审批表
			if(stuChangeInfoList != null && stuChangeInfoList.size() > 0){
				// 学籍异动审批表类型
				List<Dictionary> stuchangeModeList = CacheAppManager.getChildren("printStuchangeModel");
				Map<String,String> stuchangeModeMap = new HashMap<String, String>();
				if(stuchangeModeList!=null && stuchangeModeList.size()>0){
					for(Dictionary dic : stuchangeModeList){
						stuchangeModeMap.put(dic.getDictName(), dic.getDictValue());
					}
				}
				// 学籍异动类型在审批表中显示的名称
				List<Dictionary> changTypeToNameList = CacheAppManager.getChildren("changeTypeToName");
				Map<String,String> changTypeToNameMap = new HashMap<String, String>();
				if(changTypeToNameList!=null && changTypeToNameList.size()>0){
					for(Dictionary d : changTypeToNameList){
						changTypeToNameMap.put(d.getDictName(), d.getDictValue());
					}
				}
				
				// 学习形式
				List<Dictionary> teachTypeList = CacheAppManager.getChildren("CodeTeachingType");
				Map<String,String> teachTypeMap = new HashMap<String, String>();
				if(teachTypeList!=null && teachTypeList.size()>0){
					for(Dictionary d : teachTypeList){
						teachTypeMap.put(d.getDictValue(), d.getDictName());
					}
				}
				// 打印学籍异动审批表
				printApprovalForm(request, response, stuChangeInfoList,stuchangeModeMap,changTypeToNameMap,teachTypeMap);
				
			}
		} catch (Exception e) {
			logger.error("打印学籍异动审批表出错：", e);;
		}
	}

	/**
	 * 打印单个学籍异动审批表
	 * 
	 * @param request
	 * @param response
	 * @param stuChangeInfoList
	 */
	@SuppressWarnings("rawtypes")
	private void printApprovalForm(HttpServletRequest request, HttpServletResponse response, List<StuChangeInfo> stuChangeInfoList,
			Map<String,String> stuchangeModeMap,Map<String,String> changTypeToNameMap,Map<String,String> teachTypeMap) {
		Map<String, Object> param = new HashMap<String, Object>();
		
		try {
			String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			String transactForm		 = CacheAppManager.getSysConfigurationByCode("graduateData.transactForm").getParamValue();
			String isPdf = request.getParameter("isPdf");
			String isRefundInformation = request.getParameter("isRefundInformation");
			String errorMsg = "";
			String excelName = "";
			if(!"Y".equals(isRefundInformation)){
				excelName = "学籍异动申请表";
			}else{
				excelName = "学生退费银行信息";
			}
			List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>();
			List<String> studentidList = new ArrayList<String>(); 
			for (StuChangeInfo stuChange :stuChangeInfoList) {
				String changeType = stuChange.getChangeType();// 异动类型
				StudentInfo studentInfo = stuChange.getStudentInfo();
				studentidList.add(studentInfo.getResourceid());
				String prefix = "";//jasper文件前缀，用于区分不同学校使用的模版
				if(!"Y".equals(isRefundInformation)){
					if(!"W".equals(stuChange.getFinalAuditStatus())) {
						errorMsg = "学生："+studentInfo.getStudentName() +" 的这条记录不是未审核状态！";
						break;
					}
					if(!changTypeToNameMap.containsKey(changeType)){
						errorMsg = "学生："+studentInfo.getStudentName() +" 申请的异动类型不能打印！";
						break;
					}
					String changeTypeName = changTypeToNameMap.get(changeType);
					// 自定义学校名称
					String schoolTitle = schoolName;
					// 完整的自定义标题、异动类型标题
					String title = schoolName+schoolConnectName+changeTypeName;
					param.put("logoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
					StudentBaseInfo student = studentInfo.getStudentBaseInfo();
					// 学生基本信息
					param.put("studentName", student.getName());
					param.put("gender", "1".equals(student.getGender()) ?"男":"女");
					param.put("phone", student.getContactPhone()==null?student.getMobile():student.getContactPhone());
					param.put("certNum", student.getCertNum());
					param.put("address", student.getContactAddress());
					param.put("zipcode", student.getContactZipcode());
					// 学生学籍信息
					String unitName = studentInfo.getBranchSchool()==null?"":studentInfo.getBranchSchool().getUnitName();
					String majorName = studentInfo.getMajor()==null?"":studentInfo.getMajor().getMajorName();
					// 去掉中括号里面的内容包括中括号
					majorName = majorName.replaceAll("\\[.*?\\]", "");
	//				majorName = majorName.substring(0, majorName.lastIndexOf("["));
					param.put("studentNo", studentInfo.getStudyNo());
					param.put("className", studentInfo.getClasses()==null?"":studentInfo.getClasses().getClassname());
					param.put("unitName", unitName);
					param.put("majorName", majorName);
					param.put("gradeName", studentInfo.getGrade()==null?"":studentInfo.getGrade().getGradeName());
					param.put("educationSystem", studentInfo.getTeachingPlan()==null?"":studentInfo.getTeachingPlan().getEduYear());
					param.put("classicName", studentInfo.getClassic()==null?"":studentInfo.getClassic().getClassicName());
					param.put("teachType", teachTypeMap.get(studentInfo.getTeachingType()));
					param.put("score", studentInfo.getGraduatePaperScore());
					param.put("certNum", student.getCertNum());
					param.put("enrolleeCode", studentInfo.getEnrolleeCode());
					param.put("examCertificateNo", studentInfo.getExamCertificateNo());
					
					// 异动信息
					String unitNameAfterChang ="";
					String gradeNameAfterChang ="";
					String classicNameAfterChange =""; 
					String majorNameAfterChang ="";
					String learningStyleAfterChange ="";
					String classNameAfterChang ="";
					String unitChangeInfo = "";
					String majorChangeInfo = "";
					String startDate = "";
					String endDate = "";
					String applyDate = "";
					String inDate = "";
					//异动后教学点
					unitNameAfterChang = stuChange.getChangeBrschool()==null?"":stuChange.getChangeBrschool().getUnitName();
					//异动后年级
					gradeNameAfterChang = stuChange.getChangeTeachingGuidePlan()==null?"":stuChange.getChangeTeachingGuidePlan().getGrade().getGradeName();
					//异动后层次
					classicNameAfterChange = stuChange.getChangeClassicId()==null?"":stuChange.getChangeClassicId().getShortName();
					//异动后专业
					majorNameAfterChang = stuChange.getChangeMajor()==null?"":stuChange.getChangeMajor().getMajorName();
					//异动后学习形式
					learningStyleAfterChange = stuChange.getChangeTeachingType()==null?"":stuChange.getChangeTeachingType();
					if(ExStringUtils.isEmpty(learningStyleAfterChange)&&ExStringUtils.isNotEmpty(gradeNameAfterChang)){
						learningStyleAfterChange = stuChange.getChangeTeachingGuidePlan().getTeachingPlan().getSchoolType();
					}
					learningStyleAfterChange = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", learningStyleAfterChange);
					//异动后班级
					classNameAfterChang = stuChange.getChangeClass()==null?"":stuChange.getChangeClass().getClassname();
					//开始时间/审核时间
					startDate = ExDateUtils.formatDateStrWithSpace(stuChange.getAuditDate(), 2);
					//结束时间
					endDate = ExDateUtils.formatDateStrWithSpace(stuChange.getEndDate(), 2);
					//申请时间
					applyDate = ExDateUtils.formatDateStrWithSpace(stuChange.getApplicationDate(), 2);
					//入学日期
					inDate = ExDateUtils.formatDateStr(stuChange.getStudentInfo().getInDate(),1);
					if("11".equals(changeType)){//休学
						//休学期限
						startDate = applyDate;
					}else if("12".equals(changeType)){// 复学
						//休学期限
						startDate = applyDate;
						param.put("memo", stuChange.getMemo());
					}else if ("13".equals(changeType)) {//退学
						//在校时间
						Calendar c = Calendar.getInstance();
						c.setTime(stuChange.getApplicationDate());
						int year = c.get(Calendar.YEAR);
						int month = c.get(Calendar.MONTH)+1;
						if(month<3) {
							year--;
						}
						endDate = startDate;
						startDate = year+" 年 3 月 1 日";
					}else if("23".equals(changeType)){// 转专业
						majorChangeInfo = "从 "+majorName+" 专业转入 "+majorNameAfterChang+" 专业";
						param.put("changeMajorLabel", "异动信息");
					} else if("81".equals(changeType)){// 转专业(含转教学站、学习形式)
						unitChangeInfo = "从 "+unitName+" 教学点转入 "+unitNameAfterChang+" 教学点";
						majorChangeInfo = "从 "+majorName+" 专业转入 "+majorNameAfterChang+" 专业";
						param.put("changeInfoLabel", "异动信息");
					} else if("82".equals(changeType)){// 更改教学点
						unitChangeInfo = "从 "+unitName+" 教学点转入 "+unitNameAfterChang+" 教学点";
						param.put("changeUnitLabel", "异动信息");
					}
					// 申请原因
					String applyReason = "  "+ExStringUtils.toString(stuChange.getReason());;
					if("10598".equals(schoolCode)){//广西医科大学
						startDate = "";
						if(!stuchangeModeMap.get(changeType).contains("_")) {
							prefix = "gxy_";
						}
						applyReason = " 申请"+changeTypeName+"理由：";
					}else if("10571".equals(schoolCode)){//广东医科大学
						startDate = "";
						if(!stuchangeModeMap.get(changeType).contains("_")) {
							prefix = "gdy_";
						}
					}else if ("10560".equals(schoolCode)) {//汕头大学
						startDate = "";
						if(!stuchangeModeMap.get(changeType).contains("_")) {
							prefix = "stdx_";
						}
						//汕大标题两行显示
						schoolTitle += transactForm;
						title = changeTypeName + "申请表";
						param.put("phone",ExStringUtils.appendSpace(param.get("phone").toString()));
					}else if("11846".equals(schoolCode)){//广东外语外贸
						if(!stuchangeModeMap.get(changeType).contains("_")) {
							prefix = "gdwy_";
						}
						schoolTitle += "成人高等学历教育";
						title = changeTypeName + "申请表";
					}else {//其它默认
						if(!stuchangeModeMap.get(changeType).contains("_")) {
							prefix = "gxy_";
						}
					}

					param.put("applyReason", applyReason);
	//				afterChangMajorName = afterChangMajorName.substring(0, afterChangMajorName.lastIndexOf("["));
					majorNameAfterChang = majorNameAfterChang.replaceAll("\\[.*?\\]", "");
					param.put("afterUnitName", unitNameAfterChang);
					param.put("afterGradeName", gradeNameAfterChang);
					param.put("afterClassicName", classicNameAfterChange);
					param.put("afterLearningStyle", learningStyleAfterChange);
					param.put("afterMajorName", majorNameAfterChang);
					param.put("afterClassName", classNameAfterChang);
					param.put("unitChangeInfo", unitChangeInfo);
					param.put("majorChangeInfo", majorChangeInfo);
					param.put("startDate",startDate);
					param.put("endDate", endDate);
					param.put("inDate", inDate);
					param.put("applyDate", applyDate);
					//一级标题，自定义学校名称
					param.put("schoolTitle", schoolTitle);
					//二级标题，异动类型名称
					param.put("title", title);
					// 通知单信息
					param.put("noticeTitle", changeTypeName+"通知");
					//学校名称 + 继续教育学院
					param.put("shoolTotalName", schoolName+"继续教育学院");
					// 学籍异动审批人
					List<Dictionary> approvalManDicList = CacheAppManager.getChildren("stuchangeApprovalMan");
					List<Map<String,String>> approvalManList = new ArrayList<Map<String,String>>();
					if(approvalManDicList!=null && approvalManDicList.size() > 0) {
						for(Dictionary d : approvalManDicList) {
							Map<String,String> approvalMan = new HashMap<String, String>();
							approvalMan.put("approvalMan", d.getDictValue());
							approvalManList.add(approvalMan);
						}
					}
				
					//JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(approvalManList);
					JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(approvalManList);
					String reportPath = File.separator+"reports"+File.separator+"stuChangeInfo"+File.separator+prefix+stuchangeModeMap.get(changeType)+".jasper";
					String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
					
					JasperPrint jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					jasperPrints.add(jasperPrint);
				}else {//获取退费银行信息
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("studentid", studentInfo.getResourceid());
					//map.put("studentids",ExStringUtils.toString(studentidList).replace(",", "','"));
					List<StuReturnFeeCommissionInfoVo> stuFeeCommissionInfo = studentPaymentService.getStuFeeCommissionInfo(map);
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(stuFeeCommissionInfo);
					param.put("title", schoolName+schoolConnectName+"学生退学费信息表");
					param.put("fillDate", ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE));
					String reportPath = File.separator+"reports"+File.separator+"stuChangeInfo"+File.separator+prefix+"refundInformation.jasper";
					String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
					JasperPrint jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					jasperPrints.add(jasperPrint);
				}
			}
			
			int i = 0;
			JasperPrint jasperPrint = new JasperPrint();
			for (JasperPrint print : jasperPrints) {
				if(i==0){
					jasperPrint = print;
					i++;
				}else{
					List<JRBasePrintPage> pages = (List<JRBasePrintPage>)print.getPages();
					for (JRBasePrintPage page : pages) {
						jasperPrint.addPage(page);
					}
				}
			}
			if(!ExStringUtils.isEmpty(errorMsg)){
				renderHtml(response,"<script>alert('"+errorMsg+"')</script>");
			}else {
				if (null!=jasperPrint) {
					if(ExStringUtils.isNotEmpty(isPdf)){
						GUIDUtils.init();	
						String filePath        		  = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
						JRPdfExporter exporter = new JRPdfExporter();
						exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);   
						exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,"UTF-8");
						exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
						exporter.exportReport(); 
						downloadFile(response,excelName+".pdf",filePath,true);
					}else{
						renderStream(response, jasperPrint);
					}
				}else {
					renderHtml(response,"缺少打印数据！");
				}
			}
			
		} catch (Exception e) {
			logger.error("打印单个学籍异动审批表出错：", e);
		}
	}
	
	/**
	 * 显示检查复学的成绩
	 * 
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/self/viewExamResult.html")
	public String viewExamResult(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		try {
			String studentNo = request.getParameter("studentNo");
			String studentName = request.getParameter("studentName");
			String studentId = request.getParameter("studentId");
			String teachGuidePlan = request.getParameter("teachGuidePlan");
			String brSchoolid = request.getParameter("brSchool");
			
			String seeOnly = request.getParameter("seeOnly");//是否只是查看
			if(ExStringUtils.isNotBlank(seeOnly)) {
				model.addAttribute("seeOnly",seeOnly);
			}
			
			Map<String, Object> condition = new HashMap<String, Object>();
		
			if(ExStringUtils.isNotBlank(studentNo)) {
				condition.put("studyNo", studentNo.trim());
			}
			if(ExStringUtils.isNotBlank(studentName)) {
				condition.put("studyName", studentName.trim());
			}
			if(ExStringUtils.isNotBlank(studentId)) {
				condition.put("studyId", studentId.trim());
			}
			
			TeachingGuidePlan teachingGuidePlan = null;
			if(ExStringUtils.isNotBlank(teachGuidePlan)){
				teachingGuidePlan = teachingGuidePlanService.findUnique("from TeachingGuidePlan t where t.resourceid=? and t.isDeleted=0", teachGuidePlan);
			}
			
			StudentInfo studentInfo = studentInfoService.findUniqueStudentInfo(condition);
			
			model.addAttribute("studentInfo",studentInfo);
			List<StudentLearnPlan> stuplanlist = stuChangeInfoService.updateStudentLearnPlanForMarkDelete(studentInfo, teachingGuidePlan,brSchoolid);
			
			if(ExStringUtils.isBlank(seeOnly)){
				List<ExamResults> templist = new ArrayList<ExamResults>();
				for(StudentLearnPlan slp : stuplanlist){
					if("Y".equals(slp.getExamResults().getIsMarkDelete())){
						List<ExamResults> results = studentExamResultsService.findByHql("from ExamResults t where t.resourceid=? and t.isDeleted=0", slp.getExamResults().getResourceid());
						if(results.size()>0){
							results.get(0).setIsMarkDelete("N");
							templist.add(results.get(0));
						}
					}
				}				
				studentExamResultsService.batchSaveOrUpdate(templist);		
				
				stuplanlist = stuChangeInfoService.updateStudentLearnPlanForMarkDelete(studentInfo, teachingGuidePlan,brSchoolid);
			}
			
			//分离及格成绩,不及格成绩,计划外成绩
			List<StudentLearnPlan> failList1 = new ArrayList<StudentLearnPlan>();
			List<StudentLearnPlan> passList1 = new ArrayList<StudentLearnPlan>();
			List<StudentLearnPlan> outOfPlanList1 = new ArrayList<StudentLearnPlan>();
					
			for(StudentLearnPlan slp : stuplanlist){
				if(slp.getPlanoutCourse()!=null){
					outOfPlanList1.add(slp);
				}else if(ExStringUtils.isNotBlank(slp.getFinalScore()) ){//这里为null
					try {
						if(Double.parseDouble(slp.getFinalScore())>=60.0){
							slp.setIsPass("Y");
							passList1.add(slp);
						}else{
							slp.setIsPass("N");
							failList1.add(slp);
						}
					} catch (Exception e) {
						slp.setIsPass("N");
						failList1.add(slp);// 解析错误就是不及格
					}
				}
					
			}
			
			model.addAttribute("failList",failList1);
			model.addAttribute("passList",passList1);
			model.addAttribute("outOfPlanList",outOfPlanList1);
			
			/*//以前的未替换
			List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(studentInfo);
			
			if(ExStringUtils.isBlank(seeOnly)){
				List<ExamResults> templist = new ArrayList<ExamResults>();
				for(StudentExamResultsVo vo : list){
					if("Y".equals(vo.getIsMarkDelete())){
						List<ExamResults> results = studentExamResultsService.findByHql("from ExamResults t where t.resourceid=? and t.isDeleted=0", vo.getExamResultId());
						if(results.size()>0){
							results.get(0).setIsMarkDelete("N");
							templist.add(results.get(0));
						}
					}
				}				
				studentExamResultsService.batchSaveOrUpdate(templist);		
				
				list = studentExamResultsService.studentExamResultsList(studentInfo);
			}
			
			//分离及格成绩,不及格成绩,计划外成绩
			List<StudentExamResultsVo> failList = new ArrayList<StudentExamResultsVo>();
			List<StudentExamResultsVo> passList = new ArrayList<StudentExamResultsVo>();
			List<StudentExamResultsVo> outOfPlanList = new ArrayList<StudentExamResultsVo>();
			
			if(list.size()>0){
				list.remove(list.size()-1);
			}
					
			for(StudentExamResultsVo vo : list){
				if(vo.getIsOutplancourse().equals("Y")){
					outOfPlanList.add(vo);
				}else if(vo.getIntegratedScoreL()!=null && vo.getIntegratedScoreL()>=60){
					passList.add(vo);
				}else{
					failList.add(vo);
					
				}
			}
			
			model.addAttribute("failList",failList);
			model.addAttribute("passList",passList);
			model.addAttribute("outOfPlanList",outOfPlanList);*/
		
		} catch (Exception e) {
			logger.error("查看复学成绩出错：", e);
		}
		return "/edu3/roll/stuchangeinfo/examresults-view";
	}
	
	/**
	 * 复学成绩设置为标记删除
	 * 
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/self/setDeleteMark.html")
	public void setDeleteMark(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		String ids = request.getParameter("ids");

		Map<String ,Object> map = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer();				
		try {		
			if(ExStringUtils.isNotBlank(ids)){
				List<ExamResults> list = examResultsService.findByHql("from ExamResults t where t.resourceid in ("+ids.substring(0, ids.length()-1)+")");
				
				for(ExamResults rs:list){
					rs.setIsMarkDelete("Y");
				}
				
				examResultsService.batchSaveOrUpdate(list);
			}			
			map.put("statusCode", 200);
			message.append("完成");
			map.put("message", message);
		} catch (Exception e) {
			logger.error("设置标志出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "设置标志出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 验证银行卡号
	 * @param cardNo
	 * @param response
	 */
	@RequestMapping("/edu3/register/stuchangeinfo/validateCardNo.html")
	public void name(String cardNo,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String bankName = CardNoUtils.getName(cardNo);
			boolean isCorrect = CardNoUtils.checkBankCard(cardNo);
			map.put("bankName", bankName);
			map.put("isCorrect", isCorrect);
			map.put("statusCode", 200);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 进入退补教材费表单
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/stuchangeinfo/refundSupple-form.html")
	public String refundSuppleForm(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		String resouceId = request.getParameter("resouceId");
		model.addAttribute("resouceId", resouceId);
		
		return "/edu3/roll/stuchangeinfo/refundSupple-form";
	}
	
	/**
	 * 生成退费或补交教材费订单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/stuchangeinfo/refundSuppleFee.html")
	public void refundSuppleFee(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "操作成功";
		try {
				String resouceId = request.getParameter("resouceId");
				String processType = request.getParameter("processType");
				String money = ExStringUtils.trimToEmpty(request.getParameter("money"));
				Double payMoney = Double.valueOf(money);
				// 处理退费或补交教材费
				Map<String,Object> returnMap = refundbackService.refundSuppleTextbookFee(resouceId, processType, payMoney);
				statusCode = (Integer)returnMap.get("statusCode");
				message = (String)returnMap.get("message");
		} catch (Exception e) {
			logger.error("生成退费或补交教材费订单出错", e);
			statusCode = 300;
			message = "操作失败";
		}finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 处理学费
	 * 
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/roll/stuchangeinfo/handleTuition.html")
	public void handleTuition(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "处理成功";
		try {
				String resouceId = request.getParameter("resouceId");
				// 处理退费补交学费
				StuChangeInfo changeInfo = stuChangeInfoService.get(resouceId);
				Map<String,Object> returnMap = returnPremiumService.handleTuitionForStuChange(changeInfo);
				statusCode = (Integer)returnMap.get("statusCode");
				message = (String)returnMap.get("message");
		} catch (Exception e) {
			logger.error("处理学费出错", e);
			statusCode = 300;
			message = "处理失败";
		}finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/roll/stuchangeinfo/displayAttachs.html")
	public String listAttachs(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) {
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			List<Attachs> list = attachsService.findAttachsByFormId(resourceid);
			if(ExBeanUtils.isNotNullOfAll(list)){
				model.addAttribute("attachList", list);
			}
		} catch (Exception e) {
			
			statusCode = 300;
			message = "加载失败";
		}finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		
		return "/edu3/roll/stuchangeinfo/stuchangeinfo-attachList";
	}
}
