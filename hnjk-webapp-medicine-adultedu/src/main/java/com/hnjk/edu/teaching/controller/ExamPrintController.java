package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.edu.teaching.service.*;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jxl.write.WritableCellFormat;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.learning.model.StudentLearnPlan;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.roll.util.Tools;
import com.hnjk.edu.roll.vo.StudentSignatureVo;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamPaperBag;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.NoExamApply;
import com.hnjk.edu.teaching.model.TeachingGuidePlan;
import com.hnjk.edu.teaching.model.TeachingPlan;
import com.hnjk.edu.teaching.model.TeachingPlanCourse;
import com.hnjk.edu.teaching.model.TeachingPlanCourseStatus;
import com.hnjk.edu.teaching.service.impl.ExamPrintServiceImpl;
import com.hnjk.edu.teaching.vo.ExamResultsVo;
import com.hnjk.edu.teaching.vo.ExportCourseAndTeacherVo;
import com.hnjk.edu.teaching.vo.FailExamStudentVo;
import com.hnjk.edu.teaching.vo.NonexaminationExportVoForGZDX;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.Role;
import com.hnjk.security.model.User;
import com.hnjk.security.model.UserExtends;
import com.hnjk.security.service.IOrgUnitService;
import com.hnjk.security.service.IUserService;
/**
 * 考试打印
 * @author luof
 *
 */
@Controller
public class ExamPrintController extends FileUploadAndDownloadSupportController{
	
	private static final long serialVersionUID = 9173086260854166677L;

	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("examPrintService")
	private IExamPrintService examPrintService;
	
	@Autowired
	@Qualifier("teachingguideplanservice")
	private ITeachingGuidePlanService teachingguideplanservice;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;//注入学籍服务
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("examPaperBagService")
	private IExamPaperBagService examPaperBagService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	@Autowired
	@Qualifier("teachingPlanCourseService")
	private ITeachingPlanCourseService teachingPlanCourseService;
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("teachingPlanCourseStatusService")
	private ITeachingPlanCourseStatusService teachingPlanCourseStatusService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("teachingJDBCService")
	private ITeachingJDBCService teachingJDBCService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentinfoservice;
	
	@Autowired
	@Qualifier("noexamapplyservice")
	INoExamApplyService noexamapplyservice;
	
	@Autowired
	@Qualifier("userService")
	private IUserService userService;
	
