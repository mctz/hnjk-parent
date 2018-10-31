package com.hnjk.edu.roll.controller;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.edu.util.Condition2SQLHelper;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExNumberUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IGraduationQualifService;
import com.hnjk.edu.roll.service.IGraduationStatJDBCService;
import com.hnjk.edu.roll.service.IStudentGDAuditJDBCService;
import com.hnjk.edu.roll.service.IStudentGraduateAndDegreeAuditService;
import com.hnjk.edu.roll.service.IStudentInfoChangeJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.vo.GraduationInfoExportVo;
import com.hnjk.edu.roll.vo.StudentInfoVo;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * <code>GradeFeeRuleController</code><p>
 * 毕业资格   毕业资格审核
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-3-21 下午17:34:32
 * @see 
 * @version 1.0
 */

@Controller
public class GraduationQualifController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 9101290846539570844L;

	@Autowired
	@Qualifier("graduationQualifService")
	private IGraduationQualifService graduationQualifService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
//	@Autowired
//	@Qualifier("stuperpayfeeservice")
//	private IStuPerpayfeeService stuperpayfeeservice;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;//学生成绩服务
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;//在线学习模块公共服务
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("studentInfoChangeJDBCService")
	private IStudentInfoChangeJDBCService studentInfoChangeJDBCService;
	
	@Autowired
	@Qualifier("studentGraduateAndDegreeAuditService")
	private IStudentGraduateAndDegreeAuditService studentGraduateAndDegreeAuditService;

	@Autowired
	@Qualifier("studentGDAuditJDBCService")
	private IStudentGDAuditJDBCService studentGDAuditJDBCService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao jdbcDao;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;//注入考试/毕业论文批次预约服务 
	
	@Autowired
	@Qualifier("graduationStatJDBCService")
	private IGraduationStatJDBCService graduationStatJDBCService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private  IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/**
	 * 获得当前年的数字表达
	 * @return
	 */
	private String getCurrentYear(){
		//当前年度 如果是 2011秋 2011.5 2011春 2011
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
		return currentYearInfo;
	}
	/**
	 * 查询分页列表 查看 毕业资格列表
	 * @param request
	 * @param Page
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/qualif/list.html")
	public String exeQualifList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式

		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus = ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//身份证号
		
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
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		//添加与毕业资格筛选的功能
		//新增年级
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("gradeid", grade);
		}
		String currentYearInfo = getCurrentYear();
		condition.put("currentYearInfo", currentYearInfo);
		
		//增加是毕业筛选的标记
		condition.put("isGraduateQualifer", 1);
		Page page = graduationQualifService.findGraduateionQualifByCondition(condition, objPage);
		
		model.addAttribute("graduationQualifList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/roll/graduationQualif/graduationQualif-list";
	}
	
	/**
	 * 导出学位名单 
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduateaudit/export.html")
	public void doExcelExportStudentGraduate(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		StringBuffer title = new StringBuffer();
		//从库中查出数据	
		String graduateDateInDegree = ExStringUtils.trimToEmpty(request.getParameter("graduateDateInDegree"));
		List<GraduateData> list = new ArrayList<GraduateData>(0);
		if(!"".equals(graduateDateInDegree)){
			list = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where  isDeleted = 0 and graduateDate=to_date(?,'yyyy-MM-dd') and studentInfo.degreeStatus='Y' ", graduateDateInDegree);
		}else{
			list = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where  isDeleted = 0 and  studentInfo.degreeStatus='Y' ");
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
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeGraduateType");
				Map<String , Object> mapdict = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				//自定义的审核状态的映射
				title.append(graduateDateInDegree+" ");						
				exportExcelService.initParmasByfile(disFile, "stuInfoWithDegree", list,mapdict);
				exportExcelService.getModelToExcel().setHeader(title+"毕业学位名单表");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			}
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, title+"毕业学位名单表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 毕业资格审核列表   
	 * 需要显示出正常注册 正常未注册 延期状态下的学生，学籍信息
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduate/audit/list.html")
	public String graduateauditList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		//String branchSchool=ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		//String major       =ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		//String classic     =ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String stuStatus   =ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));//学籍状态
		//String classId   =ExStringUtils.trimToEmpty(request.getParameter("classesid"));//班级
		//String schoolType   =ExStringUtils.trimToEmpty(request.getParameter("teachingType"));//学习形式
		//String havePhoto             = ExStringUtils.trimToEmpty(request.getParameter("havePhoto"));//是否有相片
		String stuStatusRes = stuStatus;
		model.addAttribute("studentStatus",stuStatus);
		String accountStatus = null;
		if(stuStatus.contains("a")){
			accountStatus="1";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}else if(stuStatus.contains("b")){
			accountStatus="0";
			stuStatus="11";//去掉学籍中有关帐号状态的标记
		}
		//String name        =ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		//String matriculateNoticeNo=ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		//String grade       =ExStringUtils.trimToEmpty(request.getParameter("gradeid"));//年级
		//String isReachGraYear = ExStringUtils.trimToEmpty(request.getParameter("isReachGraYear"));//达到毕业最低年限
		//String isPassEnter    = ExStringUtils.trimToEmpty(request.getParameter("isPassEnter"));//通过入学资格审核
		//String isApplyDelay   = ExStringUtils.trimToEmpty(request.getParameter("isApplyDelay"));//申请延迟毕业
		//获取毕业日期
		String graduateDate =ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));
		//毕业条件
		//String limScoreCondition = request.getParameter("limScoreCondition");//限选课毕业要求
		//String nesScoreCondition = request.getParameter("nesScoreCondition");//必修课毕业要求
		//String totalScoreCondition = request.getParameter("totalScoreCondition");//总学分毕业要求
		//String applyCondition = request.getParameter("applyCondition");//毕业申请要求
		//String enterSchCondition = request.getParameter("enterSchCondition");//入学资格要求
		//String stuStatusCondition = request.getParameter("stuStatusCondition");//学籍状态要求
		//String yearCondition = request.getParameter("yearCondition");//年限要求
		//String allPassCondition = request.getParameter("allPassCondition");//课程所有通过要求
		//String stuFeeCondition = request.getParameter("stuFeeCondition");//缴清所有费用要求
		//String scoreCondition  = request.getParameter("scoreCondition");
		//String useGraduateDate = request.getParameter("useGraduateDate");//毕业日期启用
		String fromPage = request.getParameter("fromPage");//来自页面的查询
		//获得是否使用了全选
		String checkall = ExStringUtils.trimToEmpty(request.getParameter("checkAllFlag"));//全选
		
		User currentUser = SpringSecurityHelper.getCurrentUser();
						
		//登录用户是否为教务员
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(currentUser.getOrgUnit().getUnitType())){
			//branchSchool = currentUser.getOrgUnit().getResourceid();
			model.addAttribute("brSchoolName",currentUser.getOrgUnit().getResourceid());
		}
		
		/*if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}*/

		if(ExStringUtils.isNotEmpty(stuStatus)) {
			condition.put("stuStatus", stuStatus);
		}

		if(null!=fromPage){
			condition.put("fromPage", fromPage);
		}else{
			condition.put("limScoreCondition",1);
			condition.put("nesScoreCondition",1 );
			condition.put("totalScoreCondition", 1);
			condition.put("applyCondition", 1);
			condition.put("enterSchCondition",1 );
			condition.put("stuStatusCondition", 1);
			condition.put("yearCondition",1 );
			condition.put("scoreCondition",1 );
			condition.put("allPassCondition",1);
		}
		
		String isRefresh = ExStringUtils.trimToEmpty(request.getParameter("isRefresh"));
		String pNum = ExStringUtils.trimToEmpty(request.getParameter("pNum"));
		if("1".equals(isRefresh)){
			objPage.setPageNum(Integer.parseInt(pNum));
		}
		if(null!=accountStatus){
			condition.put("accountStatus", accountStatus);
		}
		String currentYearInfo = getCurrentYear();
		condition.put("currentYearInfo", currentYearInfo);
		condition.put("isGraduateQualifer", 1);
		Page page = null;
		if(ExStringUtils.isNotEmpty(stuStatus) && ("24".equals(stuStatus) || "27".equals(stuStatus))){
			page = studentInfoService.findStudentInfoSecGraduate(condition, objPage);
		}else{
			page= studentInfoService.findStuByCondition(condition, objPage);
		}
		
		if(null!=checkall&&"1".equals(checkall)) {
			condition.put("checkAllFlag", "1");
		}else{
			condition.put("checkAllFlag", "0");
		}
		model.addAttribute("graduationQualifList", page);
		model.addAttribute("condition", condition);
		model.addAttribute("setDate",graduateDate);
		//记录页数和标记是否为修改了毕业日期刷新页面
		model.addAttribute("isRefresh",0);
		model.addAttribute("pNum", objPage.getPageNum());
		//处理组合学籍状态正常注册 正常未注册
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
		model.addAttribute("stuStatusRes", stuStatusRes);
		model.addAttribute("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());
		//处理年级-专业-班级级联查询
//		model.addAttribute("majorSelect",graduationQualifService.getGradeToMajor(grade,major,"major","major","graduateauditMajorClick()"));
//		model.addAttribute("classesSelect",graduationQualifService.getGradeToMajorToClasses(grade,major,classId,branchSchool,"graduateaudit_classid","classId"));
		return "/edu3/roll/graduationQualif/graduateaudit-list";
		
	}

	/**
	 * 刷新待毕业数据(暂废弃)
	 * 统计总学分、总必修学分等信息
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/schoolroll/graduate/qualifier/refreshAuditData.html")
	public void refreshAuditData(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();	
		//查询
		String currentYearInfo = getCurrentYear();
		List<StudentInfo> studentInfos = studentInfoService.findByHql(" from "+StudentInfo.class.getSimpleName()
				+" where isDeleted =0 "
				+" and enterAuditStatus ='Y' "
				+" and (studentStatus = '11' or studentStatus = '21')"
				+" and (to_number(replace(substr(teachingPlan.eduYear,0,3),'年',''))+to_number(grade.yearInfo.firstYear+(grade.term-1)*0.5+0.5))<=(0.5+to_number("+currentYearInfo+")) "
				+" and studentBaseInfo is not null  "
				+" and finishedCreditHour < teachingPlan.minResult "
				);
		try{
			System.out.println(studentInfos.size());
			Map<String,Object> voMap = new HashMap<String, Object>(0);
			voMap.put("refresh", 1);
			List<Map<String,Object>> results = studentExamResultsService.studentToGraduatePublishedExamResultsList(voMap);
			//为了加快执行速度 将不及格的成绩去除 提前判断 减少后面检索次数
			List<Map<String,Object>> org_results = new ArrayList<Map<String,Object>>(0);
			for (Map<String, Object> map2 : results) {
				Double score = Double.valueOf(map2.get("integratedscore").toString());
				if(score>=60){
					if(score<=100){
					org_results.add(map2);
					}else{
						if(score==2512||score==2513||score==2514||score==2515){
							org_results.add(map2);
						}
					}
				}
			}
			List<Map<String,Object>> sc     = learningJDBCService.findToGraduateStatCouseExamResults();
			List<Map<String,Object>> nc    = learningJDBCService.findToGraduateNoExamCourseResults();
			int i = 0;
			List<Map<String,Object>> list = new ArrayList<Map<String,Object>>(0);
			for (StudentInfo studentInfo : studentInfos) {
				Map<String,Object> unitMap  = new HashMap<String, Object>(0);
				Double totalScore = 0D;
				Double totalNesScore = 0D;
				System.out.println("计算了："+(i++));
				String studentid = studentInfo.getResourceid();
			
				System.out.println(studentid);
				//学生的所有的最高成绩列表
				Map<String, Object>            unitResults = new HashMap<String, Object>(0);
				for (Map<String, Object> map2 : org_results) {
					if(map2.get("studentid").equals(studentid)){
						Double scoreD  = Double.valueOf(map2.get("integratedscore").toString());
						String courseId = map2.get("courseid").toString(); 
						if(unitResults.containsKey(courseId)){
							Double maxScoreD = Double.valueOf(unitResults.get(courseId).toString());
							if(scoreD>maxScoreD){
								unitResults.put(courseId, scoreD);
							}
						}else{
							unitResults.put(courseId, scoreD);
						}
					}
				}
				//Map<String,Object> voMap = new HashMap<String, Object>(0);
				//voMap.put("studentId", studentInfo.getResourceid());
				//List<Map<String, Object>>  results= studentExamResultsService.studentPublishedExamResultsList(voMap);
				//results							  = studentExamResultsService.sortExamResultList(results);
				//统考课程成绩以及免修免考成绩 
				Map<String,Object>  statCourseRs  = new HashMap<String, Object>(); //统考课程成绩映射
				Map<String,Object> noExamCourseRs = new HashMap<String, Object>(); //免修免考成绩 映射
				//统考免考的成绩列表

				List<Map<String,Object>> scl      = new ArrayList<Map<String,Object>>(0); 
				List<Map<String,Object>> ncl      = new ArrayList<Map<String,Object>>(0);
				for (Map<String, Object> map2 : sc) {
					if(map2.get("studentid").equals(studentid)){
						Map<String,Object> tmp = new HashMap<String, Object>(0);
						tmp.put("COURSEID", map2.get("PLANCOURSEID"));
						tmp.put("scoreType", map2.get("scoreType"));
						scl.add(tmp);
					}
				}
				for (Map<String, Object> map2 : nc) {
					if(map2.get("studentid").equals(studentid)){
						Map<String,Object> tmp = new HashMap<String, Object>(0);
						tmp.put("COURSEID", map2.get("COURSEID"));
						tmp.put("checkstatus", map2.get("checkstatus"));
						ncl.add(tmp);
					}
				}
				//列表转映射
				if (null!=scl && !scl.isEmpty()) {
					for (Map<String,Object> rs1:scl) {
						statCourseRs.put(rs1.get("COURSEID").toString(),rs1);
					}
				}
				if (null!=ncl && !ncl.isEmpty()) {
					for (Map<String,Object> rs2:ncl) {
						noExamCourseRs.put(rs2.get("COURSEID").toString(), rs2);
					}
				}
				//看来teachingPlanCourse 的是计划内的 其他的成绩是计划外的。
				Set<TeachingPlanCourse> set = studentInfo.getTeachingPlan().getTeachingPlanCourses();
				//String teachingPlanId = studentInfo.getTeachingPlan().getResourceid();
				//List<StudentLearnPlan> stuPlanlist = studentLearnPlanService.getStudentLearnPlanList(studentInfo.getResourceid(),teachingPlanId);
				//该生通过的限选课的门数
				Integer limitCourseNum =0; 
				boolean isAllPassed = true;
				boolean isPassed = true;
				boolean isHas = false;
				//创建一个保存已记录计划内课程学分的列表
				List<String> courses = new ArrayList<String>(0);
				for (TeachingPlanCourse teachingPlanCourse : set) {
//				for ( StudentLearnPlan plan : stuPlanlist )	{
					String courseType = teachingPlanCourse.getCourseType();
					String courseId   = teachingPlanCourse.getCourse().getResourceid();
					courses.add(courseId);
					//必修课全部通过验证
					if("11".equals(courseType)||"thesis".equals(courseType)){
						String courseid = teachingPlanCourse.getCourse().getResourceid();
						isHas=false;
						if(noExamCourseRs.containsKey(courseid)){
							isHas = true;
							Map<String,Object> noExam = (Map<String,Object>)noExamCourseRs.get(courseid);
							//免考经过审核的以及通过的   但是成绩是免修 代修等的没确定暂时没有算成通过
							String checkStatus = null==noExam.get("checkstatus")?"":noExam.get("checkstatus").toString();
							
							if(!"1".equals(checkStatus)){
								isPassed =false ; 
							} else{
								//加总学分
								Double creditHour =teachingPlanCourse.getCreditHour();
								totalNesScore+= creditHour;
								totalScore+=creditHour;
							}
						}else if(statCourseRs.containsKey(courseid)){
							isHas = true;
							Map<String,Object> noExam = (Map<String,Object>)statCourseRs.get(courseid);
							//统考的成绩类型有2 和 PASS 认为PASS才是通过的
							String scoreType = null==noExam.get("scoretype")?"":noExam.get("scoretype").toString();
							if(!"PASS".equals(scoreType)){
								isPassed =false ; 
							}else{
								//加总学分
								Double creditHour =teachingPlanCourse.getCreditHour();
								totalNesScore+= creditHour;
								totalScore+=creditHour;
							}
						}else if(unitResults.containsKey(courseid)){
							isHas=true;
//							成绩已通过之前的判断
//							if(Double.valueOf(unitResults.get(courseid).toString())<60){
//								isPassed=false;
//							}else{
								//加总学分
								Double creditHour =teachingPlanCourse.getCreditHour();
								totalNesScore+= creditHour;
								totalScore+=creditHour;
//							}
						}
					}else
					//限选课通过累计
					if("33".equals(courseType)){
						String courseid = teachingPlanCourse.getCourse().getResourceid();
						if(unitResults.containsKey(courseid)) {
							if(Float.valueOf(unitResults.get(courseid).toString())>=60){
									limitCourseNum++;
									//计算总学分
									totalScore+=teachingPlanCourse.getCreditHour();
							}
						}
					}else{
						String courseid = teachingPlanCourse.getCourse().getResourceid();
						if(unitResults.containsKey(courseid)) {
							if(Float.valueOf(unitResults.get(courseid).toString())>=60){
								//计算总学分
								totalScore+=teachingPlanCourse.getCreditHour();
							}
						}
					}
					if(!isPassed||!isHas){
						isAllPassed=false;
					}
				}
				Set<String> unitResultsSet = unitResults.keySet();
				for (String str  : unitResultsSet) {
					if(!courses.contains(str)){
						Double creditHour = courseService.get(str).getPlanoutCreditHour();
						totalScore += creditHour;
					}
				}
//				studentInfo.setFinishedCreditHour(Math.round(totalScore));
//				studentInfo.setFinishedOptionalCourseNum(limitCourseNum);
//				if(isAllPassed){
//					studentInfo.setFinishedNecessCreditHour(Math.round(totalNesScore));
//				}else{
//					studentInfo.setFinishedNecessCreditHour(Math.round(totalNesScore));
//				}				
//				studentInfoService.update(studentInfo);
				unitMap.put("id", studentid);
				unitMap.put("finishedcredithour", Math.round(totalNesScore));
				unitMap.put("finishednescredithour", Math.round(totalScore));
				
				
			}
			map.put("statusCode", 200);
			map.put("message", "刷新操作成功！");	
		}catch(Exception e){
			logger.error("刷新操作失败！:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "刷新操作失败！:<br/>"+e.getLocalizedMessage());	
		}
		
//		map.put("hasError", hasError);
//		map.put("message", messagestr);	
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 验证选择学生的学籍状态(暂废弃)
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*@RequestMapping("/edu3/schoolroll/graduate/audit/validateStus.html")
	public void validateStus(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String studyStatus =ExStringUtils.trimToEmpty(request.getParameter("studyStatus"));
		if("graduate".equals(studyStatus)) studyStatus = "16";//当为设置毕业时间时，需要验证是不是均为“毕业状态”
		if("atSchool".equals(studyStatus)) studyStatus = "11";//当为审核毕业资格时，需要验证是不是均为"在读状态"
		String stus =  ExStringUtils.trimToEmpty(request.getParameter("stus"));
		String[] studentResourceids = stus.split(",");
		Map<String,Object> result = new HashMap<String, Object>(0); 
		result.put("isLegal", true);
		for (int i = 0; i < studentResourceids.length; i++) {
			StudentInfo studentInfoForValidate = studentInfoService.findUniqueByProperty("resourceid", studentResourceids[i]);
			if(!studyStatus.equals(studentInfoForValidate.getStudentStatus())){
				result.put("isLegal", false);
			}
		}
		renderJson(response,JsonUtils.mapToJson(result));
	}
	*/
	/** 
	 * 打开毕业时间选择界面(暂废弃)
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	/*@RequestMapping("/edu3/schoolroll/graduate/audit/setGraduateDate.html")
	public String setGraduateDate(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
		String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
		model.addAttribute("stus", stus);
		return "/edu3/roll/graduationQualif/setGraduateDate";
	}
	*/
	/**
	 * 对单个或批量设置 毕业时间(暂废弃)
	 * 毕业日期 应当用edu_teach_graduatedata的数据
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*@RequestMapping("/edu3/schoolroll/graduate/audit/batchSetGraduateDate.html")
	public void batchSetGraduateDate(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		//得到学生id集合与待设置成的毕业时间
		String stus =  ExStringUtils.trimToEmpty(request.getParameter("stus"));
		String graduateDate =  ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));
		String[] studentResourceids = stus.split(",");
		boolean isUpdate = false;
		try{
			for (int i = 0; i < studentResourceids.length; i++) {
				List<GraduateData> graduateDatas = graduateDataService.findByHql(" from " +GraduateData.class.getSimpleName()+" where studentInfo.resourceid = ?" ,studentResourceids[i]);
				//有些学生已经是毕业状态但是在毕业生库中没有他的信息。
				if(graduateDatas.size()==0){
					//说明已为毕业状态下的学生在毕业生库中 没有毕业信息。所以需要创建一个graduateData
					GraduateData graduateData = new GraduateData();
					if("".equals(graduateDate)){
						graduateData.setGraduateDate(null);//这个会失败，抛异常
					}else{
						Date tmp = ExDateUtils.parseDate(graduateDate, ExDateUtils.PATTREN_DATE);
						graduateData.setGraduateDate(tmp);
					}
					StudentInfo studentInfo = studentInfoService.get(studentResourceids[i]);
					graduateData.setStudentInfo(studentInfo);
					//毕业生库创建
					graduateDataService.save(graduateData);
				}else{
					//说明毕业生库有该学生的信息 只需要更新信息显示即可
					GraduateData graduateData = graduateDatas.get(0);
					if("".equals(graduateDate)){
						graduateData.setGraduateDate(null);//这个会失败的，抛异常
					}else{
						Date tmp = ExDateUtils.parseDate(graduateDate, ExDateUtils.PATTREN_DATE);
						graduateData.setGraduateDate(tmp);
					}
					//毕业生库更新
					graduateDataService.update(graduateData);
				}
			}
			isUpdate=true;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		Map<String,Object> map = new HashMap<String, Object>(0);
		map.put("isUpdate", isUpdate);
		renderJson(response, JsonUtils.mapToJson(map));
	}*/
	
	/**
	 * 在毕业审核之前   得到学生的学籍信息。（暂废弃）
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	/*@RequestMapping("/edu3/roll/graduateaudit/auditGraduate.html")
	public String beforeViaGraduate(Page objPage,String stus,HttpServletRequest request,ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>(0);
		stus = stus.replaceAll("'", "");
		String[] stu = stus.split(",");
		StringBuffer students = new StringBuffer();
		for (String str : stu) {
			students.append("'"+str+"',");
		}
		String studentCondition = students.toString().substring(0,students.length()-1);
		condition.put("stus", studentCondition);
//		因为在之前的毕业资格审查的显示时已经对学生进行了筛选 所以不需要再次进行筛选
		Page page = null;
		if(objPage.getPageSize()==20){//第一次打开审核页面，初始化页面对象
			page=new Page();
			page.setPageSize(1);
			page.setPageNum(1);
			String isRefresh = ExStringUtils.trimToEmpty(request.getParameter("isRefreshForm"));
			if("1".equals(isRefresh)){
				String currentPageNum = ExStringUtils.trimToEmpty(request.getParameter("currentPageNumber"));
				if(ExStringUtils.isNotEmpty(currentPageNum))
					page.setPageNum(Integer.parseInt(currentPageNum));
			}
			page = studentInfoService.findStuByCondition(condition, page);
		}else{//使用分页打开审核页面，这里需要处理当用户输入大于总页
			if(stu.length<objPage.getPageNum()){//如果输入页数大于总页数，跳转至最后一页
				objPage.setPageNum(stu.length);
			}
			page = studentInfoService.findStuByCondition(condition, objPage);
		}
		model.addAttribute("pageNum",objPage.getPageNum());
		model.addAttribute("stus",stus);
		model.addAttribute("studentInfos",page);
		
		//来自毕、位审核list页面的参数传递
		String branchSchool=ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String major=ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic=ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String stuStatus=ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
		String name=ExStringUtils.trimToEmpty(request.getParameter("name"));
		String matriculateNoticeNo=ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
		String grade=ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String pNum =ExStringUtils.trimToEmpty(request.getParameter("pNum"));
		//全局设定的毕业日期
		String graduateDateG = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));
		//获得局部设定的毕业日期
		String graduateDateL = ExStringUtils.trimToEmpty(request.getParameter("graduateDateL"));
		//form页面要用到的毕业日期
		String graduateDate = "";
		//优先使用局部设置的毕业日期
		if(!"".equals(graduateDateL)){
		graduateDate = graduateDateL;
		}else if(!"".equals(graduateDateG)){
		graduateDate = graduateDateG;
		}
		condition.put("graduateDateL",graduateDate);
		condition.put("graduateDate",graduateDateG);
		condition.put("branchSchool", branchSchool);
		condition.put("major", major);
		condition.put("classic", classic);
		condition.put("stuStatus", stuStatus);
		condition.put("name", name);
		condition.put("matriculateNoticeNo", matriculateNoticeNo);
		condition.put("grade", grade);
		condition.put("pNum",pNum);
		
		
		model.addAttribute("condition", condition);
		
		
		//以上是毕业审核部分
		//以下是学位审核部分
		StudentInfo studentInfo = (StudentInfo)page.getResult().get(0);//获得待审核的学生
		String classicId =studentInfo.getClassic().getResourceid();
		//本科的学生才参与学位资格审核
		                                                        
		if("EB43B13C75A9F518E030007F0100743D".equals(classicId)||"EB43B13C75AAF518E030007F0100743D".equals(classicId)){
			String id = studentInfo.getResourceid();
			//学位审核要求
			//1、十个主干课成总分为700
			//2、毕业论文（设计）良好(含)
			//3、通过学位课程考试:1门外国语、1门专业基础课，2门专业课 专业基础课和专业课卷面成绩合格方视为通过
			Map<String,Object> voMap = new HashMap<String, Object>(0);
			voMap.put("studentId", id);
			voMap.put("thisYear", this.getCurrentYear());
			List<Map<String,Object>> results = studentExamResultsService.studentToGraduatePublishedExamResultsList(voMap);
			Map<String,Object> unitResults = new HashMap<String, Object>(0);
			Map<String,Object> unitResults2 = new HashMap<String, Object>(0);
			for (Map<String, Object> map2 : results) {
				if(map2.get("integratedscore")==null) continue;
				Double scoreD  = Double.valueOf(map2.get("integratedscore").toString());
				String courseId = map2.get("courseid").toString(); 
				if(unitResults.containsKey(courseId)){
					Double maxScoreD = Double.valueOf(unitResults.get(courseId).toString());
					if(scoreD>maxScoreD){
						unitResults.put(courseId, scoreD);
					}
				}else{
					unitResults.put(courseId, scoreD);
				}
			}
			for (Map<String, Object> map2 : results) {
				if(map2.get("writtenscore")==null) continue;
				Double scoreD  = Double.valueOf(map2.get("writtenscore").toString());
				String courseId = map2.get("courseid").toString(); 
				if(unitResults2.containsKey(courseId)){
					Double maxScoreD = Double.valueOf(unitResults2.get(courseId).toString());
					if(scoreD>maxScoreD){
						unitResults2.put(courseId, scoreD);
					}
				}else{
					unitResults2.put(courseId, scoreD);
				}
			}
			Set<TeachingPlanCourse> set = studentInfo.getTeachingPlan().getTeachingPlanCourses();
			//主干课总分
			Float sumScoreMainCourse = 0F;
			//主干课门数
			Integer sumNumMainCourse = 0;
			//专业课通过门数
			Integer majorNum = 0;
			//主干课通过情况
			boolean isPass = true;
			//专业课通过情况
			boolean majorPass = false;
			//专业基础课通过情况 
			boolean majorBasicPass = false;
			 //毕业设计通过情况
			boolean gdPass = false;
			//外国语通过情况
			boolean flPass = false;
			for (TeachingPlanCourse teachingPlanCourse : set) {
				String courseNature = teachingPlanCourse.getCourseNature();
				String courseId     = teachingPlanCourse.getCourse().getResourceid();
				String courseType = teachingPlanCourse.getCourseType();
				if("Y".equals(teachingPlanCourse.getIsMainCourse())||"1".equals(teachingPlanCourse.getIsMainCourse())){
					sumNumMainCourse++;
					if(unitResults.containsKey(courseId)){
						Float f = Float.valueOf(unitResults.get(courseId).toString());
						sumScoreMainCourse+=f;
					}else{
						isPass=false;//说明主干课没有成绩
						break;
					}
				}
				//专业基础课 1500 学科基础课 1200
				if("1500".equals(courseNature)){
					if(unitResults2.containsKey(courseId)){
						Float f = Float.valueOf(unitResults2.get(courseId).toString());
						if(f>=60){
							majorBasicPass = true;
						}
					}
				}
				//专业课
				if("1300".equals(courseNature)){
					if(unitResults2.containsKey(courseId)){
						Float f = Float.valueOf(unitResults2.get(courseId).toString());
						if(f>=60){
							majorNum++;
							if(majorNum==2){
								majorPass = true;
							}
						}
					}
				}
				//毕业设计  ？ 毕业论文多少分是良？ score==2514||score==2515
				if("2020".equals(courseNature)||"thesis".equals(courseType)){
					if(unitResults.containsKey(courseId)){
						Float f = Float.valueOf(unitResults.get(courseId).toString());
						if(f==2514||f==2515){
							gdPass = true;
						}
					}
				}
//				//外语 是哪些课程？ 英语学位
//				if("ff8080811dcdd9d1011df01ee4264e9a".equals(courseId)){
//					if(unitResults2.containsKey(courseId)){
//						Float f = Float.valueOf(unitResults2.get(courseId).toString());
//						if(f>=60){
//							flPass = true;
//						}
//					}
//				}
				
			}
			//外语 是哪些课程？ 英语学位
			
			if(unitResults2.containsKey("FBECB1BCE7C8171EE030007F01001614")){
				Float f = Float.valueOf(unitResults2.get("FBECB1BCE7C8171EE030007F01001614").toString());
				if(f>=60){
					flPass = true;
				}
			}
			
			if(gdPass) model.addAttribute("gdPass", "<font color='blue'>通过</font>"); else model.addAttribute("gdPass","<font color='red'>不通过</font>");
			StringBuffer majorPassMsg = new StringBuffer();
			if(majorBasicPass){
				majorPassMsg.append("专业基础课<font color='blue'>通过</font> ");
			}else{
				majorPassMsg.append("专业基础课<font color='red'>未通过 </font>");
			}
			if(majorPass){
				majorPassMsg.append("专业课<font color='blue'>通过</font> ");
			}else{
				majorPassMsg.append("专业课<font color='red'>未通过</font> ");
			}
			if(flPass){
				majorPassMsg.append("外国语课<font color='blue'>通过</font> ");
			}else{
				majorPassMsg.append("外国语<font color='red'>未通过 </font>");
			}
			model.addAttribute("majorPass", majorPassMsg.toString());
			
			if(isPass&&(sumScoreMainCourse/sumNumMainCourse>=70)) {
				model.addAttribute("isPass", "<font color='blue'>完全</font>");
				model.addAttribute("mainCourseAvg", sumScoreMainCourse/sumNumMainCourse);
			}else{
				model.addAttribute("isPass","<font color='red'>不完全</font>");
				model.addAttribute("mainCourseAvg", sumScoreMainCourse/sumNumMainCourse);
			}
			model.addAttribute("auditDegree", "1");
			
		}
		return "/edu3/roll/graduationQualif/graduateaudit-form";		
//		boolean isApplyGraduate=false;
//		if(ExStringUtils.isNotEmpty(resourceid)){
//			StudentInfo studentInfo = studentInfoService.get(resourceid);
//			//获取的实际获取的总学分  scores[]0]课程总学分  scores[1] 毕业论文分数
//			int [] scores={0,0};
//			// scores=graduationQualifService.getStudenScore(studentInfo.getResourceid(),studentInfo.getTeachingPlan().getResourceid());
//			//SET这两个分数
//			model.addAttribute("studentInfo", studentInfo);
//			//实际获取的课程分数
//			model.addAttribute("realityIntegratedScore",Integer.toString(scores[0])); 
//			//实际获取的毕业论文分数
//			model.addAttribute("realityThesisScore",Integer.toString(scores[1]));
//			
//			//检查该学生是否申请学位
//			if("Y".equals(studentInfo.getIsApplyGraduate())){
//				//从字典中获取学位条件
//				List<Dictionary> dictionList = CacheAppManager.getChildren("DegreeCondition");
//				model.addAttribute("degreeList", dictionList);
//				model.addAttribute("isApplyGraduate",true);
//			}
//			
//		}
	}
	*/
	/**
	 * 单个毕业审核(暂废弃)
	 * @param stus
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/roll/graduateaudit/viaGraduateBySingle.html")
	public void viaSingleGraduate(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String id = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		//每一个学生因为在毕业审核之前已经对其的 年级要求和学分要求 做出了审核
		StudentInfo studentInfo = studentInfoService.get(id);//获得每个待审核的学生			
		//返回信息
		String messagestr="";
		boolean hasError = false;
		try{
			//得到毕业时间
			String graduateDateInAudit = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));
			//更新学籍表中的该生的学籍状态为【准毕业】毕业
			studentInfo.setStudentStatus("22");
			if(!"EB43B13C75ABF518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())){
				studentInfo.setDegreeStatus(Constants.BOOLEAN_WAIT);
			}
			studentInfoService.update(studentInfo);//执行更新操作
			//对毕业生库的更新
			try{
				List<GraduateData> graduateDatas = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where studentInfo.resourceid=? ",studentInfo.getResourceid() );
				GraduateData tmp_graduateData ;
				if(graduateDatas.size()>0){
					tmp_graduateData = graduateDatas.get(0);
				}else{
					tmp_graduateData = new GraduateData();
				}
				tmp_graduateData.setPublishStatus(Constants.BOOLEAN_WAIT);
				tmp_graduateData.setStudentInfo(studentInfo);
				tmp_graduateData.setDegreeName(studentInfo.getBranchSchool().getUnitName());
				tmp_graduateData.setDegreeNum("");//根据未知编码规则
				//关于毕业证明编号，如果之前存在毕业库中且对应的毕业证明与当前的毕业时间吻合，不重新生成
				String graduateDate = "";
				if(graduateDateInAudit.contains("-")){
					graduateDate = graduateDateInAudit.split("-")[0];	
				}else{
					messagestr += studentInfo.getStudentName()+"毕业日期丢失.";
					throw new Exception();
				}
				boolean isNeedNew = true;
				String graduateDiploma ="";
				if(graduateDatas.size()>0){
					graduateDiploma = graduateDatas.get(0).getDiplomaNum();
					if(graduateDate.equals(graduateDiploma.substring(6, 10))){
						isNeedNew = false;
					}
				}
				if(isNeedNew){
					//生成新毕业证书号
					StringBuffer diplomaNum = new StringBuffer();
					//学校代码+办学方式
					diplomaNum.append("105717");
					//毕业日期
					diplomaNum.append(graduateDate);
					//层次
					if("EB43B13C75ABF518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())){
						diplomaNum.append("05");
					}else if("EB43B13C75A9F518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())||"EB43B13C75AAF518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())){
						diplomaNum.append("06");
					}else{
						messagestr += studentInfo.getStudentName()+"层次不合法.";
						throw new Exception();
					}
					//2bit学习中心代号
					String unitCode = Integer.valueOf(studentInfo.getBranchSchool().getShoworder()).toString();
					if (unitCode.equals("0")) {
						messagestr += studentInfo.getStudentName()+"学习中心无编号.";
						throw new Exception();
					}
					if(unitCode.length()==1){
						unitCode = "0"+unitCode;
					}
					diplomaNum.append(unitCode);
					//4bit流水号
					diplomaNum.append(getDiplomaNumSuffix(diplomaNum.toString()));
					tmp_graduateData.setDiplomaNum(diplomaNum.toString());//已知编码规则
				}else{
					//使用旧的毕业证书号
					tmp_graduateData.setDiplomaNum(graduateDiploma);
				}
				//tmp_graduateData.setEntranceDate(null);//未找到入学日期
				//得到所在年级的学期
				String term = studentInfo.getGrade().getTerm();
				if("1".equals(term)){
				//如果是上学期
					tmp_graduateData.setEntranceDate(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm());
				}else{
				//如果是下学期
					tmp_graduateData.setEntranceDate(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm());
				}
				//设置毕业时间
				if("".equals(graduateDateInAudit)){
					tmp_graduateData.setGraduateDate(null);
				}else{
					Date tmp = ExDateUtils.parseDate(graduateDateInAudit, ExDateUtils.PATTREN_DATE);
					tmp_graduateData.setGraduateDate(tmp);
				}
				tmp_graduateData.setGraduateType("1");
				if(graduateDatas.size()>0){
					tmp_graduateData.setIsDeleted(0);
					graduateDataService.update(tmp_graduateData);
				}else{
					graduateDataService.save(tmp_graduateData);
				}
					messagestr += studentInfo.getStudentName()+"审核成功.</br>";
			}catch(Exception e){
				//如果出现graduateData的更新或创建异常 恢复已修改的学籍状态
				studentInfo.setStudentStatus("11");
				studentInfo.setDegreeStatus(null);
				studentInfoService.update(studentInfo);
			}
		}catch(Exception e){
			logger.error("审核操作失败！:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核操作失败！:<br/>"+e.getLocalizedMessage());	
		}
		map.put("statusCode", 200);
		map.put("hasError", hasError);
		map.put("message", messagestr);	
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	
	/**
	 * 得到待审核的条数(毕业)
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/getGraduateAuditNum.html")
	public void getDegreeAuditNum(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model) throws WebException
	{
		int total = 0;
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		Map<String, Object> condition = new HashMap<String, Object>(0);
		List<Map<String,Object>> studentinfos = new ArrayList<Map<String,Object>>(0);
		Map<String,Object> map =new HashMap<String,Object>(0);
		//判断毕业审核时间
		Date curTime                            = ExDateUtils.getCurrentDateTime();
		Date cachTime                           = null;
		String codeTime = CacheAppManager.getSysConfigurationByCode("teaching.graduateAudit.deadline").getParamValue();
		try {
			curTime                             = ExDateUtils.formatDate(curTime,ExDateUtils.PATTREN_DATE);//当前系统日期
			cachTime                            = ExDateUtils.parseDate(codeTime,ExDateUtils.PATTREN_DATE);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		User user = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool = false;
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			isBrschool = true;
		}
		if(cachTime.after(curTime) || !isBrschool){
			if("1".equals(isSelectAll)){//如果是全选是按照条件查询得到待审核的名单
				String branchSchool =ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
				String major		=ExStringUtils.trimToEmpty(request.getParameter("major"));
				String classic		=ExStringUtils.trimToEmpty(request.getParameter("classic"));
				String stuStatus	=ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
				String name		    =ExStringUtils.trimToEmpty(request.getParameter("name"));
				String matriculateNoticeNo	=ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
				String grade 		=ExStringUtils.trimToEmpty(request.getParameter("grade"));
				String isReachGraYear = ExStringUtils.trimToEmpty(request.getParameter("isReachGraYear"));//达到毕业最低年限
				String isPassEnter    = ExStringUtils.trimToEmpty(request.getParameter("isPassEnter"));//通过入学资格审核
				String isApplyDelay   = ExStringUtils.trimToEmpty(request.getParameter("isApplyDelay"));//申请延迟毕业
				if(ExStringUtils.isNotEmpty(branchSchool)) {
					condition.put("branchSchool", branchSchool);
				}
				if(ExStringUtils.isNotEmpty(major)) {
					condition.put("major", major);
				}
				if(ExStringUtils.isNotEmpty(classic)) {
					condition.put("classic", classic);
				}
				String accountStatus = "";
				if(stuStatus.contains("a")){
					accountStatus="1";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}else if(stuStatus.contains("b")){
					accountStatus="0";
					stuStatus="11";//去掉学籍中有关帐号状态的标记
				}
				if(ExStringUtils.isNotEmpty(stuStatus)){
					condition.put("stuStatus", stuStatus);
				}
				if(ExStringUtils.isNotEmpty(accountStatus)){
					condition.put("accoutstatus", accountStatus);
				}
				if(ExStringUtils.isNotEmpty(name)) {
					condition.put("name", name);
				}
				if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
					condition.put("matriculateNoticeNo", matriculateNoticeNo);
				}
				if(ExStringUtils.isNotEmpty(grade)) {
					condition.put("grade", grade);
				}
				if(ExStringUtils.isNotEmpty(isReachGraYear)) {
					condition.put("isReachGraYear", isReachGraYear);
				}
				if(ExStringUtils.isNotEmpty(isPassEnter)) {
					condition.put("isPassEnter", isPassEnter);
				}
				if(ExStringUtils.isNotEmpty(isApplyDelay)) {
					condition.put("isApplyDelay", isApplyDelay);
				}
				String currentYearInfo = getCurrentYear();
				condition.put("currentYearInfo", currentYearInfo);
				List<Map<String,Object>> infos = studentInfoChangeJDBCService.getNumForGraduateAudit(condition);		
				total = Integer.valueOf(infos.get(0).get("total").toString());
				
			}else{//如果不是使用多选，则按传入的id值确定待审核的学生名单
				String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
				String[] resourceids = stus.split(",");
				total = resourceids.length;
			}
			map.put("total", total);
		}else {
			if(cachTime==null){
				map.put("info", "尚未添加毕业审核的截止日期，请联系系统管理员！");
			}else {
				Calendar c = Calendar.getInstance();
				c.setTime(cachTime);
				map.put("info", "毕业审核截止日期为"+codeTime+"，请联系系统管理员！");
			}
			
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 毕业/结业审核前计算学分
	 * @param stus 学籍id
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/graduation_calculateTotalCreditHour.html")
	public void graduationCalculateTotalCreditHour(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage) throws WebException{
		String[] resourceids = stus.split(",");
		Map<String,Object> map =new HashMap<String,Object>(0);
		String msg = "";
		List<StudentInfo> liststu = new ArrayList<StudentInfo>();
		try {
			if(null != resourceids && resourceids.length > 0){
				for (String id:resourceids) {
					try {
						liststu.add(studentInfoService.get(id));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				msg = studentExamResultsService.graduationCalculateTotalCreditHour(liststu);
				map.put("statusCode",200);
			}else{
				msg = "没有可计算学分的学生！";
				map.put("statusCode",300);
			}
		} catch (Exception e) {
			msg = "学分计算失败;";
			map.put("statusCode",400);
		}
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "3",UserOperationLogs.UPDATE, "毕业生库：修正分数:resourceids"+resourceids);
		map.put("msg",msg);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 毕业审核(最新)
	 * @param stus
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/roll/graduateaudit/viaGraduate.html")
	public String viaGraduate(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage) throws Exception{
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		String doAudit     = ExStringUtils.trimToEmpty(request.getParameter("doAudit"));
		Map<String, Object> condition = new HashMap<String, Object>(0);
		String thisYear = this.getCurrentYear();
		List<Map<String,Object>> studentinfos = new ArrayList<Map<String,Object>>(0);
		Date auditDate = new Date();
		User user = SpringSecurityHelper.getCurrentUser();//记录审核者
		if("1".equals(isSelectAll)){//如果是全选是按照条件查询得到待审核的名单
			String branchSchool =ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String major		=ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classic		=ExStringUtils.trimToEmpty(request.getParameter("classic"));
			String stuStatus	=ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
			String name		    =ExStringUtils.trimToEmpty(request.getParameter("name"));
			String matriculateNoticeNo	=ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
			String grade 		=ExStringUtils.trimToEmpty(request.getParameter("grade"));
			String isReachGraYear = ExStringUtils.trimToEmpty(request.getParameter("isReachGraYear"));//达到毕业最低年限
			String isPassEnter    = ExStringUtils.trimToEmpty(request.getParameter("isPassEnter"));//通过入学资格审核
			String isApplyDelay   = ExStringUtils.trimToEmpty(request.getParameter("isApplyDelay"));//申请延迟毕业
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			String accountStatus = "";
			if(stuStatus.contains("a")){
				accountStatus="1";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}else if(stuStatus.contains("b")){
				accountStatus="0";
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}
			if(ExStringUtils.isNotEmpty(stuStatus)){
				condition.put("stuStatus", stuStatus);
			}
			if(ExStringUtils.isNotEmpty(accountStatus)){
				condition.put("accoutstatus", accountStatus);
			}
			if(ExStringUtils.isNotEmpty(name)) {
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
				condition.put("matriculateNoticeNo", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			if(ExStringUtils.isNotEmpty(isReachGraYear)) {
				condition.put("isReachGraYear", isReachGraYear);
			}
			if(ExStringUtils.isNotEmpty(isPassEnter)) {
				condition.put("isPassEnter", isPassEnter);
			}
			if(ExStringUtils.isNotEmpty(isApplyDelay)) {
				condition.put("isApplyDelay", isApplyDelay);
			}
			String currentYearInfo = getCurrentYear();
			condition.put("currentYearInfo", currentYearInfo);
			condition.put("isGraduateQualifer", 1);
			//需要过滤出毕业的学籍状态
			condition.put("isSelectAll", "1");
			studentinfos = studentInfoChangeJDBCService.getStudentInfoForGraduateAudit(condition);
		}else{//如果不是使用多选，则按传入的id值确定待审核的学生名单
			Map<String,Object> voMap = new HashMap<String, Object>(0);
			String[] resourceids = stus.split(",");
			StringBuffer stusCon = new StringBuffer(); 
			for (String resourceid : resourceids) {
				stusCon.append("'"+resourceid+"',");
			}
			voMap.put("stus", stusCon.deleteCharAt(stusCon.length()-1));
			String stuStatus	=ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
			
			if(stuStatus.contains("a")){
				
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}else if(stuStatus.contains("b")){
				
				stuStatus="11";//去掉学籍中有关帐号状态的标记
			}
			if(ExStringUtils.isNotEmpty(stuStatus)){
				voMap.put("stuStatus", stuStatus);
			}
			studentinfos = studentInfoChangeJDBCService.getStudentInfoForGraduateAudit(voMap);
		}
		String limScoreCondition = request.getParameter("limScoreCondition");//限选课毕业要求
		String nesScoreCondition = request.getParameter("nesScoreCondition");//必修课毕业要求
		String totalScoreCondition = request.getParameter("totalScoreCondition");//总学分毕业要求
		String applyCondition = request.getParameter("applyCondition");//毕业申请要求
		String enterSchCondition = request.getParameter("enterSchCondition");//入学资格要求
		//String stuStatusCondition = request.getParameter("stuStatusCondition");//学籍状态要求
		String yearCondition = request.getParameter("yearCondition");//年限要求
		String allPassCondition = request.getParameter("allPassCondition");//所有课程及格要求(>=60分)
		String stuFeeCondition = request.getParameter("stuFeeCondition");//缴清所有费用
		String practiceCourseCondition = request.getParameter("practiceCourseCondition");//毕业实习通过
		String practiceMaterialsCondition = request.getParameter("practiceMaterialsCondition");//毕业实习材料要求
		String studentPhotoCondition = request.getParameter("studentPhotoCondition");//学籍照片
		String juniorCheckCondition = request.getParameter("juniorCheckCondition");//专科清查
		if(null!=limScoreCondition) {
			condition.put("limScoreCondition", limScoreCondition);
		}
		if(null!=nesScoreCondition) {
			condition.put("nesScoreCondition", nesScoreCondition);
		}
		if(null!=totalScoreCondition) {
			condition.put("totalScoreCondition", totalScoreCondition);
		}
		if(null!=applyCondition) {
			condition.put("applyCondition", applyCondition);
		}
		if(null!=enterSchCondition) {
			condition.put("enterSchCondition", enterSchCondition);
		}
		//if(null!=stuStatusCondition)condition.put("stuStatusCondition", stuStatusCondition);
		if(null!=yearCondition) {
			condition.put("yearCondition", yearCondition);
		}
		if(null!=allPassCondition) {
			condition.put("allPassCondition", allPassCondition);
		}
		if(null!=stuFeeCondition) {
			condition.put("stuFeeCondition", stuFeeCondition);
		}
		if(null!=practiceCourseCondition) {
			condition.put("practiceCourseCondition", practiceCourseCondition);
		}
		if(null!=practiceMaterialsCondition) {
			condition.put("practiceMaterialsCondition", practiceMaterialsCondition);
		}
		if(null!=studentPhotoCondition) {
			condition.put("studentPhotoCondition", studentPhotoCondition);
		}
		if(null!=juniorCheckCondition) {
			condition.put("juniorCheckCondition", juniorCheckCondition);
		}

		if(null!=stus) {
			condition.put("stus", stus);
		}
		Integer passCount = 0;
		Integer unPassCount = 0;
		Integer other_passCount = 0;
		Integer other_unPassCount = 0;
		// 学校代码
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		//新建一个审核结果的列表
		List<String[]> list = new ArrayList<String[]>(0);
		StringBuffer stuids = new StringBuffer("(");
		for (Map<String, Object> studentInfo : studentinfos) {
			String[] str  = new String[13];
			
			//新需求 （毕业）：
			//1.毕业年限要求：达到毕业年限 可提前半年
			//2.学籍要求： 学资格审核通过的在籍学生
			//3.必修学分要求：达到必修学分
			//4.毕业总学分要求：达到毕业最低学分
			//5.课程通过要求:所有课程都通过,大于或等于60分 (广西医科大)
			//6.缴清所有费用(广西医科大)
			//7.毕业实习是否通过
			//8.提交毕业实习材料（桂林医学院）
			//9.提交学籍相片
			//10.专科清查（即入学资格审核通过）
			
			//（结业）
			//a.毕业年限要求：达到毕业年限 可提前半年
			//b.学籍要求： 学资格审核通过的在籍学生
			//c.达到结业最低学分
			
			//要求,毕业通过,结业就通过
			StringBuffer unAuditReason  = new StringBuffer(); //记录毕业审核信息
			StringBuffer theGraduationReason  = new StringBuffer();
			String studentStatus = null!=studentInfo.get("studentstatus")?studentInfo.get("studentstatus").toString():"";
			if("16".equals(studentStatus)){//如果这个学生在操作此功能的时候已经毕业了则不必进行一下操作了
				str[0]= null!=studentInfo.get("resourceid")?studentInfo.get("resourceid").toString():"";
				str[5]= null!=studentInfo.get("studyno")?studentInfo.get("studyno").toString():"";
				str[1]= null!=studentInfo.get("studentname")?studentInfo.get("studentname").toString():"";
				str[2]="1";
				str[3]="<font color='blue'>已确定毕业;</font>";
				str[8]="0";
				str[9]="<font color='blue'>该学生已确定毕业无需再进行结业审核;</font>";
				list.add(str);
				continue;
			}else if("24".equals(studentStatus)&&!studentInfo.get("isAllowSecGraduate").equals(Constants.BOOLEAN_YES)){
				str[0]= null!=studentInfo.get("resourceid")?studentInfo.get("resourceid").toString():"";
				str[5]= null!=studentInfo.get("studyno")?studentInfo.get("studyno").toString():"";
				str[1]= null!=studentInfo.get("studentname")?studentInfo.get("studentname").toString():"";
				str[2]="0";
				str[3]="<font color='blue'>该学生已确定结业无需再进行毕业审核;</font>";
				str[9]="<font color='blue'>已确定结业;</font>";
				str[8]="1";//结业审核通过
				list.add(str);
				continue;
			}
			
			//1和a.毕业年限要求
			Double length = Double.parseDouble(studentInfo.get("eduyear").toString().split("年")[0]);//Double.parseDouble(studentInfo.get("eduyear").toString().substring(0,3).replace("年", ""));//学制
			Double gra_s = Double.parseDouble(studentInfo.get("firstyear").toString())+(Long.parseLong(studentInfo.get("term").toString())-1L)*0.5;
			Double gra_t = Double.parseDouble(thisYear);
			Boolean str2_y = true;
			Boolean str8_y = true;
			if((gra_s+length-0.5)>gra_t){
				if("true".equals(yearCondition)) {
					unAuditReason.append("<font color='red'>1.毕业年限不符合毕业要求;</font>");
					theGraduationReason.append("<font color='red'>1.毕业年限不符合结业要求;</font>");
					str2_y = false;
					str8_y = false;
				}
			}else{
				if("true".equals(yearCondition)) {
					unAuditReason.append("<font color='blue'>1.毕业年限符合毕业要求;</font>");
					theGraduationReason.append("<font color='blue'>1.毕业年限符合结业要求;</font>");
				}
			}
			
			//2和b.学籍要求
			//学籍状态为11、24但isAllowSecGraduate = Y 的都允许进行毕业审核 20160722 lion
			String sta_s = null!=studentInfo.get("studentStatus")?studentInfo.get("studentStatus").toString():"";//学籍状态
			String ent_s = null!=studentInfo.get("enterAuditStatus")?studentInfo.get("enterAuditStatus").toString():"";//入学资格
			boolean acc = true;
			boolean statusflag = false;//学籍状态：24但isAllowSecGraduate = Y 
			if(studentInfo.get("isAllowSecGraduate")!=null){
				if("Y".equals(studentInfo.get("isAllowSecGraduate")) && "24".equals(studentStatus)) {
					statusflag = true;
				}
			}
			if(!"11".equals(sta_s) &&!"21".equals(sta_s) &&!"25".equals(sta_s)){
				acc = false;
			}else if(!Constants.BOOLEAN_YES.equals(ent_s)){
				if("true".equals(enterSchCondition)){
					acc = false;
				}
			}
			if(statusflag){
				acc = true;
			}
			if(acc){
				unAuditReason.append("<font color='blue'>2.学籍状态符合毕业要求;</font>");
				theGraduationReason.append("<font color='blue'>2.学籍状态符合结业要求;</font>");
			}else{
				unAuditReason.append("<font color='red'>2.学籍状态不符合毕业要求;</font>");
				theGraduationReason.append("<font color='red'>2.学籍状态不符合结业要求;</font>");
				str2_y = false;				
				str8_y = false;
			}
			
			//3.必修学分要求
			Double nes_s = null!=studentInfo.get("finishedNecessCreditHour")?Double.parseDouble(studentInfo.get("finishedNecessCreditHour").toString()):0D;//必
			Double nes_t = null!=studentInfo.get("tpmust")?Double.parseDouble(studentInfo.get("tpmust").toString()):0D;
			if(nes_s<nes_t){
				if("true".equals(nesScoreCondition)){
					unAuditReason.append("<font color='red'>3.必修学分不符合毕业要求;</font>");
					str2_y = false;
				}
			}else{
				if("true".equals(nesScoreCondition)){
					unAuditReason.append("<font color='blue'>3.必修学分符合毕业要求;</font>");
				}
			}
			
			//4.毕业最低学分
			Double tol_t = null!=studentInfo.get("minResult")?Double.valueOf(studentInfo.get("minResult").toString()):0D; //最低毕业学分
			Double tol_s = null!=studentInfo.get("finishedCreditHour")?Double.valueOf(studentInfo.get("finishedCreditHour").toString()):0D;//总	
			if(tol_s<tol_t){
				if("true".equals(totalScoreCondition)){
					unAuditReason.append("<font color='red'>4.毕业最低学分不符合毕业要求;</font>");
					str2_y = false;
				}
			}else{
				if("true".equals(totalScoreCondition)){
					unAuditReason.append("<font color='blue'>4.毕业最低学分符合毕业要求;</font>");
				}
			}
			
			//5.所有课程是否通过,大于或等于60分(广西医科大)->1.判断是否有未及格的,2.是否有为录入的
			List<StudentExamResultsVo> listvo = studentExamResultsService.studentExamResultsList(studentInfoService.get((String)studentInfo.get("resourceid")),null);
			if("true".equals(allPassCondition)){
				// 未录入成绩情况
				int noExamResult = 0;
				Map<String, Object> conditionMap = new HashMap<String, Object>();
				conditionMap.put("studentId", (String)studentInfo.get("resourceid"));
				conditionMap.put("allCourse", "Y");
				conditionMap.put("schoolCode", schoolCode);
				if (ExStringUtils.isNotBlank(practiceCourseCondition)) {//桂林医，排除毕业实习类型课程
					conditionMap.put("courseNature_not","3003");
				}
				List<Map<String, Object>> noExamResultList = studentExamResultsService.findNoExamResultCourse(conditionMap);
				noExamResult = noExamResultList.size();
				//不及格情况
				int noPassCount = 0;
				//成绩List中最后一条为已修学分信息
				if (null!=listvo&&listvo.size()>0) {
					noPassCount = getNoPassCount(listvo);
				}
				
				if(noPassCount>0 || noExamResult>0){
					unAuditReason.append("<font color='red'>5.所有课程成绩通过不符合毕业要求;</font>");
					str2_y = false;
				}else{
					unAuditReason.append("<font color='blue'>5.所有课程成绩通过符合毕业要求;</font>");
				}	
			}
			
			//6.缴清所有费用
			//chargeStatus==1
			if("true".equals(stuFeeCondition)){
				Map<String, Object> con = new HashMap<String, Object>();
				con.put("studentInfoId",studentInfo.get("resourceid"));
				List<StudentPayment> feeList =  studentPaymentService.findStudentPaymentByCondition(con);
				if(feeList.size()<=0){
					unAuditReason.append("<font color='red'>6.没有缴费记录信息,将无法确认毕业/结业;</font>");
					str2_y = false;
				}else{
					boolean flag = true;
					for(StudentPayment fee:feeList){
						if("1".equals(fee.getChargeStatus())){
							continue;
						}else{
							flag = false;
							break;
						}
					}
					if(flag){
						unAuditReason.append("<font color='blue'>6.已缴清费用;</font>");
					}else{
						unAuditReason.append("<font color='red'>6.未缴清费用,请查看学生缴费;</font>");
						str2_y = false;
					}
				}
			}
			
			//c达到结业最低学分
			//暂时有广西医科大的使用第五个条件去做毕业审核,不需要做学分的判断,那就会出现结业的这个条件不通过.用学校代码判断把忽略这个条件
//			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			if(!("10598".equals(schoolCode))){
				Double graduation_score = null != studentInfo.get("theGraduationScore") ? Double.parseDouble(studentInfo.get("theGraduationScore").toString()):0D; //结业最低分
				if(graduation_score > tol_s){
					theGraduationReason.append("<font color='red'>3.结业最低学分不符合结业要求;</font>");
					str8_y = false;
				}else{
					theGraduationReason.append("<font color='blue'>3.结业最低学分符合结业要求;</font>");
				}
			}		

			//7.毕业实习通过
			if ("true".equals(practiceCourseCondition)) {
				int noExamResult = 0;
				Map<String, Object> conditionMap = new HashMap<String, Object>();
				conditionMap.put("studentId", (String)studentInfo.get("resourceid"));
				conditionMap.put("allCourse", "Y");
				conditionMap.put("schoolCode", schoolCode);
				if (ExStringUtils.isNotBlank(practiceCourseCondition)) {//不包含毕业实习类型课程 -桂林医
					conditionMap.put("courseNature","3003");
				}
				List<Map<String, Object>> noExamResultList = studentExamResultsService.findNoExamResultCourse(conditionMap);
				noExamResult = noExamResultList.size();
				//不及格情况
				int noPassCount = 0;
				//成绩List中最后一条为已修学分信息
				if (null!=listvo&&listvo.size()>0) {
					noPassCount = getNoPassCountForPractice(listvo);
				}

				if(noPassCount>0 || noExamResult>0){
					unAuditReason.append("<font color='red'>7.毕业实习成绩未通过;</font>");
					str2_y = false;
				}else{
					unAuditReason.append("<font color='blue'>7.毕业实习成绩通过;</font>");
				}
			}
			//8.提交毕业实习材料
			if ("true".equals(practiceMaterialsCondition)) {
				String hasPracticeMaterials = ExStringUtils.toString(studentInfo.get("hasPracticeMaterials"));
				if ("Y".equals(hasPracticeMaterials)) {
					unAuditReason.append("<font color='blue'>8.已提交毕业实习材料;</font>");
				} else {
					unAuditReason.append("<font color='red'>8.未提交毕业实习材料;</font>");
				}
			}
			//9.提交学籍相片
			if ("true".equals(studentPhotoCondition)) {
				String hasPhoto = ExStringUtils.toString(studentInfo.get("photoPath"));
				if (ExStringUtils.isBlank(hasPhoto)) {
					unAuditReason.append("<font color='blue'>9.已提交学籍相片;</font>");
				} else {
					unAuditReason.append("<font color='red'>9.未提交学籍相片;</font>");
				}
			}
			//10.专科清查
			if ("true".equals(juniorCheckCondition)) {
				String enterAuditStatus = ExStringUtils.toString(studentInfo.get("enterAuditStatus"));
				if ("Y".equals(enterAuditStatus)) {
					unAuditReason.append("<font color='blue'>10.入学资格审核通过;</font>");
				} else {
					unAuditReason.append("<font color='red'>10.入学资格未审核通过;</font>");
				}
			}
			str[0]= null!=studentInfo.get("resourceid")?studentInfo.get("resourceid").toString():"";
			str[5]= null!=studentInfo.get("studyno")?studentInfo.get("studyno").toString():"";
			str[1]= null!=studentInfo.get("studentname")?studentInfo.get("studentname").toString():"";
			str[6]= null!=studentInfo.get("studentstatus")?studentInfo.get("studentstatus").toString():"";
			str[7]= null!=studentInfo.get("account")?studentInfo.get("account").toString():"";
			//如果最后没有一点unAuditReason，则可以通过审核
			if(str2_y){
				//毕业审核通过
				str[2]="1";
				passCount++;
			}else{
				//审核不通过
				str[2]="0";
				unPassCount++;
			}
			
			if(str8_y){
				//结业审核通过
				str[8] = "1";
				other_passCount++;
			}else{
				str[8]="0";
				other_unPassCount++;
			}
			str[3]=unAuditReason.toString();
			str[9]=theGraduationReason.toString();
			str[10] = studentInfo.get("resourceid")+"";
			StudentInfo stu = studentInfoService.get(studentInfo.get("resourceid")+"");
			StringBuffer status = new StringBuffer();
			if(null != stu){
				status.append((ExStringUtils.isNotBlank(stu.getStudentStatus())?stu.getStudentStatus():"null")+"##")
						.append((ExStringUtils.isNotBlank(stu.getAccountStatus()+"")?stu.getAccountStatus():"null")+"##")
						.append((null != stu.getSysUser() ? (ExStringUtils.isNotBlank(stu.getSysUser().isEnabled()+"")?stu.getSysUser().isEnabled():"null") : "null"));
			}
			str[11] = ExStringUtils.isNotBlank(status.toString())?status.toString():"null##null##null";
			list.add(str);
			stuids.append(studentInfo.get("resourceid")+","); //装入学籍id
	
		}
		String st = stuids.substring(0, stuids.toString().length()-1)+")";
		condition.put("selectStuids", st);//用于查询的学籍id
		if("Y".equals(doAudit)){//如果是第一次进入审核页面，则记录审核结果，分页不记录审核结果
			List<String> toInsert = new ArrayList<String>(0);//插入
			List<String> toUpdate = new ArrayList<String>(0);//更新
			List<Map<String,Object>> listStuId = studentGDAuditJDBCService.getStudentIdInAudit(null);
			List<String> stuIds = new ArrayList<String>(0);
			for (Map<String, Object> map : listStuId) {
				stuIds.add(map.get("STUDENTINFOID").toString());
			}
			for (String[] strings : list) {
				String id = strings[0];//学籍id
				Integer status = Integer.parseInt(strings[2]);//  毕业状态
				if(strings[3]==null||"".equals(strings[3])) {
					strings[3]=" ";
				}
				if(strings[4]==null||"".equals(strings[4])) {
					strings[4]=" ";
				}
				String memo = strings[3]+"|"+strings[4]+"|"+"#0"+"|"+strings[6]+"|"+strings[7];
				Integer theGraduation = Integer.parseInt(strings[8]); //结业状态
				String memo1 = strings[9]+"|"+strings[4]+"|"+"#0"+"|"+strings[6]+"|"+strings[7];
				if("已确定毕业".equals(strings[4])){
					toUpdate.add(id+":"+status+":"+memo+":"+theGraduation+":"+memo1+":"+strings[11]);
					continue;
				}
				if(!stuIds.contains(id)){
					toInsert.add(id+":"+status+":"+memo+":"+theGraduation+":"+memo1+":"+strings[11]);
				}else{
					toUpdate.add(id+":"+status+":"+memo+":"+theGraduation+":"+memo1+":"+strings[11]);
				}
				
			}
			List<Object[]> params = new ArrayList<Object[]>();
			if(0 < toInsert.size()){
				GUIDUtils.init();
				for(String infomation : toInsert){
					List<Object> objList = new ArrayList<Object>();
					objList.add(GUIDUtils.buildMd5GUID(false));//resourceid
					objList.add(infomation.split(":")[0]);//studentinfoid	
					objList.add(infomation.split(":")[1]);//graduateauditstatus
					objList.add(infomation.split(":")[2]);//graduateauditmemo
					objList.add(infomation.split(":")[3]);//thegraduationstatis
					objList.add(infomation.split(":")[4]);
					objList.add(infomation.split(":")[5]);
					objList.add(auditDate);//审核时间
					objList.add(user.getResourceid());//审核id
					objList.add(user.getUsername());//审核人名
					objList.add(0L);
					objList.add(0);			
					params.add(objList.toArray());
				}
				jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("insert into edu_roll_stuaudit (resourceid,studentinfoid,graduateauditstatus,graduateauditmemo,theGraduationStatis,theGraduationMemo,status,audittime,auditmanid,auditman,VERSION,ISDELETED) " +
						" values (?,?,?,?,?,?,?,?,?,?,?,?)", params);
			}
			if(0 < toUpdate.size()){
				params = new ArrayList<Object[]>(0);
				for(String infomation : toUpdate){
					List<Object> objList = new ArrayList<Object>();
					objList.add(infomation.split(":")[1]);//graduateauditstatus
					objList.add(infomation.split(":")[2]);//graduateauditmemo
					objList.add(infomation.split(":")[3]);
					objList.add(infomation.split(":")[4]);
					objList.add(infomation.split(":")[5]);
					objList.add(auditDate);//审核时间
					objList.add(user.getResourceid());//审核id
					objList.add(user.getUsername());//审核人名
					objList.add(infomation.split(":")[0]);//studentinfoid	
					params.add(objList.toArray());
				}
				jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("update edu_roll_stuaudit t set t.version=t.version+1 ," +
						"t.graduateauditstatus = ? ,t.graduateauditmemo=?,t.theGraduationStatis=?,t.theGraduationMemo=? ,t.status=?,audittime = ?,auditmanid=?,auditman=? where t.studentinfoid=? " , params);
			}
		}
		//分页显示处理
		Integer pageNum = 1;
		Integer pageTotal = 1;
		if(0!=objPage.getPageNum()){
			pageNum = objPage.getPageNum();
		}
		List<String[]> tmpList = new ArrayList<String[]>(0); 
		List<StudentInfoVo> finalList = new ArrayList<StudentInfoVo>(0);
		//审核页面按照审核结果排序
		List<String[]> l_1= new ArrayList<String[]>(0);
		List<String[]> l_2= new ArrayList<String[]>(0);
		for (String[] str : list) {
			if("1".equals(str[2])){
				l_1.add(str);
			}else if ("0".equals(str[2])){
				l_2.add(str);
			}
		}
		list = new ArrayList<String[]>(0);
		for (String[] str : l_1) {
			list.add(str);
		}
		for (String[] str : l_2) {
			list.add(str);
		}
		/*******************************/
		String studentids = "";
		if(null!=list&&list.size()>0){
			Integer num = list.size();
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(num);
			Integer pageSize = objPage.getPageSize();
			if(num%pageSize>0){
				pageTotal = 1+ num/pageSize;
			}else{
				pageTotal = num/pageSize;
			}
			model.addAttribute("pageTotal", pageTotal);
			//每页显示20条记录
			for (int i =0 ;i<pageSize ;i++) {
				if(num>(pageSize*(pageNum-1)+i)) {
					tmpList.add(list.get(pageSize*(pageNum-1)+i));
				}
			}
			StringBuilder builder = new StringBuilder(tmpList.size()*36);
			for (String[] str : tmpList) {
				StudentInfoVo infoVo = new StudentInfoVo(); //发现这里并不是特意为这个输出做出的一个bean 而是为了装起这些内容随便拿的一个bean
				infoVo.setStudentNo(str[10]);
				infoVo.setAccountStatus(str[5]);//学号
				infoVo.setBloodType(str[1]);//姓名
				infoVo.setBornAddress(str[2]);//毕业审核状态
				infoVo.setBornDay(str[3]);//毕业不通过原因
				//infoVo.setCertNum(str[4]);//实际情况
				infoVo.setBH(str[8]);//结业审核状态
				infoVo.setCC(str[9]);//结业审核内容
				finalList.add(infoVo);
				builder.append(",'"+infoVo.getStudentNo()+"'");
			}
			studentids = builder.substring(1);
			objPage.setResult(finalList);
		}else{
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(0);
			objPage.setResult(finalList);
			model.addAttribute("pageTotal", 0);
		}
		condition.put("pass", passCount);
		condition.put("unpass", unPassCount);
		condition.put("other_pass", other_passCount);
		condition.put("other_unpass", other_unPassCount);
		StringBuffer operateContent = new StringBuffer();
		operateContent.append("毕业审核:审核通过条数:"+passCount+"||审核不通过条数:"+unPassCount);
		operateContent.append("||勾选的毕业审核条件:"+limScoreCondition+"||");
		operateContent.append("limScoreCondition:"+limScoreCondition+"||");
		operateContent.append("nesScoreCondition:"+nesScoreCondition+"||");
		operateContent.append("totalScoreCondition:"+totalScoreCondition+"||");
		operateContent.append("applyCondition:"+applyCondition+"||");
		operateContent.append("enterSchCondition:"+enterSchCondition+"||");
		operateContent.append("yearCondition:"+yearCondition+"||");
		operateContent.append("allPassCondition:"+allPassCondition+"||");
		operateContent.append("stuFeeCondition:"+stuFeeCondition);
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "3", UserOperationLogs.PASS, operateContent.toString());
		//增加是否选取了学费交清条件,用于审核的时候排除没有缴费记录的审核记录
		if("true".equals(stuFeeCondition)){
			model.addAttribute("stuFeeCondition",stuFeeCondition);
		}
		model.addAttribute("condition",condition);
		model.addAttribute("auditResults",objPage );
		//查询不及格课程数量
		if(null != finalList && finalList.size() > 0){
	    	Map<String,String> rtmap = new HashMap<String,String>();
	    	Map<String,String>noExamResultMap = new HashMap<String,String>();
	    	StringBuilder sqlBuilder = new StringBuilder(studentids.length()+500);
			sqlBuilder.append(" select si.resourceid key,nvl(exam.examcount,0) examcount");
			sqlBuilder.append(" from edu_roll_studentinfo si");
			sqlBuilder.append(" left join (select eer.studentid,count(*) examcount from  edu_teach_electiveexamresults eer where eer.isdeleted=0 group by eer.studentid)exam on exam.studentid=si.resourceid");
			sqlBuilder.append(" where si.isdeleted=0 and si.resourceid in("+studentids+")");
			List<Map<String, Object>> mapList = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sqlBuilder.toString(), new HashMap<String, Object>());
			Map<String, Map<String, Object>> electiveExaMap = ExBeanUtils.convertMapsToMap(mapList);
			for (StudentInfoVo vo :  finalList) {
				try {
			    	//StringBuffer str = new StringBuffer("");
			    	String noExamResult = "共0门   /  ";
			    	Integer examCount = Integer.parseInt(electiveExaMap.get(vo.getStudentNo()).get("examcount").toString());
					List<StudentExamResultsVo> listvo     = studentExamResultsService.studentExamResultsList(studentInfoService.get(vo.getStudentNo()),examCount);
					//成绩List中最后一条为已修学分信息
					   if (null!=listvo&&listvo.size()>0) {
						   StringBuffer str = new StringBuffer();
						   int noPassCount = getNoPassCount(listvo);
						   if(noPassCount>0){
							   str.append("共"+noPassCount+"门   /  ");
						   }
						   else{
							   str.append("共0门  /  ");
						   }					    	  
						   rtmap.put(vo.getAccountStatus(), str.toString());
					}
					      
					// 未录入成绩情况
					Map<String, Object> conditionMap = new HashMap<String, Object>();
					conditionMap.put("studentId", vo.getStudentNo());
					conditionMap.put("allCourse", "Y");
					List<Map<String, Object>> noExamResultList = studentExamResultsService.findNoExamResultCourse(conditionMap);
					if(noExamResultList != null && noExamResultList.size()>0){
						noExamResult = "共"+noExamResultList.size()+"门   /  ";
					}
					noExamResultMap.put(vo.getAccountStatus(), noExamResult);
				} catch (Exception e) {
					logger.error("毕业结业审核：计算不及格课程出错;");
					e.printStackTrace();
				}
			}
			model.addAttribute("rtlist", rtmap);
			model.addAttribute("noExamResultMap", noExamResultMap);
		}
		return "/edu3/roll/graduationQualif/graduateAuditResults";
		
	}

	private int getNoPassCountForPractice(List<StudentExamResultsVo> listvo) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> map_pass = new HashMap<String, Object>();
		listvo.remove(listvo.size()-1);

		for (int i = 0;i<listvo.size();i++) {
			StudentExamResultsVo v = listvo.get(i);
			String checkStatus       = ExStringUtils.trimToEmpty(v.getCheckStatusCode());
			if (!"4".equals(checkStatus)) {
				continue;
			}

			if (Constants.BOOLEAN_NO.equals(v.getIsPass()) && !"3003".equals(v.getCourseNature())) {
				v.setIndex(String.valueOf(i+1));
				map.put(v.getCourseId(), v.getCourseName());
			}else{
				map_pass.put(v.getCourseId(), v.getCourseName());
			}
		}
		if(null != map && map.size() > 0){
			if(null != map_pass && map_pass.size() > 0){
				for(String key1 : map_pass.keySet()){
					Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();
					while(it.hasNext()){
						Map.Entry<String, Object> entry=it.next();
						String key2=entry.getKey();
						if(key1.equals(key2)){
							it.remove();                     //正确
						}
					}
				}
			}
			return map.size();
			//str.append("共"+map.size()+"门   /  ");
		}else{
			// str.append("共0门  /  ");
			return 0;
		}
	}

	private int getNoPassCount(List<StudentExamResultsVo> listvo) {
		Map<String,Object> map = new HashMap<String, Object>();
		Map<String,Object> map_pass = new HashMap<String, Object>();
		listvo.remove(listvo.size()-1);
		
		for (int i = 0;i<listvo.size();i++) {
		    StudentExamResultsVo v = listvo.get(i);
		    String checkStatus       = ExStringUtils.trimToEmpty(v.getCheckStatusCode());
		    if (!"4".equals(checkStatus)) {
		      continue;
		    }
		    
		    if (Constants.BOOLEAN_NO.equals(v.getIsPass()) 
		    		// 桂林医毕业审核条件不包括毕业实习课程类别
		    		/*暂时不用
		    		 * && !("10601".equals(schoolCode) && "3003".equals(v.getCourseNature()))*/) {
				v.setIndex(String.valueOf(i+1));
				map.put(v.getCourseId(), v.getCourseName());
			}else{
				map_pass.put(v.getCourseId(), v.getCourseName());
			}
		}
	    if(null != map && map.size() > 0){
			  if(null != map_pass && map_pass.size() > 0){
				  for(String key1 : map_pass.keySet()){
					  Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();  
				        while(it.hasNext()){  
				            Map.Entry<String, Object> entry=it.next();  
				            String key2=entry.getKey();  
				            if(key1.equals(key2)){
				                it.remove();                     //正确  
				            }  
				        }  
				  }
			 }
			 return map.size();
			  //str.append("共"+map.size()+"门   /  ");
	    }else{
			 // str.append("共0门  /  ");
	    	return 0;
	    }
     //rtmap.put(vo.getAccountStatus(), str.toString());
    
	}
	
	
	/**
	 * 查看某个学生的不通过未获得学分的课程成绩信息
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/teaching/result/view-student-examresults-graduate.html")
	public String viewStudentExamResults(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage) throws Exception{
		
		String studentId           = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		StudentInfo studentInfo    = studentInfoService.get(studentId);
		User curUser               = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool         = false;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool             = true;
		}
		if (null!=studentInfo) {
			List<StudentExamResultsVo> returnList   = new ArrayList<StudentExamResultsVo>();
			List<StudentExamResultsVo> list 		= studentExamResultsService.studentExamResultsList(studentInfo,null);
	
			List<GraduateData> graduateDatas        = graduateDataService.findByHql("from "+GraduateData.class.getSimpleName()+" gd where gd.isDeleted = ? and gd.studentInfo.resourceid=? order by graduateDate desc",0,studentId);
			StudentExamResultsVo stataVo= null;
			//成绩List中最后一条为已修学分信息
	    	if (null!=list&&list.size()>0) {
	    		stataVo= list.get(list.size()-1);
				list.remove(list.size()-1);
		    	Map<String,StudentExamResultsVo> map = new HashMap<String, StudentExamResultsVo>();
		    	Map<String,StudentExamResultsVo> map_pass = new HashMap<String, StudentExamResultsVo>();
		        for (int i = 0;i<list.size();i++) {
			        StudentExamResultsVo v = list.get(i);
			        String checkStatus       = ExStringUtils.trimToEmpty(v.getCheckStatusCode());
			        if (!"4".equals(checkStatus)) {
			          continue;
			        }
			        if (Constants.BOOLEAN_NO.equals(v.getIsPass())) {
			        	if(map.containsKey(v.getCourseId())){ //当有同一门课程有多个不及格成绩时 取最终成绩
			        		//获取不通过课程的最终成绩，免考，免修等并没有考试批次也只有一次考试 所以不必进行比较 [用考试批次时间比较考试先后顺序]
				        	String batchName = ExStringUtils.trim(v.getBatchName());
				        	String batchName_map = map.get(v.getCourseId()).getBatchName();
				        	if(ExStringUtils.isNotBlank(batchName) && ExStringUtils.isNotBlank(batchName_map)){
				        		Map<String,Object> param = new HashMap<String,Object>();
					        	param.put("isdeleted", 0);
					        	param.put("batchName",batchName);//考试批次名称本不会重复
					        	List<ExamSub> subList  = examSubService.findByHql(" from "+ExamSub.class.getSimpleName() + " where isDeleted = :isdeleted and batchName = :batchName ", param);
					        	param.put("batchName", batchName_map);
					        	List<ExamSub> subList_map  = examSubService.findByHql(" from "+ExamSub.class.getSimpleName() + " where isDeleted = :isdeleted and batchName = :batchName ", param);
					        	if(null != subList_map && subList_map.size()>0 && null != subList && subList.size()>0){
					        		YearInfo y1 = subList.get(0).getYearInfo();
					        		YearInfo y2 = subList_map.get(0).getYearInfo();
					        		if(null != y1 && null != y2){
					        			Long fy1 = y1.getFirstYear();
					        			Long fy2 = y2.getFirstYear();
					        			if(fy1 < fy2){
					        				continue;
					        			}
					        		}
					        	}
				        	}
			        	}
						map.put(v.getCourseId(), v);
					}else{
						map_pass.put(v.getCourseId(), v);
					}
		         }
			     if(null != map && map.size() > 0){
			    	  if(null != map_pass && map_pass.size() > 0){
				    	  for(String key1 : map_pass.keySet()){
				    		  Iterator<Map.Entry<String, StudentExamResultsVo>> it = map.entrySet().iterator();  
				    	        while(it.hasNext()){  
				    	            Map.Entry<String, StudentExamResultsVo> entry=it.next();  
				    	            String key2=entry.getKey();  
				    	            if(key1.equals(key2)){
				    	                it.remove();                     //正确  
				    	            }  
				    	        }  
				    	  }
				      }
			     }
			     
			     if(null != map && map.size()>0){
			    	 for (String key : map.keySet()) {
			    		 returnList.add(map.get(key));
					}
			     }
	    	}
//			for (int i = 0;i<list.size();i++) {
//				StudentExamResultsVo vo = list.get(i);
//				String checkStatus 	    = ExStringUtils.trimToEmpty(vo.getCheckStatusCode());
//				if (Boolean.TRUE == isBrschool&&!"4".equals(checkStatus)) {
//					continue;
//				}
//				if (Constants.BOOLEAN_NO.equals(vo.getIsPass())) {
//					vo.setIndex(String.valueOf(i+1));
//					returnList.add(vo);
//				}
//				continue;
//
//			}
	    	
			if(null!=stataVo) {
				returnList.add(stataVo);
			}
			String eduYear = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm(),"yyyy-MM");
			if ("2".equals(studentInfo.getGrade().getTerm())) {
				eduYear    = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm(),"yyyy-MM");
			}
			if (ExStringUtils.isNotEmpty(studentInfo.getStudentStatus())&&"16".equals(studentInfo.getStudentStatus())&&
				null!=graduateDatas&&null!=graduateDatas.get(0).getGraduateDate()) {
				eduYear   += "至"+ExStringUtils.trimToEmpty(ExDateUtils.formatDateStr(graduateDatas.get(0).getGraduateDate(),"yyyy-MM"));
			}else {
				eduYear   += "至"+ExDateUtils.formatDateStr(new Date(), "yyyy-MM");
			}
			model.put("eduYear", eduYear);
			model.put("list", returnList);
			model.put("studentInfo", studentInfo);
			//学期
			Map<String,Object> dmap = new HashMap<String, Object>(0);
			dmap.put("isUsed", Constants.BOOLEAN_YES);
			dmap.put("isDeleted", 0);
			//学期
			String hql = "from "+Dictionary.class.getSimpleName()+" where isDeleted = :isDeleted and isUsed = :isUsed and dictCode like 'CodeExportExam_%' order by showOrder ";
			List<Dictionary> listdict = dictionaryService.findByHql(hql, dmap);		
			if(null != listdict && listdict.size() > 0){
				StringBuffer termStr = new StringBuffer("");
				for(Dictionary dict : listdict)   {  
					termStr.append("<input type=\"checkbox\" value=\""+dict.getDictValue()+"\" name=\"person_term\" title=\""+dict.getDictName()+"\">"+dict.getDictName());            
				}
				model.addAttribute("persernal_terms", termStr);
				
			}
			
			
		}else {
			model.put("msg", "未找到此学生的学籍记录！");
		}
		return"/edu3/roll/graduationQualif/gradyate_studentExamResult_list";
	}
	
	/**
	 * 查看毕业审核历史记录
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/confirmPage.html")
	public String enterConfirmGraduateAuditPage(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage){
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式

		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));//班级
		String pNum  = ExStringUtils.trimToEmpty(request.getParameter("pNum"));//页数
		String graduateDate  = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));//毕业时间
		String isRefresh  = ExStringUtils.trimToEmpty(request.getParameter("isRefresh"));//毕业时间
		String auditStatus	   = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
		String confirmStatus = ExStringUtils.trimToEmpty(request.getParameter("confirmStatus"));
		String graduateAuditBeginTime = ExStringUtils.trimToEmpty(request.getParameter("graduateAuditBeginTime"));//毕业审核时间起
		String graduateAuditEndTime = ExStringUtils.trimToEmpty(request.getParameter("graduateAuditEndTime"));//毕业审核时间终
		String other_auditStatus = ExStringUtils.trimToEmpty(request.getParameter("other_auditStatus"));//结业审核状态
		User currentUser = SpringSecurityHelper.getCurrentUser();
		
		//登录用户是否为教务员
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(currentUser.getOrgUnit().getUnitType())){
			branchSchool = currentUser.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}else {
			model.addAttribute("isBrschool", false);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(other_auditStatus)) {
			condition.put("other_auditStatus", other_auditStatus);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(auditStatus)) {
			condition.put("auditStatus", auditStatus);
		}
		if(ExStringUtils.isNotEmpty(confirmStatus)) {
			condition.put("confirmStatus", confirmStatus);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		if(ExStringUtils.isNotEmpty(graduateAuditBeginTime)&&ExStringUtils.isNotEmpty(graduateAuditEndTime)) {
			condition.put("dateDuration", "1");
			condition.put("graduateAuditBeginTime", graduateAuditBeginTime);
			condition.put("graduateAuditEndTime", graduateAuditEndTime);
		}
		Page page = new Page();
		if("1".equals(isRefresh)){
			objPage.setPageNum(Integer.parseInt(pNum));
		}else{
			pNum=Integer.toString(objPage.getPageNum());
		}		
		//增加是毕业筛选的标记
		page = studentGraduateAndDegreeAuditService.findGraduateAuditByCondition(condition, objPage);

		if(ExStringUtils.isNotEmpty(pNum)) {
			condition.put("pNum", pNum);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDate", graduateDate);
		}
		model.addAttribute("graduationAuditList", page);
		model.addAttribute("condition", condition);
		
		//处理年级-专业-班级级联查询
		//model.addAttribute("majorSelect1",graduationQualifService.getGradeToMajor(grade,major,"graduateauditHistory_major","major","graduateAuditHistoryMajorClick()"));
		//model.addAttribute("classesSelect1",graduationQualifService.getGradeToMajorToClasses(grade,major,classesId,branchSchool,"graduateauditHistory_classid","classId"));
		
		return "/edu3/roll/graduationQualif/graduateAuditHistory";
	}
	/**
	 * 确认毕业审核操作  
	 * 修改学籍表，新增毕业生库信息，更新审核记录表
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduateaudit/confirmGraduateAudit.html")
	public void confirmGraduateAudit(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		String stuFeeCondition = ExStringUtils.trimToEmpty(request.getParameter("stuFeeCondition"));
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		String isUseGraduatedInAdvance = CacheAppManager.getSysConfigurationByCode("isUseGraduatedInAdvance").getParamValue();//是否使用预毕业
		Map<String ,Object> map = new HashMap<String, Object>();
		StringBuffer messagestr= new StringBuffer();
		StringBuffer failMessage= new StringBuffer();
		int successCount = 0;
		int failCount = 0;
		//获得所有审核通过的记录
		String[] stu ;
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		// 学校代码
		if (!"Y".equals(isUseGraduatedInAdvance)) {
			condition.put("forConfirm", "1");
		}
		//只查询学籍状态为11 或 21的申请记录
		condition.put("statusNeed", "1");
		if("1".equals(isSelectAll)){//按条件审核

			String currentYearInfo = getCurrentYear();
			condition.put("currentYearInfo", currentYearInfo);

			List<Map<String, Object>> tmpList = studentGDAuditJDBCService.getGraduateAuditResults(condition);
			List<String> ids = new ArrayList<String>(0);
			for (Map<String, Object> map2 : tmpList) {
				ids.add(map2.get("resourceid").toString());
			}
			stu = new String[ids.size()];   
			ids.toArray(stu);    
		
		} else{//按勾选审核
			//勾选审核的确认不需要statusNeed，因为之前是做了限制的
			condition.put("stus", stus.replaceAll(",", "','"));
			List<Map<String, Object>> tmpList = studentGDAuditJDBCService.getGraduateAuditResults(condition);
			List<String> ids = new ArrayList<String>(0);
			for (Map<String, Object> map2 : tmpList) {
				ids.add(map2.get("resourceid").toString());
			}
			stu = new String[ids.size()];   
			ids.toArray(stu);    
			//stu = stus.split(",");
		}
		//设置一个针对所有毕业的审核结果的记录，true说明均审核通过，false说明存在审核不通过的。
		boolean hasError = false;
		//得到毕业时间
		String graduateDateInAudit = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));
		String gMonth ="毕";
		Integer iMonth =0 ;
		if(null!= graduateDateInAudit) {
			if(1<graduateDateInAudit.split("-").length){
				//得到1位的毕业月份
				gMonth = graduateDateInAudit.split("-")[1];
				iMonth = Integer.valueOf(gMonth);
				if(4<=iMonth&&9>=iMonth){
					gMonth = "7";
				//原来的逻辑，不知道需求来源，但已确定不适用于当前的情况，注释掉
//				}else if (4==iMonth||10==iMonth){
//					gMonth = "毕";
				}else{
					gMonth = "1";
				}
			}
		}
		try{
			if("毕".equals(gMonth)){
				messagestr.append("当前设置的毕业日期月份为"+iMonth+"月，无法确定毕业/结业所属季。");
				throw new Exception();
			}
			for (String id : stu) {
				//每一个学生因为在毕业审核之前已经对其的 年级要求和学分要求 做出了审核
				StudentInfo studentInfo = studentInfoService.get(id);//获得每个待审核的学生
				
				//这里判断是否有缴费记录
				if(ExStringUtils.isNotBlank(stuFeeCondition) && "true".equals(stuFeeCondition) ){
					Map<String, Object> con = new HashMap<String, Object>();
					con.put("studentInfoId",studentInfo.getResourceid());
					List<StudentPayment> feeList =  studentPaymentService.findStudentPaymentByCondition(con);
					if(feeList.size()<=0 && !"Y".equals(isUseGraduatedInAdvance)){
						failMessage.append(studentInfo.getStudentName()+" 没有缴费记录，确认毕业/结业失败。</br>");
						messagestr.append(studentInfo.getStudentName()+" 没有缴费记录，确认毕业/结业失败。</br>");
						failCount++;
						continue;
					}
				}
				
				//更新学籍表中的该生的学籍状态为【毕业】毕业
				List<StudentGraduateAndDegreeAudit> audits =
					studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where  isDeleted = 0 and  studentInfo.resourceid=? ",id );
				StudentGraduateAndDegreeAudit audit = audits.get(0);
				//加个学位申请标记,结业换毕业的不可以申请
				Boolean degreeFlag = true;
				if("1".equals(ExStringUtils.trim(audit.getGraduateAuditStatus()+""))){ //毕业  能毕业就不需结业 不能毕业的情况下才考虑结业
					//下面结业换毕业的学位审核标志有问题,在这里毕业的状态会变成24,导致下面获取的时候可以获取学位的申请
					if("24".equals(studentInfo.getStudentStatus())){
						degreeFlag = false;
					}
					studentInfo.setStudentStatus("16");
				}else{
					if ("Y".equals(isUseGraduatedInAdvance)) {
						studentInfo.setStudentStatus("27");//预毕业
					} else if ("1".equals(ExStringUtils.trim(audit.getTheGraduationStatis() + ""))) {
						studentInfo.setStudentStatus("24");//结业
					}
				}
				studentInfo.setAccountStatus(Constants.BOOLEAN_FALSE);
				if(null != studentInfo.getSysUser()){ //没有用户则不处理
					studentInfo.getSysUser().setEnabled(false);
				}
				studentInfoService.update(studentInfo);//执行更新操作
				//对毕业生库的更新
				try{
					List<GraduateData> graduateDatas = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where studentInfo.resourceid=? ",studentInfo.getResourceid() );
					GraduateData tmp_graduateData ;
					//使用原生sql语句执行
					//List<Object> values = new ArrayList<Object>();
					//StringBuilder builder = new StringBuilder();
					if(graduateDatas.size()>0){
						tmp_graduateData = graduateDatas.get(0);
						tmp_graduateData.setIsDeleted(0);
					}else{
						tmp_graduateData = new GraduateData();
					}
					if(!"3".equals(studentInfo.getClassic().getClassicCode())){//3：高升专的层次代码
						tmp_graduateData.setDegreeStatus(Constants.BOOLEAN_WAIT);
					}
					//tmp_graduateData.setPublishStatus(Constants.BOOLEAN_WAIT);
					tmp_graduateData.setStudentInfo(studentInfo);
					//若为本科就设置学位名称，TODO：以后优化用一个系统使用的标志来唯一标识本科或专科
//					if ("1".equals(studentInfo.getClassic().getClassicCode())||"2".equals(studentInfo.getClassic().getClassicCode())){
					if ("本科".equals(studentInfo.getClassic().getClassicName())||"专升本".equals(studentInfo.getClassic().getClassicName())){
						String degreeName = "";
						if(studentInfo.getMajor().getMajorClass()!=null){
							try {
								String majorClassCode = String.format("%02d", Integer.valueOf(studentInfo.getMajor().getMajorClass()));
								degreeName = JstlCustomFunction.dictionaryCode2Value("CodeMajorClass", majorClassCode);
							} catch (Exception e) {
								degreeName = studentInfo.getMajor().getMajorClass();
							}
						}
						tmp_graduateData.setDegreeName(degreeName);
					}else{
						tmp_graduateData.setDegreeName("");
					}
					
					tmp_graduateData.setDegreeNum("");//根据未知编码规则
					//关于毕业证明编号，如果之前存在毕业库中且对应的毕业证明与当前的毕业时间吻合，不重新生成
					String graduateDate = "";
					if(graduateDateInAudit.contains("-")){
						graduateDate = graduateDateInAudit.split("-")[0];	
					}else{
						//应该不会执行到此处 ，因为js已经对毕业日期的填写做了判断。
						messagestr.append(studentInfo.getStudentName()+"未设置毕业/结业日期。");
						break;
					}
					boolean isNeedNew = true;
					//String graduateDiploma = tmp_graduateData.getDiplomaNum();
					String graduate2diplomaRule = CacheAppManager.getSysConfigurationByCode("graduate2diplomaRule").getParamValue();
					if(graduateDatas.size()>0 && ExStringUtils.isNotBlank(tmp_graduateData.getDiplomaNum())){
						 //有了毕业证号则不需生成
						List<GraduateData> graduateDatas2 = graduateDataService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.eq("diplomaNum",tmp_graduateData.getDiplomaNum()),Restrictions.ne("resourceid", tmp_graduateData.getResourceid()));
						if(!(graduateDatas2!=null && graduateDatas2.size()>0)) {
							isNeedNew = false;
						}
					}
					
					//得到所在年级的学期
					String term = studentInfo.getGrade().getTerm();
					if("1".equals(term)){
						//如果是上学期
						tmp_graduateData.setEntranceDate(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm());
					}else{
						//如果是下学期
						tmp_graduateData.setEntranceDate(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm());
					}
					//设置毕业时间 
					if("".equals(graduateDateInAudit)){
						tmp_graduateData.setGraduateDate(null);
					}else{
						Date tmp = ExDateUtils.parseDate(graduateDateInAudit, ExDateUtils.PATTREN_DATE);
						tmp_graduateData.setGraduateDate(tmp);
					}
					
					//生成新毕业证书号
					StringBuffer diplomaNum = new StringBuffer();
					boolean isCurrentYear = false;
					long year = ExDateUtils.getCurrentYear();
					double eduyear = ExNumberUtils.toDouble(studentInfo.getTeachingPlan().getEduYear());
					if(year-studentInfo.getGrade().getYearInfo().getFirstYear()<eduyear){
						isCurrentYear = true;
					}
					if(isNeedNew || ("1".equals(graduate2diplomaRule) && !isCurrentYear)){
						//先获取用于生产毕业证号的全局参数
						String num_schoolcode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue(); //院校代码						
						String num_crcode     = CacheAppManager.getSysConfigurationByCode("School.banxue.unitCode").getParamValue(); //办学类型代码
						
						//学校代码+办学方式
						diplomaNum.append(num_schoolcode); 
						//办学单位现为成人
						diplomaNum.append(num_crcode);
						//毕业日期
						if("1".equals(graduate2diplomaRule) && !isCurrentYear){
							//非应届生按照审核时间取年份
							year = ExDateUtils.getCurrentYear();
							long month = ExDateUtils.getCurrentMonth();
							diplomaNum.append((month>=8?(year+1):year)+"");
						}else{
							diplomaNum.append(graduateDate);
						}
						
						//层次，TODO：以后优化用一个系统使用的标志来唯一标识本科或专科
						/*if("6".equals(studentInfo.getClassic().getClassicCode())){//专科的层次代码 3
						String num_bkcode     = CacheAppManager.getSysConfigurationByCode("School.Specializedsubject.levelCode").getParamValue(); //层次代码 [专科]
						diplomaNum.append(num_bkcode);
					}else if("5".equals(studentInfo.getClassic().getClassicCode())){//本科的层次代码 1、
						String num_zkcode     = CacheAppManager.getSysConfigurationByCode("School.Undergraduatecourse.levelCode").getParamValue(); //层次代码[本科]
						diplomaNum.append(num_zkcode);
					}else{*/
					if("专科".equals(studentInfo.getClassic().getClassicName())||"高起专".equals(studentInfo.getClassic().getClassicName())){//专科的层次代码 3
						String num_bkcode     = CacheAppManager.getSysConfigurationByCode("School.Specializedsubject.levelCode").getParamValue(); //层次代码 [专科]
						diplomaNum.append(num_bkcode);
					}else if("本科".equals(studentInfo.getClassic().getClassicName())||"专升本".equals(studentInfo.getClassic().getClassicName())){//本科的层次代码 1、
						String num_zkcode     = CacheAppManager.getSysConfigurationByCode("School.Undergraduatecourse.levelCode").getParamValue(); //层次代码[本科]
						diplomaNum.append(num_zkcode);
					}else{
						failMessage.append(studentInfo.getStudentName()+" 层次不合法，无法生成毕业证号，确认毕业/结业失败。</br>");
						messagestr .append(studentInfo.getStudentName()+"层次不合法，无法生成毕业证号，确认毕业/结业失败。</br>");
						studentInfo.setStudentStatus("11");
						studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
						if(null != studentInfo.getSysUser()){ //没有用户则不处理
							studentInfo.getSysUser().setEnabled(true);
						}
						studentInfoService.update(studentInfo);
						failCount++;
						continue;
					}
					//2bit学习中心代号 学籍办由两位学习中心代码变成1位学习中心片区+毕业月份 （只有1/7）
