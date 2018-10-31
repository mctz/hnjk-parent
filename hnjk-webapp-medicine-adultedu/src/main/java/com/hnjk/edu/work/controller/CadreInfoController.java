package com.hnjk.edu.work.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.edu.work.model.CadreInfo;
import com.hnjk.edu.work.model.UserTimeManage;
import com.hnjk.edu.work.service.ICadreInfoService;
import com.hnjk.edu.work.service.IUserTimeManageService;
import com.hnjk.edu.work.vo.CadreInfoVo;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.User;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;

/**
 * Function : 学生干部信息 - 控制器
 * <p>Author : msl
 * <p>Date   : 2018-07-23
 * <p>Description : extends FileUploadAndDownloadSupportController
 */
@Controller
public class CadreInfoController extends FileUploadAndDownloadSupportController {

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	@Autowired
	@Qualifier("cadreInfoService")
	private ICadreInfoService cadreInfoService;

	@Autowired
	@Qualifier("userTimeManageService")
	private IUserTimeManageService userTimeManageService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;

	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;

	/**
	 * 学生干部信息 - 分页查询
	 * @param request
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/list.html")
	public String findCadreInfoPage(HttpServletRequest request, Page page, ModelMap model) {
		page.setOrderBy("yearInfo.yearName,term");
		page.setOrder(Page.DESC);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("studentInfo.branchSchool.resourceid", user.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool", true);
		}else {
			model.addAttribute("isBrschool", false);
		}
		if (!condition.containsKey("isAppoint")) {
			condition.put("isAppoint","Y");
		}
		boolean isStudent = SpringSecurityHelper.isStudent();
		if (isStudent) {
			StudentInfo studentInfo = null;
			try {
				studentInfo = studentInfoService.getByUserId(SpringSecurityHelper.getCurrentUser().getResourceid());
			} catch (Exception e) {
				e.printStackTrace();
			}
			condition.put("studyNo",studentInfo.getStudyNo());
		}
		Page pageResult = cadreInfoService.findByCondition(page,condition);
		model.addAttribute("isStudent",isStudent);
		model.addAttribute("condition",condition);
		model.addAttribute("pageResult",pageResult);
		return "/edu3/work/cadreInfoManage/cadreInfo_list";
	}

	/**
	 * 学生干部信息 - 分页查询
	 * @param request
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/applyList.html")
	public String findCadreInfoApplyPage(HttpServletRequest request, Page page, ModelMap model) {
		page.setOrderBy("yearInfo.yearName,term");
		page.setOrder(Page.DESC);
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			condition.put("studentInfo.branchSchool.resourceid", user.getOrgUnit().getResourceid());
			model.addAttribute("isBrschool", true);
		}else {
			model.addAttribute("isBrschool", false);
		}
		boolean isStudent = SpringSecurityHelper.isStudent();
		if (isStudent) {
			StudentInfo studentInfo = null;
			try {
				studentInfo = studentInfoService.getByUserId(SpringSecurityHelper.getCurrentUser().getResourceid());
			} catch (Exception e) {
				e.printStackTrace();
			}
			condition.put("studyNo",studentInfo.getStudyNo());
		}
		Page pageResult = cadreInfoService.findByCondition(page,condition);
		model.addAttribute("isStudent",isStudent);
		model.addAttribute("condition",condition);
		model.addAttribute("pageResult",pageResult);
		return "/edu3/work/cadreInfoManage/cadreInfo_applyList";
	}

	/**
	 *  新增/编辑 学生干部信息
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/input.html")
	public String editCadreInfo(String resourceid,ModelMap model) throws WebException {

		CadreInfo cadreInfo = new CadreInfo();
		boolean isStudent = SpringSecurityHelper.isStudent();
		String url =  "edu3/work/cadreInfoManage/cadreInfo_applyForm";
		if (ExStringUtils.isNotBlank(resourceid)) { //-----编辑
			cadreInfo = cadreInfoService.get(resourceid);
			model.addAttribute("cadreInfo", cadreInfo);
			url = "edu3/work/cadreInfoManage/cadreInfo_form";
		} else if(isStudent){
			StudentInfo studentInfo = null;
			try {
				studentInfo = studentInfoService.getByUserId(SpringSecurityHelper.getCurrentUser().getResourceid());
				cadreInfo.setStudyNo(studentInfo.getStudyNo());
				cadreInfo.setStudentName(studentInfo.getStudentName());
				cadreInfo.setStudentInfo(studentInfo);
			} catch (Exception e) {
				logger.error("获取学籍信息出错："+e.getMessage());
			}
			model.addAttribute("branchSchoolid",SpringSecurityHelper.getCurrentUser().getOrgUnit().getResourceid());
		}
		model.addAttribute("isStudent",isStudent);
		model.addAttribute("cadreInfo", cadreInfo);
		return url;
	}

	/**
	 * 保存 学生干部信息
	 * @param cadreInfo
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/save.html")
	public void saveCadreInfo(CadreInfo cadreInfo, HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 300;
		try {
			do{
				String yearid = ExStringUtils.trimToEmpty(request.getParameter("yearInfoId"));
				String term = ExStringUtils.toString(request.getParameter("term"));
				String branchSchoolid = ExStringUtils.trimToEmpty(request.getParameter("branchSchoolid"));
				if(ExStringUtils.isNotBlank(cadreInfo.getResourceid())){ //--------------------更新
					CadreInfo persistuserCadreInfo = cadreInfoService.get(cadreInfo.getResourceid());
					ExBeanUtils.copyProperties(persistuserCadreInfo, cadreInfo);
					cadreInfo = persistuserCadreInfo;
				}else{ //-------------------------------------------------------------------新增
					Map<String,Object> condition = new HashMap<String, Object>();
					//判断当前时间是否在招新工作时间范围类
					Date currentDate = new Date();
					condition.put("yearInfo.resourceid",yearid);
					condition.put("term",cadreInfo.getTerm());
					condition.put("unit.resourceid",branchSchoolid);
					condition.put("workType","1");
					condition.put("startTime-end",currentDate);//开始时间小于当前时间
					condition.put("endTime-start",currentDate);//结束时间大于当前时间
					List<UserTimeManage> userTimeManageList = userTimeManageService.findByCondition(condition);
					if (!(userTimeManageList != null && userTimeManageList.size() > 0)) {
						map.put("message", "不在招新工作时间范围内！");
						continue;
					}

					//判断学生是否已经申请过该职位
					condition.clear();
					condition.put("studyNo",cadreInfo.getStudyNo());
					condition.put("yearInfo.resourceid",yearid);
					condition.put("term",cadreInfo.getTerm());
					condition.put("department",cadreInfo.getDepartment());
					condition.put("position",cadreInfo.getPosition());
					List<CadreInfo> cadreInfoList = cadreInfoService.findByCondition(condition);
					if (cadreInfoList != null && cadreInfoList.size() > 0) {
						map.put("message", "不允许重复申请！");
						continue;
					}
				}
				if (!ExStringUtils.isNotBlank4All(cadreInfo.getDepartment(), cadreInfo.getPosition())) {
					map.put("message", "竞选部门和竞选职位不能为空！");
					continue;
				}
				YearInfo yearInfo = null;
				StudentInfo studentInfo = null;
				if (ExStringUtils.isNotBlank(cadreInfo.getStudyNo())) {
					studentInfo = studentInfoService.findUniqueByProperty("studyNo",cadreInfo.getStudyNo());
				}
				if (studentInfo == null) {
					map.put("message", "学籍信息不能为空！");
					continue;
				}
				cadreInfo.setStudentInfo(studentInfo);

				yearInfo = yearInfoService.findUniqueByProperty("resourceid", yearid);
				String currentYear = yearInfo.getFirstYear().toString();//第二学期
				String prevYear = yearInfo.getFirstYear()-1+"";//第一学期
				if (yearInfo == null) {
					map.put("message", "年度不能为空！");
					continue;
				}
				if ("1".equals(term)) {
					currentYear = prevYear+"_02";
					prevYear = prevYear+"_01";
				} else {
					prevYear = prevYear+"_02";
					currentYear = currentYear+"_01";
				}
				cadreInfo.setYearInfo(yearInfo);

				//获取第一学期和第二学期平均分
				Map<String,Object> scoreMap1 = examResultsService.getAvgScoreGroupByTeachType(studentInfo.getResourceid(),prevYear);
				Map<String,Object> scoreMap2 = examResultsService.getAvgScoreGroupByTeachType(studentInfo.getResourceid(),currentYear);
				Double avgScore1 = (Double) scoreMap1.get("avgScoreForFaceStudy");
				Double avgScore2 = (Double) scoreMap2.get("avgScoreForFaceStudy");
				cadreInfo.setAvgScore1(ExStringUtils.toString(avgScore1));
				cadreInfo.setAvgScore2(ExStringUtils.toString(avgScore2));

				cadreInfoService.saveOrUpdate(cadreInfo);
				statusCode = 200;
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_WORK_TIMEMANAGE_INPUT");
				map.put("reloadUrl", request.getContextPath() +"/edu3/work/cadreInfoManage/input.html?resourceid="+cadreInfo.getResourceid());

			}while(false);
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("message", "保存失败！");
		}finally {
			map.put("statusCode",statusCode);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 根据学生学号查询学籍信息
	 * @param studyNo
	 * @param response
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/validateStudyNo.html")
	public void getStudentInfoByStudyNo(String studyNo,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			User user = SpringSecurityHelper.getCurrentUser();
			StudentInfo studentInfo = studentInfoService.findUniqueByProperty("studyNo",studyNo);
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				if (studentInfo!=null && !user.getOrgUnit().getResourceid().equals(studentInfo.getBranchSchool().getResourceid())) {
					message = "不允许选择其它教学点学生！";
					statusCode = 300;
				}
			}
			map.put("studentName",studentInfo.getStudentName());
			map.put("branchSchoolid",studentInfo.getBranchSchool().getResourceid());
		} catch (Exception e) {
			statusCode = 300;
			message = "学号有误，请重新输入学生学号！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 下载学生干部申请列表
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/downloadModel.html")
	public void downloadCadreInfoModel(HttpServletRequest request,HttpServletResponse response) throws WebException{
		try {
			// 获取处理后的参数
			Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
			String yearInfoid = request.getParameter("yearInfo.resourceid");
			String term		= request.getParameter("term");
			String resourceids = request.getParameter("resourceids");
			List<CadreInfo> cadreInfoList = new ArrayList<CadreInfo>();
			YearInfo yearInfo = yearInfoService.get(yearInfoid);
			if (yearInfo == null) {
				new Throwable("请选择年度和学期！");
			}
			if (ExStringUtils.isNotBlank(resourceids)) {
				condition.put("resourceid-idList", Arrays.asList(resourceids.split(",")));
			}
			cadreInfoList = cadreInfoService.findByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(cadreInfoList)){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");

				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("Code.WorkManage.department");
				dictCodeList.add("Code.WorkManage.position");
				dictCodeList.add("yesOrNo_default");
				//dictCodeList.add("CodeTerm");
				Map<String,Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				Map<String,Object> paramMap = new HashMap<String, Object>();
				String yearAndTerm = yearInfo.getFirstYear()+"年第"+term+"学期";
				paramMap.put("yearAndTerm",yearAndTerm);
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"cadreInfoModel", cadreInfoList,dictMap);
				exportExcelService.getModelToExcel().setHeader(yearAndTerm+"学生干部评选");//设置大标题
				WritableFont font = new WritableFont(WritableFont.ARIAL, 12);
				font.setBoldStyle(WritableFont.BOLD);
				WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setBackground(Colour.YELLOW);
				format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);
				File excelFile = exportExcelService.getExcelFile();
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, yearAndTerm+"学生干部评选.xls", disFile.getAbsolutePath(),true);
			}
		} catch (Exception e) {
			logger.error("导出学生干部申请信息出错", e);
		}
	}

	/**
	 * 选择文件窗口
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/importDialog.html")
	public String importDialog(HttpServletRequest request,ModelMap model) throws WebException{
		model.addAttribute("url","/edu3/work/cadreInfoManage/importModel.html");
		model.addAttribute("navTabId","RES_WORK_CADREINFO_LIST");
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);
		return "edu3/roll/imputDialogForErrorInfo";
	}

	/**
	 * 处理导入数据
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/importModel.html")
	public void importCadreInfoModel(HttpServletRequest request,HttpServletResponse response) throws WebException{

		String exportErrorUrl = "/edu3/work/cadreInfoManage/exportCadreInfoError.html";
		//提示信息字符串
		String  rendResponseStr = "";
		List<CadreInfoVo> importFailureList = new ArrayList<CadreInfoVo>();
		File excelFile = null;
		//设置目标文件路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
		try {
			//上传文件到服务器
			List<AttachInfo> list = doUploadFile(request, response, null);
			AttachInfo attachInfo = list.get(0);
			//创建EXCEL对象 获得待导入的excel的内容
			File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());

			importExcelService.initParmas(excel, "cadreInfoVo",null);
			importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0

			//获得待导入excel内容的List
			List<CadreInfoVo> cadreInfoVoList = importExcelService.getModelList();
			if(cadreInfoVoList==null){
				throw new Exception("导入模版错误！");
			}

			//获取部门及职位数据字典
			Map<String,String> deptMap = dictionaryService.getMapByParentDictCode("Code.WorkManage.department");
			Map<String,String> posMap = dictionaryService.getMapByParentDictCode("Code.WorkManage.position");
			Map<String,String> yesOrNoMap = dictionaryService.getMapByParentDictCode("yesOrNo");
			String yearName = cadreInfoVoList.get(0).getYearName();
			String term = cadreInfoVoList.get(0).getTerm();
			List<CadreInfoVo> volist = new ArrayList<CadreInfoVo>();
			List<String> studeNoList = new ArrayList<String>();

			int inNum = 0;
			for (CadreInfoVo infoVo: cadreInfoVoList) {
				inNum++;
				if (ExStringUtils.isBlank(infoVo.getStudyNo())) {
					infoVo.setMessage("学号不能为空！");
					importFailureList.add(infoVo);
					continue;
				}
				if (ExStringUtils.isBlank(infoVo.getDepartment())) {
					infoVo.setMessage("竞选部门不能为空！");
					importFailureList.add(infoVo);
					continue;
				} else if (!deptMap.containsKey(infoVo.getDepartment().trim())) {
					infoVo.setMessage("部门名称有误，请查看【部门】数据字典！");
					importFailureList.add(infoVo);
					continue;
				}
				if (ExStringUtils.isBlank(infoVo.getPosition())) {
					infoVo.setMessage("竞选职位不能为空！");
					importFailureList.add(infoVo);
					continue;
				} else if (!posMap.containsKey(infoVo.getPosition().trim())) {
					infoVo.setMessage("职位名称有误，请查看【职位】数据字典！");
					importFailureList.add(infoVo);
					continue;
				}
				if (ExStringUtils.isNotBlank(infoVo.getDepartment_current()) && !deptMap.containsKey(infoVo.getDepartment_current().trim())) {
					infoVo.setMessage("当前部门名称有误，请查看【部门】数据字典！");
					importFailureList.add(infoVo);
					continue;
				}
				if (ExStringUtils.isNotBlank(infoVo.getPosition_current()) && !posMap.containsKey(infoVo.getPosition_current().trim())) {
					infoVo.setMessage("当前职位名称有误，请查看【职位】数据字典！");
					importFailureList.add(infoVo);
					continue;
				}
				studeNoList.add(infoVo.getStudyNo());
				volist.add(infoVo);
			}

			List<CadreInfo> cadreInfoList = new ArrayList<CadreInfo>();

			if (volist!=null && volist.size()>0) {
				//根据年度、学期、学号查找干部申请信息
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("yearInfo.yearName",yearName);
				condition.put("term",term);
				condition.put("studyNo-inList",studeNoList);
				cadreInfoList = cadreInfoService.findByCondition(condition);
			}

			//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
			if (null!=volist) {
				GUIDUtils.init();
				List<CadreInfo> saveCadreInfos = new ArrayList<CadreInfo>();
				for(CadreInfoVo vo : volist){
					CadreInfo cadreInfo = null;
					//判断是否存在该学生申请记录
					for (CadreInfo info : cadreInfoList) {
						if(vo.getStudyNo().equals(info.getStudyNo()) && deptMap.get(vo.getDepartment()).equals(info.getDepartment()) && posMap.get(vo.getPosition()).equals(info.getPosition())){
							cadreInfo = info;
							break;
						}
					}
					if(cadreInfo==null){
						vo.setMessage("该学生没有申请记录！");
						importFailureList.add(vo);
						continue;
					}

					//更新记录
					if (cadreInfo != null) {
						if (deptMap.containsKey(vo.getDepartment_current())) {
							cadreInfo.setDepartment_current(deptMap.get(vo.getDepartment_current()));
						}
						if (posMap.containsKey(vo.getPosition_current())) {
							cadreInfo.setPosition_current(posMap.get(vo.getPosition_current()));
						}
						if (yesOrNoMap.containsKey(vo.getIsCandidate())) {
							cadreInfo.setIsCandidate(yesOrNoMap.get(vo.getIsCandidate()));
						}
						if (yesOrNoMap.containsKey(vo.getIsAppoint())) {
							cadreInfo.setIsAppoint(yesOrNoMap.get(vo.getIsAppoint()));
						}
						if (ExStringUtils.isNotBlank(vo.getMemo())) {
							cadreInfo.setMemo(vo.getMemo());
						}
						if (ExStringUtils.isNotBlank(vo.getJobTime())) {
							cadreInfo.setJobTime(ExDateUtils.convertToDateTime(vo.getJobTime()));
						}
						cadreInfo.setMemo(vo.getMemo());
						saveCadreInfos.add(cadreInfo);
					}
				}
				cadreInfoService.batchSaveOrUpdate(saveCadreInfos);
			}

			//导出失败的信息以及原因
			if(importFailureList!=null && importFailureList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");

				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"cadreInfoVoError", importFailureList,null);
				exportExcelService.getModelToExcel().setHeader("学生干部评选导入失败记录");//设置大标题

				WritableFont font = new WritableFont(WritableFont.ARIAL, 12);
				font.setBoldStyle(WritableFont.BOLD);
				WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setBackground(Colour.YELLOW);
				format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);

				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());

				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (cadreInfoVoList.size() - importFailureList.size())
						+"条 | 导入失败"+  importFailureList.size()
						+"条！',forwardUrl:'"+exportErrorUrl+"?excelFile="+fileName+"'};";
			}
			if(ExStringUtils.isBlank(rendResponseStr)){
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (cadreInfoVoList.size() - importFailureList.size())
						+"条 | 导入失败"+ importFailureList.size()
						+"条！',forwardUrl:''};";
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "13", UserOperationLogs.IMPORT, "导入学生干部评选结果，成功条数："+ (volist.size() - importFailureList.size())+"  失败条数："+importFailureList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rendResponseStr = "{statusCode:300,message:'操作失败!"+e.getMessage()+"'};";
		}finally {
			StringBuffer html = new StringBuffer();
			html.append("<script>");
			html.append("var response = "+rendResponseStr);
			html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
			html.append("</script>");
			renderHtml(response, html.toString());
		}
	}

	/**
	 * 导出失败信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/exportCadreInfoError.html")
	public void exportUserOrderInfoerror(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入学生干部评选结果失败记录.xls", disFile.getAbsolutePath(),true);
	}

	/**
	 * 导出汇总信息
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/exportSummary.html")
	public void exportSummary(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		String schoolConnectName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolConnectName").getParamValue();
		String yearInfoid = request.getParameter("yearInfo.resourceid");
		try {
			// 获取处理后的参数
			Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);

			String resourceids = request.getParameter("resourceids");
			List<CadreInfo> cadreInfoList = new ArrayList<CadreInfo>();

			if (ExStringUtils.isNotBlank(resourceids)) {
				condition.clear();
				condition.put("resourceid-idList", Arrays.asList(resourceids.split(",")));
			}
			YearInfo yearInfo = yearInfoService.get(yearInfoid);

			condition.put("isAppoint","Y");
			condition.put("orderBy","yearInfo.yearName,term desc,studentInfo.classes.classname,studyNo asc");
			cadreInfoList = cadreInfoService.findByCondition(condition);

			if(cadreInfoList!=null && cadreInfoList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");

				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("Code.WorkManage.department");
				dictCodeList.add("Code.WorkManage.position");
				dictCodeList.add("yesOrNo_default");
				Map<String,Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				Map<String,Object> paramMap = new HashMap<String, Object>();

				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"cadreInfoSummary", cadreInfoList,dictMap);
				exportExcelService.getModelToExcel().setHeader(schoolName+yearInfo.getFirstYear()+"年度班干部评选结果汇总表");//设置大标题
				exportExcelService.getModelToExcel().setRowHeight(500);//设置行高
				WritableFont font = new WritableFont(WritableFont.ARIAL, 10);
				font.setBoldStyle(WritableFont.BOLD);
				WritableCellFormat format = new WritableCellFormat(font);
				format.setAlignment(Alignment.CENTRE);
				format.setBackground(Colour.YELLOW);
				format.setBorder(Border.ALL, BorderLineStyle.THIN);
				exportExcelService.getModelToExcel().setTitleCellFormat(format);
				exportExcelService.getModelToExcel().setRowHeight(300);
				File excelFile = exportExcelService.getExcelFile();
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, "学生干部评选结果汇总.xls", disFile.getAbsolutePath(),true);
			}
		} catch (Exception e) {
			logger.error("导出学生干部申请信息出错", e.getMessage());
		}
	}

	/**
	 * 查看详细信息
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/viewCadreInfo.html")
	public String viewCadreInfo(HttpServletRequest request,ModelMap model) {
		String resourceid = request.getParameter("resourceid");
		CadreInfo cadreInfo = cadreInfoService.get(resourceid);
		model.addAttribute("cadreInfo",cadreInfo);
		return "edu3/work/cadreInfoManage/cadreInfo_view";
	}

	/**
	 * 打印预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/printView.html")
	public String printView(HttpServletRequest request,ModelMap model) {
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);
		return "edu3/work/cadreInfoManage/cadreInfo_printView";
	}

	/**
	 * 打印
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/printOrExport.html")
	public void print(HttpServletRequest request,HttpServletResponse response) {
		List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>(); // 打印
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		try {

			List<Map<String,Object>> studentInfoMaps = cadreInfoService.getApplicationFormInfo(condition);

			String reportPath = File.separator+"reports"+File.separator+"work"+File.separator+"cadreApplyInfo.jasper"+File.separator;
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");

			Map<String,String> positionMap = dictionaryService.getDictMapByParentDictCode("Code.WorkManage.position");
			Map<String,String> genderMap = dictionaryService.getDictMapByParentDictCode("CodeSex");
			Map<String,String> nationMap = dictionaryService.getDictMapByParentDictCode("CodeNation");
			Map<String,String> politicsMap = dictionaryService.getDictMapByParentDictCode("CodePolitics");
			Map<String,String> isExistMap = dictionaryService.getDictMapByParentDictCode("isExist");
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
			for(Map<String,Object> stuInfo : studentInfoMaps){
				// 数据
				param.put("unitName", stuInfo.get("unitname"));
				param.put("majorName", stuInfo.get("majorName"));
				param.put("className", stuInfo.get("CLASSESNAME"));
				param.put("classNum", ExStringUtils.toString(stuInfo.get("studentnum")));
				param.put("studentName", stuInfo.get("studentName"));
				param.put("position",positionMap.get(stuInfo.get("position")));
				param.put("gender",genderMap.get(stuInfo.get("gender")));
				param.put("nation",nationMap.get(stuInfo.get("nation")));
				param.put("politics",politicsMap.get(stuInfo.get("politics")));
				param.put("photopath",stuInfo.get("photopath"));
				param.put("position_current",positionMap.get(stuInfo.get("position_current")));
				param.put("position_adjust",positionMap.get(stuInfo.get("position_adjust")));
				param.put("avgScore1",ExStringUtils.toString(stuInfo.get("avgScore1")));
				param.put("avgScore2",ExStringUtils.toString(stuInfo.get("avgScore2")));
				param.put("awards",ExStringUtils.toString(stuInfo.get("awards")));
				param.put("workExperience",ExStringUtils.toString(stuInfo.get("workExperience")));
				param.put("intention",ExStringUtils.toString(stuInfo.get("intention")));
				param.put("hasNoPass",isExistMap.get(stuInfo.get("hasNoPass")));
				param.put("bornDay",ExDateUtils.formatDateStr((Date) stuInfo.get("bornDay"),1));
				param.put("phone",stuInfo.get("mobile"));

				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				list.add(new HashMap<String, Object>());
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
				JasperPrint jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
				jasperPrints.add(jasperPrint);
			}

			JasperPrint jasperPrint = ExBeanUtils.convertListToJasperPrint(jasperPrints);

			if (jasperPrints.size()>0) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		} catch (UnsupportedEncodingException e) {
			logger.error("编码方式不支持", e);
		} catch (Exception e) {
			logger.error("打印出错", e);
		}
	}

	/**
	 * 删除 学生干部信息
	 * @param request
	 * @param response
	 * @param model
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/cadreInfoManage/remove.html")
	public void deleteCadreInfo(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException{
		String resourceid = request.getParameter("resourceid");
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
			if(ExStringUtils.isNotBlank(resourceid)){
				if(resourceid.split("\\,").length >0) {
					cadreInfoService.batchDelete(resourceid.split("\\,"));
				}
				map.put("statusCode", 200);
				map.put("message", "删除成功！");
				map.put("forward", request.getContextPath()+"/edu3/work/cadreInfoManage/list.html");
			}
		} catch (Exception e) {
			logger.error("删除出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "删除出错！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
}