	/**
	 * 考试打印-列表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/printlist.html")
	public String examPrint(HttpServletRequest request,HttpServletResponse response){
		
		return"/edu3/teaching/examPrint/examPrint";
	}

	/**
	 * 打印卷袋标签-预览
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printBagLabel-view.html")
	public String printExamBagLabelView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		return"/edu3/teaching/examPrint/examBagLabel-printview";
	}

	/**
	 * 打印卷袋标签
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printBagLabel.html")
	public void printExamBagLabel(HttpServletRequest request,HttpServletResponse response){
		
		String examSubId         = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		Map<String,Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint  = null;
		//Connection conn          = null;
		try {
			if (!"".equals(examSubId)) {
				param.put("examSubId", examSubId);
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				//conn             = examPrintService.getConn();
				String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
										  File.separator+"examPrint"+File.separator+"examBagLabel.jasper"),	"utf-8");
				File reprot_file        = new File(reprotFile);
				//jasperPrint 			= JasperFillManager.fillReport(reprot_file.getPath(), param, conn); // 填充报表
				jasperPrint		= examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
			}else {
				String msg = "考试批次不能为空，请选择要打印卷袋标签的考试批次!";
				renderHtml(response, msg);
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印卷袋标签Controller出错{}",e.fillInStackTrace());
			renderHtml(response, "打印卷袋标签出错!");
		}
	}

	/**
	 * 打印考试形式-条件选择窗口
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExamForm-condition.html")
	public String printExamFormCondition(HttpServletRequest request,ModelMap model){
		model.addAttribute("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		return "/edu3/teaching/examPrint/examForm-condition";
	}
	
	/**
	 * 打印考试形式-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExamForm-view.html")
	public String printExamFormView(HttpServletRequest request,HttpServletResponse response ,ModelMap model){

		model.addAttribute("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		model.addAttribute("gradeId",ExStringUtils.trimToEmpty(request.getParameter("gradeId")));
		model.addAttribute("majorId",ExStringUtils.trimToEmpty(request.getParameter("majorId")));
		model.addAttribute("classicId",ExStringUtils.trimToEmpty(request.getParameter("classicId")));
		
		return "/edu3/teaching/examPrint/examForm-printview";
	}
	/**
	 * 打印考试形式
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExamForm.html")
	public void printExam(HttpServletRequest request,HttpServletResponse response){
		
		String examSubId  = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String gradeId    = ExStringUtils.trimToEmpty(request.getParameter("gradeId"));
		String majorId    = ExStringUtils.trimToEmpty(request.getParameter("majorId"));
		String classicId  = ExStringUtils.trimToEmpty(request.getParameter("classicId"));
		
		List     gradeList       = null;
		JasperPrint jasperPrint  = null;//输出的报表
		Map<String,Object> param = new HashMap<String, Object>();
		if(!"".equals(examSubId)) {
			param.put("examSubId",examSubId);
		}
		if(!"".equals(gradeId)) {
			param.put("gradeid",  gradeId);
		}
		if(!"".equals(majorId)) {
			param.put("major",  majorId);
		}
		if(!"".equals(classicId)) {
			param.put("classic",classicId);
		}
		//Connection conn    		 = null;
		try {
			ExamSub examSub     = examSubService.get(examSubId);
		//	conn    		    = examPrintService.getConn();
			String reprotFile   = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
								  File.separator+"examPrint"+File.separator+"examForm.jasper"),	"utf-8");
			File reprot_file    = new File(reprotFile);
			
			if (param.containsKey("gradeid") || param.containsKey("major") || param.containsKey("classic")) {
				gradeList       = teachingguideplanservice.findTeachingGradeByCondition(param, new Page()).getResult();
			}else {
				gradeList       = teachingguideplanservice.findAlTeachingGradePlan();
			}
			for (int i = 0; i < gradeList.size(); i++) {
				
				TeachingGuidePlan gradePlan=(TeachingGuidePlan) gradeList.get(i);
				TeachingPlan plan          = gradePlan.getTeachingPlan();
				
				param.put("examSubName", examSub.getBatchName());
				param.put("title","专业："+plan.getMajor().getMajorName()+"      层次："+plan.getClassic().getClassicName());
				param.put("gradeID",gradePlan.getGrade().getResourceid());
				param.put("majorID",plan.getMajor().getResourceid());
				param.put("classicID",plan.getClassic().getResourceid());
				param.put("planID",plan.getResourceid());
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				if (i==0) {					
					//jasperPrint = JasperFillManager.fillReport(reprot_file.getPath(), param, conn); // 填充报表
					jasperPrint	=	examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
				}else {
					//JasperPrint jasperPage = JasperFillManager.fillReport(reprot_file.getPath(), param, conn); // 填充报表
					JasperPrint jasperPage = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}
			}
	
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印考试形式Controller出错{}",e.fillInStackTrace());
			renderHtml(response, "打印考试形式出错!");
		}
	}
	/**
	 * 打印分教点交接表-条件选择
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExaminationTransferList-condition.html")
	public String printExaminationTransferListCondition(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String type      = ExStringUtils.trimToEmpty(request.getParameter("type"));
		List list        = examPrintService.getExamSubUint(examSubId);
		model.put("type", type);
		model.put("brSchoolList", list);
		model.put("examSubId", examSubId);
		return "/edu3/teaching/examPrint/examinationTransferList-condition";
	}
	/**
	 * 打印分教点交接表-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExaminationTransferList-view.html")
	public String printExaminationTransferListView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String examSubId 	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String  brschoolIds  = ExStringUtils.trimToEmpty(request.getParameter("brschoolIds"));
		String type     	 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		model.put("brschoolIds", brschoolIds);
		model.put("examSubId", examSubId);
		model.put("type", type);
		return "/edu3/teaching/examPrint/examinationTransferList-printview";
	}
	/**
	 *  打印分教点交接表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExaminationTransferList.html")
	public void printExaminationTransferList(HttpServletRequest request,HttpServletResponse response){
		String examSubId 	 	 = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String  brschoolIds  	 = ExStringUtils.trimToEmpty(request.getParameter("brschoolIds"));
		String type     	 	 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String [] brschoolId	 = brschoolIds.split(",");
		//Connection conn      	 = null;
		JasperPrint jasperPrint  = null;//输出的报表
		Map<String,Object> param = new HashMap<String, Object>();
		
		try {
			ExamSub examSub      = examSubService.get(examSubId);
			//conn				 = examPrintService.getConn();
			String reprotFile    = "";
			
			if ("num".equals(type)) {
				reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
								   File.separator+"examPrint"+File.separator+"examinationTransferListParentShoworderASC.jasper"),"utf-8");
			}else if ("time".equals(type)) {
				reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
								   File.separator+"examPrint"+File.separator+"examinationTransferListParentShoworderASC.jasper"),"utf-8");
			}
			
			File reprot_file     = new File(reprotFile);
			param.put("examSubId",examSubId);
			param.put("examSubName", examSub.getBatchName());
			param.put("reportRootPath", request.getSession().getServletContext().getRealPath("/reports/examPrint"));
			for (int i = 0; i < brschoolId.length; i++) {
				
				OrgUnit unit     = orgUnitService.get(brschoolId[i]);
				param.put("branchSchoolId",unit.getResourceid());
				param.put("branchSchoolName", unit.getUnitName());
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				
				if (i==0) {
					//jasperPrint = JasperFillManager.fillReport(reprot_file.getPath(), param, conn); // 填充报表
					jasperPrint = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
				}else {
					//JasperPrint jasperPage = JasperFillManager.fillReport(reprot_file.getPath(), param, conn); // 填充报表
					JasperPrint jasperPage = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
					List jsperPageList	   = jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage 			   = null;//清除临时报表的内存占用
				}
			}
			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印分教点交接表Controller出错{}",e.fillInStackTrace());
			renderHtml(response, "打印分教点交接表出错!");
		}
		
	}
	/**
	 * 打印准考证-列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExamCard-list.html")
	public  String printExamCardCondition(HttpServletRequest request,HttpServletResponse response,ModelMap model,Page objPage){
		
		objPage.setOrder(Page.DESC);
		objPage.setOrderBy("rs.studentInfo.studyNo");
		
		Map<String,Object> condition = new HashMap<String, Object>();
		
		String examSub 			 	 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String branchSchoolName      = ExStringUtils.trimToEmpty(request.getParameter("branchSchoolName"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("stuName"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		
		User user 					 = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool 			 = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
		}
		
		if(ExStringUtils.isNotEmpty(examSub)) {
			condition.put("examSub", examSub);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(branchSchoolName)) {
			condition.put("branchSchoolName", branchSchoolName);
		}
		if(ExStringUtils.isNotEmpty(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(studyNo)) {
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		
		if(ExStringUtils.isNotEmpty(examSub) && ExStringUtils.isNotEmpty(branchSchool)){//只有这两项条件时，才查询
			objPage = examPrintService.findExamCardList(condition, objPage);
		}
		
		model.addAttribute("defaultGrade",gradeService.getDefaultGrade());
		model.addAttribute("condition", condition);
		model.addAttribute("objPage", objPage);
		
		return "/edu3/teaching/examCard/examCard-list";
	}
	/**
	 * 打印准考证-预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExamCard-view.html")
	public String printExamCardView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		
		String examSub 			 	 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String branchSchool 	 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 	 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 	 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic				 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 	 = ExStringUtils.trimToEmpty(request.getParameter("stuName"));
		String studyNo				 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String stuId                 = ExStringUtils.trimToEmpty(request.getParameter("stuId"));
		
		model.put("examSub", examSub);
		model.put("branchSchool", branchSchool);
		model.put("gradeid", gradeid);
		model.put("major", major);
		model.put("classic", classic);
		model.put("name", name);
		model.put("studyNo", studyNo);
		model.put("stuId", stuId);
		
		return "/edu3/teaching/examCard/examCard-printview";
	}
	/**
	 * 打印准考证
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printExamCard.html")
	public void printExamCard(HttpServletRequest request,HttpServletResponse response){
		
		String examSubId 		 = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String branchSchool 	 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String gradeid			 = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String major  			 = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String classic			 = ExStringUtils.trimToEmpty(request.getParameter("classic"));
		String name  			 = ExStringUtils.trimToEmpty(request.getParameter("stuName"));
		String studyNo			 = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String stuId             = ExStringUtils.trimToEmpty(request.getParameter("stuId"));
		String[] ids = stuId.split(",");
		String studentIds        = "";
		Map<String,Object> param = new HashMap<String, Object>();
		
		for (int i = 0; i < ids.length; i++) {
			studentIds  += ",'"+ids[i]+"'";
		}
		//Connection conn      	 = null;
		param.put("major"		,major);
		param.put("classic"		,classic);
		param.put("stuName"		,name);
		param.put("studyNo"		,studyNo);
		param.put("gradeid"     ,gradeid);
		param.put("examSubId"   ,examSubId);
		param.put("studentIds"	,studentIds.substring(1));
		param.put("branchSchool",branchSchool);
		param.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
		param.put("reportRootPath", request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"examPrint"));
		param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo2.path").getParamValue());
		param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
		//param.put("imageRootPath","/opt/hnjk/common/students");
		try {

			
			String reprotFile    = "";
			if (ExStringUtils.isNotEmpty(stuId)) {
				reprotFile       = URLDecoder.decode(request.getSession().getServletContext().
								   getRealPath(File.separator+"reports"+File.separator+"examPrint"+File.separator+"examCardByStuIds.jasper"),"utf-8");
			}else{
				reprotFile       = URLDecoder.decode(request.getSession().getServletContext().
								   getRealPath(File.separator+"reports"+File.separator+"examPrint"+File.separator+"examCardByCondition.jasper"),"utf-8");
			}
			JasperPrint print 	 = examPrintService.printReport(reprotFile, param, examPrintService.getConn());

			if (null!=print) {
				renderStream(response, print);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印准考证Controller出错{}",e.fillInStackTrace());
			renderHtml(response, "打印准考证出错!"+e.fillInStackTrace());
		}
	}
	/**
	 * 打印考试课程信息-预览
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/print-examcourse-view.html")
	public String examInfoPrintView(HttpServletRequest request,ModelMap model)throws WebException{
		model.put("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		return "/edu3/teaching/examPrint/examInfo-printview";
	}
	/**
	 * 打印考试课程信息
	 * @param request
	 * @param response
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/print-examcourse.html")
	public void examInfoPrint(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String examSubId       = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		JasperPrint jasperPrint= null;//输出的报表
		//Connection conn 	   = null;
		if (ExStringUtils.isNotEmpty(examSubId)) {
			try {
				ExamSub  sub   			= examSubService.get(examSubId);
				//conn                    = examSubService.getConn();;				
		    	String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
		    							  File.separator+"examPrint"+File.separator+"examCourseTime.jasper"),"utf-8");
				File reprot_file        = new File(reprotFile);
				
				map.put("examSubName", sub.getBatchName());
				map.put("examSubId", sub.getResourceid());
				map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				
				//jasperPrint 		    = JasperFillManager.fillReport(reprot_file.getPath(), map, conn); // 填充报表
				jasperPrint		= examSubService.printReport(reprot_file.getPath(), map, examSubService.getConn());
				if (null!=jasperPrint) {
					renderStream(response, jasperPrint);
				}else {
					renderHtml(response,"缺少打印数据！");
				}
				
			} catch (Exception e) {
				logger.error("打印考试课程信息出错：{}"+e.fillInStackTrace());
			}
		}
	}
	/**
	 * 导出考试课程信息
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/examExport/export-examcourse.html")
	public void examInfoExport(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String examSubId       = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		JasperPrint jasperPrint= null;//输出的报表
		//Connection conn 	   = null;
		if (ExStringUtils.isNotEmpty(examSubId)) {
			try {
				ExamSub  sub   			= examSubService.get(examSubId);
				//conn 	  			    = examSubService.getConn();
		    	String reprotFile       = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
		    							  File.separator+"examPrint"+File.separator+"examCourseTime.jasper"),"utf-8");
				File reprot_file        = new File(reprotFile);
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				GUIDUtils.init();			
				String filePath         = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
				//String filePath         = "D:\\aa.xls";
				
				map.put("examSubName", sub.getBatchName());
				map.put("examSubId", sub.getResourceid());
				
				//jasperPrint 		    = JasperFillManager.fillReport(reprot_file.getPath(), map, conn); // 填充报表
				jasperPrint		= examSubService.printReport(reprot_file.getPath(), map, examSubService.getConn());
				
				JRXlsExporter exporter  = new JRXlsExporter();
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
				exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);  
				exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
				exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);  
				exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath); 
				//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.xls"); 
				exporter.exportReport(); 
				
				downloadFile(response,"考试课程信息.xls",filePath,true);
				
			} catch (Exception e) {
				logger.error("导出考试课程信息出错：{}"+e.fillInStackTrace());
			}
		}
	}
	/**
	 * 学生打印准考证-列表
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/print/examCard-student.html")
	public String printExamCardForStudentList(HttpServletRequest request,ModelMap model){
		
		User user 						   = SpringSecurityHelper.getCurrentUser();	
		Map<String,Object> condition       = new HashMap<String, Object>();
		List<ExamSub> examSubs     		   = examSubService.findOpenedExamSub("exam");//获取当前年度的考试批次预约对象
		
		if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT") && !examSubs.isEmpty()){
			if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				String defaultStudentId    = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				StudentInfo info           = studentInfoService.load(defaultStudentId);
				condition.put("status", 2);
				condition.put("examSubId",examSubs.get(0).getResourceid());
				condition.put("studentId",defaultStudentId);
				
				List<StudentLearnPlan> slp = studentLearnPlanService.findStudentLearnPlanByCondition(condition);
				boolean assignSeat         = true; 
				for (StudentLearnPlan plan:slp) {
					Long examType 	  = plan.getExamResults().getExamInfo().getCourse().getExamType();
					String isPractice = plan.getExamResults().getExamInfo().getCourse().getIsPractice();
					if((null==plan.getExamResults().getExamroom()||ExStringUtils.isEmpty(plan.getExamResults().getExamSeatNum()))&&
					   (null!=examType&&examType!=2&&examType!=3&&examType!=5&&examType!=6&&"N".equals(isPractice))){
						assignSeat         = false;
						break;
					}
				}
				model.put("student",info);
				model.put("examSub", examSubs.get(0));
				model.put("assignSeat", assignSeat);
				model.put("studentLearnPlanList", slp);
				model.put("studentLearnPlanListSize", slp.size());
			}
		}else {
			model.put("errorMsg", "考试已结束，不允许打印准考证!");
		}
		
		return "/edu3/teaching/examPrint/examCard-list-student";
	}
	/**
	 * 学生打印准考证-下载准考证
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/examCard/download.html")
	public void printExamCardForStudent(HttpServletRequest request,HttpServletResponse response){
		
		User user 						   = SpringSecurityHelper.getCurrentUser();	
		Map<String,Object> condition       = new HashMap<String, Object>();
		//Connection conn        			   = null;
		if(SpringSecurityHelper.isUserInRole("ROLE_STUDENT")){	
			
			if(null != user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID)){
				String defaultStudentId    = user.getPropertys(UserExtends.USER_EXTENDCODE_DEFAULTROLLID).getExValue();
				//获取当前年度的考试批次预约对象
				List<ExamSub> examSubs     = examSubService.findOpenedExamSub("exam");
				condition.put("examSubId"   ,examSubs.get(0).getResourceid());
				condition.put("studentIds"	,"'"+defaultStudentId+"'");
				condition.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
				condition.put("reportRootPath", request.getSession().getServletContext().getRealPath(File.separator+"reports"+File.separator+"examPrint"));
				condition.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				try {
					setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
					GUIDUtils.init();	
					String filePath        = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".pdf";
					//String filePath        = "D:\\aa.pdf";
					
					String reprotFile      = URLDecoder.decode(request.getSession().getServletContext().
										     getRealPath(File.separator+"reports"+File.separator+"examPrint"+File.separator+"examCardByStuIds.jasper"),"utf-8");
					
					//conn				   = examPrintService.getConn();
	
					//JasperPrint print      = JasperFillManager.fillReport(reprotFile, condition, conn); // 填充报表
					JasperPrint print 	= examPrintService.printReport(reprotFile, condition, examPrintService.getConn());
					JRPdfExporter exporter = new JRPdfExporter();
					exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);   
					exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath); 
					
					//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.pdf"); 
					
					exporter.exportReport(); 
					
					downloadFile(response,"准考证.pdf",filePath,true);
					
				} catch (Exception e) {
					logger.error(" 学生打印准考证出错{}",e.fillInStackTrace());
					renderHtml(response, "打印准考证出错!"+e.fillInStackTrace());
				}
			}
		}
		
	}
	
	/**
	 * 打印导出巡考员试卷交接表-学习中心选择
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examinertransferTable-condition.html")
	public String examinerTransferTableCondition(HttpServletRequest request,ModelMap model){
		
		String flag           = ExStringUtils.trimToEmpty(request.getParameter("flag"));         //打印、导出时选择模板的标识
		String examSubId      = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String OperatingType  = ExStringUtils.trimToEmpty(request.getParameter("operatingType"));//操作方式:打印、导出  
		if (ExStringUtils.isNotEmpty(examSubId)) {
			String hql        = " select distinct(bag.unit) from "+ExamPaperBag.class.getSimpleName()+" bag where bag.isDeleted=0 and bag.examInfo.examSub.resourceid=? order by bag.unit.unitCode";
			List    list  	  = examPaperBagService.findByHql(hql, examSubId);
			model.put("list", list);
		}
		
		model.put("flag", flag);
		model.put("examSubId", examSubId);
		model.put("operatingType", OperatingType);
		
		return "/edu3/teaching/examPrint/examinerTransferTable_condition";
	}
	/**
	 * 打印交接表 --预览
	 * @param request
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/print/transferTable-view.html")
	public String printTransferTableView(HttpServletRequest request,ModelMap model){
		
		model.put("examSubId",ExStringUtils.trimToEmpty(request.getParameter("examSubId")));
		model.put("flag",ExStringUtils.trimToEmpty(request.getParameter("flag")));
		model.put("unitIds",ExStringUtils.trimToEmpty(request.getParameter("unitIds")));
		model.put("isMachineExam",ExStringUtils.trimToEmpty(request.getParameter("isMachineExam")));
		
		return "/edu3/teaching/examPrint/transferTable-printview";
	}
	/**
	 * 打印交接表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/print/transferTable.html")
	public void printTransferTable(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> param = new HashMap<String, Object>();
		String flag 	          = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String examSubId          = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String unitIds            = ExStringUtils.trimToEmpty(request.getParameter("unitIds"));  
		String isMachineExam 	  = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		
		JasperPrint jasperPrint   = null;//输出的报表
		//Connection conn   		  = null;
		if (ExStringUtils.isNotEmpty(examSubId)) {
			try {
				ExamSub  sub   	  = examSubService.get(examSubId);
				//conn			  = examSubService.getConn();
				String reprotFile = "";
				if ("partTransferTable".equals(flag)) {
					reprotFile    = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
							  		File.separator+"examPrint"+File.separator+"partTransferTable.jasper"),"utf-8");
				}else if ("examinerTransferTable".equals(flag)) {
					
					reprotFile    = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					  				File.separator+"examPrint"+File.separator+"examinerTransferTable.jasper"),"utf-8");
				}else if ("reviewOfficerTransferTable".equals(flag)) {
					reprotFile    = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					  				File.separator+"examPrint"+File.separator+"reviewOfficerTransferTable.jasper"),"utf-8");
				}
		    	 
				File reprot_file  = new File(reprotFile);
				
				param.put("examSubName", sub.getBatchName());
				param.put("examSubId", sub.getResourceid());
				param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				//param.put("scutLogoPath","c:\\scut_logo.jpg");
				
				//打印机巡考员试卷交接表 是分学习中心打印
				if ("examinerTransferTable".equals(flag)) { 
					if (ExStringUtils.isNotEmpty(isMachineExam)) {
						param.put("isMachineExam", isMachineExam);
					}
					if (ExStringUtils.isNotEmpty(unitIds)) {
						for (String id:unitIds.split(",")) {
							OrgUnit unit = orgUnitService.load(id);
							String linkMan = ExStringUtils.isEmpty(unit.getLinkman())?unit.getPrincipal():unit.getLinkman();
							param.put("unitName",unit.getUnitCode()+"--"+unit.getUnitShortName()+"("+ExStringUtils.trimToEmpty(linkMan)+" "+ExStringUtils.trimToEmpty(unit.getContectCall())+" "+ExStringUtils.trimToEmpty(unit.getAddress())+")" );
							param.put("unitId", unit.getResourceid());
							
							if (null==jasperPrint) {
								jasperPrint = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
							}else {
								JasperPrint jasperPage = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
								List jsperPageList	   = jasperPage.getPages();
								for (int j = 0; j < jsperPageList.size(); j++) {
									jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
								}
								jasperPage 			   = null;//清除临时报表的内存占用
							}
						}
					}
				}else{
					jasperPrint		= examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
				}
		    	
				if (null!=jasperPrint) {
					renderStream(response, jasperPrint);
				}else {
					renderHtml(response,"缺少打印数据！");
				}
				
			} catch (Exception e) {
				logger.error("打印考试课程信息出错：{}"+e.fillInStackTrace());
			}
		}
	}
	
	/**
	 * 导出交接表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/exam/export/transferTable.html")
	public void exportTransferTable(HttpServletRequest request,HttpServletResponse response){
		
		Map<String, Object> param = new HashMap<String, Object>();
		String flag 	          = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String examSubId          = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String unitIds            = ExStringUtils.trimToEmpty(request.getParameter("unitIds"));  
		String isMachineExam 	  = ExStringUtils.trimToEmpty(request.getParameter("isMachineExam"));
		
		JasperPrint jasperPrint   = null;//输出的报表
		String reprotFile 	      = "";
		String reprotName         = "";

		try {
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();	
			String filePath        = getDistfilepath()+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
			//String filePath        = "D:\\aa.xls";

			ExamSub  sub   	  	   = examSubService.get(examSubId);
			
			
			if ("partTransferTable".equals(flag)) {
				reprotName         = sub.getBatchName()+"本部试卷交接表";
				reprotFile         = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						  			 File.separator+"examPrint"+File.separator+"partTransferTable.jasper"),"utf-8");
				
			}else if ("examinerTransferTable".equals(flag)) {
				reprotName         = sub.getBatchName()+"巡考员试卷交接表";
				reprotFile    	   = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
				  					 File.separator+"examPrint"+File.separator+"examinerTransferTable.jasper"),"utf-8");
				
			}else if ("reviewOfficerTransferTable".equals(flag)) {
				reprotName         = sub.getBatchName()+"阅卷老师、复核人员交接表";
				reprotFile    	   = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
				  					 File.separator+"examPrint"+File.separator+"reviewOfficerTransferTable.jasper"),"utf-8");
			}
			File reprot_file 	   = new File(reprotFile);
			
			param.put("examSubName", sub.getBatchName());
			param.put("examSubId", sub.getResourceid());
			param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
			//打印机巡考员试卷交接表 是分学习中心打印
			if ("examinerTransferTable".equals(flag)) { 
				if (ExStringUtils.isNotEmpty(isMachineExam)) {
					param.put("isMachineExam", isMachineExam);
				}
				if (ExStringUtils.isNotEmpty(unitIds)) {
					for (String id:unitIds.split(",")) {
						OrgUnit unit   = orgUnitService.load(id);
						String linkMan = ExStringUtils.isEmpty(unit.getLinkman())?unit.getPrincipal():unit.getLinkman();
						param.put("unitName",unit.getUnitCode()+"--"+unit.getUnitShortName()+"("+ExStringUtils.trimToEmpty(linkMan)+" "+ExStringUtils.trimToEmpty(unit.getContectCall())+" "+ExStringUtils.trimToEmpty(unit.getAddress())+")" );
						param.put("unitId", unit.getResourceid());
						
						if (null==jasperPrint) {
							jasperPrint = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
						}else {
							JasperPrint jasperPage = examPrintService.printReport(reprot_file.getPath(), param, examPrintService.getConn());
							List jsperPageList	   = jasperPage.getPages();
							for (int j = 0; j < jsperPageList.size(); j++) {
								jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
							}
							jasperPage 			   = null;//清除临时报表的内存占用
						}
					}
				}
			}else{
				jasperPrint = examPrintService.printReport(reprot_file.getPath(), param,  examPrintService.getConn());
			}
	    	
			JRXlsExporter exporter = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);  
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);  
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath);
			//exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,"D:\\aa.xls"); 
			
			exporter.exportReport(); 
			
			downloadFile(response,reprotName+".xls",filePath,true);
			
		} catch (Exception e) {
			logger.error(" 学生打印准考证出错{}",e.fillInStackTrace());
			renderHtml(response, "打印准考证出错!"+e.fillInStackTrace());
		}
	}
	
	/**
	 * 期末机考成绩打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/printfinalonlineexam-view.html")
	public String printFinalOnlineExamResultsView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		model.addAttribute("examSubId", examSubId);
		model.addAttribute("courseId", courseId);
		return "/edu3/recruit/recruitexamlogs/finalonlineexam-printview";
	}
	/**
	 * 期末机考成绩打印
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/exam/examPrint/finalonlineexam-print.html")
	public void finalOnlineExamResultsPrint(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));		
		if (ExStringUtils.isNotEmpty(examSubId) && ExStringUtils.isNotBlank(courseId)) {
			JasperPrint jasperPrint= null;//输出的报表
			//Connection conn = null;
			try {
				ExamSub sub = examSubService.get(examSubId);
				Course course = courseService.get(courseId);				
		    	String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
		    							  File.separator+"examPrint"+File.separator+"finalOnlineExamResults_report.jasper"),"utf-8");
				File reprot_file = new File(reprotFile);				
				
				map.put("examSubId", sub.getResourceid());
				map.put("courseId", course.getResourceid());
				map.put("examSubBatchName", sub.getBatchName());
				map.put("examCourseName", course.getCourseName());
				map.put("exportDate", ExDateUtils.formatDateStr(new Date(), ExDateUtils.PATTREN_DATE));
				//map.put("scoreEncryptionDecryptionUtil", ScoreEncryptionDecryptionUtil.getInstance());
				map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				
				//conn = examSubService.getConn();
				//jasperPrint = JasperFillManager.fillReport(reprot_file.getPath(), map, conn); // 填充报表
				jasperPrint = examSubService.printReport(reprot_file.getPath(), map, examSubService.getConn());
				if (null!=jasperPrint) {
					renderStream(response, jasperPrint);
				}else {
					renderHtml(response,"缺少打印数据！");
				}
				
			} catch (Exception e) {
				logger.error("打印期末机考成绩出错：{}"+e.fillInStackTrace());
			} 
		}
	}
	
	/**
	 * 期末考试成绩打印预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresults-print-view.html")
	public String printExamResultsView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		return "/edu3/teaching/examResult/courseExamResults-printview";
	}
	
	/**
	 * 期末考试成绩检查是否全部提交
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/examresults-check-view.html")
	public void checkExamResultsView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String ,Object> map = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		boolean isallsubmit = true;
		if(ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(teachingPlanCourseId)&&ExStringUtils.isNotBlank(gradeid)){
			Map<String,Object> condition = new HashMap<String, Object>();
			User curUser = SpringSecurityHelper.getCurrentUser();
			TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			condition.put("examSubId",examSubId);
			condition.put("gradeid",gradeid);
			condition.put("teachType",flag);
			condition.put("teachplanid",planCourse.getTeachingPlan().getResourceid());
			condition.put("classesId",classesId);
			String branchschoolid = curUser.getOrgUnit().getResourceid();
			condition.put("branchschoolid",branchschoolid);
			condition.put("courseId", planCourse.getCourse().getResourceid());
			Page objPage = new Page();
			objPage.setOrderBy("unit.unitcode,major.majorcode,stu.studyno ");
			objPage.setOrder(Page.ASC);	
			objPage.setPageSize(1000);
			objPage = teachingJDBCService.findFaceStudyExamResultsVo(objPage, condition);
			List<ExamResultsVo> scoreList = objPage.getResult();
			for (ExamResultsVo examResultsVo : scoreList) {
				if (ExStringUtils.isBlank(examResultsVo.getCheckStatus()) || Integer.parseInt(examResultsVo.getCheckStatus()) < 1) {
					isallsubmit = false;
					break;
				}
			}
			if (isallsubmit) {
				map.put("statusCode", 200);
				map.put("message", "已全部提交成绩！");
			} else {
				map.put("statusCode", 300);
				map.put("message", "全部提交成绩才允许查看打印！");
			}
		} else {
			map.put("statusCode", 300);
			map.put("message", "参数不完整！");
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	
	/**
	 * 期末成绩打印
	 * 广大、汕大使用
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/examresults-print.html")
	public void examResultsPrint(HttpServletRequest request,HttpServletResponse response)throws WebException{
		
		Map<String,Object> map     = new HashMap<String, Object>();
		String examSubId 		   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("teachingPlanCourseId"));
		String teachType           = ExStringUtils.trimToEmpty(request.getParameter("teachType"));//faceTeach,
		String pc_teachType                = ExStringUtils.trim(request.getParameter("flag"));
		String examform                = ExStringUtils.trim(request.getParameter("examform"));
		String operatingType       = ExStringUtils.trim(request.getParameter("operatingType"));
		String gradeid       = ExStringUtils.trim(request.getParameter("gradeid"));
		String isPrint       = ExStringUtils.trim(request.getParameter("isPrint"));// 是否打印
		String isShowUnitCode = request.getParameter("isShowUnitCode");//是否显示教学点单位编码
		String branchSchool        = "";
		String teachingPlanId      = "";
		String creditHour          = "";
		String examClassType       = "";//考核方式
		String classesId  		   = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		ExamInfo examInfo = null;
		int examCourseType = "faceTeach".equals(teachType)?1:0;//网络，面授
		TeachingPlanCourse planCourse = null;
		if(ExStringUtils.isNotBlank(teachingPlanCourseId) && ExStringUtils.isNotBlank(gradeid)){//面授成绩打印
			User curUser = SpringSecurityHelper.getCurrentUser();
			boolean isTeachCentreTeacher = SpringSecurityHelper.isTeachingCentreTeacher(curUser);
			if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType()) 
					|| isTeachCentreTeacher) {
				// 教学中心的老师也能打印成绩
				if(isTeachCentreTeacher){
					Classes cl = classesService.get(classesId);
					branchSchool = cl.getBrSchool().getResourceid();
				}else {
					branchSchool = curUser.getOrgUnit().getResourceid();
				}
			}

			planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
			creditHour = planCourse.getCreditHour()!=null?planCourse.getCreditHour().toString():"";
			examClassType = null == planCourse.getExamClassType() ? "" : JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", planCourse.getExamClassType());
			teachingPlanId = planCourse.getTeachingPlan().getResourceid();

			Map<String, Object> _condition = new HashMap<String, Object>();
			String isMachineExam = Constants.BOOLEAN_NO;
			if("3".equals(examform)){//课程考试形式,期末机考
				//examCourseType = 0;
				isMachineExam = Constants.BOOLEAN_YES;
			}
			_condition.put("schoolId",branchSchool );
			_condition.put("examSubId", examSubId);
			_condition.put("courseId", planCourse.getCourse().getResourceid());
			_condition.put("scoreStyle", planCourse.getScoreStyle());
			_condition.put("examCourseType", examCourseType);
			_condition.put("isMachineExam", isMachineExam);
			examInfo = examInfoService.getExamInfo(_condition);
		}
		
		List<Map<String,Object>> dm = new ArrayList<Map<String,Object>>();
		if (ExStringUtils.isNotEmpty(examSubId) && examInfo!=null) {
			
			//if (ExStringUtils.isBlank(teachType)) teachType = "networkstudy";
			try {
				ExamSub sub 	   = examSubService.get(examSubId);
				Map<String, Object> condition = new HashMap<String, Object>();
				StringBuffer hql         = new StringBuffer(" from "+ExamResults.class.getSimpleName()+" rs where rs.examInfo.examSub.resourceid=:examSubId and rs.examInfo.course.resourceid=:courseId and rs.isDeleted=0 and rs.studentInfo.studentStatus='11' ");
				condition.put("examSubId",examSubId);
				condition.put("courseId", planCourse.getCourse().getResourceid());
				if(ExStringUtils.isNotBlank(branchSchool)){
					hql.append(" and rs.studentInfo.branchSchool.resourceid=:branchSchool ");
					condition.put("branchSchool", branchSchool);
				}
				if(ExStringUtils.isNotBlank(gradeid)){
					hql.append(" and rs.studentInfo.grade.resourceid=:gradeid ");
					condition.put("gradeid", gradeid);
				}
				if(ExStringUtils.isNotBlank(teachingPlanId)){
					hql.append(" and rs.studentInfo.teachingPlan.resourceid=:teachingPlanId ");
					condition.put("teachingPlanId", teachingPlanId);
				}
				if(ExStringUtils.isNotEmpty(classesId)){
					hql.append(" and rs.studentInfo.classes.resourceid = :classesId ");
					condition.put("classesId", classesId);
				}
				
				hql.append(" order by rs.studentInfo.branchSchool.unitCode asc,rs.course.resourceid,rs.studentInfo.studyNo asc ");
				
				//List<ExamResults> list = examResultsService.findByHql(hql, examInfoId);
				List<ExamResults> list = examResultsService.findByHql(hql.toString(), condition);
		    	list 			   	   = examResultsService.calculateExamResultsListIntegratedScore(list, examInfo);
		    	
		    	JasperPrint jasperPrint= null;//输出的报表
				String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
				String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
				String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();

		    	//增加免考
		    	List<StudentInfo> stulist = studentinfoservice.findByHql("from "+StudentInfo.class.getSimpleName()+" stu where stu.classes.resourceid=? and stu.studentStatus in('11','16') and stu.isDeleted = 0",classesId );
				List<String> stuids= new ArrayList<String>();
				for(StudentInfo stu :stulist){						
					stuids.add(stu.getResourceid());
				}
				List<NoExamApply> noexamlist = noexamapplyservice.findByCriteria(Restrictions.in("studentInfo.resourceid", stuids),Restrictions.eq("course.resourceid", planCourse.getCourse().getResourceid()),Restrictions.eq("isDeleted", 0),Restrictions.eq("checkStatus", "1"));
				dm = examResultsService.getMapInfoListByExamResultListForPrint(list,noexamlist,isPrint);

				//成绩比例
				if(examInfo.getExamCourseType()==1){//面授
					map.put("usuallyScore",examInfo.getFacestudyScorePer2()+"");
					map.put("writtenScore",examInfo.getFacestudyScorePer()+"");
				}else {//网络
					map.put("usuallyScore",examInfo.getNetsidestudyScorePer()+"");
					map.put("writtenScore",examInfo.getStudyScorePer()+"");
				}
				
				String jasper = "";
		    	String reprotFile  ="";
		    	String export_jasper = "";
		    	String title = schoolName;
		    	String term = sub.getTerm();
				Long year = sub.getYearInfo().getFirstYear();
		    	
		    	//改成某年某季学期非统考课程期末考试
		    	String batchName = year+"年"+("1".equals(term)?"春季":"秋季");
		    	
				if("10560".equals(schoolCode)){//汕大
					
					//改为班号
					Classes classes = classesService.get(classesId);
					if(classes!=null){
						map.put("planCourseCode", classes.getClassCode());
					}
					dm = examResultsService.getExamResultMap_STDX(map,dm,classesId,"N");
					map.put("joinNum", Integer.parseInt(map.get("joinNum").toString())-noexamlist.size()+"");
					title += schoolConnectName+"学院学生考核成绩记载表";
		    		batchName += "学期";
		    		jasper = ("facestudy".equals(pc_teachType)||"netsidestudy".equals(pc_teachType)?"courseExamResults_facestudy_stdx.jasper":"courseExamResults_facestudy_stdx.jasper");
		    		export_jasper = jasper;
				}else {//默认广大
					title += "成人高等教育考试";
		    		batchName += "课程期末考试";
		    		jasper = ("facestudy".equals(pc_teachType)||"netsidestudy".equals(pc_teachType)?"courseExamResults_facestudy.jasper":"courseExamResults.jasper");
		    		export_jasper = ("facestudy".equals(pc_teachType)||"netsidestudy".equals(pc_teachType)?"courseExamResults_export_facestudy.jasper":"courseExamResults_export.jasper");
				}
				
		    	String unitName    ="";
		    	String classicName ="";
		    	String gradeName   ="";
		    	String majorName   ="";
		    	String classesName ="";
		    	String teachingType = "";
		    	if(list.size()>0){
		    		StudentInfo student = list.get(0).getStudentInfo();
		    		unitName=  student.getBranchSchool().getUnitShortName();
		    		classicName = student.getClassic().getClassicName();
		    		gradeName = student.getGrade().getGradeName();
		    		majorName = student.getMajor().getMajorName();
		    		classesName = student.getClasses().getClassname();
		    		teachingType = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", student.getTeachingType());
		    	}
				
		    	JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dm);
		    	
				if ("export".equals(operatingType)) {
					reprotFile    = File.separator+"reports"+File.separator+"examPrint"+File.separator+export_jasper;
				}else {
					reprotFile    = File.separator+"reports"+File.separator+"examPrint"+File.separator+jasper;
				}
				File reprot_file   = new File(URLDecoder.decode(request.getSession().getServletContext().getRealPath(reprotFile),"utf-8"));				
				
				map.put("examSubId", sub.getResourceid());
				map.put("examInfoId", examInfo.getResourceid());
				map.put("batchName", batchName);
				map.put("courseName", (ExStringUtils.isEmpty(examInfo.getExamCourseCode())?"":examInfo.getExamCourseCode()+"--")+examInfo.getCourse().getCourseName());//课程名称
//				String examType    = examInfo.getCourse().getExamType()!=null?JstlCustomFunction.dictionaryCode2Value("CodeCourseExamType", examInfo.getCourse().getExamType().toString()):"";
				map.put("examType", examClassType);//考核方式
				String examDate    = "";
				try {
					if(examInfo.getExamStartTime()!=null){
						examDate = ExDateUtils.formatDateStr(examInfo.getExamStartTime(), "yyyy年MM月dd日");
					}
				} catch (ParseException e) {
				}
				map.put("printDate", ExDateUtils.formatDateStr(new Date(),2));
				map.put("examDate", examDate);//考试日期
				map.put("creditHour", creditHour);
				map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				map.put("unit",unitName);
				map.put("grade",gradeName);
				map.put("major",majorName);
				map.put("classic",classicName);
				map.put("form",teachingType);
				map.put("className",classesName);
				map.put("classes",classesName);
				map.put("school",title);//学校名称
				map.put("courseHour",creditHour);//学时
				
				//map.put("scutLogoPath", "c:\\scut_logo.jpg");
				jasperPrint 	 = JasperFillManager.fillReport(reprot_file.getPath(), map, dataSource); // 填充报表
				
				if ("export".equals(operatingType)) {
					String fileType = request.getParameter("fileType");
					GUIDUtils.init();			
					//String filePath         = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
					//File disFile   = new File();
					String filePath         = getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls";
					JRAbstractExporter exporter = null;
					if ("pdf".equalsIgnoreCase(fileType)) {
						exporter = new JRPdfExporter();
					} else if ("doc".equalsIgnoreCase(fileType) || "docx".equalsIgnoreCase(fileType)) {
						exporter = new JRDocxExporter();
					} else if ("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)) {
						exporter =  new JRXlsExporter();
					}
					//JRXlsExporter exporter  = new JRXlsExporter();
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
					UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "5", UserOperationLogs.PRINT,"录入成绩后打印总评成绩：参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
					downloadFile(response,examInfo.getCourse().getCourseName()+"总评成绩."+fileType,filePath,true);
				}else {
					if (null!=jasperPrint) {
						renderStream(response, jasperPrint);
					}else {
						renderHtml(response,"缺少打印数据！");
					}
				}
			} catch (Exception e) {
				String msg = "打印期末成绩出错：{}"+e.fillInStackTrace();
				logger.error(msg);
				renderHtml(response, "<script>alert("+"\""+msg+"\""+")</script>");

			} 
		} 
	}


	/**
	 * 异步获得选定年级下的班级
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/ajaxRefreshClasses.html")
	public void ajaxRefreshClasses(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String brschool = ExStringUtils.trimToEmpty(request.getParameter("brschool"));
		String class_id = ExStringUtils.trimToEmpty(request.getParameter("class_id"));
		Map<String,Object> data = new HashMap<String, Object>(0);
		try{
			Map<String,Object> condition = new HashMap<String, Object>(0);
			if(ExStringUtils.isNotEmpty(grade)){
				condition.put("gradeid", grade);
			}
			if(ExStringUtils.isNotEmpty(brschool)){
				condition.put("brSchoolid", brschool);
			}
			List<Classes> classes = classesService.findClassesByCondition(condition);
			StringBuffer classesSelectHtml = new StringBuffer("");
			classesSelectHtml.append("<option value=\"\">请选择</option>");
			for (Classes c : classes) {
				classesSelectHtml.append("<option value=\""+c.getResourceid()+(class_id.equals(c.getResourceid())?"\" selected =\"selected\" ":"\"")+" >"+c.getClassname()+" </option> ");
			}
			data.put("result", 200);
			data.put("classes", classesSelectHtml);
		}catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取班级出错。");
		}
		
		renderJson(response,JsonUtils.mapToJson(data));
	}
	
	/**
	 * 异步获得选定年级下的班级（补考）
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/ajaxRefreshClassesMakeup.html")
	public void ajaxRefreshClassesMakeup(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String brschool = ExStringUtils.trimToEmpty(request.getParameter("brschool"));
		String class_id = ExStringUtils.trimToEmpty(request.getParameter("class_id"));
		String major_id = ExStringUtils.trimToEmpty(request.getParameter("major"));
		Map<String,Object> data = new HashMap<String, Object>(0);
		try{
			Map<String,Object> condition = new HashMap<String, Object>(0);
			if(ExStringUtils.isNotEmpty(grade)){
				condition.put("gradeid", grade);
			}
			if(ExStringUtils.isNotEmpty(brschool)){
				condition.put("brSchoolid", brschool);
			}
			if(ExStringUtils.isNotEmpty(major_id)){
				condition.put("majorid", major_id);
			}
			List<Classes> classes = classesService.findClassesByCondition(condition);
			StringBuffer classesSelectHtml = new StringBuffer("");
			classesSelectHtml.append("<option value=\"\">请选择</option>");
			for (Classes c : classes) {																													
				classesSelectHtml.append("<option value=\""+c.getResourceid()+(class_id.equals(c.getResourceid())?"\" selected =\"selected\" ":"\"")+" >"+c.getClassname() + " -- " + c.getBrSchool().getUnitShortName() +" </option> ");
			}
			data.put("result", 200);
			data.put("classes", classesSelectHtml);
		}catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取班级出错。");
		}
		
		renderJson(response,JsonUtils.mapToJson(data));
	}
	
	/**
	 * 异步获得选定年级下的考试批次
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/ajaxExamSubByGrade.html")
	public void ajaxExamSubByGrade(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String examSub = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		Map<String,Object> data = new HashMap<String, Object>(0);
		try{
			List examSubs = examSubService.getExamSubByGradeId(grade);
			StringBuffer examSubSelectHtml = new StringBuffer("");
			examSubSelectHtml.append("<option value=\"\">请选择</option>");
//			for (Map sub : examSubs) {	
			for (int i=0; i<examSubs.size(); i++) {
				Map sub = (Map) examSubs.get(i);
				examSubSelectHtml.append("<option value=\""+sub.get("resourceid").toString()
						+(examSub.equals(sub.get("resourceid").toString())?"\" selected =\"selected\" ":"\"")+" >"+sub.get("batchname").toString()+" </option> ");
			}
			data.put("result", 200);
			data.put("examSubs", examSubSelectHtml);
		}catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取班级出错。");
		}
		
		renderJson(response,JsonUtils.mapToJson(data));
	}
	
	/**
	 * 异步获得选定考试批次下的考试类型
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/ajaxExamTypeByExamSub.html")
	public void ajaxExamTypeByExamSub(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSub"));
		String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		Map<String,Object> data = new HashMap<String, Object>(0);
		try{
			ExamSub examSub = examSubService.get(examSubId);
			examType = examSub.getExamType();
//			StringBuffer examSubSelectHtml = new StringBuffer("");
//			examSubSelectHtml.append("<option value=\"\">请选择</option>");
////			for (Map sub : examSubs) {	
//			for (int i=0; i<examSubs.size(); i++) {
//				Map sub = (Map) examSubs.get(i);
//				examSubSelectHtml.append("<option value=\""+sub.get("resourceid").toString()
//						+(examSub.equals(sub.get("resourceid").toString())?"\" selected =\"selected\" ":"\"")+" >"+sub.get("batchname").toString()+" </option> ");
//			}
			data.put("result", 200);
			data.put("examType", examType);
		}catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取班级出错。");
		}
		
		renderJson(response,JsonUtils.mapToJson(data));
	}
	
	/**
	 * 异步获得选定年级下的专业（补考）
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/ajaxRefreshMajorsMakeup.html")
	public void ajaxRefreshMajorMakeup(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));
		String brschool = ExStringUtils.trimToEmpty(request.getParameter("brschool"));
		String major_id = ExStringUtils.trimToEmpty(request.getParameter("major"));
		Map<String,Object> data = new HashMap<String, Object>(0);
		try{
			Map<String,Object> condition = new HashMap<String, Object>(0);
			if(ExStringUtils.isNotEmpty(grade)){
				condition.put("grade", grade);
			}
			if(ExStringUtils.isNotEmpty(brschool)){
				condition.put("branchSchool", brschool);
			}
			if(ExStringUtils.isNotEmpty(major_id)){
				condition.put("majorid", major_id);
			}
			
			List<Major>  Major = majorService.findAllMajorByGradeAndBrMakeup(condition);
			//过滤重复的
			StringBuffer classesSelectHtml = new StringBuffer("");
			classesSelectHtml.append("<option value=\"\">请选择</option>");
			for(Major m:Major){
				classesSelectHtml.append("<option value=\""+m.getResourceid()+(major_id.equals(m.getResourceid())?"\" selected =\"selected\" ":"\"")+" >"+m.getMajorCode()+"-"+m.getMajorName()+" </option> ");
			}
			data.put("result", 200);
			data.put("majorList", classesSelectHtml);
		}catch (Exception e) {
			data.put("result", 300);
			data.put("msg", "异步获取专业出错。");
		}
		
		renderJson(response,JsonUtils.mapToJson(data));
	}
	
	/**
	 * 期末考试成绩打印预览(补考)
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/failexamresults-print-view.html")
	public String printFailExamResultsView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
//		String examInfoId = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
//		String flag = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String operatingType = ExStringUtils.trim(request.getParameter("operatingType"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String majorid = ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String courseid = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
//		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String form               = ExStringUtils.trimToEmpty(request.getParameter("form"));
		String coursestatusid = ExStringUtils.trimToEmpty(request.getParameter("coursestatusid"));
		String isShowUnitCode = request.getParameter("isShowUnitCode");
		String totalNum	= request.getParameter("totalNum");
		//当前补考批次
//		String examSubId = "";
//		Map<String, Object> paramMap = new HashMap<String, Object>();
//		String batchName = ExamResultMakeupUtil.getBatchName(CalendarUtil.getYear(),CalendarUtil.getMonthInt());
//		if(!"年".equals(batchName.substring(4, 5))){
//			StringBuffer sb = new StringBuffer(batchName);
//			sb.insert(4, "年");
//			batchName = sb.toString();
//		}
//		logger.info("考试批次:{}", batchName);
//		System.out.println("考试批次:"+ batchName);
//		paramMap.put("batchName", batchName);
//		List<ExamSub> examList = examSubService.findByCondition(paramMap);//当前补考考试批次
//		
//		if(null != examList && examList.size()>0){
//			examSubId = examList.get(0).getResourceid();
//		}
				
		model.addAttribute("examSubId", examSubId);
//		model.addAttribute("examInfoId", examInfoId);
		model.addAttribute("teachingPlanCourseId", teachingPlanCourseId);
		model.addAttribute("gradeid", gradeid);
//		model.addAttribute("flag", flag);
		model.addAttribute("operatingType", operatingType);
		model.addAttribute("classesId",classesId);
		model.addAttribute("branchSchool",branchSchool);
		model.addAttribute("majorId",majorid);
		model.addAttribute("courseId",courseid);
		model.addAttribute("form",form);
		model.addAttribute("totalNum",totalNum);
		model.addAttribute("coursestatusid",coursestatusid);
		model.addAttribute("isShowUnitCode",isShowUnitCode);
		return "/edu3/teaching/examResult/courseFailExamResults-printview";
	}
	/**
	 * 期末成绩打印(补考)
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/failexamresults-print.html") 
	public void failexamResultsPrint(HttpServletRequest request,HttpServletResponse response)throws WebException{
		
		Map<String,Object> map     = new HashMap<String, Object>();
		String examSubId 		   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String teachingPlanCourseId = ExStringUtils.trimToEmpty(request.getParameter("plansourceid"));
//		String teachType           = ExStringUtils.trimToEmpty(request.getParameter("teachType"));	
//		String flag                = ExStringUtils.trim(request.getParameter("flag"));
		String operatingType       = ExStringUtils.trim(request.getParameter("operatingType"));
		String branchSchool        = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
		String creditHour          = "";
		String form                  = ExStringUtils.trimToEmpty(request.getParameter("form"));
		String totalNum				= ExStringUtils.trimToEmpty(request.getParameter("totalNum"));
		String majorid  		   = ExStringUtils.trimToEmpty(request.getParameter("majorid"));
		String unitid  		   = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
		String courseid  		   = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		String gradeid  		   = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classesId  		   = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String coursestatusid  		   = ExStringUtils.trimToEmpty(request.getParameter("coursestatusid"));
		String isShowUnitCode = request.getParameter("isShowUnitCode");//是否显示教学点单位编码
		TeachingPlanCourseStatus teachingPlanCourseStatus = teachingPlanCourseStatusService.get(coursestatusid);
		
		Course course = courseService.get(courseid);
		String teachType 			 = teachingPlanCourseStatus.getTeachType();//"facestudy" ;
//		if(null != examList && examList.size()>0){
		if (ExStringUtils.isNotEmpty(examSubId)) {
//			examSubId = examList.get(0).getResourceid();
			//String teachingPlanId = "";
			String examClassType = "";
			int examCourseType = teachType.contains("net")?0:1;
			ExamInfo examInfo = null;
			
			if(ExStringUtils.isNotBlank(teachingPlanCourseId)&&ExStringUtils.isNotBlank(gradeid)){//面授成绩打印
				User curUser = SpringSecurityHelper.getCurrentUser();
				TeachingPlanCourse planCourse = teachingPlanCourseService.get(teachingPlanCourseId);
				if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
					branchSchool = curUser.getOrgUnit().getResourceid();
					creditHour = planCourse.getCreditHour()!=null?planCourse.getCreditHour().toString():"";
					examClassType = null == planCourse.getExamClassType() ? "" : JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", planCourse.getExamClassType()); 
					//teachingPlanId = planCourse.getTeachingPlan().getResourceid();
				}else{
					branchSchool = unitid;
					creditHour = planCourse.getCreditHour()!=null?planCourse.getCreditHour().toString():"";
					examClassType = null == planCourse.getExamClassType() ? "" : JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", planCourse.getExamClassType()); 
					//teachingPlanId = planCourse.getTeachingPlan().getResourceid();
				}	
				Map<String, Object> _condition = new HashMap<String, Object>();
				_condition.put("schoolId",branchSchool );
				_condition.put("examSubId", examSubId);
				_condition.put("courseId", planCourse.getCourse().getResourceid());
				_condition.put("scoreStyle", planCourse.getScoreStyle());
				_condition.put("examCourseType", examCourseType);
				examInfo = examInfoService.getExamInfo(_condition);
			}
			
			List<Map<String,Object>> dm= new ArrayList<Map<String,Object>>();
			if (ExStringUtils.isNotEmpty(examSubId) ) {
				
				JasperPrint jasperPrint= null;//输出的报表
//				if (ExStringUtils.isBlank(teachType)) teachType = "networkstudy";
				try {
					ExamSub sub 	   = examSubService.get(examSubId);
					
					Map<String, Object> condition = new HashMap<String, Object>();
					String hql         = " from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0 and rs.examsubId=:examsubId";
					condition.put("examsubId", examSubId);
					if(ExStringUtils.isNotBlank(branchSchool)){
						hql += " and rs.studentInfo.branchSchool.resourceid=:branchSchool ";
						condition.put("branchSchool", branchSchool);
					}else{
						branchSchool = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
						if(ExStringUtils.isNotBlank(branchSchool)){
							hql += " and rs.studentInfo.branchSchool.resourceid=:branchSchool ";
							condition.put("branchSchool", branchSchool);
						}
					}
					if(ExStringUtils.isNotBlank(gradeid)){
						hql += " and rs.studentInfo.grade.resourceid=:gradeid ";
						condition.put("gradeid", gradeid);
					}
					if(ExStringUtils.isNotBlank(courseid)){
						hql += " and rs.course.resourceid=:courseid ";
						condition.put("courseid", courseid);
					}
					if(ExStringUtils.isNotBlank(majorid)){
						hql += " and rs.studentInfo.major.resourceid=:majorid ";
						condition.put("majorid", majorid);
					}
					if(ExStringUtils.isNotEmpty(classesId)){
						hql += " and rs.studentInfo.classes.resourceid = :classesId ";
						condition.put("classesId", classesId);
					}
//					hql += " group by rs.studentInfo.resourceid,rs.studentInfo.studyNo ";
					hql += " order by rs.studentInfo.grade.gradeName,rs.studentInfo.major.majorCode,rs.studentInfo.studyNo asc ";
					
					//List<ExamResults> list = examResultsService.findByHql(hql, examInfoId);
					List<ExamResults> list = examResultsService.findByHql(hql, condition);
//			    	list 			   	   = examResultsService.calculateExamResultsListIntegratedScore(list, examInfo, teachType);
//					list 			   	   = examResultsService.calculateExamResultsListIntegratedScore(list, sub.getFacestudyScorePer());
//			    	String longType = "";
					//去除重复记录
					List<ExamResults> list2 = new ArrayList<ExamResults>(list.size());
					for (ExamResults rs : list) {
						boolean add = true;
						for (ExamResults rs2 : list2) {
							if (rs.getStudentInfo().getResourceid().equals(rs2.getStudentInfo().getResourceid())) {
								add = false;
								break;
							}
						}
						if (add) {
							list2.add(rs);
						}
					}
					
					String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
					String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
					String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
					//成绩比例
					if(examInfo.getExamCourseType()==1){//面授
						map.put("usuallyScore",examInfo.getFacestudyScorePer2()+"");
						map.put("writtenScore",examInfo.getFacestudyScorePer()+"");
					}else {//网络
						map.put("usuallyScore",examInfo.getNetsidestudyScorePer()+"");
						map.put("writtenScore",examInfo.getStudyScorePer()+"");
					}
					
					String jasper = "";
			    	String reprotFile  ="";
			    	String export_jasper = "";
			    	String title = schoolName;
			    	String term = sub.getTerm();
					Long year = sub.getYearInfo().getFirstYear();
			    	
			    	//改成某年某季学期非统考课程期末考试
			    	String batchName = year+"年"+("1".equals(term)?"春季":"秋季");
			    	dm = examResultsService.getMapInfoListByExamResultListForPrint(list2, null, "N");

			    	String unitName    ="";
			    	String classicName ="";
			    	String gradeName   ="";
			    	String majorName   ="";
			    	String classesName ="";
			    	String teachingType = "";
			    	if(list.size()>0){
			    		StudentInfo student = list.get(0).getStudentInfo();
			    		unitName=  student.getBranchSchool().getUnitShortName();
			    		classicName = student.getClassic().getClassicName();
			    		gradeName = student.getGrade().getGradeName();
			    		majorName = student.getMajor().getMajorName();
			    		//classesName = student.getClasses().getClassname();
			    		teachingType = JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", student.getTeachingType());
			    		classesName = student.getBranchSchool().getUnitShortName()+" "+gradeName+" "+majorName+" "+teachingType+" "+classicName;
			    	}
			    	if("10560".equals(schoolCode)){//汕大
			    		//改为班号
						Classes classes = classesService.get(classesId);
						if(classes!=null){
							map.put("planCourseCode", classes.getClassCode());
						}
						examResultsService.getExamResultMap_STDX(map,dm,classesId,"Y");

						title += schoolConnectName+"学院";
						map.put("title2", "补考、补修成绩记载表");
			    		batchName += "学期";
			    		jasper = "courseExamResults_facestudy_makeup_stdx.jasper";
			    		export_jasper = jasper;
					}else {//默认广大
						title += "成人高等教育考试";
						//由广州大学 函授部提出  修改为与系统查询的考试批次名称一致的叫法   20160612 
						batchName = sub.getBatchName();
			    		jasper = "courseExamResults_facestudy_makeup.jasper";
			    		export_jasper = "courseExamResults_export_facestudy_makeup.jasper";
			    		if("us".equals(form)){//统考
			    			jasper = "courseExamUSResults.jasper";
			    			export_jasper = "courseExamUSResults.jasper";
				    	}
					}
			    	JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(dm);
			    	if ("export".equals(operatingType)) {
						reprotFile    = File.separator+"reports"+File.separator+"examPrint"+File.separator+export_jasper;
					}else {
						reprotFile    = File.separator+"reports"+File.separator+"examPrint"+File.separator+jasper;
					}
					File reprot_file   = new File(URLDecoder.decode(request.getSession().getServletContext().getRealPath(reprotFile),"utf-8"));				
					
					map.put("school", title);//学校名称
					map.put("examSubId", sub.getResourceid());
					map.put("batchName", batchName);//考试批次名称
					map.put("courseName", "《"+course.getCourseName()+"》");//课程名称
					map.put("examType", examClassType);//考试形式
					String examDate    = "";
					try {
						if(examInfo!=null && examInfo.getExamStartTime()!=null){
							examDate = ExDateUtils.formatDateStr(examInfo.getExamStartTime(), "yyyy年MM月dd日");
						}else {
							examDate = ExDateUtils.formatDateStr(sub.getStartTime(), "yyyy年MM月dd日");
						}
					} catch (ParseException e) {
					}
					map.put("examDate", examDate);//考试日期
					map.put("creditHour", creditHour);
					map.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
					map.put("unit",unitName);
					map.put("grade",gradeName);
					map.put("major",majorName);
					map.put("classic",classicName);
					map.put("form",teachingType);
					map.put("classes",classesName);
					map.put("className",classesName);
					
					jasperPrint 	 = JasperFillManager.fillReport(reprot_file.getPath(), map, dataSource); // 填充报表
					
					if ("export".equals(operatingType)) {
						String fileType = request.getParameter("fileType");
						GUIDUtils.init();
						//String filePath         = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+File.separator+"exportfiles"+File.separator+GUIDUtils.buildMd5GUID(false)+".xls";
						File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
						String filePath         = disFile.getAbsolutePath();
						JRAbstractExporter exporter = null;
						if ("pdf".equalsIgnoreCase(fileType)) {
							exporter = new JRPdfExporter();
						} else if ("doc".equalsIgnoreCase(fileType) || "docx".equalsIgnoreCase(fileType)) {
							exporter = new JRDocxExporter();
						} else if ("xls".equalsIgnoreCase(fileType) || "xlsx".equalsIgnoreCase(fileType)) {
							exporter =  new JRXlsExporter();
						}
						//JRXlsExporter exporter  = new JRXlsExporter();
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
							logger.error(e.getMessage());
						}
						
						downloadFile(response,course.getCourseName()+"补考总评成绩."+fileType,filePath,true);
					}else {
						if (null!=jasperPrint) {
							renderStream(response, jasperPrint);
						}else {
							renderHtml(response,"缺少打印数据！");
						}
					}
				} catch (Exception e) {
					String msg = "打印期末成绩出错：{}"+e.fillInStackTrace();
					logger.error(msg);
					renderHtml(response, "<script>alert("+"\""+msg+"\""+")</script>");
				} 
			}else{
				renderHtml(response, "未找到考试课程信息。");
			} 
		} else {
			String msg = "没有当前补考批次";
			renderHtml(response, "<script>alert("+"\""+msg+"\""+")</script>");
		}
	}
	
	
	/**
	 * 导出补考名单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/teaching/result/nonexamination-export.html")
	public void exportPersonalExamResults(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String branchSchool   = 	ExStringUtils.trimToEmpty(request.getParameter("branchSchool")); // 教学点
		String gradeId 		  = 	ExStringUtils.trimToEmpty(request.getParameter("gradeId")); //年级
		String major		  = 	ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String examSubId	  = 	ExStringUtils.trimToEmpty(request.getParameter("examSubId"));//批次
		String classesId	  =		ExStringUtils.trimToEmpty(request.getParameter("classesId"));//班级
		String teachType 			 = "facestudy" ;
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));//课程ID
		String classicId 			 = ExStringUtils.trimToEmpty(request.getParameter("classicId"));//层次ID
//		courseId 					 = ExStringUtils.getEncodeURIComponentByTwice(courseId);
		String yearId = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
		String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		String isPass 			     = ExStringUtils.trimToEmpty(request.getParameter("isPass"));
		String finalPass 			     = ExStringUtils.trimToEmpty(request.getParameter("finalPass"));
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		User 	curUser		  =		SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			branchSchool            = curUser.getUnitId();
		}
		try {
			Map<String,Object> condition = new HashMap<String, Object>(0);
			//if (ExStringUtils.isNotEmpty(courseName))  	    condition.put("courseName", courseName);
			if (ExStringUtils.isNotEmpty(gradeId)) {
				condition.put("gradeId", gradeId);
			}
			if (ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if (ExStringUtils.isNotEmpty(classesId)) {
				condition.put("classesId", classesId);
			}
			if (ExStringUtils.isNotEmpty(examSubId)) {
				condition.put("examSubId", examSubId);
			}
			if (ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if (ExStringUtils.isNotEmpty(classicId)) {
				condition.put("classic", classicId);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("term", term);
			}
			if (ExStringUtils.isNotEmpty(yearId)) {
				condition.put("yearId", yearId);
			}
			if (ExStringUtils.isNotEmpty(examType)) {
				condition.put("examType", examType);
			}
			if (ExStringUtils.isNotEmpty(isPass)) {
				condition.put("isPass", isPass);
			}
			if (ExStringUtils.isNotEmpty(finalPass)) {
				condition.put("finalPass", finalPass);
			}
			condition.put("teachType", teachType);
			condition.put("export", "Y");
			List<FailExamStudentVo> list = examPrintService.nonexamExportByClasses(condition);//数据	
			int num=1;
			for(FailExamStudentVo vo:list){
				vo.setSortNum(String.valueOf(num));
				vo.setTerm(Tools.getDigitalTerm(vo.getFirstyear(),vo.getTerm()));
				num++;
			}
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//导出
			GUIDUtils.init();
			File excelFile = null;
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");

			LinkedHashMap<String,Object> templateMap = new LinkedHashMap<String, Object>();//设置模板
			
			Classes cl 			= 	classesService.get(classesId);
			String classesName 	= 	null == cl ? "" : cl.getClassname();
			ExamSub	sub 		= 	examSubService.get(examSubId);
			String subName 		= 	null == sub ? "" : sub.getBatchName();

			//模板文件路径
			String templateFilepathString = "nonexamination_students.xls";
			if ("10601".equals(schoolCode)) {
				int sheetNum = 0;
				int classNum = 0;
				templateMap.put("sortNum", "序号");
				templateMap.put("studyNo", "学号");
				templateMap.put("studentName", "姓名");
				templateMap.put("integratedScore", "上次成绩");
				templateMap.put("nextScore", "补考成绩");
				templateMap.put("signer", "查阅签名");
				list.add(new FailExamStudentVo());
				FailExamStudentVo preStudentVo = list.get(0);
				List<LinkedHashMap<String,Object>> tempList = new ArrayList<LinkedHashMap<String, Object>>();
				int sortNum = 0;
				for (FailExamStudentVo exportVo : list){
					if (!(preStudentVo.getClassName().equals(exportVo.getClassName()) && preStudentVo.getCourseName().equals(exportVo.getCourseName()))) {
						exportExcelService.initParmasByExcelConfigInfo(disFile,exportExcelService.getExcelConfigInfo(templateMap),tempList,null);
						exportExcelService.getModelToExcel().setDynamicTitle(true);
						exportExcelService.getModelToExcel().setDynamicTitleMap(templateMap);
						exportExcelService.getModelToExcel().setRowHeight(400);//设置行高
						exportExcelService.getModelToExcel().setHeader(preStudentVo.getClassName()+"\n"+"《"+preStudentVo.getCourseName()+"》补考名单");
						exportExcelService.getModelToExcel().setSecondHeader("补考时间："+ExStringUtils.appendSpace(ExStringUtils.nvl2(preStudentVo.getExamStartTime(),ExDateUtils.formatDateStr(preStudentVo.getExamStartTime(),1),preStudentVo.getTerm()),15)
								+"  任课教师："+preStudentVo.getLecturerName()+"\n"+"填报时间："+ExStringUtils.appendSpace("",15)+"  教研室主任：");
						exportExcelService.getModelToExcel().setSheet(++sheetNum, preStudentVo.getClassName()+"_"+(++classNum));
						disFile = exportExcelService.getModelToExcel().getExcelfileByDynamicTitle();
						sortNum = 0;
						tempList.clear();
						if (!preStudentVo.getClassName().equals(exportVo.getClassName())) {
							classNum = 0;
						}
					}

					LinkedHashMap<String,Object> voMap = new LinkedHashMap<String, Object>();
					voMap.put("sortNum",++sortNum);
					voMap.put("studyNo",exportVo.getStudyNo());
					voMap.put("studentName",exportVo.getStudentName());
					voMap.put("integratedScore",exportVo.getIntegratedscore());
					voMap.put("nextScore",exportVo.getNextScore());
					voMap.put("signer","");
					tempList.add(voMap);
					preStudentVo = exportVo;
				}
				excelFile = disFile;
			} else {
				WritableCellFormat titleFormat3 = new WritableCellFormat();
				titleFormat3.setWrap(true);
				titleFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
				templateMap.put("exportName", classesName+"补考名单"); //标题
				templateMap.put("examSub", subName);
				templateMap.put("bkNum", null != list ? list.size()+"" : "0");
				exportExcelService.initParmasByfile(disFile, "exportNonExamination", list,null);
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				exportExcelService.getModelToExcel().setNormolCellFormat(titleFormat3);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			}

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, classesName+"补考名单.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	/**
	 * 检查补考成绩是否全部提交
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/faileExamresults-check-view.html")
	public void checkFailExamResultsView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Map<String ,Object> map = new HashMap<String, Object>();
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String planCourceId = ExStringUtils.trimToEmpty(request.getParameter("planCourceId"));
		String classesId = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		boolean isallsubmit = true;
		String isAllow = "N";
		User curUser = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())
				|| SpringSecurityHelper.isTeachingCentreTeacher(curUser)) {
			isAllow  = "Y";
		}
		if(ExStringUtils.isNotBlank(examSubId)&&ExStringUtils.isNotBlank(planCourceId)&&ExStringUtils.isNotBlank(classesId)
				&& ExStringUtils.isNotBlank(courseId)){
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("examSubId", examSubId);
			condition.put("planCourceId", planCourceId);
			condition.put("classesId", classesId);
			condition.put("courseId", courseId);
			StringBuffer hql = new StringBuffer("");
			hql.append(" from "+ExamResults.class.getSimpleName()+" rs where rs.isDeleted=0 and rs.examsubId=:examSubId and rs.majorCourseId=:planCourceId ");
			hql.append(" and rs.course.resourceid=:courseId and rs.studentInfo.classes.resourceid=:classesId ");
			List<ExamResults> failExamResultsList = examResultsService.findByHql(hql.toString(), condition);
			if(failExamResultsList != null && failExamResultsList.size() > 0) {
				for (ExamResults failExamResults : failExamResultsList) {
					if (ExStringUtils.isBlank(failExamResults.getCheckStatus()) || Integer.parseInt(failExamResults.getCheckStatus()) < 1) {
						isallsubmit = false;
						break;
					}
				}
			} else {
				isallsubmit = false;
			}
			if (isallsubmit) {
				map.put("statusCode", 200);
				map.put("isAllow", isAllow);
				map.put("message", "已全部提交成绩！");
			} else {
				map.put("statusCode", 300);
				map.put("message", "全部提交成绩才允许查看打印！");
			}
		} else {
			map.put("statusCode", 300);
			map.put("message", "参数不完整！");
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}

	/**
	 * 导出补考名单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/edu3/teaching/result/nonexamination-export-gzdx.html")
	public void gzdxExportPersonalExamResults(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String branchSchool   = 	ExStringUtils.trimToEmpty(request.getParameter("branchSchool")); // 教学点
		String gradeId 		  = 	ExStringUtils.trimToEmpty(request.getParameter("gradeId")); //年级
		String major		  = 	ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String examSubId	  = 	ExStringUtils.trimToEmpty(request.getParameter("examSubId"));//批次
		String classesId	  =		ExStringUtils.trimToEmpty(request.getParameter("classesId"));//班级
		String teachType 			 = "facestudy" ;
		String courseId 			 = ExStringUtils.trimToEmpty(request.getParameter("courseId"));//课程ID
		String classicId 			 = ExStringUtils.trimToEmpty(request.getParameter("classicId"));//层次ID
//		courseId 					 = ExStringUtils.getEncodeURIComponentByTwice(courseId);
		String yearId = ExStringUtils.trimToEmpty(request.getParameter("yearId"));
		String term = ExStringUtils.trimToEmpty(request.getParameter("term"));
		String examType = ExStringUtils.trimToEmpty(request.getParameter("examType"));
		String isPass 			     = ExStringUtils.trimToEmpty(request.getParameter("isPass"));
		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		User 	curUser		  =		SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			branchSchool            = curUser.getUnitId();
		}
		try {
			Map<String,Object> condition = new HashMap<String, Object>(0);
			//if (ExStringUtils.isNotEmpty(courseName))  	    condition.put("courseName", courseName);
			if (ExStringUtils.isNotEmpty(gradeId)) {
				condition.put("gradeId", gradeId);
			}
			if (ExStringUtils.isNotEmpty(major)) {
				condition.put("major", major);
			}
			if (ExStringUtils.isNotEmpty(classesId)) {
				condition.put("classesId", classesId);
			}
			if (ExStringUtils.isNotEmpty(examSubId)) {
				condition.put("examSubId", examSubId);
			}
			if (ExStringUtils.isNotEmpty(branchSchool)) {
				condition.put("branchSchool", branchSchool);
			}
			if (ExStringUtils.isNotEmpty(classicId)) {
				condition.put("classicId", classicId);
			}
			if (ExStringUtils.isNotEmpty(courseId)) {
				condition.put("courseId", courseId);
			}
			if (ExStringUtils.isNotEmpty(yearId)) {
				condition.put("yearId", yearId);
			}
			if (ExStringUtils.isNotEmpty(term)) {
				condition.put("term", term);
			}
			if (ExStringUtils.isNotEmpty(examType)) {
				condition.put("examType", examType);
			}
			if (ExStringUtils.isNotEmpty(isPass)) {
				condition.put("isPass", isPass);
			}
			condition.put("teachType", teachType);
			
			List<NonexaminationExportVoForGZDX> list = examPrintService.nonexamExportByClassesForGZDX(condition);//数据

			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//导出
			GUIDUtils.init();
			File excelFile = null;
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");

			LinkedHashMap<String,Object> templateMap = new LinkedHashMap<String, Object>();//设置模板
			
			Classes cl 			= 	classesService.get(classesId);
			String classesName 	= 	null == cl ? "" : cl.getClassname();
			ExamSub	sub 		= 	examSubService.get(examSubId);
			String subName 		= 	null == sub ? "" : sub.getBatchName();
			String examTypeName = JstlCustomFunction.dictionaryCode2Value("ExamResult", examType);


			WritableCellFormat titleFormat3 = new WritableCellFormat();
			titleFormat3.setWrap(true);
			titleFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);

			int sheetNum = 0;
			//模板文件路径
			String templateFilepathString = "nonexamination_students_forgzdx.xls";
			if ("10601".equals(schoolCode)) {//桂林医个性化模版
				//templateFilepathString = "nonexamination_students_forglyxy.xls";
				list.add(new NonexaminationExportVoForGZDX());
				NonexaminationExportVoForGZDX preStudentVo = list.get(0);
				List<LinkedHashMap<String,Object>> tempList = new ArrayList<LinkedHashMap<String, Object>>();
				templateMap.put("sortNum", "序号");
				templateMap.put("courseName", "科目");
				templateMap.put("studentName", "补考学员");
				templateMap.put("studentSum", "人数");
				templateMap.put("examType", "类别");
				int sortNum = 0;
				for (NonexaminationExportVoForGZDX exportVo : list){

					if (!preStudentVo.getClassesName().equals(exportVo.getClassesName())) {
						exportExcelService.initParmasByExcelConfigInfo(disFile,exportExcelService.getExcelConfigInfo(templateMap),tempList,null);
						exportExcelService.getModelToExcel().setDynamicTitle(true);
						exportExcelService.getModelToExcel().setDynamicTitleMap(templateMap);
						exportExcelService.getModelToExcel().setRowHeight(400);//设置行高
						exportExcelService.getModelToExcel().setHeader(preStudentVo.getClassesName()+examTypeName);
						exportExcelService.getModelToExcel().setSecondHeader(preStudentVo.getTerm());
						exportExcelService.getModelToExcel().setSheet(++sheetNum, preStudentVo.getClassesName());
						disFile = exportExcelService.getModelToExcel().getExcelfileByDynamicTitle();
						sortNum = 0;
						tempList.clear();
					}
					LinkedHashMap<String ,Object> mapInfo = new LinkedHashMap<String, Object>();
					mapInfo.put("sortNum",++sortNum);
					mapInfo.put("courseName",exportVo.getCourseName());
					mapInfo.put("studentName",exportVo.getStuNames());
					mapInfo.put("studentSum",exportVo.getStudentSum());
					mapInfo.put("examType",exportVo.getExamType());
					tempList.add(mapInfo);
					preStudentVo = exportVo;
				}
				excelFile = disFile;
			} else {//默认使用广大模版
				templateMap.put("exportName", classesName+"补考名单"); //标题
				templateMap.put("examSub", subName);
				templateMap.put("bkNum", null != list ? list.size()+"" : "0");
				exportExcelService.initParmasByfile(disFile, "exportNonExaminationForGZDX", list,null);
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				exportExcelService.getModelToExcel().setNormolCellFormat(titleFormat3);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			}

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, classesName+"补考名单.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 根据教学点和考试批次的学期来查询学生的上课学期
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/exam/plan/exportCourseAndTeacher.html")
	public void exportCourseAndTeacher(HttpServletRequest request, HttpServletResponse response)throws Exception{
		try {
			
			String term       = ExStringUtils.trimToEmpty(request.getParameter("term"));
			String unitid = ExStringUtils.trimToEmpty(request.getParameter("unitid"));
			Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			//ExamSub es = examSubService.get(examSubId);
			if(ExStringUtils.isNotEmpty(term)) {
				condition.put("term", term);
			}
			if(ExStringUtils.isNotEmpty(unitid)) {
				condition.put("unitid", unitid);
			}
			long starttime = System.currentTimeMillis();			
			List<ExportCourseAndTeacherVo> list  = examPrintService.exportCourseAndTeacher(condition);
			long endtime = System.currentTimeMillis();
			logger.info("exportCourseAndTeacher:  starttime : "+starttime);
			logger.info("exportCourseAndTeacher:  endtime : "+endtime);
			logger.info("exportCourseAndTeacher:  finaltime : "+(endtime-starttime));
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			String templateFilepathString = "exportCourseAndTeacherVo.xls";
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板	
			templateMap.put("exportName", JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", term)+"_开课课程"); //标题
			WritableCellFormat titleFormat3 = new WritableCellFormat();
			titleFormat3.setWrap(true);
			titleFormat3.setBorder(jxl.format.Border.ALL,jxl.format.BorderLineStyle.THIN);
			exportExcelService.initParmasByfile(disFile, "exportCourseAndTeacherVoid", list,null);
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			exportExcelService.getModelToExcel().setNormolCellFormat(titleFormat3);

			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件

			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "教学计划开课课程.xls", excelFile.getAbsolutePath(),true);
			
		} catch (Exception e) {
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
		
	}
	/**
	 * 考生签到表预览
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/studentSignature-print-view.html")
	public String printStudentSignatureView(HttpServletRequest request,HttpServletResponse response,ModelMap model){
		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		return "/edu3/teaching/examResult/ExamResults-StudentSignature-printview";
	}
	@RequestMapping("/edu3/teaching/result/printStudentSignature.html")
	public void printStudentSignature(HttpServletRequest request,HttpServletResponse response)throws WebException{
		String gradeid = ExStringUtils.trimToEmpty(request.getParameter("gradeid"));
		String classesid = ExStringUtils.trimToEmpty(request.getParameter("classesid"));
		String courseid = ExStringUtils.trimToEmpty(request.getParameter("courseid"));
		//String courseName = request.getParameter("courseName");
		Map<String,Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(courseid)) {
			condition.put("courseid", courseid);
		}
		if(ExStringUtils.isNotBlank(gradeid)) {
			condition.put("gradeid", gradeid);
		}
		if(ExStringUtils.isNotBlank(classesid)) {
			condition.put("classesid", classesid);
		}
		//if(ExStringUtils.isNotBlank(courseName)) condition.put("courseName", courseName);
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		//查找待考试的学生：1 学籍正常  2 未申请缓考  3 未申请免考
		List<StudentSignatureVo> stus =studentInfoService.findStudentSignature(condition);
		int count = stus.size();
		int row = 25;
		int column = 2;
		int pageNum=1;
		//column = getColumn(count);
		pageNum = getPageNum(count, row,column);
		List<Map<String, Object>> volist = new ArrayList<Map<String,Object>>();
		for(int i=0;i<pageNum;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			List<StudentSignatureVo> stulist=tableInit(row,i,column);
			for(int j=0;j<stulist.size();j++){
				if(count>j+i*row*column){
					stulist.get(j).setStudyNo1(stus.get(j+i*row*column).getStudyNo1());
					stulist.get(j).setStudentName1(stus.get(j+i*row*column).getStudentName1());
				}
				if(count>j+row+i*row*column){
					stulist.get(j).setStudyNo2(stus.get(j+row+i*row*column).getStudyNo1());
					stulist.get(j).setStudentName2(stus.get(j+row+i*row*column).getStudentName1());
				}
				if((count>j+row*2+i*row*column)&&column==3){
					stulist.get(j).setStudyNo3(stus.get(j+row*2+i*row*column).getStudyNo1());
					stulist.get(j).setStudentName3(stus.get(j+row*2+i*row*column).getStudentName1());
				}
			}
			map.put("datalist", stulist);
			Classes c = classesService.get(classesid);
			map.put("classesName", c.getClassname());
			map.put("gradeName", c.getGrade().getGradeName());
			map.put("classicName", c.getClassic().getClassicName());
			map.put("unitName", c.getBrSchool().getUnitName());
			map.put("majorName",c.getMajor().getMajorName());
			map.put("schoolName", schoolName);
			Course co = courseService.get(courseid);
			map.put("courseName", co.getCourseName());
			volist.add(map);
		}
		
//		//表格初始化
//		List<StudentSignatureVo> stulist = tableInit(row,pageNum,column);
//		List<StudentSignatureVo> stulist2 = new ArrayList<StudentSignatureVo>();
//		//填充数据
//		//size 人数小于25，大于25小于50，大于50小75，大于75
//		if(count>0&&count<=row){//小于25
//			for(int i=0;i<count;i++){
//				stulist.get(i).setStudyNo1(stus.get(i).getStudyNo1());
//			}
//		}else if(count>row&&count<=row*column){//大于25小于50
//			for(int i=0;i<row;i++){
//				stulist.get(i).setStudyNo1(stus.get(i).getStudyNo1());
//				if(count>i+row){
//					stulist.get(i).setStudyNo2(stus.get(i+row).getStudyNo1());
//				}
//			}
//		}else if(count>row*column&&count<=row*(column+1)){//大于50小75
//			for(int i=0;i<row;i++){
//				stulist.get(i).setStudyNo1(stus.get(i).getStudyNo1());
//				if(count>i+row){
//					stulist.get(i).setStudyNo2(stus.get(i+row).getStudyNo1());
//				}
//				if(count>i+row*column){
//					stulist.get(i).setStudyNo3(stus.get(i+row*column).getStudyNo1());
//				}
//			}
//		}else if(count>row*(column+1)&&count<=row*(column+2)){//大于75人
//			for(int i=0;i<row;i++){
//				stulist.get(i).setStudyNo1(stus.get(i).getStudyNo1());
//				if(count>i+row){
//					stulist.get(i).setStudyNo2(stus.get(i+row).getStudyNo1());
//				}
//			}
//			pageNum++;
//			stulist2 = tableInit(row,pageNum,column);
//			for(int i=0;i<row;i++){
//				stulist2.get(i).setStudyNo1(stus.get(i+row*column).getStudyNo1());
//				if(count>i+row*(column+1)){
//					stulist2.get(i).setStudyNo2(stus.get(i+row*(column+1)).getStudyNo1());
//				}
//			}
//		}
//		
//		
		try {
			String reportPath ="";
			JasperPrint jasperPrint = null; // 打印
			
			//if((count>50&&count<=75)||count>100){//三列打印
			//	reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"studentSignature2.jasper";
			//}else{//两列打印
				reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"studentSignature.jasper";
			//}
			// 报表文件
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			// logo路径
			String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();			
			
			for(int i=0;i<volist.size();i++){
				Map<String, Object> printParam = new HashMap<String, Object>();	
				Map<String, Object> tmplist = volist.get(i);
				List<StudentSignatureVo> datalist = (List<StudentSignatureVo>) volist.get(i).get("datalist");
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datalist);
				printParam.put("logoPath", logoPath);
				printParam.put("classesName", tmplist.get("classesName"));			
				printParam.put("gradeName", tmplist.get("gradeName"));
				printParam.put("classicName", tmplist.get("classicName"));
				printParam.put("unitName", tmplist.get("unitName"));
				printParam.put("majorName", tmplist.get("majorName"));
				printParam.put("courseName", tmplist.get("courseName"));
				printParam.put("schoolName", tmplist.get("schoolName"));
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0){
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, printParam, dataSource);
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}
				else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
				}
			
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @param row 行
	 * @param pageNum 第几页（1、2、3）
	 * @param column 拆分列
	 * @return
	 */
	public List<StudentSignatureVo> tableInit(int row,int pageNum,int column){
		List<StudentSignatureVo> list = new ArrayList<StudentSignatureVo>();
		for(int i=0;i<row;i++){
			StudentSignatureVo tmp = new StudentSignatureVo("",i+1+row*column*pageNum,row);
			list.add(tmp);
		}
		return list;
	}
	public int getPageNum(int size,int row,int column){
		int pageNum=1;
		int divisor = size/(row*column);
		int remainder=size%(row*column);
		if(divisor>0){
			pageNum=pageNum+divisor;
			if(remainder==0){
				pageNum--;
			}
		}
		return pageNum;
	}
	public int getColumn(int size){
		int column=2;
		if((size>50&&size<=75)||size>100){//1 50-75之间以及大于100时，分成三列
			column=3;
		}
		return column;
	}
	
