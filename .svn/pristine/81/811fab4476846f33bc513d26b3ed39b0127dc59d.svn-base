package com.hnjk.edu.roll.controller;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.finance.model.StudentPayment;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IStudentGDAuditJDBCService;
import com.hnjk.edu.roll.service.IStudentGraduateAndDegreeAuditService;
import com.hnjk.edu.roll.service.IStudentInfoChangeJDBCService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.service.impl.GraduateDataServiceImpl;
import com.hnjk.edu.roll.vo.StudentInfoVo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.StateExamResults;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IElectiveExamResultsService;
import com.hnjk.edu.teaching.service.IStateExamResultsService;
import com.hnjk.edu.teaching.service.IStudentExamResultsService;
import com.hnjk.edu.teaching.service.ITeachingActivityTimeSettingService;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;


/**
 * <code>GradeFeeRuleController</code><p>
 * 学位审核
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-4-2 下午10:34:32
 * @see 
 * @version 1.0
 */

@Controller
public class GraduationDegreeController extends BaseSupportController{

	private static final long serialVersionUID = 7695551016732690842L;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;//学生成绩服务
	

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
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
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;
	
	@Autowired
	@Qualifier("teachingActivityTimeSettingService")
	private ITeachingActivityTimeSettingService teachingActivityTimeSettingService;
	
