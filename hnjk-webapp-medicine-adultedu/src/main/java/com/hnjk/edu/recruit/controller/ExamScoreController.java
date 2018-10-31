package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.DateChineseFormat;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonModel;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.recruit.formbean.StatMatriculateForm;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
/*import com.hnjk.edu.recruit.model.ExamRoomPlan;
import com.hnjk.edu.recruit.model.ExamScore;*/
import com.hnjk.edu.recruit.model.ExamScore_import;
//import com.hnjk.edu.recruit.model.ExamSubject;
//import com.hnjk.edu.recruit.model.RecruitExamPlanDetails;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.model.RecruitPlan;
//import com.hnjk.edu.recruit.model.RecruitPlanLine;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
//import com.hnjk.edu.recruit.service.IExamRoomPlanService;
//import com.hnjk.edu.recruit.service.IExamScoreService;
//import com.hnjk.edu.recruit.service.IExamSubjectService;
//import com.hnjk.edu.recruit.service.IRecruitExamPlanDetailsService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
//import com.hnjk.edu.recruit.service.IRecruitMatriculateService;
//import com.hnjk.edu.recruit.service.IRecruitPlanLineService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
//import com.hnjk.edu.recruit.service.IStatMatriculateBySQLService;
//import com.hnjk.edu.recruit.taglib.RecruitplanCompleteVo;
//import com.hnjk.edu.recruit.vo.BatchMatriculateVo;
//import com.hnjk.edu.recruit.vo.StatMatriculateExport;
import com.hnjk.extend.plugin.excel.config.ConfigConstant;
import com.hnjk.extend.plugin.excel.config.ExcelConfigInfo;
import com.hnjk.extend.plugin.excel.config.ExcelConfigPropertyParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.extend.plugin.excel.util.ValidateColumn;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

/**
 * 录取<code>ExamScoreController</code>
 * <p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2010-5-31 下午04:46:02
 * @see
 * @version 1.0
 */
@Controller
public class ExamScoreController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -4233611685363884675L;

	
	/*@Autowired
	@Qualifier("examScoreService")
	private IExamScoreService examScoreService;*/
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;

	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;

	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;

/*
	@Autowired
	@Qualifier("statMatriculateBySQLService")
	private IStatMatriculateBySQLService statMatriculateBySQLService;
	
	@Autowired
	@Qualifier("recruitPlanLineService")
	private IRecruitPlanLineService recruitPlanLineService;

	@Autowired
	@Qualifier("examSubjectService")
	private IExamSubjectService examSubjectService;*/
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
/*	@Autowired
	@Qualifier("examRoomPlanService")
	private IExamRoomPlanService examRoomPlanService;
		
	@Autowired
	@Qualifier("recruitExamPlanDetailsService")
	private IRecruitExamPlanDetailsService recruitExamPlanDetailsService;*/
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("sysConfigurationService")
	private ISysConfigurationService sysConfigurationService;

	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	/*@Autowired
	@Qualifier("recruitMatriculateService")
	private IRecruitMatriculateService recruitMatriculateService;
	*/
	
	
