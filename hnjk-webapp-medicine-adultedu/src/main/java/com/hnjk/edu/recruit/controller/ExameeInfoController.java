package com.hnjk.edu.recruit.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

import org.apache.commons.lang.ArrayUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bcloud.msg.http.HttpSender;
import com.hnjk.core.exception.WebException;
import com.hnjk.core.foundation.utils.ExBeanUtils;
import com.hnjk.core.foundation.utils.ExCollectionUtils;
import com.hnjk.core.foundation.utils.ExDateUtils;
import com.hnjk.core.foundation.utils.ExStringUtils;
import com.hnjk.core.foundation.utils.FileUtils;
import com.hnjk.core.foundation.utils.GUIDUtils;
import com.hnjk.core.foundation.utils.HttpHeaderUtils;
import com.hnjk.core.foundation.utils.JsonUtils;
import com.hnjk.core.foundation.utils.XMLUtils;
import com.hnjk.core.foundation.utils.ZipUtils;
import com.hnjk.core.rao.configuration.property.ConfigPropertyUtil;
import com.hnjk.core.rao.dao.helper.Page;
import com.hnjk.core.rao.dao.jdbc.IBaseSupportJdbcDao;
import com.hnjk.core.support.base.controller.FileUploadAndDownloadSupportController;
import com.hnjk.core.support.base.model.AttachInfo;
import com.hnjk.core.support.context.Constants;
import com.hnjk.core.support.context.SystemContextHolder;
import com.hnjk.edu.basedata.service.IMajorService;
import com.hnjk.edu.finance.model.TempStudentFee;
import com.hnjk.edu.finance.service.IStudentPaymentService;
import com.hnjk.edu.finance.service.ITempStudentFeeService;
import com.hnjk.edu.finance.service.IYearPaymentStandardService;
import com.hnjk.edu.finance.util.TonlyPayUtil;
import com.hnjk.edu.finance.vo.HeadVO;
import com.hnjk.edu.recruit.helper.NoteCode;
import com.hnjk.edu.recruit.model.EnrolleeInfo;
import com.hnjk.edu.recruit.model.ExameeInfo;
import com.hnjk.edu.recruit.model.ExameeInfoScore;
import com.hnjk.edu.recruit.model.ExameeInfoWish;
import com.hnjk.edu.recruit.model.ExamineeChangeInfo;
import com.hnjk.edu.recruit.model.Predistribution;
import com.hnjk.edu.recruit.model.RecruitMajor;
import com.hnjk.edu.recruit.service.IEnrolleeInfoService;
import com.hnjk.edu.recruit.service.IExameeInfoService;
import com.hnjk.edu.recruit.service.IExamineeChangeInfoService;
import com.hnjk.edu.recruit.service.IRecruitMajorService;
import com.hnjk.edu.recruit.service.IRecruitPlanService;
import com.hnjk.edu.recruit.util.BarcodeUtil;
import com.hnjk.edu.recruit.vo.EnrollStatisticalVo;
import com.hnjk.edu.recruit.vo.EnrolleeInfoExportVO;
import com.hnjk.edu.recruit.vo.ExameeInfoCancelPrintVo;
import com.hnjk.edu.recruit.vo.ExameeInfoVo;
import com.hnjk.edu.roll.service.IClassesService;
import com.hnjk.edu.roll.service.IStudentInfoService;
import com.hnjk.edu.util.Condition2SQLHelper;
import com.hnjk.edu.util.Toolkit;
import com.hnjk.extend.plugin.excel.service.IExportExcelService;
import com.hnjk.extend.plugin.excel.service.IImportExcelService;
import com.hnjk.platform.system.UserOperationLogsHelper;
import com.hnjk.platform.system.cache.CacheAppManager;
import com.hnjk.platform.system.model.Attachs;
import com.hnjk.platform.system.model.SysConfiguration;
import com.hnjk.platform.system.model.UserOperationLogs;
import com.hnjk.platform.system.service.IAttachsService;
import com.hnjk.platform.system.service.IDictionaryService;
import com.hnjk.platform.taglib.JstlCustomFunction;
import com.hnjk.security.SpringSecurityHelper;
import com.hnjk.security.model.OrgUnit;
import com.hnjk.security.model.User;
import com.hnjk.security.service.IOrgUnitService;
/**
 * 考生信息管理.
 * <code>ExameeInfoController</code><p>
 * 
 * @author： 广东学苑教育发展有限公司
 * @since： 2012-9-3 下午03:17:21
 * @see 
 * @version 1.0
 */
@Controller
public class ExameeInfoController extends FileUploadAndDownloadSupportController {

	private static final long serialVersionUID = 688826077938813243L;
	
//	private static final Logger LOG = LoggerFactory.getLogger(ExameeInfoController.class);

	@Autowired
	@Qualifier("examineeChangeInfoService")
	private IExamineeChangeInfoService examineeChangeInfoService;
	
	@Autowired
	@Qualifier("exameeInfoService")
	private IExameeInfoService exameeInfoService;
	
	@Autowired
	@Qualifier("recruitPlanService")
	private IRecruitPlanService recruitPlanService;
	
	@Autowired
	@Qualifier("attachsService")
	private IAttachsService attachsService;
	
	@Autowired
	@Qualifier("orgUnitService")
	private IOrgUnitService orgUnitService;
	
	@Autowired
	@Qualifier("enrolleeInfoService")
	private IEnrolleeInfoService enrolleeInfoService;
	
	@Autowired
	@Qualifier("exportExcelService")
	private IExportExcelService exportExcelService;//Excel导出服务
	
	@Autowired
	@Qualifier("dictService")
	private IDictionaryService dictionaryService;
	
	@Autowired
	@Qualifier("yearPaymentStandardService")
	private IYearPaymentStandardService yearPaymentStandardService;
	
	@Autowired
	@Qualifier("importExcelService")
	private IImportExcelService importExcelService;
	
	@Autowired
	@Qualifier("studentPaymentService")
	private IStudentPaymentService studentPaymentService;
	
	@Autowired
	@Qualifier("majorService")
	private IMajorService majorService;
	
	@Autowired
	@Qualifier("recruitMajorService")
	private IRecruitMajorService recruitMajorService;
	
	@Autowired
	@Qualifier("classesService")
	private IClassesService classesService;
	
	@Autowired
	@Qualifier("tempStudentFeeService")
	private ITempStudentFeeService tempStudentFeeService;
	
	@Autowired
	@Qualifier("studentinfoservice")
	private IStudentInfoService studentInfoService;
	