	@Autowired
	@Qualifier("electiveExamResultsService")
	private IElectiveExamResultsService electiveExamResultsService;
	
/*
	@RequestMapping("/edu3/schoolroll/graduation/degree/list.html")
	public String degreeList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException{
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
		//过滤未毕业的条件 graduationQualifList
		//String graduationFilter=graduationQualifService.queryGraduationStr().toString();
		// 学籍表中学籍状态：16  毕业  13 退学  18学习期限已过 12 休学  11在学  20放弃入学资格
		String isThesis="16";
		if(ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
		if(ExStringUtils.isNotEmpty(major)) condition.put("major", major);
		if(ExStringUtils.isNotEmpty(classic)) condition.put("classic", classic);
		if(ExStringUtils.isNotEmpty(stuStatus)) condition.put("stuStatus", stuStatus);
		if(ExStringUtils.isNotEmpty(name)) condition.put("name", name);
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) condition.put("matriculateNoticeNo", matriculateNoticeNo);
		if(ExStringUtils.isNotEmpty(isThesis)) condition.put("isThesis", isThesis);
		condition.put("isApplyGraduate", Constants.BOOLEAN_YES);
		Page page = studentInfoService.findStuByCondition(condition, objPage);
		
		model.addAttribute("graduationQualifList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/roll/graduationdegree/graduationdegree-list";
	}
	*/
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
	 * 获得审核数据(暂废弃在GraduationQualifController中一个将毕业和学位的审核数据同时获得)
	 * resourceid为多个或单个学生
	 */
	/*
	@RequestMapping("/edu3/teaching/graduateaudit/auditdegree.html")
	public String beforeAuditdegree(Integer pageNum,String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model)throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>(0);
		String stus = resourceid.replaceAll("'", "");//这样就是id1,id2,id3这样的形式
		Integer totalCount =  stus.split(",").length;
		String[] id = stus.split(",");
		condition.put("stus", resourceid);
		//因为在之前的毕业资格审查的显示时已经对学生进行了筛选 所以不需要再次进行筛选
		Page page=new Page();
		page.setAutoCount(true);
		page.setTotalCount(totalCount);
		page.setPageSize(1);
		//page = graduationQualifService.findGraduateionQualifByCondition(condition, page);
		//page = graduatedataservice.findGraduateDataByCondition(condition, page);
		for(int i = 0 ;i<totalCount;i++){
			Map<String,Object> voMap = new HashMap<String, Object>(0);
			StudentInfo studentInfo = studentInfoService.get(id[i]);
			//学位审核要求
			//1、十个主干课成总分为700
			//2、毕业论文（设计）良好(含)
			//3、通过学位课程考试:1门外国语、1门专业基础课，2门专业课 专业基础课和专业课卷面成绩合格方视为通过
			voMap.put("studentId", id[i]);
			voMap.put("thisYear", this.getCurrentYear());
			List<Map<String,Object>> results = studentExamResultsService.studentToGraduatePublishedExamResultsList(voMap);
			Map<String,Object> unitResults = new HashMap<String, Object>(0);
			for (Map<String, Object> map2 : results) {
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
			boolean isPass = true;
			Set<TeachingPlanCourse> set = studentInfo.getTeachingPlan().getTeachingPlanCourses();
			//主干课总分
			Float sumScoreMainCourse = 0F;
			//主干课门数
			Integer sumNumMainCourse = 0;
			//专业课通过门数
			Integer majorNum = 0;
			//专业课通过情况
			boolean majorPass = false;
			//专业基础课通过情况 
			boolean majorBasicPass = false;
			//毕业设计成绩
			String gdScore = "";
			//外国语通过情况
			boolean flPass = false;
			for (TeachingPlanCourse teachingPlanCourse : set) {
				if("Y".equals(teachingPlanCourse.getIsMainCourse())){
					sumNumMainCourse++;
					if(unitResults.containsKey(teachingPlanCourse.getCourseId())){
						Float f = Float.valueOf(unitResults.get("integratedscore").toString());
						sumScoreMainCourse+=f;
					}else{
						isPass=false;//说明主干课没有成绩
						break;
					}
				}
				//专业基础课
				if("1500".equals(teachingPlanCourse.getCourseNature())){
					if(unitResults.containsKey(teachingPlanCourse.getCourseId())){
						Float f = Float.valueOf(unitResults.get("writtenScore").toString());
						if(f>=60){
							majorBasicPass = true;
						}
					}
				}
				//专业课
				if("1500".equals(teachingPlanCourse.getCourseNature())){
					if(unitResults.containsKey(teachingPlanCourse.getCourseId())){
						Float f = Float.valueOf(unitResults.get("writtenScore").toString());
						if(f>=60){
							majorNum++;
							if(majorNum==2){
								majorPass = true;
							}
						}
					}
				}
				//毕业设计  5分制
				if("2020".equals(teachingPlanCourse.getCourseNature())){
					if(unitResults.containsKey(teachingPlanCourse.getCourseId())){
						String score = unitResults.get("integratedscore").toString();
						if(score.equals("2514") || score.equals("4")){
							gdScore="良";
						}else if(score.equals("2515") || score.equals("5")){
							gdScore="优";
						}
					}
				}
				//外语 是哪些课程？
				if("ff8080811dcdd9d1011df01ee4264e9a".equals(teachingPlanCourse.getCourseId())
				 ||"FBECB1BCE7C8171EE030007F01001614".equals(teachingPlanCourse.getCourseId())){
					if(unitResults.containsKey(teachingPlanCourse.getCourseId())){
						Float score = Float.valueOf(unitResults.get("integratedscore").toString());
						if(score>=60){
							flPass=true;
						}
					}
				}
			}
			//主干课平均分
			Integer avgScoreMainCourse = Math.round(sumScoreMainCourse/sumNumMainCourse);
			
			page.setPageNum(i+1);
			List<StudentInfo> list = new ArrayList<StudentInfo>(0);
			list.add(studentInfo);
			page.setResult(list);
		}
		pageNum=(null==pageNum)?1:pageNum;
		page.setPageNum(pageNum);
		//stus传递审核时的resourceid组
		model.addAttribute("stus",stus);
		//resourceid传递跳转页时需要的resourceid
		model.addAttribute("resourceid",resourceid);
		model.addAttribute("graduateionDatas",page);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("condition", condition);
		//从字典中获取学位条件
		List<Dictionary> dictionList = CacheAppManager.getChildren("DegreeCondition");
		model.addAttribute("degreeList", dictionList);
		return "/edu3/roll/graduationdegree/graduationdegree-form";
//		if(ExStringUtils.isNotEmpty(resourceid)){
//			StudentInfo studentInfo = studentInfoService.get(resourceid);
//			//获取的实际获取的总学分  scores[]0]课程总学分  scores[1] 毕业论文分数
//			int [] scores={0,0};
//			 //scores=graduationQualifService.getStudenScore(studentInfo.getResourceid(),studentInfo.getTeachingPlan().getResourceid());
//			//SET这两个分数
//			model.addAttribute("studentInfo", studentInfo);
//			//实际获取的课程分数
//			//model.addAttribute("realityIntegratedScore",Integer.toString(scores[0])); 
//			//实际获取的毕业论文分数
//			//model.addAttribute("realityThesisScore",Integer.toString(scores[1]));
//			//从字典中获取学位条件
//			List<Dictionary> dictionList = CacheAppManager.getChildren("DegreeCondition");
//			model.addAttribute("degreeList", dictionList);
//			
//		}
//		return "/edu3/roll/graduationdegree/graduationdegree-form";
	}
	*/
	/**
	 * 本科生毕业待审核列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/DegreeAuditList.html")
	public String getDegreeAuditList(HttpServletRequest request,Page objPage,ModelMap model) throws WebException
	{
		objPage.setOrderBy("graduateDate");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String name=request.getParameter("name");//姓名
		String studyNo=request.getParameter("studyNo");//学号
		String grade=request.getParameter("grade");//年级
		String degreeEnglish=request.getParameter("degreeEnglish");//年级
		String havePhoto             = ExStringUtils.trimToEmpty(request.getParameter("havePhoto"));//是否有相片
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
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
		if(ExStringUtils.isNotEmpty(degreeEnglish)) {
			condition.put("degreeEnglish", degreeEnglish);
		}
		if(ExStringUtils.isNotEmpty(havePhoto)) {
			condition.put("havePhoto", havePhoto);
		}
		//待审核
		condition.put("forDegreeAudit", "1");
		//是教学中心的人员操作的话，只能看到自己教学中心的毕业学生
		String center = "show";
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			branchSchool = cureentUser.getOrgUnit().getResourceid();
			center = "hide";
		}
		condition.put("isgraduation","16");
		Page page = graduateDataService.findGraduateDataByCondition(condition, objPage);
		List<GraduateData> list = page.getResult();
		List<String> studentInfoResourceids = studentInfoChangeJDBCService.getAllStudentInfoId();
		List<String> studentInfoResourceidsWithoutBaseInfo =studentInfoChangeJDBCService.getStudentInfoIdWithoutBaseInfo();
		for (GraduateData tmp : list) {
			StudentInfo studentInfo = tmp.getStudentInfo();
			String resourceid = studentInfo.getResourceid();
			if(studentInfoResourceids.contains(resourceid)){
				tmp.setHasStuChange(true);
			}
			if (studentInfoResourceidsWithoutBaseInfo.contains(resourceid)) {
				tmp.setHasBaseInfo(false);
			}else{
				tmp.setHasBaseInfo(true);
			}
		}
		try {
			dealDegreeInfo(list);
		} catch (Exception e) {
			logger.debug("学位审核出错"+e.fillInStackTrace());
		}
		//获得是否使用了全选
		String checkall = ExStringUtils.trimToEmpty(request.getParameter("checkAllFlag_gdA"));//全选
		if(null!=checkall&&"1".equals(checkall)) {
			condition.put("checkAllFlag_gdA", "1");
		}else{
			condition.put("checkAllFlag_gdA", "0");
		}
		model.addAttribute("graduationDegree", page);
		model.addAttribute("condition", condition);
		model.addAttribute("showCenter", center);
		
		//学位条件做成了全局参数
		String choose_1_value  =  "";
		String choose_2_value  =  "";
		String choose_3_value  =  "";
		BigDecimal _avgCreditHour = BigDecimal.ZERO;
		BigDecimal bkInsteadof = BigDecimal.ZERO;
		try {
			choose_1_value  = CacheAppManager.getSysConfigurationByCode("degree.course.avg").getParamValue();
			_avgCreditHour = new BigDecimal(choose_1_value);
			_avgCreditHour.setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			logger.debug("未设置全局变量degree.course.avg;");
		}
		try {
			choose_2_value  = CacheAppManager.getSysConfigurationByCode("degree.insteadof.bk").getParamValue();
			bkInsteadof = new BigDecimal(choose_2_value);
			bkInsteadof.setScale(2, BigDecimal.ROUND_HALF_UP);
		} catch (Exception e) {
			logger.debug("未设置全局变量degree.insteadof.bk;");
		}
		try {
			choose_3_value  = CacheAppManager.getSysConfigurationByCode("degree.thepaper.score").getParamValue();
		} catch (Exception e) {
			logger.debug("未设置全局变量degree.thepaper.score;");
		}
		
		// 获取学校代码,用于针对不同学校在前端页面做不同的逻辑(如：显示不同的字段等)
		SysConfiguration configParam = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode");
		String schoolCode = "";
		if(configParam != null){
			schoolCode = configParam.getParamValue();
		}
		model.addAttribute("schoolCode", schoolCode);
		// 目前只有广东医使用绩点(转换为绩点)
		if(ExStringUtils.isNotEmpty(schoolCode) && "10571".equals(schoolCode)){
			choose_1_value = "0";
			choose_2_value = "0";
			if(_avgCreditHour != null && _avgCreditHour.doubleValue() >= 60){
				choose_1_value = String.valueOf(1+(_avgCreditHour.doubleValue()-60)/10);
			}
			if(bkInsteadof != null && bkInsteadof.doubleValue() >= 60){
				choose_2_value = String.valueOf(1+(bkInsteadof.doubleValue()-60)/10);
			}
		}
		model.addAttribute("choose_1_value", choose_1_value);
		model.addAttribute("choose_2_value", choose_2_value);
		model.addAttribute("choose_3_value", choose_3_value);
		return "/edu3/roll/graduationStudent/degreeaudit-list";
	}
	
	//处理学位信息
	private List<GraduateData> dealDegreeInfo(List<GraduateData> data) throws Exception{
		if(null != data && data.size() > 0){
			StudentInfo stu = null;
			Set<TeachingPlanCourse> tpc = null;
			for(GraduateData gd : data){
				stu = gd.getStudentInfo();
				tpc = gd.getStudentInfo().getTeachingPlan().getTeachingPlanCourses();
				gd.setAvg(getAvgScore(tpc,gd)); //平均分
				gd.setStudentRecord("");//赏罚记录 
				gd.setDegreeEnglish(getDegreeEnglish(stu,"name"));//学位英语
				gd.setThesisScore(getThesisScore(tpc,gd));//毕业论文成绩
				//TODO
			}
		}
		return data;
	}
	
	//平均分
	private Double getAvgScore(Set<TeachingPlanCourse> tpc,GraduateData gd){
		//判断是否需要计算平均分
		Double avg = 0d;
		BigDecimal bd = null;
		if(null != gd && (ExStringUtils.isBlank(gd.getCourseAvg()) || "0.0".equals(gd.getCourseAvg()))){			
			try {
				List<StudentExamResultsVo> list =  studentExamResultsService.studentExamResultsList(gd.getStudentInfo(),"audit",null);//所有成绩
				if(null != tpc && tpc.size() > 0){
					Double d = 0d;
					Double _d1 = 0d;
					Double _d2 = 0d;
					Map<String,StudentExamResultsVo> map = new HashMap<String, StudentExamResultsVo>();
					//先替代缓考成绩
					List<StudentExamResultsVo> hklist = new ArrayList<StudentExamResultsVo>();
					List<StudentExamResultsVo> rmlist = new ArrayList<StudentExamResultsVo>();//要删除的数据
					for(StudentExamResultsVo v : list){
						if("5".equals(v.getExamAbnormityCode())){//处理缓考
							hklist.add(v);//存缓考
						}
					}
					
					SysConfiguration guration = CacheAppManager.getSysConfigurationByCode("ExamResultCorrosion");//缓考替代全局参数
					if(null != hklist && hklist.size() > 0 && null != guration){
						String code = null == guration ? "" : guration.getParamValue();
						list.removeAll(hklist);
						for(StudentExamResultsVo hk : hklist){
							for(StudentExamResultsVo v : list){ //有缓考的要用其他成绩替换  list里要替代缓考成绩且删除这门课程的其他成绩
								if(!"5".equals(v.getExamAbnormityCode()) && hk.getCourseId().equals(v.getCourseId())){
									if(ExStringUtils.isNotBlank(code) && code.equals(v.getExamSubType())){//替换
										if(!"0".equals(v.getExamAbnormityCode())){// 替换后的成绩是非正常成绩则为0分
											v.setIntegratedScore("0");
										}
										hk.setIntegratedScore(v.getIntegratedScore());
										hk.setExamAbnormityCode("0");
									}
									//删除
									rmlist.add(v);
								}
							}
						}
						//增加
						list.removeAll(rmlist);//剔除缓考成绩课程的其他成绩
						list.addAll(hklist); //加入替换的缓考成绩
					}
					//是否将毕业论文这门课程的成绩包含在计算平均分的课程，默认为是
					String isCountThesis=CacheAppManager.getSysConfigurationByCode("isCountThesis").getParamValue();
					isCountThesis=ExStringUtils.isBlank(isCountThesis)?"Y":isCountThesis;
					for(TeachingPlanCourse pc : tpc){
						if(null != list && list.size() >0){
							if(Constants.BOOLEAN_NO.equals(isCountThesis)){
								if("thesis".equals(pc.getCourseType())){
									continue;//如果为全局参数为否并且为毕业论文，跳过;
								}
							}
							for(StudentExamResultsVo vo : list){
								if(pc.getCourse().getResourceid().equals(vo.getCourseId()) && 
										(("已发布".equals(vo.getCheckStatus()) || "免考".equals(vo.getCheckStatus()) || "免修".equals(vo.getCheckStatus()) || "代修".equals(vo.getCheckStatus()) || "Y".equals(vo.getIsunScore()) || "通过".equals(vo.getCheckStatus()))) 
										|| "选修课成绩".equals(vo.getWrittenScore())){ 
									//这个平时分是计算教学计划里的课程的平时分
									//System.out.println(vo.getCourseName()+"/"+vo.getCheckStatus()+"/"+vo.getIntegratedScore());

									Double _score = 0d;
									if(!"0".equals(vo.getExamAbnormityCode())){//非正常成绩  置为0
										vo.setIntegratedScore("0");
									}else if("统考课程".equals(ExStringUtils.trim(vo.getCourseType())) && ExStringUtils.isBlank(vo.getUsuallyScore()) && ExStringUtils.isBlank(vo.getWrittenScore())){//统考成绩鉴定
										try {
											_score = Double.parseDouble(JstlCustomFunction.dictionaryCode2Name("CodeStateExamResultsScore", vo.getIntegratedScore()));
										} catch (Exception e) {
											logger.debug("学位审核:未设置数据字典CodeStateExamResultsScore或者是数据字典CodeStateExamResultsScore没有"+vo.getIntegratedScore()+"这个值或者是"+vo.getIntegratedScore()+"不能转换成double类型;"+e.getStackTrace());
										}
										vo.setIntegratedScore(_score+"");
									}else if(ExStringUtils.isNotBlank(vo.getExamResultsChs())){ //优良中及格不及格
										try {
											_score = Double.parseDouble(JstlCustomFunction.dictionaryCode2Name("degreePointsCode", vo.getExamResultsChs()));
										} catch (Exception e) {
											logger.debug("学位审核:未设置数据字典degreePointsCode或者是数据字典degreePointsCode没有"+vo.getExamResultsChs()+"这个值或者是"+vo.getExamResultsChs()+"不能转换成double类型;"+e.getStackTrace());
											_score = Double.parseDouble(vo.getIntegratedScore());
										}
										vo.setIntegratedScore(_score+"");
									}
									if("Y".equals(vo.getIsMakeupExam()) || "T".equals(vo.getIsMakeupExam()) || "Q".equals(vo.getIsMakeupExam())) {//补考成绩
										Double d1 = 0d;
										
										d1 = Double.parseDouble(vo.getIntegratedScore());
										try {
											_score = Double.parseDouble(JstlCustomFunction.getSysConfigurationValue("degree.insteadof.bk", "server"));
										} catch (Exception e) {
											logger.debug("学位审核:未设置全局参数degree.insteadof.bk或者是全局参数degree.insteadof.bk的值不能转换成double类型;"+e.getStackTrace());
										}
										if(d1>60){ //补考成绩合格要被替换成绩
											vo.setIntegratedScore(_score+"");
										}
									}
									if(null != map && map.size() > 0 && map.containsKey(vo.getCourseId())){
										_d1 = Double.parseDouble(vo.getIntegratedScore());
										_d2 = Double.parseDouble(map.get(vo.getCourseId()).getIntegratedScore());
										if(_d1>_d2){
											map.put(vo.getCourseId(), vo);
										}
									}else{
										map.put(vo.getCourseId(), vo);
									}
								}
							}
						}
					}
					_d1 = 0d;
					if(null != map && map.size() > 0){
						for(String stvo : map.keySet()){
							//System.out.println("=="+stvo+"/"+map.get(stvo).getCourseName()+"/"+map.get(stvo).getIntegratedScore());
							_d1 = Double.parseDouble(map.get(stvo).getIntegratedScore());
							d +=_d1;
						}
						avg = d/map.size(); //平均分
					}
					bd = new BigDecimal(avg);
					//update平均分
					jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update("update EDU_TEACH_GRADUATEDATA d set d.COURSEAVG = '"+bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue()+"' where d.resourceid = '"+gd.getResourceid()+"' ");
				}
			} catch (Exception e) {
				logger.debug("学位审核:计算平均分出错{graduateDataid:"+gd.getResourceid()+";studentInfoid:"+gd.getStudentInfo().getResourceid()+"}"+e.getStackTrace());
				e.printStackTrace();
			}
			return  bd.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		}else{
			try {
				avg = Double.parseDouble(gd.getCourseAvg());
			} catch (Exception e) {
				logger.debug("学位审核:计算平均分出错"+e.getStackTrace());
			}
			return avg;
		}
	}
	
	//学位英语
	private String getDegreeEnglish(StudentInfo stu,String type){
		String english = "";
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("no", stu.getStudyNo());
			StateExamResults stas = null;
			List<StateExamResults> list = stateExamResultsService.findByHql(" from "+StateExamResults.class.getSimpleName()+" where isDeleted = 0 and isIdented = 'Y' and studentInfo.studyNo = :no ",map);
			if(null != list && list.size()>0) {
				stas = list.get(0); //学位英语只有一门
			}
			if("name".equals(type)){ //取课程
				english = null != stas ? stas.getCourse().getCourseName() : "";
			}else if("score".equals(type)){ //取成绩
				english = null != stas ? stas.getScoreType() : "";
			}
		} catch (Exception e) {
			logger.debug("学位审核：获取学位英语出错"+e.getStackTrace());
		}
		return english;
	}
	
	//判断是否是应届本科毕业生
	private Boolean isGradutionStu(StudentInfo stu){
		Boolean flag = false;
		try {
			if("16".equals(stu.getStudentStatus())){//毕业
				//教学活动时间（本届毕业时间范围）
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("mainProcessType", "graduateDate");
				// TODO:三年制，以后如果有其他年制要根据需求修改
				map.put("fistYear", stu.getGrade().getYearInfo().getFirstYear()+2);
				Calendar today = Calendar.getInstance();
				String term = "1";
				if(today.get(Calendar.MONTH) >= 8){
					term = "2";
				}
				map.put("term",term);
				List<TeachingActivityTimeSetting> list = teachingActivityTimeSettingService.findTeachingActivityTimeSettingByCondition(map);
				if(null != list && list.size() > 0){
					long time_poor = ExDateUtils.dateDiff("second", new Date(), list.get(0).getEndTime());
					if(time_poor < 0L){
						flag = true;
					}
				}else{
					logger.info("未在教学计划里设置本届毕业生学位审核时间范围;或者未在字典CodeTeachingActivity添加值为graduateDate的毕业日期类型");
				}		
			}
		} catch (Exception e) {
			logger.error("学位审核：判断是否是应届本科毕业生出错",e);
		}
		return flag;
	}
	
	//获取毕业论文成绩
	private String getThesisScore(Set<TeachingPlanCourse> tpc,GraduateData gd){
		String score = "";
		if(null != gd && ExStringUtils.isBlank(gd.getGraduationthesisscore())){
			try {
				List<StudentExamResultsVo> list =  studentExamResultsService.studentExamResultsList(gd.getStudentInfo(),null);//所有成绩
				if(null != tpc && tpc.size() > 0){
					for(TeachingPlanCourse pc : tpc){
						if(null != list && list.size() >0){
							for(StudentExamResultsVo vo : list){
								if(pc.getCourse().getResourceid().equals(vo.getCourseId()) && "已发布".equals(vo.getCheckStatus()) && "thesis".equals(pc.getCourseType())){
									if(ExStringUtils.isNotBlank(vo.getExamResultsChs())){ //优良中及格不及格
										score = vo.getExamResultsChs();
									}else{
										score = vo.getIntegratedScore();
									}						
								}
							}
						}
					}
				}
				//update毕业论文分数
				jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().update("update EDU_TEACH_GRADUATEDATA d set d.graduationthesisscore = '"+score+"' where d.resourceid = '"+gd.getResourceid()+"' ");
			} catch (Exception e) {
				logger.debug("学位审核:获取毕业论文成绩出错"+e.getStackTrace());
				e.printStackTrace();
			}
		}else{
			score = gd.getGraduationthesisscore();
		}
		return score;
	}
	
	/**
	 * 得到待审核的条数
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/getDegreeAuditNum.html")
	public void getDegreeAuditNum(HttpServletRequest request,HttpServletResponse response,Page objPage,ModelMap model) throws WebException
	{
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		int total = 0;
		if("1".equals(isSelectAll)){
			objPage.setOrderBy("graduateDate");
			//查询条件
			Map<String,Object> condition = new HashMap<String,Object>();
			
			String branchSchool =ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String major		=ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classic		=ExStringUtils.trimToEmpty(request.getParameter("classic"));
			String stuStatus	=ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
			String name		    =ExStringUtils.trimToEmpty(request.getParameter("name"));
			String matriculateNoticeNo	=ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
			String grade 		=ExStringUtils.trimToEmpty(request.getParameter("grade"));
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
				condition.put("studyNo", matriculateNoticeNo);
			}
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			//待审核
			condition.put("forDegreeAudit", "1");
			Page page = graduateDataService.findGraduateDataByCondition(condition, objPage);
			total = page.getTotalCount();
		}else{
			String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
			total = stus.split(",").length;
		}
		
		Map<String,Object> map =new HashMap<String,Object>(0);
		map.put("total", total);
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 学位审核(最新)
	 * @param stus
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/roll/graduateaudit/viaDegree.html")
	public String viaGraduate(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage) throws Exception{
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		String doAudit     = ExStringUtils.trimToEmpty(request.getParameter("doAudit"));
		Map<String, Object> condition = new HashMap<String, Object>(0);
		List<Map<String,Object>> graduationInfos = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> courseInfos = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> resultsTotal = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> resultsTotal1 = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> resultsTotal2 = new ArrayList<Map<String,Object>>(0);
		boolean isFirstTimeOpen = "Y".equals(doAudit);
		condition.put("biye", "Y");
		condition.put("benke", "Y");
		if("1".equals(isSelectAll)){//如果是全选是按照条件查询得到待审核的名单
			String branchSchool =ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String major		=ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classic		=ExStringUtils.trimToEmpty(request.getParameter("classic"));
			String stuStatus	=ExStringUtils.trimToEmpty(request.getParameter("stuStatus"));
			String name		    =ExStringUtils.trimToEmpty(request.getParameter("name"));
			String matriculateNoticeNo	=ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));
			String grade 		=ExStringUtils.trimToEmpty(request.getParameter("grade"));
			String degreeEnglish 		=ExStringUtils.trimToEmpty(request.getParameter("degreeEnglish"));
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
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			if(ExStringUtils.isNotEmpty(degreeEnglish)) {
				condition.put("degreeEnglish", degreeEnglish);
			}
			condition.put("isSelectAll", "1");
			//d1= new Date();
			graduationInfos = studentInfoChangeJDBCService.getStudentInfoForDegreeAudit(condition);
			//d2 = new Date();
			courseInfos     = studentInfoChangeJDBCService.getInfoForDegreeAudit(condition);
			if(isFirstTimeOpen){
				resultsTotal1 = studentExamResultsService.studentToGraduatePublishedExamResultsList(condition);
				resultsTotal2 = studentExamResultsService.studentToGraduateNoExamResultsList(condition);
				resultsTotal1.addAll(resultsTotal2);
				resultsTotal = resultsTotal1;
			}
			//d3 = new Date();
		}else{//如果不是使用多选，则按传入的id值确定待审核的学生名单
			Map<String,Object> voMap = new HashMap<String, Object>(0);
			String[] resourceids = stus.split(",");
			StringBuffer stusCon = new StringBuffer(); 
			for (String resourceid : resourceids) {
				stusCon.append("'"+resourceid+"',");
			}
			voMap.put("stus", stusCon.deleteCharAt(stusCon.length()-1));
			graduationInfos = studentInfoChangeJDBCService.getStudentInfoForDegreeAudit(voMap);
			courseInfos     = studentInfoChangeJDBCService.getInfoForDegreeAudit(voMap);
			if(isFirstTimeOpen){
				resultsTotal1 = studentExamResultsService.studentToGraduatePublishedExamResultsList(voMap);
				resultsTotal2 = studentExamResultsService.studentToGraduateNoExamResultsList(voMap);
				resultsTotal1.addAll(resultsTotal2);
				resultsTotal = resultsTotal1;
			}
			
		}
		//新学位审核
		/*1.应届本科毕业生，
		 * 2.全部课程总评成绩的平均分在70分以上（含70分、补考合格的课程按60分计算），
		 * 3. 毕业论文成绩中等以上（含中等，75分及以上），
		 * 4.学位外语统考，成绩合格
		 * 5.是否提交学位审核材料
		审核通过的毕业生，则记录学位状态为“已获得”；审核不通过的毕业生，记录学位状态为“未获得”；初始是“待审核”
		其中，等级成绩换算规则为：优/良/中/及格/不及格 = 95/ 85 /75/ 65/ 50*/
		/**
		 * 广东医学位审核条件：
		 * 1.正常毕业
		 *	2.通过学位英语考试
		 *	3.平均学分绩点2.0以上（含2.0）
		 */
		String  isGraduationStu 	= 	ExStringUtils.trimToEmpty(request.getParameter("isGraduationStu")); //条件1
		String  graduationAvg 		= 	ExStringUtils.trimToEmpty(request.getParameter("graduationAvg")); //条件2
		String  thesisScore 		= 	ExStringUtils.trimToEmpty(request.getParameter("thesisScore")); //条件3
		String  degreeScore 		= 	ExStringUtils.trimToEmpty(request.getParameter("degreeScore")); //条件4
		String  hasDegreeMate 		= 	ExStringUtils.trimToEmpty(request.getParameter("hasDegreeMate")); //条件5
		String  schoolCode 		= 	ExStringUtils.trimToEmpty(request.getParameter("schoolCode")); // 学校代码
		