	/**
	 * 跳转到打印补考签到表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/result/failStudentSignature-print-view.html") 
	public String printFailexamCourseView(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws ParseException{
		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		return "/edu3/teaching/examResult/examResults-failStudentSignature-printview";
	}
	
	
	@RequestMapping("/edu3/teaching/result/printFailStudentSignature.html")
	public void printFailStudentSignature(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> condition = new HashMap<String, Object>();
		String gradeId 				 = ExStringUtils.trimToEmpty(request.getParameter("gradeId")); 
		String classesId			 = ExStringUtils.trimToEmpty(request.getParameter("classesId"));
		String major       = ExStringUtils.trimToEmpty(request.getParameter("major"));
		String branchschoolid               = ExStringUtils.trimToEmpty(request.getParameter("branchschoolid"));
		String courseId           = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String  examSubId          = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));	

		if(ExStringUtils.isNotEmpty(gradeId)) {
			condition.put("gradeId",gradeId);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major",major);
		}
		if(ExStringUtils.isNotEmpty(branchschoolid)) {
			condition.put("branchschoolid",branchschoolid);
		}
		if(ExStringUtils.isNotEmpty(courseId)) {
			condition.put("courseId",courseId);
		}
		if(ExStringUtils.isNotEmpty(classesId)) {
			condition.put("classesId",classesId);
		}
		if(ExStringUtils.isNotEmpty(examSubId)) {
			condition.put("examSubId",examSubId);
		}
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		try {
			//查找待考试的学生：和录入学生一样的查询条件
			List<StudentSignatureVo> stus = teachingJDBCService.findFailStudentSignature(condition);		
		
			int count = stus.size();
			int row = 25;
			int column = 2;
			int pageNum=1;
			column = getColumn(count);
			pageNum = getPageNum(count, row,column);
			List<Map<String, Object>> volist = new ArrayList<Map<String,Object>>();
			for(int i=0;i<pageNum;i++){
				Map<String, Object> map = new HashMap<String, Object>();
				List<StudentSignatureVo> stulist=tableInit(row,i,column);
				for(int j=0;j<stulist.size();j++){
					if(count>j+i*row*column){
						stulist.get(j).setStudyNo1(stus.get(j+i*row*column).getStudyNo1());
					}
					if(count>j+row+i*row*column){
						stulist.get(j).setStudyNo2(stus.get(j+row+i*row*column).getStudyNo1());
						
					}
					if((count>j+row*2+i*row*column)&&column==3){
						stulist.get(j).setStudyNo3(stus.get(j+row*2+i*row*column).getStudyNo1());
					}
				}
				map.put("datalist", stulist);
				Classes c = classesService.get(classesId);
				map.put("classesName", c.getClassname());
				map.put("gradeName", c.getGrade().getGradeName());
				map.put("classicName", c.getClassic().getClassicName());
				map.put("unitName", c.getBrSchool().getUnitName());
				map.put("majorName",c.getMajor().getMajorName());
				Course co = courseService.get(courseId);
				map.put("courseName", co.getCourseName());
				map.put("schoolName", schoolName);
				volist.add(map);
			}
			
			String reportPath ="";
			JasperPrint jasperPrint = null; // 打印
			
			if((count>50&&count<=75)||count>100){//三列打印
				reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"studentSignature2.jasper";
			}else{//两列打印
				reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"studentSignature.jasper";
			}
			// 报表文件
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			// logo路径
			String logoPath = CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue();			
			
			for(int i=0;i<volist.size();i++){
				Map<String, Object> printParam = new HashMap<String, Object>();	
				Map<String, Object> tmplist = volist.get(i);
				List<StudentSignatureVo> datalist = (List<StudentSignatureVo>) volist.get(i).get("datalist");
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(datalist);
				printParam.put("logoPath", logoPath);
				printParam.put("classesName", tmplist.get("classesName"));			
				printParam.put("gradeName", tmplist.get("gradeName"));
				printParam.put("classicName", tmplist.get("classicName"));
				printParam.put("unitName", tmplist.get("unitName"));
				printParam.put("majorName", tmplist.get("majorName"));
				printParam.put("courseName", tmplist.get("courseName"));
				printParam.put("schoolName", tmplist.get("schoolName"));
				if (null!=jasperPrint&&jasperPrint.getPages().size()>0){
					JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, printParam, dataSource);
					List jsperPageList=jasperPage.getPages();
					for (int j = 0; j < jsperPageList.size(); j++) {
						jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
					}
					jasperPage = null;//清除临时报表的内存占用
				}
				else {
					jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
				}
			
			}
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 跳转到打印补考考试签到表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
//	@RequestMapping("/edu3/teaching/result/failStudentSignature-print-view1.html") 
	@RequestMapping(value = "/edu3/teaching/result/failStudentSignature-print-view1.html", method = RequestMethod.POST)
	public String printFailStudentSignature1(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws ParseException{
		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		model.addAttribute("teachType", "facestudy");		
		return "/edu3/teaching/examResult/examResults-failStudentSignature-printview1";
	}
	
	/**
	 * 打印补考考试签到表
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/result/printFailExamSignature1.html")
	public void printFailStudentSignature1(HttpServletRequest request,HttpServletResponse response)throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String branchSchool 		 = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String classesids			 = ExStringUtils.trimToEmpty(request.getParameter("classesids"));
		String courseids             = ExStringUtils.trimToEmpty(request.getParameter("courseids")); 
		String plansourceids		 = ExStringUtils.trimToEmpty(request.getParameter("plansourceids"));
		String type			 = ExStringUtils.trimToEmpty(request.getParameter("type"));
		
		User curUser 		 = SpringSecurityHelper.getCurrentUser();
		if (CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(curUser.getOrgUnit().getUnitType())) {
			condition.put("isBranchSchool", "Y");
			condition.put("branchSchool", curUser.getOrgUnit().getResourceid());
//			classesCondition = " and cl.ORGUNITID='" + curUser.getOrgUnit().getResourceid() + "'";
		}	
		// 判断是否顶级机构人员
		boolean isTop = false;
		List<User> users = userService.findByHql("from "+User.class.getSimpleName()+" u where u.orgUnit.unitCode=?", "UNIT_ROOT");
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			if (user.getResourceid().equals(curUser.getResourceid())) {
				isTop = true;
				break;
			}
		}
		
		if (!isTop) {
			// 判断是否教务员
			boolean isJwy = false;
			//是否成绩查询
			boolean isCjcx = false;
			Set<Role> roles = curUser.getRoles();
			for (Role role : roles) {
				if ("ROLE_BRS_STUDENTSTATUS".equals(role.getRoleCode())) {
					isJwy = true;
					break;
				}else if ("ROLE_queryscore".equals(role.getRoleCode())) {
					isCjcx = true;
				}
			}
			if (isJwy || isCjcx) {
				if(!isJwy){
					condition.put("isAllow", "N");
				}else {// 教务员录入自己负责的教学站班级的课程成绩
					condition.put("jwyId", curUser.getResourceid());
				}
				
			} else {
				// 登分老师录入自己负责的班级的课程成绩
				condition.put("teachId", curUser.getResourceid());
			}
			if(null!=curUser.getOrgUnit()){
				if(!SpringSecurityHelper.isTeachingCentreTeacher(curUser)){
					branchSchool = curUser.getOrgUnit().getResourceid();
				}
			}
		}
		try {
			if (type!=null&& "checked".equals(type)) {
				if (courseids!=null&&!"".equals(courseids)) {
					String courseidsVo="'";
					courseidsVo +=courseids.replace(",", "','");
					courseidsVo+="'";
					condition.put("courseidsVo",courseidsVo);
				}
				if (plansourceids!=null&&!"".equals(plansourceids)) {
					String plansourceidsVo="'";
					plansourceidsVo +=plansourceids.replace(",", "','");
					plansourceidsVo+="'";
					condition.put("plansourceidsVo",plansourceidsVo);
				}
				/*if (gradeids!=null&&!gradeids.equals("")) {
					String gradeidsVo="'";
					gradeidsVo +=gradeids.replace(",", "','");
					gradeidsVo+="'";
					condition.put("gradeidsVo",gradeidsVo);
				}
				if (majorids!=null&&!majorids.equals("")) {
					String majoridsVo="'";
					majoridsVo +=majorids.replace(",", "','");
					majoridsVo+="'";
					condition.put("majoridsVo",majoridsVo);
				}
				if (unitids!=null&&!unitids.equals("")) {
					String unitidsVo="'";
					unitidsVo +=unitids.replace(",", "','");
					unitidsVo+="'";
					condition.put("unitidsVo",unitidsVo);
				}*/
				if (classesids!=null&&!"".equals(classesids)) {
					String classesidsVo="'";
					classesidsVo +=classesids.replace(",", "','");
					classesidsVo+="'";
					condition.put("classesidsVo",classesidsVo);
				}
			}
			