	@Autowired
	@Qualifier("baseSupportJdbcDao")
	private IBaseSupportJdbcDao baseSupportJdbcDao;//JDBC 支持
	/**
	 * 手动触发生成学生缴费标准
	 * @param recruitPlanId
	 * @param name
	 * @param enrolleeCode
	 * @param certNum
	 * @param examCertificateNo
	 * @param request
	 * @param objPage
	 * @param model
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/recruit/exameeinfo/fee.html")
	public void getStudentPayment(HttpServletRequest request,HttpServletResponse response,Page objPage, ModelMap model) throws WebException{		
		objPage.setOrder(Page.ASC);
		Map<String, Object> condition = new HashMap<String, Object>();
		Map<String ,Object> map = new HashMap<String, Object>();
		try {
//			String examCertificateNo  = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));//
			String name  = ExStringUtils.trimToEmpty(request.getParameter("name"));//
			String certNum  = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//
			String branchSchool1  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//
			String enrolleeCode  = ExStringUtils.trimToEmpty(request.getParameter("enrolleeCode"));//
			
			String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
			String recruitPlanId  = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));//招生批次
//			String kszt  = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
			String isExistsPhoto  = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//照片状态
//			if(ExStringUtils.isNotBlank(kszt)) condition.put("kszt", kszt);
//			if(ExStringUtils.isNotBlank(isExistsPhoto)) condition.put("isExistsPhoto", isExistsPhoto);
			if(ExStringUtils.isNotBlank(enrolleeCode)) {
				condition.put("enrolleeCode", enrolleeCode);
			}
			if(ExStringUtils.isNotBlank(recruitPlanId)) {
				condition.put("recruitPlan", recruitPlanId);
			}
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotBlank(branchSchool1)) {
				condition.put("branchSchool", branchSchool1);
			}
			
			User cureentUser = SpringSecurityHelper.getCurrentUser();
			String branchSchool=cureentUser.getOrgUnit().getResourceid();
//			recruitPlanId  = ExStringUtils.trimToEmpty(recruitPlanId);//
			name  = ExStringUtils.trimToEmpty(name);//
			
			if(ExStringUtils.isNotBlank(name)) {
				name=ExStringUtils.getEncodeURIComponentByTwice(name);
//					name=new String(name.getBytes("ISO-8859-1"),"utf-8");
				condition.put("name", name);
			}
			certNum  = ExStringUtils.trimToEmpty(certNum);//
			if(null!=cureentUser.getOrgUnit() && cureentUser.getOrgUnit().getUnitType().
					indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0) {
				condition.put("branchSchool", branchSchool);//判断用户是否为校外学习中心
			}
			
			
//			if(ExStringUtils.isNotBlank(examCertificateNo)) condition.put("examCertificateNo", examCertificateNo);
//			if(ExStringUtils.isNotBlank(name)) condition.put("name", name);
			if(ExStringUtils.isNotBlank(certNum)) {
				condition.put("certNum", certNum);
			}
				
			List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findListByCondition(condition);

			studentPaymentService.createStudentPayment(enrolleeInfoList);
			map.put("statusCode", 200);
			map.put("message", "成功<br/>");
		} catch (Exception e) {
			logger.error("出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "失败:<br/>"+e.getMessage());
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 上传招生dbf文件
	 * @param from
	 * @param planid
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/framework/recruit/exameeinfo/upload.html")
	public String uploadRecruitExameeInfo(String from, String planid, HttpServletRequest request,ModelMap model) throws WebException{		
		if(ExStringUtils.isNotBlank(planid)){
			model.addAttribute("recruitPlan", recruitPlanService.get(planid));			
		}	
		if("ExameeInfo".equals(from)) {//考生信息导入
			return "/edu3/recruit/exameeinfo/exameeinfo-upload";
		} else if("RecruitMajor".equals(from)) {//计划库导入
			return "/edu3/recruit/exameeinfo/recruitmajor-upload";
		} else {//学生照片导入
			return "/edu3/recruit/exameeinfo/exameeinfo-photo-upload";
		}		
	}
	/**
	 * 导入计划库并审批招生专业
	 * @param planid
	 * @param uploadfileid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/recruitmajor/audit.html")
	public void auditRecruitMajor(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(planid) && ExStringUtils.isNotBlank(attachId)){
				Attachs attach = attachsService.get(attachId);
				int passedMajorSize = exameeInfoService.importRecruitMajor(planid, attach.getSerPath()+File.separator+attach.getSerName());
				
				map.put("statusCode", 200);
				map.put("message", "成功审批 "+passedMajorSize+" 条招生专业");	
			}			
		} catch (Exception e) {
			logger.error("导入计划库出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入计划库失败:<br/>"+e.getMessage());
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 导入考生报名信息
	 * @param planid
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/import.html")
	public void importExameeInfo(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
//		try {			
			if(ExStringUtils.isNotBlank(planid) && ExStringUtils.isNotBlank(attachId)){				
				Map<String, Object> resultMap = exameeInfoService.importExameeInfo(planid, attachId, HttpHeaderUtils.getIpAddr(request));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.IMPORT, "导入报名信息，成功条数："+resultMap.get("successCount")+" ,失败条数："+resultMap.get("errorCount"));
				map.putAll(resultMap);		
				
				map.put("statusCode", 200);				
				map.put("reloadUrl", request.getContextPath()+"/edu3/recruit/exameeinfo/list.html");
				map.put("navTabId", "RES_RECRUIT_EXAMEEINFO");
				map.put("planid", planid);
			} else {
				map.put("statusCode", 300);				
				map.put("message", "请选择一个招生批次和附件.");
			}
//		} catch (Exception e) {
//			logger.error("导入考生信息出错:{}",e.fillInStackTrace());
//			map.put("statusCode", 300);
//			map.put("message", "导入报名信息失败:<br/>"+e.getMessage());
//		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}	
	/**
	 * 导入考生相片
	 * @param planid
	 * @param attachId
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/photo/import.html")
	public void importExameeInfoPhoto(String planid,String attachId, HttpServletRequest request,HttpServletResponse response) throws WebException{
		Map<String ,Object> map = new HashMap<String, Object>();
		String tempZipPath = null;//解压路径
		try {			
			if(ExStringUtils.isNotBlank(planid) && ExStringUtils.isNotBlank(attachId)){
				Attachs attach = attachsService.get(attachId);				
				tempZipPath = Constants.EDU3_DATAS_LOCALROOTPATH+"temp"+File.separator+ExStringUtils.substringBefore(attach.getSerName(), "."+attach.getAttType());
				ZipUtils.unZip(tempZipPath, attach.getSerPath()+File.separator+attach.getSerName());//解压到临时目录
				Map<String, Object> resultMap = exameeInfoService.importExameeInfoPhoto(planid, tempZipPath);
				map.putAll(resultMap);
				
				map.put("statusCode", 200);				
				map.put("reloadUrl", request.getContextPath()+"/edu3/recruit/exameeinfo/list.html");
				map.put("navTabId", "RES_RECRUIT_EXAMEEINFO");
				map.put("planid", planid);
			} else {
				map.put("statusCode", 300);				
				map.put("message", "请选择一个招生批次和附件.");
			}
		} catch (Exception e) {
			logger.error("导入考生相片出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "导入考生相片失败:<br/>"+e.getMessage());			
		} finally {
			try {
				if(ExStringUtils.isNotBlank(tempZipPath) && new File(tempZipPath).exists()){
					FileUtils.delFolder(tempZipPath);//清空临时解压目录
				}
			} catch (Exception e2) {
			}
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 查询考生信息列表
	 * @param recruitPlanId
	 * @param name
	 * @param enrolleeCode
	 * @param certNum
	 * @param examCertificateNo
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/list.html")
	public String listRecruitExameeInfo(String recruitPlanId, String name, String enrolleeCode, String certNum, String examCertificateNo,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{		
		objPage.setOrder(Page.ASC);
		objPage.setOrderBy("recruitPlan.yearInfo.firstYear desc,recruitPlan.term desc,recruitPlan.resourceid,KSH,resourceid");
		Map<String, Object> condition = new HashMap<String, Object>();
		String kszt = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
		String isExistsPhoto = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//是否存在相片
		String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学校
		if(ExStringUtils.isNotBlank(recruitPlanId)) {
			condition.put("recruitPlanId", recruitPlanId);
		}
		if(ExStringUtils.isNotBlank(name)) {
			condition.put("name", ExStringUtils.trimToEmpty(name));
		}
		if(ExStringUtils.isNotBlank(enrolleeCode)) {
			condition.put("enrolleeCode", ExStringUtils.trimToEmpty(enrolleeCode));
		}
		if(ExStringUtils.isNotBlank(certNum)) {
			condition.put("certNum", ExStringUtils.trimToEmpty(certNum));
		}
		if(ExStringUtils.isNotBlank(examCertificateNo)) {
			condition.put("examCertificateNo", ExStringUtils.trimToEmpty(examCertificateNo));
		}
		if(ExStringUtils.isNotBlank(kszt)) {
			condition.put("kszt", kszt);
		}
		if(ExStringUtils.isNotBlank(isExistsPhoto)) {
			condition.put("isExistsPhoto", isExistsPhoto);
		}
		String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
		if(ExStringUtils.isNotBlank(major)) {
			condition.put("major", major);
		}
		String classic  = ExStringUtils.trimToEmpty(request.getParameter("classic"));//
		if(ExStringUtils.isNotBlank(classic)) {
			condition.put("classic", classic);
		}
		String isPrint  = ExStringUtils.trimToEmpty(request.getParameter("isPrint"));//
		if(ExStringUtils.isNotBlank(isPrint)) {
			condition.put("isPrint", isPrint);
		}
		//2014-3-18 增加已录取未注册条件查询
		String registorFlag = ExStringUtils.trimToEmpty(request.getParameter("registorFlag"));
		if(ExStringUtils.isNotBlank(registorFlag)) {
			condition.put("registorFlag", registorFlag);
		}
		
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
			
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}
		
		//Page exameeInfoPage = exameeInfoService.findExameeInfoByCondition(objPage, condition);
		Page exameeInfoPage = exameeInfoService.findExameeInfoByConditionWithJDBC(objPage, condition);
		model.addAttribute("exameeInfoPage", exameeInfoPage);
		model.addAttribute("condition", condition);

		// 获取是否显示录取编号的全局参数
		SysConfiguration sysConfig = CacheAppManager.getSysConfigurationByCode("exameeInfo.NO.isShow");
		String isShow = "N";// 否（不显示）
		if(sysConfig != null) {
			isShow = sysConfig.getParamValue();
		}
		model.addAttribute("isShow", isShow);

		return "/edu3/recruit/exameeinfo/exameeinfo-list";
	}	
	/**
	 * 导出考生信息列表
	 * @param recruitPlanId
	 * @param name
	 * @param enrolleeCode
	 * @param certNum
	 * @param examCertificateNo
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/list-upload.html")
	public void expListRecruitExameeInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{		
		String headerName = "录取库";
		Map<String, Object> condition = new HashMap<String, Object>();
		String resIds = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		if (ExStringUtils.isNotBlank(resIds)) {
			condition.put("resIds", "'"+resIds.replaceAll(",", "','")+"'");
		} else {
			String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学校
			String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));//报名批次
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//考生姓名
			String enrolleeCode = ExStringUtils.trimToEmpty(request.getParameter("enrolleeCode"));//考生号
			String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//考生证件号码
			String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));//考生证件号码
			String kszt = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
			String isExistsPhoto = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//是否存在相片
			String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			String classic  = ExStringUtils.trimToEmpty(request.getParameter("classic"));//
			if(ExStringUtils.isNotBlank(classic)) {
				condition.put("classic", classic);
			}
			//2014-3-18 增加已录取未注册条件查询
			String registorFlag = ExStringUtils.trimToEmpty(request.getParameter("registorFlag"));
			if(ExStringUtils.isNotBlank(registorFlag)) {
				condition.put("registorFlag", registorFlag);
			}
			if(ExStringUtils.isNotBlank(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);
			}
			if(ExStringUtils.isNotBlank(name)) {
				condition.put("name", ExStringUtils.trimToEmpty(name));
			}
			if(ExStringUtils.isNotBlank(enrolleeCode)) {
				condition.put("enrolleeCode", ExStringUtils.trimToEmpty(enrolleeCode));
			}
			if(ExStringUtils.isNotBlank(certNum)) {
				condition.put("certNum", ExStringUtils.trimToEmpty(certNum));
			}
			if(ExStringUtils.isNotBlank(examCertificateNo)) {
				condition.put("examCertificateNo", ExStringUtils.trimToEmpty(examCertificateNo));
			}
			if(ExStringUtils.isNotBlank(kszt)){ 
				condition.put("kszt", kszt);
				String ksztName = JstlCustomFunction.dictionaryCode2Value("CodeEnrollStatus", kszt);
				headerName = headerName + ksztName;
			}
			if(ExStringUtils.isNotBlank(isExistsPhoto)) {
				condition.put("isExistsPhoto", isExistsPhoto);
			}
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool = user.getOrgUnit().getResourceid();
			}
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
		}
		
		try{
			List<Map<String,Object>> exameeInfoList = exameeInfoService.findExameeInfoByConditionWithJDBC(condition);
			headerName = headerName+"学生信息列表";
		 	setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		 	GUIDUtils.init();
		 	//导出
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			//字典转换列
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeEnrollStatus");
			
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "expExamInfoByJdbc", exameeInfoList,dictionaryService.getDictionByMap(dictCodeList,true,IDictionaryService.PREKEY_TYPE_BYCODE),null);
			exportExcelService.getModelToExcel().setHeader(headerName);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
				
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			String downloadFileName = headerName + ".xls";
			String downloadFilePath = excelFile.getAbsolutePath();
			downloadFile(response, downloadFileName,downloadFilePath,true);	
	 	 
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		 }
	}	
	
	/**
	 * 导出教学站分配名单
	 * @param recruitPlanId
	 * @param name
	 * @param enrolleeCode
	 * @param certNum
	 * @param examCertificateNo
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/exportunit.html")
	public void exportUnit(HttpServletRequest request,HttpServletResponse response) throws WebException{		
		String headerName = "录取库";
		Map<String, Object> condition = new HashMap<String, Object>();
		String resIds = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("resIds")));
		if (ExStringUtils.isNotBlank(resIds)) {
			condition.put("resIds", "'"+resIds.replaceAll(",", "','")+"'");
		} else {
			String branchSchool  = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("branchSchool")));//学校
			String recruitPlanId = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("recruitPlanId")));//报名批次
			String name = 		   ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByOne(request.getParameter("name")));//考生姓名
			String enrolleeCode =  ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("enrolleeCode")));//考生号
			String certNum = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("certNum")));//考生证件号码
			String examCertificateNo = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("examCertificateNo")));//考生证件号码
			String kszt = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("kszt")));//考生状态
			String isExistsPhoto = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("isExistsPhoto")));//是否存在相片
			String major  = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("major")));//
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			String classic  = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("classic")));//
			if(ExStringUtils.isNotBlank(classic)) {
				condition.put("classic", classic);
			}
			//2014-3-18 增加已录取未注册条件查询
			String registorFlag = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("registorFlag")));
			if(ExStringUtils.isNotBlank(registorFlag)) {
				condition.put("registorFlag", registorFlag);
			}
			if(ExStringUtils.isNotBlank(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);
			}
			if(ExStringUtils.isNotBlank(name)) {
				condition.put("name", ExStringUtils.trimToEmpty(name));
			}
			if(ExStringUtils.isNotBlank(enrolleeCode)) {
				condition.put("enrolleeCode", ExStringUtils.trimToEmpty(enrolleeCode));
			}
			if(ExStringUtils.isNotBlank(certNum)) {
				condition.put("certNum", ExStringUtils.trimToEmpty(certNum));
			}
			if(ExStringUtils.isNotBlank(examCertificateNo)) {
				condition.put("examCertificateNo", ExStringUtils.trimToEmpty(examCertificateNo));
			}
			if(ExStringUtils.isNotBlank(kszt)){ 
				condition.put("kszt", kszt);
				String ksztName = JstlCustomFunction.dictionaryCode2Value("CodeEnrollStatus", kszt);
				headerName = headerName + ksztName;
			}
			if(ExStringUtils.isNotBlank(isExistsPhoto)) {
				condition.put("isExistsPhoto", isExistsPhoto);
			}
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool = user.getOrgUnit().getResourceid();
			}
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
		}
		
		try{
			List<Map<String,Object>> exameeInfoList = exameeInfoService.findExameeInfoByConditionWithJDBC(condition);
			headerName = headerName+"学生信息列表";
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
			GUIDUtils.init();
			//导出
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			//字典转换列
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("CodeEnrollStatus");
			dictCodeList.add("CodeNation");
			dictCodeList.add("CodePolitics");
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "expExamInfoByJdbc", exameeInfoList,dictionaryService.getDictionByMap(dictCodeList,true,IDictionaryService.PREKEY_TYPE_BYCODE),null);
			exportExcelService.getModelToExcel().setHeader(headerName);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			String downloadFileName = headerName + ".xls";
			String downloadFilePath = excelFile.getAbsolutePath();
			downloadFile(response, downloadFileName,downloadFilePath,true);	
			
		}catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
			renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		}
	}	
	
	/**
	 * 跳转到导入窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/imputunit.html")
	public String imputUnit(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		model.addAttribute("url","/edu3/recruit/exameeinfo/importunit.html");
		model.addAttribute("navTabId","RES_TEACHING_TEACHINGPLANCOURSETIMETABLE");
		return "edu3/roll/imputDialogForErrorInfo";
	}
	
	/**
	 * 导入教学站分配情况
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/importunit.html")
	public void importUnit(String exportAct,HttpServletRequest request, HttpServletResponse response) throws WebException {
		User user = SpringSecurityHelper.getCurrentUser();
//		if(!user.getOrgUnit().getUnitType().equals(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())){//如果为校外学习中心人员
//			throw new WebException("只有教学站有权利导入！");
//		}
//		OrgUnit unit = user.getOrgUnit();
		//提示信息字符串
		String  rendResponseStr = "";
		List<ExameeInfoVo> falseList = new ArrayList<ExameeInfoVo>();
		File excelFile = null;
		String imurl = request.getParameter("importFile");
		//设置目标文件路径
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH + request.getParameter("importFile"));
		try {
			//上传文件到服务器
			List<AttachInfo> list = doUploadFile(request, response, null);
			AttachInfo attachInfo = list.get(0);
			//创建EXCEL对象 获得待导入的excel的内容
			File excel = new File(attachInfo.getSerPath() + File.separator+ attachInfo.getSerName());
			
			importExcelService.initParmas(excel, "exameeInfoVo",null);
			importExcelService.getExcelToModel().setSheet(0);// 设置excel sheet 0
			//获得待导入excel内容的List
			List modelList = importExcelService.getModelList();
			if(modelList==null){
				throw new Exception("导入模版错误！");
			}
			//转换为对应类型的List
			StringBuilder examee_ksh_zkzh = new StringBuilder(); 
			List<ExameeInfoVo> volist = new ArrayList<ExameeInfoVo>();
			List<OrgUnit> orgUnitList = orgUnitService.findOrgUnitListByType("brSchool");
			int inNum = 1;
			for (int i = 0; i < modelList.size(); i++) {
				boolean hasUnit = false;
				String errorMsg = "无法匹配到该教学站！";
				ExameeInfoVo exameeInfoVo = (ExameeInfoVo) modelList.get(i);
				//1、判断教学站是否为空，以及是否存在该教学点
				if (ExStringUtils.isEmpty(exameeInfoVo.getUNITNAME())) {
					errorMsg = "教学站为空！";
				}else {
					for (OrgUnit orgUnit : orgUnitList) {
						if(orgUnit.getUnitName().equals(exameeInfoVo.getUNITNAME())){
							hasUnit = true;
							exameeInfoVo.setOrgUnit(orgUnit);
							break;
						}
					}
				}
				if(!hasUnit){
					exameeInfoVo.setErrorMsg(errorMsg);
					falseList.add(exameeInfoVo);
				}else {
					if(inNum==500 && i>0){
						examee_ksh_zkzh.append("('"+exameeInfoVo.getKSH()+"','"+exameeInfoVo.getZKZH()+"')");
						examee_ksh_zkzh.append(") or (ei.KSH,ei.ZKZH) in (");
						inNum = 1;
					}else{
						examee_ksh_zkzh.append("('"+exameeInfoVo.getKSH()+"','"+exameeInfoVo.getZKZH()+"')");
						if(i!=modelList.size()-1){
							examee_ksh_zkzh.append(",");
							inNum++;
						}
					}
					volist.add(exameeInfoVo);
				}
			}
			//考生信息、录取信息、学籍信息
			StringBuilder hql = new StringBuilder();
			List<ExameeInfo> exameeInfoList = new ArrayList<ExameeInfo>();
			// 获取导入报名信息唯一标识
			String uniqueTag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			if(modelList!=null && modelList.size()>0){
				hql.append(" select ei.*,eei.resourceid enrolleeinfoid,si.resourceid studentid,si.branchschoolid");
				hql.append(" from EDU_RECRUIT_EXAMINEE ei ");
				hql.append(" left join edu_recruit_enrolleeinfo eei on eei.isdeleted=0 and ");
				if ("1".equals(uniqueTag)) {
					hql.append("eei.enrolleecode=ei.ksh");
				}else {
					hql.append("eei.examcertificateno=ei.zkzh");
				}
				hql.append(" left join edu_roll_studentinfo si on si.enrolleeCode=eei.enrolleecode and si.examCertificateNo=eei.examcertificateno and si.isDeleted=0");
				hql.append(" where (ei.KSH,ei.ZKZH) in("+examee_ksh_zkzh.toString()+")");
				try {
					exameeInfoList = baseSupportJdbcDao.getBaseJdbcTemplate().findList(hql.toString(), ExameeInfo.class, new HashMap<String, Object>());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//遍历需要导入的List 将对应内容写进DB 并同时检测数据的合法性
			if (null!=volist) {
				GUIDUtils.init();
				//初始第三方接口的东西
				List<Map<String,Object>> studentInfoList = new ArrayList<Map<String,Object>>();
				StringBuffer msg = new StringBuffer();
				int totalSize = 0;
				int faileSize = 0;// 失败数量
				Date today = new Date();
				String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
				// 接收异步通知接口
				String receiveUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/tempStudentFee/editInfo.html";
				HeadVO headVO = new HeadVO(Constants.EDU_STU_INFO_MODIFY,dateTime,receiveUrl);
				List<TempStudentFee> tsfUpdateList = new ArrayList<TempStudentFee>(100);
				for(ExameeInfoVo vo : volist){
					String falseMsg = "";
					// 判断是否已经注册学籍
					ExameeInfo exameeInfo = null;
					EnrolleeInfo enrolleeInfo = null;
					String hqlStr = "";
					OrgUnit orgUnit = vo.getOrgUnit();//分配教学点
					if ("1".equals(uniqueTag)) {
						hqlStr = "from "+EnrolleeInfo.class.getSimpleName()+" where enrolleeCode=?";
						enrolleeInfo = enrolleeInfoService.findUnique(hqlStr, vo.getKSH());
					} else {
						hqlStr = "from "+EnrolleeInfo.class.getSimpleName()+" where examCertificateNo=?";
						enrolleeInfo = enrolleeInfoService.findUnique(hqlStr, vo.getZKZH());
					}
					if(enrolleeInfo!=null && "Y".equals(enrolleeInfo.getRegistorFlag())){
						if(enrolleeInfo.getBranchSchool().getResourceid().equals(orgUnit.getResourceid())){
							continue;
						}
						falseMsg = "该学生已经注册学籍，请做转点学籍异动！";
						vo.setErrorMsg(falseMsg);
						falseList.add(vo);
						continue;
					}
					
					//判断是否存在该考生
					for (ExameeInfo eInfo : exameeInfoList) {
						if(vo.getKSH().equals(eInfo.getKSH()) && vo.getZKZH().equals(eInfo.getZKZH())){
							exameeInfo = eInfo;
							break;
						}
					}
					if(exameeInfo==null){
						vo.setErrorMsg("找不到该考生！");
						falseList.add(vo);
						continue;
					}
					Map<String, Object> condition = new HashMap<String, Object>();
					
					//判断招生专业是否存在该教学站
					RecruitMajor recruitMajor = null;
					hql.setLength(0);
					hql.append("select distinct ei.resourceid,rm.resourceid recruitmajorid,major.resourceid majorid");
					hql.append(" from EDU_RECRUIT_EXAMINEE ei ");
					hql.append(" left join edu_recruit_major rm on rm.recruitplanid=ei.recruitplanid ");
					hql.append(" and rm.teachingtype=ei.xxxsdm and rm.recruitmajorcode=ei.lqzy and rm.isdeleted=0");
					hql.append(" and rm.majorid in(select m.resourceid from edu_base_major m where m.resourceid=rm.majorid and m.majorname=ei.lqzymc)");
					hql.append(" and rm.classic in(select ci.resourceid from edu_base_classic ci where ci.resourceid=rm.classic and ci.classiccode=ei.ccdm)");
					hql.append(" and rm.brschoolid in(select u.resourceid from hnjk_sys_unit u where u.resourceid=rm.brschoolid and u.unitname=:unitName)");
					hql.append(" left join edu_base_major major on major.majorname=:majorName");
					hql.append(" where ei.resourceid=:exameeInfoId");
					
					condition.put("exameeInfoId", exameeInfo.getResourceid());
					condition.put("unitName", vo.getUNITNAME());
					condition.put("majorName", vo.getLQZYMC());
					List<Map<String, Object>> maps = baseSupportJdbcDao.getBaseJdbcTemplate().findForListMap(hql.toString(), condition);
					if(maps!=null && maps.size()>0){
						Map<String, Object> map = maps.get(0);
						if(ExStringUtils.isBlank(map.get("majorid"))){
							vo.setErrorMsg("无法匹配到该专业！");
							falseList.add(vo);
							continue;
						}
						if(ExStringUtils.isBlank(map.get("recruitmajorid"))){
							vo.setErrorMsg("招生批次中的招生专业不存在该教学站！");
							falseList.add(vo);
							continue;
						}else if(enrolleeInfo.getRecruitMajor()==null){
							//错误的专业是无法导入的，所以如果已经有了招生专业就无需重复设置
							recruitMajor = recruitMajorService.get(map.get("recruitmajorid").toString());
						}else {
							recruitMajor = enrolleeInfo.getRecruitMajor();
						}
					}
					
					//7、更新教学站
					if (enrolleeInfo != null) {
						/*//判断是否已经注册学籍
						String stuInfoHql = " from "+StudentInfo.class.getSimpleName()+" where isDeleted=0 and enrolleeCode=? and examCertificateNo=? and branchSchool!=?";
						List<StudentInfo> studentInfos = studentInfoService.findByHql(stuInfoHql, enrolleeInfo.getEnrolleeCode(),enrolleeInfo.getExamCertificateNo(),orgUnit);
						if(studentInfos!=null && studentInfos.size()>0){
							falseMsg = "该学生已经注册学籍，请做转点学籍异动！";
							vo.setErrorMsg(falseMsg);
							falseList.add(vo);
							continue;
						}*/
						OrgUnit _orgUnit = enrolleeInfo.getBranchSchool();
						if(recruitMajor!=null && recruitMajor!=enrolleeInfo.getRecruitMajor()){
							exameeInfo.setLQZYMC(vo.getLQZYMC());
							enrolleeInfo.setRecruitMajor(recruitMajor);
						}
						if(orgUnit!=null && _orgUnit!=orgUnit){
							enrolleeInfo.setBranchSchool(orgUnit);
						}
						enrolleeInfoService.update(enrolleeInfo);
						
