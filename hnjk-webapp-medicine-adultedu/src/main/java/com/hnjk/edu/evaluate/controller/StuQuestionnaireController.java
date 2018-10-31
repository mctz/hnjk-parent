package com.hnjk.edu.evaluate.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.SensitivewordFilter;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.evaluate.model.QuestionBank;
import com.hnjk.edu.evaluate.model.Questionnaire;
import com.hnjk.edu.evaluate.service.IQuestionBankService;
import com.hnjk.edu.evaluate.service.IQuestionnaireService;
import com.hnjk.edu.evaluate.service.IStuQuestionnaireService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Controller
public class StuQuestionnaireController extends FileUploadAndDownloadSupportController{

	/**
	 * 
	 */
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService iStudentInfoService;
	
	@Autowired
	@Qualifier("iQuestionnaireService")
	private IQuestionnaireService iQuestionnaireService;
	
	@Autowired
	@Qualifier("iStuQuestionnaireService")
	private IStuQuestionnaireService iStuQuestionnaireService;
	
	@Autowired
	@Qualifier("iQuestionBankService")
	private IQuestionBankService iQuestionBankService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	private static final long serialVersionUID = -6524272203770848734L;
	
	/**
	 * 返回学生问卷列表
	 * @param studentInfoid
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/view.html")
	public String stuQuestionnaireView(String studentInfoid,HttpServletRequest request,ModelMap model) throws Exception{
		StudentInfo stu = iStudentInfoService.get(studentInfoid);
		
		if(stu!=null){
			String classesId = stu.getClasses()==null?"":stu.getClasses().getResourceid();
			if(!ExStringUtils.isBlank(classesId)){
				//返回学生未提交的问卷列表
				List<Questionnaire> listQn = iQuestionnaireService.getQuestionnaire(classesId,studentInfoid,"NO");
				model.addAttribute("qnList", listQn);
				model.addAttribute("qnListSize", listQn.size());
			}else {
				model.addAttribute("qnListSize", 0);
			}
			model.addAttribute("studentInfoid", studentInfoid);
		}
		return "/edu3/evaluate/stuQuestionnaire/stuQuestionnaire-view";
	}
	/**
	 * 返回学生问卷题目列表
	 * @param questionnaireid
	 * @param studentInfoid
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/list.html")
	public String stuQuestionnaireList(String questionnaireid,String studentInfoid,HttpServletRequest request,ModelMap model) throws Exception{
		Questionnaire qn = iQuestionnaireService.get(questionnaireid);		
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("isDeleted", 0);
		condition.put("courseType", "0");
		List<QuestionBank> qbList = iQuestionBankService.findQuestionBankAll(condition);
		if(qbList!=null && qn!=null){
			model.addAttribute("qn", qn);
			model.addAttribute("qbList", qbList);
			model.addAttribute("studentInfoid", studentInfoid);
		}
		return "/edu3/evaluate/stuQuestionnaire/stuQuestionnaire-list";
	}
	/**
	 * 保存学生提交的问卷
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/submit.html")
	public void stuQuestionnaireSubmit(HttpServletRequest request,HttpServletResponse response) throws Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode=200;
		String message="";
		try {
			do {
				String studentInfoid = request.getParameter("studentInfoid");
//				String commentlabel = request.getParameter("commentlabel");
				//防 XSS 注入 
				String commentlabel = StringEscapeUtils.escapeHtml(request.getParameter("commentlabel"));
				
				SensitivewordFilter sensitivewordFilter = (SensitivewordFilter)request.getSession().getServletContext().getAttribute("sensitivewordFilter");
				Set<String> sensitivewordSet = sensitivewordFilter.getSensitiveWord(ExStringUtils.trimToEmpty(commentlabel),SensitivewordFilter.maxMatchType);
				if(ExCollectionUtils.isNotEmpty(sensitivewordSet)){
					statusCode=300;
					message = "回复内容含有敏感词："+sensitivewordSet+",请修改后再提交";					
					continue;
				}
				String resourceids = request.getParameter("resourceids");
				String questionnaireid = request.getParameter("questionnaireid");
				boolean isSuccess =iStuQuestionnaireService.stuQuestionnaireSave(studentInfoid, commentlabel, resourceids,questionnaireid);
				if(isSuccess){
					statusCode=200;
					message="保存成功！";			
				}else{
					statusCode=300;
					message="保存失败！";					
				}
				map.put("studentInfoid", studentInfoid);
				map.put("reloadUrl", "/edu3/teaching/quality/evaluation/stuQuestionnaire/view.html");
				map.put("reloadDialogId", "stuQuestionnaire");
			} while (false);
		} catch (Exception e) {
			logger.error("保存回复出错：{}",e.fillInStackTrace());
			statusCode = 300;
			message = "保存失败！";
		} finally{
			map.put("statusCode",statusCode);
			map.put("message",message);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request),  "13", UserOperationLogs.UPDATE,"学生提交教评问卷：结果："+message+"。参数："+JsonUtils.mapToJson(Condition2SQLHelper.getConditionFromResquestByIterator(request)));
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 教评结果 
	 * @param objPage
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/resultPage.html")
	public String exestuQuestionPage(Page objPage,ModelMap model,HttpServletRequest request) throws Exception{
		objPage.setOrder(Page.ASC);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User cureentUser = SpringSecurityHelper.getCurrentUser();
		if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
				indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){
			String brSchoolId = cureentUser.getOrgUnit().getResourceid();
			condition.put("brSchool", brSchoolId);//判断用户是否为教学点账号
			model.addAttribute("linkageQuerySchoolId", brSchoolId);
		}
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		if(SpringSecurityHelper.isUserInRole(roleCode)){//教师角色，只能查看自己
			condition.put("teacherid", cureentUser.getResourceid());
		}
		
		Page page = iStuQuestionnaireService.findStuQuestionnairePage(condition, objPage);
		
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		return "/edu3/evaluate/stuQuestionnaire/stuQuestionnaire-page";
	}
	/**
	 * 教评结果 以教师为单位
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/TeacherView.html")
	public String stuQuestionnaireTeacher(HttpServletRequest request,ModelMap model) throws Exception{
		String teacherid = request.getParameter("teacherid");
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("teacherid", teacherid);
		List<Map<String,Object>> list = iStuQuestionnaireService.findSqnCourse(condition);
		model.addAttribute("list", list);
		return "/edu3/evaluate/stuQuestionnaire/stuQuestionnaire-TeacherView";
	}
	/**
	 * 教评结果 查看学生提交的问卷
	 * @param objPage
	 * @param model
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/stuList.html")
	public String stuQuestionnairePage(Page objPage,ModelMap model,HttpServletRequest request) throws Exception{
		objPage.setOrder(Page.ASC);
		String questionnaireid = request.getParameter("questionnaireid");
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("questionnaireid", questionnaireid);
		Page page = iStuQuestionnaireService.findSqnListByQn(condition,objPage);
		
		String roleCode = CacheAppManager.getSysConfigurationByCode("sysuser.role.teacher").getParamValue();//获取角色编码
		boolean isTeacher = false;
		if(SpringSecurityHelper.isUserInRole(roleCode)){//教师角色，只能查看自己
			isTeacher = true;
		}
		model.addAttribute("isTeacher", isTeacher);
		model.addAttribute("page", page);
		model.addAttribute("condition", condition);
		return "/edu3/evaluate/stuQuestionnaire/stuQuestionnaire-stuView";
	}
	@RequestMapping("/edu3/teaching/quality/evaluation/stuQuestionnaire/evaluationResultExport.html")
	public void evaluationResultExport(HttpServletRequest request,HttpServletResponse response)throws Exception{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		List<Map<String,Object>> list = iStuQuestionnaireService.findSqnCourse(condition);

		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		GUIDUtils.init();
		//导出
		File excelFile = null;
		File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
		//初始化配置参数
		//以导入报名信息的标识做为判断，0.ZKZH//准考证号 1.KSH//考生号
		exportExcelService.initParmasByfile(disFile, "evaluationResult2Excel", list,null);
		exportExcelService.getModelToExcel().setHeader("教学结果列表");
		exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
		
		excelFile = exportExcelService.getExcelFile();//获取导出的文件
		logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
		String downloadFileName = "教学结果列表.xls";
		
		String downloadFilePath = excelFile.getAbsolutePath();
		downloadFile(response, downloadFileName,downloadFilePath,true);	
		
	}
}
