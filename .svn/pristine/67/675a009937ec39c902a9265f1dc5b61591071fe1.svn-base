package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.RecruitPlan;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

@Controller
public class RecruitReportController extends FileUploadAndDownloadSupportController{

	private static final long serialVersionUID = -8355710569280015110L;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;
	
	/**
	 * 导出录取信息
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/recruit/matriculate/matriculatequery-export.html")
	public void genStudentEntranceExcel(HttpServletRequest request,
			HttpServletResponse response,ModelMap map) throws WebException{
		//学习中心
		String branchSchool = request.getParameter("branchSchool");
		//招生批次
		String recruitPlan = request.getParameter("recruitPlan");
		//招生专业
		String recruitMajor = request.getParameter("recruitMajor");
		//基础专业
		String major = request.getParameter("major");
		//姓名
		//String name = request.getParameter("name");
		//准考证号
		//String examCertificateNo = request.getParameter("examCertificateNo");
		//证件号码
		String certNum = request.getParameter("certNum");
		Map<String, Object> condition = new HashMap<String, Object>();
		//如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if(ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType()))
		{
			branchSchool =	user.getOrgUnit().getResourceid();
			map.addAttribute("brshSchool", true);
		}
		if(StringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
			map.addAttribute("branchSchool", branchSchool);
			map.put("branchSchoolName", ExStringUtils.defaultIfEmpty(request.getParameter("branchSchoolName"), ""));
		}
		if (StringUtils.isNotEmpty(recruitPlan)) {
			condition.put("recruitPlan", recruitPlan);
			map.addAttribute("recruitPlan", recruitPlan);
		}
		if (StringUtils.isNotEmpty(recruitMajor)) {
			condition.put("recruitMajor", recruitMajor);
			map.addAttribute("recruitMajor", recruitMajor);
		}
		if (StringUtils.isNotEmpty(major)) {
			condition.put("major", major);
			map.addAttribute("major", major);
		}
		//if (StringUtils.isNotEmpty(name)) {
		//	condition.put("name", name );
		//	map.addAttribute("name", name);
	//	}
		//if (StringUtils.isNotEmpty(examCertificateNo)) {
		//	condition.put("examCertificateNo", examCertificateNo);
		//	map.addAttribute("examCertificateNo", examCertificateNo);
	//	}
		if (StringUtils.isNotEmpty(certNum)) {
			condition.put("certNum", certNum);
			map.addAttribute("certNum", certNum);
		}
		//已录取
		condition.put("isMatriculate", "Y");
		
		List<EnrolleeInfo> list = enrolleeInfoService.findListByCondition(condition);
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板				
			RecruitPlan recruitPlan2 = recruitPlanService.get(recruitPlan);
			if (null!=recruitPlan2) {
				templateMap.put("yearInfo", recruitPlan2.getGrade().getGradeName());
			}
			
			templateMap.put("fillinMan", SpringSecurityHelper.getCurrentUserName());
			templateMap.put("fillinDate", ExDateUtils.DATE_FORMAT_CN.format(new Date()));
			templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
					CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
			
			//模板文件路径
			String templateFilepathString = "studententrance.xls";
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("yesOrNo");
//			dictCodeList.add("CodeNation");
//			dictCodeList.add("CodeCertType");
			//初始化配置参数
			Map<String, Object> paramap = dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE);
			/*paramap.put("CodeEntranceflag_Y", "审核通过");
			paramap.put("CodeEntranceflag_W", "待审核");
			paramap.put("CodeEntranceflag_N", "审核不通过");*/
			exportExcelService.initParmasByfile(disFile, "studententrance", list,paramap);
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "录取信息.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	
	