						// 转出点或转入点为未分配，或转出点和转入点相同时不记录
						if(!("未分配".equals(_orgUnit.getUnitName()) 
								|| _orgUnit.getResourceid().equals(enrolleeInfo.getBranchSchool().getResourceid())
								|| "未分配".equals(enrolleeInfo.getBranchSchool().getUnitName()))){
							
							// 记录转点信息
							ExamineeChangeInfo examineeChangeInfo = new ExamineeChangeInfo();
							examineeChangeInfo.setRecruitPlan(exameeInfo.getRecruitPlan());
							examineeChangeInfo.setEnrolleeInfo(enrolleeInfo);
							examineeChangeInfo.setExamineeName(exameeInfo.getXM());
							examineeChangeInfo.setRolloutSchool(_orgUnit);
							examineeChangeInfo.setRollinSchool(enrolleeInfo.getBranchSchool());
							examineeChangeInfo.setMajor(enrolleeInfo.getRecruitMajor().getMajor());
							examineeChangeInfo.setClassic(enrolleeInfo.getRecruitMajor().getClassic());
							examineeChangeInfo.setOperator(user);
							examineeChangeInfo.setOperatorName(user.getCnName());// 中文名，不是用户名
							examineeChangeInfo.setOperateDate(new Date());
							examineeChangeInfoService.saveOrUpdate(examineeChangeInfo);
						}
						// 是否使用本平台缴费
						String usePlatformPayFees = CacheAppManager.getSysConfigurationByCode("usePlatformPayFees").getParamValue();
						// 同步学生注册缴费表
						// 导入报名信息唯一标示:0:准考证号，1:考生号
						condition.clear();
						if("Y".equals(usePlatformPayFees)){
							if ("1".equals(uniqueTag)) {
								condition.put("examCertificateNo", vo.getKSH());
							} else {
								condition.put("examCertificateNo", vo.getZKZH());
							}					
							List<TempStudentFee> tempStudentFees = tempStudentFeeService.findListByContidion(condition);
							for(TempStudentFee tsf : tempStudentFees){			
								if(recruitMajor!=null){
									tsf.setMajor(recruitMajor.getMajor());
								}
								//同步更新学生缴费信息表
								tsf.setUnit(orgUnit);
								tsfUpdateList.add(tsf);
							}
							// 是否使用通联支付
							String useTongLianPay = CacheAppManager.getSysConfigurationByCode("useTongLianPay").getParamValue();
							if("Y".equals(useTongLianPay)) {
								// 同步第三方缴费系统
								try{
									Map<String,Object> studentInfo = new HashMap<String, Object>();
									studentInfo.put("STUDENT_ID", (String)condition.get("examCertificateNo"));	
									studentInfo.put("MAJOR", recruitMajor.getMajor().getMajorName());
									studentInfo.put("COLLEGE", orgUnit.getUnitName());
									studentInfoList.add(studentInfo);
									
									if(studentInfoList.size()%50==0){
										String _msg = null;
										Document returnDoc =TonlyPayUtil.modifyStudentInfo(headVO, studentInfoList);
										msg = getReturnInfo(returnDoc, msg,faileSize);
										_msg = msg.toString();
										int sizeIndex = _msg.lastIndexOf("-");
										faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
										msg.setLength(0);
										totalSize+=50;
										studentInfoList.clear();
									}
								}catch(Exception e){
									logger.error("分配教学点时,同步第三方失败" + e);
								}
							}
							
						}
					}
				}
				// 更新预缴费订单
				if(ExCollectionUtils.isNotEmpty(tsfUpdateList)){
					tempStudentFeeService.batchSaveOrUpdate(tsfUpdateList);
				}
				//剩下的
				if(ExCollectionUtils.isNotEmpty(studentInfoList)){// 修改剩下的
					String _msg = null;
					Document returnDoc =TonlyPayUtil.modifyStudentInfo(headVO, studentInfoList);
					msg = getReturnInfo(returnDoc, msg,faileSize);
					_msg = msg.toString();
					int sizeIndex = _msg.lastIndexOf("-");
					faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
					totalSize+=studentInfoList.size();
					studentInfoList.clear();
				}
				logger.info("申请成功："+(totalSize-faileSize)+" 条，申请失败："+faileSize+" 条");
			}
			
			//导出分配教学站导入失败的信息以及原因
			if(falseList!=null&&falseList.size()>0){
				setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
				//导出
				GUIDUtils.init();
				String fileName = GUIDUtils.buildMd5GUID(false);
				File disFile = new File(getDistfilepath()+ File.separator + fileName + ".xls");
					
				//模板文件路径
				String templateFilepathString = "exameeinfoerror.xls";
				//初始化配置参数
				exportExcelService.initParmasByfile(disFile,"exameeInfoVoError", falseList,null);
				exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 2, null);
				exportExcelService.getModelToExcel().setRowHeight(400);
					
				excelFile = exportExcelService.getExcelFile();//获取导出的文件
				logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