//						String unitCode = Integer.valueOf(studentInfo.getBranchSchool().getShoworder()).toString();
//						if (unitCode.equals("0")) {
//							messagestr += studentInfo.getStudentName()+"学习中心无编号，无法生成毕业证号，确认毕业失败。</br>";
//							studentInfo.setStudentStatus("11");
//							studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
//							studentInfo.getSysUser().setEnabled(true);
//							studentInfoService.update(studentInfo);
//							continue;
//						}
//						if(unitCode.length()==1){
//							unitCode = "0"+unitCode;
//						}
//						diplomaNum.append(unitCode);
					boolean isyes = false;
					if("1".equals(graduate2diplomaRule)){//应届 0，非应届 1
						isyes = isCurrentYear;
					}else {//校内 0  校外 1
						String school = CacheAppManager.getSysConfigurationByCode("in_school").getParamValue();
						school = school.replaceAll(",", "，");
						String[] str = school.split(",");
						String schoolname = null == studentInfo.getBranchSchool() ? "" : studentInfo.getBranchSchool().getUnitName();
						
						if(null != str && str.length > 0 ){
							for(int i = 0 ; i < str.length ; i++){
								if(str[i].equals(ExStringUtils.trim(schoolname))){
									isyes = true;
								}
							}
						}
					}
					if(isyes){ //校内、应届
						diplomaNum.append("0");
					}else{//校外、非应届
						diplomaNum.append("1");
					}
					isNeedNew = true;
				}
				// 保存或更新毕业数据
				if("1".equals(ExStringUtils.trim(audit.getGraduateAuditStatus()+""))){ //毕业  能毕业就不需结业 不能毕业的情况下才考虑结业
					tmp_graduateData.setGraduateType("1");
					audit.setGraduateAuditMemo(audit.getGraduateAuditMemo().replace("#0", "#1"));//审核数据标记为【已确认毕业】
				}else{ //结业
					audit.setTheGraduationMemo(audit.getTheGraduationMemo().replace("#0", "#1"));//审核数据标记为【已确认结业】
					if ("Y".equals(isUseGraduatedInAdvance)) {
						tmp_graduateData.setGraduateType("4");
					}else if("1".equals(ExStringUtils.trim(audit.getTheGraduationStatis()+""))){
						tmp_graduateData.setGraduateType("2");
					}
				}
				//可能上面的if里的确认毕业的字段无法用到 现在加入一个字段CONFIRM 用来做确认操作
				audit.setComfirm("1");
				String isAllowSecGraduate = Constants.BOOLEAN_YES;
				List<StudentExamResultsVo> listvo;
				try {			
					if(!"1".equals(tmp_graduateData.getGraduateType())){
						String courseCount=CacheAppManager.getSysConfigurationByCode("graduateData.secondGraduate.course.count").getParamValue();
						int courseCountInt = Integer.parseInt(courseCount);
						listvo = studentExamResultsService.studentExamResultsList(studentInfoService.get(id),"audit",null);
						//三种情况：
						//1、第一次毕业审核
						//2、经过撤销毕业审核
						//3、做结业换毕业审核
						if (null!=listvo&&listvo.size()>0) {
							int noPassCount = getNoPassCount(listvo);
							if(graduateDatas.size()>0 && graduateDatas.get(0).getIsAllowSecGraduate()!=null){
								if("Y".equals(graduateDatas.get(0).getIsAllowSecGraduate())){
									isAllowSecGraduate = Constants.BOOLEAN_NO;
								}else if(noPassCount<=courseCountInt){//小于等于4门课程，允许结业换毕业
									isAllowSecGraduate = Constants.BOOLEAN_YES;
								}else{
									isAllowSecGraduate = Constants.BOOLEAN_NO;
								}
							}else{
								if(noPassCount<=courseCountInt){//小于等于4门课程，允许结业换毕业
									isAllowSecGraduate = Constants.BOOLEAN_YES;
								}else{
									isAllowSecGraduate = Constants.BOOLEAN_NO;
								}
							}
						}
									
					}else{//毕业、本科  允许  申请学位/进行学位审核
						String classic = CacheAppManager.getSysConfigurationByCode("School.Undergraduatecourse.levelCode").getParamValue();
						if(tmp_graduateData.getStudentInfo().getClassic().getClassicCode().equals(classic.substring(1,2))){
							tmp_graduateData.setIsAllowAuditDegree(Constants.BOOLEAN_YES);
							if(!degreeFlag){//结业换毕业的，不允许申请学位,这个字段暂时没什么用					
								tmp_graduateData.setIsAllowAuditDegree(Constants.BOOLEAN_NO);
								if("10598".equals(CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue())){//广西医科大学可以
									tmp_graduateData.setIsAllowAuditDegree(Constants.BOOLEAN_YES);
								}
							}
						}else{
							tmp_graduateData.setIsAllowAuditDegree(Constants.BOOLEAN_NO);
						}
					}
				} catch (ServiceException e) {					
					e.printStackTrace();
					logger.error("{studentInfoid:"+id+"}");
				} catch (Exception e) {					
					e.printStackTrace();
				}
				tmp_graduateData.setIsAllowSecGraduate(isAllowSecGraduate);
				if(!isNeedNew){
					// 旧的毕业证号
					//tmp_graduateData.setDiplomaNum(graduateDiploma);
					graduateDataService.update(tmp_graduateData);
				}else{
					// 给生成毕业证号这块代码加锁
					synchronized (this) {
						// 新生成毕业证号
						if(diplomaNum!=null && diplomaNum.length()>0){
							diplomaNum.append(getDiplomaNumSuffix(diplomaNum.toString()));
							tmp_graduateData.setDiplomaNum(diplomaNum.toString());//已知编码规则
						}
						try {
							audit.setGraduateData(tmp_graduateData); //设置毕业/结业证书号
						} catch (Exception e) {
							messagestr.append(studentInfo.getStudentName()+"确认毕业/结业过程中出错.</br>");
							throw new Exception();
						}
						
						graduateDataService.save(tmp_graduateData);
						graduateDataService.flush();
						//jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate(builder.toString(), values);
					}
				}
					
				studentGraduateAndDegreeAuditService.saveOrUpdate(audit);
				
				messagestr .append(studentInfo.getStudentName()+"确认毕业/结业成功.</br>");
				successCount++;
			}catch(Exception e){
				//如果出现graduateData的更新或创建异常 恢复已修改的学籍状态
				failMessage.append(studentInfo.getStudentName()+" 确认时出现异常问题,请联系管理员，确认毕业/结业失败。</br>");
				failCount++;
				//studentInfo.setStudentStatus("11");
				studentInfo.setAccountStatus(Constants.BOOLEAN_TRUE);
				if(null != studentInfo.getSysUser()){ //没有用户则不处理
					studentInfo.getSysUser().setEnabled(true);
				}
				studentInfoService.update(studentInfo);
				//throw new Exception();
				e.printStackTrace();
				continue;
			}
		}
		messagestr.append("确认操作完毕。");
	}catch(Exception e){
		logger.error("确认操作失败！:{}",e.fillInStackTrace());
		map.put("statusCode", 300);
//			map.put("message", "确认操作失败！:<br/>");
		map.put("message", messagestr.toString()+"<br/>确认操作失败！");
		renderJson(response, JsonUtils.mapToJson(map));
	}
	map.put("statusCode", 200);
	map.put("hasError", hasError);
	//map.put("message", messagestr);
	//map.put("message", "确认完毕：成功确认"+successCount+"条,确认失败"+failCount+"条。<br/> 详情如下:<br/>"+failMessage.toString());
	map.put("message", "确认完毕：成功确认"+successCount+"条,确认失败"+failCount+"条。<br/>");
	renderJson(response, JsonUtils.mapToJson(map));
		
}
	
	/**
	 * 按班级导出毕业审核记录
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/exportExcelByClasses.html")
	public void doExportGraudateAuditExcelByClasses(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		String classId = ExStringUtils.trimToEmpty(request.getParameter("classId")); //班级
		
		Map<String,Object> parmas = new HashMap<String, Object>();
		String auditStatus	   = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
		String other_auditStatus = ExStringUtils.trimToEmpty(request.getParameter("other_auditStatus"));
		String confirmStatus = ExStringUtils.trimToEmpty(request.getParameter("confirmStatus"));
		if(ExStringUtils.isNotEmpty(classId)) {
			parmas.put("classId", classId);
		}
		
		parmas.put("isDeleted", 0);
		
		//StudentInfo st = null;
		String className = "";
		StringBuilder builder = new StringBuilder(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName());
		builder.append(" at where at.isDeleted = :isDeleted and at.studentInfo.classes.resourceid = :classId ");
		/*if(ExStringUtils.isNotEmpty(auditStatus)){
			builder.append(" and at.graduateAuditStatus=:auditStatus");
			parmas.put("auditStatus",auditStatus);
		}
		if(ExStringUtils.isNotEmpty(other_auditStatus)){
			builder.append(" and at.theGraduationStatis = :other_auditStatus");
			parmas.put("other_auditStatus", Integer.parseInt(other_auditStatus));
		}
		if(ExStringUtils.isNotEmpty(confirmStatus)){
			builder.append(" and at.graduateAuditMemo like :confirmStatus");
			parmas.put("confirmStatus", "%#"+confirmStatus+"%");
		}*/
		List<StudentGraduateAndDegreeAudit> listaudit = studentGraduateAndDegreeAuditService.findByHql(builder.toString(), parmas);
		List<StudentInfoVo> list = new ArrayList<StudentInfoVo>(0);//结果集
		Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
		Map<String, Map<String, Object>> studentInfoMap = new HashMap<String, Map<String,Object>>();
		if(ExStringUtils.isNotBlank(classId)){ //班级id
			
			if(null != listaudit && listaudit.size()>0){
				Integer py_pass = 0;
				Integer jy_pass = 0;
				Integer ot_pass = 0;
				try {
					builder.setLength(0);
					builder.append(" from "+StudentInfo.class.getSimpleName()+" si where si.isDeleted=0 and si.classes.resourceid = :classId");
					List<StudentInfo> studentList = studentInfoService.findByHql(builder.toString(), parmas);
					studentInfoMap = studentInfoService.getStudentInfoMapByStuList(studentList);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Map<String, Object> studentInfoMap_0 = null;
				//待优化(已优化学籍信息查询和成绩查询)
				for(StudentGraduateAndDegreeAudit atvo : listaudit){
					StudentInfoVo infoVo = new StudentInfoVo();
					StudentInfo info = atvo.getStudentInfo();
					//if(null == st)st = info;
					if(studentInfoMap_0==null) {
						studentInfoMap_0 = studentInfoMap.get(info.getResourceid());
					}
					infoVo.setSortNum(info.getStudyNo()); //学号
					infoVo.setMajor(info.getStudentName()); //姓名
					String status1 = atvo.getGraduateAuditStatus()+"";
					infoVo.setGrade("1".equals(status1) ? "审核通过":"审核不通过");//毕业审核状态
					String str1 = (atvo.getGraduateAuditMemo()+"").replaceAll("<font color='blue'>", "").replaceAll("</font>", "").replaceAll("<font color='red'>", "").split("\\|")[0];
					infoVo.setUnit(ExStringUtils.isNotBlank(str1)  ? str1.replace("null", "") : "");//毕业审核原因
					String status2 = atvo.getTheGraduationStatis()+"";
					infoVo.setClassic("1".equals(status2) ? "审核通过":"审核不通过");//结业审核状态
					String str2 =  (atvo.getTheGraduationMemo()+"").replaceAll("<font color='blue'>", "").replaceAll("</font>", "").replaceAll("<font color='red'>", "").split("\\|")[0];
					infoVo.setStudentNo(ExStringUtils.isNotBlank(str2) ? str2.replace("null", "") : "");////结业审核原因
					infoVo.setExamNo(ExStringUtils.isNotBlank(atvo.getComfirm()) && "1".equals(atvo.getComfirm()) ? "已确认" : "未确认");////确认状态
					//年限
					//String edu_year = info.getTeachingPlan().getEduYear(); //学年
					String edu_year = (String) studentInfoMap.get(info.getResourceid()).get("eduYear");
					//Long   	 year = info.getGrade().getYearInfo().getFirstYear(); //年份
					Long year = (Long) studentInfoMap.get(info.getResourceid()).get("firstyear");
					//String   term = info.getGrade().getTerm(); //学期
					String term = (String) studentInfoMap.get(info.getResourceid()).get("gradeterm");
					String currentYearInfo = getCurrentYear(); //当前年份
					
					Double eduYearD   =  Double.parseDouble(edu_year);
					Double termD      =  Double.parseDouble(term);
					Double yearD      =  Double.parseDouble(year+"");
					Double currentYearInfoD  =  Double.parseDouble(currentYearInfo);
					
					Double _d = (eduYearD-0.5)+yearD+(termD-1)*0.5;
					String strYear = "";
					if(_d <= currentYearInfoD) {
						strYear = "达到";
					}
					infoVo.setStudyAttr(strYear);////年限
					String stdentstatus = info.getStudentStatus();
					if("16".equals(stdentstatus) || "24".equals(stdentstatus) || "11".equals(stdentstatus) || "21".equals(stdentstatus)) {
						stdentstatus = "达到";
					} else {
						stdentstatus = "未达到";
					}
					infoVo.setStudyForm(stdentstatus);////学籍状态
					infoVo.setInSchoolType(info.getFinishedCreditHour()+"");////已修总学分
					infoVo.setStudyType(info.getFinishedNecessCreditHour()+"");////已修必修总学分
					//infoVo.setIsApplyGraduation(info.getTeachingPlan().getTheGraduationScore()+"");////结业最低总学分
					infoVo.setIsApplyGraduation((String) studentInfoMap.get(info.getResourceid()).get("theGraduationScore"));
					//infoVo.setEnterAttr(info.getGrade().getGradeName());//年级
					infoVo.setEnterAttr((String) studentInfoMap.get(info.getResourceid()).get("gradename"));
					//infoVo.setInSchoolStatus(info.getClassic().getClassicName());	//层次
					infoVo.setInSchoolStatus((String) studentInfoMap.get(info.getResourceid()).get("classicname"));
					//infoVo.setStudyStatus(info.getMajor().getMajorName());//专业
					infoVo.setStudyStatus((String) studentInfoMap.get(info.getResourceid()).get("majorname"));
					//infoVo.setIsBookGD(info.getBranchSchool().getUnitName());//学习中心
					infoVo.setIsBookGD((String) studentInfoMap.get(info.getResourceid()).get("unitname"));
					//不及格课程信息
					int noPassCount = 0;
					try {
						//选修课次数
						Integer examCount = Integer.parseInt(studentInfoMap.get(info.getResourceid()).get("examcount").toString());
						List<StudentExamResultsVo> listvo     = studentExamResultsService.studentExamResultsList(info,examCount);
						Map<String,Object> map_no = new HashMap<String, Object>();
				    	Map<String,Object> map_pass = new HashMap<String, Object>();
						if (null!=listvo&&listvo.size()>0) {
					    	listvo.remove(listvo.size()-1);
					        for (int i = 0;i<listvo.size();i++) {
						        StudentExamResultsVo v = listvo.get(i);
						        String checkStatus       = ExStringUtils.trimToEmpty(v.getCheckStatusCode());
						        if (!"4".equals(checkStatus)) {
						          continue;
						        }
						        if (Constants.BOOLEAN_NO.equals(v.getIsPass())) {
									v.setIndex(String.valueOf(i+1));
									map_no.put(v.getCourseId(), v.getCourseName());
								}else{
									map_pass.put(v.getCourseId(), v.getCourseName());
								}
					      }
					      if(null != map_no && map_no.size() > 0){
					    	  if(null != map_pass && map_pass.size() > 0){
						    	  for(String key1 : map_pass.keySet()){
						    		  Iterator<Map.Entry<String, Object>> it = map_no.entrySet().iterator();  
						    	        while(it.hasNext()){  
						    	            Map.Entry<String, Object> entry=it.next();  
						    	            String key2=entry.getKey();  
						    	            if(key1.equals(key2)){
						    	                it.remove();                     //正确  
						    	            }  
						    	        }  
						    	  }
						    	  if(null != map_no && map_no.size() > 0){
							    	  StringBuffer str = new StringBuffer("");
							    	  int i = 1;
							    	  for (String key : map_no.keySet()) {
							    		  if(i == map_no.size()){
							    			  str.append(map_no.get(key)+";共"+map_no.size()+"门;");
							    		  }else{
							    			  str.append(map_no.get(key)+",");
							    		  }
							    		  i++;
							    	  }
							    	  infoVo.setHomePage(str.toString());
							      }else{
							    	  infoVo.setHomePage("没有不及格的课程;");
							      }
						      }
					      }else{
					    	  infoVo.setHomePage("没有不及格的课程;");
					      }
						}else{
							  infoVo.setHomePage("没有不及格的课程;");
						}
						noPassCount = map_no.size();
					} catch (Exception e) {
						logger.error("毕业结业审核信息导出：计算不及格课程出错;");
						e.printStackTrace();
					}
					if("1".equals(atvo.getGraduateAuditStatus()+"")){ //可毕业
						py_pass++;
					}else if(!"1".equals(atvo.getGraduateAuditStatus()+"") && "1".equals(atvo.getTheGraduationStatis()+"")){
						jy_pass++;
					}else{
						ot_pass++;
					}
					infoVo.setWorkStatus(noPassCount+"");//没通过课程数量
					if((ExStringUtils.isEmpty(auditStatus) || ExStringUtils.isNotEmpty(auditStatus) && auditStatus.equals(atvo.getGraduateAuditStatus().toString()))
						&&(ExStringUtils.isEmpty(other_auditStatus) || ExStringUtils.isNotEmpty(other_auditStatus) && other_auditStatus.equals(atvo.getTheGraduationStatis().toString()))
						&&(ExStringUtils.isEmpty(confirmStatus) || ExStringUtils.isNotEmpty(confirmStatus) && confirmStatus.equals(atvo.getComfirm()))
						){
						list.add(infoVo);
					}
				}
				//Classes classes = classesService.get(classId);
				//Map<String, Object> parameter = new HashMap<String, Object>();
				//parameter.put("classesid", classId);
				//parameter.put("stuStatus", "11','12','16','21','24','25");
				//Integer stuNumber = studentInfoService.getStudentNum(parameter);
				className = (String) studentInfoMap_0.get("classesname"); 
				templateMap.put("className", className+"班毕业审核记录"); //班级名称
				templateMap.put("classNum", null != listaudit && listaudit.size()>0 ? listaudit.size() : ""); //班级人数
				//templateMap.put("schoolName", classes.getBrSchool().getUnitName()); //教学站名称
				templateMap.put("schoolName", studentInfoMap_0.get("unitname"));
				//templateMap.put("majorName",classes.getMajor().getMajorName()); //专业名称
				templateMap.put("majorName",studentInfoMap_0.get("majorname"));
	 			//templateMap.put("teachingType",JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",classes.getTeachingType())); //学习形式 字典CodeTeachingType
				templateMap.put("teachingType",JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",(String) studentInfoMap_0.get("teachingtype")));
				//templateMap.put("grade",classes.getGrade().getGradeName()); //年级
				templateMap.put("grade",studentInfoMap_0.get("gradename"));
				templateMap.put("grauationNum", py_pass); //毕业通过人数
				templateMap.put("theGraduationNum", jy_pass); //结业通过人数
				templateMap.put("otherNum", ot_pass); // 不通过人数
			}
		}else{
			
		}
		
//		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//模板文件路径
			String templateFilepathString = "exportExcelByClasses.xls";

			exportExcelService.initParmasByfile(disFile, "StudentGraduateAudit", list,null);
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			if(ExStringUtils.isBlank(className)){
				className = "";
			}else if(!className.contains("班")){
				className += "班";
			}
			downloadFile(response, className+"毕业审核记录.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	/**
	 * 导出毕业审核信息 
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/exportExcel.html")
	public void doExportGraudateAuditExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		List<StudentInfoVo> list = new ArrayList<StudentInfoVo>(0);//结果集
		List<Map<String,Object>> tmpList = new ArrayList<Map<String,Object>>(0);
		//String exportType = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出方式
		Map<String,Object> condition = new HashMap<String, Object>(0);
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		if("1".equals(isSelectAll)){
			String graduateAuditBeginTime = ExStringUtils.trimToEmpty(request.getParameter("graduateAuditBeginTime"));//毕业审核时间起
			String graduateAuditEndTime = ExStringUtils.trimToEmpty(request.getParameter("graduateAuditEndTime"));//毕业审核时间终
			if(ExStringUtils.isNotEmpty(graduateAuditBeginTime)&&ExStringUtils.isNotEmpty(graduateAuditEndTime)) {
				condition.put("dateDuration", "1");
				condition.put("graduateAuditBeginTime", graduateAuditBeginTime);
				condition.put("graduateAuditEndTime", graduateAuditEndTime);
			}
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
			String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
			String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
			String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
			String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
			String classId = ExStringUtils.trimToEmpty(request.getParameter("classId"));//班级
			String studentstatus= ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
			String auditStatus	   = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
			String confirmStatus = ExStringUtils.trimToEmpty(request.getParameter("confirmStatus"));
			String checkHis = ExStringUtils.trimToEmpty(request.getParameter("checkHis"));
			String isReachGraYear = ExStringUtils.trimToEmpty(request.getParameter("isReachGraYear"));//达到毕业最低年限
			String isPassEnter    = ExStringUtils.trimToEmpty(request.getParameter("isPassEnter"));//通过入学资格审核
			String isApplyDelay   = ExStringUtils.trimToEmpty(request.getParameter("isApplyDelay"));//申请延迟毕业
			String accountStatus  = ExStringUtils.trimToEmpty(request.getParameter("accoutstatus"));
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotEmpty(name)) {
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(studyNo)) {
				condition.put("studyNo", studyNo);
			}
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			if(ExStringUtils.isNotEmpty(studentstatus)) {
				condition.put("studentstatus", studentstatus);
			}
			if(ExStringUtils.isNotEmpty(auditStatus)) {
				condition.put("auditStatus",auditStatus);
			}
			if(ExStringUtils.isNotEmpty(confirmStatus)) {
				condition.put("confirmStatus", "%#"+confirmStatus+"%");
			}
			if(ExStringUtils.isNotEmpty(isReachGraYear)) {
				condition.put("isReachGraYear", isReachGraYear);
			}
			if(ExStringUtils.isNotEmpty(isPassEnter)) {
				condition.put("isPassEnter", isPassEnter);
			}
			if(ExStringUtils.isNotEmpty(isApplyDelay)) {
				condition.put("isApplyDelay", isApplyDelay);
			}
			if(ExStringUtils.isNotEmpty(accountStatus)) {
				condition.put("accoutstatus", accountStatus);
			}
			if(ExStringUtils.isNotEmpty(classId)) {
				condition.put("classId", classId);
			}
			String currentYearInfo = getCurrentYear();
			condition.put("currentYearInfo", currentYearInfo);
			if(!"1".equals(checkHis)){
				condition.put("statusNeed", "1");
			}
			condition.put("exportGaudit", "Y");
			condition.put("queryEleExamCount", "Y");//查询选修课成绩次数
			tmpList = studentGDAuditJDBCService.getGraduateAuditResults(condition);
		}else {
			String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
			condition.put("exportGaudit", "Y");
			condition.put("queryEleExamCount", "Y");//查询选修课成绩次数
			String currentYearInfo = getCurrentYear();
			condition.put("currentYearInfo", currentYearInfo);
			//勾选审核的导出不需要statusNeed，因为之前是做了限制的
			condition.put("stus", stus.substring(0, stus.lastIndexOf(',')).replaceAll(",", "','"));
			tmpList = studentGDAuditJDBCService.getGraduateAuditResults(condition);
		}
		for (Map<String, Object> map2 : tmpList) {
			StudentInfoVo infoVo = new StudentInfoVo();
			infoVo.setSortNum(map2.get("studyno")==null?"":map2.get("studyno").toString()); //学号
			infoVo.setMajor(map2.get("studentname")==null?"":map2.get("studentname").toString()); //姓名
			String status1 = map2.get("graduateauditstatus")==null?"":map2.get("graduateauditstatus").toString();
			infoVo.setGrade("1".equals(status1) ? "审核通过":"审核不通过");//毕业审核状态
			String str1 = map2.get("graduateauditmemo")==null?"":map2.get("graduateauditmemo").toString().replaceAll("<font color='blue'>", "").replaceAll("</font>", "").replaceAll("<font color='red'>", "").split("\\|")[0];
			infoVo.setUnit(ExStringUtils.isNotBlank(str1)  ? str1.replace("null", "") : "");//毕业审核原因
			String status2 = map2.get("theGraduationStatis")==null?"":map2.get("theGraduationStatis").toString();
			infoVo.setClassic("1".equals(status2) ? "审核通过":"审核不通过");//结业审核状态
			String str2 =  map2.get("theGraduationMemo")==null?"":map2.get("theGraduationMemo").toString().replaceAll("<font color='blue'>", "").replaceAll("</font>", "").replaceAll("<font color='red'>", "").split("\\|")[0];
			infoVo.setStudentNo(ExStringUtils.isNotBlank(str2) ? str2.replace("null", "") : "");////结业审核原因
			infoVo.setExamNo(map2.get("confirm")==null?"":map2.get("confirm").toString());////确认状态
			infoVo.setStudyAttr(map2.get("isaccYear")==null?"":map2.get("isaccYear").toString());////年限
			infoVo.setStudyForm(map2.get("isaccStustatus")==null?"":map2.get("isaccStustatus").toString());////学籍
			infoVo.setInSchoolType(map2.get("finishedcredithour")==null?"":map2.get("finishedcredithour").toString());////已修总学分
			infoVo.setStudyType(map2.get("finishednecesscredithour")==null?"":map2.get("finishednecesscredithour").toString());////已修必修总学分
			infoVo.setIsApplyGraduation(map2.get("theGraduationScore")==null?"":map2.get("theGraduationScore").toString());////结业最低总学分
			infoVo.setEnterAttr(map2.get("gradename")==null?"":map2.get("gradename").toString());//年级
			infoVo.setInSchoolStatus(map2.get("classicname")==null?"":map2.get("classicname").toString());	//层次
			infoVo.setStudyStatus(map2.get("majorname")==null?"":map2.get("majorname").toString());//专业
			infoVo.setIsBookGD(map2.get("unitname")==null?"":map2.get("unitname").toString());//学习中心
			//不及格课程信息
			int noPassCount = 0;
			try {
				//待优化(已优化成绩查询)
				Integer examCount = Integer.parseInt(map2.get("examcount").toString());
				List<StudentExamResultsVo> listvo     = studentExamResultsService.studentExamResultsList(studentInfoService.get(map2.get("resourceid")+""),examCount);
		    	Map<String,Object> map_no = new HashMap<String, Object>();
		    	Map<String,Object> map_pass = new HashMap<String, Object>();
				//成绩List中最后一条为已修学分信息
				if (null!=listvo&&listvo.size()>0) {
			    	listvo.remove(listvo.size()-1);
			        for (int i = 0;i<listvo.size();i++) {
				        StudentExamResultsVo v = listvo.get(i);
				        String checkStatus       = ExStringUtils.trimToEmpty(v.getCheckStatusCode());
				        if (!"4".equals(checkStatus)) {
				          continue;
				        }
				        if (Constants.BOOLEAN_NO.equals(v.getIsPass())) {
							v.setIndex(String.valueOf(i+1));
							map_no.put(v.getCourseId(), v.getCourseName());
						}else{
							map_pass.put(v.getCourseId(), v.getCourseName());
						}
			      }
			      if(null != map_no && map_no.size() > 0){
			    	  if(null != map_pass && map_pass.size() > 0){
				    	  for(String key1 : map_pass.keySet()){
				    		  Iterator<Map.Entry<String, Object>> it = map_no.entrySet().iterator();  
				    	        while(it.hasNext()){  
				    	            Map.Entry<String, Object> entry=it.next();  
				    	            String key2=entry.getKey();  
				    	            if(key1.equals(key2)){
				    	                it.remove();                     //正确  
				    	            }  
				    	        }  
				    	  }
				    	  if(null != map_no && map_no.size() > 0){
					    	  StringBuffer str = new StringBuffer("");
					    	  int i = 1;
					    	  for (String key : map_no.keySet()) {
					    		  if(i == map_no.size()){
					    			  str.append(map_no.get(key)+";共"+map_no.size()+"门;");
					    		  }else{
					    			  str.append(map_no.get(key)+",");
					    		  }
					    		  i++;
					    	  }
					    	  infoVo.setHomePage(str.toString());
					      }else{
					    	  infoVo.setHomePage("没有不及格的课程;");
					      }
				      }
			      }else{
			    	  infoVo.setHomePage("没有不及格的课程;");
			      }
				}else{
					  infoVo.setHomePage("没有不及格的课程;");
				}
				noPassCount = map_no.size();
			} catch (Exception e) {
				logger.error("毕业结业审核信息导出：计算不及格课程出错;");
				e.printStackTrace();
			}
			infoVo.setWorkStatus(noPassCount+"");//不及格课程数量
			list.add(infoVo);
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			if(0==list.size()){
				throw new Exception("此查询条件下的数据为空。");
			}
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			List<String> dictCodeList = new ArrayList<String>();
			
			Map<String , Object> map1 = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
			map1.put("CodeApplyGraduate_Y", "申请学位、毕业");
			map1.put("CodeApplyGraduate_N", "申请毕业");
			map1.put("CodeApplyGraduate_W", "延迟毕业");
			map1.put("CodeEnterAudit_Y", "审核通过");
			map1.put("CodeEnterAudit_N", "审核不通过");
			map1.put("CodeEnterAudit_W", "待审核");
			map1.put("CodeGraduateAudit_1", "审核通过");
			map1.put("CodeGraduateAudit_0", "审核不通过");
			map1.put("CodeGraduateAuditConfirm_#1", "已确定");
			map1.put("CodeGraduateAuditConfirm_#0", "未确定");
			exportExcelService.initParmasByfile(disFile, "StudentGraduateAudit", list,map1);
			exportExcelService.getModelToExcel().setHeader("毕业审核结果表");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "毕业审核结果表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	/** 
	 * 毕业审核操作   (暂废弃)
	 * 审核通过  审核不通过       毕业日期应当用edu_teach_graduatedata的数据   学籍状态 需要用edu_roll_studentInfo的数据
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/roll/graduateaudit/viaGraduate_unable.html")
	public void viaGraduate_unable(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String messagestr="";
		String[] stu = stus.split(",");
		//设置一个针对所有毕业的审核结果的记录，true说明均审核通过，false说明存在审核不通过的。
		boolean hasError = false;
		try{
			for (String id : stu) {
				//每一个学生因为在毕业审核之前已经对其的 年级要求和学分要求 做出了审核
				StudentInfo studentInfo = studentInfoService.get(id);//获得每个待审核的学生
				String studentStatus = studentInfo.getStudentStatus();
				if(!"11".equals(studentStatus)){
					messagestr += studentInfo.getStudentName()+"学籍状态不是在学，不予审核.</br>";
					continue;
				}
			
//				Map<String,Object> values= new HashMap<String,Object>();
//				values.put("stuInfoId", id);
//				Page page = new Page();
//				page = stuperpayfeeservice.findFactFeeByCondition(values,page);
//				//得到该学生的所有的缴费记录
//				List<StudentFactFee> list = page.getResult();
//				boolean isLack = false;
//				Double lackMoney = new Double(0);
//				//遍历缴费记录 判断有无欠费的情况
//				for (StudentFactFee studentFactFee : list) {
//					Double recpayFee = studentFactFee.getRecpayFee();
//					Double derateFee = studentFactFee.getDerateFee();
//					Double facepayFee = studentFactFee.getFacepayFee();
//					if(facepayFee<recpayFee-derateFee){
//						isLack = true;
//						lackMoney +=recpayFee-derateFee-facepayFee;
//					}
//				}
//				if(0==list.size()){//无缴费记录  无缴费记录的能否毕业？
//					messagestr += studentInfo.getStudentName()+"无缴费记录.";
//				}else if(isLack==true){//存在欠费记录 不可毕业
//					messagestr += studentInfo.getStudentName()+"欠费"+lackMoney+",详情请查阅缴费记录。";
//					hasError = true;
//				}else {//正常缴费 允许毕业
			
					//得到毕业时间
					String graduateDateInAudit = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));
					//更新学籍表中的该生的学籍状态为【准毕业】毕业
					studentInfo.setStudentStatus("22");
					if(!"EB43B13C75ABF518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())){
						studentInfo.setDegreeStatus(Constants.BOOLEAN_WAIT);
					}
					studentInfoService.update(studentInfo);//执行更新操作
					//对毕业生库的更新
					try{
						List<GraduateData> graduateDatas = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where studentInfo.resourceid=? ",studentInfo.getResourceid() );
						GraduateData tmp_graduateData ;
						if(graduateDatas.size()>0){
							tmp_graduateData = graduateDatas.get(0);
						}else{
							tmp_graduateData = new GraduateData();
						}
						tmp_graduateData.setPublishStatus(Constants.BOOLEAN_WAIT);
						tmp_graduateData.setStudentInfo(studentInfo);
						tmp_graduateData.setDegreeName(studentInfo.getBranchSchool().getUnitName());
						tmp_graduateData.setDegreeNum("");//根据未知编码规则
						//关于毕业证明编号，如果之前存在毕业库中且对应的毕业证明与当前的毕业时间吻合，不重新生成
						String graduateDate = "";
						if(graduateDateInAudit.contains("-")){
							graduateDate = graduateDateInAudit.split("-")[0];	
						}else{
							messagestr += studentInfo.getStudentName()+"毕业日期丢失.";
							break;
						}
						boolean isNeedNew = true;
						String graduateDiploma ="";
						if(graduateDatas.size()>0){
							graduateDiploma = graduateDatas.get(0).getDiplomaNum();
							if(graduateDate.equals(graduateDiploma.substring(6, 10))){
								isNeedNew = false;
							}
						}
						if(isNeedNew){
							//生成新毕业证书号
							StringBuffer diplomaNum = new StringBuffer();
							//学校代码+办学方式
							diplomaNum.append("105717");
							//毕业日期
							diplomaNum.append(graduateDate);
							//层次
							if("EB43B13C75ABF518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())){
								diplomaNum.append("05");
							}else if("EB43B13C75A9F518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())||"EB43B13C75AAF518E030007F0100743D".equals(studentInfo.getClassic().getResourceid())){
								diplomaNum.append("06");
							}else{
								messagestr += studentInfo.getStudentName()+"层次不合法.审核不通过.</br>";
								studentInfo.setStudentStatus("11");
								studentInfoService.update(studentInfo);
								continue;
							}
							//2bit学习中心代号
							String unitCode = Integer.valueOf(studentInfo.getBranchSchool().getShoworder()).toString();
							if (unitCode.equals("0")) {
								messagestr += studentInfo.getStudentName()+"学习中心无编号.审核不通过.</br>";
								studentInfo.setStudentStatus("11");
								studentInfoService.update(studentInfo);
								continue;
							}
							if(unitCode.length()==1){
								unitCode = "0"+unitCode;
							}
							diplomaNum.append(unitCode);
							//4bit流水号
							diplomaNum.append(getDiplomaNumSuffix(diplomaNum.toString()));
							tmp_graduateData.setDiplomaNum(diplomaNum.toString());//已知编码规则
						}else{
							//使用旧的毕业证书号
							tmp_graduateData.setDiplomaNum(graduateDiploma);
						}
						//tmp_graduateData.setEntranceDate(null);//未找到入学日期
						//得到所在年级的学期
						String term = studentInfo.getGrade().getTerm();
						if("1".equals(term)){
							//如果是上学期
							tmp_graduateData.setEntranceDate(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm());
						}else{
							//如果是下学期
							tmp_graduateData.setEntranceDate(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm());
						}
						//设置毕业时间
						if("".equals(graduateDateInAudit)){
							tmp_graduateData.setGraduateDate(null);
						}else{
							Date tmp = ExDateUtils.parseDate(graduateDateInAudit, ExDateUtils.PATTREN_DATE);
							tmp_graduateData.setGraduateDate(tmp);
						}
						tmp_graduateData.setGraduateType("1");
						if(graduateDatas.size()>0){
							tmp_graduateData.setIsDeleted(0);
							graduateDataService.update(tmp_graduateData);
						}else{
							graduateDataService.save(tmp_graduateData);
						}
						messagestr += studentInfo.getStudentName()+"审核成功.</br>";
					}catch(Exception e){
						//如果出现graduateData的更新或创建异常 恢复已修改的学籍状态
						studentInfo.setStudentStatus("11");
						studentInfo.setDegreeStatus(null);
						studentInfoService.update(studentInfo);
						
						throw new Exception();
					}
//				}
			}
		}catch(Exception e){
			logger.error("审核操作失败！:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核操作失败！:<br/>"+e.getLocalizedMessage());	
		}
		map.put("statusCode", 200);
		map.put("hasError", hasError);
		map.put("message", messagestr);	
		renderJson(response, JsonUtils.mapToJson(map));
		//StuFeePayTimer t=new StuFeePayTimer();
		//stuFeePayTimer.run();//对学生的缴费情况进行同步 有新的缴费情况时才做同步操作
		//t.run();
//		// 学籍表中学籍状态：16  毕业  13 退学  18学习期限已过 12 休学  11在学  20放弃入学资格
//		String via=ExStringUtils.isNotEmpty(request.getParameter("via"))?request.getParameter("via"):"";
//		System.out.println("via== "+via);
//		Map<String ,Object> map = new HashMap<String, Object>();
//		String messagestr="";
//		try{
//			if(ExStringUtils.isNotEmpty(resourceid)){
//				if(!"11".equals(via.trim())){//判断进行的是审核通过操作还是审核不通过操作，审核不通过直接修改学籍状态；
//					Map<String,Object> values= new HashMap<String,Object>();
//					values.put("resourceid", resourceid);
//					List list=stuperpayfeeservice.findByHql(values);
//					Object[] obj=(Object[])list.get(0);
//					double totalfee=Double.parseDouble(obj[2].toString());//totalfee 该学员对应的交费标准的应缴费用
//					double payedFee=Double.parseDouble(obj[3].toString());//payedFee 该学员实交费用
//					double payableFee=Double.parseDouble(obj[4].toString());//payableFee 该学员应缴费用（学生交费明细表edu_roll_stupaydetail中的字段）
////					System.out.println("  应缴费用  "+totalfee+" 实交费用 "+payedFee+"  学员应缴费用 "+payableFee);
//					if(totalfee==0){
//						messagestr="该学员未分配教学计划！";
//					}
//					else if(payedFee>=totalfee){
//						StudentInfo studentInfo = studentInfoService.get(resourceid);
//						studentInfo.setStudentStatus(via);
//						studentInfoService.update(studentInfo);//执行更新操作
//						messagestr="审核操作成功！";
//					}
//					else{
//						messagestr="该学员欠费，应缴:￥"+totalfee+"元   实缴:￥"+payedFee+"元";
//					}
//				
//				}else{
//					System.out.println("---不通过--"+via+"------");
//					StudentInfo studentInfo = studentInfoService.get(resourceid);
//					studentInfo.setStudentStatus(via);
//					studentInfoService.update(studentInfo);//执行更新操作
//					messagestr="审核操作成功！";
//				}
//			}
//		}catch(Exception e){
//			logger.error("审核操作失败！:{}",e.fillInStackTrace());
//			map.put("statusCode", 300);
//			map.put("message", "审核操作失败！:<br/>"+e.getLocalizedMessage());	
//		}
//			map.put("statusCode", 200);
//			map.put("message", messagestr);				
//			map.put("forward", request.getContextPath()+"/edu3/schoolroll/graduate/audit/list.html");
//		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 得到毕业证的编号
	 * @param prefix
	 * @return
	 */
	public String getDiplomaNumSuffix(String prefix) {
		String suffix = "";
		List<GraduateData> result = graduateDataService.findByHql("from "+GraduateData.class.getSimpleName()+" where diplomaNum like '" + prefix + "%' order by diplomaNum desc ");
		if (result.size() == 0) {
			suffix = "00000";
		} else {
			GraduateData graduateData = (GraduateData) result.get(0);
			suffix = graduateData.getDiplomaNum().substring(prefix.length());
			if ("99999".equals(suffix)) {
				throw new ServiceException("没有空余的流水号了！");
			}
		}
		Integer suffix_Num = Integer.valueOf(suffix);
		suffix_Num++;
		suffix = suffix_Num.toString();
		int remain = 5-suffix.length();
		StringBuffer suffix_buffer = new StringBuffer();
		for(int i =0 ; i<remain ;i++){
			suffix_buffer.append("0");
		}
		suffix_buffer.append(suffix);
		return suffix_buffer.toString();
	}
	/**
	 * 毕业审核信息自定义导出条件
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduate/excel/exportCustomExcelCondition.html")
	public String doExcelExportStudentGraduateCustomCondition(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		return "/edu3/roll/graduationQualif/exportCustomExcelCondition";
	}
	/**
	 * 自定义导出信息
	 * @param request
	 * @param response
	 * @param studentInfoVo
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduationQualif/exportCustomExcel.html")
	public void exportGraduationInfos(StudentInfoVo studentInfoVo,HttpServletRequest request,HttpServletResponse response) {
	//从库中查出数据		

		String unit        = ExStringUtils.trimToEmpty(request.getParameter("exbranchSchool"));
		String grade       = ExStringUtils.trimToEmpty(request.getParameter("exgrade"));
		String studystatus = ExStringUtils.trimToEmpty(request.getParameter("exstuStatus"));
		String classic     = ExStringUtils.trimToEmpty(request.getParameter("exclassic"));
		String major       = ExStringUtils.trimToEmpty(request.getParameter("exmajor"));
			
		List<StudentInfo> results = new ArrayList<StudentInfo>(0);
		
		StringBuffer hql = new StringBuffer();
		hql.append(" from "+StudentInfo.class.getSimpleName()+" where isDeleted = 0 ");
		if(ExStringUtils.isNotEmpty(unit)) {
			hql.append(" and branchSchool.resourceid =  '"+unit+"' ");
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			hql.append(" and grade.resourceid=  '"+grade+"' ");
		}
		if(ExStringUtils.isNotEmpty(studystatus)) {
			hql.append(" and studentStatus =      '"+studystatus+"' ");
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			hql.append(" and classic.resourceid = '"+classic+"' ");
		}
		if(ExStringUtils.isNotEmpty(major)) {
			hql.append(" and major.resourceid =   '"+major+"' ");
		}
//		hql.append(" and studentBaseInfo is not null and  (studentStatus  = '11' or studentStatus = '21')  "); //导出的应该是注册注册的
		hql.append(" and studentBaseInfo is not null and  studentStatus  in( '11','21','25')  "); //导出的应该是注册注册的
		results = studentInfoService.findByHql(hql.toString());
		List<StudentInfoVo> voResults = new ArrayList<StudentInfoVo>(0);
		for (StudentInfo studentInfo : results) {
			StudentInfoVo vo = new StudentInfoVo();
			vo.setStudentInfo(studentInfo);
			voResults.add(vo);
		}
	
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			List<String> filterColumnList = new ArrayList<String>();
		
			Field[] properties = StudentInfoVo.class.getDeclaredFields(); 
			for (Field field : properties) {
				String propertiesName = field.getName(); 
				if("serialVersionUID".equals(propertiesName)){
					continue;
				}
				String methodName = "get"+propertiesName.subSequence(0, 1).toString().toUpperCase()+propertiesName.substring(1, propertiesName.length());;
				System.out.println(propertiesName+":"+methodName);
				Method method = StudentInfoVo.class.getDeclaredMethod(methodName);
				System.out.println(methodName+"::"+method.invoke(studentInfoVo,null));
				if(null!=method.invoke(studentInfoVo, null) ){
					filterColumnList.add(propertiesName);
				}
			}
			//初始化参数
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeAttendAdvancedStudies");
				dictCodeList.add("CodeLearningStyle");
				dictCodeList.add("CodeStudyInSchool");
				dictCodeList.add("CodeStudentKind");
				dictCodeList.add("CodeEnterSchool");
				dictCodeList.add("CodeLearingStatus");
				dictCodeList.add("CodeStudentStatus");
				dictCodeList.add("CodeTeachingType");
				
				dictCodeList.add("CodeAccountStatus");
				dictCodeList.add("CodeAuditStatus");
				
				//baseinfo
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeCertType");
				dictCodeList.add("CodeBloodStyle");
				dictCodeList.add("CodeCountry");
				dictCodeList.add("CodeGAQ");
				dictCodeList.add("CodeNation");
				dictCodeList.add("CodeHealth");
				dictCodeList.add("CodeMarriage");
				dictCodeList.add("CodePolitics");
				dictCodeList.add("CodeRegisteredResidenceKind");
				dictCodeList.add("CodeWorkStatus");
				Map<String,Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE);
				//是否预约毕业论文
				dictMap.put("CodeAutitGCCustom_"+Constants.BOOLEAN_YES, "已预约");
				dictMap.put("CodeAutitGCCustom_"+Constants.BOOLEAN_NO, "未预约");
				//是否申请学位
				dictMap.put("CodeAuditGraduateCustom_"+Constants.BOOLEAN_YES, "申请学位+毕业");
				dictMap.put("CodeAuditGraduateCustom_"+Constants.BOOLEAN_NO, "申请毕业");
				dictMap.put("CodeAuditGraduateCustom_"+Constants.BOOLEAN_WAIT, "申请延迟毕业");
				//学生帐号状态
				dictMap.put("CodeAccountStatusCustom_1", "启用");
				dictMap.put("CodeAccountStatusCustom_0", "关闭");
				//学生预约状态
				dictMap.put("CodeAuditCustom_1", "启用");
				dictMap.put("CodeAuditCustom_0", "关闭");
				
				
				exportExcelService.initParmasByfile(disFile, "customInfo",voResults,dictMap,filterColumnList);//初始化配置参数
				exportExcelService.getModelToExcel().setHeader("自定义导出");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高			
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				fileup.downloadFile(response, "自定义导出"+".xls", excelFile.getAbsolutePath(),true);
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}	
	/**
	 * 查看学生毕业审核数据
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/view.html")
	public String stuChangeInfoView(String studentId ,HttpServletRequest request,ModelMap model) throws WebException{
		List<Map<String,Object>> studentinfos = new ArrayList<Map<String,Object>>(0);
		Map<String,Object> voMap = new HashMap<String, Object>(0);
		voMap.put("stus", "'"+studentId+"'");
		studentinfos = studentInfoChangeJDBCService.getStudentInfoForGraduateAudit(voMap);
		//新建一个审核结果的列表
		Map<String, Object> studentInfo=studentinfos.get(0);
		//限选课
		Integer lim_s = null!=studentInfo.get("finishedOptionalCourseNum")?Integer.parseInt(studentInfo.get("finishedOptionalCourseNum").toString()):0;//限
		Integer lim_t = null!=studentInfo.get("optionalCourseNum")?Integer.parseInt(studentInfo.get("optionalCourseNum").toString()):0;
	    //必修课
		Double nes_s = null!=studentInfo.get("finishedNecessCreditHour")?Double.parseDouble(studentInfo.get("finishedNecessCreditHour").toString()):0D;//必
		Double nes_t = null!=studentInfo.get("tpmust")?Double.parseDouble(studentInfo.get("tpmust").toString()):0D;
		//总分
		Double tol_s = null!=studentInfo.get("finishedCreditHour")?Double.parseDouble(studentInfo.get("finishedCreditHour").toString()):0L;//总
		Double tol_t = null!=studentInfo.get("minResult")?Double.parseDouble(studentInfo.get("minResult").toString()):0L;
		String nes_s_str = nes_s.toString().replace(".0","");
		String nes_t_str = nes_t.toString().replace(".0","");
		String tol_s_str = tol_s.toString().replace(".0","");
		String tol_t_str = tol_t.toString().replace(".0","");
		
		//延迟毕业
		String app_s = null!=studentInfo.get("isApplyGraduate")?studentInfo.get("isApplyGraduate").toString():""; //申
	    //入学资格
		String ent_s = null!=studentInfo.get("enterAuditStatus")?studentInfo.get("enterAuditStatus").toString():""; //入
	    //学籍状态
		String sta_s = null!=studentInfo.get("studentStatus")?studentInfo.get("studentStatus").toString():"";//学籍
		//毕业
		//Double length = Double.parseDouble(studentInfo.get("eduyear").toString().substring(0,3).replace("年", ""));//学制
		Double length = Double.parseDouble(studentInfo.get("eduyear").toString().replace("年", ""));//学制
		Double gra_s = Double.parseDouble(studentInfo.get("firstyear").toString())+(Long.parseLong(studentInfo.get("term").toString())-1L)*0.5;
		Double gra = gra_s+length-0.5;
		String resourceid = studentInfo.get("resourceid").toString();
		StudentInfo stuinfo = studentInfoService.get(resourceid);
		model.addAttribute("info", stuinfo);
		model.addAttribute("lim_s",lim_s );
		model.addAttribute("lim_t",lim_t );
		model.addAttribute("nes_s",nes_s_str );
		model.addAttribute("nes_t",nes_t_str );
		model.addAttribute("tol_s",tol_s_str);
		model.addAttribute("tol_t",tol_t_str );
		model.addAttribute("app_s",app_s );
		model.addAttribute("ent_s",ent_s );
		model.addAttribute("sta_s",sta_s );
		model.addAttribute("length",length );//年限
		model.addAttribute("gra",gra );
		
		return "/edu3/roll/graduationQualif/graduateaudit-view";
	}
	
	/**
	 * 浏览未发布成绩的课程
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/view-student-noExamResultCourse.html")
	public String viewNoExamResultCourse(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		List<Map<String, Object>> noExamResultList = new ArrayList<Map<String,Object>>();
		Map<String, Object> condition = new HashMap<String, Object>();
		try {
			do{
				String studentNo = ExStringUtils.trimToEmpty(request.getParameter("studentNo"));
				if(ExStringUtils.isEmpty(studentNo)){
					logger.info("所传参数中：学生的学号为空！");
					continue;
				}
				condition.put("studentNo", studentNo);
				condition.put("allCourse", "Y");
				noExamResultList = studentExamResultsService.findNoExamResultCourse(condition);
			}while(false);
		} catch (Exception e) {
			logger.error("浏览未录入成绩的课程出错", e);
		}
		model.addAttribute("condition", condition);
		model.addAttribute("noExamResultList", noExamResultList);
		return "/edu3/roll/graduationQualif/noExamResultCourse-view";
	}
	/**
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/exportExcel_new.html")
	public void doExportGraudateAuditExcel_new(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		List<StudentInfoVo> list = new ArrayList<StudentInfoVo>(0);//结果集
		//List<Map<String,Object>> tmpList = new ArrayList<Map<String,Object>>(0);
		//String exportType = ExStringUtils.trimToEmpty(request.getParameter("exportType"));//导出方式
		Map<String,Object> condition = new HashMap<String, Object>(0);
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		List<GraduationInfoExportVo>  tmpList = new ArrayList<GraduationInfoExportVo>();
		if("1".equals(isSelectAll)){
			String graduateAuditBeginTime = ExStringUtils.trimToEmpty(request.getParameter("graduateAuditBeginTime"));//毕业审核时间起
			String graduateAuditEndTime = ExStringUtils.trimToEmpty(request.getParameter("graduateAuditEndTime"));//毕业审核时间终
			if(ExStringUtils.isNotEmpty(graduateAuditBeginTime)&&ExStringUtils.isNotEmpty(graduateAuditEndTime)) {
				condition.put("dateDuration", "1");
				condition.put("graduateAuditBeginTime", graduateAuditBeginTime);
				condition.put("graduateAuditEndTime", graduateAuditEndTime);
			}
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
			String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
			String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
			String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
			String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
			String classId = ExStringUtils.trimToEmpty(request.getParameter("classId"));//班级
			String studentstatus= ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
			String auditStatus	   = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
			String confirmStatus = ExStringUtils.trimToEmpty(request.getParameter("confirmStatus"));
			String checkHis = ExStringUtils.trimToEmpty(request.getParameter("checkHis"));
			String isReachGraYear = ExStringUtils.trimToEmpty(request.getParameter("isReachGraYear"));//达到毕业最低年限
			String isPassEnter    = ExStringUtils.trimToEmpty(request.getParameter("isPassEnter"));//通过入学资格审核
			String isApplyDelay   = ExStringUtils.trimToEmpty(request.getParameter("isApplyDelay"));//申请延迟毕业
			String accountStatus  = ExStringUtils.trimToEmpty(request.getParameter("accoutstatus"));
			if(ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotEmpty(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotEmpty(name)) {
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(studyNo)) {
				condition.put("studyNo", studyNo);
			}
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			if(ExStringUtils.isNotEmpty(studentstatus)) {
				condition.put("studentstatus", studentstatus);
			}
			if(ExStringUtils.isNotEmpty(auditStatus)) {
				condition.put("auditStatus",auditStatus);
			}
			if(ExStringUtils.isNotEmpty(confirmStatus)) {
				condition.put("confirmStatus", confirmStatus);
			}
			if(ExStringUtils.isNotEmpty(isReachGraYear)) {
				condition.put("isReachGraYear", isReachGraYear);
			}
			if(ExStringUtils.isNotEmpty(isPassEnter)) {
				condition.put("isPassEnter", isPassEnter);
			}
			if(ExStringUtils.isNotEmpty(isApplyDelay)) {
				condition.put("isApplyDelay", isApplyDelay);
			}
			if(ExStringUtils.isNotEmpty(accountStatus)) {
				condition.put("accoutstatus", accountStatus);
			}
			if(ExStringUtils.isNotEmpty(classId)) {
				condition.put("classId", classId);
			}
			String currentYearInfo = getCurrentYear();
			condition.put("currentYearInfo", currentYearInfo);
			if(!"1".equals(checkHis)){
				condition.put("statusNeed", "1");
			}
			condition.put("exportGaudit", "Y");
			tmpList = studentGDAuditJDBCService.getGraduateAuditResults_new(condition);
		}else {
			String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
			condition.put("exportGaudit", "Y");
			String currentYearInfo = getCurrentYear();
			condition.put("currentYearInfo", currentYearInfo);
			//勾选审核的导出不需要statusNeed，因为之前是做了限制的
			condition.put("stus", stus.substring(0, stus.lastIndexOf(',')).replaceAll(",", "','"));
			tmpList = studentGDAuditJDBCService.getGraduateAuditResults_new(condition);
		}
		FileUploadAndDownloadSupportController fileup = new FileUploadAndDownloadSupportController();
		//文件输出服务器路径
		fileup.setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			if(0==tmpList.size()){
				throw new Exception("此查询条件下的数据为空。");
			}
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(fileup.getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
//			List<String> dictCodeList = new ArrayList<String>();
			
//			Map<String , Object> map1 = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
//			map1.put("CodeApplyGraduate_Y", "申请学位、毕业");
//			map1.put("CodeApplyGraduate_N", "申请毕业");
//			map1.put("CodeApplyGraduate_W", "延迟毕业");
//			map1.put("CodeEnterAudit_Y", "审核通过");
//			map1.put("CodeEnterAudit_N", "审核不通过");
//			map1.put("CodeEnterAudit_W", "待审核");
//			map1.put("CodeGraduateAudit_1", "审核通过");
//			map1.put("CodeGraduateAudit_0", "审核不通过");
//			map1.put("CodeGraduateAuditConfirm_#1", "已确定");
//			map1.put("CodeGraduateAuditConfirm_#0", "未确定");
			exportExcelService.initParmasByfile(disFile, "StudentGraduateAudit_new", tmpList,null);
			exportExcelService.getModelToExcel().setHeader("毕业审核结果表");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "毕业审核结果表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	/**
	 * 级联 年级-专业-班级 //学院2016修改
	 */
	@RequestMapping("/edu3/schoolroll/graduate/audit/list/grade-major-course/page1.html")
	public void gradeToMajorToCourse_page1(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String examsub =ExStringUtils.trimToEmpty(request.getParameter("examsub")); //年级
		Map<String,Object> map = new HashMap<String, Object>();
		String isMk="";
		if(ExStringUtils.isNotBlank(examsub)){
			ExamSub eb= examSubService.get(examsub);
			
			if("Y".equals(eb.getExamType())){
				isMk="Y";
			}
		}
		
		String unit =ExStringUtils.trimToEmpty(request.getParameter("unit")); //教学站
		String course =ExStringUtils.trimToEmpty(request.getParameter("course")); //控件name
		String rtmsg = graduationQualifService.getGradeToMajorToCourse(examsub,unit,course,isMk);
		map.put("msg", rtmsg);
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
}
