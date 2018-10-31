package com.hnjk.edu.work.controller;

import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.*;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.BaseSupportController;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.context.Constants;
import com.hnjk.edu.basedata.model.Grade;
import com.hnjk.edu.basedata.model.Major;
import com.hnjk.edu.basedata.model.YearInfo;
import com.hnjk.edu.basedata.service.IGradeService;
import com.hnjk.edu.basedata.service.IYearInfoService;
import com.hnjk.edu.roll.model.Classes;
import com.hnjk.edu.roll.model.StudentInfo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.teaching.service.IExamResultsService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.edu.work.model.Appraising;
import com.hnjk.edu.work.model.CadreInfo;
import com.hnjk.edu.work.model.UserTimeManage;
import com.hnjk.edu.work.service.IAppraisingService;
import com.hnjk.edu.work.service.ICadreInfoService;
import com.hnjk.edu.work.service.IUserTimeManageService;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
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
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;
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
 * Function : 学生评优信息 - 控制器
 * <p>Author : msl
 * <p>Date   : 2018-07-24
 * <p>Description : extends FileUploadAndDownloadSupportController
 */
@Controller
public class AppraisingController extends FileUploadAndDownloadSupportController {

	@Autowired
	@Qualifier("userTimeManageService")
	private IUserTimeManageService userTimeManageService;

	@Autowired
	@Qualifier("appraisingService")
	private IAppraisingService appraisingService;

	@Autowired
	@Qualifier("yearInfoService")
	private IYearInfoService yearInfoService;

	@Autowired
	@Qualifier("gradeService")
	private IGradeService gradeService;

	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;

	@Autowired
	@Qualifier("examResultsService")
	private IExamResultsService examResultsService;

	@Autowired
	@Qualifier("cadreInfoService")
	private ICadreInfoService cadreInfoService;

	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;

	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;