//					downloadFile(response, "导入缴费信息失败名单.xls", excelFile.getAbsolutePath(),true);
				String upLoadurl = "/edu3/recruit/exameeinfo/importuniterror.html";
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (modelList.size() - falseList.size()) 
				+"条 | 导入失败"+  falseList.size()
				+"条！',forwardUrl:'"+upLoadurl+"?excelFile="+fileName+"'};";
			}
			if(ExStringUtils.isBlank(rendResponseStr)){
				rendResponseStr = "{statusCode:200,message:'"+"导入成功"+ (modelList.size() - falseList.size())  
				+"条 | 导入失败"+ falseList.size()
				+"条！',forwardUrl:''};";
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.IMPORT, "导入分配教学站，成功条数："+ (volist.size() - falseList.size())+"  失败条数："+falseList.size());
		} catch (Exception e) {
			e.printStackTrace();
			rendResponseStr = "{statusCode:300,message:'操作失败!"+e.getMessage()+"'};";
		}
		
		StringBuffer html = new StringBuffer();
		html.append("<script>");
		html.append("var response = "+rendResponseStr);
		html.append("if(window.parent.donecallback) window.parent.donecallback(response);");
		html.append("</script>");
		renderHtml(response, html.toString());
	}
	
	/**
	 * 导出失败信息
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/importuniterror.html")
	public void uploadFalseUnitToImport(String excelFile,HttpServletRequest request,HttpServletResponse response) throws Exception {
		setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		File disFile = new File(getDistfilepath()+ File.separator + excelFile+".xls");
		downloadFile(response, "导入教学站分配情况失败记录.xls", disFile.getAbsolutePath(),true);
	}	
	/**
	 * 跳转到分配窗口
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/updateinputunit.html")
	public String updateInputUnit(String ids, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		model.addAttribute("ids", ids);
		String majorName = request.getParameter("majorName").trim();
		if(ExStringUtils.isMessyCode(majorName)){
			majorName = ExStringUtils.getEncodeURIComponentByOne(majorName);
		}
		StringBuffer unitOption = new StringBuffer("<option value=''></option>");
		if (ExStringUtils.isNotBlank(ids)) {
			Map<String, Object> condition = new HashMap<String, Object>();
			ids = "'" + ids.replace(",", "','") + "'";
			condition.put("examineeid", ids);
			condition.put("isUnique", "Y");
			condition.put("majorName", majorName);
			List<Map<String, Object>> unitList = exameeInfoService.findOrgUnitByConditionWithJDBC(condition);
			if(null != unitList && unitList.size()>0){
				for(Map<String, Object> unit : unitList){
					unitOption.append("<option value='"+unit.get("resourceid")+"'");				
					unitOption.append(">"+unit.get("unitcode")+"-"+unit.get("unitname")+"</option>");
				}
			}
		}
		model.addAttribute("unitOption", unitOption);
		return "/edu3/recruit/exameeinfo/updateunit";
	}	
	/**
	 * 分配教学站
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/updateunit.html")
	public void updateUnit(String ids, String brSchoolid, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		Map<String, Object> map = new HashMap<String, Object>();
		StringBuffer message = new StringBuffer("");
		int statusCode = 200;
		try {
			String[] resourceids = ids.split(",");
			User currentUser = SpringSecurityHelper.getCurrentUser();
			// 导入报名信息唯一标示:0:准考证号，1:考生号
			SysConfiguration uniqueConfiguration = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId");
			if(uniqueConfiguration != null){
				String condition = "examCertificateNo=?";
				String uniqueTag = uniqueConfiguration.getParamValue();
				if("1".equals(uniqueTag)){
					condition ="enrolleeCode=?";
				}
				String hql = "from "+EnrolleeInfo.class.getSimpleName()+" where " + condition +" and isDeleted=0 ";
				String hql1 = "from "+TempStudentFee.class.getSimpleName()+" where enrolleeInfo." + condition +" and isDeleted=0 ";
				String recruitMajorHql = "from "+RecruitMajor.class.getSimpleName()+" rm where rm.isDeleted=0 and rm.recruitPlan.resourceid=? and rm.brSchool.resourceid=? and rm.teachingType=? and rm.recruitMajorCode=? and rm.classic.classicCode=? ";
				// 要分配的教学点
				OrgUnit orgUnit = orgUnitService.get(brSchoolid);
				
				// 通联支付
				List<Map<String,Object>> studentInfoList = new ArrayList<Map<String,Object>>();
				StringBuffer msg = new StringBuffer();
				int totalSize = 0;
				int faileSize = 0;// 失败数量
				Date today = new Date();
				String dateTime =ExDateUtils.formatDateStr(today, ExDateUtils.PATTREN_DATE_TIME_COMBINE);
				// 接收异步通知接口
				String receiveUrl = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/tempStudentFee/editInfo.html";
				HeadVO headVO = new HeadVO(Constants.EDU_STU_INFO_MODIFY,dateTime,receiveUrl);
				List<TempStudentFee> tsfUpdateList = new ArrayList<TempStudentFee>(100);
				for (String id : resourceids) {
					ExameeInfo exameeInfo = exameeInfoService.get(id);
					RecruitMajor recruitMajor = null;
					try {
						recruitMajor = recruitMajorService.findUnique(recruitMajorHql, exameeInfo.getRecruitPlan().getResourceid(),brSchoolid,exameeInfo.getXXXSDM(),exameeInfo.getLQZY(),exameeInfo.getCCDM());
					} catch (Exception e) {
						String _emsg = "<font  color='red'>"+orgUnit.getUnitName()+" 教学点，录取编码为"+exameeInfo.getLQZY()+"有两个相同的招生专业</font>";
						message.append(_emsg+"<br/>");
						faileSize++;
						logger.error(_emsg, e);
						continue;
					}
					String param = exameeInfo.getZKZH();
					if("1".equals(uniqueTag)){
						param = exameeInfo.getKSH();
					}
					EnrolleeInfo enrolleeInfo = enrolleeInfoService.findUnique(hql, param);
					
					if (enrolleeInfo != null) {
						//判断是否已经注册学籍信息
						if("Y".equals(enrolleeInfo.getRegistorFlag())){
							if(enrolleeInfo.getBranchSchool().getResourceid().equals(orgUnit.getResourceid())){
								continue;
							}
							statusCode = 300;
							message.append("<font color='red'>"+enrolleeInfo.getStudentBaseInfo().getName()+"</font>已经注册学籍，请做转点学籍异动！<br/>");
							faileSize++;
							continue;
						}
						OrgUnit _orgUnit = enrolleeInfo.getBranchSchool();
						
						// 是否使用本平台缴费
						String usePlatformPayFees = CacheAppManager.getSysConfigurationByCode("usePlatformPayFees").getParamValue();
						if("Y".equals(usePlatformPayFees)){
							//找出学生缴费表
							List<TempStudentFee> tsfList = tempStudentFeeService.findByHql(hql1, param);
							if(ExCollectionUtils.isNotEmpty(tsfList)){//如果有缴费记录
								//同步更新学生缴费信息表
								for(TempStudentFee tsf : tsfList) {
									tsf.setUnit(orgUnit);
									if(recruitMajor!=null){
										tsf.setMajor(recruitMajor.getMajor());
									}
									tsfUpdateList.add(tsf);
								}
								
								// 是否使用通联支付
								String useTongLianPay = CacheAppManager.getSysConfigurationByCode("useTongLianPay").getParamValue();
								if("Y".equals(useTongLianPay)){
									//同步第三方缴费系统
									try{
										Map<String,Object> studentInfo = new HashMap<String, Object>();
										studentInfo.put("STUDENT_ID", param);	
										studentInfo.put("MAJOR", recruitMajor.getMajor().getMajorName());
										studentInfo.put("COLLEGE", orgUnit.getUnitName());
										studentInfoList.add(studentInfo);
										if(studentInfoList.size()%50==0){
											String _msg = null;
											Document returnDoc =TonlyPayUtil.modifyStudentInfo(headVO, studentInfoList);
											msg = getReturnInfo(returnDoc, msg,faileSize);
											_msg = msg.toString();
											int sizeIndex = _msg.lastIndexOf("-");
											faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
											msg.setLength(0);
											totalSize+=50;
											studentInfoList.clear();
										}
									}catch(Exception e){
										message.append("<font  color='red'>"+enrolleeInfo.getStudentBaseInfo().getName()+" 分配教学点时,同步第三方失败！</font><br/>");
										faileSize++;
										logger.error("分配教学点时,同步第三方失败" + e);
										continue;
									}
								}
							}
						}
						if(recruitMajor!=null){//如果招生专业不为空
							enrolleeInfo.setRecruitMajor(recruitMajor);
							exameeInfo.setLQZYMC(recruitMajor.getMajor().getMajorName());
						}
						enrolleeInfo.setBranchSchool(orgUnit);
						enrolleeInfoService.update(enrolleeInfo);
						UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, enrolleeInfo);
						// 转出点或转入点为未分配，或转出点和转入点相同时不记录
						if(!("未分配".equals(_orgUnit.getUnitName()) 
								|| _orgUnit.getResourceid().equals(enrolleeInfo.getBranchSchool().getResourceid())
								|| "未分配".equals(enrolleeInfo.getBranchSchool().getUnitName()))){
							
							// 记录转点信息
							ExamineeChangeInfo examineeChangeInfo = new ExamineeChangeInfo();
							examineeChangeInfo.setRecruitPlan(exameeInfo.getRecruitPlan());
							examineeChangeInfo.setEnrolleeInfo(enrolleeInfo);
							examineeChangeInfo.setExamineeName(exameeInfo.getXM());
							examineeChangeInfo.setRolloutSchool(_orgUnit);
							examineeChangeInfo.setRollinSchool(enrolleeInfo.getBranchSchool());
							examineeChangeInfo.setMajor(enrolleeInfo.getRecruitMajor().getMajor());
							examineeChangeInfo.setClassic(enrolleeInfo.getRecruitMajor().getClassic());
							examineeChangeInfo.setOperator(currentUser);
							examineeChangeInfo.setOperatorName(currentUser.getCnName());// 中文名，不是用户名
							examineeChangeInfo.setOperateDate(new Date());
							examineeChangeInfoService.saveOrUpdate(examineeChangeInfo);
						}
					}
				}
				// 更新预缴费订单
				if(ExCollectionUtils.isNotEmpty(tsfUpdateList)){
					tempStudentFeeService.batchSaveOrUpdate(tsfUpdateList);
				}
				if(ExCollectionUtils.isNotEmpty(studentInfoList)){// 修改剩下的
					String _msg = null;
					Document returnDoc =TonlyPayUtil.modifyStudentInfo(headVO, studentInfoList);
					msg = getReturnInfo(returnDoc, msg,faileSize);
					_msg = msg.toString();
					int sizeIndex = _msg.lastIndexOf("-");
					faileSize = Integer.valueOf(_msg.substring(sizeIndex+1));
					totalSize+=studentInfoList.size();
					studentInfoList.clear();
				}
				logger.info("申请成功："+(resourceids.length-faileSize)+" 条，申请失败："+faileSize+" 条");
				if(message.length()==0) {
					message.append("分配教学点成功！");
				}
				//rendResponseStr +=(errorMsg.length()==0?"分配教学点成功！":errorMsg.toString())+ "',forwardUrl:''};";
				
			} else {
				statusCode = 300;
				message.append("请添加导入报名信息唯一标示！");
			}
		} catch (Exception e) {
			statusCode = 300;
			message.append("分配教学点失败！");
			logger.error("分配教学点失败", e);
		}
		map.put("statusCode", statusCode);
		map.put("message", message.toString());
		renderJson(response, JsonUtils.mapToJson(map));
		
	}	
	
	
	/**
	 * 录取-学生分配教学站
	 * @param request
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/updateunit1.html")
	public void updateUnit1(String ids, String brSchoolid, String recruitPlanId, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws WebException{
		String[] resourceids = request.getParameter("ids").split(",");
		for (String id : resourceids) {
			ExameeInfo exameeInfo = exameeInfoService.get(id);
			OrgUnit orgUnit = orgUnitService.get(request.getParameter("brSchoolid"));
			String hql = "from "+EnrolleeInfo.class.getSimpleName()+" where examCertificateNo=? and recruitMajor.recruitPlan.resourceid=?";
			EnrolleeInfo enrolleeInfo = enrolleeInfoService.findUnique(hql, exameeInfo.getZKZH(), recruitPlanId);
			if (enrolleeInfo != null) {
				enrolleeInfo.setBranchSchool(orgUnit);
				enrolleeInfoService.update(enrolleeInfo);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, enrolleeInfo);
			}
		}
		
		renderJson(response,JsonUtils.objectToJson("Y"));
		
	}	
	
	
	/**
	 * 查看考生信息
	 * @param resourceid
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/framework/exameeinfo/view.html")
	public String viewRecruitExameeInfo(String resourceid, ModelMap model){
		if(ExStringUtils.isNotBlank(resourceid)){
			ExameeInfo exameeInfo = exameeInfoService.get(resourceid);
			model.addAttribute("exameeInfo", exameeInfo);
			model.addAttribute("exameeInfoScoreList", new ArrayList<ExameeInfoScore>(exameeInfo.getExameeInfoScores()));
			model.addAttribute("exameeInfoWishList", new ArrayList<ExameeInfoWish>(exameeInfo.getExameeInfoWishs()));
			model.addAttribute("rootUrl", CacheAppManager.getSysConfigurationByCode("web.uploadfile.rooturl").getParamValue());
		}
		return "/edu3/recruit/exameeinfo/exameeinfo-view";
	}

	/**
	 * 考生信息打印预览
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/printview.html")
	public String printviewExameeInfo(String studentId, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		model.addAttribute("studentId",ExStringUtils.trimToEmpty(studentId));
		return "/edu3/recruit/exameeinfo/exameeinfo-printview";
	}
	
	/**
	 * 打印注销考试信息
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/cancel/printview.html")
	public String printviewCancelExameeInfo(String studentId, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		//model.addAttribute("studentId",ExStringUtils.trimToEmpty(studentId));
		return "/edu3/recruit/exameeinfo/exameeinfo-cancel-printview";
	}
	
	
	/**
	 * 打印注销的考生信息
	 * @param studentId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/cancel/exameeinfo/print.html")
	public void printCancelExameeInfo(String studentId,HttpServletRequest request,HttpServletResponse response){		
		Map<String, Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint   = null;//输出的报表
		try {
			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					  File.separator+"recruit"+File.separator+"exameeInfoCancelPrint.jasper"),"utf-8");
			//param.put("basicPath",ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath")+"common"+File.separator+"students");
			
			//param.put("basicPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
			param.put("scutLogoPath", CacheAppManager.getSysConfigurationByCode("web.scutlogo.path").getParamValue());
			param.put("imageRootPath",CacheAppManager.getSysConfigurationByCode("web.uploadfile.rootpath").getParamValue()+"common"+File.separator+"students");
			param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
			List<ExameeInfoCancelPrintVo> exameeInfoCancelList = exameeInfoService.findExameeinfoCancel();				
			List<Map<String, Object>> examInfoCancelMapList = new ArrayList<Map<String,Object>>();
			for (ExameeInfoCancelPrintVo exameeInfocancel : exameeInfoCancelList) {					
				examInfoCancelMapList.add(convertToMapByExamInfoCancel(exameeInfocancel));	//转换为map
			}					
			JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(examInfoCancelMapList);	
			jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表		
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印报名信息出错:{}",e.fillInStackTrace());
		}
	}
	
	/**
	 * 考生注销信息转为map
	 * @param exameeInfo
	 * @return
	 */
	public Map<String, Object> convertToMapByExamInfoCancel(ExameeInfoCancelPrintVo exameeInfocancel){
		Map<String, Object> map = ExBeanUtils.convertBeanToMap(exameeInfocancel);
		
		map.put("numb", exameeInfocancel.getNumb()); // 序号
		map.put("zspc", exameeInfocancel.getZspc()); // 招生批次
		map.put("ksh", exameeInfocancel.getKsh()); // 考生号
		map.put("xm", exameeInfocancel.getXm()); // 姓名
		map.put("zjhm", exameeInfocancel.getZjhm()); // 证件号码
		map.put("lqzy", exameeInfocancel.getLqzy()); // 录取专业
		map.put("zkzh", exameeInfocancel.getZkzh()); // 准考证号
		map.put("xb", exameeInfocancel.getXb()); // 性别
		map.put("kszt", exameeInfocancel.getKszt()); // 考生状态
		map.put("csrq", exameeInfocancel.getCsrq()); // 出生日期
		map.put("jxd", exameeInfocancel.getJxd()); // 教学点
		map.put("zxh", exameeInfocancel.getZxh()); // 准学号
		map.put("lxdh", exameeInfocancel.getLxdh()); // 联系电话
		map.put("yy", exameeInfocancel.getYy()); // 退学/不报道原因
		map.put("bz", exameeInfocancel.getBz()); // 备注	
		return map;
	}
	
	
	/**
	 * 打印考生信息
	 * @param studentId
	 * @param request
	 * @param response
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/print.html")
	public void printExameeInfo(String studentId,HttpServletRequest request,HttpServletResponse response){		
		Map<String, Object> param = new HashMap<String, Object>();
		JasperPrint jasperPrint   = null;//输出的报表
		String [] ids = ExStringUtils.trimToEmpty(studentId).split("\\,");

		try {
			String schoolCode = CacheAppManager.getSysConfigurationByCode("graduateData.schoolCode").getParamValue();
			if(ArrayUtils.isNotEmpty(ids)){
				String jasperPath = "exameeInfoPrint.jasper";
				if ("12962".equals(schoolCode)) {//广外艺
					jasperPath = "exameeInfoPrint_wyys.jasper";
				}
				String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
						  File.separator+"recruit"+File.separator+jasperPath),"utf-8");
				param.put("basicPath",ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath")+"common"+File.separator+"students");
				String localProvince = CacheAppManager.getSysConfigurationByCode("print.msg.localprovince").getParamValue();
				String schoolname = CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue();
				param.put("localProvince", localProvince);
				param.put("schoolname", schoolname);
				param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
				List<ExameeInfo> exameeInfoList = exameeInfoService.findByCriteria(ExameeInfo.class, new Criterion[]{Restrictions.eq("isDeleted", 0),Restrictions.in("resourceid", Arrays.asList(ids))},Order.asc("KSH"));				
				List<Map<String, Object>> examInfoMapList = new ArrayList<Map<String,Object>>();
				for (ExameeInfo exameeInfo : exameeInfoList) {					
					examInfoMapList.add(convertToMap(exameeInfo));	//转换为map
				}					
				JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(examInfoMapList);	
				jasperPrint = JasperFillManager.fillReport(reprotFile, param, dataSource); // 填充报表
			}			
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
			
		} catch (Exception e) {
			logger.error("打印报名信息出错:{}",e.fillInStackTrace());
		}
	}
	/**
	 * 考生信息转为map
	 * @param exameeInfo
	 * @return
	 */
	public Map<String, Object> convertToMap(ExameeInfo exameeInfo){
		Map<String, Object> map = ExBeanUtils.convertBeanToMap(exameeInfo);
		String[] dictCode = {"CodeForeignLanguage","CodeSex","CodeCharacteristic","CodeRecruitType","CodeNation","CodePolitics","CodeMajorAttribute","CodeEducationalLevel","CodeMajorCatogery","CodeTeachingType"};
		String[] dictField = {"WYYZDM","XBDM","KSTZBZ","ZSLBDM","MZDM","ZZMMDM","BKZYSXDM","WHCDDM","ZYLBDM","XXXSDM"};
		//字典值转为字典名称
		for (int i = 0; i < dictCode.length; i++) {
			map.put(dictField[i], JstlCustomFunction.dictionaryCode2Value(dictCode[i], map.get(dictField[i])!=null?map.get(dictField[i]).toString():""));
		}
		String[] codeField = {"KSH","ZKZH","SFZH","YZBM"};
		//号码加上空格间隔
		for (String code : codeField) {
			map.put(code, map.get(code)!=null?ExStringUtils.join(map.get(code).toString().split("")," "):"");
		}
		//考生成绩
		int i = 1;
		for (ExameeInfoScore score : exameeInfo.getExameeInfoScores()) {
			//没有办法，一个表格只能显示15条考生的成绩信息，而实际上导数据时常常把31条的数据导入，故而只能将所有0分的成绩通通过滤。
			//原先版本 
//			if(null == score.getScoreValue()){ 
//				continue;
//			}
//			map.put("CJXN"+i, "  "+JstlCustomFunction.dictionaryCode2Value("CodeExameeInfoScore", score.getScoreCode()));
//			map.put("CJX"+i++, score.getScoreValue());
			if(score != null){
				//改进后
				if(null != score.getScoreValue() && 0!=score.getScoreValue()){
					map.put("CJXN"+i, "  "+JstlCustomFunction.dictionaryCode2Value("CodeExameeInfoScore", score.getScoreCode()));
					map.put("CJX"+i++, score.getScoreValue());
				}
				
			}
		}
		//考生志愿
		i = 1;
		for (ExameeInfoWish wish : exameeInfo.getExameeInfoWishs()) {
			map.put("ZYDH"+i++, "  "+wish.getZYBM()+" "+wish.getZYMC());
		}	
		map.put("recruitPlanYear", exameeInfo.getRecruitPlan().getYearInfo().getFirstYear()+("2".equals(exameeInfo.getRecruitPlan().getTerm())?1L:0L));
		return map;
	}
	/**
	 * 注销入学资格
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/cancel.html")
	public void cancelExameeInfo(String resourceid, HttpServletRequest request,HttpServletResponse response ) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				exameeInfoService.cancelExameeInfo(resourceid.split("\\,"));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, "注销入学资格：resourceids:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "注销入学资格成功");	
			} 
		} catch (Exception e) {
			logger.error("注销入学资格出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "注销入学资格失败:<br/>"+e.getMessage());			
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 注销恢复，把注销的学生信息恢复成“未注册”
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/recovery.html")
	public void recoveryExameeInfo(String resourceid, HttpServletRequest request,HttpServletResponse response ) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				exameeInfoService.recoveryExameeInfo(resourceid.split("\\,"));
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.UPDATE, "注销恢复：resourceids:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", "恢复注销成功");	
			} 
		} catch (Exception e) {
			logger.error("注销入学资格出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "注销入学资格失败:<br/>"+e.getMessage());			
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 申请注销入学资格或者保留学籍
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/applycancel_keepStudent.html")
	public void applyCancelExameeInfo(String resourceid, HttpServletRequest request,HttpServletResponse response ) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String msg = "申请注销入学资格";
		if("keepStudent".equals(type)){
			msg = "申请保留学籍";
		}
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				exameeInfoService.auditCancelExameeInfo(resourceid.split("\\,"),"Apply",type);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS, "申请注销入学资格或者保留学籍：resourceids:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", msg+"成功");	
			} 
		} catch (Exception e) {
			logger.error("申请注销入学资格或者保留学籍出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", msg+"失败!");			
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 审核注销入学资格或保留学籍
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/auditcancel.html")
	public void auditCancelExameeInfo(String resourceid, HttpServletRequest request,HttpServletResponse response ) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String auditStatus = ExStringUtils.trimToEmpty(request.getParameter("auditStatus"));
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
		String msg = "审核注销入学资格";
		if("keepStudent".equals(type)){
			msg = "审核保留学籍";
		}
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				exameeInfoService.auditCancelExameeInfo(resourceid.split("\\,"),auditStatus,type);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS, "审核注销入学资格或者保留学籍：resourceids:"+resourceid);
				map.put("statusCode", 200);
				map.put("message", msg+"成功");	
			} 
		} catch (Exception e) {
			logger.error("审核注销入学资格或保留学籍出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", msg+"失败!");			
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 检查待申请注销的学生是否已经注册
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/applyAuditCancelExameeInfoCheck.html")
	public void applyAuditCancelExameeInfoCheck(String resourceid, HttpServletRequest request,HttpServletResponse response ) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> examCertificateNoList = new ArrayList<String>();
		String[] ids = resourceid.split("\\,");
		StringBuffer msg = new StringBuffer("考生号为");
		for (String id : ids) {
			ExameeInfo exameeInfo = exameeInfoService.get(id);							
			examCertificateNoList.add(exameeInfo.getZKZH());				
		}
		List<EnrolleeInfo> enrolleeInfoList = enrolleeInfoService.findByCriteria(Restrictions.eq("isDeleted", 0),Restrictions.in("examCertificateNo", examCertificateNoList));
		for (EnrolleeInfo enrolleeInfo : enrolleeInfoList) {
			if(Constants.BOOLEAN_YES.equals(enrolleeInfo.getRegistorFlag())){//已经注册，不能申请注销
				msg.append(""+enrolleeInfo.getEnrolleeCode()+"," );
			}
		}
		if(!"考生号为".equals(msg.toString())){
			map.put("unable", "1");
			map.put("msg", msg.append("的考生已经注册").toString().replaceAll("\\,的","的"));
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 审核注销入学资格状态选择
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/exameeinfo-select.html")
	public String auditCancelExameeInfoSelect(HttpServletRequest request,HttpServletResponse response,ModelMap model) throws WebException {
		String ids = ExStringUtils.trimToEmpty(request.getParameter("ids"));
		String type = ExStringUtils.trimToEmpty(request.getParameter("type"));
		model.addAttribute("ids", ids); 
		model.addAttribute("type", type); 
		return "/edu3/recruit/exameeinfo/exameeinfo-select";
	}
	/**
	 * 注销
	 * @param resourceid
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/withDraw.html")
	public void withDrawExameeInfo(String resourceid, HttpServletRequest request,HttpServletResponse response ) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {			
			if(ExStringUtils.isNotBlank(resourceid)){
				exameeInfoService.withDrawExameeInfo(resourceid.split("\\,"));
				map.put("statusCode", 200);
				map.put("message", "注销成功");	
			} 
		} catch (Exception e) {
			logger.error("注销出错:{}",e.fillInStackTrace());
			map.put("statusCode", 300);
			map.put("message", "注销失败:<br/>"+e.getMessage());			
		} 
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 生成录取编号
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/edu3/recruit/exameeinfo/generateEnrollNO.html")
	public void generateEnrollNO(HttpServletRequest request, HttpServletResponse response) throws WebException {
		Map<String, Object> map = new HashMap<String, Object>();
		String statusCode = "200";
		String message = "生成录取编号成功！";
		try {
			String recruitPlanId = request.getParameter("recruitPlanId");
			Map<String, Object> condition = new HashMap<String, Object>();
			condition.put("recruitPlan",recruitPlanId);
			condition.put("hasEnrollNO", Constants.BOOLEAN_NO);
			condition.put("orderBy", " order by  ei.recruitMajor.classic.showOrder,ei.matriculateNoticeNo ");
			condition.put("orderType", " ASC ");
			List<EnrolleeInfo> enrolleeInfoList =  enrolleeInfoService.findListByCondition(condition);
			if(ExCollectionUtils.isNotEmpty(enrolleeInfoList)){
				// 获取某年级当前最大的录取编号
				int maxEnrollNO = enrolleeInfoService.getMaxEnrollNO(recruitPlanId);
				for(EnrolleeInfo enrolleeInfo : enrolleeInfoList){
					// 将录取编号转换为四位的字符串
					String _maxEnrollNO = Toolkit.convertNumberToString(++maxEnrollNO, 4);
					enrolleeInfo.setEnrollNO(_maxEnrollNO);
				}
				enrolleeInfoService.batchSaveOrUpdate(enrolleeInfoList);
				UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.INSERT,enrolleeInfoList );
			}
		} catch (Exception e) {
			logger.error("生成录取编号出错", e);
			statusCode = "300";
			message = "生成录取编号失败！";
		} finally {
			map.put("statusCode", statusCode);
			map.put("msg", message);
		}
		
		renderJson(response, JsonUtils.mapToJson(map));
	}
	
	/**
	 * 导出学生信息
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/export-stu.html")
	public void exportListRecruitExameeInfo(HttpServletRequest request,HttpServletResponse response) throws WebException{		
		String headerName = "录取库";
		Map<String, Object> condition = new HashMap<String, Object>();
		String resIds = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		if (ExStringUtils.isNotBlank(resIds)) {
			condition.put("resIds", "'"+resIds.replaceAll(",", "','")+"'");
		} else {
			String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学校
			String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));//报名批次
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//考生姓名
			String enrolleeCode = ExStringUtils.trimToEmpty(request.getParameter("enrolleeCode"));//考生号
			String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//考生证件号码
			String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));//考生证件号码
			String kszt = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
			String isExistsPhoto = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//是否存在相片
			String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			String classic  = ExStringUtils.trimToEmpty(request.getParameter("classic"));//
			if(ExStringUtils.isNotBlank(classic)) {
				condition.put("classic", classic);
			}
			//2014-3-18 增加已录取未注册条件查询
			String registorFlag = ExStringUtils.trimToEmpty(request.getParameter("registorFlag"));
			if(ExStringUtils.isNotBlank(registorFlag)) {
				condition.put("registorFlag", registorFlag);
			}
			if(ExStringUtils.isNotBlank(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);
			}
			if(ExStringUtils.isNotBlank(name)) {
				condition.put("name", ExStringUtils.trimToEmpty(name));
			}
			if(ExStringUtils.isNotBlank(enrolleeCode)) {
				condition.put("enrolleeCode", ExStringUtils.trimToEmpty(enrolleeCode));
			}
			if(ExStringUtils.isNotBlank(certNum)) {
				condition.put("certNum", ExStringUtils.trimToEmpty(certNum));
			}
			if(ExStringUtils.isNotBlank(examCertificateNo)) {
				condition.put("examCertificateNo", ExStringUtils.trimToEmpty(examCertificateNo));
			}
			if(ExStringUtils.isNotBlank(kszt)){ 
				condition.put("kszt", kszt);
				String ksztName = JstlCustomFunction.dictionaryCode2Value("CodeEnrollStatus", kszt);
				headerName = headerName + ksztName;
			}
			if(ExStringUtils.isNotBlank(isExistsPhoto)) {
				condition.put("isExistsPhoto", isExistsPhoto);
			}
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool = user.getOrgUnit().getResourceid();
			}
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
		}
		
		try{
			List<Map<String,Object>> exameeInfoList = exameeInfoService.findExameeInfoByConditionWithJDBC(condition);
			if(exameeInfoList.size()>8000){
				renderHtml(response, "<script>alert('导出的数据量"+exameeInfoList.size()+"太大,请控制在8000以内')</script>");
			}
			String flag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			List<EnrolleeInfoExportVO> list = covertToVo(exameeInfoList,flag);
			
			//增加最后一个end_point
			EnrolleeInfoExportVO end = new EnrolleeInfoExportVO();
			end.setEnrolleeNo("end_point");
			list.add(end);
			
			headerName = headerName+"学生信息列表";
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
	 	    GUIDUtils.init();
	 	    //导出
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			
			Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
			
			//模板文件路径
			String templateFilepathString = "";
			
			String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
			
			templateMap.put("date", date);
			templateMap.put("school", CacheAppManager.getSysConfigurationByCode("tlschoolName").getParamValue());//暂时学校,学校代码,商业名称为空,都用全局参数
			templateMap.put("schoolCode",CacheAppManager.getSysConfigurationByCode("tlschoolNo").getParamValue());
			templateMap.put("businessName", CacheAppManager.getSysConfigurationByCode("tlmerchantName").getParamValue());
			templateMap.put("total", String.valueOf(list.size()-1));
			
			templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"
			   					   + File.separator+"excel"+File.separator+"exameeinfo_exprot_stu.xls";
			//初始化配置参数
			exportExcelService.initParmasByfile(disFile, "exameeinfo_exprot_stu", list,condition);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 4, templateMap);
			
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			
			String downloadFileName = "录取学生信息.xls";
			String downloadFilePath = excelFile.getAbsolutePath();
						
			downloadFile(response, downloadFileName,downloadFilePath,true);	
	 	 
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		 }
	}
	
	/*
	 * 将查询的学生信息转换成vo
	 * flag是判断是否要准考证号还是考生号当做学号->exameeInfo.uniqueId
	 */
	private List<EnrolleeInfoExportVO> covertToVo(List<Map<String, Object>> exameeInfoList,String flag) {
		
		List<EnrolleeInfoExportVO> returnList = new ArrayList<EnrolleeInfoExportVO>();
		
		if("1".equals(flag)){//1
			for(Map<String, Object> exameeInfo:exameeInfoList){
				EnrolleeInfoExportVO tempVo = new EnrolleeInfoExportVO();
				tempVo.setEnrolleeNo((String) exameeInfo.get("KSH"));
				tempVo.setEnrolleeBrSchool((String) exameeInfo.get("UNITNAME"));
				tempVo.setEnrolleeClass("");
				tempVo.setEnrolleeMajor((String) exameeInfo.get("LQZYMC"));
				tempVo.setEnrolleeName((String) exameeInfo.get("XM"));
				tempVo.setCertNum(((String) exameeInfo.get("SFZH")).substring(((String) exameeInfo.get("SFZH")).length()-6));			
				returnList.add(tempVo);
			}
		}else{//0
			for(Map<String, Object> exameeInfo:exameeInfoList){
				EnrolleeInfoExportVO tempVo = new EnrolleeInfoExportVO();
				tempVo.setEnrolleeNo((String) exameeInfo.get("ZKZH"));
				tempVo.setEnrolleeBrSchool((String) exameeInfo.get("UNITNAME"));
				tempVo.setEnrolleeClass("");
				tempVo.setEnrolleeMajor((String) exameeInfo.get("LQZYMC"));
				tempVo.setEnrolleeName((String) exameeInfo.get("XM"));
				tempVo.setCertNum(((String) exameeInfo.get("SFZH")).substring(((String) exameeInfo.get("SFZH")).length()-6));			
				returnList.add(tempVo);
			}
		}
		List<Map<String,Object>> vos = new ArrayList<Map<String,Object>>();
		
		return returnList;
	}
	
	///edu3/recruit/exameeinfo/print-code.html
	/**
	 * 打印条形码
	 * @param request
	 * @param objPage
	 * @param model
	 * @return
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/print-code.html")
	public void PrintListRecruitExameeInfoCode(HttpServletRequest request,HttpServletResponse response) throws WebException{		
		String headerName = "录取库";
		Map<String, Object> condition = new HashMap<String, Object>();
		String resIds = ExStringUtils.trimToEmpty(request.getParameter("resIds"));
		if (ExStringUtils.isNotBlank(resIds)) {
			condition.put("resIds", "'"+resIds.replaceAll(",", "','")+"'");
		} else {
			String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学校
			String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));//报名批次
			String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//考生姓名
			String enrolleeCode = ExStringUtils.trimToEmpty(request.getParameter("enrolleeCode"));//考生号
			String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//考生证件号码
			String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));//考生证件号码
			String kszt = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
			String isExistsPhoto = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//是否存在相片
			String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			String classic  = ExStringUtils.trimToEmpty(request.getParameter("classic"));//
			if(ExStringUtils.isNotBlank(classic)) {
				condition.put("classic", classic);
			}
			//2014-3-18 增加已录取未注册条件查询
			String registorFlag = ExStringUtils.trimToEmpty(request.getParameter("registorFlag"));
			if(ExStringUtils.isNotBlank(registorFlag)) {
				condition.put("registorFlag", registorFlag);
			}
			if(ExStringUtils.isNotBlank(recruitPlanId)){
				condition.put("recruitPlanId", recruitPlanId);
			}
			if(ExStringUtils.isNotBlank(name)) {
				condition.put("name", ExStringUtils.trimToEmpty(name));
			}
			if(ExStringUtils.isNotBlank(enrolleeCode)) {
				condition.put("enrolleeCode", ExStringUtils.trimToEmpty(enrolleeCode));
			}
			if(ExStringUtils.isNotBlank(certNum)) {
				condition.put("certNum", ExStringUtils.trimToEmpty(certNum));
			}
			if(ExStringUtils.isNotBlank(examCertificateNo)) {
				condition.put("examCertificateNo", ExStringUtils.trimToEmpty(examCertificateNo));
			}
			if(ExStringUtils.isNotBlank(kszt)){ 
				condition.put("kszt", kszt);
				String ksztName = JstlCustomFunction.dictionaryCode2Value("CodeEnrollStatus", kszt);
				headerName = headerName + ksztName;
			}
			if(ExStringUtils.isNotBlank(isExistsPhoto)) {
				condition.put("isExistsPhoto", isExistsPhoto);
			}
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool = user.getOrgUnit().getResourceid();
			}
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
		}
		
		List<EnrolleeInfoExportVO> list = null;
		List<Map<String,Object>>  maps = null;
		try{
			List<Map<String,Object>> exameeInfoList = exameeInfoService.findExameeInfoByConditionWithJDBC(condition);

			String flag = CacheAppManager.getSysConfigurationByCode("exameeInfo.uniqueId").getParamValue();
			
			list = covertToVo(exameeInfoList,flag);
			//增加条形码
			addBarcode(list);
			
			Map<String,Object> param = new HashMap<String, Object>();
			JasperPrint jasperPrint = null;

			String reprotFile = URLDecoder.decode(request.getSession().getServletContext().getRealPath(File.separator+"reports"+
					  File.separator+"recruit"+File.separator+"exameeInfoPrintBarcode.jasper"),"utf-8");
			param.put("basicPath",ConfigPropertyUtil.getInstance().getProperty("web.uploadfile.rootpath")+"common"+File.separator+"students");

			//param.put("school", CacheAppManager.getSysConfigurationByCode("graduateData.schoolName").getParamValue());//学校名称
			maps = new ArrayList<Map<String,Object>>();
			for(EnrolleeInfoExportVO vo : list){
				maps.add(convertToMapByEnrolleeInfoExportVO(vo));
			}
			//((ByteArrayInputStream)(maps.get(0).get("barcode"))).read()
			JRMapCollectionDataSource dataSource = new JRMapCollectionDataSource(maps);	
			jasperPrint = JasperFillManager.fillReport(reprotFile, param,dataSource); // 填充报表
				
				
			if (null!=jasperPrint) {
				renderStream(response, jasperPrint);
			}else {
				renderHtml(response,"缺少打印数据！");
			}
		
		 }catch(Exception e){
			logger.error("打印学生信息条形码出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('打印学生信息条形码出错')</script>");
		 }finally{//关闭条形码的流
			 
				 for(Map<String,Object> map: maps){
					 try {
						 if(map.get("barcode")!=null){
							 ((InputStream)(map.get("barcode"))).close();
						 }
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 
			 
		 }
	}
	
	private void addBarcode(List<EnrolleeInfoExportVO> list) {
		for(EnrolleeInfoExportVO vo :list){
			vo.setBarcode(BarcodeUtil.encode(vo.getEnrolleeNo(),200,36));
		}
	}
	
	/**
	 * 考生注销信息转为map
	 * @param exameeInfo
	 * @return
	 */
	public Map<String, Object> convertToMapByEnrolleeInfoExportVO(EnrolleeInfoExportVO enrolleeInfoExportVO){
		Map<String, Object> map = ExBeanUtils.convertBeanToMap(enrolleeInfoExportVO);
		
		map.put("enrolleeNo", enrolleeInfoExportVO.getEnrolleeNo()); // 学号
		map.put("enrolleeName", enrolleeInfoExportVO.getEnrolleeName()); // 姓名
		/*map.put("", enrolleeInfoExportVO.getEnrolleeClass()); // 班级
		map.put("", enrolleeInfoExportVO.getEnrolleeMajor()); // 专业
		map.put("", enrolleeInfoExportVO.getEnrolleeBrSchool()); // 学校
		map.put("", enrolleeInfoExportVO.getCertNum()); // 证件号码验证
		*/		
		map.put("barcode", enrolleeInfoExportVO.getBarcode()); // 条形码流
		
		return map;
	}
	
	/**
	 * 条形码打印预览
	 * @param resourceid
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/barcodePrintview.html")
	public String printviewBarcode(String resIds, HttpServletRequest request,HttpServletResponse response,ModelMap model){
		String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学校
		String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));//报名批次
		String name = ExStringUtils.trimToEmpty(request.getParameter("name"));//考生姓名
		String enrolleeCode = ExStringUtils.trimToEmpty(request.getParameter("enrolleeCode"));//考生号
		String certNum = ExStringUtils.trimToEmpty(request.getParameter("certNum"));//考生证件号码
		String examCertificateNo = ExStringUtils.trimToEmpty(request.getParameter("examCertificateNo"));//考生证件号码
		String kszt = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
		String isExistsPhoto = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//是否存在相片
		String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
		String classic  = ExStringUtils.trimToEmpty(request.getParameter("classic"));//
		//2014-3-18 增加已录取未注册条件查询
		String registorFlag = ExStringUtils.trimToEmpty(request.getParameter("registorFlag"));
		//条件
		model.addAttribute("branchSchool",ExStringUtils.trimToEmpty(branchSchool));
		model.addAttribute("recruitPlanId",ExStringUtils.trimToEmpty(recruitPlanId));
		model.addAttribute("name",ExStringUtils.trimToEmpty(name));
		model.addAttribute("enrolleeCode",ExStringUtils.trimToEmpty(enrolleeCode));
		model.addAttribute("certNum",ExStringUtils.trimToEmpty(certNum));
		model.addAttribute("examCertificateNo",ExStringUtils.trimToEmpty(examCertificateNo));
		model.addAttribute("kszt",ExStringUtils.trimToEmpty(kszt));
		model.addAttribute("isExistsPhoto",ExStringUtils.trimToEmpty(isExistsPhoto));
		model.addAttribute("major",ExStringUtils.trimToEmpty(major));
		model.addAttribute("classic",ExStringUtils.trimToEmpty(classic));
		model.addAttribute("registorFlag",ExStringUtils.trimToEmpty(registorFlag));

		//勾选
		model.addAttribute("resIds",ExStringUtils.trimToEmpty(resIds));
		return "/edu3/recruit/exameeinfo/exameeinfo-barcode-printview";
	}
	
	/**
	 * 导出注册统计
	 * @param request
	 * @param response
	 * @throws WebException
	 */
	@RequestMapping("/edu3/recruit/exameeinfo/enrollStatistical.html")
	public void expEnrollStatistical(HttpServletRequest request,HttpServletResponse response) throws WebException{		
		
		Map<String, Object> condition = new HashMap<String, Object>();
		String resIds = ExStringUtils.trimToEmpty(ExStringUtils.getEncodeURIComponentByTwice(request.getParameter("resIds")));
		String flag  = ExStringUtils.trimToEmpty(request.getParameter("flag"));
		if (ExStringUtils.isNotBlank(resIds)) {
			condition.put("resIds", "'"+resIds.replaceAll(",", "','")+"'");
		} else {
			String branchSchool  = ExStringUtils.trimToEmpty(request.getParameter("branchSchool"));//学校
			String recruitPlanId = ExStringUtils.trimToEmpty(request.getParameter("recruitPlanId"));//报名批次
			String kszt = ExStringUtils.trimToEmpty(request.getParameter("kszt"));//考生状态
			//String isExistsPhoto = ExStringUtils.trimToEmpty(request.getParameter("isExistsPhoto"));//是否存在相片
			String major  = ExStringUtils.trimToEmpty(request.getParameter("major"));//
			String classic  = ExStringUtils.trimToEmpty(request.getParameter("classic"));
			if(ExStringUtils.isNotBlank(major)) {
				condition.put("major", major);
			}
			if(ExStringUtils.isNotBlank(classic)) {
				condition.put("classic", classic);
			}
			if(ExStringUtils.isNotBlank(recruitPlanId)) {
				condition.put("recruitPlanId", recruitPlanId);
			}
			
			User user = SpringSecurityHelper.getCurrentUser();
			if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
				branchSchool = user.getOrgUnit().getResourceid();
				/*List<Role> roles = new ArrayList<Role>(user.getRoles());
				for (int i = 0; i < roles.size(); i++) {
					Role role = roles.get(i);
					if(role.getRoleName().equals("学习中心教务员")){
						roleFlag="jwy";
					}else if(roleFlag.isEmpty() && role.getRoleName().equals("班主任角色")){
						roleFlag="bzr";
					}
				}*/
			}
			if(ExStringUtils.isNotEmpty(branchSchool)){
				condition.put("branchSchool", branchSchool);
			}
		}
		
		/*if(ExStringUtils.isNotBlank(kszt)){ 
			condition.put("kszt", kszt);
			String ksztName = JstlCustomFunction.dictionaryCode2Value("CodeEnrollStatus", kszt);
			headerName = headerName + ksztName;
		}*/
		//if(ExStringUtils.isNotBlank(isExistsPhoto)) condition.put("isExistsPhoto", isExistsPhoto);
		try{
			File excelFile = null;
			File disFile   = new File(getDistfilepath()+ File.separator + GUIDUtils.buildMd5GUID(false) + ".xls");
			List<Map<String,Object>> enrollStatisticalInfoList = new ArrayList<Map<String,Object>>();
			//字典转换列
			List<String> dictCodeList = new ArrayList<String>();
			dictCodeList.add("CodeSex");
			dictCodeList.add("yesOrNo_default");
			dictCodeList.add("CodeTeachingType");
			setDistfilepath(Constants.EDU3_DATAS_LOCALROOTPATH+"exportfiles");
		 	GUIDUtils.init();
			
			/*if(roleFlag.equals("bzr")){//班主任角色导出
				condition.put("masterid", user.getResourceid());
				List<Major> majorlList = majorService.findMjorByMaster(user.getResourceid());
				for (int i = 0; i < majorlList.size(); i++) {
					Major majorTemp = majorlList.get(i);
					condition.put("majorid", majorTemp.getResourceid());
					enrollStatisticalInfoList = exameeInfoService.findEnrollStatisticalInfo(condition);
					Map<String,Object> templateMap = new HashMap<String, Object>();//设置模板
					templateMap.put("brSchool", "");	
					//templateMap.put("teachingType", JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", classes.getTeachingType()));	
					templateMap.put("majorName", "");	
					templateMap.put("classicName", "");	
					String templateFilepathString = SystemContextHolder.getAppRootPath()+"WEB-INF"+File.separator+"templates"+File.separator+"excel"+File.separator+"enrollStatistical.xls";
					//exportExcelService.initParamsByfile(disFile, "enrollStatisticalForMaster", exameeInfoList, dictionaryService.getDictionByMap(dictCodeList,true,IDictionaryService.PREKEY_TYPE_BYCODE), null, null);		
					exportExcelService.getModelToExcel().setTemplateParam(templateFilepathString, 3, templateMap);
				}
			}else {*/
		 	String headerName = "";
			if("info".equals(flag)){//注册信息列表
				headerName="新生报到情况总表";
				enrollStatisticalInfoList = exameeInfoService.findEnrollStatisticalInfo(condition);
				exportExcelService.initParmasByfile(disFile, "expEnrollStatistical1", enrollStatisticalInfoList,dictionaryService.getDictionByMap(dictCodeList,true,IDictionaryService.PREKEY_TYPE_BYCODE),null);
				exportExcelService.getModelToExcel().setSheet(1, "报到情况表");
			}else if("result".equals(flag)){//注册信息统计
				headerName="新生报到统计表";
				List<Map<String, Object>> enrollStatisticalMajor = exameeInfoService.findEnrollStatisticalResult(condition);
				condition.put("forResult", 'Y');
				List<Map<String, Object>> enrollStatisticalResult = exameeInfoService.findEnrollStatisticalResult(condition);
				List<EnrollStatisticalVo> enrollStatisticalVos = getEnrollStatisticalList(enrollStatisticalResult,enrollStatisticalMajor);
				exportExcelService.initParmasByfile(disFile, "expEnrollStatistical2", enrollStatisticalVos,null,null);
				exportExcelService.getModelToExcel().setSheet(2, "统计表");
			}else if ("case".equals(flag)) {//注册情况
				headerName="新生报到注册情况";
				enrollStatisticalInfoList = exameeInfoService.findEnrollStatisticalInfo(condition);
				exportExcelService.initParmasByfile(disFile, "expEnrollStatistical3", enrollStatisticalInfoList,dictionaryService.getDictionByMap(dictCodeList,true,IDictionaryService.PREKEY_TYPE_BYCODE),null);
				exportExcelService.getModelToExcel().setSheet(3, "注册情况表");
			}
				
			exportExcelService.getModelToExcel().setHeader(headerName);
			exportExcelService.getModelToExcel().setRowHeight(300);//设置行高
			excelFile = exportExcelService.getExcelFile();//获取导出的文件
			logger.info("获取导出的excel文件:{}",excelFile.getAbsoluteFile());
			String downloadFileName = headerName + ".xls";
			String downloadFilePath = excelFile.getAbsolutePath();
			downloadFile(response, downloadFileName,downloadFilePath,true);	
	 	 
		 }catch(Exception e){
			logger.error("导出excel文件出错："+e.fillInStackTrace());
	 		renderHtml(response, "<script>alert('导出excel文件出错:"+e.getMessage()+"')</script>");
		 }
	}
	private List<EnrollStatisticalVo> getEnrollStatisticalList(List<Map<String, Object>> statisticalResult, List<Map<String, Object>> majorList) {
		List<EnrollStatisticalVo> enrollStatisticalVos = new ArrayList<EnrollStatisticalVo>();
		String classicname = "classicname";
		int resultIndex = 0;
		for (int index =0;index<majorList.size();index++) {
			Map<String, Object> majorInfo = majorList.get(index);
			if(!"classicname".equals(classicname) && !majorInfo.get("classicname").equals(classicname)){//添加合计
				Map<String, Object> resultInfo = statisticalResult.get(resultIndex);
				EnrollStatisticalVo temp = new EnrollStatisticalVo();
				temp.setMajorName(resultInfo.get("classicname")==null?"":resultInfo.get("classicname").toString());
				temp.setTotal(resultInfo.get("total")==null?0:Integer.parseInt(resultInfo.get("total").toString()));
				temp.setRegNum(resultInfo.get("reg")==null?0:Integer.parseInt(resultInfo.get("reg").toString()));
				temp.setNotRegNum(resultInfo.get("notreg")==null?0:Integer.parseInt(resultInfo.get("notreg").toString()));
				temp.setProportion(resultInfo.get("proportion")==null?"":resultInfo.get("proportion").toString());
				resultIndex++;
				enrollStatisticalVos.add(temp);
			}
			EnrollStatisticalVo vo = new EnrollStatisticalVo();
			vo.setRowNum(String.valueOf(index+1));
			//vo.setUnitName(majorInfo.get("unitname")==null?"":majorInfo.get("unitname").toString());
			vo.setMajorName(majorInfo.get("majorname")==null?"":majorInfo.get("majorname").toString());
			vo.setTeachingtype(majorInfo.get("teachingtype")==null?"":JstlCustomFunction.dictionaryCode2Value("CodeTeachingType", majorInfo.get("teachingtype").toString()));
			vo.setClassicName(majorInfo.get("classicname")==null?"":majorInfo.get("classicname").toString());
			vo.setTotal(majorInfo.get("total")==null?0:Integer.parseInt(majorInfo.get("total").toString()));
			vo.setRegNum(majorInfo.get("reg")==null?0:Integer.parseInt(majorInfo.get("reg").toString()));
			vo.setNotRegNum(majorInfo.get("notreg")==null?0:Integer.parseInt(majorInfo.get("notreg").toString()));
			vo.setProportion(majorInfo.get("proportion")==null?"":majorInfo.get("proportion").toString());
			classicname = majorInfo.get("classicname")==null?classicname:majorInfo.get("classicname").toString();
			enrollStatisticalVos.add(vo);
		}
		for(;resultIndex<statisticalResult.size();resultIndex++){
			EnrollStatisticalVo vo = new EnrollStatisticalVo();
			Map<String, Object> resultInfo = statisticalResult.get(resultIndex);
			vo.setMajorName (resultInfo.get("classicname")==null?"":resultInfo.get("classicname").toString());
			vo.setTotal(resultInfo.get("total")==null?0:Integer.parseInt(resultInfo.get("total").toString()));
			vo.setRegNum(resultInfo.get("reg")==null?0:Integer.parseInt(resultInfo.get("reg").toString()));
			vo.setNotRegNum(resultInfo.get("notreg")==null?0:Integer.parseInt(resultInfo.get("notreg").toString()));
			vo.setProportion(resultInfo.get("proportion")==null?"":resultInfo.get("proportion").toString());
			enrollStatisticalVos.add(vo);
		}
		return enrollStatisticalVos;
	}	
	
	/*
	 * 第三方接口的返回信息
	 */
	private StringBuffer getReturnInfo(Document returnDoc, StringBuffer msg, int faileSize){
		Element root = returnDoc.getRootElement();
		Element body = XMLUtils.getChild(root, "BODY");
//		String fileNo =XMLUtils.getChild(body, "FILE_NO").getText();//TODO:以后添加 处理批次号
		List<Element> dataList = XMLUtils.getChildElements(body, "DATALIST");
		if(ExCollectionUtils.isNotEmpty(dataList)){
			String receiptCode = null;
			String studentId = "";
			String receiptMsg = null;
			for(Element data : dataList){
//				System.out.println("-------------"+data.asXML());
				receiptCode = XMLUtils.getChild(data, "RETURN_CODE").getText();
				if(!Constants.RETURN_STATUS_SUCESS.equals(receiptCode)){// 失败
					faileSize = faileSize+1;
					if(XMLUtils.getChild(data, "BUSINESS_ID")!=null){
						studentId =XMLUtils.getChild(data, "BUSINESS_ID").getText()+" : ";
					}
					receiptMsg = XMLUtils.getChild(data, "RETURN_MSG").getText();
					msg.append(studentId+receiptMsg+"<br>");
				}
			}
		}
		msg.append("-"+faileSize);
		return msg;
	}
	/**
	 * 发送短信
	 */
	@RequestMapping("/edu/recruit/exameeinfo/sendNote.html")
	public void sendNoteByRes(HttpServletRequest request,HttpServletResponse response) throws  Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode=200;
		String message="发送成功";
		String resourceids = request.getParameter("resId");
		String moblies = "";
		try {
			if(ExStringUtils.isNotBlank(resourceids)){//勾选
				moblies = exameeInfoService.getMoblie(resourceids);
				
			}else{//按查询条件
				String recruitPlanId = request.getParameter("recruitPlanId");
				String unitId = request.getParameter("unitId");
				String classicId = request.getParameter("classicId");
				String majorId = request.getParameter("majorId");
				Map<String ,Object> condition = new HashMap<String, Object>();
				if(ExStringUtils.isNotBlank(recruitPlanId)){
					condition.put("recruitPlanId", recruitPlanId);
				}
				if(ExStringUtils.isNotBlank(unitId)){
					condition.put("branchSchool", unitId);
				}
				if(ExStringUtils.isNotBlank(classicId)){
					condition.put("classic", classicId);
				}
				if(ExStringUtils.isNotBlank(majorId)){
					condition.put("major", majorId);
				}
				moblies=exameeInfoService.getMoblieBySelect(condition);
			}
			if(ExStringUtils.isNotBlank(moblies)){
				Map<String,Object> receiveMap=httpSender(moblies);
				String result = receiveMap.get("result").toString();
				if(!"0".equals(result)){
					statusCode=300;
					message="短信提交到短信服务器时，返回出错信息！响应代码："+result+":"+NoteCode.getText(Integer.valueOf(result));
					logger.error("短信提交返回错误代码："+JsonUtils.mapToJson(receiveMap));
				}else{//暂不用处理
											
				}
			}
		} catch (Exception e) {
			statusCode=300;
			e.printStackTrace();
		} finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 
	 */
	@RequestMapping("/edu/recruit/exameeinfo/selectCount.html")
	public void sendNoteSelectCount(HttpServletRequest request,HttpServletResponse response) throws  Exception{
		Map<String,Object> map = new HashMap<String, Object>();
		int statusCode=200;
		String message="";
		String recruitPlanId = request.getParameter("recruitPlanId");
		String unitId = request.getParameter("unitId");
		String classicId = request.getParameter("classicId");
		String majorId = request.getParameter("majorId");
		Map<String ,Object> condition = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(recruitPlanId)){
			condition.put("recruitPlanId", recruitPlanId);
		}
		if(ExStringUtils.isNotBlank(unitId)){
			condition.put("branchSchool", unitId);
		}
		if(ExStringUtils.isNotBlank(classicId)){
			condition.put("classic", classicId);
		}
		if(ExStringUtils.isNotBlank(majorId)){
			condition.put("major", majorId);
		}
		try {
			int count = exameeInfoService.getSelectCount(condition);
			map.put("selectCount", count);			
		} catch (Exception e) {
			statusCode=300;
			message="查询发送人数发生错误！";
			e.printStackTrace();
		} finally{
			map.put("statusCode", statusCode);
			map.put("message", message);
		}
		renderJson(response, JsonUtils.mapToJson(map));
	}
	/**
	 * 第三方提供的连接短信发送的接口方法
	 */
	private Map<String,Object> httpSender(String mobiles){
//		String uri = "http://114.55.176.84/msg/HttpBatchSendSM";//应用地址
//		String account = "ms-gdyida";//账号
//		String pswd = "MSgdyida1115";//密码
//		String content = "各位考生：恭喜你被广东医科大学成人高等教育录取。为使你进一步了解学校，尽快融入到大学的学习当中，请尽快关注“广东医科大学继续教育学院”微信公众号，及时掌握校院信息及动态。如有问题，请致电（0769-22896307、0759-2388460）咨询。";//短信内容
//		mobiles = "15920383174";//手机号码，多个号码使用","分割
		ConfigPropertyUtil property = ConfigPropertyUtil.getInstance();
		String uri = property.getProperty("note.uri");
		String account = property.getProperty("note.account");
		String pswd = property.getProperty("note.pswd");
		String content = property.getProperty("note.content");
		boolean needstatus = true;//是否需要状态报告，需要true，不需要false
		String product = "";//产品ID
		String extno = "";//扩展码
		String respType = "json";//
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String returnsJson = HttpSender.send(uri, account, pswd, mobiles, content, needstatus, product, extno, respType);
			map=JsonUtils.jsonToMap(returnsJson);
//			System.out.println(returnsJson);
			//TODO 处理返回值,参见HTTP协议文档
		} catch (Exception e) {			
			e.printStackTrace();
		}
		return map;
	}
	
	@RequestMapping("/edu3/recruit/exameeinfo/predistribution-list.html")
	public String listPredistribution(String recruitPlanId, String name, String enrolleeCode, String certNum, String examCertificateNo,HttpServletRequest request,Page objPage, ModelMap model) throws WebException{		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition = Condition2SQLHelper.getConditionFromResquestByIterator(request);
		String branchSchool="";
		User user = SpringSecurityHelper.getCurrentUser();
		if(user.getOrgUnit().getUnitType().indexOf(CacheAppManager.getSysConfigurationByCode("orgunit.brschool.unittype").getParamValue())>=0){//如果为校外学习中心人员
			branchSchool = user.getOrgUnit().getResourceid();
			model.addAttribute("isBrschool", true);
			
		}
		if(ExStringUtils.isNotEmpty(branchSchool)){
			condition.put("branchSchool", branchSchool);
		}		
		try {
			objPage = exameeInfoService.getPredistributionPage(objPage, condition);
			model.addAttribute("page", objPage);
		} catch (Exception e) {			
			e.printStackTrace();
		}
		
		model.addAttribute("condition", condition);
		return "/edu3/recruit/exameeinfo/predistribution-list";
	}
	
	@RequestMapping("/edu/recruit/exameeinfo/predistribution-apply.html")
	public void applyPredistribution(String resourceid,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){
			synchronized (this) {
				map = exameeInfoService.handleApplyDistribute(resourceid);
			}
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS,"申请分配教学点:"+resourceid );
		}else{
			map.put("message","操作失败！");
			map.put("stastusCode", 300);
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu/recruit/exameeinfo/predistribution-deleteApply.html")
	public void deleteApplyPredistribution(String resourceid,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){
			map = exameeInfoService.handleDeleteApplyDistribute(resourceid);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS,"撤销申请:"+resourceid );
		}else{
			map.put("message","操作失败！");
			map.put("stastusCode", 300);
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	
	@RequestMapping("/edu/recruit/exameeinfo/predistribution-audtiPass.html")
	public void auditPassPredistribution(String resourceid,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){
			map = exameeInfoService.handleApplyDistributeResult(resourceid,Predistribution.APPLY_PASS);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS,"审核通过:"+resourceid );
		}else{
			map.put("message","操作失败！");
			map.put("stastusCode", 300);
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
	@RequestMapping("/edu/recruit/exameeinfo/predistribution-audtiNopass.html")
	public void auditNoPassPredistribution(String resourceid,HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Map<String,Object> map = new HashMap<String, Object>();
		if(ExStringUtils.isNotBlank(resourceid)){
			map = exameeInfoService.handleApplyDistributeResult(resourceid,Predistribution.APPLY_NOPASS);
			UserOperationLogsHelper.saveUserOperationLogs(HttpHeaderUtils.getIpAddr(request), "2", UserOperationLogs.PASS,"审核不通过:"+resourceid );
		}else{
			map.put("message","操作失败！");
			map.put("stastusCode", 300);
		}
		renderJson(response,JsonUtils.mapToJson(map));
	}
}