		condition.put("isGraduationStu", isGraduationStu);
		condition.put("graduationAvg", graduationAvg);
		condition.put("thesisScore", thesisScore);
		condition.put("degreeScore", degreeScore);
		condition.put("hasDegreeMate", hasDegreeMate);
		
		String  mainC = String.valueOf(request.getParameter("mainC"));
		String  graduateC = String.valueOf(request.getParameter("graduateC"));
		String  flC = String.valueOf(request.getParameter("flC"));
		String dmbaseC = ExStringUtils.trimToEmpty(request.getParameter("dmbaseC"));
		String dmain1C = ExStringUtils.trimToEmpty(request.getParameter("dmain1C"));
		String dmain2C = ExStringUtils.trimToEmpty(request.getParameter("dmain2C"));
		
		condition.put("mainC", mainC);
		condition.put("graduateC", graduateC);
		condition.put("flC", flC);
		condition.put("dmbaseC", dmbaseC);
		condition.put("dmain1C", dmain1C);
		condition.put("dmain2C", dmain2C);
		
		if(null!=stus&&!"1".equals(isSelectAll)) {
			condition.put("stus", stus);
		}
		Integer passCount = 0;
		Integer unPassCount = 0;
		//新建一个审核结果的列表
		List<String[]> list = new ArrayList<String[]>(0);
		int count =0 ;
		int pageNum1 = (objPage.getPageNum()-1);
		int begin = pageNum1*20;
		int end   = (pageNum1+1)*20;
		