/*	*//**
	 * 录入考生分数列表
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 *//*	
	@RequestMapping("/edu3/recruit/matriculate/examscore-list.html")
	public String listExamScore(String resourceid, HttpServletRequest request,	HttpServletResponse response, Page objPage, ModelMap model)throws WebException {
		
		objPage.setOrderBy("es.examSubject.courseName");
		objPage.setOrder("ASC");
		String recruitPlan 	     = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String branchSchool      = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String major 			 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String examCourse  	 	 = ExStringUtils.trimToEmpty(request.getParameter("examCourse"));
		String name 			 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		//高级查询
		String advanseCon 		 = ExStringUtils.trimToEmpty(request.getParameter("con"));
		//当前用户成绩录入的课程
		String inputCousre       = chekcCurUserInputCourse();
		
		String recruitPlanForJsp  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));

		Map<String, Object> condition = new HashMap<String, Object>();
		// 如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
//		if (StringUtils.isNotEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			condition.put("branchSchoolName", ExStringUtils.trimToEmpty(request.getParameter("branchSchoolName")));
		}
		if (StringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (StringUtils.isNotEmpty(examCourse)&&inputCousre.indexOf(examCourse)>0) {
			inputCousre="'"+examCourse+"'";
			condition.put("examCourse", examCourse);
		}
		if (StringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if (StringUtils.isNotEmpty(examCertificateNo)) {
			condition.put("examCertificateNo", examCertificateNo);
		}
		if (ExStringUtils.isNotEmpty(inputCousre)){
			condition.put("hqlForExamSoreInput", " and es.examSubject.courseName in ("+inputCousre+")");
			objPage = examScoreService.findExamScoreForEntryScore(condition,objPage);
		}else {
			condition.put("isNotScoreInputUser","你没有需要录入的科目!");
		}
			
		if (advanseCon.equals("advance")) {
			return "/edu3/recruit/matriculate/examscore-search";// 返回到高级检索
		}
		model.addAttribute("condition", condition);
		model.addAttribute("scoreList", objPage);
		return "/edu3/recruit/matriculate/examscore-list";
	}
	
	//查看当前用户成绩录入的科目
	private String chekcCurUserInputCourse(){
		
		User curUser             = SpringSecurityHelper.getCurrentUser();
		StringBuffer inputCousre = new StringBuffer("");
		//大学语文  成绩录入人
		SysConfiguration dxyw  	 = sysConfigurationService.findUniqueByProperty("paramCode", "entranceExamScoreInputConfig.dxyw");
		//大学英语   成绩录入人
		SysConfiguration dxyy  	 = sysConfigurationService.findUniqueByProperty("paramCode", "entranceExamScoreInputConfig.dxyy");
		//高等数学   成绩录入人
		SysConfiguration gdsx  	 = sysConfigurationService.findUniqueByProperty("paramCode", "entranceExamScoreInputConfig.gdsx");
		//高中数学   成绩录入人
		SysConfiguration gzsx  	 = sysConfigurationService.findUniqueByProperty("paramCode", "entranceExamScoreInputConfig.gzsx");
		//高中语文   成绩录入人
		SysConfiguration gzyw  	 = sysConfigurationService.findUniqueByProperty("paramCode", "entranceExamScoreInputConfig.gzyw");
		//高中英语   成绩录入人
		SysConfiguration gzyy  	 = sysConfigurationService.findUniqueByProperty("paramCode", "entranceExamScoreInputConfig.gzyy");
		
		if (curUser.getResourceid().equals(dxyw.getParamValue())) {
			inputCousre.append(",'DXYW' ");
		}
		if (curUser.getResourceid().equals(dxyy.getParamValue())) {
			inputCousre.append(",'DXYY' ");
		}
		if (curUser.getResourceid().equals(gdsx.getParamValue())) {
			inputCousre.append(",'GDSX' ");
		}
		if (curUser.getResourceid().equals(gzsx.getParamValue())) {
			inputCousre.append(",'GZSX' ");
		}
		if (curUser.getResourceid().equals(gzyw.getParamValue())) {
			inputCousre.append(",'GZYW' ");
		}
		if (curUser.getResourceid().equals(gzyy.getParamValue())) {
			inputCousre.append(",'GZYY' ");
		}
		return inputCousre.length()>0?ExStringUtils.trimToEmpty(inputCousre.toString()).substring(1):"";
	}
	*//**
	 * 保存分数
	 * 
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/examscore-save.html")
	public void saveExamScore(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		// Map<String, Object> map= new HashMap<String, Object>();
		User user = SpringSecurityHelper.getCurrentUser();
		String scores = request.getParameter("scores");
		try {
			if (StringUtils.isNotEmpty(scores)) {
				String[] values = scores.split(",");
				for (String value : values) {
					if (StringUtils.isNotEmpty(value)) {
						String[] score 			= value.split(":");
						ExamScore examScore	    = examScoreService.get(score[0]);
						Double newOriginalPoint = (score.length > 1 && StringUtils.isNotEmpty(score[1].trim())) ? Double.parseDouble(score[1]) : 0d;
						Double oldOriginalPoint = examScore.getOriginalPoint() != null ? examScore.getOriginalPoint(): 0d;
						// 如果有变更则保存变更
						if (!newOriginalPoint.equals(oldOriginalPoint)) {
							examScore.setFillinDate(ExDateUtils	.getCurrentDateTime());
							examScore.setFillinManId(user.getResourceid());
							examScore.setFillnMan(user.getCnName());
							examScore.setOriginalPoint(newOriginalPoint);
							examScoreService.saveOrUpdate(examScore);
						}
					}
				}
				// map.put("statusCode", 200);
				// map.put("message", "保存成功");
			}
		} catch (Exception e) {
			logger.error("保存入学测试成绩出错:{}",e.fillInStackTrace());
			// map.put("statusCode", 300);
			// map.put("message", "保存出错");
		}
		renderText(response, "ok");
		// renderJson(response, JsonUtils.mapToJson(map));
	}

	*//**
	 * 单科标准分列表
	 * 
	 * @param objPage
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/examscore-subjectpointlist.html")
	public String listMajorStandardPoint(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		
		String recruitPlan = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String courseGroup = ExStringUtils.trimToEmpty(request.getParameter("courseGroup"));
		String examCourse  = ExStringUtils.trimToEmpty(request.getParameter("examCourse"));
		String recruitPlanForJsp = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		
		Map<String, Object> condition = new HashMap<String, Object>();
//		if (ExStringUtils.isNotEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (ExStringUtils.isNotEmpty(courseGroup)) {
			condition.put("courseGroup", courseGroup);
		}
		if (ExStringUtils.isNotEmpty(examCourse)) {
			condition.put("examCourse", examCourse);
		}
		String conn = " and ( es.enrolleeinfo.entranceflag='Y' or (es.enrolleeinfo.noExamFlag='N' and es.enrolleeinfo.entranceflag='Y')) ";
			   conn+= " and ( es.enrolleeinfo.branchSchool.isMachineExam='N' or es.enrolleeinfo.branchSchool.isMachineExam is null) ";
		condition.put("forSubjectPointList", conn);
		
		Page page 		   = examScoreService.findExamScoreByCondition(condition,objPage);
		
		model.put("scoreList", page);
		model.put("condition", condition);
		
		return "/edu3/recruit/matriculate/examscore-subjectpointlist";
	}

	*//**
	 * 计算单科标准分
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/examscore-calMajorStandardPoint.html")
	public void calMajorStandardPoint(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		List<RecruitPlanLine> line    = new ArrayList<RecruitPlanLine>();
		String recruitPlan 			  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String courseGroup 			  = ExStringUtils.trimToEmpty(request.getParameter("courseGroup"));
		String examCourse  			  = ExStringUtils.trimToEmpty(request.getParameter("examCourse"));
		
		// 2014-5-13
		if (ExStringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
		}
		
		Double x            	      = null;
		
//		if (ExStringUtils.isNotEmpty(recruitPlan)
		if ((condition.containsKey("recruitPlan") || condition.containsKey("gradeName"))
				&&ExStringUtils.isNotEmpty(courseGroup)&&ExStringUtils.isNotEmpty(examCourse)) {
			
//			RecruitPlan plan 		  = recruitPlanService.get(recruitPlan);
//			condition.put("recruitPlan", recruitPlan);
			condition.put("courseGroupName", courseGroup);
			condition.put("isMachineExam", "N");
			
			line    			      = recruitPlanLineService.findRecruitPlanLineListByCondition(condition);
			if (null!=line&& !line.isEmpty()) {
				//如果当前招生批次未设置微调系数，默认为1.0
				x      				  = null==line.get(0).getCoefficient()?1.0:line.get(0).getCoefficient();
			}
			if (null!=x) {
				examScoreService.updateMajorStandardPoint(recruitPlan,examCourse, x);
				renderText(response, "ok");
			}else {
				renderText(response, "不能执行操作，还未设定微调系数X");
			}
		}else {
			renderText(response, "请选择招生批次、专业层次和要计算标准分的考试课程!");
		}
	}

	*//**
	 * 总分标准分列表
	 * 
	 * @param objPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/examscore-totalpointlist.html")
	public String listTotalpoint(HttpServletRequest request, Page objPage,HttpServletResponse response, ModelMap model) throws WebException {
		
		String recruitPlan  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String isMachineExam= ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String courseGroup  = ExStringUtils.trimToEmpty(request.getParameter("courseGroup"));
		String recruitPlanForJsp = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		Map<String, Object> condition = new HashMap<String, Object>();
		
//		if (StringUtils.isNotEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (StringUtils.isNotEmpty(isMachineExam)) {
			condition.put("isMachineExam", isMachineExam);
		}
		if (ExStringUtils.isNotEmpty(courseGroup)) {
			condition.put("courseGroup", courseGroup);
			
		}
		Page page = enrolleeInfoService.findEnrolleeByCondition(condition,objPage);
		
		model.addAttribute("eiList", page);
		model.addAttribute("condition", condition);
		return "/edu3/recruit/matriculate/examscore-totalpointlist";
	}

	*//**
	 * 计算总分标准分
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/calTotalpoint.html")
	public void calTotalpoint(HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		List<RecruitPlanLine> line    = new ArrayList<RecruitPlanLine>();
		String isMachineExam 		  = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String courseGroup            = ExStringUtils.trimToEmpty(request.getParameter("courseGroup"));
		String planid           	  = ExStringUtils.trimToEmpty(request.getParameter("planid"));
		Double x            	      = null;
		
		// 2014-5-13
		if (ExStringUtils.isNotBlank(planid)){
			String[] year_term = planid.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
		}
		
		if (StringUtils.isNotEmpty(courseGroup) && 
//				StringUtils.isNotEmpty(planid)) {
				(condition.containsKey("recruitPlan") || condition.containsKey("gradeName"))) {
			
			
//			condition.put("recruitPlan", planid);
			condition.put("courseGroupName", courseGroup);
			condition.put("isMachineExam", isMachineExam);
			
			line        = recruitPlanLineService.findRecruitPlanLineListByCondition(condition);
			
			if (null!=line && !line.isEmpty()) {
				//如果当前招生批次未设置微调系数，默认为1.0
				x      	= null==line.get(0).getCoefficient()?1.0:line.get(0).getCoefficient();
			}	

			if (null!=x) {
//				enrolleeInfoService.calTotalpoint(planid,courseGroup,isMachineExam, x);
				enrolleeInfoService.calTotalpoint(condition,courseGroup,isMachineExam, x);
				renderText(response, "OK");
				
			}else {
				renderText(response, "不能执行操作，还未设定微调系数X");
			}
		}else {
			renderText(response, "请选择招生专业!");
		}
	}

	*//**
	 * 查看预录取率分析
	 * 
	 * @param recruitPlan
	 * @param viewType
	 * @param model
	 * @return
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/matriculaterate-view.html")
	public String viewMatriculateRate(HttpServletRequest request, ModelMap model) throws WebException {
		
		String recruitPlan	= ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String viewType		= ExStringUtils.trimToEmpty(request.getParameter("viewType"));
		String coefficient	= ExStringUtils.trimToEmpty(request.getParameter("coefficient"));
		String classic      = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String isMachineExam= ExStringUtils.trimToEmpty(request.getParameter("isMachineExam")); 
		
		model.addAttribute("classic", classic);
		model.addAttribute("isMachineExam", isMachineExam);
		if (StringUtils.isNotEmpty(recruitPlan)) {
			try{
				if ("all".equals(isMachineExam)) {
					
					Map<Integer, String[]> voMap1            = examScoreService.getMatriculateRateStat(recruitPlan,classic,"N");
					Map<Integer, String[]> voMap2            = examScoreService.getMatriculateRateStat(recruitPlan,classic,"Y");
					//非机考异常成绩
					List<Map<String, Object>>  unNormalScore1= examScoreService.getunNormalEnrolleeInfoScoreForMatriculateRate(recruitPlan,classic,"N");
					//机考异常成绩
					List<Map<String, Object>>  unNormalScore2= examScoreService.getunNormalEnrolleeInfoScoreForMatriculateRate(recruitPlan,classic,"Y");
					
					model.addAttribute("voMapN", voMap1);
					model.addAttribute("voMapY", voMap2);
					model.addAttribute("voMapNCount",voMap1.get(100)[0]);
					model.addAttribute("voMapYCount",voMap2.get(100)[0]);
					model.addAttribute("unNormalScoreN",unNormalScore1);
					model.addAttribute("unNormalScoreY",unNormalScore2);
				}else {
					Map<Integer, String[]> voMap 			 = examScoreService.getMatriculateRateStat(recruitPlan,classic,isMachineExam);
					//异常成绩
					List<Map<String, Object>>  unNormalScore= examScoreService.getunNormalEnrolleeInfoScoreForMatriculateRate(recruitPlan,classic,isMachineExam);
					model.addAttribute("voMapCount",voMap.get(100)[0]);
					model.addAttribute("unNormalScore",unNormalScore);
					model.addAttribute("voMap", voMap);
				}
				if (StringUtils.isNotEmpty(coefficient)) {
					Double x 				= Double.parseDouble(coefficient) * 10;
					model.addAttribute("x", x.intValue());
				}
				model.addAttribute("coefficient", coefficient);
				model.addAttribute("recruitPlan", recruitPlan);
			}catch (Exception e) {
				logger.error("查看预录取率分析出错：{}"+e.fillInStackTrace());
				
			}
			
			if ("1".equals(viewType)) {
				return "/edu3/recruit/matriculate/matriculaterate-view";
			}
		}
		
		return "/edu3/recruit/matriculate/matriculaterate-view2";
	}
	*//**
	 * 根据分数线获取对应的录取率及人数
	 * @param planId
	 * @param matriculateLine
	 * @param x
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/getmatriculateRateByPoint.html")
	public void getMatriculateRateByPoint(HttpServletRequest request,HttpServletResponse response){
		
		String matriculateLine = ExStringUtils.trimToEmpty(request.getParameter("matriculateLine"));
		String isMachineExam   = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String classic         = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String planId 		   = ExStringUtils.trimToEmpty(request.getParameter("planId"));
		String x 			   = ExStringUtils.trimToEmpty(request.getParameter("x"));
		
		Map<String,Object> resultMap=new HashMap<String,Object>();
		try {
			Long pointLine 	   = Long.parseLong(matriculateLine);
			Double xDouble 	   = Double.parseDouble(x);
			resultMap		   = examScoreService.getMatriculateRateAndCountByPoint(planId, xDouble, pointLine,classic,isMachineExam);
		} catch (Exception e) {
			resultMap.put("errMsg","请检查输入的数据是否合法！");
			logger.error("根据分数线获取对应的录取率及人数出错：{}",e.fillInStackTrace());
		}
		renderJson(response, JsonUtils.mapToJson(resultMap));
	}
	*//**
	 * 修改学生分数列表
	 * 
	 * @param objPage
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 *//*	
	@RequestMapping("/edu3/recruit/matriculate/examscore-modifyscorelist.html")
	public String listModifyExamScore(HttpServletRequest request, Page objPage,	HttpServletResponse response, ModelMap model) throws WebException {
		// 学习中心
		String branchSchool = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		
		//高级查询
		String advanseCon = ExStringUtils.trimToEmpty(request.getParameter("con"));

		// 招生批次
		String recruitPlan = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlan"),"");
		// 招生专业
		String major = ExStringUtils.defaultIfEmpty(request.getParameter("major"),"");
		// 姓名
		String name = ExStringUtils.defaultIfEmpty(request.getParameter("name"),"");
		// 准考证号
		String examCertificateNo = ExStringUtils.defaultIfEmpty(request.getParameter("examCertificateNo"),"");
		// 证件号码
		String certNum = ExStringUtils.defaultIfEmpty(request.getParameter("certNum"),"");
		
		String orderBy = ExStringUtils.trimToEmpty(request.getParameter("orderBy"));//排序
		
		String recruitPlanForJsp = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		
		Map<String, Object> condition = new HashMap<String, Object>();

		// 如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			condition.put("branchSchoolName", ExStringUtils.defaultIfEmpty(request.getParameter("branchSchoolName"), ""));
		}
//		if (StringUtils.isNotEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (StringUtils.isNotEmpty(major)) {
			condition.put("major", major);
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
		condition.put("orderType", "");
		if(StringUtils.isNotBlank(orderBy)){
			if(orderBy.equals("asc")){
				condition.put("orderBy", "order by ei.originalPoint");
			}else{
				condition.put("orderBy", "order by ei.originalPoint desc");
			}
		}
		
		condition.put("noExamFlag"," and (  ei.isApplyNoexam is null or ei.isApplyNoexam='N'  or (ei.noExamFlag='N' and ei.entranceflag='Y') )");
		Page page = enrolleeInfoService.findEnrolleeByCondition(condition,	objPage);
		
		if(StringUtils.isNotBlank(orderBy)){
			condition.put("orderBy", orderBy);
		}
		
		if (advanseCon.equals("advance")) {
			return "/edu3/recruit/matriculate/examscore-modifyscorelist-search";// 返回到高级检索
		}
		
		model.addAttribute("eiList", page);
		model.addAttribute("condition", condition);
		return "/edu3/recruit/matriculate/examscore-modifyscorelist";
	}

	*//**
	 * 修改学生分数
	 * 
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/examscore-modifyscore.html")
	public String modifyScore(String resourceid, ModelMap model)	throws WebException {
		if (StringUtils.isNotEmpty(resourceid)) {
			EnrolleeInfo ei = enrolleeInfoService.get(resourceid);
			model.addAttribute("ei", ei);
		}
		return "/edu3/recruit/matriculate/examscore-modifyscore-form";
	}

	@RequestMapping("/edu3/recruit/matriculate/examscore-modifyscore-save.html")
	public void saveModifyScore(String resourceid, HttpServletRequest request,HttpServletResponse response, ModelMap model) throws WebException {
		// 原始分总分
		Double point = 0d;
		List<JsonModel> jsonList = new ArrayList<JsonModel>();
		String scores = request.getParameter("scores");
		if (StringUtils.isNotEmpty(scores)) {
			String[] values = scores.split(",");
			// 计算原始分标准分
			for (String value : values) {
				if (StringUtils.isNotEmpty(value)) {
					String[] score = value.split(":");
					ExamScore examScore = examScoreService.get(score[0]);
					// 旧的原始分
					Double oldSubjectPoint = (examScore.getOriginalPoint() == null ? 0	: examScore.getOriginalPoint());
					// 新的原始分
					Double newSubjectPoint = (score.length < 2 ? 0 : Double.valueOf(score[1]));
					// 如果该生有一门作弊或缺考(即subjectPoint < 0),则该生的总成绩为该门的分数
					// 否则累加
					if (newSubjectPoint < 0) {
						point = newSubjectPoint;
					} else if (point >= 0) {
						point += newSubjectPoint;
					}
					// 如果不相等，则更新单科标准分
					if (oldSubjectPoint != newSubjectPoint) {
						long standardPoint = 0L;
						if (newSubjectPoint < 0) {
							standardPoint = newSubjectPoint.longValue();
						} else if (point >= 0) {
							standardPoint = examScoreService.getNewStandardPoint(newSubjectPoint,examScore.getEnrolleeinfo());
						}
						examScore.setOriginalPoint(newSubjectPoint);
						examScore.setStandardPoint(standardPoint);
						examScoreService.saveOrUpdate(examScore);
						// 设置Json
						JsonModel jsonModel = new JsonModel();
						jsonModel.setKey(examScore.getResourceid());
						jsonModel.setValue(examScore.getStandardPointStr());
						jsonList.add(jsonModel);
					}
				}
			}
			// 计算总分标准分
			EnrolleeInfo ei = enrolleeInfoService.get(resourceid);
			if (point >= 0) {
				long totalPoint = examScoreService.getNewTotalStandardPoint(point, ei);
				ei.setTotalPoint(totalPoint);
			} else {
				ei.setTotalPoint(point.longValue());
			}
			ei.setOriginalPoint(point.longValue());
			enrolleeInfoService.saveOrUpdate(ei);
			// 设置Json
			JsonModel jsonModel = new JsonModel();
			jsonModel.setKey("totalPoint");
			jsonModel.setValue(ei.getTotalPointStr());
			jsonList.add(jsonModel);
		}
		renderJson(response, JsonUtils.listToJson(jsonList));
	}

	*//**
	 * 批量录取列表
	 * 
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/batchmatriculate-list.html")
	public String listBatchMatriculate(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		
		Map<String, String> condition = new HashMap<String, String>();
		List<BatchMatriculateVo> list = new ArrayList<BatchMatriculateVo>();
		// 招生批次
		String recruitPlan 			  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String courseGroupName        = ExStringUtils.trimToEmpty(request.getParameter("courseGroupName"));
		String branchSchool           = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String isMachineExam 		  = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String recruitPlanForJsp 	  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		
//		if (ExStringUtils.isNotEmpty(recruitPlan))     condition.put("recruitPlan", recruitPlan);
		if (ExStringUtils.isNotEmpty(branchSchool))    condition.put("branchSchool", branchSchool);
		if (ExStringUtils.isNotEmpty(courseGroupName)) condition.put("courseGroupName", courseGroupName);
		if (ExStringUtils.isNotEmpty(isMachineExam)) condition.put("isMachineExam", isMachineExam);
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}

		//2014-5-8
		if (condition.containsKey("recruitPlan")) {
			list = examScoreService.getBatchMatriculateList(condition.get("recruitPlan"),courseGroupName,branchSchool,isMachineExam);
		} else if(condition.containsKey("gradeName")){
			list = examScoreService.getBatchMatriculateList(condition,courseGroupName,branchSchool,isMachineExam);
		}
//		if (StringUtils.isNotEmpty(recruitPlan)) {
//			list = examScoreService.getBatchMatriculateList(recruitPlan,courseGroupName,branchSchool,isMachineExam);
//		}
		condition.put("courseGroupName",courseGroupName);
		condition.put("recruitPlan", recruitPlan);
		model.addAttribute("list", list);
		model.addAttribute("condition", condition);

		return "/edu3/recruit/matriculate/batchmatriculate-list";
	}
	
	*//**
	 * 批量录取
	 * 
	 * @param resourceid
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/batchmatriculate.html")
	public void batchMatriculate(HttpServletRequest request,HttpServletResponse response)throws WebException {
	
		Map<String, Object> map = new HashMap<String, Object>();
		String recruitMajorId   = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String courseGroupName  = ExStringUtils.trimToEmpty(request.getParameter("courseGroupName"));
		String recruitPlan      = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String branchSchool     = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String isMachineExam    = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		String recruitMajorIds  = "";
		
		// 2014-5-14
		if (ExStringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					map.put("gradeName", year_term[0]);
				}else {
					map.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				map.put("gradeName", year_term[0]);
				map.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				map.put("recruitPlan", year_term[2]);
			}
			map.put("recruitPlanForJsp", recruitPlan);
		}
		
//		if (ExStringUtils.isNotEmpty(recruitPlan))     map.put("recruitPlan", recruitPlan);
		if (ExStringUtils.isNotEmpty(branchSchool))    map.put("branchSchool", branchSchool);
		if (ExStringUtils.isNotEmpty(isMachineExam))   map.put("isMachineExam", isMachineExam);
		if (ExStringUtils.isNotEmpty(courseGroupName)) map.put("courseGroupName", courseGroupName);
	
		if (ExStringUtils.isNotEmpty(recruitMajorId)&&recruitMajorId.length()>0) {
			for (String s : recruitMajorId.split(",")) {
				recruitMajorIds += ",'"+s+"'";
			}
		}
		
		String msg 			    = "";
		RecruitPlanLine line    = null;
		if (ExStringUtils.isNotEmpty(recruitMajorIds) && ExStringUtils.isNotEmpty(courseGroupName)
				&&(map.containsKey("recruitPlan") || map.containsKey("gradeName"))) {
//				&&ExStringUtils.isNotEmpty(recruitPlan)) {
			//RecruitMajor major 			   = recruitMajorService.get(recruitMajorId);
//			RecruitPlan plan    		   = recruitPlanService.get(recruitPlan);
//			String lineHQL      		   = " from "+RecruitPlanLine.class.getSimpleName()
//										   + " line where line.isDeleted=0 and line.recruitPlan.resourceid=? and line.courseGroupName=? and line.isMachineExam=?";
			String lineHQL      		   = " from "+RecruitPlanLine.class.getSimpleName()
					   + " line where line.isDeleted=0 and line.courseGroupName=? and line.isMachineExam=?";
			List<RecruitPlanLine> lineList = Collections.EMPTY_LIST;
			// 2014-5-14
			if (map.containsKey("gradeName")) {
				if (map.containsKey("gradeTerm")) {
					lineHQL+=" and (line.recruitPlan.grade.gradeName like '" + map.get("gradeName") + "%' and line.recruitPlan.grade.term=?) ";
					lineList = recruitPlanLineService.findByHql(lineHQL,courseGroupName,isMachineExam,map.get("gradeTerm").toString());
				}else{
					lineHQL+=" and line.recruitPlan.grade.gradeName like '" + map.get("gradeName") + "%' ";
					lineList = recruitPlanLineService.findByHql(lineHQL,courseGroupName,isMachineExam);
				}			
			}
			if (map.containsKey("recruitPlan")) {
				lineHQL+=" and line.recruitPlan.resourceid=? ";
				lineList = recruitPlanLineService.findByHql(lineHQL,courseGroupName,isMachineExam,map.get("recruitPlan").toString());
			}
			
//			List<RecruitPlanLine> lineList = recruitPlanLineService.findByHql(lineHQL,plan.getResourceid(),courseGroupName,isMachineExam);
			if (null!=lineList&&!lineList.isEmpty()) {
				line 					   = lineList.get(0);
				if (null!=line.getMatriculateLine()&&line.getMatriculateLine()>0) {
					examScoreService.batchMatriculate(recruitMajorIds.substring(1),courseGroupName,line.getMatriculateLine(),branchSchool,isMachineExam);
					map.put("stutasCode", 200);
					map.put("navTabId", "RES_MATRICALATE_BATCHMATRICULATE_LIST");
					map.put("reloadUrl", request.getContextPath()+"/edu3/recruit/matriculate/batchmatriculate-list.html?recruitPlan="+recruitPlan+"&courseGroupName="+courseGroupName);
					map.put("message", "操作成功");
				}else {
//					msg = "招生批次："+plan.getRecruitPlanname()+"_" + JstlCustomFunction.dictionaryCode2Value("CodeEntranceExaminationType", courseGroupName)+ ",还没有设定录取分数线";
					msg = "招生批次："+line.getRecruitPlan().getRecruitPlanname()+"_" + JstlCustomFunction.dictionaryCode2Value("CodeEntranceExaminationType", courseGroupName)+ ",还没有设定录取分数线";
					logger.debug(msg);
					map.put("stutasCode", 300);
					map.put("message", msg);
				}
				
			}else {
				msg = "招生批次："+line.getRecruitPlan().getRecruitPlanname()+"_" + JstlCustomFunction.dictionaryCode2Value("CodeEntranceExaminationType", courseGroupName)+ ",还没有设定录取分数线";
				logger.debug(msg);
				map.put("stutasCode", 300);
				map.put("message", msg);
			}
		}else {
			msg = "请选择招生批次及专业层次点击查询后选择要录取的专业执行录取操作！";
			map.put("stutasCode", 300);
			map.put("message", msg);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	*//**
	 * 个别录取列表
	 * 
	 * @param request
	 * @param objPage
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 *//*	
	@RequestMapping("/edu3/recruit/matriculate/singlematriculate-list.html")
	public String listSingleMatriculate(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		// 学习中心
		String branchSchool 	      = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		//高级查询
		String advanseCon 		      = ExStringUtils.trimToEmpty(request.getParameter("con"));
		// 招生批次
		String recruitPlan  	      = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		// 招生专业
		String recruitMajor 	      = ExStringUtils.trimToEmpty(request.getParameter("recruitMajor"));
		// 姓名
		String name 			      = ExStringUtils.trimToEmpty(request.getParameter("name"));
		// 准考证号
		String examCertificateNo      = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		// 证件号码
		String certNum 			      = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		//是否申请英语免试
		String isEnglish              = ExStringUtils.trimToEmpty(request.getParameter("isEnglish"));
		String recruitPlanForJsp = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));

		// 如果是学习中心用户，只操作本学习中心的数据
		User user 			          = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool 			  = user.getOrgUnit().getResourceid();
			model.addAttribute("brshSchool", "Y");
		}
		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			condition.put("branchSchoolName", ExStringUtils.defaultIfEmpty(	request.getParameter("branchSchoolName"), ""));
		}
//		if (StringUtils.isNotEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (StringUtils.isNotEmpty(recruitMajor)) {
			condition.put("recruitMajor", recruitMajor);
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
		if (StringUtils.isNotEmpty(isEnglish)) {
			condition.put("isEnglish", isEnglish);
		}
		if (!condition.isEmpty()) {
			objPage = enrolleeInfoService.findEnrolleeByCondition(condition,objPage);
		}
		
		
		if ("advance".equals(advanseCon)) {
			return "/edu3/recruit/matriculate/singlematriculate-search";// 返回到高级检索
		}
		model.addAttribute("eiList", objPage);
		model.addAttribute("condition", condition);

		return "/edu3/recruit/matriculate/singlematriculate-list";
	}

	*//**
	 * 录取/不录取
	 * 
	 * @param resourceid
	 * @param flag
	 * @param response
	 * @param model
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/singlematriculate.html")
	public void singleMatriculate(String resourceid, String flag,HttpServletResponse response, ModelMap model) throws WebException {
		if (StringUtils.isNotEmpty(resourceid)) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<EnrolleeInfo> willEnrolleeInfos = new ArrayList<EnrolleeInfo>();
			try {
				synchronized (this) {
					EnrolleeInfo ei = enrolleeInfoService.get(resourceid);
					if(!ExStringUtils.equals(ei.getIsMatriculate(), flag)){//如果还未录取或取消录取
						//录取
						if ("Y".equals(flag)) {						
							// 生成学号
							if (StringUtils.isEmpty(ei.getMatriculateNoticeNo())) {
								String matriculateNoticeNo = enrolleeInfoService.genStudentid(ei);
								ei.setMatriculateNoticeNo(matriculateNoticeNo);
							}
							// 生成注册号
							if (StringUtils.isEmpty(ei.getRegistorNo())) {
								String registorNo = enrolleeInfoService	.genEnrolleeRegistorNo(ei);
								ei.setRegistorNo(registorNo);
							}
							if ("Y".equals(ei.getBranchSchool().getIsMachineExam())) {
								ei.setIsMachineExamScore("Y");
							}
							//设置录取时间
							ei.setMatriculateDate(new Date());
							//设置录取通知书打印与否，默认为否“N”
							ei.setIsPrintOfEnroll("N");
						//不录取 	
						}else {
							if ("Y".equals(ei.getBranchSchool().getIsMachineExam())) {
								ei.setIsMachineExamScore("N");
							}
						}
						
						ei.setIsMatriculate(flag);
						willEnrolleeInfos.add(ei);
						recruitMatriculateService.doMatriculate(willEnrolleeInfos);
					} 				
					//enrolleeInfoService.saveOrUpdate(ei);					
					map.put("statusCode", 200);
					map.put("message", "操作成功");
				}
			} catch (Exception e) {
				logger.error("个别录取操作失败:{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "操作失败:"+e.getMessage());
			}
			renderJson(response, JsonUtils.mapToJson(map));
		}
	}

	*//**
	 * 录取名册
	 * 
	 * @param objPage
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 *//*	
	@RequestMapping("/edu3/recruit/matriculate/matriculateinfo-list.html")
	public String listMatriculateInfo(Page objPage, HttpServletRequest request,	ModelMap model) throws WebException {
		
		// 查询条件
		Map<String, Object> condition = new HashMap<String, Object>();
		//高级查询
		String advanseCon 			  = ExStringUtils.trimToEmpty(request.getParameter("con"));
		// 学习中心
		String branchSchool 		  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		// 招生计划
		String recruitPlan 			  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		// 专业
		String major 				  = ExStringUtils.trimToEmpty(request.getParameter("major"));
		// 层次
		String classic 				  = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		// 入学资格审核标志
		String entranceFlag 		  = ExStringUtils.trimToEmpty(request.getParameter("entranceFlag"));
		//报到时间	
		String registerDate 		  = ExStringUtils.trimToEmpty(request.getParameter("registerDate"));
		//打印时间
		String printDate              = ExStringUtils.trimToEmpty(request.getParameter("printDate"));
		// 姓名 
		String name 				  = ExStringUtils.trimToEmpty(request.getParameter("name"));
		//准考证号
		String examCertificateNo 	  = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		//是否免试
		String isApplyNoexam          = ExStringUtils.trimToEmpty(request.getParameter("isApplyNoexam"));
		//年级                     
		String grade                  = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		//学习形式
		String learningStyle 		  = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		//证件号码
		String certNum 	              = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String recruitPlanForJsp = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		
		//**13.5.29
		//新增查询条件；
		//是否打印过录取通知书
		String isPrintOfEnroll        = ExStringUtils.trimToEmpty(request.getParameter("isPrintOfEnroll"));
		//最早录取时间
		String matriculateDateL       = ExStringUtils.trimToEmpty(request.getParameter("matriculateDateL"));
		//最后录取时间
		String matriculateDateH       = ExStringUtils.trimToEmpty(request.getParameter("matriculateDateH"));
		// 如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			condition.put("branchSchoolName", ExStringUtils.defaultIfEmpty(request.getParameter("branchSchoolName"), ""));
		}
//		if (!ExStringUtils.isEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (!ExStringUtils.isEmpty(major)) {
			condition.put("major", major);
		}
		if (!ExStringUtils.isEmpty(classic)) {
			condition.put("classic", classic);
		}
		//if (ExStringUtils.isNotEmpty(entranceFlag)) {
		//	condition.put("entranceFlag"," and ei.entranceflag = '"+entranceFlag+"' ");
		//}
		if (ExStringUtils.isNotEmpty(registerDate)) {
			condition.put("registerDate", registerDate);
		}
		if (ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if (ExStringUtils.isNotEmpty(examCertificateNo)) {
			condition.put("examCertificateNo", examCertificateNo);
		}
		if (ExStringUtils.isNotEmpty(printDate)) {
			condition.put("printDate", printDate);
		}
		if (ExStringUtils.isNotEmpty(isApplyNoexam)) {
			condition.put("isApplyNoexam", isApplyNoexam);
		}
		if (ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (StringUtils.isNotEmpty(learningStyle))	{ 
			condition.put("learningStyle",learningStyle);
		}
		if (ExStringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
		}
		// 已录取
		condition.put("isMatriculate", "Y");
		// 报名资格已通过
		condition.put("signupFlag", "Y");
		
		condition.put("orderBy"," order By ei.resourceid, ei.examCertificateNo desc");
		condition.put("orderType","");
		
		//新增查询条件
		//录取通知书是否已经打印
		if (ExStringUtils.isNotEmpty(isPrintOfEnroll)) {
			condition.put("isPrintOfEnroll", isPrintOfEnroll);
		}
		//最早录取时间
		if (ExStringUtils.isNotEmpty(matriculateDateL)) {
			condition.put("matriculateDateL", matriculateDateL);
		}
		//最迟录取时间
		if (ExStringUtils.isNotEmpty(matriculateDateH)) {
			condition.put("matriculateDateH", matriculateDateH);
		}
		
		Page page = enrolleeInfoService.findEnrolleeByCondition(condition,	objPage);
		
		condition.put("entranceFlag", entranceFlag);
		
		if (advanseCon.equals("advance")) {
			return "/edu3/recruit/matriculate/matriculateinfo-search";// 返回到高级检索
		}
		model.addAttribute("eiList", page);
		model.addAttribute("condition", condition);
		return "/edu3/recruit/matriculate/matriculateinfo-list";
	}
*/
	/**
	 * 录取查询
	 * 
	 * @param request
	 * @param objPage
	 * @param response
	 * @param model
	 * @return
	 * @throws WebException
	 */	
	@RequestMapping("/edu3/recruit/matriculate/matriculatequery-list.html")
	public String listMatriculateQuery(HttpServletRequest request,	Page objPage, ModelMap model) throws WebException {
		// 学习中心
		String branchSchool           = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		// 招生批次
		String recruitPlan            = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String recruitPlanForJsp            = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		// 专业
		String major           		  = ExStringUtils.trimToEmpty(request.getParameter("major"));
		// 年级
		String grade           		  = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		// 层次
		String classic           	  = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		// 姓名
		String name                   = ExStringUtils.trimToEmpty(request.getParameter("name"));
		// 准考证号
		String examCertificateNo      = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		// 证件号码
		String certNum 			      = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		//办学模式
		String teachingType           = ExStringUtils.trimToEmpty(request.getParameter("teachingType"));
		//是否录取
		String isMatriculate           = ExStringUtils.trimToEmpty(request.getParameter("isMatriculate"));
		//入学资格审核起止时间
		String entranceCheckDateb = ExStringUtils.trimToEmpty(request.getParameter("entranceCheckDateb"));
		String entranceCheckDatee = ExStringUtils.trimToEmpty(request.getParameter("entranceCheckDatee"));
		
		//2014-4-21
		// 自然年度
//		String natural_year 			      = ExStringUtils.trimToEmpty(request.getParameter("natural_year"));
//		//招生季度
//		String quarter           = ExStringUtils.trimToEmpty(request.getParameter("quarter"));
		
		Map<String, Object> condition = new HashMap<String, Object>();
		
//		if(ExStringUtils.isNotEmpty(natural_year)) condition.put("natural_year", natural_year);
//		if(ExStringUtils.isNotEmpty(quarter)) condition.put("quarter", quarter);

		// 如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			condition.put("branchSchoolName", ExStringUtils.defaultIfEmpty(	request.getParameter("branchSchoolName"), ""));
		}
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}

		if (StringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if (StringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (StringUtils.isNotEmpty(major)) {
			condition.put("major", major);
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
		if (StringUtils.isNotEmpty(teachingType)) {
			condition.put("teachingType", teachingType);
		}
		if (StringUtils.isNotEmpty(isMatriculate)) {
			condition.put("isMatriculate", isMatriculate);
		}
		if (StringUtils.isNotEmpty(entranceCheckDateb)) {
			condition.put("entranceCheckDateb", entranceCheckDateb);
		}
		if (StringUtils.isNotEmpty(entranceCheckDatee)) {
			condition.put("entranceCheckDatee", entranceCheckDatee);
		}
		
		// 已录取
//		condition.put("isMatriculate", "Y");

		Page page = enrolleeInfoService.findEnrolleeByCondition(condition,objPage);
		condition.put("name", name); 
		model.addAttribute("condition", condition);
		model.addAttribute("eiList", page);
		
		List<Major> majors = majorService.findByHql(" from "+Major.class.getSimpleName()+" where isDeleted= 0 order by majorCode asc ");
		StringBuffer majorOption = new StringBuffer("<option value=''></option>");
		
		for (Major m : majors) {
			if(ExStringUtils.isNotEmpty(major)&&major.equals(m.getResourceid())){
				majorOption.append("<option value ='"+m.getResourceid()+"' selected='selected' >"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}else{
				majorOption.append("<option value ='"+m.getResourceid()+"'>"+m.getMajorCode()+"-"+m.getMajorName()+"</option>");
			}
			
		}
		model.addAttribute("majorOption", majorOption);

		return "/edu3/recruit/matriculate/matriculatequery-list";
	}
	
/*	*//**
	 * 根据年度获取招生季度，作废，前台标签实现
	 *//*
	
	@RequestMapping(value = "/edu3/recruit/matriculate/recruitQuarter.html", method=RequestMethod.POST)
	public void recruitQuarter(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {
		String natural_year = ExStringUtils.trimToEmpty(request.getParameter("natural_year"));//2013
		Map<String,Object> data = new HashMap<String, Object>(0);
		try {
			
			StringBuffer recruitShortNameSelectHtml = new StringBuffer("");
			recruitShortNameSelectHtml.append("<option value=\"\">请选择</option>");
			Map<String,Object> planName = new HashMap<String, Object>();
//			for (RecruitplanAutoCompleteVo plan : list) {
//				//对plan.getRecruitplanname()进行截取，并且不重复
//				if(!planName.containsKey("" + plan.getFirstYear()) && "1".equals(plan.getTerm())){
//					planName.put("" +plan.getFirstYear(), natural_year+"秋季");
//					recruitShortNameSelectHtml.append("<option value=\""+natural_year+"_1"+
//							"\" >"+natural_year+"秋季"+" </option> ");
//				}else if(!planName.containsKey("" + plan.getFirstYear()) && "2".equals(plan.getTerm())){
//					planName.put("" +plan.getFirstYear(), natural_year+"春季");
//					recruitShortNameSelectHtml.append("<option value=\""+plan.getFirstYear()+"_2"+
//							"\" >"+natural_year+"春季"+" </option> ");
//				}
				
//				recruitShortNameSelectHtml.append("<option value=\""+plan.getResourceid()+
////						(id_year[0].equals(y.getResourceid())?"\" selected =\"selected\" ":"\"")+" >"+y.getc.getClassname()+" </option> ");
//						"\" >"+plan.getRecruitplanname()+" </option> ");
//			}
//			data.put("result", 200);
//			data.put("quarter", recruitShortNameSelectHtml);
		} catch (Exception e) {
			data.put("result", 300);
//			data.put("msg", "异步获取招生季度出错。");
		}
//		
//		
//		renderJson(response,JsonUtils.mapToJson(data));
		
//		String[] id_year = natural_year.split("_");
		
		Map<String,Object> data = new HashMap<String, Object>(0);
		StringBuffer sql = new StringBuffer("select t.resourceid,t.yearid,t.recruitplanname,y.yearname ,t.term,y.firstyear from edu_recruit_recruitplan t join edu_base_year y on t.yearid=y.resourceid and t.isdeleted=0"); 
		sql.append(" where ((y.firstyear=")
		.append(Integer.valueOf(natural_year) - 1)
		.append(" and t.term=2) or (y.firstyear=")
		.append(Integer.valueOf(natural_year))
		.append(" and t.term=1)) ")
		.append("order by y.firstyear desc,t.term desc,t.recruitplanname desc");
		;
		
		try {
			
			List<RecruitplanAutoCompleteVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), RecruitplanAutoCompleteVo.class, null);
			StringBuffer recruitShortNameSelectHtml = new StringBuffer("");
			recruitShortNameSelectHtml.append("<option value=\"\">请选择</option>");
			Map<String,Object> planName = new HashMap<String, Object>();
			for (RecruitplanAutoCompleteVo plan : list) {
				//对plan.getRecruitplanname()进行截取，并且不重复
				if(!planName.containsKey("" + plan.getFirstYear()) && "1".equals(plan.getTerm())){
					planName.put("" +plan.getFirstYear(), natural_year+"秋季");
					recruitShortNameSelectHtml.append("<option value=\""+natural_year+"_1"+
							"\" >"+natural_year+"秋季"+" </option> ");
				}else if(!planName.containsKey("" + plan.getFirstYear()) && "2".equals(plan.getTerm())){
					planName.put("" +plan.getFirstYear(), natural_year+"春季");
					recruitShortNameSelectHtml.append("<option value=\""+plan.getFirstYear()+"_2"+
							"\" >"+natural_year+"春季"+" </option> ");
				}
				
//				recruitShortNameSelectHtml.append("<option value=\""+plan.getResourceid()+
////						(id_year[0].equals(y.getResourceid())?"\" selected =\"selected\" ":"\"")+" >"+y.getc.getClassname()+" </option> ");
//						"\" >"+plan.getRecruitplanname()+" </option> ");
			}
			data.put("result", 200);
			data.put("quarter", recruitShortNameSelectHtml);
		} catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取招生季度出错。");
		}
		
		
		renderJson(response,JsonUtils.mapToJson(data));
		
		
		
	}
	
	*//**
	 * 异步根据招生季度获取招生计划
	 *//*
	@RequestMapping(value = "/edu3/recruit/matriculate/recruitPlan.html", method=RequestMethod.POST)
	public void recruitPlan(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {
		String quarter = ExStringUtils.trimToEmpty(request.getParameter("quarter"));//2013_2
		String[] quarter_term = quarter.split("_");
		
		Map<String,Object> data = new HashMap<String, Object>(0);
		StringBuffer sql = new StringBuffer("select t.resourceid,t.recruitplanname,g.term,g.gradename from edu_recruit_recruitplan t join edu_base_grade g on g.resourceid=t.gradeid")
		.append(" where t.isdeleted=0 and g.isdeleted=0 ")
		.append("and g.gradename like '"+quarter_term[0]+"%'")
		.append(" and g.term=").append(quarter_term[1]);
			
		
		sql.append(" order by t.recruitplanname desc");
		
		try {
			
			List<RecruitplanCompleteVo> list = baseSupportJdbcDao.getBaseJdbcTemplate().findList(sql.toString(), RecruitplanCompleteVo.class, null);
			StringBuffer recruitSelectHtml = new StringBuffer("");
			recruitSelectHtml.append("<option value=\"\">请选择</option>");
			for (RecruitplanCompleteVo plan : list) {
				recruitSelectHtml.append("<option value=\""+plan.getResourceid()+"_"+quarter+
						"\" >"+plan.getRecruitplanname()+" </option> ");
			}
			data.put("result", 200);
			data.put("recruitPlan", recruitSelectHtml);
		} catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取招生计划出错。");
		}
		
		renderJson(response,JsonUtils.mapToJson(data));
	}

	*//**
	 * 个人成绩查询
	 * 
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 *//*	
	@RequestMapping(value = {"/edu3/recruit/matriculate/studentmatriculatequery-list.html",
			"/portal/site/service/matriculatequery.html" })
	public String studentMatriculateQuery(HttpServletRequest request,Page objPage, ModelMap model) throws WebException {
		// 姓名
		String name 				  = request.getParameter("name");
		// 准考证号
		String examCertificateNo  	  = request.getParameter("examCertificateNo");
		// 证件号码
		String certNum 				  = request.getParameter("certNum");
		
		

		Boolean isNeedQuery 		  = false;
		Map<String, Object> condition = new HashMap<String, Object>();
		User user                     = SpringSecurityHelper.getCurrentUser();
		if (StringUtils.isNotEmpty(name)) {
			condition.put("name", name);
			isNeedQuery = true;
		}
		if (StringUtils.isNotEmpty(examCertificateNo)) {
			condition.put("examCertificateNo", examCertificateNo);
			isNeedQuery = true;
		}
		if (StringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
			isNeedQuery = true;
		}
		if (isNeedQuery) {
			String message = "没有查到相关录取信息";
			@SuppressWarnings("unchecked")List<EnrolleeInfo> list = enrolleeInfoService.findEnrolleeByCondition(condition, objPage).getResult();
			if (list.size() == 0) {
				message = "你输入的查询条件有误，查无此人！";
			} else if (list.size() > 1) {
				message = "由于出现重名的情况，请输入你的准考证号或者身份证号再继续查询！";
			} else {
				try {
					EnrolleeInfo ei = list.get(0);
					Date statTime   = ei.getRecruitMajor().getRecruitPlan().getEnrollStartTime();
					Date endTime    = ei.getRecruitMajor().getRecruitPlan().getEnrollEndTime();
					if (null!=endTime) {
						endTime     = ExDateUtils.addDays(endTime, 1);
					}
					
					Date currentTime= new Date();
					
					版本一、没有机考非机考之分(不存在机考)，查询时间按招生批次设置的查询时间,学生查询不显示成绩,招生办用户查询显示成绩
				 *	版本二、 加入了机考学生，机考学生只要一被录取就可查询录取状态(根据学习中心的机考属性确定机考非机考),学生查询不显示成绩,招生办用户查询显示成绩
				 *  版本三、 有机考学生跟非机考学生，但不区分此属性，统一按照招生批次中设置的查询时间查询,学生查询不显示成绩,招生办用户查询显示成绩
				 * //机考的录取查询只要被录取就可查询
					if ("Y".equals(ei.getBranchSchool().getIsMachineExam())) {
						
						if ("Y".equals(ei.getIsMatriculate())&&"Y".equals(ei.getIsMachineExamScore())) {
							message = "祝贺你，你已经被录取！你的学号是：" + ei.getMatriculateNoticeNo();
						} else if ("N".equals(ei.getIsMatriculate())&&"Y".equals(ei.getIsMachineExamScore())) {
							message = "你未被录取，下次努力！";
						}else {
							message = "成绩未发布，请耐心等待...";
						}
						
					//非机考的录取查询需要批次指定的录取查询时间范围内
					}else {
						
						if (null!=statTime && null!=endTime) {	
							if (currentTime.getTime()>=statTime.getTime() && currentTime.getTime()<=endTime.getTime()) {
								if ("Y".equals(ei.getIsMatriculate())) {
									message = "祝贺你，你已经被录取！你的学号是：" + ei.getMatriculateNoticeNo();
								} else if ("N".equals(ei.getIsMatriculate())) {
									message = "你未被录取，下次努力！";
								}
							}else {
									message = "录取查询未开放，或已过录取查询时间！<br/>查询时间："+
											  ExDateUtils.formatDateStr(statTime, "yyyy-MM-dd")+
											  " 至 "+ExDateUtils.formatDateStr(endTime, "yyyy-MM-dd");
							}
						}else {
							message = "录取查询未开放！";
						}
					}
					if (null!=statTime && null!=endTime) {	
						if (currentTime.getTime()>=statTime.getTime() && currentTime.getTime()<=endTime.getTime()) {
							
							//圆梦计划学习方式的,不区分华工录取结果
							if ("5".equals(ei.getStutyMode())) {
								    message = "你的入学测试已结束，具体面试及录取结果请等待团省委另行通知！";
							
							//非圆梦计划学习方式的
							}else {
								if ("Y".equals(ei.getIsMatriculate())) {
									message = "祝贺你，你已经被录取！你的学号是：" + ei.getMatriculateNoticeNo();
								} else if ("N".equals(ei.getIsMatriculate())) {
									message = "你未被录取，下次努力！";
								}
							}
							
						}else {
								message = "录取查询未开放，或已过录取查询时间！<br/>查询时间："+
										  ExDateUtils.formatDateStr(statTime, "yyyy-MM-dd")+
										  " 至 "+ExDateUtils.formatDateStr(endTime, "yyyy-MM-dd");
						}
					}else {
						message = "录取查询未开放！";
					}
					model.addAttribute("ei", ei);
				} catch (Exception e) {
					
					logger.error("查询录取状态出错："+e.getMessage());
					message   = "查询录取状态出错："+e.getMessage();
				}
			}
			condition.put("name", name);
			model.addAttribute("message", message);
			model.addAttribute("condition", condition);
		}
		model.addAttribute("currentUser", user);
		return "/edu3/recruit/matriculate/studentmatriculatequery-list";
	}

	*//**
	 * 考生信息查询
	 * 
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 *//*
	@RequestMapping(value = {
			"/edu3/recruit/matriculate/studentinfoquery-list.html",
			"/portal/site/service/studentinfoquery.html" })
	public String studentInfoQuery(HttpServletRequest request, Page objPage,
			ModelMap model) throws WebException {
		// 姓名
		String name = request.getParameter("name");
		// 证件号码
		String certNum = request.getParameter("certNum");

		Boolean isNeedQuery = false;
		Map<String, Object> condition = new HashMap<String, Object>();

		if (StringUtils.isNotEmpty(name)) {
			condition.put("name", name);
			isNeedQuery = true;
		}
		if (StringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
			isNeedQuery = true;
		}
		if (isNeedQuery) {
			@SuppressWarnings("unchecked")List<EnrolleeInfo> list = enrolleeInfoService.findEnrolleeByCondition(condition, objPage).getResult();
			if (list.size() == 0) {
				String message = "你输入的查询条件有误，查无此人！";
				model.addAttribute("message", message);
			} else {
				model.addAttribute("eiList", list);
			}
		}
		model.addAttribute("condition", condition);
		return "/edu3/recruit/matriculate/studentinfoquery-list";
	}

	*//**
	 * 统计录取情况
	 *//*	
	@RequestMapping("/edu3/recruit/matriculate/statMatriculate.html")
	public String statMatriculate(StatMatriculateForm form,HttpServletRequest request, Page objPage, ModelMap model)throws WebException {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		User user 					  = SpringSecurityHelper.getCurrentUser();
		String branchSchool 		  = "";
		String recruitPlan 		  = "";
		String recruitPlanForJsp  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));
		String act = ExStringUtils.trimToEmpty(request.getParameter("act"));
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("branchSchool", branchSchool);
			condition.put("isBranchSchool", true);
		}
		if (null != form) {
//			if(ExStringUtils.isNotEmpty(form.getRecruitPlan())){
//				String recruitPlan = form.getRecruitPlan();
//				String[] strArr = recruitPlan.split(",");
//				if(strArr.length ==2){
//					condition.put("recruitPlan",strArr[0] );
//					condition.put("term",strArr[1] );
//				}else{
//					condition.put("recruitPlan",recruitPlan );
//				}
//				condition.put("recruitPlanForJsp",recruitPlan );
//				
//				String recruitPlanForJsp  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));//分页从condition拿
//				strArr = recruitPlanForJsp.split(",");
//				if(strArr.length ==2){
//					condition.put("recruitPlan",strArr[0] );
//					condition.put("term",strArr[1] );
//					condition.put("recruitPlanForJsp",recruitPlanForJsp );
//				}
//			}
			if(ExStringUtils.isNotEmpty(form.getRecruitPlan()))
				recruitPlan = form.getRecruitPlan();
			if(ExStringUtils.isNotEmpty(form.getClassic()))     condition.put("classic", form.getClassic());
			if(ExStringUtils.isNotEmpty(form.getBranchSchool()))condition.put("branchSchool", form.getBranchSchool());
			if(ExStringUtils.isNotEmpty(form.getMajor()))       condition.put("major", form.getMajor());
			if(ExStringUtils.isNotEmpty(form.getTeachingType()))condition.put("teachingType", form.getTeachingType());
		}
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		condition.put("statType", "statMatriculate");//统计录取情况
		if (condition.containsKey("recruitPlan")||condition.containsKey("classic")||
				condition.containsKey("branchSchool")||condition.containsKey("major")
				||condition.containsKey("gradeName")) {
			Page page = statMatriculateBySQLService.statMatriculateByCondition(condition,objPage);
			model.addAttribute("collectCount", page);
		}
		condition.remove("isBranchSchool");
		model.addAttribute("condition", condition);
		if(ExStringUtils.isNotEmpty(act)){
			return "/edu3/recruit/matriculate/statMatriculateAdjustMajor";
		}else{
			return "/edu3/recruit/matriculate/statMatriculate";
		}
	}
	*//**
	 * 导出录取情况
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/statmatriculate-export.html")
	public void genStatMatriculateExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();
		//查询条件
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String recruitPlan=request.getParameter("recruitPlan");//招生批次
		String classic=request.getParameter("classic");//层次
		String grade=request.getParameter("grade");//年级
		String major=request.getParameter("major");//姓名
		String teachingType=request.getParameter("teachingType");//办学模式
		condition.put("statType", "statMatriculate");//统计录取情况
		
		// 2014-5-14
		if (ExStringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		
		List<StatMatriculateExport> list 					  = new ArrayList<StatMatriculateExport>();
		if(ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
//		if(ExStringUtils.isNotEmpty(recruitPlan)) condition.put("recruitPlan", recruitPlan);
		if(ExStringUtils.isNotEmpty(major)) condition.put("major", major);
		if(ExStringUtils.isNotEmpty(classic)) condition.put("classic", classic);
		if(ExStringUtils.isNotEmpty(grade)) condition.put("grade", grade);
		if(ExStringUtils.isNotEmpty(teachingType)) condition.put("teachingType", teachingType);
		
		if (condition.containsKey("recruitPlan")||condition.containsKey("classic")||
				condition.containsKey("branchSchool")||condition.containsKey("major")
				|| condition.containsKey("gradeName")) {
			list = statMatriculateBySQLService.statMatriculate(condition);
		}
		for(StatMatriculateExport s:list){
			s.setTeachingType(JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", s.getTeachingType()));
		}
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			// 导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath() + File.separator
					+ GUIDUtils.buildMd5GUID(false) + ".xls");
			if ("module".equals(exportAct)) {// 导出
				Map<String, Object> templateMap = new HashMap<String, Object>();// 设置模板
				
				// 模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+ "WEB-INF"+ File.separator+ "templates"
						+ File.separator+ "excel"+ File.separator + "StatMatriculateModule.xls";
				exportExcelService.initParmasByfile(disFile,"statmatriculateexport", list, null);// 初始化配置参数
				exportExcelService.getModelToExcel().setRowHeight(500);// 设置行高
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
				excelFile = exportExcelService.getExcelFile();// 获取导出的文件
			}
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "录取情况.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	*//**
	 * 打印录取通知书-预览
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/printnotice.html")
	public String printnotice(HttpServletRequest request,	HttpServletResponse response, ModelMap map) throws WebException {
		
		// 查询条件
		Map<String, Object> condition = new HashMap<String, Object>();
		String schoolId       = ExStringUtils.trimToEmpty(request.getParameter("schoolId"));
		String recruitPlan       = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String registerDate       = ExStringUtils.trimToEmpty(request.getParameter("registerDate"));
		String schoolName       = ExStringUtils.trimToEmpty(request.getParameter("schoolName"));
		String recruitPlanT       = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanT"));
		String printDate       = ExStringUtils.trimToEmpty(request.getParameter("printDate"));
		String enInfoIds       = ExStringUtils.trimToEmpty(request.getParameter("enInfoIds"));
		//***3.5.29
		//新增搜索项
		String isPrintOfEnroll       = ExStringUtils.trimToEmpty(request.getParameter("isPrintOfEnroll"));
		String matriculateDateL       = ExStringUtils.trimToEmpty(request.getParameter("matriculateDateL"));
		String matriculateDateH       = ExStringUtils.trimToEmpty(request.getParameter("matriculateDateH"));
		
		map.addAttribute("schoolId", schoolId);
		map.addAttribute("recruitPlan", recruitPlan);
		map.addAttribute("registerDate", registerDate);
		map.addAttribute("schoolName", schoolName);
		map.addAttribute("recruitPlanT", recruitPlanT);
		map.addAttribute("printDate", printDate);
		map.addAttribute("enInfoIds", enInfoIds);
		
		map.addAttribute("isPrintOfEnroll", isPrintOfEnroll);
		map.addAttribute("matriculateDateL", matriculateDateL);
		map.addAttribute("matriculateDateH", matriculateDateH);
		
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			schoolId = user.getOrgUnit().getResourceid();
			condition.put("brshSchool", "Y");
		}
		if (StringUtils.isNotEmpty(schoolId)) {
			condition.put("branchSchool", schoolId);
			condition.put("branchSchoolName", schoolName);
		}
		if (!ExStringUtils.isEmpty(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
		}
		
		// 已录取
		condition.put("isMatriculate", "Y");
		// 报名资格已通过
		condition.put("signupFlag", "Y");
		
		//新增查询条件
		//录取通知书是否已经打印
		if (ExStringUtils.isNotEmpty(isPrintOfEnroll)) {
			condition.put("isPrintOfEnroll", isPrintOfEnroll);
		}
		//最早录取时间
		if (ExStringUtils.isNotEmpty(matriculateDateL)) {
			condition.put("matriculateDateL", matriculateDateL);
		}
		//最迟录取时间
		if (ExStringUtils.isNotEmpty(matriculateDateH)) {
			condition.put("matriculateDateH", matriculateDateH);
		}
		if(ExStringUtils.isNotEmpty(enInfoIds)){
			String[] str = enInfoIds.split(",");
			List<EnrolleeInfo> entitys = new ArrayList<EnrolleeInfo>(0);
			for(int i = 0;i<str.length;i++){
				EnrolleeInfo entity = enrolleeInfoService.get(str[i]);
				if(null!=entity){
					entity.setIsPrintOfEnroll("Y");
					//20140728生成录取通知书序列号,每次打印都生成新的序列号
					String printNoticeNo = enrolleeInfoService.getPrintNoticeNo(entity);
					entity.setPrintNoticeNo(printNoticeNo);
					int times = entity.getPrintNoticeNoNum();
					times ++ ;
					entity.setPrintNoticeNoNum(times);
					entitys.add(entity);
				}
			}
			enrolleeInfoService.batchSaveOrUpdate(entitys);
		}else{
			enrolleeInfoService.batchMatriculateForPrintEnroll(condition, "isPrintOfEnroll");
		}
		
		
		return "/edu3/recruit/matriculate/matriculate-printview";
	}

	*//**
	 * 打印
	 * @param request
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/notice/print.html")
	public void doPrintnotice(HttpServletRequest request,	HttpServletResponse response) throws WebException {
		//Connection conn     	= null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		
		String schoolId     = ExStringUtils.trimToEmpty(request	.getParameter("schoolId"));
		String recruitPlan  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String registerDate = ExStringUtils.trimToEmpty(request.getParameter("registerDate"));
		String schoolName   = ExStringUtils.trimToEmpty(request.getParameter("schoolName"));
		String recruitPlanT = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanT"));
		String printDate    = ExStringUtils.trimToEmpty(request.getParameter("printDate"));
		String enInfoIds    = ExStringUtils.trimToEmpty(request.getParameter("enInfoIds"));
		//****3.5.30
		//新增查询项
		String isPrintOfEnroll    = ExStringUtils.trimToEmpty(request.getParameter("isPrintOfEnroll"));
		String matriculateDateL    = ExStringUtils.trimToEmpty(request.getParameter("matriculateDateL"));
		String matriculateDateH    = ExStringUtils.trimToEmpty(request.getParameter("matriculateDateH"));
		
		String fileName     = "";
		
		if (ExStringUtils.isNotEmpty(enInfoIds)) {
			String ids  = "";
			for (String id : enInfoIds.split(",")) {
				ids+=",'"+id+"'";
			}
			parameters.put("enrolleeInfoIds", ids.substring(1));
		}
		
		
		
		try {
		
			registerDate    	= ExStringUtils.isBlank(registerDate)?ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE):registerDate; 
			printDate    		= ExStringUtils.isBlank(printDate)?ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE):printDate; 
			 //conn     			= examScoreService.getConn();
			if (ExStringUtils.isNotEmpty(schoolName))
				fileName        = URLDecoder.decode(schoolName, "UTF-8");
			if (ExStringUtils.isNotEmpty(recruitPlanT))
				fileName        = URLDecoder.decode(recruitPlanT, "UTF-8") + "-"+ fileName;
			fileName            = fileName + "-录取通知";
			String reprotFile   = "";
			if (ExStringUtils.isNotEmpty(enInfoIds)) {
				reprotFile      = java.net.URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"notice_selected.jasper"),	"utf-8");
			}else {
				reprotFile      = java.net.URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"notice.jasper"),	"utf-8");
			}
			//File exe_rpt        = new File(reprotFile);
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			parameters.put("checkinDate", sdf.parse(registerDate));
			parameters.put("currentDate", DateChineseFormat.getChineseDate(printDate));
			parameters.put("orgUnitId", schoolId);
			if(ExStringUtils.isNotEmpty(recruitPlan)){
				String[] ser = recruitPlan.split("_");
				recruitPlan = ser[ser.length-1];
			}
			parameters.put("recruitPlanId", recruitPlan);
			
			if(ExStringUtils.isNotEmpty(isPrintOfEnroll))
				parameters.put("isPrintOfEnroll", isPrintOfEnroll);
			if(ExStringUtils.isNotEmpty(matriculateDateL))
				parameters.put("matriculateDateL", sdf.parse(matriculateDateL));
			if(ExStringUtils.isNotEmpty(matriculateDateH)) 
				parameters.put("matriculateDateH", sdf.parse(matriculateDateH));
			
//			parameters.put("isPrintOfEnroll", isPrintOfEnroll);
//			parameters.put("matriculateDateL", matriculateDateL);
//			parameters.put("matriculateDateH", matriculateDateH);
			
			
			//JasperPrint jasperPrint = JasperFillManager.fillReport(exe_rpt.getPath(), parameters, conn); // 填充报表
			JasperPrint jasperPrint = examScoreService.printReport(reprotFile, parameters, examScoreService.getConn());
			if (jasperPrint != null){
				renderStream(response, jasperPrint);
			}else{
				renderHtml(response, "没有打印数据...");
			}
			
			
			byte[] bytes = JasperRunManager.runReportToPdf(exe_rpt.getPath(),parameters, conn);
				
			response.setContentType("application/pdf");
			response.setContentLength(bytes.length);
			response.setHeader("Content-Disposition", "attachment; filename="+ ExamScoreController.encodeChinese(fileName, request)+ ".PDF");
			BufferedOutputStream ouputStream = new BufferedOutputStream(
					response.getOutputStream());
			ouputStream.write(bytes, 0, bytes.length);
			ouputStream.flush();
			ouputStream.close();
			conn.close();
			
		} catch (Exception e) {
			logger.error("打印录取通知书出错：{}",e.fillInStackTrace());
		}
	}
	*//**
	 * 根据招生批次ID获取所有该批次下的考试课程
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/getrecruitplanexamsubject.html")
	public void getPlanExamSubjetList(HttpServletRequest request ,HttpServletResponse response){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		List<JsonModel> subJectList  = new ArrayList<JsonModel>();
		List<JsonModel> teachingType = new ArrayList<JsonModel>();
		String planid 				 = ExStringUtils.trimToEmpty(request.getParameter("planid"));
		if (ExStringUtils.isNotEmpty(planid)) {
			subJectList 			 = getRecruitPlanExamSubject(planid);
			teachingType 			 = getRecruitPlanTeachingType(planid);
			condition.put("teachingType", teachingType);
			condition.put("subJectList", subJectList);
			
		}
		renderJson(response,JsonUtils.mapToJson(condition));
	}
	*//**
	 * 免试生录取-列表
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/noexammatriculate-list.html")
	public String noexamMatriculateList(HttpServletRequest request,	HttpServletResponse response,ModelMap model,Page objPage){
		Map<String, Object> condition = new HashMap<String, Object>();
		// 学习中心
		String branchSchool 	      = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		// 招生批次
		String recruitPlan  	      = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		// 招生专业
		String recruitMajor 	      = ExStringUtils.trimToEmpty(request.getParameter("recruitMajor"));
		// 姓名
		String name 			      = ExStringUtils.trimToEmpty(request.getParameter("name"));
		// 准考证号
		String examCertificateNo      = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		// 证件号码
		String certNum 			      = ExStringUtils.trimToEmpty(request.getParameter("certNum"));
		String recruitPlanForJsp = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanForJsp"));

		// 如果是学习中心用户，只操作本学习中心的数据
		User user 			          = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool 			  = user.getOrgUnit().getResourceid();
			model.addAttribute("brshSchool", "Y");
		}
		if (StringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
			condition.put("branchSchoolName", ExStringUtils.defaultIfEmpty(	request.getParameter("branchSchoolName"), ""));
		}
//		if (StringUtils.isNotEmpty(recruitPlan)) {
//			condition.put("recruitPlan", recruitPlan);
//		}
		
		// 2014-5-8
		if (StringUtils.isNotBlank(recruitPlan)){
			String[] year_term = recruitPlan.split("_");
			if(year_term.length==1){//年 招生计划
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
				
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlan);
		}
		if (StringUtils.isNotBlank(recruitPlanForJsp)){//分页从这取值
			String[] year_term = recruitPlanForJsp.split("_");
			if(year_term.length==1){//年
				if(year_term[0].length()==4){
					condition.put("gradeName", year_term[0]);
				}else {
					condition.put("recruitPlan", year_term[0]);
				}
			}else if(year_term.length==2){//年 季
				condition.put("gradeName", year_term[0]);
				condition.put("gradeTerm", year_term[1]);
			}else if(year_term.length==3){//年 季 招生批次
				condition.put("recruitPlan", year_term[2]);
			}
			condition.put("recruitPlanForJsp", recruitPlanForJsp);
		}
		
		if (StringUtils.isNotEmpty(recruitMajor)) {
			condition.put("recruitMajor", recruitMajor);
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
		
		condition.put("signupFlag", "Y");
		condition.put("isApplyNoexam","Y");
		condition.put("isMatriculate","N");
		
		//condition.put("signupFlag", " and ei.signupFlag='Y'");
		//condition.put("entranceFlag", " and ei.entranceflag='Y'");
		//20130816 林凯  免试资格初审复审终审都通过以后才能录取
		condition.put("noExamFlag"," and ei.noExamFlag='Y' and ei.noExamSecondFlag ='Y' and ei.noExamEndFlag ='Y' ");
	
		Page page = enrolleeInfoService.findEnrolleeByCondition(condition,objPage);
		if (StringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		model.addAttribute("eiList", page);
		model.addAttribute("condition", condition);
		
		return "/edu3/recruit/matriculate/noexammatriculate-list";
	}
	*//**
	 * 免试生录取-录取/不录取
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/noexam-matriculate.html")
	public void noexamMatriculate(String resourceid,String flag, HttpServletRequest request,HttpServletResponse response){
		if (StringUtils.isNotEmpty(resourceid)) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<EnrolleeInfo> willMatriculateEnrolleeInfos = new ArrayList<EnrolleeInfo>();
			try {
				String [] ids = resourceid.split(","); 
				for (String id:ids) {
					synchronized (this) {
						EnrolleeInfo ei = enrolleeInfoService.get(id);
						if(ExStringUtils.equals(ei.getIsMatriculate(), flag)){//如果已经执行了flag指定的操作，不用在重复执行.
							continue;
						}
						if ("Y".equals(flag)) {
							// 生成学号
							if (StringUtils.isEmpty(ei.getMatriculateNoticeNo())) {
								String matriculateNoticeNo = enrolleeInfoService.genStudentid(ei);
								ei.setMatriculateNoticeNo(matriculateNoticeNo);
							}
							// 生成注册号
							if (StringUtils.isEmpty(ei.getRegistorNo())) {
								String registorNo = enrolleeInfoService	.genEnrolleeRegistorNo(ei);
								ei.setRegistorNo(registorNo);
							}
							//获取当前系统时间，生成录取时间
							ei.setMatriculateDate(new Date());
							//录取学生，是否已打印录取通知书初始化
							ei.setIsPrintOfEnroll("N");
						}
						if ("Y".equals(flag) && ExStringUtils.isNotEmpty(ei.getBranchSchool().getIsMachineExam()) && 
						    "Y".equals(ei.getBranchSchool().getIsMachineExam())) {
							ei.setIsMachineExamScore(Constants.BOOLEAN_YES);
						}
						
						ei.setIsMatriculate(flag);
						willMatriculateEnrolleeInfos.add(ei);
						
					}
				}
				//enrolleeInfoService.saveOrUpdate(ei);
				recruitMatriculateService.doMatriculate(willMatriculateEnrolleeInfos);
				map.put("statusCode", 200);
				map.put("message", "操作成功");
				
			} catch (Exception e) {
				logger.error("免试生录取操作失败:{}",e.fillInStackTrace());
				map.put("statusCode", 300);
				map.put("message", "操作失败");
			}
			renderJson(response, JsonUtils.mapToJson(map));
		}
	}
	
	*//**
	 * 获取批次指定的学习模式
	 * @param planid
	 * @return
	 *//*
	private List<JsonModel> getRecruitPlanTeachingType(String planid){
		
		List<JsonModel> teachingType  = new ArrayList<JsonModel>();
		if (ExStringUtils.isNotEmpty(planid)) {
			RecruitPlan plan              = recruitPlanService.load(planid);
			String planType      		  = plan.getTeachingType();
			List<Dictionary> types   	  = CacheAppManager.getChildren("CodeTeachingType");
			 
			for (Dictionary dic:types) {
				
				if (ExStringUtils.isNotEmpty(planType)&&planType.indexOf(dic.getDictValue())<0) {
					continue;
				}else {
					JsonModel m  		  = new JsonModel();
					m.setKey(dic.getDictValue());
					m.setValue(dic.getDictName());
					
					teachingType.add(m);
				}
			}
		}
		
		
		return teachingType;
	}
	*//**
	 * 获取批次所有招生专业的考试课程
	 * @param planid
	 * @return
	 *//*
	private List<JsonModel> getRecruitPlanExamSubject(String planid){
		List<JsonModel> subJectList  	    = new ArrayList<JsonModel>();
		Map<String,Object> condition        = new HashMap<String, Object>();
		if (ExStringUtils.isNotEmpty(planid)) {
			condition.put("recruitPlan", planid);
			List<RecruitMajor>majors 	    = recruitMajorService.findMajorByCondition(condition);
			condition.clear();
			for (RecruitMajor major:majors) {
				Set<ExamSubject> subjectSet =  major.getExamSubject();
				if (null!=subjectSet && !subjectSet.isEmpty()) {
					for (ExamSubject subject:subjectSet) {
						JsonModel model	    = new JsonModel();
						String coursename   = JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam", subject.getCourseName());
						String coursenamestr= coursename+"-"+ major.getRecruitMajorName();
						model.setKey(subject.getResourceid());
						model.setValue(coursenamestr);
						if (coursenamestr.length()>9) {
							model.setName(coursenamestr.substring(0,9)+"...");
						}else {
							model.setName(coursenamestr);
						}
						
						subJectList.add(model);
					}
				}
			}
		}
		
		return subJectList;
	}
	*//** 字符编码转换 *//*
	public static String encodeChinese(String field, HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");
		boolean isMSIE = (agent != null && agent.indexOf("MSIE") != -1);
		String f = "";
		try {
			if (isMSIE) { // IE
				f = URLEncoder.encode(field, "UTF-8");
			} else { // Other
				f = new String(field.getBytes("UTF-8"), "ISO-8859-1");
			}
			
			 * f = java.net.URLEncoder.encode(field, "UTF-8");
			 * if(f.length()>150){ // IE6对header的长度限制在150字节左右 f = new String(
			 * field.getBytes("gb2312"), "ISO8859-1"); }
			 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return f;
	}

     //1516~end @author: GCHW
	 //功能：导入导出打印入学成绩报表 统计入学测试人数（按课程名称、按时间段）
	
	*//**
	 * 导出入学测试成绩-条件选择
	 * 
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/exportRecruitExamScoreCondition.html")
	public String exportRecruitExamScoreCondition(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		map.addAttribute("isTemplate", request.getParameter("isTemplate"));
		return "/edu3/recruit/matriculate/exportRecruitExamScoreCondition";
	}

	
	*//**
	 * 导出成绩单
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/examScore/excel/export.html")
	public void exportRecruitExamScore(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		//获得参数
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		String recruitPlan = request.getParameter("recruitPlan");
		String examSubject = request.getParameter("examSubject");
		
		List<ExamScore> list = examScoreService.findByHql(" from "+ExamScore.class.getSimpleName()+" where examSubject.courseName=? and examSubject.recruitMajor.recruitPlan.resourceid=? and isDeleted=0 and originalPoint is not null " 
				,new Object[] { examSubject, recruitPlan });
		Long showNo = new Long(0); 
		for (ExamScore examScore : list) {
			showNo++;
			examScore.setShowOrderNo(showNo);
		}
		//如果没有任何成绩记录
		if (0 == list.size()) {
			System.out.println("找不到对应招生批次和科目的成绩记录");
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+ "exportfiles");
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath() + File.separator+ GUIDUtils.buildMd5GUID(false) + ".xls");
			if ("module".equals(exportAct)) {// 导出
				Map<String, Object> templateMap = new HashMap<String, Object>();// 设置模板
				templateMap.put("recruitPlanName", "#recruitPlanName#");
				templateMap.put("courseName", "#courseName#");
				templateMap.put("examDate", "#examDate#");
				templateMap.put("examForm", "#examForm#");
				// 模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+ "WEB-INF"+ File.separator+ "templates"+ File.separator+ "excel"+ File.separator + "RecruitExamScoreModule.xls";
				exportExcelService.initParmasByfile(disFile,"recruitexamscore", list, null);// 初始化配置参数
				exportExcelService.getModelToExcel().setRowHeight(500);// 设置行高
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 5, templateMap);
				excelFile = exportExcelService.getExcelFile();// 获取导出的文件
			}
			logger.info("获取导出的excel文件:{}", excelFile.getAbsoluteFile());
			try {
				downloadFile(response, "无记录的成绩单.xls",
						excelFile.getAbsolutePath(), true);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}
		// 模板需要的值 课程名称 招生批次 考查方式（未找到对应字段） 考试时间
		// 课程名称
		String courseName = examSubject;
		List<Dictionary> tmp1 = CacheAppManager.getChildren("CodeEntranceExam");
		for (Dictionary dictionary : tmp1) {
			if (null != dictionary.getDictValue()) {
				if (courseName.equals(dictionary.getDictValue())) {
					courseName = dictionary.getDictName();
				}
			}
		}
		// 招生批次
		ExamScore tmp = list.get(0);
		RecruitPlan rp = recruitPlanService.findUniqueByProperty("resourceid",recruitPlan);
		String recruitPlanName = rp.getRecruitPlanname();
		// 考试时间
		Calendar calendar = Calendar.getInstance();
		NumberFormat formatter = NumberFormat.getInstance();
		formatter.setMinimumIntegerDigits(2);
		Date tmpDate = tmp.getExamSubject().getStartTime();
		calendar.setTime(tmpDate);
		int year     = calendar.get(Calendar.YEAR);
		String month = formatter.format(calendar.get(Calendar.MONTH));
		String date  = formatter.format(calendar.get(Calendar.DATE));
		String examDate =year+"年"+month+"月"+date+"日"; 
		// 考试方式
		//String examForm = "暂未找到考察方式字段值";
		String examForm = "";
		//导出到excel
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+ "exportfiles");
		try {
			// 导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath() + File.separator
					+ GUIDUtils.buildMd5GUID(false) + ".xls");
			if ("module".equals(exportAct)) {// 导出
				Map<String, Object> templateMap = new HashMap<String, Object>();// 设置模板
				templateMap.put("recruitPlanName", recruitPlanName);
				templateMap.put("courseName", courseName);
				templateMap.put("examDate", examDate);
				templateMap.put("examForm", examForm);
				// 模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+ "WEB-INF"+ File.separator+ "templates"
						+ File.separator+ "excel"+ File.separator + "RecruitExamScoreModule.xls";
				exportExcelService.initParmasByfile(disFile,"recruitexamscore", list, null);// 初始化配置参数
				exportExcelService.getModelToExcel().setRowHeight(500);// 设置行高
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 5, templateMap);
				excelFile = exportExcelService.getExcelFile();// 获取导出的文件
			}
			logger.info("获取导出的excel文件:{}", excelFile.getAbsoluteFile());
			downloadFile(response, recruitPlanName + "_"+ courseName + "入学测试成绩单.xls",excelFile.getAbsolutePath(), true);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("导出excel文件出错：" + e.fillInStackTrace());
		}
	}
	
	*//**
	 * 打印入学成绩报表-条件选择
	 * 
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/printRecruitExamScoreCondition.html")
	public String printRecruitExamScoreCondition(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		return "/edu3/recruit/matriculate/printRecruitExamScoreCondition";
	}

	*//**
	 * 打印入学成绩表-预览
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/report/print.html")
	public String printRecruitExamScorePreview(HttpServletRequest request,HttpServletResponse response, ModelMap model) {
		model.addAttribute("recruitPlan",ExStringUtils.trimToEmpty(request.getParameter("recruitPlan")));
		model.addAttribute("examSubject",ExStringUtils.trimToEmpty(request.getParameter("examSubject")));
		return "/edu3/recruit/matriculate/printRecruitExamScorePreview";
	}

	*//**
	 * 打印入学成绩单
	 * 
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("/edu3/framework/recruit/enroll/printRecruitExamScore.html")
	public void printRecruitExamScore(HttpServletRequest request,HttpServletResponse response) {
		//获得参数
		String recruitPlan = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String examSubject = ExStringUtils.trimToEmpty(request.getParameter("examSubject"));
		Map<String, Object> param = new HashMap<String, Object>();
		//传参
		param.put("planname", "g.recruitplanid='" + recruitPlan + "'");
		param.put("coursename", "c.coursename='" + examSubject + "'");
		//创建打印对象
		JasperPrint jasperPrint = null;
		//确定连接
		//Connection conn		    = null;
		try {			
			//conn		   	    = enrolleeInfoService.getConn();
			//创建模板
			String reportfile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+ "reports"+ File.separator+ "recruitPrint"+ File.separator+ "recruitExamScore_crosstab.jasper"),"utf-8");
			File report_file = new File(reportfile);
			//jasperPrint = JasperFillManager.fillReport(report_file.getPath(),param, conn);
			jasperPrint = enrolleeInfoService.printReport(report_file.getPath(),param, enrolleeInfoService.getConn());
			if (null != jasperPrint) {
				renderStream(response, jasperPrint);
			} else {
				renderHtml(response, "缺少打印数据！");
			}
		} catch (Exception e) {			
			logger.error("打印入学测试成绩单失败{}", e.fillInStackTrace());
			renderHtml(response, "打印入学测试成绩单失败。");
		}
	}

	*//**
	 * 入学测试成绩Excel导入-导出模板还是导入的选择并进行条件选择
	 * 
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/importRecruitExamScoreStep1.html")
	public String importRecruitExamScoreStep1(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		return "/edu3/recruit/matriculate/importRecruitExamScoreStep1";
	}
	
	*//**
	 * 导出成绩模板
	 * 
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/examScore/excel/exportRecruitExamTemplate.html")
	public void exportRecruitExamTemplate(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		//获取参数
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		String recruitPlan = request.getParameter("recruitPlan");
		String examSubject = request.getParameter("examSubject");
		String  rendResponseStr = "";
		
		//根据科目和招生批次查找考试科目表对应的成绩表
		List<ExamScore> list = examScoreService.findByHql(" from "+ExamScore.class.getSimpleName()+" where examSubject.courseName=? and examSubject.recruitMajor.recruitPlan.resourceid=? and isDeleted=0 and originalPoint is null" 
				,new Object[] { examSubject, recruitPlan });
		if (0 == list.size()) {
			System.out.println("找不到对应招生批次和科目的成绩记录");
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+ "exportfiles");
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath() + File.separator+ GUIDUtils.buildMd5GUID(false) + ".xls");
			if ("module".equals(exportAct)) {// 导出
				Map<String, Object> templateMap = new HashMap<String, Object>();// 设置模板
				templateMap.put("recruitPlanName", "#recruitPlanName#");
				templateMap.put("courseName", "#courseName#");
				templateMap.put("examDate", "#examDate#");
				templateMap.put("examForm", "#examForm#");
				// 模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+ "WEB-INF"+ File.separator+ "templates"+ File.separator+ "excel"+ File.separator + "RecruitExamScoreModule.xls";
				exportExcelService.initParmasByfile(disFile,"recruitexamscore", list, null);// 初始化配置参数
				exportExcelService.getModelToExcel().setRowHeight(500);// 设置行高
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 5, templateMap);
				excelFile = exportExcelService.getExcelFile();// 获取导出的文件
			}
			logger.info("获取导出的excel文件:{}", excelFile.getAbsoluteFile());
			try {
				downloadFile(response, "空的成绩模板.xls",
						excelFile.getAbsolutePath(), true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
			
		}else {
			// 模板需要的值 课程名称 招生批次 考查方式（未找到对应字段） 考试时间
			// 课程名称
			String courseName = examSubject;
			List<Dictionary> tmp1 = CacheAppManager.getChildren("CodeEntranceExam");
			for (Dictionary dictionary : tmp1) {
				if (null != dictionary.getDictValue()) {
					if (courseName.equals(dictionary.getDictValue())) {
						courseName = dictionary.getDictName();
					}
				}
			}
			// 招生批次
			ExamScore tmp = list.get(0);
			RecruitPlan rp = recruitPlanService.findUniqueByProperty("resourceid",recruitPlan);
			String recruitPlanName = rp.getRecruitPlanname();
			// 考试时间
			Calendar calendar = Calendar.getInstance();
			NumberFormat formatter = NumberFormat.getInstance();
			formatter.setMaximumIntegerDigits(2);
			Date tmpDate = tmp.getExamSubject().getStartTime();
			calendar.setTime(tmpDate);
			int year     = calendar.get(Calendar.YEAR);
			String month = formatter.format(calendar.get(Calendar.MONTH));
			String date  = formatter.format(calendar.get(Calendar.DATE));
			String examDate =year+"年"+month+"月"+date+"日"; 
			// 考试方式
			//String examForm = "暂未找到考察方式字段值";
			String examForm = "";
			// 文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+ "exportfiles");
			try {
				// 导出
				File excelFile = null;
				GUIDUtils.init();
				File disFile = new File(getDistfilepath() + File.separator+ GUIDUtils.buildMd5GUID(false) + ".xls");
				if ("module".equals(exportAct)) {// 导出
					Map<String, Object> templateMap = new HashMap<String, Object>();// 设置模板
					templateMap.put("recruitPlanName", recruitPlanName);
					templateMap.put("courseName", courseName);
					templateMap.put("examDate", examDate);
					templateMap.put("examForm", examForm);
					// 模板文件路径
					String templateFilepathString = SystemContextHolder.getAppRootPath()+ "WEB-INF"+ File.separator+ "templates"+ File.separator+ "excel"+ File.separator + "RecruitExamScoreModule_template.xls";
					exportExcelService.initParmasByfile(disFile,"recruitexamscore_ex", list, null);// 初始化配置参数
					exportExcelService.getModelToExcel().setRowHeight(500);// 设置行高
					exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 5, templateMap);
					excelFile = exportExcelService.getExcelFile();// 获取导出的文件
				}
				logger.info("获取导出的excel文件:{}", excelFile.getAbsoluteFile());
				downloadFile(response, recruitPlanName + "_" + courseName
						+ "入学测试成绩单模板.xls", excelFile.getAbsolutePath(), true);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("导出excel文件出错：" + e.fillInStackTrace());
			}
			
		}		
	}

	*//**
	 * 入学测试成绩Excel导入-路径输入
	 * 
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/importRecruitExamScoreStep2Url.html")
	public String importRecruitExamScoreStep2Url(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		map.addAttribute("recruitPlan", request.getParameter("recruitPlan"));
		map.addAttribute("examSubject", request.getParameter("examSubject"));
		return "/edu3/recruit/matriculate/importRecruitExamScoreStep2Url";
	}

	*//**
	 * 入学测试成绩Excel导入
	 * 
	 * @param request
	 * @param response
	 * @throws WebException
	 *//*

	@RequestMapping("/edu3/recruit/examScore/import.html")
	public void importRecruitExamScoreStep3(HttpServletRequest request,HttpServletResponse response) throws WebException {
		StringBuffer stringBuffer = new StringBuffer();
		String exportAct = ExStringUtils.defaultIfEmpty(request.getParameter("act"), "");
		//提示信息字符串
		String  rendResponseStr = "";
		//更新情况数据
		int hasScoreQuantity =0;
		int nullScoreQuantity =0;
		int clearStuQuantity = 0;
		if ("import".equals(exportAct)) {
			//设置目标文件路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+ request.getParameter("importFile"));
			try {
				//上传文件到服务器
				List<AttachInfo> list = doUploadFile(request, response, null);
				AttachInfo attachInfo = list.get(0);
				//创建EXCEL对象 获得待导入的excel的内容
				File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
				importExcelService.initParmas(excel, "recruitexamscoreimport",null);
				importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
				//获得待导入excel内容的List
				List modelList = importExcelService.getModelList();
				//转换为对应类型的List
				List<ExamScore_import> list2 = new ArrayList<ExamScore_import>();
				for (int i = 0; i < modelList.size(); i++) {
					ExamScore_import examScore_view = (ExamScore_import) modelList.get(i);
					list2.add(examScore_view);
				}
				int i = 0;
				//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
				for (ExamScore_import examScore_view : list2) {
					String resourceid = examScore_view.getUniCode();
					ExamScore eScore = examScoreService.findUniqueByProperty("resourceid", resourceid);
					if (i < list2.size()&& null!=eScore) {
						if (null == eScore.getOriginalPoint()&& !("").equals(examScore_view.getOriginalPoint())) {
							eScore.setOriginalPoint(new Double(examScore_view.getOriginalPoint()));
							examScoreService.update(eScore);
							i++;
						} else {
							stringBuffer.append(examScore_view.getName()+ "导入失败_原因:");
							if (("").equals(examScore_view.getOriginalPoint())) {
								stringBuffer.append("导入数据为空  ");
								nullScoreQuantity++;
							}
							if (null != eScore.getOriginalPoint()) {
								stringBuffer.append("待导入部分已有数据");
								hasScoreQuantity++;
							}
							if ("清空".equals(examScore_view.getOriginalPoint())){
								eScore.setOriginalPoint(null);
								examScoreService.update(eScore);
								stringBuffer.append("清空数据");
								clearStuQuantity++;
							}
							stringBuffer.append("\\n");
						}
					}
					
				}
				
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+i
				+"条 | 数据库已有成绩无法导入"+hasScoreQuantity
				+"条 | 导入数据为空"+nullScoreQuantity
				+"条 | 清空数据"+clearStuQuantity
				+"条'};";
				
			} catch (Exception e) {
				rendResponseStr = "{statusCode:300,message:'操作失败!'};";
				
			}
		}
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	

	*//**
	 * 导出入学测试预约人数统计-条件选择
	 * 
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/statReserveRecruitExamCondition.html")
	public String statReserveRecruitExamCondition(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		return "/edu3/recruit/matriculate/statReserveRecruitExamCondition";
	}	
	
	*//**
	 * 导出入学测试预约人数统计
	 * @param request
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/examScore/exportStatReserveRecruitExam.html")
	public void exportStatReserveRecruitExam(HttpServletRequest request , HttpServletResponse response) throws WebException{
		//因为无法通过dictionaryService来进行查询，所以这里按照入学测试课程在字典中的编号来进行了设置
		String[] courseCodes={"高中数学","高中语文","高中英语","高等数学","写作与翻译","机械设计基础","邓小平理论概论","民法学","电子技术","会计学原理","英语","C语言程序设计","管理学原理","大学语文","综合英语","结构力学"};
		Map<String,Integer> codeAndValue = new HashMap<String,Integer>(0);
		for (int i = 0; i < courseCodes.length; i++) {
			codeAndValue.put( courseCodes[i],i);
		}
		//获得参数年度和学期
		String yearInfo = request.getParameter("yearInfo") ;
		String term = request.getParameter("term");
		//第一部分 非机考
		//查找对应的年度和学期对应的招生批次
		List<RecruitPlan> list_recruitPlans = recruitPlanService.findByHql(" from "+RecruitPlan.class.getSimpleName()+" where yearInfo.resourceid=? and term=? and isDeleted=0 ", new Object[]{yearInfo,term});
		//按课程分类 存放ExamSubject的List
		Map<String,List<ExamSubject>> map_examSubjects = new HashMap<String, List<ExamSubject>>(0);
		if(list_recruitPlans.size()>0){
			//某年度学期下的若干个招生批次
			for (RecruitPlan recruitPlan : list_recruitPlans) {
				//得到其中一个招生批次
				String recruitPlanId = recruitPlan.getResourceid();
				//查找这个招生批次下的考试科目  每次遍历中都会将具有相同考试课程 考试时间 相应层次的ExamSubject进行归类
				List<ExamSubject> list_examSubject1 = examSubjectService.findByHql(" from "+ExamSubject.class.getSimpleName()+" where recruitMajor.recruitPlan.resourceid=? and isDeleted=0 ", recruitPlanId);
				if(list_examSubject1.size()>0){
					for (ExamSubject examSubject : list_examSubject1) {
						String start1      = examSubject.getStartTime().toString().substring(0,examSubject.getStartTime().toString().lastIndexOf(':'));
						String end1        = examSubject.getEndTime().toString().substring(examSubject.getEndTime().toString().lastIndexOf(' ')+1,examSubject.getEndTime().toString().lastIndexOf(':'));
						String classic1    = examSubject.getRecruitMajor().getClassic().getShortName();
						String courseName1 = examSubject.getCourseName();
						String key1        =start1+"_"+end1+"_"+classic1+"_"+ courseName1;
						if(map_examSubjects.size()>0&&map_examSubjects.containsKey(key1)){
							List<ExamSubject> tmp = map_examSubjects.get(key1);
							tmp.add(examSubject);
							map_examSubjects.put(key1,tmp);
						}else{
							List<ExamSubject> tmp = new ArrayList<ExamSubject>(0);
							tmp.add(examSubject);
							map_examSubjects.put(key1,tmp );
						}
					}
				}	
			}
		}
		//存放全局数据的List<Map>
		List<Map<String,String>> list_dataRes = new ArrayList<Map<String,String>>();
		//存放分教点人数合计
		Map<String, String> map = new HashMap<String, String>();
		map.put("showOrder", "合计");
		//存放标题
		Map<String, String> map_title = new HashMap<String, String>(); 
		//记录序号
		Integer showOrder = 0; 
		if(map_examSubjects.size()>0){
			Set<String> set1 = map_examSubjects.keySet(); 
			for (String str : set1) {
	 			//每个考试科目的人数总计
	 			Integer total = 0; 
	 			//每条考试记录自增 
	 			showOrder++;
	 			//每一行记录存放为一个Map类型
	 			Map<String, String> map_local = new HashMap<String, String>();
	 			//获得map的键的字符串数组
	 			String[] info     = str.split("_"); 
	 			//课程名称
	 			String courseName = info[3];
				//层次
	 			String classic    = info[2];
	 			//考试时间
				String examTime   = info[0]+"—"+info[1];
	 			//课程编码:直接从代码中按数据库字典设置的字典进行查询
				String courseCode = null; 
				if (codeAndValue.containsKey(JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",courseName ))) {
					courseCode  = codeAndValue.get(JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",courseName )).toString();	
				}else{
					courseCode  = "未找到对应课程编码";
				}
				
				map_local.put("courseCode",courseCode);
				map_local.put("courseName",JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",courseName )+"【非机考】");
				map_local.put("classic", classic);
				map_local.put("showOrder", showOrder.toString());
				map_local.put("actNum", "");
				map_local.put("memo", "");
				map_local.put("examTime", examTime.toString());
				List<ExamSubject> examSubjects = map_examSubjects.get(str);
				List<Map<String,String>> hasFound = new ArrayList<Map<String,String>>(0);
				List<ExamSubject> a = new ArrayList<ExamSubject>(0);
				for (ExamSubject examSubject : examSubjects) {
					//招生批次
		 			String recruitPlanId = examSubject.getRecruitMajor().getRecruitPlan().getResourceid();
					//招生批次和层次的组合作为查询的依据 
		 			//因为examsubject会因为同一招生批次同一层次不同的专业而导致 同一招生批次下的多个ExamSubject
		 			boolean isHas = false;
		 			for (Map<String, String> map2 : hasFound) {
		 				if(map2.containsKey(recruitPlanId)&&map2.containsValue(classic)){
		 					isHas = true;
		 				}
		 			}
		 			if(isHas==true){
		 				a.add(examSubject);
		 				continue;
		 			}else{
		 				
		 				Map<String,String> tmpMap = new HashMap<String,String>(0);
		 				tmpMap.put(recruitPlanId,classic);
		 				hasFound.add(tmpMap);
		 			}
		 			//查询条件为 符合以上所得单条考试科目的批次且为该招生批次下的学生。在ExamRoomplan有记录说明已经预约了考试
					//不对其科目进行限制，是因为默认了考生要考所在批次、层次下所有应该考的科目，数据库中未发现关于考生与科目的直接关联的信息
					List<ExamRoomPlan> list_examRoomPlan =  examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" where enrolleeinfo.recruitMajor.classic.shortName=?  and enrolleeinfo.recruitMajor.recruitPlan.resourceid=? and recruitExamPlan.resourceid is null and isDeleted=0 " , new Object[]{classic,recruitPlanId});
					if (list_examRoomPlan.size()>0) {
						for (ExamRoomPlan examRoomPlan : list_examRoomPlan) {
							total++;
							OrgUnit tmp_unitOrgUnit = examRoomPlan.getExamRoom().getBranchSchool();
							String unitCode = tmp_unitOrgUnit.getUnitCode();
							String unitName = tmp_unitOrgUnit.getUnitShortName();
							if(!map_local.containsKey(unitCode)){
								map_local.put(unitCode, "1");
							}else{
								map_local.put(unitCode, Integer.toString(Integer.parseInt(map_local.get(unitCode))+1));
							}
							//关于标题的记录
							if(!map_title.containsKey(unitCode)){
								map_title.put(unitCode, unitName);
							}
							if (!map.containsKey(unitCode)) {
								map.put(unitCode, "1");
							}else {
								map.put(unitCode, Integer.toString(Integer.parseInt(map.get(unitCode))+1));
							}
						}
						map_local.put("total", total.toString());
					}
					
				}
				if (map_local.size()>0) {
					list_dataRes.add(map_local);
			}				
		}
		//第二部分 机考
		//机考统计的方式跟非机考的有所不同
		//因为在edu_recruit_examroomplan这张表中的examplan表示有此id 则被安排了机考
		//非机考没有这个关联 ，只能从招生批次的层次的角度对报考了专业的考生 默认为要参加所有的需要科目的考试
		//机考则相对有针对性些 edu_recruit_examroomplan edu_recruit_examplan edu_recruit_expldetails(包含了课程信息)三表关联
		List<RecruitExamPlanDetails> examPlanDetails = new ArrayList<RecruitExamPlanDetails>(0);
		//找到具体对应的考试科目 不考虑人数问题
		if(list_recruitPlans.size()>0){
			for (RecruitPlan recruitPlan : list_recruitPlans) {
				//遍历符合年度学期条件下各个招生批次的详细入学测试情况
				String recruitPlanId = recruitPlan.getResourceid();
				List<RecruitExamPlanDetails> tmp_details = recruitExamPlanDetailsService.findByHql(" from "+RecruitExamPlanDetails.class.getSimpleName()+" where recruitExamPlan.recruitPlan.resourceid=? and isDeleted=0", recruitPlanId);
				//同样的 因为招生批次不作为统计的条件 所以要把不同招生批次下,相同的考试时间 考试课程 考试场次的考试科目过滤出来
				if (tmp_details.size()>0) {
					for (RecruitExamPlanDetails recruitExamPlanDetails : tmp_details) {
						//没有直接添加的原因是为了去除重复
						if(examPlanDetails.size()>0){
							boolean isAdd = true;
							for (RecruitExamPlanDetails recruitExamPlanDetails2 : examPlanDetails) {
								String starttime1 = recruitExamPlanDetails.getStartTime().toString();
								String starttime2 = recruitExamPlanDetails2.getStartTime().toString();
								String endtime1 = recruitExamPlanDetails.getStartTime().toString();
								String endtime2 = recruitExamPlanDetails2.getStartTime().toString();
								String explan1 = recruitExamPlanDetails.getRecruitExamPlan().getResourceid();
								String explan2 = recruitExamPlanDetails2.getRecruitExamPlan().getResourceid();
								String course1 = recruitExamPlanDetails.getCourseName();
								String course2 = recruitExamPlanDetails2.getCourseName();
								if (starttime1.equals(starttime2)&& endtime1.equals(endtime2)&& course1.equals(course2)&&explan1.equals(explan2)){
									isAdd = false;
								}
							}
							if (isAdd) {
								examPlanDetails.add(recruitExamPlanDetails);
							}
						}else{
							examPlanDetails.add(recruitExamPlanDetails);								
						}
					}
				}
					
			}
				
		}
		//注意 RecruitExamPlanDetails是不能直接作为统计条目来操作的
		//因为 可能出现相同科目 相同考试时间 考试批次不同 但是学生的层次相同的情况
		//如 某招生批次的某层次下 对应多个多个考试批次 以及多个招生批次某层次 对应多个考试批次
		//不像在非机考中ExamSubject的直接去重会导致招生批次减少，这里无关招生批次 因为考试批次已经与examRoomPlan取得了联系
		//也就是说要得到类似于Map<科目_时间_层次，List<ExamPlan>>
		Map<String,List<RecruitExamPlanDetails>> map_machinalExam = new HashMap<String, List<RecruitExamPlanDetails>>(0);
		for (RecruitExamPlanDetails recruitExamPlanDetails : examPlanDetails) {
			//获得考场批次
	 		String examPlan = recruitExamPlanDetails.getRecruitExamPlan().getResourceid();
			//获得层次信息
	 		//因为关联机考科目与学生信息的只有ExamRoomPlan中的enrolleeifoid 与 examplanid 两个字段
	 		//examplan与explandetails是一对多的关系 一个details应该对应一个examplan 所以通过examplan 找到符合要求的考生看他们的层次是否一致
	 		//这有两种情况导致“无法确定层次信息”一个是该场次没有学生 从而确定不了该时间段下考试科目的层次
	 		//另一个是该场次下的学生有多个层次
	 		List<ExamRoomPlan> tmp_for_classic = examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" where recruitExamPlan.resourceid=? and isDeleted=0 ", examPlan);
			String classic = "无人参考无层次";
			List<String> classics = new ArrayList<String>(0);
			if(tmp_for_classic.size()>0){
				for (ExamRoomPlan examRoomPlan : tmp_for_classic) {
					String tmp_class = examRoomPlan.getEnrolleeinfo().getRecruitMajor().getClassic().getShortName(); 
					classics.add(tmp_class);
				}
			}
			boolean isConfirm = true;
			int i=0;
			for (String string : classics) {
				if(i==0){
					classic=string;
				}
				if(!classic.equals(string)){
					isConfirm = false;
					classic="无法确定层次";
					break;
				}
			}
			if(isConfirm&&classics.size()>0){
				classic=classics.get(0);
			}
			String start1  = recruitExamPlanDetails.getStartTime().toString().substring(0,recruitExamPlanDetails.getStartTime().toString().lastIndexOf(':'));
			String end1    = recruitExamPlanDetails.getEndTime().toString().substring(recruitExamPlanDetails.getEndTime().toString().indexOf(' ')+1,recruitExamPlanDetails.getEndTime().toString().lastIndexOf(':'));
			String course1 = recruitExamPlanDetails.getCourseName();
			String key     = start1+"#"+end1+"#"+course1+"#"+classic;
			if(map_machinalExam.size()>0&&map_machinalExam.containsKey(key)){
				List<RecruitExamPlanDetails> value = map_machinalExam.get(key);
				value.add(recruitExamPlanDetails);
				map_machinalExam.put(key,value);
			}else{
				List<RecruitExamPlanDetails> value = new ArrayList<RecruitExamPlanDetails>(0);
				value.add(recruitExamPlanDetails);
				map_machinalExam.put(key,value);
			}
		}
		//当有相应科目的时候 可以统计人数
		if(map_machinalExam.size()>0){
			Set<String> set_machinalExam = map_machinalExam.keySet();
			for (String str : set_machinalExam) {
		 		//每条考试科目记录下的总计
		 		Integer total = 0; 
		 		showOrder++;
		 		//每一行记录存放为一个Map类型
		 		Map<String, String> map_local = new HashMap<String, String>();
		 		//解析Map的键
		 		String[] tmp_str  = str.split("#");
		 		//获得课程名
		 		String courseName = tmp_str[2];
		 		//获得层次
		 		String classic    = tmp_str[3]; 
				//获得时间
		 		String examTime   = tmp_str[0]+"—"+tmp_str[1];
		 		//获得课程编码 入学测试的课程编码不在course表当中 
				String courseCode = null; 
				if (codeAndValue.containsKey(JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",courseName ))) {
					courseCode = codeAndValue.get(JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",courseName )).toString();
				}else{
					courseCode = "未找到对应课程编码";
				}
				map_local.put("courseCode",courseCode);
				map_local.put("courseName",JstlCustomFunction.dictionaryCode2Value("CodeEntranceExam",courseName )+"【机考】");
				map_local.put("classic", classic);
				map_local.put("showOrder", showOrder.toString());
				map_local.put("actNum", "");
				map_local.put("memo", "");
				map_local.put("examTime", examTime);
				List<RecruitExamPlanDetails> list_reRecruitExamPlanDetails = map_machinalExam.get(str);
				for (RecruitExamPlanDetails recruitExamPlanDetails : list_reRecruitExamPlanDetails) {
					//考试场次
					String examPlan = recruitExamPlanDetails.getRecruitExamPlan().getResourceid();
					List<ExamRoomPlan> list_examRoomPlan =  examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" where recruitExamPlan.resourceid=? and recruitExamPlan.resourceid is not null and isDeleted=0 ",examPlan);
					if (list_examRoomPlan.size()>0) {
						for (ExamRoomPlan examRoomPlan : list_examRoomPlan) {
							total++;
							OrgUnit tmp_unitOrgUnit = examRoomPlan.getExamRoom().getBranchSchool();
							String unitCode = tmp_unitOrgUnit.getUnitCode();
							String unitName = tmp_unitOrgUnit.getUnitShortName();
							if(!map_local.containsKey(unitCode)){
								map_local.put(unitCode, "1");
							}else{
								map_local.put(unitCode, Integer.toString(Integer.parseInt(map_local.get(unitCode))+1));
							}
							if(!map_title.containsKey(unitCode)){
								map_title.put(unitCode, unitName);
							}
							if (!map.containsKey(unitCode)) {
								map.put(unitCode, "1");
							}else {
								map.put(unitCode, Integer.toString(Integer.parseInt(map.get(unitCode))+1));
							}
						}
						map_local.put("total", total.toString());
					}	
				}
				if (map_local.size()>0) {
					list_dataRes.add(map_local);
				}
			}
		}
		//最后统计最终总数
		Set<String> set = map.keySet();
		Integer totalInAll = 0;
		for (String str : set) {
			if(!str.equals("showOrder")){
				totalInAll += Integer.parseInt(map.get(str));
			}
		}
		map.put("total", totalInAll.toString());
		list_dataRes.add(map);	
		}
		//以上完成了所有的统计工作
		//导出Excel
		Map<String,String> titleAhead = new HashMap<String, String>();
		Map<String,String> titleBack  = new HashMap<String, String>();
		titleAhead.put("showOrder", "序号");
		titleAhead.put("courseCode", "课程编码");
		titleAhead.put("courseName", "课程名称");
		titleAhead.put("classic", "层次");
		titleAhead.put("examTime", "考试时间");
		titleBack.put("total", "总报考人数");
		titleBack.put("actNum", "实到人数");
		titleBack.put("memo", "备注");
		doExport(yearInfo, term, "test1","入学测试人数统计表", map_title, titleAhead, titleBack, list_dataRes, response,5);
		
		
		
	}  
	
	
	*//**
	 * 导出入学测试预约人数统计(按时间段)-条件选择
	 * 
	 * @return
	 *//*
	@RequestMapping("/edu3/recruit/examScore/statReserveRecruitExamByTimeCondition.html")
	public String statReserveRecruitExamByTimeCondition(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws WebException {
		return "/edu3/recruit/matriculate/statReserveRecruitExamByTimeCondition";
	}
	

	
	*//**
	 * 导出入学测试预约人数统计（按时间段）
	 * @param request
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/examScore/exportStatRecruitExamByTime.html")
	public void exportStatRecruitExamByTime(HttpServletRequest request,HttpServletResponse response) throws WebException{
		//获得参数年度和学期
		String yearInfo = request.getParameter("yearInfo") ;
		String term = request.getParameter("term");
		//获得对应招生批次
		List<RecruitPlan> recruitPlans = recruitPlanService.findByHql(" from "+RecruitPlan.class.getSimpleName()+" where yearInfo.resourceid=? and term=? and isDeleted=0 ", new Object[]{yearInfo,term});
		//遍历Map 得到填入表中的数据 （Map中已经将各个时间段分好了类）
		//填表主要数据项
		List<Map<String,String>> list_dataRes = new ArrayList<Map<String,String>>(0);
		//全局标题
		Map<String,String> map_title = new HashMap<String, String>(0);
		//全局总计
		Map<String,String> map = new HashMap<String, String>(0);
		map.put("showOrder", "合计");
		//第一部分 非机考
		//找到批次下对应的非机考考试科目时间段信息   
		Map<String,List<ExamSubject>> map_examSubjectInfo = new HashMap<String, List<ExamSubject>>(0); 
		for (RecruitPlan recruitPlan : recruitPlans) {
			String recruitPlanId = recruitPlan.getResourceid();
			//查询该招生批次下所有的examSubject 
			List<ExamSubject> tmp_exSub = examSubjectService.findByHql(" from "+ExamSubject.class.getSimpleName()+" where recruitMajor.recruitPlan.resourceid=? and isDeleted=0 ", recruitPlanId);
			if(tmp_exSub.size()>0){
				for (ExamSubject examSubject : tmp_exSub) {
					//按照考试时间查询
					String startTime = examSubject.getStartTime().toString();
					String endTime   = examSubject.getEndTime().toString();
					String period = "";
					if(Integer.valueOf(startTime.substring(startTime.indexOf(' ')+1, startTime.indexOf(':')))>=12){
						period = "下午";
					}else{
						period = "上午";
					}
					String time1 = startTime.substring(0,startTime.lastIndexOf(' '))+" "+period+" "+startTime.substring(startTime.indexOf(' '),startTime.lastIndexOf(':'))
									+"—"+endTime.substring(endTime.indexOf(' ')+1,endTime.lastIndexOf(':'))+"【非机考】";
					if(map_examSubjectInfo.size()>0&&map_examSubjectInfo.containsKey(time1)){
						List<ExamSubject> lastResult = map_examSubjectInfo.get(time1);
						lastResult.add(examSubject);
						map_examSubjectInfo.put(time1, lastResult);
					}else{
						List<ExamSubject> first = new ArrayList<ExamSubject>(0);
						first.add(examSubject);
						map_examSubjectInfo.put(time1,first);
					}
				}
			}
		}
		
		
		Set<String> set = map_examSubjectInfo.keySet();
		Integer showOrder = 0;
		for (String str : set) {
			List<ExamSubject> tmp = map_examSubjectInfo.get(str);
			if(tmp.size()>0){
				//每个考试科目的人数总计
	 			Integer total = 0; 
	 			//每条考试记录自增 
	 			showOrder++;
	 			//每一行记录存放为一个Map类型
				Map<String,String> map_local = new HashMap<String, String>(0);
				map_local.put("showOrder", showOrder.toString());
				map_local.put("actNum", "");
				map_local.put("memo", "");
 
				map_local.put("examTime",str );
				List<String[]> has_CheckedList = new ArrayList<String[]>(0);
				String[] strs = {"","",""};
				has_CheckedList.add(strs);
				//开始统计
				for (ExamSubject examSubject : tmp) {
					//课程名称
		 			String courseName = examSubject.getCourseName();
					//层次
		 			String classic = examSubject.getRecruitMajor().getClassic().getShortName();
					//招生批次
		 			String recruitPlanId = examSubject.getRecruitMajor().getRecruitPlan().getResourceid();
		 			boolean isContains = false; 
		 			for (String[] strings : has_CheckedList) {
						if(strings[0].equals(recruitPlanId)&&strings[1].equals(classic)&&strings[2].equals(courseName)){
							isContains =true;
						}
					}
		 			if(isContains){
		 				continue;
		 			}else{
		 				String[] str_import = {recruitPlanId,classic,courseName};
		 				has_CheckedList.add(str_import);
		 			}
		 			
		 			
					//查询条件为 符合以上所得单条考试科目的批次且为该招生批次下的学生。在ExamRoomplan有记录说明已经预约了考试
					//不对其科目进行限制，是因为默认了考生要考所在批次、层次下所有应该考的科目，数据库中未发现关于考生与科目的直接关联的信息
					List<ExamRoomPlan> list_examRoomPlan =  examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" where enrolleeinfo.recruitMajor.classic.shortName=?  and enrolleeinfo.recruitMajor.recruitPlan.resourceid=? and recruitExamPlan.resourceid is null and isDeleted=0 " , new Object[]{classic,recruitPlanId});
					if (list_examRoomPlan.size()>0) {
						for (ExamRoomPlan examRoomPlan : list_examRoomPlan) {
							total++;
							OrgUnit tmp_unitOrgUnit = examRoomPlan.getExamRoom().getBranchSchool();
							String unitCode = tmp_unitOrgUnit.getUnitCode();
							String unitName = tmp_unitOrgUnit.getUnitShortName();
							if(!map_local.containsKey(unitCode)){
								map_local.put(unitCode, "1");
							}else{
								map_local.put(unitCode, Integer.toString(Integer.parseInt(map_local.get(unitCode))+1));
							}
							//关于标题的记录
							if(!map_title.containsKey(unitCode)){
								map_title.put(unitCode, unitName);
							}
							if (!map.containsKey(unitCode)) {
								map.put(unitCode, "1");
							}else {
								map.put(unitCode, Integer.toString(Integer.parseInt(map.get(unitCode))+1));
							}
						}
						
					}
						
				}
				map_local.put("total", total.toString());
				if (map_local.size()>0) {
					list_dataRes.add(map_local);
				}			
			}
		}
		//第二部分 机考
		List<RecruitExamPlanDetails> examPlanDetails = new ArrayList<RecruitExamPlanDetails>(0);
		//找到具体对应的考试科目 不考虑人数问题
		if(recruitPlans.size()>0){
			for (RecruitPlan recruitPlan : recruitPlans) {
				//遍历符合年度学期条件下各个招生批次的详细入学测试情况
				String recruitPlanId = recruitPlan.getResourceid();
				List<RecruitExamPlanDetails> tmp_details = recruitExamPlanDetailsService.findByHql(" from "+RecruitExamPlanDetails.class.getSimpleName()+" where recruitExamPlan.recruitPlan.resourceid=? and isDeleted=0", recruitPlanId);
				//同样的 因为招生批次不作为统计的条件 所以要把不同招生批次下,相同的考试时间 考试课程 考试场次的考试科目过滤出来
				if (tmp_details.size()>0) {
					for (RecruitExamPlanDetails recruitExamPlanDetails : tmp_details) {
						//没有直接添加的原因是为了去除重复
						if(examPlanDetails.size()>0){
							boolean isAdd = true;
							for (RecruitExamPlanDetails recruitExamPlanDetails2 : examPlanDetails) {
								String starttime1 = recruitExamPlanDetails.getStartTime().toString();
								String starttime2 = recruitExamPlanDetails2.getStartTime().toString();
								String endtime1 = recruitExamPlanDetails.getStartTime().toString();
								String endtime2 = recruitExamPlanDetails2.getStartTime().toString();
								String explan1 = recruitExamPlanDetails.getRecruitExamPlan().getResourceid();
								String explan2 = recruitExamPlanDetails2.getRecruitExamPlan().getResourceid();
								String course1 = recruitExamPlanDetails.getCourseName();
								String course2 = recruitExamPlanDetails2.getCourseName();
								if (starttime1.equals(starttime2)&& endtime1.equals(endtime2)&& course1.equals(course2)&&explan1.equals(explan2)){
									isAdd = false;
								}
							}
							if (isAdd) {
								examPlanDetails.add(recruitExamPlanDetails);
							}
						}else{
							examPlanDetails.add(recruitExamPlanDetails);								
						}
					}
				}
					
			}
				
		}
		//RecruitExamPlanDetails的List按照考试时间分类存储
		Map<String,List<RecruitExamPlanDetails>> map_machinalExam = new HashMap<String, List<RecruitExamPlanDetails>>(0);
		for (RecruitExamPlanDetails recruitExamPlanDetails : examPlanDetails) {
			//获得考场批次
	 		String examPlan = recruitExamPlanDetails.getRecruitExamPlan().getResourceid();
			//获得层次信息
	 		//因为关联机考科目与学生信息的只有ExamRoomPlan中的enrolleeifoid 与 examplanid 两个字段
	 		//examplan与explandetails是一对多的关系 一个details应该对应一个examplan 所以通过examplan 找到符合要求的考生看他们的层次是否一致
	 		//这有两种情况导致“无法确定层次信息”一个是该场次没有学生 从而确定不了该时间段下考试科目的层次
	 		//另一个是该场次下的学生有多个层次
	 		List<ExamRoomPlan> tmp_for_classic = examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" where recruitExamPlan.resourceid=? and isDeleted=0 ", examPlan);
			String classic = "无人参考无层次";
			List<String> classics = new ArrayList<String>(0);
			if(tmp_for_classic.size()>0){
				for (ExamRoomPlan examRoomPlan : tmp_for_classic) {
					String tmp_class = examRoomPlan.getEnrolleeinfo().getRecruitMajor().getClassic().getShortName(); 
					classics.add(tmp_class);
				}
			}
			boolean isConfirm = true;
			int i=0;
			for (String string : classics) {
				if(i==0){
					classic=string;
				}
				if(!classic.equals(string)){
					isConfirm = false;
					classic="无法确定层次";
					break;
				}
			}
			if(isConfirm&&classics.size()>0){
				classic=classics.get(0);
			}
			String start1   = recruitExamPlanDetails.getStartTime().toString();
			String end1     = recruitExamPlanDetails.getEndTime().toString();
			String period = "";
			if(Integer.valueOf(start1.substring(start1.indexOf(' ')+1, start1.indexOf(':')))>=12){
				period = "下午";
			}else{
				period = "上午";
			}
			start1  = start1.substring(0,start1.indexOf(' '))+" "+period+" "+start1.substring(start1.indexOf(' '),start1.lastIndexOf(':'));
			end1    = end1.substring(end1.indexOf(' ')+1,end1.lastIndexOf(':'));
			String key     = start1+"#"+end1;//+"#"+course1+"#"+classic;
			if(map_machinalExam.size()>0&&map_machinalExam.containsKey(key)){
				List<RecruitExamPlanDetails> value = map_machinalExam.get(key);
				value.add(recruitExamPlanDetails);
				map_machinalExam.put(key,value);
			}else{
				List<RecruitExamPlanDetails> value = new ArrayList<RecruitExamPlanDetails>(0);
				value.add(recruitExamPlanDetails);
				map_machinalExam.put(key,value);
			}
		}
		if(map_machinalExam.size()>0){
			Set<String> set_machinalExam = map_machinalExam.keySet();
			for (String str : set_machinalExam) {
		 		//每条考试科目记录下的总计
		 		Integer total = 0; 
		 		showOrder++;
		 		//每一行记录存放为一个Map类型
		 		Map<String, String> map_local = new HashMap<String, String>();
		 		//解析Map的键
		 		String[] tmp_str  = str.split("#");
				//获得时间
		 		String examTime   = tmp_str[0]+"—"+tmp_str[1]+"【机考】";
				map_local.put("showOrder", showOrder.toString());
				map_local.put("actNum", "");
				map_local.put("memo", "");
				map_local.put("examTime", examTime);
				List<RecruitExamPlanDetails> list_reRecruitExamPlanDetails = map_machinalExam.get(str);
				for (RecruitExamPlanDetails recruitExamPlanDetails : list_reRecruitExamPlanDetails) {
					//考试场次
					String examPlan = recruitExamPlanDetails.getRecruitExamPlan().getResourceid();
					List<ExamRoomPlan> list_examRoomPlan =  examRoomPlanService.findByHql(" from "+ExamRoomPlan.class.getSimpleName()+" where recruitExamPlan.resourceid=? and recruitExamPlan.resourceid is not null and isDeleted=0 ",examPlan);
					//验证
					if (list_examRoomPlan.size()>0) {
						for (ExamRoomPlan examRoomPlan : list_examRoomPlan) {
							total++;
							OrgUnit tmp_unitOrgUnit = examRoomPlan.getExamRoom().getBranchSchool();
							String unitCode = tmp_unitOrgUnit.getUnitCode();
							String unitName = tmp_unitOrgUnit.getUnitShortName();
							if(!map_local.containsKey(unitCode)){
								map_local.put(unitCode, "1");
							}else{
								map_local.put(unitCode, Integer.toString(Integer.parseInt(map_local.get(unitCode))+1));
							}
							if(!map_title.containsKey(unitCode)){
								map_title.put(unitCode, unitName);
							}
							if (!map.containsKey(unitCode)) {
								map.put(unitCode, "1");
							}else {
								map.put(unitCode, Integer.toString(Integer.parseInt(map.get(unitCode))+1));
							}
						}
						map_local.put("total", total.toString());
					}	
				}
				if (map_local.size()>0) {
					list_dataRes.add(map_local);
				}
			}
		}
		
		
		//最后统计最终总数
		Set<String> setTotal = map.keySet();
		Integer totalInAll = 0;
		for (String str : setTotal) {
			if(!str.equals("showOrder")){
				totalInAll += Integer.parseInt(map.get(str));
			}
		}
		map.put("total", totalInAll.toString());
		list_dataRes.add(map);	
		//导出Excel
		Map<String,String> titleAhead = new HashMap<String, String>();
		Map<String,String> titleBack  = new HashMap<String, String>();
		titleAhead.put("showOrder", "序号");
		titleAhead.put("examTime", "考试时间");
		titleBack.put("total", "总报考人数");
		titleBack.put("actNum", "实到人数");
		titleBack.put("memo", "备注");
		doExport(yearInfo, term, "test1","入学测试人数统计表(按时间段)" ,map_title, titleAhead, titleBack, list_dataRes, response, 4);
		
		
		
	}
	
	*//**
	 * 自定义配置对象
	 * @param dynamicTitleMap 自定义表头
	 * @return
	 *//*
	private ExcelConfigInfo getExcelConfigInfo(Map<String, String> dynamicTitleMap){
		ExcelConfigInfo result = new ExcelConfigInfo();
		
		Map<String, ExcelConfigPropertyParam> propertyMap = new HashMap<String, ExcelConfigPropertyParam>();
        Map<String, ExcelConfigPropertyParam> columnMap = new LinkedHashMap<String, ExcelConfigPropertyParam>();
        
        int index = 1;
        
        for (String unitCode : dynamicTitleMap.keySet()) {
        	ExcelConfigPropertyParam modelProperty = new ExcelConfigPropertyParam();
            
        	modelProperty.setName(unitCode);
            modelProperty.setColumn(Integer.toString(index++));
            modelProperty.setExcelTitleName(dynamicTitleMap.get(unitCode));
            modelProperty.setDefaultValue("0");
            modelProperty.setIsNull(Constants.BOOLEAN_YES);
            if(unitCode.equals("courseName")||unitCode.equals("examTime")){
            	modelProperty.setColumnWidth("30");
            }else{
            	modelProperty.setColumnWidth("15");
            }
            modelProperty.setMaxLength("500"); 
            modelProperty.setDataType("String");
            
            String excelTitle = ValidateColumn.configValidate(propertyMap,modelProperty.getExcelTitleName());
            propertyMap.put(excelTitle, modelProperty);
            
            columnMap.put(modelProperty.getColumn(), modelProperty);
		}   
        //标示
        Map<String, String> flagMap = new HashMap<String, String>();
        flagMap.put(ConfigConstant.PROPERTY_NAME, "flag");
        result.setFlagMap(flagMap);
        
        //提示消息
        Map<String, String> messageMap = new HashMap<String, String>();
        messageMap.put(ConfigConstant.PROPERTY_NAME, "message");
        messageMap.put(ConfigConstant.PROPERTY_EXCEL_TITLE_NAME, "操作结果");
        result.setMessageMap(messageMap);
        result.setPropertyMap(propertyMap);
        result.setColumnMap(columnMap);	
        
		return result;
	}
	
	*//**
	 * 导出入学测试人数统计
	 * @param yearInfo
	 * @param term
	 * @param templateName
	 * @param map_title
	 * @param titleAhead
	 * @param titleBack
	 * @param list_dataRes
	 * @param response
	 * @param beginRow
	 *//*
	public void doExport(String yearInfo,String term,String templateName,String fileTitle,Map<String,String> map_title,Map<String,String> titleAhead,Map<String,String> titleBack,List list_dataRes,HttpServletResponse response,int beginRow) 
	{
		LinkedHashMap<String, String> dynamicTitleMap = new LinkedHashMap<String, String>(0);//自定义表头
		Set<String> key_titles= titleAhead.keySet();
		for(String str : key_titles){
			dynamicTitleMap.put(str,titleAhead.get(str));
		}
	    List<String> keys = new ArrayList<String>(map_title.keySet());
	    Collections.sort(keys);
	    for (String key : keys) {
	        dynamicTitleMap.put(key, map_title.get(key));
		}
		key_titles= titleBack.keySet();
	    for(String str : key_titles){
			dynamicTitleMap.put(str,titleBack.get(str));
		}
		String yearInfoStr = "";
		if(ExStringUtils.isNotBlank(yearInfo)){
			YearInfo year = yearInfoService.get(yearInfo);
			yearInfoStr += year.getYearName();
		}
		if(ExStringUtils.isNotBlank(term)){			
			yearInfoStr += JstlCustomFunction.dictionaryCode2Value("CodeTerm", term);
		}
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			GUIDUtils.init();
			File excelFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			templateMap.put("yearInfo", yearInfoStr);
			templateMap.put("exportDate", ExDateUtils.DATE_FORMAT_CN.format(new Date()));
			//模板文件路径
			String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+templateName+".xls";
			//初始化配置参数
			exportExcelService.initParmasByExcelConfigInfo(excelFile, getExcelConfigInfo(dynamicTitleMap), list_dataRes, null);
			exportExcelService.getModelToExcel().setDynamicTitle(true);//动态表头
			exportExcelService.getModelToExcel().setDynamicTitleMap(dynamicTitleMap);
			exportExcelService.getModelToExcel().setSheet(1, "入学测试预约、实考人数统计表");
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, beginRow, templateMap);			
			exportExcelService.getModelToExcel().setHeader(yearInfoStr+fileTitle);//设置标题
				
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, yearInfoStr+fileTitle+".xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			e.printStackTrace();
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}		
		
	}
	*//**
	 * 录取人数统计-条件选择
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/matriculate_statReport_condition.html")
	public String matriculateStatReportCondition(HttpServletRequest request,ModelMap model)throws Exception{
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
		return "/edu3/recruit/matriculate/export-matriculate-stat-condition";
	}
	*//**
	 * 录取人数统计-导出
	 * @param request
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/matriculate_statReport_export.html")
	public void matriculateStatReportExport(HttpServletRequest request ,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		JasperPrint jasperPrint= null;//输出的报表
	
		String ids             = "";
		String recruitPlan     = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));
		String brSchool        = ExStringUtils.trimToEmpty(request.getParameter("brSchool")); 

		String classic 		   = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String major 		   = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String isApplyNoexam   = ExStringUtils.trimToEmpty(request.getParameter("isApplyNoexam"));
		
		//如果是学习中心用户，只操作本学习中心的数据
		User user              = SpringSecurityHelper.getCurrentUser();
		
		if (ExStringUtils.isNotEmpty(isApplyNoexam))  map.put("isApplyNoexam", isApplyNoexam);
	    if (ExStringUtils.isNotEmpty(classic))        map.put("classic", classic);
	    if (ExStringUtils.isNotEmpty(major))          map.put("major", major);
	    
	    if (ExStringUtils.isNotEmpty(recruitPlan)){
	    	ids = "";
	    	for (String s:recruitPlan.split(",")) {
	    		ids   += ",'"+s+"'";
			}
	    	map.put("recruitPlan",ids.substring(1));
	    }
	    if (ExStringUtils.isNotEmpty(brSchool)){    
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
	  //  Connection conn             = null;
	    try {
	    	
	       // conn            	    = enrolleeInfoService.getConn();
	    	String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
	    							  File.separator+"recruitPrint"+File.separator+"enrolleeInfo_matriculate_Report_crosstab_export.jasper"),"utf-8");
			File reprot_file        = new File(reprotFile);
			GUIDUtils.init();			
			String filePath         = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
			//String filePath         = "D:\\aa.xls";
			//jasperPrint 		    = JasperFillManager.fillReport(reprot_file.getPath(), map, conn); // 填充报表
			jasperPrint 	= enrolleeInfoService.printReport(reprot_file.getPath(), map, enrolleeInfoService.getConn());
			JRXlsxExporter exporter = new JRXlsxExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
	        exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);  
	        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
	        exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);  
	        exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath); 
	        //exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.xls"); 
	        exporter.exportReport(); 

	        downloadFile(response,"录取统计汇总表.xlsx",filePath,true);
		} catch (Exception e) {
			logger.error("导出录取统计表出错：{}"+e.fillInStackTrace());
		}
	    
	}
	
	*//**
	 * 导出银校通样式录取名单
	 * @param request
	 * @param response
	 * @throws WebException
	 *//*
	@RequestMapping("/edu3/recruit/matriculate/matriculateListForFee_export.html")
	public void matriculateListExport(HttpServletRequest request ,HttpServletResponse response)throws WebException{

		// 查询条件
		Map<String, Object> condition = new HashMap<String, Object>();
		
		String branchSchool 		  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));// 学习中心
		String recruitPlan 			  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlan"));// 招生计划
		String major 				  = ExStringUtils.trimToEmpty(request.getParameter("major"));// 专业
		String classic 				  = ExStringUtils.trimToEmpty(request.getParameter("classic"));	// 层次	
		String registerDate 		  = ExStringUtils.trimToEmpty(request.getParameter("registerDate"));//报到时间
		String printDate              = ExStringUtils.trimToEmpty(request.getParameter("printDate"));	//打印时间
		String name 				  = ExStringUtils.trimToEmpty(request.getParameter("name"));	// 姓名 
		String examCertificateNo 	  = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));	//准考证号
		String isApplyNoexam          = ExStringUtils.trimToEmpty(request.getParameter("isApplyNoexam"));	//是否免试            
		String grade                  = ExStringUtils.trimToEmpty(request.getParameter("grade"));	//年级    
		String learningStyle 		  = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));//学习形式
		
		
		// 如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if (ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
		}
		if (StringUtils.isNotEmpty(branchSchool)) 		 condition.put("branchSchool", branchSchool);
		if (ExStringUtils.isNotEmpty(major))  			 condition.put("major", major);	
		if (ExStringUtils.isNotEmpty(classic)) 			 condition.put("classic", classic);
		if (ExStringUtils.isNotEmpty(registerDate)) 	 condition.put("registerDate", registerDate);		
		if (ExStringUtils.isNotEmpty(name)) 			 condition.put("name", name);		
		if (ExStringUtils.isNotEmpty(examCertificateNo)) condition.put("examCertificateNo", examCertificateNo);
		if (ExStringUtils.isNotEmpty(printDate)) 		 condition.put("printDate", printDate);
		if (ExStringUtils.isNotEmpty(isApplyNoexam)) 	 condition.put("isApplyNoexam", isApplyNoexam);	
		if (ExStringUtils.isNotEmpty(grade)) 			 condition.put("grade", grade);
		if (StringUtils.isNotEmpty(learningStyle))		 condition.put("learningStyle",learningStyle);
		if (ExStringUtils.isNotEmpty(recruitPlan)){
			RecruitPlan plan = recruitPlanService.get(recruitPlan);
			if(null!=plan){
				condition.put("recruitPlan", recruitPlan);		
			}else {
				condition.put("yearInfoId", recruitPlan);		
			}
		}
			
		try{
			String sql = genSql(condition);
			List<Map<String,Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, condition);

			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");

			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			//模板文件路径
			String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"matriculateListForFee.xls";
			
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "matriculateListForFee", list,null);
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, templateMap);
			exportExcelService.getModelToExcel().setRowHeight(500);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件

			downloadFile(response, "银校通样式录取名册.xls", excelFile.getAbsolutePath(),true);
			
		} catch (Exception e) {
			logger.error("导出银校通样式录取名单：{}"+e.fillInStackTrace());
		}
	    
	}
	//生成SQL
	private String genSql (Map<String, Object> condition){
		
		StringBuffer sql = new StringBuffer();
		sql.append("select ei.matriculatenoticeno,stu.name,f_dictionary('CodeSex',stu.gender) sex,stu.certnum,substr(g.gradename,0,4)  grade, ");
		sql.append("  substr(ei.matriculatenoticeno,0,4)||u.unitshortname||c.shortname||substr(m.majorname,0,2) className,");
		sql.append("  m.majorname,u.unitname collegeName,c.classicname,");
		sql.append("  ( CASE ei.teachingtype  WHEN 'face' THEN '网络成人直属班' WHEN 'net'  THEN f_dictionary('CodeLearningStyle',ei.stutymode) ELSE f_dictionary('CodeTeachingType',ei.teachingtype) END )  ");
		sql.append("  stutyType,'' studentType ,'' unitname,'' hkandmacaostation ,'' staffchildren ,'' eduYear ,'' mobile  , ");
		sql.append("  '' province,'' nation,substr(g.gradename,5,1) gradeFlag ,'' results ,'' account ,'' examcertificateno ");
		
		sql.append("  from  edu_recruit_enrolleeinfo ei  ");
		
		//----------------------------------关联表----------------------------------
		sql.append("  inner join edu_base_student stu on ei.studentbaseinfoid = stu.resourceid ");
		if(condition.containsKey("name")){
			sql.append(" and stu.name = :name");
		}
		sql.append("  inner join edu_recruit_major rm on ei.recruitmajorid = rm.resourceid ");
		if(condition.containsKey("recruitPlan")){
			sql.append(" and rm.recruitplanid = :recruitPlan");
		}
		sql.append("  inner join edu_base_classic c on rm.classic = c.resourceid ");
		if(condition.containsKey("classic")){
			sql.append(" and  c.resourceid = :classic");
		}
		sql.append("  inner join edu_base_major m on rm.majorid = m.resourceid ");
		if(condition.containsKey("major")){
			sql.append(" and  m.resourceid = :major");
		}
		sql.append("  inner join hnjk_sys_unit u on ei.branchschoolid = u.resourceid ");
		if(condition.containsKey("branchSchool")){
			sql.append(" and u.resourceid = :branchSchool");
		}
		sql.append("  inner join edu_base_grade g on ei.grade = g.resourceid ");
		if(condition.containsKey("grade")){
			sql.append(" and  g.resourceid = :grade");
		}
		if (condition.containsKey("yearInfoId")) {
			sql.append(" and  g.yearid = :yearInfoId");
		}
		//----------------------------------关联表----------------------------------
		
		sql.append(" where ei.isDeleted=0 and ei.ismatriculate = 'Y' and ei.signupflag = 'Y'");
		if(condition.containsKey("examCertificateNo")){
			sql.append(" and ei.examcertificateno = :examCertificateNo");
		}
		if(condition.containsKey("isApplyNoexam")){
			sql.append(" and ei.isapplynoexam = :isApplyNoexam");
		}
		if(condition.containsKey("learningStyle")){
			sql.append(" and ei.stutymode = :learningStyle");
		}

		sql.append(" order by ei.examcertificateno ");
		
		
		return sql.toString();
	}*/
}

