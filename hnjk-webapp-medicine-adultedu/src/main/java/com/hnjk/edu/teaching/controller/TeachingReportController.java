package com.hnjk.edu.teaching.controller;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.configuration.xml.JaxbUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.ILearningJDBCService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.recruit.util.ReplaceStr;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.GraduateData;
import com.hnjk.edu.roll.model.StudentGraduateAndDegreeAudit;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IGraduateDataService;
import com.hnjk.edu.roll.service.IStudentGraduateAndDegreeAuditService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.jaxb.entity.AchievementList;
import com.hnjk.edu.teaching.model.*;
import com.hnjk.edu.teaching.service.*;
import com.hnjk.edu.teaching.service.impl.ExamPrintServiceImpl;
import com.hnjk.edu.teaching.service.impl.ExamResultExportServiceImpl.GenFile;
import com.hnjk.edu.teaching.util.ExamResultImportFileOperateUtil;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.edu.teaching.vo.StudentExamResultsVo;
import com.hnjk.edu.teaching.vo.UsualResultsVo;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.Dictionary;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.system.service.ISysConfigurationService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.write.*;
import lombok.Cleanup;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;
import java.lang.Boolean;


/**
 * 教学管理模块Excel上传\下载Controller
 * @author luof
 *
 */
@Controller
public class TeachingReportController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 5753067050573793107L;
	
	@Autowired
	@Qualifier("stateExamResultsService")
	private IStateExamResultsService stateExamResultsService;//注入统考成绩服务

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;//excel导入服务

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;//考试批次服务
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;//成绩服务
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;//考试(课程安排)信息表服务
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;//注入附件服务
	
	@Autowired
	@Qualifier("examResultsLogService")
	private IExamResultsLogService examResultsLogService;//注入导入导出日志服务
	
	@Autowired
	@Qualifier("examResultsExportService")
	private IExamResultsExportService examResultsExportService;//注入成绩导入导出服务

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;//注入学籍服务
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;//注入学习计划服务
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService ;//注入基础课程服务
	
	@Autowired
	@Qualifier("bachelorExamResultsService")
	private IBachelorExamResultsService bachelorExamResultsService;//注入学位英语成绩服务
	
	@Autowired
	@Qualifier("studentGraduateAndDegreeAuditService")
	private IStudentGraduateAndDegreeAuditService studentGraduateAndDegreeAuditService;
	
	@Autowired
	@Qualifier("studentExamResultsService")
	private IStudentExamResultsService studentExamResultsService;
	
	@Autowired
	@Qualifier("examResultsAuditService")
	private IExamResultsAuditService examResultsAuditService;

	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;

	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("learningJDBCService")
	private ILearningJDBCService learningJDBCService;
	
	@Autowired
	@Qualifier("graduatedataservice")
	private IGraduateDataService graduateDataService;
	
	@Autowired
	@Qualifier("teachingplanservice")
	private ITeachingPlanService teachingplanservice;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("usualResultsService")
	private IUsualResultsService usualResultsService;
	
	@Autowired
	@Qualifier("usualResultsRuleService")
	private IUsualResultsRuleService usualResultsRuleService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;

	/**
	 * 下载\打印成绩单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/download.html")
	public void downLoadTranscripts(HttpServletRequest request , HttpServletResponse response){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String examSubId   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId  = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String flag        = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		Date curTime       = new Date();
		User curUser       = SpringSecurityHelper.getCurrentUser();
		String type        = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String unitId      = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		if (ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag", flag);
		}
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if (ExStringUtils.isNotEmpty(unitId)) {
			condition.put("unitId", unitId);
		}
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("unitId", curUser.getOrgUnit().getResourceid());
		}
		if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(examInfoId)) {
				
		 	    try{
		 	    	ExamSub examSub           = examSubService.get(examSubId);
			 	    ExamInfo examInfo         = examInfoService.get(examInfoId);
			 	    Date examinputStartTime   = examSub.getExaminputStartTime();
			 	    Date examinputEndTime     = examSub.getExaminputEndTime();
			 	    String examsubStatus      = ExStringUtils.trimToEmpty(examSub.getExamsubStatus());
			 	    
			 	    String inputCode          = CacheAppManager.getSysConfigurationByCode("examresults.input.rolecode").getParamValue();//期末成绩录入角色编码
			 	    boolean isTeacher         = curUser.isContainRole(inputCode);
			 	    //导出时判断是不是混机考
			 	    boolean isMixTrue          = null != examInfo.getIsmixture() && Constants.BOOLEAN_YES.equals(examInfo.getIsmixture());
			 	    if ("normal".equals(type)||curTime.getTime()>=examinputStartTime.getTime()&&
					    curTime.getTime()<=examinputEndTime.getTime()&&"3".equals(examsubStatus)){
						condition.put("isMixTrue", isMixTrue);
				 	    if("normal".equals(type)) {
							condition.put("flag2", "1");
						}
				 	    condition.put("needGradeAndClassic", "1");
						List<ExamResultsVo> list  = examResultsService.findExamResultVoList(condition);
				 	    
				 	    setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				 	    GUIDUtils.init();
				 	    //导出
						File excelFile = null;
						File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
						String examType= "";
						if(null==examInfo.getCourse().getExamType()){
							examType   = "未知考试形式";
						}else {
							examType   = examInfo.getCourse().getExamType().toString();
						}
						
						Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
						
						//模板文件路径
						String templateFilepathString = "";
						if (ExStringUtils.isNotEmpty(flag) && "1".equals(flag)) {
							templateMap.put("examSubName", examSub.getBatchName());
							templateMap.put("courseName",  "《"+ExStringUtils.trimToEmpty(examInfo.getExamCourseCode())+"---"+examInfo.getCourse().getCourseName()+"》");
							templateMap.put("examType", JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",examType));
							templateMap.put("examDate", ExDateUtils.formatDateStr(examInfo.getExamStartTime(),"yyyy-MM-dd"));
							templateMap.put("courseId", examInfo.getCourse().getResourceid());
							templateMap.put("examSubId",examSub.getResourceid());
							templateMap.put("schoolAndConnect",CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
									CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
							
							templateFilepathString = ("normal".equals(type)?"transcripts_US.xls":(isMixTrue?"transcripts_isMixTrue.xls":"transcripts_1.xls"));
							//初始化配置参数
							exportExcelService.initParmasByfile(disFile, ("normal".equals(type)?"transcripts_US":"transcripts_1"), list,condition);
							exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
							exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 8, templateMap);
							
						}else {
		
							templateMap.put("transcriptsTitle", examSub.getBatchName()+" /《"+examInfo.getExamCourseCode()+"---"+examInfo.getCourse().getCourseName()+"》");
							templateMap.put("examType", JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",examType));
							templateMap.put("examDate", ExDateUtils.formatDateStr(examInfo.getExamStartTime(),"yyyy-MM-dd"));
							templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
									CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
		
							templateFilepathString = "transcripts_2.xls";
							//初始化配置参数
							exportExcelService.initParmasByfile(disFile, "transcripts_2", list,condition);
							exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
							exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 5, templateMap);
						}
						
						
						
						excelFile = exportExcelService.getExcelFile();//获取导出的文件
						logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
						
						String downloadFileName=examInfo.getCourse().getCourseName()+"成绩单.xls";
						String downloadFilePath=excelFile.getAbsolutePath();
						
						ExamResultsLog log =  new ExamResultsLog();
						log.setOptionType(ExamResults.EXAMRESULTS_LOG_DOWNLOAD_TRANSCRIPTS);
						log.setExamSubId(examSubId);
						log.setFillinDate(new Date());
						log.setFillinMan(curUser.getUsername());
						log.setFillinManId(curUser.getResourceid());
						log.setAttachId(examInfo.getCourse().getCourseName());
						
						examResultsLogService.save(log);
						
						downloadFile(response, downloadFileName,downloadFilePath,true);	
			 	   }else{
			 		  String msg = "考试批次未关闭或者当前时间不在成绩录入时间范围之内！<br/>"
							     + "<strong>成绩录入时间:</strong><br/>"
							     + ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)
							     + " 至 "+ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME);
			 		  renderHtml(response, "<script>alert('"+msg+"')</script>");
			 	   }
				 }catch(Exception e){
					logger.error("导出excel文件出错："+e.fillInStackTrace());
			 		renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
				 }
		}else {
	 		renderHtml(response, "<script>alert('请输入合法的参数!')</script>");
		}
	}

	/**
	 * 导入未审核成绩单-打开窗口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/transcripts/upload-showpage.html")
	public String upLoadTranscriptsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String forwardURL            = "/edu3/teaching/examResult/examResultUpload-view1";
		String examSubId             = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId  			 = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String flag        			 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String type                  = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String isMixTrue			 = ExStringUtils.trimToEmpty(request.getParameter("isMixTrue"));
		if(!"normal".equals(type)){
			if ("newtWorkStudyCourse".equals(flag)) {
				forwardURL               = "/edu3/teaching/examResult/examResultUpload-view1";
			}else if ("faceStudyCourse".equals(flag)) {
				forwardURL               = "/edu3/teaching/examResult/examResultUpload-view2";
			}else if ("netsideStudyCourse".equals(flag)) {
				forwardURL               = "/edu3/teaching/examResult/examResultUpload-view3";
			}
			
			String msg                   = "";
			Date curDate                 = new Date();
			boolean success              = false;    
			
			//-----------------------------判断传入的考试批次是否合符导入成绩的要求-----------------------------
			if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(examInfoId)) {
				
				ExamSub exmaSub 		 = examSubService.get(examSubId);
				
				if (null==exmaSub) {//传入的考试批次不正确不能导入成绩
					
					msg = "没有找到对应的考试批次，不允许导入！";
					resultMap.put("success", success);
					resultMap.put("msg", msg);
					model.put("resultMap",resultMap);
					
					return forwardURL;
					
				}else if (Integer.parseInt(exmaSub.getExamsubStatus())< 3 || curDate.getTime()> exmaSub.getExaminputEndTime().getTime()) {//未关闭的考试批次不能导入成绩
					
					msg = "未关闭的考试批次或成绩录入时间已过，不允许导入！";
					resultMap.put("success", success);
					resultMap.put("msg", msg);
					model.put("resultMap",resultMap);
					
					return forwardURL;
				}
				
				String hql                       = " from "+Attachs.class.getSimpleName()+" attach where attach.formId=? and attach.formType=?  and attach.isDeleted=0 ";
				//当前批次关联的未删除的附件
				List<Attachs> attachList         = attachsService.findByHql(hql, examInfoId,ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				//当前批次上传附件时产生的日志记录
				List<ExamResultsLog> logList     = examResultsLogService.findExamResultsLogByExamSubIdAndOptionType(examInfoId, ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				
				if (null!=logList && !logList.isEmpty() && null!=attachList && !attachList.isEmpty()){
					model.addAttribute("logList",logList);
					model.addAttribute("attachList",attachList);
				} 
				
				resultMap.put("examInfoId",examInfoId);
				
				success                      = true;
				
			}else {//没有考试批次\课程不能导入
				msg =ExStringUtils.isEmpty(examSubId)?"请选择一个要导入成绩的考试批次！":"请选择一个要导入成绩的考试课程！";
				resultMap.put("success", success);
				resultMap.put("msg", msg);
				model.put("resultMap",resultMap);
				
				return forwardURL;
			}
			
			resultMap.put("examSubId",examSubId);
			model.put("resultMap",resultMap);
			if("Y".equals(isMixTrue)){
				model.addAttribute("isMixTrue", "true");
			}
			return forwardURL;
		}else{
			//如果是导入平时成绩
			model.addAttribute("type", type);
			if ("newtWorkStudyCourse".equals(flag)) {
				forwardURL               = "/edu3/teaching/examResult/examResultUpload-viewUsualScore";
			}
			
			String msg                   = "";
			Date curDate                 = new Date();
			boolean success              = false;    
			
			//-----------------------------判断传入的考试批次是否合符导入成绩的要求  目前只是要求有考试批次就可以了-----------------------------
			if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(examInfoId)) {
				
				ExamSub exmaSub 		 = examSubService.get(examSubId);
				
				if (null==exmaSub) {//传入的考试批次不正确不能导入成绩
					
					msg = "没有找到对应的考试批次，不允许导入！";
					resultMap.put("success", success);
					resultMap.put("msg", msg);
					model.put("resultMap",resultMap);
					
					return forwardURL;
					
				}
				
				String hql                       = " from "+Attachs.class.getSimpleName()+" attach where attach.formId=? and attach.formType=?  and attach.isDeleted=0 ";
				//当前批次关联的未删除的附件
				List<Attachs> attachList         = attachsService.findByHql(hql, examInfoId,ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				//当前批次上传附件时产生的日志记录
				List<ExamResultsLog> logList     = examResultsLogService.findExamResultsLogByExamSubIdAndOptionType(examInfoId, ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				
				if (null!=logList && !logList.isEmpty() && null!=attachList && !attachList.isEmpty()){
					model.addAttribute("logList",logList);
					model.addAttribute("attachList",attachList);
				} 
				
				resultMap.put("examInfoId",examInfoId);
				
				success                      = true;
				
			}else {//没有考试批次\课程不能导入
				msg =ExStringUtils.isEmpty(examSubId)?"请选择一个要导入成绩的考试批次！":"请选择一个要导入成绩的考试课程！";
				resultMap.put("success", success);
				resultMap.put("msg", msg);
				model.put("resultMap",resultMap);
				
				return forwardURL;
			}
			
			resultMap.put("examSubId",examSubId);
			model.put("resultMap",resultMap);
			
			return forwardURL;
		}

	}
	/**
	 * 检查上传的成绩文件是否合法
	 * @param excelFile
	 * @param examSub
	 * @return
	 */
	private Map<String,Object> checkAllowImport(Map<String,Object> result,File excelFile,ExamSub examSub){
		
		boolean isAllowImport = false;
		boolean hasCourse     = false;
		String excelExamSubId = "";
		String excelCourseId  = "";
		Set<ExamInfo> infoSet = examSub.getExamInfo();
		
		//隐藏在成绩模板文件中的考试批次ID及课程ID
		String excelTileInfo  = ExamResultImportFileOperateUtil.getInstance().getExamResultsFileTitleInfo(excelFile);
		
		if ( null != excelTileInfo && excelTileInfo.indexOf("$") != -1) {
			String [] infos   = excelTileInfo.split("\\$");
			excelExamSubId    = infos[0];
			excelCourseId     = infos[1];
		}
		if (ExStringUtils.isNotEmpty(excelCourseId)){
			Iterator<ExamInfo>  it= infoSet.iterator();
			while(it.hasNext()) {//遍历批次中的考试课程
				ExamInfo info     = it.next();
				if (info.getCourse().getResourceid().equals(excelCourseId)) {//当批次中有导入文件中的课程才允许导入
					hasCourse     = true;
				}
			}
		}
		
		if (ExStringUtils.isNotEmpty(excelExamSubId)
			&&excelExamSubId.trim().equals(examSub.getResourceid().trim()) 
			&&hasCourse ){
			isAllowImport     = true;
		}
		result.put("excelExamSubId", excelExamSubId);
		result.put("isAllowImport",isAllowImport);
		result.put("excelCourseId", excelCourseId);
		return result;
	}
	/**
	 * 导入未审核成绩单-核对成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/check-examresults.html")
	public void upLoadUncheckedTranscripts(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> resultMap      = new HashMap<String, Object>();
		List<ExamResultsVo> resultList    = new ArrayList<ExamResultsVo>();
		List<ExamResultsVo> noRecodeList  = new ArrayList<ExamResultsVo>();//没有考试预约记录的成绩
		List<ExamResultsVo> specialSubList= new ArrayList<ExamResultsVo>();//特殊批次的成绩
		boolean success                   = true;
 		boolean hasNoRecode               = false;
 		boolean isAllowImport             = false;
 		String msg                        = "";
 		String excelCourseId              = "";
 		String excelExamSubId             = "";
 		String isSpecialSub               = "";
		String uploadExamResultFileId     = ExStringUtils.trimToEmpty(request.getParameter("uploadExamResultFileId"));
		String examSubId                  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId                 = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String type 					  = ExStringUtils.trimToEmpty(request.getParameter("type"));
		try {
			ExamSub examSub               = examSubService.get(examSubId);
			ExamInfo info                 = examInfoService.get(examInfoId);
			isSpecialSub                  = examSub.getIsSpecial();//是否特殊批次
			String [] attachsIdArray      = uploadExamResultFileId.split(",");
			Course coures                 = info.getCourse();
			boolean isMixTrue       	  = false;
			Double mixTrueScorePer  	  = -1D;
			//是否混合机考课程
			if (null!=info&&null!=info.getIsmixture()) {
				isMixTrue           	  = null != info.getIsmixture() && Constants.BOOLEAN_YES.equals(info.getIsmixture());
			}
			if (isMixTrue) {
				if (null==info.getMixtrueScorePer()) {
					throw new ServiceException(info.getCourse().getCourseName()+"未设置笔考成绩比例！");
				}
				mixTrueScorePer     	  = info.getMixtrueScorePer();
			}
			//-----------------------当examSubId及uploadExamResultFileId都不为空时才进行核对成绩-----------------------
			if ( null!=examSub  && null != attachsIdArray) {
				
				//允许一次上传多个成绩文件
				for (int i = 0; i < attachsIdArray.length; i++) {
					
					Attachs attachs       = attachsService.get(attachsIdArray[i]);
					File excel            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
					resultMap             = checkAllowImport(resultMap,excel,examSub);
					isAllowImport         = (Boolean)resultMap.get("isAllowImport");
					excelCourseId         = resultMap.get("excelCourseId").toString();
					excelExamSubId        = resultMap.get("excelExamSubId").toString();
					resultMap.remove("isAllowImport");
					resultMap.remove("excelCourseId");
					if (!coures.getResourceid().equals(excelCourseId)) {
						throw new WebException("请上传"+coures.getCourseName()+"的成绩文件！");
					}
					
					if (isAllowImport) {
						
						if ("Y".equals(isSpecialSub)) {//特殊考试批次，如成人直属班，是不存在考试预约记录的，老师核对成绩后直接生成成绩记录
							
							importExcelService.initParmas(excel, "transcripts_specialsub",resultMap);
							importExcelService.getExcelToModel().setSheet(0);
							List modelList = importExcelService.getModelList();
							if (null!=modelList && !modelList.isEmpty()) {
								for (int j = 0; j < modelList.size(); j++) {
									ExamResultsVo vo = (ExamResultsVo)modelList.get(j);
									vo.setSort(String.valueOf(j+1));
									vo.setCourseId(excelCourseId);
									vo.setExamSubId(excelExamSubId);
									specialSubList.add(vo);
								}
							}
							
						}else {
							String modelName="transcripts_1";
							if(isMixTrue){
								modelName = "transcripts_isMixTrue";
							}
							if(ExStringUtils.isNotEmpty(type)&&"normal".equals(type)){
								modelName = "transcripts_US";
							}
							
							importExcelService.initParmas(excel, modelName,resultMap);
							importExcelService.getExcelToModel().setSheet(0);
							List modelList = importExcelService.getModelList();
							String branchSchoolId = "";
							User user = SpringSecurityHelper.getCurrentUser();
							if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
								branchSchoolId = user.getOrgUnit().getResourceid();
							}		
							if (null!=modelList && !modelList.isEmpty()) {
								
								for(int j = 0;j<modelList.size();j++){
							
									ExamResultsVo vo        = (ExamResultsVo)modelList.get(j);
									ExamResults examResults = examResultsService.findExamResulByExamSubAndStuNoAndCourseId(examSubId,vo.getStudyNo(),excelCourseId,null);
									if(ExStringUtils.isNotEmpty(branchSchoolId)&&!branchSchoolId.equals(examResults.getStudentInfo().getBranchSchool().getResourceid())){
										continue;
									}
									if (null == examResults) {//没有找到预约记录
										vo.setCourseId(excelCourseId);
										vo.setExamSubId(excelExamSubId);
										hasNoRecode = true;
										noRecodeList.add(vo);
									}else {
										if (("normal".equals(type)&&("-1".equals(examResults.getUsCheckStatus())|| "0".equals(examResults.getUsCheckStatus())||ExStringUtils.isEmpty(examResults.getUsCheckStatus()))&&("-1".equals(examResults.getCheckStatus())|| "0".equals(examResults.getCheckStatus())||ExStringUtils.isEmpty(examResults.getCheckStatus())) )||(!"normal".equals(type)&&("-1".equals(examResults.getCheckStatus())|| "0".equals(examResults.getCheckStatus())||ExStringUtils.isEmpty(examResults.getCheckStatus())))  ) {
											
											vo.setCourseId(examResults.getExamInfo().getCourse().getResourceid());
											
											if (ExStringUtils.isNotEmpty(examResults.getExamAbnormity())&&!"0".equals(examResults.getExamAbnormity())) {
												String examAbnormity = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examResults.getExamAbnormity());
												if (ExStringUtils.isNotEmpty(vo.getWrittenScore())&&!vo.getWrittenScore().trim().equals(examAbnormity)) {
													vo.setFlag(Constants.BOOLEAN_YES);
													//vo.setMessage(vo.getWrittenScore());
													vo.setMessage(examAbnormity);
												}
												//vo.setWrittenScore(examAbnormity);
											}else if (ExStringUtils.isNotEmpty(examResults.getWrittenScore())&&isMixTrue==false) {
												String writtenScore = examResults.getWrittenScore();
												if (ExStringUtils.isNotEmpty(vo.getWrittenScore())&&!vo.getWrittenScore().equals(writtenScore)) {
													vo.setFlag(Constants.BOOLEAN_YES);
													//vo.setMessage(vo.getWrittenScore());
													vo.setMessage(writtenScore);
												}
												//vo.setWrittenScore(examResults.getWrittenScore());
												
											}
											if (ExStringUtils.isNotEmpty(type)&&"normal".equals(type)&&ExStringUtils.isNotEmpty(examResults.getUsuallyScore())) {
												//当已为提交平时成绩时，会记录下来，但页面以不可编辑保存的形式出现，
												//当总成绩提交状态为提交的时候，直接就不生成核对的信息
												String usuallyScore = examResults.getUsuallyScore();
												if (ExStringUtils.isNotEmpty(vo.getUsuallyScore())&&!vo.getUsuallyScore().equals(usuallyScore)) {
													vo.setFlag(Constants.BOOLEAN_YES);
													//vo.setMessage(vo.getUsuallyScore());
													vo.setMessage(usuallyScore);
												}
												//vo.setUsuallyScore(examResults.getUsuallyScore());
												vo.setCheckStatus(null!=examResults.getUsCheckStatus()&&null!=examResults.getCheckStatus()&&(Integer.valueOf(examResults.getUsCheckStatus())>0||Integer.valueOf(examResults.getCheckStatus())>0)?"1":"0");
											}
											if(isMixTrue){
												if (null==info.getMixtrueScorePer()) {
													throw new ServiceException(info.getCourse().getCourseName()+"未设置笔考成绩比例！");
												}
												if(ExStringUtils.isNotEmpty(examResults.getWrittenHandworkScore())){
													String writtenScore = examResults.getWrittenHandworkScore();
													if (ExStringUtils.isNotEmpty(vo.getWrittenScore())&&!vo.getWrittenScore().equals(writtenScore) ) {
														vo.setFlag(Constants.BOOLEAN_YES);
														//vo.setMessage(vo.getWrittenScore());
														vo.setMessage(writtenScore);
													}
													//vo.setWrittenScore(writtenScore);
												}else{
													vo.setWrittenScore(vo.getWrittenScore());
												}
											}
											if(null==vo.getUsuallyScore()) {
												vo.setUsuallyScore("");
											}
											if(null==vo.getWrittenScore()) {
												vo.setWrittenScore("");
											}
											vo.setExamResultsResourceId(examResults.getResourceid());
											vo.setCourseName(examResults.getExamInfo().getCourse().getCourseName());
											vo.setCourseId(excelCourseId);
											vo.setExamSubId(excelExamSubId);
											vo.setMemo("");
											resultList.add(vo);	
										}else {
											vo.setCourseName(examResults.getExamInfo().getCourse().getCourseName());
											vo.setUsuallyScore(ExStringUtils.trimToEmpty(examResults.getUsuallyScore()));
											if(isMixTrue){
												vo.setWrittenScore(examResults.getWrittenHandworkScore());
											}else{
												vo.setWrittenScore(examResults.getWrittenScore());
											}
											if(ExStringUtils.isNotEmpty(examResults.getUsCheckStatus())&&Integer.valueOf(examResults.getUsCheckStatus())>0){
												vo.setMemo(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", examResults.getUsCheckStatus()));
											}else{
												vo.setMemo(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", examResults.getCheckStatus()));
											}
											
											resultList.add(vo);
											continue;
										}
									}	
								}
							}
							modelList           = null;//清理内存
						}
					}else {
						success                 = false;
						msg                     = attachs.getAttName()
												+ "中的成绩不合法,不是同一考试批次，或批次中没有该考试课程!";
						break;	
					}
				} 
				attachsIdArray 				    = null;//清理内存
				
			//-----------------------当examSubId及uploadExamResultFileId为空时不进行核对成绩-----------------------	
			}else{
				  success  						= false;
				    msg     					= "缺少考试批次或没有成绩单！";
			}
		} catch (Exception e) {
			 msg            					= "读取上传的Excel文件出错或参数异常："+ e.getMessage();
			 success       						= false;
			 
			logger.error("导入未审核成绩单-核对成绩出错:{}",e.fillInStackTrace());
		}
		if ("Y".equals(isSpecialSub)) {
			resultMap.put("isSpecialSub", isSpecialSub);
		}
		if (!specialSubList.isEmpty()) {
			resultMap.put("specialSubList", specialSubList);
		}
		if (!noRecodeList.isEmpty()) {
			resultMap.put("noRecodeList",noRecodeList);
		}
		
		//上传的文件中没有数据
		if (resultList.isEmpty()&&noRecodeList.isEmpty()&& specialSubList.isEmpty()) {
			success 					= false;
			if (ExStringUtils.isEmpty(msg)) {
				msg     					= "上传的文件中没有可核对的数据,或当前批次没有对应的成绩记录！";
			}
			
		}else {//成功读取上传文件中的数据
			resultMap.put("examResultsVoList",resultList);
		}
		
		resultMap.put("hasNoRecode",hasNoRecode);
		resultMap.put("success",success);
		resultMap.put("msg", msg);
		
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	
	/**
	 * 生成考试次数HQL
	 * @return
	 */
	private String genHQL(String flag,Map<String,Object> condition){
		StringBuffer hql = new StringBuffer();
		if ("examCount".equals(flag)) {
			hql.append(" select count(examResults.resourceid) from ExamResults examResults where examResults.isDeleted=0");
			hql.append(" and examResults.course.resourceid=? and examResults.studentInfo.resourceid=? ");
		}else if("studentInfo".equals(flag)) {
			
			hql.append("from "+StudentInfo.class.getSimpleName()+" stu where stu.isDeleted=:isDeleted");
		
			if(condition.containsKey("branchSchool")) {
				hql.append(" and stu.branchSchool.resourceid=:branchSchool");
			}
			if(condition.containsKey("gradeid")) {
				hql.append(" and stu.grade.resourceid=:gradeid");
			}
			if(condition.containsKey("major")) {
				hql.append(" and stu.major.resourceid=:major");
			}
			if(condition.containsKey("classId")) {
				hql.append(" and stu.classes.resourceid=:classId");
			}
			if(condition.containsKey("classic")) {
				hql.append(" and stu.classic.resourceid=:classic");
			}
			if(condition.containsKey("studyNo")) {
				hql.append(" and stu.studyNo=:studyNo");
			}
			if(condition.containsKey("name")) {
				hql.append(" and stu.studentName like '%"+condition.get("name")+"%'");
			}
			if(condition.containsKey("studentStatus")) {
				hql.append(" and stu.studentStatus=:studentStatus");
			}
			if(condition.containsKey("learningStyle")) {
				hql.append(" and stu.learningStyle=:learningStyle");
			}
			if (condition.containsKey("graduateDateStr")) {
				hql.append(" and exists( from "+GraduateData.class.getSimpleName()+" g where g.isDeleted=0 and g.studentInfo=stu and g.graduateDate =:graduateDate  )");
			}
			//添加关于毕业确认时间的查询 
			boolean hascGDb = condition.containsKey("confirmGraduateDateb");
			boolean hascGDe = condition.containsKey("confirmGraduateDatee");
			if(hascGDb||hascGDe){
				if (hascGDb&&hascGDe){
				hql.append(" and stu.resourceid in (select z.studentInfo.resourceid from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+ " z  where z.auditTime between to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-mm-dd') and to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-mm-dd')  and z.graduateAuditStatus='1' and z.graduateData.isDeleted='0' ) ");
				}
				if (hascGDb&&!hascGDe){
					hql.append(" and stu.resourceid in (select z.studentInfo.resourceid from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+ " z  where z.auditTime > to_date('"+condition.get("confirmGraduateDateb")+"','yyyy-mm-dd')   and z.graduateAuditStatus='1'  and z.graduateData.isDeleted='0' )  ");
					}
				if (!hascGDb&&hascGDe){
					hql.append(" and stu.resourceid in (select z.studentInfo.resourceid from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+ " z  where z.auditTime < to_date('"+condition.get("confirmGraduateDatee")+"','yyyy-mm-dd')  and z.graduateAuditStatus='1'   and z.graduateData.isDeleted='0' ) ");
					}
			}
			
			hql.append(" order by stu.branchSchool.unitCode,stu.classic.resourceid,stu.major.majorCode,stu.studyNo");
		}
		
		return hql.toString();
	}
	/**
	 * 导入未审核成绩单-保存成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/upload-process.html")
	public void upLoadUncheckedTranscriptsProcess(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		List errorList                = new ArrayList();//保存出错时存放出错成绩记录的LIST
		List saveList                 = new ArrayList();//保存成功的成绩记录LIST
		int updateRecordCount         = 0;              //成功保存的成绩个数
		boolean success  			  = true;
		String msg                    = "";
		User user                     = SpringSecurityHelper.getCurrentUser();
		String examSubId      		  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId             = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String [] examResultIds       = request.getParameterValues("importExamresoultsResourceid");//提交成绩的ID
		String examCountHQL     	  = genHQL("examCount",resultMap);                             //考试次数HQL
		
		try {
				ExamSub sub               	  = examSubService.get(examSubId);    			  //要导入成绩的考试批次
				ExamInfo info                 = examInfoService.get(examInfoId);              //要导入成绩的考试课程
				if (null!=sub&&null!=info) {
					
					Date curTime             = new Date();                                	 			    //当前时间
					BigDecimal divisor   	 = new BigDecimal("1");
					boolean isMixTrue        = false;                                                       //是否混合考
					Double mixTrueScorePer   = -1D;
					String examsubStatus     = sub.getExamsubStatus();                    				    //考试批次状态
					Date examinputEndTime    = sub.getExaminputEndTime();                 				    //考试批次成绩录入结束时间
					Date examinputStartTime  = sub.getExaminputStartTime();               				    //考试批次成绩录入开始时间
					Pattern pattern 		 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
					//是否混合机考课程
					if (null!=info&&null!=info.getIsmixture()) {
						isMixTrue           = null != info.getIsmixture() && Constants.BOOLEAN_YES.equals(info.getIsmixture());
					}
					if (isMixTrue) {
						if (null==info.getMixtrueScorePer()) {
							throw new ServiceException(info.getCourse().getCourseName()+"未设置笔考成绩比例！");
						}
						mixTrueScorePer     = info.getMixtrueScorePer();
					}
					
					//当前时间在成绩录入开始时间及成绩录入结束时间之间且考试批次为关闭，考试课程异常成绩已录完
					if (curTime.getTime()>=examinputStartTime.getTime()&&
						curTime.getTime()<=examinputEndTime.getTime()&&
						"3".equals(examsubStatus)) {
						
						for (int i = 0; i < examResultIds.length; i++){
							
						
								ExamResults examResults= examResultsService.get(examResultIds[i]);           //要保存的成绩
								int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());      
								String machineScore    = ExStringUtils.defaultIfEmpty(examResults.getWrittenMachineScore(), "0");
								if (checkStatus>0) {
									throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
								}
								List examCountList     = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(),
																					  examResults.getStudentInfo().getResourceid());
								long examCount         = (Long)examCountList.get(0) ;     					 								 //选考次数
								String writtenScore    = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+examResultIds[i]));   //卷面成绩
								String examAbnormity   = "";
								
								if("作弊".equals(writtenScore)){
									examAbnormity = "1";
								}								
								if("无卷".equals(writtenScore)){
									examAbnormity = "3";
								}
								if("缺考".equals(writtenScore)){
									examAbnormity = "2";
								}
								if("其它".equals(writtenScore)){
									examAbnormity = "4";
								}
								if (ExStringUtils.isNotEmpty(examAbnormity)&&"无卷".equals(examAbnormity)&&isMixTrue) {
									throw new WebException("混合考笔考部分成绩不允许录入无卷！");
								}
								if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
									throw new WebException("卷面成绩不能为空!");
								}
								if (ExStringUtils.isNotEmpty(examAbnormity)) {
									examResults.setExamAbnormity(examAbnormity);
									examResults.setWrittenScore("");
								}else {
									if (ExStringUtils.isEmpty(writtenScore)) {
										throw new WebException("卷面成绩不能为空!");
									}
									Matcher m = pattern.matcher(writtenScore);
									if (Boolean.FALSE == m.matches()) {
										throw new WebException(examResults.getStudentInfo().getStudentName()+"的卷面成绩应为0-100的数字</br>");
									}
									Double writtenScoreD  	= Double.parseDouble(writtenScore);
									//混合考成绩检查
									if (isMixTrue&&writtenScoreD>mixTrueScorePer) {
										throw new WebException(examResults.getStudentInfo().getStudentName()+"的笔考成绩必须为0-"+mixTrueScorePer.intValue()+"的数字</br>");
									}
									BigDecimal w_Score	     = new BigDecimal(writtenScore);
									
									if (isMixTrue) {
										BigDecimal wh_Score	 = new BigDecimal(machineScore);
										examResults.setWrittenHandworkScore(w_Score.divide(divisor,0,BigDecimal.ROUND_HALF_UP).toString());
										examResults.setWrittenScore((wh_Score.add(w_Score).divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
									}else {
										examResults.setWrittenScore((w_Score.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString());
									}
									
									examResults.setExamAbnormity("0");
								}
								examResults.setExamCount(examCount);
						 		examResults.setCheckStatus("0");
						 		examResults.setFillinMan(user.getCnName());
						 		examResults.setFillinManId(user.getResourceid());
						 		examResults.setFillinDate(curTime);
								
								examResultsService.update(examResults);
								
								
								/*List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.getStudentLearnPlanList(examResults.getStudentInfo().getResourceid(),examResults.getStudentInfo().getTeachingPlan().getResourceid());
								StudentLearnPlan learnPlan           = null;        
								if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
									for (int j = 0; j < stuLearnPlan.size(); j++) {
											learnPlan = stuLearnPlan.get(j);
										if ((null!=learnPlan.getTeachingPlanCourse()&&learnPlan.getTeachingPlanCourse().getCourse().getResourceid().equals(examResults.getCourse().getResourceid()))||//计划内课程成绩
											(null!=learnPlan.getPlanoutCourse()&&learnPlan.getPlanoutCourse().getResourceid().equals(examResults.getCourse().getResourceid()))) {
											learnPlan.setExamResults(examResults);
											learnPlan.setStatus(3);
											break;
										}
									}
									if (null==learnPlan) {
											learnPlan = new StudentLearnPlan();
											learnPlan.setExamInfo(examResults.getExamInfo());
											learnPlan.setExamResults(examResults);
											learnPlan.setStatus(3);
											learnPlan.setStudentInfo(examResults.getStudentInfo());
											learnPlan.setTerm(sub.getTerm());
											learnPlan.setYearInfo(sub.getYearInfo());	
											learnPlan.setOrderExamYear(sub.getYearInfo());
											learnPlan.setOrderExamTerm(sub.getTerm());
									}
								}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
											learnPlan = new StudentLearnPlan();
											learnPlan.setExamInfo(examResults.getExamInfo());
											learnPlan.setExamResults(examResults);
											learnPlan.setStatus(3);
											learnPlan.setStudentInfo(examResults.getStudentInfo());
											learnPlan.setTerm(sub.getTerm());
											learnPlan.setYearInfo(sub.getYearInfo());
											learnPlan.setOrderExamYear(sub.getYearInfo());
											learnPlan.setOrderExamTerm(sub.getTerm());
								}
								
								studentLearnPlanService.saveOrUpdate(learnPlan);*/
								
								
								updateRecordCount++;
								saveList.add(examResultIds[i]);
								
							
						}
					}else {
						success = false;
						msg		= "考试批次未关闭或者当前时间不在成绩录入时间范围之内！<br/>"
								+ "<strong>成绩录入时间:</strong><br/>"
								+ ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)
								+ " 至 "+ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME);
					}
				}else {
					success = false;
					msg 	= "找不到考试批次或考试课程,请与管理员联系！";
				}
		
		} catch (Exception e) {
			logger.error("导入学生成绩异常:{}",e.fillInStackTrace());
			success = false;
			msg 	= "导入学生成绩异常:"+e.getMessage();

		}

		resultMap.remove("examResultsVo");
		if (!errorList.isEmpty()) {
			resultMap.put("errorList",errorList);
		}
		if (!saveList.isEmpty()) {
			resultMap.put("saveList",saveList);
		}
		
		resultMap.put("updateRecordCount",updateRecordCount);
		resultMap.put("success",success);
		resultMap.put("msg", msg);

		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	/**
	 * 导入未审核成绩单-保存成绩（统考平时成绩）
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/upload-processUS.html")
	public void upLoadUncheckedTranscriptsProcessUS(HttpServletRequest request, HttpServletResponse response) {
		
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		List errorList                = new ArrayList();//保存出错时存放出错成绩记录的LIST
		List saveList                 = new ArrayList();//保存成功的成绩记录LIST
		int updateRecordCount         = 0;              //成功保存的成绩个数
		boolean success  			  = true;
		User user                     = SpringSecurityHelper.getCurrentUser();
		String msg                    = "";
		StringBuffer infomation 	  = new StringBuffer();
		String examSubId      		  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId             = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String [] examResultIds       = request.getParameterValues("importExamresoultsResourceid");//提交成绩的ID
		try {
				ExamSub sub               	  = examSubService.get(examSubId);    			  //要导入成绩的考试批次
				ExamInfo info                 = examInfoService.get(examInfoId);              //要导入成绩的考试课程
				if (null!=sub&&null!=info) {
					Date curTime             = new Date();                                	 			    //当前时间
					Date examinputEndTime    = sub.getExaminputEndTime();                 				    //考试批次成绩录入结束时间
					Date examinputStartTime  = sub.getExaminputStartTime();               				    //考试批次成绩录入开始时间
					Pattern pattern 		 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
					
					//当前时间在成绩录入开始时间及成绩录入结束时间之间且考试批次为关闭，考试课程异常成绩已录完
					if (curTime.getTime()>=examinputStartTime.getTime()&&
						curTime.getTime()<=examinputEndTime.getTime()) {
						
						for (int i = 0; i < examResultIds.length; i++){
								ExamResults examResults= examResultsService.get(examResultIds[i]);           //要保存的成绩
								int usCheckStatus 	   = Integer.valueOf(null==examResults.getUsCheckStatus()?"0":examResults.getUsCheckStatus());      
								if (usCheckStatus>0) {
									infomation.append(examResults.getStudentInfo().getStudyNo()+"_"+examResults.getStudentInfo().getStudentName()+"平时成绩已提交，不可保存</br>");
									continue;
								}
								
								String usuallyScore    = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore"+examResultIds[i]));   //卷面成绩
								
								if (ExStringUtils.isEmpty(usuallyScore)) {
									throw new WebException("成绩不能为空!");
								}
								Matcher m = pattern.matcher(usuallyScore);
								if (Boolean.FALSE == m.matches()) {
									infomation.append(examResults.getStudentInfo().getStudyNo()+"_"+examResults.getStudentInfo().getStudentName()+"的平时成绩应为0-100的数字</br>");
									continue;
								}
								examResults.setUsuallyScore(usuallyScore);
								examResults.setUsCheckStatus("0");
								examResults.setUsFillinDate(curTime);
								examResults.setUsFillinMan(user.getCnName());
								examResults.setUsFillinManId(user.getResourceid());
								
								examResultsService.update(examResults);
								updateRecordCount++;
								saveList.add(examResultIds[i]);
						}
					}else {
						success = false;
						msg		= "当前时间不在成绩录入时间范围之内！<br/>"
								+ "<strong>成绩录入时间:</strong><br/>"
								+ ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)
								+ " 至 "+ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME);
					}
				}else {
					success = false;
					msg 	= "找不到考试批次或考试课程,请与管理员联系！";
				}
		
		} catch (Exception e) {
			logger.error("导入学生成绩异常:{}",e.fillInStackTrace());
			success = false;
			msg 	= "导入学生成绩异常:"+e.getMessage();

		}
		resultMap.remove("examResultsVo");
		if (!errorList.isEmpty()) {
			resultMap.put("errorList",errorList);
		}
		if (!saveList.isEmpty()) {
			resultMap.put("saveList",saveList);
		}
		
		resultMap.put("updateRecordCount",updateRecordCount);
		resultMap.put("success",success);
		resultMap.put("msg", msg+infomation);

		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	/**
	 * 没有预约考试记录的成绩导入并生成预约考试记录
	 * @param request
	 * @param response
	 * @throws ParseException 
	 */
	@RequestMapping("/edu3/teaching/transcripts/upload-without-record-process.html")
	public void upLoadUncheckedTranscriptsWithOutRecordProcess(HttpServletRequest request, HttpServletResponse response)throws WebException, ParseException{
		
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		User user                     = SpringSecurityHelper.getCurrentUser();
		String examSubId      		  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String [] importResoult       = request.getParameterValues("directlyImportExamResoults");//学号+课程ID+批次ID
		String msg                    = "";											  	 //返回前台的消息
		StudentInfo studentInfo       = null;										 	 //学籍
		TeachingPlan plan             = null;										 	 //教学计划
		Course resultsCourse          = null;                                        	 //导入成绩的课程
		ExamInfo resultsExamInfo      = null;                                        	 //考试批次课程对象
		boolean success               = true;                                        	 //保存成绩是否成功
		boolean hasExamCourse         = false;										  	 //所选考试批次中是否有考试导入成绩的课程
		boolean hasPlanCourse         = false;										  	 //根据学号查询的学籍，所对应的教学计划是否包要导入成绩的课程(用于补录学习计划)
		List errorList                = new ArrayList();							  	 //保存出错时存放出错成绩记录的LIST
		List saveList                 = new ArrayList();							 	 //保存成功的成绩记录LIST
		ExamSub sub                   = examSubService.get(examSubId);   			 	 //要导入成绩的考试批次
		Set<ExamInfo> infoSet         = sub.getExamInfo();	 //考试批次课程Set
		Iterator<ExamInfo> infoIt     = infoSet.iterator();//考试批次课程Iterator
		String examCountHQL     	  = genHQL("examCount",resultMap);                            	 //考试次数HQL
		
		if (null!=importResoult  && null!=sub){//----------------------------所选考试批次不为空及提交的成绩不为空
			
			Date curTime             = new Date();                                	 			    //当前时间
			String examsubStatus     = sub.getExamsubStatus();                    				    //考试批次状态
			Date examinputEndTime    = sub.getExaminputEndTime();                 				    //考试批次成绩录入结束时间
			Date examinputStartTime  = sub.getExaminputStartTime();               				    //考试批次成绩录入开始时间
			Double writtenScorePer   = null==sub.getWrittenScorePer()?0:sub.getWrittenScorePer();   //要导入成绩的考试批次平时成绩比例
			Double usuallyScorePer   = 100-writtenScorePer;   										//要导入成绩的考试批次卷面成绩比例
			
			 //当前时间在成绩录入开始时间及成绩录入结束时间之间且考试批次为关闭
			if (curTime.getTime()>=examinputStartTime.getTime()&&curTime.getTime()<=examinputEndTime.getTime()&&"3".equals(examsubStatus)) {
				
				for (int i = 0; i < importResoult.length; i++) {
					
					String  [] importResoults  			 	 = importResoult[i].split("\\$");
					String studyNo             			 	 = importResoults[0];
					String excelCourseId      				 = importResoults[1];
					String excelExamSubId     			 	 = importResoults[2];
					try {
						
						studentInfo                          = studentInfoService.findUniqueByProperty("studyNo", importResoults[0]); //根据学号查学籍
						plan                                 = null==studentInfo?null:studentInfo.getTeachingPlan();				  //学生教学计划
						Set<TeachingPlanCourse> courseSet    = null==plan?null:plan.getTeachingPlanCourses();                		  //学生教学计划课程Set
						Iterator<TeachingPlanCourse> courseIt= null==courseSet?null:courseSet.iterator();                             //学生教学计划课程Iterator
						TeachingPlanCourse resultsPlanCourse = null;                                                                  //教学计划课程对象
						
						if (!excelExamSubId.equals(examSubId)) {
							success							 = false;
							msg     						 = "提交的成绩跟所选择的考试批次不是同一批次!";
							break;
						}
						if (null!=courseIt) {//---------------------------------检查学籍对应的教学计划是否包涵要保存成绩的课程
							while (courseIt.hasNext()) {
								TeachingPlanCourse planCourse= courseIt.next();
								if (planCourse.getCourse().getResourceid().equals(excelCourseId)) {
									resultsPlanCourse        = planCourse;
									hasPlanCourse            = true;
									break;
								}
							}
						}
						if (null!=infoIt) {//------------------------------------检查对应的考试批次是否包涵要保存成绩的课程
							while (infoIt.hasNext()) {
								ExamInfo subInfo  = infoIt.next();
								if (subInfo.getCourse().getResourceid().equals(excelCourseId)) {
									resultsExamInfo          = subInfo;
									resultsCourse            = subInfo.getCourse();
									hasExamCourse            = true;
									break;
								}
								
							}
						}
						if (hasPlanCourse && hasExamCourse){//-------------------当教学计划包涵要保存成绩的课程及考试批次包涵要保存成绩的课程时才生成成绩记录

								 ExamResults examResults              = new ExamResults();              //要保存的成绩
								 String usuallyScore                  = ExStringUtils.defaultIfEmpty(request.getParameter("usuallyScore"+studyNo+excelCourseId),"");    //平时成绩
								 String writtenScore                  = ExStringUtils.defaultIfEmpty(request.getParameter("writtenScore"+studyNo+excelCourseId),"");    //卷面成绩
								 String examAbnormity                 = ExStringUtils.defaultIfEmpty(request.getParameter("examAbnormity"+studyNo+excelCourseId),"");   //成绩异常
								 String integratedScore               = ExStringUtils.defaultIfEmpty(request.getParameter("integratedScore"+studyNo+excelCourseId),""); //综合成绩
								 long examCount                       = 0;	                            //选择次数
								 String courseId                      = resultsCourse.getResourceid();  //成绩对应的课程ID
								 String studentInfoId                 = studentInfo.getResourceid();    //学籍ID
								 List examCountList                   = examResultsService.findByHql(examCountHQL,courseId,studentInfoId);
								 examCount                            = (Long)examCountList.get(0);			
								 
								 examResults.setCourse(resultsCourse);
								 examResults.setMajorCourseId(resultsPlanCourse.getResourceid());
								 examResults.setExamCount(examCount+1);
								 examResults.setExamEndTime(resultsExamInfo.getExamEndTime());
								 examResults.setExamStartTime(resultsExamInfo.getExamStartTime());
								 examResults.setExamInfo(resultsExamInfo);
								 examResults.setExamsubId(examSubId);
								 examResults.setStudentInfo(studentInfo);
								 examResults.setIsDelayExam("N");
								 examResults.setCheckStatus("0"); 
								 examResults.setFillinMan(user.getUsername());
								 examResults.setFillinManId(user.getResourceid());
								 examResults.setFillinDate(curTime);
								 examResults.setIsMachineExam(resultsExamInfo.getIsMachineExam());
								 
								 examResults.setCourseScoreType(resultsExamInfo.getCourseScoreType());
								 examResults.setCreditHour(resultsPlanCourse.getCreditHour());
								 examResults.setStydyHour(resultsPlanCourse.getStydyHour()!=null?resultsPlanCourse.getStydyHour().intValue():null);
								 
							//-----------------------------------------------------1、当导入的成绩中综合成绩不为空，且大于零时则综合成绩为导入的综合成绩
							if (ExStringUtils.isNotEmpty(integratedScore) &&Double.parseDouble(integratedScore)>0){
								
								 examResults.setUsuallyScore(usuallyScore);
								 examResults.setWrittenScore(writtenScore);
								 examResults.setIntegratedScore(integratedScore);	
						    //-----------------------------------------------------2、当导入的成绩中综合成绩为空时，综合成绩等于考试批次时卷面成绩比例*卷面成绩+平时成绩比例*平时成绩
							}else {
								//-------------------------------------------------2.1 导入成绩单中成绩异常不为空时不设置卷面成绩、平时成绩、综合成绩						
								if (ExStringUtils.isNotEmpty(examAbnormity) && !"0".equals(examAbnormity)){
									
										examResults.setExamAbnormity(examAbnormity);
								//-------------------------------------------------2.2 导入成绩单中成绩异常为空时设置卷面成绩、平时成绩、综合成绩	
								}else {
									//---------------------------------------------2.2.1 考试批次中平时成绩比例、卷面成绩比例不为空
									if (null!=usuallyScorePer && null!=writtenScorePer && writtenScorePer > 0){
										
										Double writtenScoreD = Double.parseDouble(writtenScore) * (writtenScorePer/100);//根据批次比例算出的卷面成绩
										Double usuallyScoreD = Double.parseDouble(usuallyScore) * (usuallyScorePer/100);//根据批次比例算出的平时成绩
										examResults.setUsuallyScore(usuallyScore);
										examResults.setWrittenScore(writtenScore);
										//-----------------------------------------2.2.1.1 当综合成绩小于卷面成绩时，综合成绩等于卷面成绩
										if ((writtenScoreD+usuallyScoreD) < Double.parseDouble(writtenScore)) {
											examResults.setIntegratedScore(writtenScore);
										} else//-------------------------------------2.2.1.2 当综合成绩大于卷面成绩时,综合成绩计算方式为：卷面成绩*卷面成绩比例+平时成绩*平时成绩比例
										{
											examResults.setIntegratedScore(String.valueOf(writtenScoreD+usuallyScoreD));
										}
									//---------------------------------------------2.2.2 考试批次中平时成绩比例、卷面成绩比例为空综合成绩等于平时成绩	
									}else {
										
										examResults.setUsuallyScore(usuallyScore);
										examResults.setWrittenScore(writtenScore);
										examResults.setIntegratedScore(writtenScore);	
									}
								}
							}
							if(ExStringUtils.isEmpty(examAbnormity)) {
								examResults.setExamAbnormity("0");
							}
							examResultsService.save(examResults);
							
							//--------------------------------------------------补录的学习计划
							List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.getStudentLearnPlanList(studentInfo.getResourceid(),studentInfo.getTeachingPlan().getResourceid());
							StudentLearnPlan learnPlan           = null;
							if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()){//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
								for (int j = 0; j < stuLearnPlan.size(); j++) {
										learnPlan = stuLearnPlan.get(j);
									if (learnPlan.getTeachingPlanCourse().getCourse().getResourceid().equals(excelCourseId)) {
										learnPlan.setStatus(3);
										break;
									}
								}
								if (null==learnPlan) {
										learnPlan = new StudentLearnPlan();
										learnPlan.setExamInfo(resultsExamInfo);
										learnPlan.setExamResults(examResults);
										learnPlan.setStatus(3);
										learnPlan.setStudentInfo(studentInfo);
										learnPlan.setTeachingPlanCourse(resultsPlanCourse);
										learnPlan.setTerm(sub.getTerm());
										learnPlan.setYearInfo(sub.getYearInfo());	
								}
							}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
										learnPlan = new StudentLearnPlan();
										learnPlan.setExamInfo(resultsExamInfo);
										learnPlan.setExamResults(examResults);
										learnPlan.setStatus(3);
										learnPlan.setStudentInfo(studentInfo);
										learnPlan.setTeachingPlanCourse(resultsPlanCourse);
										learnPlan.setTerm(sub.getTerm());
										learnPlan.setYearInfo(sub.getYearInfo());
							}
							
							studentLearnPlanService.saveOrUpdate(learnPlan);
							saveList.add(studyNo+"$"+excelCourseId+"$"+excelExamSubId);
							
						}else {//---------------------------------------------------教学计划不包涵要保存成绩的课程或考试批次不包涵要保存成绩的课程	
							msg ="<strong><font color='red'>学号:"+studyNo+"</font></strong>对应的成绩，在所选考试批次中没有对应的考试课程或没有教学计划或教学计划不包涵对应的课程!<br/>";
							resultMap.put(studyNo,msg);	
							errorList.add(studyNo);
						}
					} catch (Exception e) {
						logger.error("导入学生成绩异常，学号：{}"+studyNo,e.fillInStackTrace());
						success = false;
						msg     = "导入学生成绩异常,学号："+studyNo+e.fillInStackTrace();
						break;
					}
				}
			}else {
						success = false;
						msg	 	= "考试批次未关闭或者已超过成绩录入时间范围！<br/>"
					    		+ "<strong>成绩录入时间:</strong><br/>"
					    		+ ExDateUtils.formatDateStr(examinputStartTime,ExDateUtils.PATTREN_DATE_TIME)
					    		+ " 至 "+ExDateUtils.formatDateStr(examinputEndTime,ExDateUtils.PATTREN_DATE_TIME);
			}
		}else {
			        	success = false;
			        	msg     = "没有找到所选的考试批次或未选择要保存的成绩！";
			        	resultMap.put("errorMsg",msg);
		}	
		resultMap.put("errorList",errorList);
		resultMap.put("saveList",saveList);
		resultMap.put("success",success);
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	/**
	 * 导入已审核的成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/upload-checked-transcripts-process.html")
	public void uploadCheckedTranscriptsProcess(HttpServletRequest request, HttpServletResponse response){
		
		String attid       = ExStringUtils.defaultIfEmpty(request.getParameter("uploadCheckedTranscriptsFileAttId"),"");
		String examSubId   = ExStringUtils.defaultIfEmpty(request.getParameter("examSubId"),"");
		Attachs attachs    = attachsService.get(attid);
		ExamSub examSub    = examSubService.get(examSubId);

		try {
			@Cleanup InputStream is = new FileInputStream(attachs.getSerPath() + File.separator + attachs.getSerName());
			JaxbUtil util = new JaxbUtil(AchievementList.class);
			AchievementList achievementList = (AchievementList) util.unmarshal(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 清空成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examresults/revocationImprot.html")
	public void revocationAllImprot(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		String examSubId   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));

		int count          = 0; 
		String msg         = "";
		boolean  success = false;
		if(ExStringUtils.isNotEmpty(examSubId)){
			paramMap.put("examSubId",examSubId);
			paramMap.put("revocationType",ExamResults.REVOCATION_TYPE_ALL);
			try {
				    count = examResultsService.revocationImprotResults(paramMap);
				    
			} catch (Exception e) {
				logger.error("清空所选批次的全部成绩异常：{}",e.fillInStackTrace());
				msg       = "清空所选批次的全部成绩异常,影响"+count+"条记录!"+e.fillInStackTrace();
				success   = false;
			}
		}
		if (count>0){
			success = true;
			msg     = "更新成功,影响"+count+"条记录!";  
		} 
		
		paramMap.put("success", success);
		paramMap.put("msg", msg);
		
		renderJson(response,JsonUtils.mapToJson(paramMap));
		
	}
	/**
	 * 按课程清空所选批次的成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/revocationImprot-course.html")
	public void revocationImprotByCourse(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> paramMap = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId  = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		int count        = 0; 
		String msg       = "";
		boolean  success = false;
		
		if(ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(courseId)){
			paramMap.put("examSubId",examSubId);
			paramMap.put("courseId", courseId);
			paramMap.put("revocationType",ExamResults.REVOCATION_TYPE_COURSE);
			try {
				    count = examResultsService.revocationImprotResults(paramMap);
				    
			} catch (Exception e) {
				msg       = "按课程清空所选批次的成绩异常,影响"+count+"条记录!"+e.fillInStackTrace();
				success   = false;
				logger.error("按课程清空所选批次的成绩异常：{}",e.fillInStackTrace());
			}
		}
		if (count>0){
			success = true;
			msg     = "更新成功,影响"+count+"条记录!";  
			
		}else {
			msg     = "该课程没有成绩记录!";
		} 
		paramMap.put("success", success);
		paramMap.put("msg", msg);
		
		renderJson(response,JsonUtils.mapToJson(paramMap));
	}
	/**
	 * 导出成绩-打开窗口
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresults/export-view.html")
	public String exportExamResultsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		Map<String,Object> resultMap = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String forwardURL            = "/edu3/teaching/examResult/export_examResult-view";
		String msg                   = "";
		boolean success              = false;    
		
		//-----------------------------判断传入的考试批次是否合符导入成绩的要求-----------------------------
		if (ExStringUtils.isNotEmpty(examSubId)) {
			
			ExamSub examSub = examSubService.get(examSubId);
			if (null==examSub) {//传入的考试批次不正确不能导出成绩
				
				msg = "没有找到对应的考试批次，不允许导出！";
				resultMap.put("success", success);
				resultMap.put("msg", msg);
				model.addAttribute("resultMap",resultMap);
				
				return forwardURL;
				
			}else if (Integer.parseInt(examSub.getExamsubStatus())< 3 ) {//未关闭的考试批次不能导出成绩
				
				msg = "未关闭的考试批次，不允许导出成绩！";
				resultMap.put("success", success);
				resultMap.put("msg", msg);
				model.addAttribute("resultMap",resultMap);
				
				return forwardURL;
			}
			
			String hql                       = " from "+Attachs.class.getSimpleName()+" attach where attach.isDeleted=0 and  attach.formId=? ";
										hql += " and ( attach.formType=? or attach.formType=? or attach.formType=?)";
			//当前批次关联的未删除的附件
			List<Attachs> attachList         = attachsService.findByHql(hql, examSubId,ExamResults.EXAMRESULTS_FORM_TYPE_EXPORT,
																		ExamResults.EXAMRESULTS_FORM_TYPE_REEXPORT,ExamResults.EXAMRESULTS_LOG_TYPE_PUBLISH);
			//当前批次上传附件时产生的日志记录
			List<ExamResultsLog> logList     = examResultsLogService.findExamResultsLogByExamSubId(examSubId);
			
			if (null!=logList && !logList.isEmpty() && null!=attachList && !attachList.isEmpty()){
				model.addAttribute("attId",attachList.get(0).getResourceid());
				model.addAttribute("logList",logList);
			} 
			success                      = true;
			model.addAttribute("examSub",examSub);
			
		}else {//没有考试批次不能导出成绩
			
			msg = "请选择一个要导出成绩的考试批次！";
			resultMap.put("success", success);
			resultMap.put("msg", msg);
			model.addAttribute("resultMap",resultMap);
			
			return forwardURL;
		}
		
		model.addAttribute("resultMap",resultMap);
		
		return forwardURL;
	}
	/**
	 * 导出成绩-生成XML文件
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examresults/export-genflie.html")
	public String exportExamResultsGenFile(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		String [] checkStatus = request.getParameterValues("checkstatus");
		String examSubId      = ExStringUtils.defaultIfEmpty(request.getParameter("examResultExportSubId"),"");
		String operateType    = ExStringUtils.defaultIfEmpty(request.getParameter("type"),"");
		String attId          = ExStringUtils.defaultIfEmpty(request.getParameter("attId"),"");
		
		GUIDUtils.init();
		User user             = SpringSecurityHelper.getCurrentUser();
		ExamSub examSub       = examSubService.get(examSubId);
		Attachs attachs       = attachsService.get(attId);
		String baseDIR        = Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles"+File.separator+Constants.ZIP_FILE_DIR;
		String returnAttId    = "";							 
		String filename       = "";
		String logType        = "";
		Attachs reExportAtt   = null;
		File file             = null;
		List<ExamResultsLog> logList  = null;
   
		try {
			
			if ("export".equals(operateType)) {	//导出
				
				logType               = ExamResults.EXAMRESULTS_FORM_TYPE_EXPORT;
				file                  = new File(baseDIR);
				filename              = GUIDUtils.buildMd5GUID(false) + ".zip";
				if (!file.exists()) {
					file.mkdirs();
				}
				
				
			}else if ("reExport".equals(operateType)) {//重新导出
				
				logType               = ExamResults.EXAMRESULTS_FORM_TYPE_REEXPORT;
				String oldXMLFliePath = attachs.getSerPath();
				File          oldFile = new File(oldXMLFliePath); 
				if (oldFile.exists()) {
					oldFile.delete();
				}
				
			    file                  = new File(baseDIR); 
				filename              = GUIDUtils.buildMd5GUID(false) + ".zip";
				if (!file.exists()) {
					file.mkdirs();
				}
			
			}
			
			ZipOutputStream zos  = new ZipOutputStream(new FileOutputStream(file.getAbsolutePath()+File.separator + filename));
			GenFile geninfo = examResultsExportService.generateZipStreamFromAchievement(examSub, user.getUsername(),checkStatus, zos);
			zos.close();
			
			if (geninfo==GenFile.SUCCESS && "export".equals(operateType)) {
				
				returnAttId = examResultsExportService.writeOperateLog(examSub, user, file.getAbsolutePath(), filename,logType);
				
			}else if (geninfo==GenFile.SUCCESS && "reExport".equals(operateType)) {
				
				attachs.setSerPath(file.getAbsolutePath()+File.separator+filename);
				
				ExamResultsLog log = new ExamResultsLog();
				log.setOptionType(logType);
				log.setAttachId(attachs.getResourceid());
				log.setExamSubId(examSub.getResourceid());
				log.setFillinDate(new Date());
				log.setFillinMan(user.getUsername());
				log.setFillinManId(user.getResourceid());
				examResultsLogService.save(log);
				
				returnAttId = attachs.getResourceid();
			}
			
			logList     = examResultsLogService.findExamResultsLogByExamSubId(examSub.getResourceid());
		} catch (Exception e) {
			logger.error("生成导出至隔离服务器成绩XML文件出错：{}",e.fillInStackTrace());
		}
		
		if (ExStringUtils.isNotEmpty(returnAttId)) {
			model.addAttribute("attId",returnAttId);
		}
		if (logList != null && !logList.isEmpty()) {
			model.addAttribute("logList",logList);
		}
		if (null !=examSub) {
			model.addAttribute("examSub",examSub);
		}
		
		return"/edu3/teaching/examResult/export_examResult-view";
	}
	/**
	 * 下载导出的成绩文件
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examresults/download-file.html")
	public void exportExamResultsDownLoadFile(HttpServletRequest request, HttpServletResponse response){
		String attId    = ExStringUtils.trimToEmpty(request.getParameter("attId"));
		Attachs attachs = attachsService.get(attId);
		if (null!=attachs) {
			try {
				downloadFile(response, attachs.getAttName(), attachs.getSerPath(), false);
			} catch (IOException e) {
				logger.error("下载导出的成绩文件出错:{}",e.fillInStackTrace());
			}
		}
	}
	/**
	 * 下载学位外语统考成绩单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/statexam/download-file.html")
	public void downloadExamInationFile(HttpServletRequest request, HttpServletResponse response){
		
	 
		try {
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();
			File disFile  		  = new File(getDistfilepath()+File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			// 学位外语统考课程，其他统考课程已经分开，有另外入口
//			String stateCourseHQL = "from "+Course.class.getSimpleName()+" course where course.isDeleted=0 and course.isUniteExam='Y' order by course.uniteExamTemplatesOrder";
			String stateCourseHQL = "from "+Course.class.getSimpleName()+" course where course.isDeleted=0 and course.isDegreeUnitExam='Y' order by course.uniteExamTemplatesOrder";
	 	   	List<Course> courses  =  courseService.findByHql(stateCourseHQL);
	 	   
			WritableWorkbook wwb 	 = Workbook.createWorkbook(disFile);
			WritableSheet ws 	  	 = wwb.createSheet("学位外语统考成绩单", 0);
			WritableFont wf     	 = new WritableFont(WritableFont.TIMES,10,WritableFont.BOLD);
			WritableCellFormat wcf = new WritableCellFormat(wf);
			wcf.setBorder(Border.ALL,BorderLineStyle.THIN);
			wcf.setAlignment(Alignment.CENTRE);
			ws.setColumnView(0,20);
			ws.setColumnView(1,30);
			ws.setColumnView(2,10);
			ws.setColumnView(3,30);
			ws.setColumnView(4,40);
			ws.setColumnView(5,20);
			ws.setColumnView(6,20);
			
			Label certificateNo	  = new Label(0, 0, "准考证号",wcf);
			Label studyNo	  	  = new Label(1, 0, "学号",wcf);
			Label name	  	  	  = new Label(2, 0, "姓名",wcf);
			Label certNum	  	  = new Label(3, 0, "证件号码",wcf);
			Label brschool	  	  = new Label(4, 0, "教学点",wcf);
			//Label college  	      = new Label(5, 0, "所在网院",wcf);
			Label scoreNum 	      = new Label(5, 0, "成绩编号",wcf);
			
			int labelIndex        = 6;
			for (int i = 0; i < courses.size(); i++) {
				Label dynamicLabel= new Label(labelIndex,0,courses.get(i).getCourseName(),wcf);
//				Label dynamicLabel1= new Label(labelIndex,1,"[和数据字典CodeAllowStateExamresultsImportSore里的选项名对应]",wcf);
				Label dynamicLabel1= new Label(labelIndex,1,"这里填写成绩，可以填合格、不合格或具体的分数",wcf);
				ws.addCell(dynamicLabel);
				ws.addCell(dynamicLabel1);
				ws.setColumnView(labelIndex,80);
				labelIndex++;
			}
			
			ws.addCell(certificateNo); 
			ws.addCell(studyNo); 
			ws.addCell(name); 
			ws.addCell(certNum); 
			ws.addCell(brschool); 
			//ws.addCell(college); 
			ws.addCell(scoreNum); 
			
			//举例
			Label certificateNo1	  = new Label(0, 1, "例如：1144010411114326",wcf);
			Label studyNo1	  	  = new Label(1, 1, "例如:124434324234",wcf);
			Label name1  	  	  = new Label(2, 1, "例如:王某某",wcf);
			Label certNum1	  	  = new Label(3, 1, "例如:36072918870723321X",wcf);
			Label brschool1	  	  = new Label(4, 1, "例如:校本部",wcf);
			//Label college  	      = new Label(5, 0, "所在网院",wcf);
			Label scoreNum1 	 = new Label(5, 1, "例如:粤14-18246",wcf);
			
			ws.addCell(certificateNo1); 
			ws.addCell(studyNo1); 
			ws.addCell(name1); 
			ws.addCell(certNum1); 
			ws.addCell(brschool1); 
			//ws.addCell(college); 
			ws.addCell(scoreNum1); 
			
			
			wwb.write();
			wwb.close(); 
			
			downloadFile(response, "统考成绩单模板.xls",disFile.getAbsolutePath(), true);
		} catch (Exception e) {
			logger.error("生成统考成绩单模板出错:{}"+e.fillInStackTrace());
		}
	}

	
	
	/**
	 * 解析上传的统考成绩单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/result/statexam/analysis-examination-file.html")
	public void analysisExamInationFileResults(HttpServletRequest request, HttpServletResponse response){
		
		Map<String,Object> returnMap 		  = new HashMap<String, Object>();
		Map<String,String> alp          	  = new HashMap<String, String>();			  //统考成绩单中允许的成绩分值项Map
		Map<String,String> stateCourse        = new HashMap<String, String>(); 			  //统考课程
		Map<String,String> replaceCourse_1    = new HashMap<String, String>();            //正常代替课程
		Map<String,String> replaceCourse_2    = new HashMap<String, String>();            //高起本的代替课程
		boolean success 			  		  = false;
		StringBuffer msg     		  		  = new StringBuffer();
		String passDate 			  		  = ExStringUtils.defaultIfEmpty(request.getParameter("passDate"), "");
		String attId 				  	  	  = ExStringUtils.trimToEmpty(request.getParameter("attachId"));
		int excelCount = 0;
		int excelsuccess = 0;
		
		try {
			
			if (null!=attId&&attId.split(",").length>1) {
				success 			  		  = false;
				msg.append("为了保证导入效率请一次导入一个成绩文件,谢谢！");
			}else if(null!=attId&&attId.split(",").length>0&&attId.split(",").length<2){
				Attachs attachs 	   		 	    = attachsService.get(attId.split(",")[0]);
				String filePath                     = attachs.getSerPath()+File.separator+attachs.getSerName();
				//从数据字典中获取统考课程 课程名，用于跟导入的成绩单中做匹配(字典中排序为-1的选项为 统考课程)
				List<Dictionary> dict  = CacheAppManager.getChildren("CodeStateExamResultsReplaceCourse");
				
				for (Dictionary d:dict ) {
					
					String  replaceCourseId    = d.getDictValue();  //统考课程代替的课程ID 
					String [] rci = replaceCourseId.split("_");
					
					if (-1==d.getShowOrder()) {
						String  stateCourseName = d.getDictName();   //统考课程名       
						String  stateCourseId  = d.getDictValue();  //统考课程ID    
						stateCourse.put(stateCourseName, stateCourseId);
						
					}else if(-2==d.getShowOrder()){//高起本的大学英语统考除了代替大英B（三）、大英B（四）还代替大英B（一）大英B（二）
						String oldReplaceCourseId   = ExStringUtils.defaultIfEmpty(replaceCourse_2.get(rci[0]), "");
						String stateCourseId = rci[0];
						replaceCourse_2.put(stateCourseId, oldReplaceCourseId+","+rci[1]);
						
					}else {      
						String oldReplaceCourseId   = ExStringUtils.defaultIfEmpty(replaceCourse_1.get(rci[0]), "");
						String stateCourseId        = rci[0];
						replaceCourse_1.put(stateCourseId, oldReplaceCourseId+","+rci[1]);
					}
				}
				
				//获取统考成绩单中允许的成绩分值项
				List<Dictionary> allowScore = CacheAppManager.getChildren("CodeAllowStateExamresultsImportSore");
				for (Dictionary dic:allowScore) {
					alp.put(ExStringUtils.trim(dic.getDictName()), ExStringUtils.trim(dic.getDictValue()));
				}
				//解析上传的EXCEL文件写入临时表
				Map<String,String> map = new HashMap<String, String>();
				map = stateExamResultsService.analysisExamInationFile(filePath,attachs.getResourceid(),alp);
				msg.append(map.get("msg"));
				//excel里要插入的记录数量
				excelCount = Integer.parseInt(map.get("excelCount"));
				//批量插入统考记录表
				map = stateExamResultsService.batchInputStateResults(stateCourse, replaceCourse_1, replaceCourse_2, attId.split(",")[0], passDate);
				msg.append(map.get("msg"));
				excelsuccess = Integer.parseInt(map.get("successNum")); //成功数量
				File f = new File(filePath);
				if (f.exists()) {
					f.delete();
				}
				attachsService.delete(attachs);
			
				success = true;
			}else {
				success = false;
				msg.append("请上传一个统考成绩单！");
			}
			
		} catch (Exception e) {
			success = false;
			logger.error("解析上传的统考成绩单出错：{}",e.fillInStackTrace());
			msg.append("解析excel失败,请重新上传excel;");
		}

		returnMap.put("success",success);
		returnMap.put("msg",msg);
		returnMap.put("result","导入共"+excelCount+"条,成功"+excelsuccess+"条,失败"+(excelCount-excelsuccess)+"条");
		renderJson(response,JsonUtils.mapToJson(returnMap));
	}
	
	
	/**
	 * 打印个人成绩单-打印全部功能 (页面)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalReportCard-printall-page.html")
	public String personalReportCardPrintAllPage(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classId  			 = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studentStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
		String learningStyle		 = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String totalSize             = ExStringUtils.trimToEmpty(request.getParameter("totalSize"));
		String flag                  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String graduateDate          = ExStringUtils.trimToEmpty(request.getParameter("graduateDateStr"));
		String degreeUnitExam        = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩
		//新添加的条件
		String confirmGraduateDateb  = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDatee  = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));
		String printPage          	 = ExStringUtils.trimToEmpty(request.getParameter("printPage"));
		String studyNo          	 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String name          	 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		
		User curUser                 = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit                 = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unit                     = curUser.getOrgUnit();
			model.addAttribute("isBrschool", true);
		}
		
		if (null!=unit) {
			branchSchool = unit.getResourceid();
		}
		if (ExStringUtils.isBlank(studentStatus)) {
			studentStatus="11";
		}
		if((!ExStringUtils.isBlank(confirmGraduateDateb)||!ExStringUtils.isBlank(confirmGraduateDatee)||ExStringUtils.isNotEmpty(graduateDate))&&"11".equals(studentStatus)) {
			studentStatus="16";
		}
		if (ExStringUtils.isBlank(totalSize)) {
			totalSize    = "0";
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId", classId);
		}
		if(ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId", classId);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus",studentStatus);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}
		if(ExStringUtils.isNotEmpty(flag)) {
			condition.put("flag",flag);
		}
		if(ExStringUtils.isNotEmpty(totalSize)) {
			condition.put("totalSize",totalSize);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDateStr",graduateDate);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			condition.put("graduateDateStr",graduateDate);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDateb)) {
			condition.put("confirmGraduateDateb", confirmGraduateDateb);
		}
		if(!ExStringUtils.isBlank(printPage)) {
			condition.put("printPage", printPage);
		}
		if(!ExStringUtils.isBlank(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(!ExStringUtils.isBlank(name)) {
			condition.put("name", name);
		}
		if(!ExStringUtils.isBlank(degreeUnitExam)) {
			condition.put("degreeUnitExam", degreeUnitExam);
		}
		List<String> spriltList      = new ArrayList<String>();
		if (Integer.valueOf(totalSize)>=100) {
			Integer index            = Integer.valueOf(totalSize)/100;
			if (Integer.valueOf(totalSize)%100>0) {
				index +=1;
			}
			for (int i = 0; i < index; i++) {
				Integer start = i*100+1;
				if (i < (index-1)) {
					spriltList.add(String.valueOf(start)+"-"+(start+100-1));
				}else{
					spriltList.add(String.valueOf(start)+"-"+(Integer.valueOf(totalSize)));
				}
			}
		}else {
			spriltList.add(String.valueOf(1)+"-"+Integer.valueOf(totalSize));
		}
		model.put("spriltList", spriltList);
		model.put("condition", condition);
		
		
		return "/edu3/teaching/examResult/personalReportCardSplitPrintAll";
	}
	/**
	 * 打印个人成绩单-打印全部功能(预览)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalReportCard-printall-view.html")
	public String personalReportCardPrintAllView(HttpServletRequest request, HttpServletResponse response, ModelMap model){

		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classId  			 = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studentStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
		String learningStyle		 = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String flag                  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String segment               = ExStringUtils.trimToEmpty(request.getParameter("segment"));
		String pageNum               = ExStringUtils.trimToEmpty(request.getParameter("pageNum"));
		String totalSize             = ExStringUtils.trimToEmpty(request.getParameter("totalSize"));
		String graduateDate          = ExStringUtils.trimToEmpty(request.getParameter("graduateDateStr"));
		
		//新添加的条件
		String confirmGraduateDateb  = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDatee  = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));
		String printPage          = ExStringUtils.trimToEmpty(request.getParameter("printPage"));
		String studyNo          	 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String name          	 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String degreeUnitExam        = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩
		String passExam				 = ExStringUtils.trimToEmpty(request.getParameter("passExam"));
		User curUser                 = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit                 = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unit                     = curUser.getOrgUnit();
		}
		
		if (null!=unit) {
			branchSchool = unit.getResourceid();
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			model.addAttribute("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			model.addAttribute("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			model.addAttribute("major", major);
		}
		if(ExStringUtils.isNotEmpty(classId)) {
			model.addAttribute("classId", classId);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			model.addAttribute("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(flag)) {
			model.addAttribute("flag", flag);
		}
		if(ExStringUtils.isNotEmpty(segment)) {
			model.addAttribute("segment", segment);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			model.addAttribute("studentStatus",studentStatus);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			model.addAttribute("learningStyle",learningStyle);
		}
		if(ExStringUtils.isNotEmpty(pageNum)) {
			model.addAttribute("pageNum",pageNum);
		}
		if(ExStringUtils.isNotEmpty(totalSize)) {
			model.addAttribute("totalSize",totalSize);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			model.addAttribute("graduateDateStr",graduateDate);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)) {
			model.addAttribute("graduateDateStr",graduateDate);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDatee)) {
			model.addAttribute("confirmGraduateDatee", confirmGraduateDatee);
		}
		if(!ExStringUtils.isBlank(printPage)) {
			model.addAttribute("printPage", printPage);
		}
		if(!ExStringUtils.isBlank(studyNo)) {
			model.addAttribute("studyNo", studyNo);
		}
		if(!ExStringUtils.isBlank(name)) {
			model.addAttribute("name", name);
		}
		if(!ExStringUtils.isBlank(degreeUnitExam)) {
			model.addAttribute("degreeUnitExam", degreeUnitExam);
		}
		if(ExStringUtils.isNotEmpty(passExam)) {
			model.addAttribute("passExam", passExam);
		}
		return "/edu3/teaching/examResult/personal-reportcard-printall-view";
	}
	/**
	 * 打印个人成绩单-打印全部功能
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalReportCard-printall.html")
	public void personalReportCardPrintAll(HttpServletRequest request, HttpServletResponse response, Page page){
		
		page.setPageSize(100);
		page.setAutoCount(false);
		
		Map<String,Object> condition = new HashMap<String, Object>();
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classId  			 = ExStringUtils.trimToEmpty(request.getParameter("classId"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String studentStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("studentStatus"));
		String learningStyle		 = ExStringUtils.trimToEmpty(request.getParameter("learningStyle"));
		String flag		 			 = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String pageNum               = ExStringUtils.trimToEmpty(request.getParameter("pageNum"));
		String totalSize             = ExStringUtils.trimToEmpty(request.getParameter("totalSize"));
		String graduateDate          = ExStringUtils.trimToEmpty(request.getParameter("graduateDateStr"));
		String terms 			  = ExStringUtils.trimToEmpty(request.getParameter("terms")); //学期
		String printExam          	 = ExStringUtils.trimToEmpty(request.getParameter("printExam"));
		
		//新添加的条件
		String confirmGraduateDateb  = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDateb"));
		String confirmGraduateDatee  = ExStringUtils.trimToEmpty(request.getParameter("confirmGraduateDatee"));
		String studyNo          	 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String name          	 	 = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String degreeUnitExam        = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩
		User curUser                 = SpringSecurityHelper.getCurrentUser();
		OrgUnit unit                 = null;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			unit                     = curUser.getOrgUnit();
		}
		page.setPageNum(Integer.parseInt(pageNum));
		page.setTotalCount(Integer.parseInt(totalSize));
		
		if (null!=unit) {
			branchSchool = unit.getResourceid();
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classId)) {
			condition.put("classId", classId);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studentStatus)) {
			condition.put("studentStatus",studentStatus);
		}
		if(ExStringUtils.isNotEmpty(learningStyle)) {
			condition.put("learningStyle",learningStyle);
		}
		if(ExStringUtils.isNotEmpty(graduateDate)){
			condition.put("graduateDateStr",graduateDate);
			condition.put("graduateDate", ExDateUtils.convertToDate(condition.get("graduateDateStr").toString()));
		}
		if(!ExStringUtils.isBlank(confirmGraduateDateb)) {
			condition.put("confirmGraduateDateb", confirmGraduateDateb);
		}
		if(!ExStringUtils.isBlank(confirmGraduateDatee)) {
			condition.put("confirmGraduateDatee", confirmGraduateDatee);
		}
		if(!ExStringUtils.isBlank(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(!ExStringUtils.isBlank(name)) {
			condition.put("name", name);
		}
		
		String hql			 		 = genHQL("studentInfo",condition);
		condition.put("isDeleted",0);
		page						 = studentInfoService.findByHql(page, hql, condition);
		
		try {

			StringBuffer ids = new StringBuffer();
			List list = page.getResult();
			/*for (int i = 0; i < list.size(); i++) {
				StudentInfo stu =  (StudentInfo)list.get(i);
				ids.append(","+stu.getResourceid());
			}*/

			if (ids.length()>0 || list.size()>0) {
				if ("all".equals(flag)) {
					printPersonalAllReportCard(request,response,list,"",printExam,terms,degreeUnitExam);
				}else if ("pass".equals(flag)) {
					printPersonalPassReportCard(request,response,list,"","");
				}else if ("en".equals(flag)) {
					//printPersonalEnReportCard(request,response,studentId,printDate);
				}
			}
		} catch (Exception e) {
			logger.error("打印学生成绩单出错：{}"+e.fillInStackTrace());
		}
	}
	
	/**
	 * 打印个人成绩单-预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalReportCard-view.html")
	public String personalReportCardView(HttpServletRequest request, ModelMap model){
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);

		String studyTime = ExStringUtils.trimToEmpty(request.getParameter("studyTime"));
		//model.addAttribute("classesid", ExStringUtils.trimToEmpty(request.getParameter("classes")));//班级

		try {
			studyTime = URLEncoder.encode(studyTime,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		model.addAttribute("studyTime",studyTime);
		return "/edu3/teaching/examResult/personal-reportcard-printview";
	}
	
	/**
	 * 打印历年成绩单-预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalYearReportCard-view.html")
	public String personalYearReportCardView(HttpServletRequest request, ModelMap model){
		model.addAttribute("flag",ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.addAttribute("printPage",ExStringUtils.trimToEmpty(request.getParameter("printPage")));
		model.addAttribute("studentId",ExStringUtils.trimToEmpty(request.getParameter("studentId")));
		model.addAttribute("printDate",ExStringUtils.trimToEmpty(request.getParameter("printDate")));
		String studyTime = ExStringUtils.trimToEmpty(request.getParameter("studyTime"));
		model.addAttribute("classesid", ExStringUtils.trimToEmpty(request.getParameter("classes")));//班级
		model.addAttribute("terms", ExStringUtils.trimToEmpty(request.getParameter("terms")));//学期
		model.addAttribute("electronicalSign", ExStringUtils.trimToEmpty(request.getParameter("electronicalSign")));// 是否使用电子签名
		model.addAttribute("degreeUnitExam",ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam")));//学位外语成绩
		model.addAttribute("passExam",ExStringUtils.trimToEmpty(request.getParameter("passExam")));
		try {
			studyTime = URLEncoder.encode(studyTime,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		model.addAttribute("studyTime",studyTime);
		return "/edu3/teaching/examResult/personalyear-reportcard-printview";
	}
	
	/**
	 * 按班级打印成绩
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/teaching/result/personalReportCard-by-classes.html")
	public void personalReportCardbyClasses(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception{
		//TODO 经本地测试，每个学生(16级)导出平均耗时0.16秒，需要进行优化，一次导出人数建议不超过600人(约1.5分钟) --20180921
		Map<String,Object> condition 					= 	Condition2SQLHelper.getConditionFromResquestByIterator(request);

		String terms    		     = ExStringUtils.trimToEmpty(request.getParameter("terms"));//计划学期
		//String studentStatus 		 = ExStringUtils.trimToEmpty(request.getParameter("studentStatus")); //学籍状态
		String degreeUnitExam        = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩

		//if(ExStringUtils.isNotBlank(studentStatus))			condition.put("studentStatus", studentStatus);

		condition.put("addHql"," and so.classes.resourceid!=null ");

		String school = (null == CacheAppManager.getSysConfigurationByCode("graduateData.schoolName")?"":CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue())+
										(null == CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName")?"":CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
		String schoolCode =  CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		String hk = null == CacheAppManager.getSysConfigurationByCode("ExamResultCorrosion") ? "" : CacheAppManager.getSysConfigurationByCode("ExamResultCorrosion").getParamValue();
		boolean isOnlyOneClass = true;//导出结果只有一个班级时使用
		String classesName = "";
		long startTime = System.currentTimeMillis();

		//所有学生学籍信息
		List<StudentInfo> studentInfos 					= 	studentInfoService.findByCondition(condition);
		//获取所有课程的学生的交集,不用每个学生去搜寻一遍
		List<StudentInfo> studentInfoForTitles 			= 	studentInfoService.findAllExamResultsStudentInfo(condition);
		if(null != studentInfoForTitles && studentInfoForTitles.size() > 0){
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();
			File excelFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//是否需要把XYMC特殊处理
			List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
					, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
			String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
			String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
			for (int i = 0; i < listDictZYDM.size(); i++) {
				String rpstr1 = ExStringUtils.trim(listDictZYDM.get(i).getDictName() + "").replace("（", "(").replace("）", ")");
				String rpstr2 = ExStringUtils.trim(listDictZYDM.get(i).getDictValue() + "").replace("（", "(").replace("）", ")");
				zymcReplaceArrayL[i] = rpstr1;
				zymcReplaceArrayR[i] = rpstr2;
			}
			List<String> tempList  = new ArrayList<String>(0);
			if(ExStringUtils.isNotBlank(terms)){
				String[] term_array = terms.split(",");
				tempList = Arrays.asList(term_array);
			}else{
				tempList = null;
			}
			//所有学生成绩
			List<StudentExamResultsVo> examResultsList = teachingplanservice.printResultsByClassesListMap(studentInfos, tempList, degreeUnitExam);
			Map<String,Integer>  mapDict = teachingplanservice.getDictIndext("ExamResult");
			//excel 工作表
			int sheetNum = 0;

			//追加一个空的数据，用于处理最后一个班级成绩表
			StudentInfo info = new StudentInfo();
			info.setClassesid("");
			studentInfoForTitles.add(info);
			StudentInfo info4Title_pre = studentInfoForTitles.get(0);
			Map<String,Object> courseid_mapInfo = new HashMap<String, Object>();
			List<StudentExamResultsVo> examResultsList4title = new ArrayList<StudentExamResultsVo>();
			List<StudentExamResultsVo> classesExamResultsList = new ArrayList<StudentExamResultsVo>();
			Map<String,String> learningStyleMap = dictionaryService.getDictMapByParentDictCode("CodeLearningStyle");
			for (StudentInfo info4Title: studentInfoForTitles) {
				if (!info4Title.getClassesid().equals(info4Title_pre.getClassesid())) {
					//获取每个班级所有学生成绩
					classesExamResultsList = new ArrayList<StudentExamResultsVo>(10000);
					examResultsList4title = new ArrayList<StudentExamResultsVo>(25);
					//获取每个班级不同学生课程成绩，用于生成成绩表课程标题信息
					for (StudentExamResultsVo examResult:examResultsList) {
						if (info4Title_pre.getClassesid().equals(examResult.getClassesid())) {
							classesExamResultsList.add(examResult);
							if (!courseid_mapInfo.containsKey(examResult.getCourseId())) {
								examResultsList4title.add(examResult);
								courseid_mapInfo.put(examResult.getCourseId(),"");
							}
						}
					}
					//从总成绩表删除当前班级学生成绩，以减少遍历次数
					examResultsList.removeAll(classesExamResultsList);

					Map<String, Object> template = new HashMap<String, Object>();
					template.put("unitname", "教学点：" + info4Title_pre.getUnitName());
					String year = info4Title_pre.getEduYear() + "年制";
					template.put("xz", "学制：" + year + learningStyleMap.get(info4Title_pre.getLearningStyle()));
					String majorName = info4Title_pre.getMajorName();
					if (null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)) {
						majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
					}
					template.put("majorname", "专业：" + majorName);
					template.put("classicname", "班级：" + info4Title_pre.getClassName());
					if (ExStringUtils.isNotBlank(classesName) && !classesName.equals(info4Title_pre.getClassName())) {
						isOnlyOneClass = false;
						classesName = "";
					} else if(isOnlyOneClass){
						classesName = info4Title_pre.getClassName();
					}

					//获取表格title
					//LinkedList<String> titlelist = teachingplanservice.printResultsByClassesTitleListMap(studentInfoForTitles_temp, terms, degreeUnitExam);
					LinkedList<String> titlelist = teachingplanservice.printResultsByClassesTitleListMap_new(examResultsList4title, terms, degreeUnitExam);
					//自定义表头
					Map<String, Object> dynamicTitleMap = new LinkedHashMap<String, Object>(30);
					if (ExCollectionUtils.isNotEmpty(classesExamResultsList)) {
						for (String string : titlelist) {
							if (ExStringUtils.isNotEmpty(string)) {
								String string_id = string.split("_")[1];
								//Course c = courseService.get(string_id);
								String courseName = "";
								if (string.split("_").length==5) {
									courseName = string.split("_")[2];
								}
								dynamicTitleMap.put(string, ExStringUtils.isBlank(courseName)?string_id:courseName);
							}
						}
						//将一个班级的学生成绩VO转为Map(一个VO对应一个学生的一门课程成绩，一个Map对应一个学生所有成绩)
						List<LinkedHashMap<String,Object>> classesExamResultsMaps = teachingplanservice.getStuExamMapByExamVOList(classesExamResultsList,mapDict,degreeUnitExam,hk,schoolCode);
						try {
							//初始化配置参数
							exportExcelService.initParmasByExcelConfigInfo(excelFile, exportExcelService.getExcelConfigInfo(dynamicTitleMap), classesExamResultsMaps, null);
							exportExcelService.getModelToExcel().setDynamicTitle(true);//动态表头
							exportExcelService.getModelToExcel().setDynamicTitleMap(dynamicTitleMap);
							exportExcelService.getModelToExcel().setSheet(++sheetNum, info4Title_pre.getClassName());
							exportExcelService.getModelToExcel().setHeaderCellFormat(22);
							exportExcelService.getModelToExcel().setHeader(school + "学生成绩表");
							excelFile = exportExcelService.getModelToExcel().getExcelfileByDynamicTitle_SpecialTitleFormatWithClasses(template);//获取导出的文件
						} catch (Exception e) {
							logger.error("导出excel文件出错：" + e.fillInStackTrace());
						}
					}
					courseid_mapInfo.clear();
				}
				info4Title_pre = info4Title;
			}
			//System.out.println("examResultsList:"+examResultsList.size());
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			logger.info("------>>>按班级导出成绩("+(sheetNum)+"):"+(System.currentTimeMillis()-startTime)/1000.0+" s...");
			downloadFile(response, ("".equals(classesName)?school:classesName)+"_班级成绩表.xls", excelFile.getAbsolutePath(),true);
		}else {
			renderHtml(response, "<script>alert('没有导出成绩!')</script>");
		}
	}

	
	/**
	 * 打印个人成绩单-打印
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalReportCard-print.html")
	public void personalReportCard(HttpServletRequest request, HttpServletResponse response){
		
		String flag 	 		  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String studentIds 		  = ExStringUtils.trimToEmpty(request.getParameter("studentIds"));
		String printDate 		  = ExStringUtils.trimToEmpty(request.getParameter("printDate"));
		String studyTime          = ExStringUtils.trimToEmpty(request.getParameter("studyTime"));
		String printExam		  = ExStringUtils.trimToEmpty(request.getParameter("printExam"));
		String terms 			  = ExStringUtils.trimToEmpty(request.getParameter("terms")); //学期
		String degreeUnitExam     = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩
		String majorId			  = ExStringUtils.trimToEmpty(request.getParameter("majorId"));

		try {
			studyTime  			  = URLDecoder.decode(studyTime,"UTF-8");
			Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();

			String isShowOrder = "N";//广外：按专业查询时显示序号
			if(ExStringUtils.isNotBlank(studentIds)){//勾选打印
				condition.put("studentIds", Arrays.asList(studentIds.split(",")));
			}else {//查询打印
				condition.remove("studentIds");
				if(ExStringUtils.isNotBlank(majorId)){
					isShowOrder = "Y";
				}
				/*if (!condition.containsKey("studentStatus")) {
					condition.put("studentStatus", "'11','12','21','25'");
				}*/
			}
			studentInfos = studentInfoService.findByCondition(condition);
			if ("all".equals(ExStringUtils.trim(flag))) {
				printPersonalAllReportCard(request,response,studentInfos,printDate,printExam,terms,degreeUnitExam);
			} else if ("pass".equals(ExStringUtils.trim(flag))) {
				printPersonalPassReportCard(request,response,studentInfos,printDate,studyTime);
			} else if("fail".equals(ExStringUtils.trim(flag))){
				printPersonalFailReportCard(request,response,studentIds,printDate,studyTime);
			} else if ("en".equals(ExStringUtils.trim(flag))) {
				printPersonalEnReportCard(request,response,studentIds,printDate);
			} else if("classes".equals(ExStringUtils.trim(flag))){ //按班级打印学生成绩
				printPersonalClassesReportCard(request,response, ExStringUtils.trimToEmpty(request.getParameter("classes")),printDate);
			} else if("gdy".equals(ExStringUtils.trim(flag)) || "gdwy".equals(flag)){// 广东医，广东外语
				// 没有选择学期，默认打印所有教学计划内课程
				if(ExStringUtils.isEmpty(terms)){
					List<Dictionary> termsDic = CacheAppManager.getChildren("CodeTermType");
					if(ExCollectionUtils.isNotEmpty(termsDic)){
						for(int i=0;i<termsDic.size();i++){
							terms +=termsDic.get(i).getDictValue()+",";
						}
						terms = terms.substring(0, terms.lastIndexOf(","));
					}else {
						terms = "1,2,3,4,5,6,7,8,9,10";
					}
				}
				printPersonalGdyReportCard(request,response,studentInfos,printDate,printExam,terms,degreeUnitExam,isShowOrder);
			}
			
		} catch (Exception e) {
			logger.error("打印学生成绩单出错：{}"+e.fillInStackTrace());
		}
	}

	/**
	 * 打印历年成绩单-打印
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/personalYearReportCard-print.html")
	public void personalYearReportCard(HttpServletRequest request, HttpServletResponse response){
		
		//String flag 	 		  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		//String printPage 	 		  = ExStringUtils.trimToEmpty(request.getParameter("printPage"));
		String studentId 		  = ExStringUtils.trimToEmpty(request.getParameter("studentId"));
		String printDate 		  = ExStringUtils.trimToEmpty(request.getParameter("printDate"));
		String studyTime          = ExStringUtils.trimToEmpty(request.getParameter("studyTime"));
		String printExam		  = ExStringUtils.trimToEmpty(request.getParameter("printExam"));
		String terms 			  = ExStringUtils.trimToEmpty(request.getParameter("terms")); //学期
		String classes			  = ExStringUtils.trimToEmpty(request.getParameter("classes"));
		String isUseElectronicalSign  = ExStringUtils.trimToEmpty(request.getParameter("electronicalSign")); // 是否使用电子签名
		String degreeUnitExam        = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩
		try {
			studyTime  = URLDecoder.decode(studyTime,"UTF-8");
			Map<String, Object> condition = new HashMap<String, Object>();
			List<StudentInfo> studentInfos = new ArrayList<StudentInfo>();
			
			if(ExStringUtils.isNotBlank(studentId)){//勾选打印
				condition.put("studentIds", Arrays.asList(studentId.split(",")));
			}else {//查询打印
				//if(ExStringUtils.isNotBlank(gradeId)) condition.put("gradeId", gradeId);
				//if(ExStringUtils.isNotBlank(majorId)) condition.put("majorId", majorId);
				if(ExStringUtils.isNotBlank(classes)) {
					condition.put("classesid", classes);
				}
			}
			studentInfos = studentInfoService.findByCondition(condition);
			printPersonalYearReportCard(request,response,studentInfos,printDate,printExam,terms,isUseElectronicalSign,degreeUnitExam);
			//以后再做优化
			//List<StudentExamResultsVo> list = printPersonalYearReportCard_new(condition);
		
		} catch (Exception e) {
			logger.error("打印学生成绩单出错：{}"+e.fillInStackTrace());
		}
	}

	/**
	 * 打印历年成绩单
	 * @param request
	 * @param response
	 * @param studentList
	 * @param printDate
	 */
	private void printPersonalYearReportCard(HttpServletRequest request, HttpServletResponse response, List<StudentInfo> studentList,
											 String printDate, String printExam, String terms, String isUseElectronicalSign, String degreeUnitExam){
		
		Map<String, Object> param = new HashMap<String, Object>();
		String isPdf		  = ExStringUtils.trimToEmpty(request.getParameter("isPdf"));
		JasperPrint jasperPrint   = null;//输出的报表
		//String [] ids 			  = studentId.split(",");
		User curUser              = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool        = false;
		String printPage          	= ExStringUtils.trimToEmpty(request.getParameter("printPage"));//哪种打印功能
		String passExam				= request.getParameter("passExam");
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool            = true;
		}
		try {
			String jasper = "";
			String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			String scutLogoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
			// 上传根路径
			String rootPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
			// 电子签名根路径
			String signRootPath = rootPath + "examResultReportSign";
			// 如果没有电子签名或者不使用则使用空白图片
			String signDean = "signBlank.png";
			String signAuditor = "signBlank.png";
			// 校本部院长签名
			Attachs signDean0 = attachsService.getByConditions("signDean0.png", "examResultReportSign", signRootPath);
			// 校本部审核人签名
			Attachs signAuditor0 = attachsService.getByConditions("signAuditor0.png", "examResultReportSign", signRootPath);
			// 校外点院长签名
			Attachs signDean1 = attachsService.getByConditions("signDean1.png", "examResultReportSign", signRootPath);
			// 校外点审核人签名
			Attachs signAuditor1 = attachsService.getByConditions("signAuditor1.png", "examResultReportSign", signRootPath);
			String showpresident = CacheAppManager.getSysConfigurationByCode("printreport.showpresident").getParamValue();
			
			Map<String, Map<String, Object>> studentInfoMap = studentInfoService.getStudentInfoMapByStuList(studentList);
			
			List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
					, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
			long start = Calendar.getInstance().getTimeInMillis();
			if (ExStringUtils.isNotEmpty(printDate)) {
				printDate = ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),4);
			}else {
				printDate = ExDateUtils.formatDateStr(ExDateUtils.formatDate(new Date(),"yyyy-MM-dd"), 4);
			}
			for (int i = 0; i < studentList.size(); i++) {
				StudentInfo studentInfo = studentList.get(i);
				//选修课次数
				Integer examCount = Integer.parseInt(studentInfoMap.get(studentInfo.getResourceid()).get("examcount").toString());
				//增加字段，标识是否打印成绩单：打印成绩单，则学期取教学计划原定学期，非打印成绩单，则取课程开课学期（或课程考试信息表记录的学期）
				List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(studentInfo,"print",examCount);
				List<StudentExamResultsVo> removelist = new ArrayList<StudentExamResultsVo>();
				Map<String, Object> studentScoreMap = new HashMap<String, Object>();
//				StudentExamResultsVo degreeResultsVo = null;
				if (null != list && list.size() > 0) {//删除未发布成绩和重复成绩
					String courseid = "";
					String examScoreStr_N = "";
					String examScoreStr_Y = "";
					String examScoreStr_Q = "";
					Long examScore_N = null;//正考成绩
					Long examScore_Y = null;//最大补考成绩
					Long examScore_Q = null;//最大结补成绩
					for (int j = 0; j < list.size(); j++) {
						StudentExamResultsVo result = list.get(j);
						if(!"4".equals(result.getCheckStatusCode())){//删除非发布状态成绩
							removelist.add(result);
							continue;
						}
						//只打印及格成绩
						if ("Y".equals(passExam)) {
							if (!"Y".equals(result.getIsPass())) {
								removelist.add(result);
							}
						}
						if ("N".equals(result.getExamSubType())) {
							//同一门课程会有多次正考（选修课），所以将正考成绩存放在Map中比较大小
							examScore_N = ExNumberUtils.getMaxNumber(result.getIntegratedScoreL(),(Long)studentScoreMap.get(result.getCourseId()));
							if(examScore_N.equals(result.getIntegratedScoreL())){
								examScoreStr_N = result.getIntegratedScore();
							}
							studentScoreMap.put(result.getCourseId(), examScore_N);
						}else if ("Y".equals(result.getExamSubType()) || "T".equals(result.getExamSubType())) {
							examScore_Y = ExNumberUtils.getMaxNumber(result.getIntegratedScoreL(),examScore_Y);
							if(examScore_Y.equals(result.getIntegratedScoreL())){
								examScoreStr_Y = result.getIntegratedScore();
							}
						}else if ("Q".equals(result.getExamSubType())) {//暂时未加入返校补成绩（R,G）
							examScore_Q = ExNumberUtils.getMaxNumber(result.getIntegratedScoreL(),examScore_Q);
							if(examScore_Q.equals(result.getIntegratedScoreL())){
								examScoreStr_Q = result.getIntegratedScore();
							}
						}
						//获取最大补考和结补成绩(打印历年成绩)，其它情况显示最后一次考试成绩
						if(courseid.equals(result.getCourseId())){//如果已经存在该课程则替换成绩
							StudentExamResultsVo resultPer = list.get(j-1);
							if("N".equals(result.getExamSubType())){//选修课-多次正考
								result.setIntegratedScoreL(examScore_N);
								result.setIntegratedScore(examScoreStr_N);
							}else {
								result.setBkScore(ExStringUtils.toString(examScoreStr_Y));
								result.setJbScore(ExStringUtils.toString(examScoreStr_Q));
								examScore_Y = null;
								examScore_Q = null;
								examScoreStr_Y = "";
								examScoreStr_Q = "";
							}
							removelist.add(resultPer);
						}
							
						/*if(result.getCourseTypeCode().equals("11") && result.getCourseName().contains("学位英语")){
								removelist.add(result);
//								degreeResultsVo = result;
						}*/
						//如果不是缓考，则把中文成绩复制给综合成绩（前面所有异常成绩、字符成绩、等级成绩已经得到处理）
						/*if (ExStringUtils.isNotBlank(result.getExamResultsChs())) {
							if(!"缓考".equals(result.getExamResultsChs())){
								result.setIntegratedScore(result.getExamResultsChs());
							}
						}*/
						courseid = result.getCourseId();
					}
				}
				//删除成绩统计
				StudentExamResultsVo statVo     = null;
				if (null!=list&&list.size()>0) {
					statVo     = list.get(list.size()-1);
					removelist.add(statVo);
				}
				list.removeAll(removelist);
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
				String reprotFile     		          = "";
				list= filtrateList(list,terms,studentInfo,degreeUnitExam);

				dataSource = new JRBeanCollectionDataSource(list);
				String xz = ExStringUtils.toString(studentInfoMap.get(studentInfo.getResourceid()).get("eduYear"));

				if("gxykd".equals(printPage)){//[打印全部成绩] --广西医科大个性化成绩单   默认打印教学计划内所有成绩
					jasper = "personalReportCard_gxykd.jasper";
					param.put("school", schoolName);
					String degree = "";
					String degreeEnglish = "";
					//待优化（此处逻辑每次大约耗时0.11秒）
					//广西医科大才显示学位信息
					List<StudentGraduateAndDegreeAudit> degreeAuditList = studentGraduateAndDegreeAuditService.findByHql(" from "+StudentGraduateAndDegreeAudit.class.getSimpleName()+" where studentInfo.resourceid=? and degreeAuditStatus=1" , studentInfo.getResourceid());
					if(degreeAuditList!=null && degreeAuditList.size()>0){//审核通过则获取学位及英语成绩
						if(degreeAuditList.get(0).getGraduateData()!=null && ExStringUtils.isNotBlank(degreeAuditList.get(0).getGraduateData().getDegreeName())){
							degree = degreeAuditList.get(0).getGraduateData().getDegreeName();
						}else if(ExStringUtils.isNotBlank(studentInfoMap.get(studentInfo.getResourceid()).get("degreename"))){
							List<Dictionary> list_dict = CacheAppManager.getChildren("CodeDegree");
							for (Dictionary dictionary : list_dict) {
								if(dictionary.getDictValue().equals(degree)){
									degree=dictionary.getDictName();
									break;
								}
							}
						}
						if(ExStringUtils.isNotBlank(degree)){//有学位名称才获取成绩
							Map<String, Object> condition = new HashMap<String, Object>();
							condition.put("studentNO", studentInfo.getStudyNo());
							Map<String, Object> bExamResults = bachelorExamResultsService.getTopExamByStudyNo(studentInfo.getStudyNo());
							if(bExamResults!=null && bExamResults.get("examresults")!=null){
								degreeEnglish = bExamResults.get("examresults").toString();
							}
						}
					}
					if(ExStringUtils.isNotBlank(degree)){//如果有学位信息
						param.put("degree", "授予学位："+degree);
						param.put("degreeEnglish","学位英语成绩："+degreeEnglish);
					}else {//无学位信息
						param.put("degree", "");
						param.put("degreeEnglish","");
					}
				}else if ("stdx".equals(printPage)) {//汕大个性化成绩单
					jasper = "personalReportCard_stdx.jasper";
					param.put("school", schoolName);
					xz = ExStringUtils.str2character(xz)+"年";
					printDate = ExStringUtils.str2character(printDate).replace("零", "〇");
				} else if ("wyys".equals(printPage)) {
					jasper = "personalReportCard_wyys.jasper";
					if (list.size() > 34) {//一列可以放置34行课程成绩
						removelist = new ArrayList<StudentExamResultsVo>();
						for (int j = 34; j < list.size(); j++) {
							StudentExamResultsVo vo  = list.get(j-34);
							StudentExamResultsVo vo2 = list.get(j);
							vo.setIndex2(vo2.getIndex());
							vo.setCourseName2(vo2.getCourseName());
							vo.setIntegratedScore2(vo2.getIntegratedScore());
							removelist.add(vo2);
						}
						list.remove(removelist);
					}

				} else {//打印历年成绩
					jasper = "personalYearReportCard.jasper";
					param.put("school", schoolName + "成人高等学历教育学生历年学业成绩表");
					//待优化（此处逻辑每次大约耗时0.56秒，已优化补考成绩查询）
					//获取正考、补考、结补成绩
					if (list != null) {
						for (int j = 0; j < list.size(); j++) {
							StudentExamResultsVo studentExamResultsVo = list.get(j);
							Map<String, Object> condition = new HashMap<String, Object>(2);
							condition.put("studentid", studentInfo.getResourceid());
							condition.put("courseid", studentExamResultsVo.getCourseId());
							String integratedScoreStr = studentExamResultsService.getExamResult(condition);
							/*if("Y".equals(studentExamResultsVo.getIsdelayexam())){//缓考
								
							}*/
							//String integratedScoreStr = (String) studentScoreMap.get(studentInfo.getStudyNo()+"SN");
							if (!ExStringUtils.isEmpty(integratedScoreStr)) {
								if (!"30".equals(studentExamResultsVo.getExamResultsType())) {//如果为等级成绩则显示等级，不更改
									studentExamResultsVo.setIntegratedScore(integratedScoreStr);
								}
							}
							if ("11".equals(studentExamResultsVo.getCourseTypeCode())
									&& (studentExamResultsVo.getCourseName().contains("学位英语")
									|| studentExamResultsVo.getCourseName().contains("学位外语"))) {
								studentExamResultsVo.setCreditHour(null);
								studentExamResultsVo.setInCreditHour(null);
								studentExamResultsVo.setStydyHour(null);
							}
							//studentExamResultsVo.setBkScore(studentExamResultsService.getMaxBKExamResult(condition));
							//studentExamResultsVo.setJbScore(studentExamResultsService.getJBExamResult(condition));
						}
					}
				}
				reprotFile        				= URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						File.separator+"examPrint"+File.separator+jasper),"utf-8");
				
				if (null!=statVo) {
					param.put("totalCredit",statVo.getTotalCredit().toString());
					param.put("totalMajorCredit",statVo.getTotalMajorCredit().toString());
					param.put("requiredCredit",statVo.getRequiredCredit().toString());
					param.put("electiveCredit",statVo.getElectiveCredit().toString());
					param.put("avgScore",statVo.getAvgScore().toString());
				}else {
					param.put("totalCredit","");
					param.put("totalMajorCredit","");
					param.put("requiredCredit","");
					param.put("electiveCredit","");
					param.put("avgScore","");
				}
				String majorName       = (String) studentInfoMap.get(studentInfo.getResourceid()).get("majorName");

				String graduateDate = (String) studentInfoMap.get(studentInfo.getResourceid()).get("graduateDate2");
				if (ExStringUtils.isNotBlank(graduateDate)) {
					param.put("graduateDate",ExDateUtils.formatDateStr(ExDateUtils.parseDate(graduateDate,"yyyy-MM-dd"),2));
				}else {
					param.put("graduateDate","");
				}
				Date inDate = studentInfo.getInDate();//入学时间
				if (inDate!=null) {
					param.put("inDate",ExDateUtils.formatDateStr(inDate,2));
				}
				int indexA             = majorName.indexOf("A");
				int indexB             = majorName.indexOf("B");
				if (indexA>0||indexB>0) {
					majorName          = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
				}
				//专业是否需要替换字符
				if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)){
					String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
					String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
					for (int t = 0; t < listDictZYDM.size();t++) {
						String rpstr1 = ExStringUtils.trim(listDictZYDM.get(t).getDictName()+"").replace("（", "(").replace("）", ")");
						String rpstr2 = ExStringUtils.trim(listDictZYDM.get(t).getDictValue()+"").replace("（", "(").replace("）", ")");
						zymcReplaceArrayL[t] = rpstr1;
						zymcReplaceArrayR[t] = rpstr2;	
					}
					majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
				}
				
				param.put("stuNo",studentInfo.getStudyNo());
				param.put("stuName",studentInfo.getStudentName());
				param.put("majorName",majorName);
				param.put("studentKind", JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle",studentInfo.getLearningStyle()));
				param.put("schoolType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", studentInfo.getTeachingType()));

				param.put("xz", xz);
				param.put("gender", JstlCustomFunction.dictionaryCode2Value("CodeSex",(String) studentInfoMap.get(studentInfo.getResourceid()).get("gender")));
				param.put("eduYear",studentInfoMap.get(studentInfo.getResourceid()).get("eduYear"));
				param.put("brSchool",studentInfoMap.get(studentInfo.getResourceid()).get("unitShortName"));
				param.put("gradeYear",studentInfoMap.get(studentInfo.getResourceid()).get("gradeyear"));
				param.put("gradeName",studentInfoMap.get(studentInfo.getResourceid()).get("gradeName"));
				param.put("year",studentInfoMap.get(studentInfo.getResourceid()).get("gradeyear"));
				param.put("classicName",studentInfoMap.get(studentInfo.getResourceid()).get("classicName"));
				param.put("classesName",studentInfoMap.get(studentInfo.getResourceid()).get("classesname"));
				param.put("classCode",studentInfoMap.get(studentInfo.getResourceid()).get("classCode"));
				
				param.put("schoolConnectName",schoolConnectName);
				if ("1".equals(showpresident)) {
					param.put("president", schoolConnectName + "学院\n主管院长签名：");
				} else {
					param.put("president", "");
				}
				param.put("imageRootPath",rootPath+"common"+File.separator+"students");
				//param.put("imageRootPath","c:\\");
				
				param.put("printTime",printDate);
				param.put("scutLogoPath",scutLogoPath);
				
				// 不为空且为1时使用电子签名
				if(ExStringUtils.isNotEmpty(isUseElectronicalSign) && "1".equals(isUseElectronicalSign)){
					// 校本部
					if("01000".equals(studentInfo.getBranchSchool().getResourceid())) {
						if(signDean0 != null && ExStringUtils.isNotEmpty((String)param.get("president"))){
							signDean = signDean0.getSerName();
						}
						if(signAuditor0 != null){
							signAuditor = signAuditor0.getSerName();
						}
					}else {// 校外点
						if(signDean1 != null && ExStringUtils.isNotEmpty((String)param.get("president"))){
							signDean = signDean1.getSerName();
						}
						if(signAuditor1 != null){
							signAuditor = signAuditor1.getSerName();
						}
					}
				}
				param.put("signDeanPath", signRootPath+File.separator+signDean);
				param.put("signAuditorPath", signRootPath+File.separator+signAuditor);
				
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
				}
			}
			long end = Calendar.getInstance().getTimeInMillis();
			System.out.println("耗时："+(end-start)/1000+"秒");
			if (null!=jasperPrint) {
				if(ExStringUtils.isNotEmpty(isPdf)){
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					GUIDUtils.init();
					String filePath  = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRExporterParameter.CHARACTER_ENCODING,"UTF-8");
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
					exporter.exportReport(); 
					downloadFile(response,"学生成绩单.pdf",filePath,true);
				}else{
					renderStream(response, jasperPrint);
				}
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印学生所有课程成绩单出错：{}"+e.fillInStackTrace());
		}
	}
	
	/**
	 * 打印所有成绩单
	 * @param request
	 * @param response
	 * @param studentList
	 * @param printDate
	 */
	private void printPersonalAllReportCard(HttpServletRequest request, HttpServletResponse response, List<StudentInfo> studentList, String printDate, String printExam, String terms, String degreeUnitExam){
		
		Map<String, Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint   = null;//输出的报表
		//String [] ids 			  = studentId.split(",");
		User curUser              = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool        = false;
		String printPage          	= ExStringUtils.trimToEmpty(request.getParameter("printPage"));//哪种打印功能
		String passExam 			= request.getParameter("passExam");
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool            = true;
		}
		try {
			String jasper = "";
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			String scutLogoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
			SysConfiguration showpresident = CacheAppManager.getSysConfigurationByCode("printreport.showpresident");
			String rootPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
			jasper = "personalReportCard.jasper";
			schoolConnectName += "学院";
			if (null != showpresident && "1".equals(showpresident.getParamValue())) {
				param.put("president", schoolConnectName + "  学院主管院长签名：");
			} else {
				param.put("president", "");
			}
			if ("10601".equals(schoolCode)) {
				jasper = "personalReportCard_gly.jasper";
			} else if ("11846".equals(schoolCode)) {
				jasper = "personalReportCard_gdwy.jasper";
				schoolConnectName += "成人教育办公室";
;			}

			//专业是否需要替换字符
			List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
					, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
			
			Map<String, Map<String, Object>> studentInfoMap = studentInfoService.getStudentInfoMapByStuList(studentList);
			//for (int i = 0; i < ids.length; i++) {
			for (StudentInfo studentInfo : studentList) {
				//StudentInfo studentInfo = studentInfoService.get(ids[i]);
				Integer examCount = Integer.parseInt(studentInfoMap.get(studentInfo.getResourceid()).get("examcount").toString());
				List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(studentInfo,"print",examCount);//数据
				List<StudentExamResultsVo> removelist = new ArrayList<StudentExamResultsVo>();
				if (null != list && list.size() > 0) {
					List<String> courseids = new ArrayList<String>();
					StudentExamResultsVo examResultsVo=null;
					
					for (int j = 0; j < list.size(); j++) {
						StudentExamResultsVo result = list.get(j);
						// ‘打印全部成绩’
						if(courseids.contains(result.getCourseId())){//如果已经存在该课程则替换成绩
							//removelist.add(list.get(j-1));
						}
						// 桂林医免修免考成绩显示合格
						if ("免考".equals(result.getCheckStatus()) || "免修".equals(result.getCheckStatus())) {
							if("10601".equals(schoolCode)){
								result.setIntegratedScore("免合格");
							} else if(result.getIntegratedScore()!=null && !result.getIntegratedScore().contains("免")){
								result.setIntegratedScore("免" + result.getIntegratedScore());
							}
						}
						examResultsVo = result;
						courseids.add(result.getCourseId());
					}
				}
					
				StudentExamResultsVo statVo     = null;
				if (null!=list&&list.size()>0) {
					statVo     = list.get(list.size()-1);
					list.remove(list.size()-1);
				}
				if (isBrschool) {
					//删除未发布状态成绩
					List<StudentExamResultsVo> subList = new ArrayList<StudentExamResultsVo>();
					for (StudentExamResultsVo vo : list) {
						if (!"4".equals(ExStringUtils.trimToEmpty(vo.getCheckStatusCode()))) {
							subList.add(vo);
						}
						//只打印及格成绩
						if ("Y".equals(passExam)) {
							if (!"Y".equals(vo.getIsPass())) {
								removelist.add(vo);
							}
						}
					}
					list.removeAll(subList);
				}
				/*
				if(printPage.equals("gxykd")){// ‘打印全部成绩’，删除重复成绩
					list.removeAll(removelist);
				}*/
				list= filtrateList(list,terms,studentInfo,degreeUnitExam);
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
				
				String reprotFile     		          = "";
				reprotFile        				= URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
			  		    						  File.separator+"examPrint"+File.separator+jasper),"utf-8");
				if (null!=statVo) {
					param.put("totalCredit",ExStringUtils.toString(statVo.getTotalCredit()));
					param.put("totalMajorCredit",ExStringUtils.toString(statVo.getTotalMajorCredit()));
					param.put("requiredCredit",ExStringUtils.toString(statVo.getRequiredCredit()));
					param.put("electiveCredit",ExStringUtils.toString(statVo.getElectiveCredit()));
					param.put("avgScore",ExStringUtils.toString(statVo.getAvgScore()));
				}else {
					param.put("totalCredit","");
					param.put("totalMajorCredit","");
					param.put("requiredCredit","");
					param.put("electiveCredit","");
					param.put("avgScore","");
				}
				String majorName     = (String) studentInfoMap.get(studentInfo.getResourceid()).get("majorName");

				//GraduateData   graduateData=  graduateDataService.findByHql(studentInfo);