		//Date date1= new Date();
		for (Map<String,Object> gInfo : graduationInfos) {
			if(!isFirstTimeOpen){
				if(count<begin){
					continue;
				}
				if(count>=end){
					break;
				}
			}
			String[] str  = new String[6];//记录审核情况
			//String teachingPlanId = ExStringUtils.trimToEmpty(String.valueOf(gInfo.get("teachplanid")));//教学计划id
			String studentInfoId = ExStringUtils.trimToEmpty(String.valueOf(gInfo.get("resourceid")));//学籍id
			String studyno = ExStringUtils.trimToEmpty(String.valueOf(gInfo.get("studyno")));//学号
			String stuName = ExStringUtils.trimToEmpty(String.valueOf(gInfo.get("studentname")));//姓名
			
			String degreeStatus = ExStringUtils.trimToEmpty(String.valueOf(gInfo.get("degreeStatus")));//学位状态
					
			StringBuffer unAuditReason  = new StringBuffer();
			StringBuffer reason = new StringBuffer();
			
			
			Map<String,Object> voMap = new HashMap<String, Object>(0);
			voMap.put("studentId", "'"+studentInfoId+"'");
			List<Map<String,Object>> results = new ArrayList<Map<String,Object>>(0);
			if(isFirstTimeOpen){//如果是第一次，要从所有的成绩中遍历:假设需要审核600条信息，第一次需要得到600条的审核记录，而在分页时，只需得到其中当前分页的学生的审核记录即可	
				for (Map<String,Object> map : resultsTotal) {
					if(studentInfoId.equals(map.get("studentid").toString())){
						results.add(map);
					}
				}
			}else{
				resultsTotal1 = studentExamResultsService.studentToGraduatePublishedExamResultsList(voMap);
				resultsTotal2 = studentExamResultsService.studentToGraduateNoExamResultsList(voMap);
				resultsTotal1.addAll(resultsTotal2);
				results= resultsTotal1;
			}
		   //TODO
			StudentInfo stu = studentInfoService.get(studentInfoId); //学籍信息
			Set<TeachingPlanCourse> tpc = stu.getTeachingPlan().getTeachingPlanCourses();
			List<StudentExamResultsVo> pclist = studentExamResultsService.studentExamResultsList(stu,null);//所有成绩
			//1.应届本科毕业生，改为是否毕业生（20171211）
			Boolean result1 = isGradutionStu(stu); //结果1
			//Boolean result1 = "16".equals(stu.getStudentStatus());//毕业
			
			//2.全部课程全部课程总评成绩的平均分在70分以上（含70分、补考合格的课程按60分计算）
			//拿出学位信息
			List<GraduateData> glist = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where isDeleted = ? and studentInfo.resourceid = ?",0,stu.getResourceid());
			GraduateData gd = null != glist ? glist.get(0) : null;
			Double result2 = getAvgScore(tpc,gd);//结果2
			
			//3. 毕业论文成绩中等以上（含中等，70分及以上）
			String result3_str = getThesisScore(tpc,gd);
			Double result3 = 0D;
			try {
				if(ExStringUtils.isNotEmpty(result3_str)){
					result3 = Double.parseDouble(result3_str);
				}
			} catch (NumberFormatException ne) {
				logger.error("毕业分数格式错误"+result3_str);
			}
			
			//4.学位外语统考，成绩合格
			String  result4_str = getDegreeEnglish(stu,"score");
			Boolean result4 = "0".equals(result4_str)?true:false;

			//5.是否提交学位审核材料
			String return_5 = "";
			if (ExStringUtils.isNotBlank(hasDegreeMate) && !"undefined".equals(hasDegreeMate)) {
				String hasDegreeMaterials = stu.getHasDegreeMaterials();
				Boolean result5 = true;
				if (!"Y".equals(hasDegreeMaterials) && "true".equals(hasDegreeMate)) {
					result5 = false;
				}
				return_5 = result5?"<font color='blue'>学位审核材料已提交;</font>":"<font color='red'>学位审核材料未提交;</font>";
			}


			//审核结果处理..
			String return_1 = result1?"<font color='blue'>是应届本科毕业生;</font>":"<font color='red'>非应届本科毕业生;</font>";
			if("10571".equals(schoolCode)){
				return_1 = result1?"<font color='blue'>是正常毕业;</font>":"<font color='red'>非正常毕业;</font>";
			}
			if("true".equals(isGraduationStu)){
				unAuditReason.append(return_1);
			}else{
				reason.append(return_1);
			}
			
			Double return_d2 = 0d;
			try {
				return_d2 = Double.parseDouble(JstlCustomFunction.getSysConfigurationValue("degree.course.avg", "server"));
			} catch (Exception e) {
				logger.debug("学位审核:未设置全局参数degree.course.avg或者是全局参数degree.course.avg的值不能转换成double类型;"+e.getStackTrace());
			}
			String return_2 = result2 < return_d2 ? "<font color='red'>总评成绩的平均分未达到要求;</font>":"<font color='blue'>总评成绩的平均分达到要求;</font>";
			if("10571".equals(schoolCode)){
				return_2 = result2 < return_d2 ? "<font color='red'>平均学分绩点未达到要求;</font>":"<font color='blue'>平均学分绩点达到要求;</font>";
			}
			if("true".equals(graduationAvg)){
				unAuditReason.append(return_2);
			}else{
				reason.append(return_2);
			}
			
			if(!"10571".equals(schoolCode)){
				Double return_d3 = 0d;
				try {
					return_d3 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("degree.thepaper.score").getParamValue());
					//Double.parseDouble(JstlCustomFunction.dictionaryCode2Name("degreePointsCode","中"));
				} catch (Exception e) {
					logger.debug("学位审核:未设置数据字典degreePointsCode或者是数据字典degreePointsCode没有[中]这个值或者值不能转换成double类型;"+e.getStackTrace());
				}
				String return_3 = result3 < return_d3 ? "<font color='red'>毕业论文成绩未达到要求;</font>":"<font color='blue'>毕业论文成绩达到要求;</font>";
				if("true".equals(thesisScore)){
					unAuditReason.append(return_3);
				}else{
					reason.append(return_3);
				}
			}
			
			String return_4 = result4?"<font color='blue'>学位外语合格;</font>":"<font color='red'>学位外语不合格;</font>";
			if("true".equals(degreeScore)){
				unAuditReason.append(return_4);
			}else{
				reason.append(return_4);
			}