	/**
	 * 入学资格明细打印预览
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@SuppressWarnings({ "unchecked"})
	@RequestMapping("/edu3/recruit/matriculate/entranceAuditDetail-export.html")
	public void genEntranceAuditDetailExcel(HttpServletRequest request,
			HttpServletResponse response,ModelMap map) throws WebException{
		//学习中心
		String branchSchool = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"), "");
		//招生批次
		String recruitPlan = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlan"),"");
		//招生专业
		String recruitMajor = ExStringUtils.defaultIfEmpty(request.getParameter("recruitMajor"),"");
		
		String planname = "所有年度";
		Map<String, Object> condition = new HashMap<String, Object>();
		//如果是学习中心用户，只操作本学习中心的数据
		User user = SpringSecurityHelper.getCurrentUser();
		if(ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").
				equals(user.getOrgUnit().getUnitType()))
		{
			branchSchool =	user.getOrgUnit().getResourceid();
			map.addAttribute("brshSchool", true);
		}
		if(StringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
			map.addAttribute("branchSchool", branchSchool);
			map.put("branchSchoolName", ExStringUtils.defaultIfEmpty(request.getParameter("branchSchoolName"), ""));
		}
		if (StringUtils.isNotEmpty(recruitPlan)) {
		    planname = recruitPlanService.get(recruitPlan).getRecruitPlanname();
			condition.put("recruitPlan", recruitPlan);
			map.addAttribute("recruitPlan", recruitPlan);
		}
		if (StringUtils.isNotEmpty(recruitMajor)) {
			condition.put("recruitMajor", recruitMajor);
			map.addAttribute("recruitMajor", recruitMajor);
		}
	
		
		List<EnrolleeInfo> list = enrolleeInfoService.findListByCondition(condition);
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
				templateMap.put("planname", planname);
				templateMap.put("fillinMan", SpringSecurityHelper.getCurrentUserName());
				templateMap.put("fillinDate", ExDateUtils.DATE_FORMAT_CN.format(new Date()));
				templateMap.put("schoolAndConnect", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue()+
						CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue());
				
				//模板文件路径
				String templateFilepathString = "entranceAuditDetail.xls";
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeNation");
				dictCodeList.add("CodeAuditStatus");
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "entranceAuditDetail", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "入学资格信息.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	/**
	 * 自定义导出报名信息
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws WebException
	 */
	@SuppressWarnings({ "unchecked" })
	@RequestMapping("/edu3/recruit/enrolleeinfo/enrolleeinfo-export.html")
	public void doCustomerEnrolleeInfoExcelExport(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		
		//学习中心
		String branchSchool = ExStringUtils.defaultIfEmpty(request.getParameter("branchSchool"),"");
		//招生批次
		String recruitPlan  = ExStringUtils.defaultIfEmpty(request.getParameter("recruitPlan"),"");
		//招生专业
		String major 		= ExStringUtils.defaultIfEmpty(request.getParameter("major"),"");
		//自定义选择导出的数据列
		String[] excelColumnNames = request.getParameterValues("excelColumnName");
		//默认Excel表头
		String planname	    = "所有年度"; 
		
		List<String> filterColumnList = new ArrayList<String>();//定义过滤列
		Map<String, Object> condition = new HashMap<String, Object>();//查询条件
		
		//如果是学习中心用户，只操作本学习中心的数据
		User user		    = SpringSecurityHelper.getCurrentUser();
		if(ConfigPropertyUtil.getInstance().getProperty("brSchool.unitType").equals(user.getOrgUnit().getUnitType())){
			branchSchool =	user.getOrgUnit().getResourceid();
		}
		if(ExStringUtils.isNotBlank(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotBlank(major)) {
			condition.put("recruitMajor", major);
		}
		if(ExStringUtils.isNotBlank(recruitPlan)){
			RecruitPlan plan = recruitPlanService.get(recruitPlan);
			YearInfo info    =  yearInfoService.get(recruitPlan);
			if(null!=plan){
				 planname = plan.getRecruitPlanname();
			}
			if (null!=info) {
				planname  = info.getYearName();         
			}
			condition.put("recruitPlan", recruitPlan);
		}
		if(null !=excelColumnNames && excelColumnNames.length>0){
			for(int i=0;i<excelColumnNames.length;i++){
				filterColumnList.add(excelColumnNames[i]);
			}
		}
		
		List<EnrolleeInfo> list = enrolleeInfoService.findListByCondition(condition);
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try {
			//字典转换列
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeNation");
			dictCodeList.add("CodeAuditStatus");
			dictCodeList.add("CodeIsApplyNoExam");
			//设置模板
			//Map<String,Object> templateMap = new HashMap<String, Object>();
			//templateMap.put("planname", planname);
			//templateMap.put("fillinMan", SpringSecurityHelper.getCurrentUserName());
			//templateMap.put("fillinDate", ExDateUtils.DATE_FORMAT_CN.format(new Date()));
			
			GUIDUtils.init();
			//构造EXCEL文件
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			//模板文件路径
			//String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
			//								+File.separator+"excel"+File.separator+"enrolleeInfo.xls";
			
				
			exportExcelService.initParmasByfile(disFile,"enrolleeInfo", list,dictionaryService.
												getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE),filterColumnList);
			
			exportExcelService.getModelToExcel().setHeader(planname+"报名信息");//设置大标题
			exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
			//exportExcelService.getModelToExcel().setTemplateParam(false,templateFilepathString, 3, templateMap);		
			
			File excelFile = exportExcelService.getExcelFile();//获取导出的文件	
			
			downloadFile(response, "报名信息.xls", excelFile.getAbsolutePath(),true);			
			
		} catch (Exception e) {
			logger.error("导出EXCEL出错了：{}",e.fillInStackTrace());			
		}
	
	}
}
