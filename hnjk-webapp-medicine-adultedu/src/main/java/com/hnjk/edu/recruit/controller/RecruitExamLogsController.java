package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WriteException;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.BaseSecurityCodeUtils;
import com.hnjk.core.foundation.utils.BigDecimalUtil;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
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
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.learning.model.CourseExam;
import com.hnjk.edu.learning.model.CourseExamPaperDetails;
import com.hnjk.edu.learning.service.ICourseExamService;
import com.hnjk.edu.recruit.model.RecruitExamLogs;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IRecruitExamLogsService;
import com.hnjk.edu.recruit.service.IRecruitJDBCService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamResults;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.extend.plugin.excel.config.MergeCellsParam;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.fileconvert.DocumentToRtfConvert;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 入学考试日志记录管理
 * <code>RecruitExamLogsController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-7-20 上午11:37:37
 * @see 
 * @version 1.0
 */
@Controller
public class RecruitExamLogsController extends FileUploadAndDownloadSupportController {
	private static final long serialVersionUID = 2631474323466220694L;

	@Autowired
	@Qualifier("recruitExamLogsService")
	private IRecruitExamLogsService recruitExamLogsService;
	
//	@Autowired
//	@Qualifier("studentLearnPlanService")
//	private IStudentLearnPlanService studentLearnPlanService;
	
	@Autowired
	@Qualifier("recruitJDBCService")
	private IRecruitJDBCService recruitJDBCService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("courseExamService")
	private ICourseExamService courseExamService;
	
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;
	