//			List<FailExamStudentVo> failExamStudentVoList=new ArrayList<FailExamStudentVo>();
			//查找待考试的学生：和录入学生一样的查询条件
			List<Map<String, Object>> resultList = teachingJDBCService.getFailStudentSignatureToPrint(condition);
			if (resultList!=null&&resultList.size()>0) {
				for (int i = 0; i < resultList.size(); i++) {
					Map<String, Object> map=resultList.get(i);
					String son=(String) map.get("stuno");
//					JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", term);
					String examclasstype=JstlCustomFunction.dictionaryCode2Value("CodeExamClassType", (String)map.get("examclasstype"));
					//ghfn:dictCode2Val('CodeCourseTermType',t.courseterm
					String coursetermname=JstlCustomFunction.dictionaryCode2Value("CodeCourseTermType", (String)map.get("courseterm"));
					String[] sons=son.split(",");
					String studyno="";
					for (int j = 0; j < sons.length; j++) {
						if (j==sons.length-1) {
							studyno+=sons[j]+"                ";
						}else{
						studyno+=sons[j]+"                ,";
						}
					}
					resultList.get(i).put("coursetermname", coursetermname);
					resultList.get(i).put("examclasstype", examclasstype);
					resultList.get(i).put("studyno", studyno);
					resultList.get(i).put("sortnum", i+1);
				}
//				String  titleStrbuf =(String) resultList.get(0).get("batchname");
				
				String reportPath ="";
				JasperPrint jasperPrint = null; // 打印
			    reportPath = File.separator+"reports"+File.separator+"studentinfo"+File.separator+"falseStudentSingure.jasper";
				// 报表文件
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
				
				
				for(int i=0;i<resultList.size();i++){
					Map<String, Object> printParam = new HashMap<String, Object>();	
					Map<String, Object> tmplist = resultList.get(i);
//					List<Map<String, Object>> datalist = (List<Map<String, Object>>) resultList.get(i).get("datalist");
					JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(resultList);
					printParam.put("sortnum", tmplist.get("sortnum"));			
					printParam.put("classesname", tmplist.get("classesname"));
					printParam.put("classicname", tmplist.get("classicname"));
					printParam.put("teachername", tmplist.get("teachername"));
					printParam.put("counts", tmplist.get("counts"));
					printParam.put("studyno", tmplist.get("studyno"));
					printParam.put("examtime", tmplist.get("examtime"));
					printParam.put("examplace", tmplist.get("examplace"));
					printParam.put("batchname", tmplist.get("batchname"));
					printParam.put("examclasstype", tmplist.get("examclasstype"));
//					if (null!=jasperPrint&&jasperPrint.getPages().size()>0&&!resultList.get(i-1).get("coursetermname").equals(tmplist.get("coursetermname"))){
//						JasperPrint jasperPage = JasperFillManager.fillReport(reprotFile, printParam, dataSource);
//						List jsperPageList=jasperPage.getPages();
//						for (int j = 0; j < jsperPageList.size(); j++) {
//							jasperPrint.addPage((JRPrintPage) jsperPageList.get(j));
//						}
//						jasperPage = null;//清除临时报表的内存占用
//					}
//					else {
						jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
//					}
				
				}
				if (null!=jasperPrint) {
					renderStream(response, jasperPrint);
				}else {
					renderHtml(response,"缺少打印数据！");
				}
				
			}else{
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
