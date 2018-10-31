package com.hnjk.edu.roll.controller;

import java.io.File;
import java.util.ArrayList;
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
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.recruit.model.ExportRecruitPlan;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.roll.model.Reginfo;
import com.hnjk.edu.roll.service.IReginfoService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;

/**
 * <code>ReginfoController</code><p>
 * 新生注册管理Controller.
 * @author： 广东学苑教育发展有限公司.
 * @since： 2010-4-26 下午03:14:19
 * @see 
 * @version 1.0
 */
@Controller
public class ReginfoController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 5194960125956066266L;

	
	@Autowired
	@Qualifier("reginfoservice")
	private IReginfoService reginfoService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;//Excel导出时所需的年度条件
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	/**
	 * 新生待注册名单列表.
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/registered-list.html")
	public String exeRegistered(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("studentInfo.resourceid,studentInfo.grade.yearInfo.firstYear");
		objPage.setOrder(Page.DESC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		//判断当前用户是否为校外学习中心
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
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
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
			
		Page page = reginfoService.findRegByCondition(condition, objPage);
		
		model.addAttribute("reglist", page);
		model.addAttribute("condition", condition);
		return "/edu3/roll/reginfo/studentregistered-list";
	}
	
	
	/**
	 * 新生注册情况统计列表
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/registeredcount-list.html")
	public String exeRegisterCount(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String classic=request.getParameter("classic");//层次
		
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		
		Page page = reginfoService.findRegByCondition(condition, objPage);
		
		model.addAttribute("reglist", page);
		model.addAttribute("condition", condition);
		return "/edu3/roll/reginfo/registeredcount-list";
	}

	
	
	/**
	 * 新生数据库
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/newstudent-list.html")
	public String exeStudent(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String isNewStudent="studentStatus";//排除已毕业的学生
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
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
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		condition.put("studentStatus", "11");
		if(ExStringUtils.isNotEmpty(branchSchool)||ExStringUtils.isNotEmpty(major)||ExStringUtils.isNotEmpty(classic)||ExStringUtils.isNotEmpty(grade)||ExStringUtils.isNotEmpty(name)||ExStringUtils.isNotEmpty(matriculateNoticeNo))
			{
			Page page = reginfoService.findRegByCondition(condition, objPage);
			model.addAttribute("reglist", page);
			}
		
		model.addAttribute("condition", condition);
		return "/edu3/roll/reginfo/newstudent-list";
	}
	
	/**
	 * 导出新生数据
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/newstudent-export.html")
	public void genNewStudentExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();
		//已录取
		condition.put("isMatriculate", "Y");
		//查询条件
		
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String classic=request.getParameter("classic");//层次
		String grade=request.getParameter("grade");//年级
		String name=request.getParameter("name");//姓名
		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
		String isNewStudent="studentStatus";//排除已毕业的学生
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		condition.put("studentStatus", "11");
		List<Reginfo> list = reginfoService.findByHql(condition);
		
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
				
				//模板文件路径
				String templateFilepathString = "newstudent.xls";
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodePolitics");
				dictCodeList.add("CodeNation");
				dictCodeList.add("CodeLearningStyle");
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "newstudent", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "新生库.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	/**
	 * 导出已通出入学审核数据
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/entranceFlagStudentExcel-export.html")
	public void entranceFlagStudentExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();
		//已通过入学资格审核的
		condition.put("entranceFlag", "Y");
		//查询条件
//		String branchSchool=request.getParameter("branchSchool");//学习中心
//		String major=request.getParameter("major");//专业
//		String classic=request.getParameter("classic");//层次
//		String grade=request.getParameter("grade");//年级
//		String name=request.getParameter("name");//姓名
//		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
//		String isNewStudent="studentStatus";//排除已毕业的学生
//		if(ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
//		if(ExStringUtils.isNotEmpty(major)) condition.put("major", major);
//		if(ExStringUtils.isNotEmpty(classic)) condition.put("classic", classic);
//		if(ExStringUtils.isNotEmpty(grade)) condition.put("grade", grade);
//		if(ExStringUtils.isNotEmpty(name)) condition.put("name", name);
//		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) condition.put("matriculateNoticeNo", matriculateNoticeNo);
		
		String resourceid=request.getParameter("resourceid");//年度
		String [] stu=resourceid.split(",");
		resourceid=stu[0];		//年度id
		String yearName=stu[1]; //某某年度
		if(ExStringUtils.isNotEmpty(resourceid)) {
			condition.put("yearRresourceid", resourceid);  //年度
		}
		List<ExportRecruitPlan> list = reginfoService.entranceFlagStudentExcel(condition);
		
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
				
//				templateMap.put("schoolName", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//试点高校名称
//				templateMap.put("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());//试点高校代码
				templateMap.put("yearName", yearName);//标题上的某某年度
				//模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"entranceFlagStudentExcel.xls";
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodePolitics");
				dictCodeList.add("CodeNation");
//				dictCodeList.add("CodeLearningStyle");
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "entranceFlagStudent", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高

				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "新生库.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	
	
	
	/**
	 * 在校生数据库
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/existstudent-list.html")
	public String exeExistStudent(HttpServletRequest request, Page objPage, ModelMap model) throws WebException {
		objPage.setOrderBy("resourceid");
		objPage.setOrder(Page.ASC);//设置默认排序方式
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		String branchSchool = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学习中心
		String major = ExStringUtils.trimToEmpty(request.getParameter("major"));//专业
		String classic = ExStringUtils.trimToEmpty(request.getParameter("classic"));//层次
		String grade = ExStringUtils.trimToEmpty(request.getParameter("grade"));//年级
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//姓名
		String matriculateNoticeNo = ExStringUtils.trimToEmpty(request.getParameter("matriculateNoticeNo"));//学号
		String isNewStudent="studentStatus";//排除已毕业的学生
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
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
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		condition.put("studentStatus", "11");
		Page page = reginfoService.findRegByCondition(condition, objPage);
		
		model.addAttribute("reglist", page);
		model.addAttribute("condition", condition);
		return "/edu3/roll/reginfo/existstudent-list";
	}
	
	
	@RequestMapping("/edu3/register/reginfo/existstudent-export.html")
	public void existstudent(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		Map<String, Object> condition = new HashMap<String, Object>();
		//已录取
		condition.put("isMatriculate", "Y");
		//查询条件
		
		String branchSchool=request.getParameter("branchSchool");//学习中心
		String major=request.getParameter("major");//专业
		String classic=request.getParameter("classic");//层次
		String grade=request.getParameter("grade");//年级
		String name=request.getParameter("name");//姓名
		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
		String isNewStudent="studentStatus";//排除已毕业的学生
		if(ExStringUtils.isNotEmpty(branchSchool)) {
			condition.put("branchSchool", branchSchool);
		}
		if(ExStringUtils.isNotEmpty(major)) {
			condition.put("major", major);
		}
		if(ExStringUtils.isNotEmpty(classic)) {
			condition.put("classic", classic);
		}
		if(ExStringUtils.isNotEmpty(grade)) {
			condition.put("grade", grade);
		}
		if(ExStringUtils.isNotEmpty(name)) {
			condition.put("name", name);
		}
		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) {
			condition.put("matriculateNoticeNo", matriculateNoticeNo);
		}
		condition.put("studentStatus", "11");
		List<Reginfo> list = reginfoService.findByHql(condition);
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
				Map<String,Object> templateMap = new HashMap<String, Object>();//璁剧疆妯℃澘
				
				//设置模板
				String templateFilepathString = "existstudent.xls";
				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("CodeSex");
				dictCodeList.add("CodeRegisteredResidenceKind");
				dictCodeList.add("CodePolitics");
				//模板文件路径
				exportExcelService.initParmasByfile(disFile, "existstudent", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 1, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "在校学生库统计.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
		}
	}
	
	
	
	
	
	
	
	
	

	//新生库相关导出
	
	
	
	/**
	 * 提供年度选择
	 */
	@RequestMapping("/edu3/roll/selectCondition/selectCondition.html")
	public String selectCondition(String resourceid,HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String actionType=request.getParameter("actionType");   
		List<YearInfo> list=yearInfoService.findAllByOrder();//获取年度集合，供用户选择
		model.put("yearInfoList", list);
		model.put("actionType",actionType);
		return "/edu3/roll/selectCondition/selectCondition";
	}
	
	
	
	
	
	
	/**
	 * 表一.试点高校网络教育招生计划备案表（招生办）.xls
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/exportRecruitPlan.html")
	public void genExistStudentExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("excel", "exportRecruitPlan");
//		String branchSchool=request.getParameter("branchSchool");//学习中心
//		String major=request.getParameter("major");//专业
//		String classic=request.getParameter("classic");//层次
//		String grade=request.getParameter("grade");//年级
//		String name=request.getParameter("name");//姓名
//		String matriculateNoticeNo=request.getParameter("matriculateNoticeNo");//学号
//		if(ExStringUtils.isNotEmpty(branchSchool)) condition.put("branchSchool", branchSchool);
//		if(ExStringUtils.isNotEmpty(major)) condition.put("major", major);
//		if(ExStringUtils.isNotEmpty(classic)) condition.put("classic", classic);
//		if(ExStringUtils.isNotEmpty(grade)) condition.put("grade", grade);
//		if(ExStringUtils.isNotEmpty(name)) condition.put("name", name);
//		if(ExStringUtils.isNotEmpty(matriculateNoticeNo)) condition.put("matriculateNoticeNo", matriculateNoticeNo);

		List<ExportRecruitPlan> list = recruitPlanService.exportList(condition);
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//设置模板
				Map<String,Object> templateMap = new HashMap<String, Object>();
				templateMap.put("schoolName", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//试点高校名称
				templateMap.put("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());//试点高校代码
				//模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"exportRecruitPlan.xls";
				List<String> dictCodeList = new ArrayList<String>();
//				dictCodeList.add("CodeSex");
//				dictCodeList.add("CodeRegisteredResidenceKind");
//				dictCodeList.add("CodePolitics");


				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "exportRecruitPlan", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				 
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "试点高校网络教育招生计划备案表（招生办）.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * 表二.试点高校网络教育实际录取情况表（学籍办）.xls
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/existRealityEnrollExcel.html")
	public void genExistRealityEnrollExcel(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("excel", "existRealityEnrollExcel");//执行条件
		List<ExportRecruitPlan> list = recruitPlanService.exportList(condition);
		
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//设置模板
				Map<String,Object> templateMap = new HashMap<String, Object>();
				templateMap.put("schoolName", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//试点高校名称
				templateMap.put("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());//试点高校代码
				//模板文件路径
				String templateFilepathString = "existRealityEnrollExcel.xls";
				List<String> dictCodeList = new ArrayList<String>();
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "existRealityEnrollExcel", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				 
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "试点高校网络教育实际录取情况表（学籍办）.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			e.printStackTrace();
		}
	}
	
	
	
	
	/**
	 * 表四.试点高校本年度计划招生的校外学习中心备案表（招生办）.xls
	 * @param request
	 * @param response
	 * @param map
	 * @throws WebException
	 */
	@RequestMapping("/edu3/register/reginfo/exportRecruitForYear.html")
	public void exportRecruitForYear(HttpServletRequest request,HttpServletResponse response,ModelMap map) throws WebException{
		//查询条件
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("excel", "exportRecruitForYear");
		String resourceid=request.getParameter("resourceid");//年度
		String [] stu=resourceid.split(",");
		resourceid=stu[0];		//年度id
		String yearName=stu[1]; //某某年度
		if(ExStringUtils.isNotEmpty(resourceid)) {
			condition.put("yearRresourceid", resourceid);  //年度id为查询条件
		}

		
		List<ExportRecruitPlan> list = recruitPlanService.exportList(condition);
		//文件输出服务器路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		try{
			//导出
			File excelFile = null;
			GUIDUtils.init();
			File disFile = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			//设置模板
				Map<String,Object> templateMap = new HashMap<String, Object>();
				templateMap.put("schoolName", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//试点高校名称
				templateMap.put("schoolCode", CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue());//试点高校代码
				templateMap.put("yearName", yearName);//标题上的某某年度
				//模板文件路径
				String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"exportRecruitForYear.xls";
				List<String> dictCodeList = new ArrayList<String>();

				//初始化配置参数
				exportExcelService.initParmasByfile(disFile, "exportRecruitForYear", list,dictionaryService.getDictionByMap(dictCodeList, true,IDictionaryService.PREKEY_TYPE_BYCODE));
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				 
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			downloadFile(response, "试点高校本年度计划招生的校外学习中心备案表（招生办）.xls", excelFile.getAbsolutePath(),true);
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			e.printStackTrace();
		}
	} 
	
	

	
}