//				StudentMakeupList studentMakeupList = graduateDataService.findByHql1(studentInfo);
//				param.put("bkScore",studentMakeupList.getExamResults().getIntegratedScore());
				//Date graduateDate = null;//毕业时间
				//if(graduateData!=null) graduateDate = graduateData.getGraduateDate();
				String graduateDate = (String) studentInfoMap.get(studentInfo.getResourceid()).get("graduateDate2");
				if ("11846".equals(schoolCode)) {
					param.put("graduateDate",ExDateUtils.formatDateStr(studentInfo.getGrade().getGraduateDate(),2));
				}else if (ExStringUtils.isNotBlank(graduateDate)) {
					param.put("graduateDate",ExDateUtils.formatDateStr(ExDateUtils.parseDate(graduateDate,"yyyy-MM-dd"),2));
				}else {
					param.put("graduateDate","");
				}

				Date inDate = studentInfo.getInDate();//入学时间
				if (inDate!=null) {
					param.put("inDate",ExDateUtils.formatDateStr(inDate,2));
				}
				int indexA             = majorName.indexOf("A");
				int indexB             = majorName.indexOf("B");
				if (indexA>0||indexB>0) {
					majorName          = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
				}
				param.put("stuNo",studentInfo.getStudyNo());
				param.put("stuName",studentInfo.getStudentName());
				param.put("gender", JstlCustomFunction.dictionaryCode2Value("CodeSex",studentInfo.getStudentBaseInfo().getGender()));
				if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)){
					String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
					String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
					for (int t = 0; t < listDictZYDM.size();t++) {
						String rpstr1 = ExStringUtils.trim(listDictZYDM.get(t).getDictName()+"").replace("（", "(").replace("）", ")");
						String rpstr2 = ExStringUtils.trim(listDictZYDM.get(t).getDictValue()+"").replace("（", "(").replace("）", ")");
						zymcReplaceArrayL[t] = rpstr1;
						zymcReplaceArrayR[t] = rpstr2;	
					}
					majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
				}
				param.put("schoolType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", studentInfo.getTeachingType()));
				param.put("majorName",majorName);
				/*param.put("classicName",studentInfo.getClassic().getShortName());
				param.put("gradeName",studentInfo.getGrade().getGradeName());
				param.put("year",studentInfo.getGrade().getYearInfo().getFirstYear()+"");
				param.put("brSchool",studentInfo.getBranchSchool().getUnitShortName());*/
				//param.put("xz", studentInfoMap.get(studentInfo.getResourceid()).get("eduYear"));
				//param.put("gender", JstlCustomFunction.dictionaryCode2Value("CodeSex",(String) studentInfoMap.get(studentInfo.getResourceid()).get("gender")));
				param.put("eduYear",studentInfoMap.get(studentInfo.getResourceid()).get("eduYear")+" 年");
				param.put("brSchool",studentInfo.getBranchSchool().getUnitShortNameForCustom());
				param.put("gradeName",studentInfoMap.get(studentInfo.getResourceid()).get("gradeName"));
				param.put("year",studentInfoMap.get(studentInfo.getResourceid()).get("gradeyear"));
				param.put("classicName",studentInfoMap.get(studentInfo.getResourceid()).get("classicName"));
				//param.put("classesName",studentInfoMap.get(studentInfo.getResourceid()).get("classesname"));
				
				param.put("school", school);
				param.put("schoolConnectName", schoolConnectName);

				param.put("imageRootPath",rootPath+"common"+File.separator+"students");
				//param.put("imageRootPath","c:\\");
				if (ExStringUtils.isNotEmpty(printDate)) {
					param.put("printTime",ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),2));
				}else {
					param.put("printTime",ExDateUtils.formatDateStr(new Date(),2));
				}
				param.put("scutLogoPath", scutLogoPath);
				//param.put("scutLogoPath", "c:\\scut_logo.jpg");
				
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
				}
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印学生所有课程成绩单出错：{}"+e.fillInStackTrace());
			e.printStackTrace();
		}
	}
	
	/**
	 * 按班级打印成绩单
	 * @param request
	 * @param response
	 * @param classesid
	 * @param printDate
	 */
	private void printPersonalClassesReportCard(HttpServletRequest request, HttpServletResponse response, String classesid, String printDate){
		
		Map<String, Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint   = null;//输出的报表
		//String [] ids 			  = studentId.split(",");
		User curUser              = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool        = false;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool            = true;
		}
		try {
			String	printPage = request.getParameter("printPage"); 
			String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			String imageRootPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue();
			String scutLogoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();
			List<StudentInfo> listStu = studentInfoService.findByCriteria(Restrictions.eq("classes.resourceid", classesid));
			Map<String, Map<String, Object>> studentInfoMap = studentInfoService.getStudentInfoMapByStuList(listStu);
			if(null != listStu && listStu.size() > 0){		
				for (StudentInfo studentInfo : listStu) {
					//StudentInfo studentInfo = studentInfoService.get(id.);
					Integer examCount = Integer.parseInt(studentInfoMap.get(studentInfo.getResourceid()).get("examcount").toString());
					List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(studentInfo,examCount);
					StudentExamResultsVo statVo     = null;
					if (null!=list&&list.size()>0) {
						statVo     = list.get(list.size()-1);
						list.remove(list.size()-1);
					}
					if (isBrschool) {
						List<StudentExamResultsVo> subList = new ArrayList<StudentExamResultsVo>();
						for (StudentExamResultsVo vo : list) {
							if (!"4".equals(ExStringUtils.trimToEmpty(vo.getCheckStatusCode()))) {
								subList.add(vo);
							}
						}
						list.removeAll(subList);
					}
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
					String reprotFile     		          = "";
					reprotFile        				= URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
				  		    						  File.separator+"examPrint"+File.separator+"printExamResultsByClasses.jasper"),"utf-8");
					
//					if (null!=statVo) {
//						param.put("totalCredit",statVo.getTotalCredit().toString());
//						param.put("totalMajorCredit",statVo.getTotalMajorCredit().toString());
//						param.put("requiredCredit",statVo.getRequiredCredit().toString());
//						param.put("electiveCredit",statVo.getElectiveCredit().toString());
//						param.put("avgScore",statVo.getAvgScore().toString());
//					}
					String majorName       = (String) studentInfoMap.get(studentInfo.getResourceid()).get("majorName");
					
					int indexA             = majorName.indexOf("A");
					int indexB             = majorName.indexOf("B");
					if (indexA>0||indexB>0) {
						majorName          = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
					}
					param.put("stuNo",studentInfo.getStudyNo()); //学号
					param.put("majorName",majorName); //专业名称
					param.put("stuName",studentInfo.getStudentName());//姓名				
					/*param.put("classicName",studentInfo.getClassic().getShortName());//专业名称
					param.put("gradeName",studentInfo.getGrade().getGradeName());//年级名称
					param.put("schoolName",studentInfo.getBranchSchool().getUnitShortName());//教学点
					param.put("className",studentInfo.getClasses().getClassname());//班级名称
					param.put("xz",studentInfo.getTeachingPlan().getEduYear()+"年制"+JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle", studentInfo.getLearningStyle()));//学制 */	
					
					param.put("classicName",studentInfoMap.get(studentInfo.getResourceid()).get("classicShortName"));
					param.put("gradeName",studentInfoMap.get(studentInfo.getResourceid()).get("gradeName"));//年级名称
					param.put("schoolName",studentInfoMap.get(studentInfo.getResourceid()).get("unitShortName"));//教学点
					param.put("className",studentInfoMap.get(studentInfo.getResourceid()).get("classesName"));
					param.put("xz",studentInfoMap.get(studentInfo.getResourceid()).get("eduYear")+"年制"+
							JstlCustomFunction.dictionaryCode2Value("CodeLearningStyle",(String) studentInfoMap.get(studentInfo.getResourceid()).get("learningStyle")));
							
					param.put("school",schoolName + schoolConnectName);//打印学校名称
					param.put("imageRootPath",imageRootPath + "common"+File.separator+"students");//学校图标
					//param.put("imageRootPath","c:\\");
					if (ExStringUtils.isNotEmpty(printDate)) {//打印时间
						param.put("printTime",ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),"yyyy年MM月dd日"));
					}else {
						param.put("printTime",ExDateUtils.formatDateStr(new Date(),"yyyy年MM月dd日"));
					}
					param.put("scutLogoPath", scutLogoPath);//水印
					//param.put("scutLogoPath", "c:\\scut_logo.jpg");
					
					if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
						JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
						List jsperPageList=jasperPage.getPages();
						for (int j = 0; j < jsperPageList.size(); j++) {
							jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
						}
						jasperPage = null;//清除临时报表的内存占用
					}else {
						jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					}
				}
				if (null!=jasperPrint) {
					renderStream(response, jasperPrint);
				}else {
					renderHtml(response,"缺少打印数据！");
				}
			}else{
				renderHtml(response,"所选班级缺少学生数据！");
			}	
		} catch (Exception e) {
			logger.error("打印学生所有课程成绩单出错：{}"+e.fillInStackTrace());
		}	
	}
	
	 private JasperDesign dynamiccolumn(JasperDesign jdesign, Map params) {
	        /* 
	         * 该方法目前仅进行了简单的处理，如需更多业务，且自行添加 比如：1. 修改元素的位置 2.自动调整Title的宽度 
	         * 3.自行调整整个报表的宽度 
	         */  
	        Collection dynamiccolumns = (Collection) params.get("dynamiccolumn");  
	        if (dynamiccolumns != null) {  
	  
	            JRDesignBand cHeader = (JRDesignBand) jdesign.getColumnHeader();
	            JRBand cDetailBand = jdesign.getDetailSection().getBands()[0];
	            JRDesignBand cDetail = null;
	            if (cDetailBand != null && cDetailBand instanceof JRDesignBand) {
	                cDetail = (JRDesignBand) cDetailBand;
	            }  
	            JRElement[] es_header = cHeader.getElements();
	            JRElement[] es_detail = cDetail.getElements();
	            for (int i = 0; i < es_header.length; i++) {  
	                JRDesignElement e = (JRDesignElement) es_header[i];
	                String v = "";  
	                if (e instanceof JRStaticText) {
	                    JRStaticText text = (JRStaticText) e;
	                    v = text.getText();  
	                }  
	                if (!dynamiccolumns.contains(v)) {  
	                    for (int j = 0; j < es_detail.length; j++) {  
	                        JRDesignElement ee = (JRDesignElement) es_detail[i];
	                        if (ee.getY() == e.getY()) {  
	                            cDetail.removeElement(ee);  
	                        }  
	                    }  
	                    cHeader.removeElement(e);  
	                }  
	            }  
	        }  
	        return jdesign;  
	    }  

	
	/**
	 * 打印不及格及无分数成绩
	 * @param request
	 * @param response
	 * @param studentId
	 * @param printDate
	 */
	private void printPersonalFailReportCard(HttpServletRequest request, HttpServletResponse response, String studentId, String printDate, String studyTime){
		Map<String, Object> param 			      = new HashMap<String, Object>();
		List<Map<String,String>> dataList         = new ArrayList<Map<String,String>>();
		JasperPrint jasperPrint   			      = null;//输出的报表
		String edu_year						      = "2.5";
		String [] ids 							  = studentId.split(",");
		try {
			List<GraduateData> graduateDatas      = graduateDataService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.in("studentInfo.resourceid", ids));
			Map<String, GraduateData> graduateMap = new HashMap<String, GraduateData>();
			for (GraduateData gd :graduateDatas) {
				graduateMap.put(gd.getStudentInfo().getResourceid(), gd);
			}
			List<StudentInfo> studentInfos        = studentInfoService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.in("resourceid", ids));
			for (StudentInfo studentInfo:studentInfos) {
				
				String studyTimeStr                   = studyTime;//此处不能删除，用于在查看学生成绩界面中学时间可以由页面修改后传入
				dataList                              = getFailExamResults(studentInfo.getResourceid());
				JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
				
				String [] year 						  = studentInfo.getTeachingPlan().getEduYear().split("年");
				if (null!=year&&year.length>0) {
					edu_year   						  = year[0];
				}
				
				String jasperFile     				  = "";
				if ("1".equals(studentInfo.getClassic().getClassicCode())){
					   jasperFile                     = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
														File.separator+"examPrint"+File.separator+"personalPassReportCard_1.jasper"),"utf-8");
				}else {
					   jasperFile                     = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
							   							File.separator+"examPrint"+File.separator+"personalPassReportCard_2.jasper"),"utf-8");
				}
			
				String eduStartYear					  = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm(),"yyyy-MM");
				if ("2".equals(studentInfo.getGrade().getTerm())) {
					eduStartYear                      = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm(),"yyyy-MM");
				}
				if (ExStringUtils.isNotEmpty(studentInfo.getStudentStatus())&&"16".equals(studentInfo.getStudentStatus())&&
					null!=graduateMap.get(studentInfo.getResourceid())&&null!=graduateMap.get(studentInfo.getResourceid()).getGraduateDate()) {
					eduStartYear   += "至"+ExStringUtils.trimToEmpty(ExDateUtils.formatDateStr(graduateMap.get(studentInfo.getResourceid()).getGraduateDate(),"yyyy-MM"));
				}else {
					eduStartYear   += "至"+ExDateUtils.formatDateStr(new Date(), "yyyy-MM");
				}
				if (ExStringUtils.isBlank(studyTimeStr)) {
					studyTimeStr=eduStartYear;
				}
				param.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
				//param.put("imageRootPath","c:\\");
				StringBuffer titleInfo = new StringBuffer();
				String majorName       = studentInfo.getMajor().getMajorName();
				
				int indexA             = majorName.indexOf("A");
				int indexB             = majorName.indexOf("B");
				if (indexA>0||indexB>0) {
					majorName          = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
				}
				
				if (studentInfo.getMajor().getMajorName().length()>16 || studentInfo.getStudentName().length()>8 ) {
					titleInfo.append("姓名:"+studentInfo.getStudentName()+" 院系:继续教育学院"+" 专业:"+majorName);
					titleInfo.append(" 层次:"+studentInfo.getClassic().getShortName()+" 学习时间:"+studyTimeStr+" 学制:"+edu_year+" 学号:"+studentInfo.getStudyNo());
				}else {
					titleInfo.append("姓名:"+studentInfo.getStudentName()+"  院系:继续教育学院"+"  专业:"+majorName);
					titleInfo.append("  层次:"+studentInfo.getClassic().getShortName()+"  学习时间:"+studyTimeStr+"  学制:"+edu_year+"  学号:"+studentInfo.getStudyNo());
				}
				
				param.put("titleInfo",titleInfo.toString());
				param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				//param.put("scutLogoPath", "c:\\scut_logo.jpg");