			if ("true".equals(hasDegreeMate)) {
				unAuditReason.append(return_5);
			}else {
				reason.append(return_5);
			}
			if(unAuditReason.toString().contains("<font color='red'>")){
				str[3]="0";//审核状态
				unPassCount++;
			}else{
				str[3]="1";
				passCount++;
			}
			str[0]=studentInfoId;//studentInfo resourceid
			str[1]=studyno;//学号
			str[2]=stuName;//姓名
			str[4]=unAuditReason.toString();
			str[5]=reason.toString();
			list.add(str);
		}	
		//Date date2= new Date();	
		if(isFirstTimeOpen){//如果是第一次进入审核页面，则记录审核结果，分页不记录审核结果
			List<String> toUpdate = new ArrayList<String>(0);//更新
			List<String> toInsert = new ArrayList<String>(0);//对于已经进入到毕业生库而没有经过毕业审核的学生
			List<Map<String, Object>> tmpRes  = studentGDAuditJDBCService.getStudentIdInAudit(null);
			List<String> stuIds = new ArrayList<String>(0);
			for (Map<String, Object> map : tmpRes) {
				stuIds.add(map.get("STUDENTINFOID").toString());
			}
			for (String[] strings : list) {
				String id = strings[0];//学籍id
				Integer status = Integer.parseInt(strings[3]);
				//不通过原因
				if(strings[4]==null||"".equals(strings[4])) {
					strings[4]=" ";
				}
				//实际情况备注
				if(strings[5]==null||"".equals(strings[5])) {
					strings[5]=" ";
				}
				String memo = strings[4]+"|"+strings[5]+"|"+"#0";
				if(!"已获得学位".equals(strings[5])){
					if (stuIds.contains(id)) {
						toUpdate.add(id+":"+status+":"+memo);
					}else{
						//针对那些已经在毕业生库却没有经过毕业资格审核的数据
						toInsert.add(id+":"+status+":"+memo+":1: |毕业生库老数据 |#1|11|启用");
					}
					
				}
			}
			List<Object[]> params = new ArrayList<Object[]>();
			if(0 < toUpdate.size()){
				params = new ArrayList<Object[]>(0);
				for(String infomation : toUpdate){
					List<Object> objList = new ArrayList<Object>();
					objList.add(infomation.split(":")[1]);//degreeauditstatus
					objList.add(infomation.split(":")[2]);//degreeauditmemo		
					objList.add(infomation.split(":")[0]);//studentinfoid	
					params.add(objList.toArray());
				}
				jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("update edu_roll_stuaudit t set t.version=t.version+1 ," +
						" t.degreeauditstatus=?,t.degreeauditmemo= ?  where t.studentinfoid=? " , params);
			}
			if(0 < toInsert.size()){
				params = new ArrayList<Object[]>(0);
				GUIDUtils.init();
				for(String infomation : toInsert){
					List<Object> objList = new ArrayList<Object>();
					objList.add(GUIDUtils.buildMd5GUID(false));//resourceid
					objList.add(infomation.split(":")[3]);//graduateauditstatus
					objList.add(infomation.split(":")[4]);//graduateauditmemo	
					objList.add(infomation.split(":")[1]);//degreeauditstatus
					objList.add(infomation.split(":")[2]);//degreeauditmemo		
					objList.add(infomation.split(":")[0]);//studentinfoid	
					objList.add(0L);
					objList.add(0);			
					params.add(objList.toArray());
				}
				jdbcDao.getBaseJdbcTemplate().getJdbcTemplate().batchUpdate("insert into edu_roll_stuaudit (resourceid,graduateauditstatus,graduateauditmemo,degreeauditstatus,degreeauditmemo,studentinfoid,VERSION,ISDELETED) " +
						" values (?,?,?,?,?,?,?,?)", params);
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
		if(null!=list&&list.size()>0){
			Integer num = 0;
			if(isFirstTimeOpen){
				num = graduationInfos.size();//
				condition.put("totalCount", num);
			}else{
				num = Integer.valueOf(request.getParameter("totalCount").toString());//noAudit是因为是专科而未审核的
				condition.put("totalCount", num);
			}
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
			for (int i =0 ;i<list.size()&&i<100 ;i++) {
//				if(num>(pageSize*(pageNum-1)+i))
//				tmpList.add(list.get(pageSize*(pageNum-1)+i));
				tmpList.add(list.get(i));
			}
			
			for (String[] str : tmpList) {
				StudentInfoVo infoVo = new StudentInfoVo();
				infoVo.setAccountStatus(str[1]);//学号
				infoVo.setBloodType(str[2]);//姓名
				infoVo.setBornAddress(str[3]);//审核状态
				infoVo.setBornDay(str[4]);//不通过原因
				infoVo.setCertNum(str[5]);//实际情况
				finalList.add(infoVo);
			}
			objPage.setResult(finalList);
		}else{
			objPage.setAutoCount(true);
			objPage.setPageNum(pageNum);
			objPage.setTotalCount(0);
			objPage.setResult(finalList);
			model.addAttribute("pageTotal", 0);
		}
		
		if("Y".equals(doAudit)){
			condition.put("pass", passCount);
			condition.put("unpass", unPassCount);
		}else{
			condition.put("pass", request.getParameter("pass"));
			condition.put("unpass", request.getParameter("unpass"));
		}
		/*Date date3= new Date();
		
		System.out.println("1查询："+(d2.getTime()-d1.getTime()));
		System.out.println("2查询："+(d3.getTime()-d2.getTime()));
		System.out.println("查询使用："+(date1.getTime()-date.getTime()));
		System.out.println("循环使用："+(date2.getTime()-date1.getTime()));
		System.out.println("分页使用："+(date3.getTime()-date2.getTime()));
		*/
		StringBuffer operateContent = new StringBuffer();
		operateContent.append("学位审核：");
		operateContent.append("审核条件：isGraduationStu："+isGraduationStu);
		operateContent.append("||graduationAvg："+graduationAvg+":"+JstlCustomFunction.getSysConfigurationValue("degree.course.avg", "server"));
		operateContent.append("||thesisScore："+thesisScore+":"+CacheAppManager.getSysConfigurationByCode("degree.thepaper.score").getParamValue());
		operateContent.append("||degreeScore："+degreeScore);
		operateContent.append("||审核人数："+("1".equals(isSelectAll) ?"全选":"resourceids:"+stus));
		UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),"3",  UserOperationLogs.PASS, operateContent.toString());
		model.addAttribute("condition",condition);
		model.addAttribute("auditDegreeResults",objPage );
		return "/edu3/roll/graduationStudent/degreeAuditResults";
		
	}
	/**
	 * 查看学位审核结果(历史记录页面)
	 * @param request
	 * @param response
	 * @param model
	 * @param objPage
	 * @return
	 */
	@RequestMapping("/edu3/schoolroll/graduation/student/confirmDegreePage.html")
	public String enterConfirmDegreeAuditPage(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage){
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
		String pNum  = ExStringUtils.trimToEmpty(request.getParameter("pNum"));//页数
		String graduateDate  = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));//毕业时间
		String isRefresh  = ExStringUtils.trimToEmpty(request.getParameter("isRefresh"));//毕业时间
		String auditStatus	   = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
		String confirmStatus = ExStringUtils.trimToEmpty(request.getParameter("confirmStatus"));
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
		if(ExStringUtils.isNotEmpty(auditStatus)) {
			condition.put("auditStatus", auditStatus);
		}
		if(ExStringUtils.isNotEmpty(confirmStatus)) {
			condition.put("confirmStatus", confirmStatus);
		}
		Page page = new Page();
		if("1".equals(isRefresh)){
			objPage.setPageNum(Integer.parseInt(pNum));
		}else{
			pNum=Integer.toString(objPage.getPageNum());
		}		
		//增加是毕业筛选的标记
		page = studentGraduateAndDegreeAuditService.findDegreeAuditByCondition(condition, objPage);
		
		List<StudentGraduateAndDegreeAudit> slist=new ArrayList<StudentGraduateAndDegreeAudit>();
		List<GraduateData> glist=new ArrayList<GraduateData>();
		for (Object o : page.getResult()) {
			Object[] oo=(Object[])o;
			GraduateData g=(GraduateData)oo[1];
			//GraduateData g=graduateDataService.get(s.getGraduateData().getResourceid());
			slist.add((StudentGraduateAndDegreeAudit)oo[0]);
			glist.add(g);
			
			
		}
		try {
			glist=dealDegreeInfo(glist);
			for (GraduateData gg : glist) {
				for (StudentGraduateAndDegreeAudit s : slist) {
					if(gg.getResourceid().equals(s.getGraduateData().getResourceid())){
						s.setGraduateData(gg);
					}
				}
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		// 获取学校代码,用于针对不同学校在前端页面做不同的逻辑(如：显示不同的字段等)
		SysConfiguration configParam = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode");
		String schoolCode = "";
		if(configParam != null){
			schoolCode = configParam.getParamValue();
		}
		model.addAttribute("schoolCode", schoolCode);
		if(ExStringUtils.isNotEmpty(pNum)) {
			condition.put("pNum", pNum);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDate", graduateDate);
		}
		model.addAttribute("adlist", slist);
		model.addAttribute("degreeAuditList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/roll/graduationStudent/degreeAuditHistory";
	}
	
	/**
	 * 编辑审核实际情况
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/schoolroll/graduation/student/editDegreeAuditHistory.html")
	public String viewStudentInfop(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("editDegreeAuditHistoryId");//要编辑的学位审核记录ID
		if(ExStringUtils.isNotEmpty(resourceid)){
			StudentGraduateAndDegreeAudit degree = studentGraduateAndDegreeAuditService.get(resourceid);
			model.put("editDegreeAuditHistory", degree);
		}
		return "/edu3/roll/graduationStudent/editDegreeAuditHistory-view";		
	}
	
	/**
	 * 保存编辑审核实际情况
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/schoolroll/graduation/student/saveDegreeAuditHistory.html")
	public void saveviewStudentInfop(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("saveDegreeAuditHistoryId");//要编辑的学位审核记录ID
		String memo0 = request.getParameter("eduitDegreeHistory0");
		String memo1 = request.getParameter("eduitDegreeHistory1");
		String memo2 = request.getParameter("eduitDegreeHistory2");
		Map<String,Object> map = new HashMap<String, Object>();
		String statusCode = "200";
		String message = "保存成功!";
		try {
			if(ExStringUtils.isNotEmpty(resourceid)){
				StudentGraduateAndDegreeAudit degree = studentGraduateAndDegreeAuditService.get(resourceid);
				String str = memo0+"|"+memo1+"|"+memo2;
				degree.setDegreeAuditMemo(str);
				studentGraduateAndDegreeAuditService.update(degree);
				model.put("editDegreeAuditHistory", degree);
			}
		} catch (Exception e) {
			e.printStackTrace();
			statusCode = "300";
			message = "保存失败";
		}
		
		map.put("statusCode", statusCode);
		map.put("message", message);	
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	
	//@RequestMapping("/edu3/schoolroll/graduation/student/confirmDegreePage.html")
	public String enterConfirmDegreeAuditPage1(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage){
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
		String pNum  = ExStringUtils.trimToEmpty(request.getParameter("pNum"));//页数
		String graduateDate  = ExStringUtils.trimToEmpty(request.getParameter("graduateDate"));//毕业时间
		String isRefresh  = ExStringUtils.trimToEmpty(request.getParameter("isRefresh"));//毕业时间
		String auditStatus	   = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
		String confirmStatus = ExStringUtils.trimToEmpty(request.getParameter("confirmStatus"));
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
		if(ExStringUtils.isNotEmpty(auditStatus)) {
			condition.put("auditStatus", auditStatus);
		}
		if(ExStringUtils.isNotEmpty(confirmStatus)) {
			condition.put("confirmStatus", confirmStatus);
		}
		Map<String,Object> values =  new HashMap<String, Object>();
//		
//		String hql = " from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" s  where 1=1 ";
//		hql += " and s.isDeleted = 0 ";
//	
//		hql += " and s.degreeAuditStatus is not null  ";
//		if(condition.containsKey("branchSchool")){//学习中心
//			hql += " and s.studentInfo.branchSchool.resourceid = :branchSchool ";
//			
//		}
//		if(condition.containsKey("major")){//专业
//			hql += " and s.studentInfo.major.resourceid = :major ";
//			
//		}
//		if(condition.containsKey("classic")){//层次
//			hql += " and s.studentInfo.classic.resourceid = :classic ";
//			
//		}
//		if(condition.containsKey("name")){
//			hql += " and s.studentInfo.studentName like :name ";
//		
//		}
//		if(condition.containsKey("grade")){
//			hql += " and s.studentInfo.grade.resourceid= :grade ";
//			
//		}
//		if(condition.containsKey("studyNo")){
//			hql += " and s.studentInfo.studyNo= :studyNo ";
//			
//		}
//		if(condition.containsKey("auditStatus")){
//			hql += " and s.degreeAuditStatus= "+condition.get("auditStatus");
//		}
//		if(condition.containsKey("confirmStatus")){
//			hql += " and s.degreeAuditMemo like :confirmStatus ";
//			
//		}
//		if("1".equals(isRefresh)){
//			objPage.setPageNum(Integer.parseInt(pNum));
//		}else{
//			pNum=Integer.toString(objPage.getPageNum());
//		}		
//		if(condition.containsKey("branchSchool")){//学习中心
//			hql += " and studentInfo.branchSchool.resourceid = :branchSchool ";
//			values.put("branchSchool", condition.get("branchSchool"));
//		}
//		if(condition.containsKey("major")){//专业
//			hql += " and studentInfo.major.resourceid = :major ";
//			values.put("major", condition.get("major"));
//		}
//		if(condition.containsKey("classic")){//层次
//			hql += " and studentInfo.classic.resourceid = :classic ";
//			values.put("classic", condition.get("classic"));
//		}
//		if(condition.containsKey("name")){
//			hql += " and studentInfo.studentName like :name ";
//			values.put("name", "%"+condition.get("name")+"%");
//		}
//		if(condition.containsKey("grade")){
//			hql += " and studentInfo.grade.resourceid= :grade ";
//			values.put("grade", condition.get("grade"));
//		}
//		if(condition.containsKey("studyNo")){
//			hql += " and studentInfo.studyNo= :studyNo ";
//			values.put("studyNo", condition.get("studyNo"));
//		}
//		if(condition.containsKey("auditStatus")){
//			hql += " and degreeAuditStatus= "+condition.get("auditStatus");
//		}
//		if(condition.containsKey("confirmStatus")){
//			hql += " and degreeAuditMemo like :confirmStatus ";
//			values.put("confirmStatus", "%"+condition.get("confirmStatus"));
//		}
		String hql="from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where 1=:val";
		values.put("val", 1);
		//增加是毕业筛选的标记
		List<StudentGraduateAndDegreeAudit> page1 = studentGraduateAndDegreeAuditService.findByHql(hql, values);
		
		for (StudentGraduateAndDegreeAudit s : page1) {
			System.out.println(s);
		}
		if(ExStringUtils.isNotEmpty(pNum)) {
			condition.put("pNum", pNum);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDate", graduateDate);
		}
		model.addAttribute("degreeAuditList", page1);
		model.addAttribute("condition", condition);
		
		return "/edu3/roll/graduationStudent/degreeAuditHistory";
	}
	
	
	
	
	/**
	 * 确认学位审核结果
	 * 修改学位获得情况，更新审核信息
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/roll/graduateaudit/confirmDegreeAudit.html")
	public void confirmGraduateAudit(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String messagestr="";
		String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		String[] stu =new String[]{};
		boolean isNull = false;
		if("1".equals(isSelectAll)){//按条件审核
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
			String major		= ExStringUtils.trimToEmpty(request.getParameter("major"));
			String classic		= ExStringUtils.trimToEmpty(request.getParameter("classic"));
			String grade		= ExStringUtils.trimToEmpty(request.getParameter("grade"));
			String name			= ExStringUtils.trimToEmpty(request.getParameter("name"));
			String studyNo	    = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
			String studentstatus= ExStringUtils.trimToEmpty(request.getParameter("studentstatus"));
			Map<String,Object> condition = new HashMap<String, Object>(0);
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
			condition.put("auditStatus", "1");
			List<Map<String, Object>> tmpList = studentGDAuditJDBCService.getDegreeAuditResults(condition);
			List<String> ids = new ArrayList<String>(0);
			for (Map<String, Object> map2 : tmpList) {
				GraduateData gData = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where studentInfo.studyNo = '"+map2.get("studyno").toString()+"' ").get(0);
				if(Constants.BOOLEAN_WAIT.equals(gData.getDegreeStatus())){ //待审核
					String resourceid = gData.getResourceid();
					ids.add(resourceid);
				}
			}
			stu = new String[ids.size()];   
			ids.toArray(stu);    
			if(stu.length==0){
				isNull = true;
			}
		} else{//按勾选审核
//			String[] tmp = stus.split(",");
//			StringBuffer tmp_stus = new StringBuffer();
//			for (String graduateId : tmp) {
//				Integer degreeAuditStatus = studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where graduateData.resourceid = '"+graduateId+"' ").get(0).getDegreeAuditStatus();
//				if(1==degreeAuditStatus)
//					tmp_stus.append(graduateId+",");
//			}
//			if(tmp_stus.length()>1){
//			tmp_stus = tmp_stus.deleteCharAt(tmp_stus.length()-1);
//			stu = tmp_stus.toString().split(",");
//			}else{
//				isNull = true;
//			}
			stu  = stus.split(",");
			if(null != stu && stu.length > 0) {
				isNull = true;
			}
		}
		
		
		
		//设置一个针对所有毕业的审核结果的记录，true说明均审核通过，false说明存在审核不通过的。
		boolean hasError = false;
		int success = 0;
		int fail = 0;
		if(isNull){
			try{
				String num_schoolcode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue(); //院校代码		
				String diplomaYear = CacheAppManager.getSysConfigurationByCode("graduateData.diplomaYear").getParamValue();	

				//教学活动时间（本届毕业时间范围）
				Map<String,Object> conMap = new HashMap<String, Object>();
				conMap.put("mainProcessType", "graduateDate");
				// TODO:三年制，以后如果有其他年制要根据需求修改
				conMap.put("fistYear", ExDateUtils.getCurrentYear());
				Calendar today = Calendar.getInstance();
				String term = "1";
				if(today.get(Calendar.MONTH) >= 8){
					term = "2";
				}
				conMap.put("term",term);
				List<TeachingActivityTimeSetting> list = teachingActivityTimeSettingService.findTeachingActivityTimeSettingByCondition(conMap);
				Date activityEndTime = ExDateUtils.convertToDate("2020-12-12");
				Date activityStartTime = activityEndTime;
				if(null != list && list.size() > 0){
					activityEndTime = list.get(0).getEndTime();
					activityStartTime = list.get(0).getStartTime();
				}else if("Y".equals(diplomaYear)){
					logger.info("未在教学计划里设置本届毕业生学位审核时间范围;或者未在字典CodeTeachingActivity添加值为graduateDate的毕业日期类型");
				}		
				Boolean isGradution = false;//是否应届毕业生
				for (String id : stu) {//传进来的实际为毕业数据的id
					List<GraduateData> graduateDatas = graduateDataService.findByHql(" from "+GraduateData.class.getSimpleName()+" where resourceid=? ",id );
					GraduateData tmp_graduateData  = graduateDatas.get(0); 
					StudentInfo studentInfo = tmp_graduateData.getStudentInfo();
					//对毕业生库的更新
					try{
						String studentid = studentInfo.getResourceid();
						List<StudentGraduateAndDegreeAudit> audits =
									studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where studentInfo.resourceid=? ",studentid );
						StudentGraduateAndDegreeAudit audit = audits.get(0);
						if(audit.getDegreeAuditStatus().equals(Constants.BOOLEAN_TRUE)){ //审核通过
							tmp_graduateData.setDegreeStatus(Constants.BOOLEAN_YES);
							audit.setGraduateData(tmp_graduateData);
							audit.setDegreeAuditMemo(audit.getDegreeAuditMemo().replace("#0","#1"));//审核数据标记为【已确认毕业】
							//studentGraduateAndDegreeAuditService.saveOrUpdate(audit);
							//学位确认成功后  赋予学位
							if(ExStringUtils.isBlank(tmp_graduateData.getDegreeNum())){
								//生成新毕业证书号
								StringBuffer diplomaNum = new StringBuffer();
								//学校代码+办学方式
								
								diplomaNum.append(num_schoolcode); 
								String num_cgxscode = CacheAppManager.getSysConfigurationByCode("degree.levelCode").getParamValue(); //成高学位级别	
								//成高学位级别:（成高学士为:4）
								diplomaNum.append(num_cgxscode);
								//毕业日期年份
								//diplomaNum.append(tmp_graduateData.getGraduateDate().getYear()+1900);
								long year = tmp_graduateData.getGraduateDate().getYear()+1900;
								if("Y".equals(diplomaYear)){
									//diplomaNum.append(ExDateUtils.getCurrentYear());
									//获取应届毕业生毕业年份
									long endTime = ExDateUtils.dateDiff("second", tmp_graduateData.getGraduateDate(), activityEndTime);
									long startTime = ExDateUtils.dateDiff("second", tmp_graduateData.getGraduateDate(), activityStartTime);
									if(endTime < 0L && startTime > 0L){//应届
										isGradution = true;
									}else if("10598".equals(num_schoolcode) ){//广西医科大 往届毕业生年份=当前年份+1（一月份除外）
										if(ExDateUtils.getCurrentMonth()==0){//一月份审核年份不变
											year = ExDateUtils.getCurrentYear();
										}else {//12月份审核年份+1
											year = ExDateUtils.getCurrentYear()+1;
										}
									}
								}
								diplomaNum.append(year);
								
								//流水100001起
								diplomaNum.append(getDiplomaNumSuffix(diplomaNum.toString()));
								try {
									tmp_graduateData.setDegreeNum(diplomaNum.toString());
									String majorClassCode = String.format("%02d", Integer.valueOf(studentInfo.getMajor().getMajorClass()));
									String degreeName = JstlCustomFunction.dictionaryCode2Value("CodeMajorClass", majorClassCode);
									tmp_graduateData.setDegreeName(degreeName);
								} catch (Exception e) {
									tmp_graduateData.setDegreeName("");
									logger.error("审核操作失败！无法对应数据字典CodeMajorClass:{}",e.fillInStackTrace());
								}
							}
							success++;
							//graduateDataService.update(tmp_graduateData);
						}else{//审核不通过
							tmp_graduateData.setDegreeStatus(Constants.BOOLEAN_NO); //标准为未获得学位
							audit.setGraduateData(tmp_graduateData);
							audit.setDegreeAuditMemo(audit.getDegreeAuditMemo().replace("#0","#1"));//审核数据标记为【已确认毕业】
							fail++;
						}
						studentGraduateAndDegreeAuditService.saveOrUpdate(audit);
						graduateDataService.update(tmp_graduateData);
					}catch(Exception e){
						//如果出现graduateData的更新或创建异常 恢复已修改的学籍状态
						//messagestr += studentInfo.getStudentName()+"确认审核结果时出错.</br>";
						tmp_graduateData = graduateDatas.get(0);
						tmp_graduateData.setDegreeStatus(Constants.BOOLEAN_WAIT);
						graduateDataService.update(tmp_graduateData);
						throw new Exception();
					}
				}
				messagestr += "学位审核完毕。"+"[通过："+success+"条;不通过："+fail+"条;]";
			}catch(Exception e){
				logger.error("审核操作失败！:{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "审核操作失败！:<br/>"+e.getLocalizedMessage());	
			}
		}else{
			messagestr += "没有要审核的数据";
		}
		map.put("statusCode", 200);
		map.put("hasError", hasError);
		map.put("message", messagestr);	
		renderJson(response, JsonUtils.mapToJson(map));
		
	}
		
	/**
	 * 得到学位证的编号
	 * @param prefix
	 * @return
	 */
	private String getDiplomaNumSuffix(String prefix) {
		String suffix = "";
		
		List<GraduateData> result = graduateDataService.findByHql("from "+GraduateData.class.getSimpleName()+" where 1=1 and isDeleted = 0 and degreeNum like '" + prefix + "%' order by degreeNum desc ");
		//2016.12.22广西医科大：倒数第六位，成人 5 普高1 自考6
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if (result.size() == 0) {
			
			if("10598".equals(schoolCode)){
				suffix = "500000";
			}else{
				suffix = "100000";
			}
		} else {
			GraduateData graduateData = (GraduateData) result.get(0);
			suffix = graduateData.getDegreeNum().substring(prefix.length());
			if ("999999".equals(suffix)) {//由于基本不会发生一个成高学校一个年级有99999获得学位的可能，所以这里不再做学校的判断。
				throw new ServiceException("没有空余的流水号了！");
			}
		}
		Integer suffix_Num = Integer.valueOf(suffix);
		suffix_Num++;
		suffix = suffix_Num.toString();
		int remain = 6-suffix.length();
		StringBuffer suffix_buffer = new StringBuffer();
		for(int i =0 ; i<remain ;i++){
			suffix_buffer.append("0");
		}
		suffix_buffer.append(suffix);
		return suffix_buffer.toString();
	}
		
	/**
	 * 导出学位审核信息
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/degree/student/exportExcel.html")
	public void doExportDegreeAuditExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		List<StudentInfoVo> list = new ArrayList<StudentInfoVo>(0);//结果集
		List<Map<String,Object>> tmpList = new ArrayList<Map<String,Object>>(0);
		
		String isSelectAll = ExStringUtils.trimToEmpty(request.getParameter("isSelectAll"));
		String step = ExStringUtils.trimToEmpty(request.getParameter("step"));
		Map<String,Object> condition = new HashMap<String, Object>(0);
		if("1".equals(isSelectAll)){
			String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
			String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
			String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
			name=ExStringUtils.getEncodeURIComponentByTwice(name);
			String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));//学号
			String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
			String auditStatus= ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
			branchSchool=ExStringUtils.getEncodeURIComponentByTwice(branchSchool);
			major=ExStringUtils.getEncodeURIComponentByTwice(major);
			classic=ExStringUtils.getEncodeURIComponentByTwice(classic);
			grade=ExStringUtils.getEncodeURIComponentByTwice(grade);
			if(ExStringUtils.isNotEmpty(step)) {
				condition.put("step", step);
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
			if(ExStringUtils.isNotEmpty(name)) {
				condition.put("name", name);
			}
			if(ExStringUtils.isNotEmpty(studyNo)) {
				condition.put("studyNo", studyNo);
			}
			if(ExStringUtils.isNotEmpty(auditStatus)) {
				condition.put("auditStatus", auditStatus);
			}
			if(ExStringUtils.isNotEmpty(grade)) {
				condition.put("grade", grade);
			}
			String toDoDegreeAudit = ExStringUtils.trimToEmpty(request.getParameter("toDoDegreeAudit"));// 如果是做审核时，是未做确认的审核记录
			if(ExStringUtils.isNotEmpty(toDoDegreeAudit)) {
				condition.put("toDoDegreeAudit", toDoDegreeAudit);
			}
			tmpList = studentGDAuditJDBCService.getDegreeAuditResults(condition);
		}else {
			String stus = ExStringUtils.trimToEmpty(request.getParameter("stus"));
			String toDoDegreeAudit = ExStringUtils.trimToEmpty(request.getParameter("toDoDegreeAudit"));// 如果是做审核时，是未做确认的审核记录
			if(ExStringUtils.isNotEmpty(toDoDegreeAudit)) {
				condition.put("toDoDegreeAudit", toDoDegreeAudit);
			}
			if(ExStringUtils.isNotEmpty(stus)) {
				stus = stus.substring(0, stus.length()-1);
				stus = stus.replaceAll(",", "','");
				condition.put("graduas", stus);
				tmpList = studentGDAuditJDBCService.getDegreeAuditResults(condition);
			}
		}
		List<GraduateData> grlist = new ArrayList<GraduateData>();
		for (Map<String, Object> map2 : tmpList) {
			List<GraduateData> data = graduateDataService.findByHql("from "+GraduateData.class.getSimpleName()+" g  where g.isDeleted= ? and g.studentInfo.studyNo = ? ",0,map2.get("studyno"));
			if(null != data && data.size() > 0){
				grlist.add(data.get(0));
			}
		}	
		
		try {
			grlist=dealDegreeInfo(grlist);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		SysConfiguration configParam = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode");
		SysConfiguration configunique = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId");
		for (Map<String, Object> map2 : tmpList) {
			StudentInfoVo infoVo = new StudentInfoVo();
			if(configParam!=null && "10571".equals(configParam.getParamValue())){
				String ksh = map2.get("examcertificateno")==null?"":map2.get("examcertificateno").toString();
				if(configunique!=null && "1".equals(configunique.getParamValue())){
					ksh = map2.get("enrolleecode")==null?"":map2.get("enrolleecode").toString();
				}
				infoVo.setKSH(ksh);
			}
			infoVo.setAccountStatus(map2.get("studyno")==null?"":map2.get("studyno").toString());
			infoVo.setBloodType(map2.get("studentname")==null?"":map2.get("studentname").toString());
			String sex=JstlCustomFunction.dictionaryCode2Value("CodeSex",map2.get("gender").toString());
			infoVo.setXB(sex);
			String rq=map2.get("bornDay")==null?"":map2.get("bornDay").toString().substring(0, 10);
			infoVo.setCSRQ(rq);
			infoVo.setSFZH(map2.get("certNum")==null?"":map2.get("certNum").toString());
			infoVo.setFY(map2.get("unitName")==null?"":map2.get("unitName").toString());
			infoVo.setBH(map2.get("classesname")==null?"":map2.get("classesname").toString());
			infoVo.setCC(map2.get("classicName")==null?"":map2.get("classicName").toString());
			infoVo.setXWXSZSH(map2.get("degreeNum")==null?"":map2.get("degreeNum").toString());
			if(map2.get("learningStyle")!=null)
			{
				String  xs=JstlCustomFunction.dictionaryCode2Value("CodeTeachingType",map2.get("learningStyle").toString());
				infoVo.setXXXS(xs==null?"":xs);
			}
			
			infoVo.setXWWYZSBH(map2.get("memo")==null?"":map2.get("memo").toString());
			infoVo.setSQXWML(map2.get("degreeName")==null?"":map2.get("degreeName").toString()+"学士");
			
			if(map2.get("graduateType")!=null)
			{
				String  zt=JstlCustomFunction.dictionaryCode2Value("CodeGraduateType",map2.get("graduateType").toString());
				infoVo.setBYQK(zt==null?"":zt);
			}
			
			
			for (GraduateData gg : grlist) {
				if(gg.getStudentInfo().getStudyNo().equals(map2.get("studyno").toString())){
					infoVo.setAvg(gg.getAvg());
					infoVo.setAvgCreditHour(gg.getAvgCreditHour());
					infoVo.setThesisScore(gg.getThesisScore());
					infoVo.setDegreeEnglish(gg.getDegreeEnglish());
					break;
				}
				
			}
			
			String testMemo = map2.get("degreeauditmemo")==null?"":map2.get("degreeauditmemo").toString();
			String[] info =testMemo.split("\\|");
			infoVo.setContactPhone(info[0].replace("<font color='blue'>","").replace("<font color='red'>", "").replace("</font>", ""));
			if(info.length>1) {
				infoVo.setContactZipcode(info[1]);
			}
			if(info.length>2) {
				infoVo.setCountry(info[2]);
			}
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
			map1.put("CodeDegreeAudit_1", "审核通过");
			map1.put("CodeDegreeAudit_0", "审核不通过");
			map1.put("CodeDegreeAuditConfirm_1", "已确定");
			map1.put("CodeDegreeAuditConfirm_0", "未确定");
			String export_model = "StudentDegreeAudit";
			// 广东医与其他学校使用不用的模板
			if(configParam != null && "10571".equals(configParam.getParamValue())){
				export_model = "StudentDegreeAudit_gdy";
			}
			exportExcelService.initParmasByfile(disFile, export_model, list,map1);
			exportExcelService.getModelToExcel().setHeader("学位审核结果表");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			fileup.downloadFile(response, "学位审核结果表.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	
	
	/**
	 * 批量学位审核 (暂废弃)
	 * @param stus
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/roll/graduateaudit/viaDegree_old.html")
	public void viaDegree_old(String stus,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		boolean  mainC = Boolean.valueOf(request.getParameter("mainC"));
		boolean  graduateC = Boolean.valueOf(request.getParameter("graduateC"));
		boolean  majorBaseC = Boolean.valueOf(request.getParameter("majorBaseC"));
		boolean  majorC = Boolean.valueOf(request.getParameter("majorC"));
		boolean  flC = Boolean.valueOf(request.getParameter("flC"));
		
		Map<String ,Object> map = new HashMap<String, Object>();
		String msg = "";
		String[] stu = stus.split(",");
		try{
			for (String id : stu) {
				StudentInfo studentInfo = studentInfoService.get(id);//获得待审核的学生
				String classicId =studentInfo.getClassic().getResourceid();
				StringBuffer msgStringBuf = new StringBuffer(); 
				//本科的学生才参与学位资格审核
				if("EB43B13C75A9F518E030007F0100743D".equals(classicId)||"EB43B13C75AAF518E030007F0100743D".equals(classicId)){
					//学位审核要求
					//1、十个主干课成总分为700
					//2、毕业论文（设计）良好(含)
					//3、通过学位课程考试:1门外国语、1门专业基础课，2门专业课 专业基础课和专业课卷面成绩合格方视为通过
					//String degreeStr = studentInfo.getDegreeStatus();
					String degreeStr = "";
					String statusStr = studentInfo.getStudentStatus();
					
					if("16".equals(statusStr)){
						msg+=studentInfo.getStudentName() + "毕业信息已发布,不予审核</br>";
						String sId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
						if(id.equals(sId))
							map.put("hasFail", true);
						continue;
					}
					if(!"22".equals(statusStr)){
						msg+=studentInfo.getStudentName() + "未通过毕业审核,不予审核</br>";
						String sId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
						if(id.equals(sId))
							map.put("hasFail", true);
						continue;
					}
					if(Constants.BOOLEAN_YES.equals(degreeStr)){
						msg+=studentInfo.getStudentName() + "已通过学位审核,不予审核</br>";
						String sId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
						if(id.equals(sId))
							map.put("hasFail", true);
						continue;
					}
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
								continue;
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
					}
					//外语 是哪些课程？ 英语学位
					
					if(unitResults2.containsKey("FBECB1BCE7C8171EE030007F01001614")){
						Float f = Float.valueOf(unitResults2.get("FBECB1BCE7C8171EE030007F01001614").toString());
						if(f>=60){
							flPass = true;
						}
					}
					if(isPass&&sumScoreMainCourse/sumNumMainCourse>=70){
						msgStringBuf.append("主干课程通过");
					}else{
						msgStringBuf.append("主干课程未通过");
					}
					if(gdPass) msgStringBuf.append("毕业论文通过"); else msgStringBuf.append("毕业论文未通过");
				
					if(majorBasicPass){
						msgStringBuf.append("专业基础课通过 ");
					}else{
						msgStringBuf.append("专业基础课未通过 ");
					}
					if(majorPass){
						msgStringBuf.append("专业课通过 ");
					}else{
						msgStringBuf.append("专业课未通过 ");
					}
					if(flPass){
						msgStringBuf.append("外国语课通过 ");
					}else{
						msgStringBuf.append("外国语未通过 ");
					}
				}else{
					msgStringBuf.append("该生不为本科生");
				}
				String msgString = msgStringBuf.toString();
				String tmp_msg = "";
				if(msgString.contains("主干课程未通过")&&mainC){
					tmp_msg += "主干课;";
				}
				if((msgString.contains("专业基础课未通过 ")&&majorBaseC)||(msgString.contains("专业课未通过")&&majorC)||(msgString.contains("外国语未通过"))&&flC){
					tmp_msg += "学位课程;";
				}
				if(msgString.contains("毕业论文未通过")&&graduateC){
					tmp_msg += "毕业设计(论文);";
				}
				if(msgString.contains("该生不为本科生")){
					tmp_msg += "不为本科生;";
				}
				
				//为每个学生的学位状态设置为"获得"
				if(tmp_msg.length()==0){
//					studentInfo.setDegreeStatus(Constants.BOOLEAN_YES);
					studentInfoService.update(studentInfo);
					msg+=studentInfo.getStudentName()+" 审核成功</br>";
				}else{
					msg+=studentInfo.getStudentName() + tmp_msg+" 审核失败</br>";
					//如果出现审核失败，判断当前页的学生是否为审核失败的学生，确定是否更改学位审核状态的页面信息
					String sId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
					if(id.equals(sId))
						map.put("hasFail", true);
				}
			}
		}catch(Exception e){
			logger.error("审核操作失败！:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核操作失败！");	
			map.put("hasError", true);
		}
		map.put("statusCode", 200);
		map.put("message", msg);	
		renderJson(response, JsonUtils.mapToJson(map));
	
	}
	*/
	/**
	 * 单个学位审核 (暂废弃)
	 * @param stus
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/roll/graduateaudit/viaDegreeBySingle.html")
	public void viaDegreeBySingle(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		boolean  mainC = Boolean.valueOf(request.getParameter("mainC"));
		boolean  graduateC = Boolean.valueOf(request.getParameter("graduateC"));
		boolean  majorBaseC = Boolean.valueOf(request.getParameter("majorBaseC"));
		boolean  majorC = Boolean.valueOf(request.getParameter("majorC"));
		boolean  flC = Boolean.valueOf(request.getParameter("flC"));
		
		Map<String ,Object> map = new HashMap<String, Object>();
		String messagestr="";
		String studentId = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		String mainCourseAvg= ExStringUtils.trimToEmpty(request.getParameter("mainCourseAvg"));   
		String isPass= ExStringUtils.trimToEmpty(request.getParameter("isPass")); 
		String gdPass= ExStringUtils.trimToEmpty(request.getParameter("gdPass")); 
		String majorPass= ExStringUtils.trimToEmpty(request.getParameter("majorPass")); 
		try{
			//每一个学生因为在毕业审核之前已经对其的 年级要求和学分要求 做出了审核
			StudentInfo studentInfo = studentInfoService.get(studentId);//获得每个待审核的学生
			
			if((Double.valueOf(mainCourseAvg)<70||isPass.contains("不"))&&mainC){
				messagestr += "主干课要求未达到;";
			}
			if(gdPass.contains("不")&&graduateC){
				messagestr += "毕业设计(论文)要求未达到;";
			}
		
			if(majorPass.contains("专业基础课<font color='red'>未通过")&&majorBaseC){
				messagestr += "专业基础课要求未达到;";
			}
			if(majorPass.contains("专业课<font color='red'>未通过")&&majorC){
				messagestr += "专业课要求未达到;";
			}
			if(majorPass.contains("外国语<font color='red'>未通过")&&flC){
				messagestr += "外国语课要求未达到;";
			}
			//为每个学生的学位状态设置为"获得"
			String statusStr = studentInfo.getStudentStatus(); 
			if("16".equals(statusStr)){
				messagestr =studentInfo.getStudentName()+"毕业信息已发布，不予审核";
			}else if(messagestr.toString().length()==0){
				studentInfo.setDegreeStatus(Constants.BOOLEAN_YES);
				studentInfoService.update(studentInfo);
//				GraduateData studentGraInfo = graduatedataservice.findUniqueByProperty("studentid", studentId);
//				studentGraInfo.setDegreeStatus(Constants.BOOLEAN_YES);
//				graduatedataservice.update(studentGraInfo);
				messagestr+=studentInfo.getStudentName()+"审核成功";
			}else{
				messagestr+=studentInfo.getStudentName();
				messagestr+=		"审核失败";
				map.put("hasError", true);
			}
		}catch(Exception e){
			logger.error("审核操作失败！:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("hasError", true);
			map.put("message", "审核操作失败！");	
		}
		map.put("statusCode", 200);
		map.put("message", messagestr);	
		renderJson(response, JsonUtils.mapToJson(map));
	
	}
	*/
	/**
	 * 查看学生学位审核数据
	 * @param studentId
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/schoolroll/degree/student/view.html")
	public String degreeAuditView(String studentId ,HttpServletRequest request,ModelMap model) throws WebException{
		List<Map<String,Object>> graduationInfos = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> courseInfos = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> resultsTotal = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> resultsTotal1 = new ArrayList<Map<String,Object>>(0);
		List<Map<String,Object>> resultsTotal2 = new ArrayList<Map<String,Object>>(0);
		Map<String,Object> voMap = new HashMap<String, Object>(0);
		voMap.put("studentId", "'"+studentId+"'");
//		voMap.put("biye", "Y");
//		voMap.put("benke", "Y");
		voMap.put("forDegreeAuditResult", "1");
		graduationInfos = studentInfoChangeJDBCService.getStudentInfoForDegreeAudit(voMap);
		courseInfos     = studentInfoChangeJDBCService.getInfoForDegreeAudit(voMap);
		voMap.put("needExamTime", "1");
		resultsTotal1    = studentExamResultsService.studentToGraduatePublishedExamResultsList(voMap);
		resultsTotal2    = studentExamResultsService.studentToGraduateNoExamResultsList(voMap);
		resultsTotal1.addAll(resultsTotal2);
		resultsTotal = resultsTotal1;
		Map<String,Object> gInfo = graduationInfos.get(0);
		String studentInfoId = ExStringUtils.trimToEmpty(String.valueOf(gInfo.get("resourceid")));//学籍id
		StudentInfo info = studentInfoService.get(studentInfoId);
		Integer sumNumMainCourse=0;//主干课门数
		Float   sumScoreMainCourse=0F;//主干课总分
		List<String> mainCourseList = new ArrayList<String>(0);
		List<String> majorBaseList  = new ArrayList<String>(0);
		List<String> majorList      = new ArrayList<String>(0);
		List<String> flList         = new ArrayList<String>(0);
		List<String> gdList         = new ArrayList<String>(0);
		List<String> list1100       = new ArrayList<String>(0);
		List<String> list1200       = new ArrayList<String>(0);
		List<String> list1400       = new ArrayList<String>(0);
		List<String> list1600       = new ArrayList<String>(0);
		List<String> list2010       = new ArrayList<String>(0);
		List<String> list3001       = new ArrayList<String>(0);
		List<String> list3002       = new ArrayList<String>(0);
		List<String> list3003       = new ArrayList<String>(0);
		//学位审核要求
		//1、十个主干课成总分为700
		//2、毕业论文（设计）良好(含)
		//3、通过学位课程考试:1门外国语、1门专业基础课，2门专业课 专业基础课和专业课卷面成绩合格方视为通过	
		List<Map<String,Object>> results = new ArrayList<Map<String,Object>>(0);	
		for (Map<String,Object> map : resultsTotal) {
			if(studentInfoId.equals(map.get("studentid").toString())){
				results.add(map);
			}
		}
			
		Map<String,Object> unitResults = new HashMap<String, Object>(0);//记录综合成绩
		Map<String,Object> unitResults2 = new HashMap<String, Object>(0);//记录笔试成绩
		Map<String,Object> unitResults3 = new HashMap<String, Object>(0);//记录考试时间
		Map<String,Object> unitResults4 = new HashMap<String, Object>(0);//记录免试
		for (Map<String, Object> map2 : results) {
			if(null==map2.get("integratedscore")) {
				continue;
			}
			Double scoreD = 0D;
			try{
				scoreD = Double.valueOf(map2.get("integratedscore").toString());
			}catch(Exception e){
				continue;
			}
			String courseId = map2.get("courseid").toString(); 
			if(unitResults.containsKey(courseId)){
				Double maxScoreD = Double.valueOf(unitResults.get(courseId).toString());
				//有些课程的成绩会有一条是中 一条是百分制的情况
				//如果已存在的最大值是五分制且待比较的成绩是百分制  则把最大值设为百分制的分值-1 让百分制的成绩进入
				if(maxScoreD>2000&&scoreD<2000){
					maxScoreD = scoreD-1;
				}
				//如果已存在的最大值是百分制且待比较的成绩是五分制 则这个待比较的成绩不处理
				if(maxScoreD<2000&&scoreD>2000){
					break;
				}
				if(scoreD>maxScoreD){
					unitResults.put(courseId, scoreD);
					unitResults3.put(courseId, map2.get("examtime"));
					if("1".equals(map2.get("checkstatus"))){
						unitResults4.put(courseId, "M");
					}else{
						unitResults4.remove(courseId);
					}
				}
			}else{
				unitResults.put(courseId, scoreD);
				unitResults3.put(courseId, map2.get("examtime"));
				if("1".equals(map2.get("checkstatus"))){
					unitResults4.put(courseId, "M");
				}else{
					unitResults4.remove(courseId);
				}
			}
		}
		for (Map<String, Object> map2 : results) {
			if(map2.get("writtenscore")==null) {
				continue;
			}
			Double scoreD  = 0D;
			try{
				scoreD = Double.valueOf(map2.get("writtenscore").toString());
			}catch(Exception e){
				continue;
			}
			String courseId = map2.get("courseid").toString(); 
			//需要得到学位英语的笔试成绩
			String courseName = map2.get("coursename").toString();
			if(unitResults2.containsKey(courseId)){
				Double maxScoreD = Double.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
				//有些课程的成绩会有一条是中 一条是百分制的情况
				//如果已存在的最大值是五分制且待比较的成绩是百分制  则把最大值设为百分制的分值-1 让百分制的成绩进入
				if(maxScoreD>2000&&scoreD<2000){
					maxScoreD = scoreD-1;
				}
				//如果已存在的最大值是百分制且待比较的成绩是五分制 则这个待比较的成绩不处理
				if(maxScoreD<2000&&scoreD>2000){
					break;
				}
				if(scoreD>maxScoreD){
					unitResults2.put(courseId, scoreD+":"+courseName);
				}
			}else{
				unitResults2.put(courseId, scoreD+":"+courseName);
			}
		}
		
		for (Map<String,Object> teachingPlanCourse : courseInfos) {
			String score = "";
			String resourceid = ExStringUtils.trimToEmpty(String.valueOf(teachingPlanCourse.get("resourceid")));
			String courseNature = ExStringUtils.trimToEmpty(String.valueOf(teachingPlanCourse.get("coursenature")));
			String courseId     = ExStringUtils.trimToEmpty(String.valueOf(teachingPlanCourse.get("courseid")));
			String courseType   = ExStringUtils.trimToEmpty(String.valueOf(teachingPlanCourse.get("coursetype")));
			String isMainCourse = ExStringUtils.trimToEmpty(String.valueOf(teachingPlanCourse.get("ismaincourse")));
			if("Y".equals(isMainCourse)||"1".equals(isMainCourse)){
				sumNumMainCourse++;
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|主干课|<font color='red'>"+ f+"</font>";
				}else{
					score = courseService.get(courseId).getCourseName()+"|主干课|<font color='red'>-</font>";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					sumScoreMainCourse+=f;
					score += "|<font color='red'>"+ f+"</font>";
				}else{
					score += "|<font color='red'>-</font>";
				}
				mainCourseList.add(score);
			}
			//专业课和专业基础课都是检查卷面成绩的
			//专业基础课 1500 学科基础课 1200 
			if("1500".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "1500");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				majorBaseList.add(score);
			}
			//专业课
			else if("1300".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "1300");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				majorList.add(score);
			}
			//毕业设计  ？ 毕业论文多少分是良？ score==2514||score==2515
			else if("2020".equals(courseNature)||"thesis".equals(courseType)){
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score = courseService.get(courseId).getCourseName()+"|"+ f+"";
					gdList.add(score);
				}
			}else if("1100".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "1100");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				list1100.add(score);
			}else if("1200".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "1200");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				list1200.add(score);
			}else if("1400".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "1400");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				list1400.add(score);
			}else if("1600".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "1600");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				list1600.add(score);
			}else if("2010".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "2010");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|"+ f;
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|-";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|"+ f;
				}else{
					score += "|-";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				list2010.add(score);
			}
			/*
			 学位审核条件：(12-12-19版)
			1：符合毕业条件；
			2：论文良好以上；
			3：通过学位外语；
			4：十门主干课程平均分>=70；
			5：三门学位课程卷面成绩合格（如果没有卷面成绩只要综合成绩即可）
			三门学位课程 在教学计划里 分别是：学位专业基础课、学位主干课1、学位主干课2  
			
			此条件替换了 1门专业基础课 2门专业课的
			此处会导致一门课程出现两次，因为这三门学位课程极可能是十门主干课之一，但主要为了显示出学生满足学位审核要求的具体情况
			 */
			else if("3001".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "3001");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|<font color='red'>"+ f+"</font>";
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|<font color='red'>-</font>";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|<font color='red'>"+ f+"</font>";
				}else{
					score += "|<font color='red'>-"+"</font>";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				if(unitResults3.containsKey(courseId)){
					score += "|"+unitResults3.get(courseId);
				}
				
				list3001.add(score);
			}
			else if("3002".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "3002");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|<font color='red'>"+ f+"</font>";
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|<font color='red'>-</font>";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|<font color='red'>"+ f+"</font>";
				}else{
					score += "|<font color='red'>-"+"</font>";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				if(unitResults3.containsKey(courseId)){
					score += "|"+unitResults3.get(courseId);
				}
				list3002.add(score);
			}
			else if("3003".equals(courseNature)){
				String courseNatureName = JstlCustomFunction.dictionaryCode2Value("courseNature", "3003");
				if(unitResults2.containsKey(courseId)){
					Float f = Float.valueOf(unitResults2.get(courseId).toString().split(":")[0]);
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|<font color='red'>"+ f+"</font>";
				}else{
					score = courseService.get(courseId).getCourseName()+"|"+courseNatureName+"|<font color='red'>-</font>";
				}
				if(unitResults.containsKey(courseId)){
					Float f = Float.valueOf(unitResults.get(courseId).toString());
					score += "|<font color='red'>"+ f+"</font>";
				}else{
					score += "|<font color='red'>-"+"</font>";
				}
				if(unitResults4.containsKey(courseId)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				if(unitResults3.containsKey(courseId)){
					score += "|"+unitResults3.get(courseId);
				}
				list3003.add(score);
			}
			
		}
		//外语 是哪些课程？ 英语学位 要按照课程名称 
		Set<String> courseSet = unitResults2.keySet();
		for (String string : courseSet) {
			if("英语学位".equals(unitResults2.get(string).toString().split(":")[1])){
				String score = "";
				Float f = Float.valueOf(unitResults2.get(string).toString().split(":")[0]);
				score ="英语学位"+"|-|-|"+ f;
				if(unitResults4.containsKey(string)){
					score +=  ":<font color='blue'><b>免试</b></font>";
				}
				flList.add(score);
			}
		}
		
		
		model.addAttribute("sumNumMainCourse", sumNumMainCourse);//主干课修读门数
		model.addAttribute("sumScoreMainCourse",sumScoreMainCourse );//主干课总分
		model.addAttribute("info",info );
		model.addAttribute("mainCourseList",mainCourseList );
		model.addAttribute("majorBaseList",majorBaseList );
		model.addAttribute("majorList",majorList );
		model.addAttribute("gdList",gdList );
		model.addAttribute("flList",flList );
		
		model.addAttribute("list1100",list1100 );
		model.addAttribute("list1200",list1200 );
		model.addAttribute("list1400",list1400 );
		model.addAttribute("list1600",list1600 );
		model.addAttribute("list2010",list2010 );
		model.addAttribute("list3001",list3001 );
		model.addAttribute("list3002",list3002 );
		model.addAttribute("list3003",list3003 );
		return "/edu3/roll/graduationStudent/degreeaudit-view";
	}

}
