package com.hnjk.edu.teaching.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.teaching.model.ExamSub;
import com.hnjk.edu.teaching.model.GraduatePapersOrder;
import com.hnjk.edu.teaching.service.IExamSubService;
import com.hnjk.edu.teaching.service.IGraduatePapersOrderService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
/**
 * 毕业论文成绩管理
 * <code>GraduateExamResultsController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2011-12-5 上午11:37:52
 * @see 
 * @version 1.0
 */
@Controller
public class GraduateExamResultsController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 3689110596412458160L;
	
	/**
	 * 毕业论文预约学生列表
	 * @param batchId
	 * @param branchSchool
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatepapersorder/list.html")
	public String listGraduate(String batchId, String branchSchool, HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("examSub.yearInfo.firstYear desc,studentInfo.branchSchool.unitCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String stuStudyNo = ExStringUtils.trimToEmpty(request.getParameter("stuStudyNo"));		
		
		if(ExStringUtils.isNotBlank(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotBlank(stuStudyNo)) {
			condition.put("stuStudyNo", stuStudyNo);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		condition.put("isStudent", Constants.BOOLEAN_NO);
		condition.put("loginId", user.getResourceid());
		condition.put("status", Constants.BOOLEAN_TRUE);//审核通过的		
		
		List<ExamSub> list = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and batchType = ? order by yearInfo.firstYear desc ",	0,"thesis");
		model.addAttribute("examSubList", list);
		//如果查询批次不为空
		if(ExStringUtils.isEmpty(batchId) && null != list && !list.isEmpty()){
			for(ExamSub examSub : list){
				if("2".equals(examSub.getExamsubStatus())){//当前开放批次
					batchId = examSub.getResourceid();
					break;
				}
			}
		}		
		if(ExStringUtils.isNotBlank(batchId)){		
			condition.put("batchId", batchId);
			Page page = graduatePapersOrderService.findGraduateByCondition(condition, objPage);
			model.addAttribute("ordercList", page);
			
			if(ExCollectionUtils.isNotEmpty(page.getResult())){
				ExamSub examSub = examSubService.get(batchId);
				Date today = new Date();
				String msg = "";
				String isInExaminputTime = Constants.BOOLEAN_YES;
				String isInOralexaminputTime = Constants.BOOLEAN_YES;
				
				if(today.before(examSub.getExaminputStartTime()) || today.after(examSub.getExaminputEndTime())
						|| (examSub.getGradendDate().getOralexaminputEndTime()!=null && today.after(examSub.getGradendDate().getOralexaminputEndTime()))){
					isInExaminputTime = Constants.BOOLEAN_NO;
					if(today.before(examSub.getExaminputStartTime())){
						msg += "初评成绩录入时间还未开始。";
					} else if(today.after(examSub.getExaminputEndTime()) && (examSub.getGradendDate().getOralexaminputEndTime()==null || today.before(examSub.getGradendDate().getOralexaminputEndTime()))){
						msg += "初评成绩录入有效时间已过,如果要修改初评成绩，请上报到教学管理办公室由相关负责人进行更改。";
					}					
				} 
				if(examSub.getGradendDate().getOralexaminputStartTime()==null || examSub.getGradendDate().getOralexaminputEndTime()==null
						|| today.before(examSub.getGradendDate().getOralexaminputStartTime()) || today.after(examSub.getGradendDate().getOralexaminputEndTime())){
					isInOralexaminputTime = Constants.BOOLEAN_NO;	
					if(examSub.getGradendDate().getOralexaminputStartTime()==null || examSub.getGradendDate().getOralexaminputEndTime()==null){
						msg += "答辩成绩录入时间还未设置。";
					} else if(today.before(examSub.getGradendDate().getOralexaminputStartTime())) {
						msg += "答辩成绩录入时间还未开始。";						
					} else {
						msg += "成绩录入有效时间已过,如果要修改初评成绩和答辩成绩，请上报到教学管理办公室由相关负责人进行更改。";
					}
				} 		
				model.addAttribute("isInExaminputTime", isInExaminputTime);
				model.addAttribute("isInOralexaminputTime", isInOralexaminputTime);
				model.addAttribute("inputTimeTips", msg);
				model.addAttribute("examSub", examSub);
			}			
		}		
		model.addAttribute("condition", condition);		
		return "/edu3/teaching/graduateexamresults/graduateexamresults-list";
	}	
	/**
	 * 保存录入成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatepapersorder/result/save.html")
	public void saveGraduateExamResult(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			graduatePapersOrderService.saveOrUpdateGraduateResults(request);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");					
		} catch (Exception e) {
			logger.error("录入毕业论文成绩出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}

	/**
	 * 成绩管理列表
	 * @param batchId
	 * @param branchSchool
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatepapersorder/thesis/list.html")
	public String listThesis(String batchId, String branchSchool, HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		objPage.setOrderBy("examSub.yearInfo.firstYear desc,guidTeacherName,studentInfo.branchSchool.unitCode");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String stuStudyNo = ExStringUtils.trimToEmpty(request.getParameter("stuStudyNo"));	
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));	
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));	
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));	
		String teacherName = ExStringUtils.trimToEmpty(request.getParameter("teacherName"));
		String teachType = ExStringUtils.defaultIfEmpty(ExStringUtils.trimToEmpty(request.getParameter("teachType")), "networkstudy");
		
		if(ExStringUtils.isNotBlank(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotBlank(stuStudyNo)) {
			condition.put("stuStudyNo", stuStudyNo);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(teacherName)) {
			condition.put("teacherName", teacherName);
		}
		condition.put("status", Constants.BOOLEAN_TRUE);//审核通过的		
		condition.put("teachType", teachType);	
		
		List<ExamSub> list = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and batchType = ? order by yearInfo.firstYear desc ",	0,"thesis");
		model.addAttribute("examSubList", list);
		//如果查询批次不为空
		if(ExStringUtils.isEmpty(batchId) && null != list && !list.isEmpty()){
			for(ExamSub examSub : list){
				if("2".equals(examSub.getExamsubStatus())){//当前开放批次
					batchId = examSub.getResourceid();
					break;
				}
			}
		}		
		if(ExStringUtils.isNotBlank(batchId)){		
			condition.put("batchId", batchId);
			if("netsidestudy".equals(teachType)){//A+B模式
				Page page = graduatePapersOrderService.findThesisExamResultsVoByCondition(objPage, condition);
				model.addAttribute("ordercList", page);	
				
				ExamSub thesisSub = examSubService.get(batchId);
				model.addAttribute("thesisSub",thesisSub);
			} else {//B纯网络
				Page page = graduatePapersOrderService.findGraduateByCondition(condition, objPage);
				model.addAttribute("ordercList", page);	
			}			
		}		
		model.addAttribute("condition", condition);		
		return "/edu3/teaching/graduateexamresults/thesisresults-list";
	}	
	/**
	 * 审核毕业论文成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatepapersorder/thesis/audit.html")
	public void auditThesis(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			String[] res = ExStringUtils.trimToEmpty(request.getParameter("resourceid")).split("\\,");	
			String teachType = ExStringUtils.trimToEmpty(request.getParameter("teachType"));
			if(res!=null && res.length>0){
				graduatePapersOrderService.auditThesisResults(res,teachType);
			}			
			map.put("statusCode", 200);
			map.put("message", "审核成功！");					
		} catch (Exception e) {
			logger.error("录入毕业论文成绩出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "审核失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	
	private List<GraduatePapersOrder> getGraduateResultsList(HttpServletRequest request){
		List<GraduatePapersOrder> list = new ArrayList<GraduatePapersOrder>();
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件
		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));
		String stuStudyNo = ExStringUtils.trimToEmpty(request.getParameter("stuStudyNo"));	
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));	
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));	
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));	
		String teacherName = ExStringUtils.trimToEmpty(request.getParameter("teacherName"));
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));
		String batchId = ExStringUtils.trimToEmpty(request.getParameter("batchId"));
		
		if(ExStringUtils.isNotBlank(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotBlank(stuStudyNo)) {
			condition.put("stuStudyNo", stuStudyNo);
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(teacherName)) {
			condition.put("teacherName", teacherName);
		}
		condition.put("status", Constants.BOOLEAN_TRUE);//审核通过的			
		if(!"thesis".equals(from)){
			User user = SpringSecurityHelper.getCurrentUser();
			condition.put("isStudent", Constants.BOOLEAN_NO);
			condition.put("loginId", user.getResourceid());
		}
		if(ExStringUtils.isNotBlank(batchId)){		
			condition.put("batchId", batchId);
			list = graduatePapersOrderService.findGraduateByCondition(condition);			
		}	
		return list;
	}
	/**
	 * 指导老师导出成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/graduatepapersorder/result/export.html")
	public void exportGraduateResults(HttpServletRequest request, HttpServletResponse response) throws WebException {
		String from = ExStringUtils.trimToEmpty(request.getParameter("from"));
		List<GraduatePapersOrder> list = getGraduateResultsList(request);	
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeScoreChar");
			//模板文件路径
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			if(ExCollectionUtils.isNotEmpty(list)){
				GraduatePapersOrder order = list.get(0);
				templateMap.put("batchName", order.getExamSub().getBatchName());
				templateMap.put("branchSchool", order.getStudentInfo().getBranchSchool().getUnitName());
				templateMap.put("major", order.getStudentInfo().getMajor().getMajorName());
				templateMap.put("guidTeacherName", order.getGuidTeacherName());
			} else {
				templateMap.put("batchName", "");
				templateMap.put("branchSchool", "");
				templateMap.put("major", "");
				templateMap.put("guidTeacherName", "");
			}
			String modelName = "thesis".equals(from)?"graduatePapersResults2":"graduatePapersResults";
			String templateFilepathString = modelName+".xls";
			//初始化配置参数1
			exportExcelService.initParmasByfile(disFile, modelName, list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, "thesis".equals(from)?4:5, templateMap);
			exportExcelService.getModelToExcel().setRowHeight(500);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
	      
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "毕业论文成绩导出.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * A+B模式毕业论文成绩录入
	 * @param examSubId
	 * @param branchSchool
	 * @param name
	 * @param studyNo
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/thesisexamresults/netsidestudy/list.html")
	public String listFaceStudyThesisExamResults(String batchId, String branchSchool,String name, String stuStudyNo, String status,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{
		Map<String,Object> condition = new HashMap<String,Object>();//查询条件		
		
		String grade = request.getParameter("grade");
		String major = request.getParameter("major");
		String classic = request.getParameter("classic");
		status = ExStringUtils.defaultIfEmpty(status, "0");
		User user = SpringSecurityHelper.getCurrentUser();
		if(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue().equals(user.getOrgUnit().getUnitType())){
			branchSchool = user.getOrgUnit().getResourceid();
			condition.put("isBrSchool", Constants.BOOLEAN_YES);
		}	
		if(ExStringUtils.isEmpty(batchId)){//默认选择当前开放的批次
			List<ExamSub> examSubList = examSubService.findByHql("from "+ExamSub.class.getSimpleName()+" where isDeleted = ? and batchType = ? and examsubStatus = ? order by yearInfo.firstYear desc,term desc ",	0,"thesis","2");
			if(ExCollectionUtils.isNotEmpty(examSubList)){
				batchId = examSubList.get(0).getResourceid();
			}
		}
		
		/*Grade defaultGrade = gradeService.getDefaultGrade();*/
		String yearTerm = CacheAppManager.getSysConfigurationByCode("sysCurrentGrade").getParamValue();	
		String defaultGradeYear = Long.toString(ExDateUtils.getCurrentYear());
		String defaultGradeTerm = "1";
		if (null!=yearTerm) {
			String[] ARRYyterm = yearTerm.split("\\.");
			defaultGradeYear = ARRYyterm[0];
			defaultGradeTerm = ARRYyterm[1];
		}
		condition.put("defaultGradeYear", defaultGradeYear);
		condition.put("defaultGradeTerm", defaultGradeTerm);
		if(ExStringUtils.isNotBlank(name)) {
			condition.put("name", ExStringUtils.trimToEmpty(name));
		}
		if(ExStringUtils.isNotBlank(stuStudyNo)) {
			condition.put("stuStudyNo", ExStringUtils.trimToEmpty(stuStudyNo));
		}
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(status)) {
			condition.put("status", status);//录入状态
		}
		if(ExStringUtils.isNotBlank(batchId)){		
			condition.put("batchId", batchId);			
			ExamSub thesisSub = examSubService.get(batchId);
			model.addAttribute("thesisSub",thesisSub);
		}
		if(ExStringUtils.isNotBlank(batchId) && ExStringUtils.isNotBlank(branchSchool)){		
			Page thesisExamResultsPage = graduatePapersOrderService.findThesisExamResultsVoByCondition(objPage, condition);
			model.addAttribute("thesisExamResultsPage", thesisExamResultsPage);
		}		
		model.addAttribute("condition", condition);		
		
		return "/edu3/teaching/graduateexamresults/netsidestudy-thesisexamresults-list";
	}	
	/**
	 * 保存网成班毕业论文成绩
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/teaching/thesisexamresults/netsidestudy/save.html")
	public void saveNetsidestudyThesisExamResult(HttpServletRequest request,HttpServletResponse response) throws WebException {
		Map<String,Object> map = new HashMap<String, Object>();		
		try {	
			graduatePapersOrderService.saveOrUpdateNetsideStudyThesisResults(request);
			map.put("statusCode", 200);
			map.put("message", "保存成功！");					
		} catch (Exception e) {
			logger.error("录入网成班毕业论文成绩出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！<br/>"+e.getMessage());
		}
		renderJson(response, JsonUtils.mapToJson(map));	
	}
	
	@Autowired
	@Qualifier("graduatepapersorderservice")
	private IGraduatePapersOrderService graduatePapersOrderService;
	
	@Autowired
	@Qualifier("examSubService")
	private IExamSubService examSubService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;	
	
	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
}