	/**
	 * 学生评优信息 - 分页查询
	 * @param request
	 * @param page
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/appraisingManage/list.html")
	public String findAppraisingPage(HttpServletRequest request, Page page, ModelMap model) {
		page.setOrderBy("yearInfo.yearName desc,studyNo");
		page.setOrder(Page.ASC);
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
		Page pageResult = appraisingService.findByCondition(page,condition);
		model.addAttribute("isStudent",isStudent);
		model.addAttribute("condition",condition);
		model.addAttribute("pageResult",pageResult);
		return "/edu3/work/appraisingManage/appraising_list";
	}

	/**
	 *  新增/编辑 学生评优信息
	 * @param resourceid
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/appraisingManage/input.html")
	public String editAppraising(String resourceid,ModelMap model) throws WebException {

		String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
		Appraising appraising = new Appraising();
		boolean isStudent = SpringSecurityHelper.isStudent();
		if (ExStringUtils.isNotBlank(resourceid)) { //-----编辑
			appraising = appraisingService.get(resourceid);
			model.addAttribute("appraising", appraising);
		} else {
			YearInfo yearInfo = yearInfoService.findUniqueByProperty("firstYear",ExDateUtils.getCurrentYear());
			if (isStudent) {
				StudentInfo studentInfo = null;
				try {
					studentInfo = studentInfoService.getByUserId(SpringSecurityHelper.getCurrentUser().getResourceid());
					appraising.setStudyNo(studentInfo.getStudyNo());
					//appraising.setStudentName(studentInfo.getStudentName());
					appraising.setStudentInfo(studentInfo);
				} catch (Exception e) {
					logger.error("获取学籍信息出错："+e.getMessage());
				}
			}
			appraising.setYearInfo(yearInfo);
		}
		model.addAttribute("schoolCode",schoolCode);
		model.addAttribute("isStudent",isStudent);
		model.addAttribute("appraising", appraising);
		return "edu3/work/appraisingManage/appraising_form";
	}

	/**
	 * 根据学生学号查询学籍信息
	 * @param studyNo
	 * @param response
	 */
	@RequestMapping("/edu3/work/appraisingManage/validateStudyNo.html")
	public void getStudentInfoByStudyNo(String studyNo,String yearInfoid,HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode = 200;
		String message = "";
		try {
			YearInfo yearInfo = yearInfoService.get(yearInfoid);
			User user = SpringSecurityHelper.getCurrentUser();
			StudentInfo studentInfo = studentInfoService.findUniqueByProperty("studyNo",studyNo);
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				if (studentInfo!=null && !user.getOrgUnit().getResourceid().equals(studentInfo.getBranchSchool().getResourceid())) {
					message = "不允许选择其它教学点学生！";
					statusCode = 300;
				}
			}
			Appraising appraising = new Appraising();
			isMatchCondition(appraising,studentInfo,yearInfo);
			map.put("branchSchoolid",studentInfo.getBranchSchool().getResourceid());
			map.put("avgScore",appraising.getAvgScore());
			map.put("courseCondition",appraising.getCourseCondition());
			map.put("isClassLeader",appraising.getIsClassLeader());
			map.put("studentName",studentInfo.getStudentName());
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
	 * 判断学生是否符合评优标准
	 * <p>1、非统考平均75分以上，统考各科70分以上；
	 * <p>2、担任班长以上职位，非统考平均70分以上，统考各科65分以上；
	 * <p>3、或各科成绩及格
	 * @param appraising
	 * @param studentInfo
	 * @return
	 */
	private boolean isMatchCondition(Appraising appraising,StudentInfo studentInfo,YearInfo yearInfo) {

		Map<String,Object> scoreMap = examResultsService.getAvgScoreGroupByTeachType(studentInfo.getResourceid(),ExStringUtils.toString(yearInfo.getFirstYear()));
		Boolean isAllPass = (Boolean) scoreMap.get("isAllPass");
		Boolean isAllPassForNetworkStudyScore = (Boolean) scoreMap.get("isAllPassForNetworkStudyScore");
		Double  avgScoreForFaceStudy = (Double) scoreMap.get("avgScoreForFaceStudy");
		Double  avgScoreForNetworkStudy = (Double) scoreMap.get("avgScoreForNetworkStudy");
		Boolean hasNetworkStudyScore = (Boolean) scoreMap.get("hasNetworkStudyScore");
		Double minNetworkStudyScore = (Double) scoreMap.get("minNetworkStudyScore");

		CadreInfo CadreInfo = cadreInfoService.getPositionByYear(studentInfo.getStudyNo(),yearInfo.getResourceid());
		String errorMessage = "";
		int courseCondition = 4;//无统考成绩
		String isClassLeader = "否";
		if (hasNetworkStudyScore) {
			if (avgScoreForNetworkStudy >= 70) {
				courseCondition = 3;
			} else if (avgScoreForNetworkStudy >= 65) {
				courseCondition = 2;
			} else if (isAllPassForNetworkStudyScore) {
				courseCondition = 1;
			} else {
				courseCondition = -1;//不及格
			}
		}

		if (!isAllPass) {
			errorMessage = "各科成绩没有全部及格";
			if (CadreInfo != null) {
				isClassLeader = "是";
			} else {
				//判断是否担任班长
				if(ExStringUtils.isContainsStr(studentInfo.getClasses().getClassesLeaderId(),studentInfo.getResourceid())){
					isClassLeader = "班长";
				}
			}
			if ("否".equals(isClassLeader)) {//非班干部
				if (avgScoreForFaceStudy < 75) {
					errorMessage += "，非统考成绩平均分低于75分";
				}else if(courseCondition < 3){
					errorMessage += "，统考成绩最低分低于70";
				}
			} else {//班干部
				if (avgScoreForFaceStudy < 70) {
					errorMessage += "，非统考成绩平均分低于70分";
				}else if(courseCondition < 2){
					errorMessage += "，统考成绩最低分低于65";
				}
			}
		}
		appraising.setIsClassLeader(isClassLeader);
		appraising.setAvgScore(avgScoreForFaceStudy.toString());
		appraising.setErrorMessage(errorMessage);
		String courseConditionStr = "";
		switch (courseCondition) {
			case -1:
				courseConditionStr = "不及格";
				break;
			case 1:
				courseConditionStr = "及格";
				break;
			case 2:
				courseConditionStr = "65分以上";
				break;
			case 3:
				courseConditionStr = "70分以上";
				break;
			case 4:
				courseConditionStr = "无统考成绩";
				break;
			default:
				break;
		}
		appraising.setCourseCondition(courseConditionStr);
		if (errorMessage.contains("，")) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 保存 学生评优信息
	 * @param appraising
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/appraisingManage/save.html")
	public void saveAppraising(Appraising appraising, HttpServletRequest request, HttpServletResponse response) throws WebException{
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			do{
				String yearid = ExStringUtils.trimToEmpty(request.getParameter("yearInfoId"));
				String branchSchoolid = ExStringUtils.trimToEmpty(request.getParameter("branchSchoolid"));
				YearInfo yearInfo = yearInfoService.get(yearid);
				StudentInfo studentInfo = null;

				if(ExStringUtils.isNotBlank(appraising.getResourceid())){ //--------------------更新
					Appraising persistuserCadreInfo = appraisingService.get(appraising.getResourceid());
					studentInfo = persistuserCadreInfo.getStudentInfo();
					ExBeanUtils.copyProperties(persistuserCadreInfo, appraising);
					appraising = persistuserCadreInfo;

				}else{ //-------------------------------------------------------------------新增
					if(ExStringUtils.isNotBlank(yearid)){
						yearInfo = yearInfoService.findUniqueByProperty("resourceid", yearid);
					}
					studentInfo = studentInfoService.findUniqueByProperty("studyNo",appraising.getStudyNo());
					appraising.setStudyNo(studentInfo.getStudyNo());
					appraising.setStudentName(studentInfo.getStudentName());
					Map<String,Object> condition = new HashMap<String, Object>();
					Date currentDate = new Date();
					condition.put("yearInfo.resourceid",yearid);
					condition.put("unit.resourceid",branchSchoolid);
					condition.put("workType","2");
					condition.put("startTime-end",currentDate);//开始时间小于当前时间
					condition.put("endTime-start",currentDate);//结束时间大于当前时间
					List<UserTimeManage> userTimeManageList = userTimeManageService.findByCondition(condition);
					if (!(userTimeManageList != null && userTimeManageList.size() > 0)) {
						map.put("message", "不在年度评优工作时间范围内！");
						continue;
					}
				}
				if (ExStringUtils.isBlank(appraising.getAuditStatus())) {
					appraising.setAuditStatus("W");
				}
				/*boolean isMatchCondition = isMatchCondition(appraising,studentInfo,yearInfo);
				if (!isMatchCondition) {
					map.put("statusCode", 300);
					map.put("message", appraising.getErrorMessage());
					continue;
				}*/

				if (studentInfo == null) {
					map.put("statusCode", 300);
					map.put("message", "学籍信息不能为空！");
					continue;
				}

				if (yearInfo == null) {
					map.put("statusCode", 300);
					map.put("message", "年度不能为空！");
					continue;
				}

				appraising.setYearInfo(yearInfo);
				appraising.setStudentInfo(studentInfo);
				appraising.setSelfAssessment(ExStringUtils.trimToEmpty(appraising.getSelfAssessment()));
				appraising.setMemo(ExStringUtils.trimToEmpty(appraising.getMemo()));
				appraisingService.saveOrUpdate(appraising);
				map.put("statusCode", 200);
				map.put("message", "保存成功！");
				map.put("navTabId", "RES_WORK_APPRAISING_LIST");

			}while(false);
		}catch (Exception e) {
			logger.error("保存出错：{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "保存失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 审核、复审、删除
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/work/appraisingManage/operate.html")
	public void operateCadreInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String,Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String operatingType = ExStringUtils.trimToEmpty(request.getParameter("operatingType"));

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String contents = "【学生评优——";
			int count = appraisingService.operateAppraisingInfo(operatingType,condition);
			if("pass".equals(operatingType)){
				operatingType = UserOperationLogs.PASS;
				contents += "审核通过】";
			}else if("notpass".equals(operatingType)){
				operatingType = UserOperationLogs.REPEAL;
				contents += "审核不通过】";
			}else if ("delete".equals(operatingType)) {
				operatingType = UserOperationLogs.DELETE;
				contents += "删除】";
			} else if ("recheck".equals(operatingType)) {
				operatingType = UserOperationLogs.PASS;
				contents += "复审通过】";
			}
			contents += condition.toString();
			map.put("count", count);
			map.put("success", true);
			map.put("msg", "操作成功！");
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "13", operatingType, contents);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			map.put("success", true);
			map.put("msg", "操作失败！");
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}

	/**
	 * 导出优秀学生评选结果
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/work/appraisingManage/export.html")
	public void exportApprasingInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		// 获取处理后的参数
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String yearInfoid = request.getParameter("yearInfo.resourceid");
		String appraisingType = request.getParameter("type");
		String gradeid = request.getParameter("studentInfo.grade.resourceid");
		String resourceids = request.getParameter("resourceids");
		List<Appraising> appraisingList = new ArrayList<Appraising>();
		try {
			YearInfo yearInfo = yearInfoService.get(yearInfoid);
			Grade grade = gradeService.get(gradeid);
			if (yearInfo == null) {
				new Throwable("请选择年度！");
			}
			if (grade == null) {
				new Throwable("请选择年级！");
			}
			if (ExStringUtils.isNotBlank(resourceids)) {
				condition.put("resourceid-idList", Arrays.asList(resourceids.split(",")));
			}
			appraisingList = appraisingService.findByCondition(condition);

			if(ExCollectionUtils.isNotEmpty(appraisingList)){
				int order = 1;
				for (Appraising appraising : appraisingList) {
					appraising.setOrder(order++);
				}
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");

				List<String> dictCodeList = new ArrayList<String>();
				dictCodeList.add("Code.WorkManage.appraisingType");
				dictCodeList.add("CodeSex");
				String appraisingTypeName = dictionaryService.dictCode2Val("Code.WorkManage.appraisingType",appraisingType);
				Map<String,Object> dictMap = dictionaryService.getDictionByMap(dictCodeList, true, IDictionaryService.PREKEY_TYPE_BYCODE);
				Map<String,Object> paramMap = new HashMap<String, Object>();
				String yearAndTerm = yearInfo.getFirstYear()+"";
				paramMap.put("yearAndTerm",yearAndTerm);
				//初始化配置参数
				String templateFilepathString = "appraisingInfoModel.xls";
				paramMap.put("title",schoolName+yearAndTerm+appraisingTypeName+"评选结果汇总表 （"+grade.getGradeName()+"）");
				exportExcelService.initParmasByfile(disFile,"appraisingInfoModel", appraisingList,dictMap);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, paramMap);

				File excelFile = exportExcelService.getExcelFile();
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
				downloadFile(response, yearAndTerm+appraisingTypeName+"评选结果汇总表.xls", disFile.getAbsolutePath(),true);
			}
		} catch (Exception e) {
			logger.error("导出学生干部申请信息出错", e);
		}
	}

	/**
	 * 打印预览
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/work/appraisingManage/printView.html")
	public String printView(HttpServletRequest request,ModelMap model) {
		Condition2SQLHelper.addMapFromResquestByIterator(request,model);
		return "edu3/work/appraisingManage/appraising_printView";
	}

	/**
	 * 打印
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/work/appraisingManage/printOrExport.html")
	public void print(HttpServletRequest request,HttpServletResponse response) {

		String yearInfoid = request.getParameter("yearInfoid");
		String appraisingType = request.getParameter("type");
		String schoolName = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
		Map<String, Object> condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		List<JasperPrint> jasperPrints = new ArrayList<JasperPrint>(); // 打印
		try {
			String appraisingTypeName = dictionaryService.dictCode2Val("Code.WorkManage.appraisingType",appraisingType);

			Map<String,String> positionMap = dictionaryService.getDictMapByParentDictCode("Code.WorkManage.position");
			Map<String,String> genderMap = dictionaryService.getDictMapByParentDictCode("CodeSex");

			YearInfo yearInfo = yearInfoService.get(yearInfoid);
			if (yearInfo == null) {
				new Throwable("请选择年度！");
			}
			String reportPath = File.separator+"reports"+File.separator+"work"+File.separator+"appraisingApplyInfo.jasper"+File.separator;
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(reportPath),"utf-8");
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("title",schoolName+yearInfo.getFirstYear()+appraisingTypeName+"登记表");

			List<Map<String,Object>> studentInfoMaps = appraisingService.getApplicationFormInfo(condition);
			for(Map<String,Object> stuInfo : studentInfoMaps){
				//param.putAll(stuInfo);
				param.put("unitName",stuInfo.get("unitName"));
				param.put("gradeName",stuInfo.get("gradeName"));
				param.put("classesName",stuInfo.get("classesName"));
				param.put("isClassLeader",stuInfo.get("isClassLeader"));
				param.put("studentName",stuInfo.get("studentName"));
				param.put("studyNo",stuInfo.get("studyNo"));

				// 处理数据
				param.put("position",positionMap.get(stuInfo.get("position_current")));
				param.put("gender",genderMap.get(stuInfo.get("gender")));
				param.put("selfAssessment",stuInfo.get("selfAssessment")==null?"":stuInfo.get("selfAssessment"));
				if (ExStringUtils.isNotBlank(stuInfo.get("jobTime"))) {
					param.put("jobTime",ExDateUtils.formatDateStr((Date) stuInfo.get("jobTime"),1));
				}
				List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
				list.add(new HashMap<String, Object>());
				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(list);
				JasperPrint jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
				jasperPrints.add(jasperPrint);
			}
			JasperPrint jasperPrint = ExBeanUtils.convertListToJasperPrint(jasperPrints);
			if (jasperPrint!=null) {
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
}