//				StudentMakeupList studentMakeupList = graduatedataservice.findByHql1(studentInfo);
//				param.put("bkScore",studentMakeupList.getExamResults().getIntegratedScore());
				GraduateData graduateData=  graduateDataService.findByHql(studentInfo);
				Date graduateDate = null;//毕业时间
				if(graduateData!=null) {
					graduateDate = graduateData.getGraduateDate();
				}
				Date inDate = studentInfo.getInDate();//入学时间
				if (graduateDate!=null) {
					param.put("graduateDate",ExDateUtils.formatDateStr(graduateDate,"yyyy年MM月dd日"));
				}else{
					param.put("graduateDate","");
				}
				if (inDate!=null) {
					param.put("inDate",ExDateUtils.formatDateStr(inDate,"yyyy年MM月dd日"));
				}else{
					param.put("inDate","");
				}
				if (ExStringUtils.isNotEmpty(printDate)) {
					param.put("printDate",ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),"yyyy年MM月dd日"));
				}else {
					param.put("printDate",ExDateUtils.formatDateStr(new Date(),"yyyy年MM月dd日"));
				}
				
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
					JasperPrint jasperPage = JasperFillManager.fillReport(jasperFile, param, dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}else {
					jasperPrint = JasperFillManager.fillReport(jasperFile, param, dataSource); // 填充报表
				}
				
				//jasperPrint = JasperFillManager.fillReport(jasperFile, param, dataSource); // 填充报表
				
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印学生及格成绩单出错：{}"+e.fillInStackTrace());
		}
	}
	
	
	/**
	 * 打印及格成绩
	 * @param request
	 * @param response
	 * @param studentList
	 * @param printDate
	 * @param studyTime
	 */
	private void printPersonalPassReportCard(HttpServletRequest request, HttpServletResponse response, List<StudentInfo> studentList, String printDate, String studyTime){
		
		Map<String, Object> param 			      = new HashMap<String, Object>();
		List<Map<String,String>> dataList         = new ArrayList<Map<String,String>>();
		JasperPrint jasperPrint   			      = null;//输出的报表
		String edu_year						      = "2.5";
		//String [] ids 							  = studentId.split(",");
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		String school = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
		String printPage		= 	request.getParameter("printPage");
		try {
			
			//获取毕业日期-旧逻辑
			/*List<GraduateData> graduateDatas      = graduateDataService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.in("studentInfo.resourceid", ids.toArray()));
			Map<String, GraduateData> graduateMap = new HashMap<String, GraduateData>();
			for (GraduateData gd :graduateDatas) {
				graduateMap.put(gd.getStudentInfo().getResourceid(), gd);
			}*/
			//获取毕业日期-优化后
			
			Map<String, Map<String, Object>> graduateMap = studentInfoService.getStudentInfoMapByStuList(studentList);
			//List<StudentInfo> studentInfos        = studentInfoService.findByCriteria(Restrictions.eq("isDeleted",0),Restrictions.in("resourceid", ids));
			//是否需要把XYMC特殊处理
			List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
					, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
			for (StudentInfo studentInfo : studentList) {
				
				String studyTimeStr                   = studyTime;//此处不能删除，用于在查看学生成绩界面中学时间可以由页面修改后传入
				dataList                              = getPassExamResults(studentInfo);
				JRMapCollectionDataSource dataSource  = new JRMapCollectionDataSource(dataList);
				
				String  year 						  =null==studentInfo.getTeachingPlan()?"":studentInfo.getTeachingPlan().getEduYear().split("年")[0];
				if (null!=year&&year.length()>0) {
					edu_year   						  = year;
				}
				if (null != CacheAppManager.getSysConfigurationByCode("printreport.showpresident") && "1".equals(CacheAppManager.getSysConfigurationByCode("printreport.showpresident").getParamValue())) {
					param.put("president", "院长(签名)____________");
				} else {
					param.put("president", "");
				}
				String jasperFile  = File.separator+"reports"+File.separator+"examPrint"+File.separator;
				if("10560".equals(schoolCode)){//汕大个性化学生成绩模版
					//jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(jasperFile+"personalPassReportCard_stdx.jasper"),"utf-8");
					if ("1".equals(studentInfo.getClassic().getClassicCode())){
						jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(jasperFile+"personalPassReportCard_1_stdx.jasper"),"utf-8");
					}else {
						jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(jasperFile+"personalPassReportCard_2_stdx.jasper"),"utf-8");
					}
				}else if ("10601".equals(schoolCode)) {//桂林医成绩模版
					jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(jasperFile+"personalPassReportCard_gly.jasper"),"utf-8");
				}else {//默认使用的个人成绩模版
					if ("11846".equals(schoolCode)) {
						param.put("president", "");
					}
					if ("1".equals(studentInfo.getClassic().getClassicCode())){
						jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(jasperFile+"personalPassReportCard_1_gdwy.jasper"),"utf-8");
					}else {
						jasperFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(jasperFile+"personalPassReportCard_2_gdwy.jasper"),"utf-8");
					}
				}
			
				String eduStartYear					  = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOffirstTerm(),"yyyy-MM");
				if ("2".equals(studentInfo.getGrade().getTerm())) {
					eduStartYear                      = ExDateUtils.formatDateStr(studentInfo.getGrade().getYearInfo().getFirstMondayOfSecondTerm(),"yyyy-MM");
				}
				/*if ("16".equals(studentInfo.getStudentStatus())&&
					null!=graduateMap.get(studentInfo.getResourceid()) && null!=graduateMap.get(studentInfo.getResourceid()).getGraduateDate()) {
					eduStartYear   += "至"+ExStringUtils.trimToEmpty(ExDateUtils.formatDateStr(graduateMap.get(studentInfo.getResourceid()).getGraduateDate(),"yyyy-MM"));
				}else {
					eduStartYear   += "至"+ExDateUtils.formatDateStr(new Date(), "yyyy-MM");
				}*/
				eduStartYear   += "至"+graduateMap.get(studentInfo.getResourceid()).get("graduatedate1");
				
				//GraduateData   graduateData=  graduateDataService.findByHql(studentInfo);
				//Date graduateDate = null;//毕业时间
				//if(graduateData!=null) graduateDate = graduateData.getGraduateDate();
				String graduateData = (String) graduateMap.get(studentInfo.getResourceid()).get("graduatedate2");
				if (ExStringUtils.isNotBlank(graduateData)) {
					//param.put("graduateDate",ExDateUtils.formatDateStr(graduateDate,"yyyy年MM月dd日"));
					param.put("graduateDate",ExDateUtils.formatDateStr(ExDateUtils.parseDate(graduateData,"yyyy-MM-dd"),"yyyy年MM月dd日"));
				}else {
					param.put("graduateDate","");
				}
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());
				if (ExStringUtils.isBlank(studyTimeStr)) {
					studyTimeStr=eduStartYear;
				}
				param.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
				//param.put("imageRootPath","c:\\");
				StringBuffer titleInfo = new StringBuffer();
				String majorName       = studentInfo.getMajor().getMajorName();
				
				int indexA             = majorName.indexOf("A");
				int indexB             = majorName.indexOf("B");
				if (indexA>0||indexB>0) {
					majorName          = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
				}
				if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)){
					String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
					String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
					for (int i = 0; i < listDictZYDM.size(); i++) {
						String rpstr1 = ExStringUtils.trim(listDictZYDM.get(i).getDictName()+"").replace("（", "(").replace("）", ")");
						String rpstr2 = ExStringUtils.trim(listDictZYDM.get(i).getDictValue()+"").replace("（", "(").replace("）", ")");
						zymcReplaceArrayL[i] = rpstr1;
						zymcReplaceArrayR[i] = rpstr2;	
					}
					majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
				}

				titleInfo.append("姓名:"+studentInfo.getStudentName());
				if ("11846".equals(schoolCode)) {
					titleInfo.append(" 教学点:").append(studentInfo.getBranchSchool().getUnitShortNameForCustom());
				} else {
					titleInfo.append("  院系:继续教育学院");
				}
				titleInfo.append("  专业:"+majorName).append("  层次:"+studentInfo.getClassic().getShortName()+"  学习时间:"+studyTimeStr+"  学制:"+edu_year+"  学号:"+studentInfo.getStudyNo());
				
				param.put("titleInfo",titleInfo.toString());
				param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				//param.put("scutLogoPath", "c:\\scut_logo.jpg");