	/**
	 * 入学考试日志记录列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexamlogs/list.html")
	public String listRecruitExamLogs(HttpServletRequest request, Page objPage,ModelMap model) throws WebException {
		objPage.setOrderBy("brSchool,recruitExamPlan,courseName,status,enrolleeInfo.examCertificateNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String courseName = ExStringUtils.trimToEmpty(request.getParameter("courseName"));
		String brSchool = request.getParameter("brSchool");
		String recruitExamPlanId = request.getParameter("recruitExamPlanId");
		String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String status = ExStringUtils.trimToEmpty(request.getParameter("status"));
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			brSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrSchool", true);
		}
		
		if(ExStringUtils.isNotBlank(courseName)){
			condition.put("courseName", courseName);
		}
		if(ExStringUtils.isNotBlank(name)){
			condition.put("name", name);
		}
		if(ExStringUtils.isNotBlank(examCertificateNo)){
			condition.put("examCertificateNo", examCertificateNo);
		}
		if(ExStringUtils.isNotEmpty(brSchool)){
			condition.put("brSchool", brSchool);
		}
		if(ExStringUtils.isNotEmpty(recruitExamPlanId)){
			condition.put("recruitExamPlanId", recruitExamPlanId);
		}
		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", Integer.parseInt(status));
		}
		//condition.put("notstatus", -1);
		condition.put("isEntranceExam", Constants.BOOLEAN_YES);
		
		Page recruitExamLogsPage = recruitExamLogsService.findRecruitExamLogsByCondition(condition, objPage);
		model.addAttribute("recruitExamLogsPage", recruitExamLogsPage);
		model.addAttribute("condition", condition);
		return "/edu3/recruit/recruitexamlogs/recruitexamlogs-list";
	}
	/**
	 * 编辑
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexamlogs/input.html")
	public String editRecruitExamLogs(String resourceid,ModelMap model) throws WebException{		
		if(ExStringUtils.isNotBlank(resourceid)){ //-----编辑
			RecruitExamLogs recruitExamLogs = recruitExamLogsService.get(resourceid);	
			model.addAttribute("recruitExamLogs", recruitExamLogs);
		}		
		return "/edu3/recruit/recruitexamlogs/recruitexamlogs-form";
	}	
	/**
	 * 保存
	 * @param recruitExamLogs
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexamlogs/save.html")
	public void saveRecruitExamLogs(RecruitExamLogs recruitExamLogs,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(recruitExamLogs.getResourceid())){			
				RecruitExamLogs log = recruitExamLogsService.get(recruitExamLogs.getResourceid());
				log.setStartTime(recruitExamLogs.getStartTime());
				log.setEndTime(recruitExamLogs.getEndTime());
				recruitExamLogsService.update(log);
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("navTabId", "RES_LEARNING_COURSEEXAMRULES_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/recruit/recruitexamlogs/input.html?resourceid="+recruitExamLogs.getResourceid());
			}
		} catch (Exception e) {
			logger.error("延长考试时间出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	//DEL
	/**
	 * 强制交卷
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/recruit/recruitexamlogs/hand.html")
	public void handRecruitExamLogs(String resourceid,String from, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){	
				recruitExamLogsService.handPapers(resourceid.split("\\,"),from);							
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				if(ExStringUtils.isNotBlank(from) && "online".equals(from)){
					map.put("forward", request.getContextPath()+"/edu3/teaching/recruitexamlogs/list.html");
				} else {
					map.put("forward", request.getContextPath()+"/edu3/recruit/recruitexamlogs/list.html");
				}
			}
		} catch (Exception e) {
			logger.error("强制交卷出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	*/
	/**
	 * 继续考试
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexamlogs/continueexam.html")
	public void continueRecruitExamLogs(String resourceid,String from,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){		
				recruitExamLogsService.continueExam(resourceid.split("\\,"),from);								
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				if(ExStringUtils.isNotBlank(from) && "online".equals(from)){
					map.put("forward", request.getContextPath()+"/edu3/teaching/recruitexamlogs/list.html");
				} else {
					map.put("forward", request.getContextPath()+"/edu3/recruit/recruitexamlogs/list.html");
				}				
			}
		} catch (Exception e) {
			logger.error("设置继续考试出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 重考
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexamlogs/reexam.html")
	public void reexamRecruitExamLogs(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				recruitExamLogsService.reExam(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("forward", request.getContextPath()+"/edu3/recruit/recruitexamlogs/list.html");
			}
		} catch (Exception e) {
			logger.error("设置重考出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	//DEL
	/**
	 * 网上考试状态
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	/*
	@RequestMapping("/edu3/teaching/recruitexamlogs/list.html")
	public String listOnlineRecruitExamLogs(HttpServletRequest request, Page objPage,ModelMap model) throws WebException {
		objPage.setOrderBy("examInfo.course.courseName,status,studentInfo.studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		
		String courseId = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examSubId = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool = request.getParameter("branchSchool");
		String studyNo = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));
		String status = ExStringUtils.trimToEmpty(request.getParameter("status"));
		String examStartScore = ExStringUtils.trimToEmpty(request.getParameter("examStartScore"));
		String examEndScore = ExStringUtils.trimToEmpty(request.getParameter("examEndScore"));
		String statType = ExStringUtils.trimToEmpty(request.getParameter("statType"));
			
		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrSchool", true);		
			//是否屏蔽成绩参数
			model.addAttribute("isShowScore", CacheAppManager.getSysConfigurationByCode("teaching.brschool.onlineexam.score.switch").getParamValue());
		}		
		if(ExStringUtils.isNotBlank(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotBlank(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", Integer.parseInt(status));
		}
		condition.put("isEntranceExam", Constants.BOOLEAN_NO);	
		condition.put("isMachineExam", Constants.BOOLEAN_NO);			
		condition.put("statType", statType);
		
		if(ExStringUtils.isBlank(statType) || "1".equals(statType) ||"2".equals(statType)){
			if("1".equals(statType)){
				condition.put("examEndScore", 59.0);
			} else if("2".equals(statType)){
				examStartScore = ExStringUtils.defaultIfEmpty(examStartScore, "60.0");
				examEndScore = ExStringUtils.defaultIfEmpty(examEndScore, "100.0");
				condition.put("examStartScore", Double.parseDouble(examStartScore));
				condition.put("examEndScore", Double.parseDouble(examEndScore));
			}
			Page recruitExamLogsPage = recruitExamLogsService.findRecruitExamLogsByCondition(condition, objPage);
			model.addAttribute("recruitExamLogsPage", recruitExamLogsPage);		
		} else {
			Map<String,Object> values = new HashMap<String, Object>();
			String queryHql = " and plan.examResults.writtenScore is null and plan.examResults.examAbnormity='0' ";			
			if(ExStringUtils.isNotBlank(studentName)){
				values.put("name", studentName);
			}
			if(ExStringUtils.isNotBlank(studyNo)){
				values.put("studyNo", studyNo);
			}
			if(ExStringUtils.isNotEmpty(branchSchool)){
				values.put("branchSchool", branchSchool);
			}
			if(ExStringUtils.isNotBlank(courseId)){
				//queryHql += " and plan.examInfo.course.resourceid=:course ";
				values.put("examCourseId", courseId);
			}
			if(ExStringUtils.isNotBlank(examSubId)){
				//queryHql += " and plan.examInfo.examSub.resourceid=:examSubId ";
				values.put("examSubId", examSubId);
			}
			values.put("status", 2);
			if("3".equals(statType)){					
				queryHql += " and not exists (from "+ExamResults.class.getSimpleName()+" r where r.isDeleted=0 and r.studentInfo.resourceid=plan.studentInfo.resourceid and r.examInfo.resourceid=plan.examInfo.resourceid and r.resourceid<>plan.examResults.resourceid ) ";
				values.put("queryHQL", queryHql);	
			} else if("4".equals(statType)){
				queryHql += " and exists (from "+ExamResults.class.getSimpleName()+" r where r.isDeleted=0 and r.studentInfo.resourceid=plan.studentInfo.resourceid and r.examInfo.resourceid=plan.examInfo.resourceid and r.resourceid<>plan.examResults.resourceid ) ";
				values.put("queryHQL", queryHql);
			}
			Page page = studentLearnPlanService.findStudentLearnPlanByCondition(values,objPage);
			model.addAttribute("recruitExamLogsPage", page);
		}	
		model.addAttribute("condition", condition);
		return "/edu3/recruit/recruitexamlogs/online-recruitexamlogs-list";
	}
	*/
	/**
	 * 重新考试
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/recruitexamlogs/reexam.html")
	public void reexamOnlineRecruitExamLogs(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				recruitExamLogsService.reOnlineExam(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("forward", request.getContextPath()+"/edu3/teaching/recruitexamlogs/list.html");
			}
		} catch (Exception e) {
			logger.error("设置重考出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 期末机考场次名
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/finalexam/machineexamname/list.html")
	public void listMachineExamName(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> param   = new HashMap<String, Object>();
		List<JsonModel> jsonList   = new ArrayList<JsonModel>();
		StringBuffer hql       	   = new StringBuffer();
		String examSubId 		   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String courseId  		   = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		try {
			if (ExStringUtils.isNotBlank(examSubId)) {
				param.put("isDeleted", 0);
				param.put("isMachineExam",Constants.BOOLEAN_YES);
				param.put("examSubId",examSubId);
			
				hql.append(" from "+ExamInfo.class.getSimpleName()+" info where info.isDeleted=:isDeleted and info.isMachineExam=:isMachineExam and info.examSub.resourceid=:examSubId" );
				if (ExStringUtils.isNotBlank(courseId)) {
					hql.append(" and info.course.resourceid=:courseId");
					param.put("courseId",courseId);
				}
				List<ExamInfo> list    = examInfoService.findByHql(hql.toString(), param);
				
				for (ExamInfo info:list) {
					JsonModel model 			 = new JsonModel();
					StringBuffer machineExamName = new StringBuffer(info.getCourse().getCourseName()+"(");;
					if (null!=info.getMachineExamName()) {
						machineExamName.append(info.getMachineExamName());
					}else {
						machineExamName.append(ExDateUtils.formatDateStr(info.getExamStartTime(), ExDateUtils.PATTREN_DATE_TIME));
						machineExamName.append(" 至 ");
						machineExamName.append(ExDateUtils.formatDateStr(info.getExamEndTime(), ExDateUtils.PATTREN_DATE_TIME));
					}
					machineExamName.append(")");
					model.setKey(info.getResourceid());
					model.setValue(machineExamName.toString());
					jsonList.add(model);
				}
			}
		} catch (ParseException e) {
			logger.error("获取期末机考场次名出错:{}"+e.fillInStackTrace());
		}
		renderJson(response, JsonUtils.listToJson(jsonList));
	}
	/**
	 * 期末机考状态监控
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/recruitexamlogs/list.html")
	public String listFinalExamRecruitExamLogs(HttpServletRequest request, Page objPage,ModelMap model) throws WebException {
		objPage.setOrderBy("examInfo.course.courseName,status,studentInfo.branchSchool,studentInfo.studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式	
		
		Map<String,Object> condition = fetchFinalExamRecruitExamLogsCondition(request);
		if(condition.containsKey("isBrSchool")){
			model.addAttribute("isBrSchool", true);	
		}		
		if (condition.containsKey("examSubId")) {
			Criterion[] criterions = new Criterion[3];
			if (condition.containsKey("courseId")){
				criterions 	       = new Criterion[4];
				criterions[3]      = Restrictions.eq("course.resourceid",condition.get("courseId"));
			}
			criterions[0]          = Restrictions.eq("isDeleted",0);
			criterions[1]          = Restrictions.eq("isMachineExam",Constants.BOOLEAN_YES);
			criterions[2]          = Restrictions.eq("examSub.resourceid",condition.get("examSubId"));
			List<ExamInfo> list    = examInfoService.findByCriteria(criterions);
			
			Page recruitExamLogsPage= recruitExamLogsService.findRecruitExamLogsByCondition(condition, objPage);
			model.addAttribute("recruitExamLogsPage", recruitExamLogsPage);	
			model.addAttribute("machineExamNames", list);	
		}
		model.addAttribute("condition", condition);
		return "/edu3/recruit/recruitexamlogs/finalexam-recruitexamlogs-list";
	}
	/**
	 * 期末机考考后数据统计
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/examresult/list.html")
	public String listFinalExamResult(HttpServletRequest request, Page objPage,ModelMap model) throws WebException {
		objPage.setOrderBy("examInfo.course.courseName,status,studentInfo.branchSchool,studentInfo.studyNo");
		objPage.setOrder(Page.ASC);//设置默认排序方式		
		
		Map<String,Object> condition = fetchFinalExamRecruitExamLogsCondition(request);
		if(condition.containsKey("isBrSchool")){
			model.addAttribute("isBrSchool", true);	
		}
		if(condition.containsKey("examSubId") && condition.containsKey("courseId")){
			Page recruitExamLogsPage = recruitExamLogsService.findRecruitExamLogsByCondition(condition, objPage);
			model.addAttribute("recruitExamLogsPage", recruitExamLogsPage);	
			
			Map<String, Object> statResult = recruitJDBCService.statFinalExamResult(condition.get("examSubId").toString(), condition.get("courseId").toString(), condition.containsKey("brSchool")?condition.get("brSchool").toString():"");
			if(statResult != null){
				BigDecimal passCount = (BigDecimal) statResult.get("passCount");
				BigDecimal assistCount = passCount.add((BigDecimal) statResult.get("nopassCount"));
				statResult.put("assistCount", assistCount);
				if(assistCount!=null && assistCount.intValue()>0){
					statResult.put("passPer", passCount.divide(assistCount, 3, BigDecimal.ROUND_HALF_UP));
				} else {
					statResult.put("passPer", 0);
				}
			}
			model.addAttribute("statResult", statResult);	
		}
		
		model.addAttribute("condition", condition);
		return "/edu3/recruit/recruitexamlogs/finalexam-examresult-list";
	}	
	/**
	 * 导出期末机考数据 (期末机考考后数据统计,期末机考状态监控两个功能中的导出共用)
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/examresult/export.html")
	public void exportFinalExamResult(HttpServletRequest request, HttpServletResponse response) throws WebException {

		Map<String,Object> condition   = fetchFinalExamRecruitExamLogsCondition(request);
		Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
		List<RecruitExamLogs> list     = new ArrayList<RecruitExamLogs>();
		String templateFilepathString  = "";//模板文件路径
		String fileName 		       = "";
		String excelConfig             = "";
		
		if (condition.containsKey("exportType")&&"2".equals(condition.get("exportType"))) {
			excelConfig                = "finalexamRecruitExamLogs_2";
			fileName 		      	   = "期末机考按排";
			templateFilepathString     = "finalexamRecruitExamLogs_2.xls";
			condition.put("orderby", "examInfo.examSub,studentInfo.branchSchool,examInfo.course.courseCode,studentInfo.studyNo");
		}else {
			excelConfig                = "finalexamRecruitExamLogs_1";
			fileName 		      	   = "期末机考统计";
			templateFilepathString     = "finalexamRecruitExamLogs_1.xls";
			condition.put("orderby", "examInfo.course.courseName,status,studentInfo.branchSchool,studentInfo.studyNo");
		}
		
		if(condition.containsKey("examSubId")){
			
			ExamSub examSub 		   = examSubService.get(condition.get("examSubId").toString());			
			if(condition.containsKey("courseId")){
				Course course  		       = courseService.get(condition.get("courseId").toString());
				fileName = course.getCourseName()+fileName;
			}
			fileName 				   = examSub.getBatchName() + fileName;
			list 					   = recruitExamLogsService.findRecruitExamLogsByCondition(condition);
			
			templateMap.put("finalexamTitle", fileName);
			if (!(condition.containsKey("exportType")&&"2".equals(condition.get("exportType")))) {
				Map<String, Object> statResult = recruitJDBCService.statFinalExamResult(condition.get("examSubId").toString(), condition.containsKey("courseId")?condition.get("courseId").toString():"", condition.containsKey("brSchool")?condition.get("brSchool").toString():"");
				if(statResult != null){				
					BigDecimal totalCount  = (BigDecimal) statResult.get("totalCount");
					BigDecimal passCount   = (BigDecimal) statResult.get("passCount");
					BigDecimal assistCount = passCount.add((BigDecimal) statResult.get("nopassCount"));
					statResult.put("assistCount", assistCount);
					if(assistCount!=null && assistCount.intValue()>0){
						statResult.put("passPer", BigDecimalUtil.div(passCount.doubleValue()*100, assistCount.doubleValue(), 1)+"%");
					} else {
						statResult.put("passPer", "0.0%");
					}	
					templateMap.put("totalCount", totalCount);//总人数
					templateMap.put("assistCount", assistCount);//实考数
					templateMap.put("passCount", passCount);//合格数
					templateMap.put("nopassCount", statResult.get("nopassCount"));//不合格数
					templateMap.put("noscoreCount", statResult.get("noscoreCount"));//缺考
					templateMap.put("passPer", statResult.get("passPer"));//合格率		
				}
			}			
		}		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//初始化配置参数
		
			exportExcelService.initParmasByfile(disFile, excelConfig, list,null);
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
			exportExcelService.getModelToExcel().setRowHeight(500);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
	      
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, fileName+".xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			e.printStackTrace();
		}
	}
	/**
	 * 保存期末机考结果
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/examresult/save.html")
	public void saveFinalExamResult(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {		
			String[] res = request.getParameterValues("resourceid");			
			if(res!=null && res.length>0){
				List<RecruitExamLogs> list = new ArrayList<RecruitExamLogs>();
				for (int i = 0; i < res.length; i++) {
					String memo = ExStringUtils.trimToEmpty(request.getParameter("memo"+res[i]));
					RecruitExamLogs log = recruitExamLogsService.get(res[i]);
					log.setMemo(memo);
					
					list.add(log);
				}
				recruitExamLogsService.batchSaveOrUpdate(list);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");		
			}				
		}catch (Exception e) {
			logger.error("保存期末考试结果出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	
	/**
	 * 获取期末机考查询条件
	 * @param request
	 * @return
	 */
	private Map<String,Object> fetchFinalExamRecruitExamLogsCondition(HttpServletRequest request){
		
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String courseId    	   = ExStringUtils.trimToEmpty(request.getParameter("courseId"));
		String examSubId   	   = ExStringUtils.trimToEmpty(request.getParameter("examSubId"));
		String branchSchool	   = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String studyNo     	   = ExStringUtils.trimToEmpty(request.getParameter("studyNo"));
		String studentName 	   = ExStringUtils.trimToEmpty(request.getParameter("studentName"));	
		String status      	   = ExStringUtils.trimToEmpty(request.getParameter("status"));
		String examInfoId  	   = ExStringUtils.trimToEmpty(request.getParameter("examInfoId"));
		String startTime   	   = ExStringUtils.trimToEmpty(request.getParameter("startTime"));
		String endTime     	   = ExStringUtils.trimToEmpty(request.getParameter("endTime"));
		String machineExamName = ExStringUtils.trimToEmpty(request.getParameter("machineExamName"));
		String exportType 	   = ExStringUtils.trimToEmpty(request.getParameter("exportType"));
		
		User user 		   = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())) {
			branchSchool   = user.getOrgUnit().getResourceid();	
			condition.put("isBrSchool", Constants.BOOLEAN_YES);
		}		
		if(ExStringUtils.isNotBlank(courseId)){
			condition.put("courseId", courseId);
		}
		if(ExStringUtils.isNotBlank(examSubId)){
			condition.put("examSubId", examSubId);
		}
		if(ExStringUtils.isNotBlank(studentName)){
			condition.put("studentName", studentName);
		}
		if(ExStringUtils.isNotBlank(studyNo)){
			condition.put("studyNo", studyNo);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(status)){
			condition.put("status", Integer.parseInt(status));
		}
		if(ExStringUtils.isNotBlank(examInfoId)){
			condition.put("examInfoId", examInfoId);
		}
		if(ExStringUtils.isNotBlank(startTime)){
			condition.put("startTime", startTime);
		}
		if(ExStringUtils.isNotBlank(endTime)){
			condition.put("endTime", endTime);
		}
		if(ExStringUtils.isNotBlank(machineExamName)){
			condition.put("machineExamName", machineExamName);
		}
		if(ExStringUtils.isNotBlank(exportType)){
			condition.put("exportType", exportType);
		}
		condition.put("isEntranceExam", Constants.BOOLEAN_NO);	
		condition.put("isMachineExam", Constants.BOOLEAN_YES);	//机考
		return condition;
	}
	
	/**
	 * 设置机考缺考
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/examresult/setabsent.html")
	public void setAbsent(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		Map<String,Object> condition = fetchFinalExamRecruitExamLogsCondition(request);
		Map<String, Object> map = new HashMap<String, Object>();
		String message = "";
//		boolean setAbsent = true;
		//必须在选定考试场次的考试结束时间之后才允许进行本操作，否则提示“考试尚未完结，不允许进行本操作”
		ExamInfo examInfo = examInfoService.get((String)condition.get("machineExamName"));
		Date today = new Date();
		if(today.before(examInfo.getExamEndTime())){
			message = "考试尚未完结，不允许进行本操作";
			map.put("message", message);
			map.put("statusCode", 300);
//			setAbsent = false;
		}else{
		
		//根据选定的考试批次和考试场次，获取所有参加本场次机考的机考记录（不限定课程），先调用一次“强行交卷”的操作，将已考但未交卷或交卷不成功的机考记录进行“强行交卷”，
		List<RecruitExamLogs> list = recruitExamLogsService.findRecruitExamLogsByCondition(condition);
		List<RecruitExamLogs> resourceidList = new ArrayList<RecruitExamLogs>();

		for(RecruitExamLogs logs: list){
			if(logs.getStatus()==0||logs.getStatus()==1){
				resourceidList.add(logs);
			}
		}
		//强制交卷
		if(resourceidList.size()>0){
			String[] resourceidArray = new String[resourceidList.size()];
			for(int i = 0;i<resourceidList.size();i++){
				RecruitExamLogs logs = (RecruitExamLogs)resourceidList.get(i);
				if((logs.getStatus()==0||logs.getStatus()==1)){
					resourceidArray[i] = logs.getResourceid();
				}
			}
			recruitExamLogsService.handFinalExamPapers(resourceidArray);
		}
		
		//然后根据机考日志表（edu_recruit_stustates）表的最后更新时间（lastupdatetime）判断，如果此字段为空，则判断学生为缺考，将学生的考试成绩表(edu_teach_examresults)的异常状态设为缺考；
		list = recruitExamLogsService.findRecruitExamLogsByCondition(condition);
		//获得学生id
		List<String> studentIdList = new ArrayList<String>();
		for(RecruitExamLogs logs:list){
			if(logs.getLastUpdateTime()==null || logs.getEndTime()==null || logs.getLoginIp()==null){//缺考判断依据
				studentIdList.add(logs.getStudentInfo().getResourceid());
			}
		}
		//获得学生成绩数据
		List<ExamResults> examResultsList = new ArrayList<ExamResults>();
		if(studentIdList.size()>0){
			String[] studentIdArray =studentIdList.toArray(new String[studentIdList.size()]);
			examResultsList = examResultsService.findExamResulByExamSubAndStuNoAndExamInfoId((String)condition.get("examSubId"),studentIdArray,(String)condition.get("machineExamName")); 
		}
		//设置缺考状态
		List<ExamResults> newExamResultsList = new ArrayList<ExamResults>();
		if(examResultsList!=null&&examResultsList.size()>0){
			for(ExamResults results:examResultsList){
				if(!results.getExamAbnormity().equals(Constants.EXAMRESULT_ABNORAMITY_2)){
					results.setExamAbnormity(Constants.EXAMRESULT_ABNORAMITY_2);
					newExamResultsList.add(results);
				}
			}
		}
		
		if(newExamResultsList.size()>0){
			examResultsService.batchSaveOrUpdate(newExamResultsList);
			map.put("message", "成功将"+newExamResultsList.size()+"个考生考试状态设置为缺考！");
			map.put("statusCode", 200);
		}else{
			map.put("statusCode", 400);
		}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 设置机考重登录
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/recruitexamlogs/relogin.html")
	public void reloginRecruitExamLogs(String resourceid,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){			
				recruitExamLogsService.reloginExam(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "操作成功！");	
				map.put("forward", request.getContextPath()+"/edu3/teaching/finalexam/recruitexamlogs/list.html");
			}
		} catch (Exception e) {
			logger.error("设置机考重新登录出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 入学机考考试人数统计
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexam/stat.html")
	public String statRecruitExam(HttpServletRequest request, ModelMap model) throws WebException {
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		String statType = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("statType")), "statDate");
		condition.put("statType", statType);
		if("statDate".equals(statType)){
			String statDate = ExStringUtils.trimToEmpty(request.getParameter("statDate"));
			if(ExStringUtils.isNotBlank(statDate)){
				condition.put("statDate", statDate);
				
				List<Map<String, Object>> list = getRecruitExamStatList(condition);		
				model.addAttribute("statList", list);
			}
		} else {
			String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));
			if(ExStringUtils.isNotBlank(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);	
				
				Map<String, List<Map<String, Object>>> resultMap = new LinkedHashMap<String, List<Map<String,Object>>>();
				List<Map<String, Object>> totalList = getRecruitExamStatList(condition);
				if(ExCollectionUtils.isNotEmpty(totalList)){
					resultMap.put("全部", totalList);
				}
				
				String sql = "select distinct to_char(t.starttime,'yyyy-MM-dd') examDate from edu_recruit_explandetails t join edu_recruit_examplan p on p.resourceid=t.examplanid where t.isdeleted=0 and p.recruitplanid=:recruitPlanId order by examDate asc ";
				try {
					List<Map<String, Object>> examDates = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, condition);
					for (Map<String, Object> map : examDates) {
						if(map.get("examDate")!=null){
							String examDate = map.get("examDate").toString();
							condition.put("examDate", examDate);	
							
							List<Map<String, Object>> list = getRecruitExamStatList(condition);	
							if(ExCollectionUtils.isNotEmpty(list)){
								resultMap.put(examDate, list);
							}
						}						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				model.addAttribute("resultMap", resultMap);
			}
		}		
		model.addAttribute("condition", condition);
		return "/edu3/recruit/recruitexamlogs/recruitexam-stat";
	}
	private List<Map<String, Object>> getRecruitExamStatList(Map<String, Object> condition) {
		List<Map<String, Object>> list = recruitJDBCService.statRecruitExamByCondition(condition);
		if(ExCollectionUtils.isNotEmpty(list)){
			int allCount = 0, realCount = 0, oneCount = 0, showOrder = 0;			
			for (Map<String, Object> obj : list) {
				BigDecimal b1 = (BigDecimal)obj.get("allCount");
				BigDecimal b2 = (BigDecimal)obj.get("realCount");
				BigDecimal b3 = (BigDecimal)obj.get("oneCount");
				allCount += b1.intValue();
				realCount += b2.intValue();
				oneCount += b3.intValue();
				
				obj.put("showOrder", ++showOrder);
			}
			
			Map<String, Object> total = new HashMap<String, Object>();	
			total.put("statname", "总计：");
			total.put("allCount", allCount);//应考人数
			total.put("realCount", realCount);//实考人数
			total.put("oneCount", oneCount);//实考人数
			total.put("absentCount", allCount-realCount);//缺考人数
			list.add(total);			
		}		
		return list;
	}
	/**
	 * 入学机考考试人数统计导出
	 * @param recruitPlanId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexam/stat/export.html")
	public void exportRecruitExamStat(String recruitPlanId,HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String,Object> condition = fetchFinalExamRecruitExamLogsCondition(request);
		if(ExStringUtils.isNotBlank(recruitPlanId)){
			condition.put("recruitPlanId", recruitPlanId);
			RecruitPlan plan = recruitPlanService.get(recruitPlanId);
			
			Map<String, List<Map<String, Object>>> resultMap = new LinkedHashMap<String, List<Map<String,Object>>>();
			List<Map<String, Object>> totalList = getRecruitExamStatList(condition);
			if(ExCollectionUtils.isNotEmpty(totalList)){
				resultMap.put("全部", totalList);
			}
			
			String sql = "select distinct to_char(t.starttime,'yyyy-MM-dd') examDate from edu_recruit_explandetails t join edu_recruit_examplan p on p.resourceid=t.examplanid where t.isdeleted=0 and p.recruitplanid=:recruitPlanId order by examDate asc ";
			try {
				List<Map<String, Object>> examDates = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql, condition);
				for (Map<String, Object> map : examDates) {
					if(map.get("examDate")!=null){
						String examDate = map.get("examDate").toString();
						condition.put("examDate", examDate);	
						
						List<Map<String, Object>> list = getRecruitExamStatList(condition);	
						if(ExCollectionUtils.isNotEmpty(list)){
							resultMap.put(examDate, list);
						}
					}						
				}
			} catch (Exception e) {
				e.printStackTrace();
			}		
			//文件输出服务器路径
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			try{
				GUIDUtils.init();
				File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				
				int i = 0;
				LinkedHashMap<String, String> dynamicTitleMap = new LinkedHashMap<String, String>(6);
				dynamicTitleMap.put("showOrder", "序号");
				dynamicTitleMap.put("statname", "学习中心");
				dynamicTitleMap.put("allCount", "应考人数");
				dynamicTitleMap.put("realCount", "实考人数");
				dynamicTitleMap.put("absentCount", "缺考人数");
				dynamicTitleMap.put("oneCount", "只考一科人数");
				
				WritableFont font = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD);
		        WritableCellFormat format = new WritableCellFormat(font);
		        try {
		            format.setAlignment(jxl.format.Alignment.CENTRE);
		            format.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE);
		            format.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		        } catch (WriteException e) {
		            logger.error(e.getMessage(),e.fillInStackTrace());
		        }	
		        
		       for (String examDate: resultMap.keySet()) {
					//初始化配置参数
					exportExcelService.initParmasByfile(disFile, "recruitExamStatExport", resultMap.get(examDate),null);
					exportExcelService.getModelToExcel().setDynamicTitle(true);			
					exportExcelService.getModelToExcel().setDynamicTitleMap(dynamicTitleMap);
					exportExcelService.getModelToExcel().setHeader(plan.getRecruitPlanname()+examDate);
					exportExcelService.getModelToExcel().setSheet(i++, examDate);
					exportExcelService.getModelToExcel().setHeaderCellFormat(format);
					exportExcelService.getModelToExcel().setTitleCellFormat(format);
					disFile = exportExcelService.getExcelFile();//获取导出的文件
				}			
		      
				logger.info("获取导出的excel文件:{}",disFile.getAbsoluteFile());
				downloadFile(response, plan.getRecruitPlanname()+"考试人数统计.xls", disFile.getAbsolutePath(),true);
			}catch(Exception e){
				logger.error("导出excel文件出错："+e.fillInStackTrace());
			}
		}		
	}
	/**
	 * 把空白试卷补全(临时功能)
	 * @param examSubId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/exampaperdetails/join.html")
	public void joinCourseExamPaperDetails(String examSubId, String examInfoId,HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {	
			if(ExStringUtils.isBlank(examSubId)){
				examSubId = "4aa6636738473f5901384f8d2d1c1b05";//2012-2013学年度第一学期期末考试
			}
			String[] courseCodes = {"021","025","005","008"};//管理学原理,大学语文B,高等数学B（上）,高等数学B（下）
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("examSubId", examSubId);
			param.put("courseCodes", Arrays.asList(courseCodes));
			//机考考试课程
			String hql = "from ExamInfo where isDeleted=0 and examSub.resourceid=:examSubId and course.courseCode in (:courseCodes) and isMachineExam='Y' and examCourseType=3 order by course asc";
			List<ExamInfo> list = examInfoService.findByHql(hql, param);
			if(ExStringUtils.isNotBlank(examSubId) && ExCollectionUtils.isNotEmpty(list)){	
				int totalSize = 0;
				for (ExamInfo examInfo : list) {
					int size = recruitExamLogsService.joinCourseExamPaperDetails(examSubId,examInfo.getResourceid());
					logger.info(examInfo.getMachineExamName()+" : "+size +" 份试卷");
					totalSize += size;
				}
				
				map.put("statusCode", 200);
				map.put("message", "操作成功！生成试卷："+totalSize+" 份.");
			}
		} catch (Exception e) {
			logger.error("检查期末考试试卷情况出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "操作失败！<br/>"+e.getLocalizedMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 导出期末考试学生非客观题
	 * @param examInfoId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/finalexam/exampaperdetails/export.html")
	public void listExamPaperDetails(String examInfoId,String exportType,HttpServletRequest request,HttpServletResponse response) throws WebException{
		try {			
			if(ExStringUtils.isNotBlank(examInfoId) && ExStringUtils.isNotBlank(exportType)){
				ExamInfo examInfo = examInfoService.get(examInfoId);				
				
				Map<String, Object> condition = new HashMap<String, Object>();
				condition.put("courseId", examInfo.getCourse().getResourceid());
				condition.put("examType", "5");//非客观题
				condition.put("examform", "final_exam");//期末机考题库
				condition.put("filterChildExam", Constants.BOOLEAN_NO);
				condition.put("orderby", "c.showOrder,c.resourceid");
				List<CourseExam> examList = courseExamService.findCourseExamByCondition(condition);
				
				if(ExCollectionUtils.isNotEmpty(examList)){
					Map<String, Map<String, Object>> examMap = new LinkedHashMap<String, Map<String, Object>>();
					for (CourseExam courseExam : examList) {
						Map<String, Object> exam = new HashMap<String, Object>();
						exam.put("showOrder", courseExam.getShowOrder());
						exam.put("examNodeType", JstlCustomFunction.dictionaryCode2Value("CodeExamNodeType", courseExam.getExamNodeType()));
						//exam.put("question", CourseExamUtils.trimHtml2Txt(ExStringUtils.trimToEmpty(courseExam.getQuestion())));//转为纯文本
						exam.put("question", ExStringUtils.trimToEmpty(courseExam.getQuestion()));
						examMap.put(courseExam.getResourceid(), exam);	
					}
					
					StringBuffer html = new StringBuffer();
					html.append("<html><body>");
					//html.append("<head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /><style type=\"text/css\">img {vertical-align: middle;}</style></head>");
					html.append("<body>");
					html.append("<h3><b>批次："+examInfo.getExamSub().getBatchName()+"</b></h3>");
					html.append("<h3><b>课程："+examInfo.getCourse().getCourseName()+"</b></h3>");
					html.append("<h3><b>场次："+ExStringUtils.trimToEmpty(examInfo.getMachineExamName())+"</b></h3>");
					
					if("courseExam".equals(exportType)){ //课程所有非客观题
						html.append("<br/><h3><b>"+examInfo.getCourse().getCourseName()+"非客观题:</b></h3>");
						for (Map<String, Object> exam : examMap.values()) {
							html.append("<h3><b>序号："+exam.get("showOrder")+"  ("+exam.get("examNodeType")+")</b></h3>");
							html.append("<div>"+exam.get("question")+"</div>");
							html.append("<br/>");
						}
					} else { //studentExam 学生考试试题
						if("fixed".equals(examInfo.getExamPaperType())){//指定考试试卷
							html.append("<br/><h3><b>"+ExStringUtils.repeat("/", 100)+"</b></h3>");
							html.append("<br/><h3><b>学生试题:</b></h3>");
							if(examInfo.getCourseExamPapers()!=null){
								Set<CourseExamPaperDetails> set = examInfo.getCourseExamPapers().getCourseExamPaperDetails();
								for (CourseExamPaperDetails d : set) {
									if("5".equals(d.getCourseExam().getExamType())){//非客观题
										html.append("<div>序号："+d.getCourseExam().getShowOrder()+" ("+JstlCustomFunction.dictionaryCode2Value("CodeExamNodeType", d.getCourseExam().getExamNodeType())+")</div>");
										html.append("<div>"+ExStringUtils.trimToEmpty(d.getCourseExam().getQuestion())+"</div>");
										html.append("<br/>");
									}
								}
							}
						} else { //随机成卷
							//学生非客观题试题列表							
							StringBuffer sql = new StringBuffer();
							sql.append(" select t.examinfoid,s.studyno,s.studentname,es.examnodetype,es.showorder,d.examid from edu_recruit_stustates t ");
							sql.append(" join edu_teach_examinfo ei on ei.resourceid=t.examinfoid ");
							sql.append(" join edu_roll_studentinfo s on s.resourceid=t.studentinfoid ");
							sql.append(" join edu_lear_expaperdetails d on d.paperid=t.exampaperid ");
							sql.append(" join edu_lear_exams es on es.resourceid=d.examid ");
							sql.append(" where t.isdeleted=0 and ei.examsubid=:examSubId ");
							sql.append(" and t.examinfoid=:examInfoId ");
							sql.append(" and ei.ismachineexam=:isMachineExam ");
							sql.append(" and es.examtype=:examType ");
							sql.append(" order by t.examinfoid,s.studyno,d.showorder ");
							
							Map<String, Object> params = new HashMap<String, Object>();
							params.put("examSubId", examInfo.getExamSub().getResourceid());
							params.put("examInfoId", examInfo.getResourceid());
							params.put("isMachineExam", Constants.BOOLEAN_YES);
							params.put("examType", "5");//非客观题
							List<Map<String, Object>> list = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(sql.toString(), params);
							if(ExCollectionUtils.isNotEmpty(list)){		
								html.append("<br/><h3><b>学生试题:</b></h3>");
								String currentStudyNo = "";
								for (Map<String, Object> stuMap : list) {
									Map<String, Object> exam = examMap.get(stuMap.get("examid"));
									if(!currentStudyNo.equals(stuMap.get("studyNo").toString())){
										html.append("<h3><b>学号："+stuMap.get("studyNo")+"  姓名："+stuMap.get("studentName")+"</b></h3>");
									}								
									html.append("<div>序号："+exam.get("showOrder")+" ("+exam.get("examNodeType")+")</div>");
									html.append("<div>"+exam.get("question")+"</div>");
									html.append("<br/>");
									
									currentStudyNo = stuMap.get("studyNo").toString();
								}							
							}
						}
					}
					html.append("</body></html>");
					
					String rootPath = Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles";
					GUIDUtils.init();	
					String tempFilePath = rootPath+File.separator+GUIDUtils.buildMd5GUID(false)+".doc";
					//String rootUrl = "http://wl.scutde.net";
					String rootUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort();
					String htmlContent = html.toString().toString().replace("src=\""+CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue(), "src=\""+rootUrl+CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
					
					DocumentToRtfConvert.htmlToRtfBuilder(tempFilePath, htmlContent, null);
					//FileUtils.writeFile(tempFilePath, htmlContent, 2);//导出成网页
					
					String fileName = examInfo.getCourse().getCourseName()+"学生试题";
					if("courseExam".equals(exportType)){ //课程所有非客观题
						fileName = examInfo.getCourse().getCourseName()+"非客观题";
					}
					logger.info("导出"+fileName+":{}",tempFilePath);
					downloadFile(response, fileName+".doc", tempFilePath, true);
				}
			}			
		} catch (Exception e) {
			logger.error("导出学生试题失败：{}",e.fillInStackTrace());
			renderHtml(response, "<script type=\"text/javascript\">parent.alertMsg.warn(\"导出学生试题失败:"+e.getMessage()+"\")</script>");
		}		
	}
	
	/**
	 * 合并单元格
	 * @param list
	 * @param mergeCellsParams
	 * @param startRow
	 * @param mergeColumns
	 */
	private void getMergeCellsParam(List<Map<String, Object>> list, List<MergeCellsParam> mergeCellsParams, int startRow, int[] mergeColumns) {
		int start = 0;
		String tempStr = null;
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> obj = list.get(i);
			String str = obj.get("examCertificateNo").toString();
			if(tempStr!=null && !ExStringUtils.equals(tempStr, str)){
				if(i-start>1){	//有多行相同数据时合并
					for (int j = 0; j < mergeColumns.length; j++) {
						mergeCellsParams.add(new MergeCellsParam(mergeColumns[j],start+startRow,mergeColumns[j],i+startRow-1));
					}
				} 
				start = i;
			}	
			if(i==list.size()-1 && i-start>0){//最后一行时			
				for (int j = 0; j < mergeColumns.length; j++) {
					mergeCellsParams.add(new MergeCellsParam(mergeColumns[j],start+startRow,mergeColumns[j],i+startRow));
				}
			}
			tempStr = str;
		}
	}	
	
	/**
	 * 学生答案文件上传
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitexamlogs/upload.html")
	public void webcamForEnrolleeInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{		
		String clientkey 		= ExStringUtils.trimToEmpty(request.getParameter("clientkey"));//客户端校验KEY
			
		try{
			if(!clientkey.equals(BaseSecurityCodeUtils.SHA1Encode("www.hnjk.net"))){//校验客户端
				renderText(response, "501");//非法请求
				return;
			}
					
		}catch (Exception e) {
		}
	    //String distPath = CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue() ;
		String distPath = ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath");	
		distPath = distPath+File.separator+"temp"+File.separator+"exam"+File.separator+ExDateUtils.DATE_FORMAT.format(new Date());
		this.setDistfilepath(distPath);
		this.setAcepttype(CacheAppManager.getSysConfigurationByCode("web.uploadfile.accept").getParamValue());					
		//存储
		try{
			List<AttachInfo> list = doUploadFile(request, response,null);//上传
			if(null == list || list.isEmpty()){
				renderText(response,"500");//上传失败
			}
		
		}catch (Exception e) {
			logger.error("上传学生数据文件出错:"+e.fillInStackTrace());
			renderText(response,"500");//上传失败
		}
			
		renderText(response,"200");//上传成功		
		
	}
}
