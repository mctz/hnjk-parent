package com.hnjk.edu.teaching.controller;

import com.hnjk.core.exception.ServiceException;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.learning.service.IStudentLearnPlanService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.model.BachelorExamResults;
import com.hnjk.edu.teaching.model.TeachingActivityTimeSetting;
import com.hnjk.edu.teaching.service.IBachelorExamResultsService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BachelorExamResultsController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = -6990220806955675016L;
	
	@Autowired
	@Qualifier("bachelorExamResultsService")
	private IBachelorExamResultsService bachelorExamResultsService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("studentLearnPlanService")
	private IStudentLearnPlanService studentLearnPlanService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 学生查询学士英语成绩
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/bachelorExam.html")
	public String studentMatriculateQuery(HttpServletRequest request, ModelMap model) throws WebException {

		Map<String,Object> condition  = new HashMap<String, Object>();
//		Map<String,Object> stuMap  = new HashMap<String, Object>();
		String fromPage  = StringUtils.trimToEmpty(request.getParameter("fromPage"));	// 学号
		String studentNO  = StringUtils.trimToEmpty(request.getParameter("studentNO"));	// 学号
		String studentName  = StringUtils.trimToEmpty(request.getParameter("studentName"));	// 姓名
		String message = "";
		String open = "Y";// 查询是否开放
		
		try {
			do{
				if(ExStringUtils.isEmpty(fromPage) && !"Y".equals(fromPage)){
					message = "请输入学号和姓名查询成绩！";
					continue;
				}
				//查询批次
				String batch = CacheAppManager.getSysConfigurationByCode("bachelor.exam.batch").getParamValue();
				if(ExStringUtils.isEmpty(batch)){
					message = "还没有设置查询批次，请联系管理员";
					open = "N";
					continue;
				}
				Integer sysYear = Integer.parseInt(batch.substring(0, 4));
				YearInfo yearInfo = yearInfoService.findUniqueByProperty("firstYear", Long.valueOf(sysYear));
				Integer sysTerm = Integer.parseInt(batch.substring(5, 6));
				//教学活动时间
				Map<String,Object> map = new HashMap<String, Object>(); //查询参数
				map.put("isDeleted", 0); //是否删除 0：未删除 1：已删除
				map.put("yearid",yearInfo.getResourceid()); //年级
				map.put("term", sysTerm.toString()); //学期
				map.put("acttype", "bachelorExam"); //活动类型 字典：CodeTeachingActivity
				List<TeachingActivityTimeSetting> listActivity = studentLearnPlanService.findTeachingactivitytime(map); //查看默认批次下是否设置了类型为学士英语成绩查询的活动时间
				if(ExCollectionUtils.isEmpty(listActivity)){
					message = "还没有设置查询时间，请联系教务员";
					open = "N";
					continue;
				}
				Date today = new Date();
				Date ks = listActivity.get(0).getStartTime(); //开始时间
				Date js = listActivity.get(0).getEndTime(); //结束时间
				if(!(today.after(ks) && today.before(js))){
					message = "查询时间未开放";
					open = "N";
					continue;
				}
				/*stuMap.put("studyNo", studentid);
				stuMap.put("studentName", studentName);
				StudentInfo studentInfo = studentInfoService.findUniqueStudentInfo(stuMap);
				if(studentInfo == null){
					message = "找不到该学生信息，请重新输入学号和姓名！";
					continue;
				}*/
				condition.put("yearInfo", yearInfo);
				condition.put("term", sysTerm);
				condition.put("studentNO", studentNO);
				condition.put("studentName", studentName);
				//查找该学生学士英语成绩
				BachelorExamResults results   = bachelorExamResultsService.findUniqueBachelorExamInfo(condition);
				if (results==null) {
					message = "无法查询到该学生的学士学位英语成绩信息！";
					continue;
				}
				model.addAttribute("ea", results);
			}while(false);
		} catch (Exception e) {
			logger.error("学生查询学士英语成绩出错", e);
		} finally {
			//当前时间段不能查询
			model.addAttribute("open", open);
				
			//学生信息为空
			model.addAttribute("condition",condition);
			model.addAttribute("message", message);
		}
		
		return "/edu3/teaching/examResult/bachelorExam-list";
	}
	
	@RequestMapping("/edu3/teaching/examResult/search_bachelorExam.html")
	public String listBachelorExam(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("yearInfo.yearName,term,ordinal,examNo,seatNo");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		Page page=null;
		
		try {	
			// 参数处理
			String yearInfoId = StringUtils.trimToEmpty(request.getParameter("yearInfoId"));	// 年度
			String term = StringUtils.trimToEmpty(request.getParameter("term"));	// 学期
			String brSchoolId = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_branchSchool"));	//教学点
			String gradeId = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_grade"));	//年级
			String classicId = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_classic"));	//层次
			String teachingType = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_teachingType"));	//学习形式
			String majorId = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_major"));	//专业
			String studentid = ExStringUtils.trimToEmpty(request.getParameter("studentid"));	//学号
			String studentNO = ExStringUtils.trimToEmpty(request.getParameter("studentNO"));	//学号
			String studentName = ExStringUtils.trimToEmpty(request.getParameter("studentName"));	//姓名
			String classesId = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_classes"));	//班级
			String examNo = ExStringUtils.trimToEmpty(request.getParameter("search_bachelorExam_examNo"));	//考场号
			String batch = ExStringUtils.trimToEmpty(request.getParameter("con"));//设置查询批次
			
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
//				brSchoolId = user.getOrgUnit().getResourceid();
				model.addAttribute("isBrschool", true);
			}	
			
			if(ExStringUtils.isNotEmpty(yearInfoId)) {
				condition.put("yearInfoId", yearInfoId);
			}
			if(ExStringUtils.isNotEmpty(term)) {
				condition.put("term", Integer.parseInt(term));
			}
			if(ExStringUtils.isNotEmpty(brSchoolId)) {
				condition.put("search_bachelorExam_branchSchool", brSchoolId);
			}
			if(ExStringUtils.isNotEmpty(gradeId)) {
				condition.put("search_bachelorExam_grade", gradeId);
			}
			if(ExStringUtils.isNotEmpty(classicId)) {
				condition.put("search_bachelorExam_classic", classicId);
			}
			if(ExStringUtils.isNotEmpty(teachingType)) {
				condition.put("search_bachelorExam_teachingType", teachingType);
			}
			if(ExStringUtils.isNotEmpty(majorId)) {
				condition.put("search_bachelorExam_major", majorId);
			}
			if(ExStringUtils.isNotEmpty(studentid)) {
				condition.put("studentid", studentid);
			}
			if(ExStringUtils.isNotEmpty(studentNO)) {
				condition.put("studentNO", studentNO);
			}
			if(ExStringUtils.isNotBlank(studentName)) {
				condition.put("studentName", studentName);
			}
			if(ExStringUtils.isNotBlank(classesId)) {
				condition.put("search_bachelorExam_classes", classesId);
			}
			if(ExStringUtils.isNotBlank(examNo)) {
				condition.put("search_bachelorExam_examNo", examNo);
			}
			if ("batch".equals(batch)) {
				return "/edu3/teaching/examResult/setQueryBatch";// 返回设置界面
			}
		
			page = bachelorExamResultsService.findBachelorExamByHql(condition, objPage);
		}catch (ServiceException e) {
			logger.error("查询出错", e);
		}
		
		model.addAttribute("examArrangementList", page);
		model.addAttribute("condition", condition);	
		
		return "/edu3/teaching/examResult/search_bachelorExam";
	}
	
	/**
	 * 跳转到导入窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/examResult/inputScoreForm.html")
	public String imputScore(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException {
		model.addAttribute("title", "导入学士学位英语成绩");
		model.addAttribute("formId", "bachelorExamResults");
		model.addAttribute("url", "/edu3/teaching/examResult/importScore.html");
		return "edu3/roll/inputDialogForm";
	}
	
	/**
	 * 导入学士学位英语成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 * @throws IOException 
	 * @throws ServletRequestBindingException
	 */
	@RequestMapping("/edu3/teaching/examResult/importScore.html")
	public void importScore(HttpServletRequest request, HttpServletResponse response, String exportAct) throws WebException, IOException, ServletRequestBindingException {
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
					Map<String, Object> singleMap = bachelorExamResultsService.analysisBachelorExamFile(filePath);
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
	
	@RequestMapping("/edu3/teaching/examResult/setQueryBatch.html")
	public void setBatch(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		String yearInfo = StringUtils.trimToEmpty(request.getParameter("yearInfo"));	// 年度
		String term = StringUtils.trimToEmpty(request.getParameter("term"));	// 学期
		Map<String, Object> map  = new HashMap<String, Object>();
		int statusCode = 200;
		String message = null;
		try {
			if(ExStringUtils.isEmpty(yearInfo) || ExStringUtils.isEmpty(term)){
				statusCode = 300;
				message = "年度和学期不能为空！";
			}else{
				String batch = yearInfo + "." + term;
				bachelorExamResultsService.updateBatch(batch);
				message = "设置成功！";
			}
		} catch (Exception e) {
			statusCode = 300;
			message = "设置失败！";
			logger.error("设置学士英语成绩查询批次出错", e);
		}finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
//			map.put("reloadUrl", request.getContextPath() +"/edu3/teaching/examResult/setBatch.html");
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 下载学士学位成绩模板
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/teaching/examResult/download-file.html")
	public void downloadERModel(HttpServletRequest request, HttpServletResponse response){
		try{
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			//模板文件路径
			String templateFilepathString = "bachelorExamInputModel.xls";
			downloadFile(response, "学士学位英语成绩录入模版.xls", templateFilepathString,false);
		}catch(Exception e){			
			String msg = "导出excel文件出错：找不到该文件-学士学位英语成绩录入模版.xls";
			logger.error("下载统考成绩录入模版出错", e);
			renderHtml(response, "<script>alertMsg.warn("+"\""+msg+"\""+")</script>");
		}
	}
	
	/**
	 * 跳转到设置查询批次页面
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/teaching/examResult/setBatch.html")
	public String toSetBatch(HttpServletRequest request, HttpServletResponse response, ModelMap model){
		SysConfiguration config = CacheAppManager.getSysConfigurationByCode("bachelor.exam.batch");
		if(config==null){
			model.addAttribute("msg", "还没有设置查询批次，请联系管理员");
		}else{
			String batch = config.getParamValue();
			if(ExStringUtils.isNotEmpty(batch)){
				String[] yearInfo_term = batch.split("\\.");
				model.addAttribute("yearInfo", yearInfo_term[0]);
				model.addAttribute("term", yearInfo_term[1]);
			}
		}
		
		return "/edu3/teaching/examResult/setQueryBatch";
	}
	
}