//				StudentMakeupList studentMakeupList = graduatedataservice.findByHql1(studentInfo);
//				param.put("bkScore",studentMakeupList.getExamResults().getIntegratedScore());
				Date inDate = studentInfo.getInDate();//入学时间
				if (inDate!=null) {
					param.put("inDate",ExDateUtils.formatDateStr(inDate,"yyyy年MM月dd日"));
				}
				if (ExStringUtils.isNotEmpty(printDate)) {
					param.put("printDate",ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),"yyyy年MM月dd日"));
				}else {
					param.put("printDate",ExDateUtils.formatDateStr(new Date(),"yyyy年MM月dd日"));
				}

				if (null!=jasperPrint&&jasperPrint.getPages().size()>0) {
					JasperPrint jasperPage = JasperFillManager.fillReport(jasperFile, param, dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}else {
					jasperPrint = JasperFillManager.fillReport(jasperFile, param, dataSource); // 填充报表
				}
				
				//jasperPrint = JasperFillManager.fillReport(jasperFile, param, dataSource); // 填充报表
				
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			logger.error("打印学生及格成绩单出错：{}"+e.fillInStackTrace());
		}
	}
	
	/**
	 * 获取不及格和无分数的成绩打印数据
	 * @param studentId
	 * @return
	 */
	private List<Map<String,String>> getFailExamResults(String studentId){
		
		Map<String, Object> param 				  = new HashMap<String, Object>();             //查询参数MAP
		Map<String,String> courseTerm             = new HashMap<String, String>();       	   //课程所在学期MAP
		Map<String, TeachingPlanCourse> tpcMap    = new HashMap<String, TeachingPlanCourse>(); //教学计划课程
		List<Map<String,String>> dataList         = new ArrayList<Map<String,String>>();       //返回通过的成绩数据
		List<Map<String, Object>> firstYearVo     = new ArrayList<Map<String, Object>>();      //第一年成绩
		List<Map<String, Object>> secondYearVo    = new ArrayList<Map<String, Object>>();      //第二年成绩
		List<Map<String, Object>> thirdYearVo     = new ArrayList<Map<String, Object>>();      //第三年成绩
		List<Map<String, Object>> fourthYearVo    = new ArrayList<Map<String, Object>>();      //第四年成绩
		List<Map<String, Object>> passResults     = new ArrayList<Map<String,Object>>();	   //所有通过的成绩
		int maxRecord                             = 19;												
		String edu_year						      = "2.5";
		
		StudentInfo studentInfo 		      	  = studentInfoService.get(studentId);
		
		//获取所有课程的所在学期
		for (TeachingPlanCourse pc:studentInfo.getTeachingPlan().getTeachingPlanCourses()) {
			courseTerm.put(pc.getCourse().getResourceid(), pc.getTerm());
			tpcMap.put(pc.getCourse().getResourceid(), pc);
		}
		//获取学制
		String [] year 						  = studentInfo.getTeachingPlan().getEduYear().split("年");
		if (null!=year&&year.length>0) {
			edu_year   						  = year[0];
		}
		param.put("studentId", studentInfo.getResourceid());
		param.put("printStatus", "fail");
		//学生的所有有效成绩列表
		List<Map<String, Object>>  results    = studentExamResultsService.studentPublishedExamResultsList(param);
		//获取同一科目最大的成绩
		results							      = studentExamResultsService.sortExamResultList(results);
		//统考、学位成绩
		List<Map<String,Object>> scl    	  = learningJDBCService.findStatCouseExamResults(studentInfo.getResourceid());
		//免修、免考成绩
		List<Map<String,Object>> ncl      	  = learningJDBCService.findNoExamCourseResults(studentInfo.getResourceid());
		
		//获取同一科目通过的成绩
		for (Map<String, Object> m :results) {
			
			String integratedscore 			  = null==m.get("INTEGRATEDSCORE")?"":(String)m.get("INTEGRATEDSCORE");
			String coursescoretype 			  = null==m.get("COURSESCORETYPE")?"":(String)m.get("COURSESCORETYPE");
			String stydyhour                  = null==m.get("STYDYHOUR")?"0":m.get("STYDYHOUR").toString();
			if (Integer.valueOf(stydyhour)<16) {
				m.put("STYDYHOUR", stydyhour+"周");
			}
			String score100        			  = studentExamResultsService.convertScore(coursescoretype,integratedscore);

			Double score100D       			  = 0.0;
			
			if (ExStringUtils.isNotEmpty(score100)) {
				 score100D         			  = Double.parseDouble(score100);
			}
			
			String isPass          			  = studentExamResultsService.isPassScore(coursescoretype, score100D);
			
			if (Constants.BOOLEAN_NO.equals(isPass)) {
				if (!Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(coursescoretype)) {
					integratedscore           = studentExamResultsService.convertScoreStr(coursescoretype, integratedscore);
				}
				m.put("INTEGRATEDSCORE", integratedscore);
				passResults.add(m);
			}

		}
		//将统考、学位成绩放入成绩列表,如果正常成绩中已有该课程的成绩，则移除
		for (Map<String,Object>statMap:scl) {
			Map<String, Object>  normalResults = null;
			for (Map<String, Object> m :results) {
				if (m.get("COURSEID").equals(statMap.get("COURSEID"))) {
					normalResults = m;
				}
			}
			if (null!=normalResults) {
				results.remove(normalResults);
			}
			String courseId       = statMap.get("COURSEID").toString();
			TeachingPlanCourse tpc= tpcMap.get(courseId);
			
			if (null!=tpc) {
				Map<String, Object> m = new HashMap<String, Object>();
				String credithour     = tpc.getCreditHour().toString();
				
				String scoreType      = statMap.get("SCORETYPE").toString();
				String interatedScore = "通过";
				if("0".equals(scoreType)) {
					interatedScore = "通过";
				}
				if("1".equals(scoreType)) {
					interatedScore = "免考";
				}
				if("2".equals(scoreType)) {
					interatedScore = "免修";
				}
				if("3".equals(scoreType)) {
					interatedScore = "代修";
				}
				
				m.put("COURSEID", 	 	 courseId);
				m.put("COURSENAME", 	 tpc.getCourse().getCourseName());
				m.put("INTEGRATEDSCORE",interatedScore );
				boolean isDouble      = ExStringUtils.containsAny(credithour,".".toCharArray());
				if ((isDouble&&ExStringUtils.endsWith(credithour, "0"))||isDouble==false) {//学分为.0的，或没有小数位的显示为整数
					m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().intValue()));
				}else {
					m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().doubleValue()));
				}
				m.put("STYDYHOUR",       String.valueOf(tpc.getStydyHour().intValue()));
				
				passResults.add(m);
			}
		}
		//将免修、免考成绩放入成绩列表,如果正常成绩中已有该课程的成绩，则移除
		for (Map<String,Object>noExamMap:ncl) {
			Map<String, Object>  normalResults = null;
			for (Map<String, Object> m :results) {
				if (m.get("COURSEID").equals(noExamMap.get("COURSEID"))) {
					normalResults = m;
				}
			}
			if (null!=normalResults) {
				results.remove(normalResults);
			}
			String courseId       = noExamMap.get("COURSEID").toString();
			TeachingPlanCourse tpc= tpcMap.get(courseId);
			String credithour     = tpc.getCreditHour().toString();
			if (null!=tpc) {
				Map<String, Object> m = new HashMap<String, Object>();
				
				m.put("COURSEID", 	 	 courseId);
				m.put("COURSENAME", 	 tpc.getCourse().getCourseName());
				m.put("INTEGRATEDSCORE", noExamMap.get("SCOREFORCOUNT") );
				
				boolean isDouble      = ExStringUtils.containsAny(credithour,".".toCharArray());
				if ((isDouble&&ExStringUtils.endsWith(credithour, "0"))||isDouble==false) {//学分为.0的，或没有小数位的显示为整数
					m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().intValue()));
				}else {
					m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().doubleValue()));
				}
				m.put("STYDYHOUR",       String.valueOf(tpc.getStydyHour().intValue()));
				
				passResults.add(m);
			}
		}
		
		//将不学期的课程成绩拆分到不同年份的LIST里面
		for(Map<String, Object> map :passResults){
			String term 					  = ExStringUtils.trimToEmpty(courseTerm.get(map.get("COURSEID")));
			
			if ("1".equals(term.trim())||"2".equals(term.trim())) {
				firstYearVo.add(map);
			}else if ("3".equals(term.trim())||"4".equals(term.trim())) {
				secondYearVo.add(map);
			}else if ("5".equals(term.trim())||"6".equals(term.trim())) {
				thirdYearVo.add(map);
			}else if ("7".equals(term.trim())||"8".equals(term.trim())) {
				fourthYearVo.add(map);
			}else { //选修课程归入最后一学年
				if ("2.5".equals(edu_year.trim())) {
					thirdYearVo.add(map);
				}else if("4".equals(edu_year.trim())) {
					fourthYearVo.add(map);
				}
			}
		}
		
		//获取最大行数
		if (firstYearVo.size() >maxRecord) {
			maxRecord  = firstYearVo.size();
		}
		if (secondYearVo.size()>maxRecord) {
			maxRecord  = secondYearVo.size();
		}
		if (thirdYearVo.size() >maxRecord) {
			maxRecord  = thirdYearVo.size();
		}
		if (fourthYearVo.size()>maxRecord) {
			maxRecord  = fourthYearVo.size();
		}
		
		//组合同一行，不同年份的数据
		for (int i=0;i<maxRecord;i++) {
			Map<String,String> row_data      = new HashMap<String, String>();
			
			String firstYearCourseName       = " ";
			String firstYearCreditHour  	 = " ";
			String firstYearStydyHour  		 = " ";
			String firstYearIntegratedScore  = " ";
			
			String secondYearCourseName  	 = " ";
			String secondYearCreditHour  	 = " ";
			String secondYearStydyHour  	 = " ";
			String secondYearIntegratedScore = " ";
			
			String thirdYearCourseName  	 = " ";
			String thirdYearCreditHour  	 = " ";
			String thirdYearStydyHour  	 	 = " ";
			String thirdYearIntegratedScore  = " ";
			
			String fourthYearCourseName  	 = " ";
			String fourthYearCreditHour  	 = " ";
			String fourthYearStydyHour  	 = " ";
			String fourthYearIntegratedScore = " ";
			
			if (firstYearVo.size()>=i) {
				if (firstYearVo.size()==i) {
					firstYearCourseName  	 ="以下空白";
				}else {
					Map<String,Object> fi_d  = firstYearVo.get(i);
					firstYearCourseName		 = null==fi_d.get("COURSENAME")?" ":fi_d.get("COURSENAME").toString();
					firstYearCreditHour 	 = null==fi_d.get("CREDITHOUR")?"0":fi_d.get("CREDITHOUR").toString();
					firstYearStydyHour 	 	 = null==fi_d.get("STYDYHOUR")?"0":fi_d.get("STYDYHOUR").toString();
					firstYearIntegratedScore = null==fi_d.get("INTEGRATEDSCORE")?"0":fi_d.get("INTEGRATEDSCORE").toString();
				}
			}
			if (secondYearVo.size()>=i) {
				if (secondYearVo.size()==i) {
					secondYearCourseName  	 ="以下空白";
				}else {
					Map<String,Object> se_d  = secondYearVo.get(i);
					secondYearCourseName  	 = null==se_d.get("COURSENAME")?" ":se_d.get("COURSENAME").toString();
					secondYearCreditHour  	 = null==se_d.get("CREDITHOUR")?"0":se_d.get("CREDITHOUR").toString();
					secondYearStydyHour  	 = null==se_d.get("STYDYHOUR")?"0":se_d.get("STYDYHOUR").toString();
					secondYearIntegratedScore= null==se_d.get("INTEGRATEDSCORE")?"0":se_d.get("INTEGRATEDSCORE").toString();
				}
			}
			if (thirdYearVo.size()>=i) {
				if (thirdYearVo.size()==i) {
					thirdYearCourseName  	 ="以下空白";
				}else {
					Map<String,Object> th_d  = thirdYearVo.get(i);
					thirdYearCourseName  	 = null==th_d.get("COURSENAME")?" ":th_d.get("COURSENAME").toString();
					thirdYearCreditHour  	 = null==th_d.get("CREDITHOUR")?"0":th_d.get("CREDITHOUR").toString();
					thirdYearStydyHour  	 = null==th_d.get("STYDYHOUR")?"0":th_d.get("STYDYHOUR").toString();
					thirdYearIntegratedScore = null==th_d.get("INTEGRATEDSCORE")?"0":th_d.get("INTEGRATEDSCORE").toString();
				}
			}
			if (fourthYearVo.size()>=i) {
				if (fourthYearVo.size()==i) {
					fourthYearCourseName  		 = "以下空白";
				}else {
					Map<String,Object> fh_d      = fourthYearVo.get(i);
					fourthYearCourseName  	     = null==fh_d.get("COURSENAME")?" ":fh_d.get("COURSENAME").toString();
					fourthYearCreditHour  	     = null==fh_d.get("CREDITHOUR")?"0":fh_d.get("CREDITHOUR").toString();
					fourthYearStydyHour  	     = null==fh_d.get("STYDYHOUR")?"0":fh_d.get("STYDYHOUR").toString();
					fourthYearIntegratedScore    = null==fh_d.get("INTEGRATEDSCORE")?"0":fh_d.get("INTEGRATEDSCORE").toString();
				}
			}
			
			row_data.put("firstYearCourseName", firstYearCourseName);
			row_data.put("firstYearCreditHour", firstYearCreditHour);
			row_data.put("firstYearStydyHour",firstYearStydyHour);
			row_data.put("firstYearIntegratedScore",firstYearIntegratedScore);
			
			row_data.put("secondYearCourseName", secondYearCourseName);
			row_data.put("secondYearCreditHour", secondYearCreditHour);
			row_data.put("secondYearStydyHour",secondYearStydyHour);
			row_data.put("secondYearIntegratedScore",secondYearIntegratedScore);
			
			row_data.put("thirdYearCourseName", thirdYearCourseName);
			row_data.put("thirdYearCreditHour", thirdYearCreditHour);
			row_data.put("thirdYearStydyHour",thirdYearStydyHour);
			row_data.put("thirdYearIntegratedScore",thirdYearIntegratedScore);
			
			row_data.put("fourthYearCourseName", fourthYearCourseName);
			row_data.put("fourthYearCreditHour", fourthYearCreditHour);
			row_data.put("fourthYearStydyHour",fourthYearStydyHour);
			row_data.put("fourthYearIntegratedScore",fourthYearIntegratedScore);
			
			dataList.add(row_data);
			
		}
		
		return dataList;
	} 
	
	/**
	 * 获取通过的成绩打印数据
	 * @param studentInfo
	 * @return
	 */
	private List<Map<String,String>> getPassExamResults(StudentInfo studentInfo){
		
		Map<String, Object> param 				  = new HashMap<String, Object>();             //查询参数MAP
		Map<String,String> courseTerm             = new HashMap<String, String>();       	   //课程所在学期MAP
		Map<String, TeachingPlanCourse> tpcMap    = new HashMap<String, TeachingPlanCourse>(); //教学计划课程
		List<Map<String,String>> dataList         = new ArrayList<Map<String,String>>();       //返回通过的成绩数据
		List<Map<String, Object>> firstYearVo     = new ArrayList<Map<String, Object>>();      //第一年成绩
		List<Map<String, Object>> secondYearVo    = new ArrayList<Map<String, Object>>();      //第二年成绩
		List<Map<String, Object>> thirdYearVo     = new ArrayList<Map<String, Object>>();      //第三年成绩
		List<Map<String, Object>> fourthYearVo    = new ArrayList<Map<String, Object>>();      //第四年成绩
		List<Map<String, Object>> passResults     = new ArrayList<Map<String,Object>>();	   //所有通过的成绩
		int maxRecord                             = 19;												
		String edu_year						      = "2.5";
		
		//StudentInfo studentInfo 		      	  = studentInfoService.get(studentId);
		
		if(null != studentInfo && null != studentInfo.getTeachingPlan()){
		
			//获取所有课程的所在学期
			for (TeachingPlanCourse pc:studentInfo.getTeachingPlan().getTeachingPlanCourses()) {
				courseTerm.put(pc.getCourse().getResourceid(), pc.getTerm());
				tpcMap.put(pc.getCourse().getResourceid(), pc);
			}
			//获取学制
			String  year 						  =null==studentInfo.getTeachingPlan()?"":studentInfo.getTeachingPlan().getEduYear().split("年")[0];
			if (null!=year&&year.length()>0) {
				edu_year   						  = year;
			}
			param.put("studentId", studentInfo.getResourceid());
			//学生的所有有效成绩列表
			List<Map<String, Object>>  results    = studentExamResultsService.studentPublishedExamResultsList(param);
			//获取同一科目最大的成绩
			results							      = studentExamResultsService.sortExamResultList(results);
			//统考、学位成绩
			List<Map<String,Object>> scl    	  = learningJDBCService.findStatCouseExamResults(studentInfo.getResourceid());
			//免修、免考成绩
			List<Map<String,Object>> ncl      	  = learningJDBCService.findNoExamCourseResults(studentInfo.getResourceid());
			
			//获取同一科目通过的成绩
			for (Map<String, Object> m :results) {
				
				String integratedscore 			  = null==m.get("INTEGRATEDSCORE")?"":(String)m.get("INTEGRATEDSCORE");
				String coursescoretype 			  = null==m.get("COURSESCORETYPE")?"":(String)m.get("COURSESCORETYPE");
				String stydyhour                  = null==m.get("STYDYHOUR")?"0":m.get("STYDYHOUR").toString();
				String examType = JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",m.get("EXAMTYPE")!=null?m.get("EXAMTYPE").toString():"");
				m.put("EXAMTYPE",examType);
				if (Integer.valueOf(stydyhour)<16) {
					m.put("STYDYHOUR", stydyhour+"周");
				}
				String score100        			  = studentExamResultsService.convertScore(coursescoretype,integratedscore);
	
				Double score100D       			  = 0.0;
				
				if (ExStringUtils.isNotEmpty(score100)) {
					 score100D         			  = Double.parseDouble(score100);
				}
				
				String isPass          			  = studentExamResultsService.isPassScore(coursescoretype, score100D);
				
				if (Constants.BOOLEAN_YES.equals(isPass)) {
					if (!Constants.COURSE_SCORE_TYPE_ONEHUNHRED.equals(coursescoretype)) {
						integratedscore           = studentExamResultsService.convertScoreStr(coursescoretype, integratedscore);
					}
					String scoreStyle = m.get("coursescoretype")==null?"":(String)m.get("coursescoretype");
					if("30".equals(scoreStyle)){//如果是等级成绩（分数转为字符）
						List<Dictionary> list_dict = CacheAppManager.getChildren("CodeScoreLevel");
						for (int i=0;i<list_dict.size();i++) {
							Dictionary dictionary = list_dict.get(i);
							int score = Integer.parseInt(dictionary.getDictValue());
							if(Integer.parseInt(integratedscore)>=score){
								integratedscore = dictionary.getDictName();
								i=list_dict.size();
							}
						}
					}
					m.put("INTEGRATEDSCORE", integratedscore);
					TeachingPlanCourse planCoruse = tpcMap.get(m.get("courseid").toString());
					if (planCoruse != null) {
						m.put("STYDYHOUR", planCoruse.getStydyHour());
						m.put("CREDITHOUR", planCoruse.getCreditHour());
						m.put("examClassType", JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", planCoruse.getExamClassType()));
					}
					passResults.add(m);
				}
	
			}
			//将统考、学位成绩放入成绩列表,如果正常成绩中已有该课程的成绩，则移除
			for (Map<String,Object>statMap:scl) {
				Map<String, Object>  normalResults = null;
				for (Map<String, Object> m :results) {
					if (m.get("COURSEID").equals(statMap.get("COURSEID"))) {
						normalResults = m;
					}
				}
				if (null!=normalResults) {
					results.remove(normalResults);
				}
				String courseId       = statMap.get("COURSEID").toString();
				TeachingPlanCourse tpc= tpcMap.get(courseId);
				
				if (null!=tpc) {
					Map<String, Object> m = new HashMap<String, Object>();
					String credithour     = tpc.getCreditHour().toString();
					
					String scoreType      = statMap.get("SCORETYPE").toString();
					String interatedScore = "通过";
					if("0".equals(scoreType)) {
						interatedScore = "通过";
					}
					if("1".equals(scoreType)) {
						interatedScore = "免考";
					}
					if("2".equals(scoreType)) {
						interatedScore = "免修";
					}
					if("3".equals(scoreType)) {
						interatedScore = "代修";
					}
					
					m.put("COURSEID", 	 	 courseId);
					m.put("COURSENAME", 	 tpc.getCourse().getCourseName());
					m.put("INTEGRATEDSCORE",interatedScore );
					boolean isDouble      = ExStringUtils.containsAny(credithour,".".toCharArray());
					if ((isDouble&&ExStringUtils.endsWith(credithour, "0"))||isDouble==false) {//学分为.0的，或没有小数位的显示为整数
						m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().intValue()));
					}else {
						m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().doubleValue()));
					}
					m.put("STYDYHOUR",       String.valueOf(tpc.getStydyHour().intValue()));
					
					passResults.add(m);
				}
			}
			//将免修、免考成绩放入成绩列表,如果正常成绩中已有该课程的成绩，则移除
			for (Map<String,Object>noExamMap:ncl) {
				Map<String, Object>  normalResults = null;
				for (Map<String, Object> m :results) {
					if (m.get("COURSEID").equals(noExamMap.get("COURSEID"))) {
						normalResults = m;
					}
				}
				if (null!=normalResults) {
					results.remove(normalResults);
				}
				String courseId       = noExamMap.get("COURSEID").toString();
				TeachingPlanCourse tpc= tpcMap.get(courseId);
				String credithour     = tpc.getCreditHour().toString();
				if (null!=tpc) {
					Map<String, Object> m = new HashMap<String, Object>();
					
					m.put("COURSEID", 	 	 courseId);
					m.put("COURSENAME", 	 tpc.getCourse().getCourseName());
					m.put("INTEGRATEDSCORE", "免"+noExamMap.get("SCOREFORCOUNT") );
					
					boolean isDouble      = ExStringUtils.containsAny(credithour,".".toCharArray());
					if ((isDouble&&ExStringUtils.endsWith(credithour, "0"))||isDouble==false) {//学分为.0的，或没有小数位的显示为整数
						m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().intValue()));
					}else {
						m.put("CREDITHOUR", 	 String.valueOf(tpc.getCreditHour().doubleValue()));
					}
					m.put("STYDYHOUR",       String.valueOf(tpc.getStydyHour().intValue()));
					
					passResults.add(m);
				}
			}
			
			//将不学期的课程成绩拆分到不同年份的LIST里面
			for(Map<String, Object> map :passResults){
				String term 					  = ExStringUtils.trimToEmpty(courseTerm.get(map.get("COURSEID")));
				
				if ("1".equals(term.trim())||"2".equals(term.trim())) {
					firstYearVo.add(map);
				}else if ("3".equals(term.trim())||"4".equals(term.trim())) {
					secondYearVo.add(map);
				}else if ("5".equals(term.trim())||"6".equals(term.trim())) {
					thirdYearVo.add(map);
				}else if ("7".equals(term.trim())||"8".equals(term.trim())) {
					fourthYearVo.add(map);
				}else { //选修课程归入最后一学年
					if ("2.5".equals(edu_year.trim()) || "3".equals(edu_year.trim())) {
						thirdYearVo.add(map);
					}else if("4".equals(edu_year.trim())) {
						fourthYearVo.add(map);
					}
				}
			}
			
			//获取最大行数
			if (firstYearVo.size() >maxRecord) {
				maxRecord  = firstYearVo.size();
			}
			if (secondYearVo.size()>maxRecord) {
				maxRecord  = secondYearVo.size();
			}
			if (thirdYearVo.size() >maxRecord) {
				maxRecord  = thirdYearVo.size();
			}
			if (fourthYearVo.size()>maxRecord) {
				maxRecord  = fourthYearVo.size();
			}
			
			//组合同一行，不同年份的数据
			for (int i=0;i<maxRecord;i++) {
				Map<String,String> row_data      = new HashMap<String, String>();
				
				String firstYearCourseName       = " ";
				String firstYearCreditHour  	 = " ";
				String firstYearStydyHour  		 = " ";
				String firstYearIntegratedScore  = " ";
				String firstType  = " ";
				
				String secondYearCourseName  	 = " ";
				String secondYearCreditHour  	 = " ";
				String secondYearStydyHour  	 = " ";
				String secondYearIntegratedScore = " ";
				String secondType  = " ";
				
				String thirdYearCourseName  	 = " ";
				String thirdYearCreditHour  	 = " ";
				String thirdYearStydyHour  	 	 = " ";
				String thirdYearIntegratedScore  = " ";
				String thirdType  = " ";
				
				String fourthYearCourseName  	 = " ";
				String fourthYearCreditHour  	 = " ";
				String fourthYearStydyHour  	 = " ";
				String fourthYearIntegratedScore = " ";
				String fourthType  = " ";
				
				if (firstYearVo.size()>=i) {
					if (firstYearVo.size()==i) {
						firstYearCourseName  	 ="以下空白";
					}else {
						Map<String,Object> fi_d  = firstYearVo.get(i);
						firstYearCourseName		 = null==fi_d.get("COURSENAME")?" ":fi_d.get("COURSENAME").toString();
						firstYearCreditHour 	 = null==fi_d.get("CREDITHOUR")?"0":fi_d.get("CREDITHOUR").toString();
						firstYearStydyHour 	 	 = null==fi_d.get("STYDYHOUR")?"0":fi_d.get("STYDYHOUR").toString();
						firstYearIntegratedScore = null==fi_d.get("INTEGRATEDSCORE")?"0":fi_d.get("INTEGRATEDSCORE").toString();
						firstType				 = null==fi_d.get("examClassType")?"":fi_d.get("examClassType").toString();
					}
				}
				if (secondYearVo.size()>=i) {
					if (secondYearVo.size()==i) {
						secondYearCourseName  	 ="以下空白";
					}else {
						Map<String,Object> se_d  = secondYearVo.get(i);
						secondYearCourseName  	 = null==se_d.get("COURSENAME")?" ":se_d.get("COURSENAME").toString();
						secondYearCreditHour  	 = null==se_d.get("CREDITHOUR")?"0":se_d.get("CREDITHOUR").toString();
						secondYearStydyHour  	 = null==se_d.get("STYDYHOUR")?"0":se_d.get("STYDYHOUR").toString();
						secondYearIntegratedScore= null==se_d.get("INTEGRATEDSCORE")?"0":se_d.get("INTEGRATEDSCORE").toString();
						secondType				 = null==se_d.get("examClassType")?"":se_d.get("examClassType").toString();
					}
				}
				if (thirdYearVo.size()>=i) {
					if (thirdYearVo.size()==i) {
						thirdYearCourseName  	 ="以下空白";
					}else {
						Map<String,Object> th_d  = thirdYearVo.get(i);
						thirdYearCourseName  	 = null==th_d.get("COURSENAME")?" ":th_d.get("COURSENAME").toString();
						thirdYearCreditHour  	 = null==th_d.get("CREDITHOUR")?"0":th_d.get("CREDITHOUR").toString();
						thirdYearStydyHour  	 = null==th_d.get("STYDYHOUR")?"0":th_d.get("STYDYHOUR").toString();
						thirdYearIntegratedScore = null==th_d.get("INTEGRATEDSCORE")?"0":th_d.get("INTEGRATEDSCORE").toString();
						thirdType				 = null==th_d.get("examClassType")?"":th_d.get("examClassType").toString();
					}
				}
				if (fourthYearVo.size()>=i) {
					if (fourthYearVo.size()==i) {
						fourthYearCourseName  		 = "以下空白";
					}else {
						Map<String,Object> fh_d      = fourthYearVo.get(i);
						fourthYearCourseName  	     = null==fh_d.get("COURSENAME")?" ":fh_d.get("COURSENAME").toString();
						fourthYearCreditHour  	     = null==fh_d.get("CREDITHOUR")?"0":fh_d.get("CREDITHOUR").toString();
						fourthYearStydyHour  	     = null==fh_d.get("STYDYHOUR")?"0":fh_d.get("STYDYHOUR").toString();
						fourthYearIntegratedScore    = null==fh_d.get("INTEGRATEDSCORE")?"0":fh_d.get("INTEGRATEDSCORE").toString();
						fourthType				     = null==fh_d.get("examClassType")?"":fh_d.get("examClassType").toString();
					}
				}
				
				row_data.put("firstYearCourseName", firstYearCourseName);
				row_data.put("firstYearCreditHour", firstYearCreditHour);
				row_data.put("firstYearStydyHour",firstYearStydyHour);
				row_data.put("firstYearIntegratedScore",firstYearIntegratedScore);
				row_data.put("firstType",firstType);
				row_data.put("secondType",secondType);
				row_data.put("thirdType",thirdType);
				row_data.put("fourthType",fourthType);
				
				row_data.put("secondYearCourseName", secondYearCourseName);
				row_data.put("secondYearCreditHour", secondYearCreditHour);
				row_data.put("secondYearStydyHour",secondYearStydyHour);
				row_data.put("secondYearIntegratedScore",secondYearIntegratedScore);
				
				row_data.put("thirdYearCourseName", thirdYearCourseName);
				row_data.put("thirdYearCreditHour", thirdYearCreditHour);
				row_data.put("thirdYearStydyHour",thirdYearStydyHour);
				row_data.put("thirdYearIntegratedScore",thirdYearIntegratedScore);
				
				row_data.put("fourthYearCourseName", fourthYearCourseName);
				row_data.put("fourthYearCreditHour", fourthYearCreditHour);
				row_data.put("fourthYearStydyHour",fourthYearStydyHour);
				row_data.put("fourthYearIntegratedScore",fourthYearIntegratedScore);
				
				dataList.add(row_data);
			}
		}else{
			
		}
		
		return dataList;
	} 
	
	
	
	/**
	 * 打印英文成绩单
	 * @param request
	 * @param response
	 * @param studentId
	 * @param printDate
	 */
	private void printPersonalEnReportCard(HttpServletRequest request, HttpServletResponse response, String studentId, String printDate){
		
		try {
			Map<String, Object> param 		= new HashMap<String, Object>();
			StudentInfo studentInfo 		= studentInfoService.get(studentId);
			List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(studentInfo,null);
			StudentExamResultsVo statVo     = list.get(list.size()-1);
			list.remove(list.size()-1);
			
			User curUser              		= SpringSecurityHelper.getCurrentUser();
			boolean isBrschool              = false;
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
				isBrschool                  = true;
			}
			if (isBrschool) {
				List<StudentExamResultsVo> subList = new ArrayList<StudentExamResultsVo>();
				for (StudentExamResultsVo vo : list) {
					if (!"4".equals(ExStringUtils.trimToEmpty(vo.getCheckStatusCode()))) {
						subList.add(vo);
					}
				}
				list.removeAll(subList);
			}
			for (TeachingPlanCourse pc:studentInfo.getTeachingPlan().getTeachingPlanCourses()) {
				param.put(pc.getTerm(), pc.getTerm());
			}
			int maxTerm                       	  = param.keySet().size();
			BigDecimal b1 = new BigDecimal(maxTerm);
			BigDecimal b2 = new BigDecimal("2");
			int eduYear                           = b1.divide(b2,0,BigDecimal.ROUND_UP).intValue();
			
			String namePY                   = ExStringUtils.isEmpty(studentInfo.getStudentBaseInfo().getNamePY())?"____________  ":studentInfo.getStudentBaseInfo().getNamePY();
			String majorName                = ExStringUtils.trimToEmpty(studentInfo.getMajor().getMajorEnName());
			String p_d 						= "Seal of the Academic Administration____________     Dean of School of Network Education(Signature)_____________       ";
			String stu_info                 = "Name："+namePY+"College:School of Network Education  Speciality："+majorName+"  Level：";
			String study_info               = "Studying Time："+" Schooling years："+eduYear+"years";
			
			if (ExStringUtils.isNotEmpty(printDate)) {
				p_d 						+= ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),"yyyy年MM月dd日");
			}
			GUIDUtils.init();
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			
			File disFile 	        = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//File disFile 	        = new File("c:\\personalCard.xls");
			//模板文件路径
			String templateFilePath = "personalEnReportCard.xls";

			Workbook wb 		    = null;
            WritableWorkbook wwb    = null;
            WritableSheet ws 	    = null;
            try {
                // 创建只读的Excel工作薄的对象
                wb = Workbook.getWorkbook(new File(templateFilePath));
                // copy上面的Excel工作薄,创建新的可写入的Excel工作薄对象
                wwb = Workbook.createWorkbook(disFile, wb);
                // 读取工作表--(注:工作表索引从0开始)
                ws  = wwb.getSheet(0);
                // 循环插入数据
                int colIndex       = 0;
                int cur_row        = 0;
                Double rowIndex    = new Double(0);
                //模板中各列下标 
                int a = 1;int b = 2; int c = 3 ; int d = 4 ;int e = 5 ;
                
                for (StudentExamResultsVo vo  :list) {
                	rowIndex      += 0.5d;
                	String []str   = rowIndex.toString().split("\\.");
                	colIndex      += 5;
                	if (colIndex>5) {
                		a += 5; b += 5; c += 5; d += 5; e += 5;
					}
                	if (!"0".equals(str[0])&&"5".equals(str[1])) {
					    a = 1;b = 2;c = 3 ;d = 4 ;e = 5 ;
					}
                	
                	String courseEnName         = ExStringUtils.trimToEmpty(vo.getCourseEnName());    //课程名(英文)
                	String stydyHour 		    = ExStringUtils.trimToEmpty(vo.getStydyHour());       //学时
                	String courseTerm           = ExStringUtils.trimToEmpty(vo.getCourseTerm());      //课程所在学期
                	String score                = ExStringUtils.trimToEmpty(vo.getIntegratedScore()); //综合成绩
                	String inCreditHour         = null==vo.getInCreditHour()?"0":vo.getInCreditHourStr(); //取得的学分
                	
                    WritableCell wc_courseName  = null;//课程名(英文) 单元格
                    WritableCell wc_stydyHour   = null;//学时                       单元格
                    WritableCell wc_courseTerm  = null;//课程所在学期   单元格
                    WritableCell wc_score       = null;//综合成绩             单元格
                    WritableCell wc_inCreditHour= null;//取得的学分        单元格
                    
	            	wc_courseName 		   		= ws.getWritableCell(a,cur_row+4);
	            	wc_courseTerm          		= ws.getWritableCell(b,cur_row+4);
	            	wc_stydyHour           		= ws.getWritableCell(c,cur_row+4);
	            	wc_inCreditHour        		= ws.getWritableCell(d,cur_row+4);
	            	wc_score               		= ws.getWritableCell(e,cur_row+4);
			
                   
                    //以第一行所有的列为模板
                    WritableCellFormat wcFormat = null;
                    if (wc_courseName.getCellFormat() != null) {
                        // 获得源单元格格式
                        wcFormat 				= new WritableCellFormat(wc_courseName.getCellFormat());
                    } else {
                        wcFormat 				= new WritableCellFormat();
                    }
                   
                	wc_courseName 			    = cloneCellWithValue(a,cur_row+4,courseEnName,wcFormat );
                	wc_courseTerm 			    = cloneCellWithValue(b,cur_row+4,courseTerm,wcFormat );
                	wc_stydyHour 			    = cloneCellWithValue(c,cur_row+4,stydyHour,wcFormat );
                	wc_inCreditHour 		    = cloneCellWithValue(d,cur_row+4,inCreditHour,wcFormat );
                	wc_score 				    = cloneCellWithValue(e,cur_row+4,score,wcFormat );
                	

                    ws.addCell(wc_courseName);
                    ws.addCell(wc_stydyHour);
                    ws.addCell(wc_courseTerm);
                    ws.addCell(wc_score);
                    ws.addCell(wc_inCreditHour);
                    
                	if ("0".equals(str[1])) {
                		cur_row   += 1;
					}
                }
                //获取第29列的样式
                WritableCell wc_templet         = ws.getWritableCell(1,29);
                WritableCellFormat wcFormat     = new WritableCellFormat(wc_templet.getCellFormat());
               
                WritableCell wc_stu_info        = ws.getWritableCell(1,1);
                wc_stu_info                     = cloneCellWithValue(1,1,stu_info,wcFormat );

                WritableCell wc_study_info      = ws.getWritableCell(1,2);
                wc_study_info                   = cloneCellWithValue(1,2,study_info,wcFormat );
                
                WritableCell wc_pd              = ws.getWritableCell(1,35);
                wc_pd                 		    = cloneCellWithValue(1,35,p_d,wcFormat );
                 
                ws.addCell(wc_stu_info);
                ws.addCell(wc_study_info);
                ws.addCell(wc_pd);
                
                // 写入Excel对象
                wwb.write();
                wwb.close();
                
                downloadFile(response,studentInfo.getStudentName()+"英文成绩单.xls",disFile.getAbsolutePath(),true);
            } catch (Exception e) {
              
            } finally {
                // 关闭可写入的Excel对象
                try {
                    if (null != wwb) {
						wwb.close();
					}
                } catch (Exception e) {
                      
                }
                // 关闭只读的Excel对象
                if (null != wb) {
					wb.close();
				}
            }
			
		} catch (Exception e) {
			logger.error("打印英文成绩单出错！{}"+e.fillInStackTrace());
			e.printStackTrace();
		} 
	}
   /**
    * 填充单元格	
    * @param col      列
    * @param row      行
    * @param value    写入的数据
    * @param wcFormat 单元格格式
    * @return
    */
   private WritableCell cloneCellWithValue(int col, int row, Object value, WritableCellFormat wcFormat) {
	   WritableCell wc = null;
	   // 判断数据是否为STRING类型，是用LABLE形式插入，否则用NUMBER形式插入
	   if(value == null){
           wc = new jxl.write.Blank(col, row, wcFormat);
	   } else if (value instanceof String) {
	       jxl.write.Label label = new jxl.write.Label(col, row, value.toString(), wcFormat);
	       wc = label;
	   }else {
	      wc = new jxl.write.Number(col, row, new Double(value.toString()).doubleValue(), wcFormat);
	   }
	   return wc;
   } 
   
   
   /**
	 * 期末考试成绩(复审)打印预览_统考网络课程
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresultsreview-print-view.html")
	public String printExamResultsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String examSubId      = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId     = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String printType      = ExStringUtils.trimToEmpty(request.getParameter("printType"));
		String branchSchool   = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String checkStatus 	  = ExStringUtils.trimToEmpty(request.getParameter("examResultsStatus"));
		String classesid  	   = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		if ("printReview".equals(printType)) {
			String hql        = " from "+ExamResultsAudit.class.getSimpleName()+" where isDeleted = 0 and examResults.examInfo.resourceid=? and examResults.studentInfo.classes.resourceid=? and auditType = 0 and (auditStatus = 0 or auditStatus is null) ";
			List<ExamResultsAudit> list = examResultsAuditService.findByHql(hql, examInfoId,classesid);
			if (list.isEmpty()) {
				model.addAttribute("msg","本课程没有需要打印的复审记录!");
			}
		}
		model.addAttribute("printType", printType);
		model.addAttribute("examSubId", examSubId);
		model.addAttribute("examInfoId", examInfoId);
		model.addAttribute("branchSchool",branchSchool);
		model.addAttribute("examResultsStatus",checkStatus);
		
		return "/edu3/teaching/examResult/examresultsreview-printview";
	}

	/**
	 * 期末成绩(复审)打印_网络课程
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/examresultsreview-print.html")
	public void examResultsPrint(HttpServletRequest request, HttpServletResponse response)throws WebException {
		
		Map<String,Object> map     = new HashMap<String, Object>();
		List<Map<String,Object>> dm= new ArrayList<Map<String,Object>>();
		String examSubId 		   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId 		   = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String teachType           = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String operatingType       = ExStringUtils.trim(request.getParameter("operatingType"));
		String printType  		   = ExStringUtils.trimToEmpty(request.getParameter("printType"));
		String branchSchool  	   = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String examResultsStatus   = ExStringUtils.trimToEmpty(request.getParameter("examResultsStatus"));
		String classesid  	   = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		
		User curUser               = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			branchSchool		   = curUser.getOrgUnit().getResourceid();
			
		}

		if (ExStringUtils.isNotEmpty(examSubId) && ExStringUtils.isNotBlank(examInfoId)) {
			
			JasperPrint jasperPrint= null;//输出的报表
			if (ExStringUtils.isBlank(teachType)) {
				teachType = "networkstudy";
			}
			try {
				ExamSub sub 	   = examSubService.get(examSubId);
				ExamInfo examInfo  = examInfoService.get(examInfoId);
				
				Double creditHour  = null;
				if ("printReview".equals(printType)||"export".equals(operatingType)) {
					String hql         = " from "+ExamResultsAudit.class.getSimpleName()+" audit where audit.isDeleted=0  and auditType = 0 and (auditStatus = 0 or auditStatus is null)";
					hql               += " and audit.examResults.examInfo.resourceid=? and audit.examResults.studentInfo.classes.resourceid=:classesid";
					hql  			  += " order by audit.examResults.studentInfo.branchSchool.unitCode asc,audit.examResults.studentInfo.studyNo asc ";
				
					
					List<ExamResultsAudit> list = examResultsAuditService.findByHql(hql, examInfoId,classesid);
					if (list.isEmpty()) {
						throw new WebException("本课程没有需要导出的复审记录!");
					}
			    	
			    	for (int i = 0; i < list.size(); i++) {
			    		ExamResultsAudit audit 	= list.get(i);
			    		Map<String,Object> m = new HashMap<String, Object>();
			    		m.put("sortNum",String.valueOf(i+1));
			    		m.put("unitName",audit.getExamResults().getStudentInfo().getBranchSchool().getUnitShortName());
			    		m.put("majorName",audit.getExamResults().getStudentInfo().getMajor().getMajorName());
			    		m.put("stuNo",audit.getExamResults().getStudentInfo().getStudyNo());
			    		m.put("stuName",audit.getExamResults().getStudentInfo().getStudentName());
			    		m.put("usuallyScore",ExStringUtils.trimToEmpty(audit.getExamResults().getTempusuallyScore_d()));
			    		
			    		if (ExStringUtils.isNotBlank(audit.getChangedExamAbnormity())&&!"0".equals(audit.getChangedExamAbnormity())) {
							m.put("writtenScore", JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", audit.getChangedExamAbnormity()));
							m.put("integratedScore", JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", audit.getChangedExamAbnormity()));
						}else {
							m.put("writtenScore", ExStringUtils.trimToEmpty(audit.getChangedWrittenScore()));
							m.put("integratedScore", ExStringUtils.trimToEmpty(audit.getChangedIntegratedScore()));
						}
			    		dm.add(m);
					}
				}else {			
					Map<String, Object> param = new HashMap<String, Object>();
					param.put("isDeleted", 0);
					param.put("examInfoId", examInfoId);
					String hql = " from "+ExamResults.class.getSimpleName()+" results where results.isDeleted=:isDeleted and results.examInfo.resourceid=:examInfoId ";
					if(ExStringUtils.isNotBlank(branchSchool)){
						hql += " and results.studentInfo.branchSchool.resourceid=:branchSchool ";
						param.put("branchSchool", branchSchool);
					}
					if (ExStringUtils.isNotBlank(examResultsStatus)) {
						hql += " and results.checkStatus=:examResultsStatus ";
						param.put("examResultsStatus", examResultsStatus);
					}
					hql += " order by results.studentInfo.branchSchool.unitCode,results.studentInfo.studyNo ";
					List<ExamResults> list = examResultsService.findByHql(hql, param);
					if (list != null && list.size() > 0) {
						creditHour = list.get(0).getCreditHour();
					}
					dm = examResultsService.getMapInfoListByExamResultListForPrint(list, null, "N");
				}

		    	JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dm);
		    	
				String reprotFile  ="";
				if ("export".equals(operatingType)||"exportAll".equals(operatingType)) {
					reprotFile     = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
							  		 File.separator+"examPrint"+File.separator+"courseExamResults_export.jasper"),"utf-8");
				}else {
					reprotFile     = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
							  		 File.separator+"examPrint"+File.separator+"courseExamResults.jasper"),"utf-8");
				}

				map.put("examSubId", sub.getResourceid());
				map.put("examInfoId", examInfoId);
				map.put("batchName", sub.getBatchName());//考试批次名称
				map.put("courseName", ExStringUtils.isEmpty(examInfo.getExamCourseCode())?"":examInfo.getExamCourseCode()+"--"+examInfo.getCourse().getCourseName());//课程名称
				map.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				String examType    = examInfo.getCourse().getExamType()!=null?JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType", examInfo.getCourse().getExamType().toString()):"";
				map.put("examType", examType);//考试形式
				String examDate    = "";
				try {
					if(examInfo.getExamStartTime()!=null){
						examDate = ExDateUtils.formatDateStr(examInfo.getExamStartTime(), "yyyy年MM月dd日");
					}
				} catch (ParseException e) {
				}
				map.put("examDate", examDate);//考试日期
				map.put("creditHour", creditHour!=null?creditHour.toString():"");
				map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				//map.put("scutLogoPath","c:\\scut_logo.jpg");
				
				jasperPrint 	 = JasperFillManager.fillReport(reprotFile, map, dataSource); // 填充报表
								
				if ("export".equals(operatingType)||"exportAll".equals(operatingType)) {
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					GUIDUtils.init();
					String filePath         =getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
					//String filePath         = "D:\\aa.xls";
					
					JRXlsExporter exporter  = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
					
					exporter.exportReport(); 
					try {
						//设置excel打印参数
						ExamPrintServiceImpl.setExcelPrintSetting(filePath);
					} catch (Exception e) {						
					}
					downloadFile(response,examInfo.getCourse().getCourseName()+"总评成绩.xls",filePath,true);
				}else {
					if (null!=jasperPrint) {
						renderStream(response, jasperPrint);
					}else {
						renderHtml(response,"缺少打印数据！");
					}
				}
			} catch (Exception e) {
				logger.error("打印期末成绩出错：{}"+e.fillInStackTrace());
				renderHtml(response,"<script>alert('"+e.getMessage()+"')</script>");
			} 
		}
	}
	
	
	  /**
	 * 期末考试成绩(复审)打印预览_面授、面授+网授课程
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresultsreviewForFaceCourse-print-view.html")
	public String printExamResultsForFaceCourseView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		
		String examSubId  	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String teachType     = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String courseId      = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid		 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  		 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic		 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String printType 	 = ExStringUtils.trimToEmpty(request.getParameter("printType"));
		String classesid  	   = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		//ExamInfo info        = null;
		Map<String,Object> condition = new HashMap<String, Object>();
		List<ExamResultsAudit> list = new ArrayList<ExamResultsAudit>();
		/*
		int examCourseType   	 = "facestudy".equals(teachType)?1:2;
		List<ExamInfo> infos 	 = examInfoService.findByCriteria(Restrictions.eq("course.resourceid",courseId),Restrictions.eq("examSub.resourceid",examSubId),Restrictions.eq("isDeleted",0),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
		if (null!=infos && !infos.isEmpty()) {
			 info            	 = infos.get(0);
			 condition.put("examInfoId", info.getResourceid());
		}*/
		
		if ("printReview".equals(printType)) {
			
			if (ExStringUtils.isNotBlank(examInfoId)) {
				condition.put("examInfoId", examInfoId);
				StringBuffer hql  = new StringBuffer();
				hql.append(" from "+ExamResultsAudit.class.getSimpleName()+" where isDeleted = 0 and examResults.examInfo.resourceid=:examInfoId  and auditType = 0 and (auditStatus = 0 or auditStatus is null) ");
				if (ExStringUtils.isNotEmpty(branchSchool)) {
					hql.append(" and examResults.studentInfo.branchSchool.resourceid=:branchSchool");
					condition.put("branchSchool", branchSchool);
				}
				if (ExStringUtils.isNotEmpty(gradeid)) {
					hql.append(" and examResults.studentInfo.grade.resourceid=:gradeid");
					condition.put("gradeid", gradeid);
				}
				if (ExStringUtils.isNotEmpty(major)) {
					hql.append(" and examResults.studentInfo.major.resourceid=:major");
					condition.put("major", major);
				}
				if (ExStringUtils.isNotEmpty(classic)) {
					hql.append(" and examResults.studentInfo.classic.resourceid=:classic");
					condition.put("classic", classic);	
				}
				if(ExStringUtils.isNotEmpty(classesid)){
					hql.append(" and examResults.studentInfo.classes.resourceid=:classesid");
					condition.put("classesid", classesid);
				}
				list =  examResultsAuditService.findByHql(hql.toString(),condition);
			}
			
			if (list.isEmpty()) {
				model.addAttribute("msg","本课程没有需要打印的复审记录!");
			}
		}

		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(teachType)) {
			condition.put("teachType", teachType);
		}
		if (ExStringUtils.isNotEmpty(printType)) {
			condition.put("printType", printType);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if (ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesid", classesid);
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		model.addAttribute("condition", condition);

		
		return "/edu3/teaching/examResult/examresultsreviewForFaceCourse-printview";
	}
	/**
	 * 期末成绩(复审)打印_面授、面授+网授课程
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/examresultsreviewForFaceCourse-print.html")
	public void examResultsPrintForFaceCourse(HttpServletRequest request, HttpServletResponse response)throws WebException {
		
		Map<String,Object> condition = new HashMap<String, Object>();
		Map<String,Object> param 	 = new HashMap<String, Object>();
		String examSubId  	 	     = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId            = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String teachType     	     = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
		String branchSchool  	     = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid		 	     = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  		 	     = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic		 	     = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String printType 			 = ExStringUtils.trimToEmpty(request.getParameter("printType"));
		String courseId              = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String classesid              = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(examInfoId)) {
			condition.put("examInfoId", examInfoId);
		}
		if (ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if (ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if (ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if (ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesid", classesid);
		}
		
		
		/*ExamInfo info        = null;
		int examCourseType   	 = "facestudy".equals(teachType)?1:2;
		List<ExamInfo> infos 	 = examInfoService.findByCriteria(Restrictions.eq("course.resourceid",courseId),Restrictions.eq("isDeleted",0),Restrictions.eq("examSub.resourceid",examSubId),Restrictions.eq("examCourseType",examCourseType),Restrictions.eq("isMachineExam",Constants.BOOLEAN_NO));
		if (null!=infos && !infos.isEmpty()) {
			 info            	 = infos.get(0);
			 examInfoId = info.getResourceid();
			 condition.put("examInfoId", info.getResourceid());
		}*/
		
		List<Map<String,Object>> dm= new ArrayList<Map<String,Object>>();
		if (ExStringUtils.isNotEmpty4All(examSubId,examInfoId)) {
			ExamInfo examInfo = examInfoService.get(examInfoId);
			String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
			String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			JasperPrint jasperPrint= null;//输出的报表
			try {
				ExamSub sub 	   = examSubService.get(examSubId);
				//ExamInfo examInfo  = examInfoService.get(examInfoId);		
				StudentInfo studentInfo_No1 = null;
				//condition.put("examInfoId", examInfo.getResourceid());
				StringBuffer hql  = new StringBuffer();
				hql.append(" from "+ExamResultsAudit.class.getSimpleName()+" where  isDeleted = 0 and examResults.examInfo.resourceid=:examInfoId  and auditType = 0 and (auditStatus = 0 or auditStatus is null) ");
				if (ExStringUtils.isNotEmpty(branchSchool)) {
					hql.append(" and examResults.studentInfo.branchSchool.resourceid=:branchSchool");
					condition.put("branchSchool", branchSchool);
				}
				if (ExStringUtils.isNotEmpty(gradeid)) {
					hql.append(" and examResults.studentInfo.grade.resourceid=:gradeid");
					condition.put("gradeid", gradeid);
				}
				if (ExStringUtils.isNotEmpty(major)) {
					hql.append(" and examResults.studentInfo.major.resourceid=:major");
					condition.put("major", major);
				}
				if (ExStringUtils.isNotEmpty(examSubId)) {
					hql.append(" and examResults.examInfo.examSub.resourceid=:examSubId");
					condition.put("examSubId", examSubId);
				}
				if (ExStringUtils.isNotEmpty(classic)) {
					hql.append(" and examResults.studentInfo.classic.resourceid=:classic");
					condition.put("classic", classic);
				}
				if(ExStringUtils.isNotEmpty(classesid)){
					hql.append(" and examResults.studentInfo.classes.resourceid=:classesid");
					condition.put("classesid", classesid);
				}
				if(ExStringUtils.isNotEmpty(courseId)){
					hql.append(" and examResults.examInfo.course.resourceid=:courseId and examResults.course.resourceid=:courseId");
					condition.put("courseId", courseId);
				}
				hql.append(" order by examResults.studentInfo.branchSchool.unitCode asc,examResults.studentInfo.studyNo asc ");
				Double creditHour = null; 
				//打印复审
				if ("printReview".equals(printType)||"export".equals(printType)) {
					
					List<ExamResultsAudit>list =  examResultsAuditService.findByHql(hql.toString(),condition);
					
					if (list.isEmpty()) {
						throw new WebException("本课程没有需要导出的复审记录!");
					}
			    	
			    	for (int i = 0; i < list.size(); i++) {
			    		ExamResultsAudit audit 	= list.get(i);
			    		Map<String,Object> m = new HashMap<String, Object>();
			    		m.put("sortNum",String.valueOf(i+1));
			    		m.put("unitName",audit.getExamResults().getStudentInfo().getBranchSchool().getUnitShortName());
			    		m.put("majorName",audit.getExamResults().getStudentInfo().getMajor().getMajorName());
			    		m.put("stuNo",audit.getExamResults().getStudentInfo().getStudyNo());
			    		m.put("stuName",audit.getExamResults().getStudentInfo().getStudentName());
			    		m.put("usuallyScore",ExStringUtils.trimToEmpty(audit.getExamResults().getUsuallyScore()));
			    		
			    		if (ExStringUtils.isNotBlank(audit.getChangedExamAbnormity())&&!"0".equals(audit.getChangedExamAbnormity())) {
							m.put("writtenScore", JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", audit.getChangedExamAbnormity()));
							m.put("integratedScore", JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", audit.getChangedExamAbnormity()));
						}else {
							m.put("writtenScore", ExStringUtils.trimToEmpty(audit.getChangedWrittenScore()));
							m.put("integratedScore", ExStringUtils.trimToEmpty(audit.getChangedIntegratedScore()));
						}
			    		dm.add(m);
			    		if(studentInfo_No1==null) {
							studentInfo_No1 = audit.getExamResults().getStudentInfo();
						}
					}
			    //打印全部	
				}else {
					condition.put("orderby", Constants.BOOLEAN_YES);
					List<ExamResults> list = examResultsService.queryExamResultsByCondition(condition);
					if(list!= null && list.size()>0){
						studentInfo_No1 = list.get(0).getStudentInfo();
						creditHour = list.get(0).getCreditHour();
						dm = examResultsService.getMapInfoListByExamResultListForPrint(list, null, "N");
					}
				}

				//成绩比例
				if(examInfo.getExamCourseType()==1){//面授
					param.put("usuallyScore",examInfo.getFacestudyScorePer2()+"");
					param.put("writtenScore",examInfo.getFacestudyScorePer()+"");
				}else {//网络
					param.put("usuallyScore",examInfo.getNetsidestudyScorePer()+"");
					param.put("writtenScore",examInfo.getStudyScorePer()+"");
				}
				
				String jasper = "";
		    	String reprotFile  ="";
		    	String title = schoolName;
		    	String term = sub.getTerm();
				Long year = sub.getYearInfo().getFirstYear();
		    	
		    	//改成某年某季学期非统考课程期末考试
		    	String batchName = year+"年"+("1".equals(term)?"春季":"秋季");
		    	if("10560".equals(schoolCode)){//汕大
					
					//改为班号
					Classes classes = classesService.get(classesid);
					if(classes!=null){
						param.put("planCourseCode", classes.getClassCode());
					}
					examResultsService.getExamResultMap_STDX(param,dm,classesid,"N");
					
					title += schoolConnectName+"学院学生考核成绩记载表";
		    		batchName += "学期";
		    		jasper = (printType.startsWith("export")?"courseExamResults_facestudy_stdx.jasper":"courseExamResults_facestudy_stdx.jasper");
				}else {//默认广大
					title += "成人高等教育考试";
		    		batchName += "课程期末考试";
		    		jasper = (printType.startsWith("export")?"courseExamResults_export.jasper":"courseExamResults.jasper");
				}

		    	String unitName    ="";
		    	String classicName ="";
		    	String gradeName   ="";
		    	String majorName   ="";
		    	String classesName ="";
		    	String teachingType = "";
		    	if(studentInfo_No1!=null){
		    		unitName=  studentInfo_No1.getBranchSchool().getUnitShortName();
		    		classicName = studentInfo_No1.getClassic().getClassicName();
		    		gradeName = studentInfo_No1.getGrade().getGradeName();
		    		majorName = studentInfo_No1.getMajor().getMajorName();
		    		classesName = studentInfo_No1.getClasses().getClassname();
		    		teachingType = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", studentInfo_No1.getTeachingType());
		    	}
		    	
		    	JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dm);
		    	reprotFile    = File.separator+"reports"+File.separator+"examPrint"+File.separator+jasper;
		    	File reprot_file   = new File(URLDecoder.decode(request.getSession().getServletContext().getRealPath(reprotFile),"utf-8"));
				
		    	param.put("examSubId", sub.getResourceid());
				param.put("examInfoId", examInfoId);
				param.put("batchName", batchName);//考试批次名称
				param.put("courseName", examInfo.getCourse().getCourseName());//课程名称
				String examType    = examInfo.getCourse().getExamType()!=null?JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType", examInfo.getCourse().getExamType().toString()):"";
				param.put("examType", examType);//考试形式
				String examDate    = "";
				try {
					if(examInfo.getExamStartTime()!=null){
						examDate = ExDateUtils.formatDateStr(examInfo.getExamStartTime(), "yyyy年MM月dd日");
					}
				} catch (ParseException e) {
				}
				param.put("examDate", examDate);//考试日期
				param.put("unit",unitName);
				param.put("grade",gradeName);
				param.put("major",majorName);
				param.put("classic",classicName);
				param.put("form",teachingType);
				param.put("className",classesName);
				param.put("classes",classesName);
				param.put("school",title);//学校名称
				param.put("courseHour",creditHour!=null?creditHour.toString():"");//学时
				param.put("creditHour", creditHour!=null?creditHour.toString():"");
				param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				
				jasperPrint 	 = JasperFillManager.fillReport(reprot_file.getPath(), param, dataSource); // 填充报表

				if ("export".equals(printType)||"exportAll".equals(printType)) {
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					GUIDUtils.init();
					String filePath         = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
					//String filePath         = "D:\\aa.xls";
					
					JRXlsExporter exporter  = new JRXlsExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
					exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
					exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
					exporter.exportReport(); 
					
					try {
						//设置excel打印参数
						ExamPrintServiceImpl.setExcelPrintSetting(filePath);
					} catch (Exception e) {						
					}
					
					downloadFile(response,examInfo.getCourse().getCourseName()+"总评成绩.xls",filePath,true);
				}else {
					if (null!=jasperPrint) {
						renderStream(response, jasperPrint);
					}else {
						renderHtml(response,"缺少打印数据！");
					}
				}				
			} catch (Exception e) {
				logger.error("打印期末成绩出错：{}"+e.fillInStackTrace());
				renderHtml(response,"<script>alert('"+e.getMessage()+"')</script>");
			} 
		}
	}
	/**
	 * 下载面授成绩单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/download.html")
	public void downLoadTranscriptsFacestudy(HttpServletRequest request , HttpServletResponse response){
		long st = System.currentTimeMillis();
		Map<String,Object> condition = new HashMap<String, Object>();
		//String guidePlanId = ExStringUtils.trimToEmpty(request.getParameter("guidePlanId"));	
		String gradeId    = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String classesId  = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		Date curTime       = new Date();
		User curUser       = SpringSecurityHelper.getCurrentUser();
		String classesname = ExStringUtils.getEncodeURIComponentByTwice(ExStringUtils.trimToEmpty(request.getParameter("classesname")));//班级名称

		//if (ExStringUtils.isNotEmpty(guidePlanId))  condition.put("guidePlanId", guidePlanId);
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId", teachingPlanCourseId);
		}
		if (ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}

		//if (ExStringUtils.isNotEmpty(guidePlanId)&&ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)) {				
//		if (ExStringUtils.isNotEmpty(gradeId)&&ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
		if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
	 	    try{
	 	    	ExamSub examSub = examSubService.get(examSubId);
	 	    	//TeachingGuidePlan guidePlan = teachingGuidePlanService.get(guidePlanId);
		 	    TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
		 	    Date endTime   = examSub.getEndTime();
		 	  
		 	    if (curTime.after(endTime)){	
		 	    	condition.put("courseId", planCourse.getCourse().getResourceid());
		 	    	condition.put("teachplanid", planCourse.getTeachingPlan().getResourceid());	
		 	    	condition.put("term", examSub.getTerm());
					condition.put("yearid", examSub.getYearInfo().getResourceid());
		 	    	if (ExStringUtils.isNotEmpty(gradeId)) {
						condition.put("gradeid", gradeId);
					}
		 	    	//condition.put("flag", "0");//导出空白成绩单
		 	    	if(curUser.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
		 	    		condition.put("branchschoolid", curUser.getOrgUnit().getResourceid());
		 	    	}
		 	    	condition.put("teachType", ExStringUtils.trimToEmpty(planCourse.getTeachType()));
//		 	    	condition.put("uncommit","1");
		 	    	List<ExamResultsVo> list  = teachingJDBCService.findFaceStudyExamResultsVo(condition);
			 	    long et_sql = System.currentTimeMillis();
			 	    logger.debug("查询花费时间："+(et_sql-st) +"ms");
			 	    setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			 	    GUIDUtils.init();
			 	    //导出
					File excelFile = null;
					File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
					String examType= "";
					if(null==planCourse.getCourse().getExamType()){
						examType   = "未知考试形式";
					}else {
						examType   = planCourse.getCourse().getExamType().toString();
					}
					
					Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
					
					//模板文件路径
					String templateFilepathString = "";
					templateMap.put("examSubName", examSub.getBatchName());
					templateMap.put("courseName",  "《"+planCourse.getCourse().getCourseCode()+"---"+planCourse.getCourse().getCourseName()+"》");
					templateMap.put("examType", JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",examType));
					templateMap.put("examDate", "");
					templateMap.put("classes", classesname);
					templateMap.put("courseId", planCourse.getCourse().getResourceid());
					templateMap.put("examSubId",examSub.getResourceid());
					templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
							CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
					
					templateFilepathString = "transcripts_3.xls";
					//初始化配置参数
					exportExcelService.initParmasByfile(disFile, "transcripts_3", list,condition);
					exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
					exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 8, templateMap);
					
					excelFile = exportExcelService.getExcelFile();//获取导出的文件
					logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
					
					String downloadFileName = classesname+(ExStringUtils.isNotBlank(planCourse.getCourse().getCourseName()) ? "《"+planCourse.getCourse().getCourseName()+"》":"")+"成绩单.xls";
					String downloadFilePath = excelFile.getAbsolutePath();
					long st_log = System.currentTimeMillis();
					ExamResultsLog log =  new ExamResultsLog();
					log.setOptionType(ExamResults.EXAMRESULTS_LOG_DOWNLOAD_TRANSCRIPTS);
					log.setExamSubId(examSubId);
					log.setFillinDate(new Date());
					log.setFillinMan(curUser.getUsername());
					log.setFillinManId(curUser.getResourceid());
					log.setAttachId(planCourse.getCourse().getCourseName());
					examResultsLogService.save(log);
					long et_log = System.currentTimeMillis();
					logger.debug("添加成绩日志花费时间："+(et_log-st_log) +"ms");
					downloadFile(response, downloadFileName,downloadFilePath,true);	
		 	   }else{
		 		  String msg     = "考试批次预约未结束，不允许录入成绩！\\r\\n预约结束时间："
			    				 + ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);
		 		  renderHtml(response, "<script>alert('"+msg+"')</script>");
		 	   }
			 }catch(Exception e){
				logger.error("导出excel文件出错："+e.fillInStackTrace());
		 		renderHtml(response, "<script>alert('导出excel文件出错:\\r\\n"+e.getMessage()+"')</script>");
			 }
	 	    long et = System.currentTimeMillis();
	 	   logger.debug("整个过程花费时间："+(et-st) +"ms");
		}else {
	 		renderHtml(response, "<script>alert('请输入合法的参数!')</script>");
		}
	}
	/**
	 * 面授成绩导入
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/upload-showpage.html")
	public String upLoadFacestudyTranscriptsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String,Object> condition = new HashMap<String, Object>();
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String classesId  = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String gradeId  = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String unitId  = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		User curUser       = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("curUser",curUser);

		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId", teachingPlanCourseId);
		}
		if (ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId", classesId);
		}
		String msg                   = "";
		Date curDate                 = new Date();
		String success              = Constants.BOOLEAN_NO;
		String isMachineExam = Constants.BOOLEAN_NO;
		if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			TeachingPlanCourse _planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			// 获取开课记录，获取课程的考试形式
			TeachingPlanCourseStatus courseStatus = teachingPlanCourseStatusService.findOneByCondition(gradeId,_planCourse.getTeachingPlan().getResourceid(),teachingPlanCourseId,unitId);
			if(courseStatus!=null){
				Integer examForm = courseStatus.getExamForm();
				if(examForm!=null && examForm==3){
					 isMachineExam = Constants.BOOLEAN_YES;
				}
			}
			ExamSub examSub 		 = examSubService.get(examSubId);
			if (null==examSub) {			
				msg = "没有找到对应的考试批次，不允许导入！";				
			}else if (curDate.getTime()< examSub.getEndTime().getTime()) {//考试批次预约未结束不能导入成绩
				msg = "考试批次预约未结束，不允许导入！";				
			} else {
				String hql                       = " from "+Attachs.class.getSimpleName()+" attach where attach.formId=? and attach.formType=?  and attach.isDeleted=0 ";
				//当前批次关联的未删除的附件
				List<Attachs> attachList         = attachsService.findByHql(hql, teachingPlanCourseId,ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				//当前批次上传附件时产生的日志记录
				List<ExamResultsLog> logList     = examResultsLogService.findExamResultsLogByExamSubIdAndOptionType(teachingPlanCourseId, ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				
				if (null!=logList && !logList.isEmpty() && null!=attachList && !attachList.isEmpty()){
					model.addAttribute("logList",logList);
					model.addAttribute("attachList",attachList);
				} 				
				ExamInfo examInfo = examInfoService.findExamInfoByCourseAndExamSubAndCourseType(_planCourse.getCourse().getResourceid(), examSubId,courseStatus.getTeachType());
				double facestudyscoreper = examSub.getFacestudyScorePer();
				double facestudyscoreper2 = examSub.getFacestudyScorePer2();
				if(examInfo!=null){
					if(examInfo.getExamCourseType()==0){
						facestudyscoreper = examInfo.getStudyScorePer();
						facestudyscoreper2 = examInfo.getNetsidestudyScorePer();
					}else if(examInfo.getExamCourseType()==1){
						facestudyscoreper = examInfo.getFacestudyScorePer();
						facestudyscoreper2 = examInfo.getFacestudyScorePer2();
					}
					model.addAttribute("examInfoId",examInfo.getResourceid());
					model.addAttribute("scoreper", "卷面成绩 ：平时成绩 = "+facestudyscoreper+" ："+facestudyscoreper2);
				}else {//if ("networkTeach".equals(courseStatus.getTeachType())) {
					facestudyscoreper = examSub.getWrittenScorePer();
					facestudyscoreper2 = examSub.getNetsidestudyScorePer();
					model.addAttribute("scoreper", "无考试信息！");
				}

				success              = Constants.BOOLEAN_YES;
			}			
		}else {//没有考试批次\课程不能导入
			msg =ExStringUtils.isEmpty(teachingPlanCourseId)?"请选择一个要导入成绩的考试批次！":"请选择一个要导入成绩的课程！";
		}
		condition.put("msg", msg);
		condition.put("success", success);
		condition.put("gradeId", gradeId);
		condition.put("unitId", unitId);
		condition.put("isMachineExam", isMachineExam);
		model.addAttribute("resultMap",condition);
		ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
		String isDefaultAbsent = cp.getProperty("isDefaultAbsent");
		model.addAttribute("isDefaultAbsent",isDefaultAbsent);
		return "/edu3/teaching/examResult/facestudyExamResultsUpload-view";
	}
	/**
	 * 核对面授成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/check-examresults.html")
	public void upLoadUncheckedFacestudyTranscripts(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap      = new HashMap<String, Object>();
		List<ExamResultsVo> resultList    = new ArrayList<ExamResultsVo>();
		boolean success                   = true;
 		String msg                        = "";
 		String excelCourseId              = "";
 		String excelExamSubId             = "";
 		String uploadExamResultFileId     = ExStringUtils.trimToEmpty(request.getParameter("uploadExamResultFileId"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String gradeId = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String unitId = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		try {
			ExamSub examSub               = examSubService.get(examSubId);
			TeachingPlanCourse planCourse                 = teachingPlanCourseService.get(teachingPlanCourseId);
			ExamInfo examInfo = examInfoService.get(examInfoId);
			String [] attachsIdArray      = uploadExamResultFileId.split("\\,");
			Course course                 = planCourse.getCourse();
			// 获取开课信息
			TeachingPlanCourseStatus courseStatus = teachingPlanCourseStatusService.findOneByCondition(gradeId,planCourse.getTeachingPlan().getResourceid(),planCourse.getResourceid(),unitId);
			String courseTeachType = "faceTeach";
			Integer examForm = 1;
			if(courseStatus!=null){
				courseTeachType = courseStatus.getTeachType();
				examForm = courseStatus.getExamForm();
			}
			Integer examCourseType = "faceTeach".equals(courseTeachType) ?1:0;
			resultMap.put("courseTeachType",courseTeachType);
			if (examInfo == null) {
				Map<String,Object> _condition = new HashMap<String, Object>();
				_condition.put("examSubId",examSubId);
				_condition.put("courseId",course.getResourceid());
				_condition.put("scoreStyle",planCourse.getScoreStyle());
				_condition.put("examCourseType",examCourseType);
				examInfo = examInfoService.getExamInfo(_condition);
			}
			//-----------------------当examSubId及uploadExamResultFileId都不为空时才进行核对成绩-----------------------
			if ( null!=examSub  && null != attachsIdArray) {
				String yearInfoId = examSub.getYearInfo().getResourceid();
				String term = examSub.getTerm();
				String courseId = course.getResourceid();
				String studntInfoId = null;
				// 获取成绩计算规则字典表
				List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
				Map<String, String> resultCalculateRuleMap = new HashMap<String, String>(0);
				for(Dictionary d : resultCalculateRuleList) {
					resultCalculateRuleMap.put(d.getDictCode(), d.getDictValue());
				}
				
				//允许一次上传多个成绩文件
				for (int i = 0; i < attachsIdArray.length; i++) {					
					Attachs attachs       = attachsService.get(attachsIdArray[i]);
					File excelFile            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
					String excelTileInfo  = ExamResultImportFileOperateUtil.getInstance().getExamResultsFileTitleInfo(excelFile);
					if ( null != excelTileInfo && excelTileInfo.indexOf("$") != -1) {
						String [] infos   = excelTileInfo.split("\\$");
						excelExamSubId    = infos[0];
						excelCourseId     = infos[1];
					}					
					if (!course.getResourceid().equals(excelCourseId)) {
						throw new WebException("请上传"+course.getCourseName()+"的成绩文件！");
					}					
					importExcelService.initParmas(excelFile, "transcripts_3",resultMap);
					importExcelService.getExcelToModel().setSheet(0);
					List modelList = importExcelService.getModelList();
							
					if (null!=modelList && !modelList.isEmpty()) {
						String writtenScore = null;
						ConfigPropertyUtil cp = ConfigPropertyUtil.getInstance();
						String isDefaultAbsent = cp.getProperty("isDefaultAbsent");
						for(int j = 0;j<modelList.size();j++){
							boolean isAbnormity = false;						
							ExamResultsVo vo        = (ExamResultsVo)modelList.get(j);
							String studyno = vo.getStudyNo();
							if(ExStringUtils.isNotEmpty(studyno)){
								StudentInfo student = studentInfoService.findUniqueByProperty("studyNo", studyno);
								if (student != null && student.getClasses() != null) {
									studntInfoId = student.getResourceid();
									String stuClassesid = student.getClasses().getResourceid();
									if(!classesId.equals(stuClassesid)){
										continue;
									}
								} else {
									continue;
								}
							}
							ExamResults examResults = examResultsService.findExamResulByExamSubAndStuNoAndCourseId(examSubId,vo.getStudyNo(),excelCourseId,classesId);
							
							String reg = "[0-9]+.?[0-9]*";// 用来验证分数是否是数值
							writtenScore = vo.getWrittenScore();
							vo.setExamAbnormity("0");
							if(ExStringUtils.isNotBlank(writtenScore) && !writtenScore.matches(reg)){
								isAbnormity = true;
								vo.setExamAbnormity(JstlCustomFunction.dictionaryCode2Name("CodeExamAbnormity", writtenScore));
							}
							
							if (null == examResults) {//没有找到成绩记录
								vo.setCourseId(excelCourseId);
								vo.setExamSubId(excelExamSubId);
								
							}else {
								if ("-1".equals(examResults.getCheckStatus())|| "0".equals(examResults.getCheckStatus())||ExStringUtils.isEmpty(examResults.getCheckStatus())) {
									vo.setCourseId(examResults.getExamInfo().getCourse().getResourceid());
									if (ExStringUtils.isNotEmpty(examResults.getExamAbnormity())&&!"0".equals(examResults.getExamAbnormity())) {
										String originalAbnormal = examResults.getExamAbnormity();
										if(!originalAbnormal.equals(vo.getExamAbnormity())){
											vo.setFlag(Constants.BOOLEAN_YES);
											vo.setMessage(originalAbnormal);
										}
										
//										vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", vo.getExamAbnormity()));
									}
									if (ExStringUtils.isNotEmpty(examResults.getWrittenScore())) {
										String originalWrittenScore = examResults.getWrittenScore();
										if(!originalWrittenScore.equals(vo.getWrittenScore())){
											vo.setFlag(Constants.BOOLEAN_YES);
											vo.setMessage("卷面:"+originalWrittenScore);
										}
										//vo.setWrittenScore(examResults.getWrittenScore());
									}
									if (ExStringUtils.isNotEmpty(examResults.getUsuallyScore())) {
										String originalUsuallyScore = examResults.getUsuallyScore();
										if("networkTeach".equals(courseTeachType)){// 网络
											vo.setUsuallyScore(ExStringUtils.trimToEmpty(examResults.getUsuallyScore()));
										}
										if(!originalUsuallyScore.equals(vo.getUsuallyScore())){
											vo.setFlag(Constants.BOOLEAN_YES);
											vo.setMessage(vo.getMessage()+"平时考核:"+originalUsuallyScore);
										}
										//vo.setWrittenScore(examResults.getWrittenScore());
									} 
									if (ExStringUtils.isNotEmpty(examResults.getOnlineScore())) {
//										if(!originalOnlineScore.equals(vo.getOnlineScore())){
//											vo.setFlag(Constants.BOOLEAN_YES);
//											vo.setMessage(vo.getMessage()+"网上学习:"+originalOnlineScore);
//										}
										vo.setOnlineScore( examResults.getOnlineScore());
									}
//									vo.setUsuallyScore(ExStringUtils.trimToEmpty(examResults.getUsuallyScore()));									
									vo.setExamResultsResourceId(examResults.getResourceid());
									vo.setCourseName(examResults.getExamInfo().getCourse().getCourseName());
									vo.setCourseId(excelCourseId);
									vo.setExamSubId(excelExamSubId);
								} else {
									vo.setCourseName(examResults.getExamInfo().getCourse().getCourseName());
									vo.setMemo(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", examResults.getCheckStatus()));
									vo.setWrittenScore(ExStringUtils.trimToEmpty(examResults.getWrittenScore()));
									vo.setUsuallyScore(ExStringUtils.trimToEmpty(examResults.getUsuallyScore()));
									if(ExStringUtils.isEmpty(examResults.getOnlineScore())){
										vo.setOnlineScore("0");
									}
									vo.setIntegratedScore(ExStringUtils.trimToEmpty(examResults.getIntegratedScore()));
									if(!"0".equals(examResults.getExamAbnormity())){
										vo.setIntegratedScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examResults.getExamAbnormity()));
									}
									resultList.add(vo);
									continue;
								}
							}
							vo.setMemo("");
							if("networkTeach".equals(courseTeachType)){// 网络
								// 网上学习成绩（作为平时成绩）
								if("networkTeach".equals(courseTeachType)){
									Map<String, Object> params = new HashMap<String, Object>();
									params.put("courseid", courseId);
									params.put("stuid", studntInfoId);
									params.put("yearid", yearInfoId);
									params.put("term", term);
									String score = teachingJDBCService.getUsualResultByStudent(params);
									vo.setUsuallyScore(ExStringUtils.trimToEmpty(score));
								}
							} else {
								vo.setUsuallyScore(ExStringUtils.trimToEmpty(vo.getUsuallyScore()));
							}
							if(ExStringUtils.isEmpty(vo.getOnlineScore())){
								vo.setOnlineScore("0");
							}
							vo.setOnlineScore(ExStringUtils.trimToEmpty(vo.getOnlineScore()));
							String resourceid = "";
							if (examResults != null) {
								resourceid = examResults.getResourceid();
							}
							//计算综合成绩
							//只有当成绩比例为100：0时，0的那列才允许为空							
							if(!isDefaultAbsent.equals("Y")){
								int faceOrNetwork = examInfo.getExamCourseType();
								double writePer=0L;
								double usualPer=0L; 
								if(faceOrNetwork==1){//面授
									writePer = examInfo.getFacestudyScorePer();
									usualPer = examInfo.getFacestudyScorePer2();
								}else{
									writePer = examInfo.getStudyScorePer();
									usualPer = examInfo.getNetsidestudyScorePer();
								}
								if(writePer!=0&&vo.getExamAbnormity().equals("0")){
									if(ExStringUtils.isBlank(vo.getWrittenScore())){
										success  						= false;
										msg     					    = "当前课程的卷面成绩比例不为0，学号："+vo.getStudyNo()+"的学生的卷面成绩不允许为空！请重新填写成绩表格后再上传。";
										break;
									}
								}
								if(usualPer!=0&&vo.getExamAbnormity().equals("0")){
									if(ExStringUtils.isBlank(vo.getUsuallyScore())){
										success  						= false;
										msg     					    = "当前课程的平时成绩比例不为0，学号："+vo.getStudyNo()+"的学生的平时成绩不允许为空！请重新填写成绩表格后再上传。";
										break;
									}
								}
							}
							cacluateIntegrateScore(resourceid,planCourse,examSub,examInfo
									,vo.getUsuallyScore(),vo.getWrittenScore(),vo.getOnlineScore(), vo, resultCalculateRuleMap,examForm,unitId);
							
							// 处理异常成绩
							if(isAbnormity){
								vo.setIntegratedScore(writtenScore);
							}
							resultList.add(vo);
						}
					}
					modelList           = null;//清理内存
				} 
				attachsIdArray 				    = null;//清理内存
				
			//-----------------------当examSubId及uploadExamResultFileId为空时不进行核对成绩-----------------------	
			}else{
				  success  						= false;
				  msg     					    = "缺少考试批次或没有成绩单！";
			}
		} catch (Exception e) {
			 msg            					= "读取上传的Excel文件出错或参数异常："+ e.getMessage();
			 success       						= false;
			 
			logger.error("导入未审核成绩单-核对成绩出错:{}",e.fillInStackTrace());
		}
		
		//上传的文件中没有数据
		if (resultList.isEmpty()) {
			success 					= false;
			if (ExStringUtils.isEmpty(msg)) {
				msg     					= "上传的文件中没有对应班级的数据,或当前批次没有对应的成绩记录！";
			}
			
		}else {//成功读取上传文件中的数据
			resultMap.put("examResultsVoList",resultList);
		}
		
		resultMap.put("success",success);
		resultMap.put("msg", msg);		
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	/**
	 * 计算综合成绩
	 * @param info
	 * @param usuallyScore
	 * @param writtenScore
	 * @param rs
	 * @return
	 */
	private String caculateIntegrateScoreForFaceStudy(ExamInfo info, String usuallyScore, String writtenScore, ExamResults rs){
		String integratedScoreStr = "";
		//是否混合机考课程
		boolean isMixTrue =false;
		if (null!=info&&null!=info.getIsmixture()) {
			isMixTrue           = null != info.getIsmixture() && Constants.BOOLEAN_YES.equals(info.getIsmixture());
		}
				
		Double wsp 	   		 	 = 60D; //卷面成绩分比例
		if(null!=info&&null!=info.getStudyScorePer()){
			wsp = info.getStudyScorePer();
		}
		Double usp      		 = 100-wsp;                                                  //平时分比例
	
		BigDecimal divisor 		 = new BigDecimal("1");
		BigDecimal hundredBig    = new BigDecimal(100);
		
		if(ExStringUtils.isEmpty(usuallyScore)){
			usuallyScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}
		String examAbnormity     = null!=rs?rs.getExamAbnormity():"0";
		
		//其它考试类型的课程卷面成绩等于录入的成绩
		
		BigDecimal wsBig         = new BigDecimal(writtenScore);
		BigDecimal usBig    	 = new BigDecimal(usuallyScore);
		
		BigDecimal wsPerBig      = new BigDecimal(wsp.toString());
		BigDecimal usPerBig      = new BigDecimal(usp.toString());
		
		BigDecimal wsPerRateBig  = wsPerBig.divide(hundredBig,2);		
		BigDecimal usPerRateBig  = usPerBig.divide(hundredBig,2);
	
		//成绩中成绩异常不为空时不设置卷面成绩、平时成绩、综合成绩						
		if (ExStringUtils.isNotEmpty(examAbnormity) && !Constants.EXAMRESULT_ABNORAMITY_0.equals(examAbnormity)){
				
		//成绩中成绩异常为空时设置卷面成绩、平时成绩、综合成绩	
		}else {
			
			//考试科目中平时成绩比例、卷面成绩比例不为空
			if (null!=wsp && null!=usp && wsp > 0&&usp>0){

				Double writtenScoreD = wsBig.multiply(wsPerRateBig).doubleValue();//根据批次比例算出的卷面成绩
				Double usuallyScoreD = usBig.multiply(usPerRateBig).doubleValue();//根据批次比例算出的平时成绩
				
				//当综合成绩小于卷面成绩时，综合成绩等于卷面成绩
//				if ( (writtenScoreD+usuallyScoreD) < Double.parseDouble(writtenScore)){ 
//					integratedScoreStr =(wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
//				//当综合成绩大于卷面成绩时,综合成绩计算方式为：卷面成绩*卷面成绩比例+平时成绩*平时成绩比例
//				}else{ 
					BigDecimal integratedScore = new BigDecimal(String.valueOf(writtenScoreD+usuallyScoreD));
					integratedScoreStr =(integratedScore.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();
//				}

			//考试科目中平时成绩比例、卷面成绩比例为空综合成绩等于卷面成绩	
			}else {
				integratedScoreStr = (wsBig.divide(divisor,0,BigDecimal.ROUND_HALF_UP)).toString();	
			}
		}	
		return integratedScoreStr;
	}
	/**
	 * 计算综合成绩
	 * @param resourceid
	 * @param planCourse
	 * @param examSub
	 * @param us
	 * @param ws
	 * @param os
	 * @param vo
	 * @param resultCalculateRuleMap
	 * @param examForm TeachingPlanCourseStatus:CodeExamForm
	 * @param schoolId
	 */
	private void cacluateIntegrateScore(String resourceid, TeachingPlanCourse planCourse, ExamSub examSub,ExamInfo info
			, String us, String ws, String os, ExamResultsVo vo, Map<String, String> resultCalculateRuleMap,Integer examForm, String schoolId) {
		Map<String,Object> data = new HashMap<String, Object>();
		//注意 没有考试记录的情况
		resourceid 		 = ExStringUtils.trimToEmpty(resourceid);
		Course course		= planCourse.getCourse();
		//ExamInfo info 	   = null;
		ExamResults rs 	   = null;
		double per1 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue());
		double per2 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue());
		double per3 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue());
		//ExamSub examSub = examSubService.get(examSubId);

		if(ExStringUtils.isNotEmpty(resourceid)){
			rs   = examResultsService.get(resourceid);
			rs.setExamAbnormity(vo.getExamAbnormity());
		}

		String usuallyScore      = ExStringUtils.trimToEmpty(us);
		String writtenScore      = ExStringUtils.trimToEmpty(ws);
		String onlineScore       = ExStringUtils.trimToEmpty(os);
		if(ExStringUtils.isEmpty(usuallyScore)){
			usuallyScore = "0";
		}
		if(ExStringUtils.isEmpty(writtenScore)){
			writtenScore = "0";
		}
		if(ExStringUtils.isEmpty(onlineScore)){
			onlineScore = "0";
		}
		
		Pattern pattern 			 = Pattern.compile("^((\\d|[123456789]\\d)(\\.\\d+)?|100|100(\\.0+))$");
		Matcher m = pattern.matcher(writtenScore);
		if (Boolean.FALSE == m.matches()) {
			writtenScore = String.valueOf(100) ;
			data.put("msg", (null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"卷面成绩应为0-100的数字</br>");
			//data.put("highest", 100);
		}
		m = pattern.matcher(usuallyScore);
		if (Boolean.FALSE == m.matches()) {
			usuallyScore = String.valueOf(100) ;
			data.put("msg", (data.containsKey("msg")?data.get("msg"):"")+(null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"平时考核成绩应为0-100的数字</br>");
			//data.put("highest1", 100);
		}
		m = pattern.matcher(onlineScore);
		if (Boolean.FALSE == m.matches()) {
			onlineScore = String.valueOf(100) ;
			data.put("msg", (data.containsKey("msg")?data.get("msg"):"")+(null!=rs?rs.getStudentInfo().getStudentName()+"的":"")+"网上学习成绩应为0-100的数字</br>");
			//data.put("highest2", 100);
		}
		String integratedScoreStr = examResultsService.caculateIntegrateScore(info,usuallyScore,writtenScore,onlineScore,rs,resultCalculateRuleMap);
		vo.setIntegratedScore(integratedScoreStr);
	}
	/**
	 * 保存导入的成绩
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/upload-process.html")
	public void upLoadUncheckedFacestudyTranscriptsProcess(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		int updateRecordCount         = 0;              //成功保存的成绩个数
		Double per 					  = 60D;
		List<String> saveList         = new ArrayList<String>();//保存成功的成绩记录LIST
		boolean success  			  = true;
		String msg                    = "";
		User user                     = SpringSecurityHelper.getCurrentUser();
		String courseTeachType = ExStringUtils.trimToEmpty(request.getParameter("courseTeachType"));
		String teachingPlanCourseId   = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String examInfoId = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String unitId  = ExStringUtils.trimToEmpty(request.getParameter("unitId"));
		String isMachineExam  = ExStringUtils.defaultIfEmpty(request.getParameter("isMachineExam"),Constants.BOOLEAN_NO);
		String [] studyNos      	  = request.getParameterValues("importExamresultsResourceid");//提交成绩的ID
		String examCountHQL     	  = genHQL("examCount",resultMap);                       //考试次数HQL
		try {
			double per1 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue());
			double per2 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue());
			double per3 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue());
			if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
				ExamSub examSub = examSubService.get(examSubId);
				ExamInfo examInfo = examInfoService.get(examInfoId);
		 	    TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);              //要导入成绩的考试课程
		 	    Course course = planCourse.getCourse();
		 	    
		 	    String teachType = planCourse.getTeachType();
		 	    if(ExStringUtils.isBlank(teachType)||"networkstudy".equals(teachType)){
		 		   throw new WebException("课程"+course.getCourseName()+"教学方式不是面授或网络面授!");
		 	    }
		 	    Integer examCourseType = "faceTeach".equals(courseTeachType)?1:0;
		 	   
		 	    Date curTime             = new Date();                                	 			    //当前时间
				Date endTime    		 = examSub.getEndTime();                 				   		 //考试批次成预约结束时间
				
				if (curTime.after(endTime)){
					if (examInfo == null) {
						Map<String, Object> _condition = new HashMap<String, Object>();
						if(Constants.BOOLEAN_YES.equals(isMachineExam)){
							examCourseType = 3;
							_condition.put("schoolId",unitId );
						}
						_condition.put("courseId", course.getResourceid());
						_condition.put("examSubId", examSubId);
						_condition.put("scoreStyle",planCourse.getScoreStyle());
						_condition.put("examCourseType", examCourseType);
						_condition.put("isMachineExam", isMachineExam);
						examInfo = examInfoService.getExamInfo(_condition);
					}

					// 获取各部分成绩所占比例
					if ("1".equals(examInfo.getExamCourseType().toString())
							&& null != examInfo.getFacestudyScorePer()) {//面授课程
						per1 = examInfo.getFacestudyScorePer();
						per2 = examInfo.getFacestudyScorePer2();

						if (null != examInfo.getFacestudyScorePer3()) {
							per3 = examInfo.getFacestudyScorePer3();
						} else if(null != examInfo.getExamSub().getFacestudyScorePer3()){
							per3 = examInfo.getExamSub().getFacestudyScorePer3();
						}else {
							per3 = 0.0;
						}
					} else {
						if (null != examInfo.getStudyScorePer()) {
							per2 = 100 - per1; // 平时分比例
							per3 = 0d;
						}
					}
					Double usuallyTopScore = per2;
					Double writtenTopScore = per1;
					// 判断成绩是否合法
					// 获取成绩计算规则字典表
					List<Dictionary> resultCalculateRuleList = CacheAppManager.getChildren("resultCalculateRule");
					for(Dictionary d : resultCalculateRuleList) {
						String rule = "";
						if(ExamResults.RESULTCALCULATERULE_USUALLYSCORE.equals(d.getDictCode())) {
							rule = d.getDictValue();
							if("2".equals(rule)){
								per2 = 100D;
							}
						} else if (ExamResults.RESULTCALCULATERULE_WRITTENSCORE.equals(d.getDictCode())){
							rule = d.getDictValue();
							if("2".equals(rule)){
								per1 = 100D;
							}
						}
					}
					int writtenScoreScale = 0;
					int usuallyScoreScale = 0;
					int integratedScoreScale = 0;
					List<Dictionary> dictionaries = CacheAppManager.getChildren("CodeExamresultsScale");
					for (Dictionary dictionary : dictionaries) {
						if(dictionary.getDictName().startsWith("卷面成绩")){
							writtenScoreScale = Integer.parseInt(dictionary.getDictValue());
						}else if (dictionary.getDictName().startsWith("平时成绩")) {
							usuallyScoreScale = Integer.parseInt(dictionary.getDictValue());
						}else if (dictionary.getDictName().startsWith("综合成绩")) {
							integratedScoreScale = Integer.parseInt(dictionary.getDictValue());
						}
					}
					for (int i = 0; i < studyNos.length; i++){
						ExamResults examResults = examResultsService.findExamResulByExamSubAndStuNoAndCourseId(examSubId,studyNos[i],course.getResourceid(),null);
						if(examResults==null){
							examResults = new ExamResults();
							examResults.setCourse(course);
							examResults.setExamInfo(examInfo);
							examResults.setMajorCourseId(teachingPlanCourseId);
							examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
							examResults.setCheckStatus("0");
							examResults.setExamsubId(examSubId);
							examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
							examResults.setCourseType(planCourse.getCourseType());													
							StudentInfo studentInfo = studentInfoService.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNos[i]);
							examResults.setStudentInfo(studentInfo);
							
							examResults.setCourseScoreType(examInfo.getCourseScoreType());
							examResults.setCreditHour(planCourse.getCreditHour());
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
						}else if (!examInfo.getResourceid().equals(examResults.getExamInfo().getResourceid())) {
							examResults.setExamInfo(examInfo);
						}
						examResults.setPlanCourseTeachType(planCourse.getTeachType());
						int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
						if (checkStatus>0) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
						}
						List examCountList     = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(), examResults.getStudentInfo().getResourceid());
						long examCount         = (Long)examCountList.get(0) ;     					 								 //选考次数
						if(ExStringUtils.isBlank(examResults.getResourceid())){//新生成成绩
							examCount++;
						}
						String writtenScore    = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+studyNos[i]));        //卷面成绩
						String usuallyScore    = ExStringUtils.trimToEmpty(request.getParameter("usuallyScore"+studyNos[i]));        //平时成绩
						String examAbnormity   = "";
							
						if("作弊".equals(writtenScore)){
							examAbnormity = "1";
						} else if("无卷".equals(writtenScore)){
							examAbnormity = "3";
						} else if("缺考".equals(writtenScore)){
							examAbnormity = "2";
						} else if("其它".equals(writtenScore)){
							examAbnormity = "4";
						}
						// 成绩类型
						if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩不能为空!");
						}
						
						// 卷面成绩
						if (ExStringUtils.isNotEmpty(examAbnormity)) {
							examResults.setExamAbnormity(examAbnormity);
							examResults.setWrittenScore("");
						}else {
							if (ExStringUtils.isEmpty(writtenScore)) {
								throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩不能为空!");
							}
							Pattern pattern 			 = Pattern.compile("^(\\d+|\\d+(\\.0+))$");
							if(writtenScoreScale!=0){
								pattern = Pattern.compile("([1-9]+[0-9]*|0)(\\.[\\d]+)?");
							}
							Matcher m = pattern.matcher(writtenScore);
							if(m.matches()==Boolean.FALSE){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩不合法!");
							}
							Double writtenScoreD  = Double.parseDouble(writtenScore);
							Double _writtenScore = writtenTopScore/(per1/100);
							if(writtenScoreD<0||writtenScoreD>_writtenScore){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩必须为0-"
										+_writtenScore.intValue()+"分!");
							}
							
							BigDecimal w_Score	 = new BigDecimal(writtenScore);
							if(writtenScore.contains(".")){
								examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,writtenScoreScale,BigDecimal.ROUND_HALF_UP)).toString());
							}else {
								examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}
							
							examResults.setExamAbnormity("0");
						}
						// 平时成绩
						if(ExStringUtils.isNotBlank(usuallyScore)){
							Double usuallyScoreD  = Double.parseDouble(usuallyScore);
							Double _usuallyScore = usuallyTopScore/(per2/100);
							if(usuallyScoreD<0||usuallyScoreD>_usuallyScore){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"平时成绩必须为0-"
										+_usuallyScore.intValue()+"分!");
							}							
							BigDecimal u_Score	 = new BigDecimal(usuallyScore);
							if(usuallyScore.contains(".")){
								examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,usuallyScoreScale,BigDecimal.ROUND_HALF_UP)).toString());
							}else {
								examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							}
							
						}
						examResults.setExamCount(examCount);
				 		examResults.setCheckStatus("0");
				 		examResults.setFillinMan(user.getCnName());
				 		examResults.setFillinManId(user.getResourceid());
				 		examResults.setFillinDate(curTime);

						examResultsService.saveOrUpdate(examResults);							
							
						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),teachingPlanCourseId);
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());	
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}						
						studentLearnPlanService.saveOrUpdate(learnPlan);
						updateRecordCount++;
						saveList.add(studyNos[i]);
					}
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.INSERT,"保存导入成绩：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
				}else {
					success = false;
					msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
			    		+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);
				}
			}else {
				success = false;
				msg 	= "找不到考试批次或考试课程,请与管理员联系！";
			}		
		} catch (Exception e) {
			logger.error("导入学生成绩异常:{}",e.fillInStackTrace());
			success = false;
			msg 	= "导入学生成绩异常:"+e.getMessage();

		}		
		
		resultMap.put("updateRecordCount",updateRecordCount);
		resultMap.put("saveList", saveList);
		resultMap.put("success",success);
		resultMap.put("msg", msg);
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	
	/**
	 * 下载面授成绩单(补考)
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/faildownload.html")
	public void faildownLoadTranscriptsFacestudy(HttpServletRequest request , HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();
		//String guidePlanId = ExStringUtils.trimToEmpty(request.getParameter("guidePlanId"));	
//		String gradeId    = ExStringUtils.trimToEmpty(request.getParameter("gradeId")); 
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
//		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));		
//		String classesId  = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String gradeid    = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classesid    = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String majorid    = ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String unitid    = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
		String courseid    = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String examSubId    = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		Date curTime       = new Date();
		User curUser       = SpringSecurityHelper.getCurrentUser();

		if (ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesId", classesid);
		}
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeId", gradeid);
		}
		if (ExStringUtils.isNotEmpty(majorid)) {
			condition.put("majorid", majorid);
		}
		if (ExStringUtils.isNotEmpty(unitid)) {
			condition.put("branchschoolid", unitid);
		}
		if (ExStringUtils.isNotEmpty(courseid)) {
			condition.put("courseid", courseid);
		}
		
		
		//当前补考批次
//		String examSubId  = "";	
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次
//		
//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}
		
		Course course = courseService.get(courseid);
		
		if (ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId", examSubId);
		}
		if (ExStringUtils.isNotEmpty(examSubId)&& ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
	 	    try{
	 	    	ExamSub examSub = examSubService.get(examSubId);
		 	    TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
		 	    Date endTime   = examSub.getEndTime();
		 	  
//		 	    if (curTime.after(endTime)){	
		 	    	condition.put("courseId", planCourse.getCourse().getResourceid());
		 	    	condition.put("teachplanid", planCourse.getTeachingPlan().getResourceid());		
		 	    	condition.put("gradeid", gradeid);	
		 	    	//condition.put("flag", "0");//导出空白成绩单
		 	    	if(curUser.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){
		 	    		condition.put("branchschoolid", curUser.getOrgUnit().getResourceid());
		 	    	}
//		 	    	condition.put("teachType", ExStringUtils.trimToEmpty(planCourse.getTeachType()));
		 	    	
//		 	    	condition.put("uncommit","1");//
		 	    	
		 	    	//======================================
		 	    	List<ExamResultsVo> list  = teachingJDBCService.findFailStudyExamResultsVo(condition);
			 	    
			 	    setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			 	    GUIDUtils.init();
			 	    //导出
					File excelFile = null;
					File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
					String examType= "";
					if(null==planCourse.getCourse().getExamType()){
						examType   = "未知考试形式";
					}else {
						examType   = planCourse.getCourse().getExamType().toString();
					}
				
					
					Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
					
					//模板文件路径
					String templateFilepathString = "";
					
					if(null != examSub){
						templateMap.put("examSubName", examSub.getBatchName());//这里改导出模版名称
					}
					templateMap.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());
					templateMap.put("courseName",  "《"+course.getCourseCode()+"---"+course.getCourseName()+"》");
					templateMap.put("examType", JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType",examType));
					templateMap.put("examDate", "");
					templateMap.put("courseId", course.getResourceid());
					templateMap.put("examSubId",examSub.getResourceid());
					templateMap.put("classesId",classesid);
					//templateMap.put("guidePlanId", guidePlanId);
					
					templateFilepathString = "transcripts_3_1.xls";
					//初始化配置参数
					exportExcelService.initParmasByfile(disFile, "transcripts_3_1", list,condition);
					exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
					exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 8, templateMap);
					
					excelFile = exportExcelService.getExcelFile();//获取导出的文件
					logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
					
//					String downloadFileName = planCourse.getCourse().getCourseName()+"成绩单.xls";
					String downloadFileName = course.getCourseName()+"成绩单.xls";
					String downloadFilePath = excelFile.getAbsolutePath();
					
					ExamResultsLog log =  new ExamResultsLog();
					log.setOptionType(ExamResults.EXAMRESULTS_LOG_DOWNLOAD_TRANSCRIPTS);
					log.setExamSubId(examSubId);
					log.setFillinDate(new Date());
					log.setFillinMan(curUser.getUsername());
					log.setFillinManId(curUser.getResourceid());
//					log.setAttachId(planCourse.getCourse().getCourseName());
					log.setAttachId(course.getCourseName());
					
					examResultsLogService.save(log);
					
					downloadFile(response, downloadFileName,downloadFilePath,true);	
//		 	   }else{
//		 		  String msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
//			    				 + ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);			
//		 		  renderHtml(response, "<script>alert('"+msg+"')</script>");
//		 	   }
			 }catch(Exception e){
				logger.error("导出excel文件出错："+e.fillInStackTrace());
		 		renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
			 }
		}else {
	 		renderHtml(response, "<script>alert('请输入合法的参数!')</script>");
		}
	}
	
	/**
	 * 面授成绩导入(补考)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/failupload-showpage.html")
	public String failupLoadFacestudyTranscriptsView(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		Map<String,Object> condition = new HashMap<String, Object>();
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String gradeid  = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classesid  = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String majorid  = ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String unitid  = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
		String courseid  = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		
		User curUser       = SpringSecurityHelper.getCurrentUser();
		model.addAttribute("curUser",curUser);

	
		if (ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeId", gradeid);
		}
		if (ExStringUtils.isNotEmpty(classesid)) {
			condition.put("classesId", classesid);
		}
		if (ExStringUtils.isNotEmpty(majorid)) {
			condition.put("majorid", majorid);
		}
		if (ExStringUtils.isNotEmpty(unitid)) {
			condition.put("branchschoolid", unitid);
		}
		if (ExStringUtils.isNotEmpty(courseid)) {
			condition.put("courseid", courseid);
		}
		if (ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
			condition.put("teachingPlanCourseId", teachingPlanCourseId);
		}
		
		String msg                   = "";
		Date curDate                 = new Date();
		String success              = Constants.BOOLEAN_NO;
		
		//要换成当前补考批次 
//		String examSubId="";
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("成绩录入考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次
//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		
		Course course = courseService.get(courseid);
		TeachingPlanCourse _planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
		String isMachineExam = Constants.BOOLEAN_NO;
		if (ExStringUtils.isNotEmpty(examSubId)&& null!=course && _planCourse!=null) {
			// 获取开课记录，获取课程的考试形式
			TeachingPlanCourseStatus courseStatus = teachingPlanCourseStatusService.findOneByCondition(gradeid,_planCourse.getTeachingPlan().getResourceid(),teachingPlanCourseId,unitid);
			if(courseStatus!=null){
				Integer examForm = courseStatus.getExamForm();
				if(examForm!=null && examForm==3){
					 isMachineExam = Constants.BOOLEAN_YES;
				}
			}
			ExamSub exmaSub 		 = examSubService.get(examSubId);
			if (null==exmaSub) {			
				msg = "没有找到对应的考试批次，不允许导入！";				
//			}else if (curDate.getTime()< exmaSub.getEndTime().getTime()) {//考试批次预约未结束不能导入成绩
//				msg = "考试批次预约未结束，不允许导入！";				
			} else {
				String hql                       = " from "+Attachs.class.getSimpleName()+" attach where attach.formId=? and attach.formType=?  and attach.isDeleted=0 ";
				//当前批次关联的未删除的附件
				List<Attachs> attachList         = attachsService.findByHql(hql, teachingPlanCourseId,ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				//当前批次上传附件时产生的日志记录
				List<ExamResultsLog> logList     = examResultsLogService.findExamResultsLogByExamSubIdAndOptionType(teachingPlanCourseId, ExamResults.EXAMRESULTS_FORM_TYPE_IMPORT_UNCHECK);
				
				if (null!=logList && !logList.isEmpty() && null!=attachList && !attachList.isEmpty()){
					model.addAttribute("logList",logList);
					model.addAttribute("attachList",attachList);
				} 				
				success              = Constants.BOOLEAN_YES;
			}			
		}else {//没有考试批次\课程不能导入
			msg =ExStringUtils.isEmpty(teachingPlanCourseId)?"请选择一个要导入成绩的考试批次！":"请选择一个要导入成绩的课程！";
		}
		condition.put("isMachineExam", isMachineExam);
		condition.put("schoolId", unitid);
		condition.put("msg", msg);
		condition.put("success", success);
		model.addAttribute("resultMap",condition);		
		return "/edu3/teaching/examResult/failfacestudyExamResultsUpload-view";
	}
	
	/**
	 * 核对面授成绩（补考）
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/failcheck-examresults.html")
	public void failupLoadUncheckedFacestudyTranscripts(HttpServletRequest request, HttpServletResponse response){
		Map<String,Object> resultMap      = new HashMap<String, Object>();
		List<ExamResultsVo> resultList    = new ArrayList<ExamResultsVo>();
		boolean success                   = true;
 		String msg                        = "";
 		String excelCourseId              = "";
 		String excelExamSubId             = "";
 		String uploadExamResultFileId     = ExStringUtils.trimToEmpty(request.getParameter("uploadExamResultFileId"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		
		//核对成绩时，补考成绩要从补考批次取
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次
//		
//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}
		
		try {
			ExamSub examSub               = examSubService.get(examSubId);
			TeachingPlanCourse planCourse                 = teachingPlanCourseService.get(teachingPlanCourseId);
			String [] attachsIdArray      = uploadExamResultFileId.split("\\,");
			Course course                 = planCourse.getCourse();
			//-----------------------当examSubId及uploadExamResultFileId都不为空时才进行核对成绩-----------------------
			if ( null!=examSub  && null != attachsIdArray) {				
				//允许一次上传多个成绩文件
				for (int i = 0; i < attachsIdArray.length; i++) {					
					Attachs attachs       = attachsService.get(attachsIdArray[i]);
					File excelFile            = new File(attachs.getSerPath()+File.separator+attachs.getSerName());
					String excelTileInfo  = ExamResultImportFileOperateUtil.getInstance().getExamResultsFileTitleInfo(excelFile);
					if ( null != excelTileInfo && excelTileInfo.indexOf("$") != -1) {
						String [] infos   = excelTileInfo.split("\\$");
						excelExamSubId    = infos[0];
						excelCourseId     = infos[1];
					}					
					if (!course.getResourceid().equals(excelCourseId)) {
						throw new WebException("请上传"+course.getCourseName()+"的成绩文件！");
					}					
					importExcelService.initParmas(excelFile, "transcripts_3_1",resultMap);
					importExcelService.getExcelToModel().setSheet(0);
					List modelList = importExcelService.getModelList();
							
					if (null!=modelList && !modelList.isEmpty()) {					
						for(int j = 0;j<modelList.size();j++){				
							ExamResultsVo vo        = (ExamResultsVo)modelList.get(j);
							String studyno = vo.getStudyNo();
							if(ExStringUtils.isNotEmpty(studyno)){
								String stuClassesid = studentInfoService.findUniqueByProperty("studyNo", studyno).getClasses().getResourceid();
								if(!classesId.equals(stuClassesid)){
									continue;
								}
							}
							String writtenScore = ExStringUtils.trimToEmpty(vo.getWrittenScore());
							ExamResults examResults = examResultsService.findExamResulByExamSubAndStuNoAndCourseId(examSubId,vo.getStudyNo(),excelCourseId,classesId);
							if (null == examResults) {//没有找到预约记录
								vo.setCourseId(excelCourseId);
								vo.setExamSubId(excelExamSubId);
								
//								vo.setExamSubId(examSubId);//========================
							}else {
								if ("-1".equals(examResults.getCheckStatus())|| "0".equals(examResults.getCheckStatus())
										||ExStringUtils.isEmpty(examResults.getCheckStatus()) || "1".equals(examResults.getCheckStatus())) {
//									vo.setCourseId(examResults.getExamInfo().getCourse().getResourceid());
									vo.setCourseId(examResults.getCourse().getResourceid());
									
									String examAbnormity   = "";
									
									if("作弊".equals(writtenScore)){
										examAbnormity = "1";
									}		
									if("缺考".equals(writtenScore)){
										examAbnormity = "2";
									}
									if("无卷".equals(writtenScore)){
										examAbnormity = "3";
									}
									if("其它".equals(writtenScore)){
										examAbnormity = "4";
									}
									if("缓考".equals(writtenScore)){
										examAbnormity = "5";
									}
									
									if (ExStringUtils.isNotEmpty(examAbnormity)) {
										vo.setExamAbnormity(examAbnormity);
										vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity));
										
										if (ExStringUtils.isNotEmpty(examResults.getExamAbnormity())&&!"0".equals(examResults.getExamAbnormity())) {
											String originalAbnormal = examResults.getExamAbnormity();
											if(!originalAbnormal.equals(vo.getExamAbnormity())){
												vo.setFlag(Constants.BOOLEAN_YES);
												vo.setMessage(originalAbnormal);
											}
//											vo.setWrittenScore(JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", vo.getExamAbnormity()));
										}
										
										if (ExStringUtils.isNotEmpty(examResults.getWrittenScore())) {
											String originalWrittenScore = examResults.getWrittenScore();
											
											if(!originalWrittenScore.equals(vo.getWrittenScore())){
												vo.setFlag(Constants.BOOLEAN_YES);
												vo.setMessage("卷面:"+originalWrittenScore);
											}
											//vo.setWrittenScore(examResults.getWrittenScore());
										}
									}
									else {
//										if (ExStringUtils.isEmpty(writtenScore)) {
//											throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩不能为空!");
//										}
//										Double writtenScoreD  = Double.parseDouble(writtenScore);
//										if(writtenScoreD<0||writtenScoreD>100){
//											throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩必须为0-100分!");
//										}
//										
//										BigDecimal w_Score	 = new BigDecimal(writtenScore);
//										examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
//										examResults.setUsuallyScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
//										examResults.setExamAbnormity("0");
										
										if (ExStringUtils.isNotEmpty(writtenScore)) {
											String originalWrittenScore = examResults.getWrittenScore();
											
											if(!writtenScore.equals(originalWrittenScore)){
												vo.setFlag(Constants.BOOLEAN_YES);
												examAbnormity = examResults.getExamAbnormity();
												
												if("1".equals(examAbnormity)){
													originalWrittenScore = "作弊";
												}								
												if("2".equals(examAbnormity)){
													originalWrittenScore = "缺考";
												}
												if("3".equals(examAbnormity)){
													originalWrittenScore = "无卷";
												}
												if("4".equals(examAbnormity)){
													originalWrittenScore = "其它";
												}
												if("5".equals(examAbnormity)){
													originalWrittenScore = "缓考";
												}
												vo.setMessage("卷面:"+originalWrittenScore);
											}
											//vo.setWrittenScore(examResults.getWrittenScore());
										}
									}
									
									vo.setExamResultsResourceId(examResults.getResourceid());
									vo.setCourseName(examResults.getCourse().getCourseName());
									vo.setCourseId(excelCourseId);
									vo.setExamSubId(excelExamSubId);
									vo.setMemo(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", examResults.getCheckStatus()));
//									vo.setExamSubId(examSubId);//=========================要设这个，下次取
									vo.setExamResultsResourceId(examResults.getResourceid());
								} else {
									
									vo.setCourseName(examResults.getExamInfo().getCourse().getCourseName());
									vo.setMemo(JstlCustomFunction.dictionaryCode2Value("CodeExamResultCheckStatus", examResults.getCheckStatus()));
									vo.setWrittenScore(ExStringUtils.trimToEmpty(examResults.getWrittenScore()));
									vo.setExamResultsResourceId(examResults.getResourceid());
									resultList.add(vo);
									continue;
								}
							}
							if(ExStringUtils.isEmpty(vo.getMemo())){
								vo.setMemo("");
							}
							
//							vo.setUsuallyScore(ExStringUtils.trimToEmpty(vo.getUsuallyScore()));
							resultList.add(vo);
						}
					}
					modelList           = null;//清理内存
				} 
				attachsIdArray 				    = null;//清理内存
				
			//-----------------------当examSubId及uploadExamResultFileId为空时不进行核对成绩-----------------------	
			}else{
				  success  						= false;
				  msg     					    = "缺少考试批次或没有成绩单！";
			}
		} catch (Exception e) {
			 msg            					= "读取上传的Excel文件出错或参数异常："+ e.getMessage();
			 success       						= false;
			 
			logger.error("导入未审核成绩单-核对成绩出错:{}",e.fillInStackTrace());
		}
		
		//上传的文件中没有数据
		if (resultList.isEmpty()) {
			success 					= false;
			if (ExStringUtils.isEmpty(msg)) {
				msg     					= "上传的文件中没有对应班级的数据,或当前批次没有对应的成绩记录！";
			}
			
		}else {//成功读取上传文件中的数据
			resultMap.put("examResultsVoList",resultList);
		}
		
		resultMap.put("success",success);
		resultMap.put("msg", msg);		
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	
	/**
	 * 保存导入的成绩(补考)=================================
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/transcripts/facestudy/failupload-process.html")
	public void failupLoadUncheckedFacestudyTranscriptsProcess(HttpServletRequest request, HttpServletResponse response) {
		Map<String,Object> resultMap  = new HashMap<String, Object>();
		int updateRecordCount         = 0;              //成功保存的成绩个数
		Double per 					  = 60D;
		List<String> saveList         = new ArrayList<String>();//保存成功的成绩记录LIST
		boolean success  			  = true;
		String msg                    = "";
		User user                     = SpringSecurityHelper.getCurrentUser();
		String teachingPlanCourseId   = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String schoolId  = ExStringUtils.trimToEmpty(request.getParameter("schoolId"));
		String isMachineExam  = ExStringUtils.defaultIfEmpty(request.getParameter("isMachineExam"),Constants.BOOLEAN_NO);
		String [] studyNos      	  = request.getParameterValues("importExamresultsResourceid");//提交成绩的ID
		String examCountHQL     	  = genHQL("examCount",resultMap);                       //考试次数HQL
		
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(batchName.length()>4 && !"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("考试批次:{}", batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次
//		
//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}
		
		try {
			if (ExStringUtils.isNotEmpty(examSubId)&&ExStringUtils.isNotEmpty(teachingPlanCourseId)) {
				ExamSub examSub = examSubService.get(examSubId);
		 	    TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);              //要导入成绩的考试课程
		 	    Course course = planCourse.getCourse();
		 	    
		 	    String teachType = planCourse.getTeachType();		 	    
		 	    if(ExStringUtils.isBlank(teachType)||"networkstudy".equals(teachType)){
		 		   throw new WebException("课程"+course.getCourseName()+"教学方式不是面授或网络面授!");
		 	    }
		 	    Date curTime             = new Date();                                	 			    //当前时间
				Date endTime    		 = examSub.getEndTime();                 				   		 //考试批次成预约结束时间
		 	    Integer examCourseType = "facestudy".equals(teachType)?1:2;
		 	    Map<String, Object> _condition = new HashMap<String, Object>();
	 	    	if(Constants.BOOLEAN_YES.equals(isMachineExam)){
	 	    		examCourseType = 3;
	 	    		_condition.put("schoolId",schoolId );
	 	    	}
		 	    ExamInfo examInfo 	   = null;
				_condition.put("courseId", course.getResourceid());
				_condition.put("examSubId", examSubId);
				_condition.put("examCourseType", examCourseType);
				_condition.put("isMachineExam", isMachineExam);
				List<ExamInfo> list	   = examInfoService.findExamInfoByCondition(_condition);
//				ExamInfo examInfo 	   = null;				
//				List<ExamInfo> list	   = examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
				if (null!=list && !list.isEmpty()) {
					examInfo = list.get(0);
				} else {
					examInfo = new ExamInfo();
					examInfo.setCourse(course);	
					examInfo.setIsOutplanCourse("0");
					examInfo.setExamSub(examSub);					
					examInfo.setExamCourseType(examCourseType);
					examInfo.setIsMachineExam(isMachineExam);

					double per1 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue());
					double per2 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue());
					double per3 = Double.parseDouble(CacheAppManager.getSysConfigurationByCode("facestudyScorePer3").getParamValue());
					if (1==examCourseType) {
						if (null != examSub.getWrittenScorePer()) {
							per = examSub.getWrittenScorePer();
						}
						if (null != examSub.getFacestudyScorePer()) {
							per1 = examSub.getFacestudyScorePer();
						}
						if (null != examSub.getFacestudyScorePer2()) {
							per2 = examSub.getFacestudyScorePer2();
						}
						if (null != examSub.getFacestudyScorePer3()) {
							per3 = examSub.getFacestudyScorePer3();
						}
						examInfo.setStudyScorePer(per);
						examInfo.setFacestudyScorePer(per1);
						examInfo.setFacestudyScorePer2(per2);
						examInfo.setFacestudyScorePer3(per3);
					}else if(2!=examCourseType) {
						if (null==examSub.getNetsidestudyScorePer()) {
							per=examSub.getNetsidestudyScorePer();
						}
						examInfo.setStudyScorePer(per);
					} else {
						if (null != examSub.getWrittenScorePer()) {
							per = examSub.getWrittenScorePer();
							per1 = examSub.getWrittenScorePer();
							per2 = 100 - per1;
							per3 = 0d;
						}
						examInfo.setStudyScorePer(per);
						examInfo.setFacestudyScorePer(per1);
						examInfo.setFacestudyScorePer2(per2);
						examInfo.setFacestudyScorePer3(per3);
					}
					if(planCourse.getScoreStyle()!=null){
						examInfo.setCourseScoreType(planCourse.getScoreStyle());
					}else {
						examInfo.setCourseScoreType(examSub.getCourseScoreType());
					}
					//examInfo.setCourseScoreType(examSub.getCourseScoreType());
					
					examInfoService.save(examInfo);				
				}
				
//				if (curTime.after(endTime)){	
//					ExamInfo examInfo = null;				
//					List<ExamInfo> list= examInfoService.findByHql(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=0 and info.course.resourceid=? and info.examSub.resourceid=? and info.examCourseType=? ", course.getResourceid(),examSubId,examCourseType);
//					if (null!=list && !list.isEmpty()) {
//						examInfo = list.get(0);
//					} else {
//						examInfo = new ExamInfo();
//						examInfo.setCourse(course);
//						examInfo.setIsOutplanCourse("0");
//						examInfo.setExamSub(examSub);
//						examInfo.setExamCourseType(examCourseType);
//						examInfo.setCourseScoreType(examSub.getCourseScoreType());
//						if (1==examCourseType) {
//							if (null==examSub.getFacestudyScorePer()) 
//							per=examSub.getFacestudyScorePer();
//							examInfo.setStudyScorePer(per);
//						}else if(2==examCourseType) {
//							if (null==examSub.getNetsidestudyScorePer()) 
//							per=examSub.getFacestudyScorePer();
//							examInfo.setStudyScorePer(per);
//						}
//						examInfoService.save(examInfo);				
//					}
				
				
					for (int i = 0; i < studyNos.length; i++){
						//拿到原来考试批次
						ExamResults examResults = examResultsService.findExamResulByExamSubAndStuNoAndCourseId(examSubId,studyNos[i],course.getResourceid(),null);
						if(examResults==null){
							examResults = new ExamResults();
							examResults.setCourse(course);
							examResults.setExamInfo(examInfo);
							examResults.setMajorCourseId(teachingPlanCourseId);
							examResults.setCourseScoreType(Constants.COURSE_SCORE_TYPE_ONEHUNHRED);
							examResults.setCheckStatus("0");
							examResults.setExamsubId(examSubId);
							examResults.setIsMachineExam(isMachineExam);
							examResults.setExamType(course.getExamType()!=null?course.getExamType().intValue():null);
							examResults.setCourseType(planCourse.getCourseType());													
							StudentInfo studentInfo = studentInfoService.findUnique(" from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and studyNo=? ", studyNos[i]);
							examResults.setStudentInfo(studentInfo);
							
							examResults.setCourseScoreType(examInfo.getCourseScoreType());
							examResults.setCreditHour(planCourse.getCreditHour());
							examResults.setStydyHour(planCourse.getStydyHour()!=null?planCourse.getStydyHour().intValue():null);
						}
						if(examResults.getExamInfo() == null){
							examResults.setExamInfo(examInfo);
						}
						examResults.setPlanCourseTeachType(planCourse.getTeachType());						
						int checkStatus 	   = Integer.valueOf(examResults.getCheckStatus());
						if (checkStatus>0) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"成绩已提交不允许再保存!");
						}
						List examCountList     = examResultsService.findByHql(examCountHQL,examResults.getCourse().getResourceid(), examResults.getStudentInfo().getResourceid());
						long examCount         = (Long)examCountList.get(0) ;     					 								 //选考次数
						if(ExStringUtils.isBlank(examResults.getResourceid())){//新生成成绩
							examCount++;
						}
						String writtenScore    = ExStringUtils.trimToEmpty(request.getParameter("writtenScore"+studyNos[i]));        //卷面成绩
						String usuallyScore    = writtenScore;
						
						String examAbnormity   = "";
							
						if("作弊".equals(writtenScore)){
							examAbnormity = "1";
						}				
						if("缺考".equals(writtenScore)){
							examAbnormity = "2";
						}
						if("无卷".equals(writtenScore)){
							examAbnormity = "3";
						}
						if("其它".equals(writtenScore)){
							examAbnormity = "4";
						}
						if("缓考".equals(writtenScore)){
							examAbnormity = "5";
						}
						if (ExStringUtils.isEmpty(examAbnormity)&&ExStringUtils.isEmpty(writtenScore)) {
							throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩不能为空!");
						}
						if (ExStringUtils.isNotEmpty(examAbnormity)) {
							examResults.setExamAbnormity(examAbnormity);
							examResults.setWrittenScore("");
						}else {
							if (ExStringUtils.isEmpty(writtenScore)) {
								throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩不能为空!");
							}
							Double writtenScoreD  = Double.parseDouble(writtenScore);
							if(writtenScoreD<0||writtenScoreD>100){
								throw new WebException(examResults.getStudentInfo().getStudentName()+"卷面成绩必须为0-100分!");
							}
							
							BigDecimal w_Score	 = new BigDecimal(writtenScore);
							examResults.setWrittenScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							examResults.setUsuallyScore((w_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
							examResults.setExamAbnormity("0");
						}
//						if(ExStringUtils.isNotBlank(usuallyScore)){
//							Double usuallyScoreD  = Double.parseDouble(usuallyScore);
//							if(usuallyScoreD<0||usuallyScoreD>100){
//								throw new WebException(examResults.getStudentInfo().getStudentName()+"平时成绩必须为0-100分!");
//							}							
//							BigDecimal u_Score	 = new BigDecimal(usuallyScore);
//							examResults.setUsuallyScore((u_Score.divide(BigDecimal.ONE,0,BigDecimal.ROUND_HALF_UP)).toString());
//						}
						examResults.setExamCount(examCount);
				 		examResults.setCheckStatus("0");
				 		examResults.setFillinMan(user.getCnName());
				 		examResults.setFillinManId(user.getResourceid());
				 		examResults.setFillinDate(curTime);
//				 		examResults.setIsMakeupExam("Y");
				 		examResults.setIsMakeupExam(examSub.getExamType());//考试类型
				 		
						examResultsService.saveOrUpdate(examResults);							
							
						List<StudentLearnPlan> stuLearnPlan  = studentLearnPlanService.findByHql("from "+StudentLearnPlan.class.getSimpleName()+" plan where plan.isDeleted=? and plan.studentInfo.resourceid =? and plan.teachingPlanCourse.resourceid=?", 0,examResults.getStudentInfo().getResourceid(),teachingPlanCourseId);
						StudentLearnPlan learnPlan           = null;
						if (null!=stuLearnPlan && !stuLearnPlan.isEmpty()) {//--学生的学习计划为空时查询是否包涵要导入成绩的课程的学习计划
							learnPlan = stuLearnPlan.get(0);
							learnPlan.setExamResults(examResults);
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setStatus(2);
						}else {//-----------------------------------------------学生的学习计划为空时直接新建一条学习计划记录(补录学习计划)
							learnPlan = new StudentLearnPlan();
							learnPlan.setExamInfo(examResults.getExamInfo());
							learnPlan.setTeachingPlanCourse(planCourse);
							learnPlan.setExamResults(examResults);
							learnPlan.setStatus(2);
							learnPlan.setStudentInfo(examResults.getStudentInfo());
							learnPlan.setTerm(examSub.getTerm());
							learnPlan.setYearInfo(examSub.getYearInfo());	
							learnPlan.setOrderExamYear(examSub.getYearInfo());
							learnPlan.setOrderExamTerm(examSub.getTerm());
						}						
						studentLearnPlanService.saveOrUpdate(learnPlan);
						updateRecordCount++;
						saveList.add(studyNos[i]);
					}
//				}else {
//					success = false;
//					msg     = "考试批次预约未结束，不允许录入成绩！<br/><strong>预约结束时间:</strong><br/>"
//			    		+ ExDateUtils.formatDateStr(endTime,ExDateUtils.PATTREN_DATE_TIME);		
//				}
			}else {
				success = false;
				msg 	= "找不到考试批次或考试课程,请与管理员联系！";
			}		
		} catch (Exception e) {
			logger.error("导入学生成绩异常:{}",e.fillInStackTrace());
			success = false;
			msg 	= "导入学生成绩异常:"+e.getMessage();

		}		
		
		resultMap.put("updateRecordCount",updateRecordCount);
		resultMap.put("saveList", saveList);
		resultMap.put("success",success);
		resultMap.put("msg", msg);
		renderJson(response,JsonUtils.mapToJson(resultMap));
	}
	
	
	/**
	 * 导出单个学生成绩
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/teaching/result/personalExportCard-view.html")
	public void exportPersonalExamResults(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String flag 	 		  = ExStringUtils.trimToEmpty(request.getParameter("flag")); // 导出类型
		String studentId 		  = ExStringUtils.trimToEmpty(request.getParameter("studentId")); //学号
		String printExam		  = ExStringUtils.trimToEmpty(request.getParameter("printExam"));//
		String terms			  = ExStringUtils.trimToEmpty(request.getParameter("terms"));//学期
		String degreeUnitExam        = ExStringUtils.trimToEmpty(request.getParameter("degreeUnitExam"));//学位外语成绩
		User curUser              = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool        = false;
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool            = true;
		}
		//try {
			StudentInfo studentInfo = studentInfoService.get(studentId);
			List<StudentExamResultsVo> list = studentExamResultsService.studentExamResultsList(studentInfo,"export",null);//数据
			if (null != list && list.size() > 0) {
				for (int j = 0; j < list.size(); j++) {
					StudentExamResultsVo result = list.get(j);
					if ("免考".equals(result.getCheckStatus())) {
						result.setIntegratedScore("免" + result.getIntegratedScore());
					}else if (ExStringUtils.isNotBlank(result.getExamResultsChs())) {
						result.setIntegratedScore(result.getExamResultsChs());
					}
				}
			}
			StudentExamResultsVo statVo     = null;
			if (null!=list&&list.size()>0) {
				statVo     = list.get(list.size()-1);
				list.remove(list.size()-1);
			}
			//if (isBrschool) {
				List<StudentExamResultsVo> subList = new ArrayList<StudentExamResultsVo>();
				for (StudentExamResultsVo vo : list) {
					if (!"4".equals(ExStringUtils.trimToEmpty(vo.getCheckStatusCode()))) {
						subList.add(vo);
					}
				}
				list.removeAll(subList);
			//}
			list = filtrateList(list,terms,studentInfo,degreeUnitExam);

			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				//导出
				File excelFile = null;
				GUIDUtils.init();
				//是否需要把XYMC特殊处理
				List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
						, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板				
				templateMap.put("exportName", studentInfo.getStudentName()+"个人成绩表"); //标题
				templateMap.put("studentId", studentInfo.getStudyNo()); //学号
				templateMap.put("studentName", studentInfo.getStudentName()); //姓名
				templateMap.put("gradeName",studentInfo.getGrade().getGradeName()); //年级
				String majorName = null != studentInfo.getMajor()?studentInfo.getMajor().getMajorName():"";
				if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)){
					String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
					String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
					for (int i = 0; i < listDictZYDM.size(); i++) {
						String rpstr1 = ExStringUtils.trim(listDictZYDM.get(i).getDictName()+"").replace("（", "(").replace("）", ")");
						String rpstr2 = ExStringUtils.trim(listDictZYDM.get(i).getDictValue()+"").replace("（", "(").replace("）", ")");
						zymcReplaceArrayL[i] = rpstr1;
						zymcReplaceArrayR[i] = rpstr2;	
					}
					majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
				}
	 			templateMap.put("majorName", majorName); //专业
				templateMap.put("classicName",studentInfo.getClassic().getClassicName()); //层次
				templateMap.put("schoolName", studentInfo.getBranchSchool().getUnitName()); //教学点
				
				//模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
												+File.separator+"excel"+File.separator+"exportPersonalExamResultes.xls";

				exportExcelService.initParmasByfile(disFile, "exportPersonalExamResults", list,null);
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, studentInfo.getStudentName()+"个人成绩表.xls", excelFile.getAbsolutePath(),true);
			}catch(Exception e){
				logger.error("导出excel文件出错："+e.fillInStackTrace());
			}
	}
	
	/**
	 * 按学期过滤list 并且排序
	 * @param list
	 * @return
	 * @throws Exception 
	 */
	private List<StudentExamResultsVo> filtrateList(List<StudentExamResultsVo> list, String terms, StudentInfo stu, String degreeUnitExam) throws Exception{
		List<StudentExamResultsVo> rtList = new ArrayList<StudentExamResultsVo>(0);
		List<String> tempList  = new ArrayList<String>(0);
		if(null != list && list.size() > 0){
			//冒泡升序
			for(int index = 0 ; index < list.size(); index++){
				for(int x = index + 1 ; x < list.size() ; x ++ ){
					try {
						StudentExamResultsVo tempvo = list.get(index);
						StudentExamResultsVo tempvo1 = list.get(x);
						int _vo1 = Integer.parseInt(null == tempvo.getCourseTerm() ? "0" :tempvo.getCourseTerm() );
						int _vo2 = Integer.parseInt(null == tempvo1.getCourseTerm() ? "0" : tempvo1.getCourseTerm());						
						if(_vo1 > _vo2){
							list.set(index, tempvo1);
							list.set(x, tempvo);
						}else if(_vo1 == _vo2){							
							int index_1 = Integer.parseInt(null == tempvo.getIndex() ? "0" :tempvo.getIndex() );
							int index_2 = Integer.parseInt(null == tempvo1.getIndex() ? "0" : tempvo1.getIndex());
							if(index_1 > index_2){
								list.set(index, tempvo1);
								list.set(x, tempvo);	
							}
						}
					} catch (NumberFormatException e) {
						continue;
					}
					
					
				}
			}
		}
		
		if(ExStringUtils.isNotBlank(terms)){
			String[] term_array = terms.split(",");
			tempList = Arrays.asList(term_array);
		}else{
			tempList = null;
		}
		//打印选择方式 （1单打计划外，2单打计划内，3混打）
		rtList = teachingPlanCourseService.printOutOrInExam(list,tempList,stu,degreeUnitExam);
		//添加序号
		int index=1;
		for (StudentExamResultsVo examResultsVo : rtList) {
			examResultsVo.setIndex(index+"");
			index++;
		}
		return rtList;
	}

	
	/**
	 * 打印学生成绩单（广东医、广外）
	 * 
	 * @param request
	 * @param response
	 * @param studentList
	 * @param printDate
	 */
	private void printPersonalGdyReportCard(HttpServletRequest request, HttpServletResponse response, List<StudentInfo> studentList, String printDate, String printExam, String terms, String degreeUnitExam, String isShowOrder) {
		
		Map<String, Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint   = null;//输出的报表
		//String [] ids 			  = studentId.split(",");
		User curUser              = SpringSecurityHelper.getCurrentUser();
		boolean isBrschool        = false;
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			isBrschool            = true;
		}
		String passExam = request.getParameter("passExam");

		String reprotFile = File.separator+"reports"+File.separator+"examPrint"+File.separator;
		if ("10571".equals(schoolCode)) {//广东医
			reprotFile += "reportCard.jasper";
		} else if ("11846".equals(schoolCode)) {//广东外语外贸
			if ("Y".equals(isShowOrder)) {
				reprotFile += "reportCard_gdwy_order.jasper";
			} else {
				reprotFile += "reportCard_gdwy.jasper";
			}
		}

		//专业是否需要替换字符
		List<Dictionary> listDictZYDM = dictionaryService.findByHql(" from "+Dictionary.class.getSimpleName()+ " where isDeleted = ? and isUsed = ? and parentDict.dictCode = ? "
				, 0,Constants.BOOLEAN_YES,"CodeLearnwebReplaceStr");

		//广外绩点计算规则
		List<Dictionary> dictList = CacheAppManager.getChildren("CodeFractionalGPA");
		try {
			reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reprotFile),"utf-8");
			List<StudentExamResultsVo> removelist = null;
			List<StudentExamResultsVo> list = null;
			List<String> courseids = null;
			List<StudentExamResultsVo> subList;
			JasperPrint jasperPage;
			JRBeanCollectionDataSource dataSource;
			int orderNo = 0;
			for (StudentInfo studentInfo : studentList) {
				//StudentInfo studentInfo = studentInfoService.get(ids[i]);
				removelist = new ArrayList<StudentExamResultsVo>();
				list = studentExamResultsService.studentExamResultsList(studentInfo,"print",0);//数据
				courseids = new ArrayList<String>();
				if (null != list && list.size() > 0) {
					StudentExamResultsVo result;
					for (int j = 0; j < list.size(); j++) {
						result = list.get(j);
						//如果已经存在该课程则替换成绩
						if(courseids.contains(result.getCourseId()) && "4".equals(result.getCheckStatusCode())){
							removelist.add(list.get(j-1));
						}
						if ("免考".equals(result.getCheckStatus()) || "免修".equals(result.getCheckStatus())) {
							if(result.getIntegratedScore()!=null && !result.getIntegratedScore().contains("免")){
								result.setIntegratedScore("免" + result.getIntegratedScore());
							}
						}else if (ExStringUtils.isNotBlank(result.getExamResultsChs())) {
							if(!"缓考".equals(result.getExamResultsChs())){
								result.setIntegratedScore(result.getExamResultsChs());
							}
						}
						courseids.add(result.getCourseId());
					}
				}
				StudentExamResultsVo statVo     = null;
				if (null!=list&&list.size()>0) {
					statVo     = list.get(list.size()-1);
					list.remove(list.size()-1);
				}
				subList = new ArrayList<StudentExamResultsVo>();
				for (StudentExamResultsVo vo : list) {
					//只打印已发布成绩
					if (!"4".equals(ExStringUtils.trimToEmpty(vo.getCheckStatusCode()))) {
						subList.add(vo);
					}
					//只打印及格成绩
					if ("Y".equals(passExam)) {
						if (!"Y".equals(vo.getIsPass())) {
							subList.add(vo);
						}
					}
				}
				list.removeAll(subList);
				list.removeAll(removelist);
				list= filtrateList(list,terms,studentInfo,degreeUnitExam);
				Double totalGPA = 0D;
				int makeupNum = 0;
				if (list != null) {
					StudentExamResultsVo studentExamResultsVo;
					Map<String, Object> condition;
					Map<String, Object> resultMap;
					Map<String, Object> mkReturnMap;
					for (int j = 0; j < list.size(); j++) {
						studentExamResultsVo = list.get(j);
						studentExamResultsVo.setSortNum(String.valueOf(j+1));
						condition = new HashMap<String, Object>(2);
						condition.put("studentid", studentInfo.getResourceid());
						condition.put("courseid", studentExamResultsVo.getCourseId());
						resultMap = studentExamResultsService.getNormalExamResult(condition);
						boolean isNormal = (Boolean)resultMap.get("isNormal");
						String examAbnormity = (String)resultMap.get("examAbnormity");
						Double integratedScoreNum = 0D;
						if(isNormal){
							integratedScoreNum = (Double)resultMap.get("original");
						}
						// 正考成绩
						if(!"30".equals(studentExamResultsVo.getExamResultsType()) && ExStringUtils.isNotBlank(resultMap.get("handledRs"))){//等级显示
							studentExamResultsVo.setIntegratedScore((String)resultMap.get("handledRs"));
						}
						
						// 最高补考成绩
						mkReturnMap = studentExamResultsService.getMaxMakeupExamResult(condition);
						Double maxMakeupScore = (Double)mkReturnMap.get("max");
						Boolean isMakeup = (Boolean)mkReturnMap.get("isMakeup");
						studentExamResultsVo.setBkScore(maxMakeupScore==0D?"":maxMakeupScore.toString());
						// 绩点
						Double GPA = 0D;
						if (integratedScoreNum == 0D && studentExamResultsVo.getIntegratedScoreL()!=null && studentExamResultsVo.getIntegratedScoreL()>0) {
							integratedScoreNum = Double.valueOf(studentExamResultsVo.getIntegratedScoreL());
						}
						if(integratedScoreNum>=60){
							if ("10571".equals(schoolCode)) {//广东医绩点计算规则
								GPA = 1+(integratedScoreNum-60)/10;
							}else if ("11846".equals(schoolCode)) {	//广外绩点计算规则
								GPA = examResultsService.getGPA_gdwy(integratedScoreNum,dictList);
							}
						} else {//大于等于60分的补考成绩，获得1.0个绩点
							if(maxMakeupScore>=60){
								GPA = 1D;
							}
							if(!"5".equals(examAbnormity) || isMakeup){
								makeupNum++;
							}
						}

						totalGPA += GPA;
						studentExamResultsVo.setGPA(GPA.toString());
					}
				}
				dataSource = new JRBeanCollectionDataSource(list);
				
				if (null!=statVo) {
					// 取得的总学分
					param.put("totalCredit", statVo.getTotalCredit());
					// 补考门数
					param.put("makeupNum", String.valueOf(makeupNum));
					// 取得的总学分绩点
					param.put("totalGPA", totalGPA.toString());
					// 平均学分绩点
					param.put("averageGPA", BigDecimal.valueOf(list.size()==0?0:totalGPA/list.size()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
					// 平均分
					param.put("averageScore", BigDecimal.valueOf(list.size()==0?0:statVo.getTotalScore()/list.size()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
				}
				param.put("orderNo",++orderNo+"");
				String majorName       = studentInfo.getMajor().getMajorName();

				GraduateData graduateData=  graduateDataService.findByHql(studentInfo);
				Date graduateDate = null;//毕业时间
				if(graduateData!=null) {
					graduateDate = graduateData.getGraduateDate();
				}
				Date inDate = studentInfo.getInDate()==null?studentInfo.getGrade().getIndate():studentInfo.getInDate();//入学时间
				String graduateDateStr = "";
				String inDateStr = "";
				if (graduateDate!=null) {
					graduateDateStr = ExDateUtils.formatDateStr(graduateDate,"yyyy-MM");
				}
				if (inDate!=null) {
					inDateStr = ExDateUtils.formatDateStr(inDate,"yyyy-MM");
				}
				param.put("graduateDate",graduateDateStr);
				param.put("inDate",inDateStr);
				int indexA = majorName.indexOf("A");
				int indexB = majorName.indexOf("B");
				if (indexA>0||indexB>0) {
					majorName          = indexA>0?majorName.substring(0,indexA):majorName.substring(0,indexB);
				}

				if(null != listDictZYDM && listDictZYDM.size() > 0 && ExStringUtils.isNotBlank(majorName)){
					String[] zymcReplaceArrayL = new String[listDictZYDM.size()];
					String[] zymcReplaceArrayR = new String[listDictZYDM.size()];
					for (int t = 0; t < listDictZYDM.size();t++) {
						String rpstr1 = ExStringUtils.trim(listDictZYDM.get(t).getDictName()+"").replace("（", "(").replace("）", ")");
						String rpstr2 = ExStringUtils.trim(listDictZYDM.get(t).getDictValue()+"").replace("（", "(").replace("）", ")");
						zymcReplaceArrayL[t] = rpstr1;
						zymcReplaceArrayR[t] = rpstr2;	
					}
					majorName = ReplaceStr.replace(zymcReplaceArrayL, zymcReplaceArrayR, majorName); //去除符号（含符号）里面的字符
				}
				param.put("eductionalSystem", studentInfo.getTeachingPlan()==null?"":studentInfo.getTeachingPlan().getEduYear());
				param.put("studentNo",studentInfo.getStudyNo());
				param.put("studentName",studentInfo.getStudentName());
				String gender = studentInfo.getStudentBaseInfo().getGender();
				if(gender!=null){
					gender = "1".equals(gender) ?"男":"女";
				}else {
					gender= "";
				}
				param.put("gender",gender);
				String birth = "";
				if(studentInfo.getStudentBaseInfo().getBornDay()!=null){
					birth = ExDateUtils.formatDateStr(studentInfo.getStudentBaseInfo().getBornDay(),"yyyy.MM");
				}
				param.put("birth",birth);
				param.put("majorName",majorName);
				param.put("classicName",studentInfo.getClassic().getShortName());
				param.put("gradeName",studentInfo.getGrade().getGradeName());
				param.put("brSchool", studentInfo.getBranchSchool().getUnitShortName());
				String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
				String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
				param.put("title", schoolName+schoolConnectName+"学生成绩单");
				param.put("schoolAllName", schoolName+"继续教育学院");
				param.put("logoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				// 打印日期要水印
				String printDateStr = "" ;
				if (ExStringUtils.isNotEmpty(printDate)) {
					printDateStr = ExDateUtils.formatDateStr(ExDateUtils.parseDate(printDate,"yyyy-MM-dd"),"yyyy年MM月dd日");
				}else {
					printDateStr = ExDateUtils.formatDateStr(new Date(),"yyyy年MM月dd日");
				}
				//TODO:目前先不做，制作水印时间
				param.put("printDate",printDateStr);

				if (null!=jasperPrint && jasperPrint.getPages().size()>0) {
					jasperPage = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
				}else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
				}
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印学生所有课程成绩单出错",e);
		}
		
	}
	
	/**
	 * 下载网上在线成绩单
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/usualresults/download.html")
	public void downLoadOnlineResult(HttpServletRequest request , HttpServletResponse response){
		Map<String,Object> condition = new HashMap<String, Object>();
		String brSchoolid = request.getParameter("brSchoolid");//教学站id
		String yearInfo = request.getParameter("examOrderYearInfo");
		String term = request.getParameter("examOrderTerm");
		String name = request.getParameter("name");//姓名
		String studyNo = request.getParameter("studyNo");//学号
		String courseName = request.getParameter("courseName");//课程	
		String courseId = request.getParameter("courseId");//课程id
		String usualStatus = request.getParameter("usualStatus");
		
		if(ExStringUtils.isNotEmpty(brSchoolid)) {
			condition.put("brSchoolid", brSchoolid);
		}
		if(ExStringUtils.isNotEmpty(yearInfo)) {
			condition.put("examOrderYearInfo", yearInfo);
		}
		if(ExStringUtils.isNotEmpty(term)) {
			condition.put("examOrderTerm", term);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotEmpty(courseName)) {
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotBlank(usualStatus)) {
			condition.put("usualStatus", usualStatus);
		}
		//Date curTime       = new Date();
		//User curUser       = SpringSecurityHelper.getCurrentUser();
		//String classesname = ExStringUtils.getEncodeURIComponentByTwice(ExStringUtils.trimToEmpty(request.getParameter("classesname")));//班级名称
		String teacherId = "";
		User user = SpringSecurityHelper.getCurrentUser();
		if(SpringSecurityHelper.isUserInRole(CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue())){
			teacherId = user.getResourceid();	
			condition.put("teacherId", teacherId);
		}
		
		if(ExStringUtils.isNotBlank(courseId) && ExStringUtils.isNotBlank(yearInfo) && ExStringUtils.isNotBlank(term)){
			
			do{
				List<UsualResultsVo> usualResultsVos = usualResultsService.findUsualResultsVoByCondition(condition);
				if(usualResultsVos.size()<=0){
					renderHtml(response, "<script>alert('缺少打印数据,只有已经提交的成绩才可以打印')</script>");
					continue;
				}
				UsualResultsRule currentRule = usualResultsRuleService.getUsualResultsRuleByCourseAndYear(courseId,yearInfo,term);
				
				try {
					if(currentRule==null){
						Course course = courseService.get(courseId);
						//String errorMsg = "课程:"+course.getCourseName()+"不存在学生平时分积分规则,请先建立!";
						renderHtml(response, "<script>alert('课程:'"+course.getCourseName()+"'不存在学生平时分积分规则,请先建立!')</script>");
						continue;
					}

				
				
		 	    setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		 	    GUIDUtils.init();
		 	    //导出
				File excelFile = null;
				File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");


				
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "UsualResultsVo", usualResultsVos,null);
				exportExcelService.getModelToExcel().setRowHeight(300);//设置行高

				
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				
				String downloadFileName = "网上学习成绩.xls";
				String downloadFilePath = excelFile.getAbsolutePath();
				
				downloadFile(response, downloadFileName,downloadFilePath,true);
				} catch (Exception e) {
					renderHtml(response, "<script>alert('下载出错,请联系管理员')</script>");
				}
			}while(false);
			
		}else{
			renderHtml(response,"<script>alert('请输入相应的查选条件')</script>");
		}

	 	
	}
}