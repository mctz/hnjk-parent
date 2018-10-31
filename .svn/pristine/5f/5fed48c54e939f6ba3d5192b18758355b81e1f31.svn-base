package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hnjk.core.foundation.utils.*;
import com.hnjk.edu.teaching.model.ElectiveExamResults;
import com.hnjk.platform.system.service.IDictionaryService;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Course;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.ExamInfo;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.service.ICourseService;
import com.hnjk.edu.teaching.service.IElectiveExamResultsService;
import com.hnjk.edu.teaching.service.IExamInfoService;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.impl.ExamPrintServiceImpl;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;

@Controller
public class ElectiveExamResultsController extends FileUploadAndDownloadSupportController{

	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;	
	
	@Autowired
	@Qualifier("courseService")
	private ICourseService courseService;
			
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("examInfoService")
	private IExamInfoService examInfoService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	@Autowired
	@Qualifier("electiveExamResultsService")
	private IElectiveExamResultsService electiveExamResultsService;
	
	@RequestMapping("/edu3/teaching/examResult/electiveExam-list.html")
	public String getElectiveExamResulrsPage(HttpServletRequest request,ModelMap model,Page objPage) throws WebException{
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		if(condition.containsKey("fromPage")){
			String courseId = ExStringUtils.toString(request.getParameter("courseId"));
			Page page = electiveExamResultsService.findPageByCondition(condition, objPage);
			String courseInfo = electiveExamResultsService.constructCourseOptions(condition,courseId);
			model.addAttribute("courseInfo", courseInfo);
			model.addAttribute("page", page);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("brSchoolId", user.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool", true);
		}else {
			model.addAttribute("isBrschool", false);
		}
		model.addAttribute("condition", condition);
		
		return "/edu3/teaching/electiveExamResult/electiveExam-list";
	}

	/**
	 * 下载选修课成绩录入模版
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/electiveExamResult/download-file.html")
	public void downloadERModel(HttpServletRequest request,HttpServletResponse response){
		try{
			String templateFilepathString = "electiveExamInputModel.xls";
			downloadFile(response, "选修课成绩录入模版.xls", templateFilepathString,false);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-学选修课成绩录入模版.xls";
			logger.error("下载成绩录入模版出错", e);
			renderHtml(response, "<script>alert("+"\""+msg+"\""+")</script>");
		}
	}

	/**
	 * 成绩文件选择窗口
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/electiveExamResult/inputScore.html")
	public String inputScore(ModelMap model) throws WebException{
		model.addAttribute("title", "导入选修课成绩");
		model.addAttribute("formId", "electiveExam_import");
		model.addAttribute("url", "/edu3/teaching/electiveExamResult/importScore.html");
		return "edu3/roll/inputDialogForm";
	}

	/**
	 * 导入并保存选修课成绩
	 * @param request
	 * @param response
	 * @param exportAct
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/electiveExamResult/importScore.html")
	public void importScore(HttpServletRequest request, HttpServletResponse response, String exportAct) throws WebException{
		StringBuffer message = new StringBuffer("");
		boolean success = true;
		String result = "";
		Map<String, Object > returnMap = new HashMap<String, Object>();
		try {
			do {
				String attchID = ExStringUtils.trimToEmpty(request.getParameter("attachId"));
			
				if (null != attchID && attchID.split(",").length > 1) {
					success = false;
					message.append("一次只能导入一个成绩文件,谢谢！");
				} else if (null != attchID && attchID.split(",").length == 1) {
					Attachs attachs = attachsService.get(attchID.split(",")[0]);
					String filePath = attachs.getSerPath() + File.separator + attachs.getSerName();
					Map<String, Object> singleMap = electiveExamResultsService.importElectiveExamResults(filePath);
					if (singleMap != null && singleMap.size() > 0) {
						int totalCount = (Integer) singleMap.get("totalCount");
						int successCount = (Integer) singleMap.get("successCount");
						message.append((String) singleMap.get("message"));
						result = "导入共" + totalCount + "条,成功" + successCount + "条,失败" + (totalCount - successCount) + "条";
					}
					File f = new File(filePath);
					if (f.exists()) {
						f.delete();
					}
					attachsService.delete(attachs);
				}
				
			} while (false);
		} catch (Exception e) {
			logger.error("处理导入成绩出错", e);
			success = false;
			result = "导入失败";
		} finally {
			returnMap.put("success",success);
			returnMap.put("msg",message);
			returnMap.put("result",result);
			renderJson(response,JsonUtils.mapToJson(returnMap));
		}
	}
	
	@RequestMapping("/edu3/teaching/electiveExamResult/printView.html")
	public String printViewElectiveExamResults(HttpServletRequest request,ModelMap model) throws Exception{
		Condition2SQLHelper.addMapFromResquestByIterator(request, model);
		return "/edu3/teaching/electiveExamResult/electiveExamResult-printView";
	}
	
	@RequestMapping("/edu3/teaching/electiveExamResult/print.html")
	public void printElectiveExamResults(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String, Object> condition  = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String checkStatus = ExStringUtils.toString(condition.get("checkStatus"));
		//只允许打印提交和保存状态的成绩
		if("0".equals(checkStatus) || ExStringUtils.isEmpty(checkStatus)){
			condition.put("checkStatus", "-1");
		}
		List<Map<String, Object>> list = electiveExamResultsService.findElectiveExamResultsByCondition(condition);
		String brSchoolId		= 	request.getParameter("brSchoolId");
		String courseId			= 	request.getParameter("courseId");
		String examSubId		= 	request.getParameter("examSubId");
		String operatingType	=	request.getParameter("operatingType");
		String batchName		=	"";
		String courseName		=	"";
		JasperPrint jasperPrint = null; // 打印
		Map<String, Object> printParam = new HashMap<String, Object>();
		List<Map<String, Object>> dataList = new ArrayList<Map<String,Object>>();
		try {
			if(list!=null && list.size()>0){
				electiveExamResultsService.batchUpdatePrintTime(condition);
				String schoolName = CacheAppManager.getSysConfigurationByCode("print.msg.schoolname").getParamValue();
				String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
				Course course			= 	courseService.get(courseId);
				courseName 				=	course.getCourseName();
				ExamSub sub				=	examSubService.get(examSubId);
				OrgUnit unit			=	orgUnitService.get(brSchoolId);
				condition.put("stuStatus", "11','16");//在学，毕业
				//int studentNum = electiveExamResultsService.getCountsByExamSubAndCourse(condition);
				printParam.put("studentNum", list.size()+"");
				printParam.put("unitName", unit.getUnitName());
				String term = sub.getTerm();
				Long year = sub.getYearInfo().getFirstYear();
		    	//改成某年某季学期非统考课程期末考试
		    	batchName = year+"年"+("1".equals(term)?"春季":"秋季");
		    	printParam.put("batchName", batchName);
		    	printParam.put("examType", JstlCustomFunction.dictionaryCode2Value("CodeExamClassType",course.getExamType().toString()));
				printParam.put("courseName", courseName);
				printParam.put("printDate", ExDateUtils.formatDateStr(new Date(),2));
				printParam.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
				printParam.put("school", schoolName+schoolConnectName);
				ExamInfo examInfo = examInfoService.findExamInfoByCourseAndExamSub(courseId, examSubId);
				String writtenScorePer = "";
				String usuallyScorePer = "";
				if(examInfo!=null){
					writtenScorePer = ExNumberUtils.toString(examInfo.getFacestudyScorePer());
					usuallyScorePer = ExNumberUtils.toString(examInfo.getFacestudyScorePer2());
				}
				if(ExStringUtils.isBlank(writtenScorePer,usuallyScorePer)){
					writtenScorePer =  CacheAppManager.getSysConfigurationByCode("facestudyScorePer").getParamValue();
					usuallyScorePer = CacheAppManager.getSysConfigurationByCode("facestudyScorePer2").getParamValue();
				}
				printParam.put("writtenScore",writtenScorePer);
				printParam.put("usuallyScore",usuallyScorePer);
				
				for (Map<String, Object> m : list) {
					Map<String, Object> map = new HashMap<String, Object>();
					String examAbnormity = (String) m.get("examAbnormity");
					String courseScoreType = (String) m.get("courseScoreType");
					String usuallyScore = (String) m.get("usuallyScore");
					String writtenScore = (String) m.get("writtenScore");
					String integratedScore = (String) m.get("integratedScore");
					if("0".equals(examAbnormity)){
						if("25".equals(courseScoreType)){
							integratedScore = JstlCustomFunction.dictionaryCode2Value("CodeScoreChar", integratedScore);
							usuallyScore = "";
							writtenScore = "";
						}
					}else{
						integratedScore = JstlCustomFunction.dictionaryCode2Value("CodeExamAbnormity", examAbnormity);
						usuallyScore = "";
						writtenScore = "";
					}
					map.put("stuNo", m.get("studyNo"));
					map.put("stuName", m.get("studentName"));
					//map.put("unitName", m.get("unitName"));
					//map.put("unitShortName", m.get("unitShortName"));
					map.put("unitShortName", "");
					map.put("classesName", m.get("classesName"));
					map.put("usuallyScore", usuallyScore);
					map.put("writtenScore", writtenScore);
					map.put("integratedScore", integratedScore);
					dataList.add(map);
				}
			}
			String reportPath ="";
		    reportPath = File.separator+"reports"+File.separator+"examPrint"+File.separator+"courseExamResults_elective_stdx.jasper";
			// 报表文件
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(dataList);
			jasperPrint = JasperFillManager.fillReport(reprotFile, printParam, dataSource); // 填充报表
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ("export".equals(operatingType)) {
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();			
			String filePath         = getDistfilepath() + File.separator + GUIDUtils.buildMd5GUID(false)+".xls";
			JRXlsExporter exporter  = new JRXlsExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);  
			exporter.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);  
			exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);  
			exporter.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);  
			exporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME,filePath); 
			//设置excel打印参数
			exporter.exportReport(); 
			ExamPrintServiceImpl.setExcelPrintSetting(filePath);
			downloadFile(response,batchName+"《"+courseName+"》选修课成绩.xls",filePath,true);
		}else {
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		}
	}
	
	@RequestMapping(value={"/edu3/teaching/electiveExamResult/operateExamResult.html"})
	public void saveExamResult(HttpServletRequest request,HttpServletResponse response) {
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String reids = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		String operatingType = ExStringUtils.trimToEmpty(request.getParameter("operatingType"));
		String contents = "【选修课成绩——";
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuilder builder = new StringBuilder( "update edu_teach_electiveexamresults eer");
		String checkStatus = "0";
		String org_checkStatus = "0";
		String addSql = "";
		if("cancelSubmit".equals(operatingType)){
			checkStatus = "0";
			org_checkStatus = "1";
			operatingType = UserOperationLogs.PASS;
			contents += "撤销提交】";
		}else if("cancelAudit".equals(operatingType)){
			checkStatus = "0";
			org_checkStatus = "4";
			operatingType = UserOperationLogs.REPEAL;
			contents += "撤销审核】";
		}else if("submit".equals(operatingType)){
			checkStatus = "1";
			org_checkStatus = "0";
			operatingType = UserOperationLogs.UPDATE;
			contents += "提交】";
		}else if ("audit".equals(operatingType)) {
			checkStatus = "4";
			org_checkStatus = "1";
			operatingType = UserOperationLogs.PASS;
			contents += "审核】";
		}else if ("delete".equals(operatingType)) {
			operatingType = UserOperationLogs.DELETE;
			contents += "删除】";
		}
		contents += condition.toString();
		if ("delete".equalsIgnoreCase(operatingType)) {
			builder.append(" set eer.isdeleted=1 ");
		}else {
			condition.put("checkStatus", checkStatus);
			builder.append(" set eer.checkStatus = :checkStatus ");
		}
		builder.append(" where eer.isdeleted=0 and eer.checkStatus='"+org_checkStatus+"'");
		if(ExStringUtils.isNotEmpty(reids)){
			String [] resourceid = ExStringUtils.trimToEmpty(reids).split("\\,");
			builder.append(" and eer.resourceid in("+ExStringUtils.addSymbol(resourceid, "'", "'")+")");
		}
		if(condition.containsKey("examSubId")){
			builder.append(" and eer.examSubId=:examSubId");
		}
		if(condition.containsKey("courseId")){
			builder.append(" and eer.courseId=:courseId");
		}
		builder.append(" and eer.studentid in(select si.resourceid from edu_roll_studentinfo si where si.isdeleted=0");
		if(condition.containsKey("brSchoolId")){
			builder.append(" and si.branchschoolid=:brSchoolId");
		}
		if(condition.containsKey("gradeId")){
			builder.append(" and si.gradeId=:gradeId");
		}
		if(condition.containsKey("classicId")){
			builder.append(" and si.classicId=:classicId");
		}
		if(condition.containsKey("teachingType")){
			builder.append(" and si.teachingType=:teachingType");
		}
		if(condition.containsKey("majorId")){
			builder.append(" and si.majorId=:majorId");
		}
		if(condition.containsKey("classesId")){
			builder.append(" and si.classesId=:classesId");
		}
		if(condition.containsKey("stuStatus")){
			builder.append(" and si.studentstatus=:stuStatus");
		}
		builder.append(")");
		try {
			int count = baseSupportJdbcDao.getBaseJdbcTemplate().executeForMap(builder.toString(), condition);
			map.put("count", count);
			map.put("success", true);
			map.put("msg", "操作成功！");
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "5", operatingType, contents);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("success", true);
			map.put("msg", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/teaching/electiveExamResult/getCourseInfo.html")
	public void getCourseInfo(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "操作成功！";
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String courseId = ExStringUtils.toString(request.getParameter("courseId"));
		//Map<String, Object> condition = new HashMap<String, Object>();
		try {
			//if(ExStringUtils.isNotBlank(examSubId)) condition.put("examSubId", examSubId);
			String courseInfo = electiveExamResultsService.constructCourseOptions(condition,courseId);
			map.put("courseInfo", courseInfo);
		} catch (Exception e) {
			// TODO: handle exception
			statusCode = 300;
			message = "操作成功！";
		}
		
		map.put("statusCode", statusCode);
		map.put("message", "");
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu3/teaching/electiveExamResult/editExamResult.html")
	public String editExamResult(HttpServletRequest request,ModelMap model,Page objPage){
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String reids = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
		//String [] resourceid = ExStringUtils.trimToEmpty(reids).split("\\,");
		condition.put("checkStatus", "0");
		// 获取不显示的所有异常成绩情况代码
		String noShowCodeStr = dictionaryService.getAllValues("noShowCodeExamAbnormity");
		ElectiveExamResults examResults = electiveExamResultsService.get(reids);
		ExamInfo examInfo = examInfoService.findExamInfoByCourseAndExamSubAndCourseType(examResults.getCourse().getResourceid(),examResults.getExamSub().getResourceid(),"faceTeach");
		model.addAttribute("examInfo", examInfo);
		model.addAttribute("examResults", examResults);
		model.addAttribute("noShowCodeStr",noShowCodeStr);
		return "/edu3/teaching/electiveExamResult/electiveExam-form";
	}
	
	@RequestMapping("/edu3/teaching/electiveExamResult/saveExamResult.html")
	public void saveExamResultForm(ElectiveExamResults examResults,HttpServletRequest request,HttpServletResponse response){
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			String reids = ExStringUtils.trimToEmpty(request.getParameter("resourceid"));
			ElectiveExamResults persistExamResults = electiveExamResultsService.get(reids);
			persistExamResults.setWrittenScore(examResults.getWrittenScore());
			persistExamResults.setUsuallyScore(examResults.getUsuallyScore());
			persistExamResults.setIntegratedScore(examResults.getIntegratedScore());
			persistExamResults.setExamAbnormity(examResults.getExamAbnormity());
			//ExBeanUtils.copyProperties(persistExamResults, examResults);
			//examResults = persistExamResults;
			electiveExamResultsService.saveOrUpdate(persistExamResults);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");
			map.put("navTabId", "RES_TEACHING_ELECTIVEEXAMRESULTS_INPUT");
			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/electiveExamResult/editExamResult.html?resourceid="+reids);
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
